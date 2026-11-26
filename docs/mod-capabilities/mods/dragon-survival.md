# Dragon Survival 2.0.74 — Mod Capability Atlas

**Status: quest-source-ready for the documented native capability scope, pass 3 (2026-11-25).** Evidence-only research, tracker [#410](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/410). Separate from TMM Path trees, integration and capability evaluation. No runtime changes.

## 1. Exact release provenance

[Active Packwiz pin](../../../mods/dragon-survival.pw.toml), CurseForge project 420799/file 9539454. Drive file 1n1PPQPucHbWZR-MgJXfr1M3IT4z4LFGx, DragonSurvival-1.21.1-v2.0.74-09.11.2026-all.jar, 13,750,881 bytes. SHA1 e11429dda4688711e174625bb7420873a255a715 exactly matches pin; SHA512 b3ffc3263a5fc45026a99a3f18c9e49835188c01a58257c097fa72e4a6d79242fd49de7a63711af0f29666e27ec4f8c723ee397fc7ee4b876777d00470245df8. All 7,138 ZIP entries pass CRC; 1,104 compiled classes. META-INF/neoforge.mods.toml: dragonsurvival 2.0.74, NeoForge >=21.1.186, GeckoLib >=4.6 client; incompatible geckoanimfix and trinketsandbaubles. Upstream source commit not yet independently matched; exact binary sufficient for resource claims. Census under data/dragonsurvival: 166 recipe records, 153 advancements (59 nonrecipe), 225 loot tables, 100 tag records, 12 enchantments, 16 damage types, 59 worldgen records (12 structures, 6 structure sets, 31 pools, 10 processors), 87 custom registry JSONs: 61 abilities, 9 penalties, 6 bodies, 3 species, 3 stages, 4 projectiles, 1 emote set. Record counts are not unique attainable operations. English localization at assets/dragonsurvival/lang/en_us.json. Compiled namespace by.dragonsurvivalteam.dragonsurvival. No JAR/assets committed.

## 2. Orientation and progression

Start as human, find Elder Dragon Bone (loot route to detail in pass 2), combine one bone and one oak plank into oak_dragon_altar (other altar variants are alternatives). Place/use altar; choose Cave, Forest or Sea and independently select body/appearance; altar also edits skins. Dragon species is persistent player state, not a piece of equipment. Native ability and growth UI, in-game descriptions, advancements and JEI are teaching references; exact keybindings/menus to audit next. Native growth stages (data/dragonsurvival/dragonsurvival/dragon_stage/*.json):
 
| Stage | Size range | Configured ticks | Nominal real-time at 20 TPS | Growth inputs |
|---|---|---:|---|---|
| newborn | 10–25 | 216000 | 3h | Heart Element +12000, Weak Heart +36000, Elder Heart +72000 |
| young | 25–40 | 432000 | 6h | Weak Heart +36000, Elder Heart +72000 |
| adult | 40–60 | 1728000 | 24h | Elder Heart +72000 |

All three permit Star Bone -72000 and Star Heart 0 (stops natural growth per description/predicate). Do not assume offline growth, exact conversion across stage boundaries or total elapsed playtime until compiled handler traced. Optional ancient_stage and ancient_stage_no_crushing bundled packs are not default stage definitions; unlock_wings and no_penalties bundled packs likewise separate from base. Adult range capped at 60 in base data. End visit gates spin per native description; exact wings unlock and end-platform eligibility next pass. Human route remains: hunter gear, light/dark armor, dragon rider profession, vaults. Native Dragon Soul item stores dragon characteristics and returns user to human; applying filled soul replaces current dragon stats; can place as a display block while crouching. This is not a free resurrection. Scope exact restrictions and anti-death interaction next pass.

## 3. Ability census and player actions

Evidence: data/dragonsurvival/tags/dragonsurvival/dragon_ability/{cave_dragon,forest_dragon,sea_dragon,order,test_abilities}.json, data/dragonsurvival/dragonsurvival/dragon_ability/<id>.json, English ability descriptions. 43 normal species assignments (15 Cave, 13 Forest, 15 Sea). 18 separate test_* definitions, not credited as survival powers (includes test teleport, summon, heal and function). All IDs below use dragonsurvival namespace. Actions and upgrade curves next pass.

| Species | Ability IDs and normal player-facing effects |
|---|---|
| Cave | nether_breath: fire stream, ignites entities/items/blocks, destroys eligible blocks, age-dependent reach, blocked in rain/underwater; fire_ball: explosive projectile and fire; furnace_heat: smelts ground item entities, smaller stacks faster, pickup loses progress; lava_vision: clearer lava; sturdy_skin: armor buff to self/allies; friendly_fire: fire resistance to allies/pets; cave_magic: mana cap and hot-block recovery; cave_athletics: speed on stone; contrast_shower: longer cold/rain tolerance, not full water immunity; burn: chance on attacks, damage increases with victim movement; fire_immunity: innate fire immunity; lava_swimming: swim in lava but heat limits; cave_claws_and_teeth: tool-free stone/ore harvest scales with growth; cave_wings: flight; cave_spin: air/lava speed burst, End unlock. |
| Forest | forest_breath: toxic Drain damage, age range, Stress blocks; spike: piercing darts, weaker underwater; hunter: camouflage/invisibility in grass and speed, first attack bonus, breaks on taking damage or attacking; sun_breath: grow crops, mutate dirt, poison potatoes, Stress blocks; inspiration: Haste to self and nearby creatures; forest_magic: mana cap, grass/sun recovery; forest_athletics: speed on grass/wood; light_in_darkness: darkness tolerance; cliffhanger: softer landing; forest_immunity: cactus/thorn immunity; forest_claws_and_teeth: tool-free wood cutting; forest_wings: flight; forest_spin: air/water speed burst, End unlock. |
| Sea | storm_breath: sparks, Electrified spreads damage, charged creepers, storm lightning; ball_lightning: travelling damaging electrical ball, storm interaction; sea_eyes: clearer underwater vision; ore_glow: ore illumination for self/nearby players; soul_revelation: monster XP multiplier for allies; sea_magic: mana cap and wet-block recovery; sea_athletics: speed on wet/sandy/icy surfaces; hydration: longer land/Nether endurance; spectral_impact: chance to bypass armor; electric_immunity: lightning immunity; sea_claws_and_teeth: tool-free shovel harvest; amphibious: underwater breathing and swim speed; diver: underwater mining without speed penalty; sea_wings: flight; sea_spin: air/water speed burst, End unlock. |

Native penalties: Cave cold and water weakness; Forest darkness below light 3 can trigger Stress and rapidly drain food; Sea dehydration outside water (rain, ice, snow, water bottles rehydrate); fear causes animals to avoid dragons; item blacklist restricts wielding bows/shields/tridents etc. Exact species penalty tags, grace timers, exceptions and disabled-penalty datapack precedence next pass.

### Body modifiers are independent of species

Five default bodies plus no_model helper (no modifiers, not counted as normal selectable body). Actual data operations are additive value (+/- absolute) versus add_multiplied_total (percentage). Do not equate to Tyr Path purchases or final effective stats.

| Body | Native modifier entries |
|---|---|
| center | flight speed +0.2 |
| east | armor +2, attack damage -1, attack knockback -1, safe fall +2, gravity +10%, jump strength +0.1, movement +10%, swim speed +0.5, mana +2, flight speed -0.2, flight stamina -20% |
| north | damage -20%, attack knockback -0.5, movement -30%, safe fall +1, swim speed +0.8, mana +2, flight stamina -10%; base model height 1.1 vs 2.0, eye height 0.99 vs 1.8 |
| south | attack damage +0.5, gravity +20%, jump strength +0.2, movement +20%, safe fall +2, swim speed -0.2, flight speed -0.2, flight stamina -50% |
| west | attack knockback +0.5, knockback resistance +0.15, movement -15%, safe fall +1, swim speed -0.2, flight speed +0.2, flight stamina +100% |

## 4. Crafting and resource loops

Native recipe IDs under data/dragonsurvival/recipe. Ordinary decorative recipes in JEI; key gates:
- Weak Heart: 2 lapis + 4 gold nuggets + Heart Element + 2 Charged Coal. Elder Heart: 2 diamonds + 4 gold ingots + Weak Heart + 2 netherite scrap. Native tooltip indicates rare monster drops of different heart grades based on health; exact drop formula outstanding.
- Star Bone: nether star + 8 Elder Dragon Bones yields 8 Star Bones. Star Heart: Star Bone + Elder Heart, controls growth. 
- Species Sources of Magic: all require Elder Heart. Cave: 2 nether gold ore, 2 basalt, 2 magma blocks, chiseled polished blackstone, lava bucket. Forest: 2 emerald ore, 2 stripped oak wood, 2 oak logs, lily pad, water bucket. Sea: 2 lapis ore, diamond, 2 obsidian, 2 dark prismarine, water bucket. Stand/bathe in matching source for temporary infinite mana per native tooltip, damages other creatures. Exact consumer and duration next pass. Native recipe uses ordinary ore IDs; deepslate substitutions/pack overrides separate.
- Dragon Beacon: Star Heart + item in activates_dragon_beacon tag. Beacon Activator: diamond + netherite scrap + gold ingot. Species activation gives Cave Fire/Sturdy Skin, Forest Magic/Animal Peace, Sea Peace/Haste respectively; all include Empowered Soul. Data map dragon_beacon_data stores duration 400 ticks, payment experience_cost 60 and duration_multiplier 30; actual payment selection and application to trace next. Dragon Memory Block below beacon provides automatic reduced-duration area effect at no additional cost (tooltip); recipe 4 Elder Dust, 3 Elder Bones, Heart Element and experience bottle. Not a storage block.
- Treasure/hoard: sleep on treasure for HP/mana regeneration, larger hoard increases regeneration; recipe variants and sleep-on-hoard advancements. Treasure is a placed wealth/healing surface, not established general-purpose inventory.
- Dragon Rider Workbench: 2 diamonds, saddle, 2 gold blocks, smithing table. Villager profession trades enchantments and vault access per tooltip. Hunter Key from Hunter Leader in castle; open vault branches. Diamond/netherite partisan recipes, four Light and four Dark armor smithing recipes (corresponding netherite part + matching upgrade template + Elder Heart). Exact armor, enchantment and trade stats pass 2.
- Hot Dragon Rod and Spearman Promotion shapeless ingredients include count:8 dust and count:3 emerald blocks respectively. Data fields do not establish actual consumption; prior owner acceptance [#239](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/239) remains unchanged. No fixes.
- Food families: species Treats, Charged Coal/Soups, Seasoned Fish, charred meat/mushroom/seafood/vegetable (smelting/smoking/campfire variants), other species-specific dishes and material recipes. Native diet data map separate from TMM raw-food policy; exact active values pass 3.

## 5. Typed resource ledger (initial)

| Resource | Native storage/flow | Limits / open details |
|---|---|---|
| Mana | player mana pool, HUD; species Magic ability upgrades and environmental recovery; matching Source of Magic grants temporary infinite mana; skills consume mana | exact base cap, regeneration, reservation, XP conversion, cooldown/debit, persistence pass 2 (ManaHandler, DSAttributes, ManaCost) |
| Growth | player stage/growth state, time and heart/bone consumables; Star Heart stops natural growth | stage-specific inputs above; exact tick/size and persistence formula next |
| Flight stamina | player attribute, body modifiers and wings/spin | costs/recovery/maximum next |
| Hydration/penalty timer | player state, sea environmental/consumable recovery, Hydration upgrades | timer and damage curve next |
| Vanilla XP | player XP, native ability upgrades and beacon payment data, Soul Revelation bonus | do not confuse with MXP, cost curves next |
| Hoard | placed treasure, regenerates HP/mana on rest | counts/radius/rate next |

No FE/OP producer or block/item mana network established; block Source of Magic provides effect, not demonstrated energy reservoir. Scope absence remains unknown pending class inspection.

## 6. All 29 axes — first-pass navigation, not completion

| Axis | Status | Mechanism or remaining evidence |
|---|---|---|
| gear | present | Hunter partisans, Light/Dark armor, hunter sword, enchantments; details next |
| movement | present | wings/spin, swim, terrain athletics, body choices, End platforms |
| ore_processing | present | Furnace Heat ground-item smelting; no doubling assumed |
| storage | unknown | Dragon Soul stores character state, hoard is not inventory |
| crafting | present | altar, hearts, Sources, beacon, Memory Block, workbench, armor and foods above |
| logistics | unknown | no item routing established; investigate |
| buffs | present | allied armor/fire resistance, Haste, Hunter, Soul Revelation, beacon effects |
| external_compatibility | present | NeoForge registries, GeckoLib, optional Create treasure and Silent Gems data, body backpack/Better Combat offsets; effective TMM interaction unverified |
| mob_farming | limited | monster heart drops/XP multiplier, charged creepers; no native spawner established |
| ore_generation | unknown | Ore Glow detects existing ore, not generation |
| permanent_power | present | species, body, growth, learned abilities |
| general_generation | limited | Sun Breath dirt mutation, charged creepers; no arbitrary synthesis established |
| direct_damage | present | three breaths, fireball, spikes, ball lightning, Burn, Spectral Impact |
| block_breaking | present | claws by species, fire breath/destructive fireball |
| permanent_pets | unknown | buffs to pets not pet creation; rider system separate to inspect |
| temporary_pets | unknown | test_summon is not species-assigned |
| defense | present | innate species immunities, Sturdy Skin, Friendly Fire, body modifiers, penalty tolerance |
| healing_recovery | present | hoard HP/mana recovery, Sources of Magic, hydration |
| debuff_control | present | Drain, Burn, Electrified, Stress and animal fear; hunter gear control next |
| energy | present | typed ledger above, not FE |
| automation_control | present | Memory Block passive beacon, species/human pressure plates; door predicates next |
| agriculture | present | Sun Breath accelerates plants; Forest animal peace |
| detection_information | present | Ore Glow, Sea Eyes, Lava Vision, vault maps/trades |
| equipment_augmentation | present | 12 native enchantment definitions, light/dark smithing |
| world_manipulation | present | fire/breaking blocks, crop/dirt changes, charged creepers, species doors |
| world_content | present | 12 structure definitions; species End platforms at (-200,50,0), (0,50,-200), (0,50,200) for Cave/Forest/Sea from end_platforms map; exact access and loot next |
| death_respawn | unknown | native anti-death and clone handling not yet traced; TMM Dracolich separate |
| trade_economy | present | Dragon Rider and Hunter Leader, keys and vaults |
| progression_tiers | present | human → altar/species/body → newborn/young/adult + ability upgrades, wings/spin, heart/Source/beacon routes, independent human gear routes |

## 7. Initial candidate teaching slices (not implemented quests)

Use native advancements as detection when criterion validated; possession is proxy only. No reward values set before progression review. Candidate spine: locate bone; craft/place altar; choose species and body; read growth UI; collect Heart Element; craft Weak Heart; grow young; craft Elder Heart; grow adult; Star Bone shrink; Star Heart stop growth; use Dragon Soul and return; construct hoard/rest; upgrade species mana; build matching Source of Magic; learn flight; visit End and learn spin; craft beacon/activator; install Memory Block; demonstrate each species' natural harvest, primary breath, secondary projectile, utility breath/vision, allied buff, environmental weakness mitigation and innate immunity; trade with dragon rider; locate hunter castle; obtain key; open vault; craft partisan upgrades; obtain matching Light/Dark templates and smith armor; demonstrate species pressure plate/door. Exact activation, advancement triggers, lesson dependencies, costs and safe rewards refined in passes 2–3.

## 8. Next work

Pass 2: audit every 43 ability definition (upgrade requirements, activation, costs, cooldown, ranges, modifiers and exact max rank), species penalties, stage formulas, ManaHandler/DSAttributes/DragonGrowthHandler, wing/spin unlock, death handling, loot/trade/gear/enchantments and beacon/hoard mechanics. Pass 3: reconcile active TMM overrides, diet, dragon Paths and Dracolich without designing new behavior; finalize quest-source matrix and separate 11-domain evaluation. No gameplay test required for ordinary native behavior. Native resource census and pin hash verified offline only. No source-completion claim or full build.


## 9. Pass 2 — exact binary numerical audit, 2026-11-25

Correction from exhaustive base species tags and registry files: **43 assigned abilities = Cave 15 + Forest 13 + Sea 15; 18 test abilities, 61 total**. Bundled datapack variants of wings are duplicate IDs, not extra normal abilities. Source of truth data/dragonsurvival/tags/dragonsurvival/dragon_ability/{cave_dragon,forest_dragon,sea_dragon,test_abilities}.json; default definitions under data/dragonsurvival/dragonsurvival/dragon_ability. All 43 assigned definitions inspected. Values below are default native data, not effective TMM overrides. Rank r starts at 1; ticks at nominal 20 TPS. Damage is raw HP per application, not guaranteed actual damage or DPS.

### 9.1 Rank mechanics (compiled evidence)

ExperienceLevelUpgrade.apply loops from rank 1 to maximum, counts thresholds satisfied by input XP level, and sets the instance to that rank, including decreases. No XP debit. These are reversible current-level gates, not purchased permanently. ExperiencePointsUpgrade.apply checks total raw XP and cap, calls giveExperiencePoints with negative price for upgrade; downgrade refunds the entire current rank price and lowers rank. For linear cost kr, total from 0 to N is kN(N+1)/2. Creative infinite materials bypass payment. DragonGrowthUpgrade/condition gates distinct; no claim about custom Path MXP. All lists below give each rank threshold or cost formula.

### 9.2 Every normal ability: Cave (15)

| ID | Max rank and upgrade | Activation: cast ticks / cooldown ticks / mana | Native magnitude |
|---|---|---|---|
| nether_breath | 4; XP level 0/10/30/50 | channel 20/40/0.025 per tick | 3r HP every 10 ticks, ignite 100r ticks; Burn 30%/200 ticks, block ignition 5%, snow/short grass removal 100%, snow block/powder snow 5%r |
| fire_ball | 4; XP level 0/20/40/45 | 40/140/1 | one projectile, 10+9(r−1) HP direct =37, explosion power r=4, fire/block break, no self explosion damage, max travel 32, lifetime 100 ticks |
| furnace_heat | 4; XP level 0/12/32/64 | channel 20/40/0.03 per tick | item smelt progress 4r=16 per effect application; 0.5r HP every 10 ticks and ignite 20r ticks |
| lava_vision | 4; XP level 0/25/45/60 | 40/600/1 | 600r ticks clear lava vision =120s, grounded or in lava |
| sturdy_skin | 3; XP level 0/15/35 | 60/600/2 | radius 5 non-enemies, 1200r ticks, +3 armor (DSEffects, amp 0) =180s |
| friendly_fire | 3; XP level 26/45/60 | 80/600/4 | radius 25, fire resistance raw amplifier r, 5280r ticks=792s |
| cave_magic | 10; XP points 36r, total 1980 | passive | +r max mana, +0.03r regeneration/tick when warm/lava/etc; +10 and +0.30/tick max |
| cave_athletics | 5; XP points 15r, total 225 | passive every 20 ticks | Speed raw amplifier 0.2r, duration 20r ticks on valid terrain (max raw amp 1; integer conversion below) |
| contrast_shower | 8; XP points 15r, total 540 | passive | +200r ticks penalty resistance=80s bonus |
| burn | 4; XP points 15r, total 150 | passive on attack | chance 0.15r=60%, 100r ticks=20s |
| fire_immunity | innate; no explicit upgrade | passive | damage multiplier 0 on #minecraft:is_fire |
| lava_swimming | 2; XP level 0/15 | passive | max oxygen 3600+1200(r−1) ticks=240s, stable swim rank 2 |
| cave_claws_and_teeth | 4; size 0/25/40/60 | passive | pickaxe blocks; base speed tier wood at rank 1, stone from rank 2; break_speed_multiplier 0.25r, harvest_bonus 1+0.5(r−1); no assertion that raw harvest_bonus equals vanilla mining tier |
| cave_wings | condition, binary | passive | flight_was_granted required; optional unlock_wings pack not default |
| cave_spin | condition, binary | passive | spin_was_granted; air/lava |

Cave total raw XP spend to max explicitly purchased ranks: 2895. XP level 64 needed to maintain all level-gated abilities simultaneously.

### 9.3 Forest (13)

| ID | Max rank and upgrade | Activation ticks / cooldown / mana | Native magnitude |
|---|---|---|---|
| forest_breath | 4; XP level 0/10/30/50 | channel 20/40/0.025/tick | 2r HP per 10 ticks, Drain 30%, 200 ticks |
| spike | 4; XP level 0/20/30/40 | 2/60/1 | r projectiles, 2r HP each, piercing_level 3, spread 1.5; nominal maximum four ×8 HP before hit/mitigation rules |
| hunter | 4; XP level 0/25/35/55 | 60/600/3 | radius 5, raw amplifier r, duration 600r=120s; registered speed modifier +0.2 multiplied total per effect amplitude; actual invisibility/first-hit logic separate |
| sun_breath | 2; XP level 0/24 | channel 20/40/0.04/tick | every 10 ticks, two bonemeal attempts with probability 0.5r; 50% potato conversion; 20% dirt conversion weighted grass 25/podzol 5/mycelium 1/coarse dirt 3 |
| inspiration | 3; XP level 5/15/35 | 80/600/2 | radius 5, Haste raw amp r, 4000r ticks=600s |
| forest_magic | 10; XP points 36r, total 1980 | passive | +r mana, +0.01r/tick favorable grass/sun=+0.10/tick |
| forest_athletics | 5; XP points 15r, total 225 | passive 20 ticks | Speed raw amp 0.2r, 20r ticks on tag |
| light_in_darkness | 8; XP points 15r, total 540 | passive | +400r ticks penalty resistance=160s bonus |
| cliffhanger | 6; XP points 16r, total 336 | passive | safe fall distance +7+2(r−1)=17 blocks |
| forest_immunity | innate | passive | zero damage sweet berry bush, cactus, Drain and Forest Breath |
| forest_claws_and_teeth | 4; size 0/25/40/60 | passive | axe tag, same numeric parameters as Cave claws |
| forest_wings | condition | passive | flight_was_granted |
| forest_spin | condition | passive | spin_was_granted; air/water |

Forest total raw XP purchases 3081; highest simultaneous XP level threshold 55.

### 9.4 Sea (15)

| ID | Max rank and upgrade | Activation ticks / cooldown / mana | Native magnitude |
|---|---|---|---|
| storm_breath | 4; XP level 0/10/30/50 | channel 20/40/0.025/tick | r HP every 10 ticks, Charged 50% for 600 ticks; 30% block cloud lasting 40 ticks |
| ball_lightning | 4; XP level 0/20/45/50 | 40/400/1 | projectile pulse 4r HP every 5 ticks radius 4, Charged 50%/100 ticks; rain lightning 10% per 10 ticks, explosion power 2+(r−1)=5, 100-tick lifespan/linger parameters, distance 32; bouncy variant exists but not assigned |
| sea_eyes | 4; XP level 0/25/45/60 | 20/600/1 | water vision 600r=120s |
| ore_glow | 4; XP level 0/20/45/50 | 60/600/3 | radius 5 recipients, 5200r ticks=1040s. Vision range diamond and netherite 36; emerald 26; iron/gold 22; lapis/redstone/coal/copper/general 16; quartz 12. Common ore tags |
| soul_revelation | 3; XP level 12/24/36 | 60/600/6 | radius 5, native experience attribute +0.5r=+1.5, duration 1200r ticks=180s; base 1 gives 2.5 multiplier before other modifiers |
| sea_magic | 10; XP points 36r, total 1980 | passive | +r mana; +0.02r/tick favorable wet terrain =+0.20/tick |
| sea_athletics | 5; XP points 25r, total 375 | passive every 20 ticks | Speed raw amp 0.2r, 20r ticks |
| hydration | 7; XP points 15r, total 420 | passive | +1200r ticks penalty resistance=420s bonus |
| spectral_impact | 3; XP points 64r, total 384 | passive | armor_ignore_chance +0.15r=45%, actual incoming damage armor reduction overridden on proc (EnchantmentEffectHandler), attribute clamp 0–1 |
| electric_immunity | innate | passive | zero damage #dragonsurvival:is_electric |
| sea_claws_and_teeth | 4; size 0/25/40/60 | passive | shovel tag, same parameters as Cave claws |
| amphibious | 2; XP level 0/15 | passive | infinite water oxygen, stable swim rank 2, +1 swim speed and +1200 ticks penalty resistance |
| diver | innate | passive | +0.8 submerged_mining_speed |
| sea_wings | condition | passive | flight_was_granted |
| sea_spin | condition | passive | spin_was_granted; air/water |

Sea total raw XP purchases 3159; highest simultaneous XP level threshold 60. All three totals exclude XP levels retained, not spent. Projectile evidence from projectile_data/{fireball,spike,ball_lightning,bouncy_ball_lightning}.json. Raw potion amplifier is not automatically displayed potion level; resolve codec conversion before claiming fractional Speed or exact displayed Haste. DamageEffect handler and external modifiers can affect actual damage, iframe rules prevent equating pulse interval to guaranteed DPS.

### 9.5 Typed mana and attribute formulas

Compiled DSAttributes base/min/max: mana 1/0/1024; mana_regeneration 0.004/0/1024; flight_speed 1/0/1024; flight_stamina 1/0/5; lava_swim_speed 1/0/1024; experience 1/0/1024; breath range 3/0/1024; penalty resistance 200 ticks/0/16384; armor ignore 0/0/1; block break radius 0/0/16; dragon ability damage and hunter faction damage 1/0/10. These are attribute registry clamps, not attainable native build maxima.

ManaHandling default (species JSONs omit explicit override): 0.1 mana per XP point conversion, 0.25 bonus mana per vanilla level capped at 9 (level 36). ManaHandler.getRawMaxMana = max(0, mana attribute + min(9,0.25*XP level)); getMaxMana subtracts reserved mana and floors zero. Base no gear/body: cap 20 at magic rank 10 and level 36+, east/north body +2 =>22. Native regeneration once each player tick only when not casting and available mana below max. Default 0.08/sec; rank 10 favorable terrain Cave 6.08/sec, Forest 2.08/sec, Sea 4.08/sec. Source of Magic effect bypasses mana checks/debit and adds +10 multiplied-base mana regeneration. Optional XP-to-mana switch permits paying deficit with vanilla XP, negative conversion floor(deficit/0.1), i.e. ten XP per mana before rounding; actual casting can lower XP-level gated ability ranks. No MXP exchange inferred. Source of Magic station capacity/payment and duration still need detailed handler trace.

### 9.6 Growth, penalties and treasure

DragonStage.ticksToGrowth(t) = (maxSize−minSize)/ticksUntilGrown * t. DragonGrowthHandler server Pre player tick at interval 20, uses current stage desired growth and checks allowed growth/space, so offline time not added by this handler. Desired size change per second: newborn 15/10800, young 15/21600, adult 20/86400. Elder Heart adds 5, 2.5, 0.8333 size respectively in those stages; Weak Heart 2.5 newborn/1.25 young; Heart Element 0.8333 newborn; Star Bone negative same magnitude as Elder Heart. Stage change/actual interpolation boundaries remain to inspect. Stage data modifiers (additive): attack +1/+2/+3; jump strength +.025/.05/.1; safe fall +.25/.5/1; adult step +.5; adult entity and block reach raw linear .05 per stage evaluation level; native health/scale/breath range use distinct stage level-based expressions, do not misapply them as full size before evaluating actual modifier argument. Adult health base 10.5 step .5, breath range 5.55 step .05, scale precise base −.05 step .025. Newborn health base −5.6 step .4, breath 1.55 step .05, scale −.75 step .013. Young health base .6666667 step .6666667, breath 3.3 step .05, scale −.55 step .033333335.

Penalty JSON confirms correction: Forest darkness triggers below light **3**, not below 4; exempt Forest Magic effect, Glowing or light-source tag held either hand. Supply reduction 1 and regen .013, trigger 40 ticks, Stress 200 ticks. Cave cold/rain supply reduction 1/regen .013, 1 HP per 40 ticks when depleted; direct water 1 HP every 10 ticks, Cave Fire exemption. Snowball/water splash/water drinking or milk 2 HP. Sea thin skin 1 HP dehydration; wet blocks/water/rain/snow or Sea Peace exempt; frozen raw fish and water bottle recovery inputs. Fear distance 20, walk 1.3/sprint 1.5; Animal Peace exemption; blacklist contains other mods' items and elytra regex. Need effective penalty-supply scaling handler before equating attribute bonus to final grace duration.

DragonTreasureHandler counts nearby treasure **layers** (not blocks) in scanned volume with radius parameters 16/9/16, clamp 240; default HP recovery interval = 280−layers ticks, heals 1 HP each interval, at maximum 40 ticks (plus tick counter boundary); resting effect configured Regeneration 200 ticks base probability .5 and .01 coefficient, exact effect invocation and mana gain still to trace. Explicitly not portable storage.

DragonHeartLootModifier: last-damage player must be dragon, victim LivingEntity not player; max HP inclusive 14–20 shard, 20–50 weak, 50+ elder (boundary overlap can independently roll). Config defaults c=.03/.01/.01; tag whitelist flag default false, applicable tags operate as exclusions. Looting L threshold =100c(1+L/4), random integer 0..99 succeeds <= threshold (inclusive, so default nominal 3/1/1% actually 4/2/2% at L=0, subject to global loot conditions); no assumed general mob farming machine. Confirm pack config in pass 3.

### 9.7 Gear, enchantment and damage hooks — initial numeric audit

Twelve actual base enchantment records under data/dragonsurvival/enchantment: aerodynamic_mastery, blood_siphon, bolas, combat_recovery, curse_of_kindness, curse_of_outlaw, draconic_superiority, dragonsbane, murderers_cunning, overwhelming_might, sacred_scales, unbreakable_spirit. Dragonsbane max V, bonus 2.5r vs eligible dragons plus slowness; all other eleven max I. Aerodynamic Mastery +0.5 flight stamina at I; Combat Recovery 20% when no regeneration delay, Regeneration I–II 5–10s and delay 40s; Blood Siphon 30% at I on hit as victim, applies to attacker 3–10s amp 1–2 and damages item 2. Bolas crossbow special handler. Shrink, Dragonsbonk and Dragonsboon are under data/dragonsurvival/unused/enchantment, NOT active registry entries; do not credit as native capabilities. Sacred Scales native max I: damage protection +20 on 20% chance, formula clamps 70 protection and 80% chance at higher externally granted ranks. Curse of Kindness −25% hunter-faction damage at I; Outlaw applies Hunter Omen. Draconic Superiority data defines damage ×(1.2+.08(r−1)), clamped factor 70; compiled EnchantmentEffectHandler *also* modifies dragon magic tagged incoming damage to 1.2*amount + .08r. Murderer's Cunning compiled full-health victim hit =1.4*amount + .2r, native max I. Do not conflate data multiplier with event formula or claim double stacking without event sequencing. Blood Siphon heals attacker 0.01*(amplifier+1)*incoming damage, before mitigation. Spectral Impact removes armor reduction on proc (not all damage reduction). Full aligned gear registration/remaining enchantment handlers, flight item acquisition/stamina, native anti-death and Source of Magic/beacon exact payment are still open, not silently marked resolved.

### 9.8 Handoff

43/43 native ability definitions numerically mapped, compiled XP, mana, growth, heart loot, hoard, armor-ignore and two aligned enchantment handlers inspected. No claim of exhaustive gear/death/flight runtime or effective pack config. Pass 3 should first close remaining native handler questions (gear and 12 enchantment names/stats, flight grant and spin source, beacon/source and death lifecycle), then reconcile TMM diet, Path/Dracolich, quest steps and evaluation. Same PR; no new design, gameplay or config. 


## 10. Pass 3 — remaining native mechanics and TMM reconciliation

Exact Drive binary SHA1 rechecked locally against section 1. Compiled javap -p -c inspected SourceOfMagicBlock/BlockEntity, DSItems, DragonBeacon/BlockEntity, ServerFlightHandler, FlightGrantItem, SpinGrantItem, PrimordialAnchorBlock, EnderDragonMarkHandler, OnDeath, EffectHandler, DSEquipment, Light/DarkDragonArmorItem (including switch maps), DragonSoulItem, PlayerLoginHandler. No gameplay claimed. This closes the substantial native capability survey, not every nonblocking coefficient or effective server override. Earlier unknowns/next-pass statements are historical checkpoints; latest resolutions below prevail.

### 10.1 Source of Magic, beacon, and player-state economy

All three Source of Magic item constructors in DSItems define the same five consumables and durations: elder_dragon_dust 10s, elder_dragon_bone 30s, dragon_heart_shard 60s, weak_dragon_heart 120s, elder_dragon_heart 300s. Species-specific component transferred to block entity. Place station with room for its placeholder structure, open GUI and insert one stack or drop eligible fuel into it. Container size exactly one; only same-item stacks merge up to item stack limit. Applicable dragon must occupy magic portion while charged until magicSource counter exceeds secondsToTicks(10); counter reset, one fuel removed, effect granted/extended. Duration capped at 30 minutes for extensions; at cap no debit. This is a consumable-to-player-effect operation, not mana in a block battery. Wrong-species/human player damage if damageWrongSourceOfMagic enabled: 1 HP magic state, .5 HP otherwise per hurt call; not damage every tick to all mobs (entityInside returns on non-player after item handling). Ordinary mana debit bypassed while effect active, not cooldown bypass. No FE interface established. Source storage axis limited to dedicated one-stack fuel buffer.

DragonBeacon.useWithoutItem checks total vanilla XP >=60 (creative bypass), calls applyEffects(player,true) then debits 60 XP only on success. Default data effects duration 400 ticks; paid duration multiplier 30 =12000 ticks/10min. Construct and configure with Beacon Activator as in section 4. Memory Block immediately beneath lit beacon: once every 20 world ticks apply unpaid 400-tick effects to all players in AABB inflated by 50 in all dimensions and expanded upwards by world max build height; not a spherical radius or global cross-dimension buff. Continuous operation while loaded, no per-tick fuel; no ownership gate in serverTick. Species data sets effects Cave Fire/Sturdy Skin, Forest Magic/Animal Peace, Sea Peace/Haste; each Empowered Soul. EffectHandler prevents Exhausted Soul application while Empowered Soul active and removes existing conflicting effect when applied. Hence beacon also supports switching stored forms, not player resurrection. Source and beacon are genuine distinct stations.

### 10.2 Flight, travel and native death

Flight Grant (wing_grant) and Spin Grant (spin_grant) use respective permanent flags only for dragons, consume item when used successfully. Exact loot_table/chests/end_spin.json contains two guaranteed independent pools (one of each grant). Do not promise TMM Ender Dragon drops: these are chest loot. Primordial Anchor is a distinct dragon travel/flight unlock route: fuel tag contains ender pearl; charge/use by dragon, teleports to End platform; defaults anchorGivesFlightGrantState=true and anchorGivesSpinGrantState=false. Thus merely reaching End is not equivalent to spin unlock. EnderDragonMarkHandler records contributing players and marks participating dragons on boss death if native default curse enabled; optional unlock_wings datapack is distinct and bypasses wing restrictions. Native grant state not proof of continuous flight during broken wings or other restrictions. Use actual wings/spin controls shown in Controls menu.

ServerFlightHandler defaults: maxFlightSpeed config .3 (internal motion scalar, not blocks/sec), flightHungerThreshold 6, foldWingsThreshold 0, foldWingsOnLand false, flightHungerTicks 50, spin cooldown 5, spin duration secondsToTicks(.76). Stable hover false, collision damage enabled, fall damage enabled, collision threshold 3 and speed factor 10 (not universal fixed impact damage). Flight cost is FOOD EXHAUSTION, not a separate rechargeable stamina bar: when dragon flying with wings spread, default per-tick exhaustion max(.008,min(1,max(0,1−horizontal velocity))* .08), divided by positive FLIGHT_STAMINA_COST attribute. At food <=fold threshold noncreative server folds wings; gliding eligibility uses hunger threshold. West body doubles native positive stamina-cost divisor, Aerodynamic Mastery +.5 attribute; attribute clamp 5. Do not resell flight or interpret stamina as mana.

**Critical correction/clarification:** compiled OnDeath.trigger enumerates active passive abilities with OnDeath activation; cancels LivingDeathEvent only if pre-trigger health <=0 and post-trigger health >0. Exhaustive text search of all packaged dragon_ability JSONs: ONLY test_heal uses on_death; no species-assigned default ability grants a death save. Engine supports a datapack-configurable rescue, not a native attainable guaranteed revival. Do not count test_heal toward combat or death domain, and do not imply all dragons revive. EffectHandler.preserveEffects copies Hunter Omen and Exhausted Soul on death; state preservation is not resurrection. Dragon Soul item is form storage/reapplication with Exhausted Soul cooldown, not an Arca or phylactery. No independent general grave or resurrection facility found.

### 10.3 Human/dragon equipment, trade and world route

DSEquipment both aligned materials: helmet3/chest8/legs6/boots3, total20 armor; toughness3 each, knockback resistance .1 each; enchantability30. Permanent intrinsic enchantment switch maps:
- Light helmet Combat Recovery, chest Aerodynamic Mastery, leggings Unbreakable Spirit, boots Sacred Scales; every piece Curse of Kindness.
- Dark helmet Blood Siphon, chest Murderer's Cunning, leggings Overwhelming Might, boots Draconic Superiority; every piece Curse of Outlaw.
- Light cannot equip if Hunter Omen or another worn Dark piece; Dark cannot equip if Animal Peace or another worn Light piece. Unbreakable Spirit/Overwhelming Might use effect blacklist handlers; no universal immunity or invented multiplicative stack. Equipment recipes smith matching upgrade template, corresponding netherite piece and Elder Heart. Native hunter faction route diamond partisan → netherite smithing, bolas, promotion, leader/key/vault; Dragon Rider Workbench and aligned equipment independent access. Native loot dragon_chest_loot has Elder Bone pool 1 roll, item weight1 (1–3) vs empty5, Elder Dust 1–10 weight1 vs empty5, Star Bone weight1 vs empty20. Hunter chest loot separately contains Elder Bone. First altar also has alternative materials, not only oak. Native advancements find_bones/place_altar, find_overworld_structures/find_nether_structures/find_end_structures, hunter/collect_key/open_vault/promotion/fire_bolas, light and dark key/vault/armor items/set support optional quest branch. No need to force hunter crime on pacifist player or a dragon transformation on every player. Exact trader prices/loot odds outside the major gates are reference precision, not mandatory quest instructions; use actual trading screen/JEI. No ordinary pet creation, item routing network, ore generation or arbitrary construction machine established in native normal ability/registry/data census. Test summons excluded. Sun Breath agricultural growth is genuine.

### 10.4 TMM integration comparison — source snapshot, not installed-world proof

Read test/full-integration index and actual files: defaultconfigs/dragonsurvival-server.toml sets start_with_dragon_choice=false, allow_dragon_choice_from_inventory=false, transform_altar=true, no_humans_allowed=false. Thus teach altar-based transformation, not native login prompt or inventory choice; existing world migration separate. Other native config defaults not overridden by that short file, but actual world serverconfig not inspected. Newbie restrictions/graduation belong to Progression, not native species unlocks. 

[Existing diet map](../../../kubejs/data/dragonsurvival/data_maps/dragonsurvival/dragon_species/diet_entries.json) and [dragon_diets.js](../../../kubejs/server_scripts/dragon_diets.js) still indexed; legacy Cave charred/coal values e.g. charred_meat 6/3, charred_seafood 7/11 are NOT the latest approved nutrition policy. Current integrated [DragonDietHook](../../../compat/progression/src/main/java/io/github/heatwaveqc/tyr/progression/runtime/forms/DragonDietHook.java) checks exact 2.0.74, original FOOD component, server authoritative provider flags, delegates NativeDietAdapters.decide: DENY=null, PERMIT_NATIVE=original, NUTRITION=rebuild properties preserving original use time, conversions and effects, NATIVE=fallback. Magical foods retain their own properties, unknown source fallback, permission-only drinking gives 0 nutrition and 0 saturation while permitting use. Reentrancy guard, client preview. Actual custom non-component special consumption remains upstream; recipe-provenance coverage matters. Do not assume every old diet record overridden by engine. Owner #444 raw preferred Cave terrestrial animal, Sea marine animal and sea plants, Forest plant/fungus; raw preferred 6/2.0 modifier, cooked half, charred quarter, mixed diet-relative ceiling/worst-case variants; separate food source coverage and runtime acceptance. Forest older grain-free legacy map versus newer plant-family draft explicitly distinguish. Seasoned Fish override confirmed in indexed dragon_seasoned_fish.js: raw salmon → #c:foods/raw_fish, ink sac + kelp unchanged; Sea Dragon Treat separate. Hot Dragon Rod/Spearman accepted native quirks #239; no new fix. 

All three 100-point design documents exist on integration source: Sea 22 nodes #514; Cave 25 #530; Forest 26 #536; each explicitly DESIGN ONLY, not proof installed skill tree. Shared Draconic Reservoir +60% candidate is not native Magic X; native cap 20 plus body bonus before equipment, effective order is attribute modification before XP-derived bonus, so a +60% multiplier to native mana attribute does not necessarily equal +60% total pool. Draconic Potency candidate +20% must use native damage attribute/event only once; native ability damage clamp 10 multiplier distinct from damage cap; keep typed spell attribution and Core stacking. Scales +3 additional armor, Claws +3 damage natural attacks only, Talons +45% legal harvesting speed must not sell native species claw/tool permission again. Sea: Amphibious, Diver, Ore Glow, Soul Revelation, Spectral Impact and native storm attacks are already native; any dodge/luck/breath/reservoir enhancement separate. Forest: Sun Breath, Hunter, Haste, Cliffhanger, natural immunities and darkness mitigation native; nature support/cultivation enhancements not duplicate gates. Cave: fire immunity, lava swimming, Sturdy Skin, Furnace Heat, breath/fireball, pickaxe claws native; do not infer ore generation/doubling or global water immunity. Shared native flight/spin remains conditional, not purchased. XP-level gates recalculate even if custom MXP points retained; Soul Revelation only native LivingExperienceDropEvent, no automatic MXP multiplication. New Path caps and stacking remain owner/Progression work, not changed here.

Dracolich integration exact active source DracolichDragonFirstEvents and DragonSurvivalDeathRepeatMixin (PR #69): invokes original OnDeath.trigger at HIGHEST for valid single-provider Dracolich, records event once, suppresses repeat normal invocation; missing provider flags fail closed, lich fallback if dragon does not save. This preserves extensibility but base 2.0.74 has no assigned rescue as above. No redesign requested, don't remove hook or grant test_heal. Goety Arca OR Forbidden Magic phylactery, not both; native provider hunger ownership, diet interaction separate. Existing pilot documentation has older dependency pins, not evidence current provider versions. No change to resurrection ordering, transformation access, skills, food, claims, XP or recipes.

### 10.5 Quest-source matrix — proposed instructional beats (not FTB files)

Legend A = exact native advancement ID below (criteria may be a possession proxy; not proof of operation unless verified), I = inventory/craft proxy, O = observable operation/checkmark, P = later verified personal provider state detector. No invented reward amounts, no repeated quest XP for freely reversible actions, no mandatory death or crime. JEI and native controls supplement instructions. All IDs prefixed dragonsurvival:. Lesson map groups optional branches rather than forcing every species on one character.

| ID | Prerequisite and player-facing action | Completion evidence |
|---|---|---|
| DS01 | Explore dragon/hunter structure loot; obtain Elder Dragon Bone | A find_bones / I |
| DS02 | Craft any altar variant, place and open it | A place_altar; O menu |
| DS03 | After TMM graduation eligibility, select ONE species and body, customize appearance | A cave/forest/sea/be_dragon, P current species; no forced transformation |
| DS04 | Learn inventory/ability GUI, mana bar, native skill controls and raw XP vs XP levels | O menu and first cast |
| DS05 | Read chosen species' actual diet, eat a suitable raw food; compare native special foods and TMM nutrition | O hunger; no legacy raw/charred reward incentive |
| DS06 | Find a suitable mob, collect Heart Element/Shard | A collect_heart_from_monster |
| DS07 | Craft Weak Dragon Heart; use eligible growth input | I then O size changes |
| DS08 | Reach young stage, observe growth and claw upgrade | A be_young_dragon |
| DS09 | Craft Elder Dragon Heart, use it or grow naturally | I / O |
| DS10 | Reach adult stage, see body size and health/reach | A be_adult_dragon |
| DS11 | Optional craft/use Star Bone to reduce size | I / O size decrease |
| DS12 | Optional Star Heart stop natural growth | A stop_natural_growth |
| DS13 | Rest on treasure and expand hoard, see health recovery | A sleep_on_treasure, sleep_on_hoard, sleep_on_massive_hoard |
| DS14 | Purchase one species Magic rank using raw XP, observe pool; downgrade optionally | P ability rank; O |
| DS15 | Retain XP levels to improve native breath; note rank may decrease after spending XP | P / O |
| DS16 | Craft matching Source of Magic; fuel, bathe, cast while buffed | I / O Source of Magic effect |
| DS17 | Locate/use native Primordial Anchor, ender pearl fuel, only if appropriate to native stage and dimension access | A find_end_platform / O flight flag |
| DS18 | Find End platform chest, obtain/use Flight Grant if needed and Spin Grant | I / P flag; O |
| DS19 | Spread wings, safely take off, land; learn hunger/collision constraints | O / P flight, no creative-mode proxy |
| DS20 | Demonstrate spin in valid medium, understand cooldown | O native spin |
| DS21 | Craft/activate Dragon Beacon using species activator, buy effects for 60 XP | A change_beacon / O actual buff |
| DS22 | Place Dragon Memory Block underneath, observe free area effects | A use_memory_block |
| DS23 | Optional obtain/visit all three beacon variants; buy each effect without requiring species swap | A get_all_beacons if actual criteria met |
| DS24 | Obtain Dragon Soul, save/reapply form only if player chooses; explain Exhausted Soul and Empowered Soul | A use_dragon_soul; not compulsory path reset |
| DS25C | Cave: mine stone/ore with native claws, then observe size-dependent upgrades | O harvesting |
| DS26C | Cave: cast Nether Breath, observe fire, respect environment and claims | O |
| DS27C | Cave: learn Fireball and practice on appropriate targets | O |
| DS28C | Cave: Furnace Heat on dropped smeltable input, collect output | O actual smelting |
| DS29C | Cave: Lava Vision and Lava Swimming; safe lava route | A cave/swim_in_lava / O |
| DS30C | Cave: Sturdy Skin/Friendly Fire with allies, Magic/Athletics/Burn upgrades | O buffs / P rank |
| DS31C | Cave: manage rain/water/cold penalty, Contrast Shower; no water immunity assumption | A cave/water_safety / O |
| DS25F | Forest: use axe claws, upgrade at growth stages | O |
| DS26F | Forest: toxic breath and Spike target practice | O |
| DS27F | Forest: cast Sun Breath on crop; optionally potato mutation/soil conversion | A forest/poisonous_potato / O growth |
| DS28F | Forest: Hunter camouflage and Inspiration allied Haste | O |
| DS29F | Forest: Magic/Athletics and Cliffhanger safe-fall upgrades | P / O |
| DS30F | Forest: avoid darkness Stress with light and native mitigation; thorn immunity | A forest/prevent_darkness_penalty, forest/stand_on_sweet_berries |
| DS25S | Sea: shovel claws, Amphibious and Diver; harvest underwater | O |
| DS26S | Sea: Storm Breath and Ball Lightning; demonstrate electrical effects safely | O |
| DS27S | Sea: Sea Eyes and Ore Glow, mine actual revealed ore | O |
| DS28S | Sea: Soul Revelation on eligible kill, compare native XP (not MXP) | O |
| DS29S | Sea: Magic/Athletics/Spectral Impact upgrades | P / O |
| DS30S | Sea: maintain hydration, water/rain/ice/snow and Sea Peace; Nether is optional | A sea/rain_dancing, sea/peace_in_nether / O |
| DS32 | Chosen species only: master its native paid passives and see level-dependent abilities separately | A species/master_all_passives; P |
| DS33 | Human/optional exploration: find overworld, Nether and End structure routes | A respective find_* |
| DS34 | Dragon Rider Workbench, establish native profession/trade | A light/dragon_rider_workbench; O trade |
| DS35 | Light key and vault, obtain matching upgrade template | A light/collect_key, light/open_vault |
| DS36 | Smith Light piece/full set, explain Kindness and Hunter Omen restriction | A light/get_armor_item, light/get_armor_set |
| DS37 | Separate Dark optional branch: hunter omen, key/vault, obtain Dark template | A dark/affected_by_hunter_omen, dark/collect_key, dark/open_vault |
| DS38 | Smith Dark armor, explain Outlaw and Animal Peace restriction | A dark/get_armor_item, dark/get_armor_set |
| DS39 | Separate Hunter faction: collect key, open vault, upgrade partisan and promotion | A hunter/collect_key, hunter/open_vault, hunter/promotion |
| DS40 | Demonstrate bolas on appropriate target, species/human pressure plates/doors | A hunter/fire_bolas / O |
| DS41 | Optional late TMM Dracolich informational link to Lich chapter; one provider only, no death requirement | P verified transformation, separate implementation |

Native research supports these 53 teaching rows (24 shared + 7 Cave + 6 Forest + 6 Sea + 10 closing rows). IDs are editorial not quest GUIDs. Use native advancements only after downstream criterion check; O/P where possession cannot prove mechanic. Reward proposal: small thematic ordinary materials or modest appropriate XP after quest balance review; do not duplicate expensive gate items, free resurrection, repeated flight grant or bypass graduation.

### 10.6 Final axis dispositions and boundary

Section 6 contains the 29-axis index. Resolve prior unknowns: storage = limited dedicated Source one-slot fuel / Dragon Soul character state, not general storage; logistics = limited ground-item fuel insertion, no routing network found; ore_generation = no normal native generator confirmed; permanent_pets = no independent creation/taming system confirmed (allied buffs do not create pets); temporary_pets = no normal species summon (test only); death_respawn = limited state/effect preservation and configurable hook, no default death save or grave. No additional independent industry/construction machine inferred. Native systems and numerical ceilings, gate recipes and teaching map are documented to owner-required depth. Remaining precision (exact village trade prices/gear proc ordering, every minor loot table, some conditional effect amplifiers) and actual loaded TMM settings are not completion gates; no claim of exhaustive reference manual. Cross-mod diet, custom modifier stack and death listener sequencing belong to downstream existing work, not fresh native QA. Source/JAR evidence sufficient for native quest-source-ready scope. Separate evaluation in dragon-survival-evaluation.md. No JAR redistribution; no tests/builds/quest edits. 
