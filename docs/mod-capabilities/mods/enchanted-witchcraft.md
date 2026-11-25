# Enchanted: Witchcraft 4.2.7 — native capability Atlas

**Research COMPLETE at native quest-source depth, no gameplay acceptance.** Owner 2026-11-24 approved keeping both Enchanted and JAWR as distinct Witchery variations. No cross-mod material/altar unification, removals or balance changes authorized. Enchanted emphasizes traditional environmental herbology, sympathetic protections, familiars and defensive circle magic; JAWR has additional personal infusions, covens, Tarot, Spirit World, Symbology and two ten-level supernatural bloodlines. Distinct item namespaces and power systems; do not import original Witchery or JAWR mechanics. 
## Evidence
[Pin](../../../mods/enchanted-witchcraft.pw.toml), owner supplied enchanted-neoforge-1.21.1-4.2.7.jar 3,120,843 bytes SHA1 2380cb5a49c7eb86afa6f790a920d1872d5cb85e exact, SHA512 6be310018a71f89c5cc2fc550c119278bdf11ab63d13763308e4b1769c8c0ce8c666990fb671abe6c367f464748a473d43d8f1e269fa14a04af125b8448718d2, CRC PASS reconfirmed,2126 unique paths,468 classes. CF560363/8055168, namespace enchanted, MC>=1.21.1 NeoForge>=21.1.197, stateobserver>=1.6.3, geckolib>=4.7, modopedia>=1.1.10. [Version-aligned source](https://github.com/Favouriteless/Enchanted/tree/55f37c7e15a2848be2cf25ff20232a1893795e75), targeted javap -c -p -constants and packaged JSON/English guide. No whole-source equivalence, runtime or JAR redistribution. 113 recipes (34 shaped,30 spinning,16 kettle,15 shapeless,9 byproduct,6 distilling,3 smelting),47 rites +3 circle shapes,67 altar records (36 blocks,6 tags,25 upgrades),9 mutagen files,110 Modopedia entries/14 categories,52 advancement,62 loot tables,62 Enchanted+40 vanilla tags,4 damage types,3 configured tree features. [Prior profile](../../mod-comprehension/mods/enchanted-witchcraft.md) superseded, [JAWR dossier](witchery-remake.md). [Pass1](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/410#issuecomment-7151578192), [Pass2](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/410#issuecomment-7151639154), [Pass3](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/410#issuecomment-7151726872). Teach quests + Art of Witchcraft + EMI; custom recipe integration support must be checked in actual client.
## Entry, plants and workshop
Art of Witchcraft book+feather+ink sac+Belladonna Seeds gives modopedia:book component enchanted:art_of_witchcraft. Herbology grass seeds, optional hoe-seeds config false, Mandrake screaming danger at harvest (earmuffs/night), entity drop root+1–2 seeds; grow Belladonna, Wolfsbane, Water Artichoke, Snowbell. Arthana rare mob loot Wool of Bat/Tongue of Dog/Creeper Heart; recipe emerald+gold ingot+2 nuggets+stick. Three configured tree features Rowan/Alder/Hawthorn, not proof natural placed tree spawn. Witch Oven iron bars/iron, fuel and clay jars, 200t nominal, base fumes30%, each ordinary funnel +20 points, filtered +35, two side upgrades:50/70/85/100% with appropriate combinations. Nine byproduct recipes oak Exhale, birch Breath, spruce Hint, jungle Foul, logs/raw food Foul, hawthorn Odour, alder Reek, rowan Whiff. Keep ash. 
Kettle craft cauldron+sticks/string; water, heat, ordered ingredients, max heat80, full match and160t cooking; zero AP bypass provider, otherwise one full debit at completion; insufficient AP marks failed, default wrong ingredients spoil. Bucket clears spoiled batch, collect bottle-required result with bottles. Witch's Cauldron explicitly unfinished; do not teach brewing through it. Initial Mutandis Mandrake Root→Exhale→Egg gives6 no AP. Mutandis marks eligible block position in dimension SavedData, Extremis bit; subsequent random tick scans ±2 XYZ (125 blocks). Weights count matching mutagens × variant weight; chance1.5*min(40,count)/280 cap21.43% per eligible random tick not per game tick. Preserve matching state properties on transform. Oak favors Rowan20/Hawthorn5/Alder5, birch Hawthorn20/others5, acacia Alder20/others5, neighboring witch herbs (belladonna/mandrake/wolfsbane/water artichoke). Snowbell+Nether blocks→Ember Moss, cave vines+Rowan leaves/log→Glint Weed, vine+glow lichen/Spanish Moss→Spanish Moss, mushroom+sculk shrieker+Extremis→Blood Poppy. No immediate click-spam transformation. Use exact handbook and mutagen data.
## Altar power, processing and brew ladder
Altar recipe Breath+generic potion+Exhale, 4 stone bricks/2 Rowan logs; output1, six in 3x2 formation. Default range32 ellipsoid; spatial block/tag capacity capped per type (Belladonna8 cap20, logs4 cap50, vanilla leaves2 cap150, enchanted leaves3 cap150). Power cap base sum ×(1+sum highest bonus per upgrade category); recharge base2/t ×(1+sum highest recharge bonus per category), separate maxima. E.g. filled Chalice power+2, Candelabra recharge+2, Wither skull +2/+2, player head +2.5/+3. Not multiplicative stacking, no pooled altar debit; PowerHelper first valid provider. Strict current power>cost (not >=), need headroom above8000 for familiar. Infinity Egg +999/+999 upgrade registered but no survival acquisition established. No FE/JAWR AP bridge proven.
Distillery all6 native recipes default300t,750AP (2.5/t); consumes per working tick, progress not free when insufficient power. Exact routes Breath+lapis+3 jars→Tear/Whiff/slime/Foul; diamond+Oil+3 jars→2 Diamond Vapour+Odour; Vapour+blaze rod+jar→Demonic Blood; Vapour+ghast tear+3 jars→Odour/Reek/Foul/Refined Evil; pearl+6 jars→5 Ender Dew+Whiff; Foul+Quicklime+jar→Gypsum/Oil/slime. Not JAWR Demon Heart requirement. Spinning Wheel default300t; protective recipes500/1000 AP, per tick debit, resets progress when cannot work/pay. All30 recipes include 3 base variants and 9 protection families ×3; offensive voodoo included, count variants per actual files (do not treat as 30 distinct powers).
| Kettle recipe | AP | Output and use |
|---|---:|---|
|Mutandis|0|6, early mutations|
|Extremis|7000|1, Mutandis+wart, advanced mutations|
|Love|0|3, throwable animal breeding|
|Sprouting|0|3, ingredient, standalone ensnare coming soon|
|Depths|0|3, water breathing12000t|
|Grotesque|750|3, disguise6000t, curse reagent|
|Drop of Luck|7000|1, mandrake/wart/Tear/Refined Evil/Extremis|
|Redstone Soup|1000|1, redstone/Luck/wool of bat/tongue of dog/Belladonna/Mandrake|
|Flying Ointment|3000|1, Soup+long swiftness+diamond+feather+wool+Belladonna|
|Happenstance Oil|2000|1, Soup+long night vision+eye+gold carrot+spider eye+mandrake|
|Mystic Unguent|3000|1, Soup+strong strength+diamond+Rowan sapling+creeper heart+Demonic Blood|
|Soul of World|4000|2, Soup+long regeneration+Rowan+Attuned Stone+mandrake+gold apple|
|Spirit of Otherwhere|4000|2, Soup+long swiftness+wool+2 eyes+Luck|
|Golden Chalk|3000|1, mandrake+gold nugget+ritual chalk|
|Nether Chalk|2000|1, wart+blaze powder+ritual chalk|
|Otherwhere Chalk|2000|1, wart+Tear+pearl+ritual chalk|
Advanced infusion-named ingredients do not establish functioning personal power system, no actual native personal infusion rites found. Exact order/component recipes via guide/EMI. 
## Poppetry and persistent identity
Empty taglock capture entity/bed record, filled UUID/name component no expiry found (not JAWR two-day expiry). Bind main hand poppet+offhand filled taglock, hold use, checks player UUID, finish resolves player in current Level and consumes taglock on success; not arbitrary mob beneficiary or serialized specific-item binding. Do not promise offline initial binding. 9 protective families earth (fall/fly into wall), fire, water drowning, hunger starvation, magic predicate, void fell out of world, armor, tool, voodoo. Ordinary/sturdy/infused variants 1/2/1 use, ordinary/sturdy equipment repair10% max durability, infused100%; matching fatal predicate player hook, typically survival at1HP except hunger restores food, void teleports into sky. Infused buffs fire resistance30s, fall/magic resistance10s, saturation V120s, water breathing10s, void fall resistance10s; base drowning resistance5s. Actual tag/predicate and damage pipeline matter, not universal death immunity; default totem enabled. Voodoo protective tryUseVoodoo prefers infused then ordinary/sturdy, infused lightning and Wither200t to attacker. Not guaranteed defense against direct ApplyCurseRite. Offensive Voodoo recipe infused base+needle+Grotesque500 AP, demonstrate with consenting target.
Poppet Shelf four slots, recipe four Attuned Stones/dark oak planks/green wool. Overworld SavedData indexed dimension+position and beneficiary UUID, server inventory updates index, reads/copies first matching poppet, damages or consumes, updates saved inventory, BE only visual sync if present. No explicit proximity/loaded chunk/dimension/online-owner requirement in trigger path; carried first then shelf, one matching successful trigger; verify actual persistence/unload/stale refs, no multiple burn guarantee across other mods. This is an actual remote protective resource, not generic remote storage.
## Circle magic operation and all47 recipe records
Golden heart and ring patterns small7x7 medium11x11 large15x15; X boundary, O interior. Ordinary/Nether/Otherwhere chalk. Right-click heart matches first recipe, disabled rites list, staged consumption one step/20t, requirements, entire upfront AP, sustained cost/tick; charged stone becomes uncharged, not infinite power. On fail cancel refunds recorded items, not necessarily AP/sacrifice restoration. Active rite dimension SavedData, heart loaded for operation/debit, tick counter increments unloaded, insufficient AP/missing heart stops. Rite check conditions including thunder for Sinking. Exact circle and item order from packaged rite JSON + handbook, not legacy tutorial.
| Family (record count) | Circle/conditions | AP and result |
|---|---|---|
|Bind Familiar (1)|medium ordinary, tamed cat; Tear/Odour/Whiff/Demonic Blood/diamond|8000, conversion after300t|
|Bind Talisman (2)|actual existing shapes, talisman/redstone or charged/glowstone|1000 or charged, removes and records chalk, place validated shapes|
|Blight (1)|medium nether, charged + Soup/Reek/melon/rotten flesh/diamond/fermented eye|radius50, radial3t step, decay crops/land, villager zombify|
|Broiling (2)|medium nether, ash/coal/blaze rod|1000 or charged, radius4 actual smelting on item entities, chance waste|
|Charging Stone (1)|small+medium ordinary, stone/ash/quicklime/glowstone/redstone|2000, charged stone|
|Curse (4)|medium nether, taglock/Exhale/Grotesque + type-specific ingredients|Clumsy1500; Misfortune/Overheating/Sinking2000, Sinking thunder|
|Remove Curse (4)|small ordinary, taglock/Breath + specific brew/material|2000 each,300t removal rite|
|Duplicate Waystone (2)|small ordinary,2 bound Waystones+Ender Dew/redstone or charged/quicklime|500 or charged; likely defect: factory seeks unbound Waystone identity in consumed list, no actual output guaranteed|
|Fertility (2)|small ordinary, Hint/Vapour/Quicklime/Gypsum/Mutandis/bone meal or Extremis+charged|0 AP base/charged variant, radius25 radial5t step, growth/heal/cure zombie villager|
|Forest (1)|medium ordinary, Wicker/Sprouting/Ent Twig|4000, radius25 spacing7 actual TreeGrower subject terrain|
|Imprisonment (1)|small ordinary, slime/redstone|500+3/t, fiery confinement for mobs|
|Infuse Broom (1)|small+medium ordinary, broom/Flying Ointment|3000, rideable flying entity|
|Protection (7)|small or medium ordinary, obsidian/redstone or glowstone; location via bound Waystone; temporary charged stone and blooded variant|radius4 500+4/t; large6 1000+6/t; temporary4 1000+6/t duration1200 blocks players|
|Sanctity (1)|small ordinary, feather/redstone|500+3/t, repels mobs|
|Sky Wrath (6)|small ordinary, iron sword/quicklime, local/location/blooded targets each ordinary/charged|1000 or charged, lightning|
|Summon Entity (1)|medium Otherwhere, Waystone/taglock/Ender Dew/iron axe|3000, move existing available target to circle|
|Summon Familiar (1)|medium ordinary, Breath/Hint/Whiff|1000, recall existing/saved familiar|
|Total Eclipse (2)|small ordinary, stone axe/quicklime or iron axe/quicklime/charged|3000 or charged, day start+18000|
|Transpose Caster (2)|small Otherwhere, bound or blooded Waystone|0 AP, consumed focus, move caster|
|Transpose Iron (1)|large Otherwhere, Vapour/charged/pearl/iron ingot/blaze powder|scan interior columns to min height, remove actual iron ore, drop resources at heart|
|Waystone Binding (4)|small ordinary, unbound Waystone/Ender Dew/glowstone or charged+ash; blooded taglock and slime/snowball or charged/icy needle/slime|500 or charged, location/dimension or UUID focus|
Protection real hollow sphere barrier blocks replace air/eligible fluid, StateObserver repairs, cleanup waterlogged→water else air. Ordinary crouching player passes, no owner whitelist; temporary blocks players, not absolute teleport proof. Imprisonment/Sanctity projectile/teleport exceptions per guide. Altar base40 AP/s insufficient sustain3–6/t without buffer or upgrades. No claim checks established for barrier, talisman placement, teleport or Transpose Iron; overlap/cleanup and MXP attribution separate. Waystone distinct from generic Waystones mod. EntityUtils online players first then all levels loaded UUID; no arbitrary offline entity teleport. DimensionTransition or teleportTo center+.01 Y, no explicit safety search; prepare destination. Witch hunter gear protections in guide; actual pack efficacy separate. Duplicate Waystone mismatch must not teach guaranteed success; correct via separately authorized work if needed.
## Familiars, broom and curse behavior
Only native cat type registered, tamed cat replaced after bind, owner SavedData, health50HP armor5 speed.3 damage3, fire/fall/drowning immunity not all damage. Familiar strengthens curses0→1; no general AP discount found. Recall searches UUID across worlds, reconstructs saved NBT if missing, sets persistence and UUID, old duplicate discard. FamiliarCat death calls dismiss, which sets dismissed true and saves NBT, NOT delete record. Handbook promises deceased/dismissed recall; no explicit health reset when loading saved NBT, actual dead-cat revival unresolved (do not claim permanent loss or guaranteed resurrection). Owner wants pet cat; never require death as quest objective. Broom entity acceleration.02 max speed1.0, climb/steer, mount, no mayfly, no ongoing AP/durability cost established; damage/destruction boat-like >40 accumulated damage. 
Curses saved globally by UUID; offline application from identity possible, online tick effects, persistent no finite timer; reapply max strength, no stack; Cat gives stronger variant regardless proximity. Clumsy configured .02/level drop chance, Misfortune tag-defined random harmful effects nominal120–300sec, Overheating nominal30–90sec hot biomes, Sinking penalizes swim/flight, whispers120–240sec. CurseInstanceImpl increments age twice per tick; effective nominal interval discrepancy, no game measurement or automatic death cure asserted. Remove marks pending, applies on target tick. Four distinct remove recipes; use guide and EMI. 
## Completion and downstream limits
Native research complete, exact JAR and prior source, no code/config/quest/build changes. Existing placeholder chapter separate. Owner decision keep both, no material equivalence approval. Native QA concerns: remote shelf persistence and death/totem ordering, curse cadence and respawn, actual deceased familiar health, Waystone duplicate mismatch, claim/teleport and barrier cleanup, ore attribution, guide unfinished Sprouting/Cauldron/infusion and missing familiar chapter link, actual effective config and EMI special recipe support. No gameplay acceptance required for research close. 
## All 29 capability axes
| Axis | State | Native scope |
|---|---|---|
| gear | present | Arthana, witch hunter gear, broom, poppets |
| movement | present | broom, cross-dimension transposition |
| ore_processing | present | Broiling actual smelting, Transpose Iron extraction |
| storage | present | four-slot persistent poppet shelf, not bulk item |
| crafting | present | Oven, Kettle, Distillery, Wheel, rites |
| logistics | present | bound location/entity transposition, talisman, remote poppets |
| buffs | present | Depths/Grotesque, protective effects |
| external_compatibility | present | Modopedia, tags, recipe interfaces; no JAWR bridge proven |
| mob_farming | present | Love/Fertility, imprisonment, existing entity summon |
| ore_generation | no_capability_confirmed | Transpose Iron removes actual ore, no new ore |
| permanent_power | present | persistent familiar binding and curse, no personal infusions |
| general_generation | present | plant mutations, tree growth, processing outputs |
| direct_damage | present | Sky Wrath, curse, Voodoo, mob defense |
| block_breaking | present | Transpose Iron, chalk removal |
| permanent_pets | present | cat familiar |
| temporary_pets | no_capability_confirmed | summon existing entity not owned creation |
| defense | present | remote poppets, circles, gear |
| healing_recovery | present | fatal protection, equipment repair, fertility cure |
| debuff_control | present | curse removal/curse, Grotesque disguise |
| energy | present | spatial Altar Power and charged stones |
| automation_control | present | powered machines and sustained rites |
| agriculture | present | herbology, mutagens, breeding, Fertility/Forest |
| detection_information | present | identity binding, altar reading; no general radar proven |
| equipment_augmentation | present | broom infusion, repair poppets, upgraded variants |
| world_manipulation | present | protection blocks, trees, time, iron, mutations |
| world_content | present | plants, mandrake, configured trees, familiar entity |
| death_respawn | present | damage-specific death protection, intended familiar recall with caveat |
| trade_economy | no_capability_confirmed | multiplayer services possible, no merchant system established |
| progression_tiers | present | material/altar/circle and poppet tiers; no linear final boss |
## Quest-source map — 37 representative teaching slices
Not fixed FTB quest count. Editorial dependencies not new hard runtime gates. Use observed operation, verified native advancement or manual confirmation; acquisition proxy only. Actual recipe patterns and rite items via Art of Witchcraft/EMI. No compulsory harm to other players or pets; positive XP and safe rewards, no rare-material gate bypass.
| ID | Previous | Kind | Action / success | Reference (guide section) |
|---|---|---|---|---|
| EN-01 | — | Core | Obtain Belladonna Seeds, craft Art of Witchcraft and open Getting Started. | Getting Started |
| EN-02 | EN-01 | Core | Grow witch herbs, harvest Mandrake safely with earmuffs/night, obtain root. | Herbology, Mandrakes |
| EN-03 | EN-02 | Core | Craft Witch Oven and jars, collect Exhale/Breath/Hint, preserve ash. | Fume Collection |
| EN-04 | EN-03 | Optional | Upgrade oven funnels and filters, compare fume collection chance. | Fume Collection |
| EN-05 | EN-03 | Core | Build Kettle over heat, cook free Mutandis ordered recipe. | Creating Brews |
| EN-06 | EN-05 | Core | Use Mutandis on saplings near appropriate mutagens, wait for transformation; grow Rowan/Hawthorn/Alder. | Mutated Plants |
| EN-07 | EN-06 | Core | Craft six Altar blocks, form 3x2 altar, examine capacity and recharge. | Altars |
| EN-08 | EN-07 | Core | Decorate altar with distinct natural blocks and upgrades; provide excess AP beyond cost. | Altars |
| EN-09 | EN-07, EN-03 | Core | Use Distillery to create Tear, Whiff, Diamond Vapour, Demonic Blood and other needed ingredients. | Distillation |
| EN-10 | EN-08, EN-09 | Optional | Expand capacity over7000; brew Extremis and mutate eligible mushrooms into Blood Poppy; other moss/vine mutations. | Mutated Plants |
| EN-11 | EN-05, EN-09 | Optional | Make Love/Depths brew, use on animals or self; Sprouting is a recipe component not finished combat effect. | Brewing |
| EN-12 | EN-10 | Core | Brew Drop of Luck and Redstone Soup; choose advanced reagent branch. | Brewing |
| EN-13 | EN-02 | Optional | Craft Arthana, acquire rare mob ingredients; use EMI and book loot references. | Arthana |
| EN-14 | EN-09, EN-07 | Core | Craft ritual chalk then Golden Chalk; learn 7/11/15 ring patterns and heart. | Fundamental Theory |
| EN-15 | EN-14 | Core | Perform Charging Stone rite, observe charged stone and use it as substitute power in specified rite. | Charging Stone |
| EN-16 | EN-14 | Optional | Bind Talisman to existing circle, relocate its chalk to unobstructed terrain. | Talisman |
| EN-17 | EN-12, EN-13, EN-14 | Optional | Brew Flying Ointment, infuse broom, practice low-altitude controlled flight. | Broomsticks |
| EN-18 | EN-08, EN-14 | Optional | Tame cat; build altar with >8000 AP, bind familiar in medium circle. | Familiars |
| EN-19 | EN-18 | Optional | Recall familiar to circle, including across dimensions; do not require killing pet. | Summon Familiar |
| EN-20 | EN-09, EN-07 | Core | Craft Spinning Wheel, make base/sturdy/infused poppets using appropriate materials. | Poppetry, Spinning |
| EN-21 | EN-20 | Core | Collect target identity with Taglock, bind protective poppet to consenting player present. | Taglocks, Poppetry |
| EN-22 | EN-21 | Optional | Prepare ordinary/sturdy/infused damage-type protection, demonstrate in safe test. | Poppets |
| EN-23 | EN-21 | Optional | Prepare tool/armor repair poppet; use before equipment destruction. | Poppets |
| EN-24 | EN-21, EN-15 | Optional | Build Poppet Shelf, store correctly bound poppets, demonstrate remote protection. | Poppet Shelf |
| EN-25 | EN-14, EN-08 | Optional | Cast Protection or Large Protection and maintain AP supply; stop and verify cleanup. | Protection |
| EN-26 | EN-14, EN-08 | Optional | Use Imprisonment/Sanctity with mobs and clear safe geometry. | Creature rites |
| EN-27 | EN-14, EN-15 | Optional | Create bound Waystone; transpose caster to safe location; charged alternative. | Waystone, Transpose Caster |
| EN-28 | EN-27, EN-21 | Optional | Create Blooded Waystone and summon consenting player or eligible mob; observe loaded-target constraint. | Blooded Waystone, Summon Entity |
| EN-29 | EN-25, EN-27, EN-15 | Optional | Use remote protection or temporary barrier at safe controlled destination. | Protection |
| EN-30 | EN-14, EN-11 | Optional | Use Fertility rite for crops/animals; optionally Forest on valid terrain with Sprouting reagent. | World rites |
| EN-31 | EN-14, EN-15 | Optional | Try Broiling/Total Eclipse or Sky Wrath on controlled target/area; note charged alternatives. | World rites |
| EN-32 | EN-14, EN-15, EN-09 | Optional | Create Otherwhere Chalk and execute Transpose Iron on permitted terrain; observe extracted ore and depleted source. | Transpose Iron |
| EN-33 | EN-10, EN-14, EN-21 | Optional | Brew Grotesque and Nether Chalk; demonstrate a curse only with consenting test player; remove curse. | Curses |
| EN-34 | EN-33, EN-18 | Optional | Compare ordinary and familiar-strength curse; removal and persisted state, not mandatory repeated attacks. | Curses |
| EN-35 | EN-20, EN-33 | Optional | Craft Voodoo Poppet and protection, observe protection on consenting test target; distinguish direct curse. | Poppetry |
| EN-36 | EN-12, EN-15, EN-14 | Optional | Brew advanced intermediates Happenstance/Mystic/Soul/Spirit for actual recipes; no invented personal infusion path. | Brewing |
| EN-37 | EN-18, EN-24, EN-25 | Endpoint | Demonstrate a familiar and protective witchcraft workshop; optional ritual specialization, no native final boss. | Art of Witchcraft |
