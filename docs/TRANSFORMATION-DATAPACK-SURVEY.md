# Real transformation datapack surfaces vs required NeoForge integration

**Status: source-level discovery and feasibility audit, not an installed datapack, compatibility addon or tested integration.** 2026-11-13, MC 1.21.1 NeoForge. [TRANSFORMATION-TARGET.md](TRANSFORMATION-TARGET.md) contains the user's locked transformation rules; [DEATH-COMPAT-TEST-PLAN.md](DEATH-COMPAT-TEST-PLAN.md) tracks actual-death tests.

## A. What is currently in our repository?

The `design/expanded-magic-mod-decisions` branch contains `kubejs/server_scripts/bootstrap.js` and `kubejs/startup_scripts/bootstrap.js`, each **comment-only**. There are no `data/<namespace>/...` transformation compatibility JSON files or custom NeoForge addon source in this branch. The installed B3 pack already has JAWR and JAWR: Forbidden Magic (JAWR B3 version 0.5.11); Vampirism, Werewolves, Dragon Survival and Goety are not installed on B3. The manifest remains a plan, not proof of functionality.

**Distinguish user-authored datapacks** (recipes, data maps, skill trees, tags and ritual JSON) from **native mod logic** (player attachments, faction changes, hunger/blood, sun damage, body state and resurrection). Existing JSON can configure the former; it does not rewrite the latter.

## B. Actual inspectable data-driven surfaces

| Mod (target source) | Verified source datapack paths | Useful direct override | Cannot accomplish by JSON alone |
| --- | --- | --- | --- |
| **Vampirism 1.21.x** | `data/vampirism/vampirism/skill_tree/vampire/level.json`; `configured_skill_tree/vampire_level.json`; `skill_node/vampire/*.json`; `data_maps/entity_type/entity_blood.json`; `data_maps/item/item_blood.json`; hunter equivalents | Skill tree layouts, skill-node contents/links, human/animal and item blood values; possible additional tree keyed to a registered custom faction | Registering an Abomination faction; preserving two native provider progressions when one faction active; suppressing native vampire physiology; bidirectional status synchronization |
| **Werewolves 1.21.x** | `data/werewolves/vampirism/skill_tree/werewolf/level.json`; `configured_skill_tree/werewolf_level.json`; `skill_node/werewolf/*.json` | Werewolf skill-tree structure, progression nodes and permitted skills | Werewolf player/form and native faction equivalence; retaining wolf skills when active Vampirism faction is vampire/custom; beast-model and hunger behavior |
| **Dragon Survival 1.21.1** | `data/dragonsurvival/data_maps/dragonsurvival/dragon_species/diet_entries.json`; `dragonsurvival/dragon_ability/*.json`; `dragon_stage/*.json`; `dragon_penalty/*.json`; builtin `datapacks/no_penalties/`, `datapacks/unlock_wings/` | Add Pam's, Farmer's Delight and Aquaculture foods to dragon diets; tune body growth, abilities/penalties; test per-species diet without disabling all dragon feeding | Preventing entry at altar/first join in all routes, allowing dracolich while disallowing Archlich, morph/model and lich persistence |
| **JAWR (0.5.x)** | Official documentation: `data/<namespace>/ritual/*.json` for Witchery ritual definitions, with `ritual_events` supporting `witchery:consume_items`, `witchery:run_command`, `witchery:remove_curse`; additional `nature/`, `altar_augments/`, `fetish/`, trades | Custom occult ritual costs, steps/requirements, and calling a registered TMM command at completion; separate Lore/quest authoring | Accessing JAWR player vampire/lycan state, suppressing extra native sun/blood/HUD and model/specific immortality, preserving JAWR progression. **The official issue/doc repository does not contain the complete 0.5.x Java sources or shipped 0.5.39 JAR data** |
| **JAWR: Forbidden Magic (0.5.x)** | Official docs list data-driven `imp_trades/` (items + soul cost); JAWR ritual JSON potentially supplies a ritual *front end* | Imp trade costs, potentially ritual entry costs where matching JAWR mechanism is exposed | Lich phylactery acquisition, death event/respawn control, lich levels; require jar inspection and API survey |
| **Goetied (Goety NeoForge 1.21.1 port)** | Port published at version 3.0.19, file 8679512. Its description says recipes/data-driven content and config; **no verified source-backed lichdom datapack schema** from the 1.21.1 port available to this audit | Recipe/ritual ingredient changes likely possible after exact artifact inspection; config may expose some lich rules | Arca binding, soul-energy state and fatal-event arbitration; must inspect the target JAR/available port code, not assume the 1.20 upstream implementation |

