# Next comprehensive test — source reconciliation and unresolved blockers

**Review date:** 2026-11-16 (America/Phoenix). **Base branch:** `test/full-integration`. This report documents integration preparation, NOT a full build or client test. The protected branch's latest commit, source lock and CI outcomes are authoritative; do not infer QA from this dated memo.

## Completed and supported by lightweight CI

| Scope | Integration source / change | Result and next proof |
| --- | --- | --- |
| Monk native Fortune + Hunter eligible-prey Looting | [#247](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/247), merged `45193788` | Repository + Progression Module CI PASS; live loot, Silk/Glass Touch, player arrow kills still NOT RUN |
| Monk/Hunter K-key GUI, TreeChop per-log XP/dedup + HUD | [#248](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/248), merged `a9be57f`, selective reconciliation of old #223 | Repository + Progression Module CI PASS; graphical and TreeChop multiplayer acceptance NOT RUN |
| Cave/Center Dracolich visuals, model/skin, cosmetic network and client mixins | [#249](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/249), merged `848e4f1`, selective reconciliation of old #75 | Repository + JAWR-only transform/scaffold Module CI PASS; client animations/cure/armor NOT RUN |
| B19 17 mob Gateways, B20 magic trader, Caerbannog, quest-registry gate | [#250](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/250), merged `e99d17a` | Exact donor SHAs and owned paths selected for next manual assembly; actual JSON and runtime still NOT BUILT, server/JEI NOT RUN |
| Thermalily no cooldown / 75 mana-tick and ProjectE converter | [#251](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/251), merged `24ef0d7` | Botania 2.0.2 mixins merged in JAWR-only module, Module CI PASS; converter 0.1.4 exact source pin selected for manual assembly; gameplay NOT RUN |
| Old QA and design evidence | [#252](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/252), merged `eb67d86` | 18 research/playtest files preserved, no obsolete workflows or old packwiz source |
| Namespaced progression commands and bounded operator XP | [#253](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/253), merged `1b5be21` | Resource ID parser and `/tmmprogress admin xp <path> <amount>` source + Module CI PASS; in-game commands NOT RUN |
| Bundle Curios bag slots | This reconciliation, frozen original #90 commit `305bef61` | Three Curios slot/validator files and one localization key selected for manual assembly. **Do not import historical advancement/reward function**; current quest-claim-only bag behavior must remain |

The **ore-shop source collision** was reviewed in #250: donor adds one 18-offer vanilla ore category to five unchanged baseline categories. Preflight has a documented one-file override and exact baseline Git blob guard. Do not silently override later owner changes. B19/Caerbannog shared translations are now merged by key rather than replacing the entire language file.

## Explicit unresolved integration blockers — no silent deferrals

| Candidate | Exact blocker / action required | State |
| --- | --- | --- |
| [Newbie Mode / Smart Keep Inventory #241](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/241) | Reconcile significant conflicting progression + transformations Java, first-run FTB quest IDs/rewards and Tyz KeepSafe without losing current K-key GUI/Fortune/Dracolich. Smart Keep Inventory CF `7530253` (MC 1.21.1 NeoForge 1.0.1) **not installed or hash-pinned**; verify per-player Corail compatibility, existing saves, vanilla gliding and two-player death/XP. Prefab and progression recipe bans remain separate implementation targets. | **BLOCKED for comprehensive runtime inclusion** |
| [B16 BetterEnd + Iesnium #131](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/131) | Four exact compatible provider/dependency JAR hashes and live thallasium ore/Spirit Fire item IDs are not established in installed candidate; do not import historical manifest/index wholesale. | **BLOCKED — pins/registry** |
| [B17/B18 YUNG/Ars locators #135](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/135) | Source has recipes and tag proposals, but multiple exact approved mod pins and final structure registry/locator validation are unresolved. | **BLOCKED — pins/registry** |
| [Canonical blood/JAWR bottles #126](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/126) | JAWR interop Java must reconcile with post-retirement first-party transform architecture and all blood/fluid recipes with loaded registry. Avoid restoring TeamLapen recipes/pins. | **BLOCKED — ABI/runtime content** |
| [JAWR Tyz form skills #94](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/94) | Independent `compat/forms` JAR, Tyz content and retention behavior not included in existing **two-JAR** packaging, and no combined test. | **BLOCKED — third JAR / bridge** |
| [MXP growth #161](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/161) | Independent `compat/mxp` API/dispatcher source compiles but has no shipped runtime JAR, no end-to-end Tyz credit tests. | **BLOCKED — new JAR / dispatcher** |
| [Trader's Vengence #219](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/219) | Independent `compat/traders-vengence` compiles on branch, but it is not in the two-JAR packaging or full NPC multiplayer/hero-pardon acceptance. | **BLOCKED — new JAR / player test** |
| Gilded design, Lava Fishing, Nether Depths, My Nether's Delight | Gilded server class and fishing eligibility gates are not integrated; three desired mods are **disabled / unpinned**. Both lava-fishing mods need overlapping event/loot/XP/Angler's Hat checks. | **BLOCKED — functionality and mod pins** |

**No owner-deferred cases have been inferred here.** Blocked ready work remains visible. A test with these missing is a **partial QA candidate** unless the owner explicitly authorizes a narrower scope.

## Before deployment

1. Re-run **Repository CI, Module CI and Integration Preflight** against the final protected integration SHA. A quick PASS proves no Minecraft boot.
2. Run **Integration Assembly** only on explicit owner authorization, with frozen source SHAs. Confirm B19/B20/Caerbannog/Curios/ProjectE mods and data actually appear in the final packwiz source and first-party JAR list, not merely in `manifest/integration-sources.json`.
3. Inspect any new source collisions, mod dependencies, hashes, KubeJS/FTB quest registry and loaded client keybindings. Do not publish a partial build as comprehensive.
4. Ask separately before **Server Boot Smoke** (`full_smoke=true`) and **Prism QA Package**. Client Launch Smoke and Gameplay Acceptance remain separately evidenced.
5. Keep old known-good Prism snapshots and old survival saves untouched. Use disposable test worlds.

See [canonical test levels](TEST-LEVELS-AND-TERMINOLOGY.md), [QA status](CURRENT-TEST-STATUS.md), and [live-test inclusion register](LIVE-TEST-INCLUSION-REGISTER.md).
