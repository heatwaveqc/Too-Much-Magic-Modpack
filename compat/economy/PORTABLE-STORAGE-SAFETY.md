# Economy portable-inventory safety (#16)

**Status:** source implementation in [PR #627](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/627), unintegrated and untested in a Minecraft runtime. **Not** release acceptance.

## Policy and performance

A portable storage *item* is not inherently hazardous. The risk is embedding an
unsafe serialized ItemStack payload inside another serialized ItemStack.
Reference-backed handles (ProjectE's player-owned per-color bags, Occultism
Ender Satchel) do not embed contents when moved. Preserve vanilla and other mods'
own self-nesting restrictions; bridge native tag policies wherever available.

`PortableStorageSafety.classify` is called **only by destination insertion paths**.
For ordinary items it runs constant-time component/tag checks. No per-tick scans,
recursive inventory traversals, global ItemStackHandler modifications, or
periodic deletion. A missing tag is **not** evidence of safety; when adding mods,
audit new portable-storage items and handlers explicitly.

## Source coverage (not runtime PASS)

| Destination | Code path | Known remaining evidence gap |
| --- | --- | --- |
| Ars Elemental Curio/Caster Bags | Additive native blacklist tag bridge | Old KubeJS blacklist still includes all portable bags; must reconcile hashes to unblock ProjectE |
| Vanilla shulker GUI | `ShulkerBoxSlot.mayPlace` | Launch/ABI, other inventory write APIs |
| Vanilla shulker hopper | `ShulkerBoxBlockEntity.canPlaceItemThroughFace` | Direct setters/other mods' automation |
| Vanilla bundle | `BundleContents.Mutable.tryInsert` | Existing malformed contents and deep component payloads |
| Occultism ordinary Satchel | `StorageSatchelSlot.mayPlace` | Other satchel access paths/capabilities |
| Malum Soulwoven/Ravenous + inherited Gaze pouch | `MalumPouchContentsComponent.Mutable.tryInsert` and **`tryTransfer` before `safeTake`** | Other direct payload mutation paths, actual method/ABI |
| Extra Botany Mini Handbag | Guard its **24 plain GUI Slots** on menu construction, and add `canPlaceItem` to *Botania ItemBackedInventory* for native cursor/Hopper helpers | Pinned-JAR method signatures audited; actual Mixin application and live paths unverified. Direct `setItem` can bypass eligibility checks |
| ProjectE 16 Alchemical Bags | No nested safety veto | External attachment store does not recursively embed bag contents; ordinary progression gates remain |

### Legacy bridge collision

**2026-11-24 integration reconciliation:** The indexed KubeJS Ars blacklist now references the same narrower `#tmm:non_nestable_embedded_containers` tag as the Economy module. The previous broad `#tmm:portable_bags` reference is removed from that bridge; no reference-backed bag ban is intended. The original source handoff below is preserved for provenance.

`kubejs/data/ars_elemental/tags/item/blacklist_bag_item.json` currently
adds `#tmm:portable_bags`, including all ProjectE colors. Merely adding
the new narrower Economy bridge **does not remove this old entry** because
Minecraft item tags are additive. The designated integration writer must
replace the existing KubeJS bridge with the new
`#tmm:non_nestable_embedded_containers` reference and refresh Packwiz
`index.toml` / `pack.toml` from current integration HEAD. Do not
replay stale #401 Transformations code or packwiz roots.

### Risk classifications

- `#tmm:non_nestable_embedded_containers`: curated high-risk embedded items.
  The tag includes 17 vanilla shulker colors, vanilla bundle, Ars, Occultism satchels,
  Malum/Gaze and Extra Botany Mini Handbag. Constrained Botania Flower Pouches
  are not blanket-classified as unsafe.
- `#tmm:reference_backed_storage`: ProjectE 16 colored keys and Occultism
  Ender Satchel. This tag cannot override a real vanilla CONTAINER payload on
  an item stack.
- Native `DataComponents.CONTAINER`, `BUNDLE_CONTENTS` and shulker class are checked for actual
  source stacks. Custom mod components need audited tags/adapters rather
  than an expensive generic recursive content traversal.

### Remaining work before release

CI runs `python compat/economy/tools/audit_portable_storage.py` and the
`audit_portable_storage_abi.py` checker against hash-verified Malum, Occultism,
Botania and Extra Botany jars. The latter inspects class/method signatures and
the handbag slot layout with `javap` without booting mod code. Economy Gradle
compile/JUnit is a separate quick-CI step. These tests do **not** verify
Mixin application, GUI transactions or safe serialized payload limits.

Review optional Mixin behavior, source-specific setters and cross-mod negative tests. Resolve any
unprotected insertion routes without dropping an item that a source already
removed. Separate B/C owner-authorized client/server/multiplayer, automation,
save-migration and payload-size acceptance is mandatory. Nothing here authorizes
a full smoke or Prism package.

## Exact GUI and source-path verification

Extra Botany 1.0.4's `MiniHandbagMenu` creates four armor slots followed by
24 **plain `Slot` instances**, then player inventory and hotbar. Those plain
slots do not consult the container's `canPlaceItem` restriction on ordinary
menu clicks; relying solely on Botania `ItemBackedInventory.canPlaceItem`
would leave cursor placement, number keys, and quick-move unprotected.
`PortableStorageMiniHandbagMenuMixin` changes only menu slot positions 4–27
after construction, retaining the original backing container, native
`mayPlace` / `mayPickup`, and menu/slot indices. The existing narrowly
scoped `ItemBackedInventory.canPlaceItem` override remains necessary for
Botania's `InventoryHelper.simulateTransfer` / hopper route. The native
Flower Pouch still accepts only mystical flowers.

Malum 1.8.2 `Mutable.tryTransfer` calls `slot.safeTake` before
`tryInsert`; an `tryInsert`-only veto could silently lose the source.
Economy checks the original slot **before** the native `safeTake`, as well
as the standalone `tryInsert` path (cursor/auto-collection), and does not
modify `addItem` or destructive drop behavior.

## Next authorized B/C acceptance matrix (not run)

1. **Embedded sources:** a filled and empty shulker, Minecraft bundle,
   Ars Curio/Caster Bag, Occultism ordinary/ritual satchel, Malum
   Soulwoven/Ravenous, Gaze Spirit-Channel (only when installed), and
   Extra Botany Mini Handbag. Exercise each against Ars, vanilla
   bundle/shulker, ordinary Occultism satchel, Malum/Gaze and Mini
   Handbag destinations. Expect source count and full item data unchanged
   after prohibited insertion, no loss/duplication or desync.
2. **Every supported transfer path:** left/right click, cursor on bag,
   bag over source slot, shift-click/quick-move, number-key hotbar swaps,
   double-click/drag, hopper and applicable modded capability/automation.
   Extra Botany's GUI slots and cursor helper are distinct cases. Native
   Flower Pouch only stores flowers.
3. **Reference-backed controls:** all 16 ProjectE Alchemical Bag keys and
   the Occultism Ender Satchel remain insertable in nested storage where
   the destination's *own* rules allow them; their actual external
   inventories remain intact and independent. No ProjectE progression
   or Chaos Guardian gate bypass.
4. **Recovery and stress:** old nested stacks remain inspectable and
   extractable, no silent deletion or rewriting, save/relog/dimension/death
   and multiplayer both sides remain consistent. Probe realistic nested
   payloads, large unique inventories, packet serialization and recursive
   history, with disposable worlds and backups only.

**Known gap:** arbitrary external calls to `ItemBackedInventory.setItem`
or other direct setters can bypass a predicate. The selected Mini Handbag
native user-facing paths use the guarded menu Slots or Botania helper, but
unsupported third-party mutation paths require an exact-JAR consumer audit,
not a global setter cancellation that could drop an already removed item.

**Integration writer crosswalk:** migrate the **old** KubeJS Ars bridge from
`#tmm:portable_bags` to `#tmm:non_nestable_embedded_containers`, reconcile
current Packwiz index/root hashes, avoid simultaneous #401 Transformations
mixins, and update the stale universal-ban text in `AGENTS.md` and
`docs/DESIGN.md` to #16's actual serialization-risk-only policy.
