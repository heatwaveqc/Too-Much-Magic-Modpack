# TMM Progression — remaining Path trees, one point per rank

**DESIGN ONLY · November 22, 2026 · source baseline `dev/progression`**

Authoritative ledger: `REMAINING-PATHS-100PT-FIRST-PASS.json` schema `tmm_remaining_path_tree_blueprint_v3`. None of these drafted purchases, balances or source provider hooks are installed in-game by this PR.

## Owner-locked budget accounting

- **One advancement point purchases exactly one skill rank. There are NO 2-point, 3-point, 4-point or 5-point ranks.** A five-point capstone must have five incremental 1-point ranks, or be split into genuinely distinct skills; it is not one costly rank.
- For every non-Monk tree, **full eligible mastery = exactly 100 purchasable ranks = exactly 100 advancement points**. Monk is the established exception at **1,000 points**, also at one point per rank.
- When a tree displays mutually exclusive alternatives, only the compatible combination available to **one character** needs to cost 100 points. Alternative specializations may make the entire displayed catalog exceed 100.
- Relative to the original 300–500-point trees, the intended effect per advancement point rises. **Do not** accomplish 'fewer levels' by charging more than one point per rank: if a named skill should have fewer ranks, redistribute its remaining points among distinct useful abilities.
- Some per-skill totals still span 10–50 ranks. Those are *one-point increments*, and the intended cumulative power remains a balancing target to verify against the actual provider and gear, **not** a promise the previous absolute `per_rank` can be copied unchanged.
- Shared global MXP wallet remains the planned default; configurable hybrid/separate modes remain under #475. Free innate abilities, native transformation states, and separately purchased Core ranks are not sold twice.

## Summary

| Tree | Displayed paid skills | Cost to master one eligible build |
|---|---:|---:|
| Core Skills | 27 | **100** |
| Cave Dragon | 17 | **100** |
| Sea Dragon | 18 | **100** |
| Forest Dragon | 20 | **100** |
| Vampire | 17 | **100** |
| Werewolf | 18 | **100** |
| Lich | 12 | **100** |
| Hunter | 57 | **100** |
| Gilded | 12 | **100** |
| Bare Zerker | 13 | **100** |

The ten draft catalogs display **211 paid skill options**; 57 are Hunter alternatives (not simultaneously eligible). Newbie and Normie remain baseline Paths with no independent purchasable ranks; Core is universal, not a specialist Path.

## Hunter — 62 shared + 8 training style + 30 favored prey = 100

**Hunter's existing `dev/progression` data remains unchanged.** The current 17 paid Hunter skills use 238 total points under the old escalating rank-cost formula. This is a proposed **full repricing/rebalancing**, requiring owner review, skill/milestone migration and tests before any code PR may replace it. The 14 free `specialty_*` choices are retained, one at a time.

### Shared training — 62 ranks (62 points)

| Skill | Ranks | Cost/rank | Total | Intended outcome |
|---|---:|---:|---:|---|
| **Rooted** (`tyr_progression:rooted`) | 5 | 1 | 5 | attribute |
| **Monster Resilience** (`tyr_progression:monster_resilience`) | 5 | 1 | 5 | attribute |
| **Sword Specialist** (`tyr_progression:sword_specialist`) | 5 | 1 | 5 | damage_bonus |
| **Axe Specialist** (`tyr_progression:axe_specialist`) | 5 | 1 | 5 | damage_bonus |
| **Scythe Specialist** (`tyr_progression:scythe_specialist`) | 5 | 1 | 5 | damage_bonus |
| **Bow Specialist** (`tyr_progression:bow_specialist`) | 5 | 1 | 5 | damage_bonus |
| **Crossbow Specialist** (`tyr_progression:crossbow_specialist`) | 5 | 1 | 5 | damage_bonus |
| **Fast Hands Bow** (`tyr_progression:fast_hands_bow`) | 5 | 1 | 5 | fast_hands |
| **Fast Hands Crossbow** (`tyr_progression:fast_hands_crossbow`) | 5 | 1 | 5 | fast_hands |
| **Silver Hunter** (`tyr_progression:silver_hunter`) | 5 | 1 | 5 | damage_bonus |
| **Monster Lore** (`tyr_progression:hunter_monster_lore`) | 4 | 1 | 4 | Discover verified prey types and weaknesses without revealing arbitrary boss registries. |
| **Quarry Mark** (`tyr_progression:hunter_quarry_mark`) | 4 | 1 | 4 | Mark one verified visible enemy quarry as the target of follow-on tracking bonuses. |
| **Hunter Tracking** (`tyr_progression:hunter_hunter_tracking`) | 4 | 1 | 4 | Reveal eligible prey with temporary Glowing within safe bounded range and count. |

### Training style — select ONE, 8 ranks (8 points)

| Choice | Ranks | Cost/rank | Total |
|---|---:|---:|---:|
| **Equipment Care** | 8 | 1 | 8 |
| **Fortified Training** | 8 | 1 | 8 |

### Favored prey — select ONE of 14, 30 ranks (30 points)

The existing free Favored Prey choice must gate purchases and activation on the server, not merely change UI visibility. Each of the 14 prey specializations includes **12 offense + 8 defense + 10 mastery ranks**, all cost one point each.

#### Undead specialty (30 points)
Free license: `tyr_progression:specialty_undead`

| Skill | Ranks | Cost/rank | Total | Purpose |
|---|---:|---:|---:|---|
| **Undead Slayer** (`tyr_progression:undead_slayer`) | 12 | 1 | 12 | Strong offense against eligible undead |
| **Undead Evasion** (`tyr_progression:evasion_undead`) | 8 | 1 | 8 | Reflexes against undead attackers |
| **Ranged Quarry** (`tyr_progression:ranged_quarry`) | 10 | 1 | 10 | Verified ranged shots against undead; retain old bounded ranged-quarry rules |

#### Raider specialty (30 points)
Free license: `tyr_progression:specialty_illager`

| Skill | Ranks | Cost/rank | Total | Purpose |
|---|---:|---:|---:|---|
| **Raider Slayer** (`tyr_progression:raider_slayer`) | 12 | 1 | 12 | Improve direct hits against confirmed raiders |
| **Raider Evasion** (`tyr_progression:evasion_raiders`) | 8 | 1 | 8 | Evade raider attacks |
| **Raidbreaker** (`tyr_progression:hunter_illager_mastery`) | 10 | 1 | 10 | Improve defense against valid raid weapons/magic; not global villager-raid immunity |

#### Drakebane specialty (30 points)
Free license: `tyr_progression:specialty_dragon`

