# Too Much Magic — user acceptance ledger (2026-11-14)

Source: reported in the active Magic Minecraft Modpack conversation; user claims are intentionally labeled as **player-tested** rather than automated assertions.

## Latest all-workstreams client QA observations

| Area | Status | User report / bounded interpretation |
| --- | --- | --- |
| FTB Quests navigation and quests | **PASS — player-tested** | "quest good" in full integrated client. Earlier client screenshot exhibited literal & formatter errors; fixed and previously confirmed as quest good. |
| Starter-path choice | **PASS — player-tested** | "starter choice functions"; only generic client flow confirmed, not independently tested for second player and logout/Claim All duplicate behavior. |
| Quest Shop sidebar coin button | **PASS — player-tested** | "coin button works". Earlier coin redemption and Eternal Steak were also explicitly confirmed. Catalog purchase / multiplayer accounting not independently claimed in this report. |
| Quest rewards | **PASS — player-tested** | "quest rewards look good". XP bottle hard blacklist statically validated in B14; new reward completion behavior not exhaustively sampled. |
| FTB Essentials travel | **PASS — player-tested** | "travel works". Non-OP, home limit, RTP claim safety, cooldowns and restart persistence still need separate boundary checks. |
| General QoL | **PASS — player-tested** | "qol functions". Group-level pass, not proof every individual overlay, sorting, TrashSlot and TreeChop edge case passed. |
| Vampirism dual-progression bridge | **IN PROGRESS — player testing** | Owner continues integration and progression tests. Do not promote to stable. |
| Monk first sunset | **FAIL — user-reported** | No visible action bar/graphic/completion after watching sunset. Natural procedure has strict 10-second uninterrupted sunset, western gaze, clear weather, no equipment, neutral faction and survival preconditions. No graphic was implemented, actionbar only on valid 40-tick intervals. Cause not yet diagnosed. |
| Monk skill tree | **FAIL / NOT READY — user-reported** | User sees empty skill tree / no Monk skill tree. The module currently defines only a zero-cost Step Assist root, with no later nodes. Before faction join the GUI may be absent. Need B15 operator diagnostics and actual faction-join test to distinguish registration errors from locked UI. |
| Starting inventory | **PENDING B15** | New first-tick reward: one empty Akashic Tome + 2 vanilla cooked beef + direct Improvable Skills Book access. Check separate first-log pouch, duplication, return-world migration. |
| Materials, Neo Vitae processing, Silver/Lead | **UNTESTED** | No new player results in this report. |
| Monk Step Assist on +0.5 step height | **UNTESTED** | Not reachable until faction join. |
| Tyz unified MXP, Pufferfish removal | **NOT IMPLEMENTED / not a player test** | Still pending core architecture. |

## Current build-process decision

The accepted workflow is an **all-active-streams unstable integration build** with a frozen SHA and logs, not isolated per-feature partial builds. The B15 starting inventory and Monk diagnostic source branches are registered in `manifest/integration-sources.json`. A green server smoke is not a successful client gameplay test.

## High-value next user tests

1. In a fresh B15 world: Tome + 2 cooked beef exactly once; Improvable button immediately available; separate first-log bag still once; relog, death, second player no duplicates.
2. `/tmmmonk diagnose` before sunset; record current faction, time window, gaze, weather, roof/terrain, no-gear eligibility and current trial ticks.
3. At `/time set 12000` in survival with empty armor/hands and sky/line of sight, look west (-X) and confirm actionbar at 2s and successful initiation by 10s. No animation is expected yet.
4. If natural rite fails, use operator-only `/tmmmonk testjoin` on a **disposable neutral survival player**. Open Vampirism faction skill UI and inspect Step Assist root. Reequip armor/weapon and test step height suppression; relog.
5. Continue separate native vampire blood/progression and source transformation tests.
6. Confirm the integrated resources + Monk still boot together. Test registered compats under user world, not just server log.

Do not mark Monk or Vampirism fully accepted based on compilation or mod registration alone.
