# Dragon food redesign — provenance-engine Type-A handoff (2026-11-21)

**Owner intent:** [#444](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/444). New **Cave terrestrial animal/raw**, **Sea marine animal + edible sea plants**, **Forest strict plant/fungus** physiology; uncooked preferred, cooked less, charred least; **nonthermal preparation is not cooking**; mixed dishes remain edible with an off-diet penalty determined later. Existing Dragon Survival data map and older [food candidate PR #439](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/439) express a superseded native policy and MUST NOT be promoted from this PR.

## Implemented in this isolated source-only slice

- `scripts/dragon_food_provenance.py` is a fail-closed **source evidence and preparation classifier**, not a runtime diet engine. It accepts version-1 source inventories of exact edible IDs, manually evidenced biological roots, recipe outputs/ingredients, source-evidenced tag expansions and explicit reusable tool IDs. No item-display-name substring matching.
- Biological `family` is one of **plant_fungus, terrestrial_animal, marine_animal, neutral**; marine seaweed is plant/fungus plus an *independent* marine habitat marker, not incorrectly classified as fish. This allows eventual Sea acceptance without inventing a fourth biological family. Preparation is **raw, minimally_processed, cooked, heavily_cooked, charred**. A raw plant juice can remain minimally processed; ordinary crafting is NOT automatically treated as cooking. Actual furnace/smoker/campfire/blast recipe types imply cooking, otherwise preparation requires explicit source metadata.
- Supports direct items, exact sourced tags, nested `any_of` ingredient alternatives, counts for materiality, multiple recipes producing one ID, explicitly declared reusable Pam tools, cycle detection, missing ingredient/tag reporting and a hard cap on alternative explosion. **Unresolved or partly unresolved branches stay visible; they are not silently accepted as fully classified.**
- Output is deterministic, SHA-256-bound to the full inventory input and records **every edible ID supplied** with each available composition and source chain, unknown/partial issues and alternative provenance. Numeric nutrition, saturation, special effects and real Dragon Survival `diet_entries.json` are **never generated** by this pilot. This is essential: the owner has approved *inequalities* but not final values, and JAR/registry census is incomplete.
- `docs/qa/candidates/dragon-food-source-pilot-v1.json` provides **nine grounded small pilot IDs** (vanilla beef, steak, carrot, dried kelp, seagrass, Aquaculture raw fish fillet and sushi, native Charred Meat, and an intentionally unclassified suspicious stew). The source-backed Aquaculture sushi recipe uses **raw fish fillet + seagrass**, no rice. Native Cave charred-meat current value 6/3 is evidence, **not** assigned to a new diet. The pilot explicitly marks seagrass/Charred Meat as dragon-only edibles in this classification snapshot; it does not mutate common FoodComponents.
- `tests/test_dragon_food_provenance.py` has **14 offline regressions** for raw-to-cooked, separate charred stage, raw grinding, marine plant distinction, mixed provenance, tag alternatives, reusable-tool exclusion, count propagation, unknown branches, cycles, unsupported preparation, >64 alternatives, duplicate schema and stable output. The dedicated workflow **Dragon food provenance pilot (NO GAME BOOT)** generates and uploads the JSON report as a run artifact, with a hard check that runtime maps and food values were not emitted.

### Explicitly unimplemented / requires separate source census

1. **Exact pinned mod/JAR census:** inventory actual registered edible IDs and food components, all available Pam's (all four), Farmer's Delight, Aquaculture, new approved Huaxia/Crispy/Dungeon's/Ender's/RunicLib, magic/food add-ons and Dragon Survival special foods. Verify official JAR hashes and binary versions at integration head. This nine-ID pilot is **not** evidence of registered 100% coverage. The CLI intentionally only accepts `scope: "pilot"` until a separately audited full census is supported.
2. **Exact recipes:** extract recipe alternatives, tags, reusable versus consumed tools, preparation method and native/cooking tools from those actual JAR resources. Do not treat all crafting-grid tool recipes as cooking, nor presume that a pot/skillet/mixing bowl implies the same treatment. Source-backed recipe provenance needs per-recipe links/IDs and effective datapack/JEI verification.
3. **Ambiguous ingredients:** eggs/dairy, oils, broth/stock alternatives, soy meat/fish, rice in mixed sushi, ghast/monster oddities and inedible vanilla sea plants require explicit review. The engine tracks alternative families and marine habitat so they can be classified without pretending a particular recipe was actually used for an already crafted stack. A favorable producible variant is an *owner-approved policy* to implement later, not automatic at this level.
4. **Species output and numerical tuning:** final per-species food values, saturation **modifiers** rather than flat saturation points, off-diet penalties, magical/mineral exceptions and retain-effects semantics; output **immutable reviewed candidate** before a separate integration writer promotes it. Preserve special reusable food item effects; do not double-award hunger/durability. Existing canonical indexed data map, `scripts/verify_dragon_diets.py` and KubeJS tags must stay unchanged until coherent replacement source/tests are ready.
5. **“Dragons Do It Raw” info quest:** approved title and gameplay guidance, but separate quest writer owns FTB Quests authoring and reward/claim standards.

### Design blockers (do not conceal engineering-ready slices)

- Final raw/cooked/heavily/charred **numeric curves**, especially raw beef >= legacy charred 6/3 and raw > cooked > charred > 0, are **not locked numerically**.
- Mixed-dish penalty weighting and completely off-diet pure-food treatment need an owner decision.
- Future new eternal-food variants remain separate owner review.

### In-game B/C queue — not run or authorized here

- [ ] **B, awaiting inclusion/build:** exact 1.21.1 data map loads, registered food IDs present, no pack conflicting selectors, source JAR parity.
- [ ] **C:** Cave raw beef > steak > charred meat by both nutrition and saturation; Sea raw marine seafood and edible kelp/seagrass; Forest plants/mushrooms, animal ingredients off-diet; meat substitutes and monster oddities correctly classified.
- [ ] **C:** raw-ground beef retains raw benefits, cooked preparation loses value, fish + rice sushi remains edible at reduced value, seagrass-only Aquaculture sushi is a favored Sea example, no false blanket broth penalty, Pam tool processing semantics.
- [ ] **C:** vanilla humans/Monk/Hunter unaffected, magical/reusable food keeps status effects and consumption, clean world/relog/multiplayer/no exploits.

## Review commands / boundaries

```bash
python3 -m unittest discover -s tests -p 'test_dragon_food_provenance.py' -v
python3 scripts/dragon_food_provenance.py \
  --input docs/qa/candidates/dragon-food-source-pilot-v1.json \
  --output /tmp/dragon-food-pilot-report-v1.json
```

**Ownership:** Isolated `feature/dragon-food-provenance-engine-444` from `test/full-integration@077a34d8706dba51b10ad65dde05a7d34cfb5c96`. This writer owns new source classifier, pilot, tests and separate CI; PR #439 retains its **food mod candidate** work; the integration writer owns pinned mods and all shared `index.toml`, `pack.toml`, `manifest/mods.json`, real `kubejs/data/...` map and source locks. **No full integration assembly, game boot, server smoke, Prism package or client QA performed or requested.**
