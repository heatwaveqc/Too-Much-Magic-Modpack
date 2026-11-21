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
| `trade_economy` | present, specific | Ritual of Restoration cures Zombie Villagers, with trade discount; Ars Ocultas can jar Occultism Trader, but independent Ars currency economy not established | Worker processing/trading is Occultism role integration | addon-bridges |
| `progression_tiers` | present | Novice→Apprentice→Archmage books unlock Tier-1/2/3 glyphs; Focus and Elemancy are branches | Tier-1 casting → material upgrade → Wilden Tribute/Nether Star/Totem Tier-3 book | tier-graph |

## 5. Operational capability records (to be expanded below)

These records are grouped by actual mechanism, not by a single arbitrary axis. They capture per-mechanism carrier, resource cost, minimum working setup, possible upper capability and a teachable action. The next record expansion is appended in this same Atlas branch.

### spell-learning — Novice/Apprentice/Archmage Spell Books; Scribes Table; learned glyphs

**Mechanics, carriers and costs:** Novice from Book and four iron tools; C opens the spell editor. At Scribes Table select one of 82 glyph recipes, pay vanilla XP 27/55/160 and ingredient items; crafting lasts 120 ticks once materials arrive. Collect and use glyph to add it permanently to player knowledge; cancelling can refund XP/materials.

**Entry → mature capability:** Single valid Form+Effect Tier-1 cast → 2nd-tier spell after Apprentice → third-tier configurations after Archmage, retaining learned spells.

**Quest use and bounds:** Show choosing recipe/XP debit/ingredient delivery, learned glyph in editor, valid target and player mana decrease. FTB ownership/advancement is never proof of cast. 82 recipes in dedicated exact JSON index.

### source-network — Source Jar, Agronomic/Vitalic/Alchemical/Mycelial/Volcanic Sourcelinks, Relays

**Mechanics, carriers and costs:** Source is numerically stored in Jar 10,000 and Sourcelink buffer 20,000. Agronomic: common crop +20, magic-tag crop +45; trees +50/+100. Vitalic: valid breed +600 and death +200. Alchemical: >=100 potion units consumed; Mycelial consumes food; Volcanic consumes fuel. Base transfer each 100 ticks, Manhattan search 5 and <=10,000 transferred.

**Entry → mature capability:** Passive farm at one Jar → separately fuelled potion/food/fuel Source networks → linked relay and consumer networks.

**Quest use and bounds:** Demonstrate actual qualifying trigger, power stored in Jar and one consumer, but do not schedule native stand-alone QA. TMM tag overrides and Source bridge patches justify focused pack tests only.

### crafting-stations — Imbuement Chamber, Enchanting Apparatus, Arcane Core, Pedestals, Scribes Table

**Mechanics, carriers and costs:** Imbuement processes Source Gem and elemental Essence recipes; ordinary Gem conversion may progress without an active powered producer. Essence catalyst pedestals may retain item inputs. For Apparatus, align Core adjacent, place materials on nearby pedestals (up to 3 blocks), central reagent in Apparatus and ensure Source for source-requiring recipe.

**Entry → mature capability:** Basic gems → advanced Charm/armor/trinket and Essence recipes → addon Wixie Apparatus work.

**Quest use and bounds:** Item ownership does not prove a successful Apparatus run. Source JSON material costs are in 36-device index; parent machine and recipe categories differ.

### combat-spells — Forms Touch, Projectile, Self, Rune and others; Effects Harm, Ignite, Cold Snap, Wither, Lightning, Fangs, Explosion, Pull, Freeze, Heal; Augments Amplify, AoE, Pierce

**Mechanics, carriers and costs:** Order of form, effect and augment determines target and mana; a learned glyph may require high book tier. Effects can be damage, movement/control, utility and support. Spell turrets and Elemental foci can change resulting effect and consumption.

**Entry → mature capability:** Simple single-effect spell → multi-effect/projectile/area spell → automated turret and Elemental focus/Iron's school-power bridge.

**Quest use and bounds:** Inspect actual spell class for per-glyph mana cost and level restrictions before asserting limits; recipe XP is not a casting cost.

