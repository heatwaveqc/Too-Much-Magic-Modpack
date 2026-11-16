# Goety Cataclysm — mechanics and gameplay profile

| Field | Value |
| --- | --- |
| Manifest ID / actual mod ID | `goety-cataclysm` / `goety_cataclysm` |
| Category / parent or addons | magic addon; extends [Goety](goety.md) and L_Ender's Cataclysm |
| MC / loader / pack target | 1.21.1 / NeoForge / TMM snapshot `b7023e2048a54d0c93c5e0f48977843bc302230c` |
| Pinned version / artifact / hash | `mods/goety-cataclysm.pw.toml`; Modrinth version ID `gv1JY0TU`; file `goety_cataclysm-1.21.1-1.8.4.jar`; SHA-512 `45e390fb44ea7aa6b5f81b95b57ca224489a527a66167de1bdcf9a53c71cb5a54f6d29b69fa30b3f342e853c30bee677cf2c3701d141edf76b0be8515b6b6b60` |
| Binary identity | Exact downloaded file hash matches the packwiz lock; its `META-INF/neoforge.mods.toml` reports `1.21.1-1.8.4`, while Modrinth's version record labels the file/version `1.21.1-1.9.0`. Treat the locked hash and JAR metadata as artifact identity; this is a naming/version-label discrepancy. |
| Source snapshot | Public `Polarice3/Goety_Cataclysm` has only the `master` branch at `6b5c8dd4f0b7f307aba8005e7634ade8f4438c5b`; its Gradle metadata targets Minecraft 1.20.1, so it is not version-aligned to the pinned 1.21.1 artifact. The exact locked JAR's resource inventory was audited earlier, but the JAR is not available for direct class inspection in this session. Do not extend bytecode claims from the 1.20.1 source. |
| Guide/documentation version gap | 51 English Black Book entries and 15 categories for Cataclysm content are bundled in the locked JAR. No live Patchouli or client test. |
| Profile status | researching |
| Evidence reached | exact JAR SHA-512 verified; recipes, data resources, guide entries, mod metadata and dependency declarations inspected; gameplay not tested |
| Quest ownership | covered as optional Goety chapter branch; no Goety Cataclysm specific lesson yet |

## Executive gameplay map

**What the addon does:** adds Cataclysm-themed Goety focus spells, servants, transformations, materials, a Fabricator block, loot injections, and Black Book pages. It is a content bridge rather than a stand-alone progression system: most focus crafts and servant conversions are Goety Dark Altar recipes, and access depends on the parent mod's Soul Energy, ritual research, creature control and equipment. Several powerful summon recipes consume Cataclysm boss drops or require Cataclysm mobs as inputs. Other servants are made by Animation Core rituals or by using an Infernal Forge on a structure. It does not replace Cataclysm's boss progression or Goety's ordinary guidebook onboarding.

**Why it matters in TMM:** it directly converts Cataclysm encounters into new Goety spells and minions. This creates a clear optional post-boss route, but it also means boss drops can become repeatable combat power. Servant summoning, healing, death recovery and resurrection items need specific tests before the addon can be called balanced or progression-safe.

**Quick start:** install the addon with its declared dependencies, craft/open Goety's Black Book, then use its Cataclysm pages as a recipe index. For a first low-cost branch, craft a listed Cataclysm focus with the matching base focus and Cataclysm ingredients through its stated Goety ritual; the recipe itself lists the `craftType`, activation item, pedestals, soul cost and duration. Later branches require relevant Cataclysm drops, controlled Cataclysm entities, or structures such as the Ancient Factory. [pin] [jar: SHA512]

### Player-value snapshot

| Five standout capabilities | Three early practical unlocks | Three candidate integrations |
| --- | --- | --- |
| 1. Cataclysm boss abilities recast as Goety focuses 2. Cataclysm creatures bound as servants 3. Ritual transformations for selected Goety/Cataclysm servants 4. Ender Golem and Netherite Monstrosity construction 5. Fabricator and mechanized materials | The addon has no truly early stand-alone unlock: even its basic focuses assume Goety's altar, focus progression, and Cataclysm ingredients. The least expensive candidates are crafting a focus from common ingredients, repairing a servant with its listed food/material, and learning the Black Book's optional Cataclysm section. | 1. Cataclysm boss drops → Goety altar focus recipes 2. Cataclysm entities → Goety conversion rituals and bound servant roster 3. Goety Necro/Abyss robes → passive healing for selected Cataclysm servants; exact behavior requires client testing |

