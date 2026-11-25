# Cathobb 64 v40 + Aquamirae & Nouveau — publisher-only integration and verification

**Date:** 2026-11-22. **Pull request:** [#528](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/528) into `test/full-integration`.

## Source-ready results

### Aquamirae & Nouveau 0.1.3 (one new game mod)

User provided the exact 110,796-byte `aquamirae-nouveau-0.1.3-mc1.21.1-neoforge.jar`. Inspection of this local JAR confirmed:
- ZIP CRC PASS, **SHA1 `932c37b798a04d13ca231d141d86590153e99fa3`**, SHA512 `a6b100852be8b9419e3f9eb04a516b4828779062fa5961d8d6a68ca6fbdc29ba18e276c04a0d39adb827876ad037a6483eea851f869f8e199bc73e537e2fec9b`.
- `META-INF/neoforge.mods.toml` declares `aquamirae_nouveau` version 0.1.3 (MIT), native NeoForge `javafml`; Minecraft `[1.21.1]`, NeoForge `[21.1.250,21.2)`, Ars Nouveau `[5.13.1,6)`, Ars Conflux `[0.1.47,0.2)`, Aquamirae `[7.2.11,7.3)`. Current selected versions MC 1.21.1, NeoForge 21.1.265, Ars Nouveau 5.13.5, Ars Conflux 0.1.56 and Aquamirae 7.2.13 satisfy these exact native ranges.
- No nested JARs. Added `mods/aquamirae-nouveau.pw.toml` referencing official [CurseForge file 9470029, project 1767657](https://www.curseforge.com/minecraft/mc-mods/aquamirae-nouveau/files/9470029), **metadata:curseforge** and owner-supplied digest. **No owner JAR is committed or redistributed.**
- **Critical follow-up**: the isolated GitHub Actions [Packwiz bootstrap download test run 45074889623](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/45074889623) successfully fetched the official 110,796-byte JAR through CurseForge metadata and checked a byte-for-byte SHA1 match to the uploaded file. Earlier direct `mediafilez` 403 was not an actual Packwiz download blocker.

### Cathobb Unified 64 v40 (one new client resource pack, not a mod)

- Official [public CurseForge project 1771113 / file 9411491](https://www.curseforge.com/minecraft/texture-packs/cathobbs-unified/files/9411491). Uploaded by cathobb November 2, 2026; compatible Minecraft 1.21.1. **All Rights Reserved** — **do not redistribute the ZIP** or copy any of its textures into this repository.
- Filename `cathobbs-Unified-v40-64x-CANDIDATE.zip`; **official publisher ZIP exact bytes 193,200,750**, **SHA1 `6195e347306d2567a74d3e49542eec36c33b9e51`**, SHA512 `cb4f4f39598969e83897bbdc066c4ad76ac92237f37a203ffcad20410ea4db9cef56d564aca2b98e316c1639cfcfb7732d89d7575b597b3395b59c92a0b8cea6`.
- [Official ZIP audit run 45074548445](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/45074548445) confirmed valid ZIP CRC, root `pack.mcmeta` and `assets/`, 19,218 entries; no file retained after ephemeral CI probe.
- Added client-only `resourcepacks/cathobb-unified-64.pw.toml` referencing the actual **ForgeCDN publisher URL** with SHA1 and exact CurseForge IDs. In the [isolated Packwiz bootstrap download run 45074889623](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/45074889623), an exact-packwiz-manifest one-resource fixture successfully fetched this 193MB publisher ZIP, checked its SHA1, installed it under `resourcepacks/`, and **skipped** installing it for the `server` side. **No third-party binaries are in Git/CI artifact/Prism archive.**
- `scripts/prism_qa_options.txt` seeds `resourcePacks:["vanilla","file/cathobbs-Unified-v40-64x-CANDIDATE.zip"]` for **newly created** Prism QA instances. Existing instance options are not re-imported and personal sync `scripts/tmm_qa_preferences.ps1` does not mutate the resourcePacks setting; user choices remain theirs. No shaders, PBR dependency, optifine, paid variants or optional alternate base pack enabled.
- `scripts/validate.py` validates client-only Packwiz ZIP metadata without counting it as a mod JAR; `scripts/verify_qa_inventory.py` deliberately excludes resourcepacks from native JAR count; `scripts/assemble_integration.py` now retains `resourcepacks/*.pw.toml` on **future authorized** integration rebuilds; `manifest/resourcepacks.json` records the publisher/license/hash and UI status.
- Packwiz source pinning and isolated installation tests **do not establish actual Prism/Windows UI compatibility, Minecraft 1.21.1 resource pack acceptance, game client launch or frame rate**. An owner-authorized next QA instance is required to test these observations.

## QA inventory

**A / offline-source verification completed:** exact owner-uploaded Aqua JAR/required dependency ranges, official Cathobb ZIP content+SHA, two isolated Packwiz client downloads, Cathobb server-side exclusion, target/index hash matching, draft QA option seed checks and repository CI.

**B/C pending authorized client/gameplay:** 
- [ ] Fresh Prism QA instance performs the pre-launch Packwiz download of both files; `resourcepacks/cathobbs-Unified-v40-64x-CANDIDATE.zip` is present and auto-enabled in Minecraft resource-pack screen; no missing atlas/client crash or excessive memory spike.
- [ ] Existing Prism user-controlled `options.txt` retains resource-pack order/toggles across a source update, including users who opted out of Cathobb.
- [ ] Aqua Nouveau spells/rituals/adapters actually register with Aquamirae/Ars Nouveau/Ars Conflux and behave correctly; validate claims, deaths/boss drops, EMC/MXP and persistence, with fresh disposable world.
- [ ] Confirm standalone `resourcepacks/` metadata survives the authorized comprehensive source assembly/index regeneration and is not reclassified as a third-party mod/JAR.
- [ ] Artistic quality/performance of the 64x public *candidate* varies by covered mod; record exclusions and visual defects separately, do not silently add paid assets or shader prerequisites.

**Other companion:** `Aquamirae Delight` 3.0 was found on Modrinth as a Farmer's Delight–Aquamirae addon, but **its publicly indexed builds support 1.20.1 Forge, not 1.21.1 NeoForge**. Keep it as a research/port candidate; do not install an incompatible binary just to gain recipes.

**Authorization:** No integration Assembly, full server smoke, full Prism ZIP package or actual client/gameplay test has been performed or implied by this source PR. Exact-CF-download probes are isolated, client-installer tests, not full modpack boots.
