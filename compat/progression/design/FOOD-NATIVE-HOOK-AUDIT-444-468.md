# Food/native hook audit and evidence-driven bridge — #444 / #468
2026-11-22. Source-only Progression PR; no installed gameplay changes.

## Pinned Dragon Survival 2.0.74 source evidence

Packwiz installs Dragon Survival 2.0.74. Upstream 1.21.1 GitHub commit 658c63608adc0194c9c9516804ecd66c1fd10112 (Nov 9, 2026) declares mod_version=2.0.74 in gradle.properties.

- Upstream mixins/IItemExtensionMixin.java changes **IItemExtension.getFoodProperties(ItemStack, LivingEntity)** with ModifyReturnValue. Old Progression DragonDrinkPermissionMixin targeted **IItemStackExtension**, which could not intercept the actual native selection. Corrected Progression mixin targets IItemExtension at priority 900, after the upstream default 1000 priority.
- The pinned upstream source declares DragonStateHandler.setSpecies(Player, Holder<DragonSpecies>, boolean), OnDeath.trigger(LivingDeathEvent), DragonModel.getModelResource/getTextureResource(DragonEntity), and DragonGlowLayerRenderer/DragonArmorRenderLayer.render. These are declaration-level checks, **not distributed JAR bytecode or live mixin application**.
- Food consumption callbacks, native side effects, returned containers, effects, eating times and other ordinary-player mechanics remain native. A dietary override replaces nutrition and saturation only for verified source evidence.

## Generated evidence and runtime-virtual tags

The Progression FoodPolicyRegistry reloads the config at data/tyr_progression/tyr_progression/food_policies/default.json and generated evidence records under data/<namespace>/tyr_progression/food_sources/*.json as one validated snapshot.

Each generated evidence record contains source item ID, provenance ID, number of whole raw ingredient units, optional inferred tags and per-profile preferred ingredient fractions. The nutrition rules remain in the policy JSON: these records are provenance data, **not manually maintained diet nutrition values**.

Synthetic example, not shipped in runtime:

    {
      "schema":1, "item":"example:raw_cod_slice",
      "provenance":"example:cutting/raw_cod",
      "source_units":0.5,
      "inferred_tags":["#c:foods/raw_fish","#tmm:foods/raw"]
    }

Inferred tags augment current item tags *for the dietary evaluator only*; they do NOT mutate Minecraft's item-tag registry. Shared physical tag materialization for other mods remains a responsibility of the independently reviewed provenance generator.

No real item evidence is shipped in this PR. If provenance is missing, ambiguously duplicated, or invalid, the food handler keeps the native result or rejects that data reload; it never assumes each edible item is a whole source. Generated evidence may be added/removed by datapacks. The bridge runs only for actual pinned Dragon Survival players with a verified Cave, Forest or Sea dragon species and a genuine FoodProperties component. It preserves existing magic-food permissions and diet-permission drinks, with no changes to native item uses or global food components.

## Additional hook findings / blockers

- **Goety mismatch:** installed mods/goety.pw.toml is goety-3.2.3.jar, but DracolichMixinPlugin enables GoetyDracolichAcquisitionMixin only for version 3.2.2. That setter hook is disabled in the installed roster. Do not widen the version check without verifying exact 3.2.3 bytecode of LichdomHelper.setLich(Player, boolean) or equivalent publisher-matched source.
- Version pins for Dragon Survival 2.0.74, GeckoLib 4.9.3, Botania Revived 2.0.2, JAWR 0.5.39 / Forbidden Magic 0.5.37, and Mahou 1.36.31 match the local optional-mixin gates. Pin equality is not proof that a mixin injection works at runtime. Verify client GeckoLib loader/resource methods, native JAWR setters, Botania/Mahou behaviors, and optional-provider absence during B/C tests.
- The server diet override is not automatically sent to the client. A synchronized client preview, Dragon Survival eating duration and effects must be verified before gameplay acceptance.

## Next steps

1. Complete #463 pinned recipe/loot inventory; generate vetted tags, source units, output counts and mixed profile fractions; no manual food matrix.
2. Verify Goety 3.2.3 pinned ABI before enabling its setter hook. Do not assume 3.2.2 compatibility.
3. Resolve client/server dietary preview and actual native use event interactions.
4. Coordinate single-JAR packaging and any DS native diet map/Packwiz metadata through the designated integration writer only. Owner authorization still required for full smoke or Prism QA.