### Source exact examples, not invented paths

**Vampirism faction gating is embedded in a JSON tree definition:** `vampirism/vampirism/skill_tree/vampire/level.json` contains a `faction.id: vampirism:vampire` field **and** an `unlock_predicate` with `type: vampirism:player_faction` pointing to that same faction. Werewolves' `werewolf/level.json` has `faction.id: werewolves:werewolf` with the analogous predicate. Changing these to a custom faction is conceivable as a **copied/new tree**, but cannot work until the custom faction is registered in Java and its progression/ability handlers are integrated. Do not overwrite existing trees and break normal vampires and wolves.

**Vampirism JSON blood data:** `data/vampirism/data_maps/item/item_blood.json` has entries such as `minecraft:beef -> blood:200`, `minecraft:mutton -> blood:100`. `entity_type/entity_blood.json` has `minecraft:cow -> blood:10`. Thus part of food translation may use data maps rather than a nutrition mixin, but JAWR's blood meter must still be disabled/adapted through code. Investigate how Vampirism treats Vampire's Delight items and Neo Vitae orbs rather than assuming every item uses that map.

**Dragon Survival built-in data maps:** the official `diet_entries.json` uses entries keyed by `dragonsurvival:cave_dragon`, with `neoforge:value` containing items/tags and nutrition/saturation properties. This is an immediate JSON-level integration point for approved food mods; exact mod item IDs must come from actual jar/registry. `no_penalties` built-in datapack replaces dragon penalty tags with empty sets, showing penalty customization is real, but removing all penalties is not the user's design goal.

**JAWR run-command ritual:** official ritual JSON supports an output `{\"type\": \"witchery:run_command\", \"command\": \"... {owner} ...\"}` after item/sacrifice requirements. This offers a data-driven ritual entry point to a **custom bridge command**, once that command exists in Java. Command permission, atomic transformation, rollback and correct owner/target must be verified. Do not issue console commands that directly flip native flags out of order.

### Primary code/documentation evidence

- Vampirism data files: https://github.com/TeamLapen/Vampirism/blob/version/1.21/latest/src/generated/resources/data/vampirism/vampirism/skill_tree/vampire/level.json and https://github.com/TeamLapen/Vampirism/blob/version/1.21/latest/src/generated/resources/data/vampirism/vampirism/configured_skill_tree/vampire_level.json and https://github.com/TeamLapen/Vampirism/blob/version/1.21/latest/src/generated/resources/data/vampirism/data_maps/item/item_blood.json
- Werewolves: https://github.com/TeamLapen/Werewolves/blob/1.21/src/generated/resources/data/werewolves/vampirism/skill_tree/werewolf/level.json and https://github.com/TeamLapen/Werewolves/blob/1.21/src/generated/resources/data/werewolves/vampirism/configured_skill_tree/werewolf_level.json
- Faction implementation: https://github.com/TeamLapen/Vampirism/blob/version/1.21/latest/src/main/java/de/teamlapen/vampirism/entity/factions/FactionPlayerHandler.java and https://github.com/TeamLapen/Vampirism/blob/version/1.21/latest/src/main/java/de/teamlapen/vampirism/entity/factions/FactionRegistry.java
- Dragon Survival data: https://github.com/DragonSurvivalTeam/DragonSurvival/blob/1.21.1/src/generated/resources/data/dragonsurvival/data_maps/dragonsurvival/dragon_species/diet_entries.json and https://github.com/DragonSurvivalTeam/DragonSurvival/blob/1.21.1/src/generated/resources/data/dragonsurvival/dragonsurvival/dragon_ability/cave_wings.json and https://github.com/DragonSurvivalTeam/DragonSurvival/tree/1.21.1/src/generated/resources/data/dragonsurvival/datapacks
- JAWR ritual schema: https://github.com/mrsterner/witchery-issues/blob/main/README.md#json-structures
- Goety port: https://www.curseforge.com/minecraft/mc-mods/goetied/files/8679512

