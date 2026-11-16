# Standalone player-enhancement progression core — early implementation

**Status:** source-level implementation pilot; not activated as an integrated modpack JAR, no in-world parity or multiplayer/claim validation. Building and Java unit tests are necessary but not sufficient. **Target:** Minecraft 1.21.1, NeoForge 21.1.265, Java 21. Namespace: `tmm_progression`.

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
| `enrollment` | `admin` (QA/default), `manual` (user joins with command), or `sunset` (Monk-only First Sunset). Trusted adapters may call `ProgressionApi.join` after validating their own rituals |
| `rank_xp` | Cumulative **native progression XP** thresholds beginning at 0; configurable rate and curve |
| `max_level` | Optional explicit level cap (1..200); must equal number of entries in `rank_xp` |
| `points_per_rank` | Native optional technique points; **not Tyz MXP** |
| `activities` | `event`, `target` ID/#tag, `xp`, `cooldown_ticks`; XP is credited only on trusted server events |
| `milestones` | `id`, AND-joined `criteria` (event + target + count), optional milestone IDs in `after`, one-time `xp` and `points` |
| `skills` | `id`, `max_rank`, `required_rank`, `base_cost`, `step_cost`, `requires_skills` (all), `requires_any_skills` (one), `choice_group` (mutually exclusive), optional milestone prerequisites and `effects` |
| `auto_ranks` | For mandatory milestone/rank-granted powers; rank thresholds at which effect ranks automatically increase **for free** |
| `effects` | Native effect type with optional `target`, `weapon`, `damage_tag`, `attribute`, `operation`, `per_rank`, `tier`, `empty_hand`, `unarmored`, `params` |

Native activity/criterion event types: `kill`, `block_break`, `external`, `food_consumed`, `food_discovered` (one-time per player by item registry ID), `animal_petted` (only successful server-side Animal Friendship interactions), and `advancement` (earned AdvancementHolder ID). Selectors are exact IDs, `#` registry tags, or `*` (any target of that event type). Use `food_discovered` with `*` to award mastery for any edible registered item without one-off tags. Rewards are **custom path XP and path points only**, never Minecraft XP or Tyz MXP. First Sunset and other cross-mod achievements can be added by a trusted adapter calling `ProgressionApi.recordExternal(serverPlayer, "namespace:event")`. That API **does not join the path** and awards only a configured milestone; `ProgressionApi.join(player, pathId)` must be called by a verified enrollment ritual. Native non-OP joins: `/tmmprogress enroll tmm_progression:hunter` uses the Hunter's JSON `enrollment: manual`, while Monk `enrollment: sunset` requires 200 continuous valid, unarmored, empty-handed westward-looking sky-visible sunset ticks. OP join remains QA-only.

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
| `blink`, `catch_arrows` | Server-only safe-ray aimed hop and frontal eligible arrow catch, unlocked by auto-ranks |
| `armor_lock`, `reserved_fist` | Armor Lock defaults ON: removable vanilla armor is safely **dropped into the world**, even with a full inventory; Curse of Binding armor remains equipped and takes ordinary wear every 20 server ticks while ON. Monk combat/utility powers carry `unarmored: true`, except Armor Lock and reserved-slot management, which stay available while armored. The fist slot allows manual player placement but is excluded by a targeted Inventory Mixin from vanilla auto-insert free-slot/stack-merge and direct `Inventory.add(0,...)` |
| `glass_touch`, `treasure_hand` | Post-break empty-hand glass drops and Fortune I/Silk Touch I loot replacement with persistent manual toggle; no block entity substitution |
| `water_breathing`, `water_walk`, `momentum_glide` | Air supply stages; crouch escape for water-surface buoyancy; post-Dragon limited aerial descent, never Creative Flight |
| `hunger_discipline`, `monk_leap`, `purification`, `withering_stare`, `creeper_peace` | Native hunger reduction, jump/fall adaptation, normal harmful-effect cleanse, anti-undead gaze and mob-specific pacification |
| `faction_neutrality`, `blindness_ordeal` | Target change suppression with retaliation guard; optional blinded challenge with reset/clear state |

Supported generic conditions: `empty_hand`, `unarmored`. Default values mean no restriction. Tag selectors start with `#` and are resolved from live server registries.

**New unusual power?** Register once in Java: `EffectHandlers.register("yourmod:blink", handler)`, with implementation of the `Handler` interface. The datapack can then refer to `{"type":"yourmod:blink","params":{"range":"16"}}` under any skill. Server hooks offered: `tick`, `incoming`, `harvest`, `breakSpeed`, `blockBreak` (pre-break), `kill`, `foodConsumed`, `entityInteract`, `blockDrops` (loot), `projectileImpact` (arrow interception), `equipmentChange`, `jump` and `targetChange` (neutrality/aggro). These hooks allow custom abilities without hardcoding Monk/Hunter IDs in the generic event dispatcher. Registration must be performed by trusted Java before datapack reload; arbitrary scripts in JSON are not executed.

### Persistent player ledger

Saved per player: path enrollment, rank, cumulative XP, native technique points, purchased and automatically granted skill ranks, completed milestones, criterion counts. A **separate copy-on-death food-history attachment** saves first-consumed item IDs without changing the previous path-ledger serialized layout. Another persistent mode attachment and server-only `ProgressionApi.mode/setMode` hold validated namespaced switches used by Armor Lock, Fortune/Silk, Blink cooldown, glide and ordeal. A one-way validated serverbound ability action packet and optional rebindable client keys exist; no full bidirectional skill-tree state-sync protocol or visual GUI yet. Native attachment is copy-on-death; transient attribute modifiers are reconciled, rather than permanently stamped. Invalid reload definitions do not deliberately delete stored progress. There is **no player skill-tree GUI or client status sync protocol yet**; actions have keybinds and server validation, but progression status still comes from commands.

