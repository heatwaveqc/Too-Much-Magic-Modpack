# Ars Nouveau pinned plants and first material acquisition

Exact5.13.5 JAR SHA1 `cdc946c63e1eb676f0e29e4640335c0ce68d7de3`. Native worldgen/biome modifiers/loot/recipes/tags, MageBloomCrop/SourceBerryBush/ArchfruitPod, MagicTree/MagicTrunkPlacer, ItemsRegistry/BlockRegistry/Documentation, CFR0.152. [Parent](ars-nouveau.md), [Source production](ars-nouveau-pinned-source.md), [potion uses/effects](ars-nouveau-pinned-potion-effects.md), [plant worker](ars-nouveau-pinned-whirlisprig.md), [berry worker](ars-nouveau-pinned-starbuncle.md). Native generation configuration isn't observed world distribution or measured growth/harvest rate. No worldgen/client/farming acceptance.

## Find and propagate Archwood

Notebook **Field Guide -> Archwood Trees**, including four fruit sections and their brewing uses; **Sourceberry Bush**, **Magebloom** entries in same resource/field-guide category. JEI gives recipes/uses. Native rare_archwood_mix biome modifier adds mixed tree feature to `#minecraft:is_overworld`, vegetal_decoration. Placed mixed feature uses rarity_filter chance100, in_square, max_water_depth0, OCEAN_FLOOR heightmap and biome filter. These are generator parameters, not a literal one-tree-per100chunks guarantee. Existing chunks don't gain new trees merely because a recipe was researched.

Mixed selector chooses four placed types. Each placed tree references corresponding **natural** configured feature and sapling-survival predicate. Find ordinary accessible overworld trees or native Archwood Forest; worldgen mods/effective biome lists can change availability. Collect wood and leaf-derived saplings, and **look for pods attached to natural trunks before chopping all supports**.

| Tree label / log and sapling color | Natural pod | Required support tag in ArchfruitPod registration | Brewing family |
| --- | --- | --- | --- |
| Flourishing /green | mendosteen_pod | flourishing logs | recovery |
| Vexing /purple | bastion_pod | vexing logs | shielding |
| Cascading /blue | frostaya_pod | cascading logs | freezing |
| Blazing /red | bombegranate_pod | blazing logs | blasting |

All items/logs use `ars_nouveau` namespace; ordinary sapling IDs `<color>_archwood_sapling`. Leaf loot has Shears/Silk Touch leaf-block branch; otherwise sapling chances by Fortune table0.05/0.0625/0.083333336/0.1 plus separate stick branch. Collect a sapling as a reusable wood source; no deterministic sapling-per-leaf claim.

Important first-pod gate: natural trunk configs set isWorldGen=true, sapling tree configs false. MagicTrunkPlacer adds pods only in worldgen branch, selected interior trunk levels (i>=2and i<foliageHeight-2), four corner calls, each random<=0.07then random eligible face and age0..2, only adjacent air. **Growing more saplings doesn't establish renewable first-pod generation.** Pod probability is per selected placement call, not per whole tree. Natural configs use exact podIDs above; no recipe creating first pod from wood was found in audited pod recipe set.

Once acquired, place pod against matching log-tag support, horizontal facing toward support as Cocoa-style plant. ArchfruitPod inherits CocoaBlock growth/bonemeal behavior; doesn't implement custom randomTick override. Native pod loot yields one at immature age, **three when age2**, so grow/replant/harvest can propagate stock. Preserve at least one planting item. Branch doesn't require same original tree or forest biome; support tag is checked. Spell Light can set pod light level clamp(8-DampenCount,0,8); naturally generated age0..2pods start light6+age. Light is separate from growth/food potency.

## Sourceberries: find, harvest and use without confusing Source

`common_source_berry` biome modifier selects `#ars_nouveau:berry_spawn`: native values `#minecraft:is_taiga` and `ars_nouveau:archwood_forest`. placed_berry_bush rarity32; patch_berry makes age3bush in air above Grass Block,96tries/XZspread7/Yspread3. Configuration doesn't prove encounter in a specific seed or every taiga variant after effective tag changes.

