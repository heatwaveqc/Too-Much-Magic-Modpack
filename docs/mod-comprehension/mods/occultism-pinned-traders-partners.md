# Occultism pinned trader, companion and prerequisite side routes

Exact hash-matched local `occultism-1.21.1-neoforge-1.227.0.jar` (SHA-512 in [parent](occultism.md)); CFR0.152 including nested classes. This is native source research; no live trading, player-effect or ownership acceptance run. Ordinary recipe JSON inventories do **not** include these Java-defined merchant offers or mob-loot material gates.

## Wondering Trader: acquisition and opening the right catalogue

This is Occultism's **Wondering** Trader, distinct from vanilla Wandering Trader and worker SpiritTradeRecipe. Native `ritual/summon_wondering_trader`: bound Djinni Book, summon_djinni pentacle, counter120, twoOtherworld Essence +twoEmerald-tag ingredients. Alternative native event replacement: EVENT-spawned uncanceled vanilla Wandering Trader, default configured5% chance, creates Wondering Trader with despawnDelay48000 and persistence flag; later discards original and its leashed Trader Llamas. Do not promise every natural trader converts or remains forever.

On interaction, **active Third Eye OR qualifying goggles OR staff** selects otherOffers; otherwise commonOffers. Ordinary catalogue uses vanilla wandering listings plus Datura hint (unless experimental TRADE_REBALANCE delegates vanilla behavior). Other catalogue samples categories1–8 below, rather than granting all114special choices at once: group1 one; group2 random1–3; group3 random1–2; group4 one; group5 random1–3; group6 one; group7 with50% branch, otherwise group8 with another50% test (25%overall); otherwise neither. Offers persist via entity data; no inspected method establishes unlimited automatic restocking.

Every listed native ItemTrade uses maxUses and villagerXP below, priceMultiplier0.05, and item/count input (not a copied input component predicate). Spirit-name-capable result stacks get generated names. These are offers in a pool, **not guaranteed acquisitions on one visit**, and vanilla demand/discount/merchant handling can affect final display. Table preserves Java registry constants when a registration ID was not resolved rather than guessing it. Villager XP is merchant reward metadata, not the player's guaranteed XP-orb output.

