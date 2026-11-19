# Branch lifecycle and integration authority

Owner-approved 2026-11-19. Applies repository-wide; read with [AGENTS.md](../AGENTS.md) and [branch/progression workflow](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/test/full-integration/docs/BRANCH-AND-PROGRESSION-WORKFLOW.md). Where older instructions require repeated owner approval for ordinary merges within approved scope, this document delegates that review to the designated integration writer. Existing safety/build restrictions remain in force.

## Before writing

- Look up related issues, PRs, branches, source locks and integrated code. Resume the existing workstream, even after a conversation fails; do not fork its work again. Explicitly transfer writing responsibility before resuming another agent's branch.
- Use issues for ideas and postponed work. A branch is for a concrete, reviewable change. Keep related work together, but separate independently mergeable slices or actual dependency boundaries. Do not create scratch branches for each checkpoint or each chat.
- Each temporary branch must have an accountable draft/ready PR within 24 hours (prefer immediately after first commit). Handoff: linked issue or workstream ID, designated writer, target, owned paths, approved scope, dependencies/stack, current checkpoint SHA, quick checks, blockers, next step. Maintain the same PR across interruptions.

## Continuous integration

Development writers implement and run scoped quick checks; designated subsystem writers reconcile subsystem slices; the modpack integration writer owns shared integration manifests, relocking, collision resolution, readiness and releases. No concurrent writers on a shared branch. The integration writer may merge reviewed, independently coherent slices of already approved work, including routine metadata, policy, configs and bug fixes within approved scope, without another feature approval. Read exact current head/base and check all applicable required quick checks; reconcile shared paths and dependency ordering; use expected head SHA on merge. Do not mark a draft ready merely to bypass a real blocker. Get owner direction for new mechanics, balance, architecture, unresolved design choices or scope expansion. Respect declared workstream ownership and coordinate handoffs. No automatic merge based solely on green checks.

SOURCE READY means ready for integration review, not installed/assembled/server/client/gameplay accepted. Record required future QA and preserve inclusion obligations before merge. A source-ready PR does not require a Minecraft boot to merge. Full assembly, full smoke, Prism package and stable release remain separately owner-authorized. Reconcile manifest donors to baseline-owned or retained immutable, reachable source plus provenance; do not strand active source refs or stacked PRs. Retarget or migrate dependents before a merge that deletes their base. Close superseded PRs only after needed work is preserved and successor links recorded.

## Finish the lifecycle

A task is administratively complete only when integrated or explicitly deferred/closed, handoff/provenance is complete, and the temporary branch is retired or its retention reason and next review date are recorded. Keep existing branch when active. Archive approved historical commits using reachable history or immutable tags only where needed; verify preservation and do not treat an unreferenced SHA as durable archival storage. No dummy merges, force rewrites or blanket deletes.

Permanent allowlist: main, test/full-integration, dev/progression, dev/art. Owner approval required for any additions. Existing other branches are transitional and must be reviewed, not automatically deleted. Protect permanent refs against deletion and force pushes in GitHub settings. Enable native automatically delete head branches for normal meaningful merges, after checking stacked/manifest dependencies.

## Budget and service targets

Normal goal 12–20 total remotes (including permanent); <=20 healthy, 21–30 review, >30 maintenance. Review temporary branch without PR after 24h, ready PR waiting 24h, inactive development after 72h, merged branch surviving 24h, unresolved external donor after 7d. These are review triggers, not hard caps, deletion permission or exact age claims from commit dates alone. Record explicit exceptions with owner, reason and next review. Prioritize ready work continuously; do not wait for release week.

Daily read-only inventory reports branch tips, current open PR heads/bases, frozen external donors, dependency stacks, draft/ready status and age proxies. PR collision report flags exact changed-path intersections among live same-base PRs. Cross-base ownership still needs manual review. Inspect current integration-source manifest and existing Integration Preflight on every relevant PR; read-only inventory is advisory and cannot prove behavior equivalence. Missing/ambiguous data blocks retirement, not a reason to guess.

## Safe retirement

Follow the independently reviewed batch procedure in AGENTS.md. The historical 46-entry runner and manifest are immutable history. For future batches use `scripts/branch_lifecycle.py` and the Branch retirement workflow. Prepare `maintenance/branch-retirement/<batch>.json` in its own main PR, with schema 1, batch name matching file stem, and nonempty branches array of name, full SHA, reason and evidence (issue/PR/commit links). Preserve necessary code and immutable references first. Require dry run against live refs and independent review of all candidates. Keep protected, active or uncertain branches excluded. Merging a reviewed manifest alone never deletes anything. Manually dispatch on main with the exact manifest path and SHA-256 of the reviewed bytes; execution requires explicit owner opt-in. Retire only the reviewed batch. For each candidate refresh refs/dependencies, require named ref still at approved SHA, and use Git lease-guarded deletion (no force rewriting). No scheduled/unattended pruning; no automatic retries. Report per-ref results and verify remaining refs. Note: the cross-API PR/manifest snapshot and Git ref deletion cannot be a single atomic transaction; coordinate a maintenance window and do not open new work on nominated refs during execution. Lease guards the ref itself.

GitHub settings/rulesets are administrative actions outside current connector write access; verify actual settings rather than assuming text implements protection. No workflow in this policy assembles a modpack, boots Minecraft, publishes or builds Prism.
