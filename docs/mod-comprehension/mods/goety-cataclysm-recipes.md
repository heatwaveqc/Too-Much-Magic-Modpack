# Goety Cataclysm — exact artifact recipe crosswalk

Companion to [the gameplay profile](goety-cataclysm.md). Research base `cb8e2dcab59cc625500b3ca0329df1511823610b`; inspected 2026-11-16. **Researching, no gameplay tests.**

Evidence: all 61 JSON recipes under `data/goety_cataclysm/recipe/` in the SHA-512-matched `goety_cataclysm-1.21.1-1.8.4.jar` (full hash in the profile). Counts: 28 focus recipes, 13 servant summons, **11 conversions**, one Fabricator ritual and eight other material/block crafts. This corrects the earlier ten-conversion count. Each row's recipe key is relative to that directory; prefix `goety_cataclysm:` for the JEI/book recipe ID. Ingredient tags are choices resolved by the assembled server; these are not guarantees that every tag is nonempty.

**Ritual units:** exact Goety 3.2.2 `DarkAltarBlockEntity.tick()` advances `currentTime` only on game times divisible by 20 and debits `soulCost` each advance. Thus `(1, 30)` means 30 charged advances at 1 Soul Energy each, approximately 30 seconds at 20 TPS, not one soul over 30 ticks. Nominal uninterrupted totals below are `soulCost × duration`; lag, interruption, hooks and runtime overrides require tests. Activation is a separate input from pedestal ingredients. Craft/conversion completion consumes it; do not treat it as a free reusable key. Item result and entity target are listed separately; `goety:jei_dummy/none` is a display placeholder.

Book names below are matched from bundled English entry recipe references or item mappings; a missing match is an explicit guide-reference gap. A book match proves bundled reference data, not client visibility. Shaped-craft ingredient totals do not substitute for JEI's actual grid.

## Focus choices

These are optional branches after the corresponding Goety capability, not 28 mandatory tiers. Ritual route lists `craftType`, then charge per advance × advance count = nominal total. Table crafting has no ritual charge in this data.

