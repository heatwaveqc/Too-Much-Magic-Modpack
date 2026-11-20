# Mod comprehension — coverage index

**Purpose:** resumable queue for Work research sessions. See [operating instructions](WORK-CHAT-INSTRUCTIONS.md). Every row corresponds to a `manifest/mods.json` selection record, **not a claim that the mod was successfully installed or tested**.

**Snapshot:** 2026-11-15 updated selection (165 entries; 156 enabled, 9 inactive/planned). Exact runtime pins still require reconciliation. Alex's Caves Continued and CodxLib are planned but not yet installed; Bigger Reactors variants are excluded.

**Current profile/artifact reconciliation:** see [completion receipt](COMPLETION-STATUS.md) for every linked profile, recomputed local pin hashes and remaining source gaps. The older roster snapshot below includes historical selections; it is not the current installed-mod list.

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
| `occultism` | Occultism | magic | enabled flag | full | source-complete | [profile](mods/occultism.md) |
| `pastel` | Pastel | magic | enabled flag | full | researching | [profile](mods/pastel.md) |
| `primal-magick` | Primal Magick | magic | enabled flag | full | researching | [profile](mods/primal-magick.md) |
| `psi` | Psi | magic | enabled flag | full | researching | [profile](mods/psi.md) |
| `roots-classic` | Roots Classic | magic | enabled flag | full | source-complete | [profile](mods/roots-classic.md) |
| `scriptor-magicae` | Scriptor Magicae | magic | enabled flag | full | source-complete | [profile](mods/scriptor-magicae.md) |
| `theurgy` | Theurgy | magic | enabled flag | full | researching | [profile](mods/theurgy.md) |
| `totemic` | Totemic | magic | enabled flag | full | source-complete | [profile](mods/totemic.md) |
| `witchery-remake` | Just Another Witchery Remake | magic | enabled flag | full | researching | [profile](mods/witchery-remake.md) |
| `ars-additions` | Ars Additions | magic_addon | enabled flag | full | researching | [profile](mods/ars-additions.md) |
| `ars-controle` | Ars Controle | magic_addon | enabled flag | full | source-complete | [profile](mods/ars-controle.md) |
| `ars-elemancy` | Ars Elemancy | magic_addon | enabled flag | full | researching | [profile](mods/ars-elemancy.md) |
| `ars-elemental` | Ars Elemental | magic_addon | enabled flag | full | researching | [profile](mods/ars-elemental.md) |
| `ars-n-spells` | Ars 'n Spells | magic_addon | enabled flag | full | source-complete | [profile](mods/ars-n-spells.md) |
| `ars-ocultas` | Ars Ocultas | magic_addon | enabled flag | full | source-complete | [profile](mods/ars-ocultas.md) |
| `ars-unification` | Ars Unification | magic_addon | enabled flag | full | source-complete | [profile](mods/ars-unification.md) |
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
| `summoning-rituals` | Summoning Rituals | ritual_framework | enabled flag | full | source-complete | [profile](mods/summoning-rituals.md) |
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

Reports below track individual evidence scopes. Create one Markdown file per *specific, actionable interaction* under [interactions/](interactions/README.md) after confirming both sides of an I/O contract. Maintain a short list here as each is added, with status `candidate` / `source-supported` / `verified-in-pack` / `blocked` / `rejected` / `stale`.

| Interaction report | Participants | Status | Key finding |
| --- | --- | --- | --- |
| [Neo Vitae vampire blood sources](interactions/neovitae--witchery-remake--vampire-blood.md) | Neo Vitae + JAWR | source-supported | JAWR ships an Anima-powered Bloodstone Pendant ritual and vampire Orb/altar compatibility; exact blood/Anima transfer is untested |
| [EvilCraft Rejuvenated Flesh](interactions/evilcraft--dragon-survival--rejuvenated-flesh.md) | EvilCraft + Dragon Survival | source-supported | Dragon species allowlist and custom Blood-food handler are both source-confirmed; the paired item-use behavior is untested |
| [Staged-cast payment](interactions/goety--goety-cataclysm--staged-cast-payment.md) | Goety + Goety Cataclysm | source-supported | Four staged paths (including guarded Abyssal Beam) emit before parent completion payment; cancellation behavior differs. Economics require tests. |

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
- 2026-11-16 — Ars Ocultas 2.6.2 was reclassified from `blocked` to `researching`: the official ATM10 repository identifies ATM10 as 1.21.1 and lists the same Ars Ocultas 2.6.2 Modrinth hash as TMM's pin. This contradicts the earlier inference that its declared `[1.21,1.21.1)` range prevents use on 1.21.1. The range discrepancy and TMM client behavior remain to be checked, but are no longer treated as a blocker. See [profile](mods/ars-ocultas.md).
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

