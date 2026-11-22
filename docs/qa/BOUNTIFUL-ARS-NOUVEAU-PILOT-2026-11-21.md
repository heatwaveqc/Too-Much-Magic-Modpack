# Ars Nouveau Bountiful pilot handoff — issue #365

Status: **SOURCE READY FOR REVIEW; NOT INTEGRATED OR GAMEPLAY ACCEPTED**.

## Frozen source and ownership

- Feature base: `test/full-integration` at `077a34d8706dba51b10ad65dde05a7d34cfb5c96`.
- Existing three-decree compiler/runtime provenance remains the merged #366/#378 baseline. This follow-up changes only the compiler-owned `compat/bountiful/live-pilot` source overlay, its review ledger, tests, and this QA handoff.
- The integration writer must copy/reconcile the four new Ars runtime JSON files into the baseline `config/` and `kubejs/` trees and update Packwiz hashes. This feature branch intentionally does not edit `index.toml`, `pack.toml`, `manifest/integration-sources.json`, or shared installed runtime paths.

## Exact bounded implementation

- Adds native `tmm_ars_nouveau` decree, objective pool, reward pool, localization, and the approved temporary shapeless recipe: one paper + one `ars_nouveau:source_gem` -> one rank-one native `bountiful:decree` carrying `bountiful:decree_data.ids=["tmm_ars_nouveau"]`.
- Rewards are `ars_nouveau:source_gem` (2–4, COMMON, available from reputation -30) and `ars_nouveau:air_essence` (1–2, UNCOMMON, reputation 0).
- Objectives are 4–64 `minecraft:lapis_lazuli` and one completed `tmm:mob_ars_wilden_hunter` Gateway through native `gateways:finish_gateway` criteria. A modded Ars delivery was not selected because every vetted Ars material in this bounded family is either one of the intended payout items or zero-mapped in the frozen export; using it would create a self-trade or replace an evidenced value with an invented one.
- The frozen November 18 ProjectE export reports both Ars rewards as `zero_api_value`. The review ledger records them as explicit unpriced rewards; Bountiful matching uses the native rarity effort floors (64/256 EMC-equivalent) instead of inventing ProjectE values. Lapis remains positively mapped at 864 EMC and the Gateway retains the reviewed 1024 effort budget.
- Compiler now fails closed when a zero-EMC reward lacks explicit review evidence. It still requires positive material objective values and feasible native matching at every eligible board reputation.

## Type A checks

- Run `python -m unittest tests.test_bountiful_contracts tests.test_bountiful_live_pilot tests.test_bountiful_distribution`.
- Run `python scripts/build_bountiful_live_pilot.py --verify`.
- Run `python scripts/validate_integration_sources.py` from a clean canonical-LF checkout.
- Inspect the feature diff and confirm no root runtime config, Packwiz index, mod pin, first-party JAR, or integration source lock changed.

## Remaining Type B/C queue

- **B — awaiting authorized assembled candidate:** verify the exact Bountiful beta.2 component codec loads the rank-one Ars decree, only the four TMM decree families appear, and the Wilden Hunter criterion advances only after full Gateway completion.
- **C — awaiting authorized gameplay:** craft the Ars decree, inspect tooltip/pool selection, complete lapis delivery and Wilden Hunter contracts, redeem once, refresh the board, relog, and confirm Source Gem versus Air Essence COMMON/UNCOMMON availability and quantities.
- **C — economy follow-up:** confirm actual same-candidate ProjectE values remain zero or reconcile the final positive mappings before survival promotion; check no reward/hand-in loop, duplicated claim, or unintended default Bountiful pool.

No Integration Assembly, server/client boot, Prism package, or gameplay test was run by this source change.
