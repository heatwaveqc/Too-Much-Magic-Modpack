# A-QMOD — full Packwiz mod inventory and quest coverage baseline (2026-11-22)

**Execution type:** [A — automated offline](QA-EXECUTION-TYPES.md). **Target source:** `test/full-integration`, independent of current test-build authorizations. **Frozen scope is eligibility, not a frozen installed JAR list:** [issue #390](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/390) freezes the eligible universe through Christmas, while new approved pins/repairs can still be reconciled. Count the current Packwiz-indexed mod list, not the number of historical `manifest/mods.json` rows or speculative late-December build members.

## Source baseline — verified by repository read-only inspection

At the original 2026-11-22 `test/full-integration` snapshot:

| Item | Source-observed result |
| --- | ---: |
| All `index.toml` entries | 578 |
| Indexed `mods/*.pw.toml` metadata entries | **222** |
| Indexed mod paths with `metafile = true` | **222** |
| Duplicate indexed mod paths | 0 |
| `manifest/mods.json` entries | 234 |
| Catalog entries enabled and present in Packwiz index | **222** |
| Catalog entries not indexed | **12** |
| Enabled catalog entries missing from index | **0** |
| Indexed paths missing from enabled catalog | **0** |

This cross-check verifies **source selected membership**, not downloaded file bytes, built first-party modules, runtime loaded registries, Minecraft client or mods under review in separate PRs. No claim that 222 is the final December executable count.

The generated full inventory is [`manifest/quest_mod_roster_current.json`](../../manifest/quest_mod_roster_current.json). It contains **one row for each indexed third-party mod**, with name, slug, indexed Packwiz path, index SHA-256, category, chapter references and coverage classification, plus all 12 catalog-only entries separately. The source's `catalog_date` is **2026-11-15**; the Packwiz index is the current membership authority. The freeze policy #390 also names approved conditional/research candidates not necessarily represented by those 12 unindexed rows.

## Quest coverage classification

Owner direction: **no individual quests for dependency libraries, administrative tools or server-performance/optimization mods**. Classify **all** indexed mods, but exempt those classes from quest content obligations. This does **not** mean deleting them from the pack.

| Class | Count | Authoring obligation |
| --- | ---: | --- |
| Gameplay — substantial chapter or named section | 96 | Coverage review and content ownership |
| Worldgen / dimension / structures | 12 | Exploration section or full chapter depending on depth |
| Player-facing utilities and conveniences | 29 | Relevant Tips & Tricks or other named reference section |
| Libraries, optimizers, admin/infrastructure-only | **85** | **Inventory only; no quests needed** |
| **Total indexed** | **222** | |

**Current draft status:** the auto-join finds **85 mapped**, **52 requiring coverage review**, **85 technical-no-quest**. The 52 are candidate review tasks, *not proof* that 52 mods are omitted from our questbook: ID aliases, shared chapter sections and current not-yet-updated source layouts can account for some. Review particularly The Aether, BetterEnd, Arcane Archives, Bountiful and other distinct content before writing new chapters. The coverage audit must also consult the final selected Packwiz and mod capability research, not auto-generate a chapter per jar.

### Meaning of the three tests

- **A-QMOD-01 — selected mod census:** parse exact `pack.toml` + `index.toml` with Python's native `tomllib`; verify index hash, each metafile's SHA-256, its declared jar filename, side, provider/download hash and acquisition route; fail on duplicates and Packwiz–catalog enablement drift. The test is **source/JAR metadata**, not proof a provider link currently downloads.
- **A-QMOD-02 — materialized complete list:** deterministically regenerate the JSON snapshot; `--check` fails when it goes stale following pin changes; current mod list changes must update the snapshot through `--write`.
- **A-QMOD-03 — quest coverage planning:** classify by versioned source category, with explicit exceptions for `bountiful` and purely technical QoL helpers; keep every selected mod accounted for but raise named coverage gaps only for player-facing systems. Report unindexed catalog separately; never silently install a scope-frozen candidate based on its catalog presence.

### Commands (A only)

```bash
python scripts/audit_quest_mod_roster.py --check
python -m unittest discover -s tests -p 'test_quest_mod_roster_a.py' -v
```

To refresh after **an integration-authorized Pin/Quest manifest change**:

```bash
python scripts/audit_quest_mod_roster.py --write
python scripts/audit_quest_mod_roster.py --check
```

Runs automatically on relevant source PR changes via `.github/workflows/quest-mod-roster-a.yml` (and can be manually dispatched), without downloading mod JARs or running Minecraft. A default green PR CI reports source consistency **only**.

## Work ledger

**Code-ready/implemented**
- [x] Inventory exactly 222 currently indexed mod paths and compare 222 enabled catalog rows by slug (read-only source snapshot).
- [x] Emit deterministic 222-row selected roster + 12 unindexed catalog entries with source-side coverage classifications.
- [x] Type-A script, unit fixtures and CI workflow for drift, missing metadata/index hashes, correct categories and source ID joins.
- [ ] Reconcile actual runtime **first-party Java module/JAR** roster from the frozen assembled QA manifest and published package. Do not add unverifiable module identities or mix third-party and generated first-party counts.
- [ ] Reclassify 52 coverage-review entries against final pinned mod IDs, aliases, installed features, and [v4 quest architecture](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/498); decide chapter versus section without inventing arbitrary tutorials.
- [ ] Cross-check freeze scope #390 and live-test inclusion register for approved but not-yet-indexed candidates; separately mark required next test / alpha candidate / conditional / later / research / rejected.

**Owner design blockers:** None to run the Type-A source test or maintain the roster. Individual new mod inclusion still requires the usual owner approval and exact freeze policy; quest chapter designs are separate.

**QA statuses**
- A-QMOD-00 source index/catalog membership parity: **SOURCE-INSPECTED (222/222), not full local hash audit**.
- A-QMOD-01/02/03: **NOT RUN at a checked-out full repository in this handoff until CI reports an exact run**. `--check` verifies exact pinned source hashes and determinism, but is not a smoke test.
- B/C: **NOT APPLICABLE** to an offline inventory itself. Any mod's actual runtime loading/quest navigation belongs to the separately authorized assembled-build B/C plan.

**No new assembly, smoke, Prism package or client launch was triggered.**
