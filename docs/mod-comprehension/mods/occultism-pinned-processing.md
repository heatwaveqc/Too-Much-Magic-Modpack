# Occultism — pinned processing and miner output crosswalk

Companion to [main profile](occultism.md) and [ritual acquisition](occultism-pinned-rituals.md). Same exact1.227.0 SHA512 artifact recorded there, hash-matched2026-11-16. **Recipe/source evidence; no loaded-tag, client or throughput acceptance.** All529 records here are bundled resources:240 crushing+194 miner+54 crystallize+27 spirit-fire+14 trade. No assumption that every optional metal/tag exists in TMM.

## Worker and output semantics

- Summon correct tier/job from ritual appendix; consult Dictionary **Crushing / Crystallizing / Dimensional Mineshaft / Spirit Trading / Spirit Fire** and exact JEI recipe. Teach acquisition, Book of Calling/configuration and safe input/output area before automating. Miner input is a selector/tool, not one consumed ore per result.
- Crushing/Crystallize recipe min/max tier−1 is unrestricted; omitted raw time defaults200. Job actual timer threshold multiplies raw time by configured worker time factor. Batch size is min(job operationCount, held count); actual output count truncates(base count×batch×job output factor), except ignore-multiplier forces factor1. Input is debited before job event/output handling. Thus table counts/time are base recipe values, not measured worker yield/rate. Exact crusher defaults/formulas remain in main profile.
- Crusher/Crystallizer/Trader use Extractor block below worker to route into handler two blocks below when available. They ignore returned insertion remainder and suppress loose fallback once handler exists: full/partial inventory can lose output. Without route, they spawn tagged ItemEntities with pickup delay protection. Job events can replace/suppress output after consumed input; not a preflight rollback transaction.
- Tag result chooses a cached ItemStack through TagUtil, not one random result per displayed tag member. Exact TagRecipeResult produces a named Barrier placeholder when resolution is empty; conditional nonempty guards usually prevent it, but removals/custom data need tests. Viewer getStacks enumerates alternatives, not simultaneous multiple outputs.
- Mineshaft caches all MinerRecipe matches to input slot0, then WeightedRandom picks each roll using **relative weights among matching loaded entries**. Weight142 is not142% or a fixed chance independent of the pool. Batch rolls/operation and item durability/speed come from initialized miner state; Fortune adds random extra rolls, Silk Touch randomly multiplies each selected count under native feature flags. Redstone pauses/resets miningTime. Efficiency randomly increases countdown decrement under flag.
- One durability damage is requested per nonempty-pool mining operation, regardless of number of rolls; Unbreaking/Unusing, save-miner and config alter life. Unusing at fewer than6 durability remaining pauses in its enabled branch. Output insertion remainder is ignored and no capacity preflight appears in mine(); a full output can waste durability/yield. The near-break saved miner is also inserted without remainder recovery. No FE/fuel requirement is introduced by this method.
- Trade selects weighted results matching input AND trader ID. At conversion interval it consumes one input per selected result, up to configured maxTradesPerRound, then posts TraderJobEvent and delivers output; no chosen-output guarantee. Spirit-fire table is item-in-world conversion data, not worker crushing; exact entityInside/assemble now audited in familiar/tool appendix: discard original and set result count to original stack count; no output refund gate.

## Progression-sensitive native outputs

All194 miner JSON result payloads were scanned for Draconium, Replica/Chaos, Gaia/Wilden trophies, Nether Star, Mine Key, Dragon Egg and uranium/yellorium names. Bundled results include conditional `miner/ores/draconium_ore` (weight142, nonempty ores-miner+Draconium ore tags), uranium ore140, Eldritch raw-uranium/raw-yellorium storage blocks90. No direct Replica Ore/Chaos/Gaia/Wilden/Nether Star/Mine Key/Dragon Egg result was found in those194 payloads. This is a scoped negative, **not absence across all pack recipes or recursive tag members**. Draconium can supply ordinary ore without granting Chaos shards or protected final trophies; investigate current tag membership and gate policy separately.

## Crushing240 records

Input and output tags are typed item tags. Last column lists load conditions joined by AND; blank native conditions means none. Time includes native default when omitted; tier interval shows raw−1 sentinel.

