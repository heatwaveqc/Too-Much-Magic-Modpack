# Next full-integration client test pass — practical priority order

> **Scope correction (2026-11-16):** Before issuing a comprehensive live test, reconcile every feature reported **ready for a live test** in any development chat or PR using the [QA inclusion register](qa/LIVE-TEST-INCLUSION-REGISTER.md). A previous `deferred_pending_runtime` classification is not permission to skip it again. Attempt inclusion, document technical blockers, and obtain owner approval for a partial test if necessary. This sheet describes tests of what is shipped, not permission to omit ready features.

Use [the live feature checklist](FEATURE-CHECKLIST.md) for the whole project and [the source registry](../manifest/feature-checklist.json) for stable IDs. This page is a **short, prioritized exercise sheet**, not a claim that every feature is in the downloaded ZIP. Check [BUILD-MANIFEST.json](../manifest/integration-sources.json) and Mods screen to confirm which source modules actually shipped.

**Environment:** one freshly imported FULL-INTEGRATION-UNSTABLE Prism ZIP, Minecraft 1.21.1 / NeoForge 21.1.265, Java 21, a new throwaway world, plus an existing configured tester Prism instance for preference comparison. Keep old survival worlds **closed** during these tests. Record the ZIP run ID and immutable SHA before testing. If a feature is not in the ZIP, write **NOT BUILT**, not FAIL or PASS. A successful CI server boot is a *prerequisite*, not proof of client behavior.

| Priority | Feature ID(s) | Practical acceptance task |
| ---: | --- | --- |
| 1 | `build.prism`, `build.modlist` | Confirm main menu, create/rejoin fresh world, inspect client logs, open mod list and compare manifest against actual loaded mods |
| 2 | `build.profile` | Test old-Prism keybind import, GUI scale, volume, restart and next new QA import; don't require running old/world saves |
| 3 | `quests.categories`, `quests.localization` | Visit all five FTB groups; sample 45 chapters/179+ quest descriptions; no `undefined`, raw keys or overlapping paths |
| 4 | `quests.starter_path`, `quests.starter_items`, `quests.pouch` | Pick a starter, claim once, check Akashic Tome, two cooked beef and Improvable access; break first log; relog and prevent duplicate gifts |
| 5 | `quests.rewards`, `quests.evilcraft` | Claim several new humorous/useful rewards, dark gems -> Crushed Dark Gem; verify only Cooking Pot quest intentionally earns a bowl; reject XP bottles |
| 6 | `shop.button`, `shop.coin`, `shop.ores` | Open shop button, buy a cheap item, redeem consuming copper contract, check coins, see 18 vanilla ore offers and test non-abusive resale |
| 7 | `qol.travel`, `qol.travel_limits` | Non-OP `/sethome`, `/home`, `/listhomes`, `/delhome`, `/rtp`; inspect cooldown, claims, reconnect and limit 1 |
| 8 | `qol.comforts` | **Only if installed/pinned:** sleep in bag without setting spawn; use hammock to skip day to night; check vampire day/night behavior |
| 9 | `materials.neodust`, `materials.occultism` | Raw ores processed via Occultism and Neo Vitae should yield the canonical dust and smelt into the same legitimate metal |
| 10 | `materials.mahou`, `materials.polymorph` | Catalyst Exchange consumes correct mana, refuses insufficient mana, preserves partial stacks, never duplicates; inspect recipe choices in JEI |
| 11 | `materials.silver`, `materials.ores` | Silver equipment single canonical set; inspect protection/damage, common silver/lead ore and dust smelting from each source |
| 12 | `monk.sunset`, `monk.tree`, `monk.step` | Start neutral. Natural westward sunset initiation with empty gear; inspect native faction tree and Step Assist +0.5; relog; try gear restrictions |
| 13 | `form.vampire_acquire`, `form.blood` | Test ordinary infection separately from Lilith ascension; drink blood via JAWR/Vampirism; spend ability blood and take damage; inspect meter |
| 14 | `form.hunger`, `form.cure`, `form.sunlight` | Check hunger over time, full cure, real sun versus Vampire Forest shade; avoid mixing pre- and post-ascension saves |
| 15 | `skills.backend`, `skills.core`, `skills.mxp` | If Tyz actually pinned and Pufferfish removed, inspect 99-rank tree and admin XP test. MXP is NOT assumed live until dispatcher verified |
| 16 | `world.tectonic`, `world.dungeons` | Scout fresh generated structures, Neo Vitae Mines and Botania Gaia path; confirm no structure softlock or visually broken sky |
| 17 | `end.projecte_start`, `end.projecte_trophies`, `end.projecte_high` | Confirm **three verified boss-associated center ingredients** craft the Stone in BOTH old orientations, diamond fails, original eight outer ingredients stay; Red MK3 direct/upgrade and higher direct Power Flowers consume a Chaos Shard. Audit EMC and Matter Upgrader bypasses; source-stage is NOT a gameplay PASS |
| 18 | `end.emc_every_item`, `end.emc_balancing` | Export same-build loaded item registry + post-mapping ProjectE EMC, run strict zero-missing audit except cheap glass panes; check low-cost cycles before claiming coverage |
| 19 | `release.multi`, `shop.multiplayer` | After core singleplayer cases pass, connect a second player; verify separate starter inventory/Monk rites, team coins, save/restart |

## Suggested test report

For each row write:

- **Build**: GitHub Actions URL + BUILD-MANIFEST `integration_commit` + optional world seed.
- **Case**: the stable feature ID(s), expected behavior and precise actions.
- **Result**: `PASS` (all stated checks), `PARTIAL`, `FAIL`, `NOT BUILT` or `BLOCKED`.
- **Evidence**: screenshot, exact item/recipe ID, client `logs/latest.log` excerpt or console output.
- **Reproduction**: player state, time/dimension, nearby machines/bosses, commands and any changes to the default config.
- **Regression**: whether this exact case worked in the prior immutable QA build.

**Never update the master checklist's `player_pass` state from this worksheet automatically.** After a successful real-world run, update the relevant row via `python3 scripts/feature_checklist.py mark FEATURE_ID player_pass --evidence PATH` and regenerate the report; other rows stay open.

## Why not force everything into one test run?

A live test must not claim to exercise code absent from its assembled artifact, and no important save may be used as the test fixture. **However, any feature previously reported ready for live testing must be attempted for inclusion before the next comprehensive live-test build**. Record incompatible dependencies and merge conflicts as explicit blockers rather than repeating stale deferrals. The master checklist and [inclusion register](qa/LIVE-TEST-INCLUSION-REGISTER.md) control scope; this sheet provides the short player exercise order for the resulting tested artifact.
