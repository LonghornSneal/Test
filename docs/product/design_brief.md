# CosmoBond Galaxy Watch Face - Design Brief (v1.0)

Status: approved.

- [x] Dial layout finalized (positions for time, date, complications, and pet visuals)
- [x] Typography chosen (primary/secondary typefaces, weights, sizes)
- [x] Color palette approved (interactive + ambient variants, accessibility notes)
- [x] Complication plan confirmed (types, placement, resizing rules)
- [x] Animation storyboards approved (states, transitions, ambient-safe variants)

## Goals and audience
- Deliver a legible, personality-forward watch face for Galaxy Watch8 Classic that balances timekeeping with the CosmoBond pet.
- Prioritize quick-glance readability in interactive and ambient modes; keep pet motion subtle unless user engages.

## Visual direction
- Theme: space-inspired “Cosmic Companion” with clean dial and minimal chrome.
- Mood: confident, playful, and calming; particle accents used sparingly.

## Layout
- Time: customizable position; default centered. User can switch among digital 12h/24h (military), analog hands, or hybrid, and toggle seconds on/off.
- Date: upper arc, small caps (DDD, MMM DD) to preserve dial clarity.
- User layout options: move time within expanded safe zones (top, upper-left/right, center, lower-left/right, bottom arc), choose digital or analog style, toggle 12h/24h (military), toggle seconds, and place the date on upper or lower arc.
- Complications: two SHORT_TEXT slots low-left and low-right; optional top slot for battery if enabled.
- Pet placement: lower center; idle stance occupies bottom third; avoids complication hit targets.
- Safe areas: maintain >=24 px padding from screen edge; avoid overlap with hardware bezel.

## Typography
- Primary: Google Sans Text (or SamsungOne UI fallback) — Regular for body, Medium for time/date.
- Sizes: Time 36sp interactive, 32sp ambient; Date 14sp; Complications 13sp.
- Numeral style: tabular lining for time.

## Color palette
- Interactive: Cosmic Blue (#1F3FFF), Starlight (#F5F0E6), Accent Lime (#C7FF6A), On-surface Dark (#0C0F18).
- Ambient: Monochrome slate (#A8ADB7) on near-black (#05070D); disable gradients.
- Accessibility: minimum 4.5:1 for primary text vs. background; reduced-motion palette mirrors ambient set.

## Complication plan
- Slot IDs: left = steps/health, right = heart-rate or notifications, optional top = battery.
- Types: SHORT_TEXT + MONOCHROMATIC_IMAGE where supported; respect system data provider selection.
- Bounds: bottom arc radii sized to avoid pet footprint; hit targets >=48 px.

## Animation storyboards
- States: idle breathing, happy sparkle, mild hunger (subtle rumble), severe neglect (slowed posture), celebratory bounce.
- Ambient: freeze on idle silhouette with low-opacity outline; no particle effects.
- Transitions: crossfade between states over 250–400 ms; clamp frame budget to <=16 ms interactive / <=33 ms ambient.

## Interaction model
- Tap actions: top = battery (if present), left/right = respective complication deep link, pet tap = quick reaction emote.
- Configuration: color theme toggle (Cosmic Blue/Starlight), pet expression preview, complication reassignment via system editor.
- Analog hands: default style plus per-pet alternate hand style for analog mode.

## Accessibility
- Reduced motion: disable particle effects and keep pet in slow breathing idle; honor system setting.
- Contrast: maintain compliant text/background pairs in both themes; avoid thin strokes.

## Approvals
- Product/design owner: LonghornSneal (sole owner)
- Approval date: 2025-12-02
