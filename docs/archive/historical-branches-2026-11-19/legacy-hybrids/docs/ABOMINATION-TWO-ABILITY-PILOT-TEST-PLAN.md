# Abomination two-action prototype: disposable-world test plan

**Version:** Minecraft 1.21.1 / NeoForge 21.1.265 / JAWR 0.5.39 / Vampirism 1.10.13 / Werewolves 2.0.3.4.
**Status:** experimental native ability **delegate**, **not** the combined faction or combined skill screen. This is an intentionally narrow integration proof.

## Before testing

1. Use a **new disposable world** with a current test ZIP containing the `tmm_transformations` JAR. Back up if you need to preserve any data. The experimental bridge is not part of the production packwiz lock.
2. From world server config (`saves/<world>/serverconfig/tmm_transformations-server.toml`, or server world `serverconfig/`), set `experimentalAbominationTwoAbilityPilot = true`. All defaults remain `false`. Reopen the world after modifying server config.
3. Join as a player with operator permission (2+). The test commands execute **as the same player**; there is no target-selector or automatic grant mode.
4. Acquire **JAWR vampire AND JAWR lycanthrope**, in either order, using real JAWR mechanics. The action pilot **does not infect, level up, or unlock** either status. To reach lycanthropy level one, the JAWR developer describes a Werewolf Altar, a bear trap in front of it, and being hit by the trapped werewolf. For vampirism use the Lilith chain.
5. Keep Hunter off. Other transformation types are not integrated yet; do not experiment with them in this pilot.

## Commands

| Command | Expected purpose |
| --- | --- |
| `/tmmtransform status` | Full read-only probe list. JAWR vampire + lycanthrope should both read PRESENT; other unimplemented providers can be UNKNOWN. |
| `/tmmtransform abomination pilot` | Reports flag, native JAWR vampire/wolf observations and Hunter; explains why a test is refused. |
| `/tmmtransform abomination pilot vampire` | Invoke Vampirism's **real HissingAction** with the native VampirePlayer attachment: observe native hiss sound and hostile mob targeting disruption. |
| `/tmmtransform abomination pilot werewolf` | Invoke Werewolves' **real HowlingAction** with the native WerewolfPlayer attachment: observe native howl sound and Howling effect on eligible creatures. |

These commands are **OP-only** and **off by default**. The delegates perform native `canUse` checks and track a separate conservative command cooldown of 1 to 60 seconds. They **deliberately bypass ordinary native skill unlock entitlement and native action event/packet dispatch**: those mechanisms cannot be proven until the proper custom faction and skill-tree owner exist. No skill point is consumed, granted or auto-duplicated. This is not the final gameplay acquisition path.

## Verification matrix

- With the config **false**, all two native-action tests are refused.
- With only JAWR vampire OR only JAWR wolf, **both** commands refuse, without changing state.
- If JAWR API probes fail, tests refuse rather than silently assuming absence.
- Hunter can never participate.
- With both JAWR afflictions, run the vampire Hiss and Werewolves Howl delegates **on the same player**. Record whether the native effects actually trigger. Re-run immediately to confirm cooldown rejection. Wait for cooldown and test again.
- After each command, `/tmmtransform status`, relog, verify both JAWR levels remain unchanged, and check original Vampirism/Werewolves skills are not granted.
- Test full moon, bat/wolf form, sunlight, and food interactions **as observations only**; this pilot intentionally does not arbitrate any physiology or models.
- Test dedicated-server console and two players: unauthorized non-op attempts must not execute, and one player's hybrid must not give another access.
- Observe server `latest.log` for `Abomination pilot` exceptions; if a native API mismatch occurs the command should fail with a diagnostic, **not** modify levels.

## Passing criteria / promotion gate

**Pass** means both native action *effects* work on one dual-JAWR player through their original typed attachments and do not mutate or corrupt upstream progression. It does **not** demonstrate the proposed `tmm_transformations:abomination` faction, combined tree UI, all four native skill histories, native action cooldown/event/permission parity, normal non-op ability use, diet, cure, form, death or multiplayer survival.

Next implementation stage: a real custom IPlayableFaction on embedded Vampirism 1.10, a persisted AbominationPlayer adapter with separate original player handlers, properly registered skill-tree nodes, and two earned cross-faction action wrappers. Review `docs/ABOMINATION-FACTION-MODEL-2026-11-15.md` first.
