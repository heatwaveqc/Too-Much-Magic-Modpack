# Mod comprehension — coverage index

**Purpose:** resumable queue for Work research sessions. See [operating instructions](WORK-CHAT-INSTRUCTIONS.md). Every row corresponds to a `manifest/mods.json` selection record, **not a claim that the mod was successfully installed or tested**.

**Snapshot:** 2026-11-15 updated selection (165 entries; 156 enabled, 9 inactive/planned). Exact runtime pins still require reconciliation. Alex's Caves Continued and CodxLib are planned but not yet installed; Bigger Reactors variants are excluded.

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
| 8 | `roots-classic` | Early natural magic utility and repeatable sources |
| 9 | `irons-spells` | Scrolls/spellbooks, spell school gates and Ars addon interaction |
| 10 | `scriptor-magicae` | Casting lectern, automated smelting and other programmable utility |
| 11 | `goety` | Soul energy, ritual summon pipeline and mob/owner behavior |
| 12 | `goety-cataclysm` | Precisely what addon introduces vs both parent mods |
| 13 | `ars-ocultas` | Occultism bridge: recipes, capabilities, exact shared hooks |
| 14 | `ars-n-spells` | Iron's bridge: real functionality beyond descriptions |
| 15 | `astral-sorcery` | Altars/attunement, structure dimensions, version-specific changes |
| 16 | `mahou-tsukai` | Material powder processing, mana, circles, cheap utility |
| 17 | `draconic-evolution` | Energy interfaces, Chaos Guardian and locked apex |
| 18 | `projecte` | EMC and current recipe access, especially boss-gate hazards |
| 19 | `project-expansion` | Power flowers, resource duplication boundaries |
| 20 | `quest-shop` | Item/coin/team account semantics and rewards/balance |
| 21 | `ftb-quests` | Existing task detectors and reliable quest authoring surfaces |

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
| `project-expansion` | Project Expansion | emc_progression | enabled flag | full | researching | [profile](mods/project-expansion.md) |
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
| `astral-sorcery` | Astral Sorcery | magic | enabled flag | full | researching | [profile](mods/astral-sorcery.md) |
| `botania` | Botania | magic | inactive | full | deferred | — |
| `botania-revived` | Botania Revived | magic | enabled flag | full | researching | [profile](mods/botania-revived.md) |
| `eidolon-repraised` | Eidolon: Repraised | magic | enabled flag | full | researching | [profile](mods/eidolon-repraised.md) |
| `enchanted-witchcraft` | Enchanted: Witchcraft | magic | enabled flag | full | researching | [profile](mods/enchanted-witchcraft.md) |
| `evilcraft` | EvilCraft | magic | enabled flag | full | researching | [profile](mods/evilcraft.md) |
| `forbidden-arcanus` | Forbidden & Arcanus | magic | enabled flag | full | researching | [profile](mods/forbidden-arcanus.md) |
| `goety` | Goety — The Dark Arts | magic | enabled flag | full | researching | [profile](mods/goety.md) |
| `hexcast-neoforged` | HexCast NEOFORGED | magic | enabled flag | full | researching | [profile](mods/hexcast-neoforged.md) |
| `hexerei` | Hexerei | magic | enabled flag | full | researching | [profile](mods/hexerei.md) |
| `irons-spells` | Iron's Spells 'n Spellbooks | magic | enabled flag | full | researching | [profile](mods/irons-spells.md) |
| `mahou-tsukai` | Mahou Tsukai | magic | enabled flag | full | researching | [profile](mods/mahou-tsukai.md) |
| `malum` | Malum | magic | enabled flag | full | researching | [profile](mods/malum.md) |
| `neovitae` | Neo Vitae | magic | enabled flag | full | researching | [profile](mods/neovitae.md) |
| `occultism` | Occultism | magic | enabled flag | full | researching | [profile](mods/occultism.md) |
| `pastel` | Pastel | magic | enabled flag | full | researching | [profile](mods/pastel.md) |
| `primal-magick` | Primal Magick | magic | enabled flag | full | researching | [profile](mods/primal-magick.md) |
| `psi` | Psi | magic | enabled flag | full | researching | [profile](mods/psi.md) |
| `roots-classic` | Roots Classic | magic | enabled flag | full | researching | [profile](mods/roots-classic.md) |
| `scriptor-magicae` | Scriptor Magicae | magic | enabled flag | full | source-complete | [profile](mods/scriptor-magicae.md) |
| `theurgy` | Theurgy | magic | enabled flag | full | researching | [profile](mods/theurgy.md) |
| `totemic` | Totemic | magic | enabled flag | full | researching | [profile](mods/totemic.md) |
| `witchery-remake` | Just Another Witchery Remake | magic | enabled flag | full | researching | [profile](mods/witchery-remake.md) |
| `ars-additions` | Ars Additions | magic_addon | enabled flag | full | researching | [profile](mods/ars-additions.md) |
| `ars-controle` | Ars Controle | magic_addon | enabled flag | full | researching | [profile](mods/ars-controle.md) |
| `ars-elemancy` | Ars Elemancy | magic_addon | enabled flag | full | researching | [profile](mods/ars-elemancy.md) |
| `ars-elemental` | Ars Elemental | magic_addon | enabled flag | full | researching | [profile](mods/ars-elemental.md) |
| `ars-n-spells` | Ars 'n Spells | magic_addon | enabled flag | full | researching | [profile](mods/ars-n-spells.md) |
| `ars-ocultas` | Ars Ocultas | magic_addon | enabled flag | full | blocked | [profile](mods/ars-ocultas.md) |
| `ars-unification` | Ars Unification | magic_addon | enabled flag | full | researching | [profile](mods/ars-unification.md) |
| `extra-botany-revived` | Extra Botany Revived | magic_addon | enabled flag | full | researching | [profile](mods/extra-botany-revived.md) |
| `gaze-malum` | Gaze (Malum addon) | magic_addon | enabled flag | full | researching | [profile](mods/gaze-malum.md) |
| `goety-cataclysm` | Goety Cataclysm | magic_addon | enabled flag | full | researching | [profile](mods/goety-cataclysm.md) |
| `psi-tweaks` | Psi: Tweaks and Additions | magic_addon | enabled flag | full | researching | [profile](mods/psi-tweaks.md) |
| `feywild` | Feywild | magic_adventure | enabled flag | full | researching | [profile](mods/feywild.md) |
| `embers-re-ignited` | Embers Re-Ignited | magic_industry | enabled flag | full | researching | [profile](mods/embers-re-ignited.md) |
| `projecte` | ProjectE | magic_progression | enabled flag | full | researching | [profile](mods/projecte.md) |
| `reliquary-reincarnations` | Reliquary Reincarnations | magic_utility | enabled flag | full | researching | [profile](mods/reliquary-reincarnations.md) |
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
| `summoning-rituals` | Summoning Rituals | ritual_framework | enabled flag | full | researching | [profile](mods/summoning-rituals.md) |
| `simple-backups` | Simple Backups | server_utility | enabled flag | interface | queued | — |
| `spark` | spark | server_utility | enabled flag | interface | queued | — |
| `improvable-skills` | Improvable Skills 3 | skills | enabled flag | full | queued | — |
| `magical-satchel` | Magical satchel / Surprisingly Substantial Satchel | storage | inactive | full | deferred | — |
| `vampirism` | Vampirism — Become a Vampire! | supernatural | enabled flag | full | deferred | — |
| `werewolves` | Werewolves — Become a Beast! | supernatural | enabled flag | full | deferred | — |
| `cc-tweaked` | CC: Tweaked | technology | enabled flag | full | researching | [source-backed pilot](../CC-TWEAKED-ELDRITCH-GATE.md) |
| `draconic-evolution` | Draconic Evolution | technology | enabled flag | full | researching | [profile](mods/draconic-evolution.md) |
| `quest-shop` | Quest Shop | utility | enabled flag | full | queued | — |