| Recipe (`occultism:`) | Input | Base output | Min..max tier | Raw time | Ignore multiplier | Load conditions |
| --- | --- | --- | --- | ---: | --- | --- |
| `crushing/adamant_dust_from_ingot` | `#c:ingots/adamant` | 1× `#c:dusts/adamant` | -1..-1 | 200 | true | nonempty #c:ingots/adamant; nonempty #c:dusts/adamant |
| `crushing/allthemodium_dirty_dust_from_clump` | `#c:clumps/allthemodium` | 2× `#c:dirty_dusts/allthemodium` | -1..-1 | 200 | false | nonempty #c:clumps/allthemodium; nonempty #c:dirty_dusts/allthemodium |
| `crushing/allthemodium_dust_from_ingot` | `#c:ingots/allthemodium` | 1× `#c:dusts/allthemodium` | -1..-1 | 200 | true | nonempty #c:ingots/allthemodium; nonempty #c:dusts/allthemodium |
| `crushing/allthemodium_dust_from_ore` | `#c:ores/allthemodium` | 2× `#c:dusts/allthemodium` | -1..-1 | 200 | false | nonempty #c:ores/allthemodium; nonempty #c:dusts/allthemodium |
| `crushing/allthemodium_dust_from_raw` | `#c:raw_materials/allthemodium` | 2× `#c:dusts/allthemodium` | -1..-1 | 200 | false | nonempty #c:raw_materials/allthemodium; nonempty #c:dusts/allthemodium |
| `crushing/allthemodium_dust_from_raw_block` | `#c:storage_blocks/raw_allthemodium` | 18× `#c:dusts/allthemodium` | -1..-1 | 1600 | false | nonempty #c:storage_blocks/raw_allthemodium; nonempty #c:dusts/allthemodium |
| `crushing/aluminum_dirty_dust_from_clump` | `#c:clumps/aluminum` | 2× `#c:dirty_dusts/aluminum` | -1..-1 | 200 | false | nonempty #c:clumps/aluminum; nonempty #c:dirty_dusts/aluminum |
| `crushing/aluminum_dust_from_ingot` | `#c:ingots/aluminum` | 1× `#c:dusts/aluminum` | -1..-1 | 200 | true | nonempty #c:ingots/aluminum; nonempty #c:dusts/aluminum |
| `crushing/aluminum_dust_from_ore` | `#c:ores/aluminum` | 2× `#c:dusts/aluminum` | -1..-1 | 200 | false | nonempty #c:ores/aluminum; nonempty #c:dusts/aluminum |
| `crushing/aluminum_dust_from_raw` | `#c:raw_materials/aluminum` | 2× `#c:dusts/aluminum` | -1..-1 | 200 | false | nonempty #c:raw_materials/aluminum; nonempty #c:dusts/aluminum |
| `crushing/aluminum_dust_from_raw_block` | `#c:storage_blocks/raw_aluminum` | 18× `#c:dusts/aluminum` | -1..-1 | 1600 | false | nonempty #c:storage_blocks/raw_aluminum; nonempty #c:dusts/aluminum |
| `crushing/amber_dust_from_gem` | `#c:gems/amber` | 1× `#c:dusts/amber` | -1..-1 | 200 | true | nonempty #c:gems/amber; nonempty #c:dusts/amber |
| `crushing/amber_dust_from_ore` | `#c:ores/amber` | 3× `#c:dusts/amber` | -1..-1 | 200 | false | nonempty #c:ores/amber; nonempty #c:dusts/amber |
| `crushing/amethyst_dust_from_gem` | `#c:gems/amethyst` | 1× `#c:dusts/amethyst` | -1..-1 | 200 | true | nonempty #c:gems/amethyst; nonempty #c:dusts/amethyst |
| `crushing/annealed_copper_dust_from_ingot` | `#c:ingots/annealed_copper` | 1× `#c:dusts/annealed_copper` | -1..-1 | 200 | true | nonempty #c:ingots/annealed_copper; nonempty #c:dusts/annealed_copper |
| `crushing/antimony_dirty_dust_from_clump` | `#c:clumps/antimony` | 2× `#c:dirty_dusts/antimony` | -1..-1 | 200 | false | nonempty #c:clumps/antimony; nonempty #c:dirty_dusts/antimony |
| `crushing/antimony_dust_from_ingot` | `#c:ingots/antimony` | 1× `#c:dusts/antimony` | -1..-1 | 200 | true | nonempty #c:ingots/antimony; nonempty #c:dusts/antimony |
| `crushing/antimony_dust_from_ore` | `#c:ores/antimony` | 2× `#c:dusts/antimony` | -1..-1 | 200 | false | nonempty #c:ores/antimony; nonempty #c:dusts/antimony |
| `crushing/antimony_dust_from_raw` | `#c:raw_materials/antimony` | 2× `#c:dusts/antimony` | -1..-1 | 200 | false | nonempty #c:raw_materials/antimony; nonempty #c:dusts/antimony |
| `crushing/antimony_dust_from_raw_block` | `#c:storage_blocks/raw_antimony` | 18× `#c:dusts/antimony` | -1..-1 | 1600 | false | nonempty #c:storage_blocks/raw_antimony; nonempty #c:dusts/antimony |
| `crushing/apatite_dust_from_gem` | `#c:gems/apatite` | 1× `#c:dusts/apatite` | -1..-1 | 200 | true | nonempty #c:gems/apatite; nonempty #c:dusts/apatite |
| `crushing/apatite_dust_from_ore` | `#c:ores/apatite` | 3× `#c:dusts/apatite` | -1..-1 | 200 | false | nonempty #c:ores/apatite; nonempty #c:dusts/apatite |
| `crushing/arcane_crystal_dust_from_gem` | `#c:gems/arcane_crystal` | 1× `#c:dusts/arcane_crystal` | -1..-1 | 200 | true | nonempty #c:gems/arcane_crystal; nonempty #c:dusts/arcane_crystal |
| `crushing/arcane_crystal_dust_from_ore` | `#c:ores/arcane_crystal` | 3× `#c:dusts/arcane_crystal` | -1..-1 | 200 | false | nonempty #c:ores/arcane_crystal; nonempty #c:dusts/arcane_crystal |
| `crushing/azure_electrum_dust_from_ingot` | `#c:ingots/azure_electrum` | 1× `#c:dusts/azure_electrum` | -1..-1 | 200 | true | nonempty #c:ingots/azure_electrum; nonempty #c:dusts/azure_electrum |
| `crushing/azure_silver_dirty_dust_from_clump` | `#c:clumps/azure_silver` | 2× `#c:dirty_dusts/azure_silver` | -1..-1 | 200 | false | nonempty #c:clumps/azure_silver; nonempty #c:dirty_dusts/azure_silver |
| `crushing/azure_silver_dust_from_ingot` | `#c:ingots/azure_silver` | 1× `#c:dusts/azure_silver` | -1..-1 | 200 | true | nonempty #c:ingots/azure_silver; nonempty #c:dusts/azure_silver |
| `crushing/azure_silver_dust_from_ore` | `#c:ores/azure_silver` | 2× `#c:dusts/azure_silver` | -1..-1 | 200 | false | nonempty #c:ores/azure_silver; nonempty #c:dusts/azure_silver |
| `crushing/azure_silver_dust_from_raw` | `#c:raw_materials/azure_silver` | 2× `#c:dusts/azure_silver` | -1..-1 | 200 | false | nonempty #c:raw_materials/azure_silver; nonempty #c:dusts/azure_silver |
| `crushing/azure_silver_dust_from_raw_block` | `#c:storage_blocks/raw_azure_silver` | 18× `#c:dusts/azure_silver` | -1..-1 | 1600 | false | nonempty #c:storage_blocks/raw_azure_silver; nonempty #c:dusts/azure_silver |
| `crushing/battery_alloy_dust_from_ingot` | `#c:ingots/battery_alloy` | 1× `#c:dusts/battery_alloy` | -1..-1 | 200 | true | nonempty #c:ingots/battery_alloy; nonempty #c:dusts/battery_alloy |
| `crushing/bauxite_dust_from_ore` | `#c:ores/bauxite` | 2× `#c:dusts/bauxite` | -1..-1 | 200 | false | nonempty #c:ores/bauxite; nonempty #c:dusts/bauxite |
| `crushing/beryllium_dust_from_ingot` | `#c:ingots/beryllium` | 1× `#c:dusts/beryllium` | -1..-1 | 200 | true | nonempty #c:ingots/beryllium; nonempty #c:dusts/beryllium |
| `crushing/biosteel_dust_from_ingot` | `#c:ingots/biosteel` | 1× `#c:dusts/biosteel` | -1..-1 | 200 | true | nonempty #c:ingots/biosteel; nonempty #c:dusts/biosteel |
| `crushing/black_quartz_dust_from_gem` | `#c:gems/black_quartz` | 1× `#c:dusts/black_quartz` | -1..-1 | 200 | true | nonempty #c:gems/black_quartz; nonempty #c:dusts/black_quartz |
| `crushing/black_quartz_dust_from_ore` | `#c:ores/black_quartz` | 3× `#c:dusts/black_quartz` | -1..-1 | 200 | false | nonempty #c:ores/black_quartz; nonempty #c:dusts/black_quartz |
| `crushing/blackstone_dust` | `minecraft:blackstone` | 1× `#c:dusts/blackstone` | -1..-1 | 200 | true | none |
| `crushing/blaze_gold_dust_from_ingot` | `#c:ingots/blaze_gold` | 1× `#c:dusts/blaze_gold` | -1..-1 | 200 | true | nonempty #c:ingots/blaze_gold; nonempty #c:dusts/blaze_gold |
| `crushing/blaze_powder_from_rod` | `#c:rods/blaze` | 4× `minecraft:blaze_powder` | -1..-1 | 200 | false | none |
| `crushing/blue_ice_dust` | `minecraft:blue_ice` | 1× `#c:dusts/blue_ice` | 2..-1 | 200 | true | none |
| `crushing/bone_meal_from_bone` | `#c:bones` | 4× `minecraft:bone_meal` | -1..-1 | 200 | false | none |
| `crushing/brass_dust_from_ingot` | `#c:ingots/brass` | 1× `#c:dusts/brass` | -1..-1 | 200 | true | nonempty #c:ingots/brass; nonempty #c:dusts/brass |
| `crushing/brick_dust` | `#c:bricks/normal` | 1× `#c:dusts/brick` | -1..-1 | 200 | true | nonempty #c:bricks/normal; nonempty #c:dusts/brick |
| `crushing/bronze_dust_from_ingot` | `#c:ingots/bronze` | 1× `#c:dusts/bronze` | -1..-1 | 200 | true | nonempty #c:ingots/bronze; nonempty #c:dusts/bronze |
| `crushing/cadmium_dust_from_ingot` | `#c:ingots/cadmium` | 1× `#c:dusts/cadmium` | -1..-1 | 200 | true | nonempty #c:ingots/cadmium; nonempty #c:dusts/cadmium |
| `crushing/calcite_dust` | `minecraft:calcite` | 1× `#c:dusts/calcite` | -1..-1 | 200 | true | none |
| `crushing/carbon_dust_from_plate` | `#c:plates/carbon` | 1× `#c:dusts/carbon` | -1..-1 | 200 | true | nonempty #c:plates/carbon; nonempty #c:dusts/carbon |
| `crushing/certus_quartz_dust_from_gem` | `#c:gems/certus_quartz` | 1× `#c:dusts/certus_quartz` | -1..-1 | 200 | true | nonempty #c:gems/certus_quartz; nonempty #c:dusts/certus_quartz |
| `crushing/charcoal_dust_from_item` | `minecraft:charcoal` | 1× `#c:dusts/charcoal` | -1..-1 | 200 | true | nonempty #c:dusts/charcoal |
| `crushing/chromium_dust_from_ingot` | `#c:ingots/chromium` | 1× `#c:dusts/chromium` | -1..-1 | 200 | true | nonempty #c:ingots/chromium; nonempty #c:dusts/chromium |
| `crushing/cinnabar_dust_from_gem` | `#c:gems/cinnabar` | 1× `#c:dusts/cinnabar` | -1..-1 | 200 | true | nonempty #c:gems/cinnabar; nonempty #c:dusts/cinnabar |
| `crushing/cinnabar_dust_from_ore` | `#c:ores/cinnabar` | 3× `#c:dusts/cinnabar` | -1..-1 | 200 | false | nonempty #c:ores/cinnabar; nonempty #c:dusts/cinnabar |
| `crushing/coal_coke_dust` | `#c:coal_coke` | 1× `#c:dusts/coal_coke` | -1..-1 | 200 | true | nonempty #c:coal_coke; nonempty #c:dusts/coal_coke |
| `crushing/coal_dust_from_item` | `minecraft:coal` | 1× `#c:dusts/coal` | -1..-1 | 200 | true | nonempty #c:dusts/coal |
| `crushing/coal_dust_from_ore` | `#c:ores/coal` | 2× `#c:dusts/coal` | -1..-1 | 200 | false | nonempty #c:ores/coal; nonempty #c:dusts/coal |
| `crushing/cobalt_dirty_dust_from_clump` | `#c:clumps/cobalt` | 2× `#c:dirty_dusts/cobalt` | -1..-1 | 200 | false | nonempty #c:clumps/cobalt; nonempty #c:dirty_dusts/cobalt |
| `crushing/cobalt_dust_from_ingot` | `#c:ingots/cobalt` | 1× `#c:dusts/cobalt` | -1..-1 | 200 | true | nonempty #c:ingots/cobalt; nonempty #c:dusts/cobalt |
| `crushing/cobalt_dust_from_ore` | `#c:ores/cobalt` | 2× `#c:dusts/cobalt` | -1..-1 | 200 | false | nonempty #c:ores/cobalt; nonempty #c:dusts/cobalt |
| `crushing/cobalt_dust_from_raw` | `#c:raw_materials/cobalt` | 2× `#c:dusts/cobalt` | -1..-1 | 200 | false | nonempty #c:raw_materials/cobalt; nonempty #c:dusts/cobalt |
| `crushing/cobalt_dust_from_raw_block` | `#c:storage_blocks/raw_cobalt` | 18× `#c:dusts/cobalt` | -1..-1 | 1600 | false | nonempty #c:storage_blocks/raw_cobalt; nonempty #c:dusts/cobalt |
| `crushing/constantan_dust_from_ingot` | `#c:ingots/constantan` | 1× `#c:dusts/constantan` | -1..-1 | 200 | true | nonempty #c:ingots/constantan; nonempty #c:dusts/constantan |
| `crushing/copper_dirty_dust_from_clump` | `#c:clumps/copper` | 2× `#c:dirty_dusts/copper` | -1..-1 | 200 | false | nonempty #c:clumps/copper; nonempty #c:dirty_dusts/copper |
| `crushing/copper_dust_from_ingot` | `#c:ingots/copper` | 1× `#c:dusts/copper` | -1..-1 | 200 | true | nonempty #c:ingots/copper; nonempty #c:dusts/copper |
| `crushing/copper_dust_from_ore` | `#c:ores/copper` | 2× `#c:dusts/copper` | -1..-1 | 200 | false | nonempty #c:ores/copper; nonempty #c:dusts/copper |
| `crushing/copper_dust_from_raw` | `#c:raw_materials/copper` | 2× `#c:dusts/copper` | -1..-1 | 200 | false | nonempty #c:raw_materials/copper; nonempty #c:dusts/copper |
| `crushing/copper_dust_from_raw_block` | `#c:storage_blocks/raw_copper` | 18× `#c:dusts/copper` | -1..-1 | 1600 | false | nonempty #c:storage_blocks/raw_copper; nonempty #c:dusts/copper |
| `crushing/crimson_iron_dirty_dust_from_clump` | `#c:clumps/crimson_iron` | 2× `#c:dirty_dusts/crimson_iron` | -1..-1 | 200 | false | nonempty #c:clumps/crimson_iron; nonempty #c:dirty_dusts/crimson_iron |
| `crushing/crimson_iron_dust_from_ingot` | `#c:ingots/crimson_iron` | 1× `#c:dusts/crimson_iron` | -1..-1 | 200 | true | nonempty #c:ingots/crimson_iron; nonempty #c:dusts/crimson_iron |
| `crushing/crimson_iron_dust_from_ore` | `#c:ores/crimson_iron` | 2× `#c:dusts/crimson_iron` | -1..-1 | 200 | false | nonempty #c:ores/crimson_iron; nonempty #c:dusts/crimson_iron |
| `crushing/crimson_iron_dust_from_raw` | `#c:raw_materials/crimson_iron` | 2× `#c:dusts/crimson_iron` | -1..-1 | 200 | false | nonempty #c:raw_materials/crimson_iron; nonempty #c:dusts/crimson_iron |
| `crushing/crimson_iron_dust_from_raw_block` | `#c:storage_blocks/raw_crimson_iron` | 18× `#c:dusts/crimson_iron` | -1..-1 | 1600 | false | nonempty #c:storage_blocks/raw_crimson_iron; nonempty #c:dusts/crimson_iron |
| `crushing/crimson_steel_dust_from_ingot` | `#c:ingots/crimson_steel` | 1× `#c:dusts/crimson_steel` | -1..-1 | 200 | true | nonempty #c:ingots/crimson_steel; nonempty #c:dusts/crimson_steel |
| `crushing/cupronickel_dust_from_ingot` | `#c:ingots/cupronickel` | 1× `#c:dusts/cupronickel` | -1..-1 | 200 | true | nonempty #c:ingots/cupronickel; nonempty #c:dusts/cupronickel |
| `crushing/dark_gem_dust_from_gem` | `#c:gems/dark_gem` | 1× `#c:dusts/dark_gem` | -1..-1 | 200 | true | nonempty #c:gems/dark_gem; nonempty #c:dusts/dark_gem |
| `crushing/dark_gem_dust_from_ore` | `#c:ores/dark_gem` | 3× `#c:dusts/dark_gem` | -1..-1 | 200 | false | nonempty #c:ores/dark_gem; nonempty #c:dusts/dark_gem |
| `crushing/datura` | `#c:crops/datura` | 2× `#c:seeds/datura` | -1..-1 | 200 | false | nonempty #c:crops/datura; nonempty #c:seeds/datura |
| `crushing/diamond_dust_from_gem` | `#c:gems/diamond` | 1× `#c:dusts/diamond` | -1..-1 | 200 | true | nonempty #c:gems/diamond; nonempty #c:dusts/diamond |
| `crushing/diamond_dust_from_ore` | `#c:ores/diamond` | 3× `#c:dusts/diamond` | -1..-1 | 200 | false | nonempty #c:ores/diamond; nonempty #c:dusts/diamond |
| `crushing/duratium_dust_from_ingot` | `#c:ingots/duratium` | 1× `#c:dusts/duratium` | -1..-1 | 200 | true | nonempty #c:ingots/duratium; nonempty #c:dusts/duratium |
| `crushing/echo_dust` | `minecraft:echo_shard` | 1× `#c:dusts/echo` | 4..-1 | 200 | true | none |
| `crushing/electrum_dust_from_ingot` | `#c:ingots/electrum` | 1× `#c:dusts/electrum` | -1..-1 | 200 | true | nonempty #c:ingots/electrum; nonempty #c:dusts/electrum |
| `crushing/emerald_dust_from_gem` | `#c:gems/emerald` | 1× `#c:dusts/emerald` | -1..-1 | 200 | true | nonempty #c:gems/emerald; nonempty #c:dusts/emerald |
| `crushing/emerald_dust_from_ore` | `#c:ores/emerald` | 3× `#c:dusts/emerald` | -1..-1 | 200 | false | nonempty #c:ores/emerald; nonempty #c:dusts/emerald |
| `crushing/end_stone_dust` | `#c:end_stones` | 1× `#c:dusts/end_stone` | -1..-1 | 200 | true | none |
| `crushing/ender_pearl_dust_from_tag` | `#c:ender_pearls` | 1× `#c:dusts/ender_pearl` | -1..-1 | 200 | true | nonempty #c:ender_pearls; nonempty #c:dusts/ender_pearl |
| `crushing/enderium_dust_from_ingot` | `#c:ingots/enderium` | 1× `#c:dusts/enderium` | -1..-1 | 200 | true | nonempty #c:ingots/enderium; nonempty #c:dusts/enderium |
| `crushing/energite_dust_from_ingot` | `#c:ingots/energite` | 1× `#c:dusts/energite` | -1..-1 | 200 | true | nonempty #c:ingots/energite; nonempty #c:dusts/energite |
| `crushing/entro_dust_from_gem` | `#c:gems/entro` | 1× `#c:dusts/entro` | -1..-1 | 200 | true | nonempty #c:gems/entro; nonempty #c:dusts/entro |
| `crushing/fluix_dust_from_gem` | `#c:gems/fluix` | 1× `#c:dusts/fluix` | -1..-1 | 200 | true | nonempty #c:gems/fluix; nonempty #c:dusts/fluix |
| `crushing/fluorite_dust_from_gem` | `#c:gems/fluorite` | 1× `#c:dusts/fluorite` | -1..-1 | 200 | true | nonempty #c:gems/fluorite; nonempty #c:dusts/fluorite |
| `crushing/fluorite_dust_from_ore` | `#c:ores/fluorite` | 6× `#c:dusts/fluorite` | -1..-1 | 200 | false | nonempty #c:ores/fluorite; nonempty #c:dusts/fluorite |
| `crushing/gold_dirty_dust_from_clump` | `#c:clumps/gold` | 2× `#c:dirty_dusts/gold` | -1..-1 | 200 | false | nonempty #c:clumps/gold; nonempty #c:dirty_dusts/gold |
| `crushing/gold_dust_from_ingot` | `#c:ingots/gold` | 1× `#c:dusts/gold` | -1..-1 | 200 | true | nonempty #c:ingots/gold; nonempty #c:dusts/gold |
| `crushing/gold_dust_from_ore` | `#c:ores/gold` | 2× `#c:dusts/gold` | -1..-1 | 200 | false | nonempty #c:ores/gold; nonempty #c:dusts/gold |
| `crushing/gold_dust_from_raw` | `#c:raw_materials/gold` | 2× `#c:dusts/gold` | -1..-1 | 200 | false | nonempty #c:raw_materials/gold; nonempty #c:dusts/gold |
| `crushing/gold_dust_from_raw_block` | `#c:storage_blocks/raw_gold` | 18× `#c:dusts/gold` | -1..-1 | 1600 | false | nonempty #c:storage_blocks/raw_gold; nonempty #c:dusts/gold |
| `crushing/graphite_dirty_dust_from_clump` | `#c:clumps/graphite` | 2× `#c:dirty_dusts/graphite` | -1..-1 | 200 | false | nonempty #c:clumps/graphite; nonempty #c:dirty_dusts/graphite |
| `crushing/graphite_dust_from_ingot` | `#c:ingots/graphite` | 1× `#c:dusts/graphite` | -1..-1 | 200 | true | nonempty #c:ingots/graphite; nonempty #c:dusts/graphite |
| `crushing/graphite_dust_from_ore` | `#c:ores/graphite` | 2× `#c:dusts/graphite` | -1..-1 | 200 | false | nonempty #c:ores/graphite; nonempty #c:dusts/graphite |
| `crushing/graphite_dust_from_raw` | `#c:raw_materials/graphite` | 2× `#c:dusts/graphite` | -1..-1 | 200 | false | nonempty #c:raw_materials/graphite; nonempty #c:dusts/graphite |
| `crushing/graphite_dust_from_raw_block` | `#c:storage_blocks/raw_graphite` | 18× `#c:dusts/graphite` | -1..-1 | 1600 | false | nonempty #c:storage_blocks/raw_graphite; nonempty #c:dusts/graphite |
| `crushing/he_mox_dust_from_ingot` | `#c:ingots/he_mox` | 1× `#c:dusts/he_mox` | -1..-1 | 200 | true | nonempty #c:ingots/he_mox; nonempty #c:dusts/he_mox |
| `crushing/he_uranium_dust_from_ingot` | `#c:ingots/he_uranium` | 1× `#c:dusts/he_uranium` | -1..-1 | 200 | true | nonempty #c:ingots/he_uranium; nonempty #c:dusts/he_uranium |
| `crushing/hop_graphite_dust_from_ingot` | `#c:ingots/hop_graphite` | 1× `#c:dusts/hop_graphite` | -1..-1 | 200 | true | nonempty #c:ingots/hop_graphite; nonempty #c:dusts/hop_graphite |
| `crushing/ice_dust` | `minecraft:ice` | 1× `#c:dusts/ice` | 2..-1 | 200 | true | none |
| `crushing/iesnium_dirty_dust_from_clump` | `#c:clumps/iesnium` | 2× `#c:dirty_dusts/iesnium` | -1..-1 | 200 | false | nonempty #c:clumps/iesnium; nonempty #c:dirty_dusts/iesnium |
| `crushing/iesnium_dust_from_ingot` | `#c:ingots/iesnium` | 1× `#c:dusts/iesnium` | -1..-1 | 200 | true | nonempty #c:ingots/iesnium; nonempty #c:dusts/iesnium |
| `crushing/iesnium_dust_from_ore` | `#c:ores/iesnium` | 2× `#c:dusts/iesnium` | -1..-1 | 200 | false | nonempty #c:ores/iesnium; nonempty #c:dusts/iesnium |
| `crushing/iesnium_dust_from_raw` | `#c:raw_materials/iesnium` | 2× `#c:dusts/iesnium` | -1..-1 | 200 | false | nonempty #c:raw_materials/iesnium; nonempty #c:dusts/iesnium |
| `crushing/iesnium_dust_from_raw_block` | `#c:storage_blocks/raw_iesnium` | 18× `#c:dusts/iesnium` | -1..-1 | 1600 | false | nonempty #c:storage_blocks/raw_iesnium; nonempty #c:dusts/iesnium |
| `crushing/invar_dust_from_ingot` | `#c:ingots/invar` | 1× `#c:dusts/invar` | -1..-1 | 200 | true | nonempty #c:ingots/invar; nonempty #c:dusts/invar |
| `crushing/iridium_dirty_dust_from_clump` | `#c:clumps/iridium` | 2× `#c:dirty_dusts/iridium` | -1..-1 | 200 | false | nonempty #c:clumps/iridium; nonempty #c:dirty_dusts/iridium |
| `crushing/iridium_dust_from_ingot` | `#c:ingots/iridium` | 1× `#c:dusts/iridium` | -1..-1 | 200 | true | nonempty #c:ingots/iridium; nonempty #c:dusts/iridium |
| `crushing/iridium_dust_from_ore` | `#c:ores/iridium` | 2× `#c:dusts/iridium` | -1..-1 | 200 | false | nonempty #c:ores/iridium; nonempty #c:dusts/iridium |
| `crushing/iridium_dust_from_raw` | `#c:raw_materials/iridium` | 2× `#c:dusts/iridium` | -1..-1 | 200 | false | nonempty #c:raw_materials/iridium; nonempty #c:dusts/iridium |
| `crushing/iridium_dust_from_raw_block` | `#c:storage_blocks/raw_iridium` | 18× `#c:dusts/iridium` | -1..-1 | 1600 | false | nonempty #c:storage_blocks/raw_iridium; nonempty #c:dusts/iridium |
| `crushing/iron_dirty_dust_from_clump` | `#c:clumps/iron` | 2× `#c:dirty_dusts/iron` | -1..-1 | 200 | false | nonempty #c:clumps/iron; nonempty #c:dirty_dusts/iron |
| `crushing/iron_dust_from_ingot` | `#c:ingots/iron` | 1× `#c:dusts/iron` | -1..-1 | 200 | true | nonempty #c:ingots/iron; nonempty #c:dusts/iron |
| `crushing/iron_dust_from_ore` | `#c:ores/iron` | 2× `#c:dusts/iron` | -1..-1 | 200 | false | nonempty #c:ores/iron; nonempty #c:dusts/iron |
| `crushing/iron_dust_from_raw` | `#c:raw_materials/iron` | 2× `#c:dusts/iron` | -1..-1 | 200 | false | nonempty #c:raw_materials/iron; nonempty #c:dusts/iron |
| `crushing/iron_dust_from_raw_block` | `#c:storage_blocks/raw_iron` | 18× `#c:dusts/iron` | -1..-1 | 1600 | false | nonempty #c:storage_blocks/raw_iron; nonempty #c:dusts/iron |
| `crushing/kanthal_dust_from_ingot` | `#c:ingots/kanthal` | 1× `#c:dusts/kanthal` | -1..-1 | 200 | true | nonempty #c:ingots/kanthal; nonempty #c:dusts/kanthal |
| `crushing/lapis_dust_from_gem` | `#c:gems/lapis` | 1× `#c:dusts/lapis` | -1..-1 | 200 | true | nonempty #c:gems/lapis; nonempty #c:dusts/lapis |
| `crushing/lapis_dust_from_ore` | `#c:ores/lapis` | 9× `#c:dusts/lapis` | -1..-1 | 200 | false | nonempty #c:ores/lapis; nonempty #c:dusts/lapis |
| `crushing/le_mox_dust_from_ingot` | `#c:ingots/le_mox` | 1× `#c:dusts/le_mox` | -1..-1 | 200 | true | nonempty #c:ingots/le_mox; nonempty #c:dusts/le_mox |
| `crushing/le_uranium_dust_from_ingot` | `#c:ingots/le_uranium` | 1× `#c:dusts/le_uranium` | -1..-1 | 200 | true | nonempty #c:ingots/le_uranium; nonempty #c:dusts/le_uranium |
| `crushing/lead_dirty_dust_from_clump` | `#c:clumps/lead` | 2× `#c:dirty_dusts/lead` | -1..-1 | 200 | false | nonempty #c:clumps/lead; nonempty #c:dirty_dusts/lead |
| `crushing/lead_dust_from_ingot` | `#c:ingots/lead` | 1× `#c:dusts/lead` | -1..-1 | 200 | true | nonempty #c:ingots/lead; nonempty #c:dusts/lead |
| `crushing/lead_dust_from_ore` | `#c:ores/lead` | 2× `#c:dusts/lead` | -1..-1 | 200 | false | nonempty #c:ores/lead; nonempty #c:dusts/lead |
| `crushing/lead_dust_from_raw` | `#c:raw_materials/lead` | 2× `#c:dusts/lead` | -1..-1 | 200 | false | nonempty #c:raw_materials/lead; nonempty #c:dusts/lead |
| `crushing/lead_dust_from_raw_block` | `#c:storage_blocks/raw_lead` | 18× `#c:dusts/lead` | -1..-1 | 1600 | false | nonempty #c:storage_blocks/raw_lead; nonempty #c:dusts/lead |
| `crushing/lignite_coal_dust_from_ore` | `#c:ores/lignite_coal` | 2× `#c:dusts/lignite_coal` | -1..-1 | 200 | false | nonempty #c:ores/lignite_coal; nonempty #c:dusts/lignite_coal |
| `crushing/lumium_dust_from_ingot` | `#c:ingots/lumium` | 1× `#c:dusts/lumium` | -1..-1 | 200 | true | nonempty #c:ingots/lumium; nonempty #c:dusts/lumium |
| `crushing/mithril_dirty_dust_from_clump` | `#c:clumps/mithril` | 2× `#c:dirty_dusts/mithril` | -1..-1 | 200 | false | nonempty #c:clumps/mithril; nonempty #c:dirty_dusts/mithril |
| `crushing/mithril_dust_from_ingot` | `#c:ingots/mithril` | 1× `#c:dusts/mithril` | -1..-1 | 200 | true | nonempty #c:ingots/mithril; nonempty #c:dusts/mithril |
| `crushing/mithril_dust_from_ore` | `#c:ores/mithril` | 2× `#c:dusts/mithril` | -1..-1 | 200 | false | nonempty #c:ores/mithril; nonempty #c:dusts/mithril |
| `crushing/mithril_dust_from_raw` | `#c:raw_materials/mithril` | 2× `#c:dusts/mithril` | -1..-1 | 200 | false | nonempty #c:raw_materials/mithril; nonempty #c:dusts/mithril |
| `crushing/mithril_dust_from_raw_block` | `#c:storage_blocks/raw_mithril` | 18× `#c:dusts/mithril` | -1..-1 | 1600 | false | nonempty #c:storage_blocks/raw_mithril; nonempty #c:dusts/mithril |
| `crushing/monazite_dust_from_ore` | `#c:ores/monazite` | 2× `#c:dusts/monazite` | -1..-1 | 200 | false | nonempty #c:ores/monazite; nonempty #c:dusts/monazite |
| `crushing/netherite_dust_from_ingot` | `#c:ingots/netherite` | 1× `#c:dusts/netherite` | -1..-1 | 200 | true | nonempty #c:ingots/netherite; nonempty #c:dusts/netherite |
| `crushing/netherite_scrap_dust_from_ore` | `#c:ores/netherite_scrap` | 2× `#c:dusts/netherite_scrap` | -1..-1 | 200 | false | nonempty #c:ores/netherite_scrap; nonempty #c:dusts/netherite_scrap |
| `crushing/netherite_scrap_dust_from_scrap` | `minecraft:netherite_scrap` | 1× `#c:dusts/netherite_scrap` | -1..-1 | 200 | true | nonempty #c:dusts/netherite_scrap |
| `crushing/nickel_dirty_dust_from_clump` | `#c:clumps/nickel` | 2× `#c:dirty_dusts/nickel` | -1..-1 | 200 | false | nonempty #c:clumps/nickel; nonempty #c:dirty_dusts/nickel |
| `crushing/nickel_dust_from_ingot` | `#c:ingots/nickel` | 1× `#c:dusts/nickel` | -1..-1 | 200 | true | nonempty #c:ingots/nickel; nonempty #c:dusts/nickel |
| `crushing/nickel_dust_from_ore` | `#c:ores/nickel` | 2× `#c:dusts/nickel` | -1..-1 | 200 | false | nonempty #c:ores/nickel; nonempty #c:dusts/nickel |
| `crushing/nickel_dust_from_raw` | `#c:raw_materials/nickel` | 2× `#c:dusts/nickel` | -1..-1 | 200 | false | nonempty #c:raw_materials/nickel; nonempty #c:dusts/nickel |
| `crushing/nickel_dust_from_raw_block` | `#c:storage_blocks/raw_nickel` | 18× `#c:dusts/nickel` | -1..-1 | 1600 | false | nonempty #c:storage_blocks/raw_nickel; nonempty #c:dusts/nickel |
| `crushing/obsidian_dust` | `#c:obsidians` | 1× `#c:dusts/obsidian` | -1..-1 | 200 | true | none |
| `crushing/osmium_dirty_dust_from_clump` | `#c:clumps/osmium` | 2× `#c:dirty_dusts/osmium` | -1..-1 | 200 | false | nonempty #c:clumps/osmium; nonempty #c:dirty_dusts/osmium |
| `crushing/osmium_dust_from_ingot` | `#c:ingots/osmium` | 1× `#c:dusts/osmium` | -1..-1 | 200 | true | nonempty #c:ingots/osmium; nonempty #c:dusts/osmium |
| `crushing/osmium_dust_from_ore` | `#c:ores/osmium` | 2× `#c:dusts/osmium` | -1..-1 | 200 | false | nonempty #c:ores/osmium; nonempty #c:dusts/osmium |
| `crushing/osmium_dust_from_raw` | `#c:raw_materials/osmium` | 2× `#c:dusts/osmium` | -1..-1 | 200 | false | nonempty #c:raw_materials/osmium; nonempty #c:dusts/osmium |
| `crushing/osmium_dust_from_raw_block` | `#c:storage_blocks/raw_osmium` | 18× `#c:dusts/osmium` | -1..-1 | 1600 | false | nonempty #c:storage_blocks/raw_osmium; nonempty #c:dusts/osmium |
| `crushing/othercobblerock` | `occultism:otherrock` | 1× `occultism:othercobblerock` | -1..-1 | 20 | true | none |
| `crushing/othercobblestone` | `#occultism:otherstone` | 1× `#occultism:othercobblestone` | -1..-1 | 20 | true | nonempty #occultism:otherstone; nonempty #occultism:othercobblestone |
| `crushing/packed_ice_dust` | `minecraft:packed_ice` | 1× `#c:dusts/packed_ice` | 2..-1 | 200 | true | none |
| `crushing/peridot_dust_from_gem` | `#c:gems/peridot` | 1× `#c:dusts/peridot` | -1..-1 | 200 | true | nonempty #c:gems/peridot; nonempty #c:dusts/peridot |
| `crushing/peridot_dust_from_ore` | `#c:ores/peridot` | 3× `#c:dusts/peridot` | -1..-1 | 200 | false | nonempty #c:ores/peridot; nonempty #c:dusts/peridot |
| `crushing/pewter_dirty_dust_from_clump` | `#c:clumps/pewter` | 2× `#c:dirty_dusts/pewter` | -1..-1 | 200 | false | nonempty #c:clumps/pewter; nonempty #c:dirty_dusts/pewter |
| `crushing/pewter_dust_from_ingot` | `#c:ingots/pewter` | 1× `#c:dusts/pewter` | -1..-1 | 200 | true | nonempty #c:ingots/pewter; nonempty #c:dusts/pewter |
| `crushing/pewter_dust_from_ore` | `#c:ores/pewter` | 2× `#c:dusts/pewter` | -1..-1 | 200 | false | nonempty #c:ores/pewter; nonempty #c:dusts/pewter |
| `crushing/pewter_dust_from_raw` | `#c:raw_materials/pewter` | 2× `#c:dusts/pewter` | -1..-1 | 200 | false | nonempty #c:raw_materials/pewter; nonempty #c:dusts/pewter |
| `crushing/pewter_dust_from_raw_block` | `#c:storage_blocks/raw_pewter` | 18× `#c:dusts/pewter` | -1..-1 | 1600 | false | nonempty #c:storage_blocks/raw_pewter; nonempty #c:dusts/pewter |
| `crushing/platinum_dirty_dust_from_clump` | `#c:clumps/platinum` | 2× `#c:dirty_dusts/platinum` | -1..-1 | 200 | false | nonempty #c:clumps/platinum; nonempty #c:dirty_dusts/platinum |
| `crushing/platinum_dust_from_ingot` | `#c:ingots/platinum` | 1× `#c:dusts/platinum` | -1..-1 | 200 | true | nonempty #c:ingots/platinum; nonempty #c:dusts/platinum |
| `crushing/platinum_dust_from_ore` | `#c:ores/platinum` | 2× `#c:dusts/platinum` | -1..-1 | 200 | false | nonempty #c:ores/platinum; nonempty #c:dusts/platinum |
| `crushing/platinum_dust_from_raw` | `#c:raw_materials/platinum` | 2× `#c:dusts/platinum` | -1..-1 | 200 | false | nonempty #c:raw_materials/platinum; nonempty #c:dusts/platinum |
| `crushing/platinum_dust_from_raw_block` | `#c:storage_blocks/raw_platinum` | 18× `#c:dusts/platinum` | -1..-1 | 1600 | false | nonempty #c:storage_blocks/raw_platinum; nonempty #c:dusts/platinum |
| `crushing/plutonium_dust_from_ingot` | `#c:ingots/plutonium` | 1× `#c:dusts/plutonium` | -1..-1 | 200 | true | nonempty #c:ingots/plutonium; nonempty #c:dusts/plutonium |
| `crushing/quartz_dust_from_gem` | `#c:gems/quartz` | 1× `#c:dusts/quartz` | -1..-1 | 200 | true | nonempty #c:gems/quartz; nonempty #c:dusts/quartz |
| `crushing/quartz_dust_from_ore` | `#c:ores/quartz` | 3× `#c:dusts/quartz` | -1..-1 | 200 | false | nonempty #c:ores/quartz; nonempty #c:dusts/quartz |
| `crushing/quicksilver_dirty_dust_from_clump` | `#c:clumps/quicksilver` | 2× `#c:dirty_dusts/quicksilver` | -1..-1 | 200 | false | nonempty #c:clumps/quicksilver; nonempty #c:dirty_dusts/quicksilver |
| `crushing/quicksilver_dust_from_ingot` | `#c:ingots/quicksilver` | 1× `#c:dusts/quicksilver` | -1..-1 | 200 | true | nonempty #c:ingots/quicksilver; nonempty #c:dusts/quicksilver |
| `crushing/quicksilver_dust_from_ore` | `#c:ores/quicksilver` | 2× `#c:dusts/quicksilver` | -1..-1 | 200 | false | nonempty #c:ores/quicksilver; nonempty #c:dusts/quicksilver |
| `crushing/quicksilver_dust_from_raw` | `#c:raw_materials/quicksilver` | 2× `#c:dusts/quicksilver` | -1..-1 | 200 | false | nonempty #c:raw_materials/quicksilver; nonempty #c:dusts/quicksilver |
| `crushing/quicksilver_dust_from_raw_block` | `#c:storage_blocks/raw_quicksilver` | 18× `#c:dusts/quicksilver` | -1..-1 | 1600 | false | nonempty #c:storage_blocks/raw_quicksilver; nonempty #c:dusts/quicksilver |
| `crushing/redstone_dust_from_ore` | `#c:ores/redstone` | 4× `#c:dusts/redstone` | -1..-1 | 200 | false | nonempty #c:ores/redstone; nonempty #c:dusts/redstone |
| `crushing/refined_obsidian_dust_from_ingot` | `#c:ingots/refined_obsidian` | 1× `#c:dusts/refined_obsidian` | -1..-1 | 200 | true | nonempty #c:ingots/refined_obsidian; nonempty #c:dusts/refined_obsidian |
| `crushing/ruby_dust_from_gem` | `#c:gems/ruby` | 1× `#c:dusts/ruby` | -1..-1 | 200 | true | nonempty #c:gems/ruby; nonempty #c:dusts/ruby |
| `crushing/ruby_dust_from_ore` | `#c:ores/ruby` | 3× `#c:dusts/ruby` | -1..-1 | 200 | false | nonempty #c:ores/ruby; nonempty #c:dusts/ruby |
| `crushing/salt_dust_from_ore` | `#c:ores/salt` | 2× `#c:dusts/salt` | -1..-1 | 200 | false | nonempty #c:ores/salt; nonempty #c:dusts/salt |
| `crushing/saltpeter_dust_from_ore` | `#c:ores/saltpeter` | 2× `#c:dusts/saltpeter` | -1..-1 | 200 | false | nonempty #c:ores/saltpeter; nonempty #c:dusts/saltpeter |
| `crushing/sapphire_dust_from_gem` | `#c:gems/sapphire` | 1× `#c:dusts/sapphire` | -1..-1 | 200 | true | nonempty #c:gems/sapphire; nonempty #c:dusts/sapphire |
| `crushing/sapphire_dust_from_ore` | `#c:ores/sapphire` | 3× `#c:dusts/sapphire` | -1..-1 | 200 | false | nonempty #c:ores/sapphire; nonempty #c:dusts/sapphire |
| `crushing/signalum_dust_from_ingot` | `#c:ingots/signalum` | 1× `#c:dusts/signalum` | -1..-1 | 200 | true | nonempty #c:ingots/signalum; nonempty #c:dusts/signalum |
| `crushing/silicon_dust_from_ingot` | `#c:ingots/silicon` | 1× `#c:dusts/silicon` | -1..-1 | 200 | true | nonempty #c:ingots/silicon; nonempty #c:dusts/silicon |
| `crushing/silver_dirty_dust_from_clump` | `#c:clumps/silver` | 2× `#c:dirty_dusts/silver` | -1..-1 | 200 | false | nonempty #c:clumps/silver; nonempty #c:dirty_dusts/silver |
| `crushing/silver_dust_from_ingot` | `#c:ingots/silver` | 1× `#c:dusts/silver` | -1..-1 | 200 | true | nonempty #c:ingots/silver; nonempty #c:dusts/silver |
| `crushing/silver_dust_from_ore` | `#c:ores/silver` | 2× `#c:dusts/silver` | -1..-1 | 200 | false | nonempty #c:ores/silver; nonempty #c:dusts/silver |
| `crushing/silver_dust_from_raw` | `#c:raw_materials/silver` | 2× `#c:dusts/silver` | -1..-1 | 200 | false | nonempty #c:raw_materials/silver; nonempty #c:dusts/silver |
| `crushing/silver_dust_from_raw_block` | `#c:storage_blocks/raw_silver` | 18× `#c:dusts/silver` | -1..-1 | 1600 | false | nonempty #c:storage_blocks/raw_silver; nonempty #c:dusts/silver |
| `crushing/sky_stone_dust` | `#c:sky_stones` | 1× `#c:dusts/sky_stone` | -1..-1 | 200 | true | nonempty #c:sky_stones; nonempty #c:dusts/sky_stone |
| `crushing/steel_dust_from_ingot` | `#c:ingots/steel` | 1× `#c:dusts/steel` | -1..-1 | 200 | true | nonempty #c:ingots/steel; nonempty #c:dusts/steel |
| `crushing/sulfur_dust_from_gem` | `#c:gems/sulfur` | 1× `#c:dusts/sulfur` | -1..-1 | 200 | true | nonempty #c:gems/sulfur; nonempty #c:dusts/sulfur |
| `crushing/sulfur_dust_from_ore` | `#c:ores/sulfur` | 3× `#c:dusts/sulfur` | -1..-1 | 200 | false | nonempty #c:ores/sulfur; nonempty #c:dusts/sulfur |
| `crushing/superconductor_dust_from_ingot` | `#c:ingots/superconductor` | 1× `#c:dusts/superconductor` | -1..-1 | 200 | true | nonempty #c:ingots/superconductor; nonempty #c:dusts/superconductor |
| `crushing/tin_dirty_dust_from_clump` | `#c:clumps/tin` | 2× `#c:dirty_dusts/tin` | -1..-1 | 200 | false | nonempty #c:clumps/tin; nonempty #c:dirty_dusts/tin |
| `crushing/tin_dust_from_ingot` | `#c:ingots/tin` | 1× `#c:dusts/tin` | -1..-1 | 200 | true | nonempty #c:ingots/tin; nonempty #c:dusts/tin |
| `crushing/tin_dust_from_ore` | `#c:ores/tin` | 2× `#c:dusts/tin` | -1..-1 | 200 | false | nonempty #c:ores/tin; nonempty #c:dusts/tin |
| `crushing/tin_dust_from_raw` | `#c:raw_materials/tin` | 2× `#c:dusts/tin` | -1..-1 | 200 | false | nonempty #c:raw_materials/tin; nonempty #c:dusts/tin |
| `crushing/tin_dust_from_raw_block` | `#c:storage_blocks/raw_tin` | 18× `#c:dusts/tin` | -1..-1 | 1600 | false | nonempty #c:storage_blocks/raw_tin; nonempty #c:dusts/tin |
| `crushing/titanium_dirty_dust_from_clump` | `#c:clumps/titanium` | 2× `#c:dirty_dusts/titanium` | -1..-1 | 200 | false | nonempty #c:clumps/titanium; nonempty #c:dirty_dusts/titanium |
| `crushing/titanium_dust_from_ingot` | `#c:ingots/titanium` | 1× `#c:dusts/titanium` | -1..-1 | 200 | true | nonempty #c:ingots/titanium; nonempty #c:dusts/titanium |
| `crushing/titanium_dust_from_ore` | `#c:ores/titanium` | 2× `#c:dusts/titanium` | -1..-1 | 200 | false | nonempty #c:ores/titanium; nonempty #c:dusts/titanium |
| `crushing/titanium_dust_from_raw` | `#c:raw_materials/titanium` | 2× `#c:dusts/titanium` | -1..-1 | 200 | false | nonempty #c:raw_materials/titanium; nonempty #c:dusts/titanium |
| `crushing/titanium_dust_from_raw_block` | `#c:storage_blocks/raw_titanium` | 18× `#c:dusts/titanium` | -1..-1 | 1600 | false | nonempty #c:storage_blocks/raw_titanium; nonempty #c:dusts/titanium |
| `crushing/topaz_dust_from_gem` | `#c:gems/topaz` | 1× `#c:dusts/topaz` | -1..-1 | 200 | true | nonempty #c:gems/topaz; nonempty #c:dusts/topaz |
| `crushing/topaz_dust_from_ore` | `#c:ores/topaz` | 3× `#c:dusts/topaz` | -1..-1 | 200 | false | nonempty #c:ores/topaz; nonempty #c:dusts/topaz |
| `crushing/tungsten_dirty_dust_from_clump` | `#c:clumps/tungsten` | 2× `#c:dirty_dusts/tungsten` | -1..-1 | 200 | false | nonempty #c:clumps/tungsten; nonempty #c:dirty_dusts/tungsten |
| `crushing/tungsten_dust_from_ingot` | `#c:ingots/tungsten` | 1× `#c:dusts/tungsten` | -1..-1 | 200 | true | nonempty #c:ingots/tungsten; nonempty #c:dusts/tungsten |
| `crushing/tungsten_dust_from_ore` | `#c:ores/tungsten` | 2× `#c:dusts/tungsten` | -1..-1 | 200 | false | nonempty #c:ores/tungsten; nonempty #c:dusts/tungsten |
| `crushing/tungsten_dust_from_raw` | `#c:raw_materials/tungsten` | 2× `#c:dusts/tungsten` | -1..-1 | 200 | false | nonempty #c:raw_materials/tungsten; nonempty #c:dusts/tungsten |
| `crushing/tungsten_dust_from_raw_block` | `#c:storage_blocks/raw_tungsten` | 18× `#c:dusts/tungsten` | -1..-1 | 1600 | false | nonempty #c:storage_blocks/raw_tungsten; nonempty #c:dusts/tungsten |
| `crushing/tyrian_steel_dust_from_ingot` | `#c:ingots/tyrian_steel` | 1× `#c:dusts/tyrian_steel` | -1..-1 | 200 | true | nonempty #c:ingots/tyrian_steel; nonempty #c:dusts/tyrian_steel |
| `crushing/unobtainium_allthemodium_alloy_dust_from_ingot` | `#c:ingots/unobtainium_allthemodium_alloy` | 1× `#c:dusts/unobtainium_allthemodium_alloy` | -1..-1 | 200 | true | nonempty #c:ingots/unobtainium_allthemodium_alloy; nonempty #c:dusts/unobtainium_allthemodium_alloy |
| `crushing/unobtainium_dirty_dust_from_clump` | `#c:clumps/unobtainium` | 2× `#c:dirty_dusts/unobtainium` | -1..-1 | 200 | false | nonempty #c:clumps/unobtainium; nonempty #c:dirty_dusts/unobtainium |
| `crushing/unobtainium_dust_from_ingot` | `#c:ingots/unobtainium` | 1× `#c:dusts/unobtainium` | -1..-1 | 200 | true | nonempty #c:ingots/unobtainium; nonempty #c:dusts/unobtainium |
| `crushing/unobtainium_dust_from_ore` | `#c:ores/unobtainium` | 2× `#c:dusts/unobtainium` | -1..-1 | 200 | false | nonempty #c:ores/unobtainium; nonempty #c:dusts/unobtainium |
| `crushing/unobtainium_dust_from_raw` | `#c:raw_materials/unobtainium` | 2× `#c:dusts/unobtainium` | -1..-1 | 200 | false | nonempty #c:raw_materials/unobtainium; nonempty #c:dusts/unobtainium |
| `crushing/unobtainium_dust_from_raw_block` | `#c:storage_blocks/raw_unobtainium` | 18× `#c:dusts/unobtainium` | -1..-1 | 1600 | false | nonempty #c:storage_blocks/raw_unobtainium; nonempty #c:dusts/unobtainium |
| `crushing/unobtainium_vibranium_alloy_dust_from_ingot` | `#c:ingots/unobtainium_vibranium_alloy` | 1× `#c:dusts/unobtainium_vibranium_alloy` | -1..-1 | 200 | true | nonempty #c:ingots/unobtainium_vibranium_alloy; nonempty #c:dusts/unobtainium_vibranium_alloy |
| `crushing/uranium_235_dust_from_ingot` | `#c:ingots/uranium_235` | 1× `#c:dusts/uranium_235` | -1..-1 | 200 | true | nonempty #c:ingots/uranium_235; nonempty #c:dusts/uranium_235 |
| `crushing/uranium_238_dust_from_ingot` | `#c:ingots/uranium_238` | 1× `#c:dusts/uranium_238` | -1..-1 | 200 | true | nonempty #c:ingots/uranium_238; nonempty #c:dusts/uranium_238 |
| `crushing/uranium_dirty_dust_from_clump` | `#c:clumps/uranium` | 2× `#c:dirty_dusts/uranium` | -1..-1 | 200 | false | nonempty #c:clumps/uranium; nonempty #c:dirty_dusts/uranium |
| `crushing/uranium_dust_from_ingot` | `#c:ingots/uranium` | 1× `#c:dusts/uranium` | -1..-1 | 200 | true | nonempty #c:ingots/uranium; nonempty #c:dusts/uranium |
| `crushing/uranium_dust_from_ore` | `#c:ores/uranium` | 2× `#c:dusts/uranium` | -1..-1 | 200 | false | nonempty #c:ores/uranium; nonempty #c:dusts/uranium |
| `crushing/uranium_dust_from_raw` | `#c:raw_materials/uranium` | 2× `#c:dusts/uranium` | -1..-1 | 200 | false | nonempty #c:raw_materials/uranium; nonempty #c:dusts/uranium |
| `crushing/uranium_dust_from_raw_block` | `#c:storage_blocks/raw_uranium` | 18× `#c:dusts/uranium` | -1..-1 | 1600 | false | nonempty #c:storage_blocks/raw_uranium; nonempty #c:dusts/uranium |
| `crushing/vibranium_allthemodium_alloy_dust_from_ingot` | `#c:ingots/vibranium_allthemodium_alloy` | 1× `#c:dusts/vibranium_allthemodium_alloy` | -1..-1 | 200 | true | nonempty #c:ingots/vibranium_allthemodium_alloy; nonempty #c:dusts/vibranium_allthemodium_alloy |
| `crushing/vibranium_dirty_dust_from_clump` | `#c:clumps/vibranium` | 2× `#c:dirty_dusts/vibranium` | -1..-1 | 200 | false | nonempty #c:clumps/vibranium; nonempty #c:dirty_dusts/vibranium |
| `crushing/vibranium_dust_from_ingot` | `#c:ingots/vibranium` | 1× `#c:dusts/vibranium` | -1..-1 | 200 | true | nonempty #c:ingots/vibranium; nonempty #c:dusts/vibranium |
| `crushing/vibranium_dust_from_ore` | `#c:ores/vibranium` | 2× `#c:dusts/vibranium` | -1..-1 | 200 | false | nonempty #c:ores/vibranium; nonempty #c:dusts/vibranium |
| `crushing/vibranium_dust_from_raw` | `#c:raw_materials/vibranium` | 2× `#c:dusts/vibranium` | -1..-1 | 200 | false | nonempty #c:raw_materials/vibranium; nonempty #c:dusts/vibranium |
| `crushing/vibranium_dust_from_raw_block` | `#c:storage_blocks/raw_vibranium` | 18× `#c:dusts/vibranium` | -1..-1 | 1600 | false | nonempty #c:storage_blocks/raw_vibranium; nonempty #c:dusts/vibranium |
| `crushing/wind_charge_from_rod` | `#c:rods/breeze` | 4× `minecraft:wind_charge` | -1..-1 | 200 | false | none |
| `crushing/zinc_dirty_dust_from_clump` | `#c:clumps/zinc` | 2× `#c:dirty_dusts/zinc` | -1..-1 | 200 | false | nonempty #c:clumps/zinc; nonempty #c:dirty_dusts/zinc |
| `crushing/zinc_dust_from_ingot` | `#c:ingots/zinc` | 1× `#c:dusts/zinc` | -1..-1 | 200 | true | nonempty #c:ingots/zinc; nonempty #c:dusts/zinc |
| `crushing/zinc_dust_from_ore` | `#c:ores/zinc` | 2× `#c:dusts/zinc` | -1..-1 | 200 | false | nonempty #c:ores/zinc; nonempty #c:dusts/zinc |
| `crushing/zinc_dust_from_raw` | `#c:raw_materials/zinc` | 2× `#c:dusts/zinc` | -1..-1 | 200 | false | nonempty #c:raw_materials/zinc; nonempty #c:dusts/zinc |
| `crushing/zinc_dust_from_raw_block` | `#c:storage_blocks/raw_zinc` | 18× `#c:dusts/zinc` | -1..-1 | 1600 | false | nonempty #c:storage_blocks/raw_zinc; nonempty #c:dusts/zinc |

