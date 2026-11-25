# Forbidden & Arcanus 2.6.1 — native capability Atlas

**Status: native research COMPLETE at owner-approved quest-source depth.** This is a released-mod evidence dossier, not actual FTB quests or pack gameplay acceptance. Scope Minecraft 1.21.1 NeoForge. No runtime, balance, recipes, mod pins or builds changed.

## Evidence and version
[Packwiz pin](../../../mods/forbidden-arcanus.pw.toml) forbidden_arcanus-2.6.1.jar, Modrinth MdlnLS7Q/fNjxgZPH; owner-uploaded exact release 3,402,083 bytes; SHA512 012a1d5c779a0173a5c76ea79f429486753f040dfe59739bc45fd0817182d705a80110032f024757f0ac51a9bd10c546a11a730899aa9adbf7251a5040472724 matches. 2,976 unique ZIP entries, CRC all pass, 480 class files, 288 English localization records; metadata requires NeoForge >=21.1 and Valhelsia Core >=1.1.4 (source build version is not dependency minimum); ARR, no binary/assets redistributed. 188 singular recipe records: 114 shaped,29 shapeless,15 combustion,14 stonecutting,6 smelting,6 modifier smithing,3 blasting,1 smithing transform. 188 recipe advancement records are not 188 gameplay milestones. 153 loot tables,14 loot modifiers,20 worldgen JSON,7 biome modifiers, one enchantment, 53 custom data JSON (22 rituals,7 enhancers,11 residues,6 item modifiers,3 magic circles,3 knowledge nodes,1 constellation). All category paths inspected, not claim every class decompiled. Exact data under data/forbidden_arcanus; class prefix com.stal111.forbidden_arcanus. Direct javap -p -c on RitualManager, ItemModifier, BlackHoleBlockEntity, QuantumInjectorBlockEntity, PedestalBlockEntity, ModItems, FAArmorMaterials, UpwindBlock, MagicWandItem, SpectralEyeAmuletItem, EntityEvents, PlayerEvents, FAVillagerTrades, modifier and loot classes, PlayerMixin, FerrogneticMixtureItem, ObsidianSkullItem and skull tick functions, other targeted classes. [Version-declared source f62af610d550e0d033f6c5cd166e40062638c44b](https://github.com/stal111/Forbidden-Arcanus/tree/f62af610d550e0d033f6c5cd166e40062638c44b) supplies complementary implementation; selected data/mechanics match exact binary inspection, not wholesale byte-for-byte source-build equivalence. [Historical Comprehension](../../mod-comprehension/mods/forbidden-arcanus.md), [pass 1](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/410#issuecomment-7142931654), [pass 2](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/410#issuecomment-7143731195), [addendum](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/410#issuecomment-7143756262). No normal native regression test requirement. Effective TMM config/loot/recipe changes must be checked by downstream author.

## Material discovery and renewable loops
Mine Overworld Arcane Crystal Ore (native 3 placement attempts, trapezoid Y-40..14), Runic Stone (3 attempts uniform -64..2), Darkstone, Stella Arcanum (2 attempts uniform -44..42). Lower runic placement definition exists but do not assume biome injection. Stella Arcanum drops Stellarite Piece or itself with Silk Touch; compiled/source playerDestroy explodes non-Silk mining; radius3/block damage true native defaults, explode toggle not checked by inspected method. Mine carefully; do not equate ore with Eternal Stella. Crystal -> dust (smelting/blasting/Clibano); runic stone -> Rune. Mundabitur Dust = crystal dust + redstone + blaze powder + bone meal + phantom membrane + gunpowder ->4. Deorum = 4 charcoal,2 crystal dust,2 Mundabitur,1 gold. Arcane Polished Darkstone = eight polished around Deorum ->8; make chiseled arcane from slabs; gild chiseled polished with Deorum Nugget. Obsidiansteel from Clibano with Artisan Relic (below); Corrupti Dust = Obsidiansteel + blaze powder + nether wart + crystal dust + Ender Pearl Fragment ->4. Fragments from Endermen 0–2 plus Looting; Bat Wings 0–1 plus Looting; Squid Tentacles (70% modifier then 0–1 + Looting); Dragon Scales dragon loot 1–3 + Looting or End City treasure chest 56% 1–2. Spawner Scrap one from ordinary non-Silk spawner loot. In TMM capture and spawner rules can alter access: do not recommend dismantling protected/valuable spawners without warning.

Lost Soul Overworld biome modifier weight35 groups1–3; flying14 HP, flees when hurt. Soul Looting sword enchant I–III +5% spawn chance per level, weapon death hook then entity tags select ordinary/corrupt, 4% of eligible successful spawns enchanted. Loot one corresponding soul. Ordinary Lost Soul with Corrupti Dust -> Corrupt Lost Soul; Aureal Bottle -> Enchanted Lost Soul; corrupt + bottle -> ordinary; these direct main-hand interactions do not explicitly consume reagent in inspected source; no economy exploit promised. Splash also converts nearby souls. Corrupt Soul on oak sapling or dead bush consumes one and makes Growing Edelwood. Dirt, air above, natural brightness>=9, bonemeal45%; 2–4-high logs with carved top, possible branches. No active Edelwood biome addition in exact seven modifiers despite placed feature. Native trader alternative: Growing Edelwood6 emeralds, Fungyss3, Aurum Sapling5, Yellow Orchid2, Artisan Relic18 (native injected generic trade list, TMM redesign separate). Aurum/Yellow Orchid Flower Forest; Aurum leaves can supply nuggets. Edelwood logs 2.5% oily per eligible random tick; glass bottle on oily log -> oil and reset, crafting remainder bottle. Axe carves vertical log, shears/bonemeal toggle decorative leaves, rain may waterlog hollow logs. Branch drops stick unless Silk Touch. Planks -> Utrem Jar, ladder, boat, bucket, decorations; bucket five planks, capacity-bearing water/lava/milk/powder snow family (no universal modded fluid claim). Dark Matter smelt regular Edelwood Log 400 ticks. Arcane Bone Meal four bone meal around dust ->4; use on farmland -> Magical Farmland (preserves moisture); use to grow supported plants. Native loot modifier doubles eligible crop drops on this soil, checks maturity for CropBlock and block/item deny tags; not a blanket bonus to all custom crops. Bat Soup and Cooked Tentacle ordinary food; native TMM diet integration separate.

## Clibano
Make core (eight Darkstone around Blast Furnace). 3x3x3: P=Polished Darkstone, B=Polished Darkstone Bricks, C=Core, A=air. Top and bottom PBP/BBB/PBP; middle BBB/BAB/BCB with core facing front, totals 8P,17B,1C. Use Mundabitur Dust on horizontal core face, transforms structure. Open core GUI. Two input/output lanes, separate ordinary fuel, fire item, one enhancer. 15 exact combustion records: crystal ore->crystal, crystal->dust, runic stone->rune, coal ore->coal, iron/copper/gold ore or raw->ingot, lapis/diamond/emerald ores->gem, ancient debris->scrap, raw iron+obsidian->Obsidiansteel with Artisan Relic. All output1; base100 ticks except dust80. No generalized doubling. Soul/Corrupt Soul -> Soul Fire 1.5x, Enchanted Soul -> Enchanted Fire 2.5x, ordinary 1x. Fire item consumed when fuel/recipe can start, timer2700 ticks even idle; Crimson Stone duration1.3x. All 15 data recipes allow normal fire except any external changes; enhanced fire enables residue only, same chance both modes. Residue 33% crystal ore, coal, iron/copper ore/raw and Obsidiansteel (COPPER residue); 20% gold and lapis;10% dust/rune/diamond/emerald;5% ancient debris. Shared cap64; 9 same residue -> full block (ten types crystal/rune/coal/iron/gold/copper/lapis/diamond/emerald/netherite); Deorum1 ->2 nuggets, no native recipe emits it. Auto deposits into compatible/nonfull or empty output; blocked output retains residue and full store loses new. Two independent lanes or paired recipe consumes both. Item handler top input, horizontal fuel/soul, bottom outputs; second result slot extraction precedence permits other sides in source. Use actual pipes only downstream, no generic transport network promised. Gavel also has native 30% loot modifier for eligible ore drops, tagged gavel and no Silk Touch; not all ores/other loot.

## Forge setup, sources and automation
9x9 floor, P polished Darkstone48, A gilded chiseled polished Darkstone9, C chiseled ARCANE polished Darkstone4, * unconstrained. Rows north to south:
```
***PPP***
*PPPAPPP*
*PAPPPAP*
PPPPCPPPP
PAPCACPAP
PPPPCPPPP
*PAPPPAP*
*PPPAPPP*
***PPP***
```
Smithing Table above center A, eight Darkstone Pedestals above other A (±3 cardinal and ±2 diagonal), air on required upper positions. Use Mundabitur Dust on table -> activated tier1. Empty-hand opens UI. Put main item in central slot, required amounts on pedestal slots (exact total matches ritual; do not add unrelated offerings); load essence slots and four enhancer slots; right-use appropriate Blacksmith Gavel (ritual starter component, durability on successful start) rather than assuming GUI starts ritual. Three magic circles generated by manager, no hand drawn pattern. Gavel head and wooden/stone/iron/gold/diamond recipes; netherite smithing from diamond + vanilla upgrade template/ingot. Wait for circle/animation and result; ritual manager clears pedestal offerings and handles result type. Create-item, transmute-input and upgrade-tier outcomes; source retains failure effects but chance hardcoded zero. No promised refund after interruption. Pedestal can display item independently; use Ferrognetic Mixture on empty ordinary Darkstone Pedestal to consume mixture and transform it; Magnetized Pedestal picks up an ItemEntity contacting the above shape when its slot is empty, triggers forge ingredient update. This is local offering automation, not a general transport system.

Four Forge capacities by tier (Aureal/Souls/Blood/XP): T1 1000/10/10000/900; T2 3000/50/15000/1350; T3 5000/100/30000/2500; T4 10000/500/50000/5000; T5 20000/1000/100000/7500. Separate from player essence. Items in correct slots: Soul/Corrupt1, Enchanted10, Aureal Bottle35, Splash30, XPetrified Orb91. Test Tube Rune+glass bottle, offhand while direct player attacks eligible target fills Blood Tube with int(20*post-mitigation damage) up to3000. Nearby health-loss sampling every20 ticks in inflated5 AABB adds truncated delta x20, not every death. Obelisk on gilded chiseled polished support, Arcane Polished Darkstone then 2 Arcane Crystal Blocks, Mundabitur activates; nearest Forge within4 gains1 Aureal every100 ticks. Bottle recipe eight dust around strict strong_regeneration potion; drinking grants personal35, splash grants personal30 to players in radius<4 and vertical search range, also tags passive/ambient mobs and converts Lost Souls. Personal cap100, base regen0, integer regeneration attribute every100 server ticks, no positive default regen. Aureal Tank disabled, no survival milestone. Utrem Jar seven glass + Edelwood Plank; place, use compatible essence item to type it, fill same type up to10000, loot copies essence_storage component on break; place filled item in Forge, storage input extracts up to10/call and returns empty jar/tube at depletion, single-value input consumed even if excess. Quantum Injector T3: put within4 of Forge and matching filled essence jar within8; compiled serverTick every5 ticks, transfer +5 forge/-5 jar, not FE or generic fluid. Code uses fixed transfer amount rather than explicit min(source,space); do not promise exact conservation at empty/full boundary. Normal jar loading sufficient for ordinary progression. Black Hole: drop Dark Matter+Corrupti Dust together in air, consume one each; attracts tagged entities in ±5 cube, within0.6 absorbs XP orbs, >=60 yields XPetrified Orb (91 raw XP on use), resets counter, overshoot lost, 4 magic damage to other eligible entities per tick; stored XP persists, ejected item list exemption. Not MXP. Potential net XP gain, TMM economy downstream.

## Enhancers and acquisition
Artisan Relic: village armorer/toolsmith/weaponsmith chest additions 28%, native trader18 emeralds; required for Obsidiansteel, XP eligibility multiplier .75. Elementarium: jungle temple/desert pyramid/underwater ruin chest addition21%, unlocks four prisms. Crimson Stone: pillager outpost18%, soul eligibility .5 and Clibano fire duration1.3x. Maledictus Pact: bastion treasure18%, no native ritual in exact 22 requires it. Divine Pact: required by Soul Binding Crystal; no native recipe, loot addition or trader listing found in shipped data/inspected trade registry. Crescent Moon (Aureal .9 daytime/.99 night), Soul Crimson Stone (all essence eligibility0) similarly no acquisition found in these sources; do not promise their survival availability or invent recipes. No native requirement to own all enhancers to reach Tier5. Important cost implementation: RitualManager modified eligibility, unmodified definition debit via EssencesStorage.reduce and clamp>=0; advertised reductions not reliable savings when holding full original cost. No pack fix authorized.

## Complete ritual catalog
All exact packaged 22 entries below. Costs A/S/B/X (zero when absent). Base tier1 when field omitted; upgrade tiers match exact current tier; others minimum tier. Offerings listed in addition to central item. No need to memorize ordinary JEI recipes; teach meaningful branches.

| Ritual | Tier | Center | Pedestal inputs | A/S/B/X | Enhancer |
|---|---:|---|---|---|---|
| upgrade 2 | 1 | Edelwood Planks | 4 crystal,4 scrap | 500/10/6000/0 | — |
| upgrade 3 | 2 | Chiseled Polished Darkstone | 4 crystal,4 Deorum | 1000/50/9000/0 | — |
| upgrade 4 | 3 | Chiseled Polished Darkstone | 4 Stellarite,4 Rune | 2000/100/12000/0 | — |
| upgrade 5 | 4 | Stellarite Block | 4 sculk catalyst,2 Dark Nether Star,2 Dragon Scale | 5000/500/20000/0 | — |
| Draco boots | 2 | netherite boots | 2 scale,2 Obsidiansteel | 700/5/405/370 | — |
| Draco leggings | 2 | netherite leggings | 3 scale,3 Obsidiansteel | 1000/7/565/540 | — |
| Draco chest | 2 | netherite chestplate | 4 scale,3 Obsidiansteel | 1300/9/625/760 | — |
| Draco helmet | 2 | netherite helmet | 2 scale,3 Obsidiansteel | 820/6/475/420 | — |
| Tyr boots | 4 | Draco boots | 3 aquatic scale,2 golden scale | 4460/40/5620/2170 | — |
| Tyr leggings | 4 | Draco leggings | 4 aquatic scale,3 golden scale | 5080/50/7620/3010 | — |
| Tyr chest | 4 | Draco chestplate | 4 aquatic scale,4 golden scale | 6300/60/8080/3420 | — |
| Tyr helmet | 4 | Draco helmet | 4 aquatic scale,1 golden scale | 4820/40/5930/2450 | — |
| Eternal Stella | 3 | diamond | 3 XPetrified Orb,1 Stellarite | 82/1/1000/0 | — |
| Soul Binding Crystal | 4 | quartz | 1 each soul/corrupt/enchanted,2 amethyst,3 pearl fragment | 1500/20/375/220 | Divine Pact |
| Quantum Catcher | 1 | Quantum Core | 4 scrap | 200/5/1200/155 | — |
| Boss Catcher | 4 | Quantum Catcher | 1 nether star,3 Stellarite | 500/10/7500/1200 | — |
| Quantum Injector | 3 | Mundabitur Dust | 4 Quantum Core | 5000/50/3000/1060 | — |
| Ferrognetic Mixture | 1 | lodestone | 2 clay,2 wax,2 slimeball,1 iron | 100/2/1250/0 | — |
| Smelter Prism | 1 | Arcane Crystal Block | 2 coal,4 blaze powder | 200/4/1250/0 | Elementarium |
| Terrastomp Prism | 2 | diamond block | 2 flint,2 dripstone block,2 pointed dripstone | 300/9/1500/0 | Elementarium |
| Sea Prism | 3 | Heart of Sea | 2 prismarine shard,2 turtle scute,2 lapis | 1000/8/2000/0 | Elementarium |
| Whirlwind Prism | 3 | white wool | 1 bat wing,2 feather,3 phantom membrane | 1000/3/2250/0 | Elementarium |

Dark Nether Star = nether star +4 Obsidiansteel cardinal; Stellarite Block nine pieces; Golden Scale = scale+4 Deorum; Aquatic Scale = scale+prismarine shard+crystals+dust; Silver Scale = scale+4 iron+4 dust. Quantum Core rune+flint+Mundabitur. Armor ladder netherite→Draco→Tyr; Draco armor material total30 defense (6/8/10/6 boots/legs/chest/head), toughness2; Tyr total37 (7/10/12/8), toughness3, knockback resistance0.1 per material; check actual attributes with pack mods. No Mortem armor or Draco tools/staff/scepter acquisition identified in 188+22 records, don't teach as reachable simply because registered. Native Tyr armor name unrelated to modpack naming. Tier5 is final Forge capacity, not a further set of recipes in native catalog.

## Modifier, mobility, utility, combat branches
Template = 8 Darkstone around Netherite Upgrade Template. Smithing template + valid equipment + modifier item; single modifier, incompatible item/enchantment tags and predicate. Eternal Stella removes DAMAGE and MAX_DAMAGE components (not Mending); Soul Binding Crystal applies Soulbound (inventory slot save/clear on death and restore on respawn); Fiery from Smelter Prism maps loot through smelt function; Demolishing from Terrastomp Prism scans adjacent plane based on hit side, same eligible block predicate, server break path (teach area mining, not unlimited vein); Magnetized from Ferrognetic Mixture restricted boots: compiled PlayerMixin extends item touch/pickup to bounding box inflated 2.5 horizontally, 1.5 vertically when alive, nonspectator and not mounted on a live vehicle; not XP pickup or moving entities with a force. Aquatic from Sea Prism restricted helmet: same mixin treats underwater airborne mining as on-ground for the dig-speed penalty and heals 2 HP every 100 ticks while injured and eyes submerged in water; not a confirmed water-breathing effect. Whirlwind Prism use places Whirlwind, creates 10 Upwind blocks above, base removes after20 ticks, upwind applies upward motion; use clear space (creation loop sets blocks), don't claim safe in arbitrary structure. Smelter Prism also registers 200 burn ticks, durability200. Magic Wand ordinary recipe crystal/deorum/Edelwood Stick; hold use >=30 ticks and release with enough personal Aureal (cost5 registered), fires Aureal Missile; no automatic source regen. Spectral Eye Amulet string, nether star, two Deorum, eye of ender, right-use toggles inventory-tick Spectral Vision (80-tick refreshed effect); not confirmed universal X-ray. Obsidian Skull, cracked/fragmented versions have recipes; compiled ModBlocks$BlockItems and LivingEntityMixin use GRANTS_EFFECTS fire resistance rules on applicable variants. ObsidianSkullItem ticks only while worn in head slot and on fire; DEFAULT/CRACKED/FRAGMENTED/FADING progression ultimately returns skeleton skull, stage timer random 8–13 seconds of active burning; Aurealic variant debits personal Aureal per active tick, Eternal type has empty tick function. No native acquisition for those latter variants confirmed. Arcane Dragon Egg, Boom Arrow and Draco Arrow recipes exist; Boom Arrow block damage false radius3 native default; Mundabitur can charge creepers default true; no general explosion mining or boss spawn guarantee. Research Desk/Forbiddenomicon only three knowledge nodes; no actual survival acquisition found, preview/disabled content not forced. Aureal Wardstone preview flag; do not create quests for inaccessible registered items. No claim of full active spell-school engine.

Quantum Catcher: use empty catcher on living target, personal Aureal cost round(maxHP) halved friendly or truncate x1.1 otherwise; cap100 limits targets. Normal excludes player and #c:bosses, Boss Catcher native excludes player; both check alive. Captures entity data, stops riding/ejects passengers, removes UUID/position/brain/motion/leash/etc, discards target; release on replaceable target or adjacent face, recreate then clear component, no release cost. Stack capture uses base catcher rather than dyed variant. Native tag eligibility is NOT TMM approval. TMM cross-mod capture policy #373, boss restrictions, MXP, ore and ProjectE gates, blood fluid, crop/tag compatibility, soulbound death handlers and modifier applicability on other mods are separate pack implementation concerns; do not change them in Atlas.

## 29 capability axes
Statuses below are native capabilities, absence scoped to exact data/class inventory and meaningful source survey; unknown is explicit, not a runtime test demand.

| Axis | Coverage | Actual native mechanic / terminal extent |
|---|---|---|
| gear | present | gavel ladder; Draco/Tyr armor and arrows, wand, six modifiers; unacquired registered gear excluded |
| movement | present | faster Edelwood ladder, boats, Whirlwind/Upwind temporary lift |
| ore_processing | present | Clibano 15 recipes, enhanced residue blocks, gavel probabilistic eligible drops |
| storage | present | 10k single-type Utrem Jar, 3k Blood Tube, capacity bucket, captured entity |
| crafting | present | Clibano, five-tier Forge, 22 rituals, modifier smithing |
| logistics | present | sided Clibano item handler, Quantum Injector essence routing, magnetized pedestal class |
| buffs | present | toggled Spectral Vision, equipment modifier/attribute systems |
| external_compatibility | present | NeoForge common tags, Valhelsia Core dependency, JEI recipe integration, loot hooks; no verified TMM interoperability |
| mob_farming | present | capture/release and tagged Black Hole damage, not general spawner creation |
| ore_generation | no_capability_confirmed | Worldgen ores and residue recycling, no arbitrary ore-generating machine in registry survey |
| permanent_power | present | eternal equipment, permanent modifier, Forge tier upgrades |
| general_generation | present | Black Hole XPetrified Orbs, Obelisk Aureal, soul/blood farming |
| direct_damage | present | Aureal Missile, arrows, Black Hole magic damage |
| block_breaking | present | Gavel and Demolishing plane scan, Stellarite explosion boundary |
| permanent_pets | no_capability_confirmed | captured mobs are not new controlled pets; none taught |
| temporary_pets | no_capability_confirmed | Lost Souls are ambient/spawned mobs, no controlled summon loop |
| defense | present | Draco/Tyr armor, magic-protection attribute registration, skull fire-resistance and finite burning-stage family |
| healing_recovery | present | Aquatic helmet heals 2 HP per 100 ticks while injured and submerged; no general healing spell |
| debuff_control | present | skull fire-resistance effect rule and aquatic underwater mining-penalty bypass; no general cleanse confirmed |
| energy | present | distinct four essences, personal Aureal, fire fuel, no native FE network |
| automation_control | present | Clibano IO, Injector automatic transfer, Obelisk and Black Hole ticking |
| agriculture | present | Arcane Bone Meal, Magical Farmland, Aurum/Edelwood |
| detection_information | present | Spectral Eye, Aureal meter, recipe GUI |
| equipment_augmentation | present | six exclusive modifiers and armor transmutation |
| world_manipulation | present | multiblock transforms, magical farmland, corrupt sapling, Whirlwind, charged creeper |
| world_content | present | ores, Flower Forest Aurum/orchid, Lost Souls and loot additions |
| death_respawn | present | Soulbound restores inventory slots, not resurrection/immortality |
| trade_economy | present | native five Wandering Trader additions, loot sources; TMM redesign separate |
| progression_tiers | present | five Forge tiers, four upgrades, armor/ritual/minimum-tier/enhancer gates |

## 44 instructional lessons (research only)
Prerequisites are suggested teaching dependencies, not invented hard gates. Item/advancement checks are prospective proxies; operation lessons should explain visible result rather than assert implemented detectors. Rewards modest ordinary supplies/XP, no gated items or bypasses; conditional native acquisition branches explicitly identified. Actual FTB chapter, GUIDs and runtime acceptance owned downstream.

| ID | Prior lessons | Teach action / observable success |
|---|---|---|
| FA-01 | — | Find crystal ore, Rune source and Darkstone; collect materials and use JEI. |
| FA-02 | FA-01 | Process crystal dust; make Mundabitur Dust and Deorum. |
| FA-03 | FA-01 | Mine Stella Arcanum safely; obtain Stellarite, distinguish ore from Stella. |
| FA-04 | FA-01 | Collect Ender Pearl Fragment, Bat Wing and Spawner Scrap from appropriate loot. |
| FA-05 | FA-01 | Locate Lost Soul; collect soul and explain Soul Looting. |
| FA-06 | FA-05 | Corrupt or enchant Lost Soul with relevant reagent; compare essence values. |
| FA-07 | FA-05 | Obtain Growing Edelwood from Corrupt Soul or native trader; grow and harvest. |
| FA-08 | FA-07 | Bottle oil from oily log; carve logs and make planks/sticks. |
| FA-09 | FA-01 | Find Aurum/orchid in Flower Forest or trader; cultivate plants. |
| FA-10 | FA-02 | Use Arcane Bone Meal on farmland; harvest eligible mature crop. |
| FA-11 | FA-07 | Craft Utrem Jar; fill one essence type and preserve on break. |
| FA-12 | FA-04 | Find Artisan Relic in village loot or native trader (TMM overrides separate). |
| FA-13 | FA-02 | Build 3x3x3 Clibano and activate; see formed core. |
| FA-14 | FA-13 | Process a normal combustion recipe with ordinary fuel. |
| FA-15 | FA-05, FA-14 | Feed Soul or Corrupt Soul for Soul Fire; compare cooking. |
| FA-16 | FA-06, FA-15 | Use Enchanted Soul; accumulate nine residue for block output. |
| FA-17 | FA-12, FA-14 | Place Artisan Relic and combine raw iron + obsidian; acquire Obsidiansteel. |
| FA-18 | FA-17, FA-04 | Craft Corrupti Dust; revisit conversion branch if needed. |
| FA-19 | FA-07, FA-18 | Smelt log, combine Dark Matter and Corrupti Dust; form Black Hole. |
| FA-20 | FA-19 | Feed XP orbs, observe XPetrified Orb; explain 60→91 threshold. |
| FA-21 | FA-02, FA-07 | Craft Soul Extractor; hold on Soul Sand, collect Soul and Soulless Sand. |
| FA-22 | FA-01 | Craft Test Tube; direct attack with offhand tube to collect blood. |
| FA-23 | FA-02 | Craft Aureal Bottle, use personal meter; distinguish Forge supply. |
| FA-24 | FA-02 | Craft gavel and frame blocks; build 9x9 Forge/pedestals, activate Tier1. |
| FA-25 | FA-24, FA-11, FA-20, FA-21, FA-22, FA-23 | Load four essences and learn typed caps and inputs. |
| FA-26 | FA-24, FA-23 | Build active Obelisk nearby, observe Aureal gain. |
| FA-27 | FA-25, FA-07, FA-04 | Perform exact Tier1→2 upgrade ritual with gavel. |
| FA-28 | FA-27, FA-02 | Perform Tier2→3 upgrade. |
| FA-29 | FA-28, FA-03 | Perform Tier3→4 upgrade. |
| FA-30 | FA-29, FA-17, FA-03 | Make Dark Nether Star and Stellarite Block, upgrade to Tier5 with scales. |
| FA-31 | FA-25, FA-04 | Craft Quantum Core, perform Quantum Catcher ritual; capture/release permitted mob. |
| FA-32 | FA-29, FA-31, FA-03 | Create Boss Catcher; teach TMM eligibility caveat, not unauthorized boss capture. |
| FA-33 | FA-28, FA-11, FA-25 | Create Quantum Injector; place near Forge and filled jar; observe transfer. |
| FA-34 | FA-27, FA-17 | Obtain Dragon Scales via End loot, upgrade netherite to Draco armor. |
| FA-35 | FA-29, FA-34 | Craft aquatic/golden scales; transmute Draco to Tyr armor. |
| FA-36 | FA-28, FA-03, FA-20 | Create Eternal Stella; craft template; apply to eligible equipment. |
| FA-37 | FA-25 | Find Elementarium in native structure loot, use enhancer slots. |
| FA-38 | FA-37, FA-25 | Perform Smelter Prism ritual and Fiery modifier; observe smeltable drops. |
| FA-39 | FA-37, FA-27 | Perform Terrastomp Prism ritual and Demolishing; teach controlled area breaking. |
| FA-40 | FA-37, FA-28 | Perform Sea Prism ritual; Aquatic helmet; demonstrate underwater airborne mining and injured submerged healing. |
| FA-41 | FA-37, FA-28, FA-04 | Perform Whirlwind Prism ritual; use in clear space for temporary lift. |
| FA-42 | FA-25 | Create Ferrognetic Mixture and Magnetized boots for nearby dropped-item pickup; optional use mixture on empty pedestal to automate offerings. |
| FA-43 | FA-29, FA-06, FA-04 | Conditional: Divine Pact needed for Soul Binding Crystal; no verified native acquisition, omit as mandatory; explain Soulbound if available. |
| FA-44 | FA-23, FA-08, FA-02 | Craft wand and cast with personal Aureal; optional Spectral Eye Amulet after nether star, toggle effect. |

**Dependency note:** early Edelwood can use a naturally obtained Corrupt Soul or the native trader; Corrupti Dust crafting itself needs Artisan/Clibano and is taught in FA-18, not required to start the Clibano. Lesson 06 is a demonstration with acquired reagent, not a mandatory early gate. Divine Pact and unavailable optional registered content are explicit negative/limited findings, not promises; no invented acquisition. No vendor binary or assets, actual quest, gameplay acceptance or balance change. Native dossier sufficient for authoring reachable core progression; treat optional unavailable branches as conditional and review effective TMM overlays separately.
