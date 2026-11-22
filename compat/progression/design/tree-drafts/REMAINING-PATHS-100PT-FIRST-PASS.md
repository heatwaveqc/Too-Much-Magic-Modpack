# TMM Progression — remaining Path trees, one point per rank

**DESIGN ONLY · November 22, 2026 · source baseline `dev/progression`**

Authoritative ledger: `REMAINING-PATHS-100PT-FIRST-PASS.json` schema `tmm_remaining_path_tree_blueprint_v12`. None of these drafted purchases, balances or source provider hooks are installed in-game by this PR.

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
| Cave Dragon | 25 | **100** |
| Sea Dragon | 22 | **100** |
| Forest Dragon | 26 | **100** |
| Vampire | 19 | **100** |
| Werewolf | 20 | **100** |
| Lich | 21 | **100** |
| Hunter | 57 | **100** |
| Gilded | 22 | **100** |
| Bare Zerker | 22 | **100** |

The ten draft catalogs display **261 paid skill options**; 57 are Hunter alternatives (not simultaneously eligible). Newbie and Normie remain baseline Paths with no independent purchasable ranks; Core is universal, not a specialist Path.

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

### Cave Dragon — complete 100 ranks / 100 points

**Role:** fire-breathing underground excavator, durable stone defender and high-capacity personal spellcaster with narrow infernal affinity. Native Cave Dragon status, breath/fireball/skin/lava vision, cave-claw pickaxe, Cave Athletics, growth, fireproofing and water/rain/snow weakness stay source-owned and free. TMM's raw land-meat Cave diet under issue #444 is not changed by this tree.

[Complete 25-skill Cave Dragon design, per-rank numerical candidates and adapter QA crosswalk](CAVE-DRAGON-COMPLETE-100PT-2026-11-22.md). This is design-only: the pinned 1.21.1 ability registry must be checked before any purchase works.

#### Draconic Body & Personal Sorcery — 20 points

| Purchased skill | One-point ranks | Candidate effect |
|---|---:|---|
| **Draconic Reservoir** (`tyr_progression:cave_dragon_draconic_reservoir`) | 6 | +10% per rank (+60% full) to existing eligible personal spell-resource maximums, including Dragon Survival native mana only where source-safe. Excludes machines, Arca, phylacteries and nonplayer mana grids. |
| **Draconic Potency** (`tyr_progression:cave_dragon_draconic_potency`) | 5 | +4% per rank (+20% full) to verified personally cast magic output, additive in the same bonus bucket as Core, before typed fire/infernal bonuses; not passive machines, automatic summons or double spell events. |
| **Draconic Scales** (`tyr_progression:cave_dragon_draconic_scales`) | 3 | +1 natural armor per rank (+3 full) in actual Cave Dragon form, subject to configured cap. This is additional purchased armor, not charging for native Cave scales, levels or fire immunity. |
| **Draconic Claws** (`tyr_progression:cave_dragon_draconic_claws`) | 3 | +1 physical attack damage per rank (+3 full) on verified Cave Dragon natural claw/bite melee only, respecting source claw weapon-slot precedence; not sword-held normal attacks or magic casts. |
| **Draconic Talons** (`tyr_progression:cave_dragon_draconic_talons`) | 3 | +15% per rank (+45% full) to legal native Cave pickaxe-claw block mining speed only. No mining-tier upgrade, Fortune, Silk, ore-gen permission or tool durability bypass. |

#### Pyroclastic Combat — 23 points

