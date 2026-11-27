# Weeping Angels — verified current-build dimension census and integration handoff

**2026-11-22 checkpoint:** The owner ran `/reload` and `/tmm_dimension_registry` in the experimental disposable world. [PR #527](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/527) records a **live `server.levelKeys()` 19-ID capture** at `2026-11-22T21:43:16.855382100Z`, matching the 19 persisted save registry keys with zero errors. The exact build was `c56fb18ffc3b9f12f27317e7fb37f15c32576a8c` (manifest SHA-256 `1728619fdbb48b95873fa46b7e2f93d3211cb1893e29744b395d89793e1c8fc3`). This supersedes the former 'unknown JAWR ID' blocker for **this exact build only**; other/new/lazy-loaded dimensions require a renewed census.

- **Witness:** `docs/dimensions/weeping-angels-live-census-c56fb18f.json` is a deliberately compact format adaptation of PR #527's `docs/dimensions/exports/c56fb18f-2026-11-22/live-dimension-registry.json`, not a second server capture. Original Git blob SHA: `02d8413cecd343591aef20a41f49e8f92bc9a3ae`; original PR head: `8bdc44e758617b5a971332b2847b6d4de08184d6`.
- **Loaded permit set (6):** `minecraft:overworld`, `aether:the_aether`, `the_bumblezone:the_bumblezone`, `twilightforest:twilight_forest`, `theaurorian:the_aurorian`, `eternal_starlight:starlight`.
- **Loaded deny set (13):** `deeperdarker:otherside`, `irons_spellbooks:pocket_dimension`, `mahoutsukai:reality_marble`, `minecraft:the_end`, `minecraft:the_nether`, `neovitae:dungeon`, `pastel:deeper_down`, `unicornacademy:unicorn_island`, `voidscape:void`, `wardentools:abyssdim`, `witchery:dream_world`, `witchery:mirror_world`, `witchery:nightmare_world`.
- **Proactive additional exclusion:** `botania:skybox` is a known conditional resource but **not loaded**; PR #527 found `modules.skybox=false`. It is blacklisted defensively, not counted in the 19.
- **Witchery/JAWR:** `witchery:dream_world`, `witchery:mirror_world`, and `witchery:nightmare_world` are the loaded IDs. No invented `witchery:spirit_world` entry.

## Repeatable source check

From the Weeping Angels feature branch, run:

```sh
python -m unittest discover -s scripts/tests -p 'test_weeping_angels_dimensions.py'
python scripts/audit_weeping_angels_dimensions.py \\
  --census docs/dimensions/weeping-angels-live-census-c56fb18f.json \\
  --require-census \\
  --expect-integration-commit c56fb18ffc3b9f12f27317e7fb37f15c32576a8c
```

**Interpretation:** A successful source audit establishes that all loaded levels in this frozen build are covered by the approved six or the native blacklist. It is **not** a verified Weeping Angels loaded-mod behavior test: the 1.0.5 NeoForge binary is not installed/pinned by this PR. The blacklist is not fail-closed against later added dimensions. Re-run against the exact candidate build and its full runtime keys before promotion; fail integration if any allowed realm disappears or any new realm is unaccounted for. No dimension-weighting or teleport patch is permitted.

## Integration-writer owned tasks

1. Independently download and verify the exact official 1.21.1 NeoForge Weeping Angels 1.0.5 JAR and metadata/dependency closure; candidate CurseForge project 291249 / file 8962702 (`forge-weeping_angels-mc1.21-v1.0.5-forge.jar`). Third-party SHA-1 `2f5843d18e0fe3905d3569cc19022d4f2f7e7570` remains **unverified**, not a valid pin by itself. Check runtime config filename, keys and same-ID `neoforge:none` spawn modifier behavior.
2. Reconcile this config and the source-only Angel files, index/Packwiz/manifest and inclusion matrix against latest `test/full-integration` with single-writer ownership and required quick CI/preflight. PR #527 is **stacked on PR #504**, so preserve its witness/provenance without merging unrelated QA work into PR #392.
3. Re-capture the full dimension registry if the assembled candidate differs from `c56fb18ffc3b9f12f27317e7fb37f15c32576a8c`; compare **loaded − six allowed** against the native `banned_dimensions` and ensure no new/lazy-loaded world is eligible. A later Botania skybox activation is already proactively denied, but all other new IDs still need an explicit update.

## Separate implementation and in-world QA remaining

- Physical-only damage: pickaxes, genuine unarmed/Monk fists, Dragon Survival claws/bites, native generator/admin exceptions; ordinary swords/arrows/spells rejected. No blanket `ANYTHING`.
- Two dormant `weeping_angels:statue` traps: calibrated sculk footstep chamber and concealed tripwire corridor; proper NBT, single activation, reload, sneaking, false positives and claim handling.
- FTB Chunks: with `mobGriefing=true`, Angels may alter unclaimed lights/switches/candles but **never** claimed world blocks, without suppressing statue activation/combat.
- Authorized B/C gameplay: exactly six effective teleport destinations, native local/fallback behavior, no natural or Snow Angels, no theft, five naked return routes and Newbie travel behavior. Live dimension export was a B PASS *for the census only*.

**Scope:** config and read-only checks only. No Integration Assembly, server smoke, Prism package, Angel gameplay validation or binary pin was done in this work slice. Keep PR #392 draft until independent integration conditions are met.
