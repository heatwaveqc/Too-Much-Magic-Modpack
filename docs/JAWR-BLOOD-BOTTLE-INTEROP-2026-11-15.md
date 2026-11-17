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
| Iron's `blood_vial` | Player drinks normally (KubeJS food configuration, 1 hunger, glass remainder) | Hexerei-equivalent medicine for everyone; **+500 JAWR pool** for vampires on finish |
| Hexerei `blood_bottle` | Player finishes native drinking | Native Hexerei medicine for everyone; **+500 JAWR pool** for vampires on finish, no duplicate medicine |
| Ordinary JAWR `bottle_of_blood` | Player finishes native drinking | Native **+500 JAWR blood** for vampires (exactly once), plus Hexerei-equivalent medicine for everyone |
| JAWR chicken/vampire-marked bottles | Native JAWR use and drinking | **No generic medicine override, no ordinary crafting tag, original special effects/progression preserved** |
| JAWR wine glasses | Existing native behavior | Unmodified; glass-to-bottle transfer still not confirmed |

The code detects the **native JAWR vampire attachment**, not arbitrary vampire-themed item names. It calls the **native JAWR `BloodPoolHandler.increaseBlood`** method. Non-vampire players do not gain blood credit.

Every ordinary cauldron transfer:
1. Checks the input marker gate and target mod's exact block registry ID.
2. Obtains NeoForge's native block fluid capability.
3. Requires **all 250 mB** to fit via `SIMULATE`.
4. Calls `EXECUTE`, requiring exactly 250 mB added before consuming the bottle.
5. Produces a glass bottle and cancels the native block-click action.
6. Does not modify native Hexerei Blood Sigil behavior, Neo Vitae EV, JEI listing, alternate fluid IDs or the JAWR blood reserves directly.

`BloodBottlePolicy.java` is a pure Java fail-closed classification helper with four JUnit cases covering no marker, chicken-only, vampire-only and dual-marker samples.

## Recipe and medicinal unification (KubeJS first)

**KubeJS startup code:** `kubejs/startup_scripts/drinkable_blood_vial.js` modifies Iron's Blood Vial only, using `ItemEvents.modification` with **pinned KubeJS 2101.7.2-build.381's `item.setFood(1, 1.0)` and `item.modifyFood(...)`**, a native `drink` animation, `alwaysEdible`, and `usingConvertsTo` Glass Bottle. A full restart is required. A discarded prototype used the unsupported `item.foodProperties` assignment; [smoke run 43768247822](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43768247822) identified its exact failure in the Minecraft startup log, and that incompatible setter has been removed.

**KubeJS datapack recipes:** 
- `tmm:ordinary_blood_bottles` contains only `irons_spellbooks:blood_vial` and `hexerei:blood_bottle` (NOT JAWR).
- Iron's blood focus tag now includes Hexerei's bottle; Iron's `netherite_spell_book` shaped recipe consumes either ordinary bottle.
- Hexerei willow, witch hazel and mahogany broom mixing-cauldron recipes accept either ordinary bottle in each existing blood slot. All remaining fluid inputs/outputs and other ingredients are copied from the upstream 1.21.1 recipes, including their unusual witch hazel `liquidOutput` field.
- The default `irons_spellbooks:blood_vial` can still be obtained from Iron's own 250 mB blood produced by entity damage; our separate source-fluid-specific recipe fixed an earlier extraction regression.

**Native hook remaining:** The first-party Java bridge `JAWRBottleInterop.java` is still required to check the actual JAWR special data components at cauldron boundaries and to award its native vampire blood pool on the completed drink. KubeJS recipe tags cannot safely distinguish ordinary from chicken or vampire-marked stacks sharing `witchery:bottle_of_blood`. No custom fluids, polling or broad recipe wildcard are used.

**Gameplay acceptance:** try crafting each exact broom and Netherite Spell Book with both ordinary bottle types; ensure JEI displays the tag ingredients and no special JAWR stack is accepted directly. Drink all three ordinary bottles with full and partial hunger, test absence of double return bottles and double blood credit, plus verify curse/milk cure removes positive as well as negative effects. Server startup is not proof of these interactions.

## Packaging and verification limitations

