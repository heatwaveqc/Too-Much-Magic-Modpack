# Neo Vitae — mechanics and gameplay profile

| Field | Value |
| --- | --- |
| Manifest ID / actual mod ID | neovitae / neovitae |
| Category / parent or addons | magic; standalone magic system with an optional Occultism storage bridge |
| MC / loader / pack target | 1.21.1 / NeoForge / B8 full-integration snapshot |
| Pinned version / artifact / hash | `neovitae-1.21.1-1.1.53.jar`; Modrinth version `FR26bWPa`; SHA-512 `f29b391e391476c232acbe9ba69c42c8539b5eefdd8ca324ab5e04c47dd0e708d7bd9f727cd358099c1c1066b80c759dc86d659549dee54affa17ba588ac5b0b` (`mods/neovitae.pw.toml`) |
| Source snapshot | `breakinblocks/NeoVitae`, branch `1.21.1`, commit [`2744123bb6e4f72c602178b23384be41956eef23`](https://github.com/breakinblocks/NeoVitae/tree/2744123bb6e4f72c602178b23384be41956eef23); Gradle declares MC 1.21.1 and mod version 1.1.53; binary equivalence not tested |
| Guide/documentation version gap | Generated Scriptura Vitae inspected from the version-aligned source snapshot; pinned JAR pages not extracted or compared |
| Profile status | researching |
| Evidence reached | Exact pack pin; version-aligned source, generated guide, data recipes, altar and ritual layouts, mod hooks, release notes and dependency declarations inspected. Earlier B8 dedicated-server boot includes Neo Vitae; no client/world-entry test is recorded. |
| Reviewed | 2026-11-15 |
| Quest ownership | Existing chapter has not been audited in this knowledge pass; no quest files changed |

## Executive gameplay map

**What the player does:** Neo Vitae is a blood-and-spirit magic system. The player first converts nearby life into Essentia Vitae (EV) at a bare Ara Vitae, makes a Novicius Orb, then expands the altar through six total tiers (0–5). EV powers altar transmutation and is stored in orbs, tanks, pipes and routing networks. Parallel branches use Alchemy Arrays for two-offering recipes and world effects; Throwing Daggers and a Hellfire Forge harvest and process Spiritus; an Athanor refines Spiritus and dungeon materials; Ritual Diviners invoke rites; Sentient Armor grows through use and upgrades; and the Demon Realm supplies materials and encounters needed for the upper altar tiers. The generated Scriptura Vitae contains seven categories and 229 entries: Alchemy Arrays (71), Ara Vitae (23), Dungeons (15), Rituals (45), Spiritus (24), Tabula Vitae (41), and Utility (10). [source: `2744123`, datagen book entries and generated Modonomicon JSON]

**Why it matters in TMM:** This is the pack’s explicit blood-altar system and one of three owner-approved alternative center items for the Philosopher’s Stone: the Neo Vitae Mine Key. The design only names the key as a candidate tradeable boss-associated drop; it does not assert an implemented gate. The optional Sigil of Bound Treasures can open Occultism storage controllers/proxies, so Neo Vitae exposes a direct integration surface that the earlier general mod inventory did not capture. [docs: `docs/DESIGN.md`; source: `2744123`, `compat/occultism`]

**Quick start:** Craft and open the Scriptura Vitae (one book plus four redstone dust in a plus pattern). In Utility → The Path of Ascension, start with a diamond on the bare Ara Vitae to draw enough nearby life for the first Novicius Orb. Right-click the orb to bleed into it; the first-use guide recommends making Tabula Rasa and Throwing Daggers. Use Tabula Vitae for Arcane Scribe Tools and reagents. Use the Scribe Tool on the ground to open an Alchemy Array and consult JEI for its two offerings. Damage eligible creatures with Throwing Daggers to collect Spiritus for the Hellfire Forge; a Sentient Sword is the more efficient harvesting route. The guide maps the later altar, ritual, armor and dungeon stages. [source: `2744123`, `GettingStartedEntry.java`, `guide_book.json`, `recipe/guide_book.json`]

### Player-value snapshot

| Five standout capabilities | Three early practical unlocks | Three candidate integrations |
| --- | --- | --- |
| Large tiered blood altar and EV automation; Spiritus-powered crafting and gear; ritual effects including utility, automation and combat; customizable Sentient Armor; instanced Demon Realm dungeons with a permanent late-game route | Ara Vitae transmutation; Alchemy Array crafting/effects; Spiritus gems, charges and tools from the Hellfire Forge | Occultism storage access through Sigil of Bound Treasures; JEI recipe/category integration; Mine Dungeon Key as a candidate ProjectE boss-drop center option |

### Subsystem coverage at a glance

| Subsystem / branch | Mechanics evidence | Progression completeness | Guide/JEI reference | Client validation | Blocker / next action |
| --- | --- | --- | --- | --- | --- |
| Scriptura Vitae onboarding | source: `2744123` | strong | Utility → The Path of Ascension; guide recipe in JEI | not run | Compare book in pinned JAR and assembled client |
| Ara Vitae tiers 0–5, runes, fluids and automation | source: `2744123` | strong structural map; recipe and ritual costs not fully reconciled | Altar → Ara Vitae, Runes, Redstone Automation; JEI | not run | Inspect in-world multiblocks, transfer and recipes |
| Tabula Vitae, flasks and anointments | source: `2744123` | category/branches inventoried; exhaustive ingredient walkthrough remains | Tabula Vitae category; JEI | not run | Audit cross-mod recipe tags and exact outputs |
| Alchemy Arrays and Sigils | source: `2744123` | 71 entries inventoried; all combinations not enumerated | Alchemy Arrays; JEI | not run | Confirm operation UI, consumption and external entity effects |
| Spiritus / Hellfire Forge / Athanor | source: `2744123` | core ladder strong; exact recipe graph partial | Spiritus category; JEI | not run | Verify mob trigger/drop table, throughput and container automation |
| Rituals, diviners and sentient equipment | source: `2744123` | 45 guide entries inventoried; costs and ritual-specific failures remain | Rituals; JEI | not run | Full positive/boundary tests for powerful rites and armor upgrades |
| Demon Realm / dungeon keys / Mine Key | source: `2744123` | access, key sources and major tier-gate role mapped | Dungeons → Daemonium, Keys, Endless Realm, Tau Fruit | not run | Test generated structures, key drops, boss and upper-tier access |
| Occultism storage compatibility | source: `2744123` | API behavior inspected | Sigil of Bound Treasures; Occultism Storage | not run | Run with exact installed versions; source compiles against older Occultism |

## Guidebook onboarding

- **Guide and access:** the item is `neovitae:guide_book`, localized “Scriptura Vitae.” The source recipe is one ordinary book and four redstone dust in a plus pattern. It is therefore an early, craftable book rather than a boss-locked or automatically granted guide. [source: `2744123`, `recipe/guide_book.json`, generated `assets/neovitae/lang/en_us.json`]
- **Earliest stage:** the book can be obtained as soon as vanilla book and redstone are available; its opening entry immediately points to the Ara Vitae.
- **Reference map:** seven categories: Alchemy Arrays (71 entries), Altar (23), Dungeons (15), Rituals (45), Spiritus (24), Tabula Vitae (41), Utility (10). The 18-page “The Path of Ascension” entry is the top-level sequence. The 26-page Ara Vitae entry explains construction, tiers and blood/fluid behavior. Ritual and array entries give named mechanics and often layouts; JEI exposes recipe categories for altar transmutation, arrays, rituals, flasks, forge and Athanor systems. [source: `2744123`, generated Modonomicon data and JEI plugin]
- **Guide limits:** entry coverage does not prove all structures or recipes are obvious in-world. The 1.1.53 release notes specifically address altar tier visual synchronization in multiplayer, and Sanguine Crafter chunk loading/quarry behavior; those fixes should be retained as targeted regression cases. The profile is not an in-game guide usability review.
- **Quest implication for a later authorized pass:** one early how-to should teach crafting/opening the Scriptura Vitae, then point to its guide and JEI; item ownership alone cannot establish the player read or followed its multiblock directions. Every later mandatory tier should show structure placement/validation and successful operation separately from merely possessing an altar component.

## Systems and full mechanics inventory

### Core resources and altar ladder

| System | Inputs / use | Outputs / effect | Limits, failure and evidence |
| --- | --- | --- | --- |
| Anima | Player-linked network bound through the Novicius Orb and related implements | Supplies/links personal vitaemantic instruments and Tabula Vitae use | The generated guide distinguishes personal Anima from altar EV; do not treat them as an interchangeable fluid/resource. [source: `2744123`, Anima and Vitae Link guide entries] |
| Essentia Vitae | Blood generated through self-sacrifice, altar sacrifice, mob kills with an orb equipped, and other source-supported routes; transported as a Neo Vitae fluid | Ara Vitae crafting/transmutation, orbs, tanks, and rituals/crafting paths that explicitly request it | Source lists multiple fluid buffers; the visible basin is not the whole capacity. Exact recipes state their own EV costs. No generic FE or mana equivalence found. |
| Ara Vitae, tier 0 | Place a diamond on a bare Ara Vitae; it draws only the amount needed from nearby life and does not kill the donors | 2,000 EV plus diamond yields the Novicius Orb per guide; altar initially holds 10,000 EV | One item transmuted at a time; if EV runs out, gray smoke appears and progress is lost. Crimson motes show active transmutation. A Divination Sigil reports exact stored amount. [source: `2744123`, `AraVitaeEntry.java` and translation keys] |
| Ara Vitae tiers 1–5 | Arrange rune structures around altar; from later tiers, acquire Demon Realm and hellforged materials | Expands altar recipe eligibility, capacity, rune modifiers and effects | Six tiers are indexed 0–5. Rune counts shown in the guide: tier 1 = 8; tier 2 = 24; tier 3 = 52; tier 4 = 100; tier 5 = 172. Stone pillar bodies may be any solid block where the guide says so. Validate with Divination Sigil. [source: generated altar-tier JSON and Ara Vitae guide] |
| Tier construction gates | Tier 1 uses 8 Blank Runes. Tier 2 expands to an octagon and adds corner pillars capped with Blood Stained Glass. Tier 3 grows the ring and requires Bloodstone Bricks from Tau Fruit. Tier 4 uses Hellforged Blocks from the Demon Realm. Tier 5 uses Crystal Clusters or Crystal Cluster Bricks as capstones, with an immense rune ring. | Higher-tier altar and access to recipes/rituals | Exact 3D positions and rune choices are in the six altar-tier definitions and guide illustrations; prose alone is not a substitute for those diagrams. |
| Orb progression | Novicius Orb is first; higher orbs carry larger capacity and tier-dependent recipe permissions | Personal EV storage and access to higher recipes / support interfaces | A Vitae Link chooses craft tier but can only reach one tier below its bound altar; source guide notes it follows altar downgrades but does not automatically rise with the altar. |
| Fluid automation | Pipes and Routing Nodes fill an altar input buffer; runes improve flow; routing components and Sanguine Crafter support automation | Transfer of EV, routing and crafting at a distance | Source guide gives the base altar input buffer as 1,000 mB and transfer 20 mB each 20 ticks (1 second); a router fills the buffer in one operation. Rune effects change the drain pulse. There is also a 10% internal reservoir and up to 20× overflow capacity; these are distinct from the visible basin and input buffer. Keep source values separate from measured throughput. |
| Redstone / inspection | Redstone control and guide-specific altar interfaces | Pause or operate altar/automation; divination views exact values | Jade support exposes tier, active state, buffer, transfer rate, reserve and related data when installed. No client UI verification. |

### Parallel progression branches

| Branch | How it works | Output / progression role | Guide evidence |
| --- | --- | --- | --- |
| Tabula Vitae | Uses an Orb of Vitae and EV for a recipe-based magical crafting table | Arcane Scribe Tools, sigil reagents, anointments, two-times ore processing and sundries; potioncrafting opens at altar Tier I | Tabula Vitae; Potion Crafting; Anointments; individual flask pages |
| Alchemy Arrays | Use Arcane Scribe Tool on a suitable ground position, then add two offerings as shown by JEI; some arrays are recipe crafting, others invoke effects | Craft Sigils, environment/time/movement/redstone effects, entity manipulation, upgrades, XP handling and other world changes | 71 source-generated entries include crafting, environmental, defense, movement, redstone, time, utility, downgrade, sigil and equipment-upgrade families |
| Spiritus | Throwing Daggers wounded mobs as they die; source and guide describe Spiritus harvested from such kills. Sentient Sword is more efficient. | Raw Spiritus fuels Hellfire Forge, then can be aspected/refined into specialized outputs; aspects include Raw, Ruina, Nihilum, Vindicta and Invictus | Spiritus, Throwing Daggers, Sentient Sword, Aspected Spiritus, Spiritus Gems, Catalysts, Infusion |
| Hellfire Forge | Fuel/craft using Spiritus and item recipes | Spiritus Gems, Sentient Tools, charges, daggers, and upper-tier ingredients including Crystal Cluster inputs | Hellfire Forge, Sentient Tools, Explosive Charges, Spiritus Gems |
| Athanor | Refines recipe-defined items and aspects; accepts item/fluid ingredients per recipe | Converts Demon Realm crops and other inputs into blood shards and other progression materials | Athanor, Tau Fruit, Demonite, crafting and Spiritus aspect entries |
| Potions / flasks | At Tier I, use Alchemy Flasks and recipes; amethyst daggers can be tipped with effects | Dozens of consumable effects, including mobility, combat, visibility, survival and utility | 41 Tabula Vitae entries, including 29 flask-focused entries and potioncrafting |
| Anointments | Apply recipe-made anointments to weapons | Fortune, Looting, Silk Touch, Smelting, melee/bow modifiers and other weapon behaviors | Individual anointment pages and JEI |
| Sentient Armor | Make armor after Tier II, train and evolve it through rites; extraction or reset is possible | Upgrade-point build; Tenebrae-era evolution raises cap from 100 to 300 points | Sentient Equipment and Upgrade pages; Sentient Evolution and extraction/reset ritual pages |
| Rituals | Build documented rune/pillar layout, provide recipe inputs and activate through the correct ritual interface/diviner | Harvest, movement, weather, combat, sacrifice, automation, armor, dungeon, resource-generation and world-effect actions | Ritual Basics, Ritual Diviner, 45 ritual entries, 26-page Deus Rituals reference; individual JEI ritual layouts/recipes |
| Demon Realm / dungeons | At altar Tier II, perform Breaching the Edge of Demon Realm for limited access; later Highway to Hell opens permanent access to the Endless Realm | Tau Fruit, Saturated Tau, Weak Blood Shards, Demonite, Hellforged Blocks/Parts, Mine Keys and boss/elite loot support upper altar tiers | Dungeons category: overview, keys, bestiary, drops, Tau Fruit, Demonite, lobby, Endless Realm and traps |
| Spiritus and EV networks | Routing Nodes and accumulators move and buffer Spiritus; the Sanguine Crafter can automate altar-style crafting | Automates resource movement/crafting; supports network behavior beyond a single altar | Routing Nodes (26 pages), accumulator (9), Sanguine Crafter (12), aura/gauge and storage guides |

### Demon Realm and late-game route

1. At Ara Vitae Tier II, use the ritual named **Breaching the Edge of Demon Realm** for limited entry. Locate Tau Fruit, then cultivate it into Saturated Tau and refine it in the Athanor to obtain Weak Blood Shards. These are named by the guide as the key to the next altar stage, stronger anointments and potion catalysts. [source: `2744123`, Getting Started and Dungeons entries]
2. The next altar stage requires Bloodstone Bricks whose acquisition depends on Tau Fruit. Further rites/diviner upgrades unlock a wider ritual catalogue and Sentient Armor evolution. [source: `2744123`, Ara Vitae, Getting Started, ritual guide entries]
3. The permanent **Highway to Hell** opens the Endless Realm branch. Deep exploration yields Demonite Ore; the source overview says it and a Dominus Orb support the ultimate altar progression. Intricate Hellforged Parts combine with Netherite Scrap to double altar-rune power. [source: `2744123`, Getting Started / Endless Realm / Demonite]
4. Top tier requires Crystal Clusters or valid Crystal Cluster Bricks; the guide's listed Hellfire Forge recipe for Crystal Clusters includes Sculk, Tabula Aetherea, Weak Blood Shard and Nether Star. Crystal Clusters cap the tier-5 altar pillars. [source: `2744123`, guide translation keys and recipe data]
5. **Mine Dungeon Key** is not craftable: the Keys guide says the Foreman drops multiple and Mines trial spawners can also drop them; the Foreman guards the mine and the key opens deeper doors. The Foreman is not the Chaos Guardian/Ender Dragon; do not describe the key itself as a boss kill unless a separate TMM gate uses that exact encounter. The installed-pack center-gate use remains design intent only. [source: `2744123`, Dungeons → Keys]

## Material and capability I/O contracts

| System | Input type / trigger | Consumed or remainder | Output type / location / cadence | Automation/API and limits |
| --- | --- | --- | --- | --- |
| Ara Vitae recipe | Item in altar inventory plus recipe-specific EV; right-click inserts item | Source says one item at a time; EV consumed over operation; on depletion progress resets | One recipe result in altar output/inventory per completed operation | Neo Vitae recipe type is exposed to JEI/EMI plugin code; altar capability exposes tier, current essence, capacity, progress and consumption rate. No generic fluid or FE substitution. |
| Tier 0 self sacrifice | Nearby living player/entity context, diamond item triggers first crafting | Only needed health is drained; guide says donors are not killed | EV in basin then resulting Novicius Orb | Trigger-based nearby life drain; exact radius, eligible entity list and multiplayer ownership need runtime check. Release note 1.1.53 states self-sacrifice cannot kill the user even with damage amplifiers. |
| Orb kill harvesting | Eligible mob death while Orb of Vitae is in offhand | Mob life/death event; exact entity values not audited | Essence directly into orb | Guide says it avoids bleeding the player; exact supported mobs/amounts require whitelist and measurement. |
| Throwing Dagger Spiritus | Eligible mob wounded by a dagger and then killed | Mob is killed; exact Spiritus amount/drop chance varies by source/config | Spiritus associated with player/weapon inventory or relevant system | Event-driven rather than a continuous timer; exact eligible entities, rates and farming boundary need tests. |
| Alchemy Array | Two recipe offerings or recipe-specific world state | Per-array recipe JSON; not generically inferable | Item output, entity/world effect, or other explicitly coded outcome at array | Item and effect recipe types; ritual/array JEI categories. Do not equate arrays with a general item/fluid processor. |
| Ritual | Layout, diviner/activation item, recipe ingredients; optional entity, target, biome, weather or other world condition | Per ritual; recipe determines consumption and cooldown | Items, entity spawns, world changes, buffs, damage or other effects | Ritual layouts and effect hooks; exact input/target conditions vary. Any high-impact claim needs positive and boundary tests. |
| Spiritus transport | Spiritus aspect plus node/accumulator network | Transferred quantity removed from source store | Spiritus aspect deposited in destination chunk or storage component | Native routing interfaces/API exist; no assertion of cross-mod generic gas/fluid compatibility. |
| Occultism storage bridge | Bound Treasures Sigil targets an Occultism Storage Controller or proxy | No item consumption specified for opening | Opens storage menu for player | Calls Occultism `IStorageController` / `IStorageControllerProxy` and `StorageControllerContainerBase`; checks access/reservation. Requires exact versions and client test. |

No source evidence found for arbitrary item input, generic FE, generic player XP, or foreign mana as a universal Neo Vitae energy contract. The mod does implement its own fluid and several item/entity/world-event interfaces. Every individual array/ritual/recipe has its own contract.

## Pack compatibility, issues and balance leads

### Source-supported bridge: Occultism

Neo Vitae's optional bridge is not just a shared recipe. The Sigil of Bound Treasures recognizes block entities implementing Occultism's `IStorageController` or `IStorageControllerProxy`; if the player can access it, Neo Vitae opens the provider and reserves the storage position. If lookup throws, compatibility is disabled for the remainder of that session with a warning. [source: `2744123`, `compat/occultism/OccultismCompat.java` and `OccultismHooks.java`]

The upstream Neo Vitae 1.1.53 development build declares Occultism `1.21.1-neoforge-1.224.3` as `compileOnly`; TMM pins Occultism 1.227.0. This three-release difference is a **version/API smoke-test item**, not a demonstrated incompatibility: the bridge calls the controller/proxy interfaces and a container helper whose binary linkage with 1.227.0 has not been checked in a client. TMM pins Modonomicon 1.123.0 and GeckoLib 4.9.3, newer than the source build's 1.120.1 and 4.8.4 declarations. The earlier B8 dedicated-server pass establishes co-load/startup only, not menu operation, rendering, guidebook or JEI behavior. [pin: `mods/neovitae.pw.toml`, `mods/occultism.pw.toml`, `mods/modonomicon.pw.toml`, `mods/geckolib.pw.toml`; source: `2744123`, `gradle.properties`]

### Candidate gate and balancing risks

- **Philosopher's Stone center key:** docs/DESIGN.md and docs/PROGRESSION.md choose Mine Dungeon Key as one of three alternative boss-associated center drops. The matching guide instead says the key drops in multiples from the Foreman and trial spawners. This makes it a dungeon progression item whose exact scarcity, repeatability, chest/loot paths and tradeability affect the ProjectE gate. Source confirms the named drops, but not TMM loot tables or enforced recipe gating. Confirm the pack's Mine Key item registry, Foreman/table and trial spawner loot paths, and test buying/crafting/conversion bypasses before implementing the planned center recipe.
- **Blood/EV throughput:** multiple routes feed EV, including sacrifice, orb-on-kill behavior, fluids and routing; overflow can hold 20× altar capacity and hidden buffers obscure displayed values. This can substantially alter time-to-tier. Source defaults are not balance measurements. Record controlled input, altar tier, rune set, world ticks and output during any future economy pass.
- **Mob/event farms:** dagger kill Spiritus and orb kill EV are event-driven. Do not treat a custom farm, Looting enchantment or modded mob as valid until the exact damage/death attribution, entity tags and rate are tested. Use the positive and boundary cases in the test plan.
- **Demon Realm structure compatibility:** worldgen and portal behavior have not been validated against Tectonic or the pack's other structure/dungeon mods. Treat absence of conflicts as unknown, not compatible.
- **No demonstrated incompatibility:** current exact pins were included together in a successful full server startup snapshot. No crash or server-side incompatibility has been demonstrated. Client rendering/menus and pairwise gameplay remain unverified.

## Complete player progression / walkthrough blueprint

This table follows the mod's mandatory escalation and its optional branches; it does not turn every one of 229 guide entries into a quest. The profile remains `researching` until each critical recipe/layout and the unresolved release/client gaps are audited.

| Stage / choice | Player action and recipe/build help | Observable success distinct from item possession | Guide / JEI reference | Quest coverage / remaining research |
| --- | --- | --- | --- | --- |
| 0. Obtain guide | Craft Scriptura Vitae from book + four redstone in plus pattern; open it | Book accessible and category entries readable | Utility; JEI shaped recipe | No quest audit yet; client book render/access pending |
| 1. First blood and Orb | Place Ara Vitae; place diamond; allow altar to draw nearby life; complete Novicius Orb at 2,000 EV | Completed Ara Vitae recipe and orb output; distinguish from merely holding a diamond | Utility → Path of Ascension; Altar → Ara Vitae; JEI altar recipe | Verify radius, sources and recipe display |
| 2. Personal baseline tools | Right-click orb to bleed into it or use a supported source; craft Tabula Rasa and daggers; use Tabula Vitae for Arcane Scribe Tool | Table opens and completes one listed recipe | Getting Started; Tabula Vitae; JEI | Exact inputs, tier requirement, remaining orb value and Anima effects to audit |
| 3. Arrays and Spiritus | Place array using Scribe Tool; perform a representative two-input recipe; damage and kill eligible mob with Throwing Dagger; process Spiritus in Hellfire Forge | Array completes with expected output/effect; Spiritus counter/forge output changes | Alchemy Arrays; Spiritus; JEI | Full consumption, entity eligibility and rate unknown |
| 4. Tier I altar | Place 8 Blank Runes in documented tier 1 layout; validate; use upgraded altar and explore runes/sigils | Divination reports tier 1; an upgrade-only recipe succeeds | Ara Vitae, runes, sigils, Tabula Vitae; JEI | Need exact tier-transition requirements/caps and multiplayer test |
| 5. Tier I potions | Use available tier-one Tabula Vitae recipes for flask/anointment | Flask or anointed weapon shows and performs its expected effect | Potion Crafting; Alchemy Flasks; Anointments; JEI | Individual effect durations/stacking/test fixtures needed |
| 6. Tier II / first rites | Build 24-rune ring and corner pillars; make Ritual Diviner; perform a low-risk rite; make Sentient Armor | Divination reports tier 2; ritual gives visible effect; armor gains a verified upgrade | Ara Vitae; Ritual Basics/Diviner; Sentient Equipment; JEI | Exact recipes/layout/costs and failures need audit |
| 7. Limited Demon Realm | Perform Breaching the Edge ritual, enter the realm, obtain and cultivate Tau Fruit, refine Saturated Tau in Athanor | Realm entry, fruit yield and Weak Blood Shard output observed | Ritual entry; Dungeons → Tau Fruit; Spiritus → Athanor; JEI | Realm discovery/portal persistence, exact harvest/yield and mob difficulty not tested |
| 8. Tier III and Tenebrae | Construct 52-rune tier with Tau-derived Bloodstone Brick pillars; obtain/use Tenebrae Diviner and higher rites | Altar validates; Tenebrae ritual can be selected/performed | Ara Vitae, Demonite/ritual guide, JEI | Exact recipe chain, gates and source data need full audit |
| 9. Armor evolution / optional paths | Evolve Sentient Armor from 100 to 300 points; choose reset (loses training) or extraction into tomes (preserves upgrades); explore arrays, sigils and rituals | Upgrade cap changes; reset/extraction produces stated result | Getting Started armor pages; Sentient Upgrades; ritual pages | Destructive reset/extraction boundary must be tested before advising players |
| 10. Permanent and deep dungeon branch | Perform Highway to Hell; explore Endless Realm; obtain Demonite and Intricate Hellforged Parts, combine parts with Netherite Scrap | Permanent gate opens; listed materials are retrieved; rune upgrade doubles intended rune strength | Dungeons → Endless Realm/Demonite; rituals; JEI | Structure-generation, key/loot routes, repeatability and capstone names remain |
| 11. Tier IV–V endgame altar | Use Demon Realm materials, Dominus Orb and other recipe inputs to create 100- and 172-rune altar layouts; craft Crystal Clusters and place as capstones | Divination reports each tier; Tier V recipe succeeds | Ara Vitae tier pages; Spiritus → Hellfire Forge; JEI | Exact recipe graph, spatial diagrams and capstone alternatives require JAR/client comparison |
| Optional branches | Spiritus aspects and storage routing; all sigil/array families; all ritual variants; flask/anointment collection; Mine Key route; sentient armor builds | Each chosen branch produces its own operation/output | All seven guide categories; JEI | Catalogues are extensive; teach representative routes and point to discoverability rather than requiring all recipes |

## Release notes and version-specific follow-ups

The 1.1.53 release notes report: a chunk-loading-off control for the Sanguine Crafter; an Endless Quarry restart fix when the block under the Master Stone changes; Clear Rock mode clears four times more rock per pulse; the Sanguine Deus Block covers a 511×511 quarry area; self-sacrifice cannot kill the player even with damage amplifiers; and Tier 5/6 altar visuals synchronize in multiplayer. These are upstream release-note facts, not in-pack tests. They expose four worthwhile regression cases: toggle and unload behavior, quarry restart and clearance, altar tier rendering/sync, and self-sacrifice at amplified damage. [docs: official CurseForge 1.1.53 release notes]

## Disposable-world test plan

| Test | Positive / boundary case and setup | Expected observable result | Performed? / evidence |
| --- | --- | --- | --- |
| Guide access | Craft exact plus-pattern recipe; compare with no/incorrect redstone arrangement | Exact Scriptura Vitae recipe succeeds; invalid pattern fails | not run; source recipe only |
| Tier 0 altar | Empty basin + diamond + eligible nearby player; repeat with no eligible donor or insufficient life context | Exactly needed life is drained without killing player; 2,000 EV yields orb; invalid context fails safely | not run; source/1.1.53 changelog |
| Altar depletion | Start supported recipe with sufficient then deliberately insufficient EV | Success outputs result; depletion produces guide-described smoke and resets progress | not run |
| Layout boundary | Build each altar tier per guide; remove one rune/pillar and test with a valid part substitution | Valid layout reports expected tier; removed required part invalidates; permitted solid-block substitution remains valid | not run |
| EV transfer | Measure input buffer, altar, internal reservoir and visible pool over declared ticks; repeat with/without transfer runes | Observed values follow source buffer/transfer behavior and reveal UI lag/hidden storage | not run |
| Orb kill harvesting | Test vanilla and representative modded eligible/ineligible mobs with orb offhand; compare to orb absent | Confirm eligible exact entity tags, EV values and whether another killer gets credit | not run; no whitelist conclusion |
| Dagger Spiritus | Kill same mob with/without dagger wound, then with Sentient Sword; hold other variables fixed | Spiritus appears only under source conditions; compare actual rates and credit ownership | not run |
| Occultism menu bridge | Use Sigil on Storage Controller, proxy and unrelated block; repeat with denied access | Correct menu opens and reservation works only for valid accessible controller/proxy; unrelated block does not open | not run; exact installed Occultism is 1.227.0 vs bridge compileOnly 1.224.3 |
| Mine Key gate | Exercise Foreman, Mines trial spawner and all pack shop/crafting/conversion paths | Confirm key drops/quantities and ensure Stone center has no unintended bypass | not run; source docs + `docs/PROGRESSION.md` candidate |
| Client/network regression | Two clients view changed tiers; test Scriptura pages, JEI, Sanguine Crafter chunk toggle and quarry restart | Guide/JEI display; tier visuals sync; chunk control/restart/clear-rock behavior matches 1.1.53 release | not run |

## Evidence ledger

| Claim / section | Source URL or exact inspected `file:path` + immutable SHA | Evidence tag / version | What it proves / remaining limits |
| --- | --- | --- | --- |
| Exact TMM binary pin | `mods/neovitae.pw.toml` | [pin] 1.1.53, Modrinth `FR26bWPa`, SHA-512 above | Exact selected pack artifact identity, not local JAR contents |
| Upstream source identity | https://github.com/breakinblocks/NeoVitae/tree/2744123bb6e4f72c602178b23384be41956eef23; Gradle `mod_version=1.1.53`, MC 1.21.1 | [source: `2744123`] | Version-aligned source; does not prove binary built from commit |
| Guide/progression | `src/datagen/java/.../book/utility/GettingStartedEntry.java`; `.../book/altar/AraVitaeEntry.java`; `src/generated/resources/data/neovitae/modonomicon/books/guide/**`; generated `assets/neovitae/lang/en_us.json` | [source: `2744123`] | Generated book structure, prose, tier counts/claims; not an in-client guide review |
| Exact recipe and tier structures | `src/generated/resources/data/neovitae/recipe/**`; `src/generated/resources/data/neovitae/neovitae/altar_tier/*.json` | [source: `2744123`] | Source recipe/structure definitions; no loaded-registry audit |
| Occultism bridge | `src/main/java/com/breakinblocks/neovitae/compat/occultism/{OccultismCompat,OccultismHooks}.java` | [source: `2744123`] | Controller/proxy types and menu operation; API linkage against TMM pin untested |
| Release-specific fixes | https://www.curseforge.com/minecraft/mc-mods/neovitae/files/ | [docs: 1.1.53 release notes] | Publisher-reported changes; not test evidence |
| Server co-load | `docs/COMPATIBILITY.md`, B8 full integration result | [pack-test: B8 server startup] | Included Neo Vitae JAR reached dedicated-server ready with selected roster; no client or mechanic guarantees |
| Mine Key gate intent | `docs/DESIGN.md`, `docs/PROGRESSION.md` | [docs: 2026-11-14 owner decision] | Owner-selected candidate ingredient; implementation is explicitly not yet present |

## Unknowns and next research

- [ ] Compare generated Scriptura Vitae and core JEI pages from the pinned JAR against the matching source-generated docs; exact file download was not available in this pass.
- [ ] Audit all Ara Vitae, Tabula Vitae, Hellfire Forge, Athanor, array and ritual recipe inputs/costs against the relevant tier. Current profile maps the progression and guide structure, not every recipe.
- [ ] Enumerate exact mob/entity selectors, amount formulae and kill attribution for offhand-orb EV, dagger Spiritus and any sacrifice rituals.
- [ ] Test the Occultism storage bridge against TMM's 1.227.0, especially proxied controllers, permissions and open-menu reservation.
- [ ] Check Demon Realm portal/structure generation with pack terrain and dungeon mods; confirm Mine Key routes and item registration in the actual loaded registry.
- [ ] Test client book/JEI rendering, altar structure guidance and 1.1.53 multiplayer fixes.
- [ ] Audit TMM's existing Neo Vitae FTB chapter after research scope authorizes quest inspection; no quest implementation in this pass.

## Revision notes

- 2026-11-15 — Created exact-pin, matching-source profile; mapped guidebook access, altar tier ladder, parallel systems, Demon Realm progression, Occultism storage bridge and release-specific follow-ups. Kept `researching` because recipe coverage and client/integration checks remain.
