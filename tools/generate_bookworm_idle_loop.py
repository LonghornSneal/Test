import base64
import math
from io import BytesIO
from pathlib import Path
from typing import Tuple

from PIL import Image, ImageDraw

WIDTH = HEIGHT = 438
FRAMES = 96
ROUGH_FRAMES = 6
FPS = 24

OUTPUT_DIR = Path('art/export/pets/bookworm')
ASSET_DIR = OUTPUT_DIR / 'ambient_idle_loop'
ASSET_JS_PATH = ASSET_DIR / 'bookworm_idle_assets.js'
PREVIEW_HTML_PATH = ASSET_DIR / 'bookworm_idle_preview.html'
ROUGH_EXPORT_NAME = 'bookwormIdlePlanningFrames'
FINAL_EXPORT_NAME = 'bookwormIdleFrames'
META_EXPORT_NAME = 'bookwormIdleMeta'

PALETTE = {
    'outline': (40, 24, 36, 255),
    'shadow': (52, 30, 42, 255),
    'leaf_dark': (38, 68, 40, 255),
    'leaf_mid': (64, 112, 60, 255),
    'leaf_high': (124, 172, 108, 255),
    'leaf_shimmer_a': (148, 196, 128, 255),
    'leaf_shimmer_b': (172, 212, 152, 255),
    'leaf_shimmer_c': (204, 232, 184, 255),
    'book_cover': (148, 68, 60, 255),
    'book_side': (120, 56, 52, 255),
    'book_page': (224, 206, 180, 255),
    'book_page_shadow': (196, 174, 148, 255),
    'book_trim': (96, 44, 44, 255),
    'caterpillar_dark': (60, 48, 40, 255),
    'caterpillar_mid': (124, 92, 60, 255),
    'caterpillar_light': (200, 152, 100, 255),
    'caterpillar_high': (244, 196, 140, 255),
    'eye_white': (238, 230, 210, 255),
    'eye_pupil': (48, 28, 32, 255),
    'antenna_dark': (52, 34, 40, 255),
    'antenna_light': (176, 136, 120, 255),
    'bookmark_dark': (92, 48, 32, 255),
    'bookmark_mid': (128, 64, 40, 255),
    'bookmark_light': (164, 92, 56, 255),
    'vignette': (22, 12, 24, 90),
    'highlight_soft': (244, 222, 176, 200),
    'page_line': (160, 128, 104, 255),
    'leaf_line': (44, 72, 48, 255),
    'book_line': (84, 40, 40, 255),
    'leaf_shade_dither': (88, 136, 92, 255),
    'book_dither': (172, 96, 84, 255),
    'ambient_glow': (248, 232, 200, 90),
}

LEAF_SHIMMER = [
    PALETTE['leaf_high'],
    PALETTE['leaf_shimmer_a'],
    PALETTE['leaf_shimmer_b'],
    PALETTE['leaf_shimmer_c'],
    PALETTE['leaf_shimmer_b'],
    PALETTE['leaf_shimmer_a'],
]

DITHER_PATTERN = [
    (0, 0), (2, 1), (1, 2), (3, 3)
]

BOOK_DITHER_PATTERN = [
    (0, 1), (2, 0), (1, 3), (3, 2)
]

ALLOWED_COLORS = list(PALETTE.values()) + [(0, 0, 0, 0)]
ALLOWED_COLOR_SET = set(ALLOWED_COLORS)
NEAREST_COLOR_CACHE: dict[Tuple[int, int, int, int], Tuple[int, int, int, int]] = {}


def ensure_dirs():
    ASSET_DIR.mkdir(parents=True, exist_ok=True)


def draw_pixel(draw: ImageDraw.ImageDraw, x: int, y: int, color: Tuple[int, int, int, int]):
    draw.point((x, y), fill=color)


