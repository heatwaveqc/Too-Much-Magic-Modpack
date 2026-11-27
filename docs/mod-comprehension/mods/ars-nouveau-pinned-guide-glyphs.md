# Ars Nouveau pinned guide and glyph acquisition

Exact local artifact `ars_nouveau-1.21.1-5.13.5.jar`, SHA-1 `cdc946c63e1eb676f0e29e4640335c0ce68d7de3`, rechecked against [pack pin](../../../mods/ars-nouveau.pw.toml) on2026-11-17. Current Prism copy is in instance `Too-Much-Magic-Full-Integration-Prism-UNSTABLE (2)`, replacing the earlier (1) path; find artifacts by filename/hash, not a permanent instance-number assumption. [Parent profile](ars-nouveau.md) remains researching. No gameplay/client acceptance.

## Exact-JAR corrections to earlier source survey

The JAR has **709 data recipe JSONs** and **85 glyph recipe resources**, not the historical82 source-generated glyph count. XP distribution is41at27points,28at55,16at160. Additional recipe IDs `ars_nouveau:reset`, `ars_nouveau:wololo`, `ars_nouveau:rewind` correspond to real EffectReset/EffectWololo/EffectRewind classes, not silently discarded non-glyph files. This is a resource inventory, not a count of every registered spell part: default-starting Self/Harm, for example, do not need matching recipe rows here. Addons/configs can change effective registry and learning.

Worn Notebook **opens the native Java documentation UI** (`WornNotebook.use → DocClientUtils.openBook`), not a requirement that Patchouli book JSON be present. `openBook` resumes previous native documentation screen if cached, otherwise IndexScreen. Separate Patchouli compat class existing in JAR does not make this item a Patchouli guide. Zero Patchouli JSON resources in artifact receipt therefore does not mean absent in-game documentation.

Notebook native recipe payload (exact item/tag/grid/components retained): `{"type":"minecraft:crafting_shapeless","category":"misc","ingredients":[{"tag":"c:gems/lapis"},{"item":"minecraft:book"}],"result":{"count":1,"id":"ars_nouveau:worn_notebook"}}`. Right-click opens client guide; server returns consume. An item quest proves acquisition, not reading/opening. Craft/use requires no invented mana, Source or external wiki prerequisite.

## Native documentation reference map

`api/registry/DocumentationRegistry` declares main categories Getting Started, Glyph Index (Tier One/Two/Three subcategories), Ritual Index, Source, Crafting, Field Guide, Familiars, Items/Equipment (Spell Casting/Items/Armor subcategories) and Enchanting. Pages are assembled by `setup/registry/Documentation.initOnWorldReload`, including loops over effective spell-part/ritual registries and recipe-backed entries. Use native category/page names and JEI references; do not depend on removed/fictitious Patchouli paths. Addons may register more pages after native setup.

Glyph documentation entries choose category from each part's configured tier, actual glyph item and localized description; recipe pages come from recipe lookup. Ritual pages come from RitualRegistry item map, not a fixed24-file Patchouli folder. Parent's24native ritual-class roster is separately being audited; a documentation screen rendered from dynamic registry is not proof every addon recipe exists/loads. Client page layout/search/navigation remain untested.

## Learning and Scribe's Table contract

Native starting glyph default overrides are **Touch, Self, Projectile, Break and Harm**. Whole-JAR method-reference scan located only those five core `defaultedStarterGlyph=true` overrides; AbstractSpellPart default returnsfalse. Actual `GlyphRegistry.getDefaultStartingSpells` filters registered parts through per-glyph Config.isStarterEnabled, so effective starter list is configurable. Disabled/modified entries must be checked against the loaded pack; do not claim an immutable free set after custom config.

Choose a glyph recipe at Scribe's Table through native unlock/menu flow. `ScribesTile.setRecipe` checks total experience **points**, then debits recipe.exp for noncreative before resolving master/head tile; if new selection succeeds it refunds old selection's consumed items and XP as loose items/orbs. It is not27/55/160levels, a second payment on learning, or inherently private per-player refund. The method's debit-before-null-master order is a failure boundary, not a source-approved XP exploit.

