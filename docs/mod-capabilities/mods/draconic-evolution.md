# Draconic Evolution 3.1.4.633 — Mod Capability Atlas

**Status: native quest-source survey, exact pinned binary, 2026-11-25.** Tracker [#410](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/410); completes the native capability scope of the preliminary [DE/Replication evaluation](endgame-infrastructure-evaluations.md), without rewriting the Replication checkpoint. Reuse the earlier [comprehension profile](../../mod-comprehension/mods/draconic-evolution.md) for version-aligned source routes; corrections here supersede it. Native mechanics, TMM source overlays, and proposed instruction are separate. No game execution, balance or config edits.

## 1. Provenance and census

Drive-provided Draconic-Evolution-1.21.1-3.1.4.633.jar, 6,046,229 bytes, SHA1 42ccf634a2a167db9cda533507909dd0524f2798 matches [Packwiz](../../../mods/draconic-evolution.pw.toml), CF223565/8797042. ZIP CRC clean; 2143 entries, 682 class files; 179 recipes (119 shaped,18 shapeless,3 smelting,39 fusion), 51 loot tables, 14 damage types, 10 worldgen records, eight energy-core multiblocks, one enchantment, six Curios data records, five NeoForge data records. Exact release metadata requires Minecraft1.21.1, NeoForge>=21.1.72, Brandon's Core3.2.1+, CCL4.6.1–<5, optional CC:Tweaked>=1.113.1. Drive Brandon's Core 3.2.1.309 SHA1 ef00bdaaca4e469cc3925d573434d2f84b9d8a14 matches live pin; CRC clean, 546 entries/314 classes. JAR registry and data inspected with zipfile and javap -p -c (DEModules, ModuleTypes, EquipCfg, DEConfig, IModularTieredItem, IModularMiningTool, IModularMelee, generator, energy core/OPStorageOP, crystal handlers, grinder/spawner, reactor core and related data). Upstream [immutable source 6251e32](https://github.com/Draconic-Inc/Draconic-Evolution/tree/6251e32bc93eb4bfcd910236ec1eaa6546516229) used for complementary earlier research, not claimed bit-identical. No Project Intelligence pin found in live index; native Information Tablet requires it and cannot be the only guide. JEI, module UI, Build Guide and FTB instruction required. No native packaged advancement tree found.

## 2. Acquisition and native progression graph

Native overworld/nether/end draconium ores, End comet worldgen and Dragon Heart/dragon dust are separate sources from TMM gates in section 8. Smelt ore or dust to ingot, then compress blocks. Draconium Core = 4 draconium ingots +4 gold +diamond; Wyvern Core =4 draconium ingots +4 Draconium Cores +Nether Star. Fusion Crafting Core =4 lapis blocks +4 diamonds +Draconium Core. Basic Crafting Injector =2 diamonds +Draconium Core +5 stone-tag items +iron block. Orient powered injectors toward core, each recipe ingredient on its own injector, catalyst in core; JEI fusion transfer, start when valid and powered, collect output. Default range16/minimum2, charge/craft timer each [300,220,140,60] ticks according to tier; adequate OP must be supplied, actual time not simply guaranteed timer sum; lowest used injector tier governs craft time. Do not confuse Draconium (Basic) with Draconic. Recipes explicitly set techLevel and totalEnergy. Tier gates:

| Output | Catalyst and significant materials | Ingredient count | Required tier | OP |
|---|---|---:|---|---:|
| Wyvern injector | Basic injector; Wyvern Core, 2 Draconium Cores, draconium block,4 diamonds | 8 | Basic | 32000 |
| Awakened Draconium 4 blocks | 4 draconium blocks; Dragon Heart +6 Draconium Cores | 7 | Wyvern | 50000000 |
| Awakened Core | Nether Star;4 Awakened ingots +4 Wyvern Cores | 8 | Wyvern | 1000000 |
| Draconic injector (awakened_crafting_injector) | Wyvern injector; awakened block +2 Wyvern Cores +4 diamonds | 7 | Wyvern | 256000 |
| Chaotic injector | Draconic injector;4 large chaos fragments, dragon egg,4 diamonds | 9 | Draconic | 8000000 |
| Chaotic Core | large chaos fragment;4 awakened ingots,4 Awakened Cores,4 large chaos fragments | 12 | Draconic | 100000000 |
| Draconium Chest | ordinary recipe-specific catalyst; JEI for full input | varies | Basic | 2000000 |
| Advanced Dislocator | JEI | varies | Wyvern | 1000000 |
| Wyvern equipment (7: sword,pickaxe,axe,shovel,hoe,bow,chestpiece plus capacitor) | JEI | varies | Wyvern | 8000000 each |
| Draconic equipment and capacitor | JEI | varies | Draconic | 32000000 each |
| Chaotic equipment and capacitor | JEI | varies | Chaotic | 128000000 each |
| Draconic Staff of Power | combines tools, JEI | varies | Draconic | 256000000 |
| Chaotic Staff of Power (two recipe alternatives) | JEI | varies | Chaotic | 1024000000 |
| Reactor Core | Chaos Shard;4 awakened ingots,3 draconium ingots,2 large chaos fragments | 9 | Chaotic | 64000000 |
| Reactor Stabilizer / Injector | native reactor part recipes, JEI | varies | Chaotic | 16000000 each |

Critical correction: exact packaged compress/decompress recipes use **9:1** at EACH of small→medium→large→shard, not 3:1 from old source profile. One shard=9 large=81 medium=729 small. Not interchangeable with reactor fuel-unit values. Awakened recipe consumes heart, so native ender/guardian heart ownership matters. Native item recipes for all module variants checked by path census; ordinary JEI crafting inputs not duplicated. Redstone control, bulk insertion and available injector placements affect automation. Fusion is not generic unlimited item replication.

## 3. Equipment and 71 actual module definitions

DEModules declares 71 native registered module variants; 74 item module recipe records include 3 shield uncraft recipes (module_core separate). No credit for API-only types such as cloaking/vacuum/energy share without registered module item. Module host tech compatibility, rectangular occupancy and max-install/type restrictions apply; modules are removable/configurable via native equipment GUI, not consumable permanent player traits. Defaults read from EquipCfg and DE modules constructors. Do not add all theoretical maxima together, or count shared power as Monk compensation; only inaccessible equipment power per #721.

| Host | Wyvern | Draconic | Chaotic |
|---|---|---|---|
| Tools, sword, bow | 4x4 | 6x5 | 8x6 |
| Staff | none | 8x6 | 10x8 |
| Chestpiece | 6x5 | 8x6 | 10x8 |

Base harvest speed by D/W/Dr/C=10/15/25/50; base attack factor1.125/1.25/1.75/2.5; attack frequency factor1.125/1.25/1.5/2; enchantability12/15/25/35. Equipment damage multiplier staff9/sword7/axe9/pick5/shovel5.5/hoe1; swing multiplier .5/1.6/1/1.2/1/4; staff mining efficiency multiplier3. Native base item energy all tiers0; actual charged capacity from modules, configured type multipliers on base capacity staff3/tool1/capacitor8/chestpiece2 do not create nonzero energy absent modules. IModularTieredItem: cached attack attribute = tier damage factor * tool multiplier −1 + sum of DamageData if stored OP >= energyAttack * module bonus, else omit module bonus; add vanilla player base1 for ordinary unmodified total, and swing attribute = tier speed * swing multiplier * (1+combined SpeedData) −4 as additive modifier against vanilla base4. Raw attributes are not effective damage or DPS; actual charge, vanilla clamps, armor and mod hooks matter. Speed modules have type max install8, AoE type cap1, jump type cap3 (validate host rules when mixing tiers). Damage is 1x1, no explicit per-module cap in its registration, but actual useful optimization requires storage and grid space. Do not treat 80 cells as 80 charged attack modules or all stats as simultaneously attainable.

| Module family (count) | Tiers and constructor magnitudes | Footprint / use |
|---|---|---|
| Energy (4) | D/W/Dr/C:1/4/16/64M OP,16/64/256/1024K OP/t | 1x1; charge host, capacity+transfer |
| Energy Link (3) | W/Dr/C: activation4/16/64M OP; operation512/2048/8192; transfer2048/16000/128000; dimensional false/true/true | 4x4, one; bind to energy source, actual remote link conditions, not free OP |
| Speed (4) | .1/.25/.5/1.5 | 1x1, type cap8, eligible melee/mining/ranged/armor contexts; slider can reduce speed |
| Damage (4) | +2/+4/+8/+16 HP | 1x1; charged attacks |
| AoE (4) | radius +1/+2/+3/+5 | base type3x3, actual variants may override dimensions; mining/attack/hoe; selected radius not universal cubical break |
| Mining Stability (1) | W, binary | maintain mining stability in adverse situations, not new ore tier |
| Tree Harvest (3) | W/Dr/C:16/48/144 and5/15/45 data parameters | 2x2, native tree search/harvest, not free infinite logs |
| Junk Filter (1) | W binary | 2x2, filter unwanted drops |
| Ender Collection (2) | W/Dr binary | 1x1 W,2x2 Dr; collection/remote inventory link;32 OP per single item transferred to storage |
| Projectile Velocity (3) | W/Dr/C:.15/.35/.75 primary raw values; additional accuracy tradeoff field1/.5/0 | 1x1; per-variant max install8 |
| Projectile Accuracy (3) | W/Dr/C primary .125/.2/.25 | 2x1, actual cross-parameter tradeoffs retained |
| Projectile Gravity Compensation (3) | W/Dr/C primary .2/.25/.5 | 2x1, data field-specific, not all arrows universally gravity-free |
| Projectile Penetration (3) | W/Dr/C primary .25/.5/.75 | 2x2, native penetration logic not unconditional armor bypass |
| Projectile Damage (3) | W/Dr/C primary .25/.5/.75 | 1x1, native shot damage and energy rules |
| Auto Fire (1) | W | 2x1, repeat shot, not free arrows/energy |
| Projectile Anti-Immune (1) | Dr | 2x2, eligible arrow immune target exceptions |
| Shield Controller (3) | W/Dr/C cooldown parameter20/10/5 | 2x2, one; enables shield, capacity/recovery separate |
| Shield Capacity (3) | W/Dr/C +25/+50/+100 points, recovery .1/.25/.5 | 1x1 |
| Large Shield Capacity (3) | W/Dr/C +125/+250/+500, zero recovery | 2x2; efficiency/space tradeoff; uncraft recipes |
| Shield Recovery (3) | W/Dr/C +5/+10/+20 points, recovery1/2.5/5 | 1x1; configurable regeneration |
| Flight (3) | W glide only, Dr/C glide+creative; boost1/2/3.5 | W 2x2, Dr/C base3x3, one; energy cost, TMM flight policy separate |
| Undying (3) | full table below | 2x2; death prevention, charged |
| Auto Feed (3) | D/W/Dr capacities40/150/400 | 2x1, one; consumes eligible actual food inventory, not generates food |
| Night Vision (1) | W | 2x1, one;20 OP/t |
| Hill Step (1) | W | 2x1, one |
| Jump (4) | .25/.75/1.25/4 | 1x1; type cap3; raw jump boost modifier, not jump-height ratio |
| Aqua Adept (1) | W | 1x1; underwater utility |

Counts total71. Projectile data factory takes velocity, accuracy, anti-grav, penetration, damage; above primary values not a full five-field matrix or optimized bow output. Use actual module tooltip and projectile handler before assigning combined bow ceilings; nonblocking precision. Native equipment power costs:256 OP per block mined,1024 OP × damage for attacks and bow per calculated damage per shot; base shield recharge8192 OP/point; passive shield draw=shieldPoints²*.0005 OP/t; elytra1024 OP/t, creative4096, boost W/Dr/C 1024/2048/8192 OP/t. Food stored separately from item OP. No unlimited charge or innate power. Shield controller receives protection boosts, absorbs damage while charged with source-specific exceptions, and needs capacity+recovery+energy; no unconditional immunity.

| Undying | Restore HP | Shield boost | Duration | Recharge | Nominal energy | Invuln ticks |
|---|---:|---:|---:|---:|---:|---:|
| Wyvern | 6 | 25 | 15s | 120s | 5M | 2 |
| Draconic | 12 | 50 | 30s | 60s | 10M | 3 |
| Chaotic | 20 | 100 | 120s | 45s | 20M | 3 |

Native handler recharges via integer energy per equipped server tick; cancels eligible LivingDeathEvent at highest priority when charged, consumes charge, adds health and optional shield, Draconic+ clears nonbeneficial effects and fire. DE KILL and canceled/client cases excluded. Per-variant max Dr2/C3; do not assume 6 simultaneous rescues or invulnerability vs all damage. Actual death prevention, not post-death grave or anchor. Curios/native eligible host and slot restrictions, incompatible TMM Path gear are separate. 

## 4. OP generation, transfer and storage

**Typed resource ledger:** OP is native electrical energy, FE capability bridge (not EV, mana, EMC or MXP). Blocks store/generate/transfer; equipment item storage via modules; players are carriers not free intrinsic energy tanks. Consumable furnace fuel → Generator → OP → crystals/pylon/infuser/other compatible loads. Awakened Draconium reactor fuel → OP saturation and converted Chaos material, plus containment field energy. Dislocator pearls, mob souls, Dragon Hearts, Chaos Shards and nutrition are separate operating/gate resources, not exchangeable OP. 

Generator actual Mode enum / tick: Eco+5 OP/t and50 OP per furnace fuel unit; Eco20/15; Normal40/10; Performance80/8; Performance+300/5; output throttles with full buffer, fuel value is native furnace burn time. Internal100,000 OP buffer, extract32,000/t, four inventory slots. Coal with vanilla1600 burn ticks is nominal 80k/24k/16k/12.8k/8k OP respectively, before fuel substitutions/rounding. Energy Transfuser holds chargeable equipment and routes energy using configured item I/O, unlike old Energy Infuser alone. Flux Gate configurable redstone low/high OP flow; Fluid Gate regulates fluid flow, not OP; potentiometer outputs signal, weather/rain sensor and particle generator separate. CC peripheral controls/readouts supported by optional dependency, no prewritten automation required or claimed.

Crystal network bind source I/O crystal, relay and recipient I/O or wireless crystal with Crystal Binder; set direction/input/output at appropriate device face, link within smaller endpoint range, supply actual energy; relay distributes not creates energy, wireless links explicit machine sides. Linked devices may sleep/timeout; loaded and supported capabilities matter. Exact defaults Basic/Wyvern/Draconic:
| Property | Basic | Wyvern | Draconic |
|---|---:|---:|---:|
| Crystal buffer OP | 4M | 16M | 64M |
| Max crystal link range blocks | 32 | 64 | 127 |
| Relay links | 8 | 16 | 32 |
| Direct I/O links | 2 | 3 | 4 |
| Wireless crystal links | 4 | 8 | 16 |
| Wireless devices | 16 | 32 | 64 |
| Wireless per-operation transfer limit OP | 32000 | 128000 | 512000 |

No assertion that receiver count multiplied by max operation rate equals guaranteed sustained throughput. Direct/relay balance and receiver request scheduling limit real network. Crystal energy not item transport.

Energy Core: one core block at center, required exact material layouts in all eight multiblock JSONs, four stabilizers on same plane around core (advanced stabilizer structure tier5+), build guide selects tier and can construct using actual inventory; validate then activate; place Energy Pylon with glass face and select input/output, link to active core; no need to infer pylon throughput from capacity. Native default capacity and exact count of structural materials excluding core itself, stabilizers, pylons/glass:

| Tier | Bounding cube | Draconium blocks | Redstone blocks | Awakened blocks | OP capacity |
|---|---:|---:|---:|---:|---:|
| 1 | 1 | 0 | 0 | 0 | 45500000 |
| 2 | 3 | 6 | 0 | 0 | 273000000 |
| 3 | 3 | 26 | 0 | 0 | 1640000000 |
| 4 | 5 | 54 | 26 | 0 | 9880000000 |
| 5 | 7 | 90 | 80 | 0 | 59300000000 |
| 6 | 9 | 150 | 178 | 0 | 356000000000 |
| 7 | 11 | 210 | 328 | 0 | 2140000000000 |
| 8 | 13 | 786 | 0 | 378 | -1 sentinel, extended storage |

Compiled OPStorageOP uses long valueStorage + BigInteger overflowCount, getStored=overflowCount*Long.MAX_VALUE+valueStorage; getMaxOPStored returns 9223372036854775807 when sentinel -1, but internal accounting extends beyond this. Config documents BigInteger.MAX_VALUE*Long.MAX_VALUE; not mathematically infinite, no fixed exact BigInteger practical bound claimed. External long/int APIs saturate; tier8 is genuine beyond-long storage, not simply 9.22e18 cap. This is energy storage, not item storage.

## 5. Industry, agriculture, mobility, souls and other utilities

Draconium Chest is genuine 260-slot multi-item inventory plus five smelting input slots, internal OP smelting, input feed from storage and configurable sided inventory automation. SmeltingLogic processes valid furnace recipes, not universal ore doubling. Exact individual speed/energy slider settings reference precision, teach GUI. Ordinary charged tools use legitimate mining tier/energy, AoE and tree harvest, Junk Filter and Ender Collection, no built-in universal ore generation. Modular hoe selects AoE; places inventory dirt into supported gaps after valid placement event then tills; no native full auto-seed/harvest network inferred. Explicitly material-consuming terrain prep not free creation. 

Mob Grinder: power buffer1M, default killRate5 tick cooldown parameter, DEConfig80 OP/entity health point, area/orientation and redstone control; choose legal mobs in front, collect drops/XP with separate collection, player grinding disabled by default, blacklist, no automatic soul duplication. Reaper enchantment and Mob Soul item: native default soul roll 1/1000 ordinary,1/800 passive before level adjustment, boss souls disabled default; use Reaper on eligible kill, obtain soul, convert compatible spawner to Stabilized Spawner, insert soul and upgrade tier with cores; default spawn delay basic200–800, Wyvern100–400, Draconic50–200, Chaotic25–100 ticks. Native eligibility and spawn caps apply; no claim of exact mobs per second, TMM capture policy separate. This is actual repeatable mob generation, not tamed companion system.

Dislocators: basic bound location with consumable durability/uses, Advanced Dislocator uses pearls as fuel, multiple named points/player links, default fuel cap1024, Blink 4 uses per pearl, range32/cooldown5 ticks (version-aligned prior profile). Link to pedestal/receptacle, select paired destinations, portal frames with infused obsidian and receptacle, configurable max area65536/max search distance256; not unlimited chunk loading. Player-link consent, cross-dimensional travel and protections depend on actual handler/pack policy. Entity Detector/basic and advanced sensor modes, weather sensor, potentiometer and particle generator support redstone and decoration; no invented general combat AI or arbitrary block construction. 

## 6. Chaos Guardian and native endgame

Exact pin's earlier source survey identifies Chaos Island grid spacing10000 blocks, centerY80, rough radius160, native terrain replacement within768 + transition128; void config alters shape, not encounter gate. Native End comet is distinct and suppressed by TMM. Fight manager sets 14 crystal positions about radius90 and height40; guardian spawns +80Y. Default crystal shield512, instability200 ticks; guardian shield16000, HP1000; destabilize/destroy crystal network then attack guardian shield/body. Native guardian death marks core crystal defeated and spawns invulnerable no-gravity Dragon Heart at podium; crystal only yields shards after defeat, default five on player break. No native automatic personal boss advancement assumed; quest credit should separate kill and actual shard acquisition, ownership in multiplayer explicit. Source from prior immutable profile, exact release config matches; fight phase detail and terrain interaction are downstream reference/pack-specific questions. Full native chaos progression requires enough shards, dragon egg and high energy for chaotic injectors/cores; no creative bypass as player route. 9:1 fragments as section2. TMM ProjectE gate is separately owned; do not claim a boss kill alone automatically unlocks EMC.

## 7. Reactor: native operation and exact compiled equations

Build core, four stabilizers in common plane with faces toward core, one energy injector on remaining perpendicular axis; compiled findComponents checks cardinal distances4–7 inclusive and facing, validates common stabilizer axis. Keep access/clearance and containment, separate input to Injector from extraction at Stabilizer with Flux Gates; use native UI and verified controller readouts. Minimum total fuel144 units. Fuel items: Awakened block1296, ingot144, nugget16; converted fuel large chaos fragment1296, medium144, small16. These are native menu conversions from prior version-aligned source; not fragment crafting ratios. State COLD→WARMING_UP→RUNNING→STOPPING→COOLING→COLD, BEYOND_HOPE critical terminal route. Charge allowed COLD/COOLING at >=144 units; activation WARMING_UP temp>=2000, saturation>=50%, shield>=50%; STOPPING restart exception still requires temp>=2000. Initial shield capacity=total fuel*96.45061728395062*100; saturation cap=long(total fuel*96.45061728395062*1000). Thus one awakened block nominal 12.5M shield,125M saturation. No fixed universal output: define S=saturation/max, F=converted/(converted+reactable), c=1.3F−.3, T=temperature, m=output multiplier, u=fuel multiplier. Compiled updateOnlineState:
- base B=int(maxSaturation/1000*m*15), adjusted A=int(B*(1+2c)), generationRate=(1−S)*A OP/t (generation into saturation, not net extracted power).
- temperature resistance factor q=min(99,T/10000*50), deficit p=99*(1−S); normal temperature delta per tick=10*(p³/(100−p)+444.7−q⁴/(100−q)*(1−c)+1000c)/10000. Stopping special cooling branch below.
- tempDrainFactor D=0 if T<=1000, (T−1000)/1000 to 2000, 1 above 2000 through 8000, above8000 1+(T−8000)²*2.5e−6.
- fieldDrain=int(min(2147483647,D*max(.01,1−S)*B/10.923556)); displayed fieldInputRate=fieldDrain/(1−shield/maxShield); actual incoming energy coupling dependent on injectEnergy and state. Field consumes energy continuously; input display alone not guaranteed stable setpoint.
- fuelUseRate=D*(1−S)*.005*u units/t, transfers reactable to converted when reactable positive; rates and safety are not independent. 
- STOPPING with c<1, temperature<=2001 moves to COOLING and clears startup initialized. Otherwise when saturation>=99% and fuel remains, reduces temp by 1−c each tick; else follows temperature curve. Do not disconnect shield on STOP; maintain energy supply through cooldown. At shield depleted and T>2000 critical failure route. Default config controls large explosion and output/fuel multipliers; no destructive test or exact safe automated setpoint inferred. No reactor output guarantee from this snapshot. GUI shows temperature, shield, saturation, conversion, generation, field drain/input, fuel use; CC optional for monitoring/control. Teach fail-safe and safe shutdown as separate step; not a universal guarantee. This is in-game virtual engineering, not a real reactor.

## 8. Pack-specific source reconciliation

Live Packwiz index includes TMM replacements for overworld/nether/end draconium ore and end_comet biome modifiers. All four exact files inspected: overworld/nether/end draconium ore and end_comet each set type neoforge:none. [Eldritch miner recipe](../../../kubejs/data/tmm/recipe/miner/draconium_ore_eldritch.json) produces 1 deepslate draconium ore weight90; orechid and altar/forge dust paths also indexed, effective acquisition recipes and eligible seed policy are pack-owned. Do not tell players to hunt overworld ore as normal TMM start. [Chaos EMC conversion file](../../../kubejs/data/tmm/pe_custom_conversions/chaos_shards.json) includes signed64 shard value3,058,067,520 EMC, large339,785,280, medium37,753,920, small4,194,880; not an unlock or free acquisition. Native 9:1 agrees. Existing TMM owner progression includes Dragon Heart/Chaos Guardian gate, remove Ender Dragon drops, native flight/capture restrictions, ProjectE/Replication restrictions; actual effective runtime dispatch and loaded config not claimed. Information Tablet unavailable without PI in indexed roster. [Existing FTB chapter](../../../config/ftbquests/quests/chapters/draconic_evolution.snbt) indexed; no edits. Current chapter readback confirms exactly one optional manual checkmark, XP2 + paper reward; preserve its existing chapter and quest IDs when expanding. CC:Tweaked reactor control approved #124; native optional mod support. No need to change reactor balance or select a controller strategy in Atlas. Monk #721 rule: evaluate only restricted equipment power; no need to offset shared energy infrastructure or dislocators universally available. 

## 9. 29-axis navigation and final dispositions

| Axis | State | Concrete mechanism/terminal extent |
|---|---|---|
| gear | core | Wyvern/Draconic/Chaotic powered tools, bows, chestpiece, staff, capacitor, configurable module grids |
| movement | core | glide/creative flight module, speed/jump/step/aqua, dislocators/portals |
| ore_processing | present | smelting dust/ore; powered chest furnace; no general ore doubling |
| storage | core | 260 item slots+5 smelting inputs; eight OP cores beyond long; item charge modules |
| crafting | core | four fusion tiers, 39 fusion recipes, distinct catalysts/OP |
| logistics | present | crystal I/O/relay/wireless, local collection, remote storage module; no general item network |
| buffs | core | rechargeable shield, Auto Feed, Night Vision, powered movement and combat modules |
| external_compatibility | present | Brandon's Core/CCL, FE, Curios, JEI, CC peripherals; PI absent |
| mob_farming | core | powered grinder and soul-configured four-tier stabilized spawner |
| ore_generation | absent as native machine | naturally generated ores/comets only; TMM Eldritch Miner is external |
| permanent_power | limited | equipment ownership/upgrades, not innate player tree |
| general_generation | core | OP generator/reactor, repeatable eligible mobs, no arbitrary material replication |
| direct_damage | core | charged melee, AoE and modular bows, grinder |
| block_breaking | core | speed/AoE, tree harvesting, staff mining |
| permanent_pets | absent | souls/spawner not companion ownership |
| temporary_pets | absent | no native player summon established |
| defense | core | shield control/capacity/recovery; damage prevention |
| healing_recovery | present | Undying heal, food sustain, recharge protection |
| debuff_control | present | Draconic+ Undying cleanse/fire removal, anti-immune projectile |
| energy | core | fuel→OP; networks, extended core and item storage; typed reactor resources |
| automation_control | core | Flux/Fluid Gate, redstone sensors, computer peripherals, inventory automation |
| agriculture | present | AoE hoe, dirt fill, tree harvest, mob farming; no autonomous crop growth |
| detection_information | present | entity detector, reactor/core/energy monitors, JEI, bound link UI |
| equipment_augmentation | core | 71 native module variants, tier/shape/cap restrictions |
| world_manipulation | limited | area excavation, supported dirt fill and portal/core assembly; no general builder |
| world_content | core | native ores/End comets, Chaos Island, crystal boss network |
| death_respawn | core | rechargeable actual death cancellation; no grave/post-death anchor |
| trade_economy | absent native shop | no independent trader economy established; EMC conversion external |
| progression_tiers | core | material cores, 4 injectors, 3 powered gear tiers, 8 storage tiers, Guardian/Chaotic/reaction |

## 10. Proposed quest-source map (not FTB implementation)

Use actual items/operation/state and player-specific receipt only where verified; item possession not proof of function. No duplicate rewards for reversible recipes, no mandatory catastrophic failure, no free creative items or bypass of pack gate. Exact IDs prefix draconicevolution:. Group alternate machines and optional branches, preserve existing quest IDs and TMM chapter ownership downstream.

| ID | Prerequisite / player action | Evidence |
|---|---|---|
| DE01 | TMM allowed Draconium acquisition, Eldritch Miner/approved alternative | actual ore/dust; not native ore worldgen assumption |
| DE02 | Smelt dust/ore and compress ingot/block | item |
| DE03 | Craft Draconium Core | item |
| DE04 | Nether Star→Wyvern Core | item |
| DE05 | Basic Generator, burn fuel, compare eco/output mode | OP produced |
| DE06 | Craft Fusion Core and Basic Injectors; place facing core | actual valid setup |
| DE07 | Supply power, transfer recipe via JEI; first 32k Wyvern Injector | result |
| DE08 | Upgrade adequate number of injectors (not one only), explain weakest tier | valid craft |
| DE09 | Craft Draconium Chest at 2M | item |
| DE10 | Insert varied items, configure furnace feed/sides | actual smelt output |
| DE11 | Craft Energy Core and four stabilizers | items |
| DE12 | Activate T1, pylon glass+input/output | actual energy storage |
| DE13 | Upgrade T2 and T3, use build guide | active tier |
| DE14 | T4 redstone shell | active tier |
| DE15 | Advanced stabilizers and T5 | active tier |
| DE16 | T6/T7 later expansion | active tier |
| DE17 | Basic direct/relay crystals, bind and observe flow | link and OP |
| DE18 | Wireless I/O link to receiving device side | actual transfer |
| DE19 | Upgrade crystals W/Dr, compare link caps | item/link |
| DE20 | Flux Gate low/high flow, potentiometer | controlled transfer |
| DE21 | Fluid Gate/weather/entity detector optional utility | observable signal/transfer |
| DE22 | Energy Transfuser, charge capacitor or tool | charged item |
| DE23 | Wyvern energy core and first Wyvern item (8M) | item |
| DE24 | Open module grid, install Energy module | capacity, charge |
| DE25 | Module Core and Speed/Damage | valid installed module |
| DE26 | Configure mining speed/AoE, legitimate block break | operation |
| DE27 | Tree Harvest and Mining Stability | operation |
| DE28 | Junk Filter, Ender Collection, filter/link/drop behavior | operation |
| DE29 | Wyvern bow modules velocity/accuracy/gravity | shot |
| DE30 | Damage/penetration/Auto Fire modules | shot |
| DE31 | Chestpiece Shield Controller plus capacity and recovery | charged shield |
| DE32 | Food buffer, Night Vision and Hill Step/Aqua | operation |
| DE33 | Jump and movement slider, native gliding module within Path policy | operation |
| DE34 | Wyvern Undying recharge, explain eligible lethal interception without required death | charged state |
| DE35 | Reaper enchanting, find eligible Mob Soul | item |
| DE36 | Stabilized Spawner bind soul, upgrade core and delay | actual spawn |
| DE37 | Powered Grinder, collect mob drops | operation |
| DE38 | Basic/Advanced Dislocator fuel and bound destination | successful teleport |
| DE39 | Pedestal and paired portal receptacle/infused frame | valid linked route |
| DE40 | Obtain TMM-approved Dragon Heart, 4 blocks awakened fusion | result |
| DE41 | Awakened Core→Draconic Injector | result |
| DE42 | Draconic gear / Staff of Power (256M), compare grid | item and charged use |
| DE43 | Energy Link, projectile anti-immune and Draconic flight policy | legitimate use, no gate bypass |
| DE44 | Draconic Undying and shielding, explain limits | charged state |
| DE45 | End expedition to Chaos Island, locate guardian/crystals | encounter |
| DE46 | Crystal instability and guardian defeat | actual boss credit |
| DE47 | Heart collection and separate defeated Chaos Crystal break | shard |
| DE48 | Compress/decompress 9:1, preserve shard gate | item |
| DE49 | Chaotic Core (100M) and injector (8M, egg/fragment gate) | result |
| DE50 | Chaotic tools, staff 1.024B, chestpiece/energy modules | item |
| DE51 | Chaotic Undying and shield capacity vs recovery loadout | charged state |
| DE52 | Tier8 core 786+378 blocks, advanced stabilization, active storage | active tier |
| DE53 | Reactor parts (core,4 stabilizers,injector), valid structure | validation |
| DE54 | Separate gated input/output, add fuel, charge and warm | UI state |
| DE55 | Activate only at thresholds, observe temperature/saturation/field drain | RUNNING |
| DE56 | Safe net power monitoring and optional CC controls, not fixed output | measured UI |
| DE57 | Controlled shutdown retaining shield, cool before refuel | COLD |
| DE58 | Cross-link late ProjectE progression gate; no automatic EMC grant | existing owner gate |

Scope complete at significant native capability/quest-source level. Numerical research supports module ingredients and capacity ceilings, but not every optimized combined equipment loadout, exact projectile secondary modifiers, minor trade/loot odds, every UI packet/automation edge, or effective TMM loaded recipe and reactor settings; those are explicitly separate. No new designs, cap changes, runtime edits, tests/builds, or claim of installed-world validation. Prior preliminary score 46/55 remains justified; see separate final evaluation.
