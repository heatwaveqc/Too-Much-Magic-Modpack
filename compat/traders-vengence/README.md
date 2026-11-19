# Trader's Vengence (experimental)

Isolated NeoForge 1.21.1 / Java 21 source module, selectively reconciled from draft #219 onto the current `test/full-integration` baseline through PR #283.
**Source prototype: Java compile, JAR assembly, and unit tests passed** in
[quick CI run 43885237106](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43885237106).
**Not tested in a live pack or game world.** Source synchronization does **not** build or install the `traders_vengence` first-party JAR in Packwiz. An authorized disposable build is still required.

## Implemented in source
- World-global permanent offender UUID list saved via Overworld SavedData.
  The following offense triggers share the same permanent record and admin pardon:
  (1) player kills a wandering trader; (2) a villager receives vanilla
  `VILLAGER_HURT` or `VILLAGER_KILLED` reputation for a responsible player;
  (3) player deals actual health damage to an iron golem; (4) an Iron's
  `irons_spellbooks:priest` takes a player as its current attack target.
  The villager reputation signal is observed by a narrow Mixin on
  `Villager.onReputationEventFrom`; **vanilla gossip and prices are unchanged**.
  A narrowly scoped villager death fallback captures kills with no nearby
  gossip witnesses, so isolated villagers are still protected by the global
  blacklist. This fallback never edits gossip or trade prices.
  Iron golem damage remains a separate hook because vanilla only has
  `GOLEM_KILLED`, not `GOLEM_HURT`. Merely angering a priest below its
  aggression threshold does not count.
- The **vanilla WanderingTraderSpawner only** selects positions with sky access,
  ±10 blocks of dimension sea level, and `#traders_vengence:valid_trader_ground`.
  A bounded three batches of native position searches allow fresh candidates when
  terrain/sign checks reject a legal vanilla position. Existing saved traders,
  command `/summon`, spawn eggs and custom mod-spawned traders are not vetoed.
- A vanilla sign with `No Soliciting` or `[No Soliciting]` on ANY line, front or
  back, blocks new traders within 32 blocks. Signs are read only in loaded chunks;
  ordinary sign blocks suffice (custom sign item/art deferred).
- Villagers and wandering traders carry an NPC-only Caliber Arms M1911A1 pistol
  (7 damage, 35-block effective range, 420 RPM maximum, virtual magazine and reload).
  They target blacklisted players or the specific mob that injured them; never
  proactively hunt unrelated monsters. Normal hurt cooldown/armor applies.
  Caliber Arms model, texture and CC0 pistol/reload audio are bundled with full
  original notices. No player weapon item, ammo item, recipe, loot or donor mod.
- Confirmed villager-summoned iron golems carry the PneumaticCraft minigun
  model/texture with 50-block range, 6 HP per tick attempted after spin-up.
  Normal vanilla hurt resistance applies. Player-built/unknown golems remain
  melee defenders. Llamas retain their vanilla spit. Original PneumaticCraft positional firing loop and wind-down audio are bundled; spin-up uses a credited 1.19-second CC0 excerpt by SamsterBirdies (the donor HUD clip's redistribution license remains unverified).
- **Automatic pardon:** a fresh application or renewal of Minecraft's
  `Hero of the Village` status effect clears that player's saved blacklist
  entry permanently; an existing Hero buff is not a blanket immunity to
  committing new crimes. The player receives a forgiveness message when an
  actual pardon occurs. The status effect can also be awarded by commands/mods;
  the trigger is gaining the effect, not specifically the vanilla raid event.
  Repeated Priest targeting is tracked per-priest, so the same ongoing target
  does not instantly restore the blacklist after a pardon. New Priest target
  acquisition can still count as a fresh offense.
- Operator commands: `/tradersvengence status <UUID>`,
  `/tradersvengence pardon <UUID>`, `/tradersvengence accuse <UUID>`.

## Known limitations / testing blockers
1. **Source-only upgrade:** the new `WanderingTraderSpawnerMixin` redirects the native
   `spawn(ServerLevel)` candidate search, rather than vetoing all EntityJoin events.
   Verify the exact Minecraft 1.21.1 bytecode redirect target on a real NeoForge
   server: Java compilation alone does NOT guarantee Mixin injection success.
2. Vanilla can still consume its scheduled attempt if all bounded candidate searches
   fail because the sky/ground/sign filter finds no legal ground. This is expected
   when all nearby positions are forbidden; check rates in a disposable world.
3. Villager brain-driven panic/job logic may contend with the added ranged goal. Verify
   that the goal executes in an actual world and add targeted brain arbitration if needed.
   The reputation Mixin must be checked on the pinned 1.21.1 runtime (native
   callback signature and ordering, including direct and projectile hits).
4. Pistols use a local standard projectile-tagged damage type, native line-of-sight,
   existing target policy, and an independent NPC-only cooldown/magazine adapter.
   Confirm pose, attack, reload audio and no player acquisition in a real client.
   Caliber Arms' player-input, special ballistics, headshots, and distant sound
   simulation are deliberately not ported.
5. Priest target observation is source-implemented but still requires an in-world
   check with the pinned Iron's 3.16.3 jar. Confirm hostile target acquisition is
   observed, and that other players are not wrongly flagged.
6. Goal installation now uses the native entity GoalSelector to prevent duplicate
   goal registration after a rejoin on the same object; verify repeated join/unload
   and dimension transitions in a disposable save. Also test villagers, player-created
   golems, trader llamas, ordinary llamas,
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
  and the new status persists after relog/server restart. Also test a single
  isolated villager killed in one hit with no witnesses; the vendetta must still
  be applied. Confirm vanilla villager reputation events trigger our blacklist
  and ordinary gossip and price changes are still vanilla. Confirm no double-penalties to gossip, while golem
  assault is caught even without a villager present.
- Spawn a pillager or zombie alongside a villager; confirm the villager only
  retaliates after being attacked (not on seeing the mob), fires its pistol at the actual attacker, and doesn't attack nearby unrelated mobs.
  Confirm multiple villagers do NOT automatically join another villager's
  combat, unless they themselves are attacked. Test attacks from another
  neutral mob too, and verify the permanent player blacklist is unaffected.
- Confirm environmental damage does not cause mob self-defense, and normal
  villagers still trade and work when unprovoked.
- On another fresh unblacklisted player, provoke an Iron's Priest until it truly
  selects the player as its attack target; confirm the permanent village vendetta.
  Verify approaching or trading with a neutral Priest does NOT create an offense;
  also verify transient below-threshold anger does not count.
- With an active vendetta, win a village raid to obtain Hero of the Village;
  confirm the offender is pardoned immediately, receives the forgiveness
  message, remains pardoned after the effect expires, and normal villagers stop
  attacking. Test a newly committed offense while an old Hero effect is active.
- Confirm a still-targeting Iron's Priest does not re-accuse the player every tick
  immediately after Hero of the Village, including across a chunk reload. A
  genuinely new Priest attack target should count as a new offense.
- Exercise operator pardon and make sure combat stops even after reloading chunks.
- Confirm villager trading and schedules return to normal for non-offenders.

Reconciliation merges only reviewed source, metadata provenance and module
test contracts. Do not label the feature gameplay-tested or release a binary
until real 1.21.1 spawner-Mixin, AI, NPC projectile-damage, visual/audio and persistence tests pass.
See `docs/qa/TRADERS-VENGENCE-RECONCILIATION-2026-11-16.md`.
