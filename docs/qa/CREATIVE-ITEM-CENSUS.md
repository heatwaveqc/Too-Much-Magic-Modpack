# TMM creative-item census — first pass for #596

**Status:** historical installed-registry inventory plus a source-backed non-name creative function, no recipes disabled, no recipe/loot/EMC-learning audit and no Minecraft QA. Captured **2026-11-18** from actual installed build `f7aeb56a0f7b9c5957f922f5a397037f25207c76`: **23,681 registered items in 106 item namespaces**, 209 installed JARs. Current repository selection on 2026-11-23 has **249 `enabled_in_pack` manifest entries**; it is **not** identical to this historical installed build. The next test build must refresh this census, particularly for recently selected mods.

This is **not a list of things to ban**. The first classification is by registry ID/display name; that is how we can find explicitly labeled creative/debug inventory without preemptively deciding which are actually creative-class. There is no general `#creative` item tag that conclusively answers the question across mods, and membership in a creative inventory tab alone is not a forbidden mechanic. A later phase will examine capabilities and alternate stack components, then *only after this census* enumerate recipes, loot/rewards, machines and EMC paths.

The reproducible machine-readable snapshot is [CREATIVE-ITEM-CENSUS-2026-11-18.json](CREATIVE-ITEM-CENSUS-2026-11-18.json), generated from [items.csv](surveys-2026-11-18/items.csv) by `python3 scripts/index_creative_registry.py --check`.

## A. Explicit creative/cheat item registrations — 23

All items below were **actually registered** in the 2026-11-18 installed world. They have a creative/cheat indication in the item ID or display label, **not necessarily a recipe or granted infinite power**. The observed default-stack EMC happened to be zero for every one of these 23 entries; zero EMC neither proves a blacklist nor excludes component-sensitive mappings.

| Registry item ID | Captured display name | Default-stack EMC |
|---|---|---:|
| `ars_nouveau:creative_source_jar` | Creative Source Jar | 0 |
| `ars_nouveau:creative_spell_book` | Creative Spell Book | 0 |
| `botania:corporea_spark_creative` | Creative Corporea Spark | 0 |
| `botania:creative_pool` | The Everlasting Guilty Pool | 0 |
| `draconicevolution:creative_capacitor` | Creative Capacitor | 0 |
| `draconicevolution:creative_op_capacitor` | Creative Power Source | 0 |
| `embers:creative_ember_source` | Creative Ember Source | 0 |
| `evilcraft:creative_blood_drop` | Creative Blood Drop | 0 |
| `hexcasting:creative_unlocker` | The Media Cube | 0 |
| `hexerei:creative_waxing_kit` | Creative Waxing Kit | 0 |
| `improvableskills:scroll_creative` | Creative Page with Skills | 0 |
| `neovitae:activation_crystal_creative` | Creative Activation Crystal | 0 |
| `pastel:creative_ink_assortment` | Creative Ink Assortment | 0 |
| `pastel:creative_particle_spawner` | Creative Particle Spawner | 0 |
| `primalmagick:creative_wand_gem_item` | Creative Wand Gem | 0 |
| `primalmagick:grimoire_creative` | Creative Grimoire | 0 |
| `primalmagick:mana_singularity_creative` | Creative Mana Singularity | 0 |
| `psi:cad_assembly_creative` | Creative CAD Assembly | 0 |
| `replication:creative_matter_tank` | Creative Matter Tank | 0 |
| `replication:creative_memory_chip` | Creative Memory Chip | 0 |
| `scriptor:engraving_tool` | Engraving Tool (Creative) | 0 |
| `totemic:ceremony_cheat` | Creative Ceremony Cheat | 0 |
| `totemic:creative_medicine_bag` | Creative Medicine Bag (None) | 0 |

## B. Name-based capacity/infinity/eternity review — 41

These registered IDs or labels use words such as *infinite*, *eternal*, *unbreakable*, *bottomless*, *everlasting*, *endless*, or *infrangible*. **Do not automatically remove them.** Some are decorative stone/ice, some ordinary renewable equipment, some deliberate survival tools, and some may be truly infinite creative-class storage or resource providers. They need semantic classification against the exact installed mod version and owner policy.