- 2026-11-16 — Local Windows JAR continuation: [access audit](LOCAL-ARTIFACT-AUDIT-2026-11-16.md) reconciles 159 current pins (156 locally matched; three UI/QoL artifacts missing), distinct from the older 161-pin QA checkout. [Goety Cataclysm](mods/goety-cataclysm.md) now has pinned-bytecode Fabricator operation and corrected ritual economics; [all 61 recipe routes](mods/goety-cataclysm-recipes.md) include 11 conversions. Blocked spawns can spend a queued entry without producing a Watcher; cap/sensor stalls preserve the queue. Researching, no client tests. [Resume notes](RESEARCH-PICKUP.md).

- 2026-11-16 — Corrected [base Goety](mods/goety.md) ritual economics from the exact JAR: Arca 30 nominal Soul Energy, Forbidden Piece 50 and Potion of Transformation 60,000; the raw JSON values are rates per charged advance, with duration measured in advances once per 20 game ticks. Cursed Infuser cook times unchanged.

- 2026-11-16 — [Goety altar/research walkthrough](mods/goety-ritual-gates.md) now closes Forge setup and the nine ritual families used by Goety Cataclysm, plus personal Bygone/Buried/Warred/Terminus gates. Necroturgy may deplete Grave Soil on completion; Ender Keeper blueprint delivery has a filled/missing-handler boundary. Researching; no gameplay tests.

- 2026-11-16 — [Goety Cataclysm focus crosswalk](mods/goety-cataclysm-focuses.md) maps all 28 registrations, raw cost/cast/cooldown, accepted enchantments and producer effects. Distinguishes temporary focus summons from ritual companions, local cap checks/batch overshoot, recall/grounding, Summon Down and Ashen Breath charge cadence. Damage delegation and client acceptance remain open.

- 2026-11-16 — [Goety Cataclysm servant care/recovery](mods/goety-cataclysm-servants.md) maps listed healing inputs/amounts, last-item post-shrink checks, death payloads, revival targets, Prowler cube and skull/head recognizers. Spirit revival and skull reconstruction have different failure debit order; Ancient Remnant skull yields a juvenile Modern Remnant. All gameplay acceptance remains open.

- 2026-11-16 — [Goety ritual gates](mods/goety-ritual-gates.md) now covers all15 registered family predicates and shared conversion semantics, including entity-tag selection, caster ownership assignment, equipment clearing before spawn and ignored spawn-add failure. Divination candles are counted individually and extinguished on completion; enchanting contributions truncate per block. All boundary tests remain unrun.

- 2026-11-16 — [Summoning Rituals](mods/summoning-rituals.md) is source-complete **for its unused framework baseline** after pinned binary lifecycle and current-content audit. No survival altar/ritual path is present in inspected scopes; future authored content requires new coverage. Corrected item debit timing, extraction denial, active-save gap and server command context. No client/world validation.

- 2026-11-16 — [Mahou Tsukai pinned onboarding](mods/mahou-tsukai-pinned-onboarding.md) closes local-artifact access and starter instructions:33 crafting resources,53 counted/unordered catalyst registrations with53 named book matches,35 cloth-required flags and19 advancements. Hammer is cheap entry; Mortar/Pestle costs four Diamonds. Main profile remains researching pending full mana/spell/item progression and client acceptance.

