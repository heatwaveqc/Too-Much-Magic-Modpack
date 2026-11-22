# TMM Progression — remaining trees, stronger ranks and exclusive Hunter specialties

**November 22, 2026 · Owner-reviewed direction implemented as DESIGN DRAFT ONLY · no live Path JSON updated**

The authoritative machine-readable catalog is `REMAINING-PATHS-100PT-FIRST-PASS.json` (schema `tmm_remaining_path_tree_blueprint_v2`). This document displays every proposed skill, rank count and full-rank price. The live Progression engine and **all real Hunter prices** remain untouched until explicitly accepted and ported.

## Governing design decisions

1. **Maximum investment is approximately 100 MXP points in a specialist tree**, except Monk, whose separate design is 1,000. **An entire visible catalog may cost more if it offers mutually exclusive branches.** That is intentional; 100 means the total purchasable for **one character in one tree**, not the sum of every alternative presented.
2. **Fewer, more meaningful ranks:** most previous draft ranks have been compressed by ~50%, while preserving the same total full-skill SP price and *target full-rank effect*. Purchases have explicit `rank_costs` (usually 2–3 SP/rank). A full-rank effect must be retuned in the actual handler; these files do not change current Java potency. One-rank capstones remain single purchases.
3. **Sea Dragon Abyssal Fortune** is **10 ranks × 5 MXP points = 50 points**, rather than 50×1. Each rank is proposed to give **five times the effect of one historical reference increment**, preserving the planned *cumulative Extreme ×50* investment at max, NOT promising a literal 50× loot chance or +50 raw vanilla Luck. Legal geared-source cap testing remains essential.
4. **Preserved known exceptions:** Core Reservoir I, Wellspring I, Thrift I and Potency I remain **5 ranks**. Gilded Fortune Mastery and Looting Mastery retain the owner-established **7 separate levels each**, granting +1 virtual enchantment per purchased rank. Native skills stay free.
5. **Hunter specialties use existing exclusive choice groups.** One Hunter can max 62 points of shared training, **one** 8-point Training Style, and **one** 30-point Favored Prey track. The Hunter selection is already offered for **14** zero-price species categories and remains one-of-fourteen. Other favorite categories remain visible alternatives that cannot all be bought or stacked in one build.
6. **No invented engine or extra Path wallets.** Core stays universal. MXP unified/default, hybrid and separate modes remain as planned in #475. JAWR/Dragon Survival/Goety own native transformation states and progression. Shared Progression owns all executable effect handlers; Path files are declarative.

## High-level budget

| Tree | Paid options in catalog | Max one-character investment | Rank model |
|---|---:|---:|---|
| **Core Skills** | 27 | **100** | Compressed, preserving full proposed maximum |
| **Cave Dragon** | 17 | **100** | Compressed, preserving full proposed maximum |
| **Sea Dragon** | 18 | **100** | Compressed, preserving full proposed maximum |
| **Forest Dragon** | 20 | **100** | Compressed, preserving full proposed maximum |
| **Vampire** | 17 | **100** | Compressed, preserving full proposed maximum |
| **Werewolf** | 18 | **100** | Compressed, preserving full proposed maximum |
| **Lich** | 12 | **100** | Compressed, preserving full proposed maximum |
| **Hunter** | 57 | **100** | 62 shared + 8 training choice + 30 favored prey choice |
| **Gilded** | 12 | **100** | Compressed, preserving full proposed maximum |
| **Bare Zerker** | 13 | **100** | Compressed, preserving full proposed maximum |

**Total:** 211 proposed paid nodes are *displayed* across these ten trees (57 displayed Hunter options). This does **not** imply any character can buy all 211. The per-tree maximums each remain 100 points; Monk remains separate at 1,000.

## Hunter — a 100-point build from a larger catalog

**Already live on `dev/progression`:** 17 purchased Hunter skills costing a combined 238 under their current rank pricing, plus 14 zero-cost `specialty_*` options in `tyr_progression:hunter_favored_enemy` and an exclusive two-option `hunter_training_style`. The new prices/ranks and new specialization effects below are proposals, not the installed configuration. All **17 paid source skill IDs** are retained somewhere in this proposed layout.

### Shared Hunter Training — 62 points (available to every Hunter)

| Skill | Proposed ranks | Purchase cost sequence | Total SP | New-rank effect target |
|---|---:|---|---:|---|
| Rooted (`tyr_progression:rooted`) | 3 | 2, 2, 1 | 5 | retain_actual_previous_max_as_candidate_not_approved_reprice |
| Monster Resilience (`tyr_progression:monster_resilience`) | 3 | 2, 2, 1 | 5 | retain_actual_previous_max_as_candidate_not_approved_reprice |
| Sword Specialist (`tyr_progression:sword_specialist`) | 3 | 2, 2, 1 | 5 | retain_actual_previous_max_as_candidate_not_approved_reprice |
| Axe Specialist (`tyr_progression:axe_specialist`) | 3 | 2, 2, 1 | 5 | retain_actual_previous_max_as_candidate_not_approved_reprice |
| Scythe Specialist (`tyr_progression:scythe_specialist`) | 3 | 2, 2, 1 | 5 | retain_actual_previous_max_as_candidate_not_approved_reprice |
| Bow Specialist (`tyr_progression:bow_specialist`) | 3 | 2, 2, 1 | 5 | retain_actual_previous_max_as_candidate_not_approved_reprice |
| Crossbow Specialist (`tyr_progression:crossbow_specialist`) | 3 | 2, 2, 1 | 5 | retain_actual_previous_max_as_candidate_not_approved_reprice |
| Fast Hands Bow (`tyr_progression:fast_hands_bow`) | 3 | 2, 2, 1 | 5 | retain_actual_previous_max_as_candidate_not_approved_reprice |
| Fast Hands Crossbow (`tyr_progression:fast_hands_crossbow`) | 3 | 2, 2, 1 | 5 | retain_actual_previous_max_as_candidate_not_approved_reprice |
| Silver Hunter (`tyr_progression:silver_hunter`) | 3 | 2, 2, 1 | 5 | retain_actual_previous_max_as_candidate_not_approved_reprice |
| Monster Lore (`tyr_progression:hunter_monster_lore`) | 2 | 2, 2 | 4 | Discover verified prey types and weaknesses without revealing arbitrary boss registries. |
| Quarry Mark (`tyr_progression:hunter_quarry_mark`) | 2 | 2, 2 | 4 | Mark one verified visible enemy quarry as the target of follow-on tracking bonuses. |
| Hunter Tracking (`tyr_progression:hunter_hunter_tracking`) | 2 | 2, 2 | 4 | Reveal eligible prey with temporary Glowing within safe bounded range and count. |

### Training Style — choose ONE (8 points)

The current engine already treats **Equipment Care** and **Fortified Training** as alternatives in `tyr_progression:hunter_training_style`. One is active/owned, not both; the proposed maximum is **8** regardless of what both visible entries total.

| Choice | Rank costs | Branch maximum | Existing source ID |
|---|---|---:|---|
| **Equipment Care** | 4, 4 | 8 | `tyr_progression:equipment_care` |
| **Fortified Training** | 4, 4 | 8 | `tyr_progression:fortified_training` |

### Favored Prey — choose ONE of 14 branches (30 points)

Each Hunter's selected **free** `specialty_*` license chooses a prey family. Only its corresponding three purchased upgrades are legal. Branches are mutually exclusive for purchase and effect activation (with validated respec/choice-switch rules), not just cosmetic graph connections. The pattern is **12 points of quarry offense, 8 points of quarry defense, 10 points of advanced family tactics**. Most new effects must still be validated against the pinned creature taxonomy. The first two families reuse older purchased skills; the other 12 are new data proposals.

#### Undead specialty — 30 points
Free selection: `tyr_progression:specialty_undead`

| Skill | Ranks | SP/rank | Full price | Candidate effect |
|---|---:|---|---:|---|
| **Undead Slayer** (`tyr_progression:undead_slayer`) | 4 | 3 | 12 | Strong offense against eligible undead |
| **Undead Evasion** (`tyr_progression:evasion_undead`) | 2 | 4 | 8 | Reflexes against undead attackers |
| **Ranged Quarry** (`tyr_progression:ranged_quarry`) | 2 | 5 | 10 | Verified ranged shots against undead; retain old bounded ranged-quarry rules |

