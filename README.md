# Too Much Magic — Minecraft 1.21.1 NeoForge

A magic-first Minecraft modpack under active construction.

**Status: metadata/QoL stack passed a dedicated-server smoke test.** On `build/b2-meta-qol`, 30 NeoForge-compatible mods and libraries are pinned in packwiz (23 server-side + 7 client-only). GitHub Actions run [43548230954](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43548230954) downloaded every server-side JAR, verified its hash, and started and cleanly stopped NeoForge 21.1.265. Graphical client boot and major magic systems are still pending.

Read [AGENTS.md](AGENTS.md), [the design contract](docs/DESIGN.md), [the compatibility register](docs/COMPATIBILITY.md), and [the smoke-test ledger](docs/TESTING.md) before modifying progression or activating a mod.

Target: Minecraft **1.21.1**, NeoForge **21.1.265** (provisional baseline drawn from ATM10 8.5), Java **21**.
