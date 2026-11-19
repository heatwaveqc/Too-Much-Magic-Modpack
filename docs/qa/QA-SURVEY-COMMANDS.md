# Optional survey commands for the next test session

[Source, installation and command reference](../../scripts/qa-surveys/README.md).

Eleven opt-in operator commands collect read-only registry/player/inventory/resource/recipe/trader/Gateway/Botania evidence; a separate local client helper exports key mappings on request. They are outside deployed `kubejs/` and disabled by default. No production gameplay files, engine modules, pins or packaging/workflows change.

Prepared on 2026-11-18 at owner request to write code now and stop. No helper was installed, no client/world booted, and no B/C test was executed. Runtime status is **NOT RUN** for all commands. Offline Node contract tests and pinned-Rhino parsing are separate evidence and do not establish in-game compatibility.

The next session should install manually into a disposable QA instance before launch, then run baseline/recipes/client capture before targeted before/after actions. Missing native adapters report UNSUPPORTED. Visual judgments, non-OP/multiplayer boundaries, natural worldgen and destructive fixtures remain explicitly manual. The full coverage matrix and API limits are in the reference above.

## Integration handoff (2026-11-19)

Keep these scripts outside Packwiz and the normal runtime even after the PR merges. Use the frozen #348 census as comparison evidence only, not a substitute for a new authorized test-world capture. Record the exact installed build and the helper source SHA for each future manual session, and remove the helpers afterward.
