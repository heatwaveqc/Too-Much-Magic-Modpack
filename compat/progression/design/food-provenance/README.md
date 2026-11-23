# Tag-first food provenance: first real runtime coverage (#444)

## Source-only status
This is a **real, generated, bounded** `FoodSourceEvidence` data slice stacked on #539 (`FoodPolicyRegistry`), not a manually priced diet matrix or a claim of complete coverage. The runtime rules remain configurable in #519: raw/cooked/charred 1/0.5/0.25; nonpreferred share 0.5; whole preferred source 6 nutrition and 2.0 saturation modifier. Source receipts only specify item, family, preparation, and equivalent whole-source units.

## Actual data output
- `compat/progression/design/food-provenance/source-receipts-v1.json` records pinned-JAR SHA1 receipts, source-native tag family groups, known direct whole foods, four exact Farmer's Delight cutting ratios, the Dragon Survival charred-meat stage, and explicit unresolved holds.
- `scripts/build_food_source_evidence.py` produces one `data/tyr_progression/tyr_progression/food_sources/<namespace>/<item>.json` per settled source and three interoperable `data/tmm/tags/item/foods/{raw,cooked,charred}.json` tags, all inside the Progression JAR's own resources. No separate path-owned food Java, no global FoodProperties rewrite.
- Tree and Crops produce items come from the **actual pinned source JAR** `c:fruits` and `c:vegetables` tags, verified 2026-11-22. Aquaculture fish come from `c:foods/raw_fish`. Tag membership establishes source class, **not registered edibility**. The native Dragon Survival hook only changes component-backed foods, retaining effects and containers. Nonedible entries cannot become edible through the provenance file.
- Vanilla raw/cooked roots, the already approved raw Aquaculture fillet, Farmer's Delight cabbage/tomato/onion, and seven inspected Huaxia raw roots use source/registry receipts. Huaxia is an approved **not-yet-installed** candidate, so its tag entries are explicitly `required:false` and its source records are dormant until installed.
- Native cutting `cod`, `salmon` and `cabbage` produce two half-source slices; native minced beef has 2/3 whole-source units **per** one of two outputs (8 total from beef 6). The output count is verified against Farmer's Delight 1.21 generated JSON and the frozen 2026-11-22 pinned source JAR. This deliberately is not a naive `1/output_count` rule.
- `dragonsurvival:charred_meat` has 1 cooked-meat input to 1 charred output in its pinned native smelting/campfire/smoking recipes; stage is charred. No additional cooking tier.

## Source provenance and remaining holds
Pinned SHA1 receipts: Pam Crops `2a735b24c61a0007fe98de6cc2dc5d5fff4915a2`, Pam Trees `85d54e1d4d32f669d3f183945dc813360263ca91`, Aquaculture `972b0ab7f0f07f4dbcb00af80c1addf5325d0683`, Farmer's Delight `27675ddfcb8cc37e6ea1ab6c15051c7949d94657`, Dragon Survival `e11429dda4688711e174625bb7420873a255a715`. Huaxia source upload SHA1 `8c4eb34973a6f2ca5d0aac29e1bdff065bfc6919`, publisher digest independently unconfirmed.

The recipe-output source artifacts enumerate 2,819 recipe files across twenty target JARs, but most do **not** expose all ingredient quantities and alternatives. This implementation deliberately leaves ambiguous `aquaculture:sushi`, suspicious stew, Pam orange juice, mixed Huaxia dishes and alternate oil/wrapper recipes **unmapped**. Do not infer an extra whole source unit or assign a mixed preferred-share value until effective recipe graph evidence exists. Dynamic KubeJS recipe replacement, effective merged tags, client previews, native-only consumables, a same-build edible registry denominator, and diet-map cutover remain separate slices.

## Reproduction and integration
    python scripts/build_food_source_evidence.py --receipts compat/progression/design/food-provenance/source-receipts-v1.json --output-root compat/progression/src/main/resources
    python -m unittest tests.test_food_source_evidence -v

Keep these resources with the #539 Progression food registry and exact source dependencies. The designated Progression writer reviews/promotes to `dev/progression`, then the pack integration writer stages coherent source+resource updates with any Packwiz changes. No `test/full-integration` overwrite or full pack build is authorized here. For owner Type B/C, verify real FoodProperties in the exact installed roster, effective tags, raw fruit/veg/fish/meat, cuts, cooked and charred stages, `/reload`, one-per-player dietary scoping, no duplicate magical food bonuses and preserved native effects/containers.
