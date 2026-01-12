# React Native Fragment Lifecycle Bug - Reproduction Steps

This repository demonstrates a critical bug in React Native's Fragment lifecycle management when using `FragmentTransaction.add()` with multiple Fragments.

## The Bug

When multiple React Native Fragments share the same `ReactHost`:
1. Fragment B is destroyed and calls `ReactHost.onHostDestroy()`
2. This sets the activity reference to `null` in the shared `ReactContext`
3. Fragment A (still in backstack) now has a detached/null activity reference
4. Any subsequent operation requiring the activity will crash

## Steps to Reproduce

1. **Install dependencies**
   ```bash
   npm install
   cd android && ./gradlew clean
   cd ..
   ```

2. **Run the app**
   ```bash
   npx react-native run-android
   ```

3. **Follow the in-app instructions:**
   - Tap "Launch React Native Fragment A" from the native home screen
   - In Fragment A, tap "Go to Fragment B"
   - In Fragment B, Press back button to return to Fragment A
   - Observe the Toast and Logcat - it should show **"Activity: null (DETACHED)"**

## Expected Behavior

The Activity Status should show a valid MainActivity reference even after returning from Fragment B.

## Actual Behavior

The Activity Status shows `null`, indicating the ReactContext has lost its activity reference.

## Technical Details

### Key Files

- **CustomRNFragment.kt**: Custom Fragment that intentionally skips `onHostResume()` when returning from backstack to preserve the detached state
- **MainActivity.kt**: Uses `FragmentTransaction.add()` instead of `replace()` to preserve Fragment state
- **NavigationModule.kt**: Provides `checkActivityState()` to query the current activity reference
- **App.tsx**: React Native UI that displays the activity status

### Why This Happens

1. `FragmentTransaction.add()` keeps fragments in backstack without destroying them
2. When Fragment B is popped and destroyed, it calls `super.onDestroy()`
3. This triggers `ReactHost.onHostDestroy()`
4. `ReactContext` sets `mCurrentActivity = null`
5. Fragment A (still alive in backstack) now has no activity reference
6. Libraries like `react-native-screens` that call `requireActivity()` will crash

## Impact

This bug causes crashes in real-world apps that:
- Use `add()` to preserve Fragment state across navigation
- Have multiple React Native Fragments in the backstack
- Use React Navigation with native-stack (react-native-screens)
- Perform layout calculations or access Android activity context

## Proposed Fix

React Native should:
1. Use reference counting for ReactHost across multiple Fragments
2. Only call `onHostDestroy()` when all Fragments using it are destroyed
3. Or maintain per-Fragment activity references

## Environment

- React Native: 0.76+
- React Navigation: 7.x
- react-native-screens: 4.x
- New Architecture (Fabric): Enabled
- Platform: Android
