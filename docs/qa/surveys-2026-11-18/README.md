# Installed-world inventory surveys — 2026-11-18

Owner requested the surveys and opened the existing singleplayer world for command captures. This is a read-only registry/economy census, not gameplay acceptance, an integration assembly, or a new smoke/build.

| Captured result | Count |
| --- | ---: |
| Registered items / namespaces | 23,681 / 106 |
| Positive default-stack EMC | 5,780 |
| Zero default-stack EMC, approved glass panes | 17 |
| Other zero default-stack EMC | 17,884 |
| Unpriced observed recipe outputs | 9,430 |
| Remaining root-or-special candidates | 8,454 |
| Component-aware mapped entries | 6,254 |
| Entity types / structures / recipes | 1,763 / 304 / 24,857 |
| Item / entity / recipe read errors | 0 / 0 / 0 |

Captured 2026-11-18 12:19:31 America/Phoenix, ProjectE map load index 4. Raw capture SHA-256 is in summary.json. All item IDs are unique. Coverage is measured on every registered default stack; component-variant completeness is a separate gate.

## Build identity and method

Installed BUILD-MANIFEST integration commit: `f7aeb56a0f7b9c5957f922f5a397037f25207c76`; baseline `a81723733b4437770f61f566afe583343ccccff9`; declared 205 pinned mods; installed directory contains 209 JARs including first-party artifacts. `installed-artifacts.json` records SHA-256 for every actual JAR. This report's source branch starts at integration `2f662b9e8b73cee47a1e23bf6d1f3510dd41d8ae`; that source HEAD is **not** the installed build identity.

The temporary operator-only `scripts/runtime_survey_probe.js` was installed locally in KubeJS server scripts. Owner ran `/reload` and `/tmm_survey_capture`. It enumerates `BuiltInRegistries.ITEM` and `ENTITY_TYPE`, the live server's structure registry and RecipeManager, and ProjectE PE1.1.0 `EMCMappingHandler.getMappedItems/getStoredEmcValue` plus explicit `IEMCProxy.getValue(ItemStack)` default-stack lookups. Long EMC values are serialized as decimal strings. Reload remaps EMC; the exported load index describes the captured map. Failed preliminary captures are excluded from the published dataset.

## Files and consumers

- `items.csv`: every registered item, default-stack EMC observation, explicit stored-entry presence, resolved item tags, live recipe-output references and static loot references. Serves #47/#326/#322 and food/material/tag inventories. Zero API value does not itself identify an economic root or prove blacklist status.
- `runtime-capture.json.gz`: complete successful capture, including component-aware ProjectE mapped-entry keys, entity categories, structures and effective recipe observations. Decompress with standard gzip tools. The compressed representation avoids repeating large alternative ingredient sets in an oversized Git blob.
- `projecte-mapped-entries.json`: all mapped component-aware keys and observed stored EMC, not a hand-authored override file.
- `entities.json`: actual entity IDs, native descriptions and categories for #274/#324. Categories are not proof of living status, boss status, blood eligibility or actual spawn provenance.
- `structures.json`: same-build live structure IDs for Ars locator compiler input. No recipes are generated or installed by this survey.
- `static-loot-item-references.csv`: factual item references from native JAR and installed KubeJS loot resources. This is supplementary source evidence, not a resolved live loot simulation.
- `summary.json`: counts, namespace EMC coverage and capture/parse limitations.
- `quest-registry-comparison.json`: 232 distinct installed quest item references, 592 occurrences, and three serialized missing-item placeholders for two unavailable IDs.

## Interpretation and remaining development work

Only the 17 glass-pane IDs in the existing EMC coverage policy are classified as approved exceptions. Every other unpriced item remains visible. `root_or_special_candidate` means no output was observed through the generic Recipe interface; it is **not** a proposed manual anchor. Magical/custom recipes may use different outputs or hide them from that interface. `recipe_output_unpriced` identifies a recipe output needing upstream/material/mapper investigation, not an automatic price assignment. Air, debug items, spawn eggs, charges, containers and component variants are not silently removed.

