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

### Companion Experience Charter
- **Presence management:** Companion complications and tiles must pause progression the moment the user switches to any non-CosmoBond watch face, then resume instantly when the face returns to foreground. Persist timers and state snapshots so no passive stats advance while hidden.
- **Status widget fallback:** When the CosmoBond face is not visible (for example, while Spotify or messaging overlays the display), surface an optional miniature status widget. The pet should stroll, hover, or perch from the screen edge, then auto-roost in negative space that avoids primary UI controls. Provide a long-press drag affordance so the wearer can reposition the pet with a finger and remember the offset per-app context.
- **Expressive wellbeing cues:** Never rely on text prompts to request care. Use facial expressions, posture, particle effects, and idle animations that escalate from mild hunger (subtle stomach rumbles) to severe neglect (emaciated model, slowed movement) before the pet eventually runs away. Reinforce positive states with sparkles, confident stance, and synchronized breathing.
- **Audio affinity:** Mirror the wearer’s listening habits by detecting ambient playback metadata (with explicit user consent) and queueing matching ambience—humming along to podcasts, bobbing to playlists, or curling up quietly for white-noise tracks. Offer privacy toggles and offline fallback soundscapes curated per pet archetype.
- **Evolution design:** Tie evolutionary checkpoints to adaptive wellness goals. Let the onboarding flow capture each user’s priorities (movement, mindfulness, rest, exploration, etc.), then align evolution triggers with achievable yet stretching habits. Provide dynamic alternatives—if the user masters a goal quickly, escalate to variant quests such as “explore three new hiking routes this month” or “maintain HRV gains for two consecutive weeks.”
- **Care system guardrails:** Track feeding, grooming, play, training, and socialization metrics separately so evolution requirements can mix categories. Make failure states recoverable via caretaker missions instead of immediate regressions.

### Social & Live Interaction Backlog
- **Co-op caregiving:** Allow paired Galaxy Watch users to temporarily link habitats, sharing chore rotations, synchronized feeding timers, and joint mini-games that unlock duo emotes.
- **Event-based evolutions:** Detect large events (festivals, marathons, conferences) via location/agenda opt-ins and enable special evolution branches, rare accessories, or commemorative traits earned only during those windows.
- **Playful pranks:** Introduce opt-in mischievous actions (e.g., swapping accessory colors, sending surprise confetti bursts, photobombing friend’s pet selfies) with cooldowns and gentle consent prompts.
- **Habitat trading post:** Schedule weekly meetup windows where players can exchange habitat décor or training tips in-app, supported by bite-sized conversation starters and safety filters.
- **Pet mail & AR postcards:** Let companions exchange animated postcards or short AR clips that appear on the recipient’s watch, reinforcing long-distance engagement.
- **Neighborhood leaderboards:** Surface hyperlocal rankings (steps, mindfulness streaks, adventure logs) that rotate themes to avoid fatigue and keep achievements equitable for diverse lifestyles.
- **Community care quests:** Publish cooperative challenges (e.g., “collectively log 10k mindful minutes this weekend”) with shared progress bars and global cosmetic unlocks when goals are met.

### DigiPet Watch Face Expansion Checklist

- **Universal watch face requirements:** Each DigiPet remains a fully functional CosmoBond watch face. Reserve space for time,
  date, battery, and at least two configurable complications so wearers can tailor health, productivity, or communication data
  without obscuring the companion. Use ambient-mode variants that keep the time legible while dimming pet animations.

#### Sensor-Driven DigiPets

- [ ] **CardioCritter — Heart Rate Fitness Monster**
  - **Watch face baseline:** Highlight primary time/date complications, add workout progress rings, and mirror key vitals in
    sub-complications without hiding the clock.
  - **Concept:** Thrives on heart-health consistency; syncs to resting HR, workouts, and gym visits to reflect care.
  - **Appearance cues:** Rosy, athletic animations during healthy HR/workouts; pale, slouched stance with broken-heart icon when
    readings stay unhealthy.
  - **Implementation hooks:** Use Health Services heart-rate samples, workout sessions, and optional geofenced gym detection;
    throttle polling for battery; evolve after multi-day healthy ranges; trigger Nearby Connections runaway when long-term HR
    neglect persists.
