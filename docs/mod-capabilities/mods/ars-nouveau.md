# Ars Nouveau and seven addons — Mod Capability Atlas, first pass

**Survey date:** 2026-11-21. **TMM:** Minecraft 1.21.1, NeoForge; base Ars Nouveau 5.13.5 and seven selected add-ons. **Evidence state: source-backed partial** — not a score/ranking and not a complete game-accepted quest chapter.

## 1. Scope, provenance and existing ownership

- Frozen base upstream source: [Ars Nouveau 5.13.5](https://github.com/baileyholl/Ars-Nouveau/tree/4d323fba9200c3293a4f4848cf1e0fe70beb7098). The TMM pin and 87-kB [Ars mod-comprehension profile](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/test/full-integration/docs/mod-comprehension/mods/ars-nouveau.md) already document key native operations; do not restart that research.
- The exact [82 base glyph learning recipes](../inventories/ars-nouveau-glyph-recipes.md), [24 ritual tablet recipes plus Brazier](../inventories/ars-nouveau-ritual-tablets.md), and [36 selected infrastructure recipes](../inventories/ars-nouveau-core-infrastructure-recipes.md) are indexed from immutable generated source. They are not exhaustive for all 645 top-level generated recipe entries.
- TMM selects exactly seven Ars family addons: Additions 21.3.0, Controle 1.6.16, Elemental 0.7.10.4, Elemancy 1.18.3, 'n Spells 3.3.5, Ocultas 2.6.2, Unification 1.2.23. [Detailed addon feature/source crosswalk](../inventories/ars-nouveau-addon-family.md).
- **Current chapter reconciliation correction:** At the inspected baseline, [manifest/ars_quest_pilot.json](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/test/full-integration/manifest/ars_quest_pilot.json) contains **49 quests**, as does the live Ars SNBT chapter by task-count; 29 base/20 addon, 17 advancement, 31 item and 1 checkmark task. Older prose describing all 49 as merely an unmerged pilot is stale. [PR #395](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/395) already owns compact layout changes; we will not replace those GUIDs or create duplicate runtime quests.
- Source/version parity with every downloaded compiled Jar, current JEI overrides, and installed addon interaction is not automatically established by a version-aligned source tree. **No native-mod regression QA** is required unless TMM altered the behavior; targeted TMM config/quest/adapter/blacklist or observed failure work is a separate scope.

## 2. Energy, resource and carrier ledger

The Atlas energy axis **must not mean FE**. Also distinguish energy from recipe ingredients and vanilla XP.

| Resource | Where it lives | How acquired | What it powers / how moved | Limits and warning |
| --- | --- | --- | --- | --- |
| **Casting mana** | Persistent **player** mana pool/attributes; spellbook is an editor/casting carrier | Native regen, equipment/potions, eligible addon mana bridging | Player-created Forms, Effects, Augments; later cross-cast in Iron's books | Different from Source and FE. Mana per spell depends on effect sequence, tier, gear and bridge mode |
| **Source** | **Block** Source Jar (**10,000**) and Sourcelink internal buffer (**20,000**), relays and consumers | Crop/tree/breeding/death Sourcelinks and consumable potion/food/fuel Sourcelinks | Imbuement, Enchanting Apparatus, magical workers, Ritual Brazier. Base link attempts transfers every **100 ticks**, Manhattan provider search 5, max 10,000 Source transferred/operation | Source is not a fluid. Ender Source Jar is shared Source storage, **not** an unlimited generator |
| **Material essences** | Physical **item** stacks: Source Gem, Source Gem Block, elemental Essences, other glyph components | Machine output, resource processing and loot | Consumed/catalyze recipe/crafting operations | Do not call an Essence item a Source battery without a defined capacity |
| **Glyph learning XP** | **Player vanilla experience**; Scribes Table charges it and can refund on cancellation | Vanilla XP | **82 source learning recipes at 27/55/160 XP** | Vanilla XP is **not MXP** and does not grant Tyz skill experience |
| **Mob/encounter materials** | Wilden Horn/Spike/Wing, Drygmy Shard, Charms and Tribute **items** | Creature discovery, interaction, boss summoning | Worker acquisition, Summon Wilden ritual, Archmage upgrade | Boss drop and blacklist eligibility are important TMM gates, not an energy pool |
| **Ars 'n Spells unified mana** | Ars/Iron's **player** pools under configured mode | Five available modes: iss_primary, ars_primary, hybrid, separate, disabled | Actual cast payment, regeneration/HUD; independent school-power and cooldown options | Upstream default iss_primary is **not proven effective TMM config**. Prevent double resource awards |
| **FE / ordinary fluids** | External connected capabilities, moved by **Ars Controle Linkage** | Produced by other mods, not necessarily Ars | Connected item/fluid/FE/redstone transport | No general native Source→FE conversion established |
| **Elemental Source→water** | Elemental Urn consumes **Source**, outputs **water** | Source jar/network | Cauldrons/selected consumers incl. Botania Apothecary | This is **one special addon conversion**, not arbitrary FE/fluid interoperability |

**Player state versus block/item:** Learned glyph list is persistent character progression; book holds spell configurations; numeric player mana is different from **block** Source. An item such as Source Gem is a material reagent. Portable Source battery item capacity has **not** been established in this pass. Do not invent an FE equivalence, cost exchange rate or universal Source network.

## 3. Actual multi-tier progression and player choices

| Stage | Mandatory native material/capability | Player action and observable gain | Structural role |
| --- | --- | --- | --- |
| Guide | Worn Notebook: book + item tagged c:gems/lapis | Open Notebook; *Getting Started* → Spell Books / Glyphs / Spellcrafting / Learning Glyphs / Source | Early learning, not a hard recipe tier |
| Novice spellbook | Book + iron shovel + pickaxe + axe + sword | Select book, press **C**, save a valid Form→Effect spell, cast with player mana | Tier-1 glyph capacity |
| Glyph research | Scribes Table, selected glyph ingredients, **27/55/160 vanilla XP** | Choose, supply items, collect glyph, **use it to learn**; crafting output alone is not knowledge | Choice-driven parallel capability path (82 recipes) |
| Source infrastructure | Source Jar and one suitable Sourcelink; later Source Relays | Trigger appropriate growth/breeding/item consumption; transfer Source to station | Independent from book tier; needed for powered crafting |
| Source crafting | Imbuement Chamber, Enchanting Apparatus + Arcane Core/Pedestals, selected recipe | Make Source Gems/Essences, Charms and equipment by correct material/Source path | Station-specific progression, not a fourth spellbook |
| Apprentice spellbook | Novice Book + 1 obsidian-tag + **3 diamond-tag** + **2 quartz blocks** + **2 blaze-rod-tag** | Upgrade and preserve spells; unlock Tier-2 glyphs | Native tier gate |
| Boss route | Ritual Brazier, Summon Wilden tablet, and one each Wilden Horn, Spike and Wing for Chimera | Summon in safe remote arena; defeat Chimera and acquire Tribute | Boss/rarity input for next book |
| Archmage spellbook | Apprentice + **3 ender pearls** + **2 emeralds** + Totem of Undying + Nether Star + Wilden Tribute | Upgrade while retaining spells; Tier-3 glyph access | Native top book tier |
| Optional base branches | Pets, familiars, Drygmy, Wixie, threads, armor, turrets, portals, all other rituals | Specialize in item/Source production, travel, combat and buildings | **Not** all required to achieve Archmage |
| Optional add-on branches | Elemental focus and seven Elemancy combinations, Occultism worker, Iron's spell wheel, Controle redstone, Additions remote network | Use independent bridge with exact recipe and operating limits | **Not** mandated by base book path |

The main tier graph is linear **for spellbook accessibility only**; everything else branches. **No all-82-glyph or all-24-ritual requirement**. The complete [quest reconciliation](ars-nouveau-quest-map.md) must preserve the present chapter IDs and reward/gate ownership.

## 4. Twenty-nine capability axes: source discovery, no ranking

Each line identifies **actual named objects** plus the bottom/upper operation; a positive status is not a guarantee of exhaustive feature support. Further subsection records and addon matrices hold deeper details.

| Axis ID | Source status | Named blocks/items/glyphs and native/addon mechanism | First meaningful use → higher bound | Dossier family |
| --- | --- | --- | --- | --- |
| `gear` | present | Novice, Apprentice, Archmage Spell Books; spell bows, robes, armor threads, focus accessories | Novice casting → Tier-3 books, multiple armor/focus routes | spell-learning, gear-armor |
| `movement` | present | Blink, Leap, Launch, Glide, Slowfall; Warp Scroll/Portal, Flight ritual, addon index | Short-distance cast → portals and specialist remote travel | mobility-travel |
| `ore_processing` | present | Crush/Cut glyphs; Unification Farmer's Delight Cut and Occultism Crusher recipe imports | Simple Crush/Cut → imported recipe processing | world-processing |
| `storage` | present | 10k Source Jar, 20k Sourcelink buffer; Storage Lectern/Repository; Mob Jar; Ender Source Jar | Local Jar and Lectern → remote Warp Index and shared Source | source-network, storage-logistics |
| `crafting` | present | Scribes Table, Imbuement Chamber, Enchanting Apparatus/Core/Pedestals, Wixie, Loom | Starter Source Gems/glyphs → complex powered item and cross-spell crafting | crafting-stations, spell-learning |
| `logistics` | present | Source Relay and variants, Starbuncle, Dominion Wand, Controle Scryer's Linkage, remote indexes | Manual route → shared item/Source networks and compatible fluid/FE transport | storage-logistics |
| `buffs` | present | Heal, Invisibility, Slowfall; Flight, Sanctuary/Restoration rituals and Elemental armor benefits | One positive spell → protected ritual area and school-focused set bonuses | support-rituals |
| `external_compatibility` | present (source) | Iron's through Ars 'n Spells; Occultism via Ocultas and Unification; Farmer's Delight Cut; Botania water integration | One integrated recipe → linked spirit labor and cross-spell casting | addon-bridges |
| `mob_farming` | present | Drygmy Henge nonlethal loot+XP gems; Mob Jar, Vitalic link, Spell Turrets, Siren Shrine | Ordinary breeding/death Source → repeatable Henge and aquatic loot | mob-resource |
| `ore_generation` | unknown/partial | Terrain and island creation rituals; no specific unlimited new-ore route proven | Crush is processing, not ore creation; preserve uncertainty | world-processing |
| `permanent_power` | present | Learned glyphs on player; Ars 'n Spells persistent school cast counts/affinity | Known spells → expanded permanently learned glyphs → separate school-practice scaling | spell-learning |
| `general_generation` | present | Imbuement and Essence transformation, Conjure Water, Elemental Source-water Urn | Small water creation → recipe- and Source-constrained transformation | world-processing |
| `direct_damage` | present | Harm, Ignite, Lightning, Fangs, Explosion, Wither; turrets and Elemental boosts | Single combat cast → augmented and automated spell damage | combat-spells |
| `block_breaking` | present | Break, Fell, Harvest, Crush/Cut; Burrowing/Disintegration rituals | One block or plant → area/ritual terrain work | world-processing |
| `permanent_pets` | present | Starbuncle, Drygmy, Whirlisprig, Wixie and familiar/Amethyst Golem; Elemental Siren | One befriended worker → multi-worker Source-powered station | worker-automation |
| `temporary_pets` | present | Summon Wolves, Undead, Vex, Steed and Decoy; Wilden summons | One ally → multiple role-specific temporary summons | combat-spells |
| `defense` | present | Sanctuary, armor threads, elemental resistances, Retaliate, Spell Turrets | Defensive spell → ritual shelter/elemental set and automated turret | support-rituals |
| `healing_recovery` | present | Heal, Restoration, Potion Flask; Ars 'n Spells Mana Well | Single health cast → recovery ritual and mana sustain | support-rituals |
| `debuff_control` | present | Freeze, Snare, Gravity, Dispel, Pull/Toss, Containment ritual, Controle conditional glyphs | Single effect → AoE/logic-based targeting and confinement | combat-spells |
| `energy` | present (multi) | Player mana, Source block storage/link buffers, vanilla XP for glyph research, addon Iron's bridge; FE moved not generated | Personal cast cost → Source network → configured cross-mana modes | typed-resources |
| `automation_control` | present | Dominion Wand, Source Relays, redstone detectors, turrets, Controle lag sensor and Boolean glyphs | Manual binding → timed/conditional Source and spell machines | storage-logistics |
| `agriculture` | present | Agronomic link, Growth/Harvest, Whirlisprig, Magebloom, Sourceberries, Fertility/Overgrowth | Crops and passive Source → specialized source/farm automation | source-network |
| `detection_information` | present | Sense Magic, Dowsing Rod, Scrying ritual, Scryers Oculus/Crystal, Spell Sensor, lag sensor | Local reading → remote scrying/redstone load signal | information |
| `equipment_augmentation` | present | Alteration Table armor Threads, mana rings/amulets, Elemental Foci and armor tiers | Single thread → multi-slot equipment and element school specialty | gear-armor |
| `world_manipulation` | present | Conjure Water, Place Block, Phantom Block, Exchange, Cloudshaping, Sunrise/Moonfall, island rituals | Small block effect → weather/time/terrain transformation | world-processing |
| `world_content` | present | Archwood tree variants, Magebloom, Sourceberry, Wilden/Chimera, addon ruins and Vexing Caves | Early gathering → Wilden end-book boss → addon world exploration | encounters |
| `death_respawn` | unknown | Healing is not death prevention/respawn; no verified player-resurrection path | Do not invent permanent death-bypass claims | support-rituals |
| `trade_economy` | partial | Ars Ocultas can jar an Occultism Trader; independent Ars-native currency economy not established | Worker processing/trading is Occultism role integration | addon-bridges |
| `progression_tiers` | present | Novice→Apprentice→Archmage books unlock Tier-1/2/3 glyphs; Focus and Elemancy are branches | Tier-1 casting → material upgrade → Wilden Tribute/Nether Star/Totem Tier-3 book | tier-graph |

## 5. Operational capability records (to be expanded below)

These records are grouped by actual mechanism, not by a single arbitrary axis. They capture per-mechanism carrier, resource cost, minimum working setup, possible upper capability and a teachable action. The next record expansion is appended in this same Atlas branch.
