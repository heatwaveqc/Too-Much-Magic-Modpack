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
the referenced upstream mods coexist. The bridge currently has source/binary-verified read-only probes for Vampirism vampire/hunter and JAWR vampire. All other providers are UNKNOWN.

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
