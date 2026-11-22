# TMM Progression — remaining Path trees, one point per rank

**DESIGN ONLY · November 22, 2026 · source baseline `dev/progression`**

Authoritative ledger: `REMAINING-PATHS-100PT-FIRST-PASS.json` schema `tmm_remaining_path_tree_blueprint_v8`. None of these drafted purchases, balances or source provider hooks are installed in-game by this PR.

## Owner-locked budget accounting

- **One advancement point purchases exactly one skill rank. There are NO 2-point, 3-point, 4-point or 5-point ranks.** A five-point capstone must have five incremental 1-point ranks, or be split into genuinely distinct skills; it is not one costly rank.
- For non-Monk specialist trees, **the mastery target is 100 purchasable one-point ranks**. Monk is the established 1,000-point exception. **Sea Dragon's completed draft now has 100 individually priced ranks** across 22 abilities, only ten of which belong to Luck; numerical effects remain to be source-verified.
- When a tree displays mutually exclusive alternatives, only the compatible combination available to **one character** needs to cost 100 points. Alternative specializations may make the entire displayed catalog exceed 100.
- Relative to the original 300–500-point trees, the intended effect per advancement point rises. **Do not** accomplish 'fewer levels' by charging more than one point per rank: if a named skill should have fewer ranks, redistribute its remaining points among distinct useful abilities.
- Some per-skill totals still span 10–50 ranks. Those are *one-point increments*, and the intended cumulative power remains a balancing target to verify against the actual provider and gear, **not** a promise the previous absolute `per_rank` can be copied unchanged.
- Shared global MXP wallet remains the planned default; configurable hybrid/separate modes remain under #475. Free innate abilities, native transformation states, and separately purchased Core ranks are not sold twice.

## Summary

| Tree | Displayed paid skills | Cost to master one eligible build |
|---|---:|---:|
| Core Skills | 27 | **100** |
| Cave Dragon | 17 | **100** |
| Sea Dragon | 22 | **100** |
| Forest Dragon | 20 | **100** |
| Vampire | 19 | **100** |
| Werewolf | 20 | **100** |
| Lich | 21 | **100** |
| Hunter | 57 | **100** |
| Gilded | 12 | **100** |
| Bare Zerker | 13 | **100** |

The ten draft catalogs display **228 paid skill options**; 57 are Hunter alternatives (not simultaneously eligible). Newbie and Normie remain baseline Paths with no independent purchasable ranks; Core is universal, not a specialist Path.

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

**Full balance-pass rewrite, not proportional redistribution.** The old 50-point Luck investment is **obsolete**. One 10-rank Abyssal Fortune skill delivers the complete intended extreme Luck ability at **10 points total**; 90 points buy independent Sea Dragon combat, movement, survivability, dragon body and magic capabilities. This is a **source-review candidate**, not runnable JSON.

[Complete Sea Dragon balance rationale and QA matrix](SEA-DRAGON-COMPLETE-100PT-2026-11-22.md).

