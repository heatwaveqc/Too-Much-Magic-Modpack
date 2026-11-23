# Exact food JAR audit — 2026-11-22 (source-only)

Refs #439, #444, #463, #539 and food census PR #551. Full source evidence comes from [publisher-audit CI #45099517767](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/45099517767). No pack assembly, Minecraft boot, Prism, binary redistribution or runtime diet change.

## Measured results

**19/20 source binaries verified** (all 15 priority installed/pinned food JARs and four of five newly approved release candidates). Offline synthetic JAR tests 4/4 per matrix job. **2,748 native recipe JSON resources audited**, comprising 2,460 from the existing pinned food selection and 288 from four new candidates. These produced 1,946 and 240 unique candidate recipe output IDs respectively (may overlap; they are not confirmed edible IDs). The complete edible registry denominator remains unknown. Item-tag and recipe-output evidence, including multi-result cutting recipes, are retained in the CI JSON artifacts approved-food-jar-audit-pinned and approved-food-jar-audit-candidates.

## Verified release candidates — exact SHA1 from publisher JAR bytes

| Mod | Exact filename (not display title) | SHA1 | Native mod ID | Native recipe JSONs |
|---|---|---|---|---:|
| Crispy! 1.1 | crispy-1.1.jar | a5921b81094120294c9031f5749b91701ed44e94 | crispy | 59 |
| Dungeon's Delight (advertised 1.5.3) | neoforge-dungeonsdelight-1.21.1-1.5.2.jar | ee24aa33e9a2c5ec771e0548d8c85ea04ebc0d02 | dungeonsdelight | 203 |
| Ender's Delight 1.3.1 | endersdelight-1.3.1.jar | a3a9f7fd621f14461d37357463dbd16c67d2badf | endersdelight | 26 |
| RunicLib 5.0.7 | neoforge-runiclib-1.21.1-5.0.7.jar | f0422c70689d5b31b65ef5a84c1d5eb2d7536d4c | runiclib | 0 |

These four exact-source candidate metadata files are staged under integration/food-candidates/pins/, NOT installed in mods/ or indexed in Packwiz. The Dungeon's source archive filename ends in 1.5.2 even though the CurseForge release is labeled 1.5.3; that discrepancy must remain visible.

**Huaxia Delight 1.2.0 — HOLD.** Approved CurseForge file 8829017 / project 1685797 and filename huaxia-delight-1.21.1-1.2.0.jar are recorded, but the publisher's two official ForgeCDN download endpoints returned HTTP 403. No checksum or source registry has been invented. Acquire the exact publisher release via a legitimate accessible distribution method before constructing a Packwiz pin.

## Native dependency findings

Crispy requires NeoForge >=21.1.154. Dungeon's requires NeoForge >=21.1.219, Farmer's Delight >=1.3.0, RunicLib >=5.0.0. RunicLib requires NeoForge >=21.1.80. The selected baseline NeoForge 21.1.265 and Farmer's Delight 1.3.4 and candidate RunicLib 5.0.7 satisfy those documented floors. Ender's requires NeoForge >=21.1.219 and carries an unbracketed native Farmer's Delight dependency version string 1.21.1-1.2.10; its actual NeoForge resolver semantics versus FD 1.3.4 require a compatibility check before claiming runtime readiness.

## Already pinned food JAR census, 15/15 byte verified

Farmer's Delight (333 recipes), Aquaculture (63), Aquaculture Delight (45), Pam Food Core (218), Pam Food Extended (884), Pam Crops (177), Pam Trees (77), My Nether's Delight (133), Nether Depths Upgrade (24), Dragon Survival (166), Brewin' and Chewin' (74), Cocktails Delight (178), Occult Cooking (2), Let's Do Brewery (53), HerbalBrews (33).

Occult Cooking's CurseForge CDN returned 403; its author's official Modrinth version 1.0.1 provided the *same exact file* by SHA1 456458c179b1633bf4cf4aab7783bf7eedb1ffa2, matching the existing Packwiz pin.

## Remaining implementation and test queue

- **A implemented:** exact publisher binary hashes/ZIP CRC and native metadata, JSON recipes, item tags and loot resources; four new candidate source pins; two parallel source-only audit artifact sets. Dynamic KubeJS and actual registry remain out of scope for this source-only evidence pass.
- **A pending:** Huaxia publisher release retrieval; reconcile the CI report into the graph; classify families, preparations and source-unit yields; resolve all recipe alternatives and ambiguous same-ID outputs; update the old five-stage graph to RAW/COOKED/CHARRED; safely generate tag and PR #539 evidence records. Do not mistake recipe outputs for edible items.
- **Integration writer only:** reconcile the five approved mods and runtime diet with canonical manifest, Packwiz hashes and source locks. PR #439's superseded historical diet map must not be promoted.
- **B/C pending owner-authorized game:** verify true item registry/FoodProperties, native-only consumables, ingredient/output amounts and cutting, native dependency loading, special effects, reload, client parity and multiplayer. No gameplay PASS.

No additional design choice is needed for the census. Source inability or ambiguous nutrition must stay explicitly held.
