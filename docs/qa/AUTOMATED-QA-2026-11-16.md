# Automated QA results — 2026-11-16

## Identity and execution boundary

Executed section **A** of `docs/qa/NEXT-TEST-CODEX-CHECKLIST.md` (checklist blob `4300d8b189026229610c4715d750ef6cb411068a`), with readiness observations. Installed build and tested source: **`2aacf34893bc06f8048a7fabae644b3b91a1d1d4`**. Integration branch HEAD when fetched for the handoff was `e120186fdb0c3b08736b6cbf7c30d89ed17732a9`; do not confuse that later documentation/source head with the installed snapshot.

Windows/PowerShell, Python 3.12.10, Node, Java 21.0.7 (Prism runtime), **Gradle 9.2.1** matching integration CI. Official Gradle distribution verified against its published SHA-256: `72f44c9f8ebcb1af43838f45ee5c4aa9c5444898b3468ab3f4af7b6076c5bc3f`. Fresh source clone was clean, then fetched/detached at the installed SHA. Feature drafts were inspected in separate archive copies, never integrated. Generated compilation/cache outputs remain local.

- Downloaded Prism ZIP SHA-256: `81eb0ba78427dcffa93767654196c18e333198bd5815dd97d625d17895134e91`.
- All **161 installed mod files** with installer-recorded linked hashes match; pack hash matches manifest. These are checks against recorded local provenance, not independent upstream authenticity claims.
- Exactly two installed first-party JARs, both matching BUILD-MANIFEST: progression `55c92ea1867497248300f46ec129e87809dd86cd51c76daea35a1afea8ea25a5`; transformations `c66a53160d35899b605bb494765800b3c179e56676f1e19e20dc84e6c13c38ba`. No old `tmm_monk` or runtime MXP JAR.
- Monk/Hunter JSON inside the installed progression JAR equals exact-snapshot source JSON.
- User already created the disposable world; its existing logs were inspected read-only. **No new server/client launch, full smoke, Prism artifact publication, world mutation, B commands, or C gameplay test was performed.** Offline packaging regression uses dummy fixtures, not a distributable real pack build.

**Disposition: automated compile/static checks mostly pass, but this build has command and Hunter-recipe blockers. No gameplay or survival acceptance is implied.**

## A-case results

All source checks below use the full installed SHA above unless a feature SHA is explicitly listed.

