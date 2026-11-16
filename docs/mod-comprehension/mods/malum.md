# Malum — mechanics and gameplay profile

| Field | Value |
| --- | --- |
| Manifest ID / actual mod ID | malum / malum |
| Category / parent or addons | magic; independent soul and spirit-arcana system; addon: [Gaze](gaze-malum.md) |
| MC / loader / pack target | 1.21.1 / NeoForge / TMM test/full-integration |
| Pinned version / artifact / hash | mods/malum.pw.toml; malum-1.21.1-1.8.2.jar; Modrinth project jgzwYsAN, version BIbVs1fo; SHA512 dd9ccc1dea3b090f69f807be3e7a8b99ec6d3d7d6b047e2893cb49ca505860bbf640cf767082c1a32ea403b35e2a9f9d1755327890bbfb2f8778058a9597a99e [pin] |
| Source snapshot | Previously recorded commit `03b743a37f3eeb0cc7f4364f0730e1f135f78408` currently returns GitHub contents API 404. The reachable public 1.21.1 snapshot `458518e3e1f73c3d9bc1c85522743eeb81b4f0c9` declares Malum 1.9.0, not the pack's 1.8.2; it is not used as evidence for the pinned release. Exact 1.8.2 source provenance is unresolved. |
| Guide/documentation version gap | Modrinth release metadata matches 1.8.2 / MC 1.21.1 / NeoForge / client and server. Official listing delegates changelog to CurseForge. Third-party walkthrough explicitly covers 1.8.2; source data not extracted from pinned JAR. |
| Profile status | researching |
| Evidence reached | pack pin and release metadata verified; version-scoped secondary 1.8.2 guide reviewed; previously recorded source ref is not retrievable and accessible branch source is 1.9.0; no artifact-byte comparison or in-pack test |
| Reviewed | 2026-11-16 |
| Quest ownership | Proposed Magic chapter: Malum. No quest files changed. |

## Executive gameplay map

**What the player actually does:** Mine Soulstone and find Runewood, make a Crude Scythe, and expose enemy souls before they die to harvest spirits. The Encyclopedia Arcana explains the mod and shows recipes. Spirits are item resources spent at distinct recipe systems: Spirit Infusion on the Spirit Altar, Runeworking, Soul Binding, Spirit Focusing in the Spirit Crucible, and Spirit Repair. The Altar consumes a central input and stored spirit stacks; many recipes also draw extra item ingredients from nearby pedestals. As spirit supply grows, players craft spirit materials, tools and armor, create spirit storage, activate totem rites, and build the Crucible for material conversion. The Weeping Well is a worldgen structure with a separate transformation and rare-resource branch. This is primarily a hunting-and-crafting progression, with automation opportunities around mob kills, jars, pedestals and processing.

**Why it matters in TMM:** Malum gives the pack a combat-fed spirit economy and early material utility. Its clearest immediate fit is spirit harvesting and altar-based processing; later it offers gear, rites, permanent Curio effects, material focusing and world exploration. Do not treat spirits as Source, mana, XP, FE, fluids, or a ritual entity sacrifice: they are Malum-specific items.

**Quick start (secondary-guide-informed; validate exact recipes in JEI):**
1. Mine Soulstone Ore, smelt Raw Soulstone into Processed Soulstone, and obtain Runewood from Runewood/Azure Runewood trees.
2. Craft the Encyclopedia Arcana from a Book and Processed Soulstone; the third-party guide also reports a first-undead-kill drop, which needs exact-source validation.
3. Craft a Crude Scythe and strike each target before killing it. The 1.8.2 guide describes a 10-second soul-exposure window, so a different weapon or environmental damage can finish the target within that period.
4. Gather spirits, craft the Spirit Altar, and reserve room for nearby pedestals. Begin with Hex Ash, then craft recipe-selected spirit materials.
5. Expand into jars/pouch, Soulstained Steel or Soulhunter equipment, totems, and the Spirit Crucible according to the desired branch.

