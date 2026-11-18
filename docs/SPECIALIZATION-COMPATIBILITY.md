# Specialization compatibility matrix — owner decisions, 2026-11-17

[Base matrix (CSV)](SPECIALIZATION-COMPATIBILITY.csv) · [Composite-state extensions (CSV)](SPECIALIZATION-COMPOSITE-TRANSITIONS.csv)

This is the canonical **design decision register**, not a runtime config or a validation certificate. Rows are the specialization(s) already held; columns are the new base specialization to acquire **without giving up any existing one**. Core Magic and Improvable Skills 3 are universal and outside the matrix. Remaining unspecialized is allowed. Monk/Hunter/Gilded are peers to supernatural identities, not extra independent class slots. Dragon Survival's three species are deliberately separate. Returning to human, controlled cures, re-enrollment, advancement within the same path, and changing (rather than stacking) dragon species are outside scope. Named composites are derived identities, not independent join actions; their resulting states appear in the second CSV.

## Status legend

- **DENY** — locked prohibition/invalid duplicate in this design; not a claim all denial paths are live-tested. Same-path acquisition means a second copy, not normal upgrades. Human discipline pairs are denied under the one-specialization default, and simultaneous dragon species are denied. Reverse-order Hunter/Gilded enrollment by liches is denied. Composite states are closed to additional specializations except the explicitly approved second lichdom; no automatic transitive allow.
- **PLANNED** — approved design exception, not yet enforced by integrated runtime code. Do not permit until implemented and verified.
- **IMPLEMENTED** — experimental acquisition/compatibility code exists in the integration source. Does **not** claim compiled JAR present in a particular QA build, client acceptance, or completion of physiology/rendering/cure features. Archlich and Dracolich currently qualify at this source-level scope.
- **REVIEW** — no affirmative exception approved here. Default runtime DENY still applies; this is a design-review queue, not permission. The JAWR dual-affliction guard is an existing narrow exception in source; the approved Abomination still needs a complete acquisition/identity and policy implementation.

## Explicit exceptions

| Identity | Acquisition | Status |
|---|---|---|
| Archlich | JAWR Lich then Goety Lich, or reverse | IMPLEMENTED (experimental source) |
| Dracolich | Any one Cave/Forest/Sea Dragon and exactly one of the two Lich routes, either order | IMPLEMENTED (experimental source; all six pair variants covered by generic acquisition policy, appearance may be partial) |
| Piglich | Gilded **first**, then either native Lich, optionally the other Lich afterward | PLANNED (owner approved) |
| Revenant | Hunter **first**, then either native Lich, optionally the other Lich afterward | PLANNED (owner locked Revenant; interpret as the immediately preceding proposed same acquisition rule as Piglich) |
| Golden Dragon | Gilded + Cave Dragon, either order; no additional specialization | PLANNED (owner approved) |
| Blood Immortal | Monk first, then successful native JAWR Lilith ritual; reverse order denied | PLANNED (owner approved, 2026-11-18) |
| Abomination | JAWR Vampire + Werewolf, either order | PLANNED (owner approved 2026-11-18; complete hybrid pending) |

For Piglich/Revenant, preserve existing path progress, equipment or hunting/mutation abilities; the additional lich form(s) use the shared Tyz SP wallet, no extra hybrid-exclusive tree implied. Lich physiology precedence is proposed, with food/cure/resurrection conflicts to validate before release. Both lich sources may be acquired in either order **after** Gilded/Hunter enrollment. A Dracolich cannot become Archlich; an Archlich cannot acquire a dragon. No other third specialization is automatically allowed. Denied combinations can subsequently be reconsidered only via an explicit owner decision.

## Owner review decisions (2026-11-17)

**New locked denials, both acquisition orders:** Vampire + either Lich; Hunter + Werewolf; Hunter + Vampire; Monk + Werewolf; Monk + either Lich; Hunter + any Dragon; Gilded + Vampire; Gilded + Werewolf; Vampire + any Dragon; Werewolf + any Dragon; Werewolf + either Lich; Monk + any Dragon (owner denials, 2026-11-18).

**Review queue:** Empty. All base specialization pairs have an explicit design status. Ascendant Dragon is rejected; Abomination is approved as a planned two-form hybrid.

Prior implemented/planned combinations remain unchanged. The composite-state CSV closes Golden Dragon to every additional specialization, explicitly including both lichdoms. This is design-only, not a runtime code change.

## Golden Dragon — approved design (2026-11-17)