| Case | Result | Executed evidence / limit |
| --- | --- | --- |
| A01 | PASS after checkout normalization | `python scripts/validate.py`: exit 0, 290 indexed files, 161 active mods, 170 desired entries. Initial exit 1 was Windows CRLF conversion; canonical Git blobs matched the index. Installed mod/JAR hash checks pass. |
| A02 | PASS static | `python scripts/verify_retirement.py`: exit 0. Active KubeJS/FTB source scan found no `vampirism:`, `werewolves:` or `vampiresdelight:` references. JAWR retained, old faction JAR absent. |
| A03 | PARTIAL; future assembly gate FAIL | Fetched all remote heads and compared installed workstream SHAs: all external selected workstream heads match; the two self-references to integration have advanced since the build as expected. All 13 explicit deferred entries retained. Reproducing only assembler's read-only classification logic finds **`feature/traders-vengence` unclassified** at fetch time. A future assembly would refuse it until classified. No assembly/mutation was executed. |
| A04 | PASS compile/unit/data | `gradle --no-daemon build` in `compat/progression`: exit 0, **43 tests, zero failures/errors/skips**. Source and installed JSON: Monk 30 skills, 16 milestones, terminal XP 14,300, Bedrock Rest; Hunter 31 skills, terminal XP 28,000. No live ability/exclusivity acceptance. |
| A05 | FAIL command contract | Eight `StringArgumentType.word()` inputs; no `admin xp` literal. Real Brigadier 1.3.10 fixture parses `tmm_progression:monk` as `tmm_progression`, leaves `:monk` unread. Admin branch is permission-level-2 gated in source; non-OP live permissions not exercised. |
| A06 | PASS compile/unit; PARTIAL integration behavior | `gradle --no-daemon build` in `compat/transformations`: exit 0, **23 tests, zero failures/errors/skips**. JAWR-only module compiles, retirement check passes; Archlich/Dracolich policy tests included. No live anchor precedence/AI test. |
| A07 | PASS static | `python scripts/verify_silver_equipment.py` and `python scripts/verify_materials.py`: exit 0. Five Eidolon tools/four armor recipes, no blanket removed-Werewolves bonus. Actual combat attributes remain in-world checks. |
| A08 | **FAIL loaded recipe**, unit policy checks pass | Build/unit suite passes; Hunter data has 720,000 ticks/tier, exhaustion 0.0005/tick, amplifier cap 3, no slot cap. Bio-Mutagen has requested 4 moss/4 rotten flesh/1 spider eye, **but installed world's RecipeManager rejects the ingredient JSON**. Stand-eligibility guards exist; live brewing is blocked by the missing craft. Mutation purge/cure path is absent from inspected progression source. |
| A09 | NOT BUILT runtime; PARTIAL source | No MXP JAR shipped. Installed source's older MXP math uses 1.2; configurable-growth stream is explicitly deferred. Feature `f0ab1cde706883a09929e2b82eba57482377ee17` source has default 1.02 and range 1.0–1.2. Payer/transaction staging and dedup exist in source but were not compiled/exercised for this case. Do not claim the new growth policy is installed. |
| A10 | PARTIAL static; **FAIL runtime reference** | `python scripts/validate_quests.py`: exit 0, five groups/44 chapters/177 quests; 19 Alcohol quests retained, retired chapters absent. Existing world log rejects `cookingforblockheads:oven`, still referenced by Food & Fishing. No same-build full registry export available; ghost-item coverage is incomplete. |
| A11 | PASS source; runtime audit NOT RUN | ProjectE recipe and progression gate validators exit 0; 17 recipe replacements enforce trophy/Chaos gates. EMC audit contract validator exits 0. This does not establish all registered items have EMC, nor close Matter Upgrader/transmutation bypasses. |
| A12 | PASS static | Materials validator exits 0; canonical Neo Vitae dust/circle mapping/restore checks pass. No live Catalyst Exchange, resource-charge or duplication claim. |
| A13 | PASS deferred branch static; NOT BUILT runtime | B20 validator exit 0 at `ac06472a19a5818063753123eb475f0cd59a1e98`: six guaranteed slots, twelve premium entries, configured tiers/affixed generation and pack hashes. Source blacklist inspected; no real trader spawned. |
| A14 | **FAIL Windows validator**, normalized content matches; NOT BUILT runtime | B19 validator exit 1 at `9500102fcfcc835274a0f1b190732ca67d4ca8a5`: generated JSON catalog mismatch. Cause: `str(relative_path)` yields backslashes versus forward-slash catalog keys. Separate read-only `.as_posix()` comparison confirms identical expected file set and all generated contents. Validator unchanged. |
| A15 | PASS available branch validators; NOT BUILT deferred runtime | B16 `bb5523ccf149ec3cf281302bcf0888b5646ab1a3`: CC/Iesnium validator exit 0. B17/B18 `842268976faeb3c2146ee1d19b2710a3b3f80010`: both locator/YUNG validators exit 0. Exact mod pin/worldgen/registry acceptance not inferred. |
| A16 | PARTIAL static | Six custom shop categories/47 offers/18 ore offers in source; quest validator passes reward constraints. Seven Sins Ring natural-loot entries empty with extended configs enabled; B20 runtime deferred. Full economic loops/UI/once-per-player claims not tested. |
| A17 | BLOCKED | No explicit before-and-after disposable save copies supplied. Live world's existence is not a paired offline save handoff; player data was not modified or used to claim persistence. |
| A18 | NOT RUN | No new full-smoke authorization. Existing client logs and manifest hashes inspected only, not a newly performed dedicated-server smoke/performance run. |

## Exact quick commands and outcomes

The fresh clone's `git rev-parse --abbrev-ref HEAD` returned `HEAD` (intentional detached snapshot), `git rev-parse HEAD` returned the tested SHA, and initial `git status --short` was empty; all exit 0. Only canonical newline bytes and generated local build files changed afterward; no semantic source/config changes or live instance writes were made.

| Command (from source root unless noted) | Exit | Scope/result |
| --- | --- | --- |
| `python scripts/validate.py` | 1 initially; **0** on canonical bytes | 53 index-listed files had CRLF conversion; each canonical `git show HEAD:<path>` blob matched recorded hash. Replacing disposable working bytes with those exact blobs restored the hash check. No index/expected hashes weakened. |
| `python scripts/verify_retirement.py` | 0 | TeamLapen retired; two first-party modules selected |
| `python scripts/verify_materials.py` | 0 | Dust priorities, circle mappings, stack restoration and guards |
| `python scripts/verify_silver_equipment.py` | 0 | Eidolon silver tools/armor and no global anti-lycan tag |
| `python scripts/verify_dragon_diets.py` | 0 | Source diet/charring/magical-food policy; no loaded-tag/gameplay claim |
| `python scripts/verify_tyz_migration.py` | 0 | Tyz exclusive backend, **four five-rank** initial Core Magic skills, original tabs, native XP disabled |
| `python scripts/verify_next_test_roster.py` | 0 | Target retirement roster |
| `python scripts/validate_quests.py` | 0 | Five groups, 44 chapters, 177 quests; static reward constraints |
| `node scripts/verify_projecte_boss_recipes.cjs` | 0 | 17 replacements; trophy/Chaos crafting gates |
| `python scripts/verify_emc_coverage_audit.py` | 0 | Audit contract only; no full registry export |
| `python scripts/validate_progression_gates.py` | 0 | Source boss/EMC gate policy |
| `python compat/magic/stage_preview.py --check` | 0 | Four skills, five ranks, eight overlays |
| `python tests/test_prism_qa_packaging.py` | 0 | Dummy-JAR offline regression; direct ZIP root/preferences/no third-party binaries |
| `gradle --no-daemon build` in `compat/progression` | 0 | 43 unit tests; two deprecated EventBusSubscriber API warnings; build 1m59s |
| `gradle --no-daemon build` in `compat/transformations` | 0 | 23 unit tests; build 1m49s |
| `python scripts/validate_cc_iesnium_pilot.py` | 0 | Additional existing CI quick check on exact installed source; no live peripherals |
| `javac -cp <Brigadier-1.3.10.jar> ParserProbe.java`, then `java -cp <fixture-and-Brigadier> ParserProbe` | 0 / 0 | Successful reproduction of the **defect**, not successful namespaced parsing: `parsed=tmm_progression remaining=:monk` |

