# Eternal Starlight 0.9.1 — Atlas first-pass research checkpoint

**Status: source-backed-partial / needs-research, 2026-11-27.** Tracker [#410](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/410). Independent adventure-dimension research boundary after artifact family #799; no overlapping shared index, quest, config, or gameplay work. Native functionality presumed working. Final full 29-axis instructional survey and separate rating still due.

## Evidence and scope

Exact Drive artifact eternalstarlight-0.9.1+1.21.1+neoforge.jar, 59,325,382 bytes, SHA512 2bd7d2fbc478b3cc2b6426ff9cffddf8800b46aa2ca5a4570f9d9ed2f50d4aeadc83bea0c5c410bf26c2e9e0c6cc3296cb8adff233787897265aa2d7a1ad9a71 matches [active pin](../../../mods/eternal-starlight.pw.toml), Modrinth XvcfUGfB/IyTc0Vvj. zipfile.testzip clean, 11465 entries,1447 classes. Metadata internal ID eternal_starlight, MC[1.21.1,1.22), NeoForge>=21.1.181, MIT, no other mandatory dependency declared. 1283 recipes:546 shaped,421 stonecutting,154 shapeless,54 smelting,37 blasting,18 smithing transform,11 alloy,10 smoking,10 campfire,7 Ether conversion,5 drying,4 trim,3 geyser smoking,2 tool modification,1 accessory combination. 1326 advancement JSONs including61 non-recipe progress records; 832 loot tables,440 tags,285 worldgen records,13 enchantments,18 damage types,24 jukebox songs,17 trim materials,4 trim patterns. 23 actual biome JSONs (exclude biome tags), 8 actual structure definitions/sets. 111 custom data JSONs:38 paintings,25 coolant,25 biome data,8 seeds launcher ammo,5 Ent variants,3 Boarwarf types,2 Astral Golem materials,2 Seeker variants,2 Shimmer Lacewing variants,1 Gatekeeper names. Native book custom resource path assets/eternal_starlight/eternal_starlight/books/main.json,49 section files and en_us1844 entries; no external guide dependency required. Parsed all recipe JSONs and nonrecipe advancement predicates, selected boss loot pools and book descriptions; javap -p -c of 16 key classes plus ESPortalBlock.Validator. This is a first pass, not proof every handler has been audited. All unqualified native item/block IDs in this document use eternal_starlight:.

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

## 6. Preliminary axis disposition and next pass

| Axis | Current evidence |
|---|---|
| gear | many native tiered sets, distinct special weapons |
| movement | Seeking Eye navigation, Wand of Teleportation, climbing food; actual movement mechanics next |
| ore_processing | alloy yields, smelt/blast |
| storage | chest boats, quiver; inventory details next |
| crafting | alloy/ether/drying/gear recipes |
| logistics | redstone signal routing; no generic storage network established |
| buffs | Numbness, local armor/accessories, food |
| external_compatibility | JEI/EMI class paths, common tags |
| mob_farming | breeding and animal resource loops |
| ore_generation | meteor ore and catalyst collection; exact semantics next |
| permanent_power | equipment-bound enhancements, no innate skill tree established |
| general_generation | meteor, plant/bird outputs, recipe conversion |
| direct_damage | ranged, melee, hammer crit, bosses, summons |
| block_breaking | native tiers and special tools |
| permanent_pets | tamed moth and summoned golems, owner rules next |
| temporary_pets | one-day hired Stranghoul |
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
| death_respawn | unresolved; Numbness not death prevention |
| trade_economy | Stranghoul Malarite barter, Gatekeeper trade class |
| progression_tiers | entry trial, gear, forge and garden material gates |

Current integration index has native mod pin plus TMM Alkahestry raw Aethersent and Deepsilver recipes, no directly named Eternal Starlight quest/config path found. Check those exact conversion JSONs and any global dimension/progression/ore policies in subsequent pass; do not assume live effect. Next pass: compiled boss configs and personal reward/spawner/rechallenge, portal geometry/return, Seeking Eye targets, alloy fuel/coolant heat/throughput, Catalyst exact operation, equipment/armor/accessories, enchantments, all meaningful companion/effect loops. Final pass: consolidate acquisition/61 criteria and actionable quests, remaining 29-axis dispositions and separate editorial evaluation. No score asserted yet. No game execution, changes to pack, new mod, balance or quest implementation.
