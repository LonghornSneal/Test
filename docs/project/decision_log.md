# Decision Log

## 2025-11-26 — Task 1: Guardrail audit
- Reviewed guardrail files (`README.md`, `CODEOWNERS`, `CONTRIBUTING.md`, `LICENSE`, `.gitignore`, `.editorconfig`, `.gitattributes`).
- Findings / follow-ups:
  - `README.md`: Add local run commands (lint/tests/build) and link to the active checklist; consider adding CI status badge once workflows settle.
  - `CODEOWNERS`: Single default owner `@cosmobond/watchface-maintainers`; verify team exists and add path-specific owners if different maintainers cover docs/art assets.
  - `CONTRIBUTING.md`: Lacks explicit command block for required checks (spotless/detekt/lint/tests) and no reference to code of conduct or PR templates—expand to reduce ambiguity.
  - `.gitignore`: Core entries present; consider adding `*.aab`, `app/src/main/baseline-prof.txt`, and secret/env files to avoid accidental commits.
  - `.gitattributes`: Treats common image formats as binary; add media types used in this repo (e.g., `*.mp4`) to prevent line-ending normalization.
- Branch protection: Unable to retrieve `main` protection via GitHub API without credentials (`curl .../branches/main/protection` returned 401). Request an admin to confirm settings in GitHub → Settings → Branches → Branch protection rules for `main` and enable required status checks (spotlessCheck, detekt, lint, testDebugUnitTest, connectedDebugAndroidTest), PR review requirements, and branch up-to-date enforcement; capture a screenshot when available.
- Markdown lint: `npx markdownlint-cli "**/*.md"` (2025-11-26) reported existing formatting/line-length violations across the repo (no files auto-fixed; exit code non-zero); no scope changes applied in this task.

## 2025-11-26 — Task 2: Tooling snapshot
- Documented required tooling in `docs/setup/tooling.md` with pinned/pending versions and links: JDK 17 (Temurin), AGP 8.5.2, Gradle 8.7 (minimum for AGP 8.5), Kotlin 1.9.24, Android SDK/Target API 34, Wear OS 5 emulator images (API 34) with Wear OS 6 lineage note (API 35), platform tools/CLT download, and wrapper usage note.
- Source confirmation: AGP 8.5.2 and Kotlin 1.9.24 pulled from root `build.gradle.kts`; min/target SDK 34 from `app/build.gradle.kts`.
- Play target note: Doc references Play’s target API 34 requirement for Wear OS.
- Markdown lint: `npx markdownlint-cli "**/*.md"` rerun (2025-11-26) still fails on pre-existing long-line/formatting issues across the repo (exit code non-zero, no auto-fixes); newly added doc also flagged for bare URLs/line length. Will resolve in a follow-up pass when markdown rules are triaged.

## 2025-11-26 — Task 3: Rendering track decision
- Verified rendering config: `app/build.gradle.kts` sets `minSdk = 34`, `targetSdk = 34`, `compileSdk = 34`; Kotlin/JDK/AGP versions align with WFF expectations (Kotlin 1.9.24, AGP 8.5.2, JDK 17 from root `build.gradle.kts`).
- Confirmed WFF path: `app/src/main/AndroidManifest.xml` points to `@raw/watchface` via `androidx.wear.watchface.wff` metadata; `CosmoBondWatchFaceService` remains a lightweight host.
- Documented the decision in `docs/tech/architecture.md`: chose Watch Face Format v2 + Kotlin host; cited Google WFF overview (developer.android.com/training/wearables/watch-faces/watch-face-format) and Samsung WFF overview (developer.samsung.com/watch-face/face-format/overview.html); noted downstream tasks continue on WFF track (layout, complications, ambient, screenshots) without a Kotlin-rendered canvas unless we pivot.
- Markdown lint: `npx markdownlint-cli "**/*.md"` rerun (2025-11-26) still fails on existing long-line/bare-URL issues across the repo (no auto-fixes applied).

