# Next test — Codex-executable three-tier checklist

**Scope:** next comprehensive test intake, readiness checks and live-test commands. This document does not itself authorize a build or full smoke.  
**Scope checked:** `test/full-integration`, November 16, 2026 (Arizona).  
**Target:** Minecraft 1.21.1 · NeoForge 21.1.265 · Java 21.  
**Execution status of THIS document:** instructions only; no tests pass merely because this checklist exists.  
**Live evidence/handoff:** [CURRENT-TEST-STATUS.md](CURRENT-TEST-STATUS.md).  
**Mandatory policy:** [AGENTS.md](../../AGENTS.md) — **no full smoke or server boot without explicit authorization for that run**.

This is the actionable successor to the earlier [19-step client worksheet](../NEXT-INTEGRATION-TEST-PASS.md), supplemented by the original [112-feature acceptance register](../../manifest/feature-checklist.json). Neither source-only work nor a passed server boot may be called an in-world PASS.

## Start: Codex instructions

1. Read `AGENTS.md`, `docs/qa/README.md`, this checklist, `manifest/integration-sources.json`, `manifest/mods.json`, `index.toml`, `docs/TEAM-LAPEN-TEST-REMOVAL-2026-11-15.md`, and the **current** `compat/progression/` / `compat/transformations/` resources. Check branch HEAD: `git rev-parse HEAD`; record dirty state: `git status --short`.
2. Treat the exact checkout, **not this dated snapshot**, as the authority. The 2026-11-16 retirement integration removes TeamLapen Vampirism, Werewolves and Vampire's Delight. The next QA compiles *two* first-party JARs: `tmm_progression` and a **repaired JAWR-only** `tmm_transformations`. The old `tmm_monk` faction JAR is excluded.
3. The integrated standalone Monk JSON currently includes 30 skills, rank-20 cumulative XP **14,300**, 16 optional milestone entries and Bedrock Rest. The integrated Hunter JSON includes the potion Trial of Venom, specialties, mutations, and silver_hunter. Both are unaccepted in-world. **Re-check these numbers on each commit.**
4. `tmm_progression` command registration currently uses `StringArgumentType.word()` for namespaced IDs and does **not** register `/tmmprogress admin xp` in the combined Hunter command file. Mark associated in-game commands **BLOCKED** until the source/parser/command contract is repaired and actually tested. Do not silently simplify or invent command syntax.
5. **Before the next comprehensive live build, reconcile the [mandatory ready-feature register](LIVE-TEST-INCLUSION-REGISTER.md).** Search prior development-chat handoffs and PRs for every reported-ready feature, including unmerged branches. The earlier retirement-only deferrals (B16 BetterEnd, B17/B18 YUNG/locators, B19 Gateways, B20 trader, Thermalily/ProjectE, MXP growth and others) are **historical, not an approved next-test omission**. Attempt runtime inclusion for every ready feature, record an exact blocker if an attempt fails, and request an owner decision if a narrower test is necessary. Do not claim a feature shipped unless it is present in the final assembled artifact.
6. **Default allowed activity** is quick static validation, isolated Java unit/compile checks, code review, or inspection of copies of supplied disposable save files. Do **not** execute `scripts/smoke_server.sh` with the full pack, start `runServer`, dispatch the full integration workflow with `full_smoke=true`, cause a full smoke via push, create a Prism build, or alter survival worlds without the owner's explicit instruction for that run. User has not authorized a build by requesting this checklist.
7. Capture each result in [CURRENT-TEST-STATUS.md](CURRENT-TEST-STATUS.md) and include source SHA, commands, scope, and evidence. Use `NOT BUILT` for absent features; `BLOCKED` for present but untestable or broken prerequisites; `NOT RUN` for untouched cases. Source PASS ≠ server PASS ≠ client PASS.

## Mandatory intake for features reported ready by other chats