- [ ] **StepSprite — Step Count Companion**
  - **Watch face baseline:** Pair time/date with a step-progress complication ring and configurable secondary slots for goal
    streaks.
  - **Concept:** Feeds on step goals and celebrates streaks; idleness drains energy and risks escape.
  - **Appearance cues:** Happy, fast animations when goals met; sluggish, yawning sprite with boredom icon on sedentary days.
  - **Implementation hooks:** Subscribe to step-count deltas via Health Services or Google Fit; add idle reminders; evolve after
    multi-day goal streaks; treat steps and manual feeds as hunger refills.
- [ ] **SomnoSloth — Sleepy Sloth Pet**
  - **Watch face baseline:** Surface time/date alongside last-night sleep summary complications and sunrise/sunset context.
  - **Concept:** Mirrors user sleep quality and encourages wind-down rituals; prolonged poor sleep risks runaway.
  - **Appearance cues:** Snoring, hanging sloth overnight; messy fur and dark circles after insufficient rest; zen smile after
    quality sleep streaks.
  - **Implementation hooks:** Pull sleep sessions from Samsung Health or Health Services; animate state transitions at sleep
    start/end; track streaks for evolution; push gentle bedtime reminders.
- [ ] **LumiLizard — Light & Dark Reactive Pet**
  - **Watch face baseline:** Blend time/date with ambient light indicators; offer sunrise/sunset complication slots.
  - **Concept:** Reacts to ambient light/time-of-day balance; encourages daylight exposure and restful nights.
  - **Appearance cues:** Sun-dragon form with warm glow in daylight; moonlit chameleon with bat wings in darkness or nighttime
    wakefulness.
  - **Implementation hooks:** Sample ambient light sensor; merge with local time/sunrise calculations; log daylight minutes; set
    dual evolution paths for sun- vs moon-focused care.
- [ ] **DecibelDog — Sound-Sensitive Pup**
  - **Watch face baseline:** Keep central time/date clear while adding subtle volume meters as complications.
  - **Concept:** Responds to ambient noise, user speech, and music; thrives on interactive sound play.
  - **Appearance cues:** Relaxed pup in quiet; paws over ears in loud spaces; tail-wag head tilt when hearing owner; dancing/howl
    when music detected.
  - **Implementation hooks:** Sample microphone amplitude bursts with RECORD_AUDIO consent; optionally integrate speech
    recognition sessions; tie dance mode to media-session metadata; evolve via cumulative interaction minutes.
- [ ] **RoverFox — Location & Travel Explorer Pet**
  - **Watch face baseline:** Combine time/date with distance-traveled and next-location badge complications.
  - **Concept:** Celebrates exploration, new GPS locations, and walking adventures; neglect triggers wandering off.
  - **Appearance cues:** Backpacked fox with souvenir icons for new locales; restless pacing when stationary for days.
  - **Implementation hooks:** Use FusedLocationProvider (phone or watch) with batching; log unique locations/badges; limit GPS
    sampling to motion events; enable Nearby Connections adoption when abandonment thresholds met.
- [ ] **Mounty — Elevation & Climbing Pet**
  - **Watch face baseline:** Integrate time/date with floor-count or elevation-gain complications and summit progress meters.
  - **Concept:** Gains energy from stair climbs and hikes; sedentary flat days reduce morale.
  - **Appearance cues:** Goat bounding up slopes on ascents; grazing boredom when flat; celebratory summit flag for milestones.
  - **Implementation hooks:** Read barometer/pressure sensor for floor detection; filter via step activity; compare totals to
    mountain benchmarks; trigger evolution at cumulative elevation goals.
- [ ] **Thermagon — Temperature-Driven Dragon**
  - **Watch face baseline:** Pair time/date with skin/ambient temperature complications and fever alerts without clutter.
  - **Concept:** Reacts to temperature extremes; encourages climate awareness and safe ranges.
  - **Appearance cues:** Blue shivering dragon in cold; red panting dragon when hot; dual-element adult form after adaptation.
  - **Implementation hooks:** Access skin-temp API or weather service; set comfort thresholds; notify on fever-like spikes; unlock
    seasonal evolutions after experiencing varied climates.
