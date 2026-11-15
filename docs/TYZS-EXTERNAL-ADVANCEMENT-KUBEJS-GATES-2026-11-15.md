# Tyz 6.4.2 external progression gates: source-verified clarification (2026-11-15)

**Decision implication:** Tyz's Skills **can be locked by external advancement, mod-provided or pack-defined stage and player-tag checks**, using **its built-in KubeJS events** or the equivalent NeoForge Java events. A new compiled compatibility mod is **not required simply to lock purchases**. However, the conditions are *not declarative Tyz skill/category JSON options*: a server KubeJS script (which the pack already supports) must evaluate the conditions.

This is an **architecture verification, not runtime activation**. [Non-installed JavaScript demonstration](examples/tyzs-external-gate-pilot.js) uses known vanilla advancements as placeholders rather than misrepresenting JAWR's actual advancement IDs.

## Explicit source evidence

| Feature | Supported? | Mechanism and exact 6.4.2 source |
| --- | --- | --- |
| External server-side **individual skill** purchase gate | **Yes** | [`SkillManager.tryBuySkill`](https://github.com/Tyzbrand/TyzsSkills/blob/mc1.21.1-(main)/src/main/java/com/tyzsskills/impl/server/skills/SkillManager.java) posts cancellable `SkillActionEvent.PurchasePre` **before SP debit**, likewise `tryBulkBuy`. Inspect `event.getSkill().getID()`. |
| External server-side **entire category** purchase gate | **Yes, as predicate per skill** | `event.getSkill().getCategory()` checked in the same event; reject all purchases in `tmm_vampire`, `tmm_werewolf`, `tmm_monk`, etc. It does **not** lock/hide the tab itself. |
| Config-only prerequisites | **Yes** | `SkillConfiguration` JSON supports `skillPrerequisites`, `levelRequirement`, `incompatibleSkills`, `purchasable` and `visible`, but none accepts dynamic advancement / scoreboard / external identity conditions. |
| Completed Minecraft advancement | **Yes via KubeJS** | Player's [KubeJS 1.21.1 `isAdvancementDone(ResourceLocation)`](https://kubejs-wiki.tysontheember.dev/1.21.1/reference/types/dev.latvian.mods.kubejs.core.ServerPlayerKJS/) returns a Boolean. Unknown IDs evaluate false (safe denial). Exact vanilla advancements are suitable for the pilot; **JAWR IDs must be verified first**. |
| KubeJS player stage | **Yes via KubeJS** | `player.stages.has('key')`, stage persisted per player when legitimately granted; advancement can call `PlayerEvents.advancement(...)` to grant a stage. [1.21.1 Player data guide](https://kubejs-wiki.tysontheember.dev/1.21.1/guides/player-data/). |
| Vanilla per-player `/tag` flags | **Yes via KubeJS** | `player.getTags().contains('tag')`. A vanilla **entity-type tag** is categorically different and cannot identify a particular player or their current affliction. Tags must be set/revoked by authoritative code or commands; JAWR **does not automatically expose a selected TMM tag**. |
| KubeJS **without a new JAR** | **Yes** | [Tyz KubeJS event docs](https://github.com/Tyzbrand/TyzsSkills/blob/mc1.21.1-(main)/docs/kubejs/kubejs_events.md), the real [`TyzsKubePlugin.java`](https://github.com/Tyzbrand/TyzsSkills/blob/mc1.21.1-(main)/src/main/java/com/tyzsskills/integration/kubejs/TyzsKubePlugin.java), [`JsEventsDelegate.java`](https://github.com/Tyzbrand/TyzsSkills/blob/mc1.21.1-(main)/src/main/java/com/tyzsskills/integration/kubejs/JsEventsDelegate.java) and [`KubeJSBridge.java`](https://github.com/Tyzbrand/TyzsSkills/blob/mc1.21.1-(main)/src/main/java/com/tyzsskills/integration/kubejs/KubeJSBridge.java) prove a built-in `TyzsSkillsEvents.skill_purchase_pre` event that relays `event.cancel()` to NeoForge's cancellable pre-event. Pack already pins `kubejs-neoforge-2101.7.2-build.381`. |
| Dynamically hidden/locked category **tab** | **No native demonstrated support** | `Category`/`CategoryLoader` have static id/name/icon/order, and GUI's `SortingTools` enumerates global tab list; optional client UI patch required for per-player hidden tabs. A greyed/visible tab can still have all purchases refused by server. |
| Revoke **purchased effects** after curing/transformation | **No automatic capability** | Purchase checks do not remove unconditional `CUSTOM` stat modifiers. Need runtime JAWR-aware effect suppression or a deliberate design where form skills are *native JAWR* and Tyz perks are universal; cannot assume `isAdvancementDone` tests current affliction. |

### API spelling caveat

The upstream Tyz KubeJS docs example calls `event.getSkill().getId()`; the actual pinned 1.21.1 `ISkill` interface defines **`getID()`** (capital `ID`). Use the source-backed spelling; test in the pack.

## How a working no-JAR gate would look

In `kubejs/server_scripts/tyz_external_gates.js` (once activated and attached to **real**, validated mod advancement IDs):

```js
TyzsSkillsEvents.skill_purchase_pre(event => {
  const player = event.getPlayer()
  const skill = event.getSkill()
  if (skill.getCategory() === 'tmm_werewolf' &&
      !player.isAdvancementDone('minecraft:story/enter_the_nether')) {
    event.cancel()
    player.tell('Required advancement not yet completed.')
  }
})
```

This vanilla Nether achievement is an **illustrative test signal** and **must not** be the real Werewolf unlock rule. For *permanent* subclass achievement gating, a verified JAWR/Monk/quest advancement is appropriate. For **current vampire/wolf body state**, use a properly synced, authoritative flag or read the JAWR attachment with verified source-access logic; a one-time achievement alone is insufficient.

A more fully parameterized example also demonstrates a KubeJS persistent stage and a vanilla player tag: [`docs/examples/tyzs-external-gate-pilot.js`](examples/tyzs-external-gate-pilot.js).

## Mandatory acceptance/bypass tests

1. Player has enough global Tyz SP but **lacks** the selected advancement: buy category skill in normal UI -> denied before SP debit and no rank change. Verify useful denial explanation.
2. Grant the selected advancement via normal achievement path: same skill succeeds. Retest single rank and **bulk buy**. Test one node-level prerequisite separately.
3. Revoke or clear the tag/stage in a disposable test: future purchases blocked again. If the gate is intentionally an **earned-once** achievement, do not revoke it.
4. Test direct `TyzsSkills.skills().setSkillLevel` and `addSkillLevel`, plus admin `/skills` grants: these bypass PurchasePre (source-confirmed). Treat them as administrative mechanisms only or add a separate trusted reconciliation policy; do **not** present PurchasePre as a complete rank-mutation security boundary.
5. Refund a prerequisite while owning downstream skills: `TyzsSkillsEvents.skill_refund_pre` must forbid invalid dependency removal. The same event is used for bulk and single refunds.
6. Cure/retransform player with purchased form skills: **grant/permission and buff operation are separate**. If a Tyz rank is unconditionally modifying `minecraft:generic.max_health`, it will stay active after cure without an effect adapter. Preserving ranks and suspending effects needs a further mechanism.
7. Confirm locked categories stay **visible**, including the ALL/search views, unless a separate client UI patch is built. Give players a visible locked/unlocked explanation in quests/tooltips.
8. Verify in exact pinned 6.4.2 + KubeJS 2101.7.2 build.381; source and KubeJS docs alone do **not** establish a completed client/server smoke.

## Keep or replace Tyz?

**Keep Tyz if hard server-side purchase denial by advancement, stage or external tag satisfies the original acceptance rule.** No Java addon is needed for that part; KubeJS is already present. The single global MXP/Tyz point wallet still fits this path.

**Reconsider another framework only if the requirement demands native configuration-only `advancement:` predicates, automatic **per-player tab invisibility/category locks**, or form-gated attribute effects **without any bridging/scripting**. Pufferfish's Skills, our previous backend, explicitly exposes [`/puffish_skills category lock|unlock`](https://modrinth.com/mod/skills/), but that category/point architecture was one of the reasons we were moving to a single Tyz wallet. Evaluate exact 1.21.1 versions and MXP reconciliation before switching back.

**Separation:** No new Java *compat mod* is needed for purchase permissions; actual special abilities (native bite effects or wolf-only passive regeneration) may still require gameplay implementation. Gate feature availability does **not** imply the two JAWR/Tyz systems magically share effects.
