# Test ledger

**2026-11-13 bootstrap environment**
- GitHub repository created by user and first README commit verified: **PASS**.
- Java 21 in local container (`openjdk 21.0.12.1`): **PASS**.
- Local container `packwiz` binary: **NOT INSTALLED**.
- Local container network DNS for Minecraft/Modrinth/GitHub: **BLOCKED** (`Could not resolve host`).
- Packwiz TOML format correctness: **PENDING** local parser and CI.
- NeoForge installer download and dedicated server startup: **NOT RUN**.
- Modded server startup: **NOT RUN** (no mod files pinned).
- Client startup / menu / fresh world: **NOT RUN** (no graphics-capable launcher).
- KubeJS `startup_scripts` or recipe event registration: **NOT RUN** (no KubeJS JAR installed).
- Dependency download/resolve: **NOT RUN**.
- No modpack runtime compatibility has been demonstrated.

## Required subsequent test matrix
1. **Static**: `python3 scripts/validate.py`; packwiz index and hashes; machine manifest uniqueness and restrictions.
2. **Loader baseline dedicated server**: fetch exact 21.1.265 NeoForge installer; install on Java 21 into disposable dir; accept EULA for CI only; launch `nogui`, capture exit code and `Done` log line, then terminate cleanly.
3. **Core mods server**: pin exact 1.21.1 NeoForge mod files+all required libraries. Download via supported packwiz installer, start server, assert no missing dependencies, registry errors or data pack issues. Save logs.
4. **Client**: on suitable workstation import packwiz pack, launch main menu, create disposable world, check mod screens, shader/sky render, KubeJS events and absence of crashes.
5. **Gameplay integration**: validate each gate in survival and via negative bypass tests; confirm future renewable routes; ensure banned items have no acquisition paths.

Never reclassify step 1 as steps 2–5.
