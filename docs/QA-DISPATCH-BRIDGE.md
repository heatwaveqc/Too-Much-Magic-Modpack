# Owner-authorized QA dispatch bridge

This is the preferred connector launch mechanism when direct workflow dispatch is unavailable. The owner explicitly authorizes **each specific run**, including its integration commit and any omitted ready work. The bridge cannot independently authenticate a ChatGPT conversation: it trusts the repository owner's GitHub account and command as the authorization record; agents must enforce the prior explicit consent rule. GitHub direct manual dispatch remains available.

## Exact commands on [issue #291](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/291)

- `/tmm check <full 40-hex SHA>` — checks identity, exact head, quick CI and in-flight full launchers; **no assembly, boot, package, receipt or dispatch**.
- `/tmm smoke <SHA> <32 lowercase hex unique nonce>` — assembly and full dedicated-server smoke. No Prism QA artifact; may freeze a server-tested snapshot after PASS.
- `/tmm prism <SHA> <32 lowercase hex unique nonce>` — assembly, full dedicated-server smoke **and only on success** immutable Prism QA package. No untested ZIP or packaging-only path.

Use a new cryptographic random nonce for each explicitly authorized command, never reuse one. Verify the downstream run's SHA and requested mode before calling it started. A failed dispatch or uncertain transport result consumes its receipt; do not replay it. Do not blindly retry downstream failures. The bridge is limited to fresh (<30 minutes), new, unedited comments by repository owner ID 22598785 on the open dedicated issue. No shell evaluation of comment text; PR events cannot trigger the execution job; API writes are limited to the bridge, not tests. The bridge itself runs serially and rejects in-flight full launchers; downstream approved smoke concurrency is retained. Multiple simultaneous requests can be canceled/declined; do not submit a batch. Branch head is checked before and after validation and again in the actual runner before assembly. If a docs-only integration change lacks a green exact-SHA full integration quick run, stop; use normal manual quick checks or obtain a new approved tested head, never relax the guard. The exact source HEAD (including code) must not advance.

The receipt `qa-request/<nonce>` is an atomic lightweight Git ref to the approved integration commit, not a release or tested QA snapshot. Keep receipts for replay protection and audit. All bridge reruns are refused; direct manual owner-authorized workflows retain their existing policy. If the bridge API refuses actions permissions, inspect GitHub Actions repository settings and permissions rather than adding a PAT or weakening guardrails.

Guard tests use fake API calls only. A real `check` command after both PRs merge tests the issue event and read API on main without starting Minecraft. This tests identity and CI checks, **not** the live dispatch POST. The first real authorized run establishes end-to-end dispatch. 