| Recipe key | Route / nominal Soul Energy | Activation input | Pedestal or table ingredients | Bundled guide reference |
| --- | --- | --- | --- | --- |
| `focus/abyssal_beam_focus` | deep; 1 × 10 = 10 | `goety:updraft_focus` | 1 × `goety_cataclysm:abyss_essence`; 1 × `cataclysm:athame`; 1 × `minecraft:phantom_membrane`; 1 × `goety:ectoplasm`; 1 × `#c:storage_blocks/amethyst`; 1 × `goety:prisma_beam_focus` | Abyssal Beam Focus |
| `focus/abyssal_mine_focus` | deep; 1 × 10 = 10 | `goety:biomine_focus` | 1 × `goety_cataclysm:abyss_essence`; 1 × `goety:scatter_focus`; 3 × `minecraft:tnt`; 1 × `cataclysm:athame` | Abyssal Mine Focus |
| `focus/abyssal_orb_focus` | deep; 1 × 10 = 10 | `goety:electrocute_focus` | 1 × `goety_cataclysm:abyss_essence`; 1 × `#c:dusts/glowstone`; 1 × `minecraft:end_crystal`; 1 × `cataclysm:athame` | Abyssal Orb Focus |
| `focus/amethyst_cluster_focus` | crafting table, shaped | none | 4 × `cataclysm:amethyst_crab_shell`; 4 × `cataclysm:amethyst_crab_meat`; 1 × `goety:empty_focus` | Amethyst Cluster Focus |
| `focus/ashen_breath_focus` | crafting table, shaped | none | 4 × `minecraft:blackstone`; 4 × `minecraft:magma_block`; 1 × `goety:fire_breath_focus` | Ashen Breath Focus |
| `focus/battlefield_focus` | necroturgy; 1 × 30 = 30 | `goety:quaking_focus` | 1 × `goety:biting_focus`; 1 × `cataclysm:cursium_upgrade_smithing_template`; 1 × `cataclysm:soul_render` | Battlefield Focus |
| `focus/cursed_cairn_focus` | necroturgy; 1 × 10 = 10 | `goety_cataclysm:cursed_grave_focus` | 1 × `#c:tools/crossbows`; 2 × `goety:ectoplasm` | Cursed Cairn Focus |
| `focus/cursed_grave_focus` | necroturgy; 1 × 10 = 10 | `goety:rotting_focus` | 1 × `cataclysm:black_steel_axe`; 1 × `cataclysm:black_steel_sword`; 1 × `minecraft:iron_helmet`; 2 × `goety:ectoplasm`; 2 × `#c:bones`; 1 × `#c:leather` | Cursed Grave Focus |
| `focus/cursed_tomb_focus` | necroturgy; 1 × 10 = 10 | `goety_cataclysm:cursed_grave_focus` | 1 × `cataclysm:black_steel_targe`; 2 × `#c:ingots/gold` | Cursed Tomb Focus |
| `focus/death_laser_focus` | storm; 1 × 30 = 30 | `goety:thunderbolt_focus` | 1 × `cataclysm:laser_gatling`; 2 × `goety_cataclysm:mechanized_iron_block`; 1 × `cataclysm:mech_eye`; 1 × `goety:discharge_focus` | Death Laser Focus |
| `focus/desert_crush_focus` | magic; 1 × 10 = 10 | `goety:teeth_focus` | 2 × `cataclysm:ancient_metal_block`; 6 × `#c:sandstone` | Desert Crush Focus |
| `focus/desert_raid_focus` | necroturgy; 1 × 10 = 10 | `goety:animation_core` | 1 × `cataclysm:ancient_metal_ingot`; 1 × `cataclysm:khopesh`; 4 × `cataclysm:koboleton_bone` | Desert Raid Focus |
| `focus/earth_shake_focus` | geoturgy; 1 × 10 = 10 | `goety:quaking_focus` | 1 × `cataclysm:ancient_metal_ingot`; 1 × `minecraft:tnt`; 1 × `cataclysm:amethyst_crab_shell`; 1 × `minecraft:bell` | Earth Shake Focus |
| `focus/extinct_flame_focus` | expert_nether; 5 × 10 = 50 | `goety:lavaball_focus` | 1 × `cataclysm:ignitium_ingot`; 1 × `goety:ectoplasm`; 1 × `#c:ender_pearls`; 1 × `#c:rods/blaze` | Extinct Flame Focus |
| `focus/flare_bomb_focus` | expert_nether; 5 × 10 = 50 | `goety:magma_bomb_focus` | 4 × `cataclysm:lava_power_cell`; 4 × `#c:rods/blaze` | Flare Bomb Focus |
| `focus/kakourgos_focus` | deep; 1 × 10 = 10 | `goety:animation_core` | 4 × `minecraft:pufferfish`; 4 × `minecraft:kelp` | Kakoúrgos Focus |
| `focus/kyria_focus` | deep; 1 × 10 = 10 | `goety:animation_core` | 2 × `cataclysm:lacrima`; 1 × `goety_cataclysm:water_spear_focus`; 1 × `cataclysm:essence_of_the_storm` | Kyría Focus |
| `focus/lightning_spear_focus` | storm; 1 × 30 = 30 | `goety_cataclysm:water_spear_focus` | 1 × `goety:lightning_focus`; 1 × `goety:mystic_core`; 1 × `#c:tools/tridents`; 1 × `cataclysm:essence_of_the_storm` | Lightning Spear Focus |
| `focus/polemistis_focus` | deep; 1 × 10 = 10 | `goety:rotting_focus` | 8 × `minecraft:ink_sac` | Polemistís Focus |
| `focus/sandstorm_focus` | sky; 1 × 10 = 10 | `goety:cyclone_focus` | 1 × `cataclysm:sandstorm_in_a_bottle`; 1 × `cataclysm:ancient_metal_ingot`; 1 × `#c:sand` | Sandstorm Focus |
| `focus/storm_serpent_focus` | storm; 1 × 30 = 30 | `goety:animation_core` | 1 × `cataclysm:astrape`; 1 × `minecraft:ghast_tear`; 2 × `cataclysm:essence_of_the_storm` | Storm Serpent Focus |
| `focus/sunken_current_focus` | deep; 1 × 10 = 10 | `goety_cataclysm:sunken_swell_focus` | 1 × `cataclysm:coral_bardiche`; 2 × `goety_cataclysm:abyss_essence` | Sunken Current Focus |
| `focus/sunken_swell_focus` | deep; 1 × 10 = 10 | `goety:hunting_focus` | 1 × `#c:tools/fishing_rods`; 1 × `cataclysm:coral_spear`; 1 × `#c:ender_pearls`; 2 × `goety_cataclysm:abyss_essence` | Sunken Swell Focus |
| `focus/sunken_tribune_focus` | deep; 1 × 10 = 10 | `goety_cataclysm:sunken_swell_focus` | 1 × `cataclysm:athame`; 2 × `goety_cataclysm:abyss_essence` | Sunken Tribune Focus |
| `focus/thunder_rage_focus` | storm; 1 × 30 = 30 | `goety:monsoon_focus` | 1 × `goety_cataclysm:lightning_spear_focus`; 1 × `goety:whirlwind_focus`; 1 × `cataclysm:lacrima`; 1 × `cataclysm:essence_of_the_storm` | Thunder Rage Focus |
| `focus/void_rune_focus` | crafting table, shaped | none | 4 × `cataclysm:void_stone`; 4 × `goety:magic_emerald`; 1 × `goety:empty_focus` | Void Rune Focus |
| `focus/void_vortex_focus` | end; 1 × 10 = 10 | `goety:wind_core` | 4 × `cataclysm:obsidian_explosion_trap_bricks`; 4 × `minecraft:dragon_breath` | Void Vortex Focus |
| `focus/water_spear_focus` | crafting table, shaped | none | 4 × `cataclysm:lacrima`; 4 × `minecraft:pointed_dripstone`; 1 × `goety:bouncy_bubble_focus` | Water Spear Focus |

