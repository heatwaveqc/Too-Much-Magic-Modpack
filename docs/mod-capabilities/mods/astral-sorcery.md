# Astral Sorcery — 2.0.3.59 native capability Atlas

**Status: research COMPLETE for quest-authoring handoff, not gameplay acceptance.** Major-system research under owner-approved 2026-11-24 depth: comprehensive functions, meaningful unlocks and actionable lessons; not every coefficient or ordinary JEI recipe. Prior [Mod Comprehension profile](../../mod-comprehension/mods/astral-sorcery.md) is the detailed historical reference, not a competing authority; this dossier reconciles the modern source and corrects earlier gate counts. Research checkpoints [round 1](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/410#issuecomment-7137295694), [round 2](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/410#issuecomment-7137606941).

## Evidence boundary
MC 1.21.1 NeoForge alpha, Java 21; Packwiz mods/astral-sorcery.pw.toml, CurseForge 241721/9281073, SHA-1 66f36655eae7cb404c9c16da6f33db31dff6ed9a. Earlier Comprehension inspected exact JAR SHA-512 aa3f130e1e61dccd199373b6ee39563faf7d2573090e25c36f305f247a8102338371da25a79dcf1518909b5d48de0d825c94dbcfb8a5534667906fa5b61cfb93 and matched all 1994 main/generated resource files to [immutable upstream source e5309e9cc82dfe96d0fc7b04aaa5c56692d47f55](https://github.com/HellFirePvP/AstralSorcery/tree/e5309e9cc82dfe96d0fc7b04aaa5c56692d47f55). This Atlas uses that earlier binary provenance, directly read matching generated recipe JSON, Tome localization and selected Java; no new independent JAR verification or compiled parity claim. [Release](https://www.curseforge.com/minecraft/mc-mods/astral-sorcery/files/9281073) adds engraving/Jade lumen readouts and fixes; dependencies ObserverLib and Curios. No old 1.16 guide assumed equivalent. Source path abbreviations: data = src/generated/resources/data/astralsorcery; lang = src/main/resources/assets/astralsorcery/lang/en_us.json; Java = src/main/java/hellfirepvp/astralsorcery.

## Onboarding and actual altar ladder
Tome auto-granted first login by default if space; right-click research, shift-use paper inventory. Recovery recipe in JEI. Welcome/Ores/Astrolabe/Constellation Paper pages. Aquamarine Shale waterways (shovel), Rock Crystal Ore deep (iron pick or better, close enough to catch drops), marble worldgen. Find paper/discover constellation tracing with Astrolabe, correct sky/night/moon/angle, sneak trace. Discover focal point in open terrain aligned with sky; five major Aevitas, Armara, Discidia, Evorsio, Vicio and seven minor Lucerna, Mineralis, Horologium, Octans, Bootes, Fornax, Pelotrio recorded; no extra perk roots inferred. Focal combine dropped pile under open sky, leave untouched; wand recipe two raw marble +2 Aquamarine +Ender Pearl. Wand reveals buried ore, starts devices, shows missing multiblock blocks. Focal combine four raw marble + vanilla table→Illuminated Crafting Table; first altar no multiblock, operates at night. Use actual Tome ObserverLib ghost, not invented geometry.

| Upgrade | Previous altar | Meaningful actual recipe gates |
|---|---|---|
| Illumination | focal combine | four raw marble + crafting table |
| Resonance | Illumination | crystal +4 Pillars +2 Runed Marble +1000mB Liquid Starlight in grid |
| Luminance (Luminous) | Resonance | main Resonating Gem +2 gold ingots +3 Runed Marble; relays 4 gold ingots +4 Pillars +5 Runed Marble; dropped 3 Stardust; 3000mB fluid |
| Radiance (Iridescent altar) | Luminance | Celestial Crystal +Glass Lens +6 Starmetal ingots; relays 2 Resonating Gems +8 Pillars +3 Sooty Marble +3 Runed Marble; dropped 2 each Stardust, Nocturnal and Illumination Powder; 3000mB fluid +200 each of five primary lumens +Armara starlight |

Counts from exact altar_resonance/luminance/radiance JSON patterns; corrects earlier profile's Luminance relay gold/pillar counts and Radiance ingot count. Night crafting; upgrade in place; placing higher altar or recipe's tier update advances personal research. Focus Relays accept one item per position; drops nearby and chalices supply extra inputs. T2 small or expanded structure (small 5x5 floor, rails ±3, eight relays at ±2); T3 raised relay ring and pillars/arches; T4 ±7 perimeter. Use Tome named structure pages altar_t2/t3/t4 and wand preview. Attunement and special equipment optional except where a concrete later recipe needs their components (Armara focus for final tier). No mandatory linear ordering of all branches.

## Materials, liquid and renewability
Lightwell (2 Aquamarine, crystal, 5 Runed Marble) consumes catalyst, clear overhead or destroys catalyst, 2000mB tank, extract below; full tank wastes output while catalyst continues degrading. Aquamarine, crystals or Resonating Gem yield Liquid Starlight; Netherrack/Magma yield lava; Ice/Packed/Blue Ice yield water. Earlier profile derives rates from all 9 recipes. Containment Chalice single-fluid reserve, bucket/underside pipe; clear LOS draws nearby Lightwells' Liquid Starlight automatically, supplies altars/infuser; nearby compatible chalices react, water/lava stone, starlight/water ice, starlight/lava sand or Aquamarine Shale; redstone inhibits. Pool contact water→packed ice, lava→sand/sometimes shale; logs→Infused Wood without consuming pool. Infuser requires filled structure pools, wand activation, works under roof/day, consumes one pool block or draws chalice; Aquamarine→Resonating Gem. Teach setup before Luminance.

Starmetal: drop iron ore (minecraft:iron_ores) or raw iron (c:raw_materials/iron) at focal point; transforms one-for-one into ore/raw Starmetal; furnace/blast smelt. Block transmutation iron ore tag c:ores/iron→Starmetal Ore, generated recipe requires_focused_starlight=false. Not ore multiplication or generic ore miner. Starmetal Chisel hits dropped ingot to produce Stardust, often consumes ingot; Fortune reduces loss; strikes dropped crystals to split with inherited but lossy properties; too weak cannot split; artifacts yield shards. Pool crystal grows size or merges two with loss. Stardust + crystal pool forms Celestial Cluster; open sky/night faster, Starmetal Ore below boosts but may revert to iron; harvest mature with pickaxe→Celestial Crystal and Stardust, immature yields less or none. CrystalPropertiesAS registers size (8 tiers), purity/cut/focus/channeling (4), tool efficiency/durability (3), constellation and lumen properties. Distinct property caps vs total capacity; don't promise perfect all-property crystal. Celestial higher ceiling. Engraving: eight advanced types, weighted random property upgrade not guaranteed selected type; pool crystal + typed advanced Lumen Crystal + Resonating Gem, or Luminance altar (example Hyle crystal +2 Echo Shards +Stardust +2 buckets +400 Hyle). Code checks total cap and property cap, random roll may fail to improve; consumes inputs. Distinguish binding charge vs engraved property.

## Separate resource networks
Starlight: attune Rock/Celestial Crystal at completed Attunement Altar; Luminance Focus Crystal recipe attuned crystal +4 Resonating Gems +4 Illumination Powder relays +4 Stardust dropped +4000mB. Place in matching focal column (height irrelevant), open sky; local flight near active source. Night overhead constellation/moon drives flow. Linking Tool clear LOS; multiple links split; lens many in→one out; prism splits; purity reduces attenuation, crystal size/cut determines source. Link altar, Tree Beacon, celestial cluster, block conversions. Stellar Filament layers four diagonally arranged pillars, one per quadrant, no shared columns; patterns Aevitas opposite pairs not square; Armara one-axis mirror; Discidia no square corner; Evorsio no quarter/half-turn symmetry; Vicio four different distances. Adds focus strength/visibility. Lumen network distinct, never conflate Lumen Filament with Stellar Filament.

Lumen: create five flowers by planting ordinary small flowers or dropping flower items in matching focal column (dropped slower). Resonance Lumen Array fueled with Liquid Starlight and flower catalyst, needs no sky or structure, locks first generated type, fastest around half full, redstone halts output but catalyst/liquid may still degrade. Comparator/readout. Clear short air links; Lumen Filaments relay shortest path, no storage. Luminance Alchemy Array uses Lumen Array, crystal, relay Stardust/gold/marble +2000mB; accepts catalysts only when fluid and valid recipe; each feed array must directly see alchemy array, no filament input. Every recipe below from data/recipe/lumen; catalyst degrades, quantities shown per production operation, not total lifetime yield.

| Lumen | Catalyst | Input units | Output |
|---|---|---|---:|
| Aevitas | Hyacinth | none |4|
| Armara | Orchid | none |4|
| Discidia | Thistle | none |4|
| Evorsio | Protea | none |4|
| Vicio | Iris | none |4|
| Virel | Vivid Powder |9 Aevitas,5 Vicio|3|
| Solyn | Illumination Powder |5 Aevitas,8 Armara|3|
| Caldor | Quartz |4 Armara,10 Discidia,6 Evorsio|3|
| Nullae | Nocturnal Powder |5 Evorsio,7 Vicio|3|
| Hyle | Echo Shard |8 Armara,5 Solyn,4 Virel|2|
| Dynamis | Amethyst |7 Solyn,8 Vicio,8 Virel|2|
| Aion | Resonating Gem |5 Caldor,16 Discidia,8 Nullae|2|
| Akasha | Stardust |7 Caldor,12 Evorsio,4 Nullae|2|
| Prismatic | typed Artifact Shard (sidereal/luminous/chronal/astriform/axiomatic/spectral) |3 each Hyle,Dynamis,Aion,Akasha|2|

Crystallizer Resonance recipe; fill liquid and insert catalyst to request lumen, cluster grows atop plate; hand harvest one stage per crystal. Primary catalysts sapling, ingot, arrow, stone, feather respectively; normal crystal requests Prismatic; matching Lumen Crystal catalyst never shatters, continuing supplied production. JEI crystallization per type. Bind lumen crystal + Stardust + eligible equipment/filled Stardew flask in pool, recharge via matching crystal and equipment in pool; one primary binding, new replaces old, Prismatic amplification exception. Charge in item component, spent by relevant action when equipped/held/used; seven slot classes (helmet/chest/legs/boots/melee/ranged/tool). No player general mana bar, no established FE/Source/Botania bridge; per-player XP independent. Flask filled in water cauldron, multiple aspects stack, finite sips; rebind/refill. Prismatic one-time flask upgrade (+1 amplifier and random duration enhancement); no standalone effect. Tome contradictory re Prismatic drain, do not assert zero drain.

| Binding | Meaningful effects (slot-specific, not every item receives all) |
|---|---|
| Aevitas | health, regeneration drink |
| Armara | armor/blocking, resistance |
| Discidia | damage, strength |
| Evorsio | mining/armor pierce, haste |
| Vicio | travel/projectile speed, speed |
| Virel | health/motion, regeneration |
| Solyn | health/armor, marked enemies, lights on mining, resistance |
| Caldor | reach/armor pierce/arrow accuracy, invisibility |
| Nullae | mining speed/motion/reach, blindness then haste |
| Hyle | protection/life leech/wide mining, resistance |
| Dynamis | crit/attack speed/kill burst, Rampage |
| Aion | cooldown/potion duration, slowed foes, harvest boosts crops, Phoenix Blessing drink |
| Akasha | reach/step/fall/wide mining bundled drops, accurate arrows, night vision then luck |
| Prismatic | amplifies other binding or flask, final crafting ingredient |

## Personal constellation progression
Resonance Attunement Altar, 19x19 open sky sooty marble plaza, use constellation paper at night to show Focus Relay pattern; complete and overhead constellation, stand without sneaking; discovery required for personal attunement. Drop crystal on altar for attuned crystal of selected constellation. Major root and activity: Aevitas +10% max health/placing blocks; Armara +15% armor/taking damage; Discidia +10 percentage-point crit/dealing damage; Evorsio +2 block reach/breaking; Vicio +10% speed/travel. Default perk level cap 30, internal per-player XP not vanilla or TMM MXP, connected paths/Arete and sockets. Plain Shifting Star removes attunement and XP; attuned star requires previous attunement/discovered target, switches while preserving XP but resets allocations. No TMM perk integration change authorized. Illumination Powder+crystal in pool→gem cluster, open sky, final maturation day Ilium (more weaker), night Fengarum (fewer stronger), twilight Ourium intermediate; harvest full grown before shrink; socket right-click insert/remove. Perk Seal drag suppresses while keeping connection; double click remove seal; Nullifier drag removes and refunds point if unsealed and no dependent disconnect. Ctrl drag repeated use.
Named capabilities: Mending armor repair, Cleansing debuff removal during regen, Vivid Growth plants, Spatial Manipulation drops to inventory, Bigger Stomach reduced hunger, Diamond Skin one armor piece damage reduction, Unwavering knockback immunity, Body Blocking armor takes damage, Cursed Touch negative effects, Rampage kill stacks, Long Shot/Blunt Bolts distance specialization, Culling Strike below15% HP, Different Angles pickaxe axe/shovel, Breaking Aim strip equipment, Last Breath low HP damage/mining. Phoenix Blessing Java KeyPerkCheatDeath: active allocated perk, no cooldown, server LivingDeathEvent canceled, health1, Regeneration III 400 ticks, totem sound, default cooldown1800 ticks (90s), configurable, does not scale with perk effect. Distinguish from Aion drink effect; do not assume identical code. Possible actual stacking with class saves/mitigation belongs to downstream balance, no nerf.

## Tools, travel, utility and resource automation
Crystal tools axe/pick/shovel/sword, diamond-level mining per Tome; inherit crystal properties. Iridescent upgrades are **Luminance, not Radiance** (exact 4 altar recipes), base tool +4 Resonating Gems +4 Illumination Powder in relays +5000mB fluid, focus_constellation consumed (base_focus_shatter_chance=1), copies data components. Pickaxe Aevitas focus, wide mining and use reveals ore; Axe Evorsio focus, extended reach and fells connected tree, sneak single; Shovel Vicio focus, wide face and connected same-kind vein, sneak single; Sword Discidia focus, short charge crescent, full Celestial Strike, open stars stronger/night cooldown halved. Personal attunement: Discidia damage, Aevitas heal nearby, Armara shield, Vicio teleport to hit, Evorsio repel; charge reflects missiles, early parry guarantees full strike. 
Formation Wand teach block types sneak-use, clear sneak-attack, sneak-use air mode lines/floors/walls/solid/hollow spheres; preview, uses inventory blocks, lumen extends range. Conversion Wand same selection, exposed connected matching blocks, returns broken drops, consumes replacements, cannot convert unbreakable or harder-than-obsidian, lumen extends. Traversal Wand sneak-use mode launch charge or line teleport, fall safe launch, cooldown, Vicio increases range with charge. Impulsion hook pulls to block or entity, no resource cost beyond cooldown, fall safe while pulling. Illumination Wand colored lights remove by use, dye recipe changes color, sneak-use block owner-only reversible translucent ghost; no use cost, boosts Cave Illuminator. Cave Illuminator place by hand under sky, persistent lights in dark roofed spaces below, no daylight spaces. 
Throw powders: Illumination permanent light, Nocturnal darkness/blindness/spawns monsters except peaceful, destroys lights (warning), Vivid accelerates plant growth/harvest/replant and heals nearby, not dispenser-compatible, other powders mostly dispenser usable. Tree Beacon captures grown tree as ghost, drops wood/leaves, night linked starlight bonus, shadow wears, breaking restores tree; no permanent infinite source without reseeding. Celestial Gateway built structure, clear sky (dimension ceiling counts no-sky), select distant star while holding use or sneak, free cross-dimension travel, dye/name destinations. Knowledge Sharing Scroll blank sneak-use records knowledge; another player use grants discoveries/focal points/research tier, no personal XP or strength, reusable immutable, no self reading. Resplendent Prism Curio neck, Shifting Star + Eye of Ender core gate, random specific enchant boosts/grants/all levels, altar rerolls lose old, artifact enhances once.

## Exploration and endgame
Overworld focal points, rotundas, columns, moon dials, dig and obliteration sites; Marble/Aquamarine/rock crystal; no new dimension established. Shooting stars around midnight after understanding, chase before dawn, open sky, loot and crystal if nearby when strikes, artifact. Artifact drop awakens and binds owner, 4–6 randomized riddles, clue light/sound, meet before timeout, unrest summons mobs/teleports/destroys; settled safe pickup. Provider condition classes block proximity (19 kinds), entity (4 groups), dimension Nether/End, structures fortress/stronghold/mineshaft, damage by fire/projectile/explosion/sword. Conflicting dimensions/structures excluded. Do not require all riddles or assume exact range. Chisel shard→Akashic Singularity crafted and repeatedly use to empty loot. Three exact terminal recipes at Radiance: settled dropped artifact +600 Prismatic +2000mB Liquid Starlight; eligible enchanted item with 4 Stardust/2 Resonating Gems relays, Dynamism Gem with 4 Resonating Gems, Resplendent Prism (enchantment_amulet) with 4 Illumination Powder/8 gold nuggets. Target must not already carry enhancement flag. Enchanted raises eligible levels excluding curses/single-level, gem extra quality, prism extra bonus; consumed artifact, relic unique result. No repeat enhancement. Native terminal technique.

## 29-axis crosswalk
| Axis | Native capability |
|---|---|
| gear | Crystal and Iridescent four tools, Prism, binding |
| movement | Traversal, Impulsion, Gateway, local focus flight, Vicio |
| ore_processing | iron→starmetal one-for-one; smelting/chisel |
| storage | Containment Chalice single fluid; item lumen; no general storage system established |
| crafting | four altars, relay/drop/fluid/typed energy inputs, infuser, focal combine |
| logistics | Chalice LOS fluids, focus network, lumen air links |
| buffs | Stardew, perks, charged gear |
| external_compatibility | ObserverLib, Curios, Jade, common tags; no FE bridge established |
| mob_farming | Nocturnal Powder monster spawning, no dedicated kill/loot machine |
| ore_generation | iron focal transmutation, occasional shale reaction, no general ore generator |
| permanent_power | personal attunement/perks/XP, sockets |
| general_generation | starlight, lumen, crystal/gem growth, Tree Beacon, Lightwell fluids |
| direct_damage | sword strikes/curse-like perks, charged weapons |
| block_breaking | Iridescent area/vein/tree, tool bindings, Different Angles |
| permanent_pets | no native persistent companion identified |
| temporary_pets | no native summon family established, artifact hostile mobs not pets |
| defense | armor/perks/bindings/shield/projectile reflection |
| healing_recovery | Aevitas, Hyle, Vivid, Stardew, Cleansing/Mending |
| debuff_control | Nocturnal blindness, Cursed Touch, Aion slow, Cleansing |
| energy | celestial focus, fluid, 14 typed lumens, item charge, player XP separately |
| automation_control | redstone arrays/chalices, comparator, filaments, relays |
| agriculture | Vivid, Tree Beacon, flowers, Aion harvest |
| detection_information | Tome, Astrolabe, Wand ore/structure, Jade, Solyn mark |
| equipment_augmentation | random crystal engraving, Lumen, Prism, gems, artifact |
| world_manipulation | construction, conversion, lighting, focal transformations |
| world_content | overworld structures, ores, shooting stars |
| death_respawn | Phoenix Blessing perk prevents death, no postmortem resurrection established |
| trade_economy | no independent merchant system established; knowledge scroll shares research |
| progression_tiers | Discovery then 4 altar states, attunement, 4 lumen ranks, terminal artifact |

## Instructional quest-source map
Research IDs only; not actual FTB quests. Possession cannot prove operation. Use verified native advancement candidates constellation, focal_point, attune_player, lumen, perk_level_10/20/30 when supported, otherwise manual acknowledgement; no detector invented. Tome structure previews and JEI supply routine recipes. Every lesson teaches setup/action/result; reward directions consumables/XP, never skip meaningful unlocks. Dependencies teaching rather than forced hard locks unless explicitly recipe gates.

| ID | Prerequisites | Action and observation / Tome page |
|---|---|---|
| AS01 | none | Receive/craft and open Tome, Welcome |
| AS02 | AS01 | Mine shale, marble, rock crystal safely, Ores |
| AS03 | AS01 | Obtain paper and Astrolabe, trace constellation and locate focal point at night |
| AS04 | AS02,AS03 | Focal combine Wand, detect buried ore |
| AS05 | AS03 | Focal combine first altar, activate by Wand at night |
| AS06 | AS05 | Lightwell catalyst, fluid extraction, Liquid Starlight |
| AS07 | AS06 | Infused Wood and pool interactions |
| AS08 | AS03 | Iron to Starmetal, chisel Stardust |
| AS09 | AS05,AS06 | Resonance upgrade and T2 preview |
| AS10 | AS09 | Focus Relay extra inputs and chalice fluid network |
| AS11 | AS09,AS06 | Infuser, Resonating Gem; day/night contrast |
| AS12 | AS08,AS06 | Grow/split/merge crystals, mature celestial cluster |
| AS13 | AS09,AS08 | Attunement Altar structure and constellation relay pattern |
| AS14 | AS13 | Personal major root, spend perk point, XP action |
| AS15 | AS14 | Gem maturation/socket, seal/nullifier |
| AS16 | AS14 | Shifting Star vs attuned variant reset consequences |
| AS17 | AS13 | Attune crystal, preserve properties |
| AS18 | AS03 | Convert flowers at matching focal points |
| AS19 | AS09,AS18 | Lumen Array catalyst and liquid, five primary types |
| AS20 | AS19 | Clear LOS filament chain and comparator |
| AS21 | AS19,AS08 | Crystallizer, matching catalyst and harvest |
| AS22 | AS21 | Bind/recharge tool or armor in pool |
| AS23 | AS21 | Fill Stardew, stack aspect effects |
| AS24 | AS09,AS11,AS08 | Luminance upgrade, expanded structure |
| AS25 | AS24,AS17 | Focus Crystal craft and correct matching sky column |
| AS26 | AS25 | Link, relay/split, understand purity and night |
| AS27 | AS25 | Stellar Filament pattern and local flight |
| AS28 | AS24,AS19 | Alchemy Array, directly visible feed arrays |
| AS29 | AS28 | Make Virel/Solyn/Caldor/Nullae, distinct catalysts |
| AS30 | AS29 | Make Hyle/Dynamis/Aion/Akasha, meaningful exploration gates |
| AS31 | AS30,AS21 | Advanced binding and engraving, random capped property |
| AS32 | AS09,AS12 | Ordinary crystal equipment |
| AS33 | AS24,AS17,AS32 | Iridescent tool with matching consumed focus, ability |
| AS34 | AS24,AS17,AS32 | Iridescent Sword short/full charge and attunement |
| AS35 | AS05 | Formation and Conversion Wands, consume inventory material |
| AS36 | AS09 | Traversal and Impulsion Wands, mode and hook |
| AS37 | AS05 | Illumination Wand and Cave Illuminator |
| AS38 | AS05 | Three throwable powders and darkness warning |
| AS39 | AS09 | Tree Beacon, seed/grow/capture, harvest, maintain |
| AS40 | AS09 | Celestial Gateway pair, named destination and travel |
| AS41 | AS09 | Knowledge Sharing Scroll; share research not power |
| AS42 | AS09 | Shooting star before dawn, awaken/settle artifact |
| AS43 | AS42,AS08 | Chisel shard and Akashic Singularity loot |
| AS44 | AS30,AS43 | Prismatic Lumen, artifact catalyst, amplify bound item/flask |
| AS45 | AS24,AS12,AS25,AS19 | Radiance upgrade, five lumens and Armara starlight |
| AS46 | AS24 | Resplendent Prism enchant boost, reroll tradeoff |
| AS47 | AS45,AS44,AS42 | Terminal artifact enhancement, choose enchanted item/prism/gem; no repeat |

AS44 and AS43 teaching dependencies do not require spending all artifact shards on singularity before Prismatic; retain shard for catalyst. AS46 timing teaching choice, consult JEI for exact ordinary recipe. Optional branches not prerequisites for other unrelated lessons. Native advancement tasks subject to downstream FTB capabilities; no authored quest changes.

## Handoff
All 29 axes and 47 lessons documented, meaningful primary gates resolved with exact resource evidence. No remaining ordinary native gameplay requirement. Non-gating numeric detail, precise individual perk nodes and routine recipes left to Tome/JEI or later authoring. Pack-specific questions if needed: actual TMM attribute/cheat-death stacking with Progression and Apotheosis, mixed fluid capabilities, specific protected-resource recipes, not automatic balance changes or standing gameplay tests. No actual defect established. Owner-approved major-first queue: JAWR next, focused compats deferred. No runtime, Packwiz, FTB edits, assembly, server boot or packaging.