- [ ] **ZenPanda — Stress & Calm Companion**
  - **Watch face baseline:** Show time/date with stress or HRV complications plus breathing timer shortcuts.
  - **Concept:** Biofeedback pet that mirrors stress levels and rewards mindfulness practices.
  - **Appearance cues:** Meditative lotus pose when calm; pacing with stress clouds during tension; levitating guru form at peak
    evolution.
  - **Implementation hooks:** Consume HRV/stress metrics from Samsung Health or derived HR data; embed guided breathing micro-app;
    log calm minutes for streak-based evolution; escalate visual cues before notifications.
- [ ] **JiggleJelly — Motion-Interactive Play Pet**
  - **Watch face baseline:** Maintain clear clock/date while dedicating a complication to play-state/energy and minimizing motion
    occlusion.
  - **Concept:** Responds to wrist gestures and playful shakes; kinetic energy keeps it alive.
  - **Appearance cues:** Squishy blob deforming with shakes; dangling from top when watch inverted; dull, flattened jelly when
    ignored.
  - **Implementation hooks:** Use accelerometer/gyroscope listeners during active sessions; detect shakes/tilts/flicks; convert
    activity into energy points; fall back to manual feeds when motion data unavailable.

#### Activity & Habit-Driven DigiPets

- [ ] **LexiOwl — Language Learning Owl**
  - **Watch face baseline:** Preserve time/date readability while surfacing daily lesson streak and vocabulary complication slots.
  - **Concept:** Thrives on language practice, speaking drills, and streak maintenance; neglect prompts it to seek new knowledge
    elsewhere.
  - **Appearance cues:** Cap-and-book owl hooting happily after lessons; expectant stare holding flashcards when sessions missed.
  - **Implementation hooks:** Integrate with language apps via notifications/APIs or manual logging; support speech recognition
    practice; evolve with streaks and vocabulary milestones.
- [ ] **EchoParrot — Voice Assistant Mimic Pet**
  - **Watch face baseline:** Keep clock/date front-and-center with a conversation counter complication and mic access toggle.
  - **Concept:** Mimics user speech and celebrates frequent voice interactions; grows lonely during silence.
  - **Appearance cues:** Vibrant parrot flapping and lip-syncing during chats; drooping bird asking questions when unheard.
  - **Implementation hooks:** Provide push-to-talk sessions with SpeechRecognizer/TextToSpeech; optionally monitor media session
    or call states via companion; store safe vocabulary snippets for emergent dialogue.
- [ ] **Memophant — Note-Taking Elephant**
  - **Watch face baseline:** Combine time/date with quick-note shortcut and pending reminder complication.
  - **Concept:** Encourages capturing notes/tasks; rewards consistent information logging and recall.
  - **Appearance cues:** Elephant cataloging sticky notes when fed; concerned trunk offering reminders when backlog grows.
  - **Implementation hooks:** Sync with note/task APIs or in-app ledger; allow voice dictation; schedule pet-driven reminders;
    quiz user on past entries for bonus morale.
- [ ] **BusyBee — Productivity & To-Do Bee**
  - **Watch face baseline:** Balance time/date with task-progress gauges and honey-meter complication slots.
  - **Concept:** Gains energy from completed tasks and focus sessions; overwhelmed by unchecked queues.
  - **Appearance cues:** Celebratory waggle dance after completions; buried-under-paper animation when overdue tasks pile up.
  - **Implementation hooks:** Tie into Google Tasks/Todoist APIs or pet-native checklist; reward Pomodoro timers; log completion
    streaks for evolution; send gentle two-per-day reminders respecting notification cap.
- [ ] **BuddyPup — Social Interaction Dog**
  - **Watch face baseline:** Keep time/date visible with social-activity complication summarizing calls/messages/meetups.
  - **Concept:** Reflects user’s communication cadence; motivates regular outreach and shared moments.
  - **Appearance cues:** Tail-wagging pup delivering envelopes post-interaction; lonely whimper animation after prolonged silence.
  - **Implementation hooks:** Mirror call/message counts via companion notification listener (with consent); allow manual logging
    of in-person meetups; support Nearby co-play when two pets are close; evolve via sustained social goals.
- [ ] **BeatBunny — Music-Loving Dancing Rabbit**
  - **Watch face baseline:** Anchor clock/date while showcasing now-playing or tempo complications and keeping dance space clear.
  - **Concept:** Lives for music playback and rhythm games; languishes without regular listening.
  - **Appearance cues:** Genre-specific dances with musical notes; bored flop when environment is silent for days.
  - **Implementation hooks:** Detect active media sessions/notifications; optional mic beat checks; offer tap-to-rhythm mini-games;
    evolve through cumulative listening minutes and genre diversity.
