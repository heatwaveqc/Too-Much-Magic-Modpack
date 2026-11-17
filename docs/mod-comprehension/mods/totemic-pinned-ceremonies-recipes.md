# Totemic pinned ceremony, instrument and recipe audit

Exact artifact: `Totemic-neoforge-1.21.1-0.12.18.jar`, SHA-512 `4b3c4a6c926b8bf203b5593c558264b096f870ecfe74e6b6dbeef850825c9814a3b9a2d9f03c3a91680042dd684574f0259280210b9173750c921ff733e94896`. Read directly from local JAR; Java methods inspected using CFR 0.152. No assets or decompiled source are committed. This supersedes the parent profile's historical missing-JAR/0.12.19-only limitation. [Parent profile](totemic.md) now supplies the complete walkthrough, entities and support branches; native research is source-complete. No gameplay acceptance is claimed.

## Selection and performance contract

Carve a base from a burnable log with the knife in base mode. Carved pole blocks directly above it contribute up to five consecutive pieces; a gap ends the scan. The ceremony state machine itself does not require a minimum number of carvings. Sneak while playing the two selector instruments **in order**, then stop sneaking and make music. The selector targets the highest-priority selectable base, nearest on equal priority, in the default block-entity scan range 5. An incomplete selection expires after the `time++ >= 1200` check; each selector resets its timer. Unknown combinations or failed selection prerequisites return to the preceding passive state with smoke.

Startup counts music separately for each instrument registry type, clamped to its maximum below. More copies/players can speed delivery but cannot raise a type's cap. Music is divided by integer division among all accepting blocks at the highest priority in the scan; nearby simultaneous ceremonies can split the input. Startup has priority 16. Normal startup limit is the table's ticks; Peaceful/Easy default multiplier 1.1, Hard 0.875, truncated to integer. Server config can change these or disable individual ceremonies. Success is checked before the timeout, invokes native startup-success hooks, and transitions to the effect; failure invokes timeout hooks and clears to a fresh passive state. Carving passive effects do not tick during selection/startup/ceremony states. Ordinary music stored before selection is not the startup meter.

The effect state invokes the effect at time zero before incrementing its time. Default effect duration zero therefore still executes once. Timed effects run time 0 through duration−1. Staff left-click on the base cancels selection/startup/effect; right-click reports state (advanced tooltips also expose passive music). A comparator reads passive music ×15/7680, startup music ×15/target, and 15 during effect; this is not proof that a resulting entity/drop was delivered.

Startup/effect NBT stores ceremony ID, time and optional instance payload; startup also stores each instrument's accumulated music. Neither saves the initiating entity/player. After reload, initiator-dependent attribution can be absent. Unknown ceremony ID resets to passive. Selection saves its selectors/time and prior passive music.

Sources: `init/ModContent`, `api/ceremony/Ceremony{,Instance}`, `apiimpl/{registry/RegistryApiImpl,music/MusicApiImpl,ceremony/CeremonyAPIImpl}`, `api/music/DefaultMusicAcceptor`, `block/totem/entity/State{Selection,Startup,CeremonyEffect,TotemEffect}`, `TotemicConfig`, `block/totem/TotemBaseBlock`.

## Six instrument types

| Type | Base output / startup cap | Actual playing and limits |
| --- | --- | --- |
| flute | 180 / 3000 | Right-click; sneak selects. Normal and Infused Flute share 20-tick cooldowns and the same registry cap. Infused adds random 0–119 output, not another type. |
| drum | 240 / 3300 | Left/right-click block; sneak selects; each drum block has a 20-tick played-state cooldown. |
| wind_chime | 120 / 1500 | Automatic 160-tick playing burst, one note each 40 ticks; directly overhead leaves add 60 per note. Between bursts cooldown is integer `20*(40+5*Gaussian)` ticks. Sneak click manually sends a selector, not normal music. Needs supported underside or leaves above, nonsolid block below. On load congestion checks other uncongested chimes within range 8; count >2 congests this chime. Removing chimes retries congestion. |
| jingle_dress | 180 / 1500 | Worn leggings inventory slot 37, nonspectator player; every 20 ticks accumulate integer movement charge clamped 0–8, charge >=10 plays once then modulo 10. Speed effect multiplies measured movement by 1.2. Dancing is this instrument's movement mechanic, not a separate global ceremony completion check. |
| rattle | 300 / 3300 | Player swing (use can initiate swing), cooldown 16 ticks; sneak selects. Main-hand Rattle plus offhand Flute is an explicit guide tip. |
| eagle_bone_whistle | 360 / 3600 | Right-click; sneak selects; 20-tick cooldown. |

