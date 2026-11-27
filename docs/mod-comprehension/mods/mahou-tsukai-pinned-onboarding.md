# Mahou Tsukai — pinned onboarding and circle recipe crosswalk

Companion to [the main profile](mahou-tsukai.md). Inspected2026-11-16 using exact `mahoutsukai-1.21.1-v1.36.31.jar`, SHA-512 `0c4f3bc823d681ad9cb00895d348d2b414201c4d397ca3aa6427b45b8d2b369e0bae8c1f354a9541e4205dadb58c49203d3570e2433d772c3eef1227bc8bfa5a`, independently matched to [its pin](../../../mods/mahou-tsukai.pw.toml). **Researching; no client/world tests.** Direct binary evidence replaces the old missing-JAR limitation for these sections only.

## Exact first-session sequence

1. **Craft the Mahou Tsukai Knowledge Compendium**, `mahoutsukai:guidebook`. JEI recipe `mahoutsukai:guidebook`: five Red Dye, three Paper and one Leather in the grid below. `MahouTsukaiGuidebook.use` opens the custom guide GUI on the client; this is a native book, not a Patchouli requirement.
2. Read **Mahou Tsukai**, **Mana**, **Performing Magic**, **Catalysts**, **Catalysts (cont.)** and the named spell page. The pinned `GuidebookInstance.createPages` explicitly inserts guide sections and spell recipes, drawing values from MTConfig and translated keybind text. It also contains Leylines, The Fae, Mana Circuits, Mystic Code and other sections; inspect these as the branch progresses. Page presence is confirmed, not client render quality.
3. **Craft Hammer first** if minimizing startup material burden: one String, two Cobblestone, two Sticks in JEI's shaped pattern. Diamond cost is zero. Combining a supported material with the reusable Hammer yields one powder.
4. Optional efficiency upgrade: **Mortar** costs two Bricks plus one Diamond; **Pestle** costs three Diamonds plus two Sticks; combine them shapelessly into **Mortar and Pestle**. Total **four Diamonds**, two Bricks and two Sticks. This tool yields two powders per material; do not label it cheaper than Hammer or necessary to start.
5. Craft the reusable **Dagger** from two Iron Ingots, two Gold Ingots, two Purple Dye and one Stick in its JEI grid. Its server `use` requests generic damage2 health points and adds Bleeding with duration300 game ticks (nominal15 seconds at20TPS), regardless of the damage-call boolean. Whether armor/immunity/event hooks change actual health/effect requires testing; do not describe a measured health debit.
6. Face open floor roughly one block ahead, use Dagger, then the configured **Draw Mahoujin** key while Bleeding is active. Exact `KeyPressPacket.drawMahoujin` checks player Mahou capability and Bleeding, chooses a support/placement position from horizontal look, accepts air or Spell Cloth at placement and applies the solid-only option/support rules. On successful placement it assigns caster/cloth state, sets `hasMagic=true`, triggers native Mahou advancement logic and syncs the client.
7. For **Alarm Boundary**, prepare **two Powdered Iron and one Powdered Quartz**. Right-click the blank circle with the powders. Each accepted powder is consumed immediately. **Order does not matter; multiplicity does.** The exact `UnorderedList.equals` compares catalyst names/counts, and its hash is order-insensitive. This corrects the older ordered-input description.
8. Choose a cloth-required spell only after placing **Spell Cloth before drawing**. One White Wool plus reusable Dagger crafts four cloth. The draw packet replaces the cloth block with a blank circle retaining `hasCloth`; do not place cloth afterward and expect the existing circle's flag to change automatically.
9. On third powder, `Mahoujin.transformToSpell` looks up the counted recipe, checks GameStages/blacklist and cloth requirement, then changes the block to the spell and carries caster/catalyst/cloth state. Failed eligibility does not show an automatic refund of previously consumed powders in this path. Use the correct named guide recipe; test recovery from invalid/blocked three-catalyst state before prescribing it.

