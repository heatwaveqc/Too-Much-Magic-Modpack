# Occultism — mechanics and gameplay profile

| Field | Value |
| --- | --- |
| Manifest ID / actual mod ID | occultism / occultism |
| Category / parent or addons | magic; related profiles: [Ars Nouveau](ars-nouveau.md), [Ars Ocultas](ars-ocultas.md), [Ars Unification](ars-unification.md) |
| MC / loader / pack target | 1.21.1 / NeoForge / test/full-integration at f6f1c5894ffd1ebcdcd4fd5d0989375fcc4d8f78 |
| Pinned version / artifact / hash | occultism-1.21.1-neoforge-1.227.0.jar; SHA-512 063d775420e1efa916d1ed94f3f5bfc3b00b976b1de6fc333eda9886ea3665aa38254407a18469ed0a334bcac67b204392c9bf197fc10916ce36a9e29abd5ef0 (mods/occultism.pw.toml) |
| Source snapshot | release/v1.21.1-1.227.0, commit [990303ea95ba057791b7d407d6cac5151f66d799](https://github.com/klikli-dev/occultism/tree/990303ea95ba057791b7d407d6cac5151f66d799); version-aligned, binary equivalence not tested |
| Guide/documentation version gap | Source-generated Dictionary of Spirits at matching release; exact pinned archive now hash-matched and inspected for all158 ritual resources/types/lifecycle; full guide rendering untested |
| Profile status | researching |
| Evidence reached | Pack pin and full matching upstream source archive/guide datagen inspected; pack quest file and integration docs inspected; no client test |
| Reviewed | 2026-11-15 |
| Quest ownership | Existing Occultism FTB chapter is a one-quest empty placeholder; not edited in this knowledge pass |

## Executive gameplay map

**What the player actually does:** Occultism is ritual and spirit work. The player finds Demon's Dream seeds by breaking grass, makes the Dictionary of Spirits, then follows it to harvest or create Otherworld materials and draw chalk pentacles. A Golden Sacrificial Bowl activates a ritual using a bound Book of Binding; other bowls hold item ingredients, and some rituals ask for a nearby sacrifice or item-use event. Rituals bind, possess, summon, craft, or trade with spirits. Bound spirits become workers such as crushers, smelters, miners, farmers, transporters, cleaners, and machine operators. The progression also unlocks familiars, remote item storage, Otherworld exploration, and the Dimensional Mineshaft. Four spirit tiers—Foliot, Djinni, Afrit, Marid—lead to unbound and eldritch branches and higher-tier materials. The pinned book has nine categories and230 entry records; the ritual chapters are much broader than the single pack quest. [source: release commit; datagen/OccultismBookProvider.java, GettingStartedCategory.java, PentaclesCategory.java, SummoningRitualCategory.java]

**Why it matters in TMM:** Its strengths are item-only remote storage, tiered ore processing, automated pocket-dimension mining, and spirit labor. docs/DESIGN.md explicitly places Occultism storage in the intended core and allows magical satchels. docs/PROGRESSION.md specifically proposes the Eldritch Ancient Miner as a controlled source for Replication's Deepslate Replica Ore and considers uranium/draconic dust extraction. These make miner outputs and recipe gates progression-critical; the docs also say those gates are not implemented.

**Quick start:** Break grass until Demon's Dream seeds drop; grow the plant and combine an item in #c:seeds/datura with #c:books to craft the Dictionary. Read Getting Started → Lifting the Veil. Make Spirit Fire by throwing Datura onto the ground and igniting it with flint and steel. Use Spirit Fire to convert materials and purify impure chalk. Prepare White Chalk, candles, sacrificial bowls and a Golden Sacrificial Bowl; open Pentacles → Foliot → Summon Foliot for the exact diagram and recipe. Craft a Foliot Book of Binding and craft the tier book together with the Dictionary in a2×2-or-larger crafting grid to produce its named bound book. Put the ritual ingredients in bowls within the documented radius and activate the Golden Bowl with the bound book. Chalk pattern, particles and bowl state provide operational feedback. [source: matching GettingStartedCategory.java and PentaclesCategory.java]

### Player-value snapshot

| Five standout capabilities | Three genuinely early practical unlocks | Three candidate integrations |
| --- | --- | --- |
| Four-tier spirit ore crushing; native remote storage; pocket-dimension mining; spirit-run machine/farm/logistics jobs; familiars with utility, protection and combat effects | Spirit Fire recipe conversion; first Foliot Crusher; Foliot Cleaner/Transporter or Lumberjack | Ars Unification exposes a Crusher Spirit Crush recipe (see Ars Unification profile); Theurgy-specific divination and storage book entries appear conditionally; miner as a source for gated ores is a TMM design proposal |

### Subsystem coverage at a glance

| Subsystem / branch | Mechanics evidence | Progression completeness | Guide/JEI reference | Client validation | Blocker / next action |
| --- | --- | --- | --- | --- | --- |
| Dictionary, Otherworld, Third Eye, Spirit Fire | source: 990303e | strong | Getting Started entries listed below | not run | Compare pinned jar's generated book |
| Chalk, pentacles, bowls, binding books | source: 990303e | strong through branch map | Getting Started and Pentacles | not run | Verify pattern overlays, wait conditions and distances |
| Spirit tiers and summon/possess/craft choices | source: 990303e | all four standard tiers and unbound branches located | Pentacles, Summoning Rituals, Familiar Rituals | not run | Audit exact costs/conditions and outputs |
| Worker roles and tiered processing | source: 990303e | roles inventoried | Summoning Rituals named entries; JEI ritual recipes | not run | Measure output rates and job setup |
| Otherworld tools and mining | source: 990303e | partial | Getting Started: Divination Rod, Goggles, Infused Pickaxe, Iesnium, Spirit Miners, Mineshaft | not run | Audit every miner output against TMM ore gates |
| Storage and remote automation | source: 990303e | partial | Getting Started: Storage; Storage category | not run | Extract structure/capacity details and test |
| Familiars, resurrection, battlefield, extractor | source: 990303e | partial | Familiar Rituals and Binding Rituals | not run | Full recipes, risks and endpoint details remain |
| Ars Unification crusher bridge | Ars Unification profile and upstream source refs | candidate/source-supported | Crusher Spirit; Ars Unification documentation | not run | Test exact pins in client |

## Guidebook onboarding

- **Guide and access:** Dictionary of Spirits, a Modonomicon book. Break grass for Demon's Dream seeds, grow them, and combine seed/fruit with an ordinary book as shown by JEI. Official project instructions say to begin with this guide.
- **Earliest stage:** Immediately after the first seed drop; no dimension or boss gate.
- **First instructional quest:** “Find the Dictionary” should teach seed acquisition, growing it, crafting/opening the Dictionary, and reading About and Lifting the Veil. An item task proves possession, not that the player read a page. Offer a safe garden/ritual supply and XP, not a gift that skips a later gate.
- **Reference map:** Dictionary categories are Getting Started, Pentacles, Summoning Rituals, Binding Rituals, Storage, and Familiar Rituals. Getting Started contains pages for Datura/Third Eye, Spirit Fire, Divination Rod, Theurgy divination rods (conditional), candles, chalk/bowls, binding/calling books, ritual operation/failure, advanced chalks/satchels, Otherworld tools/miners/mineshaft and storage. JEI shows recipes and tagged variants.
- **Guide limits:** The guide provides pentacle patterns and recipe references. Quests still need to state what to place, activate, and observe; item inventory criteria do not prove a ritual succeeded. Page names and overlays are source-confirmed, not yet verified in the assembled client.
- **Other help:** JEI; ritual bowl pentacle visualization and state; native ritual/familiar advancement triggers exist in source, but FTB task support is untested.

## Systems and full mechanics inventory

| Subsystem | Access and inputs | Effects / outputs | Limits, failure and evidence |
| --- | --- | --- | --- |
| Dictionary | Demon's Dream seed + book recipe | Guide categories and progression unlocks | Seed drop is stated on official project page; exact odds not established |
| Third Eye / Datura | Consume plant or essence; spirit goggles later | Reveals Otherworld materials; Datura effect also permits harvesting low-tier Otherworld blocks/trees | Fruit effect is chance-based; essence guarantees longer effect but has side effects; purified essence keeps benefit and removes negative side effects per guide source |
| Spirit Fire | Throw Datura on ground and ignite | Recipe-defined conversion/purification without consuming the fire; e.g. Otherstone, saplings, purified chalk and book ingredients | Exact allowed items are JEI recipes; no arbitrary item/fluid conversion |
| Divination | Divination Rod with spirit-attuned gem; right-click target analogue | Finds corresponding Otherworld resources | Client config controls scan range; not a universal ore detector |
| Chalk | Craft impure chalk, purify in Spirit Fire, draw on blocks | Pentacle marks by color | Chalk wears; color and position matter, decorative glyph variant does not |
| Ritual layout | Chalk pattern, Golden Sacrificial Bowl, support candles/skulls/crystals, sacrificial bowls | Ritual yields an entity, items, world state or ritual effect | Recipe may require sacrifice or item-use event; gray particles mean waiting; exact conditions vary per ritual |
| Binding/calling books | Spirit-fire materials and tier recipe; bind with Dictionary; combine bound book with job tool | Select a spirit tier or assign a worker role | Spirit names are cosmetic according to guide; tier is operative. Bound entity is not automatically a worker |
| Possession/summoning | Yellow/green chalk rites and correct tier book | Possessed mobs, summoned spirits, unbound spirits and familiars | Risks and requirements vary; summoned tamed animals/familiars may arrive untamed |
| Worker roles | Bound Calling Book and role recipe | Crusher, smelter, crystallizer, farmer, lumberjack, transporter, cleaner, machine operator, traders and special outputs | Tier, accepted inputs, speed and job instructions vary |
| Crusher | Item matching a crushing recipe and the worker's inclusive tier gate; accepted recipe ingredients are picked up by the worker | Default raw-iron recipe yields 2 dust × tier output multiplier: Foliot 2, Djinni 3, Afrit 4, Marid 6. With the default 200-tick recipe time, corresponding cycle thresholds are 400/200/100/60 ticks. | Defaults are source-config values, not measured performance. One input item per operation by default; recipes can override time/tier and can opt out of the output multiplier. Unsupported hand-held input is dropped back into the world. A full Dimensional Extractor handler can silently lose output remainders. |
| Otherworld mining | Dimensional Mineshaft plus supported bound miner | Item stacks stored in the mineshaft | 194 matching-source miner recipes: 108 ordinary ore recipes, 59 eldritch, 9 deeps, 5 master, 11 basic-resource and 2 debug recipe files. Foliot, Djinni, Afrit and Marid miners are all included in the ordinary ore tag; see source-confirmed gate findings below |
| Storage | Dimensional Matrix + Foliot-infused Controller Base -> Storage Controller; Storage Stabilizers and Djinni remote | Item-only remote storage with integrated crafting and automation access | 1.227.0 source guide/config maps defaults, all five capacity tiers and remote chunk behavior; setup geometry has a guide/source wording discrepancy. No fluid or FE channel found. [source: 990303e] |
| Familiars | Familiar rites; familiar ring and binding items | Player effects, utility, combat, protection, mobility or upgrades | Specific effect, food, costs and death behavior vary; client verification needed |
| Advanced rites | Battlefield, extractor, resurrection, satchels, Iesnium tools, chalices | Encounters, special entity/item results, improved ritual prep or materials | Each has its own recipe and condition; not one linear final tier |

## Material / capability input-output contracts

| System | Input kind | Output kind | Trigger / automation | Evidence / limit |
| --- | --- | --- | --- | --- |
| Spirit Fire | Item entity accepted by a specific spirit_fire recipe | Item stack in world or bowl arrangement | Item collision; optional sacrificial bowl above fire | Recipe JSON; no general fluids/FE |
| Ritual | Bound book plus recipe item stacks; optional named sacrifice or item-use event | Entity, item stack, world state or effect | Golden Bowl activation; inputs consumed per ritual | ritual JSON and matching guide page; conditions vary |
| Crusher Spirit | Input stack matching a tier-valid recipe; recipe result plus configured operation count/output multiplier | Item stack, usually dust | Worker selects a tier-matching recipe, consumes up to configured operationCount per cycle, posts CrusherJobEvent, then inserts below a Dimensional Extractor or drops to the world | Default raw-iron count is 2/3/4/6 per one item at Foliot/Djinni/Afrit/Marid. A recipe's ignore_crushing_multiplier flag can suppress tier yield scaling; extractor insertion remainders are ignored. Exact loaded tag output/client behavior remain unverified |
| Spirit miner | Miner type plus Mineshaft | Configured item stacks | Internal mining cycle | Exact resource table and cycle need audit |
| Storage | Item stack insertion; remote links a dimension-qualified controller position | Stored/retrieved item stack, or integrated crafting result | Controller GUI, compatible inventory interactions, or linked remote use | Default base 128 item types / 256,000 total item count; each valid Tier 1–5 stabilizer adds config-defined capacity. Remote checks that the target chunk is already loaded; no force-load operation found. No fluids, XP, mana, blood or FE channel found. [source: 990303e] |
| Familiar | Ritual inputs and state/events | Entity, player effect, AI action or protection event | Passive/tick/event | Familiar and advancement sources; exact FTB integration unknown |

No evidence in inspected source supports arbitrary fluid, FE, mana, blood, or XP inputs. Rituals can use world entities/events as conditions and can output entities, items and world effects; they are not generically item-only recipes. Every recipe's exact input and output contract needs its own JSON/source review.

## Acquisition, renewability and output economics

- Demon's Dream starts as a grass drop and is renewable after growing the crop.
- Spirit Fire transforms only registered recipe inputs; it is not a fuel-powered machine or general transmuter.
- Tiered crushers have high multiplication. For the generated raw-iron recipe, source code yields 2/3/4/6 dust per one input at Foliot/Djinni/Afrit/Marid defaults and reaches its recipe-time threshold after 400/200/100/60 ticks. These are source formulas/defaults, not measured wall-clock throughput. Other recipes may have distinct tier/time gates or ignore the output multiplier.
- Dimensional Mineshaft is a potentially high-impact renewable source. Compare every miner output with TMM's Replication, uranium, draconic-material and boss-drop rules before claiming completion.

### Source-confirmed miner gate findings

The matching release recipe data already contains cross-mod outputs, not just generic vanilla ore support:

| Recipe / path | Miner tier tag | Result tag | Weight | TMM consequence |
| --- | --- | --- | ---: | --- |
| recipe/miner/ores/draconium_ore.json | occultism:miners/ores (includes Foliot unspecialized, Djinni ores, Afrit deeps and Marid master miners) | c:ores/draconium | 142 | **Source-confirmed low-tier path:** the version-matched Draconic Evolution 3.1.4 source adds four ore IDs to c:ores/draconium, and Occultism's miner tag includes Foliot. Its recipe conditions therefore have nonempty input/output tags in the selected source pair; exact artifact and runtime registration remain unverified. |
| recipe/miner/ores/uranium_ore.json | occultism:miners/ores (same tier inclusion) | c:ores/uranium | 140 | Conditional route only. The recipe disables itself when c:ores/uranium is empty; no selected-mod source has yet been confirmed to populate this tag. |
| recipe/miner/eldritch/raw_uranium.json | occultism:miners/eldritch (Ancient Eldritch Miner only) | c:storage_blocks/raw_uranium | 90 | Conditional late-tier route. Source requires a nonempty common tag; its population in the selected pack remains unknown. |
| recipe/miner/eldritch/raw_yellorium.json | occultism:miners/eldritch | c:storage_blocks/raw_yellorium | 90 | Conditional route only; Bigger Reactors Rework is excluded from the selected pack and no current tag provider is confirmed. |
| No active draconium storage-block recipe | — | Draconic Evolution provides c:storage_blocks/draconium, but Occultism's makeStorageRecipe("draconium", 90, ...) call is inside a commented-out //Don't found in 1.21 block | — | Corrects the earlier false path: the checked release source generates no Ancient Eldritch Draconium block recipe. |

The generated JSON uses NeoForge conditions that require nonempty input and result tags. Occultism's occultism:miners/ores tag includes occultism:miner_foliot_unspecialized; the version-matched Draconic Evolution source's c:ores/draconium tag lists its End, Nether, Overworld and Deepslate ore items. That means the low-tier recipe is source-reachable from the selected mod pair, although the pinned JAR bytes, loaded recipe and actual player route are not verified. Do not generalize this to uranium: the source recipe is conditional, and no selected-mod tag provider has yet been confirmed. The source default is one roll per operation, with mining times/durability of 400 ticks/1000 for Foliot, 300/400 for Djinni, 200/800 for Afrit, 100/1600 for Marid, and 15/3200 for Ancient Eldritch. These are source defaults, not measured runtime. Recipe weights feed weighted random selection among matching results, so 140/142/90 are not percent chances or rates. Fortune can add extra weighted rolls and Silk Touch can multiply each result; the guide documents Efficiency as reducing mining time. Exact settings can be changed in startup/server config.

The source data found no Replication Deepslate Replica Ore recipe by filename search; this does not prove none can be supplied by another mod/datapack. Check loaded registries, not only Occultism's generated files. The Dictionary's Dimensional Mineshaft page says it discards items it cannot store; top side is lamp slot, bottom side ore slot, other sides all slots; redstone pauses the spirit; comparator reads inventory/lamp durability, and signal 10 is a cue to stop. It documents enchantment effects and a server option to save miners before breakage. Include these details in the eventual how-to.

### Crusher operation and yield contract

The crusher has three separate controls that should not be conflated: recipe eligibility (input and optional inclusive min/max tier), per-cycle input count (`operationCount`), and the spirit tier's output multiplier. Its default crushing recipe time is 200 ticks. In the 1.227.0 source, default crusher config values are:

| Spirit tier | Recipe tier | Time multiplier | Output multiplier | Operation count | Default raw-iron result/time |
| --- | ---: | ---: | ---: | ---: | --- |
| Foliot | 1 | 2.0 | 1.0 | 1 | 2 iron dust / 400 ticks |
| Djinni | 2 | 1.0 | 1.5 | 1 | 3 iron dust / 200 ticks |
| Afrit | 3 | 0.5 | 2.0 | 1 | 4 iron dust / 100 ticks |
| Marid | 4 | 0.3 | 3.0 | 1 | 6 iron dust / counter60 |

These are code defaults, not measured throughput; at 20 TPS they correspond nominally to 20, 10, 5 and 3 seconds for a single item. The exact generated raw-iron recipe accepts `c:raw_materials/iron`, returns two items from `c:dusts/iron`, and has no explicit time or multiplier override, so it uses the serializer/config defaults. Whether the pack's unified tag resolves to a particular dust item is not proven here.

The worker caches a recipe after pickup. It consumes at most `min(operationCount, held stack count)` input items when a cycle completes. Output count is recipe result count × consumed input count × tier output multiplier, cast to an integer. Recipes can set `ignore_crushing_multiplier=true`; the source README says this is intended for non-raw-material conversions such as ingot-to-dust to prevent a crusher/smelter multiplication loop. A recipe with no matching entry is dropped back into the world rather than consumed. A recipe can restrict itself to an exact tier or a minimum/maximum tier range; the bounds are inclusive.

If a Dimensional Extractor is directly below the Crusher and exposes an item handler on its underside, the result is inserted there. Otherwise it is dropped into the world with a crusher marker. The handler insertion remainder is ignored by the caller, so a present but full/blocked extractor can lose the unaccepted output instead of spilling it to the world. The source also posts a `CrusherJobEvent` after input consumption and before output delivery; pack integrations should not assume the output is immutable or that the event restores consumed input.

**Research boundary:** The source maps the serializer, worker transaction and default values; it does not enumerate all 240 recipe outputs here. The pack config overrides, exact pinned JAR, Almost Unified resolution, event consumers, full-extractor boundary and real worker timing still require confirmation.

### Storage system walkthrough and exact source contracts

The matching 1.227.0 Dictionary and source establish this route. This is source-backed, not confirmed in the pinned JAR or a TMM client.

| Step | Required inputs and operation | Result / next gate | Source evidence |
| --- | --- | --- | --- |
| Craft a Dimensional Matrix | Bind a Djinni; run the Djinni craft pentacle with duration counter150 with 3 quartz blocks and 1 item in `c:ender_pearls`; activate with the bound Djinni Book of Binding | 1 `occultism:dimensional_matrix` | `ritual/craft_dimensional_matrix`, recipe datagen and Dictionary page |
| Craft a Controller Base | Bind a Foliot; run the Foliot craft pentacle with duration counter60 with 1 Otherstone Pedestal, 2 `c:ingots/copper`, and 1 `c:ingots/gold`; activate with a bound Foliot Book | 1 `occultism:storage_controller_base` | `ritual/craft_storage_controller_base` |
| Assemble the Storage Controller | Place the Dimensional Matrix above the Controller Base in the 1×2 shaped recipe | 1 `occultism:storage_controller`; the base is the operative storage block | `crafting/storage_controller`; Dictionary warns the matrix item alone is not the working storage block |
| Use and protect it | Place the controller and right-click with an empty hand to open its interface | Item store with integrated 3×3 crafting interface; breaking the controller preserves contents in the dropped block item | Dictionary Usage/Safety pages and controller/container source |
| Add capacity | Craft stabilizers from a pedestal, then upgrade by tier; place them along the six cardinal rays from one block above the controller, within 5 blocks, facing back toward it | Each ray contributes at most its nearest correctly facing stabilizer; one stabilizer is enough to increase capacity. Tier 0 contributes no capacity. | Dictionary Extending Storage and `StorageControllerBlockEntity.findValidStabilizers` |
| Craft a Remote | Use a bound Djinni and Djinni craft pentacle with duration counter150 with an inert remote, 2 `c:ender_pearls`, and 1 `c:gems/quartz` | 1 `occultism:storage_remote`; use the remote on a controller to record its dimension and position, then use the linked remote to open storage | `ritual/craft_storage_remote`, `StorageRemoteEntry`, `StorageRemoteItem` |

The Dictionary says to link a Remote by **shift-clicking** the controller. In the 1.227.0 `StorageRemoteItem.useOn` implementation, any item-use-on-controller event records the link; no shift-key check appears. Record this as a guide/source discrepancy until client validation. The saved address includes dimension and block position, but opening the remote checks that the destination chunk is already loaded. If it is not loaded, the remote reports that and does not open storage; no chunk-loading behavior was found. Source selection checks the held remote first, then Curios, then player inventory. The source also permits only one player at a time in a given controller UI.

#### Stabilizer upgrade path and default capacity

Server-config values below are source defaults and may be overridden in the assembled pack. Each valid Tier 1–5 stabilizer adds the listed amount to that controller; Tier 0 is only a base for Tier 1 and adds nothing. The controller begins at 128 distinct item types and 256,000 total items. Distinct data-component/NBT variants consume separate type slots. These are aggregate item units, not per-slot stack caps.

| Stabilizer | Ritual inputs after previous tier | Bound tier / pentacle / duration counter | Default added item types | Default added total items |
| ---: | --- | --- | ---: | ---: |
| 0 | Otherstone Pedestal, crafting recipe | — | 0 | 0 |
| 1 | Tier 0 + copper storage block + Blaze Dust + Spirit Attuned Gem | Foliot / craft / counter60 | 64 | 512,000 |
| 2 | Tier 1 + silver storage block + Ghast Tear + 2 Spirit Attuned Gems | Djinni / craft / counter150 | 128 | 1,024,000 |
| 3 | Tier 2 + gold storage block + Totem of Undying + Spirit Attuned Crystal + Afrit Essence | Afrit / craft / counter300 | 256 | 2,048,000 |
| 4 | Tier 3 + Iesnium storage block + Beacon + 2 Spirit Attuned Crystals + Marid Essence | Marid / craft / counter510 | 512 | 4,096,000 |
| 5 | Tier 4 as activation item + Sculk Shrieker + Netherite storage block + Crying Obsidian + Iesnium storage block + 2 Spirit Attuned Crystals + Enchanted Golden Apple + Dragonyst Dust; Endermen sacrifice | Eldritch contact / counter780 | 1,024 | 8,192,000 |

The regular controller recomputes valid stabilizers when one is placed or removed and adds their per-tier contributions. Source traces from one block above the controller in six directions for up to five positions. It selects the first stabilizer block in each ray and counts it only when its facing is opposite the ray. The trace tests target positions and does not stop at intervening blocks; this differs from the Dictionary wording that says stabilizers need “line of sight.” The same guide paragraph calls the participating block a “storage controller” and then says it is one block above the “Storage Actuator.” Source code's actual subject is the stabilizer. Verify the arrangement in a client before using that prose as a walkthrough instruction.

The Dictionary describes the controller as behaving like a very large shulker box for compatible inventory integrations, and warns that devices which count stored contents may mishandle unusually large item counts. Removing the controller preserves its contents in the dropped item. Removing stabilizers below current usage does not delete contents, but blocks new insertions until enough capacity is restored. No fluid, FE, mana, XP or blood storage capability was found in this subsystem.

## Player progression / walkthrough blueprint — reconciliation in progress

This is a source-backed outline, not quest data. For every nontrivial ritual, the quest should name the Dictionary entry, use its pentacle preview and JEI ritual recipe, state activation, and teach a visible success cue. Summon, possess and craft are branches; jobs and advanced systems are optional capability paths.

| Step | Gate or choice? | Player actions and prerequisite | Unlock / reference | Teaching quest, detector and coverage |
| --- | --- | --- | --- | --- |
| 0. Find/open Dictionary | Required | Break grass for Demon's Dream seed; grow; JEI craft with book; open/read | Getting Started: About, Lifting the Veil | Teach how to obtain/open; possession task does not verify reading; source-backed |
| 1. Obtain Otherworld materials | Required | Use Datura for chance-based Third Eye and harvest low-tier blocks, or make Spirit Fire and use JEI transformations | Lifting the Veil; It burns!; JEI Spirit Fire recipes | Teach difference between harvesting and conversion; don't claim operation from crafted item |
| 2. Make ritual tools | Required | Purify White Chalk; craft candles, Sacrificial Bowls and Golden Sacrificial Bowl | Candles; Ritual Preparations: Chalks/Sacrificial Bowls; JEI | Teach colors, arrangement, bowls, activation and pattern; source-backed |
| 3. Bind first Foliot | Required | Spirit Fire ingredients for Binding Book; craft tier book; bind with Dictionary; follow Pentacles recipe and activate | Books of Binding; Pentacles: Foliot summon/craft | Explain ingredients, ritual events and feedback; ritual advancement is detector candidate but FTB support untested |
| 4. First worker | Choose a role | Bound Foliot + role tool/recipe; create Calling Book; assign and use Crusher, Smelter, Farmer, Lumberjack, Cleaner or Transporter | Summoning Rituals named worker entries | Teach one real operation; book possession alone does not prove work. Feature inventory found, setup details partial |
| 5. Chalk branches and Djinni | Required access, multiple ways to summon/possess | Progress Yellow/Purple/Light Gray/Lime chalk and required otherworld/end ingredients; complete Foliot rite; summon or possess/craft Djinni | Pentacles chalk and Foliot/Djinni entries | Keep variants optional where they are alternatives; exact costs still need audit |
| 6. Djinni capabilities | Tier gate plus optional jobs | Bind/summon Djinni; follow its chalk branch; choose crusher/smelter/crystallizer, machine operator, traders, time/weather or other jobs | Summoning Rituals tier 2 and named roles | Teach one selected job and JEI option discovery; costs need page audit |
| 7. Unbound Afrit to Afrit | Tier gate; summon/possess alternatives | Djinni progression → unbound Afrit rites → Red Chalk/Afrit essence → craft/summon/possess Afrit | Pentacles: Unbound Afrit, Red/Black Chalk, Afrit | Explain unbound vs bound spirit and actual drop gate; source map confirmed, ingredients incomplete |
| 8. Unbound Marid to Marid | Tier gate; summon/possess alternatives | Afrit progression → unbound Marid → Blue/Cyan chalk and other Marid materials → craft/summon/possess Marid | Pentacles: Blue/Cyan/Brown/Magenta, Marid rites | Teach valid branches and outputs; source map confirmed, material route incomplete |
| 9. Advanced tools and chalk | Optional branch | Use higher spirit/material gates for goggles, infused/Iesnium tools, advanced chalk, Rainbow/Void chalk, satchels and essence | Getting Started advanced entries; Pentacles; Binding Rituals | Teach each capability and specific JEI recipe; complete option audit outstanding |
| 10. Mining automation | Optional, TMM-critical | Craft Dimensional Mineshaft by Djinni ritual; install supported miner and inspect GUI/output | Getting Started: Spirit Miners, Dimensional Mineshaft; Binding Rituals mining pages | Requires approved miner output table and gates before walkthrough-ready |
| 11. Storage | Optional, TMM-central | Follow Storage and JEI diagrams; build controller/actuators/stabilizers, craft/link remote, insert/retrieve items and upgrade | Getting Started: Storage; Storage category | Must explain multiblock, link, limit and success; exact capacities not audited |
| 12. Familiars and advanced rites | Optional | Choose familiar effects/gear and learn upgrades; separately pursue resurrection, battlefield and extractor branches | Familiar Rituals; Binding Rituals | State costs, risks and actual entity outcome; source entry list found but mechanics incomplete |
| 13. Completion | Open-ended | Demonstrate chosen working endpoint: crusher, miner, storage, familiar or advanced rite | Relevant page and JEI | No universal final objective found; use optional branches, not a false linear finale |

**Dependency sketch:** Dictionary → Otherworld ingredients / Spirit Fire → chalk, bowls and binding book → Foliot → chalk/material branches → Djinni → unbound Afrit → Afrit → unbound Marid → Marid. The book graph includes optional possession, summon, craft, job, storage, mining, familiar and advanced branches. Do not force all mutually exclusive or specialist choices into the main quest dependency chain.

### Ritual and system how-to requirements

| Setup | Actionable instruction to teach | Reference / failure cue |
| --- | --- | --- |
| First pentacle | Use Dictionary preview; place correct color marks; add support pieces and bowls; activate Golden Sacrificial Bowl with bound book | Pentacles → Summon Foliot; Getting Started → Chalks / Sacrificial Bowls. Gray particles mean waiting for sacrifice/item-use; purple means running |
| Spirit Fire | Throw Datura and ignite; place recipe input in fire or documented bowl above | Getting Started → It burns!; JEI Spirit Fire recipe. Only registered recipes convert |
| Worker | Bind tier, perform role ritual, make Calling Book from bound book + role tool, assign/configure and inspect output | Summoning Rituals named worker page; idle causes and range require client confirmation |
| Mineshaft | Place ritual-crafted block, supply supported miner, inspect GUI and output | Getting Started → Dimensional Mineshaft; Binding Rituals miner tier pages; full inventory/invalid miner boundary untested |
| Storage | Follow controller/stabilizer diagrams; build, link remote, insert and retrieve one item, then test an upgrade | Storage category: Controller, Stabilizer, Automation; exact capacities and malformed-structure cues outstanding |

## Internal combinations and useful survival setups

| Combination | Benefit | Limit / evidence |
| --- | --- | --- |
| Datura → Spirit Fire → Otherstone/ashes/saplings/chalk/book ingredients | Accessible early engine for multiple progression materials | Recipe list is explicit; no arbitrary transformations. Matching guide/source |
| Ritual bowl automation | Bowl input can start some rites; top bowl catches item outputs or Flame of Automation for non-item outputs | Sacrifice/item-use rites may still need player/world event; guide source, client untested |
| Tiered crusher ladder | Foliot → Djinni → Afrit → Marid improves recipe output and speed | Multiplication and possible ore gate bypass; source guide examples, actual throughput not measured |
| Mineshaft + gated ore plan | Miner spirit → mineshaft → configured resources | Intended source for some TMM progression, but exact loot tables and gates unresolved |
| Ars Unification bridge | Occultism crusher provides an actual recipe route for Crusher Spirit Crush | Native addon recipe lead, exact in-pack behavior untested |

### Spirit Fire bench — early

- **Payoff:** recurring Otherworld ingredients for chalk, books and rituals.
- **Entry:** Demon's Dream crop, flint and steel, and a named JEI input.
- **Steps:** craft Dictionary; grow crop; throw Datura and ignite; feed a registered recipe input and collect the conversion.
- **Limits:** crop is renewable; only explicit recipes work. Source-backed, not client-tested.

### Foliot ore crusher — early/mid

- **Payoff:** accepted raw ore to dust at the first spirit tier.
- **Entry:** chalk/ritual setup, Foliot binding/calling book, tier-specific bowls and accepted raw ore.
- **Steps:** draw the named Crusher pentacle; put ingredients in bowls; activate; assign Crusher job; feed ore; collect dust.
- **Limits:** guide's example yield is not a measured rate. Compare same ore and elapsed ticks across all tiers. Source-backed, not client-tested.

### Foliot Cleaner or Lumberjack — early/mid

- **Payoff:** collect dropped items or harvest/replant trees.
- **Entry:** Foliot summon, correct role tool and Calling Book.
- **Steps:** follow named Summoning Rituals page; bind/summon; configure target/work area as entry directs; leave valid work and check output.
- **Limits:** exact range, chunk behavior and controls need client test.

## API, integration and pack-config surfaces

| Surface | Capability / current TMM state | Evidence |
| --- | --- | --- |
| Pack identity | Modrinth project sbJh4AZw, version SFDVXWYt; both-side pin; manifest marks dependency verification false | mods/occultism.pw.toml, manifest/mods.json |
| Data-driven recipes | Ritual, crushing, Spirit Fire, loot, tags and related recipes are the main input/output/gate surface; no direct Occultism recipe override found in targeted search | matching source data; no exhaustive KubeJS audit |
| Advancement triggers | RitualTrigger and FamiliarTrigger exist in source; FTB task compatibility unproven | source: 990303e |
| Server config | Worker limits/speed, ritual duration, miner efficiency/fortune/silk and storage stabilizer limits exposed | OccultismServerConfig.java; exact config defaults not yet enumerated |
| Client config | Divination scan range and UI/color settings | OccultismClientConfig.java |
| Pack portable-bag tag | portable_bags.json includes occultism:satchel, ender_satchel, ritual_satchel_t1 and ritual_satchel_t2 | checked-in KubeJS tag; consumer/slot effects need audit |
| Theurgy integration | Conditional Dictionary entries include Theurgy divination rod and storage automation | matching Occultism source plus installed Theurgy pin; recipe interaction needs both sides checked |

## Compatibility leads and issues

| Other mod / system | Link and classification | Status |
| --- | --- | --- |
| Ars Nouveau + Ars Unification | Ars Unification source profile reports a native Crusher Spirit Crush recipe; no TMM script needed if present at both pins | Source-supported candidate; client operation untested |
| Theurgy | Occultism's guide has conditional divination and storage automation entries | Candidate; both endpoints need exact recipe/structure audit |
| Pam's / Farmer's Delight crops | Foliot Farmer may use crop tags or recipes | Unknown; do not assume all added crops are accepted |
| AE2, Refined Storage, generic logistics/backpacks | Occultism's own storage is allowed by pack design; other network/logistics mods are excluded | Do not add or promise bridges |

**Compatibility issue to carry on the repo:** the source pair exposes one concrete progression risk: Occultism 1.227.0 defines a Foliot-accessible result for c:ores/draconium, and the selected Draconic Evolution 3.1.4 source snapshot populates that tag with four ores. The generated recipe conditions therefore pass if both source snapshots match the selected artifacts. This conflicts with the pack's controlled Draconium progression intent, but remains a source-derived route until exact artifacts and loaded recipes are checked. The Uranium and raw Uranium/Yellorium routes remain conditional on common-tag membership; no Eldritch Draconium storage-block recipe is generated in this release. TMM's PROGRESSION.md intends controlled sources and says enforcement is pending. This discrepancy is also recorded in docs/COMPATIBILITY.md.

**Additional balance issue:** compare the 2/3/4/6 dust per operation crusher tiers against other installed doubling routes and audit raw ore availability before assigning early-game rewards or progression roles.

**Storage boundary:** native Occultism storage/satchels are allowed; the pack's portable_bags tag includes four Occultism items. Confirm its consumers and slot effects, and keep that scoped to existing owner-approved magic systems.

No actual crash or incompatibility was observed. Exact jar/source comparison, optional dependency behavior and client tests are absent.

## Quest and existing chapter reconciliation

| Stage | Current quest | Actual detector vs intended action | Gap |
| --- | --- | --- | --- |
| Entire mod | config/ftbquests/quests/chapters/occultism.snbt has one optional blank/checkmark task, 2 XP and paper | Manual checkmark proves no acquisition or mechanic | Not a walkthrough; guide, steps, gates, outputs and rewards are absent |

The chapter file was inspected directly. No additional Occultism tasks were found in the checked-in FTB chapter. The quest/runtime file is deliberately unchanged. Future authoring should teach Dictionary → first ritual → one worker → tier gates → optional mining/storage/familiar branches. Prefer a native ritual advancement predicate only after confirming task support; an item task proves possession only.

## Evidence ledger

| Claim | Source / immutable SHA | Evidence and limitation |
| --- | --- | --- |
| Exact pin | mods/occultism.pw.toml and manifest/mods.json | Pin, Modrinth ID/version/hash; no JAR bytes to compare |
| Release source | [Source tree at 990303ea95ba057791b7d407d6cac5151f66d799](https://github.com/klikli-dev/occultism/tree/990303ea95ba057791b7d407d6cac5151f66d799) | Tag release/v1.21.1-1.227.0; aligned version, not proof compiled binary |
| Dictionary graph | datagen/OccultismBookProvider.java and datagen/book/{GettingStartedCategory,PentaclesCategory,SummoningRitualCategory,BindingRitualsCategory,StorageCategory,FamiliarRitualsCategory}.java | Source book categories, entries, names and gates; exact jar GUI still unverified |
| Recipe surface | [MinerRecipes.java](https://github.com/klikli-dev/occultism/blob/990303ea95ba057791b7d407d6cac5151f66d799/src/main/java/com/klikli_dev/occultism/datagen/recipe/MinerRecipes.java); generated miner recipe JSONs at release SHA | Source tree contains 158 ritual JSONs and 240 crushing JSONs; records conditional miner outputs and commented-out draconium storage call; not a live registry count |
| Mechanics/config | src/main/java/com/klikli_dev/occultism/common/ and config/Occultism{Client,Server}Config.java | Source behavior/config surfaces; crusher tier multipliers and operation defaults are mapped, but effective TMM overrides remain unverified |
| Official start | [Official Occultism project page](https://modrinth.com/mod/occultism) | Grass → Demon's Dream → Dictionary/guide; not detailed recipe authority |
| TMM design | docs/DESIGN.md, docs/PROGRESSION.md at integration base f6f1c5894ffd1ebcdcd4fd5d0989375fcc4d8f78 | Owner design intent and unimplemented gate caveat |
| Draconium common ore/storage tags | [Occultism miner recipe JSONs](https://github.com/klikli-dev/occultism/tree/990303ea95ba057791b7d407d6cac5151f66d799/src/generated/resources/data/occultism/recipe/miner); [DE c:ores/draconium](https://github.com/Draconic-Inc/Draconic-Evolution/blob/6251e32bc93eb4bfcd910236ec1eaa6546516229/src/main/generated/data/c/tags/item/ores/draconium.json); [Occultism miner tag](https://github.com/klikli-dev/occultism/blob/990303ea95ba057791b7d407d6cac5151f66d799/src/generated/resources/data/occultism/tags/item/miners/ores.json) | Both version-matched source snapshots list tag entries satisfying the recipe conditions, including Foliot unspecialized; loaded TMM registry and pinned binary comparison unverified. |
| Storage controller and stabilizers | [ControllerEntry.java](https://github.com/klikli-dev/occultism/blob/990303ea95ba057791b7d407d6cac5151f66d799/src/main/java/com/klikli_dev/occultism/datagen/book/storage_system/ControllerEntry.java), [StabilizerEntry.java](https://github.com/klikli-dev/occultism/blob/990303ea95ba057791b7d407d6cac5151f66d799/src/main/java/com/klikli_dev/occultism/datagen/book/storage_system/StabilizerEntry.java), [StorageControllerBlockEntity.java](https://github.com/klikli-dev/occultism/blob/990303ea95ba057791b7d407d6cac5151f66d799/src/main/java/com/klikli_dev/occultism/common/blockentity/StorageControllerBlockEntity.java), and [OccultismServerConfig.java](https://github.com/klikli-dev/occultism/blob/990303ea95ba057791b7d407d6cac5151f66d799/src/main/java/com/klikli_dev/occultism/config/OccultismServerConfig.java) | [source: exact 1.227.0 release commit] | Confirms guide steps, base capacities, stabilizer geometry/tiers/default increments, content preservation and shulker-like integrations; source/JAR parity and client behavior remain open |
| Storage remote | [StorageRemoteEntry.java](https://github.com/klikli-dev/occultism/blob/990303ea95ba057791b7d407d6cac5151f66d799/src/main/java/com/klikli_dev/occultism/datagen/book/binding_rituals/StorageRemoteEntry.java), [StorageRemoteItem.java](https://github.com/klikli-dev/occultism/blob/990303ea95ba057791b7d407d6cac5151f66d799/src/main/java/com/klikli_dev/occultism/common/item/storage/StorageRemoteItem.java), [CuriosUtil.java](https://github.com/klikli-dev/occultism/blob/990303ea95ba057791b7d407d6cac5151f66d799/src/main/java/com/klikli_dev/occultism/util/CuriosUtil.java), and [StorageControllerContainerBase.java](https://github.com/klikli-dev/occultism/blob/990303ea95ba057791b7d407d6cac5151f66d799/src/main/java/com/klikli_dev/occultism/common/container/storage/StorageControllerContainerBase.java) | [source: exact 1.227.0 release commit] | Confirms guide/code linking discrepancy, global-position link, loaded-chunk access gate, and single-open-UI rule; does not prove pinned-JAR behavior |
| Quest state |
| Crusher transaction | [CrushingRecipe.java](https://github.com/klikli-dev/occultism/blob/990303ea95ba057791b7d407d6cac5151f66d799/src/main/java/com/klikli_dev/occultism/crafting/recipe/CrushingRecipe.java), [CrusherJob.java](https://github.com/klikli-dev/occultism/blob/990303ea95ba057791b7d407d6cac5151f66d799/src/main/java/com/klikli_dev/occultism/common/entity/job/CrusherJob.java), [OccultismServerConfig.java](https://github.com/klikli-dev/occultism/blob/990303ea95ba057791b7d407d6cac5151f66d799/src/main/java/com/klikli_dev/occultism/config/OccultismServerConfig.java), [crushing recipe README](https://github.com/klikli-dev/occultism/blob/990303ea95ba057791b7d407d6cac5151f66d799/src/main/resources/data/occultism/recipe/crushing/00_readme.md), generated [raw iron recipe](https://github.com/klikli-dev/occultism/blob/990303ea95ba057791b7d407d6cac5151f66d799/src/generated/resources/data/occultism/recipe/crushing/iron_dust_from_raw.json) | [source: exact 1.227.0 release commit] | Maps serializer defaults and tier gates, default tier config, input-count/output formula, multiplier guard, invalid-input return, event timing, and extractor overflow loss; full recipe registry, effective config, tag resolution and pinned JAR/client behavior remain unverified |
| Bag tag | kubejs/data/tmm/tags/item/portable_bags.json | Four Occultism bag IDs included; consumer behavior unknown |
| Ars bridge | Ars Unification profile/source cited there | Source-supported lead; no client run here |

## Disposable-world test plan

| Test | Positive and boundary case | Expected mechanic and quest credit | Performed |
| --- | --- | --- | --- |
| Dictionary/Spirit Fire | Fresh world: seed→book; valid Spirit Fire input vs unrelated item | Valid conversion only; guide opens; quest item task distinct from transformation | no client |
| Ritual waits | Valid Foliot pentacle vs wrong color/missing bowl; test rite requiring sacrifice with and without named event | Correct setup proceeds; wrong/waiting state observable; task credit checked separately | no client |
| Crusher | Test raw iron at all four tiers against the source defaults; also test a tier-restricted recipe, ignore_crushing_multiplier recipe, unsupported input, stack larger than operationCount, event-modified output, and a full/blocked Dimensional Extractor handler | Record actual output and ticks; confirm tier gates, item return, event ordering, and whether extractor remainders are lost. Do not treat source defaults as measured throughput. | no client |
| Miner gates | Verify the loaded occultism:miner/ores/draconium_ore with selected DE ore tag; test Foliot success and missing-result-tag boundary. Check whether c:ores/uranium, c:storage_blocks/raw_uranium and c:storage_blocks/raw_yellorium resolve before testing conditional recipes; confirm no Eldritch Draconium recipe. Audit Replica Ore/protected drops. | Draconium path outcome and conditional uranium paths are recorded separately; only intended sources/tier produce gated materials | not run; required before walkthrough-ready |
| Storage controller/stabilizers | Build matrix + base; use controller; connect stabilizers on each ray at 5-block boundary with correct/incorrect facing; remove capacity while over capacity | Verify displayed capacity, insertion denial without deletion, recipe operation order, and the guide's line-of-sight/setup wording | not run |
| Storage remote | Link by ordinary use and sneak-use; access with destination chunk loaded/unloaded and in another dimension | Resolve shift-click guide discrepancy, verify dimension link, chunk-loading requirement, UI exclusivity and item preservation | not run |
| Storage automation | Insert/extract through vanilla chest-compatible integration and test a consumer that counts item contents | Verify accepted capability/path and large-count behavior; no fluid/FE channel assumed | not run |
| Ars Unification | Test Crusher Spirit recipe under exact pins, including output and unavailable target | Expected native recipe loads and operates | no client |

**Status:** researching. Exact local artifact access is resolved. The pinned appendices now close all158 ritual records, all529 processing records, native storage/access/dispatch/menu mechanics, worker-job methods/defaults, Otherworld visibility-versus-harvest rules and major rare familiar branches. These supersede earlier unchecked inventory/access claims in historical notes below.

Remaining source work is the **full recipe-to-guide-to-player-stage reconciliation** (including ordinary crafting/chalk/material branches and guide references), residual familiar-specific helpers and a final parent-profile rewrite that replaces old partial tables with concrete instructions. Effective tag/config resolution and client/gameplay acceptance remain separate, unrun checks; they are not evidence that source access is blocked.

- [x] All158 ritual recipes and11 factory channels inventoried.
- [x] All529 crushing/mining/crystallization/Spirit Fire/trade recipe resources inventoried, including conditions and tier/output boundaries.
- [x] Worker acquisition/control, physical item paths, harvest/collection and configured default processing parameters traced.
- [x] Native storage capacities, stabilizer search, remote access, dispatch and crafting-menu transactions traced.
- [x] Core familiar lifecycle/upgrades plus Chimera/Goat/Shub/Headless rare branches audited with limitations explicit.
- [ ] Reconcile complete ordinary crafting/chalk/material acquisition into every mandatory tier and guide page, with exact building/activation steps.
- [ ] Review residual familiar helpers and consolidate parent progression/quest teaching matrix; avoid repeating closed finite inventories.
- [ ] Run gameplay acceptance later only under applicable authorization; no runtime PASS from this research.

## Unknowns and next research

Current remaining work is the guide-to-walkthrough reconciliation and residual helper semantics listed immediately above. All ritual/processing resources and the remaining244 crafting/building/special records now have exact-JAR inventories. See [all931 recipes and230guide-entry crosswalk](occultism-pinned-crafting-guide.md). Historical revision notes preserve what was unknown at that time; they are not the current source-access state.

Loaded-tag/config resolution, actual conditional Draconium/uranium availability, protection behavior, guide rendering, cross-mod Ars behavior and gameplay acceptance remain unrun. Quest authoring is a later separate phase and no chapter was edited.

## Revision notes

- 2026-11-15 — Added source-backed progression and compatibility profile from the matching 1.227.0 release source. Found the quest chapter is only an optional empty checkmark; recorded miner/ore-gate and storage issues; left quest/runtime untouched. Profile remains researching pending detailed recipe/output audit and client verification.
- 2026-11-16 — Cross-checked Occultism's conditional Draconium miner recipe against the selected Draconic Evolution source's four-entry c:ores/draconium tag and Occultism's Foliot-inclusive miner tag. Corrected the prior claim about an Eldritch Draconium storage output: its source call is commented out in 1.227.0. Uranium/Yellorium tag availability and loaded-pack behavior remain unverified.
- 2026-11-16 — Mapped Occultism's storage progression from its 1.227.0 release source: Dimensional Matrix, Controller Base, assembled controller, default capacities, five stabilizer upgrades, six-direction placement logic and Djinni remote. Recorded the stabilizer line-of-sight wording and remote shift-use wording discrepancies, plus unloaded-chunk access and safe capacity-reduction behavior. No quest/runtime edits; exact JAR parity and client validation remain open.

- 2026-11-16 — Occultism crusher follow-up traced the 1.227.0 CrushingRecipe/CrusherJob/config path and generated raw-iron recipe. Separated tier eligibility, operation count, time multiplier and output multiplier; documented default 2/3/4/6 dust and 400/200/100/60 tick source thresholds, the anti-loop opt-out, invalid-input return, CrusherJobEvent timing and full-extractor remainder loss. All recipe-by-recipe, effective-config, tag-resolution and client checks remain open. No quest/runtime edits.

## Exact artifact closure — 2026-11-16

[Pinned ritual crosswalk](occultism-pinned-rituals.md) inventories all158 recipes/11 factory channels, gradual consumption/refund, saved recipe identity, native timing and actual entity/item output. Exact SHA512 matches pin. Remaining job/familiar/processing/storage/Otherworld gaps remain; main still researching.

See [all529 pinned processing/miner records](occultism-pinned-processing.md) for240crushing/194miner/54crystallize/27spirit-fire/14trade, typed tags/conditions/baseoutputs and exact worker/mineshaft insertion/loss behavior. Those recipe inventories are closed; loaded tags and full remaining helper/familiar branches are not gameplay accepted.

[Pinned familiar/tool lifecycle](occultism-pinned-familiars-tools.md) maps ownership/range/Curios transport, seventeen choice roles, Blacksmith cost/timing, death recovery and key trades, plus Spirit Fire stack conversion and chalk/tool contracts. Complex AI/fusion/rare branches and Otherworld/storage closure remain explicit.

[Pinned storage/access/harvest hooks](occultism-pinned-storage-harvest.md) now verifies native capacities, six-direction stabilizer search, remote selection/load behavior, Otherworld block collection, souls/heads and Dragon Greed/Blacksmith resource semantics. Full familiar AI and remaining transactional/visibility branches remain open.


Pinned worker follow-up: [job, transport, smelting, farming, lumberjack and time/weather contracts](occultism-pinned-worker-jobs.md). This closes major method-level gaps while retaining the remaining UI/dispatcher/familiar reconciliation work.


[Pinned Battlefield and capture devices](occultism-pinned-devices-capture.md) separates ordinary/Fragile/Trinity capture, virtual loot costs/failure/output loss, and literal Nether Star/Dragon Egg override paths from the earlier miner-only audit. Wormholes/satchel controls remain source follow-through.
