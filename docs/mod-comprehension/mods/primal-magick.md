# Primal Magick — mechanics and gameplay profile

| Field | Value |
| --- | --- |
| Manifest ID / actual mod ID | primal-magick / primalmagick |
| Category / parent or addons | Magic; research, spellcraft, wand mana, elemental disciplines, attunement, rituals and magitech devices |
| MC / loader / pack target | Minecraft 1.21.1 / NeoForge / TMM test/full-integration |
| Pinned version / artifact / hash | mods/primal-magick.pw.toml; primalmagick-neoforge-1.21.1-6.0.7.jar; CurseForge project 545271, file 6677090; SHA1 165a849fe2b95355067ede9cda014dec08129731 [pin] |
| Source snapshot | Official daedalus4096/PrimalMagick tag 6.0.7. Gradle metadata declares Minecraft 1.21.1, mod ID primalmagick and version-aligned NeoForge 21.1.132 baseline; exact JAR equivalence not verified. |
| Guide/documentation version gap | Exact 6.0.7 project overview and tag README/changelog available. The Grimoire is the in-game guide/research UI; individual discipline and tutorial page names were not extracted from the pinned artifact. |
| Profile status | researching |
| Evidence reached | Exact pack pin and CurseForge release metadata; official 6.0.7 source tag metadata/README/changelog; official project overview; no JAR inspection or in-pack tests |
| Reviewed | 2026-11-16 |
| Quest ownership | No Primal Magick quest chapter reconciled. No quest files changed. |

## Executive gameplay map

**What the player actually does:** Primal Magick begins with exploration. The official 1.21.1 project overview directs the player to locate an ancient marble shrine, enter it and approach its central device; after sleeping, a player with magical potential receives an inspiration/dream event. The player gathers magic dust from the ground beneath shrines, crafts it with a stick to make a wand, then waves the wand at a bookshelf. The rest of the path unfolds through the Grimoire: observe and research items, unlock disciplines and recipes, make arcane tools and workstations, acquire and distribute wand mana, craft spells, attune to magical sources, and advance into rituals and magitech. The 6.0.7 changelog confirms later subsystems including Mana Orbs, spell tomes, mana injectors/relays, research disciplines, magitech devices, ritual tools and multiple spell/attunement features. The full discipline/research graph, costs, item routes and endpoint still need exact in-game guide/data review.

**Why it matters in TMM:** Primal Magick is an exploration-led knowledge and spellcrafting school with a broad internal progression graph. Its study loop uses item affinities/observations, research, Grimoire unlocks and discipline-specific progression. That differs from quick-access spell schools: players discover leads, then use the Grimoire to learn recipes and goals. It has its own mana economy, enchanting/gear, devices and rituals, so quest text must explain its specific operations instead of assuming players know another mod's systems.

**Quick start (official overview, exact final outputs still need client confirmation):**
1. Explore the Overworld for an ancient marble shrine; enter and approach the mysterious device at its center.
2. Sleep after visiting. Watch for the inspiration/dream event that signals magical potential.
3. Collect the magic dust found in the ground beneath the shrines; combine it with a stick to make the starter wand.
4. Use the wand on a bookshelf as directed by the official overview to open the Grimoire path. Confirm the exact result, input block and whether it is consumed in the pinned client.
5. Read the first unlocked Grimoire entries; use the book's leads and research controls to acquire observations, discover recipes and unlock disciplines.
6. Follow each study path into its devices/spells, while recording the exact research prerequisites and any environment or structure gates.

### Player-value snapshot

| Five standout capabilities | Three genuinely early practical unlocks | Three candidate integrations |
| --- | --- | --- |
| Exploration-triggered magical awakening; Grimoire-led recipe and research discovery; wand-based spellcasting and mana management; multiple research disciplines and attunements; ritual and magitech branches with a growing device network. | The shrine gives the first entry trigger; the starter wand is made from a stick and shrine-ground magic dust; wanding a bookshelf opens the Grimoire path. Their exact first spell, utility recipe and resource costs remain to be verified. | Item affinities can be data-defined for modpack items; magitech devices include mana-relay/injector networks; item analysis and essence/crafting loops may complement the wider magic roster. These are candidate cross-mod leads, not tested compatibility claims. |

### Subsystem coverage at a glance

