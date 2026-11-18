# Batch 4 — Alkahestry and Botania (2026-11-18)

Owner-authorized configuration-only pass. Refs #320 #319 #322 #47 #326 #339. Depends on Batch 2 (#320 head 4241676f1276be577b8b0574bbb48bc745827e90), including raw item mapping and ordinary metal values; integrate atomically. No scripts, code, mod pin, shared manifest/index changes, assembly, boot or Prism build.

## Reliquary

Pinned Reliquary 2.0.85.1599; aligned source [d0cde713](https://github.com/P3pp3rF1y/Reliquary/tree/d0cde7135d641529b9494aca9f1b06a52a9da603). Native ModRecipeProvider actually lists 22 crafting recipes, not 21: disable all exact IDs and redstone drain via same-ID neoforge:false with inert valid stick-to-stick payload. Preserve Tome acquisition. COMMON config items.alkahestryTome.chargeLimit=9999 (native cap); other defaults untouched. Reconcile other donor config if present. Native charging IDs overridden at 8/72/48/192 charges for redstone/dust block and glowstone dust/block respectively. At 64/384 EMC per dust and ordinary 9/4 compression, 1 charge=8 EMC; verify effective values and both charging paths, near capacity, reload, JEI and drain-free GUI (native registry uses Optional and resets on reload).

Seed ingredient strict NeoForge components with singleton item list and empty components: native implementation compares full components of default stack; rejects altered components. Native Reliquary copies seed and sets total count, returning Tome with spent charge. Multiple matching seed slots consume extra seeds, not multiply output; verify automation and shift-click. Modded seed item_exists guard; missing item is a blocker. Charge=ceil(net added EMC/10); cost=8 EMC/charge. Choose five extras for raw metals and inexpensive minerals, one extra for diamond/emerald for lower entry cost; last two slightly below 25% gain because rounded up. No guessed tin/nickel ID.

| Seed ID | Source EMC | Extra | Total | Charge | Fuel EMC | Added EMC |
|---|---:|---:|---:|---:|---:|---:|
| minecraft:raw_copper | 54 | 5 | 6 | 27 | 216 | 270 |
| minecraft:raw_iron | 108 | 5 | 6 | 54 | 432 | 540 |
| minecraft:raw_gold | 864 | 5 | 6 | 432 | 3456 | 4320 |
| occultism:raw_silver | 216 | 5 | 6 | 108 | 864 | 1080 |
| embers:raw_lead | 216 | 5 | 6 | 108 | 864 | 1080 |
| minecraft:gold_nugget | 16 | 5 | 6 | 8 | 64 | 80 |
| minecraft:coal | 128 | 5 | 6 | 64 | 512 | 640 |
| minecraft:diamond | 8192 | 1 | 2 | 820 | 6560 | 8192 |
| minecraft:emerald | 16384 | 1 | 2 | 1639 | 13112 | 16384 |
| minecraft:lapis_lazuli | 864 | 5 | 6 | 432 | 3456 | 4320 |
| minecraft:quartz | 256 | 5 | 6 | 128 | 1024 | 1280 |
| minecraft:amethyst_shard | 32 | 5 | 6 | 16 | 128 | 160 |

Excluded: redstone, glowstone, uranium, Rock Crystal, refined ingots, generic blocks, boss trophies and unseeded recipes. Held (not disallowed): approved Ancient Debris, Raw Iesnium, Raw Demonite, Draconium Dust, Replica Ore, plus sulfur and all additional candidates, until exact IDs, loot/gates and final prices/premiums are verified. Ancient Debris is approved self-dropping exception; don't guess price. Final whitelist incomplete. See #320 ore-product survey.

ProjectE native BaseRecipeTypeMapper skips isSpecial; Reliquary crafting returns true. Verify effective mappers; #322 must explicitly exclude productive recipes when adding mapper support. Deliberate ProjectE -> fuel -> Tome -> ProjectE gain is approved; free charge is not. Charged Tome component-aware valuation/transmutation/knowledge and Project Expansion automation remain unproven; don't claim safety or zero empty Tome's positive EMC. Need verified native prevention or code-pass handling.

## Botania

Botania Revived 2.0.2 [5f803227](https://github.com/Velron-Devs/Botania-Revived/tree/5f803227d484598da1f1093a91d5d7cf5cf9600d/src/generated/resources/data/botania/recipe/mana_infusion). 19 base-mod conjuration recipes verified against source; identical inputs and outputs, one extra per craft. max(200,ceil(20*EMC/100)*100) Mana; 17 overrides, 2 unchanged.

| Recipe | Native mana | Revised mana | Source EMC |
|---|---:|---:|---:|
| grass | 800 | 200 | 1 |
| coal_dupe | 2100 | 2600 | 128 |
| quartz_dupe | 2500 | 5200 | 256 |
| gravel_dupe | 720 | 200 | 4 |
| redstone_dupe | 5000 | 1300 | 64 |
| snowball_dupe | 200 | 200 | 1 |
| soul_sand_dupe | 1500 | 1000 | 49 |
| glowstone_dupe | 5000 | 7700 | 384 |
| netherrack_dupe | 200 | 200 | 1 |
| oak_leaves_dupe | 2000 | 200 | 1 |
| birch_leaves_dupe | 2000 | 200 | 1 |
| jungle_leaves_dupe | 2000 | 200 | 1 |
| cherry_leaves_dupe | 2000 | 200 | 1 |
| acacia_leaves_dupe | 2000 | 200 | 1 |
| azalea_leaves_dupe | 2000 | 200 | 1 |
| spruce_leaves_dupe | 2000 | 200 | 1 |
| dark_oak_leaves_dupe | 2000 | 200 | 1 |
| mangrove_leaves_dupe | 2000 | 200 | 1 |
| flowering_azalea_leaves_dupe | 2000 | 200 | 1 |

Extra Botany Revived 1.0.4 pinned; addon/effective recipe catalogue audit pending, not assumed covered. Relative mana-refined EMC custom conversion uses a native fake unit fixed before at 1 EMC in same file: +150/+300/+500 for manasteel/mana pearl/mana diamond from existing 3000/6000/10000 Mana infusion. Expected 168/1324/8692 EMC; graph conversion not guaranteed fixed output, competing paths and exact codecs must be checked. Retire these temporary conversions when #322 mapper owns the transitions. Parchment remains #319. Thermalily already frozen integrated source; preserve 75 Mana/tick for 600 ticks and zero cooldown, verify installed runtime and renewable lava loops. No change to #339 Orechid or EMC Catalyst/Source Jar.

## Evidence and acceptance

- NeoForge 1.21.1 [DataComponentIngredient](https://github.com/neoforged/NeoForge/blob/1.21.1/src/main/java/net/neoforged/neoforge/common/crafting/DataComponentIngredient.java): strict full default stack components; ingredient type in NeoForgeMod.
- [ProjectE defaults](https://github.com/sinkillerj/ProjectE/blob/mc1.21.1/src/datagen/generated/data/projecte/pe_custom_conversions/defaults.json) demonstrate fake unit and relative conversion; [BaseRecipeTypeMapper](https://github.com/sinkillerj/ProjectE/blob/mc1.21.1/src/main/java/moze_intel/projecte/emc/mappers/recipe/BaseRecipeTypeMapper.java) skips special recipes. Source-aligned only, exact PE pin and JAR verification still required.
- A: exact pinned IDs/codecs, effective datapacks and addon recipe catalogue, no duplicate/drain paths, strict ingredients, same-build EMC export, fuel parity and recompute if anchors differ; check indexed hashes and clean strict preflight.
- B/C: actual verified commands and gameplay verify every seed, altered components/exclusions, correct spent Tome remainder, multi-input and shift-click, automatic charging, near capacity, drain-free GUI, JEI, all conjuration outputs and measured mana debit, actual refined EMC and downstream costs. Verify no free stored-charge transfer, Thermalily and Orechid. No game validation performed.
- Integration writer owns scope/path ownership, manifest/index reconciliation against current locks, exact source hashes and CI; not integration-ready or live-test-ready. No full assembly or build authorized.

### 2026-11-18 survey-informed follow-up
Owner locked ordinary sulfur at 9 EMC (copper ingot/dust parity) on #320 and canonical ID neovitae:sulfur observed in #348. Add canonical seeded recipe: 1 sulfur + 9 charges -> 11 sulfur (10 net new, 90 EMC output vs 72 EMC charge cost, exactly 25% gain); choose ten extra rather than five to avoid rounding loss. Strict plain components and normal native charge/remainder semantics. Ancient Debris priced 6,291,456 EMC: 1 net extra requires 629,146 charges, incompatible with the approved 9,999 cap. Eligible in principle, recipe intentionally held until owner approves a capacity/alternative policy. No extra whitelist expansions, no charge cap change. Exact JAR/runtime, integration validation remain open.
