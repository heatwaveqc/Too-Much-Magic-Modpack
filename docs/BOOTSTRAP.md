# Bootstrap instructions

**Branch-specific:** `build/initial-neoforge-packwiz` is the original zero-mod loader-only baseline; `build/b1-kubejs-infrastructure` pins KubeJS, Rhino, Better Advanced Tooltips and ModernFix. `build/b2-meta-qol` pins 30 mods/dependencies and passed a dedicated-server boot with 23 hash-verified server JARs (run 43548230954). Seven client-only JARs have not undergone a graphical launch test. Two KubeJS script placeholders remain inert.

## Prerequisites
Java 21; packwiz for metadata updates; a functioning network; an appropriate Minecraft client launcher for client testing. CurseForge downloads may require a supported launcher or API auth. Do not redistribute mods as jars.

## Verify source integrity
```bash
python3 scripts/validate.py
packwiz list
# After future packwiz edits:
packwiz refresh
python3 scripts/validate.py
```

## Loader-only server smoke
```bash
SMOKE_TIMEOUT_SECONDS=120 bash scripts/smoke_server.sh
```
The smoke uses a temporary directory, downloads NeoForge directly, installs it, accepts EULA only inside the disposable smoke sandbox and checks the Done line. For pinned mods on either branch, run `SMOKE_WITH_MODS=1 SMOKE_TIMEOUT_SECONDS=180 bash scripts/smoke_server.sh`: this downloads and hash-verifies packwiz mod JARs before server startup.

## Add real mods
Resolve exact 1.21.1 NeoForge file versions and all required dependencies first. Then install via packwiz (e.g. `packwiz modrinth add <approved-slug>` or CurseForge equivalent), inspect the generated .pw.toml file and side, update the manifest's enabled flag to true, `packwiz refresh`, validate, and run server + client integration checks. Do not add a mod until its entire dependency closure has been verified. Exact project and version IDs are required for a reproducible lock.

## Client test record
A graphical client must be launched on a workstation and tested with a new disposable world. Record mod menu, crash-free world entry, and log output. CI loader-only success is insufficient.

## Broad meta/QoL stack
The reviewed snapshot is in [docs/META-QOL-RESOLUTION.json](META-QOL-RESOLUTION.json). It contains exact artifact filenames, side and official project IDs. FTB mods MUST be selected by official CurseForge project/file IDs; Modrinth search for "FTB Quests" and "FTB Chunks" can accidentally select unrelated add-ons.
`python3 scripts/resolve_meta_qol.py` is the automated resolver for a clean branch, and `bash scripts/smoke_server.sh` with `SMOKE_WITH_MODS=1` tests the server. Do not run resolution as an update operation without reviewing resulting changed pins.

Do not call the server test a graphical client test; Embeddium, ImmediatelyFast, Controlling, Mouse Tweaks, Entity Culling, Just Zoom and Searchables are client-only in this snapshot.

## B3 full magic/Tectonic release candidate
On `build/b3-stable-magic-tectonic`, **64 packwiz mod pins** exist: 57 downloadable server-side JARs and 7 client-only JARs. The modded server successfully reached `Done` and exited cleanly in [CI run 43550199289](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43550199289). Use `SMOKE_WITH_MODS=1 SMOKE_TIMEOUT_SECONDS=420 bash scripts/smoke_server.sh` to reproduce from a disposable server directory. Root/JVM config and world saves are never committed.

For a client: use a graphics-capable Java 21 launcher supporting packwiz, Minecraft 1.21.1 and NeoForge 21.1.265, install/download the full pack, inspect main menu for errors and create a throwaway world; **not yet performed**. Check Tectonic world generation, FTB Quests, spell UI, sky/rendering performance and KubeJS logs. Do not treat dedicated-server `Done` as client world-entry validation.

ProjectE and Project Expansion are currently included for reproducible startup tests only, **not permitted as an ungated survival release** until the Chaos Guardian lock is enforced. The only beta-tagged active dependency is playerAnimator, required by stable Iron's Spells; see [the exception record](B3-DEPENDENCY-EXCEPTIONS.json). No other beta gameplay mods were installed.

## B4 experimental 71-mod candidate
`build/b4-reactors-experimental` contains exact packwiz metadata for 71 mods, including the newer BiggerReactors Rework, Astral Sorcery alpha, Botania Revived release and Draconic Evolution beta. [CI run 43551344358](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43551344358) executed **four successful** consecutive, disposable dedicated-server smoke tests, hash-verifying all required server-side JARs.

To repeat locally with Java 21 and a network connection, run:

```bash
python3 scripts/validate.py
SMOKE_WITH_MODS=1 SMOKE_TIMEOUT_SECONDS=420 bash scripts/smoke_server.sh
```

For the graphical test use a launcher capable of importing the packwiz index (including CurseForge IDs); test client launch, load a disposable new Tectonic world, Astral sky (with and without Embeddium), Botania Lexica + mana flow, Draconic Energy Links and Chaos Guardian effects. **Do not use any existing survival saves.** In B4 the reactor fork's world/registry namespace differs from the older Original/Revived variants.

ProjectE remains unlocked for this test build; before normal survival distribution enforce all intended chaos/progression/reproducibility restrictions. Keep mods and worlds external to Git.

## B5 full 114-mod expanded approved roster
Branch `build/b5-expanded-approved-roster` contains exact immutable packwiz pins for the 71 mods from B4 plus all approved additions from the survey in PR #6 and required libraries. The complete 107-JAR dedicated-server test passed in [run 43553444492](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43553444492). The first two attempts revealed missing Lodestone, Caelus, PAUCAL, and a too-old Ars Elemancy; all are repaired and documented in `docs/B5-TESTING.md`.

For repeatable standalone server smoke with Java 21:

```bash
python3 scripts/validate.py
SMOKE_WITH_MODS=1 SMOKE_TIMEOUT_SECONDS=480 bash scripts/smoke_server.sh
```

Keep actual downloaded mods in the launcher only; no JARs, saved worlds or EULAs committed to Git. The CLI/headless server cannot test the seven client-only mods and UI effects; use a separate new-world client test before considering B5 fully compatible. ProjectE/Expansion and the late tech progression remain **test-only** until explicitly gated.

## B6 client test download
Use the [Prism Launcher import artifact](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43555071188/artifacts/14097951209). GitHub downloads a wrapping artifact ZIP; extract it and select the inner `Too-Much-Magic-B6-Prism-Client-Test.zip` in Prism Launcher → Add Instance → Import. It contains only `mmc-pack.json`, `instance.cfg`, an MIT-licensed packwiz bootstrap JAR, and installation instructions. No Minecraft mod JARs are bundled. On first launch packwiz retrieves and hash-verifies the exact pinned client/both-side mods from their upstream hosts. It uses the immutable snapshot ecddc4342c439a2b973ad16acff112b7d0a4bb60; no future branch changes can silently alter this test.

Select Java 21 in Prism. The instance has an 8 GiB maximum heap set (adjust for your system). Test on a **new disposable world only** and report logs/screenshots for any crashes. The 125-mod server boot passed; no one has yet run this exact graphical client locally. [Detailed quick start](B6-CLIENT-HANDOFF.md).
