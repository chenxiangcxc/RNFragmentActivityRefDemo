# Complete File Listing

All files in this reproduction repository with their purposes:

## Root Configuration Files

- **package.json** - npm/pnpm dependencies and scripts
- **tsconfig.json** - TypeScript configuration
- **babel.config.js** - Babel transpiler configuration
- **metro.config.js** - Metro bundler configuration
- **.gitignore** - Git ignore rules
- **index.js** - React Native entry point

## JavaScript/TypeScript Source Files

- **src/App.tsx** - Main React component with ScreenContainer/Screen and UI
- **src/NavigationModule.ts** - TypeScript interface for native navigation module

## Android Gradle Configuration

- **android/settings.gradle** - Gradle project settings
- **android/build.gradle** - Root Gradle build configuration
- **android/gradle.properties** - Gradle properties (AndroidX, Hermes, etc.)
- **android/gradle/wrapper/gradle-wrapper.properties** - Gradle wrapper version
- **android/app/build.gradle** - App module build configuration
- **android/app/proguard-rules.pro** - ProGuard rules (empty for debug)
- **android/app/debug.keystore** - Debug signing keystore (placeholder/note)

## Android Kotlin Source Files

- **android/app/src/main/java/com/rnscreenscrashrepro/MainApplication.kt** - Application class with ReactNativeHost
- **android/app/src/main/java/com/rnscreenscrashrepro/MainActivity.kt** - Main activity (extends FragmentActivity)
- **android/app/src/main/java/com/rnscreenscrashrepro/ReactFragmentA.kt** - Wrapper for ReactFragment A
- **android/app/src/main/java/com/rnscreenscrashrepro/ReactFragmentB.kt** - Wrapper for ReactFragment B
- **android/app/src/main/java/com/rnscreenscrashrepro/NativeFragment.kt** - Plain native fragment (control)
- **android/app/src/main/java/com/rnscreenscrashrepro/NavigationModule.kt** - Native module for navigation
- **android/app/src/main/java/com/rnscreenscrashrepro/NavigationPackage.kt** - React package for NavigationModule

## Android Resources

- **android/app/src/main/AndroidManifest.xml** - App manifest
- **android/app/src/main/res/layout/activity_main.xml** - Main activity layout
- **android/app/src/main/res/layout/fragment_container.xml** - Fragment container layout
- **android/app/src/main/res/layout/fragment_native.xml** - Native fragment layout
- **android/app/src/main/res/values/strings.xml** - String resources
- **android/app/src/main/res/values/styles.xml** - App theme styles
- **android/app/src/main/res/mipmap-*/ic_launcher.png** - App launcher icons (placeholders)
- **android/app/src/main/res/mipmap-*/ic_launcher_round.png** - Round launcher icons (placeholders)

## Documentation Files

- **README.md** - Main documentation with reproduction steps
- **SETUP_NOTES.md** - Additional setup instructions
- **PROJECT_STRUCTURE.txt** - Visual directory tree
- **COMPLETE_FILE_LIST.md** - This file

## Total Files

**Configuration:** 8 files
**JavaScript/TypeScript:** 2 files
**Android Gradle:** 7 files
**Android Kotlin:** 7 files
**Android Resources:** 9 files (+ icon placeholders)
**Documentation:** 4 files

**Grand Total:** ~37 files (excluding node_modules, build outputs, and icon variants)