## Miner194 records

| Recipe (`occultism:`) | Miner selector (not consumed material) | Base selected output | Relative weight | Load conditions |
| --- | --- | --- | ---: | --- |
| `miner/basic_resources/andesite` | `#occultism:miners/basic_resources` | 1× `minecraft:andesite` | 7000 | none |
| `miner/basic_resources/basalt` | `#occultism:miners/basic_resources` | 1× `minecraft:basalt` | 1000 | none |
| `miner/basic_resources/blackstone` | `#occultism:miners/basic_resources` | 1× `minecraft:blackstone` | 1000 | none |
| `miner/basic_resources/deepslate` | `#occultism:miners/basic_resources` | 1× `minecraft:deepslate` | 5000 | none |
| `miner/basic_resources/diorite` | `#occultism:miners/basic_resources` | 1× `minecraft:diorite` | 7000 | none |
| `miner/basic_resources/end_stone` | `#occultism:miners/basic_resources` | 1× `minecraft:end_stone` | 30 | none |
| `miner/basic_resources/granite` | `#occultism:miners/basic_resources` | 1× `minecraft:granite` | 7000 | none |
| `miner/basic_resources/mossy_cobblestone` | `#occultism:miners/basic_resources` | 1× `minecraft:mossy_cobblestone` | 3000 | none |
| `miner/basic_resources/mossy_stone_bricks` | `#occultism:miners/basic_resources` | 1× `minecraft:mossy_stone_bricks` | 3000 | none |
| `miner/basic_resources/netherrack` | `#occultism:miners/basic_resources` | 1× `minecraft:netherrack` | 1000 | none |
| `miner/basic_resources/stone` | `#occultism:miners/basic_resources` | 1× `minecraft:stone` | 10000 | none |
| `miner/debug_wand` | `occultism:debug_wand` | 1× `occultism:otherstone` | 200 | none |
| `miner/debug_wand_2` | `occultism:debug_wand` | 1× `occultism:otherrock` | 200 | none |
| `miner/deeps/deepslate_coal_ore` | `#occultism:miners/deeps` | 1× `minecraft:deepslate_coal_ore` | 1000 | none |
| `miner/deeps/deepslate_copper_ore` | `#occultism:miners/deeps` | 1× `minecraft:deepslate_copper_ore` | 584 | none |
| `miner/deeps/deepslate_diamond_ore` | `#occultism:miners/deeps` | 1× `minecraft:deepslate_diamond_ore` | 218 | none |
| `miner/deeps/deepslate_emerald_ore` | `#occultism:miners/deeps` | 1× `minecraft:deepslate_emerald_ore` | 156 | none |
| `miner/deeps/deepslate_gold_ore` | `#occultism:miners/deeps` | 1× `minecraft:deepslate_gold_ore` | 311 | none |
| `miner/deeps/deepslate_iron_ore` | `#occultism:miners/deeps` | 1× `minecraft:deepslate_iron_ore` | 750 | none |
| `miner/deeps/deepslate_lapis_ore` | `#occultism:miners/deeps` | 1× `minecraft:deepslate_lapis_ore` | 343 | none |
| `miner/deeps/deepslate_redstone_ore` | `#occultism:miners/deeps` | 1× `minecraft:deepslate_redstone_ore` | 515 | none |
| `miner/deeps/deepslate_silver_ore` | `#occultism:miners/deeps` | 1× `occultism:silver_ore_deepslate` | 381 | none |
| `miner/eldritch/amethyst` | `#occultism:miners/eldritch` | 9× `#c:gems/amethyst` | 90 | nonempty #occultism:miners/eldritch; nonempty #c:gems/amethyst |
| `miner/eldritch/ancient_debris` | `#occultism:miners/eldritch` | 9× `#c:ores/netherite_scrap` | 90 | none |
| `miner/eldritch/arcane_crystal` | `#occultism:miners/eldritch` | 1× `#c:storage_blocks/arcane_crystal` | 90 | nonempty #occultism:miners/eldritch; nonempty #c:storage_blocks/arcane_crystal |
| `miner/eldritch/bauxite` | `#occultism:miners/eldritch` | 1× `#c:storage_blocks/bauxite` | 90 | nonempty #occultism:miners/eldritch; nonempty #c:storage_blocks/bauxite |
| `miner/eldritch/black_quartz` | `#occultism:miners/eldritch` | 9× `#c:gems/black_quartz` | 90 | nonempty #occultism:miners/eldritch; nonempty #c:gems/black_quartz |
| `miner/eldritch/clay` | `#occultism:miners/eldritch` | 9× `#c:clay` | 90 | none |
| `miner/eldritch/coal` | `#occultism:miners/eldritch` | 1× `#c:storage_blocks/coal` | 90 | nonempty #occultism:miners/eldritch; nonempty #c:storage_blocks/coal |
| `miner/eldritch/dark_gem` | `#occultism:miners/eldritch` | 1× `#c:storage_blocks/dark_gem` | 90 | nonempty #occultism:miners/eldritch; nonempty #c:storage_blocks/dark_gem |
| `miner/eldritch/diamond` | `#occultism:miners/eldritch` | 1× `#c:storage_blocks/diamond` | 90 | nonempty #occultism:miners/eldritch; nonempty #c:storage_blocks/diamond |
| `miner/eldritch/emerald` | `#occultism:miners/eldritch` | 1× `#c:storage_blocks/emerald` | 90 | nonempty #occultism:miners/eldritch; nonempty #c:storage_blocks/emerald |
| `miner/eldritch/fluorite` | `#occultism:miners/eldritch` | 1× `#c:storage_blocks/fluorite` | 90 | nonempty #occultism:miners/eldritch; nonempty #c:storage_blocks/fluorite |
| `miner/eldritch/glowstone_dust` | `#occultism:miners/eldritch` | 9× `#c:dusts/glowstone` | 90 | none |
| `miner/eldritch/inferium_essence` | `#occultism:miners/eldritch` | 1× `#c:storage_blocks/inferium_essence` | 90 | nonempty #occultism:miners/eldritch; nonempty #c:storage_blocks/inferium_essence |
| `miner/eldritch/jade` | `#occultism:miners/eldritch` | 1× `#c:storage_blocks/jade` | 90 | nonempty #occultism:miners/eldritch; nonempty #c:storage_blocks/jade |
| `miner/eldritch/lapis` | `#occultism:miners/eldritch` | 1× `#c:storage_blocks/lapis` | 90 | nonempty #occultism:miners/eldritch; nonempty #c:storage_blocks/lapis |
| `miner/eldritch/lignite_coal` | `#occultism:miners/eldritch` | 1× `#c:storage_blocks/lignite_coal` | 90 | nonempty #occultism:miners/eldritch; nonempty #c:storage_blocks/lignite_coal |
| `miner/eldritch/mining_dim_core` | `#occultism:miners/eldritch` | 1× `occultism:mining_dim_core` | 1 | none |
| `miner/eldritch/monazite` | `#occultism:miners/eldritch` | 1× `#c:storage_blocks/monazite` | 90 | nonempty #occultism:miners/eldritch; nonempty #c:storage_blocks/monazite |
| `miner/eldritch/onyx` | `#occultism:miners/eldritch` | 1× `#c:storage_blocks/onyx` | 90 | nonempty #occultism:miners/eldritch; nonempty #c:storage_blocks/onyx |
| `miner/eldritch/peridot` | `#occultism:miners/eldritch` | 1× `#c:storage_blocks/peridot` | 90 | nonempty #occultism:miners/eldritch; nonempty #c:storage_blocks/peridot |
| `miner/eldritch/prosperity_shard` | `#occultism:miners/eldritch` | 1× `#c:storage_blocks/prosperity_shard` | 90 | nonempty #occultism:miners/eldritch; nonempty #c:storage_blocks/prosperity_shard |
| `miner/eldritch/quartz` | `#occultism:miners/eldritch` | 9× `#c:gems/quartz` | 90 | nonempty #occultism:miners/eldritch; nonempty #c:gems/quartz |
| `miner/eldritch/raw_aethersent` | `#occultism:miners/eldritch` | 1× `#c:storage_blocks/raw_aethersent` | 90 | nonempty #occultism:miners/eldritch; nonempty #c:storage_blocks/raw_aethersent |
| `miner/eldritch/raw_allthemodium` | `#occultism:miners/eldritch` | 1× `#c:storage_blocks/raw_allthemodium` | 30 | nonempty #occultism:miners/eldritch; nonempty #c:storage_blocks/raw_allthemodium |
| `miner/eldritch/raw_aluminum` | `#occultism:miners/eldritch` | 1× `#c:storage_blocks/raw_aluminum` | 90 | nonempty #occultism:miners/eldritch; nonempty #c:storage_blocks/raw_aluminum |
| `miner/eldritch/raw_antimony` | `#occultism:miners/eldritch` | 1× `#c:storage_blocks/raw_antimony` | 90 | nonempty #occultism:miners/eldritch; nonempty #c:storage_blocks/raw_antimony |
| `miner/eldritch/raw_azure_silver` | `#occultism:miners/eldritch` | 1× `#c:storage_blocks/raw_azure_silver` | 90 | nonempty #occultism:miners/eldritch; nonempty #c:storage_blocks/raw_azure_silver |
| `miner/eldritch/raw_cloggrum` | `#occultism:miners/eldritch` | 1× `#c:storage_blocks/raw_cloggrum` | 90 | nonempty #occultism:miners/eldritch; nonempty #c:storage_blocks/raw_cloggrum |
| `miner/eldritch/raw_cobalt` | `#occultism:miners/eldritch` | 1× `#c:storage_blocks/raw_cobalt` | 90 | nonempty #occultism:miners/eldritch; nonempty #c:storage_blocks/raw_cobalt |
| `miner/eldritch/raw_copper` | `#occultism:miners/eldritch` | 1× `#c:storage_blocks/raw_copper` | 90 | nonempty #occultism:miners/eldritch; nonempty #c:storage_blocks/raw_copper |
| `miner/eldritch/raw_crimson_iron` | `#occultism:miners/eldritch` | 1× `#c:storage_blocks/raw_crimson_iron` | 90 | nonempty #occultism:miners/eldritch; nonempty #c:storage_blocks/raw_crimson_iron |
| `miner/eldritch/raw_froststeel` | `#occultism:miners/eldritch` | 1× `#c:storage_blocks/raw_froststeel` | 90 | nonempty #occultism:miners/eldritch; nonempty #c:storage_blocks/raw_froststeel |
| `miner/eldritch/raw_gold` | `#occultism:miners/eldritch` | 1× `#c:storage_blocks/raw_gold` | 90 | nonempty #occultism:miners/eldritch; nonempty #c:storage_blocks/raw_gold |
| `miner/eldritch/raw_iesnium` | `#occultism:miners/eldritch` | 1× `#c:storage_blocks/raw_iesnium` | 90 | nonempty #occultism:miners/eldritch; nonempty #c:storage_blocks/raw_iesnium |
| `miner/eldritch/raw_iridium` | `#occultism:miners/eldritch` | 1× `#c:storage_blocks/raw_iridium` | 90 | nonempty #occultism:miners/eldritch; nonempty #c:storage_blocks/raw_iridium |
| `miner/eldritch/raw_iron` | `#occultism:miners/eldritch` | 1× `#c:storage_blocks/raw_iron` | 90 | nonempty #occultism:miners/eldritch; nonempty #c:storage_blocks/raw_iron |
| `miner/eldritch/raw_lead` | `#occultism:miners/eldritch` | 1× `#c:storage_blocks/raw_lead` | 90 | nonempty #occultism:miners/eldritch; nonempty #c:storage_blocks/raw_lead |
| `miner/eldritch/raw_nickel` | `#occultism:miners/eldritch` | 1× `#c:storage_blocks/raw_nickel` | 90 | nonempty #occultism:miners/eldritch; nonempty #c:storage_blocks/raw_nickel |
| `miner/eldritch/raw_osmium` | `#occultism:miners/eldritch` | 1× `#c:storage_blocks/raw_osmium` | 90 | nonempty #occultism:miners/eldritch; nonempty #c:storage_blocks/raw_osmium |
| `miner/eldritch/raw_platinum` | `#occultism:miners/eldritch` | 1× `#c:storage_blocks/raw_platinum` | 90 | nonempty #occultism:miners/eldritch; nonempty #c:storage_blocks/raw_platinum |
| `miner/eldritch/raw_silver` | `#occultism:miners/eldritch` | 1× `#c:storage_blocks/raw_silver` | 90 | nonempty #occultism:miners/eldritch; nonempty #c:storage_blocks/raw_silver |
| `miner/eldritch/raw_tin` | `#occultism:miners/eldritch` | 1× `#c:storage_blocks/raw_tin` | 90 | nonempty #occultism:miners/eldritch; nonempty #c:storage_blocks/raw_tin |
| `miner/eldritch/raw_titanium` | `#occultism:miners/eldritch` | 1× `#c:storage_blocks/raw_titanium` | 90 | nonempty #occultism:miners/eldritch; nonempty #c:storage_blocks/raw_titanium |
| `miner/eldritch/raw_tungsten` | `#occultism:miners/eldritch` | 1× `#c:storage_blocks/raw_tungsten` | 90 | nonempty #occultism:miners/eldritch; nonempty #c:storage_blocks/raw_tungsten |
| `miner/eldritch/raw_unobtainium` | `#occultism:miners/eldritch` | 1× `#c:storage_blocks/raw_unobtainium` | 10 | nonempty #occultism:miners/eldritch; nonempty #c:storage_blocks/raw_unobtainium |
| `miner/eldritch/raw_uranium` | `#occultism:miners/eldritch` | 1× `#c:storage_blocks/raw_uranium` | 90 | nonempty #occultism:miners/eldritch; nonempty #c:storage_blocks/raw_uranium |
| `miner/eldritch/raw_vibranium` | `#occultism:miners/eldritch` | 1× `#c:storage_blocks/raw_vibranium` | 20 | nonempty #occultism:miners/eldritch; nonempty #c:storage_blocks/raw_vibranium |
| `miner/eldritch/raw_yellorium` | `#occultism:miners/eldritch` | 1× `#c:storage_blocks/raw_yellorium` | 90 | nonempty #occultism:miners/eldritch; nonempty #c:storage_blocks/raw_yellorium |
| `miner/eldritch/raw_zinc` | `#occultism:miners/eldritch` | 1× `#c:storage_blocks/raw_zinc` | 90 | nonempty #occultism:miners/eldritch; nonempty #c:storage_blocks/raw_zinc |
| `miner/eldritch/redstone` | `#occultism:miners/eldritch` | 1× `#c:storage_blocks/redstone` | 90 | nonempty #occultism:miners/eldritch; nonempty #c:storage_blocks/redstone |
| `miner/eldritch/ruby` | `#occultism:miners/eldritch` | 1× `#c:storage_blocks/ruby` | 90 | nonempty #occultism:miners/eldritch; nonempty #c:storage_blocks/ruby |
| `miner/eldritch/rune` | `#occultism:miners/eldritch` | 1× `#c:storage_blocks/rune` | 90 | nonempty #occultism:miners/eldritch; nonempty #c:storage_blocks/rune |
| `miner/eldritch/sal_ammoniac` | `#occultism:miners/eldritch` | 9× `#c:gems/sal_ammoniac` | 90 | nonempty #occultism:miners/eldritch; nonempty #c:gems/sal_ammoniac |
| `miner/eldritch/salt` | `#occultism:miners/eldritch` | 1× `#c:storage_blocks/salt` | 90 | nonempty #occultism:miners/eldritch; nonempty #c:storage_blocks/salt |
| `miner/eldritch/sapphire` | `#occultism:miners/eldritch` | 1× `#c:storage_blocks/sapphire` | 90 | nonempty #occultism:miners/eldritch; nonempty #c:storage_blocks/sapphire |
| `miner/eldritch/soulium_dust` | `#occultism:miners/eldritch` | 1× `#c:storage_blocks/soulium_dust` | 90 | nonempty #occultism:miners/eldritch; nonempty #c:storage_blocks/soulium_dust |
| `miner/eldritch/stellarite` | `#occultism:miners/eldritch` | 1× `#c:storage_blocks/stellarite` | 90 | nonempty #occultism:miners/eldritch; nonempty #c:storage_blocks/stellarite |
| `miner/eldritch/sulfur` | `#occultism:miners/eldritch` | 1× `#c:storage_blocks/sulfur` | 90 | nonempty #occultism:miners/eldritch; nonempty #c:storage_blocks/sulfur |
| `miner/eldritch/uraninite` | `#occultism:miners/eldritch` | 1× `#c:storage_blocks/uraninite` | 90 | nonempty #occultism:miners/eldritch; nonempty #c:storage_blocks/uraninite |
| `miner/master/ancient_debris` | `#occultism:miners/master` | 1× `minecraft:ancient_debris` | 100 | none |
| `miner/master/iesnium_ore` | `#occultism:miners/master` | 1× `occultism:iesnium_ore` | 100 | none |
| `miner/master/mining_dim_core` | `#occultism:miners/master` | 1× `occultism:mining_dim_core` | 1 | none |
| `miner/master/stella_arcanum` | `#occultism:miners/master` | 1× `#c:ores/stella_arcanum` | 100 | nonempty #occultism:miners/master; nonempty #c:ores/stella_arcanum |
| `miner/master/stellarite` | `#occultism:miners/master` | 1× `#c:ores/stellarite` | 50 | nonempty #occultism:miners/master; nonempty #c:ores/stellarite |
| `miner/ores/agate_ore` | `#occultism:miners/ores` | 1× `#c:ores/agate` | 200 | nonempty #occultism:miners/ores; nonempty #c:ores/agate |
| `miner/ores/aluminum_ore` | `#occultism:miners/ores` | 1× `#c:ores/aluminum` | 422 | nonempty #occultism:miners/ores; nonempty #c:ores/aluminum |
| `miner/ores/amber_ore` | `#occultism:miners/ores` | 1× `#c:ores/amber` | 184 | nonempty #occultism:miners/ores; nonempty #c:ores/amber |
| `miner/ores/amethyst_ore` | `#occultism:miners/ores` | 1× `#c:ores/amethyst` | 200 | nonempty #occultism:miners/ores; nonempty #c:ores/amethyst |
| `miner/ores/antimony_ore` | `#occultism:miners/ores` | 1× `#c:ores/antimony` | 80 | nonempty #occultism:miners/ores; nonempty #c:ores/antimony |
| `miner/ores/aquamarine_ore` | `#occultism:miners/ores` | 1× `#c:ores/aquamarine` | 200 | nonempty #occultism:miners/ores; nonempty #c:ores/aquamarine |
| `miner/ores/arcane_crystal` | `#occultism:miners/ores` | 1× `#c:ores/arcane_crystal` | 200 | nonempty #occultism:miners/ores; nonempty #c:ores/arcane_crystal |
| `miner/ores/ardite_ore` | `#occultism:miners/ores` | 1× `#c:ores/ardite` | 159 | nonempty #occultism:miners/ores; nonempty #c:ores/ardite |
| `miner/ores/baronyte_ore` | `#occultism:miners/ores` | 1× `#c:ores/baronyte` | 260 | nonempty #occultism:miners/ores; nonempty #c:ores/baronyte |
| `miner/ores/bauxite_ore` | `#occultism:miners/ores` | 1× `#c:ores/bauxite` | 168 | nonempty #occultism:miners/ores; nonempty #c:ores/bauxite |
| `miner/ores/beryl_ore` | `#occultism:miners/ores` | 1× `#c:ores/beryl` | 200 | nonempty #occultism:miners/ores; nonempty #c:ores/beryl |
| `miner/ores/black_quartz_ore` | `#occultism:miners/ores` | 1× `#c:ores/black_quartz` | 360 | nonempty #occultism:miners/ores; nonempty #c:ores/black_quartz |
| `miner/ores/blazium_ore` | `#occultism:miners/ores` | 1× `#c:ores/blazium` | 218 | nonempty #occultism:miners/ores; nonempty #c:ores/blazium |
| `miner/ores/bloodstone_ore` | `#occultism:miners/ores` | 1× `#c:ores/bloodstone` | 160 | nonempty #occultism:miners/ores; nonempty #c:ores/bloodstone |
| `miner/ores/blue_gemstone_ore` | `#occultism:miners/ores` | 1× `#c:ores/blue_gemstone` | 300 | nonempty #occultism:miners/ores; nonempty #c:ores/blue_gemstone |
| `miner/ores/bone_fragments_ore` | `#occultism:miners/ores` | 1× `#c:ores/bone_fragments` | 302 | nonempty #occultism:miners/ores; nonempty #c:ores/bone_fragments |
| `miner/ores/boron_ore` | `#occultism:miners/ores` | 1× `#c:ores/boron` | 199 | nonempty #occultism:miners/ores; nonempty #c:ores/boron |
| `miner/ores/certus_quartz_ore` | `#occultism:miners/ores` | 1× `#c:ores/certus_quartz` | 187 | nonempty #occultism:miners/ores; nonempty #c:ores/certus_quartz |
| `miner/ores/charged_runium_ore` | `#occultism:miners/ores` | 1× `#c:ores/charged_runium` | 142 | nonempty #occultism:miners/ores; nonempty #c:ores/charged_runium |
| `miner/ores/cinnabar_ore` | `#occultism:miners/ores` | 1× `#c:ores/cinnabar` | 190 | nonempty #occultism:miners/ores; nonempty #c:ores/cinnabar |
| `miner/ores/clay` | `#occultism:miners/ores` | 1× `minecraft:clay` | 300 | none |
| `miner/ores/coal_ore` | `#occultism:miners/ores` | 1× `#c:ores/coal` | 1000 | none |
| `miner/ores/cobalt_ore` | `#occultism:miners/ores` | 1× `#c:ores/cobalt` | 163 | nonempty #occultism:miners/ores; nonempty #c:ores/cobalt |
| `miner/ores/copper_ore` | `#occultism:miners/ores` | 1× `#c:ores/copper` | 584 | none |
| `miner/ores/crying_obsidian` | `#occultism:miners/ores` | 1× `minecraft:crying_obsidian` | 50 | none |
| `miner/ores/crystallite_ore` | `#occultism:miners/ores` | 1× `#c:ores/crystallite` | 500 | nonempty #occultism:miners/ores; nonempty #c:ores/crystallite |
| `miner/ores/dark_gem_ore` | `#occultism:miners/ores` | 1× `#c:ores/dark_gem` | 200 | nonempty #occultism:miners/ores; nonempty #c:ores/dark_gem |
| `miner/ores/diamond_ore` | `#occultism:miners/ores` | 1× `#c:ores/diamond` | 218 | none |
| `miner/ores/dimensional_shard_ore` | `#occultism:miners/ores` | 1× `#c:ores/dimensional_shard` | 127 | nonempty #occultism:miners/ores; nonempty #c:ores/dimensional_shard |
| `miner/ores/draconium_ore` | `#occultism:miners/ores` | 1× `#c:ores/draconium` | 142 | nonempty #occultism:miners/ores; nonempty #c:ores/draconium |
| `miner/ores/elecanium_ore` | `#occultism:miners/ores` | 1× `#c:ores/elecanium` | 200 | nonempty #occultism:miners/ores; nonempty #c:ores/elecanium |
| `miner/ores/electrotine_ore` | `#occultism:miners/ores` | 1× `#c:ores/electrotine` | 155 | nonempty #occultism:miners/ores; nonempty #c:ores/electrotine |
| `miner/ores/emberstone_ore` | `#occultism:miners/ores` | 1× `#c:ores/emberstone` | 302 | nonempty #occultism:miners/ores; nonempty #c:ores/emberstone |
| `miner/ores/emerald_ore` | `#occultism:miners/ores` | 1× `#c:ores/emerald` | 156 | none |
| `miner/ores/fluorite_ore` | `#occultism:miners/ores` | 1× `#c:ores/fluorite` | 133 | nonempty #occultism:miners/ores; nonempty #c:ores/fluorite |
| `miner/ores/garnet_ore` | `#occultism:miners/ores` | 1× `#c:ores/garnet` | 200 | nonempty #occultism:miners/ores; nonempty #c:ores/garnet |
| `miner/ores/gemenyte_ore` | `#occultism:miners/ores` | 1× `#c:ores/gemenyte` | 400 | nonempty #occultism:miners/ores; nonempty #c:ores/gemenyte |
| `miner/ores/ghastly_ore` | `#occultism:miners/ores` | 1× `#c:ores/ghastly` | 200 | nonempty #occultism:miners/ores; nonempty #c:ores/ghastly |
| `miner/ores/ghoulish_ore` | `#occultism:miners/ores` | 1× `#c:ores/ghoulish` | 180 | nonempty #occultism:miners/ores; nonempty #c:ores/ghoulish |
| `miner/ores/gilded_blackstone` | `#occultism:miners/ores` | 1× `minecraft:gilded_blackstone` | 373 | none |
| `miner/ores/glowstone` | `#occultism:miners/ores` | 1× `minecraft:glowstone` | 234 | none |
| `miner/ores/gold_ore` | `#occultism:miners/ores` | 1× `#c:ores/gold` | 311 | none |
| `miner/ores/gravel` | `#occultism:miners/ores` | 1× `minecraft:gravel` | 300 | none |
| `miner/ores/green_gemstone_ore` | `#occultism:miners/ores` | 1× `#c:ores/green_gemstone` | 300 | nonempty #occultism:miners/ores; nonempty #c:ores/green_gemstone |
| `miner/ores/heliodor_ore` | `#occultism:miners/ores` | 1× `#c:ores/heliodor` | 200 | nonempty #occultism:miners/ores; nonempty #c:ores/heliodor |
| `miner/ores/indicolite_ore` | `#occultism:miners/ores` | 1× `#c:ores/indicolite` | 200 | nonempty #occultism:miners/ores; nonempty #c:ores/indicolite |
| `miner/ores/inferium_ore` | `#occultism:miners/ores` | 1× `#c:ores/inferium` | 190 | nonempty #occultism:miners/ores; nonempty #c:ores/inferium |
| `miner/ores/iolite_ore` | `#occultism:miners/ores` | 1× `#c:ores/iolite` | 200 | nonempty #occultism:miners/ores; nonempty #c:ores/iolite |
| `miner/ores/iridium_ore` | `#occultism:miners/ores` | 1× `#c:ores/iridium` | 40 | nonempty #occultism:miners/ores; nonempty #c:ores/iridium |
| `miner/ores/iron_ore` | `#occultism:miners/ores` | 1× `#c:ores/iron` | 750 | none |
| `miner/ores/jade_ore` | `#occultism:miners/ores` | 1× `#c:ores/jade` | 200 | nonempty #occultism:miners/ores; nonempty #c:ores/jade |
| `miner/ores/jewelyte_ore` | `#occultism:miners/ores` | 1× `#c:ores/jewelyte` | 370 | nonempty #occultism:miners/ores; nonempty #c:ores/jewelyte |
| `miner/ores/lapis_ore` | `#occultism:miners/ores` | 1× `#c:ores/lapis` | 343 | none |
| `miner/ores/lead_ore` | `#occultism:miners/ores` | 1× `#c:ores/lead` | 500 | nonempty #occultism:miners/ores; nonempty #c:ores/lead |
| `miner/ores/lignite_coal_ore` | `#occultism:miners/ores` | 1× `#c:ores/lignite_coal` | 212 | nonempty #occultism:miners/ores; nonempty #c:ores/lignite_coal |
| `miner/ores/limonite_ore` | `#occultism:miners/ores` | 1× `#c:ores/limonite` | 400 | nonempty #occultism:miners/ores; nonempty #c:ores/limonite |
| `miner/ores/lithium_ore` | `#occultism:miners/ores` | 1× `#c:ores/lithium` | 201 | nonempty #occultism:miners/ores; nonempty #c:ores/lithium |
| `miner/ores/lyon_ore` | `#occultism:miners/ores` | 1× `#c:ores/lyon` | 160 | nonempty #occultism:miners/ores; nonempty #c:ores/lyon |
| `miner/ores/magma_block` | `#occultism:miners/ores` | 1× `minecraft:magma_block` | 300 | none |
| `miner/ores/magnesium_ore` | `#occultism:miners/ores` | 1× `#c:ores/magnesium` | 233 | nonempty #occultism:miners/ores; nonempty #c:ores/magnesium |
| `miner/ores/malachite_ore` | `#occultism:miners/ores` | 1× `#c:ores/malachite` | 200 | nonempty #occultism:miners/ores; nonempty #c:ores/malachite |
| `miner/ores/mithril_ore` | `#occultism:miners/ores` | 1× `#c:ores/mithril` | 169 | nonempty #occultism:miners/ores; nonempty #c:ores/mithril |
| `miner/ores/monazite_ore` | `#occultism:miners/ores` | 1× `#c:ores/monazite` | 48 | nonempty #occultism:miners/ores; nonempty #c:ores/monazite |
| `miner/ores/morganite_ore` | `#occultism:miners/ores` | 1× `#c:ores/morganite` | 200 | nonempty #occultism:miners/ores; nonempty #c:ores/morganite |
| `miner/ores/mystite_ore` | `#occultism:miners/ores` | 1× `#c:ores/mystite` | 280 | nonempty #occultism:miners/ores; nonempty #c:ores/mystite |
| `miner/ores/nether_gold_ore` | `#occultism:miners/ores` | 1× `minecraft:nether_gold_ore` | 373 | none |
| `miner/ores/nether_quartz_ore` | `#occultism:miners/ores` | 1× `minecraft:nether_quartz_ore` | 560 | none |
| `miner/ores/nickel_ore` | `#occultism:miners/ores` | 1× `#c:ores/nickel` | 232 | nonempty #occultism:miners/ores; nonempty #c:ores/nickel |
| `miner/ores/niter_ore` | `#occultism:miners/ores` | 1× `#c:ores/niter` | 244 | nonempty #occultism:miners/ores; nonempty #c:ores/niter |
| `miner/ores/obsidian` | `#occultism:miners/ores` | 1× `minecraft:obsidian` | 300 | none |
| `miner/ores/onyx_ore` | `#occultism:miners/ores` | 1× `#c:ores/onyx` | 200 | nonempty #occultism:miners/ores; nonempty #c:ores/onyx |
| `miner/ores/opal_ore` | `#occultism:miners/ores` | 1× `#c:ores/opal` | 200 | nonempty #occultism:miners/ores; nonempty #c:ores/opal |
| `miner/ores/ornamyte_ore` | `#occultism:miners/ores` | 1× `#c:ores/ornamyte` | 280 | nonempty #occultism:miners/ores; nonempty #c:ores/ornamyte |
| `miner/ores/osmium_ore` | `#occultism:miners/ores` | 1× `#c:ores/osmium` | 203 | nonempty #occultism:miners/ores; nonempty #c:ores/osmium |
| `miner/ores/otherrock` | `#occultism:miners/ores` | 1× `occultism:otherrock` | 50 | none |
| `miner/ores/otherstone` | `#occultism:miners/ores` | 1× `occultism:otherstone` | 50 | none |
| `miner/ores/peridot_ore` | `#occultism:miners/ores` | 1× `#c:ores/peridot` | 200 | nonempty #occultism:miners/ores; nonempty #c:ores/peridot |
| `miner/ores/platinum_ore` | `#occultism:miners/ores` | 1× `#c:ores/platinum` | 150 | nonempty #occultism:miners/ores; nonempty #c:ores/platinum |
| `miner/ores/prosperity_ore` | `#occultism:miners/ores` | 1× `#c:ores/prosperity` | 155 | nonempty #occultism:miners/ores; nonempty #c:ores/prosperity |
| `miner/ores/purple_gemstone_ore` | `#occultism:miners/ores` | 1× `#c:ores/purple_gemstone` | 300 | nonempty #occultism:miners/ores; nonempty #c:ores/purple_gemstone |
| `miner/ores/red_gemstone_ore` | `#occultism:miners/ores` | 1× `#c:ores/red_gemstone` | 300 | nonempty #occultism:miners/ores; nonempty #c:ores/red_gemstone |
| `miner/ores/redstone_ore` | `#occultism:miners/ores` | 1× `minecraft:redstone_ore` | 515 | none |
| `miner/ores/ruby_ore` | `#occultism:miners/ores` | 1× `#c:ores/ruby` | 200 | nonempty #occultism:miners/ores; nonempty #c:ores/ruby |
| `miner/ores/runic_stone` | `#occultism:miners/ores` | 1× `#forbidden_arcanus:runic_stones` | 200 | nonempty #occultism:miners/ores; nonempty #forbidden_arcanus:runic_stones |
| `miner/ores/runium_ore` | `#occultism:miners/ores` | 1× `#c:ores/runium` | 300 | nonempty #occultism:miners/ores; nonempty #c:ores/runium |
| `miner/ores/sal_ammoniac_ore` | `#occultism:miners/ores` | 1× `#c:ores/sal_ammoniac` | 750 | nonempty #occultism:miners/ores; nonempty #c:ores/sal_ammoniac |
| `miner/ores/salt_ore` | `#occultism:miners/ores` | 1× `#c:ores/salt` | 160 | nonempty #occultism:miners/ores; nonempty #c:ores/salt |
| `miner/ores/sapphire_ore` | `#occultism:miners/ores` | 1× `#c:ores/sapphire` | 200 | nonempty #occultism:miners/ores; nonempty #c:ores/sapphire |
| `miner/ores/shyregem_ore` | `#occultism:miners/ores` | 1× `#c:ores/shyregem` | 302 | nonempty #occultism:miners/ores; nonempty #c:ores/shyregem |
| `miner/ores/shyrestone_ore` | `#occultism:miners/ores` | 1× `#c:ores/shyrestone` | 302 | nonempty #occultism:miners/ores; nonempty #c:ores/shyrestone |
| `miner/ores/silver_ore` | `#occultism:miners/ores` | 1× `#c:ores/silver` | 381 | nonempty #occultism:miners/ores; nonempty #c:ores/silver |
| `miner/ores/sulfur_ore` | `#occultism:miners/ores` | 1× `#c:ores/sulfur` | 222 | nonempty #occultism:miners/ores; nonempty #c:ores/sulfur |
| `miner/ores/tanzanite_ore` | `#occultism:miners/ores` | 1× `#c:ores/tanzanite` | 200 | nonempty #occultism:miners/ores; nonempty #c:ores/tanzanite |
| `miner/ores/thorium_ore` | `#occultism:miners/ores` | 1× `#c:ores/thorium` | 222 | nonempty #occultism:miners/ores; nonempty #c:ores/thorium |
| `miner/ores/tin_ore` | `#occultism:miners/ores` | 1× `#c:ores/tin` | 602 | nonempty #occultism:miners/ores; nonempty #c:ores/tin |
| `miner/ores/titanium_ore` | `#occultism:miners/ores` | 1× `#c:ores/titanium` | 10 | nonempty #occultism:miners/ores; nonempty #c:ores/titanium |
| `miner/ores/topaz_ore` | `#occultism:miners/ores` | 1× `#c:ores/topaz` | 200 | nonempty #occultism:miners/ores; nonempty #c:ores/topaz |
| `miner/ores/tungsten_ore` | `#occultism:miners/ores` | 1× `#c:ores/tungsten` | 192 | nonempty #occultism:miners/ores; nonempty #c:ores/tungsten |
| `miner/ores/uraninite_dense_ore` | `#occultism:miners/ores` | 1× `#c:ores/uraninite_dense` | 200 | nonempty #occultism:miners/ores; nonempty #c:ores/uraninite_dense |
| `miner/ores/uraninite_ore` | `#occultism:miners/ores` | 1× `#c:ores/uraninite` | 140 | nonempty #occultism:miners/ores; nonempty #c:ores/uraninite |
| `miner/ores/uraninite_poor_ore` | `#occultism:miners/ores` | 1× `#c:ores/uraninite_poor` | 750 | nonempty #occultism:miners/ores; nonempty #c:ores/uraninite_poor |
| `miner/ores/uraninite_regular_ore` | `#occultism:miners/ores` | 1× `#c:ores/uraninite_regular` | 500 | nonempty #occultism:miners/ores; nonempty #c:ores/uraninite_regular |
| `miner/ores/uranium_ore` | `#occultism:miners/ores` | 1× `#c:ores/uranium` | 140 | nonempty #occultism:miners/ores; nonempty #c:ores/uranium |
| `miner/ores/varsium_ore` | `#occultism:miners/ores` | 1× `#c:ores/varsium` | 200 | nonempty #occultism:miners/ores; nonempty #c:ores/varsium |
| `miner/ores/white_gemstone_ore` | `#occultism:miners/ores` | 1× `#c:ores/white_gemstone` | 300 | nonempty #occultism:miners/ores; nonempty #c:ores/white_gemstone |
| `miner/ores/xpetrified_ore` | `#occultism:miners/ores` | 1× `#c:ores/xpetrified_ore` | 200 | nonempty #occultism:miners/ores; nonempty #c:ores/xpetrified_ore |
| `miner/ores/yellow_gemstone_ore` | `#occultism:miners/ores` | 1× `#c:ores/yellow_gemstone` | 300 | nonempty #occultism:miners/ores; nonempty #c:ores/yellow_gemstone |
| `miner/ores/zinc_ore` | `#occultism:miners/ores` | 1× `#c:ores/zinc` | 186 | nonempty #occultism:miners/ores; nonempty #c:ores/zinc |