### Player-value snapshot

| Five standout capabilities | Three genuinely early practical unlocks | Three candidate integrations |
| --- | --- | --- |
| Soul-exposure combat resource loop; Spirit Altar infusion; totem rites and environmental/block effects; Spirit Crucible material focusing; Weeping Well transmutation/rare spirit branch | Soulstone and Runewood are early-world resources; Runewood logs can be smelted to Arcane Charcoal; Spirit Altar recipes include bulk building materials and Hex Ash. Iron is required by the documented Crude Scythe recipe, so spirit harvesting is not assumed before iron. | Candidate: Occultism mob farms may supply targets, but loot-event compatibility and scythe tagging need testing. Candidate: Ars/Iron's magic combat can use Malum Soulhunter equipment, but no stat or spell interaction is established. Candidate: TMM exploration/quest loot may expose rare Malum items too early; audit item/drop tables before any claim. |

### Subsystem coverage at a glance

| Subsystem / progression branch | Mechanics evidence | Progression completeness | In-game guide/JEI reference | Client validation | Blocker / next action |
| --- | --- | --- | --- | --- | --- |
| Guidebook / Encyclopedia Arcana | version-scoped 1.8.2 guide; prior source ref unavailable | partial | Encyclopedia Arcana; exact entry names/pages not mapped from pinned artifact | not run | confirm recipe/drop/keybind and complete entry index against exact jar/client |
| Soulstone, Runewood, ores, wood and sap | version-scoped guide; source worldgen not extracted | partial | Encyclopedia Arcana and JEI | not run | extract worldgen and all material recipes |
| Soul exposure, spirits and mob-drop mapping | version-scoped guide; source implementation not inspected | partial | guide entries and spirit tooltips need mapping | not run | enumerate spirit families, per-mob drops, conditions, config and spawn exceptions |
| Spirit Altar / Spirit Infusion | 1.8.2 guide describes input/spirit/pedestal operation; recorded source ref unavailable | partial | Spirit Infusion category / encyclopedia recipe entry; exact named page unknown | not run | inspect 1.8.2 recipes and altar implementation, costs and valid ranges |
| Runeworking and rune materials | secondary version comparison: 24 recipes on 1.21.1 | partial | JEI Runeworking; guide path unverified | not run | exact inputs, station and guide sections needed |
| Spirit Crucible / Spirit Focusing | 1.8.2 secondary guide describes impetus, spirit input and output; source ref unavailable | partial | JEI Spirit Focusing / Encyclopedia Arcana | not run | inspect recipes, augment slots, wear formula, speed and automation interface from pin |
| Spirit Repair / Repair Pylon | 1.8.2 guide and prior source notes; source ref unavailable | partial | JEI Spirit Repair; page unknown | not run | confirm pinned recipe set, pylon placement/storage, repair amount and costs |
| Totemancy / Rites / Soulwood / Blight | secondary 1.8.2 guide, not yet corroborated against implementation | partial | encyclopedia rite pages; exact sequence map absent | not run | source-map every rite, pole order, radius, duration, block/entity effects and spread/cleanup |
| Soul Binding / Geas / Curios | 1.21.1 secondary page inventories 36 Soul Binding recipes, not exhaustively inspected | partial | JEI Soul Binding and guide entries unknown | not run | inspect branches, costs, Curio requirements, permanence/removal and effects |
| Void / Weeping Well / Umbral | 1.8.2 external guide identifies a Weeping Well entry, structure and transformation claims; prior source ref unavailable | partial | Encyclopedia Arcana → The Weeping Well / Void; exact unlock order unknown | not run | verify generation, transmutation, rejection costs and Umbral source |
| Worldgen and combat additions | 1.8.2 release/guide descriptions; 1.9 source snapshot is not treated as pin evidence | partial | unknown | not run | inventory 1.8.2 mobs, equipment, structures, hostile effects and loot |
| KubeJS/config/API and pack integration | matching source unavailable; 1.9 source metadata does not establish 1.8.2 hooks | unresearched | n/a | not run | inspect recipe serializers/config defaults and live pack overrides |

