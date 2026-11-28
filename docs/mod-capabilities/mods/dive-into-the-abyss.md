# Dive Into the Abyss — first Atlas pass

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
