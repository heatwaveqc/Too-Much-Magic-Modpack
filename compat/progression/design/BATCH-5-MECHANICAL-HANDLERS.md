# Batch 5 — generic mechanical effects and vision
2026-11-18 — PR #347. Implements reusable source mechanisms, not tree assignments or gameplay acceptance. No Tyz source imported. No donor dependency or assets. Existing effect handlers and path definitions preserved.

## Mechanical effect interface
The existing PathJson skill effect record is used. Effects inherit path/skill/effect-time predicates and legacy equipment gates. Example **uninstalled** data:
```json
{"type":"magnet","per_rank":2,"target":"*","params":{"interval_ticks":5,"max_targets":16,"move_xp":false}}
```
```json
{"type":"status_on_hit","per_rank":0.08,"target":"#tmm_progression:undead","params":{"effect":"minecraft:slowness","duration_ticks":100,"amplifier":0,"cooldown_ticks":20,"delivery":"melee"}}
```
```json
{"type":"heal_on_kill","per_rank":0.03,"params":{"basis":"target_health","max_heal":20}}
```
```json
{"type":"lava_vision","per_rank":20,"params":{"default_enabled":true}}
```
All parameters in examples are illustrative, not approved tree balance.

| Type | Meaning and parameters |
|---|---|
| magnet | Blocks per rank, capped at 32; item ID/tag target; interval, max_targets, move_xp. Uses attributed EverlastingAbilities MagnetMotion; no teleport, no pickup-delay clearing; respects vanilla reservation and trusted claim veto adapters. Crouch disables. |
| status_pulse | Self, hostile or allied nonplayer scope, radius per rank capped at 32; effect registry ID, amplifier 0–9, duration, interval, max_targets; uses short-lived normal potion behavior, skips instantaneous/stronger existing effects. |
| status_on_hit | Chance per rank capped at 1, optional chance multiplier/cooldown, delivery, target and weapon, damage_tag, duration and amplifier; actual positive post-damage event. |
| status_on_hurt | Chance per rank, self/attacker recipient, target selector, delivery, damage_tag, effect and cooldown; positive post-damage, nonplayer nonallied living attackers only. |
| heal_on_kill | Fraction of target maximum HP per rank or flat HP per rank, max_heal, chance/cooldown, target/weapon/delivery/damage_tag; player-attributed death event. |
| heal_on_hit | Fraction of post-damage or target max HP or flat HP, same caps and filters; only positive post-damage. |
| critical_hit | Chance per rank, chance multiplier, damage multiplier, cooldown, target/weapon/delivery/damage_tag; pre-damage event. |
| damage_at_low_health | Additive bonus per rank expressed as a multiplier over 1; owner's health fraction threshold, delivery, target/weapon/damage_tag. Combined critical/low-health multiplier bounded to 10. |
| enhanced_night_vision | Separate server-authorized toggle, default_enabled, full lightmap brightness without a potion. JDT attribution and optional MixinExtras wrapper. |
| lava_vision | Blocks per rank capped at 128; Create fog hook, default_enabled toggle, no fire immunity or fluid physics change. |
| attribute existing handler | Optional max_contribution limits only this positive Progression modifier; no global attribute suppression or interference with other mods. Reconcile removed and changed attribute IDs. |

Intervals round up to multiples of five ticks; four due area operations and 32 processed targets at most per five-tick player pulse. Bounded entity result lists, round-robin due ordering; no global entity scan. The area cap is per player, not a universal server bound. No arbitrary PvP targeting, no invulnerability/instant spells, no untrusted client-provided effects or extra XP/loot. Status durations and hit cooldowns are normal game effects/transient server state, not guaranteed immediate revocation; current cooldowns reset on logout/respawn. Critical rolls occur before final damage, so a subsequently prevented hit may consume its cooldown. Claim systems need specific guard integrations before using magnet on protected multiplayer servers. Magnet moves entities physically; vanilla pickup/competition still applies, optional XP orbs are unreserved. No generic harmful aura claim guard yet.

## Added eligibility types
- health_fraction (0..1), food_level (0..20): comparison and value.
- time_window: start/end 0..23999, wraps midnight.
- weather: value clear/rain/thunder (local precipitation).
- player_state: state sneaking/sprinting/underwater/in_lava/on_ground/sky_visible/empty_hand/unarmored, optional boolean value.
- equipped: slot mainhand/offhand/head/chest/legs/feet/armor/any, target ID/tag or wildcard (nonempty items only).
- skill_rank: path, id, minimum; milestone: path, id.
- Existing unavailable-provider semantics preserved for absent item tags/IDs and skill/milestone definitions.

## Vision contract
Server computes granted vision from eligible active effects and persisted preferences; one-way payload, bounded range, fresh local-player-instance lease expires after 40 client ticks. Independently rebindable keys default unbound. Default enabled is data; no skill definitions assigned. Blindness and Darkness take precedence over these custom render effects. Night wrapper only answers the NIGHT_VISION query; vanilla disabled behavior retained. Optional wrapper may fail to apply with conflicting shaders/mixins; client QA must confirm both injection sites. Lava fog only when local camera under lava surface; screen overlay/shaders still require tests. No Create or JDT install.

## Verification
- b95b7413b57560970ab58400499e6aa6bae79696 prior batch follow-up passed three workflows (44384032582, 44384032576, 44384032637).
- 63606ac1873198d3ae9ac57dc1fabef59a30159c mechanical slice: Verify bootstrap 44385115038, standalone core 44385115113, JAWR/MXP contract 44385115171 all succeeded.
- 502bb08bd84f12357927a2dfbabfe49e7e676948 vision slice: Verify bootstrap 44385448419, standalone core 44385448625, JAWR/MXP contract 44385448726 all succeeded.
- 9dbc91600b729db752ab380bda10dc51ab520ad8 contextual predicates/reactive status follow-up: check CI on final head; new pure predicate tests added.
- No game boot, client render test, pack assembly, dependency changes, merge or live-test readiness declaration. Existing tree configs have no new nodes; QA fixtures are parser tests only.

## Next
Batch 5 is a substantial foundation, not complete stock-skill parity. Remaining mechanical primitives: food efficiency, vanilla XP multiplier, broader bounded/protected area effects, resource/crafting/loot hooks with ProjectE duplication audits; actual whole-attribute suppression only with explicit scoped policy; richer event targeting, status immunity, movement/flight variants and persistence/cooldown policy. Generic mechanic coverage should be mapped against Tyz/EverlastingAbilities/Apotheosis capabilities without importing restricted code. Batch 6 magic/form adapters can start once current source changes are reviewed. Do not activate speculative effects just to fill the tree. See IMPLEMENTATION-ROADMAP.md.

## Superseding follow-up
See [BATCH-5-6-FOLLOWUP.md](BATCH-5-6-FOLLOWUP.md): food, vanilla XP, scoped drops/virtual loot, crafting signal, immunity, delayed regen, hunger, jump/fall, persistent proc cooldown and area guard added. Prior contextual slice CI passed. Residual gaps explicitly tracked.
