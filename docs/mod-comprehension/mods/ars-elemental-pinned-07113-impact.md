# Ars Elemental 0.7.11.3 — Horn, Life Link and Homing impact review

Exact current addon SHA512 and integration pin: [delta receipt](ars-elemental-pinned-utility-rituals.md). Direct CFR0.152 old/new body comparison and exact recipe resources; native Ars remains5.13.5. Current shared Sauce74.113 is audited in [recipient report](ars-family-pinned-sauce-74113.md). Integration `17a564d85b5ee3f0f4f830b68397e54a824a768e` adds [Ars Conflux](ars-conflux.md); native/addon bodies here are not proof of behavior after its resolver/carrier overlays. No client or gameplay tests.

## Enchanter's Horn: change its instrument sound

The [historical Horn report](ars-elemental-pinned-horn.md) still supplies acquisition, methodless Scribes Table fragment/prepended Self, hold threshold10, AABB inflation0.5+elapsed/10, one resolver target loop then one expendMana, long-hold buff at150 and200tick cooldown. Direct body comparison finds these operations unchanged; changed release code selects an instrument before sound playback. Do not infer a new mana admission check from this cosmetic update, or assume Conflux's ordinary onCast injections cover Horn's direct onResolveEffect route.

Current recipe `ars_elemental:set_horn_sound`, serializer `ars_elemental:set_horn`: shapeless **one Goat Horn + one Enchanter's Horn → one Enchanter's Horn**. SetHornRecipe inherits ShapelessRecipe matching. Assemble first copies component patches from input stack matching output item, preserving stored spell/components, then copies Goat Horn DataComponents.INSTRUMENT when present. No custom getRemainingItems override in this class: it does not itself return the Goat Horn as a reusable tuning tool. Treat both recipe ingredients as consumed under ordinary crafting, and inspect any effective pack remainder override separately. No Source fee.

Use JEI's **Crafting** recipe for the output Horn. Its custom category enumerates GOAT_HORNS-tag instrument choices; focused Goat Horn input previews its instrument, otherwise cycles constructed Goat Horn/output component pairs. Preview icons are not extra products or a second output count. Crafting outside the viewer uses actual input component, not a random JEI selection.

SpellHorn.getInstrument uses stored component first; absent component falls back to first entry in built-in GOAT_HORNS instrument tag. If that tag has no entry, legacy generic horn sound routine remains fallback. Instrument playback uses instrument.soundEvent, RECORDS sound source, range-derived volume and INSTRUMENT_PLAY game event. It does **not** use instrument.useDuration/range to replace Horn casting duration or range. Tooltip adds translated instrument key when available. This is optional sound customization, not a casting tier/unlock.

Future optional teaching: craft original Horn by Apparatus, inscribe effect fragment through Scribes Table, acquire chosen Goat Horn, craft together using JEI, verify stored spell and chosen sound. Inventory output is only an acquisition proxy; manual sound/spell check is required. Small positive XP and ordinary thematic supplies, no repeated tuning payout. Test absent-INSTRUMENT input, stored custom name/spell/color, actual Goat Horn remainder and sync. No live verification.

## Life Link: configurable sharing fractions

EffectLifeLink adds two default doubles under its native glyph config: `dmg_link=0.5`, `heal_link=0.5`, each clamped0..1. These are **artifact defaults**, not a checked effective generated config. Applying link and carried owner/target references are unchanged from [Anima report](ars-elemental-pinned-anima.md).

In linked-target LivingHealEvent, when carried target matches event entity and owner is alive: owner.heal(originalAmount×heal_link), then event amount becomes originalAmount×(1−heal_link). On owner's LivingDamageEvent.Pre, unless source bypasses invulnerability, when owner matches event entity and linked target alive: target.hurt(sameDamageSource, newDamage×dmg_link), then owner event damage becomes newDamage×(1−dmg_link). The target hurt return is ignored; protected/rejected recipient damage still does not restore owner's diverted fraction. Fractions0and1 are meaningful boundaries; defaults reproduce historical half sharing but amounts are no longer hard-coded halves.

Dead/missing linked partner removes effect. This is not symmetric every-entity damage/heal distribution, and not proof of final health conservation after armor/events/heal cancellation. No added personal mana or Source charge in these handlers. Future tests compare0/0.5/1 fractions, recipient invulnerability, canceled heal and link expiry; no balance override performed.

## Homing: ally data and protected player states

MethodHomingProjectile.basicIgnores now explicitly rejects all players unless stats.isSensitive; with Sensitive it still rejects **creative/spectator players**. It also rejects dead entities/self, native FamiliarEntity, Flashjack and invisible entities. Native scoreboard isAlliedTo is only added when shooter has a team. For a Player shooter on ServerLevel, it additionally rejects entities selected by Sauce AlliesSavedData in that **shooter level**, not an unconditional global cross-dimension list.

Contextual spell filters remain appended through Sauce GlyphEffectUtil. The ordinary onCast route still adds player-owned ISummon/OwnableEntity exclusions, or same-owner summon/pet exclusions for ISummon shooter; new null checks guard repeated getOwnerUUID in lambdas. These extra exclusions belong to onCast, not automatically every call to static basicIgnores. Phalanx and any alternate call sites must be audited separately.

Allies protection is narrower than immunity: [Sauce report](ars-family-pinned-sauce-74113.md) traces separate IDamageEffect.canDamage guard, does not prevent every status/world/routine damage path or vanilla lightning collateral. Homing avoidance should be tested with ordinary/Sensitive, creative/spectator, configured player/type ally, other-owner pets and direct-versus-block routes. Existing Homing recipe/speed/lifetime recipients are not rewritten by this diff; Conflux reflection/conversion/billing overlays remain part of combined review.

## Other reviewed deltas and remaining scope

RainbowPrismLens now canConvert returnsfalse and shoot only delegates; no rainbow particle rewrite remains. It was already deprecated/no recipe in historical prism audit; do not teach a survival rainbow converter.

BlackstoneFormation baseRadius changes from3..5 to3..7 (`nextInt(5)+3`), and runaway x/z bound32→15. Existing10%gilded/lava/ground logic requires source placement conditions as before; this changes attempted shapes, not guaranteed mineable yield. ModWorldgen/Vexing-carver and registry/Phalanx style changes remain to consolidate. Forest rituals already reviewed against current source in [forest report](ars-elemental-pinned-forest-rituals.md).

Current profile remains stale pending complete impact reconciliation, custom Netherite book/Crush recipes, newly installed Conflux and final progression/pilot consolidation. This receipt revalidates named bodies only; do not mark entire addon source-complete.
