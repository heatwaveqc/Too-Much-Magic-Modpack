# Next full-integration client test pass — practical priority order

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
| 16a | `world.jawr_wild_vampires` (**NEXT-CANDIDATE TARGET**) | **Only if new pack-owned JAWR spawn config is included:** confirm `witchery:vampire` with `/summon witchery:vampire ~ ~ ~` (entity/ownership smoke), then verify truly natural nighttime Overworld spawns, singleton groups, human/Monk/Hunter hostility, daylight/relog behavior, no inherited Vampire Child owner, and observed rate toward ~1 vampire per 20 naturally spawned zombies. Record spawn counts, biome and logs; if no config shipped, mark **NOT BUILT**. |
| 17 | `end.projecte_start`, `end.projecte_trophies`, `end.projecte_high` | Confirm **three verified boss-associated center ingredients** craft the Stone in BOTH old orientations, diamond fails, original eight outer ingredients stay; Red MK3 direct/upgrade and higher direct Power Flowers consume a Chaos Shard. Audit EMC and Matter Upgrader bypasses; source-stage is NOT a gameplay PASS |
| 18 | `end.emc_every_item`, `end.emc_balancing` | Export same-build loaded item registry + post-mapping ProjectE EMC, run strict zero-missing audit except cheap glass panes; check low-cost cycles before claiming coverage |
| 19 | `release.multi`, `shop.multiplayer` | After core singleplayer cases pass, connect a second player; verify separate starter inventory/Monk rites, team coins, save/restart |

## Next-candidate lightweight config gate: JAWR wild vampires

This item is **queued for the next config integration**, not retroactively included in the in-progress/frozen client ZIP. See [the exact design and QA target](hunter/AFFINITY-AND-ENCHANTMENT-DEVELOPMENT-TARGET.md#next-build-config-target-wild-jawr-vampires).

1. **Source/static:** check the exact pinned JAWR 0.5.39 `witchery:vampire` spawn placement, existing mod spawn rules, owner/master initialization and the 1.21.1 NeoForge `add_spawns` JSON schema. Config-first; fallback to Java only if necessary, with a new implementation target rather than hidden test-build code.
2. **Fast command test:** in a new disposable world use `/summon witchery:vampire ~ ~ ~`; test ordinary unowned entity viability, hostility, sunlight, relog and logs. This does not validate natural spawn selection.
3. **Actual spawn and rate test:** after the next build includes the datapack modifier, count naturally spawning `witchery:vampire` versus ordinary zombies on repeat eligible nights in several Overworld biomes. Target roughly **1:20**; do not equate spawn weight directly to a percent. Require only singleton wild vampires and no accidental ownership of player-created vampire children.

If the newly built ZIP does not contain the modifier, report **NOT BUILT**. A successful `/summon` alone is **PARTIAL**, not a natural-spawn PASS.

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

The experimental branch contains some source-only work that has not been pinned or enabled, notably the Tyz form/MXP network and some quest drafts. Testing an absent mod or applying unverified transformations to an important save creates false regressions. The master checklist is comprehensive; this sheet is the efficient sampling order for whatever actually shipped.
