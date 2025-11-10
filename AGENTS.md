# CosmoBond Roadmap for AI Agents

## Agent Orientation
- **Platform focus:** Build the CosmoBond Galaxy Watch8 Classic experience using Watch Face Format v2 with a lightweight Kotlin host for configuration and complications.
- **Toolchain expectations:** Android Gradle Plugin 8.5+, Kotlin 1.9+, Gradle wrapper (JDK 17), GitHub Actions for CI/CD, Gradle Play Publisher for distribution, Google Play internal→production release tracks.
- **Workflow rule:** Progress strictly in checklist order across the baseline CI/CD phases. Claim an item by appending `_(claimed by @agent, YYYY-MM-DD HH:mm UTC)_` to the task line before making changes. Complete all acceptance checks before marking `[x]` and append `_Completed: summary (timestamp)_`. Use `_Blocked: reason (timestamp)_` if stalled. Do not pull from the DigiPet backlog until every baseline phase item relevant to the current scope is complete and documented.
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

### DigiPet Watch Face Expansion Backlog _(Optional — post-baseline)_

> **Unlock condition:** Tackle these DigiPet expansion items only after finishing the baseline CI/CD phases (Phases 1–9) relevant to your assignment. They are exempt from the strict sequencing rule once unlocked, allowing agents to pick the pet initiative that best matches current priorities.

- **Universal watch face requirements:** Each DigiPet remains a fully functional CosmoBond watch face. Reserve space for time, date, battery, and at least two configurable complications so wearers can tailor health, productivity, or communication data without obscuring the companion. Use ambient-mode variants that keep the time legible while dimming pet animations.

##### Watch face animation asset checklist

- **Shared directory schema:** Export every frame sequence or vector animation into `art/export/pets/<pet>/<state>/`. Mirror the same state name in the Watch Face Format payload (`app/src/main/res/raw/<pet>_<state>.xml` or `.wff`) and fallback preview sprites stored under `app/src/main/res/drawable/<pet>_<state>_preview.*`.
- **CardioCritter states:**
  - `ambient_idle`, `healthy_workout`, `cooldown_breathing`, `unhealthy_slouch`, `runaway_departure`.
- **StepSprite states:**
  - `ambient_idle`, `goal_celebration`, `idle_slump`, `manual_feed`, `runaway_departure`.
- **SomnoSloth states:**
  - `ambient_idle`, `sleep_cycle`, `rested_morning`, `sleep_deprived`, `runaway_departure`.
- **LumiLizard states:**
  - `ambient_idle`, `sun_charged`, `moonlit_watch`, `overexposed`, `runaway_departure`.
- **DecibelDog states:**
  - `ambient_idle`, `quiet_relax`, `conversation_perk`, `music_dance`, `noise_overload`, `runaway_departure`.
- **RoverFox states:**
  - `ambient_idle`, `explore_stride`, `souvenir_show`, `stationary_restless`, `runaway_departure`.
- **Mounty states:**
  - `ambient_idle`, `climb_push`, `summit_victory`, `overworked_exhausted`, `runaway_departure`.
- **Thermagon states:**
  - `ambient_idle`, `temperature_balance`, `heatwave_flush`, `cold_snap_shiver`, `runaway_departure`.
- **ZenPanda states:**
  - `ambient_idle`, `calm_breathe`, `stress_alert`, `mindful_checkpoint`, `runaway_departure`.
- **JiggleJelly states:**
  - `ambient_idle`, `playful_bounce`, `motion_stretch`, `neglected_still`, `runaway_departure`.
- **LexiOwl states:**
  - `ambient_idle`, `lesson_focus`, `quiz_success`, `missed_session`, `runaway_departure`.
- **EchoParrot states:**
  - `ambient_idle`, `voice_listen`, `command_echo`, `silence_wait`, `runaway_departure`.
- **Memophant states:**
  - `ambient_idle`, `note_delivery`, `overdue_burdened`, `review_celebration`, `runaway_departure`.
- **BusyBee states:**
  - `ambient_idle`, `task_complete`, `overdue_swarm`, `planning_focus`, `runaway_departure`.
- **BuddyPup states:**
  - `ambient_idle`, `social_greet`, `lonely_whimper`, `message_delivery`, `runaway_departure`.
- **BeatBunny states:**
  - `ambient_idle`, `beat_drop`, `tempo_change`, `silence_pause`, `runaway_departure`.
- **BookWorm states:**
  - `ambient_idle`, `reading_glow`, `knowledge_share`, `stalled_progress`, `runaway_departure`.
- **ShutterBug states:**
  - `ambient_idle`, `photo_snap`, `gallery_proud`, `creative_block`, `runaway_departure`.
- **TranquiliTurtle states:**
  - `ambient_idle`, `meditation_pose`, `stress_warning`, `breath_coaching`, `runaway_departure`.
- **VoltVampire states:**
  - `ambient_idle`, `charge_boost`, `battery_warning`, `energy_saver`, `runaway_departure`.

###### Animation export & integration workflow

1. **Prep source timelines:** Open the pet’s master project under `art/source/pets/<pet>/` (After Effects, Blender, or Spine as defined in the pet brief). Confirm layers follow the state names above so exported folders inherit deterministic identifiers.
2. **Batch export:** Run `./gradlew :art:exportWatchFace --pet <pet>` (or use the matching script documented beside the project) to render Watch Face Format frame sequences into `art/export/pets/<pet>/<state>/`. For manual exports, render PNG sequences at 60 fps, trim to the watch-face safe zone, and drop them into the same directory structure.
3. **Optimize assets:** Execute `./gradlew :art:optimizeWatchFace --pet <pet>` to convert sequences into animated WebP/WebM (ambient) and vector drawables (low-bit) variants. Verify the optimizer outputs `.wff` or `.xml` payloads and preview sprites per state.
4. **Commit expectations:** Stage the generated directories plus optimized resources under `app/src/main/res/raw/` and `app/src/main/res/drawable/`. Include the export logs emitted under `art/export/pets/<pet>/export.log` so reviewers can trace tooling versions.
5. **Wire into Watch Face Format:**
   - Update `app/src/main/res/raw/watchface.xml` so each `state` element references the new `@raw/<pet>_<state>` assets and defines transitions tied to the gameplay state machine.
   - For Canvas/Kotlin fallback, edit `app/src/main/java/.../renderer/<Pet>Renderer.kt` to load the matching drawable previews when Watch Face Format assets are unavailable (e.g., ambient low-bit mode).
6. **Verification:** Run `./gradlew :app:assembleDebug` to ensure the build packages new raw assets, then preview on device/emulator to confirm state transitions map to the expected animations. Capture before/after GIFs or frame dumps and save them under `docs/pets/<pet>/`.
7. **Documentation update:** Append an entry to `docs/pets/<pet>/animation.md` summarizing export settings, optimization parameters, and integration commit hash before completing the checklist item.
- **Shared DigiPet Evidence Primer:** All DigiPets must retain uninterrupted timekeeping, include automated state-transition coverage, capture before/after visuals for happy vs. neglected states, and document key metrics in `docs/pets/<pet>/` alongside the relevant gradle command log. Reference this primer in each pet-specific acceptance checklist.

###### Generative animation sourcing

When AI video tools are used to draft state timelines, capture the same evidence trail as hand-built exports and store raw plus processed files beside the existing `art/export/pets/<pet>/<state>/` directories.

