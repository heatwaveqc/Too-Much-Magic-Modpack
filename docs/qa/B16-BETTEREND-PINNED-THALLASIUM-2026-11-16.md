# B16 — BetterEnd: New Dawn + Thallasium Iesnium gate

**2026-11-16 source state:** all four approved Minecraft 1.21.1 NeoForge releases are now **SHA-512-pinned in Packwiz**, and the only native Occultism Spirit Fire Iesnium recipe now uses `betterend:thallasium_ore`. This is **metadata/source readiness**, not an End worldgen, dedicated-server, Prism-client or gameplay PASS.

## Exact selected files

| Mod | Exact file/version | Provider |
| --- | --- | --- |
| BetterEnd: New Dawn | `BetterEnd-21.0.39.jar`, file `9306604` | [CurseForge New Dawn](https://www.curseforge.com/minecraft/mc-mods/betterend-neoforge/files/9306604), SHA-512 `41cbcc3e104269ac9330747018a4e6a130647d4df598d59307f33520764f22146dfafdfcd3971cefb344d610e71f832f345c01370658374a553568af53a32257` (computed while streaming exactly this JAR in a metadata-only runner; JAR was not committed) |
| BCLib: New Dawn | `bclib-21.0.26.jar`, version `QkXw8Fsl` | [Modrinth](https://modrinth.com/mod/bclib-neoforge/version/QkXw8Fsl), official SHA-512 |
| WunderLib: New Dawn | `wunderlib-21.0.10.jar`, version `5db3GZzg` | [Modrinth](https://modrinth.com/mod/wunderlib-neoforge/version/5db3GZzg), official SHA-512 |
| WorldWeaver: New Dawn | `worldweaver-21.0.25.jar`, version `TrReu8cF` | [Modrinth](https://modrinth.com/mod/worldweaver-neoforge/version/TrReu8cF), official SHA-512 |

All four `mods/*.pw.toml` metadata files have `side = "both"`. `index.toml` contains their SHA-256 metadata digests and the replacement recipe's SHA-256, with a matching `pack.toml` index digest. `manifest/mods.json` flags all four as indexed. They are **not** claimed to be client/server loaded. This implementation preserves Gilded's existing `B16` batch separately under `B16-BETTEREND`.

## Unique Iesnium route

Replaced `kubejs/data/tmm/recipe/spirit_fire/iesnium_from_end_draconium.json` with `kubejs/data/tmm/recipe/spirit_fire/iesnium_from_thallasium.json`, containing native `occultism:spirit_fire` type and **one `betterend:thallasium_ore` block → one `occultism:iesnium_ore`**.

The older End Draconium recipe is **deleted**, not left as an early alternate path. Updated `scripts/validate_cc_iesnium_pilot.py` retains the Eldritch-gated CC: Tweaked checks, verifies only the new recipe, and checks the changed Packwiz index/hash. No separate scripted dropped-item detector was added.

## Next owner-authorized acceptance

1. Assemble and check **all four actual JARs** and dependencies on both sides, with provider hashes, without mixing original Fabric libraries or duplicate New Dawn ports.
2. Launch a clean NeoForge server and Prism client, generate fresh End chunks, and verify BetterEnd terrain, biomes, ore registration, portal return and Tectonic/other End mod compatibility.
3. Check `betterend:thallasium_ore` is a valid registered harvestable block; test ordinary and Silk Touch mining to learn whether a whole block requires Silk Touch.
4. Throw 1 block and a stack into native Occultism Spirit Fire; count output and confirm no conversion from End Draconium or generic End Stone.
5. Retest CC Eldritch Logic Core crafting/mining and existing unlock gates, plus effects on End progression and Iesnium access.
6. Keep **YUNG Better End Island** uninstalled/held until the actual BetterEnd central-island/dragon/crystal/exit portal compatibility test passes.

**No full smoke, client launch, Integration Assembly or Prism packaging was executed in this source-pin reconciliation.**
