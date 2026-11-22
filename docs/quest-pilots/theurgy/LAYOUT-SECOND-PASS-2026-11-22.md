# Theurgy quest layout — second source pass (2026-11-22)

**Target:** `test/full-integration` source, Minecraft 1.21.1 NeoForge, Theurgy 1.79.0. **Scope:** presentation and transitively redundant dependency strokes only. Source/static A testing is allowed; **no** unrequested Integration Assembly, full server smoke, Prism artifact, or gameplay test is performed.

## Why the first arrangement was hard to read

The 20-node source had the right lesson families, but its optional Niter, Flux, Divination, heat, and Mercurial logistics branches sat on opposite sides of a small core, with arrows spanning most of the chapter. A graph-line schematic of the exact first-pass node coordinates exposed **12 proper dependency crossings** and a **24.70-coordinate** single arrow. The validator checked art-versus-node intersection but not edges running across one another or through nodes.

## Layout v2

- **Main horizontal alchemy route:** Hermetica → Brazier → distillation/salts/Sal Ammoniac/sulfur → Incubator → first full material production → second material. Retain all original 9 core lesson nodes and source-verified actual prerequisites.
- **Upper-left:** two independent Divination rod tasks, branching from the guide. **Upper-right:** item then fluid Mercurial logistics, originating from first successful alchemy. Heat substitution sits near its actual Mercury Flux prerequisite.
- **Lower band:** Fermentation → Digestion → Exaltation, feeding the advanced Reformation pair and the separate rare-material sample-gating explanation.
- **Art:** relocate original TMM banners/panels into explicit framing positions outside both node bounding regions and connector strokes. No ATM texture/art copied, no additional texture bytes required.
- **Visual simplification:** remove six arrows that were *already implied by other prerequisite paths*. Example: TH-12 requires TH-07, which itself requires TH-02; a second direct TH-02→TH-12 arrow adds visual clutter but no real unlocking condition. Preserve the complete transitive ancestor set of **every** node; the Type-A test compares against the first-pass dependency map.
- **Stable data:** retain all 20 quest IDs, existing orientation ID, task/reward IDs, XP and item reward contents, criteria, descriptions, optional flags, and native mod/gating requirements. Do not introduce quests or bypasses.

| Offline geometry check | First pass | Revised |
| --- | ---: | ---: |
| Quest nodes | 20 | 20 |
| Direct visible dependency arrows | 30 | 24 |
| Proper non-endpoint arrow crossings | 12 | **1** |
| Total straight connector distance | 247.53 | **138.39** |
| Longest straight connector | 24.70 | **14.42** |
| Arrow passing dangerously near another node | Not guarded | **Guarded (min 1.10)** |
| Decorative panel overlapping a quest or arrow | Checked for quest only | **Checked for both** |

One arrow crossing remains near the salts-to-Digestion and sulfur-to-Incubator paths. This is preferable to a contrived alternative that has zero line crossings **but sends a connector directly through another quest node**. FTB connectors are straight in this chapter; do not invent a waypoint item merely to hide a crossing.

## Data integrity and verification

The source manifest is `manifest/theurgy_quest_pilot.json`, with reproducible renderer `scripts/build_theurgy_quest_pilot.py`. The source and updated positions flow into **both** `docs/quest-pilots/theurgy/generated/theurgy.snbt` and the live `config/ftbquests/quests/chapters/theurgy.snbt`, the derived `manifest/quest_layouts.json`, and the SHA256-chained `index.toml` / `pack.toml`.

`tests/test_theurgy_layout_a.py` verifies all effective predecessor sets, source geometry, generated/live byte equivalence, source-derived positions, and Packwiz hashes. The generator also rejects newly introduced excessive crossing, node-near-line, image-near-line, backwards, or overly long dependencies. Run **source only**:

```bash
python scripts/build_theurgy_quest_pilot.py --check
python -m unittest discover -s tests -p test_theurgy_layout_a.py -v
```

**Type-C / future owner-authorized client acceptance:** confirm readable in-game at normal and reduced zoom, centered entry point and intuitive side lanes, only the intended single visible crossing, no obscure line under image art, all item glyphs and custom quest art loaded, no changed quest reward claims, and stable completion/rejoin. This document and the schematic are **not** claims of an actual FTB client screenshot.

## Follow-up content-level issue, intentionally not smuggled into a visual-only PR

The currently loaded authored 9-node core ends with a **second-material Incubation lesson (TH-08)**, while sophisticated Niter/Reformation and rare sample-gated replication remain optional. The owner has since locked the broader **Mod-Focused chapter capstone** rule: finish the **mainline** on the hardest meaningful mod-native craft/ritual or deliberate TMM crossover. That needs a later research/authoring decision about the specific Theurgy final *operation* and honest FTB completion detector; **TH-19 is only a manual explanation of protected-material duplication**, not proof of producing an endgame item. Do not silently turn optional Niter/Reformation tasks into fictitious native prerequisites or call a knowledge check a completed ritual. Keep the geometry improvement independent and flag the remaining capstone-content follow-up.