| Branch | Skill | Ranks | Cost per rank | Candidate effect |
|---|---|---:|---:|---|
| Dragon Body & Personal Sorcery | **Draconic Reservoir** | 6 | **1** | +10% per rank to eligible personal magic resource maximums (+60% full); never to machinery/world tanks. |
| Dragon Body & Personal Sorcery | **Draconic Potency** | 5 | **1** | +4% per rank eligible personally cast spell potency (+20% full); do not multiply by aquatic affinity recursively. |
| Dragon Body & Personal Sorcery | **Draconic Scales** | 3 | **1** | +1 natural armor per rank (+3 total) while in verified Sea Dragon form; does not sell native scales again. |
| Dragon Body & Personal Sorcery | **Draconic Claws** | 3 | **1** | +1 physical attack damage per rank (+3) on confirmed native dragon claw attacks only. |
| Dragon Body & Personal Sorcery | **Draconic Talons** | 3 | **1** | +15% per rank claw mining speed (+45%) only for blocks native Sea Dragon can already harvest; no new tier permissions. |
| Oceanic Movement | **Sleek Form** | 5 | **1** | +8% per rank normal submerged swim speed (+40% full), within configured movement cap; distinct from water-flow handling. |
| Oceanic Movement | **Current Rider** | 5 | **1** | +6% per rank effective swimming movement against verified flowing water/current forces (+30% full), not a blanket speed multiplier. |
| Oceanic Movement | **Slipstream** | 4 | **1** | +2% dodge chance per rank (+8%) against real qualifying direct hits while actively swimming; no immunity, no independent dash action. |
| Oceanic Movement | **Ocean Leap** | 4 | **1** | +0.25 block per rank (+1 block) legal water-exit jump height or native breach control; no extra flight, wall bypass or free creative movement. |
| Tideshaping Combat | **Tidal Strike** | 6 | **1** | +3% per rank confirmed melee damage (+18%) when Sea Dragon and prey are in water; no boost to unrelated ranged attacks. |
| Tideshaping Combat | **Undertow** | 4 | **1** | On confirmed native claw/bite hit while swimming, apply Slowness I for +0.5 s per rank (2 s full) to valid enemies, at most once per 40 ticks per target; no new cast button. |
| Tideshaping Combat | **Storm Channel** | 5 | **1** | +4% per rank (+20%) to verified *native* Sea Dragon lightning/breath damage when the relevant ability has been earned; no free weather control or invented storm attack. |
| Tideshaping Combat | **Wakebreaker** | 3 | **1** | +5% per rank native Flight Spin contact damage (+15%) *while submerged*, after native Spin is legitimately unlocked; no new active attack. |
| Abyssal Survival | **Brine Scales** | 5 | **1** | Reduce remaining eligible direct physical damage by 3% per rank (15% full) while submerged; no invulnerability or extra damage outside water. |
| Abyssal Survival | **Deepwater Refuge** | 4 | **1** | Recover +0.2 HP/s per rank (+0.8 HP/s) while submerged after 8 s without hostile damage; never heals through lethal hit or duplicates totem effect. |
| Abyssal Survival | **Dryland Endurance** | 5 | **1** | Increase real native Sea Dragon safe dry-land hydration duration by 10% per rank (+50% full); dehydration still occurs and cannot be disabled. |
| Abyssal Survival | **Pressure Adaptation** | 3 | **1** | +20% per rank (+60%) underwater mining throughput where native water penalty exists; no fictitious vanilla 'pressure damage' reduction and no harvest-tier bypass. |
| Tidal & Abyssal Magic | **Tidal Attunement** | 5 | **1** | +4% per rank (+20%) to *verified* player-cast water/cold spell effects; spell school IDs must be traced from installed providers. |
| Tidal & Abyssal Magic | **Stormcalling** | 4 | **1** | +4% per rank (+16%) to *verified* player-cast lightning/storm spell effects; does not grant control of thunderstorms or any new cast button. |
| Tidal & Abyssal Magic | **Conduit Resonance** | 4 | **1** | -3% real personal mana spend per rank (-12% full) only while the actual vanilla Conduit Power effect is active; MXP credit remains based on real, source-verified nominal cost. |
| Tidal & Abyssal Magic | **Abyssal Sovereignty** | 4 | **1** | -5% per rank verified native Sea Dragon breath cooldown (-20% full) while submerged, capped by the source mod's minimum cooldown; no new native permissions. |
| Abyssal Fortune | **Abyssal Fortune** | 10 | **1** | TEN ranks, ONE point each, for the ENTIRE extreme Sea Dragon Luck capability. Each rank grants 1/10 of the *newly calibrated* final native Luck target; legitimate Luck-sensitive vanilla/modded results only, no raw drop duplication. |

| **Total** | **22 paid skills** | **100** | **1** | **10-point full Luck feature, 90 points other Sea Dragon capabilities** |

**Native abilities remain free:** underwater breathing, sea vision, growth, hydration, flight/spin, storm breath, existing species claws/diet and any provider mana regeneration. No duplicate native unlocks, no new standalone attack hotkeys, no unsourced water-school tags, and no fake pressure damage. Abilities requiring Dragon Survival hooks must be verified against the exact pinned binary before gameplay activation.
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