| Subsystem / progression branch | Mechanics evidence | Progression completeness | In-game guide/JEI reference | Client validation | Blocker / next action |
| --- | --- | --- | --- | --- | --- |
| Ancient shrines and awakening | Official 1.21.1 overview describes shrine exploration, central device, sleep and inspiration/dream sequence | Partial | Early Grimoire entry/advancement page unknown | not run | Inspect structure, first visit trigger, sleep condition and observable success |
| First wand and Grimoire | Official overview says stick + shrine-ground magic dust makes wand, then wand at bookshelf | Partial | Grimoire is the in-game guide; exact entry names unknown | not run | Confirm exact block/item transformation, guide acquisition, book contents and first tutorial |
| Observation, affinities and research | 6.0.7 source maps project generation/completion, material selection, reward and knowledge rules | Partial | Grimoire still not extracted | not run | Audit observation scans, all registered project templates and material-type edge cases |
| Arcane crafting and spellcasting | Official project overview describes hundreds of recipes, Arcane Workbench and spells; source changelog tracks wands, spells and recipe costs | Partial | Grimoire recipe/spell entries and JEI | not run | Enumerate recipe discovery and first spell construction/casting |
| Mana and wand progression | Changelog describes Mana Orbs, wands, paired wand passive regeneration, Mana Fonts, chargers, relays/injectors and mana devices | Partial | Grimoire chapters for mana and devices unknown | not run | Exact capacities, costs, generation and networks need artifact/guide extraction |
| Disciplines and attunement | Official overview describes several disciplines from manaweaving to dangerous rituals; changelog adds discipline unlock toasts and attunement mechanics | Partial | Grimoire discipline pages, upcoming leads and attunement entries | not run | Map all branches, gates, choices and how to gain/lose attunement |
| Rituals and magitech | Official overview mentions dangerous rituals; source changelog names ritual props/tools and magitech device progression | Partial | Exact ritual entries/setup pages unknown | not run | Map valid props, geometry, stability, salts, costs, outputs and failure cases |
| Equipment, enchantments and side systems | Changelog records robes/warded armor, staffs, spell tomes, mana orb enchantment, ward/attunement gear, Treefolk/Pixies, etc. | Partial | Grimoire and JEI details not enumerated | not run | Inventory all major branches and interactions |

## Guidebook onboarding — required before the player's first complex step

- **Guide name and access:** The Grimoire is Primal Magick's in-game reference and research interface. The official 1.21.1 overview starts with a wand made from a stick and magic dust found beneath a shrine, then instructs the player to wave it at a bookshelf.
- **Earliest attainable stage:** After the player finds an ancient marble shrine, experiences the awakening/dream sequence and makes the first wand. Exact bookshelf type and whether the wand is consumed must be checked in the pinned version.
- **First instructional quest:** “Find the first shrine and open your Grimoire” should teach the shrine/device/sleep/wand/book sequence in order. It should use an actual awakening/Grimoire unlock signal if FTB can detect it; shrine or wand possession is only a proxy.
- **Reference map:** Grimoire pages are the primary in-game route. Version 6.0.4 added navigation buttons from certain entries to related Grimoire content, unread-entry highlighting and new-discipline toasts. Changelog also names an affinity index and upcoming research section. Exact page titles are still unverified.
- **Where the guide may need support:** Research criteria may involve observations, crafted items, rituals, world exploration, attunement or device operation. Quest text should add the specific sequence when a Grimoire entry does not give actionable build/setup detail.
- **Other in-game help:** JEI integration is referenced in official source history, including Arcane Workbench catalysts for vanilla crafting and Infernal Furnace catalyst for vanilla smelting. Confirm current JEI categories and recipe pages in the pinned client.

## Systems and full mechanics inventory

