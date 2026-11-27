# Ars Elemental — protected Spell Book and three Crush recipes

Exact current Elemental0.7.11.3 SHA512 and Ars5.13.5 SHA1: [pin receipt](ars-elemental-pinned-utility-rituals.md). Direct recipe resources/CFR0.152: NetheriteUpgradeRecipe+serializer, P4E component, ItemEntityMixin, Events.despawnProtection, SpellBookTextureMixin, AEDocumentation/P4EEntry/JEI; native EnchantingApparatusTile/Recipe and EffectCrush/CrushRecipe including inners. Current [Conflux](ars-conflux.md) adds execution overlays; this report identifies native transformation recipients, not final whole-pack casting/payment acceptance.

## Optional Spell Book protection, not a fourth tier

Native Worn Notebook **Getting Started → Spell Books** is extended by AEDocumentation.postInit with `ars_elemental.page.book_protection` and dynamic P4EEntry recipe. JEI registers dedicated netherite-upgrade category and Enchanting Apparatus catalyst. Ordinary apparatus category excludes this dynamic-output recipe; an empty static output is not evidence it cannot be crafted.

Recipe **`ars_elemental:invincible_book`**, type `ars_elemental:netherite_upgrade`: reagent1item in **`#ars_nouveau:spellbook`**, five separate pedestal ingredients **Netherite Ingot +Earth/Water/Fire/Air Essence each1**, **2000Source**. This uses Enchanting Apparatus+correctly aligned Arcane Core and pedestals within per-axis3, per [native crafting setup](ars-nouveau-pinned-crafting.md). Put the book on/in reagent interaction, not as a sixth pedestal ingredient. Effective tag membership determines valid books; do not generalize to any written book, spell parchment or school tome.

Recipe type is appended to native enchanting recipe-type list. Match invokes ordinary ingredient/count matching and rejects catalyst with existing `ars_elemental:p4e` ElementProtectionFlag(true); absent/false flag qualifies. Custom assemble copies **actual catalyst stack** and sets P4Etrue. It preserves same item and stored components, not a new tier/item ID or a second book. Serializer reads reagent/pedestalItems/sourceCost; constructor fixes keepNBTtrue, so JSON keepNbtOfReagent is not a user-selectable alternate mode here. P4E component is persistent and network-synchronized; Blessed tooltip and default enabled client black-book texture identify it. Turning off `Enable BlackBookTexture` changes appearance, not protection flag.

Apparatus source preflight checks nearby Source range10; positive start attempts2000Source debit **before**210-counter completion and ignores actual take result after preflight. Invalidating/moving pedestal inputs can cancel without generic Source refund. At valid completion output replaces reagent; pedestal inputs become crafting remainders except effective preserves tag. Current Sauce plinth redirect can instead decrement1from stacked plinth ingredients; see [shared report](ars-family-pinned-sauce-74113.md). Do not advertise a free failed upgrade or charge-per-tick operation. Already-protected book fails match before normal start/payment.

## What is protected

ItemEntityMixin intercepts ItemEntity.hurt's isInvulnerableTo call at priority1846. For any dropped stack with P4Etrue and damage source **not** in BYPASSES_INVULNERABILITY, returns true; otherwise delegates original check. Thus ordinary dropped-item damage can be prevented, including paths such as lava/cactus only insofar as they use that hurt call/tag. It does **not** make a player invulnerable, keep the book in inventory on death, repair it, teleport it back, bind ownership or guarantee void survival. Soulbound is a separate enchantment/drop mechanism in [enchantment audit](ars-elemental-pinned-enchantments.md).

Events.despawnProtection on ItemExpireEvent checks same flag, calls **setUnlimitedLifetime** and sets extraLife0. This expresses an unlimited-lifetime request at expiry; actual loader expiry ordering/lifetime/reload interaction needs disposable-world verification, not a universal no-loss warranty. No global immunity to explicit discard, admin cleanup, inventories/corpse mods, empty stack removal or world deletion is established. This report does not promote moving upstream loader source as proof of the pinned loader recipient.

JEI SpellBookUpgradeRecipeCategory enumerates reagent books, or focused matching SpellBook input/output, and produces flagged **preview copies**. P4EEntry similarly constructs a display result. These are viewer representations, not additional rewards or products.

