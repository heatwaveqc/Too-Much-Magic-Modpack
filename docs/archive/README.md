# Historical design and research archive

This directory preserves superseded and exploratory work for research, attribution, and future implementation planning. **Nothing here is authoritative for currently installed mods, shipped Java, active recipes, skill trees, acceptance tests, or integration/packwiz manifests.** These are verbatim copies from historical commits; original internal links and references may point to the old branches.

For current engineering rules use [repository AGENTS.md](../../AGENTS.md). For the current Minecraft modpack implementation, source manifests, and testing status use [`test/full-integration`](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/tree/test/full-integration); for the coherent shared progression engine use [`dev/progression`](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/tree/dev/progression). Archive proposals do **not** override later decisions, especially single-code-owner Progression and JAWR-only transformations.

## PR #48 — MXP research and 42-system implementation targets

- [42-system MXP source implementation target roster](pr-48-mxp/docs/MXP-ALL-MAGIC-SOURCE-IMPLEMENTATION-TARGETS.md)
- [Ritual and completion reward proposal](pr-48-mxp/docs/MXP-RITUAL-COMPLETION-REWARDS-PROPOSAL.md)
- [Historical progression decisions snapshot](pr-48-mxp/docs/PROGRESSION.md)
- [Historical MXP dispatcher README](pr-48-mxp/compat/mxp/README.md)

**Provenance:** closed, unmerged [PR #48](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/48), exact source commit [`ad9c519029e2aa3653f885ea788e84ed0df5a76d`](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/tree/ad9c519029e2aa3653f885ea788e84ed0df5a76d). Four files archived; research/proposed payouts, not independently implemented runtime. The original active MXP branch is **not** activated by this record.

## PR #71 — legacy Monk source audit and design blueprints

- [Monk candidate skill catalog](pr-71-legacy-monk/docs/MONK-SKILL-CATALOG.md)
- [Monk skill-placement crosswalk](pr-71-legacy-monk/docs/MONK-SKILL-PLACEMENT-NO-PUFFERFISH.md)
- [Historical standalone/faction Monk README](pr-71-legacy-monk/compat/monk/README.md)
- [Historical QA handoff](pr-71-legacy-monk/compat/monk/NEXT-QA-2026-11-15.md)
- [Design blueprint and source inventory](pr-71-legacy-monk/compat/monk/design/README.md)
- [Legacy design JSON and non-runtime previews](pr-71-legacy-monk/compat/monk/design/)
- [Legacy 270-candidate placement profile](pr-71-legacy-monk/compat/monk/profiles/skill-placement-no-pufferfish-v2.json)

**Provenance:** closed, unmerged [PR #71](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/71), exact source commit [`67d974bc8f52588c94bb4f8349e3a019a5880392`](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/tree/67d974bc8f52588c94bb4f8349e3a019a5880392). Twelve files archived, including designs and preview JSON. Do not restore the old `compat/monk` Java or the Vampirism-backed skill-tree resources. The current architecture requires all executable behavior to live in `compat/progression`, with Monk as declarative configuration.

## Preservation notes

- These 16 files were copied from the immutable source revisions **without editing their content**, at paths corresponding to their original repository locations under each PR archive.
- We intentionally did **not** copy legacy Java, test executable code, old resource-pack skill registries, or CI workflows.
- Archiving a proposal does not mean approving a value, activating an ability, importing a source stream, or confirming Minecraft gameplay.
- Future documentation may summarize or link these records, but this archive should remain a stable snapshot to avoid depending on historical working branches.
