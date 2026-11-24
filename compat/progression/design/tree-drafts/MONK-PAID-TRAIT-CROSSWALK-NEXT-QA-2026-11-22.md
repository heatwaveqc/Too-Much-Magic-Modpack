# Monk paid-tree reconciliation and next-build QA handoff

**2026-11-22. Exact branch:** `feature/monk-crosswalk-traits-next-qa-20261122` (stacked on [Enlightenment config PR #480](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/480), itself stacked on engine [#479](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/479)). Target after dependency merge: `dev/progression`; test integration: `test/full-integration` only by its designated integration writer, through frozen source PR.

## Status at this commit

- **60 live candidate Monk skill definitions**: preserve **29 existing free automatic** abilities; preserve purchased `Focus` and new first-purchase `Enlightenment`; add **29** code-backed purchased traits via live `paths/monk.json`. No Java gameplay logic added here.
- **37 full-tree paid nodes** in [design PR #478](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/478); of these, **31 now have active definitions**, six held. The 31 purchased nodes total **868 MXP points**; held costs sum to **132**; complete target remains **1,000**.
- All new purchases use `xp_channel:tyr_progression:mxp`, gated by the existing native `tyr_progression:monk` level until the three-mode XP redesign #475 is implemented. Level-1 entry is reachable after First Sunset. The old 29 free abilities stay free.
- Historical source values are retained at comparable maxima with finer per-rank increments for Force, Counter, Poise and Body Speed. Where the generic handler has a different effect contract (Balanced Landing and several magic providers), the deviation is recorded and must be accepted or changed after live testing.
- **Not a gameplay PASS.** Compile/static checks need to be read from CI; in-world QA still required. Runtime source is never installed just by merging this PR.

## Every proposed purchased skill (crosswalk against complete 1,000-point blueprint)

| Skill | Branch | Point cost | Status | Live backend / hold reason |
|---|---|---:|---|---|
| Empty Hand Force | Martial Body | 50 | qa_candidate | attribute |
| Flowing Counter | Martial Body | 40 | qa_candidate | counter_attack |
| Empty Hand Precision | Martial Body | 40 | qa_candidate | critical_hit |
| Fist Tempo | Martial Body | 20 | qa_candidate | attribute |
| Piercing Palm | Martial Body | 20 | held | No generic armor-penetration handler established; cannot pretend damage bonuses pierce armor. |
| Paragon Force | Martial Body | 20 | qa_candidate | attribute |
| Pressure Points | Martial Body | 5 | qa_candidate | status_on_hit |
| Empty Hand Paragon | Martial Body | 5 | qa_candidate | attribute, attribute |
| Natural Poise | Conditioning | 40 | qa_candidate | attribute |
| Iron Body | Conditioning | 40 | qa_candidate | attribute |
| Inner Vitality | Conditioning | 40 | qa_candidate | attribute |
| Quick Recovery | Conditioning | 40 | qa_candidate | delayed_regeneration |
| Elemental Tempering | Conditioning | 20 | qa_candidate | damage_multiplier, damage_multiplier |
| Hunger Mastery | Conditioning | 20 | qa_candidate | hunger_efficiency |
| Focus | Footwork | 20 | already_present | attribute |
| Body Speed | Footwork | 30 | qa_candidate | attribute |
| Balanced Landing | Footwork | 20 | qa_candidate | fall_reduction |
| Evasion | Footwork | 20 | qa_candidate | evasion |
| Glide Control | Footwork | 20 | qa_candidate | glide_control_bonus |
| Momentum Step | Footwork | 20 | qa_candidate | attribute |
| Graceful Step | Footwork | 20 | qa_candidate | attribute |
| Barehand Efficiency | Hands As Tools | 30 | qa_candidate | break_speed |
| Fortunate Hands | Hands As Tools | 20 | held | Virtual Fortune ignores the existing Treasure Hand Silk toggle; adding ranks now could pay Fortune in Silk mode. |
| Looting Palm | Hands As Tools | 20 | qa_candidate | ordinary_drop_bonus |
| Toolless Reach | Hands As Tools | 20 | qa_candidate | attribute |
| Worldly Cultivation | Hands As Tools | 20 | qa_candidate | crop_bonus, crop_bonus, crop_bonus, crop_bonus |
| Stone Resonance | Hands As Tools | 20 | held | Current block break-speed effect supports block selectors, not data-driven original-hardness thresholds. |
| Overworld Bounty | Hands As Tools | 20 | held | Available ordinary_drop_bonus works on monsters, not ore blocks; source block loot adapter required. |
| Enlightenment | Magical Cultivation | 18 | already_present | xp_growth_reduction |
| Inner Reservoir | Magical Cultivation | 60 | qa_candidate | magic_stat, magic_stat, magic_stat |
| Inner Wellspring | Magical Cultivation | 60 | qa_candidate | magic_stat, magic_stat, magic_stat, magic_stat |
| Inner Thrift | Magical Cultivation | 40 | qa_candidate | magic_stat, magic_stat |
| Inner Potency | Magical Cultivation | 40 | qa_candidate | magic_stat, magic_stat, magic_stat, magic_stat |
| Spellward | Magical Cultivation | 32 | held | Common magic resistance tag and cross-mod damage-source semantics unverified against pinned providers. |
| Arcane Attunement | Magical Cultivation | 20 | qa_candidate | magic_stat, magic_stat |
| Meditative Elixir | Magical Cultivation | 20 | held | Beneficial consumed-potion duration increase does not yet have verified runtime support. |
| Inner Fortune | Magical Cultivation | 10 | qa_candidate | attribute |

## Historical 18 Tyz specialization reconciliation

Historical [PR #229](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/229) had **18 implemented purchased traits**, but the Tyz backend was retired during unified engine cutover; its Java bridge and trait definitions cannot simply be copied back into the live pack. We reuse the effect semantics wherever the current engine has a real handler, with explicit changes listed below.

| Historical Tyz skill | Current Monk destination | Reconciliation |
|---|---|---|
| Empty-Hand Force | `tyr_progression:empty_hand_force` | native attribute; 25 old ranks → 50 finer ranks, same +8.75 max |
| Flowing Counter | `tyr_progression:flowing_counter` | existing counter_attack; 10 old ranks → 20 finer ranks, same 4 damage/cooldown |
| Empty-Hand Precision | `tyr_progression:empty_hand_precision` | existing critical_hit, 10% at 1.5x max |
| Natural Poise | `tyr_progression:natural_poise` | native armor attribute; old +10 max preserved with 40 ranks |
| Hunger Discipline | `tyr_progression:hunger_mastery` | existing hunger_efficiency; complements free automatic Hunger Discipline, 40% of remaining exhaustion max |
| Fire & Blast Tempering | `tyr_progression:elemental_tempering` | existing damage_multiplier, 12.5% of remaining fire/explosion each max |
| Body Speed | `tyr_progression:body_speed` | native movement_speed attribute; old +0.075 absolute speed preserved with 30 ranks |
| Balanced Landing | `tyr_progression:balanced_landing` | existing fall_reduction is FLAT fall-distance removal (10 blocks max), not old percentage mitigation; review gameplay |
| Glide Control | `tyr_progression:glide_control` | existing glide_control_bonus, only with Dragon milestone/native gliding |
| Barehand Efficiency | `tyr_progression:barehand_efficiency` | existing break_speed; 2.5x final hand mining multiplier max |
| Fortunate Hands | `tyr_progression:fortunate_hands` | held until native Fortune/Silk exclusivity preserved |
| Looting Palm | `tyr_progression:looting_palm` | existing ordinary_drop_bonus, 20% for one safe vanilla ordinary mob drop with valid fist kill; temporary prerequisite |
| Monastic Mastery Learning | not in 37-node tree | not included in approved 37-node blueprint: native Monk-XP-only bonus conflicts with new unified MXP default; needs explicit economic reconciliation |
| Focused Advancement | not in 37-node tree | not included: grants native Monk points, bypassing unified MXP budget and refund protections; do not reactivate |
| Inner Fortune | `tyr_progression:inner_fortune` | native true Luck attribute (+10 max), distinct from Fortune/Looting |
| Inner Reservoir | `tyr_progression:inner_reservoir` | existing magic_stat aliases to native Ars/Iron/Psi personal capacity; some other provider families still need parity |
| Worldly Cultivation | `tyr_progression:worldly_cultivation` | existing crop_bonus, real mature vanilla wheat/carrots/potatoes/beetroots only, 20% one bonus item max |
| Empty-Hand Paragon | `tyr_progression:empty_hand_paragon` | existing conditional attribute modifiers +5 fist damage/+3 armor, Dragon + cross-lane paid prerequisites; Curios permitted |

## Testing by priority (next authorized comprehensive unstable build)

**A — Load and ownership:** exact new `tyr_progression` namespace; 60 Monk nodes, 29 free + 31 purchased, no duplicate IDs or rejected paths. Monk initiation and Newbie veto. Verify first purchasable Enlightenment rank, 18-rank effects, relative global-MXP level cost, respec/refund, non-Monk dormant, offline/relog, multiplayer isolation. Ensure no retired Tyz/standalone MXP second award engine.

**A — Basic manual trait acceptance:** operator grant native Monk rank and shared MXP for purchases; test Force, Counter (real hostile attacks), Precision (statistical damage), Poise armor, Body Speed, fist attack speed, passive health/toughness, hunger, fall damage, and Barehand Efficiency against actual player gear changes. Verify 5-rank baseline Focus still works and one currency pays exactly once for each purchase. Use disposable worlds.

**A — Safety/edge:** abilities sleep when normal armor, fist-only perks sleep when mainhand occupied, Curios remain allowed; precise attr cleanup after death/respawn/dimension/reload. No double incoming-damage modifiers, protected mobs, players/allies, reentrant counter, or invalid evasion refunds. At advanced gates, ensure Dragon-locked Glide Control and Paragon fail purchase before genuine kill and do not bypass creative-flight ban.

**B — Loot and magic:** test Looting Palm on actual hostile ordinary drops only (not bosses/quest keys), mature crop bonus on each of four named vanilla crops, normal treasure Fortune/Silk toggle without new Fortune ranks, source-native Ars/Iron/Psi/Aureal/Goety/Neo/Eidolon attributes and ordinary mana payment before cost discounts. Optional absent provider should fail cleanly with no fake effect.

**B — Mining earnings, EXISTING SEPARATE BLOCKER:** fist-destroy stone before hand harvest unlock (no cobblestone) MUST award correct practice; no duplicate logs/TreeChop or spell AoE; player-placed obsidian earns linear hardness MXP. Current drop-gated event path and XP-mode redesign (#475/#59) have not been fixed by this config-only PR. Do not call those tests PASS based on the 29 trait definitions.

**Hold tests for next iteration:** Fortunate Hands in Silk mode, Piercing Palm armor bypass, hardness-gated Stone Resonance, ore-block Bounty, cross-provider Spellward, Meditative Elixir. These six are **not** in live purchased skill list; no misleading no-op purchases. Also old Monastic Mastery Learning and Focused Advancement are intentionally not restored under unified MXP until economy design reconciliation.

## Integration promotion and owner-only execution

1. Merge or frozen-select generic growth engine #479; then stack/retarget Monk Enlightenment #480; then merge or frozen-select this PR's isolated Monk JSON and handoff. Do not replay pre-rename PR #379 wholesale.
2. **Register this explicit ready-for-next-build request** in `docs/qa/LIVE-TEST-INCLUSION-REGISTER.md` on integration writer's branch; freeze exact source commit and reconcile with current `manifest/integration-sources.json`. Status per feature INCLUDED/BLOCKED/OWNER-DEFERRED; do not silently omit.
3. Run repository/module CI and read-only integration preflight. Owner-only authorized integration assembly / server smoke / Prism QA package remain separate levels. No gameplay PASS until actual disposable-world test ledger is filled.

Related: [#271](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/271), [#475](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/475), [#59](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/59).