## Servant summons

All rows use `goety:summon_tamed`. Output is the declared entity, not an inventory item. Lifetime defaults to `-1` in the parent recipe codec when absent; that data default alone does not prove permanent persistence, cross-dimension following or safe recovery.

| Recipe key | Craft type; nominal Soul Energy | Activation | Pedestal ingredients | Entity output |
| --- | --- | --- | --- | --- |
| `thrall/summon_aptrgangr_servant` | necroturgy; 5 × 30 = 150 | `cataclysm:aptrgangr_head` | 4 × `#c:bones`; 4 × `cataclysm:black_steel_block` | `goety_cataclysm:aptrgangr` |
| `thrall/summon_cindaria_servant_thrall` | deep; 1 × 10 = 10 | `goety_cataclysm:water_spear_focus` | 1 × `cataclysm:lacrima`; 1 × `minecraft:obsidian` | `goety_cataclysm:cindaria_servant` |
| `thrall/summon_clawdian_servant_thrall` | deep; 1 × 10 = 10 | `cataclysm:essence_of_the_storm` | 2 × `cataclysm:chitin_claw`; 2 × `cataclysm:lacrima`; 2 × `cataclysm:blessed_amethyst_crab_meat`; 2 × `cataclysm:amethyst_crab_shell` | `goety_cataclysm:clawdian` |
| `thrall/summon_deepling_angler_servant_thrall` | deep; 1 × 10 = 10 | `#c:tools/fishing_rods` | 1 × `goety_cataclysm:abyss_essence`; 1 × `#c:ender_pearls` | `goety_cataclysm:deepling_angler_servant` |
| `thrall/summon_deepling_servant_thrall` | deep; 1 × 10 = 10 | `cataclysm:coral_spear` | 1 × `goety_cataclysm:abyss_essence`; 1 × `#c:ender_pearls` | `goety_cataclysm:deepling_servant` |
| `thrall/summon_draugr_servant_thrall` | necroturgy; 1 × 10 = 10 | `cataclysm:black_steel_ingot` | 1 × `goety:ectoplasm`; 1 × `minecraft:rotten_flesh`; 1 × `#c:bones`; 1 × `#c:leather` | `goety_cataclysm:draugr_servant` |
| `thrall/summon_elite_draugr_servant_thrall` | necroturgy; 1 × 10 = 10 | `#c:tools/crossbows` | 1 × `goety:ectoplasm`; 1 × `minecraft:rotten_flesh`; 1 × `#c:bones`; 1 × `#c:leather`; 1 × `cataclysm:black_steel_ingot` | `goety_cataclysm:elite_draugr_servant` |
| `thrall/summon_hippocamtus_servant_thrall` | deep; 1 × 10 = 10 | `cataclysm:azure_sea_shield` | 4 × `cataclysm:lacrima`; 2 × `minecraft:prismarine`; 1 × `#c:storage_blocks/gold`; 1 × `#minecraft:corals` | `goety_cataclysm:hippocamtus` |
| `thrall/summon_koboleton_servant_thrall` | necroturgy; 1 × 10 = 10 | `cataclysm:koboleton_bone` | 1 × `cataclysm:koboleton_bone`; 1 × `cataclysm:ancient_metal_nugget`; 1 × `cataclysm:khopesh` | `goety_cataclysm:koboleton_servant` |
| `thrall/summon_prowler_servant` | forge; 1 × 30 = 30 | `cataclysm:mech_eye` | 1 × `minecraft:wither_skeleton_skull`; 1 × `cataclysm:wither_assault_shoulder_weapon`; 1 × `cataclysm:meat_shredder`; 2 × `goety_cataclysm:mechanized_iron_block` | `goety_cataclysm:the_prowler` |
| `thrall/summon_royal_draugr_servant_thrall` | necroturgy; 5 × 30 = 150 | `cataclysm:black_steel_targe` | 1 × `goety:ectoplasm`; 1 × `minecraft:rotten_flesh`; 1 × `#c:bones`; 1 × `#c:leather`; 1 × `cataclysm:black_steel_ingot`; 1 × `#c:ingots/gold` | `goety_cataclysm:royal_draugr_servant` |
| `thrall/summon_urchinkin_servant_thrall` | deep; 1 × 10 = 10 | `minecraft:pufferfish` | 1 × `minecraft:pufferfish`; 1 × `minecraft:kelp` | `goety_cataclysm:urchinkin_servant` |
| `thrall/summon_zombie_amethyst_crab_servant_thrall` | necroturgy; 10 × 10 = 100 | `#c:storage_blocks/amethyst` | 5 × `cataclysm:amethyst_crab_meat`; 3 × `cataclysm:amethyst_crab_shell` | `goety_cataclysm:zombie_amethyst_crab` |

