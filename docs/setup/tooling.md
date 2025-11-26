# Tooling Snapshot (Wear OS watch face)

- JDK: Temurin 17 LTS — install via https://adoptium.net/temurin/releases/ (required for Gradle/AGP 8.5.x).
- Android Gradle Plugin: 8.5.2 (see root `build.gradle.kts`) — release notes https://developer.android.com/build/releases/gradle-plugin.
- Gradle: 8.7 (minimum compatible with AGP 8.5.x) — https://gradle.org/releases/.
- Kotlin: 1.9.24 (as configured in `build.gradle.kts`) — https://kotlinlang.org/docs/releases.html#release-details.
- Android SDK / Target API: 34 (Android 14, aligns with Play requirement for Wear OS) — https://developer.android.com/google/play/requirements/target-sdk.
- Wear OS emulator images: Wear OS 5 (API 34, Android 14 base) and note Wear OS 6 lineage on API 35 — release notes https://developer.android.com/wear/release-notes.
- Command-line tools / Platform Tools: latest from https://developer.android.com/studio#command-tools to manage SDK 34 and Wear OS system images.
- Gradle wrapper: use the project wrapper when added (targeting Gradle 8.7) to ensure consistent builds on CI and local.
