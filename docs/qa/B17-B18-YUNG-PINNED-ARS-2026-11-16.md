# B17/B18 — pinned YUNG, loot, QoL and Ars structure locators

**Status:** exact provider SHA-512 pins are indexed into the unstable integration source; no full NeoForge boot, Minecraft worldgen test, Prism QA package, client launch or gameplay acceptance has been run. This is the narrow replacement for historical [PR #135](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/135) with source SHA `0efb800e3976f23c125c58cbce36ccf473adc779`.

## Included, pinned and indexed (15 new mods)

| Area | Approved 1.21.1 NeoForge versions |
| --- | --- |
| Libraries/Overhauls B17 | YUNG's API 5.1.9, Better Strongholds 5.1.3, Better Nether Fortresses 3.1.5 |
| Loot and performance B17 | LootJS 3.7.0, Clumps 19.0.0.1, Extreme Sound Muffler 3.57 (client-only), Get It Together Drops! 1.4 |
| YUNG B18 | Better Mineshafts 5.1.1; Better Dungeons 5.1.0; Better Desert Temples 4.1.5; Better Jungle Temples 3.1.0; Better Ocean Monuments 4.1.1; Better Witch Huts 4.1.1; Bridges 5.1.0; Extras 5.1.0 |

The **already installed AttributeFix 21.1.3 remains untouched**; it was the eighth B17 selection, not a new pin. Original Modrinth exact release IDs and official SHA-512 file hashes are in the 15 `mods/*.pw.toml`. Each metadata-file SHA-256 is in `index.toml`, the pack's index digest is updated in `pack.toml`, and `manifest/mods.json` marks the 15 pins for installation at the next authorized assembly. Both Gilded batch IDs `B16` and `B17`, and separate BetterEnd `B16-BETTEREND`, remain unchanged. The YUNG plans are tracked as `B17-YUNG` and `B18`.

**Deliberate hold:** YUNG's Better End Island 3.1.2 remains `enabled_in_pack=false`, with **no installed Packwiz pin**, until new-world testing confirms compatibility with BetterEnd: New Dawn, Ender Dragon, crystals, exit portal, central island, and return gateways.

## Native Ars Additions source, not a universal locator gameplay PASS

- Six `ars_additions` structure tag overrides target the correct **YUNG replacement** stronghold, fortress, desert temple, jungle temple, ocean monument and witch hut instead of disabled vanilla templates.
- Two independent `ars_additions:locate_structure` recipes for Better Mineshafts and vanilla shipwreck retain their Rail/Nautilus Shell costs.
- `kubejs/server_scripts/tmm_structure_registry_export.js` is an operator-only experimental export; `scripts/generate_ars_all_structures.py` requires a verified live runtime structure registry and uses `manifest/ars_structure_locator_policy.json` to exclude disabled structures. It **does not** generate hundreds of speculative recipes at source time.
- Static validators `scripts/validate_b17_ars_locators.py`, `scripts/validate_b18_yungs_ars.py` validate source + indexed data. The B18 validator correctly does not require the source-only `manifest/` policy to be installed in Packwiz.
- **Explorer's Compass stays excluded**. No old automatic full-smoke workflow, stale branch-wide `manifest/mods.json`, obsolete `index.toml`, or fabricated registry map is imported.

## Next disposable QA, when separately authorized

1. Resolve exact 15 indexed mod JARs (plus existing AttributeFix and B16 BetterEnd four mods), verify provider hashes, transitive dependencies and KubeJS/Ars Additions version compatibility on client and dedicated server.
2. Generate *new* Overworld/Nether and verify YUNG replacement structures, stronghold End portal, Nether blaze and wither skeleton spawns, loot density and Tectonic interactions. Verify fresh End terrain with BetterEnd New Dawn separately.
3. Use Ars Locate Structure rituals with the selected augment costs to confirm correct new locations and no vanilla dead targets. Check native `Ars Additions 21.3.0` recipe codec/tag support.
4. Export the **actual** full structure registry and enabled-worldgen list, filter disabled entries, generate modded-structure recipe addresses, reindex, validate no collisions and test wayfinders plus JEI discoverability.
5. Compare Clumps and Get It Together Drops! behavior for unique drops, ItemStack NBT, XP total under dense mobs; test Extreme Sound Muffler client.
6. Test YUNG Better End Island *only in a separate controlled comparison* before lifting the hold. Incompatible End worlds are not to be migrated in place.

**Source pin/quick CI passing is not a client or gameplay certification; no automatic Integration Assembly, full server boot or Prism packaging has been authorized.**
