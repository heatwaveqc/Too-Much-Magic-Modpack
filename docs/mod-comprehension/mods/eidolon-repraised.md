# Eidolon: Repraised — mechanics and gameplay profile

| Field | Value |
| --- | --- |
| Manifest ID / actual mod ID | eidolon-repraised / eidolon_repraised |
| Category / parent or addons | magic; Curios required upstream |
| MC / loader / pack target | Minecraft 1.21.1 / NeoForge / TMM test/full-integration |
| Pinned version / artifact / hash | eidolon_repraised-1.21.1-0.5.0.8.jar; CurseForge file 9332315; SHA-1 12a816c11a064a7050e74c5c15cf07fb8269b6d4 |
| Source snapshot | 1.21.1 branch commit [85484f2804bcff82abb3397d8f384422d7e21898](https://github.com/Alexthw46/Eidolon-Repraised/commit/85484f2804bcff82abb3397d8f384422d7e21898); gradle.properties declares 0.5.0.8; binary equivalence not proven |
| Guide/documentation version gap | Codex text and data in matching-version source; exact client display not tested |
| Profile status | researching |
| Evidence reached | Exact pack pin metadata, matching-version upstream source and prior server-generated config; no locally available jar for hash comparison or gameplay test |
| Reviewed | 2026-11-16 |
| Quest ownership | Existing Eidolon: Repraised chapter is one optional checkmark with 2 XP and paper; no lessons |

## Executive gameplay map

**What the player actually does:** Eidolon is an occult crafting and spell system organized around its Codex, divine reputation, sign sequences, rituals, research, and soul equipment. The Codex starts the player: a book plus rotten flesh crafts eidolon_repraised:codex, which opens a six-category in-game guide. Players give an unmarked Codex to a Witch or Cleric villager to learn the Wicked or Sacred sign, then use an altar, effigy, and chants to gain patron reputation. Both patron tracks can be advanced at once. Further signs and spells come from reputation milestones, sacrifice/healing rites, and a small research table branch. Crafting uses several distinct stations: pewter worktable patterns with reagent slots, a water-filled Crucible over a lit campfire, a Brazier that burns a focus item to run a ritual, and the Wooden Brewing Stand. The mod also adds silver/lead equipment, Curios, hostile/neutral creatures, ores, plants, structures and worldgen.

**Why it matters in TMM:** Eidolon contributes an additional magic progression, utility spells, enemy summons/transformations, healing, and gear. It has its own patron reputation, mana and research knowledge. No conversion into Ars Source, Iron's spell power, ProjectE EMC, Neo Vitae EV, or Draconic OP/FE was established. Its prayer gates and research features are a separate progression system and the Codex itself warns that research is unfinished.

**Quick start:** Craft the Codex from one book and one rotten flesh; open it and read Nature, Rituals, Artifice, Theurgy, Signs and Spells. Drop an unmarked Codex near a Witch to get the Wicked sign or a Cleric villager for the Sacred sign. Make an altar and straw effigy; place an altar icon and any desired, recognized altar-enhancing objects on top. Use the sign sequence for the corresponding patron prayer, then follow the Codex for the first required rite. The guide is the instruction source; recipes are split among crafting, worktable, crucible, brewing and ritual systems.

### Player-value snapshot

| Five standout capabilities | Three genuinely early practical unlocks | Three candidate integrations |
| --- | --- | --- |
| Sign-sequence chants; parallel dark/light patron progression; multistep Crucible alchemy; entity/item rituals; soul enchanting and equipment | Codex (book + rotten flesh); Pewter Blend/early worktable crafts; altar, effigy and first patron sign | Curios equipment slots (native); Apotheosis Wand affix compatibility (conditional source code); TMM monster/structure progression (needs spawn and loot audit) |

### Subsystem coverage at a glance

| Subsystem / branch | Mechanics evidence | Progression completeness | In-game guide/JEI reference | Client validation | Next action |
| --- | --- | --- | --- | --- | --- |
| Codex and onboarding | CodexItem, CodexChapters, lang/en_us.json at release-aligned source | Main navigation and first sign barter mapped | Codex categories | not run | Verify pages/GUI at pack client |
| Wicked/Sacred reputation and altars | Deity, DarkDeity, LightDeity, PrayerSpell, AltarInfo | Both stage ladders mapped | Rituals and Theurgy | not run | Verify altar data maps, costs and triggers |
| Signs, chants and mana | Signs, Spells, KnowledgeUtil | Core sign inventory and spell list mapped; cast economics partial | Signs and Spells | not run | Audit each spell cost, prerequisites and effect |
| Crucible, Worktable, Brazier and Wooden Stand | tile entities and recipe serializers | Operating loops mapped; every recipe/cost not audited | Artifice, Rituals, JEI | not run | Source-map all station recipes and failure/remainder behavior |
| Research Table | Researches and Codex research entry | Research explicitly WIP; only selected studies registered | Artifice → Research Table | not run | Confirm task generation and finalization at client |
| Soul equipment, armor, Curios and gear | item/tile/entity sources; Curios capability | Partial | Artifice and JEI | not run | Inventory every effect, recipe, slot and balance ceiling |
| Worldgen, structures and creatures | generated worldgen, structure data and source entities | Structure roster mapped; rates/loot partial | Nature, Monsters, Ores | not run | Inspect spawn/biome settings and pack worldgen overlap |
| Current TMM quest | config/ftbquests/quests/chapters/eidolon_repraised.snbt | One optional manual task only | None in chapter | not run | Knowledge only; no quest edits |

## Codex onboarding — required before the first complex step

- **Guide and access:** The Codex item is the in-game guide. Craft it shapelessly from a vanilla book and rotten flesh. It opens the Codex UI directly and can be placed on a Lectern.
- **Earliest stage:** First book and rotten flesh; no structure, dimension or boss prerequisite.
- **First instructional quest:** Teach Codex recipe/opening; then drop it for a Witch or Cleric to collect so the appropriate patron sign is added to that Codex and learned when the player retrieves/handles it.
- **Guide map:** Six categories: Nature, Rituals, Artifice, Theurgy, Signs and Spells. Source chapters include creatures, ores, pewter, plants, research, altars, altar lighting/skulls/herbs, effigy, goblet, censer, dark prayer, sacrifices, healing, holy touch, sign explanations, ritual recipes and station instructions.
- **Important guide caveat:** Codex says Research Table is a work in progress and only the Stray/Wraith and Blaze research paths currently work. Do not build the chapter as if all planned research exists.
- **Other help:** JEI plus Codex pages for crafting, worktable, Crucible, ritual, entity, smelting and sign details. GUI and recipe transfer not tested in TMM.

## Systems and full mechanics inventory

| Subsystem | Access and inputs | Effect / outputs | Limits, failure and evidence |
| --- | --- | --- | --- |
| Codex/sign knowledge | Craft and open Codex; trade it to Witch or Cleric | Adds the Wicked or Sacred sign to the Codex item; taking/handling the signed Codex grants that sign to the server player through item inventory tick | Codex is per item; the sign grant is a player capability. Witch and Cleric barter is source-confirmed in Events.java. |
| Patrons and reputation | Effigy near an altar; perform deity-linked prayer chant; altars can be decorated with recognized entries | Independent Dark and Light reputation values; milestones unlock spells, signs, facts or other knowledge | Both can progress simultaneously per README. Altars are connected horizontal runs of same altar-block type; items on top contribute data-map-defined power/capacity. |
| Dark patron ladder | Wicked sign; dark prayer; rites/research | At 3 reputation: mob sacrifice gate requiring its research; 10: Dark Touch; 15: villager sacrifice plus research; 35: zombify villager plus research; 50: enthrall undead plus research; maximum 100 | Major stages require named research/facts; not just raw reputation. Dark unlocks Soul, Mind, Harmony, Warding and Magic signs through stage callbacks. |
| Light patron ladder | Sacred sign; light prayer/incense; rites/research | At 3: basic incense prayer; 10: Light Touch; 15: heal villager plus research; 35: cure zombie plus research; 50: smite undead plus research; maximum 100 | Light unlocks Soul, Mind, Death, Warding and Magic signs through callbacks. Separate from dark reputation, not an exclusive faction. |
| Prayer / altar mana | Cast prayer before an Effigy with altar in range | Adds reputation based on base reputation plus altar power × configured multiplier; also updates patron mana and maximum | Spell-level cooldown and altar values matter. A valid prayer uses the nearest Effigy in the search box; missing effigy, inadequate patron reputation or cooldown blocks it. |
| Altar build | Wooden or Stone Altar blocks, icon above, optional occult objects above connected altar tiles | Adds power/capacity to linked altar info; power influences prayers/sacrifices and capacity limits imbued magic | Codex says altar shape/size is flexible; same-block horizontal connected run is detected. For each light/skull/herb category only the highest power/capacity entry counts, not additive stacking. Exact item table is in data maps. |
| Signs and Chants | Codex sign page/knowledge, spell recipe or registered spell; cast a known sequence | Spells can pray, attack, heal, sacrifice, transform, enthrall, summon, enchant/reinforce, or manipulate targets | Eleven signs in source: Wicked, Sacred, Blood, Soul, Mind, Flame, Winter, Harmony, Death, Warding and Magic. Cast cost/delay is per spell and some unlock on deity/research events. |
| Wand charges | Craft a wand and use its cast action; recharge through a matching rite | Fires spell projectiles and can use optional Apotheosis wand affix handling | Wand implementation uses server-side projectile creation and durability/recharge. Exact costs, charges and affix effects vary by item. |
| Crucible | Crucible over a lit campfire; fill with 1,000 mB source water; drop recipe items inside and stir by empty-hand interaction or redstone | Recipes consume items in sequential steps; valid final sequence yields a result | Timed mode default; first step timer is half configured duration (50 ticks at default 100), later unmatched steps reset to 100 ticks. Without required stirs/inputs it fizzles and drains water. Turn-based option avoids fizzle while arranging recipe; backoff default 40 ticks. |
| Worktable | Use the Eidolon Worktable with a 3×3 core pattern plus separate reagent positions | Special shaped crafting including altar, effigies and ritual equipment | Recipe serializer has independent core/outer patterns; remainder items are returned. JEI visibility and automation not client-checked. |
| Brazier rituals | Insert one ritual focus item; ignite with Flint and Steel; satisfy ritual setup/step requirements | Crafting rituals, timed/world rituals, summoned entities, item results and world effects | Brazier consumes focus when recipe is matched, checks ritual setup after 40 ticks, then advances multi-step requirements. Failed setup extinguishes; shift + empty hand extinguishes manually. |
| Wooden Brewing Stand | Four-slot custom stand; water potion bottles plus ingredient and heat | Eidolon-specific brewing and potion effects | Operation uses custom container and heat state; exact recipe list and fuel/automation not completed. |
| Research | Craft Note Taking Tools; use on a registered subject; place resulting Research Notes in Research Table; complete generated tasks and apply an arcane seal | Learns a Research; select studies grant additional sign/knowledge | Code registers Gluttony from Pig (3 stars), Flames from Blaze (5 stars) and Frost from Stray/Wraith (5 stars). Codex explicitly says the system is WIP and only Blaze/Stray/Wraith research works. Task pool mixes Scribe Items and XP; tasks are seeded/randomized. |
| Soul Enchanter | Apply item with stored soul resource to Soul Enchanter | Applies enchantments using souls | Config defaults to unlimited uses and unlimited enchantment count (-1); max ethereal health default 60 half-hearts. Full soul input/output and exact recipes remain open. |
| World generation and structures | Overworld worldgen | Lead/Silver ore; Illwood tree; Catacomb, Lab and Stray Tower structures; slugs, ravens and hostile Eidolon mobs | Source files define structures/biome tags and placed features; TMM/Tectonic rates, loot pools and spawn gates require assembled-pack review. |
| Gear and Curios | Pewter/silver/lead crafting, rituals and monster drops | Armors, wands, scythe/axe, amulets/belts/rings, Raven Cloak, robes and other utility gear | Several older design items remain explicitly not craftable in the upstream README, while newer source adds items; validate actual pinned recipe roster, not old promotional list. |

### Material and capability input-output contracts

| System | Input kind and source | Output kind | Trigger / cost / limits |
| --- | --- | --- | --- |
| Reputation prayer | Player sign sequence, nearest Effigy, connected decorated altar, deity and cooldown state | Player-specific deity reputation plus patron mana | Configured baseRep + powerMultiplier × altar power; prayer timestamps saved per deity-linked spell |
| Altar entries | Block items above connected altar run, via altar-entry data map | Power/capacity numbers | Highest applicable power/capacity per altar category contributes; not a generic block or FE input |
| Crucible | Water source (1 bucket/1000 mB), item entities inside Crucible, stir count, lit campfire below | Recipe item stack | Item entities are collected server-side every 8 ticks while boiling; items consumed into step record; timed or turn-based step processing |
| Worktable | Shaped core items plus reagent-grid items; items with crafting remainders | Crafted ItemStack and retained remainders | No fluid/mana input established in serializer |
| Brazier | One item focus plus registered RitualRecipe; Flint and Steel; world/entity/setup requirements | Ritual-specific item(s), summoned entities, or world/entity effect | Step setup and invariants are ritual-specific; not a universal crafting recipe |
| Research Table | Research Notes data component, randomized item/XP tasks and matching player progress | CompletedResearch item, then player knowledge | Notes require each task; completion plus arcane seal grants research. Only registered research above confirmed in Researches.java |
| Soul mechanics | Entity soul/research state and Soul Enchanter inputs | Soul resource, ethereal health and enchantment changes | Exact extraction and enchantment economics still need source-to-runtime mapping |
| Other currencies | No Source, Blood, EV, EMC or FE bridge established | — | Keep these resources distinct absent explicit API/compatibility evidence |

## Acquisition, renewability and progression economics

- Codex is early and cheap: one book plus one rotten flesh. The primary entry gate is finding a Witch or Cleric villager and obtaining the sign barter.
- Pewter uses lead-based alloy crafting and is described by Codex as an inert structural metal for magical constructs. Lead and silver are generated ore veins in source.
- Patron reputation is repeatable through prayer, but cooldown and altar power govern rate; the Codex says once per day for each prayer spell. Source config defines a cooldown floor.
- Altar light/skull/herb entries choose category maxima, not additive stacking. Quest/build instructions must not tell players to stack duplicate candle/skull classes for linear benefit.
- Research tasks include XP and tagged Scribe Items; the random task selection is seeded by world/research/progress. Exact task usability and seal acquisition remain untested.
- Crucible transformations use item recipes and water/campfire operation; it is not an EMC machine or an arbitrary item transmuter.

## Complete player progression / walkthrough blueprint

| Step | Gate or branch | Prerequisite and player action | Result / teaching target | Evidence and remaining gap |
| --- | --- | --- | --- | --- |
| 0. Craft/read Codex | Required onboarding | Book + Rotten Flesh; open Codex | Learns six categories and guide navigation | Recipe and CodexItem source; no client validation |
| 1. Obtain first sign | Choose Witch/Wicked or Cleric/Sacred; both may be pursued | Drop unmarked Codex near Witch or Cleric villager; retrieve signed item | Corresponding sign is granted by server inventory tick | Events.java; confirms class/behavior, exact pathing success untested |
| 2. Build first wooden altar and straw effigy | Required for prayers | Three Wooden Altars from slabs/planks; wheat Straw Effigy; place altar run, icon/effigy | Valid prayer focus, decorative altars enhance values | Source recipe and Codex; altar datamap values not fully indexed |
| 3. Perform first prayer | Separate Dark and Light branches | Use relevant triple-sign prayer sequence before an effigy; meet cooldown and reputation | Gain patron reputation and mana | Spells.java, PrayerSpell; action/task detector untested |
| 4. Reach 3/10/15/35/50 reputation gates | Path progression, with two independent deity tracks | Prayer plus each newly learned rite/research; build power/capacity | Unlock signs/facts/spells per deity callbacks | Deity source; sequence requirements mapped, exact task instruction/reward not written |
| 5. Craft Pewter and worktable devices | Required for multiple items | Alloy Pewter Blend and use Worktable core/reagent patterns | Build Stone Altar, effigies, other occult devices | Source recipe roster; full materials/rates incomplete |
| 6. Use Crucible and Wooden Brewing Stand | Choose recipes as needed | Add water and campfire; add/stir step recipe, or use stand/heat for potion recipe | Item and potion outputs | Station serializers and partial tile sources; recipe-by-recipe map open |
| 7. Perform Brazier rite | Recipe-dependent branch | Place item focus, ignite, meet ritual conditions and preserve invariants | Ritual item, summoned entity or world effect | 20 registered ritual recipe files identified; input/output audit incomplete |
| 8. Research a subject | WIP optional branch; not a full core gate | Note-taking item on registered subject; do tasks in Research Table; apply seal | Research/knowledge; limited spell sign reward | Current source only registers Pig, Blaze, Stray and Wraith studies; Codex explicitly labels this incomplete |
| 9. Pursue gear, soul magic and creatures | Optional branches | Follow Codex/JEI for weapon, armor, Curios, familiar/creature and soul systems | Combat, utility and equipment effects | Full item/recipe/effect matrix pending |
| 10. Completion | Open-ended | Choose patron, crafting, spell, creature or equipment goals | No single terminal milestone found | Both patron paths can coexist; no pack chapter teaches any stage |

**Progression graph:** Codex → choose or combine patron sign routes → altar/effigy → independent patron reputation ladders → signs/spells and rite knowledge. Worktable, Crucible, Brazier, brewing, research, world structures and gear are parallel branches, not one linear magic-tech tier chain.

## Structures and operating how-to

| Setup | Player-facing steps | Failure/maintenance |
| --- | --- | --- |
| Altar + effigy | Connect a run of the same altar block horizontally; place an icon and recognized embellishments above; add Effigy; cast prayer in range | Same-block connected component is the detected altar. Missing Effigy, no reputation or active spell cooldown prevents prayer. |
| Crucible | Place over a lit Campfire, add a water bucket, drop only recipe items, stir each prescribed step by empty hand or redstone | Default timed recipes can fizzle when sequence/timing wrong; turn-based config gives arrangement time but remains recipe strict. |
| Worktable | Place core 3×3 layout and reagent inputs separately | Check both pattern areas; catalysts/remainders may be returned, so do not infer all reagents consume. |
| Brazier | Insert item focus, ignite with Flint and Steel, wait through setup and follow each ritual step | Unsupported focus does not match; invalid setup extinguishes. Shift + empty hand can extinguish intentionally. |
| Research Table | Start a research with Note Taking Tools, place notes, complete shown tasks, apply arcane seal | Only current registered studies should be presented as working; Codex warns the feature is WIP. |

## Internal combinations and overlooked mechanics

| Combination | Benefit | Limitation / evidence |
| --- | --- | --- |
| Altar decoration + prayer | Higher altar power raises reputation gain; capacity can empower/imbue | Per-category maxima, not additive duplicates; altar entry datamap defines eligible blocks |
| Sign learning through Codex barter | A compact, early entry into dark or holy magic | The barter signs the Codex as an item; a FTB check for holding a Codex cannot prove a sign was learned |
| Patron milestones + new knowledge | Reputation stages reward a guided loop of try-new-rite/use-new-sign | Major gates have research requirements; raw reputation alone is insufficient |
| Research notes + random tasks | Extends Codex with item/XP research tasks | Only three research definitions and their source pages are supported; feature still WIP |
| Crucible + redstone stir | Can automate triggering a manual stir step | Recipe still requires correct inputs and step order; redstone is not a generic completion signal |
| Optional Apotheosis compatibility | Wand affix hook can add projectiles/tracking at cast | This changes combat ceiling; actual effects/config and runtime integration need testing |

## Low-hanging setups useful in ordinary survival

### 1. First Codex and sign — early
- **Goal:** Learn how the Codex and a patron sign begin spell access.
- **Minimum entry:** One book, one rotten flesh, and a Witch or Cleric villager.
- **Steps:** Craft/open Codex; drop it near Witch for Wicked or Cleric for Sacred; let the NPC retrieve it; recover Codex and confirm the sign was added.
- **Limits:** Barter is an entity AI goal, not a guaranteed direct click trade; pathing/chunk behavior can affect timing.
- **Evidence:** Recipe, Codex item and Events.java source; no TMM client test.

### 2. Altar prayer — early
- **Goal:** Establish reputation and patron mana.
- **Minimum entry:** Relevant sign, altar blocks, straw effigy and valid prayer sequence.
- **Steps:** Place altar run, set an icon/effigy; cast the triple-sign prayer near it; inspect Codex/reputation feedback; learn and use new rites as the patron asks.
- **Limits:** Default prayer cooldown is 21,000 ticks; decor maxima are not additive.
- **Evidence:** PrayerSpell/Deity source and Codex text.

### 3. Pewter worktable craft — early/mid
- **Goal:** Access special occult construction recipes.
- **Minimum entry:** Lead-derived Pewter Blend and Worktable recipe materials.
- **Steps:** Smelt or craft Pewter per JEI; open Worktable; use the core pattern plus separate reagent slots; craft a later altar/equipment item.
- **Limits:** Exact recipe chain and returned remainders vary; catalog still incomplete.
- **Evidence:** WorktableRecipe serializer and generated recipe data.

## API, integration and pack-config surfaces

| Surface | Capability / TMM state | Evidence |
| --- | --- | --- |
| Player capabilities | Separate knowledge, mana, soul and patron reputation data | IKnowledge/IReputation/ISoul and capability implementations |
| Codex barter events | Witch and Cleric barter Codex items to attach Wicked/Sacred sign data component | event/Events.java, GenericBarterGoal |
| Altar data maps | Datapack-style AltarEntry map defines power/capacity for recognized blocks | AltarInfo, AltarEntries |
| Recipe serializers | Worktable, Crucible, Ritual, Chant/Command Chant and brewing | recipe classes, generated resources |
| Spell config | Per-spell delay, cost, and prayer cooldown/reputation/power multipliers | Spell.buildConfig and prayer configs |
| Common config | Crucible duration/mode/backoff; ethereal health; Soul Enchanter application limits | Config.java |
| Curios | Required upstream; many Eidolon items use accessory slots | README and item source |
| Apotheosis compatibility | Wand code checks compatibility handler before applying wand affixes | source optional integration; TMM gameplay effects untested |
| Pack override | No targeted Eidolon KubeJS or recipe override found in reviewed paths; feature integrations may exist elsewhere | repo searches not exhaustive |

## Compatibility leads and issues

| Other system | Link / status |
| --- | --- |
| Curios | Required dependency and native accessory integration; pack includes Curios |
| Apotheosis | Conditional wand projectile/tracking affix path detected in source; test interaction, not a demonstrated bug |
| Ars Nouveau, Iron's Spells, ProjectE, Neo Vitae, Draconic Evolution | No shared mana/energy conversion established; keep currencies separate |
| Worldgen/Tectonic | Eidolon places lead/silver ore, Illwood, Catacombs, Lab and Stray Tower; actual biome/spacing and spawn pools should be checked in a generated world |
| Research system | Upstream Codex explicitly says work in progress and limits working research to Blaze, Stray and Wraith; do not promise a complete study progression |

**Configuration discrepancy:** Source PrayerSpell.buildConfig declares cooldown default 0 with a minimum of 21,000 ticks, while PrayerSpell.getCooldown falls back to 21,000. The prior TMM server-generated dark/light prayer TOML records the effective value as 21,000 and comments that the default is 0/range is greater than 21,000. The configuration therefore enforces the Codex's one-day cadence in generated pack config, but the source default and minimum disagree. This is a config-data issue, not evidence that casting was runtime-tested.

**Soul-enchant balance lead:** common config defaults maximum Soul Enchanter uses and maximum enchantments per item to -1 (unlimited). Review desired TMM ceilings before release; no override was implemented or changed here.

## Quest and existing chapter reconciliation

| Stage | Current quest | Truthful detector | Gap |
| --- | --- | --- | --- |
| Entire mod | config/ftbquests/quests/chapters/eidolon_repraised.snbt: one optional checkmark, 2 quest XP, one paper | Manual checkmark only | No Codex, sign, altar, prayer, station, research, structure or gear instruction; quest file untouched |

Codex pages can teach actual setup, but holding the book or item is not proof of reading or performing the mechanic. Use sign/research advancements only after confirming the native task IDs and FTB detector support. No quests or runtime content changed.

## Conflicts, limitations and version drift

- Pack pins CurseForge artifact file 9332315 with filename ending 0.5.0.8 and SHA-1. CurseForge page metadata labels the release 0.5.0.7 while the attached artifact and 1.21.1 source branch declare 0.5.0.8. The pack pin is artifact-specific; don't use the displayed page label alone for version matching.
- Release-aligned source branch head declares MC 1.21.1 and mod version 0.5.0.8, but pinned JAR bytes were not available for hashing against this source snapshot.
- The upstream README states holy and dark paths can both be taken. Older descriptions that frame one as an exclusive branch are not accurate for this port.
- Research is explicitly unfinished and only limited studies are registered.
- Default prayer cooldown min/default discrepancy and Soul Enchanter unlimited config are noted above.
- No pack-side quest or runtime changes were made.

## Evidence ledger

| Claim | Exact source | Evidence / limit |
| --- | --- | --- |
| EI-001 Pin | mods/eidolon-repraised.pw.toml; CurseForge file 9332315 | Pack filename/hash and file ID; page labels 0.5.0.7 although artifact filename is 0.5.0.8 |
| EI-002 Source version | [1.21.1 branch commit 85484f2804bcff82abb3397d8f384422d7e21898](https://github.com/Alexthw46/Eidolon-Repraised/commit/85484f2804bcff82abb3397d8f384422d7e21898); gradle.properties | Declares Minecraft 1.21.1 and version 0.5.0.8; binary equivalence unverified |
| EI-003 Codex recipes/pages | CodexItem.java; recipe/codex.json; CodexChapters.java; assets/eidolon_repraised/lang/en_us.json | Guide access and topics; client rendering not tested |
| EI-004 Barter/signs | event/Events.java; GenericBarterGoal.java; Signs.java | Witch/Cleric accept unsigned Codex, attach Wicked/Sacred; player capability grant occurs on inventory tick |
| EI-005 Reputation/altar | DarkDeity.java, LightDeity.java, PrayerSpell.java, AltarInfo.java, AltarEntries.java | Tier IDs/values, altar computation and configured gain; client/behavior untested |
| EI-006 Crafting stations | CrucibleTileEntity.java, WorktableRecipe.java, BrazierTileEntity.java, WoodenStandTileEntity.java | Source operation and serializer contracts; all recipe outputs not audited |
| EI-007 Research | Researches.java, ResearchTableTileEntity.java, ResearchTask.java, Codex text | Only Gluttony/Pig, Flames/Blaze and Frost/Stray/Wraith source definitions; Codex warns WIP |
| EI-008 World generation | generated placed features and worldgen structure, template pool, biome-tag resources | IDs and structures exist; pack-world rates/biome placement untested |
| EI-009 Prior server config | prior generated config/eidolon_repraised/dark_prayer.toml and light_prayer.toml; server-final.log mod roster | Shows effective config 21,000 and loaded mod label 0.5.0.8 in a previous server candidate; not a cast/world gameplay test |
| EI-010 TMM chapter | config/ftbquests/quests/chapters/eidolon_repraised.snbt | One checkmark placeholder; not edited |
