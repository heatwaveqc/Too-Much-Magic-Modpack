# #474 — Astral Sorcery native perk point tokens purchased with shared MXP

**Scope:** first-party Tyr Progression external reward, no new installed mod, no new XP wallet or Path tree, no change to Astral's perk XP/level or research unlock. The native resource is a **unique token adding a bonus allocation point**, not a perk rank or an active skill. `dev/progression` is the target; the shared branch remains owner-only.

## Owner-approved policy and configurable purchase

Pack-provided declarative resource:

`data/tyr_progression/tyr_progression/external_rewards/astral_perk_point.json`

```json
{
  "kind": "astral_perk_token",
  "currency": "tyr_progression:mxp",
  "base_points": 0,
  "step_points": 0,
  "max_purchases": 0
}
```

- `base_points=0` **explicitly disables new purchases**. The owner has approved *purchasing* but has **not chosen a price**. This safe shipped default avoids fabricating an MXP balance decision.
- Once configured `base_points>0`, price for the `N`th *already purchased* token is `base_points + N × step_points` **whole MXP mastery points**. Currently the generic XP ledger earns **one point per 1,000 MXP**, so these are spendable levels, *not raw fractional XP*. Existing Progression current-XP and paid-rank accounting remains authoritative. Overflow, negative values, unexpected fields and unknown reward kind/currency fail closed. `step_points=0` means flat prices.
- `max_purchases=0` means unlimited except the core engine's 1,000,000-rank safety cap. Positive values cap only further purchases; already-paid tokens are never lost when the config changes.
- A datapack can override the JSON; `/reload` validates the offer and economy together. **No price is taken from a client packet.**

## Transaction and native API

1. Confirm online logical server survival/adventure player, not Newbie/creative/spectator/fake; verify exact Astral 2.0.3 family is loaded and native research is initialized with a real attuned constellation (the perk tree's native unlock).
2. Compute the next token ID by counting only permanent paid purchases in `ExperienceEconomy.PaidRank(path=tyr_progression:external, skill=tyr_progression:astral_perk_point, system=tyr_progression:mxp)`. This synthetic path is intentionally never enrollable and is **not refunded by a real Path respec/switch**.
3. Validate the shared unspent MXP mastery points and cap. Write one *new paid rank* to Progression's existing player ledger before the optional Astral native grant (recoverable if a process dies between authorities).
4. Call public `ResearchHelper.givePerkPointToken(ServerPlayer, ResourceLocation)` for `tyr_progression:astral_purchase_000001` (then ...000002, etc). This API returns false on duplicate, schedules native save and sends native client sync, and leaves native perk XP and maximum perk level untouched. Use reflection to keep Astral optional, no direct `PlayerPerkData` mutations. If grant fails without a token, revert the one pending paid rank; if native already has a token, retain its paid receipt.
5. Reconcile paid receipts after login and every 100 ticks while attuned (max 32 restorations per pass): restore only missing purchased native tokens without another debit. On a full Astral research wipe, **do not restore while unattuned**; re-attunement re-enables paid tokens. Native duplicates are skipped. Orphaned `tyr_progression:astral_purchase_[0-9]+` native tokens beyond one's paid receipt count are revoked; an interrupted commit must never become a free point. A native API failure leaves Progression's authoritative paid receipts recoverable, not a second reward.

UI: the existing Progression screen shows an optional Astral +1 button at ordinary desktop widths using **server-computed price/buyability**, with the normal authenticated `menu_mutate` request. Fallback/QA command: `/tyrprogress astral status` and `/tyrprogress astral buy`, non-operator and subject to the same gating. The command is not a creative/admin mint.

## Type-A provenance, accepted source scope

- Pack pin: `mods/astral-sorcery.pw.toml`, `AstralSorcery-2.0.3.59.jar`, CurseForge file **9281073**, SHA-1 **`66f36655eae7cb404c9c16da6f33db31dff6ed9a`**.
- [Exact pinned JAR ABI GitHub Actions](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/45267979857): **PASS**, downloaded official `edge.forgecdn.net` bytes, matched frozen SHA-1; inspected actual methods on `ResearchHelper`, `ResearchManager`, `PlayerProgress`, and `PlayerPerkData` with Java 21 `javap`. The historical pre-release source commit `HellFirePvP/AstralSorcery@b8c6ab67828f358bf6dff108146fe6964fa81924` independently shows the same methods.
- [Progression Java 21 Gradle build and unit tests](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/45267604905): earlier source HEAD PASS (recheck at latest feature HEAD). No gameplay server/client or integration assembly.
- No bundled Astral code/JAR/assets and no changes to the pinned binary or native research tree.

## Handoff and separate queues

**Code-ready (source implementation):** declarative offer parser, native adapter, purchase/rollback/repair, path-independent ledger, command/UI, pure policy tests; install reviewable PR into the designated `dev/progression` writer lane. **Owner balance decision remaining:** select `base_points` and optional `step_points`. Leave the pack default disabled until selected.

**QA B/C after owner-authorized inclusion only:** unset price/no charge; set price and afford/insufficient/capped; Newbie blocked, graduate enabled; genuine Astral attunement; first and second token exact MXP point reduction and native allocation increase, unchanged native perk level; no double grant on duplicate network packet; native failure rollback; creative/fake/dedicated client cases; Path respec/leave/enter, research reset and subsequent attunement, restart/relog, two players, save ordering, and UI synchronization.

**Separate census AS-A01 in issue #474:** on next owner-authorized comprehensive QA candidate, count actual pinned native perk nodes, graph connectivity, exclusivity and true point cost using effective installed data and config. This code PR **does not claim that census is complete**. Do not substitute item count, a wiki or compile results for a real registry-derived census.
