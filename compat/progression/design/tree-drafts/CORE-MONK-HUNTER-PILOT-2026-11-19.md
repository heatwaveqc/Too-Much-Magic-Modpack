# Core / Monk / Hunter skill-tree pilot — 2026-11-19

**Status: config-only, committed source candidate; requires PR review and native gameplay QA.**
Frozen base: `dev/progression` at `1063355f7ba920e113baae38ce4a6f03ba2c8e44`. No Java, KubeJS, XP code, packwiz, default mod list, or client packaging changes.
Owner request: exercise the existing Unified Progression engine with a small, representative cross-tree pilot. Source-only work is **not** a full build authorization.

## What this PR enables

| Tree | Node | Change | Wallet / gate | At rank one |
| --- | --- | --- | --- | --- |
| Core | Reservoir I | New | MXP, 1 point/rank | +20 Ars mana, +20 Iron's mana, +990 Psi maximum |
| Core | Wellspring I | New | MXP, 1 point/rank | +0.495 Ars regen, +0.198 Iron's regen, +4.95 Psi regen, +0.495 Aureal regen |
| Core | Thrift I | New | MXP, 1 point/rank | +0.0594 Goety discount, +5.94 Neo Vitae cost reduction (native value); payment semantics remain QA-gated |
| Core | Potency I | New | MXP, 1 point/rank | +0.99 Ars spell damage, +0.198 Iron's spell power, +20 Goety potency, +0.198 Eidolon potency |
| Monk | Empty-Hand Force | New | MXP points, Monk level 1 | +0.35 attack damage while truly empty-handed and unarmored; stacks with existing native Iron Palm |
| Monk | Flowing Counter | New | MXP points, Monk level 10 and Force rank 1 | +0.4 bounded counter damage (4 cap, 40-tick cooldown) while unarmed and unarmored |
| Monk | Natural Poise | New | MXP points, Monk level 1 | +0.4 personal armor while unarmored, beyond existing native Natural Armor |
| Hunter | Undead Evasion | Existing control, presentation only | Hunter points, Hunter level 2 | Existing 4% per rank vs tagged undead; not a second copy |
| Hunter | Undead Specialty | Existing control, presentation only | Existing free mutually exclusive specialty | Existing Hunter native category selection and XP behavior |

Core rank-5 values, Monk rank costs/caps, and all native handler effect values match the archived draft candidates; no new percentage curve or max-power rebalance was approved. Higher ranks and eligibility are defined in the active path JSON. No hidden startup grants, no alternate wallets. Monk and Hunter baseline skills, native XP curves, ritual enrollment, and Hunter specialty choice group are left unchanged.

## Configuration and semantics

- Runtime data: `compat/progression/src/main/resources/data/tmm_progression/tmm_progression/paths/{core,monk,hunter}.json`; these are active path resources, unlike `design/tree-drafts/*`.
- Core skill currency is shared `tmm_progression:mxp`. New Monk skills use MXP points and `tmm_progression:monk` as their level gate; native Monk mastery is not replaced.
- Monk empty-hand / no-armor eligibility gates purchasing and active effects. Verify no bonus while holding an item, wearing armor, switching equipment, respawning or dimension-hopping; an existing purchase remains owned.
- Counter is the shared committed-hurt handler, not a new Monk parry implementation. Verify actual hit attribution, target scope, chance, upper damage and cooldown.
- Core attributes were translated directly from the historical 4-node candidate. Native registration, in-world magnitude, spell-provider payments, and Thrift discounts have **not** yet been validated.
- Hunter specimens are existing source nodes, used to test the tree UI, named native Hunter currency, exclusive choice and prey-tag routing. No duplicate Hunter perk was added.

## Lightweight source acceptance / QA handoff

1. Validate all three changed path JSON files parse; all node IDs are distinct within each tree; rank cost arrays match ranks; `rank_values` length matches `max_rank`; every prerequisite points to an existing same-tree node. Core 4, Monk 33 (30 existing + 3 pilot), Hunter 31 unchanged.
2. Run repository/config validation, Progression module compilation and unit tests, and Integration Preflight where applicable. Record actual CI outcomes; no boot, assembly, or Prism build without separate approval.
3. On a fresh, disposable world **after** Unified Progression cutover (#376) merges, inspect the Progression GUI and confirm Core has 4 visible paid nodes, Monk retains original 30 plus 3, Hunter retains 31. Buy Core ranks and check MXP wallet/point debit across all four; verify rank-1 and rank-5 measured values and refund/respec behavior.
4. Complete First Sunset on a Monk QA character; check native mastery vs MXP are distinct. Check level 1 Force/Poise, then level 10 Counter prerequisite before/after Force purchase; check empty-hands/unarmored predicates both on purchase and effect time.
5. Complete Hunter potion initiation on a separate character; select Undead Specialty and test exclusive second-choice denial, undead kill XP and no unrelated family bonus; buy Undead Evasion and verify only tagged-undead mitigation.
6. Test `/reload`, relog, death/respawn, equipment swaps, dimension changes and multiplayer isolation. On Core, compare actual Ars/Iron's/Psi/Goety/Neo/Eidolon results to native provider effects; note absent providers and avoid interpreting unloaded attributes as passing.
7. Negative safety: legacy Tyz/standalone MXP must not simultaneously award the same skill; fail-closed registry IDs/invalid configs; no save conversion. Native cost discounts require a successful paid action, retain exact minimum positive cost and capped savings, and never yield extra MXP or refunded resources.

## Dependencies, release boundary and handoff

- Base is coherent `dev/progression`; PR targets `dev/progression` and must be integrated by the designated Progression writer. It should not merge or edit `test/full-integration` directly.
- [Unified Progression cutover PR #376](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/376) must be reconciled by the pack integration writer before any live pilot. It removes retired Tyz and external MXP and currently ships Core with zero nodes.
- Missing/conditional native attributes, exact post-discount payments, Monk shared-counter event timing, the final Monk/Chaos Guardian benchmark and multiplayer behavior are **runtime acceptance holds**, not claims of implementation completeness.
- Promotion: freeze the accepted config SHA, review the three exact JSON files and re-run source/CI checks against the newest integration candidate. **No Integration Assembly, Server Boot Smoke, Prism QA Package, or gameplay test was authorized by this config-only request.**
