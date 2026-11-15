# Canonical EvilCraft blood — config-first implementation (2026-11-15)

> **Experimental, source-backed but not client/world-tested.** This branch advances the owner's canonical physical blood target (design PR #122) using seven small datapack JSON files, **zero Java/mixin/gameplay JavaScript**, and one offline Python verification script. Existing modded worlds are not automatically migrated. Do not hide legacy fluids yet.

## Implemented in this branch

| Operation | New behavior | Implementation | State |
|---|---|---|---|
| EvilCraft placed blood | Unmodified EvilCraft fluid continues to randomly dry into Hardened Blood | Keep native `evilcraft:blood`; no block replacement | Native source-confirmed; world test outstanding |
| Neo Vitae Ara Vitae INPUT | Accept EvilCraft, Iron's, and Hexerei ordinary blood alongside own EV | `neovitae:essentia_vitae` additive fluid tag | Source-confirmed `isFluidValid`; in-game check pending |
| Common blood ingredients | Recognize Neo Vitae, Iron's and Hexerei physical blood as `c:blood` | Additive `c:blood` fluid tag; original EvilCraft tag remains | Data check only |
| Iron's Alchemist Cauldron -> output | Empty Iron Blood Vial into **250 mB EvilCraft blood** | Override `irons_spellbooks:alchemist_cauldron/fill_blood_vial` JSON | Exact data shape from 1.21 upstream branch, pinned JAR not retested |
| Iron's Alchemist Cauldron -> vial | Fill Iron Blood Vial from **250 mB EvilCraft blood** | Override `irons_spellbooks:alchemist_cauldron/empty_blood_vial` JSON | Same limitation |
| Hexerei Cauldron -> Hex bottle | Withdraw **250 mB EvilCraft blood** to obtain native Hexerei Blood Bottle | **Additional** `hexerei:cauldron_emptying` recipe; native Hex recipe preserved | Exact format from 1.21.1 source branch, pinned 0.5.0.3 untested |
| Iron Blood Vial into Hexerei Cauldron | Convert vial into **250 mB EvilCraft blood**; return Glass Bottle | New `hexerei:cauldron_filling` recipe | Recipe/code source checked, live test pending |
| Hexerei Blood Bottle into Iron's Cauldron | Convert bottle into **250 mB EvilCraft blood**; return Glass Bottle | New `irons_spellbooks:alchemist_cauldron_fill` recipe | Recipe/code source checked, live test pending |
| Pack delivery | All seven data files now in `index.toml`; `pack.toml` index checksum refreshed | Hash-pinned packwiz resources | Static metadata validation pending CI |

**Why no global recipe or tick hook?** NeoForge fluid IDs are real registry identities. A fluid tag cannot change machine output, bucket placement or an internal `FluidStack`. A periodic world scan, entity scan or arbitrary fluid block replacement would add server load, fail on unloaded chunks, and risk changing unrelated fluid effects.

## Intentionally NOT changed in this pass

1. **Neo Vitae output:** Ara Vitae *reports and drains* `neovitae:essentia_vitae_source` in hardcoded Java despite tag-aware input. Native outputs cannot be made EvilCraft blood by a tag or recipe. Neo Vitae Athanor also checks `NVFluids.ESSENTIA_VITAE_SOURCE` by identity; arbitrary external substitution would strand it. Full output canonicalization calls for a tested, small capability wrapper/targeted injection plus downstream test.
2. **Hexerei Blood Sigil production:** `MixingCauldronTile` creates `new FluidStack(ModFluids.BLOOD_FLUID.get(), 250)` directly and uses exact fluid comparisons. Native sigil flow is retained; blindly rewriting the existing bottle-to-fluid recipe would make it fail when the cauldron already holds Hexerei blood. A precise producer patch is the remaining runtime-code candidate.
3. **Hexerei native blood-bottle emptying:** Its existing 250 mB Hexerei-fluid recipe is preserved alongside the new, separately keyed EvilCraft-fluid recipe. Do not change two recipes with the same bottle input and different liquid outputs; ambiguous selection may be nondeterministic.
4. **JAWR bottles and vampire blood bank:** Bottle data and distinction between ordinary and exceptional vampire blood are not proven for the exact pinned 0.5.39 binary; leave untouched until measured. Do not substitute all bottles based on an item tag if special component metadata would be lost. Existing JAWR/Neo Vitae native bridge remains intact.
5. **Buckets/JEI hiding:** Source-mod bucket IDs, fluid blocks and existing save data remain registered. No lossy 1:1 crafting replacement that returns an extra empty bucket. Hide duplicates only once normal producers and consumers have canonical outputs and legacy migration is addressed.
6. **Natural blood production vs a mana flower:** The Botania Sanguine Bloom is a distinct feature and not implemented here.
7. **Old worlds:** No NBT rewrite. A migration must be separate and tested on disposable copies.

