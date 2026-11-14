# Transformation bridge — implementation gates

Status: **vampire pilot in progress, still diagnostic-only** on `feature/transformation-bridge-scaffold`.
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
the referenced upstream mods coexist. The bridge has source/binary-verified read-only probes for Vampirism vampire/hunter and JAWR vampire, plus experimental source-specific hooks for this pilot. All other providers remain UNKNOWN.

## Design partition

- Body, undeath/curse, faction/profession, temporary form are distinct axes.
- The underlying mods own their unlocks/progression, with the bridge
  coordinating only mechanics that actually conflict.
- Shared resource meters, feeding, model/body, resurrection and cure need
  an explicit owner for each permitted compound state.
- Monk (under consideration) is a discipline/faction, not an additional
  permanent transformation provider; faction coexistence is TBD.

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

## First pilot candidate

The passive **Vampirism 1.10.13** vampire/hunter and **JAWR 0.5.39** vampire observers are now installed in source, with explicit UNKNOWN fallback. Both JARs were hash-verified in [research CI](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43612789188); exact binary player attachment signatures were extracted. The current build adds `/tmmtransform vampire` to read both native progression and blood values. **Actual untransformed/vampire/client-world tests are still pending.** Do not enable united physiology merely because the observers compile. See [vampire deep audit](VAMPIRE-FUSION-AUDIT-2026-11-14.md).

## Approved new integration requirement — Mahou Drain Life (2026-11-14)

Mahou Tsukai's **Boundary of Drain Life** rewards a successful drain in
order of caster missing health, hunger restoration when health is full,
then Mahou regeneration. Owner requests that the hunger step fill a dual
vampire's **canonical Vampirism blood** instead of vanilla FoodData, while
JAWR's blood UI derives from that same pool. Respect Mahou's
`DRAIN_LIFE_IGNORE_HUNGER` option; when hunger rewards are disabled,
do not introduce bonus vampire blood. Preserve health-first priority,
mana fallback at full canonical blood, original boundary mana expenditure,
target-acceptance/count limits and non-vampire behavior. No second pass
on FoodData, free healing, or duplicate rewards.

Status **DESIGN ACCEPTED / NOT IMPLEMENTED**. Audit exact Mahou Tsukai
1.36.31 JAR for the reward branch; then implement a guarded server-side
compat adapter with arithmetic and regression tests. See vampire live
test plan for full/partial/empty/normal-player validation.

## Bootstrap files

`compat/transformations/` contains Gradle project, core snapshot classifier,
extension interfaces, review-data attachment, read-only diagnostics and unit
tests. Workflow: `.github/workflows/transformation-bridge.yml`.

## Current source milestones (2026-11-14)

- Pure full-form resolver and fail-closed transition-policy tests: implemented, compiled.
- Read-only Vampirism vampire/hunter and JAWR vampire observers: implemented, compiled, **not gameplay verified**.
- Vampire target subsystem ownership and safe blood conversion arithmetic: implemented, unit-tested; **no runtime meter synchronization**.
- JAWR 0.5.39 source behavior inspected from binary, including independent blood/hunger and cancellable healing/sun events.
- Full-modpack dedicated-server workflow: available under `.github/workflows/transformation-vampire-server-smoke.yml`; use only actual workflow results to report PASS.
- Remaining: source-specific pre-acquisition hooks, JAWR hunger suppression, native blood-transaction translation, cure and death arbitration, and real two-provider playtests.

## Scope update: vampire chassis v1

The earlier implementation checkpoints described **diagnostic-only code**. Subsequent commits introduced guarded server-side vampire hooks. Automatic native dual acquisition, one-sided cure prevention, a backup-required operator cure, single blood pool transaction routing with persistent fractional carry, and deduplicated sunlight/healing/garlic/fire paths are implemented **as experimental code**. Every mutating flag defaults to false. This is not a claim that live gameplay paths have passed.

Read [VAMPIRE-CHASSIS-V1-TEST-PLAN.md](VAMPIRE-CHASSIS-V1-TEST-PLAN.md). Remaining release blockers are in-world acquisition/cure in both orders, native rollback, client/player tests, unique JAWR sun-resistant gear, bat and HUD visuals, and death handling. No packwiz installation authorized.
