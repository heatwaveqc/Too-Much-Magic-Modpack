# Tyz's Skills + MXP — external tier and Monk gating contract

**Design accepted:** 2026-11-14. **Status:** specification only; not yet implemented or runtime-tested. Target Minecraft 1.21.1 NeoForge. **Category layout and universal magic skill proposals:** [Core Magic + five transformation categories](TYZS-CORE-MAGIC-AND-FORM-TREES.md). **This is the authoritative planned progression interface, superseding earlier Pufferfish-specific MXP text.**

## User-approved decisions

1. **Only the cross-mod MXP award dispatcher may add ordinary XP to Tyz's Skills.** Disable Tyz's native skill XP earned from mining, kills, crafting, food, sleep, breeding, fishing, advancements, exploring biomes/dimensions, etc. Boss victories can still earn Tyz XP, but **only** via the MXP boss registry with first-time fixed / repeat scaled exceptions. Vanilla Minecraft XP remains independent.
2. Tyz's global XP / level / skill-point balance is the **intended unified wallet**. No separate magic-vs-combat XP currencies and no second magic-only point ledger. MXP is the global earning source, so players may invest their acquired skill points across any enabled Tyz category, subject to prerequisites and gates. This supersedes earlier 'Option B' deliberation.
3. A rank-1 purchase of a skill is sufficient to satisfy the native `skillPrerequisites` for the next skill. **Do not enforce previous node rank 99** just to unlock the following tier. Each tier/node still may have 1–99 ranks and independently configured SP prices.
4. **External locks remain mandatory** for advanced magic tiers and Monk integration. External requirements are evaluated on the server when a skill is purchased, in addition to Tyz's built-in prerequisites and skill-point price. They may depend on verified player advancements, stage flags, Monk state/tiers, ritual/boss completion, transformation compatibility, quest milestones, or other installed mod-owned state. Do not fabricate APIs; bind exact data/attributes from deployed mods.
5. Never treat existing skill ranks or point purchases as permission to bypass the pack's hard recipe / ProjectE/Chaos Guardian progression gates.

## Verified 1.21.1-branch Tyz API

- Java `TyzsSkillsAPI.xp().addXP(ServerPlayer, float)` and overload with overlay and limit flags; `TyzsSkillsAPI.sp().addSP(...)`; `TyzsSkillsAPI.skills().getSkillLevel(player, id)`, `tryBuySkill`, `tryRefundSkill`, and per-node levels. KubeJS global binding `TyzsSkills.xp().addXP(player, amount)` and skill managers.
- Native `SkillConfiguration.skillPrerequisites` checks whether `ownedSkillIds` **contains** prerequisite ID, not the number of levels invested; one rank suffices.
- `SkillActionEvent.PurchasePre` is **cancellable**, carries `ISkill` and `ServerPlayer`, fires before `SkillManager.tryBuySkill` deducts SP. Its KubeJS wrapper is `TyzsSkillsEvents.skill_purchase_pre` with `event.getSkill()`, `event.getPlayer()`, `event.cancel()`. Java `ISkill.getID()` is the exact 1.21.1 interface spelling. `skill_refund_pre` is likewise cancellable.
- `SkillManager.tryBulkBuy` posts PurchasePre **once for the whole bulk** and can then apply multiple ranks, while direct `setSkillLevel` and `addSkillLevel` mutate skill ranks **without PurchasePre**. Thus: enforce external tier gates at **node boundaries**, not across a threshold *inside* a single bulk-buyable node. If per-rank external gating is ever desired, prohibit bulk purchase on that node / use a controlled purchase adapter. Treat direct grants as trusted administrative or carefully validated internal actions only; test every bypass path.
- External rules are **not** reflected in native `Skill.isAvailable`, which only checks native level, owned skills and incompatibilities. A server-side denied purchase may visually look available until attempted; give the player a concrete missing-requirement message (KubeJS/packet/quest UI), and optionally add an overlay/tooltip integration later. Do not claim the native UI already displays arbitrary external gate conditions.
- A refund of a prerequisite may strand a later purchased node. For any strict dependency chain, use `RefundPre` to block an invalid refund while dependent node(s) remain owned, or explicitly cascade refunds if approved. Respec must preserve lifetime earned MXP points and first-discovery records.
- An external *unlock* is normally permanent once earned (advancement/stage milestone), but a persistent state-dependent Monk or Transformation effect can check the **current** state at effect time. Don't silently delete or unlearn existing skill ranks when a temporary form is lost; design state-specific ability operation separately from purchase gating.

## Tyz XP routing and level pool

