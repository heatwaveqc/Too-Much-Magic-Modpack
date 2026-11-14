# ATM10 QoL audit vs Too Much Magic B6
**Surveyed:** 2026-11-13 (America/Phoenix). **Source of truth for ATM10 mods:** official `AllTheMods/ATM-10` repository, particularly `config/crash_assistant/modlist.json` (497 entries including NeoForge loader and duplicate metadata), `config/invtweaks-client.toml` and its supplied build date. Compare to Too Much Magic B6 `manifest/mods.json`: **125 active pin records**. This comparison addresses **quality-of-life behavior**, not copying ATM's 500-mod architecture. A mod being installed/packwiz-pinned is not equivalent to a client behavior test.

- Official ATM10 repo: https://github.com/AllTheMods/ATM-10
- Official ATM10 modlist: https://github.com/AllTheMods/ATM-10/blob/main/config/crash_assistant/modlist.json
- Sorting configuration: https://github.com/AllTheMods/ATM-10/blob/main/config/invtweaks-client.toml
- Local previous known-good snapshot: `ecddc4342c439a2b973ad16acff112b7d0a4bb60` (125 mods, 118 server/both, 7 client-only; local player confirmed client launch + terrain generation)

## A. Owner-approved addition, and why
**Inventory Tweaks — ReFoxed** — CurseForge project **976858**, exact file **6407807**; `invtweaks-1.21.1-1.3.2.jar`, NeoForge 1.21.1, upstream stable release. The official ATM10 modlist confirms `invtweaks` at `1.21.1-1.3.2`; its `[tweaks]` config has `enableSort = 3` (both player and external sort), `autoRefill = true` and `enableButtons = 1`. Upstream defaults `enableSort = 3`, so no custom config is required. This supplies chest **middle-click sorting**; Mouse Tweaks only supplies item move gestures, not sorting. Keep both. Since the new mod is **client-only**, a successful dedicated-server boot **will not prove sorting works**: verify on client in chest and player inventory, and check for mouse/key mapping overlap.

Upstream: https://www.curseforge.com/minecraft/mc-mods/inventory-tweaks-refoxed

## B. Already covered; DO NOT duplicate

| Existing mod in B6 | ATM10 behavior already provided |
| --- | --- |
| Mouse Tweaks | Drag, wheel and bulk inventory item movement, **not sorting** |
| JEI + Jade | Recipes and contextual block/entity information |
| AppleSkin + Better Advanced Tooltips | Food/saturation, expanded tooltip information |
| Polymorph | Player selects output where recipes collide |
| Controlling | Searches, diagnoses conflicting key bindings |
| Just Zoom | Field of view zoom |
| Lootr | Per-player dungeon/container loot |
| FTB Chunks, Teams, Library | Map/chunk claiming and team basis; not identical to JourneyMap |
| Corail Tombstone | Recovery after death |
| FerriteCore, ModernFix, ImmediatelyFast, Entity Culling | Memory/load/render optimizations; optimize further only after profiling |
| FTB Quests | Quest framework present; **actual authored quests are absent** |
| Pufferfish Skills + Attributes; Improvable Skills 3 | Frameworks present; **custom Pufferfish magic tree absent** |

## C. Useful ATM10 gaps, **recommended for evaluation only**, not approved or installed in this pass

### Priority 1 — high practical value
1. **Crafting Tweaks** (+ required **Balm**): rotate, balance, clear craft grids, batch craft. Very useful with ~40 magic systems and food recipes. NeoForge 1.21.1 `21.1.11` published stable, exact CurseForge file **8697050**, project **233071**. It is different from container sorting. Need review modded altar/crafting-table slots and keybinds before activation. https://www.curseforge.com/minecraft/mc-mods/crafting-tweaks/files/8697050
2. **Simple Backups**: scheduled/rolling world backups with storage quota. Strong choice before any save/upgrade migrations and for long-lived multiplayer worlds; investigate correct backup interval and retention, available disk, restoring while players are online, and dedicated-server versus local Prism instance placement. ATM10 includes `SimpleBackups-1.21-4.0.30.jar`. https://www.curseforge.com/minecraft/mc-mods/simple-backups
3. **KeyBind Bundles**: radial menu / reuses a single key for many spells, rituals, curios and transformations. Particularly valuable given Ars, Iron's, Primal Magick, Occultism, Pufferfish and JAWR. ATM10 uses `1.5.0` according to official modlist; verify exact compatible file and UI coexistence with Controlling and Pufferfish. https://www.curseforge.com/minecraft/mc-mods/keybind-bundles
4. **Enchantment Descriptions**: in-tool-tip descriptions of enchants, useful for Apotheosis, Apothic Enchanting and modded enchants; small risk and no economy impact.
5. **Better Advancements**: full-size and more legible advancement screen with many mods, no progression effect. Especially appealing if some magic discovery ties to advancements. https://modrinth.com/mod/better-advancements

