# Source-audited food / species matrix — four Delight selections (2026-11-21)

**Status: SOURCE-AUTHORED, NO SMOKE TEST.** This record reconciles selected Huaxia Delight 1.2.0, Crispy! 1.1, Dungeon's Delight 1.5.3, Ender's Delight 1.3.1 with the current TMM Dragon Survival 2.0.74 data-map override, JAWR/lich, and Gilded food policies. This is not proof of actual diet consumption, loader compatibility, complete recipe graph, End worldgen or gameplay. RunicLib >=5.0.0 is mandatory for Dungeon's. Owner explicitly requires an inclusion attempt for **all four selected food mods plus RunicLib** in the next owner-authorized comprehensive unstable test. This is a narrow exception to #390. The integration writer must still hash-pin binaries and reconcile the shared next-test register, active Packwiz metadata and source locks.

## Policy precedence and source evidence

1. **TMM approved physiology** overrides permissive third-party default diets: Cave raw meat/fish and native charred foods; Forest raw produce/mushrooms/sugar and simple plant products only; Sea raw aquatic animal/plant food and rice-free sushi; no grain dishes or ordinary cooked meals for any dragon. Explicit magic-food allowlist remains universal. This is not a new decision.
2. The in-pack replacement at `kubejs/data/dragonsurvival/data_maps/dragonsurvival/dragon_species/diet_entries.json` and `kubejs/server_scripts/dragon_diets.js` are the **source** for dragon selectors. Existing source doc `docs/DRAGON-DIETS-2026-11-14.md` establishes the native char recipes, magical exceptions and actual diet properties.
3. External code examined (source, NOT verified exact published binary contents):
   - [Dungeon's registry](https://github.com/Yirmiri/Dungeons-Delight/blob/1.21.1-v1.5/src/main/java/net/yirmiri/dungeonsdelight/core/registry/DDItems.java), [food properties](https://github.com/Yirmiri/Dungeons-Delight/blob/1.21.1-v1.5/src/main/java/net/yirmiri/dungeonsdelight/common/util/DDProperties.java), [item categories](https://github.com/Yirmiri/Dungeons-Delight/blob/1.21.1-v1.5/src/main/java/net/yirmiri/dungeonsdelight/datagen/DDItemTagGen.java), [dependencies](https://github.com/Yirmiri/Dungeons-Delight/blob/1.21.1-v1.5/src/main/resources/META-INF/neoforge.mods.toml).
   - [Ender's registry](https://github.com/Ax3dGaming/EndersDelight/blob/1.21.1/src/main/java/com/axedgaming/endersdelight/Common/Registries/EdItems.java) (default branch source inspected), [food values](https://github.com/Ax3dGaming/EndersDelight/blob/1.21.1/src/main/java/com/axedgaming/endersdelight/Common/Registries/EdFoods.java), [chorus juice recipe](https://github.com/Ax3dGaming/EndersDelight/blob/1.21.1/src/main/resources/data/endersdelight/recipe/chorus_juice.json) and [Shulker filet cutting recipe](https://github.com/Ax3dGaming/EndersDelight/blob/1.21.1/src/main/resources/data/endersdelight/recipe/cutting/shulker_filet.json).
   - Huaxia 1.2.0 [official release](https://www.curseforge.com/minecraft/mc-mods/huaxia-delight/files/8829017); Crispy! 1.1 [official release](https://www.curseforge.com/minecraft/mc-mods/crispy/files/6447126). No published-JAR registry item census yet; no invented explicit item selectors.
4. Whole-recipe ingredient provenance outranks named category, effect, culinary region or dimension. Do not whitelist `#dungeonsdelight:monster_foods`, `#c:foods/cooked_meat`, `#c:foods/cooked_fish`, baked foods, all End foods or all Huaxia dishes.

## Effective physiology policy matrix

| Body/path | New additions | Default treatment / boundary |
| --- | --- | --- |
| Human/Monk/Hunter (ordinary physiology) | All native edible dishes and cookie types | No added restrictions; retain native harmful or positive effects. Monk food-discovery events use actual registry ID; no free mastery aliases. |
| Cave Dragon | Listed raw Dungeon's creature foods and raw Ender's Shulker/endermite pieces | Allowed **raw** with original effects; conventional cooked Monster Pot meals are not auto-allowed. Native cooked -> charred crafting remains a distinct process, not a direct raw+coal shortcut. |
| Forest Dragon | Raw Rotgourd Slice, chorus-fruit juice (fruit+sugar), standard raw fruit/vegetable tags filtered by pack | **No** raw monster meat, End Shulker meat, cakes, cookies, noodles, rice, dumplings, cooked curries or pies. Existing soy/tofu faux-meat ingredients are plant-based and still eligible if individually supported. |
| Sea Dragon | None of the newly source-inspected raw foods qualify | Keep Aquaculture raw fish/sushi (no rice), actual aquatic plants, and existing magic exceptions. End Shulker mollusk is NOT ocean seafood; ghast calamari is NOT a marine catch. |
| JAWR Vampire | Existing blood/bottled-blood compatibility owns hunger/feeding | None of these four mods is assumed to provide real blood; 'Bloody Mary', raw meat or red foods do not count as blood by name. |
| JAWR Werewolf | Existing transformation-specific carnivorous diet | Raw animal foods are *candidates for the JAWR diet adapter*, **not proven allowed** by vanilla `c` tags alone. Do not add retired TeamLapen Werewolves tags. |
| JAWR/Goety Lich, Archlich | Current undead metabolism/hunger authority | Do not add ordinary food healing or bypass phylactery/Arca mechanics via generic food override. Food items may retain native buffs only if class can legally consume them. |
| Dracolich | Dragon body plus one lich nutrition authority | New raw/plant categories should respect the already-selected body without creating second hunger/essence pools. Requires runtime acceptance; no new hybrid changes. |
| Gilded (separate Progression path) | No blanket grant | Only foods obtainable from Nether OR crafted with gold ingredient/valid gold recipe chain qualify. Dungeon's Nether monster meals could qualify if the *actual* recipe/provenance does; End cooking alone does not. Gilded adapter is owned by Progression, not this dragon map. |

## Source-confirmed selectors added to pack Dragon Survival map

These are **exact registered items**, each explicit selector with `retain_effects: true`. No custom nutrition overrides; preserve native food properties/effects. Their presence in a source file **does not prove a live item is present** until the verified published binary is installed.

| Item | Cave | Forest | Sea | Rationale |
| --- | --- | --- | --- | --- |
| `dungeonsdelight:silverfish_abdomen` | Allow | Deny | Deny | Raw insect body |
| `dungeonsdelight:ghast_calamari`, `dungeonsdelight:ghast_tentacle` | Allow | Deny | Deny | Raw Ghast flesh, not aquatic fish despite 'calamari' |
| `dungeonsdelight:spider_meat` | Allow | Deny | Deny | Raw spider body; cooked smoked form excluded |
| `dungeonsdelight:rotten_tripe`, `dungeonsdelight:brined_flesh`, `dungeonsdelight:gritty_flesh` | Allow | Deny | Deny | Raw/foul animal or monster tissue; retain negative effects |
| `dungeonsdelight:bogged_brain` | Allow | Deny | Deny | Monster organ; negative effects preserved |
| `dungeonsdelight:sniffer_shank` | Allow | Deny | Deny | Raw animal meat; cooked Sniffer Shank excluded |
| `endersdelight:mite_crust` | Allow | Deny | Deny | Raw endermite-derived body food |
| `endersdelight:shulker_mollusk`, `endersdelight:shulker_filet` | Allow | Deny | Deny | End-mob meat tagged `c:foods/raw_meat`, nausea properties preserved |
| `dungeonsdelight:rotgourd_slice` | Deny | Allow | Deny | Confirmed edible plant slice |
| `endersdelight:chorus_juice` | Deny | Allow | Deny | Source recipe chorus fruit + sugar + glass bottle; contains no grain/animal component |

### Pre-existing Forest Dragon policy repair

The override was inadvertently carrying 75 native animal/monster selectors, including `#c:foods/raw_meat`, vanilla rabbit, Farmer's Delight meats, Twilight raw venison, Born in Chaos corpse maggot and *meat*-containing Dragon Survival specialties. This directly contradicted the approved Forest plant-only diet and would have admitted the new raw meats via `#c:foods/raw_meat` even without the above selectors. All 75 have now been removed from **Forest only**. Original counts: Cave 35, Forest 120, Sea 164. New source counts: **Cave 49, Forest 47, Sea 164** (12 explicit raw monster/End items and two common raw meat/fish selectors added to Cave). Native magical explicit entries, supported plants/preserves, soy imitation, and Sea food entries are preserved. Never restore the upstream whole replacement list on a future mod update without applying the pack-specific Forest exclusion policy.

## Not automatically allowed (important regressions)

- **Dungeon's**: Monster Burger, Silverfish Fried Rice, Monster Muffin, Smoked Spider Meat, Fried Ghast Calamari, mixed Monster Pot meals, rice balls and `#dungeonsdelight:monster_foods`. Recipe/entity contents and cooking methods vary; a broad monster-food tag would violate Forest plant-only and Cave raw-only rules. Custom Monster Pot recipes and Dungeon Stove do not enter ordinary Cooking for Blockheads pot handlers automatically.
- **Ender's**: Uncanny Cookies, Chorus Pie Slice, Strange Eclair, Pearl Pasta, Ender Paella, Twisted Cereal, curries and other cooked composites. Actual edible `endersdelight:chorus_juice` is a narrowly verified exception. `endersdelight:ethereal_saffron`, `amberveil`, `chorusflame`, `voidpepper` register as *plant/block items without FoodProperties*; not separately edible merely because they are garden products. Check native Ender Phasing effect and expensive dishes when balancing quests.
- **Huaxia**: Wok dishes and common grain-based dumplings/rice/noodles are human cuisine, not blanket dragon foods; separately check any raw produce or tofu against exact pinned 1.2.0 JAR and recipe. Only eligible raw produce tagged by common food tags may flow into Forest after the existing filtered KubeJS collector; word-based filters are insufficient proof of full ingredient provenance.
- **Crispy!**: Even its Luck II Fortune Cookie is ordinary baked confectionery, **not** an automatically sanctioned magical Dragon exception. Do not make all cookies universally edible because they can provide effects. Review Fortune Cookie Luck II and ProjectE EMC value/recipe exploitability separately.
- **Sea**: no End meat, raw ghast pseudo-calamari, normal cooked fish, or rice sushi. **Gilded**: no automatic gold permission merely because a name contains 'golden' or a dish has a buff; inspect actual recipes/chain.

## Implementation-ready checks vs outstanding test work

- [x] Source-ready: selected desired/inactive roster for all four food addons + required RunicLib; exact provider release candidates identified, awaiting trusted JAR hashes.
- [x] Source-ready: named 12 Cave and 2 Forest selectors from upstream registry/recipes, retained effect behavior; no guessing across Huaxia/Crispy.
- [x] Source-ready: corrected 75 inherited Forest meat selectors and added targeted static guard script assertions (not yet claiming CI PASS).
- [ ] Integration writer: confirm exact official 1.5.3 Dungeon JAR version mismatch between release and file detail/Modrinth published filename; verify SHA, mod IDs and RunicLib >=5.0.0; add immutable Packwiz metadata, preserve all live source ownership. Ender's 1.3.1 release notes say cutting recipe fixed; audit actual JAR recipe format.
- [ ] Owner-design blocker: **none** for these additive native-food policies; new magical exceptions, spawner reward/buff nerfs or bulk ingredient replacement must come back to the owner only if shown necessary.
- [ ] Engineering inspection: audit pinned full Huaxia/Crispy registries and all relevant exact recipes after retrieving vendor binaries; check KubeJS `#c:foods/vegetable` filtered expansion for grain composites, Pam overlaps, transformed diet and snack replacement interactions.
- [ ] **B, next authorized test build**: server plus client load mod IDs/dependencies/recipes, inventory/JEI; `/reload` without errors and source-consistent tag expansion. Run all species positive and *negative* items against Dragon Survival native consumption handling.
- [ ] **C, next authorized test build**: cook Monster Pot, Dungeon Stove, Cutting Board, Wok, standard pot, cookies, and native End dishes; CFB/Occult storage without station bypass; verify container/remainder returns and multiplayer.
- [ ] **C, next authorized test build**: test BetterEnd native biomes vs Ender's planted crop generation, Dungeon's mob loot and spawner-stained scrap rewards/EMC, native food effects, Forest soy and chorus juice, Sea sushi.
- [ ] **B/C, next authorized test build**: JAWR vampire blood, werewolf meat diet, undead hunger/essence and Gilded Nether/gold recipe chains remain unchanged; ensure no independent magic-buff shortcuts or XP/MXP grants. Test both transformation and ordinary human controls.

**QA vocabulary:** A = static/source inspection/checks; B = runtime commands; C = manual gameplay. No server smoke, Integration Assembly, Prism package or live gameplay verification performed or authorized by this change.
