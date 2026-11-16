# Integration branch protection — repository-admin checklist

**Status:** not applied by the GitHub connector. GitHub administration/branch-protection write permissions are unavailable to this integration, so **the owner must activate the rule in the repository settings**. This document does not claim the branch is protected.

Target: `test/full-integration`. Keep `main` and historical snapshot branches separate.

1. Open **Repository → Settings → Rules → Rulesets → New ruleset → New branch ruleset** (or **Settings → Branches → Add branch protection rule**, if that's the interface shown).
2. Select branch **`test/full-integration` only**, ensure the rule is active, and require a pull request before merge. Do not allow force pushes or deletion of this integration branch.
3. Require the **lightweight `Verify bootstrap / metadata`** GitHub Actions status check. It runs packwiz/quest validation and the **read-only Integration Preflight** on integration PRs. Require the PR to be current with its target branch if practical for concurrent work.
4. Consider requiring an integration-owner review. If the repository has only one contributor, do not accidentally require someone else's approval in a way that prevents the owner from merging.
5. Do **not** require `Server Boot Smoke`, `Prism QA Package`, `full_smoke=true`, or a complete integration artifact as a merge status check. Those need a fresh owner request for each run.
6. After enabling, verify a temporary PR touching `manifest/integration-sources.json` cannot merge while the lightweight check fails; verify that no server smoke or Prism packaging is scheduled. Do not trigger a full smoke as a rule test.

`.github/CODEOWNERS` marks the integration-owned paths for human review; without enforcement through GitHub settings it is advisory, not a technical barrier.

## Why this matters

GitHub currently has automatic feature-branch deletion enabled. That is compatible with the new process because donor inputs must be frozen SHAs or marked as already merged into baseline. Previously, merged PR #229 left `manifest/integration-sources.json` referencing a now-deleted branch. A required read-only merge gate must catch that before source consolidation.

The previous integration job mutated its checkout on ordinary pushes. It has been split so **Repository CI + Module CI + Integration Preflight** remain automatic; **Integration Assembly + Server Boot Smoke + Prism QA Package** are manual-only, with separate approval semantics.

See [test levels](TEST-LEVELS-AND-TERMINOLOGY.md), [agent contract](../../AGENTS.md), and [integration sources](../../manifest/integration-sources.json).
