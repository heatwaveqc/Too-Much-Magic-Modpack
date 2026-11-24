# Hexerei Mixing Cauldron ordinary blood normalization (#557)

**Owner-locked boundary, 2026-11-22:** keep `evilcraft:blood` canonical in external buckets, pipes, tanks and world blocks. All **ordinary physical blood** stored inside the Hexerei Mixing Cauldron must use `hexerei:blood_fluid`, regardless of input route, conserving volume. Keep nonblood fluids, custom fluid components, JAWR vampire/chicken blood, Neo Vitae EV/Anima and Forbidden & Arcanus Blood Essence unchanged. No duplicate input-item recipes for the same ordinary blood bottle.

**Implementation** is first-party Progression, isolated under `feature/progression-hexerei-cauldron-blood-normalization-557`, targeting `dev/progression`. It is not the existing extracted-blood output bridge [PR #552](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/552), and no separate Transformations Java runtime should be installed in the eventual unified pack. Integrator must reconcile the [#468](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/468)/[PR #515](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/515) single-JAR migration before promotion.

## Exact source boundary: two different ingress paths

Hexerei 0.5.0.3's 1.21.1 source:
- [`MixingCauldronTile.fill(FluidStack,FluidAction)`](https://github.com/JoeFoxe/Hexerei/blob/1.21.1/src/main/java/net/joefoxe/hexerei/tileentity/MixingCauldronTile.java): public `IFluidHandler` entry for pipes, pumps and other capability-based transfers. Native `fill` tests the identity of the argument directly and preserves it in the tank; when that argument is `evilcraft:blood`, the tank would have the wrong fluid.
- [`MixingCauldronTile.interactWithFluid(IFluidHandlerItem)`](https://github.com/JoeFoxe/Hexerei/blob/1.21.1/src/main/java/net/joefoxe/hexerei/tileentity/MixingCauldronTile.java): Hexerei's held-container interaction drains the item and **directly assigns** its fluid into `this.fluidStack`, bypassing `fill`. Merely overriding `fill` cannot normalize EvilCraft Extractor → empty cauldron. This method also otherwise demands literal fluid identity on partial tanks, preventing directly depositing EvilCraft blood into existing Hexerei blood.
- [`MixingCauldron.useItemOn`](https://github.com/JoeFoxe/Hexerei/blob/1.21.1/src/main/java/net/joefoxe/hexerei/block/custom/MixingCauldron.java): native recipe filling/emptying routes run **before** the generic `IFluidHandlerItem` interaction. We do not replace those recipes or JAWR's strict component-gated listener.

`HexereiOrdinaryBloodInputMixin` targets both methods under a **separate optional** `tyr_progression_hexerei_blood.mixins.json` registered in the Progression module metadata. Uses target name string + `@Pseudo`, no compile-time Hexerei classes. Compile target and actual installed version ABI remain independently verified; source-level compilation is NOT runtime mixin proof.

Two recognized ordinary physical blood source IDs ONLY:
- `evilcraft:blood` — pack's external canonical liquid.
- `irons_spellbooks:blood` — Iron's native ordinary blood produced by direct damage interactions.

Hexerei-native `hexerei:blood_fluid` already fits; no conversion needed. In particular **do not convert** `neovitae:essentia_vitae_source`, JAWR vampire blood meters/components, F&A blood test tubes/typed essence, unknown `c:blood` members, or component-backed custom blood `FluidStack`s.

### Code behavior

1. **Pipe/capability fill:** rewrite incoming `FluidStack` before native `fill`, to a *new*, equal-amount Hexerei `FluidStack` when and only when its registry ID is in the exact allowlist and the source has no extra components. Native capacity/identity rules then stay authoritative; `SIMULATE` does not mutate either side. No global fluid registry or tag overwrite.
2. **Held fluid container:** intercept only the source item's ordinary blood `FluidStack`. If the cauldron has an empty tank or native Hexerei blood, first simulate a typed item drain and Hexerei-fluid cauldron fill, choose an exact bounded mB amount and re-simulate. On the server, drain from Hexerei's **already-copied** `IFluidHandlerItem` and fill the cauldron with native blood. Hexerei publishes the copied item to the player's hand only when the operation returns success. On error, do not publish the altered item, and roll back any partial cauldron fill. Client predicts without changing fluids.
3. **No unauthorized inputs:** if an ordinary input ID is recognized but the transfer is unsafe/full/incompatible, the mixin cancels the native `interactWithFluid` route and returns false, preventing native code from silently recording `evilcraft:blood` in the cauldron.
4. **Existing native recipes:** retain original Hexerei Blood Bottle ↔ native Hexerei fluid, TMM Iron's vial ↔ native Hexerei fluid, existing *ordinary only* JAWR bottle bridge, the TMM 250 mB bottle and 1000 mB EvilCraft bucket outward recipes. These should now always see native Hexerei blood on fresh cauldron inputs. No new duplicate `hexerei:cauldron_filling` JSON.

### Compatibility / acceptance (unrun Type C until owner authorizes)

- **A:** static source/ABI validation and Gradle module compilation/unit checks. `HexereiBloodInputPolicyTest` covers exact source allowlist, absent/alien identities, container/tank limits, full/empty, 1/249/250/999/1000/1750/2000 mB, other fluid rejection, invalid native handler states.
- **C1:** EvilCraft Blood Extractor 5000 mB → empty Hexerei cauldron, transferring up to 2000 mB. Tank **must report** `hexerei:blood_fluid`, extractor actual contents decreased by identical amount. Refill extractor from cauldron using [PR #552](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/552) (which separately converts **outgoing** blood to EvilCraft).
- **C2:** Partly-filled native Hexerei cauldron (250/750/1750 mB) plus EvilCraft Extractor, Dark Tank held item, and pipe from an EvilCraft/Neo Vitae Blood Tank holding *EvilCraft ordinary fluid*. Existing native Hexerei pool must increase 1:1 without replacement. No dupe/loss near 2000 mB.
- **C3:** Ordinary Iron's native blood in a supported fluid handler → Hexerei cauldron; confirm native `irons_spellbooks:blood` does NOT persist inside. Iron's 250 mB Blood Vial recipe remains.
- **C4:** All 250 mB ordinary bottles (Hexerei, Iron's, component-gated ordinary JAWR) can fill and empty the Hexerei cauldron after the above imports, and return only one glass. JAWR source UUID allowed; `CHICKEN_BLOOD` and `VAMPIRE_BLOOD` marker-presence bottles rejected, without stripping anything or awarding vampire pool. Test each hand, inventory full and interrupted clicks.
- **C5:** Water, potion, unrelated/magical/metadata-bearing blood and Neo Vitae EV are *not* normalized. Try full and empty cauldrons; components stay present. Native Hexerei Blood Sigil, potion/brew recipe, cauldron graphics and save/reload are unchanged.
- **C6:** Native, External and automation `SIMULATE/EXECUTE` consistency: one external amount debit per one internal credit. Verify no repeat event registration after Progression internalizes the former Transformations module, and ensure merged build only includes one Progression JAR.
- **No old-world migration requirement:** no established stable world exists. Existing saved `evilcraft:blood` Hexerei cauldrons are not silently rewritten by this patch; fresh input normalization is the approved contract.

**No Minecraft world/client/server smoke, packwiz integration assembly or Prism package is authorized by this PR.** Use only quick module CI; do not mark gameplay PASS.
