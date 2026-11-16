# Feywild — mechanics and gameplay profile

| Field | Value |
| --- | --- |
| Manifest ID / actual mod ID | `feywild` / `feywild` |
| Category / parent or addons | Magic adventure; no Feywild addon selected in the current manifest |
| MC / loader / pack target | 1.21.1 / NeoForge / TMM `test/full-integration` |
| Pinned version / artifact / hash | `feywild-1.21.1-5.6.4.jar`; CurseForge project 484377, file 9321431; SHA-1 `e59b68dd85b92ebbdbde478ea4e52fdb8b84c9d0` |
| Source snapshot | [`2a339f98a5a2ea0d5620801538095a7b013b6de2`](https://github.com/SaphieNyako/Feywild/commit/2a339f98a5a2ea0d5620801538095a7b013b6de2), source `gradle.properties` declares 5.6.4 / MC 1.21.1 / NeoForge 21.1.169. Release date and metadata align; JAR-to-source identity has not been proved. |
| Dependencies in TMM | Patchouli `1.21.1-93-NEOFORGE`; Quest Giver `1.21.1-1.5.3` (both pinned; no runtime interaction test) |
| Guide/documentation version gap | The official GitHub wiki's “How to get started” explicitly covers Feywild 5.0, not 5.6.4. Use the 5.6.4 source lexicon/recipes where inspected; retain the wiki only as a historical lead. |
| Profile status | researching |
| Evidence reached | Exact pack pin; official 5.6.4 CurseForge release page; source snapshot at the release date; source recipes, lexicon text, config and Quest Giver data inspected. No client/world test. |
| Reviewed | 2026-11-16 |
| Quest ownership | No TMM quests edited. The mod itself bundles Quest Giver quest data. |

## Executive gameplay map

**What the player actually does:** Feywild is a companion-summoning and exploration mod. The player obtains Fey Dust and Fey Gems, makes Fey Ink and a summoning scroll, then uses a Fey Altar to turn the scroll and four themed offerings into a scroll that summons a specific Fey. A summoning scroll used on a block summons an owned companion; a Pixie Orb opens its menu to toggle abilities, set follow/stay behavior, or dismiss it back into its scroll. Seasonal Pixies provide practical animal-breeding, crop-growth, crop-harvest and animal-drop abilities. Other Fey add combat, trades, transformations, resource and story branches. The Lexicon and JEI are intended references; the four seasonal Pixies also have native quest stories. [source: `2a339f9`, release line]

**Why it matters in TMM:** The clearest near-term utility is farming with a companion, while the wider mod adds an independent Fey bestiary, themed gear, structures/blocks, combat encounters and story progression. Its Fey Altar is a dedicated five-input, recipe-matched interface rather than an ordinary crafting grid. Treat Feywild as its own progression chapter; its built-in Quest Giver story content is not a TMM FTB Quests implementation.

**Quick start:** The source config defaults `spawn_with_lexicon=true`; first check inventory for the Feywild Lexicon. Otherwise the exact 5.6.4 recovery recipe is not yet confirmed. Use the Lexicon's “Fey Gems” and “Summoning Fey” sections and JEI to follow the current recipes. The first useful summon is a seasonal Pixie, but first obtain Fey Dust, a Fey Gem, Fey Ink and an empty scroll, craft/place a Fey Altar, and offer the matching recipe's five items.

### Player-value snapshot

| Five standout capabilities | Three genuinely early practical unlocks | Three candidate integrations |
| --- | --- | --- |
| Four toggleable seasonal Pixie abilities; owned summon/dismiss loop; Fey Altar recipes; Shroomling mushroom trades/variants; Arch Fey encounters and flight tiaras | Fey Dust from a peaceful cookie offer (chance-based); Fey Dust and Fey Gem from underground Fey Ore; Spring/Summer/Autumn Pixies for breeding or crop work once the altar chain is affordable | Vanilla animal/crop systems; Curios for tiara slot compatibility; Supplementaries jars/cages for compatible Fey (official page says some Fey, from 5.6.3+) |

### Subsystem coverage at a glance

| Subsystem / progression branch | Mechanics evidence | Progression completeness | In-game guide/JEI reference | Client validation | Blocker / next action |
| --- | --- | --- | --- | --- | --- |
| Lexicon / onboarding | Config defaults to spawn with a Lexicon; Patchouli guide declared | Partial | Feywild Lexicon; exact page availability checked in source text, not UI | not run | Confirm grant behavior and recover/crafting route in pinned client |
| Fey Dust and Fey Gems | Lexicon text, item code, source recipes | Partial | Lexicon: “Fey Gems”; JEI | not run | Confirm exact ore worldgen/drop values and recipe reversibility |
| Fey Altar / summoning scrolls | Exact 5.6.4 recipe JSON and altar code | Partial; seasonal and several intermediate/Arch Fey recipes | Lexicon: “Summoning Fey”; JEI Fey Altar category | not run | Inspect all altar recipes, physical altar recipe, input failure and source consumption behavior |
| Seasonal Pixies / Pixie Orb | Exact lexicon ability text; wiki supports menu controls but is 5.0 | Partial | Lexicon: “Pixies” / “Handling Fey”; JEI | not run | Verify 5.6.4 menu, range, cooldown, area and resource limits |
| Mandragora, Bee Knight, Bellsnickel, Shroomling, Tree Ents | Modrinth roster, recipe files, partial lexicon; Quest Giver callbacks in summon code | Roster/recipe partial | Their Lexicon entries and JEI | not run | Map each complete recipe, ability, trade, behavior and summon condition |
| Sovereigns / Arch Fey and wing tiaras | Lexicon describes four courts and tiara drops; source has sovereign scroll recipes | Partial | Lexicon: “Sovereigns” / “Pixie Wings”; JEI | not run | Establish encounter triggers, combat/loot rules, crafting/use of each tiara |
| Seasonal Pixie Quest Stories | Source Quest Giver data contains a sprite root and numbered tasks; official page confirms seasonal questlines | Partial; only the sprite introduction chain was mapped here | Feywild Lexicon prelude + built-in sprite dialogue | not run | Inventory all four full quest lines and actual story triggers |
| World generation / building / decor | Official roster and Lexicon locale contain themed content | Roster only | Lexicon entries / JEI | not run | Inventory dimensions/biomes, blocks, structures and acquisition routes |

## Guidebook onboarding

- **Guide name/access:** Feywild Lexicon (Patchouli). In source config `feywild.spawn_with_lexicon` defaults to true. The mod's public page says Patchouli supplies the guide and describes it as a prelude to the Quest Stories. The current runtime's grant behavior and a recovery/crafting recipe for players who lack it remain untested.
- **Earliest attainable stage:** The config implies the book is intended at spawn, before Feywild materials. Treat this as an expected default, not verified pack behavior.
- **First reference:** Lexicon sections include “Feywild,” “Fey Gems,” “Summoning Fey,” “Pixies,” “Sovereigns,” and “Pixie Wings.” The source locale provides the first-step instructions for dust, gems, the altar, handling Fey and Pixie abilities.
- **JEI:** The old wiki says all recipes are visible in JEI. The pinned-source recipe JSON confirms a custom `feywild:fey_altar` recipe type plus ordinary crafting recipes; confirm the category's display in the installed JEI client.
- **Guide gap:** The official “How to get started” wiki is expressly for Feywild 5.0. Its recipe sequence is useful as a lead, but cannot stand in for recipes/pages from the pinned 5.6.4 JAR. The source-backed profile below uses exact source data only when identified.

## Systems and mechanics inventory

| Subsystem | How accessed | Inputs + costs | Effects / outputs | Limits, failure conditions and source |
| --- | --- | --- | --- | --- |
| Fey Dust | Fey death; cookie interaction with untamed Fey; smelt a Fey Gem; source item interaction | Cookie is offered to wild Fey; exact yield probability and consumption rules not inspected. Source Lexicon says Fey leave dust on death and cookies may be peaceful; source item code confirms dust has an edible/effect interaction. | Fey Dust item; also used as Fey Gem ingredient and Fey Ink/altar material | The Lexicon says Fey Ore can be found deep underground and Fey Gems smelt to dust. Exact ore generation and drop counts unknown. |
| Fey Gem | Mine Fey Ore; craft four Fey Dust in a 2×2 pattern (source recipe) | 4 `feywild:fey_dust` → 1 `feywild:fey_gem`; source recipe is shaped | Fey Gem for altar scroll recipes and other Fey crafting | Lexicon says dust condensation has ingredient loss; exact conversion is 4:1 in the source recipe. Ore block output details not fully audited. |
| Mandrake / Fey Ink | Mandrake item plus ink sac and glass bottle | Old 5.0 wiki: Fey Dust + Poisonous Potato crafts a Mandrake Root, planted on farmland and grown with bone meal; ink sac + bottle + Mandrake makes Fey Ink. The exact current recipe JSON has not yet been located in 5.6.4 source. | Mandrake is a scroll-writing ingredient; Fey Ink Bottle is used with paper and feather | Source Lexicon warns that growing/handling mandrakes is loud. Keep the recipe details labeled as older documentation until exact pin reconciliation. |
| Empty summoning scroll | Shapeless crafting | 1 Fey Ink Bottle + 1 Feather + 1 Paper → 1 `feywild:empty_summoning_scroll` | Base item in the seasonal and other known altar recipes | Exact 5.6.4 generated recipe inspected. |
| Fey Altar | Lexicon says crafted in crafting table; custom block/entity has five input slots and output slot | A recipe requires five matching ingredient stacks; source recipe matching treats the five ingredients as an unordered list. | Produces the matching Fey scroll; source block entity has a 40-tick progress value and stores one result slot | No source support yet for power/fuel use; none is visible in the inspected altar entity. Exact item-removal timing, GUI automation and altar block recipe remain to inspect. |
| Fey summoning | Use a Fey's scroll on a block | Consumes the scroll on successful use (Lexicon); source code places the Fey at the clicked block + 1 and assigns player ownership/summon position | Spawns the selected Fey as an owned companion | Source callback reports several summon events to Quest Giver. Exact failure and duplicate-summon behavior not tested. |
| Pixie Orb | Right-click summoned Fey | Old wiki reports an altar recipe using 2 Ender Pearls, 2 Fey Gems and Fey Dust; current recipe JSON not inspected | Menu toggles ability, follow/stay, or dismisses Fey back into its scroll | Validate recipe and UI against the pinned build. Dismiss behavior is documented in the Lexicon source for Fey handling. |
| Seasonal Pixies | Summon with corresponding scroll | Seasonal scroll recipes each consume a Fey Gem, empty scroll, and three season-themed offerings | Spring breeds nearby eligible animals; Summer grows nearby crops; Autumn harvests crops; Winter collects animal drops (Lexicon text) | Exact areas, rates, cooldowns, eligibility and item collection destinations are unknown. Old wiki is not the source for these effect details. |
| Shroomling | Summon with its scroll; interact/trade | Exact source altar recipe: Autumn Pixie scroll, Fey Gem, Red Mushroom Block, empty scroll, Shroomlight | Lexicon says it can trade mushrooms, stews and potions for a Fey Gem; spores can turn a cow into a variant Mooshroom; its variant affects harvest/stew | The text describes trades and variant-specific mechanics. Exact inventory of trades, timers, range and variant conditions require more source/runtime review. |
| Fey courts / Sovereigns | Progression toward Arch Fey through Fey-specific scrolls/encounters | Known source recipes use a seasonal Pixie scroll, Fey Gem, an intermediate Fey scroll, empty scroll, and same-season Tree Ent scroll for Oberon, Queen Mab and Ashen Lord; full recipe list still being checked | Four court rulers/Arch Fey; source Lexicon says defeated Arch Fey drop a Fey Crown/Pixie Wing Tiara used to craft seasonal wings | The Lexicon names Queen Titania as Summer ruler, Queen Mab as Winter, Prince Oberon as Spring and Ashen Lord as Autumn. How encounters are triggered, how Titania's route differs, and exact drops/crafting outputs are incomplete. |
| Fey Dust edible/effect interaction | Use dust on a living entity / consume | Source `FeyDustItem` applies Levitation on entity interaction and consumes one outside Creative; duration is configurable, default 30 ticks (1.5 s) | Levitation effect; Quest Giver special callback when used on a sheep | This is a source observation, not a recommended food use. The sheep callback uses a hard-coded quest event `fey_dust_sheep`. |
| World generation and hostiles | Feywild biomes/overworld content; config-backed spawn weights | Common config has spawn weights (default 2 for each seasonal Pixie; 3 Mandragora/Bee Knight; 5 Bellsnickel/Shroomling), each configurable 0–1000; zero disables natural spawning | Fey Ore and varied Fey/mobs/trees/flowers | Exact biome placement, spawn conditions and Tree Ent trigger probabilities need inspection. Config also sets Tree Ent attack damage 12.0, sapling cracked-log chance 1/1200, natural-world chance 1/100, and whether Tree Ents attack players. |

### Pinned-source Fey Altar recipes inspected

The seasonal Pixie recipes in `src/main/resources/data/feywild/recipe/` each consume the following five item/tag ingredients and output one scroll. JEI should be used for the actual altar interaction order and current recipe names.

| Output | Five ingredients (exact item IDs/tags) |
| --- | --- |
| `feywild:summoning_scroll_spring_pixie` | `feywild:fey_gem`, `minecraft:flowers` tag, `minecraft:cake`, `feywild:empty_summoning_scroll`, `minecraft:wool` tag |
| `feywild:summoning_scroll_summer_pixie` | Fey Gem, `minecraft:honeycomb`, `minecraft:golden_sword`, empty scroll, `minecraft:melon` |
| `feywild:summoning_scroll_autumn_pixie` | Fey Gem, `c:mushrooms` tag, `minecraft:hoes` tag, empty scroll, `minecraft:pumpkin` |
| `feywild:summoning_scroll_winter_pixie` | Fey Gem, `minecraft:snowball`, `minecraft:rotten_flesh`, empty scroll, `minecraft:bone` |
| `feywild:summoning_scroll_mandragora` | Spring Pixie scroll, Fey Gem, `minecraft:flowering_azalea`, empty scroll, `feywild:mandrake` |
| `feywild:summoning_scroll_shroomling` | Autumn Pixie scroll, Fey Gem, `minecraft:red_mushroom_block`, empty scroll, `minecraft:shroomlight` |
| `feywild:summoning_scroll_bee_knight` | Summer Pixie scroll, Fey Gem, `minecraft:saddle`, empty scroll, `minecraft:bee_nest` |

Other named recipes in the source include Bellsnickel, Tree Ent variants and sovereigns. Their ingredient files and exact full ordering have not all been inventoried here; this table is not the complete recipe roster.

### Material / capability input-output contracts

| System | Input kind + exact ID/tag/event | Input qty/formula; consumed/remainder | Output kind and location | Trigger/cadence/interface | Evidence |
| --- | --- | --- | --- | --- | --- |
| Fey Gem craft | Item stacks `feywild:fey_dust` | 4 dust, crafting grid | 1 Fey Gem item | Crafting recipe | Source generated recipe JSON |
| Empty scroll | Item stacks Fey Ink Bottle, Feather, Paper | One each; shapeless; output one scroll | Item in crafting output | Crafting recipe | Source generated recipe JSON |
| Fey Altar | 5 item stacks matched against 5 ingredients; custom recipe type | Exact recipe costs; matching order is irrelevant in source predicate; 40 progress ticks in block entity | Item in altar output slot | Player GUI/block entity | Source recipe class + block entity |
| Summoning | Scroll item + clicked block + player | Scroll disappears on summon per Lexicon; code positions entity and attaches owner | Fey entity in world, owned by player | Item use on block | Source `SummoningScrollItem` + Lexicon |
| Natural spawning | Mob spawn weight config | Config defaults and bounds listed above; weight is not a guaranteed rate | Fey entity | Natural spawn system, exact biome/predicate unknown | Source config defaults |
| Quest Giver integration | Fey action / special task ID | No item cost established for callbacks; callbacks are separate from recipes | Native Quest Giver task state | Fey Dust on sheep and specific summon scrolls call `SpecialTask` IDs | Source classes `FeyDustItem` and `SummoningScrollItem` |

### Acquisition, renewability and output economics

- **Fey Dust:** the current source Lexicon describes Fey death, cookie offers to untamed Pixies, and Fey Ore as sources. Four Fey Dust craft one Fey Gem. Smelting one Fey Gem back to dust is described by the Lexicon, but exact smelting output has not been inspected in source here.
- **Fey Ore:** source Lexicon says it generates deep underground in the Overworld. Exact height, biome and Fortune/drop behavior remain unknown.
- **Companion loop:** a summoned Fey can be dismissed back into a scroll according to the Lexicon, so the same scroll can be reused after dismissal; whether that item retains entity state or has edge cases is unknown.
- No Fey-to-EMC, FE or other external energy output is established. Do not assume that living Fey, seasonal abilities or Fey Altar expose generic automation.

## Complete player progression / walkthrough blueprint

**Completion test:** Not met. The fundamental summon loop is substantially mapped, but the full Fey roster, Fey court boss paths, four seasonal story questlines, worldgen and end-state are still incomplete. No TMM quests are authored in this research pass.

| Path and step | Gate or choice? | Exact prerequisite / materials / world condition | Player actions | Unlock / visible success / next step | In-game guide or JEI reference | Coverage |
| --- | --- | --- | --- | --- | --- | --- |
| 0. Find the guide | Expected at spawn by default; config-backed | Lexicon item, `spawn_with_lexicon=true` default | Open Lexicon; if missing, locate current 5.6.4 recovery route | Access to Feywild guide and sprite prelude | Feywild Lexicon | Partial; verify actual grant |
| 1. Collect first Fey Dust | Several alternate sources; peaceful route is chance-based | Cookie + untamed Fey, combat with Fey, underground Fey Gem Ore, or cracked Fey Log / Tree Ent event | Offer cookie or use one of the hostile/mining alternatives | Fey Dust; required for first materials | Lexicon “Fey Gems”; JEI | Partial |
| 2. Obtain Fey Gem | Recipe or mine ore | Four Fey Dust for one Gem; or find Fey Ore in Overworld | Craft four dust; alternatively mine ore and inspect drops | Fey Gem for scroll recipes | Lexicon “Fey Gems”; JEI | Source recipe verified; worldgen partial |
| 3. Grow Mandrake | Exact current recipe unresolved | Old wiki says Fey Dust + Poisonous Potato; farmland and bone meal | Use current JEI recipe; plant and grow; avoid surprise scream | Mandrake ingredient | Lexicon “Summoning Fey”; JEI | Partial, pin recipe gap |
| 4. Make Fey Ink and empty scroll | Empty-scroll recipe is exact | Fey Ink Bottle (source recipe input); feather; paper | Craft Fey Ink using pinned recipe; shapeless craft empty scroll | One empty scroll | JEI: Fey Ink / Empty Summoning Scroll | Partial |
| 5. Build Fey Altar | Required for summoning | Fey Altar block; exact crafting ingredients not located in source inspection | Craft per JEI, place, open altar interface | Five ingredient slots + output | Lexicon “Summoning Fey”; JEI Fey Altar category | Partial |
| 6. Choose a seasonal court | Player choice, not mandatory to craft all four | Fey Gem, empty scroll, three season-specific offerings | Put five matching ingredients in altar; wait for recipe completion; take the scroll | Summoning scroll for a chosen Pixie | Lexicon “Pixies”; JEI exact scroll recipe | Recipes verified for all four; input operations need client |
| 7. Summon and manage companion | Optional choice by Fey | Corresponding scroll; a place to summon | Use scroll on block; use Pixie Orb on Fey to toggle skill/follow/stay/dismiss | Owned Fey; ability can support farm or combat loop | Lexicon “Handling Fey”; Pixie Orb JEI | Source summon semantics partial; UI/area untested |
| 8. Build intermediate Fey branches | Optional, several recipes rely on seasonal Pixies | Mandragora, Shroomling, Bee Knight and other named Fey scroll recipes | Use current Lexicon and JEI; craft scroll, summon, learn each ability/trade | Further combat, trade, variant and court routes | Fey Lexicon entries / JEI | Partial; only several recipes inspected |
| 9. Pursue each seasonal story | Four separate Quest Giver chains according to official page | Seasonal Pixie and appropriate in-mod event/objectives | Follow the Pixie’s built-in story content | Quest story progression; claimed court rewards unknown | Feywild Lexicon sprite prelude; Quest Giver UI | Partial; only numbered “sprite” intro chain inspected |
| 10. Challenge court rulers / Arch Fey | Major optional/endgame branch | Four courts and their Fey scrolls; precise encounter unlocks unknown | Complete the relevant court progression and fight its sovereign | Source Lexicon says each defeated Arch Fey drops a Pixie Wing Tiara | “Sovereigns” and “Pixie Wings” | Partial; source says outcome, not full path |
| 11. Craft and use seasonal wings | Post-Arch Fey reward | A Pixie Wing Tiara plus recipe ingredients; Curios/offhand use named by Lexicon | Use JEI to craft seasonal Pixie Wing; equip in offhand or Curios slot | Flight with court-themed wings | Lexicon “Pixie Wings”; JEI | Partial; exact recipes/effects not inspected |
| 12. Complete the mod | No defined endpoint yet | Unknown | Finish each court, story, encounter and any alternate/decorative branches if desired | No verified terminal criterion | Lexicon and Quest Giver | Not established |

### Dependency graph and alternate routes

- The opening resource path branches: **cookie offer / combat / ore mining / cracked Fey Log** can produce Fey Dust, while a Fey Gem can be made from dust or mined as Fey Ore.
- The core chain is **Fey Dust → Fey Gem + Fey Ink → empty scroll + crafted Fey Altar → selected seasonal Pixie scroll → summon and Pixie Orb controls**.
- Intermediate Fey and Arch Fey are optional branches built from the seasonal scrolls. The exact branch topology past the source-listed recipes is incomplete.
- Native Feywild Quest Giver quests are mod-authored content and callbacks; they do not establish TMM task IDs, rewards or chapter structure. This profile documents them as existing mod content only.

## Internal combinations and overlooked mechanics

| Internal combination | Full chain | Benefit | Limitation / exploit potential | Evidence |
| --- | --- | --- | --- | --- |
| Seasonal crop loop | Summer Pixie grows crops → Autumn Pixie harvests crops | Companion-based crop handling | Growth/harvest ranges, timing, item pickup and chunk behavior unknown; do not claim unattended automation | Lexicon ability descriptions |
| Animal loop | Spring Pixie triggers breeding → Winter Pixie collects animal drops | Companion-based breeding and animal product handling | Requires valid breeding conditions; exact Winter drops and animal eligibility need testing | Lexicon |
| Fey resource loop | Cookie offer to wild Pixie or Fey defeat → Fey Dust → Fey Gem → scroll/altar costs | Peaceful and combat/mining resource routes coexist | Cookie yield may be random; no exact resource rates are established | Lexicon |
| Fey court flight | Defeat Arch Fey → Pixie Wing Tiara → seasonal wing craft → offhand/Curios equip | Flight reward tied to boss progression | Boss access and balance not yet documented; wing recipes need confirmation | Lexicon text and public roster |

## Low-hanging setups useful in ordinary survival

### 1. Spring animal breeding — early after altar entry
- **Goal and payoff:** Trigger nearby eligible animals' normal breeding behavior without feeding them one at a time.
- **Minimum entry:** Fey Dust, Fey Gem, empty scroll chain, Fey Altar, Spring Pixie scroll and Pixie Orb.
- **Build/use steps:** Make the Spring scroll from its five JEI altar inputs; use the scroll on a block to summon the Pixie; interact with it using the Orb and enable its ability; bring eligible animals into its area and meet their ordinary breeding conditions.
- **Sustainability and limits:** Seasonal ability effects, eligibility and range are Lexicon-described, but output rates, cooldown and item costs need a pack test.
- **Evidence:** 5.6.4 source recipe JSON + Lexicon strings; no client validation.

### 2. Summer grower and Autumn harvester
- **Goal and payoff:** Pair growth and harvesting companions for a crop area.
- **Minimum entry:** The altar chain plus each scroll's different offerings.
- **Build/use steps:** Craft either scroll using its exact JEI recipe; summon and enable the Pixie; arrange crops within the ability area; inspect where harvested items land.
- **Sustainability and limits:** No verified growth rate, crop list, area, collection mode or chunk-loading behavior. Do not treat this as a proven automatic farm.
- **Evidence:** Source Lexicon descriptions + exact seasonal recipe JSONs; no client test.

### 3. Peaceful Fey Dust gathering
- **Goal and payoff:** Obtain some Fey Dust without fighting a Fey.
- **Minimum entry:** Find an untamed Pixie and carry cookies.
- **Build/use steps:** Approach the wild Pixie and offer a cookie; check inventory for dust; repeat only if the Pixie remains and accepts another offer.
- **Sustainability and limits:** The Lexicon says cookies heal Fey and may yield dust; chance, hunger and item consumption mechanics are not established. A mined Fey Gem route is predictable after locating ore but requires mining/smelting.
- **Evidence:** Pinned Lexicon text; cookie behavior not client-tested.

## API, integration and pack-config surfaces

| Hook or customization surface | Namespace / exact file | Capabilities / constraints | Current TMM override? | Evidence |
| --- | --- | --- | --- | --- |
| Common config | `com/saphienyako/feywild/config/FeywildConfig.java`; generated server common config | Lexicon spawn toggle; Fey Dust levitation duration; voices; Tree Ent attack/detection/target behavior; natural spawn weights for Pixies, Mandragora, Bee Knight, Bellsnickel and Shroomling | None identified in reviewed files; runtime config not inspected | Source at `2a339f9` |
| Client config | Same source class | Flower particles toggle | None identified | Source at `2a339f9` |
| Recipes | `data/feywild/recipe/`; custom type `feywild:fey_altar` | Recipe JSON specifies item/tag inputs and output; five inputs in inspected recipe class | No TMM recipe overrides identified | Pinned-source snapshot |
| Quest Giver special tasks | Mod checks for Quest Giver; `SpecialTask` callbacks include `fey_dust_sheep`, `summon_mandragora`, `summon_shroomling`, `summon_bellsnickel`, and four Tree Ent summon IDs | Native tasks depend on Quest Giver being loaded; these callbacks are not proof the task JSON itself is present or active | Quest Giver is pinned; no custom quest changes | Source `FeyDustItem` / `SummoningScrollItem` |
| Built-in Quest Giver quest data | `data/quest_giver/quest_lines/sprite/` | Sprite intro parent chain checks for dust, Mandrake, Fey Ink, empty scroll and Fey Altar; seasonal scroll branches require scroll item and reward a Pixie Orb; later repeatable task detects the sprite entity via a structure-void proxy | Not modified | Source JSON at `2a339f9` |

## Compatibility leads

| Other mod | Candidate link and why | Native / pack-built / would require bridge | Status |
| --- | --- | --- | --- |
| Patchouli | Hosts the Feywild Lexicon | Native dependency | Documented by official project page; client access untested |
| Quest Giver | Seasonal Pixie stories and mod-specific special-task hooks | Native dependency/integration | Content roster and some callbacks source-inspected; full quest-line trigger mapping incomplete |
| JEI | Recipe discovery, including custom Fey Altar recipe type | Native compatibility advertised | Source recipe exists; JEI category not client-tested |
| Curios | Source Lexicon says seasonal wings can be worn in Curios slot or offhand | Native compatibility advertised | Exact slot behavior not tested |
| Supplementaries | Official page says some Fey can be put into jars or cages from Feywild 5.6.3 onward | Native compatibility advertised | Supplementaries not confirmed in current TMM pin set; do not assume usable in this pack |

## Quest and teaching opportunities — research only

- The in-mod sprite storyline already teaches the first route: obtain Fey Dust, Mandrake, Fey Ink, an empty scroll, then a Fey Altar; its 5.6.4 JSON tasks primarily detect item stacks.
- Four seasonal branches are represented by summoning scroll tasks, with a Pixie Orb item reward. A later repeatable story task uses `minecraft:structure_void` as the task item proxy and `feywild:sprite` as the entity condition. This is a detector detail, not a TMM quest recommendation.
- Source callback IDs for summoning various Fey should be checked against the bundled quest data before relying on them as completion detectors.
- No TMM FTB Quest file, questbook, task, reward or quest design was changed in this pass.

## Existing chapter / pilot reconciliation

| Walkthrough stage | Current quest ID/title and status | Intended action vs actual detector | Gap / next action |
| --- | --- | --- | --- |
| Feywild chapter | No TMM chapter inspected or edited | Not applicable | Inspect canonical quest authoring data only if the user later requests quest work |
| Native sprite intro | Source Quest Giver root and `quest_01`–`quest_10` inspected | First four stages detect item possession; altar stage detects item tagged `feywild:fey_altars`; Pixie branches detect scroll items; repeatable sprite task uses entity-qualified structure void | Inspect all four seasonal story chains and confirm Quest Giver version compatibility |

## Conflicts, limitations and version drift

- **Wiki drift:** The available official “How to get started” page is explicit that it covers 5.0. Its basic chain (including Mandrake recipe, altar layout and Orb recipe) is not treated as exact 5.6.4 evidence unless corroborated by the pinned source.
- **Source identity:** The public source branch at the exact release date declares version 5.6.4 and includes the 5.6.4 source contents, but the CurseForge binary has not been downloaded/compared; do not claim reproducible binary identity.
- **No runtime validation:** No pack launch, recipe, GUI, quest, worldgen, drop, summon, companion ownership or chunk-loading behavior was tested.
- **Content not yet mapped:** Full item and block roster; all guidebook entries; complete natural spawn conditions; intermediate scroll recipes; all four seasonal story trees; sovereign encounter prerequisites; wing recipes; structures/worldgen; exact ability range/cooldown/automation and failure-cost behavior.

## Evidence ledger

| Claim / section | Source URL or exact inspected file + immutable ref | Evidence tier |
| --- | --- | --- |
| Pack pin | `mods/feywild.pw.toml` on TMM `test/full-integration` | Pack metadata |
| 5.6.4 release date, filename, platform, file ID and release notes | [CurseForge file 9321431](https://www.curseforge.com/minecraft/mc-mods/feywild/files/9321431) | Documented upstream |
| Mod roster, Patchouli/Quest Giver dependencies, Lexicon and compatibility summary | [Official Modrinth project](https://modrinth.com/mod/feywild) | Documented upstream |
| Source version metadata / runtime config | [`gradle.properties`](https://github.com/SaphieNyako/Feywild/blob/2a339f98a5a2ea0d5620801538095a7b013b6de2/gradle.properties); [`FeywildConfig.java`](https://github.com/SaphieNyako/Feywild/blob/2a339f98a5a2ea0d5620801538095a7b013b6de2/src/main/java/com/saphienyako/feywild/config/FeywildConfig.java) | Inspected source snapshot |
| Fey Gem and empty scroll recipes | [Gem](https://github.com/SaphieNyako/Feywild/blob/2a339f98a5a2ea0d5620801538095a7b013b6de2/src/generated/resources/data/feywild/recipe/fey_gem.json); [scroll](https://github.com/SaphieNyako/Feywild/blob/2a339f98a5a2ea0d5620801538095a7b013b6de2/src/generated/resources/data/feywild/recipe/empty_summoning_scroll.json) | Inspected source snapshot |
| Seasonal and intermediate scroll recipes | [Source recipe directory](https://github.com/SaphieNyako/Feywild/tree/2a339f98a5a2ea0d5620801538095a7b013b6de2/src/main/resources/data/feywild/recipe) | Inspected source snapshot |
| Altar recipe matching, slots and progress | [`FeyAltarRecipe.java`](https://github.com/SaphieNyako/Feywild/blob/2a339f98a5a2ea0d5620801538095a7b013b6de2/src/main/java/com/saphienyako/feywild/recipe/FeyAltarRecipe.java); [`FeyAltarBlockEntity.java`](https://github.com/SaphieNyako/Feywild/blob/2a339f98a5a2ea0d5620801538095a7b013b6de2/src/main/java/com/saphienyako/feywild/block/entity/FeyAltarBlockEntity.java) | Inspected source snapshot |
| Lexicon prose for summon control, Pixies, courts and wings | [Lexicon locale](https://github.com/SaphieNyako/Feywild/blob/2a339f98a5a2ea0d5620801538095a7b013b6de2/src/main/resources/assets/feywild_feywild_lexicon/lang/en_us.json) | Inspected source snapshot |
| Quest Giver intro tasks | [Quest line source folder](https://github.com/SaphieNyako/Feywild/tree/2a339f98a5a2ea0d5620801538095a7b013b6de2/src/main/resources/data/quest_giver/quest_lines/sprite) | Inspected source snapshot |
| Previous how-to page and its version declaration | [Official wiki mirror](https://github-wiki-see.page/m/SaphieNyako/Feywild/wiki/How-to-get-started%21) | Documented upstream, explicitly Feywild 5.0 |
| Client/server behavior | Not tested | Not established |

## Next research actions

1. Inspect all current 5.6.4 altar recipes, especially the missing Bellsnickel, Tree Ent and Queen Titania branches; document exact recipe inputs and outputs.
2. Map each Lexicon category and page to the relevant item, build and progression step; verify the Lexicon grant/recovery path.
3. Inventory the four seasonal Quest Giver lines, their parent/task chain and reward/callback semantics.
4. Inspect Pixie abilities, Shroomling trade logic, Tree Ent combat/spawning, sovereign encounters and wing recipes.
5. If the exact client is available, validate one seasonal summon end-to-end and check the custom altar recipe category in JEI.