Compendium grid (`D` Red Dye, `P` Paper, `L` Leather):

```text
DPP
DLP
DDD
```

Exact onboarding evidence: `data/mahoutsukai/recipe/{guidebook,hammer,mortar,pestle,mortar_and_pestle,dagger,spell_cloth,powdered_*}.json`; `stepsword/mahoutsukai/item/{MahouTsukaiGuidebook,Dagger,Hammer,MortarAndPestle}.class`; `item/guide/GuidebookInstance.class`; `networking/KeyPressPacket.class`; `block/spells/{Mahoujin,MahoujinRecipeRegistrar}.class`; `tile/MahoujinTileEntity.class`; `util/UnorderedList.class`; `assets/mahoutsukai/lang/en_us.json`.

### Typed starter crafting contracts

| Material input item | Hammer +1 powder recipe | Mortar and Pestle +2 powder recipe |
| --- | --- | --- |
| Iron Ingot | `powdered_iron` | `powdered_iron_2` |
| Gold Ingot | `powdered_gold` | `powdered_gold_2` |
| Quartz | `powdered_quartz` | `powdered_quartz_2` |
| Diamond | `powdered_diamond` | `powdered_diamond_2` |
| Emerald | `powdered_emerald` | `powdered_emerald_2` |
| Ender Pearl | `powdered_ender` | `powdered_ender_2` |
| Eye of Ender | `powdered_eye` | `powdered_eye_2` |

Prefix recipe keys with `mahoutsukai:`. All14 are vanilla shapeless crafting recipes in the artifact; these are exact vanilla input item IDs, not a general ore/material-tag processing interface. Hammer and MortarAndPestle override crafting-remainder behavior to return a copy of the input tool; no wear decrement occurs in that returned copy path. Dagger does likewise for cloth crafting. Remaining-item handling and mass crafting should still be checked in the actual client. The outputs are Mahou powdered catalysts, not confirmed smeltable industrial dusts.

### First-circle placement and failed-use state

The packet's initial support selection excludes liquid blocks and uses `entityCanStandOnFace(UP)`, a ChestBlock exception, or disabled `BLOOD_CIRCLE_SOLID_ONLY`. If placement is obstructed it may shift the attempt upward once. Bleeding removal sits inside the later support branch **outside** the final air/cloth placement check; a blocked final position can consume the current Bleeding opportunity without placing a circle. The Dagger already requested damage before this placement action. Record health, effect state, world block and advancement separately; a failed keypress is not proven free.

This sequence maps guide/tool/catalyst/blank-circle acquisition. Activation/mana/effects of Alarm Boundary and every other spell remain in their specific implementations; recipe registration alone does not prove that a spell is usable in a protected area or with a given mana amount.

## All registered three-catalyst circle recipes

The following crosswalk summarizes `MahoujinRecipeRegistrar.init` registrations. Powder labels are suffixes of `mahoutsukai:powdered_*`; apply exactly three counted units. The output column is an **internal ModBlocks field**, not an invented registry ID. “Cloth required” is the registrar flag consumed by `transformToSpell`; no flag is treated false there. False does not prove that a spell supports scroll extraction or that all scroll/circle forms behave identically. A bundled book match is a named recipe reference, not an audit of its spell effect.

