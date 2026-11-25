# Food provenance census — installed source inventory handoff (2026-11-22)

Refs #444, #463, Progression food handler #539. **Source-only first census pass. Not a complete food registry, not a generated runtime food map, and not gameplay acceptance.**

## What we actually know at this source snapshot

- Canonical mod inventory is in index.toml + indexed mods/*.pw.toml, not every desired entry in manifest/mods.json. Actual JAR contents and runtime registered items must be verified independently; a mod entry by itself is not food evidence.
- Checked-in legacy Dragon Survival 2.0.74 map, at kubejs/data/dragonsurvival/data_maps/dragonsurvival/dragon_species/diet_entries.json, has **319 species-specific selectors**: Cave 35 (34 literal items + 1 tag), Forest 120 (109 literals + 4 tags + 7 expressions), Sea 164 (163 literals + 1 tag). Across species this is **294 unique literal item IDs**, six tag selectors, and seven legacy Pam's regex selectors. The regex and old diet permissions are NOT proof that the item exists in the current installed registry, has a FoodProperties component, or satisfies the NEW raw-first species policy.
- The earlier exact-source pilot is **nine item entries**, **eight source-resolved** in the standalone provenance graph, with Suspicious Stew left unresolved. The separate numeric pilot yields **three** candidate species/item rows: raw beef, raw Aquaculture fish fillet, raw carrot.
- The existing source pilot covers only two recipe descriptions. It is neither the full native recipe graph nor full edible registry. PR #539's new runtime provenance data directory currently ships no real item evidence, intentionally.
- The pack also carries KubeJS dynamic recipe replacement, notably dragon_seasoned_fish.js (widens the raw-salmon ingredient) and dragon_diets.js (old Forest/Pam name rules). Neither may be silently treated as a resolved or authoritative native recipe after transformation.

## Read-only census tool and output

Run from a checkout with Python 3.12:

    python3 scripts/food_provenance_census.py --repo . --output /tmp/tmm-food-census.json

This inventories **only installed indexed Packwiz mod metadata**, checks metadata hashes, identifies priority food mods, scans all checked-in KubeJS JSON recipes/item tags/loot tables, lists dynamic JS files as not statically evaluated, counts all existing Dragon Survival diet selectors (literal/tag/expression), and unions them with the source pilot and discovered recipe outputs as *candidate IDs, not confirmed edibles*. It publishes an explicit unresolved queue and leaves the actual edible registry denominator **null**.

To add pinned JAR source coverage, put exact, separately acquired publisher JARs in a local staging directory, named exactly as their metadata specifies. Do not commit them:

    python3 scripts/food_provenance_census.py --repo . --jars /path/to/pinned-jars --output /tmp/tmm-food-census-jars.json

For each **indexed** mod, the scan verifies the recorded SHA1/SHA256/SHA512 of the exact file before reading its ZIP entries. Unverified, missing or hash-mismatched JARs are not scanned. From verified ZIPs it reads data/*/{recipe,recipes,tags/item,tags/items,loot_table,loot_tables} JSON. Exact recipe result and output count are captured only when a supported schema exists; otherwise the recipe remains visibly unresolved. Ingredient tag references and multiple output-recipe routes remain independently auditable. This path **does not execute mods, inspect Java registries or emulate tags**. Dynamic KubeJS scripts remain an explicit separate reconciliation queue.

An optional *same exact build* registry export permits the first true denominator:

    python3 scripts/food_provenance_census.py --repo . --registry-export /path/to/registry.json --output /tmp/tmm-food-census-registry.json

Export schema 1 must include a full 40-character integration build SHA, a boolean complete flag, and **all** item-registry rows, each with id, has_food_component (boolean), and native_only_edible (boolean). If complete=false, observed edible IDs are listed but the denominator remains null. A complete export must come from the assembled client/server runtime, not inferred from item names, model JSON, recipes, or the legacy Dragon Survival diet map. Obtaining this export is a separate authorized QA/build task.