### mobility-travel — Leap, Launch, Blink, Glide, Slowfall, Warp Scroll, Portal, Warping/Flight rituals, Controle Holder

**Mechanics, carriers and costs:** Direct movement consumes player mana. A native Warp Portal uses Sourcestone rectangle from 1×1 to 21×21, a full Source Jar nearby and a destination-bound Warp Scroll; source guide describes same-dimension portal path. Controle/ Additions enable different remote/travel routes.

**Entry → mature capability:** Short cast mobility → warp scroll and portal → specialist spell prisms and cross-dimension remote targeting.

**Quest use and bounds:** Separate player movement from spell projectile routing; do not promise a universal Waystones-like mechanism or unloaded destinations.

### gear-armor — Arcanist/Battlemage/Sorcerer robes, Alteration Table and Threads, mana amulets/rings, Elemental focus and elemental armor

**Mechanics, carriers and costs:** Alteration Table receives worn armor on a stand; apply threads to its display then remove armor to finalize, or remove thread with empty-hand interaction. Elemental foci boost one school with off-school tradeoff, while higher armor sets require recipe-based progression.

**Entry → mature capability:** Basic robes → first thread slot/mana gear → selected school focus/full-set effects and Elemancy pair/four-element specialization.

**Quest use and bounds:** Thread slots and source cost vary by gear; avoid assuming every set can accept unlimited threads or that focus gains permanent innate skills.

### worker-automation — Starbuncle, Whirlisprig, Drygmy, Wixie, Mob Jar, Amethyst Golem

**Mechanics, carriers and costs:** Starbuncle handles assigned item logistics; Whirlisprig habitat and plant diversity provide Source; Drygmy Henge can simulate eligible nearby natural/Mob-Jar mob loot and experience gems without killing. Wixie works via Cauldron and supported recipes. Workers require native token/charm, workstation, suitable inventory and sometimes Source.

**Entry → mature capability:** One Starbuncle item route → Whirlisprig Source garden → Drygmy habitat and Wixie recipe automation.

**Quest use and bounds:** Teach individual Charm acquisition and actual role, not just crafting Charm; inspect native role on source rather than asserting all worker functions are interchangeable.

### mob-resource — Drygmy Henge, natural mobs, Mob Jar, Vitalic link, ritual containment, Elemental Siren Shrine

**Mechanics, carriers and costs:** Drygmy Henge scans natural candidates approx 10 horizontal/6 vertical and Mob Jars Manhattan 10, uses workers and Source. Base code charges 1,000 Source per 20 progress output; target loot count 1 + 2×distinct entity types + min(5, nearby count), with vanilla XP converted at 1/4 to gems. Blacklist filter occurs late, so excluded mobs may affect population bonus; default blacklist only Iron Golem in researched base.

**Entry → mature capability:** Breeding/death Source → common passive Henge → multiple worker variety/Mob-Jar and Siren aquatic loot.

**Quest use and bounds:** High-risk TMM gating: fake-player loot, rare/boss drops and blacklists. This is source-derived and not a permission to farm protected mob drops. Missing storage may silently lose output remainders.

### mining-world — Break, Fell, Cut, Crush, Harvest, Burrowing/Disintegration rituals

**Mechanics, carriers and costs:** Break and Fell are direct block or tree actions; Cut and Crush transform allowed input by native or imported recipes; Burrowing/Disintegration are Source/ritual/worldwork effects, not synonymous with ore multiplication.

**Entry → mature capability:** One block effect → selected tree/field and recipe batch → area ritual removal/earth changes.

**Quest use and bounds:** Separate quarrying existing ores from generating new ore; gather actual per-spell mana and per-ritual world permission code before asserting protective behavior.

### agriculture-world — Agronomic link, Magebloom, Sourceberries, Archwood, Grow/Harvest glyphs, Fertility/Overgrowth/Flowering rituals, Whirlisprig

**Mechanics, carriers and costs:** Magebloom gives material fiber; Archwood and Sourceberry have world acquisition and growing processes. Agronomic Source arises from eligible successful crop/tree growth events. Growth and harvest spells/rituals alter target plant blocks or yield items; Whirlisprig favors plant diversity.

