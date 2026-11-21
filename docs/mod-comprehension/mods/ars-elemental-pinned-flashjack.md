# Ars Elemental 0.7.10.4 — Flashjack scout and familiar

Exact addon SHA1 `562f640916aa6f5c83b3b3ee2384ca828b870e01`; [artifact/guide](ars-elemental-pinned-guide-urn.md). Evidence: FlashjackEntity, FlashjackCharm, HijackTurretGoal, FlashLightning, FlashjackHolder/Familiar, registry/documentation/worldgen code and packaged recipe/biome/loot/lang; native Ars5.13.5 RotatingTurretTile and [turret audit](ars-nouveau-pinned-turrets.md), [familiar dispatch](ars-nouveau-pinned-familiars.md). Research2026-11-21; no runtime/client/ownership tests.

## Obtain the bird and give it a job

Read Notebook **Crafting → Flashjack Charm**, including its second scouting/turret page; **Archwood** entry has Flashpine information. Flashpine item is `ars_elemental:flashpine_pod`, registered as ArchfruitPod tied to Flashing logs. ModWorldgen's natural/sapling Flashing tree configurations pass this pod ID to MagicTrunkPlacer. Collect pods from Flashing Archwood; packaged pod block loot returns1, or3 when age2. Full tree/fruit renewal and worldgen controls remain in the pending worldgen branch; this does not promise a specific biome/chunk has a fruiting tree.

Wild `ars_elemental:flashjack_entity` biome modifier: weight7/groups1–2 in `#ars_elemental:flashjack_spawn`, including `#c:is_mountain/peak`, `#c:is_jungle`, `#ars_nouveau:archwood_biome`. Placement is ON_GROUND, checking block below in PARROTS_SPAWNABLE_ON and native brightness predicate. Eligibility is not a guaranteed encounter.

Give a wild bird one Flashpine on server. It sets BEING_TAMED, spends1, and after tamingTime>60 drops1–2 `ars_elemental:flashjack_shards`, spawns FlashLightning and discards the bird. Like Siren acquisition, this produces tokens rather than leaving that wild bird as a tamed worker. Unlike Siren's !taming guard, this interaction does **not** check BEING_TAMED before spending: repeated feeding during the delay can waste additional fruit without additional token batches.

JEI **Enchanting Apparatus**, `ars_elemental:flashjack_charm`: one Flashjack Token reagent, pedestals two Air Essences + two Gold Ingots, Source0, result one charm; keepNbtOfReagent=false. Use charm on a block with player present: spawn at click coordinates, set home to clicked position, copy charm color/name, tame to player, consume1 unless infinite materials. SummoningTile uses identical routine, no special station needed; no-player FAIL. Spawn result is unchecked.

Worker maxHealth36/flyingSpeed0.6/movement0.4/attackDamage6; no distance despawn, baseXP0, damage delegates through native SummonUtil.canSummonTakeDamage, canBeHitByProjectile returns false (it rejects projectile hits), thunderHit does nothing. It invokes native healOverTime, whose precise rate is not newly established here. Target acquisition: alive Enemy Mob, sight required, type not blacklisted. Empty-hand owner interaction while not flying and tame toggles ordered sitting. Its helper isTamed checks **resolved getOwner()!=null**, distinct from merely stored tame/owner UUID; owner-unavailable behavior needs acceptance.

### Configure wand links

- First-endpoint callback with stored RotatingTurretTile position appends that BlockPos to turret list. Use Dominion Wand's stored turret selection followed by bird endpoint. List permits duplicates; callback does not check owner, distance or GlobalPos dimension equality and stores only position. It checks block entity in player's current world.
- Last-endpoint callback with stored living entity toggles that **entity type** in blacklist, not one UUID. With stored non-turret block position and no entity, sets home. Both call inherited wand behavior afterward; observe messages/state rather than inferring exclusive handling.
- Home-return goal uses10, with condition target absent or home distanceSquared>600; its start clears target. This is AI behavior, not permission protection/chunk loading.

## Scout firing is conditional and still uses turret Source

HijackTurretGoal canUse requires resolved tamed bird, **nonempty turret list**, live target. Without at least one list entry, this goal's glowing, close spark and Walker warning do not run. Stale entries can still satisfy nonempty while yielding no actual turret shots.

Every goal tick applies Glowing40 to target. Within distance7 and attackCooldown≤0: spark damage4 request, animation/particles, attackCooldown40, then early return. Other ticks decrement that cooldown. Every cooldown20 pass otherwise:

