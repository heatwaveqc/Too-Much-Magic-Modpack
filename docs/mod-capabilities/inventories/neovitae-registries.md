# Neo Vitae — directly declared registry inventory (pilot supporting index)

**Target evidence:** [upstream commit `2744123bb6e4f72c602178b23384be41956eef23`](https://github.com/breakinblocks/NeoVitae/tree/2744123bb6e4f72c602178b23384be41956eef23), aligned with the pinned pack's 1.21.1-1.1.53 metadata; **binary equivalence not established**. Source-inspected declarations from [NVItems.java](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/main/java/com/breakinblocks/neovitae/common/item/NVItems.java) and [NVBlocks.java](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/main/java/com/breakinblocks/neovitae/common/block/NVBlocks.java). This is an **index of statically named declarations**, not proof every ID exists in the assembled TMM client, not an exhaustive registry dump of dynamic entries/material registries, and **not a substitute for the detailed capability dossier**.

- Counts: 211 named item declarations and 90 named block declarations found on single-line `public static final` registration declarations. Some names may overlap between item and block registries, or have block-items. Complex multi-line declarations and dynamically generated material entries are not enumerated.
- The `neovitae:` namespace is sourced from deferred registries created with `NeoVitae.MODID`; the `source line` is a navigation aid for the exact frozen source.
- Identity alone is **not** evidence of use, crafting path, tier, result or pack acceptance. Check the [pilot dossier](../mods/neovitae.md) for behavior and quest meaning.

## Items (211 single-line registrations)

| Java field | Declared ID | Source line |
| --- | --- | ---: |
| `SENTIENT_HELMET` | `neovitae:sentient_helmet` | 54 |
| `SENTIENT_PLATE` | `neovitae:sentient_plate` | 55 |
| `SENTIENT_LEGGINGS` | `neovitae:sentient_leggings` | 56 |
| `SENTIENT_BOOTS` | `neovitae:sentient_boots` | 57 |
| `UPGRADE_TOME` | `neovitae:upgrade_tome` | 58 |
| `EXPERIENCE_TOME` | `neovitae:experience_tome` | 60 |
| `TRAINING_BRACELET` | `neovitae:training_bracelet` | 62 |
| `ORB_WEAK` | `neovitae:blood_orb_weak` | 64 |
| `ORB_APPRENTICE` | `neovitae:blood_orb_apprentice` | 65 |
| `ORB_MAGICIAN` | `neovitae:blood_orb_magician` | 66 |
| `ORB_MASTER` | `neovitae:blood_orb_master` | 67 |
| `ORB_ARCHMAGE` | `neovitae:blood_orb_archmage` | 68 |
| `ORB_TRANSCENDENT` | `neovitae:blood_orb_transcendent` | 69 |
| `RAW_SPIRITUS` | `neovitae:raw_spiritus` | 79 |
| `SPIRITUS_GEM_PETTY` | `neovitae:spiritus_gem_petty` | 81 |
| `SPIRITUS_GEM_LESSER` | `neovitae:spiritus_gem_lesser` | 82 |
| `SPIRITUS_GEM_COMMON` | `neovitae:spiritus_gem_common` | 83 |
| `SPIRITUS_GEM_GREATER` | `neovitae:spiritus_gem_greater` | 84 |
| `SPIRITUS_GEM_GRAND` | `neovitae:spiritus_gem_grand` | 85 |
| `MONSTER_SOUL_RAW` | `neovitae:base_spiritus_soul_raw` | 87 |
| `MONSTER_SOUL_RUINA` | `neovitae:base_spiritus_soul_ruina` | 88 |
| `MONSTER_SOUL_NIHILUM` | `neovitae:base_spiritus_soul_nihilum` | 89 |
| `MONSTER_SOUL_VINDICTA` | `neovitae:base_spiritus_soul_vindicta` | 90 |
| `MONSTER_SOUL_INVICTUS` | `neovitae:base_spiritus_soul_invictus` | 91 |
| `TABULA_RASA` | `neovitae:tabula_rasa` | 93 |
| `TABULA_ROBUR` | `neovitae:tabula_robur` | 94 |
| `TABULA_ANIMATA` | `neovitae:tabula_animata` | 95 |
| `TABULA_SPIRITUS` | `neovitae:tabula_spiritus` | 96 |
| `TABULA_AETHEREA` | `neovitae:tabula_aetherea` | 97 |
| `PRISMATIC_SPIRITUS_GEM` | `neovitae:prismatic_spiritus_gem` | 98 |
| `DEUS_SHARD` | `neovitae:deus_shard` | 99 |
| `SIGIL_DIVINATION` | `neovitae:sigil_divination` | 101 |
| `SIGIL_SEER` | `neovitae:sigil_seer` | 102 |
| `SIGIL_WATER` | `neovitae:sigil_water` | 103 |
| `SIGIL_LAVA` | `neovitae:sigil_lava` | 104 |
| `SIGIL_VOID` | `neovitae:sigil_void` | 105 |
| `SIGIL_GREEN_GROVE` | `neovitae:sigil_green_grove` | 106 |
| `SIGIL_AIR` | `neovitae:sigil_air` | 107 |
| `SIGIL_BLOOD_LIGHT` | `neovitae:sigil_blood_light` | 108 |
| `SIGIL_FAST_MINER` | `neovitae:sigil_fast_miner` | 112 |
| `SIGIL_MAGNETISM` | `neovitae:sigil_magnetism` | 113 |
| `SIGIL_FROST` | `neovitae:sigil_frost` | 114 |
| `SIGIL_SUPPRESSION` | `neovitae:sigil_suppression` | 115 |
| `SIGIL_HOLDING` | `neovitae:sigil_holding` | 116 |
| `SIGIL_TELEPOSITION` | `neovitae:sigil_teleposition` | 117 |
| `SIGIL_PHANTOM_BRIDGE` | `neovitae:sigil_phantom_bridge` | 118 |
| `SIGIL_NECROMANCY` | `neovitae:sigil_necromancy` | 119 |
| `SIGIL_BOUND_TREASURES` | `neovitae:sigil_bound_treasures` | 120 |
| `ARCANE_SCRIBE_TOOL` | `neovitae:arcane_scribe_tool` | 122 |
| `TAU_OIL` | `neovitae:tau_oil` | 124 |
| `REAGENT_WATER` | `neovitae:reagent_water` | 126 |
| `REAGENT_LAVA` | `neovitae:reagent_lava` | 127 |
| `REAGENT_VOID` | `neovitae:reagent_void` | 128 |
| `REAGENT_GROWTH` | `neovitae:reagent_growth` | 129 |
| `REAGENT_FAST_MINER` | `neovitae:reagent_fast_miner` | 130 |
| `REAGENT_MAGNETISM` | `neovitae:reagent_magnetism` | 131 |
| `REAGENT_AIR` | `neovitae:reagent_air` | 132 |
| `REAGENT_BLOOD_LIGHT` | `neovitae:reagent_blood_light` | 133 |
| `REAGENT_SIGHT` | `neovitae:reagent_sight` | 134 |
| `REAGENT_BINDING` | `neovitae:reagent_binding` | 135 |
| `REAGENT_HOLDING` | `neovitae:reagent_holding` | 136 |
| `REAGENT_SUPPRESSION` | `neovitae:reagent_suppression` | 137 |
| `REAGENT_TELEPOSITION` | `neovitae:reagent_teleposition` | 138 |
| `REAGENT_FROST` | `neovitae:reagent_frost` | 139 |
| `REAGENT_PHANTOM_BRIDGE` | `neovitae:reagent_phantom_bridge` | 140 |
| `WEAK_BLOOD_SHARD` | `neovitae:weak_blood_shard` | 142 |
| `LAVA_CRYSTAL` | `neovitae:lava_crystal` | 144 |
| `TELEPOSER_FOCUS` | `neovitae:teleposer_focus` | 146 |
| `TELEPOSER_FOCUS_ENHANCED` | `neovitae:enhanced_teleposer_focus` | 147 |
| `TELEPOSER_FOCUS_REINFORCED` | `neovitae:reinforced_teleposer_focus` | 148 |
| `DEMONITE_RAW` | `neovitae:raw_demonite` | 156 |
| `CORRUPTED_DUST` | `neovitae:corrupted_dust` | 171 |
| `CORRUPTED_DUST_TINY` | `neovitae:corrupted_tiny_dust` | 172 |
| `BLOOD_PEARL` | `neovitae:blood_pearl` | 173 |
| `BASIC_CUTTING_FLUID` | `neovitae:basic_cutting_fluid` | 175 |
| `INTERMEDIATE_CUTTING_FLUID` | `neovitae:intermediate_cutting_fluid` | 176 |
| `ADVANCED_CUTTING_FLUID` | `neovitae:advanced_cutting_fluid` | 177 |
| `RESONATOR` | `neovitae:resonator` | 178 |
| `GUIDE_BOOK` | `neovitae:guide_book` | 179 |
| `SANGUINE_REVERTER` | `neovitae:sanguine_reverter` | 180 |
| `PRIMITIVE_FURNACE_CELL` | `neovitae:furnacecell_primitive` | 181 |
| `PRIMITIVE_HYDRATION_CELL` | `neovitae:primitive_hydration_cell` | 182 |
| `PRIMITIVE_CRYSTALLINE_RESONATOR` | `neovitae:primitive_crystalline_resonator` | 183 |
| `HELLFORGED_RESONATOR` | `neovitae:hellforged_resonator` | 184 |
| `DEUS_CUTTING_FLUID` | `neovitae:deus_cutting_fluid` | 185 |
| `DEUS_RESONATOR` | `neovitae:deus_resonator` | 186 |
| `DEUS_HYDRATION_CELL` | `neovitae:deus_hydration_cell` | 187 |
| `DEUS_REVERTER` | `neovitae:deus_reverter` | 188 |
| `ACTIVATION_CRYSTAL_WEAK` | `neovitae:activation_crystal_weak` | 190 |
| `ACTIVATION_CRYSTAL_AWAKENED` | `neovitae:activation_crystal_awakened` | 191 |
| `ACTIVATION_CRYSTAL_DIVINUS` | `neovitae:activation_crystal_divinus` | 192 |
| `ACTIVATION_CRYSTAL_CREATIVE` | `neovitae:activation_crystal_creative` | 193 |
| `INSCRIPTION_TOOL_AIR` | `neovitae:air_scribe_tool` | 195 |
| `INSCRIPTION_TOOL_FIRE` | `neovitae:fire_scribe_tool` | 196 |
| `INSCRIPTION_TOOL_WATER` | `neovitae:water_scribe_tool` | 197 |
| `INSCRIPTION_TOOL_EARTH` | `neovitae:earth_scribe_tool` | 198 |
| `INSCRIPTION_TOOL_TENEBRAE` | `neovitae:tenebrae_scribe_tool` | 199 |
| `INSCRIPTION_TOOL_DEUS` | `neovitae:deus_scribe_tool` | 200 |
| `RITUAL_DIVINER` | `neovitae:ritual_diviner` | 202 |
| `RITUAL_DIVINER_TENEBRAE` | `neovitae:ritual_diviner_tenebrae` | 203 |
| `RITUAL_DIVINER_DEUS` | `neovitae:ritual_diviner_deus` | 204 |
| `RITUAL_READER` | `neovitae:ritual_reader` | 205 |
| `RITUAL_LEDGER` | `neovitae:ritual_ledger` | 206 |
| `RITUAL_DESIGNER` | `neovitae:ritual_designer` | 207 |
| `SENTIENT_SWORD` | `neovitae:sentient_sword` | 209 |
| `SENTIENT_AXE` | `neovitae:sentient_axe` | 210 |
| `SENTIENT_PICKAXE` | `neovitae:sentient_pickaxe` | 211 |
| `SENTIENT_SHOVEL` | `neovitae:sentient_shovel` | 212 |
| `SENTIENT_SCYTHE` | `neovitae:sentient_scythe` | 213 |
| `LEX_VITAE` | `neovitae:lex_vitae` | 214 |
| `RAW_SPIRITUS_CRYSTAL_ITEM` | `neovitae:raw_crystal_shard` | 216 |
| `SPIRITUS_RUINA_CRYSTAL_ITEM` | `neovitae:spiritus_ruina_shard` | 217 |
| `SPIRITUS_NIHILUM_CRYSTAL_ITEM` | `neovitae:spiritus_nihilum_shard` | 218 |
| `SPIRITUS_VINDICTA_CRYSTAL_ITEM` | `neovitae:spiritus_vindicta_shard` | 219 |
| `SPIRITUS_INVICTUS_CRYSTAL_ITEM` | `neovitae:spiritus_invictus_shard` | 220 |
| `SPIRITUS_GAUGE` | `neovitae:spiritus_gauge` | 221 |
| `RAW_SPIRITUS_CATALYST` | `neovitae:raw_spiritus_catalyst` | 223 |
| `SPIRITUS_RUINA_CATALYST` | `neovitae:spiritus_ruina_catalyst` | 224 |
| `SPIRITUS_NIHILUM_CATALYST` | `neovitae:spiritus_nihilum_catalyst` | 225 |
| `SPIRITUS_VINDICTA_CATALYST` | `neovitae:spiritus_vindicta_catalyst` | 226 |
| `SPIRITUS_INVICTUS_CATALYST` | `neovitae:spiritus_invictus_catalyst` | 227 |
| `NODE_ROUTER` | `neovitae:node_router` | 229 |
| `MASTER_NODE_UPGRADE` | `neovitae:master_core` | 230 |
| `MASTER_NODE_UPGRADE_SPEED` | `neovitae:master_core_speed` | 231 |
| `THROWING_DAGGER` | `neovitae:throwing_dagger` | 233 |
| `THROWING_DAGGER_AMETHYST` | `neovitae:amethyst_throwing_dagger` | 234 |
| `THROWING_DAGGER_SYRINGE` | `neovitae:throwing_dagger_syringe` | 235 |
| `THROWING_DAGGER_TIPPED` | `neovitae:tipped_throwing_dagger` | 236 |
| `SIMPLE_KEY` | `neovitae:simple_key` | 238 |
| `MINE_KEY` | `neovitae:mine_key` | 240 |
| `MINE_ENTRANCE_KEY` | `neovitae:mine_entrance_key` | 242 |
| `STANDARD_KEY` | `neovitae:standard_key` | 244 |
| `BOSS_KEY` | `neovitae:boss_key` | 246 |
| `DUNGEON_TESTER` | `neovitae:dungeon_tester` | 249 |
| `ANIMATED_SPIRITUS` | `neovitae:animated_spiritus` | 252 |
| `GORE_CLOTTED_FANG` | `neovitae:gore_clotted_fang` | 255 |
| `BLIGHT_MARROW` | `neovitae:blight_marrow` | 256 |
| `VENOMGLAND_SAC` | `neovitae:venomgland_sac` | 257 |
| `HOLLOW_GUT` | `neovitae:hollow_gut` | 258 |
| `ECTOPLASMIC_RESIDUE` | `neovitae:ectoplasmic_residue` | 259 |
| `ANIMUS_MOTE` | `neovitae:animus_mote` | 260 |
| `REVENANT_PLATE` | `neovitae:revenant_plate` | 261 |
| `REVENANT_PLATING` | `neovitae:revenant_plating` | 262 |
| `FROZEN_MARROW_SHARD` | `neovitae:frozen_marrow_shard` | 263 |
| `CINDER_HEART_FRAGMENT` | `neovitae:cinder_heart_fragment` | 264 |
| `PERMAFROST_CORE` | `neovitae:permafrost_core` | 265 |
| `DEMONITE_TRIM_INGOT` | `neovitae:demonite_trim_ingot` | 266 |
| `BLIGHT_WHETSTONE` | `neovitae:blight_whetstone` | 267 |
| `TAINTED_FLESH` | `neovitae:tainted_flesh` | 271 |
| `VITAE_MORSEL` | `neovitae:vitae_morsel` | 275 |
| `BOTTLED_SPITE` | `neovitae:bottled_spite` | 280 |
| `SIGIL_DAMNED` | `neovitae:sigil_damned` | 287 |
| `SULFUR` | `neovitae:sulfur` | 289 |
| `SALTPETER` | `neovitae:saltpeter` | 290 |
| `PLANT_OIL` | `neovitae:plant_oil` | 291 |
| `HELLFORGED_INGOT` | `neovitae:ingot_hellforged` | 292 |
| `TABULA_VIAL` | `neovitae:tabula_vial` | 294 |
| `ALCHEMY_FLASK` | `neovitae:alchemy_flask` | 295 |
| `ALCHEMY_FLASK_THROWABLE` | `neovitae:alchemy_flask_throwable` | 296 |
| `ALCHEMY_FLASK_LINGERING` | `neovitae:alchemy_flask_lingering` | 297 |
| `FRAME_PARTS` | `neovitae:component_frame_parts` | 382 |
| `BLOOD_SWEAT_AND_TEARS` | `neovitae:blood_sweat_and_tears` | 385 |
| `SIMPLE_CATALYST` | `neovitae:simple_catalyst` | 395 |
| `STRENGTHENED_CATALYST` | `neovitae:strengthened_catalyst` | 396 |
| `CYCLING_CATALYST` | `neovitae:cycling_catalyst` | 397 |
| `COMBINATIONAL_CATALYST` | `neovitae:combinational_catalyst` | 398 |
| `MUNDANE_LENGTHENING_CATALYST` | `neovitae:mundane_lengthening_catalyst` | 399 |
| `MUNDANE_POWER_CATALYST` | `neovitae:mundane_power_catalyst` | 400 |
| `AVERAGE_LENGTHENING_CATALYST` | `neovitae:average_lengthening_catalyst` | 401 |
| `AVERAGE_POWER_CATALYST` | `neovitae:average_power_catalyst` | 402 |
| `WEAK_FILLING_AGENT` | `neovitae:weak_filling_agent` | 404 |
| `STANDARD_FILLING_AGENT` | `neovitae:standard_filling_agent` | 405 |
| `HELLFORGED_PARTS` | `neovitae:hellforged_parts` | 407 |
| `TABULA_AMPOULE` | `neovitae:tabula_ampoule` | 409 |
| `DAEMONIUM_IGNIS_SPAWN_EGG` | `neovitae:daemonium_ignis_spawn_egg` | 411 |
| `DAEMONIUM_CRUORIS_SPAWN_EGG` | `neovitae:daemonium_cruoris_spawn_egg` | 416 |
| `DAEMONIUM_CORRODIS_SPAWN_EGG` | `neovitae:daemonium_corrodis_spawn_egg` | 421 |
| `DAEMONIUM_GLACIARIS_SPAWN_EGG` | `neovitae:daemonium_glaciaris_spawn_egg` | 426 |
| `DAEMONIUM_PESTIS_SPAWN_EGG` | `neovitae:daemonium_pestis_spawn_egg` | 431 |
| `DAEMONIUM_VORAXIS_SPAWN_EGG` | `neovitae:daemonium_voraxis_spawn_egg` | 436 |
| `DAEMONIUM_DOLORIS_SPAWN_EGG` | `neovitae:daemonium_doloris_spawn_egg` | 441 |
| `DAEMONIUM_FERVIDIS_SPAWN_EGG` | `neovitae:daemonium_fervidis_spawn_egg` | 446 |
| `DAEMONIUM_ANIMARIS_SPAWN_EGG` | `neovitae:daemonium_animaris_spawn_egg` | 451 |
| `DAEMONIUM_RANCORIS_SPAWN_EGG` | `neovitae:daemonium_rancoris_spawn_egg` | 456 |
| `SLIME_VITAE_SPAWN_EGG` | `neovitae:slime_vitae_spawn_egg` | 461 |
| `ARRAY_BOUNCE` | `neovitae:array_bounce` | 468 |
| `ARRAY_SPIKE` | `neovitae:array_spike` | 469 |
| `ARRAY_UPDRAFT` | `neovitae:array_updraft` | 470 |
| `ARRAY_MOVEMENT` | `neovitae:array_movement` | 471 |
| `ARRAY_DAY` | `neovitae:array_day` | 472 |
| `ARRAY_NIGHT` | `neovitae:array_night` | 473 |
| `ARRAY_ELEVATOR` | `neovitae:array_elevator` | 474 |
| `ARRAY_REPULSION` | `neovitae:array_repulsion` | 475 |
| `ARRAY_COLLECTION` | `neovitae:array_collection` | 476 |
| `ARRAY_LIGHT` | `neovitae:array_light` | 477 |
| `ARRAY_FURNACE` | `neovitae:array_furnace` | 478 |
| `ARRAY_RAIN` | `neovitae:array_rain` | 479 |
| `ARRAY_GROWTH` | `neovitae:array_growth` | 480 |
| `ARRAY_FREEZE` | `neovitae:array_freeze` | 481 |
| `ARRAY_SIGNAL` | `neovitae:array_signal` | 482 |
| `ARRAY_TRIGGER` | `neovitae:array_trigger` | 483 |
| `ARRAY_SPIRIT_SIPHON` | `neovitae:array_spirit_siphon` | 484 |
| `ARRAY_DEFLECTION` | `neovitae:array_deflection` | 485 |
| `ARRAY_ENDLESS_FOUNTAIN` | `neovitae:array_endless_fountain` | 486 |
| `ARRAY_UNDERTOW` | `neovitae:array_undertow` | 487 |
| `ARRAY_LOYAL_FRIENDS` | `neovitae:array_loyal_friends` | 488 |
| `ARRAY_VORTEX` | `neovitae:array_vortex` | 489 |
| `ARRAY_IMPRISONMENT` | `neovitae:array_imprisonment` | 490 |
| `ARRAY_LIQUIFIED_EXPERIENCE` | `neovitae:array_liquified_experience` | 491 |
| `ARRAY_MINER` | `neovitae:array_miner` | 492 |
| `ARRAY_ENDER_SEVER` | `neovitae:array_ender_sever` | 493 |

## Blocks (90 single-line registrations)

| Java field | Declared ID | Source line |
| --- | --- | ---: |
| `ARA_VITAE` | `neovitae:ara_vitae` | 42 |
| `BLOOD_TANK` | `neovitae:blood_tank` | 43 |
| `BLOOD_BATTERY` | `neovitae:blood_battery` | 44 |
| `HELLFIRE_FORGE` | `neovitae:hellfire_forge` | 45 |
| `ATHANOR_BLOCK` | `neovitae:athanor` | 46 |
| `TELEPOSER` | `neovitae:teleposer` | 47 |
| `SPIRIT_CACHE` | `neovitae:spirit_cache` | 48 |
| `SPIRIT_ACCUMULATOR` | `neovitae:spirit_accumulator` | 49 |
| `VITAE_LINK` | `neovitae:vitae_link` | 50 |
| `ORB_FILLING_LINK` | `neovitae:orb_filling_link` | 51 |
| `SANGUINE_CRAFTER` | `neovitae:sanguine_crafter` | 52 |
| `SANGUINE_CASING` | `neovitae:sanguine_casing` | 53 |
| `HELLFORGED_SANGUINE_CASING` | `neovitae:hellforged_sanguine_casing` | 54 |
| `SWIFT_SANGUINE_CASING` | `neovitae:swift_sanguine_casing` | 55 |
| `RUNE_BLANK` | `neovitae:rune_blank` | 72 |
| `RUNE_SACRIFICE` | `neovitae:rune_sacrifice` | 74 |
| `RUNE_SELF_SACRIFICE` | `neovitae:rune_sacrifice_self` | 75 |
| `RUNE_CAPACITY` | `neovitae:rune_capacity` | 76 |
| `RUNE_CAPACITY_AUGMENTED` | `neovitae:rune_capacity_augmented` | 77 |
| `RUNE_CHARGING` | `neovitae:rune_charging` | 78 |
| `RUNE_SPEED` | `neovitae:rune_speed` | 79 |
| `RUNE_ACCELERATION` | `neovitae:rune_acceleration` | 80 |
| `RUNE_DISLOCATION` | `neovitae:rune_dislocation` | 81 |
| `RUNE_ORB` | `neovitae:rune_orb` | 82 |
| `RUNE_EFFICIENCY` | `neovitae:rune_efficiency` | 83 |
| `RUNE_2_SACRIFICE` | `neovitae:rune_2_sacrifice` | 85 |
| `RUNE_2_SELF_SACRIFICE` | `neovitae:rune_2_sacrifice_self` | 86 |
| `RUNE_2_CAPACITY` | `neovitae:rune_2_capacity` | 87 |
| `RUNE_2_CAPACITY_AUGMENTED` | `neovitae:rune_2_capacity_augmented` | 88 |
| `RUNE_2_CHARGING` | `neovitae:rune_2_charging` | 89 |
| `RUNE_2_SPEED` | `neovitae:rune_2_speed` | 90 |
| `RUNE_2_ACCELERATION` | `neovitae:rune_2_acceleration` | 91 |
| `RUNE_2_DISLOCATION` | `neovitae:rune_2_dislocation` | 92 |
| `RUNE_2_ORB` | `neovitae:rune_2_orb` | 93 |
| `RUNE_2_EFFICIENCY` | `neovitae:rune_2_efficiency` | 94 |
| `BLOODSTONE` | `neovitae:bloodstone` | 97 |
| `BLOODSTONE_BRICK` | `neovitae:bloodstone_brick` | 98 |
| `HELLFORGED_BLOCK` | `neovitae:hellforged_block` | 101 |
| `SANGUINE_DEUS_BLOCK` | `neovitae:sanguine_deus_block` | 102 |
| `RAW_DEMONITE_BLOCK` | `neovitae:raw_demonite_block` | 103 |
| `CRYSTAL_CLUSTER` | `neovitae:crystal_cluster` | 105 |
| `CRYSTAL_CLUSTER_BRICK` | `neovitae:crystal_cluster_brick` | 106 |
| `ALCHEMY_ARRAY` | `neovitae:alchemy_array` | 108 |
| `BLOOD_LIGHT` | `neovitae:blood_light` | 110 |
| `SPECTRAL_BLOCK` | `neovitae:spectral_block` | 119 |
| `PHANTOM_BRIDGE_BLOCK` | `neovitae:phantom_bridge` | 121 |
| `TABULA_VITAE` | `neovitae:tabula_vitae` | 123 |
| `INCENSE_ALTAR` | `neovitae:incense_altar` | 125 |
| `STRONG_TAU` | `neovitae:strong_tau` | 128 |
| `WEAK_TAU` | `neovitae:weak_tau` | 129 |
| `BLANK_RITUAL_STONE` | `neovitae:ritual_stone` | 135 |
| `AIR_RITUAL_STONE` | `neovitae:air_ritual_stone` | 136 |
| `WATER_RITUAL_STONE` | `neovitae:water_ritual_stone` | 137 |
| `FIRE_RITUAL_STONE` | `neovitae:fire_ritual_stone` | 138 |
| `EARTH_RITUAL_STONE` | `neovitae:earth_ritual_stone` | 139 |
| `TENEBRAE_RITUAL_STONE` | `neovitae:tenebrae_ritual_stone` | 140 |
| `DEUS_RITUAL_STONE` | `neovitae:deus_ritual_stone` | 141 |
| `MASTER_RITUAL_STONE` | `neovitae:master_ritual_stone` | 142 |
| `INVERTED_MASTER_RITUAL_STONE` | `neovitae:inverted_master_ritual_stone` | 143 |
| `RITUAL_ANCHOR` | `neovitae:ritual_anchor` | 144 |
| `IMPERFECT_RITUAL_STONE` | `neovitae:imperfect_ritual_stone` | 145 |
| `VAS_MALEFICUM` | `neovitae:vas_maleficum` | 147 |
| `CRYSTALLARIUM_MALEFICUM` | `neovitae:crystallarium_maleficum` | 148 |
| `SPIRA_INFERNALIS` | `neovitae:spira_infernalis` | 149 |
| `RAW_SPIRITUS_CRYSTAL` | `neovitae:raw_spiritus_crystal` | 154 |
| `SPIRITUS_RUINA_CRYSTAL` | `neovitae:spiritus_ruina_crystal` | 155 |
| `SPIRITUS_NIHILUM_CRYSTAL` | `neovitae:spiritus_nihilum_crystal` | 156 |
| `SPIRITUS_VINDICTA_CRYSTAL` | `neovitae:spiritus_vindicta_crystal` | 157 |
| `SPIRITUS_INVICTUS_CRYSTAL` | `neovitae:spiritus_invictus_crystal` | 158 |
| `ROUTING_CONDUIT` | `neovitae:routing_conduit` | 161 |
| `INPUT_ROUTING_NODE` | `neovitae:input_routing_node` | 162 |
| `OUTPUT_ROUTING_NODE` | `neovitae:output_routing_node` | 163 |
| `OMNI_ROUTING_NODE` | `neovitae:omni_routing_node` | 164 |
| `MASTER_ROUTING_NODE` | `neovitae:master_routing_node` | 165 |
| `SHAPED_CHARGE` | `neovitae:shaped_charge` | 169 |
| `AUG_SHAPED_CHARGE` | `neovitae:aug_shaped_charge` | 170 |
| `DEFORESTER_CHARGE` | `neovitae:deforester_charge` | 172 |
| `VEINMINE_CHARGE` | `neovitae:veinmine_charge` | 174 |
| `FUNGAL_CHARGE` | `neovitae:fungal_charge` | 176 |
| `MIMIC` | `neovitae:mimic` | 179 |
| `ETHEREAL_MIMIC` | `neovitae:ethereal_mimic` | 181 |
| `INVERSION_PILLAR` | `neovitae:inversion_pillar` | 184 |
| `INVERSION_PILLAR_CAP` | `neovitae:inversion_pillar_cap` | 185 |
| `SPATIAL_RIFT` | `neovitae:spatial_rift` | 186 |
| `DUNGEON_CONTROLLER` | `neovitae:dungeon_controller` | 188 |
| `DUNGEON_SEAL` | `neovitae:dungeon_seal` | 189 |
| `DUNGEON_SEAL_INACCESSIBLE` | `neovitae:dungeon_seal_inaccessible` | 190 |
| `SANDS_OF_VITAE` | `neovitae:sands_of_vitae` | 192 |
| `BLOOD_STAINED_GLASS` | `neovitae:blood_stained_glass` | 194 |
| `BLOOD_STAINED_GLASS_PANE` | `neovitae:blood_stained_glass_pane` | 196 |

## Blood Orb tier data — important resource distinction

From [BloodOrbStats.java](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/datagen/java/com/breakinblocks/neovitae/datagen/content/datamap/BloodOrbStats.java), **separate** the orb's item-local EV storage from the player's maximum Anima capacity. The source's six ordered data-map entries are:

| Registered Orb ID | Ara Vitae tier | Item EV fluid capacity (mB-equivalent as defined) | Personal Anima cap (EV) | Raw source parameters |
| --- | ---: | ---: | ---: | --- |
| `neovitae:blood_orb_weak` | 0 | 4000 | 5000 | `0, 4000, 5000, 2` |
| `neovitae:blood_orb_apprentice` | 1 | 6000 | 25000 | `1, 6000, 25000, 5` |
| `neovitae:blood_orb_magician` | 2 | 8000 | 150000 | `2, 8000, 150000, 15` |
| `neovitae:blood_orb_master` | 3 | 10000 | 1000000 | `3, 10000, 1000000, 25` |
| `neovitae:blood_orb_archmage` | 4 | 12000 | 5000000 | `4, 12000, 5000000, 50` |
| `neovitae:blood_orb_transcendent` | 5 | 14000 | 10000000 | `5, 14000, 10000000, 160` |

**Wiki discrepancy to resolve:** the same source revision's `wiki/Quick-Reference.md` uses a different sequence of names (e.g., 'Dominator/Imperator' instead of the `BloodOrbStats` keys and names in `wiki/Blood-Orbs-and-Anima.md`). Use exact registry keys and the above data-map values for the pinned-source pilot; verify actual localized names and JEI before writing player-facing quest names. The **third parameter is Anima cap, not Orb fluid capacity**. Never collapse the two into a single resource.

## Research status

These declarations are source-backed, **not directly inspected in the pinned JAR**. Dynamic registries, recipe-specific items, translated names, branch gates and output behaviors still require recipe/generated data review. See [pilot gaps](../mods/neovitae.md#research-gaps-and-quest-readiness).