See **[LIVE-TEST-INCLUSION-REGISTER.md](LIVE-TEST-INCLUSION-REGISTER.md)**. This new owner rule supersedes the former practice of treating old `deferred_pending_runtime` items as acceptable omissions. A "ready for live test" report in **any** project chat obligates the next comprehensive integration to **attempt** including that work, whether or not the previous QA chat registered or merged it. Merge conflicts, missing compatible pins, unresolved APIs and runtime crashes are legitimate **BLOCKED** outcomes only with documented evidence, not blanket deferrals. Owner-specific deferrals are the only voluntary exclusions. The build/handoff agent must provide the inclusion matrix **before** requesting the next full-smoke authorization. A complete source inclusion matrix still does **not** prove runtime success.

The 13 previously deferred runtime branches are listed for re-review in the register. Their older `NOT BUILT` labels describe the **previous retirement-focused build**, not permission to exclude them again. When a build includes these features, change the cases below to live-test tasks against the actual shipped SHA.

## R — Twelve readiness gates (run before any prospective build)

| ID | Gate | What Codex must verify | State/priority at writing |
| --- | --- | --- | --- |
| R01 | Retirement lock | No `vampirism`, `werewolves`, `vampires-delight` in active manifest/index/quests/tags; keep JAWR and shared libraries; run `scripts/verify_retirement.py` | Source staged; revalidate |
| R02 | JAR identity | Exactly `tmm_progression` + repaired `tmm_transformations` in intended two-JAR build; no old `tmm_monk`; no accidental TeamLapen API imports | Source staged; unbooted |
| R03 | Monk/Hunter branch union | Check newest Monk 30-skill/Bedrock/14,300-XP data and newest Hunter Trial/specialties/mutagenic-brew data, then check actual compiled module paths, no ancestor overwrite | Source staged; unaccepted |
| R04 | Command syntax | Fix/verify namespaced path/skill/event parsing and restore admin XP fixture; test player permissions and tab/command registration | **Known blocker** |
| R05 | Cross-mod bridge repair | Preserve JAWR-only Archlich/Dracolich death anchors, mob retaliation neutrality, Dragon Survival/Goety/Forbidden Magic compatibility; confirm old TeamLapen blood/HUD/cure hooks absent | Source staged; unaccepted |
| R06 | Silver canonicalization | Eidolon is sole craftable silver equipment provider; optional Hunter `silver_hunter` adds appropriate extra damage; no unconditional Werewolves multiplier or dead tags | Source staged |
| R07 | Tyz/MXP ownership | One Tyz skills backend; Pufferfish Skills removed only in actual assembled test build; Pufferfish Attributes retained if needed; MXP source-only not falsely marked runtime | Conditional |
| R08 | **Ready-feature inclusion gate** | Inventory every prior chat/PR "ready for live test" report; compare with all branches, source manifest and [intake register](LIVE-TEST-INCLUSION-REGISTER.md); attempt each ready runtime import/pin/build; show per-feature INCLUDED / BLOCKED with evidence / explicit OWNER-DEFERRED | **Old 13-item deferral requires re-review; not a waiver** |
| R09 | Quest and shop registry | Existing alcohol chapter is preserved; retire two TeamLapen quest chapters; detect invalid items and the prior oven/reactor placeholders; do not let Ars pilot clobber the new quest content | Needs validator |
| R10 | Banned routes | Seven Deadly Sins Ring absent from normal loot/Curios and, when B20 actually ships, from all trader offers including legacy trades | Static + conditional runtime |
| R11 | Production/data safety | Exact mod pins, hash index, no obsolete world migrations, immutable SHA, no altered stable snapshots or committed worlds/JARs | Precondition |
| R12 | Smoke authorization | Quick checks do not start full server; authorized `full_smoke=true` only after new explicit owner request; no candidate/stable promotion from quick checks | **Hard policy** |

## A — Codex/automation (no manual staging)

Run from a clean disposable worktree at the repository root. **These commands are allowed quick preflight**, *not* a full pack smoke:

```bash
git rev-parse --abbrev-ref HEAD
git rev-parse HEAD
git status --short
python3 scripts/validate.py
python3 scripts/verify_retirement.py
python3 scripts/verify_materials.py
python3 scripts/verify_silver_equipment.py
python3 scripts/verify_dragon_diets.py
python3 scripts/verify_tyz_migration.py
python3 scripts/verify_next_test_roster.py
python3 scripts/validate_quests.py
node scripts/verify_projecte_boss_recipes.cjs
python3 scripts/verify_emc_coverage_audit.py
python3 scripts/validate_progression_gates.py
python3 compat/magic/stage_preview.py --check
python3 tests/test_prism_qa_packaging.py
(cd compat/progression && gradle --no-daemon build)
(cd compat/transformations && gradle --no-daemon build)
```