| Skill | Ranks | Cost/rank | Total | Purpose |
|---|---:|---:|---:|---|
| **Drakebane** (`tyr_progression:hunter_dragon_slayer`) | 12 | 1 | 12 | Damage legal Dragon taxonomy quarry |
| **Scaled Hunter** (`tyr_progression:hunter_dragon_defense`) | 8 | 1 | 8 | Defend against verified dragon attacks, source-scoped |
| **Dragonbane Tactics** (`tyr_progression:hunter_dragon_mastery`) | 10 | 1 | 10 | Improve genuine dragon-hunt timing and damage without affecting all bosses |

#### Void specialty (30 points)
Free license: `tyr_progression:specialty_void`

| Skill | Ranks | Cost/rank | Total | Purpose |
|---|---:|---:|---:|---|
| **Void Slayer** (`tyr_progression:hunter_void_slayer`) | 12 | 1 | 12 | Damage confirmed End/void quarry |
| **Voidwalker Guard** (`tyr_progression:hunter_void_defense`) | 8 | 1 | 8 | Defend against valid void creature attacks |
| **Rift Tracker** (`tyr_progression:hunter_void_mastery`) | 10 | 1 | 10 | Keep marked teleporting quarry reacquirable within limits |

#### Piglin specialty (30 points)
Free license: `tyr_progression:specialty_piglin`

| Skill | Ranks | Cost/rank | Total | Purpose |
|---|---:|---:|---:|---|
| **Piglin Slayer** (`tyr_progression:hunter_piglin_slayer`) | 12 | 1 | 12 | Damage approved Piglin/Hoglin quarry |
| **Bastion Guard** (`tyr_progression:hunter_piglin_defense`) | 8 | 1 | 8 | Defend against Piglin/Hoglin attacks |
| **Hoglin Harrier** (`tyr_progression:hunter_piglin_mastery`) | 10 | 1 | 10 | Improve direct melee vs those groups without inheriting Gilded's diplomacy |

#### Sculk specialty (30 points)
Free license: `tyr_progression:specialty_sculk`

| Skill | Ranks | Cost/rank | Total | Purpose |
|---|---:|---:|---:|---|
| **Sculk Slayer** (`tyr_progression:hunter_sculk_slayer`) | 12 | 1 | 12 | Damage approved Sculk prey, including genuine Warden where eligible |
| **Deepdark Vigilance** (`tyr_progression:hunter_sculk_defense`) | 8 | 1 | 8 | Resist verified Sculk/sonic damage types only |
| **Sonic Defense** (`tyr_progression:hunter_sculk_mastery`) | 10 | 1 | 10 | Recognize and evade valid sonic attacks without silencing arbitrary sensors |

#### Aquatic specialty (30 points)
Free license: `tyr_progression:specialty_aquatic`

| Skill | Ranks | Cost/rank | Total | Purpose |
|---|---:|---:|---:|---|
| **Aquatic Slayer** (`tyr_progression:hunter_aquatic_slayer`) | 12 | 1 | 12 | Damage actual aquatic prey taxonomy |
| **Deepwater Guard** (`tyr_progression:hunter_aquatic_defense`) | 8 | 1 | 8 | Reduce underwater prey attacks, not all drowning |
| **Harpoon Hunter** (`tyr_progression:hunter_aquatic_mastery`) | 10 | 1 | 10 | Improve actual underwater bow/trident/projectile fights with legal attribution |

#### Construct specialty (30 points)
Free license: `tyr_progression:specialty_construct`

| Skill | Ranks | Cost/rank | Total | Purpose |
|---|---:|---:|---:|---|
| **Construct Slayer** (`tyr_progression:hunter_construct_slayer`) | 12 | 1 | 12 | Damage eligible construct targets only |
| **Construct Plating** (`tyr_progression:hunter_construct_defense`) | 8 | 1 | 8 | Improve defense against authentic construct attacks |
| **Golem Breaker** (`tyr_progression:hunter_construct_mastery`) | 10 | 1 | 10 | Armor penetration against constructs if supported by native attribute and creature tags |

#### Beast specialty (30 points)
Free license: `tyr_progression:specialty_beast`

| Skill | Ranks | Cost/rank | Total | Purpose |
|---|---:|---:|---:|---|
| **Beast Slayer** (`tyr_progression:hunter_beast_slayer`) | 12 | 1 | 12 | Damage actual eligible wild beasts, not pets |
| **Beast Evasion** (`tyr_progression:hunter_beast_defense`) | 8 | 1 | 8 | Dodge real hostile beast attacks |
| **Apex Pursuit** (`tyr_progression:hunter_beast_mastery`) | 10 | 1 | 10 | Improve tracking/close pursuit of valid beast quarry |

#### Arthropod specialty (30 points)
Free license: `tyr_progression:specialty_arthropod`

| Skill | Ranks | Cost/rank | Total | Purpose |
|---|---:|---:|---:|---|
| **Arthropod Slayer** (`tyr_progression:hunter_arthropod_slayer`) | 12 | 1 | 12 | Damage spiders/arthropods by verified taxonomy |
| **Venom Guard** (`tyr_progression:hunter_arthropod_defense`) | 8 | 1 | 8 | Reduce arthropod-specific poison damage/effects |
| **Webbreaker** (`tyr_progression:hunter_arthropod_mastery`) | 10 | 1 | 10 | Improve mobility when trapped by valid spider web sources |

#### Plant specialty (30 points)
Free license: `tyr_progression:specialty_plant`

| Skill | Ranks | Cost/rank | Total | Purpose |
|---|---:|---:|---:|---|
| **Plant Slayer** (`tyr_progression:hunter_plant_slayer`) | 12 | 1 | 12 | Damage verified plant taxonomy including eligible Creepers |
| **Spore Guard** (`tyr_progression:hunter_plant_defense`) | 8 | 1 | 8 | Limit effects from verified botanical attacks, not all poison |
| **Creeper Ward** (`tyr_progression:hunter_plant_mastery`) | 10 | 1 | 10 | Reduce actual Creeper explosion source types only |

#### Elemental specialty (30 points)
Free license: `tyr_progression:specialty_elemental`

| Skill | Ranks | Cost/rank | Total | Purpose |
|---|---:|---:|---:|---|
| **Elemental Slayer** (`tyr_progression:hunter_elemental_slayer`) | 12 | 1 | 12 | Damage verified elemental prey |
| **Primordial Guard** (`tyr_progression:hunter_elemental_defense`) | 8 | 1 | 8 | Reduce actual tagged elemental-origin attack types with source evidence |
| **Elemental Analysis** (`tyr_progression:hunter_elemental_mastery`) | 10 | 1 | 10 | Boost effectiveness when countering selected verified elemental weaknesses |