**2026-11-15 B14:** CC: Tweaked 1.121.0 beta selected as programmable Draconic power control, with native Occultism Eldritch item gate and Spirit Fire Iesnium recipe. Runtime/JEI still pending. **Prior decision:** Bigger Reactors original and Rework removed, no reactor profile queued. Alex's Caves Continued and CodxLib are planned through [staging PR #56](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/56) without its now-obsolete uranium conversion. Preserve ongoing magic-mod research additions.

## Cross-mod interaction reports

No reports yet. Create one Markdown file per *specific, actionable interaction* under [interactions/](interactions/README.md) after confirming both sides of an I/O contract. Maintain a short list here as each is added, with status `candidate` / `source-supported` / `verified-in-pack` / `blocked` / `rejected` / `stale`.

| Interaction report | Participants | Status | Key finding |
| --- | --- | --- | --- |
| [Neo Vitae vampire blood sources](interactions/neovitae--witchery-remake--vampire-blood.md) | Neo Vitae + JAWR | source-supported | JAWR ships an Anima-powered Bloodstone Pendant ritual and vampire Orb/altar compatibility; exact blood/Anima transfer is untested |
| [EvilCraft Rejuvenated Flesh](interactions/evilcraft--dragon-survival--rejuvenated-flesh.md) | EvilCraft + Dragon Survival | source-supported | Dragon species allowlist and custom Blood-food handler are both source-confirmed; the paired item-use behavior is untested |