All six caps sum to 16200. Five types without whistle sum to 12600; larger ceremonies require whistle contribution under unmodified native values. Instrument methods spend cooldowns but show no consumable fuel or ordinary durability charge. These are native defaults, subject to KubeJS mutations and effective server settings.

## All 14 registered ceremonies

Times below are raw game ticks; 20 ticks nominally one second. Ranges are axis-aligned regions, not promises of spherical targeting. See exact classes `ceremony/*Ceremony` and `init/ModContent`.

| ID (`totemic:`) | Ordered selectors | Music | Startup ticks | Effect and exact restrictions |
| --- | --- | --- | --- | --- |
| `war_dance` | drum → drum | 4500 | 400 | Once: players within 8 receive Strength II and Speed II for 3600 ticks. |
| `depths` | flute → flute | 4500 | 400 | Once: players within 8 receive Water Breathing amplifier 1 for 3600 ticks. |
| `fertility` | flute → drum | 5280 | 460 | 400 ticks; every 20 converts at most one noncedar #minecraft:saplings block within 6 to Cedar Sapling, and sets at most one age-0 not-in-love Animal within 8 in love if a matching dropped food exists within 8. Food shrinks by one with probability 2/3. Despite method name, this body does not breed villagers. |
| `zaphkiel_waltz` | wind_chime → flute | 6720 | 400 | 900 ticks; every 7 scans range-6 block box; random-ticking blocks in #totemic:zaphkiel_waltz_growable get one randomTick with probability 3/4. This calls native random tick, not generic bonemeal or an unrestricted machine ticker. |
| `animal_growth` | flute → wind_chime | 6900 | 420 | 900 ticks; every 20: baby Animals within 8 outside Hymn blacklist have 1/4 chance ageUp(random 0–59); dropped eggs within 8 have 1/4 chance spawn baby Chicken and shrink one egg; turtle eggs on sand within 6 have 1/45 chance progress hatch state or spawn each egg as baby Turtle. |
| `buffalo_dance` | drum → wind_chime | 7380 | 480 | Once: selection requires alive Mob in #totemic:buffalo_dance_targets within 8 (bundled cow). Converts up to two to baby Buffalo, preserving health fraction, position and leash; removes original before unchecked addFreshEntity. Not an egg or owned pet output. |
| `rain` | drum → rattle | 10980 | 520 | Once: if ServerLevel is not already raining, sets rain 6000 ticks, no thunder. Existing rain leaves timers unchanged. |
| `drought` | rattle → drum | 10980 | 520 | Once: if raining, sets clear 6000 ticks, no thunder. Existing clear weather leaves timers unchanged. |
| `flute_infusion` | flute → rattle | 11340 | 560 | Once: range 6, every dropped Flute entity and every Flute inventory slot of nearby players replaced by a fresh single Infused Flute. Whole stack/component replacement; not a one-unit conversion preserving NBT. Normal and infused share music cap/cooldown. Infused normal playing also installs attraction goals on nearby eligible ground Animals/Villagers within 2. |
| `eagle_dance` | rattle → wind_chime | 11580 | 500 | Once: selection requires alive Mob in #totemic:eagle_dance_targets within 8 (bundled parrot). Converts up to two to Bald Eagles, copies position/leash, discards originals before unchecked addFreshEntity; does not copy pet ownership. |
| `cleansing` | eagle_bone_whistle → flute | 14700 | 600 | Once: selection requires nearby (8) convertible Mob with Weakness. Bundled data map: Zoglin→Hoglin, Zombie Horse→Horse, Zombie Villager→Villager, Zombified Piglin→Piglin. Zombie Villager starts conversion with timer 1 and optional player UUID; others convertTo(...,true), receive Nausea 200 ticks. Weakness not replaced by generic undead eligibility. |
| `sun_dance` | drum → eagle_bone_whistle | 14820 | 620 | Startup every 20 ticks at phase 10 hurts nonspectator players within 8 by 1 when health >1; this cost remains on failed attempts. Once successful: Regeneration IV 300 ticks and Absorption V 6000 ticks to players within 8. |
| `danse_macabre` | eagle_bone_whistle → wind_chime | 14940 | 640 | Non-Peaceful selection. 900 ticks; every 20 each Rotten Flesh item entity within 6 has 1/4 chance to spend one and summon an undead if two local block collision shapes are empty. Nether→Zombified Piglin; otherwise 1/10 Zombie Villager; otherwise water→Drowned, desert→Husk, else Zombie. Strength III/Speed III 1200 ticks. Input shrunk before unchecked spawn. |
| `baykok_summon` | wind_chime → eagle_bone_whistle | 15060 | 640 | Non-Peaceful selection. 80-tick effect, at time 79 spawns Baykok at heightmap surface one horizontal block from the base. Spawn result unchecked. Prepare arena before selecting; ceremony advancement is not kill evidence. |