#326's final pricing decision queue remains PARTIAL: intrinsic acquisition yields/rarity/gates, resource costs, catalysts/returned containers, conditional recipes, strongly connected/rootless dependencies, Theurgy sulfur coverage, blacklist attribution and component-sensitive transactions require follow-through. Generic Ingredient alternatives and default result stacks are insufficient to prove cost conservation. No prices, exclusions, recipes or mapper settings were changed.

#274/#324 taxonomy input is captured; boss/miniboss decisions, exact Reliquary charm/fragment eligibility, JAWR blood-capacity units/formula, native custom death handlers, natural spawn provenance and Theurgy production coverage remain separate source/gameplay audits. Do not install a boss blacklist from native `monster` category alone.

Quest registry gate was attempted against the **installed** FTB chapters. The current source scanner fails closed at `a_sufficiently_advanced_magic.snbt:29`: an `item` compound has `count` before `id`. A separate read-only balanced-compound survey (`scripts/survey_quest_references.py`) handles field ordering and nested component payloads. All 232 direct IDs exist, but three existing `ftbquests:missing_item` placeholders still refer to unavailable content: `brewinandchewin:beer_tankard` at `alcohol_is_magic.snbt:163`, and `cookingforblockheads:oven` at `food_fishing.snbt:639` and `:665`. Those quest references need repair; direct-ID presence alone is not a quest PASS. The existing CI gate was not modified.

Recipe ingredient alternatives are bounded to 128 per slot; **754 slots are explicitly marked truncated** with their original alternative count retained. Recipe component data, magical resource costs, catalysts and returned items were not exported. Eight positive default EMC values exceed JavaScript's exact integer range; their exact final digits require a native Java long serialization check before numerical balance work. Coverage positivity remains established; do not use those eight observed decimals as audited exact prices. Earlier probe iterations exposed Rhino const/overload differences and one heap exhaustion while serializing recipe component data. The final smaller export completed with zero read errors; only this final dataset is published. The temporary probe file has been removed from the installed server_scripts directory.

Static resource scan observed 51,975 parseable selected resources, including 628 entity loot resources; 39 strict-JSON parse failures are recorded in summary.json. Native JSON-with-comments resources, other world/ZIP datapacks, conditions, tag replacement/merge order and custom event handlers are unresolved. The static overlay uses path precedence for inspection, not a faithful Minecraft resource-pack resolver.

## Reproduction

From a compatible checkout, with the same installed instance and existing world explicitly authorized:

1. Record BUILD-MANIFEST identity and hash installed JARs. Install the temporary probe from `scripts/runtime_survey_probe.js` into local KubeJS server scripts only.
2. Run `/reload`, then `/tmm_survey_capture`. Read `kubejs/export/tmm-runtime-survey.json`; require zero item errors before using EMC coverage.
3. Run `python scripts/survey_installed_resources.py --minecraft <instance-minecraft> --output <local-static.json>`.
4. Run `python scripts/analyze_runtime_survey.py --capture <runtime-json> --static <local-static.json> --output docs/qa/surveys-2026-11-18`.
5. Remove the temporary probe after capture. Do not package it as a gameplay feature. No saves, player identifiers, raw logs, JARs, textures or upstream recipe payloads from the static scan are published.

Python syntax checks were run. Successful live probe execution and dataset counts/errors are recorded in summary.json. No automatic full smoke or test package was dispatched.

## Integration handoff (2026-11-19)

Preserve the frozen survey datasets from the original capture commit `fa869c43a685241d4d9b48e7d2b89d019c321856` unmodified. Consumers #320/#326 (economy), #353 (taxonomy) and #347 (Progression) must cite the installed build identity and dataset, not interpret this merge as a new census. Merging this research does not activate the temporary probe or resolve its stated coverage limitations.
