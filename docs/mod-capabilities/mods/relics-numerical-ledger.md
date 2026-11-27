# Exact Relics 0.12.9 numerical ability ledger

Atlas #410 / #799, 2026-11-27. Exact SHA1 6b17e3a3292bf678415b4763cf830df1d01ebc33. 20 registered native relic classes, 39 ability templates, 133 numeric stat templates extracted from javap -p -c constructDefaultRelicTemplate. This is a constructor ledger, not a claim of measured output. Stat format first initial endpoint, second (quality10) endpoint -> target; scaling M=multiplicative base, A=additive, L=logarithmic, R=radical, E=exponential. Descending beneficial rolls intentional; raw fractions e.g. .25 mean 25% where handler uses ratio, not universal units. Template defaults ability cap10, point cost1, required relic level/rank0, rank cap5, rank scaling .25, base XP cost100 step100. Rank modifiers listed as rank:name. Actual use requires unlock/research/activation and handler constraints. Do not mistake rank scaling target denominator for additional purchasable levels. Exact runtime thresholds, probability clamps, integer casts and conditional multipliers can change displayed outputs. See companion [family dossier](artifact-family.md).

| Relic / acquisition category (native) | Ability / exceptions / rank modifiers | Stat values (initial endpoints -> model:target) | Named XP sources |
|---|---|---|---|
| Leafy Mantle / forest,tropic | camouflage; 3 absorption,5 disappearance | heal .1,.25>L:2.50009; absorption 1,3>L:19.9995; cooldown 15,10>L:4.9985 | hiding, healing |
| Leafy Mantle | revival; lvl5; 1 piercing | radius 5,10>E:24.97833; heal .5,1>L:4.99992; damage .5,1>L:4.99992; paralysis .5,1>L:4.99992 | consuming_leaves, leaves_impact |
| Springy Boot / mountain | bounce; 1 disappearance,3 strike,5 shockwave | power .5,.75>L:1.99993; damage_modifier .05,.15>M:.50017; radius 1,3>L:10.00005; damage 2.5,5>M:30.0075; stun .25,.5>M:4.99925 | bounce, strike, create_shockwave, shockwave_hit |
| Kinetic Belt / End,End-like | slots; cap5,2 points, rank scaling .1 | amount 1,2>A:12 | none |
| Kinetic Belt | gliding; 1 momentum,3 strike,5 resistance | efficiency .25,.35>M:1.00048; damage .1,.25>M:2.49963; resistance .05,.15>M:.75008 | gliding,strike,resistance |
| Reflective Necklace / Nether-like,Nether | reflection; 1 stun,3 piercing,5 bounce | chance .1,.25>R:1.00016; damage .25,.5>L:2.49996; lifetime 5,10>E:60.04761; piercings 1,3>M:19.9995; stun .25,.5>M:2.50025; bounces 1,2>M:49.999 | construct,impact |
| Jellyfish Necklace / aquatic | regeneration; 3 retention | max_health .1,.2>L:1.5001; regeneration .15,.35>L:1.99985 | health_regeneration |
| Jellyfish Necklace | shock; 1 conductor,5 charge | cooldown 120,60>L:9.99988; rings 1,2>A:10.001; radius .25,.5>R:4.99977; knockback .25,.5>E:2.50355; paralysis 1,2.5>E:9.9989; distance 2.5,5>L:19.99989; bounces 1,3>M:24.9975; damage 2.5,5>L:19.99989; damage_modifier .25,.5>E:4.99993; duration 2.5,5>R:30.00017 | rings_accumulating,arcs_bouncing,hit_paralysis |
| Midnight Mantle / End,End-like | phase; 1 switch; relic max rank7 | attack_damage .25,.5>M:2.50008; attack_speed .25,.5>M:2.50008; max_health .25,.5>M:1.4999; health_regeneration .25,.5>M:1.4999; duration 5,10>M:120; modifier .1,.25>M:1.50001 | health_regeneration,damage_dealing |
| Midnight Mantle | invisibility; lvl5; 3 strike | brightness .1,.25>M:.74995; cooldown 15,10>M:1.002; damage .25,.5>M:2.50008 | being_invisible,damage_dealing |
| Midnight Mantle | constellation; lvl10; 5 stun | star_chance .1,.2>L:.90001; constellation_radius 5,7>M:25.018; tremor_duration .25,1>M:9.998; explosion_radius .5,1>M:7.501; explosion_damage 1,5>M:49.99; star_lifetime 10,15>M:120.0225; stun_duration 1,2.5>L:10.00004 | star_creation,star_tremor,star_damage |
| Midnight Mantle | starfall; lvl15; 7 bounce | chance .1,.25>M:.7505; radius 1,2>M:9.997; damage .25,.5>M:4.999; stun .25,.5>M:10.00125; bounce_chance .05,.15>M:.4998 | star_creation,shockwave_hit,star_bounce |
| Roller Skate / Overworld | skating; 1 step_height,3 resistance,5 sparkling | speed .1,.25>L:2.50009; step_height .6,1>M:2.9985; resistance .1,.25>M:.99988; damage 1,2.5>M:9.99875; ignite 1,2>M:10.001 | skating,resisting_damage,creating_sparks,spark_hit |
| Chorus Staff / End-like,End | blink; 1 flicker,3 safe_fall,5 ascent | distance 10,15>M:30; max_charge 3,5>A:40; cooldown 30,15>M:5 | blink,flicker,safe_fall |
| Clot of Time / End,End-like | rewind; 1 invulnerability,3 oblivion,5 health_rewind | time 3,5>M:30.0075 | rewind,health_rewind |
| Piglin Mask / bastion | neutrality; cap0; 1 legion | none | target |
| Piglin Mask | barter; 3 pocket | trades 1,3>A:11.75; items_count 1,4>M:64.004 | trade,pickup |
| Piglin Mask | looting; lvl5; 5 frenzy | chance .1,.15>M:.75008; health 10,7.5>M:2.5125; duration 5,10>L:29.99998; attack_damage .005,.01>M:.02999; attack_speed .005,.01>M:.06999 | drop,stack |
| Chef Hat / village | satiety; 1 quick_meal,3 meatball_healing,5 cooked_meatball | chance .1,.15>M:.75008; healing 1,2>M:10.001 | drop,consume,healing |
| Cut Glass Boot / aquatic | glass; max rank0 | capacity 1,5>M:50; max_fluids 1,2>M:10; speed .01,.05>M:.1 | standing |
| Rider Flute / village,cave | stable; cap5,rank scaling .1; 1 regeneration,3 recall,5 resistance | max_slots 1,2>M:4.286; heal .05,.1>M:.5; resistance .05,.1>M:.5 | riding,healing,recall,resistance |
| Seven Sins Ring / Nether,Nether-like (TMM banned) | pride; rank0 max | multiplier .025,.05>M:.1 | height_advantage |
| Seven Sins Ring | envy | outgoing_damage_multiplier .005,.015>M:.025; incoming_damage_multiplier .75,.5>M:.25 | health_gap |
| Seven Sins Ring | wrath | window 1,2>M:5; early_multiplier .1,.25>M:1; late_multiplier 1,.75>M:.25 | timing |
| Seven Sins Ring | sloth | time 10,7.5>M:.9975; speed 1,.75>M:.24975 | immortality |
| Seven Sins Ring | greed | luck 1,3>A:10; looting 1,3>A:10; chance .1,.075>M:.00998 | ore,mob,nullification |
| Seven Sins Ring | gluttony | early_multiplier .0025,.005>M:.1; late_multiplier .1,.05>M:.01 | food |
| Seven Sins Ring | lust | amount 1,3>A:13; time 360,420>A:720 | offspring |
| Sphere of Self-Sacrifice / Nether,Nether-like | sacrifice; 1 resistance,3 salvo,5 salvation | stacks 1,2>L:9.99985; resistance .01,.025>M:.05003 | self_inflicted,resisting_damage,salvation |
| Hunting Belt / village | slots; cap5,2 points,rank scaling .1 | amount 1,2>A:12 | none |
| Hunting Belt | pack; 1 leader,3 relentless,5 revival | damage_modifier .25,.5>M:3.00075; pet_radius 3,5>M:14.9925; resistance_per_pet .01,.05>M:.10005; revival_cost 10,7.5>M:1.00312 | pet_damage,pet_resistance,bone_drop |
| Experience Disperser / wildcard | dispersion; 1 same_item,3 player_xp,5 max_level | distribution_ratio .05,.15>M:.99997; same_item_bonus .15,.25>M:.99988; player_xp_ratio .025,.05>M:.49993 | trigger |
| Glitchy Mantle / End-like,End | distortion; 1 disorientation | miss_chance .1,.25>M:.5 | miss |
| Glitchy Mantle | illusion; lvl5; 3 echo | interval 15,10>M:2.5; duration 2.5,5>M:10; stun .5,1>M:5; stun_radius 1,3>M:5; echo_radius .25,.5>M:2.5 | creation,detonation,echo_attack |
| Glitchy Mantle | glitch; lvl10; 5 phase | stability_time 1,3>M:10; fall_damage 2.5,2>M:.25; phase_time 2.5,5>M:10 (threshold 2.5–10) | air_walking,phasing |
| Ghostly Mantle / Nether,Nether-like | fog; 1 frostbite | duration 3,5>M:15; radius .5,1>M:2.5; air_loss 1,2>M:10; suffocation_damage .1,.25>M:1; tremor 1,2>M:5 | fog_creation,fog_exposure |
| Ghostly Mantle | gaze; lvl5; 3 dread | range 5,10>M:30; charges 1,3>M:10; weakness .005,.01>M:.075; damage .01,.025>M:.15 | eye_contact |
| Ghostly Mantle | spectral_escape; lvl10; 5 reprisal | cooldown 125,100>M:25; duration 1,3>M:10; damage .5,1>M:10 | spectral_form,death_prevention,reprisal_damage |
| Shield of Retaliation / Nether-like,Nether | retaliation; 1 projectile,3 stun,5 guard | window .5,.75>M:1.5; miss_damage 1,.75>M:.1; miss_duration 5,3.5>M:1; cooldown 1.5,1>M:.25; projectile_damage .25,.5>M:2.5; extension .5,.75>M:2.5; stun_radius 1,2.5>M:5; stun .5,1>M:2.5 | block,stun |

Relic-specific XP cost step200 (initial100): Leafy Mantle, Reflective Necklace, Jellyfish Necklace, Roller Skate, Chorus Staff, Piglin Mask uses step100, Cut Glass Boot, Seven Sins Ring, Sphere, Shield. Experience Disperser step150. Midnight Mantle initial1000 step100 rank7. All others initial100 step100. Piglin Mask explicitly step100 (not step200). Ring and Cut Glass Boot max rank0. Exact default relic cap from template point costs, sum per ability. Ring70 points, no rankups; Midnight Mantle40 per rank with 7 available rankups; belt20 points per rank (slots 5x2 plus gliding/pack10); Rider Flute5; Piglin Mask20. Raw player XP for an ability upgraded 0→10 = 2750 (50+100+...+500), separate from relic-specific experience. Rankup resets level and points and ability levels, not automatic max stat.
 
Loot category names are actual LootEntries predicates, not guaranteed individual structure outcomes. For exact acquisition scheme and exceptions see main dossier. No ring entitlement or TMM balance change.
