# Exact food JAR audit — 2026-11-22 (source-only)

Refs #439, #444, #463, #539 and food census PR #551. Full source evidence comes from [publisher-audit CI #45099517767](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/45099517767). No pack assembly, Minecraft boot, Prism, binary redistribution or runtime diet change.

## Measured results

**20/20 source binaries inspected** (15 priority indexed food JARs, four official publisher downloads, and one owner-uploaded Huaxia JAR). The first **19** have publisher/download provenance; Huaxia has independently inspected uploaded-file evidence but **no publisher checksum corroboration**. Offline synthetic JAR tests 4/4 per matrix job. **2,819 native recipe JSON resources audited**, comprising 2,460 from the existing pinned food selection, 288 from four publisher additions, and 71 from the owner-uploaded Huaxia artifact. These produced 1,946 and 240 unique candidate recipe output IDs respectively (may overlap; they are not confirmed edible IDs). The complete edible registry denominator remains unknown. Item-tag and recipe-output evidence, including multi-result cutting recipes, are retained in the CI JSON artifacts approved-food-jar-audit-pinned and approved-food-jar-audit-candidates.

## Verified release candidates — exact SHA1 from publisher JAR bytes

| Mod | Exact filename (not display title) | SHA1 | Native mod ID | Native recipe JSONs |
|---|---|---|---|---:|
| Crispy! 1.1 | crispy-1.1.jar | a5921b81094120294c9031f5749b91701ed44e94 | crispy | 59 |
| Dungeon's Delight (advertised 1.5.3) | neoforge-dungeonsdelight-1.21.1-1.5.2.jar | ee24aa33e9a2c5ec771e0548d8c85ea04ebc0d02 | dungeonsdelight | 203 |
| Ender's Delight 1.3.1 | endersdelight-1.3.1.jar | a3a9f7fd621f14461d37357463dbd16c67d2badf | endersdelight | 26 |
| RunicLib 5.0.7 | neoforge-runiclib-1.21.1-5.0.7.jar | f0422c70689d5b31b65ef5a84c1d5eb2d7536d4c | runiclib | 0 |

All five candidate metadata files are staged under integration/food-candidates/pins/, NOT installed in mods/ or indexed in Packwiz. Huaxia's staged SHA is taken only from the supplied binary, whereas the other four are independently retrieved publisher SHA1 values. The Dungeon's source archive filename ends in 1.5.2 even though the CurseForge release is labeled 1.5.3; that discrepancy must remain visible.

**Huaxia Delight 1.2.0 — uploaded-source audit complete; independent publisher checksum remains a separate hold.** User supplied the exact approved filename; ZIP CRC passed and SHA1/SHA512 were computed from bytes. Native dependencies are Minecraft [1.21.1], NeoForge [21.1.192,21.2), Farmer's Delight [1.2.9,1.4); optional Butchercraft [2.6.5,2.7). No required new dependency. Its 71 recipes include 22 Farmer's Delight cooking, **10 Farmer's Delight cutting with nested result-list shapes**, 18 native processing (17 wok/1 barrel), 17 shapeless and four shaped; 54 item-tag files, seven loot-table files, 64 distinct output candidates, seven multiple-route outputs and 11 conditional recipes. Cutting yields include four chopped scallions per source and two dough wrappers per source; source-based nutrition must respect those yields. The original binary is not committed; source audit is regenerable with `python3 scripts/audit_approved_food_jars.py --scope candidates --local-jar huaxia-delight=/path/to/huaxia-delight-1.21.1-1.2.0.jar --json-out /tmp/food-audit.json`. That command does not make the publisher's CDN accessible.

## Native dependency findings

Crispy requires NeoForge >=21.1.154. Dungeon's requires NeoForge >=21.1.219, Farmer's Delight >=1.3.0, RunicLib >=5.0.0. RunicLib requires NeoForge >=21.1.80. The selected baseline NeoForge 21.1.265 and Farmer's Delight 1.3.4 and candidate RunicLib 5.0.7 satisfy those documented floors. Ender's requires NeoForge >=21.1.219 and carries an unbracketed native Farmer's Delight dependency version string 1.21.1-1.2.10; its actual NeoForge resolver semantics versus FD 1.3.4 require a compatibility check before claiming runtime readiness.

## Already pinned food JAR census, 15/15 byte verified

Farmer's Delight (333 recipes), Aquaculture (63), Aquaculture Delight (45), Pam Food Core (218), Pam Food Extended (884), Pam Crops (177), Pam Trees (77), My Nether's Delight (133), Nether Depths Upgrade (24), Dragon Survival (166), Brewin' and Chewin' (74), Cocktails Delight (178), Occult Cooking (2), Let's Do Brewery (53), HerbalBrews (33).