**Vampire has been fully rewritten for the new 100-point balance pass**, not merely repriced from old Tyz. It is a blood-managed nighttime predator whose JAWR native blood, sunlight and BAT rules remain free and authoritative. Four JAWR adapters and the bat duration adapter are already source-backed; exact gameplay and the other generic handlers require separate verification. Full design, numeric candidate effects and source/QA handoff: [Vampire full skill tree](VAMPIRE-COMPLETE-100PT-2026-11-22.md).

| Branch | Skill | One-point ranks | Intended result |
|---|---|---:|---|
| Bloodcraft | **Efficient Feeding** | 6 | +5% accepted native blood gain per rank (+30% max) from one verified JAWR feeding event, including recognized bottles, bites and valid player/nonplayer donors; no bonus on unrelated passive transfers or synthetic blood. |
| Bloodcraft | **Deep Reservoir** | 6 | +8% native maximum blood capacity per rank (+48% full), computed from JAWR's level baseline instead of multiplying existing bonuses; require native vampire level at least 2. |
| Bloodcraft | **Blood Thrift** | 6 | -3% verified native personal self-blood expenditure per rank (-18% max), only hunger/healing/approved timed ability charges, never donor blood extraction or orb/altar transfers; positive costs pay at least one unit. |
| Bloodcraft | **Crimson Renewal** | 7 | On a real completed feeding transaction that increases native blood, heal 0.5 HP per rank (+3.5 HP max), once per 20 seconds; no failed/full-pool, self-feeding or reentrant reward. |
| Predation | **Fang Training** | 5 | Successful native bite marks the same hostile prey for five seconds; the next real melee hit on that victim gains +4% damage/rank (+20% max), then consumes the mark. Drinking bottles does not trigger this. |
| Predation | **Blood Duelist** | 5 | +4% direct melee damage/rank (+20% max) against a real hostile whose current health is below half its maximum health; no self-damage or fake hostile target. |
| Predation | **Predatory Precision** | 5 | +2 percentage points real direct-melee critical chance per rank (+10 pp max), active only during genuine night/darkness. |
| Predation | **Blood Trail** | 5 | On a verified native bite against a hostile nonplayer, apply Glowing for 2 seconds/rank (10 seconds max). No global mob radar, no duplicate target marking and PvP exclusions configurable. |
| Nightstalker | **Shadow Stalker** | 6 | Reduce eligible hostile mobs' initial acquisition distance by 5%/rank (-30% max) while crouching in genuine darkness; cease after combat aggro and do not mask boss targeting. |
| Nightstalker | **Night Runner** | 5 | +3% grounded movement speed/rank (+15% max) during genuine darkness/night only; not constant daytime speed or free flight. |
| Nightstalker | **Bat Mastery** | 5 | +20% duration/rank (+100% max) on already-unlocked native JAWR BAT shape, using existing native BAT_FORM_DURATION attribute; does not enable flying or independently change steering. |
| Nightstalker | **Night Veil** | 4 | +2.5 percentage points projectile evasion/rank (+10 pp max) when crouching in genuine darkness; never negates area damage or sunlight. |
| Undead Constitution | **Pale Resilience** | 5 | Shorten newly applied finite harmful Wither, Weakness and Slowness by 5%/rank (-25% full), if JAWR vampire is actually susceptible; do not buy immunity it already has. |
| Undead Constitution | **Sunshroud** | 5 | -8% native ordinary sunlight tick damage/rank (-40% max); not immunity, no suppression of native lethal-special sunlight check or vampire forest exposure, no independent blood drain discount. |
| Undead Constitution | **Bloodbound Vitality** | 5 | +2 maximum health/rank (+10 HP or 5 hearts) only when the authoritative native JAWR blood reservoir is ≥50% full; no free healing when buffs toggle. |
| Undead Constitution | **Elder Presence** | 5 | On true melee hit at night against ordinary hostile targets, apply Weakness I for 1 second/rank (5 seconds max), once per 40 ticks per target; no boss pacification or PvP aura. |
| Sanguine Arts | **Sanguine Attunement** | 5 | +5% personally cast and actually blood-paid spell potency/rank (+25% full) for source-verified Neo Vitae/EvilCraft blood spell categories. Not all spells or machine/ritual throughput. |
| Sanguine Arts | **Hematic Efficiency** | 5 | -3% real personally paid blood spell costs/rank (-15% full); not JAWR hunger/heal, donors, stationary rituals or resources not consumed. Preserve MXP payment accounting. |
| Sanguine Arts | **Midnight Invocation** | 5 | +4% speed/cooldown throughput per rank (+20% full) for eligible personally cast blood spells at night/in actual darkness. No invented active spell, free ritual acceleration or global spell cooldown change. |

