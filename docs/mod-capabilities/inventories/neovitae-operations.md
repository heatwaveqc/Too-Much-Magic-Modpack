# Neo Vitae — registered ritual, array-effect and sigil surface (source inventory)

**Historical registration snapshot:** frozen source commit [`2744123bb6e4f72c602178b23384be41956eef23`](https://github.com/breakinblocks/NeoVitae/tree/2744123bb6e4f72c602178b23384be41956eef23), aligned with TMM's 1.21.1-1.1.53 mod selection but **not proven binary-identical**. Extracted from [`NVRituals.java`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/main/java/com/breakinblocks/neovitae/ritual/NVRituals.java) and [`NVItems.java`](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/src/main/java/com/breakinblocks/neovitae/common/item/NVItems.java). Cross-read with the committed same-revision [Rituals](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/wiki/Rituals.md), [Alchemy Arrays](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/wiki/Alchemy-Arrays.md) and [Sigils](https://github.com/breakinblocks/NeoVitae/blob/2744123bb6e4f72c602178b23384be41956eef23/wiki/Sigils.md) overviews.

**Purpose:** preserve exact source-registry identity. The once-missing operating semantics are now linked in [48 + 3 ritual effects](neovitae-1.1.54-ritual-effects.md), [26 Array effects](neovitae-1.1.54-array-effects.md), [18 Sigils](neovitae-1.1.54-sigils.md), and [1.1.54 version/creative audit](neovitae-1.1.54-version-delta-and-creative-policy.md). **Legacy unresolved wording in old rows is superseded by those pinned 1.1.54 inventories**; do not infer that an item/ritual is unresearched or inactive merely because this 1.1.53 historical inventory originally said to audit. Do **not** equate the 51 registered ritual declarations with the 45 ritual **guide entries** or 26 registered Array Effect items with the 71 Alchemy Array **guide entries**; guides include non-operation pages and variants. No runtime tests.

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
| `MAGNETISM` | `neovitae:magnetism` | `block_breaking,ore_processing`: **The Endless Quarry** physically scans/moves ore-tag blocks below Master Stone. NOT an item magnet; see 1.1.54 [ritual effects](neovitae-1.1.54-ritual-effects.md). |
| `SHEPHERD` | `neovitae:shepherd` | `agriculture`: Breeds animals/grows juveniles; source-guide behavior |
| `BUTCHERING` | `neovitae:butchering` | `agriculture,automation_control`: Automated adult livestock cull while preserving configured breeding stock |
| `FELLING` | `neovitae:felling` | `block_breaking,agriculture`: Fells trees in configured area |
| `SUPPRESSION` | `neovitae:suppression` | `world_manipulation,defense`: Fluid suppression; validate placement and effect boundaries |
| `CONTAINMENT` | `neovitae:containment` | `debuff_control,mob_farming`: pushes escaping nonplayer mobs back into an 11×5×11 area; 2,000 EV startup |
| `EXPULSION` | `neovitae:expulsion` | `debuff_control,mob_farming`: pushes nonplayer mobs outward in a 21×11×21 area; 2,000 EV startup |
| `ZEPHYR` | `neovitae:zephyr` | `mob_farming,logistics`: **The Gathering**—41×21×41 dropped-item + vanilla XP-orb collector; chest atop master inserts items, XP Tome in first slot can absorb XP. 1,000 EV startup; 2 EV base per active 5-tick collection pulse, plus source bonus per ten entities; no live mob motion |
| `PUMP` | `neovitae:pump` | `logistics`: Draws and deposits fluids; actual volume/cost requires audit |
| `CRYSTALLUM_FRACTURA` | **dynamic `RitualCrystallumFractura.NAME` — resolve in class** | `agriculture,energy`: Harvests Spiritus crystals and accelerates/reinforces Aura generation |
| `DOWNGRADE` | `neovitae:downgrade` | `equipment_augmentation`: **Extracts non-trainer upgrades from dropped Sentient gear into Upgrade Tomes**, unlike `upgrade_remove`. |
| `PENANCE` | `neovitae:penance` | `equipment_augmentation`: Applies downgrade catalysts to Sentient Armor to free upgrade points |
| `METEOR` | `neovitae:meteor` | `world_manipulation`: Calls catalyst-selected meteors, terrain safety needs audit |
| `FORSAKEN_SOUL` | `neovitae:forsaken_soul` | `mob_farming,energy`: Registered soul-related rite; exact target/trigger requires audit |
| `FULL_STOMACH` | `neovitae:full_stomach` | `healing_recovery`: Feeds practitioners in area from larder |
| `CONDOR` | `neovitae:condor` | `movement`: True flight in ritual area per source guide |
| `SPHERE` | `neovitae:sphere` | `world_manipulation`: Registered spatial/terrain ritual; exact behavior unresolved |
| `ARMOUR_EVOLVE` | `neovitae:armour_evolve` | `equipment_augmentation,progression_tiers`: Evolves worn Sentient Armor +100 upgrade points per activation to 500 |
| `UPGRADE_REMOVE` | `neovitae:upgrade_remove` | `equipment_augmentation`: **Destructive erase** of equipped Sentient Armor upgrades, no Upgrade Tomes; **`downgrade`** is the real dropped-item Tome extraction. |
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
| `DEUS_MAGNETISM` | `neovitae:deus_magnetism` | `block_breaking,ore_processing`: **Deus Endless Quarry**, 4× ores/scan capacity, configurable Fortune/Silk and Backfill/Clear Rock terrain; **not item magnetism**. |
| `DEUS_CRYSTALLUM_FRACTURA` | **dynamic `RitualDeusCrystallumFractura.NAME` — resolve in class** | `energy,agriculture`: Deus crystal ritual upgrade; exact differences unverified |
| `DEUS_GREEN_GROVE` | **dynamic `RitualDeusGreenGrove.NAME` — resolve in class** | `agriculture`: Deus plant-growth upgrade; exact differences unverified |
| `DEUS_ENCHANTED_VITAE` | **dynamic `RitualDeusEnchantedVitae.NAME` — resolve in class** | `equipment_augmentation`: Deus enchantment ritual upgrade; exact differences unverified |
| `DEUS_PLACER` | **dynamic `RitualDeusPlacer.NAME` — resolve in class** | `world_manipulation`: Deus Mason/Placer upgrade; exact differences unverified |
| `DEUS_FELLING` | **dynamic `RitualDeusFelling.NAME` — resolve in class** | `block_breaking`: Deus tree-felling upgrade; exact differences unverified |
| `DEUS_CELERITY` | **dynamic `RitualDeusCelerity.NAME` — resolve in class** | `automation_control`: Deus Celerity, guide says ×10 machines; requires source-config verification |
| `DEUS_METEOR` | **dynamic `RitualDeusMeteor.NAME` — resolve in class** | `world_manipulation`: Deus Meteor upgrade; exact differences unverified |
| `DEUS_ARMOUR_EVOLVE` | **dynamic `RitualDeusArmourEvolve.NAME` — resolve in class** | `equipment_augmentation,progression_tiers`: Deus Armor Evolution upgrades to 600 points and Blood Mending per source wiki |
| `HELLFORGING` | **dynamic `RitualHellforging.NAME` — resolve in class** | `equipment_augmentation,progression_tiers`: Hellforges eligible Sentient tools, Lex Vitae and armor with late materials |
| `IMPERFECT_RAIN` | `neovitae:rain` | `world_manipulation`: water-over-stone, 5,000 EV constructor, 6,000-tick rain/thunder. |
| `IMPERFECT_ZOMBIE` | `neovitae:zombie` | `temporary_pets`: coal block, 5,000 EV constructor; persistent **hostile** zombie with leather chestplate (not guaranteed allied pet). |
| `IMPERFECT_RESISTANCE` | `neovitae:resistance` | `defense,buffs`: bedrock, 5,000 EV constructor; applies Fire Resistance for 1,200 ticks (amplifier 1). |

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
| `ARRAY_REPULSION` | `neovitae:array_repulsion` | `debuff_control,mob_farming`: Repulsion Array 5-block hostile-only outward push; iron base + lapis catalyst |
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
| `ARRAY_VORTEX` | `neovitae:array_vortex` | `debuff_control,mob_farming`: Vortex **Alchemy Array** 8-block inward pull on living entities; Tabula Robur base + Blood Pearl catalyst; redstone pause and Tabula Rasa player exclusion |
| `ARRAY_IMPRISONMENT` | `neovitae:array_imprisonment` | `mob_farming`: **reprograms vanilla spawner** to type of eligible nearby mob killed, then consumes array; [event source](https://github.com/breakinblocks/NeoVitae/blob/501a8d6a0431ed8aeb19e18be2e83f1856f29a21/src/main/java/com/breakinblocks/neovitae/common/event/ImprisonmentArrayHandler.java). |
| `ARRAY_LIQUIFIED_EXPERIENCE` | `neovitae:array_liquified_experience` | `crafting` |
| `ARRAY_MINER` | `neovitae:array_miner` | `block_breaking` |
| `ARRAY_ENDER_SEVER` | `neovitae:array_ender_sever` | `debuff_control`: cancels EnderEntity teleport within 16 blocks of origin OR destination via event handler, not general player teleport cancellation. |

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

**Source-backed effect crosswalk complete on 1.1.54:** [all 48 regular + 3 imperfect rituals](neovitae-1.1.54-ritual-effects.md), [all 26 arrays](neovitae-1.1.54-array-effects.md), and [all 18 Sigils](neovitae-1.1.54-sigils.md). The current 1.1.54 source has no changed registration or generated structure/recipe IDs in the 1.1.53→1.1.54 comparison, but source–published JAR/pack runtime parity remains separate.

**Mob-farm crosswalk correction:** the [detailed Neo mob-farm mechanisms](neovitae-mob-farm-systems.md) connect the item/XP **Gathering** (`ZEPHYR`) and `ARRAY_VORTEX`/ `ARRAY_REPULSION` living-mob transport to Well/Spike EV harvesting. These are source-derived physical effects, not a complete working farm or integrated QA pass.

## Items this inventory does **not** prove

- Registration count is not a count of independently craftable or survival-accessible features.
- Dynamically populated materials, recipes, rituals, effect parameters and generated books are outside this declaration-only listing.
- Source-constant `NAME` entries must be traced to concrete registry IDs before copy/pasting into commands, KubeJS, quests or compatibility JSON.
- No in-pack `/give` command, FTB task type, runtime loaded registry, balanced EV cost or accepted cross-mod integration has been validated here.

**Quest-source next step:** for a chosen ritual, resolve dynamic ID, read ritual stats/layout and actual activation code, locate the Modonomicon page and JEI recipe, record precise bottom/tier/upper operation, then add a substantive `neovitae::<capability>` record and quest handoff. Never replace operation research with an alphabetical list alone.
