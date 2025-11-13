# CosmoBond BookWorm — Ambient Idle Loop

This document captures the asset generation steps and export settings for the CosmoBond BookWorm ambient idle loop prepared for the Galaxy Watch8 Classic.

## Canvas & Timing
- **Canvas:** 438 × 438 px (transparent RGBA)
- **Frames:** 96
- **Frame rate:** 24 fps (4-second seamless loop)
- **HUD allocation:** Reserved a 200 × 120 px band centered along the top edge for the HUD overlay; left the upper-left and lower-right corners free for complications.

## Layering Workflow
1. **Planning rough:** Generated a six-frame pencil outline sequence (exported as data URIs in `bookworm_idle_assets.js`) on a dedicated layer with mirrored guides to establish pose, book placement, and bookmark curl.
2. **Volume blocking:** Used the guides to block-in the leaf curl, bookmark leaf, and caterpillar segments before detailing.
3. **Refinement:** Smoothed every contour using sub-pixel arcs and selective color anti-aliasing to maintain crisp 1 px linework.
4. **Onion skinning:** Animated the breathing, antenna sway, and page flutter manually frame-by-frame without auto-tweening.
5. **Dithering & shading:** Applied multi-pass manual dithering on the caterpillar segments and book trim while preserving the 64-color palette.
6. **Hue offsets:** Shifted the leaf highlight hue slightly per frame to create a subtle shimmer cycle.
7. **Lighting:** Added layer-based light masks for the ambient glow and dusk vignette, keeping the background transparent.
8. **Cleanup:** Performed pixel-level sweeps on each frame to remove stray pixels and confirm clean transparency.

## Motion Beats
- **Breathing:** Torso easing cycle keyed with sin-based offsets at 24 fps.
- **Antenna sway:** Alternating arc sway with tip highlights.
- **Page flutter:** Book page edge oscillates in sync with breathing cadence.
- **Leaf shimmer:** Highlight ramps rotate every 16 frames for a six-step shimmer.

## Palette
Documented palette lives in `art/export/pets/bookworm/palette.txt` and is limited to 33 curated RGBA swatches (≤ 64 requirement) with 26 currently utilized across the loop.

## Export
- **Module:** `art/export/pets/bookworm/ambient_idle_loop/bookworm_idle_assets.js` contains ES module exports with base64-encoded PNG frames for both the 96-frame production loop and the six planning roughs.
- **Preview:** `art/export/pets/bookworm/ambient_idle_loop/bookworm_idle_preview.html` imports the module for in-browser review without requiring binary assets.
- **Alpha integrity:** Verified that all pixels in the HUD band and reserved corners remain fully transparent in every frame before encoding.

## Integration Notes
- Consume the ES module directly or decode the exported data URIs at build time to materialize sprite sheets within the watch-face toolchain.
- Maintain 24 fps playback to preserve breathing cadence.
- Vignette and glow are subtle overlays—ensure additive blending if composited with other layers.
- Use the HTML preview as a quick smoke test when reviewing palette or motion tweaks without restoring binary exports.
