# Next live-test inclusion register

**Policy:** "Ready for a live test" is a **request to attempt inclusion** in the **next owner-authorized comprehensive live test**, not permission to park a source branch as "deferred." This includes readiness reported in a development chat, handoff, PR, issue, or project note. The phrase can be equivalent: *ready to test*, *next test build*, *ready for in-game QA*, *prepared for the next full integration*, etc. The declaring agent must register the work in this file and link its source; the next integration agent must independently discover unregistered readiness claims too.

**This document is an intake and inclusion-decision ledger, NOT a pass report.** See [CURRENT-TEST-STATUS.md](CURRENT-TEST-STATUS.md) for what was actually tested and [NEXT-TEST-CODEX-CHECKLIST.md](NEXT-TEST-CODEX-CHECKLIST.md) for the live-test procedure. The authoritative runtime source lock is [manifest/integration-sources.json](../../manifest/integration-sources.json). **A prior `deferred_pending_runtime` entry is not an inclusion decision for a later comprehensive test.**

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

## New ready feature record template

| Date / origin | Ready feature and source | Code / pins / conflicts | Inclusion status | Evidence (SHA, destination or blocker) | Assigned QA case |
| --- | --- | --- | --- | --- | --- |
| YYYY-MM-DD · chat/PR URL | Feature name, `branch` / #PR, quoted readiness decision or owner instruction | Paths/JARs/mod pins and needed integration | ATTEMPT REQUIRED | — | Bxx/Cxx |

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
