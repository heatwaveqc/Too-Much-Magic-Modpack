# Starter guidebooks and portable storage — research and proposal

Status: **first-log Trinkets Pouch reward approved and authored as an integration candidate**, Akashic Tome pinned, automatic book consolidation still unimplemented. This does not change the post-Chaos-Guardian ProjectE lock. **Full cross-mod bag nesting rejection is NOT yet implemented**; see the blocker below.

## Book consolidation: selected mod

- **Akashic Tome 1.8-30**, 1.21.1 NeoForge release, CurseForge project **250577**, file **7773841**. Bundled by packwiz reference only (no mod JAR checked into Git); the published file SHA-1 is `6493e575f295e18fac1f7c5399c05c53caa1f7ce`.
- CurseForge release: https://www.curseforge.com/minecraft/mc-mods/akashic-tome/files/7773841 ; Modrinth equivalent version: https://modrinth.com/mod/akashic-tome/version/MRro5nPG
- Like older ATM packs, the tome consolidates documentation books in one slot. Manually attach a book by crafting it with the tome. Akashic Tome **alone does not suppress or combine each mod's first-spawn book distribution**.
- **Next phase:** identify the exact three books observed in the B6 test (and any others introduced later), verify they can be stored in a Tome, choose whether to give a pre-filled Tome at first join, and disable/reconcile each upstream automatic book grant so books aren't duplicated. Confirm the specific 1.21.1 serialization/API and persistence before writing KubeJS startup logic. Do not guess registry IDs.
- Avoid the third-party automatic guidebook harvesting addon without auditing it and its book-handling safety. A minimal first-login pack script may be easier to control.

## General-purpose backpacks already installed

| Item | Source | Slots | Supports ordinary inventory? | Suggested placement |
| --- | --- | ---: | --- | --- |
| Trinkets Pouch (`ars_elemental:curio_bag`) | Ars Elemental | **27** | **Yes**, except restricted nested containers/blacklist | Candidate for first storage quest or spawn; requires regression testing |
| Spellcaster Bag (`ars_elemental:caster_bag`) | Ars Elemental | **54** | **Yes**, same exclusions | Upgrade from Trinkets Pouch through Ars crafting |
| Surprisingly Substantial Satchel | Occultism | verify in installed build | Magical portable storage | Early alternative branch, test recipe and acquisition |
| Alchemical Bag, any color | ProjectE | **104** in known ProjectE versions | Yes; verify owner/color behavior in this release | **Post-Chaos Guardian only**; do not give as spawn/early reward |

The two Ars Elemental items are both genuine general-storage bags. Source class `ArsElemental.attachCaps` attaches **27** slots to `CURIO_BAG` and **54** slots to `CASTER_BAG`. `CurioHolder.canStore` uses the storage blacklist and vanilla `canFitInsideContainerItems()`; the blacklist includes both bag items and vanilla bundle. They can be equipped in Curios or used in the hotbar, and have a configurable keybind (default J). The basic bag recipe uses a chest and eight Magebloom Fiber; the caster bag is its upgrade.

Source references:
- https://github.com/Alexthw46/Ars-Elemental/blob/1.21/src/main/java/alexthw/ars_elemental/ArsElemental.java
- https://github.com/Alexthw46/Ars-Elemental/blob/1.21/src/main/java/alexthw/ars_elemental/common/items/CurioHolder.java
- https://ars.guide/book/items/curio_bag/
- **Open stability issue**: https://github.com/Alexthw46/Ars-Elemental/issues/128 reports intermittent bag interaction packet crashes and possible Curios contents loss. The original report is not a confirmed reproduction in our pinned 0.7.10.4 build; test before granting automatically or relying on it for important storage.

The Ars Additions **Handy Haversack** is **not** a portable container; it sends items to a bound, loaded remote inventory. Do not confuse it with the wearable bag.

## Locked introduction decision and authored prototype

