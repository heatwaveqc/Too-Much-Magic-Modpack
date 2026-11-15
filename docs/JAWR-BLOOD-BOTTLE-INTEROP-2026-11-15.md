# JAWR × Iron's × Hexerei — Strict Blood Bottle Compatibility (2026-11-15)

> **Experimental first-party compatibility code committed; client and combined-pack behavior not yet certified.**
> This contract covers *ordinary* bottled blood only and intentionally preserves all JAWR special-blood mechanics. No wholesale item tags, recipe-driven JAWR bottle substitutions or vanilla food hacks are permitted.

## Exact pinned-JAR discoveries

JAWR **0.5.39+mc1.21.1** is pinned by `mods/just-another-witchery-remake.pw.toml` to Modrinth release `TWcJbsjg` with SHA-512 verification. The [pinned-JAR bytecode audit, GitHub Actions run 43762890498](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43762890498) completed successfully.

All three JEI-visible JAWR blood bottles are the **same registered item** `witchery:bottle_of_blood`, implemented by `dev.sterner.witchery.content.item.BottleOfBloodItem` which **extends** `WineGlassItem`. Native items use `WitcheryDataComponents`:

| Component | Pinned bytecode meaning | Compat policy |
| --- | --- | --- |
| `BLOOD` | Source UUID; ordinary blood collected with Bone Needle can carry the player's UUID; important in special vampire progression attribution | **Preserve** the component and do not equate presence with special blood |
| `CHICKEN_BLOOD` | Boolean marker. Native `WineGlassItem.consumeBlood` checks it and can apply Poison with a 75% chance | **Never convert** marked JAWR bottles to generic blood, even if marker value is `false` |
| `VAMPIRE_BLOOD` | Boolean marker. Native `WineGlassItem.finishUsingItem` checks it; this branch includes special vampire leveling/progression and UUID attribution. `WineGlassItem.interactLivingEntity` can collect Lilith's blood into a wine glass | **Never convert** marked JAWR bottles to generic blood, even if marker value is `false` |
| neither marker present | Ordinary blood | Only JAWR bottle variant eligible for ordinary fluid interoperability |

Important nuance: the **'Blood?'** tooltip likely corresponds to special vampire blood, but the pinned `VAMPIRE_BLOOD` flag is the **actual authoritative boundary**. Special vampire blood is not exclusively Lilith's: the Bone Needle source also sets this marker in certain vampire-level conditions. Avoid deriving subtype from translated tooltip text.

Native JAWR bottle drinking is source-confirmed:
- `BottleOfBloodItem.finishUsingItem` calls inherited `WineGlassItem` behavior and then `consumeBlood` for vampires.
- `WineGlassItem.consumeBlood` calls `BloodPoolHandler.increaseBlood(livingEntity, 500)`; chicken blood also retains its distinct poison behavior.
- The inherited `WineGlassItem` component-sensitive progression logic remains unmodified.

The Bone Needle's native use accepts a **wine glass** or an **empty glass bottle** in the offhand to collect blood. This is *two collection paths*, not evidence of a direct **filled wine glass → filled bottle** transfer. No lossy, component-stripping conversion has been added.

## Why a hard code gate is unavoidable for JAWR ordinary bottles

Hexerei's `CauldronFillingRecipe.matches` compares the **input item ID**, not data components. A plain recipe such as `input: witchery:bottle_of_blood` would therefore accept the chicken and special vampire variants, silently launder them into generic blood. Iron's simple ingredient recipes have the same general risk if they are not explicitly component-filtered.

The implemented gate resolves **the actual pinned JAWR component types via reflection** after server registration completes, then performs:

```text
ordinary = item == witchery:bottle_of_blood
           AND CHICKEN_BLOOD component ABSENT
           AND VAMPIRE_BLOOD component ABSENT
```

Both protected markers are checked by **presence**, regardless of the boolean value, to fail closed on anomalous stacks. The `BLOOD` UUID alone does not exclude legitimate ordinary blood.

The adapter is `JAWRBottleInterop.java` in the existing first-party `compat/transformations` module. No new standalone mod, no global tick scan, no upstream class modifications, and **no JSON recipe that matches all JAWR bottles**.

## Implemented server-side interactions

| Source | Action | Result |
| --- | --- | --- |
| Ordinary JAWR Blood Bottle | Right-click Hexerei Mixing Cauldron, if 250 mB fits | Exactly **250 mB** `hexerei:blood_fluid`; return one glass bottle |
| Ordinary JAWR Blood Bottle | Right-click Iron's Alchemist Cauldron, if 250 mB fits | Exactly **250 mB** `evilcraft:blood`; return one glass bottle |
| JAWR Chicken Blood Bottle | Either cauldron | **No compat conversion**; remains JAWR-special |
| JAWR Vampire Blood Bottle / marked 'Blood?' | Either cauldron | **No compat conversion**; retains vampire progression data |
| Iron's `blood_vial` | JAWR vampire right-clicks the item | Native JAWR blood pool **+500**, consume one vial, return glass bottle (instant drink) |
| Hexerei `blood_bottle` | JAWR vampire finishes drinking it | Native Hexerei food/effects apply; native JAWR blood pool **+500** on finish |
| JAWR own bottles and wine glasses | Existing native behavior | **Unmodified**, including special effects and vampire leveling |