| Subsystem | How accessed | Inputs + costs | Effects / outputs | Limits, failure conditions and source |
| --- | --- | --- | --- | --- |
| Shrine initiation | Explore Overworld marble shrine; enter and approach central device | Structure presence and player approach; exact required interaction/tick unknown | After sleeping, magical potential may trigger inspiration/dream sequence | Whether every shrine works, whether device interaction is needed, and event detector are unverified. [docs: official 1.21.1 overview] |
| Starter wand and Grimoire | Craft wand from stick + magic dust under shrines; use wand on bookshelf | Stick and shrine-ground dust; exact count and bookshelf block variant unknown | Starter wand and Grimoire/research access pathway | The summary page says “wave it at a bookshelf”; verify exact transformation and consumed inputs. [docs: official 1.21.1 overview] |
| Item affinities and observations | Study/analyze items using mod research tools and Grimoire | Items/entities may provide observations; exact scanner and costs not mapped | Knowledge, affinity entries and unlock prerequisites | Official README says affinities derive from recipe sources, while recipe-less base items require datapack affinity definitions. This is not proof that every item can be scanned. [source: tag 6.0.7 README/changelog] |
| Arcane research | Grimoire disciplines, research projects and table(s) | Item/material/observation/theory requirements vary; the Research Table project transaction is now source-mapped, but exact template options and Grimoire progression remain incomplete | Research and recipe/disciplines unlock | The game has hidden leads and research pages; do not use item possession as proof of research. 6.0.7 source maps randomized Research Table projects and costs. |
| Arcane Workbench / recipes | Transform/craft workstation and discover recipes in Grimoire/JEI | Exact recipes, catalyst and mana costs need extraction | Hundreds of arcane recipes and spells | v6.0.1 beta notes allow all blocks in common crafting tables block tag to transform into Arcane Workbenches; current 6.0.7 recipes need pin check. [source: changelog] |
| Wand spellcasting | Cast from wands/staves/spelltomes as unlocked | Mana cost modified by wand/spell components; exact formula per spell not enumerated | Spell actions and effects; spell catalog not mapped | v6.0.2 changed mana effectiveness to diminishing returns and recipe mana precision to hundredths; 6.0.7 fixed wand mana reduction bug. [source: changelog] |
| Mana storage and portable supplies | Wands, paired wands and Mana Orbs | Mana; exact capacities/refill recipe/consumption not inspected | Stored/portable mana and passive paired-wand regeneration | Mana Orbs supplemented mana on the go in v6.0.6; v6.0.7 fixed an issue that could sharply reduce stored wand mana. [source: changelog] |
| Mana Fonts and network | Mana Fonts, Mana Injectors and Mana Relays | Font mana and adjacent device connections; exact consumption, distance and input mode not mapped | Networked mana supply to compatible magitech devices | v6.0.5 adds injectors that pull mana from fonts and relays that extend network range; Artificial Fonts were nerfed and Heavenly Fonts buffed. [source: changelog] |
| Magitech processing | Devices such as furnaces, charger, casks, extractor and water-processing devices | Item/fluid/mana inputs vary by machine; exact processing chains unknown | Processed materials, mana operations and device outputs | Devices have mana capacities and changed costs with networking; side hopper handling was added for compatible devices. Infernal Furnace cannot accept Ignyx fuel through hopper per changelog note. [source: changelog] |
| Attunement and discipline | Unlock disciplines and attune to an affinity/source | Research gate and source-specific actions unknown | Attunement bonuses/penalties and branch progression | Attunement Shackles can suppress some attribute modifiers per changelog; Humming Artifacts can be unattuned via recipes since 6.0.1 beta. [source: changelog] |
| Rituals | Ritual page and its required tools/props | Props, salt connection and stability requirements; costs/geometry unknown | Ritual effects/resources | Changelog fixes ritual stability when props are disconnected from salt and premature tool-rune unlocks; exact ritual recipes and failure costs remain unknown. [source: changelog] |
| Enchantments, gear and side systems | Research / craft / enchant robes, armor, wands, staves and other equipment | Exact materials and enchant costs unknown | Mana, spell power, warding, mobility and combat benefits | v6.0.6 extends Mana Effectiveness to robes/offhand and Spell Power to spelltomes; exact caps and balance unknown. [source: changelog] |
| Affinity pack-maintainer API | Data-pack definitions and /pm affinities commands | Affinity source lists; exact schema not inspected | Derived affinities for items; lint/generateDatapack reports missing sources | README says recipe-derived affinities cover recipe-bearing items; recipe-less base items need datapack definitions. Commands may write temporary datapack files; no TMM override was changed. [source: tag 6.0.7 README] |

## Material / capability input-output contracts

| System | Input kind + exact ID/tag/event | Input qty/formula; consumed/remainder | Output kind, qty and location | Trigger/cadence; range/capacity | Automation/interface and evidence |
| --- | --- | --- | --- | --- | --- |
| Shrine awakening | World structure + player approach + sleep event; exact registry IDs unknown | No item cost identified; exact actions/timing unknown | Player research/inspiration state and dream candidate | Structure/world event followed by sleep | Structure/advancement trigger not inspected |
| First wand | Stick item + magic dust from shrine grounds | Official overview says combine stick with dust; quantity/recipe form unknown | Wand item | Crafting operation | Exact recipe/ID and recipe type require JEI/artifact |
| Grimoire access | Wand-use event on bookshelf candidate | Input preservation and block replacement unknown | Guide/research interface; exact output not verified | Player interaction | Need client-confirmed result |
| Affinity/observation | Item recipes and resource affinity data; scans of items/entities possible but exact trigger not mapped | Data-derived relations; observation counts unknown | Research data/knowledge | Player research interaction; range unknown | Datapack affinity definitions and mod commands documented for maintainers |
| Wand mana | Mana Orbs / font / paired-wand regeneration | Mana values, capacity and rate unknown | Mana stored in wand or available for casting | Charge event/passive tick; exact network ranges unknown | Charger, Orb and paired-wand behavior need disposable-world tests |
| Mana distribution | Font -> Injector -> compatible device; Relay extends network | Consumption and transfer amount unknown | Mana at device | Networked transfer; distance/capacity unknown | Native source-changelog feature, exact adjacency/range not yet established |
| Device processing | Item/fluid/mana input varies by magitech block | Exact quantities/consumption/order unknown | Machine-specific item/fluid/effect | Recipe/tick operation | Hoppers supported on compatible devices from side/top since v6.0.5; exact supported devices need check |
| Ritual | Ritual tool/props + salt-linked stability candidate | Exact consumed inputs and failure costs unknown | Ritual output/effect unknown by ritual | Activation event and stability rules | Must map each ritual individually; never infer arbitrary inputs |