## C. Code-level barriers we can already demonstrate

1. **One Vampirism faction:** `FactionPlayerHandler.currentFaction` is a single nullable `IPlayableFaction`, not a set. `setFactionAndLevel()` leaves the current faction and **resets its task manager** when the faction changes. Merely alternating faction values would erase tasks/progress. A hybrid needs dedicated preserved progression state and one active registered faction, or carefully constrained proxies without resetting.
2. **Exact native faction tests:** Vampirism's `Helper.isVampire()` checks `VReference.VAMPIRE_FACTION`; Werewolves' `Helper.isWerewolf()` checks `WReference.WEREWOLF_FACTION`. JSON skill-tree changes alone do **not** make the new hybrid faction pass either check. Need a targeted bridge, wrappers/hook injection, or custom implementations of selected powers. Also evaluate blood HUD, sunlight, lycan body forms, attributes and player-level skill handlers.
3. **JAWR opacity:** JAWR documents ritual JSON, but Java ownership of vampire/lycan player state, tick damage, feeding and rendering must be discovered by inspecting the exact `0.5.39` artifact. We **cannot claim** any specific injection or number of mixins needed until that review.
4. **Lich death behavior:** JAWR Forbidden Magic phylactery and Goetied Arca are independent. Even though a datapack can specify **Archlich ritual costs**, a datapack alone cannot ensure the two native fatal-death handlers run exactly once or coordinate anchors. Require dedicated death/respawn integration with evidence of true-death state and Corail grave ownership.
5. **Dragon restriction:** Dragon Survival's altar and first-login choice are native GUI/Java paths, not JSON-only. Server config can disable initial dragon choice (so bridge-controlled progression remains authoritative), while custom policy must block second lich on a dragon and unwanted dragon-vampire/wolf states.

## D. Practical work breakdown (estimates are effort classes, NOT tested time promises)

| Package | Datapack/config work | Java bridge work | Effort / unknown |
| --- | --- | --- | --- |
| Universal transformation eligibility + Hunter protection | JSON/KubeJS ritual costs, recipe locking, server configs | One shared player-state manager, veto hooks, transactional conversions and save/load/reversion | **Medium**; must cover each source's entry paths |
| Unified vampire: Vampirism + JAWR | Keep Vampirism skill-tree JSON; optionally custom nodes; data-map blood food; JAWR ritual front end | Mirror native JAWR status + Vampirism faction; suppress one blood/sun/immortality engine; preserve separate JAWR quest progression | **High**, dependent on JAWR binary audit |
| Unified werewolf: Werewolves + JAWR | Reuse Werewolves `configured_skill_tree`; optional data edits, JAWR ritual | Mirror statuses, reconcile form render, hunger and hard-coded faction checks; preserve both progression tracks | **High**, dependent on JAWR binary audit |
| Abomination (four source base states + 1–2 hybrid tracks) | New configured skill trees/skill nodes, recipes, advancements/quests; avoid reusing incompatible faction IDs | Register hybrid playable faction, integrate two forms and both feeding traditions, isolate/retain all four source histories and custom progression | **Very high**, most new code and gameplay test cases |
| Archlich (JAWR lich + Goetied lich) | Custom rite/gates and optional item recipes | Persist both lich states, one death/respawn arbitration, two anchors with deterministic handling, no double invulnerability | **High**, primarily death integration |
| Dracolich (Dragon Survival + exactly one lich) | Dragon species diets/stages/abilities; ritual and resource gates | Eligibility with one lich only, body render, shape persistence/flight, soul/food/death behavior | **High** for initial playable; custom skeletal art higher |
| True-death grave/XP integration | Two Enigmatic config switches; Corail config; optional simple recipes/loot | Guard/replace Escape Scroll drop teleport; verify true-death lifecycle and native lich hooks | **Low–medium** except integration bugs; *locked policy* |
| Food ecosystem | Dragon Survival diet data map and tags + Vampire's Delight / pantry tags | Small hooks only if target metadata cannot express intended behavior | **Low–medium**; high JSON leverage |

