# Ars Elemental 0.7.10.4 — five biomes, Vexing Caves and Starbuncle Shrine

Exact addon SHA1 `562f640916aa6f5c83b3b3ee2384ca828b870e01`; [receipt](ars-elemental-pinned-guide-urn.md). Five biome JSON, configured/placed features, biome/structure tags, three structure/pool files, compressed shrine NBT parsed read-only; ArsElemental/TerrablenderAE/ModWorldgen, SourcestoneFormationFeature/QuartzSpikeFeature/BlackstoneFormation, required weather mixin and Breeze placement handler. No new world, terrain, spawn, structure or resource harvest acceptance. Mage combat/loot remains a separate next audit.

## Access and what generation actually adds

These are Overworld biomes, not portal dimensions or new spellbook tiers. Common setup queues TerrablenderAE.registerBiomes only when TerraBlender loaded and **extra_biomes>0** (default1, range0..100); registers weighted ArchwoodRegion plus surface rules. Registry/JSON presence is separate from enabled terrain distribution. It does not regenerate old chunks. Existing saves need unexplored eligible terrain; no verified seed/location or biome-finder item is established here.

Use Worn Notebook **Archwood** pages for native/Flashing materials and named helper pages, plus JEI for recipes. Source has no complete bespoke expedition/build instruction for every biome feature; a future quest must supply safe exploration/identification and local resource steps, not send a player to an external wiki or require finding all five before ordinary casting. These are optional resource/ecology branches alongside normal Ars progression.

| Biome `ars_elemental:` | Packaged temperature/downfall/precipitation | Distinct resources and hazards |
| --- | --- | --- |
|`blazing_forest`|0.9 /0.1 /false|Blazing Archwood clusters, Torchflowers, Blackstone formations, lava pools; Fire Mage, Blazing Walker and Husk entries. Volcanic material hazards, not evidence base temperature meets Fire Bangle's>1.8 threshold.|
|`cascading_forest`|0.7 /0.8 /true|Cascading clusters, sparse Mangrove, drip/pool feature, river fish/Frogs; Water Mage/Cascading Walker/Drowned. Aquatic helper eligibility still uses own tags/placement.|
|`flashing_forest`|0.4 /0.8 /true|Flashing clusters, Sparkflowers, Quartz spikes; Air Mage/Flashing Walker, Flashjack, Goats, Breeze. Extra FlashLightning is rain/config/sample gated, not permanent damaging thunder.|
|`flourishing_forest`|0.7 /0.6 /true|Flourishing clusters, sparse jungle trees, ground blossoms; Earth Mage/Flourishing Walker/Bogged.|
|`vexing_caves`|0.5 /0.5 /true|Vexing cluster/short/ceiling trees, native Sourceberry bushes and addon ceiling vines, Sourcestone deposits/formations/lights; all four Mage types plus Wilden hunters/stalkers/guardians and Vexing Walkers. It is an underground biome branch, not an independently generated dungeon/boss arena.|

TerraBlender climate mappings: Blazing prefers neutral/warm/hot and arid/dry/neutral middle/far inland; Cascading spans icy/cool/neutral/warm and humid/wet/neutral coast→far inland; Flashing middle/far inland/low erosion and outer weirdness bands; Flourishing neutral/warm inland with neutral/wet/humid; Vexing frozen/cool/neutral, mushroom/middle/far inland, erosion0..3, **climate depth0.1..0.8**. Climate depth is **not block Y**. Mapper also adds native Archwood, Ocean and River overlays; don't convert region weight/climate bands into encounter probabilities or exact coordinates. Surface rules for Flourishing/Vexing use grass ON_FLOOR and dirt UNDER_FLOOR above bottom+6, not a hardcoded grass plane at a fixed world height.

Common biome helpers include Starbuncle weight2/group3–5, Drygmy/Whirlisprig weight2/group1–3 and matching Walker weight10/group1–3. Mage entries weight4/group1–3 per included type. Flashing has its own Flashjack weight35/group1–2, separate from generic modifier weight7 elsewhere; actual merged spawn table may contain both contributions. Breeze weight5/single is also constrained by addon placement handler: in Flashing tag it rejects Y<45, native dark-enough condition **or air directly below**. Night/darkness is not permission for unrestricted Breeze spawning. Vexing includes Wilden Hunter100/group3–5, Stalker100/1–3, Guardian80/2–5. These weights are relative spawn-table entries, not per-second spawn rates or guaranteed farm outputs.

## Vexing Caves: how resources are placed

Packaged features use native mechanisms and explicit placement attempts:

