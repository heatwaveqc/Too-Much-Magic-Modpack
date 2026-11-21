# Unified XP channel integration — first source pass (2026-11-18)

> **SUPERSEDED DESIGN (owner-approved 2026-11-18):** This document describes the first-pass implementation, not the current target. The three-channel cap, source-tree-owned curves and single-currency-per-tree restriction must be removed before further mechanic ports. See PR #340's named experience systems contract. Retain the named-wallet ledger, existing arithmetic and provider conditions; move earning and curves into independent namespaced XP definition JSON, preserve built-in MXP with its committed resource hooks, and support per-skill currency overrides with named level requirements. No backward migration needed before stable release. Next implementation is the XP registry refactor within this workstream; no new balanced skills, runtime pin changes or full smoke authorized by this documentation edit.

Owner direction: before the first stable release no legacy save migration, including Tyz ranks/points, is required. Use fresh disposable QA worlds with intentionally changed schemas; never automatically delete saves. Current-build persistence across death, login, restart and dimensions remains required. See the policy on PR #340. One engine owns all executable progression functionality; path definitions must remain data-only.

## Implemented
- Shared persisted channel wallets and per-tree purchased skills/milestones in one player attachment. One to three channels, validated together with path bindings; native curve source tree determines its existing XP thresholds, awards and starting points. Other trees referencing the channel spend the same points without awarding the curve a second time. Native Monk/Hunter paths retain their existing data and curves. MXP is the first magic channel; Hunter is the proposed third channel based on existing advancement. New automatic empty Core path is a schema fixture, not completed content. No new balanced skills.
- Existing Track/Ledger/ProgressionMath reused with projected wallet values and automatic skill ranks. Purchase/respec, native XP events, automatic enrollment, shared wallet projection for effects, and server/client snapshots use the unified path/channel API. New ledger serialization intentionally incompatible with old QA worlds.
- MxpMath copied from our own existing module without changing normalized/fixed/ordinary math. Trusted committed-resource and successful-completion APIs write the internal magic wallet, fractional MXP, lifetime levels, recent transaction IDs and first discovery IDs. No duplicate award: these APIs refuse to work while standalone tmm_mxp is loaded. The actual standalone adapter hooks still own cast/ritual/boss events; no source hook moved yet, no Tyz or MXP pins removed. Native existing Tyz bridge remains during the transition. Internal MXP owns its own fresh test wallet, never imports old SP or delivery queues.
- Epiphany-derived condition composition plus typed predicates wired into tree enrollment, purchase, XP events and generic effect dispatch. Path eligibility, skill eligibility and per-effect `when` are data-only. Unknown provider state fails closed and cannot be inverted by NOT. Trusted flag registry permits reusable optional compatibility adapters. Optional read-only Transformations ProbeRegistry bridge exposes current provider flags tyr_progression:jawr_vampire, jawr_werewolf, jawr_lich, goety_lich, dragon; one verified snapshot per player/server tick, cleared on logout. No dragon subtype probes supplied yet. `path_joined` supports native discipline prerequisites. No new transformation trees/rituals enabled.
- Server snapshot now lists actual configured trees and currency ID; existing native screen uses previous/next selectors, not hardcoded two buttons. Epiphany final renderer is a separate port. Existing tree-specific legacy mechanics in Progression are not all gated by the generic runtime or parameterized; treat as remaining refactor. No new class-owned code. No new external libraries.

## Datapack schema
The reserved table is `data/tyr_progression/tyr_progression/paths/_channels.json`. It declares up to three named XP channels; a `path_curve` has a `source_path` (existing source tree owns the rank curve, activities and milestone currency), an `mxp` channel has a growth_factor (default 1.02, existing 1.00–1.20 validation). Exactly one MXP type permitted. Every path declares `xp_channel`; all trees sharing a native curve must use its same thresholds/points-per-rank. Earning is restricted to the source tree so duplicate registrations cannot mint points. A magic tree can specify its own rank ceiling for skill gates. No repeated free starting points by joining further paths. No direct process/game boot or manifest promotion.

Example (illustrative, no active form tree):
```json
{
  "schema": 1,
  "enrollment": "automatic",
  "xp_channel": "tyr_progression:mxp",
  "eligibility": {
    "type": "or",
    "conditions": [
      {"type":"flag","id":"tyr_progression:jawr_lich"},
      {"type":"flag","id":"tyr_progression:goety_lich"}
    ]
  },
  "rank_xp": [0, 1000, 2000],
  "points_per_rank": 0,
  "skills": []
}
```

Tests authored: shared spending, separate native currencies, automatic grants, refund, MXP fractions/level boundaries/first completion/dedup, channel count, missing provider flags. Existing regression tests should run along with these. Source-only verification; compile status from CI is authoritative. Next: compile, move real MXP hook/service registrations and existing config into engine, ensure single award authority, parameterize existing native path-specific enrollment and effects, verify provider subtypes and lifecycle, then data-driven connected Epiphany-style GUI and ten-tree definitions. Keep tests on fresh worlds. Reconcile #340 and #344 plus #341's source and licensing via designated writers; preserve original donor license files. No live-ready claim.
