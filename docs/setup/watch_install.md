# Install the CosmoBond watch face on a Wear OS watch (Wi‑Fi)

Use this when you have a built APK (debug or release) and need to sideload it to a Wi‑Fi connected Wear OS watch. The steps below are phone-friendly and use `adb` over Wi‑Fi.

## Prerequisites
- Wear OS watch with **ADB debugging** and **Debug over Wi‑Fi** turned on. Note the IP:port shown on the watch (e.g., `10.195.4.92:36965`).
- `adb` installed on your computer or Android device (e.g., via Android Platform Tools or Termux pkg).
- The CosmoBond APK available locally (default path: `app/build/outputs/apk/debug/app-debug.apk`). If you only have the workflow artifact, download the APK from the run and place it anywhere on your device.

## Quick install
1. Make sure the APK is present locally. If you need to build one yourself, run:
   ```bash
   ./gradlew :app:assembleDebug
   ```
2. Run the helper script with your watch IP:port (default APK path shown above):
   ```bash
   ./tools/install_watch.sh 10.195.4.92:36965
   ```
   - To point at a different APK (e.g., a release build), pass it as the second argument:
     ```bash
     ./tools/install_watch.sh 10.195.4.92:36965 path/to/app-release.apk
     ```
3. After the install finishes, the script sets CosmoBond as the active watch face. If it does not appear immediately, wake the watch and swipe to select it.

## Manual commands (if you cannot run the script)
If you prefer to type the commands yourself:
```bash
adb connect 10.195.4.92:36965
adb -s 10.195.4.92:36965 install -r app/build/outputs/apk/debug/app-debug.apk
adb -s 10.195.4.92:36965 shell cmd watchface set com.cosmobond.watchface/.CosmoBondWatchFaceService
```

> Tip: Keep the watch screen awake during install to avoid Wi‑Fi debugging from pausing.