**Important:** report commands individually with exit code and concise output, not a single universal PASS. Some validators are **post-assembly checks** that may legitimately reject the unassembled baseline or need generated metadata. In that case mark `BLOCKED (requires assembled disposable candidate)`, never weaken/remove the gate or edit the live pack just to make a report green. Gradle compilation/unit tests are permitted; `gradle runServer` is not. Do not download/run a complete modded server.

| ID | Test / executable task | Expected result and artifact |
| --- | --- | --- |
| A01 | `scripts/validate.py`; inspect `index.toml`, `pack.toml`, `manifest/mods.json`, `mods/*.pw.toml` | Every indexed path exists, hashes/side/loader and manifest IDs agree; excluded mods absent |
| A02 | `scripts/verify_retirement.py`; grep retired IDs in KubeJS/quests/configs/mod pins; reconcile intentional historical docs | No active TeamLapen dependency, quests or new-world content; JAWR survives |
| A03 | Compare all branch heads, ready-for-live-test chat/PR handoffs, `manifest/integration-sources.json`, [inclusion register](LIVE-TEST-INCLUSION-REGISTER.md) and final assembled runtime destination paths | Every ready feature has a documented inclusion attempt; no stale/deferred-only classification, missing pins or overwritten Monk/Hunter/other source |
| A04 | `(cd compat/progression && gradle --no-daemon build)`; parse final Monk/Hunter JSON | Parser, rank math, 30 classic Monk skill mapping, Hunter XP/abilities and exclusivity assertions pass |
| A05 | Inspect `compat/progression/src/main/java/.../command/ProgressionCommands.java` and run command parser unit tests if available | Full namespaced path/skill/event IDs parse; `admin xp` exists; non-OP cannot mutate other profiles; otherwise FAIL/BLOCKED |
| A06 | `(cd compat/transformations && gradle --no-daemon build)` and source grep | Compiles without TeamLapen, retains JAWR-only bridges/Archlich and relevant mob neutrality; no old blood owner |
| A07 | Test end-to-end attribute/silver code against exact tags and formula fixtures: `scripts/verify_silver_equipment.py`, `scripts/verify_materials.py` | One Eidolon silver output per slot, correct silver Hunter conditional, no removed werewolf blanket bonus |
| A08 | Check Hunter mutations and source tests: recipe 4 moss blocks + 4 rotten flesh + spider eye, brewing-stand-only outputs, 720,000 ticks/effect-tier, replacement, hunger cost 0.0005/tick, no slot cap | Valid data, no synthetic potion acceptance; note purge/cure gap explicitly |
| A09 | Source/assert MXP `growth_factor=1.02`, allowed 1.00–1.20, ordinary `g^P`, fixed awards bypass, committed payer and shared transaction dedup; verify first-party JAR staging | **Source-only unless actual MXP runtime JAR present** |
| A10 | `scripts/validate_quests.py`, quest sources/locale/advancement scan and same-build registry probe *only when authorized and available* | No undefined placeholders, ghost items, TeamLapen quests, stale reactors, duplicate rewards, or clobbered Alcohol questline |
| A11 | Verify ProjectE Philosopher's Stone trophy recipes and MK3+ Chaos Shard gates; `node scripts/verify_projecte_boss_recipes.cjs` | All original recipe orientations/gates, no diamond bypass; all-item EMC audit remains source-only until same-build registry export |
| A12 | Validate materials tags, Neo Vitae dusts, Occultism/Iesnium, no global Mahou dropped-item tick proxy, `scripts/verify_materials.py` | No double processing/free mana or invalid item paths; source checks only |
| A13 | Inspect B20 draft `scripts/validate_b20_trader_stock.py` on its branch; **attempt to integrate the reported-ready trader source for the next full live candidate** | Six guarantees + four distinct premium rotations, valid prices, banned Ring excluded; current old artifact may be NOT BUILT but next-build inclusion must be reconciled |
| A14 | Inspect B19 draft `scripts/validate_b19_gateways.py` and **attempt to integrate the reported-ready Gateway definitions/recipes into the next live candidate** | Seventeen real Gateways/catalysts; three friendly helper reward spawns, no guessed monster IDs; do not silently carry forward the old deferral |
| A15 | Review B16/B17/B18 branch manifests and validators where they exist; **attempt to resolve/pin approved mods and merge ready source for the next comprehensive live candidate** | BetterEnd/Thallasium, YUNG structures, Ars locators with verified packwiz pins and valid runtime IDs; blockers require specific evidence |
| A16 | Quest Shop 18 vanilla ore offers, economy loops, Ring/Relics blacklist, FTB starter once-only rewards | Static content and conflicts; player/UI confirmation remains category B/C |
| A17 | **Only with a user-provided disposable-world before+after copy:** inspect `level.dat`, `playerdata/*.dat`, `advancements/`, `stats/`, `data/` and matching config/logs **read-only** | Compare per UUID native Monk/Hunter XP, skills, mutation progress and modes, survival/relog/death. No world attachment supplied = BLOCKED |
| A18 | **Only after owner-authorized full smoke:** compare full log, registry dump, two JAR hashes, startup ready line, crash/mixin warnings and performance against previous immutable SHA | Actual server result + link, not just a green compilation. No authorization = NOT RUN |

