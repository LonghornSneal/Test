# CosmoBond Roadmap for AI Agents

## Agent Orientation
- **Codebase focus:** Build CosmoBond as a native Wear OS 4 watch-face experience written in Kotlin using Jetpack Compose for Wear OS, Kotlin Coroutines, and Android Architecture Components. Avoid alternative stacks unless a future task explicitly approves them.
- **Coding objectives:** Maintain an offline-first pet-care loop, anime-quality visuals, and extensible social interactions while keeping every source file under 500 lines of code by splitting logic into subpackages and shared utilities.
- **Workflow expectations:** You are the sole developer. Work strictly from the first unchecked task downward; do not skip ahead or parallelize. Create directories before adding files, and never generate code artifacts until the roadmap instructs you to do so.
- **Quality obligations:** Every task ends with a Definition of Done that requires test creation/updates, execution of relevant automated suites, on-device or emulator validation, documentation of evidence, and resolution of all failures before marking the task complete. Cosmetic overlay behaviour must remain non-blocking when other apps are foregrounded, and "Do Not Disturb" settings must always be respected.
- **Documentation hygiene:** All specifications, diagrams, and reports live inside the `docs/` tree. Run Markdown linting (using `npx markdownlint "**/*.md"`) or perform an equivalent manual checklist if the tooling is unavailable. Record notes about decisions and validations in the same commit as the task’s artifacts.

---

## Sequential Task Checklist

### Phase 1 · Product Definition
1. [ ] **Document CosmoBond product vision**
   - Implementation: Create `docs/product/vision.md` capturing the watch-face mission, user personas, success metrics, and high-level experience pillars (personalized evolution, ambient companionship, social play, offline-first).
   - Definition of Done: Vision file saved with clear sections; self-review against README alignment; run `npx markdownlint "docs/product/vision.md"` (or manually verify headings, lists, links) and note results in the task log.
2. [ ] **Capture starter pet roster details**
   - Implementation: Create `docs/product/pets.md` describing Nebula Pangolin (sleep guardian), Glyph Dragonet (language mini-games), and Rhythm Lynx (music-driven dancer) including evolution triggers, neglect states, emotional cues, and runaway rules.
   - Definition of Done: Pet profiles documented with table of stats and trigger thresholds; linted with markdownlint; confirm consistency with README and record validation notes.
3. [ ] **Define personalization & accessibility charter**
   - Implementation: Draft `docs/product/personalization_accessibility.md` explaining adaptive evolution goals, gesture-only affordances, color contrast requirements, font sizing, and behaviour under Do Not Disturb.
   - Definition of Done: Charter lists at least three persona-driven goal templates and accessibility checkpoints; markdownlint passes; perform manual accessibility checklist (contrast ratios, gesture descriptions) and log findings.
4. [ ] **Specify data retention and storage policy**
   - Implementation: Author `docs/data/retention.md` covering pet history caps (e.g., latest 12 evolutions), archival/compression strategy, offline cache limits for art/audio, and purge triggers.
   - Definition of Done: Policy references storage thresholds and pruning workflow; markdownlint passes; run a peerless self-review verifying alignment with privacy expectations and document conclusions.
5. [ ] **Outline care state communication rules**
   - Implementation: Create `docs/product/pet_states.md` enumerating visual cues for hunger, mood, fatigue, runaway behaviour, and recovery for each starter pet, including animation requirements for overlays.
   - Definition of Done: File includes state diagrams or tables; linted; manually verify states cover happy, neutral, warning, critical; record QA note.

### Phase 2 · Experience & Narrative Design
6. [ ] **Map interaction surfaces**
   - Implementation: Produce `docs/experience/interaction_map.md` detailing the main watch face, customization panels, and cosmetic status overlay behaviour when other apps are foregrounded (drag-to-move, pause when hidden).
   - Definition of Done: Diagram or structured outline stored; markdownlint passes; validate flow against Wear OS interaction guidelines and record confirmation.
7. [ ] **Draft episodic story arc plan**
   - Implementation: Write `docs/experience/story_arcs.md` defining anime-style chapter cadence, art needs per chapter, and how arcs tie to evolution milestones.
   - Definition of Done: Outline contains at least one season with episode summaries; linted; manually confirm storage needs feed retention policy; log QA note.