#### Raider specialty — 30 points
Free selection: `tyr_progression:specialty_illager`

| Skill | Ranks | SP/rank | Full price | Candidate effect |
|---|---:|---|---:|---|
| **Raider Slayer** (`tyr_progression:raider_slayer`) | 4 | 3 | 12 | Improve direct hits against confirmed raiders |
| **Raider Evasion** (`tyr_progression:evasion_raiders`) | 2 | 4 | 8 | Evade raider attacks |
| **Raidbreaker** (`tyr_progression:hunter_illager_mastery`) | 2 | 5 | 10 | Improve defense against valid raid weapons/magic; not global villager-raid immunity |

#### Drakebane specialty — 30 points
Free selection: `tyr_progression:specialty_dragon`

| Skill | Ranks | SP/rank | Full price | Candidate effect |
|---|---:|---|---:|---|
| **Drakebane** (`tyr_progression:hunter_dragon_slayer`) | 4 | 3 | 12 | Damage legal Dragon taxonomy quarry |
| **Scaled Hunter** (`tyr_progression:hunter_dragon_defense`) | 2 | 4 | 8 | Defend against verified dragon attacks, source-scoped |
| **Dragonbane Tactics** (`tyr_progression:hunter_dragon_mastery`) | 2 | 5 | 10 | Improve genuine dragon-hunt timing and damage without affecting all bosses |

#### Void specialty — 30 points
Free selection: `tyr_progression:specialty_void`

| Skill | Ranks | SP/rank | Full price | Candidate effect |
|---|---:|---|---:|---|
| **Void Slayer** (`tyr_progression:hunter_void_slayer`) | 4 | 3 | 12 | Damage confirmed End/void quarry |
| **Voidwalker Guard** (`tyr_progression:hunter_void_defense`) | 2 | 4 | 8 | Defend against valid void creature attacks |
| **Rift Tracker** (`tyr_progression:hunter_void_mastery`) | 2 | 5 | 10 | Keep marked teleporting quarry reacquirable within limits |

#### Piglin specialty — 30 points
Free selection: `tyr_progression:specialty_piglin`

| Skill | Ranks | SP/rank | Full price | Candidate effect |
|---|---:|---|---:|---|
| **Piglin Slayer** (`tyr_progression:hunter_piglin_slayer`) | 4 | 3 | 12 | Damage approved Piglin/Hoglin quarry |
| **Bastion Guard** (`tyr_progression:hunter_piglin_defense`) | 2 | 4 | 8 | Defend against Piglin/Hoglin attacks |
| **Hoglin Harrier** (`tyr_progression:hunter_piglin_mastery`) | 2 | 5 | 10 | Improve direct melee vs those groups without inheriting Gilded's diplomacy |

#### Sculk specialty — 30 points
Free selection: `tyr_progression:specialty_sculk`

| Skill | Ranks | SP/rank | Full price | Candidate effect |
|---|---:|---|---:|---|
| **Sculk Slayer** (`tyr_progression:hunter_sculk_slayer`) | 4 | 3 | 12 | Damage approved Sculk prey, including genuine Warden where eligible |
| **Deepdark Vigilance** (`tyr_progression:hunter_sculk_defense`) | 2 | 4 | 8 | Resist verified Sculk/sonic damage types only |
| **Sonic Defense** (`tyr_progression:hunter_sculk_mastery`) | 2 | 5 | 10 | Recognize and evade valid sonic attacks without silencing arbitrary sensors |

#### Aquatic specialty — 30 points
Free selection: `tyr_progression:specialty_aquatic`

| Skill | Ranks | SP/rank | Full price | Candidate effect |
|---|---:|---|---:|---|
| **Aquatic Slayer** (`tyr_progression:hunter_aquatic_slayer`) | 4 | 3 | 12 | Damage actual aquatic prey taxonomy |
| **Deepwater Guard** (`tyr_progression:hunter_aquatic_defense`) | 2 | 4 | 8 | Reduce underwater prey attacks, not all drowning |
| **Harpoon Hunter** (`tyr_progression:hunter_aquatic_mastery`) | 2 | 5 | 10 | Improve actual underwater bow/trident/projectile fights with legal attribution |

#### Construct specialty — 30 points
Free selection: `tyr_progression:specialty_construct`

| Skill | Ranks | SP/rank | Full price | Candidate effect |
|---|---:|---|---:|---|
| **Construct Slayer** (`tyr_progression:hunter_construct_slayer`) | 4 | 3 | 12 | Damage eligible construct targets only |
| **Construct Plating** (`tyr_progression:hunter_construct_defense`) | 2 | 4 | 8 | Improve defense against authentic construct attacks |
| **Golem Breaker** (`tyr_progression:hunter_construct_mastery`) | 2 | 5 | 10 | Armor penetration against constructs if supported by native attribute and creature tags |

#### Beast specialty — 30 points
Free selection: `tyr_progression:specialty_beast`

| Skill | Ranks | SP/rank | Full price | Candidate effect |
|---|---:|---|---:|---|
| **Beast Slayer** (`tyr_progression:hunter_beast_slayer`) | 4 | 3 | 12 | Damage actual eligible wild beasts, not pets |
| **Beast Evasion** (`tyr_progression:hunter_beast_defense`) | 2 | 4 | 8 | Dodge real hostile beast attacks |
| **Apex Pursuit** (`tyr_progression:hunter_beast_mastery`) | 2 | 5 | 10 | Improve tracking/close pursuit of valid beast quarry |

#### Arthropod specialty — 30 points
Free selection: `tyr_progression:specialty_arthropod`

| Skill | Ranks | SP/rank | Full price | Candidate effect |
|---|---:|---|---:|---|
| **Arthropod Slayer** (`tyr_progression:hunter_arthropod_slayer`) | 4 | 3 | 12 | Damage spiders/arthropods by verified taxonomy |
| **Venom Guard** (`tyr_progression:hunter_arthropod_defense`) | 2 | 4 | 8 | Reduce arthropod-specific poison damage/effects |
| **Webbreaker** (`tyr_progression:hunter_arthropod_mastery`) | 2 | 5 | 10 | Improve mobility when trapped by valid spider web sources |

#### Plant specialty — 30 points
Free selection: `tyr_progression:specialty_plant`

| Skill | Ranks | SP/rank | Full price | Candidate effect |
|---|---:|---|---:|---|
| **Plant Slayer** (`tyr_progression:hunter_plant_slayer`) | 4 | 3 | 12 | Damage verified plant taxonomy including eligible Creepers |
| **Spore Guard** (`tyr_progression:hunter_plant_defense`) | 2 | 4 | 8 | Limit effects from verified botanical attacks, not all poison |
| **Creeper Ward** (`tyr_progression:hunter_plant_mastery`) | 2 | 5 | 10 | Reduce actual Creeper explosion source types only |

#### Elemental specialty — 30 points
Free selection: `tyr_progression:specialty_elemental`

| Skill | Ranks | SP/rank | Full price | Candidate effect |
|---|---:|---|---:|---|
| **Elemental Slayer** (`tyr_progression:hunter_elemental_slayer`) | 4 | 3 | 12 | Damage verified elemental prey |
| **Primordial Guard** (`tyr_progression:hunter_elemental_defense`) | 2 | 4 | 8 | Reduce actual tagged elemental-origin attack types with source evidence |
| **Elemental Analysis** (`tyr_progression:hunter_elemental_mastery`) | 2 | 5 | 10 | Boost effectiveness when countering selected verified elemental weaknesses |

#### Ooze specialty — 30 points
Free selection: `tyr_progression:specialty_ooze`