- **Sora 2 (OpenAI video sandbox):**
  1. Request access through the OpenAI Research Preview portal, then launch the Sora workspace session assigned to your CosmoBond project.
  2. Craft prompts that lock resolution, cadence, and background hygiene: `"4K square 2160x2160 creature animation, 60 fps, 6-second loop, flat studio light, chroma key green background"`. Explicitly mention whether you need negative space for watch complications and remind the model to avoid props outside the safe zone.
  3. Generate the take, download the MP4 master from the session’s “Exports” drawer, and log the model version plus safety filters in `docs/pets/<pet>/animation.md`.
  4. Convert to CosmoBond frame sequences with `ffmpeg -i sora-output.mp4 -vf "fps=60" art/export/pets/<pet>/<state>/%04d.png` and store the untouched MP4 under `art/source/pets/<pet>/generative/sora/` using the naming pattern `<state>_sora2_<yyyymmdd>.mp4`.
  5. Note OpenAI’s usage policy and capture any consent documentation if live-action elements or personal likenesses were referenced; do not ingest runs that violate the policy.

- **Veo 3.1 (Google VideoFX beta):**
  1. Open Vertex AI > VideoFX, ensure the `cosmobond` project is active, and start a Veo 3.1 prompt card with the “Cinematic loop” template.
  2. Include framing metadata such as `resolution: 2048x2048`, `duration_seconds: 6`, `frame_rate: 60`, and specify `background: solid #00FF00 for transparency keying` inside the structured prompt field.
  3. Export the resulting `.mp4` plus the optional `.json` prompt metadata; keep both under `art/source/pets/<pet>/generative/veo/` following `<state>_veo31_<yyyymmdd>.mp4` naming.
  4. Use `ffmpeg -i <state>_veo31_<yyyymmdd>.mp4 -vf "fps=60,format=rgba" art/export/pets/<pet>/<state>/%04d.png` if alpha is unavailable, then manually key out the solid background before optimization.
  5. Respect Google’s VideoFX terms, especially around third-party IP and consent for any people depicted; log approvals in the pet’s documentation folder.

- **Gemini (Imagen + VideoFX pipelines):**
  1. Launch the Gemini Studio workspace and pick either the Imagen still-to-video or VideoFX motion pipeline based on state needs.
  2. Define prompt variables that match CosmoBond constraints: specify a square render (minimum 1024×1024), 60 fps, ≤6 seconds, and require either transparent PNG export or a single-color background.
  3. Download both the `.mp4` animation and the `.zip` bundle of intermediate PNG frames when available; save them under `art/source/pets/<pet>/generative/gemini/` as `<state>_gemini_<yyyymmdd>.mp4` and `<state>_gemini_<yyyymmdd>.zip`.
  4. If PNG frames are not supplied, create them locally with `ffmpeg` using the same `fps=60` filter, then drop the processed sequence into `art/export/pets/<pet>/<state>/`.
  5. Add a consent note when source prompts include recognizable brands, locations, or contributors and confirm the Gemini output license allows redistribution inside the CosmoBond repository.

- **ChatGPT (DALL·E + frame export extension):**
  1. Start a ChatGPT session with the DALL·E video plug-in enabled, then issue a storyboard prompt plus a follow-up request for a 60 fps loop rendered against a chroma-key background.
  2. Use DALL·E’s “Advanced settings” to force a 2048×2048 aspect ratio, limit duration to 6 seconds, and include `"background": "flat #00FF00"` instructions for easier compositing.
  3. Download the delivered `.mp4` or `.mov` file along with any `.json` prompt export, saving them to `art/source/pets/<pet>/generative/chatgpt/` named `<state>_chatgpt_<yyyymmdd>.mp4`.
  4. Run `ffmpeg -i <state>_chatgpt_<yyyymmdd>.mp4 -vf "fps=60" art/export/pets/<pet>/<state>/%04d.png`, then review frames for artifacts before committing.
  5. DALL·E inherits OpenAI content policy—log stylistic inspirations, avoid real-person likenesses without written consent, and document approvals in the pet’s evidence notes.

After generating sequences, bundle each tool’s raw deliverables into `art/source/pets/<pet>/generative/<tool>/` (keep MP4s, JSON prompts, and QA notes) and deposit cleaned PNG frames inside `art/export/pets/<pet>/<state>/`. Zip the entire `generative/<tool>/` directory into `art/source/pets/<pet>/archives/<pet>_<state>_<tool>_<yyyymmdd>.zip`, then reference the archive path plus the processed export folder in `docs/pets/<pet>/animation.md`. When submitting checklist evidence, attach the zip alongside the optimized assets so downstream agents can reproduce conversions or rerun optimization jobs without repeating the AI generation step.

#### Sensor-Driven DigiPets

- [ ] **CardioCritter — Heart Rate Fitness Monster**
  - **Watch face baseline:** Highlight primary time/date complications, add workout progress rings, and mirror key vitals in sub-complications without hiding the clock.
  - **Concept:** Thrives on heart-health consistency; syncs to resting HR, workouts, and gym visits to reflect care.
  - **Appearance cues:** Rosy, athletic animations during healthy HR/workouts; pale, slouched stance with broken-heart icon when readings stay unhealthy.
  - **Implementation hooks:** Use Health Services heart-rate samples, workout sessions, and optional geofenced gym detection; throttle polling for battery; evolve after multi-day healthy ranges; trigger Nearby Connections runaway when long-term HR neglect persists.
  - **Steps:**
    * Prototype the Watch Face Format layout with dual vitals complications and animated cardio rings.
    * Pipe in continuous and session-based heart-rate data, smoothing spikes and mapping thresholds to CardioCritter emotional states.
    * Script evolution/runaway sequences tied to streak achievements and chronic neglect, syncing Nearby Connections handoff triggers.
  - **Acceptance:**
    * Satisfies the **Shared DigiPet Evidence Primer**.
    * Heart-rate zones drive visible animation swaps within 5 seconds of sensor updates without obscuring timekeeping.
    * Evolution unlocks only after configured healthy-range streak and runaway triggers when sustained unhealthy averages persist.
    * Animation assets listed in the checklist are committed under `art/export/pets/cardiocritter/` with matching `@raw/@drawable` resources and documented in `docs/pets/cardiocritter/`.
  - **Artifacts:**
    * Updated watchface layout diff plus animation reference sheet for cardio states.
    * Sensor replay logs showing heart-rate threshold transitions and resulting animations.
    * Nearby Connections trigger notes confirming runaway handoff behavior.
    * `docs/pets/cardiocritter/animation.md` updated with export settings, optimization output, and asset verification screenshots.

