# Occultism pinned worker-job contracts

Exact local artifact `occultism-1.21.1-neoforge-1.227.0.jar`, SHA-512 `063d775420e1efa916d1ed94f3f5bfc3b00b976b1de6fc333eda9886ea3665aa38254407a18469ed0a334bcac67b204392c9bf197fc10916ce36a9e29abd5ef0`. [Parent](occultism.md), [all ritual/job recipes](occultism-pinned-rituals.md), [processing recipes](occultism-pinned-processing.md), [storage/harvesting](occultism-pinned-storage-harvest.md). Direct ZIP resource and CFR 0.152 method inspection; no runtime test. Job methods establish capability and boundaries, not proof that pathfinding/GUI/loaded pack behaves correctly.

## Registered job roster and activation

`registry/OccultismSpiritJobs` registers lumberjack, farmer, manage_machine, transport_items, cleaner; trader_otherstone, trader_otherrock, trader_otherworld_saplings, gambler; crush_tier1–4, smelt_tier1–4, crystal_tier1–4; clear_weather, rain_weather, thunder_weather, day_time, night_time. The ritual appendix identifies exact summon-with-job recipes, spirit types, activation/ingredients and pentacles. These jobs are not all interchangeable abilities of any summoned entity. Tier processing parameters are suppliers from server config, not immutable arithmetic inferred from spirit names.

`SpiritJob.init` remakes brain and writes work-area center/size and deposit position/facing to memories before job initialization. Base NBT records factoryId and recreates that factory on load; subclasses save their own state. Base pickup eligibility is false. Worker ownership/control/inventory and actual summoning walkthrough still depend on SpiritEntity and instruction-tool code; this audit does not substitute guessed universal GUI controls.

## Smelting: four recipe types, one fuel-free worker path

`common/entity/job/SmelterJob` searches recipes in strict order **smelting → blasting → smoking → campfire cooking**. On initialization it builds its pickup ingredient list from all four loaded recipe types; recipe reload behavior is not established. A hand item with no applicable recipe is dropped with `occultism:dropped_by_smelter` tag. Pickup excludes tagged recent results until server-configured smelterResultPickupDelay, preventing immediate repeat pickup; older valid outputs can be processed again.

A matching recipe advances one timer per job update, completing at recipe cookingTime × configured timeMultiplier. There is no furnace fuel input in this worker method. Completion batches `min(config operationCount, hand count)`, multiplies assembled output count by that batch, copies/spends inputs **before** onSmelt and mutable SmelterJobEvent. This handler does not award recipe cooking XP. Its method is not a generic all-machine recipe executor.

Normally nonempty event output becomes a loose item at the spirit, tagged as above. If directly below the spirit is Dimensional Extractor and an UP-face item handler exists two blocks below, it inserts there and suppresses loose fallback. **Insertion remainder is ignored**, so a rejecting/full destination can lose output after inputs were spent. Cached destination handler refresh is based on block-state identity, not a guaranteed refresh for every capability change. Never describe this output path as lossless. NBT stores conversionTimer, but current recipe fields are not serialized; recipe acquisition resets timer on a new match, so stored timer alone does not guarantee resumable partial work.

## Cleaner and generic pickup

`CleanerJob` uses ReturnToWorkAreaGoal, PickupItemsGoal and DepositItemsGoal. Eligibility is `(not blacklist and matches) OR (blacklist and not matches)`, with matches being item-filter OR tag-filter match. Actual item/tag comparator semantics belong to StorageUtil, not an assumed NBT-exact whitelist.

`PickupItemsGoal` tests simulated inventory insertion before selecting an eligible ItemEntity. Its work-area pickup box uses ±configured work size horizontally and ±half that size vertically around work-area center. It sorts nearby candidates, navigates to the first and within distance <2 inserts, then reduces the source ItemEntity count to the actual remainder. This preserves ordinary partial-insertion quantities in this goal. Selection is throttled by random executionChance 10 and gameTime modulo 10; “every ten ticks” is not an unconditional processing guarantee. Work-area behavior is not the same geometry as farmer/tree target sensors below.

## Transport and deposits

`TransportItemsJob` adds extraction, deposit and door-opening goals and provides SpiritTransporterContainer. It is a physical navigating entity/capability route, not an unrestricted remote link. Generic extraction needs empty main hand/no combat target, configured extract position, and a target handler. It moves to the side of the target and accesses within distance <1.86. `canSeeTarget()` currently returns true; that is no line-of-sight filter, but navigation/reach still matters.

