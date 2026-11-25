# Goety — The Dark Arts 3.2.3 native capability Atlas

**Status: native research COMPLETE at owner-approved quest-source depth; source review/integration pending.** Separate from Goety Cataclysm and TMM's custom Lich/Progression rules. No native gameplay test requirement, actual FTB quest changes, recipe changes, or build. This replaces earlier 3.2.2 estimates for the current release, not the historical Comprehension record.

## Evidence
Exact Packwiz mods/goety.pw.toml, goety-3.2.3.jar, MC1.21.1 NeoForge, Modrinth 4ZVIxU8x/9DZs33ZX, owner-supplied 98,650,609 bytes, SHA512 372e59756123de38cc30a521f776f885a52f5317bbde1b95452a6be9dcc6efca8c9d717e4f534b19e21ffbc18d6d071775f3e30929de0b6f6f5e9cb6b520ae74 EXACT; 22,786 ZIP entries CRC pass; 3,604 class files. Metadata Curios >=9.5.1 required; Patchouli conditional guide, Apotheosis optional; upstream build Java21/official MC mappings/NeoForge21.1.234. [Immutable source 10e104de5c99ca37d5f6b0f2d93b48fc9a1532d2](https://github.com/Vivideru/Goety-3/tree/10e104de5c99ca37d5f6b0f2d93b48fc9a1532d2), declares 3.2.3. Five checked source blobs (Arca, transformation, Terminus recipe, Lichdom and Pyre guide) byte-identical to JAR; direct javap -c -p DarkAltarBlockEntity, SoulEnergyEvents, TotemOfSouls confirms key paths. Not a full compiled/source equivalence claim. Prior [Comprehension 3.2.2](../../mod-comprehension/mods/goety.md) and [round1](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/410#issuecomment-7140743684), [round2](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/410#issuecomment-7141179594). No vendor binary committed. Data paths below data/goety/recipe, loot_table, advancement and assets/goety/patchouli_books/black_book/en_us/entries; Java prefix com.Polarice3.Goety.

Actual singular recipe directory: 1667 records (697 shaped,109 shapeless,327 stonecutting,361 ritual,82 pulverize,31 cursed infuser,22 brewing,15 smelting,8 soul absorber,5 cauldron,5 brazier,1 cauldron_sus,1 campfire,1 smoking,1 blasting,1 scroll cloning). Do not count obsolete plural recipe duplicates as new recipes. Conditional records not all active in TMM. English Black Book 414 entries: crafts150, focuses124, intro8, mutations23, rituals18, rumors8, summons42, world41; Witch's Brew8. Entry count not unique spells. Whole category census and meaningful progression gates reviewed; routine JEI recipes and nonessential coefficients not reproduced.

## First steps, soul economy and components
Craft Black Book with vanilla Book and goety:black_book_craft tag (emerald/Ectoplasm/Shade Stone per earlier profile; check live JEI); starterBook default false. First Steps: Ectoplasm from Wraiths/loot, 2 lapis+4 wooden rods+1 Ectoplasm→Totem of Roots. Carry totem while killing eligible mobs. TotemFinder selects offhand first, else first index0..9, Curio fallback; one container, not combined capacities. Roots100 (earlier /100 registration; current same TotemOfSouls class takes IntSupplier; exact registration value minor remaining reference), Totem of Souls10000 and Arca100000 configurable defaults; soul multipler2. Player-attributed kills excluding FakePlayer, owned summons and most nonenemy babies; per-mob soul_taken data and classifications, weapon Soul Eater boosts, servant kill attribution conditional. Do not treat every dead mob as a harvest. Arca mode overrides totems; personal storage, not a shared block FE battery or MXP. Soul container charge item data. Charged totem as crafting ingredient retains/debits according to recipe/config; four Empty Foci from four amethyst +totem or iron/quartz/redstone alternative. Magical Fabrics/Metal/Magic Emerald with charged totem or Cursed Infuser. Totem of Souls 2 Magic Emerald,4 gold,2 lapis,Soul Lantern. Soul Emerald 8 Magic Emerald around Empty Focus. 
Cursed Infuser directly ABOVE vanilla spawner, one slot, uses spawner energy without soul debit; processing stops/decays if missing, result dropped as item, insertion but no extraction; 31 current recipes. Basic amethyst→Empty Focus, Bone Block→Savage Tooth, Emerald→Magic Emerald, iron→Cursed Ingot, Spore Blossom→Corpse Blossom. Calcite→Marble, sandstone→Siltstone, steepable wood→Steep Wood, stone→Shade Stone, sapling→Haunted Sapling; iron block→Cursed Metal Block, emerald block→Awakened Emerald Block. Eight reversible garment style pairs; conditional Caverns&Chasms terrain pair not assumed active. Grim Infuser Haunting gate, Obsidian→Crypt Stone (grim-only). Earlier timing 20/60/540 ticks not necessary for teaching. Cursed Cage 8 Cursed Bars ring; insert totem, powers machine above, not a generic energy cable. Transfer Gem magic emerald/ender pearl/redstone/soul-fire-base recipe; bind by using on owned Arca, put into cage, draws owner's pool. Soul Candlestick atop filled cage supplies nearby machines/brewing in default8 radius; multiple increase throughput. 
Soul Absorber after Arca: old Totem of Souls drained into personal pool, consumable item recipes Ectoplasm5, Sculk5, Sensor10, Shrieker25, soul-fire-base blocks25, Totem of Undying200; Born in Chaos Ethereal Spirit5 and Quark Soul Bead5 conditional on mod ID. Separate Sculk Devourer XP→souls 1:1, Soul Eater raises yield, Radius expands area; not FE. Ice Bouquet Trap redstone unflames harm mobs and send souls to owner; Black Crystal drains nonallied entities until low HP (Wight loot gate). Soul harvest/XP integration to reconcile with TMM MXP, no balance decisions.

## Wands, spell schools, equipment
Dark Wand from Magic Fabric, Empty Focus, lapis/planks. Hold wand and default Z Open Wand Focus Slot, insert selected focus, hold/release use as spell requires, observe soul charge/cooldown. Focus Bag10/Focus Pack20 foci only, waist Curio, default C bag and X circle; binding system in 3.2.3 source, do not equate with generic storage backpack. Focus enchantments (not all applicable): Potency damage/mining tier; duration; radius; burning; range +2 per level; life leech incompatible Potency; direct inventory for mined blocks; projectile velocity. Enchanted focus casting doubles energy per guide; actual per-spell computation and discounts apply. Teach JEI/guide before enchanting; specialized staff makes same focus stronger/different, no universal new spell system.
| School | Native focus roster (guide entry stems; not exact registry naming for every focus) | Major uses and staff |
|---|---|---|
| General | arrow rain, biting, bulwark, command, corruption, crafting, feasting, ignite, illusion, iron hide, magic bolt, order, shockwave, shredding, sonic boom, soul bolt, soul heal, soul light, sword, teeth, telekinesis, vexing, weakening | direct combat/shields/healing/light, remote crafting, move entities/items, mirror clones, command. Ominous Staff from Minister orb boosts specified foci/Vexing and undead illager variants |
| Necromancy | blackguard, ghost fire, killing, leeching, osseous, phantasm, reaping, rotting, skull, spooky, vanguard | undead summons, soul/healing/offense; Necro Staff extras; Nameless Staff forbidden→Necro Bolts and mirror synergy; Front gates blackguard/vanguard; Killing Focus Apostle blood |
| Geomancy | barricade, burrowing, earth punch, eruption, ministrous, pulverize, quaking, rotation, scatter, sensing, smack stone, temblor clap | mining, conversion, detection, construction/control; Geo Staff 3x3 pulverize and burrowing; Potency unlocks harder mining tiers; not unrestricted protected-block bypass |
| Frost | blizzard, chilling, frostborn, frost breath, frost nova, hail, iceology, ice spike, ice storm | freezing/offense, ice servants, Frost Staff |
| Storm | bolting, charge, discharge, electrocute, lightning, monsoon, shocking, sprightly, surging, thunderbolt, thunderstorm | shock/lightning/weather, Storm Staff chains nonallied targets and bypasses rods |
| Abyss | biomine, bouncy bubble, guardian, prisma beam, steaming, tidal, trident storm, water jet, water whip | water combat, guardians and underwater utility, Abyss Staff |
| Wild | blossoming, carrion, entangling, grapple, hunting, leaping, mauling, overgrowth, poison dart, slimy, swarm, whispering | plant/animal summons, vines/kelp barriers or poisonous ranged, movement; Wild Staff stronger grapple and fall protection; Floral gates leaping/whispering |
| Void | banish, blasting, blink, call, ender chest, end walk, recall, rupture, snaring, stellar, troop, tunnel, void flash, watching | remote Ender Chest, invisibility with interaction return, bind recall to bed/anchor/Arca, short tunnel3x3→5x5 with Void Staff, teleport servant/call type; End gates |
| Nether | blazing, bombardment, fireball, fire blast, fire breath, flame strike, ghastly, hogging, lavabomb, magma bomb, meteor shower, wither skull | fire/hellfire and Nether creatures, Bygone Nether Staff/blazing, Apostle blood Wither Skull |
| Wind | breeze, cushion, cyclone, flying, launch, razor wind, trembling, updraft, whirlwind, wind blast | flight/control/launch, Flying Focus requires Elytra-derived progress (guide advancement minecraft:end/elytra); Mistral Wind Staff faster flight |

Necro Staff necroturgy Dark Wand +2 Ectoplasm+4 Cursed Ingots+lapis block+Spirit Fabric. Staff variants 11 including Ominous, Nameless, Necro, Geo, Frost, Storm, Abyss, Wild, Void, Nether, Wind. Nameless actual ritual requires Forbidden and Heart of Night, Necro Staff input. Guide locks distinct from recipe locks (Minister kill→Ominous orb, etc). Dark Robe cost discount, hat/crown faster cast. Necro Crown preserves undead and halves casting time, daylight Weakness; Cape daylight Hunger, allied skeleton arrows pass through and zombies convert wild ones. Nameless set Forbidden avoids sunlight garment debuffs. Matching elemental robes/crowns boost family and servant upkeep. Witch Hat chance not consume potion; Witch Robes portable brewing stand V, magic resist, neutral witches/warlocks and emerald barter with full set. Warlock garments Wartful Egg/Snap Fungi benefits, auto consume egg on debuff. Grand Set Vizier Ruby, Irk defensive spawns and healing by crouch Vexing sacrifice, shield while casting; Unholy set Pure blood from Nether Apostle, fire/Burn Hex protection/hellfire and Nether defense. Black Iron→Dark Alloy forged equipment, Cursed Knight→Cursed Paladin armor, Dark Anvil, Scythe→Dark Metal→Death Scythe (Unholy Blood). Rings of Force/Forge/Thirst/Want/Wrecking, charm/amulet/monocle/belt branches modify attacks, summons, movement, utility, healing; consult respective guide/JEI for exact slot and ordinary ingredients. Philosopher's Stone is GOETY item (not ProjectE) after Apostle, duplicates raw gold at durability cost, repairs using souls; no blanket EMC equivalence.

## Dark Altar — native ritual schools
Place charged Cursed Cage below altar, pedestals first then altar activation item. Required blocks in radius8 default, not fixed layout; sneak empty-hand toggles range. 361 ritual recipes include craft, enchant, summon, sacrifice and convert; preserve exact JEI item order/requirements, not all listed entities sacrificed. Source DarkAltarBlockEntity and compiled debit: every valid progress second (gameTime%20) deduct soulCost and increment currentTime; duration is SECONDS, not ticks. Total generally cost×duration, enchant can extend until XP paid; enchant XP debit each10 ticks when level>1; invalid/insufficient souls stops, missing caster/sacrifice or pending pauses, missing structure/conversion60 ticks abort; no guaranteed refund. 
| School | Meaningful environmental conditions from exact guide |
|---|---|
| Basic magic | 16 enchant power/bookshelves, enchanting table, lectern with book |
| Necroturgy | 16 nonwood slabs,16 sculk or Grave Soil,8 occupied flowerpots, night; soil can decay to Dark Dirt |
| Divination | Crystal Ball,16 lit candles, skeleton skull |
| Animation | 15 ladders,15 rails, carved pumpkin |
| Geoturgy | y<=32 under cover OR smithing table,8 amethyst blocks,16 deepslate |
| Frost | snowing biome OR16 ice,8 snow blocks,4 Freezing Lamps |
| Overgrown | jungle OR32 leaves,16 Overgrown Roots,16 mossy blocks |
| Deep | deep ocean below sea level OR4 sea lanterns,16 prismarine,16 granite; altar waterlogged |
| Sky | y>=128 OR8 marble,16 jade tiles,4 indented gold |
| Storm | sky requirements + thunderstorm exposure,4 lightning rods,20 chains,12 copper blocks |
| End | End dimension,16 Void Blocks,64 End Stone variants,32 Purpur |
| Adept Nether | Nether,8 basalt,16 blackstone,4 glowstone |
| Expert Nether | Nether,4 wither skulls,32 nether bricks,8 nether wart |
| Forge | lava cauldron,2 furnace/blast furnace, anvil |
| Teleport | bound Waystone same dimension, ritual-specific items |
| Enchanting | exact recipe, souls and caster levels |
| Sabbath |8 crying obsidian,16 obsidian,4 soul fires; Apostle summon |
Use JEI recipe and actual RitualRequirements for exact tag recognition; guide describes intended alternatives. General magic altar and ritual families teach optional stations, not mandatory sequence.

## Research unlocks, encounters and loot
ResearchScroll.use grants personal flag, consume first use/advancement, duplicate use criterion without consuming; possession isn't research. Black Book secret and advancement pages sometimes unlock after acquiring/defeating item, teach prerequisites first. Actual 10 flags and significant recipe locks from JAR:
| Scroll | Acquisition route | Native recipe/use gate |
|---|---|---|
| Buried | desert pyramid injected loot | 5 necromancer conversions |
| Front | desert pyramid injected loot | Blackguard/Vanguard focus & thrall, military training |
| Floral | jungle temple injected loot | Leaping/Whispering |
| Ravaging | mansion injected loot, Vizier loot | Ravaged, Trampler, Ripper, zombified Ravager and armor |
| Warred | ancient city, stronghold library injected loot | Terminus deciphering, construct progression |
| Haunting | crypt book/tomb/rich crypt loot | Grim Infuser, Haunted Armor Stand/Mirror, Black Beast, 11 plinth variants |
| Mistral | shrine treasure/vault | Resonance Crystal, wind set/staff, wind/storm military training |
| Bygone | Withered Manuscript from Wither Necromancer, expert Nether ritual | Nether set/staff, Blazing Cage/Focus, Wildfire, Blaze/Wither Skeleton thralls |
| Terminus | Ender Keeper shrine yields Shrouded Blueprint; Warred Forge decipher | Monstrous Core, reinforced redstone |
| Forbidden |9 Forbidden Fragments | Heart of Night, Nameless set/staff, Potion of Transformation |
Exact injected loot tables show desert rolls equal weights Buried/Front/empty, Jungle and stronghold library half entries, not guarantee per chest or current pack placement. Combined advancement read_buried_and_bygone, read_terminus_and_haunting, read_warred_and_haunting and unlock_necromancer; don't conflate guide conditions and hard locks. Blazing Horn craft Goat Horn,4 Blaze Powder,2 netherite scrap,2 gold; use within Nether Fortress to call Wither Necromancer per guide. Bygone ritual manuscript activation+2 Ectoplasm+soul-fire base+paper+Blaze Rod,1 soul/s30s. Terminus Warred+Shrouded Blueprint activation, Redstone Golem Skull+paper+Ectoplasm, Forge1/s30s. World: Graveyards/Buried Ritual/Blighted Shack, Dark Manor, Sorcerous Keep, Ominous Blacksmith, Crypts (trial spawners/vaults), Black Assembly, Wind Shrine, End shrine; exploration, rare components and hostile encounters. Wraith Ectoplasm, Wight Shadow Essence (hunts high-soul owners), Minister orb, Vizier Ruby and Ravaging, Skull Lord crypt jar and loot (head/body, Fell Blade upgrade), Ender Keeper Void Echo/blueprint, Wither Necromancer manuscript. Guide rumor crypt jar/Respawn Anchor or crystal ball at original location suggests rematch; don't assert uninspected exact repeat-summon predicate. 
Apostle: Sabbath ritual Netherite Ingot activation, Crone Hat/Malefic Helm/Warlock Sash/Infernal Tome,1 soul/s30s; alternate eight goety:sabbath_ritual tag offerings,1/s10s. Boss phase change half HP, uses fire/hellfire, summons, heals via arrow hits, Smite suppresses healing. Guide recommends outside Nether; Pure blood custom_data obtained if slain in Nether, actual pure ingredient for Unholy Fabric expert Nether. Normal blood adequate transformation, Killing/Death Scythe/Philosopher's Stone. Distinguish vanilla Netherite vs custom pack gates. No need boss numerical exhaustive audit.

## Servants and military progression
Temporary focus summons have species caps and lifespan, matching crown may make persistent; ritual thralls explicitly permanent, not all pets immortal. Matching robe/Necro Cape supports soul healing, no fire; helmets prevent undead sun burning. Follow/wander/stand wand hit (config right-use option, except Vex); crouch cast corresponding focus recalls. Command Focus selects one via hit, target block/mob moves/mount, sneak-use air clear; Order8; Call records specific servant, Troop species; Grimoire of Goodwill/Grudges controls ally/enemy rules; dismiss/recall and ownership distinct. 
Rotting→Zombie/Husk desert exposed sky/Drowned underwater/Zombie Villager village/Frozen cold or frost/Jungle wild/Frayed storm/mansion zombie Vindicator/Nether zombified Piglin/bastion Brute. Swords/axes/armor. Osseous→Skeleton/Stray/Mossy/Sunken harpoon/Rattled/Outpost Skeleton Pillager, bows/swords/armor; Wither Skeleton requires Bygone ritual. Front blackguard shield repair iron ingot/vanguard. Bound Evoker/Geomancer/Iceologer/Cryologer/Wind Caller/Storm Caster (guide Evoker kill), heal Ectoplasm, can lead. Buried necromancer conversion actual Necro Cape activation, Necro Staff+Crown+Ectoplasm+Rotting Focus,150 souls30s, target skeleton tag; alternate Cairn/Mossy/Drowned/Wither variants; accepts new undead foci, empty-hand summon, Empty Soul Jar capture on death and use on skeleton to restore, another jar boosts per guide (not numerical cap audited).
Other 42 summoning pages cover Black Wolves/Wargs/Black Beast, Bear, Hoglin, spiders/Brood Mother, slimes, carrion flies, gnashers, leapleaves, quick vines, snappers, sprites, whisperers, wartlings, twilight goats, Blaze/Breeze, Mini Ghasts, Phantom/Vex/Wraith/Haunted Skulls/Reaper, guardian, enderlings, doppelganger, vanguard/blackguard, Flame Construct/Grave/Ice/Squall/Redstone golems, Grave Monstrosity, Ministrosities/Redstone Cube/Monstrosity, summon storm. Teach family-specific actual spell/ritual/animation and cap/armor/upkeep; guides list material/biome per form, not arbitrary permanence. Mutation branches include Wildfire, horse undead, elder guardian, zombified ravager, ravaged/trampler/ripper, witches/heretics/reprobates/mavericks/warlocks, hurricane; use conversion target tags. Howling Totem raw meat+bone→2 permanent wolves (hopper supported), crypt→skeleton wolf, cold→winter, storm→stormhound, Nether→hellhound; Waystone bind rescue, filled cage below100 souls, wounded cooldown; wolf→Warg progression, matching Cursed/Dark armor (no interchangeable harness).
Illager society: convert Villager→Neollager; purple robe magic talent. Ominous Pyre forge campfire+4 Ominous Stone Bricks+4 Magic Emerald,150 souls30s. Command/Order to train,8 trainees, >10 blocks/removal stops, same-role neighbors and babies faster. Pillager2 hay/target+2 pumpkins+16 fences+20 wool; Signaler, Vindicator, Vindicator Chef via4 smokers/furnaces+16 barrels+2 cakes, Mountaineer snow, Crusher8 blast furnaces+2 lava+2 water cauldrons+4 anvils+smithing; Front advanced soldiers/guards, magic Neollager→Evoker/Enchanter/Geomancer/Iceologer, Cryologer snow, Mistral Wind Caller/Storm Caster. Use exact Pyre guide role requirements. Chef cooks/crafts food, Crusher ore smelting, replacement/upgrade weapons, Repair Putty golems; Signaler alerts guards. Food healing/breeding when full health/noncombat/not following/beds; share food, baby Neollager. Register banner via wand on placed banner without block focus, give captain, command followers; Taglock target hunt, Waystone loaded village+horn raid, Ceasefire holding banner or Goodwill villagers. Return loot, food and chest via Waystone; Raiding Chest deposit only. Ominous Shackles capture mode, shears cancel, Prisoner pickaxe mines nearby existing ore while not following, hunger/durability/inventory limit; supplies/drop chest, stick removes pickaxe, sneak-use frees. Not infinite ore generation; real pack protected-ore interaction must be evaluated separately. Gear, totem offhand and charms; stick remove. 

## Witch's Brew — independent guide
Craft Book +2 mushrooms. Witch Cauldron 7 Cursed Ingots, heat campfire/magma or other supported heat, water, Soul Candlesticks on filled Cages nearby. Boline harvest grass/fern for Henbane/Nightshade seeds. Cauldron Ladle completion; sneak-use wand clears failed mixture. Nether Wart first, capacity→catalyst→augmentation→completion IN ORDER, black smoke fail. Base capacity4, ordered Crimson Fungus+2, Snap Warts+2, Awakened Emerald+2, Concentrated Emerald+2, Ruby of Souls+4. Catalyst ingredients give effects and consume capacity; some need boiling target mob instead of spawn egg; 22 brewing records separate from effect registry. Duration Redstone→Prismarine Block→Chorus Flower; amplify Glowstone→Blaze Rod→Mystic Core; area Charcoal→Firework Star→Tall Skull; lingering roots/dripleaf/blossom; drink speed honey/lichen/egg; throw speed snowball/bow/crossbow; Soul Energy multipliers per ingredient. Gunpowder splash→Dragon Breath lingering→Wind Core gaseous; Eye of Ender hides particles, Tube Coral water immunity, netherite scrap lava/fire protection. Sneak look at cauldron for required power, ladle right-use starts completion, wait effect particles, bottle; Witch attire and black cat bonus yield, apples accept brews. Swamp reduces cost, up to3 Witch Poles within8 reduce cost/increase remote reach; full cauldron Taglock or Waystone,3 poles same-dimension distant application per guide. Brew Bag throwable only H inventory/J circle. Five nonbrew cauldron recipes/one suspicious recipe: Nightshade instead of Henbane for lotions, age changing cream, Flying Ointment (broom synergy), stew, candles. Meaningful effects: custom potion buffs/debuffs, growth/breeding, teleport/flight/air jump, mining Fortune/pulverize, life leech, damage/fire/ice/webs/void, entity displacement, disarm, potion cleansing, extinguish, water/lava paths, blocks/crop transforms, persistent effects (echo shard); exact JEI catalyst/limits rather than assume all effects unconditional. Do not infer compatibility with Hunter mutagen whitelist.

## Artifices and automation
| Device | How to use / meaningful gate |
|---|---|
| Arca | personal soul storage/return, see lichdom |
| Animator | animation core advancement; cage below, bound Waystone to valid construct location, redstone, distance-based souls |
| Apparition Door | hand or redstone intangible, adjacent chain |
| Black Crystal | Wight kill/essence, drains nonallies to low HP |
| Blazing Cage | Bygone, magma block input spawns Blaze servant, wand toggles enemy-only and guard/follow |
| Crystal Ball | Waystone scry loaded location or Taglock view victim, same dimension within1024, target type/proximity limits |
| Dark Anvil | Dark Alloy gate, cage below repairs self; increases repair penalty by1 instead of doubling, caps cost rather than too expensive |
| Freezing Lamp | freezes area, redstone OFF, frost gate |
| Haunted Glass | allies pass, others/items blocked; invert; tinted version |
| Haunted Jug | infinite water bucket/bottle/pipe; nearby fills/extinguishes, wand lid toggle |
| Haunted Mirror | Haunting, aligned pairs horizontal <=32 same height facing away or vertical same XZ <=16 same facing; travel and cosmetic dye |
| Hook Bell | hand/projectile rings, brings distant raiders near bell; no redstone |
| Howling Totem | permanent hounds and rescue above |
| Ice Bouquet Trap | Wraith gate, redstone soul-sapping flame |
| Necrotic Brazier | Deep Dark only green flame, dropped/placed ingredients, soul-powered craft, not FE |
| Night Beacon | rare ancient replica, eternal night per guide; actual activation/pack policy separate |
| Ominous Decree | record target block and servant type via hits or Waystone/Taglock, nearby guard assignment |
| Ominous Idol | Evoker gate, Waystone bind up to16 servants by default, same dimension fatal save, wounded and one Minecraft day lockout |
| Ominous Pyre | role training above |
| Cursed Plinth | Haunting, Waystone or guard on plate; statue invulnerable, heals/repairs2%/s, wake4 blocks +6/Radius level, return5s after no target; small1, larger3x3, Monstrosity5x5 |
| Polar Crystal | spell hit toggles redstone, beam counts once, attach to face |
| Resonance Crystal | Mistral, wind-powered, stores Squall Golem, linked crystals via Waystone |
| Sculk Converter | Sculk Catalyst gate, cage supplies soul→sculk, redstone, Potency; no native XP-to-sculk on death after conversion |
| Sculk Relay | redirects Converter to closest relay, Silk Touch hoe harvest |
| Sculk Devourer | XP drops→souls, Radius/Soul Eater |
| Sculk Grower | cage above, redstone bulk soul→growth charges, default8x4x8 crops, Radius |
| Sealer | Animator gate, Waystone chest/Apparition Door within32 same dim, signal locks opening/mining/explosion/spells; cage below1 soul/min, structure ones free; power loss unseals |
| Shade Gravestone | iron nugget→zombie, environmental variant, wand guard/follow and conditional spawning |
| Shade Ossuary | bone meal→skeleton, environmental variant, same controls |
| Shriek Obelisk | cage below, skull levels/tall skull perimeter expands anti-Illager assault radius |
| Soul Absorber | Arca, drain totems and defined consumables |
| Soul Candlestick | cage below, distribute to nearby multi-source appliances |
| Soul Menderer | Dark Alloy guide gate, cage below, repair tool/weapon |
| Spider Nest | meat via hand/hopper, variant environment, guard/follow/enemy toggle |
| Wind Blower | redstone strength→push, crouch resists; wind crystal network |
Additional ritual and spell appliances use exact JEI, no generic FE link established. Cursed Infuser uses spawner rather than soul storage. Portable Ender Chest access is vanilla ender inventory, not new mass storage. 

## Independent native Lich walkthrough (before secret book entry unlocks)
1. Build core Soul Energy and Forge infrastructure; construct Arca: Cursed Cage activation + Recovery Compass, Soul Emerald, Respawn Anchor, Ender Pearl tag, Dark Prismarine, Lodestone,2 Obsidian-tag. 30 souls (1/s×30). Place owner-bound, one active; another replaces previous, empty-hand use inactive owned Arca selects, sneak-use breaks. Broken Arca drains souls/Soul Hunger. Bind Transfer Gem and insert in cage under altar. Safe return area and optional Waystone bound within8 for revive pos. Prepare at least60k souls to fund transformation plus10k recommended safety reserve; max100k default. 
2. Collect nine Forbidden Fragments. Rich crypt chest has guaranteed fragment pool, other graveyard/ancient city/necromancer loot gives pieces, rare structure-spawned spellcaster direct fragments per earlier audited event; archaeology modifier declared2.5% but exact multi-condition behavior not required. Repeatable Necroturgy Forbidden Piece: paper activation, Ectoplasm+soul-fire-base+Echo Shard+Wither Skeleton Skull, 5/s10s=50. Nine pieces→fragment, nine fragments→scroll (81 pieces); use Scroll to learn Forbidden, not just carry. 
3. Obtain Apostle Unholy Blood via Sabbath encounter; gather 11 other transformation items: Wither Rose, Honey Bottle, Echo Shard, Totem of Undying, End Crystal, Nether Wart, Ectoplasm, Enchanted Golden Apple, Nightshade Blossom, Fermented Spider Eye, Glowstone.
4. Necroturgy at NIGHT, filled cage connected to Arca (60,000 souls), pedestals twelve ingredients then Glass Bottle altar activation; actual goety:villagers sacrifice; 60 seconds,1,000/s. Complete Potion of Transformation. Keep additional souls; ordinary portable max10k not enough alone.
5. Drink at night moon brightness>0.9, active SE/non-null bound Arca, LichEnable true. UndeathPotionItem sets lichdom and lichMode BEFORE applying50 wither damage; failure also applies50 without lich state and consumes potion. Don't assume damage unavoidable when external immunities exist. Native become_lich reveals secret On Lichdom guide; teach N cosmetic form before relying on hidden guide. Already lich potion heals20.
6. Physiology: food stable per guide, poison/regen/hunger/saturation immunity, inverted instant heal/harm, no drowning, night vision, undead neutral, sunlight burns unless helmet or fire resistance, helmet damage, Smite, villager hostility. Heal1 HP for5 souls per configured1s (tick modulo21 default), unless on fire without immunity/smited. Default no blanket magic85% resistance. Form N appearance not cure. Haunted Mirror changes appearance.
7. Fatal save: SoulEnergyEvents exact source and compiled flow. Nonlich active Arca with>=10k saves, consumes10k; health1, clear effects, RegenII45s, AbsorptionII5s, FireRes40s. Lich default ArcaUndying/ArcaLichUndying true, LichArcaRemove true, **LichNoSERemove false**, contrary guide: active nonnull Arca saves even if below10k, debit10k (clamped by pool), low souls Soul Hunger amplifier4 2min, high souls Absorption/FireRes but no normal Regen. If LichNoSERemove true and underfunded, loses lich instead. Missing active Arca on actual death can remove lich. SEHelper teleport attempts safe stand-up same/cross dimension, world border, caller ignores false result: don't guarantee relocation if blocked. Portable full global10k Totem death prevention drains to0, Roots full100 not sufficient. TMM shared death and multiple lich providers separate authoring and integration; native default can differ from intended balance.

## 29-axis crosswalk
| Axis | Native capability |
|---|---|
| gear | wand/staff sets, Dark Alloy/Scythes, robes/crowns, charms |
| movement | launch/flying/grapple/blink/recall/tunnel, Mirror/Waystone |
| ore_processing | pulverize, crusher smelt, infuser metals |
| storage | focus bag/pack, brew bag, remote Ender Chest, totems/Arca (energy) |
| crafting | charged recipes, 361 rituals, infuser, cauldron, brazier |
| logistics | Transfer Gem, candlesticks, servant chests, Haunted Jug pipes |
| buffs | brew, robes/charms, enchants, Enchanters, foci |
| external_compatibility | Curios/Patchouli, conditional loot/recipes, no blanket FE bridge |
| mob_farming | summon nests, totem, breeding, soul trap, owned summons excluded from ordinary harvest |
| ore_generation | Prisoner existing-ore mining, gold doubling via own Philosopher Stone; no generic ore generation |
| permanent_power | research scroll flags, lich state, Arca mode |
| general_generation | kills→souls, XP devourer, absorber, water jug, crops |
| direct_damage | 9 themed schools plus general foci, weapons, servants |
| block_breaking | Burrowing/Pulverize, mining brews, prisoner, tunnel |
| permanent_pets | ritual thralls, matching crown persistence, wolves, trained Illagers, constructs |
| temporary_pets | ordinary focus summons, Vex/Irk, storm and vines |
| defense | ward/bulwark/iron hide, armor, Plinth/Sealer/Obelisk, idol |
| healing_recovery | Soul Heal, robes, lich healing, Menderer, putty |
| debuff_control | potions/cleansing, frost/shock, Grimoire allegiance, Smite healing block |
| energy | soul container/player reserve, cage/transfer/candlestick, cost/sec |
| automation_control | Animator, Polar Crystal, Sculk machines, nests/hopper, redstone |
| agriculture | Sculk Grower, brews, plants, breeding, Chef |
| detection_information | scry/Taglock, sensing, guide, research, Signaler |
| equipment_augmentation | enchanted foci, Dark Anvil, ritual enchanting, upgraded staffs/armor |
| world_manipulation | freeze, pulverize, Night Beacon, teleport, wall tunnel, winds |
| world_content | structures, cultists/Illagers, bosses, crypts, shrine, Nether/End |
| death_respawn | Totem and Arca, Ominous Idol/Howling rescue, soul jar |
| trade_economy | witch/warlock barter, raiding loot, servant crafting |
| progression_tiers | early totem→foci/infuser→rituals→scroll specialties→Arca/lich/construct |

## Quest-source map — 76 instructional nodes
Teaching graph not forced global linear gates; ordinary recipes JEI; actual advancement/research/current state or manual acknowledgement when no verified detector, possession isn't operating proof. Group optional branches; no need complete every element before lichdom.
| ID | Prerequisites | Action and observed result |
|---|---|---|
| GO01 | none | Black Book acquisition, read First Steps |
| GO02 | GO01 | Ectoplasm source, Totem Roots, kill and soul meter |
| GO03 | GO02 | Empty Focus & Magic Fabric, charged recipe remainder |
| GO04 | GO03 | Dark Wand, Z slot/use/cooldown |
| GO05 | GO02 | Cursed Infuser over spawner, component pickup |
| GO06 | GO05 | Magic Emerald, Cursed Ingot, Totem of Souls |
| GO07 | GO04 | Focus Bag/Pack, X/C and binding |
| GO08 | GO06 | Cursed Cage, embed charged totem |
| GO09 | GO08 | Dark Altar/Pedestals, range and activation order |
| GO10 | GO09 | Basic magic and enchant-cost distinction |
| GO11 | GO09 | Forge structure and Dark Alloy gear |
| GO12 | GO09 | Necroturgy structure, night and Grave Soil |
| GO13 | GO09 | Divination, Crystal Ball, bound Waystone/Taglock |
| GO14 | GO09 | Geoturgy, Burrowing and Pulverize |
| GO15 | GO09 | Frost requirements, Freezing Lamp, foci/staff |
| GO16 | GO09 | Storm and Sky, lightning and Wind branch |
| GO17 | GO09 | Deep/waterlogged altar, Abyss staff and foci |
| GO18 | GO09 | Overgrown, Wild staff, vines/grapple |
| GO19 | GO09 | End ritual, Void staff/teleport/tunnel |
| GO20 | GO09 | Adept/Expert Nether structures and fire foci |
| GO21 | GO09 | Animation school, Animation Core construct |
| GO22 | GO10 | Enchant focus, Potency/Radius/cost tradeoff |
| GO23 | GO04 | General healing/defense/light/telekinesis/crafting |
| GO24 | GO19 | Ender Chest, Recall, End Walk and Tunnel |
| GO25 | GO16 | Launch and later Flying Focus/Elytra |
| GO26 | GO12 | Rotting/Osseous, compare temporary/ritual permanence |
| GO27 | GO26 | Necro Staff, Crown/Cape and servant healing |
| GO28 | GO26 | Equip variants, sun/biome/staff conditions |
| GO29 | GO26 | Command/Order, stance, recall, Call/Troop |
| GO30 | GO01 | Desert scrolls Buried/Front; use/read |
| GO31 | GO30,GO27 | Necromancer conversion, teach foci, Soul Jar |
| GO32 | GO30,GO26 | Blackguard/Vanguard focus & permanent thrall |
| GO33 | GO01 | Jungle Floral, use scroll then spell |
| GO34 | GO01 | Mansion/Vizier Ravaging, use scroll |
| GO35 | GO34 | Ravager/Trampler/Ripper and equipment |
| GO36 | GO01 | Crypts, Haunting scroll, Grim Infuser |
| GO37 | GO36 | Haunted Armor/Mirror/Plinth; align and guard |
| GO38 | GO01 | Wind Shrine Mistral, Wind Staff/Crystal |
| GO39 | GO01 | Ancient City/stronghold Warred |
| GO40 | GO20 | Blazing Horn in fortress, Wither Necromancer manuscript |
| GO41 | GO40 | Expert Nether Bygone decipher, read |
| GO42 | GO41 | Nether equipment, Wither Skeleton/Blaze/Wildfire |
| GO43 | GO19 | Ender Keeper encounter, blueprint/Void Echo |
| GO44 | GO43,GO39 | Forge Terminus decipher and read; Monstrous Core |
| GO45 | GO21,GO44 | Advanced constructs/Redstone Monstrosity |
| GO46 | GO29,GO09 | Convert Villager→Neollager; Ominous Pyre |
| GO47 | GO46 | Train martial vs magic roles, requirements |
| GO48 | GO47 | Chef/Crusher food, chest and repair |
| GO49 | GO47 | Banner captain, followers, guard/raid/ceasefire |
| GO50 | GO49 | Goodwill/Grudges, capture/free, supplies/loot chest |
| GO51 | GO50 | Prisoner mining actual ores, hunger and tools |
| GO52 | GO26 | Nest/Gravestone/Ossuary and Howling Totem |
| GO53 | GO52 | Warg/Black Beast, armor and canine rescue |
| GO54 | GO04 | Witch/Warlock garments, barter, portable stand |
| GO55 | GO08 | Witch's Brew book, cauldron, ladle, candlesticks |
| GO56 | GO55 | Boline seeds and heat, ordered capacity/catalyst |
| GO57 | GO56 | Augmentation tiers, ladle, bottling, failed mix clear |
| GO58 | GO57 | Splash/lingering/gaseous, Brew Bag and apples |
| GO59 | GO57 | Pole/swamp/cat station, Taglock/Waystone delivery |
| GO60 | GO55 | Nightshade ointment/stew/candles and broom |
| GO61 | GO11 | Arca ritual, bind Transfer Gem, replace totem |
| GO62 | GO61 | Soul Absorber/Devourer resource transfer |
| GO63 | GO62 | Sculk Converter/Relay/Grower, redstone |
| GO64 | GO11 | Dark Anvil/Menderer, cage-fed repair |
| GO65 | GO21 | Animator, Waystone construction, Sealer |
| GO66 | GO16 | Wind Blower/Polar Crystal, spell redstone |
| GO67 | GO09 | Haunted Jug/Glass/Apparition Door, automation |
| GO68 | GO26 | Ominous Idol/Decree, soul traps/Obelisk |
| GO69 | GO12 | Forbidden Pieces/Fragments exploration/ritual; consume Scroll |
| GO70 | GO69,GO27 | Nameless staff/set and Heart of Night |
| GO71 | GO20,GO09 | Sabbath ritual and Apostle; Unholy Blood |
| GO72 | GO71 | Pure blood optional Nether encounter, Unholy set/Scythe/gold stone |
| GO73 | GO61,GO69,GO71 | gather12 ingredients, villager sacrifice,60k souls |
| GO74 | GO73 | full-moon night with active Arca, Potion of Transformation |
| GO75 | GO74 | Lich physiology, form, heal and safe Arca revival explanation |
| GO76 | GO75 | native death failure/low reserve/config distinction; no forced lethal test |

GO45 depends on late Monstrous Core only for terminal variant; lesser golems earlier. GO53 Black Beast needs Haunting; optional Warg branch earlier. GO60 use applicable guide recipes. GO68 Evoker/Wraith and Sculk gates as appropriate; split into independent actual quest nodes. GO69 may use crypt route or repeatable piece ritual; not forced boss beyond materials. GO73 explicitly teach cage connection and 60k cost, full moon after recipe. Actual TMM transformed player features and advancement hooks owned by Progression. 
## Handoff
All 29 axes, 76 source lessons, independent Lich how-to, scroll/ritual/brewing gates and machine families complete to agreed depth. No ordinary native test prerequisite. Nonessential per-spell damage, every enchant eligibility, rare drop probabilities and exact rematch predicates intentionally not transcribed; distinguish guide and recipe from code. Remaining pack-specific questions: native renewable underfunded Arca vs TMM death handler, Sculk/XP soul economy and actual servant ore mining protection, Goety own Philosopher's Stone vs ProjectE gating (not same item), crossmod blood/lich state, Curio restrictions. These are not native research blockers. No design decisions or changes made. Next major target Mahou Tsukai; focused Cataclysm addon deferred. JAWR #664 currently shares README/intake ownership and is unmerged: this independent dossier PR intentionally changes only this file, record completion in #410 and reconcile index/intake after #664 lands, not competing changes.