- Ceiling Vexing trees: count105 attempts, random Y from aboveBottom0 to absolute256, scan upward through air≤12 to solid, offset−1. Short trees: count75, analogous downward solid scan/offset+1, water-depth≤3. These counts precede biome/terrain/tree survival checks, not actual trees per chunk.
- Ceiling berry-vine column: count88, upward scan≤12 to sturdy downward face, offset−1; downward column config uses custom source_vines/head/body and random berry flags. Right-use yields native Sourceberries; [actual harvest/break boundary](ars-elemental-pinned-plants.md). No collected vine seed item inferred.
- Native patch_berry: rarity2 attempt, uniform height aboveBottom0..256. Underground lights count7; lit source formation also appears in more than one biome feature stage. Do not silently deduplicate this registry inventory into a measured spawn frequency.
- Sourcestone ore feature replaces `#minecraft:base_stone_overworld` with native Sourcestone, size64, two placed attempts in aboveBottom0..absolute30. This is actual block material, not mined Source currency or an XP/FE ore.
- SourcestoneFormationFeature searches downward for dirt-tag/Deepslate/stones-tag ground, then creates height6–17/base radius2–4 tapered Sourcestone/Alternating Sourcestone, top lit native Source Lamps at light8. Placement uses direct setBlock; harvesting/Source Lamp operation follow native blocks, not a new Source generator here.

Other biome geological features matter to safe collection: QuartzSpikeFeature builds height4–9/base radius1–2, lower Calcite/Diorite and upper Quartz Blocks. BlackstoneFormation can place Gilded Blackstone at10%per checked block and lava at its top/optional short flow; do not treat it as decorative-only terrain. Actual harvest drops/tools and loaded datapack overrides remain separate from feature's placed block IDs.

## Structure branch and existing structures

Addon **Starbuncle Shrine** is `minecraft:jigsaw`, size2, start pool `ars_elemental:starbuncle_shrine/shrine`, surface_structures/WORLD_SURFACE_WG, startHeight0, maxDistance80, expansionHack=false. Eligibility tag combines village-plains structure biome tag and forest biome tag. Random-spread structure set spacing100/separation50chunks, salt294539046, single weighted entry. These are candidate grid rules, not a guaranteed shrine at a fixed distance. Pool uses one rigid single element `ars_elemental:starbuncle_shrine`, empty processor/fallback lists.

Read-only gzip/NBT parser confirms **13×13×13** template, embedded native Starbuncle entities with tamed=0, decorative wool/terracotta/concrete/grass, **26 Sourceberry Bush blocks, two Raw Gold Blocks and an Anvil**; no block-entity chest/loot table in this template. Names/lore are decoration, not a hidden acquisition ritual. Structure creature spawn override permits Starbuncles weight5/group1–3 in piece bounding box over time; do not count saved entities as proven spawned/retained in an actual placed structure. Use [native creature/worker](ars-nouveau.md) acquisition rules; no auto-taming/familiar unlock from entering shrine.

Biome tags also extend **native** Woodland Mansion eligibility through Archwood tag, Jungle Temple to Flourishing, Swamp Hut to Cascading. Optional Ars Additions Arcane Library/Nexus Tower tags list all five addon biomes; this is only one side of those addons' structure bridge, to reconcile in Additions audit. No duplicate bespoke Elemental versions of these structures are registered by these tags.

## Quest / unrun acceptance handoff

Teach one optional expedition: prepare light/escape/normal combat protection, explore new terrain, identify selected school's tree/biome, collect a renewable starter plant, return to JEI/helper route. Vexing expedition should explicitly distinguish berry right-use from empty vine break drops and explain mixed Wilden/Mage population. Shrine optional discovery teaches Starbuncle/berries, not compulsory rare-loot gating. Item possession is proxy; manual discovery/operation acknowledgement until a real biome/structure detector is separately authored. Modest XP and expedition building supplies; don't award repeated raw-gold/Totem loops for revisiting.

Unrun positive/boundaries: extra_biomes0/1 and TerraBlender availability/new versus old chunks; actual biome sampling/climate depth versus world Y; merged spawn caps/weights/placement; Breeze bright-ground test and weather/FlashLightning; source vines/ore/formation output and light; shrine spacing/biome/support/NBT entities versus actual generated structure; native/addon structure-tag integration. No assembly/server/client/gameplay tests or runtime edits. Mage recipients, recipe reconciliation/global dependency/pilot/full profile review remain before completion.

Artifact evidence: compressed `data/ars_elemental/structure/starbuncle_shrine.nbt` SHA256 `4821650c93ed14066fbb6eed4d35cde4869e71d42722fa99d316e87d7d83e3f0`; inspected in memory, not copied into repository.
