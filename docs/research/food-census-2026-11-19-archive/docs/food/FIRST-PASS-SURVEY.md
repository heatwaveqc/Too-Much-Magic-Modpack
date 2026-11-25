# Complete first-pass food registry screen

Source snapshot 4dea85e9771baffc8931264f61e754a2f8a5658d; historical frozen #348 census, not current or upcoming pack contents. [Successful offline workflow](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/44632389542) passed 32 regression tests and screened all 23681 IDs. Every earlier ID/flag is retained (346 earlier batch entries, zero prior food candidates lost). Initial broad screening is complete, **not an exhaustive proven edible list or activated policy**.

## Exact-ID results

| Status | Records |
|---|---:|
| Provisional classification | 523 |
| Generic candidates (review only if a consumer needs specificity) | 1250 |
| Targeted review | 185 |
| Nonfood or planting review | 193 |
| Total surfaced records | 2151 |
| No food evidence; not proof of inedibility | 21530 |

Newly surfaced 1127 mixed-quality candidates include producer crafting outputs, not 1127 proven foods. Eight compact JSONL shards in first-pass-v1/index.json preserve all exact-ID rows, status, tentative traits, flags, batches and frozen provenance. Full regenerated all-item evidence JSONL remains an offline workflow artifact. Batch counts overlap: 436 prepared meals or meal candidates, 218 drinks, 36 special, plus agriculture, animal products and intermediates.

## Review priorities (next pass, not new restrictions)

- Alcohol: 58 provisional alcohol candidates; 10 uncertain drinks (native effect review flags total 68). Exact Brewin' and Chewin' and Cocktails Delight drink IDs supported by observed registrations, with native effect/use validation pending. Beachparty cocktails, kombucha, glittering grenadine, mermaid lemonade and mulled cider remain explicitly uncertain. No recipe, fermentation or name is decisive. Root beer and bales are no longer inferred alcoholic; pool noodle excluded from meals. No production or intoxication changes.
- Special: 36 flags, including JAWR's component-sensitive blood bottle, magical rechargeable/reusable food, dragon foods, golden foods and mutagen brews. Keep the native cost, effects and state gates. Pastel magnificent_star_candy absent from this frozen registry; do not guess replacement. Actual Pastel enchanted golden carrot and enchanted star candy were observed; the older spectrum keys remain historical absences.
- 79 historical-candidate-only flags from agriculture/animal batches; inspect only as needed for consumer eligibility. Two grain-free seafood flags (sushi/seasoned fish).
- 1250 generic flags do not create a blanket manual-review obligation; generic stock/soup/mixed meals stay item-ID scoped. Nonfood/planting records stay distinct; no automatic inheritance or edible proof.
- Preserve first 11 equivalence-group audit and pending quantities/EMC review. No consumer tags generated from provisional records. Existing explicit charred, Gilded and vegetable bridges remain as previously authored. Need native FOOD/default properties/use and provider state validation before runtime permissions. Integration writer owns shared packwiz and runtime selection; no launch, assembly, package or readiness declaration.

First-pass classification is the review baseline, not an instruction to rewrite recipes or per-item histories.
