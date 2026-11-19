# Ten-tree engineering handoff — INACTIVE
Owner-directed player-only skills. These are review data, NOT runtime datapacks. The existing live resource trees are unchanged. INDEX describes original references and first-pass handler coverage; it is not a completion percentage.

176 archived sketch nodes including capstones + 8 native Gilded unlocks + 2 purchased Gilded ranks = 186 concepts. Monk and Hunter existing active native trees are separate; preserve them during eventual merges. Alternative JAWR concepts remain alongside original sketches. One Lich tree.

## Concrete draft candidates
- core-pilot-candidate.json: four exact existing first-party attribute pilot translations (not newly balanced percent growth).
- monk-pilot-candidate.json: 17 proposed owned nodes from original archived rank/cost sketches and current first-party TyzMonkBridge effect formulas. Not a standalone graph: Fortunate Hands, original native references, proposed tags, overlap/stack semantics and exact registry IDs need reconciliation. Candidate list excludes Fortunate Hands pending the existing Treasure Hand Fortune/Silk mode and selector integration. Some candidate effects still have documented semantic differences. Do not enable with legacy Tyz or MXP duplicate awards. The final naked Chaos Guardian benchmark is not validated.
- gilded.json: recovered original isolated native and paid drafts and generic food rite candidate. Verify current hybrid, food, loot and innate behavior policies; original isolated code is not yet integrated into this branch.

## Reusable backing
Named XP systems and skill-level currencies; nonlinear rank prices/values; per-rank native gates; AND/OR prerequisites and purchase predicates; innate and paid ownership; persistent cooldown/toggle/timed activation; irreversible rank purchases with atomic native point credits; generic completed-food rite; native experience gain, glide, backstab, counter, and source-filtered mitigation; titles/descriptions/lanes and GUI actions; configurable bounded virtual loot cap. No equipment-owned powers, detachable traits or sockets. Resource-paid activation requires specific provider integration rather than a fake debit.

Next: approve balance and eligibility, resolve individual unsupported mechanics, complete provider audits in BATCH-5-6-FOLLOWUP.md, resolve scope tags and merge candidates into actual tree definitions, verify source, UI and disposable-world QA. Remain inactive until explicit cutover. No Tyz donor code copied; existing first-party integration formulas only. See IMPLEMENTATION-ROADMAP.md.

## Gilded native candidate (inactive)
`gilded-native-candidate.json` translates the old eight native nodes and two paid nodes plus named XP curve/food rite and archived tags, backed by new player-owned effect hooks and original gold equipment deltas. It is a review bundle, NOT loadable as one resource. Missing loot and beverage tags and native claw/breath, hybrid eligibility/physiology, lava permission and balance gates remain blockers. Do not promote; no code in the design directory. See roadmap and reuse audit for A–C commits and exact source/CI evidence.
