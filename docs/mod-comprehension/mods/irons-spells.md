# Iron's Spells 'n Spellbooks — gameplay and progression profile

| Field | Value |
| --- | --- |
| Manifest ID / mod ID | `irons-spells` / `irons_spellbooks` |
| Pinned artifact | `irons_spellbooks-1.21.1-3.16.3.jar`; packwiz SHA-512 `fd782f98c6c59b193c4832f33775291d2a7e639e1e23dd47510bfb494d99d182bec1253566b9394f96c733a0d0108be34bd729614b82dc29e55e096fdeb96f5b` |
| Target | Minecraft 1.21.1 / NeoForge; current TMM integration branch `test/full-integration` |
| Source snapshot | `iron431/irons-spells-n-spellbooks`, tag [`v1.21.1-3.16.3`](https://github.com/iron431/irons-spells-n-spellbooks/tree/532c98dfa5b219f3927aca9b23d9042c00e767bb) (peeled commit `532c98dfa5b219f3927aca9b23d9042c00e767bb`, dated 2026-08-18). The tag's Gradle metadata declares mod version `1.21.1-3.16.3`, matching the pinned artifact; source-to-binary reproducibility has not been established. |
| Profile status | `researching` — the source and official guide provide a strong progression map, but the complete pinned-JAR recipe/loot audit and some paths still need reconciliation. |
| Evidence limits | Downloaded the exact Modrinth pin to a transient inspection path and recomputed SHA-512; it matches packwiz. No JAR was committed. No client/world behavior is described as tested. |
| Quest boundary | Existing chapter is an optional checkmark placeholder. No quest or runtime files were changed. |

## Executive gameplay map

Iron's Spells 'n Spellbooks is a loot-driven RPG magic progression system. Early play revolves around finding spell scrolls, spellbooks and Arcane Essence in magical structures, then using an Inscription Table to put a spell into a reusable book. Nether-level materials unlock the Scroll Forge, better books and school equipment. Netherite-era progression adds the Arcane Anvil, which upgrades scrolls/equipment and imbues weapons. Dangerous structures and bosses supply specialized materials, unique gear and repeatable late-game resources. A separate Eldritch path uses recovered manuscript pieces to learn Eldritch spells.

### Start here: guide, spellbook and first cast

- **Guide acquisition:** With Patchouli installed, combine one vanilla book and one `irons_spellbooks:arcane_essence` in a shapeless recipe to craft the `patchouli:guide_book` for Iron's. The book is flagged as a pamphlet and its landing page links to the external wiki, but it also contains nine useful pages across Progression, Schools of Magic, Spell Books, Scrolls and Casting Implements. It teaches the broad ladder and points to recipes; it does not fully replace JEI or spell out every structure/boss procedure. Source: [`patchouli_book.json`](https://github.com/iron431/irons-spells-n-spellbooks/blob/532c98dfa5b219f3927aca9b23d9042c00e767bb/src/main/resources/data/irons_spellbooks/recipe/patchouli_book.json) and [`book.json`](https://github.com/iron431/irons-spells-n-spellbooks/blob/532c98dfa5b219f3927aca9b23d9042c00e767bb/src/main/resources/data/irons_spellbooks/patchouli_books/iss_guide_book/book.json).
- **Find starting gear:** Loot magical structures and spellcasting enemies for scrolls, a beginner spellbook, Arcane Essence and other equipment. A scroll can be consumed directly for its spell; the official guide describes direct scroll casting as free of mana and cooldown, but check the pinned item behavior before teaching it as a reliable fallback.
- **Make a reusable spell:** Craft an Inscription Table, put a compatible Iron's spellbook and a spell scroll into its interface, select a book slot, then inscribe. The source menu accepts only `SpellBook` and `irons_spellbooks:scroll` inputs. A successful inscription consumes the scroll and writes its spell/level into the selected slot; a cancellable `InscribeSpellEvent` can veto it. A spell that can be removed can be extracted as a scroll from its slot. [source: `InscriptionTableMenu.java`]
- **Cast and maintain a loadout:** Equip the spellbook in its Curios spellbook slot, select/equip a spell through the Iron's spell UI, then cast with the configured keybind. Spells use their own mana cost, cast time, cooldown and level parameters; costs and effects are spell-specific. Iron's HUD shows mana/spell information, with client-configurable anchors that must be checked against TMM overlays.

## Progression from entry to late game

| Stage | Player route and unlock | Inputs / result | Reference and limits |
| --- | --- | --- | --- |
| 1. Early exploration | Search wizard structures, village Priest houses, dungeon-like sites and spellcasting enemies. Collect spell scrolls, book options, Arcane Essence and school materials. | Loot and combat yield the first spells and equipment; structures can contain traps and hidden treasure. | Official [Structures](https://iron.wiki/structures/) page says secrets do not require block-breaking. Exact structure frequency, loot tables and TMM biome fit still need pinned-data audit. |
| 2. Inscription | Craft the Inscription Table (book above 3 wooden slabs, 2 wooden fences at the bottom corners); craft or loot a beginner book; insert a scroll and choose an empty slot. | Copper Spell Book recipe: 2 copper ingots, 6 paper, 1 string. A successful inscription consumes the scroll; slots and bonuses improve with later book tiers. | Guide: **Early Game**, **Basic Spell Books**, **Scrolls**; JEI: Inscription Table and book recipes. The menu supports taking removable spells back out as scrolls. |
| 3. Midgame spell production | Reach Nether-level resources and craft the Scroll Forge (3 polished deepslate across the top, 4 crying obsidian in the lower rows). Select an eligible spell; provide paper, ink and its focus. | Ink sets the spell level to the lowest level allowed by that rarity; focus must match the school tag; spells with a higher minimum rarity are rejected. Taking a valid result consumes one paper, ink and focus. | Guide: **Progression → Mid Game**, **Ink**, **Schools of Magic**; JEI: Scroll Forge. The pinned source menu and recipe confirm the exact recipe and selection rules. |
| 4. Midgame equipment | Progress from Copper/Iron books to Gold and Diamond books; use Hogskin (from Hoglins), Arcane Essence and Arcane Cloth. Raid magical structures for blank Runestones and school gear. | Gold Spell Book: 3 gold ingots + 4 Arcane Essence + 2 Hogskin. Diamond Spell Book: 2 diamonds + 4 Hogskin + 2 Magic Cloth + 1 enchanted book. Magic Cloth feeds armor/book recipes; a blank rune can be inscribed as a school rune, and school mages drop their matching rune. | Guide: **Progression → Mid Game**, **Basic Spell Books**; JEI: Gold/Diamond Spell Book, Magic Cloth and school armor. Exact school-armor material counts remain JEI-driven. |
| 5. Netherite / Arcane Anvil | Craft the Arcane Anvil (3 amethyst blocks, diamond, 2 polished deepslate and an anvil). Craft Netherite Spell Book from Netherite, Magic Cloth, Blood Vials, a Lightning Bottle and a Ruined Book. | Base Upgrade Orb recipe uses 4 Arcane Ingots, 4 Cinder Essence and one item in `irons_spellbooks:mithril_material`; school/stat variants combine a base orb with 8 matching runes. Default max is 3 upgrades per supported item. The Anvil also levels scrolls and permanently imbues eligible weapons. | Guide: **Progression → Late Game**, **Upgrading**, **Unique Spell Books**; JEI: Arcane Anvil, Upgrade Orb and imbuement. Arcane Debris and Cinder Essence are separate progression materials. |
| 6. Eldritch branch | Find 8 Ancient Knowledge Fragments; craft them around 1 Echo Shard to make an Eldritch Manuscript, then use it and select one spell in the research UI. | The manuscript is consumed to learn one Eldritch spell. After learning, Echo Shards are its focus for scroll crafting. The book explicitly says fragments are scattered through the world but does not identify exact loot sources. | Guide: **Schools of Magic → Eldritch**; JEI: Eldritch Manuscript; use the learned-spell screen. Exact loot distribution and duplicate-learning behavior remain to audit. |
| 7. Boss and advanced loot | Craft a Wayward Compass (4 Arcane Essence, 4 iron ingots and a Soul Lantern); it binds to the nearest Catacombs within its locator search, and can retry when used if still unbound. The guide recommends enchanted diamond gear for the Dead King. Build a Dead King Phylactery from 8 Phylactery Shards around a Soul Lantern; after a kill, interact with the hovering Dead King Soul at its spawn point while holding the phylactery to restart the encounter. In the Nether Citadel, use the Cinderous Soulcaller only near a Cinderous Keystone: the player must be within 22 blocks and no more than 2 blocks below its Y; it consumes the item only when it successfully summons Tyros, and fails if a Tyros is already nearby. Break Armor Piles in Ancient Battlegrounds with a diamond pickaxe to awaken Ancient Knights. | Catacombs/Dead King yield boss-specific materials and repeat resources; Ancient Knights drop Cinder Essence used for cauldron/anvil progression. The Cinderous Soulcaller recipe uses 4 Cinder Essence, 2 Netherite Scrap and 1 Echo Shard; its success check uses an 80-block-wide nearby-boss AABB around the keystone. The Dead King Phylactery is in the pinned JAR's `dead_king_respawnable` item tag. | Guide: **Progression → Late Game**; JEI: Wayward Compass, Phylactery, Ancient Map and Soulcaller; official [Structures](https://iron.wiki/structures/) for locations. Exact boss-room activation and item consumption must still be confirmed in-game. |
| 8. Alchemy and long-term optimization | Craft an Alchemist Cauldron from 6 iron ingots, 1 vanilla cauldron, 2 Arcane Essence and 1 Cinder Essence. Add water, then use JEI categories to brew/empty/fill potion and ink containers, recycle scrolls, and produce elixirs. | 4 input + 4 output slots. The profile's two JAR inventories agree on 46 cauldron-related JSON records, but disagree on type counts: one lists 16 `fill`, 16 `empty`, 13 `brew` and 1 `soak`; the evidence ledger lists 16 `fill`, 16 `empty` and 14 `brew`. Keep the type breakdown provisional until the pinned data files are enumerated again. Examples reported in the inventory include 1,000 mB Uncommon Ink + an iron ingot → 250 mB Rare Ink and Hogskin + 1,000 mB Blood → Bloody Vellum. A mob/player route can create Blood Vials in water. | JEI: Alchemist Cauldron. Glass bottles extract container outputs; water is a required operating fluid. The pinned JAR has 30 Create `emptying`/`filling` compat recipes; post-release source-only pipe/pickup changes are not assumed present. |

### Schools and scroll foci

The inspected source defines eight ordinary schools plus the separately gated Eldritch school. These item tags are the real input contract for Scroll Forge selection; not every school has a vanilla focus.

| School | Source focus tag item | Typical specialization / evidence |
| --- | --- | --- |
| Fire | `minecraft:blaze_rod` | Damage and damage-over-time; source `fire_focus` tag |
| Ice | `irons_spellbooks:frozen_bone` | Damage and control; source `ice_focus` tag |
| Lightning | `irons_spellbooks:lightning_bottle` | Concentrated damage; source `lightning_focus` tag |
| Holy | `irons_spellbooks:divine_pearl` | Healing/support; source `holy_focus` tag |
| Ender | `minecraft:ender_pearl` | Arcane utility and mobility; source `ender_focus` tag |
| Blood | `irons_spellbooks:blood_vial` | Damage and self-enhancement/necromancy; source `blood_focus` tag |
| Evocation | `minecraft:emerald` | Broad offense, defense, utility and summons; source `evocation_focus` tag |
| Nature | `minecraft:poisonous_potato` | Debuffs and damage; source `nature_focus` tag |
| Eldritch | `minecraft:echo_shard` | Learned separately; manuscript required before ordinary use; source `eldritch_focus` tag |

The wiki's school pages and homepage are not fully consistent about school names/counts. This table follows the source registry and tag JSON at tag `v1.21.1-3.16.3`; do not use an unversioned wiki list to overwrite a pinned registry.

#### Subsystem status and open work

| Subsystem | Source coverage in this profile | Completion state / next evidence needed |
| --- | --- | --- |
| Spellbooks, slots and casting | Inscription/selection mechanics, spell scrolls, Curios book slot, HUD and school attributes | `spell_book_equip` advancement detects equipping a Curios spellbook; no generated cast/inscription advancement found. Exact pinned-client controls, mana debit edge cases and book-by-tier stats need verification. |
| Scroll generation and upgrades | Scroll Forge inputs/rules, ink rarity, school focus, Arcane Anvil roles | Core menu logic mapped; all pinned recipes, per-target eligibility and failure costs remain to audit. |
| Schools and spells | Registry has eight ordinary schools plus Eldritch; 111 core spell registrations in the inspected source snapshot | All spell-by-spell costs, levels, ranges, configuration and progression not yet mapped. |
| Cauldron/alchemy | Water-gated cauldron, item slots, recipe categories, blood-vial route, automation surfaces | The full input/output contract, recipe quantities, entity handling and TMM pipe behavior need source/JAR and client checks. |
| Gear and materials | Arcane Cloth, Runestones, Upgrade Orbs, school gear, Arcane Debris, Cinder Essence | Main tier outline known; exact recipe tiers, loot tables and caps need pinned data. |
| Structures and bosses | Named wizard sites, Ancient Battlegrounds, Ice Spider Den, Catacombs, Citadel | Exact placed features, structure loot, boss triggers and repeat costs need full pinned-data reconciliation. |
| Eldritch branch | Manuscript recipe, spell-learning UI and Eldritch focus path | Core gate mapped; discovery distribution, failed/duplicate learning and spell-by-spell unlocks remain open. |
| Ars 'n Spells bridge | The exact TMM pin is profiled separately in [Ars 'n Spells](ars-n-spells.md): `ars_n_spells-3.3.5.jar`, Modrinth version `5OeZngO7`; its profile traces source commit `f25c379d5e0521821ab6e80a9f485ae41d4506a0`. | Source-defined mana modes, cross-casting, spell-power scaling and school progression are mapped in the addon profile. Effective TMM settings, client behavior and measured interactions remain unverified. |

## Core item and station contracts

| System | Typed inputs | Transformation / output | Costs, restrictions and gaps |
| --- | --- | --- | --- |
| Inscription Table | Spellbook item + scroll item; menu slot selection | Adds a scroll's spell and level to a chosen book slot; removable spells can be converted back to a scroll | One scroll consumed on successful inscription; event can cancel. No claim that just possessing either item proves operation. |
| Scroll Forge | Blank paper + `InkItem` + item in the selected school's focus tag + selected craftable spell | One spell scroll at the minimum level matching ink rarity | One of each input removed on taking a valid result. `allowCrafting`, minimum rarity and school match all gate output. [source `ScrollForgeMenu.java`] |
| Arcane Anvil | Scroll, supported equipment and/or compatible upgrade orb | Levels scrolls, upgrades supported books/armor, or imbues one eligible weapon with a spell | Scrolls consumed for imbue and cannot be recovered; max equipment upgrades default 3, config-dependent. Eligibility is per target/operation. |
| Alchemist Cauldron | Recipe-specific item inputs, water in the cauldron, sometimes entity/player interaction; bottle/container for output | Potion/elixir brewing, ink upgrade, scroll recycling, Blood Vials | Four input/four output item slots; recipes define quantities and output. Cauldron does not establish generic fluid automation; source adds modded pipe/pick behavior, requiring in-pack confirmation. |
| Eldritch learning | Eight Ancient Knowledge Fragments + Echo Shard; completed manuscript used from inventory/UI | Learns one chosen Eldritch spell; enables its later scroll/item recipes | Manuscript is a distinct gate, not an ordinary focus shortcut. Need verify duplicate spell, failed selection and fragment loot cases. |

### Three useful survival setups

1. **Build a reusable starter kit:** Loot early scrolls and a beginner spellbook, craft the Inscription Table, and inscribe one reliable utility spell plus one defensive/offensive spell. Keep spare scrolls until you confirm book-slot compatibility; use the actual Iron's HUD/keybind to select and cast.
2. **Specialize scroll output:** Build the Scroll Forge when Nether-level materials are available. Keep school foci, ink grades and paper together; use JEI to compare eligible spells before taking the result. This avoids consuming rare ink on a school mismatch or spell below minimum rarity.
3. **Turn surplus into alchemy inputs:** Once the Alchemist Cauldron is available, use JEI's fill/brew/empty categories to convert appropriate ingredients into elixirs or upgraded ink, or recycle unwanted scrolls. Supply water and extract with the recipe's correct container. Blood Vial collection is a separate entity-input route with its own handling and has not been demonstrated in the current pack.

## Structures, bosses and source-coverage boundary

The pinned structure sets use random spread, so spacing/separation values are average placement grids and minimum chunk gaps, not guaranteed map distances. The shipped biome tags and placed heights are more reliable than a generic biome description:

| Structure | Biome filter / placement | Player route |
| --- | --- | --- |
| Catacombs | TMM biome tag spans hills, taiga, jungle, forest, stony shore, ice spikes, savanna/desert/plains/swamp and caves; spacing 72 / separation 48 chunks; start height uniform −40 to 20 | Wayward Compass points to it; contains Dead King dungeon. |
| Ancient Battleground | Bastion-remnant biome tag; spacing 35 / 12; start height 35–75 | Nether; mine Armor Pile to awaken Ancient Knight. |
| Citadel | Bastion-remnant biome tag; spacing 100 / 80; start height 33 | Nether; use Ancient Map and Cinderous Soulcaller. |
| Mangrove Hut | `#c:is_swamp`; spacing 25 / 18 | Apothecarist site. |
| Mountain Tower | mountain tag, snowy plains/taiga, grove, ice spikes; spacing 45 / 33 | Ice mage site. |
| Pyromancer Tower | hills, forest, plains, swamp tags; spacing 85 / 50 | Fire mage site. |
| Evoker Fort | hill/forest/desert/plains/savanna tags; spacing 85 / 50 | Archevoker/illager complex. |
| Ice Spider Den | snowy/icy biome tags; spacing 85 / 50 | Silk Touch egg; breaking the protective ice wakes a nearby spider. |
| Impaled Icebreaker | frozen ocean/ice-spike/frozen-peaks plus optional Terralith tags; spacing 75 / 50 | Map-led rare shipwreck. |

The official structure guide identifies wizard sites (Mountain Tower, Mangrove Hut, Pyromancer Tower, Evoker Fort and Priest House), Ancient Battlegrounds, Impaled Icebreaker, Ice Spider Den, Catacombs and Citadel. The exact JAR additionally places each set with a random-spread grid; for example, Catacombs use 72/48 chunk spacing/separation while Ancient Battlegrounds use 35/12. TMM-biome tags and optional Terralith entries are enumerated above. These locations drive much of the loot progression. The official wiki says Mountain Tower is in mountain/snow biomes, Mangrove Hut in swamps, Pyromancer Tower in forest/grassy biomes, Evoker Fort in forest/plains/desert/savanna, Ancient Battlegrounds in the Nether and Ice Spider Den in icy biomes; a Wayward Compass points to Catacombs, while an Ancient Map can locate the Citadel. These are wiki leads pending exact 1.21.1 worldgen data verification against Tectonic.

The Catacombs/Dead King and Citadel boss chains, Eldritch research, equipment progression and cauldron recipes are separate branches. The profile must not imply that killing a boss, crafting a summoning item or looting a structure is automatically detected by FTB Quests; detector support is not established.

## Version and source caveats

- The pack pin is `1.21.1-3.16.3` from Modrinth. The exact `v1.21.1-3.16.3` source tag matches the locked version and predates the release's known post-release changes. The pinned JAR contents and hash were inspected directly; a reproducible source-to-binary build comparison remains unavailable. A later development commit is not used as evidence for pinned behavior.
- The official wiki includes version-select controls and legacy/version-specific pages. The progression page currently exposed as `/1_19/progression/` is older; use it only for orientation, then verify items and exact costs against current JEI/JAR.
- The source snapshot defines eight ordinary schools and the Eldritch school, while some official web summaries show different lists. The registry and school focus tags take precedence for this source snapshot.
- Current TMM includes the exact Ars 'n Spells 3.3.5 pin, whose source-level routes are documented in [its profile](ars-n-spells.md). Keep effective settings, cast/resource behavior, and equipment or attribute stacking provisional until validated in the pack; a source profile does not establish runtime outcomes.
- Historical dedicated-server startup only proves dependency loading. It does not prove client UI, structure placement, spell behavior, quest credit or Tectonic worldgen compatibility.


## Low-hanging setups useful in ordinary survival

### 1. Inscribe a small reusable spell loadout — early game
- **Goal and payoff:** turn found scrolls into repeatable spells in an equipped spellbook instead of spending every scroll as a one-use cast.
- **Minimum entry and ingredients:** any compatible Iron's spellbook, a spell scroll, and an Inscription Table.
- **Build/use steps:** craft the table; place the book and scroll in the menu; select an empty book slot; confirm inscription; equip the book in Curios and use the spell UI/keybinds to select and cast.
- **Sustainability and limits:** a successful inscription consumes the scroll. Slot count, spell eligibility, mana, cast time and cooldown depend on the book and spell; the source menu can cancel inscription through an event.
- **Evidence / pack test:** source `InscriptionTableMenu`; official [getting-started guide](https://iron.wiki/) describes scrolls as direct one-use casts or inputs to reusable books. Not client-tested.

### 2. Use school focus and ink to target crafted scrolls — midgame
- **Goal and payoff:** produce a scroll for an eligible spell in a chosen school and minimum level allowed by ink rarity.
- **Minimum entry and ingredients:** Nether-resource Scroll Forge, paper, one rarity-specific ink, matching school focus.
- **Build/use steps:** craft the forge; load ingredients; choose a craftable spell matching focus and ink rarity; take the result.
- **Sustainability and limits:** consumes one of each input per scroll; high-rarity inks and rare foci are progression costs; exact yields for cauldron-produced ink require full recipe audit.
- **Evidence / pack test:** exact selection logic and input debit source-confirmed; not client-tested.

### 3. Specialize an equipment slot — mid/late
- **Goal and payoff:** add the needed mana, school power, spell slots, cooldown or other stat to a supported item.
- **Minimum entry and ingredients:** Arcane Anvil, one supported item, compatible upgrade orb/slot item/scroll and an open upgrade/spell slot.
- **Build/use steps:** build the anvil; inspect target eligibility in JEI/tooltip; insert the exact pair; inspect result; equip and confirm attribute.
- **Sustainability and limits:** standard upgrades are capped at three by default; imbuing consumes the scroll and can replace a spell in a full weapon container.
- **Evidence / pack test:** menu and config source-confirmed; attribute and input behavior require client test.

## API, integration and pack-config surfaces

| Hook or customization surface | Namespace / exact file, API or serializer | Capabilities / constraints | Current TMM override? | Evidence |
| --- | --- | --- | --- | --- |
| Spell definition/config | Registry `irons_spellbooks:spells`; `SpellConfigManager`; `irons_spellbooks_spell_config` global configs and datapack `data/<namespace>/irons_spellbooks_spell_config/<spell>.json` | Configure school, min rarity, mana cost, cooldown, max level and spell parameters; world datapack supersedes global config when present per changelog | No tracked Iron's spell config found | `[source: v1.21.1-3.16.3]` [docs: 3.15+ changelog] |
| Server settings | `ServerConfigs` | Mana regen multiplier, initial mana after death, scroll recycle chance, max upgrades, scroll merging, spell griefing and other behavior | No tracked Iron's server config found | `[source: v1.21.1-3.16.3]` |
| Client HUD | `ClientConfigs`: mana bar anchor/display, spell bar display, offsets | Contextual mana bar default; spell bar default always; anchor includes hunger/experience-like areas; check overlay collision with TMM UI | No tracked client config found | `[source: v1.21.1-3.16.3]` |
| Spell events/API | `SpellPreCastEvent`, `SpellOnCastEvent`, `InscribeSpellEvent`, `SpellCooldownAddedEvent`; `AbstractSpell` and casting APIs | Addon can inspect/modify/cancel casts and inscription; actual external spell implementation still needs correct source/dependency versions | Ars 'n Spells installed; other bridge effects not claimed | `[source: v1.21.1-3.16.3]` |
| Recipe types | `irons_spellbooks:alchemist_cauldron_{brew,fill,empty}`; JEI plugin | Cauldron input/output and fluids; recipes can be datapack supplied | No TMM custom recipes found | `[jar: SHA-512]` + `[source: v1.21.1-3.16.3]` |
| Summon and portal state | `SummonManager`, `PortalManager`, `PocketDimensionManager` | Owns summon expiration/tracking, portal records and per-player pocket-dimension data | No TMM custom hooks found | `[source: v1.21.1-3.16.3]` |
| Required runtime dependencies | `neoforge.mods.toml` requires NeoForge, MC 1.21.1, GeckoLib ≥4.7.5.1, PlayerAnimator ≥2.0.1+1.21.1, Curios ≥9.0.5+1.21.0, Iron's Lib `[1.21.1-2,1.21.1-3)` | These requirements are on both client and server | Present in current pack; PlayerAnimator is the documented beta library exception | `[jar: SHA-512]` + `docs/B3-DEPENDENCY-EXCEPTIONS.json` |

## Compatibility leads

| Other mod | Candidate link and why | Native / pack-built / would require bridge | Status / source |
| --- | --- | --- | --- |
| Ars Nouveau via Ars 'n Spells | Addon is explicitly intended to connect Iron's spellbooks, spell schools/power and mana with Ars glyphs/Source; potential casting and progression cross-over. | Addon bridge (not core-native). | Installed and pinned at 3.3.5; exact artifact-side hooks, TMM config and in-world behavior not validated. Existing note in `docs/PUFFERFISH-COMPATIBILITY-MATRIX.md` flags possible cross-system spell power stacking; confirm against this exact release. |
| Curios | Spellbook slot and Iron's Curios items require Curios. | Native required dependency. | Loaded in server integration; client slot UX/equipment interactions not tested. |
| Apotheosis | Source registers compatibility classes and Curios/equipment modifiers; gems/enchanting could alter spell equipment. | Optional compatibility code. | Current TMM includes Apotheosis; exact gem/attribute stacking and supported recipes not reviewed. |
| Tectonic | Iron's structures/ore and Tectonic both modify world generation. | Both worldgen systems. | Dedicated-server startup does not establish valid placement; verify structures/biomes and mithril ore in disposable worlds. |

## Quest and teaching opportunities (research, not actual quests)

Canonical quest outline is the progression table above. Every task should teach an action, with a reward aligned to a useful Iron's item/material or a thematic survival reward. An item possession trigger must not imply that a cast, inscription, cauldron brew, dungeon clear or boss victory occurred. The chapter pilot currently has none of those operations as tasks.

| Stage and teaching goal | Actionable in-game instructions | Named reference | Intended behavior vs truthful detector / fallback | Reward direction | Coverage and gating risk |
| --- | --- | --- | --- | --- | --- |
| Get/open guide | With Patchouli installed, craft the shapeless book + Arcane Essence recipe; open **Progression → Early Game** and **Schools of Magic** | Iron's Guidebook; exact category/entry names | Advancement/item tasks cannot prove page read; use explicit manual acknowledgement and teach recipe/steps in quest text | Arcane Essence or a useful ink bottle | External wiki dependency and version gap |
| Make first reusable spell | Build Inscription Table; put in book and scroll; select free slot | JEI Inscription Table | Craft detector is weak; operation needs native event/advancement or manual check | Scroll/Arcane Essence aligned to mod | Task detector cannot infer successful operation from table possession |
| Cast and manage mana | Equip spellbook in Curios; select and cast a nonhazardous spell; observe mana and cooldown | Spell UI; named spell tooltip | Requires spell-cast detector/custom advancement or manual fallback | School focus or mana ring ingredient direction | Spell effects may alter blocks/mobs; client bindings/overlay untested |
| Forge spell | Build Scroll Forge; choose paper/ink/school focus; generate eligible spell | JEI Scroll Forge; Schools page | Recipe task can detect scroll only; custom cast event verifies output if needed | Ink plus focus-associated material | Nether-resource gate and rarity threshold |
| Use Arcane Anvil | Upgrade one piece with compatible orb/scroll; read result | JEI Arcane Anvil | Item output task; not proof the item is equipped or its attribute active | Thematic upgrade orb | Max-upgrade/scroll consumption needs explicit prose |
| Eldritch discovery | Gather eight fragments and Echo Shard; craft and use manuscript; learn selected spell | JEI Eldritch Manuscript; Eldritch screen | Item task detects crafted manuscript, not spell learned; use player learned-state trigger or fallback | Echo Shard/rare ink direction | Each spell requires its own manuscript; don't award all spell learns for one craft |
| Cauldron craft | Fill with water, enter exact recipe inputs, extract output with correct container | JEI Alchemist Cauldron | Cauldron craft item insufficient; recipe output event/manual fallback | Bottle, ingredient or useful potion matching recipe | Explain whether the selected blood route consumes its entity and verify the exact output |
| Find/defeat boss | Locate structure, navigate, use intended trigger, defeat the boss | Official Structures page; Wayward Compass/Soulcaller JEI | `enter_catacombs` detects location only; no boss-defeat advancement found in the pinned generated data. Use a reliable boss-kill detector or manual acknowledgement; possession of a summoner/loot is not a boss clear | Thematic boss material or unique drop | Generation, ownership, allies and loot rules need in-pack test |

## Existing chapter / pilot reconciliation

| Walkthrough stage | Current quest ID/title and status | Intended action vs actual detector | Named reference | Gap / next action |
| --- | --- | --- | --- | --- |
| Whole Iron's chapter | `544D303000B00001`, optional, single checkmark; 2 XP + paper | No mechanic or behavior indicated | None | Replace in later quest pass with progression-linked how-to chapter; do not infer coverage from chapter presence. |

## Conflicts, limitations and version drift

- **Pinned standalone dependency requirement:** PlayerAnimator's selected release is beta and remains the documented single unavoidable beta library exception for the server-loaded Iron's stack. GeckoLib, Curios and Iron's Lib are required. Current dedicated server boot covers dependency resolution, not client animation or casts. [jar: SHA-512] [pack-test: historical server build]
- **Guide limitation:** The Patchouli Guidebook is flagged as a pamphlet and links to the external wiki, but the pinned JAR also contains nine English entries for progression, schools, spellbooks, scrolls and casting implements, plus recipe pages. It does not provide a complete construction/boss walkthrough; quest text still needs actionable steps and exact JEI references. [pinned JAR]
- **Cross-mod resource/bar risk:** Iron's uses its own mana attribute/capability; the pinned Ars 'n Spells 3.3.5 source maps `iss_primary`, `ars_primary`, `hybrid`, `separate` and `disabled` modes and a cast-payment ordering fix. These source paths are documented in [the addon profile](ars-n-spells.md); TMM's effective mode, cast debit/refund boundaries and HUD result remain untested. [docs: Ars 'n Spells 3.3.5 official release/source; exact installed bridge untested]
- **Cross-power risk:** existing Pufferfish compatibility note flags possible addition of Iron spell power to Ars spell damage; no exact 3.3.5 controlled result in this profile. Assess whether the same effect is counted once or twice.
- **Overlay/keybind risk:** client HUDs include an always-visible spell bar by default and contextual mana bar, plus Curios and spell-wheel controls. Client UI overlap/key conflict is untested with pack overlays and keybind mods.
- **Worldgen risk:** structures and mithril ore share the same world; Tectonic is installed. Server boot does not prove viable overworld/Nether generation, biome placement, rarity or structure spacing.
- **Loot/progression risk:** structure loot contains scrolls, books, runes, maps and materials; existing mod/pack loot edits may dilute or bypass intended school progression. Verify live registry loot before quest rewards.
- **Source-to-JAR limit:** downloaded the exact pinned JAR again and verified its SHA-512 against packwiz; recipes, guide entries, worldgen and advancements cited here were read from that artifact. The exact upstream `v1.21.1-3.16.3` tag matches the locked version and predates later development changes. Source-to-binary reproducibility remains unproven. Official wiki pages expose version selectors but no immutable version pin.

## Evidence ledger

| Claim / section | Source URL or inspected file + immutable SHA | Evidence / limits |
| --- | --- | --- |
| Locked artifact identity | [`mods/irons-spells-n-spellbooks.pw.toml`](../../../mods/irons-spells-n-spellbooks.pw.toml) and `/tmp/irons-research/irons.jar` | `[pin]` + `[jar: SHA-512]`; SHA512 independently recomputed and matches TMM lock. JAR directly inspected. |
| Version-aligned upstream source | [Iron's source at tag `v1.21.1-3.16.3`](https://github.com/iron431/irons-spells-n-spellbooks/tree/532c98dfa5b219f3927aca9b23d9042c00e767bb) | `[source: v1.21.1-3.16.3]`; Gradle metadata declares MC 1.21.1 and mod 3.16.3; no reproducible build comparison. |
| Official progression and docs | [Progression](https://iron.wiki/progression/), [Schools](https://iron.wiki/schools/), [Blocks](https://iron.wiki/blocks/), [Structures](https://iron.wiki/structures/), [Spellbooks](https://iron.wiki/spellbooks/), [Curios](https://iron.wiki/curios/), [Items](https://iron.wiki/items/), [Spells](https://iron.wiki/spells/), [3.16.3 changelog](https://iron.wiki/changelog/) | `[docs: current page; selected version not encoded]`; use in combination with pinned JAR/source, not as version-proof by itself. |
| Structure/loot/crafting | JAR: `data/irons_spellbooks/{recipe,loot_table,worldgen,advancement}/...`; source `src/main/resources` | `[jar: SHA-512]`; 261 recipe JSONs (including 16 cauldron fill + 16 empty + 14 brew, 38 smithing transforms, 15 Create filling + 15 emptying); 120 loot tables; 86 worldgen JSONs; 85 advancements. Counts are inventory metadata, not proof every route is reachable in world. |
| Core spell/school registry | `src/main/java/io/redspace/ironsspellbooks/api/registry/{SpellRegistry,SchoolRegistry}.java`; JAR focus tags | `[source: v1.21.1-3.16.3]`; 111 `registerSpell` statements and nine registered schools (eight ordinary schools plus Eldritch). Pinned JAR tags confirm the nine focus items in the school table. |
| Mana and casting | `api/registry/AttributeRegistry.java`, `capabilities/magic/MagicManager.java`, `api/magic/MagicData.java` | `[source: v1.21.1-3.16.3]`; base attributes/timer/formula source-derived; no measurement. |
| Machines and failure/consumption | `gui/{inscription_table,scroll_forge,arcane_anvil}/*Menu.java`; `recipe_types/alchemist_cauldron/*` | `[source: v1.21.1-3.16.3]` and JAR recipe JSON; typed operations / costs source-derived, no pack runtime check. |
| Eldritch unlock | `recipe/eldritch_manuscript.json`, `item/EldritchManuscript.java`, `network/spells/LearnSpellPacket.java`, `gui/EldritchResearchScreen.java` | `[jar: SHA-512]` + `[source: v1.21.1-3.16.3]`; menu + server item check, packet consumes one manuscript on successful learn. |
| Guidebook | Pinned JAR `assets/irons_spellbooks/patchouli_books/iss_guide_book/en_us/{book.json,categories,entries}` and `data/irons_spellbooks/recipe/patchouli_book.json` | `[jar: SHA-512]`; book recipe gated on Patchouli, pamphlet landing link, nine English entries/pages and named progression sections. |
| Native advancements | Pinned JAR `data/irons_spellbooks/advancement/irons_spellbooks/*.json` | `[jar: SHA-512]`; station/book/ink inventory triggers, `enter_catacombs` location, `spell_book_equip` Curios equip; no generated cast/inscription/cauldron/boss-defeat trigger found. `steal_from_wizard` is impossible. |
| Existing quest | `config/ftbquests/quests/chapters/irons_spells_n_spellbooks.snbt` | Direct repo inspection; one checkmark placeholder. No quest changed. |
| TMM pack load evidence | `docs/B3-TESTING.md`, `docs/B6-TESTING.md`, `docs/B3-DEPENDENCY-EXCEPTIONS.json` | Historical dedicated-server load/roster; does not establish current client/world/functional behavior. |
| Ars bridge | [Ars 'n Spells profile](ars-n-spells.md); [`mods/ars-n-spells.pw.toml`](../../../mods/ars-n-spells.pw.toml); source commit [`f25c379d5e0521821ab6e80a9f485ae41d4506a0`](https://github.com/otectus/ars-n-spells/commit/f25c379d5e0521821ab6e80a9f485ae41d4506a0) | The separate profile records the exact artifact hash and source-defined bridge paths; this profile has not run cross-system client/world validation. |

## Disposable-world test plan

| Test | Positive/boundary case and disposable-world setup | Expected observable result | Performed? |
| --- | --- | --- | --- |
| Guide and first spell | With Patchouli loaded, craft/open guide; inspect Early Game and Schools entries; then use valid book+scroll and an incompatible/full slot case | Guide renders its progression and recipe entries and external wiki link; valid insertion consumes scroll and updates book; invalid insertion consumes nothing | Not run |
| Scroll Forge | One eligible spell with matching focus/rarity ink; one wrong school focus; one below-minimum rarity ink | One scroll on valid output; no invalid output; only valid result take consumes inputs | Not run |
| Mana/casting failure order | Cast a simple spell with enough mana, then below cost; interrupt a long cast; compare mana/cooldown/scroll state | Record resource debit, cooldown, scroll and effect separately for each cast type | Not run |
| Alchemist Cauldron | Valid recipe with water; same recipe dry; extraction with valid/invalid container; blood route using representative living entity | Record fluid, item, entity and output changes; check automation only after direct use | Not run |
| Eldritch research | Use one manuscript to learn one spell; attempt already learned/invalid selection; repeat with a second manuscript | One manuscript consumed per newly learned spell; rejected attempt doesn't consume; server/client learned state syncs | Not run |
| Boss repeat | Defeat Dead King; interact with its soul at spawn while holding a Phylactery; test without it and away from spawn. For Tyros, test Soulcaller near the keystone, too far/below it, and while Tyros already exists. | Dead King item is consumed only at spawn; Tyros summon requires position and no active nearby boss, and Soulcaller is not consumed on failed condition | Not run |
| Addon resource bridge | Ars and Iron's mana states controlled separately; test each conversion/config mode, cast and affordability boundaries in 3.3.5 | Track each pool before/during/after and spell effect; detect double spend, cancellation, regen or power stacking | Not run |
| Worldgen with Tectonic | Fresh seed, eligible and ineligible biomes/dimensions; survey structure placements and mithril ore | Structures/ore occur only at intended targets with workable frequency and no required route hidden | Not run |
| Client UI | Fresh client with TMM overlay/keybind roster, test all HUD anchors and spell wheel | Spell/mana bars visible and usable without important overlay collision or key conflict | Not run |

## Unknowns and next research

- [x] Exact pinned Ars 'n Spells 3.3.5 artifact identity and source-defined cross-system routes are documented in [`ars-n-spells.md`](ars-n-spells.md). Source-to-binary reproducibility and TMM runtime behavior remain unverified.
- [ ] Audit all spell-specific parameters (mana, rarity, max levels, cooldown, range and griefing) against exact JAR config data; 111 core spell class registrations are not a full balancing pass.
- [ ] Re-enumerate the pinned Alchemist Cauldron recipe JSONs to resolve the conflicting `13 brew + 1 soak` versus `14 brew` breakdown. Then finish per-recipe input/output and automation review, especially blood collection, scroll recycling chance and Create fluid integration.
- [ ] Confirm spellbook-by-tier recipes/stat values, all boss/structure acquisition routes, loot distributions and map creation in pinned JAR/JEI.
- [ ] Test worldgen with Tectonic, Curios casting, spell overlay and representative spells in current full TMM client.
- [ ] Audit actual TMM quest authoring source before later quest redesign; current generated chapter is only a checkmark.

## Revision notes

- 2026-11-16 — Replaced the post-release source snapshot with the exact `v1.21.1-3.16.3` tag; aligned source citations and version caveats. Confirmed Wayward Compass locator behavior and retained open source-to-binary reproducibility limits.
- 2026-11-16 — Linked the exact Ars 'n Spells 3.3.5 pin and source profile, narrowing the bridge gap to untested TMM runtime behavior. Flagged conflicting Alchemist Cauldron type tallies for a fresh pinned-data enumeration; no unsupported breakdown is treated as settled.
- 2026-11-15 — Recovered the profile from a binary-corrupted Markdown prefix, then re-downloaded the exact pinned JAR and verified the packwiz SHA-512. Corrected guidebook coverage, added exact crafting contracts, cauldron recipe counts, structure placement data and native advancement signals; retained `researching` status and the quest/runtime boundary. No quest/runtime changes.