### Theorycrafting projects — source contract

The Research Table runs a randomized submission loop separate from Grimoire research-entry unlocks. This contract is traced from the 6.0.7 source snapshot; exact pinned-JAR equivalence and pack-config effects remain unverified.

- **Start:** opening a project requests a weighted random project template. Templates can be gated by research requirements and may require one of their configured aid blocks within five blocks of the Research Table (an 11×11×11 search volume). The nearby-block scan is attempted only when that area is loaded.
- **Materials:** templates provide weighted material options. Eligible options are filtered by each material's research requirement and, for item materials, an afterCrafting stat. The default number of candidate materials is min(4, 1 + floor(successfulProjects / 5)), unless the template overrides it. The project randomly chooses distinct options. Each starts unselected; the player selects which available materials to submit.
- **Success chance:** unless overridden, base chance is max(0, 0.5 - 0.1 * floor(successfulProjects / 3)). Each selected candidate adds an equal share of (1 - baseChance) / candidateCount, capped at 100%. Selecting every candidate therefore reaches 100% under this formula.
- **Reward:** a successful project awards floor(32 * (templateRewardMultiplier + sum(selectedMaterialBonusRewards))) theory points, with the THEORY progression value of 32 points per knowledge level. theorycraftSpeed config halves the result in SLOW mode or doubles it in FAST mode. Other template rewards are granted only on success. The successful-project statistic increments only on success and drives later material count/base chance.
- **Submission costs:** the server completion handler consumes writing supplies first: one paper and, if the writing implement is damageable, one durability. It then checks selected requirements, consumes selected materials, and rolls success. A failed roll still spends selected materials but grants no theory/rewards. The handler creates a fresh random project after a valid completion packet even if the selected requirements were unsatisfied. The UI/server boundary and failure feedback need client validation.
- **Material-type discrepancy:** ItemProjectMaterial and ItemTagProjectMaterial honor consumed=false, but ExperienceProjectMaterial.consume always removes the configured XP levels and ObservationProjectMaterial.consume always subtracts count × 16 observation points, even though both types store and expose the consumed flag. Observation requirements test whether the raw observation-point pool is at least count, while consumption subtracts count × 16; this makes small thresholds and nonconsumed observations a source-level boundary worth testing. Record as an implementation discrepancy, not verified in-game behavior.
- **Quest implication:** item possession alone cannot verify a completed research project. The source increments RESEARCH_PROJECTS_COMPLETED only after a successful project; check whether FTB can detect that stat/advancement before designing a detector. Teach that selected requirements are at risk on a failed roll, and avoid describing XP/observation as reusable until tested.

## Acquisition, renewability and output economics

- **Starter dust:** the official overview places the magic dust in the ground beneath ancient shrines. Exact block/item, harvest tool and repeatability are unverified.
- **Wand mana:** current 6.0.6/6.0.7 notes describe portable Mana Orbs, wand charging and paired-wand passive regeneration. Exact refill loop, Orb uses and generation costs are unknown.
- **Mana Fonts:** v6.0.5 adds mana injectors/relays and rebalances device storage/consumption. Artificial Fonts have reduced regeneration; Heavenly Fonts increased. Exact source rates and tier differences need artifact/source inspection.
- **Research materials:** Grimoire research and projects form a long progression path; exact observation, theory and item sinks are not mapped.
- **Affinities:** The official maintainer README says recipe-bearing items derive affinities from their recipes; recipe-less base items may need explicit datapack data. This may affect pack-added items and should be audited only when authorized.
- No numeric throughput, renewable material, mana rate or item-multiplication loop is asserted without exact formulas and disposable-world measurements.

## Complete player progression / walkthrough blueprint

**Completion test:** partial only. The official first-entry route is documented, but all discipline unlocks, research tiers, spell paths, ritual branches, device upgrades and the end objective are not yet mapped.

