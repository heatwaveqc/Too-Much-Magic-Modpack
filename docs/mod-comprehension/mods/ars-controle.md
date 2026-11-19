# Ars Controle — focused addon profile

| Field | Value |
| --- | --- |
| Manifest ID / mod ID | `ars-controle` / `ars_controle` |
| Parent | [Ars Nouveau](ars-nouveau.md) |
| Pack pin | `ars_controle-1.21.1-1.6.16.jar`; SHA-512 `458c5a878a1418ffce46d1dd109f4ae23bcbe10bd7153edcaff8d1ab49ab3032f37977488eee7cecdb550cd99672f560e9d851818d6775d5d7e222632285d1a6` |
| Source snapshot | [Version commit 1.6.16](https://github.com/Vonr/Ars-Controle/commit/14c5f4770a9ec265491fb9a7ae1a60a2123dfbc0); Gradle declares mod 1.6.16, Minecraft 1.21.1. It compiles against Ars 5.13.1, while TMM pins 5.13.5. |
| Status | **researching** — source and all major README features are mapped; exact source-to-JAR comparison and client behavior tests remain. |
| Guide | Pinned ACDocumentation registers native Worn Notebook Crafting entries for five devices and Items entry for Portable Brazier Relay; glyphs use native index. Cite the matching JEI recipe/use and include control steps in quest text. |

## What the addon does

Ars Controle adds spell-routing/control, remote configuration, fluid/item/energy/redstone transport, a server-load signal and portable portal/ritual utilities. It has no linear progression tier; each device is an optional applied-control lesson.

| Feature | Inputs / operation | Result and limits | Pilot coverage |
| --- | --- | --- | --- |
| Remote | Crouch-use the Remote on a compatible Ars endpoint; radial menus select which endpoint is locked and single/multiple target selection. Use again on compatible targets; left-click air clears the target. | Remote Dominion-Wand-compatible configuration. Pinned binary resolves GlobalPos across server levels; invalid dimensions fail, while endpoint compatibility/loading determines useful operation. | Pilot item quest; does not prove configuration. |
| Scryer's Linkage | Use Dominion Wand first on the target block, then on Linkage. | Remote connection for supported items, liquids, energy, redstone and other capabilities. Exact capability matrix varies by endpoint. | Not in pilot; must be checked against current pack machines. |
| Temporal Stability Sensor | Place sensor and read with comparator; connect output to a reversible shutoff. | Higher redstone output means the world is closer to lagging; use it to stop optional farms. It signals load; it does not itself reduce lag. | Pilot item quest; does not prove comparator or cutoff works. |
| Portable Brazier Relay | Craft and link to an operating Brazier. Keep original ritual brazier loaded and supplied with Source. | Carries supported ritual effects with the player; some rituals are blacklisted. | Pilot item quest; must test one allowed and one blacklisted ritual. |
| Warp Scroll Holder | Replace a suitable portal-frame block with the holder; insert a location-bound Warp Scroll; supply Source within 10 blocks Manhattan distance. | Opens/closes a configured Warp Portal. | Not in pilot. Source README gives the construction procedure; verify exact source jar requirement in client. |
| Warping Spell Prism | Aim the prism in a spell route; compatible projectile may be warped across dimensions. README describes CC:Tweaked configuration for block positions, not entities. | Long-range projectile routing; cross-dimensional targeting carries world/loading and server-authority concerns. | Not in pilot; exact setup and destination rules need artifact inspection. |
| Glyphs | Adaptive NOT/OR/XOR/XNOR filters; Above/Level/Below/Random filters; Precise Delay. | Filters gate following spell branches; Random has a 50% base chance adjusted by Amplify/Dampen. Precise Delay delays the rest of a spell by (2^{	ext{Extend Time}}) ticks (README). | Entire set is absent from pilot. Exact sequence grammar and cost require the pinned guide/recipe. |

## Walkthrough map

1. Browse JEI for the Remote, Linkage, Sensor, Brazier Relay, Scroll Holder and Prism. Treat each as a separate optional branch.
2. For Remote and Linkage, choose one Ars device and write down the endpoint order before linking; verify a real item or fluid transfer.
3. For the Sensor, use a comparator to measure both quiet and deliberately loaded states, then verify a redstone-controlled machine changes state.
4. For a Portal, cite Ars *Warp Portals* plus the Controle Scroll Holder procedure; verify source requirements and open/close behavior.
5. For the portable relay, use a safe supported ritual first; compare it with a known blacklisted ritual.
6. For glyphs, use Scribe's Table, include the target filter then a simple branch, and compare valid, invalid and random outcomes.

Quest tasks must identify whether they detect an item, configured endpoint, transported resource or actual spell effect. Rewards should be XP plus useful redstone/linking items; never imply that crafting a Sensor proves it throttled a farm.

## Evidence and remaining boundary tests

- Exact JAR pin/hash and 3 pilot item tasks are in `manifest/ars_quest_pilot.json`.
- Version-specific primary source: [1.6.16 README](https://github.com/Vonr/Ars-Controle/blob/14c5f4770a9ec265491fb9a7ae1a60a2123dfbc0/README.md).
- Source has a clear 1.21.1/1.6.16 identity but Ars dependency is 5.13.1 vs the pack's 5.13.5; test capability exchange against TMM's real endpoints.
- Not run: positive/negative linkage, sensor threshold, valid/blacklisted portable ritual, portal source distance and cross-dimension Prism boundaries.


## Direct-binary continuation — 2026-11-19

[Native guide/glyph/sensor companion](ars-controle-pinned-glyphs-sensor.md) now covers exact artifact/all15recipes, eightfilter+PreciseDelay acquisition and actual grammar/chance/Y/timing, plus comparator getter/scheduling. Native Java guide registration corrects earlier no-addon-page caveat. Five other device operation routes remain to audit; source-vs-runtime distinction keeps status researching. All97classes externally decompiled, fullSHA512matched.


[Remote and Scryer's Linkage binary companion](ars-controle-pinned-remote-linkage.md) now traces mainhand/sneak/radial/lock/corner controls, perendpoint dispatch, event/credit limits, capability/context/cache/blacklist and redstone proxy contracts. Source supports crossdimension lookup; operation while unloaded and claims aren't promised. Three device branches and final UI/pilot review remain.


[Portable Relay lifecycle](ars-controle-pinned-portable-relay.md) now covers binding/blacklist, item and map caches, moving context/inventory and actual Source/pause/toggle/range-query limits. Two device routes and final control/pilot review remain; researching.
