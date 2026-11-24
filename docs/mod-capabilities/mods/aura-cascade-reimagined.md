# Aura Cascade Reimagined — native capability Atlas, 0.2.1+1.21.1

Status: source-backed native survey; exact NeoForge release JAR reopened and selected compiled methods inspected on 2026-11-24. Research only, not TMM runtime acceptance. Prior comprehensive checkpoint: [#410 comment](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/410#issuecomment-7136682429). This dossier supersedes provisional notes where explicit evidence differs.

## Evidence and acquisition
Packwiz mods/aura-cascade-reimagined.pw.toml: CF 1519395/9005415, SHA-1 4cfde7f28e7d691fa771d91c23c79e8f2297e6a5; SHA-256 f2bc9d9c6b73e59004f5e582196f850f1e4bfc5bd0515a299a5201ead0605f0b (prior #425 provenance), 1,572,301 bytes. Uploaded ZIP CRC scan passed and SHA-1 independently matched. Namespace aura; Java 21, MC 1.21.1, NeoForge [21.1.252,21.2), Patchouli >=1.21.1-93-NEOFORGE. No JAR copied to Git. Exact archive: 196 ordinary recipes (176 shaped,20 shapeless) under data/aura/recipe; 44 processor and 13 vortex records under data/aura/recipes (custom catalogs, not extra vanilla recipes); 15 advancement JSONs, 6 enchantments, 44 loot table JSONs. 41 Patchouli entries, 371 pages under assets/aura/patchouli_books/encyclopedia_aura/en_us/entries. Source below refers to filenames in this directory and compiled pixlepix.auracascade classes. No assumption that Fabric source is identical.

## Native operations and resource scope

* aura_basics, aura_colors, node_variants, pumps_control: two White crystals from amethyst surrounded by eight gold nuggets; each adds 1000 Aura directly or dropped. Block-node per-color storage, distinct stored falling work power, no ordinary player mana. Straight cardinal links, prior checkpoint reports 15-block range; guide confirms obstruction, refresh within 200 ticks; every second eligible lower/level neighbors weighted (20-distance)^2, self weight 400, gradient >25. Regular powered node stops sending but receives; powered pump pauses. Conserving node same-height only. Capacitor thresholds 100/1000/10000/100000; checks every 19 ticks, five-tick outgoing burst, 110-tick incoming lockout. Black/Orange manipulators clear every 20 ticks and powered refill 100000 own color, retain power and transfer. Comparator node capacity, capacitor threshold, consumer stored work/base step; Monitor checks first adjacent pump or consumer (15 empty/no valid work, 0 fueled/valid), not aggregate storage.
* White ordinary; Black vertical zero weight, cannot pump, no falling power; Orange horizontal zero weight, induces current; Red captures TNT/Creeper pre-detonation for lift budget 200000/50000, source red reserve/rise limits, visual-only explosion; Yellow 2x ascent and decay; Green weight 2x day/0.5x night; Blue ascent 4x rain/0.5x dry; Violet grows in node, dissipates <=25, clears >2600. Aura moves rather than being generically consumed, but color passive rules change quantity. Pump eligible seconds pause if inhibited/no target.
* pump_variants: nominal one-block ascent speed Aura/sec and duration: burning 300, burn time/5; illumination torch 750/30s, glowstone 750/180s (consumed); Momentum 500/2s per block fallen; Projectile arrow 1000/20s, egg 500/90s, snowball 500/10s; redstone wire 1500/10s plus 40% duration each following wire, max 15, consumed. Alternating triple nominal rate varying 0–full over 20000 ticks. Color/height alters actual transfer. Each pump needs input branch; cannot pass sideways pump-to-pump.
* consumers_vortex, consumer_fieldwork: adjacent block work power; idle cycles spend power, drops refreshed within three blocks every 500 ticks. Work up to twice/sec; cumulative steps cost base*(2^steps-1) per update, reset each update; quarter remains near end per guide. Processor 60 steps/150 base; Prismatic 9/1000; Smelter 3/190; Grower 2/50; Fisher 200/200; Brewer 25/500; Colorer 12/50; Synthesizer 50/10000; Enchanter 1000/500. Smelter one eligible dropped input; grower 50 random ticks on block exactly two above; fisher 4x4 water one below x,z offsets -1..2, plain rod/no luck/no XP; brewer water bottle→awkward→random base→long/strong if supported; colorer shears-regrowth plus random color on sheep within radius two. Processor tagged ore→2 dust, prismatic 3 if matching dust tag; catalog wins; no output leaves item.
* arcane_progression: iron + wool→ingot (16 wool colors into eight aura colors). Vortex controller + four cardinal pedestals, each captures one dropped item, tracks falling-power receipts by color, white wildcard. Diamond 60000 White plus three matching ingots 20000 White each→gem. Eight distinct gems→Prism at ordinary Processor. Prism gates Prismatic Processor (wool→colorant, including blue lapis/brown cocoa/white bone meal/black ink). Synthesizer no input→tier1 Angelsteel. Three grounded same-tier ingots within three blocks combine to next, cap12. Fluxing Node node+two red ingots: 15 FE per power, rounded-up debit, once/sec, face-connected receiver group, 1–4 receivers, >=5 pauses. No player/item/chunk aura battery established; item crystals supply network; vortex receipts distinct from stored work.
* Vortex Ring of Binding: diamond block Blue 50000, obsidian White 50000, gold block Yellow 50000, redstone block Red 50000. Looter four Yellow gems, Spawner four Violet gems, Nether rite four Red gems, Enchanter four Black gems (see actual vortex catalog for per-pedestal costs; do not infer black falling power from black ingredient). Native guide quest_progression: 14 listed discovery/possession rewards, each once, item not consumed; opening encyclopedia claims. Do not conflate with FTB completion or freely duplicate powerful rewards.

## Storage
storage_network: distinct types × each: Basic 27×64; Dense 8×1000; Very Dense 4×5000; Super Dense 2×25000; Extremely Dense 1×100000; Light 100×32; Very Light 150×16; Super Light 1000×4; Extremely Light 10000×1; Mineral 8×100000; Mob and Farming 32×100000; Mod 64×100. Mineral common mineral tags, Mob listed drops/eggs, Farming crop/seed tags, Mod one non-Minecraft namespace. Books persist actual contents. Convert vanilla bookshelf using book, no separate shelf recipe. Clear line of sight Coordinator network, adjacent powered node. Draw examples 5/31/81 at 1/5/10 shelves; searchable 9×3 browser, component-aware grouping, amount/max retrieve, held item deposit, sneak empty first-stack withdrawal. Incomplete/unloaded/unreachable/unpowered disables actions, contents remain in books. Owner wants companion to book magic; allowed native portable storage, universal bag nesting remains pack policy. Arcane Archives inventory-retaining blocks not backpacks per owner; no special compatibility work merely for that.

## Gear and fairies
accessory_loadout: physical menu B; amulet, two rings, belt; right-use fills first empty matching slot, no swap. Ring binds fixed-role charm (consumed), 15 per ring, sneak-use releases charms. Role names: Fighter, Savior, Debuffer, Buffer, Shooter, Pusher, Stealer, Fetcher, Baiter, Breeder, Scarer, Extinguisher, Digger, Lighter, Glider, Training. Respectively damage, emergency damage, negative effects, positive effect, arrows, repel, held-item theft, dropped-item collection, farm animal generation, breeding, spawn suppression, fire/lava, mining speed, lights, fall distance, XP. Exact timing/multipliers from prior checkpoint remain provisional unless separately traced. No automatic Stealer nerf approved. Native slots not presumed Curios.
accessory_amulets/belt: Red blocks fire, Orange explosions, Green falls, Blue drowning; blocked hits heal except direct flame; Yellow halves projectile damage without heal, Violet blocks Wither without heal. Angel Wing active V, look level/up to ascend, steep down to clear standing place; Heels step up to two blocks while clear. Forbidden Fruit gives food/drink-specific gift on completed use, apples regeneration, potions/milk qualify; cancellation no gift. 
gear_utility/prismatic_wand: Barbarian combo hits 5–99 ticks apart, 5% compounding, max100, reset on faster/slower hit. Thief held when villager dies 1/4 chance first trade item. Transmuting Sword pairs cow/mooshroom, creeper/enderman, sheep/pig, ghast/blaze, slime/magma cube, ocelot/wolf; preserve name/current health. Mirror deflects Ghast/Blaze fireballs not Wither skulls. Black Hole consumes inventory cobble every five seconds. Dropped Red Hole explodes about every five seconds, fades ~25 minutes, retrieve to stop. Prismatic Wand selection two opposite corners, copy live source coordinates and player-relative offset, paste reads current source; max512 cells including air; all source/dest loaded/in bounds or abort; air-only destinations, matching survival block consumed after successful place, no machine data/contents/fluid resources or undo. 
AngelsteelToolHelper static initializer and AngelsteelTier record: displayed n=1..12, d=n-1; tier uses parameter 10, speed floor(5*1.15^d), material attack bonus floor(3*1.15^d), enchantability10, netherite incorrect-block tag, repair tag aura:angelsteel_ingots. Do not equate tier uses parameter with effective item max damage without checking item properties. randomBuffSet allocates 2d independent random points across four buffs, persisted custom data. angelsteel_tools: Efficiency 1.3^level, Shatter 3^level hardness>=2, Disintegrate 3^level hardness<=1, Fortune overrides lower existing level for eligible noncrop drops. Swords no mining gifts. 36 tool recipes and 72 sword recipes. AngelsteelSwordItem.hurtEnemy applies curse amplifier a=min(d,10); AngelsteelCurseMobEffect.curseDuration=100+100*a^2 ticks, cap10100 ticks at tiers11–12. Guide colors red fire, orange lift, yellow lightning, green spreading magic damage, blue below-half-health damage, violet swaps with nearby living beings. Exact pulse rates not claimed.
Kaleidoscopic: consumes one colored ingot per attempt even failure; odds decline with enchantment load. Individual red Silk Touch, orange speed, yellow Fortune, green connected logs, blue knockback, violet damage. enchant_matrix pairs: Yellow+Green up to25*pair connected growables except grass; Blue+Violet splash 2-block cube; Green+Blue heal floor(pair/2); Yellow+Blue ignite20*pair ticks (splash10*pair); Red+Violet incoming multiplier0.9^pair; Yellow+Violet extra killed-entity loot roll transient Looting (not nearby drops); Yellow+Red chance supported ore→two ingots; Red+Green speed /3^pair; Red+Blue recoil pair damage; Green+Violet outgoing damage minus pair floor0; Orange+Red ore, +Yellow stone, +Blue dirt/grass/gravel/sand, +Green logs multiply1.25^pair; Orange+Violet hardness>=3 speed1.5^pair; Orange pairs need harvest-capable tool.

## World machines and renewables
late_systems: Looter 100 work/5000 base, rolls dungeon chest table one item above; Spawner 15/190 natural monster local biome/structure; no player-kill attribution implied. Miner powered singularity, expires after100 ticks uncharged; redstone releases ore at >=21 charge, fortified enclosure required. LateGameWorldLogic.minerOreYield = 0 through20, floor(charge^1.5/50) above. Weighted base ore blocks coal46525, iron20665, copper8325, redstone6885, gold2970, lapis1285, diamond1265, emerald780, nether quartz19600. Additional unique item/block c:ores entries 1000 each; removes base duplicates and air. Effective TMM tag roster is an actual pack integration question: especially Draconium/Replica and other protected materials; do not infer gate bypass or silently nerf. Fortified blocks Processor End Stone + dirt/cobble/six traditional planks/glass/obsidian/stone. Nether and End rituals 100 work/5000 base; transform blocks/biome ~150 radius then remove self after queue, End leaves water. Traveler's Bricks contact motion >0.25 blocks/tick adds5 along 3D motion; Rebounding Enigma sets vertical10 blocks/tick, retains horizontal, no fall protection. Shattered Stone ring within 3 blocks each axis of blast center preserves nonterrain incl ores; terrain whitelist grass/stone/sandstone variants/sand/dirt/cobble/gravel destructible; wearer not protected.

## 29-axis crosswalk
| Axis | Source-grounded native capability |
|---|---|
| gear | Angelsteel, accessories, swords, tools above |
| movement | Wing, Heels, Traveler's Bricks, Rebound, violet swap |
| ore_processing | two/three dust, tagged inputs, yellow/red enchant |
| storage | 13 book variants, shelves, coordinator |
| crafting | Processor, Vortex, ingot compression, synthesis, brewing |
| logistics | aura links, book network, Fetcher |
| buffs | Buffer, food gifts, mining bonuses |
| external_compatibility | Fluxing Node FE, common ore/dust tags; Curios not assumed |
| mob_farming | Spawner, Looter independent loot, Baiter/Breeder |
| ore_generation | Miner weighted native+tagged table |
| permanent_power | no innate persistent learned ability established; gear/roles distinct |
| general_generation | Synthesizer, dungeon loot, fishing, dye, XP |
| direct_damage | swords, Fighter/Savior, enchant splash, Red Hole |
| block_breaking | mining bonuses, connected log/crop harvest |
| permanent_pets | bound persistent fairies, release charms |
| temporary_pets | no separate expiring summon family established |
| defense | protection amulets, Shattered Stone world protection, Red/Violet |
| healing_recovery | amulets, Fruit, Green/Blue |
| debuff_control | sword curses, Debuffer, Pusher |
| energy | colored aura, work power, receipt, FE conversion |
| automation_control | node gates, comparator, capacitor, Monitor, pumps |
| agriculture | Grower, Fisher, Colorer, Breeder, Baiter |
| detection_information | HUD/Monitor, coordinator search, guide |
| equipment_augmentation | six-color enchanting +15 pairs, randomized tool bonuses |
| world_manipulation | rituals, Wand, Extinguisher, lights |
| world_content | no new dimension/worldgen established; rituals change existing world |
| death_respawn | no resurrection mechanic established; Savior damage not death save |
| trade_economy | Thief trade-derived drop; no merchant system established |
| progression_tiers | crystal→circuit→ingots→gems→Prism and 12 actual Angelsteel tiers, independent utility branches |

## Quest-source map (research IDs, not FTB GUIDs)
Each row is a proposed lesson with observable success. Acquisition criteria can prove possession only; use manual instructional completion or separately implemented verified event detectors for actions. XP and modest consumable/material rewards suggested, no high-tier ingot/gem/Prism gate bypass by reward. Independent branches optional unless labeled core.
| ID | Prerequisites | Lesson / observation |
|---|---|---|
| AC01 | none | Core: craft encyclopedia and white crystals |
| AC02 | AC01 | Core: fill node, inspect colored reserve |
| AC03 | AC02 | Core: first descending circuit, work power receipt |
| AC04 | AC03 | Core: fuel pump, return aura uphill |
| AC05 | AC04 | Alternate illumination/momentum/projectile/wire pump |
| AC06 | AC03 | Redstone gate, monitor/comparator and blocked link |
| AC07 | AC03 | Capacitor, conserving/manipulator controls |
| AC08 | AC02 | Explore eight colors, day/weather/passive rules |
| AC09 | AC04 | Core: Processor consumes iron+wool, produces ingot |
| AC10 | AC09 | Smelter and ore/dust multiplication |
| AC11 | AC09 | Core: assemble controller and four pedestals |
| AC12 | AC11 | Core: four inputs and white power receipts produce gem |
| AC13 | AC12 | Core: eight gems to Prism in Processor |
| AC14 | AC13 | Prismatic processing/dye/ore multiplication |
| AC15 | AC09 | Fluxing Node exports FE to compatible receiver |
| AC16 | AC04 | Grower 50 random ticks, Fisher water footprint |
| AC17 | AC04 | Colorer sheep and Brewer potion |
| AC18 | AC13 | Synthesizer produces first Angelsteel |
| AC19 | AC18 | Grounded triple compression through tier12 |
| AC20 | AC18 | Craft mining tool and inspect persistent random roll |
| AC21 | AC18 | Craft colored sword and observe curse |
| AC22 | AC11 | Ring of Binding and four accessory slots |
| AC23 | AC22 | Bind typed charm then release; compare combat roles |
| AC24 | AC22 | Work/world/travel fairies, storage and XP effects |
| AC25 | AC09 | Protection amulets, Wing/Heels, food completion |
| AC26 | AC12 | Enchanter attempt consumes colored ingot |
| AC27 | AC26 | Pair matrix: beneficial and adverse combos |
| AC28 | none | Storage Book, actual deposit/retrieve |
| AC29 | AC28 | Compare dense/light/specialty filters |
| AC30 | AC28,AC03 | Convert shelves, coordinator search/retrieve |
| AC31 | AC13 | Wand select/copy/paste with real material |
| AC32 | AC09 | Mirror, special swords and portable holes; optional |
| AC33 | AC12 | Looter and Spawner distinct outputs |
| AC34 | AC09 | Fortified enclosure and Miner charged release |
| AC35 | AC12 | Nether and End ritual local transformation |
| AC36 | AC09 | Traveler's Bricks, Rebound, Shattered Stone |
All 36 identifiers unique and dependencies earlier in table. This is native teaching research, not quest implementation. Verify actual recipes for suggested acquisition ordering (not all optional prerequisites are hard gates).

## Handoff
Native broad source survey and quest map recorded. Remaining precision gaps: exhaustive per-recipe ingredient inventory (counts verified, not every row transcribed), exact sword curse pulse rates, all fairy numerical timing, exact per-pedestal advanced vortex costs, effective item max durability, precise ritual remapping and node link bound bytecode. These are source precision gaps, not routine gameplay QA. Separate concrete integration concern: Miner common ore tags versus pack gates; owner decision only if an actual conflict requires changing behavior. No runtime changes or builds, no new ordinary native gameplay QA. Atlas documentation writer owns this isolated PR; integration writer reviews and merges after applicable lightweight checks.