## Entity conversions

Use Black Book → Cataclysm → Mutations and the exact JEI recipe. The target tag is an eligibility set, not proof of existing ownership requirements. Parent `ConvertRitual.finish()` consumes the activation input and calls `MobUtil.convertTo` with the casting player on tamed conversions; the helper's full ownership/equipment transfer semantics remain open. `convert_complete_tamed` differs from `convert_tamed`; do not promise identical equipment preservation.

| Recipe key | Craft type / research; nominal Soul Energy | Activation | Target entity tag → result | Pedestal ingredients |
| --- | --- | --- | --- | --- |
| `convert_baby_leviathan_to_leviathan_servant` | deep / terminus; 10 × 10 = 100 | `goety:aging_cream` | `#goety_cataclysm:baby_leviathan` → `goety_cataclysm:leviathan` (goety:convert_tamed) | 1 × `cataclysm:tidal_claws`; 4 × `goety_cataclysm:primordial_essence`; 2 × `goety_cataclysm:abyss_essence`; 1 × `cataclysm:abyssal_sacrifice` |
| `convert_blazes_to_ignited_berserker` | expert_nether / bygone; 5 × 10 = 50 | `cataclysm:dying_ember` | `#goety:blazes` → `goety_cataclysm:ignited_berserker` (goety:convert_tamed) | 2 × `minecraft:netherite_sword`; 2 × `#c:rods/blaze`; 1 × `goety:ectoplasm`; 1 × `#minecraft:soul_fire_base_blocks` |
| `convert_deepling_servant_to_deepling_brute` | deep / no explicit recipe research; 1 × 10 = 10 | `cataclysm:coral_bardiche` | `#goety_cataclysm:deepling_servants` → `goety_cataclysm:deepling_brute_servant` (goety:convert_tamed) | 1 × `goety_cataclysm:abyss_essence`; 1 × `minecraft:obsidian` |
| `convert_deepling_servant_to_deepling_priest` | deep / no explicit recipe research; 1 × 10 = 10 | `cataclysm:athame` | `#goety_cataclysm:deepling_servants` → `goety_cataclysm:deepling_priest_servant` (goety:convert_tamed) | 1 × `goety_cataclysm:abyss_essence`; 1 × `#c:gems/amethyst`; 1 × `#c:dusts/prismarine` |
| `convert_deepling_servant_to_deepling_warlock` | deep / no explicit recipe research; 1 × 10 = 10 | `cataclysm:athame` | `#goety_cataclysm:deepling_servants` → `goety_cataclysm:deepling_warlock_servant` (goety:convert_tamed) | 1 × `goety_cataclysm:abyss_essence`; 1 × `#c:gems/amethyst`; 1 × `minecraft:crying_obsidian` |
| `convert_draugr_to_draugr_necromancer` | necroturgy / buried; 5 × 30 = 150 | `goety:necro_cape` | `#goety_cataclysm:draugrs` → `goety_cataclysm:draugr_necromancer_servant` (goety:convert_complete_tamed) | 1 × `goety:necro_staff`; 1 × `goety:necro_crown`; 1 × `goety:ectoplasm`; 1 × `goety_cataclysm:cursed_grave_focus` |
| `convert_drowned_servant_to_drowned_host` | deep / no explicit recipe research; 1 × 10 = 10 | `minecraft:ink_sac` | `#goety_cataclysm:drowned_servants` → `goety_cataclysm:drowned_host_servant` (goety:convert_tamed) | 3 × `minecraft:ink_sac` |
| `convert_koboleton_to_kobolediator_servant` | necroturgy / no explicit recipe research; 10 × 10 = 100 | `cataclysm:kobolediator_skull` | `#goety_cataclysm:koboletons` → `goety_cataclysm:kobolediator` (goety:convert_tamed) | 1 × `cataclysm:necklace_of_the_desert`; 4 × `cataclysm:ancient_metal_block`; 1 × `cataclysm:bone_reptile_chestplate`; 2 × `cataclysm:koboleton_bone` |
| `convert_koboleton_to_wadjet_servant` | necroturgy / no explicit recipe research; 10 × 10 = 100 | `cataclysm:ancient_spear` | `#goety_cataclysm:koboletons` → `goety_cataclysm:wadjet` (goety:convert_tamed) | 1 × `goety_cataclysm:desert_crush_focus`; 2 × `cataclysm:ancient_metal_ingot`; 1 × `cataclysm:bone_reptile_chestplate`; 4 × `cataclysm:koboleton_bone` |
| `convert_modern_remnant_to_ancient_remnant_servant` | necroturgy / terminus; 10 × 10 = 100 | `cataclysm:necklace_of_the_desert` | `#goety_cataclysm:modern_remnants` → `goety_cataclysm:ancient_remnant` (goety:convert_tamed) | 1 × `cataclysm:sandstorm_in_a_bottle`; 4 × `goety_cataclysm:primordial_essence`; 1 × `cataclysm:bone_reptile_helmet`; 1 × `cataclysm:bone_reptile_chestplate`; 1 × `cataclysm:desert_eye` |
| `convert_wildfire_to_ignited_revenant` | expert_nether / bygone; 5 × 10 = 50 | `cataclysm:burning_ashes` | `#goety_cataclysm:wildfires` → `goety_cataclysm:ignited_revenant` (goety:convert_tamed) | 1 × `goety:ectoplasm`; 1 × `#c:bones`; 1 × `#minecraft:soul_fire_base_blocks` |

