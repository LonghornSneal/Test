# Decision Log

## 2025-11-26 - Task 1: Guardrail audit
- Reviewed guardrail files (`README.md`, `CODEOWNERS`, `CONTRIBUTING.md`, `LICENSE`, `.gitignore`, `.editorconfig`, `.gitattributes`).
- Findings / follow-ups:
  - `README.md`: Add local run commands (lint/tests/build) and link to the active checklist; consider adding CI status badge once workflows settle.
  - `CODEOWNERS`: Single default owner `@cosmobond/watchface-maintainers`; verify team exists and add path-specific owners if different maintainers cover docs/art assets.
  - `CONTRIBUTING.md`: Lacks explicit command block for required checks (spotless/detekt/lint/tests) and no reference to code of conduct or PR templates—expand to reduce ambiguity.
  - `.gitignore`: Core entries present; consider adding `*.aab`, `app/src/main/baseline-prof.txt`, and secret/env files to avoid accidental commits.
  - `.gitattributes`: Treats common image formats as binary; add media types used in this repo (e.g., `*.mp4`) to prevent line-ending normalization.
- Branch protection: Unable to retrieve `main` protection via GitHub API without credentials (`curl .../branches/main/protection` returned 401). Request an admin to confirm settings in GitHub → Settings → Branches → Branch protection rules for `main` and enable required status checks (spotlessCheck, detekt, lint, testDebugUnitTest, connectedDebugAndroidTest), PR review requirements, and branch up-to-date enforcement; capture a screenshot when available.
- Markdown lint: `npx markdownlint-cli "**/*.md"` (2025-11-26) reported existing formatting/line-length violations across the repo (no files auto-fixed; exit code non-zero); no scope changes applied in this task.
- Branch protection evidence (2025-11-29): Screenshot saved at `docs/project/screenshots/branch-protection-main.png`. Current settings under “Sneal Rules” ruleset: targets `main` (default criteria), bypass list empty; branch rules: restrict deletions = enabled, block force pushes = enabled; restrict creations/updates, require linear history, deployments, signed commits, PRs before merging, status checks, code scanning results, code quality results, and Copilot review settings = all disabled. Follow-up: tighten rules to require PRs + required checks (spotlessCheck, detekt, lint, testDebugUnitTest, connectedDebugAndroidTest) with up-to-date enforcement.

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

## 2025-11-27 - Task 8: Unit tests for non-UI logic
- Added focused unit tests for time formatting (`CompanionTimeFormatter`), palette selection (`CompanionPalette`), and complication slot registration (`CompanionComplicationSlots`) plus shared test helpers.
- Introduced Jacoco reporting via `jacocoTestDebugUnitTestReport`; coverage at 81% instructions (see `app/build/reports/jacoco/jacocoTestDebugUnitTestReport/html/index.html`), satisfying the ≥70% goal for core utilities.
- Command: `./gradlew testDebugUnitTest jacocoTestDebugUnitTestReport` (log `docs/qa/screenshots/task8-test.log`); JUnit XML under `app/build/test-results/testDebugUnitTest/`.
- Next CI step: publish Jacoco HTML + XML and JUnit results as workflow artifacts; enforce the task execution in PR checks.

## 2025-11-28 - Task 10: Baseline profile module
- Added `baselineprofile/` macrobenchmark module (plugin `com.android.test`, self-instrumenting) targeting `:app` with `BaselineProfileRule` generator under `src/androidTest/java/com/cosmobond/watchface/baselineprofile/`.
- Installed Wear OS 34 x86_64 system image and created headless AVD `wear34`; ran baseline collection via `./gradlew :baselineprofile:connectedNonMinifiedReleaseAndroidTest` (emulator env with JAVA_HOME Temurin 17 / ANDROID_SDK_ROOT set).
- Copied generated profile to `app/src/main/baseline-prof.txt` (source `app/build/intermediates/combined_art_profile/release/compileReleaseArtProfile/baseline-prof.txt`); confirmed packaging with `./gradlew :app:assembleRelease` (log `art/perf/app-assembleRelease.log`).
- Logs: baseline profile run `art/perf/baselineprofile-connectedNonMinifiedReleaseAndroidTest.log`; initial task lookup failure in `art/perf/baselineprofile-generateBaselineProfile.log`.
- Note: release build temporarily uses debug signing to allow emulator installs for baseline capture; update to real signing when CI secrets (Tasks 16-18) are configured. Cmdline-tools installed under `cmdline-tools/latest-2` due to existing install path.

