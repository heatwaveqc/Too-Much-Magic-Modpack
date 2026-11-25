# Unicorn Academy: Bonded Magic — exact-pin Atlas pass 1 (2026-11-25)
Status: needs-research; research only, not quest-source-ready. Chapter: But I want to be a horse girl. Parent #410 / intake #643. Independent equestrian chapter research, unique paths, no edits to other active Atlas writers' index or dossiers.
## Evidence
Owner-supplied unicornacademy-1.0.0.jar, SHA1 b989b1dfec0d4c1360f52682adfa4f2196c38c50 matches test/full-integration mods/unicorn-academy-bonded-magic-unofficial.pw.toml; 860 ZIP entries, CRC pass. SWEM 1.6.14 SHA1 55ba0684e7bd852ba298a666595ac9c9f83977af also matches. Java bytecode via javap -p -c -constants; bundled JSON and en_us locale. Native defaults, not effective pack config. No JAR committed.
## Progression, resource and operation
UnicornEntity extends SWEM LegacySwemHorse. BondBook/Record per player UUID per unicorn, XP to next = 50*(level+1), cap10, awakening5 (750 total), wings8 (1800), max10 (2750). UAConfig: ride 2/16 blocks; new ridden chunk 5; feed8/200t; brush10/400t. Sky berries restore floor(maxEnergy/4), heal2 HP, feed XP16 subject to cooldown; consume berry in survival. Magic max100, regen2/sec after awakening. Ability.energyCost discounts 4% per level beyond base min capped25%, rounded, min1; targeted overrides exist. No FE/EMC bridge found. UnicornEntity.tryUseAbility checks controlling passenger and server world, rider's bond and unlocked ability, cost and cooldown. Saved tags UnicornWild, UnicornBonds, UnicornEnergy, UnicornCooldowns, UnicornGem, UnicornWingsOut. Automatic defense is separate from rider casting; highest bond and defense eligibility used. SWEM training is separate.
WildUnicorns: common60/uncommon30/rare10 weights; ability slots 3/4/5 limited by pool, schedules 5/7/9, 5/6/8/10, 5/6/7/9/10. Wild roll selects at least one level-5 ability if available, other random powers, sorted then assigned schedule. Canonical chance config .03. Wild element concealed until awakening. Element weights plant22 water22 light16 ice14 fire14 weather8 (relative, not percentages). 
## Complete ability registration census
Each ID is unicornacademy:<path>. Columns: canonical base bond, energy, cooldown ticks. Source: magic.Abilities, magic.abilities.*Ability and unicorn.UnicornDefinitions. Effects from bundled locale, specific numerical magnitudes pending pass 2.
| Element | Path | Bond | Energy | CD ticks | Function |
|---|---|---:|---:|---:|---|
| light | rainbow_bridge | 5 | 35 | 160 | variable bridge |
| light | light_flash | 5 | 30 | 240 | dazzle, slow, undead damage |
| light | guiding_light | 5 | 15 | 40 | illumination |
| light | light_blast | 6 | 20 | 40 | beam damage |
| light | light_shield | 7 | 45 | 400 | reflection, repel |
| light | rainbow_vision | 8 | 40 | 400 | ore, chest, track, entity reveal |
| light | rainbow_starfall | 10 | 70 | 600 | damage and ally healing |
| plant | vine_manipulation | 5 | 25 | 120 | restrain, fetch, carry |
| plant | plant_growth | 5 | 25 | 100 | native bonemeal interface |
| plant | plant_resizing | 6 | 20 | 80 | giant plants, shrink obstacles |
| plant | vine_barrier | 7 | 35 | 300 | temporary wall |
| plant | butterfly_vision | 8 | 40 | 300 | remote scouting |
| ice | ground_freeze | 5 | 30 | 160 | freeze terrain |
| ice | ice_blast | 5 | 20 | 30 | chill projectile |
| ice | ice_wall | 6 | 35 | 240 | solid wall |
| ice | ice_slide | 7 | 25 | 100 | transient path |
| ice | time_freeze | 8 | 40 | 300 | entity immobilization |
| water | water_creation | 5 | 20 | 100 | water, fill containers |
| water | water_manipulation | 5 | 22 | 50 | gather/throw orb |
| water | water_shield | 7 | 40 | 400 | protection |
| water | bubble | 8 | 35 | 600 | underwater/aerial movement |
| fire | fireball | 5 | 22 | 35 | projectile |
| fire | heated_hooves | 5 | 25 | 200 | melt, scorch |
| fire | fire_wall | 6 | 35 | 260 | flame barrier |
| fire | fire_pillar | 7 | 40 | 300 | launch enemies |
| fire | fiery_speed | 8 | 40 | 600 | speed |
| weather | lightning_strike | 5 | 30 | 60 | lightning/stun |
| weather | weather_manipulation | 5 | 35 | 400 | local storm cloud |
| weather | tornado | 6 | 40 | 360 | entity/item displacement |
| weather | thunder_shockwave | 7 | 35 | 240 | area stun |
| weather | flying_lightning | 8 | 50 | 900 | flight |
Canonical definitions Wildstar/light, Leaf/plant, Glacier/ice, River/water, Cinder/fire, Storm/weather. Target-dependent bridge overrides; default max length96 at bond10, width3, lifetime15s. Fireball block ignition false default, damage multiplier1. 
## Acquisition and dimension
Fairy Gemstone unicornacademy:fairy_gemstone, default bond8; dedicated slot, consumes item, grants wings. No bundled recipe, loot/advancement JSON or other data reference; class references only commands, SelfTest, menu, entity, registration and creative tab. Effective TMM recipe/loot/shop audit pending. Do not award or change recipe without design approval.
Dimension unicornacademy:unicorn_island, 10 biomes. Herd feature in Academy Meadows, Ember Highlands, Skyberry Thickets, Unicorn Wildwood, Whispering Willows; berries in meadows/thickets/wildwood. FerryFeature permits both Overworld and Island; biome modifier #unicornacademy:ferry_waters delegates to vanilla beach/river/ocean tags; island biome inclusion in those tags not established (no explicit ferry feature in ten biome JSONs). Native ferry config 640 block spacing, 45% chance, destination search128, island scale .25. FerryTravel.prepare registers/link partner if absent and safe dock found, refuses if no landing, no bond requirement observed in prepare. Naked Angel arrival return remains unresolved because availability of naturally generated island-side ferries is uncertain. #387/#392: approved seventh destination, no config changes.
## 29 axis crosswalk (first pass, unknown != absent)
gear: gemstone and inherited tack; movement: bridge, ice slide, bubble, flying lightning, fairy wings, speed, ferry; ore_processing: none identified; storage: inherited SWEM inventory, gem slot not general storage; crafting: no bundled recipes, acquisition audit pending; logistics: vine fetch, tornado displacement, no network identified; buffs: guiding light, fiery speed, bubble; external_compatibility: native SWEM subclass, vanilla bonemeal, TMM claim/flight/taxonomy hooks unknown; mob_farming: herd/breeding inherited, farming output not established; ore_generation: ambient SWEM features only, no renewable generator established; permanent_power: per-pair bond, entity wings; general_generation: water creation limited, no open-ended generator found; direct_damage: named spell table, detailed damage pending; block_breaking: resizing/hooves environmental removal, harvest semantics pending; permanent_pets: unicorn, tame/death/ownership delegated to SWEM needs trace; temporary_pets: butterfly scouting, duration/controls pending; defense: shields/walls; healing_recovery: berry 2HP, starfall allies; debuff_control: flash, vines, freeze, lightning, tornado; energy: entity store, no block/item/player network identified; automation_control: autonomous defense, no programmable automation identified; agriculture: berries and plant growth; detection_information: rainbow vision, butterfly, bond/magic UI; equipment_augmentation: gemstone and wings; world_manipulation: temporary terrain, water, crops, localized storm; world_content: 10 biomes, herd, berry, ferry; death_respawn: unknown inherited behavior, no revival established; trade_economy: no native merchant found; progression_tiers: 0–10 bond and separate SWEM progression.
## Proposed research-only quest map
UA01 find/board ferry (actual ride, no possession proxy); UA02 visit Island (dimension signal; UA01 optional alternate entry); UA03 discover herd (entity encounter; UA02); UA04 tame unicorn (owner/UUID state; UA03); UA05 equip tack (SWEM requirements pending; UA04); UA06 groom (actual interaction; UA04); UA07 feed berry (actual feed; UA04); UA08 explore (bond XP/visited-chunk event; UA05); UA09 awaken bond5 (read native bond record; UA04); UA10 cast unlocked ability (successful server cast; UA09); UA11–16 optional light/plant/ice/water/fire/weather demonstrations (correct unlocked spell; UA10); UA17 bond8 (UA09); UA18 obtain gemstone (survival route unresolved, optional design hold; UA17); UA19 equip wings and fly (UA18); UA20 max bond10 (UA09); UA21 return ferry (UA02). No mandatory collect-all requirement, no FTB task IDs. Native detector availability not yet established; possession never proves spell/bond.
## Next pass
Trace exact damage, ranges, durations, block changes/rollback, protection and cost/cooldown overrides; SWEM survival taming, tack, inheritance, death; native ferry generation in island-side biome tags and safe exit, effective TMM gemstone route, quest observation hook vs proxy. No game boot or routine native gameplay tests required. Targeted pack interactions only where static evidence leaves real ambiguity. No gameplay/FTB/pin edits.
