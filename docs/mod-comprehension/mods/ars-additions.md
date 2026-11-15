# Ars Additions — focused addon profile

| Field | Value |
| --- | --- |
| Manifest ID / mod ID | `ars-additions` / `ars_additions` |
| Parent | [Ars Nouveau](ars-nouveau.md); depends on Ars Nouveau |
| Pack pin | `ars_additions-1.21.1-21.3.0.jar`; CurseForge/Modrinth pin in `mods/ars-additions.pw.toml`; SHA-512 `3f06e6e838c6a5bb14cb1f475f393c0ceeb7b78a7faf9f73df008adae69f68410382cde4511d2b73c0e99dd38732ba1e5d192aac941bb26f2ab771f8bbdbcc43` |
| Source and version gap | Exact addon release source found at [commit `91f102a90dc058cf40e4eac5a67a881e48b856b4`](https://github.com/Jarva/Ars-Additions/commit/91f102a90dc058cf40e4eac5a67a881e48b856b4), whose `version` file is `21.3.0`; its Gradle metadata depends on Ars 5.11.2 while TMM pins Ars 5.13.5. This aligns the addon source revision to the pack's addon version, but does not prove source-to-JAR equivalence or compatibility with the newer base. |
| Status | **researching** — release-version source and the primary feature surface are mapped; exact artifact comparison, addon interaction details and player routes need review. No client test. |
| Guide access | Use Ars Nouveau's Worn Notebook for shared Source/storage procedures. No Ars Additions guide section was verified. Addon quests must give full addon-specific instructions and cite the precise JEI item recipe/use. |

## Player-facing systems

| System | Inputs and actions | Output / value | Pack-specific status |
| --- | --- | --- | --- |
| Advanced Dominion Wand | Craft it from the pinned JEI recipe. Its radial controls set endpoint order and single/multi selection; test bulk-linking on a small group before applying. | Faster configuration of compatible Ars endpoints. | Exact recipe `ars_additions:apparatus/advanced_dominion_wand` is recorded by the pilot manifest. Pilot checks item acquisition, not successful bulk links. |
| Warp Index / Stabilized Warp Index | Craft the index, bind it to a Storage Lectern, then test one inventory read/write. Stabilized version is the cross-dimensional route. Destination chunk must remain loaded. | Remote access to linked Storage Lectern inventories. | Pilot covers Warp Index and the stabilization upgrade; exact dimension/chunk boundary needs client verification. |
| Ender Source Jar | Make a pair and link/configure them as the addon specifies; generate Source at one end and observe a consumer at the other. | Shared Source storage, not additional Source production. | Pilot covers the item recipe and intended shared-pool behavior; remote/unloaded-chunk behavior untested. |
| Mark / Recall / Unstable Reliquary | README advertises Mark to record a block/entity reference and Recall to target it; Reliquary stores the reference. | Reusable target reference for spell effects. | Not in the 49-quest pilot. Exact item/target limits and whether players are valid references in 21.3.0 need pinned evidence. |
| Retaliate | Form targets the entity that last hurt the caster in the prior five seconds (README). | Reactive counterspell setup. | Not piloted; target selection, expiry boundary and cast-form restrictions need test. |
| Ruined Warp Portals / Nexus Tower | World structures reportedly include an Explorer's Warp Scroll and teleport network. | Exploration and travel. | Worldgen frequency, structure dimension/biome rules and exact activation path not verified for the pin. |
| Codex Entries | README describes tiered random glyph teaching; tier 1 in basic dungeon loot, higher tiers not obtainable by default. | Alternate glyph discovery. | Loot tables and the meaning of “not obtainable” need exact-JAR inspection. |
| Arcane Permanent ritual | README says configurable and disabled by default. | Chunk loading if enabled. | Treat as disabled until the pinned config and TMM config prove otherwise; do not teach as an available pack feature yet. |
| Wixie Enchanting Apparatus and decor | README describes Wixie conversion of Apparatus for automated apparatus recipes/enchantments plus themed blocks/painting. | Craft automation and decoration. | Not represented in pilot; needs exact pin confirmation and an operational recipe walkthrough before quest coverage. |

## Walkthrough coverage and recommendations

The [49-quest pilot](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/74) covers only Advanced Dominion Wand, Warp Index, and Ender Source Jar. Add optional lessons for Mark/Recall, Retaliate, the Nexus Tower/ruined portal route, Codex discovery, and Wixie Apparatus only after the exact 21.3.0 artifact confirms those systems. For each, map recipe/structure access → configuration/use → observable success → honest task criterion and a useful fixed reward. Additions is not a tier ladder; its goal is remote logistics, reactive spells, exploration and automation.

## Evidence and next actions

- Exact pin/hash and quest evidence: `manifest/ars_quest_pilot.json` on the pilot branch.
- Upstream primary overview: [Ars Additions README at 21.3.0 release source](https://github.com/Jarva/Ars-Additions/blob/91f102a90dc058cf40e4eac5a67a881e48b856b4/README.md).
- Outstanding: inspect the exact 21.3.0 JAR; compare registries, recipes, loot, worldgen, config and Notebook entries; verify current Ars 5.13.5 compatibility, supported endpoints and chunk-loading behavior.
- Client tests: positive and unloaded/missing-link boundary for Warp Index; two jars with one shared Source event; bulk wand single vs multi endpoint; both portal structures; one Mark/Recall and one Retaliate timing boundary; confirm the Arcane Permanent default remains disabled.
