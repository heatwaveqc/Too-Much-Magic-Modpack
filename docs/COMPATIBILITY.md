# Release evidence and unresolved compatibility

Date surveyed: 2026-11-13. Source details for individual entries are in `manifest/mods.json`. **A published NeoForge 1.21.1 jar does not establish pairwise or runtime compatibility.**

## Known compatible release targets (publication verified only)
- Neo Vitae 1.21.1-1.1.51; Occultism 1.21.1-neoforge-1.227.0; Ars Nouveau 5.13.5; Iron's Spells 3.16.3.
- Theurgy 1.79.0; Astral Sorcery 2.0.3 **alpha**; Roots Classic 1.5.10; Mahou Tsukai 1.36.31; EvilCraft 1.2.100.
- Just Another Witchery Remake 0.5.11; Cataclysm 3.33; Extended Crafting 7.0.9; Re:Avaritia 1.4.4.
- Draconic Evolution 3.1.4.633 **beta**; Project Expansion 1.0.6; FTB Quests 2101.1.38; FTB Chunks 2101.1.24; KubeJS 2101.7.2-build.381.
- Ars Nouveau and other mods may have newer versions than a search provider's cached main-file display. Exact file metadata must be pinned by packwiz, not inferred from this list.

## Open blockers / audits
- **Tectonic planned, not installed:** official [Tectonic 3.0.28 NeoForge 21.1](https://www.curseforge.com/minecraft/mc-mods/tectonic/files/8855043) is published for Minecraft 1.21.1 (CurseForge file **8855043**, project **686836**). **Lithostitched** is a required dependency; exact target artifact/dependency closure must be pinned before adding either to packwiz. Test world generation with structure mods, magic biomes/dimensions and restricted ore placements in disposable worlds. Terralith is deliberately excluded to avoid extensive biome proliferation.
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
