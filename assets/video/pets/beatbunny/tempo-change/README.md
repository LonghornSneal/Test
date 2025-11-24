# BeatBunny Tempo Change Asset

Prepared for the Test (Galaxy Watch8 Classic) app.

## Files
- `beatbunny_tempo-change_1080p24.mp4` — H.264, 1920x1080, 24 fps, 8.00s, no audio.
- `beatbunny_tempo-change_1080p24.webm` — VP9, 1920x1080, 24 fps, 8.00s, no audio.
- `beatbunny_tempo-change_1080p24_loop.mp4` — Loop-smoothed H.264 (end crossfades into start over 0.25s for seamless looping).
- `beatbunny_tempo-change_1080p24_loop.webm` — Loop-smoothed VP9 version.
- `beatbunny_tempo-change_640w24.gif` — Original GIF preview (scaled to 640px wide, 24 fps, ~16 MB).
- `beatbunny_tempo-change_640w24_loop.gif` — Loop-smoothed GIF preview.

## Usage
- Looping playback (video-only). Example HTML preview/snippet:
  ```html
  <video autoplay loop muted playsinline width="640">
    <source src="beatbunny_tempo-change_1080p24_loop.webm" type="video/webm" />
    <source src="beatbunny_tempo-change_1080p24_loop.mp4" type="video/mp4" />
  </video>
  ```
- If you need transparency later, export from source with alpha and re-encode to WebM VP9 using `-pix_fmt yuva420p`.

## Notes
- Original source kept at repo root: `BeatBunny-TempoChange.mp4` (unchanged).
- Current exports remove the padded 1088px height (now 1080px) and strip audio.
