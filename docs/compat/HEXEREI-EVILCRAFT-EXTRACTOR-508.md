# EvilCraft Blood Extractor ↔ Hexerei Mixing Cauldron (#508)

**Status:** source implementation / pending designated Progression writer review and next authorized Type C client verification.

## Reproduction / exact native implementation

The owner tested Experimental installed source `c56fb18ffc3b9f12f27317e7fb37f15c32576a8c`, EvilCraft 1.2.102 and Hexerei 0.5.0.3. EvilCraft Extractor → Hexerei Cauldron works; Hexerei Cauldron → EvilCraft blood bucket works through the existing **1000 mB** `hexerei:cauldron_emptying` recipe. The reverse direct Cauldron → Extractor fails. This was an originally deferred direct fluid-container boundary, not a regression in the bucket conversion.

**Two legitimate tank identities, not one:** Blood Sigil produces native `hexerei:blood_fluid`, while directly emptying an EvilCraft Blood Extractor into an **empty** Hexerei cauldron transfers `evilcraft:blood` unchanged. Both states are relevant to the owner report. The bridge therefore handles exactly those two fluids, converting only the former and preserving the latter. No other fluid is eligible.

Native source review (compare pinned JAR classes before claiming runtime equivalence):

- [EvilCraft ItemBloodExtractor, 1.21-lts source](https://github.com/CyclopsMC/EvilCraft/blob/master-1.21-lts/src/main/java/org/cyclops/evilcraft/item/ItemBloodExtractor.java): extends `ItemBloodContainer` and enables native fluid placement. Its `onItemUseFirst` only explicitly transfers from a Blood Stain while crouching. The full handler is an item fluid capability, not an arbitrary `hexerei:blood_fluid` recipient.
- [EvilCraft ItemBloodContainer](https://github.com/CyclopsMC/EvilCraft/blob/master-1.21-lts/src/main/java/org/cyclops/evilcraft/core/item/ItemBloodContainer.java): extends `DamageIndicatedItemFluidContainer` with `RegistryEntries.FLUID_BLOOD` (EvilCraft's own blood).
- [Hexerei MixingCauldronTile](https://github.com/JoeFoxe/Hexerei/blob/1.21.1/src/main/java/net/joefoxe/hexerei/tileentity/MixingCauldronTile.java): directly implements `IFluidHandler`, one tank, max 2000 mB, native Blood Sigil fills `hexerei:blood_fluid`. Generic FluidUtil interaction can also fill an **empty cauldron with `evilcraft:blood` from the extractor**. `interactWithFluid` fills the passed item with **the literal source `FluidStack`**, not TMM's canonical external blood. In inspected 1.21 upstream source, successful fill accesses `fluidHandler.getFluidInTank(1)` after checking `getTanks() > 0`, suspicious for a native **one-tank extractor**; this is another reason to preempt only this known item/tank interaction. That source defect requires installed-pin validation; don't declare it proven at runtime. `drain(..., SIMULATE/EXECUTE)` and `fill(..., SIMULATE/EXECUTE)` exist and `setChanged()` synchronizes.
- [Hexerei MixingCauldron](https://github.com/JoeFoxe/Hexerei/blob/1.21.1/src/main/java/net/joefoxe/hexerei/block/custom/MixingCauldron.java): invokes configured filling/emptying recipes before trying generic `IFluidHandlerItem`; the latter is different from native bucket recipe conversion.
- [NeoForge 1.21.1 PlayerInteractEvent.RightClickBlock](https://github.com/neoforged/NeoForge/blob/1.21.1/src/main/java/net/neoforged/neoforge/event/entity/player/PlayerInteractEvent.java): cancellable pre-block-use hook with `setCancellationResult`; first-party bridge registers at `LOWEST` priority to respect prior claim and permission cancellation.

## Implementation

`HexereiEvilCraftBloodBridge` in the single unified `compat/progression` runtime registers only if both native mods exist, with **no direct classes from those mods in its compilation API**. It checks the exact item, exact block, exact `hexerei:blood_fluid` **or** `evilcraft:blood` source, empty components and server-authorized player interaction; ordinary bucket/Blood Bottle/JAWR/NV paths remain untouched.

1. Inspect native cauldron `IFluidHandler` and the **copied** extractor's `IFluidHandlerItem`.
2. Simulate EvilCraft-fluid fill and native Hexerei-fluid source drain. The transferred amount is `min(source available, destination accepted)`, supporting partial source and partial destination, never a new fluid source.
3. Server executes **source drain first** and EvilCraft-fluid fill into the copied extractor, regardless of whether that source started as native Hexerei fluid or already-canonical EvilCraft fluid. Replace held item only on full matched success. If execution differs from simulation, try to restore drained Hexerei fluid and log fail-closed rollback errors.
4. Native cauldron's `setChanged` syncs the tank; hand update uses the fluid handler's returned container. Client predicts the click without mutating state. On success consume the event to skip Hexerei generic path.

Not a global fluid remapper; not a new fluid tag; no KubeJS recipe change; no direct JAWR bottle component matching or effect changes. The existing cauldron↔EvilCraft bucket recipes remain as-is.

### Test status

- **A:** Java compilation and pure decision tests covering empty source, full extractor, exact bucket, partially-filled cauldron, partially-filled extractor and invalid handler returns. Compile/runtime code doesn't load Hexerei/EvilCraft classes directly.
- **B/C (NOT EXECUTED):** on the next owner-authorized test build, test **both** 250/1000/1750 mB native Hexerei blood (Blood Sigil) **and** extractor-deposited EvilCraft blood (owner's round-trip) and fresh/partially-filled EvilCraft Blood Extractors; each click increases canonical EvilCraft stored blood by exactly the cauldron decrease, with no more than native extractor capacity. Verify extractor → empty cauldron behavior is unchanged, vanilla bucket → EvilCraft bucket remains, 2000 mB capacity respected, JEI/cross-mod blood vials remain intact, refill after saving/reloading, and a full extractor cannot drain any fluid. Exercise both hands and client/server prediction. Include a source-level probe for native tank serial IDs if an installed release differs from upstream source.
- **No game/client/server boot, full smoke, Prism packaging or integration assembly is authorized/performed by this branch.** Do not mark T17 PASS solely from source-level checks.

**Ownership:** source PR from `feature/progression-hexerei-evilcraft-blood-transfer-508` into `dev/progression`, cherry-picked/promoted only by designated integration writer under source locks, then `test/full-integration` after review.