| Skill | Ranks | SP/rank | Full price | Candidate effect |
|---|---:|---|---:|---|
| **Ooze Slayer** (`tyr_progression:hunter_ooze_slayer`) | 4 | 3 | 12 | Damage verified slime/ooze prey |
| **Slime Resilience** (`tyr_progression:hunter_ooze_defense`) | 2 | 4 | 8 | Reduce legitimate ooze melee/slowness effects |
| **Viscous Tactics** (`tyr_progression:hunter_ooze_mastery`) | 2 | 5 | 10 | Improve mobility and attacks when engulfed/slowed by ooze |

#### Demon specialty — 30 points
Free selection: `tyr_progression:specialty_demon`

| Skill | Ranks | SP/rank | Full price | Candidate effect |
|---|---:|---|---:|---|
| **Demon Slayer** (`tyr_progression:hunter_demon_slayer`) | 4 | 3 | 12 | Damage verified demon prey |
| **Infernal Guard** (`tyr_progression:hunter_demon_defense`) | 2 | 4 | 8 | Reduce genuine demonic attacks using source IDs |
| **Exorcist** (`tyr_progression:hunter_demon_mastery`) | 2 | 5 | 10 | Improve combat against demon summons without harming player-owned servants |

### Hunter budget proof

- Shared training: 10 existing core Hunter abilities ×5 points + 3 new Lore/Mark/Tracking skills ×4 = **62**.
- Exactly one existing Training Style: two ranks at 4 SP/rank = **8**.
- Exactly one Favored Prey specialty: offense four ranks ×3 SP =12, defense two ranks ×4 SP =8, mastery two ranks ×5 SP =10, totaling **30**.
- **One Hunter's attainable spend = 62 + 8 + 30 = 100**. Visible alternative catalog spend = 62 + (2×8) + (14×30) = **498**, deliberately unattainable on one Hunter. Fourteen selection licenses are still free.
- New 57 paid Hunter catalog nodes + 14 free existing selection nodes would be **71 visible nodes** if the full proposal is activated, but only 13 shared + 1 style + 3 specialty = **17 purchasable nodes** for the same Hunter, in addition to the free choice license.
- Existing live `undead_slayer`, `raider_slayer`, `evasion_undead`, `evasion_raiders` and `ranged_quarry` become **specialty-locked** by the appropriate existing choice; source max power and exact taxonomy need retuning/testing before activating. Current `silver_hunter` stays generally available to equipped silver hunters.

## Other established Path and universal Core skill budgets

Every row is **design-only**. Rank-cost sequences sum to the same prior proposed total, even though ranks decrease. Prior full-rank strength is a **target to be measured**, not a computed live Java effect.

### Core Skills — 100 points / 27 skills

*Identity and native guardrails:* One global Core independent of Newbie/Normie and specialist identity; no Core rank duplicated in specialist trees.

#### Universal magic fundamentals — 37 points

| Skill | Old draft ranks | New ranks | SP cost per rank | Full skill cost | Intended result |
|---|---:|---:|---|---:|---|
| **Reservoir I** (`tyr_progression:core_reservoir_i`) | 5 | **5** | 1, 1, 1, 1, 1 | 5 | Personal maximum mana/resources. Initial four runtime preview nodes preserve their original absolute rank-5 ceilings until per-mod base normalization is source-verified. |
| **Wellspring I** (`tyr_progression:core_wellspring_i`) | 5 | **5** | 1, 1, 1, 1, 1 | 5 | Personal magical regeneration; dragon forms may purchase this UNIVERSAL option, but have no dragon-exclusive magic regen nodes. |
| **Thrift I** (`tyr_progression:core_thrift_i`) | 5 | **5** | 1, 1, 1, 1, 1 | 5 | Discount personal magic payments; original resource-specific hard ceiling and minimum positive payment apply. |
| **Potency I** (`tyr_progression:core_potency_i`) | 5 | **5** | 1, 1, 1, 1, 1 | 5 | Base personal spell effect strength; source-specific spells and damage tags. Existing four preview attributes stay exact until normalized. |
| **Quickening** (`tyr_progression:core_quickening`) | 3 | **2** | 1, 2 | 3 | Personal spell casting speed, not industrial throughput. |
| **Projection** (`tyr_progression:core_projection`) | 3 | **2** | 1, 2 | 3 | Valid spell target range, not free global AoE. |
| **Persistence** (`tyr_progression:core_persistence`) | 5 | **3** | 1, 2, 2 | 5 | Duration of supported temporary effects and personally owned summons. |
| **Conjuration** (`tyr_progression:core_conjuration`) | 3 | **2** | 1, 2 | 3 | Bounded strength of personally owned summoned allies, not free summoned mob count. |
| **Warding** (`tyr_progression:core_warding`) | 3 | **2** | 1, 2 | 3 | Source-verified magical damage resistance, hard mitigation cap. |

#### Retained Tyz 6.4.2 source skills (proposed rehomes) — 63 points

| Skill | Old draft ranks | New ranks | SP cost per rank | Full skill cost | Intended result |
|---|---:|---:|---|---:|---|
| **Health Boost** (`tyr_progression:core_health_boost`) | 5 | **3** | 1, 2, 2 | 5 | Increases maximum health by {value} half hearts |
| **Oxygen Boost** (`tyr_progression:core_oxygen_boost`) | 3 | **2** | 1, 2 | 3 | Increases underwater breathing time by {value}% |
| **Block Reach** (`tyr_progression:core_block_reach`) | 2 | **1** | 2 | 2 | Increases the range at which the player can place and destroy by {value} blocks |
| **Attack Damage** (`tyr_progression:core_attack_damage`) | 3 | **2** | 1, 2 | 3 | Increases melee damage by {value} |
| **Sneaking Speed** (`tyr_progression:core_sneaking_speed`) | 7 | **4** | 1, 2, 2, 2 | 7 | Increases movement speed while sneaking by {value}% |
| **speed_boost** (`tyr_progression:core_speed_boost`) | 3 | **2** | 1, 2 | 3 | NO ENGLISH STRING FOUND |
| **Step Height** (`tyr_progression:core_step_height`) | 2 | **1** | 2 | 2 | Allows stepping up blocks up to {value} blocks high without jumping |
| **Swim Speed** (`tyr_progression:core_swim_speed`) | 5 | **3** | 1, 2, 2 | 5 | Increases swim speed by {value}% |
| **Excavation** (`tyr_progression:core_excavation`) | 6 | **3** | 2, 2, 2 | 6 | Increases mining speed by {value}% |
| **Swift Learning** (`tyr_progression:core_swift_learning`) | 5 | **3** | 1, 2, 2 | 5 | Increases Skill XP gain by {value}% |
| **Metabolism** (`tyr_progression:core_metabolism`) | 3 | **2** | 1, 2 | 3 | Increases food value (nutrition & saturation) by {nutrition_bonus}% |
| **Wisdom** (`tyr_progression:core_wisdom`) | 3 | **2** | 1, 2 | 3 | Increases Vanilla XP gain by {bonus_percentage}% |
| **Stealth** (`tyr_progression:core_stealth`) | 5 | **3** | 1, 2, 2 | 5 | Reduces the detection range of hostile mobs by {range_reduction}% |
| **Magnet** (`tyr_progression:core_magnet`) | 2 | **1** | 2 | 2 | Attracts items within a {block_radius} block radius |
| **Spare Parts** (`tyr_progression:core_spare_parts`) | 2 | **1** | 2 | 2 | Grants a {refund_chance}% chance for each ingredient to be recovered when crafting. Up to {max_materials} item(s) per craft |
| **Twist Of Fate** (`tyr_progression:core_twist_of_fate`) | 3 | **2** | 1, 2 | 3 | Increases the quantity of items found in structure loot chests by {loot_multiplier}% |
| **Safekept** (`tyr_progression:core_safekept`) | 2 | **1** | 2 | 2 | Safeguards the first {saved_slots} slot(s) of your inventory upon death |
| **Haggler** (`tyr_progression:core_haggler`) | 2 | **1** | 2 | 2 | Holding an Emerald attracts villagers within a {block_radius} block radius.  They will follow you with a {villager_speed}% speed bonus |

### Cave Dragon — 100 points / 17 skills

*Identity and native guardrails:* Keep JAWR/Goety/Dragon Survival authority over native physiology and progression; bought ranks enhance, not replace.