| Registry item ID | Captured display name | Default-stack EMC |
|---|---|---:|
| `ars_elemental:everfull_urn` | Urn of Endless Waters | 0 |
| `artifacts:eternal_steak` | Eternal Steak | 0 |
| `artifacts:everlasting_beef` | Everlasting Beef | 0 |
| `betterend:eternal_crystal` | Eternal Crystal | 0 |
| `betterend:eternal_pedestal` | Eternal Pedestal | 0 |
| `betterend:flavolite_runed_eternal` | Eternal Runed Flavolite | 0 |
| `botania:infinite_fruit` | The Fruit of Grisaia | 0 |
| `botania:infrangible_platform` | Infrangible Platform | 0 |
| `cataclysm:unbreakable_skull` | Unbreakable Skull | 0 |
| `enchanted:infinity_egg` | Infinity Egg | 0 |
| `enigmaticlegacyplus:avarice_scroll` | Pact of Infinite Avarice | 0 |
| `enigmaticlegacyplus:cosmic_cake` | The Eternal Cake | 0 |
| `eternal_starlight:eternal_ice` | Eternal Ice | 0 |
| `eternal_starlight:eternal_ice_brick_slab` | Eternal Ice Brick Slab | 0 |
| `eternal_starlight:eternal_ice_brick_stairs` | Eternal Ice Brick Stairs | 0 |
| `eternal_starlight:eternal_ice_brick_wall` | Eternal Ice Brick Wall | 0 |
| `eternal_starlight:eternal_ice_bricks` | Eternal Ice Bricks | 0 |
| `eternal_starlight:eternal_ice_deepsilver_ore` | Eternal Ice Deepsilver Ore | 0 |
| `eternal_starlight:eternal_ice_lantern` | Eternal Ice Lantern | 0 |
| `eternal_starlight:eternal_ice_redstone_ore` | Eternal Ice Redstone Ore | 0 |
| `eternal_starlight:eternal_ice_saltpeter_ore` | Eternal Ice Saltpeter Ore | 0 |
| `eternal_starlight:eternal_ice_starcore_ore` | Eternal Ice Starcore Ore | 0 |
| `eternal_starlight:eternal_ice_starlit_diamond_ore` | Eternal Ice Starlit Diamond Ore | 0 |
| `eternal_starlight:thin_eternal_ice` | Thin Eternal Ice | 0 |
| `evilcraft:box_of_eternal_closure` | Box of Eternal Closure | 0 |
| `evilcraft:bucket_eternal_water` | Eternal Water Bucket | 0 |
| `evilcraft:eternal_water` | Eternal Water Block | 0 |
| `extrabotany:infinite_wine` | Infinite Fallback Wine (12) | 0 |
| `forbidden_arcanus:eternal_obsidian_skull` | Eternal Obsidian Skull | 0 |
| `forbidden_arcanus:eternal_stella` | Eternal Stella | 0 |
| `goety:eternal_cauldron` | Eternal Cauldron | 0 |
| `malum:ring_of_the_endless_well` | Ring of the Endless Well | 0 |
| `neovitae:array_endless_fountain` | Endless Fountain Array | 0 |
| `pastel:bottomless_bundle` | Bottomless Bundle | 0 |
| `projecte:aeternalis_fuel` | Aeternalis Fuel | 8192 |
| `projecte:aeternalis_fuel_block` | Aeternalis Fuel Block | 73728 |
| `projecte:gem_of_eternal_density` | Gem of Eternal Density | 319616 |
| `projectexpansion:infinite_fuel` | Infinite Fuel | 2229512 |
| `projectexpansion:infinite_steak` | Infinite Steak | 2229000 |
| `witchery:eternal_catalyst` | Eternal Catalyst | 141632 |
| `witchery:infinity_egg` | Infinity Egg | 0 |

Examples to **preserve unless a specific confirmed exploit changes the design**: `artifacts:eternal_steak`, `artifacts:everlasting_beef`, and `botania:infinite_fruit` are deliberately supported survival foods; `eternal_starlight:eternal_ice` is a material name, not a creative infinite-energy assertion. Do not reclassify `projecte:aeternalis_fuel` merely for the word *eternal*. `projectexpansion:infinite_fuel` and `infinite_steak` are real Project Expansion items with positive captured EMC, but require normal vs genuinely creative-class semantic review rather than automatic deletion. `botania:infrangible_platform` is in Botania's creative recipe module yet may be a legitimate high-tier utility; hold classification.

## C. Creative *component variants* that cannot be identified by item ID

| Base registry ID | Variant and component evidence | Registry/default-stack detection |
|---|---|---|
| `botania:mana_tablet` | Native Botania Revived `CreativeCraftingModule.creativeTablet()` creates a regular mana tablet with a **creative=1 native custom-data state** and max mana. A variant of the ordinary tablet, not a new registered item. | **Not enumerated** by item-ID/default-stack matching; source-confirmed variant, actual instance/loaded recipe pending. |

