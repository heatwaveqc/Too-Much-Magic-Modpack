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

Ars Unification lets Ars glyphs use selected recipe types from supported processing mods. Ars-native recipes take priority. Other recipe priorities can be set in server config and recipe IDs can be excluded by regular expressions. Almost Unified is recommended by upstream to reduce duplicate/near-duplicate recipes.

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

- `FarmersDelightCuttingBoardProcessor.getIngredient` admits recipes with exactly one ingredient. `processCommon` creates a Cut wrapper from the original recipe ID (under a new namespaced ID, as clarified below) and copies every rollable result stack and its chance. It does not copy or enforce the Cutting Board tool ingredient in this conversion method. Imported Cut is therefore not evidence a physical knife/board was used. Output rolling and final glyph consumption remain to trace.
- `OccultismCrusherProcessor.getIngredient` requires exactly one nonempty ingredient and a recipe whose minimum/maximum tier accepts the configured adapter tier. Nonnegative min/max bounds are inclusive; a negative bound is absent. `Config` defines `occultism.crusher.tier` default **1**, allowed **1–4**, and processor priority default **115** (`-1` disables). These are binary defaults, not a receipt of effective world configuration.
- Occultism conversion copies the result and uses the corresponding Foliot/Djinni/Afrit/Marid server-config output multiplier for tiers 1/2/3/4, unless the recipe's `getIgnoreCrushingMultiplier()` is true, which forces 1. Result count is `(int)(originalCount * multiplier)`, truncating the float product. Failure to obtain the result logs and omits the wrapper. Importing this recipe does not itself summon, consume or require a live Crusher Spirit; casting costs and native-priority filtering are separate paths still to trace.

Resume with `Processor`, `ArsUnification`, `GenericRecipeCache`, wrappers, `EffectCutMixin`, `AbstractEffectMixin` and native Ars Cut/Crush callers; then inspect both pinned Farmer's Delight and Occultism recipes/config endpoints. Do not turn the old “compare to Crusher Spirit” suggestion into a summoning prerequisite. External scratch is `Temp/tmm-magic-research-20261116/unification-fresh` and `artifacts/ars_unification-1.2.23.jar`. No runtime tests were run; status remains **researching**.

## Import ordering, casting and two-sided recipe receipt — 2026-11-18

Direct binary paths: `ArsUnification.processRecipes`, `Processor.processRecipes/getSortedRecipes/makeRecipe`, `RecipeWrapper`, `EffectCutMixin.wrapResolve`, `ItemUtil.processItems`, `GenericRecipeCache`, `CutRecipe.rollOutputs`; native Ars `EffectCrush` from the hash-verified 5.13.5 artifact.

**Import is server-side recipe conversion.** The addon registers a SERVER config, not a COMMON config. After config is loaded, processors sort by descending priority; `-1` or absent target mod skips that processor. Within each processor, original IDs sort lexicographically. Existing target-type inputs are collected first, and each successful import claims its input item IDs, so prior native/custom target recipes and earlier imports take precedence. Farmer's Delight default priority is60; Occultism115. The priorities apply within their processing types rather than making Cut displace Crush.

`Processor` rejects excluded IDs, null/empty ingredients, vanilla ingredients with multiple values, empty item alternatives, input stacks whose count is not1, and already-claimed item IDs. It can reduce a tag to its remaining item alternatives. Custom/other ingredients are flattened to their exposed item stacks; this does not preserve a promise of arbitrary custom component/predicate semantics. Import retains the original machine recipe and adds `ars_unification:cut/<original namespace>/<original path>` or `ars_unification:crush/...`; it does **not** retain the original ID unchanged. Imported recipes enter the recipe manager on startup/datapack synchronization; recipe counts in a JAR are not the count of live eligible imports.

