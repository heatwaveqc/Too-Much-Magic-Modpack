# Ars Ocultas — focused addon profile

| Field | Value |
| --- | --- |
| Manifest ID / mod ID | `ars-ocultas` / `ars_ocultas` |
| Parent | [Ars Nouveau](ars-nouveau.md) × Occultism |
| Pack pin | `ars_ocultas-1.21.1-2.6.2.jar`; SHA-512 `4a673096a2d0b9a5a83aca2747a8a08211205ed40875b4c22061f774146d9d4fff9c872e2c1b6b59e5fef2d1bf3b3a08732f22743ade4e0a82ba08636dc701a5` |
| Source snapshot | [2.6.2 version commit](https://github.com/dphaldes/Ars-Ocultas/commit/14aa0f4a05f8e85aca03b9bbe084e2bff36b007b). Gradle declares MC 1.21.1, Ars range [5.3,6), and Occultism >=1.220.4; TMM pins meet those declared ranges. |
| Status | **researching** — the worker-containment loop and recipe branch are documented; exact altar operations, all spirit types and throughput need artifact/client validation. |
| Guide | Ars Worn Notebook contains an **Ars Ocultas** section per the addon README. Teach players to open it, then cite *Ars Ocultas* plus each JEI recipe. |

## Player-facing systems

Ars Ocultas makes Occultism worker spirits usable from Ars-containment-compatible jars and adds recipe alternatives and an altar integration.

| System | Inputs and operation | Output / constraints | Pilot coverage |
| --- | --- | --- | --- |
| Spirit-attuned gem alternative | In Enchanting Apparatus: Source Gem plus Conjuration, Manipulation and Demon's Dream Essences; supply Source. Compare with the base recipe in JEI. | Alternate route when the normal gem recipe's diamond is scarce; output is the existing Occultism Spirit Attuned Gem. | Pilot quest 40 detects gem ownership, not which route created it. Recipe evidence is in the exact addon manifest. |
| Containment Jar worker | Obtain a valid Occultism spirit using Occultism's own ritual; capture/use it in a compatible Containment Jar. The jarred spirit retains its job. It pulls appropriate nearby items in a 7×7 area; players can right-click items onto the jar, and the jar exposes an item capability for hoppers/pipes/Starbuncles. | Processed items/normal worker output; throughput and vertical bounds are not stated in README. A Crusher Spirit still needs output collection. Janitor Spirit has special output pushing to an inventory above. | Pilot quest 41 checks a jar item only; the actual worker process is not credited. |
| Sacrificial Altar | Craft the addon altar via Enchanting Apparatus; follow the *Ars Ocultas* Notebook entry and use an ordinary eligible Occultism ritual to establish the input arrangement. | Bridge for Occultism ritual work involving contained creatures and supplied items; exact accepted ritual matrix needs source inspection. | Pilot quest 42 checks altar ownership; this does not prove a ritual completed. |
| Additional recipes / quality of life | Check every JEI recipe after choosing Ars Ocultas or Occultism progression. | README says “new recipes to make progression easier,” but does not enumerate the whole set. | Needs full recipe inventory to close. |

## Walkthrough and test plan

1. Read the *Ars Ocultas* Notebook section and choose one ordinary Occultism spirit job.
2. Craft the compatible Containment Jar; place/capture the spirit; feed one supported item by right-click, then by hopper. Observe the corresponding processed output; place an output inventory where required.
3. Compare Spirit Attuned Gem recipe paths in JEI and craft the alternate route once.
4. Build the Sacrificial Altar only after the specific Occultism ritual inputs and output location are read from JEI/guide.

Positive/boundary tests: one valid Crusher input and one unsupported item; hopper in vs no hopper; Janitor with/without inventory above; exact 7×7 edge vs outside; ritual with valid vs missing input. No tests run.

## Evidence and limits

- Exact hash and pilot IDs/recipes: `manifest/ars_quest_pilot.json`.
- Primary source: [2.6.2 README](https://github.com/dphaldes/Ars-Ocultas/blob/14aa0f4a05f8e85aca03b9bbe084e2bff36b007b/README.md) and [version metadata](https://github.com/dphaldes/Ars-Ocultas/blob/14aa0f4a05f8e85aca03b9bbe084e2bff36b007b/gradle.properties).
- Unresolved: enumerate every recipe and altar-supported ritual from the exact JAR; confirm Worker's item whitelist, distance/vertical rules and chunk lifecycle.
