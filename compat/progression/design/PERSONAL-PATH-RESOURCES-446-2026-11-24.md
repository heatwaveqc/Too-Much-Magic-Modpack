# Personal Path resources — first engine slice (#446)

Owner approval 2026-11-24: integer only; capacity 1–1,000,000, current balance 0..capacity. Display may divide by 10^n without fractional state. Personal per-player, multiple per Path. No sharing. HUD required. Engine stays within Progression, independently reviewable from deferred Bare Zerker/other Path configs. Java 21, Minecraft 1.21.1 NeoForge.

Add optional `resources` array to an existing Path JSON. Example ONLY (not shipped, not a playable Path):
```json
{
  "resources": [{
    "id": "example:fury",
    "capacity": 100,
    "initial": 0,
    "reset_on_death": true,
    "interval_ticks": 20,
    "hud": {"visible": true, "title": "Fury", "color": "d45b35", "display_divisor": 1},
    "rules": [
      {"event": "interval", "operation": "add", "amount": -1},
      {"event": "example:verified_damage", "operation": "add", "amount_per_unit": 1}
    ]
  }]
}
```
A reviewed native adapter must call PathResources.committed(player, namespaced event, stable operation receipt, nonnegative integer units) **after** a real successful gameplay event. It is not an arbitrary client RPC, command, or existing event auto-subscription. The above example event is not emitted yet. Actual damage attribution, overkill/fraction conversion, direct and indirect ownership and native Totem/death-prevention transaction are independent concrete adapters; do not claim Fury implemented. Fixed interval rules execute on online server tick (shared game time phase, no offline accumulation), fixed add/set only. Signed add clamps, set clamps, spend requires full amount or entire matching event transaction fails without any balance mutation. Rule `when` uses existing Eligibility, all matching predicates see pre-event balances. `{"type":"path_resource","id":"example:fury","comparison":">","value":50}` can gate existing effects/skills; unknown or not-joined resource yields unavailable, never recursive eligibility. Resource conditions read joined state even if a Path is temporarily ineligible, so a resource gate cannot recurse. Resource mutations/HUD require joined AND eligible. Capacity upgrades optional `capacity_skill` and `capacity_per_rank`; rank-based cap saturates at 1m, excess discarded on active cap decrease. Default dormant retention on leave, reset_on_death optional, no offline decay. Explicit reset-on-departure hook not implemented this pass; no sharing or native resource duplication.

Limits: 16 definitions per Path/256 catalog, 32 rules each, 1024 saved balances, 256 most recent committed receipts per player (trusted source must enforce permanent one-shot identity separately). State separate from XP, immutable persisted attachment copyOnDeath + clone reset handling; recent receipts survive death. Missing definitions dormant and never rendered or mutated. Server-only actor and eligibility; no world action rollback or crash-atomic two-file claim. Direct spend is trusted engine API, not proof an external irreversible world action succeeded. Future death prevention/world action must supply its own verified atomic adapter. HUD snapshot read-only personal lease, max 8 simultaneously displayed sorted by ID, top-left, hide with vanilla hide-GUI or open screen; each meter configurable label/color/visibility/display divisor. No user-adjustable placement yet. Data pack reload validates resource catalog before installing with paths; unknown condition reference unavailable at runtime. No TMM resource rules shipped yet (Path content deferred); no effect changes for unconfigured players.

QA A: regression tests for bounds, insufficient cost rollback, replay, display, isolated state. Module CI pending. B/C: add temporary approved QA-only fixture to an eligible Path, verify initial/interval/clamp/rank-cap, relog/death reset vs retention, leave/rejoin dormancy, two-player HUD isolation, resource condition passive effect, reload, screen size/F1, stale packet expiry and actual direct event adapter when available. No full assembly/boot/gameplay authorized.

CI repair: move Mojang codec out of pure ResourceState to ResourceStateCodec, unchanged serialized keys/contract. Isolated JUnit runtime lacks Mojang serialization classes. Reconcile current integrated transformation, food policy and housing registration from 50e28498549fbbc4320e2ed81156ec0eb356359b. No weakening of tests.
