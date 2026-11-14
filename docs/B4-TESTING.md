# B4: BiggerReactors Rework + Astral Sorcery + Botania Revived + Draconic Evolution

Minecraft 1.21.1 / NeoForge 21.1.265 / Java 21. Experiments are tested in order
with a fresh disposable dedicated server for each stage. Every server JAR is
downloaded and hash-verified from packwiz metadata.

Pre-existing B3 stable baseline: 64 mods; 57 server JARs; full dedicated-server PASS.

| Stage | Exact mod(s) | Result |
|---|---|---|
| reactor | bigger-reactors-rework | **PASS** |

## Detailed results

### reactor: PASS
- CI run: https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43551344358
- Packwiz added: bigger-reactors-rework
- Observed boot: Clean server readiness and stop; JAR hashes verified
- Log: `[m[1;31m[03:24:04.236] [main/ERROR] [loading.moddiscovery.ModFile/LOADING]: Access transformer file accesstransformer.cfg provided by mod projectexpansion does not exist!`
- Log: `[m[33m[03:24:07.673] [main/WARN] [mixin/]: Error loading class: org/violetmoon/quark/content/tweaks/module/GoldToolsHaveFortuneModule (java.lang.ClassNotFoundException: org.violetmoon.quark.content.tweaks.module.GoldToolsHaveFortuneModule)`
- Log: `[m[33m[03:24:07.682] [main/WARN] [mixin/]: Error loading class: org/violetmoon/quark/content/tweaks/module/GoldToolsHaveFortuneModule (java.lang.ClassNotFoundException: org.violetmoon.quark.content.tweaks.module.GoldToolsHaveFortuneModule)`

## Constraints and untested items
- **Reactor selection:** BiggerReactors Rework by vindocel, not Bigger Reactors Revived. Different registry namespace `biggerreactorsrework` and not backward-compatible with original worlds.
- **Botania selection:** unofficial Botania Revived port, not original Vazkii Botania; no Sophisticated Storage/Backpacks/Core dependency allowed. Revived's optional Sophisticated integration is not proof those packages are installed.
- **Astral Sorcery** is officially alpha-classified for NeoForge 1.21.1. ObserverLib is likewise alpha.
- **Draconic Evolution** is beta-classified; Brandon's Core is stable.
- **KubeJS progression restrictions remain unimplemented.** ProjectE and Project Expansion remain installed for testing, not production-ready survival.
- **No graphical client or fresh-player-world test was performed.** Dedicated-server readiness proves only server boot.
- Failed stages are rolled back from the working pack; their failed attempts remain in this report and CI artifacts.
