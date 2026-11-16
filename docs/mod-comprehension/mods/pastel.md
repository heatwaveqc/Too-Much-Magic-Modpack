# Pastel — mechanics and gameplay profile

| Field | Value |
| --- | --- |
| Manifest ID / actual mod ID | pastel / pastel |
| Category / parent or addons | magic; standalone color-resonance magic system; source lists optional integrations including Botania, Create, Xycraft, Malum and Bumblezone |
| MC / loader / pack target | 1.21.1 / NeoForge / TMM integration branch test/full-integration |
| Pinned version / artifact / hash | mods/pastel.pw.toml; Pastel-1.1.5.5.jar; CurseForge project 1264556, file 7591337; SHA1 53a582520b78aebd56c9be1fd041472047dca3d3 [pin] |
| Required projects per CurseForge relation list | Cloth Config, Curios API, Databank, Exclusions Lib, Modonomicon. Pack pin files exist for Cloth Config, Databank, Exclusions Lib and Modonomicon; Curios filename/pin needs reconciliation in current lock. |
| Source snapshot | CurseForge links terrarium-earth/Pastel. Source branch/ref matching 1.1.5.5 has not been established. Public branch 1.21.1 on unilock/Pastel is a separate repository and has Fabric-oriented build metadata; do not treat it as binary-equivalent to the NeoForge CurseForge file. |
| Guide/documentation version gap | CurseForge release page is exact 1.1.5.5, 1.21.1 NeoForge. Version-specific secondary data pages identify 1.1.5.5 and 1,533 items/blocks, 2,196 recipes, 221 advancements, 1,205 loot tables, 20 enchantments, 8 biomes. No JAR artifact inspection performed. |
| Profile status | researching |
| Evidence reached | exact pack pin and official release metadata; source identity established but exact source snapshot not matched; version-scoped secondary recipe/advancement guides; no client test |
| Reviewed | 2026-11-16 |
| Quest ownership | Proposed Magic chapter: Pastel. No quest files changed. |

## Executive gameplay map

**What the player actually does:** Pastel is a long-form exploration and crafting system built around color, gemstones, pigments, ink and resonance. Early advancement discovery points the player toward ores, crystals, crops and leaves; the Pigment Pedestal then provides a recipe-driven crafting platform that consumes gemstone powder and ingredients and is started with a Paintbrush or redstone. Progression expands through colored trees and pigment collection, multiblock crafting and processing, resource growth, potion/food branches, tools and equipment, fluid conversion, and a late descent below the Overworld bedrock into Imbrifer. The recipe families include pedestal and shapeless pedestal crafting, Spirit Instiller, Cinderhearth, Enchanter, Fusion Shrine, Titration Barrel, Potion Workshop, Ink Converting, Crystallarieum Growing and other processing methods. Pastel also has logistics networks and dimensional world generation, so its footprint is much broader than a single altar or spellbook.

**Why it matters in TMM:** Pastel offers distinctive color-based processing, resource growth, potion crafting, item logistics and dimension exploration. The Pedestal and color/pigment economy are the core progression spine; machines, networks, food, equipment and Imbrifer are parallel or later branches. The mod is explicitly designed around discovery and advancement triggers. Do not replace that with a fabricated linear tech tree or infer automation compatibility from a block's name.

**Quick start (version-scoped draft; verify every acquisition in JEI and the in-game book):**
1. Follow the first Pastel advancement and collect the surface/underground curiosities it requests: Amethyst Ore and shards, Blackslag Shimmerstone Ore, Amaranth and the Bottomless Bundle branch.
2. Obtain the in-game guide and use the advancement tree to see what each discovery unlocks; exact guide item acquisition is not yet verified.
3. Make Amethyst Powder through the correct crushing route and craft a Pigment Pedestal. Place ingredients and gemstone powder, then trigger with Paintbrush or redstone.
4. Collect pure Pigments from player-broken leaves of colored trees; branch into colored saplings, additional gemstones and Pedestal upgrades.
5. Use the advancement tree to reach the appropriate machine recipes. Treat the Cinderhearth, Enchanter, Spirit Instiller, Crystallarieum, Potion Workshop, Titration Barrel and Fusion Shrine as separate systems with separate build and operation lessons.
6. Continue through Ink conversion/networking, equipment and resource-growth branches before pursuing the below-bedrock Imbrifer dimension and its endgame resources.

