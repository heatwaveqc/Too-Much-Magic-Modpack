# Canonical EvilCraft Blood — Configuration-First Implementation (2026-11-15)

> Experimental branch. The **fluid, bucket and cauldron recipe stage uses no Java**. JAWR ordinary-bottle component gates and cross-mod vampire drinking now add a small module in existing first-party `compat/transformations` code; see [the exact-JAR JAWR bottle audit and strict backend bridge](JAWR-BLOOD-BOTTLE-INTEROP-2026-11-15.md). Runtime bottle/click validation is still outstanding. Source references describe the upstream Minecraft 1.21.1 implementations and are not a substitute for testing the selected pack versions.

## Owner-approved boundary model

- **External ordinary physical blood:** `evilcraft:blood` in buckets and fluid outputs under pack control; placed EvilCraft source blocks keep their native random-tick hardening into Hardened Blood.
- **Neo Vitae Ara Vitae:** original EV accounting and `neovitae:essentia_vitae_source` remain internal and accessible to normal pipes. Blood is accepted on input through the existing `neovitae:essentia_vitae` fluid tag. **Only the altar's bucket-crafting recipe** changes to output an EvilCraft bucket. The native fluid capability is not patched.
- **Hexerei Mixing Cauldron:** Blood Sigil still produces `hexerei:blood_fluid` in its internal tank. Player-facing **bucket recipes convert at extraction and refill**, instead of changing the cauldron's internal fluid. Native Hexerei Blood Bottles remain available because they have their own effects and are used in broom recipes.
- **Bottles:** Iron's Blood Vial and Hexerei Blood Bottle both represent 250 mB ordinary blood *as items*; they do not themselves store an EvilCraft FluidStack. At cauldron boundaries, the chosen recipe sets the correct physical fluid ID. JAWR bottles and vampire bank remain untouched.

## Implemented datapack recipes and tags

| Boundary | Input | Output / retained fluid | Runtime status |
| --- | --- | --- | --- |
| Common blood `c:blood` | Existing EvilCraft, Iron's, Hexerei and Neo Vitae fluids | Additive tag members | Static-only |
| Neo Vitae fluid input `neovitae:essentia_vitae` | EvilCraft, Iron's and Hexerei blood | Accepted by native `AraVitaeTile.isFluidValid` | Source-verified predicate; runtime pending |
| **Neo Vitae altar bucket ritual** | Empty bucket + 1,000 EV | **`evilcraft:bucket_blood`** (was `neovitae:essentia_vitae_bucket`) | Recipe override; runtime pending |
| **Hexerei blood cauldron → bucket** | Empty bucket + 1,000 mB `hexerei:blood_fluid` | **`evilcraft:bucket_blood`** | New `hexerei:cauldron_emptying` recipe; runtime pending |
| **EvilCraft bucket → Hexerei blood cauldron** | `evilcraft:bucket_blood` | Empty bucket + 1,000 mB **`hexerei:blood_fluid`** | New `hexerei:cauldron_filling` recipe; runtime pending |
| **Iron's vial → Hexerei blood cauldron** | `irons_spellbooks:blood_vial` | Empty glass bottle + 250 mB **`hexerei:blood_fluid`** | New recipe; runtime pending |
| Native Hexerei blood bottle → Hexerei cauldron | `hexerei:blood_bottle` | Empty glass bottle + 250 mB Hexerei blood | **Unmodified native recipe**, source-confirmed |
| Native Hexerei cauldron → blood bottle | Empty glass bottle + 250 mB Hexerei blood | `hexerei:blood_bottle` | **Unmodified native recipe**, source-confirmed |
| EvilCraft fluid in Hexerei cauldron → Hexerei bottle | Empty glass bottle + 250 mB EvilCraft blood | `hexerei:blood_bottle` | Additional recipe; runtime pending |
| Iron's vial → Iron's Alchemist Cauldron | `irons_spellbooks:blood_vial` | Empty glass bottle + 250 mB **EvilCraft blood** | Overrides native Iron's recipe; runtime pending |
| Iron's Alchemist Cauldron → vial | Empty glass bottle + 250 mB EvilCraft blood | `irons_spellbooks:blood_vial` | Overrides native Iron's recipe; runtime pending |
| Hexerei blood bottle → Iron's Alchemist Cauldron | `hexerei:blood_bottle` | Empty glass bottle + 250 mB **EvilCraft blood** | Additional Iron's recipe; runtime pending |
| Iron's Alchemist Cauldron native damage-generated blood → vial | Empty glass bottle + 250 mB `irons_spellbooks:blood` | `irons_spellbooks:blood_vial` | **Restored fluid-specific fallback** after finding vanilla Iron's damaging-entity output bypasses recipes; static validation passed, in-game pending |

