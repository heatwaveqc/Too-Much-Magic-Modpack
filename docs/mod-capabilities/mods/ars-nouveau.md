# Ars Nouveau and seven addons — Mod Capability Atlas (source research COMPLETE)

**Survey date:** 2026-11-21. **TMM:** Minecraft 1.21.1, NeoForge; base Ars Nouveau 5.13.5 and seven selected add-ons. **Evidence state: source Atlas COMPLETE for quest authoring, separate TMM and client acceptance pending** — not a score/ranking and not a complete game-accepted quest chapter.

**Completion decision (2026-11-21):** The [source-research closure audit](../inventories/ars-nouveau-source-closure.md) reconciles all 29 axes, actual Novice→Archmage progression, player mana versus Source, 133 cross-family glyph learning recipes, 24 ritual implementations, all seven addon capability routes, and 49 existing quest records with 52 instructional editorial candidates. Parsing every one of 1,283 discovered recipe JSONs is **not** a requirement to teach all material first/tier/top-end capabilities. Existing references to a partial first/second/third pilot below describe its historical state before the closure. Remaining source-JAR/client parity, tag consumer (#373), disabled Arcane Permanence, first-party mana/XP bridges and FTB task changes are separate tracks. This is **NOT gameplay acceptance**.

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
| `storage` | present | 10k Source Jar, 20k Sourcelink buffer; Storage Lectern/Repository; Mob Jar; Ender Source Jar; Additions **Handy Haversack** remote-filtered deposit | Local Jar and Lectern → remote Warp Index, shared Source, bound remote-handler pickup deposit (no extra bag inventory) | source-network, storage-logistics |
| `crafting` | present | Scribes Table, Imbuement Chamber, Enchanting Apparatus/Core/Pedestals, Wixie, Loom | Starter Source Gems/glyphs → complex powered item and cross-spell crafting | crafting-stations, spell-learning |
| `logistics` | present | Source Relay and variants, Starbuncle, Dominion Wand, Controle Scryer's Linkage, Handy Haversack, Elemental **Attraction** for living mobs **and item entities** | Manual route → shared item/Source networks, bound remote deposits, item magnetism and compatible fluid/FE transport | storage-logistics |
| `buffs` | present | Heal, Invisibility, Slowfall; Flight, Sanctuary/Restoration rituals and Elemental armor benefits | One positive spell → protected ritual area and school-focused set bonuses | support-rituals |
| `external_compatibility` | present (source) | Iron's through Ars 'n Spells; Occultism via Ocultas and Unification; Farmer's Delight Cut; Botania water integration | One integrated recipe → linked spirit labor and cross-spell casting | addon-bridges |
| `mob_farming` | present | Drygmy Henge nonlethal loot and XP gems; Additions **Source Spawner** converts Source + Mob-Jar template into **real mobs**; Vitalic, Siren Shrine, Disintegration, Containment, Elemental Attraction/Repulsion | Breeding/death Source → repeatable nonlethal Henge → Source-paid actual entity spawning / XP-gem / fishing-loot systems, gated by entity/loot tags | mob-resource |
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
| `energy` | present (multi) | Player mana, block Source, Scribe vanilla XP, **Additions XP Jar (two voided items → one vanilla XP)**, cross-Iron's mana bridge; FE transported only | Personal cast cost → Source network → configurable cross-mana modes, and separated vanilla XP acquisition | typed-resources |
| `automation_control` | present | Dominion Wand, Source Relays, redstone detectors, turrets, Controle lag sensor and Boolean glyphs | Manual binding → timed/conditional Source and spell machines | storage-logistics |
| `agriculture` | present | Agronomic link, Growth/Harvest, Whirlisprig, Magebloom, Sourceberries, Fertility/Overgrowth | Crops and passive Source → specialized source/farm automation | source-network |
| `detection_information` | present | Sense Magic, Dowsing Rod, Scrying ritual, Scryers Oculus/Crystal, Spell Sensor, lag sensor, **Elemental Detection (Glowing monsters)**, Additions Locate Structure (15 target definitions) | Local inspection → remote scrying, structure directions, 128-block-area monster reveal and redstone load signals | information |
| `equipment_augmentation` | present | Alteration Table **19 craftable Armor Threads**, rings/amulets, Elemental Foci, dual-element Elemancy armor and six-tier Elemancer gear variants | One thread → whole armor/focus specialization and up to **7,000 Source per sampled advanced armor upgrade** | gear-armor |
| `world_manipulation` | present | Conjure Water, Place Block, Phantom Block, Exchange, Cloudshaping, Sunrise/Moonfall, island rituals | Small block effect → weather/time/terrain transformation | world-processing |
| `world_content` | present | Archwood tree variants, Magebloom, Sourceberry, Wilden/Chimera, addon ruins and Vexing Caves | Early gathering → Wilden end-book boss → addon world exploration | encounters |
| `death_respawn` | unknown | Healing is not death prevention/respawn; no verified player-resurrection path | Do not invent permanent death-bypass claims | support-rituals |
| `trade_economy` | present, specific | Ritual of Restoration cures Zombie Villagers, with trade discount; Ars Ocultas can jar Occultism Trader, but independent Ars currency economy not established | Worker processing/trading is Occultism role integration | addon-bridges |
| `progression_tiers` | present | Novice→Apprentice→Archmage book unlocks Tier-1/2/3 glyphs; 133 learnable glyph recipes across 4 family modules; Focus and six paired Elemancy paths + four-element Elemancer equipment stages | Tier-1 casting → Tier-2 materials → Wilden Tribute/Nether Star/Totem Tier-3, with separate Source-hungry Focus/armor side progression | tier-graph |

## 5. Sixteen operational capability records

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

**Quest use and bounds:** The Ocultas 5,000-Source substitution and TMM shared Drygmy/spawner replication policy are owner-approved; capture integration [#373](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/373) remains staged. The owner reports Ars 'n Spells cross-mana is already operational; do **not** demand a new mode/design decision before teaching the native bridge. Its effective config and any **changed first-party** MXP credit bridge can be examined as technical evidence. Other remaining items: Source Spawner's alternate native tag parity and default-disabled Additions Arcane Permanence opt-in.

## 6. Research ledger and immediate source/owner gaps

| Evidence | Source-first finding | Boundary |
| --- | --- | --- |
| [82 core glyph recipes](../inventories/ars-nouveau-glyph-recipes.md) | **82/82** generated recipes, XP distribution 39×27 / 28×55 / 15×160 | Learned glyph recipes are not the full cast-effect / mana cost audit; installed addons may add glyphs |
| [24 ritual tablets and Brazier](../inventories/ars-nouveau-ritual-tablets.md) | **24 tablets + ritual_brazier** source recipes | 24 runtime classes now indexed, including 9 explicit getSourceCost override expressions; parent scheduling/activation and complete external augment validations remain |
| [36 selected core machine/Source recipes](../inventories/ars-nouveau-core-infrastructure-recipes.md) | Source, books, workstations, relays, four workers, turrets and key items | Selected subset of 645 top-level recipe files; no claims of full recipe exhaustiveness |
| [7-addon capability matrix](../inventories/ars-nouveau-addon-family.md) | All seven installed addon families, candidate low- to high-end routes and major cross-mod effects | Detailed addon recipe catalogs and high-impact version pin deltas remain |
| [49 existing Ars quest records](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/test/full-integration/manifest/ars_quest_pilot.json) | 29 base, 20 addon; 31 item, 17 advancement, 1 checkmark | **Current integration chapter is not empty**; separate PR #395 owns compact layout; do not generate duplicate GUIDs |

**Survey progress:** base native spell-effect/method/augment costs and compat declarations, 24 ritual Source-charge behavior, 19 Armor Threads, 14 Imbuement recipes and addon glyph learning catalogs are source-mapped. [Third-pass compound spell economics](../inventories/ars-nouveau-compound-spell-economics.md) adds actual ordered nominal cost, augment-target price overrides, player discounts, separate Pre/Post event calculations, crafting-vs-cast validator composition and five explicit beginner-to-Tier-II examples. **Next source research:** installed effective overrides/addon cost hooks if needed for quest accuracy, selected ritual geometry, comprehensive addon gear variants and exact Source Spawner tag parity. These are not automatic native-functionality test obligations.

**Owner policy and verification split (2026-11-21):** **Approved:** Drygmy native loot automation subject to the shared replication-deny registry and its original Iron Golem ban; Source Spawner follows the same TMM spawner/replication eligibility; Ars Ocultas consumes Source (5,000 per native sacrifice/item fulfillment) as an intentional alternative without consuming the worker/item; Siren Shrine may generate fishing/treasure-table items without Angler's Hat valuables bonuses. **Technical follow-up:** capture implementation [#373](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/373) already maps `#tmm:replication_blacklist` to Drygmy and Apothic Spawners, but the Additions Source Spawner checks `#ars_nouveau:jar_release_blacklist` and `#c:bosses` while staged `ars_nouveau:jar_blacklist` aliases only `#tmm:capture_blacklist`; verify/provide explicit parity in the capture-owned consumer without weakening native denials. A weighted Parchment Fragment may appear in common fishing treasure after [#433](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/433) integrates; the Siren synthetic score-based Luck of the Sea is distinct from Angler's Hat bonuses. **Operational report:** owner says Ars 'n Spells mana already works; no additional mode decision or rebalancing hold. Exact effective enum/addon damage and Progression bridge hooks are configuration/evidence details, not proof of a problem. Disabled Arcane Permanence must not get survival quests without opt-in.

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


## 8. Second-pass verified spell, recipe and addon capability results (2026-11-21)

This pass extended the original 29-axis survey using **the same immutable upstream release snapshots** and intersecting the seven addon source families with the TMM enabled roster. It did **not** modify, integrate, or test native gameplay. The source catalog is now deep enough to distinguish low-end/advanced operations and typed costs, but still not an exhaustive numerical balance simulator of every possible spell combination.

### 8.1 Complete native spell-part source economics, not just glyph learning

| Type of record | Source-count and what was actually extracted | Player-facing interpretation |
| --- | --- | --- |
| [Base 82 glyph learning recipes](../inventories/ars-nouveau-glyph-recipes.md) | **82**, item/tag ingredients + vanilla XP: 39×27 / 28×55 / 15×160 | One-time **research/learning**, not cast mana, Source or MXP |
| [Base Java spell Effects](../inventories/ars-nouveau-spell-effects.md) | **67 effect classes**, default numeric mana **0–500**, tier, spell school, applicable augments and source guide effects. Default effect tiers: **33 Tier I / 19 Tier II / 15 Tier III** across the classes, not all necessarily player-learnable. | Per-effect config defaults; a finished glyph sequence has separate cost/validity and config overrides |
| [Base Java cast Forms and Augments](../inventories/ars-nouveau-spell-forms-augments.md) | **5** direct cast-method classes + **13** augment classes; all individual default mana/tier and direct descriptions | Form and augment cost stack on effects; nonstandard forms (Rune/Orbit) are in Effect source classes |
| [Elemental glyph learning recipes](../inventories/ars-elemental-glyph-recipes.md) | **39** exact items + vanilla XP: 14×27 / 15×55 / 10×160 | New conditional filters, projectiles, utility, healing/damage/terrain and summons, not merely four elemental damage types |
| [Controle conditional learning recipes](../inventories/ars-controle-exact-recipes.md) | **9**, exact items/XP in full **15** recipe catalog | NOT/OR/XOR/XNOR, directional/height/Random filters, precise delay, for programmable targeting and spell logic |
| [Additions spell learning recipes](../inventories/ars-additions-high-impact.md) | **3**, Mark/Recall/Retaliate all **160 vanilla XP**, Tier III; class mana default **25/50/25** | Remote selected target & counterattack; requires context/Reliquary, not unconditional teleport |
| **Total known Ars-family glyph learning JSONs** | **133 = 82 base + 39 Elemental + 9 Controle + 3 Additions** | All addon trees inspected for standalone glyph source JSON; other four addons add routes rather than additional static glyph-learning recipes |

**Cross-cutting safeguard:** A default glyph **0 mana** part does not make its full spell free; runtime cost is configured by `AbstractSpellPart.getCastingCost()` and modifiers. [Compound spell pipeline and validator audit](../inventories/ars-nouveau-compound-spell-economics.md) gives 15/20/35/50/60 default-mana examples, per-action augment cost overrides, equipment discount and Pre/Post debit distinctions, and separate construction/cast validity. All learning recipes' `exp` values use **vanilla XP**, which never grants TMM MXP.

### 8.2 All known static source recipe trees and source class operations

- [Base recipe discovery](../inventories/ars-nouveau-top-level-recipe-index.md): **709 exact source JSON file paths** (639 root and 70 nested); still a discovery index, not 709 parsed recipes.
- [Seven addon recipe discovery](../inventories/ars-nouveau-addon-recipe-tree.md): **574 file paths** (Additions 115, Controle 15, Elemental 182, Elemancy 250, 'n Spells 7, Ocultas 5, Unification 0 because imported recipes are dynamic). The **two trees together contain 1,283 enumerated recipe JSONs**, not 1,283 unique gameplay powers or installed-enabled crafts.
- **Parsed contents:** base **175 unique** recipe JSONs (previous 142 + [19 Armor Threads](../inventories/ars-nouveau-armor-threads.md) + [14 Imbuement operations](../inventories/ars-nouveau-imbuement-recipes.md)). Other inspected addon exact-json subsets include **all 15 Controle**, **all 39 Elemental glyph learning**, **eight Elemental ritual tablets + ten selected Focus/urn/worker tools**, **16 selected Additions mechanics**, **all seven 'n Spells**, **all five Ocultas**, and **seven Elemancy Essence + nine Focus + four sampled armor variant recipes**. Hence **295 unique recipe JSONs parsed across the Ars family** in the inspected, nonoverlapping recipe subsets. The rest are linked filenames only.
- [Base ritual Java classes](../inventories/ars-nouveau-ritual-operations.md): **24/24**, with [parent Source charging contract](../inventories/ars-nouveau-ritual-charging.md). Nine native classes return a positive `getSourceCost`; the cost is requested for an actual effect, batch or refresh, not every tick by default. Remaining 15 inherit zero from `AbstractRitual` for that **one standard Source-charge path**; other sub-work may consume Source.

### 8.3 New concrete bottom-end and upper-end mob automation routes

| Distinct machine/ritual | Minimum working setup and actual typed resource | Output and operational ceiling | TMM gate/quest concern |
| --- | --- | --- | --- |
| **Base Drygmy Henge** | Tamed Drygmy→Charm→Henge, Source Jar, neighboring inventory, eligible real mobs or Mob Jars. Base **1,000 Source** per completion cycle. | Repeatable fake-player **loot-table items and XP gems** without killing the mobs. More creature diversity and workers improve output. | TMM protected/rare mob policy already approved: capture-integration #373 stages `#tmm:replication_blacklist` into `ars_nouveau:drygmy_blacklist`, preserving native Iron Golem exclusion and bound worker restrictions. Integration status and effective tag expansion still unverified |
| **Additions Source Spawner** | Craft block using Drygmy Charm + Summon Focus and Conjuration Essence; requires valid populated Mob Jar template, Source within 5 and living player within 16. Template search radius 10 Manhattan. | **Real entity spawning**, default 4 attempts every **200–800 ticks**. Source per spawned mob = **200 + 50×maxHP + 1,000 if persistent mob + 10,000 if tagged boss**, unless custom recipe overrides. | Approved: obey TMM's existing spawner/replication eligibility. Technical gap: native Additions checks `#ars_nouveau:jar_release_blacklist` + `#c:bosses`; existing #373 tag aliasing of `#tmm:replication_blacklist` into Apothic Spawners/Drygmy and `#tmm:capture_blacklist` into `ars_nouveau:jar_blacklist` does **not** establish this alternate consumer's coverage. Check/bridge it in capture integration, not through a new gameplay decision |
| **Base Disintegration ritual** | Active Brazier+tablet near qualifying monsters; **300 Source** request after a successful positive XP batch; scans every **60 ticks** in radius5 AABB. | **Removes real monsters**, drops **2× XP as Ars experience gem items**, **no ordinary mob loot**. The source removes mobs before requesting Source; zero-XP removals may not trigger a debit. | Different from a conventional kill and does not itself generate MXP; don't use for ordinary item farming |
| **Elemental Attraction** | Ritual Brazier + tablet, optional Earth Essence per radius +1; base **8-block radius**, **10 Source per active batch**, every10 ticks. | Pulls **all unblacklisted entity objects including dropped item entities**, not merely living mobs. | Check actual `ars_elemental:attraction_ritual_blacklist` vs bosses; a native helper for sorting physical mobs/drops |
| **Elemental Repulsion** | Brazier + tablet, optionally bone for undead-only; **15-block radius**, **10 Source** per effect batch. | Applies Repel effect for 200 ticks; scan every20ticks when entities present, every80ticks when idle. | Normal path filters player/boss; bone path has a potentially different undead-only exclusion predicate. Document, don't assume blanket no-boss interaction |
| **Elemental Siren Shrine** | Siren Charm and Mermaid worksite/aquatic variety, Source Jar. Default **1,000 Source/generation** and 30 work progress; output cap five item stacks. | Repeatably draws actual **fishing/treasure/junk loot tables** using fake-player context, with diversity influencing extra rolls/treasure chance. | Approved native automation. No Reliquified Angler's Hat valuables-tag bonuses; it constructs score-dependent synthetic Luck of the Sea and can roll the shared treasure pool, including weighted Parchment Fragment if #433 integrates. This is not a design blocker |
| **Ars Additions XP Jar** | Filter item pickup list on XP Jar (Void Jar derivative); item stacks voided in player's inventory. | **1 vanilla XP per 2 voided items**, stores 0/1 remainder; destroys items. Base Void Jar instead grants 5 mana per item. | Neither a physical XP-storage Tome nor an MXP converter; optional renewable trash→vanilla XP sink |
| **Elemental Urn of Endless Waters** | Connect target water-accepting fluid handler/Botania Apothecary; numeric Source within 6, default **100 Source per successful fill**. | Up to **1,000 mB water**/destination per **60 ticks**, including selected compatible receivers. | Source→water, **not** Source→FE. Deliberately narrow fluid adapter |

These systems are **not interchangeable**. Drygmy repeats loot without a death event; Source Spawner creates mobs that can be killed or moved; Disintegration removes mobs but produces no normal loot; Vitalic needs eligible events; Siren rolls fishing tables. Their Source budgets, tags, inventory output and TMM progression implications differ.

### 8.4 Addon progression and automation beyond base book tier

- [Elemental addon core items and eight extra ritual tablets](../inventories/ars-elemental-high-impact.md): **Attraction, Repulsion, Detection, Tesla/Zapping, pollination/squirrels/forest**. Detection applies Glowing for ten minutes around 128-block AABB; source class returns **250 Source cost** but has no internal needs-Source flag request, so **do not claim** this amount is actually debited. Tesla's Air Essence catalyst can enable **player-targeted lightning**. Standard Elemental focus and turret examples cost **5,000 Source to craft**; Mark of Mastery **10,000 Source**.
- [Elemancy seven Essence paths and gear](../inventories/ars-elemancy-progression-recipes.md): each of six pairs or four-element Essence uses **3,000 Source** in imbuement. Six duo paths have two numbered armor variants each; **Elemancer** has six numbered gear variants/three focus recipes. Sampled `sauce:armor_upgrade` entries cost **7,000 Source per upgrade**. This equipment ladder is **separate from Novice/Apprentice/Archmage glyph capacity** and does not force every school choice.
- [Controle full recipe inventory](../inventories/ars-controle-exact-recipes.md): 9 conditional glyphs plus Remote, Scryer's Linkage, Temporal Stability Sensor, Portable Brazier Relay, Warp Scroll Holder and Warping Prism; true redstone/item/fluid/FE transport is endpoint-dependent.
- [Additions practical systems](../inventories/ars-additions-high-impact.md): **Handy Haversack** binds actual external item handler and deposits selected pickup stacks remotely, **not** storage inside a backpack. **Bulk Scribing** uses an Imbuement recipe costing a caster's stored **spell Source cost**. Locate Structure ritual has 15 target definitions. **Arcane Permanence chunkloading tablet is recipe-disabled by default** through its `neoforge:conditions`; if TMM enables, defaults to **10,000 Source** on start and every **24,000 ticks** while online, initially 1×1 chunk. Do not write an enabled survival quest before owner changes that setting.
- [Ocultas exact recipes](../inventories/ars-ocultas-exact-recipes.md): Spirit Attuned Gem 500 Source, Sacrificial Altar crafting 5,000 and separate **5,000 Source per Occultism bowl sacrifice/item flag**; still source-derived nonconsuming jar target behavior. Gold↔silver Imbuement recipes are **2,000 Source** using the `source` key. Its Marid stabilizer recipe is a separate legacy-plural path; preserve the Occultism tier gate.
- [Ars 'n Spells seven recipes](../inventories/ars-n-spells-exact-recipes.md): 2,000 Source Spell Transcription, 2,500 Binding, 500 Uninscription, 1,500 Mana Infusion, 2,000 Mana Well. Owner reports **working cross-mana integration**; effective enum, school power, practice and cooldown remain configurable and have not been independently checked in this source-only pass.

### 8.5 What the current 49 quests are missing, and changes authorized

[Existing 49 quest/task reconciliation](ars-nouveau-quest-map.md) now has a second-pass handoff with **additional research-only candidate lessons**. **No new FTB IDs, runtime chapter edits, quest rewards, KubeJS scripts or mod patches** were made; the compact-layout work remains in PR #395. Current work is research-complete in the sense of a broad second-pass **survey**, but not a 1,283-recipe exhaustive content audit or a final playable quest walkthrough.

**Remaining owner/technical split (2026-11-21):** No new gameplay decision is needed for Drygmy, Source Spawner eligibility, Siren loot or Ocultas Source-for-sacrifice. Owner says Ars 'n Spells mana already works; no extra mode or balance vote is needed to continue survey/quest drafting. Capture integration #373 must verify Source Spawner consumes the intended shared policy (not automatically proven by its different jar-release tag), and verify Drygmy's staged native tags in the assembled pack. Ars 'n Spells effective enum and addon hooks are documentation/changed-code QA matters, not established bugs. Separate optional owner config choice remains whether to enable disabled Arcane Permanence; Handy Haversack deposits to an external inventory, consistent with TMM magic-only logistics. Apply gameplay QA only to changed TMM paths.

**Explicit QA scope:** The repository policy/user decision remains **no standalone validation of untouched upstream mod behavior**. Read source/recipes and write quest guidance without game-testing every spell or ritual. If TMM changes a blacklist, loot table, config, XP-to-MXP adapter, custom quest predicate or external capability bridge, test **that TMM-owned change** and its affected boundary only.