The HEAD part performs crafting. Every5ticks server scans ItemEntities in block box inflate2, consumes one needed unit from first eligible entity. If no loose item found, every20ticks with autoYoink enabled it searches capability handlers in X/Z±6,Y±2 and extracts one needed item, making a flying-item visual. Direct consumeStack splits one required unit from offered stack. Handler extraction assumes simulated/actual agreement; do not advertise lossless behavior for arbitrary changing handlers. Ingredients are exact repeated units in the table, not one whole stack per ingredient icon.

When remaining ingredients empty it starts120-tick craft. At completion server drops a copy of recipe output above table, clears recipe and consumed list. Successful addFreshEntity is not checked before clearing; possession/delivery is separate from visual animation. Recipe identity, crafting countdown and consumed-item reload/break behavior should be acceptance tested; source selection/crafting is not a live throughput measurement.

Right-click a produced Glyph item server-side: checks glyph enabled, then player capability; already-known/default-starting reports already learned without spending glyph. Successful unlock syncs player data, raises mana glyph bonus if appropriate and consumes1unless infinite materials. Knowledge is player capability state, not merely “the spellbook contains a glyph item.” The Glyph.use method itself is not the entire UI/book-tier/validation pipeline; book tier, spell validity and casting costs must be distinguished from acquiring/learning an item.

`SpellBook.use` server updates player's remembered highest book tier upward for noncreative-book tier and known-glyph mana bonus; client caster sends cast request through castOnServer. Merely moving a higher-tier book away does not prove remembered mana book-tier bonus is reset. This does not override actual held-book casting/validation caps. Those remain parent source-audit work.

Sources: exact `common/items/{WornNotebook,Glyph,SpellBook}`, `common/block/tile/ScribesTile`, `api/{registry/GlyphRegistry,registry/DocumentationRegistry,documentation/DocClientUtils,spell/AbstractSpellPart}`, `setup/{config/Config,registry/Documentation}`, glyph recipe JSON. Scratch decompilation outside Git. No recipe/source copies, textures or JAR committed.

## All85 native glyph recipe resources

Full native IDs and repeated ingredients below. `#` denotes actual tag, not confirmed pack-effective members. A recipe row alone does not establish spell mana cost, target behavior, augment validity, configured tier or enabled state. Those belong to spell classes/config and are not inferred from the XP number.

