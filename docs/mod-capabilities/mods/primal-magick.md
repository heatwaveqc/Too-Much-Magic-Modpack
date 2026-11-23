# Primal Magick 6.0.7 — 29-axis capability Atlas, first source-backed pass

> **Status: SOURCE-BACKED PARTIAL, not quest-source-ready.** In this first pass all 29 capability axes are navigable, but research costs, native ingredient graphs, 38 project material rules, and the spell/ritual/equipment end states still require a second source pass. This is **not** a client-gameplay PASS or a claim that published JAR contents were independently inspected.

| Provenance | Exact selected input |
| --- | --- |
| Pack | TMM 1.21.1 NeoForge, `primalmagick` |
| Selected JAR | `primalmagick-neoforge-1.21.1-6.0.7.jar`; CurseForge project 545271/file 6677090; SHA-1 `165a849fe2b95355067ede9cda014dec08129731` |
| Official version-aligned source | [PrimalMagick@5977468bda968b994e54066236ec5719295ac9c3](https://github.com/daedalus4096/PrimalMagick/tree/5977468bda968b994e54066236ec5719295ac9c3) |
| Existing prior research | [Primal Magick full Mod Comprehension profile](../../mod-comprehension/mods/primal-magick.md), including deep Research Table and Ritual Altar source analysis |
| New exact registries | [298 research entries, 38 templates, 35 spell primitives, 8 disciplines and 9 Sources](../inventories/primal-magick-6.0.7-research-and-spells.md) |
| Source code generation vs pinned client | Source ref matches tag 6.0.7; JAR contents, runtime default configs, JEI/Grimoire and first-party overrides not verified |

## 1. Actual gameplay identity and first player loop

Primal Magick combines **discovery/observation → staged player research → typed Mana → wand/spell/component crafting → alchemy, runes, rituals and magitech**. Its progression is not merely a checklist of completed crafts: a player can hold an item without having the Grimoire research stage that unlocks its recipe. Source defines **298** registered research entries grouped into Basics, six specialist disciplines, scans and internal state; **38** weighted Research Table projects; nine typed Source elements; a composable spell package; and four expertise/research tiers. [ResearchEntries.java](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/research/ResearchEntries.java), [ResearchDisciplines.java](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/research/ResearchDisciplines.java), [ProjectTemplates.java](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/theorycrafting/ProjectTemplates.java).

**Source-backed first-play instructions, with honest unknowns:**

| Step | Player action and exact object | Why/what confirms it | Quest/QA boundary |
| --- | --- | --- | --- |
| 1 | Find an ancient Primal Magick **marble Shrine** and approach its central ancient Font/device | Native worldgen has five [Shrine variants](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/worldgen/structures/ShrineStructure.java); shrine discovery records internal `primalmagick:found_shrine` | Precise proximity trigger and structure ID need closer audit; do not reward sleeping alone |
| 2 | Sleep **after** Shrine discovery to receive the actual **Dream Journal** | [PlayerEvents.onWakeUp](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/events/PlayerEvents.java) checks `found_shrine` and, once, writes `got_dream` and gives a `dream_journal` static book | Honest detector is internal research state, not merely possessing any book |
| 3 | Gather Shrine-ground magical dust, craft the **Mundane Wand** (per native [official overview](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/README.md)) | [MundaneWandItem.java](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/items/wands/MundaneWandItem.java) holds 2500 centimana (25 shown Mana), channels 100 centimana at a time and **holds zero spells** | Exact two-item recipe/which dust tag should be checked against generated recipe before final quest |
| 4 | Use Wand on suitable bookshelf to make/use `primalmagick:grimoire` | [InitRecipes.java](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/init/InitRecipes.java) has a bookshelf-tag wand transformation gated by Dream/First Steps; [GrimoireItem.java](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/items/misc/GrimoireItem.java) opens the research UI on use | Do not claim all bookshelves are eligible; specifically tagged empty bookshelves are rejected |
| 5 | Wand-transform a crafting table into `primalmagick:arcane_workbench`; actually craft it | `first_steps` stage has `requiredCraft(ARCANE_WORKBENCH)`; native transformation is research-stage gated | Display/gate and actor credit to inspect; inventory task only proves item possession |
| 6 | Channel Mana from first **Ancient Mana Font** into Mundane Wand, then make **one Observation** with introductory analysis tools | `first_steps` requires stat `mana_siphoned=10` then `observations_made=1`, with Wood Table, Magnifying Glass and Analysis Table in stage recipes | Need in-game stat/scan event bridge; not the same as spending Mana or using another magic mod |
| 7 | Learn `primalmagick:theorycrafting`; craft **Research Table** and **Enchanted Ink & Quill**; complete first project | Exact ResearchEntries stages need one Observation, then crafted research tools, then `theories_formed=1` | Project success/actual Theory grant, not table possession |

**Important correction to a common shortcut:** the starter Mundane Wand cannot cast *any* spell. It must teach Font siphoning, source observation and transformations; modular spellcrafting belongs later in the Sorcery branch.

## 2. Research graph and actual native gates

[Source research entry bootstrap](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/research/ResearchEntries.java) links the six specialist disciplines to different **earlier** discoveries:

`first_steps` → `unlock_manaweaving`

`basic_manaweaving` → `mana_arrows` → `unlock_alchemy`; separately `basic_manaweaving` → `wand_charger` → `unlock_sorcery`

`basic_alchemy` → `calcinator_basic` → `unlock_runeworking`

`basic_sorcery` + advanced wandmaking → `wand_inscription` **and** `basic_runeworking` → `rune_project` → `unlock_ritual`

`basic_ritual` + `mana_salts` → `manafruit`, **and** `wand_charger` → `mana_salts` → `unlock_magitech`

These are **entry-level parent prerequisites** (each unlock also requests one Observation at a stage), not evidence that every prior research node must be completed in the external FTB chapter. Source has `complete_basics` and six specialization finale entries, plus hidden `theory_of_everything` requiring all seven main discipline finales and **attunement 2** in all nine Sources. `secrets_of_the_universe` is another hidden advanced branch with several special research/scan conditions and **Hallowed attunement 4**. See [named entry census](../inventories/primal-magick-6.0.7-research-and-spells.md).

**Formal tiers:** Basic, Expert, Master, Supreme. Individual `ResearchEntries` specify research **parents**, stage objectives, expertise requirements and Source-discovery prerequisites; do not model this as a single linear cross-discipline level counter. [ResearchTier.java](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/research/ResearchTier.java).

## 3. Concrete workstation/equipment source inventory

Every ID below is from actual [BlocksPM.java](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/blocks/BlocksPM.java) or [ItemsPM.java](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/items/ItemsPM.java) literal registrations, **not** presumed obtainable at game start. Source declares **274 distinct block names and 660 item names**, including block-item forms and cosmetic variants; these are declaration counts, not 934 separate mechanics or complete recipe budgets.

| Source IDs (prefix `primalmagick:`) | Actual role and input/output category | Access / limit / first operation |
| --- | --- | --- |
| `arcane_workbench`, `wood_table`, `analysis_table`, `magnifying_glass`, `research_table` | First physical crafting, observation and later Research Table project cycle | Wand transformation and `first_steps` stages; Research Table also requires Enchanted Ink & Quill |
| `wand_assembly_table`, `wand_inscription_table`, `spellcrafting_altar` | Craft modular Wand with Core/Cap/Gem; create a spell package; inscribe useable Wand | Sorcery gated by `wand_charger`; Wand Inscription requires a previously crafted spell + Advanced Wandmaking |
| `essence_furnace`, `calcinator_basic`, `calcinator_enchanted`, `calcinator_forbidden`, `calcinator_heavenly` | Alchemy essence/extraction, tiered refining and synthesis | Furnace wand transform after `basic_alchemy`; higher calcinators require expertise and later materials |
| `runecarving_table`, `runescribing_altar_basic`, `runescribing_altar_enchanted`, `runescribing_altar_forbidden`, `runescribing_altar_heavenly`, `runic_grindstone` | Create rune components, inscribe rune effects and alter/repair rune systems | `basic_runeworking` after Calcination; higher altars later |
| `ritual_altar`, `offering_pedestal`, `salt_trail`, `incense_brazier`, `ritual_lectern`, `ritual_bell`, `bloodletter`, `soul_anvil`, `entropy_sink`, `celestial_harp` | Real multi-step salt-linked rituals and stabilization props | Require research, connected salt-network placement, recipe offerings and Source Mana; negative stability risks mishaps |
| `ancient_font_{earth,sea,sky,sun,moon}`, `artificial_font_*`, `forbidden_font_*`, `heavenly_font_*` | Typed Source-specific Mana generation/holding, wand siphoning, device supply | Native Ancient five in world; higher artificial/forbidden/heavenly Fonts need research and cost; exact rate/capacity to verify |
| `wand_charger`, `auto_charger`, `mana_relay_{basic,enchanted,forbidden,heavenly}`, `mana_injector_{basic,enchanted,forbidden,heavenly}`, `mana_nexus`, `mana_singularity` | Mana buffers, charge automation and relay/injector network | Magic-energy logistics only; exact tick rates/range and balance unverified |
| `concocter`, `sanguine_crucible`, `essence_transmuter`, `dissolution_chamber`, `infernal_furnace`, `desalinator`, `zephyr_engine`, `void_turbine` | Advanced brewing, essence/mob inputs, item transformations, magic machines and wind/void power | Higher Magitech/Sorcery/Alchemy research gates; exact recipes and performance in later pass |
| `essence_cask_{enchanted,forbidden,heavenly}`, `sunlamp`, `spirit_lantern`, `honey_extractor` | Essence storage and practical devices with separate non-FE uses | Distinguish Essence item storage from typed Mana reservoir and player-scoped knowledge |
| `primalite_golem_controller`, `hexium_golem_controller`, `hallowsteel_golem_controller` | Construct/golem control family | Source registers blocks; loyalty, summon cost, limitations and lifetime require review |

**Items/gear:** `mundane_wand`; nine named Wand Core options (Heartwood, Obsidian, Coral, Bamboo, Sunwood, Moonwood, Bone, Blaze Rod, Purpur), additional Primal/Dark/Pure core variants, five caps (Iron/Gold/Primalite/Hexium/Hallowsteel), four ordinary Wand Gems (Apprentice/Adept/Wizard/Archmage), staff counterparts, tiered Mana Orbs/Spell Tomes, Primalite/Hexium/Hallowsteel tool armor and shield branches, nine Source Runes/Attunement Shackles/Humming Artifacts, Pixie families and Sanguine Cores. Exact item IDs and recipe exposure should be read from [ItemsPM.java](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/items/ItemsPM.java) and individual class files before any FTB reward design.

## 4. Typed resource ledger — never conflate cross-mod energy

| Source/store scope | Type, first and higher access | Real consumption/limits |
| --- | --- | --- |
| **Nine typed Mana Sources** | `primalmagick:earth`, `sea`, `sky`, `sun`, `moon`, `blood`, `infernal`, `void`, `hallowed` | `IManaContainer` works in centimana, 100 = 1 displayed point; per-source costs and available pools matter |
| **Wand ItemStack** | Mundane first; modular core/cap/gem later | Mundane 2500 centimana capacity, siphon 100; **zero spell slots**. Modular wand mana efficiency and usable spell count depend on parts |
| **Mana Orb / Spell Tome ItemStack** | Source-defined Apprentice/Adept/Wizard/Archmage tiers | Portable typed Mana and spell capacity; exact maxima from classes and recipe unlock required |
| **World Font BlockEntity** | Native Ancient five, later player-built Artificial/Forbidden/Heavenly Fonts | Source-specific recharge, current charge, capacity, siphon, network supply. No native FE generation implied |
| **Mana network Buffer/route** | Wand Charger, Auto Charger, Injector/Relay, Mana Nexus and device inventories | Side/source/owner/range, route discovery, throughput and loaded chunks need further exact code audit |
| **Player research** | Observations, Theories and discipline-specific Expertise; research stage completions | Distinct from Mana, XP, and TMM MXP; `ResearchManager` persists player research; `TheorycraftManager` chooses templates |
| **Source Attunement** | Nine typed per-player attunements, source/gear/event bonuses | Explicit requirements in research, e.g. all nine level 2 for Theory of Everything; not currency to spend as Mana |
| **Ritual altar process** | Multiple typed Mana costs + item offerings, salt trail, prop state/stability | Stability clamped -100..25, loses `0.01×recipe instability` per evaluation plus excess salt penalty; mishap and cancellation risk; not free item conversion |

[Sources.java](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/sources/Sources.java), [ManaManager.java](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/wands/ManaManager.java), [ProjectTemplate.java](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/theorycrafting/ProjectTemplate.java), [RitualAltarTileEntity.java](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/tiles/rituals/RitualAltarTileEntity.java).

## 5. Spell system and operational effects

Source registers **5** vehicles (`empty`, `touch`, `projectile`, `bolt`, `self`), **24** payloads (nine damage Sources + break, flight, teleport, healing, polymorphs, conjurations and others), **6** modifiers (`empty`, `amplify`, `burst`, `quicken`, `mine`, `fork`). [Complete exact IDs](../inventories/primal-magick-6.0.7-research-and-spells.md). [SpellPackage.java](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/spells/SpellPackage.java) creates a **name + vehicle + payload + zero to two modifiers**; [SpellManager.java](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/spells/SpellManager.java) filters components by per-player research requirements.

The immediate first Sorcery path uses **`spellcrafting_altar` → crafted spell → `wand_inscription_table` → modular spell-casting item**, not an undocumented fixed list of spellbooks. Later payloads supply distinct axes (flight, healing, block breaking, polymorph, elemental damage, creation), but exact Mana/permission/area/lag safety require specialist pass. Do not invent a hard requirement to cast every payload to progress; they are available branches.

## 6. Rituals — structural truth vs recipe gaps

[Source RitualAltarTileEntity.java](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/tiles/rituals/RitualAltarTileEntity.java) scans a **connected, salt-powered** network of offering pedestals and props within altar-tier geometry and checks a **real matching ritual recipe** plus player research and sufficient typed Mana at start. It creates offering/prop steps, returns source recipe results on completion and awards Expertise. `stability` clamped to **[-100,25]**, drifts downward by **0.01×recipe instability**, also **0.001 per salt segment beyond allowed safe count**, plus symmetry/prop contributions. Negative stability permits mishaps including lost/dropped offerings, salt disruption, player damage and detonation; cancellation may provoke a mishap. [DowsingRodItem.java](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/items/misc/DowsingRodItem.java) gives altar stability, salt connectivity and symmetry diagnostics.

**Source-ready teaching example:** place Altar and connected salt trail, add the required exact Pedestals and props, use the Dowsing Rod to verify a stable layout, set the actual recipe's item offerings, activate with a Wand holding the required Sources, observe a completed output. **Specific first safe ritual recipe and amounts remain unverified; don't invent one or pay a quest for merely placing the altar.** Scan out the ritual generator and rare/unsafe outputs next.

## 7. World generation, equipment paths and ending

[Primal ShrineStructure.java](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/worldgen/structures/ShrineStructure.java) and [ShrinePiece.java](https://github.com/daedalus4096/PrimalMagick/blob/5977468bda968b994e54066236ec5719295ac9c3/common/src/main/java/com/verdantartifice/primalmagick/common/worldgen/structures/ShrinePiece.java) define five terrestrial Shrine types that anchor discovery. Other native biome/structure content includes magical woods, ores, Treefolk and Pixie systems. TMM uses Tectonic, so actual Overworld placement is a separate pack-owned QA case, not an automatic source fact.

**Late-game progression:** Source research includes Hexium/Hallowsteel materials, the forbidden Blood/Infernal/Void Sources, Heavenly/Hallowed, higher Sorcery and Ritual tiers, advanced Mana Fonts and networks, `secrets_of_the_universe`, and the seven-discipline finale `theory_of_everything`. **None** should automatically grant ProjectE, infinite FE or creative-only rewards; preserve owner TMM endgame decisions. `primalmagick:mana_singularity_creative` is a registered creative-class block name, but this is **not** an approved craftable survival reward; central creative-item [policy #596](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/596) applies.

## 8. All 29 capability axes (index, not replacement for the sections above)

The following table points to real source objects for every axis and explicitly limits unsupported capability claims.

| Axis | Named source objects/actions | Verified entry → advanced boundary / next evidence |
| --- | --- | --- |
| `gear` | Mundane Wand, modular wand cores/caps/gems; stave cores; Spell Tomes Apprentice→Archmage, Mana Orbs Apprentice→Archmage; Primalite→Hexium→Hallowsteel armor/tools, tridents/shields/bows, attunement shackles and humming artifacts | Mundane Wand cannot hold spells; stronger gear needs ResearchEntries and source materials; stats/recipes still unindexed |
| `movement` | Spell payloads `flight` and `teleport`; Sky attunement jump bonus (PlayerEvents); potential Recall Stone movement | Early spell availability requires Sorcery and researched payload; range/mana/cooldown and Recall Stone behavior not yet traced |
| `ore_processing` | `essence_furnace`, `calcinator_basic/en chanted/forbidden/heavenly` (actual ID `calcinator_enchanted`), `essence_transmuter`, `dissolution_chamber`, shard/crystal/cluster synthesis | Recipes/ore tags and exact multiplicative yields are not yet extracted; no assumption of ore doubling |
| `storage` | Essence Casks (enchanted/forbidden/heavenly); Mana Orbs, Mana Fonts and batteries are **magic-energy reservoirs**, not general backpacks | Cask item vs essence container semantics and capacity remain to trace; owner permits native magic-mod storage |
| `crafting` | `arcane_workbench`, `wand_assembly_table`, `spellcrafting_altar`, `wand_inscription_table`, `runecarving_table`, four-tier `runescribing_altar_*`, Ritual Altar, Concocter and Sanguine Crucible | Wand transforms for workbench/furnace are confirmed; individual ingredient recipes and research requirements need pass two |
| `logistics` | Typed Mana Injector `mana_injector_basic/enchanted/forbidden/heavenly`, Relay `mana_relay_*`, Mana Nexus; device item/hopper interfaces | Dedicated *magical energy routing*, not a new generic item pipe; native network distance/throughput to verify |
| `buffs` | Attunement-derived conditional attributes, rune/item effects, concoctions and spell payloads | Actual amplifiers, duration and stacking await effect/item implementation review |
| `external_compatibility` | Native Forge/NeoForge tags (`CommonTags`, ore/woods), modded item affinity derivation, JEI categories and device interfaces | Any TMM cross-mod MXP/FE/Essence integration is separate; no direct confirmed conversion to Ars/Iron's resources |
| `mob_farming` | `conjure_animal` payload, Golem Controller blocks, Pixie items, Sanguine Crucible and spell interactions | Golems/pixies exist; automated farm rates and drop mechanics unverified; do not claim general mob farm |
| `ore_generation` | Synthetic budding Amethyst/Diamond/Emerald/Quartz blocks and clusters; Rock Salt/Quartz ores appear in worldgen | Source registers physical blocks; cycle/regeneration timing and TMM ore balance not verified |
| `permanent_power` | Player-scoped research completion, source Attunement levels, Grimoire unlocks, long-term spell/package investment | Seven discipline finales + `theory_of_everything` source requirements; not TMM Progression Paths |
| `general_generation` | Five types of ancient Shrine, natural Moonwood/Sunwood/Hallowood, Rock Salt, Quartz and others | Worldgen may be affected by Tectonic; source IDs not equivalent to observed placement |
| `direct_damage` | Nine Source damage payload families (earth/frost/lightning/solar/lunar/blood/flame/void/holy), weapons and spell modifiers | Requires valid spell vehicle+payload and active Mana; no universal fixed damage claims |
| `block_breaking` | `break` payload, Earthshatter Hammer, blocks changed by rituals | Permission/break event and dimensions need QA; source-level registration is not proof of cross-mod protected-block bypass |
| `permanent_pets` | Primalite/Hexium/Hallowsteel Golem Controllers, pixie companions and Treefolk-related items | Durable construct class exists; lifetime/owner binding and mob-limit behavior to trace |
| `temporary_pets` | `conjure_animal`, polymorph wolf/sheep payloads | Summon duration and target rules unknown; not a guaranteed pet conversion |
| `defense` | Warding modules, Primalite/Hexium/Hallowsteel shields, warded armor, spells, Attunement | Exact resistance, shield thresholds and armor enchantments unverified |
| `healing_recovery` | `healing` payload, concoctions, Essence and mana recharges, recuperation project template | Separate health healing from Mana and Theory/Observation recovery |
| `debuff_control` | `polymorph_wolf`, `polymorph_sheep`, damage effects, Rune dispel/protect/absorb | Mob eligibility, permission and persistence need targeted review |
| `energy` | Nine independent typed Mana Sources; 100 centimana per displayed mana; wand/gem/orb item stores, player ManaManager, Font/Nexus/Injector/Relay block network, ritual Mana costs | First Mundane Wand 2500 centimana, siphon 100; no FE/Ars/Iron's equivalence or free infinite conversion established |
| `automation_control` | Font/Battery, Auto Charger, Mana Injector/Relay network, Essence Cask, Essence Transmuter, Sanguine Crucible, Zephyr/Void turbines, Desalinator | Actual throughput/ownership/redstone and net magic costs pending |
| `agriculture` | Hydromelon, Blood Rose, Emberflower, Sunwood/Moonwood/Hallowood and conjure/growth spell routes | Crop growth/bonemeal/harvest outputs not yet verified |
| `detection_information` | Grimoire, Magnifying Glass, Analysis Table, Research Table, Arcanometer, Dowsing Rod, knowledge/scan achievements | Rich in-guide research data and altar diagnostics; must distinguish known item from completed player research |
| `equipment_augmentation` | Nine wand cores, five caps, Apprentice/Adept/Wizard/Archmage gem chain, modular stave, Runecarving and Runic Grindstone | Inspect caps' Mana efficiency, spell counts and exact rune socket rules before balancing |
| `world_manipulation` | Conjure Stone/Water/Lava/Light, block-break spell, Flight/Teleport, rites, Infusion/Synthesis | Claim only exact source-backed effect category; no unrestricted creative placement or cross-mod world edit |
| `world_content` | Five Shrine templates, Treefolk, Pixie item families, Inner Demon, natural magical trees and ores | World spawning/biome/encounter placement and TMM Tectonic integration unverified |
| `death_respawn` | Dream Vision Talisman and Recall Stone exist; there is no proven automatic resurrection or respawn routing in this pass | Do not infer native totem or lich mechanism |
| `trade_economy` | Research Table project names `trade`, `rich_trade`, `prosperous_trade`, `piglin_barter` and artifact/knowledge exchange | These are randomized projects, NOT a distinct village-currency shop; real trade outputs/materials unparsed |
| `progression_tiers` | Basics→six specialist unlocks, hidden Scans; Basic/Expert/Master/Supreme formal research tiers, source discoveries and `secrets_of_the_universe`/`theory_of_everything` finales | Expertise and parent chains impose gates; nine typed sources/attunements separate from TMM paths |

## 9. Quest-source handoff and deliberate first-pass holds

[First research-only Primal quest map](primal-magick-quest-map.md) uses exact native research parent edges, proposes a player-instruction spine and lateral optional systems, and preserves existing TMM FTB chapter state until actual quest implementation is separately authorized. Read the [quest walkthrough standard](../../QUEST-WALKTHROUGH-STANDARD.md); a 29-row index is not a quest.

**Material follow-up research (not new owner design votes):** full generated recipe inventory and first real safe ritual; complete 298 research-stage order with high-impact hidden/gated exceptions; all 38 template material/aid/reward predicates; typed Mana source capacity/range formulas; all wand component interactions and spell effect costs; Grimoire/JEI first-use UI; player-level prerequisites/advancement detectors; TMM-effective datapacks and boss/EMC gate leakage. **No runtime configs, mods, recipes, FTB tasks, or packs modified in this Atlas pass.**
