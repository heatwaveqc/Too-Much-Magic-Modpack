# Ars Elemental 0.7.10.4 — Cauterize, Conflagrate, Spark and Discharge

Exact artifact/hash [receipt](ars-elemental-pinned-guide-urn.md). Direct four glyph classes/recipes, native Ars5.13.5 IDamageEffect/IPotionEffect/BlastEffect recipients; addon Static Charged recipient in [thread audit](ars-elemental-pinned-threads.md). Cauterize tag comparator lambdas confirmed independently with javap. No combat, FE or status-removal tests. **29of39 glyph recipe routes** audited;10other glyphs and remaining major branches still open.

## Acquire and learn the combinations

Search Worn Notebook glyph names, learn via [Scribes Table](ars-nouveau-pinned-guide-glyphs.md). Consumed recipe inputs; mana defaults exclude method/augments/modifiers:

| `ars_elemental:glyph_…` | Tier / XP / default mana | Recipe inputs |
| --- | --- | --- |
|cauterize|3 /160 /80|Fire Essence, Abjuration Essence, Blaze Powder, Ghast Tear|
|conflagrate|3 /160 /80|Gunpowder, Fire Essence, Bombegranate Pod, Netherite Scrap|
|spark|1 /27 /15|Air Essence, `minecraft:wool` tag ingredient, Iron Bars|
|discharge|2 /55 /40|Lightning Rod, `ars_elemental:flashpine_pod`, Air Essence|

Use Cauterize for a damage-gated harmful-status cleanse; Ignite→Conflagrate for an already-burning target; Spark→Discharge for static priming and nearby damage. These are optional learned spells, not a requirement to produce FE or install a new energy-transfer mod. No generic FE transfer addition is authorized.

Native attemptDamage rejects through canDamage, adds native spell damage stats, applies Randomize/Luck, posts cancellable SpellDamageEvent.Pre, calls entity.hurt and returns true only after successful hurt/Post event. A caller checking that boolean differs materially from a caller applying a status regardless of return.

## Cauterize: successful fire damage before cleansing

Fire-school Cauterize overrides canDamage to reject only dead living targets, **not allies**. Attempts default **3** native FLARE damage; no compatible augments. Only on successful attemptDamage does it snapshot effects and remove harmful effects whose cures include **MILK**. Failed/canceled/zero damage means no cleanse. It does not merely consume mana for harmless healing.

It also queries native DISPEL_DENY and DISPEL_ALLOW tags, but **both matching lambdas compare `holder.value()` to `MobEffectInstance.getEffect()` (Holder)** by object identity. Bytecode confirms Holder.value→getEffect→if_acmpne. These are different kinds of object, so ordinary entries do not match as intended: do not promise blacklist protection or extra whitelist removals in this exact implementation. Harmful milk-curable branch still removes effects; otherwise whitelist branch does not establish an override. This finding is not approval to patch the mod or bypass progression effects.

## Conflagrate: detonation visuals, fire spread and delayed Blast

Server LivingEntity hit must be **actually isOnFire** and take successful initial damage **`9+5×ampMultiplier`** via FLARE source. Magic Fire marker alone is not this initial gate (other [empowering handlers](ars-elemental-pinned-empowering.md) can affect native fire damage). Failure stops this glyph's spread.

Then query **all Entity** in an axis-aligned box centered on target with extents **3+AoE**, excluding shooter in world query. **Before** checking living type, original target or damage permission, add **60 fire ticks** to every returned entity. Thus a secondary ally/nonliving entity can receive the fire mutation even when later damage is rejected; there is no spherical-distance check or separate fire-spread permission gate in this loop.

For other LivingEntity, attempt **75%** of initial damage variable, through ordinary IDamageEffect eligibility/events. On successful secondary damage, random double>0.9 applies native **Blast**,200ticks/amplifier0. This is nominal10% per successful secondary, not the original target. Native Blast explodes at remaining-duration1, strength **2+amplifier**, y+1, no fire, ExplosionInteraction.NONE; explosion damage remains possible even though it does not request block destruction. Immediate glyph particles are not themselves a block explosion.

Fire school; compatible Amplify/Dampen/AoE/Fortune/Randomize, own Amplify limit2. Original and secondary damage plus ungated fire mutation are distinct boundaries; do not claim every negative/protected hit cancels all side effects.

## Spark: wet bonus and status after attempted damage

Air-school Spark requires living target passing native canDamage (alive/nonallied). Computes **`3+1.5×ampMultiplier +2 if isInWaterOrRain()`** and attempts addon SPARK damage. It then applies status **without checking attemptDamage return**: Air focus check→addon **Static Charged**, otherwise native **Shocked**. An accepted predicate with later canceled damage can still reach status application.

Configured duration **15seconds +5seconds×duration multiplier** through native IPotionEffect formula, amplifier `(int)ampMultiplier`. Static Charged's recipient creates **amplifier+1 vanilla lightning bolts at effect duration1**, not immediate lightning; cause/source boundary is in [threads](ars-elemental-pinned-threads.md). Compatible Extend Time/Duration Down/Amplify/Dampen/Randomize, own Amplify limit2. Nonplayer source builder substitutes native fake player; no general original-owner credit guarantee.

## Discharge: status consumption and armor FE before damage

Server LivingEntity hit must have native Shocked **or** Static Charged. No initial canDamage test precedes resource/status changes. Compute **`7+3×ampMultiplier`**; with Static Charged multiply **1.3**, then **remove Static Charged** immediately. Ordinary Shocked is not removed here.

Loop initial target's **armor slots**; for each item exposing `Capabilities.EnergyStorage.ITEM`, request extraction of **`(int)(storedEnergy×0.25)`** with simulate=false, and multiply damage by **1.1**. Extracted amount is unchecked: an empty/rejecting/read-only capability still reaches the damage multiplier. This is per-capability multiplicative (`1.1^count`), not one flat10% for any armor, and no player mana/Source is credited with that FE. It does not drain held tools or every Curios item through this loop.

Only afterward query LivingEntity in **block-position AABB inflated3+AoE**, excluding shooter, **including initial target** if inside. Each gets normal attemptDamage; success applies native Shocked and sends discharge visual packet. Thus resource drain/Static removal can occur even if every subsequent damage attempt fails or is canceled. This is an observed source order, not an approved resource-grief mechanic or protection PASS.

Shocked duration15seconds +5seconds per duration unit; overridden applyPotion caps zero-based amplifier **at5**, casts to int (no explicit lower clamp). Air school; compatible Amplify/Dampen/Extend Time/Duration Down/AoE/Randomize/Fortune, own Amplify limit2. The status/FE effects are not recovered if later damage fails.

## Acceptance and remaining coverage

Future explicit acceptance: actual glyph config/recipes; Cauterize milk-cure and tag mismatch, ally/failed damage; Conflagrate initial burn and fire ticks on protected/allied/nonliving entities, delayed Blast; Spark wet versus merely Soaked and status after canceled damage; Static expiry; Discharge initial versus secondary targets, capability presence versus actual FE extraction, status loss/payment/permissions. No runtime/config/quest/energy-policy changes. Remaining10glyphs: Bubble Shield, Cavitate, Charm, Conjure Terrain, Geyser, Life Link, Phantom Grasp, Spike, Watery Grave, Water Jet; plus12tomes/helper/device/gear/worldgen routes and final review.
