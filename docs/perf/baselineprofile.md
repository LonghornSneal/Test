# Baseline Profile Module (Task 10)

- **Module:** `baselineprofile/` (plugin `com.android.test`, target `:app`, self-instrumenting).
- **Instrumentation:** `androidx.benchmark.junit4.AndroidBenchmarkRunner` with `BaselineProfileRule` in `src/androidTest/java/com/cosmobond/watchface/baselineprofile/BaselineProfileGenerator.kt`.
- **Dependencies:** Benchmark macro + runner (`androidx.benchmark:benchmark-macro-junit4:1.2.4`, `androidx.benchmark:benchmark-junit4:1.2.4`), Test Runner/UiAutomator.
- **Emulator:** Wear OS API 34 x86_64 AVD `wear34` created via `avdmanager` (image `system-images;android-34;android-wear;x86_64`); launched headless (`-no-window -gpu swiftshader_indirect`).
- **Generation command:**  
  `JAVA_HOME=C:/Users/HhsJa/.jdks/temurin-17/jdk-17.0.17+10 ANDROID_SDK_ROOT=C:/Users/HhsJa/AppData/Local/Android/Sdk ./gradlew :baselineprofile:connectedNonMinifiedReleaseAndroidTest`
- **Output:** Baseline profile copied to `app/src/main/baseline-prof.txt` (source from `app/build/intermediates/combined_art_profile/release/compileReleaseArtProfile/baseline-prof.txt`).
- **Logs:** `art/perf/baselineprofile-connectedNonMinifiedReleaseAndroidTest.log`, `art/perf/baselineprofile-generateBaselineProfile.log` (first attempt failing to locate task), and release packaging confirmation `art/perf/app-assembleRelease.log`.
- **Notes:** Baseline profile plugin warns about AGP 8.5.2 compatibility; keep 1.2.4 with `connectedNonMinifiedReleaseAndroidTest` until an updated plugin is available, then upgrade. Cmdline-tools installed under `cmdline-tools/latest-2` due to existing install; `ANDROID_SDK_ROOT` set explicitly during commands. Regenerate the profile after each renderer tick/power change using the connected task above when a Wear AVD or device is available.
- **Current status (2025-12-02):** Existing `app/src/main/baseline-prof.txt` retained; regeneration deferred until a Wear emulator/device is available in this environment.

## CI automation (Task 15 - 2025-12-01)
- Nightly workflow `.github/workflows/baselineprofile-nightly.yml` runs at 07:00 UTC (plus manual dispatch) on `ubuntu-latest`, boots a Wear OS API 34 x86_64 emulator via `ReactiveCircus/android-emulator-runner`, and executes `./gradlew --stacktrace :baselineprofile:generateBaselineProfile`.
- The `generateBaselineProfile` Gradle task (registered in `baselineprofile/build.gradle.kts`) depends on `connectedNonMinifiedReleaseAndroidTest` and copies the first generated `baseline-prof.txt` under `app/build` into `app/src/main/baseline-prof.txt` for packaging; no auto-commits.
- Artifacts uploaded from the workflow live under `artifacts/baselineprofile/` and include the copied baseline profile plus `baselineprofile/build/outputs/macrobenchmark` for perf metrics.
