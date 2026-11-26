# Dragon Survival 2.0.74 — Mod Capability Atlas

**Status: source-backed-partial, pass 1 of 3 (2026-11-25).** Evidence-only research, tracker [#410](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/410). Separate from TMM Path trees, integration and capability evaluation. No runtime changes.

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

Evidence: data/dragonsurvival/tags/dragonsurvival/dragon_ability/{cave_dragon,forest_dragon,sea_dragon,order,test_abilities}.json, data/dragonsurvival/dragonsurvival/dragon_ability/<id>.json, English ability descriptions. 44 normal species assignments (15 Cave, 14 Forest, 15 Sea). 17 separate test_* definitions, not credited as survival powers (includes test teleport, summon, heal and function). All IDs below use dragonsurvival namespace. Actions and upgrade curves next pass.

| Species | Ability IDs and normal player-facing effects |
|---|---|
| Cave | nether_breath: fire stream, ignites entities/items/blocks, destroys eligible blocks, age-dependent reach, blocked in rain/underwater; fire_ball: explosive projectile and fire; furnace_heat: smelts ground item entities, smaller stacks faster, pickup loses progress; lava_vision: clearer lava; sturdy_skin: armor buff to self/allies; friendly_fire: fire resistance to allies/pets; cave_magic: mana cap and hot-block recovery; cave_athletics: speed on stone; contrast_shower: longer cold/rain tolerance, not full water immunity; burn: chance on attacks, damage increases with victim movement; fire_immunity: innate fire immunity; lava_swimming: swim in lava but heat limits; cave_claws_and_teeth: tool-free stone/ore harvest scales with growth; cave_wings: flight; cave_spin: air/lava speed burst, End unlock. |
| Forest | forest_breath: toxic Drain damage, age range, Stress blocks; spike: piercing darts, weaker underwater; hunter: camouflage/invisibility in grass and speed, first attack bonus, breaks on taking damage or attacking; sun_breath: grow crops, mutate dirt, poison potatoes, Stress blocks; inspiration: Haste to self and nearby creatures; forest_magic: mana cap, grass/sun recovery; forest_athletics: speed on grass/wood; light_in_darkness: darkness tolerance; cliffhanger: softer landing; forest_immunity: cactus/thorn immunity; forest_claws_and_teeth: tool-free wood cutting; forest_wings: flight; forest_spin: air/water speed burst, End unlock. |
| Sea | storm_breath: sparks, Electrified spreads damage, charged creepers, storm lightning; ball_lightning: travelling damaging electrical ball, storm interaction; sea_eyes: clearer underwater vision; ore_glow: ore illumination for self/nearby players; soul_revelation: monster XP multiplier for allies; sea_magic: mana cap and wet-block recovery; sea_athletics: speed on wet/sandy/icy surfaces; hydration: longer land/Nether endurance; spectral_impact: chance to bypass armor; electric_immunity: lightning immunity; sea_claws_and_teeth: tool-free shovel harvest; amphibious: underwater breathing and swim speed; diver: underwater mining without speed penalty; sea_wings: flight; sea_spin: air/water speed burst, End unlock. |

Native penalties: Cave cold and water weakness; Forest darkness below light 4 can trigger Stress and rapidly drain food; Sea dehydration outside water (rain, ice, snow, water bottles rehydrate); fear causes animals to avoid dragons; item blacklist restricts wielding bows/shields/tridents etc. Exact species penalty tags, grace timers, exceptions and disabled-penalty datapack precedence next pass.

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

Pass 2: audit every 44 ability definition (upgrade requirements, activation, costs, cooldown, ranges, modifiers and exact max rank), species penalties, stage formulas, ManaHandler/DSAttributes/DragonGrowthHandler, wing/spin unlock, death handling, loot/trade/gear/enchantments and beacon/hoard mechanics. Pass 3: reconcile active TMM overrides, diet, dragon Paths and Dracolich without designing new behavior; finalize quest-source matrix and separate 11-domain evaluation. No gameplay test required for ordinary native behavior. Native resource census and pin hash verified offline only. No source-completion claim or full build.
