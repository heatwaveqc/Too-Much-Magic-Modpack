# Ars Elemental 0.7.10.4 — Shocking, Spores and Summoning threads

Exact artifact/version scope: [guide receipt](ars-elemental-pinned-guide-urn.md) and [bundled Sauce receipt](ars-elemental-pinned-foci.md). Direct ShockPerk/SporePerk/SummonPerk, SummonEvents, LightningLureEffect, ModPotions, ArsNouveauRegistry and three packaged recipes; native Ars5.13.5 PerkEvents/PerkUtil and bundled Sauce DamageEventHandler/Sauce constructor recipient ends. CFR0.152 plus `javap -c -p` confirmation of the Summoning Sickness integer arithmetic. No runtime tests or fixes.

## Craft and install

Use JEI exact recipe IDs `ars_elemental:thread_shock`, `thread_spore`, `thread_summon`; native Worn Notebook **Armor → Alteration Table / armor / thread** explains installing and recovering threads. [Native station walkthrough](ars-nouveau-pinned-armor-threads.md) supplies geometry and interaction order. Each recipe uses **Blank Thread reagent**, Enchanting Apparatus, zero Source and keepNbt false; consumes pedestal ingredients below and returns one same-ID thread.

| Thread | Consumed pedestal ingredients | Expected use |
| --- | --- | --- |
|Shocking / `thread_shock`|two Air Essences, Lightning Rod, **`ars_elemental:flashpine_pod`**|apply Shocked before eligible damage; highest slot instead applies Static Charged|
|Spores / `thread_spore`|two Earth Essences, vanilla Spore Blossom, Spider Eye|apply Poison, or Hunger to inverted-healing targets|
|Summoning / `thread_summon`|two Conjuration Essences, Echo Shard, two `ars_nouveau:wilden_drop` tag ingredients|add summon-power attribute at slot2/3; advertised sickness reduction is broken by integer division|

`ArsNouveauRegistry.registerPerks` registers all three perks. Install on compatible armor, including native armor; an Elemental full set is not required by these perk methods. Slot **value** controls strength, not the armor's stored upgrade index or number of duplicate threads. The native table assigns highest-valued available slots first; use the [Elemental capacity table](ars-elemental-pinned-armor.md) to choose a suitable piece. Remove competing threads before a controlled comparison. Slot3 is needed for Shocking's alternate effect; do not teach stacking three slot1 copies as equivalent.

## Shocking and Spores resolve before damage

Native `PerkEvents.preEffectResolve` collects perks from the shooter's armor and calls `IEffectResolvePerk.onEffectPreResolve`. These are **pre-resolve** status hooks, not kill rewards or proof the final damage event succeeded.

Shocking requires the resolving effect implement `IDamageEffect`, an entity hit on a LivingEntity other than the shooter, and `damageEffect.canDamage(...)` true. Slot value1/2 applies native Shocked for **100 ticks**, zero-based amplifier **1/2** respectively. Slot value≥3 instead applies **`ars_elemental:static_charged`**, 100 ticks, amplifier1. It does not simply apply a longer Shocked III.

`ModPotions.LIGHTNING_LURE` is the field for registered **static_charged**, backed by LightningLureEffect. That effect schedules its action **when remaining duration equals1**. It creates `amplifier+1` vanilla LightningBolts at the affected entity's block position: the thread's amplifier1 therefore requests **two bolts at expiry**. Bolt cause is the affected entity itself only if it is a ServerPlayer, otherwise null; the original thread caster is not propagated here. This method has no explicit server-side guard in its body, so actual side dispatch belongs to runtime acceptance. Do not promise harmless visuals, player ownership attribution or immediate lightning. The separate LightningMixin targets the native Ars LightningEntity and skips ItemEntity/ExperienceOrb thunderHit calls; that alone does not prove these newly created vanilla bolts spare loose items.

Spores requires the same damage-effect/entity/not-self shape. It accepts either normal `canDamage` or, **only for addon EffectSpores**, a simplified check that target health is positive and it is not allied to shooter. It applies **Poison**, except `isInvertedHealAndHarm()` targets get **Hunger**. Duration is **slotValue×100 ticks**, zero-based amplifier **slotValue−1**: slots1/2/3 mean100/200/300 ticks and amplifiers0/1/2. This does not assert Hunger deals damage to every undead, or that all other damage-effect permission failures are bypassed; the alternate predicate is limited to the named effect. Full Poison Spores glyph behavior remains for the glyph audit.

## Summoning: power works through two event handlers; sickness text disagrees

`SummonPerk.applyAttributeModifiers` adds Sauce **SUMMON_POWER** by **slotValue−1**, with ADD_VALUE and modifier ID `ars_elemental:summon_power` in the equipment-slot group. Slot1 contributes0, slot2 contributes1, slot3 contributes2. Reused modifier IDs mean an aggregate across armor pieces must be checked from effective attributes, not assumed to sum every copy.

Two inspected, registered listeners consume that attribute for damage whose source entity implements native `ISummon` and whose resolved owner is a Player:

- Addon's subscribed `SummonEvents.summonPowerup` adds owner SUMMON_POWER to `LivingDamageEvent.Pre.newDamage`.
- Bundled Sauce's `DamageEventHandler.empowerEntitiesBySchool` independently adds the same owner attribute for its `ISummon` branch. Sauce constructor explicitly registers DamageEventHandler on NeoForge's event bus.

Thus, **within these exact two binary versions**, an eligible event reaches two additive paths, nominally **+2×the effective attribute** if neither is altered by other listeners. This is a source-level duplicate, not a measured pack damage total: global Sauce version selection and event ordering with other installed mods remain open. Sauce also has an Evoker Fangs owner branch; addon listener's extra focus-school effects specifically test SummonWolf (Fire sets100 fire ticks, Water native Freezing100/amplifier1, Air Shocked100/1, Earth Poison100/0).

The description advertises **10% less Summoning Sickness per tier**. Actual `SummonEvents.summonSickReduction` rewrites duration as:

`oldDuration × (1 − PerkUtil.countForPerk(SummonPerk, entity) / 10)`

Native `countForPerk` returns the **maximum installed slot value**, not a sum of threads. Both it and division are integer arithmetic; direct javap shows `bipush10 → idiv → isub → imul`. For normal slot values1–3, `value/10` is **0**, so duration is **unchanged**. Multiple copies do not raise that maximum to10. Do not teach the advertised10/20/30% reduction as a working reward, and do not silently patch the calculation during research.

## Acceptance and coverage

Future explicit acceptance: three recipes and station recovery; slot placement; Shocked versus Static Charged timing/bolt attribution/loose-item behavior; protected/allied/dead/self targets; Spores inverted-healing and alternate predicate; effective summon-power attribute with repeated copies; both damage listeners; exact observed sickness duration. Source findings provide a usable optional combat customization route with the broken advertised benefit stated. The three pilot Elemental nodes do not cover these threads. Remaining full addon glyph/helper/device/worldgen/summon routes keep the parent researching.
