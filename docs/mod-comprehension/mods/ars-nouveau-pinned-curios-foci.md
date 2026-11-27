# Ars Nouveau pinned mana curios, movement belts and foci

Exact5.13.5 JAR SHA1 `cdc946c63e1eb676f0e29e4640335c0ce68d7de3`. Direct nine functional items, three mundane precursor recipes, Curios tags, unstable-effect tag, all seven `items/curios` classes, ItemsRegistry **including anonymous inner siblings**, ArsNouveauCurio/CuriosUtil, KeyHandler/PacketGenericClientMessage, SummonEvent, SpellResolver, ShapersFocus consumers/EnchantedFallingBlock, Config/Documentation. CFR0.152. [Parent](ars-nouveau.md), [mana/modifier dispatch](ars-nouveau-pinned-casting.md), [summon/ritual distinction](ars-nouveau-pinned-rituals.md), [familiar bonuses](ars-nouveau-pinned-familiars.md). Alchemist's Crown belongs to remaining potion audit. No equip/client/multiplayer acceptance.

## Craft and equip

Notebook **Items**: Mana Boost/Mana Regeneration Amulets, Lesser/Greater Discount Rings, Levitation/Unstable Gifts Belts, Ring of Jumping. **Spell Casting**: Summoning Focus; Shaper's Focus with **Blocks**, **Entities**, **Examples** pages. JEI exact recipes/tags. These are optional paths after Source Gem/Apparatus rather than requirements to learn every glyph.

Mundane precursors have no mana powers in registration: Ring of Potential = Source-gem center plus eight Iron-nugget-tag items; Dull Trinket = Source-gem center plus four Iron nuggets at cardinal positions; Mundane Belt = Source-gem center, Leather left/right/below. Upgrades use Apparatus, SourceCost0, keepNbt=false. Nine output recipes follow; repeated ingredient counts are exact native tags/items, not necessarily literal vanilla item types.

| Output | Center reagent | Pedestal inputs |
| --- | --- | --- |
| ring_of_lesser_discount | ars_nouveau:ring_of_potential | #c:gems/diamond x4, #c:ender_pearls x2, #c:gems/source x2 |
| ring_of_greater_discount | ars_nouveau:ring_of_lesser_discount | #c:gems/diamond x4, #c:rods/blaze x2, #c:gems/source x2 |
| amulet_of_mana_boost | ars_nouveau:dull_trinket | #c:gems/diamond x3, #c:gems/source x5 |
| amulet_of_mana_regen | ars_nouveau:dull_trinket | #c:gems/diamond x2, #c:ingots/gold x2, #c:gems/source x4 |
| belt_of_levitation | ars_nouveau:mundane_belt | #c:ingots/gold x4, minecraft:feather x3, ars_nouveau:air_essence x1 |
| belt_of_unstable_gifts | ars_nouveau:mundane_belt | minecraft:sugar x1, #c:crops/nether_wart x1, #c:rods/blaze x1, #c:dusts/glowstone x1, minecraft:fermented_spider_eye x1, #c:dusts/redstone x1, minecraft:brewing_stand x1, #c:feathers x1 |
| jump_ring | ars_nouveau:ring_of_potential | ars_nouveau:wilden_wing x3, ars_nouveau:air_essence x1 |
| summon_focus | #c:storage_blocks/source | ars_nouveau:wilden_horn x1, ars_nouveau:wilden_spike x1, ars_nouveau:wilden_wing x1, ars_nouveau:wilden_tribute x1, #c:ingots/gold x1 |
| shapers_focus | ars_nouveau:manipulation_essence | #c:ingots/gold x1, minecraft:piston x1, minecraft:slime_block x1, #c:gems/diamond x1 |

