# Ars Nouveau pinned Whirlisprig grove worker

Exact `ars_nouveau-1.21.1-5.13.5.jar`, SHA-1 `cdc946c63e1eb676f0e29e4640335c0ce68d7de3`; JSON/CFR0.152. Evidence: `Whirlisprig`, `WhirlisprigCharm`, `WhirlisprigTile`, `InspectPlantGoal`, `BonemealGoal`, `EventHandler.treeGrow`, `Config`, recipe/tag JSON. [Parent](ars-nouveau.md), [Source supply](ars-nouveau-pinned-source.md), [Wixie/shared charm use](ars-nouveau-pinned-wixie.md). **Consumes Source to generate plant-associated item drops; not a Source generator.** No in-pack rate/eligibility acceptance.

## Acquire, convert a flower, build a grove

Use native Notebook **Crafting → Whirlisprig Charm**, related grove/creature entries and JEI. Find a wild Whirlisprig; successfully grow a tree within distance helper<=10. EventHandler tree-growth listener marks untamed registered Whirlisprigs droppingShards; merely planting a sapling is not sufficient. After tamingTime>60server ticks it spawns exactly one shard (`1+nextInt(1)`), then discards itself. All eligible nearby wild ones can be marked by that event; this is not a gold-nugget trade.

Exact `recipe/whirlisprig_charm.json` Enchanting Apparatus: `whirlisprig_shards` reagent; pedestal inputs Source-gem tag, `ars_nouveau:magebloom_crop`, `ars_nouveau:magebloom`, Diamond-gem tag, Oak/Spruce/Birch Saplings, Wheat-seed tag; SourceCost0. Those are distinct registered Magebloom inputs, not two interchangeable flower icons. Apply the charm to a block in vanilla FLOWERS tag to create Whirlisprig Flower; successful survival use spends one charm. Initial station conversion after120ticks spawns the worker. Additional charm on the station spawns another without an explicit existing-worker guard in this method; no maximum worker count or safe linear-speed promise is inferred.

Build a mixed grove around the flower, with accessible plants/logs/leaves and adjacent output storage. Put a replenished Source Jar nearby; worker consumes a configurable batch cost, default250Source, when ready to generate. Keep paths open and inspect actual output. An attractive grove, hearts or a charm in inventory are not proof that generation occurred.

## Habitat, progress and output

| Stage | Exact native contract |
| --- | --- |
| Grove scan | X/Z±10, Y-1 throughY+30 around flower, initially and then every2400tile ticks. Points use each block's **default state**; generation table uses actual state and excludes block entities from count. |
| Scoring | Water/Grass Block/Podzol/Dirt Path1; BushBlock2; harvest-stems tag2; Logs2; Leaves1; other Bonemealable1; kinda_likes tag1; greatly_likes tag2. First matching branch wins. Score contribution stops after50 of the same default state; diversity is number of distinct scored default states. Uninteresting blocks add0. |
| Progress | InspectPlantGoal searches local±4 around worker for scored block with an exposed air side and within home restriction15. On goal start, resets timeSinceGen and immediately adds floor(moodScore/30), capped at configured progress maximum(default250). Worker then observes/moves; this is not a fixed Source-per-crop tick. If mood<30, addition0. |
| Triggering inspection | Requires flower not powered; after timeSinceGen>300 can attempt, otherwise random check nextInt(100)<=2 plus10tick boundary. Pathing/other goals affect actual work. Multiple workers share a station's progress; total rate not measured. |
| Batch payment | Every60tick world boundary, if progress>=configured maximum and SourceUtil range5 can pay configured cost(default250), resets progress0 **before** generating. Defaults are server config `sylphManaCost=250`, `whirlisprigProgress=250`, each allowed0..10000. Effective server config unverified. |
| Batch size | floor(diversityScore/2)+3 drop attempts. A preliminary random state can be null and abort after payment/reset. Each getDrops chooses from weighted actual-state table, generates native block loot at flower position with no block entity, filters prohibited/ignored stacks, and rerolls up to8 initially/16 after nonempty candidate drops. Count is attempts, not guaranteed distinct stacks/items. |
| Delivery | Calls BlockUtil.insertItemAdjacent for resulting stacks. BlockUtil.insertItemAdjacent iterates adjacent inventories, returns uninserted remainder, and does not drop it. WhirlisprigTile ignores that return, so full/missing/rejecting storage can lose produced items; target inventory behavior still needs runtime acceptance. It does not destroy the original grove block in this loop. No general rare/protected modded-plant duplication approval. |

The outer output loop makes one preliminary random draw, then getDrops draws again; do not claim the preliminary state is necessarily the one harvested. Mature vs immature actual states can change loot. Some eligible scored blocks have no useful drops, and block-entity content is not reproduced. Crop maturity, loot conditions, effective tags and permissions must be inspected for each consequential target before promising a renewable loop.

Native `tags/item/whirlisprig/denied_drop.json`: Dirt and `#c:seeds`. Per-worker interaction with Deny Item Scroll appends scroll items to station ignoreItems, comparing item identity. A filter excludes output; it does not turn the block into another desired drop or guarantee exactly one preferred item. Native denied tag and other datapack contributions remain separate.

## Control, optional bonemeal and recovery

Holding a block while querying creature can report whether it likes the block and whether>=50 already exist; empty-hand/status controls report mood/diversity and ignored outputs. This feedback is useful for teaching variety over endless duplicate logs, while the actual scan still updates on its2400tick schedule.

Tamed goals include Bonemeal(home10), InspectPlant(home15) and return-home20. Bonemeal waits timeSinceBonemeal>=9600, random1/5 attempt, seeks Grass Block with air above and after60nearby ticks applies internally created Bone Meal, resetting timer. This is an occasional terrain-growth action; no assumption it emits Agronomic CropGrowEvent.Post or pays for its own Source. Source-driven plant-drop output and any Agronomic event feedback are two-sided interaction candidates requiring both event/quantity checks.

Station power blocks new InspectPlant starts; output tile's60tick generation branch has no isOff check. Stored full progress can therefore still lead to a batch even while powered. BonemealGoal also lacks a direct station-isOff gate. Do not teach power as immediate total stop/refund. Missing home tile causes tamed worker damage on60tick checks; Dispel of a living tamed worker emits charm with persistent familiar data and removes worker. Dispel does not prove station/filter/progress are packed into that charm.

Station saves score/diversity/progress/evaluation countdown and ignored items, but genTable/scoreMap are reconstructed by evaluation rather than serialized. Because countdown is saved, reload can resume with empty tables until reevaluation; test paid generation before that interval rather than claim lossless immediate resume. Worker gen timer and home coordinates have separate saves.

## Quest and boundary handoff

Teach tree-growth befriending → apparatus charm → flower station → diverse accessible grove → Source plus output storage → observed batch → filter/control. Each quest needs named Notebook/JEI references and safe XP plus ordinary grove supplies. Charm/station/item-possession tasks are acquisition proxies; manual observed batch or a confirmed native operation trigger is needed to credit actual work. A dirt/seed filter quest should not reward a protected plant or substitute for a real production gate.

Positive: one valid tree beside wild worker gives shard; valid flower converts; varied grove reaches progress and250Source batch produces eligible items. Boundaries: distant/no-success tree, unscored/repeated-only grove, immature crops, denied/ignored outputs, no Source, no/full output inventory, powered full-progress station, multiple workers, unloaded/reloaded grove, and a second player collecting output. Check debit, progress reset and delivered stacks independently. Parent remains researching; Bookwyrm/Starbuncle and other native systems/addons remain open.
