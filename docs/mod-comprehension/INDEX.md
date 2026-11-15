# Mod comprehension — coverage index

**Purpose:** resumable queue for Work research sessions. See [operating instructions](WORK-CHAT-INSTRUCTIONS.md). Every row corresponds to a `manifest/mods.json` selection record, **not a claim that the mod was successfully installed or tested**.

**Snapshot:** 2026-11-15, initial roster from `test/full-integration` (155 selection rows; 147 marked `enabled_in_pack` by manifest; 8 inactive/deferred). Work must reconcile with live packwiz pins and integrated runtime before claiming version or activation. The repository may evolve after this snapshot.

## Highest-value queue (suggested order)

Work one substantial slice at a time; update corresponding coverage status and link. These are **research priorities**, not documented mechanics.

| Rank | Manifest ID | Research reason |
| ---: | --- | --- |
| 1 | `ars-nouveau` | Core spellmaking, source, automation; map Ars addons next |
| 2 | `occultism` | Storage, ore crushing, bound helpers, practical first-week utility |
| 3 | `neovitae` | Ritual mechanics, energy/EV, tier gates, blood interactions |
| 4 | `witchery-remake` | JAWR base factions, vampirism/lycanthropy and alt progression |
| 5 | `forbidden-magic` | JAWR Forbidden Magic lich/other spell and faction extensions |
| 6 | `vampirism` | Factions, blood pool, vampire/hunter progression and native detection |
| 7 | `werewolves` | Forms, feeding, skills and combinations with vampirism/JAWR |
| 8 | `summoning-rituals` | Investigate actual native input/output and customization limits |
| 9 | `botania-revived` | Early mana, floral loops, runic crafting and port-specific changes |
| 10 | `evilcraft` | Blood, rejuvenated flesh, vengeance systems and useful artifacts |
| 11 | `roots-classic` | Early natural magic utility and repeatable sources |
| 12 | `irons-spells` | Scrolls/spellbooks, spell school gates and Ars addon interaction |
| 13 | `scriptor-magicae` | Casting lectern, automated smelting and other programmable utility |
| 14 | `goety` | Soul energy, ritual summon pipeline and mob/owner behavior |
| 15 | `goety-cataclysm` | Precisely what addon introduces vs both parent mods |
| 16 | `ars-ocultas` | Occultism bridge: recipes, capabilities, exact shared hooks |
| 17 | `ars-n-spells` | Iron's bridge: real functionality beyond descriptions |
| 18 | `astral-sorcery` | Altars/attunement, structure dimensions, version-specific changes |
| 19 | `mahou-tsukai` | Material powder processing, mana, circles, cheap utility |
| 20 | `draconic-evolution` | Energy interfaces, Chaos Guardian and locked apex |
| 21 | `projecte` | EMC and current recipe access, especially boss-gate hazards |
| 22 | `project-expansion` | Power flowers, resource duplication boundaries |
| 23 | `quest-shop` | Item/coin/team account semantics and rewards/balance |
| 24 | `ftb-quests` | Existing task detectors and reliable quest authoring surfaces |

