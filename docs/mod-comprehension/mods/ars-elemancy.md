# Ars Elemancy — focused addon profile

| Field | Value |
| --- | --- |
| Manifest ID / mod ID | `ars-elemancy` / `ars_elemancy` |
| Parent | [Ars Nouveau](ars-nouveau.md); depends on Ars Elemental |
| Pack pin | `ars_elemancy-1.21.1-1.18.3.jar`; SHA-1 `f7e01437c86fc74e2abb2d93554dc20c30b214b` |
| Source snapshot | [Source commit dfb1828](https://github.com/Lyrellion/Ars-Elemancy/tree/dfb18286106aca1ca39a9b0053d64a1ef5041751) identifies 1.18.3. Its declared Ars / Ars Elemental dependencies are older than the TMM pins; binary equivalence is unverified. |
| Status | **researching** — recipe-level essence combinations are source-backed; armor upgrade variants, exact guide entries and cross-version behavior need review. |
| Guide | Use Ars *Imbuement Chamber* and JEI for the recipe/catalyst arrangement. The exact addon-specific Notebook page is not confirmed. |

## What the addon adds

Ars Elemancy expands Ars Elemental with six dual-element and one four-element specialization. Its essence recipes are a real progression bridge into the combined equipment; it is not an independent magic school with its own experience resource.

| Imbuement output | Base reagent | Nonconsumed pedestal essences | Source cost in source datagen | Specialized path |
| --- | --- | --- | ---: | --- |
| Cinder Essence | Manipulation Essence | Fire + Air | 3,000 | Cindermancer |
| Tempest Essence | Manipulation Essence | Water + Air | 3,000 | Tempestmancer |
| Silt Essence | Manipulation Essence | Earth + Air | 3,000 | Siltmancer |
| Mire Essence | Manipulation Essence | Water + Earth | 3,000 | Miremancer |
| Vapor Essence | Manipulation Essence | Water + Fire | 3,000 | Vapormancer |
| Lava Essence | Manipulation Essence | Fire + Earth | 3,000 | Lavamancer |
| Elemancer Shard | Source Gem | Air + Fire + Water + Earth | 3,000 | Elemancer four-element set |

These recipes come from the exact 1.18.3 source commit’s `AEImbuementProvider`; verify the recipe JSON in the pinned JAR/JEI before treating the cost as pack-observed. The catalysts are pedestal ingredients in Ars's Imbuement recipe format, not consumed recipe inputs.

Each path adds light/medium/heavy clothing and armor variants plus a Focus of the corresponding dual element; the quad-element set combines all four. The installed pilot teaches Cinder Essence and Cindermancer Hat only (quests 38–39).

## Walkthrough

1. Make Manipulation Essence and the two selected elemental Essences through Ars progression.
2. Place the Imbuement Chamber, add the base reagent, and put the elemental essences on nearby pedestals exactly as the *Imbuement Chamber* page explains. Confirm Source reaches the recipe and retrieve the combined Essence.
3. Use JEI to choose a matching armor/focus route; compare recipe tiers and ensure the chosen piece retains threads/enchants.
4. Repeat optionally for the other five paired elements or the four-element Elemancer Shard. These are choices, not mandatory gates for the core Ars book.

## Evidence and limits

- Pin and two pilot recipes: `manifest/ars_quest_pilot.json`.
- Exact source recipe provider: [AEImbuementProvider.java at dfb1828](https://github.com/Lyrellion/Ars-Elemancy/blob/dfb18286106aca1ca39a9b0053d64a1ef5041751/src/main/java/lyrellion/ars_elemancy/datagen/AEImbuementProvider.java).
- Source cost is declared as 3,000 in the upstream recipe object; no measured rate/processing time or client behavior.
- Not run: each exact recipe against pinned JEI; armor thread/upgrade retention; whether all recipe paths remain unchanged with the current Ars Elemental pin.
