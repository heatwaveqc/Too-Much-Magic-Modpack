# Configurable XP systems and cross-currency skill ranks — 2026-11-23

**Status:** Owner-approved economy direction and design specification; design-only PR, **not implemented, built or gameplay tested**. Supersedes earlier "three XP modes" as fixed engine alternatives, the historical/lifetime MXP-level growth divisor, and the default dormant-purchased-Path-ranks assumption. Tracks #455 and #475. Keep the Progression implementation solely under `compat/progression`; Path packs supply data, not Java.

## Owner-approved universal configuration rule — 2026-11-23

> **If a rule determines what a player can earn, buy, unlock, retain, combine, or lose, it should generally be configurable. If it determines whether an action is authentic, safe and correctly synchronized, it belongs in the engine.**

Treat this as a **general Progression architecture principle**, not an invitation to create untrusted script execution. Implement one generic server-authoritative mechanism per kind of rule and bind gameplay content to it through validated data. Native compatibility adapters may be coded, but their mappings and gameplay thresholds should be data-configurable when safe.

**Consolidated P0/P1 scope:** arbitrary XP ledgers/source routing (without special-casing MXP), current-XP-derived purchasing and levels, multiple currencies and caps per skill, Path membership compatibility/replacement and configurable enter/leave/respec loss policy, achievement/level/flag-based automatic unlocks, and composable experience-growth/earning modifiers (target, priority, stacking/caps). TMM shipped preset remains the owner-locked one-wallet MXP configuration with destructive loss of departed Path investments and unallocated XP.

**Defer to follow-up slices:** boss reward entity/advancement catalogs and rarity bands; general trusted advancement adapters replacing the Monk-only Tyz marker bridge; Newbie-protected command list and other baseline convenience policy. These should be data where suitable, but **not** a reason to delay the central engine cutover.

**Safety boundaries remain code-owned:** authoritative event receipts, legitimate committed actions and cancellation/protection checks, non-replayable first-completion rewards, anti-overflow and finite-growth limits, atomic transactions, persist/clone/reload correctness, native mod version checks, effect scheduling and client synchronization. Bounded parsing and maximum computational work are engine safeguards, not gameplay tuning defaults.

**Current source audit:** `XpChannel` reserves MXP via `source==null`; `ProgressionApi.join` implements one exclusive group; `ChannelEngine.view` derives auto ranks from wallet rank only; `ProgressionApi.respec` always refunds; `ExperienceBonuses` excludes MXP and caps +200%; `ExperienceGrowth` assumes additive-only reduction with a floor of 1; `BossRewardPolicy` has hardcoded boss advancement mappings and elite rarity values; `NewbieMode` hardcodes six command roots; `ProgressionApi.awardMonkTyzPointOnce` hardcodes Monk and rank 17. Existing JSON eligibility and effect handler registries already offer many expressive features; extend them rather than duplicating.

## 1. One generic XP system, arbitrarily many instances

An XP system is a named configuration-defined resource (for example `tyr_progression:mxp`, `tyr_progression:cultivation`, `tyr_progression:hunting`). There is no hard-coded distinction between generic, native, magic, hybrid or separate currencies. A TMM default can configure one MXP system buying all accessible trees; an alternate pack can configure any number of systems and bind any tree/skill to one or more of them. This configuration supersedes the earlier special-case `unified`, `hybrid`, `separate` economy-mode switch; those are **presets/possible configurations**, not three required separate execution paths.

For each XP system S, persist **current total XP** `xp[S]` with sufficient precision for fractional awards and named purchase-commitment receipts. `xp_per_point[S] > 0` is configurable (TMM default MXP 1000). Derived values, **never independent persisted sources of truth**:

- `level[S] = floor(xp[S] / xp_per_point[S])`.
- `committed[S] = sum(cost in S of currently retained purchased ranks)`.
- `available[S] = level[S] - committed[S]`, validated nonnegative.
- Buying a skill commits `cost` points to an owning tree/skill/XPS; **do not subtract XP** from `xp[S]`. Derived display levels can change only when XP itself changes (earnings or explicitly configured loss/rebase).
- XP source attribution is not skill-investment ownership: XP earned during Monk activity and invested into Core survives a Monk loss under the TMM default.

No duplicate persisted `magicLevels`, `rank` or independently mutable `points` should control currency growth or availability. Other native third-party provider progression (Dragon Survival/JAWR) remains provider-owned where applicable, with explicit adapters; never pretend an upstream provider's XP has automatically become an engine-owned system.

## 2. Earn-side growth, independent sources, and safe arithmetic

For an ordinary eligible server-verified award in system S, resolve effective growth `g(S, player, source, context, event)` at settlement time. The **configurable allowed effective range is 0.001 through 100, inclusive**, with finite positive input validation. Growth may change dynamically with owned/active abilities (e.g. Enlightenment), Path, conditions or explicit data rules; resolution must be server-authoritative, deterministic, inspectable and bounded. Reuse common effect eligibility rather than hardcode Monk. Distinguish growth changes from per-source earning-rate modifiers; retain existing owner-approved nonimplicit Path rate-stacking policy unless separately changed.

