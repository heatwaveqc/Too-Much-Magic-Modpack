# Vampire chassis v1 — experimental integration and acceptance (2026-11-14)

**NOT SURVIVAL READY.** This branch integrates Minecraft 1.21.1 / NeoForge
21.1.265 / Java 21 with exact, hash-pinned **Vampirism 1.10.13** and
**JAWR 0.5.39**. Source code and a disposable server boot are necessary but
insufficient to establish in-world correctness. Never use a valuable player
save or existing world for the experiments below.

## Scope implemented

- JAWR `VampireLeveling.setLevel` mixin recognizes initial acquisition or
  removal; native Vampirism's cancellable `FactionLevelChangePre` and
  post-change event coordinate the opposite enrollment path.
- A dual vampire has two native provider progressions. Neither provider
  level is numerically mapped onto the other.
- JAWR player `BloodPoolHandler.increaseBlood/decreaseBlood` is redirected to
  Vampirism's native blood methods; a persistent fractional carry conserves
  small JAWR feeding gains. Vampirism blood changes refresh a *derived* JAWR
  shadow only after native mutations.
- JAWR blood hunger and ordinary player blood regen are suppressed for
  dual vampires. JAWR passive healing and sun damage are canceled via
  native cancellable events; JAWR garlic aura and duplicate fire damage
  are suppressed, but existing drowning/freezing/wooden-weapon behavior stays
  under JAWR. Empowered JAWR Sunstone protection affects Vampirism exposure.
- A dual-vampire one-sided cure is blocked. The explicit operator-only test
  cure preflights Vampirism's canLeaveFaction and removes both in one call.

## Config — all mutations OFF by default

Generated per-world server config:
`world/serverconfig/tmm_transformations-server.toml`
(path varies for integrated singleplayer under `saves/<world>/serverconfig`).

- `experimentalVampireChassis=false`: no survival/blood/cure mutations.
- `experimentalAutomaticDualAcquisition=false`: do not link separate native
  acquisition paths.
- `experimentalOperatorCure=false`: reject bridge test cure command.
- `experimentalAllowUnverifiedOtherProviders=false`: deny automatic enrollment
  when unknown other physiology providers have no verified observers.

Only in a **disposable test world**, after taking a manual backup, enable the
chassis and auto acquisition. With currently missing Goety, Werewolves,
Forbidden Magic, and Dragon probes, enabling the last flag is additionally
necessary to test transformations; it is intentionally unsafe and prohibited
on permanent worlds. Set all flags back to false on production worlds and
do not use a pilot-created dual vampire save when the pilot is disabled
(the native mods would again apply double survival rules).

## Test sequence (two separately backed-up disposable worlds)

1. Verify client and dedicated-server start. Capture `latest.log` and exact
   mod versions. Enter `/tmmtransform status` and
   `/tmmtransform vampire` as a fresh human; verify both readable `ABSENT`.
2. On World A, follow **Vampirism's native acquisition**. After the native
   change, both provider states must report `PRESENT`, starting at their
   distinct native levels. The other mods' default progression and skills
   should still unlock through normal play.
3. On World B, follow **JAWR's native acquisition**. Confirm both provider
   flags and native progression. Test save/reload, dimension changes, and
   a second, unaffected multiplayer player.
4. Drain canonical blood via movement/exhaustion and Vampirism abilities:
   JAWR's derived blood pool must decrease proportionately. Test JAWR
   abilities that consume blood: **only once** from canonical blood,
   including empty/near-empty and rapid repeated inputs.
5. Fill via Vampirism bites and bottled blood, JAWR bites/blood bottles and
   Neo Vitae blood-orb/altar. Test small JAWR gains that would ordinarily
   round to zero; repeated partial feeds must accumulate to canonical
   units without manufacturing extra blood. Check native capacity changes
   on both independently progressing skill trees.
6. Check hunger after repeated sprinting and under starvation. The
   `FoodData` value must not oscillate between the mods' 10 and 19/1.
   Verify no double ambient regeneration, no free casting, and prepared
   meals cause only intended vampire reactions.
7. Sunlight at JAWR low and high levels: no additional instant-death from
   JAWR when Vampirism supplies the one damage source. Check shelter,
   weather, other dimensions, native Vampirism sunscreen, empowered JAWR
   Sunstone, and JAWR sun-resistant armor. Armor's quantitative mapping
   is **not finished** and must be recorded as a remaining balance gate.
8. Fire, garlic field/crop, wood weapon, freezing, drowning: native damage
   should not be multiplied twice; unique weakness/immunity behavior
   remains. **JAWR direct garlic-crop damage still needs special testing.**
9. Try each native cure while dual. They must refuse to remove only one
   provider. On an independently backed-up operator-only world, enable
   `experimentalOperatorCure`, use `/tmmtransform vampire cure`,
   then confirm both flags absent and inspect native tasks/abilities,
   attachments, hunger, effects and inventory. If anything fails, STOP
   and restore the backup; the current test cure cannot fully restore
   native quest/skill mutations after an exception.
10. Use another untransformed player and verify their food, blood and buffs
    are unchanged; verify one-player leave/rejoin cannot affect another.

## Deliberately deferred to next pass

- Bat form/input exclusivity, cosmetic rendering/HUD unification, model changes.
- Death/DBNO, JAWR immortality, Totems, poppets, lich resurrection, Tombstone.
- Production-safe atomic rollback for cures and acquisitions; native cure
  rituals can expend ingredients before the intercept is reached.
- Quantitative mapping of JAWR sun-resistance equipment beyond the verified
  empowered Sunstone, and untested JAWR direct garlic attacks.
- Verified probes for other permanent forms, which are needed before
  removing the temporary unknown-provider test override.

## Promotion rules

`source compiled`, `unit tested`, `full server boots`, `client world loads`,
`both acquisition orders`, `blood economy`, `defense cases`, and
`cure + recovery` are separate gates. Do not change packwiz installed
pins or mark this `integrated` without all applicable live tests.
