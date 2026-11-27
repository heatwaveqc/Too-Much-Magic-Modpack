# Ars Nouveau pinned Imbuement, Apparatus and enchanting

Exact `ars_nouveau-1.21.1-5.13.5.jar`, SHA-1 `cdc946c63e1eb676f0e29e4640335c0ce68d7de3`; direct JSON, CFR0.152 and javap method cross-check. [Parent](ars-nouveau.md), [guide/learning](ars-nouveau-pinned-guide-glyphs.md), [Source](ars-nouveau-pinned-source.md). Inspected ImbuementBlock/Tile/Recipe, EnchantingApparatusBlock/Tile/Recipe, EnchantmentRecipe/ReactiveEnchantmentRecipe, IEnchantingRecipe, IPedestalMachine and API recipe selection. All192 native resources in these four recipe types are listed below:13Imbuement+82ordinary Apparatus+96enchantment ranks+1Reactive initial application. Other recipe types/equipment operations remain separate parent research.

These are native defaults/possible operations, not measured rates or live JEI/override acceptance. No runtime or quest/config change.

## Start without an existing Source supply

Native Notebook **Getting Started → Magical Crafting**, **Crafting → Imbuement Chamber / Enchanting Apparatus** and **Enchanting → How to Enchant**. Documentation registers chamber Source Gem/essence recipes and apparatus/core/pedestal construction pages; enchantment pages are generated sorted by rank. JEI has distinct **Imbuement**, **Enchanting Apparatus**, and **Apparatus Enchanting** categories. Pick the actual operation, not another category showing the same output icon.

1. Craft Imbuement Chamber: six `ars_nouveau:archwood_planks` and two Gold-ingot-tag items, grid `xyx/x x/xyx`, x=planks,y=gold. Place it and main-hand use with one Lapis-gem-tag item or Amethyst Shard. Either native recipe costs500internal Source and yields one Source Gem, with no pedestals required.
2. **Chamber produces its own slow progress without external Source.** While a valid recipe needs Source, on20tick boundaries it tries external SourceUtil range2; if unavailable, adds10internal Source. No need to obtain Source Gems for a Sourcelink before ever making a Source Gem. The100craftTicks timer is a separate minimum once recipe selected. Actual first completion depends on stored energy/world phase, not one invariant wall-clock duration.
3. Source Jar/link can accelerate: successful nearby request takes min(200,recipe cost) and chamber adds200internally; failed request adds10. This is Source internally, not consuming a second reagent. With a100cost native special-arrow recipe it can take100 but add200, leaving internal carryover; record that exact mismatch rather than assume every energy unit is conserved.
4. For essences, craft Arcane Pedestals, place three matching catalyst items around chamber within **per-axis1 cube** and insert one Source Gem. All seven essence recipes cost2000 and retain those pedestal catalysts. Do not put essence ingredients into an ordinary crafting grid or burn them as apparatus inputs by mistake.
5. Obtain Sourcestone and ordinary crafting materials through JEI, then craft Arcane Core plus Enchanting Apparatus and enough pedestals. For default upright arrangement, Core directly below Apparatus with compatible vertical axis. Apparatus must have Core opposite its FACING, and Core axis must match; missing/wrong-axis produces distinct feedback.

## Chamber transaction and reusable catalysts

ImbuementRecipe requires input ingredient plus exact-size multiset of nonempty pedestal stacks. IPedestalMachine searches a full cube using offset1; extra nonempty pedestals can prevent a match. Recipe matches are repeatedly rechecked; missing catalyst cancels active recipe lookup/backoff20. Output replaces the chamber's sole item slot with assembled recipe result, then subtracts recipe Source cost. **No pedestal debit in this chamber completion method**; ingredients around chamber are reusable catalysts.

Chamber Container declares one slot/max stack1. Native manual insertion removes one selected-hotbar item only after recipe match; rejected insertion restores temporary probe and leaves held input. Main-hand use while occupied spawns old chamber stack at player, then optionally inserts one newly valid selected item. It is not guaranteed direct-to-inventory output. Client/offhand do no server transaction. Removing or changing recipe does not refund/debit previously accumulated Source by that interaction. Native player destruction drops current item; exact other break/automation paths require acceptance.

Recipe selection sets craftTicks100; ticks decrement while input present, backoff can suspend. Internal storage capacity10000000, max receive10000000/max extract0; receive capability can feed it, extraction forbidden. Source/craft ticks/draining/item have save fields; direct recipe reference is reselected after load. This is machine energy storage, not a user-facing ten-million-unit Source Jar.

## Apparatus transaction and failure costs

1. Place one recipe ingredient on each pedestal within **per-axis3 cube** of apparatus. Extra empty pedestals don't add ingredients; extra nonempty ones reject exact ingredient count. Standard doItemsMatch uses RecipeMatcher unordered matching, so pedestal order is usually a choice, not one mandatory compass diagram. Each stack is one ingredient candidate; don't stack eight ingredients on one pedestal.
2. Main-hand use apparatus with reagent/catalyst. Manual activation first checks Core/axis, then recipe and Source availability. Ordinary recipe sourceCost0 needs no jars. Positive cost checks SourceUtil range10, attempts immediate multi-provider payment, then marks isCrafting and removes one reagent from selected hotbar into apparatus. Debited Source is **before completion**; result of actual payment call is not rechecked by attemptCraft after preflight, so concurrent resource changes are a boundary case.
3. During work each tick rechecks recipe; invalid ingredients clear isCrafting. After counter>=210, if still crafting and valid, replaces reagent with assembled output, clears pedestal ingredients (except preserves tag), finishes sound/effect. Not one item per tick. Counter is not explicitly reset at attemptCraft or the early invalidation branch; interruption/restart timing requires testing, not a promise each retry always takes210fresh ticks.
4. clearItems replaces each nonpreserved pedestal's whole stack with its crafting remainder. Native preserve tag `ars_nouveau:apparatus_not_consumed` contains Spell Parchment. This differs from chamber catalysts: ordinary apparatus inputs are consumed at completion. If another player changes items after payment, no generic Source refund branch shown. Do not move unrelated ingredients onto nearby pedestals during a craft.
5. Use occupied, noncrafting apparatus to spawn existing result at player; may immediately start another recipe with held reagent. Busy interaction returns without taking output. For apparatus recipes keepNbtOfReagent=true, copies reagent component patch to result and resets damage if present; false recipes use declared result. This is recipe-specific, not universal NBT preservation.

