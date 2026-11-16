# Too Much Magic — repository-wide agent safety and CI rules

This file applies to all AI agents, automated contributors, coding workflows, scheduled tasks, and branches in this repository. For detailed integration procedures read [the active integration branch contract](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/test/full-integration/AGENTS.md) and [build channels](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/test/full-integration/docs/BUILD-CHANNELS-AND-TESTING.md).

## Owner approval required before any full Minecraft server smoke

- **By default, do not run a full modded NeoForge server smoke test.** Routine development checks are source/manifest/packwiz/quest/config validation, syntax checks, Java compilation and unit tests. Label these `QUICK CHECKS` or `STATIC/COMPILE PASS`, not `server-smoked`.
- **A new, explicit owner instruction is required for each full server smoke run.** Completing a feature, opening/merging a PR, updating the test branch, preparing a test build or test plan, checking build readiness, or ordinary permission to run tests is **not** approval to start a modded dedicated server.
- Agents must not manually dispatch, schedule, retry, use `workflow_call`, run `scripts/smoke_server.sh`, or indirectly trigger a full smoke via push to a legacy branch without such approval. Do not bypass this rule through alternate workflows, renamed scripts, or CI edits. Disable unguarded automatic full-smoke triggers before pushing to a branch that has them.
- The active [full integration workflow](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/test/full-integration/.github/workflows/full-integration.yml) defaults to fast validation/compilation. Its opt-in `full_smoke: true` input is permitted **only** following an explicit instruction from the owner for that run. Only then may CI boot the full NeoForge mod roster, freeze a server-tested integration snapshot and publish the corresponding tested Prism QA ZIP.
- Do not mark quick checks as in-game/server compatibility success, publish a smoke-tested build without the test, or promote a candidate/stable build without the established verification gates.
- Preserve the 2026-11-15 overnight test already in progress; **do not cancel or restart it**. This rule governs subsequent invocations.

When in doubt, complete the quick checks and report that the full smoke remains **NOT RUN — OWNER APPROVAL REQUIRED**. This instruction supersedes any older text in historical workflows or documentation suggesting an automatic full boot after each change.
