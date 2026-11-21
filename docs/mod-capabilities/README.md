# Mod Capability Atlas — complete quest-source research framework (draft v0.2)

**Owner correction, 2026-11-20:** The Atlas is the **primary factual research basis for creating the mod's quests**. It is **not a mod ranking**, scoring rubric, single-word checklist, or shortcut to an incomplete 'profile complete'. Each researched axis must disclose actual blocks, specific items, spells, operations, first obtainable version, upgrades, terminal reachable extent, resource inputs, evidence and actionable player steps.

This is the **independent, capability-first parallel workstream** tracked by [issue #410](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/410) and [draft PR #411](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/411). Original [Mod Comprehension](../mod-comprehension/WORK-CHAT-INSTRUCTIONS.md) work continues to research progression, inputs/outputs and in-game walkthroughs. The Atlas may cite and reconcile that work without modifying the files of the original project. The two can eventually be integrated by IDs and reviewed links, not by discarding either set of evidence.

## Read first

1. [The complete 29-axis field guide](AXIS-FIELD-GUIDE.md): for **each axis**, specifies what concrete objects/effects, lower and upper capabilities and quests must be documented.
2. [Energy and progression](ENERGY-AND-PROGRESSION.md): resource storage in **blocks, player state and items**, plus formal tier/branch graph and complete unlock steps. **Energy is not equivalent to FE**.
3. [Copyable detailed mod dossier](templates/MOD-CAPABILITIES.md): inventory of exact items/blocks/actions, per-capability long-form records, resource flows, tier progression and quest-ready matrix. The 29-row axis matrix within it is a **navigation index**, never the content itself.
4. [Machine-readable axis registry](taxonomy.v0.json) for stable axis IDs, descriptions and mandatory record fields.
5. [Existing quest walkthrough acceptance standard](../QUEST-WALKTHROUGH-STANDARD.md): the eventual chapter must be a runnable guide using quests + JEI + in-game guide.

## Active pilot and research backlog

**Neo Vitae pilot (2026-11-20) — source-backed partial, not yet quest-source-ready:**

- [Neo Vitae 29-axis detailed capability dossier](mods/neovitae.md): typed block/player/item energy, six formal altar tiers, representative 21 multi-axis capability records, concrete objects, evidence and missing recipe coverage.
- [Neo Vitae quest-authoring map](mods/neovitae-quest-map.md): 38 proposed main/optional teachable steps with exact player actions, observable outcomes, honest FTB detector uncertainty and safe reward directions. These are research IDs, not a committed quest chapter.
- [Neo Vitae declared registry inventory](inventories/neovitae-registries.md): 211 statically named items, 90 blocks and six Orb data-map values from version-aligned upstream source; **not** a pin-matched JAR dump or evidence that every object is craftable.
- [Neo Vitae operating-family registrations](inventories/neovitae-operations.md): 51 ritual declarations (48 regular, 3 imperfect), 26 array-effect items and 18 sigil items; an **identity/discovery index**, not a complete recipe/operation dossier.

**New complete generated recipe indexes (second pass, 2026-11-20):**

| Source recipe family | Full exact-data index | Source-generated files |
| --- | --- | ---: |
| Ara Vitae | [All altar conversions](inventories/neovitae-altar-recipes.md) | 31 |
| Alchemy Array | [All offering pairs and effects](inventories/neovitae-array-recipes.md) | 48 |
| Tabula Vitae | [All recipes: Orb tier, Anima EV, inputs, output, ticks](inventories/neovitae-tabula-recipes.md) | 116 |
| Hellfire Forge | [All regular + transform recipes](inventories/neovitae-hellfire-forge-recipes.md) | 90 |
| Athanor | [All tool/EV-fluid/Spiritus/outputs](inventories/neovitae-athanor-recipes.md) | 74 |
| Flask | [All 113 effect/catalyst/transformation recipes](inventories/neovitae-flask-recipes.md) | 113 |
| **Total** | 6 separate direct-source catalogs with one file link per recipe | **472** |

Also see [starter/gating machine recipes](inventories/neovitae-core-station-recipes.md), [ritual/sigil/Orb data-map costs and native event hooks](inventories/neovitae-resource-and-ritual-stats.md), and [16-tier Blood Tank vs creative FE Blood Battery](inventories/neovitae-block-storage.md).

**Important progression corrections:** A Tier-1 Discipulus Orb is needed for Tabula Scribe/Dagger, Binding Reagent needs tier-3 Magus Orb, and level-1 Awakened Activation Crystal's first source recipe is Tier IV. The dungeon rites use Weak Crystal (80,000/150,000 EV). These findings are now reflected in the Neo quest map; exact pack runtime exceptions and alternative loot remain unverified.

**Creative/debug warning:** the Blood Battery generates FE from a redstone signal without consuming magical energy; it should not be treated as normal magic→FE conversion or used as a survival quest reward before an explicit reachability review. Its block storage is **10,000,000 FE**, distinct from EV fluid tanks and player Anima.


**Remaining work before quest-source-ready:** the 472 generated recipe JSONs are now **exhaustively indexed for those six directories**; still compare against pinned JAR and actual pack recipe overrides, gather exact 3-D ritual and altar structures, resolve effect code and non-recipe capabilities, test effective creature/gate/EMC rules and truthful FTB per-player detection, and perform positive/boundary client gameplay. Another researcher should fill these gaps rather than restart the source catalog.

## The axis taxonomy — 29 research dimensions, no rankings

The initial owner-proposed 16 axes remain. There are 12 supporting suggestions from draft v0.1 and **one new explicitly owner-directed axis: progression tiers**, whose transition graph is also required **throughout every other axis**. The previous energy axis has been redefined to include **all typed magical resources and all storage locations**, not only FE.

| Group | Axis ID | Research focus | Origin |
| --- | --- | --- | --- |
| Player & combat | `gear` | Equipable/held equipment, intrinsic functionality, passive effects and equipment-provided abilities. | Owner, initial |
| Player & combat | `movement` | Movement speed, flight, dashes, blink/teleport, mounting, water/air traversal and player travel between dimensions. | Owner, initial |
| Resources & production | `ore_processing` | Crushing, refining, multiplying, washing or otherwise upgrading mined ore or raw metal inputs. | Owner, initial |
| Infrastructure | `storage` | Accessible items, fluids, magical reagents, inventories, portability, remote access and capacity. | Owner, initial |
| Infrastructure | `crafting` | Recipes, rituals, infusion, potions, item synthesis, fabrication and conversion whose goal is producing crafted outputs. | Owner, initial |
| Infrastructure | `logistics` | Transfer, routing, sorting, filtering and remote delivery of items, fluids, entities or resources. | Owner, initial |
| Player & combat | `buffs` | Temporary or maintained positive effects to players, allies and pets, including potion/spell auras. | Owner, initial |
| Cross-cutting | `external_compatibility` | Native or pack-specific interactions that bridge distinct mods, APIs, energy types, entities or mechanics. | Owner, initial |
| Resources & production | `mob_farming` | Renewable mob acquisition, capture, duplication, spawning, automated kills, drops and experience farming. | Owner, initial |
| Resources & production | `ore_generation` | Generation or acquisition of new ore/raw-ore equivalents through mining magic, miners, magical sources or repeated supply independent of an existing vanilla vein. | Owner, initial |
| Player & combat | `permanent_power` | Persistent player-state abilities, attunements, transformations, learned skills and irreversible character unlocks. | Owner, initial |
| Resources & production | `general_generation` | General-purpose creation, replication, exchange, transmutation or regeneration of useful resources from substitutes/currency/ambient systems. | Owner, initial |
| Player & combat | `direct_damage` | Entity damage inflicted by spells, powers, traps, constructs or equipment-independent actions; record separately even if cast using gear. | Owner, initial |
| Resources & production | `block_breaking` | Active breaking, mining, excavation, timbering, harvesting or destruction of existing blocks, including spells and machines. | Owner, initial |
| Actors & entities | `permanent_pets` | Persistent owned, summoned, created or bound helpers/familiars/constructs that survive normal expiration and can continue service. | Owner, initial |
| Actors & entities | `temporary_pets` | Expiring or one-shot controlled minions, summons, temporary allies and short-lived constructs. | Owner, initial |
| Player & combat | `defense` | Wards, barriers, armor-independent mitigation, resistance, immunity, shielding and hazard protection. | Suggested |
| Player & combat | `healing_recovery` | HP healing, hunger/saturation restoration, mana or ability-resource recovery and cleansing of harmful effects. | Suggested |
| Player & combat | `debuff_control` | Slows, curses, crowd control, fear, roots, silence, disarming, forced motion, enemy disablement and targeting manipulation. | Suggested |
| Infrastructure | `energy` | All distinct quantifiable power or spendable operating resources (not just FE), including EV, Anima, mana, Source, Media, lumen/starlight, soul-based charge and FE/OP when supported. Document generation, storage on blocks/players/items, use, transport, persistence, conversion and each resource's full progression. | Suggested |
| Infrastructure | `automation_control` | Redstone, logic, sensing triggers, programmable computers, state evaluation and repeatable task sequencing. | Suggested |
| Resources & production | `agriculture` | Crop/tree growth, fishing, animal breeding, food farming, biomass, gardening and natural-resource regeneration. | Suggested |
| World & utility | `detection_information` | Locate/reveal ores, structures, mobs, threats, places, recipes, status, hidden world state and exploration guidance. | Suggested |
| Infrastructure | `equipment_augmentation` | Enchantment, reforging, socketing, upgrade tiers, imbuement and modification of existing items or tools. | Suggested |
| World & utility | `world_manipulation` | Placement/building, terrain alteration, environmental changes, weather/time/light, protections and world-rule interactions. | Suggested |
| World & utility | `world_content` | Added dimensions, biomes, structures, ambient ores, worldgen, encounter spawns and discoverable features. | Suggested |
| Player & combat | `death_respawn` | Resurrection, death avoidance, corpse/grave/inventory preservation, respawn location and recovery after death. | Suggested |
| Resources & production | `trade_economy` | Merchant trades, bounty systems, currency, shops, barter, exchange markets and economic access paths. | Suggested |
| Cross-cutting | `progression_tiers` | Actual formal mod progression tiers, research stages, altar/multiblock upgrades, personal unlock trees, dimension gates and meaningful nonformal access stages. Capture every transition and resulting new capabilities, from first usable to terminal branches. | Owner, follow-up |

For important fields and specific examples under each axis, **read the field guide**; the registry's short descriptions are not sufficient to write quests. An Atlas profile is neither a leaderboard nor a finite list of 'top abilities'. A mod can expose many independent capabilities on any axis or none, and an item can legitimately link to multiple axes.

## What one complete *capability* actually looks like

For a named spell, altar operation, item, ritual, summon, production system or player state, the researcher must answer:

- **What exact in-game thing?** Item, block, multiblock, entity, spell, player ability or recipe: localized name, actual ID if verified, and what it does independently of the item carrying it.
- **How do you first unlock and use it?** Guidebook/JEI reference, prerequisites, materials, physical build/recipe, interaction, mode, typed costs and the observable result.
- **What is its bottom end?** Earliest possible operation and functional limits in a new survival world (not 'power 1').
- **How does it progress?** Every meaningful intermediate upgrade and its exact crafting/research/ritual/region/boss/energy gate; whether alternate branches are optional or mutually exclusive.
- **What is its top end?** Its terminal reachable version(s), limits and practical effect, not how it scores relative to another mod.
- **How is it powered?** Name the actual resource and the true block, player, item or world container; track generation, transfer, spending and failure/overflow. Never silently translate unlike pools into FE.
- **How would a quest teach it?** Concrete ordered actions, guide/JEI reference, visible result, truthful FTB detection predicate versus inventory proxy, proposed positive XP and safe thematic item reward, and an exact positive/boundary acceptance test.
- **How certain is the claim?** Immutable pinned-JAR/source evidence, current pack configuration, proven native/installed compatibility and whether any client/world tests have actually been performed.

A *staff* with a damaging spell creates an equipment record under Gear and a distinct functional record under Direct Damage. A mana→FE converter gets resource-flow records under Energy and a verified compatibility bridge. A Neo Vitae ritual may simultaneously require altar tier, EV storage, particular blocks and a spell effect: keep distinct records linked by stable capability IDs, not five duplicated unconnected descriptions.

## How to research a mod

1. **Identity and onboarding.** Confirm the current selected mod ID, exact pack pin/hash (not just the desired manifest), compatible addons, official guidebook, JEI categories and how the first player reaches a functioning setup. Recover relevant material from the existing deep Mod Comprehension profile with source links; don't assume older descriptions prove the currently pinned JAR.
2. **Actual objects.** Make inventories for all major named blocks/multiblocks, items/gear, rituals/spells/abilities, mobs and world features. Preserve registry IDs and recipe/source references. Exhaustive bulk lists may live in a linked secondary table, but each distinct gameplay system needs a fully explained entry.
3. **Capabilities.** Investigate every supported axis, create detailed records per independent action/effect and multi-tag where appropriate. Map earliest use → intermediate developments → maximal reachable output(s) with objects, exact mechanics and risks, rather than leaving one-word 'present' summaries.
4. **Resource systems.** Audit every currency/power/channel by producer, storage in **block**, **player**, **item** and other locations, capacity, persistence, transfer, consumers and progression. Neo Vitae's block EV, item Orb EV and personal Anima paths are the grounding example, **not an invitation to treat their units as interchangeable**.
5. **Progression graph.** List *all* formal tiers/stages, exact unlock actions, new block/item/spell/energy options, optional branches, and endgame. Neo Vitae's Ara Vitae 0–5 and Astral Sorcery's research/altar/attunement progression require separate detailed transition nodes.
6. **Quest handoff.** Build a complete main spine and meaningful optional branches. Each quest source entry must specify actual player steps, in-game reference, visible success, honest predicate, safe reward direction, prerequisite dependencies and positive/boundary test. Separate a **research-ready quest source** from an authored/implemented/accepted quest.
7. **Review.** A `source-backed-partial` profile is useful and honest; mark `quest-source-ready` only if every required progression tier, core resource path and significant capability is documented, with an actionable player-facing walkthrough and no unstated mandatory gaps. A source-backed profile is **never** a claim of client acceptance.

## Evidence and status semantics

Each axis coverage row is `unknown`, `present`, `no_capability_confirmed` (cite a thorough scoped search) or `not_applicable` (explain). A capability's presence is separately `native_upstream`, `present_in_pack_unverified`, `verified_in_pack`, `pack_customization_proposed`, or `unsupported_unknown`. Evidence grades remain `[pin]`, `[jar: SHA]`, `[source: SHA]`, `[docs: version]`, `[pack-test: build/world]`, `[inference]`. Never claim that a source path or configured resource has been gameplay-tested just because a NeoForge server booted.

## Ownership and change boundary

All files for this branch stay in `docs/mod-capabilities/`; no mod selection/manifest, scripts, gameplay data, quest files or existing `docs/mod-comprehension/` is changed here. Quest designs/rewards are research proposals until independently approved and implemented by the correct owner. There is **no automatic Minecraft build, integration assembly, server boot or Prism package** authorization in this workstream. Make future per-mod research work in isolated reviewable PRs; coordinate with the ongoing Work session rather than duplicate its original profile edits.
