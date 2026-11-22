# Monk: full five-lane 1,000-point tree — authoring handoff

> **Design blueprint / not yet playable.** This document accompanies the machine-readable `MONK-FIVE-LANE-1000PT-DRAFT.json` and deliberately does not overwrite the live Monk path. Numerical effects outside already-defined `Focus` and approved `Enlightenment` are first-pass candidates.

## Approved architecture and identity

- **Total purchase budget: 1,000 MXP skill points** across all five Monk specialization branches, with full mastery allowed. Other Path trees are generally planned around 100 points.
- **37 purchased nodes:** 36 proposed additions plus existing five-rank `Focus` (20 total points), alongside the 29 existing free, automatic Monk skills. `Focus` is preserved, not duplicated.
- **Enlightenment is the very first purchasable node** available upon First Sunset Monk initiation. 18 ranks cost 1 point each and reduce the base ordinary XP exponential factor by 0.001 per rank (from 1.020 down to 1.002). A player already at lifetime MXP level P keeps P and only subsequent ordinary awards change.
- **Unified MXP wallet is default**, with three configurable economic models required by [#475](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/475). Existing native level gates must be reconciled so the default does not secretly depend on earning/spending a second Monk XP currency.
- **All five lanes can be purchased fully.** Every proposed new ability is passive or enhances another activity/spell/potion; no native Spirit Projectile, new attack button, or free creative flight.
- **Curios permitted** for Monk endgame and Empty-Hand Paragon, without conventional weapons/armor. Maintain 80–90% of real optimized endgame gear/specialists and feasibility of solo Chaos Guardian, not an automatic victory.
- **Core family completeness:** the live Core definition contains zero active nodes; unmerged pilot [PR #379](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/379) contains Reservoir, Wellspring, Thrift and Potency. Monk Magical Cultivation extends each, plus all subsequently approved universal Core abilities. Core ranks are not duplicated or charged twice.
- **XP sourcing:** all legitimate barehand blocks, including stone without drops and player-placed blocks, qualify for linear hardness MXP in unified mode; a validated spell mining action may yield **both** nominal-cost magical MXP and every eligible physical block reward. Anti-duplicate callback protections are correctness, not farming suppression.

## Exact rank-purchase budget

| Branch | Nodes | Cost |
|---|---:|---:|
| Martial Body | 8 | 200 |
| Conditioning | 6 | 200 |
| Footwork | 7 | 150 |
| Hands As Tools | 7 | 150 |
| Magical Cultivation | 9 | 300 |
| **Total** | **37** | **1,000** |

The following tables sum each rank cost *including repeated ranks*. A 20-rank skill costing 2 MXP points/rank contributes 40 points to the budget.

## Martial Body — 200 points

| Purchased skill | Max ranks | Rank cost | Total points | Gate | Effect proposal |
|---|---:|---:|---:|---|---|
| Empty Hand Force | 50 | 1 | 50 | entry | +0.30 unarmed damage per rank (+15 max), no normal armor. |
| Flowing Counter | 20 | 2 | 40 | adept; `empty_hand_force` | Retaliation against real hostile melee hits, capped 10 damage, 40-tick cooldown. |
| Empty Hand Precision | 20 | 2 | 40 | adept; `empty_hand_force` | +1 percentage point/rank chance of 1.5x fist strike (20% max). |
| Fist Tempo | 20 | 1 | 20 | entry; `empty_hand_force` | +0.5% fist attack speed/rank (10% max). |
| Piercing Palm | 20 | 1 | 20 | mastery; `flowing_counter` | +1%/rank bounded armor penetration on actual fist strikes (20% max). |
| Paragon Force | 10 | 2 | 20 | ascendant; `empty_hand_precision` | +2%/rank final fist damage (20% max), not duplicate of native Iron Palm. |
| Pressure Points | 5 | 1 | 5 | mastery; `empty_hand_precision` | +1%/rank chance of short Slowness after real fist hit (5% max), bosses excluded. |
| Empty Hand Paragon | 1 | 5 | 5 | capstone; `paragon_force` | +5 fist damage and +3 armor unarmored; Curios allowed; Dragon + late milestones required. |

## Conditioning — 200 points

| Purchased skill | Max ranks | Rank cost | Total points | Gate | Effect proposal |
|---|---:|---:|---:|---|---|
| Natural Poise | 40 | 1 | 40 | entry | +0.4 natural armor/rank (+16); effective defense audited versus already free armor. |
| Iron Body | 40 | 1 | 40 | adept; `natural_poise` | +0.25 armor toughness/rank (+10), no normal armor. |
| Inner Vitality | 40 | 1 | 40 | entry; `natural_poise` | +0.5 maximum health/rank (+20 HP), no normal armor. |
| Quick Recovery | 20 | 2 | 40 | mastery; `inner_vitality` | +0.1 HP per second/rank passive regeneration (+2 HP/s). |
| Elemental Tempering | 20 | 1 | 20 | mastery; `iron_body` | +0.75%/rank reduction of remaining fire, explosion and magic damage (15% max). |
| Hunger Mastery | 20 | 1 | 20 | adept; `inner_vitality` | +1%/rank reduction in remaining exhaustion (20% max), above free Hunger Discipline. |

## Footwork — 150 points

| Purchased skill | Max ranks | Rank cost | Total points | Gate | Effect proposal |
|---|---:|---:|---:|---|---|
| Focus | 5 | 2+3+4+5+6 | 20 | existing; `steady_hands` | Existing paid Focus: +5% base movement speed/rank; preserve current native rank/Steady Hands gate until economy migration. |
| Body Speed | 30 | 1 | 30 | entry | +0.4%/rank base movement speed (+12%), conditionally unarmored. |
| Balanced Landing | 20 | 1 | 20 | adept; `body_speed` | +1%/rank reduction of remaining fall damage (20% max), after free Feather Fall. |
| Evasion | 20 | 1 | 20 | adept; `body_speed` | +0.5%/rank dodge against eligible direct attacks (10% max), not an immunity. |
| Glide Control | 20 | 1 | 20 | ascendant; `balanced_landing` | +1%/rank post-Dragon Momentum Glide control (20% max). |
| Momentum Step | 20 | 1 | 20 | mastery; `body_speed` | +1%/rank sprint/strafe control bonus (20% max), no creative flight. |
| Graceful Step | 20 | 1 | 20 | entry | +0.015 knockback resistance/rank (0.30 maximum). |

## Hands As Tools — 150 points

| Purchased skill | Max ranks | Rank cost | Total points | Gate | Effect proposal |
|---|---:|---:|---:|---|---|
| Barehand Efficiency | 30 | 1 | 30 | entry | +5%/rank legitimate final fist mining speed (+150% max), above free Hand Mining. |
| Fortunate Hands | 10 | 2 | 20 | adept; `barehand_efficiency` | +0.3 Fortune/rank (max +3), Treasure Hand Fortune mode only, not Silk mode. |
| Looting Palm | 20 | 1 | 20 | mastery; `fortunate_hands` | +1.25%/rank chance for one extra ordinary mob drop on direct fist kill (25%), no boss loot. |
| Toolless Reach | 20 | 1 | 20 | entry | +0.075 block reach/rank (+1.5 blocks), while fist is empty. |
| Worldly Cultivation | 20 | 1 | 20 | adept; `toolless_reach` | +1%/rank chance of additional ordinary mature-crop drop (20%). |
| Stone Resonance | 20 | 1 | 20 | mastery; `barehand_efficiency` | +1%/rank mining speed for block hardness >=5 (+20%); does not modify XP reward hardness. |
| Overworld Bounty | 20 | 1 | 20 | mastery; `worldly_cultivation` | +1%/rank chance of one extra eligible ordinary ore drop (20%), no machine/boss loot. |

## Magical Cultivation — 300 points

| Purchased skill | Max ranks | Rank cost | Total points | Gate | Effect proposal |
|---|---:|---:|---:|---|---|
| Enlightenment | 18 | 1 | 18 | first | FIRST purchasable immediately after Monk initiation: -0.001 future ordinary global MXP growth exponent per rank, 1.020 -> 1.002 at 18/18; not retroactive. |
| Inner Reservoir | 30 | 2 | 60 | entry; `enlightenment` | Extends every global Core Reservoir capacity family, much higher personal mana/EV/Psi endgame limits; no external tanks. |
| Inner Wellspring | 30 | 2 | 60 | entry; `enlightenment` | Extends every global Core Wellspring regeneration family, improved personal recovery. |
| Inner Thrift | 20 | 2 | 40 | adept; `inner_reservoir` | Extends every Core Thrift efficiency family; provisional +1%/rank bounded actual cost discount (20%), MXP tracks nominal. |
| Inner Potency | 20 | 2 | 40 | adept; `inner_wellspring` | Extends every Core Potency family; provisional +1%/rank magic potency (20%). |
| Spellward | 16 | 2 | 32 | mastery; `inner_thrift` | +1%/rank reduction of remaining magical damage (16%), after other passive effects. |
| Arcane Attunement | 10 | 2 | 20 | mastery; `inner_potency` | +0.5%/rank native casting speed/cooldown throughput where provider supports it (5%). |
| Meditative Elixir | 10 | 2 | 20 | mastery; `inner_wellspring` | +2%/rank duration of beneficial consumed potions (20%); no new active skill. |
| Inner Fortune | 10 | 1 | 10 | ascendant; `spellward` | +1 true Luck attribute per rank (+10); not Fortune or Looting. |

## Stage gates and prerequisites

- **first:** rank 1 purchase offered immediately after Monk enrollment; Enlightenment deliberately has no additional stage/milestone prerequisite.
- **entry:** early Monk development. **adept:** at least one rank in indicated prerequisite plus appropriate training. **mastery:** relevant earned Monk milestones / specialization training. **ascendant:** challenging late-game progression. **capstone:** Dragon victory, late Monk mastery and a paid mastery of another lane.
- Existing automatic baseline skills should continue to unlock through approved physical cultivation and milestones; under unified economy those gates cannot accidentally require a hidden separate spendable Monk skill-point wallet.
- All purchased skill ranks remain owned but dormant when the player is ineligible for Monk effects; respec does not reset lifetime MXP and cannot double-refund permanent rank purchases.

## What exists, and what needs executable engine work

| Component | Baseline reality | Next work |
|---|---|---|
| 30 existing Monk skills | Present on `dev/progression`: 29 free auto-ranked, 1 paid Focus | Preserve existing effects and adjust eligibility for default unified currency |
| Historical paid skills | 18-skill design in merged old Tyz [PR #229](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/229); retired backend | Forward-port effects only; no retired Tyz Java or Curios-empty capstone |
| Core/Monk pilot | Open [PR #379](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/379), old `tmm_progression` namespace, unmerged | Reconcile three paid Monk nodes and four Core families; do not duplicate |
| Unified purchases | `ChannelConfig` supports independent currency/level binding; ordinary Path events still skip magic-wallet settlement | Implement three-mode policy, global activity MXP awards, and level gate semantics in generic Progression engine |
| Enlightenment | `MxpMath.apply(...,growth)` supports runtime growth input; no purchased per-player growth modifier in active code | Add generic purchased-player `xp_growth_delta` and tests, apply after checking eligible Monk purchased rank |
| Barehand no-drop stone | Current `BlockDropsEvent` award is harvest-gated | Observe validated successful removal irrespective of item drops, preserve TreeChop/Spell AoE ownership, use original hardness |
| Specialty effects | Attribute and some basic combat handlers exist; most high-end conditional effects need generic adapters | Source audit/prioritize effect types; no paid no-op nodes in the live tree |
| Endgame parity | Gear-normalized audits are historical, not verified against current pinned endgame gear | Measure 85% actual DPS/survival/magic and Chaos Guardian feasibility in owner-authorized QA |

## Engine work order

1. `feature/progression-*`: complete [#475](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/475) default unified wallet and all three configurable modes with existing-save reset scope as documented in AGENTS; add authoritative source-specific additive and deduplicated MXP awards.
2. Generic `xp_growth_delta` ability for Enlightenment (18 ranks, immediate post-initiation purchase), including dedicated unit tests for per-player growth, lifetime P, nonretroactivity, fixed reward exceptions, Path exit, relog and mode differences.
3. Restore no-drop barehand stone credit (#59) through post-commit physical block attribution and configurable hardness coefficient, without a per-block whitelist; multi-block spells must award magic + every legitimate block independently.
4. Promote the paid Monk pilot Force/Counter/Poise and Core Reservoir/Wellspring/Thrift/Potency after namespace reconciliation and active effect tests. Maintain exactly one paid Focus.
5. Implement and verify each new generic passive handler, stage/graph purchase gate and source-native magic family extension. Insert runtime nodes incrementally: no placeholder/no-op skill purchases.
6. Run scoped JSON/compile/unit and integration preflight before any integration-owner promotion; **no unsolicited full smoke, Prism build, or gameplay PASS**. QA approval still required.

## Pricing and source validation

- All 37 IDs, per-rank positive integer prices, prerequisites and lane budgets were checked in the generator. First-purchase and exactly 1,000 spent point totals are mechanically asserted.
- These are costs to **purchase every rank**, not maximum rank count, lifetime XP cap, or `1000` total node count.
- Stat values are provisional. Re-evaluate against current enchantments, Apotheosis affixes/gems, Curios, legitimate gear acquisition and DR mechanics. Any provider attribute without a safe registry binding remains blocked until verified.
- The baseline 1.020 XP growth results in ~19.9 trillion ordinary base MXP for 1,000 global earned points without Enlightenment; full Enlightenment at level 0 (theoretical lower bound) reduces this to ~3.19 million. Purchasing 18 ranks progressively costs more and must be profiled.

## Merge ownership / current testing state

**This PR is configuration-owned design work only**. Do not install this blueprint as `data/tyr_progression/.../paths/monk.json`: that schema cannot implement all new effects and unified-mode XP yet. No first-party gameplay code, test runtime, packwiz, manifest, QA builds or server smoke are changed here. The Progression engine writer integrates any necessary reusable runtime capability in a separate `feature/progression-*` PR targeting `dev/progression`.

References: [#271](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/271), [#475](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/475), [#59](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/59), [#455](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/455).