**Net conclusion:** JSON offers a genuinely large amount of progression, diet and ritual control. It is **not enough** for the locked species synchronization, Abomination, Archlich death handling or Dracolich. A purpose-built NeoForge **Too Much Magic Transformations** bridge is unavoidable. Reuse supported APIs and datapacks first, then add narrowly targeted mixins for unreachable behaviors; avoid replacing whole upstream mods.

## E. Release-oriented investigation/prototype sequence

**P0 — Source and exact-artifact audit**
- Fetch and pin correct NeoForge 1.21.1 builds for Vampirism, Werewolves, Dragon Survival, Goetied and JAWR 0.5.39; confirm dependency closure. Do not enable on production worlds.
- Inspect JAWR 0.5.39 and Forbidden Magic shipped data registries, commands, NBT/attachments, events, food/sun/death methods and quest persistence. Inspect Goetied lich handlers and Arca death event for its actual 3.0.19 port. Confirm Dragon Survival alter and first-login packet hooks.
- Document each native acquisition route, exact owner, persistent ID and hook; reference bytecode/class method names rather than guessing.

**P1 — Datapack proof**
- In disposable test instance, override **one** Vampirism skill node without changing any native player state; confirm data reload and UI.
- Add a **second** configured skill tree for a registered mock/custom faction and verify unlock predicates, node rendering and actual skill grant; measure whether two separate trees can be held by one active faction.
- Add approved food to Dragon Survival `diet_entries` using item IDs from the pinned registry; test all 3 species and world reload.
- Implement a **JAWR ritual** whose `witchery:run_command` calls an innocuous test command first, then switch to validated bridge command only after an atomic transition prototype exists.

**P2 — Bridge vertical slices**
- V1: Standard Vampirism vampire -> JAWR recognition at **level 1 only**, with one blood and sunlight authority; JAWR vampire rite -> standard Vampirism vampire; cure both. No free progress; retain both trees. Test both directions.
- W1: Same for werewolf pathways, including controlled beast form, plus no dual render/survival.
- H1: Hunter inoculation repels all accidental conversions, but a confirmed rite replaces Hunter safely.
- L1: JAWR lich + Goetied lich yields Archlich; track both progression states but one resurrection arbiter; debug Arca+phylactery failures.
- D1: Dragon + exactly one lich forms Dracolich, never Archlich; model/food/flight retained after respawn.
- A1: Register hybrid faction and prove four progression tracks persist across login, death and cure, before adding 1–2 hybrid skill trees.

**P3 — Combined acceptance**
- Execute [DEATH-COMPAT-TEST-PLAN.md](DEATH-COMPAT-TEST-PLAN.md) and a complementary matrix of all transformation acquisition routes on a disposable dedicated server + client. Document each tested exact version and pass/fail.
- Do not enable forbidden combinations, introduce an unofficial JEI/Curios item ID, change server configs or promote planned mods to installed without staged evidence.

**No code, datapack, config, JAR or integration test was created by this research note.**
