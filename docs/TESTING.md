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

## B4 reactor and experimental magic candidate — 2026-11-13 local
- [Staged CI run 43551344358](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43551344358): **PASS all four stages**, each from a disposable new dedicated server. Sequence: Reactor Rework 0.1.6; Astral 2.0.3 alpha + ObserverLib 1.10.3 alpha; Botania Revived 2.0.2 release; Draconic Evolution 3.1.4.633 beta + Brandon's Core 3.2.1.309 + auto-required CodeChicken Lib 4.6.1.529.
- 71 distinct packwiz mod pins in final working configuration. Hash verification, NeoForge 21.1.265 `Done` ready line and graceful exit succeeded for each stage.
- Nonblocking recurring log messages: Project Expansion claims missing `accesstransformer.cfg`; a mixin referring to an absent Quark GoldToolsHaveFortuneModule emits warnings. Neither prevented server readiness, but both warrant investigation before a production release.
- Graphical client / shaders / Astral sky effects / player-world entry / Botania mechanics / reactor energy balances / Draconic Chaos Guardian **NOT TESTED**. Endgame gates remain unimplemented; the pack is not production-ready for survival.
- [Detailed stage evidence](B4-TESTING.md) and [pinned artifacts](B4-RESOLUTION.json).

## B5 expanded user-approved magic roster (2026-11-13 local)
- 32 user-approved survey selections reconciled with working B4 branch; **31 new approved systems** plus **12 required libraries** were added (Botania Revived was already installed): final total **114 mod pins**.
- [Attempt 1, run 43552770652](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43552770652): all 102 selected server JARs hash-verified, startup blocked by missing **Lodestone** required by Malum. Added Lodestone 1.8.2 plus Ars Elemental and Gaze from their verified upstream projects.
- [Attempt 2, run 43553107811](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43553107811): all 105 server JARs hash-verified; loader reported missing **Caelus API** and **PAUCAL** for HexCast/Enigmatic Legacy+ and incompatible **Ars Elemancy 1.17** for Ars 'n Spells 3.3.5.
- [Attempt 3, run 43553444492](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43553444492): upgraded Ars Elemancy to 1.18.3 and installed Caelus 7.0.1 and PAUCAL 0.7.0. **PASS** 107/107 JAR content hashes, NeoForge 21.1.265 reached `Done`, clean shutdown exit 0.
- One new alpha-tagged required library **PAUCAL 0.7.0** and one beta-tagged optional-power addon **Psi: Tweaks and Additions** are boot-test candidates, not validated gameplay mechanics. Previously approved B4 alphas/betas remain.
- No hard-excluded or unapproved maturity-test systems installed. No graphical client, Tectonic worldgen gameplay, multiplayer connection, FTB Quests, KubeJS gameplay scripts, creative/survival balance or Chaos Guardian/ProjectE progression restrictions were verified.
- [B5 test ledger](B5-TESTING.md), [exact resolution report](B5-RESOLUTION.json), [survey approvals](MAGIC-MOD-SURVEY-DECISIONS.md).
