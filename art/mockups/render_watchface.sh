#!/usr/bin/env bash
set -euo pipefail

usage() {
    cat <<'USAGE'
Usage: ./render_watchface.sh --pet <pet> --state <state> --input <png_dir> [--output-root <dir>] [--fps <fps>]

Copies a rendered PNG sequence into the repository export tree and produces WebM, animated WebP,
 and GIF previews for integration testing. Requires ffmpeg on PATH.

Arguments:
  --pet          Lowercase identifier for the DigiPet (e.g. cardiocritter).
  --state        Animation state name (e.g. idle, happy).
  --input        Directory containing PNG frames named as a zero-padded sequence (0001.png ...).
  --output-root  Destination root directory (defaults to art/export/pets).
  --fps          Frame rate of the sequence (defaults to 60).
USAGE
}

pet=""
state=""
input_dir=""
output_root="art/export/pets"
fps=60

while [[ $# -gt 0 ]]; do
    case "$1" in
        --pet)
            pet="$2"
            shift 2
            ;;
        --state)
            state="$2"
            shift 2
            ;;
        --input)
            input_dir="$2"
            shift 2
            ;;
        --output-root)
            output_root="$2"
            shift 2
            ;;
        --fps)
            fps="$2"
            shift 2
            ;;
        -h|--help)
            usage
            exit 0
            ;;
        *)
            echo "Unknown argument: $1" >&2
            usage
            exit 1
            ;;
    esac
done

if [[ -z "$pet" || -z "$state" || -z "$input_dir" ]]; then
    echo "Missing required arguments." >&2
    usage
    exit 1
fi

if ! command -v ffmpeg >/dev/null 2>&1; then
    echo "ffmpeg is required but was not found on PATH." >&2
    exit 1
fi

if [[ ! -d "$input_dir" ]]; then
    echo "Input directory '$input_dir' does not exist." >&2
    exit 1
fi

sequence_glob=$(find "$input_dir" -maxdepth 1 -name '*.png' | sort | head -n1)
if [[ -z "$sequence_glob" ]]; then
    echo "Input directory '$input_dir' does not contain PNG frames." >&2
    exit 1
fi

export_root="$output_root/$pet"
state_dir="$export_root/$state"
frames_dir="$state_dir/frames"
renders_dir="$state_dir/renders"
logs_dir="$export_root/logs"
mkdir -p "$frames_dir" "$renders_dir" "$logs_dir"

run_id="$(date -u +"%Y%m%dT%H%M%SZ")"
log_file="$logs_dir/${run_id}_${state}.log"

{
    echo "pet=$pet"
    echo "state=$state"
    echo "fps=$fps"
    echo "input_dir=$input_dir"
    echo "export_root=$export_root"
    echo "run_id=$run_id"
    echo "ffmpeg_version=$(ffmpeg -version | head -n1)"
} > "$log_file"

# Copy frames to the export directory for traceability.
if command -v rsync >/dev/null 2>&1; then
    rsync -a --delete "$input_dir"/ "$frames_dir"/
else
    rm -f "$frames_dir"/*.png
    cp "$input_dir"/*.png "$frames_dir"/
fi

webm_file="$renders_dir/${state}_${fps}fps.webm"
webp_file="$renders_dir/${state}_${fps}fps.webp"
gif_file="$renders_dir/${state}_${fps}fps.gif"

# Render WebM (VP9 with alpha support)
ffmpeg -y -r "$fps" -i "$frames_dir/%04d.png" -c:v libvpx-vp9 -pix_fmt yuva420p -b:v 0 -lossless 1 "$webm_file" >> "$log_file" 2>&1

# Render animated WebP
ffmpeg -y -r "$fps" -i "$frames_dir/%04d.png" -vf "scale=iw:ih" -c:v libwebp_anim -lossless 1 -compression_level 6 -qscale 75 "$webp_file" >> "$log_file" 2>&1

# Render GIF preview for quick reviews
ffmpeg -y -r "$fps" -i "$frames_dir/%04d.png" -vf "fps=15,scale=320:-1:flags=lanczos" "$gif_file" >> "$log_file" 2>&1

cat <<METADATA > "$state_dir/metadata.json"
{
  "pet": "$pet",
  "state": "$state",
  "fps": $fps,
  "run_id": "$run_id",
  "frames": "frames/%04d.png",
  "renders": {
    "webm": "renders/$(basename "$webm_file")",
    "webp": "renders/$(basename "$webp_file")",
    "gif": "renders/$(basename "$gif_file")"
  },
  "log": "../logs/$(basename "$log_file")"
}
METADATA

echo "Export complete. Assets stored in $state_dir"
echo "Log captured at $log_file"
