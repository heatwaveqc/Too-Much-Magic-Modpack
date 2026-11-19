# Agricultural, animal and intermediary food survey — first pass

Frozen #348 census: 23681 exact registered items; old installed build f7aeb56a0f7b9c5957f922f5a397037f25207c76, not the upcoming integration runtime. [Offline run 44630751678](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/44630751678): 19 tests and census completed successfully. See batch-1-3-overlay-v1.json for all 346 unique batch entries, and ingredient-equivalence-audit-v1.json for the 11 groups. Reproduce by rerunning existing workflow against same frozen source and rules.

| Review result | Count |
|---|---:|
| Provisional classification | 256 |
| Targeted review | 90 |
| Later batch or nonfood review | 723 |
| No batch evidence (not proof of inedibility) | 22612 |

93 former candidate-only rows have provisional classifications; 45 additional rows surfaced beyond prior evidence screen, including planting/crop ingredients. Counts by batch overlap (129 agricultural, 96 animal, 38 intermediary provisional). 79 historical-only candidates and 11 special-consumer flags remain explicit review items; review individual entries only if needed by a consumer. No recipe trait inheritance, no per-stack provenance or coupling to actual diet eligibility. Stock stays generic. No changes to current active tags or recipes in this pass.

## Ingredient audit

- Tomato/onion/cabbage: both providers exist; Pam versions were absent from the frozen FD common tags. Existing additive bridges are justified. Partial slots 65/70/84 respectively; these are historical observations, not unique recipe counts nor proof a tag bridge fixes every slot.
- Rice: both already share c:crops/rice. No duplicate bridge. 25 partial slots still use narrower alternatives.
- Wheat dough and raw pasta: both exact items present, Pam versions absent from common FD tags; 127 and 28 partial slots respectively. Strong next targets, but check output quantities, source recipes, remainders and EMC implications before an additive bridge. Direct-ID recipes do not automatically broaden.
- Salt and flour: only one declared provider each; 49 and 40 matching slots. No additional equivalence proven. Their common tags were empty/unobserved in frozen census; avoid adding an alias without an actual consumer.
- Milk: all three declared forms are present in common milk tag; 51 partial slots. Do not substitute containers/volumes wholesale.
- Raw fillet: Aquaculture raw fillet omitted from c:foods/raw_fish in this census; fish fillet versus whole catch and species-specific cuts need review. Cooked declared fillets already tagged. Don't rewrite raw material yields or accidentally widen dragon rules.

Pastel exact observed keys: pastel:enchanted_golden_carrot, pastel:enchanted_star_candy, pastel:star_candy. Old spectrum enchanted IDs absent; magical nourishment pass must reconcile exact intended effects (magnificent candy not established by this scan). Historical absent hint pamhc2foodextended:stockitem is not an active tag; pamhc2foodcore:stockitem and farmersdelight:bone_broth classified generic stock. No live activation or readiness claim. Integration writer owns shared metadata and runtime selection.
