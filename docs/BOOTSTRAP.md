# Bootstrap instructions

The committed `pack.toml` and `index.toml` specify a **loader-only pack**, with two inert KubeJS script placeholders. There are **zero third-party mod downloads** in the packwiz index: this is deliberately not advertised as a booted magic pack.

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
The smoke uses a temporary directory, downloads NeoForge directly, installs it, accepts EULA only inside the disposable smoke sandbox and checks the Done line. This does **not** install any mod files.

## Add real mods
Resolve exact 1.21.1 NeoForge file versions and all required dependencies first. Then install via packwiz (e.g. `packwiz modrinth add <approved-slug>` or CurseForge equivalent), inspect the generated .pw.toml file and side, update the manifest's enabled flag to true, `packwiz refresh`, validate, and run server + client integration checks. Do not add a mod until its entire dependency closure has been verified. Exact project and version IDs are required for a reproducible lock.

## Client test record
A graphical client must be launched on a workstation and tested with a new disposable world. Record mod menu, crash-free world entry, and log output. CI loader-only success is insufficient.