**Important:** Hexerei's `CauldronFillingRecipe.matches` tests the input **item**, not the destination cauldron fluid. The prior prototype offered an `irons_spellbooks:blood_vial` → EvilCraft-fluid recipe in Hexerei alongside the proposed vial → Hexerei-fluid recipe. That would lead to nondeterministic selection of two recipes for the same bottle. The old `iron_vial_to_evilcraft.json` recipe is **deleted**. Only the vial → Hexerei blood recipe remains for Hexerei's cauldron. Iron's own cauldron still yields EvilCraft blood.

The 250 mB bottled-blood transaction and 1,000 mB bucket transaction are exact one-to-one conversions. Native cauldron recipes execute *before* `FluidUtil` generic container interactions, allowing bucket conversion without an API wrapper.

## Player-facing JEI reduction — not yet enabled

The desired presentation is one ordinary blood fluid — `evilcraft:blood` — with one primary bucket — `evilcraft:bucket_blood`. Hide **redundant fluid entries and buckets**, not their registered IDs, after client tests. Leave vanilla and existing machine paths intact.

The liquid identity is not yet universal for pipes: Neo Vitae's altar drains `neovitae:essentia_vitae_source`; Hexerei's cauldron contains `hexerei:blood_fluid`; Iron's native damage cauldron produces `irons_spellbooks:blood`. The added Iron's **native blood-to-vial** recipe ensures the custom `evilcraft:blood` vial fill override does not strand that production.

Do not indiscriminately hide **all** blood bottle items or treat them as one crafting tag:
- Iron's `irons_spellbooks:blood_vial` was used as a native `irons_spellbooks:blood_focus` and an explicit ingredient in the Netherite Spell Book. **Upgraded in this branch:** the blood-focus tag accepts Hexerei's bottle and the Netherite Spell Book uses `tmm:ordinary_blood_bottles`. Iron's vial is also KubeJS-modified into a normal drinkable with a glass-bottle remainder.
- Hexerei's `hexerei:blood_bottle` is genuinely consumable (Absorption, removes milk-curable effects), and its three broom recipes have now been rewritten to accept either the Hexerei or Iron's ordinary bottle through the KubeJS tag. Its medicinal effects are also shared with ordinary JAWR and Iron's bottles through the strict Java drink-finish bridge.
- JAWR's *three JEI bottle variants are one item ID* distinguished by `WitcheryDataComponents.CHICKEN_BLOOD` and `VAMPIRE_BLOOD`. Only **ordinary** blood is eligible for generic hiding/conversion. Never hide special chicken/vampire blood in a blanket `witchery:bottle_of_blood` rule. Source UUID (`BLOOD` component) may exist on ordinary bottles.
- For **KubeJS 2101.7.2 / Minecraft 1.21.1**, use `RecipeViewerEvents.removeEntries` (not legacy `JEIEvents.hideItems`). Client-side predicate filters permit selective JAWR variant hiding; this is pending validation with pinned JEI 19.64.0.455. Hiding in JEI is visual only: all items, old recipes and direct mod-native outputs still exist.

**Before JEI hiding:** test all cauldron extraction/refill, KubeJS food-drinking completion and crafting-tag recipes in-game; then choose which ordinary blood bottle stays visible. The JAWR ordinary bottle cannot safely enter a wildcard item tag because its special variants share one item ID. No JEI hide events are committed as of this revision.

## Why keep Hexerei Blood Bottles?

Hexerei's `BottleBloodtem` is an edible/special item with its own effects. Three native broom recipes required `hexerei:blood_bottle` directly; **KubeJS overrides now allow either Iron's or Hexerei's ordinary bottle in those slots.** Removing the Hexerei bottle itself would still suppress its original distinct visual identity and independently consumable behavior. Accordingly, the bottles remain available and functionally interchangeable **as blood reagents at supported recipe boundaries**, not as registry-alias fluid containers.