## Crystallize54 records

| Recipe (`occultism:`) | Input | Base output | Min..max tier | Raw time | Ignore multiplier | Load conditions |
| --- | --- | --- | --- | ---: | --- | --- |
| `crystallize/amber_from_dust` | `#c:dusts/amber` | 1× `#c:gems/amber` | -1..-1 | 200 | true | nonempty #c:dusts/amber; nonempty #c:gems/amber |
| `crystallize/amber_from_ore` | `#c:ores/amber` | 2× `#c:gems/amber` | -1..-1 | 200 | false | nonempty #c:ores/amber; nonempty #c:gems/amber |
| `crystallize/amethyst_from_block` | `minecraft:amethyst_block` | 4× `minecraft:amethyst_shard` | 2..-1 | 200 | true | none |
| `crystallize/amethyst_from_dust` | `#c:dusts/amethyst` | 1× `#c:gems/amethyst` | -1..-1 | 200 | true | nonempty #c:dusts/amethyst; nonempty #c:gems/amethyst |
| `crystallize/apatite_from_dust` | `#c:dusts/apatite` | 1× `#c:gems/apatite` | -1..-1 | 200 | true | nonempty #c:dusts/apatite; nonempty #c:gems/apatite |
| `crystallize/apatite_from_ore` | `#c:ores/apatite` | 2× `#c:gems/apatite` | -1..-1 | 200 | false | nonempty #c:ores/apatite; nonempty #c:gems/apatite |
| `crystallize/arcane_crystal_from_dust` | `#c:dusts/arcane_crystal` | 1× `#c:gems/arcane_crystal` | -1..-1 | 200 | true | nonempty #c:dusts/arcane_crystal; nonempty #c:gems/arcane_crystal |
| `crystallize/arcane_crystal_from_ore` | `#c:ores/arcane_crystal` | 2× `#c:gems/arcane_crystal` | -1..-1 | 200 | false | nonempty #c:ores/arcane_crystal; nonempty #c:gems/arcane_crystal |
| `crystallize/black_quartz_from_dust` | `#c:dusts/black_quartz` | 1× `#c:gems/black_quartz` | -1..-1 | 200 | true | nonempty #c:dusts/black_quartz; nonempty #c:gems/black_quartz |
| `crystallize/black_quartz_from_ore` | `#c:ores/black_quartz` | 2× `#c:gems/black_quartz` | -1..-1 | 200 | false | nonempty #c:ores/black_quartz; nonempty #c:gems/black_quartz |
| `crystallize/blackstone` | `#c:dusts/blackstone` | 1× `minecraft:blackstone` | 3..-1 | 200 | true | none |
| `crystallize/blue_ice` | `#c:dusts/blue_ice` | 1× `minecraft:blue_ice` | 3..-1 | 200 | true | none |
| `crystallize/budding_amethyst` | `minecraft:amethyst_cluster` | 1× `minecraft:budding_amethyst` | 4..-1 | 200 | true | none |
| `crystallize/calcite` | `#c:dusts/calcite` | 1× `minecraft:calcite` | 3..-1 | 200 | true | none |
| `crystallize/certus_quartz_from_dust` | `#c:dusts/certus_quartz` | 1× `#c:gems/certus_quartz` | -1..-1 | 200 | true | nonempty #c:dusts/certus_quartz; nonempty #c:gems/certus_quartz |
| `crystallize/charcoal` | `#c:dusts/charcoal` | 1× `minecraft:charcoal` | -1..-1 | 200 | true | nonempty #c:dusts/charcoal |
| `crystallize/cinnabar_from_dust` | `#c:dusts/cinnabar` | 1× `#c:gems/cinnabar` | -1..-1 | 200 | true | nonempty #c:dusts/cinnabar; nonempty #c:gems/cinnabar |
| `crystallize/cinnabar_from_ore` | `#c:ores/cinnabar` | 2× `#c:gems/cinnabar` | -1..-1 | 200 | false | nonempty #c:ores/cinnabar; nonempty #c:gems/cinnabar |
| `crystallize/coal` | `#c:dusts/coal` | 1× `minecraft:coal` | -1..-1 | 200 | true | nonempty #c:dusts/coal |
| `crystallize/crying_obsidian` | `minecraft:obsidian` | 1× `minecraft:crying_obsidian` | 4..-1 | 200 | true | none |
| `crystallize/dark_gem_from_dust` | `#c:dusts/dark_gem` | 1× `#c:gems/dark_gem` | -1..-1 | 200 | true | nonempty #c:dusts/dark_gem; nonempty #c:gems/dark_gem |
| `crystallize/dark_gem_from_ore` | `#c:ores/dark_gem` | 2× `#c:gems/dark_gem` | -1..-1 | 200 | false | nonempty #c:ores/dark_gem; nonempty #c:gems/dark_gem |
| `crystallize/diamond_from_dust` | `#c:dusts/diamond` | 1× `#c:gems/diamond` | -1..-1 | 200 | true | nonempty #c:dusts/diamond; nonempty #c:gems/diamond |
| `crystallize/diamond_from_ore` | `#c:ores/diamond` | 2× `#c:gems/diamond` | -1..-1 | 200 | false | nonempty #c:ores/diamond; nonempty #c:gems/diamond |
| `crystallize/echo_shard` | `#c:dusts/echo` | 1× `minecraft:echo_shard` | 4..-1 | 200 | true | nonempty #c:dusts/echo |
| `crystallize/emerald_from_dust` | `#c:dusts/emerald` | 1× `#c:gems/emerald` | -1..-1 | 200 | true | nonempty #c:dusts/emerald; nonempty #c:gems/emerald |
| `crystallize/emerald_from_ore` | `#c:ores/emerald` | 2× `#c:gems/emerald` | -1..-1 | 200 | false | nonempty #c:ores/emerald; nonempty #c:gems/emerald |
| `crystallize/end_stone` | `#c:dusts/end_stone` | 1× `minecraft:end_stone` | 3..-1 | 200 | true | none |
| `crystallize/ender_pearl` | `#c:dusts/ender_pearl` | 1× `#c:ender_pearls` | 2..-1 | 200 | true | nonempty #c:dusts/ender_pearl; nonempty #c:ender_pearls |
| `crystallize/entro_from_dust` | `#c:dusts/entro` | 1× `#c:gems/entro` | -1..-1 | 200 | true | nonempty #c:dusts/entro; nonempty #c:gems/entro |
| `crystallize/fluix_from_dust` | `#c:dusts/fluix` | 1× `#c:gems/fluix` | -1..-1 | 200 | true | nonempty #c:dusts/fluix; nonempty #c:gems/fluix |
| `crystallize/fluorite_from_dust` | `#c:dusts/fluorite` | 1× `#c:gems/fluorite` | -1..-1 | 200 | true | nonempty #c:dusts/fluorite; nonempty #c:gems/fluorite |
| `crystallize/fluorite_from_ore` | `#c:ores/fluorite` | 4× `#c:gems/fluorite` | -1..-1 | 200 | false | nonempty #c:ores/fluorite; nonempty #c:gems/fluorite |
| `crystallize/ice` | `#c:dusts/ice` | 1× `minecraft:ice` | 3..-1 | 200 | true | none |
| `crystallize/lapis_from_dust` | `#c:dusts/lapis` | 1× `#c:gems/lapis` | -1..-1 | 200 | true | nonempty #c:dusts/lapis; nonempty #c:gems/lapis |
| `crystallize/lapis_from_ore` | `#c:ores/lapis` | 6× `#c:gems/lapis` | -1..-1 | 200 | false | nonempty #c:ores/lapis; nonempty #c:gems/lapis |
| `crystallize/obsidian` | `#c:dusts/obsidian` | 1× `minecraft:obsidian` | 3..-1 | 200 | true | none |
| `crystallize/otherrock` | `#c:dusts/otherrock` | 1× `occultism:otherrock` | 3..-1 | 200 | true | none |
| `crystallize/otherstone` | `#c:dusts/otherstone` | 1× `occultism:otherstone` | 3..-1 | 200 | true | none |
| `crystallize/packed_ice` | `#c:dusts/packed_ice` | 1× `minecraft:packed_ice` | 3..-1 | 200 | true | none |
| `crystallize/peridot_from_dust` | `#c:dusts/peridot` | 1× `#c:gems/peridot` | -1..-1 | 200 | true | nonempty #c:dusts/peridot; nonempty #c:gems/peridot |
| `crystallize/peridot_from_ore` | `#c:ores/peridot` | 2× `#c:gems/peridot` | -1..-1 | 200 | false | nonempty #c:ores/peridot; nonempty #c:gems/peridot |
| `crystallize/prismarine_crystal` | `minecraft:prismarine_shard` | 1× `minecraft:prismarine_crystals` | 2..-1 | 200 | false | none |
| `crystallize/quartz_from_block` | `minecraft:quartz_block` | 4× `minecraft:quartz` | 2..-1 | 200 | true | none |
| `crystallize/quartz_from_dust` | `#c:dusts/quartz` | 1× `#c:gems/quartz` | -1..-1 | 200 | true | nonempty #c:dusts/quartz; nonempty #c:gems/quartz |
| `crystallize/quartz_from_ore` | `#c:ores/quartz` | 2× `#c:gems/quartz` | -1..-1 | 200 | false | nonempty #c:ores/quartz; nonempty #c:gems/quartz |
| `crystallize/ruby_from_dust` | `#c:dusts/ruby` | 1× `#c:gems/ruby` | -1..-1 | 200 | true | nonempty #c:dusts/ruby; nonempty #c:gems/ruby |
| `crystallize/ruby_from_ore` | `#c:ores/ruby` | 2× `#c:gems/ruby` | -1..-1 | 200 | false | nonempty #c:ores/ruby; nonempty #c:gems/ruby |
| `crystallize/sapphire_from_dust` | `#c:dusts/sapphire` | 1× `#c:gems/sapphire` | -1..-1 | 200 | true | nonempty #c:dusts/sapphire; nonempty #c:gems/sapphire |
| `crystallize/sapphire_from_ore` | `#c:ores/sapphire` | 2× `#c:gems/sapphire` | -1..-1 | 200 | false | nonempty #c:ores/sapphire; nonempty #c:gems/sapphire |
| `crystallize/sulfur_from_dust` | `#c:dusts/sulfur` | 1× `#c:gems/sulfur` | -1..-1 | 200 | true | nonempty #c:dusts/sulfur; nonempty #c:gems/sulfur |
| `crystallize/sulfur_from_ore` | `#c:ores/sulfur` | 2× `#c:gems/sulfur` | -1..-1 | 200 | false | nonempty #c:ores/sulfur; nonempty #c:gems/sulfur |
| `crystallize/topaz_from_dust` | `#c:dusts/topaz` | 1× `#c:gems/topaz` | -1..-1 | 200 | true | nonempty #c:dusts/topaz; nonempty #c:gems/topaz |
| `crystallize/topaz_from_ore` | `#c:ores/topaz` | 2× `#c:gems/topaz` | -1..-1 | 200 | false | nonempty #c:ores/topaz; nonempty #c:gems/topaz |