At derived current level P, routine base XP converts into effective XP using `g^P` as divisor. For a source award crossing an `xp_per_point` boundary, process the remainder against the next derived P. Fixed one-time reward settlement can bypass ordinary growth only where its source policy explicitly says so. Distinct independently qualifying source categories may each award the same currency once (e.g. mana debit + every verified qualifying block from spell mining); duplicate callbacks for the same source/operation must not. A source can explicitly credit multiple systems, never by an implicit one-to-one conversion. Newbie veto is enforced centrally per configured source policy and the global TMM gate.

**Numerical safety is part of the architecture.** `g < 1` creates increasingly cheap levels; the total base XP needed for an unbounded number of later levels is a *finite geometric series* at fixed g, so sufficiently large awards can imply infinitely many levels mathematically. `g = 100` rapidly creates huge divisors and can produce zero representable gain. Accept the entire configured factor range **without a hidden clamp to 1.0**, but define explicit configurable safety ceilings or reject unsafely large/non-finite awards/level transitions; use log-domain/precision-aware comparison where necessary. Do not silently mint infinity, produce NaN, wrap integer counters, or hide an arbitrary rebalancing multiplier. The exact ceiling/bounded-award policy and dynamic modifier conflict-resolution syntax are engineering/config decisions to document before runtime cutover.

## 3. Generic multi-Path transition policy

A transition is a validated change to active Path membership, including gameplay, commands, compatibility-forced removal and simultaneous compatible Paths. No-op join/leave is not a transition and must never trigger a wipe. Baseline Newbie/Normie lifecycle and graduation rules remain #455.

For each Path-to-XP-system binding, configure treatment of the *departed Path's purchased rank contributions*:

1. `retain`: keep the paid investment, but suppress its effects while the owning Path is inactive; rejoining can reactivate it.
2. `refund`: delete that Path's purchased contributions, release their committed points back into the owning XP system's available pool, keeping total XP unless further configured.
3. `forfeit`: delete purchased contributions irreversibly and reduce/rebase current XP according to configured loss formula, without refund.

Configure separately whether **unallocated full points and partial XP progress** survive any actual Path change or are discarded. TMM's default: **forfeit** lost-Path purchases, **discard all unallocated MXP**, and **rebase current MXP to exactly 1000 times the surviving purchased MXP points** (Core plus all other still-active compatible Paths). More generally, `xp[S] = xp_per_point[S] * surviving_committed[S]` for a configured rebase ledger. Example: 20 Monk MXP ranks + 15 Core MXP ranks + 2400 unallocated MXP gives 37,400 current MXP before, 15,000 afterward on losing Monk; new derived level 15, available points 0. The next growth divisor uses level 15 and the *currently active* effects, not historical XP earned while Enlightenment was active.

Preserve globally recorded personal achievements, milestone completions and deed counters through Path loss; free/automatic benefits are active only with an owning active Path, and return automatically on rejoin. Paid ranks never survive `forfeit`. Preserve source-level one-time payout receipts and replay/dedup history across resets. For `refund` or `retain`, respect the selected unallocated policy without making committed points negative. Unrelated active Paths and Core commitments must never be destroyed by a different Path's removal.

Apply the entire transition atomically: validate all affected Path memberships, resolve each lost tree's per-system receipts, update all affected ledgers, preserve invariant `available >= 0`, synchronize tag/effect/GUI/client state, and publish once. Failed transitions leave the previous state intact. Specify policies for `on_enter`, `on_exit`, `respec` and administrative change with the same canonical handler, but prevent administrative UI shortcuts from bypassing the configured penalties.

## 4. One skill purchased through multiple XP systems

**Approved design goal, feasibility under review in owner conversation.** Model each paid contribution as `(player, treeId, skillId, xpSystemId) -> purchasedRankCount` plus independent receipts/point commitments. A skill can list zero, one or many purchasing systems with distinct per-system rank caps, costs and gates. The effective *numeric* skill rank can be `min(total_cap, sum(active contributed purchased ranks) + applicable free automatic ranks)` by explicit `stack: additive` semantics. This produces a **single skill and one effect pipeline**, not cloned duplicate skills, wallets or second simultaneous abilities.

Illustrative *not shipped* data schema:

```json
{
  "id": "tyr_progression:wellspring",
  "effective_rank_cap": 8,
  "purchases": [
    {"system": "tyr_progression:mxp", "max_ranks": 5, "cost_per_rank": 1},
    {"system": "tyr_progression:cultivation", "max_ranks": 3, "cost_per_rank": 1}
  ],
  "rank_combine": "additive"
}
```

