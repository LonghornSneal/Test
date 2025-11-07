# CosmoBond Roadmap for AI Agents

## Agent Orientation
- **Platform focus:** Build the CosmoBond Galaxy Watch8 Classic experience using Watch Face Format v2 with a lightweight Kotlin host for configuration and complications.
- **Toolchain expectations:** Android Gradle Plugin 8.5+, Kotlin 1.9+, Gradle wrapper (JDK 17), GitHub Actions for CI/CD, Gradle Play Publisher for distribution, Google Play internal→production release tracks.
- **Workflow rule:** Progress strictly in checklist order. Claim an item by appending `_(claimed by @agent, YYYY-MM-DD HH:mm UTC)_` to the task line before making changes. Complete all acceptance checks before marking `[x]` and append `_Completed: summary (timestamp)_`. Use `_Blocked: reason (timestamp)_` if stalled.
- **Quality bar:** Each completed item must include updated/created tests, executed commands, screenshots or logs when specified, and documentation of decisions in `docs/`.
- **Documentation lint:** Run `npx markdownlint "**/*.md"` (or manually audit if tooling unavailable) after editing Markdown files and record the result in the task evidence.
- **Execution principles:**
  1. Smaller prompts work better—ship one feature at a time.
  2. Prototype core logic first before polishing visuals or notifications.
  3. Name and reuse components so prompts become building blocks.
  4. Keep context lean by feeding only relevant files/configs.
  5. Use version control aggressively; avoid large untracked change sets.
  6. Debug quickly with print statements and share the captured output when looping.

## Ready-to-Run Repository Snapshot
This repository already includes baseline policy files, directory scaffolding, GitHub Actions workflows, and a minimal Watch Face Format XML. Review the structure before claiming tasks:
- `.github/workflows/android.yml` and `release.yml` implement PR checks and internal releases.
- `app/` contains a starter Wear OS module with `res/raw/watchface.xml` and a placeholder `CosmoBondWatchFaceService`.
- `docs/`, `art/`, and related directories are pre-created so that documentation tasks can start immediately.

---

## CI/CD Checklist for the Galaxy Watch Face
Follow the tasks in order. Each item lists its purpose, precise steps, acceptance checks, required artifacts, and recovery guidance.

### Pre-Checklist Prerequisites

- [ ] **Design brief finalized:** Dial layout, typography, color palette, complication plan, and animation storyboards approved.
- [ ] **Asset requirements gathered:** Vector/raster assets, fonts, and ambient-mode variants stored in an accessible location.
- [ ] **Feature scope prioritized:** Feature list with interactive behaviors, data integrations, and configuration options locked for v1.0.
- [ ] **Account and secret access secured:** Google Play Console, signing keys, analytics credentials, and related secrets available for automation.
- [ ] **Validation plan prepared:** Target device matrix, manual test scenarios, and acceptance criteria documented for handoff.

### Phase 0 — Baseline Verification & Policy Alignment

1. **[ ] Prompt:** _"Audit the repository guardrails. Inspect `README.md`, `CODEOWNERS`, `CONTRIBUTING.md`, `LICENSE`, `.gitignore`, `.editorconfig`, and `.gitattributes`; record any needed adjustments in `docs/project/decision_log.md`; capture current branch protection status and document how to request updates."_
   - **Purpose:** Confirm guardrails and initial documentation are present.
   - **Acceptance:** Baseline files reviewed; issues noted in `docs/project/decision_log.md`; branch protections documented.
   - **Artifacts:** Decision log entry, branch protection screenshot.
   - **Fail?:** Address missing protections or file updates, recommit.

2. **[ ] Prompt:** _"Lock the platform versions and compliance targets. Confirm JDK 17, AGP 8.5.x+, Kotlin 1.9+, Gradle wrapper, Android SDK API 34, and Wear OS emulator images, then update `docs/setup/tooling.md` with the versions, download links, and Wear OS 5/6 lineage references."_
   - **Purpose:** Freeze host/tool versions for repeatability.
   - **Acceptance:** `docs/setup/tooling.md` describes tooling, cites Play target API 34 requirement.
   - **Artifacts:** Tooling doc diff.
   - **Fail?:** Adjust mismatched versions and re-document.