### Player-value snapshot

| Five standout capabilities | Three genuinely early practical unlocks | Three candidate integrations |
| --- | --- | --- |
| Pigment Pedestal crafting; gemstone/pigment discovery and colored-tree farming; an Ink fluid/energy and logistics network; specialized multiblock processing and crystal growth; Imbrifer with unique biomes/resources | Pigment Pedestal can craft broad mod recipes once unlocked; colored leaves yield pure pigment when broken by the player; Amaranth is an early crop/ingredient branch. Exact material burden and earliest timing still need exact recipe data. | **Native upstream claim, still verify in pack:** Pastel offers compatibility for Botania, Create, Xycraft, Malum and Bumblezone. **Candidate:** Botania is enabled in TMM, but inspect exact cross-recipes and reagent direction. **Candidate:** Malum is installed and its separate spirit economy could appear in Pastel's recipes; inspect both-side recipes before designing a quest bridge. |

### Subsystem coverage at a glance

| Subsystem / progression branch | Mechanics evidence | Progression completeness | In-game guide/JEI reference | Client validation | Blocker / next action |
| --- | --- | --- | --- | --- | --- |
| Guidebook and advancement tree | exact-version changelog fixes a guidebook-page advancement ID; secondary version guide inventories 221 advancements | partial | guidebook exists; access and section/page map unknown | not run | establish item recipe/drop/keybind and advancement parent/trigger graph |
| Early ores, geodes, Amaranth and pigment | 1.1.5.5 guide describes first journey, ores, crop, shards and leaves | partial | gemstone/pigment pages; exact guide navigation unknown | not run | source-confirm worldgen, harvesting, yields and gate prerequisites |
| Pigment Pedestal tiers | version guide inventories pedestal recipe type and upgrade references | partial | JEI Pedestal and Pedestal Shapeless; book structure pages unknown | not run | exact structure geometry, tiers, inputs, start/failure costs, output semantics |
| Colored trees / pure pigments | guide and advancement names enumerate pigment collection | partial | colored leaves / pigment pages | not run | verify whether hand-breaking requirement and yields are universal |
| Ink and palettes | version guide identifies Color Picker, Ink, connected containers, palettes and staff uses | partial | JEI Ink Converting; guide map incomplete | not run | trace all ink colors, conversion ratios, storage, network and wearable palettes |
| Machines / multiblocks | Cinderhearth, Enchanter, Spirit Instiller; page exists for exact version | partial | dedicated structure guide and JEI recipe types | not run | reconstruct each build, operation, automation and failure state |
| Potion Workshop / Titration Barrel / food | exact-version guide index lists systems | partial | JEI categories; guide entry mapping incomplete | not run | recipes, effects, fluid handling, durations and food utility |
| Fusion Shrine / equipment / fluid conversion | exact-version guide index; secondaries describe Onyx/Bedrock gear and fluids | partial | Fusion Shrine guide and JEI | not run | full gates, night/environment conditions, cost and output inventory |
| Crystallarieum / pure resource growth | version guide names growth recipe type and example clusters | partial | Crystallarieum Growing | not run | growth inputs, cadence, yield, bounds, automation and environmental conditions |
| Logistics networks | secondary page describes Ink balancing across containers and item logistics nodes | partial | networking pages unknown | not run | inspect node types, filters, range, throughput, chunk/load/ownership limits |
| Imbrifer dimension and endgame | exact-version guide outlines portal beneath bedrock, ores, biomes, ruins, structures and final pedestal tier | partial | Imbrifer/endgame entries; exact key item and unlock order unknown | not run | trace portal, entry/exit, dimensions, biome/structure distribution and terminal route |
| Config, API, dependencies and pack overrides | official relations list required dependencies; source project advertises addon API | partial | n/a | not run | inspect exact artifact metadata/config, pack lock and active integrations |

## Guidebook onboarding

