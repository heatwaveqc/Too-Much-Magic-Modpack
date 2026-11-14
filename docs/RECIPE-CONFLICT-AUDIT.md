# Too Much Magic — Polymorph Recipe Collision Audit

**Design goal:** Polymorph remains installed as a last-resort crafting UI fallback, but no *unintended* recipe should require Polymorph to select its correct output.

## Reproducible test

In a disposable server world with all pinned mods, all KubeJS scripts, and all Almost Unified / Neo Vitae configs loaded, execute from OP chat:

`/polymorph conflicts`

Or from the dedicated server console (no slash):

`polymorph conflicts`

The command writes `logs/polymorph-conflicts.log` if it finds a conflict. If none, the console should say `Found 0 potential conflicts`, and no report is generated.

The repository's `audit/polymorph-conflicts-2026-11-14` branch has a reproducible dedicated-server workflow: `.github/workflows/polymorph-audit.yml`. Its output is an Actions artifact with the native log and grouped JSON/Markdown reports. The smoke-server runner adds a **temporary opt-in** `SMOKE_POLYMORPH_AUDIT=1` mode; normal server boots remain unchanged.

## How to interpret results

1. **Accidental identical inputs, different products:** Fix via recipe reassignment (changing at least one meaningful material ingredient) or a dedicated alternate recipe. Prefer preserving both products and their intended crafting tiers.
2. **Unification conflicts:** Inspect all effective tags, canonical item outputs, and ore-to-dust / dust-to-ingot / refining routes. Do not suppress necessary mod recipe behavior just to silence Polymorph.
3. **Cosmetic/duplicate outputs:** Same item with alternate recipes is usually not an issue. Check whether outputs truly differ before taking action.
4. **Unintentional progression bypass:** Highest urgency, e.g., access to pre-gate ProjectE, draconic, or custom transformation materials.
5. **Intentional recipe overload:** Explicit design decision and documentation required; still aim for zero in the default crafting table, and retain Polymorph so the fallback works.
6. **Special recipe exclusions:** Polymorph's scanner explicitly skips `CustomRecipe` implementations. Magical altars, ritual systems, machines, and modded custom recipe types are *not* exhaustively scanned; assess these separately by type and gameplay.

Do not infer the amount of game-breaking crafting from the raw conflict count: Polymorph reports **potential overlapping ingredient groups**, sometimes with harmless choices. Its greedy grouping can omit transitive overlaps, and it does not prove all UI stations are supported.

## Full modpack baseline — 2026-11-14

**Native Polymorph scan passed against the complete hash-pinned disposable dedicated server.** Run [43621750797](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43621750797), archived [conflict artifact](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43621750797/artifacts/14123794613).

- **93 potential conflict groups**: 60 crafting, 17 smelting, 14 blasting, 1 smoking, 1 smithing.
- **67** groups contain recipe IDs from multiple namespaces; **26** contain only one namespace.
- **44** custom crafting recipes were explicitly skipped by the native scanner.
- The native report lists **60 crafting groups**, not 60 individual crafting recipes. Group 30 contains **95 special/dynamic recipes** and requires independent verification: this is almost certainly not 95 simultaneous conflicting player crafts.

### Highest-priority verification

| Category | Representative actual recipe IDs | Recommended resolution |
|---|---|---|
| Silver equipment (12 crafting groups involving silver) | `eidolon_repraised:silver_sword`, `embers:silver_sword`, `werewolves:silver_sword`; corresponding axe, pick, shovel, armor | **Do not delete mod-specific functional tools.** Preserve unique items but differentiate ingredients or add deliberate mod-specific recipes. Standardize ingot/dust/block outputs separately |
| Witchcraft blocks (3) | `witchery:iron_witches_oven` vs `enchanted:witch_oven`; `witchery:clay_jar` vs `enchanted:soft_clay_jar`; corresponding fume funnel parts | Give distinct blocks distinct meaningful recipes. Check tier ordering |
| Two magic altars | `hexerei:book_of_shadows_altar` vs `eidolon_repraised:wooden_altar` | Verify ingredient overlap and use distinct spell-system markers |
| Food variants | `pamhc2foodcore:flour_x1_flour_plants` vs `pamhc2foodextended:pistachiobutteritem`, plus cupcakes, cobblers, pies and donuts | Fix accidental food collisions without removing the dishes; prioritize low-cost shapeless overlaps |
| Goety / vanilla blocks | `minecraft:chest` vs `goety:chorus/chorus_chest2`; `minecraft:bookshelf` vs `goety:rotten/rotten_bookshelf2` | Confirm actual ingredient-subset match and preserve special block access |
| Ore and dust smelting | `tmm:iron_dust_smelting` vs `primalmagick:iron_ingot_from_grit_smelting`; `tmm:gold_dust_smelting` vs `neovitae:materials/smelting_gold_dust` (also blasting) | Our broad c:dusts recipes create **4** flagged pairs. Audit whether final outputs/yields equal before deleting or narrowing redundant recipes |
| Silver/lead ore-processing duplicates | `neovitae:materials/smelting_silver_dust` vs `eidolon_repraised:smelt_silver_dust`; Embers vs Werewolves smelting/blasting of raw silver | Evaluate canonical ingot output and furnace yields rather than removing ore variants outright |
| Special-stone smelting | Vanilla versus Deeper & Darker Gloomslate/Sc ulk Stone or Pastel Blackslag ore recipes | Likely same ingots: confirm output identity. Must **not** remove specialized dimension ore drops |
| Smithing | `neovitae:anointment_apply` vs `forbidden_arcanus:smithing/apply_*_modifier` | Tag overlap from broad template/base slots is a possible scanner false positive; perform actual smithing station tests |
| Dynamic / internal patterns | Crafting group 30 has 95 recipe IDs (colors, NBT-dependent repairs, addons); Botania dye group has 16; Extra Botany accessory group has 11 | **Do not bulk-delete.** Inspect actual matched inputs in JEI/in-world before classifying as bugs |