**Important:** This is the **one component-only creative variant confirmed by the source investigation so far**, not a claim that all other mods have none. The installed survey's default-stack entries cannot reveal arbitrary NBT/component variants. This is why an item-ID-only recipe filter would be insufficient for #596. The 2026-11-18 registry had `botania:mana_tablet` in the general 23,681 items, but not its special creative-form stack.

## D. Source-backed creative functions with noncreative item names — 1

| Registry ID | Source evidence | Classification / census boundary |
|---|---|---|
| \`neovitae:blood_battery\` | Neo Vitae 1.1.54 \`BloodBatteryBlock\` labels this **Creative Only**; its \`BloodBatteryBlockEntity\` refills FE on a redstone signal, without regular EV/Anima/Spiritus debit. Recorded on issue #596. | **Creative-class by source behavior**, registered in the historical item survey, but **not found by creative-name/infinity-name patterns**. No survival acquisition proven by this inventory. |

This source-backed item is a **third independent candidate lane**, not a 24th literal creative-named item and not one of the 41 semantic-name watches. Source inspection establishes intended creative power, not a loaded recipe or ProjectE mapping. Confirm native config/recipe/loot/reward/EMC handling only in a later acquisition-path phase.

## E. Reusable capture and comparison contract

\`scripts/index_creative_registry.py --check\` still rederives the frozen November 18 JSON from its exact historical CSV. **The hardcoded November 18 date, integration commit and 209 installed JARs now apply only when the input is that exact frozen CSV path.** A new installed-world export must use \`--input\` and explicit \`--metadata\` pointing to a JSON object with keys \`source\`, \`capture_date\`, \`installed_build_sha\` (exact 40-character lowercase commit), and \`source_installed_jar_count\` (positive integer). This fails closed instead of accidentally publishing old provenance for a fresh export. The owner/QA operator must supply verified capture metadata, not guess it from the current repo HEAD.

Example after an *already authorized* build's item survey is collected (paths are illustrative, not claimed to exist):

\`\`\`bash
python3 scripts/index_creative_registry.py \
  --input docs/qa/new-installed-survey/items.csv \
  --metadata docs/qa/new-installed-survey/capture-metadata.json \
  --output docs/qa/new-creative-census.json \
  --compare docs/qa/CREATIVE-ITEM-CENSUS-2026-11-18.json \
  --diff-output docs/qa/new-creative-diff.json
\`\`\`

The optional comparison reports registered item-count delta and added/removed **candidate IDs**, including the source-backed and component-variant lanes. It does not equate registry drift with craftability, acquisition or EMC learnability. Native creative-only component creation, creative-tab stack variants and behavioral source sweeps still need explicit investigation; the input CSV alone cannot prove completeness.

## F. Scope, gaps and next decision sequence

1. **Inventory refresh, not a recipe change:** rerun a read-only item registry/default-stack inventory on the next **owner-authorized installed build**. Contrast with the 249 selected manifest mods and pin exact installed JAR/SHA; the older 209-JAR snapshot misses incoming content. Reuse the existing `scripts/runtime_survey_probe.js` only via its established authorized survey procedure, not automatic game startup. Re-run `scripts/index_creative_registry.py` against the resulting `items.csv`.
2. **Discover hidden variants and behavior:** for each candidate, inspect mod registry/source/asset paths, native creative-tab stacks, custom component/NBT construction, config flags, true resource production vs merely renewable tools, and acquisition type. Add reviewed component-only or non-obvious infinite-provider candidates individually; do not infer their absence from negative name scans.
3. **Owner classification before acquisition suppression:** mark each candidate as `creative_class`, `ordinary_survival`, `decorative`, `dev_test`, `component_variant`, or `uncertain`, with source-backed reasons; preserve deliberate normal survival and ProjectE capstone logic.
4. **Then, as a separate follow-up, check recipes and alternatives:** only after classification, enumerate native recipe types, external recipe/output paths, loot, quest/shop/trader/Gateway rewards, EMC-learning and component-transform routes; decide targeted removals/config flags from actual evidence. Creative inventory/operator testing remains available.

### Work readiness

- **Implemented/type A:** historical registry exact-ID census, stable JSON, regen/check script; verified source registry IDs and observed default EMC, with stale-build caveat.
- **Pending investigation, no owner design blocker:** current pinned JAR/registered-item refresh, component and hidden-resource-provider sweep, per-item function classification.
- **B/C QA:** none performed as part of this census; use separately authorized current build for registry refresh and later acquisition tests. No recipe changes, no full build or smoke authorized by this report.
