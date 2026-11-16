# Trader's Vengence (experimental)

Isolated NeoForge 1.21.1 / Java 21 module, branched from `test/full-integration`.
**Source prototype: Java compile, JAR assembly, and unit tests passed** in
[quick CI run 43885237106](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43885237106).
**Not tested in a live pack or game world.** This branch does
not edit the packwiz index or the running QA build.

## Implemented in source
- World-global permanent offender UUID list saved via Overworld SavedData.
  Three offense triggers share the same permanent record and admin pardon:
  (1) player kills a wandering trader; (2) player deals actual health damage to
  a villager or iron golem; (3) an Iron's `irons_spellbooks:priest` takes a
  player as its current attack target. Direct melee, arrows, and player-attributed
  spell damage all credit the responsible player; canceled/zero-health-damage
  attacks do not count. Merely angering a priest below aggression threshold does
  not count. A priest must actually target the player, rather than simply
  see/trade with them.
- New wandering traders blocked unless their spawn feet have sky access, are within
  10 blocks of dimension sea level, and stand on `#traders_vengence:valid_trader_ground`.
  Existing traders loaded from disk are preserved.
- A vanilla sign with `No Soliciting` or `[No Soliciting]` on ANY line, front or
  back, blocks new traders within 32 blocks. Signs are read only in loaded chunks;
  ordinary sign blocks suffice (custom sign item/art deferred).
- Golems and llamas independently target blacklisted players; llamas inherit their
  vanilla spitting mechanic. Villagers get generic `RangedAttackGoal` and launch
  Iron's `magic_missile` entity at an offender (50-tick cooldown, 16-block range,
  6 damage). No mana/crafting or new gun system.
- Operator commands: `/tradersvengence status <UUID>`,
  `/tradersvengence pardon <UUID>`, `/tradersvengence accuse <UUID>`.

## Known limitations / testing blockers
1. `EntityJoinLevelEvent` has no spawn reason. For now the filter applies to ANY
   newly added trader, including `/summon` and spawn eggs. A future pre-spawn
   spawner hook should exempt commands and allow retrying failed candidate positions.
2. The original WanderingTraderSpawner may still consume a spawn attempt when a trader
   is vetoed. Ensure bad candidates don't unfairly exhaust attempts.
3. Villager brain-driven panic/job logic may contend with the added ranged goal. Verify
   that the goal executes in an actual world and add targeted brain arbitration if needed.
4. Iron's projectiles are instantiated via its registered entity type, with reflective
   `shoot(Vec3)` and `setDamage(float)` calls. These must be validated against the
   **pinned Iron's 3.16.3 runtime**; compile alone won't test them. Failures log once.
5. Priest target observation is source-implemented but still requires an in-world
   check with the pinned Iron's 3.16.3 jar. Confirm hostile target acquisition is
   observed, and that other players are not wrongly flagged.
6. Goal installation on entity joins is unverified for repeated join/unload cycles.
   Also test villagers, player-created golems, trader llamas, ordinary llamas,
   creative/spectator immunity and cross-mod hostility overrides.
7. Default sign radius, attacks, and damage are currently constants. Configuration,
   unique sign item, in-game repentance and localization are deferred.

## Quick checks (no modded smoke without explicit permission)
From this directory, with Gradle and a reachable NeoForge dependency cache:
`gradle --no-daemon test build`

Test in a disposable save after an owner-authorized test build:
- Place a normal oak sign reading `No Soliciting`, attempt normal trader spawns
  inside and outside 32 blocks.
- Check sea-level Y 53 and 73 are accepted, Y 52 and 74 rejected, and only tagged
  ground blocks work with unobstructed sky.
- Kill a trader with direct hit then projectile, verify UUID persists after server
  restart, and golem/llama/villager retaliation occurs only for the offender.
- On a fresh unblacklisted player, strike a villager or iron golem (melee, then
  player-fired projectile); confirm all three defender types target that player
  and the new status persists after relog/server restart. Confirm canceled damage,
  environmental damage, and hits from another mob do not falsely blacklist.
- On another fresh unblacklisted player, provoke an Iron's Priest until it truly
  selects the player as its attack target; confirm the permanent village vendetta.
  Verify approaching or trading with a neutral Priest does NOT create an offense;
  also verify transient below-threshold anger does not count.
- Exercise operator pardon and make sure combat stops even after reloading chunks.
- Confirm villager trading and schedules return to normal for non-offenders.

Do not merge or publish this as a gameplay-tested binary until runtime AI,
spawn restrictions, and Iron's projectile integration are verified in a disposable world.