- [ ] **StepSprite — Step Count Companion**
  - **Watch face baseline:** Pair time/date with a step-progress complication ring and configurable secondary slots for goal streaks.
  - **Concept:** Feeds on step goals and celebrates streaks; idleness drains energy and risks escape.
  - **Appearance cues:** Happy, fast animations when goals met; sluggish, yawning sprite with boredom icon on sedentary days.
  - **Implementation hooks:** Subscribe to step-count deltas via Health Services or Google Fit; add idle reminders; evolve after multi-day goal streaks; treat steps and manual feeds as hunger refills.
  - **Steps:**
    * Build animated progress ring visuals and secondary streak complication tied to configurable daily targets.
    * Connect Health Services step streams and fall back to manual feed interactions when unavailable.
    * Implement streak tracker influencing StepSprite energy, evolutions, and idle-warning behaviors.
  - **Acceptance:**
    * Satisfies the **Shared DigiPet Evidence Primer**.
    * Step goal completion updates the watch face within one minute and unlocks celebratory animations.
    * Consecutive idle periods trigger gentle nudges before runaway state initiates after configured neglect window.
    * Animation assets listed in the checklist are committed under `art/export/pets/stepsprite/` with matching `@raw/@drawable` resources and documented in `docs/pets/stepsprite/`.
  - **Artifacts:**
    * Streak calculator unit test output and animation capture for goal celebrations vs. idle slump.
    * Logs illustrating Health Services ingestion and manual override fallback.
    * Documentation of idle reminder cadence and runaway timing in `docs/pets/stepsprite/`.
    * `docs/pets/stepsprite/animation.md` updated with export settings, optimization output, and asset verification screenshots.

- [ ] **SomnoSloth — Sleepy Sloth Pet**
  - **Watch face baseline:** Surface time/date alongside last-night sleep summary complications and sunrise/sunset context.
  - **Concept:** Mirrors user sleep quality and encourages wind-down rituals; prolonged poor sleep risks runaway.
  - **Appearance cues:** Snoring, hanging sloth overnight; messy fur and dark circles after insufficient rest; zen smile after quality sleep streaks.
  - **Implementation hooks:** Pull sleep sessions from Samsung Health or Health Services; animate state transitions at sleep start/end; track streaks for evolution; push gentle bedtime reminders.
  - **Steps:**
    * Design dual complications for prior-night duration and current streak health aligned with circadian cues.
    * Integrate sleep session ingestion with smoothing for naps vs. full cycles and schedule transition animations at bedtime/wake events.
    * Configure evolution, reminder, and runaway logic based on sustained sleep quality thresholds.
  - **Acceptance:**
    * Satisfies the **Shared DigiPet Evidence Primer**.
    * Sleep-quality streaks adjust SomnoSloth posture and mood immediately after nightly sync completes.
    * Bedtime reminder cadence adapts to user preference without exceeding notification caps and runaway triggers after repeated poor scores.
    * Animation assets listed in the checklist are committed under `art/export/pets/somnosloth/` with matching `@raw/@drawable` resources and documented in `docs/pets/somnosloth/`.
  - **Artifacts:**
    * Timeline capture showing bedtime, sleep, and wake animations.
    * Sleep session parsing tests plus metrics summary stored with reminder configuration notes.
    * Evidence of runaway recovery quest text and thresholds in `docs/pets/somnosloth/`.
    * `docs/pets/somnosloth/animation.md` updated with export settings, optimization output, and asset verification screenshots.

- [ ] **LumiLizard — Light & Dark Reactive Pet**
  - **Watch face baseline:** Blend time/date with ambient light indicators; offer sunrise/sunset complication slots.
  - **Concept:** Reacts to ambient light/time-of-day balance; encourages daylight exposure and restful nights.
  - **Appearance cues:** Sun-dragon form with warm glow in daylight; moonlit chameleon with bat wings in darkness or nighttime wakefulness.
  - **Implementation hooks:** Sample ambient light sensor; merge with local time/sunrise calculations; log daylight minutes; set dual evolution paths for sun- vs moon-focused care.
  - **Steps:**
    * Create light-exposure complication pairing with sunrise/sunset indicators inside the WFF layout.
    * Fuse ambient light sensor readings with geolocation-based solar data to score daylight vs. nocturnal engagement.
    * Drive dual-path evolution art states and runaways based on imbalance, providing coaching nudges when thresholds slip.
  - **Acceptance:**
    * Satisfies the **Shared DigiPet Evidence Primer**.
    * Light exposure scoring updates within 60 seconds of environmental change and selects matching animation palette.
    * Evolution branch (solar or lunar) only unlocks after sustained balance and runaway occurs if imbalance persists for configured days.
    * Animation assets listed in the checklist are committed under `art/export/pets/lumilizard/` with matching `@raw/@drawable` resources and documented in `docs/pets/lumilizard/`.
  - **Artifacts:**
    * Sensor sampling chart illustrating smoothing, throttling, and update cadence.
    * Palette/animation storyboard for sun vs. moon personas.
    * Geo/time calculation tests stored with daylight scoring documentation.
    * `docs/pets/lumilizard/animation.md` updated with export settings, optimization output, and asset verification screenshots.

- [ ] **DecibelDog — Sound-Sensitive Pup**
  - **Watch face baseline:** Keep central time/date clear while adding subtle volume meters as complications.
  - **Concept:** Responds to ambient noise, user speech, and music; thrives on interactive sound play.
  - **Appearance cues:** Relaxed pup in quiet; paws over ears in loud spaces; tail-wag head tilt when hearing owner; dancing/howling when music detected.
  - **Implementation hooks:** Sample microphone amplitude bursts with RECORD_AUDIO consent; optionally integrate speech recognition sessions; tie dance mode to media-session metadata; evolve via cumulative interaction minutes.
  - **Steps:**
    * Implement audio-level complication visualization with opt-in privacy gating and microphone status indicators.
    * Process amplitude bursts and media-session callbacks to classify quiet, conversation, and music modes.
    * Link classification outcomes to DecibelDog animations, rewards, and runaway thresholds respecting consent revocation.
  - **Acceptance:**
    * Satisfies the **Shared DigiPet Evidence Primer**.
    * Opt-in prompts and consent revocation immediately start/stop audio sampling with clear UI feedback.
    * Distinct animations trigger for quiet, conversational, and loud/music environments with hysteresis preventing flicker.
    * Animation assets listed in the checklist are committed under `art/export/pets/decibeldog/` with matching `@raw/@drawable` resources and documented in `docs/pets/decibeldog/`.
  - **Artifacts:**
    * Privacy consent flow recording and configuration notes.
    * Classification unit tests with audio fixture summaries.
    * Animation capture for each sound band stored alongside amplitude logs.
    * `docs/pets/decibeldog/animation.md` updated with export settings, optimization output, and asset verification screenshots.

- [ ] **RoverFox — Location & Travel Explorer Pet**
  - **Watch face baseline:** Combine time/date with distance-traveled and next-location badge complications.
  - **Concept:** Celebrates exploration, new GPS locations, and walking adventures; neglect triggers wandering off.
  - **Appearance cues:** Backpacked fox with souvenir icons for new locales; restless pacing when stationary for days.
  - **Implementation hooks:** Use FusedLocationProvider (phone or watch) with batching; log unique locations/badges; limit GPS sampling to motion events; enable Nearby Connections adoption when abandonment thresholds met.
  - **Steps:**
    * Build exploration badge complication with last-trip distance indicator and souvenir iconography.
    * Implement motion-gated location batching, syncing new locale discoveries to RoverFox’s state machine.
    * Script adoption/runaway cutscenes that hand off to Nearby Connections when neglect thresholds are hit.
  - **Acceptance:**
    * Satisfies the **Shared DigiPet Evidence Primer**.
    * Location sampling respects motion gating and logs fewer than the allowed GPS polls per hour when stationary.
    * New locale discovery immediately awards badges and unlocks celebratory animations; runaway triggers after configured stationary duration.
    * Animation assets listed in the checklist are committed under `art/export/pets/roverfox/` with matching `@raw/@drawable` resources and documented in `docs/pets/roverfox/`.
  - **Artifacts:**
    * Location throttling metrics and badge ledger stored in documentation.
    * Animation captures for discovery, idle pacing, and runaway adoption sequences.
    * Consent and privacy note for location sharing appended to `docs/pets/roverfox/`.
    * `docs/pets/roverfox/animation.md` updated with export settings, optimization output, and asset verification screenshots.

