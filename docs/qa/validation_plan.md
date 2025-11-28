# Validation Plan (v1.0 draft)

Document the test approach and device coverage for the watch face. Mark checkboxes when approved.

- [ ] Device matrix defined (models, firmware, brightness profiles)
- [ ] Manual test scenarios documented (core flows, edge cases)
- [ ] Acceptance criteria agreed (per ritual/feature)
- [ ] Performance/battery checks planned
- [ ] Accessibility checks planned (contrast, reduced motion)

## Device matrix (link to `docs/qa/device_matrix.md` when populated)
- Target devices: Galaxy Watch8 Classic (primary), Wear OS 5 emulator (API 34), Wear OS 6 emulator (API 35) when available.
- Firmware/OS: Wear OS 5 (API 34) baseline; note firmware build numbers in matrix.
- Notes: capture ambient screenshots across brightness profiles; include burn-in protection checks.

## Manual scenarios
- Time/date render: verify format, legibility, alignment in interactive and ambient.
- Complications: set left/right to steps and heart-rate; confirm data updates within provider SLA; optional top battery slot.
- Configuration: switch palettes (Cosmic Blue/Starlight); ensure persistence across reboots.
- Ambient/AOD: verify monochrome palette, static pet silhouette, no particles; check low-bit/burn-in behavior.
- Notifications/prompts: none in v1.0; ensure no unsolicited prompts.
- Pet state transitions: idle ↔ happy ↔ hunger/neglect timers; ensure reduced-motion follows system.

## Acceptance criteria
- No crashes during watch face selection or configuration.
- Time/date and complications remain legible at arm’s length; contrast ≥ 4.5:1 for text.
- Complication updates respect provider cadence; no runaway updates in ambient.
- Ambient frame time meets ≤33 ms; interactive ≤16 ms (profiling spot checks).
- Reduced-motion disables particles and limits pet animation to slow breathing.

## Performance/battery
- Run linted WFF + Macrobenchmark smoke; confirm no Play warnings for frequent updates.
- Verify no continuous animations in ambient; confirm update throttling.

## Accessibility
- Contrast verified for both palettes; pet does not occlude time/complications.
- Reduced-motion honored; haptics not used in v1.0.

## Test data and tools
- Emulators/AVDs: Wear OS 34 x86_64; Wear OS 35 when available.
- Physical devices: Galaxy Watch8 Classic (target); note any others in matrix.
- Logs/reports: Paparazzi screenshots, lint/detekt, unit tests, Macrobenchmark summaries.