## Guidebook onboarding

- **Guide name and access:** Encyclopedia Arcana according to the version-scoped external guide. It reports a recipe from a Book plus one Processed Soulstone and says the first undead kill can drop one. Recipe and drop are not confirmed against the pinned JAR; do not encode the drop route or task predicate as final.
- **Earliest attainable stage:** after mining and smelting Soulstone, or potentially the first undead kill (unverified).
- **First instructional quest:** “Read the Encyclopedia Arcana.” Teach the confirmed acquisition route, have the player open the book, and direct them to its opening/material entries before introducing the scythe. Task should detect book acquisition only as a proxy for reading.
- **Reference map:** The previously recorded source-generated language inventory was tied to a commit that now returns 404; treat the named Chronicles of the Void and The Weeping Well entries as provisional until recovered from a matched JAR/source. The 1.8.2 guide and exact client should establish the page map. Use JEI categories Spirit Infusion, Runeworking, Soul Binding, Spirit Focusing, and Spirit Repair only after verifying them in the exact client.
- **Where the guide is insufficient:** exact acquisition, first-spirit collection teaching, altar/pedestal placement, totem construction, worldgen routes and operation failure signals have not been compared against the pinned book text.
- **Other in-game help:** Prior profile notes named recipe categories based on the unavailable source ref; exact category names, registrations and display semantics require exact release confirmation.

## Systems and full mechanics inventory

| Subsystem | How accessed | Inputs + costs | Effects / outputs | Limits, failure conditions and source |
| --- | --- | --- | --- | --- |
| Soulstone progression | mine Soulstone Ore; smelt Raw Soulstone | ore, pickaxe and furnace fuel | Processed Soulstone; core crafting material | external 1.8.2 guide says ore in stone/deepslate from low world to around Y100 and smelts to 2 Processed Soulstone; exact ore placement and Fortune behavior unverified in artifact |
| Runewood progression | locate Runewood or Azure Runewood trees; collect saplings/logs | axe/wood; bottle for bark sap per guide | Runewood materials, Arcane Charcoal fuel, Runic Sap and sapballs | biome distribution and renewability need configured worldgen/loot check |
| Soul exposure | strike a living target with Crude Scythe; guide also names staves and Soulstained Steel tools | attack event; weapon durability/attack cost not established | target gains temporary exposed-soul state; eligible death yields spirit item(s) | guide says 10-second window; per-entity spirit table, immunity, spawner behavior, drop chance and loot events unverified |
| Spirit items and storage | pick up drops; Spirit Pouch; Spirit Jars | specific spirit items, pouch/jars | typed spirit inventory; jars can serve as stored input | guide says pouch auto-collects 27 slots; jar stores one type and supports hoppers. Exact limits/channels not confirmed |
| Spirit Altar / Infusion | place Spirit Altar; insert central target item and spirit stacks; place recipe-specific ingredients on nearby Runewood pedestals/stands | recipe target, typed spirits, extra item stacks; costs are recipe-defined | crafted infused item appears above altar according to guide | exact ordering, consume/return conditions, timing, pedestal coordinates, failure consumption, range and automation need source review |
| Runeworking | use Runic Workbench | rune-work recipe inputs and spirits (exact contracts unresearched) | runes | version comparison lists 24 recipes for 1.21.1; exact setup/recipe mechanics unresearched |
| Soul Binding | guide/JEI branch; station and recipe not yet confirmed | offerings and spirit inputs, exact recipe costs unresearched | Geas/pact or other binding output, exact mappings unresearched | 1.21.1 secondary data counts 36 recipes; permanence, cancellation and slot restrictions unknown |
| Spirit Crucible / focusing | build Spirit Crucible; supply an Alchemical Impetus and spirit recipe input | recipe-specific spirit; Impetus wear; possibly augment materials | recipe-defined focused materials or nodes | guide describes four augment slots and Spirit Catalyzers; output quantities and wear per recipe need exact data/code |
| Spirit Repair / Pylon | 1.8.2 guide describes a repair branch; source ref unavailable | equipment + reported recipe spirits/materials | durability repair | exact recipe, operation and repair amount unverified | recover matched source or inspect pinned JAR; no pack test |
| Totemancy | craft Runewood Totem Base, carve spirit poles into log sides, stack in ordered sequence, activate base | base + Runewood logs + one spirit per carved pole; recipe-specific requirements | ongoing rite effects on player, mobs or blocks | guide warns five-Arcane Undirected Rite corrupts the totem and blights ground; radius, duration, reversibility and safety boundaries unverified |
| Soulwood / Blight | trigger Undirected Rite; grow Runewood on blighted ground near Unchained Rite per guide | Totem base, poles, blight and sapling | Soulwood Totem/tree/logs and altered rite set | spreading soil is a world mutation; containment and removal require source/client validation |
| Weeping Well / Void | find generated structure; throw recipe input into well | item entity/world interaction; eligibility and item cost unverified | transmuted item; first-player-contact Umbral Spirit alleged by guide | structure frequency/location, item timing, multi-item handling, failed-attempt loss, mob/pet behavior and repeatability unverified |
| Combat gear / Curios / rites | 1.8.2 guide / secondary recipe descriptions; exact data not confirmed | spirit-made materials and reported recipe inputs | Soulstained Steel weapons/armor, Soulhunter clothing, Curios, effects | gear stats/effects not benchmarked; source recipes and requirements need matched release evidence |