- [ ] **Mounty — Elevation & Climbing Pet**
  - **Watch face baseline:** Integrate time/date with floor-count or elevation-gain complications and summit progress meters.
  - **Concept:** Gains energy from stair climbs and hikes; sedentary flat days reduce morale.
  - **Appearance cues:** Goat bounding up slopes on ascents; grazing boredom when flat; celebratory summit flag for milestones.
  - **Implementation hooks:** Read barometer/pressure sensor for floor detection; filter via step activity; compare totals to mountain benchmarks; trigger evolution at cumulative elevation goals.
  - **Steps:**
    * Craft elevation progress complication with configurable summit goals and intermediate checkpoint markers.
    * Calibrate barometer-derived floor detection using activity context and fallback to manual entry when unavailable.
    * Map cumulative elevation achievements to evolution stages and define neglect decay for low-activity periods.
  - **Acceptance:**
    * Satisfies the **Shared DigiPet Evidence Primer**.
    * Elevation gain updates appear on-watch within two minutes of climb completion with validated barometer smoothing.
    * Summit celebrations trigger exactly at goal thresholds and neglect decay follows documented pacing.
    * Animation assets listed in the checklist are committed under `art/export/pets/mounty/` with matching `@raw/@drawable` resources and documented in `docs/pets/mounty/`.
  - **Artifacts:**
    * Calibration dataset showing raw vs. filtered elevation samples.
    * Animation captures for ascent, idle grazing, and summit celebration.
    * Evolution threshold table recorded in `docs/pets/mounty/`.
    * `docs/pets/mounty/animation.md` updated with export settings, optimization output, and asset verification screenshots.

- [ ] **Thermagon — Temperature-Driven Dragon**
  - **Watch face baseline:** Pair time/date with skin/ambient temperature complications and fever alerts without clutter.
  - **Concept:** Reacts to temperature extremes; encourages climate awareness and safe ranges.
  - **Appearance cues:** Blue shivering dragon in cold; red panting dragon when hot; dual-element adult form after adaptation.
  - **Implementation hooks:** Access skin-temp API or weather service; set comfort thresholds; notify on fever-like spikes; unlock seasonal evolutions after experiencing varied climates.
  - **Steps:**
    * Design dual-temperature complications showing current skin temp and ambient/forecast context with alert badges.
    * Integrate temperature readings with hysteresis and fallback to weather APIs when skin data unavailable.
    * Configure adaptation evolutions and safety notifications for sustained extremes, including runaway triggers during prolonged unsafe readings.
  - **Acceptance:**
    * Satisfies the **Shared DigiPet Evidence Primer**.
    * Alerts fire when thresholds breached and auto-clear after readings stabilize within comfort band.
    * Seasonal evolution requires documented exposure to both heat and cold patterns without conflicting with safety notifications.
    * Animation assets listed in the checklist are committed under `art/export/pets/thermagon/` with matching `@raw/@drawable` resources and documented in `docs/pets/thermagon/`.
  - **Artifacts:**
    * Alert log and notification copy stored with threshold configuration spreadsheet.
    * Animation captures for cold, hot, neutral, and dual-element states.
    * Integration tests verifying sensor vs. weather fallback priority.
    * `docs/pets/thermagon/animation.md` updated with export settings, optimization output, and asset verification screenshots.

- [ ] **ZenPanda — Stress & Calm Companion**
  - **Watch face baseline:** Show time/date with stress or HRV complications plus breathing timer shortcuts.
  - **Concept:** Biofeedback pet that mirrors stress levels and rewards mindfulness practices.
  - **Appearance cues:** Meditative lotus pose when calm; pacing with stress clouds during tension; levitating guru form at peak evolution.
  - **Implementation hooks:** Consume HRV/stress metrics from Samsung Health or derived HR data; embed guided breathing micro-app; log calm minutes for streak-based evolution; escalate visual cues before notifications.
  - **Steps:**
    * Layout HRV/stress complication alongside instant-access breathing control and meditation start buttons.
    * Translate HRV readings into stress tiers powering ZenPanda mood changes and escalation cues.
    * Implement calm-minute streak tracking with guided-session completions influencing evolution and runaway forgiveness.
  - **Acceptance:**
    * Satisfies the **Shared DigiPet Evidence Primer**.
    * Stress spikes surface progressive visual cues before notifications and guided breathing entry point launches within one tap.
    * Calm streak milestones unlock evolution tiers; neglecting practices triggers shell-withdrawn state before runaway.
    * Animation assets listed in the checklist are committed under `art/export/pets/zenpanda/` with matching `@raw/@drawable` resources and documented in `docs/pets/zenpanda/`.
  - **Artifacts:**
    * HRV-to-tier mapping chart plus associated unit tests.
    * Session logs demonstrating guided breathing launches and calm-minute accumulation.
    * Visual capture of escalation cues vs. calm glow stored in documentation.
    * `docs/pets/zenpanda/animation.md` updated with export settings, optimization output, and asset verification screenshots.

- [ ] **JiggleJelly — Motion-Interactive Play Pet**
  - **Watch face baseline:** Maintain clear clock/date while dedicating a complication to play-state/energy and minimizing motion occlusion.
  - **Concept:** Responds to wrist gestures and playful shakes; kinetic energy keeps it alive.
  - **Appearance cues:** Squishy blob deforming with shakes; dangling from top when watch inverted; dull, flattened jelly when ignored.
  - **Implementation hooks:** Use accelerometer/gyroscope listeners during active sessions; detect shakes/tilts/flicks; convert activity into energy points; fall back to manual feeds when motion data unavailable.
  - **Steps:**
    * Build play-energy complication with quick access to manual feed controls and energy decay display.
    * Implement motion gesture classifier differentiating shakes, flips, and waves with configurable sensitivity.
    * Link gesture outcomes to animation rigs, energy economy, and runaway timer that pauses when manual feeds triggered.
  - **Acceptance:**
    * Satisfies the **Shared DigiPet Evidence Primer**.
    * Gesture recognition distinguishes at least three motions with less than 5% false positives in recorded tests.
    * Energy decay and replenishment follow documented curve; runaway triggers after sustained inactivity unless manual feed logged.
    * Animation assets listed in the checklist are committed under `art/export/pets/jigglejelly/` with matching `@raw/@drawable` resources and documented in `docs/pets/jigglejelly/`.
  - **Artifacts:**
    * Gesture classifier evaluation report and accelerometer fixture data.
    * Animation captures for shake joy, inverted dangle, and flattened neglect states.
    * Energy economy configuration written to `docs/pets/jigglejelly/`.
    * `docs/pets/jigglejelly/animation.md` updated with export settings, optimization output, and asset verification screenshots.

#### Activity & Habit-Driven DigiPets

