# Relics / Artifacts / Reliquified Artifacts — combined native capability dossier

2026-11-27; tracker [#410](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/410). **Exact three-JAR native quest-source survey; numerical ledger and acquisition/teaching map complete.** Extends [preliminary evaluation](artifact-family-evaluation.md). Scope is one installed ecosystem, not duplicate credits for vanilla Artifacts effects and their replacements. No gameplay, balance, config, loot, recipe or quest changes.

## Provenance

- Artifacts 13.2.5 exact Drive JAR, 1,086,922 bytes, SHA512 9079bc8a22c237881b22b38027bbab2a8a2e685a1f49c1e1e0e5796f3e0a5509a130d73753c7b2980f12e4f00845f8e8c967ee969f1eadcd52139c2780afe09d matches [pin](../../../mods/artifacts.pw.toml). CRC clean, 1058 entries, 334 classes, 108 loot-table JSON paths, 3 recipe paths (including overlays where present).
- Reliquified Artifacts 1.0.9 exact Drive JAR, 699259 bytes, SHA512 8797ce1a64d483f74fccc380fbaa64821d04505e930b199c1300bbb6c1acfaa40ee2268b926802f3592915320ff500dd509a00308bfd7b69dd0f8c9e8f3172fb matches [pin](../../../mods/reliquified-artifacts.pw.toml). CRC clean, 340 entries,146 classes,12 loot tables,1 recipe. Exact classes, localization, Curios data and recipes enumerated. Prior version-aligned source [37a8e757](https://github.com/Octo-Studios/reliquified-artifacts/tree/37a8e757b809b8d49aa554037cd94537ceb730fd), including registration mixin, retained.
- Relics active [pin](../../../mods/relics.pw.toml) 0.12.9, newly synced exact Drive JAR 5,007,686 bytes, SHA1 6b17e3a3292bf678415b4763cf830df1d01ebc33; CRC clean,1681 entries,702 classes,5 recipes,10 Curios records,14 tag JSONs,3 global loot modifier JSONs. [#725](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/725) independently verified the same owner-upload hash. The previous 0.12.5 evidence gap is resolved; no old binary substituted. 78 javap class transcripts produced across Relics and RA, including 67 concrete template implementations; 90 distinct template definitions and 338 numeric stat records: Relics20 classes/39 abilities/133 stats, RA47 classes/51 abilities/205 stats (48 item IDs because two drinking hats share implementation). [Relics exact numeric ledger](relics-numerical-ledger.md) and [RA exact numeric ledger](reliquified-artifacts-numerical-ledger.md) preserve initial endpoints, scaling model, target, rank gates and named XP sources, plus native loot categories. Count verified in JAR extraction and repository readback. Upstream immutable source [25a864208](https://github.com/Octo-Studios/relics/tree/25a86420867481870470ed1967802948b4aade85) provides complementary handler context. All three exact pin hashes compared and CRC checks passed. No loaded game or config behavior claimed.

## 1. Native progression, quality and slots

Relics 0.12.9 compiled progression, not old 0.8/0.11:
- Items retain abilities, stats/quality, progression, research and option data. Obtain naturally generated item, use inventory inspection/research UI, unlock ability and solve constellation by joining stars based on icon/required degree. Use named ability XP source to level item, earn points and spend points on unlocked researched stat abilities; activate where relevant and use specific target filters/cooldowns. Item XP is not vanilla XP or MXP. Source AbilityData.isUnlocked requires enabled, adequate level, lock and completed research; canPlayerUse delegates to isUnlocked; actual active-use, targeting and handler predicates are separate.
- Template defaults: 10 ability levels per rank, one point/upgrade, required level/rank0, rank modifier .25, max rank5, additive level XP initial100 and step100, unless overridden. Actual relic level cap per current rank is sum(initialMaxLevel * requiredPoints) across ability templates, not global universal 10. Level XP cumulative default sum of 100+100i for i=0..L−1. Spending upgrades consumes (current ability level+1)*50 raw vanilla XP plus configured relic points. Reroll costs150 raw XP, reset costs ability level*250 and refunds invested points. Rankup at full relic level and below max rank: rank+1, resets level, points and ability levels to0. Read source for rank modifiers and item-specific costs. No blanket claim that every ability ranks up to same total.
- Initial stat quality integer0..10, uniformly initialized by random nextInt(11), interpolate first and second initial endpoints, including descending advantageous cooldowns. Display quality is descriptive, not independent innate buff. Perfect quality is second endpoint. AbilityStatData scales from rolled value using modifier computed against second endpoint and target value. Calculate max target points by starting current template cap and, for each remaining rank to maxRank, add ceil(cap*maxLevelRankModifier). Additive model base+(target−bestInitial)*p/N; multiplicative-base base*(1+((target/bestInitial)−1)*p/N) with zero checks; clamp to threshold and round5. Other scaling models possible. Targets do not equal simultaneous realized loadout values; separate handler caps, rounding, slots and conditions. Rank modifiers frequently 1/3/5. Both linked exact-binary ledgers include every named ability template and numeric stat target; constructor thresholds and handler clamps are distinct.
- Exact Artifacts base Curios data head/necklace/belt/hands/feet; base hands2 feet1, other size unspecified. RA exact data adds player bracelet slot, moves onion_ring and withered_bracelet to bracelet, adds artifacts:slot/charm to curios:charm, removes it from belt, removes artifact all tag from curios:all. Exact Relics 0.12.9 slot records: back1/belt1/charm1/curio1/feet2/hands2/head1/necklace1/ring1; its player entity modifier lists feet,back,belt,feet,hands,necklace,ring,charm,head, not curio. Do not interpret duplicate feet entry as four slots. Multiple mods' data merge, not blindly additive. Do not claim a universal exact final slot count. RA ModItemsMixin replaces existing artifact registrations (not parallel copies of old and new powers), both drinking hats share a class, special hand/food redirects. Exact active item keys are artifacts namespace, not reliquified_artifacts except support item Mimi-Dust. 
- Hunting Belt and Kinetic Belt each use RelicSlotModifier.modifier("charm", round(amount)); slots ability 5 initial cap,2 points/level, rank multiplier .1, start1–2 additional charms at quality extremes, target12 at terminal perfect progression. Source explicitly supports 12 additional charm slots per belt as target, not ring slots or guaranteed simultaneous 24. Other belt abilities and existing slot occupancy matter. Shared Curio access available to all Paths under their ordinary rules; do not include as additional Monk compensation (#721).

## 2. Ring of the Seven Deadly Sins — native exceptional power, pack banned

Do not confuse Relics ring with Enigmatic Legacy+'s Ring of Seven Curses (opt-in Hard Mode). Exact 0.12.9 binary and active version-aligned [ring source](https://github.com/Octo-Studios/relics/blob/25a86420867481870470ed1967802948b4aade85/src/main/java/it/hurts/sskirillss/relics/items/relics/ring/RingOfTheSevenDeadlySinsItem.java) agree on the seven template targets. Ring defines seven abilities with initialMaxLevel10 each, default cost1 point per upgrade, leveling initial100/step200 and maxRank0 (no prestige). Perfect terminal target values in source, not rolled initial ranges or loaded config proof:
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

Exact RelicsItems class and template census: 20 relic implementations +5 support items. Ghostly Mantle, Shield of Retaliation and Sphere of Self-Sacrifice are actual current registrations. No deprecated Magma Walker/Rage Glove etc from commented registry credited. Each item below is covered by the linked exact numerical ledger; descriptions summarize selected compiled and version-aligned handler evidence, not every possible combat interaction. 
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
| sphere_of_self_sacrifice | self-sacrifice and restorative effect, resistance stacks, rank1 resistance/3 salvo/5 salvation; exact stacks/resistance template in ledger; detailed event timing separate |
| hunting_belt | charm expansion and pet damage/bone-revival branch |
| experience_disperser | fraction of earned relic XP shared to equipped relics; exact event recursion guard follow-up |
| glitchy_mantle | damage miss, temporary decoy/stun and surface tear |
| ghostly_mantle | current source actual death cancel, temporary flight/immortality, initial cooldown100–125s and duration1–3s |
| shield_of_retaliation | current registered shield/parry/retaliatory combat, exact abilities follow-up |
Support: relic_experience_bottle, golden_tooth, pet_bone, raw_meatball, cooked_meatball. Exact current five recipes: three cooking types (campfire600t/smelt200t/smoke100t, .35XP); 3 teeth→one gold ingot,1 tooth→THREE gold nuggets.

## 4. Artifacts + Reliquified Artifacts exact binary item/ability census

English Artifacts names include mimic_spawn_egg (not equippable relic). Remaining 48 functional artifact item keys are represented in RA exact registration redirects (45 wearable switch cases and three distinct held/food redirects) and both exact mimic item tags; don't double count baseline and replacement. All names below prefix artifacts:. Descriptions summarize packaged RA ability language, distinguish actual earlier selected handler evidence from tooltip-only claims; exact RA template audit in the numerical ledger, selected handler behavior below.
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

Exact readback: 48 functional item IDs + mimic_spawn_egg in base Artifacts localization, 45 wearable redirect switch cases + three special item redirects, 52 RA named ability localization entries. Both exact mimic_loot and mimificable tags contain the same 48 artifact IDs; neither contains the Relics seven-sins ring. The 52 strings are a localization census; compiled constructors contain 51 distinct named ability definitions across 47 implementation classes. Registry keys are not individual abilities. 
Exact RA recipe: Mimi-Dust uses 2 glowstone dust+2 redstone+echo shard; no other packaged recipe path. Compiled MimiDustItem.useOn accepts vanilla chest with ChestBlockEntity on server, removes all stacks matching mimificable tag, counts their item total n, fails if n=0, consumes one dust and replaces chest with hostile Mimic targeted at player. Adds n^1.7 to both attack damage and max HP and fills health. Compiled MimicHandler.LivingDropsEvent uses loaded mimic_loot list, drops n random default-instance artifacts (one if no relicCount set), selection with replacement. Thus exchange unwanted ordinary artifacts for random new copies; no preservation of item-specific XP/quality, no guaranteed desired loot, no native Relics ring in the pool. The chest itself is consumed. Teach use only on dedicated chest, avoid unrelated contents and claim interaction; no arbitrary copying. Exact Artifacts JAR has 108 loot-table JSON paths incl. native integration/overlay data; native acquisition distribution resolved by the compiled modifier and per-item loot categories below. Current source [Angler valuables](../../../kubejs/data/reliquified_artifacts/tags/item/anglers_hat_valuables.json) includes #c:raw_materials optional, amethyst shard, echo shard, prismarine crystals, nautilus shell, heart of sea, optional Dragon Survival elder dragon dust, Draconic Evolution draconium dust and TMM eldritch logic core. This is a notable progression gate interaction (not a guaranteed fishing drop). Angler treasure rank5 and its exact stat targets in the RA ledger; loaded eligible tag population and TMM economy remain separate.

Previously inspected actual source handlers: Chorus Totem LivingDamageEvent.Pre caps eligible fatal damage at health+absorption−1 and attempts safe teleport, sets cooldown, initial75–100s; Kitty Slippers lethal save chance initially2.5–10% and evasion10–25%; Everlasting food regenerates durability initial1–3/25–30s, rank1 restoration/3 preservation/5 quick meal, steak8/.8 beef3/.3; Pickaxe Heater tagged ores+pickaxe and recipe lookup; Universal Attractor local push/pull, rank5 teleport. These are initial rolls, not final maxima. No automatic item inventory routing, creative flight or unlimited instant food from tooltip. 
   
## 5. 29-axis navigation and teaching scope

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
| agriculture | limited | native banned ring breeding bonus, Rooted Boots rank5 fertilizer, food/fishing, pet aid |
| detection_information | present | inspection research, quality/stat/XP UI, vision |
| equipment_augmentation | core | item levels, rank modifiers, reroll and Curios slots |
| world_manipulation | limited | leaf absorption, grass consumption, no builder |
| world_content | present | native loot and Mimic, no new dimension |
| death_respawn | core | actual repeatable lethal interception, no grave retrieval |
| trade_economy | present | Piglin Mask and Villager Hat, gold teeth |
| progression_tiers | core | relic level/points/research/quality/rank, not universal skill tree |

## 6. Actual acquisition and upgrade economy

Compiled RelicLootModifier requires origin AND entity; first rolls configured relicGenChance (default .2), then matches queried loot-table ID, actual dimension, biome at origin against cached entries, sums eligible weights, selects one entry with weighted random, adds default-instance item. No eligible entry or nonpositive total weight produces nothing. 20% is an initial gate per eligible modifier application, not a specific relic's chest drop chance. LootEntries categories in both ledgers: wildcard any matching chests path; Overworld/Nether/End dimensions; Nether-like named fiery/infernal/nether chest IDs and ruined_portal, End-like chest IDs containing end/stronghold; biome-name matching aquatic, forest, mountain, tropical, cave, snow, taiga, desert, savanna, swamp, plains, sculk; village/pillage, bastion/piglin and mine matched chest-table names. Native regexes can match modded chest IDs; not every biome contains an eligible chest. No fixed loot rate guaranteed with loaded overrides, overlapping categories and multiple candidate weights. Default chest roll applies to RA items through Relics API as well.

Important acquisition boundary: exact RA RollLootTableModifierMixin replaces Artifacts original injection handler, discarding generated items implementing IRelicItem while retaining non-relic items; therefore do not count old injection table's artifact chance in addition to new Relics pool. RA ConfigManagerMixin/ModConfigMixin cancel selected old config setup paths. Exact Artifacts assets include underground full/minimalist campsites, campsite chest/barrel, 31 chest injection paths (village variants, structures), five archaeology injection paths and two entity injection paths (cow/mooshroom), native equipment loot, individual item tables and Mimic. These paths are not automatically effective artifact drops after RA; its 12 same-namespace loot records include empty base Mimic pool and equipment overlays (e.g. blaze empty). MimicHandler custom drop route is authoritative for Mimi-Dust exchange (see §4), natural mimic fallback one item. Cook Everlasting Beef into Eternal Steak via smoker100t/furnace200t/campfire600t, .35XP. RA furnace/campfire mixins present; do not promise exact progress component transfer without further handler trace. Current pack KubeJS loot/tag policy supersedes defaults; #721 shared-equipment scope remains controlling.

### Notable handler limits and quantitative consequences

- Fortune and Looting multicast is **consecutive success until first failure or integer cast limit**, not independent binomial trials. Exact MathUtils.multicast(random,p,int N) returns count. Baseline at p=.75008, N≈50: expected additional levels sum(p^k,k=1..N), ~3.0; chance of 50 successes ~5.7e-7 before rank bonuses. LuckyScarf clamps probability to0..1, rounds/limits cast counts, rank1 pity adds previous failures, rank3 chain can add casts, rank5 same-block neighbors multiply chance by 1+efficiency*count, clamps again. At effective chance1 the actual integer cap is reached; avoid presenting baseline expected bonus as max rank expectation. Superstitious Hat separate conditional first-kill/animal/streak modifiers. Both operate on actual loot context; no arbitrary global guaranteed Fortune50. Angler Hat rank5 separate valuable tag source and treasure chance; material-gate interactions are already pack design matters.
- Charm slots: exact Relics tag lists back mantles; belt Kinetic/Hunting; charm Experience Disperser; feet Roller/Springy/Cut Glass; head Piglin/Chef; necklace Reflective/Jellyfish; ring Seven Sins (TMM removes). Other Relics items use held/item behavior. RA charm tag exactly seven: Obsidian Skull, Cloud in Bottle, Warp Drive, Crystal Heart, Universal Attractor, Chorus Totem, Antidote Vessel. RA belt tag replaced with Helium Flamingo only; ring item Onion Ring and Withered Bracelet use bracelet; eight hands items, nine footwear, nine necklaces, original head/face plus Whoopee Cushion. Umbrella and food are held/consumed. RA clears original all-tag, not universal cross-slot equipment. Both belts' slot handler literally rounds amount and adds charm modifier; each has perfect target12, normal one belt slot from Relics, no simultaneous two-belt assumption. Curios resolved slots/duplicate uniqueness and other mods can affect legal builds; no unconditional combined maxima.
- Crystal Heart terminal bonus_health20.007, cooldown10.041s, full-health resistance.64988, low-health heal bonus1.00048, heavy-hit threshold.24975 and immortality duration10.0035s rank5. This is a reserve and conditional defense, not continuous immortality. Cross Necklace salvation and Kitty Slippers also have probabilistic death mitigation. Chorus Totem and Ghostly Mantle actual death prevention handlers, Leafy Mantle foliage and Sphere salvation conditional; do not count all saves as guaranteed simultaneous/independent or post-death inventory recovery. Event order with other mods is separate.
- Vampiric Glove target .99988 lifesteal before streak, kill-heal and overheal rank modifiers; conditional dealt damage and event eligibility, not damage-independent healing. Midnight Mantle uniquely rank7; phase, invisibility, constellation, starfall unlock at relic levels0/5/10/15, modifier gates1/3/5/7; exact stat targets and XP in ledger. Running Shoes and Charm of Sinking have named conditional immortality rank modifier; no global invulnerability guarantee. Rooted Boots rank5 fertilizer interval .96s target, terrain/hunger conditions separate. Endgame numbers are a constructor ceiling reference, not a balance directive.
- Relic XP and vanilla XP are distinct currencies; points are earned from relic leveling, spent per ability. E.g. ring's 70 ordinary relic levels cost cumulative 490000 relic XP under 100+200i (i=0..69), not 490000 raw player XP. The ring is forbidden in TMM and this is native analysis only. One ordinary 10-level ability costs 2750 raw vanilla XP to purchase all upgrades in a rank. Rank-up resets all allocated ability levels; permanent rank determines progression curve. Max rank final perfect-quality target = raw template target if ability fully purchased and unclamped. At earlier ranks targets cannot be assumed achieved. No new personal Path currency.

## 7. Quest-source teaching map (proposed, not authored quests)

Read ledgers for exact item-specific loot category, ability IDs, stat values and XP sources. A possession task establishes acquisition only; research/unlocks, XP gain, rank and successful actions need supported custom task or explicit instructional manual task. Every TMM-eligible native item is represented by one or more rows; no requirement to obtain every random drop before progressing, no artificial maxing of all 68 item IDs, no forced lethal trial or granting banned item. Keep existing three optional checkmark IDs in artifacts.snbt. Group alternative accessories by function, not repeat the same research tutorial 67 times. Rank progression taught once, with optional individual mastery goals. All source items are relics: or artifacts: except Mimi-Dust reliquified_artifacts:mimi_dust.

| ID | Player-facing action / instructional milestone | Evidence |
|---|---|---|
| AF01 | Find first permitted relic via chest exploration, use native inspection interface | actual item |
| AF02 | Inspect quality roll and initial stats, distinguish ability from cosmetic item | UI |
| AF03 | Unlock and solve research constellation for an available ability | researched ability |
| AF04 | Perform item-specific XP action from ledger; observe level/points | XP increase |
| AF05 | Spend relic points plus actual vanilla XP to upgrade | ability level |
| AF06 | Compare quality and optionally reroll at150 vanilla XP | UI/state |
| AF07 | Explain reset/refund and choose build; reset only if desired | UI/state |
| AF08 | Reach full relic level on chosen accessible item, perform rankup and recover levels | rank |
| AF09 | Unlock rank1/3/5 modifier branches, learn optional higher rank7 Midnight Mantle | ability state |
| AF10 | Configure active abilities key and targeting mode; test allowed ability | operation |
| AF11 | Fit legal Curio slots, compare competing items, understand slot expansion | inventory/equip |
| AF12 | Find Kinetic Belt in End-like loot, expand charms and use glide momentum | equip/operation |
| AF13 | Find Hunting Belt in village-like loot; expand charms, pet combat/bone revival lesson | equip/operation |
| AF14 | Experience Disperser: share actual relic XP via dispersion; no self-loop assumption | XP state |
| AF15 | Springy Boot bounce and fall safety, later shockwave | operation |
| AF16 | Roller Skate continuous movement, step height and sparks | operation |
| AF17 | Cut Glass Boot store/select compatible fluid and traverse, do not imply infinite tank | operation |
| AF18 | Chorus Staff blink, actual charge/recharge, target distance | operation |
| AF19 | Clot of Time rewind stored player route, later health rewind | operation |
| AF20 | Rider Flute store/release tamed mount; rank-modified recall and care | operation |
| AF21 | Leafy Mantle foliage healing and camouflage, revival explanation without fatal task | operation |
| AF22 | Reflective Necklace collect/launch reflection orbs | operation |
| AF23 | Jellyfish Necklace water/rain regeneration and shock | operation |
| AF24 | Midnight Mantle phase and invisibility, later constellations/starfall | operation |
| AF25 | Glitchy Mantle evade/illusion/phase, no compulsory damage | operation |
| AF26 | Ghostly Mantle fog/gaze and spectral escape explanation | operation |
| AF27 | Shield of Retaliation timed block, later projectile/stun/guard | operation |
| AF28 | Sphere of Self-Sacrifice explain deliberate HP cost and restorative/stacks, no mandatory fatal action | UI/operation |
| AF29 | Piglin Mask neutrality and barter, collect gold teeth, demonstrate three nuggets/one tooth | operation/craft |
| AF30 | Chef Hat meatball drop, cook in one native recipe | item/operation |
| AF31 | Artifacts aquatic traversal Aqua Dashers and Flippers | operation |
| AF32 | Charm of Sinking and Snorkel breath/depth conditions | operation |
| AF33 | Bunny Hoppers and Cloud in Bottle jumping | operation |
| AF34 | Running Shoes and Steadfast Spikes terrain motion | operation |
| AF35 | Snowshoes native cold terrain and Strider Shoes lava stride | operation |
| AF36 | Helium Flamingo hover reserve, aerial rank modifiers, no creative flight guarantee | operation |
| AF37 | Warp Drive reusable pearl and cooldown | operation |
| AF38 | Charm of Shrinking size, visibility/target and fall conditions | operation |
| AF39 | Umbrella held glider and bounded shield | operation |
| AF40 | Obsidian Skull finite lava buffer and recharge | operation |
| AF41 | Crystal Heart reserve and cooldown, conditional survival | UI/operation |
| AF42 | Chorus Totem lethal save/cooldown explanation; no forced death | UI/manual |
| AF43 | Kitty Slippers aura/evasion/nine lives and Cross Necklace protection/salvation | UI/operation |
| AF44 | Antidote Vessel negative-effect reduction and limiter | operation |
| AF45 | Night Vision Goggles and Scarf of Invisibility | operation |
| AF46 | Panic Necklace target count, resistance/healing, frenzy | operation |
| AF47 | Fire Gauntlet and Flame Pendant fire attack and retaliation | operation |
| AF48 | Shock Pendant and Thorn Pendant retaliation, rank conditional effects | operation |
| AF49 | Feral Claws timed stacks and Power Glove repeated attacks | operation |
| AF50 | Vampiric Glove lifesteal, rank5 overflow absorption; Withered Bracelet debuffs | operation |
| AF51 | Pocket Piston extended interaction and attack range | operation |
| AF52 | Golden Hook mob XP, pull and rank5 disarm | operation |
| AF53 | Digging Claws mining speed, later momentum | operation |
| AF54 | Onion Ring hunger-linked mining and sustenance | operation |
| AF55 | Pickaxe Heater eligible ore smelting, later molten luck | operation |
| AF56 | Lucky Scarf Fortune roll, pity/chain/vein as optional mastery | operation |
| AF57 | Superstitious Hat Looting, first-kill/animal/streak | operation |
| AF58 | Angler's Hat fishing and rank5 treasure, obey effective TMM value tag | operation |
| AF59 | Both drinking hats share implementation; drink faster and rank recovery | operation |
| AF60 | Everlasting Beef regeneration, cook Eternal Steak, compare charge/time | item/operation |
| AF61 | Rooted Boots grass food, later fertilizer | operation |
| AF62 | Cowboy Hat riding and taming/reach/mounted absorption | operation |
| AF63 | Villager Hat trade discount and Golem protection | operation |
| AF64 | Universal Attractor pull/repel and rank5 item teleport | operation |
| AF65 | Whoopee Cushion crouch push, later cloud/paralysis | operation |
| AF66 | Craft Mimi-Dust, dedicate chest with spare artifact(s), defeat resulting Mimic, compare random replacements | craft/encounter |
| AF67 | Explain TMM Ring of Seven Deadly Sins exclusion vs Enigmatic Seven Curses; no acquisition task | manual |
| AF68 | Choose a legal late accessory loadout, compare shared Curio slot competition and conditional effects | manual/equip |

## 8. Final evaluation and handoff

[Final native evaluation](artifact-family-final-evaluation.md): 39/55, seven independent domains, Substantial. Ring contributes no essential independent domain; pack exclusion remains intact. Exact three-JAR evidence plus all constructor ledgers, 29 axes, 68 teaching slices. Actual handler eligibility and numerical clamps verified in selected high-impact systems, not exhaustive event permutation or combined loadout optimization; individual minor effect timings and loaded loot weights, config merge/slot stack interactions, Angler economy, death-save event ordering, held-food progression transfer remain precisely separate downstream questions. No new mechanics, configs, quests, balance changes or game execution. Research ready for docs review, quick CI and designated integration writer.