### Subsystem coverage

| Subsystem | Evidence | Progression completeness | Guide/JEI reference | Client validation | Next action |
| --- | --- | --- | --- | --- | --- |
| Black Book extension | exact JAR bundled entries/categories | broad index; actual in-game display not checked | Black Book → Cataclysm; Crafts, Focuses, Summons, Mutations, World/Bestiary | not run | open book and confirm ordering/visibility |
| Focus spells | 28 focus recipe JSONs; exact recipe inputs and ritual fields parsed | recipe families identified; costs are mapped in data but parent altar debit/failure semantics apply | Cataclysm → Focuses; matching focus entry | not run | test casts, costs, staff interactions, limits and damage/effects |
| Servants and construction | 13 thrall recipe JSONs plus guide entries for golems/large servants; latest official 1.21.1 release notes say servant persistence and robe/crown healing were fixed | summon recipe declarations and construction routes mapped; applied fix behavior, cap, command, AI and persistence are not client-tested | Cataclysm → Summons; matching recipe/JEI | not run | verify the pinned file's reported fixes, owner/cap behavior, chunk persistence, healing, death and recovery |
| Entity conversions | 10 `goety:ritual` recipe JSONs | input entity tags, activation item, pedestals and recipe costs mapped. The current Goety 3.2.2 parent engine consumes the activation item at start, charges Soul Energy once per 20 ticks, and stages pedestal consumption; its base interrupt returns the activation item but not already-paid souls/ingredients. Applying that transaction contract to these addon recipes is an inference until the pinned addon ritual classes are inspected. | Cataclysm → Mutations | not run | test wrong entity/tag, interruption, owner preservation and exact recipe debit; inspect for addon overrides when a 1.21.1 source/artifact becomes available |
| Crafting materials and Fabricator | 61 addon recipe JSONs total; guide covers Abyss/Primordial Essence, mechanized iron and Fabricator | some recipes parsed; Fabricator operating interface and full recipe set remain open | Cataclysm → Crafts / Artifice; JEI | not run | inspect operating recipes and automation |
| Loot / world / boss links | 12 advancements, 9 loot JSONs, spawn modifier and tags | drops/loot injections not fully reconciled with Cataclysm pin | Cataclysm → World/Bestiary; Cataclysm guide | not run | audit boss drop values and world spawn predicates |

## Guidebook onboarding and usage

