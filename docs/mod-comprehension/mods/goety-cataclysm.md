# Goety Cataclysm — mechanics and gameplay profile

| Field | Value |
| --- | --- |
| Manifest ID / actual mod ID | `goety-cataclysm` / `goety_cataclysm` |
| Category / parent or addons | magic addon; extends [Goety](goety.md) and L_Ender's Cataclysm |
| MC / loader / pack target | 1.21.1 / NeoForge / research base `cb8e2dcab59cc625500b3ca0329df1511823610b`; local artifacts independently hash matched |
| Pinned version / artifact / hash | `mods/goety-cataclysm.pw.toml`; Modrinth version ID `gv1JY0TU`; file `goety_cataclysm-1.21.1-1.8.4.jar`; SHA-512 `45e390fb44ea7aa6b5f81b95b57ca224489a527a66167de1bdcf9a53c71cb5a54f6d29b69fa30b3f342e853c30bee677cf2c3701d141edf76b0be8515b6b6b60` |
| Binary identity | Exact downloaded file hash matches the packwiz lock; its `META-INF/neoforge.mods.toml` reports `1.21.1-1.8.4`, while Modrinth's version record labels the file/version `1.21.1-1.9.0`. Treat the locked hash and JAR metadata as artifact identity; this is a naming/version-label discrepancy. |
| Source snapshot | Public source snapshot `6b5c8dd4f0b7f307aba8005e7634ade8f4438c5b` targets 1.20.1 and is not binary provenance. This pass directly inspected the locked 1.21.1 addon and Goety 3.2.2 classes using `javap` and transient CFR 0.152 output; see E-BC below. |
| Guide/documentation version gap | 51 English Black Book entries and 15 categories for Cataclysm content are bundled in the locked JAR. No live Patchouli or client test. |
| Profile status | researching |
| Evidence reached | exact addon and parent JAR hashes verified; 61 recipes crosswalked; Fabricator and parent training/altar debit paths inspected; no gameplay tests |
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
| Focus spells | 28 registered spell classes, base cost/time/cooldown, enchantments and producer effects mapped in [focus appendix](goety-cataclysm-focuses.md) | acquisition routes and operation inventory mapped; delegated damage/friendly-fire and runtime remain partial | Cataclysm → Focuses; matching focus entry | not run | test casts, costs, staff interactions, limits and damage/effects |
| Servants and construction | 13 thrall recipe JSONs plus guide entries for golems/large servants; latest official 1.21.1 release notes say servant persistence and robe/crown healing were fixed | summon recipe declarations and construction routes mapped; applied fix behavior, cap, command, AI and persistence are not client-tested | Cataclysm → Summons; matching recipe/JEI | not run | verify the pinned file's reported fixes, owner/cap behavior, chunk persistence, healing, death and recovery |
| Entity conversions | 10 `goety:ritual` recipe JSONs | input entity tags, activation item, pedestals and recipe costs mapped. The current Goety 3.2.2 parent engine consumes the activation item at start, charges Soul Energy once per 20 ticks, and stages pedestal consumption; its base interrupt returns the activation item but not already-paid souls/ingredients. Applying that transaction contract to these addon recipes is an inference until the pinned addon ritual classes are inspected. | Cataclysm → Mutations | not run | test wrong entity/tag, interruption, owner preservation and exact recipe debit; inspect for addon overrides when a 1.21.1 source/artifact becomes available |
| Crafting materials and Fabricator | 61 addon recipe JSONs total; guide covers Abyss/Primordial Essence, mechanized iron and Fabricator | all 61 recipes crosswalked; Fabricator operation and parent training interface inspected; effective automation remains untested | Cataclysm → Crafts / Artifice; JEI | not run | client-test fuel queue, blocked spawning, cap and hopper input |
| Loot / world / boss links | 12 advancements, 9 loot JSONs, spawn modifier and tags | drops/loot injections not fully reconciled with Cataclysm pin | Cataclysm → World/Bestiary; Cataclysm guide | not run | audit boss drop values and world spawn predicates |

See the [altar setup and research-gate walkthrough](goety-ritual-gates.md) for the Forge build, nine addon-used ritual families, Bygone/Buried/Warred/Terminus acquisition, personal research checks and positive/boundary tests.

See the [28-focus operation crosswalk](goety-cataclysm-focuses.md) for exact registered spell classes, raw defaults, staff/crouch variants, temporary summon lifetimes/caps and payment boundaries.

See the [servant care/recovery crosswalk](goety-cataclysm-servants.md) for accepted healing inputs, last-item ordering, owner-bound payloads, spirit/core replacement routes and skull/head reconstruction patterns.

## Guidebook onboarding and usage

