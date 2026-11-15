# Mod comprehension — coverage index

**Purpose:** resumable queue for Work research sessions. See [operating instructions](WORK-CHAT-INSTRUCTIONS.md). Every row corresponds to a `manifest/mods.json` selection record, **not a claim that the mod was successfully installed or tested**.

**Snapshot:** 2026-11-15 updated selection (164 entries; 155 enabled, 9 inactive/planned). Exact runtime pins still require reconciliation. Alex's Caves Continued and CodxLib are planned but not yet installed; Bigger Reactors variants are excluded.

## Highest-value queue (suggested order)

Work one substantial slice at a time; update corresponding coverage status and link. These are **research priorities**, not documented mechanics.

| Rank | Manifest ID | Research reason |
| ---: | --- | --- |
| 1 | `ars-nouveau` | Core spellmaking/source walkthrough and seven focused addon profiles; close exact-pin gaps and client acceptance |
| 2 | `occultism` | Storage, ore crushing, bound helpers, practical first-week utility |
| 3 | `neovitae` | Ritual mechanics, energy/EV, tier gates, blood interactions |
| 4 | `witchery-remake` | JAWR base factions, vampirism/lycanthropy and alt progression |
| 5 | `forbidden-magic` | JAWR Forbidden Magic lich/other spell and faction extensions |
| 6 | `summoning-rituals` | Investigate actual native input/output and customization limits |
| 7 | `botania-revived` | Early mana, floral loops, runic crafting and port-specific changes |
| 8 | `evilcraft` | Blood, rejuvenated flesh, vengeance systems and useful artifacts |
| 9 | `roots-classic` | Early natural magic utility and repeatable sources |
| 10 | `irons-spells` | Scrolls/spellbooks, spell school gates and Ars addon interaction |
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

**Statuses:** `queued` (not researched), `researching` (one or more subsystems/tiers/guidebook or walkthrough paths remain incomplete), `source-complete` (full mechanics **and full tier progression / guidebook / quest coverage** source-backed; not necessarily client-tested), `verified-in-pack` (reproducible client/world test), `blocked` (explicit reason in profile), `deferred` (inactive manifest selection), `stale` (version changed and evidence needs refresh).

**Depth:** `full` = all gameplay systems, outputs, progression and low-hanging setups; `focused` = user-facing utility and key interactions; `interface` = API, dependencies, configuration and effects on integrated mods, not a fictitious gameplay path. If a supposed dependency has real player gameplay, elevate it to `full`.

Replace `—` in the Profile column with `[profile](mods/<manifest-id>.md)` once a real file exists. Never link to a nonexistent placeholder. Keep rows for removed or inactive selections clearly marked, or document reconciliation when the manifest changes.

| Manifest ID | Name | Category | Manifest record | Depth | Research status | Profile |
| --- | --- | --- | --- | --- | --- | --- |
| `alexs-caves-continued` | Alex's Caves Continued | adventure | **planned, not installed** | full | deferred | — |
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
| `codxlib` | CodxLib | dependency | **planned, not installed** | interface | deferred | — |
| `codechicken-lib-1-8` | CodeChicken Lib 1.8.+ | dependency | enabled flag | interface | queued | — |
| `common-network` | Common Network | dependency | enabled flag | interface | queued | — |
| `cucumber` | Cucumber Library | dependency | enabled flag | interface | queued | — |
| `curios-api` | Curios API | dependency | enabled flag | interface | queued | — |
| `cyclops-core` | Cyclops Core | dependency | enabled flag | interface | queued | — |
| `databank` | Databank | dependency | enabled flag | interface | queued | — |
| `exclusions-lib` | Exclusions Lib | dependency | enabled flag | interface | queued | — |
| `forbidden-magic` | JAWR: Forbidden Magic | dependency | enabled flag | full | researching | [profile](mods/forbidden-magic.md) |
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
| `ars-nouveau` | Ars Nouveau | magic | enabled flag | full | researching | [profile](mods/ars-nouveau.md) |
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
| `neovitae` | Neo Vitae | magic | enabled flag | full | researching | [profile](mods/neovitae.md) |
| `occultism` | Occultism | magic | enabled flag | full | researching | [profile](mods/occultism.md) |
| `pastel` | Pastel | magic | enabled flag | full | queued | — |
| `primal-magick` | Primal Magick | magic | enabled flag | full | queued | — |
| `psi` | Psi | magic | enabled flag | full | queued | — |
| `roots-classic` | Roots Classic | magic | enabled flag | full | queued | — |
| `scriptor-magicae` | Scriptor Magicae | magic | enabled flag | full | queued | — |
| `theurgy` | Theurgy | magic | enabled flag | full | queued | — |
| `totemic` | Totemic | magic | enabled flag | full | queued | — |
| `witchery-remake` | Just Another Witchery Remake | magic | enabled flag | full | researching | [profile](mods/witchery-remake.md) |
| `ars-additions` | Ars Additions | magic_addon | enabled flag | full | researching | [profile](mods/ars-additions.md) |
| `ars-controle` | Ars Controle | magic_addon | enabled flag | full | researching | [profile](mods/ars-controle.md) |
| `ars-elemancy` | Ars Elemancy | magic_addon | enabled flag | full | researching | [profile](mods/ars-elemancy.md) |
| `ars-elemental` | Ars Elemental | magic_addon | enabled flag | full | researching | [profile](mods/ars-elemental.md) |
| `ars-n-spells` | Ars 'n Spells | magic_addon | enabled flag | full | researching | [profile](mods/ars-n-spells.md) |
| `ars-ocultas` | Ars Ocultas | magic_addon | enabled flag | full | researching | [profile](mods/ars-ocultas.md) |
| `ars-unification` | Ars Unification | magic_addon | enabled flag | full | researching | [profile](mods/ars-unification.md) |
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
| `vampirism` | Vampirism — Become a Vampire! | supernatural | enabled flag | full | deferred | — |
| `werewolves` | Werewolves — Become a Beast! | supernatural | enabled flag | full | deferred | — |
| `draconic-evolution` | Draconic Evolution | technology | enabled flag | full | queued | — |
| `quest-shop` | Quest Shop | utility | enabled flag | full | queued | — |