#### Universal Dragon Physiology & Sorcery (species-local) — 37 points

| Skill | Old draft ranks | New ranks | SP cost per rank | Full skill cost | Intended result |
|---|---:|---:|---|---:|---|
| **Draconic Reservoir** (`tyr_progression:cave_dragon_draconic_reservoir`) | 6 | **3** | 2, 2, 2 | 6 | Massive bonus to personally usable maximum magic resource capacity; never machine/world storage. |
| **Draconic Potency** (`tyr_progression:cave_dragon_draconic_potency`) | 4 | **2** | 2, 2 | 4 | Very high general personal spell potency per SP; additive by same reference with typed affinity, not multiplicative recursion. |
| **Draconic Scales** (`tyr_progression:cave_dragon_draconic_scales`) | 8 | **4** | 2, 2, 2, 2 | 8 | Species natural armor; deliberately inefficient per SP versus specialized martial defense. |
| **Draconic Claws** (`tyr_progression:cave_dragon_draconic_claws`) | 8 | **4** | 2, 2, 2, 2 | 8 | Real dragon form physical strike boost; not global magic damage. |
| **Draconic Talons** (`tyr_progression:cave_dragon_draconic_talons`) | 11 | **6** | 1, 2, 2, 2, 2, 2 | 11 | Improve legal dragon claw harvest rate, not tool-tier bypass. |

#### Claw & Fang — 17 points

| Skill | Old draft ranks | New ranks | SP cost per rank | Full skill cost | Intended result |
|---|---:|---:|---|---:|---|
| **Stone Claws** (`tyr_progression:cave_dragon_stone_claws`) | 8 | **4** | 2, 2, 2, 2 | 8 | Scale unarmed/claw damage only in dragon body; no weapon-wide bonus. |
| **Crushing Bite** (`tyr_progression:cave_dragon_crushing_bite`) | 5 | **3** | 1, 2, 2 | 5 | Bounded armor/knockback pressure on confirmed bite hits. |
| **Seismic Pounce** (`tyr_progression:cave_dragon_seismic_pounce`) | 4 | **2** | 2, 2 | 4 | Cooldown-limited landing shockwave against entities; terrain damage off by default. |

#### Stone Scales — 16 points

| Skill | Old draft ranks | New ranks | SP cost per rank | Full skill cost | Intended result |
|---|---:|---:|---|---:|---|
| **Stonehide** (`tyr_progression:cave_dragon_stonehide`) | 8 | **4** | 2, 2, 2, 2 | 8 | Incremental natural armor; respect equipment stacking caps. |
| **Earthbound Poise** (`tyr_progression:cave_dragon_earthbound_poise`) | 5 | **3** | 1, 2, 2 | 5 | Reduce knockback and fall damage while grounded. |
| **Mountain Bastion** (`tyr_progression:cave_dragon_mountain_bastion`) | 3 | **2** | 1, 2 | 3 | Short defensive stance with a resource cost and meaningful cooldown, not permanent immunity. |

#### Excavation — 19 points

| Skill | Old draft ranks | New ranks | SP cost per rank | Full skill cost | Intended result |
|---|---:|---:|---|---:|---|
| **Tunneler** (`tyr_progression:cave_dragon_tunneler`) | 11 | **6** | 1, 2, 2, 2, 2, 2 | 11 | Improve legal claw-mining speed; preserve vanilla/tool harvest checks. |
| **Vein Sense** (`tyr_progression:cave_dragon_vein_sense`) | 5 | **3** | 1, 2, 2 | 5 | Short-range, timed ore awareness without unrestricted x-ray or chunk scans. |
| **Deep Excavation** (`tyr_progression:cave_dragon_deep_excavation`) | 3 | **2** | 1, 2 | 3 | Controlled multi-block excavation; count actual blocks, honor claims and server limits. |

#### Infernal Attunement — 11 points

| Skill | Old draft ranks | New ranks | SP cost per rank | Full skill cost | Intended result |
|---|---:|---:|---|---:|---|
| **Infernal Resonance** (`tyr_progression:cave_dragon_infernal_resonance`) | 6 | **3** | 2, 2, 2 | 6 | Bonus to source-verified demonic/infernal personal magic, beyond shared dragon arcane power but not world rituals. |
| **Hellbound Command** (`tyr_progression:cave_dragon_hellbound_command`) | 3 | **2** | 1, 2 | 3 | Strengthen eligible player-owned demonic servants and source-verified infernal spell interactions. |
| **Infernal Dominion** (`tyr_progression:cave_dragon_infernal_dominion`) | 2 | **1** | 2 | 2 | Late specialization in demon-affiliated spell potency; strong, narrowly classified and gated. |

### Sea Dragon — 100 points / 18 skills

*Identity and native guardrails:* Keep JAWR/Goety/Dragon Survival authority over native physiology and progression; bought ranks enhance, not replace.

**Sea-specific:** Abyssal Fortune is deliberately reduced from **50 ranks ×1 SP** to **10 ranks ×5 SP**. Maximum planned total power unchanged; individual purchases 5× stronger. Actual final Luck must respect the best legal geared build and Sea Dragon vs Monk benchmark.

#### Universal Dragon Physiology & Sorcery (species-local) — 19 points

| Skill | Old draft ranks | New ranks | SP cost per rank | Full skill cost | Intended result |
|---|---:|---:|---|---:|---|
| **Draconic Reservoir** (`tyr_progression:sea_dragon_draconic_reservoir`) | 3 | **2** | 1, 2 | 3 | Massive bonus to personally usable maximum magic resource capacity; never machine/world storage. |
| **Draconic Potency** (`tyr_progression:sea_dragon_draconic_potency`) | 2 | **1** | 2 | 2 | Very high general personal spell potency per SP; additive by same reference with typed affinity, not multiplicative recursion. |
| **Draconic Scales** (`tyr_progression:sea_dragon_draconic_scales`) | 4 | **2** | 2, 2 | 4 | Species natural armor; deliberately inefficient per SP versus specialized martial defense. |
| **Draconic Claws** (`tyr_progression:sea_dragon_draconic_claws`) | 4 | **2** | 2, 2 | 4 | Real dragon form physical strike boost; not global magic damage. |
| **Draconic Talons** (`tyr_progression:sea_dragon_draconic_talons`) | 6 | **3** | 2, 2, 2 | 6 | Improve legal dragon claw harvest rate, not tool-tier bypass. |

#### Tideshaping — 8 points

| Skill | Old draft ranks | New ranks | SP cost per rank | Full skill cost | Intended result |
|---|---:|---:|---|---:|---|
| **Current Rider** (`tyr_progression:sea_dragon_current_rider`) | 4 | **2** | 2, 2 | 4 | Improve movement control in water, including currents. |
| **Tidal Strike** (`tyr_progression:sea_dragon_tidal_strike`) | 2 | **1** | 2 | 2 | Enhance wet-target combat and native aquatic attacks, not generic damage. |
| **Undertow** (`tyr_progression:sea_dragon_undertow`) | 2 | **1** | 2 | 2 | Cooldown-limited pull/slow applied to legal targets without moving world water blocks. |

#### Abyssal Defense — 8 points

| Skill | Old draft ranks | New ranks | SP cost per rank | Full skill cost | Intended result |
|---|---:|---:|---|---:|---|
| **Pressure Adaptation** (`tyr_progression:sea_dragon_pressure_adaptation`) | 4 | **2** | 2, 2 | 4 | Reduce underwater environmental penalties. |
| **Brine Scales** (`tyr_progression:sea_dragon_brine_scales`) | 2 | **1** | 2 | 2 | Situational protection while submerged; no blanket armor bonus on land. |
| **Deepwater Refuge** (`tyr_progression:sea_dragon_deepwater_refuge`) | 2 | **1** | 2 | 2 | Bounded underwater healing or defense with a meaningful resource/cooldown cost. |

#### Swimmer's Grace — 9 points

