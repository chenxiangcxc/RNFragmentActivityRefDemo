# React Native Screens Crash Reproduction

Minimal reproduction repository for **react-native-screens issue #2872**: Android crash when using two `ReactFragment` instances in a `FragmentActivity`.

## Issue Summary

When an Android `FragmentActivity` hosts multiple `ReactFragment` instances and navigates between them, the app crashes inside `react-native-screens` after returning to the first fragment and triggering a React state update.

**Error typically seen:**
- Context detached errors
- `ScreenDummyLayoutHelper` related crashes
- React Native screens unable to access proper context after fragment navigation

## Project Setup

### Prerequisites

- Node.js 18+
- pnpm 9.0+
- Android SDK with API 26+
- JDK 17

### Installation

1. Clone this repository
2. Install dependencies:
   ```bash
   pnpm install
   ```

3. For Android, ensure you have:
   - Android Studio installed
   - Android SDK configured
   - An Android emulator or physical device

### Running the App

1. Start Metro bundler:
   ```bash
   pnpm start
   ```

2. In another terminal, run Android:
   ```bash
   pnpm android
   ```

## Crash Reproduction Steps

### Scenario 1: React Fragment Navigation (CRASHES)

This scenario reproduces the crash:

1. **Launch the app** - Fragment A appears with blue header
2. **Press "Push React Fragment B (Will Crash)"** button
3. Fragment B appears with red header
4. **Press the Android Back button** to return to Fragment A
5. **Press "Update State"** button in Fragment A
6. **App crashes** 💥

Expected crash location: Inside `react-native-screens` library, typically related to:
- Detached React context
- `ScreenDummyLayoutHelper` trying to access destroyed context
- Native module bridge errors

### Scenario 2: Native Fragment Navigation (NO CRASH)

This is the control scenario that should NOT crash:

1. **Launch the app** - Fragment A appears with blue header
2. **Press "Push Native Fragment (No Crash)"** button
3. A green native fragment appears (no React content)
4. **Press the Android Back button** to return to Fragment A
5. **Press "Update State"** button in Fragment A
6. **No crash** ✅ - App continues to work normally

## Technical Details

### Architecture

- **MainActivity**: Extends `FragmentActivity` (not `ReactActivity`)
- **ReactFragmentA** & **ReactFragmentB**: Wrapper fragments that host `com.facebook.react.ReactFragment`
- **Single ReactNativeHost**: Shared across both fragments
- **react-native-screens**: Uses `ScreenContainer` and `Screen` components with `enableScreens()`

### Key Code Locations

**JavaScript:**
- `src/App.tsx` - Main React component with `ScreenContainer` + `Screen`
- `src/NavigationModule.ts` - TypeScript interface for native module

**Android (Kotlin):**
- `android/app/src/main/java/com/rnscreenscrashrepro/MainActivity.kt` - Manages fragment transactions
- `android/app/src/main/java/com/rnscreenscrashrepro/ReactFragmentA.kt` - Wraps ReactFragment for Fragment A
- `android/app/src/main/java/com/rnscreenscrashrepro/ReactFragmentB.kt` - Wraps ReactFragment for Fragment B
- `android/app/src/main/java/com/rnscreenscrashrepro/NavigationModule.kt` - Native module for navigation
- `android/app/src/main/java/com/rnscreenscrashrepro/NativeFragment.kt` - Plain native fragment (control)

### Why It Crashes

The crash occurs because:

1. **Fragment A** creates a `ReactFragment` with `react-native-screens` components
2. When **Fragment B** is pushed, Fragment A's view hierarchy is destroyed but some screen components remain registered
3. After returning to **Fragment A**, the view is recreated
4. **State update** triggers a re-render in `react-native-screens`
5. `react-native-screens` tries to access context/views that were destroyed, causing a crash

The native fragment scenario doesn't crash because it doesn't use React or `react-native-screens`.

## Expected Behavior

Both scenarios should work without crashing. Fragment navigation should properly clean up and restore React Native screen components.

## Environment

- React Native: 0.76.5
- react-native-screens: 4.4.0
- Android minSdk: 26
- Android targetSdk: 35
- Kotlin: 1.9.24

## Related Issues

- [react-native-screens #2872](https://github.com/software-mansion/react-native-screens/issues/2872)

## License

MIT - This is a minimal reproduction repository for debugging purposes.
