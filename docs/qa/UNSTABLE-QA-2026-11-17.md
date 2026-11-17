# Unstable QA — 2026-11-17 / run 44108614695

**Installed build, not moving HEAD:** `322cf3f9f0deb303fc6e6c29b340273ed3f481f6` (baseline `eaedbd10bd09c5d5a42f8e37e3f9c232a3ece9bb`). Minecraft 1.21.1 / NeoForge 21.1.265 / Java 21. 205 external pins, 4 first-party JARs: progression, transformations, MXP, Trader's Vengence. 28 locked workstreams. [Successful build](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/44108614695), [exact Prism artifact](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/44108614695/artifacts/14341405152), [latest unstable (may advance)](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/releases/download/unstable/Tyr-Unstable.zip), [test evidence](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/44108614695/artifacts/14340336409). ZIP SHA-256 `a2c2160c6f1aaaeca8328712f8215d57d036da795fd0abcd1fc9f9052924df96`. Server smoke and package passed, no client/play PASS. This QA list is **not** a new smoke/build authorization.

[Execution standard](QA-EXECUTION-TYPES.md) · [Results for this build](UNSTABLE-QA-RESULTS-2026-11-17.md) · [Prior player follow-up](PLAYER-FOLLOWUP-2026-11-16.md) · [Historical checklist](NEXT-TEST-CODEX-CHECKLIST.md).

**Scope omissions:** Gilded and lava-fishing / Nether Depths Upgrade / My Nether's Delight are unresolved (not owner-deferred permanently). Aether, Deep Aether and Liroth deferred; dynamic Newbie/Astral darkness, actual 37 JAWR form skill effects, native JAWR blood-spend MXP callback, Prefab housing/crafting restrictions pending. Twilight Forest custom final boss and future art pack are following-deployment work, not tests for this package. Per-player EMC tooltip gating and all-item EMC audit, cross-mod nested-bag prevention remain open. Do not mark any absent feature PASS. Existing native JAWR baseline rites and prior accepted natural Monk/Hunter initiation need not be repeated unless affected; changed native acquisition vetoes DO need coverage. Hot Dragon Rod/Spearman Promotion upstream quirks waived; Bottle Caps Treasure conditional on absent addon (do not install).

## A — Codex / automated offline (run first)

Codex: read AGENTS.md and this document; detach a **disposable** worktree at the installed commit above, fetch donor refs only for read-only provenance, compare it with the actual downloaded BUILD-MANIFEST and installed mod files if supplied. On Windows use canonical LF bytes for Packwiz verification; do not rewrite expected hashes. No new server/client launch, assembly, Prism packaging or live-world commands. Existing CI pass is evidence, not a substitute for an independent A check. If a local dependency, copied save or built registry dump is missing, record BLOCKED or PARTIAL with exact reason. Never modify the actual instance/saves. Record exact exit codes and concise outputs per ID in the linked results file. Unit/development compilation in an isolated checkout is allowed if available, not a game boot.