1. For each stored position still a RotatingTurretTile, clip from target eye to turret center using COLLIDER/Fluid.NONE. Continue only when returned hit block position equals turret position. Aim at eye BlockPos using native fake player, snap rotations to needed rotations, call shootSpell.
2. Find alive native WealdWalkers within bird AABB inflated30; set each target to this target. No owner/alliance filter in this loop.
3. Set goal cooldown20. Close spark's early return delays this work on that tick.

Native RotatingTurretTile.shootSpell still requires server/nonempty spell and pays its positive getManaCost from linked Source capability (simulation then extraction), otherwise native multi-provider range10 lookup. It then dispatches supported cast-type behavior; payment precedes behavior lookup. Bird does not supply free turret mana or skip recipient spell checks. It calls shootSpell directly rather than requiring a redstone pulse in this method. Duplicated list entries can request multiple shots/payment; no added list deduplication or cooldown check here. Elemental turret subtype specifics remain part of pending device audit; proven common interface is RotatingTurretTile.

## Recovery, visual lightning and appearance

Tamed death-loot and tamed Dispel create charm with createCharmData; restore code copies color/name. Dispel has no caster-owner check, only not-removed/server/tamed. No turret/home/blacklist data is explicitly copied by fromCharmData; world entity persistence separately saves color/home, turret positions and blacklist type IDs. Untamed processing saves tamingTime/BEING_TAMED. Parent Parrot save handles its own tame state.

Owner recolors worker using yellow/red/blue dye tags to flashjack/flapjack/bluejay, consuming1 if variant differs, without explicit creative exemption. Owner Flashpine interaction spawns FlashLightning/animation **without shrinking fruit** in this branch. A nonowner with Flashpine sets processing and spends1 even on a bird owned by someone else; while isTamed remains true its conversion tick is gated out. Test owner absent before claiming this harmless.

FlashLightning overrides tick without normal LightningBolt.tick: initial server event powers lightning rod, clears copper oxidation and emits lightning game event; client sound/sky flash and lifetime repeats follow. It does **not** call vanilla fire spawning or entity thunderHit loop in this override. Thus do not promise charged creepers, mob conversion or lightning damage from the fruit interaction. Rod/copper/game-event consequences are more than a purely cosmetic particle.

## Familiar and book discrepancies

Holder `ars_elemental:flashjack_familiar` accepts instanceof FlashjackEntity, no owner/tame requirement. Follow [Binding/script/summon](ars-elemental-pinned-siren.md#bind-learn-and-summon-the-siren-familiar): original bird removed, plain script no copied worker data; main-hand use learns, duplicates preserve script, familiar screen summons. Familiar is not the turret scout worker.

Actual onModifier adds2 per callback for **Air-school** parts, broader than book's Lightning wording; two native modifier dispatchers can invoke it. Book20%movement cost differs from actual callback: currentCost−0.5×wholeSpellBaseCost, int result, gated by first spell part in list **Leap, Launch, Pull, Knockback, Blink, Glide**. These are effects, not ordinary first-form Method glyphs. A normal Self→Leap or Touch→Launch does not match because first part is Self/Touch. Do not advertise a generally functioning movement-spell discount from registration/prose alone; test constructible forms and actual event recipe first.

Owner Flashpine interaction consumes1 except infinite materials and grants owner SpeedI/NightVisionI6000ticks (five minutes at20TPS), familiar SpeedI6000. Dye branch checks player's main-hand dye tag but resolves color from interaction-hand stack; red→flapjack, yellow/default-other→flashjack, **no bluejay mapping here** and no dye shrink in this subclass. Worker/familiar appearance and food costs therefore differ.

## Quest / unrun acceptance handoff

Teach fruit acquisition → one wild conversion → JEI charm → actual owned summon → wand turret/home/blacklist setup → observed targeting/paid turret firing; link named Notebook pages and native turret spell/Source setup. Charm/token possession is only a proxy. Use manual operation acknowledgement unless actual detectors are implemented separately; modest XP, scout/perch building materials and limited Source-workshop supplies are safe reward directions. Familiar is a separate Binding/script/summon lesson with modest XP, not a free replacement guard.

Positive/boundary tests: one fruit versus repeated conversion feeding; wild/owned/nonowner/owner-offline processing; missing/stale/duplicate turret list; real Source cost and empty/unsupported spell; blocked turret sightline; same-type blacklist toggle; nearby differently-owned Walkers; sitting/home return; death/Dispel data loss; rod/copper event versus absent vanilla lightning entity hit; Air damage duplicate callbacks; normal Self→Leap fails first-effect discount; five-minute fruit buff versus worker no-consume lightning; dye-hand mismatch. No tests were run. Full tree/worldgen, turret addon subtypes and Flashing Walker remain unfinished elsewhere; whole addon stays researching.
