# Ars Elemental 0.7.10.4 — Arc Projectile and Propagate Arc

Exact artifact/hash: [guide receipt](ars-elemental-pinned-guide-urn.md). Direct MethodArcProjectile/PropagatorArc **including anonymous inner classes**, two packaged recipes, exact native Ars5.13.5 EntityProjectileSpell collision/lifetime and bundled Sauce IPropagator. `javap -c -p` independently checked all three anonymous constructors/getType overrides. No runtime projectile tests.

## Acquire and use the two Tier2 glyphs

Worn Notebook glyph search **Arc Projectile / Propagate Arc**; learn via [native Scribes Table](ars-nouveau-pinned-guide-glyphs.md), use Tier2-capable spellbook. Both recipe XP55:

| Exact recipe/output `ars_elemental:` | Consumed ingredients | Default glyph mana |
| --- | --- | ---: |
|`glyph_arc_projectile`|Arrow, Snowball, Slime Ball, Ender Pearl|10|
|`glyph_propagator_arc`|Manipulation Essence + **Arc Projectile glyph item**|150|

Choose Arc as the first cast method, followed by legal effects, to launch a gravity-affected projectile. Add Pierce for extra impacts/bounces, Split for extra projectiles, Accelerate/Decelerate for initial speed, Sensitive for outline-block collision. Gravity is built in, not an additional Dampen requirement. Propagate Arc instead belongs after an initial cast/effect and before the remainder it should carry; do not use it as a first cast method.

Direct Arc creates **1+Split count** projectiles. Initial speed is **`max(0.2,1+accMultiplier/2)`**, inaccuracy0.5, yaw split fan alternating in5-degree steps. Each starts at native caster projectile position plus vertical **`0.25×(shooterEyeHeight/1.62)`**. SUCCESS means launch was requested, not that the carried effect hit. Entity-click overload launches along caster aim rather than locks clicked target; UseOnContext block overload launches, BlockHitResult-only overload returns FAILURE.

## Native bounce, impact and lifespan recipient

The anonymous projectile constructors call **native EntityProjectileSpell(Level,resolver)**; apparent extra `this` in CFR output is the synthetic outer parameter, not a different native constructor. All three anonymous classes override only getType to native `SPELL_PROJ_ARC`; none overrides collision or expiration. They use the same passed resolver reference, not a newly paid cast for each split.

Parent constructor reads Pierce/Sensitive from spell buffs at index0. With gravity enabled and pierceLeft>0, a block hit can bounce: multiply the velocity component perpendicular to hit face by **−0.9**, leaving other components. Default branch still resolves the carried effect at the block and decrements Pierce through attemptRemoval, discarding when negative. If numSensitive>1, the bounce branch instead decrements Pierce and returns before ordinary block resolution. Thus “one extra bounce per Pierce” is a useful guide description, but extra Sensitive and block special cases change effect resolution.

Native hit tracing clips **OUTLINE** blocks with any Sensitive, otherwise COLLIDER, ignores fluids, checks entity collisions and NeoForge projectile-impact cancellation; server onHit posts native SpellProjectileHitEvent. Prisms, portals and native bounce have special handling. Do not claim an arc inherently ignores allies, claims or walls; it has no Homing ignore-predicate list and still uses ordinary native hit rules.

MethodArcProjectile declares its own max_lifespan default60 seconds but this field has no inspected read. Actual parent getExpirationTime uses **native MethodProjectile.getProjectileLifespan×20** (default60 seconds), removes server projectile when age>limit or resolver null. Defaults coincide, hiding the distinction; changing the addon field alone is not demonstrated to affect lifetime. [Homing audit](ars-elemental-pinned-homing.md) documents the same recipient discrepancy with different defaults.

## Propagation preserves a remainder, not a fresh paid ordinary cast

Bundled IPropagator.copyResolver makes child context from remaining spell, prepends dummy Dampen, constructs new resolver, sets castType Arc, cancels parent's continuation and directly invokes propagate. It does not call the new resolver.onCast, so no second ordinary cast/payment event occurs in this helper; original spell and downstream effect-specific costs remain. Propagator is Manipulation school and adds itself to native EffectReset.RESET_LIMITS.

Base projectile starts hit position+(0,1,0). Additional Split projectiles start at **integer block** offsets: for i1..Split, clockwise from shooter facing (opposite on even i), distancei, vertical integer truncation of `1.5×sizeRatio`; all then add0.25×sizeRatio vertically. This is spatial offsetting, unlike direct Arc's yaw fan.

Normally all propagated arcs use caster pitch/yaw, inaccuracy0.3. With Extract or FakePlayer, use direction **hit−caster position**, or rotating-turret shoot angle/basic turret or rune facing, inaccuracy0.8. Native shoot normalizes direction. Compatible augments: Pierce/Split/Accelerate/Decelerate/Sensitive, plus Extract for propagation. No separate Homing acquisition/seek/player eligibility behavior applies.

## Glyph route coverage checkpoint

Four recipe routes now have operational audits: **Arc / Propagate Arc here**, **Homing / Propagate Homing** in [companion](ars-elemental-pinned-homing.md). Remaining35: **12 filter recipes** (six positive/negative pairs: aerial, aquatic, fiery, insect, summon, undead), plus23 other effects. Other23 comprise Bubble Shield, Cauterize, Cavitate, Charm, Conflagrate, Conjure Terrain, Discharge, Envenom, Geyser, Life Link, Mist, Nullify Defense, Oxidize, Phantom Grasp, Poison Spores, Rage, Slip Feet, Spark, Spike, Summon Bee, Summon Slime, Watery Grave and Water Jet. Twelve caster-tome recipes are additional routes, not counted in39glyphs.

Future explicit acceptance: launch speed/aim/split offsets, Pierce and Sensitive combinations, carried-effect ordering after bounce, canceled impacts, original versus child context/payment, turret/rune Extract direction and actual native/addon lifespan settings. Full addon remains researching for filters/remaining effects/tomes/helper/device/gear/worldgen and final walkthrough/pilot review.