| Purchased skill | One-point ranks | Candidate effect |
|---|---:|---|
| **Nether Breath** (`tyr_progression:cave_dragon_nether_breath`) | 5 | +4% per rank (+20% full) damage from a verified already-learned native Cave Dragon flame breath. Does not unlock breath, increase terrain burning or permit casting in rain/water when source forbids it. |
| **Fireball Mastery** (`tyr_progression:cave_dragon_fireball_mastery`) | 4 | +5% per rank (+20% full) to confirmed native Cave Fireball direct entity impact damage after source unlock. Explosion terrain effects, ignition footprint and block destruction remain native only. |
| **Burning Wake** (`tyr_progression:cave_dragon_burning_wake`) | 4 | +0.5 second per rank (+2 seconds full) to the existing burn on legitimate hostile targets hit by native Cave fire abilities; does not add independent passive ignition or extend beyond configured source fire cap. |
| **Stone Claws** (`tyr_progression:cave_dragon_stone_claws`) | 3 | +0.1 knockback strength per rank (+0.3 full) to confirmed direct natural Cave Dragon claw attacks against legal hostiles; separate from Draconic Claws' raw damage. Does not affect mined blocks or hand-held weapons. |
| **Crushing Bite** (`tyr_progression:cave_dragon_crushing_bite`) | 4 | +3% per rank (+12% full) direct bite damage against verified armored hostile mobs; preserve vanilla source armor calculation, no armor-bypass tag and no bonus to ordinary unarmored targets. |
| **Seismic Pounce** (`tyr_progression:cave_dragon_seismic_pounce`) | 3 | On a genuine already-unlocked Dragon Survival Flight Spin contacting a hostile while landing on eligible stone, apply Slowness I for +0.5 second per rank (1.5 seconds full), 15-second player cooldown. No new pounce key or terrain shockwave. |

#### Stone Scales & Hearth — 19 points

| Purchased skill | One-point ranks | Candidate effect |
|---|---:|---|
| **Stonehide** (`tyr_progression:cave_dragon_stonehide`) | 5 | +0.5 armor toughness per rank (+2.5 full) in real Cave Dragon form, under effective defensive caps. Unlike Draconic Scales, this does not raise ordinary armor points. |
| **Earthbound Poise** (`tyr_progression:cave_dragon_earthbound_poise`) | 4 | -5% per rank (-20% full) ordinary fall damage only when landing on a verified eligible stone block; no void protection, immunity or separate flight permission. |
| **Mountain Bastion** (`tyr_progression:cave_dragon_mountain_bastion`) | 4 | After taking one genuine hostile hit while grounded on natural stone, gain 3% per rank (12% full) reduction to subsequent eligible direct physical damage for 5 seconds, with 20-second cooldown. The triggering hit is not retroactively reduced. |
| **Hearthskin** (`tyr_progression:cave_dragon_hearthskin`) | 3 | +8% per rank (+24% full) duration to the actual earned native Cave Sturdy Skin protective effect when legitimately activated; leave source target selection, cooldown, cost and effect strength unchanged. |
| **Water Temperance** (`tyr_progression:cave_dragon_water_temperance`) | 3 | -4% per rank (-12% full) of explicitly source-tagged Cave Dragon water, rain and snow weakness damage; partial mitigation only, never weather immunity or extinguishing native penalties. |

#### Excavation & Depth — 22 points

| Purchased skill | One-point ranks | Candidate effect |
|---|---:|---|
| **Tunneler** (`tyr_progression:cave_dragon_tunneler`) | 5 | +4% per rank (+20% full) movement speed while already benefiting from earned native Cave Athletics on stone and grounded below open sky; does not grant Cave Athletics or invent extra Speed when inactive. |
| **Vein Sense** (`tyr_progression:cave_dragon_vein_sense`) | 4 | After actually mining an eligible ore block with legal Cave claws, briefly (3 seconds) reveal up to 2 additional connected identical ore blocks per rank within five blocks of the broken block. Never global x-ray/chunk scan or excluded gated ores. |
| **Deep Excavation** (`tyr_progression:cave_dragon_deep_excavation`) | 4 | After breaking three real stone-like blocks consecutively with native legal Cave claws, gain +5% per rank (+20% full) claw mining speed for five seconds. No automated extra block breaks, vein miner or bypass of individual durability/hunger. |
| **Clawkeeper** (`tyr_progression:cave_dragon_clawkeeper`) | 4 | 5% chance per rank (20% full) to prevent precisely one durability expenditure on the actual equipped native Cave pickaxe-claw tool on a successful legal break. Never repairs items or changes fake slot visuals. |
| **Understone Sight** (`tyr_progression:cave_dragon_understone_sight`) | 5 | After three seconds in genuinely dark enclosed underground terrain, gain server-authorized enhanced night vision lasting +2 seconds per rank (10 seconds full), refreshed only while conditions hold. Distinct from free native lava vision. |

#### Infernal Sorcery — 16 points