**Entry → mature capability:** First Source/food crop → diversified plant and Magebloom field → repeated growth and farm automation.

**Quest use and bounds:** TMM's Pam's/Farmer's crops need actual tag mapping before claiming 45 Source magic crop bonus. Do not count bone meal alone as a native growth event.

### rituals-support — Ritual Brazier + 24 separately crafted tablets, augment items and Source Jar

**Mechanics, carriers and costs:** Place Brazier and tablet, add optional/required augments before activation, activate with empty hand. Tablet consumed when ritual starts. Source-consuming rituals pull from eligible Jars within 6 blocks. Options include Flight, Sanctuary, Restoration, Harvest, Moonfall, Scrying, Burrowing, Containment, Warping, Conjure Island and Disintegration.

**Entry → mature capability:** First low-risk ritual → personal support/field production → extended terrain/boss summon and Controle portable ritual relay.

**Quest use and bounds:** All 24 tablet inputs indexed, but all 24 activation costs, augmentation behavior and entity prerequisites are **not** audited yet. Do not make a 24-ritual mandatory quest chain.

### storage-logistics — Storage Lectern, Repository, Source Relays variants, Dominion Wand, Starbuncle, Warp Index, Scryer's Linkage

**Mechanics, carriers and costs:** Lectern/Repository implement linked storage/crafting. Source Relays route energy with collector/deposit/splitter/warp variants, normally bound by Dominion Wand source then destination. Additions Warp Index reads a loaded remote Lectern; Controle Linkage can route eligible item/fluid/FE/redstone endpoints.

**Entry → mature capability:** Local storage and one Jar → linked Source and items → cross-dimension special remote and compatible other capability networks.

**Quest use and bounds:** Source remains its own magic energy. Do not add generic storage/FE infrastructure to TMM simply because a Control addon can transport it.

### information — Sense Magic, Scrying ritual, Dowsing Rod, Scryer's Crystal/Oculus, Item Detector, Spell Sensor, Controle Temporal Stability Sensor

**Mechanics, carriers and costs:** Diagnostics detect relevant magic/item/block targets; Scrying and Oculus expose information from afar; Sensor can feed comparator indicating server load for player-designed throttling. Item Detector/Spell Sensor drive redstone based on observed criteria.

**Entry → mature capability:** Local targeting or magic inspection → remote scrying and scripted load-controlled machines.

**Quest use and bounds:** Temporal Stability Sensor observes lag, does not fix server TPS by itself. Any custom CC:Tweaked interface is optional/integration-specific.

### automated-combat — Spell Turret, Basic/Rotating/Timer variants, prisms, redstone, conditional Controle glyphs

**Mechanics, carriers and costs:** Store a valid spell in a turret, provide firing activation and supported Source/mana mechanics, aim or set rotation/timing; prisms redirect incoming compatible spells. Controle can branch with NOT/OR/XOR/XNOR, height/Random filters and precise delay glyphs.

**Entry → mature capability:** One triggered turret → rotating/timed area → conditional/remote projectile networks.

**Quest use and bounds:** Need pinned source class costs, filtering and mob attribution for exact performance; don't interpret native automated damage as player MXP without a first-party bridge.

### encounters — Wilden Horn/Spike/Wing, Summon Wilden tablet, Ritual Brazier, Wilden Chimera and Tribute

**Mechanics, carriers and costs:** Craft tablet from Purple Archwood log, 3 Wilden-tag drops and lapis block. Ordinary activation summons a Wilden; with all three named Horn/Spike/Wing augments it summons destructive Chimera. The Archmage upgrade also requires its Tribute plus a Nether Star and Totem.

**Entry → mature capability:** First Wilden creatures → controlled arena and Chimera → material unlock of Archmage Tier 3.

**Quest use and bounds:** Separate actual boss kill advancement, Tribute inventory, source table inputs and arbitrary quest reward; don't freely reward tier-3 gating ingredients.

### addon-bridges — Seven separately selected addons: Additions, Controle, Elemental, Elemancy, Ars 'n Spells, Ars Ocultas, Unification

