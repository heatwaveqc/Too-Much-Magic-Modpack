# Ars Nouveau source pilot crosswalk, 2026-11-18

[Parent](ars-nouveau.md). Read-only audit of local `manifest/ars_quest_pilot.json`, canonical `manifest/quest_content.json`, resolved-ID manifest, generated `config/ftbquests/quests/chapters/ars_nouveau.snbt`, `scripts/build_ars_quest_pilot.py`/validator and custom Chimera advancement. Latest fetched integration `2f662b9e8b73cee47a1e23bf6d1f3510dd41d8ae` compared directly. No quest/runtime modifications or game launch.

## Actual source state and limits

**Forty-nine pilot nodes are already physically present in this research checkout and fetched integration source.** Old parent language saying PR74 is the only location and chapter isn't integrated is obsolete. Git history attributes local manifest to821bca15. PR74 is now CLOSED, mergedAt=null, head6af5ed27b53c33a153e0a7c4c2d2266f70e5b9f2, base feature/quest-reward-humor-first-pass; closure isn't evidence of a merge. Source presence is established independently by checked-in files, not inferred from PR state. Final assembled runtime/client acceptance remains separate.

Comparison of local versus fetched integration pilot JSON: same49rows and all non-position fields;48rows differ only in coordinates. Preserve current integration layout. Do not regenerate/replay stale quest trees from research branch. This report maps semantic fields, not a proposal to restore older positions.

Canonical and resolved manifests each contain49Ars rows. Criteria:31item,17advancement,1orientation checkmark. All49grant direct XP (2750total);48also fixed item rewards. Orientation5XP only. Rewards are personal according to generated declarations/pilot policy; actual team/claim persistence untested. Validator PASS:49quests,7/7addons, one justified orientation, acyclic branches, personal fixed rewards; it reports repeated payout IDs across whole book for editorial review. This is source validation, not operation credit or per-player acceptance.

Historical validation receipt is2026-11-15Linux/Java21/NeoForge21.1.265 on148pin quest-source branch,49Ars nodes and93item/17advancement registry probe references. It explicitly excludes graphical/multiplayer action/claims and differs from rolling integration. Do not use that receipt as current pack gameplay PASS.

## Every node: stable identity, actual criterion and teaching stage

Exact IDs below come from resolved manifest and are checked against generated chapter. Item tasks establish possession; eleven native Ars advancements below also use inventory_changed and therefore establish possession, **not operation**. Native familiar has a custom trigger; four Ars 'n Spells advancement meanings still require exact addon audit. Chimera custom advancement uses vanilla player_killed_entity for `ars_nouveau:wilden_boss`, not a ritual-activation/material-payment detector. Persistent advancement credit can predate quest visibility.

