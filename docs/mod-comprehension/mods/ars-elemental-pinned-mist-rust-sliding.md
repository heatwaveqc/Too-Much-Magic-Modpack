# Ars Elemental 0.7.10.4 — Mist, Oxidize, Sliding and Nullify Defense

Exact artifact/hash [receipt](ars-elemental-pinned-guide-urn.md). Direct four glyphs, EntityMistCloud/MistEffect/RustEffect/SlipFeetEffect, ModPotions, ClientEvents fog hooks, LivingEntityMixin and ConfigCondition; native Ars5.13.5 IPotionEffect/SpellStats/AbstractEffect recipients. No client/combat/block-edit tests. These four recipe routes bring audited glyph coverage to **22of39**, including Nullify's disabled-by-default acquisition boundary.

## Acquisition and useful placement

Search Worn Notebook **Mist Cloud / Oxidize / Sliding / Nullify Defense**, then use [native Scribes Table](ars-nouveau-pinned-guide-glyphs.md). Recipe ingredients below are consumed. Spell mana is per-glyph default before method/augments/modifiers, not a measured effective cost.

| `ars_elemental:glyph_…` | Tier / XP / default mana | Recipe inputs |
| --- | --- | --- |
|mist|2 /55 /40|Water Essence, Air Essence, Phantom Membrane, Blue Ice|
|oxidize|3 /160 /80|Water Essence, Air Essence, Oxidized Copper, Abjuration Essence|
|slip_feet|2 /55 /20|Water Essence, Abjuration Essence, Slime Ball, Ice|
|nullify_defense|3 /160 /1000|Nether Star, **two Marks of Mastery**, Netherite Block; recipe condition below|

Choose a suitable cast method and then the desired effect. Mist uses the hit location to create a cloud; Sliding and entity Oxidize need a living hit; block Oxidize needs a block with a next oxidation state. Nullify sets a damage-delay field and needs no item-use target equipment UI. Item possession/learned glyph alone does not establish any effect application.

## Mist Cloud: geometry, cadence and client recipient

Water-school EffectMist.onResolve returns on client; server creates EntityMistCloud at the hit's **block position**, centered horizontally. Effect passes duration **`(int)(100+20×durationMultiplier)` ticks** and radius argument `3+aoeMultiplier`. The entity constructor stores **1+that argument**, so actual radius is **`4+aoeMultiplier`**, not3. Compatible AoE/Extend Time/Duration Down; no actor/owner is stored by this constructor.

Cloud server tick calls applyMist, increments age and discards when age≥maxDuration. applyMist **returns when age%20==0**, so it runs on19of20 ticks, not once each20 ticks. Query LivingEntity inside inflated box and squared-distance≤radius²; applies to caster/allies as well as enemies, with **no filter/owner exemption** here. Adds Mist35 ticks/amplifier0/hidden particles. For Mob, clear target only if it **does not already have Mist**; it does not clear a reacquired target every refresh while the effect persists.

ModPotions registers Mist with **Follow Range −0.75 ADD_MULTIPLIED_TOTAL**; marker class alone does not define blindness. ClientEvents camera-effect hooks set spherical fog **near0.2/far8**, cancel RenderFog and tint RGB0.85/0.9/0.95 when camera LivingEntity has Mist. This is the source behind the guide's obscured-vision claim, not a client visual PASS. Particles have separate spherical sampling; cloud age/duration/radius serialize independently. No Source upkeep is in tick.

## Oxidize: armor Rust versus block aging

Living target receives configurable **Rust**, without an explicit IDamageEffect.canDamage predicate in this glyph method. Native IPotionEffect computes duration **`baseSeconds×20 + extendSeconds×(int)(20×durationMultiplier)`**, amplifier `(int)ampMultiplier`. Oxidize builds default potion config **30seconds base /8seconds extension unit**. Rust registration attaches Armor attribute modifier **−0.15 ADD_MULTIPLIED_TOTAL**, which scales with ordinary effect-amplifier semantics rather than instantly destroying worn armor. No equipment durability mutation is present in these inspected methods.

Block branch computes native AoE/Pierce positions. For each, reject outside build height; code also reads a Randomize skip chance **`RandomizeCount×0.25`** even though Randomize is **not** in its allowed augment set. Then require native `destroyRespectsClaim`, block implements ChangeOverTimeBlock and getNext present; separate **10%** random test advances one state through setBlockAndUpdate. It is not guaranteed oxidation per cast, reverse scraping or wax removal. Compatible Amplify/Extend Time/Duration Down/Pierce/AoE; schools Water+Air. Entity Rust and block chance use distinct paths; Amplify's entity strength does not make the block's10% test automatically succeed.

## Sliding: friction, not a placed ice floor

EffectSlipper applies `ars_elemental:slip_feet` to LivingEntity, schools Water+Abjuration. Compatible Extend Time/Duration Down, no Amplify. No custom buildConfig fills duration fields here; getters fall back **10seconds base /10seconds extension unit**, using native IPotionEffect formula above and current configured fields if supplied. Marker SlipFeetEffect itself is NEUTRAL.

LivingEntityMixin wraps block friction read during travel: while effect present return **`clamp(baseFriction+0.38,baseFriction,1.075)`**. It changes movement calculation, not the underlying blocks, boots or a passive full-set ability. The same multiplier cap means different surfaces can converge at1.075; do not translate this into a fixed tested walking speed. Glyph methods don't add an ally/owner exemption. Mob-effect applicability/other protections remain acceptance boundaries.

## Nullify Defense: conditionally obtainable damage-delay reset

Packaged glyph recipe has `neoforge:conditions` type **`sauce:ae_config`**, config **`frame_skip_recipe`**. Addon ModRegistry registers this codec through Sauce's condition registry; ConfigCondition queries the common config boolean. ConfigHandler default is **false**. Therefore this recipe resource's presence in the39-count does **not** establish normal default survival acquisition. Do not enable it or promise it as a required walkthrough step. Glyph itself is registered independently; no recipe does not mean class absent.

EffectNullify on a LivingEntity simply assigns **`invulnerableTime=0`**. Necromancy school, no compatible augments, default mana1000. It does not remove armor, clear all invulnerability sources, force damage, bypass boss phase mechanics or rewrite claims/PvP policy. If explicitly enabled in a future approved configuration, place it in an appropriate legal effect sequence to reset the ordinary post-hit delay; this report is not approval to enable it.

## Acceptance and remaining coverage

Future explicit acceptance: config/recipe visibility, Mist actual radius and19/20 refresh versus target reacquisition/fog UI; caster/allies; Rust amplifier/equipment attributes; protection and oxidation chance/state boundaries; sliding friction across surfaces and expiry; Nullify disabled/default and specific hit-delay behavior if later approved. No runtime changes, policy bypass or client acceptance claimed. **17 other glyph recipes**,12tomes and major helper/device/gear/worldgen branches remain researching before addon completion.
