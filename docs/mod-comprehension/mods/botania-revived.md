# Botania Revived — mechanics and gameplay profile

| Field | Value |
| --- | --- |
| Manifest ID / actual mod ID | `botania-revived` / `botania` |
| Category / parent or addons | Nature/mana magic; unofficial NeoForge port; TMM also pins [Extra Botany Revived](extra-botany-revived.md) |
| MC / loader / pack target | 1.21.1 / NeoForge / B8 full-integration snapshot |
| Pinned version / artifact / hash | `Botania-Revived-1.21.1-2.0.2-NeoForge.jar`; CurseForge project 1725641, file 9577651; SHA-1 `b333e5ac4fd55b0e8822238ced0b25b193db9628` matches `mods/botania-revived.pw.toml` (`[jar: SHA1]`) |
| Source snapshot | Official port tag `curseforge-neoforge-1.21.1-2.0.2`, commit [`5f803227d484598da1f1093a91d5d7cf5cf9600d`](https://github.com/Velron-Devs/Botania-Revived/tree/5f803227d484598da1f1093a91d5d7cf5cf9600d); exact source version and direct artifact data inspected |
| Guide/documentation version gap | Exact pinned JAR Lexica data inspected: 13 categories, 291 entries. Lexica rendering not client-tested. |
| Profile status | researching |
| Evidence reached | Hash-matched JAR, exact metadata, Lexica entries, recipes, loot tables, worldgen, release source, official port docs and optional Ars bridge inspected. B8 server boot includes the mod; gameplay/client remains unverified. |
| Reviewed | 2026-11-15 |
| Quest ownership | Existing TMM Botania chapter not audited in this pass; no quest file changed. |

## Executive gameplay map

**What the player actually does:** Botania is a nature-themed mana technology system. The player gathers Mystical Flowers, makes the Lexica Botania, Petal Apothecary and Pure Daisy, then creates Livingwood/Livingrock and the Wand of the Forest. Generating Flowers convert consumable resources or events into mana; Mana Spreaders transmit bursts into Mana Pools, which store mana and power devices. Mana crafts new flowers in the Petal Apothecary, Runes on the Runic Altar, and advanced materials/gear on the Terrestrial Agglomeration Plate. The Alfheim Gateway trades selected items for elven materials. Functional Flora and Natural Apparatus automate or simplify tasks; tools, baubles, brewing, Ender Artifacts and optional modules expand the system. The Gaia Guardian provides the main high-tier combat gate and Gaia Spirits for powerful rewards. The exact Lexica in the locked JAR has 13 categories and 291 entries, including 25 Botania Extras entries, but category visibility is module/advancement-dependent. [source: `[jar: SHA1]`, `data/botania/recipe/**`, Lexica and advancement data; source commit `5f80322`]

**Why it matters in TMM:** Botania is a full, broad magic branch with renewable mana, automation, utility gear and a boss. The 2.0.2 artifact is Botania Revived, an unofficial altered NeoForge port with the same `botania` mod ID/namespace, not Vazkii's original release. It includes several port-specific modules and a native Ars Nouveau bridge. Because its optional Asgard and creative modules are enabled by default upstream, and no TMM Botania config override is tracked, their balance consequences need review before a survival-ready progression is described as settled. [source: exact JAR config defaults/source, repo config audit]

**Quick start:** Break grass and collect naturally generated Mystical Flowers; make the Lexica from a sapling and book. Read Basics → Flowers, Petal Apothecary and Pure Daisy. Use the Apothecary with water, the listed petals and a seed to make a Pure Daisy; surround it with logs or stone to create Livingwood/Livingrock over time. Make a Wand of the Forest, Petal Apothecary, Mana Spreader and Mana Pool. For the first mana loop, make a Lexica-documented generating flower such as Endoflame; bind the flower to a spreader and the spreader to a pool with the Wand. Use the Lexica and JEI for exact recipes and each flower's accepted fuel/trigger. [source: `[jar: SHA1]`, `basics/lexicon`, `basics/flowers`, `basics/pure_daisy`, Lexica recipes]

### Player-value snapshot

| Five standout capabilities | Three genuinely early practical unlocks | Three candidate integrations |
| --- | --- | --- |
| Renewable event/item-driven mana; mana-powered crafting; broad automation flowers/devices; functional combat and travel gear; Gaia Guardian endgame and loot | Pure Daisy converts common logs/stone into core crafting materials; Endoflame turns burnables into mana; Mana Pool + Spreader powers multiple devices | Ars Nouveau Source/mana and player-resource bridge; Botany Pots flower/fertilizer support; Extra Botany Revived addon content |

### Subsystem coverage at a glance

| Subsystem / progression branch | Mechanics evidence | Progression completeness | In-game guide/JEI reference | Client validation | Blocker / next action |
| --- | --- | --- | --- | --- | --- |
| Lexica onboarding and Mystical Flowers | Exact JAR recipe, book data, advancements | Strong | Basics → Lexicon, Flowers, Flower Pouch | not run | Confirm entry display and advancement unlocks in client |
| Petal Apothecary / Pure Daisy | Exact recipe and multiblock guide page | Strong | Basics → Petal Apothecary and Pure Daisy; JEI | not run | Verify water basin interaction, item order and conversion time |
| Mana generation and transmission | Exact flower data/code, mana block classes and Lexica | Strong branch map; rates not measured | Generating Flora, Mana Manipulation, individual flower pages | not run | Test two starter flowers, fuel triggers, spreader losses and pool readouts |
| Mana Fluxfield → FE | Exact port `PowerGeneratorBlockEntity`, `XplatImpl`, recipe and Lexica entry | Source contract mapped: 10 FE per mana; 12,800 FE buffer; output attempted each server tick | Mana → Mana Manipulation → RF Generator/Mana Fluxfield; JEI | not run | Verify full/near-full input, output rates, adjacent capability sides, partial acceptance and live pack consumers |
| Runic Altar / rune crafting | Exact recipes, Lexica entries and recipe viewers | Strong | Basics → Runic Altar; JEI | not run | Verify required Livingrock return item and craft completion feedback |
| Alfheim / Elven Gateway | Exact recipes and Lexica | Path mapped; exact full trade set not inventoried | Alfhomancy/Ender Artifact sections; JEI, gateway | not run | Test gateway construction, portal persistence and trade outputs |
| Terrasteel / Terrestrial Agglomeration | Recipe, multiblock and Lexica pages | Strong | Basics → Terrasteel; JEI multiblock preview | not run | Measure mana delivery and simultaneous item consumption |
| Functional Flora, devices, tools, baubles, brew | 291 entry files and exact recipes | Major branches mapped; every variant not enumerated | Functional Flora, Natural Apparatus, Mystical Items, Trinkets, JEI | not run | Test high-impact automation, equipment and recipes individually |
| Gaia Guardian / Gaia gear | Recipe, loot and advancement data; guide pages | Structural map; battle not tested | Alfhomancy/Challenges and Gaia recipe entries | not run | Test summon arena, wave phases, death/reward, player count and loot |
| Botania Revived modules | Exact source/JAR and conditional entries | Defaults known; TMM runtime config absent | Lexica → Modules and module-specific entries | not run | Audit Asgard, Loonium, creative outputs, worldgen and module balance |
| Ars Nouveau bridge | Exact Botania bridge source and Ars pin | Full code pathway mapped; no exact-pair test | Botania Lexica → Modules/Ars Nouveau; Source and Mana entries | not run | Run resource conversion and spell/item-cost tests at exact pins; see interaction report |
| Extra Botany Revived | Exact TMM addon pin and official release metadata | Addon roster known; no per-feature profile | Lexica includes Botania Extras only; addon guide updates Lexica | not run | Create a separate addon profile and verify actual 1.0.4 features |

## Lexica onboarding and reference map

- **Guide and access:** `botania:lexicon`, localized “Lexica Botania.” Its shapeless recipe uses any item in `minecraft:saplings` plus `minecraft:book`. This is available at the start of a new world after collecting a book and sapling; do not treat Lexica ownership as proof a player read the relevant page. [source: `[jar: SHA1]`, `data/botania/recipe/lexicon.json`, `assets/botania/patchouli_books/lexicon/en_us/entries/basics/lexicon.json`]
- **Guide map:** Basics and Mechanics (11), Mana Manipulation (20), Generating Flora (16), Functional Flora (31), Natural Apparatus (26), Alfhomancy (15), Ender Artifacts (21), Mystical Items (43), Trinkets and Accessories (31), Botania Extras (25), Modules (14), Miscellaneous (17), Challenges (21). Total 291 entries. Some pages unlock on specific Botania advancements or item turn-ins; Extras and optional module pages are conditional.
- **In-game recipes:** JEI/EMI/REI support mana infusion, petal apothecary, runic altar, Terrestrial Agglomeration Plate, Elven trade, brewery, Pure Daisy, Orechid, Orechid Ignem and Marimorphosis. Botania Revived adds Ponder scenes and Jade/WTHIT tooltips when those optional integrations are installed. TMM has JEI, Jade and WTHIT but not Ponder; no bridge should be assumed from missing optional mods.
- **Guide insufficiencies:** flower-specific fueling/timing should be taught from each Lexica entry and checked against actual behavior. Multiblock recipes and mana-transfer loss need direct setup guidance; page presence is not proof of valid construction. The 2.0.2 release notes/docs describe Botania Revived-specific changes and additional modules; older official Botania tutorials should not be assumed to cover those changes.
- **Future quest implication:** an early quest should teach making/opening the Lexica. Progression quests need to distinguish an item recipe from an operating system: Pure Daisy conversion, an actual generating flower's output, mana transfer into a pool, and one completed mana recipe are four observable actions.

## Core progression and mechanics inventory

| System | Access and inputs | Output / player action | Limits, failure and evidence |
| --- | --- | --- | --- |
| Mystical Flowers | Wild patches and flower drops; common flowers are dye-color variants. Flower Pouch stores gathered flowers. | Petals, seeds, dyes and components for Botania recipes | Flower generation, biome distribution and drop rates are separate worldgen/loot questions; one flower stack does not represent the full color palette. |
| Petal Apothecary | Fill the block with water; throw exact recipe petals into the water, then add seed as the completing catalyst | Living flower / functional or generating flower recipe result | Ingredient list, order and seed use are recipe-specific. The Lexica entry and JEI provide exact recipes. |
| Pure Daisy | Make through Apothecary; surround with supported block types such as wood or stone | Converts blocks to Livingwood/Livingrock and several other documented material conversions | Conversion is a timed block-state operation; requires valid nearby blocks and free space. Do not count only Daisy crafting as proof of successful conversion. |
| Mana generation | Generating flower-specific inputs/events, such as burnable fuel, consumed items, nearby entities, explosions, rain or other trigger classes | Mana units stored in each flower buffer and emitted as Mana Bursts | Each flower has its own trigger, cooldown, buffer and throughput. Some flowers are passive or temporary; classify them by exact entry/code before recommending automation. |
| Mana Spreader network | Bind flower → spreader → receiver using Wand of the Forest; configure aim, lenses and spreader tiers as needed | Mana burst carries mana to a compatible receiver | Burst transmission can lose mana over distance; spreader fires based on target accepting mana, with one burst in flight at a time for standard spreaders. Check the beam and target UI. |
| Mana Pool and mana items | Receive bursts; use a Wand to bind pool to spreaders or machines; fill Mana Tablets/Mirrors by recipe/interaction | General mana buffer, crafting input, portable resource | Mana remains a distinct Botania resource, not a fluid or player spell mana. The separate Mana Fluxfield converts incoming Mana to FE at 10 FE per Mana; this is a specific adapter, not a universal unit equivalence. Pool tier alters functionality; exact capacity is item/block-specific. |
| Mana Fluxfield | Aim a Mana Spreader at `botania:mana_fluxfield`; connect an adjacent FE-capable block/cable | Receives Mana bursts; each Mana becomes 10 FE. Stores up to 12,800 FE (1,280 Mana-equivalent); each server tick attempts to export at most 1,600 FE across adjacent receivers | Lexica → Mana → RF Generator entry and JEI recipe; source adapter probes each of six neighboring faces, then unsided capability fallback; accepted energy alone is removed from its buffer. Near-full burst clamping and exact consumer compatibility need tests. |
| Mana crafting | Petal Apothecary consumes petals/seed; Runic Altar consumes ingredients and mana; Mana Infusion uses an item and mana; Terrestrial Plate performs the late recipe with a multiblock | Flowers, runes, materials, gear and creative outputs where recipes allow | Viewer recipe shows ingredient and mana requirement; operation only succeeds with valid structure and delivered mana. Exact totals are in the locked recipe records. |
| Alfheim / Elven Gateway | Gateway recipe and paired Natura Pylons; supply mana and throw trade inputs | Elven trade outputs; advanced Alfheim materials | Trade is an exchange, not an item input to a normal crafting table. Lexica and JEI show current trade pairs; no foreign portal dimension is assumed. |
| Functional Flora | Species-specific biome/entity/block/event targets plus mana | Crop/soil/plant growth, block placement/mining, item handling, mob effects, damage, protection, teleportation and other utilities | Many flowers have distinct range, target filters and mana costs; cannot compress their behavior into a single generic “automation” claim. |
| Devices and Corporea | Mana, redstone, items, comparator signals, Corporea sparks/requests, structures | Mana storage/network management, redstone control, crafting/transport systems and routing | No Create, AE2 or generic storage inference. Distinguish Botania's bounded Corporea network from general-purpose logistics. |
| Terrasteel / Terrestrial Agglomeration | Plate with three Natura Pylons; provide listed ingredients and a large mana stream | Terrasteel ingot and other high-tier recipes | Multiblock and inputs are described in Basics → Terrasteel. Exact build is visualized by Lexica/JEI; do not mistake crafted plate for formed multiblock. |
| Gaia Guardian | Gaia Pylon structure/ritual and configured summon recipe | Gaia Spirit and high-tier materials/equipment | Boss fight has phases and mob/player safety concerns; exact arena, waves, death rules, drops and multiplayer scaling need a disposable world. Gaia is a distinct progression endpoint, not merely a crafting ingredient. |
| Botania Revived modules | `[modules]` config keys read at startup; module registration conditions | Optional items, recipes, loot, structures and sky rendering | Upstream defaults enable Botania Extras, Exploration, Creative Crafting and Asgard; Skybox is off. Disabled module content is not registered. No TMM Botania config override is tracked, so upstream defaults are the expected starting behavior. |
| Ars Nouveau compatibility | Optional integration activates with Ars Nouveau; see cross-mod report | Bidirectional mana/Source capability adapters plus player spell/gear conversion | Exact ratios and item paths are code-backed but not measured. Both systems remain distinct resources; do not treat `1 Source = 15 mana` as a universal conversion for every player/machine path. |

## Walkthrough blueprint for later quest work

| Step | Player action | Observable success | Lexica/JEI reference and status |
| --- | --- | --- | --- |
| 0. Open the guide | Craft and open `botania:lexicon` using any sapling plus a book | Patchouli book opens to Basics and Mechanics | Lexica → Lexicon; exact recipe in JEI |
| 1. Gather flowers | Find/break Mystical Flowers and craft a Flower Pouch | Multiple flower colors are collected and stored | Basics → Flowers, Flower Pouch |
| 2. Make natural blocks | Use Petal Apothecary to craft a Pure Daisy; build a valid surrounding conversion patch | Logs/stone visibly become Livingwood/Livingrock | Basics → Pure Daisy; Petal Apothecary category |
| 3. Build first mana device | Craft and bind a Wand, Mana Spreader, Mana Pool and a chosen generating flower | Mana burst arrives and pool's mana value increases | Mana Manipulation → Spreader/Pool; chosen Generating Flora entry |
| 4. Complete one mana recipe | Make an item through Petal Apothecary and one through Runic Altar or Mana Infusion | Result appears and expected mana is spent | Basics → Apothecary/Runic Altar; JEI uses |
| 5. Expand generation/utility | Pick a flower matching an actual available resource loop, configure spreader and validate its output | Repeatable mana production is visible; functional flower carries out its task | Generating Flora, Functional Flora; classify trigger and throughput before questing |
| 6. Make runes and late materials | Craft Runic Altar, supply correct ingredients/mana; build Terrestrial Plate and pylons for Terrasteel | Recipe completes and output is awarded | Basics → Runic Altar / Terrasteel; JEI pattern preview |
| 7. Open Alfheim | Complete the Elven Gateway recipe, place gateway/pylons and complete a selected trade | Correct elven material is produced | Alfhomancy / Ender Artifacts; elven trade JEI category |
| 8. Choose parallel utility branches | Use selected Functional Flora, Natural Apparatus, Baubles, Tools or Brewing entries | One meaningful utility is activated and its cost/target is observed | Corresponding Lexica category; optional breadth need not be a mandatory quest chain |
| 9. Defeat Gaia | Build the documented arena, perform the summon and complete the fight | Gaia Spirit/expected loot appears | Challenges and JEI Gaia recipe; combat test still required |
| 10. Optional port modules/addon | Follow Botania Extras, module and Extra Botany Revived entries only if selected in pack | Module-specific recipe or structure works | Modules and Botania Extras; check actual startup config and exact addon registry |
| Optional FE power bridge | Craft the Mana Fluxfield with four Livingrock, four Redstone Blocks and a Manasteel Ingot; bind a Spreader to it and place an FE receiver adjacent | Lexica RF Generator page documents the mana input/output role; source defines conversion, buffer and export limits | Mana → Mana Manipulation → RF Generator; JEI `botania:mana_fluxfield`; exact pack consumer/interoperability remains untested |

## Port-specific changes and pack risks

| Feature | Exact pinned behavior/evidence | TMM impact and status |
| --- | --- | --- |
| Unofficial port identity | Same `botania` namespace/mod ID; source and JAR describe altered NeoForge port | Original Botania is explicitly disabled in manifest; do not install both or substitute original addon builds without review |
| Wither Mana Rose | Port adds a two-step Nether Star-consuming generating flower; output/speed/cooldown/buffer configurable | Repeated Nether Stars become mana. Audit source of stars and configured output before recommending as a high-end loop. |
| Loonium | README says default mode drops dungeon/structure loot directly as items instead of spawning mobs; mana/cooldown/items/radius configurable | Potentially farms structure loot and can bypass normal loot acquisition. Verify 2.0.2 config defaults and all drop tables before treating it as a balanced quest reward path. |
| Asgard module | Enabled by default; Asgardandelion is documented at 1,000,000 mana per tick by default and can be disabled/configured | Very high-output mana source. Treat as a major progression/balance risk until exact recipe access, configuration, and throughput are reviewed. |
| Creative Crafting module | Enabled by default; Terrestrial Plate recipes reportedly use 500,000 or 1,000,000 mana for creative-only items; Re:Avaritia table integration is optional | Re:Avaritia is excluded and absent, but plate recipes remain; audit whether any creative output is obtainable through survival and how Asgard interacts with the mana burden. |
| Exploration module | Enabled by default; six Overworld structure sets using 28 templates; decorative structures | Interacts with Tectonic, worldgen density and structure loot; world-generation behavior not tested. |
| Extra Botany Revived addon | TMM pins `ExtraBotany-1.21.1-1.0.4-NeoForge.jar`; official 1.21.1 project is expressly built for Botania Revived and requires it | Metadata is more aligned than generic Botania addon builds. Co-load/server startup does not verify its recipes, bosses, structures, Mini Handbag, or player-facing data. Separate full profile needed. |
| Sophisticated optional module | Botania Revived has optional Sophisticated integration classes/modules | TMM's hard rule excludes Sophisticated Core/Storage/Backpacks; those optional mods are absent. Do not add them as a dependency or rely on module features. |
| Mana Fluxfield | Native core block `botania:mana_fluxfield`, not listed among optional modules | Direct Mana-to-NeoForge-Energy capability route; ratio/capacity and tick export are source-backed, client and consumer pairing untested |
| Ars bridge | Optional but active by default when Ars Nouveau exists, per port source. Exact pair meets declared versions: Botania requires Ars `>=5.13.0`; TMM pins Ars 5.13.5. | See [Botania × Ars interaction report](../interactions/botania-revived--ars-nouveau--mana-bridge.md). Mana conversion and player-cost bridging may substantially change relative progression; not in-world tested. |

## Dependencies and compatibility

- Exact JAR requires Minecraft 1.21.1, NeoForge 21.1.240+, Patchouli 1.21.1-93+. TMM's NeoForge 21.1.265 and Patchouli 93 satisfy these declarations.
- Optional Curios minimum is 9.5.1+1.21.1 and TMM pins 9.5.1. Optional KubeJS minimum is build.368, TMM pins build.381; optional Ars minimum 5.13.0, TMM pins 5.13.5. JEI minimum 19.27.0.340; TMM pins stable 19.64.0.455. These exact declarations establish version floors, not pairwise client behavior.
- Botania Revived's package internals differ from upstream original Botania; official addons may fail or be incompatible even though namespace/item IDs are preserved. Extra Botany Revived's own project says it is specifically built for this port and requires Botania Revived 1.0.3+; TMM pins 1.0.4 and Botania Revived 2.0.2.
- No confirmed server-startup incompatibility: Botania Revived and its selected dependencies were included in the B8 roster server boot. That does not test recipe registration, Lexica screen, client renderers, world generation, recipes, or mana capability transfer.
- **No Botania config override found in tracked `config/` or `defaultconfigs/`.** Module and integration behavior should therefore be audited using actual defaults before a test-build recipe or progression is authored.
- No Sophisticated mods or Create are installed. Optional compatibility metadata is not authorization to introduce them.

## Disposable-world verification plan

| Test | Positive and boundary cases | Expected observable | Performed? |
| --- | --- | --- | --- |
| Lexica and guide | Craft from sapling/book, trigger category advancements, read module pages with enabled/disabled config | Correct guide opens and reveals only relevant pages | no |
| Core mana loop | Pure Daisy with valid/invalid blocks; Endoflame with fuel/no fuel; flower/spreader/pool bindings and several distances | Correct block conversion, burst, loss and pool value are visible | no |
| Runic / Terrasteel | Correct and missing ingredient, valid/invalid multiblock, low mana and sufficient mana | Recipe accepts mana only when valid; expected output returns and mana is accounted | no |
| Alfheim / Gaia | Trade valid/invalid items, ritual requirements, solo and multiplayer boss run | Expected trade/drop; safe and repeatable battle and reward behavior | no |
| Asgard / creative | Check module registration, craft full Asgardandelion path, measure mana rate; attempt creative output with and without Asgard enabled | Real output aligns with config and available resources; creative objects remain inaccessible unless deliberately intended | no |
| Loonium | Direct loot mode and MOBS mode, compare supported structure tables, mana cost, cooldown and duplicate rolls | Intended mode and table/rate are clear; no unintended gated loot duplication | no |
| Worldgen | New chunks under Tectonic with Botania Exploration on/off | Structure density and placement are acceptable and reproducible | no |
| Botania × Ars | At exact pair, transfer source/mana both directions; test Mana Burst receiving, casting deficit, Botania-item cost, overflow and cookie regeneration | Resource units, remainder, consumption and HUD synchronization match documented ratios | no |
| Mana Fluxfield FE bridge | Fill via a normal Mana Burst; use adjacent receiver that accepts 0, partial and all offered FE; test near-full/full buffer and all six faces | Confirm 10 FE/Mana, 12,800 FE cap, ≤1,600 FE/tick export across neighbors, retained unaccepted output, near-full mana handling and whether selected TMM FE consumers accept it | no — source only |
| Extra Botany Revived | Representative flower, recipe, boss and Mini Handbag path; compatible with Curios/JEI | Addon registration and its guide/loot/recipe behavior are verified at 1.0.4 | no |

## Evidence ledger

| Claim | Exact source | What it establishes / limit |
| --- | --- | --- |
| Pinned artifact | `mods/botania-revived.pw.toml`; CurseForge 1725641/9577651; exact JAR SHA-1 above | Hash-matched direct inspection of the selected bytes |
| Version-aligned source | [commit `5f803227`](https://github.com/Velron-Devs/Botania-Revived/tree/5f803227d484598da1f1093a91d5d7cf5cf9600d), exact `gradle.properties` and README | Source declares same 1.21.1 NeoForge 2.0.2 release; port docs and source behavior |
| Guidebook/access and scope | Pinned JAR `assets/botania/patchouli_books/lexicon/**`, `data/botania/recipe/lexicon.json` and `assets/botania/lang/en_us.json` | Lexica recipe, categories, entries, page references and translations; not UI rendering |
| Recipe/loot/worldgen totals | Exact JAR `data/botania/recipe/**` (1,703), `loot_table/**` (1,056), `worldgen/**` (38) | Resource counts and exact item/rule data; not all individual systems gameplay-tested |
| Dependency floors | Exact JAR `META-INF/neoforge.mods.toml` | Required Patchouli, optional dependencies and versions |
| Port modules and configuration | Official pinned source `README.md`, `BotaniaModules`, `BotaniaConfigSpec`, module implementations | Module defaults, registered content and intended effects; TMM's active values not verified in a running client |
| TMM co-load and configs | `docs/COMPATIBILITY.md`, `manifest/mods.json`, `mods/*.pw.toml`, tracked config tree | Server startup pass, selected dependencies and no tracked Botania config override; not feature compatibility |
| Mana Fluxfield | [`PowerGeneratorBlockEntity.java`](https://github.com/Velron-Devs/Botania-Revived/blob/5f803227d484598da1f1093a91d5d7cf5cf9600d/src/main/java/velrondevs/botania/common/block/block_entity/mana/PowerGeneratorBlockEntity.java), [`XplatImpl.transferEnergyToNeighbors`](https://github.com/Velron-Devs/Botania-Revived/blob/5f803227d484598da1f1093a91d5d7cf5cf9600d/src/main/java/velrondevs/botania/xplat/XplatImpl.java), recipe and Lexica entry at source commit `5f803227d484598da1f1093a91d5d7cf5cf9600d` | [source] Proves 10 FE/Mana, 12,800 FE buffer, ≤1,600 FE/tick export attempt over adjacent FE capabilities, and recipe/guide placement; no measured transfer or TMM consumer test. |
| Ars integration | Exact port source `integration/arsnouveau/**` and JAWR-independent Ars pinned metadata | Code-backed capability mapping, default ratios and player-cost hooks; no exact-pair runtime observation |
| Extra Botany relationship | `mods/extra-botany-revived.pw.toml`; official addon page and dependency metadata | Exact addon pin and declared Botania Revived dependency; does not prove complete feature behavior |

## Unknowns and next research

- [ ] Audit every mandatory Petal Apothecary, Runic Altar, Mana Infusion, Elven Trade and Terrestrial Agglomeration dependency graph against actual JEI; this profile covers the path but not every recipe line.
- [ ] Confirm module config defaults in the actual TMM client, especially Asgard, Creative Crafting, Loonium and Exploration, and decide whether any need an owner-authorized settings change later.
- [ ] Measure flower rates, spreader loss and Ars conversion at exact versions; do not infer throughput from source formulas.
- [ ] Complete separate Extra Botany Revived 1.0.4 profile and review its bosses, relics, Baubles, flower inputs, worldgen and Mini Handbag.
- [ ] Verify Gaia Guardian summon arena, combat phases and reward table in a disposable client world.
- [ ] Reconcile the authored TMM quest chapter against all mandatory setup stages only in the later quest pass; leave it untouched now.

## Revision notes

- 2026-11-15 — Added exact-artifact/source profile for Botania Revived 2.0.2. Mapped the 13-category/291-entry Lexica, core mana progression, addon/module defaults and Ars Nouveau bridge. Flagged high-output Asgard, creative crafting, Loonium and structure/worldgen questions; none are claimed tested. No quest or runtime files changed.
