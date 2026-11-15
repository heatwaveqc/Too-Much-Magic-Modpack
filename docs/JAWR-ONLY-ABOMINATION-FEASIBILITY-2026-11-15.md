# JAWR-only Abomination: feasibility and first test slice

Date: 2026-11-15. **Research/design only, not a playable implementation.** Target MC 1.21.1, JAWR 0.5.39 NeoForge. TeamLapen Vampirism and Werewolves are removed from the future target, but remain pinned in the current rolling test pack pending dependency migration.

## Decision history to reconcile

- The newest [JAWR-only retirement target](TEAM-LAPEN-RETIREMENT-TARGET-2026-11-15.md) and the earlier [JAWR hybrid binary audit PR #70](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/70) support a hybrid based on **JAWR vampire + JAWR lycanthrope**, with independent progression.
- [Experimental Abomination PR #73](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/73) treats **Vampire + Werewolf, no lichdom prerequisite**, as the identity model. Its TeamLapen custom-faction action adapters are obsolete, NOT to be ported.
- Older [transformation policy](TRANSFORMATION-TRANSITION-POLICY.md) and the uninstalled Java FormResolver still classify Abomination as Vampire + Werewolf + both lichdoms. **This is a conflicting legacy design and must not be shipped as the final JAWR-only rule.** Do not silently edit/redeploy the retired TeamLapen bridge just to make this test work.
- **Proposed design interpretation:** JAWR Vampire + JAWR Werewolf is the physical combination, and a deliberate joining rite (if we require one) grants the Abomination identity. Dual affliction without completion of the rite remains an explicitly observable, unapproved test state. Other lich/dragon/hunter combinations remain excluded until separately authorized.

## What the pinned JAWR 0.5.39 binary establishes

See [prior detailed binary research report](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/blob/bbc115466bfd596002876b94ddb59b1ed40bf86b/docs/JAWR-VAMPIRE-LYCANTHROPE-BINARY-AUDIT-2026-11-15.md), [second audit run](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43688703448), and the [fresh ABI check](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43765621180).

1. VampirePlayerAttachment.Data and WerewolfPlayerAttachment.Data hold independent levels. isVampire and isWerewolf each reflect their own level. The central WerewolfLeveling.infectPlayer method checks existing werewolf status but does not explicitly gate on vampire status; central setters do not reset each other's progression. This is *data-structure* support for coexistence, not an in-world gameplay proof.
2. TransformationPlayerAttachment supplies one physical TransformationType: NONE, BAT, WOLF, or WEREWOLF. This is a shared, single active slot, not an extra hybrid model. TransformationHandler.removeForm reapplies relevant affliction modifiers.
3. VampireEvents ticks handle sun, blood hunger, blood healing, garlic and vampire effects. WerewolfEvents ticks handle lunar status, forced forms, silver and werewolf attributes. **Both event loops may fire on a dual-afflicted player**; their ordering, damage, hunger and effects are not yet validated together.
4. WerewolfAfflictionEvent.ForceTransform and ForceRevert are cancellable events around involuntary transformation. A Moon Charm also affects the forced change path. Do not override the native moon/bat priority until observing it.
5. RemoveCurseRitualEvent delegates to AfflictionEvents.cureAfflictions, which loops positive-level JAWR afflictions and resets them. **One Remove Curse can remove BOTH vampire and werewolf.** The separately implemented Tyz cure-refund observer should then refund both categories, provided it is installed and tested. Lilith/garlic vampire cure may leave wolf status, also to be tested.
6. The prior class/constant scan found no explicit JAWR Abomination player class or independent Abomination skill progression; do not assume historical Witchery hybrid penalties or bonuses exist.

## Proposed identity and permissions

| Native JAWR state | Proposed label | Gameplay access |
| --- | --- | --- |
| Neither | Human / unrelated class | No form skills |
| Vampire only | Vampire | Native Vampire progress and gated Tyz Vampire category |
| Werewolf only | Werewolf | Native Werewolf progress and gated Tyz Werewolf category |
| Vampire + Werewolf, joining rite incomplete | Unbound dual-afflicted | Diagnostics only until safe coexistence and ritual is validated; **not automatically Abomination** |
| Both + explicit joined flag after a verified rite | Abomination | Both native trees at their separately earned levels; existing one global Tyz/MXP wallet; optional unique Abomination effects |
| Both + forbidden unrelated affliction, or unknown state | Conflict / unverified | No promotion, no auto-purge, no perks; operator review |

The explicit rite is a **proposal**. A safe first implementation would mark a verified dual-afflicted player as joined; do not write a recipe that grants arbitrary JAWR levels or assumes a hybrid cure can roll back. A JAWR ritual output can run commands, but exact owner attribution, idempotency and permission must be validated before declaring the rite complete.

## Mechanical ownership hypotheses to test

| Subsystem | First-choice owner | Highest risk |
| --- | --- | --- |
| Blood, feeding, hunger and healing | JAWR native vampire blood/food rules; Neo Vitae support stays native | Wolf-form food consumption or vampire hunger rewrite conflicts, especially at zero blood |
| Shape, collision, HUD and active model | JAWR single active transformation attachment | Forced wolf during Bat and revert without losing a native ability or level |
| Moon, lycan quests and silver checks | JAWR werewolf mechanics | Sun/moon events concurrently, silver bypasses vampire mitigation |
| Sunlight, fire, garlic | JAWR vampire mechanics | Wolf form accidentally bypasses or amplifies hazards; keep both vulnerability families |
| Max health, movement and attacks | JAWR modifier lifecycle, plus ordinary Tyz Core if present | Repeated form changes duplicating or forgetting modifiers |
| Progress and skills | Separate JAWR vampire and werewolf native levels; same global Tyz wallet for optional upgrades | Granting second tree's ranks for free or inventing a new XP currency |
| Cure/respawn | JAWR authoritative afflictions and the existing per-form Tyz refund observer | Remove Curse clearing both, stale shape, double SP refunds or failing to clear joining proof |
| Death/graves | JAWR survival ownership and Corail Tombstone for genuine death | Native vampire death prevention, totems/poppets, silver and world reload |

Do NOT create a second blood meter, extra full immortality, native dual-typed TeamLapen faction, new worldgen, minions/raids, another Werewolf renderer, or a duplicate XP/SP wallet.

## First executable artifact (NON-installed)

- [Read-only KubeJS observer](../compat/abomination/jawr_dual_affliction_observer.js) reads pinned JAWR flags, native levels and the current single active shape on login/once per second, logging changes only. Both true is reported as DUAL_AFFLICTED_RESEARCH_ONLY. It never modifies JAWR, grants skills, sets stages, or marks Abomination.
- [Seven mocked regression cases](../scripts/test_jawr_abomination_observer.js) cover both acquisition orders, all four active form values, one-sided cure, independent players, unknown class/data and no log spam.
- [CI](../.github/workflows/jawr-abomination-research.yml) checks exact hash-pinned JAWR 0.5.39 and javap ABI: all three attachment readers, both level/is-affliction getters, shared active-form getter and four enum members. [Run 43765621180 passed](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/actions/runs/43765621180). No native Minecraft gameplay has been run.

## Disposable-world playtest: both orders are mandatory

1. Isolate the new JAWR-only target (no TeamLapen) and its dependencies in a **disposable world** with a full backup. Only in this world, install the read-only observer under KubeJS server scripts. Do not enable old dual-TeamLapen bridge/faction code.
2. **Vampire -> Werewolf:** obtain actual JAWR vampirism; record level and blood; acquire lycanthropy naturally via the JAWR native route. Record both levels and abilities; relog, restart dedicated server.
3. **Werewolf -> Vampire:** repeat in opposite order with a fresh player/world. Confirm neither infection overwrites the other status or ability progression.
4. Try normal, Bat, Wolf and Werewolf forms as unlocked; test forced full moon and Moon Charm, especially switching while already Bat. Monitor physical model, collision, speed, fall damage, inventory, hotkeys and HUD.
5. Test daylight/rain, fire, garlic, silver, all stages of blood, Neo Vitae feeding, eating meat, starvation, healing, moon phases and attack attribution; compare solo-vampire, solo-werewolf and dual-afflicted controls.
6. Perform Remove Curse on the dual player and verify which JAWR levels disappear, form returns to NONE, and both Tyz categories refund exactly once if the separate cure script is staged. Try Lilith garlic for a one-sided vampire cure with werewolf retention; re-infect.
7. Test death/respawn, relog, dimensions, multiplayer and intermittent invalid attachment states; no silent overwrite, progression wipe or inventory loss.
8. Only then test a deliberately joined player with a new rite. Decline to implement a new active Abomination talent until its unique value is clear.

## Remaining gates

- **P0:** Two-order dual-state in-world proof; full-moon Bat/Wolf conflict and survival/weakness source-of-truth.
- **P1:** Explicit rite gate and revocable Abomination joined flag, with both JAWR current flags mandatory and no unconditional permanent powers after cure. Integrate per-form cure refunds and clear joined flag if either affiliation is lost.
- **P2:** Small fixes only for *observed* conflicts, using public JAWR events where possible; test the value of a distinct Abomination skill category sharing the global Tyz wallet.
- **P3:** Rewrite/reconcile *the replacement* transformation policy to the new two-affliction target, not old TeamLapen assumptions; rerun Dragon/Lich/Hunter/Monk compatibility tests when those separate workstreams are available.

**Research conclusion:** structurally promising; no claim of safe native dual gameplay or an already-existing special hybrid class.