| Internal recipe key | Counted powders (any order) | Internal block field | Cloth required | Bundled named spell page |
| --- | --- | --- | --- | --- |
| `alarmBoundaryRecipe` | 2× iron; 1× quartz | `alarmBoundaryMahoujin` | false | Alarm Boundary |
| `drainLifeBoundaryRecipe` | 2× iron; 1× emerald | `drainLifeBoundaryMahoujin` | false | Boundary of Drain Life |
| `tangibleBoundaryRecipe` | 2× iron; 1× diamond | `tangibleBoundaryMahoujin` | false | Tangible Boundary |
| `gravityBoundaryRecipe` | 2× iron; 1× gold | `gravityBoundaryMahoujin` | false | Gravity Boundary |
| `displacementBoundaryRecipe` | 2× iron; 1× ender | `displacementBoundaryMahoujin` | false | Displacement Boundary |
| `raiseEnclosureBoundaryRecipe` | 3× iron | `raiseEnclosureBoundaryMahoujin` | false | Enclosure Boundary |
| `projectionRecipe` | 2× diamond; 1× quartz | `projectionMahoujin` | true | Projection |
| `weaponShooterRecipe` | 2× diamond; 1× gold | `weaponShooterMahoujin` | true | Weapon Projectiles |
| `strengtheningRecipe` | 2× diamond; 1× iron | `strengtheningMahoujin` | true | Strengthening |
| `proximityProjectionRecipe` | 2× diamond; 1× ender | `proximityProjectionMahoujin` | true | Proximity Projection |
| `realityMarbleRecipe` | 3× diamond | `realityMarbleMahoujin` | true | Reality Marble |
| `powerConsolidationRecipe` | 2× diamond; 1× emerald | `powerConsolidationMahoujin` | false (implicit) | Power Consolidation |
| `treasuryProjectionRecipe` | 2× diamond; 1× eye | `treasuryProjectionMahoujin` | true | Treasury Projection (cont.) |
| `projectileDisplacementRecipe` | 2× ender; 1× diamond | `projectileDisplacementMahoujin` | true | Projectile Displacement |
| `ascensionRecipe` | 2× ender; 1× gold | `ascensionMahoujin` | false | Ascension |
| `orderedDisplacementRecipe` | 3× ender | `orderedDisplacementMahoujin` | false (implicit) | Ordered Displacement |
| `equivalentDisplacementRecipe` | 2× ender; 1× emerald | `equivalentDisplacementMahoujin` | false (implicit) | Equivalent Displacement |
| `mentalDisplacementRecipe` | 2× ender; 1× quartz | `mentalDisplacementMahoujin` | true | Mental Displacement |
| `protectiveDisplacementRecipe` | 2× ender; 1× iron | `protectiveDisplacementMahoujin` | true | Protective Displacement |
| `scryingRecipe` | 2× ender; 1× eye | `scryingMahoujin` | false (implicit) | Sympathetic Magic (cont.) |
| `catalystExchangeRecipe` | 3× emerald | `catalystExchangeMahoujin` | false (implicit) | Catalyst Exchange |
| `durabilityExchangeRecipe` | 2× emerald; 1× diamond | `durabilityExchangeMahoujin` | false (implicit) | Durability Exchange |
| `damageExchangeRecipe` | 2× emerald; 1× iron | `damageExchangeMahoujin` | true | Damage Exchange |
| `chronalExchangeRecipe` | 2× emerald; 1× quartz | `chronalExchangeMahoujin` | false (implicit) | Chronal Exchange |
| `alchemicalExchangeRecipe` | 2× emerald; 1× gold | `alchemicalExchangeMahoujin` | false (implicit) | Alchemical Exchange |
| `contractRecipe` | 2× emerald; 1× ender | `contractMahoujin` | false (implicit) | Contract |
| `immunityRecipe` | 2× emerald; 1× eye | `immunityExchangeMahoujin` | true | Immunity Exchange |
| `mysticStaffRecipe` | 2× gold; 1× diamond | `mysticStaffMahoujin` | true | Staff of Explosive Mana Condensation (cont.) |
| `borrowedAuthorityRecipe` | 3× gold | `borrowedAuthorityMahoujin` | true | Borrowed Authority |
| `rhoAiasRecipe` | 2× gold; 1× iron | `rhoAiasMahoujin` | true | Rho Aias |
| `damageReplicationRecipe` | 2× gold; 1× emerald | `damageReplicationMahoujin` | true | Shared Pain |
| `spatialDisorientationRecipe` | 2× gold; 1× ender | `spatialDisorientationMahoujin` | true | Staff of Spatial Disorientation |
| `cupOfHeavenRecipe` | 2× gold; 1× quartz | `cupOfHeavenMahoujin` | false | Cup of Heaven (cont.) |
| `summonFamiliarRecipe` | 3× quartz | `summonFamiliarMahoujin` | true | Summon Familiar |
| `recallFamiliarRecipe` | 2× quartz; 1× ender | `recallFamiliarMahoujin` | true | Recall Familiar |
| `possessEntityRecipe` | 2× quartz; 1× gold | `possessEntityMahoujin` | true | Shared Vision |
| `swapFamiliarRecipe` | 2× quartz; 1× emerald | `swapFamiliarMahoujin` | true | Familiar Exchange |
| `familiarGardenRecipe` | 2× quartz; 1× iron | `familiarsGardenMahoujin` | true | Familiar's Garden |
| `butterflyEffectRecipe` | 2× quartz; 1× diamond | `butterflyEffectMahoujin` | true | The Butterfly Effect |
| `mysticEyesRecipe` | 2× eye; 1× gold | `mysticEyesMahoujin` | true | Mystic Eyes of Binding |
| `predictionRecipe` | 2× eye; 1× quartz | `predictionMahoujin` | true | Minor Clairvoyance |
| `deathCollectionRecipe` | 2× eye; 1× iron | `deathCollectionMahoujin` | true | Mystic Eyes of Death Collection |
| `blackFlameRecipe` | 2× eye; 1× diamond | `blackFlameMahoujin` | true | Mystic Eyes of the Black Flame |
| `reversionEyesRecipe` | 2× eye; 1× emerald | `reversionEyesMahoujin` | true | Mystic Eyes of Reversion |
| `faySightRecipe` | 3× eye | `faySightMahoujin` | true | Mystic Eyes of Fay Sight |
| `insightRecipe` | 2× eye; 1× ender | `insightEyesMahoujin` | true | Mystic Eyes of Insight |
| `retributionRecipe` | 1× emerald; 1× iron; 1× diamond | `retributionMahoujin` | true | Retribution |
| `presenceConcealmentRecipe` | 1× ender; 1× iron; 1× diamond | `presenceConcealmentMahoujin` | true | Presence Concealment (cont.) |
| `gandrRecipe` | 1× gold; 1× emerald; 1× diamond | `gandrMahoujin` | true | Gandr |
| `geasRecipe` | 1× gold; 1× emerald; 1× iron | `geasMahoujin` | true | Geas (cont.) |
| `fallenDownRecipe` | 1× gold; 1× ender; 1× iron | `fallenDownMahoujin` | true | Fallen Down (cont.) |
| `probabilityAlterRecipe` | 1× gold; 1× diamond; 1× quartz | `probabilityAlterMahoujin` | false | Probability Alter |
| `selectiveDisplacementRecipe` | 1× eye; 1× ender; 1× emerald | `selectiveDisplacementMahoujin` | true | Selective Displacement |

