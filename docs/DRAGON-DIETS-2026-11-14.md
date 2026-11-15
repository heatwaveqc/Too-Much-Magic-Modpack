# Dragon Survival food compatibility — first implementation (2026-11-14)

**Status:** source-authored, untested in a NeoForge/Prism client. No stable release approval.
**Target:** Minecraft 1.21.1 / NeoForge / Dragon Survival **2.0.74** (desired in Monk stream; do not assert installed until packwiz-pinned).
**Source of upstream diet:** [Dragon Survival 1.21.1 diet_entries.json](https://github.com/DragonSurvivalTeam/DragonSurvival/blob/1.21.1/src/generated/resources/data/dragonsurvival/data_maps/dragonsurvival/dragon_species/diet_entries.json), blob `435b6c7132ca4eb1af302dcbb5988ad2ca2217d0`.
**Source of Aquaculture raw fish tag:** [raw_fish.json](https://github.com/TeamMetallurgy/Aquaculture/blob/master/src/main/resources/data/c/tags/item/foods/raw_fish.json).
**Source of Aquaculture sushi recipe:** [sushi.json](https://github.com/TeamMetallurgy/Aquaculture/blob/master/src/main/resources/data/aquaculture/recipe/sushi.json): one `aquaculture:fish_fillet_raw` + one `minecraft:seagrass`; **no rice**.

## Binding rules

1. All dragons: **NO cereal grains, rice, bread, pasta, grain-based desserts, or ordinary cooked food**. Exceptions are Dragon Survival's purpose-built charred/cultivation items and explicitly authorized non-grain fruit preserves. Do not add all of `#c:foods/cooked_fish`, `#c:foods/cooked_meat`, `#c:foods/candy`, or `#c:foods/vegetable` without filtering.
2. **Sea Dragon:** raw fish, raw fish portions/fillets, seaweed/kelp/seagrass and directly derived foods. `aquaculture:sushi` is ALLOWED; rice sushi is not. `aquaculture:fish_fillet_cooked` and all regular cooked fish are NOT Sea Dragon foods. Additional `minecraft:seagrass`, `minecraft:dried_kelp`, `aquaculture:minnow` and `aquaculture:jellyfish` are explicitly edible.
3. **Forest Dragon:** raw fruit, vegetables (including Farmer's Delight cabbage, leaves, tomatoes, onions), berries, mushrooms, nuts, sugar/sugar cane, simple fruit/vegetable salads and stand-alone jellies, preserves, juices/smoothies. Raw Pam orchard fruit/nut item selectors included; `#c:foods/fruit` and `#c:foods/vegetable` are expanded through KubeJS and filtered to avoid grains. No toast/jelly sandwiches, pies, pastries, grain salads, or normal cooked dishes.
4. **Cave Dragon:** retain Dragon Survival's cave-specific foods and native second-cook routes (`#c:foods/cooked_meat` -> Charred Meat; `#c:foods/cooked_fish` -> Charred Seafood). A new **raw meat/fish + one coal or charcoal** shapeless fallback provides a charring path even when a mod lacks cooked intermediates. This avoids conflicting with native cooking/smoking recipes and deliberately consumes extra fuel.

A KubeJS `ServerEvents.tags` handler materializes Forest produce into `#tmm:dragon_food/forest_produce`. It uses the resolved common fruit/vegetable tags and verified vanilla/Farmer's Delight items, and excludes grain-like, cooked, and seaweed IDs. This does **not** alter the global `c` tags or normal human food values. Pam preserves are matched with Dragon Survival's **native regex selector** support. No extra compatibility Java mod is required for this first pass.

## Dragon-specific food properties

**These are `FoodProperties` saturation modifiers, not flat saturation points.**
Preserve every unchanged upstream entry, including special effects and native item values.

| Dragon | Diet entry | Nutrition | Saturation modifier |
| --- | --- | ---: | ---: |
| Sea | `#c:foods/raw_fish` | 1 | 1.0 |
| Sea | `aquaculture:fish_fillet_raw` | 2 | 2.0 |
| Sea | `aquaculture:sushi` (seagrass) | 6 | 5.0 |
| Sea | seagrass / dried kelp / additional small aquatic items | 1 | 1.0 |
| Forest | `#tmm:dragon_food/forest_produce` | 3 | 2.0 |
| Forest | Pam jellies, jams, other simple preserves | 4 | 3.0 |
| Forest | Pam juices and smoothies | 3 | 2.0 |
| Forest | vanilla sugar | 1 | 1.0 |
| Forest | sweet berries (native override) | 2 | 2.0 |
| Cave | `dragonsurvival:charred_meat` (native override) | 6 | 3.0 |
| Cave | `dragonsurvival:charred_seafood` (native override) | 7 | 11.0 |

Golden Apple / Enchanted Golden Apple / Golden Carrot are allowed with native food properties and `retain_effects`. Native Forest Dragon chorus fruit and sweet-berry values remain protected from the generic produce category.

Removed upstream conditional entries that violate the policy: cake/creative blaze cake and ordinary nether skewer/cooked magmacube fish for Cave; turtle soup/foul soup/chorus bread for Forest; raw tofu imitation-fish, flour-battered salmon tempura and conventional seafood soups/skewers for Sea. Most of these mods are not installed; this protects the pack if they are added later. Native special Dragon Survival foods are retained.

## Files

- `kubejs/data/dragonsurvival/data_maps/dragonsurvival/dragon_species/diet_entries.json`: complete explicit pack override of the 1.21.1 upstream data map, with upstream values preserved except documented removals. **Important:** it is a *same-path* override, not an automatically merged additive map. It uses `"replace": true`; resynchronize against the actual shipped 2.0.74 jar before promoting a Candidate and whenever Dragon Survival updates.
- `kubejs/server_scripts/dragon_diets.js`: server-side filtered plant tags and fallback charring recipes. Guards on `Platform.isLoaded('dragonsurvival')`.
- `scripts/verify_dragon_diets.py`: static shape/food rule tests. Run `python scripts/verify_dragon_diets.py`.

## Disposable-world QA required before integration acceptance

1. Confirm Dragon Survival **is actually loaded** and `/reload` has no JSON/data-map errors or KubeJS script errors. Test dedicated server and graphical Prism client separately.
2. Forest: verify vanilla carrot, apple, pumpkin, Farmer's Delight cabbage, tomato, onion, Pam's apple/cherry, jams, juices and sugar are edible. Check tag expansion for **every Pam crop**; note and explicitly add untagged raw produce. Check raw walnuts, chestnuts and acorns, but exclude roasted versions.
3. Grain negatives: test rice, raw wheat, barley, amaranth, corn, bread, toast, jelly toast, fruit pie, pasta, and rice sushi. None should provide normal dragon nutrition.
4. Sea: verify Aquaculture Atlantic Cod, Tuna, Halibut, Rainbow Trout, raw fillet, vanilla kelp, seagrass, dried kelp, and Aquaculture sushi. Ensure Cooked Cod, Cooked Salmon and Cooked Fillet are not accepted. Check small fish/fillet classes have expected values.
5. Cave: `raw cod + charcoal` and `raw beef + charcoal` produce Charred Seafood/Meat. Try Aquaculture raw fish, raw fillets, and Farmer's Delight raw meat cuts. Native `c:foods/cooked_fish` second-cooking still produces Charred Seafood; ordinary cooked fish is not an edible Cave meal.
6. Verify special effect retention, sat modifiers and partial-stack consumption, including containers; regression-test no accidental ordinary cooked-food status.
7. Compare **the pinned 2.0.74 jar's** bundled data map and registry with this upstream-source snapshot. The source branch may advance independently; do not claim byte-for-byte version match without checking.

**Known limitation:** recipe-derived classification is not performed automatically for all ~900 Pam foods. Regex selectors cover straightforward named preserves and drinks, while other composite foods need recipe-source review. Static tests cannot establish live tag membership. Additional raw produce whose source mod omits common tags must be added after a live registry audit. The design is intentionally conservative where ingredient provenance is ambiguous.