## Spirit-fire27 records

| Recipe (`occultism:`) | Input | Result item |
| --- | --- | --- |
| `spirit_fire/awakened_feather` | `#c:feathers` | 1× `occultism:awakened_feather` |
| `spirit_fire/book_of_binding_empty` | `#occultism:books/books_for_empty` | 1× `occultism:book_of_binding_empty` |
| `spirit_fire/chalk_black` | `occultism:chalk_black_impure` | 1× `occultism:chalk_black` |
| `spirit_fire/chalk_blue` | `occultism:chalk_blue_impure` | 1× `occultism:chalk_blue` |
| `spirit_fire/chalk_brown` | `occultism:chalk_brown_impure` | 1× `occultism:chalk_brown` |
| `spirit_fire/chalk_cyan` | `occultism:chalk_cyan_impure` | 1× `occultism:chalk_cyan` |
| `spirit_fire/chalk_gold` | `occultism:chalk_yellow_impure` | 1× `occultism:chalk_gold` |
| `spirit_fire/chalk_gray` | `occultism:chalk_gray_impure` | 1× `occultism:chalk_gray` |
| `spirit_fire/chalk_green` | `occultism:chalk_green_impure` | 1× `occultism:chalk_green` |
| `spirit_fire/chalk_light_blue` | `occultism:chalk_light_blue_impure` | 1× `occultism:chalk_light_blue` |
| `spirit_fire/chalk_light_gray` | `occultism:chalk_light_gray_impure` | 1× `occultism:chalk_light_gray` |
| `spirit_fire/chalk_lime` | `occultism:chalk_lime_impure` | 1× `occultism:chalk_lime` |
| `spirit_fire/chalk_magenta` | `occultism:chalk_magenta_impure` | 1× `occultism:chalk_magenta` |
| `spirit_fire/chalk_orange` | `occultism:chalk_orange_impure` | 1× `occultism:chalk_orange` |
| `spirit_fire/chalk_pink` | `occultism:chalk_pink_impure` | 1× `occultism:chalk_pink` |
| `spirit_fire/chalk_purple` | `occultism:chalk_purple_impure` | 1× `occultism:chalk_purple` |
| `spirit_fire/chalk_red` | `occultism:chalk_red_impure` | 1× `occultism:chalk_red` |
| `spirit_fire/chalk_white` | `occultism:chalk_white_impure` | 1× `occultism:chalk_white` |
| `spirit_fire/otherflower` | `#minecraft:flowers` | 1× `occultism:otherflower` |
| `spirit_fire/otherrock` | `minecraft:diorite` | 1× `occultism:otherrock` |
| `spirit_fire/otherstone` | `minecraft:andesite` | 1× `occultism:otherstone` |
| `spirit_fire/otherworld_ashes` | `#occultism:otherworld_logs` | 1× `occultism:otherworld_ashes` |
| `spirit_fire/otherworld_essence` | `occultism:demons_dream_essence` | 1× `occultism:otherworld_essence` |
| `spirit_fire/otherworld_sapling_natural` | `minecraft:oak_sapling` | 1× `occultism:otherworld_sapling_natural` |
| `spirit_fire/purified_ink` | `#c:dyes/black` | 1× `occultism:purified_ink` |
| `spirit_fire/spirit_attuned_gem` | `#c:gems/diamond` | 1× `occultism:spirit_attuned_gem` |
| `spirit_fire/taboo_book` | `minecraft:book` | 1× `occultism:taboo_book` |

