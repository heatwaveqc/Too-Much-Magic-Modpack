# Unstable QA results — run 44108614695

Frozen installed SHA: `322cf3f9f0deb303fc6e6c29b340273ed3f481f6`. [Checklist](UNSTABLE-QA-2026-11-17.md). Existing GitHub Server Boot Smoke and Prism QA Package PASS, no player QA performed. A offline recheck executed by Codex on 2026-11-17; B/C remain assigned to the owner. [Independent A report, exact commands and limits](AUTOMATED-A-REPORT-2026-11-17.md). No new server/client launch or world mutation.

| Case | Type | Result | Exact command / file / observed evidence | Next action |
| --- | --- | --- | --- | --- |
| A01 | A | PASS | Published ZIP checksum, root files, 205 installed hashes and four custom JAR hashes match; canonical-LF metadata validator exits 0. [Details](AUTOMATED-A-REPORT-2026-11-17.md) | Retain stated scope; gameplay remains B/C |
| A02 | A | PARTIAL | 28 frozen streams/lock reviewed; read-only preflight exits 0 but --strict-overrides exits 1 for silver-ore-materials: config/almostunified/unification/materials.json. No assembly attempted. [Details](AUTOMATED-A-REPORT-2026-11-17.md) | Resolve strict overwrite conflict before future assembly |
| A03 | A | PASS | All four modules build; 118 tests pass, first_party_runtime paths and offline dummy packaging pass; Trader requires exact native Iron's [1.21.1-3.16.3]. [Details](AUTOMATED-A-REPORT-2026-11-17.md) | Retain stated scope; gameplay remains B/C |
| A04 | A | PASS | ResourceLocationArgument replaces broken namespaced parser; OP-gated admin XP exists. Monk 30 skills, rank17 7700/rank20 14300; Hunter 31 skills/rank20 28000. GUI/TreeChop/native loot hooks included; live commands/drops remain B/C. [Details](AUTOMATED-A-REPORT-2026-11-17.md) | Retain stated scope; gameplay remains B/C |
| A05 | A | PASS | Bio-Mutagen has nine proper ingredient objects, 4 moss/4 rotten flesh/1 spider eye; Hunter mutation/potion/recipe unit tests pass; silver/material checks pass. Actual craft/brew still C08. [Details](AUTOMATED-A-REPORT-2026-11-17.md) | Retain stated scope; gameplay remains B/C |
| A06 | A | PASS | MXP runtime JAR present; growth default 1.02, range 1.00–1.20, Newbie fail-closed gate and spend/transaction/Tyz bridge source/tests present. Tyz and stage preview validators pass; no live spend proof. [Details](AUTOMATED-A-REPORT-2026-11-17.md) | Retain stated scope; gameplay remains B/C |
| A07 | A | PASS | Newbie integration validator exits 0 on canonical bytes: Smart Keep exact pin, personal graduation, Safekept disabled, mode gates/config; live per-world keepInventory/death not tested. [Details](AUTOMATED-A-REPORT-2026-11-17.md) | Retain stated scope; gameplay remains B/C |
| A08 | A | PARTIAL | Retirement/canonical blood/Dracolich assets validators pass; no TeamLapen or tmm_monk JAR. Installed transformation mixins include acquisition guards/death/visual hooks. Third-party werewolves_food missing-reference tag error remains in client log; no full native behavior acceptance. [Details](AUTOMATED-A-REPORT-2026-11-17.md) | Retain stated scope; gameplay remains B/C |
| A09 | A | PASS | Native Mahou CatalystExchange mixin limits change to catalyst eligibility; no global dropped-item tick shim; material checks pass. Mana/count/RNG are not gameplay verified. [Details](AUTOMATED-A-REPORT-2026-11-17.md) | Retain stated scope; gameplay remains B/C |
| A10 | A | PARTIAL | Quest and Ars validators pass 5 groups/44 chapters/227 quests, 49 Ars nodes; source locale/starter/filter repaired. Registry gate --help and synthetic selftest pass; no full same-build registry export supplied. No UI/claim pass inferred. [Details](AUTOMATED-A-REPORT-2026-11-17.md) | Retain stated scope; gameplay remains B/C |
| A11 | A | PARTIAL | ProjectE/gates/EMC contract validators pass, but no full registry EMC export or bypass experiment. Existing optional upstream recipe/loot mapping errors remain; all-item EMC not established. [Details](AUTOMATED-A-REPORT-2026-11-17.md) | Retain stated scope; gameplay remains B/C |
| A12 | A | PASS | B19/B20 validators exit 0 on Windows; 17 Gateways and configured trader stock validate. Vendetta module builds, 5 tests pass. Entity registry/actual traders/natural AI remain B/C. [Details](AUTOMATED-A-REPORT-2026-11-17.md) | Retain stated scope; gameplay remains B/C |
| A13 | A | PASS | Roster, CC/Iesnium, B17 and B18 validators pass; all 205 installed binaries match recorded pins. No same-build complete structure/entity export; worldgen not exercised. [Details](AUTOMATED-A-REPORT-2026-11-17.md) | Retain stated scope; gameplay remains B/C |
| A14 | A | PASS | Diet and Seasoned Fish source validators pass; shipped defaultconfig disables both initial/inventory dragon choice. All raw-fish variants and new-world config inheritance require C09. [Details](AUTOMATED-A-REPORT-2026-11-17.md) | Retain stated scope; gameplay remains B/C |
| A15 | A | PARTIAL | 315 fallback defaults and source registry-unbinding policy validate, vanilla 34 retained in observed registry scan. Existing client log has 2,563 Invalid key -1 occurrences; policy/API interaction needs investigation. C16 first-run/KeySets behavior not accepted. [Details](AUTOMATED-A-REPORT-2026-11-17.md) | Investigate unbound-key GL error flood; C16 not accepted |
| A16 | A | PARTIAL | Thermalily 75 mana/tick and zero-cooldown mixins shipped; source-only review. Eternal Steak/Relics operation not exercised. Cross-mod nested-bag guard and complete EMC boundary remain open, not passed. [Details](AUTOMATED-A-REPORT-2026-11-17.md) | Retain stated scope; gameplay remains B/C |
| A17 | A | PARTIAL | Existing authorized run 44108614695 confirmed completed/success; local client logs inspected read-only. New full smoke NOT RUN. Paired before/after save inspection BLOCKED (no supplied copies). [Details](AUTOMATED-A-REPORT-2026-11-17.md) | Retain stated scope; gameplay remains B/C |
| B01 | B | NOT RUN | — | Follow checklist |
| B02 | B | NOT RUN | — | Follow checklist |
| B03 | B | NOT RUN | — | Follow checklist |
| B04 | B | NOT RUN | — | Follow checklist |
| B05 | B | NOT RUN | — | Follow checklist |
| B06 | B | NOT RUN | — | Follow checklist |
| B07 | B | NOT RUN | — | Follow checklist |
| B08 | B | NOT RUN | — | Follow checklist |
| B09 | B | NOT RUN | — | Follow checklist |
| B10 | B | NOT RUN | — | Follow checklist |
| C01 | C | NOT RUN | — | Follow checklist |
| C02 | C | NOT RUN | — | Follow checklist |
| C03 | C | NOT RUN | — | Follow checklist |
| C04 | C | NOT RUN | — | Follow checklist |
| C05 | C | NOT RUN | — | Follow checklist |
| C06 | C | NOT RUN | — | Follow checklist |
| C07 | C | NOT RUN | — | Follow checklist |
| C08 | C | NOT RUN | — | Follow checklist |
| C09 | C | NOT RUN | — | Follow checklist |
| C10 | C | NOT RUN | — | Follow checklist |
| C11 | C | NOT RUN | — | Follow checklist |
| C12 | C | NOT RUN | — | Follow checklist |
| C13 | C | NOT RUN | — | Follow checklist |
| C14 | C | NOT RUN | — | Follow checklist |
| C15 | C | NOT RUN | — | Follow checklist |
| C16 | C | NOT RUN | — | Follow checklist |
| C17 | C | NOT RUN | — | Follow checklist |
| C18 | C | NOT RUN | — | Follow checklist |

Append dated evidence for each attempt: tester, installed SHA, environment, exact inputs, expected vs actual, result, sanitized evidence link, next action. Keep historical checklist/results intact. Existing CI evidence is linked above; do not mark A-case independent execution PASS until Codex actually runs it. No build or new full smoke authorized by this document.


## 2026-11-17 independent automated execution

Tested frozen installed SHA `322cf3f9f0deb303fc6e6c29b340273ed3f481f6`: ZIP, 205 installed mod hashes and four custom JAR hashes match. All four modules compile and **118 tests pass**. Routine preflight passes but strict mode fails an unapproved silver material config replacement. Existing client log contains 2,563 Invalid key -1 messages and two first-party marble stonecutting decode failures, plus upstream optional integration errors. Full [A report](AUTOMATED-A-REPORT-2026-11-17.md) preserves original Windows EOL/launcher failures and exact successful adaptations. No assembly, new smoke, new client launch, real packaging or live commands. Missing full registry exports/paired save copies leave relevant subchecks blocked; no gameplay or stable promotion.
