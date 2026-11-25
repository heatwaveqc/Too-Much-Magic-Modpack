# ProjectE ecosystem — native capability Atlas (1.21.1 NeoForge)

**Native research COMPLETE, not gameplay acceptance.** Three exact-release passes for ProjectE PE1.1.0, Project Expansion 1.0.6 and ProjectE Energy Converters 0.1.4. #410. Teach representative operation and tier progression, not every color recipe. Separate native defaults from existing TMM source policy; no new gates, balances, runtime or quests.

## Exact source evidence
| Module | Pin and hash | Packaged records |
|---|---|---|
| ProjectE | [pin](../../../mods/projecte.pw.toml), CF226410/6611984, SHA1 24fb5997727453a15e956522761a3f586e646c2a | 1901 ZIP entries,721 classes,157 recipes,19 nonrecipe advancements |
| Project Expansion | [pin](../../../mods/project-expansion.pw.toml), MR a7yNQPzW/F5C1OobM, SHA512 97e832da2ca03309f4ef875b42a9250bba3798453f271f82b441e7c0bf739e103ee9f4274839b5e4d9a154a44167cc0f8169bd97ed37b3d16e7e09d34cfe3c85 | 1487 entries,282 classes,248 recipes,81 advancements |
| Energy Converters | [pin](../../../mods/projecte-energy-converters.pw.toml), CF1727449/9072881, SHA1 22e6a476508b9d9d9fa35cbfdecf5677ec5c623c | 437 entries,15 classes,30 safe_upgrade recipes |
Owner JARs reopened CRC passes; exact recipe/locales, advancements, targeted javap -c -p -constants. Pass [1](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/410#issuecomment-7172961264), [2](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/410#issuecomment-7173293838); final pass targeted ItemStar, StarType, ItemFinalStar, AlchemicalBook tiers, KnowledgeSharingBook, ArcaneTablet container, EMCLink inner handlers, PowerFlower/Collector, Util, AdvancedAlchemicalChest, ConverterEntity/Tier/Ledger/UpgradeRecipe/OpBridge. Source snapshot 41a31bb8ddf241422b1a05f90aa9c3bcfd548dc9; no binary-to-upstream source equivalence claimed, no JAR committed. Native configs not effective pack settings.

## Entry: native versus TMM
Native Stone: two redstone/glowstone/diamond orientations, reusable crafting remainder, portable crafting, world transmutations and selectable shapes. TMM disables both native recipes. **Actual current source** [Ritual of Equivalent Exchange](../../../kubejs/data/tmm/recipe/ritual/equivalent_exchange.json): Occultism craft_marid pentacle, bound Marid activation book, duration600t, one input from each of12 tags (primordial powers, masters of magic, ancient horrors, titans of destruction, fallen sovereigns, conquerors of other worlds, arcane metallurgy, celestial artifacts, forbidden witchcraft, blood and spirit, magical artifice, sufficiently advanced magic), result Stone. Older boss-center proposal superseded. Item-based, not mandatory personal kill. MK1 collector/relay center top Stone replaces glass. TooltipMilestonesRuntime first possession in inventory/ender chest, permanent acquisition bits, pending CovalenceKnowledgeGrant native API, no free items or EMC. #44: low/medium/high dust native crafts disabled, virtual value edges preserve mapping; live rod recipes 8 Neo Vitae iron dust+stick,8 Neo Vitae diamond dust+rod1,8 Occultism netherite dust+rod2. Repair Talisman Eternal Stella + Iesnium Anvil, alternate disabled; water/lava sigil replaces one bucket in respective amulet. No claim live JEI/recipes or effective EMC mapping accepted. 
## Native base systems
Table four obsidian/four stone/Stone; Tablet four dark matter blocks/four stone/Table. Same personal native knowledge capability, BigInteger account; learn/unlearn, search, lock, consume and materialize affordable learned item; not separate table accounts. Item values and several APIs signed long. 19 base nonrecipe advancements inventory_changed acquisition, not action proof. Alchemical Chest104 slots,1000 EMC local; colored Bags player capability. Condenser MK1 chest+4 diamonds+4 obsidian, one input/output per tick; MK2 MK1+4 dark blocks+4 red blocks, separate input/output and multiple results bounded by space/charge. Both require target template, not personal knowledge. Klein Star capacity 50,000*4^n n0..5 (Ein..Omega), native stored component. Do not confuse item EMC value with stored EMC.
Dark Matter 8 aeternalis fuel+diamond block, Red Matter6 aeternalis+3 dark. Base collectors 4/12/40 EMC/s, buffer10k/30k/60k; generation genRate*(lightAbove+1)/320 per tick, ultraWarm dimension max light. Fuel upgrade and charge stars. Relays64/192/640 nominal rates,100k/1m/10m storage; connection bonus1/3/10 EMC/s, not independent generation. Dark/Red Furnace base10/3 ticks, fuel efficiency3/4,9/13 input/output slots,64 buffer and2 EMC/t alternative fuel. Ore doubling chance.5/1, raw material .333/.667 according to actual tags and smelting recipe. Economy mapping separate.
Matter tools no ordinary damageItem cost, charged mining modes and composite Morning Star/Katar. Divining Rod 3/16/64 depth scans drop EMC average/high not exact ore coordinates. DM/RM/Gem armor native full-set base reduction80/90/90%, piece weights20/30/30/20; damage type caps/bypass not universal immunity. Gem helmet heal/night vision, chest fire protection, legs mobility, boots step assist; offensive abilities false default. Stone portable craft and world transmutation modes; Mercurial Eye EMC/template construction/replace modes; Destruction Catalyst/Catalytic Lens excavation, Hyperkinetic Lens projectile; Gem of Eternal Density condenses to chosen higher value, Black Hole Band attraction, Void Ring combines. Repair Talisman inventory/pedestal repair20t, chest/bag handling; current TMM narrow eligible Apotheosis single noninstant potion charm predicate, not generalized repair. Evertide native water, opEvertide false; Volcanite tooltip32 EMC per operation; native policy distinct. Mind Stone XP int capacity, Body/Soul stone charge64 per personal use, Life Stone availability128 and separate64 costs; SWRG shield/flight repulsion range5 availability64 not verified per-tick debit; Ring of Arcana cycles Zero/Ignition/Harvest/SWRG. Flight/enchant/claims/creative policy #330/#328/#596 downstream.
Dark Matter Pedestal item-driven, range4 default. Empty-hand toggle, insert/remove; native default cooldown ticks Archangel40,Body10,Evertide20,Harvest10,Ignition40,Life5,Repair20,SWRG70,Soul10,Volcanite20,Zero40; -1 disables. XP, repair, collection, agriculture, weather/combat utility, no universal resource debit for pedestal mode. Watch of Flowing Time native enabled,18 bonus ticks and mobs .1 speed, excludes certain tickers, not universal machine acceleration; personal cost (charge+2)/2 per tick. Interdiction Torch default8 range hostile mode. Cross-mod TPS/economy risks noted not new restrictions.

## Project Expansion progression and account operations
16 matter tiers Basic, Dark, Red, Magenta, Pink, Purple, Violet, Blue, Cyan, Green, Lime, Yellow, Orange, White, Fading, Final. Nominal generation collector4*6^n EMC/s; relay bonus1*6^n; relay transfer64*6^n except Final special long maximum. Compressed collectors and flowers. Native Power Flower (18 collectors+30 relay bonuses)*config multiplier =102*6^n EMC/s at default multiplier, tickDelay20; Final47,958,868,426,752. Power Flower **banks BigInteger generation locally while owner absent if loaded**, credits stored+new via PowerFlowerCollector on online lookup, clears local buffer; no offline account debit, no force-loading guarantee. Native Final Star plain item (no EMC holder methods), recipe8 final flowers+dragon egg; Final Star Shard8 Colossal Omega+Nether Star; Compact Sun four Final Stars/four Shards/yellow matter, native default bonus10x, price compensation enabled may raise bonus; don't promise exact effective boost. 
All14 Red-through-Final direct TMM Power Flower overrides verified Chaos Shard replaces one relay; Red upgrade retains Dark Flower and adds shard. Native Matter Upgrader recipe Basic flower, links, collector, relay, dark matter, dye; learned next machine and EMC difference upgrades placed block without ordinary craft. Do not claim shard gate is universal: knowledge, trade and upgrader paths separate existing gate QA. 
EMC Link basic recipe covalence dust, two tablets, condenser; imports item/EMC, exports configured item/fluid within remainingEMC/import/export/fluid limits, Final special limits. Owner provider Util.getKnowledgeProvider(UUID) returns null if player offline, not an offline wallet. **Native EMCLink EMCHandler.extractEmcBigInteger returns requested positive amount with no matching debit**; do not teach direct EMC extraction. Energy converter explicitly refuses extraction from link; use legitimate charged star or storage peer. Source-backed limitation, not request to exploit/fix. Advanced Alchemical Chest exposes matching colored owner bag, getBag returns null offline. MK3 condenser5 MK2+4 magenta blocks, five independent sides and shared bottom output. Arcane Tablet4 tablets,2 magenta matter, crafting table,chest,Magnum Ein; same native account,16 output slots,3x3 crafting grid and recipe transfer/refill/rotate/balance/spread. Knowledge Sharing Book recipe six violet matter,2 nether stars,book; native copies missing knowledge via addKnowledge, no new EMC account. Source and recipient online lookup; TMM scroll filter still applies. Alchemical Books Basic/Advanced/Master/Arcane, destination management; tier distance cost coefficients1000/500/100/0, Advanced+ bind player; use native UI and actual travel policy, no unverified cross-dimension promise. Infinite steak64 EMC per use/fuel128 per1600t burn native paid reusable items, not creative resource generator.
Star progression exact ItemStar array204800000*4^n n0..17, offsets0/6/12; four previous stars craft next, native plain shapeless records, verify stored component handling before charged upgrade (recommend empty). Magnum Ein204800000/Omega209715200000; Colossal Ein838860800000/Omega858993459200000; Gargantuan Ein3435973836800000/Omega3518437208883200000. Distinct from Final Star material.
## Energy Converters and TMM resource bridges
15 tiers skip Pink; two directions, 30 safe_upgrade recipes, default tier EMC budget16*4^ordinal/t, native config forward/reverse1000 per unit, 200t capacity or one batch. Server-side only; redstone signal pauses, recursion guard; one per-tick aggregate movement budget, loops six sides; charged-star slot via native EMC capability, item upgrade recipe rejects charged holder ingredients, preserves allowed converter component patch (block entity data/container/custom name/lock), no arbitrary components. OP bridge uses long native peer if present, otherwise FE int interface, clamps request; simulation then actual transfer, no assumed external network compatibility. ConverterEntity explicitly blocks native link extraction because no debit; use charged star or actual storage. Ledger stores emc, energy, conversionCredit; TMM OpExchange constants nominal200 OP/EMC, actual160 OP per EMC and250 OP per EMC reverse, .64 roundtrip. Config mixin forward1 internal batch, reverse250; forward tier port capacity/rate scale160, ledger matched bounded debit/credit, no free transfer. Source-only QA. 
TMM EMC Catalyst (Conjuration Catalyst+Basic EMC Link) under ordinary Botania pool,20 Mana/1 EMC; EMC Source Jar (Source Jar+Basic Link) native SourceJarTile subclass 10k,4 Source/3 EMC. Both AccountRefill directly queries online actual placer's UUID native capability, server thread, loaded target, capacity, redstone pause; debits before receive, refunds only definite unused batches, sync; no fake offline wallet, no link extraction, no force load. Source on placement retains legitimate fill component, new owner. Neither reverse bridge claimed. Preserve existing valuation/resource policy. 
TMM protections: ScrollKnowledgeMixin denies learning/hasKnowledge, filters even full knowledge; corresponding output/consume/import/return paths existing, not universally zero EMC. TomeMaterializationMixin strips modified component template on creation for Tome/relic, physical inventory unchanged; existing condenser/template guards. Effective recipe mapping/positive EMC coverage and all cross-mod integration QA separate. Ore/raw blacklist mappers disabled in mapping.toml. Not every value/crafting path reproduced here.

## 29-axis navigation
| Axis | Finding |
|---|---|
| gear | Matter armor/tools, Gem armor, charged artifacts |
| movement | SWRG, Alchemical Books, Gem mobility; policy #330 |
| ore_processing | Matter furnaces ore/raw doubling |
| storage | Personal EMC, stars, buffers, bags/chests |
| crafting | Stone craft, Arcane Tablet, transmutation and matter recipes |
| logistics | Collectors/relays/Links, colored bag access, condensation |
| buffs | Gem effects, life/body/soul stones, rings |
| external_compatibility | Expansion, converter, TMM bridges, mapping and policy hooks |
| mob_farming | Pedestal attack/repulsion and item pickup; no native spawner asserted |
| ore_generation | Learned/template synthesis, separate source gate |
| permanent_power | Learned knowledge and permanent first acquisition milestone in TMM |
| general_generation | Light generation, flowers, EMC materialization |
| direct_damage | Matter combat, rings and pedestal attacks |
| block_breaking | Charged tools and destruction artifacts |
| permanent_pets | No native persistent helper evidenced |
| temporary_pets | No native temporary helper evidenced |
| defense | Matter reduction, Gem armor, shield and torch |
| healing_recovery | Repair, health/hunger, mind stone XP |
| debuff_control | Zero/ignition, time slow, repulsion |
| energy | Distinct account/item/block EMC, OP/FE, TMM Mana/Source |
| automation_control | Condensers, links, flowers, pedestal and converter redstone |
| agriculture | Harvest Goddess Band, pedestal growth/harvest |
| detection_information | Divining Rod, native knowledge and EMC display |
| equipment_augmentation | Star charges, matter upgrade, repair |
| world_manipulation | Stone/Mercurial Eye, excavation and fluid amulets |
| world_content | No native new dimension/boss asserted |
| death_respawn | No native new death system evidenced |
| trade_economy | EMC learning, exchange, knowledge sharing, TMM shop policy |
| progression_tiers | Base3, expansion16, converter15, star24; Stone and Chaos recipe gates |

## Representative instructional map
Teaching dependencies not mechanical locks; acquisition checks distinct from actual operations, especially owner account, source and destination debit. Native recipes/JEI for intermediate colors, no per-tier duplicate quest spam.
| ID | Prior lessons | Teach / evidence |
|---|---|---|
| PE-01 | — | Read Ritual of Equivalent Exchange requirements; collect 12 category ingredients |
| PE-02 | PE-01 | Perform Marid ritual, acquire Stone and verify first knowledge unlock |
| PE-03 | PE-02 | Stone reusable crafting/portable crafting and permitted transmutation |
| PE-04 | PE-02 | Learn/unlearn sample, verify EMC balance and item recreation |
| PE-05 | PE-04 | Portable Tablet and shared account |
| PE-06 | PE-02 | Three dust knowledge grant and repair material values; no physical craft |
| PE-07 | PE-02 | Divining Rod I–III, interpret reported values |
| PE-08 | PE-02 | Alchemical Chest and colored Bag |
| PE-09 | PE-02 | Alchemical fuels and Dark Matter |
| PE-10 | PE-09 | Red Matter and block materials |
| PE-11 | PE-02 | MK1 Collector/Relay source gate, light and charge |
| PE-12 | PE-11, PE-09 | MK2 then MK3 generation/transfer and bonus |
| PE-13 | PE-11 | Klein Star Ein→Omega storage; charge/discharge safely |
| PE-14 | PE-08, PE-04 | Condenser MK1 template and input/output |
| PE-15 | PE-14, PE-10 | Condenser MK2 separate outputs |
| PE-16 | PE-09 | Dark/Red Furnace, ore versus raw doubling and fuel |
| PE-17 | PE-09 | Matter tools, charge/mode and mining |
| PE-18 | PE-10, PE-17 | Composite Red Matter tools |
| PE-19 | PE-10 | Dark/Red armor and Gem upgrade, approved abilities |
| PE-20 | PE-06 | Repair Talisman TMM recipe and eligible charm repair |
| PE-21 | PE-09 | Water/Lava Sigil amulets and costs |
| PE-22 | PE-13 | Body/Soul/Life and Mind Stone, charge versus XP |
| PE-23 | PE-13 | Item attraction and condensation, Void Ring |
| PE-24 | PE-13 | Construction/excavation artifacts; claims |
| PE-25 | PE-13 | SWRG and Ring of Arcana; approved flight only |
| PE-26 | PE-09 | Dark Matter Pedestal and representative repair/agriculture/combat |
| PE-27 | PE-26 | Watch of Flowing Time, performance caution |
| PE-28 | PE-05, PE-14 | Basic EMC Link with actual account-backed item input/output; no raw extraction |
| PE-29 | PE-28, PE-12 | Basic/Dark Power Flowers, production and owner offline banking |
| PE-30 | PE-29 | Chaos Shard and Red flower direct/upgrade; distinguish source gate |
| PE-31 | PE-30 | Magenta through Final ladder, representative higher collector/relay/flower |
| PE-32 | PE-13 | Magnum/Colossal/Gargantuan stars and safe empty upgrade |
| PE-33 | PE-31, PE-32 | Final Star Shard, Final Star and Compact Sun |
| PE-34 | PE-28, PE-08 | Advanced Alchemical Chest owner bag access |
| PE-35 | PE-31, PE-15 | MK3 Condenser and side inventory |
| PE-36 | PE-31, PE-32, PE-05 | Arcane Tablet native recipe transfer/craft |
| PE-37 | PE-28 | Matter Upgrader learned machine and account price; existing gate QA |
| PE-38 | PE-31 | Alchemical Books travel and Knowledge Sharing Book; policy |
| PE-39 | PE-28 | Paid Infinite Steak/Fuel |
| PE-40 | PE-28, PE-13 | Basic EMC→OP machine legitimate stored source, verify 160 output |
| PE-41 | PE-40 | OP→EMC verify 250 debit, round-trip loss, redstone stop |
| PE-42 | PE-41, PE-31 | Converter tier ladder and safe upgrade; no raw EMC Link extraction |
| PE-43 | PE-28 | TMM EMC Catalyst under ordinary pool, online owner and redstone |
| PE-44 | PE-28 | TMM EMC Source Jar, paid refill and online owner |
| PE-45 | PE-30, PE-33, PE-42 | Endgame resource project demonstrating legitimate generation, storage and exchange |

## Remaining boundaries
No runtime verification of loaded effective config, recipe mapping/EMC, personal or shared unlock, machine gate bypasses, stored-component handling, claims/flight, offline resume or compatibility with other mods. Exact individual artifact coefficients and optional travel behavior deferred to native UI/guide when authoring; no generic gameplay QA prerequisite. Next Apotheosis family, already installed and integration-researched but no dedicated Atlas profile.