## All 31 bundled recipe resources

These are exact native JSON records, including three cooking recipes in the `minecraft` namespace. Tags retain `#`; effective membership and later pack replacements require separate loaded-pack inspection. Shaped rows preserve spaces and `/` separates grid rows. This table documents recipes, not proof that every tag is populated.

| Resource ID | Type | Pattern / inputs | Result |
| --- | --- | --- | --- |
| `minecraft:cooked_buffalo_meat_from_campfire_cooking` | minecraft:campfire_cooking | totemic:buffalo_meat; 600 ticks; XP 0.35 | 1 × `totemic:cooked_buffalo_meat` |
| `minecraft:cooked_buffalo_meat_from_smelting` | minecraft:smelting | totemic:buffalo_meat; 200 ticks; XP 0.35 | 1 × `totemic:cooked_buffalo_meat` |
| `minecraft:cooked_buffalo_meat_from_smoking` | minecraft:smoking | totemic:buffalo_meat; 100 ticks; XP 0.35 | 1 × `totemic:cooked_buffalo_meat` |
| `totemic:cedar_button` | minecraft:crafting_shapeless | totemic:cedar_planks | 1 × `totemic:cedar_button` |
| `totemic:cedar_door` | minecraft:crafting_shaped | `##/##/##`; #=totemic:cedar_planks | 3 × `totemic:cedar_door` |
| `totemic:cedar_fence` | minecraft:crafting_shaped | `W#W/W#W`; #=minecraft:stick, W=totemic:cedar_planks | 3 × `totemic:cedar_fence` |
| `totemic:cedar_fence_gate` | minecraft:crafting_shaped | `#W#/#W#`; #=minecraft:stick, W=totemic:cedar_planks | 1 × `totemic:cedar_fence_gate` |
| `totemic:cedar_hanging_sign` | minecraft:crafting_shaped | `X X/###/###`; #=totemic:stripped_cedar_log, X=minecraft:chain | 6 × `totemic:cedar_hanging_sign` |
| `totemic:cedar_planks` | minecraft:crafting_shapeless | #totemic:cedar_logs | 4 × `totemic:cedar_planks` |
| `totemic:cedar_pressure_plate` | minecraft:crafting_shaped | `##`; #=totemic:cedar_planks | 1 × `totemic:cedar_pressure_plate` |
| `totemic:cedar_sign` | minecraft:crafting_shaped | `###/###/ X `; #=totemic:cedar_planks, X=minecraft:stick | 3 × `totemic:cedar_sign` |
| `totemic:cedar_slab` | minecraft:crafting_shaped | `###`; #=totemic:cedar_planks | 6 × `totemic:cedar_slab` |
| `totemic:cedar_stairs` | minecraft:crafting_shaped | `#  /## /###`; #=totemic:cedar_planks | 4 × `totemic:cedar_stairs` |
| `totemic:cedar_trapdoor` | minecraft:crafting_shaped | `###/###`; #=totemic:cedar_planks | 2 × `totemic:cedar_trapdoor` |
| `totemic:cedar_wood` | minecraft:crafting_shaped | `##/##`; #=totemic:cedar_log | 3 × `totemic:cedar_wood` |
| `totemic:drum` | minecraft:crafting_shaped | `EEE/LWL/WLW`; E=#c:leathers, L=#minecraft:logs_that_burn, W=#minecraft:wool | 1 × `totemic:drum` |
| `totemic:eagle_bone_whistle` | minecraft:crafting_shaped | `S /BF`; B=totemic:eagle_bone, F=totemic:eagle_feather, S=#c:strings | 1 × `totemic:eagle_bone_whistle` |
| `totemic:flute` | minecraft:crafting_shaped | ` LS/ S /S  `; L=#minecraft:leaves, S=#c:rods/wooden | 1 × `totemic:flute` |
| `totemic:iron_bells` | minecraft:crafting_shaped | ` N /NNN/ N `; N=#c:nuggets/iron | 1 × `totemic:iron_bells` |
| `totemic:jingle_dress` | minecraft:crafting_shaped | ` L /BHB/LBL`; B=totemic:iron_bells, H=#c:leathers, L=totemic:cedar_leaves | 1 × `totemic:jingle_dress` |
| `totemic:leather_from_hide` | minecraft:crafting_shapeless | totemic:buffalo_hide | 1 × `minecraft:leather` |
| `totemic:medicine_bag` | minecraft:crafting_shaped | `PST/HDH/ H `; D=#c:gems/diamond, H=totemic:buffalo_hide, P=totemic:cedar_planks, S=#c:strings, T=totemic:buffalo_tooth | 1 × `totemic:medicine_bag` |
| `totemic:rattle` | minecraft:crafting_shaped | ` WW/ BW/S  `; B=totemic:buffalo_tooth, S=#c:rods/wooden, W=#minecraft:logs_that_burn | 1 × `totemic:rattle` |
| `totemic:stripped_cedar_wood` | minecraft:crafting_shaped | `##/##`; #=totemic:stripped_cedar_log | 3 × `totemic:stripped_cedar_wood` |
| `totemic:tipi_from_hide` | minecraft:crafting_shaped | ` S /SWS/W W`; S=#c:rods/wooden, W=totemic:buffalo_hide | 1 × `totemic:tipi` |
| `totemic:tipi_from_wool` | minecraft:crafting_shaped | ` S /SWS/W W`; S=#c:rods/wooden, W=#minecraft:wool | 1 × `totemic:tipi` |
| `totemic:totem_torch` | minecraft:crafting_shaped | `STS/SWS/ S `; S=#c:rods/wooden, T=minecraft:torch, W=#minecraft:logs_that_burn | 2 × `totemic:totem_torch` |
| `totemic:totem_whittling_knife` | minecraft:crafting_shaped | `  I/ SF/S  `; F=minecraft:flint, I=#c:ingots/iron, S=#c:rods/wooden | 1 × `totemic:totem_whittling_knife` |
| `totemic:totemic_staff` | minecraft:crafting_shaped | ` LS/ S /S L`; L=#minecraft:leaves, S=#c:rods/wooden | 1 × `totemic:totemic_staff` |
| `totemic:totempedia` | minecraft:crafting_shaped | `WPW/WPW/WPW`; P=minecraft:paper, W=#minecraft:logs_that_burn; requires Patchouli loaded | 1 × `patchouli:guide_book` {'patchouli:book': 'totemic:totempedia'} |
| `totemic:wind_chime` | minecraft:crafting_shaped | `WWW/S S/C C`; C=#c:ingots/copper, S=#c:strings, W=#totemic:cedar_logs | 1 × `totemic:wind_chime` |

