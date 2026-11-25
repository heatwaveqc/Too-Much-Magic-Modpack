# Thanksgiving QA results — run 45550879036

Frozen installed SHA: `721ea058294f838b9d7f145af427f5db228d248d`. [Checklist](THANKSGIVING-QA-2026-11-24.md). This is the per-build execution ledger; canonical feature acceptance stays in the feature registry.

Existing CI levels: Integration Preflight / Assembly / Module CI / Server Boot Smoke / Prism QA Package PASS in [45550879036](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/45550879036); rolling publication PASS [45552620361](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/45552620361). Client Launch Smoke and Gameplay Acceptance NOT RUN. No new game process launched while preparing this ledger.

| Case | Status | Evidence or next action |
| --- | --- | --- |
| A01 | PASS | Nov 25 installed audit: 255/255 JAR filenames and expected hashes match frozen 721ea058. Existing publication evidence retained; see automated report. |
| A02 | PASS | Nov 25 independent isolated compile/unit: Progression 462, Economy 58, Trader 14, Friendship Garden 9 = 543 tests, zero failures/errors/skips. No game boot. |
| A03 | PASS | Frozen manifest/mods.json and first-party-runtime.json inspected: Sodium 0.8.13, EMI 1.1.24, EMIsemble 1.2.1, Extra Mod Integrations 1.0.3, Multiplayer Spawners 2.0.2, Lootr/Liaison/Twilight Lootr enabled; JEI disabled. Selected runtime tyr_progression, traders_vengeance, tmm_economy, tmm_friendship_garden; no separate MXP or Transformations JAR. |
| A04 | PASS | Frozen ProgressionCommands, MxpMod, NewbieMode, core/monk/hunter path JSON, Economy/Vendetta command roots reviewed. Namespaced IDs use ResourceLocationArgument. No claim registered runtime command works. Monk glide config requires strict Wither milestone; old command failure string still mentions dragon; report inconsistency rather than taking text as mechanic. |
| A05 | FAIL | Installed inventory PASS; client log FAIL: Reactive-book script execution error (12/13 loaded), 52 recipe decoder failures incl Perpetual Life, 2 EMI plugin ABI failures, ProjectE/Goety mapping failure, recurring invalid-unbound-key spam. Full sanitized details in Nov 25 report. |
| A06 | PARTIAL | Collected generated food registry (26,940 IDs / 2,543 foods), full native EMC reports and 27,947-row effective recipe census. EMC reports internally valid but 15,591 unmapped / 15,572 nonexcepted missing; native Replication value/learned-pattern payloads not captured. See collection report. |
| A07 | PARTIAL | Source quests pass; collected native config files show Ars spawnBook=true, Astral giveTomeOnJoin=true and IS3 Add Book=true, despite default-template disable intent. No fresh first-login/claim gameplay test; collection preserves files and provenance. |
| A08 | PARTIAL | Frozen integration lock has no deferred_pending_runtime entries but pending_runtime_work includes stale historic notes. Explicit Nov 24 #448/#565 survey-dependent work and #656 Lilith deferred; do not claim historical rows are current. Audit each unresolved ready-feature path against actual frozen files before claiming exhaustive inclusion. |
| A09 | PASS | Nov 22 report preserved: 226/226 prior inventory, historical source/command census, Lost Soul surface observation, parchment Rhino failure, invalid key spam. Earlier natural Monk/Hunter initiation and unchanged JAWR baseline accepted/waived as documented; no automatic carryover to changed mechanics. |
| A10 | PARTIAL | 13 source validators, 20 Node cases, 543 module cases and 2 log-auditor cases PASS. Broad UTF-8 Python suite: 196/200 pass, 1 failure + 3 errors (stale fixture/Tyz paths/generated checklist). All 22 installed scripts parse, but Reactive runtime scope repro FAIL. AS-A01 partial resource census only. |
| B01 | NOT RUN | Await client launch and disposable fixture; see checklist. |
| B02 | NOT RUN | Await client launch and disposable fixture; see checklist. |
| B03 | NOT RUN | Await client launch and disposable fixture; see checklist. |
| B04 | PARTIAL | Generated food registry collected; existing one-shot world recipe census CAPTURED: 27,947 unique rows, zero adapter errors, 2,571 stonecutting recipes / 1,775 outputs, 242 Replication-related entries. Auxiliary baseline/fixture/player adapters UNSUPPORTED. No learned-pattern/value payload export. |
| B05 | PARTIAL | Existing native EMC audit collected and validated against supplied source label and installed 255-JAR inventory: 26,940 items, 11,349 positive, 0 zero, 15,591 unmapped. Coverage incomplete (15,572 nonexcepted). No price edits or in-world EMC acceptance claimed. |
| B06 | NOT RUN | Await client launch and disposable fixture; see checklist. |
| B07 | NOT RUN | Await client launch and disposable fixture; see checklist. |
| B08 | NOT RUN | Await client launch and disposable fixture; see checklist. |
| B09 | NOT RUN | Await client launch and disposable fixture; see checklist. |
| B10 | NOT RUN | Await client launch and disposable fixture; see checklist. |
| C01 | PARTIAL | Existing Nov 25 client log independently records world preparation and local player login. Codex did not launch the client or verify fresh-world/rejoin/UI conditions. See automated report. |
| C02 | FAIL | Existing client log: EMI Rechiseled and Reliquary plugins fail with missing native classes; visual sidebar, Sodium, search/bookmark/transfer checks not performed. |
| C03 | NOT RUN | P0 — New player inventory and Tips & Tricks |
| C04 | NOT RUN | P0 — Quest titles, reward choice and housing |
| C05 | NOT RUN | P0 — Newbie protections and Normie transition |
| C06 | PARTIAL | Existing client log snapshot contains 15,584 Invalid key -1 lines. No interactive keybind, screen or TreeChop check performed. |
| C07 | NOT RUN | P0 — EMC and Replication per-player visibility |
| C08 | NOT RUN | P0 — Replica worldgen and effective recipes |
| C09 | NOT RUN | P1 — Universal MXP and Core |
| C10 | NOT RUN | P1 — Monk XP/trials and abilities |
| C11 | NOT RUN | P1 — Monk strict Wither and glide |
| C12 | NOT RUN | P1 — Hunter XP, mutations and specialties |
| C13 | NOT RUN | P1 — Unified food and Dragon Survival |
| C14 | NOT RUN | P1 — Enchanting, potion charms and captures |
| C15 | FAIL | Perpetual Life recipe is rejected: unknown neovitae:hellforged_ingot. Native installed recipes produce neovitae:ingot_hellforged. Other recipes, book and ritual gameplay NOT RUN. |
| C16 | NOT RUN | P1 — Magical industry and material/blood bridges |
| C17 | NOT RUN | P1 — ProjectE, quest shop and Bountiful |
| C18 | NOT RUN | P1 — Trader's Vengeance and stock |
| C19 | NOT RUN | P1 — Friendship Garden (multiplayer) |
| C20 | NOT RUN | P1 — Lootr + Multiplayer Spawners (two players) |
| C21 | NOT RUN | P2 — Worldgen, locators, spooky spawns |
| C22 | FAIL | Reactive chest-book script fails to execute; same top-level conditional const failure reproduced with pinned Rhino offline. Parchment parses and its six fixture tests pass; actual fishing/loot encounters NOT RUN. |
| C23 | NOT RUN | P2 — Transformation cross-mod acceptance |
| C24 | NOT RUN | P2 — Long-play, multiplayer and persistence |

