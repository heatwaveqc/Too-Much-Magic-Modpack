# Tyr Progression legacy-runtime retirement — 2026-11-21

Status: isolated source cleanup stacked on the rename [PR #424](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/424), **not** an integrated/assembled/live-tested pack.

## Implemented, code-ready
- Reject an installed `tmm_mxp` or `tyzs_skills` JAR explicitly in the single `tyr_progression` constructor, instead of silently suspending MXP awards.
- Remove the dormant, Tyz-dependent 37-skill prefab registration/listeners, Tyz Monk event bridge, JitPack Maven repository and Tyz 6.4.2 compile dependency from the unified JAR. Native server-owned Monk/Hunter and JAWR/Dragon adapters remain.
- Retain the unified `mxp` implementation and `MxpMod.active()` internal API for existing hooks; its value is now always true after successful engine startup, never a second mod.
- Rename old `/tmmmxp` diagnostic surface to `/tyrmxp` and move synthetic event IDs from `tmm_mxp:diagnostic` to `tyr_progression:diagnostic`. Diagnostic award commands remain operator-only.
- Retain historical `compat/mxp`, Tyz overlay and old experiments as source-only provenance **until** separate cleanup and manifest ownership reconciliation. They must never be compiled, pinned, staged or shipped as live runtime artifacts.

## Integration-owned outstanding slices
1. Reconcile [#376](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/376) (retire Tyz/standalone MXP in Packwiz + first-party selection) **and** [#424](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/424) before promoting this stacked cleanup to `dev/progression` and then `test/full-integration`. This branch is based on exact rename head `a97f875cf21a813d1d65d5f26e127bfd1c2c41c3`.
2. Confirm `manifest/first-party-runtime.json` selects only `tyr_progression` for experience, not `tmm_mxp`; `manifest/mods.json` disables `tyzs-skills`; and Packwiz `index.toml` selects neither Tyz nor retired `tmm-mxp`. The rename-only base branch predates the cutover and **still contains Tyz in its pack index and legacy assembly path**; do not publish it alone.
3. Update cross-project `manifest/integration-sources.json` and archived source notes so legacy Tyz/MXP streams are explicitly provenance-only, not future runtime donors; preserve immutable donor SHA and existing retirement rules. The cutover PR retains old wording/paths and workflows still py_compile old activation utilities.
4. Reconcile #379/#382/#418/#393/#427 against new packages and namespaces. No blind wholesale directory replacement.
5. Run Module CI, root/pack validation and read-only Integration Preflight on the combined exact head. No full assembly, server smoke or Prism package is authorized by this change.

## Gameplay QA queue (B/C, awaiting owner-authorized build)
- Verify only `tyr_progression` loads as the unified runtime; `tyzs_skills`/`tmm_mxp` absent; installing an old JAR fails clearly rather than silently changing awards.
- Personal MXP paid-resource debit, boss and ritual first/repeat credit, point balance, respec, persistence after relog/death and two-player isolation.
- Monk/Hunter acquisition, skill purchases and representative effects; JAWR provider eligibility after cure; no Tyz GUI/points/dependency.
- `/tyrprogress` and operator-only `/tyrmxp` diagnostics; no old `/tmmprogress` or `/tmmmxp` aliases.

**Design blocker:** none in this deletion/retirement slice. Optional form skill designs remain separate content decisions. **Quick checks:** pending Actions on this PR; no gameplay claims.