| Skill | Old draft ranks | New ranks | SP cost per rank | Full skill cost | Intended result |
|---|---:|---:|---|---:|---|
| **Sleek Form** (`tyr_progression:sea_dragon_sleek_form`) | 5 | **3** | 1, 2, 2 | 5 | Increase underwater speed, capped against sprint/exotic gear. |
| **Slipstream** (`tyr_progression:sea_dragon_slipstream`) | 2 | **1** | 2 | 2 | Short burst dodge in water with collision safety. |
| **Ocean Leap** (`tyr_progression:sea_dragon_ocean_leap`) | 2 | **1** | 2 | 2 | Leap from water and control reentry without free flight. |

#### Tidal & Abyssal Magic — 6 points

| Skill | Old draft ranks | New ranks | SP cost per rank | Full skill cost | Intended result |
|---|---:|---:|---|---:|---|
| **Tidal Attunement** (`tyr_progression:sea_dragon_tidal_attunement`) | 3 | **2** | 1, 2 | 3 | Enhance verified water, ocean and cold-themed personal spell damage/effects, not arbitrary wet status. |
| **Stormcalling** (`tyr_progression:sea_dragon_stormcalling`) | 2 | **1** | 2 | 2 | Enhance verified lightning/storm magic and aquatic control where supported; no unconditional world weather control. |
| **Abyssal Sovereignty** (`tyr_progression:sea_dragon_abyssal_sovereignty`) | 1 | **1** | 1 | 1 | High-level sea/abyssal spell focus, not an assumption that Occultism automatically counts as ocean magic. |

#### Abyssal Fortune — 50 points

| Skill | Old draft ranks | New ranks | SP cost per rank | Full skill cost | Intended result |
|---|---:|---:|---|---:|---|
| **Abyssal Fortune** (`tyr_progression:sea_dragon_abyssal_fortune`) | 50 | **10** | 5, 5, 5, 5, 5, 5, 5, 5, 5, 5 | 50 | Extremely high Sea Dragon luck; powerful personal Luck-stat and source-verified luck-sensitive results, but no unconditional duplicate item generation or boss/quest gate bypass. |

### Forest Dragon — 100 points / 20 skills

*Identity and native guardrails:* Keep JAWR/Goety/Dragon Survival authority over native physiology and progression; bought ranks enhance, not replace.

#### Universal Dragon Physiology & Sorcery (species-local) — 37 points

| Skill | Old draft ranks | New ranks | SP cost per rank | Full skill cost | Intended result |
|---|---:|---:|---|---:|---|
| **Draconic Reservoir** (`tyr_progression:forest_dragon_draconic_reservoir`) | 6 | **3** | 2, 2, 2 | 6 | Massive bonus to personally usable maximum magic resource capacity; never machine/world storage. |
| **Draconic Potency** (`tyr_progression:forest_dragon_draconic_potency`) | 4 | **2** | 2, 2 | 4 | Very high general personal spell potency per SP; additive by same reference with typed affinity, not multiplicative recursion. |
| **Draconic Scales** (`tyr_progression:forest_dragon_draconic_scales`) | 8 | **4** | 2, 2, 2, 2 | 8 | Species natural armor; deliberately inefficient per SP versus specialized martial defense. |
| **Draconic Claws** (`tyr_progression:forest_dragon_draconic_claws`) | 8 | **4** | 2, 2, 2, 2 | 8 | Real dragon form physical strike boost; not global magic damage. |
| **Draconic Talons** (`tyr_progression:forest_dragon_draconic_talons`) | 11 | **6** | 1, 2, 2, 2, 2, 2 | 11 | Improve legal dragon claw harvest rate, not tool-tier bypass. |

#### Verdant Vitality — 17 points

| Skill | Old draft ranks | New ranks | SP cost per rank | Full skill cost | Intended result |
|---|---:|---:|---|---:|---|
| **Green Recovery** (`tyr_progression:forest_dragon_green_recovery`) | 8 | **4** | 2, 2, 2, 2 | 8 | Small recovery benefit in living biomes, not in all dimensions. |
| **Living Bark** (`tyr_progression:forest_dragon_living_bark`) | 5 | **3** | 1, 2, 2 | 5 | Situational natural defense near vegetation, with stacking caps. |
| **Elder Grove** (`tyr_progression:forest_dragon_elder_grove`) | 4 | **2** | 2, 2 | 4 | Time-limited regeneration/support pulse, costing dragon resource. |

#### Thorn & Root — 12 points

| Skill | Old draft ranks | New ranks | SP cost per rank | Full skill cost | Intended result |
|---|---:|---:|---|---:|---|
| **Bramble Claw** (`tyr_progression:forest_dragon_bramble_claw`) | 6 | **3** | 2, 2, 2 | 6 | Add a bounded nature-themed status to confirmed claw attacks. |
| **Entangling Breath** (`tyr_progression:forest_dragon_entangling_breath`) | 4 | **2** | 2, 2 | 4 | Native breath enhancer applying short slows only where source breath supports it. |
| **Thornstorm** (`tyr_progression:forest_dragon_thornstorm`) | 2 | **1** | 2 | 2 | Cooldown-limited small-area crowd control with no world block destruction. |

#### Fertility & Bounty — 12 points

| Skill | Old draft ranks | New ranks | SP cost per rank | Full skill cost | Intended result |
|---|---:|---:|---|---:|---|
| **Germination Touch** (`tyr_progression:forest_dragon_germination_touch`) | 4 | **2** | 2, 2 | 4 | Enhance actual bonemeal application efficiency/area/yield without free unbounded creation of growth. |
| **Breeder's Blessing** (`tyr_progression:forest_dragon_breeders_blessing`) | 4 | **2** | 2, 2 | 4 | Improve legal animal breeding/cooldown or food efficiency via real pair interaction; never spawn animals without parents. |
| **Harvest Bounty** (`tyr_progression:forest_dragon_harvest_bounty`) | 4 | **2** | 2, 2 | 4 | Increase real crop harvest outcomes with explicit per-block credit and cooldown/caps; preserve claim and mature crop checks. |

#### Fae Affinity — 12 points

| Skill | Old draft ranks | New ranks | SP cost per rank | Full skill cost | Intended result |
|---|---:|---:|---|---:|---|
| **Fey Courtesy** (`tyr_progression:forest_dragon_fey_courtesy`) | 6 | **3** | 2, 2, 2 | 6 | Improve source-verified fae-aligned personal magic effects and fae relations; no universal mob pacification. |
| **Fey Conduit** (`tyr_progression:forest_dragon_fey_conduit`) | 4 | **2** | 2, 2 | 4 | Specialize fae-aligned spell potency and owned fae summons at higher benefit per SP. |
| **Grove Guardian** (`tyr_progression:forest_dragon_grove_guardian`) | 2 | **1** | 2 | 2 | Bounded fae-aligned personal magic and allied-support synergy without taking over world production. |

#### Retained Tyz 6.4.2 source skills — 10 points

| Skill | Old draft ranks | New ranks | SP cost per rank | Full skill cost | Intended result |
|---|---:|---:|---|---:|---|
| **Venomous Strike** (`tyr_progression:forest_dragon_venomous_strike`) | 4 | **2** | 2, 2 | 4 | Adds a {success_probability}% chance to poison targets on hit |
| **Green Thumb** (`tyr_progression:forest_dragon_green_thumb`) | 3 | **2** | 1, 2 | 3 | Adds a {success_probability}% chance to double drops when harvesting crops and plants |
| **Shepherd’s Blessing** (`tyr_progression:forest_dragon_shepherds_blessing`) | 3 | **2** | 1, 2 | 3 | Baby animals grow {growth_speed}% faster |

### Vampire — 100 points / 17 skills

*Identity and native guardrails:* Keep JAWR/Goety/Dragon Survival authority over native physiology and progression; bought ranks enhance, not replace.

#### Bloodcraft — 18 points

| Skill | Old draft ranks | New ranks | SP cost per rank | Full skill cost | Intended result |
|---|---:|---:|---|---:|---|
| **Efficient Feeding** (`tyr_progression:vampire_efficient_feeding`) | 9 | **5** | 1, 2, 2, 2, 2 | 9 | Improve real blood gained from eligible feeding transactions; cannot create blood from empty actions. |
| **Deep Reservoir** (`tyr_progression:vampire_deep_reservoir`) | 5 | **3** | 1, 2, 2 | 5 | Increase one authoritative usable blood capacity, not two independent HUD pools. |
| **Crimson Renewal** (`tyr_progression:vampire_crimson_renewal`) | 4 | **2** | 2, 2 | 4 | Trade a real measured blood debit for bounded emergency healing. |

