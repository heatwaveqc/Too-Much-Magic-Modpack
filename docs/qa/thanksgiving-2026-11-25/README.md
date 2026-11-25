# Installed Thanksgiving build — automated QA, November 25

Owner-authorized Type A execution for [PR #708](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/708), continuing its existing queue/ledger rather than opening another workstream. Writer: this QA execution chat. Installed source **721ea058294f838b9d7f145af427f5db228d248d**, baseline **e090df93d500784a050fb7cab2b2c153fb4fcc03**, package run **45550879036**. Minecraft 1.21.1 / NeoForge 21.1.265 / Java 21.0.7 / Gradle 9.2.1 / Python 3.12 / Node 24.22.3. Current integration HEAD is not substituted for this build.

No client/server/world was launched, no commands were sent to Minecraft, and no installed files, player state, settings or save data were changed. Existing client logs were read. All compilation used an isolated copy of the frozen source; only compile/test tasks ran. No assembly, packaging or full smoke was performed.

## Completed checks

- **255/255 installed JARs match** the packaged expected filenames and SHA hashes. Four selected first-party JARs are Progression, Economy, Trader's Vengeance and Friendship Garden. No extra/missing JARs.
- **543 Java tests passed, zero failures/errors/skips:** Progression 462, Economy 58, Trader 14, Friendship Garden 9. All four compile successfully. These isolated tests do not instantiate Minecraft or certify mixin application/gameplay.
- **13 source validators passed:** repository metadata/hash chain (704 indexed files / 251 pins), quests (44 chapters / 277 quests / 19 personal graduation choices), retirement, unified Progression, silver, materials, dragon diets, ProjectE progression gates, Default Options, trader stock, Gateways, QA inventory and Newbie integration.
- Seasoned Fish source validator passed. **20 Node tests passed** across parchment fishing and QA surveys. **2 new log-auditor tests passed**, including private login-line exclusion.
- **22/22 installed KubeJS files parse** with installed Rhino 2101.2.7-build.85. Parsing and compilation alone do not detect the execution failure below.
- Broad Python discovery: **200 tests, 196 passed, 1 failure + 3 errors** using UTF-8 mode. Failure: generated feature-checklist Markdown differs from its canonical renderer. Errors: Default Options fixture omits newly required `key.invtweaks_sort_either.desc`; two old Monk/Tyz tests refer to removed `TyzMonkBridge.java`. These are real test-suite maintenance failures, not evidence that retired Tyz should be restored. A first run under Windows CP1252 additionally failed two downloader tests on UTF-8 pin text; UTF-8 rerun resolves those two only.
- The old `node scripts/verify_projecte_boss_recipes.cjs` instruction points to an absent file. Its replacement `python scripts/validate_progression_gates.py` passed; record the old instruction as blocked/stale, not a gameplay failure.

Initial temporary source extraction used Windows CRLF and caused false indexed-hash/fixture failures. Normalizing only that temporary source's text files to canonical LF resolved the repository/default-options/Newbie checks. No validator or indexed source was weakened. Progression's first offline Gradle attempt lacked cached Jade; ordinary scoped dependency resolution succeeded on retry. All final Java results above come from successful runs.

## Reproduced failures and precise follow-up

### 1. Reactive chest-book script does not execute (#328; C22)

The client reports `Loaded 12/13 KubeJS server scripts ... with 1 errors` twice. Both failures point to `zz_reactive_loot_books.js#4`: `TypeError: redeclaration of var TMMReactiveComponents`. Its installed source has a top-level `if (...) { const ... }` block; no second source declaration was found. Introduction: `56d72ea9fff06efa7d676411102586c87baef532`.

Pinned Rhino offline **execution** reproduces this without loading any mod or Java game class:

```javascript
if (true) { const TMMReactiveComponents = 1; }
```

The identical installed Rhino throws the same redeclaration error in a fresh standard scope. Controls `const TMMReactiveComponents = 1;`, `if (true) { let TMMReactiveComponents = 1; }`, and `(function () { if (true) { const TMMReactiveComponents = 1; } })()` all evaluate successfully. `Parser.parse` and `Context.compileString` both accept the failing script: this is an execution-scope issue, not a syntax defect or proof of duplicate script files. The developer should scope the guarded declarations compatibly, retain the mod-presence guard, and run a pinned-Rhino execution regression before a separate in-game loot test. No gameplay patch applied here.

### 2. Perpetual Life ritual is absent from loaded recipes (#338; C15)

`tmm:ritual/perpetual_life` is rejected: `Unknown registry key ... neovitae:hellforged_ingot`. The authored recipe uses that ID twice. The installed Neo Vitae 1.1.54 JAR's own `data/neovitae/recipe/hellforged_ingot_from_block.json` and blasting recipe instead produce **`neovitae:ingot_hellforged`**. Correct the authored ingredient through its existing writer and add exact-provider ID validation; source contract tests currently miss this. Integration introduction is `4b3323dac82f59b17e5a88bafe47496d390b896d` (the source donor predates it). Do not ask the owner to run this ritual on the current build as if registered.

### 3. EMI plugins have binary API mismatches (#507 / C02)

- `extra_mod_integrations_rechiseled` fails with `NoClassDefFoundError: com/supermartijn642/rechiseled/chiseling/ChiselingRecipes`.
- `extra_mod_integrations_reliquary` fails with `NoClassDefFoundError: reliquary/items/AlkahestryTomeItem` while building Alkahestry charging recipes.

These exact client exceptions establish plugin registration failures, not that all EMI functions fail. Review native provider/plugin versions and their exact class references through the existing viewer integration writer. Visual inventory/sidebar tests remain manual.

### 4. Other observed failures requiring triage

- **52 recipe decoder errors:** Pastel 38, Malum 9, Cocktails Delight 2, Dungeon's Delight 1, TMM 1, Ender's Delight 1. IDs and reasons are in [client-log-summary.json](client-log-summary.json). Examples: Pastel missing `colors` / invalid fluid encoding. Separate native shipped recipes from authored overrides before choosing repairs. The log reports **27,947 loaded recipes**, which is not a full effective registry export.
- ProjectE reports an error mapping `goety:savage_tooth_craft` because `goety:totem_of_roots` throws while returning its container item. This is a mapping failure, not proof of a client crash or a completed all-item EMC audit.
- **15,584 `Invalid key -1` lines** in the captured log snapshot; counts are bounded to that SHA-256 snapshot and the active file may grow. Recurs after the blanket-unbind changes; exact caller is not yet attributed. Do not infer Sodium fixed the earlier issue.
- No `InvalidInjectionException`, `MixinApplyError` or `MixinTransformerError` lines were found by the targeted scanner. This is not a general assertion of zero warnings/errors.

## Installed resources, save and export limits

Compared **422** indexed resources under KubeJS, Default Options, FTB Quests and defaultconfigs against this frozen build. None missing. **376 match** (text EOL normalized; binary bytes compared unchanged); **46 FTB quest files differ**. Spot inspection shows FTB runtime reserialization, generated image IDs/defaults and field reordering. Preserve these as observed differences; a semantic comparison is still required before calling every difference cosmetic. All compared KubeJS/default-options/defaultconfig files match. No live files were restored or overwritten.

No same-build recipe/item/food/EMC exports were present when inspected. A06/B04/B05 remain **BLOCKED / NOT RUN**, not an empty census or pass. The owner can later run the queued native food and EMC commands and the opt-in recipe observer; this task did not install helpers or run commands. The priority Replica/stonecutter follow-up (#448/#565) must use that fresh effective recipe export.

Additional AS-A01 input: installed Astral 2.0.3.59 ships **364 distinct perk-resource IDs**, **5 roots**, **90 nodes without declared requirements**, **274 with declared requirements**, and config `perkLevelCap = 30`. Type counts and provider SHA are in [astral-partial.json](astral-partial.json). This is **PARTIAL**, not a live registered/accessible node count or a completed token-cost calculation. Hidden/access/exclusion/allocation-cost/native-point behavior still needs native review; no maximum simultaneous-unlock or bonus-token number is asserted.

Read-only Integration Preflight also passed on clean QA handoff commit `faa80679011fc3133bb0d352d46621faa0f87d79` (the report branch, not a new frozen build). It reported unclassified draft refs as advisory; no exhaustive all-ready-features inclusion claim is made. Existing frozen-build preflight evidence remains attached to run 45550879036.

## Reproduction and handoff

Run source checks from a canonical-LF copy of the exact frozen source:

```text
python scripts/audit_client_inventory.py <instance/minecraft>
python -X utf8 -m unittest discover -s tests -p test_*.py
node --test tests/test_parchment_fishing.cjs tests/test_qa_surveys.cjs
gradle --no-daemon -p compat/progression test
gradle --offline --no-daemon -p compat/economy -PtmmCompileModsDir=<verified-installed-mods> test
gradle --offline --no-daemon -p compat/traders-vengeance test
gradle --offline --no-daemon -p compat/friendship-garden test
python scripts/audit_kubejs_syntax.py --minecraft <minecraft> --java <Java21> --rhino-jar <installed-rhino.jar> --report <local-report.json>
python scripts/audit_qa_client_log.py --log <latest.log> --output <summary.json>
java -cp <installed-rhino.jar> tests/RhinoBlockScopeProbe.java
```

[Offline commands/results](offline-checks.json), [module counts](module-tests.json), [Rhino parse results](rhino.json), [installed-resource comparison](installed-runtime-resources.json). These contain derived evidence, not raw private logs, saves or upstream assets.

- **Implementation-ready:** completed this owner-requested Type A execution and reusable sanitized log auditor; 2 auditor tests pass. QA writer owns this report and PR #708 continuation. Scoped source checks complete; source defects above handed to existing subsystem writers, not silently patched in the installed build.
- **Design blockers:** none for reporting these failures. Repair investigation/version reconciliation is engineering work, not a new owner balance choice.
- **B/C queue:** retained in the parent [per-build results ledger](../THANKSGIVING-QA-RESULTS-2026-11-24.md). Ritual C15 and Reactive C22 need repaired inclusion before their positive tests. EMI C02 is failed for the two logged plugin-registration cases; remaining visuals require manual observation. No B commands or C gameplay actions performed by this chat.
- **Status:** `status: needs-review` until evidence/source review and required quick checks on the final PR head. Designated integration writer reviews this continuation of #708. No separate QA branch/PR created; no merge or release claimed.
