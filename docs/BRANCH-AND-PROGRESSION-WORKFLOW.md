# Branch coordination and configuration-owned Progression paths

**Owner policy — 2026-11-16.** Applies to automated agents and human contributions. Read with [AGENTS.md](../AGENTS.md), [build channels](BUILD-CHANNELS-AND-TESTING.md), and the [QA level vocabulary](qa/TEST-LEVELS-AND-TERMINOLOGY.md). This policy governs future changes; it does **not** assert that all existing legacy code has already been converted.

## Architecture boundary: one executable Progression engine

**`compat/progression` / `tyr_progression` owns ALL executable Progression behavior.** The engine owns Java, public and internal APIs, event subscribers, mixins, networking, effect handlers, commands, registration, persistence, integrations and reusable validation/tests. Prefer generic, data-driven primitives and registries rather than hard-coded Monk/Hunter/Gilded switches. A path may refer to a registered effect or event in config; the engine evaluates it authoritatively.

**Monk, Hunter, Gilded, and future paths are declarative content packages, not independently coded mods or Java implementations.** A path workstream owns only its own JSON/datapack definitions, Tyz skill-tree JSON/config, relevant tags/data assets, localization, documentation and test fixtures/data. These can live under the current `compat/progression/src/main/resources/data/.../paths/` and `compat/magic/tyzs-test-overlay/config/tyzs_skills/...` structures, or a future schema-defined path content directory. It does **not** own Java, mixins, KubeJS/JavaScript/Python runtime logic, commands, or separate class-specific engine code. Do not create `compat/monk`, `compat/hunter`, or `compat/gilded` executable implementations.

**If a path needs new behavior:** identify the missing engine capability, open a separate `feature/progression-*` PR against `dev/progression` for the engine API/handler/mixin and its tests, and keep the `feature/monk-*` / `feature/hunter-*` / `feature/gilded-*` PR config-only. The same chat/agent may implement both in separate branches/PRs; separate chats are not required. The config PR may be stacked temporarily on the engine PR if it needs the new schema; document the dependency and retarget after the engine change merges. Generalize when practical, but all necessary code stays in Progression regardless of whether a particular capability is currently used by one path.

**Legacy exceptions are migration work, not precedent.** Some already-merged Java/classes and older PRs have path-specific logic. Do not claim they are gone or remove them casually. For new work, extend the engine boundary above; when touching old code, plan extraction/generalization under a Progression-owned PR with regression checks. No silent rewriting of other paths to meet this policy.

## Branch hierarchy and write ownership

| Branch / PR target | Purpose | Write rule |
| --- | --- | --- |
| `main` | Accepted policy/bootstrap reference, **not** the rolling combined test pack | No routine feature development |
| `test/full-integration` | Unified unstable modpack and owner-authorized QA-build source | Only the designated modpack integration writer reconciles shared runtime, packwiz, quest/config and source manifests; changes via PR |
| `dev/progression` | Long-lived, **coherent shared Progression subsystem** combining engine and declarative path content | One designated Progression integration writer merges reviewed PRs; never a scratch branch |
| `feature/progression-<capability>` | Engine Java/API/mixin/handler/schema/tests | Branch from latest `dev/progression`, PR targets `dev/progression` |
| `feature/monk-<capability>`, `feature/hunter-<capability>`, `feature/gilded-<capability>`, etc. | Config-only path definition, Tyz JSON and supporting non-executable content | Branch from latest `dev/progression`, PR targets `dev/progression` |
| Other `feature/*` branches | Separate mods, quest systems, pack features and unrelated repairs | Existing branch and integration ownership rules apply |

No shared-branch edits by concurrent agents. Agents may commit freely to their **own isolated feature branches** but cannot push directly to `dev/progression` or `test/full-integration`. The owner/integrator reviews and merges. Do not rebase or force-push a shared branch. Avoid long-lived `dev/monk`/`dev/hunter` branches: per-path work is short-lived and declarative.

## Discover, develop, hand off

1. **Before branching, search** GitHub Issues, open/merged PRs, live branches, and latest `dev/progression` and `test/full-integration` for the requested behavior. A feature already present in baseline must not be reimplemented because an ancestor PR is still open.
2. **Identify the workstream** with a linked Issue (create one when substantial work has no existing tracking item), primary area, current active PR, and whether core functionality is a prerequisite. Reuse the existing workstream identity after chat interruption.
3. **Declare the edit boundary** in the PR: expected data paths vs engine paths, base/head branch, dependencies/stack, acceptance criteria and planned QA level. If two PRs will edit a shared Tyz category registry or similar central data file, coordinate a single owner/reconciliation rather than overwriting it.
4. **Develop independently** on short-lived branches, commit/push recoverable checkpoints, perform scoped schema/static validation and Progression module compile/unit tests when applicable. No unattended full Minecraft boots or Prism builds.
5. **Handoff in the PR description** at interruption or completion: exact last commit, files changed, tasks complete/incomplete, integration dependency, test commands/results, last known good, blockers and next step. PR description and Issue—not an ephemeral chat—are the durable record.
6. **Merge into `dev/progression`** in dependency order, via PR, after reviewing combined data/config behavior and required quick CI. Never merge stale whole directories over newer ones. A green merge alone is not in-world acceptance.
7. **Synchronize both directions:** refresh active feature branches from the latest `dev/progression` regularly; bring relevant newer `test/full-integration` changes into `dev/progression` through a reviewed synchronization PR before promotion or when shared dependencies change. Inspect the diff and resolve real conflicts; never overwrite newer integration work with an old snapshot or replay deleted donor branches.
8. **Promote coherent slices to `test/full-integration`** through an integration-owner PR with frozen `dev/progression` commit SHA and a reviewed exact file/path diff. Follow `manifest/integration-sources.json` donor provenance rules. Do not wait for every path to be complete before integrating tested slices, but do not promote unreviewed partial engine/config combinations.
9. **Reconcile lifecycle:** mark original PRs fully integrated, partially integrated, superseded or blocked; link successor PRs and Issue decisions. Close superseded drafts only after confirming which commits/behaviors were preserved. Do not prune a branch still referenced by a mutable manifest or unresolved dependency.

