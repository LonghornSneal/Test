# CosmoBond CardioCritter Animation Plan — Gemini GIF Workflow

## Production Approach
- **Generator:** Google Gemini video diffusion configured for 4-second seamless loops depicting the athletic CardioCritter in a softly lit training studio.
- **Export guardrails:** 438 × 438 px square framing, 8-bit color, deep charcoal (#0F1116) holdout background for keying, and camera locked to a gentle parallax tilt to preserve watch-face safe zones.
- **Post-processing:** Convert MP4 masters to 60 fps PNG stacks, retouch sweat highlights in Photoshop, then assemble GIF/WebM previews plus trimmed sprite sheets for Watch Face Format ingestion.

## Pet Model Snapshot
- Plump yet toned heart-shaped creature blending axolotl cheeks with a plush monster build; rendered in cel-shaded neo-anime lines with subtle grain.
- Primary palette: gradient magenta torso (#FF4F81 to #B3003C), neon coral fins, and glowing cyan cardio ring tattoo pulsing along its ribs.
- Face: oversized almond eyes with bright gold irises, a confident half-smile, and faint freckles; two antenna-like heart monitors sprout from its brow with ECG diodes at the tips.
- Accessories: sweatband stitched with micro LEDs, thumb-sized smart dumbbells that clip to its hips, and floating heart-rate HUD glyphs.
- Scale: fills lower two-thirds of dial while leaving northern arc free for complications; body language normally buoyant with springy knees.

## Interaction Map
- **Workout Boost:** Triggered when the user starts a logged workout or taps the cardio boost tile; emphasizes energetic movement and rep tracking.
- **Cooldown Coaching:** Triggered when the user launches the guided breathing cool-down; focuses on deliberate inhales/exhales and pulse recovery guidance.

## Animation States

### Idle State — "Pulse Glow Hover"
- **Title:** Pulse Glow Hover
- **Pet Visual Description:**
  - Base magenta body floats slightly above a circular gradient pad with cyan ECG pulses rippling outward.
  - Antenna diodes emit alternating fuchsia and teal light trails; eyes soften into relaxed crescents while the half-smile persists.
  - Sweatband stays centered with LED beats matching resting heart rate (animated dot sweeping left to right).
  - Tiny holographic cardio rings orbit the creature’s midsection, shimmering with semi-transparent pink overlays.
  - Background props include a translucent treadmill silhouette and faint gridlines anchoring perspective.
- **Action Sequence:**
  1. CardioCritter gently lowers its knees, arms dangling beside its torso while a cyan ECG line scrolls across the floor pad.
  2. It inhales, torso expanding and glowing brighter as antenna diodes raise upward, leaving neon trails that form a floating heart icon.
  3. The creature drifts three centimeters upward, tail curling toward its belly; eyes close halfway while the LED on the sweatband pulses twice.
  4. A soft exhale deflates the torso, and the cardio ring holograms widen; tiny motes of pink light fall like dust toward the pad.
  5. It performs a micro hip sway left to right, keeping the shoulders level, and the treadmill silhouette brightens then fades.
  6. Knees settle back into the initial hover stance, antenna trails dissolve, and the ECG line resets to begin the seamless loop.

### Interaction State — Workout Boost "Heartfire Interval Sprint"
- **Title:** Heartfire Interval Sprint
- **Pet Visual Description:**
  - Muscles appear more defined with specular streaks along the arms; magenta skin gradients intensify to fiery crimson.
  - Eyes open wide with determined pupils; mouth shifts into a toothy grin showing two canines.
  - Antenna diodes flare bright white, projecting translucent rep counters above each dumbbell prop.
  - Additional props: neon step blocks, holographic timer numbers, and animated motion blur swashes hugging the limbs.
  - Body elongates as it leans forward into a sprint stance, tail acting as dynamic counterweight.
- **Action Sequence:**
  1. CardioCritter plants one foot on a glowing step block and crouches, arms coiled, eyes narrowed on an invisible finish line.
  2. It launches forward, leaving a streak of magenta fire along the pad; antennae whip backward creating twin ECG lightning bolts.
  3. Mid-stride, the dumbbells detach and orbit like satellites while the pet pumps its arms, chest puffed and teeth exposed in exhilaration.
  4. The fox-like tail whips around, drawing a luminous trail that forms a heart outline, while the sweatband LED counts down "3, 2, 1".
  5. CardioCritter lands in a lunge, knees bent deeply, exhaling sparks that scatter as triangular particles near the floor.
  6. It springs back to the starting block, holographic rep counters increment, and the motion blur resets to loop the high-energy sprint.

### Interaction State — Cooldown Coaching "Rhythmic Recovery Breath"
- **Title:** Rhythmic Recovery Breath
- **Pet Visual Description:**
  - Skin gradients shift to soothing rose and lavender; shoulders relax while arms unfold outward like wings.
  - Eyes close peacefully with lashes casting soft shadows, and the smile softens into a serene line.
  - Antenna diodes pulse in slow alternating beats, projecting concentric breathing circles that fade from teal to lilac.
  - Props: translucent yoga mat with hexagonal texture, floating timer pebble showing inhale/exhale counts, and a hovering droplet of condensation.
  - Body hovers cross-legged with tail curled beneath like a cushion.
- **Action Sequence:**
  1. CardioCritter folds legs beneath its torso atop the hex mat, palms facing upward while a translucent timer pebble shows "4".
  2. It inhales through a tiny "o" mouth, chest expanding visibly as lavender light washes up from the mat edges.
  3. The antennae rise, emitting teal rings that drift outward; eyelids flutter, revealing golden irises for a beat before closing again.
  4. During the hold, the creature’s tail uncurls slightly and the timer pebble glows white, casting speckles onto the mat.
  5. A slow exhale releases sparkling droplets that arc downward, and the breathing circles contract toward the sternum.
  6. CardioCritter smiles gently, hands press together at heart center, and the mat returns to its baseline glow ready to restart the cycle.

### Sick State — "Slumped Signal Dip"
- **Title:** Slumped Signal Dip
- **Pet Visual Description:**
  - Colors desaturate toward muted mauve and dusty rose; a faint gray vignette encroaches from the canvas edges.
  - Eyes droop with heavy bags; sweatband LEDs blink irregularly, occasionally flickering off.
  - Antenna tips hang limp, dripping glitchy pixel droplets; skin texture gains subtle noise speckles representing fatigue.
  - Props: cracked heart-rate HUD hovering overhead with red warning triangles, toppled dumbbell resting beside the pet.
  - Body curls inward on a bench-like cushion, shoulders slouching noticeably.
- **Action Sequence:**
  1. CardioCritter collapses onto the cushion, elbows on knees, while a jagged ECG line sputters above its head.
  2. It sighs, shoulders rising minimally, and eyelids flutter with exhaustion; antennae droop, releasing dull red sparks that fizzle on contact with the floor.
  3. The pet rubs its chest with one paw, face twisting into a concerned frown as a translucent thermometer icon flickers beside it.
  4. It leans forward, almost sliding off the cushion, then catches itself with both paws, knees knocking together weakly.
  5. A cough animation emits small square particles, and the heart HUD dims to near darkness while the sweatband LED blinks a distressed amber color.
  6. The creature slumps back, hugging the toppled dumbbell for comfort, and the glitchy droplets rewind to loop the malaise sequence.

### Happy State — "Cardio Confetti Burst"
- **Title:** Cardio Confetti Burst
- **Pet Visual Description:**
  - Skin glows with saturated hot pinks and electric oranges, accented by shimmering glitter veins.
  - Eyes sparkle with star-shaped highlights; smile stretches wide showing gleaming enamel.
  - Antennae fan outward, each diode spawning holographic confetti ribbons and teeny heart balloons.
  - Props: floating podium with digital laurel wreath, animated fanfare banners swirling behind the pet.
  - Body stands tall with chest out, one foot perched on the podium edge.
- **Action Sequence:**
  1. CardioCritter hops onto the floating podium, arms spread, and tilts its chin upward while confetti cannons charge at both sides.
  2. It pumps both fists overhead, triggering the antennae to release a shower of neon ribbons cascading in a spiral.
  3. The pet spins 180 degrees on one foot, tail flaring into a glowing comet trail that encircles the podium.
  4. Landing the spin, it winks at the viewer, finger guns firing tiny heart sparks toward the screen corners.
  5. A triumphant stomp sends concentric waves through the podium surface, and digital laurel leaves wrap briefly around its torso.
  6. The pose resets to arms open as the confetti drifts downward, seamlessly returning to the opening celebration beat.

### Runaway State — "Signal Fade Dash"
- **Title:** Signal Fade Dash
- **Pet Visual Description:**
  - Colors streak into elongated magenta trails as if captured with long exposure; edges dissolve into pixel dust.
  - Eyes glance backward anxiously, and the smile disappears into a determined straight line.
  - Antenna diodes emit sharp white sparks that tear open a portal-like ECG waveform.
  - Props: dissolving gym floor tiles and a receding doorway made of holographic vitals icons.
  - Body leans far forward mid-sprint with arms tucked close to aerodynamic posture.
- **Action Sequence:**
  1. CardioCritter glances over its shoulder toward the viewer while cracks spider across the floor pad.
  2. It bolts toward the holographic doorway, leaving a trail of magenta light that stretches across the canvas.
  3. The antennae slice the air, opening an ECG portal whose waveform oscillates wildly as particles get sucked inside.
  4. Mid-run, the creature’s lower body becomes semi-transparent, revealing trailing afterimages of previous strides.
  5. It dives through the doorway, claws extended, while the gym floor tiles crumble into floating fragments.
  6. Only the fading ECG portal remains, shrinking to a single blinking dot before the loop resets to the initial glance.

### Death State — "Flatline Ember Drift"
- **Title:** Flatline Ember Drift
- **Pet Visual Description:**
  - Body desaturates to pale pink and translucent white, edges glowing with ghostly cyan outlines.
  - Eyes close peacefully; the sweatband LED shows a single horizontal line, and antennae stand motionless with dim embers.
  - Chest cavity reveals a faintly beating light that gradually dims; tiny ash motes rise from the paws.
  - Props: floating lotus-shaped stretcher composed of ECG lines and a hovering memorial beacon projecting a muted heart glyph.
  - Body reclines on the lotus cradle, limbs gently folded across its torso.
- **Action Sequence:**
  1. CardioCritter exhales a final visible breath, cheeks puffing before deflating as the heartbeat light flickers erratically.
  2. The lotus stretcher assembles beneath it, glowing cyan petals lifting the body as gravity loses influence.
  3. Antennae release the last pair of embers, which drift upward and burst into faint glitter before fading.
  4. The sweatband LED draws a flat horizontal line accompanied by a gentle white pulse that radiates outward.
  5. The creature’s outline dissolves into translucent ribbons that wrap around the memorial beacon.
  6. Only the beacon remains, pulsing softly, before rewinding to the initial exhale for a respectful looping tribute.
