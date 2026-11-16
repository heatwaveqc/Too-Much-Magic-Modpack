# Embers Re-Ignited — mechanics and gameplay profile

| Field | Value |
| --- | --- |
| Manifest ID / actual mod ID | `embers-re-ignited` / `embers` |
| Category / parent or addons | magic-industry; forward port of Embers Rekindled |
| MC / loader / pack target | 1.21.1 / NeoForge / TMM `test/full-integration` |
| Pinned version / artifact / hash | 1.21.1-1.5.9; `embers-reignited-1.21.1-1.5.9.jar`; SHA-512 `7e0f7b72f6c3516f28f3fc4b7539088c1ae320d557a0c80fb01dd80d5479ce504f4ff2a8bedf2a8f90bae9d8445baabe0fd887aef4afd61efa2d239fffcbf361`; Modrinth version `2r51o0lC` |
| Source snapshot | [Upstream commit `82c263a2af72ef320ef2593a942586424f0026cf`](https://github.com/Riieno/EmbersRekindled-Reignited/tree/82c263a2af72ef320ef2593a942586424f0026cf); `gradle.properties` declares `1.21.1-1.5.9`. Binary equivalence not tested. |
| Guide/documentation version gap | The upstream wiki's Complete Guide says it was edited 2026-06-23; current pin 1.5.9 was released 2026-10-11. Release notes are bug fixes, but this does not prove the guide is fully current. |
| Profile status | researching |
| Evidence reached | exact packwiz pin; version-aligned upstream source metadata; official port wiki/project page; no client test |
| Reviewed | 2026-11-16 |
| Quest ownership | No quest edits in this research pass |

## Executive gameplay map

**What the player actually does:** Embers Re-Ignited is a dwarven magic-tech workshop built around Ember, its own power system rather than Forge Energy. The player finds an Ancient Golem, uses its drops to craft the Ancient Codex, surveys an Ember field, and builds an Ember Bore near bedrock. The Bore consumes ordinary fuel and produces Ember material; an Activator converts that material into Ember. A Tinker's Hammer links packet emitters and receptors so Ember can charge a cell and run machines. The first processing chain is a Melter, Stamp Base and Stamper for turning metal items into molten metal and shaped parts. Later systems include stronger Ember generation, metal alloying, steam and heat, Exchange Tablet alchemy, anvils, forgery, crystal seeds and energy/transport devices. The in-game Codex has progression entries; the source config defaults to requiring entries to be completed in order. [docs: upstream wiki; source: ConfigManager.java]

**Why it matters in TMM:** It combines resource extraction, its own power network, processing, alchemy, transport and gear work. It may also generate Forge Energy through the port's Ember-to-FE dynamo. This creates two policy questions for TMM: Embers includes its own general item/fluid pipe transport, and it exposes Ember-to-FE conversion. The pack's hard baseline excludes generic logistics and generic FE-transfer mods. This profile records the overlap for design review; it does not change the pack or infer that an internal Embers block violates the decision.

**Quick start:** Find an Ancient Golem and make the Ancient Codex; then use the Field Chart to choose an Ember Bore site. The official starter walkthrough places the Bore so its blades contact at least three bedrock blocks, burns ordinary fuel, feeds Bore output into a two-block Activator, links an Emitter to a Receptor with the Tinker's Hammer and stores the result in a Copper Cell. The Codex/wiki guide and JEI provide exact recipes; recipe ingredients can vary by pack.

### Player-value snapshot

| Five standout capabilities | Three genuinely early practical unlocks | Three candidate integrations |
| --- | --- | --- |
| Distinct Ember power network; bedrock-adjacent Ember extraction; metal melting and stamping; Exchange Tablet alchemy; Ember-powered transport and tools | Codex/lookup progression; Bore-to-Activator Ember supply; first Melter/Stamping line | Ember-to-FE dynamo (energy-gate audit); TMM ore/material recipe policy; native item/fluid transport vs logistics restriction |

### Subsystem coverage at a glance

| Subsystem / progression branch | Mechanics evidence | Progression completeness | In-game guide/JEI reference | Client validation | Blocker / next action |
| --- | --- | --- | --- | --- | --- |
| Ancient Codex and progression entries | [source: 82c263a] | partial | Ancient Codex; source says entries unlock in sequence | not run | Enumerate actual entry graph and first recipe |
| Ember survey, Bore, Activator | [official guide](https://github.com/Riieno/EmbersRekindled-Reignited/wiki) + source config | strong walkthrough, pin not checked | Getting Started; First Ember Setup; Ember Generation | not run | Confirm Bore structure and output in pinned client |
| Ember transport/storage | [official guide](https://github.com/Riieno/EmbersRekindled-Reignited/wiki) | strong | Ember Transport and Storage | not run | Verify link behavior, packet loss and TMM performance |
| Item/fluid transport | [official guide](https://github.com/Riieno/EmbersRekindled-Reignited/wiki) | partial | Item Transport; Fluid Transport and Storage | not run | Assess pack policy; inspect recipes and filtering |
| Metalworking | [official guide](https://github.com/Riieno/EmbersRekindled-Reignited/wiki) | partial | Metalworking | not run | Map JEI recipe/cost coverage and separator byproducts |
| Heat, water, steam | [official guide](https://github.com/Riieno/EmbersRekindled-Reignited/wiki) | partial | Heat, Water, and Steam | not run | Verify explosion boundary and all heat-source recipes |
| Alchemy and forging | [official guide](https://github.com/Riieno/EmbersRekindled-Reignited/wiki); source config | partial | Alchemy and Forging | not run | Confirm randomized aspectus and failure consumption in pin |
| Seeds, upgrades, combat/energy extensions | [official project page](https://modrinth.com/mod/embers-re-ignited); wiki | partial | Seeds, Upgrades, and Workshop Blocks; other sections | not run | Inventory every branch and assess FE conversion |

## Guidebook onboarding — required before the player's first complex step

- **Guide name and access:** Ancient Codex. The project page says to find and defeat an Ancient Golem, then use its drops to craft the Codex. Exact drop set and recipe ingredients should be confirmed against the pinned JAR/JEI.
- **Earliest attainable stage:** Golem encounter and drops; exact spawn locations/conditions are not mapped here.
- **First instructional quest:** Not authored. A future lesson should guide the player to find the golem, craft/open the Codex, then complete its first entry before directing them to the Field Chart.
- **Reference map:** Upstream wiki headings include Getting Started, Understanding Embers, First Ember Setup, Ember Generation, Ember Transport and Storage, Item Transport, Fluid Transport and Storage, Metalworking, Advanced Ember Generation, Heat, Water, and Steam, Alchemy and Forging, Seeds, Upgrades, and Workshop Blocks, and Forge Energy/Create/Aeronautics/Sable. These are online wiki section names, not verified in-game Codex page titles.
- **Where the Codex gates content:** Upstream config defaults `codexProgression=true`, described as requiring Codex entries to be completed before the next entry unlocks. The full entry order and whether it locks recipes or only Codex pages need exact-source/runtime confirmation.
- **Other in-game help:** The official wiki says recipe ingredients can change by modpack/datapack and directs users to JEI for exact recipes. The same wiki is explicit that it is a forward-port guide and old Embers explanations can mislead.

## Systems and mechanics inventory

| Subsystem | How accessed | Inputs + costs | Effects / outputs | Limits, failure conditions and source |
| --- | --- | --- | --- | --- |
| Ancient Codex | Loot from Ancient Golem, then crafting | Golem drops; exact ingredients not listed here | In-game Ember craft notes and progression entries | Entry order enabled by default in source config; exact pin config unknown |
| Field Chart | Crafted item/use | No resource cost documented | Reads Ember richness below a candidate location; distinguishes current reading and base field | Exact range and generation formula not established |
| Ember Bore | Place low in world with blades contacting bedrock; add ordinary fuel | Fuel consumed; source config default 3 fuel units/tick and 200 ticks per dig attempt | Produces Ember material and eligible excavation outputs, not stored Ember | Wiki recommends contact with at least three bedrock blocks. Output slots must remain clear or be extracted. Exact recipe outputs/biome modifiers not audited |
| Ember Activator | Two-block machine | Ember crystals/shards/grit or another valid activation material in lower block | Converts Ember material into stored Ember in upper block | Need output capacity; exact conversion values not audited |
| Ember packet network | Emitter/Receptor and Hammer linking; relays, mirror relays, splitters, ejector, funnel | Stored Ember; link endpoints and redstone behavior depend on block | Transfers Ember to machines/cells | Emitter pulls up to 10 Ember/tick and sends packets up to 40 per guide; Ejector up to 100/tick and 400 per packet. Receptor can lose packet overflow. Values are upstream wiki data, not pin-verified |
| Ember cells and vessels | Copper Cell, Crystal Cell, Ember Jar/Cartridge/Bulb and charger | Ember; crystals for Crystal Cell growth | Buffers or portable Ember storage | Wiki gives Crystal Cell 64,000 initial to 1,440,000 maximum; exact upgrade mechanics/config not pin-verified |
| Item/fluid transport | Pipes, extractors, transfers, vacuum, bins, reservoirs, vessels | Items or fluids; extractors need redstone by default per guide | Internal routing/filtering/storage | This is broad in-mod logistics functionality; compatibility with TMM design boundary should be reviewed |
| Metalworking | Melter → molten output → Stamp Base + Stamper; Mixer Centrifuge for alloys | Metal items and Ember; molten fluids and stamped items; exact recipe ratios via JEI | Molten metals, shaped metal parts, alloy outputs; separator may catch byproducts | Wiki defaults: Melter 200 ticks at 1 Ember/t; Stamper 80 Ember per 70-tick press; Stamp Base 1,500 mB. Input faces and tank capacity matter |
| Pressure Refinery / Ignem Reactor | Upgrade generation beyond Activator | Ember material; Refinery also water (25 mB per documented process); Reactor accepts catalyst and combustion inputs | More Ember than basic activation | Exact recipe and output economics need audit; unknown unsupported inputs |
| Hearth Coil / heatwork | Multiblock with Ember input; insulation/exchanger/bellows upgrades | Ember and cookable item; default 1 Ember/t in wiki | Heat and cooking; transfers heat between supported machines | Wiki defaults: max heat 280, heating/cooling 1 per tick. Insulation and pulsed redstone can retain heat |
| Water/steam | Mechanical Pump, Mini Boiler, reservoir, Wildfire Stirling | Water; Ember for Pump; heat; steam or Dwarven Gas | Steam storage and mechanical work | Wiki lists Mini Boiler explosion risk if gas overfills; source config defaults `mini_boiler.can_explode=true`. Keep gas output clear |
| Exchange Tablet alchemy | Tablet with arranged pedestals; charged Beam Cannon pointed at tablet and pulsed | Central item, aspectus/side ingredients, Ember charge; exact recipe via JEI | Alchemical exchange result; optional Mnemonic Inscriber records a successful exchange to paper | Source config defaults aspectus randomization=true and consume pedestal items on failed attempt=true. If failure-consumption config is false, the source description says the tablet item alone is consumed/changed to failure result |
| Dawnstone Anvil / Automatic Hammer / Inferno Forge | Hammer work, redstone-driven automation, or multiblock forge | Item plus recipe material/augment; Ember for automatic/forge work | Repairs/augments gear or completes high-heat forge recipes | Wiki defaults: 40 hammer hits maximum; Automatic Hammer stores 12,000 Ember, spends 40/action each 20 ticks; Inferno Forge uses 16 Ember/t for 200 ticks |
| Crystal Seeds and Injector | Seed with Injector adjacent | Ember | Grows supported metal crystals, including copper, iron, gold, lead, silver, dawnstone and mithril | Default injector max distance one block; output/economics and seed tier access unverified |
| Port additions | Features listed by port author | Ember, Create rotation where applicable, or stored Ember for FE dynamo | Create SU/Blaze Burner support, Ember-to-FE dynamo, new turngear/sightstone controls, metal seeds and merged tools | TMM hard-excludes Create; Create-dependent features cannot be treated as usable in this pack. Ember-to-FE block is a potential progression/balance interaction, not yet audited |

The numeric defaults in this inventory come from the upstream wiki, which does not encode its target mod version. Source-level configuration confirms several consequential toggles/defaults, including `codexProgression=true`, `alchemy.randomize_aspectus=true`, `alchemy.consume_items_on_failure=true`, `redstone.inverted_controls=false` and `mini_boiler.can_explode=true`. They are upstream defaults, not a checked TMM config.

## Material / capability input-output contracts

| System | Input kind + exact ID/tag/event | Input qty/formula; consumed/remainder | Output kind, qty and location | Trigger/cadence; range/capacity | Automation/interface and evidence |
| --- | --- | --- | --- | --- | --- |
| Ember Bore | fuel item + world/bedrock position; exact IDs are recipe/tag-dependent | source config: 3 fuel units/tick; nominal 200-tick attempt | Ember material and possible eligible excavation output to Bore inventory | each dig attempt; rate modified by upgrades/config | machine inventory/extractor; source `EmberBoreBlockEntity` and ConfigManager; wiki setup not exact-pin tested |
| Activator | Ember material item | exact per-item Ember value unknown | stored Ember capability in upper block | recipe/material-dependent | direct Ember capability, Emitter/Receptor; automation not client-tested |
| Packet transport | Ember capability in source block | packet/rate varies by transporter | Ember capability in destination or loss on overflow | Emitter/Ejector caps from wiki; world path | Hammer linking; redstone default signal mode |
| Melter | item stack + Ember | wiki says 1 Ember/t for 200 ticks; recipe material amount unknown | molten fluid in output tank | recipe and tank capacity | fluid pipe/extractor; face-specific |
| Stamper | molten fluid + stamp + Ember | wiki says 80 Ember/operation over 70 ticks; exact fluid quantity in JEI | shaped item output | one recipe operation | Stamp Base receives fluid; Stamper above |
| Exchange Tablet | central item + pedestal items/aspectus + Ember beam | recipe-defined; failure may consume pedestal contents per default | recipe result or failure result | beam/redstone event, then processing | manual or Beam Cannon; exact timing/output handling not pinned |
| Mini Boiler | water fluid + heat | recipe/cadence not stated here | steam gas | tank limited (guide says 16,000 mB) | fluid pipe and extractor; overfill explosion default enabled |
| Ember-to-FE dynamo | Ember input | conversion rate unknown | Forge Energy | block-specific | native output; exact capability/value and current TMM use not audited |

### Acquisition, renewability and output economics

- Ember generation begins with world Ember availability, a Bore, ordinary fuel and a converter (Activator, Pressure Refinery or Ignem Reactor). The system is not free power: fuel is continuously consumed and extracted material must be processed.
- The Bore's field/biome output table, excavation-bucket drop probabilities and depletion/regeneration behavior are not yet source-mapped.
- The guide recommends output buffers between generation and use. Ember packet overflow may be lost at a Receptor; full fluid or item outputs can stop machines.
- No recipe-by-recipe check has been made for duplicate ores, materials reserved for TMM gates, or the port's FE conversion ratio.

## Complete player progression / walkthrough blueprint

**Completion test:** Partially mapped. A player can follow the upstream guide from Codex to the first Ember cell and stamping line, but the exact in-game entry graph, each crafting recipe, all optional branches and the true terminal milestone are not reconciled with 1.5.9.

| Path and step | Gate or choice? | Exact prerequisite / materials / world condition | Player actions | Unlock / visible success / next step | In-game guide or JEI reference | Walkthrough coverage |
| --- | --- | --- | --- | --- | --- | --- |
| 0. Find/open guide | Initial access | Find and defeat an Ancient Golem; use its drops for Codex | Craft and open Codex; complete its first entry | Entry notes and next progression entry | Ancient Codex; exact entry names unverified | Partial; exact golem drops/craft need pin check |
| 1. Survey Ember | Early setup | Field Chart and reachable lower world | Read several candidate locations; prefer strong base Ember field | Select Bore site | Ember Generation → Field Chart | Source-documented |
| 2. Extract Ember material | Core gate | Bore, ordinary fuel, bedrock contacts, clear output | Place blades against at least three bedrock blocks, fuel Bore, collect/extract output | Ember crystals/material | Ember Generation → Ember Bore | Source-documented; exact recipe/biome table unknown |
| 3. Convert and store power | First functioning network | Activator, Ember fuel, Emitter, Receptor, Copper Cell, Hammer, redstone | Feed Activator; link Emitter to Receptor with Hammer; power Emitter; confirm cell fills | Stable Ember buffer | First Ember Setup | Step-by-step from wiki |
| 4. Shape metals | First processing branch | Melter, molten output route, Stamp Base, Stamper, matching stamp and Ember | Melt an item; pipe fluid into Base; place Stamper above; supply stamp and Ember | Plates/other stamped products | Metalworking; JEI exact recipes | Strong machine route; inputs/costs need exact pin |
| 5. Improve Ember yield/storage | Optional upgrade branch | Pressure Refinery or Ignem Reactor; water or catalysts/combustion ingredients as required | Build machine, supply inputs and route Ember into a larger cell | Higher-capacity or more efficient Ember supply | Advanced Ember Generation | Partial; all recipe choices unknown |
| 6. Route items/fluids | Workshop utility branch | Item/Fluid Pipes, Extractors, redstone, filters | Link source and destinations; orient faces; check with dials/goggles | Automated production chain | Item Transport; Fluid Transport and Storage | Documented, but TMM logistics policy review needed |
| 7. Heat and steam | Processing/kinetic branch | Hearth Coil, water pump, Boiler, heat, tanks; optional Create for kinetic components | Generate heat, pump water, route steam to Stirling | Cooking, steam and mechanical work | Heat, Water, and Steam | Create-only options excluded from TMM; standalone path remains partial |
| 8. Alchemy and forging | Advanced branch | Exchange Tablet, Pedestals, Beam Cannon, recipe items/aspectus, Ember; later Anvil/Forge | Set recipe layout from JEI, pulse cannon, record notes; forge/augment gear | Alchemical items and advanced equipment | Alchemy and Forging | Partial; failure and randomization settings matter |
| 9. Seeds, tools and port capstones | Optional/terminal branches | Crystal Seeds, Injector, upgrades, new beam/tools; FE dynamo | Grow metal crystals and use supported upgrades/weapon/energy features | Renewable material source or combat/energy tools | Seeds, Upgrades, and Workshop Blocks; port pages | Unresearched terminal objective and economics |

### Dependency graph and alternate routes

The source config exposes a linear Codex-entry gate, but it does not establish that every machine, recipe or item is locked behind the Codex. Machine progression appears to have branches: basic Ember generation and stamping; improved generation; transport; heat/steam; alchemy/forging; seeds/gear. Create-powered branches are optional integrations and are unavailable under TMM's explicit no-Create decision. The mod's Ember-to-FE dynamo is a distinct native conversion path and needs an energy-gate check.

### Structures, rituals, automation and other how-to instructions

| Required setup | Placement/shape/activation and operating inputs | Guidebook page/JEI category to cite by name | Failure symptoms or maintenance |
| --- | --- | --- | --- |
| First Bore line | Field Chart finds site; Bore low with at least three blade/bedrock contacts; ordinary fuel; clear/extracted outputs; pipe Ember material to lower Activator | First Ember Setup; Ember Generation | No output: weak/unread field, missing bedrock contact, fuel/output issue |
| Ember packet route | Emitter touches source, Receptor touches target; use Tinker's Hammer to link; redstone signal to Emitter by default | Ember Transport and Storage | Link alone does not span the terminal block gap; Receptor overflow can lose part of packet |
| Melter-to-Stamper | Feed item and Ember into Melter; route molten fluid to Stamp Base; correct stamp and Stamper above | Metalworking | Wrong face/recipe/capacity stalls work; inspect with dials/Jade |
| Steam line | Pump water with Ember; heat Mini Boiler; extract steam to vessel/Reservoir/Stirling | Heat, Water, and Steam | Full gas side can explode when config enabled |
| Alchemy Tablet | Central item on Tablet, aspectus/side items on Pedestals, charged Beam Cannon facing Tablet, redstone pulse | Alchemy and Forging; JEI | Failed attempts can consume pedestal items and tablet by default; random aspectus requirements can make world-specific layouts |

### Advanced and terminal objectives

- **Normal completion:** No explicit final boss or end-of-mod objective identified. The mod is an open-ended workshop progression.
- **Optional completion:** Advanced Ember sources, all metalworking, heat/steam, alchemy and forging, crystal seeds, augments, combat tools and FE conversion.
- **Remaining progression blockers:** Exact Codex entry graph; complete crafting and recipe families; Ore/field generation; current config; Bore output economics; all advanced machine structures; all port features; terminal milestone.
- **Quest coverage check:** Incomplete; no quest files changed.

## Internal combinations and overlooked mechanics

| Internal combination | Full chain | Benefit | Limitation / exploit potential | Evidence |
| --- | --- | --- | --- | --- |
| Ore extraction to metal parts | Bore/excavation → item extraction → Activator power → Melter → fluid route → Stamp Base/Stamper | Creates a self-powered metalworking chain | Exact ores, recipes, and throughput not audited | Upstream guide |
| Ember and storage network | Activator/refinery → Emitter → Receptor → Cell → Funnel/Ejector distribution | Buffers generation and powers multiple machines | Overflow and redstone controls can waste power | Upstream guide; source config |
| Alchemy notebook | Exchange Tablet + Beam Cannon + Pedestals + Mnemonic Inscriber | Records successful alchemy to notes for later use | Randomized aspectus and failed-input consumption make blind attempts costly | Upstream guide and ConfigManager |
| Heat retention | Hearth Coil + insulation + pulsed redstone | Stores heat between work bursts | Leaving it active every tick may waste Ember/heat | Upstream guide |
| Seeded metal production | Crystal Seed + adjacent Ember Injector | Grows supported metals from Ember supply | Access, rates, exact conversion costs unknown | Upstream guide and config |

## Low-hanging setups useful in ordinary survival

### 1. First Ember cell — early/mid

- **Goal and payoff:** Convert mined Ember material into a storable power buffer.
- **Minimum entry and ingredients:** Field Chart, Ember Bore, fuel, Ember Activator, Emitter, Receptor, Copper Cell, Tinker's Hammer and a redstone source.
- **Build/use steps:** Survey several candidate sites; place Bore near bedrock with at least three blade contacts; fuel and clear its outputs; put Activator nearby; feed its lower block; touch Emitter to the Activator upper block and Receptor to Copper Cell; link with Hammer and power Emitter; confirm the Cell fills.
- **Sustainability and limits:** Fuel and Ember material are recurring costs. Build output capacity and storage buffers.
- **Evidence / pack test:** Upstream 1.21.1 port guide; TMM client not tested.

### 2. Metal plate line — after first Ember

- **Goal and payoff:** Turn metal items into molten metal and stamped forms.
- **Minimum entry and ingredients:** Ember supply, Melter, fluid route, Stamp Base, Stamper and matching stamp.
- **Build/use steps:** Feed metal plus Ember into Melter; route molten output to Stamp Base; insert the recipe stamp; place Stamper above, feed Ember and take the output.
- **Sustainability and limits:** Costs Ember each cycle; output must be drained; exact fluid ratios come from JEI.
- **Evidence / pack test:** Upstream guide; exact recipe set and TMM client unverified.

### 3. Record alchemy — advanced

- **Goal and payoff:** Save successful Exchange Tablet recipes as Alchemy Notes.
- **Minimum entry and ingredients:** Tablet, pedestals, Beam Cannon, charged Ember, recipe ingredients, paper and Mnemonic Inscriber.
- **Build/use steps:** Arrange recipe as JEI shows; aim and pulse charged Cannon; successful work near an Inscriber with paper produces an Alchemy Note.
- **Sustainability and limits:** Failed attempts may consume pedestal items and Tablet under default config; aspectus may be randomized by world seed. Verify recipe before firing.
- **Evidence / pack test:** Upstream guide and source config; no TMM test.

## API, integration and pack-config surfaces

| Hook or customization surface | Namespace / exact file, API or serializer | Capabilities / constraints | Current TMM override? | Evidence |
| --- | --- | --- | --- | --- |
| Ore/world generation | `ConfigManager.OreGenerationMode` (`NEVER/ALWAYS/AUTO`) | AUTO disables Embers ore in a biome when another mod adds matching ore feature; config default not yet extracted | Unknown | source `ConfigManager.java` at 82c263a |
| Codex progression | `codexProgression` | Default true; each entry must be completed before next unlocks | Unknown | source `ConfigManager.java` |
| Alchemy | `alchemy.randomize_aspectus`, `alchemy.consume_items_on_failure` | Both default true; hidden aspectus requirements drawn from recipe pool; failed input consumption configurable | Unknown | source `ConfigManager.java` |
| Redstone | `redstone.inverted_controls` | Default false, so controlled blocks require signal | Unknown | source `ConfigManager.java` |
| Boiler | `mini_boiler.can_explode` | Default true for gas-side overfill explosions | Unknown | source `ConfigManager.java` |
| Recipe data | Ember recipe families include melting, stamping, mixing, boring, alchemy, forging and others | Exact input/output schemas and IDs not inventoried in this profile | No override established | source recipe registrations; wiki says use JEI |
| Optional integrations | Create; Sable; Forge Energy output | Create/Sable integrations documented by port, but Create is explicitly excluded from TMM; FE conversion behavior not audited | No changes | official Modrinth description / repo README |

## Compatibility leads

| Other mod / system | Candidate link and why | Native / pack-built / would require bridge | Status |
| --- | --- | --- | --- |
| Create | Port adds Create SU generation, speed-powered upgrades and Blaze Burner support | Native optional integration, but excluded from TMM | Unavailable by pack decision; do not teach or balance around it |
| TMM energy progression | Native Ember-to-FE dynamo can convert to Forge Energy | Native | Potential gate/leak review; exact rate and build cost unknown |
| TMM logistics rule | Embers' Item/Fluid Pipes, Extractors and Transfers form an in-mod logistics system | Native mod functionality | Policy overlap to evaluate; no new external logistics dependency introduced by this profile |
| Ore-processing mods | Melter/stamping routes may complement or bypass other doubling systems | Recipe overlap | Unverified; inspect exact recipes before balancing |

## Quest and teaching opportunities

- Teach Ancient Codex access and the exact item-drop route first; ensure the player actually reads its entry chain.
- Make the first Ember cell a built-and-confirmed setup, not merely possession of a Bore and Activator.
- Teach machine faces, linking, Hammer interaction and redstone separately from power generation.
- Teach Melter → Stamp Base → Stamper as a full fluid-processing operation and use JEI for the exact recipe.
- Put a warning before alchemy attempts if the live default still consumes pedestal items on failure and uses randomized aspectus.
- Keep Create-only instructions out of TMM chapters.

## Targeted verification and open questions

1. Inspect pinned JAR metadata and compare all in-game Codex entries/order to the source snapshot and online guide.
2. Verify Ancient Golem spawn conditions, loot outputs, exact Ancient Codex recipe, and whether Codex completion locks recipes or only later Codex pages.
3. Extract recipe schemas and key input/output values for Bore, Activator, Melter, stamping, alloying, separator, pressure refinery, reactor, boilers, Exchange Tablet, Anvil, Inferno Forge, seeds and FE dynamo.
4. Check Ember-field/ore generation under the pack's terrain and worldgen mods; inspect `AUTO` behavior.
5. Confirm TMM config values, Bore dig speed/fuel, packet overflow loss, and all redstone inversion behavior.
6. Verify failed alchemy consumption and aspectus randomization in a disposable world, including invalid recipe and insufficient Ember cases.
7. Audit item/fluid transport functionality against the pack's no-generic-logistics rule and assess FE output against progression gates.
8. Test one full starter chain and one advanced branch in the 1.21.1 TMM client; do not call the wiki values pack-tested.

## Evidence ledger

- **[pin]** `mods/embers-re-ignited.pw.toml` on `test/full-integration`: Modrinth project `8znVCTxA`, version `2r51o0lC`, file `embers-reignited-1.21.1-1.5.9.jar`, SHA-512 above.
- **[release metadata]** [Modrinth version 1.21.1-1.5.9](https://modrinth.com/mod/embers-re-ignited/version/2r51o0lC): Release, Minecraft 1.21.1, NeoForge, client/server; changelog lists funnel crash fix, duplicate tooltip fix, fluid-vessel content preservation and Motiv Core tool support.
- **[source: 82c263a]** [Upstream source snapshot](https://github.com/Riieno/EmbersRekindled-Reignited/tree/82c263a2af72ef320ef2593a942586424f0026cf): Gradle metadata declares mod `1.21.1-1.5.9`; binary equivalence not established.
- **[source: 82c263a]** [ConfigManager.java](https://github.com/Riieno/EmbersRekindled-Reignited/blob/82c263a2af72ef320ef2593a942586424f0026cf/src/main/java/com/rekindled/embers/ConfigManager.java): Codex progression, aspectus randomization, failure consumption, redstone, boiler and other config defaults.
- **[docs: wiki edited 2026-06-23]** [Embers Re-Ignited Complete Guide](https://github.com/Riieno/EmbersRekindled-Reignited/wiki): tutorial sequence, machine operation, layouts, defaults and named guide sections; recipe-specific accuracy varies with modpack/datapack; current pin is later.
- **[docs: current project page]** [Modrinth project](https://modrinth.com/mod/embers-re-ignited): states this is a forward port, gives Ancient Golem/Codex onboarding, lists Create/Sable/FE additions and warns that the project is still actively developed/beta despite the version file's Release label.
- **[pack-test]** No client or world test run; no runtime or quest files changed.

**Method note:** Embers has excellent practical upstream machine instructions but version-sensitive recipes, optional integrations and in-game Codex gating. Keep wiki defaults as documented upstream until checked against TMM's 1.5.9 artifact/config.
