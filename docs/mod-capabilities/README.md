# Mod Capability Atlas — parallel draft v0.1

**Status:** Owner-review taxonomy proposal, 2026-11-20. Tracked by [issue #410](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/410). This is a documentation-only, independent research lens; **do not replace or modify** the existing [Mod Comprehension project](../mod-comprehension/WORK-CHAT-INSTRUCTIONS.md), its [index](../mod-comprehension/INDEX.md), or its mod profiles. No mod pins, scripts, quests, or pack behavior are authorized by this proposal.

## Purpose

The existing project reconstructs *how a player progresses through each mod*. This atlas asks **what that mod can do**, across comparable capability axes. Later, cross-link Atlas capability records into the existing deep profiles and cross-mod interaction reports rather than maintaining two competing authoritative accounts of progression. Use the manifest ID as the profile filename and preserve exact modid/loader/version in its header.

**Canonical machine-readable axis registry:** [taxonomy.v0.json](taxonomy.v0.json). **Copyable per-mod worksheet:** [templates/MOD-CAPABILITIES.md](templates/MOD-CAPABILITIES.md). All 28 axes below are provisional until owner review.

## Proposed axes

| Group | Axis ID | Meaning | Origin |
| --- | --- | --- | --- |
| Player & combat | `gear` | Gear: Equipable/held equipment, intrinsic functionality, passive effects and equipment-provided abilities. | Owner |
| Player & combat | `movement` | Movement: Movement speed, flight, dashes, blink/teleport, mounting, water/air traversal and player travel between dimensions. | Owner |
| Resources & production | `ore_processing` | Ore processing: Crushing, refining, multiplying, washing or otherwise upgrading mined ore or raw metal inputs. | Owner |
| Infrastructure | `storage` | Storage: Accessible items, fluids, magical reagents, inventories, portability, remote access and capacity. | Owner |
| Infrastructure | `crafting` | Crafting: Recipes, rituals, infusion, potions, item synthesis, fabrication and conversion whose goal is producing crafted outputs. | Owner |
| Infrastructure | `logistics` | Logistics: Transfer, routing, sorting, filtering and remote delivery of items, fluids, entities or resources. | Owner |
| Player & combat | `buffs` | Buffs: Temporary or maintained positive effects to players, allies and pets, including potion/spell auras. | Owner |
| Cross-cutting | `external_compatibility` | External compatibility: Native or pack-specific interactions that bridge distinct mods, APIs, energy types, entities or mechanics. | Owner |
| Resources & production | `mob_farming` | Mob farming: Renewable mob acquisition, capture, duplication, spawning, automated kills, drops and experience farming. | Owner |
| Resources & production | `ore_generation` | Ore generation: Generation or acquisition of new ore/raw-ore equivalents through mining magic, miners, magical sources or repeated supply independent of an existing vanilla vein. | Owner |
| Player & combat | `permanent_power` | Permanent power: Persistent player-state abilities, attunements, transformations, learned skills and irreversible character unlocks. | Owner |
| Resources & production | `general_generation` | General generation: General-purpose creation, replication, exchange, transmutation or regeneration of useful resources from substitutes/currency/ambient systems. | Owner |
| Player & combat | `direct_damage` | Direct damage: Entity damage inflicted by spells, powers, traps, constructs or equipment-independent actions; record separately even if cast using gear. | Owner |
| Resources & production | `block_breaking` | Block breaking: Active breaking, mining, excavation, timbering, harvesting or destruction of existing blocks, including spells and machines. | Owner |
| Actors & entities | `permanent_pets` | Permanent pets: Persistent owned, summoned, created or bound helpers/familiars/constructs that survive normal expiration and can continue service. | Owner |
| Actors & entities | `temporary_pets` | Temporary pets: Expiring or one-shot controlled minions, summons, temporary allies and short-lived constructs. | Owner |
| Player & combat | `defense` | Defense: Wards, barriers, armor-independent mitigation, resistance, immunity, shielding and hazard protection. | Suggested |
| Player & combat | `healing_recovery` | Healing & recovery: HP healing, hunger/saturation restoration, mana or ability-resource recovery and cleansing of harmful effects. | Suggested |
| Player & combat | `debuff_control` | Debuffs & control: Slows, curses, crowd control, fear, roots, silence, disarming, forced motion, enemy disablement and targeting manipulation. | Suggested |
| Infrastructure | `energy` | Energy: Generation, conversion, storage, transmission and consumption of mana, FE/OP, Source, EV, blood or other power systems. | Suggested |
| Infrastructure | `automation_control` | Automation & control: Redstone, logic, sensing triggers, programmable computers, state evaluation and repeatable task sequencing. | Suggested |
| Resources & production | `agriculture` | Agriculture & ecology: Crop/tree growth, fishing, animal breeding, food farming, biomass, gardening and natural-resource regeneration. | Suggested |
| World & utility | `detection_information` | Detection & information: Locate/reveal ores, structures, mobs, threats, places, recipes, status, hidden world state and exploration guidance. | Suggested |
| Infrastructure | `equipment_augmentation` | Equipment augmentation: Enchantment, reforging, socketing, upgrade tiers, imbuement and modification of existing items or tools. | Suggested |
| World & utility | `world_manipulation` | World manipulation: Placement/building, terrain alteration, environmental changes, weather/time/light, protections and world-rule interactions. | Suggested |
| World & utility | `world_content` | World content: Added dimensions, biomes, structures, ambient ores, worldgen, encounter spawns and discoverable features. | Suggested |
| Player & combat | `death_respawn` | Death & respawn: Resurrection, death avoidance, corpse/grave/inventory preservation, respawn location and recovery after death. | Suggested |
| Resources & production | `trade_economy` | Trade & economy: Merchant trades, bounty systems, currency, shops, barter, exchange markets and economic access paths. | Suggested |

## Rules that keep the inventory useful

1. **Capability is not delivery.** Record actual functional effects even when performed by a staff, spell, ritual, machine, creature, passive, player-state trait, or world rule. A castable explosion can be both `gear` (the item) and `direct_damage` (the action), but the spell must have its **own** capability record independent of equipment. `gear`, `external_compatibility`, and `world_content` are deliberately retained as cross-cutting/content-impact axes alongside action/effect axes; multi-axis tagging is allowed and expected.
2. **Every record is typed and testable.** Identify the delivery surface, trigger, inputs/outputs and consumed resources, duration, cooldown, range, throughput/capacity, automation route, gates, multiplayer/ownership conditions, current pack overrides and positive/negative test where consequential. Use `unknown` explicitly rather than inventing numbers or item IDs.
3. **Unknown is not absent.** The axis matrix defaults to `unknown`; use `present` with version-scoped evidence, and `no_capability_confirmed` only after a documented sufficiently complete source/JAR search across that axis. Use `not_applicable` only when semantically invalid (e.g. pure library gameplay) and explain why. Presence in a wiki or addon does not prove presence in this pack.
4. **Separate actual capability from proposal.** Each capability says `native_upstream`, `present_in_pack_unverified`, `verified_in_pack`, `pack_customization_proposed`, or `unsupported_unknown`. These are claims about presence, not gameplay-test outcomes. Store evidence grade separately as `pinned_artifact`, `inspected_jar`, `immutable_source`, `versioned_docs`, `pack_test`, or `inference`. Quote exact source URLs, refs/hashes and version gaps. `pack_test` requires in-game evidence and build/world context.
5. **Keep ore channels separate.** `ore_generation` yields new ore or equivalents independent of mined existing vein blocks; `ore_processing` increases/refines already acquired ore; `block_breaking` acts on existing blocks; `general_generation` covers broad resource generation/transmutation; `world_content` includes naturally generated ore veins. A single machine may genuinely qualify for multiple axes with separately documented effects.
6. **Energy bridges are two facts.** A mana-to-FE converter belongs to `energy` for its production/conversion and `external_compatibility` for cross-mod interoperability. Identify the actual port/API, buffer/cap and direction of flow; never assume that all mana, FE, EV, Source or blood systems can connect.
7. **Persistent ≠ equipped or long duration.** `permanent_power` is a player-state property (such as attunement); `buffs` are temporary or maintained effects; `gear` requires possession/equipment; `permanent_pets` vs `temporary_pets` depends on persistence and ownership, not only combat role. Record survival across unequip, logout, death and dimension changes separately.
8. **Keep logical integration separate from data collection.** Neither this document nor a proposed interaction creates recipes or compatible behavior. Existing mod-comprehension evidence may be linked/reused, but initial profiles may be `unknown` on most axes; do not mark research completed to improve a progress percentage.

## Minimum per-mod research workflow

For each manifest-selected mod: confirm manifest identity and pin/runtime selection; check existing detailed [mod-comprehension profile](../mod-comprehension/mods/) and any existing compat reports; copy the [worksheet](templates/MOD-CAPABILITIES.md) to `mods/<manifest-id>.md`; fill the axis matrix with `present`/`unknown`/`no_capability_confirmed`/`not_applicable`; create **one row per distinct supported capability** with source; document delivery, typed I/O and gates; mark speculative cross-mod candidates explicitly; link the reusable deep profile. Do not blanket-mark absent. A library with no survival mechanics gets a concise interface record, not fake progression.

**Scope / merge policy:** all files for this workstream stay in `docs/mod-capabilities/`. The existing `docs/mod-comprehension/` is owned by its original Work project. Integrate the two by linking records and later reviewed schema mapping; don't duplicate or replace existing research and don't open competing research on the same source files. Once reviewed, expand the atlas via independent per-mod research PRs. Any proposed gameplay change goes to an independently approved implementation workstream.

## Open design questions for owner review

- Keep `gear`, `external_compatibility` and `world_content` as intentionally non-exclusive axes? Recommended **yes**.
- Keep `trade_economy` and `death_respawn` as top-level axes? Recommended **yes**, because they create independent gameplay routes.
- Are 28 axes appropriately granular? Recommended **yes**, using sparse capability records and a 28-row coverage matrix, rather than artificially forcing one label per mod.
