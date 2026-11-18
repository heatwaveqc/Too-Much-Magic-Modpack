# Specialization compatibility matrix — owner decisions, 2026-11-17

[Base matrix (CSV)](SPECIALIZATION-COMPATIBILITY.csv) · [Composite-state extensions (CSV)](SPECIALIZATION-COMPOSITE-TRANSITIONS.csv)

This is the canonical **design decision register**, not a runtime config or a validation certificate. Rows are the specialization(s) already held; columns are the new base specialization to acquire **without giving up any existing one**. Core Magic and Improvable Skills 3 are universal and outside the matrix. Remaining unspecialized is allowed. Monk/Hunter/Gilded are peers to supernatural identities, not extra independent class slots. Dragon Survival's three species are deliberately separate. Returning to human, controlled cures, re-enrollment, advancement within the same path, and changing (rather than stacking) dragon species are outside scope. Named composites are derived identities, not independent join actions; their resulting states appear in the second CSV.

## Status legend

- **DENY** — locked prohibition/invalid duplicate in this design; not a claim all denial paths are live-tested. Same-path acquisition means a second copy, not normal upgrades. Human discipline pairs are denied under the one-specialization default, and simultaneous dragon species are denied. Reverse-order Hunter/Gilded enrollment by liches is denied. Composite states are closed to additional specializations except the explicitly approved second lichdom; no automatic transitive allow.
- **PLANNED** — approved design exception, not yet enforced by integrated runtime code. Do not permit until implemented and verified.
- **IMPLEMENTED** — experimental acquisition/compatibility code exists in the integration source. Does **not** claim compiled JAR present in a particular QA build, client acceptance, or completion of physiology/rendering/cure features. Archlich and Dracolich currently qualify at this source-level scope.
- **REVIEW** — no affirmative exception approved here. Default runtime DENY still applies; this is a design-review queue, not permission. The JAWR dual-affliction guard is an existing narrow exception in source; full Abomination remains research-only and needs explicit rite/identity and policy reconciliation.

## Explicit exceptions

| Identity | Acquisition | Status |
|---|---|---|
| Archlich | JAWR Lich then Goety Lich, or reverse | IMPLEMENTED (experimental source) |
| Dracolich | Any one Cave/Forest/Sea Dragon and exactly one of the two Lich routes, either order | IMPLEMENTED (experimental source; all six pair variants covered by generic acquisition policy, appearance may be partial) |
| Piglich | Gilded **first**, then either native Lich, optionally the other Lich afterward | PLANNED (owner approved) |
| Revenant | Hunter **first**, then either native Lich, optionally the other Lich afterward | PLANNED (owner locked Revenant; interpret as the immediately preceding proposed same acquisition rule as Piglich) |
| Abomination | JAWR Vampire + Werewolf | REVIEW (research and native guard exception only; complete identity still unapproved/unimplemented) |
| Ascendant Dragon | Monk + any dragon | REVIEW (on hold over overlapping direct combat power) |

For Piglich/Revenant, preserve existing path progress, equipment or hunting/mutation abilities; the additional lich form(s) use the shared Tyz SP wallet, no extra hybrid-exclusive tree implied. Lich physiology precedence is proposed, with food/cure/resurrection conflicts to validate before release. Both lich sources may be acquired in either order **after** Gilded/Hunter enrollment. A Dracolich cannot become Archlich; an Archlich cannot acquire a dragon. No other third specialization is automatically allowed. Denied combinations can subsequently be reconsidered only via an explicit owner decision.

## Review counts

10 base alternatives = 45 unordered distinct pairs, 90 directional distinct transitions, plus 10 invalid duplicate diagonal cells. 7 pairs have experimental integrated source, 4 are approved one-way plans, 6 are denied in both directions, 28 await design review (blocked by default). The two CSVs use plain status values so they can be filtered or imported.

## Evidence and next steps

- Integrated source baseline `2f662b9e8b73cee47a1e23bf6d1f3510dd41d8ae`: [NativeSpeciesJoinGuard](../compat/transformations/src/main/java/io/github/heatwaveqc/tmm/transformations/core/NativeSpeciesJoinGuard.java), [TransitionPolicy](../compat/transformations/src/main/java/io/github/heatwaveqc/tmm/transformations/core/TransitionPolicy.java), [FormResolver](../compat/transformations/src/main/java/io/github/heatwaveqc/tmm/transformations/core/FormResolver.java), [integration manifest](../manifest/integration-sources.json).
- The current generic class/species veto denies all new forms for enrolled human disciplines and vice versa. Piglich/Revenant require a narrow, order-aware exception through the shared Progression/transformations architecture. Do not globally relax the veto.
- [JAWR-only Abomination research](JAWR-ONLY-ABOMINATION-FEASIBILITY-2026-11-15.md): dual-affliction guard already allows both forms, but classifier still assumes obsolete four-form Abomination; the later two-form target needs reconciliation. No older four-form ritual is authorized.
- [Dracolich pilot](../compat/transformations/docs/DRACOLICH-PILOT.md) and [Cave/Center rendering pilot](../compat/transformations/docs/DRACOLICH-CAVE-CENTER-PILOT.md): verify both directions for each species and each lich, actual power/appearance/food/death/cure behavior and rejection of the second lich.
- This register supersedes historical matrix/design surveys on combinations only. It does not change gameplay source, quest rewards, mod pins or build authorization. Quick/static and subsequent owner-authorized in-game tests must distinguish eligibility from actual transformation success.