Important exact discrepancy: `ExtractItemsGoal.resetTarget` validates the configured **extract facing**, while the actual extraction call in `tick` queries the block using **deposit facing**. Sided inventories can therefore behave differently from the selected extraction face. In the ordinary path it simulates extract + entity insert, then extracts only the amount that fits; final insertion remainder is not separately recovered if a capability changes between calls. Special MapItemStackHandler whitelist extraction ignores components for the initial filter lookup; a nonempty item whitelist can report the special path handled even if no matching item moved. Do not claim arbitrary tag-only/blacklist extraction has identical behavior.

Generic deposit requires nonempty hand/no combat target and configured block or loaded entity UUID destination; the entity destination can replace the block move target. Access distance <2.2; queries deposit face. It simulates insertion then inserts a copy, keeping actual leftover in hand. Partial simulated insertion pauses for 2000 **milliseconds** (`PausableGoal` uses System.currentTimeMillis), not 2000 game ticks. A fully rejecting destination leaves the held stack. These methods contain unchecked races if capability results change; do not infer transactional guarantees.

## Machine manager and storage orders

`ManageMachineJob` persists controller GlobalBlockPos, MachineReference, current order and queued DepositOrders. Changing machine/controller clears orders and unregisters old references. It registers a spirit and machine link with a loaded controller; on update it resolves controller again and refreshes registration if the object changed. Unloaded/missing controller returns null rather than forcing chunk loading. An order with null comparator is discarded.

`ManageMachineGoal` takes orders only with empty main hand/no combat target. To fetch a requested input it requires simulated full insertion into machine, finds a linked storage proxy near the machine, navigates within 2.2, simulates full fit in spirit inventory then extracts controller items, sets destination to machine insert position/face and clears current order. To take machine output it scans extraction slots, simulates available spirit capacity then extracts fitting quantities, and selects a nearby linked proxy as deposit destination. Proxy search is within ±work-size X/Z and ±half-work-size Y of the **machine insertion position**, selecting by distance; it is not a global unlimited search. Machine handlers honor configured insert/extract faces. Missing handlers can clear the managed machine. Full storage crafting/order dispatch transaction semantics and instruction-tool setup remain a separate audit.

## Farmer: native crop class and free replant body

`NearestCropSensor` scans at default 100-tick intervals; a no-crop result suppresses scans for 400 ticks. It searches work-center cube ±integer(work size/2), selects maximum-age **CropBlock** above **FarmBlock**, excluding cached unreachable/noncrop positions; a horizontal neighboring empty collision shape is needed for preliminary reachability. This does not establish support for arbitrary growable tags, fruit trees or machines.

`HarvestCropBehaviour` starts within squared distance 2.5² of selected crop. It checks that target remains mature, then scans a **5×5 same-Y square centered on that target**. Each mature CropBlock is destroyed with drops and immediately set to its default state, update flag 1. There is no seed inventory consumption in this body. Neighbor harvests need not each pass the sensor's soil or original work-area test; the square can extend past the scan boundary. This documents source mechanics, not a promise to bypass other protection hooks or that every modded CropBlock yields the desired loot. Drops remain subject to normal block loot/world pickup and worker collection logic.

## Lumberjack: initial tree recognition is not a felling boundary

`NearestTreeSensor`: default 100-tick scan; no-tree memory 600 ticks. Initial stumps are #minecraft:logs above #occultism:tree_soil in work-center cube ±integer(size/2), excluding cached non-tree/unreachable positions. It climbs a vertical nonempty column and needs a leaf-like top (`LeavesBlock` or #minecraft:leaves), then recursively searches downward nearby logs/leaves to identify a stump. Preliminary reachability needs a horizontal empty block. Decorative connected logs are therefore not guaranteed safe just because only the initial stump was classified as a tree.

`FellTreeBehaviour` starts within squared distance 3.5², takes 160 running ticks and destroys with drops using breadth-first traversal of log blocks: four horizontal neighbors and the 3×3 layer one block above. The traversal has visited tracking but **no work-area or maximum-count bound** in this method. It can follow connected log structures upward/sideways beyond the initial scan. Keep log buildings isolated from working groves. It does not recursively destroy leaves in this loop.

It records stump positions for replanting. `ReplantSaplingBehaviour` within 3.5 requires an inventory #minecraft:saplings slot; if recorded position is air and below is #minecraft:dirt and selected item is BlockItem, place its default block and shrink one sapling. It uses first matching sapling, not an enforced original tree species. It removes that recorded position even if placement conditions fail; no claim of universal retry or guaranteed sapling recovery. Tree-soil recognition tag and replant dirt tag differ.

