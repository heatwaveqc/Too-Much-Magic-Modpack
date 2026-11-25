# Monk — 1,000-point mastery, one point per rank

**Design only.** This is the source-of-truth draft for full Monk purchase budgets. It does not modify the live, source-tested 67-ability Monk preview in [PR #486](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/486).

## Universal rank-price correction

- Exactly **1 advancement point purchases 1 rank**. No rising price per rank, premium final rank or 5-point capstone. The 38 purchased skills contain **1,000 total one-point ranks**, and every branch can be mastered.
- **Monk is the sole planned 1,000-point Path exception** to the 100-point non-Monk tree target. Core remains universal and separate from Path skill purchases.
- The previous expensive-rank Monk design was internally inconsistent with the owner rule. To preserve 1,000-point mastery without selling multi-point ranks, each skill now has as many rank purchases as its prior *total-point budget*. Actual runtime per-rank effects must therefore be recalibrated, not blindly copied.
- **Focus migration:** currently live as 5 paid ranks costing 2,3,4,5,6 points (=20) with +5% speed/rank (+25% max). Design proposes **20 one-point ranks** with an eventual +1.25% speed/rank to preserve +25% max, pending engine/source implementation. Do not pretend the live implementation already follows this.
- **Enlightenment unchanged:** first Monk purchasable after initiation, 18 one-point ranks, each reducing future ordinary MXP growth by 0.001 from 1.020 → 1.002. No retroactivity.
- **Other perks:** retain maximum *total effectiveness* of the previous full-rank target; per-rank magnitudes should be scaled to match the longer rank ladder and validated against actual pinned attributes and gear. Numeric candidates are **not** verified game behavior.
- **Innate/free:** preserve 29 automatic Monk abilities, unarmored/no conventional weapons, Curios, native milestone and Transformation interactions.

## Exact skill-point allocation

| Lane | Paid skills | One-point ranks |
|---|---:|---:|
| Martial Body | 8 | 200 |
| Conditioning | 6 | 200 |
| Footwork | 7 | 150 |
| Hands As Tools | 5 | 110 |
| Magical Cultivation | 12 | 340 |
| **Total** | **38** | **1000** |

## Martial Body — 200 one-point ranks

| Skill | Previous priced ranks | One-point ranks | Full SP price | Effect goal |
|---|---:|---:|---:|---|
| **Empty Hand Force** (`tyr_progression:empty_hand_force`) | 50 | 50 | 50 | +0.30 unarmed damage per rank (+15 max), no normal armor. |
| **Flowing Counter** (`tyr_progression:flowing_counter`) | 20 | 40 | 40 | Retaliation against real hostile melee hits, capped 10 damage, 40-tick cooldown. |
| **Empty Hand Precision** (`tyr_progression:empty_hand_precision`) | 20 | 40 | 40 | +1 percentage point/rank chance of 1.5x fist strike (20% max). |
| **Fist Tempo** (`tyr_progression:fist_tempo`) | 20 | 20 | 20 | +0.5% fist attack speed/rank (10% max). |
| **Piercing Palm** (`tyr_progression:piercing_palm`) | 20 | 20 | 20 | +1%/rank bounded armor penetration on actual fist strikes (20% max). |
| **Paragon Force** (`tyr_progression:paragon_force`) | 10 | 20 | 20 | +2%/rank final fist damage (20% max), not duplicate of native Iron Palm. |
| **Pressure Points** (`tyr_progression:pressure_points`) | 5 | 5 | 5 | +1%/rank chance of short Slowness after real fist hit (5% max), bosses excluded. |
| **Empty Hand Paragon** (`tyr_progression:empty_hand_paragon`) | 1 | 5 | 5 | +5 fist damage and +3 armor unarmored; Curios allowed; Dragon + late milestones required. |

## Conditioning — 200 one-point ranks

| Skill | Previous priced ranks | One-point ranks | Full SP price | Effect goal |
|---|---:|---:|---:|---|
| **Natural Poise** (`tyr_progression:natural_poise`) | 40 | 40 | 40 | +0.4 natural armor/rank (+16); effective defense audited versus already free armor. |
| **Iron Body** (`tyr_progression:iron_body`) | 40 | 40 | 40 | +0.25 armor toughness/rank (+10), no normal armor. |
| **Inner Vitality** (`tyr_progression:inner_vitality`) | 40 | 40 | 40 | +0.5 maximum health/rank (+20 HP), no normal armor. |
| **Quick Recovery** (`tyr_progression:quick_recovery`) | 20 | 40 | 40 | +0.1 HP per second/rank passive regeneration (+2 HP/s). |
| **Elemental Tempering** (`tyr_progression:elemental_tempering`) | 20 | 20 | 20 | Reduce damage based on vanilla #minecraft:is_fire and #minecraft:is_explosion tags, independently of four direct vanilla-type wards. No magic-source inference. |
| **Hunger Mastery** (`tyr_progression:hunger_mastery`) | 20 | 20 | 20 | +1%/rank reduction in remaining exhaustion (20% max), above free Hunger Discipline. |

## Footwork — 150 one-point ranks

| Skill | Previous priced ranks | One-point ranks | Full SP price | Effect goal |
|---|---:|---:|---:|---|
| **Focus** (`tyr_progression:focus`) | 5 | 20 | 20 | Existing paid Focus: +5% base movement speed/rank; preserve current native rank/Steady Hands gate until economy migration. |
| **Body Speed** (`tyr_progression:body_speed`) | 30 | 30 | 30 | +0.4%/rank base movement speed (+12%), conditionally unarmored. |
| **Balanced Landing** (`tyr_progression:balanced_landing`) | 20 | 20 | 20 | +1%/rank reduction of remaining fall damage (20% max), after free Feather Fall. |
| **Evasion** (`tyr_progression:evasion`) | 20 | 20 | 20 | +0.5%/rank dodge against eligible direct attacks (10% max), not an immunity. |
| **Glide Control** (`tyr_progression:glide_control`) | 20 | 20 | 20 | +1%/rank post-Dragon Momentum Glide control (20% max). |
| **Momentum Step** (`tyr_progression:momentum_step`) | 20 | 20 | 20 | +1%/rank sprint/strafe control bonus (20% max), no creative flight. |
| **Graceful Step** (`tyr_progression:graceful_step`) | 20 | 20 | 20 | +0.015 knockback resistance/rank (0.30 maximum). |

## Hands As Tools — 110 one-point ranks

| Skill | Previous priced ranks | One-point ranks | Full SP price | Effect goal |
|---|---:|---:|---:|---|
| **Barehand Efficiency** (`tyr_progression:barehand_efficiency`) | 30 | 30 | 30 | +5%/rank legitimate final fist mining speed (+150% max), above free Hand Mining. |
| **Fortunate Hands** (`tyr_progression:fortunate_hands`) | 10 | 20 | 20 | +0.3 Fortune/rank (max +3), Treasure Hand Fortune mode only, not Silk mode. |
| **Looting Palm** (`tyr_progression:looting_palm`) | 20 | 20 | 20 | +1.25%/rank chance for one extra ordinary mob drop on direct fist kill (25%), no boss loot. |
| **Toolless Reach** (`tyr_progression:toolless_reach`) | 20 | 20 | 20 | +0.075 block reach/rank (+1.5 blocks), while fist is empty. |
| **Worldly Cultivation** (`tyr_progression:worldly_cultivation`) | 20 | 20 | 20 | +1%/rank chance of additional ordinary mature-crop drop (20%). |

## Magical Cultivation — 340 one-point ranks

| Skill | Previous priced ranks | One-point ranks | Full SP price | Effect goal |
|---|---:|---:|---:|---|
| **Enlightenment** (`tyr_progression:enlightenment`) | 18 | 18 | 18 | FIRST purchasable immediately after Monk initiation: -0.001 future ordinary global MXP growth exponent per rank, 1.020 -> 1.002 at 18/18; not retroactive. |
| **Inner Reservoir** (`tyr_progression:inner_reservoir`) | 30 | 60 | 60 | Extends every global Core Reservoir capacity family, much higher personal mana/EV/Psi endgame limits; no external tanks. |
| **Inner Wellspring** (`tyr_progression:inner_wellspring`) | 30 | 60 | 60 | Extends every global Core Wellspring regeneration family, improved personal recovery. |
| **Inner Thrift** (`tyr_progression:inner_thrift`) | 20 | 40 | 40 | Extends every Core Thrift efficiency family; provisional +1%/rank bounded actual cost discount (20%), MXP tracks nominal. |
| **Inner Potency** (`tyr_progression:inner_potency`) | 20 | 40 | 40 | Extends every Core Potency family; provisional +1%/rank magic potency (20%). |
| **Spellward** (`tyr_progression:spellward`) | 16 | 32 | 32 | Reduce remaining damage *only* for exact minecraft:magic by 1% per rank (16% maximum); not fire/explosion or mod-caster inference. |
| **Arcane Attunement** (`tyr_progression:arcane_attunement`) | 10 | 20 | 20 | +0.5%/rank native casting speed/cooldown throughput where provider supports it (5%). |
| **Meditative Elixir** (`tyr_progression:meditative_elixir`) | 10 | 20 | 20 | Extend all finite beneficial MobEffect durations applied to the Monk, from any source, by 2% per rank (+20%). |
| **Inner Fortune** (`tyr_progression:inner_fortune`) | 10 | 10 | 10 | +1 true Luck attribute per rank (+10); not Fortune or Looting. |
| **Alchemical Ward** (`tyr_progression:alchemical_ward`) | 10 | 20 | 20 | Reduce remaining incoming minecraft:indirect_magic damage by 1% per rank, up to 10%. |
| **Withering Ward** (`tyr_progression:withering_ward`) | 5 | 10 | 10 | Reduce remaining incoming minecraft:wither damage by 1% per rank, up to 5%. |
| **Dragonbreath Ward** (`tyr_progression:dragonbreath_ward`) | 5 | 10 | 10 | Reduce remaining incoming minecraft:dragon_breath damage by 1% per rank, up to 5%. |

## Activation and source testing boundaries

- The **current live-file candidate** still has rank prices greater than one for some perks. The rule is now recorded in the **design blueprint**, not silently pushed into the already compiled Monk source. Activation requires a deliberate dedicated source/config PR and adjustments to any per-rank effects and purchase UI.
- In the loader's configured skill pricing, `base_cost=1,step_cost=0` or `rank_costs=[1,…]` must apply universally to purchased ranks after approved migration. Stage/Choice gates remain distinct from rank prices.
- Refund/paid receipt accounting, lifetime MXP P, Newbie veto, first-purchase Enlightenment and first-time boss exemptions must be maintained; no backdoor skill-point inflation.
- Keep native Combat/Fortune and Monk baseline skills intact. Six previous Monk holds have already been resolved or removed in config PR #486, which is separate from universal pricing migration.
- Source tests should assert exactly 38 paid Monk skills, 1,000 priced rank purchases, 29 free abilities, original skill effects at max, real one-point deductions on every intermediate rank, and no multi-point purchases or exploitative refunds.

**Related:** [Monk implementation #486](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/486), [remaining Path budget #495](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/495), [unified MXP #475](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/475).