Note: the long 95-recipe dynamic group is **one** Polymorph group. Some intentional alternate recipes share final outputs and are not harmful. A conflict group alone does not establish an exploit, and Polymorph's native wrapper can group recipes based on partial ingredient overlap.

### Next resolution protocol

1. Divide the full native report into identical-output redundancy, genuine distinct-output collisions, special/dynamic false positives, and progression bypasses. **Prefer actual loaded output inspection**, not names alone.
2. Fix first any overlap where two distinct mod-specific tools, altars, magical machines or consumables genuinely use the same input. Keep both functions.
3. Fix repeated ore-to-ingot recipes conservatively: preserve all allowed dust inputs, yields and special resource gates while eliminating duplicate alternative outputs.
4. Re-run **this same native scan** and compare the recipe IDs, rather than changing Polymorph behavior to hide warnings.
5. Finish with client-side JEI and in-world crafting verification, especially custom crafting stations outside Polymorph's scanner.


## Maintenance

- Re-run the native command after each modlist, recipe, or common-tag change.
- Keep a non-destructive record of flagged recipe IDs, owners, chosen fixes, and a baseline comparison.
- Keep Polymorph pinned even if the regression suite reports zero conflicts; do not assume our scanner covers every crafting station.
- Only mark the issue integrated when the final-world client crafting test and normal dedicated-server tests pass.

## Silver equipment consolidation regression — PASS (2026-11-14)

- **Pass**: [run 43629084400](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43629084400), [artifact](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43629084400/artifacts/14127790398) contains raw native `polymorph-conflicts.log`, parsed `recipe-conflicts.json`, readable report, KubeJS logs and equipment-material-family census.
- Before targeted recipe removal: **93 potential Polymorph groups**, including **60 crafting** groups. After KubeJS removal of exactly 14 duplicate *silver-equipment* recipe IDs: **84 total** groups, including **51 crafting** groups. The change is exactly **nine resolved cross-mod equipment-slot conflicts** (five tool categories, four armor categories).
- Regression `scripts/check_silver_conflicts.py` passes: **zero remaining cross-recipe silver-equipment groups**. No other recipe type changed its count: 17 smelting, 14 blasting, one smoking, one smithing.
- Both `silver_equipment_stats.js` startup script and `silver_equipment_unification.js` server script loaded with **zero KubeJS errors**. Existing KubeJS fallback warnings about Psi Tweaks recipes and Malum/Botania spirit repair remain unrelated and need separate review.
- **Still intentionally not resolved** by this equipment pass: raw silver/ore/dust furnace overlaps, silver ingot/block compaction collisions, other magical altar collisions, food collisions, and special/dynamic recipe groups. Do not use the raw 84 number as the count of player-facing bugs.
- Modded-server startup proves registration and recipe scanner behavior. Client JEI, damage and mining speed checks are still required.

### Other equipment materials — census of the actual pin set

A separate scan of 128 pinned server-side JAR files indexed conventional `<material>_<equipment_slot>` item models and shaped/shapeless crafting outputs, then grouped names across mods. **Silver is the only cross-provider generic equipment family detected**: all nine slots from Embers/Eidolon/Werewolves. No cross-provider generic lead, copper, iron, gold, bronze, steel, pewter, electrum, netherite or diamond family was detected in this scan.

**Scope caution:** source recipe/model names are not a complete runtime registry; the scanner does not know supernatural effects, tag overrides, specialized recipe managers or uniquely named magical gear. Existing *material* overlaps remain significant: lead ore and ingots (Embers/Eidolon), silver metal forms (four providers), sulfur dust, quartz variants, copper nugget-to-ingot conversions, and iron/gold dust smelting. Those need material-level canonical decisions, not the unified generic silver-tool treatment.

