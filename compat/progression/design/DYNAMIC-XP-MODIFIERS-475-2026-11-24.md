# Dynamic XP modifier resolver (#475) — 2026-11-24

Owner approved generalization after the XP cap/minimum settlement design. Same PR #654 (economy catalog/ledger ownership); #629 is active writer of ProgressionApi, ChannelEngine, PathJson and MechanicalSpec. No overlapping changes there.

## Configuration
Optional `modifiers` array in economy.json. Empty/missing leaves existing balance untouched.
```json
{"id":"example:hunting_mastery","kind":"reward","system":"tyr_progression:mxp",
 "source":"example:verified_kill","path":"example:hunter","skill":"example:mastery",
 "min_rank":1,"operation":"add","value":0.25,"per_rank":true,
 "priority":10,"stack":false}
```
Example only; not a shipped balance change. IDs are unique. system/source default "*" (all); explicit system must exist in catalog. Path or persistent achievement required. Optional skill requires owning active eligible Path, skill eligibility and rank; optional effect_index requires matching currently active effect (inherits effect conditions, activation and hand/armor restrictions). Optional achievement is an AND gate against permanent personal ledger achievement. Missing runtime Path/skill/effect fails closed. No custom script predicates. Catalog validates syntax; full cross-reference reload diagnostics not yet wired.

Reward rules compose a multiplier starting at 1; growth rules compose configured factor. Operations add/multiply/replace. per_rank only on additive rules with skill; value multiplied by rank. Optional minimum/maximum constrain running result after operation, then hard limits apply (growth .001..100, reward 0..1e6). Highest priority chooses exclusive or explicit stack mode; an exclusive top-priority tie rejects modifier selection and preserves base. Stack mode combines only stack:true candidates, ascending priority then stable ID, so higher priority acts last. Fixed awards bypass growth and reward modifiers by default, reward rules can opt in include_fixed:true. Zero multiplier suppresses minimum reward as well; consumes legitimate receipt. No implicit currency exchange. Legacy xp_growth_reduction is aggregated within owning Path, priority 0 exclusive; normal single-Path Enlightenment preserved.

## Runtime boundary
Existing ExperienceGrowth.effective now resolves wildcard-source growth rules live. New ExperienceModifiers.earn(player, ledger, system, source, tx, firstCompletion, nominal, fixed, configuredGrowth) delegates to ExperienceEconomy.earnModified and computes both rules with actual source. It is not yet called by #629's shared award paths: source-scoped growth and all new reward rules require this final wiring. Do not apply both legacy ExperienceGrowth and new adapter or double-apply ExperienceBonuses. Coordinate with #629 writer: use adapter after first/repeat selection in magic; direct configured and activity/trial routes use same adapter; preserve authenticated eligibility/Newbie gates, dedup, native legacy grants and no implicit Path rate stacking. Existing native ExperienceBonuses untouched pending migration. No new rate shipped.

Type A tests: deterministic priority/conflict/explicit stacking/order, bounds, catalog parser, source/fixed targeting, multiplier before growth, cap/minimum and zero receipt. B/C after wiring/authorized inclusion: two compatible Paths competing and explicit stack, Enlightenment, source-specific reward, fixed bypass, no replay after relog. No full build authorized.