| Pool | Base input | Result | Max uses | Villager XP |
| --- | --- | --- | ---: | ---: |
| 0 | 1×`minecraft:wheat_seeds` | 1×`occultism:datura` | 1 | 1 |
| 1 | 1×`occultism:otherworld_essence` | 1×`minecraft:book` | 1 | 1 |
| 1 | 1×`occultism:otherworld_essence` | 1×`occultism:taboo_book` | 1 | 2 |
| 1 | 2×`occultism:otherworld_essence` | 1×`minecraft:writable_book` | 1 | 3 |
| 1 | 2×`occultism:otherworld_essence` | 1×`occultism:book_of_binding_empty` | 1 | 4 |
| 1 | 1×`occultism:otherworld_essence` | 1×`occultism:book_of_binding_bound_foliot` | 1 | 1 |
| 1 | 2×`occultism:otherworld_essence` | 1×`occultism:book_of_binding_bound_djinni` | 1 | 2 |
| 1 | 3×`occultism:otherworld_essence` | 1×`occultism:book_of_binding_bound_afrit` | 1 | 3 |
| 1 | 4×`occultism:otherworld_essence` | 1×`occultism:book_of_binding_bound_marid` | 1 | 4 |
| 2 | 10×`minecraft:rotten_flesh` | 1×`OccultismBlocks.LARGE_CANDLE` | 8 | 4 |
| 2 | 10×`minecraft:rotten_flesh` | 1×`OccultismBlocks.LARGE_CANDLE_WHITE` | 8 | 4 |
| 2 | 10×`minecraft:rotten_flesh` | 1×`OccultismBlocks.LARGE_CANDLE_LIGHT_GRAY` | 8 | 4 |
| 2 | 10×`minecraft:rotten_flesh` | 1×`OccultismBlocks.LARGE_CANDLE_GRAY` | 8 | 4 |
| 2 | 10×`minecraft:rotten_flesh` | 1×`OccultismBlocks.LARGE_CANDLE_BLACK` | 8 | 4 |
| 2 | 10×`minecraft:rotten_flesh` | 1×`OccultismBlocks.LARGE_CANDLE_BROWN` | 8 | 4 |
| 2 | 10×`minecraft:rotten_flesh` | 1×`OccultismBlocks.LARGE_CANDLE_RED` | 8 | 4 |
| 2 | 10×`minecraft:rotten_flesh` | 1×`OccultismBlocks.LARGE_CANDLE_ORANGE` | 8 | 4 |
| 2 | 10×`minecraft:rotten_flesh` | 1×`OccultismBlocks.LARGE_CANDLE_YELLOW` | 8 | 4 |
| 2 | 10×`minecraft:rotten_flesh` | 1×`OccultismBlocks.LARGE_CANDLE_LIME` | 8 | 4 |
| 2 | 10×`minecraft:rotten_flesh` | 1×`OccultismBlocks.LARGE_CANDLE_GREEN` | 8 | 4 |
| 2 | 10×`minecraft:rotten_flesh` | 1×`OccultismBlocks.LARGE_CANDLE_CYAN` | 8 | 4 |
| 2 | 10×`minecraft:rotten_flesh` | 1×`OccultismBlocks.LARGE_CANDLE_LIGHT_BLUE` | 8 | 4 |
| 2 | 10×`minecraft:rotten_flesh` | 1×`OccultismBlocks.LARGE_CANDLE_BLUE` | 8 | 4 |
| 2 | 10×`minecraft:rotten_flesh` | 1×`OccultismBlocks.LARGE_CANDLE_PURPLE` | 8 | 4 |
| 2 | 10×`minecraft:rotten_flesh` | 1×`OccultismBlocks.LARGE_CANDLE_MAGENTA` | 8 | 4 |
| 2 | 10×`minecraft:rotten_flesh` | 1×`OccultismBlocks.LARGE_CANDLE_PINK` | 8 | 4 |
| 2 | 1×`minecraft:iron_block` | 1×`OccultismBlocks.SPIRIT_ATTUNED_CRYSTAL` | 8 | 8 |
| 2 | 4×`minecraft:bone_block` | 1×`minecraft:skeleton_skull` | 4 | 8 |
| 2 | 64×`minecraft:coal` | 1×`minecraft:wither_skeleton_skull` | 2 | 16 |
| 3 | 1×`minecraft:rose_bush` | 15×`OccultismBlocks.OTHERFLOWER` | 15 | 15 |
| 3 | 7×`minecraft:nether_wart` | 1×`minecraft:torchflower` | 7 | 7 |
| 3 | 12×`occultism:datura` | 1×`occultism:nature_paste` | 1 | 24 |
| 3 | 4×`minecraft:tuff` | 1×`occultism:gray_paste` | 1 | 24 |
| 3 | 1×`minecraft:emerald_block` | 1×`occultism:research_fragment_dust` | 1 | 32 |
| 3 | 1×`minecraft:nether_star` | 1×`occultism:witherite_dust` | 1 | 32 |
| 3 | 1×`minecraft:dragon_head` | 1×`occultism:dragonyst_dust` | 1 | 32 |
| 3 | 2×`occultism:demonic_meat` | 1×`occultism:iesnium_ingot` | 3 | 66 |
| 3 | 4×`minecraft:glow_ink_sac` | 1×`minecraft:echo_shard` | 4 | 16 |
| 3 | 16×`minecraft:sea_lantern` | 1×`minecraft:heart_of_the_sea` | 1 | 99 |
| 3 | 1×`minecraft:spore_blossom` | 1×`minecraft:dragon_breath` | 1 | 33 |
| 3 | 1×`minecraft:dark_prismarine` | 1×`minecraft:turtle_scute` | 2 | 8 |
| 3 | 1×`minecraft:brown_glazed_terracotta` | 1×`minecraft:armadillo_scute` | 2 | 8 |
| 3 | 1×`minecraft:purpur_pillar` | 1×`minecraft:shulker_shell` | 2 | 8 |
| 3 | 1×`minecraft:chiseled_sandstone` | 1×`minecraft:nautilus_shell` | 2 | 8 |
| 3 | 5×`minecraft:amethyst_shard` | 1×`minecraft:rabbit_foot` | 4 | 20 |
| 3 | 1×`minecraft:lava_bucket` | 10×`minecraft:blaze_rod` | 1 | 20 |
| 3 | 1×`minecraft:powder_snow_bucket` | 10×`minecraft:breeze_rod` | 1 | 20 |
| 4 | 1×`minecraft:chest_minecart` | 1×`occultism:satchel` | 1 | 27 |
| 4 | 4×`minecraft:end_rod` | 1×`occultism:ender_satchel` | 1 | 27 |
| 4 | 2×`minecraft:glowstone` | 1×`occultism:ritual_satchel_t1` | 1 | 27 |
| 4 | 2×`occultism:afrit_essence` | 1×`occultism:ritual_satchel_t2` | 1 | 27 |
| 4 | 1×`minecraft:soul_lantern` | 1×`occultism:fragile_soul_gem` | 1 | 27 |
| 4 | 32×`minecraft:soul_lantern` | 1×`occultism:soul_gem` | 1 | 27 |
| 4 | 1×`minecraft:wither_skeleton_skull` | 1×`occultism:familiar_ring` | 1 | 27 |
| 4 | 2×`occultism:demonic_meat` | 1×`occultism:storage_remote` | 1 | 27 |
| 4 | 1×`minecraft:sculk_catalyst` | 1×`occultism:knowledge_tablet` | 1 | 27 |
| 5 | 27×`minecraft:quartz` | 1×`occultism:dimensional_matrix` | 1 | 27 |
| 5 | 3×`minecraft:gold_ingot` | 1×`OccultismBlocks.STORAGE_CONTROLLER_BASE` | 1 | 27 |
| 5 | 3×`minecraft:gold_ingot` | 1×`OccultismBlocks.STORAGE_CONTROLLER_BASE_DARK` | 1 | 27 |
| 5 | 2×`minecraft:ender_eye` | 1×`OccultismBlocks.STABLE_WORMHOLE` | 3 | 27 |
| 5 | 2×`minecraft:ender_eye` | 1×`OccultismBlocks.STABLE_WORMHOLE_DARK` | 3 | 27 |
| 5 | 4×`minecraft:copper_block` | 1×`OccultismBlocks.STORAGE_STABILIZER_TIER1` | 3 | 27 |
| 5 | 4×`minecraft:copper_block` | 1×`OccultismBlocks.STORAGE_STABILIZER_TIER1_DARK` | 3 | 27 |
| 5 | 8×`occultism:spirit_attuned_gem` | 1×`OccultismBlocks.STORAGE_STABILIZER_TIER2` | 3 | 27 |
| 5 | 8×`occultism:spirit_attuned_gem` | 1×`OccultismBlocks.STORAGE_STABILIZER_TIER2_DARK` | 3 | 27 |
| 5 | 4×`occultism:afrit_essence` | 1×`OccultismBlocks.STORAGE_STABILIZER_TIER3` | 1 | 27 |
| 5 | 4×`occultism:afrit_essence` | 1×`OccultismBlocks.STORAGE_STABILIZER_TIER3_DARK` | 1 | 27 |
| 5 | 5×`occultism:marid_essence` | 1×`OccultismBlocks.STORAGE_STABILIZER_TIER4` | 1 | 27 |
| 5 | 5×`occultism:marid_essence` | 1×`OccultismBlocks.STORAGE_STABILIZER_TIER4_DARK` | 1 | 27 |
| 5 | 3×`minecraft:echo_shard` | 1×`OccultismBlocks.ENTITY_WORMHOLE` | 2 | 27 |
| 5 | 3×`minecraft:echo_shard` | 1×`OccultismBlocks.ENTITY_WORMHOLE_DARK` | 2 | 27 |
| 5 | 8×`occultism:afrit_essence` | 1×`OccultismBlocks.IESNIUM_SACRIFICIAL_BOWL` | 1 | 27 |
| 5 | 8×`occultism:afrit_essence` | 1×`OccultismBlocks.DARK_IESNIUM_SACRIFICIAL_BOWL` | 1 | 27 |
| 6 | 1×`minecraft:spyglass` | 1×`occultism:otherworld_goggles` | 1 | 32 |
| 6 | 1×`minecraft:end_crystal` | 1×`occultism:vitality_compass` | 1 | 32 |
| 6 | 2×`minecraft:diamond_block` | 1×`occultism:infused_pickaxe` | 1 | 32 |
| 6 | 5×`occultism:demonic_meat` | 1×`occultism:iesnium_pickaxe` | 1 | 32 |
| 6 | 8×`occultism:demonic_meat` | 1×`OccultismBlocks.DIMENSIONAL_MINESHAFT` | 1 | 32 |
| 6 | 4×`occultism:demonic_meat` | 1×`OccultismBlocks.DIMENSIONAL_BATTLEFIELD` | 1 | 32 |
| 6 | 2×`occultism:cruelty_essence` | 1×`OccultismBlocks.DIMENSIONAL_EXTRACTOR` | 1 | 32 |
| 6 | 8×`occultism:cursed_honey` | 1×`occultism:miner_foliot_unspecialized` | 1 | 32 |
| 6 | 9×`occultism:demonic_meat` | 1×`occultism:miner_djinni_ores` | 1 | 32 |
| 6 | 64×`occultism:demonic_meat` | 1×`occultism:miner_afrit_deeps` | 1 | 32 |
| 6 | 1×`occultism:dragonyst_dust` | 1×`minecraft:elytra` | 1 | 32 |
| 6 | 1×`minecraft:enchanted_golden_apple` | 1×`minecraft:totem_of_undying` | 1 | 32 |
| 6 | 1×`minecraft:ender_eye` | 1×`minecraft:turtle_helmet` | 1 | 32 |
| 7 | 1×`OccultismBlocks.IESNIUM_BLOCK` | 1×`occultism:spawn_egg/familiar_bat` | 1 | 24 |
| 7 | 1×`OccultismBlocks.IESNIUM_BLOCK` | 1×`occultism:spawn_egg/familiar_beaver` | 1 | 24 |
| 7 | 1×`OccultismBlocks.IESNIUM_BLOCK` | 1×`occultism:spawn_egg/familiar_beholder` | 1 | 24 |
| 7 | 1×`OccultismBlocks.IESNIUM_BLOCK` | 1×`occultism:spawn_egg/familiar_blacksmith` | 1 | 24 |
| 7 | 1×`OccultismBlocks.IESNIUM_BLOCK` | 1×`occultism:spawn_egg/familiar_chimera` | 1 | 24 |
| 7 | 1×`OccultismBlocks.IESNIUM_BLOCK` | 1×`occultism:spawn_egg/familiar_cthulhu` | 1 | 24 |
| 7 | 1×`OccultismBlocks.IESNIUM_BLOCK` | 1×`occultism:spawn_egg/familiar_deer` | 1 | 24 |
| 7 | 1×`OccultismBlocks.IESNIUM_BLOCK` | 1×`occultism:spawn_egg/familiar_devil` | 1 | 24 |
| 7 | 1×`OccultismBlocks.IESNIUM_BLOCK` | 1×`occultism:spawn_egg/familiar_dragon` | 1 | 24 |
| 7 | 1×`OccultismBlocks.IESNIUM_BLOCK` | 1×`occultism:spawn_egg/familiar_fairy` | 1 | 24 |
| 7 | 1×`OccultismBlocks.IESNIUM_BLOCK` | 1×`occultism:spawn_egg/familiar_greedy` | 1 | 24 |
| 7 | 1×`OccultismBlocks.IESNIUM_BLOCK` | 1×`occultism:spawn_egg/familiar_headless` | 1 | 24 |
| 7 | 1×`OccultismBlocks.IESNIUM_BLOCK` | 1×`occultism:spawn_egg/familiar_mummy` | 1 | 24 |
| 8 | 64×`minecraft:white_dye` | 1×`OccultismBlocks.SPIRIT_GRINDSTONE` | 1 | 1 |
| 8 | 64×`minecraft:light_gray_dye` | 1×`minecraft:heavy_core` | 1 | 1 |
| 8 | 64×`minecraft:gray_dye` | 1×`minecraft:reinforced_deepslate` | 1 | 1 |
| 8 | 64×`minecraft:black_dye` | 1×`minecraft:netherite_block` | 1 | 1 |
| 8 | 64×`minecraft:brown_dye` | 1×`minecraft:enchanted_golden_apple` | 1 | 1 |
| 8 | 64×`minecraft:red_dye` | 1×`minecraft:bell` | 1 | 1 |
| 8 | 64×`minecraft:orange_dye` | 1×`minecraft:end_stone_bricks` | 1 | 1 |
| 8 | 64×`minecraft:yellow_dye` | 1×`minecraft:sponge` | 1 | 1 |
| 8 | 64×`minecraft:lime_dye` | 1×`minecraft:emerald_block` | 1 | 1 |
| 8 | 64×`minecraft:green_dye` | 1×`minecraft:sculk_shrieker` | 1 | 1 |
| 8 | 64×`minecraft:cyan_dye` | 1×`minecraft:sculk_catalyst` | 1 | 1 |
| 8 | 64×`minecraft:light_blue_dye` | 1×`OccultismBlocks.IESNIUM_BLOCK` | 1 | 1 |
| 8 | 64×`minecraft:blue_dye` | 1×`minecraft:soul_lantern` | 1 | 1 |
| 8 | 64×`minecraft:purple_dye` | 1×`minecraft:respawn_anchor` | 1 | 1 |
| 8 | 64×`minecraft:magenta_dye` | 1×`minecraft:budding_amethyst` | 1 | 1 |
| 8 | 64×`minecraft:pink_dye` | 1×`minecraft:chorus_flower` | 1 | 1 |

