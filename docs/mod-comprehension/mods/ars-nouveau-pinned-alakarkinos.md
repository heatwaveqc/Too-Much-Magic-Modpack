# Ars Nouveau pinned Alakarkinos archaeology worker

Exact `ars_nouveau-1.21.1-5.13.5.jar`, SHA1 `cdc946c63e1eb676f0e29e4640335c0ce68d7de3`. Direct recipe/biome/locale resources, CFR0.152 and selected `javap -c -p` checks. Evidence classes: Alakarkinos, AlakarkinosCharm, AbstractSummonCharm, all seven `entity/statemachine/alakarkinos` classes, AlakarkinosConversionRegistry, AlakarkinosRecipe, Config, Documentation and CrabHat. [Parent](ars-nouveau.md), [Source infrastructure](ars-nouveau-pinned-source.md), [Apparatus construction](ars-nouveau-pinned-crafting.md). Native operation only; no live spawn/pathfinding/loot/protection acceptance.

## Why and when to build it

This optional worker consumes placed ordinary Sand or Gravel and rolls archaeology loot tables, delivering results into a bound inventory. It can make a renewable archaeology loop when sand/gravel and Source are supplied; it is not a general block miner, item-input processor or a guarantee of any rare result. The first Sherd still needs an independent acquisition route. Exact loaded loot tables and progression-sensitive drops need pack review before quest/reward approval.

Native Notebook **Crafting -> Alakarkinos Charm**, including **Summoning** and **Sifting** pages, plus connected Dominion Wand/Source Jar pages. JEI **Alakarkinos Conversion** shows recipes and possible loot; its chance display is distinct from actual runtime rolls. Native guide and home-set message say three-block work range; actual FindBlockState below scans a larger region.

## Acquisition, deployment and recovery

1. Find wild `ars_nouveau:alakarkinos`. Native biome modifier `neoforge/biome_modifier/alakarkinos_spawn.json` adds weight5, min/max group1 in `#minecraft:is_beach`. This is spawn-list evidence, not a measured encounter frequency or proof all pack beaches are eligible.
2. Drop an item in `minecraft:decorated_pot_sherds` beside the wild crab. Untamed goals seek the item; nearby item scan runs every40 game ticks within bounding box inflated1. Pickup copies one into its hand and shrinks the dropped stack by one. After holding that Sherd for **more than60** ticks, server drops one `ars_nouveau:alakarkinos_token` and discards the wild entity. This is a one-entity exchange, not repeated gifts from the same persistent tame pet. No direct player-hand feeding override appears in inspected Alakarkinos class.
3. Apparatus recipe `alakarkinos_charm`: Token reagent; pedestal inputs one Brush plus three Sherd-tag items; SourceCost0, keepNbtOfReagent=false; output one Charm. Total route needs the donated Sherd plus three recipe Sherds and the Brush/Token. JEI supplies effective tag alternatives.
4. Use Charm on a block to spawn a tamed worker above it. Parent AbstractSummonCharm shrinks one on SUCCESS except infinite-material players; client predicts success, server handles spawn. SummoningTile branch delegates the same placement. It is a worker charm, not one of the six personal familiar bonuses.
5. Bind the crab to a chest/inventory with Dominion Wand (crab first, target inventory last). `onFinishedConnectionFirst` accepts target only if a null-side item handler exists and saves HOME. This stores a position, not a dimension-aware remote address. Keep worker, chest, work blocks and Source in one accessible local area.
6. Provide room around the chest, reachable placed Sand/Gravel and nearby Source. Keep output space free. Dispel on a tamed crab drops Charm and discards it; tamed death also emits Charm with persistent familiar data. Inspected serialization restores custom name; do not promise chest/home, current job, hat or prepaid-state preservation through Charm. `clear_alakarkinos_charm` shapeless recrafts a fresh Charm, intentionally removing carried custom data.

## Exact job and payment sequence

| Phase | Native contract |
| --- | --- |
| Decide | Requires tamed and HOME present. If hat absent, retrieves hat first. NEED_SOURCE initially false. When true, attempts configured Source debit every20 world ticks from range5 around HOME; native `alakarkinosSourceCost` default1000, allowed0..10000. Successful debit clears flag. Only then starts a new search when cooldown<=0. |
| Find | `withinManhattan(home,5,3,5)` covers X/Z offsets up to5 and Y up to3, ordered by Manhattan distance. Requires registry-convertible block and a nonnull reachable navigation path. No match sets retry cooldown100+random[0,99]. This differs from native prose's horizontal3/vertical1; the smaller region is a safe teaching setup. |
| Place hat | Searches replaceable, noncolliding positions around home within3 horizontal/1 vertical. Navigates toward chosen site; within2 or after200 running ticks begins20-tick animation delay. Places `alakarkinos_hat`, records position and clears HAS_HAT. Hat block has no loot table. If no valid hat site, repeatedly searches; the inspected `placeTries` field is never incremented, so its >4 guard does not establish a finite retry budget. Keep space clear. |
| Convert | Approaches target within2, but after200 running ticks proceeds without that distance requirement. Blows bubbles for60 wait ticks, rechecks current target block against conversion registry, selects weighted recipe. On success emits a flying visual, **sets target block to AIR**, sets NEED_SOURCE=true and waits another60. Does not first require/debit Source for this selected job. |
| Payout | Must still have hat position and home. Navigates within2 of hat, then blows bubbles another60 wait ticks. Sets findBlockCooldown1200 before generating loot. Rolls chosen loot table, inserts stacks into HOME handler; returns to Decide. |
| Next cycle | Retrieves hat when HAS_HAT false (within1.5 for18 ticks, then clears hat block), and pays pending Source before next search. Cooldown counts down in server tick; it can overlap other work/retrieval. |