### QA commands

```text
/tmmprogress list
/tmmprogress status tmm_progression:monk
/tmmprogress buy tmm_progression:hunter tmm_progression:undead_slayer
/tmmprogress respec tmm_progression:hunter
/tmmprogress admin join tmm_progression:monk        # OP-only
/tmmprogress admin xp tmm_progression:monk 28000  # OP-only: grant native XP; level 20 from 0
/tmmprogress admin xp tmm_progression:hunter 1000  # OP-only: test Hunter leveling
/tmmprogress admin event tmm_progression:monk_first_sunset  # OP-only
/tmmprogress enroll tmm_progression:hunter         # Player-controlled manual join
/tmmprogress power blink                          # Blink; also V key
/tmmprogress power armor                          # Toggle Armor Lock
/tmmprogress power treasure                       # Toggle Fortune/Silk hand mode
/tmmprogress power glide                          # Toggle Momentum Glide after Dragon
/tmmprogress power ordeal                         # Start/abort blindness ordeal
```

To run the module from this directory with Gradle: `gradle --no-daemon build` then `gradle runServer` or `gradle runClient`. GitHub Actions `progression-core.yml` checks Java/JUnit, boots a disposable dedicated server, and requires both Monk/Hunter datapacks to load; check the most recent branch CI run.

## Incomplete / guarded behavior

1. **Not full Monk or Hunter:** Many powers now have early Java handlers, not in-world verified parity. High-risk gaps remain: direct-slot death/grave restoration and modded inventory-container edge cases, actual no-dupe treasure drops, water physics and Momentum Glide client prediction, Wither boss gaze exception, distinct Lava Protection, advanced Animal calming, class-specific GUI and Hunter tracking/camouflage/execute. None of these should be described as complete.
2. **Tag semantics:** Pilot `tmm_progression:` tags cover a handful of vanilla entities/tools only. No claims about hundreds of modded mobs; consult Hunter taxonomy research. Do not assume external mods' tags exist.
3. **XP legitimacy:** Raw `BlockEvent.BreakEvent` remains a pre-break hook but no longer awards path XP; loot-post-commit `BlockDropsEvent` now awards XP. TreeChop/mass-break, claim-protection rules and automated block destruction still need verified adapters and anti-farm tests. Kill ownership currently credits the immediate source player; summon ownership/spell credit needs adapters. Activity cooldowns are process-local.
4. **Equipment and compatibility:** Armor checks use four vanilla armor slots, not Curios. Mining tiers currently implement vanilla stone/iron/diamond checks and not every modded tool tier. Event interactions with other combat modifiers require dedicated integration testing.
5. **UI, migration and safety:** Serverbound action keybinds exist but no visual skill-tree GUI or server-to-client progress sync, versioned progress migration, path-leave/cure/reset, configured respec price, anti-farm event dedupe, effect-handler hot reload/registration timing tests, or pack-manifest inclusion. No in-game QA has passed.
6. **Lifecycle:** Unregistering/removing a datapack path retains its saved ledger but cannot apply or respec effects until the definition returns. Effects have transient IDs; login/logout/reset consistency must pass tests.
7. **Security:** Never expose `join`, `recordExternal` or arbitrary XP awards to untrusted client packets. The integration adapter must validate true initiation/trial completion server-side.

## Testing native progression without grinding

Use `/tmmprogress admin xp <path> <amount>` as an operator (permission level 2).
This awards **that path's custom progression XP**, not vanilla XP, Tyz XP or skill points
directly. The normal `rank_xp` thresholds, level caps, automatic rank-granted abilities
and points-per-level apply, including large one-time grants. The path must already be
joined, so for a brand-new test character first run `/tmmprogress admin join
tmm_progression:monk`. A grant does **not** complete non-XP milestones (First Sunset,
Dragon defeat, blind ordeal) and deliberately does not unlock optional paid skills.

```mcfunction
/tmmprogress admin join tmm_progression:monk
/tmmprogress admin xp tmm_progression:monk 28000
/tmmprogress status tmm_progression:monk
```

## Armor discipline and dedicated fist slot (current policy)

- **Wearing any vanilla armor suppresses all Monk powers**. Each Monk effect other than the always-available `armor_lock` and `reserved_fist` control effects has `unarmored:true`; the core eligible-effect hook handles the suppression. The lock toggle is separate: switching it OFF allows armor to remain equipped, **but does not reactivate Monk powers**.
- **Armor Lock ON** drops any removable equipped armor at the player's feet (including when inventory is full). It does not destroy equipment or force-remove Curse of Binding armor. Each damageable Binding piece instead takes one native durability-wear attempt every 20 server ticks while equipped and ON. OFF stops this wear. The toggle's default state is ON; reconciliation happens on equipment changes and at tick fallback.
- **Reserved hotbar slot 0** is **not forcibly cleared**: deliberate hand swaps, manual drag/drop and inventory placement are allowed. The `InventoryReservationMixin` skips index 0 in `Inventory#getFreeSlot` and `getSlotWithRemainingSpace`; an explicit `Inventory.add(0,...)` call reroutes through auto insertion. These are common insertion methods for drops, rewards and grave recovery. **Not a blanket interception of `Inventory#setItem(0,...)` or direct NBT/inventory-list mutation**; mods that restore into exact saved slots (including some Tombstone configurations) require a verified optional adapter. Never claim that path is solved without a real tombstone recovery test.
- Player action keybinds and class progression remain independent of the armor toggle. Curios do **not** count as the four vanilla armor slots.

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
