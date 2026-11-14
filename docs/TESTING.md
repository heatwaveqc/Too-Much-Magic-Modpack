# Test ledger

**2026-11-13 bootstrap environment**
- GitHub repository created by user and first README commit verified: **PASS**.
- Java 21 in local container (`openjdk 21.0.12.1`): **PASS**.
- Local container `packwiz` binary: **NOT INSTALLED**.
- Local container network DNS for Minecraft/Modrinth/GitHub: **BLOCKED** (`Could not resolve host`).
- Packwiz TOML hash chain, manifest exclusions and CLI read: **PASS** GitHub Actions run 43545677885, job 132296314018.
- NeoForge 21.1.265 installer download and clean, loader-only dedicated-server startup: **PASS** GitHub Actions run 43545677885, job 132296313700; reached `Done` with script exit 0. This is a clean loader only, **not a modded-server boot**.
- B1 actual modded dedicated-server startup: **PASS** GitHub Actions run 43546287284, job 132298268556 (four SHA-512 pinned mod JARs fetched and verified, NeoForge 21.1.265 reached `Done`, clean exit status 0). No larger magic mods installed.
- Client startup / menu / fresh world: **NOT RUN** (no graphics-capable launcher).
- KubeJS script registration and recipe event validation: **NOT TESTED**; KubeJS JAR present in the B1 successful modded server, but the scripts are deliberately inert placeholders.
- B1 dependency resolution and download: **PASS** for exactly KubeJS, Rhino, Better Advanced Tooltips, ModernFix; Modrinth artifacts SHA-512-verified in CI run 43546287284. Other entries not resolved.
- Meta/QoL **modded-server runtime compatibility demonstrated** for the 23 server-loaded JARs only; core magic and graphical client are untested.

## Required subsequent test matrix
1. **Static**: `python3 scripts/validate.py`; packwiz index and hashes; machine manifest uniqueness and restrictions.
2. **Loader baseline dedicated server**: fetch exact 21.1.265 NeoForge installer; install on Java 21 into disposable dir; accept EULA for CI only; launch `nogui`, capture exit code and `Done` log line, then terminate cleanly.
3. **Core mods server**: pin exact 1.21.1 NeoForge mod files+all required libraries. Download via supported packwiz installer, start server, assert no missing dependencies, registry errors or data pack issues. Save logs.
4. **Client**: on suitable workstation import packwiz pack, launch main menu, create disposable world, check mod screens, shader/sky render, KubeJS events and absence of crashes.
5. **Gameplay integration**: validate each gate in survival and via negative bypass tests; confirm future renewable routes; ensure banned items have no acquisition paths.

Never reclassify step 1 as steps 2–5.

## B1 integration result — 2026-11-13 (local America/Phoenix date)
- Run: https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43546287284
- Metadata job: **PASS**, offline SHA-256 chain and actual packwiz CLI.
- Modded-server job: **PASS**; downloaded `better-advanced-tooltips-2101.1.0-build.5.jar`, `kubejs-neoforge-2101.7.2-build.381.jar`, `modernfix-neoforge-5.27.27+mc1.21.1.jar`, `rhino-2101.2.7-build.85.jar`; verified SHA-512 each, reached dedicated server `Done`, shut down with status 0.
- Client GUI/menu: **NOT RUN**. KubeJS actual recipe execution: **NOT TESTED**. Multiplayer connectivity and custom quest APIs: **NOT RUN**.
- Baseline and modded stages are distinct; nothing in this run proves future Occultism/Ars/Iron's/Neo Vitae compatibility.

## B2 metadata/QoL integration result — 2026-11-13
- Workflow: https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43548230954
- Automated resolution: **PASS**, 30 exact packwiz artifact pins, 0 requested slug failures. FTB pulled from exact official CurseForge IDs. Fuzzy-name false positives detected and rejected.
- Hash verification and dedicated-server startup: **PASS**, all 23 server-side JARs verified using source hashes (Modrinth and CurseForge), server reached `Done`, clean stop exit status 0.
- Seven client-only mods: **NOT CLIENT BOOT-TESTED**; no graphical session attempted.
- Quest content, KubeJS script events (other than empty placeholders), user joins and chunk claiming: **NOT TESTED**.
- Full resolved filenames, side and project IDs: [META-QOL-RESOLUTION.json](META-QOL-RESOLUTION.json).

## B3 stable gameplay + Tectonic (2026-11-13 local)
- Initial 55-server-JAR boot was **BLOCKED** by missing Iron's Lib and playerAnimator in [run 43549472015](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43549472015). Both were added with exact immutable artifact metadata.
- Repaired full **57-JAR dedicated-server** boot was **PASS** in [run 43549822627](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43549822627), reached `Done`, graceful stop exit 0.
- **Final release-JEI full-server regression PASS** in [run 43550199289](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43550199289): replaced beta-tagged JEI 19.70.0.469 with stable 19.64.0.455, all 57 JARs hash-verified, reached `Done`, exit 0. 64 total mods; seven client-only.
- Beta exception: playerAnimator 2.0.4, mandatory prerequisite of release-channel Iron's Spells; no beta gameplay mods. No Astral Sorcery, Botania, Draconic Evolution or beta Relics.
- **Client menu, new-world entry, Tectonic worldgen, spellcasting, scripts, recipes, quest UI, server connection: NOT TESTED**. ProjectE/Expansion Chaos Guardian gates **NOT IMPLEMENTED**; the current pack is a startup test build, not a balanced survival release.
- [Detailed B3 evidence](B3-TESTING.md), [dependency exception](B3-DEPENDENCY-EXCEPTIONS.json), [exact release metadata](B3-RESOLUTION.json).
