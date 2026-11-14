# Starter guidebooks and portable storage — research and proposal

Status: **Akashic Tome selected and pinned on feature branch**. First-join items and bag grants are **NOT approved or implemented**. This note does not change the post-Chaos-Guardian ProjectE lock.

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

## Proposed starting progression (not locked)

1. New world: one **Akashic Tome**, ideally preloaded with the selected introductory documentation, avoiding duplicate grants from individual mods. Do **not** auto-award an overpowered inventory.
2. Optional first storage milestone: a **Trinkets Pouch (27 slots)** by a short quest, or a carefully balanced default spawn grant if the owner decides early-storage pain outweighs early crafting gameplay. Choice pending testing, and avoid making Ars Nouveau a forced magic path.
3. Ars branch: allow upgrade to **Spellcaster Bag (54 slots)**, preserving its native Ars-crafted identity. Occultism's satchel should remain an independent branch, not be blocked by Ars.
4. Post-Chaos Guardian: ProjectE **Alchemical Bags** are available as part of its normally gated progression, **not** starter equipment.

## Required checks before release

- Akashic Tome release hash/download and a new **combined 126-mod dedicated server smoke**; client first-join and opening/transforming all installed documentation books.
- Confirm the tome survives logout, dimension moves, respawn/death and book extraction; verify duplicate prevention.
- Ars pouch: ordinary items, Curios equip, keybinding, death, logout/rejoin, shift-click, item count, upgrading while full, interaction crash reproduction, containment/duplication rules.
- Confirm Occultism satchel size and recipe in *this* installed release before promising a reward.
- Check ProjectE Alchemical Bags (all 16 colors, trades, loot, quests, starting items) stay inaccessible before Chaos Guardian; check all forms of acquisition, not merely recipes.

No first-join KubeJS script, quest reward, or ProjectE access change is part of this feature branch.