| Purchased skill | One-point ranks | Candidate effect |
|---|---:|---|
| **Infernal Resonance** (`tyr_progression:cave_dragon_infernal_resonance`) | 5 | +4% per rank (+20% full) personally cast explicitly source-tagged demon/infernal magic potency (e.g. verified Goety or Occultism personal actions), additive with Draconic Potency and not a blanket bonus to all Goety/Occultism rituals. |
| **Hellbound Command** (`tyr_progression:cave_dragon_hellbound_command`) | 4 | +4% per rank (+16% full) direct damage from eligible player-owned demonic servants summoned by verified personal provider actions; not wild demons, every Goety undead, bosses or other players' summons. |
| **Infernal Dominion** (`tyr_progression:cave_dragon_infernal_dominion`) | 3 | +5% per rank (+15% full) additional direct damage to eligible personally cast demonic/infernal spells when in the actual Nether or on source-verified hot stone, applied additively alongside Infernal Resonance. |
| **Furnace Focus** (`tyr_progression:cave_dragon_furnace_focus`) | 4 | -3% per rank (-12% full) real personally paid fire/infernal spell resource debit while on verified hot Nether/Cave terrain. Never discounts ritual/machine costs, native diet, dragon resurrection, or world aura generation. |

**Total: 25 distinct purchased skills, exactly 100 separate 1-point ranks.** Exact values and source predicates await pinned Dragon Survival and Progression engine audits. Do not duplicate native fire immunity, native breath unlocks, harvest tiers, water weakness mitigation beyond stated partial cap, or generic vein mining.

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
### Forest Dragon — complete 100 ranks / 100 points

**Role:** a poison-breathing woodland guardian, daylight regenerator, crop and animal caretaker, and source-verified Fae spellcaster. Native axe claws, native Forest/Sun Breaths, forest darkness Stress and native Light in the Dark remain free. Strict vegetarian preferred diet #444 is separately owned; not implemented by the tree.

[Complete Forest Dragon skills, per-rank effects, source gates and QA](FOREST-DRAGON-COMPLETE-100PT-2026-11-22.md). Design only; no live skills.

#### Draconic Body & Personal Sorcery — 20 points

| Skill | One-point ranks | Candidate effect |
|---|---:|---|
| **Draconic Reservoir** (`tyr_progression:forest_dragon_draconic_reservoir`) | 6 | +10% per rank (max +60%) eligible personal spell-resource capacity, including native Dragon Survival mana only if source modifier supports it; no machine, grid, or world storage. |
| **Draconic Potency** (`tyr_progression:forest_dragon_draconic_potency`) | 5 | +4% per rank (max +20%) personally cast verified spell potency, additive with other magic modifiers; exclude world rituals and passive machines. |
| **Draconic Scales** (`tyr_progression:forest_dragon_draconic_scales`) | 3 | +1 natural armor per rank (max +3) only in verified Forest Dragon form; normal native body armor and fall/thorn defenses remain innate. |
| **Draconic Claws** (`tyr_progression:forest_dragon_draconic_claws`) | 3 | +1 native physical claw/bite melee damage per rank (max +3), excluding held swords, breath and poison ticks. |
| **Draconic Talons** (`tyr_progression:forest_dragon_draconic_talons`) | 3 | +15% legal Forest axe-claw chop speed per rank (max +45%), only on wood/plant blocks already harvestable by native claws; no treecapitator/ore tier bypass. |

#### Verdant Vitality — 19 points

| Skill | One-point ranks | Candidate effect |
|---|---:|---|
| **Green Recovery** (`tyr_progression:forest_dragon_green_recovery`) | 5 | +0.15 HP/s per rank (max +0.75) while grounded in natural living vegetation during daylight, after eight seconds without hostile damage. |
| **Living Bark** (`tyr_progression:forest_dragon_living_bark`) | 5 | +0.5 armor toughness per rank (max +2.5) near verified natural trees in Forest form; distinct from Draconic Scales armor. |
| **Elder Grove** (`tyr_progression:forest_dragon_elder_grove`) | 4 | After a successfully paid personal Nature spell, grant self Regeneration I for one second per rank (max four seconds), 20-second player cooldown; no ability key. |
| **Sunfed Resilience** (`tyr_progression:forest_dragon_sunfed_resilience`) | 5 | -2% ordinary physical hit damage per rank (max -10%) only while grounded under actual direct daylight in Forest Dragon form; not in artificial light or other dimensions. |

#### Thorn & Root — 18 points

