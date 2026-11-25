# Scriptor Magicae 2.0.14 — native capability Atlas

**Native research COMPLETE; not gameplay acceptance.** Owner-directed scope: same as Psi and Hexcasting; teach process and representative capability bounds, not the whole language. No FTB quest/config/runtime edits. Writer continuing Atlas #410. Existing starter chapter is a single optional checkmark, not a complete course; downstream chapter author should replace/extend within approved scope.

## Evidence
Exact owner JAR scriptor-neoforge-2.0.14-1.21.1-sgd.jar, SHA1 32fece570d3f1086cc59229b1b861fd277da41e8 matches [Packwiz](../../../mods/scriptor-magicae.pw.toml) CF817436/8899737; ZIP CRC passes. 1145 entries,461 classes,58 crafting recipe records,47 tome templates (12/16/14/5),23 English Unfocused markdown pages including TODOs,8 advancement definitions,6 artifact templates,3 ordinary engravings plus village engraving,8 mob spell-item records,3 native spell crafting recipes and3 reagent data. Counts packaged, not loaded/unique. Requires Unfocused >=0.3.6-1.21.1. [Pass 1](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/410#issuecomment-7171313951), [pass 2](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/410#issuecomment-7171643118). Final pass reopens exact JAR and checks AnvilRecipeHandler, ScriptorTrades, BoundToolAction, CastingLecternBlockEntity, CastingLecternBlock, ChalkBlockEntity, CoordinateCasterCrystal, ScriptorEvents and Spell with javap -p -c; native recipes, advancement JSON and guide Markdown. No JAR committed, no source parity or runtime claims.

## Entry, language and progression
Nominomy. Overworld DictionarySavedData persists generated vocabulary across dimensions; optional community mode separate storage. Default generator mixed groups; other:and conjunction generated, one static color token biur. Constructor builds SUBJECT/ACTION/DESCRIPTOR ordering; discarded shuffle result means do not promise randomized order in this release. Read actual found spell, infer meanings; no literal vocabulary in TMM quests. One subject, action with optional descriptors, additional actions joined with conjunction. Semantic Tier1 touch_light = Touch + Light + Orange, not actual typed phrase. Tier1-4 are loot quality and collection achievements, not mandatory casting ranks. Tier1 village/mineshaft/ruined portal/dungeon; tier2 stronghold/bastion/ocean ruins/mansion; tier3 stronghold library/ancient city/Nether fortress/Wither; tier4 End City treasure or native Dragon loot. Pack Dragon loot modifications make End City alternative important. Exact AddLootEvent sources in pass2. Tome item IDs scriptor:tome_tier1…4; first tome advancement scriptor:tome rewards loot scriptor:patchouli_book (resource name, actual guide Unfocused). Four tier advancements custom tome1_collect…tome4_collect; not cast proof. Community trigger on login in community mode; fizzle trigger when cast condition fails (Spell). complex_spells criterion custom complex_spell, no reliable trigger call site proven: do not require it as first successful cast.
Nominomist villagers trade resources and knowledge; compiled ScriptorTrades level1 buy/sell materials, level2 sells material and offers tier1 tome for 3 emeralds (2 uses,15 XP), level3/4 trade one random higher tier for two of immediately lower tier (3 uses,15 XP). Native emerald prices subject to vanilla adjustments and TMM economy. No upward tier exchange proven. Village day engraving and other structures may teach spells. Knowledge scraps (three tiers), notes (two tiers). 

## Writing and casting
Scriptionary scriptor:dictionary (3 leather/3 paper/ink/gold/feather), Writing Desk scriptor:writing_table (5 slabs/bottle/paper/purple wool). Put dictionary top and found tome bottom, right-click words and assign meanings, or insert scrap; put writable book bottom and click vocabulary to insert at cursor, sign. Spellbook Binder leather/purple wool/gold/string yields2; written book+binder or writable spellbook from binder+3 paper+feather+ink. Book of Books scriptor:book_of_books uses binder/6 leather/2 string, right-click inventory, sneak scroll cycle. Unwritten scroll copper/paper/string yields2; write at desk, consumed on use. Identify Scroll guide explicitly unavailable normally, not required. Glow ink obfuscation optional.
Subject IDs self/touch/hitscan/projectile/meteor/rune/on_hit/on_damaged/enchant. Semantic actions break/place, bring/goto/swap, weather/time, water/dry, color/light/write, harm/heal/saturation, fangs/explosion/inflame/smite, bound axe/pickaxe/shovel/sword, potion effects. One representative safe spell per branch; use actual world words, no exhaustive grammar.
Default raw max costs: Tome50, Scroll75, inscribed item30, Lectern25, Chalk250, vocal -1. Cooldown multipliers tome/lectern/vocal100%, scroll50%, inscribed120%, artifact200%. Cost accumulates additive then multiplicative then post-additive, negative subtotal correction; no universal linear savings. Self/Touch +1, Light +2, Break +1, Heal +4, Goto +6, bound tool +6. Item cast helper rounded 7*cost*multiplier*empowered scale. Vocal text chat (not microphone), native enabled, command disabled; Mute prevents. Rounded raw cost*30 and empowerment/multiplier produces scaled integer; cooldown scaled*10 ticks, Hunger threshold50/damage125 use scaled integer not raw cost, duration 2*(scaled-50)t, damage (scaled-.75*125)/100 above125; creative bypass. Nondefault vocal cap enforcement questionable (message without immediate return), do not assert pack behavior. No ordinary mana/FE; optional scriptor_mana branch not found in current pack roster.
Reagents: amethyst shard -30, redstone -35, gold ingot +10 strength; duplicate reagent descriptors count total, consume after cast. Cast conditions clear skies/night/rain, healthy/critical/poisoned/on_fire, unreliable/weakened/cheap and blood_cost post-add -2. Fizzle and condition failure demonstrate safely. Native spell crafting light ink sac→glow ink, light arrow→spectral arrow, poison arrow→poison tipped arrow; disabled command_test not survival. 
BoundToolAction base strength6, duration4; expiry world gameTime + floor(duration*80) (320t baseline), tier floor(int(strength*.666)/2), component tool rule thresholds for stone/iron/diamond/netherite. Strength and duration descriptors improve temporary gear; not permanent equipment or infinite harvest tier. Phasing/wild phasing effects, not unrestricted flight; respect block exclusion policy, no unverified claim bypass.

## Inscription, ritual, automation
AnvilRecipeHandler left nonempty item, right spellbook tag with written content, copies result/left and stores spell component, glint true, adds 5 levels to existing anvil cost; refuses already-inscribed left item. No need to teach overwriting. Ordinary use or on_hit/on_damaged/enchant subjects; compiled event checks damage weapon and armor. Natural examples: Pillager fangs crossbow .05/flame .2, skeleton swap bow .005, vindicator flame axe .2, zombie resistance chestplate/thorn hat .005; spawn equipment probabilities not drop guarantee; filename wither_skeleton_wither_sword targets pillager in JSON, test_sword disabled. Spell cost ceiling30; event/cooldown and native conditions still apply. No claim of perpetual buffs.
Chalk scriptor:chalk 2 calcite, rename to world word in anvil, write chain, activate first; branches cast per endpoint. Native engravings call_lightning/clear_weather/day, village day. Higher cap250; not infinite power; no invented persistent ritual energy. Rune/projectile subjects and redirection permit different delivery. Redirect crystal scriptor:redirect_crystal 7 planks/glass/amethyst yields8, redirects projectile along face. 
Casting Lectern scriptor:casting_lectern bookshelf/5 slabs/purple wool, insert book/right-click empty hand remove, default facing toward back, nearby chest inventory. Server tick decrements cooldown; any best neighbor signal >0 disables casting; at zero and valid written book parse, if raw cost>25 fizzle and +200t*multiplier/100 cooldown; valid cast max(10,round(cost*10*multiplier/100)) ticks. Guide approximation 1/3 differs from compiled default25 vs tome50. Focus affects self target AND inventory. Crystal base scriptor:casting_crystal 8 redstone/amethyst; each focus combines with tag (paper/ink + cyan wool player, blue wool coordinate, egg animal, rotten flesh mob). scriptor:player_casting_crystal binds self, within16; coordinate_casting_crystal binds up to4 inside faces, within16, inventories can be targeted; both reset by crafting alone. animal_casting_crystal targets nearby animals within16, mob_casting_crystal all nearby mobs within16. Not cross-dimension, not arbitrary unloaded automation. Teach safe low-cost light/heal first, then focus/reagent supply and redstone stop. Config can_target_player_inventories=false default; claims, ownership, target safety and actual TMM effective settings separate. No energy generation, no general digital storage.
Artifacts found in End City/library/bastion/mansion/ancient city/Nether fortress/ocean ruin/desert pyramid. Six semantic templates Banish Me (Nether goto), Disappear (explosive projectile), Exalt Me (hero), Hunger Not (saturation), Despair (rain), Waste Away (wither); four item models not six registry IDs. Native guide noncraftable. Higher-tome discovery and independent authorship are terminal milestones; no formal final boss/skill tier.

## 29-axis navigation
| Axis | Findings |
|---|---|
| gear | Spellbooks, scrolls, inscribed equipment, artifact, temporary bound tools |
| movement | Goto/bring/swap, speed/jump/phasing, Nether artifact; no independent flight claim |
| ore_processing | No native dedicated ore multiplier evidenced; bound tools gather instead |
| storage | Book of Books, Scriptionary, external chest targeting, no virtual warehouse |
| crafting | Native recipes, write spellbooks/scrolls, three spell-crafting conversions |
| logistics | Bring/goto/place, focus inventory and adjacent chests; not universal pipes |
| buffs | Haste, hero, strength, speed, invisibility, night vision etc |
| external_compatibility | Unfocused, vanilla potion/inventory/events; optional mana absent; pack policy separate |
| mob_farming | Targeted damage/automation possible, no native dedicated spawner demonstrated |
| ore_generation | No native independent ore generator evidenced |
| permanent_power | Persisted world vocabulary and knowledge, not innate personal stat upgrades |
| general_generation | Light/water/bound equipment; no unrestricted replication evidenced |
| direct_damage | Harm, fangs, smite, explosion, fire and damage events |
| block_breaking | Break, bound tools, vein-break template |
| permanent_pets | No independent persistent owned companion evidenced |
| temporary_pets | Fangs are attack entities, no independent temporary helper demonstrated |
| defense | Resistance/fire resistance/invisibility/armor inscription |
| healing_recovery | Heal, regen, saturation, repair template |
| debuff_control | Poison/wither/slow/blindness, swap, phasing |
| energy | Typed spell cost/cooldowns/reagents; optional mana branch, not installed |
| automation_control | Lectern, crystals, redstone shutoff, chalk graph, projectile redirect |
| agriculture | Animal focus/heal and water placement; no dedicated farm machine evidenced |
| detection_information | Dictionary, notes/scraps/tomes; no independent ore detector established |
| equipment_augmentation | Five-level inscription, event triggers, bound tool components |
| world_manipulation | Light/place/break/color/water, weather/time and phasing |
| world_content | Loot, Nominomist, engravings and inscribed mob equipment; no native new dimension |
| death_respawn | No native independent death/respawn system evidenced |
| trade_economy | Nominomist material and tome exchanges |
| progression_tiers | Four loot tiers, independent casting/delivery capabilities, no caster rank gate |

## Guided teaching map
Representative quests, not a word-by-word course. Dependencies pedagogical not artificial rank gates. Item possession is only possession; actual effects manual/observable unless precise native trigger. Advancement ID for first/tier finds; fizzle optional troubleshooting; no fabricated cast trigger. No FTB IDs/rewards authored.
| ID | Prerequisites | Teach / evidence |
|---|---|---|
| SC-01 | — | Discover tome and native guide (scriptor:tome) |
| SC-02 | SC-01 | Read its semantic structure; cast known low-cost spell, observe effect |
| SC-03 | SC-01 | Craft Scriptionary and Writing Desk |
| SC-04 | SC-03 | Record meanings from tome or knowledge scrap |
| SC-05 | SC-04 | Make binder and writable spellbook; compose, sign, cast original sentence |
| SC-06 | SC-05 | Modify descriptor and compare safe result |
| SC-07 | SC-06 | Join two actions with discovered conjunction |
| SC-08 | SC-06 | Experiment with cast conditions and fizzle troubleshooting |
| SC-09 | SC-06 | Compare reagent supply/cost and observe consumption |
| SC-10 | SC-05 | Make Book of Books; insert, cycle and cast |
| SC-11 | SC-05 | Write scroll; cast and observe one-use behavior |
| SC-12 | SC-05 | Inscribe eligible item, pay anvil cost and cast |
| SC-13 | SC-12 | Discover event-subject example and demonstrate on-hit/on-damaged effect |
| SC-14 | SC-05 | Conjure bound tool, use then observe expiration |
| SC-15 | SC-05 | Demonstrate safe movement or healing/protection spell |
| SC-16 | SC-05 | Make chalk and lay valid world-word chain, activate |
| SC-17 | SC-16 | Branch engravings and observe two endpoints |
| SC-18 | SC-05 | Build lectern, insert safe spell and observe recurring cast |
| SC-19 | SC-18 | Apply redstone to stop; remove signal to resume |
| SC-20 | SC-18 | Create base crystal and player/coordinate focus; bind/reset |
| SC-21 | SC-20 | Demonstrate animal or mob focus safely |
| SC-22 | SC-18, SC-09 | Supply reagent from adjacent inventory, demonstrate actual use |
| SC-23 | SC-05 | Use projectile and redirect crystal, observe redirected path |
| SC-24 | SC-01 | Find Nominomist, trade for low-tier tome or down-tier exchange |
| SC-25 | SC-01 | Explore tier II/III and IV sources, End City alternative; native tier triggers |
| SC-26 | SC-25 | Discover ancient artifact, inspect and demonstrate safe chosen effect |
| SC-27 | SC-07, SC-09, SC-18 | Independent spell project: select delivery and show functioning result; no hardcoded vocabulary |

## Boundaries
No normal gameplay QA prerequisite. Native guide TODOs, exact phasing whitelist and some complex-spell trigger semantics are not independently resolved; do not teach unsafe traversal or use an unverified criterion. Registry presence does not guarantee early vocabulary access. Actual loaded pack conditions and claim/inventory/capture interactions belong to downstream QA, not invented gates. No changes to existing starter quest or balances. Next: Summoning Rituals exact pin.
