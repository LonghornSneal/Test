# Feature Scope (v1.0)

Status: approved scope.

- [x] v1.0 scope approved (owner + date)
- [x] Interactive behaviors defined (taps/gestures)
- [x] Data integrations confirmed (complications, sensors, services)
- [x] Configuration options locked (color/palette, pet states, toggles)
- [x] Deferred/post-v1 items documented

## Feature list
| Feature | Priority (P0/P1/P2) | Description | Dependencies | Status |
| --- | --- | --- | --- | --- |
| Time/date render | P0 | Time can move within expanded safe zones (top, upper-left/right, center, lower-left/right, bottom); supports digital 12h/24h (military), analog hands, hybrid; per-pet alternate analog hands; date can move upper/lower; seconds toggle | WFF layout | Draft |
| Two complications (short text) | P0 | Left = steps/health, Right = heart-rate/notifications; user-selectable | Health services, notification data (provider) | Draft |
| Optional battery slot (top) | P1 | Monochrome battery complication when enabled | System battery provider | Draft |
| Color themes (Cosmic Blue/Starlight) | P0 | User style schema toggles interactive palette; presets only in v1 (no custom picker) | WFF user style | Draft |
| Pet mood states (idle/happy/hungry/neglect + pet-specific behaviors) | P1 | Visual states with timers, persisted mood, and pet-specific inputs per pet (sensors/apps) so state survives restarts; 20 pet variants need mapping (no shared data source) | Local timers + storage + data inputs | Draft |
| Ambient mode options | P0 | Range of power modes: static silhouette, ultra-slow breathing, or static+micro-blink; selectable power-saving levels | Ambient callbacks | Draft |
| Tap-to-emote on pet | P0 | Tap pet to trigger multiple short emote animations per pet | Hit target mapping | Planned for v1 |
| On-device config screen | P2 | Kotlin host for palette/pet preview and complication hints | Host UI module | Deferred |

## V1 pet selection and mini-games
- V1 pets to ship: JiggleJelly, BeatBunny, ShutterBug.
- Mini-games: each pet has a unique game aligned to its inputs (e.g., JiggleJelly wobble-balance via accelerometer, BeatBunny beat-match via media/tap, ShutterBug photo-hunt via camera remote/gyro).
- Consent: gate sensor/notification/mic access per pet before enabling its mini-game; provide opt-out and fallback idle behavior.
- Remaining pets: implemented later releases; keep per-pet behavior and mini-game slots reserved.

## Integrations
- Health/fitness data: steps/heart-rate via complications (system providers first).
- Media/notifications: none in v1.0; future option to use notification count in right slot.
- Remote config/feature flags: none in v1.0; consider for pet states in post-v1.
- Companion/phone bridge: none in v1.0.
- Per-pet data sources: no universal baseline; each of the 20 pets needs its own mapping of sensors/apps to behaviors.

## Configuration options
- Palette selection: Cosmic Blue, Starlight.
- Complication assignment: left/right/top via system editor.
- Optional: toggle pet animations (reduced motion) honoring system setting.

## Deferred/post-v1
- Tap-to-emote interactions beyond the first emote.
- Expanded pet behaviors beyond v1 basics (extra states, quests, per-pet quirks).
- Companion bridge for phone battery and notifications.
- Remote-config flags for experiments.

## Risks / open questions
- Final font selection and licensing for Samsung store readiness.
- Availability of required complication providers on all target devices.
- Need for privacy messaging if notification-based data is used post-v1.

## Approvals
- Product owner: LonghornSneal
- Approval date: 2025-12-02
