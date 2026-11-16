# Ars Nouveau and first-log starter reward reconciliation — 2026-11-16

**Merged:** PR [#225](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/225), commit `edd555accf09765d802686cc6aceef5b8a2b24ea` on `test/full-integration`. Quick PR validation passed; **no owner-authorized full smoke, Prism packaging, or live client acceptance**.

## Source reconciliation
- Ported the complete 49-node Ars questline (29 core + 20 in seven installed add-ons) from `feature/ars-quest-pilot` into the current 44-chapter book, preserving all 19 Alcohol Is Magic quests, all 18 Getting Started quests and two new Welcome starter items.
- New book: **225 quest nodes**, **186 authored**; stable preexisting quest IDs and reward IDs retained. Ars pilot's overview is the only deliberately XP-only orientation; other 48 Ars nodes grant meaningful item rewards and XP.
- Imported Chimera kill-credit advancement `tmm:ars_pilot/chimera`; it has no stand-alone reward and depends on a player-credited Wilden boss kill. Pinned Ars addon versions match the pilot.
- Never import older pilot's `manifest/quest_content*.json`, shared `lang/en_us.snbt` or whole quest directories from its feature branch. Their old versions would delete newer Alcohol and starter content; the assembler imports only the pilot manifest/tools and Chimera advancement.
- Current `Punch a Tree` quest already has a personal Curio Bag and XP thanks to the concurrent quick-fix. This PR **preserves** those claims and changes `tmm:first_log` to detection-only (no reward function dispatch). The obsolete empty `first_log.mcfunction` is removed from the pack. The hidden advancement remains to detect any log or stem and never issues an item.

## Quick-only source gates
```sh
python3 scripts/validate.py
python3 scripts/validate_quests.py
python3 scripts/verify_quick_repairs.py
python3 scripts/validate_ars_quest_pilot.py
```
The linked PR's ordinary Verify bootstrap workflow may run these quick checks. **Do not** run full modded smoke, server boot, or Prism publication without explicit authorization.

## Next live QA (test build only when authorized)
1. Start a fresh disposable world/player. Obtain a single log; verify `tmm:first_log` objective succeeds but inventory does **not** receive a bag automatically.
2. Open FTB Quests → Getting Started → Punch a Tree. Claim and verify **one Ars Elemental Curio Bag + XP (no torches)**; verify a second claim does not create another bag and a teammate may claim their own.
3. Verify Welcome still awards the Enigmatic amulet and cursed ring, without unsolicited login grants.
4. Open Magic → Ars Nouveau and confirm **49 visible, readable, non-overlapping** nodes and 225 quests globally; Alcohol Is Magic remains at 19 and the deleted reactor chapter is still absent.
5. Work through novice, a source/imbuement task, a helper, a glyph, and at least one task for each of seven addons. Check claims, prerequisites, native advancement task credit, descriptions and quantities.
6. In a disposable arena, beat an actual Wilden Chimera with player kill credit; owning a tribute alone must **not** complete the kill-credit task.
7. On a backed-up older world, account for preexisting players whose inventory already includes the formerly auto-gifted bag; a newly claimable quest reward may legitimately give them a second bag. **Do not auto-strip items, reset advancement, or change valued world/player data.**

**Unverified until live:** FTB quest GUI layout, per-player claim semantics across teams, survival acquisition, client-side Curios equip, Chimera kill-credit event and older-world migration.