## Progression path acceptance gate

- A new class/path should normally be implemented by *data only*: enrollment/rank/XP/milestones/skills/effects in path JSON, optional tags/assets, Tyz configuration, localization and documented test cases.
- If an ability requires an unimplemented handler, report **ENGINE CAPABILITY REQUIRED** with its separate core PR. The class config PR may remain draft/blocked until the engine PR is merged and module checks pass.
- Cross-class effects, networking, actor/event hooks, shared mixin lists and Tyz/runtime bridges are **engine work** even if motivated by Monk/Hunter/Gilded. Path data cannot inject custom executable behavior as a shortcut.
- Validate all affected paths when the shared engine changes. Preserve data/schema compatibility or provide an explicit versioned migration and regression coverage.
- Distinguish `SOURCE READY`, `ASSEMBLED`, `SERVER PASS`, `CLIENT PASS`, and `GAMEPLAY PASS`. If any feature was declared ready for the next live test, follow the mandatory inclusion register; no silent deferral.
- No automatic `Integration Assembly`, `Server Boot Smoke`, or `Prism QA Package`. Only the owner authorizes each run as specified in `AGENTS.md`.

## Preferred audited branch retirement

**Use a reviewed `main` maintenance PR plus GitHub Actions to prune obsolete *remote* branches.** This is the preferred approach even when the GitHub connector cannot call the ref-delete API directly. The proven one-shot implementation is [`retire-orphan-branches-2026-11-16.yml`](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/main/.github/workflows/retire-orphan-branches-2026-11-16.yml), [`scripts/retire_orphan_branches.py`](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/main/scripts/retire_orphan_branches.py), and [the first batch's exact-SHA allowlist](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/main/scripts/retired-branches-2026-11-16.json). [PR #266](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/266) and its [completed Actions run](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43989944032) removed 46 obsolete refs; no old branch contents were merged. A successful normal PR merge may also auto-delete its own head.

**Procedure for a subsequent cleanup:**

1. Inventory all current remote refs and review whether each branch contains unique implementation, design, testing or research worth retaining. Compare with current `test/full-integration` and `dev/progression`, not just an older merge base. Trace merged/superseding PRs, exact file/content provenance, frozen donor/source references, live-test inclusion obligations, and any work dependent on the branch. A closed PR, a nonmatching Git ancestry, or a file already present in the baseline is not enough to establish safe deletion.
2. Separate **KEEP / MIGRATE FIRST / CANDIDATE / UNCERTAIN**. Migrate or pin work worth preserving (including full source commits and links) before nomination. Explicitly keep `main`, `test/full-integration`, `dev/progression`, protected and currently active branches, and any branch that is an open PR's head **or base** or an unresolved source/QA dependency.
3. Create a dedicated maintenance branch from up-to-date `main` and prepare a **new batch-specific** manifest of exact names and 40-hex SHAs. Review code/workflow changes independently. **The historical 2026-11-16 allowlist is frozen and the original runner asserts exactly 46 candidates**; it cannot handle a fresh batch unchanged. Use the reusable main workflow adopted in PR #354 with a new batch-specific manifest and SHA-256 digest. Do not silently append names to the previous approved deletion list.
4. Open a `main` PR and run a **read-only Actions preflight** for that exact batch. Inspect all READY/SKIP/ERROR lines; the job must reject unsafe paths, require full-SHA head matches and no open PR references or active integration-stream references, and fail closed if the API/source manifest cannot be checked. Independently inspect any non-stream dependencies that the script cannot discover automatically.
5. After independent review, merge the batch manifest to `main` and require a separate explicit owner-authorized manual dispatch of the exact reviewed manifest digest to run deletion via GitHub's `contents: write` credential on `main`. Recheck each exact ref immediately before deletion and skip changed, active or protected refs. **Never update/reset old branches just to create mergeable PRs.** No broad unattended or scheduled deletion.
6. Review the completed Actions logs, confirm counts of deleted/skipped/errors and the actual remote branch list, ensure held refs survived, and link the cleanup PR/run for future provenance. A local `git fetch --prune origin` cleans only remote-tracking refs; it does not delete local branches.

The cleanup workflow is **repository maintenance**, not an alternative route to bypass integration approvals. It must not trigger Integration Assembly, Server Boot Smoke, Prism QA Package, or destructive edits to any shared runtime branch.

## Transitional handling of existing branches

Existing Monk/Hunter/Gilded drafts were created before this rule and may contain engine code, stacked ancestors or changes already selectively integrated. **Do not mass-merge, rename, force-rebase, delete, or assert compliance automatically.** First inventory each PR against current baseline, classify actual outstanding engine vs path-data changes, isolate needed engine changes into `feature/progression-*` PRs, retain immutable source SHAs, and document successor relationships. Establish `dev/progression` from the current coherent integration baseline, then onboard remaining work intentionally. Old branch names are historical evidence, not instructions to reinstall their complete file trees.

## Continuous integration lifecycle policy (2026-11-19)

Follow the canonical [branch lifecycle and integration authority policy on main](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/main/docs/BRANCH-LIFECYCLE.md) for PR accountability, branch-budget review, delegated scope-limited integration, retirement and permanent branches. This extends the workflow above; it does not alter single-writer, progression, source pinning, or owner-authorized full build boundaries.
