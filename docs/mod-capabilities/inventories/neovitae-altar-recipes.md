# Neo Vitae — all 31 generated Ara Vitae recipes at source 1.1.53

**Scope:** full listing of **31 generated `neovitae:ara_vitae_recipe` JSON files** under [frozen source directory](https://github.com/breakinblocks/NeoVitae/tree/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/recipe/ara_vitae), at `breakinblocks/NeoVitae@2744123bb6e4f72c602178b23384be41956eef23`. This is **source-generated recipe data**, not extracted from the pin-hashed Modrinth JAR; effective pack datapacks may override inputs/rates. No client test in this pass.

`minTier` is the **native altar level 0–5**, `bloodNeeded` is total Essentia Vitae per completed conversion, `craftSpeed` is altar expenditure per tick (before rune modifiers and actual transfer bottlenecks), and `drainSpeed` is progress loss per tick on loss of supply per source `AraVitaeRecipe.java`. **Do not read drainSpeed as an additional recipe cost.** Copy-components applies only where indicated.

The input `#neovitae:vitae_stone` currently contains only `minecraft:deepslate` in [generated item tag](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/tags/item/vitae_stone.json), and the `#c:` entries are forge common item tags whose actual membership must be checked in the assembled pack.

| Source recipe file | Minimum altar | Input (single item or tag) | Output | Total EV | Craft EV/tick | Depletion progress loss/tick | Special |
|---|---|---|---|---:|---:|---:|---|
| [`air_tool.json`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/recipe/ara_vitae/air_tool.json) | Tier II · 24 | `minecraft:ghast_tear` | `neovitae:air_scribe_tool` | 1,000 | 5 | 5 | — |
| [`alchemy_flask.json`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/recipe/ara_vitae/alchemy_flask.json) | Tier I · 8 | `minecraft:glass_bottle` | `neovitae:alchemy_flask` | 4,000 | 5 | 5 | — |
| [`apprentice_blood_orb.json`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/recipe/ara_vitae/apprentice_blood_orb.json) | Tier I · 8 | `#c:storage_blocks/redstone` | `neovitae:blood_orb_apprentice` | 5,000 | 5 | 5 | — |
| [`archmage_blood_orb.json`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/recipe/ara_vitae/archmage_blood_orb.json) | Tier IV · 100 | `neovitae:hellforged_block` | `neovitae:blood_orb_archmage` | 80,000 | 50 | 100 | — |
| [`awakened_activation_crystal.json`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/recipe/ara_vitae/awakened_activation_crystal.json) | Tier IV · 100 | `neovitae:activation_crystal_weak` | `neovitae:activation_crystal_awakened` | 40,000 | 30 | 50 | copy components |
| [`blood_lantern.json`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/recipe/ara_vitae/blood_lantern.json) | Tier 0 · bare | `minecraft:soul_lantern` | `neovitae:blood_lantern` | 1,500 | 5 | 2 | — |
| [`blood_pearl.json`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/recipe/ara_vitae/blood_pearl.json) | Tier I · 8 | `minecraft:ender_pearl` | `neovitae:blood_pearl` | 500 | 5 | 20 | — |
| [`blood_sweat_and_tears_music.json`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/recipe/ara_vitae/blood_sweat_and_tears_music.json) | Tier V · 172 | `#minecraft:creeper_drop_music_discs` | `neovitae:blood_sweat_and_tears` | 2,000 | 5 | 1 | — |
| [`bucket_life.json`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/recipe/ara_vitae/bucket_life.json) | Tier 0 · bare | `minecraft:bucket` | `neovitae:essentia_vitae_bucket` | 1,000 | 5 | 0 | — |
| [`cobbled_deepslate.json`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/recipe/ara_vitae/cobbled_deepslate.json) | Tier 0 · bare | `#c:cobblestones/normal` | `minecraft:cobbled_deepslate` | 50 | 5 | 0 | — |
| [`deus_tool.json`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/recipe/ara_vitae/deus_tool.json) | Tier V · 172 | `neovitae:prismatic_spiritus_gem` | `neovitae:deus_scribe_tool` | 50,000 | 100 | 100 | — |
| [`earth_tool.json`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/recipe/ara_vitae/earth_tool.json) | Tier II · 24 | `#c:obsidians` | `neovitae:earth_scribe_tool` | 1,000 | 5 | 5 | — |
| [`enhanced_teleposer_focus.json`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/recipe/ara_vitae/enhanced_teleposer_focus.json) | Tier III · 52 | `neovitae:teleposer_focus` | `neovitae:enhanced_teleposer_focus` | 10,000 | 20 | 10 | — |
| [`fire_tool.json`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/recipe/ara_vitae/fire_tool.json) | Tier II · 24 | `minecraft:magma_cream` | `neovitae:fire_scribe_tool` | 1,000 | 5 | 5 | — |
| [`magician_blood_orb.json`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/recipe/ara_vitae/magician_blood_orb.json) | Tier II · 24 | `#c:storage_blocks/gold` | `neovitae:blood_orb_magician` | 25,000 | 20 | 20 | — |
| [`master_blood_orb.json`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/recipe/ara_vitae/master_blood_orb.json) | Tier III · 52 | `neovitae:weak_blood_shard` | `neovitae:blood_orb_master` | 40,000 | 30 | 50 | — |
| [`revenant_plate_infusion.json`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/recipe/ara_vitae/revenant_plate_infusion.json) | Tier III · 52 | `neovitae:gore_clotted_fang` | `neovitae:revenant_plate` | 20,000 | 25 | 30 | — |
| [`sands_of_vitae.json`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/recipe/ara_vitae/sands_of_vitae.json) | Tier 0 · bare | `minecraft:soul_sand` | `neovitae:sands_of_vitae` | 250 | 5 | 1 | — |
| [`spikes.json`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/recipe/ara_vitae/spikes.json) | Tier I · 8 | `minecraft:iron_bars` | `neovitae:spikes` | 500 | 5 | 20 | — |
| [`tabula_aetherea.json`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/recipe/ara_vitae/tabula_aetherea.json) | Tier IV · 100 | `neovitae:tabula_spiritus` | `neovitae:tabula_aetherea` | 30,000 | 40 | 100 | — |
| [`tabula_animata.json`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/recipe/ara_vitae/tabula_animata.json) | Tier II · 24 | `neovitae:tabula_robur` | `neovitae:tabula_animata` | 5,000 | 15 | 10 | — |
| [`tabula_rasa.json`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/recipe/ara_vitae/tabula_rasa.json) | Tier 0 · bare | `#neovitae:vitae_stone` | `neovitae:tabula_rasa` | 1,000 | 5 | 5 | — |
| [`tabula_robur.json`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/recipe/ara_vitae/tabula_robur.json) | Tier I · 8 | `neovitae:tabula_rasa` | `neovitae:tabula_robur` | 2,000 | 5 | 5 | — |
| [`tabula_spiritus.json`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/recipe/ara_vitae/tabula_spiritus.json) | Tier III · 52 | `neovitae:tabula_animata` | `neovitae:tabula_spiritus` | 15,000 | 20 | 20 | — |
| [`teleposer_focus.json`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/recipe/ara_vitae/teleposer_focus.json) | Tier II · 24 | `minecraft:ender_eye` | `neovitae:teleposer_focus` | 2,000 | 10 | 10 | — |
| [`tenebrae_tool.json`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/recipe/ara_vitae/tenebrae_tool.json) | Tier III · 52 | `#c:storage_blocks/coal` | `neovitae:tenebrae_scribe_tool` | 2,000 | 20 | 10 | — |
| [`transcendent_blood_orb.json`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/recipe/ara_vitae/transcendent_blood_orb.json) | Tier V · 172 | `neovitae:crystal_cluster` | `neovitae:blood_orb_transcendent` | 200,000 | 100 | 200 | — |
| [`vitae_link.json`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/recipe/ara_vitae/vitae_link.json) | Tier I · 8 | `neovitae:ara_vitae` | `neovitae:vitae_link` | 1,500 | 5 | 2 | — |
| [`water_tool.json`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/recipe/ara_vitae/water_tool.json) | Tier II · 24 | `#c:storage_blocks/lapis` | `neovitae:water_scribe_tool` | 1,000 | 5 | 5 | — |
| [`weak_activation_crystal.json`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/recipe/ara_vitae/weak_activation_crystal.json) | Tier II · 24 | `neovitae:lava_crystal` | `neovitae:activation_crystal_weak` | 10,000 | 20 | 10 | — |
| [`weak_blood_orb.json`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/recipe/ara_vitae/weak_blood_orb.json) | Tier 0 · bare | `#c:gems/diamond` | `neovitae:blood_orb_weak` | 2,000 | 10 | 0 | — |

## Critical advancement chains: exact source minima

| Altar tier | New Orb and input | Orb conversion EV | New Tabula and input | Tabula EV |
| --- | --- | ---: | --- | ---: |
| 0 | Novicius ← diamond | 2,000 | Rasa ← #neovitae:vitae_stone | 1,000 |
| 1 | Discipulus ← redstone block tag | 5,000 | Robur ← Rasa | 2,000 |
| 2 | Veneficus ← gold block tag | 25,000 | Animata ← Robur | 5,000 |
| 3 | Magus ← Weak Blood Shard | 40,000 | Spiritus ← Animata | 15,000 |
| 4 | Dominus ← Hellforged Block | 80,000 | Aetherea ← Spiritus | 30,000 |
| 5 | Divinus ← Crystal Cluster | 200,000 | No sixth Tabula in this recipe directory | — |

**Ladder consequences:** a Tier III altar does not itself grant access to the **Awakened Activation Crystal**: the **only generated Ara Vitae recipe** for upgrading a Weak Activation Crystal to Awakened requires **Tier IV** and **40,000 EV**, with `copyInputComponents=true`. The source's ritual data map marks many Tenebrae/advanced rituals `crystal_level:1`, i.e. Awakened, so do not schedule those rituals before Tier IV unless another valid acquisition route is verified. **Tenebrae Diviner's craftability at Tier III does not bypass activation-crystal restrictions.** `Highway to Hell` has no level-1 requirement in the generated ritual stat map and can operate with Weak Crystal, pending live validation.

**Another important gate:** the base **Tabula Vitae** uses a bound Orb whose **tier must meet `upgradeLevel`**, per source `TabulaVitaeBlockEntity.java`. The generated Arcane Scribe Tool and 16× Iron Throwing Dagger recipes are both **`upgradeLevel:1`** and cannot be placed on the actual tutorial spine before obtaining the Tier I **Discipulus Orb** through normal source crafting. This supersedes the earlier high-level pilot ordering. The Athanor's vanilla shaped recipe uses an Orb tier **2** ingredient and `tabula_animata`; do not teach the Athanor at Tier 0.

## Other essential shaped recipes (fully literal source patterns)

The following are [generated shaped recipes](https://github.com/breakinblocks/NeoVitae/tree/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/recipe) and use symbol mappings; spaces are empty crafting slots. Item `neovitae:orb_tier` is a **custom ingredient matching any Orb at or above the stated minimum tier**; the source `OrbTierIngredient.java` only proves matching, **not whether an Orb is refunded as crafting remainder**. Test real consumption in TMM before directing players to repeat rune crafting.

| Output | Source pattern, top→bottom | Ingredient key (literal tags preserved) | Advancement note |
| --- | --- | --- | --- |
| `neovitae:ara_vitae` | `s s` / `sfs` / `ggg` | s=`#neovitae:vitae_stone`, f=`minecraft:furnace`, g=`#c:ingots/gold` | Bare tier 0 station |
| `neovitae:tabula_vitae` | `sss` / `wbw` / `gog` | s=Vitae Stone, w=`#minecraft:planks`, b=`#c:ingots/iron`, g=`#c:ingots/gold`, o=Tabula Rasa | Tier 0 physical block; recipes gated by socketed Orb tier |
| `neovitae:rune_blank` | `asa` / `aoa` / `aaa` | a=Vitae Stone, s=Tabula Rasa, o=`neovitae:orb_tier` at min 0 | Used for eight-rune Tier I ring; **Orb consumption must be tested** |
| `neovitae:hellfire_forge` | `i i` / `sSs` / `sos` | i=`#c:ingots/iron`, s=Vitae Stone, S=Tabula Rasa, o=`#c:storage_blocks/iron` | Station placeable before first Spiritus harvest |
| `neovitae:athanor` | `sss` / `SoS` / `IfI` | s=Vitae Stone, S=Tabula Animata, o=Orb tier **2** min, I=`minecraft:iron_block`, f=`minecraft:furnace` | **Tier II+ minimum** due to ingredients |
| `neovitae:master_ritual_stone` | `oso` / `scs` / `oso` | o=`#c:obsidians`, s=`neovitae:ritual_stone`, c=Orb tier **2** min | Tier II ritual setup |
| `neovitae:sanguine_casing` (×4) | `bdb` / `iTi` / `bib` | b=`neovitae:bloodstone`, d=`#c:gems/diamond`, i=`#c:ingots/iron`, T=Tabula Animata | Actual need for Bloodstone must be in path |
| `neovitae:sanguine_crafter` | `cTc` / `cXc` / `ccc` | c=Sanguine Casing, T=Tabula Spiritus, X=`minecraft:crafter` | **Tier III+** with Tabula Spiritus |
| `neovitae:bloodstone_brick` (×4) | `ss` / `sb` | s=Vitae Stone, b=Weak Blood Shard | Needs one Weak Blood Shard per 4; exact source of shard remains a separate material chain |
| `neovitae:lava_crystal` | `aba` / `bcb` / `ded` | a=`#c:glass_blocks`, b=`minecraft:lava_bucket` (three occurrences), c=Orb tier 0+, d=`#c:obsidians`, e=`#c:gems/diamond` | Input for Weak Activation Crystal; test bucket remainder semantics |

**Mechanical note:** The Lava Crystal pattern contains **three** `b` slots (`aba`, `bcb`), so the correct count is **three lava buckets**, not two; the key legend above states this explicitly.

## Source/test caveats and follow-up

- Exact `minTier` in altar recipe JSON is **authoritative source data**; item capability, rune math and visual level may have additional conditions.
- Full 3-D altar positions are still sourced from guide/generated multiblock data and need an in-client overlay/physical test; rune totals and capstone kinds are already in the [pilot dossier](../mods/neovitae.md).
- Actual pack recipe replacement, tag expansions, Orb remainder return and structure detector support are not yet demonstrated. No new pack scripts/quest files were changed.