Use Curios equipment UI and appropriate slot; native ArsNouveauCurio also permits equip-from-use subject to slot validity. Tags: `curios:ring` holds both Discount Rings and Jump Ring; `necklace` both Amulets; `belt` both Belts; `an_focus` two Foci. These tags don't prove current pack slot counts or unlimited simultaneous duplicate bonuses. CuriosUtil queries **equipped** curios, not ordinary backpack inventory. Carrying these items is not the same as wearing them.

## Mana and movement contracts

| Item | Actual native behavior |
| --- | --- |
| Lesser/Greater Discount Ring | Flat personal spell-cost discount10/20 respectively (not10%/20%). Both add MAX_MANA attribute10 and MANA_REGEN_BONUS1 through equipped attribute modifiers. Greater consumes Lesser in recipe; no higher mana/regen bonus just because discount increases. |
| Mana Boost Amulet | Equipped MAX_MANA+50; default regen boost0. |
| Mana Regeneration Amulet | Equipped MANA_REGEN_BONUS+3; default capacity boost0. These are attribute values in mana formulas, not measured points per real second. |
| Ring of Jumping | With equipped ring, each fresh configured jump-key press while client player airborne and no screen sends server action. Server checks ring/mana capability, requires current mana>=`jumpRingCost` (default30,0..10000) unless creative, and rejects spectator/flying/swimming. Debits30 then overwrites velocity: lookX/Z*0.7, Y=jump power+jump boost+0.1; resets fallDistance and fires living-jump hook. No one-extra-jump counter/cooldown in these handlers: repeated airborne presses consume repeatedly. Holding key/repeat action differs from fresh press. Server method itself doesn't repeat client's onGround guard; do not call client gating an authoritative server movement policy. |
| Belt of Levitation | Server, while airborne+sneaking and not flying: tests blocks directly below at offsets1..5. Any non-air -> Levitation5ticks amp2; all air -> Slow Falling5ticks amp2; resets fallDistance. “Ground” test is non-air, not collision/support validation. No personal mana/Source debit in this method. Separate client branch scales horizontal motion by1.1 while retaining originalY, unless sqrt(motion.length())>0.6; it runs while not flying and isn't restricted by the server sneaking/airborne condition. Don't promise ordinary creative flight or infinite ascent from its name. |
| Belt of Unstable Gifts | Equipped tick, server gameTime divisible120: picks random effect from loaded unstable_gifts tag and applies120ticks with random amplifier0..2. Empty table does nothing. No mana/Source debit. Native ten effects: Slow Falling, Night Vision, Conduit Power, Absorption, Strength, Fire Resistance, Haste, Speed, Regeneration, Resistance. Loaded tags can change this; random effect is not selectable/deterministic. |

Generic ManaUtil includes all equipped IManaDiscountEquipment plus eligible armor and casting tool. Final spell cost/availability has event layers and clamping behavior traced in casting companion; possession count does not establish attribute stacking under actual Curios IDs/slots. Slot API supplies modifier IDs. No inferred infinite/free casting or survival flight approval.

## Summoning Focus

Both native Foci implement ISpellModifierItem and add damageModifier1.0 when participating in item modifier dispatch; actual duplicate modifier-event dispatch and effect-specific interpretation are documented in familiar/casting companions. Do not translate this to universally one heart or one percent.

Summoning Focus requires a Wilden Tribute in its recipe, so it is a boss-linked optional branch. Registered event subscriber checks owner's equipped Focus server-side. On SummonEvent, multiplies summon ticksLeft by2; if summon exposes living entity, adds Strength500ticks amp2 and Speed500ticks amp1. This is the supported ISummon event channel, not automatic doubling of permanent workers/familiars or every other mod's pets.

On SpellCastEvent by equipped player with cast method in sympatheticMethods (native list contains **Self only**), selects ISummon living entities in player-block AABB inflated30, filters ownerAlt equal to that player, clones context with summon as LivingCaster and uses EntitySpellResolver.onCast. No separate owner mana debit in this handler; original cast and event ordering still matter. It doesn't test event.isCanceled before cloning in its body, so “original canceled” isn't a proven universal no-side-effects guarantee. Addons can extend method list. Native prose mentions Self/Orbit examples, but Orbit alone is not an additional cast-method registration here.

