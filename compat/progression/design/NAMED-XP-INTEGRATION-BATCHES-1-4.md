# Named experience systems — integration batches 1–4

Workstream: PR #347, stacked on the permissive donor foundation PR #344. Supersedes the fixed three-channel experiment. PR #340 remains the unified engine architecture. Fresh disposable QA worlds only until the first stable release; no pre-stable save conversion.

## Batch 1 — independent XP registry
- Built-in reserved `tmm_progression:mxp`; all other systems defined in `data/<namespace>/tmm_progression/experience_systems/<name>.json`.
- Named independent balances, rank curves and point grants; optional `requires_path`, system `eligibility` and activity `when` conditions. An activity may be awarded without iterating skills/trees. Tree default `xp_channel`, individual skill override `xp_channel`, independent `level_system` and `required_rank`. Eligibility can also use `{"type":"experience_level","id":"namespace:system","minimum":5}`.
- Existing native Monk/Hunter curve, sources and points extracted into their respective independent definitions; no Gilded or transformation-specific balance decisions introduced.
- Atomic server-owned purchases, exact per-purchase currency receipts, refund independent of subsequent pricing, shared MXP across trees, independent native level gates. Milestone grants currently use the tree default native wallet. Native rank-list parser still has its existing validation limit; no fixed limit on number of named systems.
- Regression suite includes built-in MXP plus four extra native systems and mixed-currency purchasing, cross-system level prerequisites, refunds, MXP rounding/deduplication and invalid references.

## Batch 2 — MXP
Existing first-party Ars Nouveau, Ars 'n Spells, Iron's, Botania Revived and Neo Vitae Java hooks, configurations, math and service ported to `io.github.heatwaveqc.tmm.progression.mxp`. The owned service credits the shared ledger. Old standalone MXP presence suppresses internal registration and awards to avoid double credit. Boss, ritual, craft and committed resource APIs are available. Existing optional Psi/JAWR source settlement not newly activated. Standalone MXP, Tyz, pack manifests and user profiles intentionally remain unchanged pending reviewed cutover. Audit caller imports during cutover; prototype KubeJS pilots are not installed. No Tyz donor source imported (license clarification pending).

## Batch 3 — generic mechanics
- Timed initiation uses configured start/duration and event; the old Monk sunset parameters are preserved in data. Blindness ordeal reads configured effect capability and event identifiers.
- Health-based creature XP, specialty selection, inclusion/exclusion tags and potion XP discovery/repeat rules read computed XP definitions, rather than runtime-selected Hunter paths. Award and dedupe keys are per system.
- Mutation policies are attached to configured paths; progress and unlocked effects now persisted per owner, with ownership and eligibility checked at runtime. If a player has multiple eligible mutation policies, the current ownerless brew refuses consumption rather than choosing silently; explicit item routing remains future work. Existing unauthorized-use penalty is preserved.
- Existing specialized Tyz bridge, specialty convenience commands, some milestone/event/effect specifics, and additional generalized effect handlers remain to be refactored. No new native rituals, trees or balance assumptions beyond preserving existing mechanics.

## Batch 4 — first connected skill-tree interface
Epiphany-inspired graph uses attributed pure TreeLayout plus SkillGraph, explicit prerequisite edges (AND/OR), node state, skill selection, wallet names/balances, level and cost labels, pan/scroll navigation, purchases and confirmed refunds. Server provides tree list and snapshots, authorizes mutations and validates eligibility. No new LDLib2 dependency or donor assets. UI fidelity, small-screen behavior and in-game interactions need client QA.

## Verification and integration boundary
- Commit `ccdd123877d0737ddbe105a1b3bd5269679a9c7c`: Standalone progression core (44383027383), JAWR/MXP contract (44383026656), Verify bootstrap (44383027366) all completed successfully.
- Follow-up `5569fce3d0d8d07bd279ffd1c057022bf9cbd58d` adds XP/source eligibility and owner-keyed mutations; verify CI separately.
- These are source changes, not a pack release. No full game smoke, runtime test, merge, dependency removal or manifest publication. Next integration writer reviews stacked PRs, current CI and handoff, then coordinates disposable-world client/server smoke and cutover once approved. Batch 5 permissive donors/remaining reusable handlers can follow; test old source integrations before replacing them.
