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

## PR #27 — universal Tyz MXP progression design

- [MXP architecture and progression rules](pr-27-universal-mxp/docs/MAGIC-MXP-PROGRESSION.md)
- [Magic source registry](pr-27-universal-mxp/docs/MAGIC-MXP-SOURCE-REGISTRY.md)
- [Five-mod hook reassessment](pr-27-universal-mxp/docs/MAGIC-MXP-FIVE-MOD-HOOK-REASSESSMENT.md)
- [Core Magic and form tree design](pr-27-universal-mxp/docs/TYZS-CORE-MAGIC-AND-FORM-TREES.md)
- [Purchase and external gate design](pr-27-universal-mxp/docs/TYZS-MXP-EXTERNAL-GATES.md)
- [Historical progression design snapshot](pr-27-universal-mxp/docs/PROGRESSION.md)

**Provenance:** original [PR #27](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/27), immutable source commit [`1d6aa7bcef9301447dd6a4bd6f0f4cc7f413c049`](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/tree/1d6aa7bcef9301447dd6a4bd6f0f4cc7f413c049). Six source-accurate Markdown records. **Important:** `test/full-integration/manifest/integration-sources.json` still has an `mxp-hexcast-contract` source stream referencing this original branch and commit for `integration/hexcast-media-pilot/`. Archiving design documents and closing the PR **does not** authorize deleting that branch until the stream is explicitly reconciled.

## PR #83 — nine-tree Tyz blueprint, affinities and stat-cap research

- [Nine-tree master blueprint](pr-83-tyz-nine-trees/docs/TYZS-NINE-TREE-BLUEPRINT-2026-11-15.md)
- [Machine-readable concept catalog](pr-83-tyz-nine-trees/docs/tyzs/nine-tree-catalog-v1.json)
- [Strength first-pass analysis](pr-83-tyz-nine-trees/docs/TYZS-STRENGTH-AND-NINE-TREE-FIRST-PASS-2026-11-15.md) and [design data](pr-83-tyz-nine-trees/docs/tyzs/nine-tree-strength-first-pass-v1.json)
- [Theoretical stat ceilings](pr-83-tyz-nine-trees/docs/TYZS-ALL-IN-THEORETICAL-CAPS-2026-11-15.md), [attribute cap delta](pr-83-tyz-nine-trees/docs/TYZS-ATTRIBUTEFIX-CAP-DELTA-2026-11-15.md) and [underlying cap data](pr-83-tyz-nine-trees/docs/tyzs/theoretical-attribute-hard-caps-v1.json)
- [Enchanting, Apotheosis, Curios and gear stacking audit](pr-83-tyz-nine-trees/docs/TYZS-ENCHANTING-APOTHEOSIS-CURIOS-STACK-AUDIT-2026-11-15.md), [gear ceiling data](pr-83-tyz-nine-trees/docs/tyzs/gear-enchant-affix-curios-ceiling-audit-v1.json) and [Curios eligibility data](pr-83-tyz-nine-trees/docs/tyzs/curios-all-in-eligibility-v1.json)
- [Noncombat parity and magic affinities](pr-83-tyz-nine-trees/docs/TYZS-NONCOMBAT-PARITY-AND-MAGIC-AFFINITIES-2026-11-15.md), [noncombat sidecar](pr-83-tyz-nine-trees/docs/tyzs/monk-noncombat-parity-v1.json), [affinity sidecar](pr-83-tyz-nine-trees/docs/tyzs/magic-affinity-nature-blood-other-v1.json)
- [Historical progression design snapshot](pr-83-tyz-nine-trees/docs/PROGRESSION.md)
- [Original design validator script](pr-83-tyz-nine-trees/scripts/validate_tyz_strength_design.py), **archived as source, not installed as active tooling**

**Provenance:** original [PR #83](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/83), immutable source commit [`bd1e8f28720fe9986a05f09cb3cc51c697d05090`](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/tree/bd1e8f28720fe9986a05f09cb3cc51c697d05090). Fifteen verbatim design/audit/sidecar files. Nine-tree proposals are **not active Tyz configs**; the obsolete automatic-smoke workflow was deliberately excluded.

## PR #100 — standalone Monk 25-ability classic parity target

- [Human-readable 25-ability implementation target](pr-100-monk-classic/docs/monk/STANDALONE-PRE-TYZS-CLASSIC-TARGET.md)
- [Machine-readable classic crosswalk and acceptance targets](pr-100-monk-classic/docs/monk/standalone-pre-tyz-classic-parity-v1.json)
- [Original validator script](pr-100-monk-classic/scripts/validate_monk_standalone_parity.py), **archived as source, not installed as active tooling**

**Provenance:** original [PR #100](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/100), immutable source commit [`cb3510e7f9b0a32b0d68906ad1c21999b90a565d`](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/tree/cb3510e7f9b0a32b0d68906ad1c21999b90a565d). Three verbatim records. The standalone `compat/progression` engine and existing Monk path content are the current code owners, not this historical proposed design; the old CI workflow was not copied.

## Preservation notes

- The initial PR #48 and #71 collection contains **16** source-accurate files; PRs #27, #83 and #100 add **24** more, each copied from its named immutable source commit **without editing its content**. Each archived path nests its historical repository path under its PR-specific archive.
- We intentionally did **not** copy legacy Java, test executable code, old resource-pack skill registries, or CI workflows. Two historical validator scripts from the design PRs are retained only as inert archived source under `docs/archive/`, not installed in live `scripts/`.
- Archiving a proposal does not mean approving a value, activating an ability, importing a source stream, or confirming Minecraft gameplay.
- Future documentation may summarize or link these records, but this archive should remain a stable snapshot to avoid depending on historical working branches.

## November 19 historical branch consolidation

[Historical branch preservation](historical-branches-2026-11-19/README.md): 35 exact historical files, including quest standards, design status, expanded magic decisions, hybrid research, Gilded designs and Hunter census evidence; two scripts are archived as inert source. This does not activate any work. Exact original commit provenance and retirement conditions are in the linked index.
