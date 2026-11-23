# Primal Magick 6.0.7 — full Research Table project-template options

**Exact version-aligned generator source:** [PrimalMagick@5977468bda968b994e54066236ec5719295ac9c3 `ProjectTemplates.java`](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/theorycrafting/ProjectTemplates.java). This second pass enumerates **all 38 bootstrap-registered templates and all 443 `.material(...)` option declarations**, preserving template-level requirements/nearby aid block conditions, material options and their flags, weight functions and alternate rewards. Does **not** claim that a particular random project with specific material choices will be generated for every player, or that gameplay has been tested.

## Key distinction for quests

[ProjectTemplate.java](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/theorycrafting/ProjectTemplate.java) uses template-wide research predicates, optional nearby aid blocks, eligibility/weight selection, and a default max of **4 selected material options**, with certain templates overriding that count. [Project.java](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/theorycrafting/Project.java) computes the displayed/actual chance from the chosen eligible material subset; selecting all offered options can reach **100% formula chance** if resources/eligibility allow it. **Individual `.material` options are independent weighted candidates** and may have their own `.requiredResearch(...)` or `.consumed()` flags; do not mistake the sum of 443 options for a single material bill or the template's mandatory prereqs.

`HIT_THE_BOOKS` is a concrete special template: it needs a **Bookshelf as a nearby aid block**, overrides selection to **1 material** and base chance to **0.5**, offers **a Book**, and awards **5 experience** as an extra reward. It should not be used as the unconditional first project if there is no Bookshelf near the Research Table.

The **`RITUAL_PRACTICE` template** is an instructive gating example: `quorumResearch(2, MANAFRUIT, RITUAL_CANDLES, INCENSE_BRAZIER, DOWSING_ROD)` means **2 of 4** research keys are needed *at template level*, while some options (apple/honey/Mana Salts/Candles/Incense/Dowsing Rod) each have **their own** `requiredResearch`. Requiring all four technologies or all 11 options as one quest would misrepresent this source.

## 38-template summary

