# Independent automated A checks — 2026-11-17

Tested installed/frozen source SHA: **`322cf3f9f0deb303fc6e6c29b340273ed3f481f6`**. [Build-specific checklist](UNSTABLE-QA-2026-11-17.md), checklist blob `68197d7f3e0abc6168f278f4f52e95f4c3044725`. These are independently executed A checks, not new gameplay acceptance.

Environment: Windows/PowerShell, Python 3.12.10, Java 21.0.7, Gradle 9.2.1, Node. Isolated source copy initialized as a detached Git checkout at the exact frozen commit; initial tracked state clean. Frozen donor commit objects fetched read-only. No Integration Assembly, new server/client launch, world command, save modification or real Prism publication. The user had already launched the new world; existing logs were read only. No full logs, saves, binaries, credentials or private keybindings are committed.

## Installed identity

- ZIP SHA-256 **`a2c2160c6f1aaaeca8328712f8215d57d036da795fd0abcd1fc9f9052924df96`** matches published checklist.
- All **205** installed mod files with installer-recorded linked hashes match; pack-file hash matches manifest. This verifies recorded provenance, not a fresh upstream trust audit.
- Exactly four first-party JARs match manifest: progression `86b8c689c88ee8d4cd4889bf6a085196c95ae4a62d37c4509651f5b4de2eb84b`; transformations `df993c87d35dc361bb0afa0d1d782af42e08030d66a21bf4b7bbd3bd3fbe1459`; MXP `5558c1b8ed3558a56a8034403e0f23ea3bb0d807eeca242dfc5a2be47a8420e1`; Trader `3ef34627bc57f47f1408e9fa2c227b102245d54779bf9e6b83cfd751f7ddc8e7`.
- Root `instance.cfg`/`mmc-pack.json` present; no nested ZIP or copied saves. ZIP's seeded options contain **zero `key_*` records**. Only four first-party JARs plus installer bootstrap bundled. No retired faction JARs.
- Installed source resources match snapshot resources after newline normalization: progression 36 files, transformations 12, MXP 1, Trader 3. Hash-matching installed JARs are not expected to byte-match new local rebuilds because build metadata/timestamps can vary.

## Case results

