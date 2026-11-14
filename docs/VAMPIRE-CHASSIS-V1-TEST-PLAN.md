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

## Live owner-reported in-game results — 2026-11-14

The owner tested an experimental dual-vampire Prism client. This is a
**user-reported functional pass**, not a log-verified/automated regression;
no numerical before/after screenshots or client log were supplied.

| Case | Observation | Status and remaining limitations |
| --- | --- | --- |
| 2 — Vampirism first | Vampire Fangs -> Sanguinare Vampiris expired -> Vampirism and JAWR vampire states both PRESENT. First attempt failed because experimental flags were OFF; enabling chassis/acquisition and unknown-provider *test-only* override resolved it. | **PASS** for automatic dual enrollment with test flags; progression not fully exercised. |
| 3 — JAWR first | Drank creative-spawned Lilith blood, both native vampire states PRESENT. | **PASS** for reverse enrollment; full survival Lilith ritual, native level-up, save/rejoin, dimension travel and multiplayer remain OPEN. |
| 5 — Vampirism feeding | Suck Blood key worked on cow; shared blood meter increased. Chicken did not work because Vampirism assigns chickens zero native blood. | **PASS** for cow feeding. |
| 5 — JAWR feeding | Selected JAWR Drink Blood in its ability bar and right-clicked entity successfully. JAWR blood drink item also restored the depleted shared meter. | **PASS** for both JAWR feeding methods; no measured fractional/carry stress test yet. |
| 4 — Unified spending | Not yet tested. | **OPEN** and **next priority**. |
| 5–10 other cases | No validated Neo Vitae blood transfer, cross-mod expensive abilities, hunger, weakness/cure, multiplayer or rollback matrix. | **OPEN**. |

## Next active test — case 4: debit canonical blood exactly once

Use a disposable, backed-up dual-vampire world. Choose a **JAWR ability
with an explicit blood cost** (not Drink Blood). If none is unlocked at
level 1, earn one through native JAWR progression and mark this case
BLOCKED until then; do not substitute a free passive ability.

1. Start with approximately half blood (room to gain, enough to spend).
   Run `/tmmtransform vampire`. Record **Vampirism current/max** and
   **JAWR current/max** as `V0/Vmax` and `J0/Jmax`.
2. Cast the costly JAWR ability **once**. Confirm its effect occurs.
   Run diagnostics again: `V1<V0`, `J1<J0`, with JAWR tracking the
   proportional Vampirism canonical fraction. **Neither** a zero canonical
   debit nor a double debit is acceptable. The meters need not use equal
   absolute units.
3. Cast twice slowly, then several times rapidly (respecting cooldown).
   Each successful cast must debit once; rejected casts must debit zero.
4. Repeat with an unlocked **Vampirism-native costly ability**; JAWR
   shadow must follow canonical blood depletion, without a second charge.
5. At **near-empty** and **empty** canonical blood, verify costly abilities
   cannot produce free effects, blood cannot become negative, and small
   JAWR charges don't round down to zero.
6. Save/quit/rejoin, rerun diagnostics. Both provider states and equivalent
   canonical/shadow fractions must persist. On admin-hold, unexpected
   blood jumps or error logs: STOP, retain `latest.log`, diagnostic
   before/after, and backup.

Success requires actual numbers; the previously confirmed successful feeding
**does not certify unified spending**.

## Requested next-build client HUD candidate

**Colorful Hearts** 10.5.9 (NeoForge Minecraft 1.21.1; Terrails;
[Modrinth exact release](https://modrinth.com/mod/colorful-hearts/version/us0gze50))
was explicitly requested for the next combined test build. Client-only,
configurable colored heart layers instead of stacked health/absorption rows.
Desired only in `manifest/mods.json`: **not yet packwiz-installed**.
Next client test: 10/20/50+/100+ hearts, half hearts, absorption, negative
status colors, and HUD rendering with Vampirism, JAWR and AppleSkin.

## Newly approved Mahou Tsukai interaction — Drain Life boundary

**Design required; not implemented or player-tested yet.** The pinned modpack uses
Mahou Tsukai 1.36.31 and the vampire bridge's authoritative blood pool is
Vampirism. Normal Mahou Boundary of Drain Life accepts each target and rewards
the caster in priority order: injured player -> healing; at full health,
hunger restoration (when `DRAIN_LIFE_IGNORE_HUNGER=false`); otherwise Mahou
mana. For a dual vampire, **replace only the hunger-restoration reward with
a canonical blood gain**, with JAWR's shadow derived from it. Never fill
`FoodData` or pay blood twice. Do not route Mahou **health healing** into
blood. At full health **and** full vampire blood, retain the native Mahou
regen reward. If Mahou's ignore-hunger config is enabled, retain its behavior
and do not grant vampire blood. Note vampires' managed `FoodData` may be
artificially nonfull, so inspect the **original reward selection branch**;
a post-hoc food-change watcher risks missed/repeated rewards.

**Implementation gate:** pin and inspect Mahou 1.36.31 bytecode/API for the
actual Drain Life reward method; place a server-side, source-specific
conditional interception at the hunger-reward stage when the caster is
both native vampires. Keep human and other nondual forms unchanged. Use a
bounded amount per accepted target, preserving native mana costs/radius,
attack frequency and player ownership, and explicitly decide a proportional
conversion if capacities differ.

**New acceptance test:** with an active controlled Drain Life boundary and
one live target, measure canonical/JAWR blood, player health, FoodData and
Mahou before/after (a) hurt vampire, (b) full-health partially hungry
vampire, (c) full-health full-blood vampire, and (d) human caster; additionally
toggle ignore-hunger, vary number of targets, test near-full, reload/dimension
changes, and confirm no duplication/free mana. This does not replace case
4's urgent JAWR-ability *spending* test.

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
