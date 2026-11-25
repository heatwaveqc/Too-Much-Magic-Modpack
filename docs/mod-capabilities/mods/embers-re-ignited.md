# Embers Re-Ignited 1.21.1-1.5.9 — native capability Atlas

**Research COMPLETE at native quest-source depth; no gameplay acceptance.** Dwarven magical engineering, own Ember power, metallurgy, alchemical codebreaking, renewable metal seeds, equipment Heat/augmentation and automation. Teach with Ancient Codex + EMI + quests, not a replacement manual.

## Evidence and boundaries
[Exact pin](../../../mods/embers-re-ignited.pw.toml) Modrinth 8znVCTxA/2r51o0lC, owner-supplied embers-reignited-1.21.1-1.5.9.jar 15,894,849 bytes, SHA512 7e0f7b72f6c3516f28f3fc4b7539088c1ae320d557a0c80fb01dd80d5479ce504f4ff2a8bedf2a8f90bae9d8445baabe0fd887aef4afd61efa2d239fffcbf361, SHA1 ca93a7caa9a0dae23b2244f76f5d27f7d31b4681, CRC PASS reconfirmed,4213 unique entries870 classes. Actual mod ID embers, MC1.21.1 NeoForge>=21.1.228, MIT. [Aligned source](https://github.com/Riieno/EmbersRekindled-Reignited/tree/82c263a2af72ef320ef2593a942586424f0026cf); exact binary targeted javap, native JSON, English Codex and readable source; no execution/full source-bytecode equivalence. 630 recipes:182 shaped,129 melting,78 stamping,56 stonecutting,35 alchemy,29 smelting,21 blasting,20 shapeless,15 tool augment,14 metal coefficient,8 mixing,6 boring,6 catalysis/combustion,6 Create pressing,4 Create deploying,4 activation,3 excavation,2 boiling,2 Create splashing,2 gas fuel,1 each dynamic seed,Create crushing,augment removal,breakdown,materia repair,repair,gem socket,gem unsocket. 302 adv,258 tags,160 loot tables,8 worldgen,6 Curios,4 loot modifiers,1 damage type,1 dynamic seed blacklist,1 NeoForge JSON;1512 en_us keys,514 research localization keys,111 named ResearchBase static fields not exact active entry count. Prior [profile](../../mod-comprehension/mods/embers-re-ignited.md) superseded for native evidence. [First pass](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/410#issuecomment-7150791952), [second pass](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/410#issuecomment-7150950798), [third pass](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/410#issuecomment-7151416207) preserve detailed paths and constants. Optional Create, createthrusters, Sable, Aeronautics, Curios and Jade; native operation does not depend on Create. No missing dependency addition, no JAR committed.

## Entry and Codex graph
Ancient Golem drops4–11 archaic bricks (+looting),1 motive core; Codex core+4 bricks. Ruins Overworld cave, spacing7/separation1 start height -10/ocean floor projection max above_bottom128, golem spawn override1/1 weight20, pools copper2 iron2 gold1 lead2 silver1. Lead ore count8 Y-28..28, silver4 bottom..bottom+64. Hammer iron4+lead+sticks2; early iron plate hand recipe2 ingots+hammer tag (not circular Melter gate). Caminite and Mechanical Core→Bore. Atmospheric Gauge early site survey; advanced Field Chart recipe8 archaic bricks+Crystal Cluster (alchemy). Tinker Lens indicates machine face capabilities, flow/links. 
Codex config codexProgression=true, per-player checkmarks manually sent by MessageResearchTick; no hard craft/machine lock found. World initial; Mechanisms requires activator; Metallurgy dawnstone; Alchemy alchemy; Smithing wildfire; Materia/Core ComingSoon categories, not all corresponding items unavailable. Main nodes ores→hammer/gauge; caminite→access; hammer+access→bore→crystals/lens→activator. Mechanisms pipes and emitters→melter→stamper→mixer→dawnstone, pressure refinery→pump→boiler; cell→hearth. Ancestors check ALL, entry itself checked bypass. Manual quests supplement, not replace guide.

## Ember extraction and power
Bore caminite bricks4+copper+mechanical core+3iron, eligible Overworld Y<=-57, Nether<=7,3 world_bottom blocks (bedrock), actual boring recipe. Fuel3 burn units/t,200t dig, one random roll vs density or explicit recipe chance; weighted eligible output crystal20/shard60/grit20. Not guaranteed every dig. Field seeded density/stability and shared shifting offsets; no per-location spent reserve in bore code. Excavation Buckets additional gravel/flint/snow outputs. Activator6 copper+2 iron plates+furnace, lower input upper buffer, item activation:
| Input | Ember |
|---|---:|
|grit|0|
|shard|400|
|crystal|2400|
|cluster|4400|
Heat Exchanger .9×output+300: grit300, shard660, crystal2460, cluster4260. Crystal stamp→6 shards, shard→grit; not perpetual energy. 
Pressure Refinery20t threshold, water25mB/cycle, tank8000/output32000. Supporting metal coefficient C, n horizontal heat sources surrounding block underneath (0..4), multiplier1.25+.25*n*(C-1.25), unrecognized block1.25 (guide1.5 inaccurate). Coefficients gold/silver/electrum/pristine copper3, aluminum/nickel/tin2.85, iron/lead/bronze2.625, exposed copper2.5, weathered2, oxidized/dawnstone1.5. 
Ignem Reactor128k cap, threshold20t, four adjacent below chambers, fuel/catalyst each400t: coal/grit+2, nether brick/gunpowder+3, blaze powder/glowstone+4. Strict max(total combustion,total catalysis)<2*min; multiplier1+both sums, one high pair9×, two17× before upgrades. Requires both active and output headroom. 
Network link source then receiver via Hammer, redstone default emission. Emitter200 buffer/pull10/t/40 packet; Ejector2000/pull100/t/400 packet; Receiver2000/32 transfer/t, overflow loss. Packet lifetime80t, relay forwards and mirror reflects, splitter divides into2 independent outputs, Funnel2000/100 transfer/t. No guarantee arbitrary loaded/unloaded distance or lossless. Copper Cell24000 retains Ember on break (guide); Crystal Cell64000 initial to1440000 cap, consumes activation item for10× its value in capacity. Charger24000/10 per tick, Siphon reverses. Jar2000 carried inventory; Cartridge6000 hand; Bulb1000; held cells→Curios→inventory drain order. Native Ember Energy Converter100 FE/Ember, max1000FE/t, buffers2000 Ember/20000FE, rear output, recipe2 copper+2 caminite plates+receiver; no Create/reverse conversion established. 
## Metallurgy, heat and gas
Melter typical ore240mB, raw120, ingot90, nugget10, plate90, gear180, block810; direct ore2.67 ingots/raw1.33 before separator. Geologic Separator eligible ore20/raw10mB byproduct, tag precedence; exact recipes EMI, no blanket doubling. Stamper two blocks above Stamp Base with air gap, fluid input, stamp, Ember, output to Bin. Melter guide 200t/1 Ember per t, Stamper80 per70t, base1500mB; inspect effective config for balance, not universal recipe rates. Mixer separate tanks, top output; equal copper/gold Dawnstone, other conditional alloys brass/electrum/bronze/invar/constantan. 
Hearth Coil max280 heat, rise/fall1/t, cost1/t, cooking20–300t depending heat; Char Instiller/Atmospheric Bellows specialized mutually incompatible, Bellows blasting2×. Heat insulation and redstone/Clockwork Attenuator (active/inactive speed) controls. Mechanical Core provides side access and upgrade distance, count diminishing returns. 
Pump .5 Ember/t, progress400 base, source directly below, BucketPickup consumes source only if1000mB fits tank8000; spends on unsuccessful attempt too, no unconditional infinite water generation. Mini Boiler attaches as upgrade, heat from EmberEvent consumption or produced*.25 (not transfer), distance/count penalties; each tank16000, multiplier1. Boil clamp(int heat,1,input), water1→5 steam, Dwarven Oil1→1 gas. Full gas tank enabled rupture strength3 ExplosionInteraction.NONE, removes boiler,12 damaging projectiles; keep gas clear. Catalytic Plug steam nominal2× speed each, two4×, farther/more penalties. Wildfire Stirling cost/multiplier, steam2/1mB per work tick, dwarven gas2.5/5 ticks, two steam quarter cost before distance/count penalties. Dwarven Oil from Soul Crude melted from synthesized sand:10 crude+5 gas→30 oil or5 crude+20 steam→10 oil. Actual crude input, not free loop. Mystical Mechanics rotation chapter optional/unavailable; no native SteamEngine class or standalone rotation network found, Create/Sable/Aeronautics conditional. 
## Alchemy and seed production
Cinder Plinth converts item to ash, blacklist, bin below; Ashen Amulet replaces block/mob drops with ash. Aspectus crafted and matched to input pairs around Exchange Tablet; Beam Cannon recipe4 copper plates+2 crystals+2 dawnstone+caminite bricks,2000 cap, rising redstone1000 threshold,25HP beam, also transfers to receivers/splitter. Tablet threshold1000, processing40t consume2/t; don't equate beam/tablet as universal additive cost. Random code uses Random(worldSeed-id.path.hashCode), aspect pool sampled with replacement for each input (config false cycles). Correct ingredient/aspect pairs unordered physically; black/glowing pins correct pair, pale correct aspect multiplicity wrong ingredient. Default failure consumes pedestal inputs and changes tablet to waste, configurable pedestal preservation. Slate holds7 waste, Mnemonic Inscriber paper records success. 35 ordinary+1 dynamic recipe. Cluster tablet crystal+gunpowder+3 shards, copper/iron/dawnstone aspects; Wildfire Core tablet motive core+2 dawnstone+cluster+copper plate, copper/iron/silver/dawnstone aspects. Motive core alchemy shard+3 bricks. Isolated Materia tablet iron+quartz/clay/lapis→4, copper/iron/lead/silver aspects. Exact other recipes EMI/guide, not assumed all special JEI transfer works in EMI.
Seeds initially available ruins; static seed alchemy copper/iron/gold/lead/silver/tin/zinc/nickel/aluminum conditional, dynamic seed tablet existing seed+3 ingots same metal, iron/silver aspects. Dynamic discovery requires nonempty c:ingots/<metal> AND c:nuggets/<metal>, excludes static/blacklist, default hardcoded+data netherite. Config true. Metadata metal/color. Injector1 Ember/t,24000 cap, distance1, speed adds injected amount separately from consumption. Each injected Ember adds growth+XP, fractional tracking; size>1000 harvest/reset0, no overshoot retained. Segment count6+floor(sqrt(level*2)) default, each independently1/3 chance drops one tagged nugget; no guaranteed yield. XP level floor((sqrt(5)*sqrt(xp+125)-25)/50), threshold1000*L*(L+1)/2. L0 expected2, L20 expected4, L100 expected6.67 nuggets/cycle; baseline50s; no free resource. Ruin seeds may already have XP210k..5.05m, size900; break static/dynamic doesn't preserve XP in inspected drop path (dynamic retains identity). 
**TMM Replica explicitly intentional:** startup_scripts/replica_nugget.js owner-approved comment, c:nuggets/replica.json and reversible ingot recipes in live repo, Eldritch Miner initial acquisition. Preserve intended seed, actual loaded ingot tag still verify, no assumption other gated metals qualify. No new blacklist approval.

## Equipment Heat, forging and repairs
Dawnstone Anvil bottom item/top material, hammer, configured max_hits40, actual progress>40 (41 from zero), idle expiry120t; Automatic Hammer12000 cap,40/action threshold20t, Bin output, not Create dependent. Repair with valid repair ingredient or Isolated Materia both fully restore durability, copy item data, damageable/damaged and separate blacklists; materia can repair absent standard ingredient, not guarantee every mod item. Breakdown damageable no-Heat item alone with repair ingredient, returns ONE preferred repair material, not full uncraft, does not require damaged state. Augment remove returns removable ingredients, core stays. 
Ancient Motive Core free permanent augment enables Heat (broad augmentables tools/melee/ranged/armor/projectile tags, armor slot fallback). Max heat500+250*current level; mining eligible hardness>0 or tag+1, melee event pre-mitigation max(1,.5*amount), armor taking damage+5 each, arrow loose+1, Ember projectile count*clampedLerp(.5,3,charge). Full Heat gear below level5 and positive crystal fuel in Inferno Forge, closed hatch after7t, 200t and16 Ember/t, cap32000, abort on shortage. Chance atan(crystalline activation value/(14400+14400*level))/(pi/2), upgrade modifier; 6 crystals50% at L0,30 at L4. Constant max crystal115200 not enforced (commented check); complete attempt consumes fuel, failure keeps Heat, success resets Heat and adds1 level, max5. Music disc special 7F Patterns. Counted augment total levels<=gear level, repeat augment spends slot; core/lens variants no slot, core not removable. Armor compatibility fallback ORs recipe with slot (some normal ingredient checks not guaranteed), highest armor augment level used, not sum. 
| Augment | Native capability |
|---|---|
|Blasting Core|cost2, randomized neighboring block explosion, melee/armor explosions, event hook; actual claims/MXP separate|
|Caster Orb|cost2, swing projectile, unlocks compatible projectile augments|
|Focal Lens / Diffraction Barrel|homing/piercing or spread-projectile transformations|
|Superheater|cost2, additional burning and auto-smelt drops (guide/events)|
|Resonating Bell|cost5, illuminate matching nearby blocks, common material cutoff|
|Cinder Jet|cost2, sprint-start ground dash, +.4 Y, level-scaled|
|Flame Barrier|cost2, retaliatory damage/fire|
|Eldritch Insignia|target cancel when unprovoked, entity ID mod(3+level)>=2, nonplayer; not universal boss pacification|
|Intelligent Apparatus|cost4, mob XP drop ×(level+1), max6× vanilla XP not direct MXP|
|Shifting Scales|passive +1/33t at rest, cap min(6*highest level,1.5*max HP), movement cooldown33t, damage99t, source rate/pass config; not permanent health|
|Winding Gears|wind tool, charge500 base, delay20t decay.25/t, speed/autoattack; boots jump/bounce when wound tool held|
|Tinker Lens / Smoky Lens|helmet info or disable info, no slot, guide|
## Weapons, armor and damage
Clockwork tools default no durability (config switch), 5 Ember selected tool didUse flag server tick, check powered every5t, no speed without power, ignite hit40t; same tick multiple actions not guaranteed independent cost. Pick doubles shovel, axe weapon, Grandhammer no drops (guide); enchantable. Blazing Ray cost25 cooldown10t full charge20t damage7 max spread30 distance96; Cinder Staff cost25 cooldown10t charge60t max damage17 AOE2.125 lifetime160t; native config, partial charge differs. Tyrfing alchemy lead sword+coal block+obsidian+2 lead, premitigation damage multiplier(4+armor)/4, armor20→6×, no bypass established. 
Ashen armor caps damage at max-1/broken state, not guaranteed fully functional broken. Held Inflictor Gem attunes damage type, blacklist, 35% matching each; holder+string+gems crafting within capacity, holder alone unsockets first with gem remainder. EmbersEvents subtracts cumulative resistances from1, cancels if<=0; three same-type gems potential immunity subject slots/effective tag/event pipeline. Amulet of Heretic only WITCH_RESISTANT_TO tag, >=.5HP reduces to max(.1*amount,.5HP); quarter-heart floor vs guide half-heart. Dawnstone Mail knockback protection guide. No native creative flight, Cinder Jet/winding bounded movement. 
## Item/fluid logistics and interfaces
Native lead item/iron fluid pipes adjacency, insertion only, corresponding extractor needs redstone default. ItemExtractor4 per work operation; FluidExtractor push240, max drain960 per work interval, simulated routing+buffer. Not per-tick rates. Hammer disconnect sides individually; stick unclog. Transfers front/back priority, filters and comparators, fluid transfer; vacuum range7 pull .45, no internal storage guide, dropper world output. Bin one stack64; vessel16000 portable fluid, reservoir40 buckets/ring/valve bottom port plus valve sides. Tinker Lens face/links; dials; Clockwork Attenuator redstone state settings; basic upgrade distance/count. Volatile Ember Conduit appears in guide/static research field but no initialized entry, registered class or native recipe found; do not teach as survival capability. 
## Downstream review and completion
Research only, no config/quest/build changes. Native gear repair and complete type resistance, XP/heat/claim/loot hooks, FE conversion and general in-mod logistics versus pack policy, gated metal seeds and actual loaded tags, furnace/ore/EMC ratios, alchemy puzzle and EMI support, steam rupture and packet load separate. Prior owner approved Replica seed; no new approvals. No gameplay QA prerequisite for native completion. Existing placeholder chapter remains unchanged. Exact numeric values not claimed effective server config. 
## All 29 capability axes
| Axis | State | Concrete scope |
|---|---|---|
| gear | present | Clockwork, Tyrfing, Ember weapons, armor, curios|
| movement | present | Cinder Jet, winding jump/bounce|
| ore_processing | present | melting, stamping, mixing, separator|
| storage | present | cells, portable Ember, bin, vessel, reservoir|
| crafting | present | metallurgy, alchemy, anvil, forge|
| logistics | present | packet network, pipes, filters, vacuum|
| buffs | present | augments, curio effects, scales|
| external_compatibility | present | Curios/Jade, FE, optional Create|
| mob_farming | present | beam/ash/automated damage, no spawner established|
| ore_generation | present | renewable metal nuggets via seeds (not raw ore blocks)|
| permanent_power | present | Heat level and augment slots, seed XP|
| general_generation | present | Ember extraction, FE, ash, seeds|
| direct_damage | present | weapons, beam, augments|
| block_breaking | present | Clockwork tools, Blasting Core|
| permanent_pets | no_capability_confirmed | golems wild encounters, intimidation not ownership|
| temporary_pets | no_capability_confirmed | no owned temporary companion established|
| defense | present | inflictor, amulets, scales, armor|
| healing_recovery | present | repair/scale recovery; no direct HP healing established|
| debuff_control | present | fire, retaliation, intimidation|
| energy | present | Ember field, activation, steam, FE|
| automation_control | present | redstone controls, upgrades, pumps, pipes|
| agriculture | no_capability_confirmed | metal seeds not ordinary crop agriculture|
| detection_information | present | gauge/chart, lens/dials, bell|
| equipment_augmentation | present | motive core/heat/forge/anvil, gem sockets|
| world_manipulation | present | bore, pump, ash/heat, blasting|
| world_content | present | ruin/golem, lead/silver and seed deposits|
| death_respawn | no_capability_confirmed | no resurrection or retention mechanic established|
| trade_economy | no_capability_confirmed | alchemy conversion not merchant system|
| progression_tiers | present | codex graph, metallurgy, wildfire, gear level5|
## Quest-source map — 46 representative teaching slices
Not fixed quest count, combine with actual Codex/EMI; editorial dependencies not new runtime gates. Use observable outcome, verified advancement or manual confirmation, acquisition only proxy. Ordinary recipes through EMI. Positive XP and safe rewards do not bypass native or pack gates.
| ID | Previous | Kind | Action / success | Reference (Codex page family or recipe) |
|---|---|---|---|---|
| EM-01 | — | Core | Locate Ancient Golem/ruin, obtain motive core and bricks, craft Ancient Codex. | ancient_golem, ores |
| EM-02 | EM-01 | Core | Collect lead/silver/copper, make Caminite and Tinker Hammer, hand craft iron plate. | ores, hammer, caminite |
| EM-03 | EM-02 | Core | Use Atmospheric Gauge, find eligible bedrock-contact Bore site. | gauge, access, bore |
| EM-04 | EM-03 | Core | Fuel Bore, collect crystals/shards/grit, examine output through Lens. | bore, crystals, tinker_lens |
| EM-05 | EM-04 | Core | Build Activator and emitter/receptor pair, link with Hammer, power and transfer Ember. | activator, emitters, linking |
| EM-06 | EM-05 | Core | Buffer in Copper Cell, read Ember Dial, verify ongoing transfer. | copper_cell, dials |
| EM-07 | EM-05 | Core | Use extractor and item/fluid pipes; learn face, redstone, connections and unclogging. | pipes, routing, valves, pipe_tools |
| EM-08 | EM-05, EM-07 | Core | Melt ore/raw/metal, pipe molten material into base, press with stamper/stamp, collect into bin. | melter, stamper, bin |
| EM-09 | EM-08 | Optional | Attach Geologic Separator; collect eligible byproduct. | geo_separator |
| EM-10 | EM-08 | Core | Mix molten copper and gold, stamp Dawnstone. | mixer, dawnstone |
| EM-11 | EM-10 | Optional | Build relay/mirror/splitter distribution; verify separately linked outputs. | relays, mirror_relay, splitter |
| EM-12 | EM-10 | Optional | Build larger Crystal Cell, feed activation fuel to grow capacity; compare with Copper Cell. | crystal_cell |
| EM-13 | EM-12 | Optional | Use Ejector and Funnel for higher throughput and buffer. | pulser, ember_funnel |
| EM-14 | EM-07 | Optional | Filter item/fluid transfer, use vacuum/dropper, vessel or expandable reservoir. | transfer, vacuum, dropper, tank, reservoir |
| EM-15 | EM-05 | Optional | Install Heat Exchanger and activate Grit; compare efficiencies. | heat_exchanger |
| EM-16 | EM-10, EM-15 | Optional | Construct heated Pressure Refinery, supply water, compare supporting metal and heater effects. | pressure_refinery |
| EM-17 | EM-16 | Optional | Use Mechanical Pump with actual fluid source and output space. | pump |
| EM-18 | EM-05, EM-06 | Optional | Run Hearth Coil, choose Char Instiller or Atmospheric Bellows and insulation. | hearth_coil, char_instiller, atmospheric_bellows |
| EM-19 | EM-17, EM-18 | Optional | Attach Mini Boiler, extract gas before tank fills; observe heat recovery. | mini_boiler |
| EM-20 | EM-19 | Optional | Use Catalytic Plug and/or Wildfire Stirling, compare speed/cost; gas supply required. | catalytic_plug, wildfire_stirling |
| EM-21 | EM-10 | Optional | Adjust Clockwork Attenuator's active/inactive speed and upgrade access through Core. | clockwork_attenuator, access |
| EM-22 | EM-10 | Core | Construct Cinder Plinth, generate ash and make Aspectus; learn Exchange Tablet/pedestal arrangement. | cinder_plinth, aspecti, alchemy |
| EM-23 | EM-22 | Core | Build Beam Cannon and execute first alchemy; use waste clue and correct ingredient/Aspectus pairings. | beam_cannon, alchemy |
| EM-24 | EM-23 | Optional | Record successful pattern with Mnemonic Inscriber and store waste in Codebreaking Slate. | mnemonic_inscriber, codebreaking_slate |
| EM-25 | EM-23 | Core | Produce Ember Crystal Cluster, then Wildfire Core; Field Chart is now accessible. | cluster, wildfire, field_chart |
| EM-26 | EM-25 | Optional | Build balanced Ignem Reactor with catalyst and combustor; observe actual output. | ignem_reactor, combustion, catalysis |
| EM-27 | EM-23, EM-19 | Optional | Synthesize/melt Soul Crude and mix Dwarven Oil, boil for gas, use fuel upgrade. | soul_crude, wildfire_stirling |
| EM-28 | EM-23 | Optional | Craft injector, feed an existing ruin/static seed; collect nuggets, observe permanent seed XP. | crystal_seed, ember_injector |
| EM-29 | EM-28 | Optional | Make dynamic seed with same-metal ingots and correct Aspectus, verify eligible loaded tags; preserve Replica intent. | dynamic seed recipe; EMI |
| EM-30 | EM-10 | Optional | Charge portable Jar/Cartridge/Bulb; siphon to unload if needed. | charger, jars, ember_siphon |
| EM-31 | EM-30 | Optional | Craft Clockwork tool, mine/attack with accessible Ember; compare Grandhammer no-drop use. | clockwork_tools |
| EM-32 | EM-30 | Optional | Cast Blazing Ray or Cinder Staff, observe charge and cost. | blazing_ray, cinder_staff |
| EM-33 | EM-23 | Optional | Create Tyrfing, use against armored ordinary target. | tyrfing |
| EM-34 | EM-23 | Optional | Produce Isolated Materia; repair compatible damaged item at Dawnstone Anvil. | materia, dawnstone_anvil |
| EM-35 | EM-34 | Optional | Use anvil for breakdown, augment application or removal; distinguish repair from recovery. | dawnstone_anvil |
| EM-36 | EM-35 | Optional | Install Automatic Hammer and output collection, verify automated anvil recipe. | automatic_hammer |
| EM-37 | EM-25, EM-35 | Core | Apply Motive Core to eligible equipment, accumulate Heat to current cap. | motive core, inferno_forge |
| EM-38 | EM-37 | Core | Build Inferno Forge, temper heat-full item with crystalline Ember; level up on success. | inferno_forge |
| EM-39 | EM-38 | Optional | Choose Blasting Core, Caster Orb, Superheater or Resonating Bell; apply and demonstrate effect. | weapon_augments |
| EM-40 | EM-38 | Optional | Choose armor augment Cinder Jet, Flame Barrier, Eldritch Insignia, Intelligent Apparatus, Shifting Scales. | armor_augments |
| EM-41 | EM-38, EM-32 | Optional | Apply Focal Lens or Diffraction Barrel to compatible Ember projectile weapon. | projectile_augments |
| EM-42 | EM-38, EM-31 | Optional | Use Winding Gears on tool, optionally boots; wind, test mining or jump. | winding_gears |
| EM-43 | EM-23, EM-35 | Optional | Make Ashen armor and attune/socket an Inflictor Gem; try damage-specific defense. | ashen_cloak, inflictor |
| EM-44 | EM-23 | Optional | Choose Amulet of Heretic, Ashen Amulet or Dawnstone Mail; observe appropriate effect. | baubles |
| EM-45 | EM-05 | Optional | Use native Ember Energy Converter to power an eligible FE consumer if pack permitted. | native recipe; energy converter |
| EM-46 | EM-26, EM-38 | Endpoint | Demonstrate an optimized magical workshop and a chosen leveled/augmented item; no requirement to max all branches. | Codex, EMI |