- **Identity:** Gilded + Cave Dragon only. Forest/Sea Dragon combinations are denied (previously proposed Cave-only restriction retained). Either acquisition order is allowed. Preserve native rites and path progress; determine how a dragon satisfies the established Gilded full-gold-armor Nether food rite if anatomy prevents equipping all four pieces. Do not loosen the rite for other players.
- **Diet:** gold apples and gold carrots only, including enchanted versions if actually present. Unlike ordinary Gilded or Cave Dragon, no general Nether, raw-meat, lava-fish or recipe-derived gold food exception. The four-item conceptual list is golden apple, enchanted golden apple, golden carrot and modded enchanted golden carrot if registered. No speculative registry ID. Existing universal magic-food exceptions are not assumed for this stricter hybrid; verify owner's intended exceptions before adding any. Preserve ordinary Gilded's 2x golden-food nutrition unless incompatible; test one consumption and no duplicated potion effects.
- **Skills:** overlapping legitimate passive and skill bonuses stack (owner explicitly reversed non-stacking suggestion). Retain one shared Tyz SP wallet. No additional hybrid tree implied.
- **Claws and breath:** extend Gilded virtual Fortune to qualifying claw harvesting and Looting to qualifying claw/breath kills attributed to the player. If a breath ability actually harvests blocks, Fortune may apply to that harvest once. Never invent harvest drops for damage-only breath. Same-action dual eligibility does not grant the *same* Gilded bonus twice. Keep existing progression ranks, real enchantment addition and Silk Touch priority.
- **Equipment eligibility pending final choice:** recommendation is to allow the previously qualifying gold tools/weapons as well as claws/breath, and preserve normal Gilded Netherite mining eligibility unless owner specifically restricts it. Do not introduce Netherite weapon Looting; the original Gilded Looting contract is gold swords/axes only. This recommendation is not an extra owner-locked decision.
- Existing Golden Dragon may not acquire either lichdom or any additional specialization; Piglich, Revenant, Dracolich and Archlich cannot gain Gilded/Dragon to construct a Golden Dragon indirectly. The pair is non-transitive.
- **Implementation handoff:** central Progression engine and existing transformations bridge must support narrowly scoped two-way class/native-form eligibility; exact Dragon Survival 1.21.1 claws, breath damage/kill ownership, tool/armor compatibility and diet hooks need review. Test both entry orders, denial of Forest/Sea/lich and Newbie enrollment, one loot roll/bonus per qualifying event, stack math, cure/relog, and renewable food access. No gameplay code or runtime validation in this design PR.

## Blood Immortal — approved design (2026-11-18)

- Monk initiation must precede vampirism. Acquire the vampire identity **only through actual successful native JAWR Lilith ritual**, not generic infection, arbitrary positive-level setter or alternate acquisition route. Vampire-first Monk enrollment is denied. Normal already-owned vampire advancement is distinct from new acquisition.
- Preserve both paths and additive legitimate passive bonuses, ordinary Monk unarmored ability requirements, and JAWR vampire diet/blood and sunlight restrictions. One shared Tyz skill-point wallet; no separate hybrid tree. All additional specializations denied, including werewolf and both lichdoms.
- Implementation pending: identify the precise Lilith ritual completion and native acquisition call chain in the pinned JAWR source/binary. The allow rule must be contextual and server-authoritative, not a general Monk-to-vampire setter exemption; verify successful ritual, failure, bypass routes, reverse enrollment, Newbie guard, stacking, diet, cure/relog and additional-species veto. No gameplay code changed by this register.

## Abomination — approved design (2026-11-18)

- JAWR-native Vampire plus JAWR-native Werewolf, either acquisition order. No lichdom, human discipline, dragon or further persistent specialization. Preserve distinct native progression and the shared Tyz skill-point wallet.
- A narrow native dual-affliction guard exists in source; full identity, acquisition context, cure/physiology/active-form conflict rules and classifier reconciliation remain pending. The old four-form Abomination definition is obsolete; no lich requirement. Exact joining procedure and interaction details are to be resolved during implementation rather than invented here.
- No runtime code is modified by this design lock.

## Review counts

10 base alternatives = 45 unordered distinct pairs, 90 directional distinct transitions, plus 10 invalid duplicate diagonal cells. 7 pairs have experimental integrated source, 7 are approved plans (5 one-way, 2 bidirectional), 31 are denied in both directions, and 0 await design review. The two CSVs use plain status values so they can be filtered or imported.

## Evidence and next steps

- Integrated source baseline `2f662b9e8b73cee47a1e23bf6d1f3510dd41d8ae`: [NativeSpeciesJoinGuard](../compat/transformations/src/main/java/io/github/heatwaveqc/tmm/transformations/core/NativeSpeciesJoinGuard.java), [TransitionPolicy](../compat/transformations/src/main/java/io/github/heatwaveqc/tmm/transformations/core/TransitionPolicy.java), [FormResolver](../compat/transformations/src/main/java/io/github/heatwaveqc/tmm/transformations/core/FormResolver.java), [integration manifest](../manifest/integration-sources.json).
- The current generic class/species veto denies all new forms for enrolled human disciplines and vice versa. Piglich/Revenant, Blood Immortal and Golden Dragon require narrow, order-aware or bidirectional exceptions through the shared Progression/transformations architecture. Do not globally relax the veto.
- [JAWR-only Abomination research](JAWR-ONLY-ABOMINATION-FEASIBILITY-2026-11-15.md): dual-affliction guard already allows both forms, but classifier still assumes obsolete four-form Abomination; the later two-form target needs reconciliation. No older four-form ritual is authorized.
- [Dracolich pilot](../compat/transformations/docs/DRACOLICH-PILOT.md) and [Cave/Center rendering pilot](../compat/transformations/docs/DRACOLICH-CAVE-CENTER-PILOT.md): verify both directions for each species and each lich, actual power/appearance/food/death/cure behavior and rejection of the second lich.
- This register supersedes historical matrix/design surveys on combinations only. It does not change gameplay source, quest rewards, mod pins or build authorization. Quick/static and subsequent owner-authorized in-game tests must distinguish eligibility from actual transformation success.