## Time/weather jobs and a false lightning inference

ChangeTimeJob computes next 24000-tick-cycle target (day 0 or night 13000) and interpolates dayTime over configured cast updates. It saves current count, required count and target time. Config-disabled initialization reports to owner and finishes; normal completion calls die/out-of-world then removes spirit. It is not a persistent forever-running day machine. Owner-null and dynamic config/time edge cases require gameplay verification.

ChangeWeatherJob counts configured updates, calls specific weather operation, constructs a LightningBolt, positions it and sets `visualOnly=true`, then kills/removes the spirit. **This body does not add the lightning entity to the world.** Construction alone is not a usable lightning strike or Mahou Emrys source; even a spawned visual-only bolt would not be equivalent to Roots' real bolt. Specific weather duration config, global dimension effects and enabled flags still require leaf-subclass/config crosswalk before a complete weather walkthrough claim.

## Teaching and acceptance boundaries

Use the ritual table for exact job acquisition. Teach a small isolated work area, reachable target, permissive disposable destination with known faces, observed first item movement and verified output before scaling. Acquisition of a summon book is not worker operation. Farm teaching must state native crop-class filter and replant behavior; lumberjack teaching must warn of connected logs; smelter teaching must include output capacity and actual delivered count. Generic logistics policy remains unchanged; documenting native behavior does not authorize a new generic logistics mod or adapter.

Positive/boundary tests, all unrun: compare every processing tier with one and multiple inputs; full/rejecting extractor destination; distinct extraction/deposit faces; partially full cleaner destination; reload order/controller; farmer mature CropBlock vs unsupported growable, scan-edge neighbors; connected sacrificial logs beyond initial work area; insufficient/wrong saplings; unloaded controller and missing storage proxy; time/weather completion and spirit disposal. Remaining source work includes exact instruction/ownership UI, full farmer/lumberjack task composition and item sensors, complete dispatcher/storage transactions, weather leaf defaults, familiar fusion and guide-to-full-stage reconciliation. Parent remains researching.


## Books of Calling: exact owner controls and recovery boundary

Sources: `common/item/spirit/BookOfCalling{Item,FarmerItem,LumberjackItem,CleanerItem,TransportItems,ManageMachineItem}`, all `common/item/spirit/calling/*Mode`, `common/entity/spirit/SpiritEntity`. These are pinned methods, not assumed control bindings from another release.

1. Use the book appropriate to the worker's job. Farmer/Lumberjack/Cleaner books expose Set Base and Set Deposit; transporter exposes Set Extract and Set Deposit; machine manager exposes Set Managed Machine, Set Extract and Set Storage Controller.
2. Sneak-interact with an **owned** compatible SpiritEntity to link its UUID/name. The item handler rejects nonowned spirits. If the book is already linked to that same spirit, interacting serializes and removes it into the book instead of simply relinking; watch glint/payload state.
3. Nonsneak use opens the client book mode/work-size GUI. Once mode is selected, **sneak-use on a block** applies it server-side. Set Base writes work-area position; Set Deposit/Extract requires item-handler capability on the clicked face and records that face. An interaction returning success on an invalid block can still have made no change: verify confirmation/tooltip and operation.
4. For a farmer/lumberjack/cleaner, set a reachable work base, select appropriate work size and a reachable accepting deposit inventory/face. For transporter, set accepting extract and deposit faces, then verify an actual item crosses; the worker goal's deposit/extract-facing discrepancy above still applies.
5. For machine manager, first assign an item-handler machine, then optional separate extract target, then storage controller. Initial machine reference uses that machine for both endpoints; managed-machine client panel exposes insert/extract facing/custom name when an existing reference is present. Set Extract without an existing managed machine can return a message/success without creating the reference. Do not treat that as a configured machine.

Ordinary noncalling-item interaction with an untamed SpiritEntity calls tame(player). Sneak-interaction by the actual owner opens worker inventory/job menu; transporter/cleaner use their MenuProvider. This is not a universal public-access/team-owner rule. Worker aiStep initializes once, runs job updates server-side while alive and increments age every 20 ticks if canDieFromAge; age exceeding max triggers death/removal. Exact per-ritual lifetimes remain in the ritual/config source crosswalk, not an assumed immortal worker.

