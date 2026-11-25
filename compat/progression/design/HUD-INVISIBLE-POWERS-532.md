# #532 — Progression HUD-invisible powers: server-owned grant framework and Elytra pilot

**Date:** 2026-11-23. This is a first-party `compat/progression` source implementation on isolated PR #616, not a selected live JAR or gameplay PASS.

## Controlling visibility policy — owner approved 2026-11-24

This supersedes any earlier blanket requirement to hide all Progression-granted effects, and the historical inventory's Hunter "NOT MIGRATED" row. No new invisible timed-buff engine is required.

| Effect class | Presentation and implementation |
| --- | --- |
| Permanent entitlement or conditional/environmental passive | No status icon, particles or active potion entry merely for owning the power. Use scoped attributes, permission/state, vision, direct healing or native event callbacks. |
| Finite triggered/activated buff, intentional trial or adversarial debuff | Ordinary native effect, normally visible with its remaining duration and subject to ordinary potion mechanics. Existing generic status handlers remain intact. |
| Consumable effect | Ordinary native effect, including Elixir and mutagen penalty; no global HUD suppression. |
| Cooldown/eligibility/internal counters | Internal state; not a potion. |

At dev/progression 71424cd5cdf52de0bbe420d9f95325ee21e4f10b, permanent Hunter mutations are already derived from saved state; the legacy Elytra marker is only retained for cleanup. Native getEffect may expose a detached nominal 240-tick query view; do not treat it as the saved mutation or a true remaining duration. Ordinary vanilla blacklist and explicit modded whitelist cover normal potion API attributes, periodic callbacks and queries only; pack authors must verify opt-ins. No custom provider adapters are promised. Temporary/permanent stacking is acceptable for now; preserve existing stronger-effect handling and track excess overlap in #271 for a later balance pass. Hunter-blood mutation extraction is a possible future design, not a committed mechanic.

Current runtime inventory: MechanicalEffects.status (hit/hurt/threshold/pulse, native visible icon), quarry Glowing, Lich rescue Resistance, Elixir buffs, intentional Monk Blindness and target Wither, and mutagen Wither penalty are legitimate finite statuses. No active self-pulse Path definition requires a new invisible continuous-power adapter. Existing delayed regeneration and permanent attributes already bypass the potion map. No gameplay mechanics changed in this follow-up.

A regression: extend scripts/verify_invisible_powers.py to check both sides of the contract, and trigger its read-only workflow for the relevant runtime/whitelist/mixin edits. This is static evidence, not a runtime or exhaustive compatibility proof. B/C after authorized inclusion: compare permanent mutation vs ordinary timed potion (icon and actual duration, expiry, cleanse, retained permanent state), trigger native finite effects on eligible players/targets, check local and multiplayer flight/vision and skill removal, and record any overlap imbalance rather than rejecting it by default. No extra timed-buff engine, no dedicated extraction implementation, no full build.

## Core contract (implemented)

1. `PassivePowers.register(powerId, sourceResolver)` registers a namespaced power with the **current** native Progression/Path state as its only authority. Providers return a set of source IDs; `PassivePowerGrants` is immutable and allows multiple independent sources for the same power. No Path-specific second wallet, saved ability flag, new button, equipment or vanilla status effect.
2. A server-only `PassivePowers.active(ServerPlayer,id)` **re-derives** authorization when the native glide hook is queried. The cached snapshot is a presentation-only optimization, never a permission source.
3. Server `PassivePowerNetwork.Snapshot` sends only bounded, sorted active power IDs to its own client. The client-owned projection checks **local-player identity + dimension** and expires 40 ticks after receipt; server refreshes every 20 ticks while connected and immediately on changed grants. No client `grant`/revoke API or custom user-submitted flag.
4. Current pilot `tyr_progression:elytra_glide`: Newbie baseline grants one source; a graduated Monk with current `momentum_glide` skill and toggle ON grants the other. A true ordinary Elytra or another compatible provider remains untouched because TMM only adds `true` when permitted in the preexisting NeoForge `canElytraFly` / `elytraFlightTick` hooks. The gentle existing Monk horizontal/small-lift acceleration is deliberately unchanged; never creative `mayfly`, wings/armor item, no float/hover.
5. `virtual_elytra` remains registered ONLY so legacy player saves can decode it. Every server reconciliation removes the old MobEffect, and NOTHING here ever grants another instance. This is transitional data compatibility, not a new potion-backed power.

