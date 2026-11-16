# Occultism — mechanics and gameplay profile

| Field | Value |
| --- | --- |
| Manifest ID / actual mod ID | occultism / occultism |
| Category / parent or addons | magic; related profiles: [Ars Nouveau](ars-nouveau.md), [Ars Ocultas](ars-ocultas.md), [Ars Unification](ars-unification.md) |
| MC / loader / pack target | 1.21.1 / NeoForge / test/full-integration at f6f1c5894ffd1ebcdcd4fd5d0989375fcc4d8f78 |
| Pinned version / artifact / hash | occultism-1.21.1-neoforge-1.227.0.jar; SHA-512 063d775420e1efa916d1ed94f3f5bfc3b00b976b1de6fc333eda9886ea3665aa38254407a18469ed0a334bcac67b204392c9bf197fc10916ce36a9e29abd5ef0 (mods/occultism.pw.toml) |
| Source snapshot | release/v1.21.1-1.227.0, commit [990303ea95ba057791b7d407d6cac5151f66d799](https://github.com/klikli-dev/occultism/tree/990303ea95ba057791b7d407d6cac5151f66d799); version-aligned, binary equivalence not tested |
| Guide/documentation version gap | Source-generated Dictionary of Spirits at matching release; exact pinned JAR archive not available for comparison |
| Profile status | researching |
| Evidence reached | Pack pin and full matching upstream source archive/guide datagen inspected; pack quest file and integration docs inspected; no client test |
| Reviewed | 2026-11-15 |
| Quest ownership | Existing Occultism FTB chapter is a one-quest empty placeholder; not edited in this knowledge pass |

## Executive gameplay map

**What the player actually does:** Occultism is ritual and spirit work. The player finds Demon's Dream seeds by breaking grass, makes the Dictionary of Spirits, then follows it to harvest or create Otherworld materials and draw chalk pentacles. A Golden Sacrificial Bowl activates a ritual using a bound Book of Binding; other bowls hold item ingredients, and some rituals ask for a nearby sacrifice or item-use event. Rituals bind, possess, summon, craft, or trade with spirits. Bound spirits become workers such as crushers, smelters, miners, farmers, transporters, cleaners, and machine operators. The progression also unlocks familiars, remote item storage, Otherworld exploration, and the Dimensional Mineshaft. Four spirit tiers—Foliot, Djinni, Afrit, Marid—lead to unbound and eldritch branches and higher-tier materials. The matching source book has six categories and separate entry maps; the ritual chapters are much broader than the single pack quest. [source: release commit; datagen/OccultismBookProvider.java, GettingStartedCategory.java, PentaclesCategory.java, SummoningRitualCategory.java]

**Why it matters in TMM:** Its strengths are item-only remote storage, tiered ore processing, automated pocket-dimension mining, and spirit labor. docs/DESIGN.md explicitly places Occultism storage in the intended core and allows magical satchels. docs/PROGRESSION.md specifically proposes the Eldritch Ancient Miner as a controlled source for Replication's Deepslate Replica Ore and considers uranium/draconic dust extraction. These make miner outputs and recipe gates progression-critical; the docs also say those gates are not implemented.

**Quick start:** Break grass until Demon's Dream seeds drop; grow the plant and combine a seed or fruit with a book to craft the Dictionary. Read Getting Started → Lifting the Veil. Make Spirit Fire by throwing Datura onto the ground and igniting it with flint and steel. Use Spirit Fire to convert materials and purify impure chalk. Prepare White Chalk, candles, sacrificial bowls and a Golden Sacrificial Bowl; open Pentacles → Foliot → Summon Foliot for the exact diagram and recipe. Craft a Foliot Book of Binding and use the Dictionary to bind a listed spirit name. Put the ritual ingredients in bowls within the documented radius and activate the Golden Bowl with the bound book. Chalk pattern, particles and bowl state provide operational feedback. [source: matching GettingStartedCategory.java and PentaclesCategory.java]

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
| Crusher | Accepted item stack, usually raw ore | Guide examples give 2/3/4/6 dust per ore for Foliot/Djinni/Afrit/Marid | Recipe-specific; rate and maximum batches require source/config audit and client measurement |
| Otherworld mining | Dimensional Mineshaft plus supported bound miner | Item stacks stored in the mineshaft | 194 matching-source miner recipes: 108 ordinary ore recipes, 59 eldritch, 9 deeps, 5 master, 11 basic-resource and 2 debug recipe files. Foliot, Djinni, Afrit and Marid miners are all included in the ordinary ore tag; see source-confirmed gate findings below |
| Storage | Storage Controller, actuators, stabilizers, remotes | Large remote item storage and crafting/automation interfaces | Item system; no fluid or FE storage channel found. Structure/capacity needs audit |
| Familiars | Familiar rites; familiar ring and binding items | Player effects, utility, combat, protection, mobility or upgrades | Specific effect, food, costs and death behavior vary; client verification needed |
| Advanced rites | Battlefield, extractor, resurrection, satchels, Iesnium tools, chalices | Encounters, special entity/item results, improved ritual prep or materials | Each has its own recipe and condition; not one linear final tier |

## Material / capability input-output contracts

| System | Input kind | Output kind | Trigger / automation | Evidence / limit |
| --- | --- | --- | --- | --- |
| Spirit Fire | Item entity accepted by a specific spirit_fire recipe | Item stack in world or bowl arrangement | Item collision; optional sacrificial bowl above fire | Recipe JSON; no general fluids/FE |
| Ritual | Bound book plus recipe item stacks; optional named sacrifice or item-use event | Entity, item stack, world state or effect | Golden Bowl activation; inputs consumed per ritual | ritual JSON and matching guide page; conditions vary |
| Crusher Spirit | Input item stack accepted by crushing recipe | Dust/item stack | Worker operation | Native crushing recipes; source guide gives tiered dust counts |
| Spirit miner | Miner type plus Mineshaft | Configured item stacks | Internal mining cycle | Exact resource table and cycle need audit |
| Storage | Item stack or remote operation | Stored/retrieved item stack | Native storage controller/interface | No fluids, XP, mana, blood or FE channel found |
| Familiar | Ritual inputs and state/events | Entity, player effect, AI action or protection event | Passive/tick/event | Familiar and advancement sources; exact FTB integration unknown |

No evidence in inspected source supports arbitrary fluid, FE, mana, blood, or XP inputs. Rituals can use world entities/events as conditions and can output entities, items and world effects; they are not generically item-only recipes. Every recipe's exact input and output contract needs its own JSON/source review.

## Acquisition, renewability and output economics

- Demon's Dream starts as a grass drop and is renewable after growing the crop.
- Spirit Fire transforms only registered recipe inputs; it is not a fuel-powered machine or general transmuter.
- Tiered crushers have high multiplication. Source guide examples state 2/3/4/6 dust per ore operation, but exact supported ores, max batches and time depend on recipe and tier. Do not turn those outputs into a throughput estimate.
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

## Complete player progression / walkthrough blueprint

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
| Mechanics/config | src/main/java/com/klikli_dev/occultism/common/ and config/Occultism{Client,Server}Config.java | Source behavior/config surfaces; individual defaults not fully enumerated |
| Official start | [Official Occultism project page](https://modrinth.com/mod/occultism) | Grass → Demon's Dream → Dictionary/guide; not detailed recipe authority |
| TMM design | docs/DESIGN.md, docs/PROGRESSION.md at integration base f6f1c5894ffd1ebcdcd4fd5d0989375fcc4d8f78 | Owner design intent and unimplemented gate caveat |
| Draconium common ore/storage tags | [Occultism miner recipe JSONs](https://github.com/klikli-dev/occultism/tree/990303ea95ba057791b7d407d6cac5151f66d799/src/generated/resources/data/occultism/recipe/miner); [DE c:ores/draconium](https://github.com/Draconic-Inc/Draconic-Evolution/blob/6251e32bc93eb4bfcd910236ec1eaa6546516229/src/main/generated/data/c/tags/item/ores/draconium.json); [Occultism miner tag](https://github.com/klikli-dev/occultism/blob/990303ea95ba057791b7d407d6cac5151f66d799/src/generated/resources/data/occultism/tags/item/miners/ores.json) | Both version-matched source snapshots list tag entries satisfying the recipe conditions, including Foliot unspecialized; loaded TMM registry and pinned binary comparison unverified. |
| Quest state | config/ftbquests/quests/chapters/occultism.snbt | One optional checkmark, XP and paper; no lesson |
| Bag tag | kubejs/data/tmm/tags/item/portable_bags.json | Four Occultism bag IDs included; consumer behavior unknown |
| Ars bridge | Ars Unification profile/source cited there | Source-supported lead; no client run here |

## Disposable-world test plan

| Test | Positive and boundary case | Expected mechanic and quest credit | Performed |
| --- | --- | --- | --- |
| Dictionary/Spirit Fire | Fresh world: seed→book; valid Spirit Fire input vs unrelated item | Valid conversion only; guide opens; quest item task distinct from transformation | no client |
| Ritual waits | Valid Foliot pentacle vs wrong color/missing bowl; test rite requiring sacrifice with and without named event | Correct setup proceeds; wrong/waiting state observable; task credit checked separately | no client |
| Crusher | Same ore at four tiers, record output and ticks; test unsupported input | Confirm recipe yield/speed and no unsupported conversion; task matches real operation | no client |
| Miner gates | Verify the loaded occultism:miner/ores/draconium_ore with selected DE ore tag; test Foliot success and missing-result-tag boundary. Check whether c:ores/uranium, c:storage_blocks/raw_uranium and c:storage_blocks/raw_yellorium resolve before testing conditional recipes; confirm no Eldritch Draconium recipe. Audit Replica Ore/protected drops. | Draconium path outcome and conditional uranium paths are recorded separately; only intended sources/tier produce gated materials | not run; required before walkthrough-ready |
| Storage/bag | Base controller and stabilizer upgrades; insert/retrieve; inspect all four tag consumers and team ownership | Capacity/link behavior and tag purpose confirmed | no client |
| Ars Unification | Test Crusher Spirit recipe under exact pins, including output and unavailable target | Expected native recipe loads and operates | no client |

**Status:** researching. Guide graph, broad tier gates and high-risk issues are source-mapped. Recipe-by-recipe progression, miner outputs, storage structures, advanced/familiar mechanics, exact jar comparison and client tests remain open.

## Unknowns and next research

- [ ] Reconcile each major ritual and crushing JSON with its guide entry; record inputs, outputs, events and IDs.
- [ ] Client-test the source-confirmed Foliot Draconium route against the exact pinned Occultism/DE artifacts; resolve common uranium/raw-block tag membership before describing those routes as available. Confirm the commented-out Eldritch Draconium output remains absent; audit all 194 recipe files and other mod/datapack recipes for Replica Ore and protected boss outputs.
- [ ] Extract storage diagrams, capacities, remotes and automation interfaces.
- [ ] Close familiar, resurrection, battlefield/extractor, Otherworld tool and advanced chalk branches.
- [ ] Obtain exact pinned JAR and test guide, tasks, Ars Unification bridge, and bag-tag consumer behavior in client.
- [ ] Reconcile the chapter against this matrix when quest authoring is authorized; this pass did not edit it.

## Revision notes

- 2026-11-15 — Added source-backed progression and compatibility profile from the matching 1.227.0 release source. Found the quest chapter is only an optional empty checkmark; recorded miner/ore-gate and storage issues; left quest/runtime untouched. Profile remains researching pending detailed recipe/output audit and client verification.
- 2026-11-16 — Cross-checked Occultism's conditional Draconium miner recipe against the selected Draconic Evolution source's four-entry c:ores/draconium tag and Occultism's Foliot-inclusive miner tag. Corrected the prior claim about an Eldritch Draconium storage output: its source call is commented out in 1.227.0. Uranium/Yellorium tag availability and loaded-pack behavior remain unverified.
