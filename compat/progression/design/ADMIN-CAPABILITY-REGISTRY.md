# Progression administrative capability registry

Owner-approved 2026-11-24, issue #686 / PR #687. Engine-owned, not a second state store. Registry IDs are stable namespaced handler identities; keys are discovered dynamically from the currently loaded definitions or the selected player's actual persisted records. No path-specific command registration, unrestricted mode/NBT editing, or client write payload.

## Generic command surface

- `/tyrprogress admin capability <player> list`
- `/tyrprogress admin capability <player> <capability> keys`
- `/tyrprogress admin capability <player> <capability> inspect <key>`
- `/tyrprogress admin capability <player> <capability> validate <key>`
- `/tyrprogress admin capability <player> <capability> reset preview <key>`
- `/tyrprogress admin capability <player> <capability> reset confirm <key>`
- `/tyrprogress admin capability <player> <capability> reconcile preview <key>`
- `/tyrprogress admin capability <player> <capability> reconcile confirm <key>`

Explicit online player target; permission level 2; console works with player name. Keys use the remainder of the command, so namespaced/composite IDs do not require quoting. List and suggestions are bounded; use autocomplete or exact persisted key. Unsupported actions, unknown handler IDs, unknown keys fail closed. A preview is read-only, not a persistent authorization token; confirmation always computes a fresh plan against live state. Logs identify operator, target UUID, scoped before/after and action. No mass reset. Existing typed currency/skill commands remain unchanged.

| Handler ID | Key | Mutating operation |
|---|---|---|
| tyr_progression:activation | path ID + pipe + skill ID (from activation definitions) | Reset stops the skill and clears its cooldown; no activation, blood payment, or XP award |
| tyr_progression:mutation | policy-owner path ID + pipe + effect ID (from saved state) | Reset removes only that effect's potency and all of its progress tiers for that owner; no unrelated effects or XP |
| tyr_progression:reward_receipt | exact saved system/source/completion receipt (pipe-delimited) | Reset removes only the selected paid receipt, which may permit repeat earning; no XP subtraction or recent transaction/achievement clearing |
| tyr_progression:derived_state | player | Reconcile Progression-owned runtime effects and the last requested skill menu; no raw native-state changes |

## Extension contract

Register a trusted Java handler through `AdminCapabilities.registry().register(id, handler)` during initialization. Duplicate IDs fail rather than override another mechanic. A handler provides description, dynamic keys, read-only inspect/validate, advertised mutating actions and a side-effect-free plan returning before/after plus a synchronous commit. Confirmed commits must validate current state and call the owning service/attachment; do not use reflection, blanket attachment replacement or arbitrary client-provided NBT. Reset commits get common derived-state reconciliation; custom reconcile handlers own their own refresh. Treat validation findings as scoped, not proof of global player health.

Binding/resource handlers discover configured definitions and use owning service APIs. Explicit Elixir reset unbinds without awarding effects or XP. Resource amounts are raw integers; set/add/subtract require explicit amount and reject values outside the current capacity. Native forms, housing receipts and personal enchant escrow need dedicated ownership-safe handlers; generic clearing could destroy items or duplicate rewards. No automatic bulk repair, full reset, native cures, arbitrary untyped set/add payloads or provider discovery is claimed here.

## Acceptance

A: pure registry tests cover duplicate IDs, dynamic key enumeration, read-only preview, stale-plan rejection by handler, re-planning, per-target isolation, unknown keys and unsupported actions. Module compile and existing progression tests at exact PR head required.
B: with authorized candidate, inspect/list/validate from console and op, deny non-op; preview and confirm a configured activation, verify cooldown cleared without granting power or XP; reset one saved mutation and verify other effects remain; exact receipt reset permits eligible re-earn only under ordinary source rules; derived reconcile refreshes only selected player.
C: multiplayer, relog/death persistence, menu and HUD updates, no unrelated native state/effects modified. No full boot or package automatically authorized.

## Added after the dev/progression synchronization

| Capability | Key | Supported edit |
|---|---|---|
| tyr_progression:resource | resource ID | set/add/subtract (amount before key), fill, empty, reset to configured initial; only joined path; strict bounds; preserve event receipts |
| tyr_progression:elixir | configured owner path ID | explicit reset/unbind, no XP/effects; normal eligible serving rebinds |
| tyr_progression:discovery | path ID + pipe + item ID | forget local item only, never a lifetime paid receipt |
| tyr_progression:mode | stored key | read-only, no arbitrary edits to opaque state |
| tyr_progression:tree | path ID | normal configured respec with its lifecycle hooks, not a forced blanket deletion |

Example: `/tyrprogress admin capability Player tyr_progression:resource set confirm 50 namespace:meter`. Other numeric actions have the same syntax. Fill/empty/reset have no amount. Reconciliation refreshes the resource HUD. Typed skill command: `skill <player> <path> <skill> set <rank> [cascade] [no_refund] confirm` reduces by removing latest purchased ranks first, preserving original currency/payment order; `set <rank> via <currency> confirm` raises to an exact rank through normal admin grant gates. Elixir ownership loss calls the configured reset policy. Whole-tree respec retains/forfeits/refunds according to existing policy and clears local discoveries through normal lifecycle; no blanket raw attachment clearing.