| ID | File reads / offline test | Expected evidence |
| --- | --- | --- |
| A01 | Compare ZIP SHA-256, root instance.cfg, mmc-pack.json, BUILD-MANIFEST.json, packwiz URL, bundled JAR hashes, installed mod hashes against final manifest. Run `python scripts/validate.py`. | Exact snapshot, 205 pins, four named first-party JARs, no nested ZIP, no copied saves or actual personal keys. No legacy tmm_monk. |
| A02 | `python scripts/validate_integration_sources.py` (inspect branch-only guard / detached checkout caveats; compare frozen donor commits to integration-lock.json; never assemble). Reconcile open readiness claims and exact deployed paths. | 28 source streams; documented partial scope, no hidden omissions. Compare to baseline separately: no assuming rolling HEAD equals installed snapshot. |
| A03 | `python scripts/first_party_runtime.py build`, `python scripts/first_party_runtime.py paths`, `python tests/test_prism_qa_packaging.py` in isolated checkout. Inspect actual bundled metadata and mixins. | All four modules compile/unit pass; Trader requires exact native Iron's [1.21.1-3.16.3]. Offline packaging fixture only; not a real client build. |
| A04 | Read ProgressionCommands.java, Monk/Hunter path JSON and tests; NewbieMode.java; native loot mixins and TreeChop hooks. Validate full namespaced parser, OP gates, rank curve and all skill IDs, natural enrollment gates, GUI registration and event attribution. | Monk rank-17 at 7700/rank-20 at 14300; Hunter rank-20 at 28000 in frozen data; focus needs steady_hands milestone; no invented admin setskill or mutation-progress commands. Flag conflicts between sources and JAR. |
| A05 | Inspect Bio-Mutagen recipe codec, mutation item/brew/food registration and Hunter tests; `python scripts/verify_silver_equipment.py`, `python scripts/verify_materials.py`. | Valid nine ingredients (4 moss,4 rotten flesh,1 spider eye), native brewing, duration/persistence/hunger/weapon tags. No global anti-lycan damage tag. Mutagen recolor remaining artwork only if absent. |
| A06 | Inspect MxpMod/Service/config and four spend hooks, Tyz bridge, one-wallet/dedup/growth/eligibility tests; `python scripts/verify_tyz_migration.py`; `python compat/magic/stage_preview.py --check`. | Default growth 1.02; fixed vs divided, fail-closed Newbie, no credit on refills/failed casts. Runtime JAR shipped; JAWR native spend unwired, no active 37-node abilities claim. Compare boss-rule registry (may be empty); do not invent configured boss rewards. |
| A07 | `python scripts/validate_newbie_integration.py`, read Smart Keep tag JSON, Corail/Tyz config, 227th quest, FTB Essentials config and exact mode gate. | keepInventory false per world; personal graduation, tag/safe guards, inactive Safekept, one home / RTP 10m / distances 500–25000; OP bypass known. |
| A08 | `python scripts/verify_retirement.py`, `python scripts/verify_canonical_blood.py`, inspect JAWR guard, native transformation mixins and body, Dracolich assets (`python scripts/verify_dracolich_cave_assets.py` if available). | No restored TeamLapen; ordinary blood canonical EvilCraft, protected native unique components; five acquisition guards, death-anchor policy and visuals. |
| A09 | Read native Mahou Catalyst mixin and exact pinned JAR audit evidence; `python scripts/verify_materials.py`; compare dust identities against recipes. | No legacy global dropped-item tick shim; native circle cost/count semantics and allowed dust mapping. Actual exchange C. |
| A10 | `python scripts/validate_quests.py`, `python scripts/validate_ars_quest_pilot.py` only if compatible with assembled live layout, inspect SNBT locale/templates, filter and first-log reward, 49 Ars nodes/Alcohol preservation, tome + starter function. `python scripts/quest_registry_gate.py --help` first; use same-build supplied registry export if available, never generate one in game in A. | Five groups / 44 chapters / 227 quests; no literal invalid formatting, duplicate claims or ghost item references; FTB Filter System native log/stem filter. Validate actual installed registry only if provided. |
| A11 | `python scripts/validate_progression_gates.py`; `node scripts/verify_projecte_boss_recipes.cjs`; `python scripts/verify_emc_coverage_audit.py`; inspect actual transformed recipes, CC/Eldritch gate/uranium tags and shop. | Stone boss trophy, MK3+ Chaos Shard; no cheaper bypass/EMC loop; registry-complete EMC cannot be claimed from static test. |
| A12 | `python scripts/validate_b19_gateways.py`, `python scripts/validate_b20_trader_stock.py`, read actual Gateways and Wandering Trader JSON/scripts and vendetta code; compare actual installed registry if supplied. | 17 B19 definitions, valid entities, six guarantees/four distinct premium offers, blacklist; natural spawn-only filter, no summon veto. Historical Windows path-separator false failure: preserve evidence; do not weaken validator. |
| A13 | `python scripts/verify_next_test_roster.py`, `python scripts/validate_cc_iesnium_pilot.py`, `python scripts/validate_b17_ars_locators.py`, `python scripts/validate_b18_yungs_ars.py`; inspect exact pins, sides and registry export if supplied. | Alex's Caves, BetterEnd, six additional dimensions, YUNG suite, seven mobs, static darkness, FTB Essentials, Comforts, JER, Dragon Survival, Colorful Hearts. No Torchmaster. Actual natural worldgen C. |
| A14 | `python scripts/verify_dragon_diets.py`, `node scripts/verify_dragon_seasoned_fish.cjs`, native dragon configs and shipped recipe; compare food/recipe IDs and source compatibility. | Ritual-only dragon choice, raw-fish tag fix, valid diets and unchanged distinct Sea Dragon Treat; accepted upstream quirks not modified. |
| A15 | `python scripts/validate_defaultoptions_keybindings.py`; read ClientKeyDefaults, QA sync script, Default Options/KeySets pins; compare actual exported registry if provided and existing options copies. | 315 fallback defaults, preserve vanilla 34 and existing remaps; no actual private preference data shipped. |
| A16 | Review Thermalily output/cooldown mixins, energy-converter pin, Eternal Steak/durability and food source, bag-nesting guards and quest shop recipes. | Match actual shipped source, mark unfinished bag boundary/EMC explicitly; no operation proof from file reads. |
| A17 | Existing server evidence and CI logs only: run 44108614695, frozen tag, assembly lock, JAR hash checks, relevant KubeJS/loader/mixin errors and startup. If owner supplies before/after **copies**, compare data/players/attachments, logs and configs offline. | Server and package PASS already recorded; isolate any nonfatal warnings. Copied saves absent = that subcheck BLOCKED, not inferred persistence. |