1. **First-log trigger:** An FTB Quests introductory chapter contains `Punch a Tree`. The first time **each individual player** acquires one item in `#minecraft:logs`, a hidden, persistent `tmm:first_log` advancement runs `tmm:rewards/first_log` and gives **exactly one** `ars_elemental:curio_bag` (27 slots). Acquiring wood through loot or trading also qualifies; actual tree punching isn't required.
2. **Why not a conventional FTB Quests item reward?** FTB Quests can share task progress between teammates. A one-time vanilla player advancement avoids accidentally rewarding only a subset of a team or duplicating rewards via re-claims. The quest has an *advancement task* and **no item reward entry**; its description explains the direct award. This is a purposeful implementation choice, pending integrated first-login testing.
3. **All later bags must be crafted** under their own mod's recipes. The 54-slot Spellcaster Bag remains an Ars upgrade; the Occultism Satchel is an independent magical path. ProjectE's color-coded Alchemical Bags remain post-Chaos Guardian.
4. **Akashic Tome** is pinned but a preloaded spawn tome and removal of duplicate mod books are separate, still pending. Do not silently grant the tome before verifying all mod books and automatic grants.
5. **No bags inside bags**, independent of mod, color, capacity, player/team, or insertion method. Nested storage is a *release-blocking invariant*, not just a tooltip suggestion. Universal enforcement and validation remain incomplete.

### Authored files (integration candidate; not yet runtime certified)

- `config/ftbquests/quests/data.snbt`, `chapter_groups.snbt`, and `chapters/intro.snbt`: initial quest book
- `kubejs/data/tmm/advancement/first_log.json`: per-player detection of any `#minecraft:logs`
- `kubejs/data/tmm/function/rewards/first_log.mcfunction`: one-time player-context pouch award
- `kubejs/data/tmm/tags/item/portable_bags.json`: verified Ars Elemental, Occultism, and 16 ProjectE color IDs
- `kubejs/data/ars_elemental/tags/item/blacklist_bag_item.json`: additive tag entry to prevent **these bags** from being placed into either Ars Elemental bag

**No automatic grant happens at spawn**: it triggers upon the first log. Do not revoke `tmm:first_log` as part of ordinary resets unless intentionally repeating its reward.

## Cross-mod nesting: partial implementation and blocker

Ars Elemental `CurioHolder.canStore` reads `#ars_elemental:blacklist_bag_item`; the supplied additive datapack tag links it to `#tmm:portable_bags` (while preserving Ars' own entries). **Only Ars Elemental destinations are currently protected by that tag.**

Occultism `StorageSatchelSlot.mayPlace` rejects only `occultism:satchel`, not Ars bags or ProjectE bags. ProjectE `AlchBagContainer` uses `InventoryContainerSlot` backed by `ItemStackHandler`, without a pack-wide exclusion. Merely adding items to the Ars tag **does not protect those destinations**, and bag-in-bundle / shulker nesting still requires verification. Before survival release implement a narrow server-authoritative NeoForge compatibility bridge (or verified upstream APIs) to deny nested insertion both through GUIs and automation. Avoid tick-based inventory deletion/cleanup that could erase stored items.

Source-backed assessments:
- https://github.com/klikli-dev/occultism/blob/version/1.21.1/src/main/java/com/klikli_dev/occultism/common/container/satchel/StorageSatchelSlot.java
- https://github.com/sinkillerj/ProjectE/blob/mc1.21.1/src/main/java/moze_intel/projecte/gameObjs/container/AlchBagContainer.java
- https://github.com/sinkillerj/ProjectE/blob/mc1.21.1/src/main/java/moze_intel/projecte/impl/capability/AlchBagImpl.java
- https://github.com/Alexthw46/Ars-Elemental/blob/1.21/src/main/java/alexthw/ars_elemental/common/items/CurioHolder.java

## Required checks before release

- Akashic Tome release hash/download and a new **combined 126-mod dedicated server smoke**; client first-join and opening/transforming all installed documentation books.
- Confirm the tome survives logout, dimension moves, respawn/death and book extraction; verify duplicate prevention.
- Ars pouch: ordinary items, Curios equip, keybinding, death, logout/rejoin, shift-click, item count, upgrading while full, interaction crash reproduction, containment/duplication rules.
- Confirm Occultism satchel size and recipe in *this* installed release before promising a reward.
- Check ProjectE Alchemical Bags (all 16 colors, trades, loot, quests, starting items) stay inaccessible before Chaos Guardian; check all forms of acquisition, not merely recipes.

This feature **does** include a first-log advancement reward and a minimal FTB Quests chapter. No first-join grant, bag recipe edit, ProjectE access change, or fully universal cross-mod item-container insertion guard has been applied. Verify that the chosen quest schema and advancement/function actually load on 1.21.1 in a disposable world. Test one reward per player even with online/offline teams, relog, death and repeated log pickup.