## Quest/compatibility design handoffs

None yet. The [handoff directory](handoffs/README.md) holds source-backed design recommendations, not implemented quest content or approved recipe patches.

## Methodology improvements collected

Original proposals and **2026-11-15 adoption decisions** are recorded in [METHODOLOGY-IMPROVEMENTS.md](METHODOLOGY-IMPROVEMENTS.md). The [Work instructions](WORK-CHAT-INSTRUCTIONS.md), [profile template](templates/MOD-PROFILE.md) and [quest walkthrough standard](../QUEST-WALKTHROUGH-STANDARD.md) are now normative. Future researchers should audit the full progression-to-quest path, actual guide access, effective config enforcement, and runtime-injected acquisition routes, not merely inventory mechanics.

## Session notes / next pickup

- 2026-11-15 — Ars profile updated to walkthrough standard and seven addon profiles added. Important discovery: the installed Ars Unification + Occultism combination exposes a Crusher Spirit Crush route omitted by the pilot. Core glyph learning and Scribes Table operation are now source-mapped; the remaining Ars work is exact-version gap resolution, full creature/ritual/adapter inventory and client acceptance.
- 2026-11-15 — Adopted the eight original methodology refinements plus player-value summary and subsystem coverage; the [walkthrough standard](../QUEST-WALKTHROUGH-STANDARD.md) requires early guidebook access and full rewarded how-to progression. Existing pilot quests must be gap-audited against it.
- 2026-11-15 — Added choice-boundary guidance: distinguish mandatory gates from player-selected branches and audit quest prose separately from task predicates and progression coverage.
- 2026-11-15 — Resolved source identity for two previously uncertain addon pins by tracing version metadata history: Ars Additions 21.3.0 and Ars Elemental 0.7.10.4 now have immutable source commits. Their declared Ars Nouveau dependencies are older than TMM's base pin, so compatibility remains a client/source review item.
- 2026-11-15 — Occultism 1.227.0 profile started from matching release source. The Dictionary progression graph and broad worker branches are mapped; the current chapter is only a checkmark stub. Matching source defines ordinary miner-tag recipes for Draconium Ore and Uranium Ore, plus an Ancient Eldritch Miner raw-uranium recipe. Common tag/runtime availability is not yet confirmed; see docs/COMPATIBILITY.md and the profile.
- 2026-11-16 — Occultism miner follow-up verified the Foliot-tagged Draconium output against Draconic Evolution's selected source tag and corrected a stale claim: the Eldritch Draconium storage-block generator call is commented out for 1.21. Uranium and raw-metal outputs remain conditional on unresolved common-tag membership; no runtime/quest changes.
- 2026-11-16 — Occultism 1.227.0 storage follow-up mapped the Dimensional Matrix/Controller Base/controller/remote route, default and tiered capacities, stabilizer geometry, safe capacity reduction and loaded-chunk remote boundary. Source and guide differ on both stabilizer line-of-sight wording and remote shift-click wording; the access check itself accepts a loaded destination chunk and supports the held/Curios/inventory remote paths. Exact JAR parity/client validation remain open; no quest/runtime changes.

