# Manual Animation Export Pipeline

The repository currently ships only the `:app` module. Until the `:art` automation module lands, use the commands below to prepare animation assets that satisfy the DigiPet checklist directory layout.

## Prerequisites
- [FFmpeg](https://ffmpeg.org/) 5.x or newer
- [`cwebp`](https://developers.google.com/speed/webp/docs/cwebp) from the WebP toolkit (or `ffmpeg` with WebP encoder support)
- Source timeline exports under `art/source/pets/<pet>/`

## 1. Render frame sequences
Export each animation state into a high-quality video file (e.g., ProRes or lossless H.264) or render directly into PNG frames. The commands below assume you exported `<state>.mp4` files per animation state.

```bash
# Example: convert a 60 fps MP4 timeline into numbered PNG frames
ffmpeg -i art/source/pets/<pet>/<state>.mp4 \
       -vf "fps=60,scale=512:512:flags=lanczos" \
       art/export/pets/<pet>/<state>/%04d.png
```

Ensure every state name matches the checklist (e.g., `ambient_idle`, `runaway_departure`). The export directory must contain four-digit frame numbers starting at `0001.png`.

## 2. Generate optimized deliverables
Create ambient-mode WebP/WebM loops and any preview sprites the watch face will load while Watch Face Format assets initialize.

### Animated WebP (preview sprites)
```bash
# Lossy WebP tuned for preview drawables
cwebp -q 85 -loop 0 -m 6 -af \
      art/export/pets/<pet>/<state>/*.png \
      -o app/src/main/res/drawable/<pet>_<state>_preview.webp
```

If your FFmpeg build includes the WebP encoder, you can use:

```bash
ffmpeg -framerate 60 -pattern_type glob \
       -i "art/export/pets/<pet>/<state>/*.png" \
       -vf "scale=256:256:flags=lanczos" \
       -lossless 0 -compression_level 6 -loop 0 \
       app/src/main/res/drawable/<pet>_<state>_preview.webp
```

### WebM (ambient watch-face payloads)
```bash
ffmpeg -framerate 60 -pattern_type glob \
       -i "art/export/pets/<pet>/<state>/*.png" \
       -c:v libvpx-vp9 -pix_fmt yuva420p -b:v 0 -crf 28 \
       art/export/pets/<pet>/<state>.webm
```

Store the generated WebM files alongside the frame directories for archival and future conversion into Watch Face Format payloads.

## 3. Record evidence
1. Capture the command output into `art/export/pets/<pet>/export.log` (append to the same log for each state).
2. Stage the frame directories, WebP previews, WebM loops, and export log before opening a pull request.

## 4. Wire up assets
Follow the guidance in `AGENTS.md` to reference the new assets from Watch Face Format XML and Canvas/Kotlin fallbacks. When the `:art` Gradle module arrives, migrate to its verified tasks so the checklist stays authoritative.