| Recipe ID (`ars_nouveau:`) | XP points | Required unit ingredients | Output |
| --- | ---: | --- | --- |
| `glyph_accelerate` | 55 | `minecraft:powered_rail`; `minecraft:sugar`; `minecraft:clock` | 1×`ars_nouveau:glyph_accelerate` |
| `glyph_amplify` | 27 | `minecraft:diamond_pickaxe` | 1×`ars_nouveau:glyph_amplify` |
| `glyph_animate_block` | 55 | `ars_nouveau:conjuration_essence`; 3×`#c:obsidians` | 1×`ars_nouveau:glyph_animate_block` |
| `glyph_aoe` | 55 | `minecraft:firework_star` | 1×`ars_nouveau:glyph_aoe` |
| `glyph_blink` | 160 | `ars_nouveau:manipulation_essence`; 4×`#c:ender_pearls` | 1×`ars_nouveau:glyph_blink` |
| `glyph_bounce` | 27 | `ars_nouveau:abjuration_essence`; 3×`#c:slime_balls` | 1×`ars_nouveau:glyph_bounce` |
| `glyph_break` | 27 | `minecraft:iron_pickaxe` | 1×`ars_nouveau:glyph_break` |
| `glyph_bubble` | 27 | 3×`#c:feathers`; `minecraft:water_bucket`; `#minecraft:boats`; `ars_nouveau:water_essence` | 1×`ars_nouveau:glyph_bubble` |
| `glyph_burst` | 160 | `ars_nouveau:manipulation_essence`; 5×`minecraft:tnt`; `minecraft:firework_star` | 1×`ars_nouveau:glyph_burst` |
| `glyph_cold_snap` | 55 | `ars_nouveau:water_essence`; `minecraft:powder_snow_bucket`; `minecraft:ice` | 1×`ars_nouveau:glyph_cold_snap` |
| `glyph_conjure_water` | 55 | `ars_nouveau:water_essence`; `minecraft:water_bucket` | 1×`ars_nouveau:glyph_conjure_water` |
| `glyph_craft` | 27 | `minecraft:crafting_table` | 1×`ars_nouveau:glyph_craft` |
| `glyph_crush` | 55 | `ars_nouveau:earth_essence`; `minecraft:grindstone`; `minecraft:piston` | 1×`ars_nouveau:glyph_crush` |
| `glyph_cut` | 27 | `ars_nouveau:manipulation_essence`; `minecraft:shears`; `minecraft:iron_sword` | 1×`ars_nouveau:glyph_cut` |
| `glyph_dampen` | 55 | `minecraft:nether_brick` | 1×`ars_nouveau:glyph_dampen` |
| `glyph_decelerate` | 55 | `minecraft:soul_sand`; `minecraft:cobweb`; `minecraft:clock` | 1×`ars_nouveau:glyph_decelerate` |
| `glyph_delay` | 27 | `ars_nouveau:manipulation_essence`; `minecraft:repeater`; `minecraft:clock` | 1×`ars_nouveau:glyph_delay` |
| `glyph_dispel` | 27 | `ars_nouveau:abjuration_essence`; 3×`minecraft:milk_bucket` | 1×`ars_nouveau:glyph_dispel` |
| `glyph_duration_down` | 55 | `minecraft:clock`; `minecraft:glowstone_dust` | 1×`ars_nouveau:glyph_duration_down` |
| `glyph_ender_inventory` | 55 | `ars_nouveau:manipulation_essence`; `minecraft:ender_chest` | 1×`ars_nouveau:glyph_ender_inventory` |
| `glyph_evaporate` | 27 | `ars_nouveau:manipulation_essence`; 3×`minecraft:sponge` | 1×`ars_nouveau:glyph_evaporate` |
| `glyph_exchange` | 55 | `ars_nouveau:manipulation_essence`; `minecraft:emerald_block`; 2×`#c:ender_pearls` | 1×`ars_nouveau:glyph_exchange` |
| `glyph_explosion` | 55 | `ars_nouveau:fire_essence`; 3×`minecraft:tnt`; `minecraft:fire_charge` | 1×`ars_nouveau:glyph_explosion` |
| `glyph_extend_time` | 55 | `minecraft:clock`; `#c:storage_blocks/redstone` | 1×`ars_nouveau:glyph_extend_time` |
| `glyph_extract` | 55 | `minecraft:emerald` | 1×`ars_nouveau:glyph_extract` |
| `glyph_fangs` | 160 | `ars_nouveau:conjuration_essence`; 2×`minecraft:prismarine_shard`; `minecraft:totem_of_undying` | 1×`ars_nouveau:glyph_fangs` |
| `glyph_fell` | 27 | `ars_nouveau:earth_essence`; `minecraft:diamond_axe` | 1×`ars_nouveau:glyph_fell` |
| `glyph_firework` | 55 | `ars_nouveau:fire_essence`; 2×`minecraft:firework_rocket`; `minecraft:firework_star` | 1×`ars_nouveau:glyph_firework` |
| `glyph_flare` | 55 | `ars_nouveau:fire_essence`; 2×`minecraft:flint_and_steel`; 2×`minecraft:fire_charge`; `minecraft:blaze_rod` | 1×`ars_nouveau:glyph_flare` |
| `glyph_fortune` | 55 | `minecraft:rabbit_foot` | 1×`ars_nouveau:glyph_fortune` |
| `glyph_freeze` | 27 | `ars_nouveau:water_essence`; 2×`minecraft:snow_block` | 1×`ars_nouveau:glyph_freeze` |
| `glyph_glide` | 160 | `ars_nouveau:air_essence`; `minecraft:elytra`; 3×`#c:gems/diamond` | 1×`ars_nouveau:glyph_glide` |
| `glyph_gravity` | 55 | `ars_nouveau:air_essence`; 2×`minecraft:anvil`; 3×`#c:feathers` | 1×`ars_nouveau:glyph_gravity` |
| `glyph_grow` | 55 | `ars_nouveau:earth_essence`; 5×`minecraft:bone_block`; 3×`#c:seeds` | 1×`ars_nouveau:glyph_grow` |
| `glyph_gust` | 27 | `ars_nouveau:air_essence`; 3×`minecraft:piston` | 1×`ars_nouveau:glyph_gust` |
| `glyph_harm` | 27 | `ars_nouveau:earth_essence`; 3×`minecraft:iron_sword` | 1×`ars_nouveau:glyph_harm` |
| `glyph_harvest` | 27 | `ars_nouveau:earth_essence`; `minecraft:iron_hoe` | 1×`ars_nouveau:glyph_harvest` |
| `glyph_heal` | 55 | `ars_nouveau:abjuration_essence`; 4×`minecraft:glistering_melon_slice`; `minecraft:golden_apple` | 1×`ars_nouveau:glyph_heal` |
| `glyph_hex` | 160 | `ars_nouveau:abjuration_essence`; `minecraft:fermented_spider_eye`; 3×`minecraft:blaze_rod`; `minecraft:wither_rose` | 1×`ars_nouveau:glyph_hex` |
| `glyph_ignite` | 27 | `minecraft:flint_and_steel`; 3×`#minecraft:coals` | 1×`ars_nouveau:glyph_ignite` |
| `glyph_infuse` | 55 | `ars_nouveau:abjuration_essence`; `minecraft:glass_bottle`; `#c:rods/blaze` | 1×`ars_nouveau:glyph_infuse` |
| `glyph_intangible` | 160 | `ars_nouveau:manipulation_essence`; 3×`minecraft:phantom_membrane`; 2×`#c:ender_pearls` | 1×`ars_nouveau:glyph_intangible` |
| `glyph_interact` | 27 | `ars_nouveau:manipulation_essence`; `minecraft:lever`; `#minecraft:wooden_pressure_plates`; `#minecraft:buttons` | 1×`ars_nouveau:glyph_interact` |
| `glyph_invisibility` | 55 | `ars_nouveau:abjuration_essence`; `minecraft:fermented_spider_eye`; `#c:rods/blaze` | 1×`ars_nouveau:glyph_invisibility` |
| `glyph_launch` | 27 | `ars_nouveau:air_essence`; 3×`minecraft:rabbit_hide` | 1×`ars_nouveau:glyph_launch` |
| `glyph_leap` | 27 | `ars_nouveau:air_essence`; 3×`ars_nouveau:wilden_wing` | 1×`ars_nouveau:glyph_leap` |
| `glyph_light` | 27 | `minecraft:lantern`; `minecraft:torch` | 1×`ars_nouveau:glyph_light` |
| `glyph_lightning` | 160 | `ars_nouveau:air_essence`; 3×`minecraft:lightning_rod`; `minecraft:heart_of_the_sea` | 1×`ars_nouveau:glyph_lightning` |
| `glyph_linger` | 160 | `ars_nouveau:manipulation_essence`; `minecraft:dragon_breath`; `#c:storage_blocks/diamond`; 2×`#c:rods/blaze` | 1×`ars_nouveau:glyph_linger` |
| `glyph_name` | 55 | `ars_nouveau:manipulation_essence`; `minecraft:name_tag` | 1×`ars_nouveau:glyph_name` |
| `glyph_orbit` | 160 | `minecraft:compass`; `minecraft:ender_eye`; `#c:rods/blaze` | 1×`ars_nouveau:glyph_orbit` |
| `glyph_pantomime` | 27 | 8×`#c:glass_blocks` | 1×`ars_nouveau:glyph_pantomime` |
| `glyph_phantom_block` | 27 | 8×`#c:glass_blocks` | 1×`ars_nouveau:glyph_phantom_block` |
| `glyph_pickup` | 27 | 2×`minecraft:hopper` | 1×`ars_nouveau:glyph_pickup` |
| `glyph_pierce` | 55 | `minecraft:arrow`; `ars_nouveau:wilden_spike` | 1×`ars_nouveau:glyph_pierce` |
| `glyph_place_block` | 27 | `ars_nouveau:manipulation_essence`; `minecraft:dispenser` | 1×`ars_nouveau:glyph_place_block` |
| `glyph_prestidigitation` | 27 | `ars_nouveau:conjuration_essence` | 1×`ars_nouveau:glyph_prestidigitation` |
| `glyph_projectile` | 27 | `minecraft:fletching_table`; `minecraft:arrow` | 1×`ars_nouveau:glyph_projectile` |
| `glyph_pull` | 27 | `minecraft:fishing_rod` | 1×`ars_nouveau:glyph_pull` |
| `glyph_randomize` | 27 | 2×`minecraft:pink_carpet` | 1×`ars_nouveau:glyph_randomize` |
| `glyph_redstone_signal` | 27 | `ars_nouveau:manipulation_essence`; 3×`#c:storage_blocks/redstone` | 1×`ars_nouveau:glyph_redstone_signal` |
| `glyph_rotate` | 27 | `ars_nouveau:manipulation_essence` | 1×`ars_nouveau:glyph_rotate` |
| `glyph_rune` | 27 | `ars_nouveau:manipulation_essence`; `ars_nouveau:runic_chalk`; `minecraft:tripwire_hook` | 1×`ars_nouveau:glyph_rune` |
| `glyph_self` | 27 | `#minecraft:wooden_pressure_plates`; `minecraft:iron_chestplate` | 1×`ars_nouveau:glyph_self` |
| `glyph_sense_magic` | 55 | `ars_nouveau:abjuration_essence`; `ars_nouveau:dowsing_rod`; `ars_nouveau:starbuncle_shards` | 1×`ars_nouveau:glyph_sense_magic` |
| `glyph_sensitive` | 27 | `minecraft:scaffolding`; `minecraft:poppy`; `minecraft:water_bucket` | 1×`ars_nouveau:glyph_sensitive` |
| `glyph_slowfall` | 55 | `ars_nouveau:air_essence`; `ars_nouveau:wilden_wing`; 3×`minecraft:feather`; `#c:rods/blaze`; `#c:crops/nether_wart` | 1×`ars_nouveau:glyph_slowfall` |
| `glyph_smelt` | 55 | `ars_nouveau:fire_essence`; 4×`minecraft:blast_furnace`; `#c:rods/blaze` | 1×`ars_nouveau:glyph_smelt` |
| `glyph_snare` | 27 | `ars_nouveau:earth_essence`; 4×`minecraft:cobweb` | 1×`ars_nouveau:glyph_snare` |
| `glyph_split` | 160 | `ars_nouveau:relay_splitter`; `ars_nouveau:wilden_spike`; `minecraft:stonecutter` | 1×`ars_nouveau:glyph_split` |
| `glyph_summon_decoy` | 160 | `ars_nouveau:conjuration_essence`; 4×`minecraft:armor_stand` | 1×`ars_nouveau:glyph_summon_decoy` |
| `glyph_summon_steed` | 27 | 4×`minecraft:leather` | 1×`ars_nouveau:glyph_summon_steed` |
| `glyph_summon_undead` | 160 | `ars_nouveau:conjuration_essence`; `minecraft:bone`; `minecraft:wither_skeleton_skull` | 1×`ars_nouveau:glyph_summon_undead` |
| `glyph_summon_vex` | 160 | `ars_nouveau:conjuration_essence`; `minecraft:totem_of_undying` | 1×`ars_nouveau:glyph_summon_vex` |
| `glyph_summon_wolves` | 27 | `ars_nouveau:conjuration_essence`; 3×`minecraft:bone`; 4×`ars_nouveau:wilden_wing` | 1×`ars_nouveau:glyph_summon_wolves` |
| `glyph_toss` | 27 | `ars_nouveau:manipulation_essence`; `minecraft:dropper` | 1×`ars_nouveau:glyph_toss` |
| `glyph_touch` | 27 | `#minecraft:buttons` | 1×`ars_nouveau:glyph_touch` |
| `glyph_underfoot` | 27 | `minecraft:iron_boots`; `#minecraft:wooden_pressure_plates` | 1×`ars_nouveau:glyph_underfoot` |
| `glyph_wall` | 160 | `ars_nouveau:manipulation_essence`; `minecraft:dragon_breath`; `#c:storage_blocks/diamond`; 2×`#c:rods/blaze` | 1×`ars_nouveau:glyph_wall` |
| `glyph_wind_burst` | 27 | 4×`minecraft:wind_charge`; `ars_nouveau:air_essence` | 1×`ars_nouveau:glyph_wind_burst` |
| `glyph_wind_shear` | 55 | `ars_nouveau:air_essence`; 3×`minecraft:iron_sword` | 1×`ars_nouveau:glyph_wind_shear` |
| `glyph_wither` | 160 | `ars_nouveau:abjuration_essence`; 3×`minecraft:wither_skeleton_skull` | 1×`ars_nouveau:glyph_wither` |
| `reset` | 27 | `minecraft:target` | 1×`ars_nouveau:reset` |
| `rewind` | 160 | `ars_nouveau:manipulation_essence`; 3×`minecraft:clock` | 1×`ars_nouveau:rewind` |
| `wololo` | 27 | `ars_nouveau:abjuration_essence`; 3×`#c:dyes` | 1×`ars_nouveau:wololo` |