### Priority 2 — helpful, but preference or testing-dependent
6. **TrashSlot**: one-click disposal of excess drops while exploring dungeons and mining; can permanently delete irreplaceable magic artifacts, so require safety/confirmation policy, no inclusion by default.
7. **Just Enough Professions (JEP)**: villager **profession workstations** shown in JEI, valuable for complicated villager mechanics; it does **not** promise complete trade listings. **FTB JEI Extras** currently covers technology-specific JEI plugins such as ExtendedAE/AdvancedAE; with those deliberately excluded, there is little reason to install it.
8. **More Overlays Updated**: F7 mob-spawn light overlay, chunk boundaries and JEI chest-item highlight. FTB Chunks already handles claims/chunk visualization, but this adds precise spawn safety and inventory searches. https://modrinth.com/mod/more-overlays-updated
9. **Extreme Sound Muffler**: quiet noisy farms and spell effects per source. Valuable if many infernal/arcane ambient sounds become grating.
10. **Not Enough Animations / Cosmetic Armor Reworked**: optional presentation, no survival necessity; cosmetics might conflict with supernatural form rendering (vampires, JAWR lich, werewolves, dragon states) and must undergo client tests.
11. **Journeymap**: adds rich minimap/waypoints beyond FTB Chunks; client performance, discovery spoilers and UI overlap make it a user-preference decision.
12. **KeybindsPurger**: controls clutter cleanup. Review carefully to prevent hiding/clearing mandatory transformations/skill-tree bindings. Alternative is Controlling alone.
13. **AttributeFix / Overloaded Armor Bar / Colorful Hearts**: potential high-stat/high-health HUD and caps assistance in extremely high-magic play. **Not automatically needed** just because our pack contains Apothic Attributes; check actual caps, draw overlaps, and balancing. Especially worth examining if magic goes above vanilla 20 armor or normal health.
14. **spark + Observable**: developer diagnostics for late-game TPS/CPU, spawner farms and high-dimensional activity; profile problems before installing broad optimization stacks. May be better as developer/server tools than default client mods.
15. **Measurements**: measures dimensions/areas precisely when constructing Astral Sorcery and ritual layouts. Lower impact, nice for complex multiblocks.

### Additional high-value contenders for this unusually magic-heavy pack
- **Akashic Tome**: one slot for multiple **documentation books**, not a general item backpack. Strong thematic fit for the dozens of magic guides; unlike Pocket Storage and backpacks it does not carry ore, tools and combat items. I would put it in the **high-priority review queue**, with recipe/allowed-book and mod-guide compatibility checks. https://modrinth.com/mod/akashic-tome
- **Oracle Index**: displays wiki-format mod documentation inside Minecraft. Excellent in principle, but actual mod/page coverage may be sparse and requires client validation; complementary to Akashic Tome, not a replacement. https://modrinth.com/mod/oracle-index
- **Clumps / Get It Together, Drops!**: consolidate dropped XP or items to reduce rendering/entity pressure at heavy spawner farms. May matter for the planned Torment Nexus and other magic farms; profile and test actual entity behavior rather than installing every optimization mod.
- **Almost Unified**: ATM10 includes resource unification. It can prevent five incompatible silver/lead/sulfur output variants across magic systems, and is potentially one of the most important **cross-mod compatibility** helpers, not merely QoL. However, its recipe rewrites, canonical-material selection and optional NeoForge ore generation unification can **undermine planned special resource gates** (uranium, draconic dust, Replication ore). Review as an explicit integration project, with a safe whitelist/blacklist and automated KubeJS regression tests; **do not install without approval**. https://docs.almostreliable.com/almostunified/
- **Measurements**: precise tape measure for ritual circles, Astral attunement pads, and complex multiblocks; pure build convenience and low balance impact.

### Priority 3 — progression-changing or redundant; do not auto-add
- **Waystones**: easy world teleportation can trivialize magical mobility, discovery and dimension progress; reserve as a separately gated system if approved.
- **Nature's Compass / Explorer's Compass**: quick locate features may bypass biome and structure exploration or duplicate magical divination; consider only if gated appropriately.
- **FTB Essentials**: `/home`, `/spawn` travel commands can bypass the intended magic-first progression; avoid without redesign.
- **Tool Belt, Utility Vest, Pocket Storage**: potentially convenient general-purpose storage, but avoid accidental blanket storage-system bypass/early backpack creep; review use-case and progression. **Akashic Tome is a narrower documentation-book organizer and should be reviewed separately above.**
- **Easy Villagers**, farm/structure automation and similar mods: change mechanics, not mere QoL.
- **FTB Ultimine, Sophisticated Backpacks/Storage/Core, AE2, Refined Storage, Create**, and generic logistics: explicitly excluded or otherwise out of scope. Do not import ATM10 wholesale.
- Avoid duplicating UI/inventory sorters (e.g. Inventory Essentials and Inventory Tweaks together), redundant maps, or extra food/acquisition systems.

## D. Test plan / caveats
**Only Inventory Tweaks — ReFoxed is approved for B9.** Commit separately from the B6 baseline. Verify NeoForge 1.21.1 stable file and hash-pinned packwiz metadata; run metadata and dedicated-server regression to establish no original mods were lost. As a **client-only JAR**, it must additionally be installed and tested in an updated Prism instance to confirm middle-click chest sorting, player sorting, auto-refill, shortcuts, and interaction with Mouse Tweaks. Keep the old client-good 125-mod ZIP as rollback. Do not claim that any candidate in C is approved/installed or that a headless server boot validates a client-only behavior.
