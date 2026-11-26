# November 25 unstable QA results

Authorized integration input `cc8bb188f0e0498dbc5007f6c4068bafe1481279`; installed frozen SHA **887a4ffb032c3579b9ee7d2d2cfd23da9dfbac6c** (baseline **aeee4fc6155691a2a6fcf1e98712ed783ab5a5d3**); downstream run, ZIP SHA-256 and artifact URL: **pending verification**. [Checklist](NEXT-UNSTABLE-QA-2026-11-25.md). Bridge dispatch accepted only; no client launch, game test, publication or smoke PASS inferred. Prior results preserved in [Thanksgiving results](THANKSGIVING-QA-RESULTS-2026-11-24.md). Record evidence against actual installed SHA before testing.

| Case | Status | Evidence / next action |
| --- | --- | --- |
| A01 | PARTIAL | Installed BUILD-MANIFEST identifies 887a4ffb; 255/255 packaged expected JAR hashes match. Downstream artifact/ZIP verification remains pending. |
| A02 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| A03 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| A04 | NOT RUN | Input source inspected; installed source/commands not verified. |
| A05 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| A06 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| A07 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| A08 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| A09 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| A10 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| AS-A01 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| A11 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| A12 | NOT RUN | Follow checklist; no same-build acceptance evidence recorded. |
| A13 | NOT RUN | Input source inspected; installed source/commands not verified. |
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
| C01 | FAIL | Verified 887a4ffb in-world server-thread crash 2026-11-25 21:07:14 Arizona: Reactive-book script line 29, ambiguous ItemStack.is(Item/HolderSet) during Primal Magick container loot generation. [Sanitized crash/log evidence](crash-2026-11-25-210714/README.md). |
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
