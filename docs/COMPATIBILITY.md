# Release evidence and unresolved compatibility

Date surveyed: 2026-11-13. Source details for individual entries are in `manifest/mods.json`. **A published NeoForge 1.21.1 jar does not establish pairwise or runtime compatibility.**

## Known compatible release targets (publication verified only)
- Neo Vitae 1.21.1-1.1.51; Occultism 1.21.1-neoforge-1.227.0; Ars Nouveau 5.13.5; Iron's Spells 3.16.3.
- Theurgy 1.79.0; Astral Sorcery 2.0.3 **alpha**; Roots Classic 1.5.10; Mahou Tsukai 1.36.31; EvilCraft 1.2.100.
- Just Another Witchery Remake 0.5.11; Cataclysm 3.33; Extended Crafting 7.0.9; Re:Avaritia 1.4.4.
- Draconic Evolution 3.1.4.633 **beta**; Project Expansion 1.0.6; FTB Quests 2101.1.38; FTB Chunks 2101.1.24; KubeJS 2101.7.2-build.381.
- Ars Nouveau and other mods may have newer versions than a search provider's cached main-file display. Exact file metadata must be pinned by packwiz, not inferred from this list.

## Open blockers / audits
- **Bigger Reactors (original unavailable), Botania, ProjectE, Apotheosis, Artifacts, Relics, Gateways to Eternity, Deeper and Darker, Starlight/structure mods, magical satchel**: no exact compatible file or full dependency graph has been committed here. Not silently omitted from the decision roster; see manifest pending rows.
- Full transitive dependency graph remains **unverified for noninstalled magic systems**, particularly Neo Vitae, Astral Sorcery (Curios + ObserverLib reported), Ars Nouveau, Occultism (Modonomicon), Draconic Evolution (Brandon's Core), and rendering mods. Treat these names as dependency candidates, not validated installed IDs.
- Unresolved overlaps: Astral Sorcery sky/rendering with shaders/Embeddium; content/loot balance across Apotheosis/Relics/Cataclysm; acquisition leak paths for ProjectE and banned ring; world-generation interactions.
- No pairwise incompatibility has yet been **demonstrated** in a crash. Unknown is not incompatible.
- Potential substitute ideas require explicit approval: *dedicated magical satchel* for first-day carried storage; *different magical structure mod* if a requested dungeon mod lacks a 1.21.1 release. No automatic substitutions.

## Research sources
- https://www.curseforge.com/minecraft/mc-mods/neovitae
- https://www.curseforge.com/minecraft/mc-mods/occultism/files/all?page=1&pageSize=20&version=1.21.1
- https://www.curseforge.com/minecraft/mc-mods/ars-nouveau
- https://www.curseforge.com/minecraft/mc-mods/irons-spells-n-spellbooks
- https://www.curseforge.com/minecraft/mc-mods/theurgy/files/all
- https://www.curseforge.com/minecraft/mc-mods/astral-sorcery
- https://www.curseforge.com/minecraft/mc-mods/ftb-quests-forge/files/all?page=1&pageSize=20&version=1.21.1
- https://www.curseforge.com/minecraft/mc-mods/ftb-chunks-forge/files/all
- https://packwiz.infra.link/reference/pack-format/

## Resolved identity: Replication; unresolved reactor port
- **Replication** is Buuz135's [Replication](https://www.curseforge.com/minecraft/mc-mods/replication) `Replication-1.21.1-1.2.7.jar`, built for NeoForge 1.21.1 (release dated 2026-02-22). The `Deepslate Replica Ore` is a real resource. **Titanium** is a required runtime dependency; exact downloadable file and closure not yet pinned. Source: https://modrinth.com/mod/replication/version/1.21-1.2.7 and https://github.com/Buuz135/Replication/blob/1.21/build.gradle .
- **Original Bigger Reactors (RogueLogix)**: official CurseForge files terminate at Minecraft 1.20.1, with last listed 1.20.1 Forge upload 2025-01-14. No verified official 1.21.1 NeoForge release: https://www.curseforge.com/minecraft/mc-mods/biggerreactors/files/all .
- **Two UNAPPROVED alternatives** exist: [Bigger Reactors Rework](https://www.curseforge.com/minecraft/mc-mods/biggerreactors-rework) by vindocel, NeoForge 1.21.1 `0.1.6` (2026-11-05), adds notable altered reactor behaviors and even battery/energy features; and [Bigger Reactors Revived](https://www.curseforge.com/minecraft/mc-mods/biggerreactorsrevived) by VelronDevs, NeoForge 1.21.1, unofficial port. Neither is silently substituted; both need mechanical/economy review, particularly avoiding a new generalized FE transfer/storage system. Another alternative `Extreme Reactors` 1.21.1 exists but is a different gameplay system, not an equivalent drop-in.
- Release availability is not a runtime compatibility claim. Modded dedicated-server / client smoke required before accepting any of the above.

## Broad infrastructure milestone (2026-11-13)
- Dedicated server **PASS** (not a client test): https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43548230954 . 30 packwiz metafiles are pinned; 23 server JARs hash-verified and launched together on NeoForge 21.1.265 / Java 21. Remaining 7 are client-only.
- Official FTB projects resolved by **CurseForge project ID**, not Modrinth fuzzy-name search: Quests 289412 / file 9405148 / version 2101.1.38; Chunks 314906 / version 2101.1.24; Library 404465 / version 2101.1.39; Teams 404468 / version 2101.1.12; XMod Compat 889915 / version 21.1.14.
- Modrinth searches for `ftb-quests` and `ftb-chunks` **initially selected unrelated projects** (`FTB Quests Optimizer` and `FTB Chunks Aerospace`). Those were rejected before any official pin was committed; `manifest/mods.json` contains the correct projects only.
- Other pinned infrastructure includes Curios API, Corail Tombstone, Patchouli, Modonomicon, GeckoLib, Architectury, JEI/Jade, KubeJS, performance/client QoL (see machine register).
- **Remaining:** client graphical load, UI overlays and shaders, world-entry validation, actual quest/KubeJS operations, all core magic/advanced boss systems. No progression locks have been implemented.

## B3 results — stable magic / Tectonic (2026-11-13 local)
- **Full dedicated-server boot PASS:** [run 43549822627](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43549822627) and **stable-JEI regression PASS** [run 43550199289](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43550199289). NeoForge 21.1.265 / Minecraft 1.21.1 / Java 21; 64 exact packwiz mod pins, 57 server-side hash-verified JARs, reached `Done`, exited 0. Seven client-only mods **have not been GUI-tested**.
- Includes Tectonic 3.0.28, Occultism, Ars Nouveau, Iron's Spells, Theurgy, Neo Vitae, JAWR and JAWR Forbidden Magic, Mahou Tsukai, EvilCraft, Roots Classic, Cataclysm, Deeper & Darker, Gateways to Eternity, Apotheosis, Artifacts, Extended Crafting, Re:Avaritia, ProjectE, Project Expansion, and Replication/Titanium. The installed version sources are `manifest/mods.json` and `mods/*.pw.toml`; older release targets above are historic research, **not** the selected files.
- **Dependency gap found and fixed:** Iron's Spells requires Iron's Lib 1.21.1-2.3.0 and playerAnimator 2.0.4+1.21.1. PlayerAnimator is marked **beta** upstream; it is the **single explicit unavoidable beta library exception**, documented in `docs/B3-DEPENDENCY-EXCEPTIONS.json`. No main gameplay mods are beta-tagged. JEI previously beta-tagged in B2 was replaced by 1.21.1 NeoForge **stable** 19.64.0.455.
- Astral Sorcery alpha, Botania, Draconic Evolution beta, Relics (no stable compatible 1.21.1 NeoForge build), and original Bigger Reactors (no original 1.21.1 NeoForge build) remain excluded. No reactor fork substituted. Construction Gadgets, Formation Wand, the unnamed magical satchel, and unidentified dungeon mods remain pending exact project identity/build.
- **Roots Classic is present and successfully tested on server** via official CurseForge 1.5.10, despite the earlier Modrinth `roots-classic` lookup failure recorded in `docs/B3-RESOLUTION.json`. The same applies to ProjectE and Replication, sourced via official CurseForge.
- The server reaching `Done` means no startup dependency blocker remains for the **server-side set**, not that recipes, cross-mod power paths, loot balance, Tectonic generation, or the client have been functionally validated. **ProjectE and Expansion are presently ungated, not usable as a production survival release**; Chaos Guardian restrictions are still a future KubeJS/loot integration task.

## B4 experimental compatibility result (2026-11-13 local)
- **All four dedicated-server tests PASS:** [run 43551344358](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43551344358). The final pack has **71 pinned mods/dependencies**, including previously tested 64. Each stage started a fresh NeoForge 21.1.265 dedicated server and reached `Done` with a clean shutdown.
- **BiggerReactors Rework 0.1.6**: official CurseForge project 1760630/file 9448110, *not* the separate Revived port and not the original Bigger Reactors. The port has its own `biggerreactorsrework` registry namespace and expanded energy storage/industrial features. New-world testing only; balancing remains open.
- **Astral Sorcery 2.0.3 alpha**: project 241721/file 9281073; **ObserverLib 1.10.3 alpha**: project 316833/file 8726128. The 1.21.1 alpha and library both pass server boot. **Client-side astral sky rendering not tested**, particularly alongside Embeddium.
- **Botania Revived 2.0.2 release**: project 1725641/file 9577651. Unofficial NeoForge port, not original Vazkii Botania. Compatible *server startup* with Ars Nouveau and other installed systems. Sophisticated Storage/Backpacks/Core are not installed. Internal optional integrations may require later disable/configuration; final mechanics and gameplay decisions are not audited.
- **Draconic Evolution 3.1.4.633 beta**: project 223565/file 8797042. **Brandon's Core stable 3.2.1.309**: project 231382/file 7130990. **CodeChicken Lib stable 4.6.1.529**: project 242818/file 8491811, pulled as a dependency by packwiz. This full stack passes server startup. Energy transfer and Chaos Guardian combat remain untested.
- The remaining failures/unresolved items above are **historic B3 notes**, and B4's positive server tests supersede the earlier lack of runtime evidence for these specific newly added releases. **Client world creation and gameplay interactions remain untested.**
- Nonblocking server log defects: Project Expansion missing declared `accesstransformer.cfg` and optional Quark class mixin warnings; triage later.

## Pufferfish planning — not installed (2026-11-13)
- Pufferfish's Skills: 1.21.1 NeoForge candidate `puffish_skills-0.19.2-1.21-neoforge.jar`, upstream **beta-tagged**. Requires a later explicit beta-testing exception/approval; this is a planned roster entry only.
- Pufferfish's Attributes: 1.21.1 NeoForge release candidate `puffish_attributes-0.8.3-1.21-neoforge.jar`. The standalone attributes library is the intended companion to Skills versions >=0.12.0, not a substitute for Skills.
- **Neither** has a `mods/*.pw.toml`/index entry, and neither has been runtime tested in the B5 mod collection; custom perk trees are not yet authored. Verify dependency pair, file IDs, NeoForge version, client GUI, and server compatibility before enabling.
- Sources: https://modrinth.com/mod/skills ; https://modrinth.com/mod/attributes ; https://www.curseforge.com/minecraft/mc-mods/puffish-skills/files/all ; https://www.curseforge.com/minecraft/mc-mods/puffish-attributes/files/all .