### Bundled target membership

- `#goety_cataclysm:baby_leviathan`: `cataclysm:the_baby_leviathan`.
- `#goety_cataclysm:deepling_servants`: `goety_cataclysm:deepling_servant`, `goety_cataclysm:deepling_angler_servant`.
- `#goety_cataclysm:draugrs`: `cataclysm:draugr`, `goety_cataclysm:draugr_servant`, `cataclysm:elite_draugr`, `goety_cataclysm:elite_draugr_servant`, `cataclysm:royal_draugr`, `goety_cataclysm:royal_draugr_servant`.
- `#goety_cataclysm:draugr_servants`: `goety_cataclysm:draugr_servant`, `goety_cataclysm:elite_draugr_servant`, `goety_cataclysm:royal_draugr_servant`, `goety_cataclysm:draugr_necromancer_servant`, `goety_cataclysm:aptrgangr`.
- `#goety_cataclysm:drowned_servants`: `goety:drowned_servant`.
- `#goety_cataclysm:koboletons`: `cataclysm:koboleton`, `goety_cataclysm:koboleton_servant`.
- `#goety_cataclysm:modern_remnants`: `cataclysm:modern_remnant`.
- `#goety_cataclysm:wildfires`: `goety:wildfire`.

These additive tags may be expanded by other datapacks. `#goety:blazes` comes from the parent and needs separate effective-tag inspection. Do not replace a broad tag with a single guide display entity.