## Targeted in-world acceptance checklist

- Launch *full assembled* MC 1.21.1 NeoForge build with KubeJS, read startup recipe/tag errors and use JEI to confirm new recipes are loaded with the expected IDs.
- Fill Ara Vitae input with EvilCraft bucket/third-party fluid handler; observe correct input amount, altar craft result, and no phantom return.
- Extract from Ara Vitae; **expected to still be Neo Vitae fluid in this pass**. Record exact ID and note gap. Check Athanor accepts native EV.
- Fill Iron's Alchemist Cauldron with a Blood Vial; verify it gains 250 mB `evilcraft:blood`, then obtain the vial from 250 mB with a Glass Bottle. Repeat with Hexerei Blood Bottle.
- On Hexerei Cauldron: empty Iron's vial and fill native Hex bottle from 250 mB canonical blood. Verify input/output bottle counts and recipes with full and nearly full cauldrons.
- Create blood using Hexerei Blood Sigil on the cauldron; **expected to remain Hexerei blood in this pass**. Ensure its original bottle recipe still works and doesn't conflict with the added canonical path.
- Place canonical EvilCraft blood source in a suitable dry site; wait for its native random tick to harden. Verify rainfall and nearby water behavior according to pinned mod.
- Verify `c:blood` tag lists expected fluids and `neovitae:essentia_vitae` includes the native plus new allowed IDs. Test partially filled inventories, rejoin and reload.
- Run round-trip accounting at 1, 249, 250, 999 and 1000 mB; check `SIMULATE` versus `EXECUTE` if using pipes.
- Confirm no special blood bottle behavior, JAWR vampire reserve, Neo Vitae Anima/Spiritus or protected progression changed inadvertently.

## Source grounding

- Neo Vitae 1.1.53 `AraVitaeTile.isFluidValid` checks `NVTags.Fluids.ESSENTIA_VITAE`, whereas `getFluidInTank` and `drain` return `NVFluids.ESSENTIA_VITAE_SOURCE`: https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/main/java/com/breakinblocks/neovitae/common/blockentity/AraVitaeTile.java
- Neo Vitae Athanor strict check: https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/main/java/com/breakinblocks/neovitae/common/blockentity/AthanorBlockEntity.java
- EvilCraft native hardening: https://github.com/CyclopsMC/EvilCraft/blob/master-1.21-lts/src/main/java/org/cyclops/evilcraft/block/BlockFluidBlood.java
- EvilCraft `evilcraft:blood` fluid and `evilcraft:bucket_blood` item IDs: https://github.com/CyclopsMC/EvilCraft/blob/master-1.21-lts/src/main/java/org/cyclops/evilcraft/RegistryEntries.java
- Iron's 1.21 Alchemist Cauldron recipe resources: https://github.com/iron431/irons-spells-n-spellbooks/tree/1.21/src/generated/resources/data/irons_spellbooks/recipe/alchemist_cauldron
- Iron's native Blood Cauldron creates **items**, not fluid stacks: https://github.com/iron431/irons-spells-n-spellbooks/blob/1.21/src/main/java/io/redspace/ironsspellbooks/block/BloodCauldronBlock.java
- Hexerei 1.21.1 fluid registry and Blood Sigil direct fill: https://github.com/JoeFoxe/Hexerei/blob/1.21.1/src/main/java/net/joefoxe/hexerei/fluid/ModFluids.java and https://github.com/JoeFoxe/Hexerei/blob/1.21.1/src/main/java/net/joefoxe/hexerei/tileentity/MixingCauldronTile.java

## Remaining code recommendation

**Next minimum-code step:** one first-party NeoForge adapter for Ara Vitae **external fluid capability**, plus a narrow Hexerei Blood Sigil output interception if safe. Do not create a general world-fluid conversion tick. Assess whether the adapter can be placed in an existing first-party module without making that module a gameplay dependency; test actual priority/registration against Neo Vitae. Only after runtime evidence should old blood products be hidden.

Static checks: `python3 scripts/verify_canonical_blood.py` and the existing `scripts/validate.py`. Both are called in the verification workflow; they do not establish server boot, client functionality or save safety.
