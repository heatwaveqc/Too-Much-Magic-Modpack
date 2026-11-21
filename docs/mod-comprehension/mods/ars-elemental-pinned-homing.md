# Ars Elemental 0.7.10.4 — Homing Projectile and Propagate Homing

Exact hash scope: [guide receipt](ars-elemental-pinned-guide-urn.md), bundled Sauce [receipt](ars-elemental-pinned-foci.md). Direct MethodHomingProjectile/PropagatorHoming and two glyph recipes; exact Ars5.13.5 EntityHomingProjectileSpell freshly decompiled externally in `elemental-parent-fresh`, existing EntityProjectileSpell/MethodProjectile/SpellContext recipient; bundled IPropagator child-context bridge. This audits **two of39 addon glyph recipe routes**, not the full glyph set. No projectile/client tests.

## Learn and build

Worn Notebook → glyph search **Homing Projectile** / **Propagate Homing**; use native [Scribes Table](ars-nouveau-pinned-guide-glyphs.md) and a spellbook supporting **Tier3**. Both packaged glyph recipes have XP160. Acquire:

| Exact recipe/output `ars_elemental:` | Consumed Scribes Table inputs | Default glyph mana |
| --- | --- | ---: |
|`glyph_homing_projectile`|Nether Star, Manipulation Essence, native Dowsing Rod, Eye of Ender|75|
|`glyph_propagator_homing`|Manipulation Essence + **Homing Projectile glyph item**|400|

The second recipe consumes a glyph item, not simply checking that it was learned. Actual spell total includes effects/augments and modifiers; these are defaults rather than live-config measurements. Propagator is Manipulation school; the Homing form class itself provides no school override beyond its native AbstractCastMethod base.

For direct use, choose **Homing Projectile as cast method**, followed by desired legal effects. Cast toward a valid target; it seeks nearby living targets, not a chosen name or distant mob. By default players are excluded; **Sensitive** allows them subject to all other ignores. This is the exact method required by [Necrotic replay](ars-elemental-pinned-undead-summons.md); Propagate Homing as an effect under another cast method does not satisfy that listener's exact cast-method comparison.

For a propagated spell, put a suitable first cast method/effects, then **Propagate Homing**, then the effects the new projectile should carry. On resolving that point, bundled IPropagator creates a child context from the **remaining spell**, inserts Dampen as a dummy first part, constructs a new resolver and sets its castType to Homing; cancels original context continuation and invokes propagate. It directly constructs projectiles rather than calling a fresh resolver.onCast, so this propagation helper does not itself post a new ordinary cast event or perform a second ordinary cast payment. Initial spell payment and downstream effect-specific costs remain applicable.

## Launch and targeting

Direct cast constructs **1+Split count** native EntityHomingProjectileSpell entities, each sharing the passed resolver reference. Initial speed `max(0.2,0.5+accMultiplier/5)`, inaccuracy0.8; split yaw alternates around caster aim in5-degree steps. **Dampen enables gravity**. Compatible augments: Pierce, Split, Accelerate, Decelerate, Sensitive, Dampen; default own limits Pierce1/Dampen1. It returns SUCCESS after adding the projectiles, not after a target hit. onCastOnEntity launches from caster aim rather than directly locking its supplied entity argument; UseOnContext block overload launches, while BlockHitResult-only overload returns FAILURE.

Basic ignores: dead targets, shooter, familiars, Flashjack, Invisible status, allies; players unless Sensitive; applicable spell filters gathered from index0. Direct Player cast additionally ignores owned ISummon and owned OwnableEntity pets. Direct summon cast with an owner UUID ignores same-owner summons and pets. Propagate calls **basicIgnores only**, so it does not add these direct-cast ownership-specific predicates independently (allied exclusion still applies).

Native projectile acquisition occurs when target null and tickCount divisible3: query LivingEntity in projectile bounding box **inflated4**, filter, then select nearest with squared distance<75. Existing target is dropped when dead or squared distance>50. These are **squared distances**, not75/50-block radii. Existing target eligibility is not fully rechecked on every retained-target tick; acquisition/hit predicates can therefore differ after invisibility/other state changes. No line-of-sight filter appears in this selection, but ordinary collision handling remains.

When a target exists and its bounding box inflated0.2 does not intersect the projectile, home toward target **block position + (0.5,0.75,0.5)**. Target direction has magnitude0.3; velocity is blended using weight0 beyond distance3 and `(3−distance)×0.3` within3. This does not preserve launch speed as a fixed pursuit speed. Native homeTo skips adjustment for destination block **(0,0,0)**. With no target it follows ordinary projectile movement; no guarantee it finds every eligible target before hitting a wall.

Native canHitEntity combines ordinary projectile hit rules with the same ignore predicates for living entities. Server hit processing posts cancellable SpellProjectileHitEvent; ordinary block/entity hits resolve spell effects and consume a Pierce use, discarding when remaining Pierce becomes negative. Prisms/portals/bounce have separate native branches. Seeking through walls in the acquisition query is not proof of wall penetration.

## Propagation direction and limits

Propagated projectiles start **hit position+(0,1,0)**. Normally shoot along caster pitch/yaw with the same split fan. With **Extract** or FakePlayer shooter, use IPropagator.getDirection: default vector is **hit position−shooter position** (away from caster); rotating turret uses shoot angle, basic turret/rune their facing. Directional launch normalizes through native shoot; the Extract/fake-player branch does not add the normal split yaw fan. Compatible augments add Extract to Homing's set. Propagator registers itself in native EffectReset.RESET_LIMITS; this is a grammar restriction, not an unlimited recursive spell license.

### The addon lifetime config is not the actual timer recipient

MethodHomingProjectile declares **max_lifespan default30 seconds** but no inspected read uses its PROJECTILE_TTL field. Actual native EntityHomingProjectileSpell **does not override getExpirationTime**; parent EntityProjectileSpell reads native **MethodProjectile.getProjectileLifespan×20**, default **60 seconds**, and removes server projectile when age>that value or resolver null. Therefore don't claim this addon field enforces30 seconds. Parent native config and any installed mixins determine effective lifetime; this exact binary route is the evidence.

The same parent lifetime method audit also closes [Gravity field's](ars-elemental-pinned-empowering.md) accessor dispatch: its own override supplies70+200×duration ticks to the parent's age comparison. This is source dispatch confirmation, not a measured field duration.

## Acceptance and remaining coverage

Future explicit acceptance: Tier3 acquisition and consumed glyph item; actual mana/config; initial versus retained target eligibility; Sensitive/PvP/allies/own pets; invisibility/state changes; walls and claim cancellation; origin-block corner; split/acceleration/gravity; child remainder and Extract/turret direction; native versus unused addon lifespan config. Homing prism lens has a separate conversion/device route and is not completed merely by this projectile audit. Full addon status remains researching for37 other glyph recipe routes, tomes, helpers/devices/gear/worldgen and final walkthrough/pilot review.
