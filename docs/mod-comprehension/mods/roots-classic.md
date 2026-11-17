# Roots Classic — mechanics and gameplay profile

| Field | Value |
| --- | --- |
| Manifest ID / actual mod ID | `roots-classic` / `rootsclassic` |
| Category / parent or addons | Magic; no Roots Classic addon selected |
| MC / loader / pack target | 1.21.1 / NeoForge / `test/full-integration` at `40940964e592d7076972a6de53497856d2ff710e` |
| Pinned version / artifact / hash | `RootsClassic-1.21.1-1.5.10.jar`; SHA-1 `7d02ec46e4d276ebf556cdd529a75108b4724efd` (`mods/roots-classic.pw.toml`) |
| Source snapshot | Version-setting commit [`db9aa883b7a54a1558dbc710546be1f42544be72`](https://github.com/Lothrazar/RootsClassic/tree/db9aa883b7a54a1558dbc710546be1f42544be72), branch `1.21.1`, Gradle declares 1.21.1-1.5.10; source/JAR build equivalence not tested |
| Guide/documentation version gap | Exact pinned Runic Tablet research definitions and English text inspected. The project branch is version-aligned; no separate external guide required. |
| Profile status | researching |
| Evidence reached | Exact JAR SHA-1 verified and inspected; immutable source version-setting commit, config, recipe files and tracker quest inspected; no client test |
| Reviewed | 2026-11-15 |
| Quest ownership | `config/ftbquests/quests/chapters/roots_classic.snbt` is an optional checkmark placeholder with 2 XP and paper; no actual walkthrough. Not edited. |

## Executive gameplay map

**What the player actually does:** Roots Classic is a nature-spell and ritual system. Gather Old Roots from tall grass, craft the Runic Tablet, and use its built-in research pages to find recipes and natural ingredient sources. Mortar and Pestle turn specific flower/plant recipes into Spell Powder; an Imbuer puts that spell into a limited-use staff. Terra is a small player resource pool that regenerates passively; spell charges and Terra are separate costs. For rituals, make an Altar, place recipe ingredients on it, place/ignite exact Incense in nearby Brazier(s), meet the recipe's Standing Stone tier, then sneak-right-click with an empty hand. The three Runic Tablet sections—Natural Arts, Spellcraft, Rituals—describe materials, every spell component, modifier levels, and named rituals. Later options include rare biome-specific flowers, animal/hostile summoning, weather and crop control, Living/Wildwood gear, and persistent Powered Standing Stones.

**Why it matters in TMM:** Roots has useful spell utilities and renewable farming/ritual options. It has two player-side limits that should not be conflated: spell staffs have their own finite durability-like charges, and all casts require Terra; Crystal Staff removes finite staff charges but still uses Terra; its pinned class does not enforce the guide’s natural-block/health rule. Several spells affect machines, terrain, mobs and players, so the spell effect can change pack economy or combat balance. The built-in Runic Tablet is unusually self-contained and provides exact custom-recipe pages; quests should teach one representative spell and one actual ritual operation.

**Quick start:** Break tall grass until Old Roots drop (default chance 1/40). Craft the Runic Tablet using a Stone from `c:stones`, Old Root and three wheat seeds from `c:seeds/wheat`; use it in main hand to open it. Craft Mortar and the Diorite Pestle, then use one spell's tablet recipe to make Spell Powder. Make a Bark Knife to gather bark from logs, and make an Imbuer; put a stick and Spell Powder inside to create the staff. Hold right-click for the configured charge time (20 ticks by source config), then release; check both Terra and the staff durability bar. For ritual magic, craft Altar and Brazier, use the Rituals tab for inputs/incense, and build the stone layout that matches ritual level before activating. [JAR recipe JSON; source commit above; Runic Tablet research data in `ResearchManager.java` and localization keys `rootsclassic.research.*`]

### Player-value snapshot

| Five standout capabilities | Three genuinely early practical unlocks | Three candidate integrations |
| --- | --- | --- |
| broad flower-component spell repertoire; Terra-powered reusable casting; ritual crop/weather/mob utilities; self-repairing Living/Wildwood equipment; automated nearby effects from Powered Standing Stones | Runic Tablet and Old Root discovery; one simple staff spell using common flowers; Growth Powder / Healing Poultice from naturally collected materials | Ars Nouveau/Botania compare spell/source economies without assuming shared mana; JAWR factions and the hostile summon/flare/sacrifice spells; TMM skill XP or faction combat skills interacting with Roots damage events |

### Subsystem coverage at a glance

| Subsystem / branch | Mechanics evidence | Progression completeness | In-game guide/JEI reference | Client validation | Blocker / next action |
| --- | --- | --- | --- | --- | --- |
| Runic Tablet guide/research | [source: `db9aa883`] and JAR exact recipe | complete source map | Runic Tablet → Natural Arts / Spellcraft / Rituals | not run | Verify UI and recipe displays in client |
| Natural resources and tools | exact loot/recipe/config + research pages | strong | Natural Arts → Bark Harvesting, Rare Materials, Growth Powder, Mutating Powder, Berries, Stew, Poultice | not run | Verify drops and modifiers against world state |
| Spell Powder, modifiers, Imbuer and Terra | exact source handler, recipe JSON, guide pages | broad; every component inventoried | Spellcraft → Mortar, Imbuer, Modifiers, Terra, individual flower entries | not run | Verify cast/failed-cost order and settings |
| Ritual Altar/Brazier/stone tiers | exact recipe parser, structure and ritual JSON | strong source map for tiers; one ritual per type | Rituals → Basic Ritual Mechanics, named ritual entries | not run | Test valid/invalid layouts, extra incense and activation |
| Staves/Crystal Staff | source handler, rituals, research | partial, four-spell and mobility usage mapped | Spellcraft → Imbuer; Rituals → Crystal Staff | not run | Validate spell cycle, natural-block check and charge behavior |
| Ritual effects and item outputs | 46 ritual JSONs + source effect classes | all broad effect families inventoried | Rituals tab and JEI custom Ritual category | not run | Audit per-effect entity, world target, safety and costs |
| FTB chapter | exact tracked SNBT | placeholder only | — | not run | Later quest pass replaces checkmark with how-to chapter |

## Guidebook onboarding

- **Guide and access:** `rootsclassic:runic_tablet`, a custom three-tab guide UI opened by using it in the main hand. Shaped recipe: Old Root above one `c:stones` block, three wheat seeds below/at sides as pattern `" R ", "SBS", " S "`. There is no Patchouli dependency or external book acquisition path.
- **Earliest attainable stage:** early Overworld; the rare input is Old Root, default drop chance 1/40 from tall grass. No boss, dimension or chapter prerequisite.
- **First instructional quest:** “Find the Runic Tablet.” Teach tall-grass collection, exact shaped ingredients, right-click/open in main hand, and guide navigation through Natural Arts, Spellcraft and Rituals. FTB can detect possession only; task text must say how to open and consult it.
- **Reference map:** Natural Arts: Bark Harvesting; Rare Materials (Old Roots, Verdant Sprigs, Infernal Bulbs, Dragon's Eyes); Growth Powder; Mutating Powder; Berries; Rooty Stew; Healing Poultice. Spellcraft: Mortar/Pestle/Usage; Imbuer/Staff Mechanics; Modifiers; Terra; 19 standard floral ingredients and three mutated flower spells. Rituals: Basic Ritual Mechanics; Living Tools; Growth; Standing Stones; Animal Summoning; Crystal Staff; weather; Flare; Enhanced Standing Stones; Monster Summoning; Sylvan/Wildwood Armor; Powered Stones; Mass Breeding; Life Drain; Sacrifice; Runic Focus; Engraved Blade; Time Shift. Pinned JAR research definitions and localization key names were checked against source.
- **Guide limits:** Item recipe pages explain names/effects and show JEI-like recipes, but physical construction/activation still needs operational instructions. Ritual entry pages do not replace the common requirements: exact altar inventory, exact lit incense set, correct level layout, and empty-hand sneak activation. Spell pages can mention powerful consequences without describing all boundary targets.
- **Other in-game help:** JEI plugin registers custom Mortar and Ritual recipe categories; shaped recipes appear normally. No patchouli book or worldgen guide structures were found in the exact JAR.

## Systems and full mechanics inventory

| Subsystem | How accessed | Inputs/costs | Effects/outputs | Limits, failure and evidence |
| --- | --- | --- | --- | --- |
| Natural Arts materials | Loot events from tall grass, crop harvest, Nether Wart, chorus flowers, oak leaves; use Bark Knife on logs | Default chances: Old Root 1/40 tall grass, Verdant Sprig 1/30 fully grown crop, Infernal Bulb 1/20 grown Nether Wart, Dragon's Eye 1/10 chorus flower, berries 1/25 oak leaves; bark knife has 0.3 probability per harvest of stripping log | spell/ritual bases; Old Root small food; Nether wart bulb furnace fuel; Dragon's Eye edible/smeltable to Ender Pearl; five berry types | Values are source defaults/config, not measured per-minute yields; crop species and harvesting conditions may matter. Guide names exact eligible sources. |
| Runic Tablet | Craft with Old Root, stone and wheat seeds; use in main hand | One book item | Client-side GUI reveals all research groups, display recipes, component recipes and ritual recipes | Research is registered in source, not hidden behind chapter prerequisites. Main hand only opens UI. UI/client behavior not tested. |
| Bark harvesting | Craft Bark Knife; right-click supported log | Uses item durability/config chance; can damage/strip log state | bark item corresponding to wood | Source config strip chance 0.3; repeated harvesting and whether knife durability drains need exact code/test. Bark tags cover vanilla wood variants. |
| Mortar / Pestle | Craft both; put the selected component recipe's plant/ingredients into mortar | Recipe item ingredients produce one Spell Powder; spell effect component determined by recipe | `rootsclassic:spell_powder` | Exact 22 component recipes in custom recipe serializer; no generic plant mash. Mixing base belongs in Mortar; Imbuer accepts a wooden rod and completed powder. See pinned spell appendix. |
| Staff Imbuer | Craft Imbuer; place stick and powder in inventory slots and wait | Stick + Spell Powder; recipe-defined staff uses | Limited-use spell staff with component and modifier data | Cast with hold and release; pinned release guard at least13 elapsed ticks; staff loses one use before it checks sufficient Terra, see issue note below. Staff breaks at uses ≤0. |
| Terra | Player attachment initialized at 40/40 and persisted to player NBT | Each spell has component-specific mana cost divided by `efficiency+1`; source `ComponentBlueOrchid` cost 14 as one example | resource pays for spell cast | `ManaAttachment` clamps between 0 and max. `ComponentSpellsEvent` hardcodes +1 Terra every 5 player ticks, i.e. 0.2 per tick/4 per second until full. Config says 15 ticks but appears unused; no external mana transfer found. |
| Spell modifiers | Mortar recipe places base ingredient before modifier ingredients | Old Root 0 modifier slots; Verdant Sprig 1; Infernal Bulb 2; Dragon's Eye 3. Glowstone increases potency, redstone efficiency, gunpowder size | Strength/area/charges and cheaper mana per cast | Multipliers are per spell/config; exact item order in JEI/Tablet. More efficiency also grants 15 extra uses per level by current config, but failed low-mana cast still loses one use. |
| Standard spells | Produce powder from one of 19 ordinary recipe components; imbue staff | one limited staff charge and Terra; component-specific costs, target effects and damage | Insanity/hostile targeting; Earth Spike; equipment damage; block shatter; spectral skeleton; shielding; cold damage; life drain; device overclock; gust; undead smite; crop growth; thorn damage; heal; negative-effect conversion; temporary water; lightning; fireball; teleport | The Tablet maps every component. Earth Spike intentionally duplicates only overworld base stone, dirt, grass, sand, gravel; no ore duplication. Check property restrictions and TMM gated blocks separately. |
| Mutated spell plants | Mutating Powder + environment/effect/item conditions from Tablet | Radiant Daisy: Oxeye Daisy at noon, Glowstone + Prismarine Crystal, Night Vision; Flare Orchid: Blue Orchid in Nether, Netherrack ring, Fire Resistance, Blaze Rod + Lava Bucket; Midnight Bloom: Poppy two blocks above Obsidian in End, Slowness, Coal Block | Extra spells: piercing radiant beam, explosion spell, time-stop area | These are environment/ritual setup recipes, not natural worldgen. Tablet instructions are specific; validate target and block replacement in client. Mutating Powder uses Nether Star per recipe. |
| Crystal Staff | Ritual crafting and Imbuer ritual add spells | Crystal Staff + ritual components; up to four additional Spell Powders as incenses | Reusable staff stores up to four spells; sneak-right-click cycles spell | No finite uses per guide; still consumes Terra. Pinned release has no natural-ground or health fallback, correcting older guide claim. See spell/item appendix; cycling/empty slots need client acceptance. |
| Ritual Altar and Brazier | Craft Altar and one or more Braziers; fill altar with exact materials; load/ignite exact incense; sneak-right-click empty hand | Ritual recipe lists item ingredients, up to four incense ingredients and a level (0–2). Extra ingredients/incenses cause failure | One effect: item crafting, crop/mob/world/player effect | Braziers count within 9×9 horizontally and same Y. Extra burning incense can invalidate matching; empty hand sneak activates. Recipe data identifies fake presentation vs actual inputs? Here serializer fields are actual consumed ingredients/incenses, but output effect differs by ritual. |
| Tier 0 rituals | Altar and Brazier, no pillar pattern | Recipes with `level:0` | e.g., Living Tools, Grow, and basic crafting/effects | Still requires exact burning incense when recipe defines it. |
| Tier 1 Standing Stones | 8 Mundane Standing Stones at altar offsets (±3,0,±3) and four cardinal positions (±3,0,0)/(0,0,±3) | Altar components and incense per recipe | Animal summoning, crystal staff, rain/weather and other mid-tier outputs | Stone layout is checked by exact block positions; missing block fails. No power/mana input documented. |
| Tier 2 Attuned Stones | Same eight Mundane Stones plus four Attuned Standing Stones at cardinal offsets (±5,1,0)/(0,1,±5) | Higher-tier recipe items/incense | monster summon, armor, powered stone, time shift and late rituals | Data supports levels 0–2; client must confirm physical height/relative reference and success cue. |
| Ritual effects: tools/armor | Ritual recipes with result items in `effectConfig` | Base tools/armor plus natural materials/incense | Living tools repair over time; Sylvan equipment improves spell efficiency and auto-repairs; Wildwood protection above iron/below diamond, auto-repair and increased natural health regeneration per guide | Exact enchantability, rates, and complete wearable effects require code/test. |
| Ritual effects: growth/farming | Grow ritual or Grower Standing Stone; Mass Breeding | Crop catalysts and herbs or configured effect | Area crop growth; breeding; nearby crop growth boost | Grow scans 35×35×9 and randomly applies bonemeal-like action according to guide. Powered stones are persistent and described as no-cost after craft. Rates/permissions unmeasured. |
| Ritual effects: weather/time/combat | Summon/Banish Rain, Flare, Life Drain, Sacrifice, Time Shift, Engraved Blade | Recipe item/incense, tier; effects may target entities or area | Weather change, damage/kill or heal, time manipulation, weapon enchantments | Sacrifice/Flare are potentially lethal; time effects pause entities; never make default quest reward path. Need exact mob/player boundaries and boss protections. |
| Summoning | Tier 1 recipes reanimate passive mob from meat/hide/bone; tier 2 offers hostile recipes | Exact creature-specific recipe ingredients/incense; effect config names output entity | Mob appears atop altar | Not generic drop-output recipe; may consume input items and spawn creature with AI/loot. No group ownership/target behavior inferred. Test player-vs-mob targeting and drops. |
| Powered Stones | Craft each stone by ritual, place in world | Ritual crafting output; no recurring fuel | Accelerator gives Speed II nearby, Entangler Slowness II, Grower crop growth, Healer likely area healing, other utility stones use passive radius checks | Guide says perpetual and no cost. Enumerated seven types; radius, mob filters and redstone needs must be verified. |

## Material / capability input-output contracts

| System | Input kind + exact IDs/tags/events | Qty/consumed/remainder | Output and location | Trigger/cadence/range | Automation/API/evidence |
| --- | --- | --- | --- | --- | --- |
| Natural materials | World drop events for tall grass, crop harvest, nether wart, chorus flower, oak leaves, log use | Config probabilities above; bark knife uses supported log interaction | Item stack to player/drop inventory | event-based; tree/crop/block interaction | Native loot/events; no ore, fluid, FE or dimension resource API |
| Mortar | Item stacks matching `rootsclassic:component` recipe, e.g. Blue Orchid + Prismarine Shard + Lapis + Vine | Recipe items consumed; one Spell Powder result | Item in Mortar output/player hand | GUI recipe operation | JEI Mortar custom category; recipe JSON typed item list |
| Imbuer | Stick item + Spell Powder with encoded component/modifier | Inputs consumed and converted into staff stack with item components | limited-use Staff in Imbuer output | machine slot processing | Custom block entity; source recipe and staff item code |
| Staff spell cast | Staff with SpellData and StaffUses, player Terra | Release after the native charge guard; one use removed before Terra affordability | Effect only when Terra covers cost divided by efficiency+1 | Native releaseUsing; use count0 can pass guard before inventory cleanup | Exact pinned StaffItem; no client acceptance |

## Build and guide references

| Build | Minimum actionable setup | In-game reference | Failure boundary | Teaching role |
| --- | --- | --- | --- | --- |
| Mortar | Use exact component recipe and Pestle; preserve powder SpellData | Runic Tablet Spellcraft and JEI component category | Wrong inputs/modifier slot count prevents intended spell | First powder lesson |
| Imbuer | Stick + Spell Powder in block slots; wait for staff | Spellcraft → Imbuer; JEI Imbuer | Missing/wrong powder prevents staff; check result charge icon | first staff how-to |
| Base Altar | Place Altar and item ingredients; no standing-stone blocks for level 0 | Rituals → Basic Ritual Mechanics | Extra/wrong items or incense can make no ritual match | first ritual |
| Tier 1 | Eight Mundane stones at 8 code-defined positions around altar, same block level | Rituals → Standing Stones; JEI Ritual | Every block position must match exact block | tier-1 choice |
| Tier 2 | Tier-1 ring plus 4 Attuned stones at cardinal outer coordinates one block above altar | Rituals → Enhanced Standing Stones | Missing stone means recipe-level structure check fails | tier-2 choice |
| Incense | Braziers within 4 blocks horizontally, at altar's same Y; load matching item and ignite with Flint and Steel | Basic Ritual Mechanics / JEI recipe `incenses` | Extra burning incense may prevent recipe match; sneak-right-click empty hand extinguishes a Brazier | all ritual quests |
| Mutated blooms | Follow distinct biome/time/status/resource directions for flower type, then use Mutating Powder | Mutating Powder + each flower entry | Wrong dimension/time/effect/ring/item fails; biome recipe is not an item-only recipe | optional flower quest |

## Internal combinations and overlooked mechanics

| Combination | Full chain | Benefit | Limitation / exploit risk | Evidence |
| --- | --- | --- | --- | --- |
| Crop renewal → Verdant Sprigs → spells/rituals | Farm crop → harvested-drop chance → base/modifier or ritual ingredient | Renewable spell ingredient | 1/30 chance per mature-crop harvest default; rate not measured |
| Efficiency → staff lifespan and Terra economy | Use Redstone modifier → efficiency level → additional uses and cost divides by `efficiency+1` | Fewer refills and cheaper casts | Default config definitions mismatch obsolete-looking 65/32 versus actual 15/15 getters; verify exact compiled behavior |
| Earth Spike → common-block construction | Target tagged base stone/dirt/grass/sand/gravel → create temporary spikes/build columns | Creates common building blocks and launches mobs | Deliberate duplication, limited whitelist in code; test modded block tags and protected structures |
| Grow ritual → crop field | Level 0 altar recipe with wheat/beetroot/potato/carrot incense and Redstone/Bone Meal/Verdant Sprig ingredients | Area crop growth | scans 35×35×9, random few targets; may affect pack crop economies; no throughput test |
| Crystal Staff + natural block | Four Powder incenses -> four switchable effects; stand on natural block | Reusable, flexible casting; natural-ground requirement avoids life drain | Terra still pays; whether charge resets/cycling replaces a slot confirmed by guide/source; test item state persistence |
| Powered stones | Craft once via ritual then place persistently | Speed II, Slowness II, crop growth, healing, repulsion/vacuum/etc. aura | Source guide says no upkeep; exact radius and entity filters are not measured; may be passive balance exploit |

## Low-hanging setups useful in ordinary survival

### 1. Runic Tablet — early

- **Goal/payoff:** Get all Roots Classic recipes and exact spell/ritual procedures on the player.
- **Minimum entry:** Old Root from tall grass, one `c:stones` item and 3 wheat seeds.
- **Build/use:** Craft exact shaped recipe; hold in main hand and use; browse Natural Arts, Spellcraft, Rituals.
- **Sustainability/limits:** One reusable guide; recipe availability does not require progression unlocks.
- **Evidence/test:** Exact recipe and item use in JAR/source; UI not opened in game.

### 2. Simple utility staff — early

- **Goal/payoff:** A spell for controlled mobility/damage/healing/area block utility, with Terra cost and visible staff charges.
- **Minimum entry:** Common flower recipe ingredients, Old Root, Mortar, Pestle, stick, Imbuer; no rare dimension item for ordinary spell components.
- **Build/use:** Mortar recipe → Spell Powder; Imbuer with stick → staff; charge by holding use for one second; release; monitor bar/Terra.
- **Sustainability/limits:** Limited-use staff; Terra regenerates; lowest-level spells have no modifier slots unless you use rare bases. Low-Terra releases still consume a staff use in source code.
- **Evidence/test:** Source/recipes inspected; actual bar/cast not tested.

### 3. Living Tool — tier 0 ritual

- **Goal/payoff:** Upgrade a wooden tool to iron-like performance with gradual self-repair.
- **Minimum entry:** Altar, Brazier, wooden tool, Verdant Sprig, gold nugget, exact bark incense.
- **Build/use:** Use tablet's Living Tools page; place all recipe inputs on altar; ignite both Oak Bark incenses in nearby braziers; sneak-right-click Altar empty hand; verify result.
- **Sustainability/limits:** Auto-repair rate needs test; ritual is not free material duplication.
- **Evidence/test:** Exact `living_pickaxe` recipe / guide; no in-world test.

### 4. Crop Growth ritual — tier 0, short chain

- **Goal/payoff:** Affect a broad crop area via ritual rather than manually bonemealing.
- **Minimum entry:** Altar, Brazier, Redstone, Bone Meal, Verdant Sprig; Wheat, Beetroot, Potato, Carrot as burning incense.
- **Build/use:** Match exact displayed recipe, put each incense in its own lit brazier, activate with empty-hand sneak right-click, inspect scan region for crop changes.
- **Sustainability/limits:** 35×35×9 scan; only a few crops receive randomized bonemeal-like updates per ritual according to the guide. Do not promise a farm rate.
- **Evidence/test:** Exact pinned `ritual/grow.json` + language page; no client test.

## API, integration and pack-config surfaces

| Hook/surface | Exact IDs/file | Capabilities/constraints | TMM override? | Evidence |
| --- | --- | --- | --- | --- |
| Component recipes | `rootsclassic:component`; 22 JSON recipes | Item ingredients → Spell Powder and component ID; no arbitrary flowers | None found | exact JAR recipe set |
| Ritual recipes | `rootsclassic:ritual`; 46 JSON recipes | Item requirements, incenses, level 0–2, effect ID/config, ritual color | None found | JAR JSON + source serializer |
| Research GUI | `rootsclassic:runic_tablet`; `ResearchManager` | Client menu dynamically builds pages from recipe manager; not advancement-locked | None found | JAR/source |
| Player Terra | Attachment `rootsclassic:mana`; defaults 40 max/current | player-bound saved data, no fluid/FE provider; regen handler hardcoded every 5 ticks | None found | source `ManaAttachment`, `ComponentSpellsEvent` |
| Roots config | common + client specs; item drops, staff charges, PvP toggle, mana interval, bar location | `ticksPerManaRegen` and old `staffUses/efficiencyBonus` fields appear unused by current event/item getter; actual values require exact jar-bytecode/runtime comparison | No tracked roots config file | source config and handlers |
| Ritual structure | Altar, Brazier, Standing Stone blocks; `RitualPillars` | fixed coordinate checks; no support for alternate layout found | None found | immutable source commit |
| JEI support | `RootsClassic` JEI plugin | component/mortar, ritual altar, item info categories | TMM pins JEI, no Ponder | source plugin and manifest |
| World/loot hooks | Tall grass/crops/leaves/Nether Wart/chorus; no worldgen data files | event-driven resource entries, natural mutations | None found | JAR entries + source loot modifier |

## Compatibility leads and recorded issues

| Other system | Candidate link/issue | Status |
| --- | --- | --- |
| TMM shared mana design | Roots `Terra` is a private player attachment, default cap 40; no FE/Ars Source/Botania mana capability found | **No native bridge found.** Do not merge resource bars without a deliberate compatibility layer and exact cost analysis. |
| Mana regen config | `RootsConfig` exposes default `ticksPerManaRegen=15`; actual `ComponentSpellsEvent.TICKS_PER_MANA_REGEN` is hard-coded to 5 and no config reference exists in source search | **Likely ineffective setting/source bug; runtime unverified.** Actual source event implies one Terra per 5 ticks (4/sec) versus configured rate 1 per 15 ticks. Confirm exact JAR bytecode and in-game rate before tuning. |
| Staff use consumption | `StaffItem.releaseUsing` subtracts one staff use before checking whether player Terra meets the spell cost | **Source-confirmed failed-cast penalty.** Low mana can burn finite staff charge without effect. Quest instructions should teach watching Terra; a future fix/tuning is outside this pass. |
| Stale staff config values | Config exposes legacy `staffUses=65` and `efficiencyBonusUses=32`, while active getters use `staffUsesBasic=15` and `staffUsesEfficiency=15` | **Likely stale config knobs.** Need verify pinned JAR bytecode, then identify if these were intended overrides. No current TMM config override. |
| Earth Spike economy | `ComponentBlueOrchid` accepts overworld base stone, dirt, grass, sand or gravel and writes copies into surrounding world cells | **Intentional common-block duplication; no ore bypass from explicit source predicate.** Test modded blocks that inherit the tags and structure protection. |
| Mob/faction effects | Roots can summon skeleton, zombie, spider, creeper, cave spider, slime, enderman; Life Drain/Sacrifice/Flare and other hostile actions can target mobs/players | **Faction/hostility and boss interactions untested.** No TMM alliance map found for Roots summons. Include with JAWR/Dragon/undead faction audit. |
| Magic-bar overlay | Roots client draws Terra bar with configurable vertical offset | **Possible UI overlap** with other mana bars; no screenshot/client test. |

No demonstrated crash or declared dependency conflict was found. Pinned JAR metadata requires Minecraft 1.21.1 and NeoForge 21.1.1+, both met by TMM. Exact JAR contains its own book UI and declares no Patchouli dependency; the project source comments out optional Botania dependency and exposes no Roots/Botania bridge. Roots does ship optional JEI recipe categories; TMM has JEI. License in pinned JAR metadata is MIT.

## Quest and teaching opportunities (research, not actual quests)

| Stage | Actionable how-to | Guide/JEI | Detector truth / fallback | Reward/risk |
| --- | --- | --- | --- | --- |
| Find/open Tablet | Break tall grass for Old Root; craft with wheat seeds + stone; use mainhand | Natural Arts → Old Roots; Runic Tablet recipe | item detection proves only possession, not opening; manual text instruction | small XP + ingredient-themed reward |
| Make/cast first staff | craft Mortar/Pestle; create one spell; staff in Imbuer; charge/release; show Terra/charge | Spellcraft pages | item task cannot detect cast; use Roots advancement if exact criterion exists or manual fallback | XP; do not hand out staff or powder that skips recipes |
| Learn modifiers | explain base ingredient first, slots, which dust changes which stat | Modifiers; JEI | craft task only; no cast proof | targeted material reward; do not imply modifier is free |
| Perform first ritual | construct altar/brazier, load exact inputs/incense, ignite, empty-hand sneak-click, observe output | Basic Ritual Mechanics + chosen ritual page | task detector not yet checked for ritual completion; manual fallback | XP + safe recipe component |
| Build higher-tier stones | display exact 8 Mundane/4 Attuned positions and verify level-1/2 ritual | Standing Stones, Enhanced Standing Stones | checkmark/stone possession not formation; actual ritual event preferred | XP; late ritual outputs/equipment not rewards |
| Choose dangerous ritual | teach target bounds, hostile summon, damage and irreversible world effect | named Ritual page / JEI effect | don't use crafted ritual item as success; manual/action confirmation | avoid default quest rewards that provide boss/gated entity loot |

### Existing chapter / pilot reconciliation

| Walkthrough stage | Existing quest | Actual detector | Gap |
| --- | --- | --- | --- |
| Entire mod | `config/ftbquests/quests/chapters/roots_classic.snbt`: optional checkmark, XP 2, paper | Generic checkmark proves nothing about Roots | No stages or how-to; replace later with guide acquisition/spell/ritual progression |
| Tablet/spells/rituals | None | No Roots recipe task or ritual trigger verified in FTB | Need task schema/runtime inspection in quest pass; teach even if item detector is all available |

## Evidence ledger

| Claim | Exact source | Evidence/limit |
| --- | --- | --- |
| Pin and binary | `mods/roots-classic.pw.toml`, downloaded CurseForge file 8860609; SHA-1 `7d02ec46e4d276ebf556cdd529a75108b4724efd` | `[pin]` + `[jar: SHA1]`; directly inspected exact lock-matched JAR |
| Version-aligned source | [commit `db9aa883`](https://github.com/Lothrazar/RootsClassic/tree/db9aa883b7a54a1558dbc710546be1f42544be72), `gradle.properties` 1.21.1 / 1.5.10 | `[source: SHA]`; matching version declaration, not binary reproducible-build proof |
| Runic Tablet | JAR `recipe/runic_tablet.json`, `assets/rootsclassic/lang/en_us.json`; `item/RunicTabletItem.java`, `research/ResearchManager.java` | exact item recipe, action, three categories and pages; client not launched |
| Spell recipes/mechanics | JAR `data/rootsclassic/recipe/component/**`, source `component/**`, `item/StaffItem.java` | exact item inputs/outputs, costs/effects, modifiers and failed-cast ordering |
| Ritual recipe/structure | JAR `data/rootsclassic/recipe/ritual/**`; source `recipe/RitualRecipe.java`, `ritual/RitualPillars.java`, `ritual/RitualRegistry.java` | exact required ingredient/incense/level data and coordinate gates; activation not runtime tested |
| Terra/config issue | source `attachment/ManaAttachment.java`, `event/ComponentSpellsEvent.java`, `config/RootsConfig.java` | Exact pinned classes confirm hardcoded5-tick regeneration versus declared15 interval; staff active15/15 getters confirmed. |
| TMM quest/config state | `config/ftbquests/quests/chapters/roots_classic.snbt`, tracked config tree | placeholder checkmark and no runtime override found |
| Release target | [CurseForge file 8860609](https://www.curseforge.com/minecraft/mc-mods/roots-classic/files/8860609) | confirms 1.21.1 NeoForge artifact and release metadata |

## Disposable-world test plan

| Test | Positive/boundary setup | Expected observable | Performed? |
| --- | --- | --- | --- |
| Guide | Craft exact Tablet; use mainhand vs offhand; inspect all three tabs | mainhand opens full three-section UI; tab content/patterns and recipe pages render | no |
| Natural input rates | Controlled samples: tall grass, mature crop, nether wart, chorus flower, oak leaves; use knife repeatedly on log | observed drop/strip distributions approximate configured values; exact denominators remain probabilities, not guarantees | no |
| Staff cast/cost order | Fresh 15-use staff; measure Terra; valid cast then try below required Terra | valid spell changes target and consumes one use/cost; failed cast consumes staff use (source says yes) with no effect | no |
| Terra configuration | Change interval 15 → 100 in disposable config; log attachment over ticks | if config works rate changes; source suggests it will not due hardcoded constant 5 | no |
| Spell output bounds | Cast Earth Spike into supported ordinary blocks and modded blocks carrying relevant tags; test ore, protected/claimed blocks | only intended common block categories duplicate; no gated block or claim grief, or record boundary failure | no |
| Ritual tier patterns | valid level0; valid/missing one of 8 tier1 stones; valid/missing one of 4 tier2 stones | only complete structure of matching level succeeds | no |
| Ritual inputs/incense | correct ingredients and exact burning incense; add extra incense, wrong Y, unlit brazier | exact recipe succeeds; extra/wrong/unlit/out-of-range cases fail as code says | no |
| Summoning/hostility | Passive and hostile summons near player, dragon, JAWR faction mobs, town; kill and observe drops | exact target/neutral behavior and item costs; no unplanned faction or drop effect | no |
| Crystal Staff | Load 4 powders, cycle, cast on natural vs nonnatural block | four selected spells persist/cycle; natural-ground casting costs Terra, nonnatural may use life as guide says | no |
| Powered Stones | Place each powered variant; measure range/effects with allowed/disallowed entities and crops | actual effect radius and no-resource operation; no unintended combat/farm loop | no |

## Unknowns and next research

- [ ] Pinned class comparison completed for RootsConfig, event cadence and StaffItem; effective TMM world config and runtime behavior still need testing.
- [ ] Launch client to test Runic Tablet rendering, Mortar/Imbuer menus, spell targeting and whether insufficient mana consumes staff durability.
- [ ] Map each of the 46 ritual effects to exact target, range, result count, damage, duration, drops, entity AI and success/failure cue. Common recipe parser is source-backed, not all effect implementations.
- [ ] Verify Powered Stone and Living/Wildwood item exact range, repair, protection and activation behavior; confirm all quest references agree with 1.5.10 pages.
- [ ] Test Roots hostile summons against JAWR/Dragon Survival/faction policy and consider output/drop gate interactions before questing.
- [ ] Resolve Earth Spike behavior with datapack-tagged modded blocks and FTB chunks claims in a disposable world.
- [ ] Reconcile the empty FTB chapter during the later quest pass; no quest/runtime edit is part of this profile.

## Revision notes

- 2026-11-15 — Added Roots Classic 1.5.10 exact-JAR/source profile. Mapped the Runic Tablet, natural input loop, spell/staff/Terra mechanics, ritual tiers/layouts and major ritual branches. Recorded source-level regeneration and staff-consumption concerns plus intentional Earth Spike common-block duplication. No quest/runtime files changed; client tests not run.

## Local artifact and document recovery — 2026-11-16

The tracked profile contained a malformed binary prefix, a nibble-shifted recoverable first section and a damaged transition before its readable Imbuer build row. Original file SHA256 `e02b29372317a4ab10492feabf0eaedbfad6f3c62d0b07d0dea84098ad52e309`. Recovered valid UTF-8 prose and replaced the damaged table transition with newly checked StaffItem/build references; no claim of byte-for-byte restoration of lost text. All research Markdown now decodes as UTF-8. Exact local JAR SHA1 matches its pin. Its actual inventory is46 ritual recipes,22 component recipes,10 shaped,5 shapeless and1 smelting recipe, correcting older49/23 claims. See [pinned ritual audit](roots-classic-pinned-rituals.md) for all46 recipe rows and12 implementation families. Main profile remains researching pending component/item-effect closure; no client acceptance.

See [all22 pinned spells, stones and advanced branches](roots-classic-pinned-spells-items.md) for exact classes, recipe inputs, cast costs, targeting, auras, mutations and guide discrepancies. Runic Focus downstream use and Engraved Forceful hook remain the narrow source-coverage gaps; all client/claims/faction tests remain unrun.