- 2026-11-15 — Neo Vitae 1.1.53 profile added from version-aligned source and generated Scriptura Vitae; mapped tiers 0–5, parallel branches, Mine Key drops, EV/Spiritus pathways, and the source-supported Occultism storage bridge. API/version and client behavior remain untested; see the profile and compatibility note.
- 2026-11-16 — Neo Vitae follow-up traced offhand-orb EV harvesting: source checks ServerPlayer kill attribution and a BloodOrbItem in offhand, computes EV from victim max health and sacrifice bonus, and stores only the amount fitting the orb. No victim whitelist was found in this handler; projectile attribution and victim/runtime boundaries remain to test. No quest/runtime edits; see the profile.
- 2026-11-16 — Neo Vitae 1.1.53 combat follow-up mapped Blood Siphon and Blood Shield event formulas/defaults, Anima-network input/output, 10-second uncureable PvP severance with doubled upkeep, and the client-only siphon-stream setting. Distinguished these Anima transactions from Essentia Vitae fluid placed in the offhand Orb; documented an integer-truncated zero-cost shield boundary for testing. Effective pack config and client combat behavior remain unverified; no quest/runtime changes.
- 2026-11-15 — JAWR 0.5.39 profile started from the exact hash-pinned JAR. Mapped the 223-entry native Guidebook and nine-stage supernatural journals; identified the reported cauldron Altar Power bypass in all ten cauldron-crafting recipes and a cross-version familiar recipient report. See the profile and `docs/COMPATIBILITY.md`.
- 2026-11-15 — Forbidden Magic 0.5.37 profile started from the exact hash-pinned JAR. Mapped its gated Soul Magic guide additions, four code-granted necromancy ranks, soul rites and phylactery creation/binding path. The user's separate Archlich target requires Phylactery before Arca; Arca is not present in this addon. See the profile.
- 2026-11-15 — Standalone Vampirism and Werewolves are marked deferred for this knowledge queue because the owner-approved target retires those mods after Hunter/Monk migration; this is a research-status reconciliation only, not a runtime change.
- 2026-11-15 — Summoning Rituals 3.14.2 profile added from exact-release source. Confirmed it is a recipe framework with no default rituals and no TMM altar acquisition/recipe scripts; documented typed input/output channels, sacrifice semantics and command risks. See profile and compatibility notes.
- 2026-11-15 — Botania Revived 2.0.2 profile and Botania × Ars source-supported interaction report added from the exact hash-matched JAR/source. Recorded default-enabled Asgard/creative/exploration modules, Loonium loot-mode risk, and Ars mana/Source bridge for later balance/client tests. See profile, interaction report and compatibility notes.
- 2026-11-16 — Botania Revived follow-up mapped the native Mana Fluxfield omitted from the first pass: it converts each received Mana to 10 FE, buffers 12,800 FE, and attempts to export up to 1,600 FE/tick across adjacent capability receivers. Added its recipe/Lexica path and near-full, partial-transfer and consumer-compatibility checks. No quest/runtime edits.
- 2026-11-15 — EvilCraft 1.2.100 profile and Rejuvenated Flesh × Dragon Survival interaction report added from the exact hash-matched JAR and matching tagged source. The guidebook path, two-age advancement triggers, Blood Infuser examples, spirit/environment systems and food handler are mapped. Recorded the placeholder quest, worldgen/Tectonic and faction gaps; no pairwise client tests.
- 2026-11-15 — Roots Classic 1.5.10 profile added from the exact hash-matched JAR and version-aligned source. Mapped Runic Tablet onboarding, component/spell/staff progression, altar tiers, ritual setup, powered stones and crystal staff. Recorded the Terra regeneration config mismatch, staff-use debit before mana validation, Earth Spike block-copy boundary, and placeholder FTB chapter. Runtime checks remain open; see profile and compatibility notes.
- 2026-11-16 — Scriptor Magicae 2.0.14 source/JAR profile completed. Verified Dictionary → Guide access, audited all 47 tome definitions, source-injected loot pools, Nominomist trades, writing, casting, Lectern gates and artifact/Engraving data. Corrected the unavailable Identify Scroll route, unresolved Patchouli reward reference, and unenforced book/scroll cost caps. Runtime/client tests remain open; no quest/runtime files changed. See [profile](mods/scriptor-magicae.md).
- 2026-11-16 — Iron's Spells 'n Spellbooks 3.16.3 profile recovered from a binary-corrupted Markdown blob, then re-audited against a transient exact-pin JAR whose SHA-512 matches packwiz. Corrected guidebook coverage; added exact station/book recipes, structure-set placement, cauldron recipe counts and native advancement evidence. The exact `v1.21.1-3.16.3` upstream source tag now replaces the post-release snapshot; reproducible source-to-binary proof and remaining side-path audits are still open, so the profile remains `researching`. No quest/runtime edits. See profile and compatibility notes.
- 2026-11-16 — Goety 3.2.2 profile advanced: inventoried all 31 Cursed Infuser recipes and all 11 soul-yield override resources; mapped exact Arca recipe and advancement, Forbidden Scroll unlock, and Potion of Transformation ritual. It remains `researching`: Forbidden Fragment sourcing, full altar/brew/servant coverage and client boundaries remain open. No quest/runtime edits; see [profile](mods/goety.md).
- 2026-11-16 — Reconciled the Goety Cataclysm profile to the post-Goety integration snapshot. Its exact-artifact resource inventory remains useful, but further bytecode analysis is blocked until the pinned JAR is reacquired or version-aligned source becomes available; the public source reviewed targets 1.20.1. Profile remains `researching`; see [profile](mods/goety-cataclysm.md).
- 2026-11-16 — Ars Ocultas 2.6.2 is marked `blocked`: the exact required Minecraft dependency range `[1.21,1.21.1)` excludes TMM's 1.21.1 target under NeoForge's Maven-range rules. This is a static metadata finding, not a reproduced load failure; the mod was left unchanged. Existing profile includes the spirit-jar bridge, Source recipes, altar flag bypass risk and pilot reconciliation. See [profile](mods/ars-ocultas.md).
- **Current session (2026-11-16):** Scriptor Magicae, Goety, Goety Cataclysm, Ars Ocultas, Ars 'n Spells, Astral Sorcery, Mahou Tsukai, Draconic Evolution, ProjectE, Project Expansion and Eidolon: Repraised knowledge updates merged sequentially. Eidolon's profile maps its Codex, parallel patron ladders, stations and WIP research, and records the prayer cooldown config discrepancy. Exact pinned JAR equivalence and client acceptance remain open; no quest/runtime implementation.

