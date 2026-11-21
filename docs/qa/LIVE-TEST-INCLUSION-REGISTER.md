# Next live-test inclusion register

**Policy:** "Ready for a live test" is a **request to attempt inclusion** in the **next owner-authorized comprehensive live test**, not permission to park a source branch as "deferred." This includes readiness reported in a development chat, handoff, PR, issue, or project note. The phrase can be equivalent: *ready to test*, *next test build*, *ready for in-game QA*, *prepared for the next full integration*, etc. The declaring agent must register the work in this file and link its source; the next integration agent must independently discover unregistered readiness claims too.

**This document is an intake and inclusion-decision ledger, NOT a pass report.** See [CURRENT-TEST-STATUS.md](CURRENT-TEST-STATUS.md) for what was actually tested and [NEXT-TEST-CODEX-CHECKLIST.md](NEXT-TEST-CODEX-CHECKLIST.md) for the live-test procedure. The authoritative runtime source lock is [manifest/integration-sources.json](../../manifest/integration-sources.json). **A prior `deferred_pending_runtime` entry is not an inclusion decision for a later comprehensive test.**

## Owner-required next QA pins — Dragon Survival and Bountiful (2026-11-19)

**REQUIRED inclusion attempt; pin-only reconciliation proposal.** The owner explicitly corrected the missing Dragon Survival pin (required to test the Dracolich skeletal model and skin) and requires Bountiful in the next comprehensive build. At inspected integration SHA `1afb7b418831f835e8e65683c2e16f5fbdba8af5`, neither mod nor Kambrik appears in active Packwiz/manifest. Restore Dragon Survival rather than selecting a new version; independently verify the exact provider artifact before assembly.

| Pin | Exact release | Dependency / runtime acceptance |
| --- | --- | --- |
| Dragon Survival | CurseForge project 420799 file 9539454, v2.0.74, SHA1 e11429dda4688711e174625bb7420873a255a715 (existing scripts/pin_next_test_mods.py) | GeckoLib 4.9.3 already pinned. Recheck bytes and metadata. Verify transformation, Cave/Center Dracolich skeletal model and skin, real form gate, relog, both acquisition orders, diet and other dragon species as applicable. |
| Bountiful | Modrinth BpwWFOVM/swTb6NU9, 8.0.0-beta.2 | Exact provider SHA512 and JAR metadata checked in [targeted audit 44613539906](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/44613539906). Beta JAR metadata declares Minecraft bare 1.21 despite provider 1.21.1 support; verify actual runtime compatibility. |
| Kambrik | Modrinth zfbCkvdZ/rCSmCZpX, 8.0.0-beta.2 | Required by Bountiful. Exact artifact verified in same audit; needs NeoForge >=21.1.194 and Kotlin for Forge >=5 (current 21.1.265 and 5.12.0). Percale 1.0.0-beta.1 is embedded, no separate pin. |