API recipe list includes native ordinary apparatus, enchantments, Reactive, spell-write, armor-upgrade and prestidigitation types plus registered extensions; first matching recipe is selected. This companion inventories four audited types only. No default arbitrary fluid/entity/XP input is inferred from pedestal icons.

## Enchantment ranks and Reactive spell

EnchantmentRecipe allows Book/Enchanted Book or an item the enchantment canEnchant; checks compatibility against its other enchantments. **Requested rank minus current rank must equal1.** To get rankIII, applyI thenII thenIII recipes with actual existing rank each time. Wrong level yields bad_level feedback, incompatible enchantment/item yields incompatible. No player XP-level debit in these inspected apparatus/enchantment methods; **Source and pedestal ingredients are the recipe costs**. Recipe data can include ranks above ordinary vanilla maxima (e.g. ProtectionV); this proves defined rank recipe, not effective pack balance approval.

Assembly converts plain Book to Enchanted Book, otherwise copies input, then sets that enchantment rank in copied enchantments. Other existing components remain in that input copy. Dynamic recipe preview is empty in this class and excludeJei=true for ordinary presentation; use dedicated Apparatus Enchanting guide/viewer pages rather than treating empty result icon as no recipe.

Initial Reactive application is its own type, rank1. Requires Spell Parchment among pedestal inputs with a **nonempty stored spell**; applies ordinary compatibility/rank check, then writes ReactiveCasterData with parchment spell to result. Parchment preserved via native tag; do not assume a blank parchment or arbitrary book contains a spell. Native96 enchantment rows include Reactive ranks2..4 for later upgrades. Actual Reactive activation/cooldown/mana/gear behavior remains equipment audit, not proven by craft success alone.

## Exact Imbuement recipe roster (13)

Input is consumed/replaced in chamber; pedestal list is retained in this operation. Recipe ID paths are relative to `data/ars_nouveau/recipe/`, namespace `ars_nouveau`. `#` means ingredient tag and multiplicities mean separate pedestal slots.

| Recipe path | Input → output | Internal Source | Reusable pedestal ingredients |
| --- | --- | ---: | --- |
| `imbuement_abjuration_essence` | `#c:gems/source` → `1×ars_nouveau:abjuration_essence` | 2000 | `minecraft:fermented_spider_eye; minecraft:sugar; minecraft:milk_bucket` |
| `imbuement_air_essence` | `#c:gems/source` → `1×ars_nouveau:air_essence` | 2000 | `minecraft:feather; ars_nouveau:wilden_wing; #minecraft:arrows` |
| `imbuement_amethyst` | `minecraft:amethyst_shard` → `1×ars_nouveau:source_gem` | 500 | `none` |
| `imbuement_amethyst_block` | `minecraft:amethyst_block` → `1×ars_nouveau:source_gem_block` | 2000 | `none` |
| `imbuement_amplify_arrow` | `#minecraft:arrows` → `1×ars_nouveau:amplify_arrow` | 100 | `ars_nouveau:source_gem; ars_nouveau:air_essence; #c:gems/diamond` |
| `imbuement_conjuration_essence` | `#c:gems/source` → `1×ars_nouveau:conjuration_essence` | 2000 | `ars_nouveau:wilden_horn; ars_nouveau:starbuncle_shards; minecraft:book` |
| `imbuement_earth_essence` | `#c:gems/source` → `1×ars_nouveau:earth_essence` | 2000 | `#c:ingots/iron; #c:seeds; #minecraft:dirt` |
| `imbuement_fire_essence` | `#c:gems/source` → `1×ars_nouveau:fire_essence` | 2000 | `minecraft:flint_and_steel; minecraft:torch; minecraft:gunpowder` |
| `imbuement_lapis` | `#c:gems/lapis` → `1×ars_nouveau:source_gem` | 500 | `none` |
| `imbuement_manipulation_essence` | `#c:gems/source` → `1×ars_nouveau:manipulation_essence` | 2000 | `minecraft:stone_button; minecraft:redstone; minecraft:clock` |
| `imbuement_pierce_arrow` | `#minecraft:arrows` → `1×ars_nouveau:pierce_arrow` | 100 | `ars_nouveau:source_gem; ars_nouveau:air_essence; ars_nouveau:wilden_spike` |
| `imbuement_split_arrow` | `#minecraft:arrows` → `1×ars_nouveau:split_arrow` | 100 | `ars_nouveau:source_gem; ars_nouveau:air_essence; ars_nouveau:wilden_horn` |
| `imbuement_water_essence` | `#c:gems/source` → `1×ars_nouveau:water_essence` | 2000 | `minecraft:water_bucket; minecraft:snow_block; minecraft:kelp` |

## Ordinary Apparatus recipe roster (82)

These are optional choices/unlocks, not82mandatory quests. All ingredients below belong to apparatus consumption rules above. Source cost and keep-NBT flags are literal JSON values. Worker/ritual companions teach what each resulting item actually does.