| **Total** | **19 paid skills** | **100** | **One JAWR blood pool; native state and acquired BAT remain free** |

Native Vampire health/feeding/HUD and Lilith initiation are never purchased again. Sunshroud is at most partial mitigation and daylight remains dangerous. No independent active vampire buttons, no duplicate ordinary blood or MXP awards, no blanket classification of all blood-mod recipes as personal spells, and no source-free native shape/flight grants.
### Werewolf — 100 ranks / 100 points

**Complete role-first rebalance:** full JAWR Werewolf identity is focused on high maximum health and actual physical regeneration, source-owned wolf claws and bites, moon-phase mobility, and verified personal nature magic. Unlike the archived Tyz source, every rank costs one point; no Fury wallet or bonus transformation/hotkey is sold.

Detailed source status, numeric candidate magnitudes, prerequisites and gameplay checklist: [Werewolf complete 100-point draft](WEREWOLF-COMPLETE-100PT-2026-11-22.md).

| Branch | Skill | One-point ranks | Candidate effect |
|---|---|---:|---|
| Lupine Vitality | **Feral Vigor** | 8 | +3 max HP per rank (+24 HP = 12 hearts max) for legitimate JAWR lycanthropy; source form-state gating, avoid immediate free heal from attribute toggles. |
| Lupine Vitality | **Regenerative Flesh** | 7 | Recover +0.15 HP/sec per rank (+1.05 HP/sec max) while genuinely fed and in an eligible native JAWR wolf/werewolf shape; 50% rate for the first 5s after taking real hostile damage, none while starving. HEALTH regeneration, not mana or wolf blood. |
| Lupine Vitality | **Unbreakable Beast** | 5 | Reduce remaining real non-silver physical damage by 2% per rank (10% max) when below 50% HP in wolf/werewolf form; never negate native silver/fire penalties or fatal damage. |
| Lupine Vitality | **Bloodlust Recovery** | 5 | After a genuine credited hostile kill while in wolf/werewolf form, heal 0.5 HP per rank (+2.5 HP max) with a 12-second cooldown; no credit from ownerless/farm/proxy deaths, natural entities without genuine hostile intent, or other player's kills. |
| Lupine Vitality | **Tempered Against Silver** | 5 | Reduce remaining damage by 4% per rank (20% max) ONLY from provably source-identified silver weapons or hazards, leaving at least 80% of the original danger. If silver sources cannot be reliably identified, withhold rather than broadly reduce all melee. |
| Feral Might | **Rending Claws** | 7 | Increase native legal empty-hand wolf/werewolf melee damage by 3% per rank (+21% max); use actual player_attack attribution, NOT arbitrary equipped weapons or wolf-type mobs. |
| Feral Might | **Savage Momentum** | 5 | On successive actual qualifying wolf melee hits within 4s, gain a temporary +2% damage per stack per rank, at most 3 stacks (+30% full), expiring 4s after the last hit; no Fury economy or gain for misses. |
| Feral Might | **Bite Techniques** | 5 | On a true native wolf bite with verified damage owner, apply Slowness I to eligible hostile prey for 1 second per rank (5s full), at most once per target every 40 ticks; no free stun, permanent infection or new bite button. |
| Feral Might | **Primal Fury** | 4 | While in wolf/werewolf shape and below 35% current HP, increase legal direct melee damage by 5% per rank (+20% max); passive injury threshold, NOT a separate Fury resource or activated burst. |
| Feral Might | **Predator's Focus** | 4 | Against real hostiles below 50% max HP, gain +3% eligible native wolf melee damage per rank (+12% max); do not proc on nonhostiles, tamed wolves or friendly allies. |
| Moonrunner | **Predator's Stride** | 5 | +4% grounded movement speed per rank (+20% full) while in actual JAWR WOLF/WEREWOLF form; not generic human walking or creative flight. |
| Moonrunner | **Pouncing Leap** | 4 | +0.25 block of genuine jump height per rank (+1 block full) during actual wolf movement, plus native fall handling; no new targeted leap hotkey or midair flight. |
| Moonrunner | **Lunar Chase** | 4 | +5% ground pursuit speed per rank (+20% full) when tracking a legitimate recently damaged hostile within 16 blocks under clear moonlit sky; no bonus indoors or in full daylight. Check exact lunar phase separately from simple night. |
| Moonrunner | **Feral Excavation** | 4 | +15% legal native empty-hand wolf claw break speed per rank (+60% full) only for blocks the claw can already harvest, no new tier, silk/fortune or ore-gate bypass. |
| Moonrunner | **Arrow Awareness** | 3 | +2 percentage points evasion chance per rank (+6 pp max) against genuine moving projectiles while in wolf/werewolf shape, not area explosions or ownerless spells. |
| Wild Communion | **Wild Attunement** | 6 | +5% per rank (+30% full) to verified personally cast nature spell potency from source-registered Roots/Botania/nature classes while JAWR werewolf-afflicted; not passive Botania flowers or generating machine throughput. |
| Wild Communion | **Rootsong** | 5 | +5% per rank (+25% full) to actual player-applied nature healing spell effects when spell provider and credited caster are verified. Do not count the intrinsic wolf HP regeneration twice. |
| Wild Communion | **Grove Howl** | 4 | When the werewolf legitimately casts an eligible owned nature spell, its already-tamed wolves within 12 blocks receive +5% direct damage per rank (+20% full) for 10s, refreshed on real casts only; no taming, new summoning, permanent wolf ownership, or new howl button. |
| Wild Communion | **Verdant Resilience** | 5 | Reduce remaining ordinary physical damage by 3% per rank (+15% full) in native wolf shape while grounded on grass, moss or leaves; not environmental/fire/silver reduction, and no global biome-wide magic aura. |
| Moon Apex | **Moon Apex** | 5 | Under actual full-moon conditions and in JAWR WOLF/WEREWOLF form, add +4% eligible claw damage AND +4% native personal nature-spell potency per rank (both +20% full); no activation button, permanent bonus, global weather override, extra transformation or silver immunity. |