8. [ ] **Design social feature charter**
   - Implementation: Prepare `docs/experience/social_charter.md` covering Bluetooth co-op care, stealth pranks, pet relocation, invite-only circles, public rooms, event-based QR flows, and abuse mitigation strategies.
   - Definition of Done: Charter lists consent flows and telemetry for each mechanic; markdownlint passes; perform privacy/security checklist and document results.
9. [ ] **Detail audio affinity behaviour**
   - Implementation: Create `docs/experience/audio_affinity.md` describing how Rhythm Lynx reacts to media metadata, respects Do Not Disturb, and handles optional sound effects.
   - Definition of Done: Document explains metadata sources, fallback when unavailable, and quiet-hour compliance; linted; validate privacy considerations and log verification.
10. [ ] **Specify cosmetic overlay configuration**
    - Implementation: Add `docs/experience/overlay_settings.md` explaining overlay toggle, positioning UI, collision avoidance with foreground apps, and persistence across sessions.
    - Definition of Done: Settings described with wireframe references; markdownlint passes; manual review ensures overlay remains cosmetic-only; note QA outcome.

### Phase 3 · Visual & Audio Asset Direction
11. [ ] **Author art direction bible**
    - Implementation: Build `art/styleguide.md` defining color palettes, line weights, animation frame targets, and expression sheets for all pets.
    - Definition of Done: Style guide includes reference swatches and frame timing table; run markdownlint; verify assets align with anime aesthetic and record check.
12. [ ] **Produce baseline mockups**
    - Implementation: Generate high-level mockups (static images) for watch face, overlay, and customization UI, saving them under `art/mockups/` with descriptive filenames.
    - Definition of Done: At least three mockups exported (PNG or SVG); visually inspect for alignment with interaction map; document review notes in `art/mockups/README.md` and lint that file.
13. [ ] **Define animation asset plan**
    - Implementation: Draft `docs/art/animation_plan.md` listing sprite sheets or vector sequences per pet, frame counts, file-size targets (<500 KB per animation), and compression approach.
    - Definition of Done: Plan enumerates assets for happy/warning/critical states plus dance routines; markdownlint passes; manually verify size targets meet storage policy; note QA findings.
14. [ ] **Outline audio asset guidelines**
    - Implementation: Create `docs/audio/guidelines.md` explaining optional sound effects, volume standards, and conditions for muting under DND or quiet hours.
    - Definition of Done: Guidelines include testing checklist for audio levels; linted; self-verify compliance with privacy policy and log results.

### Phase 4 · Technical Blueprint
15. [ ] **Establish high-level architecture**
    - Implementation: Compose `docs/tech/architecture.md` describing module boundaries (`watchface/ui`, `watchface/pets`, `engine/sensors`, `engine/audio`, `engine/social`, `data/persistence`, `tests/`, `art/`, `docs/`), communication flows, and dependency rules.
    - Definition of Done: Diagram or textual architecture recorded; markdownlint passes; manual review ensures no anticipated file exceeds 500 LOC without planned splitting; log QA note.
16. [ ] **Define repository directory scaffold**
    - Implementation: Document intended folder structure and naming conventions in `docs/tech/directory_structure.md`, including when to create submodules to keep files under 500 LOC.
    - Definition of Done: Structure lists all planned directories and file naming rules; linted; cross-check with architecture doc and note confirmation.
17. [ ] **Set coding standards & tooling**
    - Implementation: Write `docs/tech/standards.md` covering Kotlin style (Kotlin Coding Conventions + Compose idioms), lint/format commands, static analysis tools, and commit hygiene.
    - Definition of Done: Standards mention `ktlint`, `detekt`, and testing minimums; markdownlint passes; validate tool list against workflow capabilities and record QA note.
18. [ ] **Plan testing strategy**
    - Implementation: Populate `docs/qa/strategy.md` detailing unit, integration, UI, performance, battery, and localization testing suites plus required tools/emulators.
    - Definition of Done: Strategy specifies pass/fail gates for each suite; linted; verify coverage spans every module and log review summary.
19. [ ] **Draft CI/CD pipeline blueprint**
    - Implementation: Author `docs/devops/pipeline.md` describing build stages, lint/test automation, artifact signing, battery benchmark gating, and release packaging for Google Play.
    - Definition of Done: Pipeline lists command invocations and triggers; markdownlint passes; manually confirm steps cover rollback path and note QA.
20. [ ] **Record analytics & telemetry plan**
    - Implementation: Create `docs/data/analytics.md` enumerating metrics for evolution progress, social interactions, overlay usage, and prank abuse detection.
    - Definition of Done: Plan maps metrics to data retention rules and privacy safeguards; linted; self-review ensures anonymization steps are defined and document outcome.

