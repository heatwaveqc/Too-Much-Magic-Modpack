# November 22 — unified Unstable + Experimental QA

**Status:** planned, no new in-game PASS. **A:** Codex offline; **B:** paste-ready in-game commands; **C:** manual client/setup/play. Follow [QA execution standard](QA-EXECUTION-TYPES.md), [historical B/C command recipes](UNSTABLE-QA-2026-11-17.md) and [live inclusion policy](LIVE-TEST-INCLUSION-REGISTER.md). Record evidence against the actual installed BUILD-MANIFEST, not a moving branch.

## Exact builds and mandatory identity check

| Channel | Source / package | What is actually proven |
| --- | --- | --- |
| **U — Unstable control** | 222-mod repaired integration base **2373d50732866e460dc9e9454f7187f9045e1cdc**. Current integration head **be5d7c90c0f1f76b260b4e43a6c3b9ca07585d73** adds QA documentation only. | [Server smoke PASS #44980284571](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/44980284571). **A matching 222-mod Unstable Prism ZIP has not been established.** Published rolling Unstable is still an older Nov 17 / 205-pin generation. Do not use it as an exact 222-mod control. |
| **E — Experimental four updates** | Exact updated, server-tested Packwiz commit **beb5682b77056fbf54830aa671fa0efe6e2eaaa8**, immutable QA tag **qa-update-prism/44985722108-1**. [Prism package run #44985722108](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/44985722108), artifact **Too-Much-Magic-Four-Mod-Update-Prism-EXPERIMENTAL** ID **14739742394**, ZIP SHA-256 **bfa2195fb414e72c77624687af8ea3ddd368d10021bb80dadb410e9a581f16ee**. | Server PASS and Prism PACKAGE PASS. **No client launch or gameplay PASS.** Ars Elemental beta not approved for promotion. |

The draft experiment PR #461 head contains trial tooling but **not the updated pins**. Test the frozen E commit above, never a source-only PR head. This document does **not** authorize another full smoke or Prism build. If the fresh 222-mod U client package is unavailable, U client tests are **BLOCKED**, not silently replaced by old release acceptance.

### The complete experimental pin delta

| Mod | U | E | Main regression risk |
| --- | --- | --- | --- |
| Neo Vitae | 1.1.53 | **1.1.54** | Athanor idle, EV/Spiritus, Tabula, Ara Vitae, rituals |
| EvilCraft | 1.2.100 | **1.2.102** | Blood storage/interoperability, recipes, guides/JEI |
| Goety | 3.2.2 | **3.2.3** | Natural mobs/structures, summoning, Tectonic/YUNG |
| Ars Elemental | 0.7.10.4 | **0.7.11.3 beta** | Elemental casting, Ars addons, server crash regression |

Actual frozen snapshot also updates Economy's exact-EvilCraft-name compile allowlist and generated Packwiz/index/lock/provenance files. No other external mod pin is supposed to change.

## Deduplication rule

**BOTH:** only client import/launch, world creation and rejoin, loaded versions, and logs in each build. **U:** full normal acceptance once. **E Δ:** the four changed mods and a very small core cross-mod regression; use U as a control only if E has a failure or ambiguity. **CONDITIONAL:** first verify it shipped in the exact installed artifact; absent source-ready PR work is **NOT BUILT**. Do not replay previously accepted unaffected mechanics or confuse A source PASS with in-game PASS.

## A — automated Codex CLI

New runner: [scripts/qa_compare_channels.py](../../scripts/qa_compare_channels.py). It verifies two clean **immutable 40-hex** checkouts, matches Packwiz index and mod-pin SHA-256 metadata, emits the exact changed-pin diff, runs approved offline Python validators on U, and reruns on E **only where watched files/inputs differ**. Reused evidence is labeled **REUSED SOURCE CHECK**; it is not a new E runtime PASS.

Run on the runner's feature branch (or after merge) with both immutable commits locally available. These are disposable, detached worktrees. Windows PowerShell can use the same Python command on one line; use Java 21 if compiling modules.

~~~bash
git worktree add --detach ../tmm-u 2373d50732866e460dc9e9454f7187f9045e1cdc
git worktree add --detach ../tmm-e beb5682b77056fbf54830aa671fa0efe6e2eaaa8
python tests/test_qa_compare_channels.py
python scripts/qa_compare_channels.py --unstable ../tmm-u --unstable-sha 2373d50732866e460dc9e9454f7187f9045e1cdc --experimental ../tmm-e --experimental-sha beb5682b77056fbf54830aa671fa0efe6e2eaaa8 --require-four-updates --run-a --report ../tmm-two-build-a-results.json
~~~

For optional first-party Gradle compile/unit checks, add **--build-modules**. Omit **--run-a** for a harmless plan and diff. The runner **never** invokes Minecraft, network downloads, donor assembly, server smoke or Prism packaging. Compare local client mod-folder bytes and bundled JARs separately with existing verify_qa_inventory.py against the matching source checkout, using actual first-party JAR paths as extras. A correct pin hash is not proof that an installed JAR matches.

| A IDs | Scope | Experimental reuse |
| --- | --- | --- |
| A01–A03 | Packwiz validation, strict pin roster and integration preflight | Rerun on changed metadata/manifest; note detached-branch guard separately |
| A04–A05 | Progression/MXP and quests/localization | Rerun for changed Ars/Goety dependencies and watched sources |
| A06–A08 | Canonical blood, materials/Neo dust and silver | Rerun changed EvilCraft/Neo cases; reuse unchanged silver |
| A09–A11 | Newbie/MXP, boss gating and current dragon food fixtures | No assumed food-engine rewrite; recheck changed dependencies only |
| A12–A13 | Offline Prism packaging fixture, optional first-party builds | Recheck E compile ABI for four new JAR filenames, especially EvilCraft in Economy |
| A14 — A manual audit | **Astral Sorcery perk count and hard cap:** count distinct reachable perk definitions from exact installed version/JAR/source, compare config hard cap, record raw count and method. No guessed total and no claim new purchasable perk-point feature is installed |
| A15 — A artifact audit | Inspect BUILD-MANIFEST, final source SHA/tag, actual local JAR hashes, bundled first-party JARs, client logs and already existing server evidence. No new Minecraft process |

## One B/C checklist — U baseline plus E focused difference

Check each box only with observations, exact build and log/screenshots. Use separate disposable worlds and, for modes/forms, separate fresh characters.

| Case / type | U — do ONCE | E — only what differs |
| --- | --- | --- |
| **T01 C BOTH** | [ ] Import true 222-mod U ZIP, Java 21, menu, four first-party mods, clean logs | [ ] Import **single-level** E ZIP directly in Prism; verify changed mod versions, logs/mixins |
| **T02 C BOTH** | [ ] Fresh world create, save, exit/rejoin, JEI/recipe init, no KubeJS failures | [ ] Fresh E world create/rejoin, no registry/mixin/save errors |
| **T03 B U** | [ ] Fresh Newbie read-only status and permissions, MXP refusal; graduation via personal quest versus OP synthetic fixture, one-home/RTp limits, Tyz wallet, relog | One selected MXP/mode check only in E (T22) |
| **T04 B/C U** | [ ] Monk natural sunset enrollment, gear restrictions, barehand logs, fortune/silk, powers and K GUI purchase, rank/XP | Skip unchanged |
| **T05 C U** | [ ] TreeChop multi-block progress bar/Jade visibility and completion XP; Bedrock Rest and glide if truly unlocked | Skip unchanged |
| **T06 B/C U** | [ ] Hunter poison enrollment, specialty, last-hit XP, Looting/silver, Bio-Mutagen nine ingredients and Brewing Stand-only potion, saved mutation state | Skip unchanged |
| **T07 C U** | [ ] JAWR vampire Lilith blood acquisition: immediate pool/HUD before and after relog, blood healing, cure and sun/shade; Archlich/Dracolich death anchors and exclusivity | E retests only affected EvilCraft blood bridge (T18) |
| **T08 C U** | [ ] Dragon ritual-only choice, fish/kelp/sushi for Sea, enchanted carrot/sourceberries for Forest, seasoned fish variants, three Sources of Magic | Skip new food-engine design (unfinished) and waived Hot Dragon Rod/Spearman upstream quirks |
| **T09 C U** | [ ] Five quest groups, starter Tome/two beef, first-log/Nether stem, localized Ars/Alcohol chapters, single claims, shop coins/ore stock, no ghost recipes | E one representative quest/JEI claim only |
| **T10 C U** | [ ] Trader six guarantees/four premium categories, banned ring, natural No Soliciting and vendetta, Bountiful board/decrees if actually installed | Skip unchanged |
| **T11 C U** | [ ] Gateways arena/loot, natural Tectonic/YUNG/BetterEnd/dimension/rare ore worldgen, seven added mobs | E **Goety** changed worldgen/summons (T19); don't reexplore all dimensions |
| **T12 C U** | [ ] ProjectE three trophy centers/orientations, MK3+ Chaos Shard, bypasses, CC Eldritch/Replication progression and EMC export if available | Skip unchanged |
| **T13 C U** | [ ] Mahou Catalyst native mana/count; Thermalily lava rig output and cooldown, FE conversion | Skip unless E machine cross-mod recipe is broken |
| **T14 C U** | [ ] KeySets/Default Options, vanilla key defaults, Jade/Curios/FTB HUD and K interface, saved remaps; check prior invalid-key log flood | E verify separate QA profile, no leaked personal preferences |
| **T15 C U** | [ ] Comforts, JER, Colorful Hearts, Eternal Steak and installed cooking systems | Skip unfinished dietary refactor |
| **T16 C U** | [ ] Two players: personal graduation/path/forms/XP, team coin/quests, restart/save and permission boundaries | E multiplayer only for Ars Elemental crash control |
| **T17 C E Δ** | EvilCraft 1.2.100 reference only if E fails | [ ] **EvilCraft 1.2.102:** capture/store/transfer blood, stands, guide+JEI, blood-powered recipe, JAWR/Hexerei containers and no duplication |
| **T18 C E Δ** | Neo Vitae 1.1.53 reference only if E fails | [ ] **Neo Vitae 1.1.54:** Ara Vitae/Tabula/Hellfire/Athanor output, resource draw EV/Spiritus, ritual, idle and unload/rejoin behavior, dust integration |
| **T19 C E Δ** | Goety 3.2.2 reference only if E fails | [ ] **Goety 3.2.3:** naturally generated mob/structure in fresh chunks, summon/owner/combat and worldgen coexistence |
| **T20 C E Δ** | Ars Elemental 0.7.10.4 reference only if E fails | [ ] **Ars Elemental 0.7.11.3 beta:** glyph/focus/JEI, entity/block spells, mana use, relog, actual multiplayer/dedicated casting if available (server-crash regression) |
| **T21 C E Δ** | Control known-good recipe, spell, player and quest state from U | [ ] E representative spell->MXP, quest claim, JEI recipe, blood container and save/reload; expand only if changed behavior |
| **T22 C CONDITIONAL** | [ ] **Only if in exact built package:** QoL blaze rod/fire, mushroom stew stacks, foliage attacks; Prefab houses; cross-bag prevention; player EMC tooltip/Jade; six-book enchant filter; Friendship Flower; Bountiful custom board; Astral extra perk-point grants | Mark NOT BUILT unless actual runtime code/config and pins shipped; source-ready and draft PRs are **not** acceptance |

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

Use source-verified Monk/Hunter/merchant/structure command sequences in [the earlier B01–B10 playbook](UNSTABLE-QA-2026-11-17.md) as appropriate; repeat nothing merely for coverage theater.

### Explicit NOT BUILT / future readiness checks

New first-class **Newbie and Normie Paths** and MXP-independent Core tree semantics; redesigned diet/recipe tags; ProjectE/Jade per-player hidden-value tooltip PR #464; personal enchanting filter PR #471; Friendship Garden potion relay; Monk mob detection reduction; Bare Zerker and other new paths may exist as designs or separate unmerged code. Do not presume their inclusion. Keep their A and B/C acceptance obligations open until the *actual* install proves availability. Current U baseline's established Regular behavior is the expected test, not an invented completed refactor.

## Evidence and pass gate

For **each** tested T case record: channel, immutable BUILD-MANIFEST source SHA + ZIP checksum, B/C, action and player state, expected vs observed, PASS/FAIL/PARTIAL/BLOCKED/NOT BUILT/NOT RUN, screenshot/log location and exact versions; enter in the per-build QA results ledger. Report A JSON separately. A serious E launch crash, data loss, dupe, worldgen error, broken blood economy, or server-only Ars spell crash blocks E promotion; preserve logs and reproduce on U if possible. Neither a server PASS nor green A output approves beta Ars Elemental or replaces the unstable ZIP.