| **Total** | **20 paid skills** | **100** | **Health regeneration, claws, moon and nature; native JAWR forms stay free** |

**Explicit holds before live activation:** exact JAWR bite identity, true lunar/source form conditions, silver-weapon hazard source, and player-owned nature spells / wolves. Generic Progression health attributes, regen, source-scoped claw melee, legal claw mining, heal on kill and hit streak are reusable; source tests are not equivalent to gameplay proof.
### Lich — complete first-pass 100 ranks / 100 points

*Native guardrails:* **ONE shared Lich tree** for Goety, JAWR/Forbidden Magic, Archlich, Gilded Piglich, and one compatible Dracolich. Goety personal Soul Energy, Goety Arca, JAWR phylactery and Soul in a Bottle remain separate; the death controller and source-native progression remain authoritative. No bought second death save or active skill buttons.

Full skill-by-skill numerical candidate effects, stage gates and provider blockers: [`LICH-COMPLETE-100PT-2026-11-22.md`](LICH-COMPLETE-100PT-2026-11-22.md). Unsupported provider benefits are not safe to sell until a verified effect exists.

#### Soul Economy — 25 points

| Skill | 1-point ranks | Full cost | Candidate per-rank effect |
|---|---:|---:|---|
| Soul Conservation (`tyr_progression:lich_soul_conservation`) | 6 | 6 | -3% per rank (maximum -18%) of eligible personally paid Goety Soul Energy necromancy cast costs; JAWR uses only an actually verified paid native necromantic cost, never phylactery, ritual or resurrection expense. |
| Soul Reserve (`tyr_progression:lich_soul_reserve`) | 5 | 5 | Goety: +4% per rank (maximum +20%) to verified personal Soul Energy storage, explicitly excluding Arca. JAWR: +4% per rank (maximum +20%) to duration of genuine personally cast native necromantic self-buffs, if such a source effect exists. |
| Soul Recovery (`tyr_progression:lich_soul_recovery`) | 5 | 5 | Goety: +4% per rank of legitimate credited native Soul Energy recovery; JAWR: +4% per rank of verified actual Soul-in-a-Bottle healing. Only amplify an existing successful provider transaction. |
| Soul Treasury (`tyr_progression:lich_soul_treasury`) | 5 | 5 | +3% per rank (maximum +15%) to eligible personally cast necromancy buff duration after a real, accepted source cast and paid costs; never machinery, rituals, passive auras or resurrections. |
| Death's Dividend (`tyr_progression:lich_deaths_dividend`) | 4 | 4 | One real hostile killed directly by an eligible personal necromancy spell may grant Goety 0.5% maximum native Soul Energy per rank or JAWR 0.5 HP per rank; 20-second player cooldown, no player/owned minion/farm-credit triggers. |