The code detects the **native JAWR vampire attachment**, not arbitrary vampire-themed item names. It calls the **native JAWR `BloodPoolHandler.increaseBlood`** method. Non-vampire players do not gain blood credit.

Every ordinary cauldron transfer:
1. Checks the input marker gate and target mod's exact block registry ID.
2. Obtains NeoForge's native block fluid capability.
3. Requires **all 250 mB** to fit via `SIMULATE`.
4. Calls `EXECUTE`, requiring exactly 250 mB added before consuming the bottle.
5. Produces a glass bottle and cancels the native block-click action.
6. Does not modify native Hexerei Blood Sigil behavior, Neo Vitae EV, JEI listing, alternate fluid IDs or the JAWR blood reserves directly.

`BloodBottlePolicy.java` is a pure Java fail-closed classification helper with four JUnit cases covering no marker, chicken-only, vampire-only and dual-marker samples.

## Packaging and verification limitations

- This Java source is in the **existing experimental first-party transformation module**, not in KubeJS. It will only operate in test builds that compile and **load** the transformation JAR. Merely merging the datapack/recipe files does not activate it.
- The main integration assembler currently imports `compat/transformations/` from another transformation feature branch. **Reconcile and promote this class/registration into the authoritative transformation stream before expecting it in the next combined pack.** Avoid overwriting active work in that source branch.
- The GitHub Actions transformation-module Gradle build and unit tests validate compilation and the pure classification gate. They do not reproduce a vanilla client, actual item counts, JAWR attachment initialization or interactions at a full cauldron.
- After server startup, verify the log `JAWR ordinary blood bottle bridge enabled (strict component gate)`. The module deliberately **fails closed** if pinned JAWR component/API reflection fails.
- Right-click item drinking of Iron's vial is instantaneous, since that item is a plain `Item`; a timed animation would require an additional hook and is not in this minimum-code version.

## Disposable-world acceptance tests

1. Obtain **three separate native JAWR bottle stacks** using legitimate routes / debug creative data: ordinary (possibly with source UUID), chicken-marked, vampire-marked. Test both special markers present and present-but-false samples.
2. Right-click an empty and partially filled Hexerei cauldron with each. Only ordinary deposits 250 mB of Hexerei blood and returns an empty bottle; protected bottles remain exactly unchanged.
3. Repeat with Iron's Alchemist Cauldron: only ordinary deposits 250 mB of EvilCraft blood.
4. Check that components on special JAWR stacks (including source UUID) survive attempts, repeated clicks, reload, saves and JEI searches.
5. As a native JAWR vampire, drink an Iron's Vial in each hand: observe +500 native JAWR pool, exactly one consumed vial, one returned empty bottle, no double credit or hunger counter replacement. As a non-vampire, right-clicking must not drain the vial.
6. As a native JAWR vampire, drink a Hexerei Bottle: observe its existing Absorption/Milk-cure rules, +500 JAWR pool **once at finish**, and normal returned glass bottle. Verify no reward for merely starting / cancelling drinking.
7. Check full and nearly full JAWR blood pools; native cap should enforce bounds and not result in double credit.
8. Native JAWR Blood Bottle and Wine Glass drinking, chicken poison and vampire-blood progression remain unmodified.
9. Inspect dedicated-server startup, integrated multiplayer client and login/reload; ensure component resolution occurs **after** registries load (ServerStartingEvent) and rejects unknown component types.

## Key source evidence

- [Audited pinned-JAR source-reading workflow](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43762890498)
- [Exact JAWR pinned mod metadata](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/test/full-integration/mods/just-another-witchery-remake.pw.toml)
- [Hexerei 1.21.1 cauldron recipe matching](https://github.com/JoeFoxe/Hexerei/blob/1.21.1/src/main/java/net/joefoxe/hexerei/data/recipes/CauldronFillingRecipe.java)
- [Hexerei 1.21.1 native Blood Bottle](https://github.com/JoeFoxe/Hexerei/blob/1.21.1/src/main/java/net/joefoxe/hexerei/item/custom/bottles/BottleBloodtem.java)
- [Iron's 1.21 blood vial item registration](https://github.com/iron431/irons-spells-n-spellbooks/blob/1.21/src/main/java/io/redspace/ironsspellbooks/registries/ItemRegistry.java)
