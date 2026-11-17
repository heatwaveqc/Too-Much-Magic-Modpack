# B15: Alex's Caves Continued, CodxLib and JER — reactor-free next QA

**Status: pinned and indexed in the integration source; NOT NeoForge booted, launched in Prism, or gameplay accepted.** This is the intentionally narrow replacement for draft [PR #56](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/56) at frozen source commit `ac250b44ae4a66b7d5eff9568e82ca61d51423c7`.

## Exact pinned 1.21.1 NeoForge releases

| Mod | Installed Packwiz metadata | Version / provider | Side |
|---|---|---|---|
| Alex's Caves Continued | `mods/alexs-caves-continued.pw.toml` | 1.1.2, Modrinth `wSeCJMlI` | client + server |
| CodxLib | `mods/codxlib.pw.toml` | 1.5.1, Modrinth `FW5ZWCXm` | client + server |
| Just Enough Resources (JER) | `mods/just-enough-resources-jer.pw.toml` | 1.6.0.57, Modrinth `A2G7NFcD` | client only |

The three `mods/*.pw.toml` files are **Git-blob-identical** to the PR #56 verified upstream Modrinth SHA-512 metadata, and now appear in `index.toml` with recalculated SHA-256 and an updated `pack.toml` index hash. CodxLib is the required library; Citadel is bundled by the Alex's Caves port and **must not** be added independently. Existing JEI satisfies the JER integration dependency. Check the actual download/content at owner-authorized assembly.

## Uranium: deliberate removal of retired Bigger Reactors code

Bigger Reactors and Bigger Reactors Rework have already been removed from Tyr. The old PR's `biggerreactorsrework:overworld_ores` biome-modifier suppression, `alexscaves:uranium -> biggerreactorsrework:uranium_ingot` recipe, and the BR-only uranium gate/test scripts are **obsolete and intentionally not imported**.

Alex's Caves Continued supplies native `alexscaves:radrock_uranium_ore` in the Toxic Caves biome. Leave its native feature unchanged; do **not** add duplicate uranium generation. No global vanilla-Overworld ore injection is introduced here. The separate progression policy restricting special uranium sources and Eldritch Miner interactions remains authoritative; no new ProjectE/Replication shortcuts are granted by this patch.

## Next authorized disposable-world QA

1. Resolve the three indexed JARs, verify their SHA-512 download against the pinned metadata, and confirm CodxLib and Alex's Caves start together without external Citadel.
2. Generate a fresh **Tectonic** Overworld and find Toxic Caves; verify cave biomes, blocks, native uranium ore, survival harvestability and radiation rules. No generation claims for existing chunks.
3. Verify the native `alexscaves:radrock_uranium_ore` block, `alexscaves:uranium` item and cave map IDs from loaded JAR/registry. Test no separate worldgen injection and no remaining retired reactor-specific KubeJS crafting route.
4. Launch a client with JEI/JER and verify ore/mob/drop display for both vanilla and added caves. The JER informational display is **not** a worldgen-security enforcement mechanism.
5. Test multiplayer client join, save/reload, server restarts and adjacent biome/worldgen compatibility; check for incompatible cave/worldgen mixins.
6. Recheck modpack index hash and static manifest metadata in the final assembled candidate. **No full server smoke or Prism build runs merely because this document exists.**

Do not reintroduce `kubejs/data/biggerreactorsrework/`, `kubejs/server_scripts/toxic_caves_reactor_uranium.js`, or the old stage-only `scripts/stage_next_qa_alex_jer.py` which still declares the deleted reactor conversion.
