# Approved Lootr multiplayer parity — integration candidate (2026-11-22)

**Status:** Source-staged candidate only; **NOT** in live Packwiz index, assembled pack, smoke-tested server, or released Prism ZIP. Owner approved this inclusion in live discussion. Tracking: [#542](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/542).

The repository's single integration writer must independently verify and reconcile these source files into the latest `test/full-integration` mods, index, config and manifest. This branch intentionally does **not** modify shared `index.toml`, `manifest/mods.json`, or the live `mods/` / `config/` trees. Adding a pin file to this directory is not the same as installing a mod. Preserve #390 scope-freeze exception accounting: these owner-approved additions support already-present Lootr/dungeon exploration, with the new scope explicitly tracked in #542.

## Included candidate source files

| Target | Artifact and provenance | Next action |
| --- | --- | --- |
| Multiplayer Spawners | **2.0.2** Minecraft 1.21.1 NeoForge, official [GitHub release](https://github.com/The-Plum-Team/Multiplayer-Spawners-Mod/releases/tag/v2.0.2). Official release asset lists SHA-256 `3118ab7710c29a15c1a732e80d6724d678e43dd11bb0a6c4ad23d9af4e2fda67`. | Reconcile `pins/multiplayer-spawners.pw.toml`; verify loader and binary; config below |
| Lootr Liaison | **1.3.0+1.21.1-neoforge**, CurseForge [file 9028796](https://www.curseforge.com/minecraft/mc-mods/lootr-liaison/files/9028796), project 1445934. Candidate SHA-256 from [external mod inventory](https://github.com/CtrlAltDyleted/Antarchy-Ascension/blob/main/MOD_MANIFEST.csv); **not verified against publisher binary**. | Verify hash against exact upstream download before promoting `pins/lootr-liaison.pw.toml` |
| Twilight Lootr | **0.0.0.1** Minecraft 1.21.1 NeoForge, CurseForge [file 8327180](https://www.curseforge.com/minecraft/mc-mods/twilight-lootr/files/8327180), project 1589409. Candidate SHA-1 from [third-party Packwiz pin](https://github.com/Raisierer/Cobble-Industries/blob/main/mods/twilight-lootr.pw.toml); **not yet publisher-binary verified**. | Check exact dependency ranges and hash before promoting `pins/twilight-lootr.pw.toml` |
| Lootr Compatibility: BetterEnd | **2.0**, CurseForge [file 6731744](https://www.curseforge.com/minecraft/data-packs/lootr-compat-betterend/files/6731744), project 1154866. No independently verified ZIP hash. | **Not staged as installed**. Download/inspect ZIP, datapack format and blocks against BetterEnd: New Dawn 21.0.39; only then add correct datapack location and Packwiz hash |

These entries target the existing **Lootr 1.11.38.127**, **Twilight Forest 4.8.3345**, **BetterEnd: New Dawn 21.0.39**, and **Apothic Spawners 1.4.0** currently pinned on the integration branch. Avoid reintroducing incompatible versions or duplicate native Lootr break-to-loot mods.

## Proposed Multiplayer Spawners configuration

`defaultconfigs/multiplayerspawners-server.toml` is an **exact-key proposed server template**, sourced from [upstream 2.0.2 config](https://github.com/The-Plum-Team/Multiplayer-Spawners-Mod/blob/v2.0.2/common/src/main/java/com/theplumteam/multiplayerspawners/config/ModConfig.java) and intentionally not written directly to shared `defaultconfigs/`.

- `enableGroupDisabling = false`: the upstream default is true, sharing player progress within 20 blocks. TMM requires genuinely personal spawner completions.
- `treatPlacedSpawnersAsVanilla = true`: player-made spawner farms retain expected behavior.
- `allowSpawnerBreakMessage = false`: no normal universal removal of naturally generated spawners after personal completion. Audit special spawner-breaking tools and Apothic Spawners interactions before deciding whether to permit narrowly tagged exceptions.
- `torchExtinguishDelaySeconds = 10`: **explicit TMM target**; upstream 2.0.2 code default is **600 seconds**, so do not assume a 10-second default.
- `spawnerRespawnMinutes = 0`: personal completion persists unless explicitly changed.

The [upstream TorchTracker](https://github.com/The-Plum-Team/Multiplayer-Spawners-Mod/blob/v2.0.2/common/src/main/java/com/theplumteam/multiplayerspawners/util/TorchTracker.java) checks only `Blocks.TORCH` / `Blocks.WALL_TORCH`, i.e. ordinary vanilla placed/wall torches. Ars Nouveau, Neo Vitae, Astral Sorcery and other modded light blocks are not extinguished by this code. **A bright magical light may still suppress normal vanilla spawns** near a spawner; do not confuse that with the light being removed.

## Explicit exclusions / holds

- **DO NOT install Lootr: Fair Share.** Full personal loot in every Lootr chest is required, independent of active player count.
- No redundant Lootr: Break to Loot addon (the currently pinned Lootr implements native break-to-loot).
- No Torchlit Spawners (competes for spawner behavior).
- **Loot Integrations** remains *deferred*: investigate added structure loot versus existing LootJS scripts and progression gates first. No automatic adoption.
- BetterEnd datapack *blocked for technical verification*, not owner-deferred. If it is incompatible, document the exact conflict and identify viable compatibility approach.

## A: offline / source checks (not yet run)

1. Resolve metadata and hashes for all candidate downloads (especially third-party checksums), provider file IDs and dependency versions.
2. Validate TOML syntax and `index.toml` hashes only on the reconciled candidate; verify Packwiz / Prism resolvability under exact version.
3. Inspect BetterEnd datapack ZIP and ID mapping; check Lootr Liaison with terrain conversion.
4. Run Repository CI + Integration Preflight on actual integrated SHA; no automatic source assembly or full-game QA.

## B/C: in-game acceptance (awaiting next owner-authorized build)

- Two players: player A disables dungeon spawner; player B stays able to activate it even when within 20 blocks; world save/relog/chunk reload persist personal state.
- Two players: both get **100% normal individual Lootr chest contents** without shared depletion or Fair Share reduction.
- Natural/placed spawners and Apothic Spawners interact safely; ensure no accidental duplicate boss rewards, player-specific achievements or unfair XP.
- Placed vanilla floor/wall torch within seven blocks extinguishes after configured delay; magical light sources survive intact; verify effect of residual light on actual mob spawning.
- Verify Liaison in Tectonic-generated structures, Artifacts mimic handling, BetterEnd New Dawn containers, and Twilight Forest boss per-player loot including participation restrictions.
- Dedicated-server startup, client launch, fresh chunk generation, unload/reload and performance check in dense structures.

**No Integration Assembly, Server Boot Smoke, or Prism QA Package authorization is implied by the source stage or owner approval of additions.**
