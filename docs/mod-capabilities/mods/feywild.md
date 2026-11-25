# Feywild 5.6.4 — native capability Atlas

**Research COMPLETE at native quest-source depth; no gameplay acceptance.** Companion-centered magic, autonomous local production, trade, seasonal gardens, guardians, native court stories and four sovereign encounters. Use Feywild Lexicon + EMI + Quest Giver + TMM introductory quests, not duplication of the whole native story. Existing TMM flight/capture/enchanting work owns applicable integration concerns.

## Evidence
[Packwiz](../../../mods/feywild.pw.toml) Feywild 1.21.1-5.6.4, CF484377/9321431, owner file feywild-1.21.1-5.6.4(1).jar 23,561,247 bytes, SHA1 e59b68dd85b92ebbdbde478ea4e52fdb8b84c9d0 exact, SHA512 5908172838dac544ddbb77aa3079bddb61ef2c1084b0bd79df22bc22d323a4bbbae6c95fcc317af4e36ee3c7315baefa27f03856123c80542e51392368f909c0, CRC PASS rechecked,2962 entries333 classes. Namespace feywild, NeoForge>=21.0.118-beta, MC1.21–<1.21.2, Patchouli>=1.21 and Quest Giver>=1.5.2 required. Optional Curios path in code. [Version-aligned source](https://github.com/SaphieNyako/Feywild/tree/2a339f98a5a2ea0d5620801538095a7b013b6de2); actual JAR JSON, locale, targeted javap -c -p -constants, not full source equivalence. 179 native recipes (105 shaped/32 shapeless/31 fey altar/11 smelting),61 additional minecraft namespace recipes,207 loot,148 native+61 vanilla advancements,24 worldgen (12 configured/12 placed),13 biome modifiers,3 trade JSON,7 Supplementaries catchable properties,1 Curios data,38 English Lexicon entries/13 categories and61 Quest Giver records. [Prior partial profile](../../mod-comprehension/mods/feywild.md), [pass1](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/410#issuecomment-7151861609), [pass2](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/410#issuecomment-7151931820), [pass3](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/410#issuecomment-7151976797). No JAR redistribution, gameplay or config changes.

## Entry and summoning
Feywild Lexicon Patchouli grant first login (default true), persistent feywild_got_lexicon flag after inventory add; recovery Dust+book. Fey Gem ore vein size5 count9, above_bottom -64..32 range as packaged, stone/deepslate loot1–3 gems Fortune, Silk Touch ore. 4 Dust→gem, smelt gem100t→1 Dust (lossy, not reversible parity). Wild cookie interaction may yield Dust per guide; cookies heal; exact probability not taught. Dust levitation on living target via config and food effect, sheep callback, not permanent flight. Mandrake root recipe poisonous potato+Dust, grow farmland; Ink root+glass bottle+ink sac; blank scroll Ink+feather+paper. Titania or Mab altar 3 smooth stone+gem+slab. Both six slots (5 inputs/output),40t, unordered multiset match, one each consumed, resets on invalid/no output space, no AP/Source/fuel/alignment or quest check found. Pixie Orb Dust+2 gems+2 pearls, ability/follow/stay/dismiss controls. All seasonal scrolls gem+blank+three offerings: Spring flower/cake/wool; Summer honeycomb/gold sword/melon; Autumn mushroom tag/hoe tag/pumpkin; Winter snowball/rotten flesh/bone. Native summon sets owner and summon position and consumes scroll. 31 altar recipes: 4 Pixies,4 other companions,4 Ents,4 bosses,4 saplings,6 wing tiaras,3 giant flower seeds,orb,disc. Use actual recipes/EMI; no obsolete wiki guide imported.

## Companion AI, local automation and logistics
FeyBase saves Owner/SummonPos/FollowingPlayer/AbilityActive/VoiceActive; persistent/no far despawn. IOwnable resolves owner UUID via current Level.getPlayerByUUID; four seasonal ability goals require same-level ServerPlayer and ability active, chunk ticking, randomized eligible AI evaluation, not guaranteed fixed cycle or offline production. No food/energy debit in these goals.
| Worker | Actual operation and limits |
|---|---|
|Spring Pixie|.02 chance eligible goal check; adult Animal within8 AABB; partner within8, temporarily tests love time/canMate, actual spawnChildFromBreeding, cooldown/compatibility; no feed cost|
|Summer Pixie|.01 goal check; 17³ cube ±8, each CropBlock .16 chance growCrops, executes at ticksLeft20 in30t goal; not all bonemealable blocks|
|Autumn Pixie|.01 goal check; same cube, first mature CropBlock passing .16 chance, set age0 flags2, clone item rather than harvest loot, wheat seeds→wheat/beet seeds→beetroot, drops one stack at pixie ticksLeft5; no container routing/whitelist, modded crops may yield seeds|
|Winter Pixie|.01 goal check; nearest adult Animal within8, excludes tamed AbstractHorse/TamableAnimal, 55t goal; actual loot table with ENTITY context (entity/origin/magic damage), no player/Looting/XP; drops before doHurtTarget, independent of successful damage, may kill and yield normal death loot; Pixie maxHP12/baseattack3, not nonlethal Drygmy or generic boss farming|
|Mandragora|.005 goal check,45t sing, variant flower placement on suitable dirt/air and canSurvive, direct block update; flower interaction changes variant|
|Shroomling|.005 goal check, Cow within8, replaces with vanilla or colored MushroomCow, no full old NBT preservation established, new variant saved, native stew/milk/shear; not ordinary slaughter automation|
Comparison: Hexerei crow tagged crop/whitelist harvest, distinct gathering to enabled coffer/jar, owl courier; Ars Starbuncle assigned routing, Whirlisprig Source, Wixie crafting, Drygmy Henge1000 Source per cycle nonlethal loot/XP gems with TMM replication eligibility. Feywild ambient local ability, ordinary item entities collectable by compatible pickup routes; no special adapter proven. Spring+Summer+Autumn and Winter can support farm, crow/Starbuncle collects; configure and test actual route. No unrestricted machine network, fluid routing, generic automated crafting or player-away guarantee. 
## Barter, Bellsnickel and equipment
TradeForGemsGoal nearest dropped gem within inflated4, move speed1.2, trades within2 blocks, shrinks one and spawns output item entity. No ownership predicate in this goal, no Source, actual chunk/AI limits. Native 3 data tables, trader chooses uniform index in generated list; data rarity may expand list, no equal type odds claim. Mandragora flowers/vines/lichen/moss/azalea/Wither Rose/giant seeds; Shroomling colored fungi, nylium, mycelium, poison potions/arrows and suspicious stews; Bee Knight honey/honeycomb, saddle/gold horse armor, hive/nest. Bellsnickel code-only fourth pool from every registered enchantment at every min..max level, uniform index across entries, no treasure/curse/blacklist exclusion found. Book copier consumes1 book+1 Fey Ink, copies complete enchanted book stack data count1, template stays, no XP or enchant filter. 22 slots (18 bag +4 book/copy slots). Not persistent portable storage on dismiss: KILLED or DISCARDED removal drops inventory, ordinary unload path separate. Do not assume book economics exempt from existing enchanting review.
Bee Knight and Bee Mount base24HP each; knight baseattack3. Guardian modes owner/area, linked mount, 3 inventory slots on mount (mount armor, knight armor, spear), saves Items/KnightUUID. Gold/diamond mount armor, gold/diamond/netherite knight armor/spear, stat modifiers via actual equipment; no player riding established. Pair removed together, BeeMount remove calls dropInventory in server paired-removal path without filtering removal reason; empty gear before dismissal, unload/save/dimension QA. Tree Ents are player mounts, not Bee Mount. 
FeyBase damage block predicate true for living attacker unless holding tagged tools AND TieredItem Tiers.IRON; then vanilla block check. Subclass overrides and actual damage pipeline/bypass tags matter, not blanket immunity or any iron-looking weapon. DismissEntityMessage creates new generic scroll and removes entity, not serialize complete pet; no promise name/variant/inventory persistence. Guide dismissal can reset variant. Packet handlers appear incomplete server owner/type/proximity validation (dismiss and blessing), flag downstream multiplayer review, no tested exploit. 
## Ents, natural spawns and blessings
Seasonal saplings Dust+4 corresponding ordinary saplings; four seasonal woods, giant flowers, seeds, 24 worldgen/13 biome modifier records, no separate dimension established. Cracked logs natural1/100 and sapling1/1200 defaults, block entity tracks Ent UUID. Every200t day: if tracked missing/dead find grass within10 and spawn seasonal wild Ent; night discard tracked live and clear. Repeat encounter generation, no single lifetime cap, potential duplicate if unloaded UUID lookup fails; no globally guaranteed one. Native tree_ent_attack_players=true; wild territorial, owned guardians. Ent drops dust,1–3 logs,0–2 saplings, not automatic TMM loot policy changes.
Ent scroll matching Pixie+gem+lesser Fey scroll+blank scroll+seasonal sapling: Spring/Mandragora, Summer/Bee Knight, Autumn/Shroomling, Winter/Bellsnickel. TreeEntBase120HP, attack12 default configurable, armor5/toughness2, speed.15,jump1.5,step1.2, knockback resist2 (actual clamping separate); owner riding/jump, protect against monsters. Separate seasonal blessing effects repel qualifying mobs radius1.4+.75*amplifier, strength1.5; not generic invulnerability. Blessing duration/packet ownership review.
## Sovereigns and native quest stories
Boss scroll matching Pixie+gem+lesser Fey+blank+Ent scroll, Oberon spring, Titania summer, Ashen Lord autumn, Mab winter. All300HP and hostile BossBase; Oberon charge/kick/rear, Titania cast/enchant, Mab vex/intimidation, Ashen leaf attacks/shield/retaliation. Custom death sequence disables AI/invulnerable, delayed removal, ordinary tickDeath suppressed. Actual kill/loot/XP attribution QA if pack-owned hooks; all four native loot tables base Wing Tiara guaranteed normal loot without player-kill predicate. No hard quest check in altar, no assumption all companion iron-block behavior applies to boss.
61 actual Quest Giver records incl roots: Sprite11, Spring13, Autumn13, Summer12, Winter12. Native numbered chains parent previous; quest_line_names only Sprite and link file empty; entity dialogue/summon special callbacks native, no promise universally unlocked menus. Sprite introduction dust→mandrake→ink→blank→altar→Spring/Autumn/Winter/Summer scrolls, Orb rewards, final structure_void sentinel not ordinary obtainable objective. 
| Court | Native ordered task families |
|---|---|
|Spring|pet3 sheep, dust3 sheep,3 dandelion fluff,2 cakes, summon Mandragora/Shroomling, hit Spring with snowball, Spring Ent, Oberon scroll/kill, Spring Tiara, repeatable sentinel|
|Autumn|diamond sword gift, smoker, tame/name wolf, Shroomling/Mandragora, heal potion gift, Autumn Ent, Ashen scroll/kill, Autumn Tiara, sentinel|
|Summer|gold chestplate, iron golem kill, bee nest,8 pillagers, Bee Knight/Bellsnickel, Summer Ent, **Mab** scroll/kill, Summer Tiara, sentinel|
|Winter|8 rotten flesh, soul lantern,8 pillagers,3 wither skeletons, Bellsnickel/Bee Knight, Winter Ent, **Titania** scroll/kill, Winter Tiara, sentinel|
Summer and Winter cross-court boss requirement differs from altar ingredients, exact data, no claim deliberate design. Quest rewards Totem on boss scroll, Netherite ingot on seasonal wing,3 Nether Stars after kill; Autumn quest10 invalid-looking minecraft:netherite_star and some minecraft:summoning_scroll_* icons; Summer quest7 command give @p regeneration stew. Native reward gate/data parsing review separate, no correction without authorization. Do not reproduce every native task in FTB; use observable tutorial milestones and point to dialogue, sentinel not a quest item reward.
## Flight and existing TMM ownership
Base Wing Tiara is ExplosionProofItem, not functional flight. Six variant recipes Elytra+base Tiara+Nether Star+Fey Dust+seasonal summon scroll or Blaze Rod(light)/Echo Shard(shadow). ModCompat Curios head or offhand without Curios, no normal armor-slot fallback in inspected path. Server refresh fey_flying220t when remaining<210. FeyFlyingEffect if mayfly false sets both mayfly/flying true and persistent feywild_granted_flight marker, sends abilities. Expiration warns and gives Slow Falling120t; removing tiara leaves short refresh lifetime (~11s). Survival cleanup clears both flags if marker true, even if another valid provider arrived afterward. **Already tracked #330**, [November21 direct flight ownership audit](../../qa/FLIGHT-PROVIDER-DIRECT-OWNERSHIP-2026-11-21.md) previous source finding now exact-binary corroborated. No duplicate issue or flight redesign; coexistence QA belongs to existing Progression/compatibility owner. Native late-game gate preserved. No recurring mana/durability cost.
## Downstream limits
Native research COMPLETE, no game test or new pack authorization. Winter Pixie and cracked log real generation under existing capture/replication policy review; do not assume blanket denial or unrestricted duplication. Bellsnickel enchanting, native quest reward/ID and boss cross-court mismatch, dismissal/network validation and state/gear/claims attribution, flight #330, effective biome/config/tags and addon catches separately. No quest changes, no dependency additions. 
## All 29 capability axes
| Axis | State | Concrete scope |
|---|---|---|
| gear | present | wings, guardian armor/spear, orb |
| movement | present | Ent riding, creative-style wing effect, Dust levitation |
| ore_processing | present | gem smelting to Dust, no general ore doubling |
| storage | present | Bellsnickel bag/book slots, Bee gear slots; dismiss drops |
| crafting | present | Fey Altar and book copying |
| logistics | no_capability_confirmed | dropped goods and portable companion, no assigned item routing |
| buffs | present | Ent blessings, flight, Dust |
| external_compatibility | present | Quest Giver, Patchouli, Curios, catchable data; not dedicated crow/Ars bridge |
| mob_farming | present | Spring breeding, Winter loot/damage, cracked-log spawns |
| ore_generation | no_capability_confirmed | natural ore worldgen only |
| permanent_power | present | persistent owned companions, wing item; no permanent ability tree |
| general_generation | present | flora, animal products, barters, books |
| direct_damage | present | guardians, Ent, boss, Winter |
| block_breaking | present | Autumn crop state reset, no broad mining capability |
| permanent_pets | present | seasonal Pixies, lesser Fey, Ents |
| temporary_pets | no_capability_confirmed | hostile Mab Vex not owner summon |
| defense | present | guardians, blessings, iron-sensitive Fey block |
| healing_recovery | present | cookies heal companions; not general player healing |
| debuff_control | present | Fey Trickery, levitation, slow falling, repulsion |
| energy | no_capability_confirmed | gem/ink ingredients not energy network |
| automation_control | present | follow/stay/ability toggles and ambient AI |
| agriculture | present | crop growth/harvest, breeding, flower/cow mutation |
| detection_information | present | Lexicon and native quest dialogue; no radar established |
| equipment_augmentation | present | Bee gear and wing crafting, book copying |
| world_manipulation | present | flowers/crops, seasonal trees, entity transformation |
| world_content | present | ores, seasonal trees, flowers, fey/Ent encounters |
| death_respawn | no_capability_confirmed | dismissal/resummon not death resurrection |
| trade_economy | present | autonomous gem barters incl books |
| progression_tiers | present | Pixie→lesser→Ent→sovereign→wing, parallel native stories |
## Quest-source map — 35 representative teaching slices
Not fixed quest count. Dependencies editorial, not artificial runtime locks. Use actual task/advancement or observable operation, acquisition proxy only. Native Quest Giver stories remain primary for court progression. No unnecessary pet death or hostile multiplayer, no reward bypass of pack gates.
| ID | Previous | Kind | Action / success | Reference |
|---|---|---|---|---|
| FY-01 | — | Core | Receive or craft Feywild Lexicon; discover Sprite/native quest dialogue. | Lexicon, Sprite |
| FY-02 | FY-01 | Core | Mine Fey Ore or obtain Dust through peaceful Fey interaction; convert dust/gem. | Fey Gems |
| FY-03 | FY-02 | Core | Craft Mandrake Root, grow and harvest it; produce Fey Ink and blank scroll. | Summoning Fey |
| FY-04 | FY-03 | Core | Build Titania or Mab Fey Altar and use five-input summon recipe. | Summoning Fey |
| FY-05 | FY-04 | Core | Summon Spring Pixie and craft Pixie Orb, learn follow/stay/ability controls. | Handling Fey |
| FY-06 | FY-05 | Optional | Observe Spring breeding on eligible animals, no food; check owner/chunk conditions. | Spring Pixie |
| FY-07 | FY-04 | Core | Summon Summer Pixie, demonstrate ordinary CropBlock growth. | Summer Pixie |
| FY-08 | FY-04 | Core | Summon Autumn Pixie, observe harvesting and collect dropped produce. | Autumn Pixie |
| FY-09 | FY-04 | Core | Summon Winter Pixie, observe animal loot and damage, avoid rare livestock. | Winter Pixie |
| FY-10 | FY-06, FY-07, FY-08, FY-09 | Optional | Build local seasonal farm with normal item collection; optional crow/Starbuncle integration. | Pixie abilities |
| FY-11 | FY-05 | Optional | Heal companion with cookie, test stay/follow and dismiss empty companion; resummon. | Handling Fey |
| FY-12 | FY-04 | Optional | Produce Spring court sapling and giant flower seed, grow valid seasonal trees/flowers. | Fey flora |
| FY-13 | FY-05 | Optional | Summon Mandragora, choose flower variant and observe growth. | Mandragora |
| FY-14 | FY-08 | Optional | Summon Shroomling; transform expendable cow, observe colored mushroom cow products. | Shroomling |
| FY-15 | FY-07 | Optional | Summon Bee Knight, use guardian mode and mount/knight/spear slots. | Bee Knight |
| FY-16 | FY-09 | Optional | Summon Bellsnickel; inspect inventory and one book-copy recipe, empty before dismissal. | Bellsnickel |
| FY-17 | FY-13 | Optional | Offer dropped Fey Gem, collect actual Mandragora barter output. | Trading |
| FY-18 | FY-14 | Optional | Offer gem to Shroomling, collect barter output. | Trading |
| FY-19 | FY-15 | Optional | Offer gem to Bee Knight, collect barter output. | Trading |
| FY-20 | FY-16 | Optional | Offer gem to Bellsnickel, collect enchantment book; no guaranteed selected enchant. | Trading |
| FY-21 | FY-12 | Optional | Discover cracked log/wild Ent; observe day/night behavior without needing to farm it. | Tree Ents |
| FY-22 | FY-13, FY-12 | Core | Craft Spring Ent scroll from actual ingredients; summon, ride and observe blessing. | Spring Ent |
| FY-23 | FY-15, FY-07 | Core | Grow Summer sapling and craft/summon Summer Ent. | Summer Ent |
| FY-24 | FY-14, FY-08 | Core | Grow Autumn sapling and craft/summon Autumn Ent. | Autumn Ent |
| FY-25 | FY-16, FY-09 | Core | Grow Winter sapling and craft/summon Winter Ent. | Winter Ent |
| FY-26 | FY-22 | Optional | Follow native Spring questline, obtain Oberon scroll, defeat boss. | Spring story, Oberon |
| FY-27 | FY-24 | Optional | Follow native Autumn questline, obtain Ashen scroll, defeat boss. | Autumn story, Ashen Lord |
| FY-28 | FY-23, FY-25 | Optional | Follow native Summer story, obtain Mab scroll from Winter ingredients and defeat Mab. | Summer story, Mab |
| FY-29 | FY-23, FY-25 | Optional | Follow native Winter story, obtain Titania scroll from Summer ingredients and defeat Titania. | Winter story, Titania |
| FY-30 | FY-26 | Optional | Use earned base Tiara plus Elytra/Nether Star/Dust and matching scroll to make Spring wing variant. | Pixie Wings |
| FY-31 | FY-27 | Optional | Craft Autumn wing variant after obtaining base Tiara and other late-game ingredients. | Pixie Wings |
| FY-32 | FY-28 | Optional | Craft Summer wing variant after native story encounter, demonstrate existing flight policy. | Pixie Wings |
| FY-33 | FY-29 | Optional | Craft Winter wing variant after native story encounter, demonstrate existing flight policy. | Pixie Wings |
| FY-34 | FY-30 | Optional | Compare light/shadow wing alternatives or decorative items; no need duplicate all six wings. | Pixie Wings |
| FY-35 | FY-10, FY-17 | Endpoint | Demonstrate seasonal homestead and chosen fey companion service, optional sovereign mastery. | Lexicon and native quests |
