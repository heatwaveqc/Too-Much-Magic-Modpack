# Ars Elemental 0.7.10.4 — undead revival and Homing replay

Exact addon and bundled Sauce scope: [guide receipt](ars-elemental-pinned-guide-urn.md), [focus/dependency receipt](ars-elemental-pinned-foci.md). Direct NecroticFocus, SummonEvents, three undead subclasses and ModEntities; bundled Sauce IUndeadSummon. Parent exact Ars5.13.5 SummonWolf/SummonSkeleton/EntityAllyVex (including inner classes), CapabilityRegistry and SpellCastEvent freshly decompiled externally in `elemental-parent-fresh`; existing SpellContext/EntitySpellResolver/SpellResolver/LivingCaster used for recipient/payment. No runtime tests. Addon glyphs, Bee/Slime and helper/device/worldgen routes remain open.

## Start a combat-summon branch

Open Worn Notebook → **Spell Casting → Necrotic Focus** and native summon glyph pages. Craft/equip the [Necrotic Focus](ars-elemental-pinned-foci.md); actual revival and replay checks require it **worn in Curios**, not merely held. Learn a native summon on the [Scribes Table](ars-nouveau-pinned-guide-glyphs.md):

| Native glyph | Exact recipe inputs | Recipe XP |
| --- | --- | ---: |
|`ars_nouveau:glyph_summon_wolves`|Conjuration Essence, three Bones, four Wilden Wings|27|
|`ars_nouveau:glyph_summon_undead`|Conjuration Essence, Bone, Wither Skeleton Skull|160|
|`ars_nouveau:glyph_summon_vex`|Conjuration Essence, Totem of Undying|160|

These recipes are glyph acquisition, not a guarantee any low-tier book can cast every listed spell. Native validation, summoning duration and Summoning Sickness still apply. The [thread audit](ars-elemental-pinned-threads.md) records the broken advertised sickness reduction and duplicate summon-power listeners. Do not replace this route with spawning an arbitrary vanilla mob; the death listener tests native summon classes.

## What a qualifying death creates

`SummonEvents.reRaiseSummon` handles SummonEvent.Death on server, resolves owner through IFollowingSummon's summoner or ordinary summon owner, requires Player with worn Necrotic Focus, and excludes any summon already implementing **IUndeadSummon**. It does **not** check `didExpire` or require a nonnull damage cause. An eligible native timer expiry or dispel death event can therefore enter the same branch as combat death; this is not kill-only resurrection.

| Original native summon | New addon entity | Constructor behavior |
| --- | --- | --- |
|SummonWolf|`ars_elemental:summon_direwolf`|Same precise position; tame to player, aggressive=true, target player's last hurt mob, copy beneficial effects. Declared attributes movement0.5/health20/attack6.|
|SummonSkeleton|`ars_elemental:summon_wskeleton`|Same precise position; copy main-hand weapon, assign player owner, target player's last hurt mob, copy beneficial effects. Inherits native skeleton follow/owner-target/retaliation behavior.|
|EntityAllyVex|`ars_elemental:summon_vhex`|Move to old block position, finalize spawn, set player owner and bound origin, copy beneficial effects. Netherite Sword with0 drop chance; declared health20/attack8/movement0.6.|

After construction, bundled `IUndeadSummon.inherit` **overwrites** the new reported lifetime with `max(old.getTicksLeft(),400)`, assigns old owner UUID and sets old lifetime0. Then listener adds the new entity and death-poof visual. It does not copy all inventory/health/attributes/NBT. The Vhex constructor's initial `50+oldTicks` is not the final interface-reported lifetime after inherit. Already-undead exclusion prevents repeating this same upgrade indefinitely.

### Lifetime and persistence are different for the three families

Direwolf inherits native Wolf timer: server tick decrements ticksLeft, poofs/discards and posts expired-death event at≤0; actual death posts nonexpired event; Dispel sets ticksLeft0. Inherited wolf behavior remains, rather than a new custom undead AI loop.

Undead skeleton inherits native **starvation** expiration: tick decrements limitedLifeTicks, then at≤0 resets it to20 and requests20 starvation damage. Thus the lifetime is not a simple immediate discard deadline; damage/death handling can affect survival after the counter runs out. Dispel sets counter0. Native skeleton's goals include follow summoner, copy owner target, retaliation excluding owner and enemies targeting/credited to owner.

