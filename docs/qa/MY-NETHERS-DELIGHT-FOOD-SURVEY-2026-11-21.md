# My Nether's Delight 1.10.4.1 food provenance — TMM source audit (2026-11-21)

**Status:** Source-based, **unindexed candidate only** on PR #439; **not** an integration-ready runtime verdict, client/server smoke, or gameplay approval. **Parent owner design:** [#444](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/444). Relevant target tags: `#tmm:foods/nether`, `#tmm:foods/golden`, `#tmm:foods/magical`.

## Pack verification

In `test/full-integration` and this PR's base, `manifest/mods.json` contains **enabled** `my-nethers-delight` / `MyNethersDelight-1.21.1-1.10.4.1.jar`. `mods/my-nethers-delight.pw.toml` pins CurseForge **project 1003673, file 8854319**, SHA1 `9c54439d65e77c7b4b711831138f81f3a849ff35`, `side = "both"`; `index.toml` includes this metafile. This verifies **Packwiz inclusion**, not a successfully tested Minecraft world.

**Primary recipe and food-source material:** official [SoyTutta/MyNethersDelight, `Neo-1.21+`](https://github.com/SoyTutta/MyNethersDelight/tree/Neo-1.21%2B), especially `src/generated/resources/data/mynethersdelight/recipe/` (crafting, cooking and cutting), `src/main/java/com/soytutta/mynethersdelight/common/registry/MNDItems.java`, `.../utility/MNDFoodValues.java`, `.../data/MNDItemTags.java`. Farmer's Delight [1.21 recipe source](https://github.com/vectorwing/FarmersDelight/tree/1.21/src/generated/resources/data/farmersdelight/recipe). Additional [1.21.1 recipes and items index](https://mcmodwiki.com/my-nethers-delight/1.21.1), whose extracted snapshot is **1.10.4**, so exact pinned 1.10.4.1 source/JAR parity is still a separate engineering check.

## 61 item-class food registrations inspected

Survey checked 61 food-component item IDs in `MNDItems.java` in the current `Neo-1.21+` source branch (including potions/effect food with special items). **46 source-backed Nether candidates**, **13 requiring imported/non-Nether ingredients**, **2 additional provenance/handling checks**. This counts normal food items and served `plate_of_*` portions, **not** separately the feast block items (which need serving adapters), nor non-edible culinary ingredients.

### Candidate: Nether food (46 IDs, add only in unindexed staging tag until parity review)

Registry namespace `mynethersdelight:`; each ID below retains its native food values/effects for Gilded unless also Golden.

- **Strider and its eggs**: `strider_egg`, `strider_slice`, `minced_strider`, `bleeding_tartar`, `strider_with_grilled_fungus`, `strider_stew`, `plate_of_striderloaf`, `boiled_egg`, `deviled_egg`, `scotch_eggs`, `egg_soup`. Strider eggs support egg and cooked-egg recipe tags; Farmer's Delight `nether_salad` needs only crimson/warped fungus and a wooden bowl. `egg_soup` uses onions, which the mod makes available through a low-probability **Ghast** drop; verify compiled loot table.
- **Hoglin and foods with Nether-compatible bread**: `hoglin_loin`, `hoglin_sausage`, `roasted_sausage`, `cooked_loin`, `hotdog`, `hotdog_with_nether_salad`, `breakfast_sampler`, `nether_burger`, `blue_tenderloin_steak`, `red_loin_on_a_stick`, `roast_ear`, `plate_of_stuffed_hoglin_snout`, `plate_of_stuffed_hoglin_ham`, `plate_of_stuffed_hoglin`. Nether-compatible bread is crafted from Ghast-based `ghast_dough`/`ghast_sourdough`; `breakfast_sampler` offers a **Strider Egg instead of Honey** and uses Nether-compatible egg/bread options. Stuffed Hoglin uses the Farmer's Delight ham supplied from knife-related Hoglin drops and Nether-grown fungus; confirm exact pinned loot table.
- **Ghast-based cuisine**: `ghasta`, `twisted_ghasta`, `giant_takoyaki`, `fries_ghasta`, `spicy_noodle_soup`, `spicy_cotton`, `plate_of_ghasta_with_cream`, `ghast_dough`, `ghast_sourdough`, `slices_of_bread`, `toasts`. The mod's own Ghasta/Ghasmati ingredients satisfy its rice/pasta/dough tags, and Ghasts can drop Farmer's Delight onion. A **Nether-compatible recipe path** qualifies an item type, even if other routes import Overworld ingredients.
- **Native spicy, lava, magma, gold foods**: `bullet_pepper`, `spicy_skewer`, `chilidog`, `rock_soup`, `burnt_roll`, `magma_cake_slice`, `hot_cream`, `hot_cream_cone`, `golden_egg`, `enchanted_golden_egg`. The mod yields Farmer's Delight Straw from cutting its Nether **Powdery Cannon** wood; no Overworld straw needed for Magma Cake. A verified raw Hoglin/Strider ingredient can satisfy `burnt_roll`'s broad raw meat tag. `enchanted_golden_egg` occurs as Nether Bastion loot.

**Important special native food values:** `hot_cream` is a very-high-saturation custom drink (nutrition 1, modifier 8.0); `spicy_cotton` nutrition 3, modifier 2.25; `strider_egg` edible, including when full; the two Golden Eggs have **4 nutrition/1.0 modifier** and custom effects. Do not lose these through blanket dragon diet-map overrides or allow item-use bonus to replay their effects.

### Exclude from Nether tag (13 IDs; remain usable through other diet permission)

These **explicitly require non-Nether inputs** under the pack's Nether-provenance definition even though each dish is added by a Nether-themed mod:

| Item (prefix `mynethersdelight:`) | Non-Nether ingredient or dependency |
| --- | --- |
| `crimson_stroganoff` | Milk |
| `hotdog_with_mixed_salad` | Farmer's Delight mixed salad: tomato and beetroot |
| `sausage_and_potatoes` | Potato |
| `fried_hoglin_chop` | Wheat **and** milk (also eggs) |
| `ghast_salad` | Carrot |
| `dried_ghast_with_milk` | Milk |
| `sizzling_pudding` | Milk |
| `tear_popsicle` | Ice |
| `stuffed_pepper` | Milk |
| `spicy_hoglin_stew` | Potato and carrot |
| `hot_wings` | Overworld raw chicken |
| `hot_wings_bucket` | Derived from `hot_wings` |
| `spicy_curry` | Milk (its rice/pasta may otherwise be Nether-sourced) |

**These are not blocked for everyone**: they remain ordinary items, and may still be allowed by other food authorities, a valid Golden recipe branch, or a future explicit Magical tag choice. Do not infer Nether eligibility from mod ID or recipe serializer.

### Hold for small source-parity check (2 IDs)

- `plate_of_cold_striderloaf`: Nether ingredient ancestry is plausible, but validate how the cold feast is produced and whether cooling requires imported water/ice or is merely a temperature state before asserting a Nether-obtainable path.
- `bacon-wrapped_sausage_on_a_stick`: needs Farmer's Delight **cooked bacon**; verify the entire Hoglin -> native obtainable ham -> porkchop -> bacon -> cooked bacon route in the pinned pair rather than trusting conceptual similarity.

### Other edible-when-placed and nonfood caveats

**Feast blocks** such as `mynethersdelight:striderloaf`, `mynethersdelight:cold_striderloaf`, `mynethersdelight:ghasta_with_cream`, `mynethersdelight:bread_loaf`, `mynethersdelight:roast_stuffed_hoglin`, `mynethersdelight:magma_cake_block` are **not** necessarily normal food-component stack consumables; serving and plate slices require a source-aware gameplay gate and registry ID audit. Some block IDs on web pages differ from in-source item registry IDs; source wins. Also `ghasmati`, `pepper_powder`, `raw_stuffed_hoglin`, and `hoglin_hide` are ingredients/craft steps rather than automatically edible player food.

**Cross-diet tag trap:** My Nether's Delight places **Strider Slice and Ghasta into an upstream `c:foods/raw_fish` tag** for other integrations, even though those are lava-walking Strider/Ghast products, **not marine seafood**. TMM's **Sea Dragon** 6/2.0 preferred-fish rule must not trust this tag without a source-backed **marine-origin filter**, and Cave/other terrestrial decisions must use its independent taxonomy. This is a **code/QA guard**, not an owner-design question.

### Golden eligibility and existing native tag

The source's `src/generated/resources/data/c/tags/item/foods/golden.json` already contains **`mynethersdelight:golden_egg` and `mynethersdelight:enchanted_golden_egg`**. The former has an explicit recipe with **8 gold ingots around `#c:foods/boiled_egg`**, the latter is Bastion loot; both are `minecraft:piglin_loved`. They therefore **qualify for Gilded's Golden 2× hunger and 2× saturation POINTS bonus**, and are independently Nether-eligible. Stage both in `docs/qa/candidates/golden-foods-item-tag-2026-11-21.json`; let the future live Gilded tag nested-reuse `#c:foods/golden` where supported. They **do not expand Gilded initiation foods** (remains vanilla golden apple/enchanted golden apple/golden carrot + separately approved variants).

**Enchanted Golden Egg as universal Magical** is a plausible follow-up, due to exceptional effect payload and `enchanted` status, but the canonical Magical tag is explicit/curated and **this audit does not silently enroll it as universally edible**. Owner can choose this separately. Golden itself is Gilded-specific, not universally edible.

### Migration work, no game build authorization

- [x] Source-only Nether candidate list staged (4 vanilla + 46 My Nether's Delight IDs) at `docs/qa/candidates/nether-foods-item-tag-2026-11-21.json`.
- [x] Source-only Golden candidate updated with 2 Golden Egg IDs at `docs/qa/candidates/golden-foods-item-tag-2026-11-21.json`.
- [ ] Confirm exact **pinned 1.10.4.1** JAR recipe item/tag/loot parity; especially Ghast onion drop, Strider egg alternatives, Nether bread route, Nether ham availability, and ingredient tag expansion. Clear 2 holds or add explicit notes.
- [ ] Audit edible feast blocks/placed-serving events separately.
- [ ] Integrate canonical `#tmm:foods/nether` and `#tmm:foods/golden` with legacy `tmm_progression:gilded_nether_foods` and `gilded_gold_foods` as transitional aliases, not duplicate authorities, through the designated writer. Do not alter current indexed recipes or Packwiz hashes from this isolated source-only review.
- [ ] Test Golden Egg 2× once, enchanted Golden Egg original potion effects applied once (and all native custom negative-effect conversion) plus Gilded + universal Magical overlap.
- [ ] Guard against Sea Dragon mistakenly inheriting Strider/Ghasta as marine raw-fish preferred, and against nonedible Nether materials accidentally receiving food values.

**Acceptance:** static source-backed classification is **not** in-game eligibility acceptance. No game boot, client smoke, Integration Assembly, or stable/release authorization was granted.
