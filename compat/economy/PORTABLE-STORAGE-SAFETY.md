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
| Extra Botany Mini Handbag | Guard its **24 plain GUI Slots** on menu construction, and add `canPlaceItem` to *Botania ItemBackedInventory* for native cursor/Hopper helpers | Exact-JAR ABI and live insertion paths not verified. Direct `setItem` can bypass eligibility checks |
| ProjectE 16 Alchemical Bags | No nested safety veto | External attachment store does not recursively embed bag contents; ordinary progression gates remain |

### Legacy bridge collision

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

Run `python compat/economy/tools/audit_portable_storage.py` for source
coverage. Add exact-JAR static/ABI checks, review optional Mixin behavior,
run isolated Java unit/Module CI, and cross-mod negative tests. Resolve any
unprotected insertion routes without dropping an item that a source already
removed. Separate B/C owner-authorized client/server/multiplayer, automation,
save-migration and payload-size acceptance is mandatory. Nothing here authorizes
a full smoke or Prism package.
