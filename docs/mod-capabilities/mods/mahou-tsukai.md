# Mahou Tsukai — exact 1.36.31 native capability Atlas

**Status: native research COMPLETE at the agreed quest-authoring depth.** Pack-specific leyline/Fae runtime acceptance separately tracked in [#669](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/669), not a native research blocker. No runtime or balance changes.

## Evidence and scope
Manifest mahou-tsukai, mod ID mahoutsukai, Minecraft 1.21.1 NeoForge. Packwiz mods/mahou-tsukai.pw.toml, Modrinth 7jOxn6qJ/GrYWbBFj, owner-uploaded mahoutsukai-1.21.1-v1.36.31.jar SHA512 0c4f3bc823d681ad9cb00895d348d2b414201c4d397ca3aa6427b45b8d2b369e0bae8c1f354a9541e4205dadb58c49203d3570e2433d772c3eef1227bc8bfa5a exact match, 1611 ZIP entries CRC pass, 831 classes, 33 singular data/mahoutsukai/recipe records, 20 advancement records. 125 English Compendium/Funbook description localization records (including continuation pages and instructional notes, not 125 unique spells). Closed-source/ARR; direct compiled inspection via javap -p -c of relevant stepsword.mahoutsukai classes, recipe JSON, en_us localization, no redistribution. [Prior Comprehension](../../mod-comprehension/mods/mahou-tsukai.md), [first Atlas checkpoint](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/410#issuecomment-7141611082), [exact binary checkpoint](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/410#issuecomment-7142042040). [Official wiki](https://mahoutsukai.stepsword.com/) is newer than our pin; used for supplementary operational examples, exact JAR takes precedence. Numerical values below are **native defaults**, not assertions about installed server config. No full game test.

## Start, resource scopes and crafting
Mahou Tsukai Knowledge Compendium (mahoutsukai:guidebook) recipe DCC/DBC/DDD where D red dye, C paper, B leather. Read introduction/Performing Magic/Catalysts; book accessible initially, no sequential research flags identified for ordinary spells. Book describes all spell families and meaningful equipment. Dagger recipe " DC"/"ABD"/"BA " A purple dye B iron ingot C stick D gold ingot. Damage self using Dagger or other controlled positive damage (unless config dagger-only), quickly use Draw Mahoujin default M facing clear solid floor; first successful circle reveals Mahou display and root advancement. Rebind conflicts as needed. /showmahou toggles display per guide. Hammer makes1 powder, Mortar and Pestle2; actual 14 recipes for 7 catalysts; outputs are Mahou ingredients not smeltable ore doubling. Recipe tools' remainder semantics from JEI, don't assume identical for external dusts. Place cloth BEFORE drawing for cloth-required spells; apply three powder units in ANY order (UnorderedList, multiplicity matters). Empty-hand right-click completed cloth circle to collect scroll. Boundaries generally allow portable placement scrolls, some stationary circles have no cloth form. Toggle persistent circles empty-hand right-use; redstone inverts selected boundary status; comparator0 off/1 active, Alarm15 detecting. Spell-specific mana, blacklist, GameStages, per-caster active-circle cap; don't promise universal behavior.

Personal mana defaults cap200000, +1 max per100 spent, ratio0 and punishment defaults disabled; natural regen evaluated every100 server ticks, sleep recovery0.5 maximum, hunger factor1. The misleading config MANA_REGEN_PER_TICK=1 is used in regeneration evaluation, not automatically +1 every ordinary tick. Expenditure and current/max storage separate, MXP is external integration. Normal casting draws personal mana then carried Attuned Gems then nearby active circuits per guide; specific persistent spells may prioritize circuits. Attuner recipe lapis/gold ring (CBC/B B/CBC, C lapis B gold); shapeless attuner +diamond/emerald creates respective gem. Right-use gem to charge; default capacities diamond10000, emerald5000. 
Standard Mana Circuit: eight Fae Essence around Powdered Emerald. Magitech Circuit: six iron +three Powdered Emerald (DAD/DBD/DAD; A,B emerald D iron). First right-use claims; empty hand deposits own mana, sneak-use toggles, redstone inverts; Fae Essence toggles sharing where supported. Both default100000 storage, horizontal range10. No native FE/Source/EV/blood/EMC equivalence. Mystic Code (6 Spell Cloth +3 Powdered Gold) three scroll stacks, sneak-use inventory, default Y select, use selected scroll. First Sorcery (6 cloth+2 Fae Essence+Powdered Diamond) durability-limited reusable scrolls subject to blacklist; not every spell reusable. Fae Essence right-use creates unbound blood-circle equivalent, claim with right-use if desired; unbound circle/scroll usable by others, native automated scroll creation possibility, exact machine IO/pack compatibility not asserted.

## Leylines and Fae — permanent how-to and diagnostics
**They are not generated structures, blocks, placed features or biome decorations.** Compiled effects.Leylines calculates X/Z modulo distance (negative coordinates handled). No seed/chunk-generation prerequisite. Existing worlds have grid too. Tectonic cannot suppress the mathematical grid by terrain generation; it may conceal its fixed-altitude renderer. That is a plausible explanation for owner's difficulty, not a diagnosed pack failure.
Defaults from MTConfig$Server/$Client, RenderLeyLines, PlayerManaManager, ServerHandler, FaySightEyesSpellScroll:
| Setting | Native default / actual condition |
|---|---|
| LEY_DISTANCE / OFFSET | 300 / 0 |
| Major points | X=offset+n*distance, Z=offset+m*distance; e.g. 0,0;300,300;-300,600 |
| Grid | orthogonal sides and diagonals, crossing at square midpoint |
| Sample | 7x7 horizontal neighborhood; star3, cross2, line1; at 300,300 score27; off-grid375,330 score0 |
| LEY_RENDER_HEIGHT | Y70, not terrain surface |
| Render extent | horizontal +/-100 around player |
| LEY_BLACKLIST / LEY_DIMENSION_LIST | true / empty default list (Nether is example supplier, NOT actual default exclusion) |
| LEY_AFFECTS_MANA | true |
| LEY_PUNISHMENT / FACTOR / FLAT | -1 / .01 / false |
| Nonflat contribution | truncate(-1 + nearbyValue/19 * maxMana*.01); actual cadence every100 ticks, not continuous per tick |
| Fay Sight | YYY recipe on cloth, mahoutsukai:scroll_fay_sight, 100 Mahou, 600 ticks, mahoutsukai:fay_sight_eyes |
| FAE_SPAWN_RATE | 1.0; probability .1*rate per eligible non-sleep regen evaluation |
| Fae prerequisites | initialized magic, allowed dimension, mana deficit, nearbyValue>=13, per-player FaeSpawn flag true, air at player Y and random +0..9 X/Z |
| Fae flag | actual Fay Sight scroll enables, Faecake disables; subsequent scroll re-enables |
Player how-to: craft Fay Sight and reserve100 mana; use F3 to find a known point rather than roaming. Choose safe open terrain near Y70 with line of sight; cast, see triple-ring/star leypoint and lines. If terrain above70 obscures, relocate to lower terrain at another known point or safely inspect lower altitude. Do not dig blind/teleport to unverified Y. Actual regen independent of altitude and sight. Elytra ley boost exists in guide and LEY_ELYTRA_LIMITED_TO_FAY_SIGHT default false; don't confuse render absence with regen/boost absence. 
Fae: actual scroll sets spawn flag (effect command alone insufficient); maintain deficit near point, leave air at player height; reapply sight to see Fae, listen for bells. Random attempts every5s; no guarantee in one30s window. Loot1–2 Essence plus looting; native guide says breed with any Powdered Catalyst. Use essence for circles/circuits/First Sorcery; Faecake intentionally suppresses future spawning for player. 
**Troubleshooting order:** verify initialized magic/actual scroll/effective configs/dimension, known grid XZ and Y70 clear view; compare regen independent of rendering; check Fae flag/deficit/air/probability; compare shader/resource pack only as temporary diagnostic if renderer fails. No Mahou-named config TOML in inspected integration tree fe58b6e2c1e82227ea287cb33b5f603dc45f9437, actual installed configs not read. 
Targeted [QA #669](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/669): A exact artifact/bytecode PASS; B regeneration using verified OP2 /getmahou @s at point vs off-grid under matched food/capacity conditions, C render and natural Fae observation NOT RUN awaiting authorized exact candidate. No automatic game build, no defaults changed. Add coordinate lesson before circuit/Fae gate.

## All 53 native spells — recipes and meaningful operations
Compiled MahoujinRecipeRegistrar UnorderedList recipes exactly 53. I=Powdered Iron, Q=Quartz, E=Emerald, D=Diamond, G=Gold, P=Ender Pearl, Y=Eye of Ender (all mahoutsukai powdered items); letters denote multiplicity, **order irrelevant**. Form column C persistent/stationary, S cloth-required scroll, C/S dual per native guide; Cup and Probability use stationary pattern plus scroll. Class map needsCloth true for S; omitted entries treated as stationary, explicit false examples boundaries/Ascension; form guidance based on exact Compendium. 
| Family | Spell | Powders | Form | Action, result, significant limits |
|---|---|---|---|---|
| Boundary | Alarm | IIQ | C/S | place/toggle; alerts entering mobs/players, comparator15 detecting |
| Boundary | Drain Life | IIE | C/S | recurring nearby life drain; heals, then hunger, then mana; default radius10/upkeep5 per mana cycle; configured limit and exclusions |
| Boundary | Tangible | IID | C/S | invisible barrier blocks others/projectiles, caster passes; avoids solid/water replacement |
| Boundary | Gravity | IIG | C/S | increases nearby noncaster entity gravity |
| Boundary | Displacement | IIP | C/S | sends entrant to opposite side, searches upward for free space |
| Boundary | Enclosure | III | C/S | raises terrain layerwise, consumes mana; avoid protected/block entity terrain; not a permanent shield |
| Projection | Projection | DDQ | S | first release memorizes eligible damageable item, later copies; 1000 per attempt, default5 durability, enchant/item filters; stack memory, not arbitrary duplication |
| Projection | Weapon Projectiles | DDG | S | creates bow that fires tools/weapons; sneak mode projected only |
| Projection | Strengthening | DDI | S | temporary durability protection, damage/mining boost, offhand or first hotbar slot; key gate for artifacts |
| Projection | Proximity Projection | DDP | S | Crimson Black Keys, teleport to visible target and attack |
| Projection | Reality Marble | DDD | S | isolated arena; optional target, death condition for return with opponent, damage exit solo |
| Projection | Power Consolidation | DDE | C | lake and Caliburn/lance route below |
| Projection | Treasury | DDY | S | gauntlet fires eligible inventory/Ender Chest/supported bag tools; sneak cycles aimed/AOE/chains, mana per weapon, whitelist/blacklist |
| Displacement | Projectile | PPD | S | teleport to last shot arrow |
| Displacement | Ascension | PPG | C/S | circle transports entities/items to highest solid Y; scroll caster only |
| Displacement | Ordered | PPP | C | linked by placement order to nearby circles; crouch stops teleport |
| Displacement | Equivalent | PPE | C | sneak-use link two circles across distance/dimensions; requires something on BOTH, swaps entities/items |
| Displacement | Mental | PPQ | S | temporary out-of-body remote exploration; body remains behind |
| Displacement | Protective | PPI | S | finite arrow-avoidance teleports; charge not timer |
| Displacement | Scrying | PPY | C | drop named tag, observe matching creature/player surroundings briefly; supports Kodoku |
| Exchange | Catalyst | EEE | C | transforms native powdered catalysts randomly one-for-one (can same type), 50 mana/item, 4t scan, circuits-first |
| Exchange | Durability | EED | C | dropped tool or inventory below; damage into personal Mahou; default20t cycle, max10 gain, higher-durability tax, blacklist |
| Exchange | Damage | EEI | S | finite charges convert incoming damage to Mahou; Replica gate |
| Exchange | Chronal | EEQ | C | generates during half day centered on placement, drains other half; time skip delays cycle, not free skip |
| Exchange | Alchemical | EEG | C | once per day changes natural block beneath within same class; ore/soil/stone transformations, not crafted blocks or blanket ore creation |
| Exchange | Contract | EEP | C | two players each stand on nearby own circle, pay, allied bypass of certain effects; damage other breaks contract |
| Exchange | Immunity | EEY | S | repeating same damage type immune, new type double; self or target; Replica gate |
| Mystic | Explosive Mana | GGD | S | staff, charged large explosion/smaller AOE/beam; beam accelerates damage and destroys blocks |
| Mystic | Borrowed Authority | GGG | S | speed/power, charged jump/surface cling, hits launch through walls; hold attack while moving |
| Mystic | Rho Aias | GGI | S | directional shield repels mobs/deletes projectiles; sneak to bounce |
| Mystic | Shared Pain | GGE | S | bind target to caster's received damage/potions |
| Mystic | Spatial Disorientation | GGP | S | staff launches selected entity, or block-centered pull then throw, charge matters |
| Mystic | Cup of Heaven | GGQ | C/S | construct convex hull and inner circles, trigger scroll, prime factors map effects (below) |
| Familiar/Intelligence | Summon | QQQ | S | summon observing familiar, reports entities/blocks, chunk loads; right-use block filter, empty clear |
| Familiar | Recall | QQP | S | call familiar to player |
| Familiar | Shared Vision | QQG | S | see through target's eyes |
| Familiar | Exchange | QQE | S | swap position with familiar |
| Familiar | Garden | QQI | S | familiar mobile no-harm zone |
| Familiar | Butterfly Effect | QQD | S | delayed block place/break or attack triggered by butterfly contact; avoid important blocks |
| Eyes | Minor Clairvoyance | YYQ | S | predicted paths of certain creatures, not all mobs |
| Eyes | Binding | YYG | S | eye contact immobilizes, players can break contact |
| Eyes | Reversion | YYE | S | strips buffs and some native immunities (e.g. arrow resistance, breathing/fire) |
| Eyes | Death Collection | YYI | S | watch deaths to collect souls, spends saved souls to prevent own death even after sight ends; separate from Goety/XP |
| Eyes | Black Flame | YYD | S | temporary spreading black fire deals Wither, does not consume blocks |
| Eyes | Fay Sight | YYY | S | see leyline/Fae and enable spawn flag |
| Eyes | Insight | YYP | S | see target inventory/effects |
| Secret | Retribution | EID | S | swap health percentages with target, cost proportional to health-point difference |
| Secret | Presence Concealment | PID | S | two Rippers, single fog, dual-wield invisibility (breaks on attack temporarily), backstab |
| Secret | Gandr | GED | S | curse uses fraction of max mana, scales damage, cleanses own negative effects and spreads around impact |
| Secret | Geas | GEI | S | bind self against harming named/entity type or picking up item; unnamed mob geasa regen; burn scroll removes all, costs |
| Secret | Fallen Down | GPI | S | channeled grave/terrain effect, flight counters, sneak switches destructive burning beam |
| Secret | Probability Alter | GDQ | C/S | >=2 nearby circles each with chorus fruit; scroll makes Rule Breaker, number determines modulus |
| Secret | Selective Displacement | YPE | S | buff, tap key swap self with target, hold select two others, cooldown |

Cup: arrange outer convex polygon and inner circles; number prime factors determine debuff levels. Prime mapping2 Poison,3 Weakness,5 Blindness,7 Slow,11 Wither,13 Levitation. Example5 outer +4 inner = Blindness I/Poison II. Scroll activates all within outer polygon; cost varies area/effects, teach safe arena, not indiscriminate use. Rule Breaker uses dividend/modulus for requested random values, sneak-use increases dividend, timed buff or target hit; no guarantee all pack RNG sources affected. Geas/Contract ally semantics also Replica; no new class-based team rule. 
Exchange economy: Drain Life health/hunger before Mahou; do not teach universal infinite generator or stack bypass; durability taxes, damage charge limit, Chronal negative phase, Alchemical natural block classes, Catalyst native powders only. TMM Neo Vitae adapter #57 separate; do not restore disabled global dropped-item scanner or infer adapter runtime pass. Projection blacklist empty native list with diamond sword example not default denial; explicit whitelist/enchants lists and anvil behavior configurable, PROJECTION_ANVIL_CANCEL false, projection Caliburn transformation disallowed default. No blanket duplication or ore gate exception.

## Legendary weapons and unusual acquisition
**Caliburn:** Power Consolidation DDE circle on suitable floor, lake expansion 30 mana every20t, radius12 default, connected water threshold >150. Enchanted allowed SwordItem dropped by caster into lake, caster pays5000; consumes sword, returns new Caliburn, strips enchantments, repair by replacement; optional Apotheosis socket handling default on. Exact PowerConsolidationSpellEffect checks fun gate after lake threshold before conversion, projected input disallowed default. Simulated hit with/without enchantments, subtract nerf0.6 of difference (40% contribution), then subtract base1, global cap5,000,000; do not teach cap as immediate damage. Actual default83-entry tier list first100,150,200,250,300,350...; getLimitBreakInArea counts all allowed upgrade items (default minecraft:nether_star) in radius AABB around input, discards stacks, selects list[min(count,last index)] with first tier when zero. A higher existing Caliburn innate cap preserved by max(old,selected). **Not cumulative +1 tier per star per ritual**: offer all desired tier stars together, don't waste repeated single-star attempts. Enchant/reconsolidate improves damage within effective cap, preserves name, restores durability. Fear unholy entities (config/keywords), pull/right-use, Y Smite, sweep. Native durability1000, shared ability cost300 and cooldown600t default. 
**Morgan:** direct MorganEffect canTransform requires eligible entity whitelist default wolf, and if tameable its owner UUID must equal caster. Mainhand Caliburn kill triggers transform, new Morgan preserves damage value/durability and innate cap limited by global cap, projected Caliburn disallowed. Kill AbstractVillager for progression, default400 Mahou, +0.2 adult /+1 baby, repair30 per successful upgrade, capped; no further lake cap escalation. Teach adult option; no farming recommendation. Right-use evil ball pull/stab, Y Rage, shared cooldown. TMM ethics/balance/cap changes are owner decisions, not native assumptions.
**Clarent:** Strengthened eligible sword/weapon right-use mainhand near Ender Dragon breath cloud (within2 per official guide); absorbs cloud, stores blocked damage Vengeance, next attack releases, copies target weapon power, Wounded penalizes fast movement. **Emrys:** strengthened stick offhand while struck by lightning, offhand passive electricity with attacks, empty main hand focused zap; independent Funbook phantom lesson. **Replica:** strengthened vanilla shield main hand block with high Damage Exchange charges (>90% per official guide; bundled Compendium says maximum), Death Collection souls>=min(100,configured max) per guide, Immunity Exchange active, armor>15. Stores six damage types, choose via Y, blocking shockwave scales nearby allies, sprint block cover ally, sneak-block save location and block+key teleport nearby entities; break block toggles friendly-only teleport. Actual custom pack death handler may change soul saves. **Nobu:** strengthened projectile weapon distant hit (bundled guide200 blocks, configurable); auto-lock and three squad modes, tracking while aim, autonomous with height keys, per-construct and bullet mana, durability. **Rhongomyniad:** conditional Jousting lance integration; enchanted lance Power Consolidation, mounted mobility, plant with sneak-use, smites while fed; don't promise provider in pack. **Rippers, Keys, Gauntlet, Weapon Bow, Explosive/Spatial staffs, Rule Breaker:** respective S recipes above, modes by use/sneak and keys. 
**Mandated Funbook:** CALIBURN_FUN_REQUIRED false default, default all10 flags selectable, actual PowerConsolidationSpellEffect/MandatoryFun predicate; if enabled, failure gives book when space, selected nonempty flag list must be satisfied. No gate enablement approved. Ten teaching activities: Emrys phantom; Weapon Projectiles hit; Spatial launch; Borrowed Authority hit; Butterfly contact after block break; Geas item pickup prevention; Binding Eyes creeper; Shared Vision creeper; Nobu squad firing; Selective Displacement two other entities. Teach as optional independent mastery quests, not a fabricated compulsory chain.

## Artifacts, sympathetic magic, decor
William/R'lyeh: write signed work, strengthen book, place on Lectern. Style computed from textual variety/form, fixed on creation; fame from trade/other players reading, reduced by burning; mana cost scales style. Positive William gives mana while carried (strongest, not all additive) or placed distributes to owned nonfull circuits, negative R'lyeh drains nearby players; placed illusions affect entities. Book cloning recipe William; don't assume ordinary books grant mana. 
Kodoku shapeless dirt, carrot, potato, poisonous potato, rotten flesh, nether wart. Mob kills accumulate victim value+1; apply hungry worm to specimen, kill, collect fed worm. Burn near circuits for mana scaling stored value and ley proximity. For sympathetic magic active Scrying circle +inventory underneath with fed worms; drop Blaze Powder fire, Rabbit Foot hop, splash potion effect, Glowstone Dust glow, Rotten Flesh hunger, worm misfortune, Ender Pearl teleport (player contract required), Nether Wart confusion. High-value carried worm mitigates/reflection. Do not infer remote effects through arbitrary dimensions or unloaded targets. 
Mahoujin Projector decorative circle/runes, edit size/angle/color/speed; armor right-use copies wearable effect, stick copy/paste settings. Fog Projector particle/camera fog color/thickness, not damaging spell. Murky Water lake content, do not imply infinite fluid duplication. Familiar chunk loading and projector assets/performance subject to actual pack policy. 

## 29-axis crosswalk
| Axis | Native capability |
|---|---|
| gear | legendary swords/shield/staffs, projection weapons, Mystic Codes |
| movement | Ascension, Ordered/Equivalent/Selective/Projectile/Protective Displacement, familiar swap, Replica, Borrowed Authority, leylines elytra |
| ore_processing | powder crafting for catalysts only, not metal ore doubling |
| storage | personal Mahou, attuned gems, circuits, Mystic Code scrolls; not generic item storage |
| crafting | 33 ordinary recipes, 53 circle recipes, artifacts/transforms |
| logistics | circuits, Equivalent/Ordered item swaps, Treasury inventory access |
| buffs | Strengthening, Borrowed Authority, Geas, eyes, Rage |
| external_compatibility | optional Jousting/Apotheosis, EMI/JEI, conditional supported bags, TMM dust/MXP external |
| mob_farming | Drain Life and familiar detection, Fae breeding; no generic mob spawner |
| ore_generation | Alchemical Exchange natural ore conversion; no arbitrary infinite ore claim |
| permanent_power | max Mahou growth and sword damage/caps, Geas/contract |
| general_generation | sleep/ley/Drain Life/Durability/Damage/Chronal/William/Kodoku |
| direct_damage | staffs, Gandr, Black Flame, Retribution, weapons, drain |
| block_breaking | Enclosure/Butterfly/Borrowed Authority/Explosive beam/Fallen Down/Strengthening |
| permanent_pets | familiar observation/recall, Fae breedable but not proven tamed pet |
| temporary_pets | Nobu constructs, butterfly markers; not ordinary permanent mobs |
| defense | Tangible/Gravity/Displacement, Rho Aias, Replica, Immunity/Protective |
| healing_recovery | Drain Life, Retribution, weapon repair, Geas indirect regen |
| debuff_control | Binding/Reversion/Black Flame/Gandr/Cup/Garden/Contract |
| energy | player/item/block Mahou, separate death souls, damage charges, Kodoku values |
| automation_control | redstone inversion, comparator, stationary catalysts/durability, unbound Fae circles |
| agriculture | Alchemical natural material transforms, Fae breeding, no dedicated crop multiplier established |
| detection_information | Alarm, Insight, Clairvoyance, Scrying, Shared Vision, familiar |
| equipment_augmentation | Strengthening, projection, Caliburn tier, Morgan, wearable projector |
| world_manipulation | Enclosure, lake, Alchemical, arena, displacement, Fallen Down |
| world_content | deterministic leylines/Fae, Reality Marble, murky lake |
| death_respawn | Death Collection finite stored souls, Reality Marble escape; TMM death handler separate |
| trade_economy | William fame through trades/readers, Catalyst Exchange material conversion; no dedicated vendor |
| progression_tiers | circle unlock, capacity growth, Fae circuits, optional weapon gates/Funbook |

## Quest-source map — 55 instructional nodes
Teaching dependencies not mandatory full linear progression; optional branches. Native advancement proof where appropriate and available; possession not proof of activation. Actual quest task detector to be verified independently; honest manual lessons otherwise. Reward ordinary materials, not free legendary swords. 
| ID | Prerequisites | Lesson/action |
|---|---|---|
| MH01 | none | craft/read Compendium, damage source/Dagger |
| MH02 | MH01 | Draw Mahoujin, root and mana GUI |
| MH03 | MH02 | Hammer/Mortar and Pestle, seven catalyst types, unordered recipe |
| MH04 | MH03 | Alarm Boundary, toggle/comparator/redstone |
| MH05 | MH03 | cloth and scroll pickup/cast, forms distinction |
| MH06 | MH04 | capacity via expenditure, sleep/hunger and cadence |
| MH07 | MH05,MH06 | Fay Sight YYY, locate grid point with F3, Y70 |
| MH08 | MH07 | Fae deficit/flag/air, collect essence, Faecake |
| MH09 | MH06 | Attuner and gems, charge/consume |
| MH10 | MH06 | Magitech Circuit claim, transfer, active/redstone |
| MH11 | MH08,MH10 | Fae Circuit, sharing/unbound circles |
| MH12 | MH05 | Mystic Code, inventory/switch/use |
| MH13 | MH08,MH12 | First Sorcery limited reusable scrolls |
| MH14 | MH04 | Tangible/Gravity/Displacement boundaries |
| MH15 | MH04 | Enclosure, persistent terrain safety |
| MH16 | MH04,MH06 | Drain Life health/hunger/mana and upkeep |
| MH17 | MH06 | Durability Exchange dropped item/chest and tax |
| MH18 | MH06 | Chronal half-day cycle and negative phase |
| MH19 | MH10 | Catalyst Exchange native powder conversion |
| MH20 | MH03 | Alchemical natural-block conversion |
| MH21 | MH05 | Damage Exchange and charge count |
| MH22 | MH05 | Immunity Exchange repeated/new source |
| MH23 | MH04 | two-player Contract formation/break |
| MH24 | MH05 | Projectile/Ascension/Protective movement |
| MH25 | MH03 | Ordered/Equivalent linked circles and item swap |
| MH26 | MH05 | Mental Displacement and body safety |
| MH27 | MH03 | Scrying name tag and target view |
| MH28 | MH05 | Projection memorize/copy/durability limit |
| MH29 | MH05 | Strengthening and offhand target |
| MH30 | MH28 | Weapon Projectiles and valid hit, gauntlet modes |
| MH31 | MH05 | Proximity Keys and Reality Marble return conditions |
| MH32 | MH05 | Rho Aias/Shared Pain |
| MH33 | MH05 | Explosive/Spatial staff modes and terrain warnings |
| MH34 | MH05 | Borrowed Authority jump/hit |
| MH35 | MH05 | Cup polygon, prime-factor effects |
| MH36 | MH05 | Summon familiar/recall/filter |
| MH37 | MH36 | Exchange/Garden/Shared Vision |
| MH38 | MH05 | Butterfly delayed action, Funbook contact |
| MH39 | MH05 | Clairvoyance/Insight/Binding/Reversion/Black Flame |
| MH40 | MH05 | Death Collection watch deaths, finite reserve |
| MH41 | MH05 | Retribution/Gandr, health swap and cleanse |
| MH42 | MH05 | Geas item refusal, mana benefit and removal |
| MH43 | MH05 | Rippers, Fallen Down and terrain safety |
| MH44 | MH05 | Probability Alter circles+chorus, Rule Breaker |
| MH45 | MH05 | Selective swap two other entities |
| MH46 | MH29 | Clarent strengthened weapon and dragon breath |
| MH47 | MH29 | Emrys lightning and phantom |
| MH48 | MH29 | Nobu distant hit and squad controls |
| MH49 | MH29,MH21,MH22,MH40 | Replica shield prerequisites and ally/teleport |
| MH50 | MH06,MH10 | Power Consolidation lake and 5000 reserve |
| MH51 | MH50 | Caliburn enchant, cap via simultaneous Nether Stars, optional Funbook |
| MH52 | MH51 | Morgan transform, cap inheritance, upgrade and cooldown |
| MH53 | MH29,MH10 | William written book strength/lectern, style/fame and storage |
| MH54 | MH27,MH10 | Kodoku feed/collect/burn, sympathetic magic and counter |
| MH55 | MH03 | projectors, wearable settings/copy and fog |
Optional Rhongomyniad lesson only with verified provider; MH23 not mandatory solo, MH49 ally mechanic reference; all Funbook tasks can be represented by MH30/33/34/37/38/39/42/45/47/48. Do not enable mandatory Funbook by quest dependency unless owner approves. Native advancement IDs root, sword_in_the_lake, secret_calibur, morgan, clarent, emrys, replica, nobu, treasury_projection, kodoku, lesser_grail, greater_grail, bakuretsu, beam, boogie_woogie, breadcrumbs, butterfly_effect, flying_pigs, gandr (20 JSON files total including root; consult actual data for exact detector). No requirement to transcribe every spell damage/interval or optional integration. 

## Handoff
All 53 recipes, 29 axes, 55 instructional nodes, native resource economy, legendary equipment, Funbook and coordinate-based leyline how-to recorded. No native research blocker at agreed depth; some secondary exact item event predicates (Replica threshold, Clarent breath radius and Nobu distance) described with Compendium/current guide provenance rather than claiming independent compiled confirmation of every detail. Actual installed config, owner-requested leyline visibility/regeneration/Fae acceptance remain #669; no runtime PASS. TMM power/balance, protected ores, projected modded gear, MXP payments, Catalyst dust, cross-death policy separate. Next major target Forbidden & Arcanus. Goety #668 remains pending review; index and intake must be reconciled by designated integration writer after Goety/Mahou land, avoid conflicting edits.