## Material / capability input-output contracts

| System | Input kind + exact ID/tag/event | Input qty/formula; consumed/remainder | Output kind, qty and location | Trigger/cadence; range/capacity | Automation/interface and evidence |
| --- | --- | --- | --- | --- | --- |
| Spirit drops | living entity + soul-exposure event | target must be exposed before death per guide; exact trigger and item qty per mob unknown | Malum spirit item, reportedly flies to player inventory | death event; exposure window reported as 10 seconds | spawner loot behavior and magnetization not confirmed; source implementation needed |
| Spirit Infusion | item stack on altar, spirit stacks, extra item stacks on pedestals | recipe-defined counts; failed-attempt costs unknown | recipe result item at altar | altar cycle/range/timing remain unverified for pin | prior source ref unavailable; recipe datagen and altar operation need matched 1.8.2 source or JAR review |
| Totem rites | placed block state + ordered carved totem poles | guide says one matching spirit per pole during carving; sustained resource cost unknown | effect, block operation or world state | activation/ticking; range/duration unknown | not established; inspect exact implementation |
| Spirit Crucible | spirit item plus valid focusing recipe and installed Impetus | spirit cost per recipe; Impetus durability cost unquantified | focusing recipe output | machine tick; operation timing unknown | item insertion/automation untested; operation and output queue need matched source/JAR review |
| Repair Pylon | equipment + guide-reported matching material + spirit stacks | recipe quantities and consumption unverified | durability restored | operation trigger/cadence unknown | source builder field cannot be verified while the recorded source SHA is unavailable; do not interpret an earlier 0.5f note as an amount or percent |
| Weeping Well | dropped item entity | exact count, eligibility and consumption timing unknown | transformed item, reportedly emerges from well | throw/item-in-well contact | entity event/world interaction; automation and failure cost unknown |
| FE, fluids, Source, XP, blood | not established as a core Malum economy | not established | not established | not established | do not infer a conversion from spirit imagery |

### Acquisition, renewability and output economics