- 2026-11-16 — [Mahou mana/circuit contract](mods/mahou-tsukai-mana-circuits.md): exact payment branches, personal-only growth credit, donation, owner/shared access, strict horizontal range without Y filter, redstone inversion and native defaults. Regeneration cadence, gem lifecycle, spell callers and effective pack overrides remain open.

- 2026-11-16 — Extended [Mahou mana research](mods/mahou-tsukai-mana-circuits.md) with100-game-tick regeneration cadence/formula, ley geometry, Fae spawn/loot supply, consumed Attuner, gem charging from circuits and Magitech interface scope. Formula-derived examples and unrun boundary tests remain separate from gameplay acceptance.

- 2026-11-16 — [Pinned Caliburn/Morgan progression](mods/mahou-tsukai-caliburn-morgan.md): lake growth/funding, strict size guard, enchanted sword conversion, innate-cap offerings and pre-payment loss boundary, owned-pet conversion, villager growth, native guide/quest stages and unrun acceptance tests. Combat delegates/effective configs remain partial.

- 2026-11-16 — [Mahou exchange/life-drain economy](mods/mahou-tsukai-exchange-life-drain.md): seven-powder random exchange and shrinking batch loop, durability tax/iteration limits and NORTH-side inventory, Drain Life payment/immunity/reward order and reward-after-failed-damage boundary. Native cap and unrun balance tests recorded.

- 2026-11-16 — [Mahou Contract/Geas/scroll rules](mods/mahou-tsukai-contract-geas.md): two-player sequential payment/persistent recognition, contract-break attribution, Geas named/species/item identity and species-only regen bonus, paid vow removal/list, fire reset, stationary boundary-scroll placement/loss boundary. Multiplayer acceptance unrun.

- 2026-11-16 — [Mahou artifact acquisition](mods/mahou-tsukai-artifact-acquisition.md): Clarent Dragon-owned cloud, Emrys strengthened offhand Stick/lightning event, Replica exact compound shield/counter/effect/armor checks and supporting Damage Exchange/Death Collection/Immunity Exchange. Cancellation ordering and gameplay remain unverified.

- 2026-11-16 — [Mahou William/Kodoku economy](mods/mahou-tsukai-william-kodoku.md): dictionary-dependent fiction creation, fame0/copy/reader gates, carried vs deployed owner checks, negative-style drain, worm recipe/death accumulation and consumed fire payout to active circuits without owner check. Full text scoring/theater/misfortune and gameplay remain open.

- 2026-11-16 — Recovered malformed Roots Classic Markdown; [pinned ritual audit](mods/roots-classic-pinned-rituals.md) corrects inventory46 rituals/22 components, maps all12 effect families and exact layouts, confirms Terra/staff defaults and identifies in-progress recipe reload and sacrifice-target indexing boundaries. Main remains researching.

- 2026-11-16 — [Roots22 spells/items](mods/roots-classic-pinned-spells-items.md) maps every component, eight powered stones, Crystal Staff/Sylvan/Wildwood, all three mutations and direct-handler discrepancies. Narrow Focus/Forceful hooks remain to close; client tests separate.

- 2026-11-16 — Roots Classic native source coverage completed after all22 spells/46 rituals/8stones/3mutations and major gear closure. Whole-JAR scans found no Focus/Forceful operation consumer; Engraved book recipe points at missing ID, so appendix provides actual JEI route. Known implementation gaps are findings; client/claims/quest acceptance remains unrun.

- 2026-11-16 — [Occultism pinned rituals](mods/occultism-pinned-rituals.md):158 native records/11 factory channels, activation/ingredients/event gates, timing/refund/saved identity and actual entity vs display output. Exact hash now locally verified; full jobs/familiars/processing research open.

- 2026-11-16 — [Occultism processing/miner crosswalk](mods/occultism-pinned-processing.md): all529 typed records/conditions/baseoutputs, exact worker/mineshaft/random-weight/capacity behavior, scoped sensitive-result scan. Full loaded tags and remaining helper/familiar/storage branches open.

