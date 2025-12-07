# Phone-to-watch install playbook (Galaxy Watch8 Classic)

## Quick path: update your watch from your phone
1. **Fastest single step (CI → watch):** on the phone, run the new helper to download the latest CI APK and push it straight to the watch at `10.195.4.92:36965`:
   ```bash
   export GH_TOKEN=<token_with_repo_access>
   export GH_REPO=<owner/repo> # skip if git remote origin is set
   sh tools/update-watch-latest.sh 10.195.4.92:36965 debug
   ```
   - Swap `debug` with `release` if you have upload-key signing available and prefer the release build.
   - The script resolves the newest successful `Build installable artifacts` run, downloads the matching APK to a temp folder, and installs it over Wi‑Fi ADB in one go.
2. **If you want to keep the files:** use the original fetcher, then choose how to deliver:
   ```bash
   export GH_TOKEN=<token_with_repo_access>
   export GH_REPO=<owner/repo> # skip if git remote origin is set
   sh tools/fetch-build-artifacts.sh ~/Downloads/cosmobond-build
   ```
   - The helper script pulls the newest successful workflow run and downloads available artifacts into `~/Downloads/cosmobond-build/`.
   - If the repo remote is already configured, `GH_REPO` is optional. You can rerun the script after every code update to always grab the latest build.
3. Pick your install method (ADB sideload or Play upload) from the sections below and point it at the downloaded APK/AAB.
4. After installing, open **CosmoBond** on the watch, save once, and confirm the face updates without reopening the picker (see validation checklist at the bottom).

### One-command sideload to a paired watch (ADB over Wi‑Fi)
- Requirements: `adb` on the phone (e.g., `pkg install android-tools` in Termux) and Wi‑Fi debugging enabled on the watch.
- Command: after fetching artifacts, run
  ```bash
  sh tools/install-to-watch.sh 10.195.4.92:36965 ~/Downloads/cosmobond-build/app-debug.apk
  ```
  Replace the IP:port with the one shown on your watch if it changes; swap in the release APK if you have signing in place. The script connects and installs in one step.

## CI artifacts you can grab from your phone
- **Workflow:** `Build installable artifacts` (`.github/workflows/build-artifacts.yml`). Trigger it from the GitHub mobile app or mobile browser via **Actions → Build installable artifacts → Run workflow**.
- **Outputs and paths:**
  - `cosmobond-debug-apk` → `app/build/outputs/apk/debug/app-debug.apk` (sideload-friendly, debug-signed).
  - `cosmobond-release-apk` → `app/build/outputs/apk/release/app-release.apk` (release build, signed with upload key when secrets are present, otherwise debug keystore).
  - `cosmobond-release-aab` → `app/build/outputs/bundle/release/app-release.aab` (use for Play Console uploads/App Sharing).
- **PR checks:** `PR Checks` workflow still runs lint/tests/emulator but does **not** publish installables. Use the installable workflow when you need binaries.

## Play Console private tracks (internal/closed testing)
1. From your phone, open [Play Console](https://play.google.com/console/) in Chrome and pick the CosmoBond app.
2. Ensure **App signing by Google Play** is enabled (blocked in this repo’s checklist until verification finishes). If upload signing is still pending, keep using the debug APK for sideload tests.
3. Go to **Testing > Internal testing** (or **Closed testing**) and create a new release.
4. Tap **Upload** and pick `cosmobond-release-aab` from your phone’s Downloads (pulled from the workflow artifact).
5. Add yourself as a tester (email/Gmail group), review release notes, and roll out. Google will serve the build privately to the invitee list only.
6. On the watch (signed in with the tester account), open Play Store → **My apps** → **Beta** to install/update. Expect a few minutes of propagation after upload.

## Play App Sharing (quick, invite-only)
1. In Play Console, open **Internal app sharing**.
2. Upload the same `cosmobond-release-aab` from your phone.
3. Copy the generated sharing link and open it on the phone with the same Google account. Accept the warning and install to the watch via Play Services (keeps distribution private and revocable).

## Direct sideload from phone to watch (ADB over Wi‑Fi)
1. On the watch: enable Developer Options → toggle **ADB debugging** and **Debug over Wi‑Fi**. Note the IP:port shown (e.g., `192.168.0.14:5555`).
2. On the phone: install **Termux** (or another Android shell) and run `pkg install android-tools` to get `adb`.
3. Download `cosmobond-debug-apk` (or `cosmobond-release-apk`) from the workflow artifact to your phone’s storage.
4. Connect: `adb connect <watch_ip:port>` then `adb devices` to verify the watch is listed.
5. Install: `adb -s <watch_ip:port> install -r /path/to/app-debug.apk`. If Play signing keys are unavailable, prefer the debug APK.
6. Optional: capture logs with `adb -s <watch_ip:port> logcat -s CosmoBond` while testing.

## Watchface visibility and sanity checks (watch-only)
- The watch face service is registered in `AndroidManifest.xml` (`CosmoBondWatchFaceService` with `WATCH_FACE` category), so it should appear under **My watch faces** after install.
- After sideload/install:
  1. Long-press the watch face → **My watch faces** → pick **CosmoBond** if not already active.
  2. Launch **CosmoBond** from the app list to open the on-watch layout screen. Save once to persist defaults; the app will toast and return without launching the picker.
  3. Reopen the layout screen, adjust fonts/date format/sizes with the chips + sliders, save, and confirm the running face updates live (time/date positions and typography reflect the changes).
  4. Verify the face remains in **My watch faces** and the app never forces the picker after saving.
