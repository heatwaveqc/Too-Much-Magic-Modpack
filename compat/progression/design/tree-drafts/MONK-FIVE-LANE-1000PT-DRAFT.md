# Monk five-lane 1,000-point purchased tree — reconciled blueprint

**Design reference.** Runtime config is [PR #486](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/486); generic beneficial-duration and Fortune-mode engine support is [PR #485](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/485). No new executable module.

## Owner decisions incorporated

- The tree keeps **29 free automatic Monk abilities**, existing purchased Focus, and first-purchase Enlightenment (18 ranks × 1 point, growth divisor 1.020→1.002).
- **All five branches can be mastered**: **38 purchased nodes** for exactly **1,000 points**. Shared MXP is the default target after the separate three-mode economy work.
- **Stone Resonance and Overworld Bounty have been deleted** as redundant mining-speed/ore bonus inventions. No anti-farming restrictions are introduced.
- **Spellward is split into four exact vanilla incoming damage-type skills**. Nothing is classified by caster mod, projectile appearance, or abstract 'magic' source: `minecraft:magic`, `minecraft:indirect_magic`, `minecraft:wither`, `minecraft:dragon_breath`. Elemental Tempering already reduces `#minecraft:is_fire` and `#minecraft:is_explosion` damage types separately.
- **Meditative Elixir** is now +2% per rank (20% max) to all finite, beneficial status effects received while active, regardless of source. It does not extend harmful/neutral or infinite effects and has no potion-source detection.
- **Fortunate Hands** reuses the existing `MonkFortuneMixin`/`NativeLootBonuses` connector; +0.3 level/rank up to +3, Fortune mode only; Silk Touch receives no virtual Fortune. **Piercing Palm** reuses conditional `apothic_attributes:armor_shred` attribute at +1%/rank; effective registry and fight parity must be verified in game.
- Native Monk initiation, First Sunset, unarmored/empty-hand conditions and Curios semantics are preserved. Changes stay data-only in Monk config; reusable engine code remains under `feature/progression-*`.

## Exact budget

| Branch | Purchased skills | Points |
|---|---:|---:|
| Martial Body | 8 | 200 |
| Conditioning | 6 | 200 |
| Footwork | 7 | 150 |
| Hands As Tools | 5 | 110 |
| Magical Cultivation | 12 | 340 |
| **Total** | **38** | **1000** |

## Martial Body

| Skill | Ranks | Rank prices | Point cost | Dependency | Intended effect |
|---|---:|---|---:|---|---|
| Empty Hand Force (`tyr_progression:empty_hand_force`) | 50 | 1 | 50 | None | +0.30 unarmed damage per rank (+15 max), no normal armor. |
| Flowing Counter (`tyr_progression:flowing_counter`) | 20 | 2 | 40 | tyr_progression:empty_hand_force | Retaliation against real hostile melee hits, capped 10 damage, 40-tick cooldown. |
| Empty Hand Precision (`tyr_progression:empty_hand_precision`) | 20 | 2 | 40 | tyr_progression:empty_hand_force | +1 percentage point/rank chance of 1.5x fist strike (20% max). |
| Fist Tempo (`tyr_progression:fist_tempo`) | 20 | 1 | 20 | tyr_progression:empty_hand_force | +0.5% fist attack speed/rank (10% max). |
| Piercing Palm (`tyr_progression:piercing_palm`) | 20 | 1 | 20 | tyr_progression:flowing_counter | +1%/rank bounded armor penetration on actual fist strikes (20% max). |
| Paragon Force (`tyr_progression:paragon_force`) | 10 | 2 | 20 | tyr_progression:empty_hand_precision | +2%/rank final fist damage (20% max), not duplicate of native Iron Palm. |
| Pressure Points (`tyr_progression:pressure_points`) | 5 | 1 | 5 | tyr_progression:empty_hand_precision | +1%/rank chance of short Slowness after real fist hit (5% max), bosses excluded. |
| Empty Hand Paragon (`tyr_progression:empty_hand_paragon`) | 1 | 5 | 5 | tyr_progression:paragon_force | +5 fist damage and +3 armor unarmored; Curios allowed; Dragon + late milestones required. |

## Conditioning

| Skill | Ranks | Rank prices | Point cost | Dependency | Intended effect |
|---|---:|---|---:|---|---|
| Natural Poise (`tyr_progression:natural_poise`) | 40 | 1 | 40 | None | +0.4 natural armor/rank (+16); effective defense audited versus already free armor. |
| Iron Body (`tyr_progression:iron_body`) | 40 | 1 | 40 | tyr_progression:natural_poise | +0.25 armor toughness/rank (+10), no normal armor. |
| Inner Vitality (`tyr_progression:inner_vitality`) | 40 | 1 | 40 | tyr_progression:natural_poise | +0.5 maximum health/rank (+20 HP), no normal armor. |
| Quick Recovery (`tyr_progression:quick_recovery`) | 20 | 2 | 40 | tyr_progression:inner_vitality | +0.1 HP per second/rank passive regeneration (+2 HP/s). |
| Elemental Tempering (`tyr_progression:elemental_tempering`) | 20 | 1 | 20 | tyr_progression:iron_body | Reduce damage based on vanilla #minecraft:is_fire and #minecraft:is_explosion tags, independently of four direct vanilla-type wards. No magic-source inference. |
| Hunger Mastery (`tyr_progression:hunger_mastery`) | 20 | 1 | 20 | tyr_progression:inner_vitality | +1%/rank reduction in remaining exhaustion (20% max), above free Hunger Discipline. |

## Footwork

| Skill | Ranks | Rank prices | Point cost | Dependency | Intended effect |
|---|---:|---|---:|---|---|
| Focus (`tyr_progression:focus`) | 5 | 2+3+4+5+6 | 20 | tyr_progression:steady_hands | Existing paid Focus: +5% base movement speed/rank; preserve current native rank/Steady Hands gate until economy migration. |
| Body Speed (`tyr_progression:body_speed`) | 30 | 1 | 30 | None | +0.4%/rank base movement speed (+12%), conditionally unarmored. |
| Balanced Landing (`tyr_progression:balanced_landing`) | 20 | 1 | 20 | tyr_progression:body_speed | +1%/rank reduction of remaining fall damage (20% max), after free Feather Fall. |
| Evasion (`tyr_progression:evasion`) | 20 | 1 | 20 | tyr_progression:body_speed | +0.5%/rank dodge against eligible direct attacks (10% max), not an immunity. |
| Glide Control (`tyr_progression:glide_control`) | 20 | 1 | 20 | tyr_progression:balanced_landing | +1%/rank post-Dragon Momentum Glide control (20% max). |
| Momentum Step (`tyr_progression:momentum_step`) | 20 | 1 | 20 | tyr_progression:body_speed | +1%/rank sprint/strafe control bonus (20% max), no creative flight. |
| Graceful Step (`tyr_progression:graceful_step`) | 20 | 1 | 20 | None | +0.015 knockback resistance/rank (0.30 maximum). |

## Hands As Tools

| Skill | Ranks | Rank prices | Point cost | Dependency | Intended effect |
|---|---:|---|---:|---|---|
| Barehand Efficiency (`tyr_progression:barehand_efficiency`) | 30 | 1 | 30 | None | +5%/rank legitimate final fist mining speed (+150% max), above free Hand Mining. |
| Fortunate Hands (`tyr_progression:fortunate_hands`) | 10 | 2 | 20 | tyr_progression:barehand_efficiency | +0.3 Fortune/rank (max +3), Treasure Hand Fortune mode only, not Silk mode. |
| Looting Palm (`tyr_progression:looting_palm`) | 20 | 1 | 20 | tyr_progression:fortunate_hands | +1.25%/rank chance for one extra ordinary mob drop on direct fist kill (25%), no boss loot. |
| Toolless Reach (`tyr_progression:toolless_reach`) | 20 | 1 | 20 | None | +0.075 block reach/rank (+1.5 blocks), while fist is empty. |
| Worldly Cultivation (`tyr_progression:worldly_cultivation`) | 20 | 1 | 20 | tyr_progression:toolless_reach | +1%/rank chance of additional ordinary mature-crop drop (20%). |

## Magical Cultivation

| Skill | Ranks | Rank prices | Point cost | Dependency | Intended effect |
|---|---:|---|---:|---|---|
| Enlightenment (`tyr_progression:enlightenment`) | 18 | 1 | 18 | None | FIRST purchasable immediately after Monk initiation: -0.001 future ordinary global MXP growth exponent per rank, 1.020 -> 1.002 at 18/18; not retroactive. |
| Inner Reservoir (`tyr_progression:inner_reservoir`) | 30 | 2 | 60 | tyr_progression:enlightenment | Extends every global Core Reservoir capacity family, much higher personal mana/EV/Psi endgame limits; no external tanks. |
| Inner Wellspring (`tyr_progression:inner_wellspring`) | 30 | 2 | 60 | tyr_progression:enlightenment | Extends every global Core Wellspring regeneration family, improved personal recovery. |
| Inner Thrift (`tyr_progression:inner_thrift`) | 20 | 2 | 40 | tyr_progression:inner_reservoir | Extends every Core Thrift efficiency family; provisional +1%/rank bounded actual cost discount (20%), MXP tracks nominal. |
| Inner Potency (`tyr_progression:inner_potency`) | 20 | 2 | 40 | tyr_progression:inner_wellspring | Extends every Core Potency family; provisional +1%/rank magic potency (20%). |
| Spellward (`tyr_progression:spellward`) | 16 | 2 | 32 | tyr_progression:inner_thrift | Reduce remaining damage *only* for exact minecraft:magic by 1% per rank (16% maximum); not fire/explosion or mod-caster inference. |
| Arcane Attunement (`tyr_progression:arcane_attunement`) | 10 | 2 | 20 | tyr_progression:inner_potency | +0.5%/rank native casting speed/cooldown throughput where provider supports it (5%). |
| Meditative Elixir (`tyr_progression:meditative_elixir`) | 10 | 2 | 20 | tyr_progression:inner_wellspring | Extend all finite beneficial MobEffect durations applied to the Monk, from any source, by 2% per rank (+20%). |
| Inner Fortune (`tyr_progression:inner_fortune`) | 10 | 1 | 10 | tyr_progression:spellward | +1 true Luck attribute per rank (+10); not Fortune or Looting. |
| Alchemical Ward (`tyr_progression:alchemical_ward`) | 10 | 2 | 20 | tyr_progression:spellward | Reduce remaining incoming minecraft:indirect_magic damage by 1% per rank, up to 10%. |
| Withering Ward (`tyr_progression:withering_ward`) | 5 | 2 | 10 | tyr_progression:spellward | Reduce remaining incoming minecraft:wither damage by 1% per rank, up to 5%. |
| Dragonbreath Ward (`tyr_progression:dragonbreath_ward`) | 5 | 2 | 10 | tyr_progression:spellward | Reduce remaining incoming minecraft:dragon_breath damage by 1% per rank, up to 5%. |

## Code crosswalk and acceptance boundaries

- Existing 30 native Monk abilities and historic 18 retired Tyz specializations were reconciled in [PR #481](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/481), with 29 additional purchasable active effects using already-implemented generic handlers. [PR #486](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/486) restores the seven previously held/new ward purchases, bringing runtime config to 38 paid skills and 29 automatic skills.
- Generic engine source [PR #485](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/485) implements reusable `beneficial_duration_bonus` and Fortune/Silk mode guard; the exact 4 wards reuse existing `damage_multiplier` scoped by exact `damage_tag`.
- **Source compilation and module CI are not gameplay acceptance.** Player must test rank purchase prices, per-source DR correctness, not-twice-applied status extension on refresh, world save/reload, Dragon gates, actual Apothic Attributes player instance, armor restrictions, Curios, and Fortune/Silk exclusivity.
- **Not solved by this feature:** unified/hybrid/separate global MXP economy #475, post-commit no-drop stone #59, hardness-linear all-block farming MXP, and full endgame 85% performance audit. Integration promotion remains on the authorized next-build writer.