3. **[ ] Prompt:** _"Confirm the rendering track. Update `docs/tech/architecture.md` to summarize the Watch Face Format v2 + Kotlin host approach, set minSdk/targetSdk to 34, and cite the supporting Google/Samsung guidance. If you pivot to the Kotlin-rendered path, adjust downstream notes accordingly."_
   - **Purpose:** Document Watch Face Format v2 choice (or switch to Kotlin track if required).
   - **Acceptance:** Architecture doc captures decision with references.
   - **Artifacts:** Architecture doc diff.
   - **Fail?:** If opting into Kotlin-rendered path, update downstream steps and checklist notes accordingly.

---

### Phase 1 — Android Project Skeleton

> **USER NOTE — first on-watch testing unlock:** Task 4 produces a debug build you can sideload to the Galaxy Watch8 Classic for initial smoke checks once completed. AI agents should ignore this line.

4. **[ ] Prompt:** _"Validate the Wear OS project scaffolding. Review `app/build.gradle.kts`, `settings.gradle.kts`, and `AndroidManifest.xml`; run `./gradlew :app:assembleDebug` on JDK 17; capture the build logs and record any dependency updates needed in the decision log."_
   - **Purpose:** Ensure the existing module builds and aligns with naming/package expectations.
   - **Acceptance:** Debug build succeeds; notes recorded in decision log.
   - **Artifacts:** Build log, Gradle wrapper version, decision log entry.
   - **Fail?:** Resolve version mismatches or missing dependencies and rerun.

5. **[ ] Prompt:** _"Expand the Watch Face Format v2 skeleton. Enhance `res/raw/watchface.xml` with initial layout elements, verify the service metadata and preview assets, and build a release bundle with `./gradlew :app:assembleRelease` to confirm the resource packaging."_
   - **Purpose:** Flesh out declarative structure and previews.
   - **Acceptance:** Release bundle contains WFF resource; previews render.
   - **Artifacts:** AAB artifact, manifest diff, screenshots.
   - **Fail?:** Correct resource paths or metadata and rebuild.

6. **[ ] Prompt (optional Kotlin track):** _"Introduce the Jetpack Watch Face renderer. Add the `androidx.wear.watchface:watchface` dependencies, implement the `WatchFaceService`, `ComplicationSlotsManager`, and `UserStyleSchema`, and prove the renderer with a passing unit test preview."_
   - **Purpose:** Code-rendered face (if not using WFF).
   - **Acceptance:** Unit test renders preview and compiles.
   - **Artifacts:** Sample screenshot.
   - **Fail?:** Reconcile dependencies/Compose Canvas usage.

---

### Phase 2 — Quality Gates (Local)

7. **[ ] Prompt:** _"Establish static analysis and style enforcement. Add ktlint with Spotless, configure Android Lint to treat new issues as fatal (with a baseline), and wire in a Detekt ruleset, then verify with `./gradlew spotlessApply detekt lint`."_
   - **Purpose:** Enforce Kotlin style and code health.
   - **Acceptance:** `./gradlew spotlessApply detekt lint` passes.
   - **Artifacts:** Lint HTML report, Detekt SARIF, Spotless status.
   - **Fail?:** Fix violations or adjust rules narrowly.

8. **[ ] Prompt:** _"Add unit tests for the non-UI logic. Implement `src/test` coverage for time formatting, color scheme selection, and complication ID mapping, then confirm `./gradlew testDebugUnitTest` passes with ≥70% coverage on the core utilities."_
   - **Purpose:** Cover non-UI logic (style schema parsing, config).
   - **Acceptance:** `./gradlew testDebugUnitTest` green, coverage ≥ 70% for core utils.
   - **Artifacts:** JUnit XML, coverage report.
   - **Fail?:** Add tests/fix logic.

9. **[ ] Prompt:** _"Create screenshot tests for the previews. For WFF, write an instrumentation test that loads `watchface.xml` and captures frames via the wear-watchface screenshot API (use Paparazzi/Compose for the Kotlin track). Commit the golden images to `app/src/androidTest/assets/goldens/` and surface them in CI."_
   - **Purpose:** Lock visual baselines (dark/light/AOD).
   - **Acceptance:** Golden images generated & committed under `app/src/androidTest/assets/goldens/`.
   - **Artifacts:** Generated PNGs uploaded by CI.
   - **Fail?:** Update goldens only after design sign-off.

---

### Phase 3 — Performance & Battery Scaffolding

