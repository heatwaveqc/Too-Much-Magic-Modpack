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
| B01 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| B02 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| B03 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| B04 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| B05 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| B06 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| B07 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| B08 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| B09 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| B10 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| C01 | FAIL | Earlier 887a4ffb crash at 21:07 preserved. Replacement 20729a36 crashed at 21:19:40 with same Reactive overload error; sanitized stack in current A report. Owner-requested PR801 local script overlay applied after checks, live retest NOT RUN. |
| C02 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| C03 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| C04 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| C05 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| C06 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| C07 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| C08 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| C09 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| C10 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| C11 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| C12 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| C13 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| C14 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| C15 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| C16 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| C17 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| C18 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| C19 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| C20 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| C21 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| C22 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| C23 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| C24 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| C25 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| C26 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| C27 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| C28 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
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
