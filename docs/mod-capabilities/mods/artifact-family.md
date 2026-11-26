# Relics / Artifacts / Reliquified Artifacts — combined Atlas research checkpoint

2026-11-25; tracker [#410](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/410). **Research in progress, not a completed exact three-JAR survey.** Extends [preliminary evaluation](artifact-family-evaluation.md). Scope is one installed ecosystem, not duplicate credits for vanilla Artifacts effects and their replacements. No gameplay, balance, config, loot, recipe or quest changes.

## Provenance

- Artifacts 13.2.5 exact Drive JAR, 1,086,922 bytes, SHA512 9079bc8a22c237881b22b38027bbab2a8a2e685a1f49c1e1e0e5796f3e0a5509a130d73753c7b2980f12e4f00845f8e8c967ee969f1eadcd52139c2780afe09d matches [pin](../../../mods/artifacts.pw.toml). CRC clean, 1058 entries, 334 classes, 108 loot-table JSON paths, 3 recipe paths (including overlays where present).
- Reliquified Artifacts 1.0.9 exact Drive JAR, 699259 bytes, SHA512 8797ce1a64d483f74fccc380fbaa64821d04505e930b199c1300bbb6c1acfaa40ee2268b926802f3592915320ff500dd509a00308bfd7b69dd0f8c9e8f3172fb matches [pin](../../../mods/reliquified-artifacts.pw.toml). CRC clean, 340 entries,146 classes,12 loot tables,1 recipe. Exact classes, localization, Curios data and recipes enumerated. Prior version-aligned source [37a8e757](https://github.com/Octo-Studios/reliquified-artifacts/tree/37a8e757b809b8d49aa554037cd94537ceb730fd), including registration mixin, retained.
- Relics active [pin](../../../mods/relics.pw.toml) 0.12.9 SHA1 6b17e3a3292bf678415b4763cf830df1d01ebc33. [#725](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/725) independently verified earlier owner-uploaded 5007686-byte file and keybind fix. Current Drive folder contains OLD 0.12.5 (4476316 bytes SHA1 17da9f1bf39145486579241a74786360d385c6f1, CRC clean,1553 entries,637 classes). It is not the installed pin. Direct exact CDN download DNS failed; prior upload unavailable from current conversation/Library search. **Do not claim local 0.12.9 binary inspection.** Version bump immutable source [25a864208](https://github.com/Octo-Studios/relics/tree/25a86420867481870470ed1967802948b4aade85) reopened RelicsItems, RingOfTheSevenDeadlySinsItem, HuntingBeltItem, KineticBeltItem, AbilityData, AbilityStatData, LevelingData, RelicData, LevelingTemplate, AbilityTemplate, additive/multiplicative-base models. Old binary ring inspected using javap -p -c and language compared with newer source. Remaining per-item implementation and stat/loot pass remains due.

## 1. Native progression, quality and slots

Relics 0.12.9 source, not old 0.8/0.11:
- Items retain abilities, stats/quality, progression, research and option data. Obtain naturally generated item, use inventory inspection/research UI, unlock ability and solve constellation by joining stars based on icon/required degree. Use named ability XP source to level item, earn points and spend points on unlocked researched stat abilities; activate where relevant and use specific target filters/cooldowns. Item XP is not vanilla XP or MXP. Source AbilityData.isUnlocked requires enabled, adequate level, lock and completed research; canPlayerUse additionally checks relevant activation predicates.
- Template defaults: 10 ability levels per rank, one point/upgrade, required level/rank0, rank modifier .25, max rank5, additive level XP initial100 and step100, unless overridden. Actual relic level cap per current rank is sum(initialMaxLevel * requiredPoints) across ability templates, not global universal 10. Level XP cumulative default sum of 100+100i for i=0..L−1. Spending upgrades consumes (current ability level+1)*50 raw vanilla XP plus configured relic points. Reroll costs150 raw XP, reset costs ability level*250 and refunds invested points. Rankup at full relic level and below max rank: rank+1, resets level, points and ability levels to0. Read source for rank modifiers and item-specific costs. No blanket claim that every ability ranks up to same total.
- Initial stat quality integer0..10, uniformly initialized by random nextInt(11), interpolate first and second initial endpoints, including descending advantageous cooldowns. Display quality is descriptive, not independent innate buff. Perfect quality is second endpoint. AbilityStatData scales from rolled value using modifier computed against second endpoint and target value. Calculate max target points by starting current template cap and, for each remaining rank to maxRank, add ceil(cap*maxLevelRankModifier). Additive model base+(target−bestInitial)*p/N; multiplicative-base base*(1+((target/bestInitial)−1)*p/N) with zero checks; clamp to threshold and round5. Other scaling models possible. Targets do not equal simultaneous realized loadout values; separate handler caps, rounding, slots and conditions. Rank modifiers frequently 1/3/5. Need comprehensive per-item template ledger.
- Exact Artifacts base Curios data head/necklace/belt/hands/feet; base hands2 feet1, other size unspecified. RA exact data adds player bracelet slot, moves onion_ring and withered_bracelet to bracelet, adds artifacts:slot/charm to curios:charm, removes it from belt, removes artifact all tag from curios:all. Old Relics 0.12.5 has back1/belt1/charm1/curio1/feet2/hands2/head1/necklace1/ring1; active 0.12.9 actual slot data not reverified. Multiple mods' data merge, not blindly additive. Do not claim a universal exact final slot count. RA ModItemsMixin replaces existing artifact registrations (not parallel copies of old and new powers), both drinking hats share a class, special hand/food redirects. Exact active item keys are artifacts namespace, not reliquified_artifacts except support item Mimi-Dust. 
- Hunting Belt and Kinetic Belt each use RelicSlotModifier.modifier("charm", round(amount)); slots ability 5 initial cap,2 points/level, rank multiplier .1, start1–2 additional charms at quality extremes, target12 at terminal perfect progression. Source explicitly supports 12 additional charm slots per belt as target, not ring slots or guaranteed simultaneous 24. Other belt abilities and existing slot occupancy matter. Shared Curio access available to all Paths under their ordinary rules; do not include as additional Monk compensation (#721).

## 2. Ring of the Seven Deadly Sins — native exceptional power, pack banned

Do not confuse Relics ring with Enigmatic Legacy+'s Ring of Seven Curses (opt-in Hard Mode). Actual active version-aligned [ring source](https://github.com/Octo-Studios/relics/blob/25a86420867481870470ed1967802948b4aade85/src/main/java/it/hurts/sskirillss/relics/items/relics/ring/RingOfTheSevenDeadlySinsItem.java) and 0.12.5 bytecode agree on major behavior. Ring defines seven abilities with initialMaxLevel10 each, default cost1 point per upgrade, leveling initial100/step200 and maxRank0 (no prestige). Perfect terminal target values in source, not rolled initial ranges or loaded config proof:
| Ability | Native effect | Initial low→high quality | Perfect terminal target |
|---|---|---|---|
| Pride | outgoing damage per vertical block above target; incoming penalty below | .025→.05 | .1 (10% per block) |
| Envy | outgoing damage per 5HP relative advantage of opponent, incoming vulnerability for inverse | outgoing .005→.015; incoming .75→.5 | .025 outgoing, .25 incoming |
| Wrath | damage window resets on attack; early bonus to midpoint neutral, later penalty | window1→2s; early .1→.25; late1→.75 |5s, early1, late.25 |
| Sloth | stationary immortality, moving vulnerability | time10→7.5s; speed1→.75 | .9975s; speed .24975 |
| Greed | Luck and loot yield bonus with chance to destroy loot | luck1→3; looting1→3; nullify.1→.075 | +10 Luck/+10 loot, chance.00998 (~1%) |
| Gluttony | eligible attribute multipliers around 10 hunger, hunger-dependent downside | positive .0025→.005; negative .1→.05 | .1 positive/.01 negative per hunger |
| Lust | extra offspring with expiration unless bred again | amount1→3, time360→420s |13 additional offspring,720s |

Gluttony handler foodLevel center10, positive=max(0,food−10)*early; negative=max(0,10−food)*late; clamps net −.9 to1; uses ADD_MULTIPLIED_TOTAL per eligible registered attribute, skips config blacklist. At full ordinary hunger and perfect terminal stat gives **+100% total attribute modifier per ring**, not unbounded automatic double of every final damage/ability or all statistics of other relics. Engine clamps, explicit blacklist, other handlers and slots still apply. Sloth source uses position/rotation checks, not unconditional invulnerability while actively moving/aiming; thresholds/cancel event exceptions require final handler pass. Pride/Envy/Wrath event interactions and Greed loot probability not single combined guaranteed maximum. Native ring loot Nether/Nether-like, TMM intentionally excludes it. 
[config/relics.yaml](../../../config/relics.yaml) enabledExtendedConfigs:true and [ring override](../../../config/relics/relics/ring_of_the_seven_deadly_sins.yaml) lootData.entries:[]; [ban_seven_sins_ring.js](../../../kubejs/server_scripts/ban_seven_sins_ring.js) removes ring tag on item tag event. These are source verified, **not registry removal, retroactive purge or proof all Curios predicates exclude admin-spawned/already equipped items**. Prior config comment refers to old 0.12.5; active pin upgraded; effective loot loading and equip gate separate runtime. Do not teach ring acquisition as obtainable survival content, grant ring, or silently change ban. This family does not require ring to retain seven independent domains.

## 3. Relics 0.12.9 registry-based item inventory

RelicsItems.java at version bump source actually registers 20 relics +5 support items; old 0.12.5 classes include only 18 named relic classes, Ghostly Mantle and Shield of Retaliation are current source additions not old binary evidence. No deprecated Magma Walker/Rage Glove etc from commented registry credited. Native ability functions summarized from current source preliminary evaluation and old binary language; exact current templates and effects for remaining items still due. 
| Native ID | Practical ability/role |
|---|---|
| leafy_mantle | leaf traversal/fall safety and foliage healing; leaf-consuming death interception |
| springy_boot | crouch-jump propulsion and repeated bounce/fall safety |
| kinetic_belt | charm expansion and momentum/gliding |
| reflective_necklace | damage-storing orbs then release on attack |
| jellyfish_necklace | in liquid/rain increased HP/regen, electric discharge |
| midnight_mantle | darkness invisibility, constellations and falling stars |
| roller_skate | increasing sprint acceleration/slipperiness |
| chorus_staff | recharge-limited directional blink (source initial range10–15, charges3–5, recharge15–30s; rank1 flicker/3 safe fall/5 ascent) |
| clot_of_time | path rewind; initial3–5s, rank1 invulnerability/3 oblivion/5 health rewind; no world/inventory rollback |
| piglin_mask | neutrality, multi-result barter, gold teeth from kills |
| chef_hat | probabilistic meatball drops |
| cut_glass_boot | multiliquid bucket and traversal on stored liquids; select liquid via crouch-scroll |
| rider_flute | store/release tamed rideable mounts; not creates or tames creature |
| ring_of_the_seven_deadly_sins | seven abilities above, banned in TMM |
| sphere_of_self_sacrifice | old language HP cost33% max, restorative double amount over10s, stacking cap; current registered source, handler confirmation due |
| hunting_belt | charm expansion and pet damage/bone-revival branch |
| experience_disperser | fraction of earned relic XP shared to equipped relics; exact event recursion guard follow-up |
| glitchy_mantle | damage miss, temporary decoy/stun and surface tear |
| ghostly_mantle | current source actual death cancel, temporary flight/immortality, initial cooldown100–125s and duration1–3s |
| shield_of_retaliation | current registered shield/parry/retaliatory combat, exact abilities follow-up |
Support: relic_experience_bottle, golden_tooth, pet_bone, raw_meatball, cooked_meatball. Old exact five recipes: three cooking types (campfire600t/smelt200t/smoke100t, .35XP); 3 teeth→gold ingot,1 tooth→gold nugget. Current version-aligned source recipe census also five; exact 0.12.9 resource parity pending.

## 4. Artifacts + Reliquified Artifacts exact binary item/ability census

English Artifacts names include mimic_spawn_egg (not equippable relic). Remaining 43 functional artifact item keys are represented in RA exact classes/localization; don't double count baseline and replacement. All names below prefix artifacts:. Descriptions summarize packaged RA ability language, distinguish actual earlier selected handler evidence from tooltip-only claims; current exact RA template/handler numerical audit remains due.
| Item | Primary native RA behavior |
|---|---|
| anglers_hat | catch multiplication |
| antidote_vessel | negative effect duration reduction |
| aqua_dashers | sprint over water, acceleration penalty recovery |
| bunny_hoppers | sustained higher jump |
| charm_of_shrinking | size control |
| charm_of_sinking | underwater bottom air recovery |
| chorus_totem | lethal damage cap and safe-spot teleport attempt/cooldown |
| cloud_in_a_bottle | extra aerial jumps |
| cowboy_hat | mounted creature stat bonuses |
| cross_necklace | increased post-hit invulnerability |
| crystal_heart | extra HP reserve, depleted reserve restores after cooldown |
| digging_claws | faster mining |
| eternal_steak | regenerating food charges |
| everlasting_beef | regenerating food charges |
| feral_claws | attack speed stacks on timed successive attacks |
| fire_gauntlet | ignite targets |
| flame_pendant | retaliatory fire chance |
| flippers | swimming speed |
| golden_hook | kill XP bonus |
| helium_flamingo | bounded hover, speed based on swimming |
| kitty_slippers | creeper/phantom avoidance, chance lethal save at1HP |
| lucky_scarf | conditional added Fortune levels |
| night_vision_goggles | vision |
| novelty_drinking_hat | faster drinking |
| obsidian_skull | limited lava protection buffer and recharge |
| onion_ring | saturation-scaled mining speed |
| panic_necklace | speed per targeting mob, bounded |
| pickaxe_heater | chance auto-smelt mined ore |
| plastic_drinking_hat | faster drinking |
| pocket_piston | attack and interaction reach |
| power_glove | periodic amplified attack |
| rooted_boots | consumes grass to restore food/saturation |
| running_shoes | gradual sprint speed |
| scarf_of_invisibility | idle Vanishing, interaction interrupts/cooldown |
| shock_pendant | retaliation spark chains |
| snorkel | underwater breath within depth from air |
| snowshoes | snow travel speed |
| steadfast_spikes | knockback/slippery surface mitigation, wall slide |
| strider_shoes | sneak over lava |
| superstitious_hat | conditional added Looting levels |
| thorn_pendant | damage reflection |
| umbrella | slowed fall held in hand and shield hit limit/cooldown |
| universal_attractor | local polarity item attraction/repel |
| vampiric_glove | lifesteal |
| villager_hat | Iron Golem defense, merchant discounts |
| warp_drive | reusable ender pearl subject to cooldown |
| whoopee_cushion | crouch-triggered push burst |
| withered_bracelet | wither on attacks |

Correction to count: 48 rows including both distinct food and both hat items; exact localization/registry redirect count reconciliation due (do not infer total from 43 classes because shared class and nonwearable item implementations). Registry keys not individual abilities. 
Exact RA recipe: Mimi-Dust uses 2 glowstone dust+2 redstone+echo shard; no other packaged recipe path. Exact Artifacts JAR has 108 loot-table JSON paths incl. native integration/overlay data; effective loot distribution and Mimi-Dust handler need follow-up. Current source [Angler valuables](../../../kubejs/data/reliquified_artifacts/tags/item/anglers_hat_valuables.json) includes #c:raw_materials optional, amethyst shard, echo shard, prismarine crystals, nautilus shell, heart of sea, optional Dragon Survival elder dragon dust, Draconic Evolution draconium dust and TMM eldritch logic core. This is a notable progression gate interaction (not a guaranteed fishing drop). Check effective rank-specific ability/loot conditions in final pass.

Previously inspected actual source handlers: Chorus Totem LivingDamageEvent.Pre caps eligible fatal damage at health+absorption−1 and attempts safe teleport, sets cooldown, initial75–100s; Kitty Slippers lethal save chance initially2.5–10% and evasion10–25%; Everlasting food regenerates durability initial1–3/25–30s, rank1 restoration/3 preservation/5 quick meal, steak8/.8 beef3/.3; Pickaxe Heater tagged ores+pickaxe and recipe lookup; Universal Attractor local push/pull, rank5 teleport. These are initial rolls, not final maxima. No automatic item inventory routing, creative flight or unlimited instant food from tooltip. 
   
## 5. 29-axis preliminary navigation and teaching scope

| Axis | Status | Concrete system |
|---|---|---|
| gear | core | equipped artifacts, relics and held active utility |
| movement | core | blink, rewind, hover, glide, sprint, swimming, liquid traversal |
| ore_processing | limited | Pickaxe Heater chance smelting, gold tooth recipes |
| storage | limited | Cut Glass Boot fluid buffer, Rider Flute mounts, no general warehouse |
| crafting | limited | ordinary support recipes, Mimi-Dust |
| logistics | limited | local attraction, mount recall, liquid storage |
| buffs | core | HP/regen, damage/speed/attribute modifiers, food, reach |
| external_compatibility | core | RA redirect, Relics, Curios, OctoLib, other mods |
| mob_farming | limited | Looting, XP, pet damage, no spawner |
| ore_generation | absent | gathering bonuses, no ore block generator |
| permanent_power | limited | item-bound progression, not innate Path ranks |
| general_generation | limited | reusable food, teeth/meatball drops |
| direct_damage | core | orbs/stars, retaliation, wither, gloves |
| block_breaking | present | speed, extra Fortune, auto-smelt |
| permanent_pets | limited | enhance/store existing mounts/pets, no ordinary summon |
| temporary_pets | limited | decoys/temporary helpers not permanent pet |
| defense | core | shield, dodge, HP, invulnerability, repel, elemental |
| healing_recovery | core | lifesteal, regen, food, sacrifice, health rewind |
| debuff_control | present | Antidote, visibility, stun, breath |
| energy | present | item XP, vanilla XP, charges, cooldown, food/durability |
| automation_control | limited | passive triggers/local effects, no machine network |
| agriculture | limited | native banned ring breeding bonus, food/fishing, pet aid |
| detection_information | present | inspection research, quality/stat/XP UI, vision |
| equipment_augmentation | core | item levels, rank modifiers, reroll and Curios slots |
| world_manipulation | limited | leaf absorption, grass consumption, no builder |
| world_content | present | native loot and Mimic, no new dimension |
| death_respawn | core | actual repeatable lethal interception, no grave retrieval |
| trade_economy | present | Piglin Mask and Villager Hat, gold teeth |
| progression_tiers | core | relic level/points/research/quality/rank, not universal skill tree |

Proposed lesson clusters (not final individual quest map): obtain first relic and inspect; research constellation; earn XP from specific ability; spend vanilla XP and relic points; compare quality and reroll; reset; complete rankup; item activation and targeting; equip Curios slot, expand charms, Experience Disperser; defensive/healing/movement examples; hand/food charge items; combat and gathering, merchant and pet utilities, mimic and loot; lethal save mechanics optional explanatory node (do not mandate fatal damage); exclude banned ring from survival route. Individual item/rank/loot paths in next pass. Current artifacts.snbt has three optional checkmarks, not one; preserve existing IDs. 
   
## 6. Status / handoff

Prior combined score39/55 (7 independent, Substantial) remains **provisional**, no new rating or score credit for ring. All three pins checked, two exact JARs CRC/hash verified, 0.12.9 source core/registry/ring/belts verified, old 0.12.5 explicitly not substitute. Need exact 0.12.9 binary from owner upload/Drive, full RA and Relics numerical template+handler census, effective acquisition/mimic/loot, individual instructional map. No build, config, recipe or quest changes. Keep draft status needs-research until native source completeness and exact version question resolved. No duplicate runtime work.
