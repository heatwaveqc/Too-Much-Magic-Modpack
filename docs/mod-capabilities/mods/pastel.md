# Pastel 1.1.5.5 — native capability Atlas

**Native research COMPLETE (not gameplay acceptance).** Four exact-release passes and final source review, native progression through Forsaken Cathedral documented. No gameplay acceptance, no new design. Use Modonomicon and EMI for full recipe detail; representative teaching slices below are not authored FTB Quests. Do not unlock unfinished content. Writer: continuing Atlas chat, #410. Independent research-only dossier and index reconciliation.

## Evidence and boundaries
Owner-provided Pastel-1.1.5.5.jar, SHA1 53a582520b78aebd56c9be1fd041472047dca3d3 matches [Packwiz](../../../mods/pastel.pw.toml) CF1264556/7591337. CRC pass, 17054 entries, 2052 compiled classes; 2368 native recipe records (394 conditional), 758 advancements,1225 loot tables,404 Modonomicon entries/13 categories/23 multiblock definitions. Counts are packaged, not loaded or unique capabilities. [Pass 1](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/410#issuecomment-7170310397), [Pass 2](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/410#issuecomment-7170475000), [Pass 3](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/410#issuecomment-7170615663), [Pass 4](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/410#issuecomment-7171163705). Exact recipe/advancement/loot JSON and en_us guide locale, targeted javap. No upstream source parity claim, JAR redistribution or runtime verification. All registry IDs in referenced recipes use pastel namespace unless explicitly identified otherwise. Guide categories general, ink, equipment, magical_blocks, enchanting, creating_life, brewing, cuisine, pastel_network, dimension, decoration, mod_integration, about.

## Native entry and four crafting tiers
Root pastel:pastel can be triggered by starter shards or several exploration discoveries; book four gemstone shards around ordinary book, hidden pages open on actual advancement. Cyan topaz/matter hills, magenta amethyst/time deep, yellow citrine/energy upper/middle. Basic pedestal three same shards, four tuff, two planks; paintbrush stick/copper/wool. Anvil crushing shard→2 powder, buds→4/6/8, cluster→16. Craft on pedestal using powder and brush (or redstone), open space/inventory above. Actual craft and all three shards open CMY, 11×11×4 focus; native progression includes craft or interact with existing upgraded pedestal. Fading consumes organics to Vegetal; craft colored saplings, harvest leaves with hand/hoe, crush leaves for pigment. Mix colors. Collect 11 non-brown basic pigments and construct 9×9×5 Fusion Shrine with sky access; topaz/amethyst/citrine + lava, 480t, midnight/new moon→Onyx; 13×13×6 advanced pedestal. Neolith and Fusion Shrine during day/clear sky produce Midnight Aberration; isolate it. Collect Midnight Chip; failing and Ruin organisms; Decay Away arrests spread. Bottle of Ruin then default converted Ruin broken or Bedrock Dust possession unlocks next gate; actual dimension entry necessary. Moonstone geodes and Moonstone pedestal, 13×13×7 initial structure then 28 chiseled Moonstone blocks upgrade to full complex structure. Recipe gating is personal advancement, not only possession. Native ordinary JEI recipes not transcribed. Use named Modonomicon structure pages for exact block layout.

## Material and Ink economy
Liquid Crystal heals; water interaction Frostbite Crystal, lava Blazing Crystal, Humus clay. Midnight Solution damages life, corrupts certain items and disenchants to XP. 16 independent colors, not FE/fluids. Color Picker after midgame converts dye/pigment/block to 5/100/900 recipe units, irreversible; 2 slots,6t base,26,214,400 raw internal capacity. Equalization transfer; display vs raw scaling not assumed. Flask 409,600 one color; Assortment 6,400/color clover gate; Pigment Palette 409,600/color player-only drain, liquid crystal/Azurite/three trinkets gate. Creative Ink Assortment excluded under #596; Artist's Palette 1,677,721,600 raw constructor capacity but WIP-gated. Knowledge Gem 10,000 XP, Efficiency capacity/Quick Charge rate, chest collection, Enchanter and Cinderhearth. Pedestal and shrine use recipe powder, fluids and XP separately from Ink.

## Industry, farms and logistics
Crystal Apothecary advanced pedestal Storm Stone, 800t/8 yellow powder/4 recipe XP; 27 slots, harvest mature clusters radius12. Compensates elapsed game ticks >1200 from current nearby budding blocks/growth faces; native geode definitions 2 shards per face per 10k ticks. Not wall clock while server stopped; no explicit elapsed cap seen, inventory/overlap caveats. Crystallarieum complex pedestal, Bismuth Crystal/Liquid Crystal/pigment blocks, 1200t/8 cyan/16 magenta/4 white; two slots catalyst/provider, 409,600 internal Ink. Throw starter seed and catalyst, provide correct Ink; small→large→cluster. Seventeen native growth entries (pass2 table); raw metals seed, crystalline resource copies; base cost per second 2^(tier-1), catalyst speed/cost multiplier and probabilistic consumption. Diamond cyan tier3 480s/stage, coal8x/0.25 cost/20% consumption per second, coal block10x/0.25/2%, charcoal16x/0.25/100%. Normal diamond cluster 3–5 pure diamonds; Silk Touch cluster; smaller buds Silk Touch only. Netherite scrap brown tier3 960s/stage. No native Replica/Draconium growth. Bismuth cluster drops3–5 crystals, crush flakes to reseed. Bootstrap BEFORE Crystallarieum: find Bismuth buds in Deeper Down, place near sacrificial beacon-base blocks; BismuthBudBlock.randomTick samples five positions within ±3 blocks and converts a matching beacon-base block to cobblestone per successful stage. Harvest crystal, then craft Crystallarieum; never teach circular dependence. Malachite white tier4 300s/stage, requires complex pedestal. Cinderhearth advanced pedestal Stratine, 600t; multiblock 21 polished,6 crest,5 glasslike,2 chiseled,6 lava buckets; orange Ink, input/provider/Knowledge Gem/8 outputs, raw cap51,200, base8/t; upgrades speed magenta, yield light blue, XP purple, efficiency black. Pure ingot/diamond/coal/netherite equivalents →2; pure echo2+25%, lapis/glowstone4+50%, quartz/prismarine/redstone4+50% extra. Ancient debris1+50%, coal ore4+2% diamond. Exact recipe distributions in pass2. 
Network after Azurite/Neolith and advanced pedestal: Sender pushes Gather>Buffer>Storage; Gather requests Buffer>Sender>Provider>Storage; Buffer requests Sender>Provider>Storage and serves Gather; Storage provides/accepts filtered; Provider offers without pushing; Connection extends. Link12 no sight, default1 item/30t per node, native IItemHandler. Tuning Stamp link/unlink, sneak clears; color after Pure Azurite/network milestone; Paintbrush. Default redstone disconnect, filters5 slots/row up to25, vellum tags. Azurite expands rows, Malachite latency, Bloodstone batch, Resonance rate, Shimmerstone glow, redstone always on, coal invert, echo transfer pulse, lapis off, quartz single-pulse, glowstone lamp. No virtual terminal/cross-dimension reach inferred. Black Hole Chest collect radius12 filters (solid block above blocks collection), Knowledge Gem XP; Compacting Chest 1/2/3 crafting sizes; Fabrication Chest four tablets maintains recipe stock; Ender Hopper/Dropper insert/extract owner's Ender Chest while online; Heartbound 54 slots owner-only open. Block Breaker iron tier, directional Placer, detector reference, weather rain8/thunder15, wireless Redstone Transceiver 16/color. Preservation claims, cross-mod nested containers and resource loops separate. 
Agriculture: colored trees renewable pigments; Doomblooms harvest gunpowder with care (Silk Touch/shears safe removal), Nightdew and sleep-associated buds, Weeping Gala milk resin, amaranth, fissure plum, Jadeite petals, Nephrite fruit, sawblade holly, blood orchids, fish. Nature staff Vegetal plant growth/lure. Idols feed/milk/shear radius6, fertilize, smelt blocks, freeze, silverfish, slimes, mob effects and barter. No economy rebalancing.

## Enchanting and gear
Enchanter advanced pedestal Stratine/entered Liquid Crystal,400t/4 magenta/2 black/2 recipe XP; 11×11×5,8 bowls,4+ liquid crystal buckets,4 gemstone blocks,8 lights,12 pillars,24 slabs,159 polished. Deterministic create, apply without consuming book, copy, upgrade, Gilded Book extraction; guide apply50–200 XP/enchant varying. 71 recipes/51 upgrade records including conditional. Canvas swaps item-bound enchant sets; Midnight Solution resets/disenchants. Indestructible 1000XP/1000t,4 blue pigment,2 downstone fragments,2 pure netherite scrap. Resonance 800XP/400t,4 white pigment,2 Paltaeria gems,2 resonance shards, Hummingstone Hymn/complex gate; alternative found resonant pick unlock. Resonance preserves budding blocks/spawners/reinforced deepslate/shriekers/brushable block data, pure ores; exclusive Silk/Fortune/Pest Control. Native 20 enchants: Big Catch3, Clover's Favor3, Voiding1, Disarming2, Exuberance5, First Strike2, Foundry1, Improved Critical2, Indestructible1, Inertia3, Inexorable1, Inventory Insertion1, Pest Control1, Razing3, Resonance1, Serendipity Reel2, Sniping2, Steadfast1, Tight Grip2, Treasure Hunter3. Treasure Hunter heads vs Looting; Foundry smelts; Steadfast item survives lava/void. Enchanted apple 100XP/600t apple+4 shimmerstone blocks+4 pure gold blocks. Overchanting/conflict bypass actual milestone checks WIP. Bedrock indestructible gear separate native above-max enchantments (Efficiency VI, Sharpness VI, Fortune IV, Power VI, protection V variants); gemstone armor brittle/shard repair; Malachite tools Fusion Shrine after growth, Glass Crest socket Moonstone Core powers ranged mining/ground slam/charged bow/bident air dash/echo projectile. Azure Dike shared regenerating barrier; Blue Ink infusion via Color Picker has exponentially increasing cost. Ring (pastel:azure_dike_ring) grants 6 units and 2x recharge speed; Belt (pastel:azure_dike_belt) grants 8 and 2.5x post-hit recovery modifier. Both require Liquid Crystal and Azurite milestones, Fusion Shrine 600t/2 recipe XP, base ring/belt +8 raw azurite+16 blue pigment in Liquid Crystal. Core (pastel:azuresque_dike_core) requires Aether Vestiges, Moonstone Core, Downstone Fragments, substantial shield charge; Shrine 1200t/20 XP, 4 fragments+vestiges+core+16 pure azurite+16 blue pigment, Liquid Crystal. Core doubles other providers (no shield alone), 1.5x charge and recovery modifiers; tooltip warns doubles unabsorbed damage. Puff Circlet4, projectile/fall protection. Cotton Cloud Boots sprint air support, Seven League speed/step, Take Off Belt charged sneak jump, Weeping Circlet underwater breathing/vision/mining, Heartsinger pink HP, Laurels purple stealth, Pursuit magenta mining, Dawn's Grasp light blue reach, Totem Pendant single-use, Arrogance Ascension risk. Dreamflayer relative armor and red Ink empowerment; Bismuth Crystal, Moonstruck Nectar, light gray pigment and Ink gates; Shrine 1200t/16 XP, netherite sword+4 Bismuth+nectar+4 each red/light blue/light gray/black pigment in Midnight Solution, full moon/night. Draconic Twinsword throw/rebound/split daggers; Bloodstone and wireless-redstone ruin milestone; Shrine 1200t/16 XP, 2 netherite swords+4 raw Bloodstone+8 gold+8 blood orchid petals+Sniping II book+16 yellow pigment in Liquid Crystal. Nightfall's Blade Potion Workshop fills effects; complex pedestal, pyrite, Quitoxic Reeds, Frostbite Crystal, pure resource and Ink gates; Shrine 1200t/16 XP, iron sword+4 pyrite chunks+2 pure quartz+8 frostbite essence+4 each red/purple pigment in Midnight Solution. Constructor mass place, Exchanging connected swaps, Nature growth/lure, Radiance invisible lights, Ender Splice bind/return with cross-dim enchant, Omni Accelerator projectile/remote placement (not time acceleration). No unconditional creative flight assumed.

## Creature systems
Spirit Instiller advanced pedestal Azurite/Midnight Chip,1200t/1 black/2 recipe XP; 8×9×5, two bowls,2 notched+2 chiseled,2 Onyx chiseled upgrades,6 pillars,17 slabs,41 polished,2 chimes. Treasure Hunter head+structure or staff gate. 197 standard records incl123 ordinary memory variants and46 head fusions; head+favorite item+4 Vegetal/Neolith/conditional Malum mnemonic fragment typically800t/4 recipe XP. Place Memory; random manifestation steps base1/magma3/blazing8/ice0. Head fusion variants axolotl/parrot/shulker. Staff of Remembrance complex tier, Moonstone Core/Jadeite petals/dragonbone, removes target/drops adult Memory; compiled hostile branch without milestone falls to ordinary 1000 raw light gray Ink rather than rejection; hostile unlocked/creative10000; explicit boss/unsupported/warden blacklist and alive/nonvehicle conditions. No claims that TMM allows all capture. Boss recipes WIP. Wooly Pig four farm-animal heads +16 Vegetal +Midnight Solution in Shrine1200t/16XP, then memory; eggs, milk, shear. Kindling memory dragonbone chunks/food/binder; feathers→raw Bloodstone via Liquid Crystal. Budding Onyx spirit recipe Midnight Aberration/Moonstruck Nectar/Onyx. Idols 43 guide actions: step/use/projectile activates creature-specific effect, including Vex summon (ownership/lifetime not asserted), barter, feed/milk/shear6, echolocation8, teleport16, smelt/freeze/attack and status effects. Everpromise Ribbon binds/names/tames eligible pets; death drops Broken Promise (guide confirms memorial, revival not asserted). Find ribbon in city Preservation Chest, then unlock recipe with complex pedestal, Jadeite and grown Malachite; 800t/2 XP, 4 petals/3 string/1 pure Malachite and 1 each CMY +8 white powder. Native hardcore revival recipe personal player head, mode-specific; don't claim ordinary player resurrection. 
Spawner harvest actual Resonance event unlocks modification; existing spawner center,4 upgrade+4 Vegetal,200t/0XP. Neolith count4→16, Mermaid Gem nearby6→40, Stratine range16→64, Midnight Chip delay200/800→20/40t (exponent .98). Downstone fragments and skull for type change, rewrites SpawnData/Potentials. TMM Multiplayer Spawners and creature policy separate; no new allowance.

## Alchemy and cuisine
Potion Workshop gate Mermaid Gem + Stratine + Quitoxic Reeds; advanced pedestal1200t/4 magenta/4 yellow/1 black/4 recipe XP. Mermaid Gem fuel, empty bottles accepted; 49 brewing/49 reacting/30 crafting records incl conditional. Reagent order independent, unique, flat then percent; Vegetal +200% duration/-25% potency, citrine +25%, onyx +2 yield, moonstone negative→positive, shimmerstone +20% both, Paltaeria random positive, pure echo previous main effect, bedrock dust +50% potency/-75% duration/-1 yield, bloodstone +100% potency/-2 yield/slower drink. Gunpowder splash, dragon breath lingering or tipped arrows; native effects including calming, somnolence, magic annulation, projectile rebound, lava gliding, ascension, divinity, life drain. Actual advancement amplifier≥3, duration≥24000t, effect count≥4. Lesser Pendant one potion up to III, greater three up to I, Ink upkeep; Nightfall Blade. Non-potion crafting water/lava, fluids, bone meal, XP bottles10XP, Dragon Breath300XP/3 bottles + popped chorus + raw bloodstone (gated). 
Titration Barrel basic pigment/amethyst gate 160t/4 magenta/.5XP, six colored planks/two iron/pigment. 64 total items/1 bucket,5 slots; 67 ordinary +5 dynamic recipes (aqua regia/cheong/jade wine/nectered viognier/suspicious brew). Time, heat/humidity/water share affect fermentation. Example 1 milky resin+4 sugar→16 clotted cream after 1 game hour. Native optional age confirmation for alcoholic beverage recipes, not progression mandatory; nonalcoholic food/tea/cookbook branches. TMM food tags/dragon diets downstream.

## Dimension and narrative finale
Ruin converts bedrock; onRemove converted→air at Overworld minY creates downward portal. Nether top/bottom paths also implemented; Deeper Down ceiling upward portal. Apply Bedrock Dust to portal closes it as Bedrock. Dimension pastel:deeper_down minY -320,height256, no skylight, ceiling, bed works, no raids/anchors, coordinate scale1. Deep Barrens ores; Deep Dripstone giant caves; Howling Spires ash/Varia; Dragonrot Swamp toxic atmosphere; Black Langast Midnight Solution; Crystal Gardens liquid crystal/hummingstone; Razor Edge damaging terrain; Noxshroom Forest fungal trees. Preservation Turrets hostile; Erasers pack spiders; Doomblooms explosive, primordial fire. Explore Excavation Sites for Downstone Fragments; Hummingstone Hymn yields Resonance shards; Moonstone geodes; Bismuth/Malachite/Paltaeria gates; Forgotten City gives Moonstone Core progression. Undergrowth Manor/Poisoner's Handbook and sleeping inhabitant optional lore. Collect enough hints actual min5 of 8 plus city prerequisite: cracked dragonbone, all biomes, revived Hornslake, excavation, city, floor, hymn, Moonstone. Endgame requires complete complex pedestal, hints and found Mysterious Locket. Preservation chest locket; Fusion Shrine1200t/16 recipe XP/16 white pigment+Moonstone Core in Liquid Crystal sockets it; use yields Mysterious Compass pointing to forsaken_cathedral tag. Actual structure location after compass triggers finish_progression. Native end, no final boss claimed.
 
## Unavailable gates and pack interactions
Eight definitions require missing pastel:__wip: Overchanting, conflicted enchanting, hostile memorizing (compiled fallthrough above), boss Memories, Ominous Sapling, spectral shards, Bottle of Forfeiture, Artist's Palette. No natural acquisition taught. Existing #711 rejected cross-mod recipes, #330 flight, #328 enchanting, #596 creative objects, food/claims/capture policies downstream; don't fix during Atlas. Native release assumed working except exact documented contradictions. Material uncertainties: absolute Ink display scale, precise compensation overlap, all individual potion coefficients and pet revival implementation, Vex control/lifetime; not broad native QA requirements. Final review confirmed representative gear acquisition and native endgame criteria; individual optional equipment details remain available through exact advancement/recipe and guide references. No novel design or tests.

## 29-axis navigation
| Axis | Finding |
|---|---|
| gear | Gemstone→Bedrock/Malachite→Glass Crest; curio and legendary branches above |
| movement | Cloud boots, bident dash, Ender Splice, portal, speed/step, underwater |
| ore_processing | Anvil crushing, Cinderhearth, pure ores and Foundry |
| storage | Chests, Knowledge Gem, Ink containers and Ender inventory |
| crafting | Pedestals, Fusion Shrine, Workshop, Spirit Instiller, Enchanter, barrel |
| logistics | Six-role Pastel Network, Ender devices, filters and upgrades |
| buffs | Potion Workshop, pendants, armor, idols |
| external_compatibility | Conditional recipes, Malum binder, IItemHandler, Curios, #711 |
| mob_farming | Memories, spawners, idols; capture restrictions |
| ore_generation | Apothecary compensation, Crystallarieum seeded growth |
| permanent_power | Persistent discovery and unlock milestones; no independent innate stat ladder asserted |
| general_generation | Pigments, crystal products, microorganisms, renewable materials |
| direct_damage | Weapons, blade potion infusions, primordial fire, idol projectiles |
| block_breaking | Resonance, Exchanging, Glass Crest mining, Block Breaker |
| permanent_pets | Memories, Wooly Pig, Kindling, Everpromise Ribbon |
| temporary_pets | Idol Vex summon present; control/lifetime unverified |
| defense | Azure Dike, immunity, armor, rebound, resistance |
| healing_recovery | Liquid Crystal, restorative foods, regeneration, idols |
| debuff_control | Calming, slumber, magic annulation, disarm, poison |
| energy | 16 Ink colors, powder/XP/fluids, item and block stores, wearer |
| automation_control | Redstone nodes, detectors, Fabrication Chest, transceiver |
| agriculture | Colored trees, creature products, crops, Nature staff, idols |
| detection_information | Modonomicon hints, echolocation, block/weather detection, compass |
| equipment_augmentation | Enchanter, Canvas, Ink infusion, Moonstone Core socket |
| world_manipulation | Decay, block placing, light, liquid reactions, idols |
| world_content | Deeper Down eight biomes, ruins, geodes, native creatures |
| death_respawn | Totem Pendant, Everpromise memorial, hardcore-only revival |
| trade_economy | Gold-barter idol; no independent currency/shop ladder asserted |
| progression_tiers | Basic→CMY→Onyx→Moonstone→Cathedral |
 
## Representative teaching map
Core progression and selected optional demonstrations. Dependencies are instructional ordering, not invented hard gates. Use native advancement triggers where verified, inventory only as possession proxy, manual/observation for actions without supported triggers. Exact detailed construction remains native guide. No FTB IDs or rewards authored.
| ID | Prior lessons | Route | Teach / observable success |
|---|---|---|---|
| PA-01 | — | Core | Discover first primary shard and access guide; actual root unlock |
| PA-02 | PA-01 | Core | Collect all three primary shards; native discovery milestones |
| PA-03 | PA-01 | Core | Craft basic pedestal and brush; crush shard to powder |
| PA-04 | PA-03 | Core | Run first actual pedestal recipe |
| PA-05 | PA-02, PA-04 | Core | Upgrade CMY pedestal; complete basic focus |
| PA-06 | PA-05 | Core | Create Fading, collect Vegetal; control decay |
| PA-07 | PA-06 | Core | Make colored sapling, harvest/crush leaves into pigment |
| PA-08 | PA-07 | Core | Mix and collect eleven basic non-brown pigments |
| PA-09 | PA-08 | Core | Build Fusion Shrine; demonstrate lunar Onyx recipe |
| PA-10 | PA-09 | Core | Complete advanced Onyx pedestal; dark color unlock |
| PA-11 | PA-07 | Optional | Titration Barrel; tap fermented food |
| PA-12 | PA-11 | Optional | Fermentation and cookbook/tea branches |
| PA-13 | PA-10 | Optional | Discover Liquid Crystal and contact reactions |
| PA-14 | PA-10 | Optional | Unlock Ink and convert pigment in Color Picker |
| PA-15 | PA-14 | Optional | Fill Flask/Assortment; transfer color without conversion back |
| PA-16 | PA-10 | Core | Neolith and failed daytime fusion; isolate Midnight Aberration |
| PA-17 | PA-16 | Core | Collect Midnight Chip; craft bottle of Ruin and Decay Away |
| PA-18 | PA-17 | Core | Decay lowest bedrock, break converted Ruin, open portal |
| PA-19 | PA-18 | Core | Enter Deeper Down, confirm native dimension trigger |
| PA-20 | PA-19 | Core | Collect Moonstone from geode |
| PA-21 | PA-20 | Core | Craft Moonstone pedestal; build initial and upgraded complex focus |
| PA-22 | PA-10 | Optional | Find Storm Stone and run Crystal Apothecary in geode |
| PA-23 | PA-10, PA-13 | Optional | Collect Stratine, build Cinderhearth and smelt an ore |
| PA-24 | PA-23, PA-14 | Optional | Use four color upgrade channels and Knowledge Gem |
| PA-25 | PA-10, PA-13 | Optional | Build Enchanter, manufacture enchanted book |
| PA-26 | PA-25 | Optional | Apply, copy and upgrade a reachable enchant; reusable book |
| PA-27 | PA-25 | Optional | Treasure Hunter head and Knowledge Gem XP workflow |
| PA-28 | PA-10 | Optional | Mermaid's Gem/Stratine/Quitoxic Reeds→Potion Workshop |
| PA-29 | PA-28 | Optional | Brew actual potion, add unique reagents |
| PA-30 | PA-29, PA-14 | Optional | Fill Lesser or Greater Pendant and observe sustained effect |
| PA-31 | PA-29 | Optional | Manufacture non-potion item; XP bottle from Knowledge Gem |
| PA-32 | PA-16, PA-10 | Optional | Collect Azurite, craft network nodes and Tuning Stamp |
| PA-33 | PA-32 | Optional | Connect Sender to Gather and observe requested delivery |
| PA-34 | PA-33 | Optional | Add Provider, Buffer, Storage and relay; tag filter/redstone |
| PA-35 | PA-14, PA-21, PA-34 | Optional | Pure Azurite and network milestone: color separation, upgrade nodes |
| PA-36 | PA-14 | Optional | Magical chests and Fabrication Chest; produce maintained stock |
| PA-37 | PA-36 | Optional | Block Breaker, Placer, detector and transceiver |
| PA-38 | PA-19 | Core | Explore Forgotten City; native structure milestone |
| PA-39 | PA-19 | Optional | Excavation site and Downstone Fragments |
| PA-40 | PA-19 | Optional | Hummingstone Hymn and Resonance Shards |
| PA-41 | PA-19 | Optional | Eight native biomes; hazards and resources |
| PA-42 | PA-19 | Optional | Explore manor, read Poisoner's Handbook and lore |
| PA-43 | PA-21, PA-14 | Optional | Find Bismuth buds, grow using nearby sacrificial beacon blocks before Crystallarieum; then craft machine and grow crystal |
| PA-44 | PA-43 | Optional | Compare catalysts, harvest pure mineral cluster and reseed |
| PA-45 | PA-44, PA-23 | Optional | Refine pure material in Cinderhearth |
| PA-46 | PA-21, PA-40, PA-25 | Optional | Paltaeria and Resonance book; harvest special block |
| PA-47 | PA-21, PA-43 | Optional | Grow Malachite, create tool in Fusion Shrine |
| PA-48 | PA-38, PA-47 | Optional | Collect Moonstone Core; socket Glass Crest tool, demonstrate action |
| PA-49 | PA-10, PA-27, PA-16 | Optional | Build Spirit Instiller; create and manifest ordinary Memory |
| PA-50 | PA-49 | Optional | Head fusion and idols; demonstrate creature-linked action |
| PA-51 | PA-49 | Optional | Reconstruct Wooly Pig or Kindling; obtain useful product |
| PA-52 | PA-49, PA-46, PA-39 | Optional | Harvest and modify existing spawner; no boss bypass |
| PA-53 | PA-21, PA-49 | Optional | Staff of Remembrance eligible capture, placement and blacklist boundary |
| PA-54 | PA-14, PA-21 | Optional | Ink-infused gear and Azure Dike protection; no creative flight claim |
| PA-55 | PA-21, PA-29, PA-43, PA-45, PA-51 | Optional | Legendary weapon branch; follow each recipe unlock (including wireless redstone ruin for Twinsword) and demonstrate activation |
| PA-56 | PA-21 | Optional | Bedrock equipment and native pre-enchanted properties |
| PA-57 | PA-19, PA-38 | Core | Complete any five of eight hint advancements, city mandatory |
| PA-58 | PA-21, PA-38, PA-57 | Core | Find Locket in Preservation Chest, enter native endgame |
| PA-59 | PA-58 | Core | Collect Moonstone Core, socket locket in Shrine and use it |
| PA-60 | PA-59 | Core | Follow Compass to Forsaken Cathedral; finish_progression location event |