The dedicated workflow verify-food-provenance-census.yml runs only offline Python fixtures and a read-only checkout census, then uploads a JSON audit artifact. It does **not** download JARs, start NeoForge, assemble the pack, modify Packwiz or publish a datapack.

## Priority source-census queue (pinned artifacts)

1. Core ingredients: Minecraft 1.21.1 vanilla, Farmer's Delight 1.3.4 (cutting boards, outputs/yields), Aquaculture 2 2.7.22 and Aquaculture Delight 1.2.0 (raw/cooked fish, sushi, fish slices).
2. Wide recipe fanout: Pam's HarvestCraft 2 Core 1.0.4 / Extended 1.0.0 / Crops 1.0.9 / Trees 1.0.9 (jellies, produce, juices, alternative ingredients, reused tools, output counts).
3. Distinct habitats/processing: My Nether's Delight 1.10.4.1 and Nether Depths Upgrade 3.3 (Nether fish, cuts, cooked dishes, mixed origins).
4. Custom consumables: Dragon Survival 2.0.74 (native-only edibles, special use handlers, feeding values), Brewin' And Chewin' 4.5.0, Cocktails Delight 1.1.12, Occult Cooking 1.0.1, Brewery and HerbalBrews (food/drink classification only; do not override potions without a real FOOD component).
5. Secondary mods revealed by the indexed-JAR data scan, source-tag graph, KubeJS scripts and actual live item registry. Do NOT stop at the predefined shortlist.

## Required follow-on engineering

- **Ready / next:** Supply exact verified JARs (or equivalent pinned source receipts) for priority mods, run the tool, inspect unresolved JSON schemas and registered items. Record full ingredient alternatives, ingredient counts, output counts and tools. Do not mistake an item appearing in a recipe for a registered edible.
- **Ready / next:** Reconcile every KubeJS recipe mutation and the actual effective tag graph after data-pack merge. Add the generated source/recipe provenance graph from #463, converted to only RAW/COOKED/CHARRED and new plant/fungus/marine-plant families. No per-item nutrition authoring.
- **Ready / next:** Emit a single PR #539-compatible food_sources source receipt only where source units, stage, all viable recipe routes and preferred shares agree. If two valid routes for the same item differ materially, do not emit a global record; keep native food handling or pursue separately approved stack-specific provenance.
- **Ready / next:** Generate real shared item tags with correct merge/replace semantics. Test recipe loops/duplicate value, fractional source units, fish slices, mince, juice, mixed sushi, charred food, magical/costed items and native-only food.
- **Integration-owned / not started:** Replace old Dragon Survival map and KubeJS name filters coherently, reconcile Packwiz index/hash, run requested B/C live tests. No shared integration branch writes from the census agent.

## Distinct remaining tests

**A (offline, implemented):** Synthetic pinned SHA1 JAR, bad hash rejection, exact recipe outputs and tag refs, KubeJS dynamic script exclusion, old map selector census, absent/partial registry denominator, duplicate registry IDs, unsupported schema holds. CI publishes the full read-only report.

**A (pending):** Actual 1.21.1 pinned mod JAR census, effective KubeJS recipe/tag audit, all affected mod registries and source-unit graph; deduplicate effects of tags and recipe cycles.

**B (awaiting authorized live build):** Same exact assembled build item registry including FOOD components plus Dragon Survival native-only edibles; verify every registered edible in coverage report.

**C (awaiting authorized gameplay):** Representative recipes, cuts, off-diet/mixed dishes, actual use effects, client parity, reload, relog and multiplayer.

## Blockers/decisions

No new owner-level mechanical decision is required to generate the census. The principal blockers are **missing verified pinned source bytes**, unknown final registry/export, and dynamic recipe/tag behavior. Those are data and QA dependencies, not permission to guess. No full smoke, Prism build, or Integration Assembly is authorized by this source-only work.
