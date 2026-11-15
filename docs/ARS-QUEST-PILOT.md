# Ars Nouveau quest pilot

Implemented on `feature/ars-quest-pilot`, based on the current humorous-reward quest source. Replaces the single Ars scaffold with **49 quests**: **29 core Ars entries and 20 add-on entries**. The full book becomes **228 quests / 187 authored**, retaining 45 chapters and five groups. Other chapter data and existing claim IDs are preserved.

## Intended playtest

Test this chapter against the new quest standards before scaling the approach to other schools. The main route is Novice Book → Imbuement/Source/Scribing → Apparatus and Mage Book → ritual preparation and Chimera → Archmage. Archmage acquisition is persistent and follows native recipe requirements; the optional kill objective separately requires actual player kill credit. Side branches remain optional and add-on entry nodes have no artificial cross-school quest gates.

Practical lessons include passive imbuement and reusable catalysts; natural growth versus bone meal; Source versus casting mana; relay direction and redstone shutoffs; thread-slot tiers; helpers and familiars; lectern/catalog storage; portable self spells; temporary ritual flight; remote access requiring loaded chunks; lag-aware farm shutoffs; and safe cross-mod spell transcription.

### Add-on coverage

| Installed add-on | Quests | Practical coverage |
| --- | ---: | --- |
| Ars Additions | 3 | Advanced Dominion Wand, remote Storage Lectern access, shared Ender Source storage |
| Ars Controle | 3 | Remote endpoint controls, Temporal Stability Sensor, Portable Brazier Relay |
| Ars Elemental | 3 | Lesser Fire Focus, Source-powered water, elemental armor |
| Ars Elemancy | 2 | Cinder Essence and combined fire/air armor |
| Ars Ocultas | 3 | Alternative Spirit Attuned Gem recipe, preparation for jarred workers, Sacrificial Altar |
| Ars ’n Spells | 4 | Loom, actual transcription, actual binding, actual cross-cast |
| Ars Unification | 2 | Farmer’s Delight pumpkin/cabbage cutting, Sensitive/AoE/Pierce processing |

### Detection and reward decisions

- Only the initial orientation uses a manual checkmark, with 5 direct XP and no item. This is the first narrowly validated XP-only orientation exception in the authoring schema. The original overview quest/task/XP IDs remain stable; its old provisional item payout is retired.
- The other 48 quests pay direct XP and useful fixed items. There are no coins, XP bottles, paper/cookie/compass filler, empty bowls, repeatable gifts, free upgraded books, Nether Stars, Totems, Wilden Tributes or other boss-gate materials.
- Sixteen native advancements preserve book/helper acquisition or track real actions; a seventeenth, `tmm:ars_pilot/chimera`, uses vanilla `minecraft:player_killed_entity` for `ars_nouveau:wilden_boss`, without a separate advancement payout. Other objectives detect equipment or collected products without consumption.
- Quest progress continues to use the book’s existing FTB team behavior. Each new reward is explicitly personal and claimed once per player (`team_reward: false`). This does not imply that each teammate personally performed a team-shared objective.
- The preexisting starter choice may intentionally satisfy Novice Book acquisition. All ordinary fixed rewards are checked against direct item tasks **and the pinned native inventory advancement predicates** to prevent unnoticed gift chains.
- Inventory detection for Ocultas equipment does not prove a worker was captured or a ritual ran. Unification output collection accepts ordinary cutting as well as magical preparation. Quest titles/text identify these as equipment/product milestones; instructions teach the add-on route without claiming instrumented spell-processing detection.
- The Unification pin contains other optional machine adapters. This pack’s quests teach its installed Farmer’s Delight integration; they do not install or promise excluded/absent tech mods.

### Repeated reward review

