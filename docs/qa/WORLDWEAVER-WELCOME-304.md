# WorldWeaver welcome and default-preset audit — issue #304

**Status:** exact-JAR source audit and non-runtime config candidate. This branch starts from `test/full-integration` commit `077a34d8706dba51b10ad65dde05a7d34cfb5c96`. It does not modify indexed runtime config, assemble the pack, launch Minecraft, or claim world-generation acceptance.

Issue [#304](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/304) was last updated on 2026-11-17 and had no comments, remote implementation branch, or open implementation PR at pickup. Sprint inventory [#431](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/431) names this exact pinned-JAR/default-preset investigation as ready work. The existing config was introduced by commit `3ad5b1f80d9306c994758f970e346818d84220b2`; its welcome suppression is correct, but its preset conclusion was based on moving source and remained explicitly unverified in client/worldgen QA.

## Exact frozen inputs

All JARs were streamed from the URLs already frozen in their Packwiz metadata, hashed outside the repository, and inspected without committing their bytes.

| Artifact | Frozen identity | SHA-512 | Size |
| --- | --- | --- | ---: |
| WorldWeaver: New Dawn | `worldweaver-21.0.25.jar`, Modrinth `R8uGDQpB/TrReu8cF` | `23926e6f737f9d73797ea7e5dfac2e6611a6cecdb768f236465c05e5750108100b6dbeb911e5f55b987f81a30f786e30a5910904dec6751e5c56bc8bf78eb2a7` | 2,215,098 |
| BetterEnd: New Dawn | `BetterEnd-21.0.39.jar`, CurseForge `1422294/9306604` | `41cbcc3e104269ac9330747018a4e6a130647d4df598d59307f33520764f22146dfafdfcd3971cefb344d610e71f832f345c01370658374a553568af53a32257` | 97,734,709 |
| Tectonic | `tectonic-3.0.28-neoforge-21.1.jar`, Modrinth `lWDHr9jE/n4iyW7aB` | `e07b9cb54f078b7bf74c925c5cebd6643f0b4c210b8f94474e68659b69468ece7d61432f70eafe7abaa1dfa798e4638aad297987c121dc127588c2623b7f7a81` | 451,757 |
| BCLib: New Dawn | `bclib-21.0.26.jar`, Modrinth `7bdKEtKC/QkXw8Fsl` | `0724010228a724c77b021676310e96e6d428a5df1f66890ee11b74ea1c2592bdaf9be250bcdbd0eb8bf4a1d4d0c5bddde06fca5ab5d6d700be19d0c86fe67bf2` | 2,159,176 |
| WunderLib: New Dawn | `wunderlib-21.0.10.jar`, Modrinth `HZmhgdJk/5db3GZzg` | `8785b16968ed82ead0fc3f6f42ca3dd7fa94b94a79eeb97a61e57b258bf8c0e6775002a561cf28a1840216bd06f3b86a31da420d4ccc2d37c14abe22058bdf96` | 378,820 |

WorldWeaver declares Minecraft `1.21.1`, NeoForge `[21.1.176,)`, WunderLib `[21.0.8,)`, mod ID `wover`, MIT, and both sides. BetterEnd declares BCLib `[21.0.14,)`, WorldWeaver `[21.0.16,)`, WunderLib `[21.0.9,)`, and both sides. The frozen versions satisfy those declared ranges.

## Exact 21.0.25 behavior

The exact JAR's `ClientConfig` defaults `internal.did_present_welcome_screen` to `false` and `general.force_betterx_world_type` to `true`.

- `VersionCheckerClient.presentUpdateScreen` queues `WelcomeScreen` only when `did_present_welcome_screen` is false. Shipping `true` therefore suppresses this specific WorldWeaver welcome screen without changing `loading.disable_experimental_warning` or update checks.
- `WelcomeScreen` writes `did_present_welcome_screen=true` on Proceed and separately saves its BetterX checkbox into `force_betterx_world_type`. The two settings are independent.
- `LibWoverWorldGeneratorClient` suggests `wover-generator:normal` at priority 2000 when the BetterX flag is true. When false, it suggests `minecraft:normal` at priority 10000. `CreateWorldScreenMixin` uses that selected default for every fresh client world.
- `wover-generator:normal` retains the current Overworld stem, then installs WorldWeaver Nether and End biome sources. Its preset-info record explicitly identifies `minecraft:normal` as the Overworld override.
- Tectonic's built-in resource pack replaces `minecraft:worldgen/noise_settings/overworld.json` and its density-function graph. Its exact incompatible-preset tag contains only `minecraft:large_biomes` and `minecraft:amplified`; it does not exclude `wover-generator:normal`. Consequently the BetterX normal preset's retained Overworld stem is the source-level route that can preserve Tectonic terrain.
- BetterEnd supplies 27 `data/betterend/wover/worldgen/biome_data` entries plus matching surface rules for WorldWeaver's End biome source. Selecting plain `minecraft:normal` does not itself install that source. The current shipped `force_betterx_world_type=false` therefore cannot be considered proof that new worlds retain BetterEnd generation.
- On a dedicated server, `MainConfig.server.force_default_world_preset` defaults true. `DedicatedServerPropertiesMixin` uses the selected WorldWeaver default when that flag is true and only fills a missing `level-type`; an explicit server `level-type` remains reviewable configuration. The candidate pins the native true value explicitly rather than changing unrelated server properties.

## Reviewable candidate

`docs/qa/candidates/worldweaver-304/client.json` keeps the verified welcome value true and changes only `force_betterx_world_type` to true. `docs/qa/candidates/worldweaver-304/main.json` pins the native server default true. These files are deliberately outside `config/` and `index.toml`; they are evidence for the integration writer, not installed behavior.

The integration writer should copy the two candidate JSON objects to `config/wover/client.json` and `config/wover/main.json`, reconcile `index.toml` and `pack.toml`, update the active QA ledger, and run normal quick validation. Do not copy decompiled sources or third-party JARs into the repository.

## Remaining limits

Static source evidence establishes setting semantics and the intended composition path, not actual registry or terrain output. Owner-authorized client QA must still verify no welcome screen, the default world-type selection, Tectonic terrain/noise, BetterEnd biome IDs and Thallasium in fresh End chunks, End Cities, Alex's Caves/YUNG structures, vanilla Nether coverage, save/reload, and unchanged existing worlds. No Integration Assembly, server/client launch, full smoke, or Prism package was run.