Occult Cooking's CurseForge CDN returned 403; its author's official Modrinth version 1.0.1 provided the *same exact file* by SHA1 456458c179b1633bf4cf4aab7783bf7eedb1ffa2, matching the existing Packwiz pin.

## Remaining implementation and test queue

- **A implemented:** exact publisher binary hashes/ZIP CRC and native metadata, JSON recipes, item tags and loot resources; four new candidate source pins; two parallel source-only audit artifact sets. Dynamic KubeJS and actual registry remain out of scope for this source-only evidence pass.
- **A pending:** Independently reconcile the uploaded Huaxia SHA with the official publisher if accessible; ingest both CI reports and uploaded-source report into the graph; classify families, preparations and source-unit yields; resolve all recipe alternatives and ambiguous same-ID outputs; update the old five-stage graph to RAW/COOKED/CHARRED; safely generate tag and PR #539 evidence records. Do not mistake recipe outputs for edible items.
- **Integration writer only:** reconcile the five approved mods and runtime diet with canonical manifest, Packwiz hashes and source locks. PR #439's superseded historical diet map must not be promoted.
- **B/C pending owner-authorized game:** verify true item registry/FoodProperties, native-only consumables, ingredient/output amounts and cutting, native dependency loading, special effects, reload, client parity and multiplayer. No gameplay PASS.

No additional design choice is needed for the census. Source inability or ambiguous nutrition must stay explicitly held.


## Huaxia source identity and provenance details (owner upload)

- **Archive:** 221,894 bytes; ZIP CRC PASS; JAR metadata `META-INF/neoforge.mods.toml` says `huaxiadelight` `1.2.0`, `javafml` loader `[4,)`. Internal resource pack supports 1.21.1.
- **SHA1:** `8c4eb34973a6f2ca5d0aac29e1bdff065bfc6919`
- **SHA512:** `38f20f8860809bf37b58ade41423480dcd09b17d30da5795b69b91a3be4f2befe6259218ae22ecd91bb7daa2b8e1c3080bb97cbe74de4162358ad541fde4ee33`
- **Source status:** genuine readable, self-consistent local uploaded JAR; not independent proof of identity with official CurseForge file 8829017. The source-only pin hash has exactly this provenance.
- **Important graph holds:** Seven recipe-output IDs have multiple production routes. In particular `huaxiadelight:cooking_oil` comes from alternative recipes returning **one or two** units, and `huaxiadelight:dough_wrapper` comes from routes yielding **one or two** units. Neither can get an unconditional global `source_units` value without proving equivalent per-item nutrition. `huaxiadelight:processing` also distinguishes wok-heated dishes from barrel-fermented cooking wine; do not auto-label everything raw/cooked from recipe type alone.
- **Raw source evidence:** locally generated structured JSON inventory holds all 71 native recipe paths, inputs, outputs, yield counts, recipe type/station/tool, 54 item-tag expansions, seven loot-table paths and conditional recipe declarations. Its source can be independently regenerated from the uploaded binary; no copyrighted binary is stored in git.


### Source-declared edible registrations (native bytecode, not a game boot)

Read-only `javap -c -p dev.huaxiadelight.Content` on the exact uploaded SHA1, cross-checked against `HuaxiaDelight.ingredient` and its `lambda$ingredient$5` item factory:
- **57 direct ingredient registrations**: **46 have positive hunger and receive actual `DataComponents.FOOD`** in the item constructor; **11** have hunger zero and therefore do *not* receive that component (e.g. cooking wine, oil, flour, unbaked wrappers and raw baozi).
- **Five crop registrations** (`chili`, `soybean`, `ginger`, `garlic`, `scallion`) call the same `ingredient(name,1,0.2,false)` and contribute **five additional native food items**.
- **51 source-declared registered native food items total**, distinct from the 64 distinct *recipe output candidates*. The 51 is exact for the inspected registration code, **not** authoritative proof of the assembled game's expanded registry.
- **29 source-declared food items attach native Regeneration (160 ticks) through the FoodProperties builder.** The shared food handler must preserve these native effects, as well as the return-bowl/return-bottle semantics, while replacing only allowed dietary nutrition. Do not treat regeneration as an automatic universal Magical-food permission.
- Source-only tables of all 51 food IDs and their original native FoodProperties are available in the owner audit export, not in a gameplay nutrition matrix; our rule engine still uses configurable families, source ratios and preparation stages.

This fills the Huaxia source census without promoting any gameplay change, and avoids mistaking `cooking_wine` or `raw_baozi` for directly food-component-backed edible items. The separate complete same-build registry denominator remains pending.