10. **[ ] Prompt:** _"Stand up the baseline profile module and ensure Macrobenchmark-generated rules ship in release builds."_
    - **Purpose:** Improve startup/render perf; reduce CPU.
    - **Steps:**
      1. Add `baselineprofile` module with **Macrobenchmark** to exercise face config screen and render path.
      2. Generate & include baseline profile in release builds.
    - **Acceptance:** `./gradlew :baselineprofile:generateBaselineProfile` produces rules; included in AAB.
    - **Artifacts:** Baseline profile file; macrobenchmark HTML/CSV.
    - **Fail?:** Verify profile merging per Compose/Wear guidance.

11. **[ ] Prompt:** _"Complete the battery and performance guideline audit for the watch face."_
    - **Purpose:** Enforce Google’s watch face power best practices.
    - **Steps:**
      * Audit animation cadence, complication update frequency, and phone interactions against **Optimize watch faces** guide.
    - **Acceptance:** Checklist signed with notes on update rates/AOD behavior.
    - **Artifacts:** `docs/qa/battery-checklist.md`.
    - **Fail?:** Reduce updates/animations; re-test.

---

### Phase 4 — CI Foundation (GitHub Actions)

12. **[ ] Prompt:** _"Provision the CI secrets required for builds and publishing."_
    - **Purpose:** Secure keys for build/publish.
    - **Steps:**
      * Add repo secrets: `PLAY_SERVICE_ACCOUNT_JSON`, `UPLOAD_KEYSTORE_BASE64`, `UPLOAD_KEY_ALIAS`, `UPLOAD_KEY_PASSWORD`, `STORE_PASSWORD`.
    - **Acceptance:** Secrets present & referenced in workflow; no secrets in code.
    - **Artifacts:** Screenshot of secrets page (sensitive fields redacted).
    - **Fail?:** Rotate keys and retry.

13. **[ ] Prompt:** _"Wire the PR build and lint workflow in GitHub Actions."_
    - **Purpose:** Automatic checks on PR.
    - **Steps:**
      * Ensure `.github/workflows/android.yml` runs `./gradlew spotlessCheck detekt lint assembleDebug` with cached Gradle and Android SDK setup.
    - **Acceptance:** PRs show passing checks; SARIF uploaded.
    - **Artifacts:** Lint/Detekt reports as build artifacts.
    - **Fail?:** Fix build/lint errors.

14. **[ ] Prompt:** _"Extend CI to execute the unit test suite and surface the results."_
    - **Purpose:** Enforce correctness gates.
    - **Steps:**
      * Extend workflow to run `./gradlew testDebugUnitTest` and publish reports.
    - **Acceptance:** Tests pass; coverage comment on PR.
    - **Artifacts:** Test reports uploaded.
    - **Fail?:** Fix tests or code.

15. **[ ] Prompt:** _"Add the Wear OS emulator job to run connected tests in CI."_
    - **Purpose:** Run instrumentation/screenshot tests headless.
    - **Steps:**
      * Use **ReactiveCircus/android-emulator-runner** with a Wear OS x86_64 system image; start emulator; run `connectedDebugAndroidTest`.
    - **Acceptance:** Emulator boots in CI; `androidTest` suite passes; screenshots uploaded.
    - **Artifacts:** Instrumentation logs, screenshots.
    - **Fail?:** Bump RAM/timeouts; pre-download system images; retry.

16. **[ ] Prompt:** _"Schedule the nightly Macrobenchmark workflow to capture baseline profiles."_
    - **Purpose:** Generate baseline profile in CI nightly.
    - **Steps:**
      * Nightly workflow triggers `:baselineprofile:collect` then commits asset as artifact (not auto-commit).
    - **Acceptance:** Baseline profile artifact attached to nightly run.
    - **Artifacts:** Baseline profile, perf metrics.
    - **Fail?:** Skip battery check on no-battery hosts if needed.

---

### Phase 5 — Signing & Play Integration

17. **[ ] Prompt:** _"Enable Play App Signing in the Google Play Console and capture evidence."_
    - **Purpose:** Use Google-managed signing; CI only needs upload key.
    - **Steps:**
      * In Play Console, enable **App signing by Google Play**; download upload certificate; store **upload keystore** in CI (Base64).
    - **Acceptance:** Play shows “App signing enabled.”
    - **Artifacts:** App signing status screenshot.
    - **Fail?:** Complete identity verification; retry.