#### Nightstalker — 21 points

| Skill | Old draft ranks | New ranks | SP cost per rank | Full skill cost | Intended result |
|---|---:|---:|---|---:|---|
| **Nocturnal Vision** (`tyr_progression:vampire_nocturnal_vision`) | 12 | **6** | 2, 2, 2, 2, 2, 2 | 12 | Better low-light perception with native-sunlight penalties untouched. |
| **Shadow Stalker** (`tyr_progression:vampire_shadow_stalker`) | 5 | **3** | 1, 2, 2 | 5 | Short stealth advantage in darkness; avoid invisibility in full daylight. |
| **Bat Mastery** (`tyr_progression:vampire_bat_mastery`) | 4 | **2** | 2, 2 | 4 | Improve native bat-form controls/efficiency after that native ability is acquired. |

#### Predation — 17 points

| Skill | Old draft ranks | New ranks | SP cost per rank | Full skill cost | Intended result |
|---|---:|---:|---|---:|---|
| **Fang Training** (`tyr_progression:vampire_fang_training`) | 8 | **4** | 2, 2, 2, 2 | 8 | Scale only verified vampire bite/melee attacks. |
| **Measured Feast** (`tyr_progression:vampire_measured_feast`) | 5 | **3** | 1, 2, 2 | 5 | Limited post-kill blood recovery from eligible kills, with transaction deduplication. |
| **Blood Duelist** (`tyr_progression:vampire_blood_duelist`) | 4 | **2** | 2, 2 | 4 | Focused single-target bonuses under low blood; prevent intentional zero-cost loops. |

#### Undead Constitution — 17 points

| Skill | Old draft ranks | New ranks | SP cost per rank | Full skill cost | Intended result |
|---|---:|---:|---|---:|---|
| **Pale Resilience** (`tyr_progression:vampire_pale_resilience`) | 8 | **4** | 2, 2, 2, 2 | 8 | Bounded poison/disease-type mitigation where the source system allows it. |
| **Sunshroud** (`tyr_progression:vampire_sunshroud`) | 5 | **3** | 1, 2, 2 | 5 | Short resource-funded sunlight resistance; never erase core daylight tradeoffs. |
| **Elder Presence** (`tyr_progression:vampire_elder_presence`) | 4 | **2** | 2, 2 | 4 | Bounded defensive/control aura with cooldown, not full charm immunity. |

#### Retained Vampire Skill Concepts (JAWR-owned) — 22 points

| Skill | Old draft ranks | New ranks | SP cost per rank | Full skill cost | Intended result |
|---|---:|---:|---|---:|---|
| **Blood Thrift** (`tyr_progression:vampire_blood_thrift`) | 5 | **3** | 1, 2, 2 | 5 | Reduce only real JAWR blood expenditure/hunger cost on verified committed actions. |
| **Predatory Finesse** (`tyr_progression:vampire_predatory_finesse`) | 5 | **3** | 1, 2, 2 | 5 | Blood-funded vampire-specific melee attack speed, not spell casting speed. |
| **Night Runner** (`tyr_progression:vampire_night_runner`) | 8 | **4** | 2, 2, 2, 2 | 8 | Improved vampire walking/running mobility in darkness; no universal free speed. |
| **Crimson Surge** (`tyr_progression:vampire_crimson_surge`) | 4 | **2** | 2, 2 | 4 | Blood-paid high-stage predatory burst, supplementing Blood Regent without minions. |

#### Retained Tyz 6.4.2 source skills — 5 points

| Skill | Old draft ranks | New ranks | SP cost per rank | Full skill cost | Intended result |
|---|---:|---:|---|---:|---|
| **Bloodlust** (`tyr_progression:vampire_bloodlust`) | 5 | **3** | 1, 2, 2 | 5 | Killing a hostile mob regenerates {health_percentage}% of its max health |

### Werewolf — 100 points / 18 skills

*Identity and native guardrails:* Keep JAWR/Goety/Dragon Survival authority over native physiology and progression; bought ranks enhance, not replace.

#### Lupine Vitality — 17 points

| Skill | Old draft ranks | New ranks | SP cost per rank | Full skill cost | Intended result |
|---|---:|---:|---|---:|---|
| **Feral Vigor** (`tyr_progression:werewolf_feral_vigor`) | 8 | **4** | 2, 2, 2, 2 | 8 | Large additional maximum health / extra hearts, materially more per SP than universal vitality boosts. |
| **Regenerative Flesh** (`tyr_progression:werewolf_regenerative_flesh`) | 5 | **3** | 1, 2, 2 | 5 | Strong, bounded actual HP regeneration under verified werewolf physiology and legal feeding/hunger rules; not mana regen. |
| **Unbreakable Beast** (`tyr_progression:werewolf_unbreakable_beast`) | 4 | **2** | 2, 2 | 4 | High-level extra HP and in-combat wound recovery with caps and meaningful werewolf vulnerabilities. |

#### Feral Might — 17 points

| Skill | Old draft ranks | New ranks | SP cost per rank | Full skill cost | Intended result |
|---|---:|---:|---|---:|---|
| **Rending Claws** (`tyr_progression:werewolf_rending_claws`) | 8 | **4** | 2, 2, 2, 2 | 8 | Scale claw damage in valid wolf/beast form, respecting weapon modifiers. |
| **Savage Momentum** (`tyr_progression:werewolf_savage_momentum`) | 5 | **3** | 1, 2, 2 | 5 | Short stacking melee bonus after actual hits, capped and reset out of combat. |
| **Primal Fury** (`tyr_progression:werewolf_primal_fury`) | 4 | **2** | 2, 2 | 4 | Timed burst consuming native appropriate resource or applying fatigue. |

#### Moonrunner — 17 points

| Skill | Old draft ranks | New ranks | SP cost per rank | Full skill cost | Intended result |
|---|---:|---:|---|---:|---|
| **Predator's Stride** (`tyr_progression:werewolf_predators_stride`) | 8 | **4** | 2, 2, 2, 2 | 8 | Bonus mobility in valid wolf form. |
| **Pouncing Leap** (`tyr_progression:werewolf_pouncing_leap`) | 5 | **3** | 1, 2, 2 | 5 | Targeted controlled leap with landing safety. |
| **Lunar Chase** (`tyr_progression:werewolf_lunar_chase`) | 4 | **2** | 2, 2 | 4 | Short chase burst under moon conditions with weaker daytime counterpart. |

#### Wild Communion — 16 points

| Skill | Old draft ranks | New ranks | SP cost per rank | Full skill cost | Intended result |
|---|---:|---:|---|---:|---|
| **Wild Attunement** (`tyr_progression:werewolf_wild_attunement`) | 8 | **4** | 2, 2, 2, 2 | 8 | Increase eligible nature magic potency (Botania/Roots personal effects) while werewolf state is valid. |
| **Rootsong** (`tyr_progression:werewolf_rootsong`) | 5 | **3** | 1, 2, 2 | 5 | Further increase player nature spell potency or healing, separate from ordinary combat regeneration. |
| **Grove Howl** (`tyr_progression:werewolf_grove_howl`) | 3 | **2** | 1, 2 | 3 | High-level synergy between wolf pack allies and personally cast nature magic, bounded to eligible owners/effects. |

#### Retained Lycan Skill Concepts (JAWR-owned) — 28 points

