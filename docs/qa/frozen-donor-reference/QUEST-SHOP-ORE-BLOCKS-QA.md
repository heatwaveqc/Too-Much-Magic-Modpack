# Ore Blocks & Mining — Shop expansion (QA candidate)

The user approved ordinary vanilla ore **blocks** for the quest shop, especially iron and gold. This addition is a dedicated **sixth custom shop category**, plus the built-in General category. Existing five-category 29-offer shop is preserved; this adds **18** offers for a total of **47** purchasable offers.

Purchases yield the block itself (including the deepslate and nether forms), **not raw items/ingots**. Players can smelt ores or apply the usual mined-block Fortune and magic processing rules if they have the tools. Natural ore variants are a choice of convenience; deepslate is the same price as ordinary stone.

| Offer | Quantity | Team coins |
| --- | ---: | ---: |
| Coal ore / Deepslate coal ore | 4 | 1 |
| Copper ore / Deepslate copper ore | 2 | 1 |
| Iron ore / Deepslate iron ore | 4 | 3 |
| Gold ore / Deepslate gold ore | 4 | 4 |
| Redstone ore / Deepslate redstone ore | 4 | 3 |
| Lapis ore / Deepslate lapis ore | 4 | 3 |
| Diamond ore / Deepslate diamond ore | 1 | 10 |
| Emerald ore / Deepslate emerald ore | 1 | 10 |
| Nether quartz ore | 4 | 3 |
| Nether gold ore | 4 | 3 |

**Hard exclusions:** ancient debris and anything requiring Netherite upgrades, modded boss loot, Chaos Guardian drops, unusual worldgen ores and progression-locked resources. No new coin sales added or changed.

**Test:** Open FTB Quests > Quest Shop button; confirm Ore Blocks & Mining category with 18 offers. After earning 1 coin from copper sale, buy copper or coal ore, verify exact amount/block and team balance decrement. Buy iron/gold with adequate coins, check block ID, smelting/Fortune output and no ore/coin infinite loops (including Neo Vitae dust and Mahou circles). Verify 29 old offers and purchase behavior unchanged. Confirm no malformed KubeJS datapack on server startup.

**Status:** source/data authored; does not mean the pack is rebuilt or client-verified. Import via `test/full-integration` integration manifest and smoke before delivery.