## Spirit-trade14 records

| Recipe (`occultism:`) | Input | Trader | Result | Relative weight |
| --- | --- | --- | --- | ---: |
| `spirit_trade/gambler_amethyst` | `#c:gems` | `occultism:gambler` | 1× `minecraft:amethyst_shard` | 4 |
| `spirit_trade/gambler_diamond` | `#c:gems` | `occultism:gambler` | 1× `minecraft:diamond` | 1 |
| `spirit_trade/gambler_emerald` | `#c:gems` | `occultism:gambler` | 1× `minecraft:emerald` | 1 |
| `spirit_trade/gambler_gold` | `#c:gems` | `occultism:gambler` | 1× `minecraft:gold_nugget` | 8 |
| `spirit_trade/gambler_iesnium` | `#c:gems` | `occultism:gambler` | 1× `occultism:iesnium_nugget` | 1 |
| `spirit_trade/gambler_iron` | `#c:gems` | `occultism:gambler` | 1× `minecraft:iron_nugget` | 16 |
| `spirit_trade/gambler_lazuli` | `#c:gems` | `occultism:gambler` | 1× `minecraft:lapis_lazuli` | 2 |
| `spirit_trade/gambler_prismarine` | `#c:gems` | `occultism:gambler` | 1× `minecraft:prismarine_crystals` | 4 |
| `spirit_trade/gambler_quartz` | `#c:gems` | `occultism:gambler` | 1× `minecraft:quartz` | 2 |
| `spirit_trade/gambler_silver` | `#c:gems` | `occultism:gambler` | 1× `occultism:silver_nugget` | 8 |
| `spirit_trade/gambler_spirit` | `#c:gems` | `occultism:gambler` | 1× `occultism:spirit_attuned_gem` | 8 |
| `spirit_trade/otherworld_sapling` | `#occultism:saplings/otherworld_natural` | `occultism:trader_otherworld_saplings` | 1× `occultism:otherworld_sapling` | 1 |
| `spirit_trade/stone_to_otherrock` | `#c:stones` | `occultism:trader_otherrock` | 2× `occultism:otherrock` | 1 |
| `spirit_trade/stone_to_otherstone` | `#c:stones` | `occultism:trader_otherstone` | 2× `occultism:otherstone` | 1 |