### Phase 5 · Feature Specification
21. [ ] **Spec Nebula Pangolin sleep guardian**
    - Implementation: Write `docs/features/nebula_pangolin.md` detailing sleep metrics consumed, threshold logic, animations per state, and offline fallbacks.
    - Definition of Done: Spec includes pseudocode for state machine and QA scenarios; markdownlint passes; manually verify retention compatibility; record QA note.
22. [ ] **Spec Glyph Dragonet language mini-games**
    - Implementation: Prepare `docs/features/glyph_dragonet.md` describing gameplay loops, localization packs, offline bundles, and neglect consequences.
    - Definition of Done: Document lists mini-game flowchart and localization testing plan; linted; confirm offline strategy matches data policy; note QA results.
23. [ ] **Spec Rhythm Lynx dance system**
    - Implementation: Draft `docs/features/rhythm_lynx.md` explaining media metadata hooks, arrow timing tolerances, failure animations, and Do Not Disturb compliance.
    - Definition of Done: Spec includes timing equations and latency targets; markdownlint passes; validate privacy for media access and log findings.
24. [ ] **Spec customization & settings flows**
    - Implementation: Create `docs/features/settings.md` mapping on-watch pet selection, evolution goal configuration, overlay toggle, and reposition UX.
    - Definition of Done: Flow includes step-by-step UI states with gestures; linted; confirm accessibility charter requirements met; log QA.
25. [ ] **Spec social matchmaking & relocation**
    - Implementation: Build `docs/features/social_bluetooth.md` detailing Bluetooth discovery, consent prompts, prank throttling, and neglected pet relocation rules.
    - Definition of Done: Spec lists state diagrams and abuse mitigations; markdownlint passes; run security self-audit checklist and document.
26. [ ] **Spec invite circles & public rooms**
    - Implementation: Document `docs/features/social_network.md` covering invite-only circles, open public rooms, and event-based QR onboarding.
    - Definition of Done: File includes user flows, backend assumptions, and rate limits; linted; confirm telemetry hooks align with analytics plan; record QA.
27. [ ] **Spec large event triggers & rewards**
    - Implementation: Write `docs/features/event_triggers.md` defining location-based or scheduled event evolutions, reward distribution, and opt-in UX.
    - Definition of Done: Spec outlines cooldowns and abuse prevention; markdownlint passes; manually verify compliance with privacy/geolocation policies and note QA.
28. [ ] **Plan story content pipeline**
    - Implementation: Draft `docs/features/story_pipeline.md` describing how episodic chapters are stored, scheduled, and localized.
    - Definition of Done: Pipeline details asset packaging and delivery cadence; linted; confirm retention policy handles archive; document QA.

### Phase 6 · Implementation Readiness
29. [ ] **Create backlog tracker**
    - Implementation: Initialize `docs/project/backlog.csv` enumerating tasks derived from feature specs with estimates and dependencies.
    - Definition of Done: CSV populated and sorted by sequence; verify data integrity using spreadsheet check; log QA note.
30. [ ] **Author risk register**
    - Implementation: Add `docs/project/risks.md` covering top technical, schedule, and experience risks plus mitigation plans.
    - Definition of Done: Register includes probability/impact matrix; linted; perform self-review ensuring mitigations tie to roadmap tasks; document.
31. [ ] **Define communication & decision log**
    - Implementation: Create `docs/project/decision_log.md` template to record future architectural and design decisions with timestamps.
    - Definition of Done: Template includes fields for context, decision, alternatives, rationale, and QA impact; markdownlint passes; confirm usage instructions align with workflow; note QA.
32. [ ] **Set up repository contribution guide**
    - Implementation: Author `CONTRIBUTING.md` summarizing workflow rules, branching strategy, commit message format, testing expectations, and file-length policy.
    - Definition of Done: Guide cross-links to standards and roadmap; linted; manual review ensures policy clarity; document QA.
33. [ ] **Update README references**
    - Implementation: Ensure `README.md` references all foundational docs and clarifies roadmap location without duplicating specs.
    - Definition of Done: README updated accordingly; run markdownlint on README; verify summary stays concise; log QA note.

### Phase 7 · Repository Scaffolding
34. [ ] **Initialize Gradle project structure**
    - Implementation: Create Gradle settings and module directories (`app`, `wear`, etc.) per architecture, ensuring each module will stay under 500 LOC per file by planning subpackages.
    - Definition of Done: Project syncs with `./gradlew help`; run `./gradlew lint` (expect baseline success); document results and fix any failures before proceeding.