- [ ] **BookWorm — Reading & Knowledge Pet**
  - **Watch face baseline:** Combine time/date with reading streak progress and quick journal entry complication.
  - **Concept:** Feeds on books/articles consumed; metamorphoses as user builds a reading habit.
  - **Appearance cues:** Page-munching caterpillar during sessions; cocooning when neglected; butterfly form after major goals.
  - **Implementation hooks:** Track reading app usage via usage stats/APIs or manual timers; integrate fact-of-the-day prompts;
    evolve on streaks, completed books, and total minutes read.
- [ ] **ShutterBug — Photography & Creativity Pet**
  - **Watch face baseline:** Show time/date with photo-count complication and camera remote shortcut without obscuring dial.
  - **Concept:** Thrives on new photos and creative prompts; fades when no moments are captured.
  - **Appearance cues:** Ladybug flashing camera after shots; colorless carapace when photo drought persists; vibrant shell when
    gallery grows.
  - **Implementation hooks:** Companion monitors camera roll for new media (READ_MEDIA_IMAGES); surface daily photo challenges;
    optionally sync thumbnails to phone app; evolve as albums fill and prompt quests completed.
- [ ] **TranquiliTurtle — Meditation & Mindfulness Turtle**
  - **Watch face baseline:** Keep time/date legible with mindfulness-minute complication and quick-start breathing control.
  - **Concept:** Rewards guided breathing, meditation, and calm streaks; hides in shell when routines lapse.
  - **Appearance cues:** Floating lotus pose during sessions; shell-withdrawn turtle signaling missed practices; radiant aura at
    master tier.
  - **Implementation hooks:** Pull mindfulness minutes from Health Services or built-in exercises; include pet-led breathing UI;
    track streaks and total minutes; gently prompt when stress trends high.
- [ ] **VoltVampire — Tech Use & Battery Pet**
  - **Watch face baseline:** Present time/date with dual battery complications (watch/phone) and charging reminders without
    obscuring the dial.
  - **Concept:** Feeds on healthy charging habits; weakens when devices regularly hit critical battery or over-discharge.
  - **Appearance cues:** Glowing bat sipping energy during charges; pale, swooning sprite when batteries near zero; regal vampire
    persona after sustained battery stewardship.
  - **Implementation hooks:** Monitor watch battery locally and sync phone battery via Data Layer; award timely-charge points;
    trigger low-battery nudges; log deep discharges to influence runaway/death sequences.

### Retention Expectations & Ritual Design
- **Engagement cadence:** Craft daily micro-rituals (feed, play, quick training) taking <90 seconds, weekly depth loops (adventures, co-op quests), and monthly aspirational milestones (rare evolutions, habitat overhauls). Track activation, day-7, and day-30 retention with explicit thresholds per ritual type.
- **Notification philosophy:** Favor ambient nudges through pet behavior rather than push spam. When notifications are required, cap at two actionable alerts per day, contextualized with streak progress and sent during empirically successful engagement windows.
- **Re-engagement safety net:** If a pet flees due to neglect, provide a compassionate return quest (e.g., “complete two self-care tasks to earn back trust”) to avoid permanent loss while still reinforcing accountability.
- **Analytics instrumentation:** Log interaction categories, streak outcomes, social feature usage, and evolution progress. Correlate with health goals to ensure personalized triggers feel attainable; revise thresholds if churn indicators spike.
- **Content refresh schedule:** Maintain a rotating calendar of limited-time accessories, social quests, and ambient animations so returning players discover novelty without overwhelming new users.

### QA Rigor Enhancements
- **Companion simulation suite:** Build automated scenarios that fast-forward lifecycle states (happy → neglected → runaway → reunion) and assert UI/animation fidelity in each stage. Capture golden videos or frame sequences for regression tracking.
- **Device matrix:** Expand beyond Watch8 Classic to include smaller/larger bezels and different brightness profiles. Document pass/fail matrices in `docs/qa/device_matrix.md` with firmware versions and ambient-mode screenshots.
- **Complication/tile contract tests:** Validate pause/resume semantics by programmatically switching watch faces and confirming timers, animations, and data flows halt and resume without drift.
- **Audio consent verification:** Unit test preference toggles ensuring pet audio mirroring only activates when explicit consent is stored, and that revoking access silences features immediately.
- **Social sandbox:** Mock co-op sessions to verify prank consent, cooldown timers, and cross-account state sync.