- 2026-11-16 — [Occultism familiar/tool lifecycle](mods/occultism-pinned-familiars-tools.md): owner/range/Curios capture-release,17 roles, upgrades/recovery/trades, Spirit Fire stack conversion and Chalk/Otherworld-tool contracts. [Whole-profile receipt](COMPLETION-STATUS.md) now verifies all42 local artifact hashes;39 profiles remain researching.

- 2026-11-16 — [Occultism storage/harvest](mods/occultism-pinned-storage-harvest.md) verifies capacities, actual stabilizer search, remote selection/loading, Otherworld collection, soul/head and familiar resource hooks. Added two-sided [Roots→Mahou Emrys lightning](interactions/mahou-tsukai--roots-classic--emrys-lightning.md) event candidate with unrun positive/boundary tests.

- 2026-11-17 — Occultism native profile completed against hash-rechecked1.227.0 JAR: full tier/optional walkthrough;931runtime recipes,230guide entries,20build references; worker/familiar/storage/device/trader/partner methods,115merchant offers and possession-loot material gates. Five/42profiles source-complete;37researching. Runtime/effective-pack acceptance separate and unrun.

- 2026-11-17 — Ars5.13.5 exact-JAR companions now cover85glyph recipes/native guide,24ritual effects,5Source producers/5relays, Wixie/Whirlisprig/Bookwyrm/Starbuncle/Drygmy workers and six registered familiars. Parent remains researching for casting/processing/equipment/devices, seven addons and pilot/full-stage reconciliation. Five/42profiles source-complete,37researching; no runtime acceptance claimed.

- 2026-11-17 — Ars crafting companion adds Imbuement/Apparatus operation and failure costs,13+82+96+1=192recipe records,30enchantment families with required sequential ranks, and first passive Source Gem setup. Parent remains researching. Prism instance artifacts disappeared during QA rotation; exact Ars file recovered transiently from pin file9521184 and SHA1 matched;297retained classes independently byte-matched recovered JAR. No source-access blocker.

- 2026-11-17 — Ars now has fifteen exact-artifact companions, adding [four turret variants](mods/ars-nouveau-pinned-turrets.md) and [Warp Scroll/portal travel](mods/ars-nouveau-pinned-warp-portals.md). Source debit/failure ordering, timer controls, permanent versus temporary construction and destination/return boundaries are documented. Parent remains researching; 5/42 source-complete, 37 researching. No runtime acceptance claimed.

- 2026-11-17 - Ars now has nineteen exact-binary companions: [Alakarkinos archaeology](mods/ars-nouveau-pinned-alakarkinos.md), [remote cameras/Eye](mods/ars-nouveau-pinned-scrying.md), [Planarium dimensions](mods/ars-nouveau-pinned-planarium.md) and [spell records/Tomes/Codex](mods/ars-nouveau-pinned-spell-records.md) added. Payment/failure ordering, dimension/return identities, all50fixed compositions and knowledge-transfer XP are explicit. Source completion remains5/42;37researching. No runtime acceptance.

- 2026-11-18 - Ars now has twenty-one companions, adding [five Enchanter tools](mods/ars-nouveau-pinned-enchanter-tools.md) and [spell archery](mods/ars-nouveau-pinned-archery.md). Exact recipes, effects-only inscription, personal mana/repair, ammo and load/fire failure boundaries are source-backed. Counts remain5source-complete/37researching; no runtime acceptance.

- 2026-11-18 - Ars now has twenty-three companions: [equipped curios/foci](mods/ars-nouveau-pinned-curios-foci.md) and [potion storage/delivery](mods/ars-nouveau-pinned-potion-devices.md) added. Native effect bodies and remaining devices/world/addon/stage crosswalks remain open. Counts5/42source-complete,37researching unchanged; no gameplay acceptance.

- 2026-11-18 - Ars now has twenty-four companions, adding [nineteen status consumers and eighteen brewed variants](mods/ars-nouveau-pinned-potion-effects.md), including22Java brewing registrations and actual damage/heal/expiry/movement gates. Source count remains5/42complete,37researching; remaining devices/world/addon/stage work stays explicit.

