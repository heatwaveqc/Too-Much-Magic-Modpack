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

## MXP growth factor — 1.2 current prototype; 1.1 or 1.0 under consideration

**Status as of 2026-11-15: open balancing decision, not authorization to change the MXP runtime.** The owner is reconsidering the current `1.2^P` lifetime level normalizer because the approved Monk full-tree **Tyz SP price remains ~10x** a normal specialist's completed tree. Explore `1.1^P` and `1.0^P` (linear), and calculate 1.01/1.02/1.03/1.05 as useful sensitivity points. Preserve **one shared MXP ledger** and **one Tyz level/skill wallet**; lowering growth must not accidentally create a separate Monk XP source.

The source-only current MXP prototype at `feature/mxp-ars-irons-botania-neo-hooks` implements `effectiveEventXP = nominalBaseXP / (1.2 ^ P)`, where **P is lifetime MXP levels earned, not spent Tyz skill ranks**. The pilot Tyz level pool grants **one skill point per 1,000 effective MXP**. For growth factor `g >= 1`, an ordinary-only fresh player starting at P=0 requires

```text
nominalBaseXP(N, g) = 1000 * ((g^N) - 1) / (g-1)   if g > 1
nominalBaseXP(N, 1) = 1000 * N                     if g = 1
ratio(N, 10*N, g) = nominalBaseXP(10*N,g) / nominalBaseXP(N,g)
```

### Example cumulative *ordinary base XP* to reach an illustrative total point budget

| Lifetime points/levels | 1.2^P (prototype) | 1.1^P (proposed) | 1.05^P (sensitivity) | 1.02^P (sensitivity) | 1.0^P (linear proposal) |
| --- | ---: | ---: | ---: | ---: | ---: |
| 20 | 186,688 | 57,275 | 33,066 | 24,297 | 20,000 |
| 50 | 45,497,191 | 1,163,909 | 209,348 | 84,579 | 50,000 |
| 100 | 414,089,867,610 | 137,796,123 | 2,610,025 | 312,232 | 100,000 |
| 200 | 3.4294e19 | 1,899,052,754,605 | 345,831,616 | 2,574,245 | 200,000 |

### Effect on the Monk's 10x-spend target

| Example specialist SP -> Monk SP | 1.2^P ordinary XP ratio | 1.1^P | 1.05^P | 1.02^P | 1.0^P |
| --- | ---: | ---: | ---: | ---: | ---: |
| 5 -> 50 | 6,114x | 190.6x | 37.9x | 16.3x | 10x |
| 10 -> 100 | 15.95 million x | 8,646x | 207.5x | 28.5x | 10x |
| 20 -> 200 | 1.84e14 x | 33.16 million x | 10,459x | 105.9x | 10x |

**Design implication:** With **g=1.0**, the literal 10x Tyz SP target gives **10x** ordinary nominal XP from otherwise identical fresh starts—not the earlier `significantly >10x` XP objective. Thus `>10x XP` is now a **conditional preference when g>1**, not a separate hard invariant. With g=1.1, even 200 points can be impossibly expensive in normal gameplay. A very mild multiplier (1.01–1.03) could preserve a substantial experience premium without the astronomical costs of 1.1 or 1.2, but the best factor depends on the **actual eventual Tyz tree SP totals** and verified recurring income.

**Pacing / implementation gates:** First enumerate the specialist and Monk complete Tyz point budgets; model the runtime game sources of *ordinary MXP per hour* and *fixed first-time milestones*, plus shared-wallet spending in other trees. Then choose the growth factor. A future implementation must make the currently **hardcoded** `MxpMath.GROWTH = 1.2` adjustable by validated config, test exact g=1.0 handling (no division-by-zero in geometric-sum models, but `Math.pow(1,P)` is valid in existing event code), test high P/floating-point overflow, and preserve lifetime P across refunds/respecs.

No runtime MXP growth setting, Tyz level-pool settings or gameplay skills are changed by this research.

## Acceptance tests

- [ ] Every Monk-modified numeric trait has its own 80%, 85%, 90% comparator row with a fully equipped TMM specialist **final** attribute and the legal unarmored Monk's **final** attribute.
- [ ] At full legitimate Monk tree investment, **every modified trait** lies in its intended 80–90% band, with explicit rationale if any trait's semantics make simple ratios meaningless.
- [ ] TMM specialist stat maxima are calibrated to legal fully enhanced ATM10 reference equipment; no comparison uses an impossible sum of mutually exclusive gems/affixes.
- [ ] Total Tyz points needed to max Monk is 9–11x canonical full specialist tree cost; collect per-tree actual node/rank SP totals to select comparator.
- [ ] MXP earning scenario from a fresh level-0 player, with/without first-time fixed awards, compares g=1.0, 1.01, 1.02, 1.05, 1.1 and 1.2; records the actual nominal ordinary XP ratio (10x at g=1) and predicted progression time, with and without fixed bonuses.
- [ ] Verify Tyz skill purchase, bulk purchase, refund, max-rank, external gate, and MXP bridge without free ranks or negative-currency exploits.
- [ ] Preserve Monk classic powers, armor-exclusion rules and separate native discipline progression; never misrepresent standalone native points as Tyz SP.

## Authoritative/relevant references

- ATM10 benchmark: [ATM10-SPECIALIST-BENCHMARK-2026-11-15.md](ATM10-SPECIALIST-BENCHMARK-2026-11-15.md) and [manifest/atm10_specialist_benchmark.json](../../manifest/atm10_specialist_benchmark.json).
- Existing standalone Monk/Hunter core: [compat/progression/README.md](../../compat/progression/README.md) (distinct technique points).
- Tyz 6.4.2 gating: [TYZS-EXTERNAL-ADVANCEMENT-KUBEJS-GATES-2026-11-15.md](../TYZS-EXTERNAL-ADVANCEMENT-KUBEJS-GATES-2026-11-15.md).
- MXP implementation math, source-only prototype: [MxpMath.java](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/feature/mxp-ars-irons-botania-neo-hooks/compat/mxp/src/main/java/io/github/heatwaveqc/tmm/mxp/core/MxpMath.java).
- Tyz level pool prototype: [level-pool.json](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/feature/mxp-ars-irons-botania-neo-hooks/compat/magic/tyzs-test-overlay/config/tyzs_skills/custom/data/level-pool.json).

**Supersession:** The Monk's 80–90% attribute target and ~10x SP requirement remain owner-locked. The older unconditional >10x XP requirement is superseded: XP ratio depends on the growth factor, which remains undecided. All earlier one-number "Monk is exactly 85%" statements are superseded by 80–90% for each modified trait, with 85% only the preferred midpoint. Earlier claims that Monk needs 85% of *throughput alone* are superseded by **attribute-level parity**, with throughput retained as a secondary diagnostic.