- [ ] **LexiOwl — Language Learning Owl**
  - **Watch face baseline:** Preserve time/date readability while surfacing daily lesson streak and vocabulary complication slots.
  - **Concept:** Thrives on language practice, speaking drills, and streak maintenance; neglect prompts it to seek new knowledge elsewhere.
  - **Appearance cues:** Cap-and-book owl hooting happily after lessons; expectant stare holding flashcards when sessions missed.
  - **Implementation hooks:** Integrate with language apps via notifications/APIs or manual logging; support speech recognition practice; evolve with streaks and vocabulary milestones.
  - **Steps:**
    * Implement streak and vocabulary complications with quick-launch link to preferred language exercises.
    * Sync lesson completions via partner APIs or manual log entries, awarding XP for diverse practice modes.
    * Configure evolution path rewarding streak longevity and vocabulary milestones while handling neglect transitions.
  - **Acceptance:**
    * Satisfies the **Shared DigiPet Evidence Primer**.
    * Lesson completion sync updates LexiOwl demeanor before next session reminder and accounts for offline entries.
    * Vocabulary milestone unlocks trigger celebratory animations and log entries while missed sessions degrade morale.
    * Animation assets listed in the checklist are committed under `art/export/pets/lexiowl/` with matching `@raw/@drawable` resources and documented in `docs/pets/lexiowl/`.
  - **Artifacts:**
    * API/notification sync log plus fallback manual entry workflow notes.
    * Animation captures for milestone celebration vs. expectant idle state.
    * Streak decay schedule documented in `docs/pets/lexiowl/`.
    * `docs/pets/lexiowl/animation.md` updated with export settings, optimization output, and asset verification screenshots.

- [ ] **EchoParrot — Voice Assistant Mimic Pet**
  - **Watch face baseline:** Keep clock/date front-and-center with a conversation counter complication and mic access toggle.
  - **Concept:** Mimics user speech and celebrates frequent voice interactions; grows lonely during silence.
  - **Appearance cues:** Vibrant parrot flapping and lip-syncing during chats; drooping bird asking questions when unheard.
  - **Implementation hooks:** Provide push-to-talk sessions with SpeechRecognizer/TextToSpeech; optionally monitor media session or call states via companion; store safe vocabulary snippets for emergent dialogue.
  - **Steps:**
    * Craft conversation counter complication with quick mic toggle and privacy indicators.
    * Implement push-to-talk dialog pipeline capturing safe snippets and mirroring assistant responses.
    * Schedule call/media detection hooks that influence EchoParrot mood and runaway thresholds.
  - **Acceptance:**
    * Satisfies the **Shared DigiPet Evidence Primer**.
    * Voice sessions respect consent toggles, immediately muting capture when disabled.
    * Interaction frequency changes animation state within the next poll cycle and runaway triggers after sustained silence.
    * Animation assets listed in the checklist are committed under `art/export/pets/echoparrot/` with matching `@raw/@drawable` resources and documented in `docs/pets/echoparrot/`.
  - **Artifacts:**
    * Conversation counter test logs and privacy toggle UX capture.
    * Audio session transcript samples with anonymization notes.
    * Mood state change timeline recorded in `docs/pets/echoparrot/`.
    * `docs/pets/echoparrot/animation.md` updated with export settings, optimization output, and asset verification screenshots.

- [ ] **Memophant — Note-Taking Elephant**
  - **Watch face baseline:** Combine time/date with quick-note shortcut and pending reminder complication.
  - **Concept:** Encourages capturing notes/tasks; rewards consistent information logging and recall.
  - **Appearance cues:** Elephant cataloging sticky notes when fed; concerned trunk offering reminders when backlog grows.
  - **Implementation hooks:** Sync with note/task APIs or in-app ledger; allow voice dictation; schedule pet-driven reminders; quiz user on past entries for bonus morale.
  - **Steps:**
    * Add quick-note launcher and backlog complication showcasing pending items and quiz prompts.
    * Connect to task/notes provider APIs with offline-first caching and safe voice dictation handling.
    * Build reminder cadence, quiz interactions, and runaway conditions tied to backlog health.
  - **Acceptance:**
    * Satisfies the **Shared DigiPet Evidence Primer**.
    * Notes sync bi-directionally with provider within user-defined latency, updating Memophant mood accordingly.
    * Reminder and quiz flows respect notification caps and reduce backlog when completed; runaway triggers only after persistent neglect.
    * Animation assets listed in the checklist are committed under `art/export/pets/memophant/` with matching `@raw/@drawable` resources and documented in `docs/pets/memophant/`.
  - **Artifacts:**
    * Sync transaction logs and caching strategy notes.
    * UX recording for reminder and quiz interactions.
    * Backlog scoring rubric captured in `docs/pets/memophant/`.
    * `docs/pets/memophant/animation.md` updated with export settings, optimization output, and asset verification screenshots.

- [ ] **BusyBee — Productivity & To-Do Bee**
  - **Watch face baseline:** Balance time/date with task-progress gauges and honey-meter complication slots.
  - **Concept:** Gains energy from completed tasks and focus sessions; overwhelmed by unchecked queues.
  - **Appearance cues:** Celebratory waggle dance after completions; buried-under-paper animation when overdue tasks pile up.
  - **Implementation hooks:** Tie into Google Tasks/Todoist APIs or pet-native checklist; reward Pomodoro timers; log completion streaks for evolution; send gentle two-per-day reminders respecting notification cap.
  - **Steps:**
    * Implement honey-meter complication summarizing completion percentage and Pomodoro streaks.
    * Integrate with chosen task provider, reconciling completed vs. overdue items and focus sessions.
    * Script morale/evolution logic that rewards streaks and handles backlog overload leading to runaway.
  - **Acceptance:**
    * Satisfies the **Shared DigiPet Evidence Primer**.
    * Task completion sync updates BusyBee animations within the next refresh window; overdue thresholds trigger supportive prompts before runaway.
    * Reminder system never exceeds two actionable alerts per day and respects quiet hours settings.
    * Animation assets listed in the checklist are committed under `art/export/pets/busybee/` with matching `@raw/@drawable` resources and documented in `docs/pets/busybee/`.
  - **Artifacts:**
    * Task provider sync log and reconciliation tests.
    * Animation capture for waggle dance, paperwork overwhelm, and runaway preview.
    * Reminder schedule documentation in `docs/pets/busybee/`.
    * `docs/pets/busybee/animation.md` updated with export settings, optimization output, and asset verification screenshots.

- [ ] **BuddyPup — Social Interaction Dog**
  - **Watch face baseline:** Keep time/date visible with social-activity complication summarizing calls/messages/meetups.
  - **Concept:** Reflects user’s communication cadence; motivates regular outreach and shared moments.
  - **Appearance cues:** Tail-wagging pup delivering envelopes post-interaction; lonely whimper animation after prolonged silence.
  - **Implementation hooks:** Mirror call/message counts via companion notification listener (with consent); allow manual logging of in-person meetups; support Nearby co-play when two pets are close; evolve via sustained social goals.
  - **Steps:**
    * Create social pulse complication aggregating digital communications and manual meetup logs.
    * Implement consent-aware notification listener bridge with manual entry UI for offline meetups.
    * Coordinate co-play detection and runaway behavior tied to communication drought periods.
  - **Acceptance:**
    * Satisfies the **Shared DigiPet Evidence Primer**.
    * Social tally updates within five minutes of interactions and differentiates digital vs. in-person credits.
    * Co-play handshake succeeds between nearby devices and runaway triggers after documented silence duration.
    * Animation assets listed in the checklist are committed under `art/export/pets/buddypup/` with matching `@raw/@drawable` resources and documented in `docs/pets/buddypup/`.
  - **Artifacts:**
    * Interaction log sample plus privacy note for notification listener usage.
    * Animation captures for mail delivery, co-play joy, and lonely whimper states.
    * Documentation of manual meetup entry workflow in `docs/pets/buddypup/`.
    * `docs/pets/buddypup/animation.md` updated with export settings, optimization output, and asset verification screenshots.