- 2026-11-18 - Ars now has twenty-six companions, adding [inventory/spell detectors](mods/ars-nouveau-pinned-detectors.md) and [prism/redstone/ritual controls](mods/ars-nouveau-pinned-routing-controls.md). Exact thresholds, pulse attribution, actual occlusion and routing/payment/failure behavior documented. Counts5source-complete/37researching unchanged; jars/world/addons/stage reconciliation remain.

- 2026-11-18 - Ars now has twenty-seven companions with [Void and Light utility jars](mods/ars-nouveau-pinned-utility-jars.md). Item destruction/personal mana and portable light state boundaries source-backed; Mob Jar/all entity behaviors remain a distinct open branch. Counts5complete/37researching unchanged; no gameplay acceptance.

- 2026-11-18 - Ars now has twenty-eight companions with [Containment Jar and all24native special entity roles](mods/ars-nouveau-pinned-mob-jars.md): capture/release/state, direct insertion, item production, trade, redstone and boss-role boundaries. Counts5source-complete/37researching unchanged; world acquisition/addons/fullstage work remains.

- 2026-11-18 - Ars now has twenty-nine companions, adding [native plant/material acquisition](mods/ars-nouveau-pinned-plants.md): Archwood first pods versus saplings, Sourceberry location/growth/harvest and Magebloom seed-to-fiber/food consequences. Counts5source-complete/37researching unchanged; final core/stage/addon work remains.

- 2026-11-18 - Ars now has thirty companions with [field tools/physical runes/four grates](mods/ars-nouveau-pinned-field-tools.md). Next is full native walkthrough/pilot consolidation and seven addon exact-binary routes, rather than repeated core inventory. Counts5complete/37researching unchanged; no gameplay acceptance.

- 2026-11-18 - Ars native parent walkthrough consolidated against all30companions and [all49pilot IDs/tasks/rewards](mods/ars-nouveau-pilot-crosswalk.md). Pilot is physically source-present; PR74closed/unmerged, localvsintegrationsemanticdata identical except48positions. Native boss/Tribute endpoint source closed. Seven addon fullroutes remain before parent completion; counts5complete/37researching unchanged. No quest/runtime changes or gameplay acceptance.

- 2026-11-18 — Ars Unification exact-binary import ordering/Cut batch execution/native Crush route and both hash-pinned recipe endpoints audited (106Cut/240Crush resource JSONs). Profile remains researching for final glyph acquisition/cost and recipient-default audit; counts5source-complete/37researching unchanged. No runtime acceptance.

- 2026-11-18 — Ars Unification source-complete for two installed targets after final glyph/tier/mana acquisition, recipient serializer/multiplier, JEI and pilot audit. Important physical-vs-magical Cut probability distribution/tool difference documented. **6/42source-complete,36researching**; six Ars addon routes still open, parent researching. Runtime acceptance unrun.

- 2026-11-18 — Ars n Spells direct-binary carrier/guide/advancement companion added: all7recipes,6legacy guide resources, Loom/transcription distinction, all4pilot criterion meanings and ritual/Table/command credit boundaries. Broader mana/damage/cooldown/parent lifecycle and Iron's endpoint audit remains; counts6source-complete/36researching unchanged. No runtime acceptance.

- 2026-11-18 — Ars n Spells mana/lifecycle companion traces5mode routes, quote/payment reservations and exact Iron's3.16.3 mana event/clamp endpoint. Native Mana Well has no universal timer or recurring Source charge; finite Infusion distinct. Remaining regen/combat/caller lifecycle work explicit;6complete/36researching unchanged. No runtime acceptance.

- 2026-11-18 — Ars n Spells actual cast caller/payment companion added: Ars reserve/commit/cleanup, pinned Iron's native write/effect boundary, pre-effect-only refunds, last paid channel/scroll completion and legacy-open alternative-payment limitations. Remaining regen/combat/cooldown/adapter scope explicit;6/42complete36researchingunchanged. No runtime tests.

