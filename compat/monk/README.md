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