- [ ] **BeatBunny — Music-Loving Dancing Rabbit**
  - **Watch face baseline:** Anchor clock/date while showcasing now-playing or tempo complications and keeping dance space clear.
  - **Concept:** Lives for music playback and rhythm games; languishes without regular listening.
  - **Appearance cues:** Genre-specific dances with musical notes; bored flop when environment is silent for days.
  - **Implementation hooks:** Detect active media sessions/notifications; optional mic beat checks; offer tap-to-rhythm mini-games; evolve through cumulative listening minutes and genre diversity.
  - **Steps:**
    * Add now-playing/tempo complications with access to tap-to-rhythm micro-interaction.
    * Integrate media session callbacks and optional beat detection to set animation tempo.
    * Track listening duration and genre diversity, mapping to BeatBunny morale, evolution, and neglect decay.
  - **Acceptance:**
    * Satisfies the **Shared DigiPet Evidence Primer**.
    * Beat-driven animations sync within 500ms of tempo changes and pause gracefully when playback stops.
    * Diversity scoring unlocks evolution while extended silence triggers bored flop and eventual runaway.
    * Animation assets listed in the checklist are committed under `art/export/pets/beatbunny/` with matching `@raw/@drawable` resources and documented in `docs/pets/beatbunny/`.
  - **Artifacts:**
    * Media session/beat detection logs highlighting tempo sync.
    * Animation capture for genre variants and silence slump.
    * Genre diversity scoring doc placed in `docs/pets/beatbunny/`.
    * `docs/pets/beatbunny/animation.md` updated with export settings, optimization output, and asset verification screenshots.

- [ ] **BookWorm — Reading & Knowledge Pet**
  - **Watch face baseline:** Combine time/date with reading streak progress and quick journal entry complication.
  - **Concept:** Feeds on books/articles consumed; metamorphoses as user builds a reading habit.
  - **Appearance cues:** Page-munching caterpillar during sessions; cocooning when neglected; butterfly form after major goals.
  - **Implementation hooks:** Track reading app usage via usage stats/APIs or manual timers; integrate fact-of-the-day prompts; evolve on streaks, completed books, and total minutes read.
  - **Steps:**
    * Implement reading streak complication with quick-journal shortcut for reflections or summaries.
    * Gather reading duration via usage stats/manual timer inputs and validate across multiple sources.
    * Tie milestones (completed books, streak length) to transformation animations and runaway/backslide conditions.
  - **Acceptance:**
    * Satisfies the **Shared DigiPet Evidence Primer**.
    * Reading sessions register within tracked categories and update BookWorm form at milestone boundaries.
    * Fact-of-the-day prompts rotate without repetition during 14-day window and skip when streak is broken.
    * Animation assets listed in the checklist are committed under `art/export/pets/bookworm/` with matching `@raw/@drawable` resources and documented in `docs/pets/bookworm/`.
  - **Artifacts:**
    * Usage stats ingestion logs with reconciliation script notes.
    * Animation captures for caterpillar, cocoon, and butterfly states.
    * Milestone table and prompt rotation plan saved under `docs/pets/bookworm/`.
    * `docs/pets/bookworm/animation.md` updated with export settings, optimization output, and asset verification screenshots.

- [ ] **ShutterBug — Photography & Creativity Pet**
  - **Watch face baseline:** Show time/date with photo-count complication and camera remote shortcut without obscuring dial.
  - **Concept:** Thrives on new photos and creative prompts; fades when no moments are captured.
  - **Appearance cues:** Ladybug flashing camera after shots; colorless carapace when photo drought persists; vibrant shell when gallery grows.
  - **Implementation hooks:** Companion monitors camera roll for new media (READ_MEDIA_IMAGES); surface daily photo challenges; optionally sync thumbnails to phone app; evolve as albums fill and prompt quests completed.
  - **Steps:**
    * Add photo-count complication with quick camera remote access and prompt indicator badges.
    * Implement media monitoring respecting privacy scopes, syncing new shots and creative prompt completions.
    * Map photo cadence to ShutterBug coloration, evolution states, and neglect fade/runaway sequences.
  - **Acceptance:**
    * Satisfies the **Shared DigiPet Evidence Primer**.
    * New photo detection reflects on-watch within one sync cycle and challenge prompts rotate without repeating inside weekly set.
    * Neglect fades progress predictably and runaway occurs after documented drought duration.
    * Animation assets listed in the checklist are committed under `art/export/pets/shutterbug/` with matching `@raw/@drawable` resources and documented in `docs/pets/shutterbug/`.
  - **Artifacts:**
    * Media sync audit log and permission rationale documentation.
    * Animation captures for vibrant, neutral, and faded shells.
    * Prompt rotation schedule recorded in `docs/pets/shutterbug/`.
    * `docs/pets/shutterbug/animation.md` updated with export settings, optimization output, and asset verification screenshots.

- [ ] **TranquiliTurtle — Meditation & Mindfulness Turtle**
  - **Watch face baseline:** Keep time/date legible with mindfulness-minute complication and quick-start breathing control.
  - **Concept:** Rewards guided breathing, meditation, and calm streaks; hides in shell when routines lapse.
  - **Appearance cues:** Floating lotus pose during sessions; shell-withdrawn turtle signaling missed practices; radiant aura at master tier.
  - **Implementation hooks:** Pull mindfulness minutes from Health Services or built-in exercises; include pet-led breathing UI; track streaks and total minutes; gently prompt when stress trends high.
  - **Steps:**
    * Implement mindfulness-minute complication with ambient animation toggle for low-power mode.
    * Sync guided session completions from services or native routines, applying streak logic for calm aura unlocks.
    * Configure stress-detection nudges, shell-withdrawn fallback, and runaway gating on prolonged inactivity.
  - **Acceptance:**
    * Satisfies the **Shared DigiPet Evidence Primer**.
    * Guided session completions update TranquiliTurtle aura before the next scheduled prompt and respect reduced-motion settings.
    * Stress-triggered nudges stay within notification policy and runaway occurs after confirmed inactivity threshold.
    * Animation assets listed in the checklist are committed under `art/export/pets/tranquiliturtle/` with matching `@raw/@drawable` resources and documented in `docs/pets/tranquiliturtle/`.
  - **Artifacts:**
    * Session sync logs and reduced-motion configuration notes.
    * Animation captures for meditation, shell-withdrawn, and radiant aura states.
    * Prompt schedule and inactivity thresholds documented in `docs/pets/tranquiliturtle/`.
    * `docs/pets/tranquiliturtle/animation.md` updated with export settings, optimization output, and asset verification screenshots.