- 2026-11-19 — Ars n Spells regen/equipment/HUD and tag-only Source proximity audited against native Iron's10tick cadence/defaultmultiplier. Documented nominal rate/timing discrepancy for acceptance; no measured rate or balance approval. Counts6complete36researching unchanged; combat/school/cooldown/adapter source coverage remains.

- 2026-11-19 — Ars n Spells combat/school/resonance/cooldown source companion added, including event-credit limits, allschool decay behavior, multiplier versus additive directions and proxy/category cooldown distinction. Final optional-adapter/combined profile review remains;6/42complete36researching unchanged. No runtime tests.

- 2026-11-19 — Ars n Spells source-complete for installed3.3.5 scope: consolidated5pinnedcompanions into full guide/Loom/bind/cast and optional utility/regen/schools/cooldown walkthrough.317non-gametest classMethodref audit found no activeAlternativePayment.reservecaller; Curios discount/tag and school mapping scope closed. **7/42source-complete,35researching**; five Ars addon routes and combined parent review remain. No runtime acceptance.

- 2026-11-19 — Ars Controle exact1.6.16 binary nativeguide/glyph/sensor companion added: all15recipes, adaptive grammar, height/RNG/delay boundaries and unclamped sensor comparator formula. Five other device operation routes remain;7/42complete35researching unchanged. No runtime tests.

- 2026-11-19 — Controle Remote/Linkage binary companion added: order-specific dispatch, loaded crossdimension lookup, capability/context/class/blockblacklist/cache and outputredstone contracts. PortableRelay/ScrollHolder/WarpingPrism and finalcontrolreview remain;7complete35researchingunchanged. No runtime tests.

- 2026-11-19 — Controle Portable Relay lifecycle, blacklist, cache, moving context and actual Source/pause/toggle/range-query limitations documented. ScrollHolder/WarpingPrism and final review remain;7complete35researching unchanged. No runtime tests.

- 2026-11-19 — Controle Holder/Prism binary companion covers portal frame/inventory/Source and projectile/ticket/cost/CC contracts. Source debit differences and cancellation/partial-frame limits explicit. Final Remote control and whole-addon walkthrough/pilot review remain;7complete35researching unchanged. No runtime tests.

- 2026-11-19 — Controle source-complete for pinned1.6.16 installedscope: consolidated4binarycompanions into guide/acquisition/fulloptionalcontrolroutes and3pilotcrosswalk; left-clickair Remote reset/nativeSelectionHUD path closed. Known native limitations retained, not gameplay approval. **8/42source-complete,34researching**; four Ars addon routes/finalparentreview remain.

- 2026-11-19 — Ocultas exact2.6.2 guide/recipe/Altar companion:4currentrecipes+1legacypluralfile, reusable Imbuement catalysts, nativeJava guide and both-end nonconsuming ritual flag bridge. Spirit/SoulGem/finalroute audit remains;8complete34researchingunchanged. No runtime tests.

- 2026-11-19 — Ocultas worker/transfer companion maps four registered tiers, native jobs/output recipients/age, GUI filters, channel differences and Soul Gem failure ordering. Final full-route/pilot review remains;8complete34researching unchanged. No runtime tests.

- 2026-11-19 — Ocultas source-complete for pinned2.6.2 installedscope, with known failure/metadata limits: two binary companions consolidated into full guide/worker/material/Altar/gem route and3pilotcrosswalk; current nativeTier4stabilizer recipe distinguished from addonlegacyfile. **9/42source-complete,33researching**; three Ars addons and finalparentreview remain. No runtime acceptance.

- 2026-11-19 — Ars Elemental exact 0.7.10.4 artifact hash matched; native Notebook registration and Urn acquisition/linking/fluid/payment/manual bucket paths audited. Botania fallback is a stub; cauldron needs external fluid capability. New [companion](mods/ars-elemental-pinned-guide-urn.md); addon remains researching, total 9/42 source-complete.

- 2026-11-19 — Elemental [focus companion](mods/ars-elemental-pinned-foci.md): exact recipes/Mark/Anima, whole-spell first-focus cost subtraction, greater passive conditions and Necrotic worn-versus-held/event boundaries. Embedded Sauce recipient audited; global dependency selection and remaining addon systems still open. 9/42 unchanged.

