# Bookworm Animation Exports

The Bookworm DigiPet now ships hand-drawn, colored-pencil style SVG loops that can be previewed directly in a browser—no binary assets required. Each state includes four key-frame SVGs plus a richly animated SVG render that mirrors Veo/Sora-quality motion while staying repository-friendly.

## Available States

### `ambient_idle`
- **Form:** Caterpillar relaxing on a leaf with an open book.
- **Files:**
  - `art/export/pets/bookworm/ambient_idle/frames/0001.svg`–`0004.svg`
  - `art/export/pets/bookworm/ambient_idle/renders/ambient_idle.svg`
- **Highlights:**
  - Layered pencil texture derived from `feTurbulence` blending for tactile shading.
  - Gentle body-segment sway (±4px) and head tilt (-6° ↔ 3°) for a lifelike breathing loop.
  - Subtle page shimmer keeps the book readable without overpowering complications.
- **Preview:** Open the render SVG in any modern browser to watch the seamless loop.

### `reading_glow`
- **Form:** Butterfly evolution hovering triumphantly above a glowing storybook.
- **Files:**
  - `art/export/pets/bookworm/reading_glow/frames/0001.svg`–`0004.svg`
  - `art/export/pets/bookworm/reading_glow/renders/reading_glow.svg`
- **Highlights:**
  - Veo/Sora-inspired wing rotations (-6° ↔ 5°) with luminous gradients echoing hand-colored pencils.
  - Hover bob and antenna arcs synced to the book’s magical glow pulse.
  - Dedicated `bookGlow` filter delivers a soft fantasy aura without introducing raster assets.
- **Preview:** The animated SVG showcases the full celebration beat in-loop.

## Integration Notes
- Keep state directories synchronized with Watch Face Format assets (`@raw/bookworm_ambient_idle`, `@raw/bookworm_reading_glow`) during app integration.
- When additional states are generated, mirror this directory layout (`frames/` + `renders/` + metadata) and append prompt details to `art/export/pets/bookworm/prompts.md`.
- For QA captures, export frame grabs from the SVG renders or animate them via the existing render script once PNG export is permitted.

## Provenance & Logging
- Prompt history, palette selections, and manual edits live in `art/export/pets/bookworm/prompts.md`.
- Execution summary and viewing tips are archived in `art/export/pets/bookworm/export.log`.
