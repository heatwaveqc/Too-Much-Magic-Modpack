# Too Much Magic: testing levels and names

**Canonical terminology (2026-11-16).** Use these names in chat handoffs, PRs, CI summaries, QA ledgers, and artifact labels. A successful earlier level is never evidence that a later level passed. The old phrase "full Prism build" means **Prism QA Package**, not a tested graphical client.

| ID | Canonical name | What runs / what PASS proves | Normal trigger | Must not claim |
| --- | --- | --- | --- | --- |
| **CI-REPO** | **Repository CI** | Static schema, exact packwiz index and hash chain, quest/reward/config validation, exclusion rules, whitespace/EOL and source policy | Automatic push/PR | Java compilation, source assembly, server or client |
| **CI-MODULE** | **Module CI** | Java compile, unit tests and isolated KubeJS/recipe/feature validators | Automatic push/PR | Loaded runtime behavior |
| **CI-PREFLIGHT** | **Integration Preflight** | **Read-only** frozen-source commit availability, path ownership, overwritten-baseline audit and visibility of unclassified draft branches | Automatic push/PR | Integration Assembly or a complete ready-feature inclusion matrix |
| **QA-ASSEMBLY** | **Integration Assembly** | Explicitly authorized staging of pinned source commits, lock/manifest reconciliation, first-party module compilation and version records in an ephemeral candidate workspace | Owner-authorized manual dispatch: `assemble_only=true` or `full_smoke=true` | A running game, server-loaded mods, or distributable ZIP |
| **QA-SERVER** | **Server Boot Smoke** | Exact assembled NeoForge server with pinned mods + expected first-party JARs boots to ready in a disposable runtime; report JVM/loader/logs | Owner authorization for **this** run, dispatch `full_smoke=true` | GUI, recipes or gameplay acceptance |
| **QA-PRISM** | **Prism QA Package** | Commit-locked launcher/bootstrap ZIP with correct packwiz metadata, mod sources, preferences policy, manifest/hashes; this workflow packages it **after server smoke** | Same expressly authorized `full_smoke=true` run; separate result | A launching client or in-world mechanics |
| **QA-CLIENT** | **Client Launch Smoke** | Prism imports the exact ZIP, resolves mods, starts the graphical client and enters a **disposable** world; HUD/settings/basic GUI launch checked | Explicit client-testing assignment, generally interactive | Feature progression, economy balance, multiplayer |
| **QA-GAMEPLAY** | **Gameplay Acceptance** | Named recipe, path, quest, AI, multiplayer, persistence and compatibility cases pass with build and world evidence | Explicit targeted player/agent testing | Untested features or stable release approval |

**Quick CI** is an umbrella for `CI-REPO + CI-MODULE + CI-PREFLIGHT`. It does **not** build a full pack. Offline Prism fixture tests are `CI-MODULE`, not a real `QA-PRISM` artifact. A dry-run/preflight is not an assembly.

**Full integration test** is a *scope declaration*, not an extra test level. It means the readiness register was reconciled and every reported-ready feature is `INCLUDED`, `BLOCKED` with specific evidence, or `OWNER-DEFERRED` explicitly; a partial test requires the owner's agreement. It may perform several QA levels, and its result must identify which levels actually ran.

## Status vocabulary

- `NOT BUILT`: source/feature was absent from the exact artifact.
- `SOURCE READY`: implementation committed, no runtime proof.
- `PREFLIGHT PASS`: read-only source audit finished, no Minecraft boot.
- `ASSEMBLED`: candidate sources and pins staged; no server PASS.
- `SERVER PASS`: dedicated server actually reached readiness with exact JAR evidence.
- `PRISM PACKAGED`: QA ZIP exists with checksum; not proof of import or client success.
- `CLIENT PASS`: imported QA ZIP launched, entered a disposable world.
- `GAMEPLAY PASS`: named, actually executed in-world case passed.
- `BLOCKED`, `NOT RUN`, `FAIL`, and `OWNER-DEFERRED`: keep distinct. A source validator passing is not a workaround for an observed in-game failure.

## Required evidence for each result

Record **source HEAD and imported donor SHAs**, command/workflow and run link, level ID, environment (Java/NeoForge, OS, dedicated vs singleplayer), exit/result, exact generated JAR/ZIP hashes when present, and logs/screenshots or player observations for runtime tests. Maintain known last-good artifacts and never retroactively upgrade a historical test from an unrelated source revision.

A `full_smoke=false` push/PR workflow must never mutate the integration checkout, download the full roster, launch Minecraft, create a Prism ZIP, or publish a tested tag. `assemble_only=true` may assemble in a throwaway GitHub runner but does not boot or publish a game client. `full_smoke=true` is a fresh manual owner authorization covering assembly, server boot, and the QA ZIP; it is not a blanket authorization for client/play tests. A scheduled or implicit retry of either manual stage is prohibited.

## Branch and merge gates

1. Feature agents own branches and narrowly defined source paths. Only the integration writer reconciles `manifest/integration-sources.json`, shared quest/config owners, `index.toml` and `pack.toml` into the rolling integration branch.
2. External source streams record a **full immutable `source_commit`**. After merging a feature into baseline, mark `integrated_into_baseline=true`, preserve `merged_from`, PR and `merged_commit`, and stop importing that directory from a temporary branch.
3. Run `CI-PREFLIGHT` before any source restore. Review all candidate replacements of existing baseline files; an approved exception must name the path/pattern and give a concrete reason in `approved_overrides`. No silent `git merge` of a moving baseline.
4. New draft branches are **reported** by routine CI, not automatic assembly blockers. Once a chat/PR reports "ready for live test", register it; comprehensive test intake must attempt inclusion or document a blocker/owner decision.
5. Prohibit `[skip ci]` on commits affecting integration sources, build scripts, packwiz metadata, QA gating or shared runtime. Only source/metadata/compilation preflight is automatic. No automatic server, full assembly, or Prism packaging.

Related: [live-test inclusion register](LIVE-TEST-INCLUSION-REGISTER.md), [current QA ledger](CURRENT-TEST-STATUS.md), [build channels](../BUILD-CHANNELS-AND-TESTING.md), [agent contract](../../AGENTS.md).

## Execution-type axis (2026-11-17)

Use [QA execution types A/B/C](QA-EXECUTION-TYPES.md) for test assignment: A Codex/offline, B command-driven in game, C play/setup in game. These are orthogonal to the eight levels above; a source-only A check cannot grant a gameplay PASS.
