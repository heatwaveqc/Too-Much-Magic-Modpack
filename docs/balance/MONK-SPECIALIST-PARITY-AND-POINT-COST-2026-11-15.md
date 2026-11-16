# Monk parity and progression cost contract — owner target (2026-11-15)

**Status:** APPROVED DESIGN TARGET; research and simulation only. No Monk, Tyz, MXP, or standalone progression runtime JSON is activated by this document.

## Order of operations

1. Determine the legal fully enhanced ATM10 specialist attribute maxima for each monitored trait, using **distinct** loadouts where equipment/gems/affixes conflict.
2. Author and measure TMM non-Monk specialist Tyz trees to bring maximum compatible TMM specialist equipment + skills approximately into parity with ATM10's same trait and loadout (initial +/-5% design tolerance).
3. Calculate TMM Monk values against those **completed TMM specialist attributes**. The Monk is deliberately broad but far more expensive, rather than strictly as strong as or stronger than an optimally dedicated specialist.
4. Calibrate the **cost/XP economics** of the Monk Tyz tree, then test attainable progression.

## Hard parity rule: per modified trait 80–90%

For **every numeric attribute or trait that the Monk tree actively modifies**, the final fully invested, legal Monk build for that trait should reach **80% to 90% of the final trait value of the optimized comparable TMM non-Monk specialist**. Default design aim is **85%**, but 85% is not a strict universal point target. All modified traits get their own benchmark; no averaging across categories and no hiding a sub-80% attribute behind 100% performance on a different attribute.

**Attribute target** is the requirement, not merely DPS or effective-hit-points parity. Also record actual DPS, cooldown duration, armor mitigation etc. as secondary sanity/safety checks to detect nonlinear outcomes. If an attribute is clamped, signed, a reduction percentage, a chance, or has a nonzero/negative baseline, document its *meaningful final stat / effective attribute level* and the comparator's semantics, rather than blindly multiplying a bonus modifier. For nonnumeric abilities (flight, projectile catch, water walking, gear restrictions, harvest legality) write an explicit qualitative parity or availability test; do not invent a percentage.

Examples of numeric comparison candidates (only if Monk explicitly modifies them): max health, armor, armor toughness, attack damage, attack speed, movement speed, dodge, resistances, spell power, mana capacity, mana regeneration, cooldown reduction, cast-time reduction, harvest/break speed, reach, damage reduction, critical chance/damage. For specialist-specific stats unavailable to Monk, mark NOT MODIFIED, not FAIL. Each record should include: trait ID, comparable specialist, source loadout, specialist final value, Monk target minimum (0.8x), preferred 0.85x, maximum (0.9x), actual Monk final value, stacking/cap rules, and whether tested.

Equipment policy: Monk armor-dependent powers remain subject to **unarmored** conditions and reserved-slot policy. Compare an *actual legal unarmored Monk* to a *fully geared specialist*; do not credit Monk with armor items it cannot use. Named Monk abilities that are not stat modifications retain their own classic-parity specifications.

## Hard pricing rule: Monk Tyz tree costs about 10x the points

Let `C_s` be the **total Tyz skill points spent to fully buy all purchasable ranks of a typical non-Monk specialist tree** (a comparable completed path, not a single node), and `C_m` the cost of fully buying the Monk's Tyz skill tree. Target **C_m ~= 10 * C_s**, initially interpret "roughly" as a **9x–11x design acceptance window** against the chosen standard-specialist baseline. Record each other specialist's total as well; if their costs differ substantially, choose and document a canonical comparator (e.g. median), don't silently claim 10x every unequal tree. All skills purchased and prerequisite chains must be included in the sum; any automatically granted/free baseline powers and other currencies are tracked separately.

**One shared Tyz/MXP wallet, no special Monk XP source or free Monk discount.** Monk receives the same earnable global skill points as everyone else; each Monk node/rank costs more total points. Its benefit is distributed breadth across many traits, not a superior stat ceiling. Do not define a separate easy Monk point economy. If the standalone progression module provides classical Monk techniques automatically or uses its **native** technique-point ledger, that is **not** the Tyz spend being compared. Document both economies separately to prevent double counting and ambiguity; future design can decide which classic abilities are native and which are Tyz purchases.

## XP is significantly more than 10x (and needs a playability audit)