Repeated ingredient IDs across the older book are surfaced by the validator. Their uses in this pilot are deliberate: lapis funds scribing; bone meal establishes the Source garden; string supplies clothing; nether wart supplies Wixie brewing; beef supplies the Chimera attempt; feathers supply safe mobility experiments; glow ink/signs support bulk-linked storage labeling; item frames label destinations; repeaters support the farm cutoff; magma cream supports fire specialization; crying obsidian supports the ritual theme; rockets support portable exploration. Source Berry Rolls recur for a novice’s provisions and after successful cross-mod binding. No repeated payout is an objective or a native inventory-advancement trigger.

### Source and validation

`manifest/ars_quest_pilot.json` owns original prose, rewards, layout, IDs by stable allocation number, and evidence links into the exact hash-pinned distribution JARs. Recipes, in-mod descriptions, native advancements, and Unification’s integration classes were inspected. No mod versions or recipes were changed.

Rebuild with `python scripts/build_ars_quest_pilot.py`. It updates the canonical authoring/ID manifests, runtime chapter, shared English locale, layout ledger, Chimera advancement and packwiz hash chain. Repeating the compiler produces byte-identical output.

Run `python scripts/validate.py`, `python scripts/validate_quests.py`, and `python scripts/validate_ars_quest_pilot.py --jars <hash-verified-mods-directory>`. The branch CI also boots the complete pinned pack and injects a disposable one-shot registry probe for **93 items and 17 advancements**. The probe is not shipped as a gameplay script. Local clean server boot and registry probe passed: 135 server JARs, 93 pilot item IDs, 17 advancement IDs and 228 loaded quests. Preexisting issues outside Ars are recorded in COMPATIBILITY.md. A server boot is not graphical/client acceptance.

## Disposable client acceptance checklist

1. Open Magic → Ars Nouveau. Confirm readable titles, escaped punctuation, visible dependency lines, no overlap, optional side branches, and 49 nodes.
2. Read the orientation and inspect at least one short quest and the longer Wilden ritual explanation. Confirm the instructions and humor are useful in play rather than tiring to read.
3. Use the existing Ars starter choice or craft a Novice Book. Upgrade it before revisiting the quest: native advancement detection must retain credit after the old item is gone.
4. Test Source production, apparatus crafting, one helper and one armor/thread task. Equipment acquisition should not falsely claim the setup was used. Rewards must match their descriptions and quantities.
5. Summon a real Chimera in a remote disposable arena; a player-credited defeat must count. Receiving a Tribute alone must not satisfy the kill objective. The Archmage book should still require its real recipe.
6. Complete at least one task in every add-on branch. For Ars ’n Spells, make a real carrier, bind it, and cross-cast; mere Loom possession must not complete later action quests.
7. For Unification, test Touch → Cut → Sensitive on dropped pumpkin/cabbage, compare JEI results and try AoE/Pierce in small batches. Ordinary preparation may satisfy the collection milestone, as the prose states.
8. Test one second player in the same team: shared task progress, personal item/XP claims once each, Claim All, relog and no duplicate claim. New book rewards must not advance unrelated quests.
9. Confirm no later reward is automatically received from orientation and no boss/progression ingredient is gifted. Judge the Chimera soundtrack, helper sunglasses, food rewards and school-specific supplies as the pilot reward standard.

## Quest inventory

