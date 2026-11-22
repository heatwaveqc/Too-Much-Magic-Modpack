# Too Much Magic — QA hub

**Working branch:** `test/full-integration`. **Target:** Minecraft 1.21.1 / NeoForge 21.1.265 / Java 21.

> **Next owner playtest — 2026-11-22:** [ONE Unstable + Experimental differential checklist](DUAL-CHANNEL-TEST-2026-11-22.md) and [Codex offline A runner](../../scripts/qa_compare_channels.py). This is the current planned **dual-channel** worklist, superseding the 2026-11-17 worksheet only for the upcoming playtest. It does **not** supersede old test evidence, authorize any server/Prism build, or assert a matching 222-mod Unstable Prism ZIP has been published. Both channels must be identified by their installed immutable source SHA. The older Unstable release is not a comparable 222-mod control.


## Start here

- **[Active unstable QA checklist — run 44108614695](UNSTABLE-QA-2026-11-17.md):** Codex A offline/source, owner B exact in-game commands, owner C play/setup. Frozen installed SHA `322cf3f9`; update its [results ledger](UNSTABLE-QA-RESULTS-2026-11-17.md). This supersedes yesterday's worksheet for this build, not the historical evidence.
- **[Required QA execution type standard](QA-EXECUTION-TYPES.md):** A/B/C for every future build; separate from canonical testing levels.

- **[Canonical QA/test levels](TEST-LEVELS-AND-TERMINOLOGY.md):** Repository CI, Module CI, Integration Preflight, manual Integration Assembly, Server Boot Smoke, Prism QA Package, Client Launch Smoke and Gameplay Acceptance. Record exactly which level ran.
- **[Codex next-test checklist](NEXT-TEST-CODEX-CHECKLIST.md):** ordered, executable quick checks, branch/readiness gates, in-game command recipes, and minimal setup tests. Codex should read it before testing or proposing a new QA build.
- **[Current test status](CURRENT-TEST-STATUS.md):** living, date- and SHA-scoped handoff. Record each attempted case, result, exact evidence, and unresolved blockers. It is not a substitute for historical test records.
- **[Live-test inclusion register](LIVE-TEST-INCLUSION-REGISTER.md):** required intake for features that any development chat or PR reports **ready for a live test**. All such features must be attempted for the next comprehensive live build, regardless of earlier `deferred_pending_runtime` classification.
- [Historical test ledger](../TESTING.md): past GitHub Actions runs and bounded smoke results.
- [Feature-by-feature acceptance checklist](../FEATURE-CHECKLIST.md) and [machine-readable registry](../../manifest/feature-checklist.json): the **canonical** feature IDs and evidence-based statuses. Do not duplicate or silently override those statuses here.
- [Prior client acceptance observations](../USER-TEST-LEDGER-2026-11-14.md) and [short client pass](../NEXT-INTEGRATION-TEST-PASS.md): earlier evidence and checklist, potentially superseded by the Codex checklist for the next build.
- [Build channel/acceptance policy](../BUILD-CHANNELS-AND-TESTING.md), [retirement contract](../TEAM-LAPEN-TEST-REMOVAL-2026-11-15.md), and [agent contract](../../AGENTS.md).

## Codex execution contract

1. Read `AGENTS.md` and the checklist. Check the **actual current branch SHA** and `manifest/integration-sources.json` before evaluating any case. For the next comprehensive live test, discover readiness handoffs beyond already-registered streams, then reconcile **every** ready feature against the assembled output using the inclusion register.
2. **Quick CI only by default:** Repository CI, Module CI and the **read-only Integration Preflight**. Neither frozen donor assembly nor source restore is automatic. Isolated Java compile/unit tests, read-only registry analysis, and read-only copies of provided disposable saves are allowed. Run commands from the repository root as the checklist specifies. Do not start a server or generate a test build without the owner's explicit instruction for that exact run.
3. **Do not run Integration Assembly, a full NeoForge smoke, a Prism QA Package, dispatch/call with `assemble_only=true` or `full_smoke=true`, or a push that indirectly launches these** without explicit owner authorization. The quick-path workflow is separate and is not a server PASS.
4. Mark a case `PASS`, `FAIL`, `PARTIAL`, `BLOCKED`, `NOT BUILT`, or `NOT RUN`. `PASS` is bounded to the described check; a passing static check is never a client/in-world pass. A `NOT BUILT` on a reported-ready feature is an unresolved **inclusion failure** requiring its own attempted-import/blocker record, not an accepted blanket deferral.
5. Update `CURRENT-TEST-STATUS.md` with evidence and commands, and append historical findings to `docs/TESTING.md` when a test has actually been performed. Update the machine feature registry only with evidence via the existing `scripts/feature_checklist.py` workflow. The inclusion register must separately show each ready feature as `INCLUDED`, `BLOCKED` with evidence, or `OWNER-DEFERRED` with an explicit decision; an old `deferred_pending_runtime` entry alone is insufficient.
6. Never commit downloaded mod JARs, generated worlds, test saves, player data, credentials, full logs, or copyrighted upstream mod assets. Link external CI/artifacts or commit small sanitized text summaries instead.

## Record template

| Field | Required |
| --- | --- |
| Timestamp and branch commit | ISO date/time and full Git SHA |
| Case ID | Rxx, Axx, Bxx, or Cxx from the checklist |
| Test level | Canonical CI-REPO / CI-MODULE / CI-PREFLIGHT / QA-ASSEMBLY / QA-SERVER / QA-PRISM / QA-CLIENT / QA-GAMEPLAY |
| Status | PASS / FAIL / PARTIAL / BLOCKED / NOT BUILT / NOT RUN |
| Commands and inputs | Exact commands; sanitized paths and relevant configuration |
| Evidence | CI run URL, log excerpt, screenshot reference, test output or read-only save comparison |
| Follow-up | Next smallest actionable repair or reason for deferral |

**The status file is a handoff, not a claim of tests that were never executed.**
