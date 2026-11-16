# Hunter implementation — required delta from the standalone progression core

**Reviewed:** 2026-11-15. **Source:** live `test/full-integration` `compat/progression` implementation, namespace `tmm_progression`; [successful core CI run](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43758086560). **Intent:** retain the core as independent shared infrastructure, create Hunter as a standalone gameplay module requiring that core but **not** Tyz, and add optional pack adapters. This document is not a claim that Hunter has been implemented.

## Hunter initiation design — Trial of Venom (owner-proposed, 2026-11-15)

**Use existing vanilla items and effects. Do not introduce a custom potion, crafting recipe, or mandatory new item.**

A human player earns the vanilla **Monster Hunter** advancement (`minecraft:adventure/kill_a_mob`, verify advancement ID against pinned runtime), gains **Regeneration** from *any* valid source, then **drinks** a standard vanilla Potion of Poison while Regeneration is still active. This is a one-time initiation ritual.

**Accepted consumed potion contents** (Minecraft 1.21.1): `minecraft:poison`, `minecraft:long_poison`, `minecraft:strong_poison`; `minecraft:potion` item only. **Not accepted:** splash potion, lingering potion, poison damage, poisonous food, applied potion effect without drinking, other-mod mixtures, or potions with custom replacement effects unless separately approved.

**Server-side event:** `LivingEntityUseItemEvent.Finish` (NeoForge 1.21.1), which provides the consumed-item snapshot, even though the potion effects have been applied by the time the callback fires. Verify `DataComponents.POTION_CONTENTS`/vanilla `Potions` holder, the advancement, **Regeneration active at drink completion**, and the player's native/supernatural eligibility. Invoke only a trusted server-side core join API with a Hunter-specific join policy. Do **not** join from the mere presence of Poison + Regeneration status effects or from item-use Start, to prevent splash/application false positives and aborted drinking.

On successful enrollment: set persistent Hunter identity; grant baseline Hardened Physiology (Poison immunity and intended detrimental-effect duration reduction), immediately clear Poison applied by the completed potion, send initiation feedback; do not repeatedly grant awards on future poison consumption. A player who fails any gate simply consumes the potion normally. Existing alternate supernatural forms fail closed until a verified conversion/eligibility adapter exists; status-effect cleansing alone is not conversion prevention.

**Cheap vanilla pathway:** golden apple grants Regeneration II for 5 seconds, enough for the ~1.6-second potion consumption if the player switches promptly. Brewing or other modded Regeneration sources work as well. This does not imply that vanilla Potion of Poison is brewable without Nether Wart, only that the ritual uses no new custom recipes.

**Edge-case tests:** preexisting Monster Hunter from old worlds; prior and active Regen; exactly expiring Regen during the drink; canceled item use; poison II/extended; splash/lingering exclusion; duplicate event/respawn/reload; species-incompatible form; creative/spectator; consumed potion on failure; interruption; modded potion component spoof; server/client authority. `Finish` reports post-item-use state, so verify that a normal vanilla poison drink cannot supply its own Regen (matching exact base potion prevents this).

## Core already provides

| Requirement | Core implementation |
| --- | --- |
| Per-player persistent XP/rank/points and purchased skills | `Ledger`, `Track`, `ProgressionAttachments` (copy-on-death) |
| Data-defined paths and server reload | `PathJson`, `PathRegistry` |
| Configurable rank curves (1–200 thresholds), milestones and skill prerequisites | `Definition`, `ProgressionMath` |
| Native skill-buy, respec, and admin QA commands | `ProgressionApi`, `ProgressionCommands` |
| Attributes and conditional effects | `EffectRuntime`; empty-hand and unarmored modifiers |
| Target/weapon tags and direct-melee bonuses | `Selectors`, `melee_bonus` |
| Basic incoming damage mitigation, mining speed and effective harvest tier | `damage_multiplier`, `break_speed`, `harvest_tier` |
| Extensible registered Java effect handlers | `EffectHandlers.register` and event hooks: tick, incoming, harvest, break speed, block break, kill |
| Trusted mod-controlled custom milestones/events | `ProgressionApi.recordExternal` |