Example: 4 MXP + 3 Cultivation purchases yield effective rank 7 (subject to the overall cap). If the owning Path loses paid contributions from one system via a configured forfeit policy, recompute the skill's rank from **remaining valid contributions**. A system-scoped loss rule can remove a contribution without deleting contributions funded by another system only when the tree/path ownership and configured rules authorize that split. In TMM's default Path-loss rule, **all paid ranks in the lost Path disappear regardless of which currency funded them**; Core ranks survive regardless of which activities earned their XP. This distinction is essential.

**Guards / decisions to make explicit:**
- Per-system cap and global effective cap; reject purchases that cannot increase any currently allowed rank/effect rather than accepting point-burning no-ops. Validate cross-system rank prerequisites, mutually exclusive choices and purchase eligibility.
- Existing `Definition.Skill.maxRank`, `rankValues` and effect handlers assume **one purchased rank total**. Migrate to a *resolved effective rank* for additive numeric effects and explicit rules for non-stackable booleans, toggles, mutually exclusive modes, proc chances, per-rank value arrays and provider-imposed attribute caps. Never apply the same effect twice just because two systems paid for it.
- UI should display total effective ranks **and** separate source contributions, caps, balances and available points; action requests must name purchasing XP system; server validates.
- Refund/forfeit must use actual per-system receipts, not infer from total effective rank. Correct behavior when one contributor is lost/rebought, when aggregate caps are reached, when multiple Paths change at once, and when data is reloaded.
- No implicit currency exchange, no free cross-system level, no double-point award on a shared gameplay deed unless two independent source rules explicitly qualify.

## 5. Source gap inventory from dev/progression (2026-11-23)

Current code is **not yet compliant**. `Wallet` persists `xp`, `rank`, `points`, `magicLevels`, `magicProgress`; `MxpMath` uses lifetime levels, validates growth only `[1.0,1.2]`, and `Wallet.magic` forbids rewinds. `ChannelEngine.buy` buys **one** wallet/channel per skill and `Definition.Skill` stores one purchased rank; `ChannelConfig.Binding` stores one currency and one level system. `ProgressionMath.buy` consumes native `Track.points`. Core path has no live skills on current base; PR #379 has four pilot Core nodes. No source/CI/gameplay PASS is claimed.

## 6. Code-ready slices versus design/QA

### Ready for implementation after source reconciliation
- [ ] Generalize wallet/ledger to current-XP authority, derived level/available points and original receipt provenance, with high-precision effective award arithmetic.
- [ ] Generalize definitions/config parsing for any XP system count, XP-per-point, tree bindings and source routing, with stable namespace IDs and reload validation.
- [ ] Add dynamic growth resolver at central award boundary, declared `[0.001,100]` validation, deterministic growth snapshots, per-level crossing, fixed source behavior, and overflow/underflow/award safety.
- [ ] Implement atomic Path transition with configured `retain`/`refund`/`forfeit`, independent unallocated-XP treatment and achievement persistence.
- [ ] Generalize per-skill per-system purchased-rank ownership; aggregate valid contributions through **one** effect evaluation, with per-system purchase endpoints and purchase receipt tests.
- [ ] Provide declarative TMM preset for one MXP wallet, 1,000 MXP/point, Newbie veto, Path forfeiture/rebase and universal Core, while retaining configurable multi-wallet alternative.
- [ ] Avoid duplicate overlap with already merged Enlightenment #479, Monk data #480/#481/#486, Core candidate #379, and Transformations-owned hooks #470/#513/#515; isolated engine PRs target `dev/progression`, integration writer alone promotes to `test/full-integration`.

### Remaining design/balance details
- [ ] Choose explicit numeric engine safety ceiling / bounded behavior at extreme growth factors, without changing the allowed factor range.
- [ ] Define how multiple simultaneously matching dynamic growth modifiers resolve (including priority/composition) and the minimum required stable config schema.
- [ ] Specify numerical and non-numerical skill contribution behavior when multiple XP systems purchase the same skill, including whether additive ranks are the first implemented supported option.
- [ ] Per-system entry/exit/administrative policy defaults for non-TMM presets; do not invent new gameplay XP rates.

### Type A source tests and B/C gameplay queue
- [ ] Deterministic math at levels 0, 1, 1000 boundaries and factors 0.001, 1, 1.02, 100; fractional XP, dynamic modifier activation, fixed vs ordinary awards, fail-closed overflow/infinite award handling.
- [ ] Two currencies funding one skill with distinct caps; aggregate effects once, UI source labeling, cap enforcement; forfeiture vs refund vs retention across one/two active Paths.
- [ ] Persistent personal achievement and one-time payout receipts; core retention, no-op transitions, Newbie→Normie, administrative removal, relog/death/restart, old source migration policy consistent with no established worlds.
- [ ] Verified committed spell-mana and physical block awards remain independently additive and deduped.
- [ ] **B/C pending owner-authorized build and packaging:** actual UI, purchases, break/mining/ritual sources, Path exits/rejoins, commands and client/server desync. Source checks never substitute for gameplay QA.
