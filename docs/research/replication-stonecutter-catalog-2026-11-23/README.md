# Replication stonecutter exclusion catalog — 2026-11-23

Issue [#448](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/448) requires every effective stonecutting output to be excluded from Replication with its three native prevention tags. This Type-A slice makes the item set reproducible from an actual `RecipeManager` capture instead of guessing from recipe names or maintaining a hand-picked exploit list.

## Exact source and result

- PR base: `test/full-integration` at `c11a95fc14955b3e646885d37e33636b9e37ddf1`.
- Frozen live capture provenance: installed integration `f7aeb56a0f7b9c5957f922f5a397037f25207c76`, published from capture commit `fa869c43a685241d4d9b48e7d2b89d019c321856`.
- Input: `docs/qa/surveys-2026-11-18/runtime-capture.json.gz`, SHA-256 `193076c93bb39a31ee082961d53cbaaa25d9702918e9c8721508bbd82b4586a4`.
- Observed recipes: 24,857 total; 2,503 effective `minecraft:stonecutting` recipes.
- Unique stonecutting output IDs: **1,720**.
- Outputs with another observed recipe: **1,545**. They remain in scope because the owner explicitly selected all stonecutter outputs, even when crafting or another process also produces the item.
- Outputs covered by an explicit native `replication:matter_value` item/tag anchor: **4** — polished andesite, polished deepslate, polished diorite and polished granite. Native tag exclusions alone cannot be assumed to suppress those anchors or already learned printing.

`stonecutter-outputs.csv` records the effective stonecutting recipe IDs, alternate-recipe presence and explicit-anchor overlap for every output. `proposed-native-tag-values.json` supplies the same sorted 1,720 IDs for each approved native tag:

- `replication:cant_be_scanned`
- `replication:cant_be_disintegrated`
- `replication:skip_calculation`

The proposed values are review input, **not installed runtime data**. This research branch intentionally does not write the shared KubeJS data tree, Packwiz index, manifests or integration source locks owned by the designated integration writer.

## Reproduction

```bash
python scripts/build_replication_stonecutter_catalog.py \
  --capture docs/qa/surveys-2026-11-18/runtime-capture.json.gz \
  --output docs/research/replication-stonecutter-catalog-2026-11-23
python -m unittest tests.test_replication_stonecutter_catalog -v
```

The generator reads recipe `type` and output IDs from the live capture. It does not infer stonecutting from an ID substring; that avoids both false positives and recipes whose IDs omit “stonecutting.” Explicit matter anchors are resolved from the observed Replication recipe IDs, including tag anchors expanded against captured item tags.

## Remaining limits and next implementation step

This catalog is exact for the cited November 18 installed snapshot, not the newer November 22 roster. A fresh owner-authorized gameplay session is **not** required to review the generator, but the integration writer must regenerate from a current effective recipe capture before installing the three tag files. Newly added mods or datapacks can add outputs.

The runtime implementation must also remove or override the four explicit anchors and enforce non-printability for previously learned patterns; Replication 1.2.7 has no native `cant_be_replicated` tag. Static catalog checks cannot prove datapack priority, RecipeManager contents on a later build, or scanning/disintegration/printing behavior. Those remain source integration plus authorized B/C acceptance tasks. No Integration Assembly, server/client boot, Prism package or gameplay test was run, and no gameplay acceptance is claimed.
