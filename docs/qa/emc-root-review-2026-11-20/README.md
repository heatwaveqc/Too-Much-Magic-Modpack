# EMC root and mapper review queue (issue #326)

## Result

A complete read-only per-item inventory is available in the frozen installed-world survey at [`../surveys-2026-11-18/items.csv`](../surveys-2026-11-18/items.csv). This supplement reproducibly summarizes its default-stack EMC coverage and splits every unpriced non-exception item into the survey's two observed review queues. It adds **no EMC values, exclusions, recipe changes, or mapper settings**.

| Observed queue | Items | What the evidence means |
| --- | ---: | --- |
| Positive default-stack EMC | 5,780 | ProjectE API returned a positive value for the default stack in the captured build. Eight values exceed JavaScript's exact integer range; use a native `long` export before exact price comparisons. |
| Approved zero-value glass-pane exceptions | 17 | The complete explicit exception list in `manifest/emc-coverage-policy.json`; no additional exception is inferred. |
| Recipe-output investigation | 9,430 | A generic RecipeManager output reference was observed for the item, but the default stack still had zero EMC. This does not prove that its mapper failed or that the recipe's full costs are represented. |
| No generic recipe output observed | 8,454 | The survey did not see a generic recipe output. This does not prove the item is a true economic root; loot, mob drops, world generation, custom producers, state and eligibility still need review. |
| **Unpriced non-exception items** | **17,884** | Every captured item other than the 17 approved exceptions is accounted for in one of the two queues above. |

The per-namespace counts are in [`namespace-summary.csv`](namespace-summary.csv); exact totals and SHA-256 provenance are in [`summary.json`](summary.json). Run `python scripts/build_emc_root_review.py` to reproduce both from the published per-item census.

## Largest investigation queues by namespace

| Namespace | Recipe outputs observed, unpriced | No generic output observed |
| --- | ---: | ---: |
| Rechiseled | 0 | 3,627 |
| Pam's Food Extended | 783 | 2 |
| Pastel | 733 | 321 |
| Botania | 591 | 76 |
| Goety | 587 | 337 |
| Primal Magick | 586 | 89 |
| Eternal Starlight | 513 | 351 |
| Neo Vitae | 423 | 113 |
| BetterEnd | 329 | 173 |
| Twilight Forest | 0 | 252 |
| Alex's Caves | 259 | 236 |
| Occultism | 255 | 208 |
| Malum | 240 | 193 |
| Theurgy | 243 | 130 |

Rechiseled's 3,627 entries dominate the no-generic-recipe queue and should be triaged as a family before anyone considers manual anchors. Namespace counts are evidence for prioritization, not provider identity, root status, or an approved exception.

## Classification boundary and remaining work

The raw census distinguishes default-stack API value from explicit stored-entry presence and includes observed tags, generic recipe-output IDs and static loot-resource references. The new summary deliberately leaves these determinations unresolved per item:

- true economic roots versus recipe derivatives and rootless cycles;
- exact provider/pin ownership for each registry namespace;
- conditional/reachable/renewable loot or production and acquisition yields;
- full ingredient alternatives, input counts, catalysts, returned containers and resource costs;
- mapper blacklist or exception attribution, including component-sensitive behavior;
- the eight values beyond JavaScript safe-integer precision.

The source survey explicitly marks 754 recipe ingredient slots as truncated at 128 alternatives, with original alternative counts retained. Static loot references are not resolved loot simulation; 39 selected resource files had strict-JSON parse errors, and resource-pack precedence/custom handlers remain incomplete. Therefore this is a complete coverage inventory for the **captured default stacks**, plus a reproducible decision queue; it is not a complete root-price recommendation or proof that all values are safe.

The capture is from installed integration `f7aeb56a0f7b9c5957f922f5a397037f25207c76` (baseline `a81723733b4437770f61f566afe583343ccccff9`), not the moving Nov 20 branch. Use this evidence for triage, then refresh the census if a later authorized build changes pins or recipe/mapping inputs. No game launch, assembly or new QA build was performed by this audit.