The initial cycle can consume a block and produce loot with no Source because NEED_SOURCE starts false; payment gates a subsequent search, not the completed payout. Later1000Source is a recharge debt. Do not describe every output as atomically prepaid. Dispel/death/new deployment state may affect this balance; no exploit approval or effective pack enforcement is established.

Conversion does not call ordinary block break/harvest APIs in these methods; it directly replaces with AIR. Once removed, no rollback/replant branch exists here if crab, home or hat is lost before payout. A target changed before registry selection can cancel the job without consuming that new unsupported block. Hat loss/state interruption and save/reload need acceptance. Animation timing is not measured throughput: navigation, waits, cooldown, chunk activity and output conditions affect cycles.

## All six native conversions

All resources under `data/ars_nouveau/recipe/alakarkinos/`; type `ars_nouveau:alakarkinos_conversion`. Registry groups by exact input block and chooses a weighted recipe on each successful conversion. These are table-selection weights, not per-item odds.

| Recipe basename | Input block | Loot table | Weight / total for input |
| --- | --- | --- | --- |
| desert_well | minecraft:sand | minecraft:archaeology/desert_well | 100/150 |
| desert_pyramid | minecraft:sand | minecraft:archaeology/desert_pyramid | 25/150 |
| ocean_ruins_warm | minecraft:sand | minecraft:archaeology/ocean_ruin_warm | 25/150 |
| trail_ruins_common | minecraft:gravel | minecraft:archaeology/trail_ruins_common | 100/150 |
| trail_ruins_rare | minecraft:gravel | minecraft:archaeology/trail_ruins_rare | 25/150 |
| ocean_ruins_cold | minecraft:gravel | minecraft:archaeology/ocean_ruin_cold | 25/150 |

Actual `SpawnLootState.getLoot` resolves table from server reloadable registries, builds **CHEST** loot parameters with crab position as ORIGIN and ANFakePlayer as THIS_ENTITY, and calls getRandomItems. It does not brush a suspicious block, require a particular archaeology structure location, or use the consumed block's drops. Addons/datapacks can change registry recipes/table contents; six entries describe only native JAR. All table outputs, rarity modifiers, conditions and protected-material implications must be audited in the effective pack before endorsing farm outputs. Display cache `LootDrop.computeLootDrops` normalizes expected item drops; that viewer calculation is not the runtime sampling algorithm.

**Output failure:** handler insertion remainder is ignored (`insertItemStacked` return followed by bytecode `pop`). No preflight capacity check in this payout; full/partially full output can lose leftovers after block consumption and cooldown assignment. Flying item is a visual path, not evidence all shown loot reached storage. Missing HOME capability is not explicitly guarded in this body; don't promise graceful buffer/retry recovery. Provide free storage and verify counted inventory delta.

## Quest and acceptance handoff

Optional progression after Apparatus and Dominion Wand: obtain Token -> craft/deploy Charm -> bind inventory -> place one Sand/Gravel -> observe block removal and inventory loot -> supply Source and demonstrate a subsequent cycle. Notebook Crafting/Alakarkinos and JEI conversion category are the teaching references. Item tasks can detect Token/Charm/loot possession; they cannot prove feeding, binding, Source payment or actual sifting. Use explicit manual observation or a separately implemented operation detector. Reward XP and modest ordinary building/crafting supplies; do not reward rare archaeology results or another Charm as an operation proxy.

Unrun disposable-world acceptance:

- Positive: one home chest with space, accessible hat site, one Sand within3/1, default config and1000Source range5. Record Sand -1, actual output stacks, Source before/after first and second job, hat lifecycle and cooldown. Repeat with Gravel; identify selected table only from instrumented evidence, not an arbitrary rare drop.
- Payment boundary: empty Source initially; first job versus second-job stall, then recharge. Confirm no claim of up-front payment. Compare farther work blocks at offset4/5 to native guide's smaller recommendation and unreachable path rejection.
- Failure boundary: full and partially full chest, removed chest, target changed during bubbles, blocked hat site, worker Dispel/death/chunk reload mid-job. Count destroyed input, retained/lost loot and pending payment state.
- Economy/protection: inspect effective recipe registry and six resolved archaeology tables; test protected block/conversion behavior only in authorized disposable world. No guaranteed rare item, claims compatibility or progression bypass is asserted.
