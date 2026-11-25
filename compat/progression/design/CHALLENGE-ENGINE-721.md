# Challenge engine primitives — #721

Engine-only change; no current path definitions altered. All paths may opt in.

- Milestones accept boolean `strict_order` (default false). A strict milestone with `after` prerequisites collects only when they were complete at the start of the signal. One signal cannot satisfy consecutive strict steps. Legacy milestones keep pre-counting and completion semantics. Existing saved counters are not deleted; use fresh IDs or explicit migration when converting previously live milestones. Cyclic milestone prerequisite graphs now fail validation.
- Event `dimension_entered`: target is destination dimension registry ID (e.g. `minecraft:the_nether`); emitted on actual server dimension transition, not login or historical vanilla advancement. Creative/spectator/dead excluded, ordinary earning and path eligibility enforced. Count 1 for one-time entitlement; existing ledger handles idempotence. No client packets or fake advancement grants.
- Existing achievement-backed skills (`auto_achievements`) and `activation: {"mode":"toggle"}` are the foundation for reusable tool forms, not paid ranks. Tool forms must have toggleable effect gates; plan mutually exclusive active right-click tool forms (normal/off available), separate Fortune/Silk mode, no forced block modification. Actual native tool action handlers, mutual exclusion, trial unlocks and persistent HUD remain subsequent engine slices. No Monk IDs introduced in engine code.
- Unit tests cover strict vs legacy vs parallel progress, no same-signal progress on next strict step, count caps, joined state, matching dimension, malformed flags and cyclic graph. Server event, persistence and multiplayer gameplay acceptance outstanding.

No full smoke, assembly, packaging or live gameplay run authorized. 
