# Neo Vitae — complete source-generated ritual and sigil cost ledger

**Source/version:** Neo Vitae `breakinblocks/NeoVitae@2744123bb6e4f72c602178b23384be41956eef23`, generated data from [ritual_stats.json](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/data_maps/neovitae/ritual/ritual_stats.json), [sigil_stats.json](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/data_maps/item/sigil_stats.json) and [blood_orb_stats.json](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/generated/resources/data/neovitae/data_maps/item/blood_orb_stats.json). Aligned with pinned 1.1.53 metadata; **current JAR byte parity and effective pack override not established**. Data-map defaults can be changed via datapacks.

These cost entries **do not prove actual ritual activation, valid structure, first acquisition of every input, or successful economy balance**. The numbers are exact **source defaults**, while target conditions, areas and valid effect targets require separate ritual-class research and in-pack tests.

## Activation crystal gate mapping (verified at source type level)

`ItemActivationCrystal.CrystalType` enum order: `WEAK = 0`, `AWAKENED = 1`, `DIVINUS = 2`, `CREATIVE = MAX_VALUE`; ritual `crystal_level` demands this minimum. `RitualStats.java` comment incorrectly calls level 1 "standard"/level 2 "awakened"; the actual enum and `RitualStatsHelper.java` generated guide label correctly identify **Awakened (1), Divinus (2)**.

- **Weak level 0:** `neovitae:activation_crystal_weak` from Lava Crystal in Tier-II Ara Vitae, **10,000 EV**.
- **Awakened level 1:** `neovitae:activation_crystal_awakened` from Weak Crystal in **Tier-IV** Ara Vitae, **40,000 EV**, input components copied. A Tier-III Tenebrae Diviner is not enough to activate a level-1 ritual before that.
- **Divinus level 2:** `neovitae:activation_crystal_divinus` from **Hellfire Forge** recipe with Awakened Crystal + Prismatic Spiritus Gem, **minimum Spiritus availability 5,000; drain 4,000**. Other costs/material gates may apply upstream.
- These are **first known source recipes**, not an assertion of no dungeon loot, trade, alternate pack recipe or player advancement shortcut. Review other crafting and loot sources before enforcing minimum survival acquisition.

## Ritual data map: 46 regular rituals

**Cost interpretation:** `activation_cost` is startup EV from the linked Anima. `refresh_cost` is a nominal per-refresh EV figure; `refresh_time` defaults to **20 ticks** when omitted, but a particular ritual's code can bill only on actual work, simulate a different output amount, or split costs. `per_operation=true` denotes per-use accounting as defined in code, **not a permanent upkeep**. Zero refresh cost does not mean no inputs; meteor ritual consumes a catalyst, etc. Actual runtime operation must be checked per class.

