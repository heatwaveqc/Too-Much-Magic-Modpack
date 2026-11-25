# Occultism ink rituals and Dictionary of Spirits source handoff (#336)

**Scope:** selective source integration from immutable donor `898738e93bed74e38cf946975b00c0b38f2c3838` into reviewed `test/full-integration@32f9b91824e0dfdd255eb9f31ecf09b7642561ec`. The five ritual JSONs are unchanged from the donor. The missing book integration is a **new additive** Modonomicon entry with five native `occultism:ritual_recipe` pages under the existing Crafting Rituals category. It is linked to the native Crafting Rituals overview, available after reading the Foliot Craft pentacle chapter; later recipe pages clearly state their higher-tier books and pentacles. No native book/category replacement.

| Ink | Bound book | Native craft pentacle | Inputs | Yield | Duration |
| --- | --- | --- | --- | --- | --- |
| Common | Foliot | Foliot | Purified Ink + Arcane Essence + Copper Ingot | 4 | 60 |
| Uncommon | Foliot | Foliot | 4 Common Ink | 2 | 60 |
| Rare | Djinni | Djinni | 4 Uncommon Ink | 2 | 150 |
| Epic | Afrit | Afrit | 4 Rare Ink | 2 | 300 |
| Legendary | Marid | Marid | 4 Epic Ink | 2 | 510 |

Each ritual consumes its bound book. The durations are Occultism native units (seconds at the standard bowl, subject to native modifiers), not claimed wall-clock observations. The donor uses the actual result ink as the `ritual_dummy`, so JEI output/dummy references do not require a newly registered item. No scripted duplicate recipe is introduced.

## Why JEI is separate

Pinned upstream Occultism **1.227.0**, source commit `990303ea95ba057791b7d407d6cac5151f66d799`, registers its standard JEI ritual category and enumerates `Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(OccultismRecipes.RITUAL_TYPE.get())`. Therefore no custom JEI plugin/registration is authored here. The in-game Modonomicon book **does not automatically import new recipes as entries**, and needs authored pages with exact recipe IDs.

The already observed Philosopher's Stone problem #505 proves why this distinction matters: its `occultism:ritual` recipe was present on the **server-side Experimental census** but remained missing in JEI **even after keybindings were rebound**. The book guide from merged PR #563 has not been client-tested. We must not treat this source/book integration as a solution to the separate JEI client-side discovery issue or assume native recipes must appear.

## Required validation

1. **A (this PR):** Python contracts check exact 5 donor recipes, outputs, names, pentacles, all 5 book links/strings and Packwiz SHA-256 chain; protected source CI.
2. **B in next separately authorized boot/client diagnostic:** Capture *both server and client* recipe-manager IDs `tmm:ritual/occultism_ink_*`; verify Occultism JEI category receives those holders; check R-click output and U-click input **and** search/category without keybind shortcuts; inspect JEI hidden ingredient/blacklist state and client resource logs. Compare with Philosopher's Stone's known registered-server / invisible-client case #505. Do not invent a custom JEI bridge until failure stage is proven.
3. **C next separately authorized game test:** Verify five distinct book recipe pages, correct icon, text and unlock; actual Foliot/Djinni/Afrit/Marid craft pentacles, bound-book consumption, exact yields, normal golden bowl durations and persistence/no accidental dummy extra drop. Verify alternate/native Iron's ink routes and upgrades have no unchecked EMC/ink/scroll arbitrage.
4. **Future paired source:** PR #338 separately owns tier-matched *unbound* book upgrades and multiple other Occultism recipes; its 16 files are not brought in implicitly. Exact pinned native item compatibility must be tested with this ink change. No automatic overwrite of #338.

**No** Integration Assembly, authorized server smoke, Prism package, fresh client, B/C gameplay run or release acceptance occurs here.
