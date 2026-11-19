# E6b residual exact recipe inventory — 2026-11-19
PR #347; source checkpoint cf09411ef8653e7d5d6f2df2be9b3f7ed051caf8. Inventory derived from exact hash-verified [E6b run 44584477924](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/44584477924), provider jobs 135773582395, 135773582042, 135773581682, and current explicit Java catalogs. Count all bundled native recipe records, including conditional records; presence in binary is not proof active in a given world. Counts are source mappings, not proven gameplay coverage.

| Provider | Explicit mappings | Bundled records |
|---|---:|---:|
| Enchanted | 6 | 47 |
| JAWR | 18 | 57 |
| Eidolon | 15 | 24 |
| Totemic | 14 | 14 |
| Total | 53 | 142 |

Totemic source scope: all 14 performances mapped; accepted positive player music + committed noncanceled native transition, not subsequent magical effect. No spectator credit. Enchanted and JAWR remain independent namespaces. Unmapped does not mean rejected. Preserve genuine distinct rites; only routine copying/recharging listed as candidate exclusions, not new owner decisions. JAWR brazier separate (not included in 57 circle count); ordinary crafting excluded. No new award values or skill ranks.

## Enchanted

6/47 exact native records have explicit source receipt mappings. Remaining IDs (prefix `enchanted:`):

| Remaining evidence/scope | Exact recipe paths |
|---|---|
| Actual familiar bind/summon, prevent trivial loops (2) | `bind_familiar`, `summon_familiar` |
| Verify actual bound/transformed output (4) | `bind_talisman`, `bind_talisman_charged`, `broiling`, `broiling_charged` |
| Actual block/entity world effect (4) | `blight`, `fertility`, `fertility_charged`, `forest` |
| Native curse state mutation/clear (8) | `curse_clumsy`, `curse_misfortune`, `curse_overheating`, `curse_sinking`, `remove_clumsy`, `remove_misfortune`, `remove_overheating`, `remove_sinking` |
| Routine copying candidate exclusion (2) | `duplicate_waystone`, `duplicate_waystone_charged` |
| Paid commissioning or finite effect; no passive upkeep award (9) | `imprisonment`, `protection`, `protection_large`, `protection_large_waystone`, `protection_temporary`, `protection_temporary_blooded`, `protection_temporary_waystone`, `protection_waystone`, `sanctity` |
| Delayed world/weather/lightning effect (8) | `sky_wrath`, `sky_wrath_blooded`, `sky_wrath_blooded_charged`, `sky_wrath_charged`, `sky_wrath_waystone`, `sky_wrath_waystone_charged`, `total_eclipse`, `total_eclipse_charged` |
| Actual entity/block transposition (4) | `summon_entity`, `transpose_caster`, `transpose_caster_blooded`, `transpose_iron` |

## JAWR

18/57 exact native records have explicit source receipt mappings. Remaining IDs (prefix `witchery:ritual/`):

| Remaining evidence/scope | Exact recipe paths |
|---|---|
| Exact primary command committed success/state (ignore ancillary hunter updates) (22) | `befuddlement`, `change_biome`, `change_biome_cheap`, `change_biome_waystone`, `change_biome_waystone_cheap`, `curse_of_insanity`, `curse_of_misfortune`, `curse_of_overheating`, `curse_of_sinking`, `curse_of_the_undead`, `curse_of_the_wolf`, `fragility`, `hunger`, `infuse_infernal`, `infuse_light`, `infuse_otherwhere`, `infuse_overworld`, `manifestation`, `mirror_pair`, `rot`, `teleport_owner_to_waystone`, `teleport_taglock_to_waystone` |
| Native event actual state mutation (5) | `bestial_call`, `bind_familiar`, `bind_spectral_creatures`, `remove_curse`, `resurrect_familiar` |
| Special output identity/components/count (2) | `bind_hobgoblin_statue`, `raining_toad` |
| Verify actual ore/block extraction, not returned stone (6) | `blocks_below_amber`, `blocks_below_cinnabar`, `blocks_below_copper`, `blocks_below_gold`, `blocks_below_iron`, `blocks_below_zinc` |
| Routine recharge candidate exclusion (1) | `charge_attuned` |
| Finite effect or commissioned operation, no upkeep ticks (3) | `pull_mobs`, `push_mobs`, `rite_of_charging_infusion` |

## Eidolon

15/24 exact native records have explicit source receipt mappings. Remaining IDs (prefix `eidolon_repraised:rituals/`):

| Remaining evidence/scope | Exact recipe paths |
|---|---|
| Native timed effect/commission, actual state and owner (4) | `absorption`, `allure`, `deceit`, `repelling` |
| Special item/world result (3) | `crystal`, `purify`, `ritual_locate_catacombs` |
| Routine recharge candidate exclusion (2) | `recharging_chill`, `recharging_soulfire` |


Next source passes: specific native output/command success and state witnesses (reuse common actor/operation lifecycle), then delayed/commissioned effects. Do not give generic XP on premature native start, void command call, unchecked spawn, proximity, or passive upkeep. All ten CI checks passed at 221b939; cf09411 expanded exact audit passed all four matrix jobs [44588273596](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/44588273596). Full installed mixin weave, disposable gameplay tests, pack assembly and smoke are not authorized or performed. No claim of complete E6b source or readiness for live test.