| Path and step | Gate or choice? | Exact prerequisite / materials / world condition | Player actions: obtain, build, activate, use | Unlock / visible success / next step | In-game guide section or JEI reference | Proposed quest/how-to, detection and reward | Evidence / coverage |
| --- | --- | --- | --- | --- | --- | --- | --- |
| 0. Find an ancient shrine | Required | Overworld exploration; exact structure placement/biome list unknown | Enter marble shrine and approach central device | First magical exposure; observe any message/sound/advancement | Grimoire start entry after awakening; exact name unknown | Teach how to recognize and enter shrine; detect advancement/interaction if available, not structure loot alone | Official overview; partial |
| 1. Awaken | Required | Shrine/device visit and then sleep; exact order/conditions unknown | Return/rest, wake and check for inspiration/dream | Magical potential event and first study route | Grimoire first chapter unknown | Require confirmed dream/awakening event; sleep/task alone is a proxy | Official overview; partial |
| 2. Craft starter wand | Required | Stick plus magic dust from shrine ground; exact quantities unknown | Harvest dust and combine with stick using correct recipe | First wand and ability to use wand interaction | JEI recipe and first Grimoire entry | Teach dust location and recipe; detect wand craft only as proxy | Official overview; recipe unverified |
| 3. Open Grimoire | Required guide onboarding | Starter wand and bookshelf type unknown | Wave wand at bookshelf | Grimoire/research interface becomes available | Grimoire pages; exact initial screen unknown | Confirm exact block result/access; reward basic guide-side supplies, not progression skips | Official overview; partial |
| 4. Learn research/observations | Required core loop | First Grimoire entry; item/scan/research gates unknown | Follow lead, observe/analyze required target, complete first project | Research unlock/discipline or recipe | Named research and affinity pages unknown | Teach exact research action and visible state; avoid item possession task | Official README/changelog; partial |
| 5. Build arcane crafting setup | Required core progression | Workbench recipe and transform trigger unknown | Create Arcane Workbench; follow Grimoire/JEI recipes | More arcane recipes become craftable | Arcane Workbench category unknown | Describe the exact transformation/setup and catalyst after pin review | Official overview/changelog; partial |
| 6. Cast and manage first spells | Required core branch | Wand/spell recipe and mana source unknown | Craft/select spell, charge wand, cast and verify effect | New capabilities and research progression | Spell entries, mana pages unknown | Include actual cast and a boundary/failure case; item craft is only proxy | Official description/changelog; partial |
| 7. Choose/unlock disciplines | Multiple branches; player choice likely | Exact names, prerequisites, mutually exclusive choices unknown | Follow Grimoire leads, unlock discipline, perform named research | New recipe/spell/equipment domains | Discipline pages including Upcoming sections | Teach gates and choice implications; don't force all choices if mutually exclusive | Official overview/changelog; partial |
| 8. Mana network and magitech | Advanced branch | Mana Fonts, Injector/Relay and device recipes; world/structure conditions unknown | Supply fonts, connect relay network, feed compatible device, verify processing | More scalable mana distribution and automation | Mana Network/Devices pages unknown | Teach network geometry and device input/output; detect operation rather than block ownership | Changelog v6.0.5–6.0.7 |
| 9. Attunement and rituals | Advanced/specialization branches | Research, source and ritual-prop requirements unknown | Attune, use tools and build a salt-connected stable ritual setup | Bonuses, ritual effects or new unlocks | Attunement and ritual entries unknown | Cross-link exact Grimoire pages; explain safety/cost and fail conditions | Official overview/changelog; incomplete |
| 10. Endgame / completion | Not established | Highest discipline/ritual/device/spell gate unknown | Must map before defining completion | No exact terminal objective yet | Grimoire end pages not inspected | Do not claim archmage completion before source graph is complete | Open question |

## Internal combinations and overlooked mechanics

| Internal combination | Full chain | Benefit | Limitation / exploit potential | Evidence |
| --- | --- | --- | --- | --- |
| Shrine discovery to research | Locate shrine -> approach device -> sleep -> dream/inspiration -> dust -> wand -> bookshelf/Grimoire | Strong world-discovery opening with a natural guide handoff | Exact event conditions and trigger detection remain unknown | Official 1.21.1 overview |
| Recipe affinities to item research | Item recipe inputs/source chain -> derived affinities -> item knowledge and research | May automatically integrate many other pack items into this school | Recipe-less items require affinity definitions; cyclic or missing recipe sources may create empty affinity data | Official tag 6.0.7 README |
| Mana-networked magitech | Mana source/font -> Injector -> compatible device; Relay expands range | Supports automation without treating each device as isolated | Network rules, loss, range and device consumption must be measured | Changelog v6.0.5 |
| Paired wand + Mana Orb | Pairing/passive regeneration + portable Mana Orb supply | Sustains spell use away from stationary infrastructure | Exact passive rate, capacity, Orb refill and charging risks unknown; v6.0.7 fixed a serious stored-mana reduction bug | Changelog v6.0.6–6.0.7 |
| Grimoire navigation/unread markers | Unlock discipline -> toast/pulsing icon -> read new entry -> follow related-page button | Helps a new player find progression leads | Feature depends on 6.0.4+; exact page mapping still requires client review | Changelog v6.0.4 |
| Observation and research item loop | Analyze items/entities -> gain knowledge/observations -> unlock research -> discover recipes | Gives broad progression and potentially pack-item hooks | Exact observation sources, counts and repeatability unknown | Source changelog/Grimoire concepts; partial |