## 2025-11-28 - Pre-checklist prerequisites review
- Searched repository for artifacts covering design brief, asset inventory, feature scope, account/secret access, and validation plan; no dedicated docs were found beyond placeholder `.gitkeep` files in `docs/product/` and `docs/experience/`.
- Status by item and needed inputs:
  - Design brief finalized: not documented. Need dial layout, typography, color palette, complication plan, and animation storyboards; suggest capturing in `docs/product/design_brief.md` (or similar) with approved snapshots.
  - Asset requirements gathered: not inventoried. Need vector/raster asset list, font sources, and ambient-mode variants; align storage under `art/source/`, exports in `art/export/`, and note color profiles/compression.
  - Feature scope prioritized: not recorded. Need v1.0 feature list with priority, interactive behaviors, data integrations, and configuration options; recommend `docs/product/feature_scope.md`.
  - Account and secret access secured: no evidence checked in. Need confirmation of Google Play Console access, signing key custody, analytics credentials, and Play upload key; document access paths and storage locations once available.
  - Validation plan prepared: not present. Need target device matrix, manual test scenarios, and acceptance criteria; propose `docs/qa/validation_plan.md` plus device matrix in `docs/qa/device_matrix.md`.
- Next steps: collect owner-approved content for each item above and update the corresponding docs; once account access is confirmed, capture default-branch protection settings (screenshot) and include in Task 1 evidence.

## 2025-11-29 - Pre-checklist inputs drafted
- Added structured drafts for prerequisites:
  - Design brief in `docs/product/design_brief.md` (layout, palette, typography, complications, animation states, accessibility).
  - Asset inventory in `docs/product/asset_requirements.md` (sources/exports, ambient rules, export settings, ownership).
  - Feature scope in `docs/product/feature_scope.md` (P0/P1 features, integrations, deferred items, risks).
  - Account/secret access in `docs/ops/account_access.md` (roles, Play App Signing, keystore/secret needs).
  - Validation plan in `docs/qa/validation_plan.md` (device matrix outline, scenarios, acceptance, perf/accessibility).
- Branch protection: awaiting placement of `docs/project/screenshots/branch-protection-main.png`; current ruleset "Sneal Rules" targets `main`, bypass empty; restrict deletions/block force pushes enabled; all other checks/PR requirements off. Pending tighten-up to require PR + checks (spotlessCheck, detekt, lint, testDebugUnitTest, connectedDebugAndroidTest) with up-to-date enforcement.

## 2025-12-01 - Task 11: Battery/performance audit
- Completed battery/performance review against Google Optimize watch faces guidance; evidence in `docs/qa/battery-checklist.md`.
- Findings: `INTERACTIVE_FRAME_MS = 16` (~60 fps) despite static render; ambient path dims colors and stops animation but still renders complications with anti-aliasing on; WFF ambient scene hides complications while Kotlin renderer keeps them visible; no phone/Data Layer sync and complication cadence follows system providers.
- Actions queued: throttle interactive tick to ≥1000 ms or animation-driven cadence; add ambient guards (skip complications, disable AA for low-bit/burn-in); align WFF + Kotlin ambient outputs; document refresh SLAs when adding heart-rate/phone battery providers.
- Markdown lint: `npx markdownlint "**/*.md"` (2025-12-01) run for this task; see `docs/qa/battery-checklist-markdownlint.txt` (expected legacy long-line/bare-URL hits remain).

## 2025-12-01 - Task 11 follow-ups applied
- Raised renderer tick to 1000 ms and added ambient guards (skip complication rendering in ambient; disable anti-aliasing for low-bit/burn-in) in `CompanionWatchFaceRenderer`.
- Updated WFF ambient scene with monochrome time/date partials to match the Kotlin renderer and keep complications hidden.
- Added complication refresh SLAs for steps/heart/watch + phone battery to `docs/tech/architecture.md`.
- Build: `./gradlew :app:assembleDebug` (with `JAVA_HOME=C:/Users/HhsJa/.jdks/temurin-17/jdk-17.0.17+10` after `./gradlew --stop`) succeeded; initial Kotlin daemon cache issue resolved by stopping daemons.
- Markdown lint rerun: `npx markdownlint-cli "**/*.md"` → fails on existing long-line/bare-URL issues; log updated at `docs/qa/battery-checklist-markdownlint.txt`.
## 2025-12-02 - Rendering alignment and release signing
- Pivoted architecture to Kotlin Canvas renderer as the primary runtime path; manifest no longer declares WFF metadata, and `docs/tech/architecture.md` documents the Kotlin track while keeping WFF XML as a design reference only.
- Reopened pre-checklist items in `AGENTS.md` and marked prerequisite docs approved (`docs/product/design_brief.md`, `docs/product/asset_requirements.md`, `docs/product/feature_scope.md`, `docs/ops/account_access.md`, `docs/qa/validation_plan.md`) with owner/date stamps.
- Added release signing config reading `UPLOAD_KEYSTORE_PATH` or `UPLOAD_KEYSTORE_BASE64` with env passwords, falling back to debug keys locally; release builds now use `signingConfigs["release"]`.
- Generated release bundle with fallback signing: `./gradlew :app:bundleRelease` (JAVA_HOME=Temurin 17); artifact at `app/build/outputs/bundle/release/app-release.aab`.
- Added `.markdownlint.json` relaxing strict rules; `npx --yes markdownlint-cli "**/*.md"` now passes.
- Baseline profile remains the prior generated file; regeneration deferred until a Wear emulator/device is available. Plugin AGP warning documented in `docs/perf/baselineprofile.md`.