| Recipe path | Reagent → output | Source | Keep reagent components | Pedestal ingredients |
| --- | --- | ---: | --- | --- |
| `alakarkinos_charm` | `ars_nouveau:alakarkinos_token` → `1×ars_nouveau:alakarkinos_charm` | 0 | false | `minecraft:brush; 3×#minecraft:decorated_pot_sherds` |
| `alchemists_crown` | `minecraft:golden_helmet` → `1×ars_nouveau:alchemists_crown` | 0 | false | `3×minecraft:glass_bottle` |
| `amulet_of_mana_boost` | `ars_nouveau:dull_trinket` → `1×ars_nouveau:amulet_of_mana_boost` | 0 | false | `3×#c:gems/diamond; 5×#c:gems/source` |
| `amulet_of_mana_regen` | `ars_nouveau:dull_trinket` → `1×ars_nouveau:amulet_of_mana_regen` | 0 | false | `2×#c:gems/diamond; 2×#c:ingots/gold; 4×#c:gems/source` |
| `arcanist_boots` | `minecraft:iron_boots` → `1×ars_nouveau:arcanist_boots` | 0 | true | `4×ars_nouveau:magebloom_fiber` |
| `arcanist_hood` | `minecraft:iron_helmet` → `1×ars_nouveau:arcanist_hood` | 0 | true | `4×ars_nouveau:magebloom_fiber` |
| `arcanist_leggings` | `minecraft:iron_leggings` → `1×ars_nouveau:arcanist_leggings` | 0 | true | `4×ars_nouveau:magebloom_fiber` |
| `arcanist_robes` | `minecraft:iron_chestplate` → `1×ars_nouveau:arcanist_robes` | 0 | true | `4×ars_nouveau:magebloom_fiber` |
| `battlemage_boots` | `minecraft:diamond_boots` → `1×ars_nouveau:battlemage_boots` | 0 | true | `4×ars_nouveau:magebloom_fiber` |
| `battlemage_hood` | `minecraft:diamond_helmet` → `1×ars_nouveau:battlemage_hood` | 0 | true | `4×ars_nouveau:magebloom_fiber` |
| `battlemage_leggings` | `minecraft:diamond_leggings` → `1×ars_nouveau:battlemage_leggings` | 0 | true | `4×ars_nouveau:magebloom_fiber` |
| `battlemage_robes` | `minecraft:diamond_chestplate` → `1×ars_nouveau:battlemage_robes` | 0 | true | `4×ars_nouveau:magebloom_fiber` |
| `belt_of_levitation` | `ars_nouveau:mundane_belt` → `1×ars_nouveau:belt_of_levitation` | 0 | false | `4×#c:ingots/gold; 3×minecraft:feather; ars_nouveau:air_essence` |
| `belt_of_unstable_gifts` | `ars_nouveau:mundane_belt` → `1×ars_nouveau:belt_of_unstable_gifts` | 0 | false | `minecraft:sugar; #c:crops/nether_wart; #c:rods/blaze; #c:dusts/glowstone; minecraft:fermented_spider_eye; #c:dusts/redstone; minecraft:brewing_stand; #c:feathers` |
| `brazier_relay` | `ars_nouveau:ritual_brazier` → `1×ars_nouveau:brazier_relay` | 0 | false | `3×ars_nouveau:manipulation_essence` |
| `decor_blossom` | `minecraft:spore_blossom` → `1×ars_nouveau:decor_blossom` | 0 | false | `ars_nouveau:conjuration_essence` |
| `dominion_wand` | `minecraft:stick` → `1×ars_nouveau:dominion_wand` | 0 | false | `2×#c:gems/source; #c:ingots/gold` |
| `drygmy_charm` | `ars_nouveau:drygmy_shard` → `1×ars_nouveau:drygmy_charm` | 0 | false | `#minecraft:fishes; minecraft:wheat; minecraft:apple; minecraft:carrot; #c:seeds; 3×#c:gems/source` |
| `enchanters_eye` | `ars_nouveau:scryers_crystal` → `1×ars_nouveau:enchanters_eye` | 0 | true | `4×minecraft:blaze_powder; 4×#c:ender_pearls` |
| `enchanters_fishing_rod` | `minecraft:fishing_rod` → `1×ars_nouveau:enchanters_fishing_rod` | 0 | true | `2×#c:storage_blocks/gold; 2×#c:storage_blocks/source` |
| `enchanters_gauntlet` | `#c:leathers` → `1×ars_nouveau:enchanters_gauntlet` | 0 | true | `#c:gems/diamond; 2×#c:storage_blocks/gold; 2×#c:storage_blocks/source` |
| `enchanters_mirror` | `#c:storage_blocks/source` → `1×ars_nouveau:enchanters_mirror` | 0 | false | `2×#c:glass_blocks; 2×ars_nouveau:manipulation_essence; 2×#c:logs/archwood; 2×#c:ingots/gold` |
| `enchanters_shield` | `minecraft:shield` → `1×ars_nouveau:enchanters_shield` | 0 | true | `2×#c:storage_blocks/gold; 2×#c:storage_blocks/source` |
| `enchanters_sword` | `minecraft:diamond_sword` → `1×ars_nouveau:enchanters_sword` | 0 | true | `#c:gems/diamond; 2×#c:storage_blocks/gold; 2×#c:storage_blocks/source` |
| `jar_of_light` | `minecraft:glass_bottle` → `1×ars_nouveau:jar_of_light` | 0 | false | `4×minecraft:glowstone; 2×minecraft:redstone_lamp; 2×#c:glass_blocks` |
| `jump_ring` | `ars_nouveau:ring_of_potential` → `1×ars_nouveau:jump_ring` | 0 | false | `3×ars_nouveau:wilden_wing; ars_nouveau:air_essence` |
| `lingering_flask_cannon` | `ars_nouveau:splash_flask_cannon` → `1×ars_nouveau:lingering_flask_cannon` | 0 | false | `minecraft:dragon_breath; 2×ars_nouveau:air_essence` |
| `magebloom_crop` | `#c:seeds` → `1×ars_nouveau:magebloom_crop` | 0 | false | `4×#c:gems/source` |
| `planarium` | `ars_nouveau:mob_jar` → `1×ars_nouveau:planarium` | 0 | false | `ars_nouveau:stable_warp_scroll; ars_nouveau:conjuration_essence; minecraft:diamond` |
| `potion_diffuser` | `minecraft:campfire` → `1×ars_nouveau:potion_diffuser` | 0 | false | `3×#c:ingots/gold; 3×ars_nouveau:archwood_planks` |
| `potion_flask` | `minecraft:glass_bottle` → `1×ars_nouveau:potion_flask` | 0 | false | `2×ars_nouveau:abjuration_essence; #c:storage_blocks/source; #c:storage_blocks/gold` |
| `potion_flask_amplify` | `ars_nouveau:potion_flask` → `1×ars_nouveau:potion_flask_amplify` | 0 | false | `8×ars_nouveau:abjuration_essence` |
| `potion_flask_extend_time` | `ars_nouveau:potion_flask` → `1×ars_nouveau:potion_flask_extend_time` | 0 | false | `8×ars_nouveau:manipulation_essence` |
| `potion_melder` | `ars_nouveau:potion_jar` → `1×ars_nouveau:potion_melder` | 0 | false | `2×ars_nouveau:abjuration_essence; 2×#c:storage_blocks/gold; 4×#c:rods/blaze` |
| `relay_collector` | `ars_nouveau:relay` → `1×ars_nouveau:relay_collector` | 0 | false | `4×#c:chests` |
| `relay_deposit` | `ars_nouveau:relay` → `1×ars_nouveau:relay_deposit` | 0 | false | `4×minecraft:hopper` |
| `relay_splitter` | `ars_nouveau:relay` → `1×ars_nouveau:relay_splitter` | 0 | false | `4×#c:gems/quartz; 4×#c:gems/lapis` |
| `relay_warp` | `ars_nouveau:relay` → `1×ars_nouveau:relay_warp` | 0 | false | `4×#c:ender_pearls; 4×minecraft:popped_chorus_fruit` |
| `resting_drygmy_painting` | `minecraft:painting` → `1×minecraft:painting` | 0 | false | `ars_nouveau:drygmy_shard` |
| `ring_of_greater_discount` | `ars_nouveau:ring_of_lesser_discount` → `1×ars_nouveau:ring_of_greater_discount` | 0 | false | `4×#c:gems/diamond; 2×#c:rods/blaze; 2×#c:gems/source` |
| `ring_of_lesser_discount` | `ars_nouveau:ring_of_potential` → `1×ars_nouveau:ring_of_lesser_discount` | 0 | false | `4×#c:gems/diamond; 2×#c:ender_pearls; 2×#c:gems/source` |
| `scryers_oculus` | `minecraft:ender_eye` → `1×ars_nouveau:scryers_oculus` | 0 | false | `minecraft:observer; minecraft:spyglass; #c:storage_blocks/source; #c:gems/diamond` |
| `shapers_focus` | `ars_nouveau:manipulation_essence` → `1×ars_nouveau:shapers_focus` | 0 | false | `#c:ingots/gold; minecraft:piston; minecraft:slime_block; #c:gems/diamond` |
| `sorcerer_boots` | `minecraft:golden_boots` → `1×ars_nouveau:sorcerer_boots` | 0 | true | `4×ars_nouveau:magebloom_fiber` |
| `sorcerer_hood` | `minecraft:golden_helmet` → `1×ars_nouveau:sorcerer_hood` | 0 | true | `4×ars_nouveau:magebloom_fiber` |
| `sorcerer_leggings` | `minecraft:golden_leggings` → `1×ars_nouveau:sorcerer_leggings` | 0 | true | `4×ars_nouveau:magebloom_fiber` |
| `sorcerer_robes` | `minecraft:golden_chestplate` → `1×ars_nouveau:sorcerer_robes` | 0 | true | `4×ars_nouveau:magebloom_fiber` |
| `spell_bow` | `minecraft:bow` → `1×ars_nouveau:spell_bow` | 0 | true | `#c:storage_blocks/source; #c:storage_blocks/gold; ars_nouveau:manipulation_essence` |
| `spell_crossbow` | `minecraft:crossbow` → `1×ars_nouveau:spell_crossbow` | 0 | true | `#c:storage_blocks/gold; ars_nouveau:manipulation_essence; #c:storage_blocks/source` |
| `spell_sensor` | `minecraft:sculk_sensor` → `1×ars_nouveau:spell_sensor` | 0 | false | `none` |
| `spell_turret` | `ars_nouveau:basic_spell_turret` → `1×ars_nouveau:spell_turret` | 0 | false | `#c:storage_blocks/source; 2×#c:rods/blaze` |
| `splash_flask_cannon` | `minecraft:dispenser` → `1×ars_nouveau:splash_flask_cannon` | 0 | false | `2×#c:ingots/gold; 2×#c:rods/blaze; 4×#c:gunpowders` |
| `stable_warp_scroll` | `ars_nouveau:warp_scroll` → `1×ars_nouveau:stable_warp_scroll` | 0 | true | `4×minecraft:blaze_powder; 2×#c:ender_pearls` |
| `starbuncle_charm` | `ars_nouveau:starbuncle_shards` → `1×ars_nouveau:starbuncle_charm` | 0 | true | `4×#c:ingots/gold` |
| `starbuncle_painting` | `minecraft:painting` → `1×minecraft:painting` | 0 | false | `ars_nouveau:starbuncle_shards` |
| `storage_lectern` | `minecraft:lectern` → `1×ars_nouveau:storage_lectern` | 0 | false | `4×#c:chests` |
| `summon_focus` | `#c:storage_blocks/source` → `1×ars_nouveau:summon_focus` | 0 | false | `ars_nouveau:wilden_horn; ars_nouveau:wilden_spike; ars_nouveau:wilden_wing; ars_nouveau:wilden_tribute; #c:ingots/gold` |
| `thread_amethyst_golem` | `ars_nouveau:blank_thread` → `1×ars_nouveau:thread_amethyst_golem` | 0 | false | `3×#c:obsidians` |
| `thread_chilling` | `ars_nouveau:blank_thread` → `1×ars_nouveau:thread_chilling` | 0 | false | `minecraft:blue_ice; 2×ars_nouveau:water_essence; minecraft:powder_snow_bucket` |
| `thread_depths` | `ars_nouveau:blank_thread` → `1×ars_nouveau:thread_depths` | 0 | false | `3×#minecraft:fishes; 2×ars_nouveau:water_essence; minecraft:pufferfish` |
| `thread_drygmy` | `ars_nouveau:blank_thread` → `1×ars_nouveau:thread_drygmy` | 0 | false | `3×ars_nouveau:drygmy_shard; 2×ars_nouveau:earth_essence; minecraft:rabbit_foot` |
| `thread_feather` | `ars_nouveau:blank_thread` → `1×ars_nouveau:thread_feather` | 0 | false | `4×#c:feathers; 2×ars_nouveau:abjuration_essence` |
| `thread_gliding` | `ars_nouveau:blank_thread` → `1×ars_nouveau:thread_gliding` | 0 | false | `2×ars_nouveau:air_essence; minecraft:elytra` |
| `thread_heights` | `ars_nouveau:blank_thread` → `1×ars_nouveau:thread_heights` | 0 | false | `minecraft:spider_eye; 3×minecraft:rabbit_hide; 2×ars_nouveau:air_essence` |
| `thread_high_step` | `ars_nouveau:blank_thread` → `1×ars_nouveau:thread_high_step` | 0 | false | `3×ars_nouveau:air_essence` |
| `thread_immolation` | `ars_nouveau:blank_thread` → `1×ars_nouveau:thread_immolation` | 0 | false | `3×ars_nouveau:fire_essence` |
| `thread_kindling` | `ars_nouveau:blank_thread` → `1×ars_nouveau:thread_kindling` | 0 | false | `minecraft:magma_cream; ars_nouveau:fire_essence; minecraft:fire_charge` |
| `thread_life_drain` | `ars_nouveau:blank_thread` → `1×ars_nouveau:thread_life_drain` | 0 | false | `ars_nouveau:mendosteen_pod; minecraft:sculk_catalyst; 2×ars_nouveau:abjuration_essence` |
| `thread_magic_capacity` | `ars_nouveau:blank_thread` → `1×ars_nouveau:thread_magic_capacity` | 0 | false | `3×ars_nouveau:sourceberry_bush; 3×ars_nouveau:magebloom` |
| `thread_repairing` | `ars_nouveau:blank_thread` → `1×ars_nouveau:thread_repairing` | 0 | false | `minecraft:anvil; 2×ars_nouveau:manipulation_essence` |
| `thread_spellpower` | `ars_nouveau:blank_thread` → `1×ars_nouveau:thread_spellpower` | 0 | false | `ars_nouveau:fire_essence; ars_nouveau:magebloom; ars_nouveau:manipulation_essence; ars_nouveau:water_essence; ars_nouveau:abjuration_essence; ars_nouveau:air_essence; ars_nouveau:earth_essence; ars_nouveau:conjuration_essence` |
| `thread_starbuncle` | `ars_nouveau:blank_thread` → `1×ars_nouveau:thread_starbuncle` | 0 | false | `3×ars_nouveau:starbuncle_shards; 3×minecraft:sugar; 2×ars_nouveau:manipulation_essence` |
| `thread_undying` | `ars_nouveau:blank_thread` → `1×ars_nouveau:thread_undying` | 0 | false | `minecraft:totem_of_undying; minecraft:phantom_membrane; 2×ars_nouveau:abjuration_essence` |
| `thread_warding` | `ars_nouveau:blank_thread` → `1×ars_nouveau:thread_warding` | 0 | false | `8×ars_nouveau:magebloom_fiber` |
| `thread_whirlisprig` | `ars_nouveau:blank_thread` → `1×ars_nouveau:thread_whirlisprig` | 0 | false | `3×ars_nouveau:whirlisprig_shards; 2×ars_nouveau:earth_essence; minecraft:golden_apple` |
| `thread_wixie` | `ars_nouveau:blank_thread` → `1×ars_nouveau:thread_wixie` | 0 | false | `3×ars_nouveau:wixie_shards; 2×ars_nouveau:abjuration_essence; 2×#c:crops/nether_wart; minecraft:blaze_rod` |
| `timer_spell_turret` | `ars_nouveau:basic_spell_turret` → `1×ars_nouveau:timer_spell_turret` | 0 | false | `minecraft:clock` |
| `void_jar` | `minecraft:glass_bottle` → `1×ars_nouveau:void_jar` | 0 | false | `minecraft:lava_bucket; minecraft:bucket; ars_nouveau:allow_scroll; #c:ender_pearls` |
| `wand` | `#c:logs/archwood` → `1×ars_nouveau:wand` | 0 | false | `4×#c:gems/source; 2×#c:ingots/gold; ars_nouveau:air_essence; ars_nouveau:manipulation_essence` |
| `warp_scroll_copy` | `ars_nouveau:warp_scroll` → `2×ars_nouveau:warp_scroll` | 1000 | true | `ars_nouveau:warp_scroll` |
| `whirlisprig_charm` | `ars_nouveau:whirlisprig_shards` → `1×ars_nouveau:whirlisprig_charm` | 0 | false | `#c:gems/source; ars_nouveau:magebloom_crop; ars_nouveau:magebloom; #c:gems/diamond; minecraft:oak_sapling; minecraft:spruce_sapling; minecraft:birch_sapling; #c:seeds/wheat` |
| `wixie_charm` | `ars_nouveau:wixie_shards` → `1×ars_nouveau:wixie_charm` | 0 | false | `#minecraft:saplings; #c:gems/emerald; minecraft:crafting_table; minecraft:brewing_stand` |