## Materials and Fabricator construction

| Recipe key | Route | Inputs | Output |
| --- | --- | --- | --- |
| `abyss_essence` | crafting_shaped | 4 × `minecraft:crying_obsidian`; 4 × `minecraft:obsidian`; 1 × `goety_cataclysm:abyss_essence` | 2 × `goety_cataclysm:abyss_essence` |
| `fabricator` | forge; 1 × 30 = 30 souls | activation `goety:fireball_focus`; 1 × `goety:cursed_cage`; 1 × `cataclysm:witherite_ingot`; 2 × `goety_cataclysm:mechanized_iron_block`; 1 × `minecraft:anvil` | 1 × `goety_cataclysm:fabricator` |
| `mechanized_iron_block` | crafting_shaped | 5 × `#c:ingots/iron`; 4 × `#c:dusts/redstone` | 1 × `goety_cataclysm:mechanized_iron_block` |
| `netherite_plating` | crafting_shaped | 4 × `#c:ingots/netherite`; 4 × `#c:rods/blaze`; 1 × `#c:obsidian` | 1 × `goety_cataclysm:netherite_plating` |
| `primordial_essence` | crafting_shapeless | 1 × `cataclysm:ancient_metal_block`; 1 × `goety_cataclysm:netherite_plating`; 1 × `minecraft:sniffer_egg`; 1 × `minecraft:nautilus_shell` | 1 × `goety_cataclysm:primordial_essence` |
| `void_crystal` | crafting_shaped | 4 × `cataclysm:void_jaw`; 4 × `#c:gems/amethyst`; 1 × `#c:obsidian` | 1 × `cataclysm:void_crystal` |
| `void_lantern_block` | crafting_shaped | 4 × `cataclysm:void_jaw`; 4 × `#c:dusts/glowstone`; 1 × `#c:obsidian` | 1 × `cataclysm:void_lantern_block` |
| `void_mold_block` | crafting_shaped | 4 × `#c:gems/diamond`; 4 × `#c:obsidian`; 1 × `cataclysm:void_crystal` | 4 × `goety_cataclysm:void_mold_block` |
| `void_stone` | crafting_shaped | 8 × `cataclysm:void_jaw`; 1 × `#c:obsidian` | 1 × `cataclysm:void_stone` |

Abyss Essence has a **net +1** expansion craft: one existing essence plus four Crying Obsidian and four Obsidian produces two essence. It does not establish the first-essence acquisition route. The bundled Leviathan injection declares four essence; registration/loot-codec execution must be verified before teaching guaranteed drops. Primordial Essence additionally requires Netherite Plating, a Sniffer Egg and other materials; it is not a cheap early resource. Recipe results in the `cataclysm` namespace are deliberate addon recipes, not automatically native parent recipes.

## Remaining coverage

This crosswalk closes the recipe inventory, not all progression. Required remaining work: verify research unlock predicates and altar structure references; complete focus effects/costs and servant care/death recovery; verify every Cataclysm ingredient's acquisition path; verify effective tags, datapack conditions and loot hooks in a disposable world. Guide matches above must be opened in the client before acceptance. The profile stays `researching`.

## Cataclysm dependency identity check

The 47 distinct explicit `cataclysm:` IDs in these recipes (items and block items) were compared with the exact Cataclysm 3.33 `ModItems`/`ModBlocks` registration-name literals; all 47 matched. Conversion tag membership names entities separately. Parent JAR: `L_Ender's Cataclysm 1.21.1-3.33.jar`, SHA-512 `289f6e8364a6e432f34803a47cff8381899a1df0fdc675170323b43fd1c09579b1320d8b4e0de5df65c609b738d450c13c89e240d6d79f607888da26b7724947`, matched to [the parent pin](../../../mods/l_enders-cataclysm.pw.toml). This validates identifier availability in that artifact's registration code; it does not show which boss drops each item, that a recipe successfully loads, or that tag choices are populated in TMM.