- **Spirit supply is combat-gated by a hit state**, not merely by killing mobs. The guide reports scythe/staff/Soulstained Steel contact exposes a soul for a limited window; a normal weapon can finish the target afterward. Mob farms need this condition, drop tables, ownership and item collection confirmed.
- **Runewood and Soulwood** appear renewable via saplings. Soulwood has deliberate spread/corruption costs: establish containment before proposing a base-scale farm.
- **Spirit Infusion** has recipe-specific outputs, including a reported 16-block conversion from 16 cobblestone into Tainted/Twisted Rock, but no complete recipe inventory or throughput analysis has been performed.
- **Spirit Crucible** is a material-focusing/ore-processing system, not a free continuous generator: recipes spend spirits and an Alchemical Impetus wears. Exact costs, durability and repair economy remain open.
- **No FE, mana, fluid, Source, XP, or blood conversion path is established.** Search for exact recipe/implementation evidence before writing an integration claim.

## Complete player progression / walkthrough blueprint

This is a secondary-guide-informed draft, not yet a quest-authoring handoff; the 1.8.2 source snapshot recorded in earlier notes is currently unavailable.

| Path and step | Gate or choice? | Exact prerequisite / materials / world condition | Player actions: obtain, build, activate, use | Unlock / visible success / next step | In-game guide section or JEI reference | Proposed quest/how-to, detection and reward | Evidence / coverage |
| --- | --- | --- | --- | --- | --- | --- | --- |
| 0. Obtain/open guide | required teaching | Book + Processed Soulstone; possible undead drop | acquire and open Encyclopedia Arcana | access to instructions | Encyclopedia Arcana; exact page index unknown | Read the Encyclopedia Arcana; item is proxy for opening; XP + basic book/altar materials | secondary 1.8.2 guide; exact source recipe needed |
| 1. Gather materials | required | underground Soulstone Ore; Runewood/Azure Runewood | mine, smelt, collect logs/saplings | core material and fuel supply | “Getting Started with Soulstone”, “Runewood Trees” per guide | teach location and sapling planting; detect processed ore/logs; XP + mining/wood reward | version guide; worldgen unverified |
| 2. Start soul hunting | required | 3 Iron, 1 Processed Soulstone, 2 Sticks (recipe needs JEI check) | craft Crude Scythe; strike target then kill during exposure | first spirit items | “The Crude Scythe and Soul Hunting” | demonstrate exposure and spirit drop; item acquisition cannot prove hit event; XP + spirit-hunting supplies | guide reports 10 sec; source needed |
| 3. Craft and operate Spirit Altar | core gate | 4 Runewood Planks, 2 Gold Ingots, Processed Soulstone reported; central input and available spirits | craft/place altar; run first recipe; set extra items on valid pedestals | spirit infusion materials (Hex Ash, Hallowed Gold, Soulstained Steel, etc.) | JEI Spirit Infusion and encyclopedia recipe entry; exact named page unknown | teach altar use; FTB output task is a proxy; XP + safe spirit-hunting supplies | secondary recipe; altar code incomplete |
| 4. Build recipe workspace | recommended support | Runewood pedestals/stands, clearance, recipe ingredients | place within confirmed radius; feed ingredients; observe output | repeatable infusion workflow | Spirit Infusion / pedestal pages | teach geometry and no-start troubleshooting; XP + low-value ingredients | radius from secondary guide only |
| 5. Store and scale spirits | optional productivity | Spirit Fabric, String, Soul Sand/Soil; jars need Hallowed Gold + panes per guide | craft pouch/jars; sort spirits; test hopper where supported | inventory management and possible farm buffer | Encyclopedia entries / JEI | teach typed storage/transfer; item is proxy; XP + storage materials | capacity/hopper claims secondary |
| 6. Select equipment branch | choice | infusion recipes, spirit supply, base gear | make Soulstained Steel combat/tool gear or Spirit Fabric Soulhunter gear; review Curios and staffs | survivability or magic-oriented gear | JEI infusion and equipment entries | optional branches; teach roles, not full-set requirement; XP + safe materials | recipes, stats and guide incomplete |
| 7. Totems and basic rites | optional capability | Runewood Totem Base and correctly spirit-carved poles | carve log side; stack bottom-to-top; activate/deactivate | healing, movement, warding, haste, loyalty, decay, growth and other block effects | Encyclopedia rite entries; exact list needs source confirmation | build harmless rite and observe effect; items are proxies; XP + totem material | external guide; source inventory needed |
| 8. Create Soulwood / Blight | optional world-change branch | Totem base, five Arcane poles, controlled area; sapling for propagation | activate Undirected Rite in contained area; manage blight; cultivate Soulwood | Soulwood materials and corrupted rites | Soulwood / rite entries | optional warning lesson; action evidence over item possession; XP + containment supplies | external guide; spread/cleanup blocker |
| 9. Spirit Crucible | major optional processing | furnace core, Tainted/Twisted Rock, Hex Ash, spirits, Alchemical Impetus | craft via altar, install Impetus, run focus recipes, repair/replace worn tool | spirit-funded material processing and augments | JEI Spirit Focusing and “The Spirit Crucible” | operate one recipe and observe output; item is proxy; XP + processing ingredients | recipes/output guide; source costs unverified |
| 10. Repair Pylon | optional maintenance | pylon, worn valid gear, matching material and spirits | run repair recipe | recover durability | JEI Spirit Repair | show durability before/after; state detector/manual fallback; XP + repair inputs | recipe builder partial; pylon mechanics open |
| 11. Soul Binding / Geas | advanced optional | offering recipes and spirit types; exact station unknown | choose and perform one binding; explain replaceability/exclusivity | Curio/pact/authority capability | JEI Soul Binding and book entry missing | teach one selected pact and full option list; detect bound item/effect; XP + safe secondary material | recipe count secondary; setup gap |
| 12. Explore Weeping Well | rare progression/exploration | locate structure; treat it as a world interaction, not a generic fluid | perform confirmed item transformation; separately document Umbral access | rare transmuted items and Umbral branch | Encyclopedia Arcana → The Weeping Well / Void | optional exploration; require correct output, not structure visit alone; XP + safe exploration reward | guide reports the Well/Void branch; transformation costs and exact entry remain unconfirmed |
| 13. Terminal capability audit | open-ended | meaningful branches above | demonstrate chosen combat, crafting, rite, focusing, binding and rare-resource paths | capability checklist, not forced completion of all recipes | encyclopedia and JEI categories | optional capstone; authentic action per chosen branch; XP + thematic reward | cannot declare endpoint until source/recipe inventory is complete |

