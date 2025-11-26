# Architecture — Watch Face Format v2 track

## Rendering approach
- Chosen path: Watch Face Format (WFF) v2 with a lightweight Kotlin host service (`CosmoBondWatchFaceService`) for configuration hooks and future complication wiring.
- Rationale: Declarative WFF keeps rendering consistent across devices, simplifies preview generation, and aligns with Google/Samsung guidance for new Wear OS faces.
- References: Google WFF overview — https://developer.android.com/training/wearables/watch-faces/watch-face-format; Samsung WFF overview — https://developer.samsung.com/watch-face/face-format/overview.html.

## Platform levels
- `minSdk` / `targetSdk`: 34 (set in `app/build.gradle.kts`), matching the Play target requirement for Wear OS (API 34, Android 14 base) and WFF v2 expectations.
- Compile SDK: 34 (`app/build.gradle.kts`).
- JDK/AGP/Kotlin: JDK 17, AGP 8.5.2, Kotlin 1.9.24 (see root `build.gradle.kts`).

## Implications for downstream tasks
- Continue on the WFF track (Tasks 5, 9, 21–23) for layout, complications, ambient, and screenshots; no Kotlin-rendered canvas renderer needed unless we pivot later.
- Kotlin host remains minimal but must expose configuration and complications via WFF metadata (`AndroidManifest.xml` points to `@raw/watchface`).
- Ensure previews/goldens and CI checks use the WFF resource; baseline profiles and performance work (Task 10+) should warm the WFF host paths.