- **Guide name and access:** Pastel has a guidebook, and the exact 1.1.5.5 release changelog refers to unlocking a guidebook page through an advancement. The item name and earliest recipe/drop/keybind are not yet established. Do not borrow Spectrum's access recipe without checking Pastel.
- **Earliest attainable stage:** the first-start discovery advancement; exact trigger and guide access order require pinned data.
- **First instructional quest:** “Open Pastel’s Guidebook.” Show how to obtain/open it and how to use the advancement tree as its discovery index. Detect acquisition as a proxy only; the quest must not assume an item is read.
- **Reference map:** current 1.1.5.5 secondary pages identify Gemstones/Pigments/Pedestal, Cinderhearth/Enchanter/Spirit Instiller, Fusion Shrine, Ink Conversion/Networks, Potion Workshop/Titration Barrel/Pastries, and Imbrifer/Endgame. These external guide names are not confirmed in the in-game book.
- **Where the guide is insufficient:** exact unlock order, multiblock diagrams, recipes, alternative inputs, environmental/night conditions, ink network operation and all endgame dimension transitions must be checked against the pinned guidebook.
- **Other in-game help:** 221 advancements are a major discovery surface; exact advancement tree and recipe unlock predicates need artifact inspection. JEI categories are known from secondary recipe data but must be checked against installed EMI/JEI, since CurseForge lists EMI as an optional dependency and the pack uses a specific recipe viewer.

## Systems and full mechanics inventory

| Subsystem | How accessed | Inputs + costs | Effects / outputs | Limits, failure conditions and source |
| --- | --- | --- | --- | --- |
| Discovery / advancement progression | advancement triggers attached to exploration, collection, crafting and operations | exact trigger predicates and parent chain uninspected | recipe/guide-page discovery and milestone progress | advancement names are not enough to know player action or hidden prerequisites; inspect exact advancement JSON |
| Gemstones and ore | mine/locate Amethyst, Citrine, Topaz, Onyx and further gems; geodes and ores are version-specific | mining, Fortune/tool requirements unknown | shards, blocks and powdered crafting inputs | source worldgen and drop tables not inspected |
| Pigment Pedestal | craft/place Pedestal; supply gemstone powder and recipe ingredients; activate with Paintbrush or redstone per 1.1.5.5 secondary page | recipe-specific items and pigment/gemstone powder; exact consumption conditions unknown | recipe result item | pedestal process is a recipe category, not vanilla crafting; tier, geometry, activation/failure and redstone behavior need source confirmation |
| Pedestal upgrade and energy structure | upgrade Pedestal with gem/shard components and surrounding structure per guide | exact tier ingredients and geometry unknown | more advanced recipe access/energy throughput claimed by guide | exact structure, range, capacities and costs are not source-confirmed |
| Colored trees and pigment | craft/grow colored saplings; collect leaves | sapling, substrate/growth conditions unknown | corresponding pure pigment from leaves; guide reports player-breaking condition | exact drops, tools, silk touch, decay and automation effects unknown |
| Ink conversion / palette | Color Picker and connected containers; fill Pigment/Artist's Palette | dyes/pigments converted into colored Ink; exact ratios and fluid units unknown | stored/networked Ink; palette resource for staffs/Paintbrush abilities per guide | do not call Ink FE, Source or mana. Need exact container capacity, network topology, transfer rate, redstone behavior and sided automation |
| Cinderhearth | construct multiblock and process valid material | pure resource clusters / fuel or machine inputs unknown | secondary guide reports double yields for pure resource clusters; Dragonbone Chunks → 45 Bone Ash | exact build, lava/no-lava variants, operation and speed unknown |
| Enchanter | construct multiblock; provide recipe inputs and XP where needed | item/XP cost depends on recipe; secondary guide reports 2,500 XP for a Knowledge Gem-related operation | enchanted golden apple, copied enchanted books, XP transfer and boot empowerment examples | exact recipes, output copies, XP consumed/retained and exploit boundary not checked |
| Spirit Instiller | construct structure and run Spirit Instiller recipes | recipe items/spirits not mapped | distinct recipe family; exact outputs unresearched | do not conflate with Malum's spirit resource; name overlap is thematic only |
| Crystallarieum | place/grow crystal clusters | target cluster/ampoule examples include Bloodstone and Azurite | grown cluster/crystal outputs | growth inputs, rate, environment and automation not verified |
| Potion Workshop | special crafting category | recipe reagents/containers/effects not mapped | potions | exact process and resulting effects/duration unknown |
| Titration Barrel | fluid/reagent process category | ingredients and fluid quantities unknown | potion/alchemical outputs unknown | source category exists in version guide dataset; operation/failure costs not established |
| Fusion Shrine | multiblock, with night condition reported for Onyx Shard creation | inputs include recipe-specific gems/materials; exact recipe matrix unknown | Onyx shard and equipment upgrade/crafting branch | night/environment and progression conditions are external-guide claims; inspect exact recipes |
| Fluid conversion | exact fluid categories not inventoried | fluids/items/units unknown | conversions unknown | no fluid conversion details are promoted as proven |
| Item logistics | color/network nodes and containers | items and network components | item transport | secondary guide says logistics is a feature; exact side/channel/filter/range/capacity unresearched |
| Imbrifer | pass through portal beneath the Overworld bedrock after progression gates | portal trigger/item and entry cost unknown | new dimension with its own biomes, minerals, structures and endgame recipes | entry/exit safety, dimension rules, location, generation and return route need source/client check |
| Combat/equipment/Curios/enchantments | craft tools, armor, curios and enchantments through progression systems | recipe items, gems, Ink or other resources vary | over 30 tools, two armor sets, nearly 30 Curios and 20 enchantments per official/secondary feature lists | no stats or relative power benchmark performed |
| Food / agriculture | grow Pastel crops/flora and craft foods | seeds, ingredients, machines unknown | many food items and potion ingredients | identify useful effects, harvest and recipe channels from exact data |