**Mechanics, carriers and costs:** Ars 'n Spells connects Iron's mana/casts, school power and inscription; Ocultas jars Occultism workers and can automate sacrifice flags; Unification imports installed Farmer's Delight Cut/Occultism Crusher conversions; Elemental adds Source→water, armor, pets; Elemancy seven specializations; Controle multi-capability links and control glyphs; Additions storage travel and glyph extensions.

**Entry → mature capability:** One optional recipe/bridge → specialized multi-mod operation → guarded Source/loot and mana interactions.

**Quest use and bounds:** Design holds: effective mana mode and damage stacking; nonconsuming Ocultas altar flags; Drygmy protected loot; Additions chunk-loading policy. Detailed 7-addon table is already separate.

## 6. Research ledger and immediate source/owner gaps

| Evidence | Source-first finding | Boundary |
| --- | --- | --- |
| [82 core glyph recipes](../inventories/ars-nouveau-glyph-recipes.md) | **82/82** generated recipes, XP distribution 39×27 / 28×55 / 15×160 | Learned glyph recipes are not the full cast-effect / mana cost audit; installed addons may add glyphs |
| [24 ritual tablets and Brazier](../inventories/ars-nouveau-ritual-tablets.md) | **24 tablets + ritual_brazier** source recipes | 24 runtime classes now indexed, including 9 explicit getSourceCost override expressions; parent scheduling/activation and complete external augment validations remain |
| [36 selected core machine/Source recipes](../inventories/ars-nouveau-core-infrastructure-recipes.md) | Source, books, workstations, relays, four workers, turrets and key items | Selected subset of 645 top-level recipe files; no claims of full recipe exhaustiveness |
| [7-addon capability matrix](../inventories/ars-nouveau-addon-family.md) | All seven installed addon families, candidate low- to high-end routes and major cross-mod effects | Detailed addon recipe catalogs and high-impact version pin deltas remain |
| [49 existing Ars quest records](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/test/full-integration/manifest/ars_quest_pilot.json) | 29 base, 20 addon; 31 item, 17 advancement, 1 checkmark | **Current integration chapter is not empty**; separate PR #395 owns compact layout; do not generate duplicate GUIDs |

**Specific next source research:** per-glyph cast effects/mana and augment applicability; parent charge cadence/complete augmentation geometry; armor Threads and source production recipe families; addon exact glyph/registered-item lists. These are research tasks, not automatic native-functionality test obligations.

**Owner-level decisions still distinct from source work:** (1) Drygmy loot from protected bosses and blacklist policy; (2) Ars Ocultas altar signals Occultism sacrifice/item-use flags for 5,000 Source without consuming the corresponding jarred entity/item; (3) Ars 'n Spells effective mana mode, damage and resonance stacking with TMM Progression; (4) chunk loading and Source production balance only if pack overrides them.

**TMM QA scope:** This survey edits documentation only; do **not** re-test untouched upstream spellcasting, Henge, workers, ritual mechanics or native XP features. Add narrowly scoped tests only for TMM-owned recipes/config/blacklist/quest detectors, changed adapters or observed failures. No full build was authorized or launched.

## 7. Native ritual effect and Source-cost findings — all 24 runtime classes surveyed

The [24-class frozen-source operational inventory](../inventories/ars-nouveau-ritual-operations.md) now complements the [24 actual tablet inputs](../inventories/ars-nouveau-ritual-tablets.md). These are source-class `getSourceCost()` return values and the native effect descriptions: they are **not universally charged each tick**, because parent activation and effect scheduling determine when Source is drawn. Absence of a local override does **not** mean zero Source; see the linked implementation.