## 2025-11-26 — Task 4: Scaffold validation and debug build
- Gradle wrapper added (8.7) via `gradle/wrapper/gradle-wrapper.properties` and `gradlew*`; verified SHA256 pin. Root plugins remain AGP 8.5.2, Kotlin 1.9.24.
- Installed Android SDK locally (platform-tools, build-tools 34.0.0, platforms;android-34) and set `local.properties` to `sdk.dir=C:/Users/HhsJa/AppData/Local/Android/Sdk` to satisfy build.
- Adjusted Play Publisher block in `app/build.gradle.kts` to skip missing creds locally; downgraded watchface deps to `1.2.1` (1.3.x artifacts unavailable on Google/Maven at run time).
- Satisfied build by stubbing `CosmoBondWatchFaceService` as a basic `Service` host (no renderer yet); manifest still references `@raw/watchface`.
- `./gradlew :app:assembleDebug` succeeded with JDK 17; log captured at `docs/project/logs/phase1-task4-build.txt`. Resulting APK at `app/build/outputs/apk/debug/app-debug.apk`.
- Follow-ups: remove `package` attribute from `app/src/main/AndroidManifest.xml` (AGP warning), revert to a proper Watch Face host implementation once WFF/Kotlin track finalized, and consider bumping watchface libs when the 1.3.x artifacts are available.
- Markdown lint: not rerun post-build; outstanding repo-wide issues remain (long lines/bare URLs).

## 2025-11-26 - Task 5: WFF layout expansion & release build
- Expanded `app/src/main/res/raw/watchface.xml` with date text and two SHORT_TEXT complications (left/right) positioned near the bottom; adjusted time positioning for better balance.
- Added preview drawable `app/src/main/res/drawable-nodpi/watchface_preview.xml` and wired service metadata (`androidx.wear.watchface.preview`) in `app/src/main/AndroidManifest.xml`; removed manifest `package` attribute per AGP guidance.
- Release build succeeded via `./gradlew :app:assembleRelease`; log stored at `docs/project/logs/phase1-task5-build.txt`. Output APK at `app/build/outputs/apk/release/app-release-unsigned.apk`.
- Dependencies: watchface libs remain pinned to `1.2.1` (newer artifacts still not resolving at build time).
- Follow-ups: implement a proper WatchFaceService host (currently stub Service), add `watch_face_config.xml` if needed for WFF config, and consider richer WFF visuals plus preview screenshots when design assets are ready.
- Markdown lint: not rerun for this task; existing repo-wide failures persist.

## 2025-11-27 - Task 9: Screenshot tests for watch face previews
- Added a Paparazzi-based screenshot suite (`WatchFaceScreenshotTest`) using deterministic time and palette states; renders the Kotlin canvas watch face via a fake SurfaceHolder to avoid native surface deps and captures interactive cosmic blue, interactive starlight, and ambient frames.
- Goldens recorded under `app/src/test/snapshots/images/` and mirrored to `app/src/androidTest/assets/goldens/`; evidence copies live in `docs/qa/screenshots/task9/`.
- Commands executed with `JAVA_HOME=C:/Users/HhsJa/.jdks/temurin-17/jdk-17.0.17+10`: `./gradlew :app:recordPaparazziDebug` (log `docs/qa/screenshots/task9-paparazzi-record.log`) and `./gradlew :app:verifyPaparazziDebug` (log `docs/qa/screenshots/task9-paparazzi-verify.log`).
- CI note: add `:app:verifyPaparazziDebug` plus Paparazzi HTML report (`app/build/reports/paparazzi/debug/index.html`) as PR artifacts in `.github/workflows/android.yml`; keep `app/src/test/snapshots` and `app/src/androidTest/assets/goldens` in sync when updating goldens.
- Markdown lint: `npx markdownlint-cli "**/*.md"` (log `docs/qa/screenshots/task9-markdownlint.log`, 2025-11-27) fails on existing long-line/bare-URL/style issues across the repo; initial `npx markdownlint` alias could not resolve an executable. No auto-fixes applied.