#### Ooze specialty (30 points)
Free license: `tyr_progression:specialty_ooze`

| Skill | Ranks | Cost/rank | Total | Purpose |
|---|---:|---:|---:|---|
| **Ooze Slayer** (`tyr_progression:hunter_ooze_slayer`) | 12 | 1 | 12 | Damage verified slime/ooze prey |
| **Slime Resilience** (`tyr_progression:hunter_ooze_defense`) | 8 | 1 | 8 | Reduce legitimate ooze melee/slowness effects |
| **Viscous Tactics** (`tyr_progression:hunter_ooze_mastery`) | 10 | 1 | 10 | Improve mobility and attacks when engulfed/slowed by ooze |

#### Demon specialty (30 points)
Free license: `tyr_progression:specialty_demon`

| Skill | Ranks | Cost/rank | Total | Purpose |
|---|---:|---:|---:|---|
| **Demon Slayer** (`tyr_progression:hunter_demon_slayer`) | 12 | 1 | 12 | Damage verified demon prey |
| **Infernal Guard** (`tyr_progression:hunter_demon_defense`) | 8 | 1 | 8 | Reduce genuine demonic attacks using source IDs |
| **Exorcist** (`tyr_progression:hunter_demon_mastery`) | 10 | 1 | 10 | Improve combat against demon summons without harming player-owned servants |

**Hunter budget proof:** Any one character can purchase 62 shared ranks + one 8-rank Training Style + one 30-rank prey specialty = **100 ranks / 100 points**. The 57 paid options in the full catalog sum to 498 ranks only because mutually exclusive alternatives are shown together; a character cannot buy all those alternatives. Existing choices, switch behavior, refunds and specialty gating must be validated by the Progression integrator.

## Other eight specialist Paths and universal Core

Every rank in the following tables costs **1 point**. A `Full cost` of 5 means five separate purchased ranks, not one 5-point purchase. Numerical benefit per rank is a source-verified tuning task.

### Core Skills — 100 ranks / 100 points

*Native guardrails:* One global Core independent of Newbie/Normie and specialist identity; no Core rank duplicated in specialist trees.

#### Universal magic fundamentals — 37 points

| Skill | Number of 1-point ranks | Full cost | Design intent |
|---|---:|---:|---|
| Reservoir I (`tyr_progression:core_reservoir_i`) | 5 | 5 | Personal maximum mana/resources. Initial four runtime preview nodes preserve their original absolute rank-5 ceilings until per-mod base normalization is source-verified. |
| Wellspring I (`tyr_progression:core_wellspring_i`) | 5 | 5 | Personal magical regeneration; dragon forms may purchase this UNIVERSAL option, but have no dragon-exclusive magic regen nodes. |
| Thrift I (`tyr_progression:core_thrift_i`) | 5 | 5 | Discount personal magic payments; original resource-specific hard ceiling and minimum positive payment apply. |
| Potency I (`tyr_progression:core_potency_i`) | 5 | 5 | Base personal spell effect strength; source-specific spells and damage tags. Existing four preview attributes stay exact until normalized. |
| Quickening (`tyr_progression:core_quickening`) | 3 | 3 | Personal spell casting speed, not industrial throughput. |
| Projection (`tyr_progression:core_projection`) | 3 | 3 | Valid spell target range, not free global AoE. |
| Persistence (`tyr_progression:core_persistence`) | 5 | 5 | Duration of supported temporary effects and personally owned summons. |
| Conjuration (`tyr_progression:core_conjuration`) | 3 | 3 | Bounded strength of personally owned summoned allies, not free summoned mob count. |
| Warding (`tyr_progression:core_warding`) | 3 | 3 | Source-verified magical damage resistance, hard mitigation cap. |

#### Retained Tyz 6.4.2 source skills (proposed rehomes) — 63 points

| Skill | Number of 1-point ranks | Full cost | Design intent |
|---|---:|---:|---|
| Health Boost (`tyr_progression:core_health_boost`) | 5 | 5 | Increases maximum health by {value} half hearts |
| Oxygen Boost (`tyr_progression:core_oxygen_boost`) | 3 | 3 | Increases underwater breathing time by {value}% |
| Block Reach (`tyr_progression:core_block_reach`) | 2 | 2 | Increases the range at which the player can place and destroy by {value} blocks |
| Attack Damage (`tyr_progression:core_attack_damage`) | 3 | 3 | Increases melee damage by {value} |
| Sneaking Speed (`tyr_progression:core_sneaking_speed`) | 7 | 7 | Increases movement speed while sneaking by {value}% |
| speed_boost (`tyr_progression:core_speed_boost`) | 3 | 3 | NO ENGLISH STRING FOUND |
| Step Height (`tyr_progression:core_step_height`) | 2 | 2 | Allows stepping up blocks up to {value} blocks high without jumping |
| Swim Speed (`tyr_progression:core_swim_speed`) | 5 | 5 | Increases swim speed by {value}% |
| Excavation (`tyr_progression:core_excavation`) | 6 | 6 | Increases mining speed by {value}% |
| Swift Learning (`tyr_progression:core_swift_learning`) | 5 | 5 | Increases Skill XP gain by {value}% |
| Metabolism (`tyr_progression:core_metabolism`) | 3 | 3 | Increases food value (nutrition & saturation) by {nutrition_bonus}% |
| Wisdom (`tyr_progression:core_wisdom`) | 3 | 3 | Increases Vanilla XP gain by {bonus_percentage}% |
| Stealth (`tyr_progression:core_stealth`) | 5 | 5 | Reduces the detection range of hostile mobs by {range_reduction}% |
| Magnet (`tyr_progression:core_magnet`) | 2 | 2 | Attracts items within a {block_radius} block radius |
| Spare Parts (`tyr_progression:core_spare_parts`) | 2 | 2 | Grants a {refund_chance}% chance for each ingredient to be recovered when crafting. Up to {max_materials} item(s) per craft |
| Twist Of Fate (`tyr_progression:core_twist_of_fate`) | 3 | 3 | Increases the quantity of items found in structure loot chests by {loot_multiplier}% |
| Safekept (`tyr_progression:core_safekept`) | 2 | 2 | Safeguards the first {saved_slots} slot(s) of your inventory upon death |
| Haggler (`tyr_progression:core_haggler`) | 2 | 2 | Holding an Emerald attracts villagers within a {block_radius} block radius.  They will follow you with a {villager_speed}% speed bonus |

