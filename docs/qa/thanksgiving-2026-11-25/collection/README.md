# Thanksgiving live-data collection — November 25

Continuation of owner-requested QA in [PR #708](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/708). Installed source **721ea058294f838b9d7f145af427f5db228d248d**, package run **45550879036**. Rechecked all **255 installed JARs** against the packaged hashes: PASS. No gameplay code/configuration, build selection, player progression, or permissions were changed. No new build or game process was started by this collector.

## Collected data

| Evidence | Measured result | File |
| --- | --- | --- |
| Generated food registry | 26,940 item IDs; 2,543 default-stack foods, with resolved item tags | [food-registry.json.gz](food-registry.json.gz) |
| Food recipe previews | Report records 28,185 loaded holders, 25,948 result previews, 2,698 food previews / 2,136 distinct food output IDs, 2,237 unresolved previews | Same generated food report; original scope flags preserved |
| Native EMC audit | 26,940 registry IDs; 11,349 positive default-item mappings; 0 mapped-zero; 15,591 unmapped | [emc/](emc/) contains all four same-invocation reports, compressed without dropping fields |
| EMC validation | Four reports agree internally and match the supplied source label; 15,572 missing outside the frozen policy exceptions, completeness **false** | [emc-validation.json](emc-validation.json) |
| Effective recipe census | **27,947 unique ID/type/result/count rows**, zero result-adapter errors | [effective-recipes.json.gz](effective-recipes.json.gz) |
| Stonecutting subset | **2,571 recipes**, **1,775 distinct non-air outputs** | [stonecutting-recipes.json](stonecutting-recipes.json), [output IDs](stonecutting-output-ids.txt) |
| Replication-related subset | **242 entries**: 224 matter-value, 15 crafting, 1 blasting, 1 smelting, 1 Occultism miner | [replication-related-recipes.json](replication-related-recipes.json) |
| Native configuration files | 8 current config files plus 2 packaged default-config templates | [configs/](configs/) |
| Progression diagnostics | Sanitized saved modes/path ledgers, MXP balance/purchases/achievement state, food history/mutations, IS3 and Ars mana attachments; existing client diagnostic messages | [saved diagnostics](progression-saved-diagnostics.json), [log diagnostics](progression-log-diagnostics.json) |
| Current client failures | Sanitized script/recipe/EMI/EMC failure census, hash of the inspected log snapshot | [client-log-summary.json](client-log-summary.json) |

The food registry and EMC audit contain the **same exact set of 26,940 item IDs**. The EMC audit is the completed native directory captured at `2026-11-25T15:18:19.005572400Z`; the operator supplied its SHA label. Its original `source_sha_verified: false` is preserved. Independent installed-JAR verification establishes the local package identity separately. The native ProjectE entry counter is 11,989 whereas the serialized default-item map contains 11,349 IDs; do not treat that counter as 11,989 positive registry items.

The food report's earlier holder count **28,185** differs from the later effective recipe observer's **27,947**. Preserve both snapshots and their different capture paths; no claim that their recipe populations are interchangeable. Food booleans/tags and result previews are not complete component-sensitive edible-stack evaluation, crafting execution or nutrition/effect acceptance.

## Existing one-shot execution and cleanup

Used the unchanged repository preparer:

```text
python scripts/qa-surveys/prepare_one_shot.py --minecraft <installed-minecraft>
```

It staged only `kubejs/server_scripts/zz_tmm_world_observe.js`, `kubejs/config/tmm-observe-once.json`, and `kubejs/config/tmm-survey-build.json`. The owner performed vanilla `/reload`, saved/quit and rejoined the existing OP test world. The existing observer exported `tmm-world-observe.json`; its effective recipe section contains all 27,947 rows with no adapter errors and the correct build-copy identity. No grant, graduation, recipe/currency mutation, summon or teleport command was used.

The one-shot's **world capture succeeded**, while its **baseline, detached recipe-fixture and player-envelope adapters reported UNSUPPORTED**. These auxiliary failures do not invalidate the independently completed `world.recipes` census, and are not hidden by the final success marker. Current reasons include KubeJS refusing `Java.loadClass('java.lang.System')` and a missing `getGameTime` wrapper method. See [per-capture statuses](recipe-census-summary.json) and [adapter limitations](survey-adapter-limitations.json). Class filters were not bypassed and no gameplay/helper code was patched to force a green result.

After capture all three temporary files were removed and their absence verified. The owner then ran `/reload` again. The log's server-script roster returned from **13/14 with observer** to **12/13 without observer**, retaining only the previously documented Reactive-script failure. [Cleanup evidence](survey-cleanup.json). Generated local exports are retained; no temporary helper remains on disk or in the reloaded script roster.

The computer-use helper could not start because of an invalid-directory error, including after reset. No screen capture/input occurred; the owner performed the reload/rejoin steps. An initial process-check inference that Minecraft was closed was wrong and is not used as evidence.

## Configuration observations

The copied **current native files** show:

- `config/ars_nouveau-common.toml`: `general.spawnBook = true`. Installed Ars 5.13.5 registers `Config.COMMON_CONFIG` as `ModConfig.Type.COMMON`.
- `config/astralsorcery.toml`: `general.giveTomeOnJoin = true`; `perks.perkLevelCap = 30`.
- `config/improvableskills.cfg`: `Client-side` → `Add Book to Inventory = true`.

These are important counter-evidence to a source-only starter-gift-disable claim. Templates such as `defaultconfigs/astralsorcery-server.toml` say `giveTomeOnJoin = false`; a template is not proof the current native main config has that value. No matching requested mod-specific files exist in this save's `serverconfig` directory. The archive distinguishes current config files from default templates; it does not assert every disk setting was independently read back through an in-memory native API. No values were changed, and this collection is not a new-player gift gameplay test.

## Data limits and privacy

- Recipe rows expose effective ID/type/default result/count, **not full input/component/serializer payloads**. The 224 `replication:matter_value` recipes normally preview `minecraft:air`/0; that does not mean the matter definitions are empty. Actual native material-value objects, learned patterns, scanning/printing and protection-tag behavior remain separate evidence requirements. The stonecutting **effective output IDs** are now available to the existing #448/#565 writers; no tags, exclusions, prices or gates were generated/applied here.
- Saved diagnostics show an existing regular-mode Monk/Core fixture with purchases/trial milestones; they do not establish the command sequence, natural progression, or a before/after gameplay PASS. Transaction UUIDs were replaced by event counts; player identity, coordinates and unrelated attachments/inventory were excluded.
- Published client logs are structured failures and sanitized relevant diagnostic messages, **not the raw log**. Names, UUIDs, local paths and coordinate triples are removed from diagnostic messages. Observer player/nearby-entity/inventory sections were not uploaded. Configs were checked for private paths/credentials. No JAR, save, region file, private keybind or screenshot was uploaded.
- Gzip files are deterministic compact JSON copies of the generated report content; original raw source hashes are in [provenance.json](provenance.json). Sanitized evidence hashes are in [evidence-files.json](evidence-files.json). JSON/gzip round-trip, unique-ID/count checks, exact registry-set comparison, private-identifier scans and unchanged installed-JAR hashes PASS. File inventory predates this explanatory README, which is intentionally outside the machine-data list.

## Handoff

- **Implementation-ready:** requested data collection and sanitization complete in existing #708, writer this QA chat; no new workstream. Review/merge by designated integration writer after required quick checks. The EMC validator was run without `--enforce`: successful structural validation is distinct from its explicitly false coverage result.
- **Design blockers:** none for this evidence collection. Existing EMC pricing/exception policy, recipe repairs, starter config fixes and Replication exclusions remain with their existing workstreams; collection authorizes none of those changes.
- **B/C queue:** B04 census collected (auxiliary adapters partial), B05 report collected/validated (coverage incomplete). Native material-map/learned-pattern details and actual crafting/printing/food/UI/EMC behavior remain untested. Retain the parent build ledger rather than marking all acceptance complete.