The user requested a **single physical blood fluid**, not deletion of all distinct bottle items. If desired later, a single external bottled item can be adopted after auditing JAWR and Hexerei special-item effects and dependent recipes.

## Remaining code and tests

**Not needed for the approved manual bucket route:** custom Neo Vitae fluid capability wrapper, Hexerei Blood Sigil interception, global fluid normalization loops.

**Still not solved without custom integrations:** every modded pipe, third-party fluid container, auto-fill machine and world source block reporting only `evilcraft:blood`. Neo Vitae pipe draining still emits native EV fluid; Hexerei cauldron pipes emit native Hexerei blood. This is **approved** for now. Existing-world migration and item hiding are deferred until in-world validation.

### Required in-world checks (disposable world)

1. Right-click Hexerei cauldron containing 1,000 mB Hexerei blood with empty bucket: returns EvilCraft blood bucket; cauldron loses exactly 1,000 mB.
2. Right-click same empty cauldron with EvilCraft blood bucket: returns empty bucket and **1,000 mB Hexerei blood**; Blood Sigil continues filling.
3. Empty an Iron's Blood Vial into Hexerei cauldron with 0 and 250 mB existing Hexerei blood. Verify 250 mB deposited and empty bottle returned; **no duplicate recipe selected**.
4. Bottle Hexerei blood with empty glass bottle, then return the Hexerei Blood Bottle to the cauldron. Verify 250 mB both ways, and native Hexerei broom crafting and bottle effects.
5. In Iron's Alchemist Cauldron, empty Hexerei bottle or Iron's vial; 250 mB EvilCraft blood enters. Bottle it out. Check exact item/fluid quantities.
6. Fill Neo Vitae altar through tag-aware native handler; craft an EvilCraft bucket with empty bucket + exactly 1,000 EV using the same `bucket_life` ID. Test altars near 999/1000 EV and automation. Verify normal pipes still drain native EV fluid successfully.
7. Place extracted EvilCraft blood in the world and confirm native drying into Hardened Blood under correct conditions; verify adverse rain/water conditions.
8. Restart/rejoin, save/reload, partially fill cauldrons, and check no bucket duplication, invalid JEI recipes or datapack codec errors.
9. Retain JAWR blood bank and Neo Vitae Anima/Spiritus behavior unchanged.

## Evidence / source pointers

- Neo Vitae exact-version generated recipe: `src/generated/resources/data/neovitae/recipe/ara_vitae/bucket_life.json` from [pinned-source snapshot](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/recipe/ara_vitae/bucket_life.json).
- [Hexerei 1.21.1 MixingCauldron item interaction](https://github.com/JoeFoxe/Hexerei/blob/1.21.1/src/main/java/net/joefoxe/hexerei/block/custom/MixingCauldron.java), handles recipe filling and emptying first, then `FluidUtil`.
- [Hexerei 1.21.1 MixingCauldronTile](https://github.com/JoeFoxe/Hexerei/blob/1.21.1/src/main/java/net/joefoxe/hexerei/tileentity/MixingCauldronTile.java), Blood Sigil directly adds its own blood fluid to cauldron.
- [Hexerei native bottle filling](https://github.com/JoeFoxe/Hexerei/blob/1.21.1/src/main/resources/data/hexerei/recipe/cauldron_filling/blood_bottle.json) and [bottle withdrawal](https://github.com/JoeFoxe/Hexerei/blob/1.21.1/src/main/resources/data/hexerei/recipe/cauldron_emptying/blood_to_bottle.json).
- [Hexerei BottleBloodtem](https://github.com/JoeFoxe/Hexerei/blob/1.21.1/src/main/java/net/joefoxe/hexerei/item/custom/bottles/BottleBloodtem.java), edible native item.
- [EvilCraft BlockFluidBlood](https://github.com/CyclopsMC/EvilCraft/blob/master-1.21-lts/src/main/java/org/cyclops/evilcraft/block/BlockFluidBlood.java), native hardening.
- Iron's 1.21 Alchemist Cauldron generated recipe `fill_blood_vial.json` / `empty_blood_vial.json` sourced from [upstream repository](https://github.com/iron431/irons-spells-n-spellbooks/tree/1.21/src/generated/resources/data/irons_spellbooks/recipe/alchemist_cauldron).

CI static: `python3 scripts/verify_canonical_blood.py` and `python3 scripts/validate.py`; game tests NOT RUN.