| Ritual registry ID | Min. activation crystal | Start EV | Nominal refresh/per-operation EV | Interval ticks (default 20) | Special flags / study |
| --- | --- | ---: | ---: | ---: | --- |
| `neovitae:butchering` | Weak 0 | 25,000 | 25 | 40 | area/effect/actual charges require individual class audit |
| `neovitae:containment` | Weak 0 | 2,000 | 5 | 5 | area/effect/actual charges require individual class audit |
| `neovitae:expulsion` | Weak 0 | 2,000 | 5 | 5 | area/effect/actual charges require individual class audit |
| `neovitae:feathered_knife` | Weak 0 | 25,000 | 20 | 20 | area/effect/actual charges require individual class audit |
| `neovitae:felling` | Weak 0 | 2,000 | 10 | 20 | area/effect/actual charges require individual class audit |
| `neovitae:full_stomach` | Weak 0 | 1,000 | 100 | 40 | area/effect/actual charges require individual class audit |
| `neovitae:green_grove` | Weak 0 | 1,000 | 20 | 20 | ambient `neovitae:overgrowth`; area/effect/actual charges require individual class audit |
| `neovitae:grounding` | Weak 0 | 2,000 | 10 | 1 | area/effect/actual charges require individual class audit |
| `neovitae:harvest` | Weak 0 | 20,000 | 3 | 20 | area/effect/actual charges require individual class audit |
| `neovitae:lava` | Weak 0 | 10,000 | 500 | 10 | area/effect/actual charges require individual class audit |
| `neovitae:magnetism` | Weak 0 | 5,000 | 50 | 40 | area/effect/actual charges require individual class audit |
| `neovitae:phantom_bridge` | Weak 0 | 2,000 | 1 | 1 | area/effect/actual charges require individual class audit |
| `neovitae:placer` | Weak 0 | 5,000 | 10 | 5 | area/effect/actual charges require individual class audit |
| `neovitae:pump` | Weak 0 | 2,500 | 50 | 20 | area/effect/actual charges require individual class audit |
| `neovitae:regeneration` | Weak 0 | 500 | 50 | 40 | area/effect/actual charges require individual class audit |
| `neovitae:shepherd` | Weak 0 | 500 | 30 | 20 | area/effect/actual charges require individual class audit |
| `neovitae:speed` | Weak 0 | 500 | 5 | 1 | area/effect/actual charges require individual class audit |
| `neovitae:sphere` | Weak 0 | 20,000 | 10 | 1 | area/effect/actual charges require individual class audit |
| `neovitae:suppression` | Weak 0 | 3,000 | 5 | 10 | area/effect/actual charges require individual class audit |
| `neovitae:water` | Weak 0 | 500 | 25 | 1 | area/effect/actual charges require individual class audit |
| `neovitae:well_of_suffering` | Weak 0 | 50,000 | 2 | 20 | area/effect/actual charges require individual class audit |
| `neovitae:yawning_void` | Weak 0 | 5,000 | 10 | 10 | area/effect/actual charges require individual class audit |
| `neovitae:zephyr` | Weak 0 | 1,000 | 2 | 5 | area/effect/actual charges require individual class audit |
| `neovitae:armour_evolve` | Awakened 1 | 50,000 | 25,000 per operation | 20 | one operation per trigger; area/effect/actual charges require individual class audit |
| `neovitae:celerity` | Awakened 1 | 25,000 | 5 | 20 | area/effect/actual charges require individual class audit |
| `neovitae:condor` | Awakened 1 | 10,000 | 100 | 20 | area/effect/actual charges require individual class audit |
| `neovitae:crystallum_fractura` | Awakened 1 | 100,000 | 160 | 100 | area/effect/actual charges require individual class audit |
| `neovitae:downgrade` | Awakened 1 | 20,000 | 10,000 per operation | 20 | one operation per trigger; area/effect/actual charges require individual class audit |
| `neovitae:enchanted_vitae` | Awakened 1 | 10,000 | 0 | 4 | area/effect/actual charges require individual class audit |
| `neovitae:forsaken_soul` | Awakened 1 | 40,000 | 100 | 20 | area/effect/actual charges require individual class audit |
| `neovitae:meteor` | Awakened 1 | 250,000 | 0 | 20 | area/effect/actual charges require individual class audit |
| `neovitae:penance` | Awakened 1 | 20,000 | 5,000 per operation | 20 | one operation per trigger; area/effect/actual charges require individual class audit |
| `neovitae:torment_nexus` | Awakened 1 | 25,000 | 0 | 20 | area/effect/actual charges require individual class audit |
| `neovitae:upgrade_remove` | Awakened 1 | 20,000 | 10,000 per operation | 20 | one operation per trigger; area/effect/actual charges require individual class audit |
| `neovitae:deus_armour_evolve` | Divinus 2 | 500,000 | 250,000 per operation | 20 | one operation per trigger; area/effect/actual charges require individual class audit |
| `neovitae:deus_celerity` | Divinus 2 | 250,000 | 25 | 20 | area/effect/actual charges require individual class audit |
| `neovitae:deus_crystallum_fractura` | Divinus 2 | 400,000 | 160 | 100 | area/effect/actual charges require individual class audit |
| `neovitae:deus_enchanted_vitae` | Divinus 2 | 100,000 | 0 | 4 | consumes books; area/effect/actual charges require individual class audit |
| `neovitae:deus_felling` | Divinus 2 | 8,000 | 10 | 20 | area/effect/actual charges require individual class audit |
| `neovitae:deus_green_grove` | Divinus 2 | 4,000 | 20 | 20 | ambient `neovitae:overgrowth`; area/effect/actual charges require individual class audit |
| `neovitae:deus_magnetism` | Divinus 2 | 20,000 | 50 | 40 | area/effect/actual charges require individual class audit |
| `neovitae:deus_meteor` | Divinus 2 | 1,000,000 | 0 | 20 | area/effect/actual charges require individual class audit |
| `neovitae:deus_placer` | Divinus 2 | 20,000 | 10 | 5 | area/effect/actual charges require individual class audit |
| `neovitae:deus_torment_nexus` | Divinus 2 | 100,000 | 0 | 20 | area/effect/actual charges require individual class audit |
| `neovitae:deus_well_of_suffering` | Divinus 2 | 200,000 | 4 | 20 | area/effect/actual charges require individual class audit |
| `neovitae:hellforging` | Divinus 2 | 1,000,000 | 500,000 per operation | 20 | one operation per trigger; area/effect/actual charges require individual class audit |

