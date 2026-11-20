# Approved mod pins — November 20 reconciliation

**Scope:** Minecraft 1.21.1 / NeoForge 21.1.265, owner-frozen mod universe [#390](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/390). **Type A source-only candidate**, NOT an installed pack and NOT permission to assemble, boot, package, or launch.

## Reconciliation against integration

On inspection, `test/full-integration` has 220 active, indexed third-party mods, matching the November 19 `manifest/next-qa-target.json` target. That target predates the new approvals in [#387](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/387), [#386](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/386), and [#331](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/331).

**Eight newly approved third-party mods** not in the current active 220: Mowzie's Mobs, Weeping Angels, Aquamirae, Sons of Sins, Aura Cascade Reimagined, Arcane Archives: Reborn, The Graveyard, HerbalBrews. **Two new required libraries:** Fragmentum (Aquamirae) and VillagersTradingPlus (The Graveyard). These are new required inclusion *attempts*, not already-tested installations. Existing library pins satisfy GeckoLib 4.9.3, Patchouli 93, Architectury 13.0.11 and Modonomicon 1.123.0 at a preliminary dependency level; exact mod JAR ranges must be verified before inclusion.

## This PR: six exact provider candidates, unindexed

Each `mods/*.pw.toml` contains a release filename, CurseForge project+file ID and a published SHA-1. **Published SHA-1 values below are corroborated by secondary modpack catalogs, not independently computed from provider JAR bytes in this chat.** The designated integration writer must independently download from the authorized publisher endpoint, verify bytes, inspect native NeoForge metadata, confirm loader, dependencies, license, and side. These are **candidate pins**, not verified live install claims.

| Candidate | Exact CurseForge project / file | Filename | Candidate SHA-1 | Requirements and scope |
| --- | --- | --- | --- | --- |
| [Mowzie's Mobs](https://www.curseforge.com/minecraft/mc-mods/mowzies-mobs/files/7760267) | 250498 / 7760267 | `mowziesmobs-1.21.1-1.8.2.jar` | `d64475cd77444b056ece6472c79d40293dc63c6c` | GeckoLib already pinned; original mob AI, boss loot, Hunter tagging, capture and progression |
| [Aquamirae](https://www.curseforge.com/minecraft/mc-mods/aquamirae/files/9228645) | 536254 / 9228645 | `aquamirae-neoforge-1.21.1-7.2.13.jar` | `9ecfe49fcdc9266d4e2d3c6a05b7374ebc5f6f45` | GeckoLib plus new Fragmentum; aquatic mobs, fresh-world ocean structures and dragon diets |
| [Sons of Sins](https://www.curseforge.com/minecraft/mc-mods/sons-of-sins/files/8782200) | 538529 / 8782200 | `sons_of_sins-2.2.1d-neoforge-1.21.1.jar` | `ba1fe4eb8a3df55e611d39eaa1a837b23517dce9` | Necromantic rituals, organs/blood and transformations; verify blood economy, mobs, capture and exploit boundaries |
| [The Graveyard](https://www.curseforge.com/minecraft/mc-mods/the-graveyard-fabric/files/9346778) | 514468 / 9346778 | `graveyard-NEOFORGE-4.0.1+mc1.21.1.jar` | `eda871e48b28eefa2be8067dad72c2d36e8f31ff` | GeckoLib and new VillagersTradingPlus; crypt worldgen, creatures, bosses and structure collision |
| [Fragmentum](https://www.curseforge.com/minecraft/mc-mods/fragmentum-neoforge/files/9202578) | 1123977 / 9202578 | `fragmentum-5.4.0+1.21.1-neoforge.jar` | `711f46b591cbee203e68cf023277dae9060725f8` | Required Aquamirae library; do not treat as unrelated discretionary content |
| [VillagersTradingPlus](https://www.curseforge.com/minecraft/mc-mods/villagerstradingplus/files/9346744) | 1635301 / 9346744 | `villagertradingplus-neoforge-mc1.21.1-1.1.1.jar` | `8ffd86625d67bd911d7f2e90ef5b2f67ab3630ef` | Required by The Graveyard, minimum stated 1.1.0; verify precise native range, villager behavior and conflicts with Trader's Vengence |

All six files are deliberately **not indexed** and not added to `manifest/mods.json`, `manifest/next-qa-target.json` or shared `index.toml`/`pack.toml`. The master inventory and actual hash chain must be reconciled atomically by the single authorized integration writer, especially while other pin/config PRs remain open. Never hand-edit old 220/220 counts or label these INCLUDED solely because a candidate file exists.

## Other approved workstreams: prevent duplication

| Mod | Status and next action |
| --- | --- |
| HerbalBrews 1.1.4 | Unindexed provider-candidate pin on [draft #398](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/398). CF 951221/8918021; hash still needs independent exact-byte verification. **Do not restage here.** |
| Weeping Angels 1.0.5 | Separate active native-config workstream [draft #392](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/392). Tentative CF 291249/8962702, file `forge-weeping_angels-mc1.21-v1.0.5-forge.jar`, published SHA1 `2f5843d18e0fe3905d3569cc19022d4f2f7e7570` (secondary catalog only). Confirm **actual** 1.21.1 native mod metadata; preserve the approved six-destination native blacklist, encounter-only spawning and item-theft off. **Do not duplicate or supersede #392.** |
| Aura Cascade Reimagined 0.2.1+1.21.1 | Official NeoForge beta candidate CF 1519395/9005415, `aura-cascade-neoforge-0.2.1+1.21.1.jar`, Patchouli already pinned. **BLOCKED from exact pin:** trustworthy artifact hash not established in this source pass. Verify native loader/dependencies and beta correctness/performance before next QA. |
| Arcane Archives: Reborn 1.0.2 | Official NeoForge 1.21.1 candidate CF 1696190/8985321, `ArcaneArchives-1.0.2+Neoforge-1.21.1.jar`, requires Modonomicon; pack currently has 1.123.0. **BLOCKED from exact pin:** trustworthy SHA not established and native minimum Modonomicon version not checked. Verify storage/EMC, Occultism interoperability and restricted logistics scope before acceptance. |

**Not part of this new required tranche:** Deep Aether and Liroth excluded by explicit November 19 owner direction, Nuit solely for Liroth excluded, YUNG's Better End Island remains conditional on actual BetterEnd live compatibility, and the three unresolved old catalog identities are not authority to invent mods. Born in Chaos and Runes of Wizardry remain **research holds**, not current installation approvals. Scope freeze #390 retains every earlier category without silently promoting a historic/rejected option.

## Integration handoff and acceptance

1. Download and SHA-verify **each candidate artifact** from an authorized provider; inspect actual JAR `META-INF/neoforge.mods.toml` and bundled dependency IDs/ranges; confirm licensing/redist policy. Refuse conflicting metadata; never fake a hash or bypass restricted downloads. Check library chain closure for Aqua/Graveyard and Arcane.
2. Review distinct owner-scope candidates and active draft branches (#392 Angels, #398 HerbalBrews), then reconcile `manifest/mods.json`, `manifest/next-qa-target.json`, `index.toml`, `pack.toml` and QA inclusion register together at latest integration HEAD; preserve immutable provenance and do not replay stale manifest snapshots. If an artifact is blocked, record specific evidence and impact, do not silently omit it.
3. Type A static: run canonical `python3 scripts/validate.py`, `python3 scripts/validate_integration_sources.py`, ordinary Repository CI and any relevant existing manifest inventory validator; check no duplicate mod IDs, side mismatches, missing dependencies or stale packwiz hashes. This PR itself is **not** an integrated source-ready modpack.
4. Future owner-authorized client/server tests: actual mod resolution/boot plus Mowzie boss combat, Aquamirae frozen-ocean structures and aquatic diets, Sons' blood/soul loot with canonical blood, Graveyard crypt generation and spawns, villager/trader AI and existing magic capture/Progression/Hunter/MXP/EMC rules. Review any native configuration and balance choices with owner. Only after explicit authorization run full smoke, Prism and gameplay tests.

**Authority:** this pin-candidate PR owns six new `mods/*.pw.toml` files and this research record only. No assembly, server boot, Prism packaging, client launch, mod download or gameplay acceptance performed.
