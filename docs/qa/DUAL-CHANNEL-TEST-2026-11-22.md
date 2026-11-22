# November 22 — unified Unstable + Experimental QA

**Status:** planned, no new in-game PASS. **A:** Codex offline; **B:** paste-ready in-game commands; **C:** manual client/setup/play. Follow [QA execution standard](QA-EXECUTION-TYPES.md), [historical command syntax only](UNSTABLE-QA-2026-11-17.md) and [live inclusion policy](LIVE-TEST-INCLUSION-REGISTER.md). Record evidence against the actual installed BUILD-MANIFEST, not a moving branch. This is a **TMM-owned QA checklist**, not a certification suite for upstream mods.

## Exact builds and mandatory identity check

| Channel | Source / package | What is actually proven |
| --- | --- | --- |
| **U — Unstable control** | Immutable *assembled/server-tested* snapshot **c78479f52f9592ca1f139a6b874d630422c34042** (from repaired integration source **2373d50732866e460dc9e9454f7187f9045e1cdc**, tag **integration-qa/44980284571-1**). Code on the rolling branch has advanced and is intentionally NOT selected. | [Server smoke PASS #44980284571](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/44980284571). **Matching 222-mod Unstable Prism PACKAGE PASS:** [run #44995616598](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/44995616598), direct-import artifact [14742659598](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/44995616598/artifacts/14742659598), SHA-256 `6270f7b1b3699b2a5d86337cb45c68eccb6a1d1d795d6d959676be003ec6544f`; no new smoke run. Published rolling Unstable remains an older Nov 17/205-pin generation; never use it as the 222-mod control. |
| **E — Experimental four updates** | Exact updated, server-tested Packwiz commit **beb5682b77056fbf54830aa671fa0efe6e2eaaa8**, immutable QA tag **qa-update-prism/44985722108-1**. [Prism package run #44985722108](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/44985722108), artifact **Too-Much-Magic-Four-Mod-Update-Prism-EXPERIMENTAL** ID **14739742394**, ZIP SHA-256 **bfa2195fb414e72c77624687af8ea3ddd368d10021bb80dadb410e9a581f16ee**. | Server PASS and Prism PACKAGE PASS. **No client launch or gameplay PASS.** Ars Elemental beta not approved for promotion. |

The draft experiment PR #461 head contains trial tooling but **not the updated pins**. Test the frozen E commit above, never a source-only PR head. This document does **not** authorize another full smoke or Prism build. Use only the [successful direct Prism control artifact #14742659598](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/44995616598/artifacts/14742659598) from package-only run #44995616598; its BUILD-MANIFEST pins the frozen U assembled snapshot. Never substitute the old rolling release. All client/gameplay cases are **NOT RUN** until the owner launches Prism.

### The complete experimental pin delta

| Mod | U | E | TMM-owned compatibility boundary — not a native feature test |
| --- | --- | --- | --- |
| Neo Vitae | 1.1.53 | **1.1.54** | Our canonical dust mapping, Mahou circle adapter and MXP/EV spend bridge, **only if shipped** |
| EvilCraft | 1.2.100 | **1.2.102** | Our EvilCraft-canonical blood conversion/containers with JAWR, Hexerei and other TMM bridges |
| Goety | 3.2.2 | **3.2.3** | Offline API/pin compatibility; **no dedicated manual Goety test** unless TMM lich/path integration breaks or an attributable bug is reported |
| Ars Elemental | 0.7.10.4 | **0.7.11.3 beta** | One focused Ars/Progression spell-to-MXP integration and known server-crash boundary; beta is the exception to release-version trust |

Actual frozen snapshot also updates Economy's exact-EvilCraft-name compile allowlist and generated Packwiz/index/lock/provenance files. No other external mod pin is supposed to change.

## Deduplication rule

**Trust released upstream mods by default.** Do **not** ask the player to prove an upstream mod's normal crafting, machines, entities, structures, spells, JEI pages, HUD or built-in mechanics work. Test (1) code/configuration/data TMM authors or overrides, (2) our integrations between mods, (3) pack startup and data persistence at the integration boundary, and (4) a narrowly reproducible reported upstream/TMM bug, documenting its link and affected version. A **beta/prerelease** may get a short targeted compatibility check; this is not a blanket native-feature test. **BOTH:** only verify client import/launch, world creation/rejoin, installed versions and integration errors. **U:** run TMM-owned acceptance once. **E Δ:** retest only affected TMM integration seams and the beta boundary; use U as a control if E fails or differs. **CONDITIONAL:** verify the feature was shipped in the exact artifact; absent work is **NOT BUILT**. Green A or an upstream release label is not a client PASS.

## A — automated Codex CLI

New runner: [scripts/qa_compare_channels.py](../../scripts/qa_compare_channels.py). It verifies two clean **immutable 40-hex** checkouts, matches Packwiz index and mod-pin SHA-256 metadata, emits the exact changed-pin diff, runs approved offline Python validators on U, and reruns on E **only where watched files/inputs differ**. Reused evidence is labeled **REUSED SOURCE CHECK**; it is not a new E runtime PASS.

Run on the runner's feature branch (or after merge) with both immutable commits locally available. These are disposable, detached worktrees. Windows PowerShell can use the same Python command on one line; use Java 21 if compiling modules.

~~~bash
git worktree add --detach ../tmm-u c78479f52f9592ca1f139a6b874d630422c34042
git worktree add --detach ../tmm-e beb5682b77056fbf54830aa671fa0efe6e2eaaa8
python tests/test_qa_compare_channels.py
python scripts/qa_compare_channels.py --unstable ../tmm-u --unstable-sha c78479f52f9592ca1f139a6b874d630422c34042 --experimental ../tmm-e --experimental-sha beb5682b77056fbf54830aa671fa0efe6e2eaaa8 --require-four-updates --run-a --report ../tmm-two-build-a-results.json
~~~

For optional first-party Gradle compile/unit checks, add **--build-modules**. Omit **--run-a** for a harmless plan and diff. The runner **never** launches Minecraft, donor assembly, server smoke or Prism packaging. Normal A validators operate locally; optional --build-modules may fetch Gradle and compile-classpath dependencies. Compare local client mod-folder bytes and bundled JARs separately with existing verify_qa_inventory.py against the matching source checkout, using actual first-party JAR paths as extras. A correct pin hash is not proof that an installed JAR matches.

| A IDs | Scope | Experimental reuse |
| --- | --- | --- |
| A01–A03 | Packwiz validation, strict pin roster and integration preflight | Rerun on changed metadata/manifest; note detached-branch guard separately |
| A04–A05 | Progression/MXP and quests/localization | Rerun for changed Ars/Goety dependencies and watched sources |
| A06–A08 | Canonical blood, materials/Neo dust and silver | Rerun changed EvilCraft/Neo cases; reuse unchanged silver |
| A09–A11 | Newbie/MXP, boss gating and current dragon food fixtures | No assumed food-engine rewrite; recheck changed dependencies only |
| A12–A13 | Offline Prism packaging fixture, optional first-party builds | Recheck E compile ABI for four new JAR filenames, especially EvilCraft in Economy |
| A14 — offline **design-input inventory**, no player QA | Count reachable Astral Sorcery perks and its configured hard cap **only to implement TMM's planned purchasable perk-point feature**. Record method, source and raw count; this does not test whether Astral Sorcery's native perk tree functions |
| A15 — A artifact audit | Inspect BUILD-MANIFEST, final source SHA/tag, actual local JAR hashes, bundled first-party JARs, client logs and already existing server evidence. No new Minecraft process |

## One B/C checklist — TMM-authored behavior and integration only

**Do not test upstream release functionality in isolation.** Keep a mod's native behavior as a *setup step* only when necessary to exercise our own hook, with the assertion explicitly on TMM's result. Owner-reported or upstream bugs can restore one narrowly scoped case with an issue link and exact version. B/C observations are distinct from offline A checks.

| Case / type | U — test TMM once | E — only changed TMM seam |
| --- | --- | --- |
| **T01 C BOTH** | [ ] Import the exact frozen U package; confirm our four first-party JARs are loaded and loader/integration logs have no TMM errors | [ ] Import E separately; verify exact four changed pins and no newly failing TMM mixins/registry hooks |
| **T02 C BOTH** | [ ] One disposable world: join, save, rejoin; confirm **TMM** player state, injected recipes/registry and KubeJS initialization survive | [ ] Same minimal TMM state/world persistence and startup check; no general mod functionality tour |
| **T03 B U** | [ ] TMM Newbie/Regular state, MXP denial/graduation, personal quest, configured FTB travel limits and Tyz wallet persistence | E one MXP/Progression hook only if a changed pin can affect it (T20) |
| **T04 B/C U** | [ ] TMM Monk initiation, armor/fist restrictions, owned powers, skill purchases and MXP attribution | No repeat without a changed dependency or issue |
| **T05 C U** | [ ] Our TreeChop XP/progress + Jade adapter, Monk Bedrock Rest/glide **where TMM implements these** | No repeat without a changed dependency or issue |
| **T06 B/C U** | [ ] TMM Hunter initiation/specialty, XP attribution, silver bonus, custom Bio-Mutagen recipe/mutation persistence | Skip native potion/Brewing Stand tests outside our recipe/handler |
| **T07 C U** | [ ] Our JAWR acquisition vetoes, blood healing/containers, vampire pool/HUD integration before/after relog (**previous player-reported issue**), TMM lich/dracolich exclusivity and death priority | E retest only changed EvilCraft-canonical blood bridge (T17); Goety lich integration **only if an actual TMM regression is observed** |
| **T08 C CONDITIONAL U** | [ ] Only shipped **TMM diet/recipe override** behavior: Sea fish/kelp/sushi, Forest enchanted carrot/sourceberries, and our seasoned-fish handling. Ritual-only choice config if shipped | Unfinished food-engine redesign = NOT BUILT; **do not test native Dragon Survival Sources of Magic, Hot Dragon Rod or Spearman crafting** |
| **T09 C U** | [ ] Our authored starter kit, quests, Ars/Alcohol localization, one-time rewards, quest-shop coins and custom ore recipes | E only an Ars-dependent **TMM** quest/recipe if dependency changed |
| **T10 C U** | [ ] **Our** Wandering Trader stock/blacklist, vendetta and No Soliciting spawn restrictions; custom Bountiful contracts only if shipped | No vanilla/upstream trader mechanics sweep |
| **T11 C U** | [ ] **Our** Gateways custom wave definitions, Chaos Bunny reward/trophy and scripted loot; custom worldgen/biome overrides **only if authored by TMM** | **No native Goety mobs/structures or Tectonic/YUNG/BetterEnd/dimension exploration**; bug-driven exception only |
| **T12 C U** | [ ] Our ProjectE trophy recipes/bypass prevention, Chaos Shard requirement, gated CC Eldritch/Replication/EMC rules and custom shop interactions **where installed** | No native ProjectE or Replication operation checks |
| **T13 C U** | [ ] **Our** Mahou-to-Neo dust exchange adapter and altered Thermalily output/cooldown; custom FE/MXP conversion only if shipped | Do not test native Mahou Catalyst, Botania or Psi machinery on its own |
| **T14 C U** | [ ] Our Prism QA options scripts, remap preservation, customized default-key rules and TMM-specific Jade/Curios/FTB/Progression UI integration | E verify separate QA instance identity, no preference leakage |
| **T16 C U** | [ ] Two players only for **our** path, mode, team quest/coin, MXP attribution, per-player permissions and save-boundary isolation | Multiplayer E only where an Ars Elemental beta + TMM hook exposes a reproducible server problem (T20) |
| **T17 C E Δ** | Use U EvilCraft bridge as control **only if E differs** | [ ] **Our** EvilCraft blood canonicalization with JAWR/Hexerei containers, healing, transfer rules and duping protection; **skip native EvilCraft machines/guide/recipes** |
| **T18 C E Δ** | Use U Neo dust adapter as control **only if E differs** | [ ] **Our** Neo Vitae dust recipes/tags, Mahou circle compatibility, EV/Spiritus-to-MXP donor rules if shipped; **skip native Athanor/Tabula/Ara Vitae rituals** |
| **T20 B/C E Δ beta** | U stable Ars baseline only if beta integration fails | [ ] A representative Ars Elemental beta spell exercising **our** spell-to-MXP/Ars compatibility bridge and any known server-crash reproduction; **no all-glyph/spell/functionality survey** |
| **T22 C CONDITIONAL** | [ ] **Only if actually shipped:** our blaze/fire, stew/foliage tweaks; Prefab restrictions; bag-nesting guard; personal EMC/Jade tooltip; custom enchant filter; Friendship Flower; Bountiful custom board; Astral perk-point purchases | Mark NOT BUILT for source-only designs and PRs; do not test the underlying release mods |

**Removed player-test cases:** old T15 (native Comforts/JER/Colorful Hearts/cooking functionality), T19 (native Goety mob/structure/summoning functionality) and T21 (duplicate E spell/MXP/recipe/blood/save checks already covered by T02/T09/T17/T18/T20). Also removed native-only subchecks from T07, T08, T11, T13, T14, T17 and T18. IDs remain stable for evidence history; removed IDs are **not mandatory coverage**.

### Verified source-era command fixtures (operator only where indicated)

Confirm Tab completion in this installed version; do not invent commands. Run one line at a time on a disposable player, keeping world keepInventory false:

~~~mcfunction
/gamemode survival
/gamerule keepInventory false
/tmmprogress list
/tmmnewbie status
/tmmmxp status
/tmmtransform status
~~~

To compare synthetic Newbie versus graduated XP on a throwaway OP profile (not proof of natural quest completion):

~~~mcfunction
/tmmnewbie admin newbie @s
/tmmmxp test fixed 1000
/tmmnewbie admin graduate @s
/tmmmxp test fixed 1000
/tmmmxp test ordinary 1000
/tmmmxp status
~~~

Use historical B01–B10 [command syntax](UNSTABLE-QA-2026-11-17.md) **only** for the TMM-owned cases above. Its upstream-mod and native-behavior tasks are superseded by this scope; do not restore them just because they appear in an older list.

### Explicit NOT BUILT / future readiness checks

New first-class **Newbie and Normie Paths** and MXP-independent Core tree semantics; redesigned diet/recipe tags; ProjectE/Jade per-player hidden-value tooltip PR #464; personal enchanting filter PR #471; Friendship Garden potion relay; Monk mob detection reduction; Bare Zerker and other new paths may exist as designs or separate unmerged code. Do not presume their inclusion. Keep their A and B/C acceptance obligations open until the *actual* install proves availability. Current U baseline's established Regular behavior is the expected test, not an invented completed refactor.

## Evidence and pass gate

For **each** tested T case record: channel, immutable BUILD-MANIFEST source SHA + ZIP checksum, B/C, action and player state, expected vs observed, PASS/FAIL/PARTIAL/BLOCKED/NOT BUILT/NOT RUN, screenshot/log location and exact versions; enter in the per-build QA results ledger. Report A JSON separately. A serious E client/server integration crash, TMM state/data loss, **TMM bridge** duplication, broken canonical blood economy, or beta Ars spell crash affecting TMM blocks E promotion; preserve logs and reproduce on U if possible. An unmodified released mod's native feature is not a mandatory QA gate absent a reproducible linked bug. Neither a server PASS nor green A output approves beta Ars Elemental or replaces the unstable ZIP.
