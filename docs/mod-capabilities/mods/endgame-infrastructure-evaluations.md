# Endgame infrastructure — Draconic Evolution and Replication

2026-11-25. Atlas #410. Minecraft 1.21.1 NeoForge. Targeted version-aligned source research plus provisional 11-domain evaluations; **not a completed exact-JAR Atlas survey**. Integration baseline a98eadfbd625260f8d2c984cd51844ee76f198af. No new runtime acceptance, effective-config or loaded-recipe proof.

## Evidence

- Draconic Evolution 3.1.4.633, active SHA1 42ccf634a2a167db9cda533507909dd0524f2798, CF223565/8797042, [release](https://www.curseforge.com/minecraft/mc-mods/draconic-evolution/files/8797042). Reopened earlier [comprehension profile](../../mod-comprehension/mods/draconic-evolution.md) and [immutable upstream 1.21 snapshot](https://github.com/Draconic-Inc/Draconic-Evolution/tree/6251e32bc93eb4bfcd910236ec1eaa6546516229). Snapshot is the latest branch commit before the release date, with release-note matching Energy Link fixes; not verified binary equivalence. Fresh source inspection: DEContent, DEModules, RecipeGenerator, UndyingEntity/Data, ModularArmorEventHandler, AutoFeedEntity, ModularHoe, TileDraconiumChest, SmeltingLogic, DEConfig, TileEnergyCore.
- Replication 1.2.7, active SHA1 bb433ec94394e960ba26fc2e7fd5537fcb7fbe50, CF638351/7662870, [release](https://www.curseforge.com/minecraft/mc-mods/replication/files/7662870). [Immutable upstream snapshot](https://github.com/Buuz135/Replication/tree/8482d3e995c6185ea760251f0140ceab0b3e8328), commit 2026-02-22 with build.gradle explicitly 1.21.1-1.2.7; no binary equivalence claim. Fresh inspection of registry/config, MatterType, Disintegrator, Identification Chamber, Replicator, Terminal, Chip Storage, MemoryChip, Motor, MatterNetwork and food anchor data. Pack [stonecutter catalog](../../research/replication-stonecutter-catalog-2026-11-23/README.md), pending #565 implementation separate. No fresh binary supplied/inspected in this pass.

## Scores

0 absent/no confirmed capability; 1 some assistance; 2 significant assistance; 3 independent; 4 superior; 5 transformative. Major 9–11 independent with personal and base breadth, Substantial 4–8, Minor 0–3. Native source-supported provisional ratings; no custom cross-mod acquisition mechanism credited as native.

| Domain | Draconic Evolution | Replication |
| --- | ---: | ---: |
| Combat | 5 | 0 |
| Protection | 5 | 0 |
| Survival | 4 | 3 |
| Gathering | 4 | 4 |
| Industry | 5 | 5 |
| Construction | 2 | 2 |
| Transportation | 5 | 0 |
| Logistics & storage | 3 | 2 |
| Life & agriculture | 4 | 2 |
| Death & recovery | 4 | 0 |
| Advancement | 5 | 3 |
| Total | 46 | 21 |
| Independent (>=3) | 10 | 4 |

## Draconic Evolution — 46/55, 10 independent, Major (provisional)

Combat5 modular weapons, AoE, attack/charge modules, boss progression. Protection5 rechargeable shield controls/capacity/recovery, armor modules. Survival4 energy-supported equipment, Auto Feed modules which consume eligible effect-free foods and store nutrition/saturation (capacities40/150/400), not free food. Gathering4 mining AoE, tree harvest, magnet/collection, mob grinder and renewable spawns, no arbitrary ore generation. Industry5 four fusion injector tiers, generators/reactor, energy transfer/storage, powered smelting and automation. Construction2 excavation and limited material-consuming terrain fill (hoe), portal/multiblock construction assistance, no general building wand confirmed. Transportation5 dislocators/blink/portals, native creative flight at Draconic/Chaotic tier subject to configured power, not a free unconditional ability. Logistics3 true multi-item 260-slot Draconium Chest with five smelting inputs, managed item-handler capability and automatic feed, plus local item attraction; energy storage/distribution independently transformative but not general item routing. No full item network inferred. Life4 modular area hoes, tree harvesting, Stabilized Spawners and Mob Grinder, not a complete automated crop planting/growth network. Death4 actual recharge-based Undying modules. Advancement5 Draconium/Wyvern/Draconic/Chaotic tiers, grid modules, fusion, eight energy core tiers, Chaos Guardian/Chaos Shard gate and reactor.

### Important verified source mechanics

[DEConfig](https://github.com/Draconic-Inc/Draconic-Evolution/blob/6251e32bc93eb4bfcd910236ec1eaa6546516229/src/main/java/com/brandon3055/draconicevolution/DEConfig.java) core capacity defaults OP tiers1–7:45,500,000;273,000,000;1,640,000,000;9,880,000,000;59,300,000,000;356,000,000,000;2,140,000,000,000; tier8=-1 sentinel, do not assert exact resolved long cap without dependency verification. Four stabilizers, advanced tier5+. Fusion injector upgrades source recipe energies 32000/256000/8000000 OP; four tiers. Spawner delay ranges200–800/100–400/50–200/25–100 ticks. Existing comprehension evidence supports reactor fuel/charge/shield/saturation and catastrophic failure; no new reactor operation guide or tested output.

[DEModules](https://github.com/Draconic-Inc/Draconic-Evolution/blob/6251e32bc93eb4bfcd910236ec1eaa6546516229/src/main/java/com/brandon3055/draconicevolution/init/DEModules.java) and [UndyingData](https://github.com/Draconic-Inc/Draconic-Evolution/blob/6251e32bc93eb4bfcd910236ec1eaa6546516229/src/main/java/com/brandon3055/draconicevolution/api/modules/data/UndyingData.java) define native defaults:
| Undying tier | Health restored (HP) | Shield boost | Boost duration sec | Charge sec | Nominal OP cost | Invulnerability ticks |
| --- | ---: | ---: | ---: | ---: | ---: | ---: |
| Wyvern | 6 | 25 | 15 | 120 | 5000000 | 2 |
| Draconic | 12 | 50 | 30 | 60 | 10000000 | 3 |
| Chaotic | 20 | 100 | 120 | 45 | 20000000 | 3 |

[UndyingEntity](https://github.com/Draconic-Inc/Draconic-Evolution/blob/6251e32bc93eb4bfcd910236ec1eaa6546516229/src/main/java/com/brandon3055/draconicevolution/api/modules/entities/UndyingEntity.java) recharges each equipped server tick with sufficient stored OP, integer rate nominal energy / charge ticks, clears charge at successful save, boosts shield when controller present; Draconic+ removes nonbeneficial effects and fire. [Armor event handler](https://github.com/Draconic-Inc/Draconic-Evolution/blob/6251e32bc93eb4bfcd910236ec1eaa6546516229/src/main/java/com/brandon3055/draconicevolution/handlers/ModularArmorEventHandler.java) highest-priority LivingDeathEvent, excludes DE KILL damage and canceled/client cases, searches eligible equipment and cancels on successful module save. General death is not blocked during invulnerability window; guardian laser special branch. Draconic max install2, Chaotic3 by module registration, not a universal promise of six charges or any effective loadout. This is actual death prevention, not resurrection. Native flight authorization is separate from TMM policy #330.

[Draconium Chest](https://github.com/Draconic-Inc/Draconic-Evolution/blob/6251e32bc93eb4bfcd910236ec1eaa6546516229/src/main/java/com/brandon3055/draconicevolution/blocks/tileentity/chest/TileDraconiumChest.java) source 260 main slots plus five furnace input slots, real multi-item storage and managed item handler, auto-feed from main inventory, powered smelting. [ModularHoe](https://github.com/Draconic-Inc/Draconic-Evolution/blob/6251e32bc93eb4bfcd910236ec1eaa6546516229/src/main/java/com/brandon3055/draconicevolution/items/equipment/ModularHoe.java) uses selected AoE, fills supported spaces with inventory dirt after place event, then attempts tilling; no proof of auto-seed/harvest. This distinguishes farming support from claims of a complete crop factory.

### Research boundary
Information Tablet requires Project Intelligence, absent in prior roster profile; recheck if changed before quest authoring. Earlier profile's End terrain replacement, dragon/Chaos Crystal reward ownership and reactor safety remain open exact-binary/effective QA. TMM Draconium and Chaos Shard gates, fusion mapper, flight/capture and effective OP rates separately owned. Do not grant a native Eldritch Miner or assume native unrestricted ore access. Full Atlas 29-axis dossier, actual packaged recipe/registry census, module numbers and instructional map still due.

## Replication — 21/55, 4 independent, threshold Substantial (provisional)

Combat0 Protection0 Transportation0 Death0: no such native player gear/system in inspected registry. Survival3 native food anchors and learned pattern manufacture can independently supply food from other eligible matter inputs and electricity; not healing, hunger bypass, or infinite food. Gathering4 matter-based material synthesis substitutes for collecting many learned items; no native mining machine or free resource generation. Industry5 its primary independent system disintegrates items into typed matter, scans patterns and creates items using a powered network, parallel and continuously filtered replication; cannot freely manufacture unvalued or excluded patterns. Construction2 renewable eligible building material supply, no block placement/excavation system. Logistics2 specialized Matter Tanks, energy/matter network, pattern library and output routing; not actual arbitrary inventory storage/sorting/retrieval. Life2 native organic/living matter can create eligible animal/plant products but does not grow crops, breed/spawn creatures or automate harvesting. Advancement3 independent expanding learned patterns plus chip storage/network expansion, enclosure and motor upgrades; no permanent innate power or ranked gear system. This is borderline Substantial: survival3 depends on actual independent food printing, advancement3 on meaningful learned-pattern/production progression. Downgrading either by one point gives20/55, three independent, Minor. No special exemption.

### Native processing and ceilings

[Registry](https://github.com/Buuz135/Replication/blob/8482d3e995c6185ea760251f0140ceab0b3e8328/src/main/java/com/buuz135/replication/ReplicationRegistry.java) eight matter types Earth, Nether, Organic, Ender, Metallic, Precious, Living, Quantum (Empty sentinel not ninth resource). [Config](https://github.com/Buuz135/Replication/blob/8482d3e995c6185ea760251f0140ceab0b3e8328/src/main/java/com/buuz135/replication/ReplicationConfig.java) defaults disintegration40 progress/1500 FE per completed action, six internal tanks each16000; Identification Chamber100 half-cycle progress,5000 FE per accepted scan, +.5 ordinary identification progress, source consumes sample; Blueprint can carry other progress. Memory Chip16 patterns; Chip Storage8 chips, nominal128 patterns with duplicate/component/value checks. Matter Tank256000 capacity; matter pipe power transfer config2560, not universal throughput or per-port guarantee. Replicator100 progress per half-cycle,80FE/t while actively advancing, seven output slots; enclosure0.8 duration/1.1 power multiplier; motor slider20–100 adjusts duration and introduces failure chance. Do not derive unconditional throughput or power cost from nominal progress alone; conditions, retries, network allocation and output space matter. Terminal18 output slots, pattern browsing, request queue and typed matter supply, not general storage terminal. [MatterNetwork](https://github.com/Buuz135/Replication/blob/8482d3e995c6185ea760251f0140ceab0b3e8328/src/main/java/com/buuz135/replication/network/MatterNetwork.java) prioritizes matter suppliers/consumers, syncs task state. No arbitrary raw item network.

[Beef native anchor](https://github.com/Buuz135/Replication/blob/8482d3e995c6185ea760251f0140ceab0b3e8328/src/generated/resources/data/replication/recipe/matter_values/minecraft/items/beef.json) organic4/living4. Data provider assigns corresponding food items such as carrot, potato, apple and fish; wheat organic2/earth2. This supports actual food synthesis rather than conjectural survival. Native recipe calculation depth11, visited recipes50, remaining-item subtraction true, ignored recipe mod list default ae2qolrecipes. No universal material conversion rate or arbitrary pattern support.

### Pack boundary
Native creative matter tank/chip are not survival score evidence. Prior owner-approved Eldritch Miner acquisition and Embers Replica seed are other mods/TMM mechanics, not native Replication mining. Existing #448/#565 stonecutter exclusions: catalog 1720 unique outputs on older live capture; native cant_be_scanned/cant_be_disintegrated/skip_calculation tags are not a dedicated cant_be_replicated tag; explicit anchors/already learned patterns need separately owned enforcement. Do not claim pending implementation merged or effective on current build. Actual loaded matter values and protected resources require downstream QA. Full native 29-axis dossier, exact binary/recipe census, acquisition and numerical task details still due.

## Handoff

Two source-based preliminary evaluations, not closure of full research queue. Preserve earlier profile and pending runtime exclusions. Separate research PR with no shared index, gameplay/config/quest/build changes. Next full binary research can confirm source compatibility, expand teaching slices and revise scores if needed. Remaining Voidscape, Relics/Artifacts/Reliquified Artifacts, Dragon Survival and Improvable Skills still outstanding.
