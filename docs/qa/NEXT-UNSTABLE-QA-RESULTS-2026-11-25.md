# November 25 unstable QA results

Authorized integration input `cc8bb188f0e0498dbc5007f6c4068bafe1481279`; current installed frozen SHA **20729a369e6f811730cea04cadfd12f265c96424**, baseline **cc8bb188f0e0498dbc5007f6c4068bafe1481279**; [build run 45751755305](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/45751755305), ZIP/artifact identity verified in A01. Earlier 887a4ffb instance was replaced during A execution. [Checklist](NEXT-UNSTABLE-QA-2026-11-25.md). Bridge dispatch accepted only; no client launch, game test, publication or smoke PASS inferred. Prior results preserved in [Thanksgiving results](THANKSGIVING-QA-RESULTS-2026-11-24.md). Record evidence against actual installed SHA before testing.

| Case | Status | Evidence / next action |
| --- | --- | --- |
| A01 | PASS | Current replacement 20729a36 / cc8bb188 verified: ZIP fc5a35fa... matches run 45751755305 artifact 15086506741; 258/258 installed JAR hashes match. Earlier 887a4ffb checks retained separately. |
| A02 | PASS | Existing run 45751755305 records successful preflight/assembly/compile/smoke/package. Independent current-source local compile: 596 Java cases pass; no new game process, assembly or package. |
| A03 | PASS | 254 pins, 258 JARs, three cozy additions, Relics 0.12.9, Modrinth Aquaculture Delight, Sodium/EMI and selected unified modules verified. Oaks recipe decoder defect separately A05. |
| A04 | PASS | Frozen command/OP/namespaced parser source inspected; installed Monk/Hunter/Core JSON equals frozen UTF-8 source. Classic Monk 55/21, Hunter 31 skills, Core 4. Runtime commands NOT RUN. |
| A05 | FAIL | 20729a36 also crashed 21:19:40: Reactive ItemStack.is ambiguity line 29. Current log: 17 new Oaks display-case recipe failures, 526 invalid-key lines; old decoder groups/ExMI/Goety mapping errors absent. Exact PR801 script later applied locally at owner request; native retest pending. |
| A06 | BLOCKED | No current replacement-build food/EMC/recipe exports; morning #708 counts not transferred. Fresh B04/B05/native material maps needed. |
| A07 | PARTIAL | Source quests PASS: 45 chapters/298 quests, 21 Friendship lessons, 19 personal home choices. Native Ars/Astral/Feywild grant flags false; generated IS3 Add Book flag still true. Actual first-login/claims/UI pending. |
| A08 | PARTIAL | Verified Classic/cozy/Friendship/density source and installed paths; explicit Unlimited, #768/#742, Flowerary, #656/#448/#565 scope limits retained; no exhaustive runtime acceptance claim. |
| A09 | PASS | Prior #708 and 887a4ffb crash evidence preserved; replacement build is separately identified, no gameplay PASS carried forward. |
| A10 | PARTIAL | 14 validators, 23 Node tests and 596 Java tests pass. UTF-8 Python discovery 233/234 pass; generated feature-checklist Markdown mismatch remains. Full commands/results in A report. |
| AS-A01 | PARTIAL | Exact Astral resource recount: 364 IDs, 5 roots, 90 unconditional/274 conditional; cap 30. Native point formula and constellation discovery gate verified. Connector/free-grant/hidden/max simultaneous-token calculation incomplete, no guess. |
| A11 | BLOCKED | Reference registry revision 6 / 1763 IDs/dispositions validates but is historical and not runtime active. Current entity/tags/boss/egg/blood/sulfur native census absent. |
| A12 | PASS | All 69 source and installed frequency overrides match; 69 native originals across 13 verified providers match recorded originals. Historical paired-pilot evidence linked; no new worldgen run or multi-seed claim. |
| A13 | PASS | Installed Classic equals frozen source: 55 skills, 21 milestones, mastery max 20; 511 Progression test cases pass. Runtime attributes, harvesting and combat acceptance remain C tests. |
| B01 | PASS | World 1: six commands succeeded while Newbie; after graduation/Monk initiation mode Regular, newbie=false, normie=false, core=true rank=1, zero XP/points/MXP, growth 1.02. Automatic Core enrollment verified; isolated Normie state not captured, reserved C05. |
| B02 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| B03 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| B04 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| B05 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| B06 | PARTIAL | Pre-graduation EMC/material valuations hidden; after graduation/Monk upper-left EMC: 0 visible before intended Stone unlock, owner confirms defect. Other acquisition/independent player probes pending. |
| B07 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| B08 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| B09 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| B10 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| C01 | PARTIAL | World 1 post-#801 local overlay: owner reports successful entry, exploration, save/rejoin Nov 26, no recurrence of crash. Earlier 887a4ffb/20729a36 pre-overlay FAIL evidence retained; clean rebuilt package and actual Reactive replacement still pending. |
| C02 | FAIL | World 1 Nov 26: FTB hover text still black under Sodium; EMI looks good. Other icon/GUI-scale/transfer/category specifics not independently reported. #507 renderer defect persists. |
| C03 | FAIL | World 1: empty first-login inventory PASS; claimed starter kit via function, Tome opens/morphs correctly but only six guides observed vs twelve configured. Owner wants native personal item rewards distributed across multiple introductory quests; #306/#509. |
| C04 | FAIL | World 1: graduation home choice claimed, custom paper blueprint repeatedly refuses placement as obstructed. #804 native Prefab placement correction. General quest presentation sampled good; Choose your magic controls gives paper, unwanted Ring/Amulet in early Intro; owner changes routed. |
| C05 | PARTIAL | Newbie teleport, keep inventory and elytra work per owner; ghost suppression and flat-ground jump boost #287. Regular mode, Newbie left, Core joined verified after Monk initiation. Pure Normie and post-graduation ability/teleport denial not isolated; use later fixture. |
| C06 | PARTIAL | Owner reports controls work; individual bindings/log counts, progression screen/TreeChop details remain unverified. |
| C07 | FAIL | World 1 pre-graduation values hidden; upper-left EMC: 0 appears after graduation, owner confirms should not display before Stone. Isolate HUD provider and mode vs acquisition trigger; no post-unlock/independent-player proof. |
| C08 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| C09 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| C10 | PARTIAL | World 1 First Light completed after time set 0 then 12000 (command-assisted, actual meditation). Owner reports sunrise did not count with mountain in way; config has clear sky/dry/direction/200 ticks, exact cause not proven. Core rank 1, MXP 0 after initiation. Training XP, skill purchase and attributes pending. |
| C11 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| C12 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| C13 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| C14 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| C15 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| C16 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| C17 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| C18 | FAIL | World 1: villager ranged damage works, gun model absent; owner confirms ranged engagement: golem aggroed, advanced and used melee rather than firing; both weapons invisible (provenance/LOS/exact distance not established; #803); aggression persisted into second village. Commands, relog, Hero pardon, stock, ranged setup and safety remain untested. |
| C19 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| C20 | PARTIAL | World 1: owner observed missing/broken texture on a Stray spawner attributed to individual/multiplayer spawner mod; exact registry ID/screenshot pending. Multiplayer independent state and cooldown NOT RUN. |
| C21 | PARTIAL | World 1 structure density owner reports way more reasonable. Alex's Caves/Tectonic warning previously recorded; actual cave generation unresolved. No multi-seed claim. |
| C22 | PARTIAL | World 1: no further Reactive crash reported after #801 local script overlay; no confirmed Reactive-only/mixed/configured chest book or filter diagnostic log yet. Remaining loot/Gateway/fishing cases NOT RUN. |
| C23 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| C24 | PARTIAL | World 1 save/rejoin good. Nov 26 screenshots: Java process 11,111.5 MB (Task Manager), system memory 90%; F3 5976/8192 MB heap, 8080 MB allocated, off-heap +1101 MB, allocation 642 MB/s, 60 FPS, integrated server 19.2/50 ms. One snapshot, no leak inference; #802. Long-play/multiplayer pending. |
| C25 | PARTIAL | Owner approves Friendship is Magic visual presentation (looks amazing); task credit, rewards, exact zoom and native progression still not independently verified. |
| C26 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| C27 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| C28 | PARTIAL | World 1: owner reports Reactive crash no longer occurs with local #801 overlay; actual loot transformation, other targeted recipe fixes and log regressions remain unverified. |
| C29 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| C30 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| C31 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| C32 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |

For each attempt append ISO time, installed SHA, level, case, setup, exact command if applicable, actual result and sanitized log/screenshot/issue. PASS/FAIL/PARTIAL/BLOCKED/NOT BUILT/NOT RUN only; retain historical evidence, never commit worlds, binaries, full logs or private data.

## Test 1 — fresh world

Owner-reported session: client booted and all mods loaded correctly; world creation reached player entry, then client crashed. C01 **FAIL (launch/world-entry stability)**; successful initial boot and world entry are bounded partial observations, not a complete launch smoke or gameplay PASS. No B commands or further C gameplay verified. Crash report/log collection by Codex pending; exact installed SHA/artifact still to verify. Preserve this world and crash evidence; do not infer cause until logs are reviewed.

## Crash evidence collected — November 25 evening

[Crash packet](crash-2026-11-25-210714/README.md): exact installed identity, complete initial exception stack and matching log excerpt, sanitized with source-file digests. Retrieval/publication only; no repair, restart, build or new test. Morning #708 remains historical; this packet continues #796.

## A sweep and requested local #801 overlay

[Current A report and evidence](a-results-2026-11-25-20729a36/README.md) distinguishes initial 887a4ffb checks from the replacement 20729a36 candidate. All fourteen A rows reconciled above; A06/A11 data and remaining Astral research explicitly unresolved. No B commands/game launch/build by this collector. After baseline checks the owner requested the exact #801 script: backed up original, applied one local file, five mock groups and pinned Rhino parse pass; no world/loot retest yet. Source/pack files in this QA PR remain documentation/evidence only.

## World 1 — November 25 evening, post-hotfix gameplay

Installed snapshot `20729a369e6f811730cea04cadfd12f265c96424`, locally applied Reactive script #801 per A report. Owner's named world is **World 1**, distinct from prior crash fixtures; do not rename it Test 2. Owner reported spawning close to a village, then villager gun attacks causing damage without a visible weapon model, iron golem walking up to melee rather than visibly using its minigun/rocket, and subsequent village also firing (cross-village vendetta persistence observed, not a save/reload or command proof). Stray spawner texture broken (owner attribution: individual spawner mod; exact block/model or entity renderer unknown). Memory confirmed by owner as 11 GB on Nov 26; measurement source unverified, investigation #802; collect heap used/committed/max, process working set, idle vs exploration and GC behavior before classifying. No further crash reported during this session after Reactive hotfix; actual successful book replacement and absence of filter-disabled diagnostic still unverified. No commands executed or save/rejoin claimed. Owner stopped for the evening; continue tomorrow. Route Trader's Vengeance to #423 / #284, texture and memory triage to QA #274; no new build or gameplay patch authorized by these observations.

## Thanksgiving morning — owner clarification and clustered plan

Owner confirms 11 GB memory, and that the golem test was at engagement range: acquired aggro but approached rather than firing; neither weapon rendered. Owner approves both shoulder weapons emerging when aggroed. #802 memory research and #803 Trader render/combat/deployment follow-up created. No additional runtime result inferred. [Clustered B/C plan](NEXT-UNSTABLE-QA-2026-11-25.md#thanksgiving-execution-order--november-26-owner-gameplay) starts with read-only/non-OP World 1 baseline, then natural graduation before admin or irreversible fixtures. A report already uploaded; do not reassign A work to owner. No new build.

## World 1 — Thanksgiving morning Batch 1 (2026-11-26 ~08:33 Arizona)

Owner-reported: save/rejoin good, starting inventory empty, FTB hover text still black, EMI looks good, EMC and material values correctly hidden, controls work, quests generally look good (not exhaustive), Friendship is Magic chapter visually excellent. No B results yet. Two supplied screenshots inspected; Task Manager Java process 11,111.5 MB, total system memory 90%; Chrome 4,407.8 MB, F3 Java 21.0.7, heap used 5976/8192 MB (72% displayed), allocated 8080 MB (98%), off-heap +1101 MB, instantaneous allocation 642 MB/s, 60 FPS, integrated server 19.2/50 ms. Measurements from nearby snapshots rather than synchronized instrumentation; not a memory leak diagnosis. #802 and #507 updated. No raw screenshots uploaded (contain coordinates/system details). No new build or commands run by QA coordinator. Next B01 owner-run, then Batch 2 non-OP onboarding.

## World 1 — B01 (2026-11-26 ~08:35 Arizona)

All six owner-run baseline commands returned as recorded above. Core automatic enrollment is configured but Core not joined while Newbie; follow up after natural graduation. Backup succeeded in 3.130s (60.6 MB / 60.8 MB displayed). Alex's Caves warning names Tectonic incompatibility; investigate actual cave generation and compatibility before suppressing warning or updating. No raw screenshot committed. Continue Batch 2 natural Newbie setup.

## World 1 — Batch 2, Thanksgiving morning ~08:43 Arizona

Owner confirms prior teleport tests and deaths to villagers, Newbie elytra works, structure generation considerably more reasonable. Ghost form on Newbie death undesirable; jump boost requested for flat-ground takeoff. Starter kit function chat visible, Tome six guides observed (12 configured), functionality good. Replace command-based bundle with normal personal item rewards distributed across several quests; remove paper placeholder for Choose your magic controls. Move Ring of Seven Curses to optional post-graduation General Progression `Make It Hardmode`; owner tentatively favors removing Unwitnessed Amulet entirely (clarify scope, not silent item deletion). Claimed house fails placement: repeated obstruction at 1332,160,197 and earlier 1097,112,166; exact frozen RegularHomePower confirms custom paper + direct vanilla placement, Generic native only. New native placement investigation #804. Screenshot upper-left EMC: 0; acquisition not confirmed, flag for immediate B01/visibility recheck. No raw screenshot committed. No source changes or new build.

## World 1 — graduation and Monk initiation (Nov 26 ~08:52 Arizona)

Owner advanced to Monk before isolated Normie testing. Chat shows time set 0 and 12000, First Light complete: tyr_progression:monk unlocked; Regular mode, Newbie false, Normie false, Core true rank 1, zero XP/points and MXP. Core auto-join confirmed; pure Normie deferred to separate fixture. Owner reports mountain obstructed sunrise attempt; configured clear sky, dry weather, direction, continuous 200 ticks, empty hands/unarmored, correct Overworld time windows; no proof mountain itself was the cause. Owner confirms unintended upper-left EMC HUD after graduation. Keep World 1 as Monk fixture and perform command surveys later in a consolidated OP batch; preserve all earlier evidence. No new build or game commands executed by coordinator.