## Enchantment recipe roster (96 ranks / 30 families)

For each row catalyst is a compatible item/book currently at preceding rank; output is same item/book with named enchantment rank. Ranks are alternatives across families and sequential **within** selected family, not a compulsory all-enchantments checklist.

| Recipe path | Enchantment / target rank | Source | Pedestal ingredients |
| --- | --- | ---: | --- |
| `aqua_affinity_1` | `minecraft:aqua_affinity` / 1 | 5000 | `4×#minecraft:fishes; 2×#c:storage_blocks/source; 2×#c:storage_blocks/lapis` |
| `bane_of_arthropods_1` | `minecraft:bane_of_arthropods` / 1 | 2000 | `2×minecraft:spider_eye; #c:storage_blocks/source; #c:storage_blocks/lapis` |
| `bane_of_arthropods_2` | `minecraft:bane_of_arthropods` / 2 | 3500 | `3×minecraft:spider_eye; #c:storage_blocks/source; #c:storage_blocks/lapis` |
| `bane_of_arthropods_3` | `minecraft:bane_of_arthropods` / 3 | 5000 | `4×minecraft:fermented_spider_eye; #c:storage_blocks/source; #c:storage_blocks/lapis` |
| `bane_of_arthropods_4` | `minecraft:bane_of_arthropods` / 4 | 6500 | `4×minecraft:fermented_spider_eye; 2×#c:storage_blocks/source; #c:storage_blocks/lapis` |
| `bane_of_arthropods_5` | `minecraft:bane_of_arthropods` / 5 | 8000 | `4×minecraft:fermented_spider_eye; 2×#c:storage_blocks/source; 2×#c:storage_blocks/lapis` |
| `blast_protection_1` | `minecraft:blast_protection` / 1 | 2000 | `2×#c:storage_blocks/iron; #c:storage_blocks/source` |
| `blast_protection_2` | `minecraft:blast_protection` / 2 | 4000 | `2×#c:storage_blocks/iron; #c:storage_blocks/source; 2×#c:storage_blocks/lapis` |
| `blast_protection_3` | `minecraft:blast_protection` / 3 | 6000 | `2×#c:gems/diamond; 2×#c:storage_blocks/source; 2×#c:obsidians` |
| `blast_protection_4` | `minecraft:blast_protection` / 4 | 8000 | `4×#c:gems/diamond; 2×#c:storage_blocks/source; 2×#c:storage_blocks/gold` |
| `depth_strider_1` | `minecraft:depth_strider` / 1 | 3000 | `2×ars_nouveau:water_essence; 2×#c:storage_blocks/source` |
| `depth_strider_2` | `minecraft:depth_strider` / 2 | 6000 | `2×ars_nouveau:water_essence; 2×#c:storage_blocks/source; 2×minecraft:prismarine_shard` |
| `depth_strider_3` | `minecraft:depth_strider` / 3 | 9000 | `4×ars_nouveau:water_essence; 3×#c:storage_blocks/source; minecraft:nautilus_shell` |
| `efficiency_1` | `minecraft:efficiency` / 1 | 2000 | `minecraft:sugar; minecraft:iron_pickaxe; 2×#c:storage_blocks/source` |
| `efficiency_2` | `minecraft:efficiency` / 2 | 3500 | `#c:storage_blocks/redstone; minecraft:golden_pickaxe; 2×#c:storage_blocks/source` |
| `efficiency_3` | `minecraft:efficiency` / 3 | 5000 | `2×#c:storage_blocks/redstone; minecraft:golden_pickaxe; 3×#c:storage_blocks/source; 2×#c:obsidians` |
| `efficiency_4` | `minecraft:efficiency` / 4 | 6500 | `2×#c:storage_blocks/redstone; minecraft:diamond_pickaxe; minecraft:iron_shovel; 2×#c:storage_blocks/source; 2×#c:obsidians` |
| `efficiency_5` | `minecraft:efficiency` / 5 | 8000 | `2×#c:storage_blocks/redstone; minecraft:diamond_pickaxe; minecraft:diamond_shovel; 2×#c:storage_blocks/source; 2×#c:storage_blocks/lapis` |
| `feather_falling_1` | `minecraft:feather_falling` / 1 | 2000 | `ars_nouveau:air_essence; #c:slime_balls; 2×#c:storage_blocks/source` |
| `feather_falling_2` | `minecraft:feather_falling` / 2 | 3500 | `2×ars_nouveau:air_essence; #c:slime_balls; 2×#c:storage_blocks/source` |
| `feather_falling_3` | `minecraft:feather_falling` / 3 | 5000 | `3×ars_nouveau:air_essence; #c:slime_balls; 2×#c:storage_blocks/source; 2×#c:storage_blocks/lapis` |
| `feather_falling_4` | `minecraft:feather_falling` / 4 | 6500 | `4×ars_nouveau:air_essence; 2×#c:storage_blocks/source; 2×#c:storage_blocks/lapis` |
| `fire_aspect_1` | `minecraft:fire_aspect` / 1 | 2000 | `ars_nouveau:fire_essence; 2×#c:storage_blocks/source; 2×#c:storage_blocks/lapis` |
| `fire_aspect_2` | `minecraft:fire_aspect` / 2 | 4000 | `2×ars_nouveau:fire_essence; 2×#c:storage_blocks/source; 2×#c:storage_blocks/lapis` |
| `fire_protection_1` | `minecraft:fire_protection` / 1 | 2000 | `ars_nouveau:fire_essence; ars_nouveau:abjuration_essence; 2×#c:storage_blocks/source` |
| `fire_protection_2` | `minecraft:fire_protection` / 2 | 3500 | `2×ars_nouveau:fire_essence; ars_nouveau:abjuration_essence; 2×#c:storage_blocks/source` |
| `fire_protection_3` | `minecraft:fire_protection` / 3 | 5000 | `2×ars_nouveau:fire_essence; ars_nouveau:abjuration_essence; 3×#c:storage_blocks/source; #c:rods/blaze` |
| `fire_protection_4` | `minecraft:fire_protection` / 4 | 6500 | `2×ars_nouveau:fire_essence; 2×ars_nouveau:abjuration_essence; 2×#c:storage_blocks/source; 2×#c:rods/blaze` |
| `fire_protection_5` | `minecraft:fire_protection` / 5 | 8000 | `3×ars_nouveau:fire_essence; 3×ars_nouveau:abjuration_essence; #c:storage_blocks/source; #c:rods/blaze` |
| `flame_1` | `minecraft:flame` / 1 | 5000 | `ars_nouveau:fire_essence; ars_nouveau:air_essence; #c:rods/blaze` |
| `fortune_1` | `minecraft:fortune` / 1 | 6000 | `ars_nouveau:earth_essence; #c:gems/diamond; 6×#c:storage_blocks/source` |
| `fortune_2` | `minecraft:fortune` / 2 | 8000 | `2×ars_nouveau:earth_essence; 4×#c:gems/diamond; 2×#c:storage_blocks/source` |
| `fortune_3` | `minecraft:fortune` / 3 | 9000 | `3×ars_nouveau:earth_essence; #c:storage_blocks/diamond; 2×#c:storage_blocks/lapis; 2×#c:storage_blocks/source` |
| `infinity_1` | `minecraft:infinity` / 1 | 9000 | `7×ars_nouveau:air_essence; #c:storage_blocks/diamond` |
| `knockback_1` | `minecraft:knockback` / 1 | 2000 | `ars_nouveau:manipulation_essence; ars_nouveau:air_essence; 2×#c:storage_blocks/source` |
| `knockback_2` | `minecraft:knockback` / 2 | 4000 | `ars_nouveau:manipulation_essence; 2×ars_nouveau:air_essence; 2×#c:storage_blocks/source` |
| `looting_1` | `minecraft:looting` / 1 | 6000 | `ars_nouveau:earth_essence; #c:gems/emerald; 6×#c:storage_blocks/source` |
| `looting_2` | `minecraft:looting` / 2 | 8000 | `2×ars_nouveau:earth_essence; 4×#c:gems/emerald; 2×#c:storage_blocks/source` |
| `looting_3` | `minecraft:looting` / 3 | 9000 | `3×ars_nouveau:earth_essence; #c:storage_blocks/emerald; 2×#c:storage_blocks/lapis; 2×#c:storage_blocks/source` |
| `mana_boost_1` | `ars_nouveau:mana_boost` / 1 | 2000 | `ars_nouveau:sourceberry_bush; 4×#c:storage_blocks/source` |
| `mana_boost_2` | `ars_nouveau:mana_boost` / 2 | 3500 | `ars_nouveau:sourceberry_bush; #c:gems/diamond; 4×#c:storage_blocks/source; 2×#c:storage_blocks/lapis` |
| `mana_boost_3` | `ars_nouveau:mana_boost` / 3 | 5000 | `ars_nouveau:sourceberry_bush; 2×#c:gems/diamond; 4×#c:storage_blocks/source; #c:storage_blocks/lapis` |
| `mana_regen_1` | `ars_nouveau:mana_regen` / 1 | 2000 | `2×ars_nouveau:sourceberry_bush; 2×#c:storage_blocks/source; 2×#c:storage_blocks/lapis` |
| `mana_regen_2` | `ars_nouveau:mana_regen` / 2 | 3500 | `2×ars_nouveau:sourceberry_bush; 2×ars_nouveau:abjuration_essence; 2×#c:storage_blocks/source; 2×#c:storage_blocks/lapis` |
| `mana_regen_3` | `ars_nouveau:mana_regen` / 3 | 5000 | `2×ars_nouveau:sourceberry_bush; 3×ars_nouveau:abjuration_essence; 2×#c:storage_blocks/source; #c:storage_blocks/lapis` |
| `multishot_1` | `minecraft:multishot` / 1 | 9000 | `3×ars_nouveau:manipulation_essence; 5×#c:storage_blocks/source` |
| `piercing_1` | `minecraft:piercing` / 1 | 2500 | `ars_nouveau:manipulation_essence; 2×#c:storage_blocks/source; 2×#c:storage_blocks/lapis` |
| `piercing_2` | `minecraft:piercing` / 2 | 5000 | `2×ars_nouveau:manipulation_essence; 2×#c:storage_blocks/source; 2×#c:storage_blocks/lapis` |
| `piercing_3` | `minecraft:piercing` / 3 | 7500 | `3×ars_nouveau:manipulation_essence; 2×#c:storage_blocks/source; 2×#c:storage_blocks/lapis` |
| `piercing_4` | `minecraft:piercing` / 4 | 9000 | `4×ars_nouveau:manipulation_essence; 2×#c:storage_blocks/source; 2×#c:storage_blocks/lapis` |
| `power_1` | `minecraft:power` / 1 | 2000 | `#c:gems/diamond; ars_nouveau:air_essence; #c:storage_blocks/source` |
| `power_2` | `minecraft:power` / 2 | 3500 | `2×#c:gems/diamond; ars_nouveau:air_essence; 2×#c:storage_blocks/source` |
| `power_3` | `minecraft:power` / 3 | 5000 | `3×#c:gems/diamond; ars_nouveau:air_essence; 2×#c:storage_blocks/source; 2×#c:storage_blocks/lapis` |
| `power_4` | `minecraft:power` / 4 | 6500 | `4×#c:gems/diamond; ars_nouveau:air_essence; #c:storage_blocks/source; 2×#c:storage_blocks/lapis` |
| `power_5` | `minecraft:power` / 5 | 9000 | `5×#c:gems/diamond; ars_nouveau:air_essence; #c:storage_blocks/source; #c:storage_blocks/lapis` |
| `projectile_protection_1` | `minecraft:projectile_protection` / 1 | 2000 | `minecraft:arrow; ars_nouveau:air_essence; ars_nouveau:abjuration_essence; 2×#c:storage_blocks/source` |
| `projectile_protection_2` | `minecraft:projectile_protection` / 2 | 3500 | `minecraft:arrow; 2×ars_nouveau:air_essence; ars_nouveau:abjuration_essence; 2×#c:storage_blocks/source` |
| `projectile_protection_3` | `minecraft:projectile_protection` / 3 | 5000 | `minecraft:arrow; 2×ars_nouveau:air_essence; ars_nouveau:abjuration_essence; 3×#c:storage_blocks/source` |
| `projectile_protection_4` | `minecraft:projectile_protection` / 4 | 6500 | `minecraft:arrow; 2×ars_nouveau:air_essence; 2×ars_nouveau:abjuration_essence; 2×#c:storage_blocks/source` |
| `projectile_protection_5` | `minecraft:projectile_protection` / 5 | 8000 | `minecraft:arrow; 3×ars_nouveau:air_essence; 3×ars_nouveau:abjuration_essence; #c:storage_blocks/source` |
| `protection_1` | `minecraft:protection` / 1 | 2000 | `ars_nouveau:manipulation_essence; ars_nouveau:abjuration_essence; #c:storage_blocks/lapis; 2×#c:storage_blocks/source` |
| `protection_2` | `minecraft:protection` / 2 | 3500 | `2×ars_nouveau:manipulation_essence; ars_nouveau:abjuration_essence; 2×#c:storage_blocks/source; 2×#c:storage_blocks/lapis` |
| `protection_3` | `minecraft:protection` / 3 | 5000 | `2×ars_nouveau:manipulation_essence; ars_nouveau:abjuration_essence; 3×#c:storage_blocks/source; 2×#c:storage_blocks/lapis` |
| `protection_4` | `minecraft:protection` / 4 | 6500 | `2×ars_nouveau:manipulation_essence; 2×ars_nouveau:abjuration_essence; 2×#c:storage_blocks/source; 2×#c:storage_blocks/lapis` |
| `protection_5` | `minecraft:protection` / 5 | 8000 | `3×ars_nouveau:manipulation_essence; 3×ars_nouveau:abjuration_essence; #c:storage_blocks/source; #c:storage_blocks/diamond` |
| `punch_1` | `minecraft:punch` / 1 | 2000 | `2×ars_nouveau:air_essence; 2×#c:storage_blocks/source` |
| `punch_2` | `minecraft:punch` / 2 | 4000 | `3×ars_nouveau:air_essence; 2×#c:storage_blocks/source` |
| `quick_charge_1` | `minecraft:quick_charge` / 1 | 2000 | `ars_nouveau:air_essence; ars_nouveau:abjuration_essence; 2×#c:storage_blocks/source` |
| `quick_charge_2` | `minecraft:quick_charge` / 2 | 4000 | `ars_nouveau:air_essence; 2×ars_nouveau:abjuration_essence; 2×#c:storage_blocks/source` |
| `quick_charge_3` | `minecraft:quick_charge` / 3 | 6000 | `ars_nouveau:air_essence; 2×ars_nouveau:abjuration_essence; 3×#c:storage_blocks/source` |
| `reactive_2` | `ars_nouveau:reactive` / 2 | 6000 | `4×minecraft:blaze_powder; ars_nouveau:air_essence; ars_nouveau:earth_essence; ars_nouveau:fire_essence; ars_nouveau:water_essence` |
| `reactive_3` | `ars_nouveau:reactive` / 3 | 9000 | `4×#c:gems/emerald; #c:ender_pearls; ars_nouveau:abjuration_essence; ars_nouveau:conjuration_essence; ars_nouveau:manipulation_essence` |
| `reactive_4` | `ars_nouveau:reactive` / 4 | 9000 | `4×#c:gems/diamond; #c:ender_pearls; ars_nouveau:wilden_tribute` |
| `respiration_1` | `minecraft:respiration` / 1 | 3000 | `ars_nouveau:water_essence; 2×#c:storage_blocks/source` |
| `respiration_2` | `minecraft:respiration` / 2 | 6000 | `4×ars_nouveau:water_essence; 4×#c:storage_blocks/source` |
| `respiration_3` | `minecraft:respiration` / 3 | 9000 | `6×ars_nouveau:water_essence; 2×#c:storage_blocks/source` |
| `sharpness_1` | `minecraft:sharpness` / 1 | 2000 | `2×minecraft:quartz_block; #c:storage_blocks/source; #c:storage_blocks/lapis` |
| `sharpness_2` | `minecraft:sharpness` / 2 | 3500 | `2×minecraft:quartz_block; 2×#c:storage_blocks/source; #c:storage_blocks/lapis` |
| `sharpness_3` | `minecraft:sharpness` / 3 | 5000 | `2×minecraft:quartz_block; #c:gems/diamond; #c:storage_blocks/lapis` |
| `sharpness_4` | `minecraft:sharpness` / 4 | 6500 | `2×minecraft:quartz_block; 2×#c:gems/diamond; #c:storage_blocks/lapis` |
| `sharpness_5` | `minecraft:sharpness` / 5 | 8000 | `2×minecraft:quartz_block; 2×#c:gems/diamond; 2×#c:storage_blocks/lapis` |
| `silk_touch_1` | `minecraft:silk_touch` / 1 | 9000 | `2×#c:gems/emerald; 2×#c:gems/diamond; 2×#c:storage_blocks/source; 2×#c:storage_blocks/lapis` |
| `smite_1` | `minecraft:smite` / 1 | 2000 | `2×minecraft:bone_block; #c:storage_blocks/source; #c:storage_blocks/lapis` |
| `smite_2` | `minecraft:smite` / 2 | 3500 | `3×minecraft:bone_block; #c:storage_blocks/source; #c:storage_blocks/lapis` |
| `smite_3` | `minecraft:smite` / 3 | 5000 | `4×minecraft:bone_block; #c:storage_blocks/source; #c:storage_blocks/lapis` |
| `smite_4` | `minecraft:smite` / 4 | 6500 | `4×minecraft:bone_block; 2×#c:storage_blocks/source; #c:storage_blocks/lapis` |
| `smite_5` | `minecraft:smite` / 5 | 8000 | `4×minecraft:bone_block; 2×#c:storage_blocks/source; 2×#c:storage_blocks/lapis` |
| `sweeping_edge_1` | `minecraft:sweeping_edge` / 1 | 2000 | `ars_nouveau:manipulation_essence; #c:storage_blocks/source; #c:storage_blocks/lapis` |
| `sweeping_edge_2` | `minecraft:sweeping_edge` / 2 | 3500 | `2×ars_nouveau:manipulation_essence; 2×#c:storage_blocks/source; 2×#c:storage_blocks/lapis` |
| `sweeping_edge_3` | `minecraft:sweeping_edge` / 3 | 5000 | `3×ars_nouveau:manipulation_essence; 2×#c:storage_blocks/source; 3×#c:storage_blocks/lapis` |
| `thorns_1` | `minecraft:thorns` / 1 | 2000 | `ars_nouveau:wilden_spike; #c:storage_blocks/source; #c:storage_blocks/lapis` |
| `thorns_2` | `minecraft:thorns` / 2 | 3500 | `2×ars_nouveau:wilden_spike; 2×#c:storage_blocks/source; 2×#c:storage_blocks/lapis` |
| `thorns_3` | `minecraft:thorns` / 3 | 5000 | `3×ars_nouveau:wilden_spike; 2×#c:storage_blocks/source; 3×#c:storage_blocks/lapis` |
| `unbreaking_1` | `minecraft:unbreaking` / 1 | 2000 | `ars_nouveau:abjuration_essence; #c:gems/diamond; #c:storage_blocks/source; #c:storage_blocks/lapis` |
| `unbreaking_2` | `minecraft:unbreaking` / 2 | 3500 | `ars_nouveau:abjuration_essence; 2×#c:gems/diamond; 2×#c:storage_blocks/source; #c:storage_blocks/lapis` |
| `unbreaking_3` | `minecraft:unbreaking` / 3 | 5000 | `ars_nouveau:abjuration_essence; 3×#c:gems/diamond; 2×#c:storage_blocks/source; 2×#c:storage_blocks/lapis` |

