# Standalone player-enhancement progression core — early implementation

**Status:** compiling/development chassis only; not activated as an integrated modpack JAR or declared gameplay parity. **Target:** Minecraft 1.21.1, NeoForge 21.1.265, Java 21. Namespace: `tmm_progression`.

## Goal and ownership

This is a **reusable engine** for data-defined player disciplines such as Monk and Hunter, not a new version of the Vampirism-based Monk pilot and not a Tyz skill wallet. A discipline should primarily be a datapack path plus **occasional reusable Java effect handlers** rather than handwritten progression code. No mandatory Monk, Hunter, Vampirism, Werewolves, JAWR, Curios or Tyz dependency. The pack-global MXP/Tyz wallet remains separate.

**Monk classic parity remains governed by** [standalone design PR #100](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/100). This pilot intentionally DOES NOT implement all 25 classic powers. All 25 must eventually be attainable by one Monk natively without Tyz points; post-dragon Momentum Gliding replaces original Creative Flight. The earlier `compat/monk` prototype is not integrated into this module.

**Hunter creature taxonomy** is researched independently under [PR #97](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/97), so the tags supplied here are explicitly *pilot vanilla fixtures*, not the authoritative pack-wide taxonomy.

## Config / datapack interface

Create `data/<your_namespace>/tmm_progression/paths/<path>.json`. See both sample files in `src/main/resources/data/tmm_progression/tmm_progression/paths/`: `monk.json` and `hunter.json`. JSON datapacks reload on `/reload`.

| Key | Meaning |
| --- | --- |
| `schema: 1` | Strict format version; invalid JSON/policy errors reject the path |
| `exclusive_group` | Optional mutually exclusive path set; external transformations still need their own arbitration |
| `rank_xp` | Cumulative **native progression XP** thresholds beginning at 0; configurable rate and curve |
| `max_level` | Optional explicit level cap (1..200); must equal number of entries in `rank_xp` |
| `points_per_rank` | Native optional technique points; **not Tyz MXP** |
| `activities` | `event`, `target` ID/#tag, `xp`, `cooldown_ticks`; XP is credited only on trusted server events |
| `milestones` | `id`, AND-joined `criteria` (event + target + count), optional milestone IDs in `after`, one-time `xp` and `points` |
| `skills` | `id`, `max_rank`, `required_rank`, `base_cost`, `step_cost`, `requires_skills` (all), `requires_any_skills` (one), `choice_group` (mutually exclusive), optional milestone prerequisites and `effects` |
| `auto_ranks` | For mandatory milestone/rank-granted powers; rank thresholds at which effect ranks automatically increase **for free** |
| `effects` | Native effect type with optional `target`, `weapon`, `damage_tag`, `attribute`, `operation`, `per_rank`, `tier`, `empty_hand`, `unarmored`, `params` |

Native activity/criterion event types: `kill`, `block_break`, `external`, `food_consumed`, `food_discovered` (one-time per player by item registry ID), and `animal_petted` (only successful server-side Animal Friendship interactions). Selectors are exact IDs, `#` registry tags, or `*` (any target of that event type). Use `food_discovered` with `*` to award mastery for any edible registered item without one-off tags. Rewards are **custom path XP and path points only**, never Minecraft XP or Tyz MXP. First Sunset and other cross-mod achievements can be added by a trusted adapter calling `ProgressionApi.recordExternal(serverPlayer, "namespace:event")`. That API **does not join the path** and awards only a configured milestone; `ProgressionApi.join(player, pathId)` must be called by a verified enrollment ritual. The only current in-game path join is an OP command for QA.

### Built-in effect handlers

| Type | Gameplay |
| --- | --- |
| `attribute` | Stable, reversible attribute modifier; `per_rank` and `operation`; resource-location attribute |
| `melee_bonus` | Added melee damage matching target entity ID/tag and optional held weapon ID/tag; `empty_hand` is supported |
| `damage_bonus` | Target + weapon ID/tag matching for direct melee and arrow hits; `params.delivery` = `melee`, `arrow`, `any`. Arrows inspect the original firing bow/crossbow; no magic projectile guessing |
| `damage_multiplier` | Damage reduction by source type/tag, with rank-scaled fraction; capped at zero damage |
| `break_speed` | Multiplies speed on matching block ID/tag; allows unarmed mining condition |
| `harvest_tier` | Effective vanilla tier 0..3, limited by attained rank; can make bare hands harvest higher tiers |
| `gear_durability` | Periodic native wear (`params.action:damage`) or repair (`repair`) on equipment `slot` mainhand/offhand/head/chest/legs/feet/armor/all; `target` item tag/ID; `interval_ticks` 1..72000, `per_rank` 0..64 |
| `animal_friendship` | Empty-main-hand adult Animal love via server-side interaction; targets entity ID/tag or `*`, awards `animal_petted` when accepted |

Supported generic conditions: `empty_hand`, `unarmored`. Default values mean no restriction. Tag selectors start with `#` and are resolved from live server registries.

**New unusual power?** Register once in Java: `EffectHandlers.register("yourmod:blink", handler)`, with implementation of the `Handler` interface. The datapack can then refer to `{"type":"yourmod:blink","params":{"range":"16"}}` under any skill. Server hooks offered: `tick`, `incoming`, `harvest`, `breakSpeed`, `blockBreak` (pre-break), `kill`, `foodConsumed`, `entityInteract`. The latter two allow novel-food and per-animal effects without hardcoding Monk/Hunter IDs. Registration must be performed by trusted Java before datapack reload; arbitrary scripts in JSON are not executed.

### Persistent player ledger

Saved per player: path enrollment, rank, cumulative XP, native technique points, purchased and automatically granted skill ranks, completed milestones, criterion counts. A **separate copy-on-death food-history attachment** saves first-consumed item IDs without changing the previous path-ledger serialized layout. Native attachment is copy-on-death; transient attribute modifiers are reconciled, rather than permanently stamped. Invalid reload definitions do not deliberately delete stored progress. There is **no player GUI or client sync protocol yet**; command output is server-side.

### QA commands

```text
/tmmprogress list
/tmmprogress status tmm_progression:monk
/tmmprogress buy tmm_progression:hunter tmm_progression:undead_slayer
/tmmprogress respec tmm_progression:hunter
/tmmprogress admin join tmm_progression:monk        # OP-only
/tmmprogress admin event tmm_progression:monk_first_sunset  # OP-only
```

To run the module from this directory with Gradle: `gradle --no-daemon build` then `gradle runServer` or `gradle runClient`. GitHub Actions workflow `progression-core.yml` checks compilation/JUnit on pushes and PRs; check the latest run on the PR before treating this revision as passing.

## Incomplete / guarded behavior

1. **Not full Monk or Hunter:** Monk first-sunset initiation, 15+ classic mechanics beyond this pilot, extraordinary custom effects (armor lock, fist slot, Fortune/Silk, water walking, leap/blink, arrow catch, Momentum Glide, animal charm, Withering Stare, etc.), and Hunter specialized tracking and taxonomy integration are outstanding.
2. **Tag semantics:** Pilot `tmm_progression:` tags cover a handful of vanilla entities/tools only. No claims about hundreds of modded mobs; consult Hunter taxonomy research. Do not assume external mods' tags exist.
3. **XP legitimacy:** `BlockEvent.BreakEvent` is a pre-break event; canceled events are ignored but other systems may subsequently veto a break. TreeChop/mass-break and automation attribution need audited post-commit bridges. Kill ownership currently credits the immediate source player; summon ownership/spell credit needs adapters. Activity cooldowns are process-local.
4. **Equipment and compatibility:** Armor checks use four vanilla armor slots, not Curios. Mining tiers currently implement vanilla stone/iron/diamond checks and not every modded tool tier. Event interactions with other combat modifiers require dedicated integration testing.
5. **UI, migration and safety:** No custom HUD/skill tree GUI, client-sync, versioned progress migration, path-leave/cure/reset, configured respec price, anti-farm event dedupe, effect-handler hot reload/registration timing tests, or pack-manifest inclusion. No in-game QA has passed.
6. **Lifecycle:** Unregistering/removing a datapack path retains its saved ledger but cannot apply or respec effects until the definition returns. Effects have transient IDs; login/logout/reset consistency must pass tests.
7. **Security:** Never expose `join`, `recordExternal` or arbitrary XP awards to untrusted client packets. The integration adapter must validate true initiation/trial completion server-side.

## Configured mini-examples (not complete classes)

```json
{
  "schema": 1,
  "max_level": 3,
  "rank_xp": [0, 25, 75],
  "points_per_rank": 2,
  "activities": [
    {"event":"kill","target":"#tmm_progression:undead","xp":5,"cooldown_ticks":0},
    {"event":"food_discovered","target":"*","xp":8,"cooldown_ticks":0}
  ],
  "skills": [
    {"id":"example:sword","required_rank":2,"choice_group":"example:stance",
     "effects":[{"type":"damage_bonus","weapon":"#tmm_progression:swords",
                 "target":"#tmm_progression:undead","per_rank":1,
                 "params":{"delivery":"melee"}}]},
    {"id":"example:bow","required_rank":2,"choice_group":"example:stance",
     "effects":[{"type":"damage_bonus","weapon":"#tmm_progression:ranged_weapons",
                 "target":"#tmm_progression:undead","per_rank":1,
                 "params":{"delivery":"arrow"}}]}
  ]
}
```

## Acceptance gates before merging into test/full-integration

- [ ] GitHub CI gradle build and all JUnit tests pass
- [ ] Dedicated-server create/save/respawn/dimension/logout/rejoin test, including negative `/reload`
- [ ] Monk First Sunset native enrollment; per-player saved automatic baseline grants
- [ ] Hunter enrollment/transform exclusion; target-specific weapon damage and XP validation
- [ ] Damage/defense modifier coexistence with magic and modded equipment
- [ ] Harvest check and break speed with bare hands across vanilla and installed modded tiers
- [ ] Core effect handler contract for remaining classic powers, gameplay implementations in Monk/Hunter layer
- [ ] No unexpected XP from creative, spectators, canceled/non-player block breaks or kill credit
- [ ] Later migration and GUI plan; only then integrate JAR and dependent paths into pack manifests

This is **an implementation skeleton and functional pilot**, not release-ready Monk/Hunter or a verified build.