### By minimum crystal level

**Weak Activation Crystal (actual level 0; obtainable at altar II):** 23 registered rituals: `butchering`, `containment`, `expulsion`, `feathered_knife`, `felling`, `full_stomach`, `green_grove`, `grounding`, `harvest`, `lava`, `magnetism`, `phantom_bridge`, `placer`, `pump`, `regeneration`, `shepherd`, `speed`, `sphere`, `suppression`, `water`, `well_of_suffering`, `yawning_void`, `zephyr`.

**Awakened Activation Crystal (actual level 1; first known altar recipe IV):** 11 registered rituals: `armour_evolve`, `celerity`, `condor`, `crystallum_fractura`, `downgrade`, `enchanted_vitae`, `forsaken_soul`, `meteor`, `penance`, `torment_nexus`, `upgrade_remove`.

**Divinus Activation Crystal (actual level 2; Hellfire Forge path):** 12 registered rituals: `deus_armour_evolve`, `deus_celerity`, `deus_crystallum_fractura`, `deus_enchanted_vitae`, `deus_felling`, `deus_green_grove`, `deus_magnetism`, `deus_meteor`, `deus_placer`, `deus_torment_nexus`, `deus_well_of_suffering`, `hellforging`.

**Important topology clarification:** the first available Tier-III Endless Realm ritual (`neovitae:standard_dungeon`) is **absent** from the explicit data-map overrides. Its *default* cost and crystal level therefore derive from its ritual class, not from this table. Likewise `neovitae:simple_dungeon` is not in this data map. The ledger only enumerates the **46 explicitly serialized ritual-stat overrides**, and we must separately inspect the registered ritual classes for those 2 omitted entries before claiming complete ritual cost coverage. It is **not** safe to assert `standard_dungeon` uses a Weak Crystal just because the default codec value is 0.

## Sigil data map: 14 overrides

A Sigil's `lp_cost` is the default documented per-use or configurable drain measure. **Continuous/toggle sigils require inspection of their actual `drain_interval` handling**, because this table often omits that field. Radius/effect properties are included when present. Registration inventory has **18 Sigil items** but only **14 generated overrides**; the rest may use item implementation defaults, not absent cost.