| Skill | One-point ranks | Candidate effect |
|---|---:|---|
| **Bramble Claw** (`tyr_progression:forest_dragon_bramble_claw`) | 4 | Legitimate native Forest natural claw hits inflict Slowness I for 0.5 second per rank (max two seconds), with 40-tick cooldown per hostile target. |
| **Entangling Breath** (`tyr_progression:forest_dragon_entangling_breath`) | 4 | Source-verified already-unlocked Forest Breath applies Slowness I for 0.5 second per rank (max two seconds) to hostile targets; native poison behavior stays unchanged. |
| **Thornstorm** (`tyr_progression:forest_dragon_thornstorm`) | 3 | A native Forest Spike projectile direct hostile hit may slow at most one additional nearby hostile within 2.5 blocks for 0.5 second per rank (max 1.5 seconds), 15-second caster cooldown; no new projectile or block changes. |
| **Venomous Strike** (`tyr_progression:forest_dragon_venomous_strike`) | 4 | +5 percentage-point chance per rank (max 20%) for real natural claw melee to apply Poison I for three seconds to legal hostile targets; no bleed-through from poison ticks or allies. |
| **Forest Breath Mastery** (`tyr_progression:forest_dragon_forest_breath_mastery`) | 3 | +4% per rank (max +12%) real source-identified Forest Breath poison damage, after native breath has been learned; not every poison effect or Sun Breath. |

#### Fertility & Bounty — 19 points

| Skill | One-point ranks | Candidate effect |
|---|---:|---|
| **Germination Touch** (`tyr_progression:forest_dragon_germination_touch`) | 4 | 5% per rank chance (max 20%) to refund exactly one bonemeal consumed in one successful, claim-allowed player crop-growth application; no free growth without real use. |
| **Breeder's Blessing** (`tyr_progression:forest_dragon_breeders_blessing`) | 4 | After personally triggering a legitimate fed breeding pair, reduce both parents' normal subsequent breeding cooldown by 5% per rank (max 20%) subject to a floor; no extra child spawned. |
| **Harvest Bounty** (`tyr_progression:forest_dragon_harvest_bounty`) | 4 | +5% chance per rank (max 20%) of exactly one extra ordinary edible fruit/berry for one legally harvested mature shrub/vine/fruit tree; excludes conventional field crops and hidden loot. |
| **Green Thumb** (`tyr_progression:forest_dragon_green_thumb`) | 4 | +4 percentage-point chance per rank (max 16%) to duplicate normal edible produce from one personally harvested mature planted field crop, not shrub berries, rare loot or automation. |
| **Shepherd’s Blessing** (`tyr_progression:forest_dragon_shepherds_blessing`) | 3 | +8% juvenile livestock growth rate per rank (max 24%) only for player-owned/cared-for legitimate juveniles within 12 blocks; bounded refresh, no instant maturity. |

#### Fae Affinity — 14 points

| Skill | One-point ranks | Candidate effect |
|---|---:|---|
| **Fey Courtesy** (`tyr_progression:forest_dragon_fey_courtesy`) | 5 | +3% beneficial status duration per rank (max +15%) for personally cast, source-tagged Fae spells; no blanket mob friendliness or bonus to every Roots/Botania action. |
| **Fey Conduit** (`tyr_progression:forest_dragon_fey_conduit`) | 4 | +4% per rank (max +16%) verified personal Fae spell healing or damage within six blocks of natural vegetation, additive with Core and Draconic Potency. |
| **Grove Guardian** (`tyr_progression:forest_dragon_grove_guardian`) | 3 | After a paid personal Nature/Fae support spell, grant at most two nearby valid allies +2% physical hit mitigation per rank (max 6%) for five seconds, 25-second cooldown, no permanent aura. |
| **Woodland Accord** (`tyr_progression:forest_dragon_woodland_accord`) | 2 | +5% max HP per rank (max +10%) to genuine player-owned, source-confirmed Fae summons only; none for wild fae, villagers or generic pets, and no ghost rank if provider lacks them. |

#### Light & Shadow — 10 points

