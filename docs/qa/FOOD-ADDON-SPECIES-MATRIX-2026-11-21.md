# TMM food/species compatibility matrix — corrected 2026-11-21

**Status:** source-audited **unindexed candidate**; no server smoke, Integration Assembly, Prism launch or gameplay acceptance. **This revision supersedes the incorrect 2026-11-21 forest-vegetarian/cave-raw-meat proposal.** The owner corrected the design: **Forest Dragons eat raw meat; Cave Dragons eat charred meat/seafood.** The source-authored active baseline already preserves native Forest raw meat. Do not remove those selectors.

## Recurring reversal root-cause analysis (owner report 2026-11-21)

This is the **second owner correction** of the same Forest/Cave inversion (also corrected on 2026-11-19). It was a contributor source-interpretation error, not an automated rollback of the in-pack data map:

1. The initial [`docs/DRAGON-DIETS-2026-11-14.md`](../DRAGON-DIETS-2026-11-14.md) document focused its Forest section and nutrition table on **new produce integrations**, omitting the baseline `#c:foods/raw_meat` selector even though the compiled data-map JSON retained it. The early [PR #46](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/46) description likewise emphasized Forest fruits and Cave charring, inviting the mistaken simplification "Forest = vegetarian".
2. The old `scripts/verify_dragon_diets.py` verified Forest fruit/sugar, Cave charred outputs and the absence of cooked meals but **did not assert Forest raw-meat admission or Cave raw-meat exclusion**. Its checks passed even if a contributor deleted native Forest meats and broadened Cave.
3. The normal quick `.github/workflows/verify.yml` metadata job **did not execute the diet verifier**, so these invariants had no merge-blocking test even for changes that might be statically detected.
4. Counting species `diet_entries.json` selectors (Cave 35 versus Forest 120) misrepresented the breadth of Cave **crafting inputs** through common cooked-meat/fish tags and native secondary charred recipes. A small direct-food selector list does not mean Cave is missing inputs.

**Prevention staged in this PR:** explicit locked species rules at the top of `AGENTS.md`, corrected canonical prose in `docs/DRAGON-DIETS-2026-11-14.md`, new raw-versus-charred assertions in the existing `scripts/verify_dragon_diets.py`, a candidate verifier for the new food addons, and a source-only quick-CI step invoking both. No active diet JSON, binary pin, compiled mod, full smoke, or world is changed by this root-cause work.

**Additional narrow audit item:** the old baseline verifier singles out `artifacts:everlasting_beef` as allowed for Cave but disallowed for Forest. This was inherited from earlier exception mapping and should be reviewed **separately** against the owner's "raw-meat dragon" intent; it is not evidence that Cave generally eats raw meat. Do not opportunistically flip magical-item exceptions in an unrelated food intake PR.

## Owner-selected mods and ownership

- **Next owner-authorized comprehensive unstable test inclusion attempt REQUIRED:** Huaxia Delight 1.2.0; Crispy! 1.1; Dungeon's Delight 1.5.3; Ender's Delight 1.3.1; and mandatory RunicLib 5.0.7.
- This is a narrow owner-directed exception to new-mod scope freeze #390, not permission to add further Delight mods.
- Selected candidate vendor release files and QA checklist: [FOOD-ADDON-INTAKE-2026-11-21.md](FOOD-ADDON-INTAKE-2026-11-21.md).
- Candidate Dragon Survival data map is **`docs/qa/candidates/dragon-diet-entries-2026-11-21.json`**, validated by **`scripts/verify_food_addon_diet_candidate.py`**. Neither is an active runtime path. Active `kubejs/data/dragonsurvival/data_maps/dragonsurvival/dragon_species/diet_entries.json`, original `scripts/verify_dragon_diets.py`, and Packwiz `index.toml`/`pack.toml` remain unchanged until the designated integration writer promotes and hashes the candidate atomically.

## Species policy (corrected)

| Physiology | Allowed foods and new-admission policy | Restrictions |
| --- | --- | --- |
| Ordinary human / nontransformed Monk or Hunter | All native edible foods and cookie variations with normal effects | No new diet bans |
| **Forest Dragon** | **Raw meat** (native `#c:foods/raw_meat` and its original individual meat/monster selectors); raw fruit, vegetables, mushrooms, berries, sugar; simple non-grain preserves/juices. Add 12 raw Dungeon's/Ender's monster meat items and two source-confirmed plant foods. | No ordinary cooked food, baked sweets, rice, grain dishes, or broad monster-meal tag. Special Dragon Survival native foods and explicit magical exceptions remain. |
| **Cave Dragon** | **Native charred meat/seafood** and original cave-specific foods/coal and explicit magical exceptions. Prepare raw -> cooked -> Dragon Survival charred, wherever native recipe/tag route exists. | Do **not** grant raw meat, raw fish, raw End-mob flesh or new raw Dungeon food as direct Cave nutrition. Do not add a direct raw + coal shortcut. |
| Sea Dragon | Raw aquatic fish/fillets, kelp, seaweed/seagrass, rice-free Aquaculture sushi; explicit magical exceptions. | No End Shulker meat, Ghast pseudo-calamari, normal cooked fish, rice sushi or broad monster meals. |
| JAWR Vampire | Existing unified blood-feeding rules govern hunger/healing | Red food, Bloody Mary cocktail and raw meat are not automatically blood |
| JAWR Werewolf | Existing JAWR carnivorous feeding rules | Proposed raw meat compatibility does not prove JAWR's hooks accept every new item; TeamLapen Werewolves tags remain retired |
| Lich / Archlich / Dracolich | Existing Goety / JAWR soul, essence, hunger, chosen body authorities | Don't create free nutrition, extra hunger pools or duplicated death anchors |
| Gilded (Progression) | Existing Nether-attainable or gold-ingredient recipe-chain eligibility | End food or monster meals are not automatically legal merely from name/location |

**General rules:** retain the source item's native effects; do not confer a universal magical-food exception just because an item has a positive buff. Preserve explicit existing magic-food exceptions (e.g. Botania Mana Cookie, EvilCraft Rejuvenated Flesh, Fruit of Grisaia). Don't indiscriminately include `#dungeonsdelight:monster_foods`, cooked food tags, or all of a mod's food IDs. These source classifications are not proof of runtime tag expansion.

## Verified new Forest selectors in the unindexed candidate

The 12 raw animal/monster food IDs below are registered in upstream 1.21.1 source, selected **for Forest only**, with `retain_effects: true`; they are intentionally **absent from Cave and Sea**:

| Mod | Raw Forest food items |
| --- | --- |
| Dungeon's Delight | `dungeonsdelight:silverfish_abdomen`, `dungeonsdelight:ghast_calamari`, `dungeonsdelight:ghast_tentacle`, `dungeonsdelight:spider_meat`, `dungeonsdelight:rotten_tripe`, `dungeonsdelight:brined_flesh`, `dungeonsdelight:gritty_flesh`, `dungeonsdelight:bogged_brain`, `dungeonsdelight:sniffer_shank` |
| Ender's Delight | `endersdelight:mite_crust`, `endersdelight:shulker_mollusk`, `endersdelight:shulker_filet` |

These are potentially dangerous monster ingredients; native food effects stay intact. An item may already enter Forest through a raw-meat tag, but explicit entries provide audited compatibility without admitting unexamined mixed monster meals.

Two additional source-confirmed **Forest plant** selectors (also absent from Cave/Sea):
- `dungeonsdelight:rotgourd_slice`: edible raw Rotgourd vegetable.
- `endersdelight:chorus_juice`: exact upstream shapeless recipe = chorus fruit + sugar + glass bottle; simple fruit drink.

**Original native selectors are restored in full**, including the `#c:foods/raw_meat` tag, vanilla rabbit, Farmer's Delight raw cuts, raw Twilight Forest meats, native Forest meat-and-berry recipes, and other source-native animal entries. The previous deletion of 75 entries was an error. Native `dragonsurvival:forest_dragon_treat` and other special foods remain.

### Source data-map counts

| Species | Existing indexed baseline | **Corrected candidate** | Change |
| --- | ---: | ---: | --- |
| Cave | 35 | **35** | No raw additions; native charred route preserved |
| Forest | 120 | **134** | +12 raw monster meats, +2 plant foods, no native removals |
| Sea | 164 | **164** | Unchanged |

## Cooking and provenance follow-ups without smoke

- **Charred path gap:** inspect which new raw Dungeon's/Ender's ingredients have valid cooked forms *and* are actually tagged `#c:foods/cooked_meat` or `#c:foods/cooked_fish` for Dragon Survival's native second-cook charring. For example, Dungeon's registers Smoked Spider Meat, but don't assume it is eligible for charring until its real recipe/tag is examined. Where unavailable, label a **design/recipe compatibility review** rather than granting raw Cave consumption or inventing a new recipe.
- **Huaxia Delight:** no guessed item IDs. Use the published JAR's actual food/item tags to distinguish raw meat/produce from wok-cooked food, rice/noodles/dumplings and other grain composites. Forest accepts verified raw meat and eligible produce, not finished cooked dishes. Cave still needs properly charred inputs.
- **Crispy!:** fortune cookie remains an ordinary baked cookie; Luck II does not automatically make it magical universal dragon nourishment. Verify effects, interactions with vanilla cookies and ProjectE EMC separately.
- **Ender's Delight:** `ender_paella`, `pearl_pasta`, `uncanny_cookies`, `strange_eclair`, `chorus_pie_slice`, curries and other cooked/grain dishes aren't automatically dragon legal. `ethereal_saffron`, `amberveil`, `chorusflame`, `voidpepper` are source-registered plant/block items without native food properties; don't equate crop status with direct edibility.
- **Dungeon's Delight:** monster meals (Monster Burger, Silverfish Fried Rice, Monster Muffin, cooked/smoked forms) need their own recipe review. Monster Pot and Dungeon Stove are separate specialty stations; don't bypass them with generic Kitchen/CFB/Occult storage automation. Audit spawner-material rewards, looting, unusual status effects and EMC loops.
- **Sea/BetterEnd:** check End biome vegetation placement, while Sea raw aquatic categories remain exclusively fish and aquatic plants. Shulker is not a Sea diet item by virtue of its shell.

## Verification scope and next-test checklist

- [x] **Source-ready** owner-corrected candidate data map and dedicated Python validation source; active Packwiz map and root hash untouched.
- [x] **Source-backed** 12 explicit raw IDs + 2 plant IDs; preserved all 120 original Forest, 35 Cave, 164 Sea selectors.
- [ ] **Integrator** retrieve and cryptographically hash all five official JARs, including RunicLib; inspect exact released Dungeon 1.5.3 JAR/version versus provider filename discrepancy and Ender's real metadata, then pin and reconcile source/packwiz hashes.
- [ ] **Integrator** promote this unindexed candidate to active runtime map and update `index.toml` and `pack.toml` in the same current-HEAD integration transaction; run existing `scripts/verify_dragon_diets.py` and candidate `scripts/verify_food_addon_diet_candidate.py` and quick CI/preflight.
- [ ] **Engineering review** full Huaxia/Crispy item/recipe census, new raw -> cooked -> charred route, tags and effects, and cross-mod kitchens.
- [ ] **B (next authorized QA):** verify pack load/registry, KubeJS+Dragon Survival map, no crash or invalid dynamic tags.
- [ ] **C (next authorized QA):** raw meats are nutritious to **Forest** but not **Cave**; Cave can eat native Charred Meat/Charred Seafood and cannot bypass with raw + coal; Sea can eat Aquaculture raw fish and rice-free sushi but no End Shulker/Ghast.
- [ ] **C (next authorized QA):** cooked foods, specialties, cookies, End crop generation, container returns, blood feeding, native werewolf/lich diets, Gilded gold provenance, multiplayer/reload and EMC/exploit balance.

**No smoke/gameplay test run or implied by these source updates.**

## Source references

- [Pack native Dragon food baseline](../DRAGON-DIETS-2026-11-14.md)
- [Dungeon's Delight item registry](https://github.com/Yirmiri/Dungeons-Delight/blob/1.21.1-v1.5/src/main/java/net/yirmiri/dungeonsdelight/core/registry/DDItems.java)
- [Dungeon's Delight item category tags](https://github.com/Yirmiri/Dungeons-Delight/blob/1.21.1-v1.5/src/main/java/net/yirmiri/dungeonsdelight/datagen/DDItemTagGen.java)
- [Ender's Delight item registry](https://github.com/Ax3dGaming/EndersDelight/blob/1.21.1/src/main/java/com/axedgaming/endersdelight/Common/Registries/EdItems.java)
- [Ender's chorus juice recipe](https://github.com/Ax3dGaming/EndersDelight/blob/1.21.1/src/main/resources/data/endersdelight/recipe/chorus_juice.json)