## Low-hanging setups useful in ordinary survival

### 1. Shrine-start wand and Grimoire — early
- **Goal and payoff:** Open the mod's own guide and first magical interaction.
- **Minimum entry and ingredients:** Find ancient marble shrine; sleep after approaching its center device; obtain a stick and dust from the ground beneath shrines.
- **Build/use steps:** Visit and enter shrine; approach device; sleep and check for dream; gather magic dust; combine with stick; wave wand at bookshelf as official overview directs.
- **Sustainability and limits:** Shrine density, dust amount/renewability and wand durability unknown; verify with fresh client.
- **Evidence / pack test:** Official overview gives the sequence; no gameplay test run.

### 2. First research/analysis operation — candidate after Grimoire
- **Goal and payoff:** Learn how knowledge unlocks recipes/discipline.
- **Minimum entry and ingredients:** Grimoire access; exact analysis device/materials unknown.
- **Build/use steps:** Follow first research lead, perform the specified scan/study, inspect visible unlock, then try the newly revealed recipe.
- **Sustainability and limits:** Exact target, observations and costs not mapped.
- **Evidence / pack test:** Official changelog describes analysis/research systems; no client test.

### 3. Font-fed magitech device — midgame candidate
- **Goal and payoff:** Feed a compatible device from a mana network.
- **Minimum entry and ingredients:** Mana Font, Injector and device; recipes/tier unknown.
- **Build/use steps:** Place Injector on a compatible device, supply the Font and use a Relay only if range requires it; process a verified recipe and observe mana/input/output.
- **Sustainability and limits:** Rates, distance, capacity, loss and accepted device list unknown.
- **Evidence / pack test:** v6.0.5 changelog; no client test.

## API, integration and pack-config surfaces

| Hook or customization surface | Namespace / exact file, API or serializer | Capabilities / constraints | Current TMM override? | Evidence |
| --- | --- | --- | --- | --- |
| Affinity data | Datapack-format affinity definitions; exact folder/schema needs source resource inspection | Supplies affinities for recipe-less base items; recipe-bearing items derive from recipe sources | No Primal Magick override identified in this pass | Official tag 6.0.7 README |
| Affinity commands | /pm affinities lint [all] and /pm affinities generateDatapack [all] | Detects items with no source affinity; can generate a datapack in configured Java temp directory | Not run | Official tag 6.0.7 README |
| Research/project data | Datapack registries for research entries and project templates; template fields include requirements, weighted material options, aid blocks, reward multiplier, base-chance/material-count overrides and rewards | Progression and research projects can be data-defined; exact TMM overrides still require repository audit | No TMM override identified in this pass | Version-aligned source ProjectTemplate / ProjectTemplates and ResearchManager |
| Config | v6.0.7 adds client option to render affinities as text rather than icons; exact config file/key unknown | Client display option; other gameplay config not inventoried | None identified | Exact CurseForge release notes |
| JEI integration | Arcane Workbench and Infernal Furnace are described as JEI catalysts in source history | Helps show crafting/smelting recipes through mod devices | No TMM override identified | Source changelog |
| Hopper/device I/O | v6.0.5 expands hopper input to device sides, in addition to top for compatible devices | Machine input automation; Infernal Furnace cannot accept Ignyx by hopper at that release | No TMM override identified | Source changelog v6.0.5 |
| Modded item affinity support | Recipe-derived affinities plus datapack definitions for recipe-less items | Could accommodate other TMM mods, but needs audits and may encounter items with empty/ambiguous recipe sources | No override identified | Official tag 6.0.7 README |

## Compatibility leads

| Other mod | Candidate link and why | Native / pack-built / would require bridge | Status / source |
| --- | --- | --- | --- |
| Other TMM crafting/loot mods | Affinities are derived from recipes; newly added items may gain source relations automatically | Native recipe-derived system; datapack for recipe-less items | Candidate; test cyclic/missing-source cases before recommending |
| Mana-consuming devices | Injectors and relays distribute mana among compatible devices | Native Primal Magick | Needs exact device roster, range and throughput testing |
| Quest system | Grimoire unlocks, discipline toasts, scans and device operations may offer quest detectors | Unknown | Need advancement/event/API inspection; item possession is not operation proof |
| Other magic mods | Separate mana, rituals, spells and recipe graph may complement other schools | No resource bridge established | Hypothesis only; inspect both ends before writing an interaction |