- 2026-11-19 — Elemental [armor audit](mods/ars-elemental-pinned-armor.md) covers all48 tier-gated conversion recipes, retained components, light/medium/heavy slots and both attribute/tagged-damage response paths. Tooltip4 is not proof of stored tier3; matching school can mix weights. Three addon threads and other major systems remain open.

- 2026-11-19 — Elemental [three-thread audit](mods/ars-elemental-pinned-threads.md): exact recipes/slot/status/expiry bolt paths and both summon-power consumers. Summoning Sickness reduction does not occur for slots1–3: integer division of native maximum slot by10. Addon remains researching, 9/42 unchanged.

- 2026-11-19 — Elemental [empowering audit](mods/ars-elemental-pinned-empowering.md): native glyph substitutions, burn/freeze handlers, gravity query/velocity, exact five head recipes and inclusive RNG thresholds (6/11/21%, not nominal5/10/20). Remaining addon glyph/summon routes open;9/42 unchanged.

- 2026-11-20 — Elemental [steed audit](mods/ars-elemental-pinned-steeds.md): all four focus substitutions, native summon acquisition/event order/duration, riding and noSave boundaries. SkeleHorse inherits Horse, no breathing override proved; combat summons still open.9/42 unchanged.

- 2026-11-20 — Elemental [undead/Homing audit](mods/ars-elemental-pinned-undead-summons.md): revival includes expiry/dispel, inherited minimum400 and Vhex shadow lifetime, replay validates/posts events but bypasses mana precheck and debits summon capability if any; native provider only Player. Specific replay listener does not recurse.9/42 unchanged.

- 2026-11-20 — Elemental [Homing/Propagate Homing](mods/ars-elemental-pinned-homing.md): two Tier3 recipes, split/ignore/direction/child context, exact native targeting squared-distance thresholds; actual lifetime uses native Projectile config default60s rather than unread addon30s field.9/42 unchanged.

- 2026-11-20 — Elemental [Arc/Propagate Arc](mods/ars-elemental-pinned-arc.md): two Tier2 recipe routes, native bounce/Pierce/Sensitive semantics, propagated offsets and bytecode-confirmed anonymous constructor endpoints.4of39 glyph routes covered;12filters+23other effects remain, plus12tomes.9/42 unchanged.

- 2026-11-20 — Elemental [12filter audit](mods/ars-elemental-pinned-filters.md): exact acquisition/predicates/tags, positive allows family and negative inversion despite reversed native descriptions, negative block allowance and legacy two-argument inversion omission.16of39 glyph routes covered;23effects/12tomes/other systems remain.9/42 unchanged.

- 2026-11-20 — Elemental [Bee/Slime audit](mods/ars-elemental-pinned-bee-slime.md): two Tier2 glyph acquisitions, duration/sickness, custom AI/owner/nectar guards, variant/size and death-only split boundary.18of39 glyph routes covered;21effects/12tomes/other systems remain.9/42 unchanged.

- 2026-11-20 — Elemental [four status/control routes](mods/ars-elemental-pinned-mist-rust-sliding.md): Mist actualradius4+AoE and19/20tick cadence/fog; Rust/entity versus10%claimed-block oxidation; Sliding friction; Nullify recipe disabled by default and field-only delay reset.22of39 recipe routes audited,9/42 complete unchanged.

- 2026-11-20 — Elemental [Venom/Spores/Rage](mods/ars-elemental-pinned-venom-spores-rage.md): three recipe/status/AI routes, actual5+5Envenom config, Poison promotion, Spores initial gate/box/allies/reused bonus, Rage existing-target preservation.25of39 glyph recipes audited;9/42 unchanged.

- 2026-11-20 — Elemental [four Fire/static routes](mods/ars-elemental-pinned-fire-static.md): Cauterize Holder/value tag mismatch bytecode-confirmed; Conflagrate fire-before-eligibility/Blast; Spark status after unchecked damage; Discharge FE extraction and Static removal before damage.29of39 glyph recipes audited;9/42 unchanged.