Harvest age2or3by ordinary use: age2drops1..2, age3drops2..3 of **sourceberry_bush item**, resets age1. This item serves as planting material, edible berry and Mana Regeneration brewing ingredient; no separately assumed `sourceberry` item ID. Bone Meal on not-fully-grown bush passes to default bonemeal action, increments age by1cap3; mature harvest instead. Native randomTick grows age<3when brightness above>=9and canCropGrow accepts random1/5attempt, then fires CropGrowPost. Bone Meal handler doesn't fire that same growth-post callback; Agronomic Source requires its audited event route, not “every growth interaction makes Source.”

Living entities not in BERRY_BLACKLIST are slowed; server age>0and horizontal movement>=0.003can cause1source-berry-bush damage. Don't lay unprotected berry work rows across player walkways. Starbuncle path treats bush walkable and can harvest as companion describes. Sourceberry food grants **personal Mana Regeneration**, not a packet of Source Jar energy. Agronomic generation responds separately to crop growth; Mycelial can consume supported food via different economics.

## Magebloom: craft first seed, farm fiber

First `magebloom_crop` item is Apparatus result: any `c:seeds` reagent +four `c:gems/source` pedestal ingredients, SourceCost0, keepNbt=false. This requires Apparatus, not a naturally found seed assumption. Plant crop as inherited CropBlock on suitable farmland and grow to mature stage. Class declares AGE_0_4 constant but does not override CropBlock age property/max-age; native loot explicitly checks **age7**. Don't teach maturity4from unused constant.

Mature block loot yields Magebloom plus seed-item pool with Fortune binomial bonus; immature alternative returns seed item. Actual effective harvest interaction/addons need acceptance. Magebloom shapeless ->four Magebloom Fiber; four Fiber in2x2 ->Magebloom Block; `magebloom_block_to_magebloom` name is misleading: output is **four Fiber**, not flower. Two Mageblooms ->two Pink Dye. Plant->flower->fiber supports Blank Parchment/armor; flower itself brews Spell Damage. Keep seed distinct from edible/crafting flower and fiber.

Magebloom growth participates in Agronomic crop economy by its event/tag configuration; no rate measured here. Whirlisprig generated plant drops and ordinary crop harvesting are distinct inputs/outputs; don't infer worker cloning includes every effective seed/tag without its companion's eligibility rules.

## Native food effects and consequences

ItemsRegistry food declarations are application channels in addition to brewed bottles. All listed alwaysEdible; this does not override pack hunger/diet/faction policy.

| Edible | Nutrition / saturationModifier | Guaranteed declared effect |
| --- | --- | --- |
| Sourceberry Bush item | 2 /0.1 | Mana Regeneration100ticks amp0 |
| Mendosteen | 4 /0.6 | Recovery1200ticks amp0 |
| Bastion | 4 /0.6 | Shielding1200ticks amp0 |
| Frostaya | 4 /0.6 | Freezing600ticks amp0 |
| Bombegranate | 4 /0.6 | Blasting200ticks amp0 |

Frostaya/Bombegranate are **not uniformly beneficial healing fruit**: freezing accumulates frozen state, Blasting explodes on last remaining tick with entity-damage implications. Recovery modifies an existing heal event, not periodic regeneration. Consult effect companion before suggesting food rewards or group farms. SaturationModifier isn't literal saturation restored by itself. No permission for indefinite rare consumables, diet exemptions or safe explosion use follows from food declarations.

## Walkthrough and acceptance handoff

Native resource spine: Field Guide -> find Archwood/Sourceberries -> collect sapling and first pods -> replant supported pods -> seed Apparatus -> mature Magebloom -> fiber/parchment/armor -> chosen brewing branch. Optional ornamental/dye/light uses don't replace core crafting/food distinctions. Teach actual material ID and guide/JEI reference at each step; no external wiki needed for these first acquisitions.

FTB item tasks detect materials only, not natural-tree discovery, planting/growth/harvest event or successful Source production. Use manual observation or real detector; XP and ordinary farming supplies are suitable reward direction. Don't reward first required pod/seed before its acquisition lesson unless explicit self-resource exception approved.

Unrun boundaries: actual biome/feature availability in current seed; natural vs sapling-grown pod generation; four support tags, immature/mature drops and replant multiplication; berries age2/3, brightness/bonemeal vs crop event, entity damage and Starbuncle harvest; Magebloom age7and seed/fiber/block conversion; food effect/potion differences, full mana, hunger/diet rules. Effective tags/worldgen/data overrides and actual growth rates remain separate from native source evidence.
