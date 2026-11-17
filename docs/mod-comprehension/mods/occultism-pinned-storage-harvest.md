# Occultism — pinned storage access and harvesting hooks

Companion to [main progression/storage setup](occultism.md), [ritual recipes](occultism-pinned-rituals.md), [processing](occultism-pinned-processing.md) and [familiars](occultism-pinned-familiars-tools.md). Exact1.227.0 SHA512 artifact in those references, inspected2026-11-16. **Native contracts, no client/claims/multiplayer acceptance.**

## Storage setup and limits

Follow Dictionary **Storage** and the named Matrix/Controller Base/Stabilizer/Remote entries; the ritual appendix lists bound-spirit ingredients. Craft Dimensional Matrix, create Controller Base via proper binding, assemble controller, open it and insert/retrieve a mundane test stack. This is a magic-native storage system, not permission to add generic external logistics. Teach distinct item-type and total-count limits.

Native config constructor: controller128 types/256000 total items. Stabilizer additions by tier are64/512000,128/1024000,256/2048000,512/4096000,1024/8192000. Counts are total across all types, not capacity per type. Stabilized-controller variant uses configured8 tier5 equivalents (native8320 types/65792000 total), whereas ordinary controller sums discovered physical stabilizers. Effective TMM overrides remain untested.

`findValidStabilizers` starts **one block above controller**, traces each of six directions out5, and accepts first StorageStabilizerBlock only when facing back toward search direction. `Math3DUtil.simpleTrace` includes starting position and does not test intervening opaque blocks: this is not an unobstructed-air line-of-sight ray. A first wrongly facing stabilizer can hide a farther correctly oriented one because facing is tested after first hit selection. Use native guide preview plus actual linked-capacity feedback; no arbitrary circle layout implied.

Controller first server tick initializes stabilizers. Limits setter delegates max item types/count to its handler and syncs; storage contents and matrix are saved separately. Removing upgrades/capacity reduction and breaking/replacing controller need live tests before trusting valuable items to it. Displayed capacity is not proof a specific third-party insertion/storage proxy respects it.

## Remote linking and access

`StorageRemoteItem.useOn` links **any use-on a block entity implementing IStorageController**; there is no sneaking condition or owner check in this method. Stores GlobalBlockPos and sets Rare display rarity. Mere remote possession does not prove link or access.

Server use resolves linked server dimension and tests hasChunkAt; unloaded target displays not-loaded rather than deliberately loading it. It then tests IStorageController and opens menu for ServerPlayer. No UUID/team authorization appears in these two item methods; external claim/menu/container protections require acceptance. A shared/transferred linked remote is not proven private storage access control. Missing target dimension has no null guard immediately before hasChunkAt in this path, so stale-dimension links need a boundary test.

Menu creation uses `CuriosUtil.getStorageRemote`: main selected slot first, then equipped Curios remote, then first remote inventory slot; fallback expression accepts only slot>0. Offhand/current-hand use can therefore select another remote than expected. Teach the linked remote in main hand for initial test and compare multiple remote links before recommending offhand/Curios use. This is a selector finding, not a tested theft/loss claim.

Native storage manipulation, machine-manager jobs and the Neo Vitae/Ars bridges must be traced from both sides before describing automation/permissions as compatible. Main profile's source-based setup remains useful; this appendix closes these exact pinned item/controller methods, not every GUI transaction.

## Harvesting, loot and familiar interactions

- `OtherworldPickaxeItem` and `InfusedPickaxeItem` both return supernatural harvest tierTWO. In `LootEventHandler.breakSpecialBlocks`, a noncreative Player breaking a block tagged OTHERWORLD_COLLECTS with held Iesnium/Infused pickaxe or detected staff gets one BlockItem spawn, block replaced with Air and BreakEvent canceled by this handler. Block NBT is not copied; direct state writes/spawn booleans and event ordering need protection tests. This is not a general Silk Touch on every block.
- `playerHead` can spawn victim Player Head with profile when source weapon matches Iesnium knife tag and death not canceled. Player-attributed damage vs source weapon is a different gate from familiar lifesteal. No repeatable-player farm or faction exception is authorized.
- Fracture Soul on killer LivingEntity's weapon can add Shattered Soul to LivingDrops with chance=enchantment level×configured chance. Excludes soul-shattered deny tag, IFamiliar and Player; requires encode ID. Saved payload temporarily restores victim health/clears effects for serialization. Actual boss blacklist/tag and revivable output require live loaded-tag/ritual audit; encoded entity payload is not automatically an allowed boss respawn.
- **Dragon Greed closure:** LootEventHandler only adds amplifier+1 to a nonzero experience drop with attackingPlayer carrying effect. It does not multiply ordinary item loot in this inspected handler. Thus familiar effect's name is not a guaranteed ore/boss-drop multiplier.
- **Blacksmith repair closure:** enabled nearby/equipped Blacksmith plus pickup of stone/cobblestone tag can repair the first damaged equipped/held slot by2 with probability configured0.33×ItemEntity stack count. Regardless of repair success, the event denies pickup and discards **the whole stone ItemEntity**. More stones do not give one repair per unit; full stack can be consumed for one attempt and no repair when nothing is damaged. Keep valuable building stacks away when testing.

## Quest and boundary handoff (unrun)

Storage: actual mundane insertion/retrieval, base vs one correctly facing stabilizer, obstruction vs wrong-facing nearer stabilizer, missing chunk/dimension, full capacity/reduced capacity, multiple remote main/offhand/Curios links, second player/claim and proxy. Use possession as craft proxy only; source-specific counter/manual observed operation if no detector. XP+ordinary building/ritual materials, avoiding a complete advanced controller reward.

Harvesting: ordinary vs OTHERWORLD_COLLECTS-tag block, exact tool vs wrong tier/staff, claimed block/denied BreakEvent, unexpected block entity and rejected spawn. Familiar resources: greed XP0/nonzero, repair stack1/64/no damaged slots/disabled setting. Soul: denied entity/player/familiar vs ordinary allowed mob, canceled death/drops, Fracture level/chance and actually reconstructed result. No in-world tests performed.

Evidence: `StorageControllerBlockEntity`, `StorageRemoteItem`, `OccultismServerConfig$StorageSettings`, `Math3DUtil/CuriosUtil`, `LootEventHandler`, `OtherworldPickaxeItem/InfusedPickaxeItem`, native tags referenced by hooks. Remaining exact helper/familiar AI, Otherworld block visibility/loot conditions and full storage transaction surface remain explicit research gaps.