| # | Branch | Quest | Detection | XP | Fixed reward |
| ---: | --- | --- | --- | ---: | --- |
| 0 | ars-nouveau | Ars: A Workshop with Teeth | checkmark | 5 | — |
| 1 | ars-nouveau | Your First Spellbook | advancement · `ars_nouveau:novice_spell_book` | 20 | 2 × `ars_nouveau:source_berry_roll` |
| 2 | ars-nouveau | Amethyst Goes In, Source Gems Come Out | item · `ars_nouveau:imbuement_chamber` | 25 | 4 × `ars_nouveau:source_gem` |
| 3 | ars-nouveau | The Table That Eats Your Homework | item · `ars_nouveau:scribes_table` | 25 | 6 × `minecraft:lapis_lazuli` |
| 4 | ars-nouveau | Bottle the Workshop Budget | item · `ars_nouveau:source_jar` | 20 | 8 × `minecraft:blue_stained_glass` |
| 5 | ars-nouveau | Your Garden Has a Second Job | item · `ars_nouveau:agronomic_sourcelink` | 35 | 12 × `minecraft:bone_meal` |
| 6 | ars-nouveau | Link Direction Is a Verb | item · `ars_nouveau:relay` | 30 | 2 × `minecraft:lever` |
| 7 | ars-nouveau | Eight Pedestals and a Plan | item · `ars_nouveau:enchanting_apparatus` | 50 | 8 × `ars_nouveau:arcane_pedestal` |
| 8 | ars-nouveau | Grow Your Own Wardrobe | item · `ars_nouveau:magebloom_crop` | 30 | 12 × `minecraft:string` |
| 9 | ars-nouveau | The Mage Upgrade | advancement · `ars_nouveau:apprentice_spell_book` | 75 | 2 × `ars_nouveau:air_essence` |
| 10 | ars-nouveau | Rituals Need Floor Space | item · `ars_nouveau:ritual_brazier` | 40 | 4 × `minecraft:soul_lantern` |
| 11 | ars-nouveau | Schedule a Very Bad Meeting | item · `ars_nouveau:ritual_wilden_summon` | 60 | 8 × `minecraft:cooked_beef` |
| 12 | ars-nouveau | The Meeting Has Escalated | advancement · `tmm:ars_pilot/chimera` | 200 | 1 × `ars_nouveau:music_disc_firel_the_wild_hunt` |
| 13 | ars-nouveau | Archmage, Pending Paperwork | advancement · `ars_nouveau:archmage_spell_book` | 250 | 1 × `ars_nouveau:thread_spellpower` |
| 14 | ars-nouveau | Robes with Actual Benefits | item · `ars_nouveau:arcanist_robes` | 35 | 4 × `minecraft:purple_dye` |
| 15 | ars-nouveau | Read the Slot Before Sewing | item · `ars_nouveau:alteration_table` | 40 | 2 × `ars_nouveau:blank_thread` |
| 16 | ars-nouveau | Hire a Small Orange Courier | advancement · `ars_nouveau:starby_charm` | 35 | 1 × `ars_nouveau:starbuncle_shades` |
| 17 | ars-nouveau | A Gardener with Standards | advancement · `ars_nouveau:whirlisprig_charm` | 40 | 4 × `minecraft:flowering_azalea` |
| 18 | ars-nouveau | Loot without the Assembly Line | advancement · `ars_nouveau:drygmy_charm` | 50 | 8 × `minecraft:mossy_cobblestone` |
| 19 | ars-nouveau | A Witch with a Work Order | advancement · `ars_nouveau:wixie_charm` | 40 | 8 × `minecraft:nether_wart` |
| 20 | ars-nouveau | Promote a Helper to Familiar | advancement · `ars_nouveau:familiar` | 75 | 3 × `ars_nouveau:source_berry_pie` |
| 21 | ars-nouveau | A Spell with a Day Job | advancement · `ars_nouveau:basic_spell_turret` | 45 | 1 × `minecraft:daylight_detector` |
| 22 | ars-nouveau | Craft from a Magical Library | item · `ars_nouveau:storage_lectern` | 45 | 4 × `ars_nouveau:archwood_sign` |
| 23 | ars-nouveau | Give the Library a Catalog | item · `ars_nouveau:repository` | 35 | 2 × `minecraft:name_tag` |
| 24 | ars-nouveau | Leave Yourself a Way Home | advancement · `ars_nouveau:warp_scroll` | 35 | 2 × `minecraft:glow_item_frame` |
| 25 | ars-nouveau | Creative Flight, Local Subscription | item · `ars_nouveau:ritual_flight` | 70 | 8 × `minecraft:hay_block` |
| 26 | ars-nouveau | Put an Emergency Spell in Your Pocket | item · `ars_nouveau:enchanters_mirror` | 45 | 12 × `minecraft:feather` |
| 27 | ars-nouveau | A Potion for More than One Occasion | advancement · `ars_nouveau:potion_flask` | 40 | 4 × `minecraft:glistering_melon_slice` |
| 28 | ars-nouveau | An Amethyst Department | advancement · `ars_nouveau:amethyst_golem_charm` | 45 | 16 × `minecraft:calcite` |
| 29 | ars-additions | Additions: Bulk Linking, Fewer Clicks | item · `ars_additions:advanced_dominion_wand` | 45 | 4 × `minecraft:glow_ink_sac` |
| 30 | ars-additions | Additions: The Library Travels with You | item · `ars_additions:warp_index` | 60 | 2 × `ars_nouveau:archwood_hanging_sign` |
| 31 | ars-additions | Additions: Source on the Other End | item · `ars_additions:ender_source_jar` | 75 | 2 × `minecraft:ender_eye` |
| 32 | ars-controle | Controle: Configure from a Distance | item · `ars_controle:remote` | 40 | 4 × `minecraft:redstone_torch` |
| 33 | ars-controle | Controle: Give the Farm an Off Switch | item · `ars_controle:temporal_stability_sensor` | 50 | 4 × `minecraft:repeater` |
| 34 | ars-controle | Controle: A Ritual That Follows You | item · `ars_controle:portable_brazier_relay` | 65 | 8 × `minecraft:firework_rocket` |
| 35 | ars-elemental | Elemental: Choose an Element, Keep Options | item · `ars_elemental:lesser_fire_focus` | 45 | 4 × `minecraft:magma_cream` |
| 36 | ars-elemental | Elemental: Source-Powered Plumbing | item · `ars_elemental:everfull_urn` | 55 | 1 × `minecraft:cauldron` |
| 37 | ars-elemental | Elemental: Dress for Your School | item · `ars_elemental:fire_hat` | 60 | 4 × `minecraft:orange_dye` |
| 38 | ars-elemancy | Elemancy: Fire Meets Air | item · `ars_elemancy:cinder_essence` | 60 | 2 × `ars_nouveau:manipulation_essence` |
| 39 | ars-elemancy | Elemancy: Two Schools, One Hat | item · `ars_elemancy:cinder_hat` | 100 | 8 × `minecraft:fire_charge` |
| 40 | ars-ocultas | Ocultas: Attunement without a Diamond | item · `occultism:spirit_attuned_gem` | 40 | 4 × `minecraft:purple_candle` |
| 41 | ars-ocultas | Ocultas: Prepare a Spirit Office | item · `ars_nouveau:mob_jar` | 55 | 2 × `minecraft:hopper` |
| 42 | ars-ocultas | Ocultas: The Sacrifice Has Been Delegated | item · `ars_ocultas:altar` | 80 | 4 × `minecraft:crying_obsidian` |
| 43 | ars-n-spells | Ars 'n Spells: Build the Spell Loom | advancement · `ars_n_spells:craft_spell_loom` | 45 | 3 × `ars_n_spells:blank_scroll` |
| 44 | ars-n-spells | Ars 'n Spells: Weave a Carrier Scroll | advancement · `ars_n_spells:transcribe_spell` | 60 | 8 × `irons_spellbooks:arcane_essence` |
| 45 | ars-n-spells | Ars 'n Spells: Bind It into the Wheel | advancement · `ars_n_spells:bind_spell` | 75 | 4 × `ars_nouveau:source_berry_roll` |
| 46 | ars-n-spells | Ars 'n Spells: Cast across the Curriculum | advancement · `ars_n_spells:first_cross_cast` | 125 | 1 × `minecraft:enchanted_golden_apple` |
| 47 | ars-unification | Unification: A Knife Made of Magic | item · `farmersdelight:pumpkin_slice` | 35 | 2 × `farmersdelight:pumpkin_soup` |
| 48 | ars-unification | Unification: Prep More, Chase Less | item · `farmersdelight:cabbage_leaf` | 40 | 2 × `farmersdelight:cabbage_rolls` |
