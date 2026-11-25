# Eidolon: Repraised 0.5.0.8 — native capability Atlas

**Research COMPLETE at native quest-source depth; no gameplay acceptance implied.** Independent Wicked/Sacred patron paths, alchemy, rituals, soul enchanting, necromancy, research, equipment, herbs and world encounters. Quests + Codex + EMI together teach the mod; no standalone replacement manual required.

## Evidence
[Packwiz](../../../mods/eidolon-repraised.pw.toml): eidolon_repraised-1.21.1-0.5.0.8.jar, CurseForge project870250 file9332315. Owner-supplied file 4,056,114 bytes, SHA1 12a816c11a064a7050e74c5c15cf07fb8269b6d4 exact pin, SHA512 2fb648e2de9dbffe2d8a041b22f0deff84dfc2f350049290542a21e91bf9aa2326bb8b5ac2ec5c11af9359b2fb6a12516d699ea62b37369bb293e600b94ebef7, ZIP CRC PASS (reopened third pass), 2326 unique entries,585 classes. Actual mod ID eidolon_repraised, MC1.21.1/NeoForge, Curios required, optional Apotheosis. [Version-aligned source](https://github.com/Alexthw46/Eidolon-Repraised/tree/85484f2804bcff82abb3397d8f384422d7e21898), gradle0.5.0.8, NeoForge21.1.220, 2226 tree entries. Exact JAR 278 recipes:82 shaped,34 stonecutting,33 crucible,29 worktable,25 shapeless,18 chant,11 smelt,11 summon ritual,10 blast,10 generic ritual,5 forage,4 conversion chant,3 dye,2 crafting ritual,1 location ritual. 79 advancement records (27 ordinary/52 recipe award),101 loot tables,21 tags,18 worldgen,9 biome modifier,7 Curios,3 damage types,3 affixes,1 altar map; 909 English keys,98 Codex chapter-title and288 page localization keys (not actual page count). Targeted javap -c -p -constants and exact recipe/loot inspection; not full source-to-bytecode equivalence. [Pass1](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/410#issuecomment-7150298204), [Pass2](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/410#issuecomment-7150551723), [Pass3](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/410#issuecomment-7150634479) retain precise implementation evidence. Prior [partial profile](../../mod-comprehension/mods/eidolon-repraised.md) superseded for native details; historical effective generated config useful but not new cast test. No JAR committed.

## Entry, materials and workstations
Codex shapeless book+rotten flesh, opens directly/lectern, six categories Nature/Rituals/Artifice/Theurgy/Signs/Spells. Lead and silver ore stone/deepslate, size6, attempts lead6 Y-47..41, silver5 Y-60..33 Overworld. Iron+lead tagged ingots→2 Pewter Blend, furnace/blast. Worktable uses 3x3 core plus separate reagent slots, actual worktable recipe serializer, returns crafting remainders. Crucible 1000mB water over lit campfire; insert steps per Codex/EMI; empty-hand stir (20t cooldown), redstone stir, fluid handler; server processing every8t; config default timed100t step, first half time, backoff40t, turnBased false. Wrong sequence/time can fizzle and drain water; don't improvise arbitrary ingredients. Wooden Brewing Stand native alternative, follow Codex and EMI; no universal potion bypass. 
Significant alchemy: Enchanted Ash furnace; death essence zombie heart+rotten flesh→stir2+bone meal2→charcoal makes4 (withered heart8); Crimson Essence fungus+wart→stir1+sulfur makes4 (roots/vines2); shadow/crimson gem coal→stir1 ghast tear+essence→stir1 shards2+essence→diamond; Lesser Soul Gem redstone2+lapis2→stir2+shards4→quartz; arcane gold alchemy redstone2+shard→gold2 outputs2; Ender Calx pearl+ash→2. Magic Ink black dye2+glow ink sac→blue dye/stir1→silver nuggets2/stir1→glass bottle outputs2. Parchment leather→stir1→stir1→paper3+ash outputs4. Sulfur2, gunpowder4, glowstone2, gilded foods, leather, elder bricks16, polished planks, sprouts, incenses. Tether ingredient minecraft:lead is vanilla leash, not lead ingot, actual packaged data. Treat canonical sulfur reconciliation #343 separately; do not change recipe. Ordinary station recipes in EMI; no wholesale recipe transcription.

## Patron progression, shared mana and sign casting
Drop unsigned Codex near Witch or Cleric, barter goal returns signed book, retrieving learns Wicked or Sacred. Both independent and attainable concurrently. Wooden Altar 3 slabs+4 planks→3; Straw Effigy5 wheat. Place icon/effigy on connected horizontal same-block-type altar; nearest effigy within AABB offsets -4..+5, ready state. Decorations data map maxima power and capacity separately per category, summed across categories: light torch1, candle/candlestick2, magic candlestick3, lantern1 power/1 capacity; plant roots1/fungus2/wither rose3; skull skeleton capacity2, wither power1/capacity3, zombie power1/capacity1; goblet/censer capacity2 same category. No same-category stacking. Triple Wicked/Sacred prayer base rep1+.25*power, each spell has configurable cooldown; compiled default0 below minimum21000, fallback21000, historical generated TMM actual21000 (no current committed override). Approx daily per prayer per prior config, not assume corrected source. Prayer updates shared mana max=max(old,20+reputation*(1+capacity/2)), adds rep+2*power clamped0..max. Separate reputation states, knowledge and shared mana. No cross-school conversion confirmed.
| Rep | Dark | Light |
|---:|---|---|
|0|Wicked Prayer|Sacred Prayer|
|3|Animal Sacrifice, Blood then Soul|Offering Incense, Flame then Soul|
|10|Dark Touch|Holy Touch|
|15|Villager Sacrifice, Mind|Lay on Hands, Mind|
|35|Zombify Villager, Harmony|Cure Zombie, Death/Harmony|
|50|Enthrall, Magic/Warding|Smite, Magic/Warding|
|100|maximum devotion|maximum devotion|
Actual major-stage requirement is KnowledgeUtil.knowsResearch lock, granted by successfully doing rite (not Research Table task). Goblet captures type from nearest non-Monster LivingDropsEvent (offsets -2..+3), no Athame needed; Animal or AbstractVillager type requirement for sacrifices, consumed recorded type, rep3+.5power or 6+power. No automatic evil/holy exclusion. Lay on Hands ray non-undead living target, self fallback; injured OTHER grants rep3 and research, not strictly villager, heals5+.05*light devotion and cleans harmful milk-curable effects. Zombify and cure respective prayer/conversion, rep8+1.25power; exact mana cost20. Offering needs rep>=3, censer/altar, grants lock research rep2+.5power and mana update. Smite successful hit grants research, default10 magic damage, Weakness200t amp2. Chants learn11 signs Wicked/Sacred/Blood/Soul/Mind/Flame/Winter/Harmony/Death/Warding/Magic, sequences in Codex and 18 native chant recipes. Configurable delay (default10t), mana affordability and spell pre-event plus targeting requirements. 
| Action | Base mana | Effect/gates |
|---|---:|---|
|Darklight/Light|3|place light, known signs|
|Fire|10|block ignition/target fire200t, delay5t|
|Water|10|1000mB fluid handler or water placement|
|Lay on Hands|15|healing and cleanse, other grants rite token|
|Frost|20|water to ice or chilled200t|
|Dark/Holy Touch|20|rep10, suitable target|
|Zombify/Cure|20|conversion with patron rite checks|
|Smite|40|undead damage/weakness|
|Sunder/Reinforce Armor|50|Vulnerable1200t/Reinforced1800t|
|Enthrall|50 initial gate|actual cost100*remaining HP fraction; initial50 affordability still applies|
Prayer/sacrifice separate reputation gain/cooldown; inspect effective TOMLs when balancing. Undead Lure class registered cost50, TODO no native chant recipe found, not normal survival lesson. Scriptorium feather/ink/slabs/planks, consume parchment or scroll +1 ink, output same-sign stack, cast with known signs and normal checks; item use invokes hurtAndBreak(1) but registered stack64 without durability, actual wear not established; 20t cooldown, reusable by registered properties. Do not promise scroll knowledge bypass. Four chant conversions includes top hat/disc/inlay. 
## Research independent of patron rites
Notetaking Tools interact Blaze or Stray/Wraith, consumes one and gives seeded notes; insert Research Table, perform generated item-tag/XP tasks, timer advances step; learn completed research. Flames5, final3 blaze rods+3 fire charges+6 XP; Frost5 step4 ice10+snow10+6 XP, step5 Wraith Hearts3. Unlock Flame/Winter signs. Pig Gluttony3 registered but no meaningful learned callback, Codex WIP, no imagined extra research branches. Flame also patron path. 
## Brazier rituals, soul acquisition, pet system
Craft Brazier, pedestal/focus Plinth, reagent inside single slot, ignite, up to80t finding period; recipe exact requirements and focus/invariants, staged setup then tick/start, incomplete setup fails/cancel. Use Codex/EMI, distinguish focus consumed vs invariant. 24 actual recipes:
| Rite | Inputs/operating effect |
|---|---|
|Crystal|bone meal reagent+2 redstone; eligible undead ritual damage1000×maxHP, 1–3 Soul Shards on accepted damage; not guarantee against boss immunity|
|Absorption|Death Essence+2 cloth+bone+2 shards, invariant Summoning Staff; weakened eligible non-owned undead/whitelist <=1/3HP, heal full serialize remove, store up to100|
|Allure|rose bush+golden apple+2 red dye+2 shards; attract animals AABB96,16,96|
|Repelling|nautilus+iron/leather/quartz+2 shards; repel monsters same AABB|
|Deceit|villager gossip decay probability1/120 each second in48,16,48; not instant wipe|
|Purify|glistering melon+2 ash+healing potion+2 shards; reverses zombie villager, zombified piglin, zoglin|
|Daylight/Moonlight|sunflower or black dye focus reagent, seed/charcoal or snowball/spider eye +2 shards; time+100 per tick toward day1000..11999 or night>=13000; world time not machine acceleration|
|Locate Catacomb|map+compass+magic ink+raven feather, nearest structure tag search100, red-X Magic Map if found|
|Sanguine Amulet|basic amulet, diamond+4 redstone+lesser gem, harming potion focus, health40, reagent data retained|
|Sapping Sword|crafting rite health20, reagent and focus per EMI; preserve actual native requirements|
|Recharge Soulfire/Bonechill|lesser gem+2 blaze powder/snowballs+redstone, invariant wand; reset damage|
|11 summons|creeper,drowned,husk,phantom,raven,skeleton,slimy slug,stray,wither skeleton,wraith,zombie; normal MOB_SUMMONED entities NOT automatically owned; raven/slug/drowned/husk3, others1. Wither skeleton charcoal+shard+bone, soul sand focus|
HealthRequirement counts nearby non-inverted mobs and players actual HP, applies ritual damage until nominal total consumed, not harmless catalyst. Ritual damage tags bypass armor/enchant not all protection. Choose intended game mobs for setup, avoid player self-damage. 
Summoning Staff obtained Necromancer drop0–1 (50%, no player-kill condition), no craft. Absorption stores serialized mobs; excess beyond100 removed before truncation; beware overfill. Shift-use selects, release after>=20t target within16 ray, UUID removed, spawn new persistent thrall and consume one charge survival. Bonk own thrall recalls via event, remove-before-add cap risks loss when full. Direct Enthrall uses block interaction range+3, whitelist OR undead&&!blacklist (direct whitelist precedence), both shipped tags empty, no global follower cap established; persistence owner UUID, persistenceRequired, follow/target goals, no teleport per Codex. Separate direct thralls and wild summons. Raven seed tame1/10, breed beetroot seeds, sit/follow, adult feather12000–23999t; slug tame/breed temptation items and adult slime same interval; native renewable resources. 
## Incense
Censer one incense, ignite valid start; reagent clears at80t but actual effect continues to duration. Default 10-block inflated AABB. Most generic effect refresh every20t. 13 registered varieties; Warding commented out. No unsupported planned content.
| Incense | Burn ticks | Operation |
|---|---:|---|
|Offering|800|rep>=3 sacred prayer, lock research, rep2+.5power|
|Bloodlust|400|Strength12000t|
|Quicken|400|Speed12000t|
|Tough|400|Reinforced2400t amp1|
|Undeath|400|Undeath2400t, refresh40t; harmful, sunlight burn, healing inversion, not permanent Lich|
|Soul Harvest|400|Soul Harvest100t, loot/XP effects, reaper bonus+2 looting|
|Deathbane|800|Light Blessed12000t non-undead, nausea2400t undead|
|Restoration|800|heal1 living per20t, Wither200t amp1 undead|
|Gloom|800|heal1 undead per20t, Wither200t living|
|Purity|800|cleanse eligible harmful effects every40t|
|Frail|800|Vulnerable200t|
|Frostbind|2400|Chilled30t every10t, slow/no healing|
|Tether|2400|Anchored40t prevents teleport|
Burn vs lingering duration different; buff refresh can affect any applicable nearby creature, not assumed party-only. Test cross-mod Lich/Hunter effects and PvP separately.
## Soul enchantment, tools, armor and curios
Soul Enchanter worktable book+2 arcane gold+4 obsidian, outer2 diamonds+2 gold inlay. Three seeded choices one enchant at a time, +1 existing or level1, actual supported items, enchanting table tag, excludes treasure/curse/blacklist and conflicting/max-level enchant; optional Apotheosis max-level hook. Each click 1 Soul Shard+min(5,result level) XP levels. Native config max uses/count=-1, blacklist empty; unlimited repeat/count not infinite enchant levels. Separate pack enchanting balance. 
Athame pewter tool, Looting event upgrades level to2*existing+1; x4 damage against Enderman/Endermite/EnderDragon in damage Pre. Rightclick eligible plants with chance to harvest; five forage recipe paths fern→Avennian, oxeye/lily valley/white tulip→Merammer, lily pad→Oanna, crimson/warped fungus→Mirecap, jungle leaves→Sildrian; Planter soil/ash/shard and regrowth, athame mature harvest. Reaper Scythe undead kill removes nonarmor drops except player, shard random nextInt(2+looting [+2 Soul Harvest]); Deathbringer adds Undeath900t to non-undead, no automatic boss kill. Cleaving Axe head-drop path; Inversion Pick speed sqrt(original*.25)*sqrt(hardness/(1/(hardness/2))) (harder faster, tool tier and unbreakable rules separate). Silver tools/armor, material ingredients via EMI. 
Wands: Soulfire worktable arcane gold/shadow gem/lesser soul gem/blaze powder; Bonechill pewter/wraith heart/lesser soul gem/bone meal; max durability253, no normal repair, casts cost1 damage and cooldown15t; recharge rite resets damage. Bonechill Chilled stops healing; Soulfire offensive projectile; optional Apotheosis affixes/tracking. Angel's Sight worktable basic ring/shadow gem/patron symbol/wither skull etc, mode-selected tracking hook on projectile join; use guide modes. 
| Equipment | Native effects and boundaries |
|---|---|
|Raven Cloak|wicked weave3/raven feathers4/cloth2/gravity belt/membranes2;10 flap/dash charges, ground refill, +.5 upward flap, dash100t, fall distance quarter; not free mayfly|
|Gravity Belt|-60% gravity, fall distance quarter|
|Prestigious Palm|+4 block interaction range|
|Enervating Ring|+.5 chanting speed modifier|
|Resolute Belt|knockback resistance and repulsion|
|Void Amulet|cancels projectile damage once/100t; guide says10s, actual5s|
|Warded Mail|armor defends magic via damage handler; not universal invulnerability|
|Glass Hand|outgoing damage doubled, incoming ×5; dangerous tradeoff|
|Mind Shielding Plate|nausea immunity, cancels XP drop, clone copies original level/progress using bit flag; guide75% retention inconsistent, actual death ordering separate|
|Sanguine Amulet|40 HP reservoir, gain1 every80t full health+food>=18, exhaustion up to16 (saturated) or4, heals1HP every10t if enough missing health|
|Soulbone Amulet|+2 ethereal HP on source kill while worn, raises cap; guide10 hearts but no amulet-specific cap found, global config60HP; buffer before ordinary HP, decreases cap unless persistent floor|
|Bonelord/Bone Paladin|helmet10/chest20/legs15 persistent ethereal HP attributes, regen persistent portion, guide poison/wither protection; global60HP cap|
|Warlock Hat/Cloak/Boots|hat +.5 magic power attribute, guide magic/wither offense and wither healing; cloak magic/wither mitigation and boots slowness/terrain mitigation per guide; self-repair code commented|
|Terminus Mirror|registered projectile cancel every hit, no native recipe or loot acquisition established; do not teach survival reward|
Use exact Curios slots/EMI, no promise effects stack with pack armor/damage engine. 
## World and encounters
Lab random spread32/8, Catacomb80/40 (giant skeleton spawn override), Stray Tower24 with tagged biomes, Illwood in spooky tag. Wraith/Zombie Brute weight60 group1–3 Overworld except deep dark/mushroom/no default monsters. Raven forest10 group2–4; slug biome variants. Loot Wraith cloth0–2, heart5% base (Looting1 7.5%,+2.5% further); Brute heart15% (L1 25%,+10%), Wither event changes heart to Withered Heart; lead2.5%; Giant Skeleton imbued bones15% (L1 30%,+15%); Necromancer staff0–1; Raven feathers1–2, slug slime0–1. Other loot and exact generation data via EMI/source. Natural mob and ore behavior not pack rates; no custom mob taxonomy change. 
## Downstream review / limits
Native research complete; no new build, QA, implementation or balance approval. Historical prayer default/min anomaly, scroll no durability registration, Void Amulet/XP-retention/Soulbone guide discrepancies, registered but unobtainable Mirror and Undead Lure, Warding unregistered. Review Soul Enchanter/Apotheosis economy, thrall eligibility/cap/recall/ownership, crystal and Athame boss gates, damage effects and Hunter/Monk/lich interactions, Undeath vs JAWR/Progression, sulfur unification ownership, claims and global time; no automatic recipe/config changes. Exact binary recipe references support guide+EMI. Existing TMM chapter single optional checkmark, no quest edit. 
## All 29 capability axes
| Axis | State | Native scope |
|---|---|---|
| gear | present | metal tools, wands, scythes, Curios, armor|
| movement | present | Raven Cloak, gravity belt, terrain effects|
| ore_processing | present | lead/silver smelt, pewter blend, alchemy|
| storage | present | staff creature storage, mana, soul reserve; no mass item storage|
| crafting | present | Worktable, Crucible, brewing, rituals, Scriptorium|
| logistics | no_capability_confirmed | station I/O not general routing|
| buffs | present | incense, chants, equipment|
| external_compatibility | present | Curios, Apotheosis conditional, recipes/tags/capabilities|
| mob_farming | present | summons, allure, crystal, reaper, raven/slug production|
| ore_generation | no_capability_confirmed | natural ore worldgen, not player-created ore|
| permanent_power | present | signs/reputation/research, gear, persistent soul-heart floor|
| general_generation | present | essence, materials, water/light, renewable pets/herbs|
| direct_damage | present | chants, wand, scythe, ritual damage|
| block_breaking | present | Inversion Pick and tools, Athame plant harvest|
| permanent_pets | present | tamed animals and persistent thralls|
| temporary_pets | no_capability_confirmed | wild ritual summons not temporary owned pets|
| defense | present | soul hearts, amulets, armor, reinforced, repelling|
| healing_recovery | present | Lay on Hands, Restoration/Gloom, Sanguine|
| debuff_control | present | Chilled, Anchored, Purity, Undeath, Vulnerable|
| energy | present | patron mana capacity/recharge, soul shards, wand charge|
| automation_control | present | redstone stir, station interfaces, ongoing rituals|
| agriculture | present | foraging, planters, pets|
| detection_information | present | Catacomb map, research observation, targeting|
| equipment_augmentation | present | Soul Enchanter, wand recharge, soul armor|
| world_manipulation | present | day/night, block/light/water, conversion, rituals|
| world_content | present | ores, trees, three structures, six creature types|
| death_respawn | present | Mind Shielding Plate XP retention path, no resurrection|
| trade_economy | present | Witch/Cleric Codex barter, Deceit gossip|
| progression_tiers | present | parallel devotion and research, artifice/ritual progression|
## Quest-source map — 44 representative instructional slices
Not required FTB count; combine where guide+EMI sufficient. Acquisition tasks are proxies; native advancement when actual trigger verified, otherwise manual observable cast/output. Editorial dependencies not new runtime gates. Positive XP and safe reward, don't bypass rare loot or patron gates. 
| ID | Previous | Kind | Objective | Reference |
|---|---|---|---|---|
| EI-01 | — | Core | Craft/open Codex; read six sections; use EMI for recipes. | Nature, Artifice |
| EI-02 | EI-01 | Core | Mine lead/silver or acquire tagged ingots; lead+iron→2 Pewter Blend, smelt; craft Worktable. | Artifice: Pewter, Worktable |
| EI-03 | EI-02 | Core | Operate Worktable core + reagent grid, verify crafted output and remainders. | Artifice: Worktable |
| EI-04 | EI-02 | Core | Craft Crucible, lit campfire, bucket water, complete a simple correct recipe and stir sequence. | Artifice: Crucible |
| EI-05 | EI-04 | Core | Make Enchanted Ash and early arcane materials, then lesser soul gem/shadow gem. | Artifice: Ash, Soul Gems |
| EI-06 | EI-03 | Optional | Craft Athame and obtain magical herb via valid plant foraging; plant/regrow in Planter. | Nature: herbs; Artifice: Athame |
| EI-07 | EI-01 | Dark | Drop unsigned Codex to Witch, retrieve signed Codex, learn Wicked. | Signs, Theurgy |
| EI-08 | EI-01 | Light | Drop another unsigned Codex to Cleric, retrieve, learn Sacred. | Signs, Theurgy |
| EI-09 | EI-03, EI-07 | Dark | Build altar/effigy, cast Wicked triple prayer, observe reputation/mana. | Theurgy: Dark Prayer |
| EI-10 | EI-03, EI-08 | Light | Build altar/effigy, cast Sacred triple prayer, observe reputation/mana. | Theurgy: Light Prayer |
| EI-11 | EI-09 | Optional | Improve altar with different decoration categories, compare power/capacity; use same type connected altar. | Theurgy: Altars |
| EI-12 | EI-09 | Dark | Reach rep3, learn Blood; fill Goblet using eligible animal death, perform Animal Sacrifice, unlock Soul. | Theurgy: Sacrifice |
| EI-13 | EI-10, EI-04 | Light | Reach rep3, learn Flame, make Offering Incense, ignite Censer near altar, unlock Soul. | Theurgy: Incense |
| EI-14 | EI-12 | Dark | Reach rep10; perform Dark Touch and see target damage. | Spells: Dark Touch |
| EI-15 | EI-13 | Light | Reach rep10; perform Holy Touch on suitable undead target. | Spells: Holy Touch |
| EI-16 | EI-14 | Dark | Reach rep15; perform Villager Sacrifice with appropriate recorded goblet type, unlock Mind. | Theurgy: Villager Sacrifice |
| EI-17 | EI-15 | Light | Reach rep15; use Lay on Hands on injured non-undead other, unlock Mind; healing grants research. | Theurgy: Lay on Hands |
| EI-18 | EI-16 | Dark | Reach rep35; Zombify Villager, acquire Harmony/knowledge. | Theurgy: Zombify |
| EI-19 | EI-17 | Light | Reach rep35; cure Zombie Villager, acquire Death/Harmony knowledge. | Theurgy: Cure Zombie |
| EI-20 | EI-18 | Dark | Reach rep50; perform Enthrall Undead on eligible mob with enough mana; verify loyal thrall. | Theurgy: Enthrall |
| EI-21 | EI-19 | Light | Reach rep50; successfully Smite suitable undead; unlock Warding. | Theurgy: Smite |
| EI-22 | EI-20 | Optional | Continue dark reputation toward 100; no artificial obligation to max both. | Theurgy |
| EI-23 | EI-21 | Optional | Continue light reputation toward 100; use Reinforce Armor or healing. | Theurgy, Spells |
| EI-24 | EI-03 | Optional | Make Notetaking Tools and Research Table; obtain Blaze notes and complete Flame research. | Artifice: Research Table |
| EI-25 | EI-03 | Optional | Obtain Stray/Wraith notes and complete Frost research; learn Winter. | Artifice: Research Table |
| EI-26 | EI-04, EI-24 | Optional | Cast fire/light or other known-sign utility chant; see book for actual sequence and target. | Spells |
| EI-27 | EI-25, EI-24 | Optional | Use Frost Touch and Create Water on valid target; demonstrate fluid or freeze output. | Spells |
| EI-28 | EI-04, EI-07 | Optional | Make magic ink/parchment and Scriptorium; inscribe known chant and cast without manual sequence. | Artifice: Scriptorium |
| EI-29 | EI-03 | Core | Build Brazier and pedestals/focus blocks, execute Crystal ritual on eligible undead, collect Soul Shards. | Rituals: Crystal |
| EI-30 | EI-29 | Optional | Run Allure, Repelling, Deceit, Purify or day/night rite; demonstrate selected effect. | Rituals |
| EI-31 | EI-29 | Optional | Summon one wild creature using recipe, distinguish from tamed/enthralled pets. | Rituals: Summoning |
| EI-32 | EI-29 | Optional | Make magic map and locate Catacomb; explore and obtain imbued bones. | Rituals: Location; Nature |
| EI-33 | EI-29 | Optional | Explore Necromancer encounter and obtain Summoning Staff, perform Absorption on weakened undead. | Nature: Necromancer; Rituals |
| EI-34 | EI-33 | Optional | Summon a stored thrall, recall with staff, avoid full staff; demonstrate following and combat. | Artifice: Summoning Staff |
| EI-35 | EI-06 | Optional | Tame/breed Raven or slug, collect naturally produced feather/slime. | Nature |
| EI-36 | EI-05, EI-29 | Optional | Craft Soul Enchanter, add or improve a compatible enchantment using shard+levels. | Artifice: Soul Enchanter |
| EI-37 | EI-05, EI-29 | Optional | Craft wand and cast; recharge via focus ritual when needed. | Artifice: Wands; Rituals |
| EI-38 | EI-05, EI-29 | Optional | Craft Reaper Scythe and harvest undead souls; later Deathbringer, Cleaving Axe, Inversion Pick. | Artifice |
| EI-39 | EI-05, EI-29 | Optional | Create soulbone or sanguine amulet, demonstrate protective/healing reserve; avoid self as ritual health source. | Artifice: Ethereal Hearts; Rituals |
| EI-40 | EI-32, EI-39 | Optional | Make Bone Paladin gear with imbued bones, demonstrate regenerative portion of ethereal health. | Artifice: Bone Paladin |
| EI-41 | EI-05 | Optional | Craft Warlock armor or selected Curio, observe intended effect; use EMI exact components. | Artifice |
| EI-42 | EI-35, EI-41 | Optional | Craft Raven Cloak, learn charge-based flap/dash and ground recharge. | Artifice: Raven Cloak |
| EI-43 | EI-04, EI-13 | Optional | Use restorative, offensive, defensive or utility incense in Censer, observe burn vs lingering effect. | Rituals: Incenses |
| EI-44 | EI-29, EI-36 | Endpoint | Demonstrate chosen advanced patron/ritual/soul-artifice combination; editorial milestone only. | Codex, EMI |
