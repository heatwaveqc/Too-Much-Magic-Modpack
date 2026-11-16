# Monk/Hunter skill GUI and TreeChop — next client test cases

**Date:** 2026-11-16. **Implementation branch:** `feature/progression-skill-gui-treechop-2026-11-16`, [PR #223](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/223).  
**Base:** current integrated `tmm_progression`, **not** the older Vampirism module or stale four-level pilot.  
**Source of symptoms:** [player follow-up](PLAYER-FOLLOWUP-2026-11-16.md).  
**Status:** compiled and unit-tested source; **client and full-pack TreeChop behavior NOT ACCEPTED**. Do not rerun stable First Sunset initiation unless the exact implementation changed. No full smoke without separate owner approval.

## Owner-observed faults — two independent TreeChop cases

1. **Chopping display regression:** after the first chop, TreeChop's indicator/progress bar disappears even while the Jade tooltip still displays `Chopped Spruce Log`, `2/6 chops` and 8 logs. This is a HUD persistence issue; it is not Monk XP. [PR #223](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/223) renders a narrow fallback progress bar **only while aiming at `treechop:chopped_log`**, reading TreeChop's own client tree cache and honoring TreeChop's indicator toggle. It does not change TreeChop's state, source texture or Jade overlay.
2. **XP credit:** the user observed no Monk XP from full felling. TreeChop 0.19.3's felling uses `Block.dropResources` directly and fires `ChopEvent.BeforeFellEvent` and `FinishChopEvent`; this can bypass the normal player `BlockDropsEvent` progression listener. PR #223 listens to the **actual optional events** via runtime reflection (no required TreeChop JAR); it remembers exact pre-fell log positions/materials, checks each really disappeared to air/fluid, and awards the same native barehand-block activity once per removed log. `RecentBlockCredits` deduplicates normal and TreeChop callbacks by player/dimension/position/tick, preserving one XP award per actual log and one-time milestone counts.

**Deliberate limitation:** the current Monk activity awards 2 XP per hand-harvested `#minecraft:logs` block. Logs that mod authors failed to put in that tag, creative/automated fells, tool-driven fells, and leaves should not silently get that bonus. The bridge currently does **not** solve player-placed block XP farming or bulk magic mining; those are separate issues in the core engine.

## K-key progression GUI behavior

The client screen is now a first-party native progression interface, not Vampirism or Tyz. The default key is **K**, rebindable in Options → Controls → Key Binds, category **TMM Progression**. It shows tabs for **Monk** and **Hunter**, native rank/XP/next threshold, available native points, scrollable colored ability cards (learned, locked, automatic), owned/max ranks, price, required cultivation rank, and effect summaries on hover. The client can request **Buy** for selectable skills and a two-click confirmed **Respec**; the server validates all costs, exclusive choices and prerequisites using existing `ProgressionApi.buy`/`respec`, then returns a fresh authoritative snapshot. The GUI works around the known `StringArgumentType.word()` namespaced command defect for purchases, but does **not** fix the broken command syntax by itself.

Important: the Monk's 25 classic powers are **native automatic grants** by cultivation rank, not purchasable with SP. Therefore its screen is predominantly a readable progression catalog; Hunter's optional branching nodes are the immediate purchase/respec exercise. Unspent Monk points are not proof the classic baseline needs purchasing. Optional Monk enhancements can be placed in this GUI later, after their effects are implemented.

## Targeted live acceptance matrix

| ID | Setup / action | Required outcome |
| --- | --- | --- |
| G01 | Load Prism client with rebuilt `tmm_progression`; press **K** | Progression screen opens without crash, K appears as rebindable control; closing restores gameplay |
| G02 | Join Monk on disposable world (skip repeat First Sunset if already verified), open K | Native rank, points and **actual current Monk XP** match server state; all 30 automatic abilities visible and scrollable |
| G03 | Rank up with gameplay, close/reopen K | Snapshot refreshes with correct current rank, XP, skill ranks; no client guesses or stale ranks after logout/dimension change |
| G04 | Open Hunter tab on a Hunter character with unspent native SP, choose eligible paid skill | Server deducts configured points once, card changes to learned; locked/insufficient prereq skill refuses purchase |
| G05 | Try mutually exclusive Hunter choices, then confirmed **Respec** | Both cannot be owned simultaneously, respec refunds paid ranks once, auto-grants remain; no XP minting or rank reversal |
| G06 | Open GUI on server with **different** player state (two clients), or while not enrolled | No other player's XP/skills leaked; not-enrolled path shows no purchase privilege |
| G07 | HT TreeChop 0.19.3 with a normal spruce tree; chop once, then aim at the same chopped trunk | Fallback `chops / total` and horizontal progress bar remain visible after first hit and update every subsequent hit; Jade text/count not hidden |
| G08 | Toggle TreeChop's native chopping-indicator setting OFF | Fallback respects OFF and does not force HUD progress back on |
| G09 | Barehand fell a tree of exactly seven log blocks (record XP before/after) | **14 native Monk XP** from seven real log removals, plus any legitimately earned *one-time* 10-log milestones. No XP for leaves or merely starting a chop. Use the actual tree count, not the number of strikes |
| G10 | Fell a second tree, then cut seven logs with an ordinary axe | Only real barehand fells award Monk physical-training log XP; no extra credit for ordinary tool use. No double credit if both callback types fire |
| G11 | Sneak-break individual log (TreeChop bypass), plus full-fell with non-Monk | Ordinary vanilla barehand log earns its normal 2 XP; non-Monk unaffected. No TreeChop bridge XP for unfinished chops |
| G12 | Pam's trees and modded chopped logs, including log variants | Credit only qualifying source materials in `#minecraft:logs`; no missing/duplicated logs, no forced crash from absent tags |
| G13 | Test with TreeChop **removed** and server dedicated/no client | Progression loads without a hard TreeChop dependency; K GUI still works on client; no TreeChop-only HUD attempts or server classloader errors |
| G14 | Cancel/protect an attempted log break and try creative-mode felling | No Monk practice XP for canceled/blocked/creative fells; no item loss or creative XP abuse |
| G15 | Large tree/multiplayer TreeChop assistance | Only actual completed block removals credited to verified player, once each; no cross-player duplicate, expected CPU and packet behavior |

**Important observed source caveat:** HT's TreeChop 0.19.3 has a `fellCreditStrategy` option but its indirect log breaking still goes through `LevelUtil.harvestBlock` and `Block.dropResources`, so changing only the config is not a reliable substitute for testing the event bridge. Its `NeoForgePlatform.finishFellTreeEvent` constructs an `AfterFellEvent` without posting it in the inspected upstream source; therefore the bridge intentionally listens to **`FinishChopEvent`** instead.

## Source-only checks and next-build status

- `compat/progression` Gradle compile/JUnit is the **only automated build intended** on this branch. The `progression-core.yml` workflow has no server startup steps.
- [PR #223](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/223) changes the already-integrated first-party Java JAR. Merely merging documentation or importing an old branch will **not** include the GUI and XP adapter: the next authorized build must compile `tmm_progression` from a source tree containing the new `ProgressionScreen.java`, `ProgressionMenuNetwork.java`, `TreeChopHud.java`, `TreeChopXpBridge.java`, and revised `ProgressionEvents.java`.
- Do not claim GUI CLIENT PASS, TreeChop progress-bar CLIENT PASS, or TreeChop per-log XP PASS without the corresponding G01–G15 evidence.
- Do not rerun accepted JAWR/First Sunset controls or a full smoke just to verify a cosmetic widget; wait for the next owner-authorized comprehensive live build and target the failures directly.