| Native project template | Template-wide prerequisite(s) | Option count | Mandatory nearby aid block(s) (one eligible) | Offered option count | Base success chance | Additional reward |
| --- | --- | ---: | --- | --- | --- | --- |
| [`primalmagick:advanced_enchanting_studies`](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/theorycrafting/ProjectTemplates.java#L89) | `EXPERT_MANAWEAVING`, `PRIMALITE` | 14 | — | default 4 | source default | — |
| [`primalmagick:advanced_essence_analysis`](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/theorycrafting/ProjectTemplates.java#L108) | `CRYSTAL_SYNTHESIS` | 19 | — | default 4 | source default | — |
| [`primalmagick:advanced_ritual_practice`](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/theorycrafting/ProjectTemplates.java#L130) | `quorum(2, RITUAL_LECTERN, RITUAL_BELL, PRIMAL_SHOVEL, PRIMAL_FISHING_ROD, PRIMAL_AXE, PRIMAL_HOE, PRIMAL_PICKAXE)` | 16 | — | default 4 | source default | — |
| [`primalmagick:advanced_runework`](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/theorycrafting/ProjectTemplates.java#L150) | `quorum(3, RUNE_BLOOD, RUNE_INFERNAL, RUNE_VOID, RUNE_ABSORB, RUNE_DISPEL, RUNE_SUMMON, RUNE_AREA, RUNE_CREATURE, RUNE_INSIGHT)` | 13 | — | default 4 | source default | — |
| [`primalmagick:advanced_spellwork`](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/theorycrafting/ProjectTemplates.java#L167) | `EXPERT_SORCERY`, `SHARD_SYNTHESIS` | 14 | — | default 4 | source default | — |
| [`primalmagick:advanced_wand_tinkering`](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/theorycrafting/ProjectTemplates.java#L186) | `MASTER_MANAWEAVING` | 11 | — | default 4 | source default | — |
| [`primalmagick:apiamancy`](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/theorycrafting/ProjectTemplates.java#L200) | — | 1 | `Blocks.BEEHIVE` | 1 | 0.5D | `new ItemReward(Items.HONEYCOMB, 3)` |
| [`primalmagick:beacon_emanations`](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/theorycrafting/ProjectTemplates.java#L205) | — | 1 | `Blocks.BEACON` | 1 | 0.5D | — |
| [`primalmagick:brewing_experiments`](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/theorycrafting/ProjectTemplates.java#L209) | `DISCOVER_INFERNAL` | 17 | — | default 4 | source default | — |
| [`primalmagick:conduit_forces`](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/theorycrafting/ProjectTemplates.java#L229) | — | 1 | `Blocks.CONDUIT` | 1 | 0.5D | — |
| [`primalmagick:draconic_energies`](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/theorycrafting/ProjectTemplates.java#L233) | — | 1 | `Blocks.DRAGON_EGG` | 1 | 0.5D | — |
| [`primalmagick:draconic_memories`](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/theorycrafting/ProjectTemplates.java#L237) | — | 1 | `Blocks.DRAGON_HEAD`, `Blocks.DRAGON_WALL_HEAD` | 1 | 0.5D | — |
| [`primalmagick:enchanting_studies`](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/theorycrafting/ProjectTemplates.java#L241) | `BASIC_MANAWEAVING` | 14 | — | default 4 | source default | — |
| [`primalmagick:end_expedition`](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/theorycrafting/ProjectTemplates.java#L258) | `DISCOVER_VOID` | 11 | — | default 4 | source default | — |
| [`primalmagick:essence_analysis`](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/theorycrafting/ProjectTemplates.java#L272) | `BASIC_ALCHEMY` | 19 | — | default 4 | source default | — |
| [`primalmagick:expedition`](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/theorycrafting/ProjectTemplates.java#L294) | — | 11 | — | default 4 | source default | — |
| [`primalmagick:hit_the_books`](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/theorycrafting/ProjectTemplates.java#L308) | — | 1 | `Blocks.BOOKSHELF` | 1 | 0.5D | `new ExperienceReward(5)` |
| [`primalmagick:magitech_tinkering`](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/theorycrafting/ProjectTemplates.java#L313) | `quorum(3, ARCANOMETER, CONCOCTING_TINCTURES, ENTROPY_SINK, AUTO_CHARGER, ESSENCE_TRANSMUTER, DISSOLUTION_CHAMBER, INFERNAL_FURNACE)` | 12 | — | default 4 | source default | — |
| [`primalmagick:master_enchanting_studies`](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/theorycrafting/ProjectTemplates.java#L329) | `MASTER_MANAWEAVING`, `HEXIUM` | 14 | — | default 4 | source default | — |
| [`primalmagick:master_essence_analysis`](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/theorycrafting/ProjectTemplates.java#L348) | `CLUSTER_SYNTHESIS` | 19 | — | default 4 | source default | — |
| [`primalmagick:master_ritual_practice`](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/theorycrafting/ProjectTemplates.java#L370) | `quorum(2, BLOODLETTER, SOUL_ANVIL, FORBIDDEN_TRIDENT, FORBIDDEN_SWORD, FORBIDDEN_BOW)` | 13 | — | default 4 | source default | — |
| [`primalmagick:master_runework`](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/theorycrafting/ProjectTemplates.java#L387) | `RUNE_HALLOWED` | 15 | — | default 4 | source default | — |
| [`primalmagick:master_spellwork`](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/theorycrafting/ProjectTemplates.java#L405) | `MASTER_SORCERY`, `CRYSTAL_SYNTHESIS` | 13 | — | default 4 | source default | — |
| [`primalmagick:mundane_tinkering`](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/theorycrafting/ProjectTemplates.java#L423) | — | 10 | — | default 4 | source default | — |
| [`primalmagick:nether_expedition`](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/theorycrafting/ProjectTemplates.java#L436) | `DISCOVER_INFERNAL` | 12 | — | default 4 | source default | — |
| [`primalmagick:observation_analysis`](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/theorycrafting/ProjectTemplates.java#L451) | — | 1 | `BlocksPM.ANALYSIS_TABLE.get()` | 1 | 0.5D | — |
| [`primalmagick:piglin_barter`](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/theorycrafting/ProjectTemplates.java#L455) | `DISCOVER_INFERNAL` | 21 | — | default 4 | source default | `LootTableReward.builder(BuiltInLootTables.PIGLIN_BARTERING).description("label.primalmagick.loot_table.piglin_bartering.desc").build()` |
| [`primalmagick:portal_detritus`](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/theorycrafting/ProjectTemplates.java#L480) | — | 1 | `Blocks.NETHER_PORTAL` | 1 | 0.5D | — |
| [`primalmagick:prosperous_trade`](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/theorycrafting/ProjectTemplates.java#L484) | `DISCOVER_INFERNAL` | 23 | — | default 4 | source default | `LootTableReward.builder(LootTablesPM.THEORYCRAFTING_PROSPEROUS_TRADE).description("label.primalmagick.loot_table.prosperous_trade.desc").build()` |
| [`primalmagick:raiding_the_raiders`](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/theorycrafting/ProjectTemplates.java#L511) | — | 10 | — | default 4 | source default | — |
| [`primalmagick:recuperation`](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/theorycrafting/ProjectTemplates.java#L524) | — | 9 | — | default 4 | source default | — |
| [`primalmagick:redstone_tinkering`](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/theorycrafting/ProjectTemplates.java#L536) | `BASIC_MAGITECH` | 12 | — | default 4 | source default | — |
| [`primalmagick:rich_trade`](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/theorycrafting/ProjectTemplates.java#L551) | `DISCOVER_VOID` | 25 | — | default 4 | source default | `LootTableReward.builder(LootTablesPM.THEORYCRAFTING_RICH_TRADE).description("label.primalmagick.loot_table.rich_trade.desc").build()` |
| [`primalmagick:ritual_practice`](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/theorycrafting/ProjectTemplates.java#L580) | `quorum(2, MANAFRUIT, RITUAL_CANDLES, INCENSE_BRAZIER, DOWSING_ROD)` | 11 | — | default 4 | source default | — |
| [`primalmagick:runework`](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/theorycrafting/ProjectTemplates.java#L595) | `quorum(3, RUNE_EARTH, RUNE_SEA, RUNE_SKY, RUNE_SUN, RUNE_MOON, RUNE_PROJECT, RUNE_PROTECT, RUNE_ITEM, RUNE_SELF)` | 13 | — | default 4 | source default | — |
| [`primalmagick:spellwork`](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/theorycrafting/ProjectTemplates.java#L612) | `BASIC_SORCERY` | 11 | — | default 4 | source default | — |
| [`primalmagick:trade`](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/theorycrafting/ProjectTemplates.java#L626) | — | 21 | — | default 4 | source default | `LootTableReward.builder(LootTablesPM.THEORYCRAFTING_TRADE).description("label.primalmagick.loot_table.trade.desc").build()` |
| [`primalmagick:wand_tinkering`](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/theorycrafting/ProjectTemplates.java#L651) | `BASIC_MANAWEAVING` | 12 | — | default 4 | source default | — |

## 443 exact source material option declarations, grouped per template

Material descriptions below intentionally preserve Java item/tag constant tokens, not fictitious TMM tag resolutions. `PM:` abbreviates `ItemsPM.`, `#PM:` is `ItemTagsPM.`, and `#c:` is `CommonTags.Items.`. All optional flags in each expression are from the **source material builder**; e.g. `consumed` means an intended consumed option, not proof the current installed runtime actually deducts XP/Observation correctly. No custom external FE/MXP conversion is inferred.

### `primalmagick:advanced_enchanting_studies` — 14 candidate materials

**Research/conditions:** `EXPERT_MANAWEAVING`; `PRIMALITE`; **aid blocks:** none; **weighted generation:** `ProgressiveWeight.builder(5).modifier(MASTER_MANAWEAVING, -1).modifier(SUPREME_MANAWEAVING, -1).build()`; **reward multiplier:** `0.5D`; **material count override:** `default 4`; **base chance override:** `native derived default`. **Other rewards:** none.

**All source material options** (these are weighted *alternatives*, **not** an all-items-on-table checklist; each may have its own `consumed()` and extra research or reward predicates):
 - `ItemTag(#PM:ENCHANTING_TABLES) [weight 5]`
 - `ItemTag(#c:GEMS_LAPIS).quantity(2) consumed [weight 5]`
 - `Experience(2) consumed [bonus 0.25D] [weight 5]`
 - `Item(Items.BOOK) [weight 1]`
 - `Item(PM:PRIMALITE_SWORD) [weight 1]`
 - `Item(PM:PRIMALITE_PICKAXE) [weight 1]`
 - `Item(PM:PRIMALITE_SHOVEL) [weight 1]`
 - `Item(PM:PRIMALITE_AXE) [weight 1]`
 - `Item(PM:PRIMALITE_HOE) [weight 1]`
 - `Item(PM:PRIMALITE_HEAD) [weight 1]`
 - `Item(PM:PRIMALITE_CHEST) [weight 1]`
 - `Item(PM:PRIMALITE_LEGS) [weight 1]`
 - `Item(PM:PRIMALITE_FEET) [weight 1]`
 - `Observation(1) consumed [weight 5]`

### `primalmagick:advanced_essence_analysis` — 19 candidate materials

**Research/conditions:** `CRYSTAL_SYNTHESIS`; **aid blocks:** none; **weighted generation:** `ProgressiveWeight.builder(5).modifier(CLUSTER_SYNTHESIS, -2).build()`; **reward multiplier:** `0.5D`; **material count override:** `default 4`; **base chance override:** `native derived default`. **Other rewards:** none.

**All source material options** (these are weighted *alternatives*, **not** an all-items-on-table checklist; each may have its own `consumed()` and extra research or reward predicates):
 - `Observation(1) consumed [weight 3]`
 - `Item(PM:ESSENCE_SHARD_EARTH) consumed [weight 3]`
 - `Item(PM:ESSENCE_SHARD_SEA) consumed [weight 3]`
 - `Item(PM:ESSENCE_SHARD_SKY) consumed [weight 3]`
 - `Item(PM:ESSENCE_SHARD_SUN) consumed [weight 3]`
 - `Item(PM:ESSENCE_SHARD_MOON) consumed [weight 3]`
 - `Item(PM:ESSENCE_SHARD_BLOOD) consumed.requiredResearch(DISCOVER_BLOOD).afterCrafting(5) [weight 3]`
 - `Item(PM:ESSENCE_SHARD_INFERNAL) consumed.requiredResearch(DISCOVER_INFERNAL).afterCrafting(5) [weight 3]`
 - `Item(PM:ESSENCE_SHARD_VOID) consumed.requiredResearch(DISCOVER_VOID).afterCrafting(5) [weight 3]`
 - `Item(PM:ESSENCE_SHARD_HALLOWED) consumed.requiredResearch(DISCOVER_HALLOWED).afterCrafting(5) [weight 3]`
 - `Item(PM:ESSENCE_CRYSTAL_EARTH) consumed.afterCrafting(5) [bonus 0.25D] [weight 1]`
 - `Item(PM:ESSENCE_CRYSTAL_SEA) consumed.afterCrafting(5) [bonus 0.25D] [weight 1]`
 - `Item(PM:ESSENCE_CRYSTAL_SKY) consumed.afterCrafting(5) [bonus 0.25D] [weight 1]`
 - `Item(PM:ESSENCE_CRYSTAL_SUN) consumed.afterCrafting(5) [bonus 0.25D] [weight 1]`
 - `Item(PM:ESSENCE_CRYSTAL_MOON) consumed.afterCrafting(5) [bonus 0.25D] [weight 1]`
 - `Item(PM:ESSENCE_CRYSTAL_BLOOD) consumed.requiredResearch(DISCOVER_BLOOD).afterCrafting(5) [bonus 0.25D] [weight 1]`
 - `Item(PM:ESSENCE_CRYSTAL_INFERNAL) consumed.requiredResearch(DISCOVER_INFERNAL).afterCrafting(5) [bonus 0.25D] [weight 1]`
 - `Item(PM:ESSENCE_CRYSTAL_VOID) consumed.requiredResearch(DISCOVER_VOID).afterCrafting(5) [bonus 0.25D] [weight 1]`
 - `Item(PM:ESSENCE_CRYSTAL_HALLOWED) consumed.requiredResearch(DISCOVER_HALLOWED).afterCrafting(5) [bonus 0.25D] [weight 1]`

### `primalmagick:advanced_ritual_practice` — 16 candidate materials

**Research/conditions:** `quorumResearch(2, RITUAL_LECTERN, RITUAL_BELL, PRIMAL_SHOVEL, PRIMAL_FISHING_ROD, PRIMAL_AXE, PRIMAL_HOE, PRIMAL_PICKAXE)`; **aid blocks:** none; **weighted generation:** `ProgressiveWeight.builder(3).modifier(BASIC_RITUAL, 1).modifier(EXPERT_RITUAL, 1).modifier(MASTER_RITUAL, -1).modifier(SUPREME_RITUAL, -1).build()`; **reward multiplier:** `0.5D`; **material count override:** `default 4`; **base chance override:** `native derived default`. **Other rewards:** none.

**All source material options** (these are weighted *alternatives*, **not** an all-items-on-table checklist; each may have its own `consumed()` and extra research or reward predicates):
 - `Item(PM:RITUAL_ALTAR) [weight 10]`
 - `Item(PM:OFFERING_PEDESTAL) [weight 5]`
 - `Item(PM:REFINED_SALT, 2) consumed [weight 3]`
 - `Item(PM:RITUAL_LECTERN).requiredResearch(RITUAL_LECTERN) [weight 1]`
 - `Item(Items.ENCHANTED_BOOK).requiredResearch(RITUAL_LECTERN) [weight 1]`
 - `Item(PM:RITUAL_BELL).requiredResearch(RITUAL_BELL) [weight 1]`
 - `Item(PM:PRIMALITE_SHOVEL) consumed.requiredResearch(PRIMAL_SHOVEL) [bonus 0.25D] [weight 1]`
 - `Item(PM:ESSENCE_SHARD_EARTH) consumed.requiredResearch(PRIMAL_SHOVEL) [bonus 0.25D] [weight 1]`
 - `Item(PM:PRIMALITE_FISHING_ROD) consumed.requiredResearch(PRIMAL_FISHING_ROD) [bonus 0.25D] [weight 1]`
 - `Item(PM:ESSENCE_SHARD_SEA) consumed.requiredResearch(PRIMAL_FISHING_ROD) [bonus 0.25D] [weight 1]`
 - `Item(PM:PRIMALITE_AXE) consumed.requiredResearch(PRIMAL_AXE) [bonus 0.25D] [weight 1]`
 - `Item(PM:ESSENCE_SHARD_SKY) consumed.requiredResearch(PRIMAL_AXE) [bonus 0.25D] [weight 1]`
 - `Item(PM:PRIMALITE_HOE) consumed.requiredResearch(PRIMAL_HOE) [bonus 0.25D] [weight 1]`
 - `Item(PM:ESSENCE_SHARD_SUN) consumed.requiredResearch(PRIMAL_HOE) [bonus 0.25D] [weight 1]`
 - `Item(PM:PRIMALITE_PICKAXE) consumed.requiredResearch(PRIMAL_PICKAXE) [bonus 0.25D] [weight 1]`
 - `Item(PM:ESSENCE_SHARD_MOON) consumed.requiredResearch(PRIMAL_PICKAXE) [bonus 0.25D] [weight 1]`

### `primalmagick:advanced_runework` — 13 candidate materials

**Research/conditions:** `quorumResearch(3, RUNE_BLOOD, RUNE_INFERNAL, RUNE_VOID, RUNE_ABSORB, RUNE_DISPEL, RUNE_SUMMON, RUNE_AREA, RUNE_CREATURE, RUNE_INSIGHT)`; **aid blocks:** none; **weighted generation:** `ProgressiveWeight.builder(3).modifier(BASIC_RUNEWORKING, 1).modifier(EXPERT_RUNEWORKING, 1).modifier(MASTER_RUNEWORKING, -1).modifier(SUPREME_RUNEWORKING, -1).build()`; **reward multiplier:** `0.5D`; **material count override:** `default 4`; **base chance override:** `native derived default`. **Other rewards:** none.

**All source material options** (these are weighted *alternatives*, **not** an all-items-on-table checklist; each may have its own `consumed()` and extra research or reward predicates):
 - `Item(PM:RUNECARVING_TABLE) [weight 5]`
 - `Item(Items.STONE_SLAB, 2) consumed [weight 3]`
 - `ItemTag(#c:GEMS_LAPIS).quantity(2) consumed [weight 3]`
 - `Item(Items.DIAMOND_SWORD) [weight 1]`
 - `Item(PM:RUNE_BLOOD) consumed.requiredResearch(RUNE_BLOOD) [weight 1]`
 - `Item(PM:RUNE_INFERNAL) consumed.requiredResearch(RUNE_INFERNAL) [weight 1]`
 - `Item(PM:RUNE_VOID) consumed.requiredResearch(RUNE_VOID) [weight 1]`
 - `Item(PM:RUNE_INSIGHT) consumed.requiredResearch(RUNE_INSIGHT) [bonus 0.25D] [weight 1]`
 - `Item(PM:RUNE_ABSORB) consumed.requiredResearch(RUNE_ABSORB) [weight 1]`
 - `Item(PM:RUNE_DISPEL) consumed.requiredResearch(RUNE_DISPEL) [weight 1]`
 - `Item(PM:RUNE_SUMMON) consumed.requiredResearch(RUNE_SUMMON) [weight 1]`
 - `Item(PM:RUNE_AREA) consumed.requiredResearch(RUNE_AREA) [weight 1]`
 - `Item(PM:RUNE_CREATURE) consumed.requiredResearch(RUNE_CREATURE) [weight 1]`

### `primalmagick:advanced_spellwork` — 14 candidate materials

**Research/conditions:** `EXPERT_SORCERY`; `SHARD_SYNTHESIS`; **aid blocks:** none; **weighted generation:** `ProgressiveWeight.builder(5).modifier(MASTER_SORCERY, -1).modifier(SUPREME_SORCERY, -1).build()`; **reward multiplier:** `0.5D`; **material count override:** `default 4`; **base chance override:** `native derived default`. **Other rewards:** none.

**All source material options** (these are weighted *alternatives*, **not** an all-items-on-table checklist; each may have its own `consumed()` and extra research or reward predicates):
 - `Item(PM:SPELLCRAFTING_ALTAR) [weight 5]`
 - `Item(PM:WAND_INSCRIPTION_TABLE).requiredResearch(WAND_INSCRIPTION) [weight 2]`
 - `Item(PM:AUTO_CHARGER).requiredResearch(AUTO_CHARGER) [weight 2]`
 - `Item(PM:MUNDANE_WAND) [weight 1]`
 - `Item(PM:SPELL_SCROLL_BLANK, 2) consumed [weight 5]`
 - `Item(PM:ESSENCE_SHARD_EARTH) consumed [weight 1]`
 - `Item(PM:ESSENCE_SHARD_SEA) consumed [weight 1]`
 - `Item(PM:ESSENCE_SHARD_SKY) consumed [weight 1]`
 - `Item(PM:ESSENCE_SHARD_SUN) consumed [weight 1]`
 - `Item(PM:ESSENCE_SHARD_MOON) consumed [weight 1]`
 - `Item(PM:ESSENCE_SHARD_BLOOD) consumed.requiredResearch(DISCOVER_BLOOD) [weight 1]`
 - `Item(PM:ESSENCE_SHARD_INFERNAL) consumed.requiredResearch(DISCOVER_INFERNAL) [weight 1]`
 - `Item(PM:ESSENCE_SHARD_VOID) consumed.requiredResearch(DISCOVER_VOID) [weight 1]`
 - `Observation(1) consumed [weight 5]`

### `primalmagick:advanced_wand_tinkering` — 11 candidate materials

**Research/conditions:** `MASTER_MANAWEAVING`; **aid blocks:** none; **weighted generation:** `new ConstantWeight(5)`; **reward multiplier:** `0.5D`; **material count override:** `default 4`; **base chance override:** `native derived default`. **Other rewards:** none.

**All source material options** (these are weighted *alternatives*, **not** an all-items-on-table checklist; each may have its own `consumed()` and extra research or reward predicates):
 - `Item(PM:WAND_ASSEMBLY_TABLE).requiredResearch(ADVANCED_WANDMAKING) [weight 3]`
 - `Item(PM:WAND_GLAMOUR_TABLE).requiredResearch(WAND_GLAMOUR_TABLE) [weight 3]`
 - `Item(PM:HEARTWOOD, 2) consumed [weight 1]`
 - `ItemTag(#c:BONES) consumed [weight 1]`
 - `ItemTag(#c:RODS_BLAZE) consumed [weight 1]`
 - `Item(Items.PURPUR_BLOCK) consumed [weight 1]`
 - `ItemTag(#PM:INGOTS_PRIMALITE) consumed.requiredResearch(PRIMALITE) [weight 1]`
 - `ItemTag(#PM:INGOTS_HEXIUM) consumed.requiredResearch(HEXIUM) [weight 1]`
 - `Item(PM:ESSENCE_DUST_BLOOD) consumed.requiredResearch(DISCOVER_BLOOD) [weight 1]`
 - `Item(PM:ESSENCE_DUST_INFERNAL) consumed.requiredResearch(DISCOVER_INFERNAL) [weight 1]`
 - `Item(PM:ESSENCE_DUST_VOID) consumed.requiredResearch(DISCOVER_VOID) [weight 1]`

### `primalmagick:apiamancy` — 1 candidate materials

**Research/conditions:** no template-wide research predicate; **aid blocks:** `Blocks.BEEHIVE`; **weighted generation:** `new ConstantWeight(5)`; **reward multiplier:** `0.5D`; **material count override:** `1`; **base chance override:** `0.5D`. **Other rewards:** `new ItemReward(Items.HONEYCOMB, 3)`.

**All source material options** (these are weighted *alternatives*, **not** an all-items-on-table checklist; each may have its own `consumed()` and extra research or reward predicates):
 - `ItemTag(ItemTags.SMALL_FLOWERS) consumed [weight 1]`

### `primalmagick:beacon_emanations` — 1 candidate materials

**Research/conditions:** no template-wide research predicate; **aid blocks:** `Blocks.BEACON`; **weighted generation:** `new ConstantWeight(5)`; **reward multiplier:** `0.5D`; **material count override:** `1`; **base chance override:** `0.5D`. **Other rewards:** none.

**All source material options** (these are weighted *alternatives*, **not** an all-items-on-table checklist; each may have its own `consumed()` and extra research or reward predicates):
 - `ItemTag(ItemTags.BEACON_PAYMENT_ITEMS) consumed [weight 1]`

### `primalmagick:brewing_experiments` — 17 candidate materials

**Research/conditions:** `DISCOVER_INFERNAL`; **aid blocks:** none; **weighted generation:** `new ConstantWeight(5)`; **reward multiplier:** `0.5D`; **material count override:** `default 4`; **base chance override:** `native derived default`. **Other rewards:** none.

**All source material options** (these are weighted *alternatives*, **not** an all-items-on-table checklist; each may have its own `consumed()` and extra research or reward predicates):
 - `Item(Items.BREWING_STAND) [weight 5]`
 - `ItemTag(#c:CROPS_NETHER_WART) consumed [weight 3]`
 - `Item(Items.FERMENTED_SPIDER_EYE) consumed [weight 1]`
 - `ItemTag(#c:DUSTS_GLOWSTONE) consumed [weight 1]`
 - `ItemTag(#c:DUSTS_REDSTONE) consumed [weight 1]`
 - `Item(Items.SUGAR) consumed [weight 1]`
 - `Item(Items.RABBIT_FOOT) consumed [bonus 0.25D] [weight 0.5D]`
 - `Item(Items.BLAZE_POWDER) consumed [weight 1]`
 - `Item(Items.GLISTERING_MELON_SLICE) consumed [weight 1]`
 - `Item(Items.SPIDER_EYE) consumed [weight 1]`
 - `Item(Items.GHAST_TEAR) consumed [weight 1]`
 - `Item(Items.MAGMA_CREAM) consumed [weight 1]`
 - `Item(Items.PUFFERFISH) consumed [weight 1]`
 - `Item(Items.GOLDEN_CARROT) consumed [weight 1]`
 - `Item(Items.TURTLE_HELMET) consumed [bonus 0.25D] [weight 0.5D]`
 - `Item(Items.PHANTOM_MEMBRANE) consumed [bonus 0.25D] [weight 0.5D]`
 - `Observation(1) consumed [weight 3]`

### `primalmagick:conduit_forces` — 1 candidate materials

**Research/conditions:** no template-wide research predicate; **aid blocks:** `Blocks.CONDUIT`; **weighted generation:** `new ConstantWeight(5)`; **reward multiplier:** `0.5D`; **material count override:** `1`; **base chance override:** `0.5D`. **Other rewards:** none.

**All source material options** (these are weighted *alternatives*, **not** an all-items-on-table checklist; each may have its own `consumed()` and extra research or reward predicates):
 - `ItemTag(#c:GEMS_PRISMARINE) consumed [weight 1]`

### `primalmagick:draconic_energies` — 1 candidate materials

**Research/conditions:** no template-wide research predicate; **aid blocks:** `Blocks.DRAGON_EGG`; **weighted generation:** `new ConstantWeight(5)`; **reward multiplier:** `1.0D`; **material count override:** `1`; **base chance override:** `0.5D`. **Other rewards:** none.

**All source material options** (these are weighted *alternatives*, **not** an all-items-on-table checklist; each may have its own `consumed()` and extra research or reward predicates):
 - `ItemTag(#c:ENDER_PEARLS) consumed [weight 1]`

### `primalmagick:draconic_memories` — 1 candidate materials

**Research/conditions:** no template-wide research predicate; **aid blocks:** `Blocks.DRAGON_HEAD`, `Blocks.DRAGON_WALL_HEAD`; **weighted generation:** `new ConstantWeight(5)`; **reward multiplier:** `0.5D`; **material count override:** `1`; **base chance override:** `0.5D`. **Other rewards:** none.

**All source material options** (these are weighted *alternatives*, **not** an all-items-on-table checklist; each may have its own `consumed()` and extra research or reward predicates):
 - `Experience(3) consumed [weight 1]`

### `primalmagick:enchanting_studies` — 14 candidate materials

**Research/conditions:** `BASIC_MANAWEAVING`; **aid blocks:** none; **weighted generation:** `ProgressiveWeight.builder(5).modifier(EXPERT_MANAWEAVING, -1).modifier(MASTER_MANAWEAVING, -1).modifier(SUPREME_MANAWEAVING, -2).build()`; **reward multiplier:** `default 0.25`; **material count override:** `default 4`; **base chance override:** `native derived default`. **Other rewards:** none.

**All source material options** (these are weighted *alternatives*, **not** an all-items-on-table checklist; each may have its own `consumed()` and extra research or reward predicates):
 - `ItemTag(#PM:ENCHANTING_TABLES) [weight 5]`
 - `ItemTag(#c:GEMS_LAPIS) consumed [weight 5]`
 - `Experience(1) consumed [bonus 0.125D] [weight 5]`
 - `Item(Items.BOOK) [weight 1]`
 - `Item(Items.GOLDEN_SWORD) [weight 1]`
 - `Item(Items.GOLDEN_PICKAXE) [weight 1]`
 - `Item(Items.GOLDEN_SHOVEL) [weight 1]`
 - `Item(Items.GOLDEN_AXE) [weight 1]`
 - `Item(Items.GOLDEN_HOE) [weight 1]`
 - `Item(Items.GOLDEN_HELMET) [weight 1]`
 - `Item(Items.GOLDEN_CHESTPLATE) [weight 1]`
 - `Item(Items.GOLDEN_LEGGINGS) [weight 1]`
 - `Item(Items.GOLDEN_BOOTS) [weight 1]`
 - `Observation(1) consumed [weight 5]`

### `primalmagick:end_expedition` — 11 candidate materials

**Research/conditions:** `DISCOVER_VOID`; **aid blocks:** none; **weighted generation:** `new ConstantWeight(5)`; **reward multiplier:** `1.0D`; **material count override:** `default 4`; **base chance override:** `native derived default`. **Other rewards:** none.

**All source material options** (these are weighted *alternatives*, **not** an all-items-on-table checklist; each may have its own `consumed()` and extra research or reward predicates):
 - `Item(Items.NETHERITE_SWORD) [weight 1]`
 - `Item(Items.CROSSBOW) [weight 1]`
 - `Item(Items.ARROW, 32) consumed [weight 1]`
 - `Item(Items.NETHERITE_CHESTPLATE) [weight 1]`
 - `Item(Items.MAP) consumed [weight 1]`
 - `Item(Items.CARTOGRAPHY_TABLE) [weight 1]`
 - `Item(Items.TORCH, 32) consumed [weight 1]`
 - `Item(Items.BREAD, 8) consumed [weight 1]`
 - `Item(PotionContents.createItemStack(Items.POTION, Potions.SLOW_FALLING)) consumed [bonus 0.5D] [weight 1].matchNbt()`
 - `ItemTag(#c:ENDER_PEARLS).quantity(4) consumed [bonus 0.5D] [weight 3]`
 - `Item(Items.ENDER_EYE) consumed [weight 1]`

### `primalmagick:essence_analysis` — 19 candidate materials

**Research/conditions:** `BASIC_ALCHEMY`; **aid blocks:** none; **weighted generation:** `ProgressiveWeight.builder(5).modifier(CRYSTAL_SYNTHESIS, -2).modifier(CLUSTER_SYNTHESIS, -2).build()`; **reward multiplier:** `default 0.25`; **material count override:** `default 4`; **base chance override:** `native derived default`. **Other rewards:** none.

**All source material options** (these are weighted *alternatives*, **not** an all-items-on-table checklist; each may have its own `consumed()` and extra research or reward predicates):
 - `Observation(1) consumed [weight 5]`
 - `Item(PM:ESSENCE_DUST_EARTH) consumed [weight 3]`
 - `Item(PM:ESSENCE_DUST_SEA) consumed [weight 3]`
 - `Item(PM:ESSENCE_DUST_SKY) consumed [weight 3]`
 - `Item(PM:ESSENCE_DUST_SUN) consumed [weight 3]`
 - `Item(PM:ESSENCE_DUST_MOON) consumed [weight 3]`
 - `Item(PM:ESSENCE_DUST_BLOOD) consumed.requiredResearch(DISCOVER_BLOOD).afterCrafting(5) [weight 3]`
 - `Item(PM:ESSENCE_DUST_INFERNAL) consumed.requiredResearch(DISCOVER_INFERNAL).afterCrafting(5) [weight 3]`
 - `Item(PM:ESSENCE_DUST_VOID) consumed.requiredResearch(DISCOVER_VOID).afterCrafting(5) [weight 3]`
 - `Item(PM:ESSENCE_DUST_HALLOWED) consumed.requiredResearch(DISCOVER_HALLOWED).afterCrafting(5) [weight 3]`
 - `Item(PM:ESSENCE_SHARD_EARTH) consumed.requiredResearch(SHARD_SYNTHESIS).afterCrafting(5) [bonus 0.125D] [weight 1]`
 - `Item(PM:ESSENCE_SHARD_SEA) consumed.requiredResearch(SHARD_SYNTHESIS).afterCrafting(5) [bonus 0.125D] [weight 1]`
 - `Item(PM:ESSENCE_SHARD_SKY) consumed.requiredResearch(SHARD_SYNTHESIS).afterCrafting(5) [bonus 0.125D] [weight 1]`
 - `Item(PM:ESSENCE_SHARD_SUN) consumed.requiredResearch(SHARD_SYNTHESIS).afterCrafting(5) [bonus 0.125D] [weight 1]`
 - `Item(PM:ESSENCE_SHARD_MOON) consumed.requiredResearch(SHARD_SYNTHESIS).afterCrafting(5) [bonus 0.125D] [weight 1]`
 - `Item(PM:ESSENCE_SHARD_BLOOD) consumed.requiredResearch(SHARD_SYNTHESIS).requiredResearch(DISCOVER_BLOOD).afterCrafting(5) [bonus 0.125D] [weight 1]`
 - `Item(PM:ESSENCE_SHARD_INFERNAL) consumed.requiredResearch(SHARD_SYNTHESIS).requiredResearch(DISCOVER_INFERNAL).afterCrafting(5) [bonus 0.125D] [weight 1]`
 - `Item(PM:ESSENCE_SHARD_VOID) consumed.requiredResearch(SHARD_SYNTHESIS).requiredResearch(DISCOVER_VOID).afterCrafting(5) [bonus 0.125D] [weight 1]`
 - `Item(PM:ESSENCE_SHARD_HALLOWED) consumed.requiredResearch(SHARD_SYNTHESIS).requiredResearch(DISCOVER_HALLOWED).afterCrafting(5) [bonus 0.125D] [weight 1]`

### `primalmagick:expedition` — 11 candidate materials

**Research/conditions:** no template-wide research predicate; **aid blocks:** none; **weighted generation:** `ProgressiveWeight.builder(5).modifier(DISCOVER_INFERNAL, -2).modifier(DISCOVER_VOID, -2).build()`; **reward multiplier:** `default 0.25`; **material count override:** `default 4`; **base chance override:** `native derived default`. **Other rewards:** none.

**All source material options** (these are weighted *alternatives*, **not** an all-items-on-table checklist; each may have its own `consumed()` and extra research or reward predicates):
 - `Item(Items.IRON_SWORD) [weight 1]`
 - `Item(Items.BOW) [weight 1]`
 - `Item(Items.ARROW, 4) consumed [weight 1]`
 - `Item(Items.IRON_CHESTPLATE) [weight 1]`
 - `Item(Items.MAP) consumed [weight 1]`
 - `Item(Items.COMPASS) [weight 1]`
 - `Item(Items.CLOCK) [weight 1]`
 - `Item(Items.CARTOGRAPHY_TABLE) [weight 1]`
 - `Item(Items.CAMPFIRE) consumed [weight 1]`
 - `Item(Items.TORCH, 4) consumed [weight 1]`
 - `Item(Items.BREAD) consumed [weight 1]`

### `primalmagick:hit_the_books` — 1 candidate materials

**Research/conditions:** no template-wide research predicate; **aid blocks:** `Blocks.BOOKSHELF`; **weighted generation:** `new ConstantWeight(5)`; **reward multiplier:** `0.5D`; **material count override:** `1`; **base chance override:** `0.5D`. **Other rewards:** `new ExperienceReward(5)`.

**All source material options** (these are weighted *alternatives*, **not** an all-items-on-table checklist; each may have its own `consumed()` and extra research or reward predicates):
 - `Item(Items.BOOK) [weight 1]`

### `primalmagick:magitech_tinkering` — 12 candidate materials

**Research/conditions:** `quorumResearch(3, ARCANOMETER, CONCOCTING_TINCTURES, ENTROPY_SINK, AUTO_CHARGER, ESSENCE_TRANSMUTER, DISSOLUTION_CHAMBER, INFERNAL_FURNACE)`; **aid blocks:** none; **weighted generation:** `ProgressiveWeight.builder(3).modifier(MASTER_MAGITECH, 2).build()`; **reward multiplier:** `0.5D`; **material count override:** `default 4`; **base chance override:** `native derived default`. **Other rewards:** none.

**All source material options** (these are weighted *alternatives*, **not** an all-items-on-table checklist; each may have its own `consumed()` and extra research or reward predicates):
 - `Item(PM:MAGITECH_PARTS_BASIC) consumed [bonus 0.125D] [weight 3]`
 - `Item(PM:MAGITECH_PARTS_ENCHANTED) consumed.requiredResearch(EXPERT_MAGITECH) [bonus 0.25D] [weight 3]`
 - `Item(PM:MAGITECH_PARTS_FORBIDDEN) consumed.requiredResearch(MASTER_MAGITECH) [bonus 0.5D] [weight 3]`
 - `Item(PM:HONEY_EXTRACTOR).requiredResearch(HONEY_EXTRACTOR) [weight 1]`
 - `Item(PM:DESALINATOR).requiredResearch(DESALINATOR) [weight 1]`
 - `Item(PM:ARCANOMETER).requiredResearch(ARCANOMETER) [weight 1]`
 - `Item(PM:CONCOCTER).requiredResearch(CONCOCTING_TINCTURES) [weight 1]`
 - `Item(PM:ENTROPY_SINK).requiredResearch(ENTROPY_SINK) [weight 1]`
 - `Item(PM:AUTO_CHARGER).requiredResearch(AUTO_CHARGER) [weight 1]`
 - `Item(PM:ESSENCE_TRANSMUTER).requiredResearch(ESSENCE_TRANSMUTER) [weight 1]`
 - `Item(PM:DISSOLUTION_CHAMBER).requiredResearch(DISSOLUTION_CHAMBER) [weight 1]`
 - `Item(PM:INFERNAL_FURNACE).requiredResearch(INFERNAL_FURNACE) [weight 1]`

### `primalmagick:master_enchanting_studies` — 14 candidate materials

**Research/conditions:** `MASTER_MANAWEAVING`; `HEXIUM`; **aid blocks:** none; **weighted generation:** `new ConstantWeight(5)`; **reward multiplier:** `1D`; **material count override:** `default 4`; **base chance override:** `native derived default`. **Other rewards:** none.

**All source material options** (these are weighted *alternatives*, **not** an all-items-on-table checklist; each may have its own `consumed()` and extra research or reward predicates):
 - `ItemTag(#PM:ENCHANTING_TABLES) [weight 5]`
 - `ItemTag(#c:GEMS_LAPIS).quantity(3) consumed [weight 5]`
 - `Experience(3) consumed [bonus 0.5D] [weight 5]`
 - `Item(Items.BOOK) [weight 1]`
 - `Item(PM:HEXIUM_SWORD) [weight 1]`
 - `Item(PM:HEXIUM_PICKAXE) [weight 1]`
 - `Item(PM:HEXIUM_SHOVEL) [weight 1]`
 - `Item(PM:HEXIUM_AXE) [weight 1]`
 - `Item(PM:HEXIUM_HOE) [weight 1]`
 - `Item(PM:HEXIUM_HEAD) [weight 1]`
 - `Item(PM:HEXIUM_CHEST) [weight 1]`
 - `Item(PM:HEXIUM_LEGS) [weight 1]`
 - `Item(PM:HEXIUM_FEET) [weight 1]`
 - `Observation(1) consumed [weight 5]`

### `primalmagick:master_essence_analysis` — 19 candidate materials

**Research/conditions:** `CLUSTER_SYNTHESIS`; **aid blocks:** none; **weighted generation:** `new ConstantWeight(5)`; **reward multiplier:** `1.0D`; **material count override:** `default 4`; **base chance override:** `native derived default`. **Other rewards:** none.

**All source material options** (these are weighted *alternatives*, **not** an all-items-on-table checklist; each may have its own `consumed()` and extra research or reward predicates):
 - `Observation(1) consumed [weight 3]`
 - `Item(PM:ESSENCE_CRYSTAL_EARTH) consumed [weight 3]`
 - `Item(PM:ESSENCE_CRYSTAL_SEA) consumed [weight 3]`
 - `Item(PM:ESSENCE_CRYSTAL_SKY) consumed [weight 3]`
 - `Item(PM:ESSENCE_CRYSTAL_SUN) consumed [weight 3]`
 - `Item(PM:ESSENCE_CRYSTAL_MOON) consumed [weight 3]`
 - `Item(PM:ESSENCE_CRYSTAL_BLOOD) consumed.requiredResearch(DISCOVER_BLOOD).afterCrafting(5) [weight 3]`
 - `Item(PM:ESSENCE_CRYSTAL_INFERNAL) consumed.requiredResearch(DISCOVER_INFERNAL).afterCrafting(5) [weight 3]`
 - `Item(PM:ESSENCE_CRYSTAL_VOID) consumed.requiredResearch(DISCOVER_VOID).afterCrafting(5) [weight 3]`
 - `Item(PM:ESSENCE_CRYSTAL_HALLOWED) consumed.requiredResearch(DISCOVER_HALLOWED).afterCrafting(5) [weight 3]`
 - `Item(PM:ESSENCE_CLUSTER_EARTH) consumed.afterCrafting(5) [bonus 0.5D] [weight 1]`
 - `Item(PM:ESSENCE_CLUSTER_SEA) consumed.afterCrafting(5) [bonus 0.5D] [weight 1]`
 - `Item(PM:ESSENCE_CLUSTER_SKY) consumed.afterCrafting(5) [bonus 0.5D] [weight 1]`
 - `Item(PM:ESSENCE_CLUSTER_SUN) consumed.afterCrafting(5) [bonus 0.5D] [weight 1]`
 - `Item(PM:ESSENCE_CLUSTER_MOON) consumed.afterCrafting(5) [bonus 0.5D] [weight 1]`
 - `Item(PM:ESSENCE_CLUSTER_BLOOD) consumed.requiredResearch(DISCOVER_BLOOD).afterCrafting(5) [bonus 0.5D] [weight 1]`
 - `Item(PM:ESSENCE_CLUSTER_INFERNAL) consumed.requiredResearch(DISCOVER_INFERNAL).afterCrafting(5) [bonus 0.5D] [weight 1]`
 - `Item(PM:ESSENCE_CLUSTER_VOID) consumed.requiredResearch(DISCOVER_VOID).afterCrafting(5) [weight 1] [bonus 0.5D]`
 - `Item(PM:ESSENCE_CLUSTER_HALLOWED) consumed.requiredResearch(DISCOVER_HALLOWED).afterCrafting(5) [bonus 0.5D] [weight 1]`

### `primalmagick:master_ritual_practice` — 13 candidate materials

**Research/conditions:** `quorumResearch(2, BLOODLETTER, SOUL_ANVIL, FORBIDDEN_TRIDENT, FORBIDDEN_SWORD, FORBIDDEN_BOW)`; **aid blocks:** none; **weighted generation:** `new ConstantWeight(5)`; **reward multiplier:** `1D`; **material count override:** `default 4`; **base chance override:** `native derived default`. **Other rewards:** none.

**All source material options** (these are weighted *alternatives*, **not** an all-items-on-table checklist; each may have its own `consumed()` and extra research or reward predicates):
 - `Item(PM:RITUAL_ALTAR) [weight 10]`
 - `Item(PM:OFFERING_PEDESTAL) [weight 5]`
 - `Item(PM:REFINED_SALT, 4) consumed [weight 3]`
 - `Item(PM:BLOODLETTER).requiredResearch(BLOODLETTER) [weight 1]`
 - `Item(Items.WATER_BUCKET) consumed.requiredResearch(BLOODLETTER) [weight 1]`
 - `Item(PM:SOUL_ANVIL).requiredResearch(SOUL_ANVIL) [weight 1]`
 - `Item(PM:SPELLCLOTH).requiredResearch(SOUL_ANVIL) [weight 1]`
 - `Item(PM:HEXIUM_TRIDENT) consumed.requiredResearch(FORBIDDEN_TRIDENT) [bonus 0.5D] [weight 1]`
 - `Item(PM:ESSENCE_SHARD_BLOOD) consumed.requiredResearch(FORBIDDEN_TRIDENT) [bonus 0.5D] [weight 1]`
 - `Item(PM:HEXIUM_BOW) consumed.requiredResearch(FORBIDDEN_BOW) [bonus 0.5D] [weight 1]`
 - `Item(PM:ESSENCE_SHARD_INFERNAL) consumed.requiredResearch(FORBIDDEN_BOW) [bonus 0.5D] [weight 1]`
 - `Item(PM:HEXIUM_SWORD) consumed.requiredResearch(FORBIDDEN_SWORD) [bonus 0.5D] [weight 1]`
 - `Item(PM:ESSENCE_SHARD_VOID) consumed.requiredResearch(FORBIDDEN_SWORD) [bonus 0.5D] [weight 1]`

### `primalmagick:master_runework` — 15 candidate materials

**Research/conditions:** `RUNE_HALLOWED`; **aid blocks:** none; **weighted generation:** `new ConstantWeight(5)`; **reward multiplier:** `1D`; **material count override:** `default 4`; **base chance override:** `native derived default`. **Other rewards:** none.

**All source material options** (these are weighted *alternatives*, **not** an all-items-on-table checklist; each may have its own `consumed()` and extra research or reward predicates):
 - `Item(PM:RUNECARVING_TABLE) [weight 5]`
 - `Item(Items.STONE_SLAB, 4) consumed [weight 3]`
 - `ItemTag(#c:GEMS_LAPIS).quantity(4) consumed [weight 3]`
 - `Item(Items.DIAMOND_SWORD) [weight 1]`
 - `Item(PM:RUNE_HALLOWED) consumed.requiredResearch(RUNE_HALLOWED) [weight 3]`
 - `Item(PM:RUNE_POWER) consumed.requiredResearch(RUNE_POWER) [bonus 0.5D] [weight 1]`
 - `Item(PM:RUNE_PROJECT, 2) consumed.requiredResearch(RUNE_PROJECT) [weight 1]`
 - `Item(PM:RUNE_PROTECT, 2) consumed.requiredResearch(RUNE_PROTECT) [weight 1]`
 - `Item(PM:RUNE_ITEM, 2) consumed.requiredResearch(RUNE_ITEM) [weight 1]`
 - `Item(PM:RUNE_SELF, 2) consumed.requiredResearch(RUNE_SELF) [weight 1]`
 - `Item(PM:RUNE_ABSORB, 2) consumed.requiredResearch(RUNE_ABSORB) [weight 1]`
 - `Item(PM:RUNE_DISPEL, 2) consumed.requiredResearch(RUNE_DISPEL) [weight 1]`
 - `Item(PM:RUNE_SUMMON, 2) consumed.requiredResearch(RUNE_SUMMON) [weight 1]`
 - `Item(PM:RUNE_AREA, 2) consumed.requiredResearch(RUNE_AREA) [weight 1]`
 - `Item(PM:RUNE_CREATURE, 2) consumed.requiredResearch(RUNE_CREATURE) [weight 1]`

### `primalmagick:master_spellwork` — 13 candidate materials

**Research/conditions:** `MASTER_SORCERY`; `CRYSTAL_SYNTHESIS`; **aid blocks:** none; **weighted generation:** `new ConstantWeight(5)`; **reward multiplier:** `1D`; **material count override:** `default 4`; **base chance override:** `native derived default`. **Other rewards:** none.

**All source material options** (these are weighted *alternatives*, **not** an all-items-on-table checklist; each may have its own `consumed()` and extra research or reward predicates):
 - `Item(PM:SPELLCRAFTING_ALTAR) [weight 5]`
 - `Item(PM:WAND_INSCRIPTION_TABLE).requiredResearch(WAND_INSCRIPTION) [weight 2]`
 - `Item(PM:MANA_NEXUS).requiredResearch(MANA_NEXUS) [weight 2]`
 - `Item(PM:SPELL_SCROLL_BLANK, 4) consumed [weight 5]`
 - `Item(PM:ESSENCE_CRYSTAL_EARTH) consumed [weight 1]`
 - `Item(PM:ESSENCE_CRYSTAL_SEA) consumed [weight 1]`
 - `Item(PM:ESSENCE_CRYSTAL_SKY) consumed [weight 1]`
 - `Item(PM:ESSENCE_CRYSTAL_SUN) consumed [weight 1]`
 - `Item(PM:ESSENCE_CRYSTAL_MOON) consumed [weight 1]`
 - `Item(PM:ESSENCE_CRYSTAL_BLOOD) consumed.requiredResearch(DISCOVER_BLOOD) [weight 1]`
 - `Item(PM:ESSENCE_CRYSTAL_INFERNAL) consumed.requiredResearch(DISCOVER_INFERNAL) [weight 1]`
 - `Item(PM:ESSENCE_CRYSTAL_VOID) consumed.requiredResearch(DISCOVER_VOID) [weight 1]`
 - `Observation(1) consumed [weight 5]`

### `primalmagick:mundane_tinkering` — 10 candidate materials

**Research/conditions:** no template-wide research predicate; **aid blocks:** none; **weighted generation:** `ProgressiveWeight.builder(5).modifier(BASIC_MAGITECH, -1).modifier(EXPERT_MAGITECH, -1).modifier(MASTER_MAGITECH, -1).modifier(SUPREME_MAGITECH, -1).build()`; **reward multiplier:** `default 0.25`; **material count override:** `default 4`; **base chance override:** `native derived default`. **Other rewards:** none.

**All source material options** (these are weighted *alternatives*, **not** an all-items-on-table checklist; each may have its own `consumed()` and extra research or reward predicates):
 - `Item(Items.CRAFTING_TABLE) [weight 1]`
 - `ItemTag(ItemTags.ANVIL) [weight 1]`
 - `Item(Items.FURNACE) [weight 1]`
 - `Item(Items.BLAST_FURNACE) [weight 1]`
 - `Item(Items.LOOM) [weight 1]`
 - `Item(Items.SMOKER) [weight 1]`
 - `Item(Items.SMITHING_TABLE) [weight 1]`
 - `Item(Items.STONECUTTER) [weight 1]`
 - `Item(Items.GRINDSTONE) [weight 1]`
 - `Observation(1) consumed [weight 5]`

### `primalmagick:nether_expedition` — 12 candidate materials

**Research/conditions:** `DISCOVER_INFERNAL`; **aid blocks:** none; **weighted generation:** `ProgressiveWeight.builder(5).modifier(DISCOVER_VOID, -2).build()`; **reward multiplier:** `0.5D`; **material count override:** `default 4`; **base chance override:** `native derived default`. **Other rewards:** none.

**All source material options** (these are weighted *alternatives*, **not** an all-items-on-table checklist; each may have its own `consumed()` and extra research or reward predicates):
 - `Item(Items.DIAMOND_SWORD) [weight 1]`
 - `Item(Items.CROSSBOW) [weight 1]`
 - `Item(Items.ARROW, 16) consumed [weight 1]`
 - `Item(Items.DIAMOND_CHESTPLATE) [weight 1]`
 - `Item(Items.MAP) consumed [weight 1]`
 - `Item(Items.CARTOGRAPHY_TABLE) [weight 1]`
 - `Item(Items.TORCH, 16) consumed [weight 1]`
 - `Item(Items.BREAD, 4) consumed [weight 1]`
 - `Item(PotionContents.createItemStack(Items.POTION, Potions.FIRE_RESISTANCE)) consumed [bonus 0.25D] [weight 4].matchNbt()`
 - `ItemTag(ItemExtensionTags.MILK) consumed [weight 1]`
 - `ItemTag(#c:OBSIDIANS).quantity(10) [weight 1]`
 - `Item(Items.FLINT_AND_STEEL) [weight 1]`

### `primalmagick:observation_analysis` — 1 candidate materials

**Research/conditions:** no template-wide research predicate; **aid blocks:** `BlocksPM.ANALYSIS_TABLE.get()`; **weighted generation:** `new ConstantWeight(5)`; **reward multiplier:** `0.5D`; **material count override:** `1`; **base chance override:** `0.5D`. **Other rewards:** none.

**All source material options** (these are weighted *alternatives*, **not** an all-items-on-table checklist; each may have its own `consumed()` and extra research or reward predicates):
 - `Observation(1) consumed [weight 1]`

### `primalmagick:piglin_barter` — 21 candidate materials

**Research/conditions:** `DISCOVER_INFERNAL`; **aid blocks:** none; **weighted generation:** `new ConstantWeight(5)`; **reward multiplier:** `1D`; **material count override:** `default 4`; **base chance override:** `native derived default`. **Other rewards:** `LootTableReward.builder(BuiltInLootTables.PIGLIN_BARTERING).description("label.primalmagick.loot_table.piglin_bartering.desc").build()`.

**All source material options** (these are weighted *alternatives*, **not** an all-items-on-table checklist; each may have its own `consumed()` and extra research or reward predicates):
 - `Item(Items.BELL) consumed [weight 0.5D]`
 - `ItemTag(#c:STORAGE_BLOCKS_GOLD) consumed [bonus 0.5D] [weight 0.5D]`
 - `Item(Items.RAW_GOLD_BLOCK) consumed [bonus 0.5D] [weight 0.5D]`
 - `Item(Items.CLOCK) consumed [weight 1]`
 - `Item(Items.GILDED_BLACKSTONE) consumed [weight 2]`
 - `Item(Items.GLISTERING_MELON_SLICE) consumed [weight 2]`
 - `Item(Items.RAW_GOLD) consumed [weight 2]`
 - `ItemTag(#c:INGOTS_GOLD) consumed [weight 5]`
 - `Item(Items.GOLDEN_APPLE) consumed [weight 1]`
 - `Item(Items.GOLDEN_AXE) consumed [weight 1]`
 - `Item(Items.GOLDEN_BOOTS) consumed [weight 1]`
 - `Item(Items.GOLDEN_CARROT) consumed [weight 2]`
 - `Item(Items.GOLDEN_CHESTPLATE) consumed [weight 1]`
 - `Item(Items.GOLDEN_HELMET) consumed [weight 1]`
 - `Item(Items.GOLDEN_HOE) consumed [weight 1]`
 - `Item(Items.GOLDEN_HORSE_ARMOR) consumed [bonus 0.5D] [weight 0.5D]`
 - `Item(Items.GOLDEN_LEGGINGS) consumed [weight 1]`
 - `Item(Items.GOLDEN_PICKAXE) consumed [weight 1]`
 - `Item(Items.GOLDEN_SHOVEL) consumed [weight 1]`
 - `Item(Items.GOLDEN_SWORD) consumed [weight 1]`
 - `Item(Items.LIGHT_WEIGHTED_PRESSURE_PLATE) consumed [weight 1]`

### `primalmagick:portal_detritus` — 1 candidate materials

**Research/conditions:** no template-wide research predicate; **aid blocks:** `Blocks.NETHER_PORTAL`; **weighted generation:** `new ConstantWeight(5)`; **reward multiplier:** `0.5D`; **material count override:** `1`; **base chance override:** `0.5D`. **Other rewards:** none.

**All source material options** (these are weighted *alternatives*, **not** an all-items-on-table checklist; each may have its own `consumed()` and extra research or reward predicates):
 - `Item(PM:MAGNIFYING_GLASS) [weight 1]`

### `primalmagick:prosperous_trade` — 23 candidate materials

**Research/conditions:** `DISCOVER_INFERNAL`; **aid blocks:** none; **weighted generation:** `ProgressiveWeight.builder(5).modifier(DISCOVER_VOID, -2).build()`; **reward multiplier:** `0.5D`; **material count override:** `default 4`; **base chance override:** `native derived default`. **Other rewards:** `LootTableReward.builder(LootTablesPM.THEORYCRAFTING_PROSPEROUS_TRADE).description("label.primalmagick.loot_table.prosperous_trade.desc").build()`.

**All source material options** (these are weighted *alternatives*, **not** an all-items-on-table checklist; each may have its own `consumed()` and extra research or reward predicates):
 - `ItemTag(#c:GEMS_EMERALD).quantity(2) consumed [bonus 0.5D] [weight 2]`
 - `Item(Items.LAVA_BUCKET) consumed [weight 1]`
 - `ItemTag(#c:GEMS_DIAMOND) consumed [bonus 0.25D] [weight 3]`
 - `Item(Items.COAL, 2) consumed [weight 2]`
 - `Item(Items.MUTTON, 2) consumed [weight 1]`
 - `Item(Items.BEEF, 2) consumed [weight 1]`
 - `Item(Items.COMPASS) consumed [weight 1]`
 - `ItemTag(#c:INGOTS_GOLD) consumed [weight 1]`
 - `Item(Items.GLOWSTONE) consumed [weight 1]`
 - `Item(Items.MELON) consumed [weight 1]`
 - `Item(Items.CAKE) consumed [weight 1]`
 - `Item(Items.SALMON, 2) consumed [weight 1]`
 - `Item(Items.TROPICAL_FISH) consumed [weight 1]`
 - `Item(Items.FLINT, 3) consumed [weight 3]`
 - `ItemTag(#c:FEATHERS).quantity(3) consumed [weight 1]`
 - `Item(Items.RABBIT_HIDE) consumed [weight 1]`
 - `Item(Items.INK_SAC) consumed [weight 1]`
 - `Item(Items.WRITABLE_BOOK) consumed [weight 1]`
 - `Item(Items.GRANITE) consumed [weight 1]`
 - `Item(Items.ANDESITE) consumed [weight 1]`
 - `Item(Items.DIORITE) consumed [weight 1]`
 - `Item(Items.SHEARS) consumed [weight 1]`
 - `ItemTag(ItemTags.BEDS) consumed [weight 1]`

### `primalmagick:raiding_the_raiders` — 10 candidate materials

**Research/conditions:** no template-wide research predicate; **aid blocks:** none; **weighted generation:** `new ConstantWeight(5)`; **reward multiplier:** `0.5D`; **material count override:** `default 4`; **base chance override:** `native derived default`. **Other rewards:** none.

**All source material options** (these are weighted *alternatives*, **not** an all-items-on-table checklist; each may have its own `consumed()` and extra research or reward predicates):
 - `Item(Raid.getLeaderBannerInstance(bannerPatternGetter)) consumed.matchNbt() [bonus 0.25D] [weight 1]`
 - `Item(PM:BLOODY_FLESH) consumed [weight 1]`
 - `Item(Items.CROSSBOW) consumed [weight 3]`
 - `Item(Items.IRON_SWORD) consumed [weight 3]`
 - `Item(Items.IRON_AXE) consumed [weight 3]`
 - `Item(Items.ARROW, 4) consumed [weight 3]`
 - `Item(Items.GLASS_BOTTLE, 3) consumed [weight 3]`
 - `Item(Items.DARK_OAK_LOG, 16) consumed [weight 3]`
 - `Item(Items.CARVED_PUMPKIN) consumed [weight 1]`
 - `ItemTag(#c:GEMS_EMERALD) consumed [bonus 0.25D] [weight 1]`

### `primalmagick:recuperation` — 9 candidate materials

**Research/conditions:** no template-wide research predicate; **aid blocks:** none; **weighted generation:** `ProgressiveWeight.builder(5).modifier(DISCOVER_INFERNAL, -2).modifier(DISCOVER_VOID, -2).build()`; **reward multiplier:** `default 0.25`; **material count override:** `default 4`; **base chance override:** `native derived default`. **Other rewards:** none.

**All source material options** (these are weighted *alternatives*, **not** an all-items-on-table checklist; each may have its own `consumed()` and extra research or reward predicates):
 - `ItemTag(ItemTags.BEDS) [weight 2]`
 - `Item(Items.JUKEBOX) [weight 1]`
 - `Item(Items.BOOK) [weight 2]`
 - `ItemTag(#PM:FOOD_COOKED_BEEF) consumed [weight 1]`
 - `ItemTag(#PM:FOOD_BAKED_POTATO) consumed [weight 1]`
 - `ItemTag(ItemExtensionTags.MILK) consumed [weight 1]`
 - `Item(Items.CAKE) consumed [bonus 0.125D] [weight 1]`
 - `Item(Items.ROSE_BUSH) [weight 1]`
 - `Item(Items.TNT) consumed [bonus 0.125D] [weight 0.5D]`

### `primalmagick:redstone_tinkering` — 12 candidate materials

**Research/conditions:** `BASIC_MAGITECH`; **aid blocks:** none; **weighted generation:** `ProgressiveWeight.builder(5).modifier(BASIC_MAGITECH, -1).modifier(EXPERT_MAGITECH, -1).modifier(MASTER_MAGITECH, -1).modifier(SUPREME_MAGITECH, -1).build()`; **reward multiplier:** `default 0.25`; **material count override:** `default 4`; **base chance override:** `native derived default`. **Other rewards:** none.

**All source material options** (these are weighted *alternatives*, **not** an all-items-on-table checklist; each may have its own `consumed()` and extra research or reward predicates):
 - `Item(Items.DETECTOR_RAIL) [weight 1]`
 - `Item(Items.ACTIVATOR_RAIL) [weight 1]`
 - `Item(Items.DISPENSER) [weight 1]`
 - `Item(Items.DROPPER) [weight 1]`
 - `Item(Items.DAYLIGHT_DETECTOR) [weight 1]`
 - `Item(Items.PISTON) [weight 1]`
 - `Item(Items.HOPPER) [weight 1]`
 - `Item(Items.REDSTONE_LAMP) [weight 1]`
 - `Item(Items.STICKY_PISTON) [weight 1]`
 - `Item(Items.COMPARATOR) [weight 1]`
 - `ItemTag(#c:DUSTS_REDSTONE).quantity(4) consumed [weight 3]`
 - `Observation(1) consumed [weight 5]`

### `primalmagick:rich_trade` — 25 candidate materials

**Research/conditions:** `DISCOVER_VOID`; **aid blocks:** none; **weighted generation:** `new ConstantWeight(5)`; **reward multiplier:** `1D`; **material count override:** `default 4`; **base chance override:** `native derived default`. **Other rewards:** `LootTableReward.builder(LootTablesPM.THEORYCRAFTING_RICH_TRADE).description("label.primalmagick.loot_table.rich_trade.desc").build()`.

**All source material options** (these are weighted *alternatives*, **not** an all-items-on-table checklist; each may have its own `consumed()` and extra research or reward predicates):
 - `ItemTag(#c:GEMS_EMERALD).quantity(4) consumed [bonus 1D] [weight 2]`
 - `Item(Items.SHIELD) consumed [weight 1]`
 - `Item(Items.DIAMOND_CHESTPLATE) consumed [bonus 0.5D] [weight 1]`
 - `Item(Items.DRIED_KELP_BLOCK, 2) consumed [weight 1]`
 - `Item(Items.SWEET_BERRIES, 4) consumed [weight 1]`
 - `Item(Items.ITEM_FRAME) consumed [weight 1]`
 - `ItemTag(ItemTags.BANNERS) consumed [weight 2]`
 - `Item(Items.GLASS_BOTTLE, 3) consumed [weight 1]`
 - `ItemTag(#c:CROPS_NETHER_WART).quantity(4) consumed [weight 1]`
 - `Item(Items.GOLDEN_CARROT) consumed [weight 1]`
 - `Item(Items.GLISTERING_MELON_SLICE) consumed [weight 1]`
 - `Item(Items.PUFFERFISH) consumed [weight 1]`
 - `ItemTag(ItemTags.BOATS) consumed [weight 1]`
 - `Item(Items.TRIPWIRE_HOOK, 2) consumed [weight 1]`
 - `Item(Items.CROSSBOW) consumed [weight 1]`
 - `Item(Items.LEATHER_HORSE_ARMOR) consumed [weight 1]`
 - `Item(Items.SADDLE) consumed [bonus 0.5D] [weight 1]`
 - `Item(Items.CLOCK) consumed [weight 1]`
 - `Item(Items.NAME_TAG) consumed [bonus 0.5D] [weight 1]`
 - `ItemTag(#c:GEMS_QUARTZ).quantity(2) consumed [weight 1]`
 - `ItemTag(ItemTags.TERRACOTTA) consumed [weight 1]`
 - `Item(Items.PAINTING) consumed [weight 1]`
 - `Item(Items.DIAMOND_AXE) consumed [bonus 0.25D] [weight 1]`
 - `Item(Items.DIAMOND_PICKAXE) consumed [bonus 0.25D] [weight 1]`
 - `Item(Items.DIAMOND_SWORD) consumed [bonus 0.25D] [weight 1]`

### `primalmagick:ritual_practice` — 11 candidate materials

**Research/conditions:** `quorumResearch(2, MANAFRUIT, RITUAL_CANDLES, INCENSE_BRAZIER, DOWSING_ROD)`; **aid blocks:** none; **weighted generation:** `ProgressiveWeight.builder(5).modifier(EXPERT_RITUAL, -1).modifier(MASTER_RITUAL, -1).modifier(SUPREME_RITUAL, -2).build()`; **reward multiplier:** `default 0.25`; **material count override:** `default 4`; **base chance override:** `native derived default`. **Other rewards:** none.

**All source material options** (these are weighted *alternatives*, **not** an all-items-on-table checklist; each may have its own `consumed()` and extra research or reward predicates):
 - `Item(PM:RITUAL_ALTAR) [weight 10]`
 - `Item(PM:OFFERING_PEDESTAL) [weight 5]`
 - `Item(PM:REFINED_SALT) consumed [weight 3]`
 - `Item(Items.APPLE) consumed.requiredResearch(MANAFRUIT) [weight 1] [bonus 0.125D]`
 - `Item(Items.HONEY_BOTTLE) consumed.requiredResearch(MANAFRUIT) [weight 1] [bonus 0.125D]`
 - `Item(PM:MANA_SALTS) consumed.requiredResearch(MANAFRUIT) [weight 1] [bonus 0.125D]`
 - `ItemTag(#PM:RITUAL_CANDLES).requiredResearch(RITUAL_CANDLES) [weight 1]`
 - `Item(Items.FLINT_AND_STEEL).requiredResearch(RITUAL_CANDLES) [weight 1]`
 - `Item(PM:INCENSE_BRAZIER).requiredResearch(INCENSE_BRAZIER) [weight 1]`
 - `Item(PM:INCENSE_STICK) consumed.requiredResearch(INCENSE_BRAZIER) [weight 1]`
 - `Item(PM:DOWSING_ROD).requiredResearch(DOWSING_ROD) [weight 1]`

### `primalmagick:runework` — 13 candidate materials

**Research/conditions:** `quorumResearch(3, RUNE_EARTH, RUNE_SEA, RUNE_SKY, RUNE_SUN, RUNE_MOON, RUNE_PROJECT, RUNE_PROTECT, RUNE_ITEM, RUNE_SELF)`; **aid blocks:** none; **weighted generation:** `ProgressiveWeight.builder(5).modifier(EXPERT_RUNEWORKING, -1).modifier(MASTER_RUNEWORKING, -1).modifier(SUPREME_RUNEWORKING, -2).build()`; **reward multiplier:** `default 0.25`; **material count override:** `default 4`; **base chance override:** `native derived default`. **Other rewards:** none.

**All source material options** (these are weighted *alternatives*, **not** an all-items-on-table checklist; each may have its own `consumed()` and extra research or reward predicates):
 - `Item(PM:RUNECARVING_TABLE) [weight 5]`
 - `Item(Items.STONE_SLAB) consumed [weight 3]`
 - `ItemTag(#c:GEMS_LAPIS) consumed [weight 3]`
 - `Item(Items.DIAMOND_SWORD) [weight 1]`
 - `Item(PM:RUNE_EARTH) consumed.requiredResearch(RUNE_EARTH) [weight 1]`
 - `Item(PM:RUNE_SEA) consumed.requiredResearch(RUNE_SEA) [weight 1]`
 - `Item(PM:RUNE_SKY) consumed.requiredResearch(RUNE_SKY) [weight 1]`
 - `Item(PM:RUNE_SUN) consumed.requiredResearch(RUNE_SUN) [weight 1]`
 - `Item(PM:RUNE_MOON) consumed.requiredResearch(RUNE_MOON) [weight 1]`
 - `Item(PM:RUNE_PROJECT) consumed.requiredResearch(RUNE_PROJECT) [weight 1]`
 - `Item(PM:RUNE_PROTECT) consumed.requiredResearch(RUNE_PROTECT) [weight 1]`
 - `Item(PM:RUNE_ITEM) consumed.requiredResearch(RUNE_ITEM) [weight 1]`
 - `Item(PM:RUNE_SELF) consumed.requiredResearch(RUNE_SELF) [weight 1]`

### `primalmagick:spellwork` — 11 candidate materials

**Research/conditions:** `BASIC_SORCERY`; **aid blocks:** none; **weighted generation:** `ProgressiveWeight.builder(5).modifier(EXPERT_SORCERY, -1).modifier(MASTER_SORCERY, -1).modifier(SUPREME_SORCERY, -2).build()`; **reward multiplier:** `default 0.25`; **material count override:** `default 4`; **base chance override:** `native derived default`. **Other rewards:** none.

**All source material options** (these are weighted *alternatives*, **not** an all-items-on-table checklist; each may have its own `consumed()` and extra research or reward predicates):
 - `Item(PM:SPELLCRAFTING_ALTAR) [weight 5]`
 - `Item(PM:WAND_INSCRIPTION_TABLE).requiredResearch(WAND_INSCRIPTION) [weight 2]`
 - `Item(PM:WAND_CHARGER).requiredResearch(WAND_CHARGER) [weight 2]`
 - `Item(PM:MUNDANE_WAND) [weight 1]`
 - `Item(PM:SPELL_SCROLL_BLANK) consumed [weight 5]`
 - `Item(PM:ESSENCE_DUST_EARTH) consumed [weight 1]`
 - `Item(PM:ESSENCE_DUST_SEA) consumed [weight 1]`
 - `Item(PM:ESSENCE_DUST_SKY) consumed [weight 1]`
 - `Item(PM:ESSENCE_DUST_SUN) consumed [weight 1]`
 - `Item(PM:ESSENCE_DUST_MOON) consumed [weight 1]`
 - `Observation(1) consumed [weight 5]`

### `primalmagick:trade` — 21 candidate materials

**Research/conditions:** no template-wide research predicate; **aid blocks:** none; **weighted generation:** `ProgressiveWeight.builder(5).modifier(DISCOVER_INFERNAL, -2).modifier(DISCOVER_VOID, -2).build()`; **reward multiplier:** `default 0.25`; **material count override:** `default 4`; **base chance override:** `native derived default`. **Other rewards:** `LootTableReward.builder(LootTablesPM.THEORYCRAFTING_TRADE).description("label.primalmagick.loot_table.trade.desc").build()`.

**All source material options** (these are weighted *alternatives*, **not** an all-items-on-table checklist; each may have its own `consumed()` and extra research or reward predicates):
 - `ItemTag(#c:GEMS_EMERALD) consumed [bonus 0.25D] [weight 2]`
 - `Item(Items.COAL, 2) consumed [weight 3]`
 - `ItemTag(#c:INGOTS_IRON) consumed [weight 3]`
 - `Item(Items.CHICKEN, 2) consumed [weight 1]`
 - `Item(Items.PORKCHOP) consumed [weight 1]`
 - `Item(Items.PAPER, 4) consumed [weight 2]`
 - `ItemTag(#c:GLASS_PANES).quantity(2) consumed [weight 1]`
 - `Item(Items.ROTTEN_FLESH, 4) consumed [weight 1]`
 - `ItemTag(#c:CROPS_WHEAT).quantity(3) consumed [weight 1]`
 - `ItemTag(#c:CROPS_POTATO).quantity(3) consumed [weight 1]`
 - `ItemTag(#c:CROPS_CARROT).quantity(3) consumed [weight 1]`
 - `ItemTag(#c:CROPS_BEETROOT).quantity(3) consumed [weight 1]`
 - `ItemTag(#c:STRINGS).quantity(3) consumed [weight 2]`
 - `Item(Items.COD, 2) consumed [weight 1]`
 - `ItemTag(#c:RODS_WOODEN).quantity(4) consumed [weight 1]`
 - `ItemTag(#c:LEATHERS) consumed [weight 1]`
 - `Item(Items.BOOK) consumed [weight 1]`
 - `Item(Items.CLAY_BALL, 2) consumed [weight 1]`
 - `Item(Items.STONE, 2) consumed [weight 1]`
 - `ItemTag(ItemTags.WOOL).quantity(2) consumed [weight 1]`
 - `ItemTag(#c:DYES).quantity(2) consumed [weight 1]`

### `primalmagick:wand_tinkering` — 12 candidate materials

**Research/conditions:** `BASIC_MANAWEAVING`; **aid blocks:** none; **weighted generation:** `ProgressiveWeight.builder(5).modifier(MASTER_MANAWEAVING, -2).build()`; **reward multiplier:** `default 0.25`; **material count override:** `default 4`; **base chance override:** `native derived default`. **Other rewards:** none.

**All source material options** (these are weighted *alternatives*, **not** an all-items-on-table checklist; each may have its own `consumed()` and extra research or reward predicates):
 - `Item(PM:WAND_ASSEMBLY_TABLE).requiredResearch(ADVANCED_WANDMAKING) [weight 3]`
 - `Item(PM:HEARTWOOD) consumed [weight 1]`
 - `ItemTag(#c:OBSIDIANS) consumed [weight 1]`
 - `ItemTag(#PM:CORAL_BLOCKS) consumed [weight 1]`
 - `Item(Items.BAMBOO) consumed [weight 1]`
 - `ItemTag(#PM:SUNWOOD_LOGS) consumed [weight 1]`
 - `ItemTag(#PM:MOONWOOD_LOGS) consumed [weight 1]`
 - `ItemTag(#c:INGOTS_IRON) consumed [weight 1]`
 - `ItemTag(#c:INGOTS_GOLD) consumed [weight 1]`
 - `ItemTag(#c:GEMS_DIAMOND) consumed [weight 1]`
 - `ItemTag(#PM:ESSENCES_TERRESTRIAL_DUSTS) consumed [weight 1]`
 - `Observation(1) consumed [weight 5]`


## Remaining boundary

Actual Research Table generated offerings depend on nearby aid blocks, player research, weights, materials and items present in the configured pack. The original Mod Comprehension profile identified possible XP/Observation consumed-flag edge cases—**validate in an authorized Minecraft client before asserting a bug or exact item deletion**. This source inventory is suitable for designing multiple *representative* research quests, not binding one precise random project to every player.
