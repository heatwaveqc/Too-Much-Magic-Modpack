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

## Current run evidence

See `.github/workflows/polymorph-audit.yml` and generated Actions audit artifact. First full scan is on the material-unification snapshot. **Actual recipe groups must come from the live scan, not guesses.**

## Maintenance

- Re-run the native command after each modlist, recipe, or common-tag change.
- Keep a non-destructive record of flagged recipe IDs, owners, chosen fixes, and a baseline comparison.
- Keep Polymorph pinned even if the regression suite reports zero conflicts; do not assume our scanner covers every crafting station.
- Only mark the issue integrated when the final-world client crafting test and normal dedicated-server tests pass.
