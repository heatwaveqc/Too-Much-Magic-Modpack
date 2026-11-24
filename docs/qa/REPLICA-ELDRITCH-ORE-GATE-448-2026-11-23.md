# Replica Ore: exact-source gate promotion — #448

**Promotion candidate; not gameplay accepted.** Source-only cherry-pick from
`feature/magical-industry-config-batch3` at full SHA `5fba8450fb96538945c6e65ce97837e133ef8681`,
original PR [#339](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/339).
Base `test/full-integration@a408f46b217f4d504ca9fca889143d51ca7da2f7`.

## Included
- `kubejs/data/replication/neoforge/biome_modifier/replica_ore.json`: same-ID override of Replication 1.2.7's Overworld ore biome modifier with `neoforge:none`. Suppresses native natural generation for **new chunks only**, preserving the existing ore registry, drops and smelting.
- `kubejs/data/tmm/recipe/miner/replica_ore_eldritch.json`: adds `tmm:miner/replica_ore_eldritch` with exclusively `occultism:miners/eldritch`, one `replication:deepslate_replica_ore`, native weighted result 90 (donor's provisional balance). No earlier miner routes added.
- Packwiz source-index and pack hash chain updated. This change does not touch mod pins, shared Progression, or any other magical-industry paths.

## Checks and limitations
- Type A: `python3 -m unittest discover -s tests -p 'test_replica_eldritch_gate.py' -v`, `python3 scripts/validate.py`, and `python3 scripts/validate_integration_sources.py` on a clean canonical-LF checkout; repository CI and Integration Preflight must be green before shared promotion.
- Type C, **next separately owner-authorized Minecraft test**: fresh Overworld chunks must contain no *naturally generated* Replica Ore; the Eldritch Ancient Miner must produce the ore at its normal weighted chance; ore must still yield raw Replica and smelt to Replica Ingots; confirm ordinary miners do not acquire ore from this recipe.
- Does **not** establish exclusion of other modded generation routes, retroactive deletion from explored terrain, or in-world acceptance. All effective stonecutter-output Replication exclusions are separate #561/#565 work; this PR does not import them.
- No Integration Assembly, server smoke, Prism package, client launch or gameplay test is authorized or run by this PR.