| Sigil ID | LP/Anima debit from map | Range | Vertical range | Duration | Effect level | Other |
| --- | ---: | ---: | ---: | ---: | ---: | --- |
| `neovitae:sigil_air` | 50 | — | — | — | — | source default |
| `neovitae:sigil_blood_light` | 10 | — | — | — | — | source default |
| `neovitae:sigil_divination` | 0 | — | — | — | — | source default |
| `neovitae:sigil_fast_miner` | 100 | 10 | 10 | 600 | 2 | source default |
| `neovitae:sigil_frost` | 100 | 2 | 2 | — | — | source default |
| `neovitae:sigil_green_grove` | 150 | 3 | 2 | — | — | source default |
| `neovitae:sigil_holding` | 0 | — | — | — | — | source default |
| `neovitae:sigil_lava` | 1000 | — | — | — | — | source default |
| `neovitae:sigil_magnetism` | 50 | 5 | 5 | — | — | source default |
| `neovitae:sigil_seer` | 0 | — | — | — | — | source default |
| `neovitae:sigil_suppression` | 400 | 5 | 5 | — | — | source default |
| `neovitae:sigil_teleposition` | 1000 | — | — | — | — | source default |
| `neovitae:sigil_void` | 50 | — | — | — | — | source default |
| `neovitae:sigil_water` | 100 | — | — | — | — | source default |

**Source wiki conflicts to verify:** `array/loyal_friends.json` declares `ev_cost:2500`, while `wiki/Alchemy-Arrays.md` describes **5,000 EV**. In the Atlas and quests, prefer **2,500 source-generated data-map value as a candidate**, and flag the discrepancy rather than silently picking the larger prose number; actual cost application must be checked in source or client.

## Blood Orb data map: independent storage and fill-rate fields

`fillRate` is an explicitly named **Orb stat** in generated data, not an unlabeled fourth number and not the same as total fluid capacity or the player Anima maximum. Runtime throughput may still be multiplied by Speed Runes or Orb Vitae Link factors.

| Exact Orb ID | Required altar tier | Orb-held fluid cap | Player Anima max | Orb fillRate stat |
| --- | ---: | ---: | ---: | ---: |
| `neovitae:blood_orb_weak` | 0 | 4,000 | 5,000 | 2 |
| `neovitae:blood_orb_apprentice` | 1 | 6,000 | 25,000 | 5 |
| `neovitae:blood_orb_magician` | 2 | 8,000 | 150,000 | 15 |
| `neovitae:blood_orb_master` | 3 | 10,000 | 1,000,000 | 25 |
| `neovitae:blood_orb_archmage` | 4 | 12,000 | 5,000,000 | 50 |
| `neovitae:blood_orb_transcendent` | 5 | 14,000 | 10,000,000 | 160 |

## Use as quest / integration primary evidence

- **Ritual activation:** `RitualEvent.Activated` in [RitualEvent.java](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/main/java/com/breakinblocks/neovitae/common/event/RitualEvent.java) fires after success and identifies the activating **Player**, ritual, master stone position and owner. This is a concrete native observer for potential *future approved* quest-integrity integration, **not proof the current installed FTB Quests recognizes it**.
- **Craft outputs:** `NeoVitaeCraftedEvent.Altar`, `.Forge`, `.AlchemyTable` expose input/output, but no player identity in those event objects. A native event listener could prove a recipe operated yet would still need an owner attribution strategy to award a specific player's quest correctly.
- **Anima changes:** `AnimaEvent.PostAdd` and `PostSyphon` expose owner UUID and actual credited/debited amount. A combined before/after quest criterion might use them, but events alone do **not** identify the originating Orb or machine.
- **Ritual `Perform` event** is a pre-operation tick notification, so should **not** be treated as proof of a successful harvest, heal or output without a post-success observer.
- All these are **native NeoForge events** examined in frozen source; wiring into FTB, KubeJS or custom progression requires separate implementation authorization, testing and proper teams/claim handling.

## Explicit remaining evidence

1. Both **registered dungeon rituals absent from generated stats** are now source-resolved: Weak Crystal level 0, 80,000/150,000 startup EV, respectively; still map exact rune layouts, valid portal area, on-success output, client behavior, return persistence and any pack overrides.
2. Confirm **Awakened level 1** availability: review dungeon loot and pack recipes; no added Tier III shortcut should be assumed.
3. Confirm actual installed data-map overrides and tooltip consistency for all sigils and Blood Orbs, including `fillRate`.
4. Inspect the pinned binary or a disposable live client for recipe changed/replaced by the pack, plus exact FTB event-task support.
