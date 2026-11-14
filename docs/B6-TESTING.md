# B6 — Pufferfish, food suite and Improvable Skills final boot

**Full dedicated-server result: PASS**

- CI: https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43554705678
- Minecraft 1.21.1 / NeoForge 21.1.265 / Java 21.
- 125 exact packwiz mod pins, 118 server-side, 7 client-only.
- 12 newly pinned mods (including any transitive dependencies).
- Aspect Alchemy removed following latest explicit exclusion.
- Pufferfish's Skills (beta), Pufferfish's Attributes (stable), Improvable Skills 3 and HammerLib selected.
- All four Pam's HarvestCraft 2 modules, Farmer's Delight, Aquaculture 2, Harvest with Ease and Cobweb selected.
- Combined startup tests do not test the Pufferfish custom skill tree; it has not been authored.
- Client GUI, world generation, food recipe tags/harvesting, graphical performance, and ProjectE Chaos Guardian progression gates are NOT TESTED.

## New exact mod pins
- aquaculture — `Aquaculture-1.21.1-2.7.22.jar`
- cobweb — `cobweb-neoforge-1.21-1.4.0.jar`
- farmers-delight — `FarmersDelight-1.21.1-1.3.4.jar`
- hammerlib — `HammerLib-1.21-21.0.17.jar`
- harvest-with-ease — `harvest-with-ease-neoforge-1.21-9.4.0.jar`
- improvable-skills — `ImprovableSkills-1.21-21.0.2.jar`
- pams-harvestcraft-2-crops — `pamhc2crops-NEOFORGE-1.21.1-1.0.9.jar`
- pams-harvestcraft-2-food-core — `pamhc2foodcore-NEOFORGE-1.21.1-1.0.4.jar`
- pams-harvestcraft-2-food-extended — `pamhc2foodextended-NEOFORGE-1.21.1-1.0.0.jar`
- pams-harvestcraft-2-trees — `pamhc2trees-NEOFORGE-1.21.1-1.0.9.jar`
- puffish-attributes — `puffish_attributes-0.8.3-1.21-neoforge.jar`
- puffish-skills — `puffish_skills-0.19.2-1.21-neoforge.jar`

## Installer handoff
Do not export a Modrinth or CurseForge pack ZIP that silently bundles copyrighted cross-provider mod jars.
Use the packwiz manifest and its standard installer, or a Prism instance that downloads from upstream providers.
Client instructions will be finalized after successful boot and launcher-package verification.
