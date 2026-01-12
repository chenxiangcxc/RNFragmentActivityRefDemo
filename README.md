# React Native Fragment Activity Reference Bug Reproduction

Minimal reproduction repository for **react-native-screens**: React Native fragments lose Activity reference when navigating between fragments in Android.

## Issue Summary

When an Android `FragmentActivity` hosts multiple `ReactFragment` instances and navigates between them, the React Native fragments lose their Activity reference (`reactContext.currentActivity` becomes null) after returning to the first fragment.

**Bug symptoms:**
- `reactContext.currentActivity` returns `null` after fragment navigation
- React Native bridge calls fail because Activity reference is lost
- UI interactions may become unresponsive
- Toast messages show "Activity: null (DETACHED - This is the bug!)"

## Project Setup

### Prerequisites

- Node.js 18+
- npm (comes with Node.js)
- Android compile SDK version 36
- JDK 17

### Installation

1. Clone this repository
2. Install dependencies:
   ```bash
   npm install
   ```

3. For Android, ensure you have:
   - Android Studio installed
   - Android SDK configured
   - An Android emulator or physical device

### Running the App

1. Start Metro bundler:
   ```bash
   npm start
   ```

2. In another terminal, run Android:
   ```bash
   npm run android
   ```

### Troubleshooting

**Gradle build issues:**
```bash
cd android
./gradlew clean
cd ..
npm run android
```

**Metro cache issues:**
```bash
npm start -- --reset-cache
```

**Device not found:**
- Ensure you have an Android emulator running, OR
- Connect a physical device with USB debugging enabled
- Verify with: `adb devices`

## Bug Reproduction Steps

### How to Reproduce the Activity Reference Loss

1. **Launch the app** - Fragment A appears with instructions
2. **Click "Go to Fragment B"** button
3. Fragment B appears with purple header
4. **Press the Android Back button** to return to Fragment A
5. **Watch the logcat output and toast messages**
6. **Bug appears** ⚠️ - Activity status shows "Activity: null (DETACHED - This is the bug!)"

### What You'll See

The app automatically polls and displays the Activity state every 500ms:

- **Before navigation**: Toast and logs show `Activity: MainActivity@<hashcode>` ✅
- **After returning from Fragment B**: Toast and logs show `Activity: null (DETACHED)` ❌
- **Button may become unresponsive**: The "Go to Fragment B" button won't work because the Activity reference is lost

## Technical Details

### Architecture

- **MainActivity**: Extends `FragmentActivity` (not `ReactActivity`)
- **ReactFragmentA** & **ReactFragmentB**: Wrapper fragments that host `com.facebook.react.ReactFragment`
- **Single ReactNativeHost**: Shared across both fragments

### Why Activity Reference Is Lost

The bug occurs because:

1. **Fragment A** creates a React Native fragment and gets proper Activity reference
2. When **Fragment B** is pushed using `FragmentTransaction.add()`, Fragment A is moved to the back stack but not destroyed
3. When Fragment B is destroyed (via back button), it calls `ReactHost.onHostDestroy()`
4. This sets `mCurrentActivity = null` in the shared `ReactContext`
5. **Fragment A** (still alive in backstack) now has a `null` activity reference
6. All native module calls and libraries that require Activity (like `react-native-screens`) fail or crash

**Key technical issue:**
- Multiple fragments share the same `ReactHost` and `ReactContext`
- React Native doesn't use reference counting for fragments
- When one fragment destroys the host, it affects all other fragments using the same context

### Monitoring the Bug

The app includes automatic monitoring:
- **Auto-polling**: Every 500ms, checks `reactContext.currentActivity` state
- **Toast notifications**: Shows Activity status on screen
- **Logcat output**: Detailed logging with tags `[Fragment A]` and `NavigationModule`
- **Native bridge**: `checkActivityState()` method to query Activity from JS side

## Expected Behavior

The Activity reference should remain valid throughout fragment navigation. After returning to Fragment A, `reactContext.currentActivity` should still point to MainActivity, not become null.

## Potential Solutions

React Native could address this by:
1. **Reference counting**: Track how many fragments are using a `ReactHost` and only call `onHostDestroy()` when count reaches zero
2. **Per-fragment activity references**: Maintain separate activity references for each fragment
3. **Lifecycle coordination**: Better coordinate fragment lifecycle with React context lifecycle

## Environment

- React Native: 0.83.1
- react-native-screens: 4.19.0
- @react-navigation/native: 7.1.14
- @react-navigation/native-stack: 7.3.21
- Android minSdk: 27
- Android targetSdk: 36
- Kotlin: 2.3.0

## Debug Output Example

**Good state (before navigation):**
```
[Fragment A] Auto-check - Activity status: Activity: MainActivity@a1b2c3d
```

**Bad state (after back navigation):**
```
[Fragment A] Auto-check - Activity status: Activity: null (DETACHED - This is the bug!)
```

## License

MIT - This is a minimal reproduction repository for debugging purposes.