35. [ ] **Configure Kotlin and Compose dependencies**
    - Implementation: Update Gradle files to include Kotlin, Compose for Wear OS, coroutines, lifecycle libraries, and testing dependencies.
    - Definition of Done: Build succeeds with `./gradlew assembleDebug`; run `./gradlew test` (should pass with placeholder tests) and record evidence.
36. [ ] **Set up static analysis tooling**
    - Implementation: Integrate `ktlint` and `detekt` via Gradle plugins and configure baseline rules in `config/` directories.
    - Definition of Done: `./gradlew ktlintCheck detekt` passes; document configuration choices in decision log.
37. [ ] **Implement base package structure**
    - Implementation: Create empty Kotlin packages for `watchface.time`, `pets`, `engine.sensors`, `engine.audio`, `engine.social`, `data.persistence`, and `ui.overlay`, keeping each file under 500 LOC.
    - Definition of Done: Build still passes; add placeholder unit tests verifying package wiring; run `./gradlew test`; log outcomes.
38. [ ] **Establish resource folders and placeholders**
    - Implementation: Add resource directories for drawable, raw audio, and story content along with placeholder JSON definitions respecting storage policy.
    - Definition of Done: Resources compile; run `./gradlew assembleDebug`; confirm APK size within projections; record QA notes.
39. [ ] **Document scaffolding status**
    - Implementation: Update `docs/project/decision_log.md` summarizing scaffolding actions, tool versions, and outstanding questions.
    - Definition of Done: Entry added with date/time; lint decision log; ensure backlog updated if new tasks arise.

### Phase 8 · Feature Implementation
40. [ ] **Build timekeeping watch-face core**
    - Implementation: Implement Compose-based time display, complications placeholders, and base rendering loop.
    - Definition of Done: Unit tests for time updates + UI snapshot tests pass; run on emulator to confirm smooth updates; fix issues before closing.
41. [ ] **Implement pet state engine**
    - Implementation: Create state machine module handling pet moods, neglect timers, and runaway logic shared across pets.
    - Definition of Done: Comprehensive unit tests for transitions pass; integration test with persistence layer passes; run emulator scenario validating visual state changes.
42. [ ] **Wire offline persistence layer**
    - Implementation: Implement local database (Room) and data access objects for pet history, story progression, and settings.
    - Definition of Done: Unit tests for DAOs pass; instrumented tests confirm offline usage; verify data retention rules enforced; document test evidence.
43. [ ] **Develop Nebula Pangolin features**
    - Implementation: Connect to sleep APIs, implement evolution thresholds, animations, and fallback for missing data.
    - Definition of Done: Sensor simulation tests, UI tests, and manual device validation (real or emulator with mock data) succeed; energy impact measured and within limits.
44. [ ] **Develop Glyph Dragonet mini-games**
    - Implementation: Build language mini-game UI, localization pack loader, and neglect outcomes.
    - Definition of Done: Unit + UI tests covering gameplay; localization tests for at least two locales; emulator session verifying offline bundles.
45. [ ] **Develop Rhythm Lynx dance gameplay**
    - Implementation: Integrate media metadata listener, arrow sequencing logic, and animation reactions respecting DND.
    - Definition of Done: Timing accuracy tests, audio privacy checks, and manual playtest with sample music succeed; record latency metrics.
46. [ ] **Create customization and settings panels**
    - Implementation: Implement on-watch configuration screens for pet selection, evolution goal tuning, overlay toggle, and reposition tutorial.
    - Definition of Done: UI tests verifying gestures; emulator manual test ensures accessibility goals met; fix before closure.
47. [ ] **Implement cosmetic overlay widget**
    - Implementation: Build overlay service showing pet cameo when other apps in foreground with drag-to-move and pause behaviour.
    - Definition of Done: Instrumented tests verifying overlay respects app boundaries; manual testing ensures purely cosmetic; battery impact logged.
48. [ ] **Integrate audio affinity reactions**
    - Implementation: Hook Rhythm Lynx animations to currently playing media, include fallbacks when metadata unavailable, and ensure DND compliance.
    - Definition of Done: Automated tests for metadata parsing; manual tests with various media apps; confirm no audio when DND active.