- [ ] **VoltVampire — Tech Use & Battery Pet**
  - **Watch face baseline:** Present time/date with dual battery complications (watch/phone) and charging reminders without obscuring the dial.
  - **Concept:** Feeds on healthy charging habits; weakens when devices regularly hit critical battery or over-discharge.
  - **Appearance cues:** Glowing bat sipping energy during charges; pale, swooning sprite when batteries near zero; regal vampire persona after sustained battery stewardship.
  - **Implementation hooks:** Monitor watch battery locally and sync phone battery via Data Layer; award timely-charge points; trigger low-battery nudges; log deep discharges to influence runaway/death sequences.
  - **Steps:**
    * Build dual battery complications with charge schedule indicators and healthy-habit streak tracker.
    * Implement Data Layer sync plus charging habit analytics that differentiate quick top-offs vs. deep cycles.
    * Tie analytics to VoltVampire mood, evolution, and runaway/hibernation sequences with configurable thresholds.
  - **Acceptance:**
    * Satisfies the **Shared DigiPet Evidence Primer**.
    * Battery sync stays within one-minute freshness between watch and phone, and charging habit analysis classifies sessions accurately.
    * Nudges respect notification caps and runaway/hibernation only triggers after repeated critical battery events.
    * Animation assets listed in the checklist are committed under `art/export/pets/voltvampire/` with matching `@raw/@drawable` resources and documented in `docs/pets/voltvampire/`.
  - **Artifacts:**
    * Battery sync logs and analytics summary stored with threshold configs.
    * Animation capture for charging feast, low-battery swoon, and regal evolution.
    * Notification copy and escalation ladder documented in `docs/pets/voltvampire/`.
    * `docs/pets/voltvampire/animation.md` updated with export settings, optimization output, and asset verification screenshots.

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
   - **Steps:**
     1. Create `docs/setup/tooling.md` (and the parent directory) if it does not already exist before documenting the tooling snapshot.
     2. Capture each required tool in the doc using a consistent bullet list or table that includes: tool name, pinned version, official download or installation link, and the Wear OS 5/6 lineage references that justify the target selections.
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

10. **[ ] Prompt:** _"Stand up the baseline profile module and ensure Macrobenchmark-generated rules ship in release builds."_ _(claimed by @assistant, 2025-11-10 13:40 UTC)_
    - **Purpose:** Improve startup/render perf; reduce CPU.
    - **Steps:**
      1. Scaffold the `baselineprofile/` Macrobenchmark module (Gradle script, manifest, and source folders) so it targets the `:app` module and runs on a debuggable variant.
      2. Register the module in `settings.gradle.kts` with `include(":baselineprofile")` and sync the project.
      3. Apply the Macrobenchmark/Baseline Profile plugins and configurations in `baselineprofile/build.gradle.kts`, including the `androidx.benchmark` dependencies and instrumentation runner pointing at the app under test.
      4. Add starter benchmark classes under `baselineprofile/src/main/java/...` that warm up the watch face configuration screen and primary render path before profile capture.
      5. Generate the baseline profile and copy the resulting `baseline-prof.txt` into `app/src/main/baseline-prof.txt` so it ships with release builds.
    - **Acceptance:**
      * `./gradlew :baselineprofile:generateBaselineProfile` completes successfully and refreshes `app/src/main/baseline-prof.txt`.
      * `./gradlew :app:assembleRelease` finishes without errors, confirming the packaged artifact contains the refreshed profile.
    - **Artifacts:**
      * `art/perf/baselineprofile-generateBaselineProfile.log` and `art/perf/app-assembleRelease.log` capturing the command outputs.
      * Updated `docs/perf/baselineprofile.md` summarizing the module structure, Macrobenchmark coverage, and profile storage path.
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

12. **[ ] Prompt:** _"Wire the PR build and lint workflow in GitHub Actions."_
    - **Purpose:** Automatic checks on PR.
    - **Steps:**
      * Ensure `.github/workflows/android.yml` runs `./gradlew spotlessCheck detekt lint assembleDebug` with cached Gradle and Android SDK setup.
    - **Acceptance:** PRs show passing checks; SARIF uploaded.
    - **Artifacts:** Lint/Detekt reports as build artifacts.
    - **Fail?:** Fix build/lint errors.

13. **[ ] Prompt:** _"Extend CI to execute the unit test suite and surface the results."_
    - **Purpose:** Enforce correctness gates.
    - **Steps:**
      * Extend workflow to run `./gradlew testDebugUnitTest` and publish reports.
    - **Acceptance:** Tests pass; coverage comment on PR.
    - **Artifacts:** Test reports uploaded.
    - **Fail?:** Fix tests or code.

14. **[ ] Prompt:** _"Add the Wear OS emulator job to run connected tests in CI."_
    - **Purpose:** Run instrumentation/screenshot tests headless.
    - **Steps:**
      * Use **ReactiveCircus/android-emulator-runner** with a Wear OS x86_64 system image; start emulator; run `connectedDebugAndroidTest`.
    - **Acceptance:** Emulator boots in CI; `androidTest` suite passes; screenshots uploaded.
    - **Artifacts:** Instrumentation logs, screenshots.
    - **Fail?:** Bump RAM/timeouts; pre-download system images; retry.

15. **[ ] Prompt:** _"Schedule the nightly Macrobenchmark workflow to capture baseline profiles."_
    - **Purpose:** Generate baseline profile in CI nightly.
    - **Steps:**
      * Nightly workflow triggers `:baselineprofile:collect` then commits asset as artifact (not auto-commit).
    - **Acceptance:** Baseline profile artifact attached to nightly run.
    - **Artifacts:** Baseline profile, perf metrics.
    - **Fail?:** Skip battery check on no-battery hosts if needed.

---

### Phase 5 — Signing & Play Integration

16. **[ ] Prompt:** _"Enable Play App Signing in the Google Play Console and capture evidence."_
    - **Purpose:** Use Google-managed signing; CI only needs upload key.
    - **Steps:**
      * In Play Console, enable **App signing by Google Play**; download upload certificate; export a dedicated upload keystore and base64-encode it for CI.
      * Create a Play Console service account with release-upload rights and download the JSON credentials for later use in Task 18.
    - **Acceptance:** Play shows “App signing enabled,” and the service-account JSON + upload keystore are archived for CI secrets.
    - **Artifacts:** App signing status screenshot; redacted note of service-account JSON location.
    - **Fail?:** Complete identity verification; retry.

17. **[ ] Prompt:** _"Configure Gradle release signing and build types to use CI-provided credentials."_
    - **Purpose:** Deterministic release builds in CI.
    - **Steps:**
      * In `app/build.gradle.kts`, configure `signingConfigs { release }` reading env vars; enable R8; set `versionCode` auto from tags.
    - **Acceptance:** `./gradlew :app:bundleRelease` uses release keystore in CI.
    - **Artifacts:** Build scans/logs.
    - **Fail?:** Fix keystore passwords/aliases.

18. **[ ] Prompt:** _"Provision the CI secrets required for builds and publishing."_
    - **Purpose:** Secure keys for build/publish.
    - **Prerequisites:** Phase 5 Task 16 (Play App Signing + service account JSON) and Task 17 (Gradle release signing + upload keystore) are completed.
    - **Steps:**
      1. Confirm Task 16's service-account JSON is stored in the secure vault, then immediately upload it as the `PLAY_SERVICE_ACCOUNT_JSON` GitHub secret.
      2. Right after completing Task 17, base64-encode the upload keystore generated there and add/update the `UPLOAD_KEYSTORE_BASE64`, `UPLOAD_KEY_ALIAS`, `UPLOAD_KEY_PASSWORD`, and `STORE_PASSWORD` secrets.
      3. Update CI workflow references (Tasks 12–15, 19–20) to read these secrets via environment variables only—no plaintext commits.
    - **Acceptance:** GitHub secrets reference the Task 16 service-account JSON and Task 17 upload keystore; workflows consume them without leaking secrets in code or logs.
    - **Artifacts:** Screenshot of the GitHub secrets page (sensitive fields redacted) plus links back to the Task 16/17 evidence in `docs/`.
    - **Fail?:** Rotate keys from Tasks 16/17 and retry.

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

