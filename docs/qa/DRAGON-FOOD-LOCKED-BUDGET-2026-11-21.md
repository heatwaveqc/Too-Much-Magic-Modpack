# Dragon food locked nutrition budget — #444

**Status:** Source-only implementation slice. NOT an active Dragon Survival diet map or installed gameplay change.

**Base:** test/full-integration at 2373d50732866e460dc9e9454f7187f9045e1cdc, after the #447 provenance engine was selected in integration #457. This branch owns only its new Python planner, tests, candidate policy, CI workflow and handoff. No KubeJS, Packwiz, manifest, source lock or runtime food component is edited.

## Code-ready and implementation slices

- [x] **Owner-locked dragon-only preferred raw values:** exactly 6 nutrition / 2.0 saturation modifier (24 theoretical saturation) for directly sourced Cave terrestrial meat, Sea raw fish, and Forest terrestrial produce. The limited nine-item pilot currently generates THREE candidate rows: vanilla raw beef, Aquaculture raw fish fillet and vanilla carrot. This is not a census or wildcard/tag override, and never alters ordinary human or other form food values.
- [x] **Source-aware Cutting Board ratio:** per-cut nutrition is 6 times (native cut hunger / native whole-source hunger), with an audited recipe ID, source, native ratio and exact result count. No invented rounding. Synthetic fixtures verify raw cod/salmon/cabbage 6 -> two 3-hunger pieces; raw beef 6 -> two 4-hunger minced pieces (8 total). No whole-source bonus applied independently to each piece.
- [x] **Single-output, all-preferred, nonthermal aggregation:** sum consumed nutrition, keep 2.0 modifier. Fixture two raw oranges -> one juice = 12/2.0 (48 theoretical saturation). No blanket assumption that all crafting or all Pam tools are nonthermal; reused tools do not contribute nutrition.
- [x] **Fail-closed behavior:** unresolved/ambiguous provenance, off-diet sources, unapproved sea-plant numeric tuning, cooking disguised as nonthermal, unsourced cutting yields, tag/alternative recipes, fractional portion results, duplicated candidates and missing recipe inputs yield explicit holds, never inferred nutrition.
- [x] **Static regression suite and scoped CI:** source-only candidate report includes source inventory SHA-256, held decisions and unresolved inventory. No runtime map generation and no global Minecraft food components changed.
- [ ] **Next source census:** exact pinned food JAR registries and native recipes for Pam's, Farmer's Delight, Aquaculture, Huaxia/Crispy/Dungeon's/Ender's, and other approved sources. Populate actual result counts and native nutrition receipts into the inventory before enabling real cut/juice candidates; synthetic tests prove the algorithm, not installed recipe identities.
- [ ] **Next implementation:** source-derived complete per-species Dragon Survival candidate preserving magical and zero-zero custom-use properties, effects, and other dietary authorities. The designated integration writer alone may promote it and update the indexed Packwiz hash chain. Add the information quest “Dragons Do It Raw” in the quest workstream.

## Remaining design holds

Exact numerical cooking/heavy-charring penalties; off-family mixed-food reduction; wholly off-diet item treatment; numerical rewards for fungi/kelp/seagrass; Eternal food replacements. None blocks this locked first slice.

## B/C in-game testing queue

- [ ] B: Confirm selected Dragon Survival map format, registry/tag expansion and preservation of native item/effect special cases after integration.
- [ ] C: Confirm raw 6/2.0, native cut ratios, juice budget, cooked/charred decreases after coefficient approval, marine/Nether raw-fish and rice mix handling, plus magical food special-use correctness.
- [ ] C: Prove regular humans/Monk/Hunter unaltered; no cross-form bypass, creative exploit, recursive nutrient increase, save/relog or multiplayer regressions.

Static/quick checks are Type A only. No Integration Assembly, NeoForge server smoke, Prism package, full client test or gameplay PASS is claimed. Do not promote this pilot as complete #444.