| # / key | Stable quest ID | Criterion | Native/addon teaching reference | Fixed reward after XP |
| --- | --- | --- | --- | --- |
| 0 / orientation | 544D303000100001 | checkmark | [orientation](ars-nouveau-pinned-casting.md) | 5XP; no item |
| 1 / novice | 544DB13000010000 | advancement: ars_nouveau:novice_spell_book; minecraft:inventory_changed | [novice](ars-nouveau-pinned-casting.md) | 20XP; ars_nouveau:source_berry_roll x2 |
| 2 / chamber | 544DB13000020000 | item: ars_nouveau:imbuement_chamber x1; possession | [chamber](ars-nouveau-pinned-crafting.md) | 25XP; ars_nouveau:source_gem x4 |
| 3 / scribe | 544DB13000030000 | item: ars_nouveau:scribes_table x1; possession | [scribe](ars-nouveau-pinned-guide-glyphs.md) | 25XP; minecraft:lapis_lazuli x6 |
| 4 / jar | 544DB13000040000 | item: ars_nouveau:source_jar x1; possession | [jar](ars-nouveau-pinned-source.md) | 20XP; minecraft:blue_stained_glass x8 |
| 5 / agro | 544DB13000050000 | item: ars_nouveau:agronomic_sourcelink x1; possession | [agro](ars-nouveau-pinned-source.md) | 35XP; minecraft:bone_meal x12 |
| 6 / relay | 544DB13000060000 | item: ars_nouveau:relay x1; possession | [relay](ars-nouveau-pinned-source.md) | 30XP; minecraft:lever x2 |
| 7 / apparatus | 544DB13000070000 | item: ars_nouveau:enchanting_apparatus x1; possession | [apparatus](ars-nouveau-pinned-crafting.md) | 50XP; ars_nouveau:arcane_pedestal x8 |
| 8 / magebloom | 544DB13000080000 | item: ars_nouveau:magebloom_crop x1; possession | [magebloom](ars-nouveau-pinned-plants.md) | 30XP; minecraft:string x12 |
| 9 / mage | 544DB13000090000 | advancement: ars_nouveau:apprentice_spell_book; minecraft:inventory_changed | [mage](ars-nouveau-pinned-casting.md) | 75XP; ars_nouveau:air_essence x2 |
| 10 / brazier | 544DB130000A0000 | item: ars_nouveau:ritual_brazier x1; possession | [brazier](ars-nouveau-pinned-rituals.md) | 40XP; minecraft:soul_lantern x4 |
| 11 / summon | 544DB130000B0000 | item: ars_nouveau:ritual_wilden_summon x1; possession | [summon](ars-nouveau-pinned-rituals.md) | 60XP; minecraft:cooked_beef x8 |
| 12 / chimera | 544DB130000C0000 | advancement: tmm:ars_pilot/chimera; player kill | [chimera](ars-nouveau-pinned-rituals.md) | 200XP; ars_nouveau:music_disc_firel_the_wild_hunt x1 |
| 13 / archmage | 544DB130000D0000 | advancement: ars_nouveau:archmage_spell_book; minecraft:inventory_changed | [archmage](ars-nouveau-pinned-casting.md) | 250XP; ars_nouveau:thread_spellpower x1 |
| 14 / robes | 544DB130000E0000 | item: ars_nouveau:arcanist_robes x1; possession | [robes](ars-nouveau-pinned-armor-threads.md) | 35XP; minecraft:purple_dye x4 |
| 15 / alteration | 544DB130000F0000 | item: ars_nouveau:alteration_table x1; possession | [alteration](ars-nouveau-pinned-armor-threads.md) | 40XP; ars_nouveau:blank_thread x2 |
| 16 / starby | 544DB13000100000 | advancement: ars_nouveau:starby_charm; minecraft:inventory_changed | [starby](ars-nouveau-pinned-starbuncle.md) | 35XP; ars_nouveau:starbuncle_shades x1 |
| 17 / whirl | 544DB13000110000 | advancement: ars_nouveau:whirlisprig_charm; minecraft:inventory_changed | [whirl](ars-nouveau-pinned-whirlisprig.md) | 40XP; minecraft:flowering_azalea x4 |
| 18 / drygmy | 544DB13000120000 | advancement: ars_nouveau:drygmy_charm; minecraft:inventory_changed | [drygmy](ars-nouveau-pinned-drygmy.md) | 50XP; minecraft:mossy_cobblestone x8 |
| 19 / wixie | 544DB13000130000 | advancement: ars_nouveau:wixie_charm; minecraft:inventory_changed | [wixie](ars-nouveau-pinned-wixie.md) | 40XP; minecraft:nether_wart x8 |
| 20 / familiar | 544DB13000140000 | advancement: ars_nouveau:familiar; ars_nouveau:familiar | [familiar](ars-nouveau-pinned-familiars.md) | 75XP; ars_nouveau:source_berry_pie x3 |
| 21 / turret | 544DB13000150000 | advancement: ars_nouveau:basic_spell_turret; minecraft:inventory_changed | [turret](ars-nouveau-pinned-turrets.md) | 45XP; minecraft:daylight_detector x1 |
| 22 / lectern | 544DB13000160000 | item: ars_nouveau:storage_lectern x1; possession | [lectern](ars-nouveau-pinned-bookwyrm-storage.md) | 45XP; ars_nouveau:archwood_sign x4 |
| 23 / repository | 544DB13000170000 | item: ars_nouveau:repository x1; possession | [repository](ars-nouveau-pinned-bookwyrm-storage.md) | 35XP; minecraft:name_tag x2 |
| 24 / warp | 544DB13000180000 | advancement: ars_nouveau:warp_scroll; minecraft:inventory_changed | [warp](ars-nouveau-pinned-warp-portals.md) | 35XP; minecraft:glow_item_frame x2 |
| 25 / flight | 544DB13000190000 | item: ars_nouveau:ritual_flight x1; possession | [flight](ars-nouveau-pinned-rituals.md) | 70XP; minecraft:hay_block x8 |
| 26 / mirror | 544DB130001A0000 | item: ars_nouveau:enchanters_mirror x1; possession | [mirror](ars-nouveau-pinned-enchanter-tools.md) | 45XP; minecraft:feather x12 |
| 27 / flask | 544DB130001B0000 | advancement: ars_nouveau:potion_flask; minecraft:inventory_changed | [flask](ars-nouveau-pinned-potion-devices.md) | 40XP; minecraft:glistering_melon_slice x4 |
| 28 / golem | 544DB130001C0000 | advancement: ars_nouveau:amethyst_golem_charm; minecraft:inventory_changed | [golem](ars-nouveau-pinned-amethyst-golem.md) | 45XP; minecraft:calcite x16 |
| 29 / add_wand | 544DB130001D0000 | item: ars_additions:advanced_dominion_wand x1; possession | [ars-additions](ars-additions.md); full addon route pending | 45XP; minecraft:glow_ink_sac x4 |
| 30 / add_index | 544DB130001E0000 | item: ars_additions:warp_index x1; possession | [ars-additions](ars-additions.md); full addon route pending | 60XP; ars_nouveau:archwood_hanging_sign x2 |
| 31 / add_ender | 544DB130001F0000 | item: ars_additions:ender_source_jar x1; possession | [ars-additions](ars-additions.md); full addon route pending | 75XP; minecraft:ender_eye x2 |
| 32 / control_remote | 544DB13000200000 | item: ars_controle:remote x1; possession | [ars-controle](ars-controle.md); addon source route complete, operation acceptance pending | 40XP; minecraft:redstone_torch x4 |
| 33 / control_sensor | 544DB13000210000 | item: ars_controle:temporal_stability_sensor x1; possession | [ars-controle](ars-controle.md); addon source route complete, operation acceptance pending | 50XP; minecraft:repeater x4 |
| 34 / control_brazier | 544DB13000220000 | item: ars_controle:portable_brazier_relay x1; possession | [ars-controle](ars-controle.md); addon source route complete, operation acceptance pending | 65XP; minecraft:firework_rocket x8 |
| 35 / element_focus | 544DB13000230000 | item: ars_elemental:lesser_fire_focus x1; possession | [ars-elemental](ars-elemental.md); full addon route pending | 45XP; minecraft:magma_cream x4 |
| 36 / element_urn | 544DB13000240000 | item: ars_elemental:everfull_urn x1; possession | [ars-elemental](ars-elemental.md); full addon route pending | 55XP; minecraft:cauldron x1 |
| 37 / element_hat | 544DB13000250000 | item: ars_elemental:fire_hat x1; possession | [ars-elemental](ars-elemental.md); full addon route pending | 60XP; minecraft:orange_dye x4 |
| 38 / elemancy_essence | 544DB13000260000 | item: ars_elemancy:cinder_essence x1; possession | [ars-elemancy](ars-elemancy.md); full addon route pending | 60XP; ars_nouveau:manipulation_essence x2 |
| 39 / elemancy_hat | 544DB13000270000 | item: ars_elemancy:cinder_hat x1; possession | [ars-elemancy](ars-elemancy.md); full addon route pending | 100XP; minecraft:fire_charge x8 |
| 40 / ocultas_gem | 544DB13000280000 | item: occultism:spirit_attuned_gem x1; possession | [ars-ocultas](ars-ocultas.md); full addon route pending | 40XP; minecraft:purple_candle x4 |
| 41 / ocultas_jar | 544DB13000290000 | item: ars_nouveau:mob_jar x1; possession | [ars-ocultas](ars-ocultas.md); full addon route pending | 55XP; minecraft:hopper x2 |
| 42 / ocultas_altar | 544DB130002A0000 | item: ars_ocultas:altar x1; possession | [ars-ocultas](ars-ocultas.md); full addon route pending | 80XP; minecraft:crying_obsidian x4 |
| 43 / ans_loom | 544DB130002B0000 | advancement: ars_n_spells:craft_spell_loom; inventory possession | [pinned carrier audit](ars-n-spells-pinned-carriers.md); addon source route complete, operation acceptance pending | 45XP; ars_n_spells:blank_scroll x3 |
| 44 / ans_transcribe | 544DB130002C0000 | advancement: ars_n_spells:transcribe_spell; successful Loom transaction; not generic ritual | [pinned carrier audit](ars-n-spells-pinned-carriers.md); addon source route complete, operation acceptance pending | 60XP; irons_spellbooks:arcane_essence x8 |
| 45 / ans_bind | 544DB130002D0000 | advancement: ars_n_spells:bind_spell; successful shared bind; ritual/Table/command | [pinned carrier audit](ars-n-spells-pinned-carriers.md); addon source route complete, operation acceptance pending | 75XP; ars_nouveau:source_berry_roll x4 |
| 46 / ans_cast | 544DB130002E0000 | advancement: ars_n_spells:first_cross_cast; server accepted cast; not effect/damage proof | [pinned carrier audit](ars-n-spells-pinned-carriers.md); addon source route complete, operation acceptance pending | 125XP; minecraft:enchanted_golden_apple x1 |
| 47 / unify_pumpkin | 544DB130002F0000 | item: farmersdelight:pumpkin_slice x4; possession | [ars-unification](ars-unification.md); addon source route complete, operation acceptance pending | 35XP; farmersdelight:pumpkin_soup x2 |
| 48 / unify_cabbage | 544DB13000300000 | item: farmersdelight:cabbage_leaf x8; possession | [ars-unification](ars-unification.md); addon source route complete, operation acceptance pending | 40XP; farmersdelight:cabbage_rolls x2 |