21. **[ ] Prompt:** _"Implement the complication slots and schema within the Watch Face Format layout."_ _(claimed by @agent, 2025-11-10 02:56 UTC)_
    - **Purpose:** Add standard complications (steps, heart rate, battery).
    - **Steps:**
      * **Watch Face Format track:** Define slots and bounds in WFF; for phone battery or advanced data, consider a small provider app.
      * **Kotlin-rendered track:** Configure `ComplicationSlotsManager` inside the renderer host (`WatchFaceService`) and register `ComplicationSlot`s with supported data types; surface data via `Renderer.CanvasRenderer` drawing logic or Compose bridge.
    - **Acceptance:** Either (a) the WFF-based face exposes selectable complications in the system editor and renders live data, or (b) the Kotlin-rendered face registers the same slots through `ComplicationSlotsManager` and displays updates in the custom renderer without regression. Document which path is implemented.
    - **Artifacts:** Screenshots from emulator (WFF) or renderer preview captures showing complications (Kotlin track).
    - **Fail?:** Verify slot IDs and data types.

22. **[ ] Prompt:** _"Design and validate the always-on display and power-saving modes for the watch face."_ _(claimed by @agent, 2025-11-10 02:56 UTC)_
    - **Purpose:** Great battery behavior.
    - **Steps:**
      * **Watch Face Format track:** Provide simplified `ambient` group in WFF; throttle updates; avoid constant phone interactions & heavy animations to pass Play warnings.
      * **Kotlin-rendered track:** Implement ambient callbacks in `Renderer.CanvasRenderer` (or Compose renderer) to switch palettes, disable anti-aliasing, and gate animation timers; ensure `setAmbientMode`/`onPropertiesChanged` paths cover low-bit and burn-in protection cases.
    - **Acceptance:** Either (a) Macrobenchmark confirms stable frame times and no Play warnings for the WFF ambient group, or (b) profiling of the Kotlin renderer shows compliant ambient transitions (documented renders plus profiler output) with the same lack of Play warnings. Make the chosen validation explicit.
    - **Artifacts:** Perf numbers; Play review notes; Kotlin track may substitute renderer logs + ambient screenshots if applicable.
    - **Fail?:** Reduce animation frequency/bitmap size.

23. **[ ] Prompt:** _"Automate generation of multi-density previews and Play Store screenshots."_ _(claimed by @agent, 2025-11-10 02:56 UTC)_
    - **Purpose:** Auto-produce Play assets.
    - **Steps:**
      * **Watch Face Format track:** Instrumentation test renders preset styles (light/dark/AOD) at multiple densities, saves PNGs; CI uploads to `src/main/play/listings/en-US/graphics/phone-screenshots/`.
      * **Kotlin-rendered track:** Add Compose or view-based screenshot tooling (e.g., Paparazzi or AndroidX Screenshot tests) that exercise the Kotlin renderer/`Renderer.CanvasRenderer` surfaces across densities and styles, exporting assets to the same Play listings path.
    - **Acceptance:** Either pipeline generates Play-compliant screenshots and `publishListing` succeeds; note whether instrumentation or Paparazzi/Screenshot tests produced the assets.
    - **Artifacts:** PNGs as CI artifacts and committed copies, plus test logs for the corresponding tooling.
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

26. **[ ] Prompt:** _"Draft the privacy policy, complete the Data Safety questionnaire, and archive the evidence."_
    - **Purpose:** Lock down Play compliance artifacts before advancing.
    - **Steps:**
      * Draft/update the privacy policy and data handling disclosures with PM/legal input.
      * Complete the Google Play Data Safety questionnaire; export the review summary.
      * Store the policy, questionnaire exports, and supporting notes under `docs/security/` (or the designated security docs folder).
      * Capture screenshots or PDFs showing the questionnaire submission before proceeding to pre-release gates.
    - **Acceptance:** Privacy policy and Data Safety entries approved in Play Console; repository contains the synced documentation.
    - **Artifacts:** `docs/security/` assets; Play Console submission evidence.
    - **Fail?:** Resolve policy gaps or questionnaire blockers with PM/legal and resubmit.

27. **[ ] Prompt:** _"Verify the Wear OS target API policy compliance and document the evidence."_
    - **Purpose:** Compliance gate.
    - **Steps:**
      * Confirm `targetSdkVersion=34` for Wear OS app submission.
    - **Acceptance:** Play pre-launch report shows correct target API.
    - **Artifacts:** Pre-launch report PDF.
    - **Fail?:** Update `targetSdk` and resubmit.

---

### Phase 8 — Pre-Release Gates

28. **[ ] Prompt:** _"Run a Google Play pre-launch report smoke test and address findings."_
    - **Purpose:** Automated device lab sanity.
    - **Steps:**
      * Upload to **Internal**; trigger PLR; review crashes, ANRs, permissions.
    - **Acceptance:** Zero critical issues; medium issues triaged.
    - **Artifacts:** PLR HTML/PDF.
    - **Fail?:** Fix and re-upload.

29. **[ ] Prompt:** _"Complete the accessibility review for the watch face and document adjustments."_
    - **Purpose:** Legibility on small displays.
    - **Steps:**
      * Check contrast ratios and tap targets per Wear guidance.
    - **Acceptance:** `docs/ux/accessibility.md` updated; issues resolved.
    - **Artifacts:** Before/after screenshots.
    - **Fail?:** Adjust palettes/typography.

30. **[ ] Prompt:** _"Promote the build to the closed testing track and collect feedback."_
    - **Purpose:** External validation.
    - **Steps:**
      * Promote internal → closed; collect feedback for 7–14 days.
    - **Acceptance:** No crash spikes; battery feedback positive.
    - **Artifacts:** Crash-free sessions %, user notes.
    - **Fail?:** Patch, re-test.

---

### Phase 9 — Release & Post-Release

31. **[ ] Prompt:** _"Execute a staged production rollout via Gradle Play Publisher."_
    - **Purpose:** Safe release.
    - **Steps:**
      * Use GPP: `./gradlew publishRelease -Ptrack=production -ProlloutFraction=0.1`.
    - **Acceptance:** 10% staged rollout created in Play; monitoring enabled.
    - **Artifacts:** Rollout screenshot; CI logs.
    - **Fail?:** Halt rollout; hotfix via internal → production.

32. **[ ] Prompt:** _"Establish monitoring dashboards and alerting for post-release health."_
    - **Purpose:** Catch regressions.
    - **Steps:**
      * Enable ANR/Crash alerts; track Play vitals; set alerting in observability tool.
    - **Acceptance:** Alert rules confirmed; dashboard link recorded.
    - **Artifacts:** Dashboard URL in `docs/devops/monitoring.md`.
    - **Fail?:** Adjust filters and thresholds.

33. **[ ] Prompt:** _"Cut the production tag, publish the changelog, and write the release retrospective."_
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
