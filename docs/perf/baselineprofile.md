# Baseline Profile Module (Task 10)

- **Module:** `baselineprofile/` (plugin `com.android.test`, target `:app`, self-instrumenting).
- **Instrumentation:** `androidx.benchmark.junit4.AndroidBenchmarkRunner` with `BaselineProfileRule` in `src/androidTest/java/com/cosmobond/watchface/baselineprofile/BaselineProfileGenerator.kt`.
- **Dependencies:** Benchmark macro + runner (`androidx.benchmark:benchmark-macro-junit4:1.2.4`, `androidx.benchmark:benchmark-junit4:1.2.4`), Test Runner/UiAutomator.
- **Emulator:** Wear OS API 34 x86_64 AVD `wear34` created via `avdmanager` (image `system-images;android-34;android-wear;x86_64`); launched headless (`-no-window -gpu swiftshader_indirect`).
- **Generation command:**  
  `JAVA_HOME=C:/Users/HhsJa/.jdks/temurin-17/jdk-17.0.17+10 ANDROID_SDK_ROOT=C:/Users/HhsJa/AppData/Local/Android/Sdk ./gradlew :baselineprofile:connectedNonMinifiedReleaseAndroidTest`
- **Output:** Baseline profile copied to `app/src/main/baseline-prof.txt` (source from `app/build/intermediates/combined_art_profile/release/compileReleaseArtProfile/baseline-prof.txt`).
- **Logs:** `art/perf/baselineprofile-connectedNonMinifiedReleaseAndroidTest.log`, `art/perf/baselineprofile-generateBaselineProfile.log` (first attempt failing to locate task), and release packaging confirmation `art/perf/app-assembleRelease.log`.
- **Notes:** Release build temporarily uses debug signing to allow emulator installs for baseline capture until real signing is configured; update once CI signing is wired. Cmdline-tools installed under `cmdline-tools/latest-2` due to existing install; `ANDROID_SDK_ROOT` set explicitly during commands.
