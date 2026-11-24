# Arcane Archives: Reborn 1.0.2 — pinned release JAR, recipes, registries and Tome Atlas

**Status: release binary/data/selected bytecode VERIFIED, NOT a gameplay/pack-integration claim.** User supplied the exact pinned release `ArcaneArchives-1.0.2+Neoforge-1.21.1.jar`. A **read-only local** archive and JDK `javap -c -p` review on 2026-11-23 verified JAR bytes, 1.0.2 bundled data and core storage/route/permission method implementations. This is a *third-party code research* record, not a QA checklist.

## 1. Binary provenance and version lock

| Property | Exact verified result |
| --- | --- |
| Packwiz | [`mods/arcane-archives-reborn.pw.toml`](../../../mods/arcane-archives-reborn.pw.toml), manifest ID `arcane-archives-reborn` |
| Filename | `ArcaneArchives-1.0.2+Neoforge-1.21.1.jar` |
| Bytes | **3,192,671** |
| SHA-1 | **`082f5f560981c04c487b9c1f5e5ec9976f8ec2db`**, **exact Packwiz match** |
| SHA-256 | `ad3ce7609f9de5d24387245a68a061e55726e88143b5cb9913a9f744f43bfed4` |
| Archive integrity | **993 ZIP entries**, no duplicates, CRC scan PASS |
| Java classes | **130** actual `.class` files, all in the pinned artifact |
| Embedded NeoForge identity | `META-INF/neoforge.mods.toml`: `arcanearchives` **1.0.2**, Minecraft **[1.21.1]**, NeoForge **[21.1.251,)**, Modonomicon **[1.120.5,)**, **both sides**, license **MIT** |
| Recipe JSON files | **34** under `data/arcanearchives/recipe/`: **14** conventional Minecraft crafting + **20** Gem Cutting |
| Historical legacy recipe index | `data/arcanearchives/gemcutting.json`: **20 records identical** to the 20 individually bundled Gem Cutting recipes' output/count/ingredients; **do not double count as 40** |
| Native guide | `data/arcanearchives/modonomicon/books/tome_arcana/`: **5 categories**, **40 entry JSONs** |
| Native advancement data | **24 JSONs** (root +23 named advancements); native rewards are not FTB quests or 24 mandatory gameplay gates |
| English localization | **782 keys** in `assets/arcanearchives/lang/en_us.json`; **legacy text is not registry evidence** |

Source-level method evidence: `com.arcanearchives.reborn.ArcaneArchives`, `ArchiveConfig`, `ArchiveBlockEntity`, `ArchiveNetworks`, `ArchiveTransfers`, `AmphoraTransfers`, `Translocation`, `GemRecipes`, `GemcuttingRecipe`, `ArchiveEvents`, `ArchiveLetters`, `ManifestIndex`, `transfer/transaction/Transaction`, and bundled internal test suites `ArchiveGameTests`, `ParityGameTests`, `ReleaseGameTests`. This verifies the published bytecode class/method declarations and selected arithmetic, **not every path in every method**.

## 2. Actual registered items, blocks and retired-localization distinction

The compiled `ArcaneArchives` static registration contains **18 named non-block items**:

`raw_quartz`, `shaped_quartz`, `radiant_dust`, `scintillating_inlay`, `matrix_brace`, `material_interface`, `containment_field`, `devouring_charm`, `manifest`, `tome_arcana`, `scepter_revelation`, `scepter_manipulation`, `scepter_translocation`, `radiant_key`, `letter_invitation`, `letter_resignation`, `writ_expulsion`, `radiant_amphora`.

All have namespace prefix `arcanearchives:`. The block registry contains **8 primary functional machines/containers**—`radiant_resonator`, `radiant_chest`, `gemcutters_table`, `radiant_trove`, `radiant_tank`, `radiant_crafting_table`, `brazier_of_hoarding`, `lectern_manifest`—plus separately registered `monitoring_crystal`, **5** resource/lighting blocks (`storage_raw_quartz`, `storage_shaped_quartz`, `raw_quartz_cluster`, `quartz_sliver`, `radiant_lantern`) and the hidden/internal auxiliary part block `fake_air`. Total **15 block registrations** including internal part. The 14 user-facing block registry names, but not `fake_air`, appear in the native creative/useful content.