Book capture stores full entity serialization and removes original. Placement with stored payload loads a fresh entity at clicked block or its adjacent face if the original position collides, removes saved Pos, updates book from entity and clears payload. **Pinned `useOn` calls addFreshEntity on that same entity twice and ignores both boolean results**, verified against javap bytecode. This is not evidence of two successful entities or a duplication recipe; UUID/add rejection semantics and world events determine outcome. It does establish that the book clears payload without checking successful delivery, so do not promise lossless relocation if spawn is rejected. Prepare a disposable test and verify returned worker before relying on relocation.

Tamed spirit death records UUID/time in a static in-memory map. Every 1200 game ticks a bound book can consume an older entry, mark itself dead and clear its UUID. This is delayed notification, not a persistent global soul ledger or guaranteed cross-restart death detector. Exact GUI packet authority/work-size bounds need their own network audit before security claims.


## Farmer/lumberjack brain composition, collection and native defaults

Farmer core priority uses MoveToWalkTarget, HarvestCrop, PickupItem and DepositItems in FirstApplicableBehaviour; idle tries crop target before deposit before wanted item. Lumberjack core prioritizes MoveToWalkTarget, ReplantSapling, DepositItems, PickupItem then FellTree; idle tries replant, deposit, wanted item then tree. Their onChangeWorkArea clears no-target/unreachable/non-target memories. These task lists are not a guarantee of fixed throughput under path obstruction.

Farmer accepted pickup ingredients are `c:seeds` and `c:crops`; lumberjack accepts log/leaf/sapling item tags, Occultism fruits and Stick. NearestJobItemSensor runs default 20-tick scan, selects an eligible item with line of sight in work-center cube ±half-size. WorkAreaSize enum values are Small16/Medium32/Large64; these are widths for this sensor, while the generic PickupItemsGoal above uses those same values as horizontal radii. Do not label every worker “16-block radius” based solely on enum name.

Their brain `PickupItemBehaviour` requires deposit-position and deposit-facing memories, plus wanted-item memory. It first accepts wanted item within per-axis distances (3.5,16,3.5) if some inventory capacity exists. After that, it scans alive ItemEntities within wanted item's box inflated3 and attempts insertion of each. **This incidental sweep does not recheck job eligibility, line of sight or work-area membership**. Actual leftovers are written back to each entity; condition on simulated remainder <=64 is not a predicate restricting to crop/log loot. Keep unrelated dropped items away. This differs from cleaner/processor PickupItemsGoal and should not be conflated with it.

Brain DepositItemsBehaviour starts within squared distance2.5² with any filled inventory slot, accesses the memorized face, and transfers first filled slot through simulate then actual insertion retaining leftover. Its10-tick behavior runtime is not a promise to empty all slots at once. Deposit walk target requires an empty horizontal neighbor not cached unreachable; no route may stall collection/deposit. This explains why assigning deposit is part of a functioning farmer/lumberjack setup, even though their harvest methods themselves can act without consuming storage inputs.

Pinned native `OccultismServerConfig.SpiritJobSettings` defaults below (effective world overrides untested):

| Family / tiers Foliot,Djinni,Afrit,Marid | Time multipliers | Output multipliers | Operations per batch |
| --- | --- | --- | --- |
| Crusher | 2,1,0.5,0.3 | 1,1.5,2,3 | 1 each |
| Crystallizer | 1,0.5,0.3,0.1 | 1,1.5,2,3 | 1 each |
| Smelter | 1,0.5,0.1,0.01 | native assembled count; no separate output multiplier | 1 each |

Crusher/crystallizer recipe eligibility tiers default1/2/3/4. Each family's result repickup delay is60ticks. Trader Sapling defaults20ticks/1operation; Otherstone and Otherrock10ticks/4operations; Gambler200ticks/16operations; trader delay60ticks. As documented in processing appendix, fractional output scaling truncates and ignore-multiplier recipes override it; do not round up or treat output multiplier as an extra batch size.

Weather leaf methods: ClearWeather sets `(clearTime=0,rainTime=0,raining=false,thundering=false)`; Rain `(0,6000,true,false)`; Thunder `(0,6000,true,true)`, each gated by corresponding ritual enable config. Clear's zero is not a promised6000-tick forced-clear duration. Disabled leaf sends owner a message, then parent still disposes spirit. Default cast update counts: day100, night100, clear100, rain200, thunder300. Parent completion compares incremented count for equality, so config-allowed zero is a boundary worth testing rather than assumed instantaneous success. Construction of visual-only lightning remains unspawned.