## Material / capability input-output contracts

| System | Input kind + exact ID/tag/event | Input qty/formula; consumed/remainder | Output kind, qty and location | Trigger/cadence; range/capacity | Automation/interface and evidence |
| --- | --- | --- | --- | --- | --- |
| Pigment Pedestal | item stacks + gem powder + Paintbrush interaction or redstone signal | per recipe; start/rejection consumption unknown | crafted item output from pedestal | activation event; machine duration unknown | secondary guide confirms trigger options; inspect block entity, recipe JSON and redstone behavior |
| Pigment extraction | colored-tree leaf block break | exact yield and player/automation predicate unknown | pigment item | block-break event | guide says player break; verify loot conditions and automation suppression |
| Ink conversion | dye/pigment item to Ink fluid in Color Picker | conversion ratio and side effects unknown | colored Ink in connected container/network | process event; transfer tick/rate unknown | secondary guide reports equalization across connected containers; exact network type/config unknown |
| Cinderhearth | pure resource cluster or other accepted item/block + machine structure | recipe-specific; fuel/lava conditions unknown | smelted items; guide says some pure clusters yield x2 | machine operation; time unknown | recipe type confirmed secondarily; automation/valid structure untested |
| Crystallarieum | cluster + growing inputs | quantity/timing unknown | grown crystal cluster/item | growth over time (exact cadence unknown) | automation and output collection untested |
| Enchanter | item + enchantment/book/XP inputs depending on recipe | guide reports an operation spending 2,500 XP; exact costs unknown | enchanted/copy/upgrade result | machine recipe operation | exact XP/orb/item semantics unverified |
| Potion/Titration | item, liquid/fluid or potion inputs unknown | no quantity asserted | potion/food/processed reagent | recipe operation unknown | do not infer fluid channels from category names |
| Imbrifer gateway | world state under bedrock + unknown player action | entry cost/portal not established | dimension transfer | event, likely one-way until return method verified | no runtime test |
| FE, Source, mana, spirit economy | not established as standardized Malum/Pastel cross-system channel | unknown | unknown | unknown | Pastel Ink is its own network resource until evidence shows otherwise |

### Acquisition, renewability and output economics