| Skill | One-point ranks | Candidate effect |
|---|---:|---|
| **Gloam Endurance** (`tyr_progression:forest_dragon_gloam_endurance`) | 4 | Reduce only source-native darkness Stress *extra hunger drain* by 5% per rank (max 20%); never cancel Stress, ordinary hunger, or native Light in the Dark's identity. |
| **Sunward Focus** (`tyr_progression:forest_dragon_sunward_focus`) | 3 | Reduce cooldown of already-earned, actual native Sun Breath by 4% per rank (max 12%) in direct daylight; do not expand growth/transmutation targets or create the breath ability. |
| **Lantern Keeper** (`tyr_progression:forest_dragon_lantern_keeper`) | 3 | +3% per rank (max +9%) personal source-verified Nature support-spell duration while sheltered in artificial block light level 11+, not a blanket magic buff or source Stress immunity. |

**Total: 26 paid skills / 100 one-point ranks.** All 20 earlier named skills retained, including three stock Tyz provenance entries. Inert purchases must remain disabled until native adapters exist.

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

### Gilded — complete 100-rank gold-and-Nether skill tree

**Free, never purchasable:** Nether four-gold-armor+gold-food enrollment; diamond-effective gold while equipped, +3 virtual Fortune for gold/Netherite tools and +3 virtual Looting for golden swords/axes, base extra virtual Unbreaking and actual equal gold/Netherite durability repair, Piglin/Brute friendship and protected gold/chest anger exemption, native initiation lava fishing, gold-qualified foods 2× normal hunger and saturation **points**, native Gilded rank18 Undying Ember fire-tag immunity. Normal attack retaliation still occurs, lava fish and ore XP remain source/event-authenticated.

[Full Gilded 22-skill design, numeric candidates, native source crosswalk, QA and unresolved provider holds](GILDED-COMPLETE-100PT-2026-11-22.md). Design only; no installed Gilded Path config or hooks.

#### Goldcraft & Enchantment — 32 points

| Skill | Purchased one-point ranks | Proposed effect |
|---|---:|---|
| **Fortune Mastery** (`tyr_progression:gilded_fortune_mastery`) | 7 | +1 virtual Fortune level per rank, from FREE +3 at initiation to +10 purchased maximum, for real gold/Netherite mining tools only. Stored Fortune levels add without an artificial cap; Silk Touch wins. Exactly one player-owned eligible block-loot context; no drop multiplication after loot. |
| **Looting Mastery** (`tyr_progression:gilded_looting_mastery`) | 7 | +1 virtual Looting level per rank, from FREE +3 at initiation to +10 purchased maximum, for actual direct melee kills with golden sword or axe only. Stored Looting adds; never projectiles, spell kills, offhand gold held during other kills or another player's loot. |
| **Lasting Gilt** (`tyr_progression:gilded_lasting_gilt`) | 5 | +1 additional virtual Unbreaking level per rank on legitimately worn gold/Netherite armor or actively held mainhand gold/Netherite tool, *above* the FREE baseline extra Unbreaking. Do not alter stored enchantment, speed or durability max. |
| **Golden Renewal** (`tyr_progression:gilded_golden_renewal`) | 5 | Reduce the source-configured FREE base interval between each legal equipped gold/Netherite 1-point durability repair by 10% per rank (max 50% shorter interval), with identical actual ticks/amounts for both materials and bounded min interval. Never repair backpack/offhand/container gear. |
| **Gilded Grip Mastery** (`tyr_progression:gilded_gilded_grip_mastery`) | 4 | +5% per rank (max +20%) ordinary mining speed with correctly held golden pickaxe/axe/shovel/hoe on legally harvestable blocks *after* FREE diamond-tier equivalence. Preserve golden tool's already high speed; no harvest tier increase from purchases. |
| **Metallurgist** (`tyr_progression:gilded_metallurgist`) | 4 | +4% per rank (max +16%) direct physical damage with a genuinely held golden sword or axe against legal hostile mobs, without changing base item material, offhand weapon or all Netherite tools; no damage from passive fire or distant projectiles. |

#### Piglin Kinship & Barter — 18 points

