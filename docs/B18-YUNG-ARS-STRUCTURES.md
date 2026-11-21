# B18 — All approved YUNG structure overhauls and universal Ars locating

> **November 20 owner update:** The earlier conditional hold on **YUNG's Better End Island** is lifted for a **required next owner-authorized comprehensive unstable QA inclusion attempt**, using existing 1.21.1 NeoForge 3.1.2 with a staged unindexed packwiz candidate. **Live End main island, dragon, gateway/exit portal and BetterEnd: New Dawn compatibility remain mandatory** before gameplay PASS. See [current reconciliation](qa/OWNER-MOD-DISPOSITIONS-2026-11-20.md). Historical B18 wording below is preserved for provenance; no full build or smoke authorized.

**Decision 2026-11-15:** add the eight remaining vanilla-exploration YUNG overhauls to the next test build, and stage YUNG's Better End Island conditionally with BetterEnd: New Dawn. Exact candidate releases are recorded in \`manifest/mods.json\`. **No new YUNG JAR is installed/pinned by this source PR.**

## New planned mods

| Mod | 1.21.1 NeoForge version | Modrinth version ID |
| --- | --- | --- |
| YUNG's Better Mineshafts | 5.1.1 | Go3nbneL |
| YUNG's Better Dungeons | 5.1.0 | ZWDbFefV |
| YUNG's Better Desert Temples | 4.1.5 | GQ9iNWkI |
| YUNG's Better Jungle Temples | 3.1.0 | rimnNoke |
| YUNG's Better Ocean Monuments | 4.1.1 | NWit9vEK |
| YUNG's Better Witch Huts | 4.1.1 | AvedwcIe |
| YUNG's Bridges | 5.1.0 | yA2nuSdw |
| YUNG's Extras | 5.1.0 | uE6pFqck |
| YUNG's Better End Island | 3.1.2 | I52NZ1qK |

**Better End Island hold:** test the **BetterEnd: New Dawn** port actually selected in B16, not only generic upstream Better End claims. Verify new-world central island, dragon, crystals/pillars, exit portal and gateways. Do not promote that mod until the combined End test passes. No Better Caves or Cave Biomes were approved.

YUNG's API is already approved in B17. Pin compatible 1.21.1 NeoForge artifacts from Modrinth, record SHA hashes and transitive dependencies, then dedicated-server/Prism/new-world smoke.

## Ars Additions — remove disabled vanilla targets, NOT useful ritual recipes

The installed Ars Additions 21.3.0 supplies native \`ars_additions:locate_structure\` JSON recipes. Their target tags currently contain *both* vanilla structures and YUNG replacement structures. For instance, \`ars_additions:stronghold\` in upstream generated resources has \`minecraft:stronghold\` AND \`#betterstrongholds:better_strongholds\`; a ritual can therefore search an entry that no longer naturally generates. Source: https://github.com/Jarva/Ars-Additions/blob/1.21.0/src/generated/resources/data/ars_additions/tags/worldgen/structure/stronghold.json

**Staged datapack tag overrides** (\`replace:true\`, YUNG-only target):
- \`ars_additions:stronghold\` -> \`#betterstrongholds:better_strongholds\`
- \`ars_additions:nether_fortress\` -> \`#betterfortresses:better_fortresses\`
- \`ars_additions:desert_temple\` -> \`#betterdeserttemples:better_desert_temples\`
- \`ars_additions:jungle_temple\` -> \`#betterjungletemples:better_jungle_temples\`
- \`ars_additions:monument\` -> \`#betteroceanmonuments:better_ocean_monuments\`
- \`ars_additions:witch_hut\` -> \`betterwitchhuts:witch_hut\`
- B17's newly-added \`tmm:mineshaft\` recipe now targets \`#bettermineshafts:better_mineshafts\` rather than the vanilla \`#minecraft:mineshaft\` tag.

This retains already-useful Ars recipes and their augment costs (including **12 Eyes of Ender** for the stronghold) but prevents them from wasting resources searching disabled vanilla structures. Any other vanilla structure with generation disabled later must undergo the same explicit tag/recipe audit. Check the selected exact Ars JAR still uses these target tags; source was inspected on upstream 1.21.0, not certified against the client's runtime.

## Locate ANY registered modded structure

**Source compiler implemented:** \`scripts/generate_ars_all_structures.py\`. It accepts an authoritative **full runtime structure registry dump** as JSON list or \`{"structures":[...]}\` and generates an Ars Additions Locate Structure recipe for every non-vanilla structure ID not on the explicit disabled list. It never guesses identifiers from biome names, config files or item IDs. Usage:

\`\`\`bash
python3 scripts/generate_ars_all_structures.py --self-test
python3 scripts/generate_ars_all_structures.py --registry /path/to/verified-structures.json
python3 scripts/generate_ars_all_structures.py --registry /path/to/verified-structures.json --write
# Then refresh packwiz index / hash, test registry and gameplay.
\`\`\`

Policy file \`manifest/ars_structure_locator_policy.json\` lists disabled IDs, namespace opt-outs, address marker items and dye palette. Each resulting recipe uses a **Compass, one Ars Source Gem and four colored dyes** as a distinct deterministic ritual augment combination (3,876 possible combinations). The generator searches for hash collisions, assigns unique combinations, and fails if there are more than 3,876 targets. These are **unordered ingredient multisets**, not typed structure names; players will need JEI/quest discoverability. New mods should not require coding each individual recipe: regenerate using the next exact live registry dump, prune compiler-owned recipes, and refresh packwiz metadata.

**Important limitation:** this is a recipe-generation path, **not a runtime arbitrary-text structure picker**. To select literally any structure by entering its registry name in a UI would require a custom runtime bridge/augmented item that the native Ars recipe codec does not supply. The current registry dump is NOT in the repository, so no all-structures output has been generated or certified. Structures present in the registry but disabled through mod configs must be enumerated in \`disabled_structures\` before generation; registry membership alone is not proof of world-generation availability.


### Registry export from the disposable test world

A **source-stage, admin-only** KubeJS command has been added at `kubejs/server_scripts/tmm_structure_registry_export.js`. In a disposable integrated world, run `/tmm_structure_registry` (requires operator permission), then copy the JSON content following `TMM_STRUCTURE_REGISTRY_JSON=` from `logs/latest.log` to a file and pass it as `--registry` to the compiler. The command uses the documented 1.21.1 KubeJS `Registry.of('minecraft:worldgen/structure').getKeys()` API and `ServerEvents.basicCommand`; the exact client/server runtime remains untested. Export only, no world mutation or files written outside normal logs. **Registry presence does not prove worldgen is enabled**; apply the disabled ID list to avoid unreachable recipes.

### Required acceptance
1. Obtain actual live structure registry IDs and dimension/worldgen status from a disposable server running *exactly* this pack; confirm every enabled modded structure appears, with no phantom biomes/features.
2. Confirm each YUNG replacement tag is nonempty in live registry and that its vanilla predecessor does not generate. Use Ars ritual to locate those replacements in new chunks; test the stronghold End portal and Nether fortress blaze/wither skeleton spawning.
3. Regenerate target-specific Ars recipes, verify the schema and KubeJS datapack discovery, refresh packwiz hashes, and verify a Wayfinder points to each sampled target.
4. Confirm disabled/removed mod structures don't retain reachable Ars recipes, including the original built-in entries. Check vanilla+worldgen conflicts and BetterEnd gate.
5. Player-visible discoverability: add JEI/Ars quests/index for choosing from all generated addresses, rather than forcing memorization of hundreds of dyes. This is a necessary UX follow-up before declaring universal locating done.