### Dependency graph and alternate routes

Soulstone + Runewood → Encyclopedia / Crude Scythe → exposed-soul drops → Spirit Altar → spirit materials → (gear | storage | rites | Crucible | repair).

The Soulwood/Blight route is a potentially irreversible world-editing branch; keep it optional and teach containment. Soul Binding/Geas and Weeping Well are advanced branches with separate unknowns. Do not make every spirit type, rite, pact, or infusion recipe a compulsory quest. Distinguish craft-item tasks from demonstrations of altar, pylon, totem or well operation.

**Native gate vs TMM gate:** No pack progression lock, recipe change, quest reward route or TMM-specific integration has been reviewed for Malum. A recipe or advancement mentioned by secondary material is not proof of a TMM quest gate or a hidden tier.

## Internal synergies and low-hanging setups

- **Early fuel:** Runewood Logs → Arcane Charcoal via furnace (guide reports longer burn time than charcoal). Low cost after the first tree; useful for smelting Soulstone. Confirm ratio/time against exact release JEI or JAR.
- **Altar starter:** Soulstone + Runewood + Gold → Spirit Altar; try a simple Hex Ash recipe if JEI confirms it. Reserve pedestal clearance before decorating.
- **Spirit inventory:** Spirit Fabric → Spirit Pouch and Hallowed Gold → Spirit Jars are described as early storage aids; exact recipes/limits require pinned data.
- **Hunting:** scythe tag → ordinary kill method allows a bow, fall or other finisher after the setup hit. Mob-farm proposals need exposure timing, per-mob yields, collection, ownership and hopper/jar boundaries tested.
- **Material focusing:** Spirit Crucible offers material outputs, but recipe eligibility and Impetus lifecycle need validation before calling it ore doubling.