- The mod's progression depends on discovering and using many resources, not just crafting the next machine. Advancement completion, recipe unlocks, guide pages and item acquisition may be separate events; do not collapse them.
- Colored-tree pigment harvesting is a likely renewable input, but player-only break predicates and mob/automation behavior need exact loot-table review.
- Pigment Pedestal and processing recipes are large enough to change the pack's resource economy. Audit alternative inputs and all high-output conversions before treating a recipe as an early ore-doubling shortcut.
- Ink is described as an equalized network resource with color-specific conversions and palette uses. It is not equivalent to FE, Source or mana without an explicit bridge.
- Imbrifer contains new ore/mineral lines and a high-tier Pedestal progression. Its below-bedrock gate could be a significant dimension progression branch; verify access and return paths before quests or structure navigation.
- The source project describes compatibility and integration as existing for Botania, Create, Xycraft, Malum and Bumblezone. Confirm that exact compat is included in this file/build and trace both sides before planning a TMM bridge.

## Complete player progression / walkthrough blueprint

This is a version-scoped outline and coverage audit, not yet sufficient for quest authoring. Exact recipes, multiblock diagrams and advancement predicates remain required.

| Path and step | Gate or choice? | Exact prerequisite / materials / world condition | Player actions: obtain, build, activate, use | Unlock / visible success / next step | In-game guide section or JEI reference | Proposed quest/how-to, detection and reward | Evidence / coverage |
| --- | --- | --- | --- | --- | --- | --- | --- |
| 0. Obtain/open guide | required teaching | item recipe/drop/keybind not established | obtain/open book; inspect advancement tree | discover progression graph | Pastel guidebook; section mapping incomplete | explicit onboarding and safe book reward only after exact ID/route; task detects item as proxy; XP + useful starter crafting materials | exact guidebook unlock blocker |
| 1. First discoveries | required | initial world exploration and gathering | collect Amethyst Ore/shards, Blackslag Shimmerstone Ore, Amaranth Grains/Bushel, Bottomless Bundle | Start of a Journey and linked resource discoveries | first-journey/gemstone guide | teach what to mine/grow and advancement feedback; detect discovered items as proxies; XP + useful seeds/mining supply | advancement names in 1.1.5.5 guide |
| 2. Gemstone powder | required Pedestal prerequisite | gemstone shards and crushing method | crush shards to powder with anvil-crushing or confirmed alternative | pedestal recipe input | JEI Anvil Crushing | show actual crushing method and output; item task may proxy; XP + low-tier gem supply | recipe type and example secondary |
| 3. Pigment Pedestal | core gate | pedestal recipe ingredients; exact recipe not extracted | craft/place, load powder and ingredients, activate with Paintbrush or redstone | first pedestal craft, advancement; open recipe branches | JEI Pedestal / Pedestal Shapeless; guide chapter on pedestal | operate one verified recipe and observe output; acquisition only proxy; XP + safe common ingredients | page names and exact tier recipe missing |
| 4. Pigment collection / colored tree | required resource branch | colored sapling/tree; growth inputs unknown | grow selected color tree; personally break leaves if required | pure pigment and colored sapling line | colored-tree/pigment entry | demonstrate harvest and verify correct pigment; FTB item detector is proxy; XP + sapling/farm supply | player-break behavior needs source confirmation |
| 5. Pedestal upgrade / structure | choice but likely progression gate | base gems/shards and structure blocks; exact pattern missing | upgrade pedestal and construct surrounding energy/funneling structure | expanded recipe access/capacity | JEI pedestal tier + book structure diagram | teach full pattern, trigger, missing-block troubleshooting; detect operation only if supported; XP + construction materials | major guide gap |
| 6. Machine progression | several optional/required branches | Cinderhearth, Enchanter, Spirit Instiller structure mats; recipes | build each multiblock exactly; submit a real recipe and observe result | doubled processing, enchanting, spirit recipes | Cinderhearth/Enchanter/Spirit Instiller guide and JEI categories | one how-to per subsystem; no multiblock-item possession proxy as completion; XP + useful recipe materials | structures and recipes incomplete |
| 7. Crystal growth | optional resource branch | crystallarieum and supported seed/cluster | run one growth operation; record cadence/output | renewable crystal resource | JEI Crystallarieum Growing | demonstrate growth, not just item; XP + seed/growth input | recipe requirements incomplete |
| 8. Ink and palette | core utility branch | Color Picker, ink containers/network, dyes/pigments, palette | convert pigment to Ink, connect storage, equip palette, use an ability | Ink supply and equipment interactions | Ink Converting/network guide; guide's palette section unknown | teach color-specific conversion and how to verify stored Ink; task detector unknown; XP + low-risk pigments | rate/capacity/ability costs unknown |
| 9. Potion and food systems | optional branch | Potion Workshop, Titration Barrel, flora/ingredient lines | brew one potion and prepare one useful food/pastry | effects, food and reagent utility | Potion Workshop/Titration Barrel guide | recipes and practical effects; require output/action as supported; XP + ingredient seeds | no recipe/effect sweep |
| 10. Fusion Shrine / equipment | major equipment branch | shrine structure; night condition reported for Onyx | fuse Onyx shard, craft/upgrade Bedrock equipment and other tools | advanced equipment | Fusion Shrine guide / JEI | teach structure and condition, wear/stat utility and outputs; XP + safe component | structure and gates incomplete |
| 11. Item logistics | optional automation branch | network nodes and containers | set up a minimal route, transfer a real item and verify destination | internal Pastel transport | network guide | operation detector or manual observation; XP + network component | node types/limits unknown |
| 12. Open Imbrifer | dimension gate | below-bedrock portal; trigger and prerequisites unknown | safely descend/open portal and enter; record exit/return | new dimension, biomes and resource lines | Imbrifer/endgame section | step-by-step location/access/return and safety; dimension task if FTB supports, otherwise manual fallback; XP + safe exploration kit | gate hard blocker |
| 13. Imbrifer progression | advanced/endgame | Moonstone geodes, Forgotten Cities/Core, Malachite/Bismuth/Paltaeria and other flora | explore biomes/structures, gather/grow resources, reach final Pedestal tier | final resource/equipment/recipe branches | exact Imbrifer, Moonstone, Malachite and pedestal pages | separate mandatory path from optional resource/equipment branches; reward XP + safe materials; do not skip dimension gates | secondaries enumerate milestones, exact recipe graph missing |
| 14. Terminal checklist | open-ended until final advancement confirmed | source-defined completion advancement unknown | complete genuine final milestone and selected capability showcase | no terminal objective claimed yet | exact advancement chain required | capstone only after final trigger and prerequisites verified | not complete |