#### Sanguine Necromancy — 20 points

| Skill | 1-point ranks | Full cost | Candidate per-rank effect |
|---|---:|---:|---|
| Sanguine Attunement (`tyr_progression:lich_sanguine_attunement`) | 6 | 6 | +3% per rank (maximum +18%) personally cast verified blood-magic damage; in exchange -2% per rank (maximum -12%) personally cast Roots/Botania nature-magic potency. No other player's spells or machine effects. |
| Crimson Channel (`tyr_progression:lich_crimson_channel`) | 5 | 5 | +3% per rank (maximum +15%) personal necromantic spell potency, and -2% per rank (maximum -10%) personal nature-magic healing. Does not multiply all generic magic or ally casts. |
| Verdant Severance (`tyr_progression:lich_verdant_severance`) | 4 | 4 | +4% per rank (maximum +16%) personal blood/necromantic spell damage while imposing -4% per rank (maximum -16%) personal nature spell potency; cumulative self-only tradeoff, clamped against zero. |
| Grave Sorcery (`tyr_progression:lich_grave_sorcery`) | 5 | 5 | +3% per rank (maximum +15%) to qualifying harmful necromantic status durations that this player personally inflicts with genuine accepted spells; excludes instant damage and friendly effects. |

#### Undead Host — 25 points

| Skill | 1-point ranks | Full cost | Candidate per-rank effect |
|---|---:|---:|---|
| Bone Captain (`tyr_progression:lich_bone_captain`) | 6 | 6 | +3% per rank (maximum +18%) damage from personally owned, verified summoned undead servants; never naturally spawned hostile undead or another player's units. |
| Grave Regiment (`tyr_progression:lich_grave_regiment`) | 5 | 5 | +4% per rank (maximum +20%) to supported native follow/leash range for already-authorized personally owned undead; never raises summon cap, chunk-loads or overrides native command controls. |
| Death Host (`tyr_progression:lich_death_host`) | 5 | 5 | After a verified personal necromantic cast with real cost, owned eligible undead gain +2% damage per rank (maximum +10%) for eight seconds; refresh at most once per 15 seconds. |
| Grave Intercession (`tyr_progression:lich_grave_intercession`) | 5 | 5 | When an actual hostile attack damages the Lich, one nearby eligible owned undead may legitimately retaliate with +2% attack speed per rank for five seconds; 15-second cooldown, no PvP/claims bypass. |
| Grave Vigil (`tyr_progression:lich_grave_vigil`) | 4 | 4 | +4% per rank (maximum +16%) max health for player-owned eligible undead summons; apply once on native creation/update, remove cleanly on owner loss; no mob cap increase. |

#### Deathless Form — 20 points