- 2026-11-16 — Ars 'n Spells 3.3.5 profile clarified the Mana Well's default source-level throughput (2 mana/tick per player, 40/sec at 20 TPS), cubic area test, per-tick player scan, and no per-tick cost in the addon handler. Parent ritual lifetime and effective config remain unverified; see [profile](mods/ars-n-spells.md).
- 2026-11-16 — Astral Sorcery source profile now inventories all 14 lumen binding families and seven equipment-slot types, with their major trigger/effect themes. It also maps all nine Lightwell recipes and derives baseline throughput, catalyst lifetime and 2,000 mB overflow risk from the pinned source formula. Per-slot lumen values/charge drain, remaining optional recipes and network throughput remain open; no quest/runtime edits. See [profile](mods/astral-sorcery.md).
- 2026-11-16 — Mahou Tsukai profile added from official wiki and pack records: Compendium/circle/catalyst onboarding, mana/leylines/circuits, spell families, selected spell economics, advancement map, and current chapter gap. The pinned JAR symlink is dangling, so artifact mechanics and pack defaults remain unverified; the profile stays `researching`. Recorded that the Neo Vitae catalyst adapter is disabled/unsafe. No quest/runtime edits; see [profile](mods/mahou-tsukai.md).
- 2026-11-16 — Draconic Evolution profile added from the exact pack pin, official 1.21 source snapshot and pack progression records. Mapped fusion-tier recipes, Energy Core structure, Reactor thresholds/fuel values, Chaos Island and Guardian flow, and integration risks. The pinned JAR is not locally available, source-to-binary equivalence is unproven, and the chapter is a placeholder; full recipe/guide/client coverage remains open. No quest/runtime edits; see [profile](mods/draconic-evolution.md).
- 2026-11-16 — Draconic Evolution follow-up traced Guardian death and Chaos Crystal rewards to immutable source: death marks the arena defeated and spawns an unattributed Dragon Heart; a separate post-defeat crystal break gives configured Chaos Shards. No native death advancement was found; `SUMMONED_ENTITY` is used on respawn. Keep FTB player-kill credit as an explicit unverified blocker; no quest/runtime edits.
- 2026-11-16 — ProjectE 1.1.0 profile added from its exact pack pin and version-matched upstream release commit. Mapped the native Stone/EMC/Table loop, Collector/Relay/Condenser contracts, practical branches, gate records and quest gap. Identified mismatch between recipe-only boss-associated item gates and the older personal-boss entitlement manifest; gate source remains unmerged and untested. Full item/effect coverage, exact artifact comparison and EMC registry audit remain open. No quest/runtime edits; see [profile](mods/projecte.md).
- 2026-11-16 — ProjectE follow-up traced PE1.1.0 Table slot mutation paths: peripheral slots learn valued items, center slot separately adds sell EMC, shift-click clears the input, and output extraction debits EMC. The regular click-in/click-out transaction needs client validation; no duplication claim is made. A canceled learn event does not cancel the independent source-level value addition. No quest/runtime edits; see [profile](mods/projecte.md).
- 2026-11-16 — Project Expansion 1.0.6 profile added from the pack pin and immutable release source. Mapped sixteen matter tiers, Power Flower generation/formula and owner behavior, Stars, Alchemical Books, portable transmutation and Matter Upgrader. The Upgrader can promote compatible placed machines without crafting, subject to target-item knowledge and EMC; TMM's staged Red MK3 Shard recipe is therefore not a universal-use lock. Full recipes, guide/tutorial path, effective config, exact JAR comparison and client testing remain open. No quest/runtime edits; see [profile](mods/project-expansion.md).
- 2026-11-16 — Project Expansion follow-up traced `ItemMatterUpgrader.onItemUseFirst`: the tool is reusable, advances supported machines one tier per use, checks learned target item plus personal EMC delta, applies owner checks only to Flowers/Links, preserves their state, and still charges EMC in Creative. Added separate negative/boundary client cases for knowledge, balance, ownership, final tier and the Shard recipe path; no quest/runtime edits.
- 2026-11-16 — Eidolon: Repraised 0.5.0.8 profile added from its CurseForge pin and version-aligned 1.21.1 source. Mapped Codex/sign barter, both independently progressing patron ladders, altar inputs, sign chants, Crucible/Worktable/Brazier and the limited WIP research registrations. Recorded the source cooldown default/minimum conflict and the previous generated config's effective 21,000 tick value; Soul Enchanter defaults are unlimited. Exact JAR comparison and client/gameplay review remain open. No quest/runtime edits; see [profile](mods/eidolon-repraised.md).
- 2026-11-15 — Ars 'n Spells exact 3.3.5 pin/source reviewed; corrected its source line to the NeoForge 1.21.1 commit, split its two inscription paths, and mapped its mana/config/ritual contracts. TMM runtime and client behavior remain unverified; current quest chapter has no addon lessons.