- **Guide:** Goety's existing Black Book; the addon adds category `assets/goety/patchouli_books/black_book/en_us/categories/cataclysm.json` and entries under `.../categories/cataclysm/` and `.../entries/`.
- **First access:** craft and open the base Goety Black Book as described in [Goety's profile](goety.md). The addon does not provide a separate book or new initial gate.
- **Reference map:** Cataclysm categories cover Crafts, Focuses (Abyss, Geomancy, Necromancy, Nether, Storm, Void, Wind), Mutations, Summons, and World/Bestiary. Individual entries pair guide prose with JEI recipe references. The exact English guide bundle contains 15 category files and 51 entry files.
- **Guide limitations:** entries explain broad summon behavior and some healing/repair methods, but most pages rely on displayed recipe data. They do not establish how ritual failure consumes soul energy or items, whether all servants persist through unloads, or whether boss drops remain available after conversion. Teach those mechanics only after disposable-world tests.

## Mechanics and typed input/output

| System | Inputs and costs in locked resources | Output and trigger | Boundaries / evidence |
| --- | --- | --- | --- |
| Focus recipes | 28 focus recipes: most are `goety:ritual` with an activation focus, named altar `craftType`, soul cost, duration, and pedestal item/tag ingredients; several are vanilla shaped crafts. Examples include Abyssal Beam (Abyss Essence + Cataclysm Athame + Phantom Membrane + Ectoplasm + amethyst block + Prisma Beam Focus; Deep ritual, 1 soul per advance for 10 advances (nominal 10 souls / approximately 10 seconds at 20 TPS)) and Extinct Flame (Ignitium Ingot + Ectoplasm + ender pearl + blaze rod; Expert Nether, 5 souls per advance for 10 advances (nominal 50 souls / approximately 10 seconds at 20 TPS)). | Focus item, normally via the Dark Altar ritual; shaped recipes craft immediately. Focuses then cast Cataclysm-inspired projectiles, areas, summons or continuous effects using Goety's wand/staff system. | Inputs and recipe fields are exact from JAR JSON. The parent mod's ritual code determines actual debit/consumption order; no in-world test. Some casts are area damage or persistent effects and need safety/boundary tests. |
| Thrall recipes | 13 `goety:ritual` recipes use Goety altar craft types, soul cost/duration and pedestal ingredients. Examples span Draugr, Elite/Royal Draugr, Aptrgangr, Deeplings, Prowler, Urchinkin, and Zombie Amethyst Crab. | The JSON declares summon entities and can use a display proxy in its JEI result; actual spawn/ownership behavior is a ritual/runtime question. | Parent Goety's common ritual framework provides the expected soul debit and progressive ingredient handling, but any addon-specific ritual override is unverified. Summon caps, owner/faction handling and successful spawn remain untested. |
| Entity conversion | 11 `goety:ritual` recipes use `convert_tamed` or `convert_complete_tamed`. They target entity tags and require activation item plus pedestal items. Examples: a tamed baby Leviathan + Aging Cream + Tidal Claws/Primordial/Abyss Essences → Leviathan (`soulCost=10`, `duration=10`: nominal 100 souls); a Blaze + Dying Ember plus swords, blaze rods, Ectoplasm and soul-fire block → Ignited Berserker (`5 × 10`: nominal 50 souls); a Draugr + Necro Cape and Goety gear/focus → Draugr Necromancer (`5 × 30`: nominal 150 souls). | Existing entity becomes the named servant after a valid rite; JSON `result` is `goety:jei_dummy/none`, a recipe display placeholder, not an item output. | Source JSON identifies required target tags but does not prove runtime target selection, entity consumption, ownership, or failure refund. A wrong-tag creature is the critical negative case. |
| Golem/construct assembly | Guide instructions use Animation Core on the center Coral (Coralssus), center Copper Block (Coral Golem), or center Void Stone (Ender Golem); Netherite Monstrosity uses Infernal Forge on center Reinforced Redstone Block. | Places a servant entity from blocks/structure rather than ordinary item output. | Coralssus requires one coral type among Fire, Horn or Tube coral; Ender Golem allows Obsidian or Obsidian Bricks; Netherite Monstrosity shares Redstone Monstrosity summon cap. These are guide claims; structure recognition and costs need testing. |
| Servant upkeep/recovery | Guide names healing inputs: fish for many abyss servants; bones/rotten flesh for Draugr; iron for Watchers/Prowler; Netherite Block/Lava Bucket/Lava Power Cell or Soul Fire for Netherite Monstrosity. Some receive passive healing from owner robes/capes. Death drops can include a bound skull/head, spirit or core used for cheaper recovery. | Right-click healing; passive robe-driven healing; death loot can become a resurrection/repair route. | Listed heal amounts, owner checks, recovery paths and debit ordering are now in the servant appendix; unlisted/inherited care and delivery persistence remain open. Do not promise universal or guaranteed resurrection. |
| Fabricator | An Altar ritual crafts `goety_cataclysm:fabricator` using Fireball Focus, Cursed Cage, Witherite Ingot, two Mechanized Iron Blocks and an Anvil; Forge craft type, 1 soul per advance for 30 advances (nominal 30 souls / approximately 30 seconds at 20 TPS). | Creates a block; guide identifies it under Crafts/Artifice. | Operating input is one Mechanized Iron Block per queued Watcher, inherited from the parent training block; see the exact operating audit below. No FE/fluid input occurs in the inspected production path. [E-BC] |
| Boss loot and world | Locked JAR includes entity loot, injected loot for Ender Golem and Leviathan, a biome spawn modifier, and advancement triggers for servant summon/conversion and Fabricator crafting. | Items from Cataclysm encounters feed recipes and recovery; entities/spawns are triggered by Cataclysm/Goety systems. | Exact parent Cataclysm drop tables, item quantities and stage gates are external dependencies and were not audited here. Do not equate an advancement's existence with a player-visible how-to. |

### Acquisition and progression boundaries

This addon is optional after the base Goety start. Its recipe JSONs identify `research` fields on some rituals (`bygone`, `buried`, `terminus`) and the Goety altar `craftType`, but these are parent-mod research gates and require the parent profile's unlock predicates. Cataclysm boss loot is a prerequisite for several advanced foci and conversions. The addon adds no dedicated XP, mana, fluid or FE system in the inspected data. Most effects cost Goety Soul Energy through parent ritual/casting paths.

A recipe result of `goety:jei_dummy/none` on conversion recipes and summon recipes is display metadata, not a produced item. The actual result is a spawned or transformed entity handled by Goety's ritual serializer. Treat that statement as a source-backed recipe interpretation requiring runtime confirmation, not a tested outcome.

## Walkthrough blueprint

| Stage | Gate/choice | Player action and visible result | Named guide/JEI reference | Honest quest detector/fallback | Coverage |
| --- | --- | --- | --- | --- | --- |
| 0. Open base Goety guide | prerequisite to learn recipes | craft and open Black Book, then select Cataclysm category | Goety → Cataclysm | possession only proves crafting; manual check to confirm guide was opened | path explained; display not tested |
| 1. Establish Goety ritual access | parent prerequisite | follow base Goety progression until required focus, altar, research and Soul Energy are available | Goety rituals and focus entries; addon recipe in JEI | detect actual recipe output or conversion, not altar possession | parent progression referenced |
| 2. Choose a focus branch | optional Cataclysm drop/resource gate | inspect recipe, defeat/loot the needed Cataclysm source, activate matching recipe at altar or craft shaped focus | Cataclysm → Focuses; JEI recipe category | item acquisition can detect focus craft, but can't prove altar operation; task/event detector unverified | all 28 recipe routes crosswalked; cast effects/research remain partial |
| 3. Bind a servant | optional summon/conversion branch | make required focus/materials; perform `thrall` summon ritual, convert a valid owned/tagged mob, or assemble the guide-described structure | Cataclysm → Summons/Mutations; JEI | require actual servant/converted mob check; manual verification if FTB lacks trigger | broad path, target predicates and costs untested |
| 4. Heal, command and recover | practical use branch | use the named repair/heal item; test Goety command wand/staff; test recovery item after controlled servant death | relevant Summon entry | item task alone doesn't prove healing; test health/owner state and use a manual fallback | guide directions known, effect unknown |
| 5. Fabricator and materials | optional advanced craft branch | gather listed Forge altar inputs; construct Fabricator; test one documented operation before teaching automation | Crafts → Artifice → Fabricator; JEI | block possession is not operation; observe a spawned Watcher or use an honest manual fallback | construction and operating path mapped below; live behavior untested |
| 6. Optional boss/servant completion | open-ended | explore Cataclysm content and choose selected focuses, mutations, servants and recovery paths | Cataclysm → World, Bestiary, Focuses, Summons | milestones should be selected branches, not require every item/entity | scope is broad; full capstone inventory remains open |

## Internal synergies and useful setups

| Combination | Full chain | Payoff | Cost/risk | Evidence |
| --- | --- | --- | --- | --- |
| Boss drop → Goety focus | Cataclysm encounter/item → Goety altar recipe → focus → matching wand/staff cast | Makes a defeated boss's signature attack available as a spell | Could duplicate high-impact boss abilities; exact damage/cooldown and boss-drop rate need tests | exact recipe/guide data; cast untested |
| Tamed mob → upgraded servant | base/ordinary Cataclysm servant → conversion ritual → stronger specialist servant | Preserves the servant-investment route while unlocking specialized abilities | Ritual may consume or replace input and uses rare boss ingredients; owner and failure behavior unknown | conversion recipe data + Black Book |
| Servant recovery item → repeated field use | servant dies → core/head/spirit drop → reassemble/rebind | May reduce loss from expensive high-end summons | Drop may not be guaranteed; skull/head owner-lock is guide-described; could bypass boss gates if duplicated | guide pages + loot resources; not tested |
| Robe/cape → passive servant heal | equip named Abyss Robe or Necro Cape → selected servant regenerates | Reduces food/repair burden for a matching servant group | Exact rate, range, chunk behavior, and outfit interaction unknown | Black Book guide text only |

## Pinned-bytecode operating audit — 2026-11-16

### Evidence scope and ritual units

**E-BC:** addon SHA-512 is the full hash in the identity table. Parent `goety-3.2.2.jar` SHA-512 is `d68103012faad1b74cf96017aa22711d80033d04035e98e5452f4f70d6fbc30c6523b4d3f561fedba798e116055caf598432b472b13e8a11dca07d00468b8b46`, independently matched to [its pin](../../../mods/goety.pw.toml). Claims below are original analysis of these classes, not copied code or a client test:

- Addon `com/Polarice3/goety_cataclysm/common/blocks/entities/FabricatorBlockEntity.class`: `setVariant`, `isFuel`, `maxTrainAmount`, `summonLimit`, `tick`.
- Parent `com/Polarice3/Goety/common/blocks/entities/TrainingBlockEntity.class`: constructor, `placeItem`, `startTraining`, `tick`, `hasNearbyTarget`, container/sided methods and network NBT.
- Parent `com/Polarice3/Goety/common/blocks/TrainingBlock.class`: placement owner, hand controls and redstone output.
- Parent `.../blocks/entities/DarkAltarBlockEntity.class`: `startRitual`, `tick`, `stopRitual`, `clearRitual`; `.../crafting/RitualRecipe$Serializer.class`; `.../ritual/Ritual.class`, `CraftItemRitual.class`, `ConvertRitual.class`.

The [61-recipe crosswalk](goety-cataclysm-recipes.md) records every optional recipe route and all 11 conversions. **Correction:** JSON `duration` is a count of charged one-per-20-game-tick advances, not ticks; `soulCost` is charged on every such advance, not once. With uninterrupted valid operation, nominal total is `duration × soulCost`. Fabricator construction is therefore 30 souls over about 30 seconds at 20 TPS; the Leviathan conversion is 100 souls over about 10 seconds. Tick alignment, lag and interruptions affect elapsed time. These are code-derived nominal values, not measured throughput. [E-BC]

The altar splits one activation item into its slot at start. Pedestal ingredients are consumed progressively. On ordinary interruption with a restored caster, `stopRitual(false)` calls the interrupt hook, drops the activation item and clears progress/consumed-ingredient tracking; inspected default interrupt code plays a failure sound and does not refund souls or already consumed pedestal ingredients. `CraftItemRitual.finish()` consumes activation and puts the crafted output into the altar handler. `ConvertRitual.finish()` consumes activation before calling the conversion helper. Therefore interruptions are not a full refund, and recipe initiators are not generally reusable tools. External ritual-type callbacks and the full conversion helper remain separate audit items. [E-BC]

### Mechanical Fabricator — actionable setup

**Guide reference:** Black Book → Cataclysm → Crafts → Artifice → **Mechanical Fabricator**, bundled entry `assets/goety/patchouli_books/black_book/en_us/entries/crafts/artifice/fabricator.json`. Its ritual page links `goety_cataclysm:fabricator`.

1. Establish Goety's **Forge** ritual setup using the parent Black Book/JEI structure reference; the [altar-gate appendix](goety-ritual-gates.md) gives its exact minimum setup.
2. Activate the Fabricator recipe with one `goety:fireball_focus`; supply pedestal ingredients: one `goety:cursed_cage`, one `cataclysm:witherite_ingot`, two `goety_cataclysm:mechanized_iron_block` and one vanilla Anvil. Keep at least the nominal 30 Soul Energy available through the parent altar/cage system, and avoid interrupting the ritual. This consumes the focus on normal craft completion. [recipe + E-BC]
3. Place the resulting Fabricator yourself: parent placement stores the player as owner and addon variant selection chooses `WATCHER_SERVANT`. Leave a generous clear volume around and above the block for the spawn search. [E-BC]
4. Craft additional Mechanized Iron Blocks: JEI's shaped recipe uses **five iron-ingot-tag items and four redstone-dust-tag items** for one block. Right-click the Fabricator with one block to queue one Watcher. Up to five summons can be queued; the item is spent immediately when accepted, before any eventual spawn. Queue-full/wrong-fuel hand insertion returns false without the fuel shrink in `placeItem`. [recipe + E-BC]
5. With default unconditional mode, available cap and a collision-free search candidate, the inherited timer runs from zero to 100, then attempts spawning on the following tick. That is **101 game ticks per attempt** from a fresh zero state, nominally 5.05 seconds at 20 TPS. A Watcher entity appears near the cage, copying its owner, with guarding enabled by default. Confirm the entity and its ownership, not only the machine animation. No additional FE, fluid or Soul Energy debit appears in this inspected production path. [E-BC]

| Typed operating contract | Exact behavior / limits | Evidence |
| --- | --- | --- |
| Item input → queue | One `goety_cataclysm:mechanized_iron_block` accepted per queue entry; queue ceiling five. Fuel consumed at acceptance. | addon `isFuel/maxTrainAmount`, parent `placeItem/startTraining` |
| Queue → entity | One attempt consumes one queue entry, clears timer and stored item, then tries up to 64 random nearby candidate positions. Successful owned servant is added to the server level. | parent `tick`, addon `setVariant` |
| Candidate space | Random X/Z offsets use difference of two random fractions ×4 plus block-center offset; candidate Y is block Y−1, Y or Y+1. Collision check uses the random location before final block-center snapping; keep spacious clearance and validate actual placement. | parent `tick` |
| Summon cap | Count alive WatcherServant instances returned by the current ServerLevel's `getAllEntities()` whose resolved owner is the same object as this cage's owner. Stop when count reaches `GCSpellConfig.WatcherLimit` (bytecode default **16**). This is a loaded-level count, not proof of a persistent all-dimension global cap. | addon `summonLimit`; `GCSpellConfig` initializer |
| Sensor mode | Wand/staff right-click toggles conditional spawning; crouch-use toggles guarding versus following. Crouch empty-hand use toggles area display. Ground Grimoire toggles grounding through parent handling; its broader semantics remain outside this audit. | parent `TrainingBlock` |
| Sensor area | Full-block AABB from offsets −4 to +4 in all axes. `hasNearbyTarget()` returns the predicate result for the first alive nonspectator candidate when an owner resolves; it does not necessarily scan onward after a non-target. Neutral-first/mixed-mob behavior needs a boundary test. | parent `hasNearbyTarget/predicate` |
| Paused attempt | At ready time, cap or missing sensor target prevents queue decrement and holds the ready timer. Adding fuel while capped can still spend the item into the queue. | parent `tick/placeItem` |
| Blocked spawn **loses entry** | Once conditions permit an attempt, queue decrement happens **before** all collision tests/entity creation. If all 64 candidates fail, there is no queue restoration in this path. Fuel was already spent at insertion. Do not promise retries without cost. | parent `tick` |
| Native automation | Parent implements one-slot `WorldlyContainer`: slot 0 advertised on every face; accepts fuel when server-side and queue not full; denies extraction. `setItem` queues from a copy; `getItem` always empty. This is queue insertion, not a persistent output inventory. Test vanilla hoppers; no generic logistics mod is required or authorized. | parent sided/container methods |
| Redstone | Block emits level 15 while queue amount is positive, including cap/sensor stalls. It is a queue-activity output, not evidence of successful spawning or a demonstrated redstone input switch. | parent `getSignal`; `tick` sets POWERED from `isTraining` |
| Persistence / permission | Queue, timer, entity ID and mode flags are written to NBT; inherited owner/network persistence needs unload/reload validation. Hand-control methods inspected contain no explicit owner check; test another player's interaction and pack protection separately. | parent network methods / `TrainingBlock` |

**Local config observation:** the existing QA instance's `config/goety_cataclysm/goety_cataclysm-spells.toml` contains `watcherLimit = 16`, agreeing with the addon default at inspection. This is a file observation (SHA-256 `1d5f88bdaefe184cd6c2ab6d911c0ae61c4ab1d7cb727190391fc2fd1b63c22a`), not proof of the effective setting in a loaded world or this newer integration base. [local-file, 2026-11-16]

### Quest coverage for the Fabricator branch

| Optional lesson | Prerequisite → action → visible result | Truthful detection / reward direction | Status |
| --- | --- | --- | --- |
| Read Mechanical Fabricator | Parent guide access → open the named Artifice entry and JEI ritual → identify Forge setup and ingredients | Manual reading confirmation; XP plus modest vanilla construction supplies, reviewed against the global reward/task inventory | guide data inspected; not client-opened |
| Make the cage | Forge setup and Cataclysm Witherite → perform the exact ritual → collect Fabricator | Native `goety_cataclysm:craft_fabricator` is **inventory_changed**, an acquisition proxy despite its name; it cannot prove ritual operation. XP plus ordinary food, not a free gated focus/material | advancement JSON inspected; no task implementation |
| First Watcher | Place cage with room and one fuel block → wait for entity → verify owner/guard mode | Manual operation fallback until an exact behavior detector is established. No item output exists to use as a production detector. XP plus ordinary repair supplies after reward-policy review | bytecode path mapped; no client test |
| Use modes safely | Use wand/staff toggles → compare cap/sensor pause with blocked spawning → confirm guarding/following | Manual behavior test; never ask players to waste rare inputs as mandatory progression. XP plus safe vanilla utility item | optional advanced lesson; boundary tests below |

The current generated Goety chapter contains one optional checkmark quest `544D303000800001`, task `544D403000800001`, rewards 2 XP and one paper (`544D903000800001`, `544DA03000800002`). No Goety rows were found in `manifest/quest_content.json` or `quest_content_resolved.json`; the generated scaffold does not establish an authored addon walkthrough. Reading, crafting, operating, maintaining and optional completion remain unauthored. This pass changes no quests.

### Targeted disposable-world tests (not run)

| Test | Positive case | Boundary case / observations to record |
| --- | --- | --- |
| One paid Watcher | Player-placed cage, spacious clearance, zero existing Watchers, one block → one owned Watcher | Wrong item and sixth queued item must not be hand-consumed; inspect before/after counts, queue, owner and timer |
| Collision debit | One fuel in clear test area → spawn | Fully obstruct every candidate volume before feeding one block; record queue decrement with no entity and no returned fuel; restore room and check no unpaid retry |
| Cap and dimensions | Under cap → summon | At 16 loaded same-owner Watchers → queue waits; remove one → resumes. Compare another owner, unloaded chunks and another dimension before making global-cap claims |
| Sensor ordering | Valid hostile alone in shown area → conditional spawn | Place harmless living candidates before/with hostile; vary entity enumeration/order and outfit/faction neutrality; observe no-target stall versus target presence |
| Automation/redstone | Vanilla hopper feeds one block → one queue entry; queue emits 15 | Full queue must reject further input; no extraction/output item; record real hopper source decrement and blocked/sensor queue signal |
| Persistence and control | Queue/modes survive controlled chunk unload and reload | Other player toggles/feeds cage, owner offline, break/re-place; record ownership, fuel loss and pack protection |
| Ritual economics | Fabricator recipe supplied with adequate souls/materials → nominal 30 charged advances | Interrupt after known advances/pedestal consumption; compare activation return, souls and consumed/unconsumed ingredients. Separately test missing Forge structure and insufficient souls |

## API, compatibility and version notes

| Surface | What the exact JAR shows | TMM status / constraint |
| --- | --- | --- |
| Dependencies | `META-INF/neoforge.mods.toml` requires NeoForge `[21.1,)`, Minecraft `[1.21.1,1.22)`, Goety `[3.1.4,)`, Cataclysm `[3.31,)`; the artifact credits Lionfish API and Modrinth metadata lists it as a dependency. | TMM pins Goety 3.2.2 and Cataclysm 3.33, which meet declared minimum versions; this is metadata compatibility, not a functional guarantee. |
| Registries/data | `goety_cataclysm` items, blocks, entities, recipes, advancements, loot, tags and a NeoForge biome modifier; extends `goety` guidebook resources. | Exact resource files inspected in locked artifact. No TMM overrides identified in this pass. |
| Source | Public repository exposes only `master` at `6b5c8dd4…`; its Gradle metadata targets 1.20.1/Goety older line. | Do not use this code as 1.21.1 proof. Exact pinned JAR resource data remains evidence for JSON; Fabricator and selected parent behavior are now backed directly by the matching JAR bytecode (E-BC), not by that older source. |
| License | Modrinth lists ARR; JAR metadata says ARR. | Research only. Do not redistribute artifact or bundled art. |

## Quest and teaching opportunities (research only)

| Goal | Actionable teaching | Reference | Detector and risk |
| --- | --- | --- | --- |
| Discover addon branch | open Black Book → Cataclysm → choose a focus, summon, or mutation branch | Cataclysm categories | FTB cannot prove page reading; use manual fallback until tested |
| Craft first addon focus | inspect required base focus and Cataclysm drops, then perform the specified altar craft | named Focus entry + JEI | item possession proves result only; altar detector unsupported until tested |
| Upgrade a servant | identify exact source entity and ritual; set up the entity and ingredients; activate the conversion | Mutations entry + ritual recipe | require actual target entity/form after operation; beware destructive conversion failure |
| Heal or recover servant | use guide-listed heal item or test death/recovery branch in disposable world | Summons entry | must measure health and owner binding; no quest should demand killing a permanent servant without safe recovery proof |
| Build Fabricator | gather Forge recipe ingredients, place required ritual inputs, complete craft | Crafts → Artifice → Fabricator | crafted block task is honest for construction only; add a separate manual Watcher-operation lesson using the bytecode-backed operating audit above |

## Existing quest reconciliation

The current Goety chapter is a generic checkmark stub. No addon-specific task teaches the Cataclysm index, one focus craft, a servant summon/conversion, servant care, or Fabricator use. This profile is research input; quest content remains unchanged.

## Evidence ledger

| Claim | Inspected source | Evidence and limits |
| --- | --- | --- |
| Exact artifact identity | `mods/goety-cataclysm.pw.toml`; Modrinth version API record `gv1JY0TU`; downloaded file `goety_cataclysm-1.21.1-1.8.4.jar` | SHA-512 exactly matches lock; SHA-1 `b8b066f0ee12099505306043455cdc879e2d3949`; JAR internal mod version is 1.21.1-1.8.4. Modrinth calls the release 1.9.0 while reusing the 1.8.4 filename. |
| Loader and dependencies | `META-INF/neoforge.mods.toml` inside exact JAR | requires MC 1.21.1+, NeoForge 21.1+, Goety 3.1.4+, Cataclysm 3.31+; runtime load order metadata says AFTER for both mods. |
| Guide and content breadth | exact JAR `assets/goety/patchouli_books/black_book/en_us/.../cataclysm` | 15 Cataclysm category JSONs and 51 entry JSONs; guide content inspected, not rendered in client. |
| Recipes and advancement | exact JAR `data/goety_cataclysm/recipe/`, `advancement/`, `loot_table/` and `neoforge/biome_modifier/` | 61 recipe JSONs, including 28 focus, 13 thrall and 11 conversion recipes; 12 advancement JSONs and 9 loot JSONs. Recipe declaration does not prove runtime operation. |
| Upstream description/version | [Modrinth project](https://modrinth.com/mod/goety-cataclysm), [locked version ID page](https://modrinth.com/mod/goety-cataclysm/version/gv1JY0TU) | official description says this bridges Goety and Cataclysm; current version label is 1.21.1-1.9.0 but locked file bytes identify themselves as 1.21.1-1.8.4. |
| Public source gap | `Polarice3/Goety_Cataclysm`, HEAD `6b5c8dd4f0b7f307aba8005e7634ade8f4438c5b` | inspected public tree uses Minecraft 1.20.1 and Goety 1.20-era dependency line; not version aligned, so not cited for 1.21.1 code semantics. |
| Parent operating bytecode | Goety 3.2.2 exact SHA-512 and methods listed in E-BC above | Altar charge cadence, activation consumption, partial interruption losses, inherited training queue and sided methods; no runtime test |
| Recipe and dependency crosswalk | [61-recipe appendix](goety-cataclysm-recipes.md); Cataclysm 3.33 `com/github/L_Ender/cataclysm/init/ModItems.class` and `ModBlocks.class` | All 47 distinct explicit `cataclysm:` recipe references matched registration names in the exact parent artifact. This does not establish acquisition, effective tags or loot execution. |

## Disposable-world test plan

| Test | Positive and boundary case | Expected observable | Performed |
| --- | --- | --- | --- |
| Guide access | Open Black Book with addon present; compare against addon absent in disposable instances | Cataclysm categories appear only with addon and all recipe links render | no |
| Focus altar | Complete an easy focus recipe with correct activation item; repeat with wrong `craftType`, missing ingredient, insufficient soul and interrupted ritual | one focus produced on valid setup; exact energy/item loss and retry state recorded on failure | no |
| Conversion target | Use valid owned mob and wrong-tag/unowned mob against same ritual; interrupt after activation | correct servant conversion only; no false conversion; record consumed gear/entity and owner preservation | no |
| Thrall summon | Summon same servant below and at cap; retry with insufficient souls; reload chunk/server | correct owned entity, enforced cap, persistence, and truthful task credit | no |
| Structure servants | Compare valid/invalid Coralssus coral types, Golem layouts, Netherite Monstrosity center block and cap | valid structure creates exactly one servant; invalid variation leaves inputs/world as documented | no |
| Healing/recovery | Measure health before/after each listed feed; death recovery with correct vs other player's head/skull/spirit | exact item use/heal amount; owner lock and recovery action confirmed | no |
| Fabricator | Follow Mechanical Fabricator setup and targeted positive/boundary tests above | actual queued fuel debit, owned Watcher spawn, cap/sensor pause, blocked-spawn loss and vanilla hopper input captured | no |
| Boss gates/loot | controlled Cataclysm boss kill with Goety addon enabled; inspect item drops and altar recipes | verify exact drops and whether they create repeatable higher-tier combat | no |
| Multiplayer/server | player without addon joins server with addon; then both sides with matching versions | loader rejects missing addon or synchronizes content correctly; no desync/crash | no |

## Unknowns and next research

- [x] Match all 47 explicit Cataclysm recipe item/block IDs to pinned registration literals. Remaining: effective tags and each required item/boss acquisition path.
- [x] Map all 28 registered focus producers, raw Soul cost/cast/cooldown, accepted enchantments, major staff/crouch variants and staged callbacks. Still open: delegated damage/HP scaling, grief/friendly-fire filtering and runtime boundaries; see focus appendix.
- [ ] Complete entity conversion helper ownership/equipment transfer and full summon/attack/command behavior. Recipe routes, target tags and listed care/death/recovery ordering are mapped; runtime caps and delivery remain untested.
- [x] Map Fabricator operation and inherited sided queue interface from the exact addon/parent bytecode; client and hopper behavior remain untested.
- [ ] Parent Goety 3.2.2 common transaction/failure costs are now mapped. Verify addon-specific `ritual_type` overrides, research gates, servant spawn/owner handling and every conversion/summon debit from the pinned 1.21.1 code or client.
- [ ] Run client/world tests for guide rendering, entity behavior, loot, servant recovery and server sync before promoting beyond `researching`.

## Parent ritual contract carried forward

The addon recipes use the Goety ritual framework. This pass inspected the exact parent artifact rather than relying only on version-aligned source: shared charge cadence, craft/conversion activation consumption and ordinary interruption losses are detailed in E-BC above. The addon has 28 focus recipes, including shaped table crafts; do not label all 28 altar rituals. Audit any custom callbacks and test successful/interrupted/one-charge-short recipes before claiming effective pack behavior. See [Goety profile](goety.md).

## Revision notes

- 2026-11-16 — Added servant care/recovery appendix: family-specific heal inputs/amounts and post-shrink last-item boundary; serialized spirit/core death returns and target replacement; Prowler 27-block recovery; juvenile Remnant recovery; exact skull/head patterns and destructive pre-spawn clearing. Recorded inherited care, triggers, delivery and client gaps. Remains researching.

- 2026-11-16 — Added all 28 registered focus classes/default triples/enchantment lists and producer effects. Traced temporary-summon lifespan, current-level cap/batch behavior, recall/grounding and Summon Down; recorded Ashen Breath execution-based charge and the OctoHost cap comment/default discrepancy. Extended stage-payment report to guarded AbyssMark and post-emission portal boundary. Remains researching.

- 2026-11-16 — Added the pinned altar/research appendix: Forge placement and count rules; nine addon-used environment/structure gates; Necroturgy Grave Soil depletion; scroll consumption and capability checks; Bygone/Buried/Warred/Terminus sources and prerequisites; Ender Keeper blueprint container-loss boundary. All are artifact evidence, not gameplay tests.

- 2026-11-16 — Resumed from locally hash-matched addon and Goety 3.2.2 artifacts. Added all 61 recipe routes and 11 conversion crosswalks; corrected ritual time/rate economics, activation consumption and interruption losses. Mapped Fabricator fuel queue, ownership, 101-tick attempt, cap, sensor ordering, blocked-spawn debit, native input and redstone output. Reconciled exact scaffold IDs and added boundary tests. Remains researching; no gameplay/quest/runtime changes.

- 2026-11-16 — Applied the current Goety 3.2.2 parent ritual transaction findings as a clearly labeled inference for the addon's `goety:ritual` recipes: Soul Energy cadence, progressive ingredient consumption and no base refund on interruption. Confirmed the official release page's 1.21.1 changelog mentions servant persistence and robe/crown healing fixes, but did not treat that as client validation. Rechecked the linked public source repository: its only branch is 1.20.1, so bytecode/Fabricator work is blocked pending exact artifact access or aligned source. No quest/runtime edits.
- 2026-11-16 — Reconciled the profile's pack snapshot after Goety PR #154 and made the remaining bytecode blocker explicit: the transient exact JAR link is dangling and the only inspected public source is for 1.20.1. The existing exact-artifact resource inventory remains valid; no new class-level claims were inferred. No quest/runtime files changed.
- 2026-11-15 — Added exact hash-matched Goety Cataclysm 1.21.1 artifact profile. Indexed its 51 English Black Book entries, 61 recipes, focus/servant/conversion branches, guide-described construct servants, dependencies, and version-label discrepancy. Recorded source mismatch and the then-open client/behavior gaps; no quest or runtime files changed.

See [staged-cast payment analysis](../interactions/goety--goety-cataclysm--staged-cast-payment.md): four addon paths emit stage effects before ordinary completion payment; release handlers retain different cooldown boundaries. Source-supported, untested; no fix implemented.
