# November 26 food/EMC census and Trader's Vengeance status

Owner-requested collection for **20729a369e6f811730cea04cadfd12f265c96424**, continuing QA PR #796. Source reports were read from the current test save; no game commands, grants, accusation/pardon, gameplay edits, helper installation, restart or build were performed. Rechecked **258/258 installed JAR filenames/hashes** against the packaged inventory: PASS. The existing unrelated local #801 script overlay is not a new packaged build.

## Generated food census

[food-registry.json.gz](food-registry.json.gz) preserves the complete generated report as deterministic compressed JSON:

- **27,125 registered item IDs**, **2,634 default-stack food IDs**, including resolved item tags.
- The report records **28,187 loaded recipe holders**, **26,172 result previews**, **2,782 food previews** and **2,015 unresolved previews**.
- The food and EMC reports contain the **same exact set of 27,125 item IDs**.

Original scope flags are retained: default-stack food booleans/tags and separate recipe previews. These are not full ingredient/serializer payloads, component-sensitive consumption tests or nutrition/effect acceptance. Unresolved previews do not automatically mean broken recipes. The existing fresh effective-recipe/Replication material/learned-pattern capture obligations remain; this food report does not silently replace them.

## Native EMC audit

All four same-invocation reports are preserved under [emc/](emc/). Native capture timestamp **2026-11-26T17:02:09.214719900Z** (10:02 Arizona), mapping generation **1**:

| Measure | Count |
| --- | ---: |
| Registered items | 27,125 |
| Positive default-item mappings | 11,410 |
| Mapped zero | 0 |
| Unmapped | 15,715 |
| Missing outside current policy exceptions | 15,696 |

The existing `audit_emc_snapshot.py` independently validates the four reports' counts, exact ID sets, source label and status lists. [Validation report](emc-validation.json.gz): structurally consistent, **coverage incomplete**. The command's native total mapped-entry counter is **12,058**, distinct from the **11,410** positive default-item IDs serialized here; do not substitute one for the other.

The original operator-provided source label and `source_sha_verified: false` remain intact. Installed inventory was independently verified, not inferred from that label. No EMC values, exception policy or item eligibility were changed, and successful snapshot validation is not complete EMC coverage or balance acceptance.

## Trader's Vengeance

**Saved blacklist status for the owner-supplied player: `true`.** There is one saved offender entry, and it matches the screenshot's UUID integer array and the saved player's UUID. The UUID, player name and raw blacklist are intentionally omitted from public evidence.

[traders-vengeance-status.json](traders-vengeance-status.json) records the sanitized result, source digest and saved-file time. Read `data/traders_vengeance_offenders.dat` with the existing read-only NBT decoder; checked its `data.offenders` membership. This is the same membership predicate used by `TraderBlacklist.contains` and the source-verified `/tradersvengence status` command.

**This is a saved-state check, not a command executed in the open world.** Saved data may lag a running server. No matching target-specific blacklist/pardon message was found in the current client log. This does not prove the triggering aggression, live targeting/weapon safety, a new relog-persistence test, Hero pardon or natural trader behavior. No accusation or pardon was issued.

## Verification and handoff

[summary.json](summary.json) and [provenance.json](provenance.json) preserve counts, generated-source hashes/times and published-file hashes. Report content was canonicalized and gzip-compressed without dropping fields; original raw-file digests are retained separately. JSON/gzip round-trip, exact registry equality, reported food counts, native EMC snapshot validation, and private-identifier scans PASS. No save, raw player/blacklist file, UUID screenshot, full private log or JAR uploaded.

- **Implementation-ready:** collection/sanitization complete in existing #796, writer this QA collection chat; designated integration writer reviews/merges evidence under normal gates. No duplicate workstream.
- **Design blockers:** none for this collection. EMC pricing and remaining native export/recipe/creature investigations retain their existing owners and scope.
- **B/C queue:** B04 food capture collected; B05 EMC capture validated but coverage incomplete; B07 saved membership observed, live status/behavior/pardon checks pending. A06 remains partial until fresh effective recipes/native Replication data are collected. Preserve all other current QA ledger observations.