**Current Hunter pilot is not the actual Hunter:** `hunter.json` defines only Undead/Raider kill XP and three skills; both pilot `hunter.json` and `monk.json` have `exclusive_group: ""`. Both are demonstration datapacks, and the Hunter taxonomy from [PR #97](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/97) is not imported.

## General-purpose CORE changes needed before an independent Hunter release

**P0 — Enrollment/identity policy and lifecycle (core API).**
- Shared `exclusive_group` must be nonempty when Monk and Hunter are mutually exclusive.
- Provide an external, server-side `canJoin` / `preJoin` veto or participant policy and an invariant checked **inside** `ProgressionApi.join` (not only in an enrollment NPC/ritual). In current code the OP command calls `join` directly and the method checks only other joined paths sharing the string group; it cannot test species transformations.
- Provide controlled `leave`, reset/cure and rejoin behavior, and a transition callback, with deterministic effect cleanup. Hunter transformation immunity requires explicit adapters to other species mods; a poison status handler alone cannot prevent involuntary transformation.
- Add a queryable player identity/eligibility API that separate Hunt, Monk and optional transformations compatibility modules can use without a hard dependency on Vampirism, JAWR or Dragon Survival.

**P0 — Trusted reward provenance and antifarming policy (core API).**
- Current `LivingDeathEvent` records a kill by immediate source `ServerPlayer`; it does **not** exclude tame entities, owned/summoned mobs, indefinite re-summons, social traders or artificially duplicated kill credits. It cannot reliably credit every spell or owned projectile to the hunter.
- Add a trusted `ActivityGuard` / `KillContext` seam invoked before XP and milestone counters: victim UUID/type, player ID, direct/source/owner provenance, mob origin if available, summon/master, game mode, and deduplication/award token. If a signal lacks required provenance, fail closed for Hunter rewards.
- Support data-driven `hunt_reward_policy` tags and per-mob exclusions layered atop the already verified [candidate-runtime-crosswalk.csv](candidate-runtime-crosswalk.csv). General core should know **eligibility policy extension**, not hardcode the Hunter taxonomy.
- Allow hunter-authored validated discoveries/contracts/track-and-kill steps to submit safe events; the current external signal API is useful for fixed awards, but a generalized event context/counter schema will avoid proliferating hardcoded placeholder events.

**P1 — Path presentation metadata and client-safe state synchronization (core API).**
- Current `Definition.Skill` is one flat list, with no branch/tree metadata, tier display, icons, or a discovery-visibility field. Hunter needs many species/faction specialization trees. Add optional `branch` / `category` / icon/display-name/description IDs and rank/point budgets to the schema, or a supported public API for a Hunter-supplied branch registry. Data-level skill prerequisites already exist.
- No network sync or skill GUI yet. Expose server-authoritative **read-only** snapshot/branch graph and validated buy/activate requests. Hunter's Journal can implement its own screens, but needs an approved view/subscription protocol. Keep UI optional for headless server use.

**P1 — Active-ability/interaction dispatch (general API, can begin Hunter-local).**
- The current custom handler extension has tick, incoming, harvest, breakSpeed, blockBreak, kill. It has no standardized activation/keybinding/cooldown channel, effect-application event, discovery/scanning event, player-victim hunting event or projectile-specific lifecycle. Add a secure server-validated activation request and cooldown state to core for reusable skill mechanics; Hunter can implement tracking, countermeasure and projectile events as its own NeoForge listeners initially.
- Current built-in `melee_bonus` only applies if the attacker is the **direct** damage source player; that excludes bows/crossbows and many spell projectiles. A Hunter Java handler **can** use `EffectHandlers.incoming` today to evaluate ranged damage, so a new core ranged handler is convenient but **not a blocker** if Hunter supplies one. Preserve projectile owner and weapon snapshot to avoid incorrectly using the currently held item.
- Poison immunity and harmful-effect duration reduction should be event-based Hunter handlers. If promoted to generic core effects, register safe `effect_immunity` and `effect_duration_scale` effects by ID/tag; protect bad-omen/raid/trial progression exceptions.

**P1 — Resilient effect handling.**
- `EffectRuntime` reconciles transient attributes and removes them using a process-local `previousAttributes` map. Add tests for server restarts, invalid datapack reloads, post-death rejoin, and path leave. The current README explicitly warns that no actual in-game acceptance tests have passed. Avoid claiming Hunter immunity or armor state survives lifecycle until these pass.

## HUNTER implementation responsibilities (not reasons to bloat the shared Core)

1. **Datapacks/registry:** full class path, general skills, and separate specialty branches based on [creature-inventory.csv](creature-inventory.csv). **Dinosaurs are Beasts; never a Prehistoric tree**. Spectral dead are Undead. No Mage Slayer, Boss, dimension-general or explosive-specialist trees.
2. **Initiation & exclusive human identity:** player-facing Hunter initiation, actual species checks and conversion prevention/retaliation exemptions. The core owns generic enrollment policy; Hunter supplies its eligibility/compat adapters.
3. **Hardened Physiology:** immediately granted poison immunity, status duration reduction (initial proposal 25%), immunity to involuntary vampire/werewolf/dragon/etc transformation via verified provider interception rather than broad speculation. Trial Omen/Bad Omen are configurable exclusions.
4. **Hunt loop:** tracking, bestiary/journal, observation/discovery, contracts and hunt credit; no XP from friendly traders, owned minions, summons/automatic farms or inert entities.
5. **Combat specializations:** data-tagged bonus against creature lineages/factions, crossbow/bow/weapon modifiers, defenses and active counters unique to prey families. For multi-category entities (e.g. Ender Dragon = Dragon + Void) implement a cap or diminished stacking rather than unbounded bonuses.
6. **Standalone visual/gameplay progression:** Journal interface, clear next goal, learnable basic/general skills, visible ranks and character choices. Tyr integration is optional; Hunter's native progression never uses Tyz as a required points wallet.
7. **Optional integrations:** enhance tag support for Cataclysm, Goety, Iron's, Witchery, Alex's Caves **only once its NeoForge port is approved**, and other installed mods. Add faction neutrality to the pack compatibility bridge separately: vampires neutral to most ordinary Undead; werewolves to most ordinary Beasts; social/owner/retaliation/boss exemptions take precedence.

## Suggested build order

1. Ask core owner for **two P0 APIs**: join veto/leave and trusted contextual XP guard, plus basic branch metadata/sync contract (P1) before authoring dozens of trees.
2. Start a **separate `compat/hunter` module** depending on `tmm_progression` only; include vanilla categories, identity/poison baseline and one Undead specialization (MVP). Do not reuse the current demo path as production without replacing its kill awards.
3. Validate Hunter standalone with no Tyr/other magic mods, in a real NeoForge client + dedicated server. Then add modpack data tags and optional faction compatibility.
4. Expand all specialized trees after baseline anti-farm, serialization, UI and damage stacking tests pass.

## Current code caveat

The code is **compiled and unit-testable**, and the [progression-core CI](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43758086560) has a successful run. **That is not proof of a full Hunter/Monk gameplay build, nor proof the module is installed in the pack**. `compat/progression/README.md` still labels it an early implementation requiring in-game acceptance. This document requests shared API work; **it does not implement these APIs**.
