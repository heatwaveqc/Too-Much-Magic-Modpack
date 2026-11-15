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