| Case | Result | Evidence and limit |
| --- | --- | --- |
| A01 | PASS | Published ZIP checksum, root files, 205 installed hashes and four custom JAR hashes match; canonical-LF metadata validator exits 0. |
| A02 | PARTIAL / strict preflight FAIL | 28 frozen streams/lock reviewed; read-only preflight exits 0 but --strict-overrides exits 1 for silver-ore-materials: config/almostunified/unification/materials.json. No assembly attempted. |
| A03 | PASS module/unit/packaging fixture | All four modules build; 118 tests pass, first_party_runtime paths and offline dummy packaging pass; Trader requires exact native Iron's [1.21.1-3.16.3]. |
| A04 | PASS bounded source/unit | ResourceLocationArgument replaces broken namespaced parser; OP-gated admin XP exists. Monk 30 skills, rank17 7700/rank20 14300; Hunter 31 skills/rank20 28000. GUI/TreeChop/native loot hooks included; live commands/drops remain B/C. |
| A05 | PASS bounded source/unit | Bio-Mutagen has nine proper ingredient objects, 4 moss/4 rotten flesh/1 spider eye; Hunter mutation/potion/recipe unit tests pass; silver/material checks pass. Actual craft/brew still C08. |
| A06 | PASS bounded source/unit | MXP runtime JAR present; growth default 1.02, range 1.00–1.20, Newbie fail-closed gate and spend/transaction/Tyz bridge source/tests present. Tyz and stage preview validators pass; no live spend proof. |
| A07 | PASS static | Newbie integration validator exits 0 on canonical bytes: Smart Keep exact pin, personal graduation, Safekept disabled, mode gates/config; live per-world keepInventory/death not tested. |
| A08 | PARTIAL | Retirement/canonical blood/Dracolich assets validators pass; no TeamLapen or tmm_monk JAR. Installed transformation mixins include acquisition guards/death/visual hooks. Third-party werewolves_food missing-reference tag error remains in client log; no full native behavior acceptance. |
| A09 | PASS static; exchange NOT RUN | Native Mahou CatalystExchange mixin limits change to catalyst eligibility; no global dropped-item tick shim; material checks pass. Mana/count/RNG are not gameplay verified. |
| A10 | PARTIAL | Quest and Ars validators pass 5 groups/44 chapters/227 quests, 49 Ars nodes; source locale/starter/filter repaired. Registry gate --help and synthetic selftest pass; no full same-build registry export supplied. No UI/claim pass inferred. |
| A11 | PARTIAL | ProjectE/gates/EMC contract validators pass, but no full registry EMC export or bypass experiment. Existing optional upstream recipe/loot mapping errors remain; all-item EMC not established. |
| A12 | PASS static | B19/B20 validators exit 0 on Windows; 17 Gateways and configured trader stock validate. Vendetta module builds, 5 tests pass. Entity registry/actual traders/natural AI remain B/C. |
| A13 | PASS pins/static; registry/worldgen PARTIAL | Roster, CC/Iesnium, B17 and B18 validators pass; all 205 installed binaries match recorded pins. No same-build complete structure/entity export; worldgen not exercised. |
| A14 | PASS static | Diet and Seasoned Fish source validators pass; shipped defaultconfig disables both initial/inventory dragon choice. All raw-fish variants and new-world config inheritance require C09. |
| A15 | PARTIAL; client diagnostic FAIL | 315 fallback defaults and source registry-unbinding policy validate, vanilla 34 retained in observed registry scan. Existing client log has 2,563 Invalid key -1 occurrences; policy/API interaction needs investigation. C16 first-run/KeySets behavior not accepted. |
| A16 | PARTIAL | Thermalily 75 mana/tick and zero-cooldown mixins shipped; source-only review. Eternal Steak/Relics operation not exercised. Cross-mod nested-bag guard and complete EMC boundary remain open, not passed. |
| A17 | PARTIAL | Existing authorized run 44108614695 confirmed completed/success; local client logs inspected read-only. New full smoke NOT RUN. Paired before/after save inspection BLOCKED (no supplied copies). |

## Commands and exit codes

All commands executed from the frozen source root unless noted.