- **Guide:** Goety's existing Black Book; the addon adds category `assets/goety/patchouli_books/black_book/en_us/categories/cataclysm.json` and entries under `.../categories/cataclysm/` and `.../entries/`.
- **First access:** craft and open the base Goety Black Book as described in [Goety's profile](goety.md). The addon does not provide a separate book or new initial gate.
- **Reference map:** Cataclysm categories cover Crafts, Focuses (Abyss, Geomancy, Necromancy, Nether, Storm, Void, Wind), Mutations, Summons, and World/Bestiary. Individual entries pair guide prose with JEI recipe references. The exact English guide bundle contains 15 category files and 51 entry files.
- **Guide limitations:** entries explain broad summon behavior and some healing/repair methods, but most pages rely on displayed recipe data. They do not establish how ritual failure consumes soul energy or items, whether all servants persist through unloads, or whether boss drops remain available after conversion. Teach those mechanics only after disposable-world tests.

## Mechanics and typed input/output

| System | Inputs and costs in locked resources | Output and trigger | Boundaries / evidence |
| --- | --- | --- | --- |
| Focus recipes | 28 focus recipes: most are `goety:ritual` with an activation focus, named altar `craftType`, soul cost, duration, and pedestal item/tag ingredients; several are vanilla shaped crafts. Examples include Abyssal Beam (Abyss Essence + Cataclysm Athame + Phantom Membrane + Ectoplasm + amethyst block + Prisma Beam Focus; Deep ritual, 1 soul, 10 ticks) and Extinct Flame (Ignis Ingot + Ectoplasm + ender pearl + blaze rod; Expert Nether, 5 souls, 10 ticks). | Focus item, normally via the Dark Altar ritual; shaped recipes craft immediately. Focuses then cast Cataclysm-inspired projectiles, areas, summons or continuous effects using Goety's wand/staff system. | Inputs and recipe fields are exact from JAR JSON. The parent mod's ritual code determines actual debit/consumption order; no in-world test. Some casts are area damage or persistent effects and need safety/boundary tests. |
| Thrall recipes | 13 `goety:ritual` recipes use Goety altar craft types, soul cost/duration and pedestal ingredients. Examples span Draugr, Elite/Royal Draugr, Aptrgangr, Deeplings, Prowler, Urchinkin, and Zombie Amethyst Crab. | The JSON declares summon entities and can use a display proxy in its JEI result; actual spawn/ownership behavior is a ritual/runtime question. | Parent Goety's common ritual framework provides the expected soul debit and progressive ingredient handling, but any addon-specific ritual override is unverified. Summon caps, owner/faction handling and successful spawn remain untested. |
| Entity conversion | 10 `goety:ritual` recipes use `convert_tamed` or `convert_complete_tamed`. They target entity tags and require activation item plus pedestal items. Examples: a tamed baby Leviathan + Aging Cream + Tidal Claws/Primordial/Abyss Essences → Leviathan (10 souls/10 ticks); a Blaze + Dying Ember plus swords, blaze rods, Ectoplasm and soul-fire block → Ignited Berserker (5/10); a Draugr + Necro Cape and Goety gear/focus → Draugr Necromancer (5/30). | Existing entity becomes the named servant after a valid rite; JSON `result` is `goety:jei_dummy/none`, a recipe display placeholder, not an item output. | Source JSON identifies required target tags but does not prove runtime target selection, entity consumption, ownership, or failure refund. A wrong-tag creature is the critical negative case. |
| Golem/construct assembly | Guide instructions use Animation Core on the center Coral (Coralssus), center Copper Block (Coral Golem), or center Void Stone (Ender Golem); Netherite Monstrosity uses Infernal Forge on center Reinforced Redstone Block. | Places a servant entity from blocks/structure rather than ordinary item output. | Coralssus requires one coral type among Fire, Horn or Tube coral; Ender Golem allows Obsidian or Obsidian Bricks; Netherite Monstrosity shares Redstone Monstrosity summon cap. These are guide claims; structure recognition and costs need testing. |
| Servant upkeep/recovery | Guide names healing inputs: fish for many abyss servants; bones/rotten flesh for Draugr; iron for Watchers/Prowler; Netherite Block/Lava Bucket/Lava Power Cell or Soul Fire for Netherite Monstrosity. Some receive passive healing from owner robes/capes. Death drops can include a bound skull/head, spirit or core used for cheaper recovery. | Right-click healing; passive robe-driven healing; death loot can become a resurrection/repair route. | Exact heal amount, item consumption, death drop chance, owner binding, and recovery ritual need code or live testing; do not promise guaranteed resurrection. |
| Fabricator | An Altar ritual crafts `goety_cataclysm:fabricator` using Fireball Focus, Cursed Cage, Witherite Ingot, two Mechanized Iron Blocks and an Anvil; Forge craft type, 1 soul and 30 ticks. | Creates a block; guide identifies it under Crafts/Artifice. | Exact internal recipe interface and energy/automation contract are not established by the recipe that crafts the machine. No FE capability should be assumed. |
| Boss loot and world | Locked JAR includes entity loot, injected loot for Ender Golem and Leviathan, a biome spawn modifier, and advancement triggers for servant summon/conversion and Fabricator crafting. | Items from Cataclysm encounters feed recipes and recovery; entities/spawns are triggered by Cataclysm/Goety systems. | Exact parent Cataclysm drop tables, item quantities and stage gates are external dependencies and were not audited here. Do not equate an advancement's existence with a player-visible how-to. |

### Acquisition and progression boundaries

This addon is optional after the base Goety start. Its recipe JSONs identify `research` fields on some rituals (`bygone`, `buried`, `terminus`) and the Goety altar `craftType`, but these are parent-mod research gates and require the parent profile's unlock predicates. Cataclysm boss loot is a prerequisite for several advanced foci and conversions. The addon adds no dedicated XP, mana, fluid or FE system in the inspected data. Most effects cost Goety Soul Energy through parent ritual/casting paths.

A recipe result of `goety:jei_dummy/none` on conversion recipes and summon recipes is display metadata, not a produced item. The actual result is a spawned or transformed entity handled by Goety's ritual serializer. Treat that statement as a source-backed recipe interpretation requiring runtime confirmation, not a tested outcome.

## Walkthrough blueprint

| Stage | Gate/choice | Player action and visible result | Named guide/JEI reference | Honest quest detector/fallback | Coverage |
| --- | --- | --- | --- | --- | --- |
| 0. Open base Goety guide | prerequisite to learn recipes | craft and open Black Book, then select Cataclysm category | Goety → Cataclysm | possession only proves crafting; manual check to confirm guide was opened | path explained; display not tested |
| 1. Establish Goety ritual access | parent prerequisite | follow base Goety progression until required focus, altar, research and Soul Energy are available | Goety rituals and focus entries; addon recipe in JEI | detect actual recipe output or conversion, not altar possession | parent progression referenced |
| 2. Choose a focus branch | optional Cataclysm drop/resource gate | inspect recipe, defeat/loot the needed Cataclysm source, activate matching recipe at altar or craft shaped focus | Cataclysm → Focuses; JEI recipe category | item acquisition can detect focus craft, but can't prove altar operation; task/event detector unverified | representative routes known; 28 recipes not all item-by-item crosswalked |
| 3. Bind a servant | optional summon/conversion branch | make required focus/materials; perform `thrall` summon ritual, convert a valid owned/tagged mob, or assemble the guide-described structure | Cataclysm → Summons/Mutations; JEI | require actual servant/converted mob check; manual verification if FTB lacks trigger | broad path, target predicates and costs untested |
| 4. Heal, command and recover | practical use branch | use the named repair/heal item; test Goety command wand/staff; test recovery item after controlled servant death | relevant Summon entry | item task alone doesn't prove healing; test health/owner state and use a manual fallback | guide directions known, effect unknown |
| 5. Fabricator and materials | optional advanced craft branch | gather listed Forge altar inputs; construct Fabricator; test one documented operation before teaching automation | Crafts → Artifice → Fabricator; JEI | block ownership is not operation; use output from actual recipe or manual task | machine inputs identified; operating recipe unknown |
| 6. Optional boss/servant completion | open-ended | explore Cataclysm content and choose selected focuses, mutations, servants and recovery paths | Cataclysm → World, Bestiary, Focuses, Summons | milestones should be selected branches, not require every item/entity | scope is broad; full capstone inventory remains open |

## Internal synergies and useful setups

| Combination | Full chain | Payoff | Cost/risk | Evidence |
| --- | --- | --- | --- | --- |
| Boss drop → Goety focus | Cataclysm encounter/item → Goety altar recipe → focus → matching wand/staff cast | Makes a defeated boss's signature attack available as a spell | Could duplicate high-impact boss abilities; exact damage/cooldown and boss-drop rate need tests | exact recipe/guide data; cast untested |
| Tamed mob → upgraded servant | base/ordinary Cataclysm servant → conversion ritual → stronger specialist servant | Preserves the servant-investment route while unlocking specialized abilities | Ritual may consume or replace input and uses rare boss ingredients; owner and failure behavior unknown | conversion recipe data + Black Book |
| Servant recovery item → repeated field use | servant dies → core/head/spirit drop → reassemble/rebind | May reduce loss from expensive high-end summons | Drop may not be guaranteed; skull/head owner-lock is guide-described; could bypass boss gates if duplicated | guide pages + loot resources; not tested |
| Robe/cape → passive servant heal | equip named Abyss Robe or Necro Cape → selected servant regenerates | Reduces food/repair burden for a matching servant group | Exact rate, range, chunk behavior, and outfit interaction unknown | Black Book guide text only |

## API, compatibility and version notes

| Surface | What the exact JAR shows | TMM status / constraint |
| --- | --- | --- |
| Dependencies | `META-INF/neoforge.mods.toml` requires NeoForge `[21.1,)`, Minecraft `[1.21.1,1.22)`, Goety `[3.1.4,)`, Cataclysm `[3.31,)`; the artifact credits Lionfish API and Modrinth metadata lists it as a dependency. | TMM pins Goety 3.2.2 and Cataclysm 3.33, which meet declared minimum versions; this is metadata compatibility, not a functional guarantee. |
| Registries/data | `goety_cataclysm` items, blocks, entities, recipes, advancements, loot, tags and a NeoForge biome modifier; extends `goety` guidebook resources. | Exact resource files inspected in locked artifact. No TMM overrides identified in this pass. |
| Source | Public repository exposes only `master` at `6b5c8dd4…`; its Gradle metadata targets 1.20.1/Goety older line. | Do not use this code as 1.21.1 proof. Exact pinned JAR resource data remains evidence for JSON; runtime bytecode and Fabricator behavior cannot be verified from the available source. |
| License | Modrinth lists ARR; JAR metadata says ARR. | Research only. Do not redistribute artifact or bundled art. |

## Quest and teaching opportunities (research only)

| Goal | Actionable teaching | Reference | Detector and risk |
| --- | --- | --- | --- |
| Discover addon branch | open Black Book → Cataclysm → choose a focus, summon, or mutation branch | Cataclysm categories | FTB cannot prove page reading; use manual fallback until tested |
| Craft first addon focus | inspect required base focus and Cataclysm drops, then perform the specified altar craft | named Focus entry + JEI | item possession proves result only; altar detector unsupported until tested |
| Upgrade a servant | identify exact source entity and ritual; set up the entity and ingredients; activate the conversion | Mutations entry + ritual recipe | require actual target entity/form after operation; beware destructive conversion failure |
| Heal or recover servant | use guide-listed heal item or test death/recovery branch in disposable world | Summons entry | must measure health and owner binding; no quest should demand killing a permanent servant without safe recovery proof |
| Build Fabricator | gather Forge recipe ingredients, place required ritual inputs, complete craft | Crafts → Artifice → Fabricator | crafted block task is honest for construction only; no operational quest until a Fabricator recipe is verified |

## Existing quest reconciliation

The current Goety chapter is a generic checkmark stub. No addon-specific task teaches the Cataclysm index, one focus craft, a servant summon/conversion, servant care, or Fabricator use. This profile is research input; quest content remains unchanged.

## Evidence ledger

| Claim | Inspected source | Evidence and limits |
| --- | --- | --- |
| Exact artifact identity | `mods/goety-cataclysm.pw.toml`; Modrinth version API record `gv1JY0TU`; downloaded file `goety_cataclysm-1.21.1-1.8.4.jar` | SHA-512 exactly matches lock; SHA-1 `b8b066f0ee12099505306043455cdc879e2d3949`; JAR internal mod version is 1.21.1-1.8.4. Modrinth calls the release 1.9.0 while reusing the 1.8.4 filename. |
| Loader and dependencies | `META-INF/neoforge.mods.toml` inside exact JAR | requires MC 1.21.1+, NeoForge 21.1+, Goety 3.1.4+, Cataclysm 3.31+; runtime load order metadata says AFTER for both mods. |
| Guide and content breadth | exact JAR `assets/goety/patchouli_books/black_book/en_us/.../cataclysm` | 15 Cataclysm category JSONs and 51 entry JSONs; guide content inspected, not rendered in client. |
| Recipes and advancement | exact JAR `data/goety_cataclysm/recipe/`, `advancement/`, `loot_table/` and `neoforge/biome_modifier/` | 61 recipe JSONs, including 28 focus, 13 thrall and 10 conversion recipes; 12 advancement JSONs and 9 loot JSONs. Recipe declaration does not prove runtime operation. |
| Upstream description/version | [Modrinth project](https://modrinth.com/mod/goety-cataclysm), [locked version ID page](https://modrinth.com/mod/goety-cataclysm/version/gv1JY0TU) | official description says this bridges Goety and Cataclysm; current version label is 1.21.1-1.9.0 but locked file bytes identify themselves as 1.21.1-1.8.4. |
| Public source gap | `Polarice3/Goety_Cataclysm`, HEAD `6b5c8dd4f0b7f307aba8005e7634ade8f4438c5b` | inspected public tree uses Minecraft 1.20.1 and Goety 1.20-era dependency line; not version aligned, so not cited for 1.21.1 code semantics. |

## Disposable-world test plan

| Test | Positive and boundary case | Expected observable | Performed |
| --- | --- | --- | --- |
| Guide access | Open Black Book with addon present; compare against addon absent in disposable instances | Cataclysm categories appear only with addon and all recipe links render | no |
| Focus altar | Complete an easy focus recipe with correct activation item; repeat with wrong `craftType`, missing ingredient, insufficient soul and interrupted ritual | one focus produced on valid setup; exact energy/item loss and retry state recorded on failure | no |
| Conversion target | Use valid owned mob and wrong-tag/unowned mob against same ritual; interrupt after activation | correct servant conversion only; no false conversion; record consumed gear/entity and owner preservation | no |
| Thrall summon | Summon same servant below and at cap; retry with insufficient souls; reload chunk/server | correct owned entity, enforced cap, persistence, and truthful task credit | no |
| Structure servants | Compare valid/invalid Coralssus coral types, Golem layouts, Netherite Monstrosity center block and cap | valid structure creates exactly one servant; invalid variation leaves inputs/world as documented | no |
| Healing/recovery | Measure health before/after each listed feed; death recovery with correct vs other player's head/skull/spirit | exact item use/heal amount; owner lock and recovery action confirmed | no |
| Fabricator | Craft block, test each GUI slot/input/output and automation face with valid/invalid recipes | actual recipe behavior, consumption, output, FE/soul cost and sided transfer captured | no |
| Boss gates/loot | controlled Cataclysm boss kill with Goety addon enabled; inspect item drops and altar recipes | verify exact drops and whether they create repeatable higher-tier combat | no |
| Multiplayer/server | player without addon joins server with addon; then both sides with matching versions | loader rejects missing addon or synchronizes content correctly; no desync/crash | no |

## Unknowns and next research

- [ ] Compare every Cataclysm recipe's item IDs and tags against the exact pinned Cataclysm 3.33 JAR and establish which required drops are available from which boss.
- [ ] Map all 28 focus recipes to exact effect, damage, cooldown, Soul cost, staff modifiers and unsafe/griefing boundaries from the locked JAR.
- [ ] Audit 13 summon recipes, 10 entity conversions, target tags, owner transfer, summon caps, death drops and event ordering; separate recipe display proxies from spawned entities.
- [ ] Identify the Fabricator operational recipes and sided automation API from the exact class bytecode or client runtime.
- [ ] Parent Goety 3.2.2 common transaction/failure costs are now mapped. Verify addon-specific `ritual_type` overrides, research gates, servant spawn/owner handling and every conversion/summon debit from the pinned 1.21.1 code or client.
- [ ] Run client/world tests for guide rendering, entity behavior, loot, servant recovery and server sync before promoting beyond `researching`.
- [ ] Hard blocker for further class-level claims: reacquire the exact pinned JAR for inspection or locate version-aligned 1.21.1 source. The only public source repository exposes a 1.20.1 branch, and the Modrinth/CurseForge binary is not available to the current repository-inspection tool. Fabricator operations, addon overrides, and exact servant persistence behavior cannot be inferred safely.

## Parent ritual contract carried forward

The 28 focus and 13 summon recipes declare the Goety ritual recipe type; the 10 conversion recipes also use Goety ritual definitions. In the current pack, parent Goety 3.2.2 source shows that the shared altar engine inserts one activation item, charges the recipe's Soul Energy cost once per 20 ticks, progressively consumes pedestal ingredients, and on a base interrupt returns the activation item without refunding previously paid souls or consumed ingredients. This is a useful preparation warning for addon altar recipes, but it is an **inference from the parent engine**: addon 1.21.1 ritual subclasses and overrides have not been inspected. Confirm a successful, interrupted, and one-pulse-short recipe before teaching exact addon failure costs. See [Goety profile](goety.md).

## Revision notes

- 2026-11-16 — Applied the current Goety 3.2.2 parent ritual transaction findings as a clearly labeled inference for the addon's `goety:ritual` recipes: Soul Energy cadence, progressive ingredient consumption and no base refund on interruption. Confirmed the official release page's 1.21.1 changelog mentions servant persistence and robe/crown healing fixes, but did not treat that as client validation. Rechecked the linked public source repository: its only branch is 1.20.1, so bytecode/Fabricator work is blocked pending exact artifact access or aligned source. No quest/runtime edits.
- 2026-11-16 — Reconciled the profile's pack snapshot after Goety PR #154 and made the remaining bytecode blocker explicit: the transient exact JAR link is dangling and the only inspected public source is for 1.20.1. The existing exact-artifact resource inventory remains valid; no new class-level claims were inferred. No quest/runtime files changed.
- 2026-11-15 — Added exact hash-matched Goety Cataclysm 1.21.1 artifact profile. Indexed its 51 English Black Book entries, 61 recipes, focus/servant/conversion branches, guide-described construct servants, dependencies, and version-label discrepancy. Recorded source mismatch and all client/behavior gaps; no quest or runtime files changed.
