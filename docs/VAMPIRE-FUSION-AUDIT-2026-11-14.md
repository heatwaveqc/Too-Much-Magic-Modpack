# Vampire fusion — pinned binary deep audit and implementation contract (2026-11-14)

**Status:** Exact pinned binaries inspected in CI; first JAWR read-only observer,
blood arithmetic, and target ownership model implemented. **No live mechanics are
yet arbitrated or enabled in the pack.**

## Verified target artifacts

- Minecraft 1.21.1 / NeoForge 21.1.265 / Java 21.
- Vampirism `Vampirism-1.21-1.10.13.jar`, CurseForge file 8814121,
  SHA1 `349b6e4ea893c98a580775b1725399903e222228`.
- JAWR `witchery-0.5.39+mc1.21.1.jar`, Modrinth TWcJbsjg,
  SHA512 `a3d50e65bb494b4d9b709163a295ddcecf53c498314df473d6de55633a4a5ebe810b9a0b5672f97f41786c03af01ef3ac91f0262216e074887e75d6afc35141d`.
- [Pinned JAR hash and JVM signature audit — CI PASS](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43612789188).
  Inspected 2,452 JAWR classes and 1,518 Vampirism classes without
  publishing the third-party JARs.

## Source-backed contrasts

| Topic | Vampirism 1.10.13 | JAWR 0.5.39 | Arbitration |
| --- | --- | --- | --- |
| Vampire flag | `VampirismAPI.factionPlayerHandler(Player).getCurrentFaction().getID()` == `vampirism:vampire` | `VampirePlayerAttachment.getData(Player).isVampire()` (its `Data.getLevel()` records progress) | Both native states must coexist; no fake combined NBT tag |
| Progression | Native faction level / skill and lord trees | Native `VampireLeveling`, quest advancement, abilities | Two independent progression tracks; NEVER map either level numerically |
| Blood | `VampirismAPI.vampirePlayer(Player).getBloodStats()`: current/max blood and exhaustion/saturation | `BloodPoolAttachment.getData(LivingEntity)`: `bloodPool` and `maxBlood` | Target single canonical Vampirism pool; JAWR reads/writes require translator |
| Draining/food | `BloodStats.onUpdate()` rewrites FoodData and converts exhaustion | `VampireEvents.handleBloodHunger(ServerPlayer)` forces food 19 or 1 and spends 20 JAWR blood for each food point restored | **Direct conflict**; must suppress JAWR routine conditionally in dual state |
| Healing | Native blood regeneration and starvation weakness | `VampireEvents.handleBloodHealing` (75 JAWR blood per heal, fires cancellable `VampireAfflictionEvent.BloodHealing`) | Vampirism owns passive healing in dual state; cancel the JAWR event |
| Sun | Native sun exposure/level and sun-protection skills | `VampireEvents.handleSunExposure`, sun ticks, damage, armor sun resistance, dimension/weather checks, cancellable `SunDamageApplied` | One damage owner, but honor JAWR protections; blanket cancellation would erase JAWR equipment value |
| Garlic/fire | Vampirism garlic weakness and fire vulnerability | JAWR `tickGarlicExposure` and `handleHurt` contain extra checks | Must avoid stacked damage while preserving valid equipment, hazards |
| Bat mode | Vampirism native bat action | JAWR `BatFormAbility` | Preserve both progression unlocks but never allow simultaneous bat models |
| Feeding | Native blood drinking, bottling and blood grinder | `DrinkBloodAbility`, `BloodPoolHandler.increaseBlood/decreaseBlood`, Neo Vitae blood orb/altar integration | Translate JAWR blood gains/costs to the canonical pool at the source, not by tick-based bar mirroring |
| Death | Vampirism DBNO/downed-vampire behavior and eventual real death | JAWR configurable vampire immortality | Target Vampirism death interception in dual state; Tombstone handles actual death drops |
| Cure | Vampirism faction removal and cure mechanisms | JAWR Remove Curse ritual and Lilith garlic cure | Both native state removals must be transactional; never leave a half-cured vampire |

Official Vampirism 1.10 NeoForge docs:
https://wiki.vampirism.dev/docs/1.10%20NeoForge/wiki/factions/getting-started
and source:
https://github.com/TeamLapen/Vampirism/tree/version/1.21/latest/src/main/java/de/teamlapen/vampirism

JAWR public project changelog:
https://github.com/mrsterner/witchery-issues
and exact pinned release:
https://www.curseforge.com/minecraft/mc-mods/just-another-witchery-remake/files/9525978