Future optional how-to: after ordinary Spell Book tier/Netherite access, reference appended Spell Books page, build apparatus/core, place five ingredients, supply2000Source, activate with unprotected book, wait/take actual same book and inspect Blessed/stored spell. Acquisition of book's item ID cannot distinguish flag or prove operation; use component-aware criterion only if supported after separate task audit, otherwise manual observation. Positive XP +ordinary low-value supplies; no tier/boss reward bypass.

Unrun positive/boundary: preserve known custom spell/name/color through upgrade; compare unflagged/flagged dropped books in ordinary damage and bypass-tag source, age expiry/reload, and full inventory output pickup. Reapply to flagged book should reject without Source start; remove pedestal after prepayment to observe cancellation/refund behavior. No destruction of user items or live world testing performed.

## Three native Crush transformations

Use native Worn Notebook **Glyph Index → Crush** and JEI **Crush** recipes. These add alternatives to ordinary crafting; actual selected recipe is first matching native CrushRecipe entry, so overlap/datapack precedence must be checked in effective recipe manager. Do not call their expected output guaranteed or a measured farm rate.

| Exact addon recipe | Input consumed per roll | Native output stacks, independent chance entries | Nominal expected items/input |
| --- | --- | --- | ---: |
| `ars_elemental:blaze_powder` |1Blaze Rod |3Blaze Powder certain +1with0.5chance |3.5 |
| `ars_elemental:bone_meal` |1Bone |3Bone Meal certain +1with0.5chance +1with0.5chance |4 |
| `ars_elemental:wool_to_string` |1`#minecraft:wool` |3String certain +1with0.5chance |3.5 |

All output entries `maxRange=1`: native CrushRecipe.getRolledOutputs independently rolls random.nextDouble≤chance and copies stack; maxRange>1would control number of stack copies, not spatial range. These recipes do not set it above1. No Fortune/Amplify-dependent output scaling in this recipe roll helper. Amplify's entity damage/harvest semantics are separate.

**Loose item route requires Sensitive.** Native EffectCrush.onResolve with stats.isSensitive queries ItemEntity in block-hit-location AABB inflate(AOE+1). Processing cap is integer **4+4×AOE+4×Pierce count**, across input units, not stacks or one entire pile. Per accepted input unit: roll outputs, shrink1input, increment cap counter, spawn each output ItemEntity at old item position; spawn return unchecked. Native routine does not directly insert inventory, place a container or consume jar Source. Normal spell route pays personal mana according to actual resolver/admission; current Conflux budget/billing review still required before quoting final cast fee. Repeat casting is not automatically free.

For these three item inputs, aim Touch/Projectile or another valid delivery at the dropped-item area with Crush+Sensitive; verify actual rolls and remaining stack. Without Sensitive a dropped rod/Bone is an entity target path, not automatic recipe processing. Sensitive also changes resolution dispatch and can coexist with AoE/Pierce item cap; don't confuse that with Fortune output bonus.

**Placed wool block route:** ordinary nonsensitive Crush.onResolveBlock matches block's item recipe. For wool outputs are non-BlockItem String, so spawn rolled strings and replace input block with AIR; actual placement/spawn outcomes unchecked by this body. Bonemeal and Blaze Rod are item recipes, not placeable blocks. Native Crush entity damage is separate(underwater base×3plusAmplify), so careless aiming can hurt creatures; current Sauce/Conflux filters/damage scope remain relevant.

Why use it: improve materials from already obtained Bone/Rod and convert wool to String; it neither creates Blaze Rods nor bypasses Nether/creature acquisition. A small workshop needs native Tier2Crush learning, Sensitive glyph and personal mana; no new processing machine or forbidden logistics dependency. Renewable upstream sheep/bone/rod supply, actual mob drops, recipe overlap and downstream sell/EMC behavior need separate balance proof.

Future optional quest: learn Crush and Sensitive through existing native book/Scribes route, drop small known pile, cast and retrieve output; point to exact JEI recipe. Input/output possession is a proxy and cannot attribute processing; manual action acknowledgement or real supported operation event only. Positive XP and ordinary processing materials; avoid repeatable profit rewards.

Unrun cases: 1input versus pile>cap; Sensitive versus absent, AoE/Pierce cap change, ordinary wool block versus dropped wool, output count over many trials without assuming every roll bonus, exhausted mana/canceled resolve, claim/event-denied output, overlapping recipe selection. Test source formulas and item loss/output delivery separately; no client/server gameplay PASS.