- Use `config/tyzs_skills/custom/data/level-pool.json` to override global level curve with a single fallback entry: `"-1": { "goal": 1000, "reward": 1 }` (confirmed from `XpManager.loadPool` and `getLevelData` source). One effective 1,000 MXP grants one global Tyz level and its configured SP reward. Tyz's defaults award **variable XP/SP**, and must not be used silently.
- Keep Tyz's daily XP cap disabled (`xp_daily=-1`, the inspected source default), and avoid a total-level cap (`max_lvl=-1`), so fixed boss-repeat rewards and high-level MXP progression remain meaningful.
- Disable **all** Tyz native XP sources via common config: mining, kills, eating, and numeric rewards for sleeping, fishing, crafting, breeding, advancement task/goal/challenge, and new biome/dimension. Set each corresponding numeric reward to 0; inspect the actual pinned config syntax and final boot output.
- Consider a server-side `SkillXPChangeEvent` allowlist to cancel *unauthorized positive increases* outside our MXP dispatcher (e.g. other addons), while allowing authorized tests/admin operations explicitly. This is defense in depth and must not interfere with Tyz's legitimate internal XP level-up deductions, respec, or migrations. Prevent recursive events and avoid trusting client packets.
- MXP dispatcher retains all existing normalization: fixed 1,000-XP threshold, 1.2^P divisor for routine awards, undiscounted nominal spell cost, fractional accumulation, no duplicate resource transfer credits, fixed first ritual/boss rewards, and designated fixed repeat boss overrides (Chaos Guardian: proposed 100 MXP).
- **Important:** Tyz's `XpManager` uses `float` XP; retain high precision/fractional accounting in our server ledger until a safe representable award can be passed through the API. Recompute P after each level-up and prevent a large award from incorrectly using a stale divisor over multiple points. Tying P to Tyz's lifetime earned global levels requires a durable accounting record and migration check, not just current invested ranks.

## External gate example (illustrative, not production-verified Monk code)

```js
// kubejs/server_scripts/tyz_external_gates.js
TyzsSkillsEvents.skill_purchase_pre(event => {
  const id = event.getSkill().getID()
  const player = event.getPlayer()
  // Example: replace the placeholder with our validated server-side
  // Monk-stage / advancement / compatibility status checker.
  if (id === 'magic_capacity_tier_3' && !hasActualMonkTier3(player)) {
    event.cancel()
  }
})
```

The placeholder `hasActualMonkTier3` is not a real current API, and this file is only an architectural sketch. The production adapter should centralize predicates and provide a descriptive failure reason to the user. Java NeoForge listeners can enforce equivalent gates directly from the separate Monk mod, with cleaner typed state access.

## MVP tests

1. Completing ordinary mining, farming, vanilla kills, fishing, exploring and crafting generates **zero Tyz XP**. Eligible spellcasts, successful rituals, boss defeats via MXP do generate XP.
2. With 1,000 MXP, Tyz grants exactly the configured skill point(s). One rank in tier I unlocks the tier II prerequisite; tier I's other 98 ranks are not required.
3. Tier III with valid points but without Monk advancement is refused **before** SP debit. On obtaining external flag, same purchase works. The UI should report why it was blocked.
4. Attempt both single and bulk purchases; direct rank grants via admin command and API; refunds of a prerequisite with later tiers already owned; relog and death persistence.
5. Confirm whether unlock persists when Monk stage changes or transformation status toggles according to the intended specific gate semantics.
6. Validate XP divisor across levels with fractional, big and fixed exception awards; no default XP leak, no spurious XP modifier from native Tyz or other mods.

## Upstream evidence

- [Tyz's 1.21.1 source — Skill.java](https://github.com/Tyzbrand/TyzsSkills/blob/mc1.21.1-(main)/src/main/java/com/tyzsskills/impl/server/model/Skill.java)
- [SkillManager purchase, bulk and direct grant paths](https://github.com/Tyzbrand/TyzsSkills/blob/mc1.21.1-(main)/src/main/java/com/tyzsskills/impl/server/skills/SkillManager.java)
- [Cancellable purchase and refund events](https://github.com/Tyzbrand/TyzsSkills/blob/mc1.21.1-(main)/src/main/java/com/tyzsskills/api/events/SkillActionEvent.java)
- [Native KubeJS events guide](https://github.com/Tyzbrand/TyzsSkills/blob/mc1.21.1-(main)/docs/kubejs/kubejs_events.md)
- [LevelPoolPreset and XP manager](https://github.com/Tyzbrand/TyzsSkills/blob/mc1.21.1-(main)/src/main/java/com/tyzsskills/impl/server/Level/LevelPoolPreset.java)
- [Custom JSON settings and one-rank prerequisites](https://github.com/Tyzbrand/TyzsSkills/blob/mc1.21.1-(main)/docs/json-tweaks/skill-structure-on-tyzs-skills-6.4+.md)
