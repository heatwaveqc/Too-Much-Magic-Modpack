# Ars Elemental 0.7.10.4 — Envenom, Poison Spores and Rage

Exact artifact/hash [receipt](ars-elemental-pinned-guide-urn.md). Direct three glyphs, VenomEffect and recipes; bundled Sauce RageEffect/registry recipient (dependency scope in [focus receipt](ars-elemental-pinned-foci.md)); native Ars5.13.5 IDamageEffect/IPotionEffect/SpellStats endpoints. No combat/AI tests. **25of39 glyph recipe routes audited**,14other glyphs and other addon systems still open.

## Acquisition and setup

Search Worn Notebook **Envenom / Poison Spores / Rage**, learn via [Scribes Table](ars-nouveau-pinned-guide-glyphs.md), choose a legal cast method and the effects below. Recipe inputs consumed; defaults are before method/augments/modifiers:

| Recipe/output `ars_elemental:glyph_…` | Tier / XP / default mana | Recipe inputs |
| --- | --- | --- |
|envenom|2 /55 /20|Poisonous Potato, Fermented Spider Eye, Suspicious Stew|
|poison_spores|2 /55 /30|Spore Blossom, Red Mushroom, Earth Essence|
|rage|3 /160 /100|Fire Essence, **`sauce:anima_essence`**, Wilden Horn, Red Carpet, Fermented Spider Eye|

Useful sequence: Envenom to prime a living enemy with Poison; a second application promotes it to Venom; Poison Spores consumes neither status as a recipe but requires a qualifying status before its initial damage/spread path. Rage is a separate Mob target-control/status branch, not required for the poison combo. [Spore Thread](ars-elemental-pinned-threads.md) can also prime statuses before damage resolution with its specific guards.

## Envenom: Poison first, Venom next

On LivingEntity hit, native IDamageEffect.canDamage must pass: rejects dead living targets and entities allied to shooter. If target already has **vanilla Poison**, add **half its zero-based amplifier** to current spellStats amp, apply addon Venom, then remove Poison. This uses float division, not integer truncation until potion amplifier is constructed. It does not transfer the old Poison's remaining duration.

If no Poison exists, apply Poison normally. With Earth focus check, targets tagged **IGNORES_POISON_AND_REGEN** or Spider instead get Venom immediately. It does not globally remove all status immunity. Code uses target predicate plus native addEffect applicability, so actual immune targets remain acceptance work.

Envenom builds config defaults **5seconds base /5seconds per duration unit**, not its fallback getters30/8 when fields null. Native IPotionEffect duration is `20×baseSeconds + extendSeconds×(int)(20×durationMultiplier)`; potion amplifier `(int)ampMultiplier`. Compatible Extend Time/Duration Down/Amplify; Earth school. The extra AMP_VALUE config5 is declared, but this glyph path does not multiply potion amplifier by that field; don't infer +5 poison levels per Amplify from the unused value.

VenomEffect periodically requests **`2+0.25×amplifier`** addon POISON damage, with **no original caster attached** to the constructed source. Tick interval: amplifier0→20,1→10, otherwise `32 >> amplifier`; interval0 means every effect tick. For ordinary nonnegative amplifiers2/3/4/5 this gives8/4/2/1 ticks. Final damage/credit/immunity depend on damage recipients; don't claim a fixed measured DPS or original-player kill ownership.

## Poison Spores: status gate and box spread

Entity resolve requires LivingEntity and ServerLevel. Base damage **6+2.5×ampMultiplier**, range **3+AoE**, status seconds `(int)(10+3×durationMultiplier)` by built config defaults. If **initial target has Venom**, add **2+3×Venom amplifier** to damage variable. That value is then reused for secondary targets; it is not independently recomputed from each secondary Venom level.

Override canDamage requires target alive and **Poison OR Hunger OR Venom**. It has **no ally exclusion**. If initial target fails, return before any spread. On passing, damage initial target, then query LivingEntity in an axis-aligned box centered on it with those range extents—**no spherical-distance filter**. Skip initial target and shooter. Secondary already qualifying targets receive damage; others get **both Poison and Hunger** for computed duration at `(int)ampMultiplier`. This can prime nearby allies because the branch has no explicit alliance filter.

Damage helper multiplies base by **1.25 for undead-tagged targets**, invokes native attemptDamage and emits spores. Native attemptDamage reuses this overridden canDamage and posts normal spell-damage event; it does not restore the base interface's ally exclusion automatically. Nonplayer shooter is replaced with native fake player for the source builder. Actual permission/cancellation and external listeners still matter.

After attempted damage, a dead/dying target has a **5%** roll with Earth focus for ground blossom: Moss Block/dirt/leaves underneath and air above → **`ars_elemental:spore_blossom_up`**, trigger blossom advancement for real ServerPlayer. Direct setBlockAndUpdate has no additional claim check in this branch. This is distinct from [Earth Grow's20%](ars-elemental-pinned-empowering.md). If the combined death/roll/Earth condition fails, add Hunger for computed seconds at amplifier0; if it passes but terrain fails, that else is not entered. Don't promise vanilla hanging blossom, guaranteed drops or permission acceptance.

Compatible Amplify/Dampen/Extend Time/Duration Down/AoE/Fortune/Randomize, default own Amplify limit2, Earth school. Some accepted augments act through native stats/damage, not an extra status-strength formula here. No item/Source upkeep appears beyond ordinary spell cast payment.

## Rage: actual Mob targeting recipient

EffectRage applies **`sauce:rage`** only to **Mob**, not every LivingEntity/Player. Fire+Necromancy schools, compatible potion augments Amplify/Extend Time/Duration Down. Getters return fixed **60seconds base /120seconds per duration unit**, using native IPotionEffect formula. No local canDamage/ally check is in this application method; actual effect applicability still applies.

Bundled Sauce registers Rage with Attack Damage **+0.25 ADD_MULTIPLIED_TOTAL**. RageEffect server tick requests a target **only when Mob currently has none**; it does not forcibly replace an existing target every second. Search box is inflated **10horizontal/4vertical**, nearest entity selected via **TargetingConditions.forCombat().range(10)**. Selector predicate is true, but ordinary combat targeting rules still participate. The guide's claim of attacking even allies is therefore not proof of bypassing every vanilla/custom alliance rule.

Tick eligibility is duration%20==0 **or parent effect eligibility**. No added attack AI goal, persistent goal-selector replacement or owner-team rewrite occurs in this effect. A Mob that cannot use its selected target is not automatically granted a new attack. Actual retargeting, range/line-of-sight and damage boosts require runtime observation.

## Acceptance and remaining coverage

Future explicit acceptance: recipes/tiers/config; Envenom first/second cast, Earth poison immunity, amplifier/duration and credit; initial Spores status gate, secondary box corners/allies, reused initial Venom bonus, canceled damage and blossom terrain; Rage players excluded, current-target preservation and actual combat targeting eligibility. **14 glyphs** remain: Bubble Shield, Cauterize, Cavitate, Charm, Conflagrate, Conjure Terrain, Discharge, Geyser, Life Link, Phantom Grasp, Spark, Spike, Watery Grave, Water Jet; plus12tomes and major helper/device/gear/worldgen branches. Addon stays researching; no quests/config/balance/runtime changes.