**Offline save handoff:** receive a *copy* of the disposable world both before and after B tests, matching `BUILD-MANIFEST`, `serverconfig/`, `config/`, `defaultconfigs/`, `latest.log` and registry exports. Never modify the only world, commit the saves, or claim a single snapshot proves a live ability.

## B — In-game commands (short disposable-world procedures)

**Applies only to the exact compiled build.** Check that the relevant command actually exists using auto-completion / real registered commands. ``AFTER FIX`` means expected syntax **after** the namespaced parser and admin XP command are repaired; commands are **not currently certified to parse**. Enter the lines one at a time with operator permission where needed; do not paste comments or Markdown. Use separate disposable players/world copies for exclusive Monk and Hunter.

### B00 — Presence and world basics

```mcfunction
/gamemode survival
/gamerule keepInventory true
/datapack list enabled
/tmmprogress list
```

Expect both `tmm_progression:monk` and `tmm_progression:hunter` from the actual JAR. Check actual mods and two first-party JARs independently. Missing JAR = NOT BUILT.

### B01 — Monk admin path and immediate rank checks [AFTER FIX]

```mcfunction
/tmmprogress admin join tmm_progression:monk
/tmmprogress status tmm_progression:monk
/tmmprogress admin xp tmm_progression:monk 7700
/tmmprogress status tmm_progression:monk
/tmmprogress power armor
/tmmprogress power blink
/tmmprogress power treasure
```

Expect native Monk XP only, level according to the **14,300** terminal curve, free classic grants and correct equipment toggles. **The current integrated command lacks `admin xp`; do not treat this as a working sequence until fixed.** For Armor Lock, try one disposable iron chestplate and verify dropped, not deleted; Binding and full-inventory controls need separate checks.

### B02 — Bedrock Rest with 2 blocks [AFTER FIX]

```mcfunction
/time set midnight
/setblock ~1 ~-1 ~ minecraft:bedrock
/setblock ~2 ~-1 ~ minecraft:bedrock
```

On an already rank-17+ unarmored Monk, empty hand, adjacent exposed bedrock and unobstructed sky/headspace: right-click; expect true sleeping pose, night vote/time skip and repeatable eligibility. Pre-rank-17/non-Monk/unsafe hostile controls must refuse. Do not regrant cumulative XP to a player who already has it.

### B03 — Natural First Sunset

```mcfunction
/time set 12000
/gamerule doDaylightCycle true
```

Fresh unjoined human in Overworld; empty hands, unarmored, open sky, face west for 200 valid ticks. Confirm exactly one enrollment, and negative wrong direction/noon/armor cases. This natural rite cannot be replaced by admin-joining.

