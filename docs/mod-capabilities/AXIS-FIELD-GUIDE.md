# Capability axis field guide — v0.2 (research/quest contract)

**Purpose:** These are **research instructions**, not scores, short labels or capability claims. For a given mod, document **every relevant concrete item, block, spell, ritual, entity, player state and action**, using version-scoped evidence. A blank axis is `unknown`, not absent. An axis summary is only a map to its detailed records.

**Universal minimum for every present capability:** exact name and registry identifier (if supported); physical block/item/GUI, spell or state; how player first obtains and performs it; inputs and resulting outputs (quantity/type), cost and failure behavior; **bottom end (first usable access)**, intermediate stages and **top end (last reachable capability)**; version-specific limitations; guide/JEI reference; native/pack-specific unlock; quest teaching step and truthful detectable completion; links to source and validation. Avoid imaginary tiers for feature-only mods.

**Bottom/top means functional bounds within the mod, not a rank relative to other mods.** Record ranges and branching capabilities even when there is no strictly better end state. Each capability must be linked to the [progression timeline](templates/MOD-CAPABILITIES.md#progression-ladder-and-branch-map), and where power is spent, to the [resource ledger](ENERGY-AND-PROGRESSION.md).

## `gear`

**What qualifies:** All equipment actually usable by a player (armor, curios, tools, weapons, staffs, rings, trinkets and gear that only carries spells).

**Inventory:** Name/ID of every meaningful item family and upgrade; slot, equipping/activation method, stats, passive effect, durability/charge, recipe source, repair and restrictions. Map independent active effects to their functional axes rather than hiding them inside an item description.

**Entry → full breadth:** Record the earliest useful item and its exact acquisition path, every intermediate upgrade, and the strongest available item with its costs and limitations. 'Best' means terminal capability, not a comparative power ranking.

**Quest-authoring expectation:** Teach crafting or finding the object, equipping/activating it and verifying one relevant use; an inventory task alone proves only possession.

## `movement`

**What qualifies:** Travel abilities performed by players or mounts, including acceleration, leaps, flight, gliding, grappling, swimming, traversal, dimensional travel and teleportation.

**Inventory:** Record named spell/item/ritual/block/creature, exact activation, target and range, consumables/charges, cooldown, destination restrictions, dimension restrictions, safe-failure and multiplayer rules.

**Entry → full breadth:** Earliest available travel tool through largest range/freedom or highest unlocked traversal mode; record separate branches rather than asserting a single ladder.

**Quest-authoring expectation:** Quest should demonstrate the movement itself, with a safe location and observable arrival; where task detection is missing, call out the proxy.

## `ore_processing`

**What qualifies:** Increasing, refining, or converting the value/form of already acquired ore, raw metal, dust or ingot.

**Inventory:** List each machine/block, ritual, spell and required intermediate item; exact eligible inputs, ratio/yield, byproducts, power, heat, catalysts, automation input/output and loss risk. Link item tags and ore unification behavior.

**Entry → full breadth:** Entry from first usable single-step conversion through advanced multi-step chains and maximum verified yield; separate recipe-specific yields and configuration-dependent rates.

**Quest-authoring expectation:** Teach one full input → operation → output cycle and the next upgrade; never treat obtaining the processor as proof of processing.

## `storage`

**What qualifies:** Holding and retrieving ordinary items, fluids or inventories, including magical/remote access, shared or personal stores.

**Inventory:** List all container blocks, bags, remote terminals, keys, access devices, capacities, stacking/filters, owner/team rules, menus, pickup/safety, persistence and nested-container restrictions. Energy reservoirs are also specifically documented under energy.

**Entry → full breadth:** From first container or remote-access method through biggest usable store, cross-dimension access, advanced filtering and multiplayer limits, keeping optional routes separate.

**Quest-authoring expectation:** Teach first successful deposit and retrieval, then advanced access and limits; possession of a bag is not proof that remote storage works.

## `crafting`

**What qualifies:** Producing specific items/effects through tables, altars, spells, infusions, cauldrons, rituals and special recipe systems.

**Inventory:** Every major workstation or recipe family with block/item ID, multiblock shape, operation order, exact typed ingredients/energy, byproducts, catalysts, failure, recipe viewer and guidebook path. Large recipe lists may be linked as evidenced inventories; do not replace the important flow with 'crafting exists'.

**Entry → full breadth:** First functioning crafted output, intervening recipe gates, highest-tier station, rarest mandatory source and terminal crafting capabilities.

**Quest-authoring expectation:** Quest must tell the player exactly how to assemble and run a representative recipe; a craft or possession criterion may not prove an altar operated.

## `logistics`

**What qualifies:** Moving, routing and filtering items, fluids, entities and other transferable resources between locations.

**Inventory:** Name actual pipe/block/item, endpoint API, transfer direction, speed, range, filters, redstone control, loading/unloading rules, chunk boundaries, dimensional restrictions, loss/duplication conditions and accepted foreign inventories.

**Entry → full breadth:** Earliest manual or one-hop logistics, intermediate filtered transport, maximum network topology/range and remote transfer, with practical throughput rather than a score.

**Quest-authoring expectation:** Teach connection, a controlled transfer and an intentional reject/filter; pair with native energy transport records where applicable.

## `buffs`

**What qualifies:** Temporary or sustained beneficial effects granted through spells, elixirs, food, rituals, blocks, pets or auras.

**Inventory:** Name source object/spell/effect ID, target, exact positive changes, intensity, duration, repeatability, stacking, costs, uptime, cancellation, visibility, range and affected entity classes.

**Entry → full breadth:** First available useful buff through extended duration/highest amplifier/strongest sustained aura; preserve alternatives by use case rather than forcing a power order.

**Quest-authoring expectation:** Teach how to grant and identify the effect, plus expiry/reapplication; item possession alone is insufficient.

## `external_compatibility`

**What qualifies:** Actual links to other mods, vanilla systems, installed addons, APIs, events or data layers.

**Inventory:** For each bridge document the exact originating mod/item/block/capability and target, native/addon/pack adaptation, typed conversion or API call, direction, version, gating, serialization, failure behavior, tests and whether both endpoints are present in TMM.

**Entry → full breadth:** From earliest accessible supported bridge through advanced cross-mod automation and upper-tier integrations; 'possible' is not 'working'.

**Quest-authoring expectation:** A cross-mod quest must operate the full bridge, not simply craft the two endpoints; evidence must cover both sides.

## `mob_farming`

**What qualifies:** Spawning, capturing, breeding or killing mobs for repeatable drops or XP, including cages/spawners/automation.

**Inventory:** Document exact mobs/tags, spawner/capture block or entity, initial mob acquisition, loot tables and special drops, spawn conditions, kill attribution, energy/fuel, output, rates, cooldowns, boss exclusions and anti-exploit settings.

**Entry → full breadth:** First valid farmable entity and setup through widest allowed farmed set and highest-output machine; gated loot and blacklist exceptions matter more than rankings.

**Quest-authoring expectation:** Teach a legitimate spawn/capture and an actual output event, plus a negative case for protected mobs.

## `ore_generation`

**What qualifies:** Obtaining new ore/raw equivalents through repeatable magical or machine sources independently of mining a pre-existing placed vein.

**Inventory:** Record mining construct/block/ritual/item, location or dimension, extractable ore list, loot/weight, source input (including vein or no-vein), rates, cost, gates and exhaustion/renewal behavior. Distinguish ore conversion and ambient worldgen.

**Entry → full breadth:** First repeatable ore source, changes in mining/generation tiers, highest-resource/rarest-ore source and its gates; explicitly note what remains unavailable.

**Quest-authoring expectation:** Teach the player to obtain one fresh ore unit through the system; never confuse worldgen presence with renewable generation.

## `permanent_power`

**What qualifies:** Persistent character states and abilities: attunements, perks, mutations, paths, permanent stat improvements and transformations.

**Inventory:** Exact unlock ritual/item/UI, resource and milestone, state owner (player/team/world), selectable choices, respec/irreversible paths, persistence across logout/death, stats/abilities, caps and stacking with other classes.

**Entry → full breadth:** Earliest unlock and first usable ability through maximum achieved attunement/perk/transform cap, documenting mandatory vs optional branches.

**Quest-authoring expectation:** Quest must explain acquiring and exercising the power; distinguish an advancement trigger from actual saved player-state mutation.

## `general_generation`

**What qualifies:** Broad creation, transmutation, exchange or replication of resources via EMC, replication, magical mining/exchange or similar open-ended systems.

**Inventory:** Name generator/machine/item and currencies, supported and excluded items, initial source and bootstrap costs, per-unit cost, cap, throughput, special catalysts, conservation, duplication defenses, blocked items and boss timing.

**Entry → full breadth:** Earliest legal conversion/generator through maximum output and highest unlocked item classes; record pack restrictions and special gates explicitly.

**Quest-authoring expectation:** Teach an authorized input–conversion–output and one exclusion; avoid rewards that give early access to gated resources.

## `direct_damage`

**What qualifies:** Damaging entities through spell effects, powers, traps, runes, devices and attack abilities independently from equipment.

**Inventory:** Name the individual spell/projectile/ritual/trap and registry identifiers, delivery object, targeting, damage type/formula, area/range, cooldown/cost, status riders, PvP attribution, entity exclusions and scaling.

**Entry → full breadth:** From first accessible damage effect through highest unlocked power/radius or complex targeting; list distinct damage mechanics separately, not as a single staff entry.

**Quest-authoring expectation:** Demonstrate the actual effect on an allowed target; item acquisition is not spell use. Include safe and excluded targets for high-impact effects.

## `block_breaking`

**What qualifies:** Destroying, mining, harvesting or excavating existing blocks by an ability, tool, ritual, controlled entity or machine.

**Inventory:** List spell/tool/construct/machine, affected tags/harvest tier, AOE shape, range, block protection, durability/resource costs, silk/fortune and tile inventory safety, claims and blacklist interactions.

**Entry → full breadth:** First mineable material and small region through highest tier/area, filters and protected-block behavior, not a single power score.

**Quest-authoring expectation:** Quest should require an actual mined block/output where detectable, with explicit boundaries for unbreakable/protected blocks.

## `permanent_pets`

**What qualifies:** Persistent companions, bound spirits, golems, minions or familiars that normally remain owned/available beyond a timer.

**Inventory:** Name summon/craft/tame component, exact mob/entity ID, owner binding, inventory/gear, orders, AI jobs, consumption, death/respawn, chunk/portal behavior, maximum counts, dismissal and safety.

**Entry → full breadth:** First summonable helper through most complex upgraded/persistent servant and advanced orders; do not merge combat, storage and farm jobs into 'has pet'.

**Quest-authoring expectation:** Teach acquiring/binding then commanding a specific action, with safe handling and owner/permission checks.

## `temporary_pets`

**What qualifies:** Summons, servants and constructs with lifetimes or one-shot behavior.

**Inventory:** Name summons, catalysts, rituals/spells, lifetime and upkeep, commands/AI, eligible targets, limits, costs, despawn, chunk loading and damage attribution.

**Entry → full breadth:** First simple timed summon through advanced army/support/specialist options and maximum controllable count where known.

**Quest-authoring expectation:** Teach spawning, observing one behavior and understanding expiration; separate a projectile from a controlled entity.

## `defense`

**What qualifies:** Damage prevention, absorption, resistances, wards, shields, reflect/redirect and hazard immunity.

**Inventory:** Name equipment-independent shields/wards/spells/blocks and linked gear where relevant; incoming damage class, mitigation formula/cap, energy payment timing, duration, stacking, environment/claim/party filters.

**Entry → full breadth:** Earliest useful barrier or resistance through top-tier prevention/immunity modes and depletion/failure conditions.

**Quest-authoring expectation:** Quest must demonstrate a safe prevented/reduced event where practical, including a bypass/depletion case for major defenses.

## `healing_recovery`

**What qualifies:** Restoring health, hunger, saturation, status conditions, stamina, mana or other personal resources.

**Inventory:** Name heal source/item/spell/altar/camp, exact target/resource, magnitude, cadence, cost, cooldown, duration, excess handling and risk; energy replenishment pathways also receive energy records.

**Entry → full breadth:** First cheap heal/cleanse through advanced continuous or group recovery, noting whether it can be sustained indefinitely.

**Quest-authoring expectation:** Teach an actual recovery, not just crafting a potion; avoid harmful self-injury prerequisites unless necessary and safe.

## `debuff_control`

**What qualifies:** Negative status, environmental denial, target control and non-damaging crowd manipulation.

**Inventory:** Name curse/spell/trap/effect, eligible targets, status formula, duration, area, dispelling, boss immunity, PvP and claims; tag direct damage separately if it also occurs.

**Entry → full breadth:** First useful crowd-control spell and later wider/longer or specialized control, with immune targets and counterplay.

**Quest-authoring expectation:** Quest observes a debuff/control event against a supported target and a known immune/blocked case where consequential.

## `energy`

**What qualifies:** Any quantified spendable power or resource used to run magic or machinery, not only FE: EV, Anima, blood, mana, Source, media, starlight/lumen, souls, FE/OP, and comparable typed pools.

**Inventory:** Enumerate **each distinct resource** and its producer, channel, units, UI, owner, capacity, storage in BLOCKS, on PLAYER state and in ITEMS separately, generation/regen, transfer/extraction, conversion (if any), costs/drain, overflow and persistence. Distinguish fluid/personal network/item charge even when named similarly; provide exact IDs and containers as verified.

**Entry → full breadth:** Earliest way to make each resource and perform a real spend, network/buffer upgrades and maximum reachable generation/storage/throughput; show which tier unlocks each pool/carrier. A mod can have all three storage scopes, as Neo Vitae does across its systems.

**Quest-authoring expectation:** Quest teaches resource production, visible accumulation, transfer to a real consumer, and result; detection of a tank or orb item is not evidence it was filled. Never call a resource FE without a proven FE interface.

## `automation_control`

**What qualifies:** Conditional, programmable, timer-driven and redstone-driven automation of magic tasks.

**Inventory:** Record controller/block/script endpoint, sensing, triggers, redstone inputs/outputs, programming/API, scheduling, recipes/modes, failure handling, multiplayer permissions and chunk-loading prerequisites.

**Entry → full breadth:** First automatic one-cycle operation through full unattended multi-step system, with actual power/throughput limits.

**Quest-authoring expectation:** Teach setting trigger, observing two cycles and verifying a stop/disable condition; transport and production effects keep their own axes.

## `agriculture`

**What qualifies:** Farming crops, trees, fish, passive animals, food, biomass and ecology.

**Inventory:** Name seeds/saplings/baits/tools, planting/harvest conditions, biomes/dimensions, growth accelerators, soil/water, methods, drop lists, food processing and automation.

**Entry → full breadth:** First sustainable harvest through specialized crops, highest-tier food/resources and renewable closed loops.

**Quest-authoring expectation:** Teach planting/raising/harvesting, with a separate safe farm loop; dangerous mob drop farms stay under mob farming.

## `detection_information`

**What qualifies:** Finding or revealing world entities, structures, ores, risks, status, recipes and navigation information.

**Inventory:** Name scanner/wand/map/overlay/book/spell, target filters, range/precision, inputs/cooldown, dimension rules, displayed UI, permission/privacy and false positives.

**Entry → full breadth:** Earliest useful discovery aid through advanced remote search/diagnostics and relevant locked target categories.

**Quest-authoring expectation:** Teach acquisition, an actual scan/reading and what to do with the result; a crafted wand alone is not ore detection.

## `equipment_augmentation`

**What qualifies:** Imbuing, enchanting, socketing, charging, reforging or upgrading existing gear.

**Inventory:** Name upgrade bench/altar/shelf/modifier item, input item class, materials, enchanting level/tier/charges, attribute/effect options, permanence, refund or failure and compatibility conflicts.

**Entry → full breadth:** First obtainable modifier through final upgrade tier, available choices, caps and exceptional combinations; note nonrefundable consumption.

**Quest-authoring expectation:** Quest teaches upgrade operation and verifies changed item state; distinguish seeing upgrade materials from performing enhancement.

## `world_manipulation`

**What qualifies:** Actively modifying blocks, terrain, weather, light, time, environment, protections or structures.

**Inventory:** Name specific wand/ritual/block/command interface, placement rules, volume/radius, block whitelist, claimed area permissions, costs, duration, rollback, environmental effects and TPS risks.

**Entry → full breadth:** First one-block or local action through largest/most complex structure/world effect; separate irreversible from reversible change.

**Quest-authoring expectation:** Teach safe placement or environment modification with measurable observation, including restrictions around protected terrain.

## `world_content`

**What qualifies:** Passive game content the mod adds: biomes, ore veins, structures, dungeons, dimensions, bosses and naturally spawning mobs.

**Inventory:** Name exact dimension IDs, biomes, structures, blocks, worldgen features, mob spawn rules, loot/encounters, gate conditions, recovery route, disabled/configured exclusions and pack terrain compat.

**Entry → full breadth:** Earliest reachable discovery through last gated encounter/dimension and terminal loot, but do not pretend passive generation itself is a player skill.

**Quest-authoring expectation:** Quest teaches how to reach and recognize content, key route to leave, and a meaningful encounter/loot action; location tasks need real availability.

## `death_respawn`

**What qualifies:** Preventing death, resurrection, postdeath retrieval, graves, soul/corpse systems and spawn-binding.

**Inventory:** Name totem/item/ability/grave/block, exact trigger and inventory/XP retention, cost, ownership, death causes, cross-dimension persistence, multiplayer rules and exploits.

**Entry → full breadth:** Early grave protection or recovery through strongest available survival/revival route and consumption/failure edge cases.

**Quest-authoring expectation:** Quest must describe safe setup and recovery without requiring a lethal test unless authorized; inventory possession alone does not verify rescue.

## `trade_economy`

**What qualifies:** NPC/shop/merchant/barter/bounty access to items, resources or services.

**Inventory:** List actual traders/boards/currency/items, recipes or trade pools, refresh, price, stock, unlock/reputation, EMC restrictions, dimension availability and pack overrides.

**Entry → full breadth:** First reachable trade and currency source through advanced/unlocked offers, repeatability and boss/gated item exclusions.

**Quest-authoring expectation:** Teach finding/creating the trade route and conducting the transaction; do not grant unintended gate-bypassing currency or items.

## `progression_tiers`

**What qualifies:** Formal and informal advancement stages, alternate specializations, research unlocks, altar tiers, rank ladders and access gates throughout the mod.

**Inventory:** List exact named tier/research stage/rank **with authoritative order**, in-game UI or guide section, acquisition/build, material/energy gate, effects unlocked, output proof and next step. Mark native vs pack-imposed gates, mandatory vs optional, per-player vs per-block vs world, branch-specific and reversibility. Include early and terminal capabilities per *other axis*.

**Entry → full breadth:** Describe the full entry → middle unlocks → maximum reachable stage, with every transition, side branch and any deliberately inaccessible gate. Example references: Neo Vitae Ara Vitae 0–5; Astral Sorcery research/altar and attunement ladder. Do not assume the two ladders operate the same way.

**Quest-authoring expectation:** Provide a questable ordered main spine plus optional branches; each transition needs actionable construction/use, guide/JEI reference, a real operation/progression signal and an honest fallback criterion.


## Research coverage / no false completion

A single magic altar can appear under crafting, energy, progression tiers and automation, but **one stable capability ID** connects those aspects. Similarly a staff is gear while the spell it casts gets separate damage, mining or movement records. Do not multiply identical capabilities for counts; use multi-axis links. A mod profile is **quest-source-ready** only when all material systems and required tier transitions have named objects/operations, inputs/outputs, meaningful lower/upper bounds, reference pages, and an honest operation/detection contract. A subtype without exact version evidence remains an open gap, not an asserted feature.