## Demonic Husband/Wife optional companion

Two separate summon_tamed rituals, each bound Djinni Book, summon_djinni pentacle, counter120 and chicken-tag sacrifice. Husband: Gold Ingot, Emerald, Gunpowder, Porkchop, Sword-tag item, Glass Bottle. Wife swaps Emerald for Diamond; other ingredients same. These are optional companions, not prerequisites for all spirit work. Owner is assigned on successful eligible native tamed summon; an untamed/nonowned interaction branch can tame with Diamond when animal-tame hook permits. The method does not explicitly reject an already-tamed other-owner partner before that diamond branch, so do not advertise permanent private ownership enforcement.

Owned companion interactions in pinned `DemonicPartner.mobInteract`:

- Cursed Honey after strict worldTime>heartTime+12000 consumes1 and gives Sweet Honey Heart; no refund when on cooldown because input is not spent in that branch. Heart food's native absorption is amplifier9 with durationInteger.MAX_VALUE; it is a significant optional survival payoff, not an ordinary cosmetic food.
- Item with PotionContents effects applies each to player: noninstant duration×5 at original amplifier; instant effect is invoked at amplifier+2 and factor1. Noncreative consumes1 then gives crafting remainder or Glass Bottle. This is not general “all potions positive”: harmful/custom effects also follow the loop, and final effect behavior belongs to each effect implementation.
- Suspicious Stew component noninstant effects use duration×50, amplifier0; instant uses amplifier1. Calls player food eat twice with Suspicious Stew food properties; noncreative consumes1 and gives Bowl. No assertion that arbitrary custom foods implement this component.
- Matching SmokingRecipe input can be cooked immediately; nonsneak one item, sneak whole offered stack, result count scales with input count. No fuel/time debit or cooking XP in that interaction method. This cached-recipe branch was read with CFR; custom dynamic/component-sensitive smoking results need acceptance, so do not promise universal modded-food fidelity.
- Partner-food tag heals according to food nutrition (fallback1) when injured, consuming1 noncreative; empty-hand owner interaction can toggle sit after ordinary interaction handling. No breed offspring route is invented from TamableAnimal inheritance.