### Cave Dragon — 100 ranks / 100 points

*Native guardrails:* Keep JAWR/Goety/Dragon Survival authority over native physiology and progression; bought ranks enhance, not replace.

#### Universal Dragon Physiology & Sorcery (species-local) — 37 points

| Skill | Number of 1-point ranks | Full cost | Design intent |
|---|---:|---:|---|
| Draconic Reservoir (`tyr_progression:cave_dragon_draconic_reservoir`) | 6 | 6 | Massive bonus to personally usable maximum magic resource capacity; never machine/world storage. |
| Draconic Potency (`tyr_progression:cave_dragon_draconic_potency`) | 4 | 4 | Very high general personal spell potency per SP; additive by same reference with typed affinity, not multiplicative recursion. |
| Draconic Scales (`tyr_progression:cave_dragon_draconic_scales`) | 8 | 8 | Species natural armor; deliberately inefficient per SP versus specialized martial defense. |
| Draconic Claws (`tyr_progression:cave_dragon_draconic_claws`) | 8 | 8 | Real dragon form physical strike boost; not global magic damage. |
| Draconic Talons (`tyr_progression:cave_dragon_draconic_talons`) | 11 | 11 | Improve legal dragon claw harvest rate, not tool-tier bypass. |

#### Claw & Fang — 17 points

| Skill | Number of 1-point ranks | Full cost | Design intent |
|---|---:|---:|---|
| Stone Claws (`tyr_progression:cave_dragon_stone_claws`) | 8 | 8 | Scale unarmed/claw damage only in dragon body; no weapon-wide bonus. |
| Crushing Bite (`tyr_progression:cave_dragon_crushing_bite`) | 5 | 5 | Bounded armor/knockback pressure on confirmed bite hits. |
| Seismic Pounce (`tyr_progression:cave_dragon_seismic_pounce`) | 4 | 4 | Cooldown-limited landing shockwave against entities; terrain damage off by default. |

#### Stone Scales — 16 points

| Skill | Number of 1-point ranks | Full cost | Design intent |
|---|---:|---:|---|
| Stonehide (`tyr_progression:cave_dragon_stonehide`) | 8 | 8 | Incremental natural armor; respect equipment stacking caps. |
| Earthbound Poise (`tyr_progression:cave_dragon_earthbound_poise`) | 5 | 5 | Reduce knockback and fall damage while grounded. |
| Mountain Bastion (`tyr_progression:cave_dragon_mountain_bastion`) | 3 | 3 | Short defensive stance with a resource cost and meaningful cooldown, not permanent immunity. |

#### Excavation — 19 points

| Skill | Number of 1-point ranks | Full cost | Design intent |
|---|---:|---:|---|
| Tunneler (`tyr_progression:cave_dragon_tunneler`) | 11 | 11 | Improve legal claw-mining speed; preserve vanilla/tool harvest checks. |
| Vein Sense (`tyr_progression:cave_dragon_vein_sense`) | 5 | 5 | Short-range, timed ore awareness without unrestricted x-ray or chunk scans. |
| Deep Excavation (`tyr_progression:cave_dragon_deep_excavation`) | 3 | 3 | Controlled multi-block excavation; count actual blocks, honor claims and server limits. |

#### Infernal Attunement — 11 points

| Skill | Number of 1-point ranks | Full cost | Design intent |
|---|---:|---:|---|
| Infernal Resonance (`tyr_progression:cave_dragon_infernal_resonance`) | 6 | 6 | Bonus to source-verified demonic/infernal personal magic, beyond shared dragon arcane power but not world rituals. |
| Hellbound Command (`tyr_progression:cave_dragon_hellbound_command`) | 3 | 3 | Strengthen eligible player-owned demonic servants and source-verified infernal spell interactions. |
| Infernal Dominion (`tyr_progression:cave_dragon_infernal_dominion`) | 2 | 2 | Late specialization in demon-affiliated spell potency; strong, narrowly classified and gated. |

### Sea Dragon — 100 ranks / 100 points

*Native guardrails:* Keep JAWR/Goety/Dragon Survival authority over native physiology and progression; bought ranks enhance, not replace.

**Abyssal Fortune:** 50 one-point ranks = 50 points. The earlier 10-rank ×5-point idea was invalid. If lower rank counts are desirable, create genuinely different Luck abilities to share this investment; no dummy tier division or escalating rank costs.

#### Universal Dragon Physiology & Sorcery (species-local) — 19 points

| Skill | Number of 1-point ranks | Full cost | Design intent |
|---|---:|---:|---|
| Draconic Reservoir (`tyr_progression:sea_dragon_draconic_reservoir`) | 3 | 3 | Massive bonus to personally usable maximum magic resource capacity; never machine/world storage. |
| Draconic Potency (`tyr_progression:sea_dragon_draconic_potency`) | 2 | 2 | Very high general personal spell potency per SP; additive by same reference with typed affinity, not multiplicative recursion. |
| Draconic Scales (`tyr_progression:sea_dragon_draconic_scales`) | 4 | 4 | Species natural armor; deliberately inefficient per SP versus specialized martial defense. |
| Draconic Claws (`tyr_progression:sea_dragon_draconic_claws`) | 4 | 4 | Real dragon form physical strike boost; not global magic damage. |
| Draconic Talons (`tyr_progression:sea_dragon_draconic_talons`) | 6 | 6 | Improve legal dragon claw harvest rate, not tool-tier bypass. |

#### Tideshaping — 8 points

| Skill | Number of 1-point ranks | Full cost | Design intent |
|---|---:|---:|---|
| Current Rider (`tyr_progression:sea_dragon_current_rider`) | 4 | 4 | Improve movement control in water, including currents. |
| Tidal Strike (`tyr_progression:sea_dragon_tidal_strike`) | 2 | 2 | Enhance wet-target combat and native aquatic attacks, not generic damage. |
| Undertow (`tyr_progression:sea_dragon_undertow`) | 2 | 2 | Cooldown-limited pull/slow applied to legal targets without moving world water blocks. |

#### Abyssal Defense — 8 points

| Skill | Number of 1-point ranks | Full cost | Design intent |
|---|---:|---:|---|
| Pressure Adaptation (`tyr_progression:sea_dragon_pressure_adaptation`) | 4 | 4 | Reduce underwater environmental penalties. |
| Brine Scales (`tyr_progression:sea_dragon_brine_scales`) | 2 | 2 | Situational protection while submerged; no blanket armor bonus on land. |
| Deepwater Refuge (`tyr_progression:sea_dragon_deepwater_refuge`) | 2 | 2 | Bounded underwater healing or defense with a meaningful resource/cooldown cost. |

