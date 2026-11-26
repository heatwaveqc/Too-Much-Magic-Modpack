# Apotheosis family — native capability Atlas (1.21.1 NeoForge)

**Native research COMPLETE, not gameplay acceptance.** Three exact-release passes; independent progression branches Adventure/equipment, Attributes, Enchanting, Spawners. Owner requires exact numerical benchmark for Monk #721/#722 R2. Preserve all TMM policy, no skill value/gear/quest/runtime edits. Source snapshot 1b6d1eedc36c62c9bbe863e9b399b375e486a12a.

## Evidence
| Exact module | Packwiz pin SHA512 | ZIP entries / classes |
|---|---|---:|
| Apotheosis 8.9.0 | [pin](../../../mods/apotheosis.pw.toml) b3022bc31f4e9f99d298c36486e09cad308d4aa87e2aa65a341809aeca41f0067e744086768884959036a46685d4c0babc4274600884ad8962fe9d080d8ef3ee |2679 / 556|
| Apothic Attributes 2.11.0 | [pin](../../../mods/apothic-attributes.pw.toml) 568580e68016161151fc0254fb0da949b42cf285fa2bf7c3aec1cd320405c430483e9eb990aaddef537bfac4aa4beb21db82fbff9249131e05b15063b6839611 |359 / 124|
| Apothic Enchanting 1.6.4 | [pin](../../../mods/apothic-enchanting.pw.toml) b560ed61810030472c42fa39e6199b1db07c19baba8f78d98b44ab3e1d00a1bebff5f69942bd076fea9754f63940df920cfb19906073b77bcc459bb8d88575de |847 / 153|
| Apothic Spawners 1.4.0 | [pin](../../../mods/apothic-spawners.pw.toml) 2fdd49cdb3e6610846c3a746ab0730d6d49412eaf8c21e3d0b9d588d80c8cd1a1d0b7e19ade7fc86fd699ac02e270e5fa01fb5cf106d24a7740692ecc1ad8820 |128 / 33|
All supplied JARs SHA512/CRC rechecked. Native current data (exclude old_data):99 affixes,23 gems/81 bonus entries,5 rarities+shears override,96 affix loot entries,48 gear sets,31 invader definitions,4 elite definitions,27 tier augments,36 wanderer trade records,9 rogue spawner definitions,5 Gateways records,109 English Chronicle guide entries. Enchanting26 packaged stat definitions and19 native infusion recipes, Attributes37 brewing mixes, Spawners32 standalone modifier recipes and15 advancements. Apotheosis adds 26 rune modifiers and false-conditions all32 standalone modifier IDs. Numeric reference [pass1 complete individual rolls/gem matrix/caps](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/410#issuecomment-7173482378); [pass2 optimized stat witnesses/formulas](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/410#issuecomment-7173919013); [Monk R2 handoff](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/721#issuecomment-7173919905). Targeted javap of AttributeEvents, ALCombatRules, affix and gem bonus types/slot IDs, enchant level handlers, WorldTier, AdventureConfig, AugmentingMenu, ReforgingMenu, PurityUpgradeRecipe. Exact packaged data and guide, not upstream binary-source equivalence or effective loaded config.

## Adventure: World Tiers, loot and encounters
Haven initially available; first affix acquisition reveals it. Frontier: common-or-better affixed equipment head/chest/legs/feet/held item. Ascent: uncommon-or-better same five equipment requirements and kill Apothic Invader. Summit: rare-or-better plus Wither. Pinnacle: epic-or-better plus Ender Dragon. Criteria are actual equipped_item advancements and kill predicates; requirements are cumulative advancement evidence, not currently simultaneous full set asserted. Unlock then manually activate using Ctrl+T by default, inspect detailed info/weights/augments; different players can choose different unlocked tier, content usually closest player. **Monk armor lock/gearless access is a separate policy question, not automatic equivalence to innate stats.**
| Tier | Common/Uncommon/Rare/Epic/Mythic weights | Cracked/Chipped/Flawed/Normal/Flawless/Perfect weights | Player augment |
|---|---|---|---|
| Haven |600/360/40/0/0|550/410/40/0/0/0|Max Eterna -70|
| Frontier |290/600/100/10/0|230/540/180/50/0/0|XP×1.35, luck×1.2, Max Eterna -55|
| Ascent |100/300/500/100/0|20/250/560/160/10/0|XP×1.55, luck×1.3, Max Eterna -40|
| Summit |0/120/290/540/50|0/80/310/580/20/10|XP×1.75, luck×1.5, Max Eterna -25|
| Pinnacle |0/0/100/650/250|0/0/100/330/470/100|XP×2.25, luck×2, no Eterna penalty|
Weights baseline per relevant roll, not unconditional mob drop probabilities; luck quality varies, show actual UI. Pinnacle mob augment +16 armor/+20 toughness/+25 armor pierce/+20 protection pierce and15% physical/magic direct reduction; not player grants. Invaders replace eligible natural spawns, announcement/beam, no Haven invaders, base cooldown3600t configurable. Native dimension chances Frontier/Ascent/Summit/Pinnacle: Overworld and End .018/.02/.025/.03, Nether .025/.027/.03/.035, Twilight conditional .05/.053/.06/.063; dimension rules and cooldown mean not unconditional all-mob probability. Overworld sky or same vertical slice, End outer islands, Nether any, Twilight surface. One guaranteed named affix trophy matching rarity, stronger rolls; possible extra loot and gems. Elites quieter natural replacements; may drop affix items. Caged invaders underground trigger on approach, loot chests; brutal/swarm rogue spawners and Gateways distinct encounters. Wanderer native tier trades; TMM trader gear/economy policy separate (QA/APOTHEOSIS-TRADER-GEAR-2026-11-22).

## Equipment crafting and gem lifecycle
Affix equipment from loot, invaders, elites, wandering trader or reforging. Rarity materials Common Mysterious Scrap Metal, Uncommon Timeworn Fabric, Rare Luminous Crystal Shard, Epic Arcane Sands, Mythic Godforged Pearl. Salvaging Table breaks gear/gems down; anvil crushes loose gems into Gem Dust. 
| Rarity | Stat affix rules | Basic effect rules | Ability | Native sockets | Durability bonus |
|---|---|---|---|---|---|
| Common |1 +25% second|0|0|none|none|
| Uncommon |2|50% one|0|0–1|none|
| Rare |2|1 +35% second|0|0–2|10–25%|
| Epic |3|1 +25% second|1|1–3|25–55%|
| Mythic |4|2|1|1–3 at95%, 4 at5%|45–75% at99%, unbreakable at1%|
Rules subject to eligible category/pool/exclusivity, not guaranteed impossible affixes. Shears lower budget (1 stat,2 basic,1–2 sockets at Mythic). Sigil of Socketing cap2, not fifth socket. Reforging Table center item, rarity material, Sigils of Rebirth; eligible unaffixed gear works. Simple supports Common/Uncommon/Rare; advanced supports all. Base level/material/sigil costs 2/1/0,5/2/1,15/2/2,30/2/4,50/3/5. Three offers, higher position multiplies requirements/materials, actual XP consumption described in Chronicle (one/two/three levels' worth); inspect UI rather than treating listed level requirement as fixed raw XP cost. Reforge repeatedly. Augmenting Table (Nether Star, two Godforged Pearls, enchanting table): select single affix, upgrade +.25 internal normalized level capped1 (not 25% compounded), cost2 Sigils of Enhancement and total XP equivalent to225 levels. Reroll same type through eligible weighted alternative list, cost1 sigil and total XP equivalent175 levels, new randomized roll. Affixes without upgradeable levels excluded. These establish attainable native max endpoints without assuming free optimization.
Gems six purities Cracked→Chipped→Flawed→Normal→Flawless→Perfect; same gem ID AND purity pair required by UnsocketedGem.equals; one upgraded gem out, dust and ONE alternative rarity material:
| Step | Dust | Alternative material counts (choose one) |
|---|---:|---|
| Cracked→Chipped |1|3 common or1 uncommon|
| Chipped→Flawed |3|9 common or3 uncommon or1 rare|
| Flawed→Normal |5|27 common or9 uncommon or3 rare or1 epic|
| Normal→Flawless |7|27 uncommon or9 rare or3 epic or1 mythic|
| Flawless→Perfect |9|27 rare or9 epic or3 mythic|
Smithing Table or inventory right-click socket; unique gem restriction per item, not per player; withdrawal sigil returns all gems and gear. Gem Case holds32k/type/purity, six auxiliary material slots, name and equipment filters, internal upgrading; Fabergé case via infusion at E100/Q100/A45–50, capacity2b/type/purity. TMM Malum spirit-fire gem recipes, component-sensitive EMC and affixed/socketed gear no EMC guard separate, not universal duplication.

## Numerical benchmark for Monk R2
See [complete per-affix and per-gem maximum matrix](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/410#issuecomment-7173482378) and [optimized witnesses](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/410#issuecomment-7173919013). Pass2 evaluated37 attributes ×6 fixed equipment layouts (222 independent results), relevant affix subset and socket multiset enumeration, exclusive/unique constraints, Pareto pruning. Baseline netherite sword damage8/speed1.6, armor20/toughness12/KB.4, HP20, movement.1; no potions/Curios/external base gear/ench damage/tier augments. These are independent numerical objectives, not simultaneously achievable stats. ADD_VALUE then ADD_MULTIPLIED_BASE then product ADD_MULTIPLIED_TOTAL. Native IDs include slot group/socket index. AttributeFix21.1.3 installed, effective runtime cap unknown. 
| Stat | Conventional raw optimized | Higher alternate where applicable |
|---|---:|---|
| Attack damage |284.122335|(X+48)*1.725*2.941225 for verified base X, not Morgan cap|
| Attack speed |10.212|base*6.3825|
| HP |1103.59704|(X+80)*2.4*4.598321|
| Armor |1238.360620875|(X+96)*2.1*5.0835821875|
| Toughness |211.2642|(X+36)*2.1*2.095875|
| Knockback resistance |10.695|functional saturation separate|
| Movement attribute |16.8665095227|trident/shield23.6131133|
| Luck |150.65|before tier augment|
| Step height |16.79|blocks|
| Oxygen bonus |1.25|attribute|
| Block reach |5.175|breaker13.225|
| Entity reach |8.9125|sword/shield|
| Submerged mining |.2|breaker1|
| Swim speed |8.211|base1|
| Cooldown reduction |.30|native cap.95|
| Armor pierce |27.6|sword/offhand bow52.9|
| Armor shred |1.84|sword/offhand bow3.68 before cap2|
| Protection pierce |27.6|sword/offhand bow58.65 before cap34|
| Protection shred |.7475|sword/offhand bow1.4375 before cap.9|
| Crit chance |1.265|sword/offhand bow2.4725|
| Crit damage |8.303|sword/offhand bow17.457|
| Life steal |1.794|native cap10|
| Overheal |.2875|absorption cap half HP|
| Current HP damage |.2875|native cap1|
| Dodge |.2875|native cap1|
| Healing received |3.1625|native cap1000|
| Mining speed |7.7121875 breaker|native cap10|
| XP |3.91|breaker/offhand bow11.0055 before tier×2.25|
| Cold/fire damage |91.77 each|exclusive separate builds|
| Draw speed |1|bow2.64|
| Arrow velocity |1.725|bow7.452|
| Arrow damage |1.15|trident2.4955|
| Ghost health/projectile damage |0/1|no positive direct affix/gem in scope|
Attack witness Violent+8/Murderous+.55 base/4 Ballast+10 on sword, Blood Lord helmet×1.6 with HP×.35 drawback and3 Warlord×1.225 each, chest Molten Breach+.175 base with healing×.65. HP witness Blessed all4+8, Royalty helmet×1.15, chest4 Warlord+.35 base, legs4 Brawlers+12, boots Tyrannical×1.4 with lifesteal drawback, shield4 Brawlers×1.3. Mining breaker Destructive×1.85,3 Lightning+.5,Slipstream+.45 base,Royalty×1.15. Movement8 nonunique Lightning×1.7 leggings/boots, Windswept×1.45 both, Royalty×1.15, trident optional×1.4. Full other witnesses and event caps linked above. Native direct typed DR residual product: physical62.02359375%, magic58.2395%, projectile51%, explosion57.75%, fall40%, lightning64% separate builds, not sum. ALCombatRules protection residual1-min(.025*effectiveProt,.85), max85% at34; shred then pierce. Armor residual a/(a+armor), a=10 under20 incoming damage else10+(damage-20)/2; toughness reduces incoming shred/pierce by min(.02*toughness,.6). Damage tags/bypass and custom formulas matter. Overcrit each successful roll subtract1 chance, adds original damage*(multiplier-1), next multiplier×.85; no cap at100% chance. Lifesteal direct physical living attacker post damage bounded by target prior HP, then healing received; overheal absorption cap50% HP.
Enchant default max derives from min-power<200 formula/expanded level curve, forced cap and IMC separate. Native Sharpness IX and Protection VIII, prospective Earth+4 and Endersurge+2 on existing weapon gives XV; Protection 10/14/14/10=48 before forced cap and damage predicates. Fortune +10 existing (Earth4/Endersurge2/Prosperous4); Efficiency +6 (Inferno4/Endersurge2). Bow Prosperous SINGLE up to10 Looting even absent; global boosts don't create from zero; no universal +12. Berserker's Fury III+4 prospective VII if compatible. Full raw source matrix includes22 attribute definitions,23 packaged enchant definitions (other vanilla/modded registry not supplied). Do not infer final TMM caps, Morgan base, simultaneous universal max, or approve Monk ranks.

## Enchanting and potion progression
Eterna increases maximum offered level, shelf max limits contribution and player max Eterna; Quanta variance factor in [1-Q,1+Q], stability shifts to positive uniform; Arcana rarity weights and minimum two enchants at25%, three at75%; Clues reveal additional preview. Native guide progression:
- 1–30 fifteen vanilla bookshelves, candles Arcana, skulls Quanta. 
- 30–45 Hellshelf or Seashelf (+3 E, cap45; +3 Q or A respectively). Infuse at45/30/0 or45/15/10 minimum, then 60-cap infused and variants. Hellshelf of Sight, budding amethyst infusion and stability shelf. 
- Dormant Deepshelf infusion minimum60/40/40; Deepshelf advances70; echo/soul-touched variants cap75, sculk upgrades cap80 with Warden material. 
- Infused Breath minimum80/15/60, maxQ30; Endshelf cap90, Pearl variants, Draconic Endshelf +20 E cap100 via Dragon Head and outer End materials. Selected world tier caps player Eterna30/45/60/75/100. Table stat layout not a fixed exact recipe, check minimum and maximum stats.
| Representative shelf | E / cap | Q | A | clues |
|---|---:|---:|---:|---:|
| Basic bookshelf |2/30|0|0|0|
| Hellshelf |3/45|3|0|0|
| Seashelf |3/45|0|3|0|
| Infused Hellshelf |5/60|5|0|0|
| Infused Seashelf |5/60|0|5|0|
| Heart Seashelf |15/60|0|20|0|
| Echoing Deepshelf |5/75|0|15|0|
| Soul-touched Deepshelf |5/75|15|0|0|
| Echoing Sculkshelf |10/80|10|15|1|
| Soul-touched Sculkshelf |10/80|15|10|1|
| Endshelf |5/90|5|5|0|
| Pearl Endshelf |10/90|7.5|7.5|0|
| Draconic Endshelf |20/100|0|0|0|
| Melonshelf |-2/0|-10|0|0|
| Stoneshelf |-3/0|0|-7.5|0|
Sight shelves +1/+2 clues, Arcane Treasures shelf enables treasure (including curses), Aquatic Filtration up to6 single-enchant blacklist books; stable shelf prevents negative Quanta rolls. Infusion uses third offer, 3 lapis and E requirement XP level cost minimum3, one input, min/max constraints (not ordinary craft). Other recipes include budding amethyst, echo shard4, trident, XP bottles1/8/32, leads, extraction tomes, discs, library and gem case. Enchantment Library points and highest inserted level per enchant; level capacity16, Alexandria31 via infusion E100/Q45–50/A100, not create unlearned levels; excess voided. Scrapping destroys item and returns ceil(half) random enchants, Superior all but destroys, Extraction all preserves item; prismatic cobweb removes curses; typed tomes limit pool. TMM virtual shelves use native stat accumulator/actual maxEterna/32 positions and personal filters, no synthetic Eterna; existing recipe hooks separate.
Potion charms3 identical potions+blaze powder, toggle inventory/Curios application bursts consume durability. Native unbreakable infusion Pinnacle, E100/Q8.5–13.5/A32.5–37.5; TMM eligibility hook valid single noninstant effect, Talisman narrow repair exception separate. Native Attributes brewing mixes including Ancient Knowledge I×4/II×8/IV×16 XP configurable, Vitality +20% heal/lvl, Grievous -40%/lvl, Sundering +20% damage/lvl additive with Resistance, Bleeding periodic armor-bypass; not new MXP multipliers. Actual effect tags/handlers and loaded settings control. 

## Spawners (combined modules, not standalone recipe assumptions)
Apothic Spawners Silk Touch pickup with extra tool durability, spawn egg type change, Capturing (definition max3, default extended7) eligible entity egg chance, blacklisted mobs denied. Native tag warden, elder guardian,#c:bosses, current TMM tag appends #tmm:replication_blacklist; shared capture/replication/EMC policy remains separate. Do not teach unrestricted spawn egg/type conversion or bypass. Apotheosis packaged same-ID false recipes for all32 standalone item modifiers; use its runes, not sugar/clock/nether star vanilla item mods. Explosion breaks spawner then delayed chain cascade releases mob and chains, craft blank and infused runes; right-click consumes rune, quartz offhand reverses individual stat rune without consuming quartz, tier runes cannot invert.
| Tier rune | Min/Max delay ticks | Count | Nearby same-type cap | Activation range |
|---|---:|---:|---:|---:|
| Frontier |150/600|7|12|24|
| Ascent |100/400|10|18|32|
| Summit |50/200|13|24|40|
| Pinnacle |20/20|16|32|48|
Tier runes set these five stats directly (rarity material craft gates, not asserted personal tier lock). Actual spawn attempts bounded by conditions, space and nearby same-type count; reset after successful spawn; no guaranteed per-second rate. Eleven individual runes: spawn range+2 max32 (inverse-2 min1), initial HP -.16 min.2 (inverse+.16 max1), echoing +1 max3 extra loot copies (inverse-1 min0), redstone control requires signal, ignore light, ignore conditions (still needs space), ignore players (not chunk loading), silent, youthful where possible, burning, no AI. Native rune recipe gates/mob eligibility/MP spawner mod compatibility separate. 

## 29-axis navigation
| Axis | Finding |
|---|---|
| gear | Affix rarities, gear sets, invaders, sockets, enchants |
| movement | Speed, step, swim, gravity, flight/elytra affixes, potion flight |
| ore_processing | No separate ore refinery; Fortune/Boon, block transformations |
| storage | Gem Case, Fabergé, Enchantment Libraries |
| crafting | Salvaging/reforging/augmenting/cutting, infusion, rune crafting |
| logistics | Gem case filters/storage and ore magnet/item collection affixes; no full network |
| buffs | Stat affixes, gems, tier augments, potions and charms |
| external_compatibility | Four modules, Twilight conditional gems, TMM Economy/Progression and Curios |
| mob_farming | Capturing, spawner runes, echoing; shared blacklist |
| ore_generation | Boon/transforming effects, budding amethyst infusion; no unrestricted ore generator |
| permanent_power | World-tier unlocks; selected tier bonuses, not innate affix grant |
| general_generation | Affix/gem loot, XP effects, infusion outputs |
| direct_damage | Weapon affixes, enchants, crit, current HP, elemental effects |
| block_breaking | Radial/supermassive, mining, tool tiers, Chainsaw, Miner's Fervor |
| permanent_pets | No native independent permanent companion |
| temporary_pets | No independent temporary summon companion asserted |
| defense | Armor/toughness/protection replacement, typed DR, dodge |
| healing_recovery | Lifesteal/overheal, vitality, Life Mending, charm effects |
| debuff_control | Sundering, Grievous, Bleeding, freeze/burn, curse removal |
| energy | XP points/levels, consumable rarity materials; no mana or FE network |
| automation_control | Spawner redstone/conditions and echoing, local farm modifiers |
| agriculture | Nature's Blessing, Growth Serum, Tempting, spawner animal variants |
| detection_information | World tier UI, affix/gem tooltips, clues, loot beams |
| equipment_augmentation | All reforging, augmenting, socketing, cutting, enchant library |
| world_manipulation | Radial excavation, block transform, world tier selection |
| world_content | Invaders, elites, cages, rogue spawners and Gateways |
| death_respawn | No independent resurrection system established |
| trade_economy | Native tier wanderer trades, TMM EMC restrictions |
| progression_tiers | Five world tiers, five rarities, six purities, Eterna stages, four rune tiers |

## Representative instructional map
Native actions/advancements where verified, acquisition otherwise only possession proxy; no FTB IDs/rewards/config changes. Teach equipped-item gate as native; do not force new Monk armor policy. 
| ID | Prior lessons | Teach / observable success |
|---|---|---|
| AP-01 | — | Obtain affix item, Chronicle, activate Haven |
| AP-02 | AP-01 | Learn rarity, stat/basic/ability, numerical units and equipment slots |
| AP-03 | AP-01 | Frontier equipped-item criteria and manual activation |
| AP-04 | AP-03 | Locate and defeat Invader; identify guaranteed named loot |
| AP-05 | AP-04 | Ascent uncommon equipment plus invader criterion |
| AP-06 | AP-05 | Summit rare equipment and Wither |
| AP-07 | AP-06 | Pinnacle epic equipment and Dragon |
| AP-08 | AP-03 | Caged invader, elite and rogue spawner distinctions |
| AP-09 | AP-03 | Wandering trader tier offers and TMM policy |
| AP-10 | AP-02 | Crush gem, craft Salvaging Table, recover rarity materials |
| AP-11 | AP-10 | Simple Reforging Table eligible gear, Common–Rare |
| AP-12 | AP-11 | Advanced Reforging Table Epic/Mythic |
| AP-13 | AP-12 | Sigils of Enhancement and single-affix upgrade/reroll |
| AP-14 | AP-02 | Gem categories, purity, socket budget and unique restriction |
| AP-15 | AP-14 | Socket gem through smithing/inventory |
| AP-16 | AP-15 | Withdrawal sigil and Socketing cap |
| AP-17 | AP-10, AP-14 | Gem Cutting Table same gem/purity and alternative material |
| AP-18 | AP-17 | Progress to Perfect gem |
| AP-19 | AP-17 | Gem Case storage filters/internal cutting |
| AP-20 | AP-12, AP-18, AP-13 | Optimize one eligible equipment stat without claiming simultaneous maxima |
| AP-21 | AP-02 | Attribute units, additive vs multiplicative, caps |
| AP-22 | AP-21 | Critical chance above100%, crit damage and attack speed |
| AP-23 | AP-21 | Armor/protection/pierce/shred/toughness, residual DR |
| AP-24 | AP-21 | Lifesteal, overheal, healing_received and damage basis |
| AP-25 | AP-21 | Mining, movement, reach, utility attributes |
| AP-26 | AP-01 | Vanilla shelves, Eterna30, Quanta/Arcana/clues |
| AP-27 | AP-26, AP-03 | Hellshelf or Seashelf cap45 |
| AP-28 | AP-27 | Infusion stat requirements and infused shelves |
| AP-29 | AP-28, AP-05 | Eterna60, Sight and Stability |
| AP-30 | AP-29 | Dormant Deepshelf infusion and cap70 |
| AP-31 | AP-30, AP-06 | Deep Dark upgraded shelves cap75/80, Warden material and selected tier |
| AP-32 | AP-31 | Infused Breath and Endshelves cap90 |
| AP-33 | AP-32, AP-07 | Dragon Head and Draconic Endshelf Eterna100 |
| AP-34 | AP-29 | Treasure shelf, curse risk, filtration |
| AP-35 | AP-26 | Typed enchantment tomes and actual preview |
| AP-36 | AP-28 | Scrapping, Superior and Extraction tomes; preserve/destroy differences |
| AP-37 | AP-29 | Enchantment Library insert/extract learned levels |
| AP-38 | AP-33, AP-37 | Library of Alexandria infusion |
| AP-39 | AP-33, AP-19 | Fabergé Gem Case infusion |
| AP-40 | AP-34 | Prismatic Cobweb curse removal |
| AP-41 | AP-21 | Brew and observe representative attribute potions |
| AP-42 | AP-41 | Craft toggleable Potion Charm and manage durability |
| AP-43 | AP-42, AP-33 | Pinnacle unbreakable charm infusion and TMM eligibility |
| AP-44 | AP-01 | Silk Touch spawner pickup, inspect stats |
| AP-45 | AP-44 | Capturing permitted mob, egg type change, blacklist |
| AP-46 | AP-44, AP-10 | Spawner Chains explosion recovery and blank runes |
| AP-47 | AP-46, AP-03 | Frontier/Ascent upgrade runes core stats |
| AP-48 | AP-47, AP-06 | Summit and Pinnacle upgrade rune progression |
| AP-49 | AP-46 | Individual rune modifiers and quartz inversion |
| AP-50 | AP-48, AP-49 | Redstone, player/condition bounds, echoing and responsible farm |
| AP-51 | AP-20, AP-33 | Endgame gear benchmark exercise: actual modifiers/effect, not universal max |

## Boundaries and next
Native released behavior researched; no game boot, effective caps/loaded enchant registry, full TMM balance or cross-mod runtime assertion. AttributeFix effective limits, Morgan base damage, actual armorless World Tier progression policy and multi-mod spawner interaction remain separate R2/design/QA. No owner decision presumed. Major standalone queue now surveyed; proposed next focused addon Goety Cataclysm (exact pin check), then Occult Cooking / encounter-adjacent research by owner priority. Other active equestrian research #732 remains separate.
