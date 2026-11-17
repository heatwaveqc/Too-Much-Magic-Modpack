# B17 — Approved mod selections and Ars Additions structure-locator extension

**Date:** 2026-11-15. **Status:** approved roster / verified 1.21.1 NeoForge upstream versions, but **not pinned, installed or boot-tested**. MC 1.21.1 NeoForge 21.1.265 / Java 21.

## Approved additions (seven mods plus one required library)

| Mod | Selected 1.21.1 NeoForge release | Modrinth version ID | Runtime side | Function |
| --- | --- | --- | --- | --- |
| LootJS | 3.7.0 | `5AZDyUSn` | Server, including local singleplayer | KubeJS loot event support; no loot rules introduced |
| Clumps | 19.0.0.1 | `jo7lDoK4` | Server | XP orb clustering |
| YUNG's Better Strongholds | 5.1.3 | `8U0dIfSM` | Server with compatible client/pack setup | Stronghold replacement |
| YUNG's Better Nether Fortresses | 3.1.5 | `iopJiJQp` | Server with compatible client/pack setup | Nether fortress replacement |
| YUNG's API | 5.1.9 | `2prKITKh` | Both | Required YUNG dependency and improved structure locator compatibility |
| Extreme Sound Muffler | 3.57 | `nUCCmquN` | Client | Selectively mute sound events |
| Get It Together, Drops! | v1.4 | `K4TUzlwa` | Server | Radius-based item stacks consolidation |
| AttributeFix | 21.1.3 | `TyNITLDY` | Both | Higher vanilla attribute caps; stat balance audit |

Upstream sources and version IDs are recorded in `manifest/mods.json`. Every roster entry is `enabled_in_pack=false` until an **exact versioned, hashed** packwiz `mods/*.pw.toml` is checked in, indexed and tested. Do not substitute 1.21.4/26.x files or assume server-only mods can safely be omitted from the singleplayer test instance. Check required dependency closure (LootJS needs existing KubeJS, YUNG addons need YUNG's API; verify AttributeFix library requirements).

## Explorer's Compass decision — use existing Ars Additions instead

**Do not include Explorer's Compass.** Ars Additions **Ritual of Locate Structure** is already installed. Native `ars_additions:locate_structure` JSON recipes accept an item/tag augment list, an exact structure key OR a structure tag, optional `radius`, and optional `skip_known_structures`; the ritual returns a Wayfinder pointing to the target.

Primary source: https://github.com/Jarva/Ars-Additions/blob/1.21.0/src/main/java/com/github/jarva/arsadditions/common/recipe/LocateStructureRecipe.java ; existing generated examples: https://github.com/Jarva/Ars-Additions/tree/1.21.0/src/generated/resources/data/ars_additions/recipe/locate_structure . Version-scoped target: pack's Ars Additions 21.3.0, inspect live codec/JAR to confirm this source contract before declaring runtime PASS.

### Proof-of-format, non-gated additions

- `kubejs/data/tmm/recipe/locate_structure/mineshaft.json`: offer one vanilla **Rail** to locate `#bettermineshafts:better_mineshafts`, intentionally excluding disabled vanilla mineshafts when YUNG's replacement is enabled.
- `kubejs/data/tmm/recipe/locate_structure/shipwreck.json`: offer one vanilla **Nautilus Shell** to locate `#minecraft:shipwreck`, covering both at-sea and beached shipwrecks.

Both use the native Ars Additions ritual; no Java or new structure compass mod. These two files are staged as a **source pilot**. The reference vanilla structure tags are documented in https://minecraft.wiki/w/Structure . The JSON's embedded `id` is intentionally a unique `tmm:` ID, as required by the upstream codec.

### Expansion plan for the modpack

1. Export the **actual** 1.21.1 modded structure registry from a disposable full integration world, or extract and verify structure JSON IDs from pinned JARs. Include Alex's Caves, Neo Vitae, Occultism, Cataclysm, Feywild, Ars Additions, Goety, and BetterEnd where applicable; do not invent a structure ID for terrain biomes/caves which are not registered structures.
2. Group appropriate structures by thematic structure tags (e.g. `tmm:arcane_libraries`, `tmm:eldritch_ruins`), then add distinct `ars_additions:locate_structure` datapack JSONs with specific, thematically appropriate and progression-consistent augment sets. Validate cost and search radius per dimension. Avoid hard-gate bypasses and infinite free scanning.
3. **YUNG integration:** native Ars Additions fortress and stronghold recipes already exist. Verify the original ritual targets the actual YUNG replacement structures rather than empty vanilla locations. YUNG API 5.1.9 added structure-replacement support for locate utilities, but ritual compatibility is **not guaranteed** until world-tested.
4. Add tutorial/JEI/quest coverage in the Ars progression chapter only after new recipes are actually found and verified in-game.

## B17 required QA and integration

1. Pin the eight exact upstream mod files (matching hashes, sides, full dependencies), ensure the manifest flags and packwiz index agree, and run clean server startup + Prism client/new world tests. The first-party mod build and other pending B16 BetterEnd changes are **separate**; do not accidentally merge B16 into B17 or claim B16 included.
2. With Clumps and GiTD, compare XP-orb totals and NBT-sensitive/special item drops under mob-farm and Torment Nexus loads. Set a conservative GiTD search radius; tags should prevent unwanted merging of unique items.
3. With AttributeFix, export effective vanilla/Apothic Attribute caps; ensure the Monk theoretical-max balancing audit uses the **installed** environment. Do not use this mod to create unlimited balance without explicit design.
4. With YUNG mods, test new-world stronghold generation, valid End Portal activation, Ender Eye search, Nether Fortress loot and wither skeleton/blaze spawn behavior, and the existing Ars stronghold/fortress Wayfinder.
5. Load two new Ars recipes in `/reload`/JEI and use a Ritual Brazier with each augment; ensure a Wayfinder is produced with correct position and that no relevant default Ars recipe is overwritten.
6. Use `python3 scripts/validate_b17_ars_locators.py` for the staged static schema and packwiz hash checks; this does **not** replace an actual Minecraft registry or gameplay test.

**No change to existing worlds, quest balance, loot or progression gates is approved by simply adding these mods.**

## B18 superseding expansion (2026-11-15)

Nine more YUNG modules approved (Better End Island conditional), plus a complete native Ars Additions target-tag audit and a registry-driven all-modded-structures locator generator. See [B18-YUNG-ARS-STRUCTURES.md](B18-YUNG-ARS-STRUCTURES.md). The earlier B17 mineshaft target is now the YUNG replacement tag, not vanilla.