| Source native ritual | Named effect, size and material augment examples | Source-cost expression (when class overrides) | Axis interaction and useful quest lesson |
| --- | --- | --- | --- |
| **Sanctuary** (DenySpawn) | Denies natural hostile spawn within **32 blocks**; rotten flesh increases radius one per item up to **128**; charges on denied spawn per its timed rule | **500** | Defense and mob-farm management; can suppress legitimate mob Source/loot farms; no innate Torchmaster needed |
| **Disintegration** | Destroys nearby monsters; produces **Experience Gems at 2× experience** but **no ordinary item drops** | **300** | Mob farming and XP economics; contrast with Drygmy item/XP gems and Vitalic death Source; no automatic MXP |
| **Flight** | Gives nearby players temporary flight upon jumping; refreshes while near Brazier | **200** per source-class refresh effect | Movement; range-limited magical flying, not permanent unrestricted creative flight |
| **Gravity** | Applies recurring downward pull to nearby players | **200** | Debuff/control; may conflict with flight or other movement effects |
| **Harvest** | Harvests crops in area, routes drops into adjacent inventory if present, else ground | **100** per eligible harvest cycle | Agriculture, logistics, Source; **physical drops** differ from player XP rewards |
| **Restoration** (RitualHealing) | Heals ordinary mobs/players, damages undead, **immediately cures Zombie Villagers** and offers nearby-player discounts | **200** | Healing, damage, **villager trade economics**; source-derived native benefit, not TMM currency |
| **Containment** (RitualMobCapture) | Moves eligible nearby mob into a nearby **Mob Jar**; both jar and mob within **3 blocks**; requires ongoing Source after first capture | **500** | Physical entity capture → Drygmy habitat/Occultism jar bridge; honor TMM blacklist |
| **Overgrowth** | Periodically bone-meals blocks; **Bone Block** catalyst turns it into baby-animal growth | **500** | Agriculture and age cycling; not directly an EV/MXP generator |
| **Fertility** (RitualBreed) | Periodic animal breeding; will not act where **20+ animals** nearby | **500** | Livestock propagation → Vitalic Source events; bounded headcount |
| **Scrying** | Tracks chosen block through walls via particle distance colors; Manipulation Essence extends effect to **15 minutes** | inherited/none locally | Detection, block search; player must specify sample block before start |
| **Flowering** | Creates plants; Source Gems each +1 radius, sand switches to cactus/dead bushes | inherited/none locally | Resource landscaping, not automatic rarity/ore generation |
| **Forestation** | Oak/Birch trees and growth in a roughly **7×7 circular** area; each Source Gem +1 radius; mushroom/glow berries change foliage/biome style | inherited/none locally | World growth and Source through Agronomic when real growth event fires |
| **Plains/Desert Conjuration** | Generates **radius-7 island**; each Source Gem increases radius by 1, catalysts change climate/material | inherited/none locally | World creation, not a proved renewable-ore source; Source during block generation |
| **Awakening** | Archwood into **Weald Walkers**; Budding Amethyst into **Amethyst Golems**; Book and Quill catalysts for **Bookwyrm Charm** | inherited/none locally | Actual permanent-helper acquisition from a ritual, not merely a creature-spawn egg |
| **Challenge** (PillagerRaid) | Triggers a raid in village; Emerald raises difficulty, with possibility of Totems on lower difficulties | inherited/none locally | Mob farming and Archmage Totem sourcing; do not silently gate a native route via quest-only arrows |
| **Burrowing** (RitualDig) | Digs four vertical shafts to bedrock and drops blocks | inherited/none locally | Powerful block-breaking, placement safety important when a TMM protection rule exists |
| **Warping** | Uses **inscribed Warp Scroll** catalyst; moves all nearby entities to target | inherited/none locally | Group travel rather than single-person use |
| **Wilden Summoning** | Ordinary wild Wilden summons; **Horn+Spike+Wing** produces a destructive Wilden Chimera | inherited/none locally | Native boss material gate and arena preparation |
| **Moonfall/Sunrise/Cloudshaping** | Change time/weather; Lapis Block gives storm and Gunpowder gives rain | inherited/none locally | Environmental/world effects; available to players independent of book's upper glyphs |

**One important energy detail:** The base class decides when the `getSourceCost` expression is charged; use the runtime source and named activation conditions rather than treating `500` as 500 Source/sec by default. Rituals can draw Source and sometimes route actual item drops or XP gems. **TMM MXP remains separate**, and untouched native rituals are not a new QA queue.

