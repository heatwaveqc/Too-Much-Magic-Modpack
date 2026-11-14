> **PRIMARY TYZ TREE OWNERSHIP (2026-11-14):** No second Monk Tyz engine.
> The primary global MXP/Tyz branch owns all shared skills and XP; this Monk
> module owns only its Vampirism faction state, permissions and original/unique
> Monk effect adapters. No repeated purchase of universal magic bonuses.
> See [cross-branch reconciliation](../../docs/MONK-INTEGRATION-WITH-PRIMARY-TYZS-TREE.md).

> **Current Monk catalog target (2026-11-14):** No Pufferfish Skills.
> Tyz 6.4.2 handles new rank purchases; Improvable Skills 3 remains a separate
> existing general perk engine; native Vampirism Monk faction handles identity,
> one-time powers and permission. See
> [v2 source-by-source placement](../../docs/MONK-SKILL-PLACEMENT-NO-PUFFERFISH.md).
> Older Pufferfish architecture descriptions below are historical. No actual
> launcher pack, mod installation, or game behavior is changed by this design.

> **Backend chosen 2026-11-14:** Tyz's Skills 6.4.2 for
> 99-rank leveled purchases, with x10 stage effect/cost.
> Monk native faction stores licenses/singular powers;
> Pufferfish retains nonoverlapping one-time nodes.
> Tyz approved, not yet installed; no live integration.
> See [tiered skill plan](../../docs/MONK-TYZS-TIERED-ADVANCEMENT.md).

# TMM Monk — Vampirism faction addon

**State: technical scaffold, not survival-integrated or playable progression.**

This module builds independently from `compat/transformations` and deliberately
targets the **Vampirism 1.10.x embedded faction API** in Minecraft 1.21.1,
not the later Vampirism 2.x standalone FactionApi.

## Planned identity (owner approved)

Monks surrender the use of conventional weapons and armor, forego supernatural
transformation paths (including dragons, liches, archliches, vampires,
werewolves, abominations and dracoliches), and gain more rapid development of
their faction skill tree, increased experience gain and superior magic use.
Exact bonuses, progression rates, exceptions for magical implements and
initiation rituals remain open to balance review.

A *real Vampirism playable faction* means only one Vampirism faction
membership at a time (vampire/hunter/werewolf/monk). Other mods' physiology
must be gated separately by the transformation bridge.

## Present source

- Separate Java 21 / NeoForge 21.1.265 Gradle project and CI.
- Monk faction registration through Vampirism's 1.10 API.
- Registration-time faction max is configurable in `config/tmm_monk-startup.toml`
  under `progression.max_faction_level` (default 20 rather than old provisional
  14). It is a **STARTUP** config because NeoForge registration precedes the
  COMMON/SERVER config loading phase; clients and servers need the same value.
  Restart both processes after changes; in-world rank requirements and large
  UI layout remain untested. Do not lower a cap under existing player levels.
- Monk player attachment, actions and skill handler.
- An **inert**, zero-point root skill and the Vampirism JSON skill tree,
  skill-node, configured-tree and localization files.
- Pure, unit-tested intended eligibility/equipment policy.

## NOT IMPLEMENTED

No real initiation trigger, advancement tasks, accelerated XP, magic bonuses,
unarmed abilities, gear enforcement, cross-mod exclusion or power trees.
`DisciplineRules` is a policy specification and is **not wired into live
events**. Do not report the rules as enforced. No survival release.

## Build/test

`cd compat/monk && gradle build` (Gradle 9.2.1 and Java 21).
After compilation, run a disposable world with exact pinned Vampirism and
Werewolves releases and verify faction registration, JSON load, player data
save/reload, death, commands and UI. Then implement one end-to-end initiation.

See `../../docs/MONK-MOD.md`.

## Faction level config and future progression

The former source hardcode `14` is no longer used. The NeoForge
`STARTUP` TOML value `progression.max_faction_level` defaults to 20,
permits up to the Java integer maximum, and is read before faction
registration. This is NOT a promise of functioning progression/UI at all
those ranks; test data-generation, commands, GUI and migrations before
choosing extreme values. NeoForge does not synchronize STARTUP config:
clients and servers **must have matching settings**. This does not
change Vampirism's own vampire/hunter/werewolf faction caps.

Pack-wide mod resource limits are tracked separately at
`docs/GLOBAL-PROGRESSION-CAPS.md`. The proposed five-tier rarity-food
infusion remains design-only. No decision yet on using Pufferfish to
own repeatable numeric Monk ranks.

## Live trait inventory diagnostic (new)

Run `/tmmmonk attributes` as an operator (permission level 2) **while
connected as a player** in a disposable 1.21.1 world containing the full
pack. The server writes `<world>/tmm-monk-player-attributes.json`, a
snapshot of the actual attribute instances on that player: resource ID,
base/current values, native bounds (where available). This command is
read-only; no attribute, item, skill or world-tier data is changed. No
personally identifying data is written. It is not an attribute unlock or
a list of mod-specific resource attachments.

Static source candidates:
`profiles/attribute-candidates-1.21.1.json` (142 IDs after removing a
commented-out Ars definition), `profiles/non-attribute-trait-candidates.json`.
Design-only world boss and flight milestones:
`profiles/world-tier-unlocks.json`. Actual in-game verification remains
necessary; no boss events or flight handlers have been implemented.

## Original trait coverage and mining/flight

`profiles/original-monk-abilities-1.4.json` now crosswalks all 25
registered original Monk abilities. Some match ordinary player
attributes while unique behaviors (Catch Arrows, Withering Stare,
Creeper Kiss, Water Walking, Glass Touch, etc.) require actual
server-side code. The classic-only Creative Flight ability is NOT
in the TMM skill tree; the permitted Draconic Curio supplies it.

Harvest capability is now explicitly two different mechanics:
a **finite** barehand harvest-tier track bounded by real tool
requirements, and a **repeatably improvable** harvest-speed trait
with rising costs. Dragon Survival 1.21.1 already models the
two separately; its source is a design reference rather than an
active dependency. Confirm Dragon Survival's actual pack pin
separately (absent from this branch's manifest snapshot).

## Proposed skills and separated decisions

See `../../docs/MONK-SKILL-CATALOG.md`. The backend-neutral catalog lists **172 core/mod-specific skills** and **98** selected Apotheosis affix techniques (Unbound excluded). Models are `once`, `staged`, `bounded`, `repeatable`, or `finite_source_rarity`; **no skill owner or progression location is chosen yet**. Includes an explicit Creative Flight exclusion and Dragon Survival harvesting tier compatibility as an approved-but-not-installed upstream candidate.
## Two-layer upgrade design

A one-time Monk skill is a faction skill (Step Assist, Momentum Glide,
arrow interception, etc.). Every leveled Monk discipline has a faction
**permission** node and invests via a proposed *unified* Pufferfish
skill, not duplicate mod-specific stats. The Pufferfish backend remains
an **experiment**: documented skill nodes are binary and cannot
indefinitely level without generated nodes or a custom bridge/UI.
See `../../docs/MONK-TWO-LAYER-SMOKE-TEST.md`.

## Ranked backend alternatives

[Full assessment](../../docs/MONK-SKILL-BACKEND-EVALUATION.md):
Improvable Skills 3 is the first candidate for an addon-registered,
Monk-gated leveled skill because it already stores per-skill ranks and
checks `canUpgrade` on normal server purchases. The short rank limit,
vanilla XP costs, separate GUI and unguarded skill-scroll path require
explicit testing before integration. No implementation or mod roster
change has been made.