49. [ ] **Implement Bluetooth co-op and pranks**
    - Implementation: Develop discovery, consent dialogs, co-op care flows, prank delivery, and neglected pet relocation via Bluetooth.
    - Definition of Done: Integration tests using emulator pairs; security tests for consent bypass; manual session verifying throttling.
50. [ ] **Implement invite circles & public rooms**
    - Implementation: Build UI and storage for circles, public rooms, and QR onboarding leveraging local networking or companion sync as planned.
    - Definition of Done: Unit tests for membership management; UI tests for join flows; manual validation at least with emulator cluster.
51. [ ] **Enable large event triggers**
    - Implementation: Implement event scheduler, geofence or time-based triggers, and reward distribution respecting privacy opt-ins.
    - Definition of Done: Automated tests for trigger calculations; manual simulation verifying opt-in/out; telemetry logs captured.
52. [ ] **Deliver story arc content system**
    - Implementation: Implement content loader, scheduling, and narrative surfacing on watch face.
    - Definition of Done: Unit tests for schedule engine; UI tests for chapter display; manual check ensuring storage usage within limits.
53. [ ] **Finalize pet audio/visual polish**
    - Implementation: Integrate final animations, audio cues, and transitions ensuring file-size budget maintained.
    - Definition of Done: Visual regression tests (screenshot comparisons); audio level checks; manual review confirming anime aesthetic preserved.

### Phase 9 · Quality Assurance & Compliance
54. [ ] **Execute full automated test suite**
    - Implementation: Run `./gradlew test connectedCheck ktlintCheck detekt` plus any snapshot or performance suites configured.
    - Definition of Done: All suites pass; capture reports in `docs/qa/reports/`; rerun after addressing failures until clean.
55. [ ] **Perform battery & performance benchmarking**
    - Implementation: Use Wear OS profiler to measure frame rate, CPU, memory, and battery impact during typical sessions.
    - Definition of Done: Benchmarks meet targets defined in strategy; results logged in `docs/qa/reports/performance.md`; repeat if thresholds missed.
56. [ ] **Conduct localization and accessibility audit**
    - Implementation: Verify UI across target locales and accessibility settings (font scaling, high contrast).
    - Definition of Done: Audit notes stored in `docs/qa/reports/accessibility.md`; issues resolved and re-tested before sign-off.
57. [ ] **Complete security & privacy review**
    - Implementation: Review sensor permissions, Bluetooth interactions, data retention, and prank abuse mitigations.
    - Definition of Done: `docs/security/audit.md` updated with findings and fixes; confirm all blockers resolved.
58. [ ] **Run closed playtest sessions**
    - Implementation: Facilitate self-led or invited playtests, capturing feedback on pet behaviour, overlays, and social features.
    - Definition of Done: Feedback summary saved to `docs/qa/reports/playtest.md`; actionable items added to backlog and addressed where feasible.

### Phase 10 · Launch & Operations
59. [ ] **Prepare Google Play release assets**
    - Implementation: Compile store listing text, screenshots, promo images, privacy disclosures, and Wear OS policy checklists.
    - Definition of Done: Assets stored in `docs/release/play_store/`; lint README references; confirm checklist complete.
60. [ ] **Finalize build signing & packaging**
    - Implementation: Configure release keystore, assemble release APK/AAB, and verify signature.
    - Definition of Done: `./gradlew bundleRelease` succeeds; signature verified with `apksigner`; logs archived in `docs/release/build_notes.md`.
61. [ ] **Publish support & telemetry playbook**
    - Implementation: Document incident response steps, alert thresholds, and content update cadence in `docs/ops/runbook.md`.
    - Definition of Done: Runbook cross-references analytics plan; markdownlint passes; confirm monitoring hooks exist.
62. [ ] **Conduct launch readiness review**
    - Implementation: Review all checklists, QA reports, and outstanding risks to confirm launch criteria met.
    - Definition of Done: Launch checklist stored in `docs/ops/launch_checklist.md` marked complete; any open issues resolved.
63. [ ] **Perform post-launch retrospective setup**
    - Implementation: Create `docs/ops/retro_template.md` capturing metrics to review, questions to answer, and backlog follow-up process.
    - Definition of Done: Template linted; schedule first retrospective session in backlog tracker; log confirmation.
64. [ ] **Archive roadmap status**
    - Implementation: Summarize completed tasks, outstanding backlog items, and lessons learned in `docs/project/roadmap_summary.md`.
    - Definition of Done: Summary references task evidence; markdownlint passes; ensure AGENTS checklist updated to reflect completion.