| Skill | 1-point ranks | Full cost | Candidate per-rank effect |
|---|---:|---:|---|
| Cold Persistence (`tyr_progression:lich_cold_persistence`) | 6 | 6 | -3% per rank (maximum -18%) duration of verified harmful necrotic/wither-like status effects not already negated by native Lich immunity. Preserve Smite, daylight, fire and other actual source weaknesses. |
| Ossuary Skin (`tyr_progression:lich_ossuary_skin`) | 5 | 5 | 1.5% per rank (maximum 7.5%) reduction to ordinary incoming physical hits in an authenticated Lich state, after eligible mitigation; exclude true damage, void, native Smite weakness and fatal-event decisions. |
| Grave Resilience (`tyr_progression:lich_grave_resilience`) | 5 | 5 | After a direct, credited hostile kill by the player or an eligible owned undead, heal this Lich 0.5 HP per rank (maximum 2.5 HP) at most once every 12 seconds. |
| Unquiet Stride (`tyr_progression:lich_unquiet_stride`) | 4 | 4 | +2% per rank (maximum +8%) grounded movement speed in natural darkness or below open sky; no speed in flight, no new flight permission, no night-vision duplication. |

#### Soul Preservation — 10 points

| Skill | 1-point ranks | Full cost | Candidate per-rank effect |
|---|---:|---:|---|
| Anchor Attunement (`tyr_progression:lich_anchor_attunement`) | 4 | 4 | +2% knockback resistance per rank (maximum +8%) only while native provider confirms a charged, currently valid Lich anchor (JAWR phylactery or Goety Arca); never creates, charges or substitutes an anchor. |
| Final Passage (`tyr_progression:lich_final_passage`) | 3 | 3 | Only after the native unified death controller actually records one successful Lich rescue, restore +2 HP per rank (maximum +6 HP), capped at max health, once for that event. |
| Second Witness (`tyr_progression:lich_second_witness`) | 3 | 3 | Only after the same single native rescue receipt, grant +3 seconds per rank (maximum nine seconds) of Resistance I to cover the dangerous return; respect actual native spawn/world rules. |

**Total Lich: 21 skills / 100 one-point ranks.** Source/behavior verification needed before engine activation. Complete design only.

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

## Balance-pass assumptions audit — separate firm decisions from inherited guesses

**Confirmed constraints:** (a) every rank costs one advancement point; (b) 100-point per-build mastery for ordinary specialist Paths, Monk 1,000; (c) Core/MXP are independent of Path identity, Newbie XP prohibition; (d) known free abilities remain free and source mods own form physiology; (e) mostly passive, contextual or mod-enhancing improvements rather than inventing new attack buttons. These constraints do **not** prescribe exact skill lists or branch weights.

**Superseded historical figures (not new commitments):** the old archived Tyz trees' per-skill SP/rank totals, **50-point Sea Dragon Luck**, former 300–500+ point totals, and their relative proportion of skills to Path power. The current new balance pass explicitly authorizes changing all of these. Older maximum effect values are *references*, not automatically preserved caps or exact numerical targets.

**Earlier user preferences to reconfirm, not automatically extrapolate:** the older Gilded seven independent Fortune and seven Looting ranks; the very strong Sea Dragon Luck identity (now exactly ten ranks), Core Reservoir/Wellspring/Thrift/Potency as families, existing transformation and innate abilities. A prior seven-rank decision does not imply every future skill needs seven ranks.

**Assistant-chosen structural scaffolding, not owner-locked:** Hunter **62 shared / 8 training / 30 prey** and **12/8/10** within the prey branch; exact rank distributions in every other Path; assumptions that all Paths need the same number of skill nodes or that every named node must have enough ranks to consume its old SP allocation; treating **universal Core as automatically subject to a separate 100-point mastery cap**; expecting old capped effects to transfer unchanged; the arbitrary existing Monk five-lane **200/200/150/110/340** balance and **38 purchased node count**. These need a gameplay-role review.

**Meaningful distinction:** the engine already has mutually exclusive Hunter choices, so supporting distinct prey specialties is grounded. The **specific budget split**, irreversible selection or lifetime rigidity is not. A single-build 100-point budget is about what the player may actually purchase after valid choices, not the sum of visible alternatives.

**Planning standard for future edits:** when the owner proposes a new balance pass, first identify which earlier numbers are revoked. Draft the intended *effect at mastery*, then choose a meaningful number of one-point ranks, then distribute the remaining Path budget among different useful gameplay capabilities. Never 'conserve' an obsolete skill's cost without a new design reason.
