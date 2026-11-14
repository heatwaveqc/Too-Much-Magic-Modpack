# Gameplay and Tectonic: full server integration

**Dedicated-server smoke: PASS**

- Run: https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43549822627
- Previous missing-dependency error: Iron's Lib and playerAnimator.
- Added Iron's Lib as a stable release and playerAnimator as an explicitly documented beta-tagged **required library**.
- All main gameplay mods were selected from release channels; beta Astral Sorcery, Botania and Draconic Evolution were omitted.
- Relics 1.21.1 builds remain beta; Construction Gadgets is not a verified exact project.
- 62 mods were pinned before library fix; plus two required libraries.
- Roots Classic, ProjectE and Replication were pinned from their official stable CurseForge builds.
- ProjectE and Project Expansion are **test-only until Chaos Guardian progression is actually enforced**.
- Graphical client boot and fresh-world gameplay: **NOT TESTED**.

## Status of actual game boot
NeoForge reached the Done readiness message and shut down cleanly.

## Explicit release-type exception
PlayerAnimator is beta-tagged on NeoForge 1.21.1. This is an unavoidable runtime dependency of Iron's Spells 3.16.3; see B3-DEPENDENCY-EXCEPTIONS.json.
