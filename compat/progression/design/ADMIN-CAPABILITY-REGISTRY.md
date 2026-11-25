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

Future binding/resource handlers should discover configured definitions (and stale owned records where safe), validate allowed values and use the mechanic's existing mutation API. Elixir (#610) must unbind through its actual exclusivity handler, not erase arbitrary mode keys. Resource #681 needs the final resource service and typed value-changing operations. Native forms, housing receipts and personal enchant escrow need dedicated ownership-safe handlers; generic clearing could destroy items or duplicate rewards. No automatic bulk repair, full reset, native cures, arbitrary set/add payloads or provider discovery is claimed here.

## Acceptance

A: pure registry tests cover duplicate IDs, dynamic key enumeration, read-only preview, stale-plan rejection by handler, re-planning, per-target isolation, unknown keys and unsupported actions. Module compile and existing progression tests at exact PR head required.
B: with authorized candidate, inspect/list/validate from console and op, deny non-op; preview and confirm a configured activation, verify cooldown cleared without granting power or XP; reset one saved mutation and verify other effects remain; exact receipt reset permits eligible re-earn only under ordinary source rules; derived reconcile refreshes only selected player.
C: multiplayer, relog/death persistence, menu and HUD updates, no unrelated native state/effects modified. No full boot or package automatically authorized.
