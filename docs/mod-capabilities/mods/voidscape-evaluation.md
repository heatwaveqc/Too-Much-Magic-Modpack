# Voidscape — native source research and preliminary capability evaluation

2026-11-25. Atlas #410. Minecraft 1.21.1 NeoForge, integration baseline 017447747232a0c7f9699f3801d60776b7ba68d8. Independent documentation-only review boundary: a new mod not covered by existing evaluation PRs. This is **targeted source research, not a complete exact-JAR survey**.

## Evidence

Live pin Voidscape-1.21.1-1.9.588.jar, CF251730/8175491, SHA512 704b7358c3b961a54b7ca3ac566d976faf026464188aaab979658a208d0c4210a47dd78816dad1558702e0cd9cf4821a71c96896bd673ba0d87e49d252db6759. [Publisher release](https://www.curseforge.com/minecraft/mc-mods/voidscape/files/8175491), 2026-05-31, Astral axe/crossbow tags fixed and tool tags cleaned. [Version-aligned source](https://github.com/Tamaized/Voidscape/tree/683cd570f051a58d1091737490194e4c47ca82b9), branch 1.21 latest commit on release day, matching changelog and preceding fix 52d97fb4a3a75b886a4c62d8745e883aa54ae43c. Source tree census 112 recipe JSON paths and 25 non-recipe advancements under data/voidscape/advancement; not binary registry/recipe census. [Publisher wiki](https://voidscape.tamaized.com) is supplemental and may describe other versions. Inspected exact immutable source paths under src/main/java/tamaized/voidscape: Insanity, VoidDimensionDeathHandler, VoidicCrystalOreGenerator, OreBlocks, ModAttributes, AstralArmorSet, ModToolTiers, SpellTomeSet, SpellTomeItem, EtherealFruitItems, BlockTransformerItem, MiscItems, MaterialItems, portal activator, SpawnPointTeleporter, StrangePearlEntity, eight machine entities. No fresh JAR, effective loaded config or gameplay inspection.

## Eleven-domain evaluation

Scale 0 absent/no confirmed capability, 1 some assistance, 2 significant assistance, 3 independent, 4 superior, 5 transformative. Major 9–11 independent with personal and base breadth, Substantial 4–8, Minor 0–3.

| Domain | Score |
| --- | ---: |
| Combat | 4 |
| Protection | 4 |
| Survival | 4 |
| Gathering | 4 |
| Industry | 2 |
| Construction | 2 |
| Transportation | 3 |
| Logistics & storage | 2 |
| Life & agriculture | 4 |
| Death & recovery | 2 |
| Advancement | 5 |
| Total | 36 |
| Independent (>=3) | 7 |

Combat4 separate Voidic damage and resistance, tiered melee/ranged gear, fang augment, four spell tomes, potion aura, boss encounters. Protection4 armor tiers with infusion/paranoia/visibility/resistance attributes, Fortified tome, extra HP Astral upgrades. Survival4 growable magical fruits, Regeneration IV food, infusion/paranoia management and Defuser, Well water; no arbitrary food replacement. Gathering4 renewable Voidic crystal ore, Ether essence conversion, specialized harvesting tools and loot, not general ore-generation network. Industry2 focused crystal liquification, fluid handling, smelting/blasting, smithing upgrades and specialized transformations; no broad general material processing system. Construction2 3x3 shovel and specialized Void terrain transforms, no general builder. Transportation3 Charred Bone portal, Corrupt Tome launch/slow fall, elytra chestplate upgrades; no confirmed universal teleport or creative flight. Logistics2 Collector moves dropped items to adjacent inventory, fluid adjacency export, no general storage or routing. Life4 native Germinator bonemeal/ethereal plant generation, Coop egg production, Well water, Titanite hoe and specialized Hatchery. Death2 actual dimension-limited death interception and spawn ejection, not generally applicable outside Void; 3 would yield37/55 with8 independent, still Substantial. Advancement5 tiered gear, material/biome/encounter progress, specialized tools and augments, machinery and 25 source advancement nodes.

## Dimension, materials and progression

Entry: bedrock-standing eligibility checks dimension allow/deny list, stochastic once-per-second 1/8 checks, progress to 200, not a fixed wait. Portal code separate 200t charge, Charred Bone activation with frame predicates. Void has infusion/paranoia maximum600. Infusion lowers max HP with floor1 HP and adds native void damage up to10; resistance/visibility separate attributes. Check actual source in data/Insanity.java for rate and modifiers before teaching exact timings. Infusion recedes outside Void. Do not claim effect immunity from ordinary armor.

Voidic Crystal Ore Generator: non-spectator in Void, every30 ticks 20% chance, chooses one random position in a 64-cube (offset -32..31), replaces actual bedrock if selected. Client visual branch has paranoia condition; do not confuse with server eligibility. Ore registered TransformOnBreakBlock returns to bedrock, establishes renewable native crystal generation. Ethereal Essence converts bedrock to ore in Void; qualified non-player non-IEthereal kill by player in Void with target infusion>=200 yields essence 2/3 chance, not guaranteed mob automation. Spider eggs remove bedrock or thunder nylium only in Void. Other ores Titanite, Flesh, Strange and Cracked Astralrock require appropriate Void tools. Strange Pearl projectile can yield Astral Shards on impact under its specific conditions; not an ender teleport item. Exact resource dependencies and biome/boss drop rates still require full pass.

Five armor families Voidic Crystal, Corrupt, Titanite, Ichor, Astral; Charred is additional tool tier, not sixth armor family. Native smithing recipes include fang weapon augments, elytra chestplates (Voidic/Titanite/Ichor/Astral), Astral draconic variants. Astral pieces individually +5 voidic resistance, +.20 infusion resistance, +.25 paranoia resistance and +.30 visibility on head; optional draconic component adds5 max HP each (20 full set). Attribute caps and loaded modifiers separately verified; no universal immortality. Source registry has Null Servant, Void's Wrath, Corrupted Pawn and Voidling; full boss phase and loot audit remains outstanding.

## Spell tomes and ethereal food

SpellTomeItem: 100 durability each, cast on release after strictly more than15 ticks held, uses one durability, repair material per tome. Four recipes/source registrations:
| Tome | Effect | Cooldown | Repair |
| --- | --- | ---: | --- |
| Voidic | Aura 30s, optional lingering potion contents | 45s | Voidic Crystal |
| Titanite | Fortified 30s | 45s | Titanite Shard |
| Corrupt | Adds look-direction motion x2.5, Slow Falling10s | 5s | Tendril |
| Ichor | Ichor Bolt projectile | 10s | Ichor Crystal |

Aura checks every20t, AABB expanded2, applies potion contents to nearby living entities (self harmful effects skipped, ordinary duration quartered; instantaneous effect applied), otherwise voidic damage. Not a general spell composer or mana network. Food variants Void +150 infusion, Null -150 infusion, Overworld -150 paranoia, Nether +150 paranoia, End Regeneration IV 20s. Salad 18 nutrition, saturation modifier.5, always edible, random 11 outcomes including hazardous effects and two no-effect outcomes, not a guaranteed combination of all benefits.

## Actual native machines

Source paths block/entity/*BlockEntity.java. Liquid Voidic Crystal is typed fluid, not FE or general mana. Liquifier consumes1 crystal per80 ticks to produce250mB, capacity10000, pushes up to1000mB per eligible neighbor each20 ticks, bounded by accepted amount. Other regular fluid tanks10000. No arbitrary generic pipes demonstrated.

| Device | Operation |
| --- | --- |
| Defuser | 1mB starts100t process; each active tick decrements infusion of nearby affected entities in inflated32 AABB. |
| Infuser | 1mB starts40t; entities within inflated6 AABB with infusion below200 gain250; idle retry80t. |
| Collector | 1mB/40t attempt, finds dropped item entities in inflated6 AABB and inserts into adjacent item handlers, keeps unaccepted remainder; idle retry80t. |
| Germinator | 1mB starts60t; uses fake-player bonemeal in inflated4 AABB, can generate biome ethereal plants in Void; idle retry120t. |
| Coop | 1mB starts60–199t, creates one ordinary egg without chicken, exports adjacent inventories, blocked retry200t. |
| Hatchery | 100000mB accumulated, replaces device with vanilla dragon egg; not mob breeding or a generic spawner. |
| Well | Water tank Integer.MAX_VALUE refilled automatically; pushes water to adjacent handlers, not a finite reservoir. |
| Liquifier | Crystal→liquid feed as above. |

Redstone pauses checked machines including Coop/Collector. Actual accept/claim, chunk, recipe/automation, infinite water and Dragon Egg economy/capture policy separate; no new balance permission. The Hatchery is an unusual native alternate Dragon Egg acquisition, relevant to progression restrictions; flag for gate review, not disable. No general item network. Native machines distinguish Voidscape from pure equipment adventure mods.

## Death handling

[VoidDimensionDeathHandler](https://github.com/Tamaized/Voidscape/blob/683cd570f051a58d1091737490194e4c47ca82b9/src/main/java/tamaized/voidscape/event/VoidDimensionDeathHandler.java) highest-priority LivingDeathEvent for ServerPlayer in Void dimension: set health to10% current max, remove milk-curable effects, reset fall distance; obtain spawn level or Overworld fallback, change dimension via SpawnPointTeleporter (player findRespawnPositionAndUseSpawnBlock), cancel event when destination present. No inventory death-drop path on canceled death. Not universal resurrection; post-ejection survival not guaranteed and other mod handlers may interact. Score2 because restricted to one dimension, no generalized death save established. Hardcore, keep-inventory, bed/anchor, death mods and progression event ordering separate runtime concerns.

## Remaining work and handoff

Source-based assessment only, no full exact-release binary survey. Need exact JAR census and 29-axis crosswalk, complete armor/tool/module stats and acquisition, recipe/portal predicates, dimension/biome data, boss encounters/drops, machine/advancement detection and quest teaching map. No new quest chapter/config/recipe/balance/runtime or build changes; no shared index edits. Integration writer review after quick checks. Next combined Relics/Artifacts/Reliquified Artifacts, then Dragon Survival and Improvable Skills.