**Inventory result:** 53 registered recipes, each three catalysts; 35 explicitly require cloth. This is the pinned runtime registrar inventory, separate from the33 JSON crafting resources (including special William book-cloning serializer), and separate from the current wiki's broader named spell/item catalog. Do not substitute wiki counts for pinned registrations.

## Walkthrough and test coverage

| Lesson | Actionable steps / reference | Honest detector and reward direction | Unrun positive/boundary check |
| --- | --- | --- | --- |
| Open Compendium | Exact grid above → right-click → named Mahou Tsukai/Catalysts pages | Book item proves acquisition, not opening; manual reading confirmation, XP plus ordinary supplies after global reward/task audit | Client book opens and key labels match actual rebound controls. |
| First powder | Craft Hammer → shapeless Iron/Quartz processing → retrieve powder and unchanged tool | Powder acquisition proxy; do not call it ore doubling. XP plus modest crafting supplies, not the next quest's complete powder set | Input/tool/output counts in manual and bulk crafting; wrong material/tagged alternate. |
| Efficient powder option | Four-Diamond combined tool → same material → two powder | Optional branch; item task only acquisition. No arbitrary quest gate requiring this before first spell | Tool remainder and output counts; no duplicate tool or wear assumption. |
| First Mahoujin | Dagger → Bleeding → clear support → configured key | Native trigger emitted on successful placement; `mahoutsukai:root` has the matching custom trigger; FTB support/credit still needs client verification | Expired Bleeding, obstructed floor, liquid/chest/solid-only option, repeated keypress, failed opportunity removal. |
| First chosen spell | Named Compendium recipe → three correct powders in any order → correct cloth state → transform | Spell-state/manual operation evidence; inventory item task cannot prove a placed circle | Reordered same-count powders vs incorrect counts, cloth-required versus dry, blacklist/GameStage gate and powder refund behavior. |