## Configuration, APIs and current pack behavior

- Pack pin exists at mods/malum.pw.toml; no Malum-specific config override or KubeJS integration file was inspected in this pass.
- Earlier notes attributed Lodestone/Curios/Wayward Attributes/JEI versions and optional Iron's Spells, KubeJS and Create hooks to the now-unavailable source ref. The reachable 1.9.0 source is not evidence for 1.8.2; use the packwiz metadata and exact artifact before asserting dependency or integration behavior.
- The profile does not establish that Create is installed or imply permission to add it.
- Exact 1.8.2 recipe categories, language data, KubeJS serializers, data-pack extension points, config defaults, spirit-drop JSON and installed-addon effects remain unresearched.
- Do not characterize a proposed TMM recipe or quest as existing behavior.

## Progression-to-quest coverage matrix

| Stage | Teaching objective and action | Guide/JEI reference | Authentic completion / FTB detector | Reward direction | Coverage |
| --- | --- | --- | --- | --- | --- |
| Guidebook | obtain/open Encyclopedia Arcana and find material entry | Encyclopedia Arcana | item possession proxies opening | Processed Soulstone/book materials and XP | partial |
| Materials | mine and smelt Soulstone; find/plant Runewood | material entries + JEI smelting | crafted output detects result, not mining | mining/planting supplies and XP | partial |
| Soul hunt | tag target with scythe and collect spirit | soul-hunting entry | spirit item detects result, not exposure event itself | combat utility and XP | partial |
| Infusion | operate altar with target, spirits and pedestal items | JEI Spirit Infusion; exact book page unknown | output item is a proxy; operation detector unknown | safe ingredients and XP | partial |
| Storage | store/retrieve a spirit; test hopper if supported | JEI pouch/jar recipes | possession does not prove transfer | storage supplies and XP | partial |
| Gear | craft one chosen Soulstained or Soulhunter item | JEI and gear entry | item detector | matching safe material and XP | partial |
| Totem/rite | build one harmless rite in correct pole order; observe effect | rite entry unknown | item/base is proxy; effect detector unknown | building supplies and XP | partial |
| Soulwood | create controlled blight and propagate tree | Soulwood/Undirected Rite | possession insufficient; manual observation likely | no gated rare item; XP | partial; containment risk |
| Crucible | run one focusing recipe and inspect output | JEI Spirit Focusing / Spirit Crucible | output is proxy; operation detector unknown | focus ingredients and XP | partial |
| Repair | restore damaged eligible gear | JEI Spirit Repair | durability comparison/manual fallback | repair supply and XP | partial |
| Soul Binding | choose and perform a binding | JEI Soul Binding / missing entry map | bound Curio/effect if detectable; manual fallback | XP + safe secondary material | partial |
| Well | use structure for confirmed transformation and rare access | Encyclopedia → Well/Void | transformation output; task integration unknown | exploration reward without rare bypass | partial |

**Reward policy:** Positive XP and useful fixed item are preferred. Do not reward a rare material that bypasses the operation or another progression gate. Mark item-acquisition tasks as proxies where they do not verify an operation. No quest has been edited.

## Known upstream report requiring a TMM check

