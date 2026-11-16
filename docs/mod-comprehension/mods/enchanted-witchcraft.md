# Enchanted: Witchcraft — mechanics and gameplay profile

| Field | Value |
| --- | --- |
| Manifest ID / actual mod ID | `enchanted-witchcraft` / `enchanted` |
| Category / parent or addons | magic; standalone profile |
| MC / loader / pack target | 1.21.1 / NeoForge / TMM `test/full-integration` |
| Pinned version / artifact / hash | `enchanted-neoforge-1.21.1-4.2.7.jar`; CurseForge file 8055168; SHA-1 `2380cb5a49c7eb86afa6f790a920d1872d5cb85e` [pin] |
| Source snapshot | upstream `Favouriteless/Enchanted`, branch `1.21.1`, commit [`55f37c7e15a2848be2cf25ff20232a1893795e75`](https://github.com/Favouriteless/Enchanted/tree/55f37c7e15a2848be2cf25ff20232a1893795e75); source declares 4.2.7, but pinned JAR binary equivalence is not established |
| Guide/documentation version gap | Source-generated Modopedia content at the version-aligned source snapshot; exact JAR contents still need comparison |
| Profile status | researching |
| Evidence reached | pin metadata plus upstream code, recipes, loot/worldgen resources and generated in-game handbook; no exact JAR inspection or client test |
| Reviewed | 2026-11-16 |
| Quest ownership | Existing placeholder chapter in `manifest/quest_chapters.json`; no quest edits in this profile |

## Executive gameplay map

**What the player actually does:** Enchanted is a broad, mostly non-linear witchcraft toolkit, not a spell level tree. The player crafts the *Art of Witchcraft* book, gathers witch plants and mob drops, uses a Witch's Oven to extract fumes and byproducts, then combines those resources in Kettles, the Distillery, a Spinning Wheel, poppets and chalk rites. The altar is the shared power source for several machines and circle magic. Herbology, brewing, poppetry, familiars, mobility, protection, curses, transposition and world-altering rites are branches; the handbook does not define a single required final boss or mastery gate. [source: upstream commit above]

**Why it matters in TMM:** The mod offers ingredient-based utility and persistent defensive/transport effects rather than a conventional projectile-spell school. Its handbook explicitly describes a witchcraft practice built from everyday objects. The strongest integration value is therefore specific recipes, taglocks, altar power, plants, entity effects and world interactions; no direct bridge to another TMM magic system is established here.

**Quick start:** Craft the *Art of Witchcraft* from a book, feather, ink sac and Belladonna Seeds. Use its *Material Processing* and *Herbology* chapters to begin gathering ingredients. Craft the Witch's Oven and clay jars, collect fume recipes from basic wood inputs, then make the altar ingredients shown in JEI. Build a 2×3 altar, surround it with varied plants, and use *Altars* and *Circle Magic → Fundamental Theory* to branch into powered tools and rites.

### Player-value snapshot

| Five standout capabilities | Three genuinely early practical unlocks | Three candidate integrations |
| --- | --- | --- |
| Ordered, recipe-driven brews and plant mutation | Witch's Oven turns common burnable inputs into useful fumes/byproducts; jars make outputs storable | Blood Poppy/taglock interactions with TMM blood factions (hypothesis; verify item semantics first) |
| Altar-powered distillation, spinning and rites | Kettle brews from ordered ingredients over heat; wrong inputs can spoil a batch | Witch-oven outputs as ingredient sources for other mods' crafting (recipe compatibility audit needed) |
| Sympathetic poppets that protect or affect a bound target | Mutandis lets players pursue otherwise unavailable witch plants | Altar-powered Distillery/Spinning Wheel alongside other machine systems (no native FE conversion found) |
| Familiar bonuses and remote summoning | Earmuffs reduce danger when harvesting screaming Mandrakes | Entity targeting / faction protections across Enchanted and JAWR, Vampirism, Werewolves, Goety or Iron's (boundary tests needed) |
| Flight via an infused broom; location and entity transposition | Broom sweeps up chalk glyphs as well as serving as a possible transport item | No direct integration profile exists yet |

### Subsystem coverage at a glance

| Subsystem / progression branch | Mechanics evidence | Progression completeness | In-game guide/JEI reference | Client validation | Blocker / next action |
| --- | --- | --- | --- | --- | --- |
| Handbook and initial tools | book recipe and Modopedia book definition [source] | partial | *Art of Witchcraft*; *Getting Started* | not run | Compare exact pinned JAR and verify access in client |
| Witch's Oven and fumes | generated pages, recipes, oven entity [source] | partial | *Material Processing → Fume Collection* | not run | Validate output amounts, burn times, slot and funnel/filter behavior |
| Herbology and mutations | handbook pages, crops, recipes and worldgen resources [source] | partial | *Herbology*; *Mutated Plants* | not run | Map all mutation outcomes and plant sourcing in exact artifact |
| Kettle brewing | handbook, recipe data and block entity [source] | partial | *Brewing → Creating Brews*; JEI kettle category | not run | Confirm exact recipe list, timing and failure costs |
| Altar and power | altar guide, recipe, power consumer/provider code [source] | partial | *Getting Started → Altars* | not run | Confirm complete 2×3 validation, range/recharge and upgrades |
| Distillery and spinning | handbook, recipes and block entities [source] | partial | *Material Processing → Distillation*; *Poppetry → Spinning* | not run | Inspect per-recipe costs, automation and outputs |
| Poppets, taglocks and curses | handbook categories, recipe set, curse code [source] | partial | *Poppetry*; *Circle Magic → Curses* | not run | Full bind/trigger/consumption matrix and death/ownership edge cases |
| Circle magic and rites | handbook has rite pages and rite data [source] | partial | *Circle Magic → Fundamental Theory* and rite-specific pages | not run | Transcribe each rite's geometry, exact foci, cost and failure behavior |
| Familiars, broom, waystones | handbook entries and rite data [source] | partial | *Circle Magic → Binding / Creature / Transposition*; item entries | not run | Verify full familiar catalog and entity state/persistence |
| Config/API and pack behavior | upstream server config/source; TMM roster/pin [source]/[pin] | partial | config files and JEI | not run | Compare generated config and pack overrides; no runtime edits |

## Guidebook onboarding

- **Guide name and access:** The guide is *Art of Witchcraft*, a Modopedia book. The source recipe is shapeless: `minecraft:book`, `minecraft:feather`, `minecraft:ink_sac` and `enchanted:belladonna_seeds` → `modopedia:book` carrying book ID `enchanted:art_of_witchcraft`. [source: `data/enchanted/recipe/art_of_witchcraft.json`]
- **Earliest attainable stage:** The seed and book ingredients are ordinary early-world materials in the source recipe; exact seed acquisition should be checked in the pinned build. The handbook's Mandrake page says Mandrakes drop from tall or short grass; do not conflate that with Belladonna Seeds.
- **First instructional quest:** A future chapter should explicitly craft/open the handbook and point to *Getting Started*, *Material Processing* and *Herbology*. A handbook reward is an alternative only if the reward system doesn't bypass or duplicate intended acquisition.
- **Reference map:** *Getting Started*; *Getting Started → Altars*; *Herbology → Mutated Plants*; *Material Processing → Fume Collection / Distillation*; *Brewing → Creating Brews*; *Poppetry → Poppetry / Spinning*; *Circle Magic → Fundamental Theory / Binding / Creature / Curses / Transposition / World*. The *Arthana*, plant, machine, poppet and rite entries provide additional recipe and use references.
- **Where guide is insufficient:** The source handbook indexes known rites and gives a rite-specific information page, but it is not by itself a verified full survival walkthrough. It does not establish a single progression endpoint. Exact inputs, costs, ordering and failure penalties for every rite still need source/JAR review before quests can safely describe them.
- **Other in-game help:** JEI exposes ordinary crafting and custom Enchanted recipe types (Kettle, distilling, spinning, byproduct); cite the named category/item uses only after confirming the pack's JEI view. The handbook contains the principal construction and use references.

## Systems and full mechanics inventory

| Subsystem | How accessed | Inputs + costs | Effects / outputs | Limits, failure conditions and source |
| --- | --- | --- | --- | --- |
| Witch plants | Natural drops, crops, mutation or worldgen | Plant/seed items, bonemeal or mutagen recipes where supported | Ingredients for brewing, altar power, rites and poppets | Exact spawn and mutation tables need a full pass. Mandrakes scream when agitated, can injure or kill, sleep at night; earmuffs protect. Handbook: *Herbology* and plant entries. |
| Mutandis | Kettle recipe, then use on a plant block | Mutandis is applied to a plant; nearby blocks act as mutagens | After several minutes the plant may turn into another species | More mutagens raise the chance. Mutandis Extremis enables advanced mutations such as Blood Poppies. The outcome is probabilistic and environment-dependent; no exact odds asserted. |
| Witch's Oven | Craft an oven; fuel/burn a supported input with a clay jar in the middle slot | Burnable item in input, fuel, empty clay jar; recipes define byproducts/fumes | Byproduct and/or fume item collected in the jar/output slots | Not an ore furnace. Side funnels increase yield; up to two filters guarantee success according to the handbook. A third funnel on top is described as decorative chimney. Per-input output quantity and timing not established here. |
| Kettle | Craft and place over a heat source; fill with water | Bucket of water, ordered ingredients, fire/lava/campfire below | Recipe-specific infusion, brew or plant-mutating reagent | Wait for water to boil, add items one by one in recipe order, then let it boil for several seconds to finish. Wrong item or removal of heat may spoil brew; remove spoiled brew with an empty bucket. Server setting `kettle_item_spoil` defaults true in source. |
| Altar | Craft altar block using recipe and arrange six into a rectangular 2×3 platform | Six altar blocks; surrounding natural plants; optional typed upgrades | Recharging magical power accessible to tools/machines and rites | Variety of nearby plants is generally better. Certain blocks on the altar multiply effects; only the best upgrade per type counts. Exact power values/range need verification. |
| Attuned Stones | Craft a diamond infused with magic using its JEI recipe; charge via rite | Diamond plus rite-specific inputs; altar power or stored power, depending on operation | Portable altar-power store and remote circle-magic resource | Handbook says a charged stone can substitute for an altar during circle rites and is used in several tools. Amount/cost/capacity not established. |
| Distillery | Craft and place within altar-power range | Recipe-defined item inputs and altar power | Separates recipe inputs into listed component outputs | Power is consumed. Do not treat as generic ore processing or assume an output from viewer icons alone. Verify per-recipe input/output and automation. |
| Spinning Wheel | Craft and place within altar-power range | Recipe-defined material stacks plus altar power | Weaves materials together; used to add effects to poppets | Powered by an altar. The actual material chain and whether inputs are consumed per tick/per completion need exact inspection. |
| Poppets | Craft base doll, weave at Spinning Wheel, bind to a living target | Doll, effect materials, target taglock; altar power for weaving | A beneficial or harmful effect for the bound being, depending on type | Poppet must be in target's inventory or an active Poppet Shelf. A shelf holds up to four poppets belonging to any player. Full trigger list, persistence, charges and protection priority not yet mapped. |
| Taglocks | Craft Taglock Kit and use on player or their bed | Kit used on a player or bed to collect a sample | A sample that represents that player in magic | Handbook defines these two collection paths. Exact ownership/privacy edge cases and item reuse are not established. |
| Chalk rites | Draw heart glyph, then correct chalk glyph/circles and place focus items/entities before activating | Golden Chalk heart glyph; Ritual, Nether or Otherwhere Chalk circles; rite-specific foci; stored altar power | Effect depends on rite: protection, growth, curse, summoning, transport, etc. | The handbook describes order and heart glyph activation. Rite-specific geometries, range, consumption timing and failed-cast costs remain open. |
| Familiars | Tame a qualifying animal, then perform Bind Familiar; use Summon Familiar to call it | Tamed creature inside circle and rite-defined focus/power | Bound companion grants creature-specific benefits; can be recalled even when dismissed or dead per handbook | Specific species and bonuses remain to be inventoried. A familiar or coven increases curse skill. |
| Brooms | Craft ordinary broom; optionally infuse it through rite with Flying Ointment | Broom and rite-specific ointment/power | Sweeps away chalk glyphs; infused broom provides flight | Guide warns to practice at low altitude. Flight duration/control and fuel are not established here. |
| Waystones | Use binding rite at the target location/entity; another rite can duplicate an existing stone | Waystone, target and rite-specific power/focus | Location-bound or entity-bound token used by transposition/protection/lightning rites | Blooded waystones bind to an entity and cannot be duplicated; ordinary waystones can be duplicated according to handbook. Exact target rules need verification. |
| Curses and cleansing | Perform target-specific curse rite, then corresponding cleansing rite | Taglock/target, foci and rite-specific resources | Clumsy, Misfortune, Overheating or Sinking curse; corresponding cleanse | Curse strength increases with caster skill; cat familiar or coven helps. A failed cleansing worsens the curse; do not make this a casual early quest action. |
| World and creature rites | Learn from rite-specific pages and cast with required geometry/foci | Sacrifices, dropped items, targets, time/weather/environment and altar power vary by rite | E.g. Broiling cooks raw food dropped in a ring; Forest grows trees matching a sapling focus; Fertility grows plants/heals animals/cures zombified villagers; Transpose Iron surfaces iron ore while consuming the ore; protection/imprisonment create barriers | This is a broad optional branch, not one linear tier. Some rites can destroy inputs, alter large areas or have defensive gaps. Exact prerequisites and failure costs require the rite-by-rite pass. |

## Material / capability input-output contracts

| System | Input kind + exact ID/tag/event | Input qty/formula; consumed/remainder | Output kind, qty and location | Trigger/cadence; range/capacity | Automation/interface and evidence |
| --- | --- | --- | --- | --- | --- |
| Handbook | Item stacks: book, feather, ink sac, `enchanted:belladonna_seeds` | One of each; recipe is shapeless | Modopedia book item with `enchanted:art_of_witchcraft` data component | Crafting action | Recipe JSON in pinned-version-aligned source; binary not inspected |
| Mutandis | Plant block plus item use; surrounding world blocks are mutation candidates | Recipe-specific; application appears to start a timed mutation; exact consumption amount unverified | Mutated block in world | Minutes/tick process; mutagens in surrounding area alter chance | Native block behavior; exact code/recipe needed |
| Oven fume collection | Item input + fuel + clay jar; no fluid or FE channel established | Recipe-specific burnable stacks; exact input/output amounts unverified | Byproduct item and fumes caught by jar | Furnace-like processing; funnels/filters alter yield | Container slots expose item automation in source; filters/funnels are native upgrades |
| Kettle | Bucket/water state + ordered item stacks | Recipe-specific list and water; exact consumed amounts not reproduced here | Brew/reagent item; collection with bucket/bottle depends on recipe | Must be heated; ingredients are order-sensitive; seconds of boiling after final item | Custom recipe type/JEI; no claim of fluid-tank or FE API |
| Distillery / Spinning Wheel | Item stacks + altar-power capability | Recipe-specific item counts and altar power | Recipe-defined item stacks | Powered operation; precise tick/cost not established | Block-entity inventory suggests item automation; verify sided slots on exact build |
| Rites | Chalk placed as world blocks; item/entity/world-state foci; altar power or charged stone | Rite-specific; some inputs are entities or location/time predicates, not item recipes | World/entity/player state or spawned/changed blocks/items | Activation and duration are rite-specific | Recipe-like handbook displays describe rite variants; viewer icons do not prove all inputs are consumed |
| Player/mob state | Bound taglock, target entity/player, familiar and curse state | Targeting/identity; persistent costs and timers unverified | Curse, protection, teleportation, familiar relationship, etc. | Event, targeted activation or timed rite | Native server mechanic; team/faction compatibility is not established |
| FE, mana, XP, blood | No shared FE/mana/XP currency established by examined sources | Unestablished | Altar power is Enchanted-specific; do not equate it with FE or Source | Altar recharge and item storage | No native conversion/API bridge established in this pass |

### Acquisition, renewability and output economics

- Witch-oven inputs include ordinary wood/saplings, making the extractor a plausible renewable ingredient path; exact eligible item tags and yields must be transcribed from the recipe set.
- Fume jars and funnel/filter upgrades give the oven a yield-optimization branch. The guide guarantees success with two filters, but gives no precise output formula.
- Plant mutation is time-based and probabilistic rather than a deterministic recipe output. Use the handbook's mutation diagram and JEI Kettle recipe for Mutandis/Extremis.
- Altar power is supplied by nearby nature and rechargeable over time according to configuration; it is not shown to be FE. Attuned Stones provide portability but are charged through a rite.
- No EMC values, general ore doubling, passive FE output or automatic unlimited ritual loop are established by this source pass.

## Complete player progression / walkthrough blueprint

**Completion test:** Enchanted has no single mandatory campaign or terminal boss in the inspected handbook/source. The intended chapter should be a navigable set of practical branches, and it should not claim that acquiring every rite is required for progression.

| Path and step | Gate or choice? | Exact prerequisite / materials / world condition | Player actions: obtain, build, activate, use | Unlock / visible success / next step | In-game guide section or JEI reference | Proposed quest/how-to, detection and reward | Evidence / coverage |
| --- | --- | --- | --- | --- | --- | --- | --- |
| 0. Find/open guide | Required teaching step | Book, feather, ink sac, Belladonna Seeds | Craft and open *Art of Witchcraft* | Recipe and table of contents become available | *Getting Started*; handbook recipe | Teach actual craft/open. Detect book item only as proxy; reward small witchcraft supply after validating no early gate bypass | Recipe source; pin not binary-verified |
| 1. Gather witchcraft basics | Entry branch | Tall grass, ordinary plants/wood and common hostile/passive mobs | Collect Belladonna Seeds and plant ingredients; craft Arthana if pursuing rare mob drops; make earmuffs before disturbing Mandrakes | Unlock book access, safe harvesting, witch-specific drops | *Herbology*, *Getting Started*, *Arthana*, *Mandrakes*, *Earmuffs* | Teach seed and plant identification plus earmuff protection. Detect crafted gear or harvested plant item; manual task for survival behavior | Handbook/source partial |
| 2. Start material processing | Main practical branch | Iron for oven recipe; clay and fuel for jars; common burnable input | Craft Witch's Oven, soft clay jars and clay jars; fire a supported recipe; fit side funnels and two filters | Obtain fume/byproduct ingredients; see a jar fill and oven output | *Material Processing → Fume Collection*; JEI Byproduct | Teach a complete supported oven recipe and observe output; item output is a proxy unless FTB can detect operation | Recipe/pages; yields unverified |
| 3. Start brewing and mutations | Branch | Kettle recipe; water bucket; heat; valid ordered recipe ingredients | Craft Kettle; fill with water, heat from below, add recipe ingredients in order; make Mutandis and apply it to an eligible plant with mutagens nearby | Produce a brew/reagent and pursue a new witch plant | *Brewing → Creating Brews*; *Herbology → Mutated Plants*; JEI Kettle | Teach exact example recipe from verified pinned JEI; detect Mutandis or changed plant item/block, otherwise manual observation | Mechanics documented, recipes partial |
| 4. Establish the altar | Core power branch | Recipe inputs include rowan log, stone bricks, potion, Breath of the Goddess and Exhale of the Horned One | Produce fumes/byproducts; craft six altar blocks; arrange a 2×3 platform; build a varied plant garden around it | Altar structure/power is visible; supplies nearby powered equipment | *Getting Started → Altars*; JEI Altar | Teach six-block shape and compare power after adding varied plants. Item count does not prove structure; use manual task unless an exact structure detector exists | Handbook says six rectangular blocks; power formula open |
| 5. Upgrade and make power portable | Optional improvements | Valid altar; blocks of available upgrade types; diamond for Attuned Stone | Place the best available upgrade of each type atop altar; charge an Attuned Stone using its recipe/rite | Increased altar effect; portable power for a rite without an altar | *Altars* and *Attuned Stones* | Teach one upgrade and one charging demonstration. Detect item/altar only as proxy; verify altar range and charge conditions first | Partial |
| 6. Distill and weave | Optional powered machines | Altar power in range; Distillery and Spinning Wheel recipes; recipe inputs | Place machines within power; operate a JEI Distilling recipe; use Spinning Wheel to weave a material into a poppet | Component extraction and effect-bearing poppet | *Material Processing → Distillation*; *Poppetry → Spinning* | Teach one actual transformation at each machine. Item task is proxy; distinguish powered processing from possession | Partial |
| 7. Bind and use poppets | Optional defense/target branch | Crafted base poppet, woven effect material, Taglock Kit; target player or supported creature as relevant | Collect taglock via player or bed; use the corresponding binding recipe/rite; keep poppet in target inventory or place in shelf | Poppet's supported effect activates for its bound being | *Poppetry*, *Taglocks*, effect-specific poppet page, *Poppet Shelf* | Teach one safe protective poppet with clear target, shelf/inventory location and known effect. Avoid dangerous curse tests. Detect crafted/placed object only as proxy | Guide confirms general rules; exact binds and triggers open |
| 8. Learn circle magic fundamentals | Optional broad branch | Golden Chalk, a chalk type, focus items/entities and altar power or charged stone | Draw a heart glyph, the correct colored circle(s), place foci, then activate at heart glyph | Rite starts; effect appears or persisted state changes | *Circle Magic → Fundamental Theory / Performing Rites* plus exact rite page | Teach a low-risk rite only after its exact geometry, materials, cost and detector are reviewed | Shape/focus details partial |
| 9. Select rites and specialized branches | Optional, non-linear | Rite-specific; may need bound player/entity, foci, biome, weather, time, plants, sacrifice or waystone | Choose utility/safety, familiar, curse/cleanse, transposition or world rites; follow relevant page exactly | Targeted effect, familiar, location token, barrier, plant/ore/world change | *Binding*, *Creature*, *Curses*, *Transposition*, *World* | Organize quests by player value and risk, not lore order. Use manual completion for operation absent a real task detector | Rite index is source-backed; details remain open |
| 10. Mobility and familiar use | Optional | Broom; Flying Ointment and infusion rite; or tamed animal and Bind Familiar | Infuse broom; test safely near ground; bind familiar in a circle and use Summon Familiar as needed | Flight/chalk clearing or a creature-specific familiar benefit | *Brooms*, *Infuse Broom*, *Familiars*, rite-specific pages | Teach one option; detect infused broom or familiar item/state only if the task system truly sees it | Partial |
| 11. Advanced world/remote rites | Optional, potentially destructive | Exact rite focus, chalk, altar power, location/time/weather or sacrifice | Use remote protection/waystone targeting, world rites, or Transpose Iron only after explaining consumed ore and environmental effects | Persistent protection, transport, growth, crop/entity changes or exposed ore | *Waystones*, *World*, each rite page | Separate safe rites from area alteration and destructive ore consumption; boundary tests needed before quest rewards | Partial; not terminal completion |

### Dependency graph and alternate routes

This is a web of craft and material dependencies, not a native tier tree:

`guidebook → plant/mob ingredients → Witch's Oven / Kettle → fume ingredients and reagents → altar → altar-powered machines / chalk rites`

- The handbook is the primary discovery surface; JEI supplies recipe-specific materials and custom recipe categories.
- The altar is a shared power gate for Distillery, Spinning Wheel and most rites. A charged Attuned Stone is a portable alternative for circle magic; exact scope needs validation.
- Mutandis and Mutandis Extremis are alternate-depth plant mutation tools; advanced mutations include Blood Poppy.
- Poppetry, familiars, curses, waystones, brooms and world rites are optional branches. No irreversible faction selection or boss-gated tier was found in inspected source.
- Distinguish native configuration from TMM: upstream source exposes `disabled_rites`, altar range/recharge, kettle spoil, seed drop and totem options; no TMM override or quest progression change is asserted here.

### Structures, rituals and other how-to instructions

| Required setup | Placement/shape/activation and operating inputs | Guidebook page/JEI reference | Failure symptoms, throughput or maintenance | Quest where taught |
| --- | --- | --- | --- | --- |
| Witch's Oven | Burn a supported item with fuel; put a Clay Jar in the center slot; attach one funnel on either side; use filters on funnels | *Material Processing → Fume Collection*; JEI Byproduct | Not an ore furnace; two filters guarantee success in guide wording; exact yield/timing unknown | Material processing |
| Kettle | Fill with a water bucket; place fire/lava/campfire under it; wait for boil; add exact ordered ingredients one-by-one; allow final boil; collect result | *Brewing → Creating Brews*; JEI Kettle | Wrong ingredient or lost heat may spoil it; empty bucket removes spoil | First brew |
| Altar | Place six altar blocks as a rectangular 2×3 platform; add diverse plants nearby; place valid upgrades atop altar | *Getting Started → Altars* | Power is nature-dependent; only best upgrade of each type counts | Altar workshop |
| Distillery | Place in altar range and provide recipe items | *Material Processing → Distillation*; JEI Distilling | Drains altar power; exact range, cost and operation rate open | Processing branch |
| Spinning Wheel | Place in altar range; input exact recipe materials; use UI recipe | *Poppetry → Spinning*; JEI Spinning | Drains altar power; required ingredients/slot automation vary | Poppetry |
| Rite circle | Draw golden chalk heart glyph; draw rite-specific colored chalk; put focus item/entity in correct circle; activate heart after requirements are met | *Circle Magic → Fundamental Theory / Performing Rites* and named rite entry | Wrong color/shape/focus or unavailable power can prevent cast; the exact consumed-input/failure order must be researched | First safe rite |
| Poppet protection | Weave effect at wheel, bind to target, then keep poppet in target inventory or put it on Poppet Shelf | *Poppetry*, relevant effect page, *Poppet Shelf* | Shelf capacity four. Exact charges, triggers, ownership and slot priority open | Poppet safety |
| Familiar and broom | Tame and place familiar in rite circle; for broom, perform infusion rite using Flying Ointment | *Familiars*, *Brooms*, *Infuse Broom* | Familiar must be in circle when bound; flight handling must be tested in client | Familiar / mobility |

## Advanced and terminal objectives

- **Normal completion:** No native campaign or explicit completion state found. Treat the mod as an elective toolbox; an eventual chapter can be considered covered when it teaches one useful setup from each chosen branch and offers references for the full rite catalogue.
- **Optional completion:** Produce the main witch resources, craft a powered altar, learn brewing and mutation, operate both powered machines, use a poppet safely, bind a familiar, infuse a broom, and select useful rites.
- **Remaining progression blockers:** Exact artifact contents; all plant spawn/mutation tables; full custom recipe list and costs; altar math and structure detector; per-rite pattern/foci/input consumption/cooldown/failure; poppet binding and trigger matrix; familiar species/benefits; stable client handbook and JEI names.
- **Quest coverage check:** The walkthrough outline identifies the necessary instructional stages, but many rows are partial. This profile must remain `researching`; no quest chapter should be considered ready solely from this survey.

## Internal combinations and overlooked mechanics

| Internal combination | Full chain | Benefit | Limitation / exploit potential | Evidence |
| --- | --- | --- | --- | --- |
| Renewable fumes → altar → powered craft | Sapling/valid burnable → Oven + Jar → fume/byproduct → altar ingredient → 2×3 Altar → Distillery/Spinning Wheel | Converts ordinary world resources into witch-specific crafting inputs, then into ongoing magical utility | Each exact recipe/yield and renewable rate needs measurement; avoid claiming infinite free power | Oven, altar and processing pages/source |
| Mutation garden | Kettle → Mutandis/Extremis → use on plant → place compatible mutagens nearby → wait | Produces specialty witch plants such as Blood Poppy | Probabilistic; mutagen choice matters; exact odds and plant outputs need lookup | *Mutations* / Mutandis pages |
| Protected taglocked target | Taglock Kit → taglock → weave/bind matching poppet → inventory or shelf | Persistent, hands-free protection/effect for a selected player | Exact type protections, trigger event, charges and failure behavior not mapped | Poppetry, Taglocks, Shelf pages |
| Portable circle kit | Charge Attuned Stone at an altar through rite → carry stone, chalk and foci → draw rite remotely | Frees a circle from needing to be adjacent to altar | Stone capacity and rite power costs not known; verify which rites accept it | *Attuned Stones* / *Charging Stones* |
| Chalk recovery and flight | Craft broom → use it to sweep up glyphs; optionally infuse with Flying Ointment | Chalk cleanup plus mobility on same item | Flight handling and any upkeep need client testing | Broom and rite pages |
| Curse and cleanse counterplay | Curse rite → target-specific cleanse rite | Makes curses a reversible subsystem, with deliberate risk | Failed removal strengthens the curse; cat familiar/coven raises curse level | *Curses* pages and rite references |

## Low-hanging setups useful in ordinary survival

### 1. Basic fume station — early, after basic iron/clay

- **Goal and payoff:** Turn common burnables into ingredients used by later witch recipes.
- **Minimum entry and ingredients:** Witch's Oven (iron ingots and iron bars), soft clay jars fired into Clay Jars, furnace fuel, and a supported input such as a sapling.
- **Build/use steps:** Craft the Oven and jars from JEI. Put the supported input in its input slot, fuel it, and place a Clay Jar in the center slot. Observe the byproduct/fume. Add side Fume Funnels; attach two Fume Filters to reach the guide's guaranteed-success setup.
- **Sustainability and limits:** Wood/saplings are renewable; the oven cannot process ores. The setup is a resource extractor, not an ore doubler. Exact rates and supported input tags remain to verify.
- **Evidence / pack test:** Source recipes and *Fume Collection* guide; no pack/client test.

### 2. Mutandis garden — early-to-mid, recipe dependent

- **Goal and payoff:** Expand ingredient access to plants unavailable as normal crops.
- **Minimum entry and ingredients:** Kettle, water, a heat source, the ordered Mutandis recipe ingredients, a target plant and nearby valid mutagens. Use Mutandis Extremis when pursuing its advanced mutation branch.
- **Build/use steps:** Prepare a correctly ordered kettle recipe; heat it until complete; apply the mutation reagent to the target block; add the mutagens shown in the handbook diagram; wait several minutes and inspect the result. Keep a backup plant because the result is probabilistic.
- **Sustainability and limits:** A garden is renewable after successful plants are propagated, but the exact mutation pool and chances are not established here.
- **Evidence / pack test:** *Creating Brews*, *Mutations*, JEI Kettle; not tested.

### 3. Small altar workshop — mid, after fume ingredients

- **Goal and payoff:** Provide Enchanted power for Distillery, Spinning Wheel and rites.
- **Minimum entry and ingredients:** Six altar blocks; their recipe requires a Rowan Log, Stone Bricks, a Potion, Breath of the Goddess and Exhale of the Horned One; garden plants; optional upgrades.
- **Build/use steps:** Use the oven chain to make the two fumes from the listed birch/oak sapling recipes; craft altar blocks; place six in a 2×3 rectangle. Grow/place a variety of plants nearby. Put one valid upgrade of each available type on top and compare power or powered-machine operation.
- **Sustainability and limits:** Power is nature-dependent and recharge configuration exists; exact power formula, range, and recharge rate need verification. Only the highest-level upgrade in each type counts.
- **Evidence / pack test:** Altar recipe, generated recipes, *Altars*; no client test.

### 4. Poppet shelf — later, protective utility

- **Goal and payoff:** Keep up to four bound poppets active without filling a player's inventory.
- **Minimum entry and ingredients:** Poppet recipe/effect materials, Taglock Kit, spinning wheel, altar power, and shelf crafting materials.
- **Build/use steps:** Craft and weave one known protective poppet; collect the target's taglock as its guide page instructs; bind it through the supported rite/recipe; put it in the target inventory or shelf; verify the effect before relying on it.
- **Sustainability and limits:** Exact use charges/activation triggers and risk type depend on poppet. The four-slot shelf may hold poppets belonging to any player, but inventory/team ownership behavior is unverified.
- **Evidence / pack test:** *Poppetry*, *Taglocks*, *Poppet Shelf*; no client test.

## API, integration and pack-config surfaces

| Hook or customization surface | Namespace / exact file, API or serializer | Capabilities / constraints | Current TMM override? | Evidence |
| --- | --- | --- | --- | --- |
| Server configuration | `enchanted-server.toml`; source `common/.../ServerConfig.java` | `disabled_rites`; altar range default 32; recharge multiplier default 2.0; kettle spoil true; hoe-only modded seeds false; disable totems false; curse whisper min/max and clumsy scaling also configurable | No override verified; inspect generated pack config | Source snapshot |
| Recipe data | `data/enchanted/recipe`; custom Kettle, Distilling, Spinning, Byproduct types | Datapack recipes define inputs/outputs and recipe paths; no unrestricted arbitrary item conversion asserted | No recipe override verified here | Generated resources |
| Altar power API | `api/altar/PowerProvider.java`, `PowerConsumer.java`, helper/data classes | Enchanted-specific power connection; integrated machines consume altar power; not evidence of FE, Source or external mana compatibility | No bridge verified | Source snapshot |
| Circle magic API | `api/circle_magic/RiteFactory`, registry; server rite manager | Rite types register behavior; active rites tracked in server saved data. Data-driven/extension scope needs a deeper API audit | No TMM custom rite verified | Source snapshot |
| Brewing API | `api/brewing/BrewEffect`, registry | Brew effect extension surface exists; not a proof that pack datapacks can add every brew | No TMM brew override verified | Source snapshot |
| Mutagen API | `api/MutagenManager` and mutagen data provider | Mutation registration/data surface exists; exact schema and pack use need review | No override verified | Source snapshot |
| Mob/entity effects | Entities, familiars, curses, poppets, rites | Target and persistent state interactions are important; cross-faction damage/AI behavior is not established | No compatibility patch claimed | Source snapshot |

**Configuration values above are source defaults, not confirmed generated pack values.** Exact TMM config and datapack override audit remains outstanding.

## Compatibility leads

| Other mod | Candidate link and why | Native / pack-built / would require bridge | Status / source |
| --- | --- | --- | --- |
| JAWR, Vampirism, Werewolves | Blood Poppy/taglock and curse/familiar targeting may interact with transformations or blood-specific gameplay | Candidate only; likely needs explicit tests or bridge | No cross-mod profile; do not imply Enchanted recognizes blood factions |
| EvilCraft | Witch's Oven outputs and mod-specific materials may feed crafting recipes | Recipe compatibility audit; no automatic substitute assumed | Not established |
| Neo Vitae | Blood Poppy and any blood-related ritual provide an integration question | Candidate only; no claim that a block produces a recognized blood pool or EV | Not established |
| Ars Nouveau, Iron's Spells, Goety | Compare targeted protection, familiars/summons and world effects for overlaps or hostile interactions | Interaction report needed; no direct channel found | Not established |
| ProjectE / Project Expansion | Audit whether witch items receive EMC and whether the Matter Upgrader can bypass intended gates | Requires live pack recipe/value audit | Not established |

## Quest and teaching opportunities (research, not actual quests)

| Chapter stage | Required teaching detail | Truthful completion evidence and reward direction |
| --- | --- | --- |
| Handbook | Craft/open the Modopedia *Art of Witchcraft* using its actual recipe | Book is an item proxy; modest research XP and safe basic witch tool after no-gate-bypass check |
| Plants and safety | Identify common witch plants; use earmuffs before harvesting Mandrakes; use Arthana for named rare drops | Harvested ingredient/tool item tasks; manual task for safe Mandrake handling |
| Oven | Demonstrate one listed fume recipe, jar capture and funnel/filter upgrades | Recipe output only proxies operation; use manual task unless an actual machine-operation detector exists |
| Kettle | Teach water → heat → ordered inputs → boil → collect, and how spoil is recognized/removed | Output item is proxy; include the JEI Kettle category and the named handbook section |
| Altar | Build six blocks as 2×3, improve nearby plant diversity, test one powered machine | Possession of six blocks is not structure proof; manual completion or validated detector required |
| Powered machines | Operate one Distillery and one Spinning Wheel recipe | Output item proxy; reward ingredient that does not shortcut rite gates |
| Poppets | Weave, bind a known target, and maintain poppet in inventory/shelf | Crafted poppet doesn't prove binding; manual check until exact trigger detector is confirmed |
| Circle magic | Draw heart glyph, chalk pattern, focus and activate a low-risk rite | Rite result/world event likely needs manual task unless quest integration exposes it |
| Optional branches | Familiar, broom flight, waystone/transport, curse and cleanse, world rites | Separate safe/useful from destructive/hostile rites; no forced all-rites checklist |
| Terminal | No native terminal objective found | Chapter may close with capability selection and a free-choice reward; do not invent mastery progression |

## Verification plan and open questions

No tests were run because this was a knowledge-only pass.

1. Inspect the exact SHA-pinned CurseForge artifact's `META-INF`, recipe JSON, item/entity registries, config defaults, book pages and loot tables; compare it to the source commit above.
2. In a disposable client world, craft the handbook; confirm early seed access, Mandrake safety, jar behavior, exact oven inputs/yields, funnel/filter success rate, and Kettle order/spoil/collection rules.
3. Build the exact altar in a clean area; check structure acceptance, plant scoring, upgrade types, radius, base recharge and storage/charge in an Attuned Stone.
4. For Distillery and Spinning Wheel, record recipe input/output counts, power-per-operation, operation ticks, sided automation slots and whether rejected operations consume power/items.
5. Inventory every rite from handbook/data: exact chalk shape and colors, focus entities/items, altar/stone power, consumption order, range, duration, failure/cooldown and save behavior. Include negative cases for missing power, wrong focus, wrong time/weather and unloaded target.
6. Map poppet recipes/binding, target representation, active trigger, charges, ownership across players, death/unload behavior, shelf operation and interaction with armor/death-protection/totems.
7. Test relevant TMM faction and magic combinations, plus EMC/recipe bypass boundaries, before creating any interaction report.

## Evidence ledger

- [pin] TMM `mods/enchanted-witchcraft.pw.toml`: filename, SHA-1, CurseForge project/file IDs. Manifest row records enabled selection and says B5 pin/server test pending; that is not a client acceptance claim.
- [source: `55f37c7e15a2848be2cf25ff20232a1893795e75`] [upstream source branch](https://github.com/Favouriteless/Enchanted/tree/55f37c7e15a2848be2cf25ff20232a1893795e75): `common/src/generated/resources/assets/enchanted/modopedia/books/art_of_witchcraft/en_us` for handbook structure, instructions and rite list.
- [source: same commit] `common/src/generated/resources/data/enchanted/recipe` for recipes; `common/src/main/java/net/favouriteless/enchanted/common/blocks/entity` for ovens, altar, kettle, distillery and spinning wheel; `common/src/main/java/net/favouriteless/enchanted/common/ServerConfig.java` for server defaults.
- [docs: release metadata] [CurseForge file listing](https://www.curseforge.com/minecraft/mc-mods/enchanted-witchcraft/files/all) lists the NeoForge 1.21.1 4.2.7 release dated 2026-05-07. Listing metadata confirms release identity, not the pack's artifact hash or binary equivalence.
- [docs: upstream] [README](https://github.com/Favouriteless/Enchanted/blob/55f37c7e15a2848be2cf25ff20232a1893795e75/README.md) identifies Enchanted as a witchcraft/occultism mod and links its wiki; [CurseForge description](https://www.curseforge.com/minecraft/mc-mods/enchanted-witchcraft) says the mod's design has moved beyond being a direct Witchery remake.
- Not verified: exact JAR binary contents, generated TMM config, client usability, recipe balance, advancement/task behavior and every rite's full contract.