True death loot produces full-health/reset-fall/cleared-effect Soul Shard payload for owner or world drop, then native resurrection rituals can restore eligible DemonicPartner ownership. Shard presence is not proof an unchecked addFreshEntity restoration succeeded. The ordinary familiar ring should not be assumed to hold this separate partner class.

## Important intermediate material routes

These are necessary branch prerequisites that a simple crafting-only recipe list can hide. Follow named possession ritual, then collect real loot; displayed ritual result is not immediate material delivery.

| Needed branch material | Exact native route | Required setup / inputs | Actual result and next action |
| --- | --- | --- | --- |
| Cursed Honey →Orange Chalk | `ritual/possess_bee`, `possession_rituals/possess_bee` | boundDjinni Book, possess_djinni preview, counter60; Honeycomb+Honey Block+Honey Bottle+Honeycomb Block, chicken-tag sacrifice | Summons Possessed Bee. Kill it; native loot set_count uniform0.5..1 rounds endpoints to1 under target NumberProvider integer semantics, so base1Honey before modifiers. Mix Orange Impure recipe and fire-purify. |
| Research Fragment Dust →Lime Chalk | `ritual/craft_research_fragment_dust` | Exact Foliot/Djinni/etc pentacle and activation in ritual table; Emerald Dust +twoExperience Bottles | Actual craft output1Research Fragment Dust. Mix White Impure+Research Dust tag+Emerald Dust+Slime Ball, fire-purify. Bottles are item ingredients, not ambient XP input. |
| Gray Paste →Gray Chalk | `ritual/craft_gray_paste` | Exact ritual table; Gunpowder+Clay Ball+Phantom Membrane+Gray Dye | Actual craft output1Paste. Mix with White Impure, fire-purify; supplies foundation-dark option before Unbound Afrit. |
| Witherite Dust →Black Chalk | `ritual/craft_witherite_dust` | Exact ritual table; Netherite Dust+Wither Skeleton Skull+Blackstone Dust+Wither Rose | Actual craft output3Dust, enough for oneBlack Impure recipe (plus White Impure); fire-purify. This is not necessarily a Wither kill/drop requirement. |
| Cruelty Essence →Brown Chalk | `ritual/possess_goat` | Exact ritual table; twoPointed Dripstone+fourRabbit Foot+fourArmadillo Scute+twoWool | Summons Mercy Goat; its native entity loot yields base1Cruelty Essence plus Looting adjustment. Kill/output separate from ritual. Brown Impure uses essence+Cocoa+Brown Mushroom+White Impure. |
| Demonic Meat →Pink Chalk | possession Zombie Piglin ritual | Exact recipe/pentacle/event in158ritual table | Actual possessed mob loot1–4Meat before Looting; Pink Impure uses threeMeat+White Impure. Do not conflate cooked Porkchop. |
| Echo Shards →Echo Dust/Cyan Chalk | Possessed Warden or chosen vanilla source, then registered crushing recipe | Use exact ritual/processing rows; do not substitute generic Warden summon assumptions | Native Possessed Warden loot6–9Echo Shards. Process to needed dust, combine Cyan additives. Combat risk/real drop separate. |