## Coverage gaps against the researched full route

The49node pilot is a useful acquisition spine plus selected options. It is **not a complete start-to-end teaching surface** merely because it mentions every installed addon.

| Full native walkthrough requirement | Pilot mapping / gap | Research handoff |
| --- | --- | --- |
| Obtain/open Worn Notebook and find named pages | No dedicated Notebook obtain/open node. Orientation and novice don't replace actual guide acquisition. | Add explicit Book+Lapis Notebook instructions and page names; item task can only prove item, not reading. |
| First spell, mana and glyph acquisition | Novice advancement is inventory; Scribe node detects table, not XP/material payment or learned glyph. | Teach starter glyphs, select/save/cast, XP-point learning and consume Glyph. Actual operation needs supported detector/manual observation, not renamed item task. |
| Processing and Source flow | Chamber/Jar/Agronomic/Relay/Apparatus detect blocks; Chamber's4Source Gem reward can precede actual processing. | Teach passive first Source Gem, reusable chamber catalysts versus consumed apparatus pedestals, actual layout/range and output. No Sourceflow from possession alone. |
| Book tiers | Native inventory advancements allow acquired book; mage prose says more combination room, while exact casting companion distinguishes tier acceptance, ten stored slots and ten base composition links. | Reconcile capacity wording and upgrade ingredients; preserve actual item/progression checks. |
| Boss/terminal Archmage | Tablet node item only; kill advancement real player-kill condition but doesn't prove Summon Wilden augments/payment; Archmage inventory doesn't certify learning Tier3glyph. | Teach safe tablet activation/materials, fight, Tribute/upgrade, chosen Tier3spell. No boss drop reward bypass. |
| Workers/familiars | Charm advancements mostly inventory, not deployed/home-bound/paid worker. Familiar custom trigger separate. | Each worker needs acquisition/placement/control/upkeep/output/recovery instructions from individual companion. |
| Optional production and storage | Lectern/Repository acquisition present; no full Bookwyrm binding/catalog capacity lesson or all Source producer branches. | Native branches need relevant guide/JEI sections and practical operation. |
| Equipment and potion choices | Robes/Alteration/Mirror/Flask partial. Mirror prose doesn't explicitly say effects-only inscription; no full thread upgrade/other tool/archery/curio/knowledge-transfer route. | Offer optional subsystem choices with actual controls; don't require every recipe or call Self-leading ordinary spell valid Mirror input. |
| Advanced building, travel and control | Warp acquisition and Flight tablet present, but no full Stable/portal/Planarium, detectors/routing, Containment24roles, utility jars, Chalk/grate route. | Teach major options through native guide and complete local steps; possession tasks never certify actual effect. |
| Seven addons |20nodes mention all7, mostly equipment/output items; Additions3, Controle3, Elemental3, Elemancy2, Ocultas3, 'n Spells4, Unification2. | Exact addon routes and enabled bridge ends remain to audit. Existing items for Unification don't prove Cut processing; Ocultas Jar/Altar don't prove bound/paid operations. |

These are documentation/quest-design findings, not authorization to edit authored quests. Safe future directions: retain meaningful XP and fixed practical supplies, provide named guide/JEI build references, distinguish operation detectors from inventory proxies, and avoid rewards auto-completing another node. Current validator checks narrow gift-loop/criterion rules, not whether all native guide branches have been taught.

## Read-only validation and next work

Ran `python scripts/validate_ars_quest_pilot.py` successfully on research checkout; no --jars mode, assembly, server/client or gameplay. Data extraction here asserted49resolved IDs exist in chapter, inspected all12base-Ars advancement JSONs and current custom kill criterion, counted criteria/rewards, and compared fetched/local pilot JSON semantics. No automatic writer invoked. Next: parent native route consolidation and seven-addon exact artifact audit, then final complete coverage decision. Effective branch layout and integration ownership stay with integration writer.