| Skill | Purchased one-point ranks | Proposed effect |
|---|---:|---|
| **Piglin Diplomacy** (`tyr_progression:gilded_piglin_diplomacy`) | 5 | -5% per rank (max -25%) to *ordinary Piglin*'s normal processing/wait time after it has actually accepted a legitimate gold ingot barter, respecting native minimum delay. Brutes do not trade. Innate Piglin/Brute friendship is FREE and unchanged. |
| **Barter Acumen** (`tyr_progression:gilded_barter_acumen`) | 4 | +3 percentage points per rank (max +12 pp) to award exactly one extra *common, allowed* item from an ordinary Piglin barter response when the original common reward has actually resolved; excluded rare tiers, boss ingredients and guaranteed high-value loops. |
| **Hoglin Pursuit** (`tyr_progression:gilded_hoglin_pursuit`) | 4 | +4% per rank (max +16%) direct melee damage against source-tagged hostile Hoglins/Zoglins when wielding a real golden sword/axe; does not apply to Piglins, friendly companions or every Nether creature. |
| **Warband Camaraderie** (`tyr_progression:gilded_warband_camaraderie`) | 5 | Reduce ordinary incoming direct physical hit damage by 1% per rank (max 5%) while physically within eight blocks of at least two non-angry genuine Piglins/Brutes in the Nether. No AI mind control or global Piglin invulnerability. |

#### Lava Livelihood & Food — 20 points

| Skill | Purchased one-point ranks | Proposed effect |
|---|---:|---|
| **Lava Angler** (`tyr_progression:gilded_lava_angler`) | 5 | -4% per rank (max -20%) normal bite/wait interval for an eligible *real lava* fishing bobber from the selected providers, never water rods or ordinary fishing reward loops. Permission to fish lava is FREE at initiation. |
| **Magma Linecraft** (`tyr_progression:gilded_magma_linecraft`) | 4 | 5% chance per rank (max 20%) to prevent one actual fishing-rod durability loss on a *completed, eligible lava catch*, no effect on casts, canceled reels, empty catches or water fishing; no free repair. |
| **Ember Salvage** (`tyr_progression:gilded_ember_salvage`) | 4 | +2 percentage points per rank (max +8 pp) to legal *ordinary* treasure-category odds within the original provider's lava fishing table, renormalizing other legal categories; never generate banned ores, boss items or unlisted loot and never duplicate an entire catch. |
| **Gold Nourishment** (`tyr_progression:gilded_gold_nourishment`) | 4 | Heal +0.5 HP per rank (max +2 HP) after one actually consumed, qualified gold-containing food, with 30-second personal cooldown; FREE 2× hunger + 2× saturation-points policy remains unchanged and special potion effects run once. |
| **Nether Forager** (`tyr_progression:gilded_nether_forager`) | 3 | +5% per rank (max +15%) theoretical saturation points from one legitimately eaten qualifying *non-gold* Nether food; ordinary hunger value unchanged, gold foods excluded and vanilla actual saturation clamp preserved. |

#### Nether Mastery — 20 points

| Skill | Purchased one-point ranks | Proposed effect |
|---|---:|---|
| **Nether Affinity** (`tyr_progression:gilded_nether_affinity`) | 5 | +3% grounded walking speed per rank (max +15%) only in the actual Nether while standing on native Nether rock/soil; no flight/glide boost, no free fire immunity and no generic Overworld speed. |
| **Basalt Footing** (`tyr_progression:gilded_basalt_footing`) | 4 | -5% ordinary fall damage per rank (max -20%) when physically landing on verified basalt/blackstone or analogous natural Nether stone; no cancel of void/kill-plane and no new flight permission. |
| **Molten Reflex** (`tyr_progression:gilded_molten_reflex`) | 4 | +2 percentage points per rank (max +8 pp) to evade legitimately incoming projectile hits while in the Nether **after** FREE native rank-18 Undying Ember is already earned; no dodge of fire-tagged damage that immunity already cancels. |
| **Prospector's Practice** (`tyr_progression:gilded_prospectors_practice`) | 4 | +5% per rank (max +20%) to the *one real, qualifying per-block* Gilded gold-ore activity XP/MXP credit under configured XP mode, never a second grant, no Fortune/yield amplification, and no Silk Touch or canceled/failed break credit. |
| **Ashen Resolve** (`tyr_progression:gilded_ashen_resolve`) | 3 | -3% direct ordinary physical damage per rank (max -9%) when the enrolled Gilded is below half max health and in the Nether, excluding void, bypass-armor and innate #minecraft:is_fire negations. |

#### Sovereignty — 10 points

