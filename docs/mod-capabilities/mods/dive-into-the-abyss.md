# Dive Into the Abyss — second Atlas pass

**Status: source-backed-partial / needs-research, 2026-11-27.** [Atlas #410](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/410). Independent dimension/adventure research; no shared index, quests, balance, configuration, runtime or build changes. Exact native mechanics only, effective TMM interactions separately identified.

## Evidence

Exact Drive file wardentools-1.1.4_neoforge_1.21.1.jar, 27,978,583 bytes. SHA512 d6436a30cb9339c63fa2c548685e61243d38b4814e4495f7cdc02294428b6be16acd066718b847d5fad8c99246baa7abc9fa4b283e09c13f7fae6443d758728a matches [Packwiz pin](../../../mods/dive-into-the-abyss.pw.toml), Modrinth x2qQNlIy/w30oDCUY. Python zipfile.testzip clean, 2286 entries,447 classes. Metadata ID wardentools, display Abyss, GPL-3.0, LuciferC137, MC[1.21.1,1.22), NeoForge>=21.1.0, no additional mandatory dependencies declared. 106 parsed recipe JSONs:67 shaped,17 shapeless,12 stonecutting,4 smelting,4 custom radiance_catalyst,2 blasting. 124 advancement JSONs,18 native nonrecipe; 98 loot-table JSON paths,65 tag records,11 actual biome and11 structure definitions/sets,1 dimension,1 enchantment. Distinguish biome tags from actual biome definitions. No Patchouli guide found; native Wind Journal and Wind Whisperer are custom narrative/instructional systems; full contents and trigger rules next pass. Parsed native recipes, advancement criteria, localization, selected loot records, exact teleportable tag, and javap -p -c on key machine, dimension, configuration, armor/effect and boss classes. Loot scan excluded directory entries; boss table has no ordinary loot pool, separate death handler still to inspect. All bare native IDs below use wardentools namespace and preserve upstream spelling (cristal, dysfunctionning).

## 1. Orientation and entry

The mod's narrative connects sculk, ancient cities, Corruption, purification, six neutral crystal types, temples and a final Contagion Incarnation encounter. No claim that every item is a hard-coded prerequisite: advancement ancestry is teaching order. Native dimension wardentools:abyssdim. Common config defaults abyssArmorProtection=9 (chestplate, scaled other pieces, allowed1–20, restart), wardenHeartLaser=10 (allowed1–1000), enableVoidFallTeleport=true.

FallIntoVoidEvent.playerFallIntoVoid listens to LivingDamageEvent.Pre, player, FELL_OUT_OF_WORLD damage, config enabled, server level, Abyss exists and not passenger. It sets position (old X,250,old X), then calls ModTeleporter.diveTo and changeDimension. The X-for-Z usage is literally in bytecode; final destination depends on helper. **No explicit source-dimension exclusion, no explicit cancellation or damage reset in this handler.** Not a universal void-death rescue guarantee. Coordinate with TMM Newbie dimension permissions, Voidscape and other void/death handling before teaching intentional fall. Do not force a void jump as quest action; native Abyss Diver provides alternative travel.

Native root advancement obtain minecraft:sculk. Warden Heart acquisition described by native corrupted_heart advancement as kill a Warden and collect its heart. WardenDeathEvent itself creates particles and spreads blocks (not proof actual heart delivery); exact heart source/drop rule follow-up. Heart shapeless→9 Corrupted Essence, reverse 9 essence→heart. Vessel: 4 glass+4 Pale Shards+1 Corrupted Essence. Dark Stick:3 Darktree planks→1. Abyss Diver:2 Dark Sticks+1 Corrupted Vessel, diagonal either orientation. Need local materials or legitimate surface/loot source; not assume first-entry prerequisite. Use Diver on tagged block; consumes1 durability before server teleport attempt, repair with Deep Fragment. Destination Abyss from any non-Abyss dimension, Overworld from Abyss, not passenger and destination loaded; ModTeleporter.diveSamePlace actual landing search next pass. Exact teleportable block tag: vanilla sculk/catalyst/sensor/shrieker; echo_block,echo_cristal,deepcristal,deepblock,solid_corruption,soul_spawner,wind_whisperer,corrupted_abyssalite. No unrestricted coordinate teleport. Native escape advancement specifically custom abyss_portal_used criterion, not proof using Diver triggers it. Escape portal after final encounter distinct from Diver return.

## 2. Dimension exploration, loot and navigation

Biomes: amethyst_cave,citrine_cave,echo_cave,malachite_cave,cristal_cave,white_forest,blinding_depth,pale_cave,ruby_cave,waste_land,deep_forest. Structures: cristal_ruin, ruby/citrine/pale/echo/amethyst/malachite temples, surface_ancient_city, ancient_citadel, abandoned_explorer_house, tree_sanctuary. Temple loot includes matching neutral crystals, books, potions, golden apples, depth berries, candles and bones. Explorer house table includes Crystal Resonator, Deep Ingots, Deep Crystal, sculk, echo shards and ordinary loot. Citadel table includes neutral crystal fragments, Corrupted Essence, Deep/Radiance Fragments, sculk, discs, potions and Ward/Silence templates. These are candidate loot entries, not guarantees or quantified weights. Surface city and tree sanctuary acquisition exact placement and loot next pass.

Crystal Resonator =4 amethyst shards+compass. Crystal Infuser is an actual five-slot world block entity, no ordinary crafting recipe found; locate naturally in relevant structure, exact template placement next pass. Slots0–3 four identical jewels matching block's CRYSTAL property (Crystal.fromItem), slot4 Resonator; beginInfuseIfAble schedules120 ticks (6s). completeInfuse checks resonator and first jewel, sets crystal data on existing resonator, clears all four jewel slots, retains resonator and triggers compass effect. COMPASS_EFFECT_DURATION2400 ticks (120s); actual structure targeting chain, reattuning and navigation effects next pass. Not an ore processor or generic item infusion station. Six jewels Amethyst, Citrine, Echo, Ruby, Malachite, Pale. Collect neutral blocks (9 fragments each including amethyst and echo shards) for actual cristals advancement, all six required. Ancient Citadel Map item exists; source and operation follow-up.

Wind Whisperer recipe8 Darktree leaves+Corrupted Vessel; Wind Journal = Noctilure Feather+book+Darktree leaves; native KnownWindWhispers and CompletedTasks player attachments and custom BookAccess mixin. Whistle five Darktree slabs. Exact whispers, progression context, journal rewards and direction mechanics next pass. Do not equate journal with Patchouli or claim it supplies every recipe.

## 3. Two material routes and purification

Both Radiance and Deep fragments form their respective crystals with 5 fragments in cross pattern; smelt400t or blast200t,2 XP per operation, one ingot. Each full armor set consumes24 ingots (120 fragments via crafting route, alternate loot naturally possible). Deep Block compress9 ingots, reversible. Native full-set milestones actually use effects_changed: Deep Corruption Vessel, Radiant Radiance Bringer. Actual armor material durability/toughness/config scaling, set and individual effects next pass. Base common chestplate protection9 vs vanilla netherite8; not proof total defense or better in all attributes. 
Radiant Staff = Pure Vessel+2 Dark Sticks. Abyssal Scythe =3 Deep Ingots+2 Dark Sticks. Radiant Spear =3 Radiance Ingots diagonally. Warden Heart laser config10 damage, exact charge/range/cooldown and targets next pass. Neutral jewels can be crafted from5 corresponding fragments or compressed into9-fragment blocks and decompressed. Citrine Fragment smelts to vanilla Amethyst Shard in50t, .1XP; no generic ore duplication demonstrated.

Radiance Catalyst = Radiant Core (8 Radiance Fragments+White Seed),4 glass+4 iron ingots. Actual inventory3 slots (fuel/input/output), internal CustomEnergyStorage1000, native purifyTime200t. Fuel Radiance Fragment burnTime51 ticks; whole Radiance Crystal251 ticks. tick charges +1 energy per burn tick while storage below max, consumes fuel at start and exits that tick, requires full1000 buffer before purification branch. Does not simply purify at first recipe-cost threshold. Refill/active progress and effective charge spent per conversion next pass. Custom energy class does not by itself establish external FE capability.
| Recipe input | Output | Encoded energy cost |
|---|---|---:|
| Corrupted Essence | Pure Essence |100|
| Corrupted Vessel | Pure Vessel |100|
| Warden Heart | Protector Heart |900|
| Dying Protector Heart | Protector Heart |600|

Alternate Protector Heart recipe9 Pure Essence. Purification of Warden Heart directly has same encoded cost as9 individual essence conversions, different transaction and recharge times. Radiant branch creates Pure Vessel and Protector Invoker; no requirement to choose one irreversible character faction established.
Sonic Blaster recipe6 Abyssalite+2 Pale Shards+redstone; actual sound/redstone behavior next. Gramophone, Soul Spawner, Golem Stone, Living Sprout, Black Lantern, Solid Corruption and reinforced glass present; functional behavior/acquisition to trace. No ordinary recipe for Crystal Infuser or Dysfunctionning Catalyst in native106.

## 4. Protector and final encounter

Protector Invoker =2 Dark Sticks+glass+Pure Vessel+Whitetree plank. Protector Heart placed in Invoker triggers summon_protector advancement. Invoker actual code tracks associated protector, searches spawn space, saves health in heart and validity, not blanket spawn-egg creation. ProtectorEntity.createAttribute base550 HP,30 attack damage,1 knockback resistance,.2 movement,30 follow range. Need activation, radius, persistence, targeting and heart degradation/repair semantics next pass. Dying Heart purification600 is a real recovery loop for this entity, not proof player resurrection.

Dysfunctionning Catalyst (actual upstream spelling) has six independent neutral crystal counters cap28 each, total charge cap72, eye progression cap40, update interval5, max fence level5 with35t intervals, summon delay200t. crystalsFullyCharged checks each counter exactly28. Correct placement, input consumption rates and real material counts not yet established: do not assume 28 items each simply from counter or swap internal counter names for jewel slots. isInCorrectSpot/readyToSummon/doSummoning/contagionDied and placeAbyssPortalBlock establish explicit encounter lifecycle; exact conditions and arena positioning final next pass. Contagion Incarnation compiled attributes max1000HP, attack10, speed.2, knockback resistance1, follow range80; damage attribute not full attack matrix. Multipart entity and corpse implementation; actual boss loot table empty, inspect custom corpse/reward route instead of claiming ordinary kill drops. Native advancement kill_contagion_incarnation; custom corruption_vessel and radiance_bringer criteria children of incarnation. Distinguish these endgame criteria from full armor effects (same names), verify exact postboss branch selection, rewards and whether mutually exclusive. Escape via native abyss portal separately from Diver teleport. 
Other entities: Deep Lurker (Lurker Eye loot), Pale Wanderer (Wanderer Paw), Noctilure (Noctilure Feather), Temper (Pale Shard), Parasyte, Shadow, Crystal Golem. Noctilure Treat recipe Deep Fruit+sugar+Blue Glow Berries; mount/taming and whistle behavior unverified. Shadow/Protector/Parasyte native ordinary loot pools empty. Four registered effect IDs: corrupted, corruption_vessel,radiance_bringer,purified. Potion Purified normal/splash/lingering. Stealth enchant max1, primary foot armor, supported armor, slots armor, weight5, anvil2, native global loot modifier for book from Ancient City. Actual vibration interception and effect magnitudes next.

## 5. All 18 native nonrecipe advancements

| Native ID | Exact criterion type / requirement | Parent |
|---|---|---|
| abyss | inventory sculk | root |
| corrupted_heart | inventory Warden Heart | abyss |
| the_abyss | changed_dimension to abyssdim | abyss |
| abyssdiver | inventory Abyss Diver | corrupted_heart |
| windwhisperer | inventory Wind Whisperer | corrupted_heart |
| radiance | inventory Radiance Fragment | the_abyss |
| deepcristal | inventory Deep Crystal | the_abyss |
| cristals | inventory all six neutral crystal blocks | the_abyss |
| radiance_catalyst | inventory Catalyst | radiance |
| protector_invoker | inventory Invoker | radiance |
| protector | custom summon_protector | protector_invoker |
| radiant_armor | effects_changed Radiance Bringer | radiance_catalyst |
| deep_armor | effects_changed Corruption Vessel | deepcristal |
| corrupted | effects_changed Corrupted | deepcristal |
| incarnation | player_killed_entity Contagion Incarnation | deepcristal |
| escape | custom abyss_portal_used | abyssdiver |
| corruption_vessel | custom corruption_vessel | incarnation |
| radiance_bringer | custom radiance_bringer | incarnation |

Native Wind Journal reward/loot records are separate from these 18 progression IDs. Future quest-source map must not use mere inventory for native action criteria, must preserve actual triggers and distinguish advancement ancestry from hard-coded gate. No gameplay test requirement for native closure.

## 6. Preliminary 29-axis map

| Axis | Evidence disposition |
|---|---|
| gear | Deep and Radiant armor, scythe/spear/staff; numeric effects next |
| movement | Diver/escape portal and native mount candidates |
| ore_processing | crystal fragment craft/smelt, no ore multiplier identified |
| storage | two chest boat families and machine buffers, no warehouse established |
| crafting | native recipes, purification |
| logistics | chest boats, no general routing confirmed |
| buffs | four effects, armor and purified potion |
| external_compatibility | common vanilla materials, NeoForge event hooks |
| mob_farming | Protector/Soul Spawner mechanics next, no autonomous farm inferred |
| ore_generation | natural deposits only; no generator demonstrated |
| permanent_power | postboss vessel/bringer criteria require handler trace |
| general_generation | native crops and creature resource loops, not unlimited |
| direct_damage | Heart laser, scythe/spear/staff, Protector and boss |
| block_breaking | no substantial new general mining tool established |
| permanent_pets | Protector invoker, Noctilure candidate, actual ownership next |
| temporary_pets | unconfirmed |
| defense | two armor sets, Purified, Protector |
| healing_recovery | entity heart restoration and effect candidate; actual player healing next |
| debuff_control | purification and corruption effects |
| energy | finite Radiance buffer and catalyst charge; no external FE assumed |
| automation_control | Sonic Blaster/Soul Spawner follow-up |
| agriculture | native wood/fruit/berries; growth/renewable loops follow-up |
| detection_information | Crystal Resonator/Infuser, map, Wind Journal |
| equipment_augmentation | Stealth and crystal attunement; named ornaments not confirmed usable |
| world_manipulation | boss arena fence/portal and corruption behavior; exact limits next |
| world_content |11 biomes,11 structures, creatures and dimension |
| death_respawn | void teleport attempt not guaranteed rescue; no player resurrection established |
| trade_economy | no confirmed trade system |
| progression_tiers | native18 advancements, two material routes, six crystal gate and boss |

## 7. Next pass

Exact teleporter landing logic and dimension escape, Warden Heart drop/laser, actual Radiance Catalyst full-buffer cost/recharge and automation, six crystal Infuser navigation and temple positioning, Dysfunctionning Catalyst per-item input/charge and arena conditions, boss custom corpse/rewards and postboss allegiance, Protector and Noctilure/Crystal Golem, armor/effect/weapon numeric handlers, Wind Whispers/journal/Stealth and meaningful item acquisition. Named Mind Tiara/Ring of Will/Strength Bracelet/Pendant of Balance/Shadow Ornament/Light Ornament language alone does not prove usable mechanics; check registry. Confirm actual pack-specific cross-mod source paths (current index direct match only pin; generic policies may apply). Final pass: complete significant 29-axis quest-source map, all18 native advancements, separate11-domain evaluation; no full game test, new quest authoring, balance change or mod update. No rating assigned on this checkpoint.

## 8. Second pass — compiled operating conditions

Reopened exact wardentools1.1.4 JAR; SHA512 and CRC match §Evidence. Also compared exact Voidscape1.21.1-1.9.588 JAR: SHA512 704b7358c3b961a54b7ca3ac566d976faf026464188aaab979658a208d0c4210a47dd78816dad1558702e0cd9cf4821a71c96896bd673ba0d87e49d252db6759, CRC clean. Inspected javap -p -c of 34 Abyss classes plus ArmorWithEffectItem, ConfigurableDefenseItem, ModMaterials and switch-map, WardenLaserAttack, TemperEntity, ServerPayloadHandler and other item classes. Native conditions below supersede first-pass provisional notes.

### 8.1 Bedrock entry clarification and teleport

**Voidscape is the bedrock-standing dimension.** Exact tamaized.voidscape.data.Insanity.canTeleport/shouldTeleport require Y<=minBuildHeight+15, block at getOnPos is BEDROCK, dimension list membership matches configured whitelist boolean, every20 entity ticks rolls random.nextInt(8)==0. Not a fixed standing timer; per eligible check 12.5% chance, expected8 eligible seconds. Actual teleport path has separate entity eligibility. Abyss instead uses FallIntoVoidEvent and Abyss Diver as in §1; bedrock is involved in its boss arena, not a standing-based entry trigger. No recommendation to deliberately fall into void given pack policy interactions.

ModTeleporter.diveSamePlace resets camera, stops riding, uses same block coordinates in destination then findValidSpawn(...,false). Search at most300 upward iterations in 2-block steps, exits if current or above block is air or canBeReplaced with water; returns position one above found candidate, or original position if limit reached. This is NOT a robust guarantee of two-block free space, solid footing or claim-safe landing. diveToAncientCity searches upward from Y−60 at selected X/Z for reinforced deepslate, at most300, then inFrontOfAncientPortal; not a universal return-to-bed. Void fall handler direct diveTo overload does not call findValidSpawn. Preserve source event caveats. Native Radiant Staff has a branch that triggers AbyssPortalCriteria, so escape advancement is not necessarily exclusively a physical portal crossing; exact staff action/portal travel final narrow pass.

### 8.2 Armor and actual allegiance gate

Both Deep and Radiant material maps default helmet5/chest9/legs7/boots5 =26 raw armor points (engine/other mod caps separate). Config C chest protection: head/feet floor(5C/9), legs floor(7C/9), chest C. ConfigurableDefenseItem.updateDefense memoizes per-slot modifiers with ADD_VALUE, armor toughness and knockback resistance from material. Both material definitions use toughness4 and knockback resistance.1 per piece, enchantability15 (full set raw toughness16, KB resistance.4). This is not measured effective reduction or automatic advantage over every enchanted Netherite loadout.
**Crucial progression correction:** ArmorWithEffectItem.hasCorrectArmorOn requires all four matching material pieces AND completed player advancement wardentools:corruption_vessel or wardentools:radiance_bringer respectively. Merely wearing full armor before selection does not grant powers. Chestplate inventoryTick refreshes matching level1 effect instance duration242t (~12.1s), so removal/switching need not instantly remove existing effect. Earlier full-set criteria radiant_armor/deep_armor detect the effect itself, not possession. ServerPayloadHandler.switchAchievement handles index0 Radiance, revokes every criterion of corruption_vessel then triggers RadianceBringerCriteria; index1 vice versa. **Native selection is switchable and mutually exclusive at advancement level, not an irreversible Path.** Server method checks only authenticated ServerPlayer and index; no explicit boss completion or proximity predicate in this method. Normal UI/story gate and completed-task availability final pass; do not teach packet invocation or claim advancement parent is security gate. TMM's actual progression enforcement separate.

| Effect | Compiled operation |
|---|---|
| corrupted | 1+amplifier damage each20 duration ticks, custom corrupted damage type, skips if Purified active |
| purified | removes Corrupted every20 duration ticks on server, Corrupted damage also suppressed while Purified present |
| corruption_vessel | no independent periodic effect; Warden Heart checks presence to enable sonic attack |
| radiance_bringer | player tickCount modulo 200/(amp+1); if fewer than3 owned Tempers in axis-aligned ±30-block cube, spawn one in first air position within horizontal radius1–3 at player on-position Y; sets player invoker |

Radiance Bringer level1 nominally attempts one summon per10s, local cap3, not global cap or guaranteed spawn. Temper tick has Radiance Bringer owner predicate; detailed attack/lifetime final pass. Corrupted fatal damage spawns Shadow via LivingDeathEvent: player/villager stasis variant, other living entity mimics original; Shadow itself excluded. No player revival or proof ownership of Shadow. Warden Heart use: server + Corruption Vessel effect required; cooldown120t (6s), source pos player+1Y, look direction, range14, calls WardenLaserAttack.tick; no item consumption in use method. Default damage config10. Need damage type, line filtering and actual loot source final pass (exact JAR has no Warden Heart loot-table entry; native advancement description says Warden kill, not confirmed actual delivery). 

### 8.3 Dysfunctionning Catalyst exact summoning transaction

Six slots in order Amethyst, Citrine, Echo, Ruby, Malachite, Pale (confirm names via tick slot predicates). Each matching jewel increments its separate counter by1 on 5-tick update; removing it causes counter decay by1 per update. No stack shrink during charging. At all six=28, total charge rises1/tick toward72; if any counter drops while total<72, total decays. Once total72 and correct arena, doSummoning schedules fence35t and summon200t, consumes exactly1 item from each inventory slot via removeInventory. **Not 28 jewels per type; only one of each.** Nominal charge from fresh counters ~7s +3.6s at20TPS (one-tick overlap), then10s summon schedule. Eye progression40 is visual/internal subsequent counter. isInCorrectSpot checks BEDROCK exactly two blocks below each getFountainBelowPositions candidate; use actual naturally generated arena, not random flat bedrock or fabricated arbitrary-coordinate ritual. Wrong position prevents automatic summon; total-charge state and recovery after relocating still final. Fence levels5 at35t intervals; no arbitrary permanent player structure generation inferred. Actual Contagion boss death calls lifecycle method for portal, corpse separate. Ordinary boss loot table empty; corpse brokenByPlayer/brokenByAnything dropAllDeathLoot code exists but exact reward item/selection and native UI gate final pass. Do not claim boss per-player loot guarantee.

### 8.4 Purification cycle and companions

Radiance Catalyst tick confirms when buffer1000 and matching input/output, purifyingTime increments; at200 progress spends recipe cost, consumes1 input, creates output or grows matching output1; if output mismatches no conversion. Every cycle must regain full buffer, even cheap recipes. Native burnTime51/251 includes startup/tick details, not guaranteed exact51/251 delivered energy. With nominal1 energy/tick, ideal refill time100/600/900 ticks respectively plus200 processing and fuel overhead; no claim external FE charging or hopper capability. A 100-energy recipe therefore approximately15s from previous full-cycle completion if uninterrupted, versus40s/55s for 600/900. Initial cold fill about50s plus10s processing. No extra item recipes beyond four native. 
Protector Invoker heart stores health plus entity ID and UUID; isProtectorValid requires heart and matches ID or persistent UUID (refreshes ID). Existing protector search local AABB; entity linked to invoker position, not ordinary tamed companion. Death/health save replaces depleted heart with Dying Protector Heart. Entity tick checks invoker, despawn lifecycle when invalid; exact patrol radius/target/heal from final handler sweep. Reinsert/reinvoke restored heart at valid open area; no portable invulnerable army or free full-heal on removing heart established. 
Noctilure actual TamableAnimal:30HP, base movement .2, attack2, flying speed attribute .1; right-click Noctilure Treat attempts server tame random.nextInt(0,4)==0, consumes one mainhand item; 25% per attempt. Owner UUID persisted, owner control, flying travel/sprint energy2000, call/join owner and safe dismount helper present. This is actual rideable flying mount progression, not merely decorative mob. Exact mount/control, stamina depletion/recharge, whistle range and breeding eligibility next pass. 
Crystal Resonator.getNearestTemplePos uses selected crystal.getTempleKey and StructureUtils.findNearestStructure; corresponding target must exist. No generic arbitrary locator. Crystal Infuser activation §2 consumes four jewels at120t, compass particle effect2400t is not necessarily target lifetime; target data persisted on stack. WindWhispers custom map and KnownWindWhispers player attachment; WhisperManager timer starts500t and random contextual whisper logic, no fixed unlock per25s asserted. Stealth book global loot modifier explicitly minecraft:chests/ancient_city; exact chance/guarantee next. 

### 8.5 Final-pass boundary

Remaining significant quest-source questions: exact Wind Journal task and switching UI, Warden Heart acquisition, Radiant Staff escape operation, corpse reward, named weapon/effect and Temper/Protector/Noctilure mechanics, Stealth vibration handling, Soul Spawner/Sonic Blaster and actual registered ornament availability. Then actionable instructional map covering all18 advancements and separate11-domain rating. No need to exhaust minor damage coefficients or ordinary JEI recipes. No runtime/game/quest/config/pin/build changes. Voidfall/dimension restrictions, switch packet authorization, generated arena world mutation and effective loaded armor/loot values are targeted TMM integration questions, not demonstrated exploits or fixes.
