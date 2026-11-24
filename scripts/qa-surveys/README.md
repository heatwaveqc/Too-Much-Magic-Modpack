# Optional in-game QA surveys

Initially prepared 2026-11-18; revised after partial live QA on 2026-11-22. See [actual results and adapter limitations](../../docs/qa/EXPERIMENTAL-2026-11-22-RESULTS/README.md). Nothing in this directory is indexed into the pack. The suite is disabled unless its local configuration explicitly enables it. All eleven server commands require an actual player with permission level 2; console and non-operators cannot run them. No enrollment, graduation, grants, kills, teleports, recipes, trades, key assignments or game rules are changed. Local report files are written on request. Native serialization/getter adapters may initialize internal caches; this is not a claim that every upstream getter is side-effect-free.

## Install later, before the next test client launch

Use a disposable test instance. Copy these files manually; preserve any existing files with the same names before replacement:

| Source under this directory | Destination relative to instance `minecraft` |
| --- | --- |
| `server_scripts/tmm_surveys.js` | `kubejs/server_scripts/tmm_surveys.js` |
| `client_scripts/tmm_survey_client.js` | `kubejs/client_scripts/tmm_survey_client.js` |
| `tmm-surveys.example.json` | `kubejs/config/tmm-surveys.json` |

Change the **local copy** of `enabled` to `true`. Copy the packaged `BUILD-MANIFEST.json` into `minecraft/kubejs/config/tmm-survey-build.json` before snapshot commands. KubeJS forbids reads outside `minecraft`, so helpers must not read `../BUILD-MANIFEST.json` directly. The optional preparer below instead creates a reduced manifest copy with a source-file hash. Snapshot comparisons refuse a missing manifest. Start the client/world normally only when ready to test. The client script requires a client restart after installation. Config is read once per script load; switching it off needs a script reload/restart. Remove the two helper scripts and local config/build copy when done; generated reports are kept separately. Never include enabled helpers in a release pack.

### Optional one-shot preparation

`python scripts/qa-surveys/prepare_one_shot.py --minecraft <disposable-instance-minecraft>` stages a combined temporary OP-only observer and explicit enable flag for the **next existing-world rejoin**; it never starts a client or world. It gathers command/recipe census and attempts baseline/match/player captures via the QA API (not the command parser). Inspect `tmm-one-shot-status.json` per-capture statuses; a final log marker never means all captures passed. Remove `zz_tmm_world_observe.js`, `tmm-observe-once.json` and `tmm-survey-build.json` after use. Native API changes may still yield PARTIAL/UNSUPPORTED. These tools were removed from the live instance after the November 22 attempt; final adapter corrections have not been rerun live.

## Commands

Run commands individually, without Markdown fences. Replace labels and coordinates where shown. Labels are 1–32 lowercase letters/digits/underscores/hyphens; use a new label for each trial. Baselines cannot be overwritten.

