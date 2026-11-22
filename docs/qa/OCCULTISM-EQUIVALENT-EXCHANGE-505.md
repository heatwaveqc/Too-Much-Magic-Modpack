# Equivalent Exchange: Occultism ritual and guide (#505)

## Evidence, 2026-11-22

Experimental installed source `c56fb18ffc3b9f12f27317e7fb37f15c32576a8c` had no visible Philosopher's Stone ritual in **JEI** or Occultism's Dictionary of Spirits. This is a Type C failure, not merely a missing JEI keybinding. The installed registry census recorded 26,569 loaded recipes, but its compressed `docs/qa/EXPERIMENTAL-2026-11-22-RESULTS/live-command-recipe-census.json.gz` in QA PR #504 was not available for direct binary inspection through the GitHub text connector. **Do not claim the exact ritual ID was present or absent from that census** based only on the aggregate number.

Frozen source: `kubejs/data/tmm/recipe/ritual/equivalent_exchange.json` has Occultism recipe serializer `occultism:ritual`, bound Marid book, `occultism:craft_marid`, 12 closed tags containing 40 distinct alternatives, duration 600, one `projecte:philosophers_stone`. Both old ProjectE crafting-grid Stone recipes are denied by `neoforge:false`. The source-level shape is consistent with **exact pinned Occultism 1.227.0**:
- [RitualRecipe.CODEC, pinned source](https://github.com/klikli-dev/occultism/blob/990303ea95ba057791b7d407d6cac5151f66d799/src/main/java/com/klikli_dev/occultism/crafting/recipe/RitualRecipe.java): required fields `ritual_type`, `ritual_dummy`, `pentacle_id`, `activation_item`, `ingredients`; `result` optional; `duration` optional.
- [Native Marid crafting example](https://github.com/klikli-dev/occultism/blob/990303ea95ba057791b7d407d6cac5151f66d799/src/generated/resources/data/occultism/recipe/ritual/craft_dimensional_mineshaft.json): same schema for the bound book and pentacle family, including result and dummy stacks.
- [JEI plugin](https://github.com/klikli-dev/occultism/blob/990303ea95ba057791b7d407d6cac5151f66d799/src/main/java/com/klikli_dev/occultism/integration/jei/impl/JeiPlugin.java): directly enumerates **all loaded** `OccultismRecipes.RITUAL_TYPE` and registers them in the ritual category; its category supports up to 12 bowl positions. **No separate custom JEI bridge** is supported by these findings.
- [Native Modonomicon entry](https://github.com/klikli-dev/occultism/blob/990303ea95ba057791b7d407d6cac5151f66d799/src/generated/resources/data/occultism/modonomicon/books/dictionary_of_spirits/entries/crafting_rituals/craft_dimensional_mineshaft.json): authored book entries explicitly contain `occultism:ritual_recipe` page links. The book does **not** automatically create one page per newly registered ritual.

## Source repair staged in this PR

1. Add a new **additive** Occultism Dictionary of Spirits crafting-ritual entry at
   `kubejs/data/occultism/modonomicon/books/dictionary_of_spirits/entries/crafting_rituals/equivalent_exchange.json`. It links `tmm:ritual/equivalent_exchange`, requires the Marid pentacle guide entry, and points from the existing Master Marid miner sequence; existing native book files are not replaced.
2. Add six translatable labels/instructions to the existing TMM `en_us.json` (not a full native Occultism locale override).
3. Add source tests for exact 12-tag/40-alternative recipe, linked guide page, language keys and both disabled grid recipes. Preserve the original ritual gameplay recipe unchanged: schema inspection has not established a defect that warrants an invented rewrite.
4. Regenerate Packwiz hashes for every changed/added pack-owned file and `index.toml`.

### Registration diagnosis still required before #505 is closed

**A (no new Minecraft session):** On the machine where PR #504's native census artifact is present, inspect for **exact** `tmm:ritual/equivalent_exchange` (including type `occultism:ritual` and output `projecte:philosophers_stone`). Do not use a substring hit in documentation or a compiled static recipe map as proof. If absent, read the same installed world's KubeJS and recipe reload error logs, inspect any unresolvable ingredient tag / missing registry entries and exact compiled active pack/source SHA. This would explain JEI absence; a guide-only fix cannot register a recipe.

**C (owner-authorized next client):** First confirm server recipe exists, then JEI recipe and the new Dictionary of Spirits entry show all twelve offerings correctly. Test native bound-book activation on Marid Craft pentacle and 600-second completion, actual Stone output and consumption; ensure both grid orientations stay absent. Do not close from static CI, guide visibility alone, or a recipe census on the wrong build.

No smoke, Prism package, new server/world or assembled integration runtime was launched for this repair.