Initial Reactive recipe:

| Recipe path | Source | Pedestal ingredients |
| --- | ---: | --- |
| `reactive_1` | 3000 | `ars_nouveau:spell_parchment; #c:storage_blocks/lapis; #c:storage_blocks/source` |

## Quest / acceptance handoff

| Required teaching stage | Honest criterion and safe reward | Positive / boundary test |
| --- | --- | --- |
| First Source Gem | Chamber/Gem item possession proxy; observed operation/manual criterion separately | Lapis or Amethyst, no Source Jar, valid recipe completes slowly; invalid input/extra pedestal fails without held debit |
| First essence | Teach retained3catalysts vs consumed center reagent; XP plus safe pedestal/workshop supplies | One Source Gem→chosen essence; catalysts survive, Source carryover, no matching catalyst,100cost arrow mismatch and full/empty supplier |
| Apparatus construction | Core/axis/apparatus/pedestals named Notebook page; XP and ordinary building supplies | Valid upright setup vs missing/wrong-axis Core and extra nonempty pedestal |
| First apparatus operation | Output item proxy cannot prove apparatus crafted it; manual completion/confirmed native operation detector | Correct reagent/all pedestal inputs, Source0/positive, changed inputs after debit, interrupted/restarted timing and output location |
| Enchantment choice | Teach each selected rank in sequence and compatibility; XP/safe ingredients, no high-rank shortcut reward | I→II, attemptIII onI rejected, conflicting gear, Book→Enchanted Book, Source debit and pedestal remainders |
| Reactive optional | Written Spell Parchment plus compatible equipment, observed later trigger separate | Nonempty vs blank spell, preserved parchment, normal upgrade path; equipment cost/activation not claimed from craft |

Existing native Chamber/Apparatus advancements use inventory_changed item criteria, so ownership is not proof of operation. Do not invent FTB detector/XP costs. Source-only findings remain separate from client guide navigation, effective recipe tags/overrides and multiplayer interruption/conservation acceptance. Parent researching.