| Skill | Old draft ranks | New ranks | SP cost per rank | Full skill cost | Intended result |
|---|---:|---:|---|---:|---|
| **Bloodlust Recovery** (`tyr_progression:werewolf_bloodlust_recovery`) | 5 | **3** | 1, 2, 2 | 5 | Health restoration from actual player-credited hostile kill, with real cooldown. |
| **Bite Techniques** (`tyr_progression:werewolf_bite_techniques`) | 5 | **3** | 1, 2, 2 | 5 | Select either limited bleeding or brief stun enhancement on confirmed wolf bites. |
| **Feral Excavation** (`tyr_progression:werewolf_feral_excavation`) | 8 | **4** | 2, 2, 2, 2 | 8 | Boost permitted claw mining/harvest throughput while in real JAWR wolf form. |
| **Arrow Awareness** (`tyr_progression:werewolf_arrow_awareness`) | 5 | **3** | 1, 2, 2 | 5 | Limited moving-projectile evasion with shared evasion budget. |
| **Tempered Against Silver** (`tyr_progression:werewolf_tempered_against_silver`) | 5 | **3** | 1, 2, 2 | 5 | Partial damage mitigation against source-verified silver damage without erasing weakness. |

#### Retained Tyz 6.4.2 source skills — 5 points

| Skill | Old draft ranks | New ranks | SP cost per rank | Full skill cost | Intended result |
|---|---:|---:|---|---:|---|
| **Rage** (`tyr_progression:werewolf_rage`) | 5 | **3** | 1, 2, 2 | 5 | Increases damage by {damage_buff}% when health is below {health_threshold}% |

### Lich — 100 points / 12 skills

*Identity and native guardrails:* Shared ONE Lich tree for either provider/Archlich; native death priority is unchanged.

#### Soul Economy — 25 points

| Skill | Old draft ranks | New ranks | SP cost per rank | Full skill cost | Intended result |
|---|---:|---:|---|---:|---|
| **Soul Conservation** (`tyr_progression:lich_soul_conservation`) | 12 | **6** | 2, 2, 2, 2, 2, 2 | 12 | Lower eligible personal necromancy spell costs, not global ritual/machine cost. |
| **Soul Reserve** (`tyr_progression:lich_soul_reserve`) | 8 | **4** | 2, 2, 2, 2 | 8 | Bounded native personal soul/Arca resource capacity where extensible. |
| **Soul Treasury** (`tyr_progression:lich_soul_treasury`) | 5 | **3** | 1, 2, 2 | 5 | Small high-level sustain on committed necromantic actions, no passive self-funding loop. |

#### Blood & Nature Opposition — 25 points

| Skill | Old draft ranks | New ranks | SP cost per rank | Full skill cost | Intended result |
|---|---:|---:|---|---:|---|
| **Sanguine Attunement** (`tyr_progression:lich_sanguine_attunement`) | 12 | **6** | 2, 2, 2, 2, 2, 2 | 12 | Improve verified blood-based personal magic (Neo Vitae/EvilCraft where actual hooks apply) while applying a meaningful reduction to this lich's personal Botania/Roots nature spell power. |
| **Crimson Channel** (`tyr_progression:lich_crimson_channel`) | 8 | **4** | 2, 2, 2, 2 | 8 | Strengthen source-verified blood magic further with proportionate nature-magic potency tradeoff; not passive blood machine throughput. |
| **Verdant Severance** (`tyr_progression:lich_verdant_severance`) | 5 | **3** | 1, 2, 2 | 5 | High blood-magic specialization at a stronger self-inflicted Botania/Roots nature power penalty; no world-machine or other-player debuff. |

#### Undead Host — 25 points

| Skill | Old draft ranks | New ranks | SP cost per rank | Full skill cost | Intended result |
|---|---:|---:|---|---:|---|
| **Bone Captain** (`tyr_progression:lich_bone_captain`) | 12 | **6** | 2, 2, 2, 2, 2, 2 | 12 | Modest bonus to personally owned undead servants. |
| **Grave Regiment** (`tyr_progression:lich_grave_regiment`) | 8 | **4** | 2, 2, 2, 2 | 8 | Improved servant coordination/leash and bounded count efficiency. |
| **Death Host** (`tyr_progression:lich_death_host`) | 5 | **3** | 1, 2, 2 | 5 | Late temporary enhancement to current owned minions with hard entity budget. |

#### Soul Preservation — 25 points

| Skill | Old draft ranks | New ranks | SP cost per rank | Full skill cost | Intended result |
|---|---:|---:|---|---:|---|
| **Cold Persistence** (`tyr_progression:lich_cold_persistence`) | 12 | **6** | 2, 2, 2, 2, 2, 2 | 12 | Limited tolerance of wither/undead hazards, preserving explicit weaknesses. |
| **Anchor Attunement** (`tyr_progression:lich_anchor_attunement`) | 8 | **4** | 2, 2, 2, 2 | 8 | Improve diagnostics or controlled anchor maintenance; never bypass native phylactery/Arca cost. |
| **Final Passage** (`tyr_progression:lich_final_passage`) | 5 | **3** | 1, 2, 2 | 5 | Minor post-rescue recovery *after* an authoritative successful resurrection; no extra death-save trigger. |

### Gilded — 100 points / 12 skills

*Identity and native guardrails:* FREE native/Path abilities: Nether golden-armor+gold-food successful initiation, baseline diamond-equivalent gold, +3 virtual Fortune and +3 virtual Looting, Piglin/Brute peace, immediate lava-fishing license, rank18 full fire-tag immunity. Do not charge points for these again.

#### Goldcraft — 44 points

| Skill | Old draft ranks | New ranks | SP cost per rank | Full skill cost | Intended result |
|---|---:|---:|---|---:|---|
| **Fortune Mastery** (`tyr_progression:gilded_fortune_mastery`) | 7 | **7** | 1, 1, 1, 1, 1, 1, 1 | 7 | Increase legal gold/Netherite virtual Fortune +3→+10, one per purchase; Silk prevails. |
| **Looting Mastery** (`tyr_progression:gilded_looting_mastery`) | 7 | **7** | 1, 1, 1, 1, 1, 1, 1 | 7 | Increase golden sword/axe direct-melee virtual Looting +3→+10, independent of Fortune. |
| **Lasting Gilt** (`tyr_progression:gilded_lasting_gilt`) | 10 | **5** | 2, 2, 2, 2, 2 | 10 | Raise virtual Unbreaking on equipped gold/Netherite gear only. |
| **Golden Renewal** (`tyr_progression:gilded_golden_renewal`) | 10 | **5** | 2, 2, 2, 2, 2 | 10 | Increase actual equipped gold/Netherite repair at equal rates. |
| **Gilded Grip Mastery** (`tyr_progression:gilded_gilded_grip_mastery`) | 10 | **5** | 2, 2, 2, 2, 2 | 10 | Supplement free diamond-equivalence with bounded legitimate equipped-gold handling; do not repurchase free parity. |

#### Nether Society — 45 points

| Skill | Old draft ranks | New ranks | SP cost per rank | Full skill cost | Intended result |
|---|---:|---:|---|---:|---|
| **Piglin Diplomacy** (`tyr_progression:gilded_piglin_diplomacy`) | 5 | **3** | 1, 2, 2 | 5 | Improve barter/trade interactions while preserving baseline Piglin/Brute friendliness. |
| **Lava Angler** (`tyr_progression:gilded_lava_angler`) | 10 | **5** | 2, 2, 2, 2, 2 | 10 | Improve lawful lava catch utility; baseline fishing stays free. |
| **Gold Nourishment** (`tyr_progression:gilded_gold_nourishment`) | 10 | **5** | 2, 2, 2, 2, 2 | 10 | Improve recovery from gold-qualified food without undoing locked restrictions or duplicating potion effects. |
| **Nether Affinity** (`tyr_progression:gilded_nether_affinity`) | 10 | **5** | 2, 2, 2, 2, 2 | 10 | Bounded environmental movement and defense in Nether; separate from rank18 fire immunity. |
| **Metallurgist** (`tyr_progression:gilded_metallurgist`) | 10 | **5** | 2, 2, 2, 2, 2 | 10 | Improve verified golden tool/weapon performance without altering global item tiers. |

#### Sovereignty — 11 points

| Skill | Old draft ranks | New ranks | SP cost per rank | Full skill cost | Intended result |
|---|---:|---:|---|---:|---|
| **Glittering Luck** (`tyr_progression:gilded_glittering_luck`) | 6 | **3** | 2, 2, 2 | 6 | True player Luck improvements, not virtual Fortune or Looting. |
| **Gilded Sovereign** (`tyr_progression:gilded_gilded_sovereign`) | 1 | **1** | 5 | 5 | Proposed post-Nether late-game golden mastery capstone; exact effect requires owner review. |

