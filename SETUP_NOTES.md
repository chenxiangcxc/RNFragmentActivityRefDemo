# Setup Notes

## Post-Clone Steps

After cloning this repository, follow these steps:

### 1. Install Dependencies

```bash
pnpm install
```

### 2. Generate Launcher Icons (Optional)

The mipmap launcher icon files are placeholders. To add proper icons, you have two options:

**Option A - Copy from a fresh React Native project:**
```bash
# Create a temporary RN project elsewhere
npx @react-native-community/cli@latest init TempProject
# Copy the mipmap folders
cp -r TempProject/android/app/src/main/res/mipmap-* android/app/src/main/res/
```

**Option B - Use this project as-is:**

The app will run fine with the placeholder icon files. Android will use a default icon.

### 3. Run the Project

```bash
# Terminal 1 - Start Metro
pnpm start

# Terminal 2 - Run Android
pnpm android
```

## Troubleshooting

### Gradle Issues

If you encounter Gradle sync issues:

```bash
cd android
./gradlew clean
cd ..
pnpm android
```

### Metro Cache Issues

If JavaScript changes aren't reflecting:

```bash
pnpm start --reset-cache
```

### Device/Emulator Not Found

Make sure you have:
- An Android emulator running, OR
- A physical device connected with USB debugging enabled

Check with:
```bash
adb devices
```

## Quick Verification

After running the app, you should see:
- Blue header labeled "Fragment A"
- A counter showing "0"
- Three buttons for testing
- Yellow instructions box at the bottom

If you see this, the setup is correct!
