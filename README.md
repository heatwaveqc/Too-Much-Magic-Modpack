# Too Much Magic — Minecraft 1.21.1 NeoForge

A magic-first Minecraft modpack under active construction.

**Status: 64-mod stable-gameplay candidate, dedicated-server boot PASS.** Branch `build/b3-stable-magic-tectonic` includes the previous 30 QoL/infrastructure mods plus Tectonic, the stable approved magic systems, exploration, bosses and their dependencies. **57 server JARs** downloaded and hash-verified, then NeoForge 21.1.265 reached `Done` and shut down cleanly in [regression run 43550199289](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43550199289). **Seven client-only JARs have not been tested in a graphical client.** ProjectE/Expansion gating is not implemented.

Read [AGENTS.md](AGENTS.md), [the design contract](docs/DESIGN.md), [the compatibility register](docs/COMPATIBILITY.md), and [the smoke-test ledger](docs/TESTING.md) before modifying progression or activating a mod.

Target: Minecraft **1.21.1**, NeoForge **21.1.265** (provisional baseline drawn from ATM10 8.5), Java **21**.

[Read B3 test results](docs/B3-TESTING.md), [installed manifest](manifest/mods.json), and [exact B3 release resolution](docs/B3-RESOLUTION.json). Gameplay builds are stable/release-channel **except** required playerAnimator 2.0.4, a beta-tagged library for Iron's Spells. Excluded as requested: Astral Sorcery, Botania, Draconic Evolution beta, Relics beta, unavailable original Bigger Reactors and unidentified mods.