#### Swimmer's Grace — 9 points

| Skill | Number of 1-point ranks | Full cost | Design intent |
|---|---:|---:|---|
| Sleek Form (`tyr_progression:sea_dragon_sleek_form`) | 5 | 5 | Increase underwater speed, capped against sprint/exotic gear. |
| Slipstream (`tyr_progression:sea_dragon_slipstream`) | 2 | 2 | Short burst dodge in water with collision safety. |
| Ocean Leap (`tyr_progression:sea_dragon_ocean_leap`) | 2 | 2 | Leap from water and control reentry without free flight. |

#### Tidal & Abyssal Magic — 6 points

| Skill | Number of 1-point ranks | Full cost | Design intent |
|---|---:|---:|---|
| Tidal Attunement (`tyr_progression:sea_dragon_tidal_attunement`) | 3 | 3 | Enhance verified water, ocean and cold-themed personal spell damage/effects, not arbitrary wet status. |
| Stormcalling (`tyr_progression:sea_dragon_stormcalling`) | 2 | 2 | Enhance verified lightning/storm magic and aquatic control where supported; no unconditional world weather control. |
| Abyssal Sovereignty (`tyr_progression:sea_dragon_abyssal_sovereignty`) | 1 | 1 | High-level sea/abyssal spell focus, not an assumption that Occultism automatically counts as ocean magic. |

#### Abyssal Fortune — 50 points

| Skill | Number of 1-point ranks | Full cost | Design intent |
|---|---:|---:|---|
| Abyssal Fortune (`tyr_progression:sea_dragon_abyssal_fortune`) | 50 | 50 | Extremely high Sea Dragon luck; powerful personal Luck-stat and source-verified luck-sensitive results, but no unconditional duplicate item generation or boss/quest gate bypass. |

### Forest Dragon — 100 ranks / 100 points

*Native guardrails:* Keep JAWR/Goety/Dragon Survival authority over native physiology and progression; bought ranks enhance, not replace.

#### Universal Dragon Physiology & Sorcery (species-local) — 37 points

| Skill | Number of 1-point ranks | Full cost | Design intent |
|---|---:|---:|---|
| Draconic Reservoir (`tyr_progression:forest_dragon_draconic_reservoir`) | 6 | 6 | Massive bonus to personally usable maximum magic resource capacity; never machine/world storage. |
| Draconic Potency (`tyr_progression:forest_dragon_draconic_potency`) | 4 | 4 | Very high general personal spell potency per SP; additive by same reference with typed affinity, not multiplicative recursion. |
| Draconic Scales (`tyr_progression:forest_dragon_draconic_scales`) | 8 | 8 | Species natural armor; deliberately inefficient per SP versus specialized martial defense. |
| Draconic Claws (`tyr_progression:forest_dragon_draconic_claws`) | 8 | 8 | Real dragon form physical strike boost; not global magic damage. |
| Draconic Talons (`tyr_progression:forest_dragon_draconic_talons`) | 11 | 11 | Improve legal dragon claw harvest rate, not tool-tier bypass. |

#### Verdant Vitality — 17 points

| Skill | Number of 1-point ranks | Full cost | Design intent |
|---|---:|---:|---|
| Green Recovery (`tyr_progression:forest_dragon_green_recovery`) | 8 | 8 | Small recovery benefit in living biomes, not in all dimensions. |
| Living Bark (`tyr_progression:forest_dragon_living_bark`) | 5 | 5 | Situational natural defense near vegetation, with stacking caps. |
| Elder Grove (`tyr_progression:forest_dragon_elder_grove`) | 4 | 4 | Time-limited regeneration/support pulse, costing dragon resource. |

#### Thorn & Root — 12 points

| Skill | Number of 1-point ranks | Full cost | Design intent |
|---|---:|---:|---|
| Bramble Claw (`tyr_progression:forest_dragon_bramble_claw`) | 6 | 6 | Add a bounded nature-themed status to confirmed claw attacks. |
| Entangling Breath (`tyr_progression:forest_dragon_entangling_breath`) | 4 | 4 | Native breath enhancer applying short slows only where source breath supports it. |
| Thornstorm (`tyr_progression:forest_dragon_thornstorm`) | 2 | 2 | Cooldown-limited small-area crowd control with no world block destruction. |

#### Fertility & Bounty — 12 points

| Skill | Number of 1-point ranks | Full cost | Design intent |
|---|---:|---:|---|
| Germination Touch (`tyr_progression:forest_dragon_germination_touch`) | 4 | 4 | Enhance actual bonemeal application efficiency/area/yield without free unbounded creation of growth. |
| Breeder's Blessing (`tyr_progression:forest_dragon_breeders_blessing`) | 4 | 4 | Improve legal animal breeding/cooldown or food efficiency via real pair interaction; never spawn animals without parents. |
| Harvest Bounty (`tyr_progression:forest_dragon_harvest_bounty`) | 4 | 4 | Increase real crop harvest outcomes with explicit per-block credit and cooldown/caps; preserve claim and mature crop checks. |

#### Fae Affinity — 12 points

| Skill | Number of 1-point ranks | Full cost | Design intent |
|---|---:|---:|---|
| Fey Courtesy (`tyr_progression:forest_dragon_fey_courtesy`) | 6 | 6 | Improve source-verified fae-aligned personal magic effects and fae relations; no universal mob pacification. |
| Fey Conduit (`tyr_progression:forest_dragon_fey_conduit`) | 4 | 4 | Specialize fae-aligned spell potency and owned fae summons at higher benefit per SP. |
| Grove Guardian (`tyr_progression:forest_dragon_grove_guardian`) | 2 | 2 | Bounded fae-aligned personal magic and allied-support synergy without taking over world production. |

#### Retained Tyz 6.4.2 source skills — 10 points

| Skill | Number of 1-point ranks | Full cost | Design intent |
|---|---:|---:|---|
| Venomous Strike (`tyr_progression:forest_dragon_venomous_strike`) | 4 | 4 | Adds a {success_probability}% chance to poison targets on hit |
| Green Thumb (`tyr_progression:forest_dragon_green_thumb`) | 3 | 3 | Adds a {success_probability}% chance to double drops when harvesting crops and plants |
| Shepherd’s Blessing (`tyr_progression:forest_dragon_shepherds_blessing`) | 3 | 3 | Baby animals grow {growth_speed}% faster |

### Vampire — 100 ranks / 100 points

