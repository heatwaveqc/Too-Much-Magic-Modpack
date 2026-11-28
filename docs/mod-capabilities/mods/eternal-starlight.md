# Eternal Starlight 0.9.1 — Atlas complete native capability dossier

**Status: quest-source-ready / needs-review, 2026-11-27.** Tracker [#410](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/410). Independent adventure-dimension research boundary after artifact family #799; no overlapping shared index, quest, config, or gameplay work. Native functionality presumed working. Three evidence passes complete; final instructional map and separate evaluation below.

## Evidence and scope

Exact Drive artifact eternalstarlight-0.9.1+1.21.1+neoforge.jar, 59,325,382 bytes, SHA512 2bd7d2fbc478b3cc2b6426ff9cffddf8800b46aa2ca5a4570f9d9ed2f50d4aeadc83bea0c5c410bf26c2e9e0c6cc3296cb8adff233787897265aa2d7a1ad9a71 matches [active pin](../../../mods/eternal-starlight.pw.toml), Modrinth XvcfUGfB/IyTc0Vvj. zipfile.testzip clean, 11465 entries,1447 classes. Metadata internal ID eternal_starlight, MC[1.21.1,1.22), NeoForge>=21.1.181, MIT, no other mandatory dependency declared. 1283 recipes:546 shaped,421 stonecutting,154 shapeless,54 smelting,37 blasting,18 smithing transform,11 alloy,10 smoking,10 campfire,7 Ether conversion,5 drying,4 trim,3 geyser smoking,2 tool modification,1 accessory combination. 1326 advancement JSONs including61 non-recipe progress records; 832 loot tables,440 tags,285 worldgen records,13 enchantments,18 damage types,24 jukebox songs,17 trim materials,4 trim patterns. 23 actual biome JSONs (exclude biome tags), 8 actual structure definitions/sets. 111 custom data JSONs:38 paintings,25 coolant,25 biome data,8 seeds launcher ammo,5 Ent variants,3 Boarwarf types,2 Astral Golem materials,2 Seeker variants,2 Shimmer Lacewing variants,1 Gatekeeper names. Native book custom resource path assets/eternal_starlight/eternal_starlight/books/main.json,49 section files and en_us1844 entries; no external guide dependency required. Parsed all recipe JSONs and nonrecipe advancement predicates, selected boss loot pools and book descriptions; javap -p -c of 16 key classes plus ESPortalBlock.Validator. Pass1 resource census plus pass2 compiled operating conditions below; not proof every handler has been audited. All unqualified native item/block IDs in this document use eternal_starlight:.

## 1. Entry and advancement spine

Native guide **Glimmering Tablet** (book), no packaged ordinary recipe or other loot-table source found except bosses/the_gatekeeper. ESBookItem.use sends server book component ID and player's unlocked sections via OpenBookPacket; some sections discovery gated. Overworld portal ruins variants common,cold,desert,forest,jungle; each random spread spacing36/separation30 chunks, distinct salts and biome predicates; not five guaranteed nearby structures. Locate naturally, interact with Gatekeeper and choose Challenge/confirm; native challenge_gatekeeper trigger, then obtain Orb of Prophecy and Tablet. Boss loot custom context has one Glistering weapon choice (sword/greatsword/morning star/bow), disc and other material pool. Actual per-player challenge reward delivery and respawn state next pass. Orb recipe four glass corners and five Blue Starlight Crystal Shards cross; ordinary replacement crafting means do not assert Gatekeeper fight is an unbypassable hard-coded global prerequisite. OrbOfProphecyItem.useOn validates block tag portal_frame_blocks (exact sole default chiseled_voidstone), calls ESPortalBlock.validateAndPlacePortal in X then Z axes; valid placement makes sound, does not consume orb in this method. Portal geometry uses diamond-shaped frame in placePortal; use actual ruins initially, validate precise size constraints before manual-build instruction. Dimension ID eternal_starlight:starlight. Return destination and eligibility next pass.

Craft Seeking Eye x16: eight Starlight Flowers around one c:ender_pearls. Tablet: open star map, select biome/structure, eye follows and points toward target, interact to recall with possible shatter. Numeric ranges/chances and target list deferred. Explore regional materials, protection, foods and equipment. Native advancement ancestry enter_starlight -> obtain_seeking_eye -> enter_golem_forge -> freeze_starlight_golem -> deactivate_energy_block -> kill_golem -> ignite_lunar_monstrosity -> kill_lunar_monstrosity -> full_unrealium_armor. Permafrost separate child of enter_golem_forge. This is suggested teaching order, not proof boss access is hard-gated by advancement possession.

## 2. Dimension and encounter inventory

23 native biomes: crystallized_desert, dark_swamp, ether_river, glimmer_scrubland, grim_shore, icy_sea, lucent_mycelium_isle, lush_shallow_sea, permafrost_peaks, scarlet_forest, shimmer_river, solaris_isles, spiral_kelp_forest, starlight_dense_forest, starlight_forest, starlight_permafrost_forest, starlight_taiga, starlit_sea, starlit_sky, the_abyss, torreya_forest, umbral_plains, warm_shore. Actual structure sets: five portal ruins + golem_forge/cursed_garden custom landmark placements + stranghoul_den custom avoid_landmark (spacing20/separation8). Biome_data count25 not same as 23 biomes. Advancement requires all listed biome predicates, not visits to every data record. 
| Encounter | Native technique (Tablet and advancement criteria) | Actual named boss-table rewards |
|---|---|---|
| Gatekeeper | interact/challenge, obtain portal tools | Tablet, Orb, one Glistering weapon, disc, assorted material roll |
| Permafrost | Golem Forge cooling-unit boss; cold attacks, Frozen Tubes | Coldsnap, 5–12 oxidized golem steel nuggets,12–18 frozen tubes, painting50%, common pool |
| Starlight Golem | stationary shielded boss; throw Frozen Tube to induce charging sooner; deactivate adjacent Energy Blocks during recharge to expose | 8–10 oxidized golem steel ingots, forge trim template, oxidized alloy furnace, Energy Sword75%, disc/painting each50%, common pool |
| Lunar Monstrosity | Cursed Garden; burn shell to make vulnerable, avoid burrow/plant attacks | Moonring Bow,10–15 tenacious petals,10–15 vines,3–4 soul dew,5–8 tangled skulls, Wand of Teleportation75%, Crescent Spear75%, Crescent Pendant, Twining trim, discs/painting each50%, common pool |

These are the packaged custom boss table pools, not yet a proof every participant receives all unconditional pools at every repeat challenge; ESBoss and BossChallengeCountCondition require next-pass audit. SolarCreeper classes exist but no packaged corresponding boss table/advancement in this census; do not count as accessible fifth boss based on class alone. Other creatures: Boarwarf, Astral Golem, Gleech, Lonestar Skeleton, Nightfall Spider, Seeker, Thirst Walker, Creteor/Tiny Creteor, Stranghoul, Ent, Ratlin/Zombified Ratlin, Shadow Snail, Yeti, Aurora Deer, Crystallized Moth, Shimmer Lacewing, Starfire Bird, Grimstone/Aethersent Golem, Rookfish, Luminofish, Luminaris, Twilight Gaze, Freeze, Tangled/Tangled Skull. Need exact spawn/loot and combat handler map. Meteor shower is meaningful repeatable event: falling meteors deliver Aethersent ore and may spawn Creteors; Aetherstrike Rocket manually calls shower; Aethersent Golem intercepts meteors and flying enemies, cannot attack ground targets, per Tablet. 
## 3. Native industry and typed resources

Alloy Furnace (oxidized form boss reward, not ordinary craft; two waxed-form recipes only), coolant data25. Distinct from FE: alloy inputs, fuel/cooling and heat, not generic electrical power. All 11 alloy recipes burn_time400 ticks (actual duration/temperature conditions next pass):
| Input | Main result |
|---|---|
| 1 iron ore + saltpeter powder | 2 iron ingots |
| 2 raw iron + saltpeter | 3 ingots |
| 1 deepsilver ore + saltpeter | 2 ingots |
| 2 raw deepsilver + saltpeter | 3 ingots |
| 2 copper or gold ores + Starcore + saltpeter | 3 corresponding ingots |
| 3 raw copper or gold + Starcore + saltpeter | 4 corresponding ingots |
| 3 deepsilver ingots +2 golem steel nuggets | 1 golem steel ingot |
| 4 gold ingots +2 netherite scraps | 1 netherite ingot |
| 1 deepsilver ingot +1 golem steel ingot +1 Malarite +1 Soul Dew | 1 Unrealium ingot |

Ore batches also have two random Dimslag output entries (0–2 each iron/deepsilver or0–3 each copper/gold). Native recipe netherite scrap economy notable; TMM permission not established by JAR. Golem Steel and Soul Dew create meaningful forge/garden material gate, not simple sequential Netherite upgrade.

Crystalborn Catalyst: recipe 4 Cinder Bricks +4 Teeth of Hunger +Redstone. Tablet: redstone-powered and uses crystals to infest surrounding blocks and collect their resources. Actual block eligibility, radius/fuel/loot and safeguards next pass; don't classify as universal ore generator yet. Energy Transmitter =2 golem steel nuggets +2 redstone +3 stone; link emitter then receiver, attached faces transfer signal wirelessly, **redstone, not FE**. Accumulator =4 redstone +4 golem steel nuggets +redstone lamp; red-dot face input, other five outputs, color indicates signal. No generic item network.
Drying Rack recipes: rotten flesh→leather2400t no fire; rotten flesh→jerky600t with fire; clay ball→brick150t with fire; kelp→dried kelp100t with fire; wet sponge→sponge100t with fire. Geyser smoking: 1 white dye→black,3 grimstone→2 voidstone, skeleton skull→wither skeleton skull; exact operation/timing next pass. Ether fluid conversion: any accessory-tag item→Butterfly Wings Amulet, shattered sword→blade, music disc tag→equal weighted Spirit/Ether Rain, four paintings→special variants. No universal item transmutation. Tool modification: Saltpeter Matchbox+stick→4 torches, shears+Twining template→Blooming template. Inputs/tool consumption and fluid conditions need handler check.

## 4. Gear and augmentations

Compiled ESItemTiers has18 named material definitions, not18 complete tool sets. Selected native tiers (uses/mining speed/tier attack contribution/enchantability): Aethersent1750/12/1/22, Thermal Springstone400/7/2/10, Glacite500/7/2.5/10, Starlit Diamond2500/9/4/20, Deepsilver800/12 (other values deferred), Unrealium1200/15/4/22, Starfire1000/8.5/2.5/15, Flowglaze1000/8.5/2.5/15, Golem Steel1000/7.5/3/22, Petal1500/7.5/3.5/22, Doomeden2000/7.5/2.5/10. Raw tier contributions not final weapon stats. Exact armor material and special item effects next pass. Native weapons include Aethersent arrows/Rage of Stars/Starfall Longbow/Galactic Quiver, Thermal Springstone hammer/scythe, Glacite shield, Deepsilver sickle/brush, Dagger of Hunger, Golem Steel greatsword/Mechanical Crossbow/Energy Boomerang/Energy Sword/Underminer, Crystal Greatsword/Crossbow, Moonring Bow/Greatsword/Petal Scythe/Chain of Souls/Wand of Teleportation/Crescent Spear/Gravity Pickaxe, Doomeden gear. Fang-style hunger weapon consumes nutrients in combat and can harm owner if starved (Tablet); exact handler. Full-strength hammer critical triggers special attack advancement.

Six actual accessory IDs: battleaxe_pendant, warhammer_pendant, butterfly_wings_amulet, fungus_amulet, pearl_necklace, crescent_pendant. Distinct native equipment-component system, NOT generic Curios slot bonus. Tablet explains bundle-like insertion/removal and crafting combination; each has compatible host type and host slot count; durability exhaustion destroys attached accessories. Tags define axe/chest armor/hammer applicability; exact per-accessory item class/host slots/effects next pass. Butterfly from Ether, Pearl from fishing above Abyss, Crescent boss pool. Remaining native loot paths next pass. Unlike universally available Curios, equipment-dependent accessories may be relevant only to restricted gear analysis under #721; no balance adjustment made.

## 5. Life, survival and gathering branches (native Tablet and criteria)

- Thirst Walker Tooth of Hunger on kill or blocked attack; craft Dagger and Catalyst. Crystallized Desert Crinoa seeds, Lunaris Cactus fruits/gel. Crystallized Moth tame with meat; loyal sound-wave guard and convert gel into Shivering Gel for Sonar Bomb/Crystal Greatsword. Exact feeding threshold later.
- Permafrost forest Ashen Snow layer, Glacite Shard→Glacite gear/arrows/Frozen Bomb. Yeti shear fur and breed with Lunar Berries/Cave Moss; Aurora Deer can drop antlers by charging into hard blocks, breed with berries.
- Ether River Thioquartz hides Nocturnal Millet seeds. Grow on dirt/farmland/Dusted Gravel, harvest panicles with stalk intact except wither chance; withered crop destroys nearby crops, bonemeal restores. Dusted Gravel + bonemeal induces Forgotten state that steals neighboring crops' life to accelerate maturity. Not normal unlimited bonemeal.
- Starfire Bird: offer seeds in nests to receive rewards including Starfire, construct aviary or collect nest with Silk Touch axe; breed with seeds, egg in nest. Throw Starfire on Twilight Sand to create Flowglaze, enhance Thermal Springstone equipment/campfire. Thermal Springstone from giant plants via fire (advancement hint, exact block route later).
- Stranghoul barters Malarite, hire for one day using Pungency Stew; low-health adventurers vulnerable to attack, fear silver weapons. Rotten flesh + fruit stew removes Hunger (guide); Silver Pungency Fruit grants Numbness temporarily deferring damage, not immunity (damage returned later).
- Rookfish bucket and air sac equipment/arrows; Abyss Luminofish/Luminaris/Twilight Gaze and aquatic resources. Ent breed Abyssal Fruit/Lunar Berries. Bouldershroom Stew enables climbing (advancement); Dusted Shard from ocean floor; strip Torreya logs for Raw Amaramber; underground Stellagmite melt via lava; Jinglestem Sandwich. 
- Carved Lunaris Cactus Fruit on Grimstone Bricks summons item-display Grimstone Golem; on Aethersent Block summons anti-air/meteor defense golem. Actual owner/target rules next pass. No generalized permanent army claimed.
- Native 61 advancements cover entry, biome exploration, gear/materials, Catalyst, meteor/weather, agriculture, companions, combat and boss rewards; extract all individual criteria into quest map in later pass. No gameplay requirement for native research closure.

## 6. Capability axis navigation (read with final mechanics below)

| Axis | Current evidence |
|---|---|
| gear | many native tiered sets, distinct special weapons |
| movement | Seeking Eye navigation, Wand of Teleportation, climbing food; actual movement mechanics next |
| ore_processing | alloy yields, smelt/blast |
| storage | 512-arrow Galactic Quiver, machine inventory and chest boats; no general warehouse |
| crafting | alloy/ether/drying/gear recipes |
| logistics | redstone signal routing; no generic storage network established |
| buffs | Numbness, local armor/accessories, food |
| external_compatibility | JEI/EMI class paths, common tags |
| mob_farming | breeding and animal resource loops |
| ore_generation | meteor-derived Aethersent only; Catalyst consumes existing terrain, not generation |
| permanent_power | equipment-bound enhancements, no innate skill tree established |
| general_generation | meteor, plant/bird outputs, recipe conversion |
| direct_damage | ranged, melee, hammer crit, bosses, summons |
| block_breaking | native tiers and special tools |
| permanent_pets | tamed moth and summoned golems, owner rules next |
| temporary_pets | one-day hired Stranghoul, teleport wand Tangled Husk |
| defense | armor, shields, Numbness, golem |
| healing_recovery | food and effects; exact recovery next |
| debuff_control | Pungency Stew and armor, cold interactions |
| energy | crystals/fuel/heat/cooling/redstone, no FE demonstrated |
| automation_control | catalyst, transmitters, accumulator, machine handling |
| agriculture | millet, crinoa, birds, breeding, gels |
| detection_information | Seeking Eye and Tablet |
| equipment_augmentation | six accessories and smithing, enchants/trim |
| world_manipulation | Ether/Flowglaze conversion, catalyst, terrain tools |
| world_content | 23 biomes, landmarks, encounters |
| death_respawn | absent as independent native capability; Numbness and Crescent are mitigation, not resurrection |
| trade_economy | Stranghoul Malarite barter, Gatekeeper trade class |
| progression_tiers | entry trial, gear, forge and garden material gates |

Current integration index has native mod pin plus TMM Alkahestry raw Aethersent and Deepsilver recipes, no directly named Eternal Starlight quest/config path found. Check those exact conversion JSONs and any global dimension/progression/ore policies in subsequent pass; do not assume live effect. Next pass: compiled boss configs and personal reward/spawner/rechallenge, portal geometry/return, Seeking Eye targets, alloy fuel/coolant heat/throughput, Catalyst exact operation, equipment/armor/accessories, enchantments, all meaningful companion/effect loops. Final pass: consolidate acquisition/61 criteria and actionable quests, remaining 29-axis dispositions and separate editorial evaluation. No score asserted yet. No game execution, changes to pack, new mod, balance or quest implementation.

## 7. Second pass — exact compiled operating rules (2026-11-27)

Reopened exact Drive JAR, SHA512 and ZIP CRC match again. Inspected javap -p -c of 35+ relevant compiled classes: ESConfig and ItemsConfig/MobsConfig records, AlloyFurnaceBlockEntity, CrystalbornCatalystBlockEntity and Cursor, ESBoss, TheGatekeeper, BossChallengeCountCondition, LootChestBlockEntity, BossSpawnerBlockEntity, StarlightGolem, Permafrost, LunarMonstrosity, ESPortalBlock/Validator, SelectSeekingEyeTargetPacket, SeekingEyeItem/Targets, ESArmorMaterials, ESItemTiers, ESAccessoryUtil, ESItems, WandOfTeleportationItem, CrystallizedMoth, AethersentGolem, StarfireBird/Nest, Stranghoul, MeteorShowerWeather and redstone block entities. Native guide/localization and exact data tags/coolants/loot conditions checked. Numerical defaults are not measured loaded TMM values.

### 7.1 Bosses and personal rewards

Compiled ESConfig.MobsConfig defaults (attack damage scale =1 for all four, spawn enabled):
| Boss | Max HP | Armor | Follow range |
|---|---:|---:|---:|
| Gatekeeper | 175 | 15 | 200 |
| Permafrost | 120 | 10 | 100 |
| Starlight Golem | 200 | 10 | 200 |
| Lunar Monstrosity | 200 | 12 | 200 |

Attack scale is not actual attack damage. ESBoss.hurt registers unique UUID of ServerPlayer causing a successful damage event; die also adds qualifying killer, iterates surviving same-dimension participants. Mere proximity is not guaranteed credit. BossChallengeCounts personal attachment, per boss key. Default config enableLootChest=true, enableBossRespawn=true, bossRespawnCooldown=36000 ticks (30 min at20TPS). BossDefeat attempts to place respawn spawner at initial position when configured and replaceable, then sets cooldown; cooldown is not a promise of exact real-time respawn unloaded. Gatekeeper challenge distinct NPC interaction. ESBoss.spawnBossLootChest creates actual loot chest and adds each eligible participant UUID with their personal count map, if chest placement succeeds. Fallback creates personal targeted glowing extended-lifetime loot bags for eligible participants or a common bag when none qualify. Special grant hook separate. doMobLoot gates native reward. No guarantee for offline/dead/dimension-changed players or cross-mod indirect attribution. Excellent native co-op reward model; teach participants to deal actual player-attributed damage and claim their own chest rewards, not steal/share a single boss roll.

**Correction to pass1 loot wording:** Gatekeeper Tablet and Orb entries have explicit boss_challenge_count max_inclusive1, so first qualifying challenge rewards, not unconditional repeats. Glistering weapon pool four entries each conditional random_chance .3, one weighted selection among surviving candidates; can yield none. Permafrost/Golem/Lunar material pools as listed in §2; no challenge-count condition in those packaged tables, but separate eligibility/delivery above. Do not claim exact boss repeat mechanics are identical for Gatekeeper NPC and ordinary spawner-based encounters. All bosses use native combat phases; no unsupported exact attack timeline necessary for quest-source scope.

### 7.2 Portal and navigation

ESPortalBlock.Validator tests diamond Manhattan radius r=2..10 in vertical plane X or Z (outer width and height 2r+1, frame unique positions4r, interior Manhattan distance<r, 1+2r(r−1) hollow cells). At minimum r2: eight frame blocks, five interior cells; at maximum r10: forty frame blocks,181 interior cells. Validator checks frame tag and accepted empty interior; activate valid Chiseled Voidstone frame with Orb, no consumption in useOn. Native return destination from Starlight is Overworld; from any other source level it targets Starlight, server dimension-travel event must allow, ESTeleporter resolves destination. Ordinary player portal transition80 ticks, invulnerable ability player1 tick, nonplayers0; not instant normal survival. Do not confuse permitted destination with guarantee of TMM Newbie permission or claim-safe destination. Seeking Eye packet locate: structure search radius argument100 (native chunk-generator search units, not guaranteed physical radius), biome search6400 blocks, horizontal step32, vertical64. Uses server locate and successful target, not arbitrary coordinates; exact target menu/cost/recall shatter odds remain final pass. No forced client/command location. Wand of Teleportation raycast64-block reach in use; actual selected destination constrained by landing check; successful invocation creates thorn circle at origin and destination and owner-tagged Tangled Husk at origin, calls platform teleport event before move, consumes1 durability and 600t cooldown (30s) even if teleport event vetoes after invocation. This is real movement/temporary attack system, not unlimited transport or flight.

### 7.3 Alloy Furnace thermal accounting

14 slots: ingredient grid0–8, fuel9, coolant10, outputs11–13. Recipe time400 ticks all11; uses platform furnace fuel burn-time with alloy recipe type; consumes fuel only when canBurn and unlit, retains/handles container remainder, uses active lit/canBurn progress+1/tick otherwise−1 clamped0..recipe time. Max3 output result slots, canBurn validates output fit. Nominal continuous 20s/batch, 3 batches/min at20TPS; heat is independent of output completion. While lit, heat+1 every tick without cooling; with cooling only increases when remaining litTicks modulo efficiency==0. While unlit, heat decreases1/tick, plus coolant efficiency if active. Coolant loaded only when heat>0 and no cooling ticks left; duration decreases while active. Overheat clamped0..12000, default explosion radius3 (configuration parameter, not measured actual destruction radius). At cap furnace explosion path; 10min cumulative uninterrupted uncooled burning from cold; cooling reduces increase, does not by itself cool an actively lit furnace. Teach pause to cool; no guaranteed indefinite safe burn merely adding ice. Front/back ingredient automation, sides fuel/coolant and bottom extraction per sided slots; verify exact orientation if needed in final.
| Coolant | Duration ticks | Efficiency |
|---|---:|---:|
| Snow/ice/snowball/Ashen Snow/Ashen Snowball/icicle | 800 | 3 |
| Snow block | 3200 | 3 |
| Packed ice | 8000 | 3 |
| Blue ice | 32000 | 5 |
| Glacite shard | 1200 | 5 |
| Glacite | 7200 | 5 |
| Glacite block | 12000 | 5 |
| Frozen Tube | 1600 | 4 |
| Glacite tools/armor/arrow/shield (12 individual records) | 300 | 4 |

25 coolant JSONs total (12 distinct Glacite gear/arrow records including axe, boots, chestplate, helmet, hoe, leggings, pickaxe, scythe, shield, shovel, sword, arrow). At continuous burn with uninterrupted efficiency5 cooling approximate one heat tick per5 burn ticks, still eventually reaches cap; useful coolant duration is not direct heat removed. Can calculate nominal batches from cold ~30 uncooled if burn continuous, stop earlier rather than teaching destructive test. Default full heat and no fuel/no coolant takes600s to clear; active efficiency5 unlit gives6/tick, conditional duration enough. Actual server TPS, idle gaps and input/output availability matter.

### 7.4 Crystalborn Catalyst: destructive harvesting, not ore creation

Exact config maxRange128 (distance squared check at start of cursor iteration; a cursor can step one block beyond threshold before removal), energyPerShard50. Container15 = fuel0 +14 output slots. LIT requires redstone state; each active tick valid fuel increments charge toward30, absent decrements, clamp0..30, charge30 consumes one shard and adds50 internal energy, resets charge. Red and blue crystal shard tag only; no FE. At most8 active cursors, allocate min(random10..19, remaining energy), one new cursor per active tick while capacity; allocation deducted from central energy. Cursor starts at machine, follows neighboring directions; previously converted crystallized sand/red or blue crystal moss can be traversed without new harvest cost, 1/3 chance change direction; native preference tag #c:ores. Actual replaceable block tag = #minecraft:sculk_replaceable, #minecraft:sand, #c:ores, #eternal_starlight:base_stone_starlight. Moss subset minecraft:moss_block,cave_moss_block,#minecraft:dirt. Calculates drops with ordinary default Diamond Pickaxe, origin and optional block entity; no player or Fortune attached. Calls ServerLevel.destroyBlock(pos,false) then replaces with red crystal moss if moss subset else crystallized sand; emits collected drops, decrements cursor energy1 per successful replacement. Outputs merge into14 slots, overflow popResource at machine. Existing transformed terrain costs no extra cursor energy; unsuitable path/failed destruction can waste allocated energy, no guaranteed50 ores per shard, no ore creation, no unconditional throughput. No explicit claim/player event call observed in Cursor.tick prior to world mutation; platform/global protections could exist. This is a **specific cross-mod claim and protected ore policy question**, not proof of observed claim bypass. Recommend separate scoped source audit of effective tags/protection; do not silently patch or exploit. TMM direct indexed mod-specific paths currently only pin and two Alkahestry recipes (§7.7).

### 7.5 Equipment and accessories

Exact compiled ESArmorMaterials registry contains nine material definitions (some partial sets). Selected base raw four-player-piece sums, toughness summed four times if full set, raw knockback resistance additive, engine cap applies:
| Material | Helmet/chest/legs/boots | Armor sum | Toughness per piece | Knockback per piece | Enchantability |
|---|---|---:|---:|---:|---:|
| Unrealium | 2/5/5/2 |14|.5|0|25|
| Deepsilver |3/8/6/3|20|.5|.2|25|
| Starlit Diamond |2/6/5/2|15|3|.1|22|
| Glacite |3/8/6/3|20|.5|0|9|
| Thermal Springstone |3/6/5/3|17|.5|0|15|
| Aethersent |2/6/5/2|15|0|0|35|
| Amaramber |2/6/5/2|15|0|0|9|
| Alchemist |1/4/4/1|10|0|0|15|

Material maps not necessarily all four items actually registered; verify ESItems registrations and special armor effects final pass. Native raw protection does NOT establish Unrealium superior armor to Deepsilver; specialized bonuses matter. Accessory effects from exact tooltip and ESAccessoryUtil attachment/UI handlers:
| Accessory | Native role |
|---|---|
| Battleaxe Pendant | reduce axe combat durability loss; prevent stripping |
| Warhammer Pendant | damage scales with movement speed |
| Butterfly Wings Amulet | use Ether to erode armor of attackers/targets |
| Fungus Amulet | wearer can consume fungus |
| Pearl Necklace | instant air recovery when exiting water |
| Crescent Pendant | cap incoming damage at75% maxHP; gradually repair Lunar Monstrosity weapons |

Tooltip statements are not yet all independently traced handler constants. ESAccessoryUtil reads active host item components, uses Set<Item> across relevant equipment slots (duplicate identity dedup in that utility), apply/remove and override stack-on-equipment operations. Compatibility tags axe #minecraft:axes, chest armor #minecraft:chest_armor, hammer #eternal_starlight:hammers. These are equipment augmentations, not Curios; exact per-host slot counts and handler conditions final pass. Crescent damage cap is not a guaranteed death save at low health. Numbness tooltip defers75% incoming damage, reapplies at end; exact event ordering and damage ledger final pass; not invulnerability.

### 7.6 Companions and repeatable world resources

CrystallizedMoth actual TamableAnimal owner targeting goals (OwnerHurtByTargetGoal, OwnerHurtTargetGoal), follow start10/stop2, owner teleport attempt squared distance>=400 (20 blocks), untamed removeWhenFarAway eligibility only after2400 ticks. Native breeding and food ingredient predicates exist; exact meat/gel conversion and taming roll still final pass. Stranghoul follows hirer and attacks hirer targets; baby growth >24000ticks. Native hiring timer24000 ticks (20 min at20TPS, one Minecraft day); no permanent tamed companion entitlement. Stranghoul has crop/plant/dry rotten flesh/eat/breed goals and native barter tags. Aethersent Golem has explicit meteor target and projectile defense; not universal ground combat pet. Starfire Bird nested gifting/breeding/aviary logic and meteor weather verified compiled class paths, guide-supported actions in §5; exact renewable rates still final pass. No invented measured rates.

### 7.7 TMM actual source interactions and next pass

Two explicit [Alkahestry raw Aethersent](../../../kubejs/data/tmm/recipe/alkahestry/eternal_starlight__raw_aethersent.json) and [raw Deepsilver](../../../kubejs/data/tmm/recipe/alkahestry/eternal_starlight__raw_deepsilver.json) recipes verified: strict no-extra-components item ingredient; charges4914 /108, output counts2 /6 respectively, both conditioned on Reliquary Alkahestry enabled and item existence. These are pack cross-mod duplication recipes, not native Eternal Starlight or guaranteed loaded configuration. Native Catalyst broad ore tags and direct terrain replacement merit separate targeted pack policy investigation, no observed failure claimed. Native multiplayer rewards are a valuable general design reference, not a new TMM implementation request.

Final narrow pass: exact accessory host/effect handlers, special weapons/armor and enchants, Seeking Eye selection and recall, key companion production/acquisition conditions, effect ledger and all61 advancement criteria; complete actionable lesson map, update 29 axes (ore_generation meteor yes, Catalyst gathering only; temporary pets hire/husk; death no demonstrated native death recovery), separate final 11-domain evaluation. No exhaustive ordinary decorative recipes, build or gameplay QA required. No gameplay, balance, config, quest or index changes. 

## 8. Final pass — equipment, status effects, acquisition and operations

Exact SHA512 2bd7d2fbc478b3cc2b6426ff9cffddf8800b46aa2ca5a4570f9d9ed2f50d4aeadc83bea0c5c410bf26c2e9e0c6cc3296cb8adff233787897265aa2d7a1ad9a71, ZIP CRC clean. Reopened all61 actual nonrecipe advancement JSONs (parents and criteria), 13 native enchantment JSONs (excluding tags), exact ammo/coolant/accessory tags, gift table and food/guide data. Targeted javap -p -c of ESCommonHandler, ESItems, LivingEntityMixin, ItemMixin, ItemStackMixin, DiggerItemMixin, NeoForge AxeItemMixin, all nine effect registrations and five effect classes, EyeOfSeeking, StarfireBirdGiftGoal, DaggerOfHungerItem, SeedsLauncherItem, GalacticQuiverItem, HammerItem and armor classes. These findings supersede earlier provisional wording; no gameplay testing required for native source closure.

### 8.1 Equipment-bound accessories, exact attributes and slots

Compiled ESItems supplier constructors (lambda static868–873), ESAccessoryUtil and handler/mixins confirm:
| Accessory | Host tag | Native modifier | Operating behavior |
|---|---|---|---|
| battleaxe_pendant | #minecraft:axes | attack speed +.15 ADD_MULTIPLIED_TOTAL | combat durability cost1 in DiggerItemMixin; axe stripping disabled by NeoForge AxeItemMixin |
| warhammer_pendant | #eternal_starlight:hammers | attack speed +.15 ADD_MULTIPLIED_TOTAL | damage multiplied by min(1+1.5*recorded movement vector length,2) |
| butterfly_wings_amulet | #minecraft:chest_armor | Ether resistance +.5 ADD_VALUE | post-hurt adds200 ticks of Ether exposure to living attacker or victim depending wearer, caps600; exposure erodes armor via native attachment handler, no bottled Ether cost established |
| fungus_amulet | chest armor | heal multiplier +.5 ADD_VALUE | nonfood items in #c:mushrooms become food6 nutrition/saturationModifier1.2, existing ordinary food property takes precedence |
| pearl_necklace | chest armor | oxygen bonus +3 ADD_VALUE, submerged mining speed +4 ADD_MULTIPLIED_TOTAL | player eyes outside water restores max air each native tick |
| crescent_pendant | chest armor | attack damage +.15 ADD_MULTIPLIED_TOTAL | actual eligible damage cap .75*maxHP; repair1 durability each600 entity ticks for six tagged weapons |

Crescent repair tag exact moonring_bow, moonring_greatsword, petal_scythe, wand_of_teleportation, chain_of_souls, crescent_spear. Separate natural-mending tag Amaramber sword/pickaxe/axe/hoe/shovel/sickle/mask/chestplate uses same1/600t server inventory tick, no pendant. Repair rate2/minute at20TPS, not free instantly repaired gear. Accessory attach/remove native stack UI uses compatibility and slot capacity; do not equate compatible host tag with availability of slots on every other mod item. Explicit registered chestpiece capacities: Deepsilver2, Glacite2, Aethersent2, Starlit Diamond3, Alchemist3, Unrealium4; other host defaults from component/utility, not an unlimited general Curios grid. Attribute modifier IDs and set dedup matter; no assumption duplicate accessories stack. Remove before host breaks. Equipment-specific bonuses are not blanket shared Curio power; owner #721 comparison governs later work, no Monk changes.

### 8.2 Native effects and defensive boundaries

Nine registered effects, all exact IDs prefix eternal_starlight:. Note Oblivion not void dimension travel.
| Effect | Concrete action |
|---|---|
| crystal_infection | armor−1.5 per level (ADD_VALUE); damage=amplifier+1 every35 duration ticks, can kill |
| dream_catcher | +5 armor per level, ADD_VALUE |
| sticky | climbing with movement speed penalty (native −.06 modifier), Bouldershroom Stew teaches |
| flammable | incoming fire damage multiplied by amplifier+2, before applicable fire resistance |
| brittle | incoming freezing damage multiplied by amplifier+2 |
| numbness | stores75% of eligible actual damage, applies25% now, then reapplies stored value on first tick without effect and clears attachment |
| teary | vision blur and mob target suppression; immune tag and accumulated exposure immunity; not permanent pacification |
| starfire | post-hurt nearby targets take one-third damage via native Starfire damage type, death spreads effect; no unrestricted recursion or fixed overall DPS claimed |
| oblivion | wall phasing, support below retained unless sneaking, +20% total movement, −20% total gravity, −.2 enemy-follow-range multiplier at level1 |

Numbness/crescent ordering: ESCommonHandler.onModifyLivingActualHurtDamage applies Crescent cap before Numbness split; bypass_invulnerability returns max(original,modified), so some bypass damage still records deferred value without immediate reduction. Exact bypasses_crescent_pendant tag contains eternal_starlight:numbness, so repayment cannot be capped by this pendant. This is NOT death prevention, not infinite stored damage exploit, and not proof interactions with other TMM death effects. Deepsilver full set removes tagged poison/wither/nausea/hunger/slowness/infested/blindness, not all debuffs. Glacite set retaliation freezes; Thermal Springstone set ignites attackers. Unrealium helmet block-suffocation immunity, chest cramming immunity; helmet eating/item-interact, chest damage, legs hit_ground/splash, boots step/hit_ground game event suppression, selected sounds muted; not universal invisibility or immunity. 
**Roasted Forgotten Nocturnal Millet** food10/.8, always edible, guaranteed Oblivion300t at level1 (ESFoods lambda11). Guide states wall traversal and crouch descent, collision mixins and actual effect implementation present. Exact unaffected_by_oblivion block tag bedrock, barrier. Important specific integration question: whether claim-protected walls, restricted areas, dungeons or special blocks permit phase-through; no claim bypass observed and no proposal to exploit. Add to separate TMM claim/policy assessment with Catalyst. Preserve player access under existing policy, do not change mods here.
Amaramber lantern/lit candle/campfire/Amaramber Fire suppress hostile spawning in Starlight dimension per Tablet. Use native flame via raw Amaramber block and flint/steel; do not extrapolate global spawn immunity or exact radius without handler. Distinct from ordinary light level. 

### 8.3 Weapons, utility and 13 enchantments

HammerItem.performCriticalAttack triggers native advancement and nearby living entities in target block AABB inflated2; damage .75*player attack damage with player source, push .4 vertical when accepted. Do not assume exact number of victims, friendly fire immunity or automatic all-Monk access. Dagger of Hunger is dual-wielding weapon with independent offhand attack ticker, nutrition/saturation-dependent damage components and starvation penalty; teach reading its hunger bar, using in combat and satiating (native saturate_dagger_of_hunger). Exact hunger handler uses item state and player food stats; nonessential coefficient timing not prerequisite. Flowglaze weapon consecutive same-target hits, tool threshold instant break, bow flight time damage, shield projectile reflection; guide/tooltip and special handler class paths, do not promise every block instant-mined or combined maxima. Golem Steel Underminer depth-dependent mining speed; Energy Boomerang returning projectile with collection/critical/homing enchants. Galactic Quiver compiled max512 arrows, large item stack component, UI insertion/extraction and arrow inventory hook; Creteor Hide3 craft, not infinite arrows or general inventory. Etheric Eye recipe Seeking Eye+4 Thioquartz gems→4; guide surface teleport, not unrestricted interdimension return. Seeking Eye use opens target GUI, only Starlight, packet target selection/range §7.2, actual EyeOfSeeking recall random .2 shatter else inventory return or drop if full (no extra return to infinite-material player). No guarantee locate success.
Seeds Launcher accepts #c:seeds; exact 8 ammo-type JSON definitions (cooldown seconds, damage multiplier, speed multiplier): beetroot .8/.8/1.2; crinoa1/1.2/1; melon1.1/1.2/.9; nocturnal millet .8/1.5/1.5; pumpkin .5/.4/1.5; pungency fruit1.2/1.5/.9; torchflower1.2/1.5/1; wheat1/1/1. These are type multipliers and cooldown, not final damage or guaranteed ammo availability. Teach seed choice and special enchants rather than repeating all ordinary weapons.
| Enchantment | Maximum level | Exact encoded effect at max (before handler restrictions) |
|---|---:|---|
| Abyssal Touch | II | whip Abyssal Fire duration10s |
| Fearless | II | +1 knockback, pull towards attacker max speed .3 |
| Fertile | III | Seeds Launcher +6 projectiles |
| Gathering | II | boomerang pickup radius +1.75 |
| Glacial Sowing | II | shot-seed freeze8s; exclusive with Overheat |
| Homing | III | boomerang homing strength +1.6 |
| Overheat | II | projectile ignite duration200 raw recipe field; vanilla effect semantics apply |
| Poisoning | IV | retaliatory poison on attacker, duration2.5–4s, amplifier0–4 |
| Precision | V | boomerang critical chance +.8 |
| Soul Snatcher | III | Chain of Souls projectile damage +1.5 |
| Swift Lash | V | whip attack speed +1 ADD_VALUE |
| Tearing | I | Pungency Fruit Spear Teary2.5s |
| Tracing | II | Chain of Souls glowing duration4.5–7s |

Fearless/Poisoning/Soul Snatcher actual treasure tag; Forge loot category Fearless/Precision/Homing/Gathering, Garden Fearless/Poisoning/Soul Snatcher/Tracing; ten remaining non_treasure entries. Raw duration fields copied from JSON; effective custom/vanilla implementation units not conflated with ticks (Overheat numeric200 deliberately recorded as raw field, see caveat). Item supported/primary tags and mainhand/armor conditions apply, not every vanilla enchant can go on every new tool.

### 8.4 Animal, farming and weather loops

Crystallized Moth isFood #minecraft:meat, untamed attempt1/3 success; actual tamed injured meat healing2*nutrition (fallback1 nutrition); feeding Lunaris Cactus Gel consumes1 and 1/5 drops Shivering Gel, independent trials, expected5 gel not guarantee. Follow/sit/defend §7.6. Starfire Bird nest consumes offered seeds; adult bird records last seed player's UUID, each allowance addition2–4 gift deliveries. Gift goal cooldown600ticks after successful delivery, random goal evaluation and valid nearby player required. Exact gift table one weighted roll: Starfire1–3 weight35, feather2–4 weight25, Deepsilver nugget5–10 weight12; no unconditional Starfire per seed, no guaranteed fixed elapsed time. Aviary6 planks+3 Starfire, native wood variants. Breed by feeding seeds to pair, egg incubates in nest, Silk Touch axe preserves occupants/egg as described in guide. Do not assume arbitrary tame/owner teleport (bird trust not moth taming). Aurora Deer antler from impact, Yeti shear, Gleech egg throwable, Ent breeding, Forgotten Millet and Crinoa crops as §5. 
Meteor shower witness and Creteor kill native milestones, craft Aetherstrike Rocket to initiate event, Aethersent meteor interception by golem protects structures; no exact guaranteed ore yield or full active-area spawn density claimed. Aethersent ore is true external material arrival, distinct from harvesting existing ore. 
All Starlight biomes advancement has22 actual location predicates, excludes starlit_sky despite23 biome records; no requirement to visit the unreachable/ambient sky biome. 

## 9. Proposed instructional map — 65 nodes

A = actual native advancement ID (prefix eternal_starlight:), I = possession/craft proxy, O = observable operation/manual instructional credit. The 61 native progression IDs are included exactly once in the evidence column below (some related criteria grouped); dependencies follow action order, not every vanilla advancement parent is a physical unlock. No reward quantities or FTB SNBT authored. Preserve personal boss participation. 
| ID | Prerequisite and player action | Evidence |
|---|---|---|
| ES01 | Overworld portal ruins; locate Gatekeeper and open challenge | A root, challenge_gatekeeper |
| ES02 | Defeat Gatekeeper, collect first-challenge Orb and Tablet, inspect guide | A obtain_orb_of_prophecy |
| ES03 | Activate diamond Chiseled Voidstone frame and enter | A enter_starlight |
| ES04 | Craft Seeking Eye, choose target, recall and learn shatter risk | A obtain_seeking_eye |
| ES05 | Mine local Deepsilver and smelt | A obtain_deepsilver_ingot |
| ES06 | Silver Pungency Fruit/Numbness risk and repayment | A obtain_numbness_effect |
| ES07 | Harvest Starlit Diamond, compare gear | A obtain_starlit_diamond |
| ES08 | Craft a scythe and harvest crops | A obtain_scythe |
| ES09 | Craft/use hammer critical AoE | A hammer_critical_hit |
| ES10 | Harvest Thermal Springstone via fire on native giant plants | A obtain_thermal_springstone |
| ES11 | Collect Dusted Shard | A obtain_dusted_shard |
| ES12 | Strip Torreya logs or break campfire for raw Amaramber | A obtain_raw_amaramber |
| ES13 | Craft Amaramber gear, natural repair and spawn-suppressing light | I/O |
| ES14 | Melt Stellagmite with lava | A obtain_stellagmite |
| ES15 | Cook Jinglestem Sandwich | A obtain_jinglestem_sandwich |
| ES16 | Bouldershroom Stew and Sticky climb | A obtain_bouldershroom_stew |
| ES17 | Find Crystallized Desert | A enter_crystallized_desert |
| ES18 | Obtain and plant Crinoa seeds | A obtain_crinoa_seeds |
| ES19 | Thirst Walker kill/block to obtain Tooth of Hunger | A obtain_tooth_of_hunger |
| ES20 | Combine Teeth of Hunger into blocks | A obtain_tooth_of_hunger_blocks |
| ES21 | Craft Dagger of Hunger, dual wield and watch nutrition | A obtain_dagger_of_hunger |
| ES22 | Satiate Dagger | A saturate_dagger_of_hunger |
| ES23 | Craft Crystalborn Catalyst, feed crystal shards, redstone and observe destructive harvesting safely | A obtain_crystalborn_catalyst / O |
| ES24 | Tame Crystallized Moth using meat | A tame_crystallized_moth |
| ES25 | Feed Cactus Gel, collect Shivering Gel; Sonar Bomb/Crystal gear | I/O |
| ES26 | Throw Gleech Egg | A throw_gleech_egg |
| ES27 | Summon Grimstone Golem with carved cactus fruit, use display | A summon_grimstone_golem |
| ES28 | Enter permafrost forest | A under_permafrost_forest |
| ES29 | Acquire Glacite Shard and make arrow | A obtain_glacite_shard, obtain_glacite_arrow |
| ES30 | Make Frozen Bomb, cold effects and armor | A obtain_frozen_bomb |
| ES31 | Obtain Aurora Deer antler; Yeti shear/breed | A obtain_aurora_deer_antler |
| ES32 | Find Ether fluid and enter | A in_ether_fluid |
| ES33 | Mine Thioquartz, craft Alchemist Mask and Etheric Eye | A obtain_thioquartz_shard, obtain_alchemist_mask |
| ES34 | Grow Nocturnal Millet, Forgotten crop via Dusted Gravel, roast and understand Oblivion limits | A obtain_forgotten_nocturnal_millet |
| ES35 | Convert compatible item in Ether and attach Butterfly Wings Amulet | I/O |
| ES36 | Enter Abyss, aquatic air limits, Rookfish/air sac | A enter_abyss |
| ES37 | Collect Red Velvetumoss Flower, aquatic ingredients, fish for Pearl Necklace | A obtain_red_velvetumoss_flower |
| ES38 | Seed offering in Starfire Bird nest | A put_seeds_into_starfire_bird_nest |
| ES39 | Collect Starfire gifts; construct aviary/breed | A obtain_starfire |
| ES40 | Throw Starfire on Twilight Sand→Flowglaze, upgrade tools | A obtain_flowglaze |
| ES41 | Witness Stranghoul hunt, find den | A witness_stranghoul_hunt |
| ES42 | Barter for Malarite, Pungency Stew hire | A hire_stranghoul |
| ES43 | Witness Meteor Shower | A witness_meteor_shower |
| ES44 | Collect meteor Aethersent, smelt | A obtain_aethersent_ingot |
| ES45 | Kill Creteor, use hide for Galactic Quiver | A kill_creteor |
| ES46 | Obtain Aetherstrike Rocket and initiate native shower | A obtain_aetherstrike_rocket |
| ES47 | Summon Aethersent Golem to defend against aerial targets | A summon_aethersent_golem |
| ES48 | Navigate to Golem Forge | A enter_golem_forge |
| ES49 | Defeat Permafrost, collect Frozen Tubes | A kill_permafrost |
| ES50 | Use Frozen Tube on Starlight Golem | A freeze_starlight_golem |
| ES51 | Deactivate Energy Blocks during charge | A deactivate_energy_block |
| ES52 | Defeat Golem, collect personal loot | A kill_golem |
| ES53 | Obtain Golem Steel Ingot, alloy recipe and recover Furnace | A obtain_golem_steel_ingot |
| ES54 | Install Alloy Furnace; fuels, coolant, outputs, pause heat | O |
| ES55 | Compare ore/metal batch recipes; optional Netherite and Unrealium gate | O |
| ES56 | Energy Transmitter link, Accumulator redstone storage/distribution | O |
| ES57 | Golem steel gear, boomerang enchants and Underminer | I/O |
| ES58 | Find Cursed Garden; ignite Lunar Monstrosity shell | A ignite_lunar_monstrosity |
| ES59 | Defeat Lunar Monstrosity, personal Soul Dew and gear | A kill_lunar_monstrosity |
| ES60 | Collect Crescent Spear, attach Crescent Pendant, repair/mitigation | A obtain_crescent_spear |
| ES61 | Chain Tangled Skull explosion | A chain_tangled_skull_explosion |
| ES62 | Alloy Unrealium, craft full armor; choose chest accessory loadout | A full_unrealium_armor |
| ES63 | Ignite Tear Bomb, use Seeds Launcher special ammo and effects | A ignite_tear_bomb |
| ES64 | Consume Blossom of Stars; optional curiosities and local foods | A use_blossom_of_stars |
| ES65 | Explore remaining22-predicate biome challenge, return through portal | A all_starlight_biomes |

All61 named native nonrecipe progress IDs represented. No need to force all gear or food variants; native material and combat actions provide instructional spine. Supplement ES54/55 with Drying Rack and Geyser smoking/ Ether conversion; teach wand teleportation alongside Garden rewards, optional regional pet/aquatic branches alongside ES24/31/37, six accessories across ES35/37/60/62, 13 enchants in gear stations. 
## 10. Completion and handoff

[Separate capability evaluation](eternal-starlight-evaluation.md): 37/55,8 independent domains, Substantial. The last pass confirms breadth, but no general warehouse/logistics, construction replacement or native death recovery, so not Major under the agreed 9-domain threshold. Earlier provisional impressions are superseded. 29 axes,65 instructional nodes,61 native advancement IDs, 13 enchants,9 effects,6 accessory classes and exact numeric handler evidence. Native source survey complete at significant quest-source depth; residual ordinary recipe parameters, rare loot rates, minor combat phase cadence and optimal cross-mod combinations are not required for normal quest teaching. Cross-mod claim/ore protections (Catalyst, Oblivion) and effective config, per-player quest detection, live biome availability and loaded effect ordering are separate integration questions, not observed failures. No gameplay, build, quest, pin, config or balance changes.