- 2026-11-16 — Feywild 1.21.1-5.6.4 profile added from the pack pin, official release data and a matching-version source snapshot. The public getting-started wiki is for 5.0, so the profile marks the 5.6.4 walkthrough and several guide/encounter branches as incomplete. No TMM quests or runtime files changed.

- 2026-11-16 — Reliquary Reincarnations 2.0.85 profile added from the exact pack pin and current version-matched source snapshot. The old wiki diverges on the Tome's Nether Star recipe: current source doubles one `c:nether_stars` input at 256 charge. Profile remains researching; no TMM quests or runtime files changed.

- 2026-11-16 — Theurgy source follow-up mapped the Incubator's two-block/three-vessel setup, heat requirement, one-item-per-input atomic consumption, output backpressure, and a representative Oak Log recipe. This is source-only: the selected 1.79.0 JAR/source provenance mismatch and client/guide/remaining progression gaps remain open. No quests/runtime edits; see [profile](mods/theurgy.md).
- 2026-11-16 — Hexerei source follow-up traced the Blood Sigil contract: 3 magic damage precedes a 250 mB `hexerei:blood_fluid` increment; the pre-release source does not filter for living entities or require damage success, and can overfill a partially filled non-multiple tank. Pin 0.5.0.3 still lacks matched source/JAR verification, so entity and capacity boundaries remain test cases. No quest/runtime edits; see [profile](mods/hexerei.md).
- 2026-11-16 — Malum evidence audit found the previously recorded 1.8.2 source SHA currently returns GitHub API 404; the accessible 1.21.1 branch snapshot declares 1.9.0 and is no longer treated as pin evidence. Added upstream open issue #477 as an unverified potential server-hang risk for the pinned 1.8.2 release. Profile remains `researching`; no quests/runtime edits.
- 2026-11-16 — HexCast NEOFORGED profile advanced from an immutable version-aligned source snapshot. Mapped staff Media extraction order and sources, default amethyst Media values, the Curios scan gap, creative bypass, and advancement-gated health overcast. The source declares 0.11.1-19 for MC 1.21.1, but its equivalence to the pinned JAR and the pack-effective configs remain unverified. No quest/runtime edits; see [profile](mods/hexcast-neoforged.md).

