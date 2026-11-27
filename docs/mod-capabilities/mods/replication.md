# Replication 1.2.7 — Mod Capability Atlas

**Status: native quest-source survey complete, 2026-11-25.** Tracker [#410](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/410). Completes the Replication portion of [preliminary endgame infrastructure evaluation](endgame-infrastructure-evaluations.md). Native behavior, TMM source overlays, and proposed instructional steps are separate. No gameplay/config/build changes.

## 1. Exact-release evidence and onboarding

Drive Replication-1.21.1-1.2.7.jar 841,370 bytes; SHA1 bb433ec94394e960ba26fc2e7fd5537fcb7fbe50 matches [active pin](../../../mods/replication.pw.toml), CF638351/7662870. Titanium 1.21-4.0.52 SHA1 b46f625333c9c570eed9962a355f37687d39e731 matches [pin](../../../mods/titanium.pw.toml). Both ZIP CRC clean. Replication 722 entries,207 classes,239 recipe JSONs: 223 matter_value anchors (186 item/37 tag),10 shaped,4 shapeless,1 smelting,1 blasting;16 recipe-unlock advancements (no independent native progression advancement tree),11 loot tables,18 common/vanilla tags,2 worldgen records and1 NeoForge biome modifier. Titanium 527 entries. Metadata Minecraft>=1.21.1, Titanium>=4.0.32, javafml>=4. Earlier [version-aligned source 8482d3e](https://github.com/Buuz135/Replication/tree/8482d3e995c6185ea760251f0140ceab0b3e8328) supplements exact binary. Used zipfile data census and javap -p -c on ReplicationConfig nested classes, DisintegratorBlockEntity, IdentificationChamberBlockEntity, ReplicatorBlockEntity, ChipStorageBlockEntity, ReplicationTerminalBlockEntity, MatterTankBlockEntity, ReplicationMachine, MemoryChipItem, ReplicatorMotorItem, ReplicatorEnclosureItem, MatterNetwork, ReplicationTask, ReplicationCalculation and CalculationReference, MatterCompound, NumberUtils. No claim of exact whole dependency source or loaded-world behavior.

Bundled guide assets/replication/guides/replication/main/guide.md and setup.snbt/.nbt show native system diagram, machine roles, motor retries, MatterOpedia syntax; a guide-viewer dependency may be needed, so provide JEI plus FTB instructions and do not assume a book item. No native guidebook item in registered English names. MatterOpedia in Terminal is native and primary player reference. JEI recipe/tooltip and Jade tank compatibility present. Actual registry families: 11 blocks (deepslate ore, replica/raw blocks, chip storage, disintegrator, identification chamber, network pipe, matter tank, terminal, replicator, creative matter tank); seven non-block item names (replica ingot/raw, memory/creative memory chip, blueprint, enclosure, motor). Creative items excluded from survival capabilities.

## 2. Entry, recipes, and tier graph

Native Overworld deepslate_replaceables ore configured size10, five placement attempts per chunk, trapezoid height anchors above_bottom -80 to +80; not absolute Y and not guaranteed veins. Smelt Raw Replica200 ticks or blast100 ticks, both produce one ingot and .35 XP. TMM suppresses ore worldgen (section8). Native 9:1 ingot/raw block reversible recipes. No native Replica nugget (TMM adds it). No inherent ore generator; acquire initial material externally.

Exact recipe quantities from packaged shapes (tags resolved at load):

| Native output | Materials |
|---|---|
| Matter Tank | 4 Replica ingots,4 glass blocks |
| Matter Network Pipe x8 | 2 Replica ingots,6 glass panes,1 redstone |
| Disintegrator | 5 Replica ingots,2 Matter Tanks,2 glass blocks |
| Identification Chamber | 5 Replica ingots,2 diamonds,1 glass pane |
| Memory Chip | 3 Replica ingots,2 redstone,2 gold ingots |
| Chip Storage | 4 Replica ingots,2 iron ingots,1 wooden chest,1 iron trapdoor |
| Replicator | 5 Replica ingots,1 piston,1 redstone,1 iron ingot |
| Replication Terminal | 6 Replica ingots,2 diamonds,1 glass pane |
| Replicator Enclosure | 8 purple stained glass panes,1 Replica ingot |
| Matter Blueprint | 6 Replica ingots,1 blaze powder,1 paper,1 piston; result component identifies Motor with Progress=1.0 |

Motor itself is NOT directly craftable through ordinary shaped recipe; native motor anchor metallic162/precious48/quantum1/nether22. Blueprint transfers 100% motor pattern by scan or sneak-right-click Chip Storage, then order motor using actual matter. This is genuine material/knowledge/production gate. No formal gear tier or permanent player rank. First network: connect powered Disintegrator → pipes → Matter Tanks; Identification Chamber → Memory Chips in Chip Storage (or directly atop chamber) → network → Terminal request → Replicator → Terminal output. All machines must share a connected matter network and FE supply where relevant. Power source is external. Expand tank/chip/worker count, then enclosure, then motor, parallel requests and filtered infinite production. No fixed native endgame machine tier beyond these upgrades.

## 3. Matter resources, calculation, and concrete examples

Eight actual typed matter resources Earth, Nether, Organic, Ender, Metallic, Precious, Living, Quantum; Empty sentinel not resource. Separate amounts, no direct generic cross-type exchange. Matter is a custom typed stack/tank network, not ordinary Forge fluid, EMC, mana, FE or XP. Electricity powers machinery; source items physically consumed, matter stored in blocks and reserved on tasks, patterns stored in item component and chip block, not personal player power. No item can be printed just because the player knows a similarly named item.

Native anchor examples (raw values before rounding):

| Item or tag | Matter vector |
|---|---|
| dirt | Earth1 |
| c:stones | Earth2 |
| minecraft:logs | Earth8 Organic8 |
| minecraft:leaves | Organic4 |
| wheat | Organic2 Earth2 |
| beef/carrot/apple (individual anchors) | Organic4 Living4 |
| rotten_flesh | Living2 Organic2 |
| coal | Earth2 Precious.25 Organic.25 |
| redstone | Precious2 Earth2 |
| iron_ingot | Metallic9 |
| diamond | Precious36 |
| ender_pearl | Organic4 Quantum1 Ender2 |
| nether_star | Quantum64 Nether16 |
| replicator_motor | Metallic162 Precious48 Quantum1 Nether22 |

Native anchors are defaults, not permission guarantees. Exact data paths data/replication/recipe/matter_values/{minecraft,replication,c}/...; all 223 records enumerated. Values can be tag-wide and overlap; actual loaded recipe resolution matters. ReplicationCalculation.getMatterCompound defaults indexed per Item; getDefaultValue checked BEFORE SKIP_CALCULATION. Thus exclusion from recursive calculation does not null explicit anchor. Fallback uses recipe references, traverses eligible ingredients, subtracts crafting remainder if configured, divides sum by output count. Config max recipe depth11, visited50; ignored recipe mods default ae2qolrecipes; checks visited items/recipes and cached resolution. MatterCompound.compare chooses one entire vector with smaller cached sum of amounts, not independently smallest quantity of each type. No arbitrary mixing of recipes or cheapest EMC guarantee. Actual ingredient/recipe compatibility can leave items without a value. Value calculation is not identical to ability to scan, disintegrate or print; inspect loaded MatterOpedia for effective values. Replication ignores component-specific item properties in ordinary scan by using getDefaultInstance (not free copies of custom enchants/NBT). Disintegrator uses NumberUtils.customCeil on each amount; ReplicationTask uses Mth.ceil/Math.ceil to reserve positive units. Coal nominal anchor fractional amounts therefore yields 2 Earth+1 Precious+1 Organic in native disintegration; don't use unrounded .25 as actual tank contribution. Fractional calculated anchors and alternate recipes warrant pack economy audit, not proposed changes here. Example native resource conversion: two rotten flesh supply Organic4 and Living4, matching one beef's default vector, subject to learned pattern and enabled resources. This is conversion, not creation from nothing.

MatterOpedia opens via button left of Terminal search bar or matter displays on right. Bundled guide confirms plain matter name search, numeric comparisons (earth>10, nether=20, quantum<6), exclusion !earth and sole type *metallic. This lets players select feedstocks by actual effective typed composition. Use exact item sample, inspect tooltip (hold configured key), learn pattern and compare storage amounts. No need to hardcode all recipes in quests.

## 4. Disintegration and storage

Disintegrator has 3 input slots, six16,000-unit internal matter tanks (96,000 combined if compatible contents), base machine energy buffer25,000FE. Progress config40; onFinish loops all three nonempty slots; for each not CANT_BE_DISINTEGRATED, sufficient energy >=1500 and nonnull matter compound, queues each rounded matter stack, shrinks input by ONE and extracts1500 FE. Thus 1500 FE per processed item, potentially up to three items in one completed cycle, NOT fixed one item/40 ticks. CanIncrease stops while queued matter remains, so tank pressure matters; tick flushes queued stacks into matching/empty tanks, retains queue and serializes it; internal splitItems runs every5 ticks to distribute input. Nominal40 progress ticks at normal tick rate; don't claim unconditional 1.5 items/s; availability, queue backpressure, actual progress component timing and FE limit apply. Supply/disintegrate prohibited items is blocked at onFinish, not universal global item deletion. Native input and output machine side restrictions; route via pipe network and ordinary external automation as permitted.

Matter Tank stores256,000 typed units (not 256 buckets of arbitrary fluid); choose one matter type/contents, priority and void-excess controls, expand across many tanks for simultaneous eight types. The guide explicitly pipes matter from Disintegrator to tanks and tanks to consumer machines; pipes also transfer power with config2560, not guaranteed per-receiver capacity multiplied by branches. MatterNetwork uses linked Titanium network elements, supplier/consumer priority and task state, not a raw-item storage grid. One Matter Tank for each of eight types is a straightforward illustrative storage plan (2,048,000 combined if eight fully filled); no native requirement to prebuild all eight before first conversion. Matter tank priority and void excess should be taught separately because discarded excess is not restored by crafting.

## 5. Identification and knowledge

Chamber base25,000FE buffer, input sample slot, three memory-chip input and three output slots; accept nonempty sample not CANT_BE_SCANNED, stored energy strictly >5000, output slot space and local chip or modifier block above. ProgressBar default max200, with half-cycle config100; the scan's completion event and UI return phase distinct, so don't promise exact wall-clock duration. On accepted ordinary executeProgress, add .5 to default-instance item pattern, debit5000 FE if ModifierAction has a pattern; ordinary sample shrinks only when completion>=1. One item and two accepted half-progress operations (10,000 FE) for typical fresh pattern, not two consumed samples. Blueprint component transfers stored progress and consumes blueprint on scan; code also executes completion shrink on input when pattern>=1 (possible extra shrink for stacked blueprints; no promise of stacked behavior). Bundled guide provides alternate direct sneak-right-click transfer to Chip Storage, recommended for motor. Native tooltip language references fast/slow scan modes but exact inspected handler/config does not demonstrate such a toggle; don't teach from stale localization alone.

Memory Chip is stack size1 and has16 pattern slots; data components hold pattern and completion. Chip Storage holds8 chips, nominal128 entries before partial/duplicate/value constraints. Inserting chips in network exposes completed patterns. A Chip Storage immediately above Identification Chamber is a direct target when local slots not used, still needs actual chips in storage. Filled chips can move to output as slots fill; inspect empty capacity and pattern list. Expand network by multiple Chip Storages; no fixed global learned-pattern ceiling established. No copy of a specific enchanted/damaged item's components promised. Creative Memory Chip and Creative Matter Tank are excluded.

## 6. Replicator scheduling, energy and acceleration

Replicator has seven output slots, 25,000 FE buffer; Terminal18 output slots, pattern and requested quantity UI. Start a single or parallel task using eligible learned pattern; network reserves each required type before work. ReplicationTask.checkHasEnough simulates availability, then drains required rounded amount and records reservation by worker position. Replicator.tickProgress requires assigned task, stored FE strictly > powerConsumption and reservation. Default half-phase H=100, full progress bar2H; each active advancing/return tick extracts80 FE. Idle progress can prefill without task; successful print occurs at forward endpoint, consumes reservation and increments completed amount, then resets task and can reassign. Do not equate 200 progress with fixed 200 paid ticks per successful item. Output first attempts to insert one item at source Terminal's UP item capability, fallback local seven slots; simulates local space before successful print, so clear worker buffers if jammed. No general arbitrary item routing inferred. Terminal task list supports cancel (sneak-click in bundled lang), single/parallel worker selection; infinite filter in Replicator makes continual single-unit tasks until matter/output/redstone prevents further work, not unlimited free production. RedstoneManager.shouldWork gates active loop; task cancellation/worker removal and reservations exist, no guarantee of refund in every unload/race condition without further inspection.

Enclosure: apply native item to Replicator (guide sneak+right-click), duration multiplier0.8, power ceil(80*1.1)=88FE/t. Motor: acquire via blueprint, apply to machine. Slider m=20..100 clamped in server handler; maxProgress full bar recalculated once per second: int(200 * enclosure factor), then int(previous * m/100) if motor; half H=floor(full/2). FailureChance=floor((100−m)*50/80) percent, uses random integer0..99. At failure, no output/finalize; flags rerolled, progress descends then rises again, same reservation retained. Bundled guide explicitly confirms retry does NOT consume more matter. Time/FE cost of retries still real. Distinguish duration multiplier from speed multiplier:20% duration is 5x nominal phase speed, not +20% speed.

| Configuration | Half-phase ticks H | Nominal FE per paid half-phase | Failure chance per attempt |
|---|---:|---:|---:|
| Default | 100 | 8000 | 0% |
| Enclosure only | 80 | 7040 | 0% |
| Motor100 | 100 | 8000 | 0% |
| Motor80 | 80 | 6400 | 12% |
| Motor60 | 60 | 4800 | 25% |
| Motor40 | 40 | 3200 | 37% |
| Motor20 | 20 | 1600 | 50% |
| Enclosure+Motor20 | 16 | 1408 | 50% |

These are exact arithmetic on compiled phase/power rules, **not fixed FE/item or measured throughput**. Active return phase, idle prefill, assignment polling every4 ticks, resupply, output and failed cycles change observed cost. At 20%, expected attempts2 with independent 50% retry rolls, but no guarantee on individual prints. Faster setting can improve throughput at cost of retries and risk of intermittent delay. No selected balance changes, no unconditional maximum throughput asserted. Multiple workers parallelize, power and typed matter provisioning become bottleneck.

## 7. Automation and exclusions

Native guide describes pipes for FE/typed matter, Terminal tasks and local Infinite Mode; no generic ore miner, automatic farmer, item-storage network, pets, weapon, armor, flight, personal buff, resurrection or grave. Existing vanilla item capability can support hoppers/external item routing. Pattern and matter storage not synonymous with chest storage. Redstone control, priority, cancellation and filter are actual native automation. MatterOpedia detects available effective value data, not ore in world.

Three native tag constants CANT_BE_SCANNED, CANT_BE_DISINTEGRATED, SKIP_CALCULATION exist in compiled code; no packaged data/replication/tags item file defining exclusion membership in this binary. Scanning is checked in canIncrease, disintegrating onFinish, calculation skip only AFTER default anchor lookup. No dedicated native CANT_BE_REPLICATED tag established. ReplicationTask.storeMatterStacksFor resolves value and checks enough matter, no scan tag check at reservation entry. Infinite filter and previous patterns are distinct access paths; therefore a scan/disintegration/calculation exclusion cannot automatically guarantee no prelearned or explicit-anchor prints. Blueprint direct transfer is another relevant integration surface; do not state a universal no-print enforcement from these three tags. This is an implementation boundary, not instructions to bypass TMM rules.

## 8. TMM current source comparison

- [Replica ore override](../../../kubejs/data/replication/neoforge/biome_modifier/replica_ore.json) is neoforge:none, verified #575 source; no normal newly generated native ore. [Eldritch Miner](../../../kubejs/data/tmm/recipe/miner/replica_ore_eldritch.json) returns deepslate_replica_ore1 weight90 for occultism:miners/eldritch. [Orechid](../../../kubejs/data/tmm/recipe/orechid/deepslate_replica_ore.json) has input deepslate and output deepslate Replica Ore weight10. Initial resource policy, eligible seed and external magical generation owned by pack. Do not imply only Eldritch is mechanically possible if Orechid recipe also indexed. TMM adds replica_nugget with 9:1 reversible crafting. Embers seed route from previous owner decision not reverified in this pass; not native.
- [Coin matter anchor](../../../kubejs/data/tmm/recipe/matter_values/adventurer_coin.json) tmm:coin Metallic4 + Precious4, exact current source. Do not use native value as guarantee of every purchase/printing privilege.
- #448 and #565 source-exclusion work uses 1,720 unique stonecutter outputs from Nov18 loaded capture (2,503 recipes), including outputs also obtainable through another recipe. Three native exclusion tags and explicit stone anchor handling staged. Live integration lookup of all three exact tags and runtime evidence doc returned 404; current index does not list them. #673 integration candidate exists, but do not call this installed or effective. Existing #601 next-test recapture and known previously-learned/explicit-anchor limitations remain separately owned. Record this as a concrete integration discrepancy, not a claim of runtime failure. No new catalog or exclusion behavior in this dossier.
- Actual [FTB chapter](../../../config/ftbquests/quests/chapters/replication.snbt) still one optional checkmark with XP2/paper reward. Preserve stable chapter/quest IDs when expanding. ProjectE gating, FE from Draconic or magical generators, other ore routes and Q Shop interactions separate.
- No additional Monk compensation: universally accessible infrastructure, not restricted gear.

## 9. 29-axis navigation

| Axis | State | Concrete capability |
|---|---|---|
| gear | absent | no equipment family |
| movement | absent | no personal movement enhancement |
| ore_processing | limited | raw Replica smelt/blast, disintegration of valued ores/material synthesis |
| storage | present | typed tanks256k, 16 patterns/chip, 128 nominal/storage, small output buffers |
| crafting | core | pattern-guided item manufacturing, not arbitrary crafting-table recipe automation |
| logistics | present | pipe matter/FE network, terminal output delivery; no general item warehouse |
| buffs | absent | no player effects |
| external_compatibility | present | Titanium/NeoForge FE/item capabilities, JEI/Jade, loaded recipe anchors |
| mob_farming | absent | no spawning/kill device |
| ore_generation | limited | replicate eligible material/ore patterns if valued and permitted; no mining machine/ore block generator |
| permanent_power | absent | stored patterns infrastructure, no innate player trait |
| general_generation | core | transmute typed feedstock into learned items, not free resource generation |
| direct_damage | absent | no weapon or spell |
| block_breaking | absent | ordinary external tools only |
| permanent_pets | absent | no companion |
| temporary_pets | absent | no summons |
| defense | absent | no armor/protection |
| healing_recovery | limited | food synthesis sustains ordinary hunger; no healing effect or cure |
| debuff_control | absent | no native |
| energy | core | eight typed matter stores and external FE; task reserve, no internal generator |
| automation_control | core | redstone, infinite filter, task modes, network priorities, pipe distribution |
| agriculture | limited | synthesize eligible crops/animal products; no plant growth/breeding/harvesting |
| detection_information | present | MatterOpedia value search, task/storage/learning UI |
| equipment_augmentation | absent | enclosure/motor modify machine, not personal gear |
| world_manipulation | absent | no placement/terraforming |
| world_content | limited | native deepslate ore feature, suppressed in TMM |
| death_respawn | absent | no death protection |
| trade_economy | limited | typed item transformation can affect external commerce, no native market |
| progression_tiers | present | materials→network→knowledge→production→motor/parallel scale |

## 10. Proposed quest-source map (not FTB implementation)

Teach actual mechanics with observable operation or source-backed machine state; items are acquisition proxies only. Avoid teaching disallowed materials as TMM guaranteed patterns; examples choose actual permitted values from MatterOpedia. Preserve current chapter and optional instructional cross-links. No forced destruction of rare gate material, no duplicate rewards, no creative blocks.

| ID | Player action / prerequisite | Evidence |
|---|---|---|
| RP01 | Obtain first Replica through TMM permitted magical acquisition, note native ore disabled | actual ore or seed |
| RP02 | Smelt/blast Raw Replica, understand ingot/block/nugget recipes | ingot |
| RP03 | Craft Matter Tanks and pipe x8 | inventory |
| RP04 | Craft Disintegrator and connect FE | powered machine |
| RP05 | Read native guide/tooltip and choose eligible feedstock | observed value |
| RP06 | Disintegrate sample; inspect item consumption and typed matter | tank increase |
| RP07 | Connect tank via pipe and observe transfer | external tank increase |
| RP08 | Add storage for multiple types and set priority | state |
| RP09 | Learn Void Excess and backpressure; leave off unless intended | state/observation |
| RP10 | Craft Identification Chamber and blank Memory Chip | items |
| RP11 | Scan a common permitted item to 50% then100%, learn one sample consumed | complete pattern |
| RP12 | Craft Chip Storage and install chip | recognized pattern |
| RP13 | Alternative place Chip Storage directly over Chamber, learn another item | completed pattern |
| RP14 | Craft Replicator + Terminal and connect same network | connected machines |
| RP15 | Use MatterOpedia search, comparison, exclusion and sole type | observable search |
| RP16 | Request one learned permitted item, confirm required matter/FE and output | result |
| RP17 | Compare compatible vs missing type, use feedstocks to satisfy each | actual storage |
| RP18 | Request batch using Single mode and collect terminal/local output | task completed |
| RP19 | Add second Replicator, request Parallel, observe worker count | two workers |
| RP20 | Cancel optional queued task and understand reservations; no refund promise | task state |
| RP21 | Configure local infinite filter for permitted pattern, redstone stop | operation |
| RP22 | Expand chip library, 16 per chip and up to8 per storage | capacity |
| RP23 | Expand tank/pipe/FE throughput to address shortages | network supply |
| RP24 | Enclosure craft/apply, inspect nominal 0.8 duration and 88FE/t | installed state |
| RP25 | Craft Motor Blueprint and transfer to chip storage | completed motor pattern |
| RP26 | Supply 4 types of motor matter, order Motor, install | item/installed state |
| RP27 | Compare slider100 and20; failed attempts retry reserved matter, don't require fail | operation |
| RP28 | Teach food/material conversion with permitted native anchors, no free food/ore | result |
| RP29 | Explain TMM stonecutter restrictions and protected progression; use real allowed patterns only | read/observed permitted example |
| RP30 | Late production system: sustained selected resource with feed, power, storage, patterns and workers | operating network |

No mandatory build/gameplay test for native survey completion. Exact native source/compiled behavior and packaged data sufficient for significant capabilities; effective tag membership, active economy/recipe graph, cancellation/reload corner cases, measured throughput and optional guide viewer remain separately scoped integration questions. No gameplay/config/quest changes. Separate evaluation remains 21/55, four independent, threshold Substantial.
