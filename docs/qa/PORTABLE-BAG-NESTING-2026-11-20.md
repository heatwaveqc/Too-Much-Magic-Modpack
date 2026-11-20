# Portable bag nesting — staged release-blocker hardening (#16)

**2026-11-20. Source implementation; NOT a gameplay PASS.** No bag should be placed inside another bag, including different mods and all 16 ProjectE colors. No first-login migration, inventory deletion, periodic stack mutation, or generic blacklist on stationary chests is allowed.

## Runtime source changes

| Destination | Pre-transfer guard | Scope |
| --- | --- | --- |
| Ars Elemental Curio / Caster Bags | Existing native `#ars_elemental:blacklist_bag_item` includes `#tmm:portable_bags` | Adds all vanilla shulker variants to the same tag |
| Occultism Storage Satchel | `StorageSatchelSlot.mayPlace` mixin | Cross-mod bags and shulker blocks on normal GUI transfer, before movement |
| ProjectE sixteen Alchemical Bags | Tag active handler identity at `AlchBagImpl.AlchemicalBagAttachment.getBag`, plus client `AlchBagContainer` constructor; `ItemStackHandler.isItemValid` rejects for those handlers only | Normal slot, shift-click and NeoForge `insertItem` paths; **not** generic chests / other unmarked machine handlers |
| Vanilla Bundle | `BundleContents.Mutable.tryInsert` | Reject tagged bag or shulker before accepting the stack |
| Vanilla Shulker Box | `ShulkerBoxSlot.mayPlace` and `ShulkerBoxBlockEntity.canPlaceItemThroughFace` | GUI and sided hopper insertion; previously stored items untouched |

`PortableContainerGuard` checks the existing shared item tag plus native `BlockItem` / `ShulkerBoxBlock` identity. Tagged shulker variants also allow Ars native blacklist to apply without a special plugin. The ProjectE handler marker uses a synchronized **weak identity-oriented object set** so cached colored player handlers are marked while ordinary storage is not globally banned; the handler object, saved data and codec remain the original ProjectE objects. (Note: `WeakHashMap` tracks Java object equality, equivalent to identity for the current `ItemStackHandler` implementation; verify if upstream overrides equality.)

**Existing nested stacks on upgraded saves are not deleted or rewritten.** Reject only *new insertion*. Escaping an illegal old nested item and placing it into a normal chest remains possible.

## Remaining release-blocker verification

This is a scoped first-party compatibility change, not verified against exact compiled third-party JARs yet. In the next **owner-authorized client and server QA** verify target mixin descriptors and absence of startup errors, then test the following matrix:

1. Every source bag (Ars Curio/Caster, Occultism regular/Ender/ritual satchels, ProjectE all colors, vanilla bundle, all shulkers) against each supported destination. Reject before move; no disappearance or duplication; normal stone/other inventory items still insert.
2. GUI cursor placement, shift-click, number-key swap, quick move, client and dedicated-server handling; ProjectE per-color shared inventory; multiplayer, death, relog, dimension transitions.
3. Automation: ProjectE `IItemHandler.insertItem`, shulker hoppers, any exposed Occultism/Ars capabilities and third-party transfer interfaces. Verify no direct `setStackInSlot` bypass or external NBT data injection causes duplication. Those unverified APIs remain a **release gate**.
4. Save migration: existing illicit nested stacks retained and recoverable, without silently deleting items; regular permanent chests may still store portable bags.
5. Vanilla-only behavior: shulkers remain placeable in chests, bundles and shulkers continue to work for ordinary materials, no negative inventory/network synchronization or performance regression.

## Testing and integration

The module's pure unit decision is `PortableBagNestingRules.rejects(empty,tagged,shulker)`. Actual runtime source uses `PortableContainerGuard` on five classes. The test module builds via source CI `.github/workflows/transformations-bridge.yml` / `.github/workflows/transformation-bridge.yml`, not the full server smoke.

Packwiz `index.toml` and `pack.toml` were updated because `kubejs/data/tmm/tags/item/portable_bags.json` changed. **Integration writer:** reconcile these with concurrent #394 and #396 before merging; do not reapply stale root metadata. First-party `compat/transformations` JAR must be rebuilt during the next explicitly authorized integration assembly; a source PR alone does not update the installed runtime.

**Remaining owner acceptance:** no one should mark #16 complete or the survival release unblocked until full cross-mod matrix, programmatic setter/bag-storage write paths, and exact pinned runtime behaviors have been tested.
