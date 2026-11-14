# Transformation bridge: transition preflight (2026-11-14)

**Implemented as pure advisory logic, not live enforcement.** See `compat/transformations/`.

## Locked target matrix

- Human may become Hunter, Vampire, Werewolf, one Lich, or Dragon.
- Vampire and Werewolf are unified physiology labels; each has independent Vampirism/Werewolves and JAWR provider branches.
- A Lich may acquire the other lichdom to become an Archlich.
- Dragon and one lichdom may form a Dracolich, regardless of acquisition order; dragon plus both liches is invalid.
- Hunter is exclusive, but an expressly voluntary replacement ceremony can remove Hunter and add one supernatural state atomically.
- Abomination requires Vampire + Werewolf + both lichdoms. From Archlich, the vampire and werewolf states **must be committed atomically**; two partial intermediate combinations are invalid.
- Ordinary acquisitions cannot remove a state. Cures must be explicitly classified as controlled cures and verified per provider. No data cleanup is currently implemented.
- Unknown or conflicting snapshots always fail preflight; no ticking purge or silent mutation. An existing conflict requires backup and administrative review.

## Implemented this pass

- `TransitionPolicy.assess(before, after, kind)` with `NORMAL`, `CONTROLLED_CURE`, `VOLUNTARY_HUNTER_REPLACEMENT`, `ABOMINATION_RITUAL`.
- Exhaustive 256-result-state unit test plus checks for upgrades, cures, hunter transitions, Archlich and Abomination.
- Two read-only Vampirism 1.10.x observers: vampire and hunter factions. These use the public `VampirismAPI.factionPlayerHandler(Player)`, `IFactionPlayerHandler.getCurrentFaction()` and `IFaction.getID()` shape from official 1.21 source; not the incompatible 2.x/FactionApi API.
- `/tmmtransform status` reports each provider as PRESENT, ABSENT, or UNKNOWN.

## Required before runtime promotion

1. Compile and test this exact commit; inspect CI for errors.
2. Confirm actual 1.10.13 jar API/binary and gameplay behavior, untransformed/vampire/hunter, death/relog and spectator cases.
3. Implement remaining JAWR, Werewolves, Goety, Forbidden Magic, and Dragon Survival probes from verified exact-version source/binaries; absent Dragon Survival is UNKNOWN until an explicit provider policy is justified.
4. Wire all acquisition routes to cancellable pre-events, beginning with Vampirism 1.21 `PlayerFactionEvent.FactionLevelChangePre` (source-confirmed, jar version and event ordering still to test).
5. Establish single authoritative food/physiology and resurrection owners and implement atomic multi-mod rituals with rollback. Corail Tombstone exclusively owns recoverable drops from actual deaths; prevented deaths make no grave. See locked D01–D24 death tests.
6. Run both-order transformation, cure, death, reload, multiplayer, UI/client and disposable-world tests before adding a compiled bridge to packwiz.

**Source references:** https://github.com/TeamLapen/Vampirism/tree/version/1.21/latest/src/api/java/de/teamlapen/vampirism/api and https://github.com/TeamLapen/Vampirism/blob/version/1.21/latest/src/api/java/de/teamlapen/vampirism/api/event/PlayerFactionEvent.java

No gameplay actions have been authorized by this preflight code; existing modpack lock and Monk Mod are unchanged.