| Command | Exit | Scope |
| --- | --- | --- |
| `python scripts/validate.py` | 1 | Initially CRLF conversion; canonical recheck exit 0 |
| `python scripts/validate_integration_sources.py` | 0 | Read-only source/static check |
| `python scripts/validate_integration_sources.py --strict-overrides` | 1 | Real unapproved baseline replacement, not bypassed |
| `python tests/test_prism_qa_packaging.py` | 0 | Dummy fixtures only, no real QA package |
| `python scripts/verify_silver_equipment.py` | 0 | Read-only source/static check |
| `python scripts/verify_materials.py` | 0 | Read-only source/static check |
| `python scripts/verify_tyz_migration.py` | 0 | Read-only source/static check |
| `python scripts/validate_newbie_integration.py` | 1 | Initially archive/Windows newline issue; canonical recheck exit 0 |
| `python scripts/verify_retirement.py` | 0 | Read-only source/static check |
| `python scripts/verify_canonical_blood.py` | 0 | Read-only source/static check |
| `python scripts/verify_dracolich_cave_assets.py` | 0 | Read-only source/static check |
| `python scripts/validate_quests.py` | 0 | Read-only source/static check |
| `python scripts/validate_ars_quest_pilot.py` | 0 | Read-only source/static check |
| `python scripts/validate_progression_gates.py` | 0 | Read-only source/static check |
| `python scripts/verify_emc_coverage_audit.py` | 0 | Audit contract only, not registry-complete EMC |
| `python scripts/validate_b19_gateways.py` | 0 | Read-only source/static check |
| `python scripts/validate_b20_trader_stock.py` | 0 | Read-only source/static check |
| `python scripts/verify_next_test_roster.py` | 0 | Read-only source/static check |
| `python scripts/validate_cc_iesnium_pilot.py` | 0 | Read-only source/static check |
| `python scripts/validate_b17_ars_locators.py` | 0 | Read-only source/static check |
| `python scripts/validate_b18_yungs_ars.py` | 0 | Read-only source/static check |
| `python scripts/verify_dragon_diets.py` | 0 | Read-only source/static check |
| `python scripts/validate_defaultoptions_keybindings.py` | 1 | Initially archive/Windows newline issue; canonical recheck exit 0 |
| `node scripts/verify_projecte_boss_recipes.cjs` | 0 | Read-only source/static check |
| `node scripts/verify_dragon_seasoned_fish.cjs` | 0 | Read-only source/static check |
| `python compat/magic/stage_preview.py --check` | 0 | Read-only source/static check |
| `python scripts/quest_registry_gate.py --help` | 0 | Help only; no live registry generated |
| `python scripts/quest_registry_gate.py selftest` | 0 | Synthetic fixture only; not actual full registry acceptance |
| `python scripts/first_party_runtime.py build` | 1 initially; 0 with Windows executable adapter | Initial WinError 2: subprocess cannot resolve bare gradle; same script rerun with only gradle executable mapped to installed gradle.bat |
| `python scripts/first_party_runtime.py paths` | 0 | All four built JAR identities verified |

The isolated Git archive initially materialized CRLF for some text files under Windows settings. Restored **251 files to exact canonical Git blob bytes**, only when their LF-normalized bytes equaled the blob; no expected hash/index was edited. Rechecks: `validate.py`, `validate_newbie_integration.py`, `validate_defaultoptions_keybindings.py` all exit 0. Final metadata reports **434 indexed files / 205 active mods / 220 desired entries**. Generated build outputs stayed local. No semantic source changes were made to force a pass.

The build runner's Windows adapter substitutes the exact Gradle 9.2.1 `gradle.bat` path for executable `gradle`, preserving all arguments (`--no-daemon clean build`), selected modules, test tasks, metadata verification and source. No validator/test assertion changed. Unit compilation may download compile dependencies; it did not run Minecraft.

## Module results

| Module | Tests | Failures/errors/skips |
| --- | --- | --- |
| tmm_progression | 53 | 0/0/0 |
| tmm_transformations | 38 | 0/0/0 |
| tmm_mxp | 22 | 0/0/0 |
| traders_vengence | 5 | 0/0/0 |

**118 tests pass.** Native recipe loading, rendering, controls, actual loot and mixin interaction are still outside plain-JVM tests. Trader metadata independently matches exact native Iron's version `[1.21.1-3.16.3]`, required/BOTH/AFTER.

## Actionable findings

