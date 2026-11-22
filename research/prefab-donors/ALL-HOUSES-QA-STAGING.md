> **CURRENT OWNER SUPERSESSION — 2026-11-22:** [#509](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/509) expressly **RETAINS** the native Prefab first-login starter-house item, ordinary starter gear/supplies, bonus chests and optional mining shaft. Prior instructions in this historical November 18–20 design/research document to disable these, confiscate head-start resources, or replace the native grant with a graduation-only home are **withdrawn**. The 18 custom house NBTs are **QA previews, not additional free personal grants**. The Progression `/tmmhome` pilot [#418](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/418) needs a separate nonduplication decision before activation. For active source and B/C tests, follow [Nov 22 QA housing record](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/test/full-integration/docs/qa/STARTER-HOMES-RECOVERY-NEXT-QA-2026-11-22.md) once integrated; do not interpret stale body below as current permission to disable native starting items.

# Custom housing — 18 structures, one native generic, disposable QA staging

**Scope:** source-backed implementation for the November 20 owner request to wire the approved custom-house blueprints to **Prefab Custom Addon 2.7.1**. The housing authoring branch is `research/prefab-donors-20261118`, frozen baseline `6c16c7756ca5a491be760c59de10cd68ee226e4d`. This feature branch is an isolated child PR; it **does not write to the parent or `test/full-integration`**.

## What is wired here

- `houses-qa-v1.json`: exact 18 approved custom house IDs, source generator path, output `.nbt` filename, all `grant:false`. The **nineteenth graduation choice** is native stock **Prefab Generic** with its 15 styles; **not** an additional custom NBT.
- `stage_all_houses_qa.py --verify-only`: create a temporary, throwaway copy of the housing source; run all **18** Python/Node stdlib generators; verify each gzip NBT (root compound tag, nonempty, below 8 MiB), file/path uniqueness and absence of command/structure blocks; print SHA256 checks for each output. No user game files touched.
- `stage_all_houses_qa.py --stage-qa-game-dir PATH --target client|server|singleplayer`: identical build+validation, followed by conservative copy into **only a marked disposable QA instance** (requires `PATH/.tmm-disposable-qa`). Client root: `prefab-extension/<id>.nbt`; dedicated server: `prefab-extension/server-cache/<id>.nbt`; singleplayer both. If an existing file differs, **refuse overwrite** rather than resetting a player's save/pack. This is an intentional manual QA staging step, not automatic production distribution.
- `test_houses_qa.py` and `.github/workflows/housing-custom-houses-static-qa.yml`: Python unit tests, static generation of every design, count/filename/SHA results under **quick Repository CI only**, without game server, Prism package or binary artifact upload. Workflow does not call any full smoke or `assemble_integration.py`.

Source importer: [Prefab Custom Addon `1.21.1-main` `ExtensionPackManager.java`](https://github.com/veyne0/prefab-custom-addon-github/blob/1.21.1-main/src/main/java/com/prefab/addon/extension/ExtensionPackManager.java), matched `mod_version=2.7.1`: standalone gzip `.nbt` files are accepted in the above directories, using filename stem as construction ID. No ZIP conversion required; standalone NBT lives in the construction list, **not necessarily the pack browser**.

### Reproducible quick CI (on this branch)

```sh
python research/prefab-donors/test_houses_qa.py
python research/prefab-donors/stage_all_houses_qa.py --verify-only > /tmp/tmm-houses-qa-verification.json
```

**DISPOSABLE-ONLY placement prep, after explicit owner authorization for the eventual gameplay test:**

```sh
mkdir -p /tmp/tmm-housing-disposable-instance
touch /tmp/tmm-housing-disposable-instance/.tmm-disposable-qa
python research/prefab-donors/stage_all_houses_qa.py --stage-qa-game-dir /tmp/tmm-housing-disposable-instance --target singleplayer
```

The example only copies the generated source files. To run in Minecraft, the integration writer must choose a fresh disposable **actual** Prism/server game directory and stage its linked mod JARs separately. Do not use a persistent world or production server directory; the marker alone does not prove the directory is disposable. Staging NBTs into `prefab-extension` makes them available for the native addon UI and can expose them as **ordinary paid/material-supplied** constructions; it is **not a personal one-time free grant**.

## Integration gates — do not shortcut

1. **Provider pin:** `mods/prefab-custom-addon.pw.toml` is staged in [roster draft #403](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/403), official Modrinth project `i4msNeGb` / version `oqFiI80X` / 2.7.1, SHA512 corroborated by `qacow37/prismnix` generated project index, **NOT downloaded-byte verified**; `mods/ldlib2.pw.toml` source candidate also staged. Integration writer verifies actual JAR hashes/native dependencies, then updates shared Packwiz `index.toml`, `pack.toml` and manifests atomically. Avoid confusing CurseForge 9284793 with the selected Modrinth file.
2. **Inn board:** [draft #381](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/381) is a child of housing research and adds a **Bountiful board with three decrees**. This source branch currently builds the **unpatched base Inn**; housing writer must merge/reconcile #381 before the **final** Inn artifact is selected, then re-run the 18-home staging check. Do not claim #381 included.
3. **Selection and free reward:** the **Progression engine writer** must supply the one-choice-per-player server-authoritative 19-option selector, entitlement consumption/rollback on failed placement/relog/team changes and route from FTB Quest graduation. This repository source does not implement that engine. Stock Generic requires its owner-approved loot/mineshaft/20-coal suppression. All other prefab custom additions are QA-only pending material/EMC, claims, native blockstate, fluid/sky/multiblock, air clearing and runtime checks.
4. **Security and economy audit:** upstream Custom Addon exposes an OP-only global `/prefabaddon freemode` bypass (server default off), custom Building Terminal, local-file upload and blueprint/crafted-resource paths. Confirm the **exact selected JAR**, server-authoritative material debits, upload permissions (deny non-OP by default if possible), no free-build outside granted-one-time selection, and no unbounded bulldozer/player claim bypass. Old `PlayerPreferences.consumeMaterials` field and commentary diverge in the inspected public source; do **not** infer survival economy safety from its GUI display. Only actual server gameplay test can certify it.
5. **Source provenance/licensing:** preserve Thun's MIT attribution for Hearthkeeper and Mimicry MIT for Arcane Forge, plus source/asset notices for donor-derived builds. House outputs are locally generated; **no third-party binaries, world saves, generated NBT or Prism package were committed** by this PR.
6. **Deployment state:** this is source validation and optional **manual QA preview** staging only, not final packaging or a gameplay PASS. The integration writer must add it to the comprehensive inclusion matrix as **SOURCE READY / RUNTIME BLOCKED** until the matching exact SHA-pinned providers, regenerated NBT and personal selector are actually included.

**No full NeoForge server boot, Prism package, client/world test or Integration Assembly was run or authorized by this PR.**