### B04 — Hunter Trial of Venom, specialty, kill XP [AFTER FIX for namespaced status]

```mcfunction
/advancement grant @s only minecraft:adventure/kill_a_mob
/effect give @s minecraft:regeneration 120 0 true
/give @s minecraft:potion[potion_contents={potion:"minecraft:poison"}] 1
```

Drink potion fully, then:

```mcfunction
/tmmprogress status tmm_progression:hunter
/tmmprogress specialty list
/tmmprogress specialty undead
/summon minecraft:zombie ~2 ~ ~ {NoAI:1b,PersistenceRequired:1b}
```

Kill zombie by player action and re-check Hunter status. Verify selected class is free/exclusive and health-scaled XP; a player missing Regeneration or Monster Hunter must not enroll. No manual `/tmmprogress enroll` bypass for the potion path.

### B05 — Bio-Mutagen and mutation status [AFTER FIX for namespaced status]

```mcfunction
/tmmprogress mutations
/give @s minecraft:moss_block 4
/give @s minecraft:rotten_flesh 4
/give @s minecraft:spider_eye 1
/give @s minecraft:brewing_stand 1
/give @s minecraft:blaze_powder 1
/give @s minecraft:potion[potion_contents={potion:"minecraft:swiftness"}] 1
```

Craft all 9 ingredients into Bio-Mutagen (shapeless), brew a **real stand-eligible** vanilla Speed potion into Mutagenic Brew, drink as Hunter and re-run `/tmmprogress mutations`. It should accumulate source duration only, with no temporary Speed and no ordinary potion XP. A non-Hunter receives documented penalty. **Do not manually grind 10 hours**; inspect progression threshold via unit/save test or propose QA-only admin fixture (not yet registered).

### B06 — MXP conditional (only if an MXP JAR is actually shipped)

```mcfunction
/tmmmxp status
/tmmmxp test ordinary 1000
/tmmmxp status
/tmmmxp test fixed 1000
/tmmmxp status
```

Verify `growth_factor=1.02` if new source shipped, one Tyz wallet, fractional progression and fixed vs ordinary awards. If `tmm_mxp` was source-only, mark `NOT BUILT`. Test g=1.00/1.20 only in separate disposable configs/worlds.

### B07 — Wandering Trader B20 stock conditional (deferred now)

```mcfunction
/summon minecraft:wandering_trader ~2 ~ ~
/summon minecraft:wandering_trader ~4 ~ ~
/summon minecraft:wandering_trader ~6 ~ ~
/give @s minecraft:emerald 64
/give @s minecraft:emerald 64
/give @s minecraft:emerald 64
```