AbstractEffect.summonLivingEntity also checks equipped Summoning Focus after adding a living summon: creates child context, changes wrapped caster to that summon and resolves remaining effects on it through EntitySpellResolver. This is a separate continuation channel from sympathetic Self casts; owning an item alone does not prove every summon exposes this helper.

On distinct SummonEvent.Death (extends Event, not SummonEvent), if ownerAlt wears Focus and damage source has entity different from owner, calls source entity.hurt for5 with a thorns source built from that **source entity itself**. This isn't measured actual final damage or ordinary owner-attributed retaliation; armor/events may alter it. No distance/wasExpiration predicate in this body beyond nonnull source and owner check. Effective PvP/claims/other summon compatibility needs both-sided evidence.

## Shaper's Focus and changed target propagation

This modifies **rest-of-spell targeting**, not just damage. tryPropagateBlockSpell requires resolver.hasFocus (equipped curio via unwrapped caster), makes child context, cancels original context, then resolves child on changed block; delayed parent schedules child with remaining delay. Entity variant attaches child context to EnchantedFallingBlock and resolves on that moving entity, again preserving delayed path. This is not a new personal cast/payment cycle in these helpers and no material rollback is implemented here.

Exact native consumers read: block propagation from Break, Crush, Rotate, Phantom Block, Conjure Water, Exchange, Freeze, Place Block, Smelt, Evaporate and Snare grounding a falling block; entity propagation from Knockback, Launch, Cold Snap, Gravity, Flare, Pull and Leap. EnchantedFallingBlock landing also propagates to resulting block when it retains context/resolver. Each caller has its own eligibility/material/world-change order. Example: Break only propagates after harvest/claim/blacklist checks and successful breakExtraBlock; Place Block uses result!=FAIL rather than requiring a single SUCCESS enum. Focus does not make every effect target every block by itself.

Safe teaching example from guide: Freeze -> Break targets new ice with Focus; without it, later effect can remain aimed at original hit. Use small controlled water/ice setup, count material transformations, and avoid explosive guide examples near a base. Multi-target/AOE repeats child propagation, so assess workload and consumed blocks rather than treat it as one single target operation.

EnchantedFallingBlock.fall sets hurtEntities=true when resolver has Focus and baseDamage=9+spellStats.damageModifier. Its onHitEntity computes `ceil(clamp(min(speedLength,2.5)*baseDamage + blockDestroySpeed,0,2147483647))` before hurt call; destroy-speed read fallback1 on exception. This is formula-level attempted damage, not measured final damage/permission. Other creation paths (Cold Snap/Flare) may set their own base stats, so don't generalize fall factory formula to every thrown entity. Separate fall-damage routine also exists. Native guide's speed/hardness/damage claim is thus specifically implemented, with caller-dependent stats and ordinary damage events still material.

## Walkthrough and acceptance handoff

Offer equipment choices after Apparatus: precursor -> chosen upgrade -> equip correct slot -> measured effect. Focus lesson follows a known spell/summon and teaches changed target rather than requiring every focus. Item tasks detect only possession; actual equip, extra jump, summon copying and target propagation require manual observation or separate detectors. Reward XP and modest precursor supplies; avoid full advanced Focus/Tribute rewards as detection substitutes.

Unrun acceptance: equip vs carry and effective slot count; exact mana capacity/regen/flat discount deltas; Jump Ring mana threshold, repeated presses and swimming/flying rejection; Levitation non-air5range and fall safety; Unstable Gifts empty/changed tag and timing. Focus: same-owner versus other-owner summons, original cast canceled versus clone side effects, death retaliation source/actual damage, Freeze->Break with/without Focus, multiple affected blocks and delayed moving-block/landing path. Test safe materials, real claims/faction rules and actual resource/durability/mana outcomes before proposing farm or combat integration. No in-world PASS.