The upstream project has an **open report** for Malum 1.8.2 / Minecraft 1.21.1: [issue #477](https://github.com/SammySemicolon/Malum-Mod/issues/477) describes a ServerHangWatchdog deadlock associated with `WeepingWellData.checkForWeepingWell()` being called through a living-entity tick path and scanning for Weeping Wells via chunk access. The reporter says the hang occurred with no Well placed and worsened with many loaded entities. The report is open with no comments at review time. This is a user-reported failure, not a reproduction or source-confirmed defect in TMM; assess it before high-entity-count server use and record exact TMM build, entity count, dimension, thread dump and whether any Well exists if reproduced.

## Open questions and next actions

1. Compare actual pinned JAR bytes against the lock hash and inspect metadata, recipes, loot tables, worldgen, configs, advancements, guidebook pages and item IDs.
2. Recover a valid immutable 1.8.2 source ref or inspect the hash-matched JAR; compare guide text, recipe data and registrations against it.
3. Recover a valid immutable 1.8.2 source snapshot or inspect the hash-matched JAR, then map soul exposure/drop logic, Spirit Altar operation order and failure costs, Crucible wear/output, Repair Pylon, totem behavior, Soul Binding and Weeping Well item/entity interaction.
4. Complete recipe and guide-entry inventory, including alternative materials, spirit families, pacts/geas, repair/focus and late Void branches.
5. Confirm hard/optional dependencies against packwiz; inspect config defaults, live overrides, addons and cross-mod recipes.
6. Resolve FTB detectors with source/task support; item possession cannot prove an altar, pylon, totem or well operation.

## Test plan — not run

No tests were run, as requested for this research-only pass.

- **Positive:** exact pack build, expose a mob with Crude Scythe and kill within the claimed window; record item ID/count/destination. **Boundary:** kill comparable mob without scythe/staff/Soulstained hit; record any spirit drops.
- **Positive:** operate a simple infusion with target, spirits and in-range pedestal items; capture consumed quantities, duration and output. **Boundary:** repeat with an out-of-range pedestal or missing spirit; record start/rejection and resource loss.
- **Positive:** complete one Crucible recipe and record Impetus durability before/after. **Boundary:** test depleted Impetus and missing spirit; record output/input loss/cracks.
- **Positive:** trigger harmless totem rite and measure affected entity/block, range and shutdown. **Boundary:** test just-outside range and unknown pole order.
- **Positive:** perform a Well transmutation and first Umbral interaction. **Boundary:** throw unsupported item and non-player entity; record loss, rejection, damage and cooldown.
- Inspect client guidebook pages, JEI categories and actual FTB detector availability before authoring quests.
- For upstream issue #477, stress a disposable TMM server with increasing living-entity counts in unloaded/never-generated Well areas; capture watchdog thread dump and confirm whether the 1.8.2 pin reproduces the reported chunk-loading hang.

## Evidence ledger

- Packwiz pin in mods/malum.pw.toml on test/full-integration: exact artifact name, Modrinth version ID and SHA512 [pin].
- [Official Modrinth release page](https://modrinth.com/mod/malum/version/BIbVs1fo): Malum 1.8.2 Release, Minecraft 1.21.1, NeoForge, client/server, version ID BIbVs1fo.
- [Official CurseForge file page](https://www.curseforge.com/minecraft/mc-mods/malum/files/7307339): matching filename, NeoForge, 1.21.1, Release, file ID 7307339, uploaded 2025-12-08, release changelog.
- [Source evidence status] The profile's previously recorded 1.8.2 commit `03b743a37f3eeb0cc7f4364f0730e1f135f78408` returned GitHub API 404 on 2026-11-16. The reachable [1.21.1 source snapshot](https://github.com/SammySemicolon/Malum-Mod/tree/458518e3e1f73c3d9bc1c85522743eeb81b4f0c9) declares 1.9.0 and is not treated as evidence for the pinned 1.8.2 artifact.
- [Version-scoped third-party walkthrough](https://www.minecraft-guides.com/mod/malum/): updated 2026-10-06; explicit 1.8.2 gameplay guide. Useful for a draft walkthrough but not primary implementation evidence.
- [Version comparison and recipe-system inventory](https://mc-wiki.net/malum/malum-spirits-and-runes-guide/): reports recipe subsystem counts for Malum 1.8.2 (1.21.1); secondary evidence only.
- Pack behavior, client testing, pinned-JAR recipes, and dependency resolution: not verified.
