# Primal Magick 6.0.7 — research, theorycrafting and spell registry census

**Evidence:** immutable source [PrimalMagick@5977468bda968b994e54066236ec5719295ac9c3](https://github.com/daedalus4096/PrimalMagick/tree/5977468bda968b994e54066236ec5719295ac9c3); TMM exact selected `primalmagick-neoforge-1.21.1-6.0.7.jar` (CurseForge 545271/file 6677090, SHA-1 `165a849fe2b95355067ede9cda014dec08129731`). **Source-pinned only, not binary-verified**, and no client/pack test. This complements the [existing deep profile](../../mod-comprehension/mods/primal-magick.md), without editing it.

## Source-complete list: 298 research entries

[ResearchEntries.java](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/research/ResearchEntries.java) defines **298 distinct** `ResearchEntry` keys and the same **298 actual `register(context, ...)` calls**. The headings match source section comments; entries are `primalmagick:<listed_id>` **research keys**, not FTB Quest IDs and not all mandatory.

### Fundamentals (25)

`first_steps`, `theorycrafting`, `attunements`, `linguistics`, `unlock_manaweaving`, `unlock_alchemy`, `unlock_sorcery`, `unlock_runeworking`, `unlock_ritual`, `unlock_magitech`, `terrestrial_magick`, `source_earth`, `source_sea`, `source_sky`, `source_sun`, `source_moon`, `forbidden_magick`, `source_blood`, `source_infernal`, `source_void`, `heavenly_magick`, `source_hallowed`, `secrets_of_the_universe`, `complete_basics`, `theory_of_everything`

### Manaweaving (51)

`basic_manaweaving`, `expert_manaweaving`, `master_manaweaving`, `supreme_manaweaving`, `complete_manaweaving`, `mana_arrows`, `wand_charger`, `mana_salts`, `advanced_wandmaking`, `staves`, `earthshatter_hammer`, `sunlamp`, `imbued_wool`, `spellcloth`, `hexweave`, `saintswool`, `artificial_mana_fonts`, `forbidden_mana_fonts`, `heavenly_mana_fonts`, `essence_cask_enchanted`, `essence_cask_forbidden`, `essence_cask_heavenly`, `wand_glamour_table`, `attunement_shackles`, `pixie_houses`, `spelltome_apprentice`, `spelltome_adept`, `spelltome_wizard`, `spelltome_archmage`, `mana_orb_apprentice`, `mana_orb_adept`, `mana_orb_wizard`, `mana_orb_archmage`, `wand_core_heartwood`, `wand_core_obsidian`, `wand_core_coral`, `wand_core_bamboo`, `wand_core_sunwood`, `wand_core_moonwood`, `wand_core_bone`, `wand_core_blaze_rod`, `wand_core_purpur`, `wand_cap_iron`, `wand_cap_gold`, `wand_cap_primalite`, `wand_cap_hexium`, `wand_cap_hallowsteel`, `wand_gem_apprentice`, `wand_gem_adept`, `wand_gem_wizard`, `wand_gem_archmage`

### Alchemy (32)

`basic_alchemy`, `expert_alchemy`, `master_alchemy`, `supreme_alchemy`, `complete_alchemy`, `stonemelding`, `skyglass`, `shard_synthesis`, `shard_desynthesis`, `primalite`, `crystal_synthesis`, `crystal_desynthesis`, `hexium`, `cluster_synthesis`, `cluster_desynthesis`, `hallowsteel`, `calcinator_basic`, `calcinator_enchanted`, `calcinator_forbidden`, `calcinator_heavenly`, `cryotreatment`, `sanguine_crucible`, `sanguine_core_land_animals`, `sanguine_core_sea_creatures`, `sanguine_core_flying_creatures`, `sanguine_core_plants`, `sanguine_core_undead`, `sanguine_core_sapients`, `sanguine_core_demons`, `sanguine_core_aliens`, `ignyx`, `synthetic_gem_buds`

### Sorcery (35)

`basic_sorcery`, `expert_sorcery`, `master_sorcery`, `supreme_sorcery`, `complete_sorcery`, `wand_inscription`, `spell_vehicle_projectile`, `spell_vehicle_bolt`, `spell_payload_frost`, `spell_payload_lightning`, `spell_payload_solar`, `spell_payload_lunar`, `spell_payload_blood`, `spell_payload_flame`, `spell_payload_void`, `spell_payload_holy`, `spell_payload_break`, `spell_payload_conjure_stone`, `spell_payload_conjure_water`, `spell_payload_shear`, `spell_payload_flight`, `spell_payload_conjure_light`, `spell_payload_healing`, `spell_payload_polymorph`, `spell_payload_polymorph_sheep`, `spell_payload_conjure_animal`, `spell_payload_conjure_lava`, `spell_payload_drain_soul`, `spell_payload_teleport`, `spell_payload_consecrate`, `spell_mod_amplify`, `spell_mod_burst`, `spell_mod_quicken`, `spell_mod_mine`, `spell_mod_fork`

### Runeworking (30)

`basic_runeworking`, `expert_runeworking`, `master_runeworking`, `supreme_runeworking`, `complete_runeworking`, `rune_earth`, `rune_sea`, `rune_sky`, `rune_sun`, `rune_moon`, `rune_blood`, `rune_infernal`, `rune_void`, `rune_hallowed`, `rune_project`, `rune_protect`, `rune_absorb`, `rune_dispel`, `rune_summon`, `rune_item`, `rune_self`, `rune_creature`, `rune_area`, `rune_insight`, `rune_power`, `rune_grace`, `runic_grindstone`, `recall_stone`, `runic_trim`, `enderward`

### Ritual Magick (38)

`basic_ritual`, `expert_ritual`, `master_ritual`, `supreme_ritual`, `complete_ritual`, `ritual_candles`, `incense_brazier`, `manafruit`, `ritual_lectern`, `ritual_bell`, `bloodletter`, `soul_anvil`, `celestial_harp`, `wand_core_primal`, `wand_core_dark_primal`, `wand_core_pure_primal`, `pixies`, `grand_pixies`, `majestic_pixies`, `ambrosia`, `greater_ambrosia`, `supreme_ambrosia`, `flying_carpet`, `cleansing_rite`, `primal_shovel`, `primal_fishing_rod`, `primal_axe`, `primal_hoe`, `primal_pickaxe`, `forbidden_trident`, `forbidden_bow`, `forbidden_sword`, `sacred_shield`, `dream_vision_talisman`, `dowsing_rod`, `hydromelon`, `blood_rose`, `emberflower`

### Magitech (32)

`basic_magitech`, `expert_magitech`, `master_magitech`, `supreme_magitech`, `complete_magitech`, `honey_extractor`, `seascribe_pen`, `arcanometer`, `primalite_golem`, `hexium_golem`, `hallowsteel_golem`, `concocting_tinctures`, `concocting_philters`, `concocting_elixirs`, `concocting_bombs`, `entropy_sink`, `auto_charger`, `essence_transmuter`, `dissolution_chamber`, `zephyr_engine`, `void_turbine`, `infernal_furnace`, `mana_nexus`, `mana_singularity`, `warding_module`, `greater_warding_module`, `supreme_warding_module`, `mana_networking`, `advanced_mana_networking`, `forbidden_mana_networking`, `heavenly_mana_networking`, `desalinator`

### Scans (17)

`raw_marble`, `rock_salt`, `quartz`, `sunwood_trees`, `moonwood_trees`, `hallowed_orb`, `hallowood_trees`, `bookshelf`, `beehive`, `beacon`, `dragon_egg`, `dragon_head`, `inner_demon`, `treefolk`, `alchemical_waste`, `mystical_relic`, `humming_artifact`

### Internal (38)

`unlock_scans`, `unlock_rune_enchantments`, `discover_blood`, `discover_infernal`, `discover_void`, `discover_forbidden`, `discover_hallowed`, `env_earth`, `env_sea`, `env_sky`, `env_sun`, `env_moon`, `sotu_discover_blood`, `sotu_discover_infernal`, `sotu_discover_void`, `sotu_research_arcanometer`, `sotu_research_hexium`, `sotu_research_power_rune`, `sotu_research_sanguine_crucible`, `sotu_research_cleansing_rite`, `sotu_scan_hallowed_orb`, `scan_primalite`, `scan_hexium`, `scan_hallowsteel`, `drown_a_little`, `near_death_experience`, `furry_friend`, `breed_animal`, `feel_the_burn`, `scan_nether_star`, `scan_flying_creature`, `scan_golem`, `wand_transform_hint`, `found_shrine`, `got_dream`, `siphon_prompt`, `unknown_rune`, `unknown_research`


Important special flags: `found_shrine`, `got_dream`, `siphon_prompt`, `unknown_rune` and `unknown_research` are internal. `complete_basics`, six discipline finales and `theory_of_everything` use hidden/finale semantics. Do not award 298 quests for these registry entries.

## Source-complete list: 38 Research Table project templates

[ProjectTemplates.java](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/theorycrafting/ProjectTemplates.java) contains **38 unique keys / 38 bootstrap registrations**, each `primalmagick:<id>`:

`advanced_enchanting_studies`, `advanced_essence_analysis`, `advanced_ritual_practice`, `advanced_runework`, `advanced_spellwork`, `advanced_wand_tinkering`, `apiamancy`, `beacon_emanations`, `brewing_experiments`, `conduit_forces`, `draconic_energies`, `draconic_memories`, `enchanting_studies`, `end_expedition`, `essence_analysis`, `expedition`, `hit_the_books`, `magitech_tinkering`, `master_enchanting_studies`, `master_essence_analysis`, `master_ritual_practice`, `master_runework`, `master_spellwork`, `mundane_tinkering`, `nether_expedition`, `observation_analysis`, `piglin_barter`, `portal_detritus`, `prosperous_trade`, `raiding_the_raiders`, `recuperation`, `redstone_tinkering`, `rich_trade`, `ritual_practice`, `runework`, `spellwork`, `trade`, `wand_tinkering`

[ProjectTemplate.java](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/theorycrafting/ProjectTemplate.java) permits player research requirements, material choices with weights, nearby aid blocks, alternate rewards, and optional success/material-count overrides; default **MAX_MATERIALS = 4**. [Project.java](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/theorycrafting/Project.java) computes a chance from selected materials; selecting all valid offered materials reaches 100% *formula* chance, provided requirements are met. Material consumption behavior and XP/observation flags need focused confirmation; old research reported a possible mismatch. **Theory** and **Observation** are Primal Magick knowledge currencies, not TMM MXP.

## Eight disciplines; four formal research tiers

[ResearchDisciplines.java](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/research/ResearchDisciplines.java) registers `basics`, `manaweaving`, `alchemy`, `sorcery`, `runeworking`, `ritual`, `magitech`, `scans`. The first seven have the Grimoire index sort order; `scans` is registered but **not listed as an ordinary main index discipline** by that method. Six specialist disciplines use actual `unlock_<id>` research.

[ResearchTier.java](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/research/ResearchTier.java) registers **Basic, Expert, Master, Supreme**, with source `defaultExpertise/defaultBonusExpertise` values **1/4, 5/20, 25/100, 125/500**. Those two fields are not a single global player level requirement; individual entries impose `requiredExpertise`, parent research and other criteria.

Actual cross-discipline unlock parents in [ResearchEntries.java](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/research/ResearchEntries.java):

| Unlock key | Must first learn |
| --- | --- |
| `unlock_manaweaving` | `first_steps` |
| `unlock_alchemy` | `mana_arrows` |
| `unlock_sorcery` | `wand_charger` |
| `unlock_runeworking` | `calcinator_basic` |
| `unlock_ritual` | `wand_inscription` **and** `rune_project` |
| `unlock_magitech` | `manafruit` **and** `mana_salts` |

Each unlock's first stage asks for **one Observation**. The authoring spine must respect these unlock dependencies. `theory_of_everything` is a real hidden end-state covering seven discipline finales and **all nine Source attunements at level two**; it is not an ordinary early recipe.

## Nine typed magical Sources, and composable spells

[Sources.java](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/sources/Sources.java) defines Earth/Sea/Sky/Sun/Moon (five initial terrestrial), Blood/Infernal/Void (discoverable forbidden group), and Hallowed (late heavenly group). Each has its own `primalmagick:` ID. [IManaContainer.java](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/wands/IManaContainer.java) defines **centimana = 1/100 displayed mana**, and [ManaManager.java](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/wands/ManaManager.java) tracks source-dependent mana across item/player supply routes. This is not Ars Source, Iron's Mana, FE, Neo Vitae EV or a convertible TMM universal pool without a separately implemented bridge.

| Spell family | Exact source declarations | Count incl. `empty` |
| --- | --- | ---: |
| [Delivery/vehicle](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/spells/vehicles/SpellVehiclesPM.java) | `empty`, `touch`, `projectile`, `bolt`, `self` | 5 |
| [Effect/payload](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/spells/payloads/SpellPayloadsPM.java) | `empty`, `earth_damage`, `frost_damage`, `lightning_damage`, `solar_damage`, `lunar_damage`, `blood_damage`, `flame_damage`, `void_damage`, `holy_damage`, `break`, `conjure_stone`, `conjure_water`, `shear`, `flight`, `healing`, `conjure_light`, `polymorph_wolf`, `polymorph_sheep`, `conjure_animal`, `conjure_lava`, `drain_soul`, `teleport`, `consecrate` | 24 |
| [Modifier](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/spells/mods/SpellModsPM.java) | `empty`, `amplify`, `burst`, `quicken`, `mine`, `fork` | 6 |

[SpellPackage.java](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/spells/SpellPackage.java) is **one vehicle + one payload + zero to two modifiers**, producing many composite spells. Type registration is not proof of beginner availability; component requirement suppliers still filter by player research. The three `empty` entries are sentinels, not real player skills. The stored package's default base cooldown is **30 ticks** before other calculations/modifiers; not all final casts are 30 ticks.

## Definitive first-player teaching boundaries

[InitRecipes.java](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/init/InitRecipes.java) registers wand transformations: a suitable **bookshelf** → `primalmagick:grimoire` after `got_dream` or appropriate `first_steps` stage; a tagged crafting table → `primalmagick:arcane_workbench` at `first_steps` stage; a tagged furnace → `primalmagick:essence_furnace` at `basic_alchemy` stage. These are **wand interactions**, not shapeless recipes.

`first_steps` stage events: craft Arcane Workbench, accrue **10 `mana_siphoned`** stat, complete **1 observation**, then additional introductory recipes. [PlayerEvents.java](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/events/PlayerEvents.java) grants `got_dream` and a real Dream Journal only upon waking **after `found_shrine`**, not every night. [GrimoireItem.java](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/items/misc/GrimoireItem.java) opens its research interface on use. [MundaneWandItem.java](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/items/wands/MundaneWandItem.java) stores **2500 centimana (25 display mana)**, siphons 100 centimana per action interval, and **cannot hold/cast spells**. Do not ask a new player to cast from a starter Mundane Wand.

## Next source work

**Second pass completed source inventories**: [298 entry parents + 501 stage predicates](primal-magick-6.0.7-research-stage-graph.md), [38 project templates with all 443 weighted material options](primal-magick-6.0.7-theorycraft-projects.md), [all 76 generated base Ritual recipes with ingredient/prop/Mana/instability fields](primal-magick-6.0.7-ritual-recipes.md), and [Wand slots/caps/gems + Mana Font/Injector/Relay figures](primal-magick-6.0.7-mana-and-wands.md). The named-registries census here remains the quick lookup, not a repeat of those full tables. **Remaining**: exact non-Ritual recipe generators, complex Grimoire text/JEI and spell numerical effects, active TMM datapack/EMC/creative overlay, and published 6.0.7 JAR/runtime behavior. In-game tests remain separately owned.