## Guide access and truthful detection

Totempedia recipe is six burnable logs plus three Paper (grid WPW/WPW/WPW), conditional on Patchouli, producing `patchouli:guide_book` with `patchouli:book=totemic:totempedia`. Its Basics entries cover poles, effects, music and staff; Instruments has six types; Ceremonies has basics plus all 14 entries; Misc covers Medicine Bag, Tipi and Totem Torch.

Page locks are broader than instrument possession: `totempedia/wind_chime_unlocked` uses an OR of Fertility ceremony performed, wind-chime possession, wind-chime recipe unlocked, Rattle possession or Whistle possession. Rattle tier ORs Buffalo Dance performed, Rattle possession/recipe or Whistle possession; whistle tier ORs Eagle Dance performed, Whistle possession/recipe. Parent links do not turn each advancement's listed alternatives into an AND. Thus obtaining an upstream ceremony result or recipe can reveal the next tier before crafting its instrument. Raw book JSON points to these advancements.

Native `totemic:performed_ceremony` fires on startup success for players in range 8, before the effect executes. Use this as ceremony-start evidence, not egg hatching, conversion, spawn delivery, curing or boss defeat evidence. Quest design should pair it with output possession or an actual entity/state detector where needed; none is implemented here.

## Completion disposition

The parent now reconciles exact carving/passive/Medicine Bag contracts, every material gate through Baykok, support blocks, renewable entities, KubeJS and quest evidence. Source research is complete for native content; client/gameplay acceptance and effective world configuration remain untested. No runtime changes were made.
