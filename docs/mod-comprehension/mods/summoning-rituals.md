# Summoning Rituals — framework and pack integration profile

| Field | Value |
| --- | --- |
| Manifest ID / actual mod ID | `summoning-rituals` / `summoningrituals` |
| Category / parent or addons | ritual framework; optional native KubeJS integration, JEI/EMI viewers |
| MC / loader / pack target | 1.21.1 / NeoForge / B8 full-integration snapshot |
| Pinned version / artifact / hash | `summoningrituals-neoforge-1.21.1-3.14.2.jar`; CurseForge project 671040, file 8539417; SHA-1 `f67fa7b8a90b231491932b848f76c68cfc935c23` (`mods/summoningrituals.pw.toml`) |
| Source snapshot | Official release tag `v1.21.1-neoforge-3.14.2`, commit [`5e919d5e133733e3fded8623d57bd5be7c28d0eb`](https://github.com/AlmostReliable/summoningrituals/tree/5e919d5e133733e3fded8623d57bd5be7c28d0eb); source Gradle properties declare MC 1.21.1, NeoForge 21.1.199 and mod 3.14.2; exact JAR/source binary equivalence not tested |
| Guide/documentation version gap | Official player and developer docs describe 1.21.1+; source tag matches pinned version; verify GUI behavior in TMM client |
| Profile status | researching |
| Evidence reached | Exact pack pin, exact-version source, generated registry data, recipe schema/builders, altar lifecycle, KubeJS API and official versioned documentation inspected. No client/world test. |
| Reviewed | 2026-11-15 |
| Quest ownership | Existing “Understand Summoning Rituals” is an informational checkmark; it correctly says the mod is a framework with no default rituals. No quest file changed. |

## Executive gameplay map

**What the player actually does:** Summoning Rituals supplies an altar and a data/KubeJS-driven ritual recipe engine; it ships no normal altar recipe or altar recipes of its own. A pack author must provide a survival acquisition route and define each ritual. In a configured pack, a player inserts item ingredients by right-clicking the altar, lures required entities into the sacrifice zone, satisfies optional time/location/biome/structure/block-pattern conditions, and inserts the one-count initiator last. The altar validates the recipe, consumes item inputs and kills matching entity sacrifices without loot, runs its animation for the configured ticks, then creates item/entity outputs and invokes commands/events. Unused items remain in the altar. The altar has no GUI; JEI/EMI exposes recipe details and block-pattern previews. [source: exact release `5e919d5`, `AltarBlockEntity`, recipe schema, official 1.21.1 player docs]

**Why it matters in TMM:** This is cross-mod integration infrastructure, not a standalone magic school. It can convert items and entities into items, entities, world actions and commands, making it a strong candidate for future boss-drop alternatives, special summon contracts or ritualized crafting. At the current TMM snapshot, however, there are no Summoning Rituals recipes in tracked KubeJS scripts or pack data, and the mod itself has no altar craft recipe. The existing quest explicitly describes this as a framework. Therefore there is no confirmed survival route or functional player progression in this pack yet. This profile documents capability and integration gaps; it does not create recipes or change quests/runtime.

**Quick start:** There is no verified TMM survival quick start. In a test world, obtain the altar through creative mode or commands, inspect its uses in JEI/EMI, place required ingredients by right-clicking, position sacrifice entities nearby, and add the recipe initiator last. The vanilla survival recipe, any authored TMM recipes, and the altar's intended role are all currently unestablished.

### Player-value snapshot

| Five standout capabilities | Three early practical unlocks | Three candidate integrations |
| --- | --- | --- |
| Recipe-driven item/entity transformations; no-loot sacrifices; output entities; server command outputs; optional structure/condition gates and custom lifecycle events | None available in the current pack without a survival altar recipe and ritual definitions | Tradeable boss-associated item routes; controlled boss/miniboss summoning; item-to-item recipes with visible entity/biome/structure requirements |

### Subsystem coverage at a glance

| Subsystem / progression branch | Mechanics evidence | Progression completeness | In-game guide/JEI reference | Client validation | Blocker / next action |
| --- | --- | --- | --- | --- | --- |
| Altar blocks and survival acquisition | Source registry and generated block data; no default altar recipe | API/creative operation mapped; TMM survival access missing | Altar usages in JEI/EMI, if recipes exist | not run | Decide whether framework gets a pack-authored altar recipe or remains infrastructure-only |
| Recipe inputs and initiator | Exact 3.14.2 schema and altar implementation | Strong | JEI/EMI Altar recipe; initiator shown separately | not run | Confirm all high-risk item remainders and initiation feedback in client |
| Entity sacrifices / fake inputs | Exact source implementation and official docs | Strong structural behavior; exact custom predicates depend on each recipe | JEI/EMI entity input tooltips | not run | Test entity selection, kill/no-loot, players/bosses and custom fake inputs |
| Items, entities, display outputs and commands | Schema and output/event implementations | Strong type inventory, but command context and individual events vary | JEI/EMI output panel; command tooltip | not run | Audit every authored recipe against visible/real output and command authority |
| Conditions and block patterns | Source condition types/builders; recipe viewer preview feature | Structural behavior mapped; no TMM patterns | Condition and structure icons/preview | not run | Validate failures, block state matching and recipe disambiguation |
| KubeJS integration | Pinned TMM has KubeJS and addon plugin in release source | API exposed; TMM has no recipe script | KubeJS script API + JEI/EMI | not run | Authoring/runtime reload not tested; avoid assumptions about supported recipe changes |
| Automation | Item handler and official docs; initiator must be inserted last | Structural API known; no pack recipes to automate | Altar inventory render and recipe viewer | not run | Test hopper insertion/order and output retrieval before promoting automation |

## Guidebook, discovery and player-facing help

- The mod does not supply a progression book or a fixed tutorial quest. Official player documentation is external; it says exact integration and recipe progression depend on each modpack. In the pack, the current quest checkmark is an informational note rather than a how-to progression chapter.
- For a future playable integration, an introductory quest must first explain how to obtain the altar. If it is a pack-authored craft, teach its recipe and placement; if it is intentionally a structure reward, explain where and how it is found. Then teach item insertion, entity placement, conditions, initiator-last activation, the color/particle feedback and extraction.
- JEI/EMI is the primary in-game recipe reference. Look up uses for `summoningrituals:altar`. Initiator, item/entity inputs, true outputs, command outputs and block patterns are separate recipe fields. Hovering condition/pattern icons may expose more detail, and selecting a pattern can preview it at a nearby altar. These recipe categories are empty unless another mod or datapack registers recipes.
- The viewer can include **display-only** outputs. `display_outputs` exist for recipe presentation and are not spawned by ritual completion; likewise, a fake entity input uses an item icon while the actual input is an entity matching a custom predicate. Any future quest instructions must distinguish those visual proxies from actual consumed/spawned values.

## Systems and input/output contracts

| System | Input kind and exact contract | Operation / consumption | Output kind and destination | Timing, range, interface and limitations |
| --- | --- | --- | --- | --- |
| Item inputs | Item stacks matching recipe ingredients, including counts/components as specified | Insert by right-clicking the altar. A stack can be inserted as a stack; recipe initiator has a separate one-count role. Matched requirements are consumed on start | Remaining unused item stacks stay in altar; after completion, normal item outputs spawn at/around altar | Stored in altar inventory; common config default 16 slots, range 1–64. Hoppers/item handlers can insert/extract. Initiator must be last for normal player use |
| Initiator | One item matching an `initiator` ingredient | Insert last; altar searches for exactly one matching recipe and checks its inputs/conditions | Starts ritual if exactly one recipe matches; otherwise initiator is returned/dropped and failure feedback is sent | Matching rejects no recipe, missing sacrifices/inputs, failed conditions, wrong pattern or multiple matches. No general GUI |
| Entity inputs | Nearby entity satisfying a declared entity predicate/input contract, including optional per-entity data such as health or effects | At ritual start, matching sacrifice entities are tagged and killed; their normal loot event is canceled | No item loot from sacrificed entities by default; the entity itself is consumed as an input | Default half-size sacrifice zone offset is `[3, 2, 3]`; recipe may override. Player exclusions, predicates and recipe-specific custom behavior need inspection |
| Fake entity inputs | Entity in sacrifice zone matching a custom predicate; recipe viewer shows a placeholder item instead of the entity | Entity matching is treated as a sacrifice at ritual start | No-loot death/other custom event as recipe declares | The placeholder item is not necessarily an actual altar input. Must explain real entity condition in quest/UI tooltip |
| Item outputs | `item_outputs` recipe values | Spawned at completion unless custom events change handling | Item entities at altar by default; recipe can change position/spread | Output location/spread configurable per recipe; no built-in inventory deposit guarantee |
| Entity outputs | `entity_outputs` recipe values | Spawn at completion | New world entities around altar, default circle | Recipe can modify spawn positions; entity configuration and AI/ownership depend on output and mod behavior |
| Commands | `commands` output values | Invoked by server when ritual completes; options can require player interaction/context | Arbitrary server command side effect | A command requiring player context cannot run for automated rituals. Common setting `announce_command=true` logs when a recipe command is invoked. Authority/safety must be reviewed per recipe |
| Custom events | Lifecycle hooks including start/cancel/complete integration events | Pack addon or KubeJS/Java listener changes or blocks behavior | Arbitrary event-defined effects; not inferable from base recipe outputs | Need exact event listener source and runtime test for each custom event. Upstream KubeJS hook is native; no event wiring exists in tracked TMM scripts |
| Block pattern | Required palette/pattern around altar; can check block IDs/states and offsets | Checked before ritual starts | Gate only, unless optional pattern extension is read by a custom event to add effects | Source allows pattern radius up to 32 blocks per axis; preview system helps placement. Palette extensions are explicitly optional for starting |
| Conditions | Additive vanilla loot conditions evaluated before start | Every listed condition must pass | Gate only | Common condition types include time/location/weather/biome and other supported loot predicates; exact availability determined by recipe condition codecs |
| Time | Recipe `ticks`; default 40 | Altar runs for specified game ticks after consumption/sacrifice | Outputs/commands after timer completes | 20 ticks = 1 second; default 2 seconds. Client shows ritual activity. Interruption/reload behavior requires test |
| FE, fluids, mana, XP and blood | No corresponding native recipe input channel found in the source schema | Must be represented through item/entity conditions, custom event/command, or custom code if desired | Effects can indirectly alter these systems via command/custom event, but base altar does not accept them as typed quantities | Do not describe the framework as a generic ritual receiver for fluid, FE, mana or XP |

### Native content and TMM integration audit

- The official project explicitly says the mod adds no altar recipes by default; the pinned source tag contains no altar recipe data-generation registrations or generated altar recipes. It contributes the block/framework, recipe type/serializer, KubeJS API and recipe-viewer support.
- Tracked TMM `kubejs/server_scripts/` contains only `bootstrap.js` and `dragon_diets.js`; startup scripts contain only `bootstrap.js`. No recipe JSON or KubeJS altar recipe declaration for Summoning Rituals is present. No direct use of `summoningrituals` appears in current pack code.
- `manifest/quest_content.json` includes “Understand Summoning Rituals” as an informational checkmark. Its description accurately states that no fixed rituals ship by default and asks the player to check JEI/TMM recipes. This is not proof that the player can obtain an altar or perform a ritual.
- Consequence: **the current pack has no documented playable progression through this framework**. To turn it into player-facing content, the pack needs both an altar acquisition route and authored, sourced/tested recipes. The mod is not incompatible with the rest of TMM; the current integration gap is absence of content.

## Progression and future walkthrough blueprint

Since the framework has no built-in recipe or tier ladder, this map describes only a future pack-authored integration. It must not be mistaken for content already present.

| Stage | Player action | Observable success | In-game help / status |
| --- | --- | --- | --- |
| 0. Obtain an altar | Complete a future TMM-authorized craft/loot/quest acquisition route | A placed altar is available in survival | No current recipe/acquisition route established |
| 1. Learn altar inputs | Check Altar usages in JEI/EMI; place actual item inputs in required quantity | Altar render shows inserted stacks; inputs remain until a valid initiator succeeds | Official guidebook absent; quest must teach right-click insertion and count |
| 2. Satisfy sacrifice inputs | Lure required entity into the declared sacrifice zone and satisfy fake-input/entity predicates | Recipe-viewer ingredients and world conditions match | Explain real entity behind any fake-input icon, and warn that starting kills it without loot |
| 3. Build required structure | Use pattern icon/preview and arrange blocks | Preview confirms a valid pattern; recipe becomes eligible | Guide cannot replace an untested preview; quest should name blocks and placement |
| 4. Add initiator last | Right-click the altar with one initiator after all inputs are present | Ritual starts with active altar visuals and correct recipe | Failure text distinguishes initiator, input, condition, structure or ambiguous match |
| 5. Collect outputs | Let timer finish, inspect item/entity/command effects and retrieve remaining inputs | Recipe-defined actual outputs appear; unused ingredients remain | Distinguish display-only outputs from spawned items |
| 6. Advance authored pack branches | Follow any future recipes that convert mod/boss materials or summon entities | Each defined output and gate is independently observable | No current TMM branch or recipe set exists |

### Developer/recipe author checklist

For every pack-authored ritual, document the exact initiator; each consumed item/component/count; true entity/fake-entity predicates and kill/no-loot consequence; conditions; required and optional patterns; duration; real item/entity outputs; display-only outputs; command text/context; custom event listeners; failure behavior; and JEI/EMI tooltip. Test a valid run and one negative case for every material gate before claiming the recipe works. Review command permissions and all boss/ProjectE gate bypasses independently.

## Compatibility, risks and test plan

- The exact release's Gradle metadata specifies Minecraft 1.21.1 and NeoForge 21.1.199; the pack's NeoForge 21.1.265 is newer. Its KubeJS integration is native but optional; TMM pins KubeJS 2101.7.2-build.381 while the release source's development dependency is build.295. Server co-load passed in the B8 full integration, which establishes startup only.
- No verified incompatibility was found for the TMM 1.21.1 runtime. The significant documented concern is functional incompleteness: no survival altar recipe and no pack-authored ritual recipes.
- **Sacrifice economy:** ritual entity inputs die at start and produce no ordinary mob drops. A recipe that consumes a rare/boss mob can therefore permanently destroy it if requirements or output handlers are incorrect; include spawnability, loot and reward replacement audit.
- **Command authority:** output commands execute server-side. Require a per-recipe safety review, ensure player-context commands do not silently fail when automated, and inspect command-output log setting.
- **Ambiguous initiators:** when more than one recipe matches the same initiator and current state, the altar refuses to start. Recipe authors should avoid overlapping initiators/conditions and test boundary states.
- **No storage substitute:** although the altar has an item handler and 16-slot default inventory, it is a bounded ritual input buffer, not general-purpose storage; upstream docs state recipe input limits prevent it from being used as infinite storage.

| Test | Positive and boundary cases | Expected observable | Performed? |
| --- | --- | --- | --- |
| TMM content inventory | Inspect all tracked datapacks, KubeJS scripts, mod configuration and quest evidence | No altar acquisition/ritual recipe unless a hidden tracked source is found | repo source audit only; no game test |
| Manual altar loop | Exact recipe, right-click whole stack, sneak-right-click extraction, initiator last | Correct inputs retained/consumed and failure messages match actual missing condition | no |
| Sacrifice behavior | Correct and incorrect entities; multiple candidate matches; player/mob/boss/custom predicate | Only intended entity dies; no normal loot; other nearby entities remain | no |
| Conditions/patterns | Valid/invalid time, biome, block state and pattern; overlapping recipes | Accurate match, preview and useful failure reason | no |
| Outputs and commands | Real item/entity outputs vs display-only item, player-required command with and without automation | Viewer matches real output; command runs only under supported context | no |
| Automation/reload | Hopper order, item-handler extraction, recipe reload, active ritual save/rejoin | Stable recipe matching and no duplicate/lost inputs | no |

## Evidence ledger

| Claim | Exact source | What it establishes / limit |
| --- | --- | --- |
| Pinned artifact | `mods/summoningrituals.pw.toml`; manifest record and CurseForge file 8539417 | Exact selected file and SHA-1 lock; source equivalence is not binary-tested |
| Exact release source | [tag commit `5e919d5`](https://github.com/AlmostReliable/summoningrituals/tree/5e919d5e133733e3fded8623d57bd5be7c28d0eb); `gradle.properties` | Same mod version, MC and loader target; provides immutable implementation evidence |
| Player operation | [official 1.21.1+ player guide](https://docs.almostreliable.com/summoningrituals/usage_for_players.html) | No GUI, right-click item insertion, entity sacrifices, outputs, JEI and automation explanation |
| Recipe contract | [official recipe basics](https://docs.almostreliable.com/summoningrituals/recipe/basics.html) | Schema fields for inputs/outputs, zones, conditions, patterns, duration and custom behavior |
| No default altar recipes | [official project description](https://www.curseforge.com/minecraft/mc-mods/summoningrituals); pinned release source and generated `data/summoningrituals` | Project states recipes must be pack-defined; exact source registers no built-in altar recipe data |
| Current pack integration | `kubejs/**`, tracked data files, `manifest/quest_content.json`, current pins | No authored altar recipe in tracked TMM files; quest is an explanatory checkmark |
| Startup | B8 server boot record in `docs/COMPATIBILITY.md` | Co-load/startup only; no client-world or ritual behavior claim |

## Unknowns and next research

- [ ] Download and hash-check the exact CurseForge artifact or obtain an upstream reproducible build to close source-to-binary provenance; this is not necessary to identify the locked file but would strengthen closed claims.
- [ ] Check altar visuals, pattern preview, JEI/EMI overlays, insertion/extraction and actual failure messages in a TMM client.
- [ ] When/if a pack recipe is authored, audit its recipe output, command authority, boss gate, entity eligibility and no-loot sacrifice cost as a separate profile update.
- [ ] Verify reload/rejoin/automation edge cases, including initiator order and KubeJS recipe reload behavior.
- [ ] Update profile status only after the current pack's intended player-facing role is decided and its mandatory steps are backed by a concrete acquisition/recipe path.

## Revision notes

- 2026-11-15 — Added source-backed profile for pinned Summoning Rituals 3.14.2. Mapped the altar recipe lifecycle and exact I/O classes; confirmed no default recipes, no tracked TMM altar acquisition or recipes, and a framework-only quest note. No quest or runtime files changed.