### Bare Zerker — 100 points / 13 skills

*Identity and native guardrails:* Free locked innate: Fury capacity100; +Fury from actual post-mitigation damage DEALT only; 1 Fury/sec decay; each actual received hit adds 1s Regeneration I+Resistance I; >50 pre-hit Fury would-be death triggers full vanilla Totem and drains all Fury. Do not sell innate abilities or alter their formulas. Chest chafing, allowed clothes, Red Cap and woad are separate form mechanics.

#### Fury Combat — 40 points

| Skill | Old draft ranks | New ranks | SP cost per rank | Full skill cost | Intended result |
|---|---:|---:|---|---:|---|
| **Reckless Sword** (`tyr_progression:bare_zerker_reckless_sword`) | 10 | **5** | 2, 2, 2, 2, 2 | 10 | Improve shieldless sword damage under approved chest-empty rules, Fury still only from actual dealt HP. |
| **Cleaving Axe** (`tyr_progression:bare_zerker_cleaving_axe`) | 10 | **5** | 2, 2, 2, 2, 2 | 10 | Improve shieldless axe damage, no new source of free Fury. |
| **Battle Rhythm** (`tyr_progression:bare_zerker_battle_rhythm`) | 10 | **5** | 2, 2, 2, 2, 2 | 10 | Bounded consecutive real-hit damage bonuses, no proc from mere incoming hits. |
| **Bloodied Drive** (`tyr_progression:bare_zerker_bloodied_drive`) | 10 | **5** | 2, 2, 2, 2, 2 | 10 | Brief valid kill-triggered combat momentum, no Fury injections. |

#### Endurance Under Fire — 30 points

| Skill | Old draft ranks | New ranks | SP cost per rank | Full skill cost | Intended result |
|---|---:|---:|---|---:|---|
| **Skin Hardened** (`tyr_progression:bare_zerker_skin_hardened`) | 10 | **5** | 2, 2, 2, 2, 2 | 10 | Actual innate mitigation while chest-empty, no conventional armor buffs. |
| **Regeneration Amplifier** (`tyr_progression:bare_zerker_regen_amplifier`) | 10 | **5** | 2, 2, 2, 2, 2 | 10 | Improve amplifier of owner-approved incoming-hit regeneration, preserving +1s per hit duration. |
| **Resistance Amplifier** (`tyr_progression:bare_zerker_resistance_amplifier`) | 10 | **5** | 2, 2, 2, 2, 2 | 10 | Improve amplifier of owner-approved incoming-hit Resistance, preserving +1s per hit duration. |

#### Blood and Woad — 20 points

| Skill | Old draft ranks | New ranks | SP cost per rank | Full skill cost | Intended result |
|---|---:|---:|---|---:|---|
| **Red Cap Fortitude** (`tyr_progression:bare_zerker_redcap_fortitude`) | 5 | **3** | 1, 2, 2 | 5 | Improve blood-dipped Red Cap temporary powers; duration/cost owner review. |
| **Woad Attunement** (`tyr_progression:bare_zerker_woad_attunement`) | 5 | **3** | 1, 2, 2 | 5 | Enhance source-verified Neo Vitae Sentient Armor-equivalent woad benefits without equipping armor. |
| **Neo Vitae Bloodmaster** (`tyr_progression:bare_zerker_neo_bloodmaster`) | 5 | **3** | 1, 2, 2 | 5 | Improve verified Neo Vitae blood magic interactions, with genuine costs. |
| **EvilCraft Hematurgy** (`tyr_progression:bare_zerker_evilcraft_bloodcraft`) | 5 | **3** | 1, 2, 2 | 5 | Improve verified EvilCraft blood transactions/ritual effects with no duplicate blood credit. |

#### Berserk Mastery — 10 points

| Skill | Old draft ranks | New ranks | SP cost per rank | Full skill cost | Intended result |
|---|---:|---:|---|---:|---|
| **Antimagic Grit** (`tyr_progression:bare_zerker_antimagic_grit`) | 5 | **3** | 1, 2, 2 | 5 | Bounded mitigation for specified applied incoming magic damage types, not mod-caster origin. |
| **Last Stand Mastery** (`tyr_progression:bare_zerker_last_stand_mastery`) | 5 | **3** | 1, 2, 2 | 5 | Improve recovery after real Too Angry to Die event, without altering >50 rule, full totem effects, or complete Fury drain. |

## Four optional Path shells still require design decisions

The following are **identity/branch skeletons only**. They do not have approved rank budgets, source mechanics, initiation or effects. They should NOT be installed as dummy purchasable abilities.

| Path | Provisional lanes | Locked directions / design holds |
|---|---|---|
| **Sapient Slime** | Body Mass / Biomass; Reactive Allied Slimes; Absorption and Ooze Kinship | Slime-themed proposed Path; mutagen-altered Potion of Oozing initiation candidate; damage-spawn and absorption require #446 state/action, size collision and caps. |
| **Sexy Demon (Incubus/Succubus)** | Infernal Body; Ranged Charm (one mob, 600 ticks); Right-click Permanent Taming; Companion Command | One Path, not sex-specific trees; generic #451 Charm and Taming are distinct. Cave Dragon pairing approved. Lilith ring initiation remains candidate. |
| **Dryad** | Photosynthesis and Saturation; Greening Footsteps; Plant/Fae Diplomacy; Nature Magic | Plant-primary, only Magical food plus drinks/potions; sunlight OR water OR plantlike ground nourishing, enhanced saturation capacity, cautious greening, fire vulnerability. Forest Dragon pairing approved. NO Biomass economy or mandatory Rooting. |
| **Spawn** | Shared natural armor and regeneration trunk; Cephalid; Deep One; Shoggoth; Mi-Go | One Aberration-primary Path with mixable anatomical physical lineages; constrained major/minor mutation choices. Sea Dragon pairing approved. Five tentative slots (2 major/3 minor) NOT locked. |

Newbie is a no-MXP baseline and Normie is a graduate baseline with universal Core access. Neither has its own additional 100-point purchase tree.

## Required approval, source adapter and game QA

- Owner must approve the **fewer ranks / higher price and stronger per-rank effect** model, notably changing Hunter's live 238 spent points and existing Hunter choices. The exact total rank strength is only a source target until actual `per_rank`/attribute math is updated and tested.
- Hunter needs explicit **server-enforced specialty purchase/activation gating**, not merely visual isolation. Existing zero-cost Favored Prey choice must remain authoritative; specialty switches/respec must not let the player purchase and keep another 30-point branch simultaneously. Ensure refunds do not launder XP or permanently duplicate purchases.
- Gilded's seven Fortune and seven Looting ranks and their native free +3 at initiation are held; verify both virtual connectors, Silk precedence and actual Golden/Netherite equipment contexts.
- Sea Dragon 10×5 Luck should be measured against the real vanilla Luck interactions, legal Curios, affixes and 85%-of-geared Monks; never assume literal 50× drop counts.
- Live source handlers and Path JSON for Vampire, Werewolf, Lich, three Dragons, Bare Zerker and Gilded are not implemented by this proposal. Validate native provider availability, blood/soul/growth state, creature tags and stage/capstone conditions before creating active skill JSON.
- Dragon diets are still held while the food engine changes (#444); don't reopen it to ship these skill trees. Any engine work belongs in separate `feature/progression-*` PRs under one shared runtime.
- All eligible Path skills should be passive, contextual or triggered by ordinary play instead of requiring new independent hotkeys. All optional newer Path shells remain drafts until their initiated mechanics are owner-approved.
- This design branch has **no Minecraft runtime changes or game-build authorization**; no statement of gameplay acceptance follows from a source/CI check on documentation.

**References:** [#495 draft PR](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/495), [#475 MXP economy](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/475), [#271 Hunter acceptance](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/271), [#455 baseline Paths](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/455), [#454 Bare Zerker](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/454), [#415 proposed Paths](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/415).
