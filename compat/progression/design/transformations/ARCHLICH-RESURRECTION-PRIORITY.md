# Archlich resurrection priority — pinned 1.21.1 test target

Status: **Implemented as an experimental handler enabled by default in pre-alpha QA.** Not gameplay validated,
not stable. Enabled only for players who are simultaneously a **Goety lich**
and a **JAWR: Forbidden Magic lich**. Neither standalone lich is modified.

## Rule and native resource ownership

1. On an uncanceled `LivingDeathEvent`, an HIGHEST-priority TMM handler checks
   both upstream lich flags from their own runtime attachments. It does not
   infer lichdom from a custom marker or merely loaded mods.
2. If the JAWR lich is level 2+ **and** has at least one charged phylactery
   soul, TMM invokes Forbidden Magic 0.5.37's **original**
   `LichdomEvents.onDeath(LivingDeathEvent, LivingEntity)` handler.
   That native function selects/drains a soul, restores health/position
   depending on the soul class, clears effects and credits its original
   `LichdomLeveling.onPhylacterySave()` advancement.
3. A successful native phylactery save **cancels the same death event**.
   Under NeoForge's normal cancellation semantics, normal-priority
   listeners of Goety and JAWR do not process a second resurrection.
4. Without a charged JAWR soul, TMM does **not** cancel, move, revive or
   charge the player. Native Goety `SoulEnergyEvents.onLivingDeathEvent`
   runs unchanged: Arca teleport, 1 HP, Soul Energy charge/penalty.
   If neither can help, natural death proceeds.

Pure Goety and pure JAWR liches retain their own behavior. No new custom
soul currency, death destination or resurrection advancement is introduced.

## Safety

- Controlled by `tmm_archlich-server.toml` (SERVER config):
  `experimentalArchlichPhylacteryFirst = true` is **default true** in new test-world server configs. Set false to disable.
- Runtime gates inspect real provider APIs, not guessed NBT. Active only for
  precisely the pinned `goety:3.2.2` and
  `witchery_forbidden_magic:0.5.37+mc1.21.1` version prefixes.
  Unknown classes/versions log errors or defer to upstream handlers.
- JAWR's native `getCurrentSouls(ServerPlayer)` is consulted on the same
  `ServerLevel` as the subsequent native save; no manual soul spending.
- Reflection failure during native execution is logged as a serious QA error.
  It is **not** a transactional rollback; a thrown native error after
  debiting a soul could leave partial effects. Back up saves, test carefully,
  never enable in a persistent world before successful client acceptance.
- This handler does **not** implement acquisition of both lichdoms, shared
  hunger value 17/saturation 0, coordinated cures, phylactery dimension
  access or the dual-source resurrection conflict in other hybrid states.
  These are separate integration gates.

## Evidence / source contracts

- Goety [3.2.2 source](https://github.com/Vivideru/Goety-3/blob/main/src/main/java/com/Polarice3/Goety/common/events/SoulEnergyEvents.java):
  default-priority `@SubscribeEvent onLivingDeathEvent` controls Arca,
  Soul Energy and Soul Hunger.
- Forbidden Magic exact `witchery_forbidden_magic-0.5.37+mc1.21.1.jar`:
  `LichdomEvents.onDeath`, `LichPlayerAttachment.getData`,
  `LichdomSoulPoolHandler.INSTANCE.getCurrentSouls`. Signatures verified
  from the pinned JAR's `javap` report in full-integration run #72.
- NeoForge priority/cancel semantics:
  [events documentation](https://docs.neoforged.net/docs/1.21.1/concepts/events).
- Unit test: `ArchlichResurrectionPolicyTest` includes dual charged,
  empty soul fallback, pre-level-2, standalone and pre-canceled event.

## Read-only diagnostic

Before the first fatal test, run `/tmmtransform archlich` in the disposable
test world. It reports whether the feature is enabled, the two actual lich
flags, Forbidden Magic level, the number of charged JAWR phylactery souls,
and whether a phylactery-first attempt is planned. It performs no
transformation, expenditure or resurrection. If the state cannot be read,
stop the test and inspect the mod versions.

## Mandatory in-game test matrix

Disposable world, backup first. New-world server config defaults to true; existing config files retain their stored value. Check `/tmmtransform archlich` before testing.

| Test | Expected result |
| --- | --- |
| Pure Goety lich + charged Arca | Native Arca save only |
| Pure Forbidden Magic lich + charged phylactery | Native phylactery save only |
| Archlich + **greater soul**, charged Arca | JAWR consumes exactly 1 soul; health=50%, stays near death; **0 Goety Soul Energy cost**; JAWR progression credited once |
| Archlich + **lesser soul**, charged Arca | JAWR consumes exactly 1 soul; health=10%, teleport to phylactery; **0 Goety Soul Energy cost** |
| Archlich, **0 phylactery souls**, charged Arca | Native Arca save at 1 HP; normal Goety cost/effects; 0 JAWR progression credit |
| Archlich, no usable phylactery, no Arca | Normal death or native Goety state consequences |
| JAWR lich **level 1** | Never shortcut into JAWR phylactery save |
| Repeated deaths | Exactly one resource charged and one location/health restoration per fatal event |
| Cross-dimension and phylactery removed at last second | Inspect logs; don't assume cross-dimensional JAWR saved correctly |
| Relog, effects, inventory and advancement counts | Correct persistence; no death-loop or duplicate triggers |
| Same cases with flag **false** | Native behavior unchanged (diagnostic comparison) |

Before Stable: verify actual event priority and cancellation, compare with
Goety's config `lichNoSERemove` and `lichArcaRemove`, confirm no duplicate
charge under differing mod load orders. Cross-mod event ordering is not
established by bytecode signature inspection alone.
