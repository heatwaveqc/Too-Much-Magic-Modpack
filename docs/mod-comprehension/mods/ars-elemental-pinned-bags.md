# Ars Elemental 0.7.10.4 — Trinkets Pouch and Spellcaster Bag

Exact addon SHA1 `562f640916aa6f5c83b3b3ee2384ca828b870e01`; [receipt](ars-elemental-pinned-guide-urn.md). CurioHolder/CasterHolder, both menu classes/CurioHolderScreen, OpenCurioBagPacket, ModRegistry menu factories, ArsElemental item capabilities, ClientEvents, registration, packaged recipes/Curios data/blacklist; exact native Ars5.13.5 Apparatus assemble and DyeRecipe inspected. Native inventories only; no new inventory mod/pack change. No GUI/network/storage tests.

## Make, open and upgrade

Read Worn Notebook **Items → Trinkets Pouch**, which includes both recipes; JEI crafting/Apparatus/dye references. Grid: vanilla Chest surrounded by **eight Magebloom Fiber** gives one `ars_elemental:curio_bag`. Native Magebloom farming is the prerequisite; no rare boss gate in this grid. Item is unstackable/fire-resistant by registration, with a **27-slot** item-handler inventory.

Use item in hand to open server menu. It cannot equip-from-use; manually put in available Curios **bundle** slot or leave in hotbar. Packaged player Curios data adds bundle size1, and `curios:bundle` tag accepts both bags plus vanilla Bundle. This is a packaged contribution, not a verified final merged UI layout.

Configure **Open Pouch** control in Minecraft key options; artifact's KeyMapping is keycode74 (J), while TMM may leave bindings unbound/overridden. Do not require J as pack default. Hotkey helper searches worn Curios **first**, then nine hotbar slots starting at selected slot and wrapping; it accepts any CurioHolder subtype, including Spellcaster Bag. Main inventory beyond hotbar is not searched. It opens the first found item, not every bag or a menu choosing among bags. Tooltip displays configured binding. Client avoids reopening while current screen is CurioHolderScreen; packet rechecks selected bag server-side.

Upgrade Apparatus consumes pouch reagent + **two Manipulation Essences + two Blaze Powder + two Gold Blocks**, Source0, gives one `ars_elemental:caster_bag`. keepNbtOfReagent=true: native assemble applies reagent's component patch to new item, including CONTAINER data, then resets damage if present. Thus contents have an explicit preservation route when expanding to **54slots**, not just a matching-name assertion. It is an optional size upgrade with Gold/Nether material burden; not automatic casting power.

Caster bag defaults red BASE_COLOR and implements native IDyeable. Packaged shapeless dye recipe uses one caster bag + dye-tag item. Native DyeRecipe assembles result, copies components from matching input item, then sets BASE_COLOR from recognized dye. Contents/name component preservation is source-backed; check actual result before transferring valuable contents. No shrink/upgrade recipe is established here.

## What it stores and what it does not equip

Both attach **plain NeoForge ComponentItemHandler**, backed by stack DataComponents.CONTAINER:27/54slots. Menu storage slots use ItemHandlerCopySlot, mayPlace checks `!#ars_elemental:blacklist_bag_item` and item.canFitInsideContainerItems. Packaged blacklist: **both addon bags and vanilla Bundle**. Despite names, this GUI predicate does not restrict contents to curios/caster items. No ability passives/casting-from-contents is implemented by these bag/menu classes; stored bangle is not automatically worn.

The blacklist is checked in **menu slots**, not a custom registered handler subclass. Direct capability insertion exposes the plain ComponentItemHandler, so equivalent restrictions cannot be assumed for another mod's handler caller without tracing that API. No nesting/automation exploit is endorsed or tested. Stored inventory follows item components, not a linked placed chest, Source buffer, dimensional address or item-transport network. There is no Source/XP/durability cost for normal open/close in these methods.

Menu builds3 or6rows of9 plus player inventory; quickMoveStack moves between handler and player ranges. Player slots containing any same bag item type refuse mayPickup, not just exact opened stack, reducing normal menu movement while open. stillValid returns true; not an ownership/distance revalidation check. Menu close plays sound. Item's re-equip animation check compares item identity only, allowing component changes without forced animation; not a delivery/persistence guarantee.

## Client/server slot-index boundaries

Direct use opens server menu with actual held stack, but writes selected hotbar index for main hand and **literal1 for offhand**. Client menu factory resolves nonnegative index via player Inventory.getItem(index), then falls back to a fresh default bag when item/type does not match. Literal1 is not the offhand slot address; this can create a different client source stack/layout from actual server-held bag. Prefer main-hand tutorial and test offhand behavior; do not infer duplication or item loss without an actual session.

Hotkey packet uses worn/hotbar SlotReference and writes−1 only if returned handler is an ICurioStacksHandler; helper obtains flattened CuriosUtil.getAllWornItems. Exact wrapper type and multi-bag ordering determine whether worn path gets correct client index. Negative client lookup picks first matching CurioHolder/CasterHolder; nonmatching indices fall back to default stack. These are unrun synchronization boundaries, not demonstrated critical runtime failures. Native handler state/server menu remains authoritative; test close/reopen and actual retained quantities.

## Quest / unrun acceptance handoff

Teach Magebloom→pouch recipe→main-hand open/store/retrieve→hotbar/Curios hotkey→optional Gold/Blaze larger bag/dye. Name Notebook and JEI; explain inventory storage versus wearing a curio. Inventory possession advancement (`curio_bag`/`caster_bag` acquisition) is proxy, not proof contents/hotkey/upgrade operated. Modest XP and ordinary building/workshop supplies; no generic storage/logistics integration added by research.

Unrun positive/boundary:27/54slot count and shift-click; normal allowed item versus bags/Bundle blacklist and cannot-fit items; direct handler insertion versus GUI predicate; worn-first/hotbar wrap/main-inventory exclusion; mainhand versus offhand index; multiple worn bags/flattened wrapper; same-item-type mayPickup restriction; upgrade/dye named bag with real contents and save/reload. No gameplay acceptance. Remaining Horn/shield/enchantments/other gear/worldgen/dependency/pilot review keep addon researching.