### Repository Structure & Standards Deep Dive
- **Modular boundaries:** Keep watch face declarative assets in `app/src/main/res/raw/` and Kotlin orchestration in `app/src/main/java/com/cosmobond/...`. Create dedicated packages for evolution logic, social networking, and analytics sinks to prevent monolith controllers.
- **Documentation map:** Expand `docs/` with subfolders (`docs/design/companions`, `docs/retention`, `docs/social`) containing personas, ritual maps, and state charts. Update the decision log whenever structure changes.
- **Naming conventions:** Use `Companion` prefix for pet-related classes (`CompanionStateMachine`, `CompanionAudioSync`), `Habitat` for environment modules, and suffix tests with `Test`/`Spec` consistently.
- **Asset governance:** Store layered art in `art/source/` (PSD/AI) with exported runtime assets in `art/export/`. Include README files describing export settings, compression, and color profiles.

### File Management & Coding Practices — Extra Steps
- **State persistence:** Centralize pet state serialization in a single repository (e.g., `CompanionStateStore`) with migration tests whenever schema changes.
- **Feature flags:** Introduce remote-config toggles for experimental social features or evolution variants to stage rollouts safely.
- **Accessibility reviews:** Document accessible color pairs and animation comfort settings; ensure every new animation ships with reduced-motion alternatives.
- **Localization readiness:** Prepare strings for internationalization from day one, storing copy in `res/values/strings_companion.xml` with developer-facing descriptions.
- **Performance budgets:** Set frame-time budgets (≤16ms for interactive scenes, ≤33ms ambient) and monitor with Baseline Profiles plus macrobenchmarks.
- **Security hygiene:** Sanitize network payloads for social features, rotate OAuth tokens regularly, and limit scopes to least-privilege.

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
   - **Steps:**
     1. Inspect `app/build.gradle.kts`, `settings.gradle.kts`, and `app/src/main/AndroidManifest.xml` for namespace, module includes, and SDK configuration.
     2. Execute `./gradlew :app:assembleDebug` from the repository root and save the terminal output to `docs/project/logs/phase1-task4-build.txt`.
     3. Verify the generated APK under `app/build/outputs/apk/debug/` and note Gradle wrapper details in `docs/project/decision_log.md`.
     4. Record any dependency or configuration follow-ups in the same decision log entry.
   - **Acceptance:** Debug build succeeds; notes recorded in decision log.
   - **Artifacts:** Build log, Gradle wrapper version, decision log entry.
   - **Fail?:** Resolve version mismatches or missing dependencies and rerun.

5. **[ ] Prompt:** _"Expand the Watch Face Format v2 skeleton. Enhance `res/raw/watchface.xml` with initial layout elements, verify the service metadata and preview assets, and build a release bundle with `./gradlew :app:assembleRelease` to confirm the resource packaging."_
   - **Purpose:** Flesh out declarative structure and previews.
   - **Steps:**
     1. Update `app/src/main/res/raw/watchface.xml` with the initial layout groups, elements, and complication bindings.
     2. Cross-check `app/src/main/AndroidManifest.xml` and `app/src/main/res/xml/watch_face_config.xml` (if introduced) for matching service metadata and preview references.
     3. Ensure preview assets live under `app/src/main/res/drawable-nodpi/` or the appropriate density directory referenced by the manifest.
     4. Run `./gradlew :app:assembleRelease` and archive the AAB from `app/build/outputs/bundle/release/` alongside captured build logs in `docs/project/logs/`.
   - **Acceptance:** Release bundle contains WFF resource; previews render.
   - **Artifacts:** AAB artifact, manifest diff, screenshots.
   - **Fail?:** Correct resource paths or metadata and rebuild.