**2026-11-15 decision:** Bigger Reactors original and Rework removed, no reactor profile queued. Alex's Caves Continued and CodxLib are planned through [staging PR #56](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/56) without its now-obsolete uranium conversion. Preserve ongoing magic-mod research additions.

## Cross-mod interaction reports

No reports yet. Create one Markdown file per *specific, actionable interaction* under [interactions/](interactions/README.md) after confirming both sides of an I/O contract. Maintain a short list here as each is added, with status `candidate` / `source-supported` / `verified-in-pack` / `blocked` / `rejected` / `stale`.

| Interaction report | Participants | Status | Key finding |
| --- | --- | --- | --- |
| [Neo Vitae vampire blood sources](interactions/neovitae--witchery-remake--vampire-blood.md) | Neo Vitae + JAWR | source-supported | JAWR ships an Anima-powered Bloodstone Pendant ritual and vampire Orb/altar compatibility; exact blood/Anima transfer is untested |

## Quest/compatibility design handoffs

None yet. The [handoff directory](handoffs/README.md) holds source-backed design recommendations, not implemented quest content or approved recipe patches.

## Methodology improvements collected

Original proposals and **2026-11-15 adoption decisions** are recorded in [METHODOLOGY-IMPROVEMENTS.md](METHODOLOGY-IMPROVEMENTS.md). The [Work instructions](WORK-CHAT-INSTRUCTIONS.md), [profile template](templates/MOD-PROFILE.md) and [quest walkthrough standard](../QUEST-WALKTHROUGH-STANDARD.md) are now normative. Future researchers should audit the full progression-to-quest path and guide access, not merely inventory mechanics.

## Session notes / next pickup

- 2026-11-15 — Ars profile updated to walkthrough standard and seven addon profiles added. Important discovery: the installed Ars Unification + Occultism combination exposes a Crusher Spirit Crush route omitted by the pilot. Core glyph learning and Scribes Table operation are now source-mapped; the remaining Ars work is exact-version gap resolution, full creature/ritual/adapter inventory and client acceptance.
- 2026-11-15 — Adopted the eight original methodology refinements plus player-value summary and subsystem coverage; the [walkthrough standard](../QUEST-WALKTHROUGH-STANDARD.md) requires early guidebook access and full rewarded how-to progression. Existing pilot quests must be gap-audited against it.
- 2026-11-15 — Added choice-boundary guidance: distinguish mandatory gates from player-selected branches and audit quest prose separately from task predicates and progression coverage.
- 2026-11-15 — Resolved source identity for two previously uncertain addon pins by tracing version metadata history: Ars Additions 21.3.0 and Ars Elemental 0.7.10.4 now have immutable source commits. Their declared Ars Nouveau dependencies are older than TMM's base pin, so compatibility remains a client/source review item.
- 2026-11-15 — Occultism 1.227.0 profile started from matching release source. The Dictionary progression graph and broad worker branches are mapped; the current chapter is only a checkmark stub. Matching source defines ordinary miner-tag recipes for Draconium Ore and Uranium Ore, plus an Ancient Eldritch Miner raw-uranium recipe. Common tag/runtime availability is not yet confirmed; see docs/COMPATIBILITY.md and the profile.

- 2026-11-15 — Neo Vitae 1.1.53 profile added from version-aligned source and generated Scriptura Vitae; mapped tiers 0–5, parallel branches, Mine Key drops, EV/Spiritus pathways, and the source-supported Occultism storage bridge. API/version and client behavior remain untested; see the profile and compatibility note.
- 2026-11-15 — JAWR 0.5.39 profile started from the exact hash-pinned JAR. Mapped the 223-entry native Guidebook and nine-stage supernatural journals; identified the reported cauldron Altar Power bypass in all ten cauldron-crafting recipes and a cross-version familiar recipient report. See the profile and `docs/COMPATIBILITY.md`.
- 2026-11-15 — Forbidden Magic 0.5.37 profile started from the exact hash-pinned JAR. Mapped its gated Soul Magic guide additions, four code-granted necromancy ranks, soul rites and phylactery creation/binding path. The user's separate Archlich target requires Phylactery before Arca; Arca is not present in this addon. See the profile.
- 2026-11-15 — Standalone Vampirism and Werewolves are marked deferred for this knowledge queue because the owner-approved target retires those mods after Hunter/Monk migration; this is a research-status reconciliation only, not a runtime change.
- **Next action:** continue with `summoning-rituals`, then `botania-revived`, `evilcraft`, `roots-classic` and `irons-spells`. Occultism, Neo Vitae, JAWR and Forbidden Magic remain `researching` for their listed exact-recipe/client/runtime gaps. Quest implementation is a later pass.