def apply_vignette(image: Image.Image):
    draw = ImageDraw.Draw(image, 'RGBA')
    reserved_top = 120
    corner_clear = 96
    for i in range(0, 22):
        alpha_scale = (i + 1) / 22
        color = (
            PALETTE['vignette'][0],
            PALETTE['vignette'][1],
            PALETTE['vignette'][2],
            int(PALETTE['vignette'][3] * alpha_scale),
        )
        margin = i * 3
        left_x = margin
        right_x = WIDTH - 1 - margin
        top_y = max(reserved_top, margin)
        bottom_y = HEIGHT - 1 - margin

        if left_x >= right_x or top_y >= bottom_y:
            continue

        # vertical edges (left/right)
        for y in range(top_y, bottom_y + 1):
            if left_x < corner_clear and y < corner_clear:
                continue
            if right_x >= WIDTH - corner_clear and y >= HEIGHT - corner_clear:
                continue
            draw.point((left_x, y), fill=color)
            draw.point((right_x, y), fill=color)

        # bottom edge (skip lower-right corner)
        for x in range(left_x + 1, right_x):
            if x >= WIDTH - corner_clear and bottom_y >= HEIGHT - corner_clear:
                continue
            draw.point((x, bottom_y), fill=color)

        # top edge (respect reserved HUD band)
        if top_y < HEIGHT:
            for x in range(left_x + 1, right_x):
                # allow gentle vignette only outside the HUD band
                if (WIDTH - 200) // 2 <= x < (WIDTH + 200) // 2 and top_y <= reserved_top:
                    continue
                if x < corner_clear and top_y < corner_clear:
                    continue
                draw.point((x, top_y), fill=color)