6. **[ ] Prompt (optional Kotlin track):** _"Introduce the Jetpack Watch Face renderer. Add the `androidx.wear.watchface:watchface` dependencies, implement the `WatchFaceService`, `ComplicationSlotsManager`, and `UserStyleSchema`, and prove the renderer with a passing unit test preview."_
   - **Purpose:** Code-rendered face (if not using WFF).
   - **Steps:**
     1. Add the required `androidx.wear.watchface` dependencies to `app/build.gradle.kts`, including the `watchface`, `watchface-style`, and `watchface-complications-data-source` artifacts.
     2. Implement `CosmoBondWatchFaceService` and related renderer classes under `app/src/main/java/com/cosmobond/watchface/`, wiring a `ComplicationSlotsManager` and `UserStyleSchema` implementation.
     3. Create unit test previews in `app/src/test/java/com/cosmobond/watchface/` that exercise the renderer and validate slot registration.
     4. Run `./gradlew testDebugUnitTest --tests "*WatchFaceRenderer*"` (or the specific class name) and capture the passing output for the task evidence.
   - **Acceptance:** Unit test renders preview and compiles.
   - **Artifacts:** Sample screenshot.
   - **Fail?:** Reconcile dependencies/Compose Canvas usage.

---

### Phase 2 — Quality Gates (Local)

7. **[ ] Prompt:** _"Establish static analysis and style enforcement. Add ktlint with Spotless, configure Android Lint to treat new issues as fatal (with a baseline), and wire in a Detekt ruleset, then verify with `./gradlew spotlessApply detekt lint`."_
   - **Purpose:** Enforce Kotlin style and code health.
   - **Steps:**
     1. Configure Spotless with ktlint inside `build.gradle.kts` or `app/build.gradle.kts`, and add any project-wide settings to `gradle/spotless.klint.gradle` if needed.
     2. Add Detekt configuration under `config/detekt/detekt.yml` (create the directory if missing) and enable fatal new issues via `app/build.gradle.kts` lintOptions baseline setup.
     3. Generate or update `app/lint-baseline.xml` after resolving findings so the lint task can treat new issues as fatal.
     4. Run `./gradlew spotlessApply detekt lint` and store the command output plus generated reports (`app/build/reports/`) in `docs/qa/static-analysis/`.
   - **Acceptance:** `./gradlew spotlessApply detekt lint` passes.
   - **Artifacts:** Lint HTML report, Detekt SARIF, Spotless status.
   - **Fail?:** Fix violations or adjust rules narrowly.

8. **[ ] Prompt:** _"Add unit tests for the non-UI logic. Implement `src/test` coverage for time formatting, color scheme selection, and complication ID mapping, then confirm `./gradlew testDebugUnitTest` passes with ≥70% coverage on the core utilities."_
   - **Purpose:** Cover non-UI logic (style schema parsing, config).
   - **Steps:**
     1. Identify the core utility classes under `app/src/main/java/com/cosmobond/` that handle time formatting, palette selection, and complication ID mapping.
     2. Add corresponding test cases to `app/src/test/java/com/cosmobond/` ensuring edge cases and expected mappings are asserted.
     3. Execute `./gradlew testDebugUnitTest` and collect the JUnit XML plus JaCoCo coverage report from `app/build/reports/tests/` and `app/build/reports/jacoco/`.
     4. Summarize the achieved coverage percentage and notable findings in `docs/qa/unit-testing.md` (or create the file if absent).
   - **Acceptance:** `./gradlew testDebugUnitTest` green, coverage ≥ 70% for core utils.
   - **Artifacts:** JUnit XML, coverage report.
   - **Fail?:** Add tests/fix logic.

9. **[ ] Prompt:** _"Create screenshot tests for the previews. For WFF, write an instrumentation test that loads `watchface.xml` and captures frames via the wear-watchface screenshot API (use Paparazzi/Compose for the Kotlin track). Commit the golden images to `app/src/androidTest/assets/goldens/` and surface them in CI."_
   - **Purpose:** Lock visual baselines (dark/light/AOD).
   - **Steps:**
     1. Set up instrumentation or Paparazzi tests under `app/src/androidTest/java/com/cosmobond/watchface/` that exercise `app/src/main/res/raw/watchface.xml` configurations.
     2. Ensure golden directories exist at `app/src/androidTest/assets/goldens/` and save dark, light, and ambient PNGs there with deterministic filenames.
     3. Run `./gradlew connectedDebugAndroidTest` (for device/emulator flows) or the Paparazzi Gradle task and export the generated screenshots/logs to `docs/qa/screenshots/`.
     4. Update CI configuration notes in `docs/project/decision_log.md` describing how the goldens will be verified on pull requests.
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
