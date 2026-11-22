# Apotheosis wandering-trader enchanted diamond gear — 2026-11-22

## Result and evidence boundary

The installed Apotheosis 8.9.0 defines **10 named enchanted diamond items**, each available as a rare trade at **Summit or Pinnacle** world tier. All required Apothic Enchanting/Spawners mods are recorded as loaded in the save. There are no named variants for Haven, Frontier or Ascent in these definitions. These are potential offers, not guaranteed stock.

The full potential trade catalog comes from the installed mod data, not from a registry stored in the save. Read-only inspection of this save found **1 wandering trader across 24 entity-region files / 660 populated entity chunks**, with **0 diamond-gear offers** and no read errors. This is a saved snapshot; unsaved live entities/offers are outside its coverage.

Build: experimental integration source `c56fb18ffc3b9f12f27317e7fb37f15c32576a8c`. Provider: `Apotheosis-1.21.1-8.9.0.jar`; SHA-256 `23a8b4a21d0d8744f097c77c8a96fcba14c4b95f3b1bb542412f40dab272872e`. Save mod list confirms Apotheosis 8.9.0, Apothic Enchanting 1.6.4 and Apothic Spawners 1.4.0. No competing copies of these ten resource paths were found in other installed JARs, KubeJS loose data or world datapacks; the world datapack directory is empty. The saved enabled/disabled pack list is preserved in the sanitized JSON. Resources under `old_data/` are excluded.

## Named gear

Every row below exists identically at both Summit and Pinnacle. Costs are the base JSON inputs before any runtime merchant price adjustments. Enchantment levels use decimal numbers, including values above vanilla caps.

| Name | Diamond base | Base cost | Enchantments |
| --- | --- | --- | --- |
| Arachnid's Fear | Diamond Sword | 1 Diamond Sword + 45 Emerald | Bane Of Arthropods 10; Fire Aspect 5; Looting 5; Mending 1; Unbreaking 5 |
| Bonesplitter | Diamond Axe | 1 Diamond Axe + 64 Emerald | Scavenger 2; Capturing 2; Looting 5; Mending 1; Sharpness 10; Unbreaking 3 |
| Captive Dreams | Diamond Sword | 1 Diamond Sword + 45 Emerald | Capturing 5; Looting 4; Mending 1; Sharpness 5; Unbreaking 5 |
| Eternal Vigilance | Diamond Sword | 64 Diamond + 32 Phantom Membrane | Life Mending 5; Scavenger 5; Looting 5; Sharpness 10; Unbreaking 5 |
| Greatplate of Eternity | Diamond Chestplate | 1 Diamond Chestplate + 55 Emerald | Berserkers Fury 2; Mending 1; Projectile Protection 5; Protection 5; Unbreaking 5 |
| Rune-Forged Greaves | Diamond Boots | 1 Diamond Boots + 45 Emerald | Stable Footing 1; Feather Falling 5; Mending 1; Protection 5; Unbreaking 5 |
| Stonebreaker | Diamond Pickaxe | 1 Diamond Pickaxe + 45 Emerald | Boon Of The Earth 4; Efficiency 5; Fortune 4; Mending 1; Unbreaking 5 |
| Thunder-Forged Legguards | Diamond Leggings | 1 Diamond Leggings + 55 Emerald | Rebounding 10; Mending 1; Protection 5; Unbreaking 5 |
| Timeworn Visage | Diamond Helmet | 1 Diamond Helmet + 45 Emerald | Aqua Affinity 1; Mending 1; Protection 5; Respiration 5; Unbreaking 5 |
| Treecapitator | Diamond Axe | 1 Diamond Axe + 45 Emerald | Chainsaw 1; Efficiency 10; Mending 1; Silk Touch 1; Unbreaking 5 |

Exact namespaced IDs, mod conditions, input stacks, per-tier rows and source-resource hashes are in [catalog.json](catalog.json); [named-gear.csv](named-gear.csv) is the flat 20-row export. Each named trade has `rare: true`; merchant XP is 500 except Eternal Vigilance (1000). This is merchant trade XP metadata, not a claim about player XP rewards.

## Tier behavior and separate random affix offers

Installed `dev.shadowsoffire.apotheosis.affix.trades.TieredTrade#getOffer` bytecode selects the nearest player with range `-1`, reads `WorldTier.getTier(player)`, and looks up that exact tier. If no matching tier/player exists, it delegates to `AutomaticAffixTrade`; it does not grant the named Summit item to a lower-tier player. No per-trader roll probabilities are inferred here.

The pack script `kubejs/server_scripts/tmm_wandering_trader_magic_stock.js` separately attempts to append one **random affixed** item using Apotheosis generation. That is not one of these fixed enchanted named items. Its diamond base pool includes sword, axe, pickaxe, shovel, helmet, chestplate, leggings and boots, weighted 25 each at Ascent/Summit and 5 each at Pinnacle; those numbers are weights, not percentages. Its fallback bases are iron only. Its generation-tier prices are 24 emeralds + 1 diamond (Haven), 32 + 2 (Frontier), 40 + 4 (Ascent), 56 + 8 (Summit), 64 + 12 (Pinnacle), with one purchase. Presence of this script does not prove it successfully appended offers in this session. Affixes and ordinary enchantments are distinct systems.

## Reproduction and work-slice handoff

Read-only exporter: `scripts/export_apotheosis_trader_gear.py --minecraft <instance/minecraft> --world <save-directory> --output <report-directory>`. It reads local files only and does not launch or change Minecraft. The exporter validates that all interpreted conditions are simple mod-loaded checks, records competing resources instead of choosing an unknown override order, and publishes no player identifiers, coordinates, seed, raw saves or logs.

- **Implementation-ready:** completed owner-requested catalog extraction and documentation; this branch owns only the exporter and report. No gameplay/config changes requested. Integration writer may review and merge this report into `test/full-integration`.
- **Design blockers:** none for this survey. Any trade rebalance would require a separate decision.
- **A checks:** 10 unique names / 20 tier rows, matching Summit/Pinnacle payloads, all mod conditions met, no competing resource paths, 24 saved regions / 660 chunks parsed without error; JSON/CSV readback and `git diff --check` pass.
- **B queue (optional, not required to complete this file survey):** actual runtime registry comparison at Summit/Pinnacle, if live completeness verification is desired; requires a verified capture command and a world containing this build. Not run.
- **C queue (optional):** inspect an actual naturally generated high-tier trader and confirm displayed costs/enchantments and purchase behavior. Ready only with a suitable high-tier player/trader; not run. Offline data is not gameplay acceptance.