Spirit/narrative book category has overview, true_names, essence_decay, wild_hunt and unbound_spirits entries. These references do not introduce an undocumented hidden global unlock system: operational gates are the actual recipes/pentacles/events, worker max-age and native entity behavior. Full future quest prose should distinguish story from measured/triggered progression.

## Teaching and boundary evidence

Guide references: corresponding Summoning/Companion/Posssession entries (exact230entry crosswalk), JEI ritual and named material recipe. Teach ordinary catalogue vs ThirdEye/goggles/staff catalogue, randomness/maxUses, actual paid purchase, companion owner/action/cooldown and intermediate mob-loot result. Quest inventory proves acquisition only; event feedback may precede output. Give XP/useful supplies, not free Heart/Elytra/Netherite/late miner before its operation/gate lesson.

Unrun tests: first trader interaction under each vision state; sampled group vs complete pool; exhausted trade/reload; named-bound result; valid/invalid/other-owner companion interaction; potion instant/noninstant/harmful/remainder, stacked cooking and custom recipe; Honey exact cooldown; possessed-mob actual loot and chalice/sacrifice event. Rare catalogues/native potential do not authorize a TMM balance change or demonstrate first-boss gate bypass.

Sources: exact `common/entity/spirit/wonderingtrader/{WonderingTraderEntity,WonderingTrades$ItemTrade}`, `handlers/ForgeEventHandler`, `common/entity/spirit/demonicpartner/DemonicPartner` and Husband/Wife subclasses, `registry/OccultismFoods`, native ritual/entity loot and Dictionary resource strings. No implementation dumps/assets committed.
