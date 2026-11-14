# Too Much Magic — Minecraft 1.21.1 NeoForge

A magic-first Minecraft modpack under active construction.

**Status: 71-mod experimental candidate, dedicated-server boot PASS (4 staged tests).** Branch `build/b4-reactors-experimental` starts from the previously verified 64-mod pack, and adds BiggerReactors Rework, Astral Sorcery + ObserverLib, Botania Revived, Draconic Evolution + Brandon's Core and CodeChicken Lib. The clean-server smoke tests for **reactor, Astral, Botania and Draconic all passed**, with SHA-verified JARs and clean shutdowns in [Actions run 43551344358](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43551344358). **Seven client-only mods remain untested in a graphical client**, and no actual gameplay feature tests have been conducted. ProjectE/Expansion remain ungated.

Read [AGENTS.md](AGENTS.md), [the design contract](docs/DESIGN.md), [the compatibility register](docs/COMPATIBILITY.md), and [the smoke-test ledger](docs/TESTING.md) before modifying progression or activating a mod.

Target: Minecraft **1.21.1**, NeoForge **21.1.265** (provisional baseline drawn from ATM10 8.5), Java **21**.

[Read B3 test results](docs/B3-TESTING.md), [installed manifest](manifest/mods.json), and [exact B3 release resolution](docs/B3-RESOLUTION.json). Gameplay builds are stable/release-channel **except** required playerAnimator 2.0.4, a beta-tagged library for Iron's Spells. Prior B3 exclusions are superseded by B4's explicit beta-testing approval. B4 includes Astral Sorcery **alpha**, Draconic Evolution **beta**, and **Botania Revived (stable unofficial port)**. Relics beta, the unavailable original Bigger Reactors, and unidentified mods remain excluded.

**B4:** [Test results](docs/B4-TESTING.md) · [exact CurseForge file IDs and results](docs/B4-RESOLUTION.json) · [manifest](manifest/mods.json). The newer reactor fork is *BiggerReactors Rework 0.1.6*, not Revived. It has battery and processing systems that require later balance review. Do not run this as a balanced progression pack until the ProjectE Chaos Guardian lock and other protections are implemented.
