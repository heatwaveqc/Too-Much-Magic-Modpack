# Quest chapter inventory — B8 live roster

**Source:** `manifest/mods.json` on `build/b8-qol-compass-farming-market`, commit `8444cd3e4fd042db5c105d8aae42b7739d17875c`, reviewed 2026-11-13/14. Inventory is a **design plan**, not FTB Quests content or gameplay integration proof.

- **148** desired-roster records; **140** enabled packwiz mod pins; **8** inactive/unpinned entries.
- **46** proposed chapters grouped into exactly **five** top-level categories: **Introduction (3), Progression (5), Magic (25), Exploration & Combat (9), Endgame (4)**.
- **89 of 140** active mods are explicitly associated with one primary chapter as its content or closely related addon; **51 of 140** are cross-cutting libraries, performance mods, or server tooling with **no independent chapter**. Mod IDs in tables are intended for reconciliation, not all exact game item IDs.
- This inventory uses the **newer B8 branch**. The already-authored preliminary FTB first-log quest and Akashic Tome plan are maintained separately in [PR #14](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/14), based on B6, and need reconciliation before actual quest deployment.
- **No mod addition, removal, item-gating or rewards** are authorized by this inventory.

## Five-category structure and chapter ownership

**Interpretation of Endgame appearing twice in the owner's proposed outline:** put an **Endgame Roadmap** chapter under Progression, with links and overview only; keep actual late-game mod quests and rewards in the separate **Endgame** category. No double completion/reward pathways.

### Introduction — 3 chapters

| Proposed chapter | Primary contents / related mod IDs | Scope and caveats |
| --- | --- | --- |
| **Getting Started** | `ftb-quests` | FTB quest navigation, starting equipment and first-log Trinkets Pouch; the grant is implemented separately on PR #14. |
| **Tips & Tricks** | `akashic-tome`, `appleskin`, `better-advancements`, `controlling`, `corail-tombstone`, `crafting-tweaks`, `enchantment-descriptions`, `ftb-chunks`, `inventory-tweaks-refoxed`, `jade`, `jei`, `just-zoom`, `keybind-bundles`, `lootr`, `more-overlays-updated`, `mouse-tweaks`, `natures-compass`, `oracle-index`, `polymorph` | Controls, guidebooks, death recovery, inventories, maps, claims, tools and overlooked interactions. Browsable reference, not mandatory tasks. |
| **Food & Fishing** | `aquaculture`, `farmers-delight`, `farming-for-blockheads`, `harvest-with-ease`, `pams-harvestcraft-2-crops`, `pams-harvestcraft-2-food-core`, `pams-harvestcraft-2-food-extended`, `pams-harvestcraft-2-trees` | Food, fishing, growing, cooking and seed market; verify magical crop acquisition cannot bypass progression gates. |

### Progression — 5 chapters

| Proposed chapter | Primary contents / related mod IDs | Scope and caveats |
| --- | --- | --- |
| **Basic Progression** | Shared storyline; no exclusive mod | General survival, earliest magic entry paths, storage, materials and choice of specialties. |
| **Advanced Progression** | `summoning-rituals` | Cross-mod infrastructure, rare materials, automations, summoning altar as custom-pack recipe framework; it has no built-in ritual recipes. **Review:** No separate Summoning Rituals chapter unless meaningful authored recipes exist. |
| **Endgame Roadmap** | Shared storyline; no exclusive mod | Overview of boss/late-resource requirements and links into the dedicated Endgame category; do not duplicate Endgame quests or rewards. |
| **Skills & Advancement** | `improvable-skills`, `puffish-skills`, `puffish-attributes` | Pufferfish custom tree, Improvable Skills, attribute overview, common progression mechanics. **Review:** Pufferfish tree itself is not authored yet. |
| **Exceptional Power: Reactors** | `bigger-reactors-rework` | Exceptional FE power source, fuels, balancing against magical power paths and Draconic Evolution. **Review:** Standalone progression chapter within Progression; not a general technology-tree invitation. |

### Magic — 25 chapters

| Proposed chapter | Primary contents / related mod IDs | Scope and caveats |
| --- | --- | --- |
| **Ars Nouveau** | `ars-nouveau`, `ars-additions`, `ars-controle`, `ars-elemancy`, `ars-elemental`, `ars-n-spells`, `ars-ocultas`, `ars-unification` | Spell crafting, source, familiars, all Ars addons; direct cross-links for Ars Ocultas/Occultism and Ars 'n Spells/Iron's. |
| **Astral Sorcery** | `astral-sorcery` | Constellations, attunement, structures and Astral perks. |
| **Botania Revived** | `botania-revived`, `extra-botany-revived` | Flowers, mana generation, runes, terrestrial and high-tier Botania; Extra Botany grouped here. |
| **Eidolon: Repraised** | `eidolon-repraised` | Rituals, prayer, soulcraft and related progression. |
| **Enchanted: Witchcraft** | `enchanted-witchcraft` | Dedicated witchcraft progression; do not merge with JAWR or Hexerei. |
| **EvilCraft** | `evilcraft` | Vengeance, blood-related crafting and equipment. |
| **Forbidden & Arcanus** | `forbidden-arcanus` | Arcane items, rituals, crafting and world-specific acquisitions. |
| **Goety** | `goety`, `goety-cataclysm` | Necromancy, soul energy, mobs, summons and Cataclysm-specific addon content. |
| **Hex Casting** | `hexcast-neoforged` | Patterns, spell programming, media and artifacts. |
| **Hexerei** | `hexerei` | Witchcraft, cauldron, broom, rituals and unique resources. |
| **Iron's Spells 'n Spellbooks** | `irons-spells` | Scrolls, spellbooks, equipment, progression and school mechanics. |
| **Mahou Tsukai** | `mahou-tsukai` | Mana, magic circles, spells and signature equipment. |
| **Malum** | `malum`, `gaze-malum` | Spirit/arcane progression, rites, enchantments and Gaze addon. |
| **Neo Vitae** | `neovitae` | Blood-magic altar progression and systems; original Blood Magic excluded. |
| **Occultism** | `occultism` | Rituals, magic mining, familiars, storage and endgame Ancient Miner as crosslink. |
| **Pastel** | `pastel` | Dedicated color/resonance magic progression. |
| **Primal Magick** | `primal-magick` | Research, knowledge, primal rituals and crafting. |
| **Psi** | `psi`, `psi-tweaks` | Spell programming, CADs, projectiles, gadgets, and optional power integration. |
| **Roots Classic** | `roots-classic` | Wildwood and nature spells, tools, staff progression. |
| **Scriptor Magicae** | `scriptor-magicae` | Discover world-randomized spell grammar, draft custom spells, scriptionary. |
| **Theurgy** | `theurgy` | Reagent alchemy, divination, replication/transmutation and ritual infrastructure. **Review:** Review against locked endgame Replication mod; they are different systems. |
| **Totemic** | `totemic` | Totem ceremonies, music-driven rituals, nature powers. |
| **Just Another Witchery Remake** | `witchery-remake`, `forbidden-magic` | Witchery base progression plus JAWR Forbidden Magic; link transformation quests to Vampirism/Werewolves. |
| **Embers Re-Ignited** | `embers-re-ignited` | Ember fuel, machines, alchemy and magical industry. |
| **Reliquary Reincarnations** | `reliquary-reincarnations` | Magical crafting, staves, potions and utilities; sufficiently substantial to merit a full chapter. |

### Exploration & Combat — 9 chapters

| Proposed chapter | Primary contents / related mod IDs | Scope and caveats |
| --- | --- | --- |
| **L_Ender's Cataclysm** | `cataclysm` | Dungeons, major bosses and unique drops; crosslink locked endgame resource gates. |
| **Deeper and Darker** | `deeper-darker` | Otherside exploration, challenges, gear and rewards. |
| **Gateways to Eternity** | `gateways-to-eternity` | Summoned combat arenas and repeatable encounters; balance mob drop farming. |
| **Feywild** | `feywild`, `quest-giver` | Feywild structures, encounters, NPCs and related quests; Quest Giver is a framework, not owned by Feywild. |
| **Apotheosis** | `apotheosis`, `apothic-attributes`, `apothic-enchanting`, `apothic-spawners` | Affixes, enchanting, spawners and improved equipment; group closely coupled Apothic modules. |
| **Artifacts** | `artifacts` | Finding, equipping and understanding artifacts. **Review:** Relatively short chapter; reassess if empty checklist. |
| **Enigmatic Legacy+** | `enigmatic-legacy-plus` | Curios, hidden challenges, unique equipment and consequences. **Review:** Could be Magic instead; prefer Exploration & Combat due power/loot focus. |
| **Vampirism** | `vampirism` | Vampire/hunter factions and progression; unified transformation rules crosslink. **Review:** Do not implement race rewards before transformation compat is verified. |
| **Werewolves** | `werewolves` | Werewolf progression, forms, factions, and interactions with Vampirism. **Review:** Approved transformation model supersedes exclusivity assumptions in older docs. |

### Endgame — 4 chapters

| Proposed chapter | Primary contents / related mod IDs | Scope and caveats |
| --- | --- | --- |
| **Draconic Evolution** | `draconic-evolution` | Draconic crafting, power, gear and Chaos Guardian. |
| **ProjectE** | `projecte`, `project-expansion` | EMC and Project Expansion, strictly after Chaos Guardian unlock; no recipe/loot/quest bypass. **Review:** Must remain locked post-Guardian across every acquisition/usage path. |
| **Replication** | `replication` | Late deepslate replica ore and ultimate Occultism miner route; boss-gated renewable resources. **Review:** Do not confuse with Theurgy transmutation. |
| **Ultimate Crafting** | `re-avaritia`, `extended-crafting` | Re:Avaritia and Extended Crafting, culminating recipes and expensive goals. **Review:** Do not allow alternate crafting to bypass Chaos Guardian and other gates. |

## Active mods without a standalone chapter (51)

These do **not** merit a quest chapter because they are support libraries, generic utility/framework components, performance/rendering optimizers or administrator-only maintenance. When player-visible (for example Curios, Tectonic, FTB libraries), explain their user-facing effect in an appropriate Tips & Tricks or specialist quest rather than manufacturing a full chapter. Only these 51 active IDs are excluded from dedicated assignment; **all other active mods appear once** in the chapter tables above.

**library (43)**: `architectury-api`, `balm`, `better-advanced-tooltips`, `bookshelf`, `brandons-core`, `caelus`, `cloth-config`, `cobweb`, `codechicken-lib-1-8`, `common-network`, `cucumber`, `curios-api`, `cyclops-core`, `databank`, `exclusions-lib`, `ftb-library`, `ftb-teams`, `ftb-xmod-compat`, `geckolib`, `hammerlib`, `irons-lib`, `konkrete`, `kotlin-for-forge`, `lionfish-api`, `lithostitched`, `lodestone`, `mezzconfig`, `modonomicon`, `modopedia`, `observerlib`, `patchouli`, `paucal`, `placebo`, `playeranimator`, `prickle`, `rhino`, `searchables`, `smartbrainlib`, `stateobserver`, `tectonic`, `titanium`, `unfocused`, `valhelsia-core`.

**performance (5)**: `embeddium`, `entity-culling`, `ferritecore`, `immediatelyfast`, `modernfix`.

**utilities (3)**: `kubejs`, `simple-backups`, `spark`.

Special cases:
- **Tectonic** changes world terrain but doesn't provide a conventional quest progression: mention in Intro/Tips and exploration descriptions, not a whole chapter.
- **Curios API, Modonomicon, Modopedia, Patchouli, FTB Teams/XMod, KubeJS** are crucial infrastructure but not new questlines.
- **Quest Giver** is a reusable NPC quest framework. It is tracked with the Feywild chapter solely for inventory accounting; quests using it should be authored wherever they fit.
- **Summoning Rituals** is accounted for in Advanced Progression, and its project explicitly adds **no recipes by default**. Promote it to a full Magic chapter only once TMM-specific ritual recipes/loops are defined.
- **Corail Tombstone** has meaningful player-facing death/recovery behavior, but Tips & Tricks is a more useful home than an independent combat chapter.
- **Farming for Blockheads** belongs to Food & Fishing; audit Market seed availability for magic progression bypass.
- **Pufferfish Skills/Attributes** and **Improvable Skills 3** are a shared Skills & Advancement chapter, not one book chapter per framework/dependency. The actual custom skill tree remains a separate implementation task.
- **Original Botania** (inactive) is not a duplicate chapter next to Botania Revived (active).

## Inactive or unpinned roster entries (8): no chapters yet

| Entry | Roster ID | Action |
| --- | --- | --- |
| Bigger Reactors | `bigger-reactors` | Use pinned Rework; original is unavailable for target build |
| Botania | `botania` | Do not activate original Botania; Botania Revived is already chaptered |
| Construction Gadgets | `construction-gadgets` | No chapter until a verified compatible mod is installed |
| Formation Wand | `formation-wand` | No chapter until a verified compatible mod is installed |
| Magical satchel / Surprisingly Substantial Satchel | `magical-satchel` | Do not double count; investigate whether named satchel is already part of Occultism |
| Relics | `relics` | Potential Exploration & Combat chapter only after install/approval |
| Roguelike-style dungeon provider | `roguelike-dungeons` | Potential Exploration & Combat chapter after exact mod identity and installation confirmed |
| Starlight / Stalwart Dungeons candidate | `starlight-structures` | Potential Exploration & Combat chapter after exact mod identity and installation confirmed |

## Items requiring an editorial decision

1. **Reliquary Reincarnations**: included as a full Magic chapter, because it has enough unique equipment, brewing and crafting systems to justify one, despite not being a classic spell tree.
2. **Embers Re-Ignited**: included as Magic, not a separate sixth `Technology` category; it is clearly magical industry.
3. **Feywild**: assigned to Exploration & Combat due to world, NPC and encounter focus, though it has magic crossover.
4. **Enigmatic Legacy+**: assigned to Exploration & Combat because discoveries/equipment make a stronger quest spine than another spell-tree chapter.
5. **Apotheosis**: assigned to Exploration & Combat rather than Magic; close-coupled Apothic Attributes, Enchanting and Spawners stay in its chapter.
6. **Vampirism and Werewolves**: **two** chapters, crosslinked to JAWR/Goety transformations and future unified rules. Do not build quests that assume unreviewed transformation conflicts.
7. **Draconic Evolution** sits in Endgame rather than Magic; **Bigger Reactors Rework** sits in Progression's exceptional-power chapter. This preserves the pack's deliberate limited tech scope.
8. **Extended Crafting** is part of Endgame's Ultimate Crafting chapter with Re:Avaritia, rather than occupying a separate chapter as a standalone recipe framework.
9. **Gateways to Eternity** remains a full combat chapter despite potential drop-farming exploits; quest design must not trivialize boss-loot gating.

## Production notes

- Don't assume approved mods are client-tested; chapter plans are based on B8 pinned/manifest state, not proven in-world quest functionality.
- Keep parent-mod addons within the parent chapter; use subclusters for addons with many quests, and crosslinks for true integration (Ars Ocultas, Ars 'n Spells, Goety Cataclysm).
- Enforce actual progression locks server-side. FTB Quests describes the locked routes; it is not the sole enforcement.
- Never use a quest reward to bypass Chaos Guardian ProjectE lock, Cataclysm boss materials, the Eldritch Ancient Miner, forbidden Ring of the Seven Deadly Sins, or bag-in-bag policy.
- Consider quest breadth: major systems get teachable progression milestones and a few exploration challenges; do not artificially assign fifty crafting checks to simple utility mods.
- Quest data, exact item/registry IDs, chapter localization, folder ordering, FTB GUI layout, team semantics and rewards are **future implementation work**.
