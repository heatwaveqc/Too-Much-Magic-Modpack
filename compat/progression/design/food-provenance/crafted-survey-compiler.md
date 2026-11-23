# Crafted food dietary survey compiler — issue #444

## Purpose and trusted inputs

This is a **source-only compiler**, stacked on PR #581 and its food-policy/source dependencies. It replaces hand-maintained item classifications for prepared foods with reproducible extraction from the **full ingredient-level Codex native recipe census**. That census includes 2,460 recipe resources from 15 approved, pinned food JARs; the frozen source is GitHub Actions run [#45103149371](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/45103149371), artifact `approved-food-jar-audit-pinned`.

The Progression feature branch has a stale `mods/` roster. Source verification therefore intentionally uses the immutable survey roster commit `48a55bde7c148c9b215eb0e66e7e8e8e465f4c5e` (PR #551), not the branch-local mod list. SHA1 **or SHA512** in the audited JAR metadata must match the exact Packwiz filename/hash in that snapshot. The workflow checks out the roster read-only. Integration must reconcile the actual target roster again before promotion.

## Deterministic pipeline

Run with Python 3.12 after retrieving the existing JSON artifact:

```bash
python scripts/compile_crafted_food_survey.py \
  --repo . --pin-snapshot /path/to/exact-audited-roster \
  --audit /path/to/food-jar-audit-pinned.json \
  --out /tmp/tmm-compiled-food
python scripts/validate_compiled_food_routes.py --repo . --out /tmp/tmm-compiled-food
```

- Reads all checked-in FoodSourceEvidence roots and reviewed FoodRecipeEvidence routes (currently 162 roots and eight reviewed recipe outputs).
- Merges pinned native item tags across JARs and expands tag nesting, NeoForge compound/difference/intersection ingredients. Tag aliases are collapsed **only when they have identical proven source budgets, family tags, and preparation**, otherwise all eligible alternatives remain separate; the runtime's worst-case route chooser handles them.
- Automatically synthesizes supplemental **ingredient-only** roots from verified native grain, rice and mushroom tags, plus kelp as a Sea Dragon marine plant. These records contain a family, a raw-stage tag and one source unit. They neither register FOOD components nor change the food properties of any item.
- Recognizes verified cooking operations (furnace, smoker, campfire, Farmer's Delight cooking, Pam pot/skillet/saucepan/bakeware), otherwise known cold shapeless assembly and cutting. Heat-implying names such as grilled, roasted or cookie **without a proven hot station** are held rather than assumed raw. Non-nutritive tools and containers are omitted from the food budget; source ingredient quantities and recipe result counts are preserved.
- Groups all distinct audited routes by output item ID; **only native food-tagged finished outputs are candidate meals**. Untagged routes are examined solely if needed for supporting source ancestry. One unresolved recipe alternative **holds that entire food output**. Does not attach crafting-origin NBT, create duplicate food variants or invent hunger/saturation numbers. The narrow owner-approved meatball finished-identity rule is automatic rather than a per-item value list.
- Excludes known nonfood seed and container outputs using **native item tag evidence**, and excludes vanilla-owned output IDs because the frozen 15-JAR sample does not enumerate vanilla's own alternative recipes. Other uncommon recipe shapes, unknown edible source materials, unknown tag members, ambiguous hot/cold preparation and recipe cycles produce explicit holds.

## Outputs and meaning

Generated review candidates are under `data/tyr_progression/tyr_progression/food_recipe_routes/` and `food_sources/` alongside classification tags under `data/tmm/tags/item/foods/generated/`. The summary file `crafted-food-survey-report.json` counts native-tagged recipe outputs, actual generated food candidates, necessary untagged provenance support, excluded outputs, and **only food-relevant unresolved output IDs**. Nonfood/unclassified outputs are excluded from the food hold queue.

**Important:** A result from a recipe is not necessarily an edible item. `crafted_candidate` means source-traceable, not validated `DataComponents.FOOD`. `native_food_tag_candidate` is stronger evidence from existing `c:foods`, `c:juices` or `farmersdelight:meals` item tags, but still does not replace the same-build registry check. Java's dragon-only hook already guards on native FOOD components, but **do not promote the candidate files into a runtime build without review**.

**Output-first food scope (owner clarification, 2026-11-23):** The compiler now evaluates a finished recipe output only if the item's ID is included in one of the recognized native food item tags (`c:foods`, `c:juices`, `farmersdelight:meals`), including those tags' recursive contents. Untagged crafting recipes are not candidate dietary foods. They may still be traversed and emitted as **supporting recipe provenance** when a tagged food requires them as ingredients; supporting intermediates must never appear in `crafted_candidate`, `native_food_tag_candidate` or the generated method/identity food tags. A future complete same-build FOOD-component registry can expand the target set to genuine edible items missing these conventional tags; the current source-only compiler does not guess from recipe names.

On the updated source scan, **131 recipe-output item IDs have native food-tag evidence; 57 generate dietary recipe candidates, 65 remain held for food-relevant reasons**, and the others are previously reviewed or excluded. The compiler skips **1,916 untagged output IDs**, rather than displaying them as unresolved *foods*. It generates **21 ingredient-only source records, four food-candidate item tags**, and excludes **209 known nonfood or vanilla-owned outputs**. The independent graph validator passed all **57 finished-food candidates**, with no extra supporting routes needed in this particular frozen scan. Heat-implying shapeless recipes without a verified hot station remain explicit food holds rather than being rewarded as cold processing. Exact counts are regenerated with each audited roster; none imply a native FOOD-component registration or gameplay QA.

## Integration and QA

Use the [crafted-food survey source-only workflow](../../../../.github/workflows/crafted-food-survey.yml) to regenerate and upload JSON candidates and the hold report. It downloads only existing JSON audit evidence—**never third-party JARs**—and has `contents: read` permissions. The producer's tests and an independent candidate-graph validator check complete routes, ingredient dependencies, cycles, no duplicate routes and no nonfood promotion.

Promotion should happen in a separate reviewable Progression data change, reconciled against the final mod/recipe roster. Integration writer alone handles the full pack manifests, Packwiz and `test/full-integration`; owner-authorized B/C game QA remains necessary for actual edible registry coverage, dynamic KubeJS recipe changes, Dragon Survival client/server parity, native effects, container returns and multiplayer.

This compiler does **not** automatically assign an edible status, a numeric food-stat matrix, an item-NBT variant or a human nutrition override.
