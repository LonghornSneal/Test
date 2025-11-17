# CosmoBond BusyBee Animation Plan — Gemini GIF Workflow

## Production Approach
- **Generator:** Google Gemini video diffusion emphasizing cel-animated wing motion and honey particle trails.
- **Export guardrails:** 438 × 438 px, 4-second loops, midnight honeycomb (#090706) background, and isometric camera to show hex grids.
- **Post-processing:** Convert to 60 fps PNG stacks, enhance glow in Fusion, and export GIF/WebM plus sprite sheets.

## Pet Model Snapshot
- Chubby bee with translucent wings, hexagonal thorax segments, and a tiny planner notebook strapped to its waist.
- Palette: black stripes, golden-yellow body, iridescent wings, and teal eye highlights.
- Face: large friendly eyes, button nose, and confident smile; art style merges kawaii and infographic iconography.
- Accessories: honey-meter gauge, floating task icons, and a pen-shaped stinger stylus.
- Hovers near the top-left quadrant, leaving dial space clear.

## Interaction Map
- **Task Complete:** Triggered when the user finishes a to-do item, spotlighting victory waggle dances.
- **Planning Focus:** Fires when the user starts a focus timer or adds tasks, highlighting note-taking poses.

## Animation States

### Idle State — "Hive Hover"
- **Title:** Hive Hover
- **Pet Visual Description:**
  - BusyBee hovers with slow wing beats; stripes glow softly.
  - Honey-meter gauge pulses gently.
  - Planner notebook flutters open slightly.
  - Floating hex tiles orbit lazily.
  - Tail stinger rests calmly.
- **Action Sequence:**
  1. Bee dips slightly, wings buzzing quietly.
  2. It glances at the honey-meter, nodding contentedly.
  3. Notebook pages flip once.
  4. Bee returns to neutral hover.

### Interaction State — Task Complete "Waggle Burst"
- **Title:** Waggle Burst
- **Pet Visual Description:**
  - Colors saturate; stripes emit light.
  - Wings beat rapidly, creating motion blur.
  - Honey-meter fills to 100% with sparkle overflow.
  - Task icons flip to checkmarks.
  - Tail stinger draws celebratory zigzags.
- **Action Sequence:**
  1. BusyBee performs a figure-eight waggle dance.
  2. It pumps fists, releasing honey sparks.
  3. The bee spins once, scattering checkmark confetti.
  4. It salutes with the stylus before looping the dance.

### Interaction State — Planning Focus "Hex Sketch"
- **Title:** Hex Sketch
- **Pet Visual Description:**
  - Bee hovers closer to the viewer; eyes focused.
  - Notebook opens fully with glowing grid pages.
  - Tail stylus drips honey ink.
  - Floating hex tiles align into a planning board.
  - Wings slow to minimize movement.
- **Action Sequence:**
  1. BusyBee dips the stinger stylus into a honey inkwell.
  2. It sketches hex icons onto the floating board.
  3. The bee taps each icon, lighting them up sequentially.
  4. It closes the notebook with satisfaction before repeating.

### Sick State — "Overdue Swarm"
- **Title:** Overdue Swarm
- **Pet Visual Description:**
  - Colors fade; eyes show worry.
  - Tiny gray bees swarm around, representing overdue tasks.
  - Honey-meter drains to near empty.
  - Notebook papers scatter.
  - Wings droop.
- **Action Sequence:**
  1. BusyBee flails, trying to shoo away the gray swarm.
  2. It sighs, shoulders sagging as honey drips fall.
  3. The bee gathers scattered papers frantically.
  4. Exhausted, it droops midair before repeating.

### Happy State — "Honey Halo"
- **Title:** Honey Halo
- **Pet Visual Description:**
  - Bee glows golden; wings form a halo pattern.
  - Honey-meter overflows elegantly.
  - Planner notebook spins, projecting success stamps.
  - Tail stylus draws luminous loops overhead.
  - Floating flowers bloom around the bee.
- **Action Sequence:**
  1. BusyBee rises, wings creating a circular halo.
  2. It sprinkles honey droplets that transform into flowers.
  3. The bee twirls, stamping "Done" icons in mid-air.
  4. It blows a kiss, resetting the halo glow.

### Runaway State — "Abandoned Hive"
- **Title:** Abandoned Hive
- **Pet Visual Description:**
  - Colors streak into muted lines; wings tear slightly.
  - Honey-meter cracks; planner shuts with a thud.
  - Bee darts toward a dark corridor formed by collapsing hex tiles.
  - Task icons shatter behind it.
  - Tail leaves a broken line.
- **Action Sequence:**
  1. BusyBee pauses, looking back with regret.
  2. It speeds toward the corridor, dodging falling tiles.
  3. The honey-meter bursts, spilling honey as it flies.
  4. Bee vanishes into the darkness, trail fading before reset.

### Death State — "Amber Reliquary"
- **Title:** Amber Reliquary
- **Pet Visual Description:**
  - Bee encased in translucent amber droplet with suspended hex icons.
  - Wings folded neatly; eyes closed peacefully.
  - Planner notebook and stylus rest beside inside the amber.
  - Soft golden light glows from within.
  - Amber droplet sits atop a pedestal hive.
- **Action Sequence:**
  1. Amber seals around BusyBee, trapping final sparkles.
  2. Light pulses gently, illuminating the suspended icons.
  3. Pedestal hive emits a warm hum, then quiets.
  4. Droplet remains still before loop restart.
