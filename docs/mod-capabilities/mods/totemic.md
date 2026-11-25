# Totemic 1.21.1-0.12.18 — native capability Atlas

**Native research COMPLETE, not gameplay acceptance.** Two exact-JAR research passes and final consolidation, #410. Native ceremonies, effects and progression mapped; TMM ritual-first policy remains separate from native capabilities. Extended Totems is separate research [#749](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/749), not approved implementation. Summoning Rituals owner-approved removal #748 is separately assigned to integration; no roster edits here.

## Evidence
Owner supplied Totemic-neoforge-1.21.1-0.12.18.jar, SHA512 4b3c4a6c926b8bf203b5593c558264b096f870ecfe74e6b6dbeef850825c9814a3b9a2d9f03c3a91680042dd684574f0259280210b9173750c921ff733e94896, matching [Packwiz](../../../mods/fenns_totemic.pw.toml) Modrinth GLffYJtD/6Vny8MoL. ZIP 721 entries, CRC pass, 28 English Totempedia entries/4 categories, 14 ceremonies,12 functional carvings plus none,6 instrument families,9 wood types,3 living entity types plus invisible arrow,26 crafting and3 cooking records. Minecraft 1.21.1 NeoForge >=21.1.1; Patchouli >=1.21-87 optional. Exact release guide JSON/localization, advancement and recipe records, targeted javap of registered constructors, StateStartup, StateTotemEffect, MedicineBagItem, effects and KubeJS builders. [Pass1](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/410#issuecomment-7172533612), [pass2](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/410#issuecomment-7172605115). No JAR redistribution or runtime acceptance. Prior comprehension source-version gap resolved with exact binary.

## Entry and passive totems
Totempedia 6 burnable logs +3 paper, conditional Patchouli recipe. Whittling Knife iron/flint/2 sticks; carve ordinary eligible logs into base and up to five pole sections, cycle pattern using right-click or sneak-scroll; Staff right-click info/left-click cancel. Tipi on flat dirt/grass open sky, sleeping; Totem Torch light. Nine native wood types; Cedar progression via Rite of Spring, not required for basic poles.
Passive Melody separately stored from startup music, max7680. Range 5+floor(Melody/1920)+1 if five pole blocks; AABB rather than spherical. Standard scaling potion amplifier floor((identical carvings-1)/2)+1 if Melody>=5760; interval80t, applied duration100t; specialized effects and non-scaling potion effects differ. 12 carvings: bat slow fall/fall immunity, blaze fire protection, buffalo haste, cow resistance+slowness, enderman night vision, horse speed, ocelot suppresses creeper explosions, pig luck, rabbit jump, spider climb, squid water breathing, wolf strength. Native config disabledTotemCarvings/disabledCeremonies/medicineBagBlacklist; effective TMM passive policy not redefined here. Medicine Bag sneak-use on carved pole copies effect, toggle use; max4800, default80 drain per80t, default duration240s, recharge400 per20t within6 blocks matching base (12s empty to full), potion amplifier0; carving-specific drain and all effects must support bag. 
## Instruments and Melody
| Instrument | Base contribution | Per-type ceremony cap | Acquisition / operation |
|---|---:|---:|---|
| Flute |180|3000|3 sticks + leaf; hold play; can upgrade through Syrinx |
| Drum |240|3300|3 leather +3 logs +3 wool; strike by hand, multiple drums for rhythm |
| Wind Chime |120|1500|3 cedar logs +2 string +2 copper; hang with free space beneath, leaves help, nearby excess inhibits |
| Jingle Dress |180|1500|3 cedar leaves +3 iron bells +leather; wear/move/jump, speed boosts, not selector |
| Rattle |300|3300|Buffalo tooth +3 logs +stick; combine main hand with offhand flute |
| Eagle-Bone Whistle |360|3600|Eagle bone +feather +string |
Select ceremony by sneak-playing two instruments in order at base, then stop sneaking and perform within startup time. Staff can cancel; smoke indicates instrument cap; cap by type not by item, so music diversity needed. Startup time multipliers peaceful/easy1.1, normal1, hard.875; difficulty-specific actual times use native code. Native Totempedia ceremony and instrument entries gated by wind_chime_unlocked, rattle_unlocked, eagle_bone_whistle_unlocked alternative acquisition criteria; guide unlock is not performance proof. 
## All native ceremonies
| Registry ID (totemic:) / guide name | Selectors | Melody | Base startup ticks | Effect / operation |
|---|---|---:|---:|---|
| war_dance / War Dance | Drum Drum |4500|400|Strength and speed 3600t, area8 |
| depths / Symphony of the Depths | Flute Flute |4500|400|Water breathing3600t, area8 |
| fertility / Rite of Spring | Flute Drum |5280|460|400t effect, dropped breeding food within8, saplings→Cedar |
| zaphkiel_waltz / Zaphkiel Waltz | Chime Flute |6720|400|900t plant acceleration |
| animal_growth / Hymn of Maturity | Flute Chime |6900|420|900t baby animal/egg acceleration |
| buffalo_dance / Buffalo Dance | Drum Chime |7380|480|Eligible cows→baby buffalo within8 |
| rain / Rain Dance | Drum Rattle |10980|520|Rain6000t |
| drought / Drought Dance | Rattle Drum |10980|520|Clear weather6000t |
| flute_infusion / Serenade for Syrinx | Flute Rattle |11340|560|Flute item within6→Pan's Flute, stronger music and animal/villager attraction |
| eagle_dance / Eagle Dance | Rattle Chime |11580|500|Parrot→eagle within8 |
| cleansing / Steve's Lullaby | Whistle Flute |14700|600|Weakened undead within8; zombie villager, zombified piglin, zoglin, zombie horse conversion data |
| sun_dance / Sun Dance | Drum Whistle |14820|620|Health sacrifice not below half heart, area8, regen300t and absorption6000t |
| danse_macabre / Danse Macabre | Whistle Chime |14940|640|900t effect, rotten flesh within6, undead strength/speed1200t; no guaranteed ownership |
| baykok_summon / Totentanz | Chime Whistle |15060|640|80t effect, spawn at last tick near heightmap position; not selectable peaceful |
Cedar→Chime/Dress→Buffalo tooth/Rattle→Eagle bone/Whistle→advanced ceremonies. Native buffalo hide0–4/tooth0–2/meat1–4; eagle bone1–2/feather1–3; loot modifiers apply. Baykok maxHP200, invisible arrows, guaranteed mainhand Bow drop in entity class; bones2–8, arrows3–10, flesh0–2, wither skull25% else skeleton skull before Looting. Native drop guarantees do not override TMM loot policy.
## Detection and integrations
Custom trigger totemic:performed_ceremony on successful transition from StateStartup to StateCeremonyEffect, nearby players within8 AABB (not solely actual performers, not completion of extended effect). Existing exact-pin [TotemicPerformanceMixin](../../../compat/progression/src/main/java/io/github/heatwaveqc/tyr/progression/mxp/mixin/TotemicPerformanceMixin.java) tracks accepted eligible player music contributions and noncancelled transition; no new XP logic. Quest evidence: use verified native advancement where available, item acquisition only as possession proxy, manual observable effect otherwise, especially actual summoned boss defeat. Existing chapter one checkmark, no runtime quest edits.
KubeJS native startup registry totemic:c_totem_carving, TotemCarvingBuilder.effect(TotemEffect), potion(Holder<MobEffect>), medicineBagDrain(int); TotemicEvents.modifyTotemCarvings effects/drain, totemic:d_ceremony and totemic:a_instrument registries. Not plain datapack JSON. TotemEffect.effect(Level,BlockPos,int,TotemEffectContext), medicineBagEffect(Player,ItemStack,int), getInterval default80, supportsMedicineBag default true; PlayerTotemEffect handles server-side nearby players/filters spectators. Models assets/<namespace>/models/block/totem_pole_<path>.json, textures, translation totemic.totem.<namespace>.<path>. No KubeJS coercion or per-mod mana integration verified; owner requires shared Progression hooks in #749. Do not introduce new carving or change policy here.

## 29-axis navigation
| Axis | Finding |
|---|---|
| gear | Knife, Staff, six instrument families, bag, Baykok bow |
| movement | Horse/rabbit/spider/bat effects, War Dance; no flight |
| ore_processing | No independent refining evidenced |
| storage | Melody in base, Medicine Bag charge; no general storage |
| crafting | Native recipes, Cedar, flute infusion, hide conversion |
| logistics | Pan flute attraction, no item network |
| buffs | 12 carvings, War Dance, Depths, Sun Dance, music scaling |
| external_compatibility | KubeJS registry/events, Patchouli, Progression hook; #749 separate |
| mob_farming | Breeding, maturity, Buffalo/Eagle conversion, Danse Macabre |
| ore_generation | No native independent ore source |
| permanent_power | Guide unlocks; no native permanent innate stat system |
| general_generation | Cedar growth, animal conversions, undead summons |
| direct_damage | Bow/arrow combat; no generic spell damage system |
| block_breaking | Buffalo haste supports mining; knife carving is block modification |
| permanent_pets | No native persistent owned helper asserted |
| temporary_pets | Undead spawned but ownership unproven, no independent summon ally claim |
| defense | Bat/blaze/cow/ocelot carvings, Sun Dance absorption |
| healing_recovery | Sun Dance, cleansing zombified creatures |
| debuff_control | Cow slowness, ocelot creeper suppression, cure prerequisites |
| energy | Local passive Melody and ceremony counters, typed caps, bag charge; no FE/mana conversion |
| automation_control | Wind chime passive music; selector and deliberate performance |
| agriculture | Rite of Spring, Waltz, Maturity, Cedar, buffalo/eagles |
| detection_information | Totempedia and Staff state, cap feedback |
| equipment_augmentation | Pan flute infusion, carved bag effect, enchantable bow |
| world_manipulation | Weather, plant growth, totem carving, animal conversion |
| world_content | Cedar and three living mob types, no new dimension evidenced |
| death_respawn | Tipi sleeping; no resurrection asserted |
| trade_economy | No dedicated vendor or currency ladder evidenced |
| progression_tiers | Instrument acquisition stages (analyst), guide milestones and boss |
 
## Representative teaching map
All 14 native ceremonies in a compact map. Dependencies instructional, not invented mandatory gates; native passive lessons may need adaptation to final TMM policy. No rewards or FTB IDs authored; ordinary JEI recipes remain guide references. 
| ID | Prior lessons | Teach / evidence |
|---|---|---|
| TO-01 | — | Obtain Totempedia and knife |
| TO-02 | TO-01 | Construct base/pole, carve and inspect via Staff |
| TO-03 | TO-02 | Basic flute and drum, learn selection and capped Melody |
| TO-04 | TO-03 | War Dance: actual effect |
| TO-05 | TO-03 | Symphony of Depths: actual effect |
| TO-06 | TO-03 | Rite of Spring: Cedar and breeding; collect sapling |
| TO-07 | TO-06 | Grow Cedar, craft Wind Chime and Jingle Dress |
| TO-08 | TO-07 | Zaphkiel Waltz: demonstrate crop growth |
| TO-09 | TO-07 | Hymn of Maturity: demonstrate growth/hatching |
| TO-10 | TO-07 | Buffalo Dance: conversion; obtain tooth and meat/hide |
| TO-11 | TO-10 | Craft Rattle and combine instrument performance |
| TO-12 | TO-11 | Rain Dance: weather change |
| TO-13 | TO-11 | Drought Dance: clear rain |
| TO-14 | TO-11 | Serenade for Syrinx: Pan flute infusion and attraction |
| TO-15 | TO-11 | Eagle Dance: conversion and collect bone/feather |
| TO-16 | TO-15 | Craft whistle, practice diverse Melody |
| TO-17 | TO-16 | Steve's Lullaby: cure eligible weakened undead |
| TO-18 | TO-16 | Sun Dance: sacrifice then healing/protection |
| TO-19 | TO-16 | Danse Macabre: native summon, not pet claim |
| TO-20 | TO-16 | Totentanz: actual Baykok spawn, not just startup trigger |
| TO-21 | TO-20 | Defeat Baykok, obtain bow and inspect loot |
| TO-22 | TO-02, TO-03 | Optional passive effect scaling and cap, only if enabled in TMM |
| TO-23 | TO-22 | Optional Medicine Bag charge/portable effect if enabled |
| TO-24 | TO-06 | Tipi, Totem Torch, Cedar building materials |

## Boundaries and next
Native mechanics assumed functional in released mod, not gameplay accepted in TMM. Exact server overrides/loot, claim behavior, passive/Medicine Bag policy and any new carving/resource adapter remain separate pack work. No general runtime QA prerequisite. #749 research only; #748 removal separately. Next Reliquary Reincarnations 2.0.85.1599.
