# Asset Requirements (v1.0)

Status: draft inventory; mark checkboxes when finalized and files are present.

- [ ] Vector/raster asset list complete (names, roles, densities)
- [ ] Font licenses collected and stored (paths/URLs)
- [ ] Ambient-mode variants prepared (reduced color/animation)
- [ ] Color profiles and compression settings documented
- [ ] Export settings verified (per tool used)
- [ ] Ownership and storage confirmed (where files live, who maintains)

## Inventory

| Asset | Purpose | Source path | Export path | Notes |
| --- | --- | --- | --- | --- |
| watchface_dial_base | Background plate with markers | art/source/watchface/watchface_dial_base.ai | art/export/watchface/watchface_dial_base.png | 512x512 nodpi; RGB; no text baked |
| watchface_pet_idle | Pet idle pose | art/source/watchface/pet_idle.psd | art/export/watchface/pet_idle.png | Transparent BG; optimize via pngquant |
| watchface_pet_happy | Pet happy animation frames (sprite) | art/source/watchface/pet_happy.psd | art/export/watchface/pet_happy_{01-08}.png | 8 frames; 12 fps cap |
| watchface_pet_neglect | Pet neglect silhouette | art/source/watchface/pet_neglect.psd | art/export/watchface/pet_neglect.png | Ambient-safe monochrome |
| complication_left_icon | Steps/health icon | art/source/watchface/complication_left.svg | art/export/watchface/complication_left.png | 64x64 nodpi |
| complication_right_icon | Heart-rate/notifications icon | art/source/watchface/complication_right.svg | art/export/watchface/complication_right.png | 64x64 nodpi |
| complication_battery_icon | Optional battery icon | art/source/watchface/complication_battery.svg | art/export/watchface/complication_battery.png | 64x64 nodpi |
| preview_interactive | Store preview | art/source/watchface/preview_interactive.sketch | app/src/main/res/drawable-nodpi/watchface_preview.png | 960x960 |
| font_primary | Google Sans Text / SamsungOne UI license | N/A (download) | app/src/main/res/font/ | License text stored in docs/license/fonts/ |

## Ambient/AOD
- Palette: convert to monochrome slate on near-black; avoid gradients and glows.
- Animation: disable particles; use single-frame idle silhouette; max 1 frame per second updates (if any).
- Testing: verify on Wear OS 34 emulator brightness levels and burn-in protection.

## Export settings
- PNG: 32-bit RGBA (full color + alpha, no 8-bit reduction); target <150 KB per frame; sRGB profile.

## Source control for assets
- Store layered source files (PSD/AI) under `art/source/` for versioning and reproducible exports.
- Store exported runtime assets under `art/export/` referenced by the watch face.
- SVG: clean with SVGO defaults; remove editor metadata.
- Fonts: include license files; subset if needed to reduce size.

## Ownership
- Asset owner: ____________________
- Export/QA owner: ____________________
