# Release evidence and unresolved compatibility

Date surveyed: 2026-11-13. Source details for individual entries are in `manifest/mods.json`. **A published NeoForge 1.21.1 jar does not establish pairwise or runtime compatibility.**

## Known compatible release targets (publication verified only)
- Neo Vitae 1.21.1-1.1.51; Occultism 1.21.1-neoforge-1.227.0; Ars Nouveau 5.13.5; Iron's Spells 3.16.3.
- Theurgy 1.79.0; Astral Sorcery 2.0.3 **alpha**; Roots Classic 1.5.10; Mahou Tsukai 1.36.31; EvilCraft 1.2.100.
- Just Another Witchery Remake 0.5.11; Cataclysm 3.33; Extended Crafting 7.0.9; Re:Avaritia 1.4.4.
- Draconic Evolution 3.1.4.633 **beta**; Project Expansion 1.0.6; FTB Quests 2101.1.38; FTB Chunks 2101.1.24; KubeJS 2101.7.2-build.377.
- Ars Nouveau and other mods may have newer versions than a search provider's cached main-file display. Exact file metadata must be pinned by packwiz, not inferred from this list.

## Open blockers / audits
- **Bigger Reactors, Botania, ProjectE, Replication, Apotheosis, Artifacts, Relics, Gateways to Eternity, Deeper and Darker, Starlight/structure mods, magical satchel**: no exact compatible file or full dependency graph has been committed here. Not silently omitted from the decision roster; see manifest pending rows.
- Full transitive dependency graph is **not verified**, particularly Neo Vitae, Astral Sorcery (Curios + ObserverLib reported), KubeJS (Rhino), FTB Quests (FTB Library, FTB Teams and possibly Item Filters), Ars Nouveau, Occultism (Modonomicon), Draconic Evolution (Brandon's Core), and rendering mods. Treat these names as dependency candidates, not validated installed IDs.
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
