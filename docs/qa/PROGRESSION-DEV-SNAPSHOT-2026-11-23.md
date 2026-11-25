# Coherent Progression source promotion — November 23, 2026

## Immutable source and selection boundary

- Source branch: `dev/progression` at **`55dd0c036242f74ffb1354792299da65f0c3c731`**; reviewed, coherent after merged #525, #552, #559 and #574.
- Frozen current pack baseline: `test/full-integration@5af8baa4b58b5aaca2aa20baab3fb8cc299ab159`.
- Comparison of the **entire** `compat/progression/` subtree by Git blob SHA: **514 files** at donor, **490 files** on baseline, **485 byte-identical**, **24 new**, **5 changed**, **0 removed**. Integrate *only* the 29 actual donor-different paths into the current shared source tree. All other Progression files remain byte-identical to current baseline.
- No whole-branch merge of hundreds of obsolete/archived files, no stale `AGENTS.md`, `.github/workflows/full-integration.yml`, manifest/pins, Packwiz index, quest content, food or Path tree snapshots.
- Excludes **unmerged** Progression development PRs #580 (generic XP pure foundation), #584 (four Core nodes), and all stacked forms/food/path work pending the Progression writer's own coherent source merge. The live Core's empty skill list is **not** fixed here.

## Promoted source-level fixes

1. **#506 / PR #525:** opaque Progression skill-screen background and correct inherited render order, without duplicate blur; Java source contract.
2. **#508 / PR #552 and #557 / PR #559:** Hexerei Mixing Cauldron ↔ EvilCraft Blood Extractor source policy, transaction and ordinary-blood normalization with optional Hexerei Mixin config; separate pure policy regression tests. Both patches are included together, not cherry-picked over one another.
3. **#237 / PR #574 (reconciles #464):** first Philosopher's Stone per-player tooltip unlock, Replication first ingot, ProjectE native learned checks, optional Jade ProjectE block tooltip, sync/persistence and tests. Preserve normal wallet/machine values and Jade's optional dependency; compile uses pinned 1.21.1 Jade provider metadata.

All changed module files are exact donor blobs. Integration changes add no new external mod JAR/pin, independent XP wallet, Path enrollment rules or consumable items.

## Evidence / tests

- **Source-only**: compile and run `gradle build` for `compat/progression` plus unit tests; verify optional mixin metadata, Jade native ABI, module identity, integrated baseline configuration and preflight. The pre-existing full-integration quick CI may compile all five selected first-party modules; do not invoke authorized-only assembly, boot or Prism.
- **B/C (next separately authorized client/build)**: Newbie and Normie screen sharp at open/hover/scroll/purchase/reopen; missing Core skill nodes tracked **separately** (#455).
- **B/C (#508)**: require both optional mods loaded; confirm actual Mixin application; test extractor→cauldron→extractor, both cauldron blood identities, 1/249/250/999/1000/1750/2000 mB, both hands, capacity, buckets/pipes, creative/claim boundaries, save/relog, JAWR protected vessels, no fluid loss or duplication.
- **B/C (#237)**: Stone first acquisition and persistence, each player's independent learned EMC/Jade display, pre/post Replica Ingot, no visibility leak on joined clients or creative path; check legitimate machine/wallet values remain.
- Neither code review nor successful source CI counts as any Minecraft gameplay PASS. The first authorized full QA build must include and explicitly record all ready streams or label BLOCKED/OWNER-DEFERRED.

## Follow-through

On successful PR merge, retain the immutable donor SHA above in integration provenance; note actual merge PR/commit and run result. Then, after the Progression writer merges #580/#584 or other coherent work to `dev/progression`, repeat a **fresh** tree comparison rather than replaying an old entire module snapshot.