*Native guardrails:* Keep JAWR/Goety/Dragon Survival authority over native physiology and progression; bought ranks enhance, not replace.

#### Bloodcraft — 18 points

| Skill | Number of 1-point ranks | Full cost | Design intent |
|---|---:|---:|---|
| Efficient Feeding (`tyr_progression:vampire_efficient_feeding`) | 9 | 9 | Improve real blood gained from eligible feeding transactions; cannot create blood from empty actions. |
| Deep Reservoir (`tyr_progression:vampire_deep_reservoir`) | 5 | 5 | Increase one authoritative usable blood capacity, not two independent HUD pools. |
| Crimson Renewal (`tyr_progression:vampire_crimson_renewal`) | 4 | 4 | Trade a real measured blood debit for bounded emergency healing. |

#### Nightstalker — 21 points

| Skill | Number of 1-point ranks | Full cost | Design intent |
|---|---:|---:|---|
| Nocturnal Vision (`tyr_progression:vampire_nocturnal_vision`) | 12 | 12 | Better low-light perception with native-sunlight penalties untouched. |
| Shadow Stalker (`tyr_progression:vampire_shadow_stalker`) | 5 | 5 | Short stealth advantage in darkness; avoid invisibility in full daylight. |
| Bat Mastery (`tyr_progression:vampire_bat_mastery`) | 4 | 4 | Improve native bat-form controls/efficiency after that native ability is acquired. |

#### Predation — 17 points

| Skill | Number of 1-point ranks | Full cost | Design intent |
|---|---:|---:|---|
| Fang Training (`tyr_progression:vampire_fang_training`) | 8 | 8 | Scale only verified vampire bite/melee attacks. |
| Measured Feast (`tyr_progression:vampire_measured_feast`) | 5 | 5 | Limited post-kill blood recovery from eligible kills, with transaction deduplication. |
| Blood Duelist (`tyr_progression:vampire_blood_duelist`) | 4 | 4 | Focused single-target bonuses under low blood; prevent intentional zero-cost loops. |

#### Undead Constitution — 17 points

| Skill | Number of 1-point ranks | Full cost | Design intent |
|---|---:|---:|---|
| Pale Resilience (`tyr_progression:vampire_pale_resilience`) | 8 | 8 | Bounded poison/disease-type mitigation where the source system allows it. |
| Sunshroud (`tyr_progression:vampire_sunshroud`) | 5 | 5 | Short resource-funded sunlight resistance; never erase core daylight tradeoffs. |
| Elder Presence (`tyr_progression:vampire_elder_presence`) | 4 | 4 | Bounded defensive/control aura with cooldown, not full charm immunity. |

#### Retained Vampire Skill Concepts (JAWR-owned) — 22 points

| Skill | Number of 1-point ranks | Full cost | Design intent |
|---|---:|---:|---|
| Blood Thrift (`tyr_progression:vampire_blood_thrift`) | 5 | 5 | Reduce only real JAWR blood expenditure/hunger cost on verified committed actions. |
| Predatory Finesse (`tyr_progression:vampire_predatory_finesse`) | 5 | 5 | Blood-funded vampire-specific melee attack speed, not spell casting speed. |
| Night Runner (`tyr_progression:vampire_night_runner`) | 8 | 8 | Improved vampire walking/running mobility in darkness; no universal free speed. |
| Crimson Surge (`tyr_progression:vampire_crimson_surge`) | 4 | 4 | Blood-paid high-stage predatory burst, supplementing Blood Regent without minions. |

#### Retained Tyz 6.4.2 source skills — 5 points

| Skill | Number of 1-point ranks | Full cost | Design intent |
|---|---:|---:|---|
| Bloodlust (`tyr_progression:vampire_bloodlust`) | 5 | 5 | Killing a hostile mob regenerates {health_percentage}% of its max health |

### Werewolf — 100 ranks / 100 points

*Native guardrails:* Keep JAWR/Goety/Dragon Survival authority over native physiology and progression; bought ranks enhance, not replace.

#### Lupine Vitality — 17 points

| Skill | Number of 1-point ranks | Full cost | Design intent |
|---|---:|---:|---|
| Feral Vigor (`tyr_progression:werewolf_feral_vigor`) | 8 | 8 | Large additional maximum health / extra hearts, materially more per SP than universal vitality boosts. |
| Regenerative Flesh (`tyr_progression:werewolf_regenerative_flesh`) | 5 | 5 | Strong, bounded actual HP regeneration under verified werewolf physiology and legal feeding/hunger rules; not mana regen. |
| Unbreakable Beast (`tyr_progression:werewolf_unbreakable_beast`) | 4 | 4 | High-level extra HP and in-combat wound recovery with caps and meaningful werewolf vulnerabilities. |

#### Feral Might — 17 points

| Skill | Number of 1-point ranks | Full cost | Design intent |
|---|---:|---:|---|
| Rending Claws (`tyr_progression:werewolf_rending_claws`) | 8 | 8 | Scale claw damage in valid wolf/beast form, respecting weapon modifiers. |
| Savage Momentum (`tyr_progression:werewolf_savage_momentum`) | 5 | 5 | Short stacking melee bonus after actual hits, capped and reset out of combat. |
| Primal Fury (`tyr_progression:werewolf_primal_fury`) | 4 | 4 | Timed burst consuming native appropriate resource or applying fatigue. |

#### Moonrunner — 17 points

| Skill | Number of 1-point ranks | Full cost | Design intent |
|---|---:|---:|---|
| Predator's Stride (`tyr_progression:werewolf_predators_stride`) | 8 | 8 | Bonus mobility in valid wolf form. |
| Pouncing Leap (`tyr_progression:werewolf_pouncing_leap`) | 5 | 5 | Targeted controlled leap with landing safety. |
| Lunar Chase (`tyr_progression:werewolf_lunar_chase`) | 4 | 4 | Short chase burst under moon conditions with weaker daytime counterpart. |

#### Wild Communion — 16 points

| Skill | Number of 1-point ranks | Full cost | Design intent |
|---|---:|---:|---|
| Wild Attunement (`tyr_progression:werewolf_wild_attunement`) | 8 | 8 | Increase eligible nature magic potency (Botania/Roots personal effects) while werewolf state is valid. |
| Rootsong (`tyr_progression:werewolf_rootsong`) | 5 | 5 | Further increase player nature spell potency or healing, separate from ordinary combat regeneration. |
| Grove Howl (`tyr_progression:werewolf_grove_howl`) | 3 | 3 | High-level synergy between wolf pack allies and personally cast nature magic, bounded to eligible owners/effects. |