The **current MXP design prototype**, `compat/mxp/src/main/java/io/github/heatwaveqc/tmm/mxp/core/MxpMath.java` on `feature/mxp-ars-irons-botania-neo-hooks`, makes ordinary effective XP for each event `base / (1.2 ^ P)`, where **P is lifetime levels earned through MXP**, NOT skills purchased. The Tyz level pool pilot awards **one skill point per 1,000 effective MXP**. Under pure ordinary XP from lifetime P=0, ignoring fixed-first bonuses, nominal base XP for N points/levels is:

```text
XP_base(N) = 1000 * ((1.2 ^ N) - 1) / 0.2
XP_ratio(10*N, N) = ((1.2 ^ (10*N)) - 1) / ((1.2 ^ N) - 1)
```

Therefore the Monk's approximately tenfold **point cost** implies a **much greater than tenfold ordinary XP cost**, as the owner intends, without applying an extra XP multiplier on top. **Do not equate lifetime P with spent points.** Respec/refunds don't reset the lifetime progression divisor. Fixed first-time boss/ritual awards, shared XP spent in other trees and actual price scaling change real runs; simulate those cases separately.

**High-risk pacing warning:** This 1.2^P lifetime scaling becomes extremely steep at large N; a literal 10x point requirement may make a completed Monk practically unreachable with ordinary play. This is intended to be a high-investment capstone, but **not** license to publish an impossible progression. Before runtime skill deployment, simulate levels, ordinary nominal XP, fixed bonuses, earning rates, practical time, exhaustion/floating-point behavior and refund/respec edge cases, and present the resulting curve for balancing approval. Do not casually lower the 10x SP target to solve the XP problem; adjust how an achievable XP economy is constructed if needed.

## Acceptance tests

- [ ] Every Monk-modified numeric trait has its own 80%, 85%, 90% comparator row with a fully equipped TMM specialist **final** attribute and the legal unarmored Monk's **final** attribute.
- [ ] At full legitimate Monk tree investment, **every modified trait** lies in its intended 80–90% band, with explicit rationale if any trait's semantics make simple ratios meaningless.
- [ ] TMM specialist stat maxima are calibrated to legal fully enhanced ATM10 reference equipment; no comparison uses an impossible sum of mutually exclusive gems/affixes.
- [ ] Total Tyz points needed to max Monk is 9–11x canonical full specialist tree cost; collect per-tree actual node/rank SP totals to select comparator.
- [ ] MXP earning scenario from a fresh level-0 player, with/without first-time fixed awards, demonstrates strictly more than 10x total nominal ordinary XP for the Monk versus comparator and records the ratio and predicted progression time.
- [ ] Verify Tyz skill purchase, bulk purchase, refund, max-rank, external gate, and MXP bridge without free ranks or negative-currency exploits.
- [ ] Preserve Monk classic powers, armor-exclusion rules and separate native discipline progression; never misrepresent standalone native points as Tyz SP.

## Authoritative/relevant references

- ATM10 benchmark: [ATM10-SPECIALIST-BENCHMARK-2026-11-15.md](ATM10-SPECIALIST-BENCHMARK-2026-11-15.md) and [manifest/atm10_specialist_benchmark.json](../../manifest/atm10_specialist_benchmark.json).
- Existing standalone Monk/Hunter core: [compat/progression/README.md](../../compat/progression/README.md) (distinct technique points).
- Tyz 6.4.2 gating: [TYZS-EXTERNAL-ADVANCEMENT-KUBEJS-GATES-2026-11-15.md](../TYZS-EXTERNAL-ADVANCEMENT-KUBEJS-GATES-2026-11-15.md).
- MXP implementation math, source-only prototype: [MxpMath.java](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/feature/mxp-ars-irons-botania-neo-hooks/compat/mxp/src/main/java/io/github/heatwaveqc/tmm/mxp/core/MxpMath.java).
- Tyz level pool prototype: [level-pool.json](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/feature/mxp-ars-irons-botania-neo-hooks/compat/magic/tyzs-test-overlay/config/tyzs_skills/custom/data/level-pool.json).

**Supersession:** All earlier one-number "Monk is exactly 85%" statements are superseded by 80–90% for each modified trait, with 85% only the preferred midpoint. Earlier claims that Monk needs 85% of *throughput alone* are superseded by **attribute-level parity**, with throughput retained as a secondary diagnostic.
