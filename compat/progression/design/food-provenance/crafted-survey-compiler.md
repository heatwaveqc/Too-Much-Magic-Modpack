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
- Groups all distinct audited routes by output item ID; one unresolved recipe alternative **holds that entire output**. Does not attach crafting-origin NBT, create duplicate food variants or invent hunger/saturation numbers. The narrow owner-approved meatball finished-identity rule is automatic rather than a per-item value list.
- Excludes known nonfood seed and container outputs using **native item tag evidence**, and excludes vanilla-owned output IDs because the frozen 15-JAR sample does not enumerate vanilla's own alternative recipes. Other uncommon recipe shapes, unknown edible source materials, unknown tag members, ambiguous hot/cold preparation and recipe cycles produce explicit holds.

## Outputs and meaning

Generated review candidates are under `data/tyr_progression/tyr_progression/food_recipe_routes/` and `food_sources/` alongside classification tags under `data/tmm/tags/item/foods/generated/`. The summary file `crafted-food-survey-report.json` has exact counts, sorted generated IDs, excluded output IDs, and a complete held-output/reason ledger.

**Important:** A result from a recipe is not necessarily an edible item. `crafted_candidate` means source-traceable, not validated `DataComponents.FOOD`. `native_food_tag_candidate` is stronger evidence from existing `c:foods`, `c:juices` or `farmersdelight:meals` item tags, but still does not replace the same-build registry check. Java's dragon-only hook already guards on native FOOD components, but **do not promote the candidate files into a runtime build without review**.

On the first safety-filtered complete scan, the compiler classified 106 candidate outputs, 59 also carrying native food-tag evidence, 21 new ingredient-only sources, and excluded 209 nonfood/vanilla-owned outputs. The rest are held with precise reasons, mostly because required ingredients lack source classification. Exact counts can change with improvements; use the latest CI report rather than treating these as coverage promises.

## Integration and QA

Use the [crafted-food survey source-only workflow](../../../..//.github/workflows/crafted-food-survey.yml) to regenerate and upload JSON candidates and the hold report. It downloads only existing JSON audit evidence—**never third-party JARs**—and has `contents: read` permissions. The producer's tests and an independent candidate-graph validator check complete routes, ingredient dependencies, cycles, no duplicate routes and no nonfood promotion.

Promotion should happen in a separate reviewable Progression data change, reconciled against the final mod/recipe roster. Integration writer alone handles the full pack manifests, Packwiz and `test/full-integration`; owner-authorized B/C game QA remains necessary for actual edible registry coverage, dynamic KubeJS recipe changes, Dragon Survival client/server parity, native effects, container returns and multiplayer.

This compiler does **not** automatically assign an edible status, a numeric food-stat matrix, an item-NBT variant or a human nutrition override.