**Cut item route.** Learn native Cut and Sensitive through the Ars guide/Scribe route, save `Touch → Cut → Sensitive`, drop eligible inputs near the aimed block and cast. On a server, Sensitive intercepts Cut before ordinary resolution: search box is the hit block's unit box inflated by `1 + AoE multiplier`; per-cast input-item limit is integer `4 + 4×AoE multiplier + 4×Pierce count`. The helper finds matching recipes, rolls outputs (once and scaled for deterministic outputs; independently per input otherwise), then shrinks input stacks. Invalid inputs are skipped. Output stacks become dropped item entities; a fully used input entity can be reused for the first output with pickup delay0 and refreshed lifetime. Failed world insertion is not refunded here. Normal Cut is called when this interception condition does not apply. A successful normal cast can still spend personal mana when nothing eligible is nearby; the helper does not add a Source bill or a recipe-specific mana bill. Ars Technica doubling branches exist in the binary but are conditional optional integration, not a native unconditional multiplier.

**Crush item route.** Use native `Touch → Crush → Sensitive` on dropped items. Native `EffectCrush` uses the same `4 + 4×AoE + 4×Pierce` limit and hit-block inflated box, consumes one item per matching roll and spawns output entities. Imported Occultism recipes are ordinary Ars Crush recipes; no live spirit or native crushing-time wait is copied into the wrapper. Without Sensitive, Crush retains entity damage/block transformation behavior; use the item route for an unambiguous tutorial. Personal casting mana remains the parent spell engine's cost; recipe conversion itself does not debit mana, durability or Source.

**Recipe cache boundary.** Cut cache starts at8 entries, caches both hits and same-item/same-component misses, and clears on all-player datapack synchronization. A cached miss can matter while debugging reload state; it is not evidence the source recipe never existed. The import excludes original machine IDs using case-insensitive anchored regex matching, compiled on config loading. Effective world settings must be checked separately; no checked-in Unification config was found under `config`/`defaultconfigs` in this research checkout.

| Exact second endpoint | Artifact evidence | Player-visible contract and limit |
| --- | --- | --- |
| Farmer's Delight1.3.4 | `FarmersDelight-1.21.1-1.3.4.jar`,3160490bytes,SHA1 `27675ddfcb8cc37e6ea1ab6c15051c7949d94657`; all106 `farmersdelight:cutting` resource JSONs counted | `cutting/pumpkin`:1pumpkin→4pumpkin slices; `cutting/cabbage`:1cabbage→2cabbage leaves. Physical recipes require knife ability/tag; adapter copies results and chances, not the tool. These two pilot outputs are deterministic recipe data, not evidence that a glyph operated. |
| Occultism1.227.0 | `occultism-1.21.1-neoforge-1.227.0.jar`,6961729bytes,fullSHA512matches its packwiz pin; all240 `occultism:crushing` JSONs inspected as data |236resources declare no tier bound;3declare min2 and1min4.104set ignore-multiplier true. Many recipes have nonempty-tag conditions, so240is not a live import count. Iron raw-material tag→2iron-dust tag units before selected spirit multiplier; iron-ingot tag→1iron-dust tag unit with ignore-multiplier true. Exact tags must resolve and competing native inputs must remain unclaimed. |

Useful setups are a small pumpkin/cabbage preparation spot, a separate tagged-mineral Crush spot after JEI confirms availability, and optional AoE/Pierce batch casting with measured inputs. This adds recipe utility without its own item tier ladder, altar or boss. Cut/Crush glyph acquisition, their exact glyph ingredients/base mana and the recipient serializer defaults/multiplier values still need a final linked audit before this profile is source-complete.

Proposed acceptance cases (not run): cast on1pumpkin and observe4slices; cast on1cabbage and observe2leaves; compare physical knife requirement to magical route;5eligible inputs with no augments must consume at most4; invalid input must remain while a normal successful cast may cost mana; confirm native-priority collision and partial tag exclusion; compare a tier1-eligible Occultism recipe with min2/min4 exclusions; compare multiplier-enabled raw input to ignore-multiplier ingot input; test reload after a cached miss. Existing pilot rows47/48 detect output possession and cannot prove these actions. Reward directions: small XP and food/recipe ingredients; avoid summoning-gate bypass rewards. No quest changes or gameplay acceptance are implied.