## Native advancement data crosswalk

There are19 singular-path advancement JSON definitions in the pinned artifact. Criteria below are declarations, not proof of how to perform every custom trigger. `KeyPressPacket.drawMahoujin` directly calls the registered Mahou trigger on successful first-circle placement, matching `mahoutsukai:root`; other custom trigger emitters remain a required mechanics audit. No item-possession proxy is silently substituted for these events.

| Exact advancement ID | Parent declared | Criterion trigger(s) |
| --- | --- | --- |
| `mahoutsukai:bakuretsu` | `mahoutsukai:root` | `mahoutsukai:bakuretsu` |
| `mahoutsukai:beam` | `mahoutsukai:root` | `mahoutsukai:beam` |
| `mahoutsukai:boogie_woogie` | `mahoutsukai:root` | `mahoutsukai:boogie_woogie` |
| `mahoutsukai:breadcrumbs` | `mahoutsukai:morgan` | `mahoutsukai:breadcrumbs` |
| `mahoutsukai:butterfly_effect` | `mahoutsukai:root` | `mahoutsukai:butterfly_effect` |
| `mahoutsukai:clarent` | `mahoutsukai:root` | `mahoutsukai:clarent` |
| `mahoutsukai:emrys` | `mahoutsukai:root` | `mahoutsukai:emrys` |
| `mahoutsukai:flying_pigs` | `mahoutsukai:root` | `mahoutsukai:flying_pigs` |
| `mahoutsukai:gandr` | `mahoutsukai:root` | `mahoutsukai:gandr` |
| `mahoutsukai:greater_grail` | `mahoutsukai:root` | `mahoutsukai:greater_grail` |
| `mahoutsukai:kodoku` | `mahoutsukai:root` | `mahoutsukai:kodoku` |
| `mahoutsukai:lesser_grail` | `mahoutsukai:root` | `mahoutsukai:lesser_grail` |
| `mahoutsukai:morgan` | `mahoutsukai:root` | `mahoutsukai:morgan` |
| `mahoutsukai:nobu` | `mahoutsukai:root` | `mahoutsukai:nobu` |
| `mahoutsukai:replica` | `mahoutsukai:root` | `mahoutsukai:replica` |
| `mahoutsukai:root` | `none` | `mahoutsukai:mahoutsukai` |
| `mahoutsukai:secret_calibur` | `mahoutsukai:root` | `mahoutsukai:secret_calibur` |
| `mahoutsukai:sword_in_the_lake` | `mahoutsukai:root` | `mahoutsukai:sword_in_the_lake` |
| `mahoutsukai:treasury_projection` | `mahoutsukai:root` | `mahoutsukai:treasury_projection` |

The root criterion is `mahoutsukai:mahoutsukai`, not a generic inventory_changed task. Advancement names alone do not prove sword acquisition, damage growth, lake completion or item-use conditions; inspect each emitter before quest authoring.

## Remaining research

No missing-artifact blocker remains for Mahou Tsukai. Still researching: core mana growth/regen and draw order; all spell cost/effect/target/failure rules; active-circle/scroll extraction/activation; special weapons and rare item acquisition; circuit/leyline/Fae/artifact economy; effective configs, integration scripts and client acceptance. The registrar and starter recipe inventory alone do not complete the full progression.