B/C additions: joined vs dormant resource owner, exact capacity and overflow rejection, confirm and HUD refresh, unbind and rebind from actual serving, discovery/receipt independence, multi-currency partial rank reduction and configured tree respec. No runtime acceptance yet.

## Additional debug coverage

| Capability | Key | Action |
|---|---|---|
| tyr_progression:milestone | path ID + pipe + milestone ID | grant marks completed and records achievement without reward; reset clears unfinished criteria only |
| tyr_progression:counter | path ID + pipe + milestone ID + pipe + zero-based index | set/add/subtract/reset unfinished count within criterion bounds; normal gameplay event may settle ready completion |
| tyr_progression:mutation | owner path ID + pipe + effect ID | set/add/subtract human rank (1-based; zero removes), normalized threshold progress; policy/registry validation for positive rank |
| tyr_progression:path | path ID | inspect join/leave blocker, validate dormant membership, join/leave through normal lifecycle and existing vetoes |
| tyr_progression:xp_trace | player | set duration in seconds (1..120), inspect last 24 lines, reset disables/clears; transient per player, no disk logs |
| tyr_progression:source_cooldown | legacy system ID / source index | reset that cooldown only; no receipts/XP edits |
| tyr_progression:native_form | player | inspect provider observation, validate UNKNOWN, reconcile cached observation and owned effects only |

Examples: `capability Player tyr_progression:milestone grant confirm test:path|test:trial`; `capability Player tyr_progression:counter set confirm 2 test:path|test:trial|0`; `capability Player tyr_progression:mutation set confirm 2 test:path|minecraft:speed`; `capability Player tyr_progression:xp_trace set confirm 30 player`, then inspect player. All are under /tyrprogress admin. No native transformation force/cure or completed milestone revocation: legacy reward replay and native provider-owned state need explicit safe adapters. Trace covers the shared router/modifier settlement boundary, not every pre-router rejected gameplay event. A trace reports a proposed ledger settlement, not independent confirmation of later attachment publication. B/C: verify normal trial payment vs admin completion, count limits, two players, mutation downgrade/no unrelated loss, join veto/leave effects, trace expiry/logout and native probe recheck.

## XP award syntax (owner-approved regular default)

- `/tyrprogress admin currency <player> <currency> award <amount>` — regular, applies configured reward modifiers and growth divisor.
- `/tyrprogress admin currency <player> <currency> award fixed <amount>` — fixed, bypasses growth, uses applicable reward modifiers and normal settlement policy.
- `/tyrprogress admin currency <player> <currency> award true <amount>` — exact direct ledger credit, no modifiers, growth or settlement receipt; rejects cap overflow.

Regular/fixed use the explicit-destination award API with the existing admin_grant source ID, unique transaction and no first-completion identity. They respect normal earning eligibility, nominal award limits, minimum reward rounding and cap. No rerouting. True is equivalent to direct add, preserving receipts and commitments; direct add/subtract/set/reset retain their existing semantics. Report both requested and credited XP, plus new balance/debt. Existing legacy path XP command remains regular. B: compare the three forms on same restored balance/known growth and multiplier, verify console/permissions, cap behavior and no first-completion receipts.

## Consolidated operator recovery

- `/tyrprogress admin diagnose <player>` validates canonical point backing, known currencies/skills, paid and payment receipt order/count, joined Paths and baseline membership; additionally scans registered diagnostic handlers (256 keys/handler, 2048 total, 256 findings maximum in registry; output bounded separately). Informational mode/trace/derived handlers are excluded from automatic findings. A clean report does not prove gameplay correctness.
- `/tyrprogress admin repair <player> [confirm]` previews or reconciles the normal baseline/Core projection, invalidates provider observation cache, refreshes owned effects/menu/HUD, then diagnoses again. No purchase/receipt deletion or native transformation. Inconsistent Newbie specialist/Core investments remain explicit recovery cases, not silently forfeited by repair.
- `capability <player> tyr_progression:class_mode set confirm <0|1> <armor|glide|treasure|ordeal>`: 0/1 off/on, fortune/silk, or off/active respectively; reset restores configured service defaults. Completed ordeal cannot be erased or granted. Non-default mode requires active owning ability.
- `capability <player> tyr_progression:stage grant confirm player` uses normal graduation; reset confirm player invokes destructive normal Newbie transition; preview describes forfeiture. No implicit arbitrary baseline change.
- `capability <player> tyr_progression:native_wallet set|add|subtract confirm <amount> <system>` changes only free legacy points of an initialized, active configured native channel. XP, rank and receipts untouched; generic currencies are excluded.
- `capability <player> tyr_progression:milestone reconcile confirm <path>|<milestone>` restores missing global achievement from completed local evidence; no XP or reward receipt changes.
- XP trace now also observes activity signal entry/earning gate, matched native source eligibility/cooldown, native nominal grant and absence of an eligible unified activity candidate. It cannot observe an event rejected before entering this API.

B/C additions: two-player diagnosis and repair isolation, report caps and stale keys, preview versus confirm, malformed toggle recovery, graduation vs Newbie forfeiture, active native free-point edits vs generic/departed channels, completed trial repair without reward replay, activity dispatch trace. No native provider data forcing, full reward replay or arbitrary opaque state editing. Remaining provider-specific transformations require a reviewed owning API; a completed milestone's local/achievement state cannot safely be revoked wholesale while legacy rewards lack durable per-trial payout receipts.