| Command | Purpose / boundary |
| --- | --- |
| `/tmm_survey_help` | Save command/syntax inventory. |
| `/tmm_survey_baseline` | Mod versions, command roots, selected datapacks, dimensions and structure IDs. Presence does not prove permissions, worldgen or behavior. |
| `/tmm_survey_player before trial1` then `/tmm_survey_player after trial1` | Player health, food, XP, effects, existing Progression attachments (ledger/skills/mutations/foods/modes), Tyz XP/level, existing MXP account counters and native Ars/Iron mana. Produces a structural diff and numeric deltas. |
| `/tmm_survey_inventory before starter1` then `/tmm_survey_inventory after starter1` | Same full snapshot protocol, emphasizing inventory/armor/offhand and Curios. Container/tome component text is bounded; not a recursive containment proof. |
| `/tmm_survey_tree before oak1` then `/tmm_survey_tree after oak1` | Same protocol around actual barehand chops/felling. Measures attachment counters and XP; does not chop or inspect Jade graphics. |
| `/tmm_survey_magic before ars1` then `/tmm_survey_magic after ars1` | Same protocol around an actual cast/exchange/refill or negative control. Measures deltas; does not infer the payer in shared-mana modes. |
| `/tmm_survey_recipes` | Seven default match tests: four raw and two cooked fish controls for Seasoned Fish, plus the nine-slot Bio-Mutagen recipe. Calls real crafting `matches` using detached input stacks; never crafts, assembles, consumes or grants anything. |
| `/tmm_survey_recipes match NAMESPACE:RECIPE SLOT1 SLOT2 ...` | Custom positive crafting test with 1–9 row-major 3×3 item-ID slots. Use `reject` for a negative test and `-` for an empty slot. Counts are one per slot; no NBT/components/tag expressions. Only crafting-input recipes are supported. Copy the actual recipe/item IDs from JEI/exports; absent or incompatible recipes are UNSUPPORTED, not silently skipped. |
| `/tmm_survey_traders` | Inspect already serialized offers on loaded wandering traders within 16 blocks. Does not call the lazy `getOffers()` generator. Interact normally first if offers are uninitialized. Captures components for later category/price review, not automatic six-plus-four stock acceptance. |
| `/tmm_survey_gateways` | Loaded Placebo Gateway registry IDs plus instantaneous wave/enemy/lives/timer/failure/completion state on loaded Gateway entities within 32 blocks. Run before/during/after an actual encounter; disappearance alone never proves success. |
| `/tmm_survey_botania start X Y Z 600` then `/tmm_survey_botania stop` | Observe one existing Botania flower/pool within 16 blocks for up to 600 server ticks. Samples native mana/max/burn/cooldown getters each tick, auto-stops sampling at duration, then exports when `stop` is run. Limit 1,200 ticks and eight uncollected monitors. No chunk loading, block placement, fuel insertion or time changes. Reports net buffer rate and cooldown/burning sample counts; not gross output or conversion efficiency. |
| `/tmm_survey_client` | Request a separate client key-registry export in integrated singleplayer. Client writes within roughly one second of active client ticks. Requires both helpers. Does not reset, assign or press keys. Dedicated-server requests explicitly refuse because client/server filesystems differ. |

All snapshot commands use the same data schema; `tree`, `magic` and `inventory` are separate namespaces for baselines, not separate native event hooks. They survive logout, death and script reload through local report files. They compare the same hashed world path/player identity, suite version and manifest. A copied world under a different path needs a new baseline. If a future build changes native APIs, individual adapters report UNSUPPORTED with a bounded reason rather than fabricate data.

### Minimal next-session sequence

1. Run `/tmm_survey_baseline`, `/tmm_survey_recipes`, and `/tmm_survey_client`.
2. For the specific remaining test, take a uniquely labeled `before`, perform **one** real action, then take `after`. A relog test uses the same label after rejoining; an actual death/graduation test still needs its independently authorized disposable fixture.
3. For trader stock, interact with an existing test trader, run the trader survey, rejoin and repeat. For a Thermalily rig, monitor the flower and pool in separate runs; ensure transfer/output conditions are documented.
4. Collect only the relevant JSON exports, not full logs or saves. Ask Codex to evaluate the measured deltas against that build's rules. No before/after comparison labels itself a gameplay PASS.

## Coverage against the B/C checklist

Reference: `docs/qa/UNSTABLE-QA-2026-11-17.md`, also present unchanged at installed source `f7aeb56a0f7b9c5957f922f5a397037f25207c76`. That checklist describes an older frozen build; reconcile feature inclusion/version and updated expectations before using it on a future build.

| Cases | Automated evidence available | Still requires action/review |
| --- | --- | --- |
| B01, B07, B09 | Presence/registry/config-manifest observations; crafting matches | Status meaning, real transformations, actual crafts/worldgen |
| B02, B04, B05; C03/C05/C06/C08 | Attachment/wallet/effect/resource before/after deltas | Fixtures, legitimate purchases, final blows, actual casts/brewing, XP expectation and attribution |
| B03, B10; C02/C18 | Snapshot conservation/persistence around actual actions | Non-OP accounts, travel/death, mixed-mode multiplayer, restoration and duplication boundaries |
| B06; C04 | Tree XP/counter measurements | Bedrock sleep, glide, tree felling, Jade bar visibility |
| B08; C11 | Saved trader offers and repeated player observations | Vendetta commands/AI/missiles/Hero policy, natural spawns and semantic category assertions |
| C07/C09/C13 | Components, Curios, food history, existing class/mutation state; detached crafting tests | JAWR blood pool adapters absent; rendering/diets/consumption, protected native blood, real first-log claims and quest text |
| C10 | Flower/pool buffer, burn and cooldown measurements | Native Mahou payment/transfer controls; energy converter API adapter not implemented |
| C12/C14/C15 | Loaded structures/dimensions, crafting matches, Gateway IDs and arena telemetry | Real worldgen/locators, magical recipe adapters, boss gates, economic loops and reward delivery |
| C01/C16/C17 | Key registry/default/current bindings; logs remain separately readable | Default/reset/restart behavior, vanilla allowlist comparison, JEI/HUD/darkness/hearts/sleeping visuals and usability |

