# Initial charred/ingredient survey — 2026-11-19

Owner correction: Cave Dragons eat native charred food and coal, not ordinary raw meat/fish. No missing generic raw selector fix is needed. Forest native raw terrestrial meat is intentional; maintain existing exceptions and no ordinary cooked/grain bypass. Sea raw aquatic food and rice-free preparations remain distinct.

## Checked source
Dragon Survival 1.21.1 upstream commit c1c6b7b99f6ed097a35fc98c9b99bd16993af941 reports 2.0.75 while our diet script refers to 2.0.74; source-family evidence only, not exact installed binary. Four native smelting recipes (50 ticks, 0.35 XP each), plus smoking and campfire variants, consume respective tags:
- charred_meat: c:foods/cooked_meat
- charred_seafood: c:foods/cooked_fish
- charred_vegetable: dragonsurvival:cooked_vegetables
- charred_mushroom: dragonsurvival:cooked_mushrooms

Native dragonsurvival:charred_food includes charged_coal and charged_soup; our tag excludes both deliberately. CFB 1.21.1 registry loads SMELTING and recognizes outputs with FOOD component, foods or ingredients tag. Add four outputs via #tmm:food/charred to its foods tag; leave native processing/timing intact. No new recipes. CFB visibility and oven routing require in-game verification, including one-stage versus two-stage.

39 distinct installed-namespace source input candidates in charred-survey-v1.json (not installed ID validation; existing grain-filter and container/composition review flagged). Other namespaces omitted from this narrow screen, not declared absent. Existing KubeJS dragon_diets.js filters grain terms from native vegetable/fungus tags. Do not change that script or add raw, mixed magical dishes, containers, reusable food, or rice/grain inputs blindly. Note: existing stew/bucket/etc. native inputs deserve review for lost containers; no override here.

## First cross-mod survey
- Aquaculture 1.21.1 family source: common raw-fish tag has 27 whole catches; cooked-fish tag contains fish_fillet_cooked. Native sushi is fish_fillet_raw + seagrass, not rice. Test fillet and all catches separately; raw fillet is not in this particular upstream raw-fish tag file, so inspect resolved tags and avoid assuming coverage. Cooked fillet already has a native route to charred seafood.
- Farmer's Delight 1.21 source: cooked_meat links cooked_chicken/pork/beef/mutton; cooked_fish links cooked_cod/salmon; inspect effective tags for rabbit and cutting-derived items before adding missing members. Common tomato/onion/cabbage tags already bridged in this PR. Native pot stays native, manual cutting stays manual.
- Pam crops/trees/core/extended: native Dragon Survival explicitly names a number of roasted/baked crops and food-core/extended inputs; those are seed candidates, not proof of real IDs or suitable ingredients. Audit legacy tags, same-ingredient recipes, prepared foods, tofu vs animal meat, grain, and component/remainder behavior. CFB already names Pam's utensils and intermediate ingredients.
- Brewin' and Chewin', Cocktails Delight, Beachparty: drinks and food need actual registry and native-use census, no automatic fermentation or brewing. HerbalBrews approval exists but no pin in this branch; don't treat it as installed.
- Magic and exploration mods: review independent special nourishment, blood, hazardous food, alcohol and placeable feasts; never infer permissive species diets from common tags. No transformation or progression behavior changed.

Full frozen #348 census is still inaccessible as a single file through connector. Existing local census processing script is ready for an offline run; need actual item IDs, resolved tag memberships, recipe/input/output counts and native food/use properties. No new boot, full smoke, assembly, or Prism package authorized. This narrow source pass does not declare live-test readiness.
