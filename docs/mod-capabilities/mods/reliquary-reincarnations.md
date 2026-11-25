# Reliquary Reincarnations 2.0.85.1599 — native capability Atlas

**Native research COMPLETE, not gameplay acceptance.** Two exact-release passes; standalone artifact toolbox, not a linear magic school. #410. Preserve existing TMM economy, capture, flight and creative-item decisions; no new balance or quests. Existing ordinary recipe advancements are not proof of use. Teach useful families through independent branches.

## Evidence and boundaries
Owner-uploaded reliquary-1.21.1-2.0.85.1599(1).jar SHA512 75e4034ffff03d85ac4647fef6802e8921034de55baa650c32b7826f6eba4406b65fedcf4a8875d1ae8a3b850395a49fd0ad40587d60ffa1a1c62bb3e48c2e37 matches [pin](../../../mods/reliquary-reincarnations.pw.toml), Modrinth fQO83PId/NjITe5sZ. CRC pass, 1527 entries; metadata 2.0.85, NeoForge>=21.1.229. 227 recipe records:89 shaped,58 shapeless,32 infernal_tear_value,22 alkahestry_crafting,19 potion_effects,4 charging,1 drain,1 mob_charm,1 fragment_to_spawn_egg. 31 loot injections and39 block loot tables; no native guidebook/non-recipe advancement tree evidenced. [Pass1](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/410#issuecomment-7172751636), exact JSON/JEI locale and javap -p -c of item, cauldron/mortar, PotionHelper, PedestalItems/BlockEntity/wrappers, config nested classes, Magicbane, FragmentToSpawnEggRecipe and MobCharmRecipe; current TMM config and all19 custom seed recipes read. Not upstream binary equivalence or loaded recipe proof; no JAR committed. Native defaults below except explicitly TMM.

## Acquisition and artifact families
Use EMI/JEI exact recipes and native JEI descriptions; collect special mob drops (molten core, catalyzing gland, slime pearl, rib bone, chelicerae, nebulous heart, eye of storm, witch hat etc.) and ordinary materials. No invented rank gates.
- **Alkahestry Tome** reliquary:alkahestry_tome, book and eight rare ingredients (molten core, witch hat, eye of storm, catalyzing gland, slime pearl, chelicerae, wither skull, nebulous heart). Native cap1000 and charges redstone1/block9/glowstone dust1/block4. Sneak-use toggle absorbs inventory fuel; craft with seed, returns spent tome; native 22 duplication records, e.g. diamond2/64, Nether Star2/256. Native drain redstone exists but TMM disables it.
- **Altar of Light** obsidian/redstone lamp/nebulous heart/gland; 3 redstone inserted, direct sunlight/sky required; glowstone block on top after base20min Gaussian variance5min, not hard bounds. Night/roof stops progress; not generic solar FE. TMM fuel loop deliberately reviewed under economy, no new free duplication.
- **Lilypad of Fertility** lily pad+3 fertile essence (rib bone/gland/slime pearl/green dye). Radius4, potency1, base secondsBetweenGrowthTicks10. Actual scan 9^3 every50 ticks, Chebyshev selection at distance<=1/2/3/4 probability1/.75/.5/.25; only loaded positions, growable eligible blocks, excludes pads and grass. Overlap increases attempts; not guaranteed growth or TPS-neutral. 
- **Harvest Rod** till/plant/bonemeal modes, radius3; uses seed/fertilizer supply, supports pedestal wrapper. Fertile Potion and Aphrodite Potion native agriculture/breeding tools. **Shears of Winter** extra harvesting utility, exact target rules in native item. 
- **Void Tear** one chosen item type, native cap2,000,000,000; right-click chooses most common inventory item, toggle absorption/chest transfer. **Infernal Tear** whitelist32 value records turns consumed items into XP (diamond32/star192 native), not a universal resource converter. **Hero's Medallion** portable XP, returns one level at a time, pedestal repair with Mending within5,20t interval, up to5XP per step. **Fortune Coin** item/XP collection default5 passive/15 held.
- **Emperor's Chalice** infinite water, trades health for hunger; **Infernal Chalice** 500,000mB lava container, protective effect pays hunger. Distinguish survival tool from creative-class item policy #596; native capability not new permission. **Kraken Shell** drowning defense uses hunger; **Angelic Feather** fall protection and jump using hunger; **Phoenix Down** death prevention reverts to feather; **Angelheart Vial** consumable prevention; **Witherless Rose** wither protection; **Salamander Eye** protective utility; **Twilight Cloak** low-light invisibility and targeting suppression.
- **Rending Gale** flight thrust, push/pull/projectile manipulation, storm lightning, sneak-scroll modes. Native charge300, feather worth100, flight cast cost1, lightning100, target12 and push/pull10; no inherent fall immunity; pedestal flight range30, cost2 per second (per-charge config, no assumption about number of recipients). #330 flight ownership/policy separate; no automatic entitlement. **Ender Staff** pearl charge and Wraith Node destinations, teleportation; **Rod of Lyssa** pull items/entities and stealing; **Sojourner Staff** torch supply and remote lighting, **Lantern of Paranoia** automatic unsafe-location lighting.
- **Destruction Catalyst** consumes gunpowder, destroys eligible mundane blocks without drops; not mining yield. **Midas Touchstone** repairs gold with glowstone; **Glacial Staff** upgraded Ice Magus rod freezes water/lava temporarily; **Pyromancer Staff** fire charge; **Serpent Staff** pacification. **Magicbane** gold-compatible fragile16 durability, chance to inflict status ailments, compiled item attribute handler adds sum of enchantment levels as attack damage; enchantment changes #328 separate. **Mercy Cross** anti-undead. Handgun system has native selectable ammunition with different fantasy effects, charge/reload skill tied to vanilla XP level (guide max20); use native descriptions for specific recipes, no exhaustive ammo course. 
- **Mob Charm** protects against matched species even if hit; default80 durability, consumes on matching kill, repairs from relevant mob drop20; belt stores charms; pedestal matching protection range21. Native fragment chance .0166667+.0083333/Looting where not craftable, native charm recipe six matching fragments + leather/string; fragment egg two matching fragments + ordinary egg, conditional spawn_egg_enabled and charm_enabled; JSON chicken egg is display fallback, real recipe derives type component. TMM config explicit four blocked entities (elder guardian/dragon/warden/wither), not full effective capture policy. Economy ReliquaryCaptureMixin and ReliquaryFragmentEggMixin/CaptureInstancePolicy also enforce deny rules at native match and output. Do not teach a general capture bypass. 

## Alchemy
Apothecary Mortar two or three inputs (another essence may serve as ingredient); five pestle uses, shared passive effect becomes active, multiple effects possible. PotionHelper sums matching effect amplifiers, caps ordinary effects at4 (level V) during combination except saturation; duration sum/1.2, additional /1.1 for three contributors, cap36000t, instant effects duration1. Default JEI/creative maxEffectCount1, threeIngredients=false, differentDurations=false, redstoneAndGlowstone=false are display enumeration settings, not brewing limits. Native configurable potionMap. Apothecary Cauldron water, heat source/fire, nether wart, essence; default cook160t, redstone limit3, glowstone2, supports drinkable/splash/lingering using gunpowder/dragon breath; draw with vials. Native potion recipes also carry potion contents into tipped arrows/ammunition; no requirement to teach every combination. Cure/Pacification/Flight effect classes present; actual flight permission remains pack-owned. No universal potion XP or mana charge.

## Pedestal automation
Native active Pedestal uses item/action wrappers and toggled switch, adjacent redstone, remote dust/block pedestal; passive pedestal display only. Default range/cooldown settings: melee5/5t, bucket4/40t, shears4/10t, remote redstone10, fishing success80%, restart2s. Melee ignores bosses, tamed and child animals (native guide); fishing doesn't collect items/XP itself; active Fortune Coin collects to adjacent containers, inactive suppresses pickup; Medallion Mending repair; buckets milk cows/collect fluids to neighboring containers; shears blocks/entities; Harvest Rod farm wrapper; Rending Gale area flight with charge. Native I/O uses item/fluids and local pedestals, not general network or FE. Actual claims, chunk and ownership interactions downstream; no unlimited automation guarantee.

## Current TMM Alkahestry source overrides
Read all22 native seed disabling records plus drain: same-ID neoforge:false. Current config cap9999; charge redstone8/block72/glowstone dust48/block192. Custom seed files use strict default item components, retain seed-dependent crafting, returned Tome charge debit. Reference [Batch4 handoff](../../ALKAHESTRY-BOTANIA-BATCH4-HANDOFF.md); intended8 EMC per charge, approximately25% net economic gain when values match; do not assert live EMC or loaded conditions. All19 source recipes listed below; modded seeds conditional item_exists. Native diamond recipe disabled but TMM custom diamond exists. No Nether Star, Ancient Debris, Replica or Draconium or fuel recursion whitelist. Source readback at integration baseline c0bd9eb2ffe20d3a0e8356d30974f40e4b827ac4.
| Seed ID | Charge | Total output |
|---|---:|---:|
| betterend:thallasium_raw |1728|6|
| embers:raw_lead |108|6|
| eternal_starlight:raw_aethersent |4914|2|
| eternal_starlight:raw_deepsilver |108|6|
| malum:natural_quartz |128|6|
| minecraft:amethyst_shard |16|6|
| minecraft:coal |64|6|
| minecraft:diamond |820|2|
| minecraft:emerald |1639|2|
| minecraft:gold_nugget |8|6|
| minecraft:lapis_lazuli |432|6|
| minecraft:quartz |128|6|
| minecraft:raw_copper |27|6|
| minecraft:raw_gold |432|6|
| minecraft:raw_iron |54|6|
| neovitae:raw_demonite |3456|2|
| neovitae:sulfur |9|11|
| occultism:raw_iesnium |1728|6|
| occultism:raw_silver |108|6|
Do not revive disabled native recipes, recommend unrestricted duplication, or reinterpret exact charge values. Existing economy QA on charged components, automation, effective item_exists conditions and actual EMC remains separate. 

## 29-axis navigation
| Axis | Finding |
|---|---|
| gear | Independent artifacts and consumables |
| movement | Rending Gale thrust/area flight, Ender Staff, Angelic Feather |
| ore_processing | No dedicated ore refiner; seeded raw resource multiplication is generation |
| storage | Void Tear, Chalices, Medallion and item charges |
| crafting | Mob drops, custom alchemy, seed recipes, charms |
| logistics | Fortune Coin and Void Tear transfer, pedestal adjacent inventories/fluid |
| buffs | Potions, protective artifacts, area flight and pacification |
| external_compatibility | Curios/Accessories, JEI, item/fluid handler, TMM capture/economy hooks |
| mob_farming | Weapon, fishing, shear/milk pedestals; fragment/egg conditional |
| ore_generation | Seeded Alkahestry raw ores, exact TMM whitelist |
| permanent_power | No innate persistent skill ladder evidenced |
| general_generation | Tome duplication, Altar of Light, infinite water survival chalice native |
| direct_damage | Magicbane, Mercy Cross, staff and other combat artifacts |
| block_breaking | Destruction Catalyst, harvesting tools |
| permanent_pets | No native persistent owned familiar evidenced |
| temporary_pets | No independent temporary companion evidenced |
| defense | Witherless Rose, cloak, shells/chalices/feather, charms |
| healing_recovery | Custom potions, hunger exchanges, XP and gear repair |
| debuff_control | Pacification, cloak, magicbane effects, gale push/pull |
| energy | Tome charge, item-specific fuel, hunger/health, XP, no shared mana/FE |
| automation_control | Functional Pedestals, redstone, automatic lighting |
| agriculture | Lilypad, Harvest Rod, fertilizer/breeding, shear/milk |
| detection_information | JEI tool descriptions, Lantern unsafe-light detection |
| equipment_augmentation | Midas repair, XP Mending, enchantment damage scaling, charm belt |
| world_manipulation | Water/lava handling, freezing, light, block destruction |
| world_content | Special mob loot injection, no native dimension/biome evidenced |
| death_respawn | Phoenix Down, Angelheart Vial death prevention |
| trade_economy | No dedicated merchant system; TMM EMC-balanced seeded resource system |
| progression_tiers | Independent recipe branches; no formal tiers |

## Representative instructional map
Independent practical branches; dependencies pedagogical, not artificial rank gates. Use possession checks only for crafting/acquisition; manual observable success for actions without native criteria. No FTB IDs/rewards authored; TMM policy restrictions prevail.
| ID | Prior lessons | Teach / evidence |
|---|---|---|
| RE-01 | — | Collect special mob ingredients; use JEI/EMI descriptions |
| RE-02 | RE-01 | Make Fertile Essence and Lilypad; observe crop growth |
| RE-03 | RE-01 | Harvest Rod supply and modes |
| RE-04 | RE-01 | Fertile/Aphrodite Potion or farm tool demonstration |
| RE-05 | RE-01 | Emperor Chalice water and hunger exchange (policy permitting) |
| RE-06 | RE-01 | Infernal Chalice lava storage and defense cost |
| RE-07 | RE-01 | Fortune Coin passive and held pickup |
| RE-08 | RE-01 | Void Tear type selection, capacity, transfer |
| RE-09 | RE-01 | Infernal Tear whitelist and XP conversion |
| RE-10 | RE-09 | Hero Medallion store/return XP |
| RE-11 | RE-01 | Witherless Rose and protective charms |
| RE-12 | RE-01 | Angelic Feather/Kraken Shell hunger-based protection |
| RE-13 | RE-12 | Phoenix Down or Angelheart Vial death prevention; safe demonstration or possession |
| RE-14 | RE-01 | Twilight Cloak activation/light conditions |
| RE-15 | RE-01 | Mob Charm fragments, matching charm and belt; pack eligibility |
| RE-16 | RE-15 | Explain permitted native fragment→egg recipe and TMM blacklist; no bypass |
| RE-17 | RE-01 | Sojourner Staff and Lantern automatic illumination |
| RE-18 | RE-01 | Ender Staff charge and Wraith Node movement |
| RE-19 | RE-01 | Rending Gale mode/fuel; approved flight policy only |
| RE-20 | RE-01 | Rod of Lyssa manipulation and restrictions |
| RE-21 | RE-01 | Ice Magus/Glacial, Pyromancer and Serpent Staff representative effect |
| RE-22 | RE-01 | Magicbane enchantment scaling and Midas gold repair |
| RE-23 | RE-01 | Mercy Cross and other combat utility; optional handgun overview |
| RE-24 | RE-01 | Apothecary Mortar, shared effects and essence |
| RE-25 | RE-24 | Cauldron heat/water/nether wart; draw potion |
| RE-26 | RE-25 | Multi-effect brewing and duration/potency modifiers |
| RE-27 | RE-25 | Splash/lingering application and native tipped-arrow branch |
| RE-28 | RE-01 | Active versus display Pedestal; switch and remote redstone |
| RE-29 | RE-28, RE-07 | Fortune Coin collection to adjacent inventory; fishing/melee demo |
| RE-30 | RE-28, RE-03 | Bucket/shears/Harvest Rod automated work |
| RE-31 | RE-28, RE-10 | Medallion pedestal and Mending repair |
| RE-32 | RE-28, RE-19 | Rending Gale pedestal capability; policy-approved use only |
| RE-33 | RE-01 | Altar of Light redstone and daylight |
| RE-34 | RE-01 | Craft Tome, charge with effective fuels |
| RE-35 | RE-34 | Duplicate allowed ordinary mineral seed; measure cost and spent Tome; explain prohibited seeds |

## Boundaries
Exact individual staff effect coefficients, uncommon item modes, effective Curios slot configuration and live cross-mod policy are not asserted beyond sourced behavior; use exact native recipe/JEI pages when authoring. No independent native gameplay QA prerequisite. Pack-specific policy/EMC/recipe/capture checks are separate. Next ProjectE 1.21.1-PE1.1.0 and Project Expansion integration context.
