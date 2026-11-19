# E6b residual exact recipe inventory — 2026-11-19
PR #347, source checkpoint 5b0fa61b76168f60ff0e95e6a031852222030030. Exact bundled recipe IDs from hash-verified [run 44584477924](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/44584477924); focused outcome census [44590603836](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/44590603836). Compare current Java catalog entries, not prior hand-counts. Conditional recipes counted as bundled records, not guaranteed available. Source mappings are not runtime acceptance.

| Provider | Mapped | Routine exclusions | Remaining | Bundled |
|---|---:|---:|---:|---:|
| Enchanted | 16 | 0 | 31 | 47 |
| JAWR circle | 34 | 0 | 23 | 57 |
| Eidolon | 22 | 2 | 0 | 24 |
| Totemic | 14 | 0 | 0 | 14 |
| Total | 86 | 2 | 54 | 142 |

Eidolon: both wand-recharging rites (recharging_chill, recharging_soulfire) excluded under existing routine maintenance policy; engineering classification, subject to owner's review. All other 22 mapped; three perpetual rites award on first verified effect of manual commissioning, not effect pulses. Totemic: 14/14 mapped, accepted actual performance independent of subsequent effect. JAWR brazier separate from 57 circle recipes; still unimplemented. Keep Enchanted and JAWR independent, preserve genuine native identities. The remaining copying and recharging candidates below are not new exclusions; require review. All reward bands unchanged.

| Provider | Remaining outcome family | Native paths |
|---|---|---|
| Enchanted | Actual familiar bind/summon, prevent trivial loops (2) | `bind_familiar`, `summon_familiar` |
| Enchanted | Verify actual bound/transformed output (2) | `broiling`, `broiling_charged` |
| Enchanted | Actual block/entity world effect (4) | `blight`, `fertility`, `fertility_charged`, `forest` |
| Enchanted | Routine copying candidate exclusion (2) | `duplicate_waystone`, `duplicate_waystone_charged` |
| Enchanted | Paid commissioning or finite effect; no passive upkeep award (9) | `imprisonment`, `protection`, `protection_large`, `protection_large_waystone`, `protection_temporary`, `protection_temporary_blooded`, `protection_temporary_waystone`, `protection_waystone`, `sanctity` |
| Enchanted | Delayed world/weather/lightning effect (8) | `sky_wrath`, `sky_wrath_blooded`, `sky_wrath_blooded_charged`, `sky_wrath_charged`, `sky_wrath_waystone`, `sky_wrath_waystone_charged`, `total_eclipse`, `total_eclipse_charged` |
| Enchanted | Actual entity/block transposition (4) | `summon_entity`, `transpose_caster`, `transpose_caster_blooded`, `transpose_iron` |
| JAWR | Exact primary command committed success/state (ignore ancillary hunter updates) (9) | `change_biome`, `change_biome_cheap`, `change_biome_waystone`, `change_biome_waystone_cheap`, `curse_of_the_wolf`, `mirror_pair`, `rot`, `teleport_owner_to_waystone`, `teleport_taglock_to_waystone` |
| JAWR | Native event actual state mutation (4) | `bestial_call`, `bind_familiar`, `bind_spectral_creatures`, `resurrect_familiar` |
| JAWR | Verify actual ore/block extraction, not returned stone (6) | `blocks_below_amber`, `blocks_below_cinnabar`, `blocks_below_copper`, `blocks_below_gold`, `blocks_below_iron`, `blocks_below_zinc` |
| JAWR | Routine recharge candidate exclusion (1) | `charge_attuned` |
| JAWR | Finite effect or commissioned operation, no upkeep ticks (3) | `pull_mobs`, `push_mobs`, `rite_of_charging_infusion` |

Next: prioritize exact native familiar bind/resurrection and summon states, Enchanted delayed effect receipts using the existing persisted Rite state, JAWR custom command state and extraction outcomes, then other finite/commissioning effects. No generic award for void dispatch, start, unchecked spawn, proximity, or maintenance. Update tests and exact mappings rather than repeat broad surveys. Prior 8da46fe (Eidolon) and 3987f06 (JAWR) all ten workflows passed. Enchanted source head 5b0fa61 CI pending at inventory creation. No runtime Mixin weave, gameplay, pack build/merge/activation or live-test readiness claim.