def draw_leaf(draw: ImageDraw.ImageDraw, frame_index: int, sway: float):
    base_y = 260
    length = 220
    curl = 80
    shimmer_index = (frame_index // 16) % len(LEAF_SHIMMER)
    highlight = LEAF_SHIMMER[shimmer_index]

    # leaf body polygon
    points = []
    for i in range(length):
        t = i / length
        width = 90 * (1 - (t - 0.4) ** 2)
        x = WIDTH // 2 + int((t - 0.5) * 220 + sway)
        y = base_y + int(t * 120 - (t ** 1.5) * curl)
        points.append((x - width // 2, y))
    for i in reversed(range(length)):
        t = i / length
        width = 90 * (1 - (t - 0.4) ** 2)
        x = WIDTH // 2 + int((t - 0.5) * 220 + sway)
        y = base_y + int(t * 120 - (t ** 1.5) * curl)
        points.append((x + width // 2, y))
    draw.polygon(points, fill=PALETTE['leaf_mid'])

    # shading band
    for i, (x, y) in enumerate(points[::6]):
        if i % 2 == 0:
            draw_pixel(draw, x, y, PALETTE['leaf_dark'])

    # midrib line
    for i in range(length):
        t = i / length
        x = WIDTH // 2 + int((t - 0.5) * 220 + sway)
        y = base_y + int(t * 120 - (t ** 1.5) * curl)
        if 120 <= y < HEIGHT - 10:
            draw_pixel(draw, x, y, PALETTE['leaf_line'])

    # highlight with shimmer variation
    for i in range(length):
        t = i / length
        x = WIDTH // 2 + int((t - 0.5) * 220 + sway) + 18
        y = base_y + int(t * 120 - (t ** 1.5) * curl) - 4
        if 140 <= y < HEIGHT - 20 and 0 <= x < WIDTH:
            draw_pixel(draw, x, y, highlight)

    # curling edge dithering
    for ix in range(WIDTH // 2 + 80, WIDTH // 2 + 150):
        for iy in range(base_y - 60, base_y + 80):
            if (ix + iy) % 4 == 0:
                draw_pixel(draw, ix, iy, PALETTE['leaf_shade_dither'])


def draw_book(draw: ImageDraw.ImageDraw, t: float, sway: float):
    base_x = WIDTH // 2 - 90 + int(sway * 0.3)
    base_y = 280 + int(math.sin(t * math.pi * 2) * 2)
    width = 180
    height = 110

    # book cover
    cover_rect = [
        (base_x, base_y),
        (base_x + width, base_y + height)
    ]
    draw.rectangle(cover_rect, fill=PALETTE['book_cover'])

    # outline
    draw.rectangle(cover_rect, outline=PALETTE['book_line'])

    # page block
    pages_rect = [
        (base_x + 16, base_y + 16),
        (base_x + width - 16, base_y + height - 24)
    ]
    draw.rectangle(pages_rect, fill=PALETTE['book_page'])
    draw.rectangle(pages_rect, outline=PALETTE['page_line'])

    # page flutter
    flutter = int(math.sin(t * math.pi * 4) * 5)
    draw.line(
        [
            (base_x + width - 16 + flutter, base_y + 18),
            (base_x + width - 16 + flutter, base_y + height - 26)
        ],
        fill=PALETTE['book_page_shadow']
    )

    # trim and bookmark tail
    draw.rectangle(
        [
            (base_x + 8, base_y + height - 24),
            (base_x + width - 8, base_y + height - 8)
        ],
        fill=PALETTE['book_side']
    )
    draw.rectangle(
        [
            (base_x + width // 2 - 10, base_y + height - 8),
            (base_x + width // 2 + 10, base_y + height + 46)
        ],
        fill=PALETTE['bookmark_mid']
    )
    draw.rectangle(
        [
            (base_x + width // 2 - 10, base_y + height + 46),
            (base_x + width // 2 + 10, base_y + height + 60)
        ],
        fill=PALETTE['bookmark_dark']
    )

    for dx, dy in BOOK_DITHER_PATTERN:
        for y in range(base_y + height - 24, base_y + height - 8, 4):
            x = base_x + 8 + dx
            if base_x + 8 <= x < base_x + width - 8:
                draw_pixel(draw, x, y + dy, PALETTE['book_dither'])


def draw_caterpillar(draw: ImageDraw.ImageDraw, frame_index: int, sway: float):
    t = frame_index / FRAMES
    base_x = WIDTH // 2 - 40 + int(sway * 0.4)
    base_y = 250 + int(math.sin(t * math.pi * 2) * 4)

    segments = 5
    seg_spacing = 26
    breathing = 1 + 0.02 * math.sin(t * math.pi * 2)

    for i in range(segments):
        seg_x = base_x + i * seg_spacing
        seg_y = base_y - int(math.sin((t + i * 0.05) * math.pi * 2) * 3)
        radius = int(26 * (1 - 0.04 * i) * breathing)
        fill = [PALETTE['caterpillar_light'], PALETTE['caterpillar_mid'], PALETTE['caterpillar_high'], PALETTE['caterpillar_mid'], PALETTE['caterpillar_light']][i]
        outline = PALETTE['caterpillar_dark']
        bbox = [
            (seg_x - radius, seg_y - radius + 20),
            (seg_x + radius, seg_y + radius)
        ]
        draw.ellipse(bbox, fill=fill, outline=outline)
        # top highlight
        for dx in range(-radius // 2, radius // 2, 2):
            draw_pixel(draw, seg_x + dx, seg_y - radius // 2 + 28, PALETTE['caterpillar_high'])

    # head details
    head_x = base_x
    head_y = base_y - 10
    draw.ellipse(
        [
            (head_x - 26, head_y - 12),
            (head_x + 26, head_y + 24)
        ],
        outline=PALETTE['caterpillar_dark'],
        fill=PALETTE['caterpillar_light']
    )
    draw.ellipse(
        [
            (head_x - 16, head_y - 2),
            (head_x - 2, head_y + 12)
        ],
        fill=PALETTE['eye_white'],
        outline=PALETTE['caterpillar_dark']
    )
    draw.ellipse(
        [
            (head_x - 12, head_y + 4),
            (head_x - 6, head_y + 10)
        ],
        fill=PALETTE['eye_pupil']
    )
    draw.line(
        [
            (head_x - 8, head_y + 16),
            (head_x + 12, head_y + 20)
        ],
        fill=PALETTE['caterpillar_dark']
    )
    mouth_curve = [
        (head_x + 6 + i, head_y + 16 + int(math.sin(i / 8) * 2))
        for i in range(-6, 7)
    ]
    draw.line(mouth_curve, fill=PALETTE['caterpillar_mid'])

    # antenna sway
    sway_angle = math.sin(t * math.pi * 2 + math.pi / 4) * 6
    for direction in (-1, 1):
        start = (head_x + direction * 10, head_y - 8)
        end = (
            head_x + direction * (10 + int(sway_angle * 0.5)),
            head_y - 38 - int(sway_angle * direction)
        )
        draw.line([start, end], fill=PALETTE['antenna_dark'])
        draw_pixel(draw, end[0], end[1], PALETTE['antenna_light'])


def draw_highlight(draw: ImageDraw.ImageDraw, t: float):
    center_x = WIDTH // 2
    center_y = 260
    radius = 120 + int(4 * math.sin(t * math.pi * 2))
    for angle in range(0, 360, 5):
        rad = math.radians(angle)
        x = center_x + int(math.cos(rad) * radius)
        y = center_y + int(math.sin(rad) * radius)
        if 120 < y < HEIGHT - 20 and 0 <= x < WIDTH:
            draw_pixel(draw, x, y, PALETTE['highlight_soft'])


def apply_ambient_glow(image: Image.Image):
    draw = ImageDraw.Draw(image, 'RGBA')
    glow_radius = 150
    for y in range(120, HEIGHT - 40, 4):
        for x in range(WIDTH // 2 - 150, WIDTH // 2 + 150, 4):
            dx = x - WIDTH // 2
            dy = y - 260
            dist = math.sqrt(dx * dx + dy * dy)
            if dist < glow_radius:
                fade = max(0, 1 - dist / glow_radius)
                color = (
                    PALETTE['ambient_glow'][0],
                    PALETTE['ambient_glow'][1],
                    PALETTE['ambient_glow'][2],
                    int(PALETTE['ambient_glow'][3] * fade)
                )
                draw_pixel(draw, x, y, color)


def add_dithering(image: Image.Image):
    draw = ImageDraw.Draw(image, 'RGBA')
    for x in range(WIDTH // 2 - 70, WIDTH // 2 + 90, 4):
        for y in range(240, 320, 4):
            for dx, dy in DITHER_PATTERN:
                px = x + dx
                py = y + dy
                if 0 <= px < WIDTH and 0 <= py < HEIGHT:
                    existing = image.getpixel((px, py))
                    if existing[3] != 0 and existing[:3] == PALETTE['caterpillar_mid'][:3]:
                        draw_pixel(draw, px, py, PALETTE['caterpillar_high'])


def enforce_palette(image: Image.Image):
    pixels = image.load()
    for x in range(image.width):
        for y in range(image.height):
            color = pixels[x, y]
            if color in ALLOWED_COLOR_SET:
                continue
            if color in NEAREST_COLOR_CACHE:
                pixels[x, y] = NEAREST_COLOR_CACHE[color]
                continue
            # find nearest allowed color (RGBA Euclidean distance)
            best_color = None
            best_distance = 10 ** 9
            for target in ALLOWED_COLORS:
                dr = color[0] - target[0]
                dg = color[1] - target[1]
                db = color[2] - target[2]
                da = color[3] - target[3]
                dist = dr * dr + dg * dg + db * db + da * da
                if dist < best_distance:
                    best_distance = dist
                    best_color = target
            pixels[x, y] = best_color
            NEAREST_COLOR_CACHE[color] = best_color


def build_frame(frame_index: int) -> Image.Image:
    t = frame_index / FRAMES
    sway = math.sin(t * math.pi * 2) * 6
    image = Image.new('RGBA', (WIDTH, HEIGHT), (0, 0, 0, 0))
    draw = ImageDraw.Draw(image, 'RGBA')

    draw_book(draw, t, sway)
    draw_leaf(draw, frame_index, sway)
    draw_caterpillar(draw, frame_index, sway)
    draw_highlight(draw, t)
    add_dithering(image)
    apply_ambient_glow(image)
    apply_vignette(image)
    enforce_palette(image)
    return image


def build_rough_frame(frame_index: int) -> Image.Image:
    t = frame_index / ROUGH_FRAMES
    sway = math.sin(t * math.pi * 2) * 4
    image = Image.new('RGBA', (WIDTH, HEIGHT), (0, 0, 0, 0))
    draw = ImageDraw.Draw(image, 'RGBA')
    base_x = WIDTH // 2
    base_y = 260
    # draw thin guides
    draw.line([(base_x, 120), (base_x, HEIGHT - 1)], fill=PALETTE['outline'])
    draw.line([(base_x - 100, base_y), (base_x + 100, base_y)], fill=PALETTE['outline'])
    draw.line([(base_x, base_y), (base_x + int(60 + sway), base_y + 100)], fill=PALETTE['outline'])
    draw.line([(base_x, base_y), (base_x - int(60 + sway), base_y + 100)], fill=PALETTE['outline'])
    draw.rectangle(
        [
            (base_x - 80 + sway, base_y + 20),
            (base_x + 80 + sway, base_y + 110)
        ],
        outline=PALETTE['outline']
    )
    draw.ellipse(
        [
            (base_x - 40 + sway, base_y - 40),
            (base_x + 40 + sway, base_y + 20)
        ],
        outline=PALETTE['outline']
    )
    return image


def save_palette_manifest():
    manifest_path = OUTPUT_DIR / 'palette.txt'
    with manifest_path.open('w', encoding='utf-8') as fh:
        fh.write('BookWorm Ambient Idle Loop Palette (<=64 colors)\n')
        for name, color in PALETTE.items():
            fh.write(f"{name}: {color}\n")


def image_to_data_uri(image: Image.Image) -> str:
    buffer = BytesIO()
    image.save(buffer, format='PNG')
    encoded = base64.b64encode(buffer.getvalue()).decode('ascii')
    return f"data:image/png;base64,{encoded}"


def write_assets_js(frames: list[str], planning_frames: list[str]):
    lines = [
        '// Generated by tools/generate_bookworm_idle_loop.py\n',
        '// Contains base64-encoded PNG frames for the BookWorm ambient idle loop.\n',
        f'export const {META_EXPORT_NAME} = {{\n',
        f'  width: {WIDTH},\n',
        f'  height: {HEIGHT},\n',
        f'  fps: {FPS},\n',
        "  hudReserve: { width: 200, height: 120, position: 'top-center' },\n",
        "  reservedCorners: ['upper-left', 'lower-right'],\n",
        "  paletteReference: '../palette.txt',\n",
        '};\n\n',
        f'export const {FINAL_EXPORT_NAME} = [\n',
    ]
    for data_uri in frames:
        lines.append(f"  '{data_uri}',\n")
    lines.append('];\n\n')
    lines.append(f'export const {ROUGH_EXPORT_NAME} = [\n')
    for data_uri in planning_frames:
        lines.append(f"  '{data_uri}',\n")
    lines.append('];\n')

    with ASSET_JS_PATH.open('w', encoding='utf-8') as fh:
        fh.writelines(lines)


def write_preview_html():
    html = f"""<!DOCTYPE html>
<html lang=\"en\">
  <head>
    <meta charset=\"utf-8\" />
    <title>BookWorm Ambient Idle Loop Preview</title>
    <style>
      :root {{
        color-scheme: dark;
        font-family: 'Inter', 'Segoe UI', sans-serif;
        background: #0d0812;
        color: #f9f5ec;
      }}
      body {{
        margin: 0;
        min-height: 100vh;
        display: flex;
        align-items: center;
        justify-content: center;
        padding: 24px;
      }}
      .card {{
        background: rgba(22, 16, 28, 0.9);
        border: 1px solid rgba(250, 212, 184, 0.25);
        border-radius: 16px;
        padding: 24px;
        box-shadow: 0 32px 64px rgba(0, 0, 0, 0.45);
        max-width: 720px;
      }}
      canvas {{
        display: block;
        width: 438px;
        height: 438px;
        image-rendering: pixelated;
        border-radius: 20px;
      }}
      .controls {{
        margin-top: 16px;
        display: flex;
        gap: 12px;
        align-items: center;
      }}
      button {{
        background: #f4c98a;
        color: #2c1b2f;
        border: none;
        padding: 10px 16px;
        border-radius: 999px;
        cursor: pointer;
        font-weight: 600;
      }}
      button:focus-visible {{
        outline: 2px solid #ffffff;
        outline-offset: 2px;
      }}
      .meta {{
        font-size: 14px;
        opacity: 0.75;
      }}
    </style>
  </head>
  <body>
    <div class=\"card\">
      <canvas id=\"preview\" width=\"{WIDTH}\" height=\"{HEIGHT}\" aria-label=\"BookWorm ambient idle animation preview\"></canvas>
      <div class=\"controls\">
        <button id=\"toggle-mode\" type=\"button\">Show planning rough</button>
        <span class=\"meta\">{FRAMES} production frames · {ROUGH_FRAMES} planning frames · {FPS} fps</span>
      </div>
    </div>
    <script type=\"module\">
      import {{ {FINAL_EXPORT_NAME}, {ROUGH_EXPORT_NAME}, {META_EXPORT_NAME} }} from './{ASSET_JS_PATH.name}';

      const canvas = document.getElementById('preview');
      const ctx = canvas.getContext('2d');
      const toggle = document.getElementById('toggle-mode');

      let frameIndex = 0;
      let usingPlanning = false;
      let lastTimestamp = 0;

      const finalFrames = {FINAL_EXPORT_NAME}.map(src => Object.assign(new Image(), {{ src }}));
      const planningFrames = {ROUGH_EXPORT_NAME}.map(src => Object.assign(new Image(), {{ src }}));

      toggle.addEventListener('click', () => {{
        usingPlanning = !usingPlanning;
        toggle.textContent = usingPlanning ? 'Show production render' : 'Show planning rough';
        frameIndex = 0;
      }});

      function drawFrame(images, index) {{
        const image = images[index % images.length];
        if (!image.complete) {{
          return;
        }}
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        ctx.drawImage(image, 0, 0);
      }}

      function step(timestamp) {{
        const elapsed = timestamp - lastTimestamp;
        const interval = 1000 / {META_EXPORT_NAME}.fps;
        const frames = usingPlanning ? planningFrames : finalFrames;

        if (elapsed >= interval) {{
          drawFrame(frames, frameIndex);
          frameIndex = (frameIndex + 1) % frames.length;
          lastTimestamp = timestamp;
        }}
        requestAnimationFrame(step);
      }}

      requestAnimationFrame(step);
    </script>
  </body>
</html>
"""

    with PREVIEW_HTML_PATH.open('w', encoding='utf-8') as fh:
        fh.write(html)


def main():
    ensure_dirs()
    final_frames = []
    planning_frames = []

    for i in range(FRAMES):
        frame = build_frame(i)
        final_frames.append(image_to_data_uri(frame))
        if (i + 1) % 12 == 0 or i == FRAMES - 1:
            print(f'Encoded {i + 1}/{FRAMES} production frames', flush=True)

    for i in range(ROUGH_FRAMES):
        frame = build_rough_frame(i)
        planning_frames.append(image_to_data_uri(frame))
        print(f'Encoded {i + 1}/{ROUGH_FRAMES} planning frames', flush=True)

    write_assets_js(final_frames, planning_frames)
    write_preview_html()
    save_palette_manifest()


if __name__ == '__main__':
    main()