Treat B20 as a required inclusion **attempt** in the next comprehensive live build; run these commands only if the resulting artifact actually includes it. Each trader: inherited stock, **six guaranteed** magic categories (real affixed Apotheosis; 3 glyph tiers; Uncommon+ Iron's ink; Relic/Artifact) and **four distinct rotating** premium categories, correct two-slot emerald prices. No Seven Deadly Sins Ring. Test save/reload and older trader duplicate protection. Earlier retirement-only QA: NOT BUILT; **not permission to defer the next ready-feature inclusion attempt**.

### B08 — FTB Essentials travel boundary

```mcfunction
/sethome
/home
/listhomes
/delhome
/rtp
```

Run as non-OP player **only if installed**; verify one-home limit, cooldown, persistence and safe RTP landing. Otherwise NOT BUILT.

### B09 — Ars/YUNG registry conditional

```mcfunction
/tmm_structure_registry
```

Operator-only exporter if B17/B18 script actually integrated; inspect log IDs, do not guess IDs. Earlier retirement-only QA: NOT BUILT; **next comprehensive inclusion attempt required if ready**.

### B10 — Worldgen/BetterEnd fixture conditional

```mcfunction
/gamemode creative
/execute in minecraft:the_end run tp @s 0 120 0
```

Only after BetterEnd and all libraries pinned; inspect generated End chunks and Thallasium/Spirit Fire under *actual* installed recipes. Teleport alone does not prove worldgen. Current targeted retirement QA: NOT BUILT.

## C — Tests that still need real world/client setup

Keep physical setup minimal; one shared disposable laboratory world, command-supplied items and short representative examples rather than full survival grinding.

| ID | Actual setup | Smallest meaningful test | When |
| --- | --- | --- | --- |
| C01 | Prism graphical client | Launch/rejoin, Mods list, JEI, quest groups/localization, skill command/UI and Curios; watch logs and visuals | Mandatory after authorized build |
| C02 | One mana rig | Attempt inclusion of ready Thermalily PR #147 before building; if included, test lava burn, buffer and 75 mana/tick/zero cooldown; test ProjectE converters separately | Previously deferred; inclusion re-review required |
| C03 | One Brewing Stand | Hunter Bio-Mutagen 9-input craft, stand conversion, component transfer and no extra potion XP | Targeted when progression JAR actually loaded |
| C04 | One Gateway arena | Attempt inclusion of ready B19; if included, sample Ars hostile, Ars friendly utility summon, Iron's hostile; registry-test remaining 14 automatically | Previously deferred; inclusion re-review required |
| C05 | One dragon/lich visual state | JAWR+Goety/Forbidden death-anchor precedence, mob retaliation; exact Dracolich client skeletal asset only if separate model PR included | High-risk conditional |
| C06 | Fresh generated terrain | Tectonic, Neo Vitae mines and 2–3 real new structures; BetterEnd/YUNG only if pinned | Targeted |
| C07 | Two real players on authorized server | Independent Monk/Hunter ledgers, team coins/quests, inventory/Curios sync, reconnect and death | Pre-candidate |
| C08 | Disposable backup and restore | Simple Backups and removed-mod-save handling on copies, never valued worlds | Pre-candidate |
| C09 | CC peripherals / boss progression | One active FE/CC peripheral and one prioritized ritual / boss-gate bypass attempt | Later |
| C10 | Dragon Survival/Jade/FTB client UI | Confirm actual Dragon Survival JAR; reproduce Jade Keypad-1 overlay toggle vs dragon Skill #1; verify Dragon Inventory tab vs editable FTB sidebar; capture top-right icons vs FTB minimap, move via M → settings and assess pack default config. See [Dragon/Jade UI triage](../DRAGON-SURVIVAL-JADE-CLIENT-UI-TRIAGE-2026-11-16.md) | Next client QA; no server-only substitute |

## Reporting and acceptance (Codex must follow)

- For **each attempted case**, append a dated evidence row to [CURRENT-TEST-STATUS.md](CURRENT-TEST-STATUS.md): case ID, repo SHA, command/path, result `PASS`, `FAIL`, `PARTIAL`, `BLOCKED`, `NOT BUILT`, or `NOT RUN`, and exact evidence. If not attempted, leave as NOT RUN. Do not mark gameplay PASS from tests that only read source.
- Also append confirmed run summaries to [docs/TESTING.md](../TESTING.md). Canonical feature statuses live in [manifest/feature-checklist.json](../../manifest/feature-checklist.json) and should change only through the existing evidence-backed workflow.
- A CI artifact with no client test is *server-only evidence*. A quick compile is *compile-only evidence*. No stable promotion without explicit owner sign-off.
- Preserve upstream build artifacts and immutable known-good snapshots. Do not commit test saves/logs/mod JARs.
- If a command/feature is missing from the built JAR, use `NOT BUILT`; if it exists but a required parser or permission is wrong, use `BLOCKED`/`FAIL` and record reproduction.
- If Codex adds future QA-only debug powers, they must be explicitly operator-gated, disabled in public builds, and source/version documented. Examples **NOT presently implemented**: `/tmmprogress admin setskill <path> <skill> <rank>` and `/tmmprogress admin mutation grantprogress <effect> <tier> <ticks>`. Never instruct testers to execute invented commands.

**References:** [current QA ledger](CURRENT-TEST-STATUS.md); [existing playtest](../NEXT-INTEGRATION-TEST-PASS.md); [retirement contract](../TEAM-LAPEN-TEST-REMOVAL-2026-11-15.md); `manifest/integration-sources.json`; PRs #127, #136, #138, #141, #142, #143, #144, #148, #161, #165, #167, #168, #131, #135, #145, #149 and #147.
