# First surface-density tuning pass (#746)

Owner-approved relative frequency factor: 0.5 for reviewed ordinary Overworld surface sets. No new mods, custom codecs, spacing changes, biome rewrites, structure weights or progression-gate changes.

## Implementation

69 exact audited random-spread structure sets across 13 pinned archives. KubeJS data overrides copy the original resource, changing only placement.frequency. Existing implicit 1 becomes 0.5; Botania floating islands 0.6 becomes 0.3; Goety graveyard 0.2 becomes 0.1 (legacy reduction method retained). Original definitions and source pin hashes are preserved in first-pass.json. All files are packwiz-indexed. Remove only these overrides and refresh packwiz hashes to roll back. Existing generated chunks remain unchanged.

| Namespace | Scope |
|---|---|
| apotheosis | towers |
| astralsorcery | columns and dig sites |
| botania | broken trees, floating islands, lakes, plants, ruins |
| ars_additions | ruined warp portals |
| born_in_chaos_v1 | common towers/farms/firewells/pumpkins and decorative graves |
| eidolon_repraised | stray tower |
| goety | ordinary shacks, graveyards, mangrove towers, igloos, wrecked mansion |
| graveyard | ordinary graves, trees, ruined villages and haunted houses |
| hexerei | covens, post offices and witch huts |
| irons_spellbooks | evoker fort, mangrove hut, mountain and pyromancer towers |
| witchery | hobgoblin huts and wells |
| sons_of_sins | sinful church |
| primalmagick | ordinary surface shrine set (all original variants and weights retained) |

**Exemptions:** Astral focal points, Botania giant mushrooms, vanilla, underground/Alex's Caves/YUNG's/Pastel/Dungeon Crawl, mixed-dimension Primal libraries, rare Astral rotunda/moon dial/obliteration, rare Goety progression and linked custom crypt/shrine wings, rare Graveyard lich prison/altar, Hexerei Baba Yaga, EvilCraft temple with cave eligibility, boss structures (Cataclysm/Mowzie), Ars Wilden ritual-related dens, rare Ars libraries/nexus, dimension gateways, aquatic, custom placement and unreviewed sets. No mod-specific dimension sets edited. Datapack structure-set frequency is global to the set: if another dimension shares its eligible Overworld biomes, the override could also affect that dimension. Existing biome eligibility is unchanged; effective runtime registry and dimension behavior must be checked in the next authorized QA.

## Verification and caveats

The source inventory is from instrumented Thanksgiving seed 424242, artifact 15065082613, snapshot 721ea058294f838b9d7f145af427f5db228d248d. All selected pin hashes matched the integration source at creation. Quick checks compare the full original data to the exact pinned JARs and prove the only resource-field change is frequency. Pin drift fails review rather than silently replacing new upstream data. The scope is intentionally conservative: these are selected standard random-spread sets, not a global filter, and the same namespace may include held sets. Native configs, exclusion zone interactions and nonstandard code can alter effective generation. No guarantee of exact halving of final starts or chunk occupancy. No live test or new full server build authorized in this source pass.

## Acceptance

Next separately authorized test: confirm active effective placement frequencies and unchanged protected sets; compare fresh seed 424242 against the prior TMM pilot, identical measured area/buffer/observer. Target 1–2% occupied, 25–40% nearby within 8 chunks, 25–40% wilderness within 16 chunks; repeat other seeds before selecting additional cuts. First-pass simulation on the broader 50-start thinning population was 2.4% occupancy; this narrower allowlist is not identical to that model. Preserve progression availability and use owner gameplay survey for actual visibility.