## Quest and teaching opportunities (research, not actual quests)

| Stage and teaching goal | Actionable in-game instructions | Named guidebook section or JEI recipe | Intended behavior vs truthful FTB detector / fallback | Reward direction | Coverage and gating risk |
| --- | --- | --- | --- | --- | --- |
| Shrine awakening | Locate shrine, enter, approach device, sleep and verify dream/inspiration | First Grimoire/advancement page unknown | Require awakening advancement or detected player state if available; sleep alone doesn't prove it | Small exploration/research supplies | Exact shrine trigger and structure conditions unknown |
| First wand/Grimoire | Gather dust under shrine, craft with stick, wand a bookshelf | JEI starter wand recipe and Grimoire start entry | Detect correct Grimoire unlock/converted block; wand possession is proxy | Basic school materials after recipe confirms safe | Must verify block type/consumption |
| Observe and research | Follow first lead, analyze exact target, submit required items/research | Grimoire research entry and matching workstation JEI page | Detect research entry/unlock, not item possession | Modest research materials | Need complete action/cost source |
| Arcane crafting/spell | Craft Arcane Workbench and starter spell, charge/cast wand, observe output | Grimoire recipe and JEI catalyst | Actual cast or recipe use if detector exists; otherwise manual validation | Safe early reagents | Casting costs and failure behavior unknown |
| Disciplines/attunement | Unlock discipline, follow Grimoire Upcoming leads, attune only after explaining choice | Exact named discipline and attunement pages unknown | Detect unlock or state; do not assume all branches mandatory | Branch-appropriate fixed item | Determine mutually exclusive branches |
| Ritual/magitech | Build documented stable ritual or font-fed device, activate, verify output | Exact Grimoire page and JEI recipe required | Operation event or measured output, not component possession | Useful intermediate devices/materials | Geometry, costs and failure consequences are mandatory gaps |

## Existing chapter / pilot reconciliation

| Walkthrough stage | Current quest ID/title and status | Intended action vs actual detector | Named in-game guide/JEI reference | Gap / next action |
| --- | --- | --- | --- | --- |
| All Primal Magick stages | No chapter or pilot inspected in this pass | Not reviewed | Not reviewed | Search canonical quest authoring/generated data before any future quest handoff |

## Conflicts, limitations and version drift

- The exact pack pin is CurseForge file 6677090, released June 21, 2025, with SHA1 in packwiz. The 6.0.7 source tag matches Minecraft and mod version but declares NeoForge 21.1.132 while the current pack target is 21.1.265. Source loader metadata permits NeoForge loader 4+; no in-pack compatibility test is claimed.
- Version 6.0.7 fixed a bug where Mana Orbs could drastically reduce wand mana and changed visual mana-value formatting. This is particularly important to test because the mod has several stored-mana systems.
- Source changelog includes a prior world-destroying bug from excessive knowledge accumulation, fixed by v6.0.2. No assumption about the current cap or exact reproduction is made.
- Version 6.0.5 notes that Infernal Furnaces cannot accept Ignyx fuel through a hopper despite general hopper support; don't describe that path as functional.
- Several pages/docs may use different names across updates (e.g., Research Table/Arcane Workbench/Mana Charger). Verify exact 6.0.7 UI strings before quest authoring.
- The source README states item affinities derive from recipe graphs; if the pack adds shapeless/ambiguous/cyclic recipes, the resulting affinities require a targeted audit.

## Evidence ledger

