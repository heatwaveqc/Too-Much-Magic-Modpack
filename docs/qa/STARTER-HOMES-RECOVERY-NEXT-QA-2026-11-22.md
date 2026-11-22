# Starter housing — 2026-11-22 source recovery and next authorized QA checklist

**Authority:** owner-approved houses under [#236](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/236), [#509](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/issues/509) most recent starter-inventory policy (Nov 22), [18-house donor #416](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/416), Inn Bountiful office board [#381](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/381), and stalled engine one-use home offer [#418](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/418). This record supersedes conflicting *current operational instructions* in old donor handoffs, but does **not rewrite their historical content**.

## What the next owner-authorized build will include

- **Source already selected:** Prefab 1.0.9, Cloth Config, Prefab Custom Addon 2.7.1 and LDLib2 2.2.46.a are separately pinned on current Unstable (verify exact runtime loaders during boot). Keep existing **native Prefab first-login starter-house item, ordinary starter items/bonus supplies, chest and optional mining shaft** intact per #509. Do not remove them.
- **18 custom NBT previews:** exact donor generators and catalog copied unchanged from #416; source-generated in a marked disposable CI directory, with one exact-hash-checked Bountiful board plus ordinary rank-1 Hunter/Hunting, Culinary and Fishing decrees from #381 inserted into the original Adventurer's Inn. 18 distinct custom-house IDs and 1 native Generic selection family (15 stock styles), but **none** of the 18 currently grants a free extra entitlement.
- **Build-time distribution:** `scripts/build_full_integration_prism.py`, invoked **only after separately authorized successful full server smoke**, regenerates every approved blueprint into ephemeral temporary directories. It includes matching files under `minecraft/prefab-extension/` and `minecraft/prefab-extension/server-cache/` in the new disposable Prism QA instance. `scripts/smoke_server.sh` stages the 18 into its empty dedicated-server directory **only if a full modded server smoke is authorized**. No persistent player instance or save is modified. All generated files are explicitly QA-only; they are not asserted survival-safe or already claimed by a player.
- **One-time graduation entitlement:** [#418](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/418) is still a **separate Progression-owned PR**, not merged or activated by this source recovery. It grants a second one-use house after graduation; that would **duplicate** the newly retained native first-login item. The owner has not approved two free houses. **HOLD entitlement activation** until the Progression writer reconciles with #509 without deleting the native item. The 18 addon constructions remain ordinary material-supplied native construction options during QA; global `prefabaddon freemode` must remain off.

## Source quick-CI (Type A)

- [ ] `test_houses_qa.py`: 18 exact unique custom source IDs, no auto-grants, safe file/path and conflicting overwrite refusal
- [ ] `test_bountiful_board.py`: exact one Inn office board and 3 normal category decrees, not the Path-gated Hunter decree
- [ ] Complete 18/18 reproducible NBT generations with source raw SHA/CRC/NBT checks, zero command/structure blocks, no generated binary artifact upload
- [ ] Dry-run stage into **both** singleplayer paths in a marked temporary directory; 18 matching files, no user game paths
- [ ] Repository bootstrap/manifest/offline synthetic Prism QA packaging checks, no full game boot

## Real Minecraft QA (B/C — NOT RUN)

- [ ] New Prism QA import downloads exact Prefab/Custom Addon/LDLib2 versions; vanilla stock first-login Prefab item, ordinary starter items/chest/shaft are still available, including optional shaft's normal resource dig. No duplicated automatic custom-house gift.
- [ ] Prefab Custom Addon detects 18 unique named choices, no missing NBT or unknown-block substitutions; UI can view/rotate/place with *native* material debit and claims checks, no freemode bypass, no untrusted blueprint uploads.
- [ ] Build one of each school-themed house in a disposable world; inspect roof/terrain/void, scale, no loot/entity duplication, native basic first stations only and legal donor attributions. Extra attention to 45×63×45 Occult tower, subfloor geode, shoreline, Wizard's Tower and large Inn.
- [ ] Inn Bountiful board loads with valid native BlockEntity and *exactly three* seeded ordinary rank-one Decrees (Hunting, Culinary, Fishing), all redeemable through current economy rules and no free boss/Hunter Path decree.
- [ ] Native free house and normally crafted/paid custom-house options coexist without duplicating graduation rewards; test two-player same-team states, logout/relog, failed placement and cross-world safety.
- [ ] Verify source/QA artifacts only; approved revised one-use Normie home mechanic remains separately owner-reviewed if desired after native starter item is preserved.

**Evidence boundaries:** green quick CI demonstrates source generation, integrity and generated packaging paths, not prefab client GUI/server import compatibility, genuine material costs, structure placement, combat, performance, accessibility, or survival acceptance. No full Integration Assembly, server boot or Prism ZIP is authorized by source PR #545 alone.
