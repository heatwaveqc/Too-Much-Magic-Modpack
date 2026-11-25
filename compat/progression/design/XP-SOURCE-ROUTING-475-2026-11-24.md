# Configurable XP source routing — owner-approved #475 (2026-11-24)

The canonical accounting model is current XP, live floor(current XP / xp_per_point) = level, level - committed point cost = available points. Purchases record point commitments and never spend XP. Explicit Path departure/forfeiture is distinct from purchases. Generic XP is not inherently owned by a Path.

## Config
Optional economy.json fields (examples only; no shipped TMM changes):
```json
{"systems":[
 {"id":"example:general","xp_per_point":1000},
 {"id":"example:combat","xp_per_point":100,"earning_path":"example:hunter"},
 {"id":"example:athletics","default_reward_multiplier":0}],
 "source_routes":[
 {"source":"example:verified_kill","destinations":[
   {"system":"example:general"},{"system":"example:combat","multiplier":2}]},
 {"source":"example:verified_sprint","destinations":[{"system":"example:athletics"}]}]}
```
An explicit source category routes to zero, one or many configured systems, replacing (never adding to) a legacy fallback destination. Empty list disables the category. Missing route preserves legacy explicit fallback; a source-only caller without fallback pays nothing. Unique source and destination IDs, finite nonnegative rate 0..1e6, bounded route/destination counts, explicit systems must exist. No wildcard routing; category IDs are the authenticated source IDs. Source verifier chooses nominal measurement; destination rate scales resolved reward multiplier, hard capped at 1e6. Original nominal limits remain validated per destination.

Optional per-system earning_path is a hard gate: joined AND runtime eligible Path; unknown path fails closed. Hard gate creates no receipt; not a permanent XP or purchasing restriction. Optional default_reward_multiplier (default 1) applies before reward modifiers, even to fixed awards. Zero can be enabled by an eligible Path reward modifier with replace 1 (include_fixed:true if fixed should also be enabled). Zero-valued eligible payout settles receipt without minimum reward. This separates deliberate zero payouts from ineligible sources. Newbie/creative/spectator/fake-player veto remains server-side. No change to point-cost calculation.

## Ownership and runtime
Reuse #654's catalog, ExperienceModifiers.earn and canonical ExperienceEconomy.earnModified. ExperienceSourceRouter.award/ completion take a verified source and one immutable prior ledger, fan out per currency, select first/repeat independently and return one immutable result for caller publication. No second wallet or event listener. Existing Path-owned TrainingXpSpec, UnifiedMxpSource, ProgressionApi and ChannelEngine are #629's active writer files. No modifications to them here. They must adopt this router once per verified action (not one call per joined Path) with existing explicit destination as fallback, stable tx and receipt; preserve strict combat, consumption discovery, training rates, native legacy grant and eligibility. Direct administrative/explicit grants can use ExperienceModifiers.earn without rerouting, subject to hard system gate. Do not also credit the old destination or apply old growth/bonus separately. Unknown hard gate fails closed; cross-reference reload diagnostics separate. Rate multipliers currently use bounded doubles, settled XP remains BigDecimal.

Type A: source config/routing, multiple currency receipt independence, live level/purchase invariant, hard gate/default multiplier, duplicate/invalid configs. B/C after coordinated caller wiring and authorized build: one source two currencies, independent first/repeat, gate before/after joining/leaving, zero default with Path override, fixed treatment, relog, duplicate callbacks, unchanged TMM default. No full runtime build authorized.