1. **Strict Integration Preflight fails.** Routine preflight reports 28 streams but warns `silver-ore-materials: config/almostunified/unification/materials.json`; strict mode exits 1 because the frozen donor would replace baseline content without an approved override. Compare both versions, reconcile ownership/provenance and document an intentional reviewed exception if appropriate. Do not assemble or weaken this gate to hide the warning. This is a prospective source/assembly issue, not evidence the installed world cannot run.
2. **Client invalid-key error flood.** Captured log contains **2,563** `65539: Invalid key -1` messages (7,689 Window error lines, three-line reporting) during Render/Pre render. Source/client log confirms controls policy captured zero prior keys and scanned vanilla=34, saved mod keys=0, updated defaults=210, newly unbound=210. This establishes a fresh-client unbinding path and an error flood, **not which mod caused it**. Audit consumers querying unbound keys and interactions among registry unbinding, Default Options, KeySets and UI mods before blaming or removing one. Do not mark KeySets/fresh-controls C16 passed from this A log inspection.
3. **Two authored marble recipes fail runtime decode.** `tmm:stonecutting/common_marble/primalmagick__marble_raw__to__astralsorcery__marble_raw` rejects string `primalmagick:marble_raw`; the reverse rejects `astralsorcery:marble_raw`. Same ingredient-shape family as the old Bio-Mutagen defect, now in stonecutting. Use the pinned 1.21.1 ingredient codec shape and loaded-recipe verification. Source material validator passing does not prove those recipes loaded.
4. **Other existing client resource/compatibility errors:** 51 recipe parse failures, 10 loot-table parse failures and two TagLoader error lines in captured log. Examples: optional mod-integration recipes; Goety-Cataclysm `minecraft:random_chance_with_looting` loot condition; Hexerei `forge:can_tool_perform_action`; PsiTweaks/Mekanism-only components; missing `werewolves:werewolf_food` references from third-party compatibility data despite TeamLapen retirement. ProjectExpansion missing access-transformer and GeckoLib/model errors persist. Distinguish optional upstream failures from first-party defects; do not infer a server crash from ERROR severity.

## Source repairs and shipped scope now eligible for B/C

- Namespaced progression arguments use `ResourceLocationArgument.id()`; OP-gated admin XP grant exists. No invented admin setskill/mutation-progress commands.
- Bio-Mutagen recipe uses nine item objects; no corresponding first-party Bio-Mutagen recipe failure observed in supplied world log. Actual craft/JEI still needs the owner.
- 227 quests / 44 chapters / five groups; 49 Ars quests and 19 Alcohol quests present. FTB native first-log filter, starter reward and locale repairs are present. Rendering/claim counts are not A passes.
- Monk/Hunter JSON matches installed JAR. Monk rank 17=7700/rank20=14300; Hunter rank20=28000. Focus requires steady_hands. GUI key/screen, TreeChop credit hooks, native loot mixins and mode gates shipped; observed prior UI absence is not carried forward as a current absence.
- Dracolich skeletal rendering and Thermalily output/cooldown mixins are **shipped**, unlike yesterday. Thermalily source targets 75 mana/tick and zero cooldown, retains native consumption checks; live measurement still required. Native Mahou eligibility-only bridge also shipped.
- MXP now shipped with 1.02 default/range1.00–1.20, Newbie fail-closed eligibility, fractional wallet/transaction hooks. Boss registry empty by default; no configured registered boss rewards were found. JAWR native blood-spend callback and 37 actual form-node ability effects remain absent/incomplete; no claims of live delivery from source alone.
- Gilded, lava-fishing additions, deferred dimensions/final boss/art, dynamic mode-dependent darkness, full EMC coverage/tooltip gating, nested-bag boundary and Prefab restrictions remain omissions/open work as listed in the build handoff. Do not revive yesterday's blanket-deferral logic for later builds or mark omissions passed.

## Existing server evidence and boundaries

Read-only GitHub metadata confirms [run 44108614695](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/44108614695) completed successfully; run workflow head `c4ea80917001c492ff9bd00479bc29c2fc94c7a1` differs from the **assembled/frozen installed SHA above**, as the handoff/build manifest records. This is existing authorized smoke/package evidence, not a new A boot.

User's existing client logs: startup scripts **5/5**, client **1/1**, server **15/15** twice, each with zero script load errors/warnings. That does not negate recipe/loot/render errors later. No complete same-build item/structure/EMC registry export or paired before/after disposable save copies supplied; those subchecks are BLOCKED. The quest-registry selftest's two IDs are synthetic fixtures, not a full live item audit. No live NBT mutated, no permissions changed, no keybindings rewritten.

B/C rows remain for owner execution. Report source failures before asking the player to test affected behavior; do not repeat previously accepted unchanged baselines merely to fill tables. No candidate/stable promotion.