Deferred-branch commands, run only in isolated archive copies: `python scripts/validate_b20_trader_stock.py` exit 0; `python scripts/validate_b19_gateways.py` exit 1; `python scripts/validate_cc_iesnium_pilot.py` exit 0 on B16; `python scripts/validate_b17_ars_locators.py` and `python scripts/validate_b18_yungs_ars.py` exit 0 on B17/B18. No files were imported into the candidate or live instance.

## Existing new-world log findings

Read-only inspection of user's already-started client/world, not an assistant-triggered boot. Startup scripts 3/3, client scripts 1/1, server scripts 11/11 (twice) report zero script errors/warnings. Integrated server and player login recorded. Script-loading success coexists with **52 recipe parse errors** and 194 ERROR-severity lines across the captured startup/world log; counts include repeats and optional integrations, not 194 distinct bugs.

1. **Hunter blocker, 07:04:10:** `Parsing error loading recipe tmm_progression:bio_mutagen ... Not a JSON object: "minecraft:moss_block" ... No ingredients for shapeless recipe`. The packaged recipe contains raw string ingredients. For this MC version use valid ingredient objects (e.g. `{"item":"minecraft:moss_block"}`), preserve all nine inputs and verify loaded recipe/actual craft. `BioMutagenRecipeTest` passed but failed to catch actual recipe codec compatibility; strengthen that validation before accepting Hunter brewing.
2. **Command blocker:** eight namespaced inputs use `word()`; `admin xp` absent. Repair parser/registration and add dispatcher-level parser/permission fixtures before suggesting B01/B02 command sequences. No runtime workaround invented.
3. **Quest registry:** server rejects `cookingforblockheads:oven`; source Food & Fishing still uses it for icon/task. Registry validation is needed beyond structural quest checks.
4. **ProjectE/Goety:** server recipe mapping errors for `goety:savage_tooth_craft` because `goety:totem_of_roots` container lookup throws. World continues; PECore/FATAL is the logger marker on an ERROR line, not proof the process crashed.
5. Other resource/integration errors persist: ProjectExpansion missing declared access transformer; GeckoLib Theurgy/Dragon Survival/Ars animation parsing; Botania barrel models; optional cocktail/vegan and Pastel/Botania/Malum recipe references. Example errors include `pastel:blackest_lotus`, `pastel:least_black_lotus`, `vegandelight:soymilk_bucket`/fluid and Malum spirit `minecraft:arcane`. Triage optional-integration errors separately from the first-party Bio-Mutagen blocker; do not assume visual symptoms or global failure from logging alone.

## Readiness and follow-up

R01/R02 retirement and installed identity pass bounded static/hash checks. R03 Monk/Hunter union matches installed data and compiles. **R04 fails** command contract. R05 compiles/tests but live event arbitration remains unaccepted. R06 silver source passes. R07 one Tyz backend, MXP not built. R08 retains 13 known deferrals but future branch classification needs `feature/traders-vengence`. **R09 fails oven runtime reference** and lacks full export coverage. R10 Ring static policy passes, later merchant acceptance deferred. R11 installed provenance verified; raw logs/worlds retained locally. R12 honored: no new full smoke, no stable promotion.

Next smallest fixes: Bio-Mutagen recipe codec; namespaced parser/admin XP fixtures; oven task/icon registry repair; Windows-safe B19 path comparison; classify the new trader branch. Then rerun affected quick tests and test the repaired exact build in-game. Section B and C remain NOT RUN by this automated pass. Canonical feature acceptance was not upgraded.

Only sanitized findings are committed. Full logs, JARs, local caches, saves, personal paths and player data remain local per AGENTS.md.