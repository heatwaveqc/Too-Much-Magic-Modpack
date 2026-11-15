# Natural stone Galore recipes and marble worldgen unification

Generated from exact hash-pinned Minecraft 1.21.1 NeoForge server JARs.
Generation: 24 Galore recipes, 12 marble stonecutter conversions, one Primal worldgen override.

## Generator operation
Place one of the listed stone blocks BELOW Cobblegen Galore. Water on one horizontal side, lava on the opposite side. One item per cycle at base speed (tier scales output), inserted directly into an inventory above. No water/lava consumption. Seed block remains in world.

## Natural modded stones: all approved by source audit
| Block ID | Pinned worldgen evidence | Native loot (static JAR data) |
|---|---|---|
| forbidden_arcanus:darkstone | data/forbidden_arcanus/neoforge/biome_modifier/add_darkstone.json | forbidden_arcanus:darkstone |
| pastel:ashen_blackslag | data/pastel/worldgen/configured_feature/varia_sprouts.json | pastel:ash_flakes, pastel:ashen_blackslag, pastel:cobbled_blackslag |
| pastel:blackslag | data/pastel/worldgen/biome/black_langast.json | pastel:blackslag, pastel:cobbled_blackslag |
| pastel:overgrown_blackslag | data/pastel/worldgen/configured_feature/disk_overgrown_slush.json | pastel:cobbled_blackslag, pastel:overgrown_blackslag |
| deeperdarker:blooming_sculk_stone | data/deeperdarker/worldgen/configured_feature/blooming_sculk_patch.json | deeperdarker:blooming_sculk_stone, deeperdarker:cobbled_sculk_stone |
| deeperdarker:sculk_stone | data/deeperdarker/worldgen/biome/deeplands.json | deeperdarker:cobbled_sculk_stone, deeperdarker:sculk_stone |
| pastel:downstone | data/pastel/worldgen/configured_feature/dd_downstone.json | No direct loot file found |
| pastel:hummingstone | data/pastel/worldgen/biome/crystal_gardens.json | pastel:hummingstone |
| vampirism:dark_stone | data/vampirism/worldgen/configured_feature/ore_dark_stone.json | vampirism:cobbled_dark_stone, vampirism:dark_stone |
| astralsorcery:marble_raw | data/astralsorcery/worldgen/configured_feature/marble_ore.json | astralsorcery:marble_raw |
| pastel:basal_marble | data/pastel/worldgen/biome/howling_spires.json | pastel:basal_marble |
| primalmagick:marble_raw | data/primalmagick/worldgen/configured_feature/ore_marble_raw.json | primalmagick:marble_raw |
| deeperdarker:gloomslate | data/deeperdarker/worldgen/biome/overcast_columns.json | deeperdarker:cobbled_gloomslate, deeperdarker:gloomslate |
| pastel:shale_clay | data/pastel/worldgen/configured_feature/dd_bismuth.json | pastel:shale_clay |

## Additional generator outputs

Harvested cobbled variants: pastel:cobbled_blackslag, deeperdarker:cobbled_sculk_stone, deeperdarker:cobbled_gloomslate, vampirism:cobbled_dark_stone.
Vanilla natural stones not already native to Galore: minecraft:deepslate, minecraft:dripstone_block, minecraft:smooth_basalt, minecraft:magma_block, minecraft:sandstone, minecraft:red_sandstone.
Galore's own cobblestone, stone, basalt, granite, andesite, diorite, tuff, netherrack, obsidian and cobbled deepslate recipes are preserved.

## Marble unification

The original configured Primal Magick ore-marble feature, source primalmagick-neoforge-1.21.1-6.0.7.jar, replaces primalmagick:marble_raw with astralsorcery:marble_raw. It preserves original target tag, count, size, placement, biomes and biome modifiers. This alters only new ordinary overworld terrain; existing chunks unchanged.
Pastel Basal Marble stays in its dimension because it is an intentionally different biome stone; Goety's dedicated structure palettes are unchanged. All four marble block types have 1:1 bidirectional stonecutter conversions.

## Excluded stone-named worldgen materials

| Block ID | Reason |
|---|---|
| astralsorcery:aquamarine_shale | Aquamarine-bearing ore-equivalent; native Chalice fluid consumption retained |
| evilcraft:infested_nether_netherrack | Infested special Nether resource block |
| forbidden_arcanus:runic_darkstone | Rune-bearing ore; mining yields Forbidden Rune, not common building stone |
| forbidden_arcanus:runic_deepslate | Rune-bearing ore |
| forbidden_arcanus:runic_stone | Rune-bearing ore |
| malum:brilliant_deepslate | Brilliant ore variant, not plain terrain |
| malum:brilliant_stone | Brilliant ore-forming resource block; verify loot before unlimited generation |
| pastel:budding_moonstone | Budding gemstone crop, not stone |
| pastel:hummingstone_glass | Glass, not stone |
| pastel:infested_blackslag | Infested host block; not ordinary stone |
| pastel:large_moonstone_bud | Gemstone crystal crop |
| pastel:medium_moonstone_bud | Gemstone crystal crop |
| pastel:moonstone_block | Gemstone storage/geode; not common stone |
| pastel:moonstone_cluster | Gemstone crystal crop |
| pastel:rock_crystal | Special rock crystal resource, not common stone |
| pastel:slate_noxcap_block | Fungal block named Slate, not mineral stone |
| pastel:slate_noxcap_gills | Fungal gills, not mineral stone |
| pastel:slate_noxcap_stem | Fungal stem, not mineral stone |
| pastel:slate_noxshroom | Mushroom, not mineral stone |
| pastel:small_moonstone_bud | Gemstone crystal crop |
| pastel:stripped_slate_noxcap_stem | Stripped fungal stem, not mineral stone |

## QA still required

A server boot and static registry assets do not establish natural spawn in every dimension, actual block-entity Galore behavior, stonecutter GUI entries, or performance gains. Test in disposable client world: dimension terrain, new overworld marble, JEI, block harvest, tier scaling, output buffer and water/lava source preservation. Profile sustained farms with spark.
No Aquamarine Shale recipe exists: that resource remains in Astral's fluid-consuming Chalice system.
