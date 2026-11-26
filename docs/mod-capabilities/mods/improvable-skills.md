# Improvable Skills 3 — 21.0.2 native Atlas

**Status: quest-source-ready for native ordinary-player capability scope, 2026-11-25.** [#410](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/410). Exact Drive JAR ImprovableSkills-1.21-21.0.2.jar, 1,209,579 bytes, SHA1 b082c298dafdf74dbe96cdfb5422d262b3499643 matches [active pin](../../../mods/improvable-skills.pw.toml), CF252902/file5685129; SHA512 5a1190af80038cd2169871923a08b5cad6180754af3e970c0ba5f18a5b8f8f381ad308724cdebdd117ce7dd8b3ab33c1154a40a41d4fa45d62d268f14816a9bd. ZIP CRC pass 422 entries, 209 classes. NeoForge >=21.0.167, MC [1.21,1.22), HammerLib >=21.0.13; active pack HammerLib21.0.17. Compiled package org.zeith.improvableskills; all 27 SkillsIS and six AbilitiesIS constructors and handlers javap -p -c inspected; PlayerSkillBase, SkillCostConfig, PlayerSkillData, PlayerDataManager, skill/XP packets, ItemsIS, ImprovableSkills.addRecipes, ItemParchmentFragment, ConfigsIS, TreasuresIS, loot constructors, en_us.lang. No bundled recipe/advancement JSONs; dynamic recipe registration. Localization legacy treasure_finder/item_repair/pvp_protection are not additional registered skills. Creative Page excluded. No game execution, JAR redistribution or runtime changes.

## 1. First operation, progression and resource ledger

Craft Book of Skills (improvableskills:skills_book), shaped LBL / PGP / LBL, L leather tag, B ordinary book, P paper, G gold ingot tag: 4 leather, 2 books, 2 paper, 1 gold. Open book with use; inventory book button enabled by persistent hasCraftedSkillBook state. Skills, Abilities and XP Bank tabs; select an available skill, Upgrade, observe rank and vanilla XP reduction, toggle active/inactive. No required class or MXP tree; all skills independently advance 0 to cap. Hidden skills require Page with Skill (scroll_normal with specific stored skill component) from appropriate loot; first page unlocks, further pages upgrade one rank for no XP until cap. Page with Ability (scroll_ability with ability component) unlocks permanent binary ability, not a numeric rank. Native duplicate-scroll drops default enabled until cap; auto-use scrolls default false. Creative Page with Skills (scroll_creative) unlocks hidden skills, not a survival route. Exact native configs support disabling individual skills/abilities and changing XP cost expressions; no extra custom costs in checked pack config.

Raw vanilla XP is debited by PlayerSkillBase.onUpgrade through HammerLib XPUtil. Cost ceil(expression evaluated at TARGET rank r, not current rank), default (r+1)^p. Downgrade calls onUpgrade(old,new): refund formula evaluated at NEW lower rank, not original purchase price (lossy), then onDowngrade. Config expressions can change values. Skill stat data, scrolls, toggles, ability ownership and bank serialized in PlayerSkillData NBT. XP Bank uses BigInteger storageXp, no fixed storage cap found, transfers actual raw XP via integer packets, Store All / draw 1 or 10 levels via book UI. Bank is player state, not block/item inventory, no RF/FE, no sharing. Auto XP Bank starts disabled with threshold equal to level30 total XP; every 2 ticks moves max(1,floor(sqrt(abs(current−target)))) XP toward threshold, withdrawal bounded by stored balance. Retain enough unbanked XP for upgrades, Dragon Survival XP-level skills and enchanting. No MXP conversion. Native PlayerDataManager.playerClone returns early if isWasDeath; nondeath clones serialize/deserialize. Do not assert death preservation solely from that handler: HammerLib player-data lifecycle would need examination before making a death guarantee; bank not resurrection. PacketStoreXP and PacketLvlUpSkill validation appears limited (signed amount/scroll entitlement questions); report as separate integration review, no exploit procedure.

## 2. Complete native skill ledger

All IDs use improvableskills namespace and match registered localization; r=target rank, p=r/max for effect progress (not cost exponent). XP costs rounded upward per rank; cumulative amounts calculated offline from constructor formulas, no scrolls used. Sources under custom/skills/Skill*.class. Open = no skill page gate; otherwise loot table named. Odds are native constructor 1/N roll where applicable, subject to exclusive/eligibility conditions; not effective pack loot guarantee.

| Skill (ID) | Cap | Cost per rank | Total raw XP | Gate / actual effect |
|---|---:|---|---:|---|
| accelerated_furnace | 15 | (r+1)^2 | 1495 | open; player-centered ±3 cube (343 positions), lit AbstractFurnaceBlockEntity, chance 1/(16−r), adds 2 round(sqrt(r)) cook progress, max +8 on every eligible tick; fuel/cook completion handler applies; not every mod machine, localization radius5 misleading |
| alchemist | 15 | (r+1)^2 | 1495 | witch 1/10; ±3 cube brewing stand timer reduces by 2 floor(sqrt(2r)), max 10 extra ticks per eligible invocation, floor timer1 |
| atkdmg_melee | 15 | (r+1)^3 | 18495 | open; eligible player melee incoming damage D→D(1+p/2)+7p, max 1.5D+7; not restricted to bare hands |
| atkdmg_ranged | 15 | (r+1)^3 | 18495 | skeleton 1/40; ranged event D→D(1+p)+r/2, max 2D+7.5 |
| attack_speed | 25 | r^1.5 | 1324 | open; attackStrengthTicker += floor(sqrt(r)/2), max +2 extra ticker ticks; not attack speed attribute or guaranteed DPS |
| cutting | 25 | r^1.5 | 1324 | open; eligible axe harvest speed adds r/8 to multiplier, max 4.125× ordinary event speed |
| dexterous_arms | 15 | (r+1)^3/2 | 9251 | mineshaft 1/4; durability damage reduction random roll threshold 60p, integer roll1..100 strictly less; max 59% per attempted point, loop can shorten as damage decreases |
| digging | 25 | r^1.5 | 1324 | open; shovel harvest multiplier 1+r/24, max 2.04167× |
| enchanter | 20 | (r+1)^2 | 3310 | stronghold library 1/4, not End fortress as old lang; matching player's enchant menu item within inflated 9 AABB, nonzero requirement reduced floor(r/4), floor1, max −5 required levels |
| ender_manipulator | 5 | (r+1)^3 | 440 | enderman 1/20; ender pearl damage reduction and terminal healing chance; precise random healing branch minor reference precision |
| fast_swimmer | 25 | (r+1)^2 | 6200 | open; +1.75p swim speed attribute, max +1.75 absolute, not blanket movement or water breathing |
| generic_protection | 20 | r^2.75 | 22119 | dungeon 1/4; armor ADD_VALUE +r, max +20 armor |
| growth | 20 | .9r^3+32 | 40339 | jungle temple 1/4; every 80+3(20−r) ticks, radius 2+floor(r/4), vertical half radius, valid BonemealableBlock excluding blocklist; choose up to floor(floor(sqrt(r))/2) targets via random bound +1; max radius7, every80 ticks, 0–2 targets, generated bone meal, no inventory reagent |
| health | 20 | (r+1)^3 | 53360 | End city treasure 1/9; +r maximum HP, max +20 HP |
| huckster | 10 | 150(r+1)+(r+1)^3 | 14105 | librarian gift loot 1/3/exclusive; floor(.25p*baseCostA) reduction to special price difference, max25%, not all trade currencies or global Q Shop |
| ladder_king | 15 | (r+1)^2 | 1495 | open; faster ladder climb, progress fraction r/15, horizontal collision influences boost; no free wall climbing |
| leaper | 15 | (r+1)^2 | 1495 | open; vertical velocity ×(1+.75p), max 1.75×, not exactly +75% jump height |
| luck_of_the_sea | 15 | (r+1)^2 | 1495 | fishing 1/10; while live fishing hook, Luck attribute +2r, max+30; localization says lure but handler modifies Luck, not wait time |
| mining | 25 | 3sqrt(r^3) | 3949 | open; qualifying correct-tool harvest pickaxe tag multiplier 1+r/8, max4.125× |
| mob_repellent | 5 | (r+1)^4 | 2274 | village armorer chest 1/1; AABB expanded 5p blocks, pushes Monster entities away normalized vector ×.15 per eligible tick; not invisibility or targeting cancellation |
| obsidian_skin | 20 | (r+1)^2 | 3310 | Nether bridge 1/3; fire/lava D×(1−.5p), max50% reduction |
| pvp | 20 | (r+1)^2 | 3310 | open; incoming eligible player damage D×min(1,.75+(1−p)/4), max25% reduction |
| silent_foot | 10 | (r+1)^4/3 | 13329 | ancient city, ice box, Warden 1/1; lerp(p, listener radius,1), cancel vibration beyond resulting radius; max radius1, not general mob invisibility |
| soft_landing | 10 | (r+1)^2 | 505 | open; positive progress fall event scales distance, fallDistance, and damage multiplier by 1−clamp(p,.25,.5); rank10 additionally caps lethal FALL incoming damage at health−1, no universal revival |
| soul_speed | 3 | (r+1)^6+150 | 5339 | piglin barter 1/7/exclusive; virtual Soul Speed enchantment max(existing,min(r,3)), ordinary vanilla movement/boot damage, not additive enchant level |
| treasure_sands | 3 | (r+1)^7+200 | 19299 | desert pyramid 1/8; break sand tag in biome base temperature>=2, chance4r%, max12% to choose eligible treasure drop; TreasuresIS includes nuggets/coal/bones/rotten flesh/lapis/diamond/golden apples, loot-table equipment; not automatic miner |
| xp_plus | 10 | r^3+100(r+1) | 9525 | elder guardian 1/3; XP-producing block/mob drops floor(E+U*E*p), U uniform [0,1), max near2× not guaranteed; breeding/fishing bonus orb path, breeding 1+randomInt(r+1); not MXP |

Totals 27 skills, 258401 raw XP calculated to purchase all from zero, before free duplicate scrolls/price overrides. Dig speed handler starts multiplier at1 and sums eligible skill increments; requires correct harvest predicates, not granting tool tier, vein mining or extra drops. Multiple skills/other mods can compose via same event; no effective stack order guaranteed. Hidden pages: 16, open skills:11. See code for exact scroll variant and exclusivity; don't infer from legacy English hints. Cost formulas are native defaults, not a proposal to rebalance TMM.

## 3. All six permanent abilities and special recipes

ImprovableSkills.addRecipes registers six ParchmentFragmentBuilder ability recipes, conditional on ability enabled. Drop one Parchment Fragment plus one of each listed component close together, wait for native item-entity conversion; collect Page with Ability and use to unlock. JEI 'Drop together' category supports the unusual operation. Fragment can instead craft ordinary paper; avoid wasting scarce TMM fragments. No magic energy, ordinary material consumption. Native pagelets and menus are player-owned not portable machine items.

| Ability ID | Additional drop ingredients (one each) | Operation |
|---|---|---|
| anvil | ender pearl tag, anvil, emerald tag | Blacksmith opens portable anvil menu; ordinary XP/material rules |
| crafter | ender pearl tag, crafting table, iron ingot tag | portable crafting grid and recipe output |
| enchanting | ender pearl tag, enchanting table, bookshelf | portable enchant menu and Enchant Power UI; ordinary enchanting input/XP, not chosen enchant list by default |
| magnetism | ender pearl tag, eye of ender, iron ingot, chain | unlock enabled default4 blocks; GUI range packet clamp0–8, operates >1; item entity attraction not XP, ignores PreventRemoteMovement flag, ordinary pickup |
| auto_xp_bank | ender pearl tag, experience bottle, redstone | configure/enable auto balance, threshold described above |
| cowboy | ender pearl tag, glowstone dust, nether star, saddle | toggle then interact with living target; event deny/cancel, boss and prevent_cowboy_interaction tags, rides eligible target; not taming or controlling every mount |

Exact field registry metadata SkillsIS, AbilitiesIS, ItemsIS; native scrolls are component variants not 27 individual items. No dedicated placeable IS3 machines; player proximity accelerates external vanilla stations. No exact global enchanting power cap claimed beyond native menu implementation; TMM personal enchanting expansion separate.

## 4. TMM source crosswalk

[config/improvableskills.cfg](../../../config/improvableskills.cfg) sets Gameplay/Parchment Fragment/Do Generation=false. Native chest generation disabled, NOT skill-page drops globally disabled. [parchment_fishing.js](../../../kubejs/server_scripts/parchment_fishing.js) #433 instead inserts fragment into loaded fishing TREASURE pool, target .005 of treasure outcomes at zero Luck, not .5% of ALL casts; vanilla six equal entries gives 1/205=0.4878%. Existing entry weights/quality scaled together; fail closed on composite/missing/overflow/duplicate. Luck and native open-water conditions affect actual loaded odds. No guarantee about other TMM sources without loaded loot audit. Teach ordinary rare fishing treasure and keep fragments for abilities. Existing Tips & Tricks first-login book unlock #567/#586, not extra physical starting book; native craft still valid, verify actual starter function before quest authoring. No duplicate starter gifts. Personal enchant filter #328/#407/#471 explicitly uses native enchanting ability authorization; source separate from IS3 native enchant menu, no invented completed UI. Active integration path for gate not found at queried snapshot; no installed feature claim. Monk, Hunter, dragon and Core balance must account for +20 HP/armor, strong fist/ranged, speed, fall save, mining, XP and fishing Luck, alongside Apotheosis. No design decision to disable/reprice or force Path-specific exclusivity. Dragon level gates depend on unbanked vanilla levels, XP Bank automatic withdrawal/deposit may alter them. Strong Fist applies to armed attacks as well; not automatically restricted by Monk's natural attack filter. Native XP multiplier and Thrift do not directly modify MXP or every vendor. Claims, growth, magnetism, Cowboy, overlapping protection/XP events and player bank lifecycle are targeted integration topics, not native gameplay acceptance requirements.

## 5. All 29 axes

| Axis | Status | Mechanism |
|---|---|---|
| gear | limited | Book and pages enable permanent skills, no gear tier family |
| movement | present | Leaper, ladder, swim, virtual Soul Speed, Cowboy mount |
| ore_processing | limited | nearby furnace acceleration, no ore multiplication |
| storage | limited | personal XP bank only, no item/fluid warehouse |
| crafting | present | book, drop-together abilities, portable crafting/anvil/enchant |
| logistics | limited | Magnetism local item attraction, no managed network |
| buffs | present | permanent toggled stat changes, growth/furnace and protection |
| external_compatibility | present | HammerLib, NeoForge events, JEI/JER, vanilla station/entity/loot hooks; TMM separate |
| mob_farming | limited | combat/XP assistance, no spawner or virtual kill system |
| ore_generation | limited | probabilistic Treasure Sands diamond/metal nuggets; requires sand breaking, no ore block generator |
| permanent_power | core | 27 purchased ranks and six permanent abilities, scroll gates |
| general_generation | limited | free bone meal action and sand treasure, no arbitrary replication |
| direct_damage | present | melee/ranged event scaling, needs eligible attacks |
| block_breaking | present | pickaxe/axe/shovel speed, legal harvest only |
| permanent_pets | absent | Cowboy rides, not taming/summoning |
| temporary_pets | absent | no normal summon |
| defense | present | armor, fire/PvP/fall reduction and monster repel |
| healing_recovery | limited | Ender Manipulator terminal healing, increased max HP not direct sustained healing |
| debuff_control | limited | monster push and silent vibration attenuation |
| energy | present | raw player XP and bank BigInteger, no block/item/FE pool |
| automation_control | limited | auto bank, player-centered passive growth/furnace/brewing |
| agriculture | present | Fast Growth free bonemeal, XP breeding assistance |
| detection_information | limited | book/JEI progression and treasure eligibility, no radar |
| equipment_augmentation | present | portable enchant/anvil, Enchanter and virtual Soul Speed, durability protection |
| world_manipulation | limited | plant growth and local attraction/repulsion, no building machine |
| world_content | limited | native loot injections into existing structures/fishing/mobs, no new dimension or structure |
| death_respawn | limited | max Soft Landing lethal fall cap; death bank persistence unconfirmed, no revival/grave |
| trade_economy | present | Huckster merchant discount, piglin barter/librarian gates |
| progression_tiers | core | book unlock, open/hidden 27 independent rank caps, six material unlocks, no formal class/tier ladder |

## 6. Proposed teaching sequence (not implemented quests)

All detection proxies explicitly distinguish possession from operation. Native skills and abilities require personal state via verified API/adapter or observable instruction, not assumed advancements (none packaged). No new reward amounts. Avoid rewarding extra fragments that bypass pack economy without approval.

| ID | Teach actual player action | Evidence |
|---|---|---|
| IS01 | Existing Tips & Tricks unlock or craft Book of Skills from JEI, open GUI | inventory/hasCraftedSkillsBook; observable GUI |
| IS02 | Obtain ordinary XP, purchase first available skill, compare cost and rank | personal rank and XP |
| IS03 | Toggle skill, demonstrate before/after; explain lossy downgrade | state/observable |
| IS04 | Bank XP, draw some for enchanting, retain XP-level gates | bank state |
| IS05 | Mine legal ore/stone with pickaxe; buy Mining, Digging, Cutting as desired | skill ranks and speed |
| IS06 | Learn Strong Fist and Attack Speed; practice eligible ordinary attack | rank and operation |
| IS07 | Soft Landing, Leaper, Ladder King and Fast Swimmer in safe environment | ranks/observe movement |
| IS08 | Train PvP Protection optional, no required PvP | rank |
| IS09 | Skeleton and witch loot pages; unlock Sharp Arrow/Alchemist | page-specific state |
| IS10 | Fish for Luck of the Sea page; compare Luck while fishing | rank/effect |
| IS11 | Locate dungeon and End city; Dense Skin/Extra Health pages | unlocks |
| IS12 | Nether bridge and enderman pages; Obsidian Shield/Ender Manipulator | unlocks |
| IS13 | Jungle temple growth page, grow crops while present | observable growth |
| IS14 | Ancient city or Warden Silent Foot page; use safe vibration demonstration | unlock/observe |
| IS15 | Mineshaft Dexterous Arms page; compare durability | rank |
| IS16 | Stronghold library Enchanter; compare actual enchant requirement | unlock/observe |
| IS17 | Elder guardian XP Plus; collect native XP from eligible source | rank/XP |
| IS18 | Desert pyramid Treasure Sands page, break hot-biome sand | actual extra loot |
| IS19 | Piglin barter Soul Speed page, travel on soul sand | unlock/observe |
| IS20 | Librarian gift after raid Huckster; armorer chest Mob Repellent | unlocks, trade/repel |
| IS21 | Repeat pages free upgrade; normal purchase alternative; no creative page | rank change |
| IS22 | Fish in qualifying open water for rare TMM Parchment Fragment treasure | inventory only, not forced drop |
| IS23 | Drop fragment + JEI ingredients for Crafter, use portable grid | unlock/output |
| IS24 | Unlock Blacksmith and repair/combine in portable anvil | unlock/output |
| IS25 | Unlock Enchanting, open portable enchant UI and enchant item | unlock/actual result |
| IS26 | Unlock Magnetism, select range and collect item, toggle off | state/observe |
| IS27 | Unlock Auto XP Bank, set target level, observe transfer | bank state |
| IS28 | Optional late Cowboy nether star recipe and ride eligible entity | state/mount |
| IS29 | Optional mastery of preferred branch, understand vanilla XP vs MXP and stacking | actual ranks; no mandatory all 27 grind |

Native scope complete for significant player-facing abilities. Remaining exact ender healing proc, portable enchanting power internals, loot weight conditions and death lifecycle are acknowledged reference/integration limits, not fabricated. No effective in-game test or balance claim. See separate evaluation.
