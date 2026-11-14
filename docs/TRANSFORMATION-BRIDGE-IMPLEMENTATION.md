# Transformation bridge — implementation gates

Status: **scaffold only** on `feature/transformation-bridge-scaffold`.
This is the intended technical direction, not an assertion of runtime compatibility.
The earlier `TRANSFORMATION-COMPATIBILITY.md` one-physiology exclusion
matrix is historical research. The owner's later target decisions govern this work.

## Target form classification

- Human/untransformed.
- Hunter: exclusive of supernatural states; remains a faction/profession.
- Vampire: common base physiology with independent Vampirism and JAWR branches.
- Werewolf: common base physiology with Werewolves and JAWR branches.
- Lich: Goety or Forbidden Magic.
- Archlich: both lich systems.
- Abomination: vampire + werewolf + both lichdoms, with custom progression.
- Dragon: dragon physiology, no vampire, werewolf, hunter or abomination.
- Dracolich: dragon plus **one** lichdom, never archlich.

These labels are a **target state vocabulary**. The in-code resolver only
classifies *fully observed* snapshots; it cannot permit acquisition or make
the referenced upstream mods coexist. The mod has no probes installed.

## Design partition

- Body, undeath/curse, faction/profession, temporary form are distinct axes.
- The underlying mods own their unlocks/progression, with the bridge
  coordinating only mechanics that actually conflict.
- Shared resource meters, feeding, model/body, resurrection and cure need
  an explicit owner for each permitted compound state.
- Monk is a dedicated Vampirism-style playable faction in `compat/monk/`,
  not an additional permanent physiology. Monk membership is exclusive of
  Vampirism vampire/hunter/werewolf factions and all external supernatural
  forms, including lich and dragon. The Monk exclusion policy is only modeled
  in source and **not yet enforced by live transformation hooks**.

## Adapter acceptance gates (per mod/version)

1. Confirm actual installed jar/version, mod ID, persisted player data and
   access API; no guessed NBT tags or registry IDs.
2. Implement **read-only probe** and test every known acquisition, removal,
   progression and respawn route against live provider state.
3. Verify cancellable hooks for *each* acquisition path before implementing
   any prevention. Unknown coverage means the path must remain disabled.
4. For explicitly allowed stack, define singular owners for diet/blood/hunger,
   immunity, movement/flight, rendering and resurrection. Build reversal plan.
5. Test at least both A->B and B->A, all inventory/skill/cure consequences,
   world save/reload, server/client HUD, and multiplayer isolation.
6. Run clean dedicated-server boot AND real client gameplay. Save logs.
7. Only then enable a feature flag and consider inclusion in packwiz.

## Dragon Survival now approved (candidate; not installed)

Official 1.21.1 NeoForge Dragon Survival **v2.0.74**, CurseForge
project 420799 / file 9539454 is now listed in `manifest/mods.json`
with `desired=true`, `enabled_in_pack=false`. GeckoLib is
already present in the installed dependency set, but the exact new
combined load/animation/state interactions still need server and
client tests. This **does not** imply a Dragon Survival implementation
or functional transformation compat.

Dragon Survival is the intended primary dragon-form provider and
progression engine for the future **Dracolich** state. The bridge
must combine **one** of the two lich systems with Dragon Survival
and reject a second simultaneous lichdom, vampire, werewolf,
Abomination, Hunter or Monk. Native dragon growth/abilities remain
owned by Dragon Survival; the bridge must not duplicate that
progression. For Monk barehand harvest tiers, its split
harvest-bonus/speed code is a mechanical reference only; Monk
cannot become a dragon.

## First pilot candidate

Start with passive **Vampirism** and **JAWR** vampire-state observers, using
pinned 1.21.1 sources. Do not attempt the unified vampire physiology until
both probes distinguish *untransformed* from *unreadable* reliably.

## Bootstrap files

`compat/transformations/` contains Gradle project, core snapshot classifier,
extension interfaces, review-data attachment, read-only diagnostics and unit
tests. Workflow: `.github/workflows/transformation-bridge.yml`.