### Dependency graph and alternate routes

Draft shape:

**first discoveries → gemstone powder → Pigment Pedestal → colored pigments + pedestal upgrades → machines / Ink / crystal growth / potion & food / equipment → portal under bedrock → Imbrifer resources and late Pedestal/equipment branches.**

Some machine and utility branches may be optional and parallel; the exact advancement-parent graph decides. Do not assume every guide-listed feature gates the next chapter. Distinguish required discoveries from recipe choices and alternate processing routes. No TMM game-mechanic gate or recipe override has been reviewed.

## Internal synergies and low-hanging setups

- **Gemstone shard → powder → Pigment Pedestal:** direct first magical crafting route; needs exact early recipe, pedestal tier and material affordability audit.
- **Colored saplings → pigment leaves → pedestal/Ink:** plausible renewable color supply. Test decay, player-break requirement, fortune/silk-touch behavior and throughput before recommending automation.
- **Cinderhearth → pure resource processing:** guide claims selected Pure Resource Blocks smelt at double yield. Confirm exact inputs, output count and time, and ensure resources aren't too cheap under TMM's ore/economy plan.
- **Crystallarieum → growing mineral clusters:** could provide renewable crystals after initial setup; actual source inputs/cadence and caps are unknown.
- **Color Picker/network → Ink → palette/staff actions:** clear internal pipeline; map colors, fluid unit, container equalization and ability drain before quest design.
- **Pastel × installed magic mods:** upstream describes Botania/Malum integrations. The integration list is only a lead; inspect exact recipe directions and confirm all needed items exist in this pack before adding a cross-mod quest.

## Configuration, APIs and current pack behavior