- 2026-11-16 — Primal Magick follow-up traced the 6.0.7 Research Table transaction: weighted project-template selection, aid-block radius, candidate-material scaling, success/reward formulas, writing costs, failure consumption and successful-project progression. Found source-level consumed-flag discrepancies for XP and observation materials; marked for disposable-world verification. Full templates/guide/spell/ritual and exact-JAR parity remain open. No quest/runtime edits; see [profile](mods/primal-magick.md).
- 2026-11-16 — Primal Magick 6.0.7 source follow-up mapped Ritual Altar salt-network scanning, the stability delta/clamp formula, negative-stability mishap cadence, obstructed/canceled start behavior, and Dowsing Rod salt/symmetry/delta diagnostics. Individual ritual recipes, exact pinned-JAR parity and in-world outcomes remain open; no quest/runtime changes. See [profile](mods/primal-magick.md).

- 2026-11-16 — Enchanted: Witchcraft altar follow-up mapped exact 3×2 formation, default 32-block orientation-dependent ellipsoid, provider capacity limits, per-tick recharge, per-type upgrade maxima and 20-tick observer updates. Found a strict greater-than power-debit boundary: an altar at exactly the requested amount returns false. Source/JAR equivalence, effective pack config and consumer behavior remain open. No quests/runtime edits; see [profile](mods/enchanted-witchcraft.md).

- 2026-11-16 — Totemic evidence corrected: located exact 0.12.18 release tag/commit `1a253b29815842aa4359d6118b8cd84cce3048a1`; tagged Gradle metadata matches the pin. The GitHub file connector returns 404 for source/resource files under this ref, so the 0.12.19 branch API findings remain explicitly non-pin-exact. Profile remains `researching`; no quests/runtime edits. See [profile](mods/totemic.md).
- 2026-11-16 — Forbidden & Arcanus follow-up audited all 22 ritual JSONs from its version-aligned source cache and exact generated resources. Corrected the prior count and cataloged all declared ingredients, essence costs, output/result types, and tier/enhancer gates. JAR parity, pack overrides, item routes and live operation remain open; no quest/runtime edits. See [profile](mods/forbidden-arcanus.md).
- 2026-11-16 — Psi follow-up reconciled exact-tag guide/code against the stale leveling-tree description. Mapped six tutorials, starter CAD/Programmer/bullet/Psidust route, default 5,000 maximum and 25/tick regeneration attributes, cast-cost/overflow boundaries, 32-block fizzle limit, and 29 non-enforced default piece groups. Pin-to-source parity, full component/trick inventory and client checks remain open; no quests/runtime edits. See [profile](mods/psi.md).

- 2026-11-16 — Psi: Tweaks and Additions follow-up reconciled its profile to immutable source commit `ebb3d3c28bb1f41790ccb7cd6bbcff31889d2363`. Added the default 19 unlock IDs and complete 19-recipe research-provider catalog, plus config gates, Ideaspace capacity/persistence and the eight recipes with direct Mekanism item inputs. Program Researcher availability and alternate no-Mekanism outputs remain unresolved, so it stays `researching`; no quests/runtime changes. See [profile](mods/psi-tweaks.md).

- 2026-11-16 — JAWR 0.5.39 follow-up mapped its opening Mutandis → Rowan → first Altar route from the exact recipe data and creator issue report. The recipe orders Mandrake Root, Exhale of the Horned One, Egg and yields four Mutandis; its displayed 500 Altar Power cost is not enforced by the reported pinned-version cauldron path, avoiding a first-Altar deadlock. Added 0.5.39 release-note changes; mutation odds, full Jar/Oven sourcing, other subsystems and in-pack behavior remain open. Profile remains `researching`; no quest/runtime edits. See [profile](mods/witchery-remake.md).