### Transition/lifecycle wiring (source)

- **Login:** `NewbieMode.onLogin` repairs first-class baseline and pushes the correct projection, removing stale effect.
- **Personal graduation / operator Newbie restore:** `NewbieMode.set` immediately forces recalculation and sync, preserving Newbie-specific restrictions. No MXP/skill ledger change from the flight implementation.
- **Monk toggle:** `ClassAbilities.toggle("glide")` immediately forces sync after successful mode commit. While enabled and unlocked, ordinary Elytra physics apply; boost still follows existing rank.
- **Skill buy/respec in Progression GUI:** after server action, effect eligibility is reconciled and flight grant resent. General Path command/other-mod changes are caught by next per-player tick.
- **Respawn/dimension:** per-player transmission cache is cleared and power recomputed for the new player/dimension; old client projection is isolated by entity identity and dimension.
- **Logout:** cache is dropped. Client's weak owner ref and 40-tick lease cannot carry an entitlement to the next player session.
- **Creative/Spectator/dead:** no Progression-granted Elytra entitlement. Real creative physics are not changed.

## Source inventory for remaining #532 migration (do not indiscriminately remove potions)

| Runtime | Present source behavior | This PR | Next-code decision |
| --- | --- | --- | --- |
| `VirtualElytra` | Former `MobEffectInstance` every 180 ticks, missing texture despite `visible=false` | **REPLACED** with passive grant and server/client projection; legacy saved instance cleaned | Actual GUI flight QA |
| `EffectRuntime` | Already applies/revokes transient `AttributeModifier` by deterministic ID | **PRESERVED** (already HUD-invisible numerical attribute system) | Reconcile Path precedence and attribute lifetime tests |
| `VisionRuntime`/`ClientVision` | Already sends server-owned vision snapshot; checks vanilla Blindness/Darkness as real external effects | **PRESERVED** | Optionally migrate to central power registry if its visual packet requirements can be retained |
| `ClassAbilities.blindness_ordeal` | Applies brief real Blindness to carry out the intentional no-vision trial | **PRESERVED** | Trial condition is a real effect, not an invisible passive buff |
| `HunterMutations` | Drinks real mutated potions, records unlocked mutation tiers, reapplies selected potion buffs every 20 ticks | **NOT MIGRATED** | Separate source-backed buff/attribute/conditional conversion; retain real potion ingestion/penalties and mutant hunger tax |
| `MechanicalEffects` | Sometimes applies native harmful MobEffect/Glowing to a **target enemy** | **PRESERVED** | Adversarial effect is intentionally visible, not a player-owned passive permission |
| `UtilityEffects.immunity` | Filters incoming external native MobEffect applications based on purchased rank | **PRESERVED** | This is already an invisible conditional handler, not a granted icon |
| `ProgressionEvents`/combat | Native permission/trigger hooks and player tick reconciliation | **PRESERVED/EXTENDED** | Convert remaining HUD-backed positive class effects deliberately, not all status effects globally |

**Origins / JDT references:** NeoOrigins/Origins native equipment-free Elytra powers demonstrate the desired player-state route, while Just Dire Things' `upgrade_elytra` demonstrates an equipment-owned native glide upgrade. Neither mod was imported and there is no dependency. The existing TMM NeoForge item-stack flight extension remains intentionally additive to native Iron's Angel Wings, Ars Glide, real Elytra gear, and other providers.

## Acceptance states

**A / verified by module source CI:** immutable source-grant semantics; deterministic bounded wire projection; pure 40-tick lease fail-closed; Newbie vs Monk toggle/respec policy; existing `canElytraFly` / `elytraFlightTick` preserved and no MobEffect grant in `VirtualElytra`. Gradle module compilation and JUnit source test are **not proof of in-game flight**.