Once two source-supported profiles expose complementary I/O, test a **cross-mod** hypothesis rather than indefinitely deferring pass 2. High-priority *unverified questions*: Ars Ocultas × Occultism; Ars 'n Spells × Iron's; JAWR × Vampirism × Werewolves (and the pack's separate transformation bridge); EvilCraft flesh and modded diets; Summoning Rituals output channels; magical processing substitutions; late-game EMC gate leakage. Do not presume any work merely because it is in this list.

## Complete roster and status tracker

**Statuses:** `queued` (not researched), `researching` (partial), `source-complete` (major mechanics evidenced, client not necessarily tested), `verified-in-pack` (reproducible client/world test), `blocked` (explicit reason in profile), `deferred` (inactive manifest selection), `stale` (version changed and evidence needs refresh).

**Depth:** `full` = all gameplay systems, outputs, progression and low-hanging setups; `focused` = user-facing utility and key interactions; `interface` = API, dependencies, configuration and effects on integrated mods, not a fictitious gameplay path. If a supposed dependency has real player gameplay, elevate it to `full`.

Replace `—` in the Profile column with `[profile](mods/<manifest-id>.md)` once a real file exists. Never link to a nonexistent placeholder. Keep rows for removed or inactive selections clearly marked, or document reconciliation when the manifest changes.

| Manifest ID | Name | Category | Manifest record | Depth | Research status | Profile |
| --- | --- | --- | --- | --- | --- | --- |
| `cataclysm` | L_Ender's Cataclysm | adventure | enabled flag | full | queued | — |
| `deeper-darker` | Deeper and Darker | adventure | enabled flag | full | queued | — |
| `gateways-to-eternity` | Gateways to Eternity | adventure | enabled flag | full | queued | — |
| `roguelike-dungeons` | Roguelike-style dungeon provider | adventure | inactive | full | deferred | — |
| `starlight-structures` | Starlight / Stalwart Dungeons candidate | adventure | inactive | full | deferred | — |
| `apothic-attributes` | Apothic Attributes | dependency | enabled flag | interface | queued | — |
| `apothic-enchanting` | Apothic-Enchanting | dependency | enabled flag | full | queued | — |
| `apothic-spawners` | Apothic-Spawners | dependency | enabled flag | full | queued | — |
| `architectury-api` | Architectury API | dependency | enabled flag | interface | queued | — |
| `balm` | Balm | dependency | enabled flag | interface | queued | — |
| `better-advanced-tooltips` | Better Advanced Tooltips | dependency | enabled flag | interface | queued | — |
| `bookshelf` | Bookshelf | dependency | enabled flag | interface | queued | — |
| `brandons-core` | Brandon's Core | dependency | enabled flag | interface | queued | — |
| `caelus` | Caelus API | dependency | enabled flag | interface | queued | — |
| `cloth-config` | Cloth Config API (Fabric/Forge/NeoForge) | dependency | enabled flag | interface | queued | — |
| `cobweb` | Cobweb | dependency | enabled flag | interface | queued | — |
| `codechicken-lib-1-8` | CodeChicken Lib 1.8.+ | dependency | enabled flag | interface | queued | — |
| `common-network` | Common Network | dependency | enabled flag | interface | queued | — |
| `cucumber` | Cucumber Library | dependency | enabled flag | interface | queued | — |
| `curios-api` | Curios API | dependency | enabled flag | interface | queued | — |
| `cyclops-core` | Cyclops Core | dependency | enabled flag | interface | queued | — |
| `databank` | Databank | dependency | enabled flag | interface | queued | — |
| `exclusions-lib` | Exclusions Lib | dependency | enabled flag | interface | queued | — |
| `forbidden-magic` | JAWR: Forbidden Magic | dependency | enabled flag | full | queued | — |
| `ftb-library` | FTB Library (NeoForge) | dependency | enabled flag | interface | queued | — |
| `ftb-teams` | FTB Teams (NeoForge) | dependency | enabled flag | interface | queued | — |
| `ftb-xmod-compat` | FTB XMod Compat | dependency | enabled flag | interface | queued | — |
| `geckolib` | Geckolib | dependency | enabled flag | interface | queued | — |
| `hammerlib` | HammerLib | dependency | enabled flag | interface | queued | — |
| `irons-lib` | Iron's Lib | dependency | enabled flag | interface | queued | — |
| `konkrete` | Konkrete | dependency | enabled flag | interface | queued | — |
| `kotlin-for-forge` | Kotlin for Forge | dependency | enabled flag | interface | queued | — |
| `lionfish-api` | Lionfish-API | dependency | enabled flag | interface | queued | — |
| `lithostitched` | Lithostitched | dependency | enabled flag | interface | queued | — |
| `lodestone` | Lodestone | dependency | enabled flag | interface | queued | — |
| `mezzconfig` | MezzConfig | dependency | enabled flag | interface | queued | — |
| `modonomicon` | Modonomicon | dependency | enabled flag | interface | queued | — |
| `modopedia` | Modopedia | dependency | enabled flag | interface | queued | — |
| `observerlib` | ObserverLib | dependency | enabled flag | interface | queued | — |
| `octolib` | ShatterLib \| OctoLib | dependency | enabled flag | interface | queued | — |
| `patchouli` | Patchouli | dependency | enabled flag | interface | queued | — |
| `paucal` | PAUCAL | dependency | enabled flag | interface | queued | — |
| `placebo` | Placebo | dependency | enabled flag | interface | queued | — |
| `playeranimator` | playerAnimator | dependency | enabled flag | interface | queued | — |
| `prickle` | Prickle | dependency | enabled flag | interface | queued | — |
| `puffish-attributes` | Pufferfish's Attributes | dependency | enabled flag | interface | queued | — |
| `puzzles-lib` | Puzzles Lib | dependency | enabled flag | interface | queued | — |
| `quest-giver` | Quest Giver | dependency | enabled flag | interface | queued | — |
| `rhino` | Rhino | dependency | enabled flag | interface | queued | — |
| `searchables` | Searchables | dependency | enabled flag | interface | queued | — |
| `smartbrainlib` | SmartBrainLib | dependency | enabled flag | interface | queued | — |
| `stateobserver` | StateObserver | dependency | enabled flag | interface | queued | — |
| `tectonic` | Tectonic | dependency | enabled flag | full | queued | — |
| `titanium` | Titanium | dependency | enabled flag | interface | queued | — |
| `unfocused` | Unfocused | dependency | enabled flag | interface | queued | — |
| `valhelsia-core` | Valhelsia Core | dependency | enabled flag | interface | queued | — |
| `project-expansion` | Project Expansion | emc_progression | enabled flag | full | queued | — |
| `aquaculture` | Aquaculture 2 | food | enabled flag | full | queued | — |
| `cooking-for-blockheads` | Cooking for Blockheads | food | enabled flag | full | queued | — |
| `farmers-delight` | Farmer's Delight | food | enabled flag | full | queued | — |
| `farming-for-blockheads` | Farming for Blockheads | food | enabled flag | full | queued | — |
| `pams-harvestcraft-2-crops` | Pam's HarvestCraft 2 — Crops | food | enabled flag | full | queued | — |
| `pams-harvestcraft-2-food-core` | Pam's HarvestCraft 2 — Food Core | food | enabled flag | full | queued | — |
| `pams-harvestcraft-2-food-extended` | Pam's HarvestCraft 2 — Food Extended | food | enabled flag | full | queued | — |
| `pams-harvestcraft-2-trees` | Pam's HarvestCraft 2 — Trees | food | enabled flag | full | queued | — |
| `ftb-chunks` | FTB Chunks (NeoForge) | infrastructure | enabled flag | focused | queued | — |
| `ftb-quests` | FTB Quests (NeoForge) | infrastructure | enabled flag | full | queued | — |
| `kubejs` | KubeJS | infrastructure | enabled flag | full | queued | — |
| `puffish-skills` | Pufferfish's Skills | infrastructure | enabled flag | focused | queued | — |
| `replication` | Replication | late_ore | enabled flag | full | queued | — |
| `ars-nouveau` | Ars Nouveau | magic | enabled flag | full | source-complete | [profile](mods/ars-nouveau.md) |
| `astral-sorcery` | Astral Sorcery | magic | enabled flag | full | queued | — |
| `botania` | Botania | magic | inactive | full | deferred | — |
| `botania-revived` | Botania Revived | magic | enabled flag | full | queued | — |
| `eidolon-repraised` | Eidolon: Repraised | magic | enabled flag | full | queued | — |
| `enchanted-witchcraft` | Enchanted: Witchcraft | magic | enabled flag | full | queued | — |
| `evilcraft` | EvilCraft | magic | enabled flag | full | queued | — |
| `forbidden-arcanus` | Forbidden & Arcanus | magic | enabled flag | full | queued | — |
| `goety` | Goety — The Dark Arts | magic | enabled flag | full | queued | — |
| `hexcast-neoforged` | HexCast NEOFORGED | magic | enabled flag | full | queued | — |
| `hexerei` | Hexerei | magic | enabled flag | full | queued | — |
| `irons-spells` | Iron's Spells 'n Spellbooks | magic | enabled flag | full | queued | — |
| `mahou-tsukai` | Mahou Tsukai | magic | enabled flag | full | queued | — |
| `malum` | Malum | magic | enabled flag | full | queued | — |
| `neovitae` | Neo Vitae | magic | enabled flag | full | queued | — |
| `occultism` | Occultism | magic | enabled flag | full | queued | — |
| `pastel` | Pastel | magic | enabled flag | full | queued | — |
| `primal-magick` | Primal Magick | magic | enabled flag | full | queued | — |
| `psi` | Psi | magic | enabled flag | full | queued | — |
| `roots-classic` | Roots Classic | magic | enabled flag | full | queued | — |
| `scriptor-magicae` | Scriptor Magicae | magic | enabled flag | full | queued | — |
| `theurgy` | Theurgy | magic | enabled flag | full | queued | — |
| `totemic` | Totemic | magic | enabled flag | full | queued | — |
| `witchery-remake` | Just Another Witchery Remake | magic | enabled flag | full | queued | — |
| `ars-additions` | Ars Additions | magic_addon | enabled flag | full | queued | — |
| `ars-controle` | Ars Controle | magic_addon | enabled flag | full | queued | — |
| `ars-elemancy` | Ars Elemancy | magic_addon | enabled flag | full | queued | — |
| `ars-elemental` | Ars Elemental | magic_addon | enabled flag | full | queued | — |
| `ars-n-spells` | Ars 'n Spells | magic_addon | enabled flag | full | queued | — |
| `ars-ocultas` | Ars Ocultas | magic_addon | enabled flag | full | queued | — |
| `ars-unification` | Ars Unification | magic_addon | enabled flag | full | queued | — |
| `extra-botany-revived` | Extra Botany Revived | magic_addon | enabled flag | full | queued | — |
| `gaze-malum` | Gaze (Malum addon) | magic_addon | enabled flag | full | queued | — |
| `goety-cataclysm` | Goety Cataclysm | magic_addon | enabled flag | full | queued | — |
| `psi-tweaks` | Psi: Tweaks and Additions | magic_addon | enabled flag | full | queued | — |
| `feywild` | Feywild | magic_adventure | enabled flag | full | queued | — |
| `embers-re-ignited` | Embers Re-Ignited | magic_industry | enabled flag | full | queued | — |
| `projecte` | ProjectE | magic_progression | enabled flag | full | queued | — |
| `reliquary-reincarnations` | Reliquary Reincarnations | magic_utility | enabled flag | full | queued | — |
| `embeddium` | Embeddium | performance | enabled flag | interface | queued | — |
| `entity-culling` | Entity Culling | performance | enabled flag | interface | queued | — |
| `ferritecore` | FerriteCore | performance | enabled flag | interface | queued | — |
| `immediatelyfast` | ImmediatelyFast | performance | enabled flag | interface | queued | — |
| `modernfix` | ModernFix | performance | enabled flag | interface | queued | — |
| `apotheosis` | Apotheosis | power | enabled flag | full | queued | — |
| `artifacts` | Artifacts | power | enabled flag | full | queued | — |
| `enigmatic-legacy-plus` | Enigmatic Legacy+ | power | enabled flag | full | queued | — |
| `relics` | Relics | power | enabled flag | full | queued | — |
| `reliquified-artifacts` | Reliquified Artifacts | power | enabled flag | full | queued | — |
| `akashic-tome` | Akashic Tome | qol | enabled flag | focused | queued | — |
| `appleskin` | AppleSkin | qol | enabled flag | focused | queued | — |
| `better-advancements` | Better Advancements | qol | enabled flag | focused | queued | — |
| `comforts` | Comforts | qol | inactive | focused | deferred | — |
| `construction-gadgets` | Construction Gadgets | qol | inactive | focused | deferred | — |
| `controlling` | Controlling | qol | enabled flag | focused | queued | — |
| `corail-tombstone` | Corail Tombstone | qol | enabled flag | focused | queued | — |
| `crafting-tweaks` | Crafting Tweaks | qol | enabled flag | focused | queued | — |
| `enchantment-descriptions` | Enchantment Descriptions | qol | enabled flag | focused | queued | — |
| `formation-wand` | Formation Wand | qol | inactive | focused | deferred | — |
| `harvest-with-ease` | Harvest with Ease | qol | enabled flag | focused | queued | — |
| `inventory-tweaks-refoxed` | Inventory Tweaks — ReFoxed | qol | enabled flag | focused | queued | — |
| `jade` | Jade | qol | enabled flag | focused | queued | — |
| `jei` | Just Enough Items | qol | enabled flag | focused | queued | — |
| `just-zoom` | Just Zoom | qol | enabled flag | focused | queued | — |
| `keybind-bundles` | KeyBind Bundles | qol | enabled flag | focused | queued | — |
| `leaves-be-gone` | Leaves Be Gone | qol | enabled flag | focused | queued | — |
| `lootr` | Lootr | qol | enabled flag | focused | queued | — |
| `more-overlays-updated` | More Overlays Updated | qol | enabled flag | focused | queued | — |
| `mouse-tweaks` | Mouse Tweaks | qol | enabled flag | focused | queued | — |
| `natures-compass` | Nature's Compass | qol | enabled flag | focused | queued | — |
| `oracle-index` | Oracle Index | qol | enabled flag | focused | queued | — |
| `polymorph` | Polymorph | qol | enabled flag | focused | queued | — |
| `trashslot` | TrashSlot | qol | enabled flag | focused | queued | — |
| `treechop` | HT's TreeChop | qol | enabled flag | focused | queued | — |
| `summoning-rituals` | Summoning Rituals | ritual_framework | enabled flag | full | queued | — |
| `simple-backups` | Simple Backups | server_utility | enabled flag | interface | queued | — |
| `spark` | spark | server_utility | enabled flag | interface | queued | — |
| `improvable-skills` | Improvable Skills 3 | skills | enabled flag | full | queued | — |
| `magical-satchel` | Magical satchel / Surprisingly Substantial Satchel | storage | inactive | full | deferred | — |
| `vampirism` | Vampirism — Become a Vampire! | supernatural | enabled flag | full | queued | — |
| `werewolves` | Werewolves — Become a Beast! | supernatural | enabled flag | full | queued | — |
| `bigger-reactors` | Bigger Reactors | technology | inactive | full | deferred | — |
| `bigger-reactors-rework` | Bigger Reactors Rework | technology | enabled flag | full | queued | — |
| `draconic-evolution` | Draconic Evolution | technology | enabled flag | full | queued | — |
| `quest-shop` | Quest Shop | utility | enabled flag | full | queued | — |

## Cross-mod interaction reports

No reports yet. Create one Markdown file per *specific, actionable interaction* under [interactions/](interactions/README.md) after confirming both sides of an I/O contract. Maintain a short list here as each is added, with status `candidate` / `source-supported` / `verified-in-pack` / `blocked` / `rejected` / `stale`.

| Interaction report | Participants | Status | Key finding |
| --- | --- | --- | --- |
| — | — | — | None yet; priority questions appear above |

## Quest/compatibility design handoffs

None yet. The [handoff directory](handoffs/README.md) holds source-backed design recommendations, not implemented quest content or approved recipe patches.

## Methodology improvements collected

Proposals from research runs are logged in [METHODOLOGY-IMPROVEMENTS.md](METHODOLOGY-IMPROVEMENTS.md); they do not change the operating contract.

## Session notes / next pickup

- 2026-11-15 — Research framework initialized; Ars Nouveau now has a source-backed profile. The exact pin is documented; no in-pack gameplay test has been performed.
- 2026-11-15 — Added [methodology improvement proposals](METHODOLOGY-IMPROVEMENTS.md) based on source/version gaps, typed I/O contracts, falsifiable tests, renewable-loop economics, addon boundaries and quest-task observability. Suggestions are not policy changes.

- **Next action:** continue with `occultism` (rank 2), reconcile its exact pin and inspect the source before profiling storage, ore processing and entity labor. Check for open research branches first.
