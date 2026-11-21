# Neo Vitae — registered ritual, array-effect and sigil surface (source inventory)

**Evidence:** frozen source commit [`2744123bb6e4f72c602178b23384be41956eef23`](https://github.com/breakinblocks/NeoVitae/tree/2744123bb6e4f72c602178b23384be41956eef23), aligned with TMM's 1.21.1-1.1.53 mod selection but **not proven binary-identical**. Extracted from [`NVRituals.java`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/main/java/com/breakinblocks/neovitae/ritual/NVRituals.java) and [`NVItems.java`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/main/java/com/breakinblocks/neovitae/common/item/NVItems.java). Cross-read with the committed same-revision [Rituals](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/wiki/Rituals.md), [Alchemy Arrays](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/wiki/Alchemy-Arrays.md) and [Sigils](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/wiki/Sigils.md) overviews.

**Purpose:** make **every statically identified operating family discoverable for quest research**. This is not a full input/output recipe inventory; each item/ritual needs separate exact source/JAR recipe, cost, shape, tier, limits, proof and guidebook reference before a complete chapter can be claimed. The class-name mapping is not evidence of an effect when marked unresolved. Do **not** equate the 51 registered ritual declarations with the 45 ritual **guide entries** or 26 registered Array Effect items with the 71 Alchemy Array **guide entries**; guides include non-operation pages and variants. No runtime tests.

## Ritual registrations — 51 (48 regular and 3 imperfect)

| Java field | Registry ID (where statically literal) | Relevant axis and supported function / remaining work |
| --- | --- | --- |
| `WATER` | `neovitae:water` | `world_manipulation,general_generation`: Water ritual; exact fluid extent/cost unresolved |
| `LAVA` | `neovitae:lava` | `world_manipulation,general_generation`: Lava ritual; exact placement/cost unresolved |
| `GREEN_GROVE` | `neovitae:green_grove` | `agriculture`: Plant growth ritual; exact effect and eligibility need individual audit |
| `WELL_OF_SUFFERING` | `neovitae:well_of_suffering` | `energy,mob_farming`: Damages eligible nonplayer targets to supply altar EV; blacklist and zero-yield tags differ |
| `FEATHERED_KNIFE` | `neovitae:feathered_knife` | `direct_damage,energy`: Source class registered; full behavior not yet examined |
| `HARVEST` | `neovitae:harvest` | `agriculture,automation_control`: Harvests/replants mature crops; guide says 3 EV per harvested crop |
| `REGENERATION` | `neovitae:regeneration` | `healing_recovery`: Area regeneration; cost/range not yet inventoried |
| `SPEED` | `neovitae:speed` | `movement,buffs`: Directed launch for nonsneaking entities, Speed II for sneaking player per guide |
| `MAGNETISM` | `neovitae:magnetism` | `logistics`: Item-gathering ritual; exact radius/cost not yet inventoried |
| `SHEPHERD` | `neovitae:shepherd` | `agriculture`: Breeds animals/grows juveniles; source-guide behavior |
| `BUTCHERING` | `neovitae:butchering` | `agriculture,automation_control`: Automated adult livestock cull while preserving configured breeding stock |
| `FELLING` | `neovitae:felling` | `block_breaking,agriculture`: Fells trees in configured area |
| `SUPPRESSION` | `neovitae:suppression` | `world_manipulation,defense`: Fluid suppression; validate placement and effect boundaries |
| `CONTAINMENT` | `neovitae:containment` | `debuff_control`: Creature containment in ritual area |
| `EXPULSION` | `neovitae:expulsion` | `debuff_control`: Pushes creatures out of an area |
| `ZEPHYR` | `neovitae:zephyr` | `movement`: Registered motion/air ritual; exact behavior requires audit |
| `PUMP` | `neovitae:pump` | `logistics`: Draws and deposits fluids; actual volume/cost requires audit |
| `CRYSTALLUM_FRACTURA` | **dynamic `RitualCrystallumFractura.NAME` — resolve in class** | `agriculture,energy`: Harvests Spiritus crystals and accelerates/reinforces Aura generation |
| `DOWNGRADE` | `neovitae:downgrade` | `equipment_augmentation`: Registered downgrade-related rite; distinct from Penance and Extraction, exact details need audit |
| `PENANCE` | `neovitae:penance` | `equipment_augmentation`: Applies downgrade catalysts to Sentient Armor to free upgrade points |
| `METEOR` | `neovitae:meteor` | `world_manipulation`: Calls catalyst-selected meteors, terrain safety needs audit |
| `FORSAKEN_SOUL` | `neovitae:forsaken_soul` | `mob_farming,energy`: Registered soul-related rite; exact target/trigger requires audit |
| `FULL_STOMACH` | `neovitae:full_stomach` | `healing_recovery`: Feeds practitioners in area from larder |
| `CONDOR` | `neovitae:condor` | `movement`: True flight in ritual area per source guide |
| `SPHERE` | `neovitae:sphere` | `world_manipulation`: Registered spatial/terrain ritual; exact behavior unresolved |
| `ARMOUR_EVOLVE` | `neovitae:armour_evolve` | `equipment_augmentation,progression_tiers`: Evolves worn Sentient Armor +100 upgrade points per activation to 500 |
| `UPGRADE_REMOVE` | `neovitae:upgrade_remove` | `equipment_augmentation`: Extraction of armor modifiers; preserve tomes behavior needs validation |
| `YAWNING_VOID` | `neovitae:yawning_void` | `block_breaking,world_manipulation`: Destroys blocks without drops inside configurable volume |
| `PLACER` | `neovitae:placer` | `world_manipulation,automation_control`: Places blocks from input container following selected shape |
| `GROUNDING` | `neovitae:grounding` | `debuff_control`: Registered grounding rite; exact effect unresolved |
| `SIMPLE_DUNGEON` | `neovitae:simple_dungeon` | `world_content,progression_tiers`: Breaching the Edge of Demon Realm Tier-II starter dungeon |
| `STANDARD_DUNGEON` | `neovitae:standard_dungeon` | `world_content,progression_tiers`: Highway to Hell Tier-III persistent Endless Realm |
| `PHANTOM_BRIDGE` | `neovitae:phantom_bridge` | `movement,world_manipulation`: Temporary spectral walkway in ritual area |
| `TORMENT_NEXUS` | **dynamic `RitualTormentNexus.NAME` — resolve in class** | `energy,mob_farming`: Simulates spawner kills for altar EV without spawning entities; verify exclusions |
| `ENCHANTED_VITAE` | **dynamic `RitualEnchantedVitae.NAME` — resolve in class** | `equipment_augmentation`: Applies compatible enchantment book properties to target item; upper-level behavior needs audit |
| `CELERITY` | **dynamic `RitualCelerity.NAME` — resolve in class** | `automation_control,crafting`: Speeds nearby native crafting stations |
| `DEUS_WELL_OF_SUFFERING` | **dynamic `RitualDeusWellOfSuffering.NAME` — resolve in class** | `energy,mob_farming`: Deus Well upgrade; exact differences unverified |
| `DEUS_TORMENT_NEXUS` | **dynamic `RitualDeusTormentNexus.NAME` — resolve in class** | `energy,mob_farming`: Deus Torment Nexus upgrade; exact differences unverified |
| `DEUS_MAGNETISM` | **dynamic `RitualDeusMagnetism.NAME` — resolve in class** | `logistics`: Deus item-magnetism variant; exact differences unverified |
| `DEUS_CRYSTALLUM_FRACTURA` | **dynamic `RitualDeusCrystallumFractura.NAME` — resolve in class** | `energy,agriculture`: Deus crystal ritual upgrade; exact differences unverified |
| `DEUS_GREEN_GROVE` | **dynamic `RitualDeusGreenGrove.NAME` — resolve in class** | `agriculture`: Deus plant-growth upgrade; exact differences unverified |
| `DEUS_ENCHANTED_VITAE` | **dynamic `RitualDeusEnchantedVitae.NAME` — resolve in class** | `equipment_augmentation`: Deus enchantment ritual upgrade; exact differences unverified |
| `DEUS_PLACER` | **dynamic `RitualDeusPlacer.NAME` — resolve in class** | `world_manipulation`: Deus Mason/Placer upgrade; exact differences unverified |
| `DEUS_FELLING` | **dynamic `RitualDeusFelling.NAME` — resolve in class** | `block_breaking`: Deus tree-felling upgrade; exact differences unverified |
| `DEUS_CELERITY` | **dynamic `RitualDeusCelerity.NAME` — resolve in class** | `automation_control`: Deus Celerity, guide says ×10 machines; requires source-config verification |
| `DEUS_METEOR` | **dynamic `RitualDeusMeteor.NAME` — resolve in class** | `world_manipulation`: Deus Meteor upgrade; exact differences unverified |
| `DEUS_ARMOUR_EVOLVE` | **dynamic `RitualDeusArmourEvolve.NAME` — resolve in class** | `equipment_augmentation,progression_tiers`: Deus Armor Evolution upgrades to 600 points and Blood Mending per source wiki |
| `HELLFORGING` | **dynamic `RitualHellforging.NAME` — resolve in class** | `equipment_augmentation,progression_tiers`: Hellforges eligible Sentient tools, Lex Vitae and armor with late materials |
| `IMPERFECT_RAIN` | `neovitae:rain` | `world_manipulation`: Imperfect weather rite; exact inputs unverified |
| `IMPERFECT_ZOMBIE` | `neovitae:zombie` | `temporary_pets`: Imperfect zombie-related rite; summon properties unverified |
| `IMPERFECT_RESISTANCE` | `neovitae:resistance` | `defense,buffs`: Imperfect resistance rite; exact duration/strength unverified |

## Registered Alchemy Array *effect items* — 26

These are item-registration IDs. An effect item registration is **not** proof of a specific two-ingredient array recipe, valid survival acquisition, complete state machine, resource cost or client acceptance. Link each to its actual recipe and triggered operation during full research.

| Java field | Effect-item ID | Primary study axis |
| --- | --- | --- |
| `ARRAY_BOUNCE` | `neovitae:array_bounce` | `movement,defense` |
| `ARRAY_SPIKE` | `neovitae:array_spike` | `direct_damage` |
| `ARRAY_UPDRAFT` | `neovitae:array_updraft` | `movement` |
| `ARRAY_MOVEMENT` | `neovitae:array_movement` | `movement` |
| `ARRAY_DAY` | `neovitae:array_day` | `world_manipulation` |
| `ARRAY_NIGHT` | `neovitae:array_night` | `world_manipulation` |
| `ARRAY_ELEVATOR` | `neovitae:array_elevator` | `movement` |
| `ARRAY_REPULSION` | `neovitae:array_repulsion` | `debuff_control` |
| `ARRAY_COLLECTION` | `neovitae:array_collection` | `logistics` |
| `ARRAY_LIGHT` | `neovitae:array_light` | `world_manipulation` |
| `ARRAY_FURNACE` | `neovitae:array_furnace` | `ore_processing,crafting` |
| `ARRAY_RAIN` | `neovitae:array_rain` | `world_manipulation` |
| `ARRAY_GROWTH` | `neovitae:array_growth` | `agriculture` |
| `ARRAY_FREEZE` | `neovitae:array_freeze` | `world_manipulation` |
| `ARRAY_SIGNAL` | `neovitae:array_signal` | `automation_control` |
| `ARRAY_TRIGGER` | `neovitae:array_trigger` | `automation_control` |
| `ARRAY_SPIRIT_SIPHON` | `neovitae:array_spirit_siphon` | `direct_damage,energy` |
| `ARRAY_DEFLECTION` | `neovitae:array_deflection` | `defense` |
| `ARRAY_ENDLESS_FOUNTAIN` | `neovitae:array_endless_fountain` | `general_generation,logistics` |
| `ARRAY_UNDERTOW` | `neovitae:array_undertow` | `movement,debuff_control` |
| `ARRAY_LOYAL_FRIENDS` | `neovitae:array_loyal_friends` | `healing_recovery` |
| `ARRAY_VORTEX` | `neovitae:array_vortex` | `debuff_control` |
| `ARRAY_IMPRISONMENT` | `neovitae:array_imprisonment` | `debuff_control` |
| `ARRAY_LIQUIFIED_EXPERIENCE` | `neovitae:array_liquified_experience` | `crafting` |
| `ARRAY_MINER` | `neovitae:array_miner` | `block_breaking` |
| `ARRAY_ENDER_SEVER` | `neovitae:array_ender_sever` | `debuff_control` |

## Registered Sigil items — 18

Each Sigil needs its own detailed native/pack crafting, activation, owner Anima debit and failure/recharge contract; the corresponding `SigilStats` data maps, not these registrations, determine measured costs. `Sigil of Holding` is an inventory and use-interface as well as gear, not a general crafting workbench.

| Java field | Item ID | Primary study axis |
| --- | --- | --- |
| `SIGIL_DIVINATION` | `neovitae:sigil_divination` | `detection_information` |
| `SIGIL_SEER` | `neovitae:sigil_seer` | `detection_information` |
| `SIGIL_WATER` | `neovitae:sigil_water` | `general_generation` |
| `SIGIL_LAVA` | `neovitae:sigil_lava` | `general_generation` |
| `SIGIL_VOID` | `neovitae:sigil_void` | `world_manipulation` |
| `SIGIL_GREEN_GROVE` | `neovitae:sigil_green_grove` | `agriculture` |
| `SIGIL_AIR` | `neovitae:sigil_air` | `movement` |
| `SIGIL_BLOOD_LIGHT` | `neovitae:sigil_blood_light` | `world_manipulation` |
| `SIGIL_FAST_MINER` | `neovitae:sigil_fast_miner` | `buffs` |
| `SIGIL_MAGNETISM` | `neovitae:sigil_magnetism` | `logistics` |
| `SIGIL_FROST` | `neovitae:sigil_frost` | `movement` |
| `SIGIL_SUPPRESSION` | `neovitae:sigil_suppression` | `world_manipulation` |
| `SIGIL_HOLDING` | `neovitae:sigil_holding` | `storage` |
| `SIGIL_TELEPOSITION` | `neovitae:sigil_teleposition` | `movement` |
| `SIGIL_PHANTOM_BRIDGE` | `neovitae:sigil_phantom_bridge` | `movement` |
| `SIGIL_NECROMANCY` | `neovitae:sigil_necromancy` | `temporary_pets` |
| `SIGIL_BOUND_TREASURES` | `neovitae:sigil_bound_treasures` | `storage` |
| `SIGIL_DAMNED` | `neovitae:sigil_damned` | `debuff_control` |

## Items this inventory does **not** prove

- Registration count is not a count of independently craftable or survival-accessible features.
- Dynamically populated materials, recipes, rituals, effect parameters and generated books are outside this declaration-only listing.
- Source-constant `NAME` entries must be traced to concrete registry IDs before copy/pasting into commands, KubeJS, quests or compatibility JSON.
- No in-pack `/give` command, FTB task type, runtime loaded registry, balanced EV cost or accepted cross-mod integration has been validated here.

**Quest-source next step:** for a chosen ritual, resolve dynamic ID, read ritual stats/layout and actual activation code, locate the Modonomicon page and JEI recipe, record precise bottom/tier/upper operation, then add a substantive `neovitae::<capability>` record and quest handoff. Never replace operation research with an alphabetical list alone.
