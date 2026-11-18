# Ars Unification — focused addon profile

| Field | Value |
| --- | --- |
| Manifest ID / mod ID | `ars-unification` / `ars_unification` |
| Parent | [Ars Nouveau](ars-nouveau.md); recipe bridges depend on supported machine mods |
| Pack pin | `ars_unification-1.2.23.jar`; SHA-512 `e1da13185d03936adc9fc96162348081c7b788bdf77175758b26b805198d129ab9de761324b21afd6bdf76f1af2ad51b954dcea05389c0fa91e80d8c87a83498` |
| Source snapshot | [Release commit 1.2.23](https://github.com/Vonr/Ars-Unification/commit/1786bff672bcf6f6a9c6f571c9c140bbad4ab05c), matching the pack version; README describes Crush/Cut recipe import and optional Flatten/Press when Not Enough Glyphs is present. |
| Status | **researching** — recipe adapter model and installed integrations are mapped; exact pin cache priorities, every Occultism recipe, and client JEI/action acceptance remain. |
| Guide | Use JEI's imported Cut/Crush recipe categories and the Ars pages for the matching glyphs. Quest text should state which recipe and input entity/item the glyph consumes. |

## What it does and what TMM actually has

Ars Unification lets Ars glyphs use selected recipe types from supported processing mods. Ars-native recipes take priority. Other recipe priorities can be set in common config and recipe IDs can be excluded by regular expressions. Almost Unified is recommended by upstream to reduce duplicate/near-duplicate recipes.

| Adapter | Upstream supported machine | TMM selection |
| --- | --- | --- |
| Farmer's Delight Cut | Cutting Board | **Installed**; pilot teaches pumpkin/cabbage Cut outputs |
| Occultism Crush | Crusher Spirit | **Installed**; pilot does **not** teach this recipe/automation route |
| Other supported adapters | Actually Additions, AE2, Ender IO, Immersive Engineering, Integrated Dynamics, Mekanism, Modern Industrialization, Oritech | Not evidenced as installed in TMM's 2026-11-14 manifest; do not promise their recipes. |
| Flatten/Press | Not Enough Glyphs + supported press machine type | Not evidenced as selected/installed; leave out until roster confirms. |

Imported recipes provide the input/output contract from the target processor. For the TMM pilot, pumpkin/cabbage inputs may be cut either by ordinary Cutting Board or the Cut glyph sequence; item output collection is not proof magic performed it. The Occultism Crush adapter is a distinct installed path and deserves an optional how-to quest after JEI establishes supported recipes and costs.

## Walkthrough

1. For Farmer's Delight, inspect JEI Cut recipes; drop a pumpkin/cabbage and use Touch → Cut → Sensitive; collect and compare the output.
2. For Occultism Crusher Spirit, inspect JEI Crush category, then place one ordinary eligible input within its supported setup and compare the glyph output to the Crusher Spirit output.
3. Keep AoE/Pierce as optional efficiency upgrades; demonstrate in small batches, collect the result, and do not imply a magical cast task if FTB only checks inventory.
4. If a recipe is missing, check whether the mod's type is supported, whether Ars-native recipe overrides it, and the adapter priority/disable regex.

The 49-quest pilot's two quests cover only Farmer's Delight. Quest additions for Occultism are recommended; no runtime edits are part of this profile pass.

## Evidence and tests

- Exact pin and processor class references: `manifest/ars_quest_pilot.json` records `FarmersDelightCuttingBoardProcessor.class` and `EffectCutMixin.class`.
- [1.2.23 README](https://github.com/Vonr/Ars-Unification/blob/1786bff672bcf6f6a9c6f571c9c140bbad4ab05c/README.md); [source version metadata](https://github.com/Vonr/Ars-Unification/blob/1786bff672bcf6f6a9c6f571c9c140bbad4ab05c/gradle.properties).
- Installed selection context: TMM `manifest/mods.json` at `test/full-integration`; supported list is not the same as installed list.
- Not run: valid/invalid Cut and Crush recipe, native recipe priority, disabled regex and alternate priority, Occultism input entity constraints.

## Exact-binary adapter checkpoint — 2026-11-18

The pinned 120,451-byte artifact was recovered from its packwiz Modrinth URL and its full SHA-512 matched the field above. All 58 classes were extracted outside Git and CFR 0.152 decompiled the complete JAR, including inner classes. This replaces the missing-artifact caveat for the following adapter findings; it does not finish the profile.

- `FarmersDelightCuttingBoardProcessor.getIngredient` admits recipes with exactly one ingredient. `processCommon` creates a Cut wrapper under the original recipe ID and copies every rollable result stack and its chance. It does not copy or enforce the Cutting Board tool ingredient in this conversion method. Imported Cut is therefore not evidence a physical knife/board was used. Output rolling and final glyph consumption remain to trace.
- `OccultismCrusherProcessor.getIngredient` requires exactly one nonempty ingredient and a recipe whose minimum/maximum tier accepts the configured adapter tier. Nonnegative min/max bounds are inclusive; a negative bound is absent. `Config` defines `occultism.crusher.tier` default **1**, allowed **1–4**, and processor priority default **115** (`-1` disables). These are binary defaults, not a receipt of effective world configuration.
- Occultism conversion copies the result and uses the corresponding Foliot/Djinni/Afrit/Marid server-config output multiplier for tiers 1/2/3/4, unless the recipe's `getIgnoreCrushingMultiplier()` is true, which forces 1. Result count is `(int)(originalCount * multiplier)`, truncating the float product. Failure to obtain the result logs and omits the wrapper. Importing this recipe does not itself summon, consume or require a live Crusher Spirit; casting costs and native-priority filtering are separate paths still to trace.

Resume with `Processor`, `ArsUnification`, `GenericRecipeCache`, wrappers, `EffectCutMixin`, `AbstractEffectMixin` and native Ars Cut/Crush callers; then inspect both pinned Farmer's Delight and Occultism recipes/config endpoints. Do not turn the old “compare to Crusher Spirit” suggestion into a summoning prerequisite. External scratch is `Temp/tmm-magic-research-20261116/unification-fresh` and `artifacts/ars_unification-1.2.23.jar`. No runtime tests were run; status remains **researching**.
