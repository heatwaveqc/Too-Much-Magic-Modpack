# November 25 unstable QA results

Authorized integration input `cc8bb188f0e0498dbc5007f6c4068bafe1481279`; current installed frozen SHA **20729a369e6f811730cea04cadfd12f265c96424**, baseline **cc8bb188f0e0498dbc5007f6c4068bafe1481279**; [build run 45751755305](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/45751755305), ZIP/artifact identity verified in A01. Earlier 887a4ffb instance was replaced during A execution. [Checklist](NEXT-UNSTABLE-QA-2026-11-25.md). Bridge dispatch accepted only; no client launch, game test, publication or smoke PASS inferred. Prior results preserved in [Thanksgiving results](THANKSGIVING-QA-RESULTS-2026-11-24.md). Record evidence against actual installed SHA before testing.

| Case | Status | Evidence / next action |
| --- | --- | --- |
| A01 | PASS | Current replacement 20729a36 / cc8bb188 verified: ZIP fc5a35fa... matches run 45751755305 artifact 15086506741; 258/258 installed JAR hashes match. Earlier 887a4ffb checks retained separately. |
| A02 | PASS | Existing run 45751755305 records successful preflight/assembly/compile/smoke/package. Independent current-source local compile: 596 Java cases pass; no new game process, assembly or package. |
| A03 | PASS | 254 pins, 258 JARs, three cozy additions, Relics 0.12.9, Modrinth Aquaculture Delight, Sodium/EMI and selected unified modules verified. Oaks recipe decoder defect separately A05. |
| A04 | PASS | Frozen command/OP/namespaced parser source inspected; installed Monk/Hunter/Core JSON equals frozen UTF-8 source. Classic Monk 55/21, Hunter 31 skills, Core 4. Runtime commands NOT RUN. |
| A05 | FAIL | 20729a36 also crashed 21:19:40: Reactive ItemStack.is ambiguity line 29. Current log: 17 new Oaks display-case recipe failures, 526 invalid-key lines; old decoder groups/ExMI/Goety mapping errors absent. Exact PR801 script later applied locally at owner request; native retest pending. |
| A06 | PARTIAL | Nov 26 current-build food/EMC reports collected: same 27,125 IDs, 2,634 foods, 11,410 positive EMC, 15,715 unmapped (15,696 nonexcepted). Fresh full effective recipe/native material/learned-pattern capture still pending. |
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
| B04 | PARTIAL | Owner-generated food census collected/validated Nov 26: 27,125 IDs, 2,634 food IDs; 28,187 reported recipe holders, 26,172 previews. No new helper or command by collector; full effective recipe serialization/native maps not captured here. |
| B05 | PARTIAL | All four owner-generated native EMC reports collected, same-invocation consistency validated. 27,125 items / 11,410 positive / 0 zero / 15,715 unmapped; coverage incomplete. Label 20729a36 retained, 258 JAR hashes independently verified. |
| B06 | PARTIAL | Pre-graduation EMC/material valuations hidden; after graduation/Monk upper-left EMC: 0 visible before intended Stone unlock, owner confirms defect. Other acquisition/independent player probes pending. |
| B07 | PARTIAL | Screenshot UUID matches saved player; saved TraderBlacklist membership true, one offender. Sanitized evidence uploaded; live status command, aggression attribution/targeting/Hero pardon not tested. No blacklist mutation. |
| B08 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| B09 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| B10 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| C01 | PARTIAL | World 1 post-#801 local overlay: owner reports successful entry, exploration, save/rejoin Nov 26, no recurrence of crash. Earlier 887a4ffb/20729a36 pre-overlay FAIL evidence retained; clean rebuilt package and actual Reactive replacement still pending. |
| C02 | FAIL | World 1 Nov 26: FTB hover text still black under Sodium; EMI looks good. Other icon/GUI-scale/transfer/category specifics not independently reported. #507 renderer defect persists. |
| C03 | FAIL | World 1: empty first-login inventory PASS; claimed starter kit via function, Tome opens/morphs correctly but only six guides observed vs twelve configured. Owner wants native personal item rewards distributed across multiple introductory quests; #306/#509. |
| C04 | FAIL | World 1: graduation home choice claimed, custom paper blueprint repeatedly refuses placement as obstructed. #804 native Prefab placement correction. General quest presentation sampled good; Choose your magic controls gives paper, unwanted Ring/Amulet in early Intro; owner changes routed. |
| C05 | PARTIAL | Newbie teleport, keep inventory and elytra work per owner; ghost suppression and flat-ground jump boost #287. Regular mode, Newbie left, Core joined verified after Monk initiation. Pure Normie and post-graduation ability/teleport denial not isolated; use later fixture. |
| C06 | PARTIAL | World 1 owner reports controls, reserved fist, tree felling work; TreeChop XP works qualitatively; Jade progress bar and exact key/log checks not yet confirmed. |
| C07 | FAIL | World 1 pre-graduation values hidden; upper-left EMC: 0 appears after graduation, owner confirms should not display before Stone. Isolate HUD provider and mode vs acquisition trigger; no post-unlock/independent-player proof. |
| C08 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| C09 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| C10 | FAIL | World 1 First Light, Reserved Fist, armor auto-unequip, harvest tiers, sprint/mining/TreeChop/combat MXP qualitative PASS; owner thinks increased movement works (not measured). Hand Mining stone/copper speed FAIL #805, reload stable. Exact combat rate/attribute deltas pending. |
| C11 | PARTIAL | Deep Breath, Water Breathing, Underwater Cultivation, Animal Friendship, Untouchable, Step Assist, harvest tiers observed. Jade incorrectly reports coal unharvestable despite successful drop; aquamarine shale no drop (iron-tier requirement suspected, verify actual registry predicate). Mining speed FAIL #805; remaining Monk trials paused at owner request pending speed repair. |
| C12 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| C13 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| C14 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| C15 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| C16 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| C17 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| C18 | FAIL | World 1: villager ranged damage works, gun model absent; owner confirms ranged engagement: golem aggroed, advanced and used melee rather than firing; both weapons invisible (provenance/LOS/exact distance not established; #803); aggression persisted into second village. Commands, relog, Hero pardon, stock, ranged setup and safety remain untested. |
| C19 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| C20 | PARTIAL | World 1: owner observed missing/broken texture on a Stray spawner attributed to individual/multiplayer spawner mod; exact registry ID/screenshot pending. Multiplayer independent state and cooldown NOT RUN. |
| C21 | PARTIAL | Owner reports improved structure density; Alex's Caves/Tectonic warning unresolved. Nighttime stalking warning matches Born in Chaos Nightmare Stalker; investigation #807; no direct encounter reported. |
| C22 | PARTIAL | World 1: no further Reactive crash reported after #801 local script overlay; no confirmed Reactive-only/mixed/configured chest book or filter diagnostic log yet. Remaining loot/Gateway/fishing cases NOT RUN. |
| C23 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| C24 | PARTIAL | World 1 save/rejoin good. Nov 26 screenshots: Java process 11,111.5 MB (Task Manager), system memory 90%; F3 5976/8192 MB heap, 8080 MB allocated, off-heap +1101 MB, allocation 642 MB/s, 60 FPS, integrated server 19.2/50 ms. One snapshot, no leak inference; #802. Long-play/multiplayer pending. |
| C25 | PARTIAL | Owner approves Friendship is Magic visual presentation (looks amazing); task credit, rewards, exact zoom and native progression still not independently verified. |
| C26 | PARTIAL | World 1: Reserved Fist, Armor Lock, Step Assist, Arrow Catching work per owner; movement increase likely; Patient Defense looks functional (provisional, exact health comparison not supplied). Flying Kick suspected failure, controlled setup pending. |
| C27 | FAIL | World 1 innate Otherworld Sight reveals trees (brief texture flash), direct Otherwood and sapling drops correct per owner. TreeChop felling produces regular oak instead; #808 native compatibility investigation. Iesnium/Palm and other magic tests remain pending. |
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

## World 1 — Classic Monk harvest and UI, Nov 26 ~08:55 Arizona

Owner reports Reserved Fist works, armor auto-unequips, tree felling works; sprint/mining/tree chopping grant MXP, harvest level increases work. At Hand Mining rank 2 stone and copper ore very slow; rank 3 dirt/sand instant but stone/copper still slow after reload. Reload stable. Exact speed, drops and XP deltas not measured. Frozen `EffectRuntime.breakSpeed` server-only early return is a candidate client prediction mismatch, not established root cause. #805 tracks diagnosis. Screenshot actionbar `Challenge: Harvesting: minecraft:wooden_pickaxe (2/5)` needs readable objective; owner asks transient custom-textured MXP bar above health/hunger, not vanilla XP/Botania bar, and hide inactive paths from normal navigation. #806 tracks UI scope. No new build or gameplay by coordinator.

## World 1 — Monk journey and night warning, Nov 26

Owner reports Deep Breath, Water Breathing, Underwater Cultivation, Animal Friendship work; Untouchable seems to give Step Assist; combat MXP looks good and movement feels increased (no quantitative measurement). Frozen Monk JSON confirms both Arrow Catching and Step Assist auto-achievement `classic/arrow_trial`; also seven total shared milestone mappings: first_sunset Reserved Fist+Armor Lock (entry constraints), deep_breath Water Breathing+Patient Defense, arrow_trial Arrow Catching+Step Assist, height_trial Feather Fall+Wall Climbing, bedrock_trial Bedrock Rest+Feign Death, blind_trial Blindness Ordeal+Night Vision, wither_trial Purification+Momentum Glide. Audit against owner one-reward rule; do not silently alter initial class constraints or invent trials. Owner says starter quest full rework handled by existing workflow (crafting table reward bowls still present); no parallel authoring. Nighttime message matches documented Born in Chaos Nightmare Stalker spawn warning, exact pin 1.7.6; #807 tracks narrow disabling research, no actual stalker encounter claimed. No build or game commands executed by coordinator.

## World 1 — Monk combat/UI follow-up (Nov 26 ~09:29 Arizona)

Owner confirms Arrow Catching works, requests organized Monk tree rather than scattered skills. Flying Kick appears not to work; controlled fixture needed, not yet definite FAIL. Frozen source: Flying Kick requires purchased rank, empty hand/unarmored eligible effect, valid native critical and negative vertical velocity, at least one block collision-free below feet, valid hostile primary and committed hit; rank 1 radius 3 around target, eligible hostile secondary targets, sweep sound/POOF ring. Test fully charged descending hit from two-block ledge, no sprint, grouped zombies; standing/normal jump negative controls. Patient Defense is scoped virtual Resistance I in native damage computation after 40 ticks without attacking, not visible potion; compare same generic damage after idle vs immediately after attack, no other Resistance/armor, heal/reset invulnerability and inspect health. Sprint XP sampler checks server sprint flag and horizontal distance, excludes riding/flying/hurt, does not exclude swimming; sprint-swimming expected to count when flag true, runtime not yet measured. Test swim sprint vs ordinary swimming separately. #806 tree layout follow-up, #721 combat evidence. No new build or game commands by coordinator.

## World 1 — Patient Defense and Otherworld Sight

Owner reports Patient Defense looks functional; provisional positive, no exact controlled damage values supplied. Otherworld Sight acquired; frozen monk.json grants tier 2, OtherworldSightClient reveals native blocks locally, OccultismOtherworldHarvestMixin targets natural logs/leaves/saplings/stripped logs and Iesnium native playerDestroy with OtherworldHarvest revealing server loot input. Actual tree reveal and correct native drops after Third Eye expires remain untested; native loot rules retained. Iesnium separately requires sight tier 2, Iesnium Palm, empty hand and correct harvest permission. No build or game commands by coordinator.

## World 1 — Otherworld harvesting

Owner confirms innate Otherworld Sight/direct Otherwood harvesting correct, direct sapling drops correct, brief ordinary-texture flash before revealed texture. TreeChop felling drops ordinary oak. Direct perception/harvest PASS, combined TreeChop loot FAIL; #808 tracks exact pinned adapter repair. Frozen TreeChopXpBridge handles XP receipts for indirect Block.dropResources and does not alter loot state; native Occultism playerDestroy hook is likely bypassed. No duplicate drop or precise XP count measured. No new build or game commands by coordinator.

## World 1 — Monk pause / registry batch

Owner requests mining speed repair before Iesnium and further Monk mining-dependent trials; preserve character, ranks and world. Jade labels blocks unharvestable despite successful coal harvest; investigate client harvest correctness separately from speed and actual server drops in #805. Aquamarine shale did not drop; owner suspects iron-tier restriction, exact native tool predicate not yet verified. Remaining Monk tests deferred pending local/installable repair; no new build authorization. Switch to read-only B04/B05/B07 command surveys, avoiding respec/path leave or progression mutations. Survey helpers optional and require explicit local staging/restart; native foodcensus and EMC audit first. Exact frozen SHA 20729a369e6f811730cea04cadfd12f265c96424 + unrelated #801 overlay. No commands executed by coordinator.

## November 26 — generated census and saved trader status collected

[Food/EMC/trader evidence](census-2026-11-26-20729a36/README.md). Current source 20729a36, 258 JAR hashes verified. Food/EMC exact registry sets agree; snapshot structure valid, EMC coverage incomplete. Owner-supplied UUID matched privately to saved blacklist: true; public identifiers omitted. No game command, helper, accusation/pardon, gameplay edit or build by collector. Existing World 1 observations above preserved.
