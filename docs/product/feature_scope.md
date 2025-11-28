# Feature Scope (v1.0)

Status: draft scope for approval. Mark checkboxes once approved by the owner.

- [ ] v1.0 scope approved (owner + date)
- [ ] Interactive behaviors defined (taps/gestures)
- [ ] Data integrations confirmed (complications, sensors, services)
- [ ] Configuration options locked (color/palette, pet states, toggles)
- [ ] Deferred/post-v1 items documented

## Feature list
| Feature | Priority (P0/P1/P2) | Description | Dependencies | Status |
| --- | --- | --- | --- | --- |
| Time/date render | P0 | Center digital time with date on upper arc; user can toggle seconds and move date to lower arc; adaptive layout for ambient | WFF layout | Draft |
| Two complications (short text) | P0 | Left = steps/health, Right = heart-rate/notifications; user-selectable | Health services, notification data (provider) | Draft |
| Optional battery slot (top) | P1 | Monochrome battery complication when enabled | System battery provider | Draft |
| Color themes (Cosmic Blue/Starlight) | P0 | User style schema toggles interactive palette | WFF user style | Draft |
| Pet mood states (idle/happy/hungry/neglect) | P1 | Visual states with timers and persisted mood so state survives restarts | Local timers + storage | Draft |
| Ambient mode simplification | P0 | Monochrome palette, static pet silhouette, no particles | Ambient callbacks | Draft |
| Tap-to-emote on pet | P2 | Tap pet to trigger short emote animation | Hit target mapping | Deferred |
| On-device config screen | P2 | Kotlin host for palette/pet preview and complication hints | Host UI module | Deferred |

## Integrations
- Health/fitness data: steps/heart-rate via complications (system providers first).
- Media/notifications: none in v1.0; future option to use notification count in right slot.
- Remote config/feature flags: none in v1.0; consider for pet states in post-v1.
- Companion/phone bridge: none in v1.0.

## Configuration options
- Palette selection: Cosmic Blue, Starlight.
- Complication assignment: left/right/top via system editor.
- Optional: toggle pet animations (reduced motion) honoring system setting.

## Deferred/post-v1
- Tap-to-emote interactions.
- Expanded pet behaviors beyond v1 basics (extra states, quests).
- Companion bridge for phone battery and notifications.
- Remote-config flags for experiments.

## Risks / open questions
- Final font selection and licensing for Samsung store readiness.
- Availability of required complication providers on all target devices.
- Need for privacy messaging if notification-based data is used post-v1.
