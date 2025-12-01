# Battery & Performance Checklist (Task 11)

- Audit date: 2025-12-01
- Auditor: @agent
- Reference: Google Optimize watch faces guidance (developer.android.com/training/wearables/watchfaces/performance).

## Findings
- Interactive renderer uses `INTERACTIVE_FRAME_MS = 16` in `CompanionWatchFaceRenderer` (app/src/main/java/com/cosmobond/watchface/CompanionWatchFaceRenderer.kt#L36), yielding ~60 fps even though the current face is static. Optimize to a 1s tick (or per-animation cadence) to avoid unnecessary wake-ups.
- Ambient path sets a black background and gray accent (`CompanionPalette`) with `shouldAnimate()` returning false, so timers stop in ambient. Anti-aliasing remains on and complications still render, which can be pared back for low-bit/burn-in cases.
- Watch Face Format ambient scene omits complications, but the Kotlin renderer still draws them; aligning the ambient pass with the WFF intent would save work and ensure monochrome output.
- Complication updates rely on system providers (day-of-week, step count) with no manual invalidations, so update frequency follows provider SLAs. No Data Layer or phone interactions are present yet.
- Macrobenchmark/baseline profile flow already exists (Task 10); re-run after update-rate changes to confirm frame-time stability.

## Checklist
- [x] Interactive update cadence reviewed — current 16 ms; needs throttle to ≥1000 ms when animations are static, with per-animation caps (≤30 fps) when added.
- [x] Ambient/AOD behavior reviewed — palette dims to monochrome/black and animations halt; add low-bit/burn-in handling and skip complication drawing in ambient to match WFF scene.
- [x] Complication update policy reviewed — stays on provider cadence; keep ambient complications minimal and avoid manual `invalidate` calls unless data changes.
- [x] Phone/companion sync reviewed — none today; future Data Layer sync should be visibility-aware and batched.
- [x] Baseline profile alignment reviewed — regenerate profile after cadence tweaks to keep startup/render metrics current.

## Action Items
- [ ] Raise `INTERACTIVE_FRAME_MS` to a ≥1000 ms tick (or animation-driven rate) and document the rationale inline.
- [ ] Add ambient guards to skip complication rendering and disable anti-aliasing for low-bit/burn-in protection.
- [ ] Mirror ambient simplifications in both the Kotlin renderer and WFF scene so outputs stay consistent.
- [ ] Document target complication refresh SLAs (steps/heart-rate/battery) in `docs/tech/architecture.md` when those sources are added.

Checklist signed: @agent, 2025-12-01 01:12 UTC