Destructive/synthetic fixtures from the survey proposal are deliberately **not implemented** here: no admin XP/join/graduation, spawning, deaths or automatic permission changes. Generic recipe matching does not test brewing, rituals, custom costs, catalysts or remainder handling. No hardcoded ten-hour mutation shortcut or fabricated admin command is added.

## Output and bounds

Server reports go under `kubejs/export/tmm-surveys/<hashed-world>/<hashed-player>/`; each report identifies suite/build/time/tick and evidence type. Snapshots contain local inventory/component details; component excerpts are capped at 2,048 characters with a full-text fingerprint and truncation flag. Hashed player/world identifiers prevent raw UUID/path disclosure but are pseudonyms, not an anonymity guarantee. Client reports include **current key bindings**, which may be private. Review/sanitize all exports before repository upload. No reports are uploaded automatically.

`OBSERVATION_ONLY`, `UNSUPPORTED`, `SETUP_MISSING`, `REQUESTED_NOT_CAPTURED`, `STOPPED_EARLY` and `INTERRUPTED` are not gameplay passes. Only a narrowly scoped `PASS_RECIPE_MATCH_ONLY`/`FAIL_RECIPE_MATCH` is emitted automatically where an explicit expected match exists. Eight high-value EMC precision problems from the earlier census are not addressed by this suite; no new EMC pricing claims are made.

Monitor data is bounded and held in memory until `stop`. Reloading scripts or closing the world discards an unfinished/uncollected monitor; the start report alone is not a result. Loaded-block checks avoid forcing chunks. Changed/unloaded block instances interrupt sampling. Server ticks measure game time rather than wall-clock time. Native resource regeneration, transfers and unrelated actions can affect comparisons.

## Offline validation and API evidence

Run from repository root:

```text
node --check scripts/qa-surveys/server_scripts/tmm_surveys.js
node --check scripts/qa-surveys/client_scripts/tmm_survey_client.js
node --test tests/test_qa_surveys.cjs
java -cp <pinned-rhino-jar> tests/ParseQaSurveys.java scripts/qa-surveys/server_scripts/tmm_surveys.js scripts/qa-surveys/client_scripts/tmm_survey_client.js
```

Node tests exercise mocked permission/opt-in gates, snapshots and build mismatch handling, unsupported adapters, parser bounds, local client requests and read-only policy. The Java helper only parses JS with Rhino; neither check starts Minecraft. Neither establishes runtime interoperability.

Pinned local bytecode was inspected for KubeJS 2101.7.2-build.381 (CommandRegistryKubeEvent, server/client events), Rhino 2101.2.7-build.85, Botania Revived 2.0.2 (`velrondevs.botania` Thermalily/FluidGenerator/GeneratingFlower/ManaPool getters), Gateways 5.1.0/Placebo 9.9.4 (custom DynamicRegistry and GatewayEntity telemetry). First-party adapters reference Progression attachment codecs and MXP `snapshot`/account APIs from current integration source. MXP uses `DimensionDataStorage.get` for existing data, never its creating `get(server)` convenience method. Tyz getters match the read side of the existing Tyz bridge. Actual client/server bridge calls and future mod versions still need the next explicitly authorized test world.

**Command-parser acceptance remains NOT RUN.** The November 22 one-shot API invocation yielded a live command/recipe census but failed other capture adapters; see linked results. Node tests and Rhino parsing do not promote those cases to PASS. Next step is targeted authorized QA, not an automatic build or launch.