18. **[ ] Prompt:** _"Configure Gradle release signing and build types to use CI-provided credentials."_
    - **Purpose:** Deterministic release builds in CI.
    - **Steps:**
      * In `app/build.gradle.kts`, configure `signingConfigs { release }` reading env vars; enable R8; set `versionCode` auto from tags.
    - **Acceptance:** `./gradlew :app:bundleRelease` uses release keystore in CI.
    - **Artifacts:** Build scans/logs.
    - **Fail?:** Fix keystore passwords/aliases.

19. **[ ] Prompt:** _"Integrate Gradle Play Publisher and validate the release task graph."_
    - **Purpose:** Automate upload to internal/closed tracks.
    - **Steps:**
      * Add plugin `com.github.triplet.play` and `play { serviceAccountCredentials.set(...) track.set("internal") }`.
    - **Acceptance:** Dry run: `./gradlew publishRelease --dry-run` resolves tasks.
    - **Artifacts:** Gradle config diff.
    - **Fail?:** Fix scopes on service account JSON.

20. **[ ] Prompt:** _"Finalize the tag-driven internal release workflow in GitHub Actions."_
    - **Purpose:** One-button internal release from a tag.
    - **Steps:**
      * Ensure `.github/workflows/release.yml` builds `bundleRelease`, runs tests, then `publishRelease` to **internal** on tag push.
    - **Acceptance:** Tagging `v0.1.0` uploads AAB to internal track.
    - **Artifacts:** Play Console artifacts screenshot, CI logs.
    - **Fail?:** Inspect GPP error output and fix listing/consent.

---

### Phase 6 — Feature Completeness for Watch Face

21. **[ ] Prompt:** _"Implement the complication slots and schema within the Watch Face Format layout."_
    - **Purpose:** Add standard complications (steps, heart rate, battery).
    - **Steps:**
      * Define slots and bounds in WFF; for phone battery or advanced data, consider a small provider app.
    - **Acceptance:** Emulator shows selectable complications in system editor; data renders.
    - **Artifacts:** Screenshots from emulator.
    - **Fail?:** Verify slot IDs and data types.

22. **[ ] Prompt:** _"Design and validate the always-on display and power-saving modes for the watch face."_
    - **Purpose:** Great battery behavior.
    - **Steps:**
      * Provide simplified `ambient` group in WFF; throttle updates; avoid constant phone interactions & heavy animations to pass Play warnings.
    - **Acceptance:** Macrobenchmark shows stable frame times; no Play warning during review.
    - **Artifacts:** Perf numbers; Play review notes.
    - **Fail?:** Reduce animation frequency/bitmap size.

23. **[ ] Prompt:** _"Automate generation of multi-density previews and Play Store screenshots."_
    - **Purpose:** Auto-produce Play assets.
    - **Steps:**
      * Instrumentation test renders preset styles (light/dark/AOD) at multiple densities, saves PNGs; CI uploads to `src/main/play/listings/en-US/graphics/phone-screenshots/`.
    - **Acceptance:** Generated screenshots meet Play specs; `publishListing` succeeds.
    - **Artifacts:** PNGs as CI artifacts and committed copies.
    - **Fail?:** Adjust emulator density and renderer.

---

### Phase 7 — Store Listing & Testing Tracks

24. **[ ] Prompt:** _"Version-control the Google Play store listing metadata and publish it through CI."_
    - **Purpose:** Keep Play listing under version control.
    - **Steps:**
      * Add `fastlane/metadata` or GPP metadata files (`src/main/play/`): title, short/long description, changelog, screenshots (generated by tests), icons.
    - **Acceptance:** `./gradlew publishListing` updates Play listing.
    - **Artifacts:** CI logs + Play change diff.
    - **Fail?:** Fix locale folders or graphic specs.

25. **[ ] Prompt:** _"Create and populate the Google Play testing tracks for internal, closed, and open testers."_
    - **Purpose:** Gradual validation.
    - **Steps:**
      * In Play Console: **Internal**, **Closed**, **Open** tracks; add testers (email lists/Google Groups).
    - **Acceptance:** Testers can install from **Internal** link.
    - **Artifacts:** Track screenshots, tester list (redacted).
    - **Fail?:** Reconcile tester emails; wait for indexing.

