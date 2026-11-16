# Current test status — active QA handoff

**Created:** 2026-11-16 (America/Phoenix)  
**Branch:** `test/full-integration`  
**Source inspection:** repository files inspected in the November 16 readiness/recovery session; checklist initially committed at `45daad21634d6d59860b199da7cf7df9dc8c0557`. **This is not a tested build SHA.**  
**Last actual test run from this checklist:** **NONE**. No server smoke, new Prism build, or in-game test was run while setting up these documents.  
**Next instruction to Codex:** read [the executable checklist](NEXT-TEST-CODEX-CHECKLIST.md), run only its quick/source tests when authorized to work, and append evidence here. Read `AGENTS.md` before any action.

## New inclusion requirement (owner correction, 2026-11-16)

For the **next comprehensive live test**, every feature that any development chat/PR reported **"Ready for a live test"** must be **attempted for integration**. Historical `deferred_pending_runtime` is **not sufficient** to exclude a ready feature. The working inventory is [LIVE-TEST-INCLUSION-REGISTER.md](LIVE-TEST-INCLUSION-REGISTER.md), which explicitly reopens all 13 previously deferred runtime branches for re-review and tracks exact INCLUDED/BLOCKED/OWNER-DEFERRED outcomes.

**Do not reinterpret the old R08 "Known deferred" row below as permission to omit the same features again.** It records a prior narrowed retirement build's scope and now represents a **next-test inclusion-reconciliation task**. For a fresh comprehensive QA build, first collect readiness handoffs from every chat and PR, attempt each ready import, and give the owner a matrix of included and blocked work. A blocked import does not become an automatic owner deferral; a smaller/partial test requires the owner's scope decision.

The user has reported that some recent tests were completed and others could not be run; **no individual test result is inferred or marked PASS/FAIL from that statement alone**. Add exact run-specific evidence below when supplied. This policy documentation change itself does not start a new test.

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

All cases below are **NOT RUN in this QA handoff**. When executing, replace only the matching case row with the tested SHA, command, status and evidence; keep historical results in `docs/TESTING.md` rather than erasing them.

| Case | Result | Exact SHA | Command / evidence | Notes |
| --- | --- | --- | --- | --- |
| A01 | NOT RUN | — | — | Follow linked case in checklist |
| A02 | NOT RUN | — | — | Follow linked case in checklist |
| A03 | NOT RUN | — | — | Follow linked case in checklist |
| A04 | NOT RUN | — | — | Follow linked case in checklist |
| A05 | NOT RUN | — | — | Follow linked case in checklist |
| A06 | NOT RUN | — | — | Follow linked case in checklist |
| A07 | NOT RUN | — | — | Follow linked case in checklist |
| A08 | NOT RUN | — | — | Follow linked case in checklist |
| A09 | NOT RUN | — | — | Follow linked case in checklist |
| A10 | NOT RUN | — | — | Follow linked case in checklist |
| A11 | NOT RUN | — | — | Follow linked case in checklist |
| A12 | NOT RUN | — | — | Follow linked case in checklist |
| A13 | NOT RUN | — | — | Follow linked case in checklist |
| A14 | NOT RUN | — | — | Follow linked case in checklist |
| A15 | NOT RUN | — | — | Follow linked case in checklist |
| A16 | NOT RUN | — | — | Follow linked case in checklist |
| A17 | NOT RUN | — | — | Follow linked case in checklist |
| A18 | NOT RUN | — | — | Follow linked case in checklist |

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
| C13 | **PARTIAL — owner-accepted observed behavior** | **Unknown; friend-tested environment** | Owner reports friend crafted `dragonsurvival:hot_dragon_rod` from **1 Elder Dragon Dust + 1 Blaze Rod**; source JSON puts `count: 8` on one shapeless ingredient. No independent pack test SHA. | **Likely upstream source-code quirk, NOT a modpack failure.** Assume shipped one-dust behavior is intended. No override/retest required. |
| C14 | NOT RUN — **waived by owner** | — | — | Likely upstream `count: 3` single-ingredient source quirk; runtime not tested. Treat author's shipped recipe as intended; no override or mandatory test. |
| C15 | NOT RUN | — | — | Dragon Survival Forest/Sea/Cave Source of Magic native crafting; ore form comparisons; no recipe change |
| C16 | NOT RUN | — | — | Bottle Caps Treasure conditional: `desert_monster_ds` absent in current pack roster; verify final build then NOT BUILT/NOT APPLICABLE if absent |

## Session log (append one row per actual execution)

| Timestamp | Source/test SHA | Case ID(s) | Level | Status | Exact evidence / URL | Next action |
| --- | --- | --- | --- | --- | --- | --- |
| 2026-11-16 (initialization) | `45daad21634d6d59860b199da7cf7df9dc8c0557` | Documentation only | Repo inspection | NOT RUN | QA hub and checklist added; no new CI or game tests executed | Codex to run authorized quick preflight |
| 2026-11-16 (friend playtest reported by owner; later reclassified) | **Unknown / external test** | C13 | User-reported live crafting | **PARTIAL — OWNER-ACCEPTED BEHAVIOR** | Hot Dragon Rod crafted from one dust + blaze rod; upstream ingredient JSON has ineffective-looking `count: 8`; exact build SHA not reported | Owner treats it as likely upstream source-code quirk, accepts current crafting behavior; no modpack bug/override; C14 similar source quirk remains untested and waived |

### Evidence format

Use `PASS`, `PARTIAL`, `FAIL`, `BLOCKED`, `NOT BUILT`, or `NOT RUN`. Always supply exact commit, executed command, meaningful output and limited interpretation. For a failing test, record expected vs actual, a reproducible fixture, and the smallest proposed repair.

If an agent has only inspected documentation or committed source, **do not upgrade any case to server-smoked or player-pass**. Canonical feature acceptance remains `manifest/feature-checklist.json`; the repository's full test history remains `docs/TESTING.md`.

## Dragon Survival native recipes — accepted upstream quirks and remaining tests (2026-11-16)

**Owner's latest decision:** Hot Dragon Rod (`C13`) and Spearman Promotion (`C14`) should be marked as **likely bugs in Dragon Survival's own recipe JSON**. They are **not modpack bugs to repair**. Hot Dragon Rod's one-dust + blaze rod behavior was observed by a friend and reported by owner; this is now **accepted as the mod author's intended shipped behavior**, despite the source `count: 8` field. Spearman Promotion contains the analogous `count: 3` field, but no in-game behavior is established; assume the shipped behavior is intended. **Do not build KubeJS overrides and do not require further tests for C13/C14.** `C15` the three Sources of Magic remains queued for normal next live test. `C16` Bottle Caps Treasure remains conditional on `desert_monster_ds`; if absent record NOT BUILT/NOT APPLICABLE and do not add the addon.
