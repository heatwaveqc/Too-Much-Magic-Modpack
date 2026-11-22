# HerbalBrews 1.1.4 — isolated inclusion candidate (#331)

**Source-only candidate, 2026-11-20.** Owner-approved tea/coffee mod in #331, for Minecraft 1.21.1 / NeoForge. This PR intentionally adds an *unindexed* packwiz pin only; it does **not** activate a mod, claim client compatibility, perform Integration Assembly, or authorize a full server smoke/Prism package.

## Exact provider selection
- Publisher: [Let's Do] HerbalBrews, **NeoForge 1.21.1 release 1.1.4**, both client/server, published September 19, 2026.
- CurseForge: project **951221**, file **8918021**, filename `letsdo-herbalbrews-neoforge-1.1.4.jar`. [Official artifact page](https://www.curseforge.com/minecraft/mc-mods/lets-do-herbal-brews/files/8918021). [Modrinth same release](https://modrinth.com/mod/lets-do-herbalbrews/version/1.1.4), version ID `kGYbyn9P`.
- Candidate SHA-1 in `mods/lets-do-herbalbrews.pw.toml`: `22cddc92a7bca8d9816de608355a8329566e44cb`. **Secondary published modpack inventories** ([example](https://modpacks.ch/modpacks/1735374/monventure)) map this exact filename and CF file ID to that hash. **Not independently verified from the publisher JAR bytes in this authoring session.** Integration writer must download through permitted provider and verify exact bytes/hash before indexing; reject on mismatch.
- Native source [NeoForge metadata](https://github.com/Let-s-Do-Collection/HerbalBrews/blob/1.21.1/neoforge/src/main/resources/META-INF/neoforge.mods.toml) requires `architectury >=13.0.0` and `neoforge >=21.1.0`. The existing pack already pins `architectury-13.0.11-neoforge.jar` and targets NeoForge 21.1.265 / MC 1.21.1. Upstream source `gradle.properties` identifies HerbalBrews 1.1.4 / Architectury 13.0.8 development reference, not an incompatible hard pin.
- Publisher's [1.1.4 changelog](https://modrinth.com/mod/lets-do-herbalbrews/version/1.1.4) explicitly repairs Jug save/reload tea data, a contraption-related item duplication bug, inaccurate duration tooltip, and redundant UI rendering. Version 1.1.3 also fixed duplicate drink effect application and a drying-leaf crash.
- License: upstream labels compiled mod `All Rights Reserved`, with distinct MIT code and ARR resources. **Reference distributor-hosted files only; do not vendor or copy JAR/textures into TMM git without separate permission.** [Source](https://github.com/Let-s-Do-Collection/HerbalBrews/blob/1.21.1/License).

## Integration ownership and decisions
The designated integration writer should (after exact hash verification and PR review) update the canonical desired roster `manifest/mods.json`, add the pin to `index.toml`, relock `pack.toml`, reconcile concurrent PR ownership and check `python3 scripts/validate.py` plus ordinary quick CI. This feature writer **does not touch** shared manifest/index/pack files. Keep the mod client+server, no unapproved dependency or other mod, and no full-smoke workflow dispatch.
The issue remains open until combined actual runtime QA. Do not call a source-only pin INCLUDED or LIVE-TEST READY.

## Type A / C acceptance
- A: download exact CF 8918021, hash verify `22cddc92a7bca8d9816de608355a8329566e44cb`, inspect actual jar's `META-INF/neoforge.mods.toml`, confirm no unexpected dependencies, library version bounds, configs and indexed hash chain.
- A: inventory planted/wild tea, coffee and floral crops; compare with Pam's HarvestCraft, Farmer's Delight and other existing food tags, but retain distinct brewed teas/coffees and station recipes. Avoid speculative mass tags/recipe rewrites without same-build item IDs and actual recipe evidence.
- A/C: test Tea Kettle, Copper Tea Kettle, Brewing Cauldron, dried leaves, jug/flasks, beneficial area effects, plant loot and reload persistence; exact component/cost/consumption conservation and effect timing. Check overlap with Cooking for Blockheads and Farmer's Delight kitchen routing and beverage tolerance #334, potion interoperability #337, transformation diets, EMC rules and natural worldgen. Do not treat Create-specific bug fix as permission to add Create (excluded in this pack).
- C: on owner-authorized disposable candidate, verify actual client/server boot and harvest/prepare/store/drink/relog; check no duplicated outputs, unique bottle/remainder handling and all intended native recipes. Existing saves should not be edited as QA fixtures.

No new mod beyond owner-approved #331; no game boot, full smoke, Prism publication, or integration assembly performed.