26. **[ ] Prompt:** _"Verify the Wear OS target API policy compliance and document the evidence."_
    - **Purpose:** Compliance gate.
    - **Steps:**
      * Confirm `targetSdkVersion=34` for Wear OS app submission.
    - **Acceptance:** Play pre-launch report shows correct target API.
    - **Artifacts:** Pre-launch report PDF.
    - **Fail?:** Update `targetSdk` and resubmit.

---

### Phase 8 — Pre-Release Gates

27. **[ ] Prompt:** _"Run a Google Play pre-launch report smoke test and address findings."_
    - **Purpose:** Automated device lab sanity.
    - **Steps:**
      * Upload to **Internal**; trigger PLR; review crashes, ANRs, permissions.
    - **Acceptance:** Zero critical issues; medium issues triaged.
    - **Artifacts:** PLR HTML/PDF.
    - **Fail?:** Fix and re-upload.

28. **[ ] Prompt:** _"Complete the accessibility review for the watch face and document adjustments."_
    - **Purpose:** Legibility on small displays.
    - **Steps:**
      * Check contrast ratios and tap targets per Wear guidance.
    - **Acceptance:** `docs/ux/accessibility.md` updated; issues resolved.
    - **Artifacts:** Before/after screenshots.
    - **Fail?:** Adjust palettes/typography.

29. **[ ] Prompt:** _"Promote the build to the closed testing track and collect feedback."_
    - **Purpose:** External validation.
    - **Steps:**
      * Promote internal → closed; collect feedback for 7–14 days.
    - **Acceptance:** No crash spikes; battery feedback positive.
    - **Artifacts:** Crash-free sessions %, user notes.
    - **Fail?:** Patch, re-test.

---

### Phase 9 — Release & Post-Release

30. **[ ] Prompt:** _"Execute a staged production rollout via Gradle Play Publisher."_
    - **Purpose:** Safe release.
    - **Steps:**
      * Use GPP: `./gradlew publishRelease -Ptrack=production -ProlloutFraction=0.1`.
    - **Acceptance:** 10% staged rollout created in Play; monitoring enabled.
    - **Artifacts:** Rollout screenshot; CI logs.
    - **Fail?:** Halt rollout; hotfix via internal → production.

31. **[ ] Prompt:** _"Establish monitoring dashboards and alerting for post-release health."_
    - **Purpose:** Catch regressions.
    - **Steps:**
      * Enable ANR/Crash alerts; track Play vitals; set alerting in observability tool.
    - **Acceptance:** Alert rules confirmed; dashboard link recorded.
    - **Artifacts:** Dashboard URL in `docs/devops/monitoring.md`.
    - **Fail?:** Adjust filters and thresholds.

32. **[ ] Prompt:** _"Cut the production tag, publish the changelog, and write the release retrospective."_
    - **Purpose:** Close the loop.
    - **Steps:**
      * Tag `v1.0.0`; generate `CHANGELOG.md` from commits; write retro in `docs/operations/retrospectives.md`.
    - **Acceptance:** Tag pushed; changelog and retro committed.
    - **Artifacts:** Release page link; changelog diff.
    - **Fail?:** Fix versioning script and retry.

---

### GitHub Actions — Starter Workflows (reference)
See `.github/workflows/android.yml` and `release.yml` in this repo for ready-to-run configurations that align with the checklist requirements.

### Gradle & Manifest References
- Top-level `build.gradle.kts` configures AGP/Kotlin versions and Play Publisher.
- `app/build.gradle.kts` targets API 34, enables release minification, and wires Play Publisher defaults.
- `AndroidManifest.xml` registers `CosmoBondWatchFaceService` pointing to `@raw/watchface`.

### Test & Evidence Menu
Use these commands when relevant tasks call for validation:
- `./gradlew clean assembleDebug`
- `./gradlew spotlessCheck detekt lint`
- `./gradlew testDebugUnitTest`
- `./gradlew connectedDebugAndroidTest`
- `./gradlew :baselineprofile:collect`
- `./gradlew publishRelease --dry-run`

### Done Definition (applies to every box)
- All acceptance checks in the item pass.
- Tests relevant to the item executed successfully; logs attached.
- On-device/emulator validation completed when required; screenshots saved.
- Battery/perf and accessibility reviewed when applicable.
- Risks/decisions captured in `docs/`.