**Do not promote dangling translation/model names into release mechanics.** `en_us.json` also contains old names like `radiant_furnace`, `matrix_repository`, `geomancy_pendulum`, `slaughtergleam`, `serenity_charm`, `fabrial`, `spiritorb`, etc., and the assets may retain old models. **None is registered by the compiled 1.0.2 `ArcaneArchives` item/block registry**; mere translation presence is not evidence of obtainable functioning content. This is a critical correction against legacy 1.12 source assumptions.

## 3. Exact native production and limits — guide plus compiled code

| Native object/system | Actual pinned 1.0.2 parameters / operation | Source |
| --- | --- | --- |
| Resonator | One clear block **above** the placed resonator, owner **online**, chunk **loaded**; generates a Raw Quartz Cluster after **6,000 ticks ≈5 minutes** at nominal 20 TPS. Default **3 resonators/player, including unloaded ones**; comparator shows progress, 15 when cluster present. Silk Touch yields the cluster item; normal break obtains quartz | `ArchiveConfig.RESONATOR_TICKS=6000`, `RESONATOR_LIMIT=3`, `ArchiveBlockEntity.tick`, `start/resonator` |
| Radiant Chest | **54** slots; each holds **`item.getMaxStackSize() × radiantMultiplier`**, config multiplier default **4** (range **1–8**); standard 64-stack item -> **256/slot**. Sneak-use Raw Quartz on normal wooden chest as an alternative conversion preserving its contents | `ArchiveBlockEntity` constructor and `getMaxStackSize(ItemStack)`; `ArchiveConfig`; `start/first_chest`; `ArchiveConversion` |
| Trove | **One item type and Data Components signature**; **32,768** items at base (1,024×32/64? See note below), single virtual bulk slot; right-click deposits, double-right-click matches inventory; left-click front/top takes one or sneak-left-click a stack. Empty Trove forgets filter unless Radiant Key installed | `ArchiveBlockEntity.getMaxStackSize()`, `canPlaceItem`, `updateFilter`; `storage/troves` |
| Radiant Tank | **One fluid type**, base **16,000 mB =16 buckets**, fluid insertion/extraction from any side; breaking preserves stored fluid/upgrades; comparator reports fill | `ArchiveBlockEntity$1` constructor initialized 16,000, `storage/tanks`, capability registration |
| Main upgrades | **0, 1, 2, 3 installed tiers** yield **1×, 3×, 6×, 10×** base. Order: **Matrix Brace**, then **Material Interface (Trove)**/**Containment Field (Tank)**, then **Shaped Quartz Block**. Remove in reverse order only when remainder fits. | `ArchiveBlockEntity.multiplier()`, `storage/upgrades` |
| Trove numeric tiers | **32,768 → 98,304 → 196,608 → 327,680 items** | Direct JAR guide `storage/upgrades`; bytecode `32768 × multiplier()` |
| Tank numeric tiers | **16 → 48 → 96 → 160 buckets**, i.e. 16,000–160,000 mB | Direct JAR guide `storage/upgrades`; bytecode `16000 × multiplier()` |
| Optional Trove/Tank slots | Up to three optional slots. **Radiant Key** keeps an empty Trove assigned to its item; **Devouring Charm** destroys applicable overflow but adds zero capacity. Only one of each type accepted | `ArchiveBlockEntity.optionalUpgrade`, `syncOptionalUpgrades`; `tools/radiant_key`, `tools/devouring` |
| Brazier | Accepts thrown or right-clicked stacks, including hopper/pipe delivery, **no fuel**. Double-use deposits matching player inventory. Routes only to **loaded, reachable** destinations **in current dimension**, default **150 horizontal blocks**, configurable **0–300**, ignoring height; can restrict to personal rather than shared endpoints | `ArchiveBlockEntity.radius=150`, `ArchiveNetworks.routingTargets`, `networks/brazier`, `networks/range` |
| Routing precedence | Stocked Gem Cutters and Chests set “existing items only” first; matching Troves next (normal/Devouring capacity-aware); ordinary Chests next; rejecting items remain at source. A Trove with no saved item identity is skipped. | `ArchiveNetworks.routingWeight` and `route`; `networks/routing` |
| Manifest | Searches **loaded** network storage, across dimensions, supports name/ID, `@mod`, `#tag`, sorting and per-dimension filters. Shows counts/locations, **does not withdraw or teleport** inventory; tracking lines target matches in the current dimension and can show slot overlays | `ManifestIndex`, `ManifestMenu`, `networks/manifest`, `networks/tracking` |
| Monitoring Crystal | **Sneak/right-click** on compatible normal/modded chest/container face to include it in Manifest inventory monitoring. Normal right-click still opens container. Double chests de-duplicated; unloaded storage not visible. Radiant containers are indexed automatically. | `MonitoringCrystalItem`, `networks/monitoring` |
| Manifest Lectern | Two-block-high fixed Manifest UI, either half usable; requires clear space above | `networks/lectern`, `lectern_manifest` regular recipe |
| Radiant Crafting Table | Native vanilla-grid crafting, grid **persists** on close; saved **3 recipe bookmarks** from code, left-click bookmark crafts once if materials present in grid/player inventory; JEI recipe transfer supported | `ArchiveBlockEntity.savedRecipes`, `start/crafting_table` |
| Gem Cutter's Table | **18 item-storage slots** and specialized Gem Cutting recipe selection. Ingredients must be inside the table; order irrelevant. Selected recipe persists; unused ingredients stay. Output can be shift-clicked to inventory; bright/dark UI indicates available/unavailable. No FE/Mana cost in packaged Gem Cutting JSON. | `ArchiveBlockEntity` constructor (18), `GemRecipes`, `GemMenu`, `start/gemcutting` |
| Amphora | Sneak-use Tank to link; no own reservoir; use on source to collect into linked **loaded, accessible** Tank; sneak-left-click block toggles between collect/place; dispenser and compatible fluid capability access operate via linked Tank; relink after Tank move | `AmphoraTransfers`, `tools/amphora` |
| Scepter of Translocation | Sneak-use device to select it; regular use on block face to relocate while preserving owner, contents, upgrades and progress. **≤64 blocks**, **same dimension**, both locations loaded; supports two-block devices with clearance; cancel by sneak-use in air | `Translocation`, `tools/translocation` |
| Scepter of Manipulation | Open upgrade/config slots in Troves/Tanks and routing settings in Brazier; label Chest with displayed item in other hand; toggle existing-items-only option | `ArchiveBlockEntity`, `tools/manipulation`, `tools/chest_labels` |
| Scepter of Revelation | Read target device state/status into chat: stored amount, capacity, growth, empty space, facing. Access check applies | `Revelation`, `tools/revelation` |
| Devouring Charm | Held GUI: **six disposal slots** destroyed on close; fluid container slot drains fluid and returns empty. Pickup filters can destroy selected items while carried; installed as overflow mode on appropriate Trove/Tank | `DevouringMenu`, `tools/devouring` |
| Shared networks | Devices retain original **owner UUID**; saved network membership allows members to access each other's **loaded** devices; invitation signed by maker, resignation and expulsion via signed letters/writs (**3.2 s use /64 ticks**); leader resignation dissolves membership. A change in sharing **does not transfer device ownership** | `ArchiveNetworks` saved membership, `ArchiveLetters`, `sharing/sharing`, `sharing/invitation`, `sharing/resignation`, `sharing/expulsion` |

**Trove translation correction:** the publisher loosely describes “1,024 stacks”, while the actual JAR uses a **raw numeric 32,768 count** (equivalent to 512 normal 64-count stacks or 1,024 32-count stacks). **Always present the compiled numeric item capacity**, not an invented “1,024×64” number. The first-pass dossier's “up to 1,024 stacks” claim was therefore **misleading without specifying stack size** and is superseded.

### Safety and implementation boundaries (not testing demands)

The compiled `ArchiveBlockEntity.canAccess(Player)` consults `ArchiveNetworks.same(owner, player UUID)` for owned, server-level devices. `ArchiveNetworks.route` processes candidate insertions using a scoped `Transaction.openRoot()` and `commit()` and returns an actual remainder; this establishes **intentional transactional routing paths**, not a proof that every cross-mod adapter can never dupe. Native batch capacity removal and portable item serialization are present (`PortableStorage`, `ArchiveBlockEntity.saveAdditional/loadAdditional`). Cross-mod FTB Chunks, ProjectE EMC and container nesting are separate TMM interactions, not defects to assume in third-party released code.

No original dedicated native **mana/FE supply** appears in the registry, crafting recipe costs or essential device interactions; the Resonator uses player/chunk time rather than spending a defined energy token. **Raw Quartz is generated by a placed Resonator, not an underground ore vein** in the published generated-data inventory.

## 4. All 20 Gem Cutting recipes, exact result count and typed ingredients

All rows are directly recorded in `data/arcanearchives/recipe/gemcutting/<recipe>.json` and precisely mirrored by `data/arcanearchives/gemcutting.json`. Prefix unqualified inputs/outputs with `arcanearchives:`; `#` denotes an item tag, and `minecraft:` references are explicit. Source reports counts consumed, not stack-position requirements.

| Gem Cutting ID | Output qty | Exact input ingredients and counts |
| --- | ---: | --- |
| `arcanearchives:gemcutting/radiant_dust` → `arcanearchives:radiant_dust` | 2 | 1 raw_quartz |
| `arcanearchives:gemcutting/shaped_quartz` → `arcanearchives:shaped_quartz` | 1 | 2 raw_quartz |
| `arcanearchives:gemcutting/manifest` → `arcanearchives:manifest` | 1 | 1 #c:paper + 1 #c:dyes/black + 2 radiant_dust |
| `arcanearchives:gemcutting/letter_invitation` → `arcanearchives:letter_invitation` | 1 | 3 #c:paper + 1 radiant_dust + 1 #c:dyes/light_blue |
| `arcanearchives:gemcutting/letter_resignation` → `arcanearchives:letter_resignation` | 1 | 3 #c:paper + 1 radiant_dust + 1 #c:dyes/pink |
| `arcanearchives:gemcutting/writ_expulsion` → `arcanearchives:writ_expulsion` | 1 | 3 #c:paper + 1 radiant_dust + 1 #c:dyes/red |
| `arcanearchives:gemcutting/scepter_manipulation` → `arcanearchives:scepter_manipulation` | 1 | 1 scepter_revelation + 1 shaped_quartz |
| `arcanearchives:gemcutting/scepter_translocation` → `arcanearchives:scepter_translocation` | 1 | 1 scepter_revelation + 1 material_interface |
| `arcanearchives:gemcutting/radiant_lantern` → `arcanearchives:radiant_lantern` | 4 | 2 raw_quartz + 1 #c:nuggets/gold |
| `arcanearchives:gemcutting/radiant_trove` → `arcanearchives:radiant_trove` | 4 | 2 shaped_quartz + 1 material_interface + 1 radiant_chest |
| `arcanearchives:gemcutting/radiant_tank` → `arcanearchives:radiant_tank` | 4 | 2 shaped_quartz + 1 containment_field + 1 #c:ingots/gold |
| `arcanearchives:gemcutting/monitoring_crystal` → `arcanearchives:monitoring_crystal` | 1 | 1 shaped_quartz + 2 #c:nuggets/gold + 4 #c:rods/wooden |
| `arcanearchives:gemcutting/scintillating_inlay` → `arcanearchives:scintillating_inlay` | 1 | 6 radiant_dust + 12 #c:dusts/redstone + 1 #c:ingots/gold + 6 #c:nuggets/gold |
| `arcanearchives:gemcutting/brazier_of_hoarding` → `arcanearchives:brazier_of_hoarding` | 1 | 4 radiant_dust + 8 minecraft:coal + 2 #c:ingots/gold + 3 #minecraft:logs |
| `arcanearchives:gemcutting/matrix_brace` → `arcanearchives:matrix_brace` | 1 | 1 scintillating_inlay + 2 #c:ingots/gold |
| `arcanearchives:gemcutting/material_interface` → `arcanearchives:material_interface` | 1 | 1 scintillating_inlay + 1 #c:ingots/gold + 1 shaped_quartz |
| `arcanearchives:gemcutting/containment_field` → `arcanearchives:containment_field` | 1 | 1 scintillating_inlay + 2 #c:ingots/gold + 2 shaped_quartz |
| `arcanearchives:gemcutting/devouring_charm` → `arcanearchives:devouring_charm` | 4 | 1 #c:ingots/gold + 2 minecraft:obsidian + 1 minecraft:flint_and_steel |
| `arcanearchives:gemcutting/radiant_key` → `arcanearchives:radiant_key` | 4 | 1 #c:ingots/gold + 3 #c:nuggets/gold + 1 shaped_quartz |
| `arcanearchives:gemcutting/radiant_amphora` → `arcanearchives:radiant_amphora` | 1 | 4 radiant_dust + 4 minecraft:clay_ball + 4 #c:nuggets/gold |

## 5. All 14 ordinary crafting recipes

These are `data/arcanearchives/recipe/<id>.json`. The first column is the **actual recipe ID**, not always the output identifier. Inputs in this table intentionally display the material quantities and pattern but are not an independent registry/type claim beyond the exact supplied JSON.

| Recipe ID | Recipe kind/pattern | Input | Output |
| --- | --- | --- | --- |
| `arcanearchives:conversion_rawquartz` | shapeless | 1 raw_quartz_cluster | 1 raw_quartz |
| `arcanearchives:destorage_rawquartz` | shapeless | 1 storage_raw_quartz block | 9 raw_quartz |
| `arcanearchives:destorage_shapedquartz` | shapeless | 1 storage_shaped_quartz block | 9 shaped_quartz |
| `arcanearchives:diorite` | shaped `cd/dc` | 2 cobblestone + 2 raw_quartz | 8 minecraft:diorite |
| `arcanearchives:gemcutters_table` | shaped `DGP/WCW/QWQ` | D diorite, G glass pane, P paper, W logs, C crafting table, Q raw_quartz | 1 gemcutters_table |
| `arcanearchives:lectern_manifest` | shaped ` M /SWS/S S` | M manifest, S stick, W plank | 1 lectern_manifest |
| `arcanearchives:radiant_chest1` | shaped `www/wqw/www` | 8 planks + 1 raw_quartz | 1 radiant_chest |
| `arcanearchives:radiant_chest2` | shapeless | 1 raw_quartz + 1 #c:chests/wooden | 1 radiant_chest |
| `arcanearchives:radiant_crafting_table` | shapeless | 1 raw_quartz + 1 minecraft:crafting_table | 1 radiant_crafting_table |
| `arcanearchives:radiant_resonator` | shaped `GIG/WBW/W W` | 2 gold ingot, 1 iron bars, 1 water bucket, 4 logs | 1 radiant_resonator |
| `arcanearchives:scepter_revelation` | shaped `  R/ G /S  ` | 1 raw_quartz + 1 gold nugget + 1 stick | 1 scepter_revelation |
| `arcanearchives:storage_raw_quartz` | shapeless | 9 raw_quartz | 1 storage_raw_quartz block |
| `arcanearchives:storage_shaped_quartz` | shapeless | 9 shaped_quartz | 1 storage_shaped_quartz block |
| `arcanearchives:tome_arcana` | shapeless | 1 minecraft:book + 1 minecraft:gold_nugget | 1 tome_arcana |

The native **Tome** is a vanilla shapeless **book + gold nugget** recipe. **Radiant Chest** has an 8-plank+raw-quartz shaped variant **and** a raw-quartz+tagged wooden-chest shapeless variant. These are separate recipes with the same output, not 2 distinct chests. A vanilla chest may additionally be converted in-world by sneak-using Raw Quartz on it, preserving the contents, according to `start/first_chest`.

## 6. All 5 guide categories and 40 exact reference entries

The **actual Modonomicon book** resource uses `custom_book_item: arcanearchives:tome_arcana` and `generate_book_item:false`. **The category titles are tutorial chapters**, not five source-enforced research tiers. Their actual in-JAR IDs give FTB authors stable links:

### 1. Grow Quartz — `arcanearchives:start` (8 entries)

`arcanearchives:start/first_steps` · `arcanearchives:start/resonator` · `arcanearchives:start/first_chest` · `arcanearchives:start/gemcutting` · `arcanearchives:start/recipe_radiant_dust` · `arcanearchives:start/recipe_shaped_quartz` · `arcanearchives:start/crafting_table` · `arcanearchives:start/next_storage`

**Scope:** Resonator/quartz production → first chest → counted Gem Cutting → dust/shaped quartz → persistent crafting.

### 2. Build Storage — `arcanearchives:storage` (8 entries)

`arcanearchives:storage/inlay` · `arcanearchives:storage/interface` · `arcanearchives:storage/troves` · `arcanearchives:storage/brace` · `arcanearchives:storage/containment` · `arcanearchives:storage/tanks` · `arcanearchives:storage/upgrades` · `arcanearchives:storage/next_networks`

**Scope:** Inlay → Material Interface → Troves; Brace → Field → Tanks; actual tiered upgrades.

### 3. Find and Route — `arcanearchives:networks` (8 entries)

`arcanearchives:networks/manifest` · `arcanearchives:networks/lectern` · `arcanearchives:networks/monitoring` · `arcanearchives:networks/tracking` · `arcanearchives:networks/brazier` · `arcanearchives:networks/routing` · `arcanearchives:networks/range` · `arcanearchives:networks/next_tools`

**Scope:** Manifest, fixed Lectern, Monitoring Crystal, tracking, fuel-free Brazier, target priority and radius/access.

### 4. Use Your Tools — `arcanearchives:tools` (8 entries)

`arcanearchives:tools/revelation` · `arcanearchives:tools/manipulation` · `arcanearchives:tools/chest_labels` · `arcanearchives:tools/amphora` · `arcanearchives:tools/translocation` · `arcanearchives:tools/devouring` · `arcanearchives:tools/radiant_key` · `arcanearchives:tools/next_sharing`

**Scope:** Revelation, Manipulation, Chest labels, linked Amphora, translocation, destructive Charm and filter-preserving Key.

### 5. Share and Automate — `arcanearchives:sharing` (8 entries)

`arcanearchives:sharing/sharing` · `arcanearchives:sharing/invitation` · `arcanearchives:sharing/resignation` · `arcanearchives:sharing/expulsion` · `arcanearchives:sharing/lighting` · `arcanearchives:sharing/automation` · `arcanearchives:sharing/settings` · `arcanearchives:sharing/checklist`

**Scope:** Personal/shared ownership, signed network invitation/resignation/expulsion, lighting, item/fluid automation, settings and native diagnostic advice.


**Modonomicon parent arrows** are recorded separately inside each of those 40 `entries/<category>/<name>.json` files, using `parents: [{entry:...}]`. They define reading order/guide navigation, not enforced craft/research locks. **24 advancement files** are predominantly `minecraft:inventory_changed` item-possession predicates, with a root `minecraft:location`—not proof of executing native storage operations. Consequently quest handoff must not convert the chapter graph or mere item possession into mandatory mechanics or truthful “used the Brazier” detection without later authoring work.

## 7. Cross-mod interfaces and Atlas completion bar

Source-established NeoForge capabilities: sided item handling on native Chests/Troves/crafting stations; fluid handlers for Tanks, item fluid handlers for the **linked Amphora** and portable Tank; portable Troves/Tanks preserve data through block-item serialization; multi-user archive access persists by UUID/network membership. These establish integration **surfaces**, not guaranteed behavior with any named TMM adapter.

For TMM, note the narrow *interaction research* boundaries only: generic container nesting/blacklist tag (Issue #16), oversize 256-slot stack transfer with other item pipes, EvilCraft canonical blood fluid tags, FTB Chunks protections for access/routing, and ProjectE EMC potential across portable storage/blocks. **No general in-world test is needed for Atlas research.** Only a **specific unresolvable interface/cross-mod disagreement** should be escalated to the integration owner; do not assume the released mod is broken.

**Result:** the pinned **1.0.2 binary**, all packaged recipes/guide entries, exact core capacities/cooldowns/ranges, registry evidence, and main native player operations are sufficiently source-known for quest-source planning. Outstanding full-machine-code edge cases (e.g. exact multi-mod fluid adapter behavior or claim-system hooks) should not be treated as unmet native-mod gameplay QA or an Atlas feature-inventory blocker. Preserve these limits when marking the main dossier.
