# Architecture - Kotlin renderer track

## Rendering approach
- Chosen path: Jetpack Watch Face Kotlin Canvas renderer (`CompanionWatchFaceRenderer`) hosted by `CosmoBondWatchFaceService`.
- Rationale: Renderer gives full control over complication slots, palette, and ambient behavior while keeping tests deterministic (Paparazzi + Robolectric). The prior Watch Face Format (WFF) XML is retained only as a design reference/preview scaffold and is not used at runtime.
- References: Jetpack Watch Face docs - https://developer.android.com/training/wearables/watchface; WFF overview (design reference only) - https://developer.android.com/training/wearables/watch-faces/watch-face-format; Samsung WFF overview - https://developer.samsung.com/watch-face/face-format/overview.html.

## Platform levels
- `minSdk` / `targetSdk`: 34 (set in `app/build.gradle.kts`), matching the Play target requirement for Wear OS (API 34, Android 14 base).
- Compile SDK: 34 (`app/build.gradle.kts`).
- JDK/AGP/Kotlin: JDK 17, AGP 8.5.2, Kotlin 1.9.24 (see root `build.gradle.kts`).

## Runtime wiring
- Manifest advertises `CosmoBondWatchFaceService` as a Jetpack watch face and points preview metadata to `@drawable/watchface_preview`; no WFF metadata is present.
- Complication slots and user style schema are defined in code (`CompanionComplicationSlots`, `CompanionUserStyle`), matching Paparazzi/Robolectric tests.
- WFF raw (`res/raw/watchface.xml`) is for design parity only and will be kept in sync visually with the Kotlin renderer when updated.

## Complication refresh SLAs (target caps)
- Steps: rely on system provider cadence (event-driven; no manual invalidations); target ≤1 update/min in interactive, no ambient refreshes beyond provider defaults.
- Heart rate (when added): use Health Services foreground-only sampling with user consent; target ≤1 update/min in interactive; reuse last known in ambient; no continuous background polling.
- Watch battery: rely on system provider; accept change-driven updates (state change or ≤5% delta) with no extra invalidations; avoid ambient refreshes.
- Phone battery (Data Layer, when added): send only on state change or ≤5% delta, debounced to ≤5 min interactive and ≤15 min ambient; batch with other payloads to minimize radio wakeups.