If a validator assumes a moving baseline, imported path, generated registry or different OS, document the exact incompatibility; run only read-only compatible subchecks, not assembly or ad hoc fixes. Inspect command `--help` for any validator requiring additional inputs. Codex should report A results and actionable defects before asking the player to work around them.

## B — In-game primarily command-driven (owner)

Use separate **disposable** worlds or profiles for Monk, Hunter and Newbie/Regular; cheats/OP only for fixtures, player chat as the relevant player. Never set global keepInventory true (would conceal the Smart Keep test). Below code lines are one command at a time. Custom TMM command literal/parser and sample skill IDs were verified by source at frozen SHA; actual registered command execution remains untested. Try Tab completion for upstream commands/IDs; if absent report exact error, not an invented substitute. Use ordinary non-OP accounts for permission tests. Client import/launch is prerequisite C01, not a command test.

### B01 — Baseline and Newbie/MXP status
~~~mcfunction
/gamemode survival
/gamerule keepInventory false
/datapack list enabled
/tmmprogress list
/tmmnewbie status
/tmmmxp status
/tmmtransform status
/tmmtransform vampire
/tmmtransform werewolf
/tmmtransform archlich
/tmmtransform dracolich
~~~
Expect Monk and Hunter, not Gilded; new character Newbie, native form probes report absence for untransformed character; MXP growth 1.02. Record output verbatim, not real transformation (read-only probes).

### B02 — Newbie denial and graduation fixture with MXP
~~~mcfunction
/tmmnewbie admin newbie @s
/tmmnewbie status
/tmmmxp status
/tmmmxp test fixed 1000
/tmmmxp status
/tmmnewbie admin graduate @s
/tmmnewbie status
/tmmmxp test fixed 1000
/tmmmxp status
/tmmmxp test ordinary 1000
/tmmmxp status
~~~
OP in fresh profile; Newbie synthetic award rejected with no pending/lifetime increase; Regular fixed undivided and ordinary reduced by 1.02^lifetime levels, same Tyz wallet. Synthetic test changes progression, so not on a survival character. Compare Tyz HUD/points in C. Admin graduation is not proof of personal quest reward.

