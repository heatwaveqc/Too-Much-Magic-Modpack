# Runtime food-component census — source-ready instrument (#444)

## Why this exists

The pinned Codex native JAR survey already knows ingredient counts and recipe outputs, but conventional item tags (`c:foods`, `c:juices`, `farmersdelight:meals`) miss most Pam's finished meals. **An item being described as a food in a recipe or having a related tag does not establish edibility**. A complete proof for the current assembled target depends on examining the actual registered default item stacks and active recipe output previews, after all NeoForge, KubeJS and datapack registrations.

## How to run (owner-authorized existing test world only)

1. Once the Progression PR stack is integrated into a coherent **owner-authorized** TMM test build, start an already-approved disposable server or a test world running the real pinned roster. **Do not boot a new full pack solely because this document exists.**
2. As a permission-level-2 server operator, execute `/tyrprogress admin foodcensus`. It runs once on demand; there is no periodic event, automatic CI boot or player-facing power.
3. Retrieve `world/reports/tmm-food-registry-v1.json` from the world directory (for a single-player integrated test, the equivalent save's `reports` directory). The exporter writes atomically, sorted by item or recipe ID, with no player or world-generation data.
4. Supply that file to the existing recipe compiler, alongside Codex's original pinned `food-jar-audit-pinned.json` and its immutable verified Packwiz roster:

```bash
python scripts/compile_crafted_food_survey.py \
  --repo . \
  --pin-snapshot /path/to/Codex-pinned-roster \
  --audit /path/to/food-jar-audit-pinned.json \
  --registry-json /path/to/tmm-food-registry-v1.json \
  --out /tmp/tmm-compiled-food
python scripts/validate_compiled_food_routes.py --repo . --out /tmp/tmm-compiled-food
```

## Report contract

- Every **loaded item ID** has a `food: true|false` default-stack `DataComponents.FOOD` result and the item tags effective on the server. This replaces loose `c:foods` membership as the primary final-food eligibility input.
- Every **active recipe ID** is inspected through `RecipeManager.getRecipes()` after native conditions and server recipe reload. The `Recipe.getResultItem(registryAccess)` **preview** is captured with item ID, count and whether that stack itself has a `FOOD` component. This can detect a recipe-specific component when the default item is nonfood. It is not a synthetic merge of all optional native JSONs.
- Recipes whose `getResultItem` returns EMPTY or throws are separately listed in `unresolved_recipe_result_previews`. Some custom/dynamic recipes only identify the final stack when `assemble(input, registryAccess)` is called with concrete inputs: this preview census **does not assert those variants are completely covered** and deliberately declares `recipe_output_stacks_examined:false`.
- `item_count`, `food_count`, `loaded_recipe_count`, `recipe_preview_count`, `recipe_preview_food_count` and `recipe_preview_unresolved_count` must match the enumerated entries. The Python loader rejects duplicates, malformed item/tag IDs, false counts and incompatible schemas, rather than guessing.
- The compiler targets the **union** of default-stack FOOD IDs and active recipe-preview FOOD IDs. It uses actual loaded tag members to resolve ingredients. Optional tag members absent from the registry are pruned; missing required tags or explicit absent item ingredients remain explicit holds. It still only generates dietary nutrition for confirmed food outputs, while preserving nonfood intermediate recipe routes strictly as dependency evidence.
- In the absence of this runtime report, the original source-only tag filter remains available for compiler development; it is NOT an authoritative full food census.

## Safety and remaining QA

Exporter and compiler only **observe** items, tags, recipe previews and native FOOD components. No item property, gamerule, recipe, save file, nutrition, saturation, transformation, path XP or native mod behavior is changed. No new food items, crafted-history components or per-recipe NBT.

Before integrating generated diet data, reconcile the runtime snapshot's actual mod list and current effective KubeJS recipes with the same pinned survey. Recheck unresolved dynamic recipe previews, player-specific food behavior, client server dietary preview parity, native effects/containers, and game behavior separately under owner-authorized runtime QA. Source compilation/unit-test PASS is not a Minecraft boot or gameplay PASS.