**B/C — NOT RUN, awaiting separately owner-authorized disposable build:**

- Newbie with empty/armored chest, real double-jump flight and vanilla Elytra physics, **zero** status icon/particles/broken texture; verify status list (including a save from the old effect version).
- Per-player personal Intro graduation immediately ends the *virtual* grant, not real elytra/Ars/Iron's power. Admin restoring Newbie is appropriately privileged; no free MXP.
- Earn Monk Momentum Glide, toggle ON/OFF, respec/out-of-Path, death/revive, dimension transition and log out/in. Ensure source retention does not leak after logout and client sync doesn't grant another player.
- Preserve weak modest forward boost/slow altitude gain at level pitch, stall on steep ascent, never hover/creative, ordinary rocket behavior and resource-limited effects.
- Native chest Elytra, Iron's Angel Wings, Ars Glide spell, multiplayer remote pose, spectator/creative, dedicated-server classloading. Recheck client after 1s heartbeat and after a deliberately dropped packet. No new mod or version is required.

**Integration ownership:** designated Progression writer reviews PR #616 into `dev/progression`; integrator later promotes frozen source to `test/full-integration`. This is not an authorization to assemble, run an in-game test, or package a Prism build.

## Nov 24 — Hunter mutation migration

Permanent vanilla mutation effects now derive from saved MutationLedger, use separate transient named attribute modifiers, native conditional queries and regeneration cadence; no effect refresh or active-effect entry. The shared passive snapshot also carries bounded effect-ID/amplifier pairs. Ordinary status instances remain native and visible, and stronger temporary potency wins; weaker native attribute contributions are compensated rather than stacking. Legacy 240-tick refreshes expire naturally to avoid deleting indistinguishable legitimate potions. The supported vanilla set is explicitly audited in MutationPowerPolicy. Unreviewed modded mutation IDs retain their saved progress but are dormant and cannot newly consume brews until a safe adapter exists; do not silently emulate capability effects. Non-Hunter Wither penalty and original recipes, duration threshold, and hunger cost are unchanged. Runtime check required for invisibility flag ownership, health boost, resistance, night vision, client/server lease, ordinary potion overlap and absent/unsupported providers. Network version changes require matching client/server module. #629/#654 remain separate integration boundaries.

### Nov 24 owner clarification — configurable mutation policy (supersedes fixed vanilla adapter admission)

Pack builders may configure the Path's `hunter_mutations` object with `vanilla_effect_blacklist` (exact minecraft registry IDs) and `modded_effect_whitelist` (exact non-minecraft registry IDs, defaults empty). The existing `effect_blacklist` is accepted as a backwards-compatible alias, including historical non-vanilla exclusions; deny wins. Supply only one blacklist field. Existing TMM blacklist remains unchanged; all modded effects excluded by default. Instantaneous effects remain ineligible and the native brewing-stand source gate still applies. Removing an allowed effect from policy suspends the saved mutation and its hunger tax without losing progress; re-enabling restores it. No runtime code required for ordinary modded attributes/conditional queries/native periodic effects. Opt-in does not automatically reproduce provider-specific on-add/on-remove state transitions, hidden resource pools, unique lifecycle or client effects; pack builders must audit those before whitelisting. Generic native periodic callback pauses while an actual potion of that type is active to avoid duplicate ticking; regeneration retains explicit strongest-wins behavior. Snapshot version 3 accepts validated namespaced IDs, not a hardcoded fifteen-effect set. Matching client/server required.

Example (illustrative subset; preserve the shipped blacklist when adding entries):

```json
{
  "hunter_mutations": {
    "target_duration_ticks": 720000,
    "exhaustion_per_mutation_tick": 0.0005,
    "maximum_amplifier": 3,
    "vanilla_effect_blacklist": ["minecraft:absorption", "minecraft:saturation"],
    "modded_effect_whitelist": []
  }
}
```
