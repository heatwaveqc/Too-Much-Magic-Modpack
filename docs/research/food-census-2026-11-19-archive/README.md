# Historical food census archive (November 19, 2026)

Historical research only, never current runtime policy. Original donor [#368](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/368), exact head `cc19584e99438ffc22b7d80d47f84f383a051e13`. The 16 approved kitchen tag files are preserved in [#676](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/676) at `5a18827c16996a5e5d4081280354de2bcfcfdefb`; the other 37 files are preserved byte-for-byte below. Historical scripts, tests, workflow and four proposed runtime tags are inert .txt copies; no active code, CI, Packwiz or dietary settings changed. Original relative paths in historical documents refer to the original donor checkout, or this archive prefix.

## Evidence
Frozen #348 census `1ae3aa4569294b8904e6bf156a73b779758832f3` from installed build `f7aeb56a0f7b9c5957f922f5a397037f25207c76`: 23,681 item IDs, 24,857 recipes, 2,151 food candidates (523 provisional, 1,250 generic, 185 targeted, 193 nonfood/planting), 21,530 without food evidence; no FOOD properties. Eight JSONL shards and eleven ingredient groups. Prior 346 entries preserved. Historical runs 44630751678 (19 tests), 44632389542 (32), 44634610019 (41), final #368 four workflows passed. No new runtime or reproduction claim.

## Disposition and ownership
- Kitchen #367/#676: approved 16 tags, no dependency on full FOOD census for source integration. Loaded tag/recipe and storage B/C still pending. Skillet deferred; prefab owner playtest.
- Food #444: newer recipe-provenance stack #590 uses pinned census #551 (20 JARs, 2,819 native recipe JSONs). #600 implements actual loaded default-stack FOOD and recipe-result previews, not all dynamic assemble outputs. Operator invokes `/tyrprogress admin foodcensus` on an authorized candidate with exporter installed, yielding `world/reports/tmm-food-registry-v1.json`. Current live export pending; compile actual resolved tags/registry via #590/#600, #618 effective recipe snapshots, #620 compatibility. Do not substitute old snapshot for current edible denominator. Designated Progression writer and integration writer own inclusion; operator runs export; food writer consumes it.
- Old Cave/Forest diets, charred/Gilded and CFB charred tags superseded or not approved for current runtime. Never restore them. Historical Pastel namespace, special blood, optional IDs and native use observations remain source leads for #444/#450, not new policy. No unresolved unique kitchen code. No full build or QA authorized.

## Residual exact blob crosswalk

- `.github/workflows/food-registry-offline.yml` -> `docs/research/food-census-2026-11-19-archive/.github/workflows/food-registry-offline.yml.txt`, `400660755a5556858cb81817e83abb24bd18b535`
- `docs/UNIFIED-KITCHEN.md` -> `docs/research/food-census-2026-11-19-archive/docs/UNIFIED-KITCHEN.md`, `7d04be913bcaa541d78c1e62e22d8363d56686f6`
- `docs/food/BATCH-1-3-SURVEY.md` -> `docs/research/food-census-2026-11-19-archive/docs/food/BATCH-1-3-SURVEY.md`, `4396bdb44a5f15d5fbfbb6d944370230c9c1e284`
- `docs/food/CHARRED-FOOD-SURVEY.md` -> `docs/research/food-census-2026-11-19-archive/docs/food/CHARRED-FOOD-SURVEY.md`, `f8ff1d4aed1dfbb1e0c9bbdb9e275761b9e18e6a`
- `docs/food/CONSUMER-EDGE-CASES-2026-11-19.md` -> `docs/research/food-census-2026-11-19-archive/docs/food/CONSUMER-EDGE-CASES-2026-11-19.md`, `844f5a1db6163904a24446c64d27899fa2546369`
- `docs/food/FIRST-PASS-SURVEY.md` -> `docs/research/food-census-2026-11-19-archive/docs/food/FIRST-PASS-SURVEY.md`, `e83dda89984718c6415145e5bead25e7479a0d27`
- `docs/food/README.md` -> `docs/research/food-census-2026-11-19-archive/docs/food/README.md`, `a8370d78018539e287afef0fef0d4c94478d0e55`
- `docs/food/batch-1-3-overlay-v1.json` -> `docs/research/food-census-2026-11-19-archive/docs/food/batch-1-3-overlay-v1.json`, `10385efa1266e8d2133abae680ea9c80d0da0af9`
- `docs/food/batch-1-3-rules-v1.json` -> `docs/research/food-census-2026-11-19-archive/docs/food/batch-1-3-rules-v1.json`, `c9e4e427b23d793cbeb14960384fee4390b63f62`
- `docs/food/batch-4-6-rules-v1.json` -> `docs/research/food-census-2026-11-19-archive/docs/food/batch-4-6-rules-v1.json`, `2296017f2f72d1ad4037621c8f605265dd8a5743`
- `docs/food/charred-survey-v1.json` -> `docs/research/food-census-2026-11-19-archive/docs/food/charred-survey-v1.json`, `a60010788460ba880e498ea4f2a35cdd8f8b4c3b`
- `docs/food/consumer-edge-case-audit-v1.json` -> `docs/research/food-census-2026-11-19-archive/docs/food/consumer-edge-case-audit-v1.json`, `3d5df66cc85de17cb97859226ba1e32946ca621c`
- `docs/food/dragon-selector-observations.json` -> `docs/research/food-census-2026-11-19-archive/docs/food/dragon-selector-observations.json`, `e73e4333d9061b031494353b599542779c9e722f`
- `docs/food/first-pass-v1/index.json` -> `docs/research/food-census-2026-11-19-archive/docs/food/first-pass-v1/index.json`, `b7b3ad12664e5dac69544d5566e32991cea5c7a7`
- `docs/food/first-pass-v1/part-01.jsonl` -> `docs/research/food-census-2026-11-19-archive/docs/food/first-pass-v1/part-01.jsonl`, `af71949eca0cd0ac07ac81e929acad49c20a48bd`
- `docs/food/first-pass-v1/part-02.jsonl` -> `docs/research/food-census-2026-11-19-archive/docs/food/first-pass-v1/part-02.jsonl`, `c02881dc5075dafcf0d41f80ff528e6e3f88ddb2`
- `docs/food/first-pass-v1/part-03.jsonl` -> `docs/research/food-census-2026-11-19-archive/docs/food/first-pass-v1/part-03.jsonl`, `281a103481c3ecf7e595fee0267ad4711cc97f62`
- `docs/food/first-pass-v1/part-04.jsonl` -> `docs/research/food-census-2026-11-19-archive/docs/food/first-pass-v1/part-04.jsonl`, `90bc9582763463deef087d02cafeeeecdd361fc8`
- `docs/food/first-pass-v1/part-05.jsonl` -> `docs/research/food-census-2026-11-19-archive/docs/food/first-pass-v1/part-05.jsonl`, `fd041aac7eacea7e87814092a407d29c2c104903`
- `docs/food/first-pass-v1/part-06.jsonl` -> `docs/research/food-census-2026-11-19-archive/docs/food/first-pass-v1/part-06.jsonl`, `b09bee4dbf741de2f9c71a8bbcf789260ce6c79c`
- `docs/food/first-pass-v1/part-07.jsonl` -> `docs/research/food-census-2026-11-19-archive/docs/food/first-pass-v1/part-07.jsonl`, `9ecc3a768ce6e7a5553de32f0d7c11470427a2de`
- `docs/food/first-pass-v1/part-08.jsonl` -> `docs/research/food-census-2026-11-19-archive/docs/food/first-pass-v1/part-08.jsonl`, `411344411ca0e4da744699d35c6c5495674bc92e`
- `docs/food/food-registry-seed-v1.json` -> `docs/research/food-census-2026-11-19-archive/docs/food/food-registry-seed-v1.json`, `98784cef8063514938dea9fde3e337fe09f84834`
- `docs/food/ingredient-equivalence-audit-v1.json` -> `docs/research/food-census-2026-11-19-archive/docs/food/ingredient-equivalence-audit-v1.json`, `fa80fb8893ed75ae6abcea86366b1d5595a7d60f`
- `kubejs/data/cookingforblockheads/tags/block/kitchen_connectors.json` -> `docs/research/food-census-2026-11-19-archive/kubejs/data/cookingforblockheads/tags/block/kitchen_connectors.json.txt`, `6aa5150421ae7a607bb210838548c46529e4c659`
- `kubejs/data/cookingforblockheads/tags/item/foods.json` -> `docs/research/food-census-2026-11-19-archive/kubejs/data/cookingforblockheads/tags/item/foods.json.txt`, `8f1e499ec0e6dbed37562af06fb7aee94268b73a`
- `kubejs/data/tmm/tags/item/food/charred.json` -> `docs/research/food-census-2026-11-19-archive/kubejs/data/tmm/tags/item/food/charred.json.txt`, `fa1c274c994b20f328a0c524730a83ee1e340b97`
- `kubejs/data/tmm/tags/item/food/gilded.json` -> `docs/research/food-census-2026-11-19-archive/kubejs/data/tmm/tags/item/food/gilded.json.txt`, `c749fe960465166c1e0303584e2754303a8b8d61`
- `scripts/audit_food_consumers.py` -> `docs/research/food-census-2026-11-19-archive/scripts/audit_food_consumers.py.txt`, `079cfaee367b3c0ecdce8af748f8921dcc6036e9`
- `scripts/audit_gilded_foods.py` -> `docs/research/food-census-2026-11-19-archive/scripts/audit_gilded_foods.py.txt`, `9608693b0e7b8818f5307ae63cff0947f6a3fbaf`
- `scripts/build_food_review.py` -> `docs/research/food-census-2026-11-19-archive/scripts/build_food_review.py.txt`, `58aa55cdbf0938dd58c4a827057b9b45c9169506`
- `scripts/classify_food_batches.py` -> `docs/research/food-census-2026-11-19-archive/scripts/classify_food_batches.py.txt`, `31300ba2d8c4c3e608e6520e18868d7dd238fe3d`
- `scripts/complete_food_first_pass.py` -> `docs/research/food-census-2026-11-19-archive/scripts/complete_food_first_pass.py.txt`, `a49821fa3387c72ab3767ef6acdddbb4ca21763b`
- `tests/test_food_batches.py` -> `docs/research/food-census-2026-11-19-archive/tests/test_food_batches.py.txt`, `9e6a335374e0cd2b2bc079e70809015ee7552a1c`
- `tests/test_food_consumers.py` -> `docs/research/food-census-2026-11-19-archive/tests/test_food_consumers.py.txt`, `53345ef03f545df65b1ed16fed4657d2f57576e2`
- `tests/test_food_first_pass.py` -> `docs/research/food-census-2026-11-19-archive/tests/test_food_first_pass.py.txt`, `136ba2279dedccb84bf1556b8d177dcc10bf3d0b`
- `tests/test_food_review.py` -> `docs/research/food-census-2026-11-19-archive/tests/test_food_review.py.txt`, `b44b2ac3d49f74995410d05744f9c12aa62e8eb5`

Base `332602a6d1a1819b20def4e4824ca20bfe47a3cc`; evidence only. Close #368 after byte-exact preservation and live dependency checks; retain original refs. No branch deletion.
