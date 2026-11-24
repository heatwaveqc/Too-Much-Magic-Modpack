# Unified Kitchen — approved source extraction (#367)

2026-11-24. Exact donor [#368](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/368) at `cc19584e99438ffc22b7d80d47f84f383a051e13`; clean base `a81867efc442f717b45da98c7b1b50af47f697fe`. Existing Occult Cooking pin 1.0.1 already indexed. Scope: additive ingredient compatibility and native CFB providers only, no new mod, recipe, diet, food component, storage automation, or processing bypass.

## Included

- `kubejs/data/c/tags/item/crops/cabbage.json` — exact donor blob `b965c882ebd2e6db11181bb9804796718d4a72f1`.
- `kubejs/data/c/tags/item/crops/onion.json` — exact donor blob `180c50980d0495bbd3d879d8668428c72f8bb73a`.
- `kubejs/data/c/tags/item/foods/onion.json` — exact donor blob `180c50980d0495bbd3d879d8668428c72f8bb73a`.
- `kubejs/data/c/tags/item/crops/tomato.json` — exact donor blob `0ca2377f9b31c94cfa6e23223c53c6c1f97739aa`.
- `kubejs/data/c/tags/item/foods/tomato.json` — exact donor blob `0ca2377f9b31c94cfa6e23223c53c6c1f97739aa`.
- `kubejs/data/c/tags/item/dough.json` — exact donor blob `3572d95ff97d8f4667aa87a747a82077519e8659`.
- `kubejs/data/c/tags/item/foods/dough/wheat.json` — exact donor blob `3572d95ff97d8f4667aa87a747a82077519e8659`.
- `kubejs/data/c/tags/item/drinks/milk.json` — exact donor blob `ddfa3b292fb6773c32d9a73af594847d3857eb04`.
- `kubejs/data/c/tags/item/foods/milk.json` — exact donor blob `ddfa3b292fb6773c32d9a73af594847d3857eb04`.
- `kubejs/data/c/tags/item/milk.json` — exact donor blob `ddfa3b292fb6773c32d9a73af594847d3857eb04`.
- `kubejs/data/c/tags/item/foods/cabbage.json` — exact donor blob `a7a0876c7efe1aa434b890ad10d9b25d6c8cc203`.
- `kubejs/data/c/tags/item/foods/pasta.json` — exact donor blob `0f0c25793a3113f106033dedcc4b2b550f12448d`.
- `kubejs/data/c/tags/item/pasta.json` — exact donor blob `0f0c25793a3113f106033dedcc4b2b550f12448d`.
- `kubejs/data/c/tags/item/foods/raw_fish.json` — exact donor blob `3d459e728c9a82ab782de9f87d095f57f21e5852`.
- `kubejs/data/cookingforblockheads/tags/block/kitchen_item_providers.json` — exact donor blob `50a23e7b9d162f87f616491901786d62ffd31862`.
- `kubejs/data/cookingforblockheads/tags/item/ingredients.json` — exact donor blob `8f5a09225d55c879f32bee097d1c7eef68cae204`.

All 14 common ingredient JSONs and two CFB tag files are exact donor blobs. Tomato/onion/cabbage, wheat dough, pasta, four milk/soy items, and only raw Aquaculture fillet added to generic raw fish; no change to yields, container volume or native safe/species tags. Native cabinet block tag and vanilla barrel are ordinary storage providers. Dough/pasta recognition is additive. CFB and Occult Cooking operate through native interfaces. Historical exact item/tag evidence: [ingredient audit](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/cc19584e99438ffc22b7d80d47f84f383a051e13/docs/food/ingredient-equivalence-audit-v1.json) (historical installed census, not today's loaded snapshot). FD source and exact Aquaculture 2.7.22 source are documented in the donor; Pam short-tag recipe spellings have published source evidence but exact pinned JAR not inspected. Optional entries fail safely if absent; effective substitutions remain acceptance cases.

## Deliberately excluded

Deferred skillet connector/adapter, old CFB charred-food recognition, legacy charred/Gilded diet tags, old survey generator/tests and historical diet docs. Their immutable source and original PR remain reachable; food research/engine is separately owned by #444. No runtime food map, processing or recipe is restored. Prefab kitchen is owner playtest, not a coding blocker. No unrelated manifest or pins replaced.

## Acceptance (not run)

A: exact donor blobs, additive JSON, index/pack SHA-256 chain, normal repository CI/preflight. B/C in next separately authorized candidate: resolved tags and actual Pam/FD recipes with both variants, four dairy/soy items and native remainder behavior, raw fillet meal and species restrictions unchanged; CFB cabinet/barrel, disconnected storage, local and linked Occult Cooking pantry, permissions, simultaneous consumers, conservation, full/unloaded storage, manual cutting/fermentation/specialty stations. No skillet automation or prefab acceptance asserted. No assembly/boot/Prism authorized.