#### Retained Lycan Skill Concepts (JAWR-owned) — 28 points

| Skill | Number of 1-point ranks | Full cost | Design intent |
|---|---:|---:|---|
| Bloodlust Recovery (`tyr_progression:werewolf_bloodlust_recovery`) | 5 | 5 | Health restoration from actual player-credited hostile kill, with real cooldown. |
| Bite Techniques (`tyr_progression:werewolf_bite_techniques`) | 5 | 5 | Select either limited bleeding or brief stun enhancement on confirmed wolf bites. |
| Feral Excavation (`tyr_progression:werewolf_feral_excavation`) | 8 | 8 | Boost permitted claw mining/harvest throughput while in real JAWR wolf form. |
| Arrow Awareness (`tyr_progression:werewolf_arrow_awareness`) | 5 | 5 | Limited moving-projectile evasion with shared evasion budget. |
| Tempered Against Silver (`tyr_progression:werewolf_tempered_against_silver`) | 5 | 5 | Partial damage mitigation against source-verified silver damage without erasing weakness. |

#### Retained Tyz 6.4.2 source skills — 5 points

| Skill | Number of 1-point ranks | Full cost | Design intent |
|---|---:|---:|---|
| Rage (`tyr_progression:werewolf_rage`) | 5 | 5 | Increases damage by {damage_buff}% when health is below {health_threshold}% |

### Lich — 100 ranks / 100 points

*Native guardrails:* Shared ONE Lich tree for either provider/Archlich; native death priority is unchanged.

#### Soul Economy — 25 points

| Skill | Number of 1-point ranks | Full cost | Design intent |
|---|---:|---:|---|
| Soul Conservation (`tyr_progression:lich_soul_conservation`) | 12 | 12 | Lower eligible personal necromancy spell costs, not global ritual/machine cost. |
| Soul Reserve (`tyr_progression:lich_soul_reserve`) | 8 | 8 | Bounded native personal soul/Arca resource capacity where extensible. |
| Soul Treasury (`tyr_progression:lich_soul_treasury`) | 5 | 5 | Small high-level sustain on committed necromantic actions, no passive self-funding loop. |

#### Blood & Nature Opposition — 25 points

| Skill | Number of 1-point ranks | Full cost | Design intent |
|---|---:|---:|---|
| Sanguine Attunement (`tyr_progression:lich_sanguine_attunement`) | 12 | 12 | Improve verified blood-based personal magic (Neo Vitae/EvilCraft where actual hooks apply) while applying a meaningful reduction to this lich's personal Botania/Roots nature spell power. |
| Crimson Channel (`tyr_progression:lich_crimson_channel`) | 8 | 8 | Strengthen source-verified blood magic further with proportionate nature-magic potency tradeoff; not passive blood machine throughput. |
| Verdant Severance (`tyr_progression:lich_verdant_severance`) | 5 | 5 | High blood-magic specialization at a stronger self-inflicted Botania/Roots nature power penalty; no world-machine or other-player debuff. |

#### Undead Host — 25 points

| Skill | Number of 1-point ranks | Full cost | Design intent |
|---|---:|---:|---|
| Bone Captain (`tyr_progression:lich_bone_captain`) | 12 | 12 | Modest bonus to personally owned undead servants. |
| Grave Regiment (`tyr_progression:lich_grave_regiment`) | 8 | 8 | Improved servant coordination/leash and bounded count efficiency. |
| Death Host (`tyr_progression:lich_death_host`) | 5 | 5 | Late temporary enhancement to current owned minions with hard entity budget. |

#### Soul Preservation — 25 points

| Skill | Number of 1-point ranks | Full cost | Design intent |
|---|---:|---:|---|
| Cold Persistence (`tyr_progression:lich_cold_persistence`) | 12 | 12 | Limited tolerance of wither/undead hazards, preserving explicit weaknesses. |
| Anchor Attunement (`tyr_progression:lich_anchor_attunement`) | 8 | 8 | Improve diagnostics or controlled anchor maintenance; never bypass native phylactery/Arca cost. |
| Final Passage (`tyr_progression:lich_final_passage`) | 5 | 5 | Minor post-rescue recovery *after* an authoritative successful resurrection; no extra death-save trigger. |

### Gilded — 100 ranks / 100 points

*Native guardrails:* FREE native/Path abilities: Nether golden-armor+gold-food successful initiation, baseline diamond-equivalent gold, +3 virtual Fortune and +3 virtual Looting, Piglin/Brute peace, immediate lava-fishing license, rank18 full fire-tag immunity. Do not charge points for these again.

#### Goldcraft — 44 points

| Skill | Number of 1-point ranks | Full cost | Design intent |
|---|---:|---:|---|
| Fortune Mastery (`tyr_progression:gilded_fortune_mastery`) | 7 | 7 | Increase legal gold/Netherite virtual Fortune +3→+10, one per purchase; Silk prevails. |
| Looting Mastery (`tyr_progression:gilded_looting_mastery`) | 7 | 7 | Increase golden sword/axe direct-melee virtual Looting +3→+10, independent of Fortune. |
| Lasting Gilt (`tyr_progression:gilded_lasting_gilt`) | 10 | 10 | Raise virtual Unbreaking on equipped gold/Netherite gear only. |
| Golden Renewal (`tyr_progression:gilded_golden_renewal`) | 10 | 10 | Increase actual equipped gold/Netherite repair at equal rates. |
| Gilded Grip Mastery (`tyr_progression:gilded_gilded_grip_mastery`) | 10 | 10 | Supplement free diamond-equivalence with bounded legitimate equipped-gold handling; do not repurchase free parity. |

#### Nether Society — 45 points

| Skill | Number of 1-point ranks | Full cost | Design intent |
|---|---:|---:|---|
| Piglin Diplomacy (`tyr_progression:gilded_piglin_diplomacy`) | 5 | 5 | Improve barter/trade interactions while preserving baseline Piglin/Brute friendliness. |
| Lava Angler (`tyr_progression:gilded_lava_angler`) | 10 | 10 | Improve lawful lava catch utility; baseline fishing stays free. |
| Gold Nourishment (`tyr_progression:gilded_gold_nourishment`) | 10 | 10 | Improve recovery from gold-qualified food without undoing locked restrictions or duplicating potion effects. |
| Nether Affinity (`tyr_progression:gilded_nether_affinity`) | 10 | 10 | Bounded environmental movement and defense in Nether; separate from rank18 fire immunity. |
| Metallurgist (`tyr_progression:gilded_metallurgist`) | 10 | 10 | Improve verified golden tool/weapon performance without altering global item tiers. |

