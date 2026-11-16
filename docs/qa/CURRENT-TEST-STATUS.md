# Current test status — active QA handoff

**Created:** 2026-11-16 (America/Phoenix)  
**Branch:** `test/full-integration`  
**Source inspection:** repository files inspected in the November 16 readiness/recovery session; checklist initially committed at `45daad21634d6d59860b199da7cf7df9dc8c0557`. **This is not a tested build SHA.**  
**Last actual test run from this checklist:** **2026-11-16 automated section A**, installed/source SHA `2aacf34893bc06f8048a7fabae644b3b91a1d1d4`. [Full command results and blockers](AUTOMATED-QA-2026-11-16.md). No new server smoke or B/C gameplay run.
**Next instruction to Codex:** read [the executable checklist](NEXT-TEST-CODEX-CHECKLIST.md), run only its quick/source tests when authorized to work, and append evidence here. Read `AGENTS.md` before any action.

## What is known from repository inspection (not runtime acceptance)

- PR [#165](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/165) retired TeamLapen Vampirism/Werewolves/Vampire's Delight from the integration manifest and pin index. Later repairs [#167](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/167) and [#168](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/168) were merged.
- The latest source examined includes **two** selected first-party modules: `tmm_progression` and repaired JAWR-only `tmm_transformations`. This does not prove that the next published ZIP has both.
- Integrated Monk JSON has 30 skills, rank-20 cumulative XP 14,300, 16 milestone entries and `bedrock_rest`; integrated Hunter JSON has 31 skills, potion enrollment and mutation policy. No gameplay results are implied.
- **Known command defect:** integrated `ProgressionCommands.java` still uses `StringArgumentType.word()` for namespaced command inputs and lacks the `admin xp` subcommand from the earlier core branch. R04/B01/B02/B04 command-driven progression requires a repair first. It is not appropriate to tell testers that the documented commands already work.
- `manifest/integration-sources.json` currently defers **13 runtime feature branches**, including B16 BetterEnd, B17/B18 YUNG, B19 Gateways, B20 trader, Thermalily/energy, and MXP configurable growth. They are **not built** in this targeted retirement QA unless a later final build manifest explicitly includes them.
- The full integration workflow has a default **quick** path and an opt-in `full_smoke` input. Per `AGENTS.md`, only the owner can authorize a specific full smoke and publication; do not schedule/trigger it from documentation work.

## R — Readiness evidence

| Case | Current assessment | Evidence | Next action |
| --- | --- | --- | --- |
| R01 | Source staged | Source inspection only | TeamLapen retirement recorded; static re-run not performed in this QA turn |
| R02 | Source staged | Source inspection only | Integration registry lists two first-party modules; dedicated-server verification not performed |
| R03 | Source inspected | Source inspection only | Monk 30 skills/14,300 terminal XP/Bedrock Rest; Hunter 31 skills/potion enrollment/mutations |
| R04 | BLOCKED | Source inspection only | Namespaced ID parser uses StringArgumentType.word(); merged command file lacks admin xp |
| R05 | Source staged | Source inspection only | JAWR-native transformation module restored; no in-world anchor or mob AI acceptance |
| R06 | Source staged | Source inspection only | Eidolon silver and silver_hunter intended; no in-world combat check |
| R07 | Conditional | Source inspection only | Tyz and MXP source/runtime boundary must be verified on exact assembled build |
| R08 | Known deferred | Source inspection only | 13 newer runtime feature branches are classified deferred_pending_runtime |
| R09 | Needs test | Source inspection only | Quest/Alcohol/retirement reconciliation and live registry still require checks |
| R10 | Conditional | Source inspection only | Ring blacklist static tests and later B20 merchant check |
| R11 | Needs test | Source inspection only | Final immutable build SHA/hashes and world safety not assessed |
| R12 | Guarded | Source inspection only | Default quick-check only; full_smoke requires separate owner authorization |

## A — Codex quick/static/save test results

The A rows below now record the **2026-11-16 automated pass**; PASS is limited to its stated static/unit scope. When executing, replace only the matching case row with the tested SHA, command, status and evidence; keep historical results in `docs/TESTING.md` rather than erasing them.

| Case | Result | Exact SHA | Command / evidence | Notes |
| --- | --- | --- | --- | --- |
| A01 | PASS | `2aacf34893bc06f8048a7fabae644b3b91a1d1d4` | [Run evidence](AUTOMATED-QA-2026-11-16.md) | validate.py exit 0 after canonical LF normalization; 161 installed mod hashes match |
| A02 | PASS | `2aacf34893bc06f8048a7fabae644b3b91a1d1d4` | [Run evidence](AUTOMATED-QA-2026-11-16.md) | retirement validator exit 0; active content scan clean |
| A03 | PARTIAL | `2aacf34893bc06f8048a7fabae644b3b91a1d1d4` | [Run evidence](AUTOMATED-QA-2026-11-16.md) | 13 explicit deferrals retained; future assembler classification rejects feature/traders-vengence |
| A04 | PASS | `2aacf34893bc06f8048a7fabae644b3b91a1d1d4` | [Run evidence](AUTOMATED-QA-2026-11-16.md) | Gradle build exit 0; 43 tests pass; installed Monk/Hunter JSON matches |
| A05 | FAIL | `2aacf34893bc06f8048a7fabae644b3b91a1d1d4` | [Run evidence](AUTOMATED-QA-2026-11-16.md) | Real Brigadier fixture leaves :monk unread; eight word() inputs; admin xp absent |
| A06 | PASS | `2aacf34893bc06f8048a7fabae644b3b91a1d1d4` | [Run evidence](AUTOMATED-QA-2026-11-16.md) | Gradle build exit 0; 23 tests pass; live bridge behavior untested |
| A07 | PASS | `2aacf34893bc06f8048a7fabae644b3b91a1d1d4` | [Run evidence](AUTOMATED-QA-2026-11-16.md) | silver/material validators exit 0; source-only |
| A08 | FAIL | `2aacf34893bc06f8048a7fabae644b3b91a1d1d4` | [Run evidence](AUTOMATED-QA-2026-11-16.md) | Policy unit tests pass but existing world log rejects Bio-Mutagen recipe ingredient codec |
| A09 | NOT BUILT | `2aacf34893bc06f8048a7fabae644b3b91a1d1d4` | [Run evidence](AUTOMATED-QA-2026-11-16.md) | No runtime MXP JAR; 1.02 feature source explicitly deferred |
| A10 | PARTIAL | `2aacf34893bc06f8048a7fabae644b3b91a1d1d4` | [Run evidence](AUTOMATED-QA-2026-11-16.md) | Quest validator passes 44 chapters/177 quests; oven runtime reference FAIL |
| A11 | PASS | `2aacf34893bc06f8048a7fabae644b3b91a1d1d4` | [Run evidence](AUTOMATED-QA-2026-11-16.md) | ProjectE/EMC/gate validators exit 0; source contract only |
| A12 | PASS | `2aacf34893bc06f8048a7fabae644b3b91a1d1d4` | [Run evidence](AUTOMATED-QA-2026-11-16.md) | Materials validator exit 0; live Mahou not tested |
| A13 | PASS | `2aacf34893bc06f8048a7fabae644b3b91a1d1d4` | [Run evidence](AUTOMATED-QA-2026-11-16.md) | B20 isolated branch validator exit 0; runtime NOT BUILT |
| A14 | FAIL | `2aacf34893bc06f8048a7fabae644b3b91a1d1d4` | [Run evidence](AUTOMATED-QA-2026-11-16.md) | B19 Windows separator mismatch; normalized read-only content comparison matches; runtime NOT BUILT |
| A15 | PASS | `2aacf34893bc06f8048a7fabae644b3b91a1d1d4` | [Run evidence](AUTOMATED-QA-2026-11-16.md) | B16 and B17/B18 isolated source validators exit 0; deferred runtime NOT BUILT |
| A16 | PARTIAL | `2aacf34893bc06f8048a7fabae644b3b91a1d1d4` | [Run evidence](AUTOMATED-QA-2026-11-16.md) | 47 custom offers/18 ores and Ring loot blacklist inspected; no transaction/economy testing |
| A17 | BLOCKED | `2aacf34893bc06f8048a7fabae644b3b91a1d1d4` | [Run evidence](AUTOMATED-QA-2026-11-16.md) | No paired supplied disposable before/after save copies |
| A18 | NOT RUN | `2aacf34893bc06f8048a7fabae644b3b91a1d1d4` | [Run evidence](AUTOMATED-QA-2026-11-16.md) | No new full-smoke authorization; existing client logs inspected only |

## B — Short command-driven player tests

Do not start B tests until there is an authorized test build and the exact command parser is verified. Missing features on the build are `NOT BUILT`; uncorrected parser is `BLOCKED`.

| Case | Result | Exact SHA | Player/command evidence | Notes |
| --- | --- | --- | --- | --- |
| B00 | NOT RUN | — | — | Follow linked case in checklist |
| B01 | NOT RUN | — | — | Follow linked case in checklist |
| B02 | NOT RUN | — | — | Follow linked case in checklist |
| B03 | NOT RUN | — | — | Follow linked case in checklist |
| B04 | NOT RUN | — | — | Follow linked case in checklist |
| B05 | NOT RUN | — | — | Follow linked case in checklist |
| B06 | NOT RUN | — | — | Follow linked case in checklist |
| B07 | NOT RUN | — | — | Follow linked case in checklist |
| B08 | NOT RUN | — | — | Follow linked case in checklist |
| B09 | NOT RUN | — | — | Follow linked case in checklist |
| B10 | NOT RUN | — | — | Follow linked case in checklist |

## C — Actual setup/client tests

These require a real installed QA build and the smallest necessary disposable world/setup. Results must specify whether the exercise was singleplayer, multiplayer, or server-only.

| Case | Result | Exact SHA | Screenshot/log/test evidence | Notes |
| --- | --- | --- | --- | --- |
| C01 | NOT RUN | — | — | Follow linked case in checklist |
| C02 | NOT RUN | — | — | Follow linked case in checklist |
| C03 | NOT RUN | — | — | Follow linked case in checklist |
| C04 | NOT RUN | — | — | Follow linked case in checklist |
| C05 | NOT RUN | — | — | Follow linked case in checklist |
| C06 | NOT RUN | — | — | Follow linked case in checklist |
| C07 | NOT RUN | — | — | Follow linked case in checklist |
| C08 | NOT RUN | — | — | Follow linked case in checklist |
| C09 | NOT RUN | — | — | Follow linked case in checklist |

## Session log (append one row per actual execution)

| Timestamp | Source/test SHA | Case ID(s) | Level | Status | Exact evidence / URL | Next action |
| --- | --- | --- | --- | --- | --- | --- |
| 2026-11-16 (automated execution) | `2aacf34893bc06f8048a7fabae644b3b91a1d1d4` | A01–A18; readiness observations | Static/unit, installed hashes, existing-log inspection | PARTIAL | [Exact commands/results](AUTOMATED-QA-2026-11-16.md); A05/A08 blockers, A14 Windows validator failure | Repair reported defects; B/C not run |
| 2026-11-16 (initialization) | `45daad21634d6d59860b199da7cf7df9dc8c0557` | Documentation only | Repo inspection | NOT RUN | QA hub and checklist added; no new CI or game tests executed | Codex to run authorized quick preflight |

### Evidence format

Use `PASS`, `PARTIAL`, `FAIL`, `BLOCKED`, `NOT BUILT`, or `NOT RUN`. Always supply exact commit, executed command, meaningful output and limited interpretation. For a failing test, record expected vs actual, a reproducible fixture, and the smallest proposed repair.

If an agent has only inspected documentation or committed source, **do not upgrade any case to server-smoked or player-pass**. Canonical feature acceptance remains `manifest/feature-checklist.json`; the repository's full test history remains `docs/TESTING.md`.


## 2026-11-16 automated execution summary

Tested installed/source snapshot `2aacf34893bc06f8048a7fabae644b3b91a1d1d4` using the later three-tier checklist. All 161 installed mod hashes and both custom JAR hashes match; 43 progression and 23 transformation tests pass. Command parsing/admin XP and loaded Bio-Mutagen recipe are blockers; oven reference and Goety/ProjectE errors persist. B19 fails on Windows path spelling despite matching normalized contents. The new `feature/traders-vengence` needs explicit future assembly classification. No full smoke or world mutation; B/C untouched. Earlier source-inspection/readiness rows above are historical context, not newer runtime acceptance. See [full report](AUTOMATED-QA-2026-11-16.md) for per-R readiness conclusions and feature-branch SHAs.
