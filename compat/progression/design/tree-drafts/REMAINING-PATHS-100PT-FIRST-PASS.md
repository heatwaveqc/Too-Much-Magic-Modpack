# Remaining TMM Progression trees — full first-pass purchase catalog

**November 22, 2026 · DESIGN ONLY / NOT LOADABLE · `dev/progression` source baseline**

This document is a **complete first pricing/skill-roster draft, not approval to install or retune live Path data**. Its machine-readable ledger is `REMAINING-PATHS-100PT-FIRST-PASS.json`. Existing Monk is separately authored as **38 paid nodes / 1,000 points** ([Monk design PR #478](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/478)), so this catalog explicitly excludes Monk.

## Shared rules and current scope

- **Ten full drafts:** universal **Core Skills**, **Hunter**, **Gilded**, **Vampire**, **Werewolf**, one shared **Lich**, **Cave Dragon**, **Sea Dragon**, **Forest Dragon**, and **Bare Zerker**. Each is costed at **100 purchased MXP points** as an initial target, so buying every rank across these ten trees would require 1,000 purchases in aggregate. That sum is **not** a per-character maximum or a promise all incompatible forms can be combined.
- **Core is universal**, not an additional form/Path; graduates who remain Normie have access, and Core purchases remain with the player across valid Path transitions. Newbie cannot earn MXP; Newbie/Normie have no independent purchased skill-tree budget.
- **Single global MXP wallet** is the intended default; hybrid and separate modes remain required by [#475](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/475). Eligibility and native stage gates remain meaningful, even when a second spendable Path wallet is not present.
- **No replacement of native forms:** JAWR owns blood, wolf/vampire forms and cures; Goety/JAWR own Lich soul/death rules; Dragon Survival owns species and growth; Bare Zerker Fury/Too Angry to Die is a Path-level innate rule; Gilded's initiation and default abilities are not purchase fees.
- Historical November 15 nine-tree research ([archived strength catalog](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/main/docs/archive/pr-83-tyz-nine-trees/docs/tyzs/nine-tree-strength-first-pass-v1.json)) contained very large Tyz totals (160–615 points per tree). **These are not simply imported as live skills.** We normalize to first-pass 100-point budgets; final ranks/values/prices need owner balancing review and actual source-handler matching. This is an *allocation proposal*, not a claim of parity to an old 25/50-rank effect.
- **Favor passive, contextual and mod-enhancing skills**; never add new independent cast buttons to solve a missing adapter. Special Abilities should use verified server events and existing Progression hooks. Exact numeric potency is **not** locked merely because this ledger suggests a rank count.
- **No feature entered the live datapack**; this branch contains only a machine-readable design and this Markdown. No source Java, KubeJS, Packwiz, Prism/test integration manifests or current saves are changed.

## Ten-tree summary

| Tree | Proposed nodes | Exact proposed spend | Current implementation/identity note |
|---|---:|---:|---|
| **Core Skills** | 27 | 100 | Core live tree empty; four candidate pilot families in unmerged #379 |
| **Cave Dragon** | 17 | 100 | Previous 2026-11-15 role catalog; native form provider remains authoritative |
| **Sea Dragon** | 18 | 100 | Previous 2026-11-15 role catalog; native form provider remains authoritative |
| **Forest Dragon** | 20 | 100 | Previous 2026-11-15 role catalog; native form provider remains authoritative |
| **Vampire** | 17 | 100 | Previous 2026-11-15 role catalog; native form provider remains authoritative |
| **Werewolf** | 18 | 100 | Previous 2026-11-15 role catalog; native form provider remains authoritative |
| **Lich** | 12 | 100 | Previous 2026-11-15 role catalog; native form provider remains authoritative |
| **Hunter** | 20 | 100 | 31 live Hunter nodes; 17 current paid cost 238, repricing review required |
| **Gilded** | 12 | 100 | Old exploratory source exists, not current runtime |
| **Bare Zerker** | 13 | 100 | Approved concept only, #454; Fury implementation pending |
| **All proposals** | **174** | **1000** | Design investment only, not a single playable combined build |

## Exact proposed skill lists and rank budgets

Each row's **Cost** is the total to buy all proposed ranks. The `source_*` identifiers in the JSON are historical research provenance, not live registry IDs. Proposed new `tyr_progression` IDs in this draft are **not activated** or reserved in the installed game.

### Core Skills — 100 points, 27 skills

*Identity/guardrail:* One global Core independent of Newbie/Normie and specialist identity; no Core rank duplicated in specialist trees.

#### Universal magic fundamentals (37 points)

| Skill | Ranks | SP/rank | Cost | Phase | Intended behavior |
|---|---:|---:|---:|---|---|
| **Reservoir I** (`tyr_progression:core_reservoir_i`) | 5 | 1 | 5 | entry | Personal maximum mana/resources. Initial four runtime preview nodes preserve their original absolute rank-5 ceilings until per-mod base normalization is source-verified. |
| **Wellspring I** (`tyr_progression:core_wellspring_i`) | 5 | 1 | 5 | entry | Personal magical regeneration; dragon forms may purchase this UNIVERSAL option, but have no dragon-exclusive magic regen nodes. |
| **Thrift I** (`tyr_progression:core_thrift_i`) | 5 | 1 | 5 | entry | Discount personal magic payments; original resource-specific hard ceiling and minimum positive payment apply. |
| **Potency I** (`tyr_progression:core_potency_i`) | 5 | 1 | 5 | entry | Base personal spell effect strength; source-specific spells and damage tags. Existing four preview attributes stay exact until normalized. |
| **Quickening** (`tyr_progression:core_quickening`) | 3 | 1 | 3 | entry | Personal spell casting speed, not industrial throughput. |
| **Projection** (`tyr_progression:core_projection`) | 3 | 1 | 3 | entry | Valid spell target range, not free global AoE. |
| **Persistence** (`tyr_progression:core_persistence`) | 5 | 1 | 5 | entry | Duration of supported temporary effects and personally owned summons. |
| **Conjuration** (`tyr_progression:core_conjuration`) | 3 | 1 | 3 | entry | Bounded strength of personally owned summoned allies, not free summoned mob count. |
| **Warding** (`tyr_progression:core_warding`) | 3 | 1 | 3 | entry | Source-verified magical damage resistance, hard mitigation cap. |

#### Retained Tyz 6.4.2 source skills (proposed rehomes) (63 points)

| Skill | Ranks | SP/rank | Cost | Phase | Intended behavior |
|---|---:|---:|---:|---|---|
| **Health Boost** (`tyr_progression:core_health_boost`) | 5 | 1 | 5 | entry | Increases maximum health by {value} half hearts |
| **Oxygen Boost** (`tyr_progression:core_oxygen_boost`) | 3 | 1 | 3 | entry | Increases underwater breathing time by {value}% |
| **Block Reach** (`tyr_progression:core_block_reach`) | 2 | 1 | 2 | entry | Increases the range at which the player can place and destroy by {value} blocks |
| **Attack Damage** (`tyr_progression:core_attack_damage`) | 3 | 1 | 3 | entry | Increases melee damage by {value} |
| **Sneaking Speed** (`tyr_progression:core_sneaking_speed`) | 7 | 1 | 7 | entry | Increases movement speed while sneaking by {value}% |
| **speed_boost** (`tyr_progression:core_speed_boost`) | 3 | 1 | 3 | entry | NO ENGLISH STRING FOUND |
| **Step Height** (`tyr_progression:core_step_height`) | 2 | 1 | 2 | entry | Allows stepping up blocks up to {value} blocks high without jumping |
| **Swim Speed** (`tyr_progression:core_swim_speed`) | 5 | 1 | 5 | entry | Increases swim speed by {value}% |
| **Excavation** (`tyr_progression:core_excavation`) | 6 | 1 | 6 | entry | Increases mining speed by {value}% |
| **Swift Learning** (`tyr_progression:core_swift_learning`) | 5 | 1 | 5 | entry | Increases Skill XP gain by {value}% |
| **Metabolism** (`tyr_progression:core_metabolism`) | 3 | 1 | 3 | entry | Increases food value (nutrition & saturation) by {nutrition_bonus}% |
| **Wisdom** (`tyr_progression:core_wisdom`) | 3 | 1 | 3 | entry | Increases Vanilla XP gain by {bonus_percentage}% |
| **Stealth** (`tyr_progression:core_stealth`) | 5 | 1 | 5 | entry | Reduces the detection range of hostile mobs by {range_reduction}% |
| **Magnet** (`tyr_progression:core_magnet`) | 2 | 1 | 2 | entry | Attracts items within a {block_radius} block radius |
| **Spare Parts** (`tyr_progression:core_spare_parts`) | 2 | 1 | 2 | entry | Grants a {refund_chance}% chance for each ingredient to be recovered when crafting. Up to {max_materials} item(s) per craft |
| **Twist Of Fate** (`tyr_progression:core_twist_of_fate`) | 3 | 1 | 3 | entry | Increases the quantity of items found in structure loot chests by {loot_multiplier}% |
| **Safekept** (`tyr_progression:core_safekept`) | 2 | 1 | 2 | entry | Safeguards the first {saved_slots} slot(s) of your inventory upon death |
| **Haggler** (`tyr_progression:core_haggler`) | 2 | 1 | 2 | entry | Holding an Emerald attracts villagers within a {block_radius} block radius.  They will follow you with a {villager_speed}% speed bonus |

### Cave Dragon — 100 points, 17 skills

*Identity/guardrail:* Keep JAWR/Goety/Dragon Survival authority over native physiology and progression; bought ranks enhance, not replace.

#### Universal Dragon Physiology & Sorcery (species-local) (37 points)

| Skill | Ranks | SP/rank | Cost | Phase | Intended behavior |
|---|---:|---:|---:|---|---|
| **Draconic Reservoir** (`tyr_progression:cave_dragon_draconic_reservoir`) | 6 | 1 | 6 | entry | Massive bonus to personally usable maximum magic resource capacity; never machine/world storage. |
| **Draconic Potency** (`tyr_progression:cave_dragon_draconic_potency`) | 4 | 1 | 4 | entry | Very high general personal spell potency per SP; additive by same reference with typed affinity, not multiplicative recursion. |
| **Draconic Scales** (`tyr_progression:cave_dragon_draconic_scales`) | 8 | 1 | 8 | entry | Species natural armor; deliberately inefficient per SP versus specialized martial defense. |
| **Draconic Claws** (`tyr_progression:cave_dragon_draconic_claws`) | 8 | 1 | 8 | entry | Real dragon form physical strike boost; not global magic damage. |
| **Draconic Talons** (`tyr_progression:cave_dragon_draconic_talons`) | 11 | 1 | 11 | entry | Improve legal dragon claw harvest rate, not tool-tier bypass. |

#### Claw & Fang (17 points)

| Skill | Ranks | SP/rank | Cost | Phase | Intended behavior |
|---|---:|---:|---:|---|---|
| **Stone Claws** (`tyr_progression:cave_dragon_stone_claws`) | 8 | 1 | 8 | entry | Scale unarmed/claw damage only in dragon body; no weapon-wide bonus. |
| **Crushing Bite** (`tyr_progression:cave_dragon_crushing_bite`) | 5 | 1 | 5 | advanced | Bounded armor/knockback pressure on confirmed bite hits. |
| **Seismic Pounce** (`tyr_progression:cave_dragon_seismic_pounce`) | 4 | 1 | 4 | mastery | Cooldown-limited landing shockwave against entities; terrain damage off by default. |

#### Stone Scales (16 points)

| Skill | Ranks | SP/rank | Cost | Phase | Intended behavior |
|---|---:|---:|---:|---|---|
| **Stonehide** (`tyr_progression:cave_dragon_stonehide`) | 8 | 1 | 8 | entry | Incremental natural armor; respect equipment stacking caps. |
| **Earthbound Poise** (`tyr_progression:cave_dragon_earthbound_poise`) | 5 | 1 | 5 | advanced | Reduce knockback and fall damage while grounded. |
| **Mountain Bastion** (`tyr_progression:cave_dragon_mountain_bastion`) | 3 | 1 | 3 | mastery | Short defensive stance with a resource cost and meaningful cooldown, not permanent immunity. |

#### Excavation (19 points)

| Skill | Ranks | SP/rank | Cost | Phase | Intended behavior |
|---|---:|---:|---:|---|---|
| **Tunneler** (`tyr_progression:cave_dragon_tunneler`) | 11 | 1 | 11 | entry | Improve legal claw-mining speed; preserve vanilla/tool harvest checks. |
| **Vein Sense** (`tyr_progression:cave_dragon_vein_sense`) | 5 | 1 | 5 | advanced | Short-range, timed ore awareness without unrestricted x-ray or chunk scans. |
| **Deep Excavation** (`tyr_progression:cave_dragon_deep_excavation`) | 3 | 1 | 3 | mastery | Controlled multi-block excavation; count actual blocks, honor claims and server limits. |

#### Infernal Attunement (11 points)

| Skill | Ranks | SP/rank | Cost | Phase | Intended behavior |
|---|---:|---:|---:|---|---|
| **Infernal Resonance** (`tyr_progression:cave_dragon_infernal_resonance`) | 6 | 1 | 6 | entry | Bonus to source-verified demonic/infernal personal magic, beyond shared dragon arcane power but not world rituals. |
| **Hellbound Command** (`tyr_progression:cave_dragon_hellbound_command`) | 3 | 1 | 3 | advanced | Strengthen eligible player-owned demonic servants and source-verified infernal spell interactions. |
| **Infernal Dominion** (`tyr_progression:cave_dragon_infernal_dominion`) | 2 | 1 | 2 | mastery | Late specialization in demon-affiliated spell potency; strong, narrowly classified and gated. |

### Sea Dragon — 100 points, 18 skills

*Identity/guardrail:* Keep JAWR/Goety/Dragon Survival authority over native physiology and progression; bought ranks enhance, not replace.

**Luck identity:** Abyssal Fortune consumes **50 points** of this draft; `Extreme ×50` is historical investment direction, **not** 50× actual loot or +50 numeric Luck without geared-ceiling evidence.


#### Universal Dragon Physiology & Sorcery (species-local) (19 points)

| Skill | Ranks | SP/rank | Cost | Phase | Intended behavior |
|---|---:|---:|---:|---|---|
| **Draconic Reservoir** (`tyr_progression:sea_dragon_draconic_reservoir`) | 3 | 1 | 3 | entry | Massive bonus to personally usable maximum magic resource capacity; never machine/world storage. |
| **Draconic Potency** (`tyr_progression:sea_dragon_draconic_potency`) | 2 | 1 | 2 | entry | Very high general personal spell potency per SP; additive by same reference with typed affinity, not multiplicative recursion. |
| **Draconic Scales** (`tyr_progression:sea_dragon_draconic_scales`) | 4 | 1 | 4 | entry | Species natural armor; deliberately inefficient per SP versus specialized martial defense. |
| **Draconic Claws** (`tyr_progression:sea_dragon_draconic_claws`) | 4 | 1 | 4 | entry | Real dragon form physical strike boost; not global magic damage. |
| **Draconic Talons** (`tyr_progression:sea_dragon_draconic_talons`) | 6 | 1 | 6 | entry | Improve legal dragon claw harvest rate, not tool-tier bypass. |

#### Tideshaping (8 points)

| Skill | Ranks | SP/rank | Cost | Phase | Intended behavior |
|---|---:|---:|---:|---|---|
| **Current Rider** (`tyr_progression:sea_dragon_current_rider`) | 4 | 1 | 4 | entry | Improve movement control in water, including currents. |
| **Tidal Strike** (`tyr_progression:sea_dragon_tidal_strike`) | 2 | 1 | 2 | advanced | Enhance wet-target combat and native aquatic attacks, not generic damage. |
| **Undertow** (`tyr_progression:sea_dragon_undertow`) | 2 | 1 | 2 | mastery | Cooldown-limited pull/slow applied to legal targets without moving world water blocks. |

#### Abyssal Defense (8 points)

| Skill | Ranks | SP/rank | Cost | Phase | Intended behavior |
|---|---:|---:|---:|---|---|
| **Pressure Adaptation** (`tyr_progression:sea_dragon_pressure_adaptation`) | 4 | 1 | 4 | entry | Reduce underwater environmental penalties. |
| **Brine Scales** (`tyr_progression:sea_dragon_brine_scales`) | 2 | 1 | 2 | advanced | Situational protection while submerged; no blanket armor bonus on land. |
| **Deepwater Refuge** (`tyr_progression:sea_dragon_deepwater_refuge`) | 2 | 1 | 2 | mastery | Bounded underwater healing or defense with a meaningful resource/cooldown cost. |

#### Swimmer's Grace (9 points)

| Skill | Ranks | SP/rank | Cost | Phase | Intended behavior |
|---|---:|---:|---:|---|---|
| **Sleek Form** (`tyr_progression:sea_dragon_sleek_form`) | 5 | 1 | 5 | entry | Increase underwater speed, capped against sprint/exotic gear. |
| **Slipstream** (`tyr_progression:sea_dragon_slipstream`) | 2 | 1 | 2 | advanced | Short burst dodge in water with collision safety. |
| **Ocean Leap** (`tyr_progression:sea_dragon_ocean_leap`) | 2 | 1 | 2 | mastery | Leap from water and control reentry without free flight. |

#### Tidal & Abyssal Magic (6 points)

| Skill | Ranks | SP/rank | Cost | Phase | Intended behavior |
|---|---:|---:|---:|---|---|
| **Tidal Attunement** (`tyr_progression:sea_dragon_tidal_attunement`) | 3 | 1 | 3 | entry | Enhance verified water, ocean and cold-themed personal spell damage/effects, not arbitrary wet status. |
| **Stormcalling** (`tyr_progression:sea_dragon_stormcalling`) | 2 | 1 | 2 | advanced | Enhance verified lightning/storm magic and aquatic control where supported; no unconditional world weather control. |
| **Abyssal Sovereignty** (`tyr_progression:sea_dragon_abyssal_sovereignty`) | 1 | 1 | 1 | mastery | High-level sea/abyssal spell focus, not an assumption that Occultism automatically counts as ocean magic. |

#### Abyssal Fortune (50 points)

| Skill | Ranks | SP/rank | Cost | Phase | Intended behavior |
|---|---:|---:|---:|---|---|
| **Abyssal Fortune** (`tyr_progression:sea_dragon_abyssal_fortune`) | 50 | 1 | 50 | mastery | Extremely high Sea Dragon luck; powerful personal Luck-stat and source-verified luck-sensitive results, but no unconditional duplicate item generation or boss/quest gate bypass. |

### Forest Dragon — 100 points, 20 skills

*Identity/guardrail:* Keep JAWR/Goety/Dragon Survival authority over native physiology and progression; bought ranks enhance, not replace.

#### Universal Dragon Physiology & Sorcery (species-local) (37 points)

| Skill | Ranks | SP/rank | Cost | Phase | Intended behavior |
|---|---:|---:|---:|---|---|
| **Draconic Reservoir** (`tyr_progression:forest_dragon_draconic_reservoir`) | 6 | 1 | 6 | entry | Massive bonus to personally usable maximum magic resource capacity; never machine/world storage. |
| **Draconic Potency** (`tyr_progression:forest_dragon_draconic_potency`) | 4 | 1 | 4 | entry | Very high general personal spell potency per SP; additive by same reference with typed affinity, not multiplicative recursion. |
| **Draconic Scales** (`tyr_progression:forest_dragon_draconic_scales`) | 8 | 1 | 8 | entry | Species natural armor; deliberately inefficient per SP versus specialized martial defense. |
| **Draconic Claws** (`tyr_progression:forest_dragon_draconic_claws`) | 8 | 1 | 8 | entry | Real dragon form physical strike boost; not global magic damage. |
| **Draconic Talons** (`tyr_progression:forest_dragon_draconic_talons`) | 11 | 1 | 11 | entry | Improve legal dragon claw harvest rate, not tool-tier bypass. |

#### Verdant Vitality (17 points)

| Skill | Ranks | SP/rank | Cost | Phase | Intended behavior |
|---|---:|---:|---:|---|---|
| **Green Recovery** (`tyr_progression:forest_dragon_green_recovery`) | 8 | 1 | 8 | entry | Small recovery benefit in living biomes, not in all dimensions. |
| **Living Bark** (`tyr_progression:forest_dragon_living_bark`) | 5 | 1 | 5 | advanced | Situational natural defense near vegetation, with stacking caps. |
| **Elder Grove** (`tyr_progression:forest_dragon_elder_grove`) | 4 | 1 | 4 | mastery | Time-limited regeneration/support pulse, costing dragon resource. |

#### Thorn & Root (12 points)

| Skill | Ranks | SP/rank | Cost | Phase | Intended behavior |
|---|---:|---:|---:|---|---|
| **Bramble Claw** (`tyr_progression:forest_dragon_bramble_claw`) | 6 | 1 | 6 | entry | Add a bounded nature-themed status to confirmed claw attacks. |
| **Entangling Breath** (`tyr_progression:forest_dragon_entangling_breath`) | 4 | 1 | 4 | advanced | Native breath enhancer applying short slows only where source breath supports it. |
| **Thornstorm** (`tyr_progression:forest_dragon_thornstorm`) | 2 | 1 | 2 | mastery | Cooldown-limited small-area crowd control with no world block destruction. |

#### Fertility & Bounty (12 points)

| Skill | Ranks | SP/rank | Cost | Phase | Intended behavior |
|---|---:|---:|---:|---|---|
| **Germination Touch** (`tyr_progression:forest_dragon_germination_touch`) | 4 | 1 | 4 | entry | Enhance actual bonemeal application efficiency/area/yield without free unbounded creation of growth. |
| **Breeder's Blessing** (`tyr_progression:forest_dragon_breeders_blessing`) | 4 | 1 | 4 | advanced | Improve legal animal breeding/cooldown or food efficiency via real pair interaction; never spawn animals without parents. |
| **Harvest Bounty** (`tyr_progression:forest_dragon_harvest_bounty`) | 4 | 1 | 4 | mastery | Increase real crop harvest outcomes with explicit per-block credit and cooldown/caps; preserve claim and mature crop checks. |

#### Fae Affinity (12 points)

| Skill | Ranks | SP/rank | Cost | Phase | Intended behavior |
|---|---:|---:|---:|---|---|
| **Fey Courtesy** (`tyr_progression:forest_dragon_fey_courtesy`) | 6 | 1 | 6 | entry | Improve source-verified fae-aligned personal magic effects and fae relations; no universal mob pacification. |
| **Fey Conduit** (`tyr_progression:forest_dragon_fey_conduit`) | 4 | 1 | 4 | advanced | Specialize fae-aligned spell potency and owned fae summons at higher benefit per SP. |
| **Grove Guardian** (`tyr_progression:forest_dragon_grove_guardian`) | 2 | 1 | 2 | mastery | Bounded fae-aligned personal magic and allied-support synergy without taking over world production. |

#### Retained Tyz 6.4.2 source skills (10 points)

| Skill | Ranks | SP/rank | Cost | Phase | Intended behavior |
|---|---:|---:|---:|---|---|
| **Venomous Strike** (`tyr_progression:forest_dragon_venomous_strike`) | 4 | 1 | 4 | entry | Adds a {success_probability}% chance to poison targets on hit |
| **Green Thumb** (`tyr_progression:forest_dragon_green_thumb`) | 3 | 1 | 3 | entry | Adds a {success_probability}% chance to double drops when harvesting crops and plants |
| **Shepherd’s Blessing** (`tyr_progression:forest_dragon_shepherds_blessing`) | 3 | 1 | 3 | entry | Baby animals grow {growth_speed}% faster |

### Vampire — 100 points, 17 skills

*Identity/guardrail:* Keep JAWR/Goety/Dragon Survival authority over native physiology and progression; bought ranks enhance, not replace.

#### Bloodcraft (18 points)

| Skill | Ranks | SP/rank | Cost | Phase | Intended behavior |
|---|---:|---:|---:|---|---|
| **Efficient Feeding** (`tyr_progression:vampire_efficient_feeding`) | 9 | 1 | 9 | entry | Improve real blood gained from eligible feeding transactions; cannot create blood from empty actions. |
| **Deep Reservoir** (`tyr_progression:vampire_deep_reservoir`) | 5 | 1 | 5 | advanced | Increase one authoritative usable blood capacity, not two independent HUD pools. |
| **Crimson Renewal** (`tyr_progression:vampire_crimson_renewal`) | 4 | 1 | 4 | mastery | Trade a real measured blood debit for bounded emergency healing. |

#### Nightstalker (21 points)

| Skill | Ranks | SP/rank | Cost | Phase | Intended behavior |
|---|---:|---:|---:|---|---|
| **Nocturnal Vision** (`tyr_progression:vampire_nocturnal_vision`) | 12 | 1 | 12 | entry | Better low-light perception with native-sunlight penalties untouched. |
| **Shadow Stalker** (`tyr_progression:vampire_shadow_stalker`) | 5 | 1 | 5 | advanced | Short stealth advantage in darkness; avoid invisibility in full daylight. |
| **Bat Mastery** (`tyr_progression:vampire_bat_mastery`) | 4 | 1 | 4 | mastery | Improve native bat-form controls/efficiency after that native ability is acquired. |

#### Predation (17 points)

| Skill | Ranks | SP/rank | Cost | Phase | Intended behavior |
|---|---:|---:|---:|---|---|
| **Fang Training** (`tyr_progression:vampire_fang_training`) | 8 | 1 | 8 | entry | Scale only verified vampire bite/melee attacks. |
| **Measured Feast** (`tyr_progression:vampire_measured_feast`) | 5 | 1 | 5 | advanced | Limited post-kill blood recovery from eligible kills, with transaction deduplication. |
| **Blood Duelist** (`tyr_progression:vampire_blood_duelist`) | 4 | 1 | 4 | mastery | Focused single-target bonuses under low blood; prevent intentional zero-cost loops. |

#### Undead Constitution (17 points)

| Skill | Ranks | SP/rank | Cost | Phase | Intended behavior |
|---|---:|---:|---:|---|---|
| **Pale Resilience** (`tyr_progression:vampire_pale_resilience`) | 8 | 1 | 8 | entry | Bounded poison/disease-type mitigation where the source system allows it. |
| **Sunshroud** (`tyr_progression:vampire_sunshroud`) | 5 | 1 | 5 | advanced | Short resource-funded sunlight resistance; never erase core daylight tradeoffs. |
| **Elder Presence** (`tyr_progression:vampire_elder_presence`) | 4 | 1 | 4 | mastery | Bounded defensive/control aura with cooldown, not full charm immunity. |

#### Retained Vampire Skill Concepts (JAWR-owned) (22 points)

| Skill | Ranks | SP/rank | Cost | Phase | Intended behavior |
|---|---:|---:|---:|---|---|
| **Blood Thrift** (`tyr_progression:vampire_blood_thrift`) | 5 | 1 | 5 | entry | Reduce only real JAWR blood expenditure/hunger cost on verified committed actions. |
| **Predatory Finesse** (`tyr_progression:vampire_predatory_finesse`) | 5 | 1 | 5 | advanced | Blood-funded vampire-specific melee attack speed, not spell casting speed. |
| **Night Runner** (`tyr_progression:vampire_night_runner`) | 8 | 1 | 8 | entry | Improved vampire walking/running mobility in darkness; no universal free speed. |
| **Crimson Surge** (`tyr_progression:vampire_crimson_surge`) | 4 | 1 | 4 | mastery | Blood-paid high-stage predatory burst, supplementing Blood Regent without minions. |

#### Retained Tyz 6.4.2 source skills (5 points)

| Skill | Ranks | SP/rank | Cost | Phase | Intended behavior |
|---|---:|---:|---:|---|---|
| **Bloodlust** (`tyr_progression:vampire_bloodlust`) | 5 | 1 | 5 | entry | Killing a hostile mob regenerates {health_percentage}% of its max health |

### Werewolf — 100 points, 18 skills

*Identity/guardrail:* Keep JAWR/Goety/Dragon Survival authority over native physiology and progression; bought ranks enhance, not replace.

#### Lupine Vitality (17 points)

| Skill | Ranks | SP/rank | Cost | Phase | Intended behavior |
|---|---:|---:|---:|---|---|
| **Feral Vigor** (`tyr_progression:werewolf_feral_vigor`) | 8 | 1 | 8 | entry | Large additional maximum health / extra hearts, materially more per SP than universal vitality boosts. |
| **Regenerative Flesh** (`tyr_progression:werewolf_regenerative_flesh`) | 5 | 1 | 5 | advanced | Strong, bounded actual HP regeneration under verified werewolf physiology and legal feeding/hunger rules; not mana regen. |
| **Unbreakable Beast** (`tyr_progression:werewolf_unbreakable_beast`) | 4 | 1 | 4 | mastery | High-level extra HP and in-combat wound recovery with caps and meaningful werewolf vulnerabilities. |

#### Feral Might (17 points)

| Skill | Ranks | SP/rank | Cost | Phase | Intended behavior |
|---|---:|---:|---:|---|---|
| **Rending Claws** (`tyr_progression:werewolf_rending_claws`) | 8 | 1 | 8 | entry | Scale claw damage in valid wolf/beast form, respecting weapon modifiers. |
| **Savage Momentum** (`tyr_progression:werewolf_savage_momentum`) | 5 | 1 | 5 | advanced | Short stacking melee bonus after actual hits, capped and reset out of combat. |
| **Primal Fury** (`tyr_progression:werewolf_primal_fury`) | 4 | 1 | 4 | mastery | Timed burst consuming native appropriate resource or applying fatigue. |

#### Moonrunner (17 points)

| Skill | Ranks | SP/rank | Cost | Phase | Intended behavior |
|---|---:|---:|---:|---|---|
| **Predator's Stride** (`tyr_progression:werewolf_predators_stride`) | 8 | 1 | 8 | entry | Bonus mobility in valid wolf form. |
| **Pouncing Leap** (`tyr_progression:werewolf_pouncing_leap`) | 5 | 1 | 5 | advanced | Targeted controlled leap with landing safety. |
| **Lunar Chase** (`tyr_progression:werewolf_lunar_chase`) | 4 | 1 | 4 | mastery | Short chase burst under moon conditions with weaker daytime counterpart. |

#### Wild Communion (16 points)

| Skill | Ranks | SP/rank | Cost | Phase | Intended behavior |
|---|---:|---:|---:|---|---|
| **Wild Attunement** (`tyr_progression:werewolf_wild_attunement`) | 8 | 1 | 8 | entry | Increase eligible nature magic potency (Botania/Roots personal effects) while werewolf state is valid. |
| **Rootsong** (`tyr_progression:werewolf_rootsong`) | 5 | 1 | 5 | advanced | Further increase player nature spell potency or healing, separate from ordinary combat regeneration. |
| **Grove Howl** (`tyr_progression:werewolf_grove_howl`) | 3 | 1 | 3 | mastery | High-level synergy between wolf pack allies and personally cast nature magic, bounded to eligible owners/effects. |

#### Retained Lycan Skill Concepts (JAWR-owned) (28 points)

| Skill | Ranks | SP/rank | Cost | Phase | Intended behavior |
|---|---:|---:|---:|---|---|
| **Bloodlust Recovery** (`tyr_progression:werewolf_bloodlust_recovery`) | 5 | 1 | 5 | advanced | Health restoration from actual player-credited hostile kill, with real cooldown. |
| **Bite Techniques** (`tyr_progression:werewolf_bite_techniques`) | 5 | 1 | 5 | advanced | Select either limited bleeding or brief stun enhancement on confirmed wolf bites. |
| **Feral Excavation** (`tyr_progression:werewolf_feral_excavation`) | 8 | 1 | 8 | entry | Boost permitted claw mining/harvest throughput while in real JAWR wolf form. |
| **Arrow Awareness** (`tyr_progression:werewolf_arrow_awareness`) | 5 | 1 | 5 | advanced | Limited moving-projectile evasion with shared evasion budget. |
| **Tempered Against Silver** (`tyr_progression:werewolf_tempered_against_silver`) | 5 | 1 | 5 | mastery | Partial damage mitigation against source-verified silver damage without erasing weakness. |

#### Retained Tyz 6.4.2 source skills (5 points)

| Skill | Ranks | SP/rank | Cost | Phase | Intended behavior |
|---|---:|---:|---:|---|---|
| **Rage** (`tyr_progression:werewolf_rage`) | 5 | 1 | 5 | entry | Increases damage by {damage_buff}% when health is below {health_threshold}% |

### Lich — 100 points, 12 skills

*Identity/guardrail:* Shared ONE Lich tree for either provider/Archlich; native death priority is unchanged.

#### Soul Economy (25 points)

| Skill | Ranks | SP/rank | Cost | Phase | Intended behavior |
|---|---:|---:|---:|---|---|
| **Soul Conservation** (`tyr_progression:lich_soul_conservation`) | 12 | 1 | 12 | entry | Lower eligible personal necromancy spell costs, not global ritual/machine cost. |
| **Soul Reserve** (`tyr_progression:lich_soul_reserve`) | 8 | 1 | 8 | advanced | Bounded native personal soul/Arca resource capacity where extensible. |
| **Soul Treasury** (`tyr_progression:lich_soul_treasury`) | 5 | 1 | 5 | mastery | Small high-level sustain on committed necromantic actions, no passive self-funding loop. |

#### Blood & Nature Opposition (25 points)

| Skill | Ranks | SP/rank | Cost | Phase | Intended behavior |
|---|---:|---:|---:|---|---|
| **Sanguine Attunement** (`tyr_progression:lich_sanguine_attunement`) | 12 | 1 | 12 | entry | Improve verified blood-based personal magic (Neo Vitae/EvilCraft where actual hooks apply) while applying a meaningful reduction to this lich's personal Botania/Roots nature spell power. |
| **Crimson Channel** (`tyr_progression:lich_crimson_channel`) | 8 | 1 | 8 | advanced | Strengthen source-verified blood magic further with proportionate nature-magic potency tradeoff; not passive blood machine throughput. |
| **Verdant Severance** (`tyr_progression:lich_verdant_severance`) | 5 | 1 | 5 | mastery | High blood-magic specialization at a stronger self-inflicted Botania/Roots nature power penalty; no world-machine or other-player debuff. |

#### Undead Host (25 points)

| Skill | Ranks | SP/rank | Cost | Phase | Intended behavior |
|---|---:|---:|---:|---|---|
| **Bone Captain** (`tyr_progression:lich_bone_captain`) | 12 | 1 | 12 | entry | Modest bonus to personally owned undead servants. |
| **Grave Regiment** (`tyr_progression:lich_grave_regiment`) | 8 | 1 | 8 | advanced | Improved servant coordination/leash and bounded count efficiency. |
| **Death Host** (`tyr_progression:lich_death_host`) | 5 | 1 | 5 | mastery | Late temporary enhancement to current owned minions with hard entity budget. |

#### Soul Preservation (25 points)

| Skill | Ranks | SP/rank | Cost | Phase | Intended behavior |
|---|---:|---:|---:|---|---|
| **Cold Persistence** (`tyr_progression:lich_cold_persistence`) | 12 | 1 | 12 | entry | Limited tolerance of wither/undead hazards, preserving explicit weaknesses. |
| **Anchor Attunement** (`tyr_progression:lich_anchor_attunement`) | 8 | 1 | 8 | advanced | Improve diagnostics or controlled anchor maintenance; never bypass native phylactery/Arca cost. |
| **Final Passage** (`tyr_progression:lich_final_passage`) | 5 | 1 | 5 | mastery | Minor post-rescue recovery *after* an authoritative successful resurrection; no extra death-save trigger. |

### Hunter — 100 points, 20 skills

*Identity/guardrail:* Preserve all 14 existing zero-price specialty_* choice nodes, original prey taxonomy, monster victim/loot checks and poison+regeneration initiation. Current 17 purchased nodes cost 238; proposal retains their current ranks/effects while repricing to 91 plus 9 new tracking points. Never silently rewrite live costs.

**Major reprice gate:** the real current 17 purchased Hunter nodes total **238** SP; their 17 unchanged rank counts total **91** ranks. The proposed 100-point curve would charge 1 SP per original rank (91), plus three new quarry studies at 3 ranks each (9). Nothing is repriced in this design PR; source owner must explicitly review and approve the economic conversion.


#### Existing 17 purchased skills (repricing proposal, no live mutation) (91 points)

| Skill | Ranks | SP/rank | Cost | Phase | Intended behavior |
|---|---:|---:|---:|---|---|
| **Undead Slayer** (`tyr_progression:undead_slayer`) | 10 | 1 | 10 | existing | melee_bonus |
| **Raider Slayer** (`tyr_progression:raider_slayer`) | 10 | 1 | 10 | existing | melee_bonus |
| **Monster Resilience** (`tyr_progression:monster_resilience`) | 5 | 1 | 5 | existing | attribute |
| **Ranged Quarry** (`tyr_progression:ranged_quarry`) | 5 | 1 | 5 | existing | damage_bonus |
| **Equipment Care** (`tyr_progression:equipment_care`) | 3 | 1 | 3 | existing | gear_durability |
| **Fortified Training** (`tyr_progression:fortified_training`) | 3 | 1 | 3 | existing | attribute |
| **Rooted** (`tyr_progression:rooted`) | 5 | 1 | 5 | existing | attribute |
| **Evasion Undead** (`tyr_progression:evasion_undead`) | 5 | 1 | 5 | existing | evasion |
| **Evasion Raiders** (`tyr_progression:evasion_raiders`) | 5 | 1 | 5 | existing | evasion |
| **Sword Specialist** (`tyr_progression:sword_specialist`) | 5 | 1 | 5 | existing | damage_bonus |
| **Axe Specialist** (`tyr_progression:axe_specialist`) | 5 | 1 | 5 | existing | damage_bonus |
| **Scythe Specialist** (`tyr_progression:scythe_specialist`) | 5 | 1 | 5 | existing | damage_bonus |
| **Bow Specialist** (`tyr_progression:bow_specialist`) | 5 | 1 | 5 | existing | damage_bonus |
| **Crossbow Specialist** (`tyr_progression:crossbow_specialist`) | 5 | 1 | 5 | existing | damage_bonus |
| **Fast Hands Bow** (`tyr_progression:fast_hands_bow`) | 5 | 1 | 5 | existing | fast_hands |
| **Fast Hands Crossbow** (`tyr_progression:fast_hands_crossbow`) | 5 | 1 | 5 | existing | fast_hands |
| **Silver Hunter** (`tyr_progression:silver_hunter`) | 5 | 1 | 5 | existing | damage_bonus |

#### Additional Quarry Studies (9 points)

| Skill | Ranks | SP/rank | Cost | Phase | Intended behavior |
|---|---:|---:|---:|---|---|
| **Monster Lore** (`tyr_progression:hunter_monster_lore`) | 3 | 1 | 3 | adept | Recognize known prey category and weakness without arbitrary boss revelations. |
| **Quarry Mark** (`tyr_progression:hunter_quarry_mark`) | 3 | 1 | 3 | adept | Mark one legitimate visible quarry using existing scoped tracking handler. |
| **Hunter Tracking** (`tyr_progression:hunter_hunter_tracking`) | 3 | 1 | 3 | adept | Give valid quarry temporary, bounded Glowing while player tracks it. |

### Gilded — 100 points, 12 skills

*Identity/guardrail:* FREE native/Path abilities: Nether golden-armor+gold-food successful initiation, baseline diamond-equivalent gold, +3 virtual Fortune and +3 virtual Looting, Piglin/Brute peace, immediate lava-fishing license, rank18 full fire-tag immunity. Do not charge points for these again.

#### Goldcraft (44 points)

| Skill | Ranks | SP/rank | Cost | Phase | Intended behavior |
|---|---:|---:|---:|---|---|
| **Fortune Mastery** (`tyr_progression:gilded_fortune_mastery`) | 7 | 1 | 7 | entry | Increase legal gold/Netherite virtual Fortune +3→+10, one per purchase; Silk prevails. |
| **Looting Mastery** (`tyr_progression:gilded_looting_mastery`) | 7 | 1 | 7 | entry | Increase golden sword/axe direct-melee virtual Looting +3→+10, independent of Fortune. |
| **Lasting Gilt** (`tyr_progression:gilded_lasting_gilt`) | 10 | 1 | 10 | entry | Raise virtual Unbreaking on equipped gold/Netherite gear only. |
| **Golden Renewal** (`tyr_progression:gilded_golden_renewal`) | 10 | 1 | 10 | entry | Increase actual equipped gold/Netherite repair at equal rates. |
| **Gilded Grip Mastery** (`tyr_progression:gilded_gilded_grip_mastery`) | 10 | 1 | 10 | entry | Supplement free diamond-equivalence with bounded legitimate equipped-gold handling; do not repurchase free parity. |

#### Nether Society (45 points)

| Skill | Ranks | SP/rank | Cost | Phase | Intended behavior |
|---|---:|---:|---:|---|---|
| **Piglin Diplomacy** (`tyr_progression:gilded_piglin_diplomacy`) | 5 | 1 | 5 | entry | Improve barter/trade interactions while preserving baseline Piglin/Brute friendliness. |
| **Lava Angler** (`tyr_progression:gilded_lava_angler`) | 10 | 1 | 10 | entry | Improve lawful lava catch utility; baseline fishing stays free. |
| **Gold Nourishment** (`tyr_progression:gilded_gold_nourishment`) | 10 | 1 | 10 | entry | Improve recovery from gold-qualified food without undoing locked restrictions or duplicating potion effects. |
| **Nether Affinity** (`tyr_progression:gilded_nether_affinity`) | 10 | 1 | 10 | entry | Bounded environmental movement and defense in Nether; separate from rank18 fire immunity. |
| **Metallurgist** (`tyr_progression:gilded_metallurgist`) | 10 | 1 | 10 | entry | Improve verified golden tool/weapon performance without altering global item tiers. |

#### Sovereignty (11 points)

| Skill | Ranks | SP/rank | Cost | Phase | Intended behavior |
|---|---:|---:|---:|---|---|
| **Glittering Luck** (`tyr_progression:gilded_glittering_luck`) | 6 | 1 | 6 | entry | True player Luck improvements, not virtual Fortune or Looting. |
| **Gilded Sovereign** (`tyr_progression:gilded_gilded_sovereign`) | 1 | 5 | 5 | capstone | Proposed post-Nether late-game golden mastery capstone; exact effect requires owner review. |

### Bare Zerker — 100 points, 13 skills

*Identity/guardrail:* Free locked innate: Fury capacity100; +Fury from actual post-mitigation damage DEALT only; 1 Fury/sec decay; each actual received hit adds 1s Regeneration I+Resistance I; >50 pre-hit Fury would-be death triggers full vanilla Totem and drains all Fury. Do not sell innate abilities or alter their formulas. Chest chafing, allowed clothes, Red Cap and woad are separate form mechanics.

#### Fury Combat (40 points)

| Skill | Ranks | SP/rank | Cost | Phase | Intended behavior |
|---|---:|---:|---:|---|---|
| **Reckless Sword** (`tyr_progression:bare_zerker_reckless_sword`) | 10 | 1 | 10 | entry | Improve shieldless sword damage under approved chest-empty rules, Fury still only from actual dealt HP. |
| **Cleaving Axe** (`tyr_progression:bare_zerker_cleaving_axe`) | 10 | 1 | 10 | entry | Improve shieldless axe damage, no new source of free Fury. |
| **Battle Rhythm** (`tyr_progression:bare_zerker_battle_rhythm`) | 10 | 1 | 10 | entry | Bounded consecutive real-hit damage bonuses, no proc from mere incoming hits. |
| **Bloodied Drive** (`tyr_progression:bare_zerker_bloodied_drive`) | 10 | 1 | 10 | entry | Brief valid kill-triggered combat momentum, no Fury injections. |

#### Endurance Under Fire (30 points)

| Skill | Ranks | SP/rank | Cost | Phase | Intended behavior |
|---|---:|---:|---:|---|---|
| **Skin Hardened** (`tyr_progression:bare_zerker_skin_hardened`) | 10 | 1 | 10 | entry | Actual innate mitigation while chest-empty, no conventional armor buffs. |
| **Regeneration Amplifier** (`tyr_progression:bare_zerker_regen_amplifier`) | 10 | 1 | 10 | entry | Improve amplifier of owner-approved incoming-hit regeneration, preserving +1s per hit duration. |
| **Resistance Amplifier** (`tyr_progression:bare_zerker_resistance_amplifier`) | 10 | 1 | 10 | entry | Improve amplifier of owner-approved incoming-hit Resistance, preserving +1s per hit duration. |

#### Blood and Woad (20 points)

| Skill | Ranks | SP/rank | Cost | Phase | Intended behavior |
|---|---:|---:|---:|---|---|
| **Red Cap Fortitude** (`tyr_progression:bare_zerker_redcap_fortitude`) | 5 | 1 | 5 | entry | Improve blood-dipped Red Cap temporary powers; duration/cost owner review. |
| **Woad Attunement** (`tyr_progression:bare_zerker_woad_attunement`) | 5 | 1 | 5 | entry | Enhance source-verified Neo Vitae Sentient Armor-equivalent woad benefits without equipping armor. |
| **Neo Vitae Bloodmaster** (`tyr_progression:bare_zerker_neo_bloodmaster`) | 5 | 1 | 5 | entry | Improve verified Neo Vitae blood magic interactions, with genuine costs. |
| **EvilCraft Hematurgy** (`tyr_progression:bare_zerker_evilcraft_bloodcraft`) | 5 | 1 | 5 | entry | Improve verified EvilCraft blood transactions/ritual effects with no duplicate blood credit. |

#### Berserk Mastery (10 points)

| Skill | Ranks | SP/rank | Cost | Phase | Intended behavior |
|---|---:|---:|---:|---|---|
| **Antimagic Grit** (`tyr_progression:bare_zerker_antimagic_grit`) | 5 | 1 | 5 | entry | Bounded mitigation for specified applied incoming magic damage types, not mod-caster origin. |
| **Last Stand Mastery** (`tyr_progression:bare_zerker_last_stand_mastery`) | 5 | 1 | 5 | mastery | Improve recovery after real Too Angry to Die event, without altering >50 rule, full totem effects, or complete Fury drain. |

## Additional newer Paths: outlines only, not priced

The owner has deliberately left several identities, abilities, initiation routes, state caps and mutation-slot choices open. Do not manufacture a final 100-point purchase table for these four and accidentally convert unresolved proposals into code obligations.

| Path | Proposed branches | What is already established |
|---|---|---|
| **Sapient Slime** | Body Mass / Biomass; Reactive Allied Slimes; Absorption and Ooze Kinship | Slime-themed proposed Path; mutagen-altered Potion of Oozing initiation candidate; damage-spawn and absorption require #446 state/action, size collision and caps. |
| **Sexy Demon (Incubus/Succubus)** | Infernal Body; Ranged Charm (one mob, 600 ticks); Right-click Permanent Taming; Companion Command | One Path, not sex-specific trees; generic #451 Charm and Taming are distinct. Cave Dragon pairing approved. Lilith ring initiation remains candidate. |
| **Dryad** | Photosynthesis and Saturation; Greening Footsteps; Plant/Fae Diplomacy; Nature Magic | Plant-primary, only Magical food plus drinks/potions; sunlight OR water OR plantlike ground nourishing, enhanced saturation capacity, cautious greening, fire vulnerability. Forest Dragon pairing approved. NO Biomass economy or mandatory Rooting. |
| **Spawn** | Shared natural armor and regeneration trunk; Cephalid; Deep One; Shoggoth; Mi-Go | One Aberration-primary Path with mixable anatomical physical lineages; constrained major/minor mutation choices. Sea Dragon pairing approved. Five tentative slots (2 major/3 minor) NOT locked. |

### Outstanding owner decisions for the optional Paths

**Sapient Slime:** real initiation; biomass cap; spawn bound and loot/XP; entity ownership; size/player rendering.
**Sexy Demon (Incubus/Succubus):** tame target allowlist; cost; max followers; charm cast/delivery; AI and raids.
**Dryad:** initiation; hunger/saturation scale; terrain and claims; precise fire weakness; magic affinities.
**Spawn:** mutation slot number; actual anatomical skills; initiation; respec/persistence; appearance; choice/exclusivity.

### Newbie / Normie are baseline Paths, not paid trees

- **Newbie**: initial first-class baseline; does not earn shared MXP. Do not create purchasable Newbie SP abilities as an implicit exception.
- **Normie**: graduated baseline; can earn MXP and use the one universal Core tree without joining a specialist. No separate Normie purchase budget or reset of a player's shared Core inventory.

## Key pre-implementation crosswalk and risk register

1. **The most immediate content reuse is Hunter.** Source `hunter.json` has 31 skills, including 14 zero-price `specialty_*` selectors. The draft preserves the *17 original purchased skill IDs and rank counts* and leaves those specialty choices free. Repricing 238→91 is significant and requires explicit gameplay acceptance, not a silent config edit. New Monster Lore, Quarry Mark and Tracking should use current accepted prey tags and source-verified entities.
2. **Core ↔ Monk overlap:** distinguish universal Reservoir/Wellspring/Thrift/Potency from the Monk's additional cultivation scaling. Reconcile open [PR #379](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/379) legacy namespace, exact effects and the 85% geared-equivalence benchmark. No duplicate purchased Core node in every specialized path.
3. **Gilded requires porting existing work, not rebootstrapping:** source-native gold→diamond parity, Nether golden armor+food initiation, +3/→+10 virtual Fortune and Looting, Piglin/Brute faction neutrality, lava fishing and rank18 Undying Ember are historic owner decisions. Do not spend SP to reacquire them. Inspect archived exploratory source and the current Progression provider adapters before any migration.
4. **Vampire/Werewolf and Lich:** provider-first native state and resource authority. Blood, wolf shape, vampire sunlight, Goety phylactery/Arca/JAWR death priorities, owned servant tracking and dracolich lifecycles require actual optional mod ABI. Archlich buys one Lich tree, not two. A skill may be *inactive* if optional provider absent, not a source of fake benefits.
5. **Three dragons:** species-exclusive Path tree IDs, growth/milestone gates and real Dragon Survival source. Cave excavation, Sea mobility and Luck, Forest nature magic differ materially. Dragon food handling #444 remains OWNER-HOLD and isn't authorized by these skill sketches. Previously permitted pairings [#452](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/452) survive: Sexy Demon + Cave, Dryad + Forest, Spawn + Sea.
6. **Bare Zerker:** the mandatory Fury formula, no chest, damage-hit potion durations, full Totem effect and chafing are **Path fundamentals**, not paid skill choices. Source-verify woad vs Neo Vitae Sentient Armor and EvilCraft/Neo Vitae blood-specific skill effects before implementation; its proposed 100-point purchased ranks must never change the locked 100 Fury, 1/sec decay or >50 death threshold.
7. **Other future Paths:** Sapient Slime biomass and bonded spawns, Sexy Demon Charm vs Taming, Dryad nutrition/terrain, and Spawn's constrained physiological mutation choices require the shared bounded state/action engine [#446](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/446). The four 'outline only' trees are not a 100-point sign-off; Spawn especially must preserve limited exclusive mutation selections.
8. **Acceptance:** validate every purchased ability against live handler registry/optional dependencies; compare highest legal equipped builds to bare Monk within known goals; test gates/respec/multiplayer/dormancy/save; run source CI on actual code PRs before any owner-authorized game build. Nothing here authorizes runtime, server or Prism work.

## Recommended content build order

**First:** Core (cross-Path dependency, reference skill families) and Hunter (already in the engine; expensive repricing review). **Second:** Gilded (source work exists) and native forms (Vampire, Werewolf, shared Lich). **Third:** Dragon species after source-state and food-engine boundaries are reconciled. **Fourth:** Bare Zerker's state/action and blood/woad source audit. **Later:** optional four Path shells after their deliberately unresolved design decisions are reviewed.

Review ledger: [#475 XP policy](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/475), [#455 Newbie/Normie](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/455), [#454 Bare Zerker](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/454), [#415 optional Paths](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/415), [#446 shared state/action](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/446), [#271 gameplay QA](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/271).
