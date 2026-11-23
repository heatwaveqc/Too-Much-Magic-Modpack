# Replication stonecutter exclusions — source-ready runtime candidate

Refs issue #448 and effective recipe catalog PR #561.

## Intended runtime behavior

- Canonical `tmm:replication/stonecutter_outputs` tag contains all 1,720 unique stonecutting outputs in the frozen November 18 effective RecipeManager capture. The three exact native `replication:` prevention tags reference it additively.
- This includes items with alternate crafting methods: 1,545 of the 1,720 outputs.
- The four native anchored outputs are priced through upstream `replication:matter_values/c/tags/stones`, not direct item recipes. That anchor normally gives `c:stones` two earth. This candidate overrides the same recipe ID using `neoforge:difference` to preserve every *nonexcluded* stone's original 2-earth anchor.
- Upstream `ReplicationTask.isValid()` and `storeMatterStacksFor()` re-query live matter valuation. With the native `skip_calculation` tag and this explicit-anchor correction, old stored patterns should no longer start printing excluded outputs; actual task behavior still requires runtime QA.
- Ordinary stonecutter recipes are preserved, as is Replication of non-stonecutter base items and boss loot.

## Reproduction

Run the existing `scripts/build_replication_stonecutter_catalog.py` against an authorized effective recipe capture, then:

    python scripts/build_replication_stonecutter_runtime.py --catalog-dir docs/research/replication-stonecutter-catalog-2026-11-23 --output-root kubejs/data
    python -m unittest tests.test_replication_stonecutter_catalog tests.test_replication_stonecutter_runtime -v

## Integration/QA holds

1. Regenerate the *effective* catalog using the current installed mod/datapack/KubeJS recipe roster. This candidate was derived from the November 18 capture, not the later November 22 roster. The generator rejects newly observed explicit matter anchor kinds so they cannot silently bypass the tags.
2. Integration writer must compare current native `stones` anchor and confirm NeoForge 21.1.265 custom `neoforge:difference` ingredient decoding; then own the Packwiz `index.toml` and `pack.toml` hashes and source manifest. Feature branch must not edit shared integration locks.
3. Type A only here. After authorized build: verify cut copper, vanilla stairs, modded stones and a previously learned blueprint cannot be scanned, disintegrated or printed; unmodified raw stones and Nether Star/Totem still replicate; verify loaded `c:stones` prices and no unexpected ID/tag omissions.
4. An already in-flight task during a live `/reload` is not proven impossible; the pre-stable pack requires no old-world migration. Do not declare completed in-game behavior from source tests.

No full Integration Assembly, server boot, client, or Prism test was run. No mod version, worldgen, stonecutter recipe, boss-drop or miner gate changes.