- This Java source is in the **existing experimental first-party transformation module**, not in KubeJS. It will only operate in test builds that compile and **load** the transformation JAR. Merely merging the datapack/recipe files does not activate it.
- The main integration assembler currently imports `compat/transformations/` from another transformation feature branch. **Reconcile and promote this class/registration into the authoritative transformation stream before expecting it in the next combined pack.** Avoid overwriting active work in that source branch.
- The GitHub Actions transformation-module Gradle build and unit tests validate compilation and the pure classification gate. They do not reproduce a vanilla client, actual item counts, JAWR attachment initialization or interactions at a full cauldron.
- After server startup, verify the log `JAWR ordinary blood bottle bridge enabled (strict component gate)`. The module deliberately **fails closed** if pinned JAWR component/API reflection fails.
- Iron's native `blood_vial` item is a plain `Item`; it is now made a standard always-edible food with a glass-bottle use remainder by `kubejs/startup_scripts/drinkable_blood_vial.js`. The old instant right-click JAWR credit listener has been **removed**. The sole +500 JAWR credit runs on successful `LivingEntityUseItemEvent.Finish`, shared with Hexerei's drink event.
- Ordinary JAWR bottles keep their own `BottleOfBloodItem.finishUsingItem` and native blood credit. The bridge adds only Hexerei-equivalent medicine to ordinary JAWR bottles, and **never** separately awards them +500 blood.
- Hexerei medicine means `removeEffectsCuredBy(EffectCures.MILK)` followed by **Absorption I for 200 ticks**. This removes beneficial milk-curable buffs too, matching Hexerei's own behavior. The Hexerei bottle is left native to avoid applying those effects twice.
- The KubeJS recipe tag `tmm:ordinary_blood_bottles` deliberately contains only **Iron's + Hexerei**. Native JAWR's single item ID cannot be whitelisted without also allowing special blood. Its ordinary bottle enters the common economy via the **Java component-checked 250 mB cauldron transfer**.
- KubeJS datapack overrides use `tmm:ordinary_blood_bottles` in Iron's Netherite Spell Book and Hexerei's three mixing-cauldron broom recipes; Iron's `blood_focus` tag includes Hexerei's bottle. The KubeJS datapack custom-recipe ingredient codecs must pass runtime test on pinned binaries.

## Disposable-world acceptance tests

1. Obtain **three separate native JAWR bottle stacks** using legitimate routes / debug creative data: ordinary (possibly with source UUID), chicken-marked, vampire-marked. Test both special markers present and present-but-false samples.
2. Right-click an empty and partially filled Hexerei cauldron with each. Only ordinary deposits 250 mB of Hexerei blood and returns an empty bottle; protected bottles remain exactly unchanged.
3. Repeat with Iron's Alchemist Cauldron: only ordinary deposits 250 mB of EvilCraft blood.
4. Check that components on special JAWR stacks (including source UUID) survive attempts, repeated clicks, reload, saves and JEI searches.
5. As a native JAWR vampire, **finish drinking** Iron's Vial with normal use animation in each hand: observe +500 native JAWR pool, exactly one consumed vial, one returned empty bottle, and 200-tick Absorption I. Beginning and cancelling drinking must not consume or award credit. Non-vampires should be able to drink it for the medicine **without** gaining JAWR blood.
6. As a native JAWR vampire, drink a Hexerei Bottle: observe its existing Absorption/Milk-cure rules, +500 JAWR pool **once at finish**, and normal returned glass bottle. Verify no reward for merely starting / cancelling drinking. Test ordinary JAWR bottle with and without source UUID: native +500 must **not double** and medicine should apply. Test chicken and vampire-marked JAWR bottles: no added medicine.
7. Check full and nearly full JAWR blood pools; native cap should enforce bounds and not result in double credit.
8. Native JAWR Blood Bottle and Wine Glass drinking, chicken poison and vampire-blood progression remain unmodified.
9. Inspect dedicated-server startup, integrated multiplayer client and login/reload; ensure component resolution occurs **after** registries load (ServerStartingEvent) and rejects unknown component types.

## CI verification evidence — update

- [Transformation bridge scaffold run 43764834523](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43764834523): **PASS**, NeoForge module compiles and the four pure Java ordinary/special-bottle policy unit tests pass.
- [Verify bootstrap run 43764992006](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43764992006): **PASS**, packwiz hash/manifest, JSON contract and related metadata checks.
- [Vampire dedicated-server run 43764834475](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43764834475): Bridge compilation and unit tests **PASS**; 142 pinned server-side mod JAR hashes verified; real modded dedicated server reached **Done**, ticked and shut down cleanly. The **overall workflow is marked failed solely because the ZIP upload step specified the wrong filename**. Its log shows the generated ZIP was `Too-Much-Magic-Quest-Vampire-QA-Prism.zip` but the workflow attempted `Too-Much-Magic-Vampire-Chassis-Dev-Prism.zip`. The workflow upload path has now been corrected on this branch and requires a new Actions run.
- **Neither boot success nor unit tests prove in-game player right-click cauldron, inventory remainder, component-value preservation or blood-meter increments.** These remain disposable-client acceptance tests.

## Key source evidence

- [Audited pinned-JAR source-reading workflow](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43762890498)
- [Exact JAWR pinned mod metadata](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/test/full-integration/mods/just-another-witchery-remake.pw.toml)
- [Hexerei 1.21.1 cauldron recipe matching](https://github.com/JoeFoxe/Hexerei/blob/1.21.1/src/main/java/net/joefoxe/hexerei/data/recipes/CauldronFillingRecipe.java)
- [Hexerei 1.21.1 native Blood Bottle](https://github.com/JoeFoxe/Hexerei/blob/1.21.1/src/main/java/net/joefoxe/hexerei/item/custom/bottles/BottleBloodtem.java)
- [Iron's 1.21 blood vial item registration](https://github.com/iron431/irons-spells-n-spellbooks/blob/1.21/src/main/java/io/redspace/ironsspellbooks/registries/ItemRegistry.java)
