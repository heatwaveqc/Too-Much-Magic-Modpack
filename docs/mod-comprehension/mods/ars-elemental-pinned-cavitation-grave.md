# Ars Elemental 0.7.10.4 — Cavitate and Watery Grave

Exact artifact/hash [receipt](ars-elemental-pinned-guide-urn.md). Direct EffectCavitate/EffectWaterGrave/WaterGraveEffect and recipes, native Ars5.13.5 IDamageEffect/IPotionEffect and **BubbleEntity** freshly decompiled externally in `elemental-parent-fresh`. No drowning, conversion or bubble gameplay tests. **31of39 glyph recipe routes audited**; other Water effects still need their entity recipients.

## Learn and prepare a target

Search Worn Notebook **Cavitate / Watery Grave**, learn via [native Scribes Table](ars-nouveau-pinned-guide-glyphs.md). Consumed recipe inputs; mana defaults exclude other spell parts and discounts:

| Recipe/output `ars_elemental:glyph_…` | Tier / XP / default mana | Inputs |
| --- | --- | --- |
|cavitate|3 /160 /80|Water Essence, Heart of the Sea, Pufferfish, Sponge|
|watery_grave|2 /55 /25|Kelp, Prismarine Shard, Water Essence|

Cavitate requires a **native Bubble entity or an entity riding one**, not merely wet/Soaked status or addon Bubble Shield. Learn native **`ars_nouveau:glyph_bubble`**: recipe XP27, three `c:feathers`, Water Bucket, `minecraft:boats` ingredient and Water Essence; consult its Notebook page for capture/movement. Cast Bubble then hit the bubble/captured target with Cavitate. Watery Grave instead acts directly on LivingEntity air/velocity and need not have a Bubble vehicle.

## Cavitate: damage success gates the box shockwave

Server-only effect computes initial base **`8+3×ampMultiplier`**, range **`2+AoE`**. Only if target is BubbleEntity **or** passenger whose vehicle is BubbleEntity, and native attemptDamage returns true, query LivingEntity in **target block-position AABB inflated by range**, excluding shooter and exact initial target. Every candidate gets another damage attempt, followed by **Soaked application without checking that secondary attempt's result**. Thus secondary ally/protected damage can fail while its status path still runs. Shape is a box, not spherical radius.

Configured Soaked duration uses **30seconds base/8seconds per duration unit** from addDefaultPotionConfig, not fallback getters15/5. Native formula `20×base + extend×(int)(20×durationMultiplier)`, amplifier `(int)ampMultiplier`. Compatible AoE/Amplify/Extend Time/Duration Down/Randomize/Dampen; Water school. Addon CAVITATION damage source has packaged exhaustion0.1 and ordinary living-nonplayer scaling; this is not an automatic true-damage claim.

### The native Bubble endpoint does not guarantee “implosion”

BubbleEntity.hurt **returns true**, but calls pop **only if passengers empty or first passenger is ItemEntity**. An occupied living-target Bubble is not popped by this method alone. Cavitate itself does not explicitly pop/discard the Bubble. If targeting captured passenger, its living damage route can succeed and trigger the area; if targeting occupied Bubble directly, Bubble's true return can also satisfy initial attemptDamage despite no living health reduction there. Do not teach guaranteed occupied-bubble destruction from the glyph's implosion description.

Native pop sets HAS_POPPED and plays sound, then server removes after poppingTicks>5. Capture and ordinary attack handlers have separate rules. Actual projectile hit selection, claim cancellation, passenger release and target allegiance remain runtime acceptance, not established by the guide's visuals.

## Watery Grave: first motion/status, then air or drowning

LivingEntity hit first checks **actual Extend Time augment presence**. With it, apply addon **watery_grave** status; otherwise directly set vertical velocity to **`min(−1,oldY−0.05)`**, leaving horizontal components. These mutations occur **before** native damage eligibility; no canDamage/ally check precedes them. Extend Time's presence, rather than just a nonzero duration stat from gear, selects the status branch.

Next, a Zombie that is **not Drowned** calls startUnderWaterConversion(**20**) and **returns**. Therefore initial velocity/status may be applied, but no ordinary air drain/damage or final hurtMarked line in this call follows for that branch. This is a forced conversion-start call, not proof every zombie subclass actually completes conversion under all external rules.

For other living targets: if **air≤0 OR entity type in `minecraft:aquatic`**, attempt **`5+2×ampMultiplier`** DROWN-source damage. Otherwise set air to **`(int)max(−19,currentAir−50×(3+ampMultiplier))`**. Damage gate tests air **before** that drain, so one cast taking positive air below0 does not also take the drowning-damage branch in the same call. Final line marks movement for update. Aquatic-tag bypasses positive-air waiting in this caller, but actual drowning immunity and other damage recipients still matter; it is not a universal one-cast kill.

Build config uses default potion **30seconds base/8seconds per duration unit**. Water school; compatible Extend Time/Duration Down/Amplify/Dampen/Randomize, own Amplify limit2. Damage uses native IDamageEffect guards/events; direct motion, conversion and air mutations have no such initial success requirement.

## Watery Grave status recipient

WaterGraveEffect is HARMFUL and requests ticking **every effect tick**. If entity **not onGround** and **in water/rain/bubble**, set vertical velocity **`min(−0.5,oldY−0.03)`** and air **`max(−19,air−5×(1+amplifier))`**. Otherwise simply subtract **5 air**, without that clamp or velocity change. Thus air drains even on ground or dry land; “prevents swimming up” is not its only behavior. There is no periodic direct damage call here; actual drowning/effect applicability follows other engine behavior. Body has no explicit side guard, so client/server dispatch remains an acceptance boundary.

Both spells can interact with [Water focus freezing/other native changes](ars-elemental-pinned-empowering.md), but that does not make arbitrary cross-status combinations automatically valid. Neither timer charges recurring Source in these methods.

## Acceptance and remaining coverage

Future explicit acceptance: glyph recipes/tiers/config; empty/item/living Bubble hurt and actual passenger hit/pop/release; area box corners/initial exclusion and status after rejected damage; Extend Time versus duration stats; dry/wet/onGround air/status branches; air drain then second damage cast; Zombie conversion; aquatic tags/immunity; allies/claims/PvP. Remaining8glyphs: Bubble Shield, Charm, Conjure Terrain, Geyser, Life Link, Phantom Grasp, Spike, Water Jet; plus12tomes/helper/device/gear/worldgen and final walkthrough review. Addon remains researching; no runtime/quest/config changes.