- Version-aligned source snapshot [`90ac9fe87e091fe5e64070747de6f52f3391b87a`](https://github.com/daedalus4096/PrimalMagick/tree/90ac9fe87e091fe5e64070747de6f52f3391b87a): ProjectTemplate, Project, TheorycraftManager, CompleteProjectPacket, ResearchTableMenu, KnowledgeType, and the Item/Tag/Experience/Observation project material classes support the Research Table contract above. Source-to-pinned-JAR comparison and client validation remain open.

| Claim / section | Source URL or exact inspected file + immutable ref | Evidence tag / version | What it proves / remaining limits |
| --- | --- | --- | --- |
| Pack pin | mods/primal-magick.pw.toml on test/full-integration | [pin] CurseForge project 545271/file 6677090; SHA1 above | Exact packwiz file identity; no JAR byte inspection |
| Exact file metadata | [CurseForge file 6677090](https://www.curseforge.com/minecraft/mc-mods/primal-magick/files/6677090) | [docs: 1.21.1 NeoForge release 6.0.7] | Confirms file name, loader, MC version, release channel and published hotfix notes |
| Gameplay overview and starting route | [Official Modrinth project](https://modrinth.com/mod/primal-magick) | [docs: project description for 1.21.1] | Confirms shrine → sleep/dream → stick+dust wand → bookshelf route; not full walkthrough |
| Source version metadata | [gradle.properties at tag 6.0.7](https://github.com/daedalus4096/PrimalMagick/blob/6.0.7/gradle.properties) | [source: immutable tag 6.0.7] | Confirms mod ID, MC, license and declared loader baseline; exact commit/JAR equivalence not proven |
| Source feature history | [changelog.md at tag 6.0.7](https://github.com/daedalus4096/PrimalMagick/blob/6.0.7/changelog.md) | [source: immutable tag 6.0.7] | Confirms feature/fix history through 6.0.7; not a substitute for in-game guide/recipe inspection |
| Affinity data and commands | [README at tag 6.0.7](https://github.com/daedalus4096/PrimalMagick/blob/6.0.7/README.md) | [source: immutable tag 6.0.7] | Confirms recipe-derived affinity concept, explicit data needs for recipe-less base items and maintenance commands |

## Disposable-world test plan

| Test | Positive/boundary case and setup | Expected observable result and quest credit | Performed? |
| --- | --- | --- | --- |
| Shrine initiation | Fresh survival world; enter shrine and approach device; sleep afterward; compare sleep without shrine and shrine without sleep | Identify exact trigger/event and visible Grimoire/advancement change; FTB credit tested separately | Not run |
| First wand and guide | Craft with stick and shrine dust; use on correct bookshelf and wrong bookshelf | Confirm recipe, output, consumed inputs, Grimoire accessibility and actual unlock detector | Not run |
| Item affinity/research | Analyze recipe-bearing item, recipe-less item, and modded item with multiple/cyclic recipe sources | Record observations, affinity result and research credit; verify datapack fallback needs | Not run |
| Mana and network | Fill wand with Mana Orb, charge through each source, test paired-wand passive generation; then font/injector/relay at and beyond a boundary | Measure capacities, costs, rates, range and whether failures consume inputs | Not run |
| Hopper and magitech | Feed compatible device from top and side; feed Infernal Furnace Ignyx by hopper as boundary | Confirm accepted faces, output, item loss and processing rate | Not run |
| Research knowledge cap | Disposable world, progress research under controlled repeated operations and observe near documented cap | Verify fix against v6.0.2 historical world-destroying bug; no production-world testing | Not run |
| Research Table project transaction | Start project, select consumed and unconsumed item/XP/observation materials, then submit with guaranteed and uncertain success | Record paper/implement cost, project reroll, material consumption on failure, theory award, completion statistic, and whether XP/observation flags match behavior | Not run |
| Aid-block/project scaling | Compare project generation with required aid block inside/outside the 5-block radius and at 0/5/10/15 successful projects | Verify aid requirement, candidate-material count, base success chance, and whether unloaded-area generation yields no project | Not run |
| Ritual stability | Valid salt-connected prop arrangement and identical disconnected-prop arrangement | Compare stability/activation/output and all consumed inputs | Not run |

**Status requirement:** keep researching. The generic Research Table transaction is now source-mapped, including a material-consumption mismatch to test. The shrine introduction, full Grimoire paths, template-by-template predicates/rewards, recipes, mana/network formulas, rituals and terminal objective remain incomplete. No gameplay tests run.

## Unknowns and next research

- [ ] Inspect pinned JAR metadata and assets/datapack content transiently; verify item IDs, recipes, advancement tree, config defaults and Grimoire page names. Do not commit JAR or copied copyrighted assets.
- [ ] Extract all exact 6.0.7 Grimoire pages/categories and map their prerequisite graph.
- [ ] Map observation/analysis methods, knowledge/theory progression and discipline order/branches. The generic Research Table contract is now source-mapped; all registered template predicates/rewards and observation-source acquisition remain open.
- [ ] Confirm shrine structure data, block IDs, biomes, first-visit trigger, sleep conditions and dust harvesting rules.
- [ ] Trace first spell and all major spell types, wand tiers, mana capacities, costs, regeneration and enchanting/attunement progression.
- [ ] Map all rituals, required props and salt geometry, costs, activation/failure order and outputs.
- [ ] Inventory each major magitech device and mana network input/output, including exact hopper faces, recipes, throughput and renewable sources.
- [ ] Reconcile existing quest files/pilots before any quest handoff.
- [ ] Perform client and server disposable-world tests when verification is requested.

## Revision notes

- 2026-11-16 — Added a version-aligned, explicitly partial profile from the exact CurseForge pin, official 1.21.1 overview and 6.0.7 source tag. No quest, runtime or config files changed.