- Pack pin is mods/pastel.pw.toml. CurseForge file metadata says client and server, release, NeoForge, Minecraft 1.21.1; it lists no additional files.
- CurseForge marks required dependencies Cloth Config, Curios API, Databank, Exclusions Lib and Modonomicon. The current pack includes pack pins for Cloth Config, Databank, Exclusions Lib and Modonomicon; Curios API was not found under the guessed mods/curios-api.pw.toml path, so reconcile its actual pin/embedded status. This is a dependency audit gap, not a claim that runtime is broken.
- Modrinth's official project page describes code as LGPLv3 and assets as ARR; CurseForge project metadata presents All Rights Reserved. Treat code/assets separately and do not copy mod assets.
- CurseForge release 1.1.5.5 changelog says a broken advancement ID preventing a guidebook page from unlocking was fixed. Pin this fact to release; do not assume earlier progression data.
- Modrinth page identifies supported compatibility with Botania, Create, Xycraft, Malum and Bumblezone. README source advertises integrations including AE2, Botania, Create, Xycraft, Malum and Bumblezone. Only some are installed/approved here; optional integrations do not imply permission to add absent mods.
- No Pastel-specific data pack, config override, KubeJS script or quest content was inspected. The exact artifact's config has one setting listed by secondary data: NameForUnrevealedEnchantments; verify default and whether this is the complete config.
- No runtime or balance change is proposed.

## Progression-to-quest coverage matrix

| Stage | Teaching objective and action | Guide/JEI reference | Authentic completion / FTB detector | Reward direction | Coverage |
| --- | --- | --- | --- | --- | --- |
| Guidebook | obtain/open book and use advancement index | in-game book / advancement screen | item possession proxies opening | XP + safe basic materials | partial |
| Early discoveries | gather first gem, ore, crop and bundle branch | opening/gemstone pages | items are proxies for discovery; actual advancement if task supports it | XP + seed/mining supply | partial |
| Gem processing | crush shards into correct powder | JEI Anvil Crushing | powder item is a proxy | XP + basic gems | partial |
| Pedestal | place/load/trigger a recipe; observe output | JEI Pedestal and exact book structure | output proxy; operation detection unknown | XP + low-tier crafting supplies | partial |
| Pigments/trees | grow color tree, harvest correct leaves | pigment section | pigment item proxy; harvest trigger unknown | XP + sapling/farm supply | partial |
| Pedestal upgrades | construct full tier/energy pattern | book diagram + JEI tier recipes | item criteria cannot prove geometry; need event/manual fallback | XP + structural blocks | partial |
| Machines | build and operate each major multiblock | system-specific book + JEI | authentic operation event required; not established | XP + useful subsystem ingredients | partial |
| Crystals | run growth cycle | JEI Crystallarieum Growing | output proxy or manual growth observation | XP + growth input | partial |
| Ink/palette | convert resource, verify network storage, use palette ability | Ink Converting + palette guide | inventory item may prove fill; ability use event unknown | XP + pigment/ink starter | partial |
| Potion/food | brew or craft useful output and identify effect | Potion Workshop/Titration Barrel | output item only; use/effect criteria need detector | XP + recipe ingredients | partial |
| Fusion Shrine/gear | build shrine under required condition and craft chosen gear | Fusion Shrine guide | output item proxy; actual condition/structure operation unknown | XP + safe component | partial |
| Logistics | move an item to a destination through network | network pages | destination state check/manual observation | XP + network pieces | partial |
| Imbrifer | find/activate portal, enter and return safely | dimension section | dimension advancement if exact task exists; otherwise manual fallback | XP + safe exploration supplies | partial |
| Endgame | obtain late resources and reach actual final advancement | Moonstone/Malachite/endgame pages | final advancement trigger; not yet known | XP + non-bypassing thematic item | partial |

**Reward policy:** use positive XP and meaningful fixed items. Do not reward a dimension-critical mineral or finished equipment before the quest teaches its acquisition. Item possession is a proxy unless the task verifies the described operation. No quest has been edited.

## Open questions and next actions

