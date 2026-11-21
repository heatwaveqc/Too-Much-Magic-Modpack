# Ars Elemental 0.7.10.4 — Bubble Shield and glyph coverage closure

Exact artifact/hash [receipt](ars-elemental-pinned-guide-urn.md). Direct EffectBubbleShield, BubbleShieldEffect, DamageEvents.postReductionDamageTweaking/statusProtect, ModPotions, packaged recipe/blacklist and ArsNouveauRegistry production registration. Native mana capability/discount endpoints previously audited. No combat, status or multiplayer tests. This completes source walkthrough coverage of **39 packaged glyph recipe routes**, not the entire addon.

## Acquire, cast and identify the shield

Worn Notebook **Bubble Shield**; learn via [Scribes Table](ars-nouveau-pinned-guide-glyphs.md). Exact `ars_elemental:glyph_bubble_shield`: Heart of the Sea, Prismarine Shard, Bastion Pod, Water Essence; **recipe XP55, Tier2**, default glyph mana **400**. Choose Self or appropriate living-target cast and Bubble Shield. Water school; compatible Amplify/Extend Time/Duration Down.

Effect requires target **LivingEntity with nonnull native mana capability**, then applies status **`ars_elemental:mana_shield`**. Ordinary native provider is Player; arbitrary mobs do not qualify simply because living. Default duration30seconds +8seconds per duration unit via native IPotionEffect; amplifier `(int)ampMultiplier`. Declared AMP_VALUE2 config is not used to multiply amplifier in this potion path. BubbleShieldEffect is a BENEFICIAL marker; resource effects occur in event handlers below.

This is **not native BubbleEntity capture**, so it does not by itself satisfy [Cavitate](ars-elemental-pinned-cavitation-grave.md). Mana spent to cast and mana spent defending are separate. No Source upkeep or per-idle-tick reserve drain is present in these inspected methods.

## Damage reduction and conditional debit

At LivingDamageEvent.Pre, after this same method's Magic Fire armor adjustment, act if target has shield and source does **not** bypass enchantments. Define:

- **C = max(0, configured absorption_cost −0.75×native discounts)**. absorption_cost defaults **350**; discount query is target's ManaUtil.getPlayerDiscounts for a spell containing **Bubble Shield only**, EMPTY caster stack.
- Require mana capability and event newDamage **<100,000**. Available reduction `min(1+statusAmplifier, currentMana/C)`; desired new damage `max(0,oldNewDamage−reduction)`.
- Apply only if actual reduction>0 **and currentMana≥C**. Thus a balance below one full C does not provide a fractional reduction despite that ratio calculation.
- Mana debit is **actualReduction×C**, **only if** container post-attack-invulnerability-ticks differs from target's current invulnerableTime. This is conditional on event state, not an unconditional fixed350-per-hit debit.
- After this branch, remove shield if currentMana<C. No automatic removal on quiet idle ticks is shown here.

The cast cost400 and absorption default350 are not interchangeable. Default amplifier0 reduces at most **one damage point** per qualifying event before other listeners; not350 damage, a percent shield or an extra heart bar. Amplifier improves the cap but can consume more mana proportional to actual mitigation.

C can reach0; code has **no division-by-zero guard**, and uses floating division. With zero balance/zero C, NaN can prevent the actualReduction>0 test; do not promise zero-cost perfect defense at that boundary. If capability absent or damage≥100,000, only a special **WaterMage** branch multiplies damage0.75; arbitrary target does not get that fallback. Bypass-enchantment damage skips this shield handling. Other listeners/protection and invulnerability timing remain acceptance boundaries.

## Debuff prevention uses a different price and gate

MobEffectEvent.Applicable checks shield. For **HARMFUL** incoming effect:

1. Return without blocking if effect Holder is in **`ars_elemental:manabubble_blacklist`**. Packaged entries: **Levitation, Hymn of Order, native Summoning Sickness**. This path cannot be taught as immunity to those progression effects.
2. Roll `nextInt(10)==0` (**10%**), require mana capability and balance at least **2×absorption_cost** (default700).
3. Debit **half that threshold** (default350), set DO_NOT_APPLY. This uses **raw configured cost**, not discounted C from damage defense, and is not guaranteed on every debuff.

For non-HARMFUL effect whose Holder matches addon **Magic Fire** delegate, set DO_NOT_APPLY without this random/mana check. MagicFireEffect is NEUTRAL, explaining why it reaches this branch. Actual holder identity/other applicability listeners remain runtime acceptance. The method prevents a new application; it does not scan and remove every existing harmful status.

## Exact glyph recipe coverage map

All names below are suffixes after `ars_elemental:glyph_`; each linked report covers acquisition and operational recipients/known limitations. Counts are recipe routes, including conditionally disabled acquisition, not live registry/gameplay PASS.

| Routes | Count | Source walkthrough |
| --- | ---: | --- |
|arc_projectile, propagator_arc|2|[Arc](ars-elemental-pinned-arc.md)|
|homing_projectile, propagator_homing|2|[Homing](ars-elemental-pinned-homing.md)|
|aerial/aquatic/fiery/insect/summon/undead_filter and six not_ variants|12|[Filters](ars-elemental-pinned-filters.md)|
|summon_bee, summon_slime|2|[Combat summons](ars-elemental-pinned-bee-slime.md)|
|mist, oxidize, slip_feet, nullify_defense|4|[Statuses/control](ars-elemental-pinned-mist-rust-sliding.md); Nullify recipe disabled by default|
|envenom, poison_spores, rage|3|[Poison/Rage](ars-elemental-pinned-venom-spores-rage.md)|
|cauterize, conflagrate, spark, discharge|4|[Fire/static](ars-elemental-pinned-fire-static.md)|
|cavitate, watery_grave|2|[Cavitation/air](ars-elemental-pinned-cavitation-grave.md)|
|water_jet, geyser|2|[Delayed/directional](ars-elemental-pinned-jet-geyser.md)|
|conjure_terrain, spike|2|[Terrain/spikes](ars-elemental-pinned-terrain-spikes.md)|
|charm, life_link, phantom_grasp|3|[Anima](ars-elemental-pinned-anima.md)|
|bubble_shield|1|This report|

**MethodCarianPhalanx is development-only registration**: ArsNouveauRegistry wraps register call in **`!FMLLoader.isProduction()`**. No packaged glyph recipe establishes a normal production survival route for it. Its class/entities are not an unresearched fortieth mandatory glyph. Twelve caster-tome recipes and their item implementation are separate, still-open routes; they are not additional glyph recipes.

## Remaining acceptance and addon completion

Future explicit acceptance: actual config/recipe, caster versus target mana, duration/amplifier, discount query, below-C/zero-C/invulnerability/bypass/high-damage conditions, exact mana depletion/removal, harmful-status10% threshold/debit/blacklist, new Magic Fire versus existing statuses. Full addon still researching for12tomes, helpers/Siren, devices/lenses/relays, bangles/bags/other gear, worldgen/Vexing Caves, global Sauce dependency selection and final native progression/pilot review. No runtime, quest, pin or config changes.