Append observed evidence with timestamp, case, exact installed SHA, test level, setup, status, actual result, screenshot/log reference and issue follow-up. Do not promote source/CI to gameplay PASS. Never merge historical evidence into this build without verifying scope and dependencies. No private data or full logs.

## November 25 — owner-requested automated execution

[Full report and sanitized evidence](thanksgiving-2026-11-25/README.md). Installed frozen source verified; this continues PR #708. No installed files or saves changed, no in-game commands, no client/server launch, no assembly or package. C entries above cite existing log evidence only and preserve unperformed manual obligations. At that offline checkpoint B01–B10 were NOT RUN; the subsequent collection below updates B04/B05. Evidence report separates 13 passing validators / 543 passing Java cases from the four broad-suite test failures and actual client-runtime defects.

## November 25 — requested live-data collection completed

[Collection evidence and limits](thanksgiving-2026-11-25/collection/README.md). Owner triggered reload/rejoin; unchanged optional preparer captured effective recipes, collector removed all three temporary files, and final owner reload/12-of-13 script roster confirms unload. Food/EMC/configs/sanitized logs and saved Progression diagnostics uploaded within #708. No gameplay code/config changes or new build. Earlier statement that all B cases are NOT RUN describes the preceding offline-only phase; B04/B05 now have the scoped capture results above.
