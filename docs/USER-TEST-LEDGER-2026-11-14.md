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
| Monk first sunset | **PASS — player-tested on earlier integrated build** | Owner re-tested after realizing the sunset viewing direction was wrong; the natural initiation worked. Original failure was incorrect test procedure, not confirmed broken rite code. B15 diagnostic commands remain untested. An animated confirmation graphic is still a future polish task. |
| Monk skill tree | **PARTIAL / FOLLOW-UP** | Earlier empty UI was seen before correct initiation. Natural faction joining now passes, but this does not prove the whole tree is populated or working. Current native Monk prototype defines one zero-cost Step Assist root; broader skill progression remains work in progress. |
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

## Follow-up correction (user report, 2026-11-14)

The owner corrected the Monk sunset procedure and successfully initiated on the older test build. Treat First Sunset's **natural initiation** as a player-tested pass. Do not extrapolate this to the B15 diagnostics, filled Monk skill tree, universal Tyz integration, or complete transformations compatibility. The earlier FAIL entry was the test procedure, not confirmed code failure.


## Full-integration direct-import ZIP player report — 2026-11-14 local, QA #43672219306

Full item-by-item observations, exact immutable commit and explicit untested boundaries are in [QA-CLIENT-PLAYTEST-43672219306-2026-11-14.md](QA-CLIENT-PLAYTEST-43672219306-2026-11-14.md).

- **PASS (bounded):** ordinary magic skill use; quests; shop; general QoL; the graphite-from-diamonds change and coal-as-fuel shift-click fix. Monk native mastery XP/ranks also increased correctly.
- **PARTIAL:** most regular dragon foods and normal vampire interactions worked.
- **FAIL / release blocker:** Lilith/JAWR vampire upgrade reset both blood pools to empty. Feeding failed to restore them until re-login. Even after re-login, JAWR's own blood HUD remained invisible. Do not mark ascension/blood accepted.
- **FAIL / release blocker:** Monk skill UI denied purchases ("You cannot buy skills" approximately), despite mastery leveling. Do not mark Monk skill tree playable.
- **FAIL / missing diet cases:** Sea Dragon could not eat sushi; Forest Dragon could eat enchanted Golden Apple, but not reported enchanted Golden Carrot or Sourceberries. `aquaculture:sushi` already exists in the authored diet map, so diagnose runtime food/data-map and exact ID rather than assuming omitted config. Exact registry ID of "enchanted golden carrot" remains unconfirmed.
- **OBSERVATION:** Treecapitator yields a single Monk block-break XP event for a whole tree. **REQUEST:** credit successfully completed, player-attributed magical block breaks, plus provide credible noncombat Monk level-one advancement and/or small starter combat improvement.
- **DO NOT INFER:** No report here of all 180 quests, all ore-shop prices, multiplayer, universal MXP dispatch, lich diets or ProjectE endgame. Treat aggregate passes as bounded.

This newer playtest supersedes the earlier ledger's Monk tree "not yet proven" status with **observed purchase FAIL** and vampire "in progress" status with **observed upgrade FAIL**. No stable promotion or reclassification of unrelated features.
