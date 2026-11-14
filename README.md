# Too Much Magic — Minecraft 1.21.1 NeoForge

A magic-first Minecraft modpack under active construction.

**Status: 125-mod client-test candidate, dedicated-server boot PASS.** `build/b6-food-pufferfish-skills-client-handoff` includes Pufferfish's Skills and Attributes, Improvable Skills 3 + HammerLib, all four Pam's HarvestCraft 2 modules, Farmer's Delight, Aquaculture 2, Harvest with Ease and Cobweb. It also **removes Aspect Alchemy** per the updated user decision. All **118 server-side JARs** were downloaded, hash-verified and loaded; NeoForge 21.1.265 reached `Done` and exited cleanly in [run 43554705678](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43554705678). **Seven client-only mods and all graphical gameplay are still untested.** ProjectE/Expansion gating is not implemented.

Read [AGENTS.md](AGENTS.md), [the design contract](docs/DESIGN.md), [the compatibility register](docs/COMPATIBILITY.md), and [the smoke-test ledger](docs/TESTING.md) before modifying progression or activating a mod.

Target: Minecraft **1.21.1**, NeoForge **21.1.265** (provisional baseline drawn from ATM10 8.5), Java **21**.

[Read B3 test results](docs/B3-TESTING.md), [installed manifest](manifest/mods.json), and [exact B3 release resolution](docs/B3-RESOLUTION.json). Gameplay builds are stable/release-channel **except** required playerAnimator 2.0.4, a beta-tagged library for Iron's Spells. Prior B3 exclusions are superseded by B4's explicit beta-testing approval. B4 includes Astral Sorcery **alpha**, Draconic Evolution **beta**, and **Botania Revived (stable unofficial port)**. Relics beta, the unavailable original Bigger Reactors, and unidentified mods remain excluded.

**B4:** [Test results](docs/B4-TESTING.md) · [exact CurseForge file IDs and results](docs/B4-RESOLUTION.json) · [manifest](manifest/mods.json). The newer reactor fork is *BiggerReactors Rework 0.1.6*, not Revived. It has battery and processing systems that require later balance review. Do not run this as a balanced progression pack until the ProjectE Chaos Guardian lock and other protections are implemented.

**B5 expanded roster:** [Full integration report](docs/B5-TESTING.md) · [Provider IDs, pinned versions and dependency fixes](docs/B5-RESOLUTION.json) · [Approved/rejected survey decisions](docs/MAGIC-MOD-SURVEY-DECISIONS.md). New systems include Goety, Malum/Gaze, Eidolon, Hexerei, Primal Magick, Forbidden & Arcanus, Psi, HexCast, Totemic, Embers, seven Ars addons, Vampirism/Werewolves, Feywild, Pastel, Scriptor Magicae, Aspect Alchemy, Summoning Rituals, Extra Botany Revived, and other approved utility add-ons.

**Download for Prism Launcher:** [Too Much Magic B6 import archive](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43555071188/artifacts/14097951209). The artifact ZIP contains a smaller `Too-Much-Magic-B6-Prism-Client-Test.zip`: extract the inner ZIP, then import that into Prism using Add Instance → Import. This package uses the MIT-licensed packwiz bootstrap, downloads the mods from upstream on launch, and points to the exact passing [commit ecddc4342](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/tree/ecddc4342c439a2b973ad16acff112b7d0a4bb60). [Full local-test steps](docs/B6-CLIENT-HANDOFF.md). No third-party mod JARs are bundled.
