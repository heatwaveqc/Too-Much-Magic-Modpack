# Too Much Magic — Minecraft 1.21.1 NeoForge

A magic-first Minecraft modpack under active construction.

**Status: 114-mod expanded magic candidate, dedicated-server boot PASS.** The branch `build/b5-expanded-approved-roster` combines the previous working 71-mod B4 build with all 32 approved additions from [design PR #6](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/6) (31 new pins, plus Botania Revived already present), plus their required libraries. **107 server-side JARs** were downloaded and hash-verified; the full NeoForge server reached `Done` and shut down cleanly with exit code 0 in [run 43553444492](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43553444492). **Seven client-only mods still lack graphical testing**. ProjectE/Expansion gating remains unimplemented.

Read [AGENTS.md](AGENTS.md), [the design contract](docs/DESIGN.md), [the compatibility register](docs/COMPATIBILITY.md), and [the smoke-test ledger](docs/TESTING.md) before modifying progression or activating a mod.

Target: Minecraft **1.21.1**, NeoForge **21.1.265** (provisional baseline drawn from ATM10 8.5), Java **21**.

[Read B3 test results](docs/B3-TESTING.md), [installed manifest](manifest/mods.json), and [exact B3 release resolution](docs/B3-RESOLUTION.json). Gameplay builds are stable/release-channel **except** required playerAnimator 2.0.4, a beta-tagged library for Iron's Spells. Prior B3 exclusions are superseded by B4's explicit beta-testing approval. B4 includes Astral Sorcery **alpha**, Draconic Evolution **beta**, and **Botania Revived (stable unofficial port)**. Relics beta, the unavailable original Bigger Reactors, and unidentified mods remain excluded.

**B4:** [Test results](docs/B4-TESTING.md) · [exact CurseForge file IDs and results](docs/B4-RESOLUTION.json) · [manifest](manifest/mods.json). The newer reactor fork is *BiggerReactors Rework 0.1.6*, not Revived. It has battery and processing systems that require later balance review. Do not run this as a balanced progression pack until the ProjectE Chaos Guardian lock and other protections are implemented.

**B5 expanded roster:** [Full integration report](docs/B5-TESTING.md) · [Provider IDs, pinned versions and dependency fixes](docs/B5-RESOLUTION.json) · [Approved/rejected survey decisions](docs/MAGIC-MOD-SURVEY-DECISIONS.md). New systems include Goety, Malum/Gaze, Eidolon, Hexerei, Primal Magick, Forbidden & Arcanus, Psi, HexCast, Totemic, Embers, seven Ars addons, Vampirism/Werewolves, Feywild, Pastel, Scriptor Magicae, Aspect Alchemy, Summoning Rituals, Extra Botany Revived, and other approved utility add-ons.