1. Find and inspect the actual pinned 91 MB JAR transiently; verify its SHA1/metadata, exact guidebook text, data pack, recipes, tags, advancement parent graph, loot, dimensions, worldgen and config.
2. Find the source commit/tag that produced the 1.1.5.5 NeoForge file. Current 1.21.1 branch and the unilock fork are not proven matching source.
3. Map guidebook acquisition/opening and all mandatory advancement gates, especially fixed page unlocks and the final Imbrifer achievement chain.
4. Reconstruct complete multiblock geometry/operation for Pedestal upgrades, Cinderhearth, Enchanter, Spirit Instiller, Fusion Shrine, Potion Workshop and any other tiered processors.
5. Read exact recipe JSON for all recipe types; audit costs, consumed inputs, outputs, catalysts, byproducts, yields, durations and failure loss.
6. Resolve the Curios API pin and confirm all 5 declared dependencies are present and correct in the runtime lock.
7. Inspect Ink network and Item network internals for connection range, transfer speed, channel/filter behavior, chunk loading, sided interfaces, storage caps and automation.
8. Inventory all 8 biomes, structures, dimension entry/exit and rare resources; map each to advancement and guide entry.
9. Review exact Botania and Malum compat files and enabled target mods; document two-sided tested candidates separately from native integrations.
10. Check correct installed recipe viewer. Pastel's source relation marks EMI optional; do not assume a JEI category label in this pack without client validation.

## Test plan — not run

No tests were run; this is a knowledge Markdown pass.

- **Positive:** exact pack build, make one first-tier Pedestal craft with powder, ingredients and Paintbrush, then repeat with redstone. Record all consumed stacks, output, time and advancement/page unlock. **Boundary:** missing ingredient, wrong tier or powder absent; record start/rejection and any input loss.
- **Positive:** build one documented machine and process a supported input; measure output quantity and cadence. **Boundary:** remove a required structure block or input and record machine response, resource loss and advancement state.
- **Positive:** harvest a colored leaf personally and receive pigment. **Boundary:** compare natural decay, piston/machine break and Silk Touch; record which actually yields pigment.
- **Positive:** convert each test dye/pigment through Color Picker and connect two containers; record fluid IDs/units/capacity and network balancing. **Boundary:** incompatible color/container, blocked connection and chunk unload.
- **Positive:** run one Crystallarieum growth and one Potion Workshop recipe; record timers and item/fluid inputs. **Boundary:** missing reagent/medium or invalid container.
- **Positive:** construct/use a Fusion Shrine at the claimed time and enter Imbrifer, then return. **Boundary:** attempt outside time/prerequisite and test dimension death/portal return.
- Review FTB task support and guidebook/EMI presentation on a graphical client before quest authoring.

## Evidence ledger

- Pack pin mods/pastel.pw.toml on test/full-integration: artifact, SHA1 and CurseForge file/project IDs [pin].
- [Official CurseForge file page](https://www.curseforge.com/minecraft/mc-mods/pastel/files/7591337): Pastel 1.1.5.5 Release, NeoForge, MC 1.21.1, client/server, uploaded 2026-02-07; one-line changelog fixes broken advancement ID blocking a guidebook page.
- [Official Modrinth project page](https://modrinth.com/mod/pastel/): target MC/loader, project synopsis, feature families, listed integrations and code/assets license summary.
- [CurseForge required-dependency list](https://www.curseforge.com/minecraft/mc-mods/pastel/relations/dependencies): Cloth Config, Curios API, Databank, Exclusions Lib and Modonomicon marked required.
- [Upstream source repository](https://github.com/terrarium-earth/Pastel): identified from CurseForge Source link; branch/ref corresponding to pinned 1.1.5.5 has not yet been found.
- [Version-scoped Pastel data profile](https://mcmodwiki.com/pastel/1.21.1): identifies 1.1.5.5 and tabulates content counts/category names; secondary evidence.
- [Pastel Pedestal and pigments guide](https://mcmodwiki.com/pastel/guides/gemstones-pigments-and-pigment-pedestal-progression), [machine guide](https://mcmodwiki.com/pastel/guides/cinderhearth-enchanter-and-spirit-instiller-structures), [Ink and network guide](https://mcmodwiki.com/pastel/guides/ink-converting-pigment-palettes-and-pastel-logistics-networks), [Imbrifer/endgame guide](https://mcmodwiki.com/pastel/guides/imbrifer-dimension-moonstone-malachite-and-endgame-progression): all explicitly identify Pastel 1.1.5.5 on Minecraft 1.21.1; secondary guidance only.
- No pinned artifact contents, pack runtime behavior, client tests or integration recipes were verified.