## Three previously omitted recipe-backed parts

- Reset has default mana cost0 and a real spell-chain context/validation role; its description says reset to original target and modify combination/duplicate limits. This is not a second cast method or guaranteed harmless “erase spell” operation. Its exact validation-policy interaction still needs spell-pipeline audit.
- Wololo has Abjuration Essence plus three dye-tag units in its27XP recipe and a dedicated effect class with dye/recipe handling. Do not infer a universal free recoloring rule from its name or count it as decorative/non-glyph data.
- Rewind has Manipulation Essence plus threeClocks,160XP, default tierTHREE and default mana100. It uses rewind entity/event state and Extend Time/Duration Down augment choices. Its native description includes prior locations/health, spell-moved block restoration and delayed subsequent glyphs; supported record/rewind limits and cross-mod targets remain to audit. No general world rollback/player resurrection promise.

## Quest and acceptance handoff

Teach Notebook acquisition/opening, free configured starter glyphs, a valid initial cast (such as Projectile→Harm or Touch→Break with a suitable book/target), then recipe selection→XP-point debit→actual ingredient units→120tick craft→glyph pickup→right-click learning→new spell use. Valid initial spell example is a teaching candidate until target/mana/held-book pipeline is checked; do not treat possessing book/glyph as operation evidence. Reward XP and ordinary feedstock without gifting future tier boss materials.

Unrun boundaries: disabled vs starter overrides, wrong recipe ingredient/tag, insufficient XP, no master tile, reselection/refund attribution, autoYoink off/on/range edges, full/invalid adjacent handler, failed item delivery, already-known glyph, personal vs teammate learning, lower held book after remembered higher tier, native documentation reload with addons. Full parent source-completion remains outstanding for all major remaining mechanics and addon crosswalks; client tests are separate.