Bountiful integration [#365](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/365) / [PR #366](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/366) remains in development (custom reward pools, default-data suppression, recipes and balance/runtime acceptance pending). Pin independently to avoid losing the required mod again; **do not treat the stock bounty configuration or unfinished Bountiful behavior as approved survival gameplay**. Before owner-authorized comprehensive build, reconcile #366's reviewed runtime files with this exact pin and record blockers rather than silently omitting either mod. License metadata for Bountiful differs between project LGPL-3.0-only and JAR GPL 3.0; original-host download pin only, no redistribution. No assembly, full boot, Prism packaging or client acceptance authorized by this source change.

## Historical branch deferral reconciliation — 2026-11-19

The following five historical source PRs were replaced by reviewed, merged integration PRs. Their original branch tips are no longer required for source selection; the exact original closed PR head refs and current integration successors remain recorded in `manifest/integration-sources.json` and issue #356. The obsolete retirement-only entries are removed from `deferred_pending_runtime`, **not** from the next comprehensive QA scope.

Read-only review at integration commit `0e825c4f0d8142975dc38a0f1138480fb273b92d` verified all 5 BetterEnd, 28 B17/B18 and 18 canonical blood selected source paths, shared form catalog/bridge and MXP sources. The [November 18 installed artifact census](surveys-2026-11-18/installed-artifacts.json) establishes historical installation in that instance of the BetterEnd four-jar group, B17/B18 15-jar group, shared Progression, Transformations, MXP and Tyz 6.4.2 JARs. That snapshot is **not** proof that a new candidate includes all changes, that exact behavior was tested, or that the currently evolving unified progression engine has shipped.

| Original PR / integrated source | Next candidate obligation |
|---|---|
| #131 / #276 BetterEnd New Dawn | Verify exact compatible pins and current candidate inclusion; Thallasium ore/worldgen, Spirit Fire Iesnium recipe, dragon/portal interaction. YUNG Better End Island remains held separately. |
| #135 / #278 YUNG and Ars locators | Verify all 15 selected pins, native structure tags and Locate Structure rituals against live registry; worldgen, multiplayer, and End Island hold. |
| #126 / #282 Canonical ordinary blood | Verify all 18 selected resources and the current JAWR-aware Transformations implementation; actual recipe codecs, cauldron capabilities, component-protected variants, drinking and cross-mod multiplayer. No MXP from bottle/refill. |
| #94 / #279, #281 JAWR/Tyz forms | Validate current authorized shared engine successor, native form gates, persistence, retention and effects. Historical 37-skill inactive catalog retained; no activation of old pilots or new standalone forms module. Coordinate #280 and #347; preserve owner-approved progression decisions. |
| #161 / #279, #281 MXP growth and resource hooks | Verify current authorized engine/JAR selection and exact config, actual resource expenditure and multicasts, growth, one-wallet credit, persistence and dedup. Native JAWR expenditure remains separately tracked (#280/#347); never reintroduce legacy dual sources. |

This section supersedes the older five branch-based blocker labels and installed-JAR assumptions below for source ownership, while retaining their remaining acceptance tasks. All five are **baseline-owned / inclusion and gameplay verification still required**; no automatic owner deferral. No historical branch deletion or new build is authorized by this metadata change.

## Canonical blood bridge — current integration preparation (PR #282)

**SOURCE-INTEGRATED / RUNTIME ACCEPTANCE STILL BLOCKED:** Reviewed replacement [PR #282](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/282) brings #126's 17 ordinary-blood KubeJS JSON assets, Iron's drinkable-vial startup file and strict JAWR component-safe bottle adapter into the current `test/full-integration` source. Frozen donor: `f55641f2227334a394475183f9d13844547d425e`. Retains the JAWR-only, Archlich and Dracolich code instead of overwriting transformations. This is **selected for attempt at the next owner-authorized comprehensive QA**, not a PASS.

**Required inclusion:** the indexed 18 Packwiz KubeJS files PLUS a rebuilt current `tmm_transformations` JAR containing `JAWRBottleInterop`; exact client/server inclusion proof and gameplay cauldron/drinking tests. Source/static/Module CI alone is not an actual installed runtime. JAWR protected chicken/vampire components must remain untouched; the MXP actual blood-expenditure hook is separately blocked in [issue #280](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/280) and no bottle/refill should ever grant XP. Acceptance: [blood QA matrix](CANONICAL-BLOOD-JAWR-RECONCILIATION-2026-11-16.md). Do not remove this task from the all-ready-feature inclusion matrix until installed and explicitly tested or owner-deferred.

## Trader's Vengence #219 — source integrated, next runtime assembly still BLOCKED

**Next owner-authorized comprehensive QA: attempt inclusion.** The reviewed source successor [PR #283](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/283) migrates the former draft [#219](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/219) at frozen SHA `a5c8f32bd50744e2b3e083273c50e75d93b27c82` to `test/full-integration/compat/traders-vengence/`. Full behavior includes permanent offender UUIDs, villager/golem/llama retaliation, Iron's 3.16.3 Magic Missile, Hero of the Village pardon, vanilla-sign `No Soliciting`, and ±10 sea-level/natural-ground/sky candidate rules. The new native spawner-only Mixin no longer blocks admin summons or spawn eggs, and joining existing mobs cannot stack attack goals repeatedly.

**Missing runtime artifact:** `traders_vengence` is **source/module CI only**; there is no first-party JAR pinned/installed in Packwiz. The integration assembler must attempt compiling and packaging this JAR, load it with pinned Iron's 3.16.3, and test native spawner Mixin, real projectile signature, AI priorities, blacklist persistence/pardon, multiplayer and B20 Wandering Trader magic stock. Record INCLUDED/BLOCKED/OWNER-DEFERRED only after actual build inclusion evidence. Compilation is not a server or gameplay PASS. [Full acceptance checklist](TRADERS-VENGENCE-RECONCILIATION-2026-11-16.md). No full smoke authorized from this source pass.

## Newbie Mode #241 — reviewed, Smart Keep Inventory pinned, real in-world QA required

**Selected and registered for next authorized comprehensive test.** [#285](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/285) merged the Newbie/Regular persisted state, genuine virtual Elytra and authoritative **no-Newbie MXP** gate into shared Progression. Replacement integration [#286](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/286) reconciles native five-species acquisition vetoes with latest JAWR-only Dracolich/Archlich/visual/thermalily work, adds one personal FTB Quest checkmark graduation (227th total), and pins **exact** Smart Keep Inventory 1.21.1 1.0.1 NeoForge `JqVOLkFB` SHA512 with player-tag keep+XP and stock-boss override. Live Tyz Safekept override is explicitly `active:false` and indexed. No prior quests or modpins are discarded.

**Not yet gameplay-certified:** rebuild/include current first-party `tmm_progression`, `tmm_mxp`, and `tmm_transformations` JARs; launch Smart Keep plus Corail 9.5.6 and Tyz 6.4.2 in a two-player fresh world, test grave/Curios/XP conservation, no teamwide graduation, real Elytra/Ars/Iron flight mixins, five native species setter signatures and Gilded/Monk/Hunter mutual exclusion. FTB Essentials native travel command source and exact pin are now baseline-owned; non-op gameplay remains unverified. Prefab housing is **owner-approved and required for the next authorized comprehensive test** (2026-11-18, #236/#287); pin/config/reward verification still pending. No actual server, client or world acceptance was run in this source/pin merge. [Full matrix](NEWBIE-MODE-SMARTKEEP-RECONCILIATION-2026-11-16.md). **Do not quietly defer these in the next all-ready feature assembly.**

## Current reconciled source state — 2026-11-16

**Latest owner handoff:** [next-test integration reconciliation matrix](NEXT-TEST-INTEGRATION-RECONCILIATION-2026-11-16.md). The historical tables below are preserved for traceability; where they say **REVIEW REQUIRED** or **ATTEMPT REQUIRED**, consult this newer matrix for source-merge and blocker status. A source merge does **not** mean the feature is in a tested Prism artifact.

- **Merged current native code:** Monk Fortune/Hunter Looting (#247), K-key Monk/Hunter GUI and TreeChop (#248), Dracolich Cave/Center skeletal rendering (#249), Thermalily cooldown/output mixins (#251), repaired namespaced progression commands and operator XP setup (#253). All received corresponding source/module CI checks; no combined gameplay acceptance has run.
- **Frozen donor streams for the next authorized assembly:** B19 Gateways (17), B20 trader stock, Caerbannog, reviewed ore-shop additions, ProjectE Energy Converters 0.1.4, and three missing Bundle Curios slot files plus translation key. This work is **SELECTED FOR ASSEMBLY**, not automatically included in the current pack.
- **Blocked:** B16 BetterEnd, B17/YUNG, Newbie Mode/Smart Keep Inventory, JAWR blood interop, JAWR Tyz form skills, MXP runtime, Trader's Vengence, and Gilded lava-fishing class/mod dependencies. See matrix for actionable reasons; no automatic owner-deferred designation.
- **Removed historical deferral classifications:** Dracolich renderer is current baseline source; Bundle Curios slot is source-locked, with current first-log quest reward preserved. The older first-log ancestor and duplicate reward function must not be replayed.
- **No automatic builds:** Repository/Module CI and Integration Preflight only. Run Integration Assembly and full server/client QA solely on a fresh, explicit owner request under `AGENTS.md`.

## Owner-approved next-test dimensions — 2026-11-16

**ATTEMPT REQUIRED at the next owner-authorized comprehensive unstable test, not installed now.** This is the owner's explicit inclusion decision, tracked in [the design contract](../DESIGN.md). The integration writer must source exact 1.21.1 NeoForge files, hashes, dependency graphs and packwiz entries for **The Aether, Deep Aether (addon), The Bumblezone, Dive Into the Abyss, Eternal Starlight, Twilight Forest, The Aurorian (unofficial port), Voidscape and Liroth: Revamped**. All nine are in the desired roster only until pinned; inspect new-world generation, boss/dungeon content, portal persistence, multiplayer restarts, balance and spell/mobility shortcuts before acceptance. This approval does not authorize running Assembly, Server Boot Smoke or Prism QA Packaging now.

**Keep** installed Deeper and Darker. **Do not include** Dimensional Dungeons (deferred for Neo Vitae overlap) or Realms of Redemption (reserve). The Twilight Forest final-castle replacement with a **Killer Rabbit of Caerbannog** is an approved *development target*; reconcile the already staged Caerbannog Gateways prototype (PR #68) but do not treat it as a finished final-castle boss. If boss implementation is not ready for the next test, show it separately as blocked rather than silently excluding the Twilight dimension.

## Required agent procedure before the next build

1. **Inventory** every report of "ready for live test" since the last owner-facing live test: ongoing and prior chat handoffs available to the agent, open PRs/descriptions, completed feature branch docs, latest `manifest/integration-sources.json` and the current QA ledger. A readiness handoff from another chat is actionable even if the branch is unmerged or not registered.
2. **Register** each ready feature with source branch/PR, exact planned runtime files or mod pins, dependencies, needed tests, and a current decision. If source is missing, say exactly what was sought; do not interpret lack of an entry in the old source map as owner approval to omit it.
3. **Attempt real inclusion** into a disposable, unified next-test integration candidate. This may require reconciling branches, building first-party JARs, resolving version-correct dependencies and packwiz pins, integrating config/KubeJS/datapack/quest content, and validating that feature files survive later source imports/final purges. This is an **integration preparation obligation**, not authorization to run Minecraft or a full smoke.
4. **Reconcile collisions** instead of simply selecting an old integration snapshot. Where two ready features edit the same file, retain the newest approved behavior of both or document a reproducible conflict/owner decision. For new mods, "planned" or `enabled_in_pack=false` is not inclusion; pin the exact compatible files, verify hashes, and check the packwiz index before claiming it is staged.
5. **Prove inclusion, not just an import statement:** compare the final assembled file list, `index.toml`, first-party JAR contents, quest/resources, and a frozen build manifest against the registered intake entries. When source-only feature files never reach the runtime ZIP, mark **NOT INCLUDED**.
6. **Resolve every ready item** using one of:
   - `INCLUDED` — present in the next test candidate; record source SHA, destination and targeted live-test case.
   - `BLOCKED` — an inclusion attempt was made; record exact blocker, commands/output, affected dependencies, smallest repair and owner impact.
   - `OWNER-DEFERRED` — owner explicitly decided to postpone this specific feature after its readiness report; link the decision. No blanket deferral from an older narrow QA run qualifies.
   - `AWAITING READINESS CONFIRMATION` — a historical unreviewed branch, not falsely labeled as already ready; investigate source and report before selecting the comprehensive live-test scope.
7. **Report missing work before publication.** A comprehensive QA build is complete in scope only if **every ready item** is `INCLUDED` or has an explicit `OWNER-DEFERRED` decision. If `BLOCKED`, continue preparing and testing independent items, show the blocker list prominently to the owner, and label any artifact a **partial integration test**, not an all-ready-features build. The owner decides whether to run that partial build.
8. **Do not launch a full modded server smoke, publish a full-smoke-certified QA ZIP, or start a live client test** without explicit authorization for that specific run under `AGENTS.md`. This register changes **inclusion planning**, not build/run permission.

### Completion gate for the build/handoff agent

Before asking for a full-smoke authorization or declaring "ready for live test," output the **inclusion matrix** in the PR or QA report: total ready items, included, blocked (with reason), owner-deferred (with source), and unresolved; branch refs/SHAs and expected client tests. Any ready feature found only in `deferred_pending_runtime` is an **unresolved omission** until reconciled. Do not silently relabel it "source-only", "later", "out of scope" or "deferred" to make the accounting pass.

A feature reported ready by **one chat** cannot be excluded merely because **another chat** was focused on retirement, quests, or a narrower repair. The normal next comprehensive unstable build attempts all ready workstreams together, with a special-case narrow build only when the owner specifically requests that narrow scope.

## Carry-forward: previously deferred runtime candidates (2026-11-16 audit)

All of these appeared under `deferred_pending_runtime` during the previous retirement-focused QA. **Each requires a fresh readiness check and an explicit include/blocked/owner-deferred decision for the next comprehensive live build.** This table does **not** assert that every draft was previously declared ready or that all are currently functional. It specifically prevents those historical deferrals from silently carrying forward.

| Branch / PR | Workstream | Next live-test intake status | Inclusion work to inspect |
| --- | --- | --- | --- |
| `feature/b16-betterend-thallasium-iesnium` · #131 | BetterEnd New Dawn + Thallasium→Iesnium | **REVIEW REQUIRED — requested for next test** | Resolve four exact compatible JAR pins, change recipe only with registered ore |
| `feature/b17-loot-performance-yungs-ars-locate` · #135 | B17/B18 YUNG, loot/performance, Ars locators | **REVIEW REQUIRED — approved next-build choices** | Pin runtime mods and reconcile native structure locators and existing recipes |
| `feature/b19-required-mob-gateways-trader` · #145 | 17 rare-mob Gateways | **REVIEW REQUIRED — testable source exists** | Import Gateway recipes/definitions, resolve live IDs and spawn semantics |
| `feature/b20-wandering-trader-guaranteed-magic-stock` · #149 | Guaranteed rotating magic trades | **REVIEW REQUIRED — testable source exists** | Include code/stock, preserve existing trades and exclude Seven Deadly Sins Ring |
| `feature/thermalily-no-waste-projecte-energy-2026-11-15` · #147 | Thermalily cooldown/50× equivalent + energy converters | **REVIEW REQUIRED — testable source exists** | Reconcile first-party `tmm_transformations` mixins, recipes and mods |
| `fix/integrate-starter-bundle-quest-2026-11-15` · #91 | Quest-claimable starter pouch + Bundle Curios slot | **REVIEW REQUIRED — next QA claimed** | Reconcile quest localization/first-log and existing Alcohol/Ars chapters |
| `fix/first-log-bundle-curio-quest-2026-11-15` · #90 | Ancestor of pouch fix | **VERIFY COVERED** | Avoid importing both ancestors; confirm successor has all required behavior |
| `feature/caerbannog-gateways-prototype` · #68 | Caerbannog/Chaos Bunny Gateways | **REVIEW REQUIRED** | Identify which recipes/encounters are approved for live QA |
| `feature/canonical-blood-config-first-2026-11-15` · #126 | EvilCraft canonical blood / JAWR bottle handling | **REVIEW REQUIRED** | Check JAWR protection and first-party bridge compatibility |
| `feature/dracolich-skeletal-model` · #75 | Dracolich client renderer | **REVIEW REQUIRED** | Resolve model/texture/runtime jar client-side and test limits |
| `feature/jawr-vampire-werewolf-tyzs-retention-2026-11-15` · #94 | JAWR-only Tyz form skills | **REVIEW REQUIRED** | Verify runtime Java and Tyz 6.4.2 dependency; source-only pilots are not runtime |
| `feature/mxp-configurable-growth-20261115` · #161 | Configurable 1.02 MXP growth | **REVIEW REQUIRED** | Source compiled upstream; reconcile shared MXP dispatcher and real JAR |
| `fix/quest-registry-gate-and-qa-priorities-2026-11-15` · #82 | Live quest item registry gate | **REVIEW REQUIRED** | Include validator/probe without unauthorized full smoke; preserve newer quest content |

**Reconciliation owners:** whichever Codex/agent assembles the next comprehensive test must fill in each row's exact commit and change status based on evidence; further newly ready work must be appended, not substituted for these. The owner has **not** given a blanket waiver for the above branches.

## Tyz migration / MXP integration boundary (2026-11-16)

| Feature | Current source status | Next test |
| --- | --- | --- |
| Tyz backend `fix/retire-puffish-skills-pin-tyz-2026-11-16` | **INCLUDED in baseline source**: exact Tyz pin, Pufferfish Skills removed, four Core Skills, native activity XP off; independent Attributes kept. Not a new client PASS. | Test Tyz GUI, 4x5 ranks, XP/SP, persistence, and no Pufferfish Skills in next authorized QA. |
| MXP engine `feature/mxp-configurable-growth-20261115` | **NOT RUNTIME INCLUDED**: `MxpService→TyzBridge.deliver` exists in Java, but no tmm_mxp JAR in pack. | Compile/integrate the newest source and verify the bridge plus resource/cross-cast hooks in disposable world; no automatic deferral. |
| Full nine-tree skill program | **OWNER-DEFERRED until built**; initial four-skill Core Skills pilot remains. | No empty dragon/form/Monk/Hunter category placeholders. |

## New ready feature record template

| Date / origin | Ready feature and source | Code / pins / conflicts | Inclusion status | Evidence (SHA, destination or blocker) | Assigned QA case |
| --- | --- | --- | --- | --- | --- |
| YYYY-MM-DD · chat/PR URL | Feature name, `branch` / #PR, quoted readiness decision or owner instruction | Paths/JARs/mod pins and needed integration | ATTEMPT REQUIRED | — | Bxx/Cxx |
| 2026-11-16 · [Monk Tyz full tree PR #229](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/229) | **18 Monk Tyz skills (17 four-lane/specialty skills + Empty-Hand Paragon), single global Tyz SP wallet**, plus unchanged 4 Core Magic previews; `feature/monk-tyz-full-tree-runtime-2026-11-16` | Assemble matching `compat/magic/tyzs-test-overlay/` **and** rebuild `compat/progression` from this same branch, pin existing Tyz 6.4.2, avoid old four-Core-only overlay; strict Curios gating and native Monk20/Dragon prerequisites. No new mod JARs. | **READY REPORTED — ATTEMPT REQUIRED at next owner-authorized comprehensive test** | Source/config + bridge committed; do not claim in-game PASS until Tyz purchase/refund, gameplay stat/event effects, separate XP wallets and native Monk eligibility are observed. Stage schema and Gradle CI results in [PR #229](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/229). Full smoke is **not authorized** by this registration. | [Monk Tyz test steps](../tyzs/MONK-TYZ-FULL-TREE-RUNTIME-2026-11-16.md#actual-test-boundaries) |

## November 16 quick QA and starter reward repair handoff

| Date / origin | Ready feature and source | Code / pins / conflicts | Inclusion status | Evidence (SHA, destination or blocker) | Assigned QA case |
| --- | --- | --- | --- | --- | --- |
| 2026-11-16 · [PR #221](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/221) | Bio-Mutagen 1.21.1 codec, ritual-only Dragon Survival and starter quest rewards · `fix/quick-qa-and-starter-rewards-2026-11-16` | `compat/progression/` recipe + JUnit; `defaultconfigs/dragonsurvival-server.toml`, `defaultconfigs/enigmaticlegacyplus-server.toml`; Introduction quest + canonical manifests/localization; `kubejs/data/tmm/function/rewards/first_log.mcfunction`; updated packwiz hashes. Reconcile any subsequent general quest/Monk branch so it does not overwrite these two starter quests; no new mod pins. | **ATTEMPT REQUIRED** at next owner-authorized comprehensive live build | Committed to isolated source PR; not yet installed, no client acceptance. Confirm compiled progression JAR recipe, new-world config copies and actual FTB claim delivery after integration. | A08, B05, C01; new-player starter/dragon smoke |
| 2026-11-16 · [PR #222](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/222) | Windows-normalized B19 Gateway catalog · `fix/b19-windows-path-catalog-2026-11-16` | One-line `scripts/build_progression_gateways.py` fix against deferred B19 feature branch; no Gateway pack files installed by this change. | **ATTEMPT REQUIRED** when B19 is assembled | B19 quick CI bootstrap passed; run B19 validator on Windows and ensure the corrected feature branch is imported rather than its stale parent. | A14, B19 |

**Lifecycle:** `READY REPORTED` → `ATTEMPT REQUIRED` → `INCLUDED` / `BLOCKED` / `OWNER-DEFERRED`; only then may a comprehensive test candidate claim a complete inclusion matrix. Do not treat source-readiness as runtime acceptance.

## Ars and starter quest reconciliation — ready candidate (2026-11-16)

| Feature | Source | Inclusion attempt | Live acceptance |
| --- | --- | --- | --- |
| Complete Ars Nouveau + seven addon questline | `feature/ars-quest-pilot` → `fix/ars-quests-current-baseline-2026-11-16` | **MERGED IN `test/full-integration` @ `edd555a`; NEXT ASSEMBLED RUNTIME CHECK REQUIRED**. 49 nodes, Chimera kill advancement, locale and authoring data; avoids older Alcohol/Welcome overwrite. | 49 Ars nodes and 19 Alcohol quests visible; novice, addons, Chimera, rewards claimed |
| Quest-claimed first-log Curio Bag | Concurrent quick starter fix + `fix/ars-quests-current-baseline-2026-11-16` | **MERGED IN `test/full-integration` @ `edd555a`; NEXT ASSEMBLED RUNTIME CHECK REQUIRED**. First log has no advancement reward dispatch; quest retains Curio Bag and XP, no torches. | Fresh player gets no auto bag; personal once-only quest claim; old player duplicate-policy check |

See [test instructions](ARS-QUEST-RECONCILIATION-2026-11-16.md). Neither row is an in-world PASS or authorization to start a full smoke.

## Dragon Survival, Jade and FTB UI defaults — next-client-test intake (2026-11-16)

| Date / origin | Ready feature and source | Code / pins / conflicts | Inclusion status | Evidence (SHA, destination or blocker) | Assigned QA case |
| --- | --- | --- | --- | --- | --- |
| 2026-11-16 · [PR #224](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/224) | Dragon/Jade/FTB UI collision mitigation · `fix/dragon-survival-jade-ui-collisions-2026-11-16` | `config/ftblibrary-client.snbt`, `config/ftbchunks-client.snbt`, `index.toml`, `pack.toml`; defaults sidebar bottom left and minimap top left. Jade Keypad-1 collision requires a *player-selected* binding remap, not a shipped personal `options.txt`. No Dragon Survival Java patch. | **ATTEMPT REQUIRED** in next comprehensive client candidate | Exact hash-pinned config staged on PR; verify config survives assembly/source override and renders appropriately, including user preference persistence. No client PASS or full smoke claimed. | C10 |

### FTB Filter System first-log readiness (2026-11-16)

| Feature | Source | Inclusion state | Next-test acceptance |
| --- | --- | --- | --- |
| Native FTB Filter System `minecraft:logs` first-log task | `fix/ftb-filter-first-log-2026-11-16` → `test/full-integration` on merge; `mods/ftb-filter-system.pw.toml` CurseForge `7429011` | **READY FOR NEXT OWNER-AUTHORIZED LIVE BUILD.** Quest, task and reward IDs retained; native Smart Filter component replaces `tmm:first_log`, whose JSON is deleted. No live smoke was authorized in this chat. | Fresh oak/spruce/cherry log and crimson/warped stem inventory triggers, dedicated server autocomplete without owning a Smart Filter item, readable “Any Log or Stem” task label, one claimed Curio Bag + 10 XP per player with no automatic gifts, old-world persistence and no duplicate reward. |

## Fresh-client key defaults and KeySets one-build trial (2026-11-16)

| Date / origin | Ready feature and source | Code / pins / conflicts | Inclusion status | Evidence (SHA, destination or blocker) | Assigned QA case |
| --- | --- | --- | --- | --- | --- |
| 2026-11-16 · Keybinding defaults owner decision | Default Options 21.1.8 (permanent candidate), KeySets 1.3.0 (single targeted run ONLY), unbound mod controls and introductory explanation · `feature/default-options-keyset-first-run-2026-11-16` | Client-only `mods/default-options.pw.toml`, `mods/keysets.pw.toml`; existing Balm, Controlling and KeyBind Bundles. New Getting Started checkmark quest, canonical locale/layout sources. 315 observed mod key IDs now installed as `config/defaultoptions/keybindings.txt`, hash-indexed with static `scripts/validate_defaultoptions_keybindings.py` guard; final-client registered-key export still needed to guarantee complete coverage. Preserve personal option remaps. | **315 OBSERVED UNBOUND DEFAULTS STAGED / FINAL-ROSTER COMPLETENESS BLOCKED / ONE-BUILD KEYSETS TEST REQUIRED** | Baseline source and packwiz pin staged; before declaring ALL mod keys unbound obtain complete final-roster mapping inventory, reconcile additions, regenerate and hash-index defaults. One KeySets live test then explicitly KEEP only on PASS; otherwise REMOVE pin/index/manifest for following build. Do not declare unbound PASS without actual client. | C11 |

## Dragon Survival Seasoned Fish raw-fish crafting correction (2026-11-16)

| Origin | Ready feature / source | Runtime destinations | Next-test status | Acceptance and evidence |
| --- | --- | --- | --- | --- |
| Owner's failed live recipe test; [PR #231](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/231) | Seasoned Fish accepts any raw fish instead of only salmon, `fix/dragon-seasoned-fish-raw-fish-2026-11-16` | `kubejs/server_scripts/dragon_seasoned_fish.js`; lightweight static test `scripts/verify_dragon_seasoned_fish.cjs`; requires pinned Dragon Survival + KubeJS + `#c:foods/raw_fish` | **READY FOR NEXT TEST; source-staged, not yet runtime-verified**. Once PR merged, confirm final pack actually includes the script; this table does not assert inclusion in an already-created ZIP. | The player already reported a **salmon-only FAIL** on the previous client. On a new disposable client with the new script, check actual `dragonsurvival:seasoned_fish` JEI entry and craft with cod, salmon, tropical fish and pufferfish, one ink sac and one kelp each. Cooked fish should fail. Check no duplicate or vanished recipe, and capture exact build SHA and KubeJS logs. Do **not** conflate with the distinct eight-dust `dragonsurvival:sea_dragon_treat`. Source validator locally PASS; no Minecraft test in this fix session. |

## Native Dragon Survival recipe observations — owner-directed next live test (2026-11-16)

**Owner decision updated 2026-11-16:** Treat Hot Dragon Rod and Spearman Promotion as **likely Dragon Survival upstream recipe-source bugs/quirks**, not modpack defects. The Hot Dragon Rod live report confirms it crafts using one dust + one blaze rod; its JSON's `count: 8` appears misleading/ineffective. Spearman Promotion has the analogous `count: 3` in one ingredient, but its runtime behavior is untested. **Assume shipped behavior is what the mod author intended. No KubeJS overrides, no patches, and no mandatory retest of C13/C14.** Source of Magic (C15) and addon-conditional Bottle Caps Treasure (C16) remain separately scheduled.

| Case | Native recipe | Required test intake |
| --- | --- | --- |
| C13 | `dragonsurvival:hot_dragon_rod` | **Likely upstream source-code quirk; owner-accepted.** Friend's reported result: one Elder Dragon Dust + Blaze Rod crafts. JSON uses `count: 8` inside one ingredient. No override, no defect ticket, no required retest |
| C14 | `dragonsurvival:spearman_promotion` | **Likely upstream source-code quirk; untested.** Single emerald-block ingredient declares `count: 3`. Accept mod author's shipped behavior; no override or required retest |
| C15 | `dragonsurvival:forest_source_of_magic`, `dragonsurvival:sea_source_of_magic`, `dragonsurvival:cave_source_of_magic` | Craft all three; compare stone/deepslate emerald and lapis ores, and verify Cave recipe; distinguish intentional restrictions from defects; leave unchanged |
| C16 | `dragonsurvival:bottle_caps_treasure` | **Conditional only** on `desert_monster_ds` being installed. Currently absent from checked-in roster; when absent record NOT BUILT/NOT APPLICABLE, do not install the addon. If included in a later build, investigate >9-slot shapeless recipe behavior, leave unchanged. |

No full smoke, build, or client run is authorized by this intake note.


## Automatic default-unbound controls from the runtime client registry (2026-11-16)

| Origin / readiness | Source / inclusion | Next owner-authorized QA acceptance | Status |
| --- | --- | --- | --- |
| Owner explicitly requested next-build implementation, 2026-11-16 | Existing first-party `tmm_progression` JAR's client-only `ClientKeyDefaults` + `ControlsDefaultsPolicy` + dedicated `tmm_controls_defaults.mixins.json`; keep all 315 fallback Default Options bindings and KeySets one-shot trial | Build the **new** progression JAR and include it in the unified Prism test, verify mixins applied and no client crash; clean profile with alternate `TMM_QA_PROFILE_DIR`: vanilla 34 unchanged, all actual standard modded registered keys start unbound including `keyset.open`, Controls Reset remains unbound, custom bindings survive restart; normal QA profile retains all earlier bindings; when a new mod adds a key, auto-unbind without another inventory export. Test KeySets/Controlling compatibility and obey one-shot KeySets PASS/REMOVE decision. External/custom hotkey providers require separate audit; snapshot read failure must fail closed. | **SOURCE STAGED / REQUIRED NEXT LIVE TEST**; do not call client-smoked based on Java build |

### 2026-11-16 · Native Monk Fortune / Hunter favored Looting — PR #247

| Source | Next QA inclusion requirement | Status | Live acceptance |
| --- | --- | --- | --- |
| [PR #247](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/247), `feature/native-monk-fortune-hunter-looting-2026-11-16` | Attempt to import rebuilt `tmm_progression` with `MonkFortuneMixin`, `HunterLootingMixin`, `HunterRareLootMixin`, `NativeLootBonuses`, helper math and new mixin metadata. Preserve all unrelated Newbie/Monk/Hunter/TreeChop progress from newer branches. Reconcile Gilded's parallel vanilla loot mixins from the separate experimental branch. | **SOURCE READY FOR INCLUSION ATTEMPT — NOT YET RUNTIME VALIDATED** | Test one-roll original Fortune from empty Monk fist (base I and Tyz up to IV), Silk toggle no Fortune, exact original ore/plant drops, no synthetic pickaxe; Hunter only selected favored-prey class gains Looting I from real direct/projectile player kills and rare-loot formulas, stacks with actual Looting but not other classes or invalid targets. Confirm no extra ItemEntity spawning and no regressed Gilded virtual Fortune/Looting after combined integration. |

Source QA detail: [Native loot parity checklist](NATIVE-LOOT-PARITY-2026-11-16.md). This registration is not owner authorization to assemble, smoke, package or run Minecraft.

## Owner scope clarification — next comprehensive QA (2026-11-16)

- **REQUIRED:** all nine approved dimension mods, all seven approved mob mods (issue #261), More Darkness itself (client only, exact compatible version/dependencies), FTB Essentials and the four selected first-party modules. No Torchmaster. Gilded remains an inclusion attempt requirement. Unresolved pins are blockers, not approved omissions.
- **OWNER-DEFERRED / pending implementation:** dynamic per-player More Darkness / Newbie / Astral rendering (#264); native JAWR blood expenditure to MXP (#280; existing eligibility/ledger still included); actual 37 JAWR Tyz skill effects (keep catalog inactive); Newbie progression crafting restrictions (#236). Preserve source and issues; no claim of gameplay PASS.
- **Mahou Catalyst Exchange compatibility (#37/#57): pending native scanner fix.** Keep original Mahou catalyst behavior available; never restore global dropped-item EntityTickEvent proxy bridge. Direct blank-circle Neo dust compatibility is separate. Attempt targeted native fix before QA; if unverified retain disabled compatibility, ordinary Mahou powders remain functional. No first-party code or build authorization implied by this record.

## Owner smoke scope update (2026-11-16)

The owner explicitly defers **Aether, Deep Aether, and Liroth: Revamped** to a future deployment; beta *library* prerequisites for those experimental dimensions are accepted in principle, but validate exact versions and Deep Aether's native loader metadata at that time. Include the new native Mahou fix #289 (old global tick script must remain disabled), all verified mob/dimension/static darkness pins #290, and the selected four first-party modules #288. Owner authorizes one full server smoke for this candidate, using the existing explicitly gated `full_smoke=true` dispatch only; no persistent automatic trigger or unrestricted retries. Gilded remains an unresolved inclusion attempt (not an owner deferral); do not label an incomplete candidate comprehensive. Record actual assembly and server results separately.

## 2026-11-19 — consolidated Economy source (PR #350)

Owner confirmed development closure and transferred integration to branch manager. Exact source ancestry #320 0a8a09720223b85125987ba5b4dd173476d33c11, #342 3232c4825ca1a12712e81a55fa2999be24569861, #319 2810eecc0abc0ed471c5c646ae99c809e5d6bb50, #346 5b2cfed56a6e9ef60d1e730d8ed5e71078586862, #350 4e738fefef8e25c4aed093650611b608a7922ea1; B19 and independent audit donors documented in integration manifest. Mandatory next authorized inclusion attempt: build selected tmm_economy, native 12-offering ritual and flower route, 19 Tome seeds, approved roots/configs, Parchment/Gateway five rewards and Replica Nugget. Source-only; record actual recipe registration and map, no obsolete grid, complete approved-zero and Capturing snapshot, expand Reliquary blocker list before claiming parity; unsupported co-products/charged defaults remain explicit holds. Test fresh relics and scroll knowledge/synthesis, exact debit/credit and offline account, converter OP/UI rates and Eco Plus, actual EMC cycle, texture and Jade/Titanium/CC presentation. Temporary legacy egg/mana mapping fixtures retained under docs/qa/economy-reference, not active alongside native mappers; verify loaded native authority before gameplay acceptance. Full boot/assembly/package not authorized by this intake.

## Prefab housing — owner-approved next comprehensive test (2026-11-18)

**ATTEMPT REQUIRED / implementation pending, not owner-deferred.** See #236 and #287. Resolve exact Prefab 1.0.9 NeoForge 1.21.1 and Cloth Config dependency, index both sides as required, generate and review the actual config, audit blueprint recipes, contents, protected placement, duplicate starter grants, EMC and progression bypasses, and verify client/server behavior. Housing is available in either player mode. Record INCLUDED only after actual artifact evidence; otherwise report BLOCKED with reason. Starter house reward mechanism and contents still need design/config decisions. School-themed custom houses and Prefab Custom Addon/LDLib2 are research candidates, not approved extra dependencies. No full assembly or smoke authorized by this record.

Integration handoff (2026-11-19): the separate `research/prefab-donors-20261118` workstream owns ongoing house design; keep its branch active. The inclusion decision can be merged independently and does not certify its house assets or authorize new dependencies.

## Astral dimension rendering and remote magical XP — 2026-11-19

**OWNER-APPROVED NEXT LIVE TEST / INCLUSION ATTEMPT REQUIRED.** Source branch `feature/astral-dimension-sky-config-2026-11-19` ([PR #361](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/361); corrected source `22e0870c22410d6d74abd0cb29b4c5b74527d90e` integrated here), config `config/astralsorcery-client.toml`, plus [Progression #347](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/347) commit `8e47e22322a8228233d540099d651ade71ccee05` or reviewed successor. Required actual configuration and rebuilt shared Progression JAR, pinned Astral Sorcery/Mahou/dimension mods. New config is a scoped addition; integration writer must regenerate current index/pack hash chain and preserve unrelated changes. [AS-D01–D07 acceptance checklist](ASTRAL-DIMENSIONS-2026-11-19.md). Rendering lists only; actual approved/denied starlight infrastructure, fixed-time/sky conditions and generation remain explicit live-test checks. No build or game run authorized by registration.

## Frozen donor source consolidation — 2026-11-19

All twelve remaining frozen streams are baseline-owned after this source intake. See [exact provenance and next QA acceptance](FROZEN-DONOR-CONSOLIDATION-2026-11-19.md). Runtime source selected for the next authorized comprehensive candidate; no claim of installed or gameplay-tested state. Roots and HexCast legacy prototypes are evidence only, not additional runtime load. B19 and ProjectE old branches already superseded. Historical refs remain until a separate exact-SHA retirement batch is approved and executed.

## Owner scope decision — 2026-11-19

Owner explicitly approves excluding Deep Aether and Liroth: Revamped from the next QA target. Remove their entries from the active required roster and discontinue their candidate audits for this QA. Remove Nuit, introduced solely for Liroth, from Packwiz and the required roster; retain its audited artifact evidence and catalog row. Retain The Aether, owo-lib and the existing BetterEnd New Dawn libraries. Result: 220/220 intended third-party pins; no missing or extra target IDs. The prior combined candidate ffe5c4c passed all nine CI workflows; armed Trader source and its reconciled shared packaging/tests passed together with Economy. Fresh CI required for this scope change; not assembly/build authorization. Other unresolved first-party integration work remains.


## 2026-11-19 · E6 native ritual MXP — mandatory next comprehensive inclusion

**SOURCE READY — ATTEMPT REQUIRED; NOT INSTALLED OR GAMEPLAY ACCEPTED.** Owner-approved closeout of merged predecessor [#347](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/347) and final slice [#375](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/375), runtime source checkpoint [`ce7b138648b6da43fd67996574b6444e54ebe9f4`](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/commit/ce7b138648b6da43fd67996574b6444e54ebe9f4). Integrator must select the coherent current `dev/progression` donor after #375 is integrated, retain immutable full donor SHA and merged provenance, compile the current shared `tmm_progression` JAR and verify the installed JAR includes all E6 catalog/receipt/mixin classes and config. Do not use the retired feature branch name as a live dependency or copy the entire old integration manifest. No new external mods requested; verify intended exact pins and supported versions are present in the assembled candidate (especially Malum 1.8.2, Gaze 1.1.7.1, Astral Sorcery 2.0.3.59, Primal Magick 6.0.7, Goety 3.2.2 and its Cataclysm addon). If a provider cannot be installed/validated, record explicit blocker and inclusion attempt instead of implicit omission.

| QA slice | Required live acceptance (source/static tests are not a gameplay pass) |
| --- | --- |
| Shared engine | Confirm optional mixin weaving/target descriptors on actual installed JARs; true connected player attribution, operation dedup, fixed first payout vs divided ordinary repeats, first-only skip, failed/canceled/no-effect operations, survival vs fake/creative/spectator, reload/relog and two simultaneous actors; no overlap with paid magic or boss victory award. |
| E6a | F&A required recipe tier (not forge casing tier), valid output/upgrade; Occultism item/summon terminal insertion, held cases remain zero; Roots actual output/effect and cancellation, native Terra separate, unsafe sacrifice excluded. |
| E6b | Totemic accepted native ceremony (subsequent effect may do nothing), Eidolon, Enchanted, JAWR circles/brazier; native completed rituals, actual participants, delayed result attribution, shared family variants and intentional exclusions. |
| E6c Primal/Goety | Primal wand-start, actual output, recipe reset; Goety altar completion, item/enchant/map/teleport/summon/conversion, storm terminal effect, Apostle summons excluded; both first/repeat tiers and paid cast overlap. |
| E6c Astral | First true personal attunement 3000 MXP; second constellation, Shifting Star, item attunement, failure/cancellation and command awards zero; pre-existing native attunement history honored. |
| E6c Malum/Gaze | 500 first/0 repeat per valid commissioned rite, normal/waveform share identity; invalid layout, pulses, repair/restart no reward. 1500/0 per actual recipient of each successful recipe-backed Geas (including both Gaze recipes), self and explicit multi-player participants only; Weakness-only incidental targets, unbind, failure, capacity rejection and repeat binding zero. Verify recipe change, interrupted/reloaded altar, recipient identity and persistence. |

Implementation details and held/excluded candidates: [E6 closeout roadmap](../../compat/progression/design/IMPLEMENTATION-ROADMAP.md#batch-e6-closeout--2026-11-19), [Malum](../../compat/progression/design/BATCH-E6C-MALUM-RITUAL-IMPLEMENTATION.md), [Astral](../../compat/progression/design/BATCH-E6C-ASTRAL-RITUAL-IMPLEMENTATION.md), [Goety](../../compat/progression/design/BATCH-E6C-GOETY-RITUAL-IMPLEMENTATION.md), [Primal](../../compat/progression/design/BATCH-E6C-PRIMAL-RITUAL-IMPLEMENTATION.md), [E6a](../../compat/progression/design/BATCH-E6A-RITUAL-IMPLEMENTATION.md), [E6b](../../compat/progression/design/BATCH-E6B-RITUAL-IMPLEMENTATION.md). Runtime commit #375 checks all green; current canonical integration baseline `ce771cae0dc9853076bd468677267f7f88c58ae4` passed read-only Integration Preflight (run 44615725976 job 135883748740, no unapproved baseline replacements); this is not a preflight of a future assembled candidate. No assembly, boot, or package authorization conferred.

## Unified Progression runtime cutover — owner 2026-11-19

Exact reviewed dev/progression donor `1063355f7ba920e113baae38ce4a6f03ba2c8e44` (#347/#344/#375) staged in isolated integration PR. Tyz 6.4.2 runtime pin, 27 configuration files and old KubeJS Thrift script excluded; standalone tmm_mxp JAR excluded, historical source kept inert. Retain Pufferfish Attributes. Independent target 219 third-party pins, four first-party runtime modules. Single award authority and Newbie gating require source checks and later fresh-world live acceptance. Core presently has zero authored nodes; individual native class/form trees and replacement of legacy purchased skills remain development scope. Legacy save migration is not required in pre-alpha QA. No rate or balance change and no assembly, boot or publication authorized.