## Teaching and acceptance (unrun)

Introduce one actual starter crusher input and compare held/debited count with returned dust; upgrade spirit tier only after teaching differing speed/batch/output factors. Separate ingot grinding with multiplier ignored from raw/ore multiplication; do not reward a ready worker that skips its binding lesson. Give XP plus ordinary support materials; item tasks detect a worker book/miner only as acquisition proxy.

Mineshaft lesson: bind/craft appropriate miner, insert selector, set safe empty output, observe operation/durability and a weighted result. Explain randomness and capacity before automating. Trader lesson: known input and matching trader ID, consume one, inspect actual output; not all14 entries apply to every trader. Spirit-fire lesson: known input placed in native fire, verify transformation and losses in a disposable area.

Positive/boundary cases: exact recipe input vs empty optional tag; minimum/maximum tier; ignored/default multiplier; operation batch1/less-than-limit; full/partly-full Extractor; output event replacement; miner matching pool/weights with recorded loaded recipes; redstone and Unusing near break; changing miner type vs components with cached pool; reload/cache and tag preference; actual Draconium member vs gated trophy/recursive tag. Do not promote arithmetic to measured throughput or call a recipe condition proof its output exists in the assembled pack.

Evidence: exact JSON resources; `CrushingRecipe/CrystallizeRecipe/MinerRecipe/SpiritTradeRecipe/SpiritFireRecipe`, result classes including `TagRecipeResult`, `CrusherJob/CrystallizerJob/TraderJob`, `DimensionalMineshaftBlockEntity`. Direct pinned binary read/CFR, scratch outside Git. Spirit Fire, tier/default configuration and major familiar/storage/Otherworld source branches are closed in companions; effective loaded tag graph and acceptance remain unverified.