| Skill | Purchased one-point ranks | Proposed effect |
|---|---:|---|
| **Glittering Luck** (`tyr_progression:gilded_glittering_luck`) | 4 | +0.25 player Luck attribute per rank (+1.0 total); only effects whose legal loot/ability code truly consults the Luck attribute. No virtual Fortune/Looting levels, automatic rare-drop duplication or Sea Dragon-level luck. |
| **Gilded Sovereign** (`tyr_progression:gilded_gilded_sovereign`) | 6 | After genuine native Gilded level 20, while wearing a complete four-piece golden armor set AND directly holding a golden sword/axe in the Nether, gain +3% direct gold melee damage and -2% incoming ordinary physical damage per rank (max +18% and -12%). Netherite armor or weapon does not trigger this *gold-regalia* capstone. |

**Gilded total: 22 skills / 100 purchased ranks, each 1 SP.** Both virtual Fortune and Looting have exactly **seven** independent purchases beyond +3 free and reach +10. Never duplicate innate gold equipment equivalence, first lava-fishing permission, golden food nutrition, or rank18 Undying Ember.

### Bare Zerker — complete 100 ranks / 100 points

**Owner-locked baseline is FREE:** Fury max 100; +1 Fury per actual post-mitigation damage dealt to a real target ONLY; decays 1/s. Each distinct real incoming damage instance adds exactly 1s of Regeneration I and 1s of Resistance I, with normal hurt i-frames removed. A would-be fatal hit at **pre-hit Fury >50** triggers a **complete vanilla Totem of Undying**, drains the entire Fury bar and has no fixed cooldown. Chest items chafe, eligible clothes other than Cap dislodge on damage, a shield disables sword/axe Path bonuses, and Woad/Red Cap are temporary owner/source-governed mechanics. **No Bare Zerker dietary limits.**

[Full Bare Zerker Fury, Woad, blood magic and Magebreaker skill specification](BARE-ZERKER-COMPLETE-100PT-2026-11-22.md). Numeric per-rank effects are design candidates; pinned Neo Vitae/EvilCraft, Woad upgrade parity and magic-object MXP require source audit and owner decisions.

#### Fury Combat — 34 points

| Skill | One-point ranks | Proposed effect |
|---|---:|---|
| **Reckless Sword** (tyr_progression:bare_zerker_reckless_sword) | 6 | +3% direct melee sword damage per rank (+18% max), only with empty chest and no shield; never a separate Fury award. |
| **Cleaving Axe** (tyr_progression:bare_zerker_cleaving_axe) | 6 | +3% direct melee axe damage per rank (+18% max), chest empty and shield absent; no extra Fury. |
| **Battle Rhythm** (tyr_progression:bare_zerker_battle_rhythm) | 5 | Actual successful shieldless sword/axe direct hits within six seconds build up to three nonstacking-renewal Rhythm stacks; each stack grants +1.5% damage per purchased rank (+22.5% at three stacks and five ranks). No stack from missed/blocked/non-damaging hits. |
| **Bloodied Drive** (tyr_progression:bare_zerker_bloodied_drive) | 5 | On a real personally credited hostile kill by direct sword or axe, +3% movement speed per rank (+15%) for five seconds; refresh, never stack. Kill itself adds no Fury. |
| **Fury Finesse** (tyr_progression:bare_zerker_fury_finesse) | 5 | While real Fury >=25, chest empty and shield absent, +2% sword/axe attack speed per rank (+10% max). No change to Fury decay, max, or gain. |
| **Headsplitter** (tyr_progression:bare_zerker_headsplitter) | 4 | At actual Fury >=50, +3% axe direct melee damage per rank (+12% max) against genuinely armored hostile mobs, with normal armor mitigation retained. |
| **Relentless Pursuit** (tyr_progression:bare_zerker_relentless_pursuit) | 3 | Following actual hostile direct sword/axe hit, +3% sprint speed per rank (+9% max) for up to three seconds while approaching the same living target. No dash button. |

#### Endurance Under Fire — 22 points

