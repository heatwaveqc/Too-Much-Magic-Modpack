# Standalone player-enhancement progression core — early implementation

> **Locked architecture update (2026-11-18):** [Unified Progression engine target](design/UNIFIED-PROGRESSION-ENGINE-TARGET.md) supersedes the separate external MXP/Tyz architecture below as a development target. Absorb MXP and selected MIT-licensed Tyz functionality into `tmm_progression`, retain one universal shared MXP/SP wallet and distinct native mastery, ten configurable trees (one shared Lich tree), and all four reference-system capability goals plus enhanced night and lava vision. All first-party mods target MIT. Historical implementation descriptions below remain current-source evidence where applicable, not future architecture. No migration, dependency removal or live build has occurred.

> **JAWR-only Vampire/Werewolf form pilot (2026-11-16, PR #279):** The old standalone `compat/forms` executable module was NOT imported. Its 16 Vampire + 21 Werewolf Tyz source definitions and four provisional Werewolf gameplay handlers now belong to **this shared engine**, with [config-only catalog, preview generator, and tests](design/jawr-forms/catalog.json). Tyz prefabs remain **inactive by default** and are registered only when both Tyz and JAWR are present; JAWR state is checked again at purchase and effect time. Native milestone/cure/ability gaps remain fail-closed. MXP is the *separate global award ledger* (`compat/mxp`) feeding Tyz's one existing XP/SP wallet; it is not another form skill-point system. [Joint integration contract](../../docs/progression/JAWR-MXP-JOINT-CONTRACT-2026-11-16.md).


> **Legacy PR reconciliation (2026-11-16):** The old eight-PR Monk/Hunter development stack (#127, #136, #141, #143, #148, #138, #142, #144) is mapped to this single Progression engine in [the consolidation ledger](../../docs/progression/LEGACY-PR-STACK-CONSOLIDATION-2026-11-16.md). The [historical audits](../../docs/archive/progression-pr-stack/README.md) are records, not active runtime or acceptance evidence. New path branches are config-only; all executable changes belong here.

**Status:** source-level implementation pilot; not activated as an integrated modpack JAR, no in-world parity or multiplayer/claim validation. Building and Java unit tests are necessary but not sufficient. **Target:** Minecraft 1.21.1, NeoForge 21.1.265, Java 21. Namespace: `tmm_progression`.

## Goal and ownership

This is the **single executable Progression engine** for all data-defined player disciplines (Monk, Hunter, Gilded and later paths), not a separately coded Monk/Hunter mod or a Tyz skill wallet. **All runtime Java, mixins, event hooks, effect handlers, network code, integrations and behavior tests belong to Progression itself.** Each discipline is a **configuration-only package**: its path/datapack JSON, Tyz skill-tree/config JSON, tags/data assets, localization, docs and non-executable fixtures. Disciplines must not add their own Java classes, KubeJS/JavaScript/Python gameplay logic, or independent runtime modules. Reusable handlers added for any path are built and maintained by the shared Progression engine; their behavior is configured in data, not owned by that path. No mandatory Monk, Hunter, Vampirism, Werewolves, JAWR, Curios or Tyz dependency. The pack-global MXP/Tyz wallet remains separate in the current implementation only; the locked target consolidates it into this engine.

Development follows [the shared branch and path-ownership contract](../../docs/BRANCH-AND-PROGRESSION-WORKFLOW.md): core functionality goes through `feature/progression-*`, declarative class work through `feature/monk-*` / `feature/hunter-*` / `feature/gilded-*`, all merging into `dev/progression` before an authorized `test/full-integration` promotion. **Some existing code and this README's implementation examples predate that architecture; migration is pending and must not be misreported as complete.**

**Long-term engine expressiveness target (design only, not implemented):** Progression should eventually represent every player enhancement/progression mechanic family demonstrated by **Oritech cybernetic augmentation**, **Everlasting Abilities**, **Astral Sorcery attunement/perk points**, and **Apotheosis gems/affixes**, using reusable engine-owned effect primitives and data-only path definitions. This is neither a next-build requirement nor an instruction to replace installed mods. Read [the four-system capability parity target](design/REFERENCE-CAPABILITY-PARITY-TARGET.md) before extending the engine for new mechanics.

**Monk classic parity remains governed by** [standalone design PR #100](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/100). This pilot intentionally DOES NOT implement all 25 classic powers. All 25 must eventually be attainable by one Monk natively without Tyz points; post-dragon Momentum Gliding replaces original Creative Flight. The earlier `compat/monk` prototype is not integrated into this module.

**Hunter creature taxonomy** is researched independently under [PR #97](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/97), so the tags supplied here are explicitly *pilot vanilla fixtures*, not the authoritative pack-wide taxonomy.

## Config / datapack interface

Create `data/<your_namespace>/tmm_progression/paths/<path>.json`. See both sample files in `src/main/resources/data/tmm_progression/tmm_progression/paths/`: `monk.json` and `hunter.json`. JSON datapacks reload on `/reload`.

| Key | Meaning |
| --- | --- |
| `schema: 1` | Strict format version; invalid JSON/policy errors reject the path |
| `exclusive_group` | Optional mutually exclusive path set; external transformations still need their own arbitration |
| `enrollment` | `admin` (QA/default), `manual` (user joins with command), `sunset` (Monk-only First Sunset), or `potion` (Hunter Trial of Venom). Trusted adapters may call `ProgressionApi.join` after validating their own rituals |
| `rank_xp` | Cumulative **native progression XP** thresholds beginning at 0; configurable rate and curve |
| `max_level` | Optional explicit level cap (1..200); must equal number of entries in `rank_xp` |
| `points_per_rank` | Native optional technique points; **not Tyz MXP** |
| `activities` | `event`, `target` ID/#tag, `xp`, `cooldown_ticks`; XP is credited only on trusted server events |
| `milestones` | `id`, AND-joined `criteria` (event + target + count), optional milestone IDs in `after`, one-time `xp` and `points` |
| `skills` | `id`, `max_rank`, `required_rank`, `base_cost`, `step_cost`, `requires_skills` (all), `requires_any_skills` (one), `choice_group` (mutually exclusive), optional milestone prerequisites and `effects` |
| `auto_ranks` | For mandatory milestone/rank-granted powers; rank thresholds at which effect ranks automatically increase **for free** |
| `effects` | Native effect type with optional `target`, `weapon`, `damage_tag`, `attribute`, `operation`, `per_rank`, `tier`, `empty_hand`, `unarmored`, `params` |

Native activity/criterion event types: `kill`, `block_break`, `external`, `food_consumed`, `food_discovered` (one-time per player by item registry ID), `animal_petted` (only successful server-side Animal Friendship interactions), and `advancement` (earned AdvancementHolder ID). Selectors are exact IDs, `#` registry tags, or `*` (any target of that event type). Use `food_discovered` with `*` to award mastery for any edible registered item without one-off tags. Rewards are **custom path XP and path points only**, never Minecraft XP or Tyz MXP. First Sunset and other cross-mod achievements can be added by a trusted adapter calling `ProgressionApi.recordExternal(serverPlayer, "namespace:event")`. That API **does not join the path** and awards only a configured milestone; `ProgressionApi.join(player, pathId)` must be called by a verified enrollment ritual. Native non-OP joins: `/tmmprogress enroll <path>` works only for paths configured with `enrollment: manual`. The shipped Hunter instead uses the completed Poison Potion + Regeneration + Monster Hunter ritual; Monk requires 200 valid sunset ticks. OP join remains QA-only.

### Data-defined potion enrollment (Trial of Venom)

Any path may use `"enrollment": "potion"` plus a strictly validated `enrollment_trigger`. The example Hunter path now requires **Monster Hunter**, an active **Regeneration** status effect, and fully drinking a **vanilla Potion of Poison**, including extended or strong variants. Its trigger is:

```json
{
  "enrollment": "potion",
  "enrollment_trigger": {
    "item": "minecraft:potion",
    "potions": ["minecraft:poison", "minecraft:long_poison", "minecraft:strong_poison"],
    "required_effect": "minecraft:regeneration",
    "required_advancement": "minecraft:adventure/kill_a_mob",
    "clear_effects": ["minecraft:poison"],
    "completion_event": "tmm_progression:hunter_trial_of_venom"
  }
}
```

The **logical server** evaluates `LivingEntityUseItemEvent.Finish` and the pre-consumption ItemStack's exact `DataComponents.POTION_CONTENTS` base-potion ID. Splash/lingering applications, interrupted drinks, food, arbitrary Poison status, custom-effect potions, creative/spectator and missing Advancement/Regeneration do not enroll. A successful `ProgressionApi.join` checks existing path exclusivity and registered `registerJoinVeto` callbacks before clearing Poison and recording the one-time external completion milestone.

Hunter and Monk share `exclusive_group: "tmm_progression:human_disciplines"`, preventing dual enrollment. Other form providers (JAWR, vampire/werewolf/dragon, etc.) must register **trusted** joining vetoes with `ProgressionApi.registerJoinVeto((player,pathId) -> ...)`. The core intentionally does not depend on their classes. The potion hook alone is **not proof** that all third-party transformations have been checked; the pack's species arbitration adapter remains required before release. Administrative QA join still bypasses the *ritual*, but not registered join vetoes.

This milestone records initiation only. **Poison immunity, shortened harmful-effect durations and involuntary transformation prevention are separate Hunter abilities and provider adapters not implemented here.** Removing the trial's immediate Poison effect on successful enrollment should not be presented as permanent immunity. A rejected path consumes the potion normally; no item is restored or duplicated.

### Hunter specialty and computed XP

The shipped Hunter path now offers 14 currently active mutually exclusive, zero-point creature-family choices (Fae and Aberration remain taxonomy placeholders without eligible prey) at rank 1. `/tmmprogress specialty list` displays valid families; one chosen tag doubles XP from a qualifying creature. Multi-family creatures are never doubled more than once.

Server-validated kill XP: `round(victim.maxHealth / 4 × (matchingSpecialty ? 2 : 1))`, minimum 1, with a 1,000,000-per-event overflow guard. Fixed Hunter Undead/Raider kill activities were removed. Qualifying kills require a hostile living victim or explicit inclusion; player, tame-animal and excluded-type victims are rejected, and server adapters can veto more via `HunterExperience.registerKillGuard`. Summon and automation adapters still need pack-specific implementation.

Completed drinking of curated registered potions awards XP proportional to configured brewing difficulty, effect amplifier and effect duration, with first-variant discovery bonus and persistent per-variant cooldown. Only custom Hunter XP is changed, not vanilla enchant XP or Tyz. See `docs/hunter/SPECIALTY-XP-WITCHER-FUNCTIONAL-REQUIREMENTS.md`.

### Mutagenic Brew and permanent Hunter mutations (draft)

Hunter now registers Bio-Mutagen (shapeless: 4 Moss Blocks, 4 Rotten Flesh, 1 Spider Eye) and a nonstandard drinkable Mutagenic Brew created via NeoForge's standard RegisterBrewingRecipesEvent and IBrewingRecipe brewing-stand hooks. A non-Hunter who drinks a Mutagenic Brew suffers Wither II for five minutes, consuming the brew; an already-trained Hunter earns mutation progress only. Dynamic `IBrewingRecipe` conversion preserves `PotionContents` only for potions whose exact base/contents are verified as ordinary brewing-stand recipe results. The `StandPotionEligibility` server-start audit reads registered `addMix` outputs and inspectable standard `IBrewingRecipe` outputs; special-machine potion effects and arbitrary custom `POTION_CONTENTS` are excluded. Exotic stand recipes that require uncommon item metadata need an explicit verified adapter. The brew **does not grant the normal potion effects**; drinking it as a Hunter adds its original positive duration toward a permanent mutation. This also means it earns no separate normal potion-consumption XP.

The Hunter JSON `hunter_mutations` policy defines **720,000 effect ticks / 10 hours**, **no minimum effect duration**, exact-effect blacklist, default max amplifier and **0.0005 hunger exhaustion per server tick per distinct active mutation**. Mutations have **no count or slot cap**. Stronger ranks of the same effect replace rather than stack with weaker ranks, and lower-level potions cannot contribute to higher-level mutation progress. Stronger ranks do not increase the distinct-effect count. Progress and unlocked levels persist across death and logout. Instant effects and banned effects are ineligible for direct permanence.

`/tmmprogress mutations` displays a mutation summary; no dedicated GUI or cure procedure is implemented. See [mutagenic-feature design and QA](../../docs/hunter/MUTAGENIC-BREW-AND-EVERLASTING-ABILITY-AUDIT.md). Draft PR #148 is stacked on #143, #141, #136, #127. **Not yet approved for the integrated pack.**

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
| `armor_lock`, `reserved_fist` | Persistent armor mode and first-hotbar slot inventory relocation; bound armor takes normal wear every 20 ticks when lock is ON; full inventory guarded rather than deleted |
| `glass_touch`, `treasure_hand` | Post-break empty-hand glass drops and Fortune I/Silk Touch I loot replacement with persistent manual toggle; no block entity substitution |
| `water_breathing`, `water_walk`, `momentum_glide` | Air supply stages; crouch escape for water-surface buoyancy; post-Dragon limited aerial descent, never Creative Flight |
| `hunger_discipline`, `monk_leap`, `purification`, `withering_stare`, `creeper_peace` | Native hunger reduction, jump/fall adaptation, normal harmful-effect cleanse, anti-undead gaze and mob-specific pacification |
| `faction_neutrality`, `blindness_ordeal` | Target change suppression with retaliation guard; optional blinded challenge with reset/clear state |
| `evasion`, `fast_hands` | Quarry-tagged hit negation; faster bow/crossbow use ticks |
| `hunter_specialty` | Data-only selected creature family; powers Hunter XP multiplier |

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
/tmmprogress admin event tmm_progression:monk_first_sunset  # OP-only
/tmmprogress specialty list                       # Available Hunter creature specialties
/tmmprogress specialty undead                     # Select one free specialty
/tmmprogress enroll <path>                         # Only for paths enrolled manually
/tmmprogress power blink                          # Blink; also V key
/tmmprogress power armor                          # Toggle Armor Lock
/tmmprogress power treasure                       # Toggle Fortune/Silk hand mode
/tmmprogress power glide                          # Toggle Momentum Glide after Dragon
/tmmprogress power ordeal                         # Start/abort blindness ordeal
```

To run the module from this directory with Gradle: `gradle --no-daemon build` then `gradle runServer` or `gradle runClient`. GitHub Actions `progression-core.yml` checks Java/JUnit, boots a disposable dedicated server, and requires both Monk/Hunter datapacks to load; check the most recent branch CI run.

## Incomplete / guarded behavior

1. **Not full Monk or Hunter:** Many powers now have early Java handlers, not in-world verified parity. High-risk gaps remain: strict first-hotbar/armor click interception and full-inventory behavior, actual no-dupe treasure drops, water physics and Momentum Glide client prediction, Wither boss gaze exception, distinct Lava Protection, advanced Animal calming, class-specific GUI and Hunter tracking/camouflage/execute. None of these should be described as complete.
2. **Tag semantics:** Pilot `tmm_progression:` tags cover a handful of vanilla entities/tools only. No claims about hundreds of modded mobs; consult Hunter taxonomy research. Do not assume external mods' tags exist.
3. **XP legitimacy:** Raw `BlockEvent.BreakEvent` remains a pre-break hook but no longer awards path XP; loot-post-commit `BlockDropsEvent` now awards XP. TreeChop/mass-break, claim-protection rules and automated block destruction still need verified adapters and anti-farm tests. Kill ownership currently credits the immediate source player; summon ownership/spell credit needs adapters. Activity cooldowns are process-local.
4. **Equipment and compatibility:** Armor checks use four vanilla armor slots, not Curios. Mining tiers currently implement vanilla stone/iron/diamond checks and not every modded tool tier. Event interactions with other combat modifiers require dedicated integration testing.
5. **UI, migration and safety:** Serverbound action keybinds exist but no visual skill-tree GUI or server-to-client progress sync, versioned progress migration, path-leave/cure/reset, configured respec price, anti-farm event dedupe, effect-handler hot reload/registration timing tests, or pack-manifest inclusion. No in-game QA has passed.
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


## Client-only default-unbound controls (2026-11-16)

This dual-sided progression JAR also carries the client-only `ClientKeyDefaults` policy and separately registered `tmm_controls_defaults.mixins.json`. It reads the original `options.txt` key names at `Options.load()` HEAD, before Default Options can rewrite them; then inspects the **runtime** client key registry rather than maintaining a per-mod key list. It leaves all 34 vanilla 1.21.1 names unchanged. New standard modded keys start unbound and have an unbound Reset default, while any pre-existing saved key remains assigned. A failed options snapshot disables changes, rather than risking user settings. Custom out-of-registry hotkeys need separate adapters. Unit checks live in `ControlsDefaultsPolicyTest`, graphical client and KeySets persistence still require an authorized live test. The separate mixin config deliberately does not activate unrelated experimental progression mixins.