#### Sovereignty — 11 points

| Skill | Number of 1-point ranks | Full cost | Design intent |
|---|---:|---:|---|
| Glittering Luck (`tyr_progression:gilded_glittering_luck`) | 6 | 6 | True player Luck improvements, not virtual Fortune or Looting. |
| Gilded Sovereign (`tyr_progression:gilded_gilded_sovereign`) | 5 | 5 | Proposed post-Nether late-game golden mastery capstone; exact effect requires owner review. |

### Bare Zerker — 100 ranks / 100 points

*Native guardrails:* Free locked innate: Fury capacity100; +Fury from actual post-mitigation damage DEALT only; 1 Fury/sec decay; each actual received hit adds 1s Regeneration I+Resistance I; >50 pre-hit Fury would-be death triggers full vanilla Totem and drains all Fury. Do not sell innate abilities or alter their formulas. Chest chafing, allowed clothes, Red Cap and woad are separate form mechanics.

#### Fury Combat — 40 points

| Skill | Number of 1-point ranks | Full cost | Design intent |
|---|---:|---:|---|
| Reckless Sword (`tyr_progression:bare_zerker_reckless_sword`) | 10 | 10 | Improve shieldless sword damage under approved chest-empty rules, Fury still only from actual dealt HP. |
| Cleaving Axe (`tyr_progression:bare_zerker_cleaving_axe`) | 10 | 10 | Improve shieldless axe damage, no new source of free Fury. |
| Battle Rhythm (`tyr_progression:bare_zerker_battle_rhythm`) | 10 | 10 | Bounded consecutive real-hit damage bonuses, no proc from mere incoming hits. |
| Bloodied Drive (`tyr_progression:bare_zerker_bloodied_drive`) | 10 | 10 | Brief valid kill-triggered combat momentum, no Fury injections. |

#### Endurance Under Fire — 30 points

| Skill | Number of 1-point ranks | Full cost | Design intent |
|---|---:|---:|---|
| Skin Hardened (`tyr_progression:bare_zerker_skin_hardened`) | 10 | 10 | Actual innate mitigation while chest-empty, no conventional armor buffs. |
| Regeneration Amplifier (`tyr_progression:bare_zerker_regen_amplifier`) | 10 | 10 | Improve amplifier of owner-approved incoming-hit regeneration, preserving +1s per hit duration. |
| Resistance Amplifier (`tyr_progression:bare_zerker_resistance_amplifier`) | 10 | 10 | Improve amplifier of owner-approved incoming-hit Resistance, preserving +1s per hit duration. |

#### Blood and Woad — 20 points

| Skill | Number of 1-point ranks | Full cost | Design intent |
|---|---:|---:|---|
| Red Cap Fortitude (`tyr_progression:bare_zerker_redcap_fortitude`) | 5 | 5 | Improve blood-dipped Red Cap temporary powers; duration/cost owner review. |
| Woad Attunement (`tyr_progression:bare_zerker_woad_attunement`) | 5 | 5 | Enhance source-verified Neo Vitae Sentient Armor-equivalent woad benefits without equipping armor. |
| Neo Vitae Bloodmaster (`tyr_progression:bare_zerker_neo_bloodmaster`) | 5 | 5 | Improve verified Neo Vitae blood magic interactions, with genuine costs. |
| EvilCraft Hematurgy (`tyr_progression:bare_zerker_evilcraft_bloodcraft`) | 5 | 5 | Improve verified EvilCraft blood transactions/ritual effects with no duplicate blood credit. |

#### Berserk Mastery — 10 points

| Skill | Number of 1-point ranks | Full cost | Design intent |
|---|---:|---:|---|
| Antimagic Grit (`tyr_progression:bare_zerker_antimagic_grit`) | 5 | 5 | Bounded mitigation for specified applied incoming magic damage types, not mod-caster origin. |
| Last Stand Mastery (`tyr_progression:bare_zerker_last_stand_mastery`) | 5 | 5 | Improve recovery after real Too Angry to Die event, without altering >50 rule, full totem effects, or complete Fury drain. |

## Newer Path shells — deliberately not priced

The design-only identities below have owner-decisions outstanding. We do not assign fake points or active ranks to them before their mechanics are agreed.

| Path | Proposed lanes | Unresolved design |
|---|---|---|
| **Sapient Slime** | Body Mass / Biomass; Reactive Allied Slimes; Absorption and Ooze Kinship | real initiation; biomass cap; spawn bound and loot/XP; entity ownership; size/player rendering |
| **Sexy Demon (Incubus/Succubus)** | Infernal Body; Ranged Charm (one mob, 600 ticks); Right-click Permanent Taming; Companion Command | tame target allowlist; cost; max followers; charm cast/delivery; AI and raids |
| **Dryad** | Photosynthesis and Saturation; Greening Footsteps; Plant/Fae Diplomacy; Nature Magic | initiation; hunger/saturation scale; terrain and claims; precise fire weakness; magic affinities |
| **Spawn** | Shared natural armor and regeneration trunk; Cephalid; Deep One; Shoggoth; Mi-Go | mutation slot number; actual anatomical skills; initiation; respec/persistence; appearance; choice/exclusivity |

## Implementation and economy guardrails

- Source rank-cost tests must enforce **`price(rank) === 1` for all purchasable Path ranks** in the approved mode. No implicit `base_cost + step_cost * rank` escalation for individual skill purchases.
- Per-path total purchase budget must be computed over **simultaneously eligible skill sets**, not all incompatible options. Hunter choice groups must enforce exactly one paid training branch and one paid prey branch; respec must not allow duplicated refunds or cross-specialty stacking.
- Verify old live player saves before migrating Hunter from 238-point prices; do not silently change existing earned/spent balances, free selections, or invalidate achievements.
- The actual strength per point has to be source-calibrated, rather than blindly dividing/multiplying old effect amounts. Sea Dragon Luck in particular must respect the optimized gear/Curios comparison and Monk's agreed 85% relation.
- Gilded retains the owner-established independently seven-rank Fortune and Looting upgrades; native +3 to each remains free. Dragons and JAWR/Goety forms retain physiology and state authority; Bare Zerker Fury/Totem are innate, not new paid abilities.
- Nothing in this design PR edits FTB Quests, coins, progression runtime, actual Path configs, client, Prism or integration state. A point-shop proposal is separate, requires a trusted server bridge and explicit coin pricing approval.

**Related:** [PR #495](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/495), [Monk #478](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/478), [MXP #475](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/475), [Hunter QA #271](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/271).
