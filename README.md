# Too Much Magic — Minecraft 1.21.1 NeoForge

A magic-first Minecraft modpack under active construction.

**Status: bootstrap / not yet boot-tested.** This repository starts with an empty packwiz baseline, an approved-mod register, and repeatable validation scripts. A published NeoForge release for a mod is **not** evidence that the assembled pack launches.

Read [AGENTS.md](AGENTS.md), [the design contract](docs/DESIGN.md), [the compatibility register](docs/COMPATIBILITY.md), and [the smoke-test ledger](docs/TESTING.md) before modifying progression or activating a mod.

Target: Minecraft **1.21.1**, NeoForge **21.1.265** (provisional baseline drawn from ATM10 8.5), Java **21**.

## Next full build — planned skill-backend migration (decision 2026-11-14)

**Pufferfish's Skills (`puffish-skills`) is slated for removal in the next full integrated pack build**, not the currently tested/locked client snapshots. Replace the Pufferfish-based tree/XP/one-time progression wiring with **Tyz's Skills 6.4.2** (new purchasable ranks and Skill Points), preserve **Improvable Skills 3** as its distinct existing general perk engine, and preserve the native **Vampirism faction skill frameworks** (including the Monk faction) for faction-specific unlocks and permissions. This is an approved *build target*, not an assertion that replacement/removal has happened.

Before removing Pufferfish from the packwiz index and manifest, audit transitive mod dependencies; determine separately whether **Pufferfish's Attributes (`puffish-attributes`)** is still needed by anything. Migrate or remove Pufferfish category/tree configuration, advancement/quest references and rewards; install and pin Tyz, and carry out clean dedicated-server and client regression testing. Retain immutable known-good B6/B8 snapshots for rollback. Do **not** silently remove the Attributes library just because Pufferfish Skills is leaving. See [Monk catalog migration draft](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/31).