**Important:** JAWR's publicly available GitHub project is an issue/docs
repository, not the pinned production source. The JAWR implementation claims
above were verified against the exact hash-pinned JAR bytecode with `javap`.

## Proposed invariant and state machine

The common *vampire identity* is acquired through either native provider, but
**both native vampire flags** ultimately need to be present to unlock the two
branches safely. The bridge must implement explicit two-provider transactions
with a backup, preflight, and postcondition. Do not just grant a hidden
duplicate status or set data unconditionally each tick.

- Human to Vampire: stage and validate both native acquisitions, then commit
  the shared identity and activate the two skill tracks at their normal entry.
- Single-provider vampire already on a save: report `PARTIAL`; never auto-join
  the other provider without an explicitly tested migration.
- Dual-provider vampire: both may keep leveling independently and unlock their
  unique actions and equipment; resource ownership becomes single-source.
- Vampire plus another allowed supernatural state (Abomination only under
  current locked rules) uses this *same* merged vampire subsystem, not a third
  source of blood or immortality.
- Cure: check both removal paths, preserve progress for restoration policy,
  revert in an orderly transactional process, validate both providers absent;
  if rollback cannot be proved, prevent initiation rather than corrupt player.
- Existing forbidden combinations: no automatic wipe; admin review required.

## Architecture and integration tasks

1. **Read-only detection** — implemented (Vampirism and JAWR faction probes).
   Observe both level and native blood pools using exact binary signatures.
   `/tmmtransform vampire` will expose diagnostic meter values.
2. **Supported provider pre-hooks** — Vampirism
   `PlayerFactionEvent.FactionLevelChangePre` is cancellable and verified in
   the 1.10.13 JAR. JAWR has a `LevelChanged` event, which is **NOT
   cancellable**, so intercept its entry routes before attaching a form.
3. **JAWR passive hunger interceptor** — exact private method
   `VampireEvents.handleBloodHunger(ServerPlayer)` is source-backed from
   bytecode. A conditional mixin is the probable implementation; do not ship
   until it is proven to apply only when both providers are active.
4. **Blood conversion** — scale by `current/max`, preserving 0 and full as
   endpoints; convert JAWR ability costs and rewards at the actual
   `BloodPoolHandler` transaction, not by periodically setting the two bars.
   Fractional cost accounting or explicit whole-unit rounding requires
   balance analysis (no free vampire abilities).
5. **Passive healing and sun** — JAWR fires cancellable `BloodHealing` and
   `SunDamageApplied` events; cancel duplicate ticks in dual state. Add
   equivalent effects for JAWR sun protection equipment and dimension rules
   under the one-owner sun pipeline.
6. **Visual and input state** — enforce one HUD and prevent simultaneous bat
   forms, without locking either mod's progression/abilities.
7. **Death arbiter** — prioritize preventative Totems/poppets then only one
   vampire death veto; Corail Tombstone is grave owner for genuine death.
8. **Cure, data migration and multiplayer** — account for infection, ritual,
   creative commands, alternate respawn, disconnect, potion, Lilith cure,
   Remove Curse, item abilities, PvP, and player isolation.

## Acceptance tests before inclusion in packwiz

- A normal human and a hunter remain unmodified.
- First vampire acquisition via Vampirism; first acquisition via JAWR.
- Distinct branch level-ups all the way to each native maximum; both
  progressions remain attainable and their earned abilities work.
- Blood at 0/half/full; ambient depletion; feeding animals/villagers;
  bottled blood, blood grinder, JAWR bottles, Neo Vitae blood orb and altar;
  high-frequency casting costs and hunger/starvation.
- JAWR sun grenade/pendant, armor sun resistance, Vampirism sun skills,
  sun/rain/dimension change, garlic/fire/wood vulnerability.
- Bat -> normal -> alternate bat, keybinds, model/HUD after relog.
- One lethal hit and double lethal hits, Totem and poppet ordering,
  DBNO, JAWR immortality, actual death/Tombstone grave once.
- All cure routes, inventory, skills, buffs, client/server reconnect,
  multiplayer isolation, packet sync and save migration.
- Additional test with Abomination model and lich resurrection owner when
  those states are implemented.

**Proven:** pinned binaries and public method shapes, build/logic testing.
**Not proven:** any live dual-vampire food or death arbitration, or
production survival readiness. No third-party JARs have been redistributed.