### B03 — Native FTB Essentials travel and permissions
As **non-OP Newbie** (owner/OP prepares mode using B02 admin command on that player's exact name). Upstream commands based on pinned config and prior command checklist; verify Tab syntax in deployed game, not source-certified.
~~~mcfunction
/sethome
/listhomes
/rtp
/home
/back
/delhome
~~~
Confirm safe destinations, one home max; immediately repeated /rtp blocked 600s, /home cooldown 10s. Record /back behavior if enabled. Try all again as **non-OP Regular** after trusted admin graduation (and any exposed namespaced aliases); expect mode rejection, not OP bypass. If a command expects a name, use actual completion and record it. Test normal-player mode with cheats/OP removed, restore privileges externally only for next fixture.

### B04 — Monk command regression and ability fixtures
Fresh Regular human (not already Hunter/form), OP.
~~~mcfunction
/tmmnewbie admin graduate @s
/tmmprogress admin join tmm_progression:monk
/tmmprogress status tmm_progression:monk
/tmmprogress admin xp tmm_progression:monk 7700
/tmmprogress status tmm_progression:monk
/tmmprogress power armor
/tmmprogress power blink
/tmmprogress power treasure
/tmmprogress buy tmm_progression:monk tmm_progression:focus
~~~
Rank 17 on fresh 0 XP, free ranks applied. Focus purchase should refuse until steady_hands (100 barehand logs) and cost points available; do not report missing milestone as parser failure. Actual positive Tyz/Monk purchases and UI C03. Check power availability against actual gates rather than assuming all unlock at rank 17. Test armor toggle with equipped disposable iron chestplate, blink toward safe clear area, and refuse unavailable power. Admin XP grants progression only. To top up a zero-start 7700 profile to max use increment `/tmmprogress admin xp tmm_progression:monk 6600` only if status still exactly 7700.

### B05 — Hunter command/kill XP and purchase fixture
Separate fresh Regular human; prior natural enrollment accepted, only rerun if relevant behavior changes. Admin join is not a natural rite.
~~~mcfunction
/tmmnewbie admin graduate @s
/tmmprogress admin join tmm_progression:hunter
/tmmprogress status tmm_progression:hunter
/tmmprogress specialty list
/tmmprogress specialty undead
/tmmprogress admin xp tmm_progression:hunter 1000
/tmmprogress buy tmm_progression:hunter tmm_progression:undead_slayer
/tmmprogress status tmm_progression:hunter
/summon minecraft:zombie ~2 ~ ~ {NoAI:1b,PersistenceRequired:1b}
~~~
Perform player melee kill (not /kill); re-run status. Check free exclusive specialty, actual health-scaled XP and points/cost, rank purchase and second specialty refusal under current policy. Validate against frozen hunter_xp configuration; use a nonfavored mob control. If testing natural enrollment on a changed path, use separate unjoined Regular with no supernatural form:
~~~mcfunction
/advancement grant @s only minecraft:adventure/kill_a_mob
/effect give @s minecraft:regeneration 120 0 true
/give @s minecraft:potion[potion_contents={potion:"minecraft:poison"}] 1
~~~
Drink potion, then `/tmmprogress status tmm_progression:hunter`. Negative missing regeneration/advancement separate fresh player. Don't use forced join to claim rite PASS.

### B06 — Bedrock Rest / command-assisted powers
Use already rank-17+ Monk, unarmored, empty hand, safe exposed bedrock.
~~~mcfunction
/time set midnight
/setblock ~1 ~-1 ~ minecraft:bedrock
/setblock ~2 ~-1 ~ minecraft:bedrock
~~~
Right-click exposed block; expect true sleep/night vote, not teleport. Check non-Monk or lower-rank refusal on separate profile. No Creative flight entitlement; actual glide/armor/Bedrock Rest interactions C03/C04 if setup needed.

### B07 — Recipe registry and item fixtures (not crafting PASS)
~~~mcfunction
/recipe give @s tmm_progression:bio_mutagen
/give @s minecraft:moss_block 4
/give @s minecraft:rotten_flesh 4
/give @s minecraft:spider_eye 1
/tmmprogress mutations
/recipe give @s dragonsurvival:seasoned_fish
~~~
Record successful recipe registration versus unknown recipe; unlock does NOT establish crafting. Actual brewing/foods C08/C09. No fake mutagen progress commands.

### B08 — Vendetta command and native summon non-veto
Use fresh disposable player/profile with OP, record UUID from:
~~~mcfunction
/data get entity @s UUID
/summon minecraft:wandering_trader ~2 ~ ~
/summon minecraft:wandering_trader ~4 ~ ~
/summon minecraft:wandering_trader ~6 ~ ~
/give @s minecraft:emerald 64
~~~
Convert displayed UUID int array to canonical UUID (Codex can convert offline; don't paste int array), substitute it for PLAYER_UUID in each following literal:
~~~mcfunction
/tradersvengence status PLAYER_UUID
/tradersvengence accuse PLAYER_UUID
/tradersvengence status PLAYER_UUID
/effect give @s minecraft:hero_of_the_village 30 0
/tradersvengence status PLAYER_UUID
/tradersvengence pardon PLAYER_UUID
~~~
Expect summon allowed (not natural-spawn control); accuse sets persisted record, gaining fresh Hero clears it; pardon idempotent/no offender afterward. Check actual trade stock in C11. Natural violence/AI separate. Commands require OP and UUID, not a profile name. Clean up in throwaway world only.

### B09 — Structure/recipe/registry command checks
~~~mcfunction
/tmm_structure_registry
~~~
Collect TMM_STRUCTURE_REGISTRY_JSON from latest.log and send to Codex, do not post full private logs. Check actual YUNG, Ars and dimension entries; registry presence not generated structure evidence. Use Tab-completed `/locate structure` and `/execute in` for registered IDs only (exact IDs from export, not guesses). Read-only registry and command results support A10/A13. For BetterEnd in fresh Creative world only:
~~~mcfunction
/gamemode creative
/execute in minecraft:the_end run tp @s 0 120 0
~~~
Return using `/execute in minecraft:overworld run tp @s 0 120 0` in disposable Creative world; exploration C12. Do not claim actual worldgen from teleport.

### B10 — Newbie death basic fixture and non-OP command boundary
Fresh Newbie and separately graduated Regular:
~~~mcfunction
/gamerule keepInventory false
/tmmnewbie status
/give @s minecraft:iron_ingot 5
/experience add @s 12 levels
/kill @s
~~~
Respawn; record items, XP, mode, and whether grave duplicated/appeared. **Do not** kill a valued character/world. OP may be necessary for /kill; non-OP natural death, Curios, boss/multiplayer/Corail and genuine graduation C02. Non-OP should not be able to use tmmnewbie admin, tmmprogress admin, tmmmxp test or tradersvengence admin commands.

## C — Actual play / setup / UI (owner)

| ID | Minimal meaningful exercise | Expected / prior evidence |
| --- | --- | --- |
| C01 | Import exact ZIP in Prism, fresh profile/world, Java21, launch and rejoin; inspect client mod list, JEI, HUD, Curios and KubeJS logs. | No client crash or malformed recipe/load errors; server smoke is not client acceptance. |
| C02 | Two mixed-mode players/FTB teammates where authorized; test personal graduation quest, tag, normal mob/boss/dimensional deaths, XP/Curios/grave, relog, actual virtual Elytra double-jump/no rockets; native class/form acquisition refusal while Newbie and after class. | keepInventory false always; no shared graduation/duplication; Regular standard Corail; mode persists. No housing/crafting restriction tests (not implemented). |
| C03 | Monk K GUI and Tyz actual purchases/points, empty-slot and no-armor toggles; barehand eligible logs/stone, magic mining, hostile fights, compare Fortune vs Silk actual drops. Test steady_hands via natural logs if useful (no fabricated admin shortcut). | Previous cannot-buy bug; successful legal purchase and gated denial; no double rewards; rank changes; armor/Curios safety. |
| C04 | HT's TreeChop on actual spruce/oak, bare hand and appropriate controls; observe Jade progress bar after first chop, subsequent chops and full felling; before/after Monk status; test bedrock sleeping and true glide gates as appropriate. | Two independent prior failures: bar disappears and completed tree awards no XP. Record exact tree logs/block count/XP and screenshot. |
| C05 | Hunter kill sample favored/unfavored types with real player final blow; favored Looting/rare-drop comparison against normal player and enchanted weapon. Silver weapon against lycan. | Health-scaled XP, native one-roll Looting, correct species and stacking; use repeated bounded trials not one lucky drop. |
| C06 | As Regular cast actual Ars, Iron's, Botania portable tablet/ring and Neo Vitae network expenditure; check /tmmmxp status before/after including failed/canceled casts, refill, double-event controls and Tyz GUI purchase. Newbie negative control. | True committed payer only, no duplicate/fake award; actual Tyz wallet, no saved hidden credit. JAWR native blood spend NOT BUILT; boss/ritual awards conditional actual configured registry. |
| C07 | JAWR ordinary blood bucket/pipe/bottle all supported mods, Hexerei/Iron's effects and empty containers; protected chicken/Lilith components unchanged. Ascend via Lilith blood and inspect both pools/HUD immediately and after relog. Test Archlich charged/fallback anchor and Dracolich dragon-first death/visuals and mob neutrality, class/species exclusions without overwriting existing forms. | No lost blood, wrong form, duplicate anchor consumption or unwanted buff; prior ascension HUD/pool failure #58. No blanket repeat unchanged JAWR baseline. |
| C08 | Hunter hand-craft Bio-Mutagen, actual Brewing Stand conversion of eligible vanilla Speed potion, drink as Hunter and non-Hunter; inspect mutations before/after/rejoin, penalty and no temporary Speed. | Recipe and JEI visibility now need PASS; partial mutation progress accepted previously. Permanent tier and hunger require controlled longer fixture; do not grind ten hours just for test. |
| C09 | Dragon ritual-only acquisition (login selection absent); Sea sushi/fish/kelp, Forest enchanted carrot/Sourceberries/fruit; Seasoned Fish each raw fish + ink sac + kelp; cooked control must fail. Three Sources of Magic native recipes via JEI. | Prior diet and salmon-only failures #60; consume correct ingredients, unchanged Sea Dragon Treat. No mandatory Hot Dragon Rod/Spearman retest; optional Bottle Caps addon absent = NOT BUILT. |
| C10 | Small Mahou Catalyst Exchange circle with valid native powder control and each canonical Neo dust; pay circuit/player mana, insufficient mana control, counts/random output, nonexchange circle unchanged. Build Thermalily lava rig; measure mana/tick and cooldown; test energy converters separately. | Native exchange charges normally with no item replacement/double cost; Thermalily no cooldown and intended output (~50 Endoflames, earlier target 75 mana/tick); measure rather than assume config. |
| C11 | Spawned trader stock inspect/reload (six guarantees + four distinct premium categories), no banned Ring; natural scheduled traders inside/outside 32-block No Soliciting signs with open sky/valid ground and sea-level band; hit/kill trader/villager/golem, priest target and villain/monster controls; Hero pardon and real missiles. | Spawn commands unaffected, legitimate stock and AI, stored offender-specific reactions; no duplicate goals; no blanket monster attacks. |
| C12 | Explore new chunks in Alex's Caves/Toxic Caves, BetterEnd, Bumblezone, Dive Into The Abyss, Eternal Starlight, Aurorian, Twilight Forest and Voidscape; sample YUNG structures, Ars locators, Tectonic and actual Iesnium/Thallasium/uranium route. Seven mob mods, Cave Creeper ore drop. | Natural generation/entry/return and expected loot, no forbidden uranium route or missing registry; teleport alone not proof. Deferred dimensions/final boss not required. |
| C13 | Quest book five groups, localized text, 49 Ars nodes, Alcohol, Food & Fishing oven and expected titles; first-log claim with logs and Nether stem fresh players; starter tome contents, initial slot, no auto bag/torches, Curio Bag slot, Enigmatic auto gifts and correct books. Test shop exchanges and quest reward once / two players when authorized. | Prior empty tome, auto-gifts, missing claim, invalid ampersands/Unnamed, ghost oven, incomplete Ars; no duplicate or clobbered Alcohol. |
| C14 | ProjectE Philosopher's Stone boss trophies, MK3+ Chaos Shard gates, transmutation/Matter Upgrader bypasses, Star Bone/Goety recipes and ore/shop conversions; check Replication Eldritch gate and CC/Tweaked peripheral; verify EMC export with actual registered IDs if available. | No bypass or loop, registry EMC coverage still open until full audit. Component-level proofs only. |
| C15 | One Gateways arena: sample hostile and friendly utility gateways, Caerbannog prototype separately (not Twilight final boss); test claimed rewards, no stuck waves; inspect other 17 definitions via A12. | Correct spawned mobs/loot and completion; no claim of final boss implementation. |
| C16 | Default Options / KeySets once: clean profile temp local QA storage, vanilla 34 unchanged, all actual mod keys default unbound including KeySets, Reset, rebind then restart/import to confirm persistence, inspect K, Jade Keypad 1, Dragon skill and FTB sidebar/minimap HUD positions. | Compare actual registered-key inventory; KEEP KeySets only full PASS else remove next test after FAIL/BLOCKED/INCONCLUSIVE. Preserve actual private remaps. |
| C17 | Comforts sleeping bags/hammocks, JER ores, Colorful Hearts HUD; verify More Darkness static client effect in darkness/night and contrast; Eternal Steak use, nourishment/durability/recharge in applicable path. | No hidden gameplay break; dynamic Newbie/Astral lighting not shipped. |
| C18 | Disposable backup/restore, rejoin and multiplayer if authorized; mixed-mode class/XP/team rewards, no nested bag exploit only when implementation available. Report unsupported cases. | Never migrate valuable worlds; nested bag open blocker not passed by absent code. |

Record smallest useful actual output in [results](UNSTABLE-QA-RESULTS-2026-11-17.md). Do not rerun formerly accepted unchanged tests just to fill a table. If a source case fails, tell the player before they waste time; retain B/C for changed behaviors independent of that issue.