Vhex lifetime needs particular care: native EntityAllyVex declares its **own private** limitedLifeTicks returned/set by ISummon methods; it does not override vanilla Vex `setLimitedLife` or supply a tick decrement for that private field in the inspected class. Constructor calls inherited setLimitedLife, while inherit writes the interface field. Do not equate `getTicksLeft()==400` with a proven400-tick disappearance. Inherited vanilla Vex lifetime and this shadow field require runtime/vanilla-endpoint reconciliation; this is a known native boundary, not permission to patch it.

Direwolf and skeleton registrations use noSave only if `CompatUtils.isSummonRework()` is true. That flag is set when the **Ars Nouveau version string contains `SSR`**; pinned5.13.5 does not. Vhex uses registerEntity rather than the explicit noSave helper. This differs from [all four temporary mounts](ars-elemental-pinned-steeds.md); do not copy the mount noSave statement onto combat summons. Serialization/owner restoration are still acceptance boundaries.

## Replaying a Homing cast through owned undead

With worn Necrotic Focus, cast a spell whose **cast method is exactly addon Homing Projectile**. Its acquisition and projectile behavior belong to the remaining addon glyph audit; merely putting Homing in descriptive text or a different cast method is insufficient. NecroticFocus listens to SpellCastEvent in ServerLevel, requires event entity Player, and selects Mob instances implementing IUndeadSummon with matching owner UUID in the player's block-position AABB inflated30. No line-of-sight/radial-distance/payment-sharing check appears in this selection.

For each selected mob, look toward its current target, else player's last hurt mob if alive. Otherwise caller passes player view vector to LookControl (not an explicit world-space hit position). Clone the event SpellContext, replace wrapped caster with **LivingCaster(mob)**, construct EntitySpellResolver and call `onCast(ItemStack.EMPTY,world)`; return value is unchecked by this loop.

Recipient behavior is now traced:

- SpellContext.withWrappedCaster(LivingCaster) changes **both wrapped caster and unwrapped entity** to the summon. Clone retains spell and copies caster tool/tag/attachments as defined by native clone; passing EMPTY to onCast is not proof all retained context fields are cleared.
- EntitySpellResolver only overrides `enoughMana` to **true**. Native `canCast` still validates spell grammar, then `onCast` posts a cancellable SpellCastEvent and invokes cast method. A canceled/invalid replay is not a successful projectile.
- Native SpellCastEvent constructor uses context.getUnwrappedCaster; replay event entity is the summon. Therefore this specific Necrotic listener's **Player** condition fails for its own replay, preventing recursive fan-out through this listener.
- On `CastResolveType.SUCCESS`, native resolver **still calls expendMana**, through the new LivingCaster. LivingCaster queries that mob's mana capability and removes mana only if nonnull. Native CapabilityRegistry registers mana provider for **Player**, not these summons. Within native providers alone, this replay has no summon reserve to debit and does not charge the player again through this code path. External capability/listener integrations can change that, so do not generalize to every installed payment bridge or promise all replays are free/successful.

The original player's cast remains subject to its normal payment. Replay is triggered from the cast event **before** original cast resolution/payment success is established; it is not an after-hit proc. Other cancellation/listener ordering can therefore matter.

## Kill healing and remaining acceptance

NecroticFocus.lifeSteal is a separate LivingDeathEvent path: if source entity implements IUndeadSummon and a player owner is found in the server level, heal owner **2 health** immediately, then add follow-projectile visual. It has **no worn-focus check**, so unequipping after revival does not disable this method. Healing can still be changed/canceled by other listeners (including Frozen). [Empowering](ars-elemental-pinned-empowering.md) and [foci](ars-elemental-pinned-foci.md) describe those other boundaries.

Future explicit acceptance: combat versus expiry/dispel revival; original and new lifetime fields; nonrepeat undead death; saved owner and equipment/drop behavior; Vhex shadow lifetime; Homing spell acquisition/targeting;30-block query corners/line of sight; canceled original/replay cast; other mana capabilities/payment listeners; own versus another player's summons; unequip before kill/heal cap. Source walkthrough is usable with native flaws explicit, but full addon completion still requires its remaining glyph/helper/device/worldgen routes. No pilot node presently teaches this combat branch.