- 2026-11-16 — Goety 3.2.2 follow-up documented new Cursed Plinth servant resting/repair, Sealer chest/Apparition Door protection with its stated 1 soul/min player-placement upkeep, Polar Crystal spell-triggered power, expanded Crypt trial/vault content and the release-note-level custom servant config feature. Exact release pin is confirmed; new-block boundary/source/client tests and config schema remain open. Profile stays `researching`; no quests/runtime edits. See [profile](mods/goety.md).
- 2026-11-16 — Ars Nouveau Drygmy follow-up mapped exact 5.13.5 taming, Charm recipe and Henge conversion; natural/Mob Jar habitat scans; worker cadence, Source draw and yield formulas; fake-player loot/XP conversion; default blacklist timing; and ignored adjacent-inventory remainders. Boss/rare eligibility, effective pack blacklist, pinned-JAR equivalence and client boundaries remain open. No quest/runtime edits; see [profile](mods/ars-nouveau.md).
- 2026-11-16 — Occultism crusher follow-up traced the 1.227.0 CrushingRecipe/CrusherJob/config path and generated raw-iron recipe. Separated tier eligibility, operation count, time multiplier and output multiplier; documented default 2/3/4/6 dust and 400/200/100/60 tick source thresholds, the anti-loop opt-out, invalid-input return, CrusherJobEvent timing and full-extractor remainder loss. All recipe-by-recipe, effective-config, tag-resolution and client checks remain open. No quest/runtime edits; see [profile](mods/occultism.md).
- 2026-11-16 — Neo Vitae follow-up mapped the 1.1.53 Well of Suffering's 1-point damage selector, EV-per-damage entity data map, baby truncation, altar credit path, default boss/villager/golem values, and distinct Well blacklist vs no_sacrifice semantics. The latter zeros yield without preventing damage; no packaged Well blacklist values were found in the source snapshot. Effective TMM tags, ritual cadence, loot/death boundaries, exact JAR parity and client behavior remain untested. Recorded the progression-sensitive balance audit in COMPATIBILITY.md. No quest/runtime edits; see [profile](mods/neovitae.md).
- 2026-11-16 — Botania Revived follow-up mapped the 2.0.2 Gaia Guardian summon/arena contract, Terrasteel vs Gaia Ingot modes, party-health scaling, true-player hit accounting, per-contributor loot calls, true-killer bonus, team-wide latched No Armor state and normal/hard reward pools. The loot table runs per unique player who hit the boss, creating a party-size reward scaling audit recorded in COMPATIBILITY.md. Exact pinned-JAR parity, effective config, item pickup and fight behavior remain untested. No quest/runtime edits; see [profile](mods/botania-revived.md).
- 2026-11-16 — Iron's Spells cauldron transaction re-audited against the exact `v1.21.1-3.16.3` source tag. Corrected the builder count to 13 normal brews + 2 soak/byproduct recipes, clarified they share the Brew recipe type, and documented the 1,000 mB total fluid cap and 50% scroll-recycle failure cost. The earlier JAR inventory still differs by one recipe, so the profile stays `researching` pending binary path-level reconciliation; no quest/runtime changes.

- 2026-11-16 — Goety 3.2.2 source audit now maps shared Dark Altar start, energy/XP debit cadence, staged pedestal consumption, and base interruption costs. Failed rites return the activation item but do not refund paid souls, XP or consumed pedestal inputs; recipe-specific outputs and ritual overrides remain open. No quest/runtime changes; see [profile](mods/goety.md) and the compatibility note.

- 2026-11-16 — Goety Cataclysm addon review now carries forward the parent Goety 3.2.2 ritual transaction costs as an explicitly labeled inference for addon `goety:ritual` recipes. The official 1.21.1 release notes mention servant persistence and robe/crown healing fixes. The addon source repository exposes only a 1.20.1 branch and the exact pinned JAR is unavailable for code inspection, leaving Fabricator and class-level behavior blocked. No quest/runtime changes; see [profile](mods/goety-cataclysm.md).