| Skill | One-point ranks | Proposed effect |
|---|---:|---|
| **Skin Hardened** (tyr_progression:bare_zerker_skin_hardened) | 6 | -2% eligible direct physical damage taken per rank (-12% max) while chest empty, before damage response; no wearable armor item. |
| **Regeneration Amplifier** (tyr_progression:bare_zerker_regen_amplifier) | 2 | Rank 1 upgrades FREE on-damage Regeneration I to II; rank 2 to III. Each actual distinct incoming damage instance still adds exactly ONE SECOND and no duration increase. |
| **Resistance Amplifier** (tyr_progression:bare_zerker_resistance_amplifier) | 2 | Rank 1 upgrades FREE on-damage Resistance I to II; rank 2 to III. Each actual distinct incoming damage instance still adds exactly ONE SECOND; cannot retroactively negate triggering damage. |
| **No Armor No Fear** (tyr_progression:bare_zerker_no_armor_no_fear) | 5 | +2 max HP per rank (+10 HP full) while enrolled and chest empty, with health clamping and no equipment-owned trait. |
| **Pain Into Purpose** (tyr_progression:bare_zerker_pain_into_purpose) | 4 | Following a qualifying hostile damage instance, +2% shieldless sword/axe melee damage per rank (+8% max) for three seconds; refresh only, no stacks or Fury from received pain. |
| **Unbroken Stance** (tyr_progression:bare_zerker_unbroken_stance) | 3 | Following real hostile incoming hit, +10 percentage points of knockback resistance per rank (+30 pp) for four seconds; no invulnerability frames added. |

#### Blood & Woad — 22 points

| Skill | One-point ranks | Proposed effect |
|---|---:|---|
| **Red Cap Fortitude** (tyr_progression:bare_zerker_redcap_fortitude) | 5 | +10% per rank (+50% max) to owner-verified temporary blood-dipped Red Cap Strength/Resistance duration while Cap worn; does not sell base cap or effects. |
| **Woad Attunement** (tyr_progression:bare_zerker_woad_attunement) | 5 | +10% per rank (+50% max) to genuine blue Woad application's duration, once pinned Neo Vitae 1.1.53 Sentient Armor-equivalent ability matrix is owner-approved; no fake armor slot or invented powers. |
| **Neo Vitae Bloodmaster** (tyr_progression:bare_zerker_neo_bloodmaster) | 6 | CANDIDATE +3% per rank (+18% max) to actual personally performed source-verified Neo Vitae blood magic beneficial effect output, with real EV payment and no altar/machine resource inflation. |
| **EvilCraft Hematurgy** (tyr_progression:bare_zerker_evilcraft_bloodcraft) | 6 | CANDIDATE +3% per rank (+18% max) to actual verified personal EvilCraft blood-powered effect duration or strength; real blood debit required; no infuser/ritual-machine output multiplication. |

#### Magebreaker — 12 points

| Skill | One-point ranks | Proposed effect |
|---|---:|---|
| **Antimagic Grit** (tyr_progression:bare_zerker_antimagic_grit) | 5 | -3% per rank (-15% max) source-tagged direct magical damage with chest empty, not a blanket reduction against every modded caster or true damage. |
| **Spellbreaker** (tyr_progression:bare_zerker_spellbreaker) | 4 | +4% per rank (+16% max) shieldless direct sword/axe damage to a target actively performing a confirmed real spell cast, not simply wearing a mage item. |
| **Hex Shatter** (tyr_progression:bare_zerker_hex_shatter) | 3 | -8% per rank (-24% max) duration of source-confirmed incoming harmful SPELL-origin status effects, without removing curses, ordinary potion effects or objective magic rules. |

#### Berserk Mastery — 10 points

| Skill | One-point ranks | Proposed effect |
|---|---:|---|
| **Last Stand Mastery** (tyr_progression:bare_zerker_last_stand_mastery) | 5 | For three seconds after a genuine FREE Too Angry to Die full vanilla Totem activation, +4% shieldless sword/axe direct melee damage per rank (+20% max). Does not alter >50 prehit condition, full Fury drain, Totem effects or zero cooldown. |
| **Red Mist** (tyr_progression:bare_zerker_red_mist) | 5 | While actual Fury strictly >75, chest empty and no shield, +3% sword/axe direct melee damage per rank (+15% max); threshold loss immediately disables. No Fury generation or decay alteration. |

**Total:** 22 purchased skills, exactly 100 one-point ranks; 13 original IDs preserved and 9 distinct additions. Regeneration/Resistance amplification changes **levels** only; it never changes the free one-second-per-hit duration. The +1 Fury per actual dealt damage, 1/s decay, >50 lethal full Totem and all-Fury drain are not purchases.

