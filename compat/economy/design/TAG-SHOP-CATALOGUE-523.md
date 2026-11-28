# Tag-driven shop catalogue (#523)

Owner-approved 80% target, 75–85% efficiency, 64 EMC coin. Distinct review boundary from toolbar #834: this is an offline candidate generator plus read-only extension to the existing integrated #47 census. No active trade, wallet, quest or price changes, no new mod pins. MC 1.21.1 / NeoForge 21.1.265 / Java 21 / Q Shop 1.8.5 / ProjectE 1.1.0.

## Usage after reviewed integration and authorized disposable-world build

1. After ProjectE and tags finish loading, run existing permission-3 command `/tmm_economy audit_emc <40hex_sha>`. The augmented snapshot includes shop-items.json with real resolved item tags, default-stack limits, food and spawn-egg facts and a SHA-256 digest in metadata. No new command or automatic writes.
2. Copy the completed snapshot directory to the repository tooling context. Run `python3 scripts/generate_adventurer_shop.py <snapshot-dir> --expected-source-sha <40hex_sha> --output-dir <new-review-dir>`. The SHA is operator-labeled, not attested; verify the actual build manifest.
3. Review shop-audit.json and shop-candidate.json. Neither file is installed automatically. No overwrite or in-place live edits. A subsequent cutover PR must reconcile active manifest/catalogue validators, legacy quest IDs and existing worlds.

Candidate tags reside in the Economy JAR resources (not Packwiz-indexed), using optional conventional references and verified vanilla IDs. Broad tags are selection candidates only. Modded ore/raw/food acquisitions need explicit reviewed eligibility tags; no default Overworld assumption. Separate dimensional stages proposed, but no stage-grant implementation or verified personal achievement bridge yet. Gated entries are marked stage_bridge_pending. No unreviewed chest loot, no spawn eggs; seeds/saplings excluded on purchase. Generic food component is a candidate, not sufficient provenance. SELL accepts tagged ordinary ingots/gems/animal/plant products. Component-sensitive namespaces held for exact stack valuation and initialization/matching research; do not sell default Apotheosis gem or relic placeholders. Permanent iron gear pilot included as approved; randomized enchanted diamond stock remains separate.

The deterministic UUID uses direction and item ID (stable across price or category changes). Exact rational bounds, actual default-stack cap and positive integer prices; no quote when tolerance cannot be met. Candidate retains only the existing native 1:1 coin exchange unchanged; prior merchandise must be mapped/reviewed before installing. Old FTB sales remain untouched, including premium tiers until cutover. No speculative EMC anti-exploit tests.

## Evidence and next actions

- Type A: Python fixture tests for bounds, rounding, stable IDs, overlap, exclusions, component holds and dimension requirements; exact CI and Java compile still required.
- Type B after authorized build: capture augmented #47 export, generate actual catalogue and inspect all hold statuses; verify source manifest and exact snapshot.
- Type C: later cutover only: native BUY/SELL on normal players, two personal wallets, exact stack counts, gates, reopen/reload, initialized relics/variants and existing-world migration. No new full build authorized.
