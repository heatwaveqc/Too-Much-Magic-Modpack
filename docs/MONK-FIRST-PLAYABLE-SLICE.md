# Monk playable-slice candidate — First Sunset and Step Assist

**Branch:** `feature/monk-first-sunset-step-assist`, based on the reconciled Monk research. **Status:** source code committed; not yet dedicated-server/client tested. Java/Gradle CI is the first build gate.

## Working behavior implemented in source

- 200 **consecutive server ticks** during **Overworld sunset** (`dayTime % 24000` between 11500 and 12500), looking within eight degrees of western horizon (`-X`).
- Sky visibility check and a 48-block horizontal westward block raycast; rain, roof, nearby blocking terrain, wrong time, wrong dimension, wrong gaze, equipment/faction ineligibility or gamemode break the streak. Session-progress map is cleared on logout, death/clone and dimension change. On-screen progress every two seconds; no reward for partial progress.
- Use pinned Vampirism `IFactionPlayerHandler.joinFaction(MonkFaction.FACTION)`; immediately recheck eligibility and confirm the authoritative faction assignment. Guard the **first** Monk faction transition with a server-only short-lived authorization token: unrelated commands and direct transitions should not bypass the sunset trial. This guard does not block legitimate level increases *after* becoming a Monk.
- Existing **zero-cost `tmm_monk:monk_root` is the one Step Assist**. It is activated through the existing Vampirism root `skill_tree` JSON, **not** the Tyz backend. Displays as "Step Assist" with a native description.
- On server ticks, if the player belongs to Monk faction, has the root skill and follows the initial no-conventional-weapons/no-armor policy, apply **one** `+0.5` `minecraft:generic.step_height` **transient modifier** under stable ID `tmm_monk:step_assist`. Vanilla player's base 0.6 becomes 1.1 blocks, enough for one-block terrain. Otherwise remove the modifier; removal covers faction changes, logout and gear swaps. Do not touch base attribute values, duplicate effects, Tyz ranks or Apotheosis Elastic.
- Data-driven `tmm_monk:conventional_weapons` item tag includes vanilla swords, axes, bow, crossbow, trident, mace and shield. Can extend tag in datapacks for other combat-mod weapons. Vanilla equipped armor slots count as disallowed; **Curios are not armor slots and are intentionally not blocked**.
- Added pure unit tests for sunset window (including day rollover/negative), gaze, continuity/reset, and Step Assist's faction+root+gear eligibility.

## Deliberately not implemented, and release blockers

1. **External transformation state:** Native Vampirism, Hunter and Werewolves faction exclusion works via `IFactionPlayerHandler` neutral check; we have **not** resolved Dragon, Lich, Archlich, Abomination or non-Vampirism physiological states from `Transformation Compat`. Neither the first-sunset eligibility nor Step Assist should be marked fully integrated until a verified API adds these checks. The present runtime does not hallucinate those capabilities or NBT ids.
2. **Full equipment taxonomy:** The minimal server tag covers standard vanilla weapons. Inspect actual modded weapon tags and legal magical staff/spellbook exceptions in the installed pack before accepting multiplayer survival enforcement.
3. **Sky geometry:** Time window and west-horizon gaze use a stable Overworld approximation; must verify in-game sunset position and obstruction with shaders/resource packs/dimension time manipulation. Rain blocks the rite by initial policy.
4. **Faction persistence/root semantics:** Depends on Vampirism 1.10.13 registering the custom tree and automatically enabling root skills on `checkSkillTreeLocks`. CI compilation, JSON registry load, first login/clone/relog, leaving/rejoining faction and dedicated-client attribute synchronization need actual game tests. Registering an event is not proof that the runtime trigger works.
5. **Reward and economy:** No separate Monastic Mastery XP or Tyz XP source is added. The primary design branch still owns MXP-only Tyz XP and shared ranks; this slice does not include block/mob/food progression, affix-event techniques or advanced Mage power.
6. **Full-build integration:** No modpack lock or known-good build changed. Package the compiled `tmm_monk` addon into the next combined test build after CI, retaining rollback snapshots.

## Suggested in-game smoke

1. Make a disposable survival Overworld with pinned Vampirism 1.10.13, Werewolves and the compiled Monk addon. Confirm UI shows "Step Assist" root and `/tmmmonk attributes` diagnostic still works.
2. Look west at an unobstructed sunset from the Overworld for 10s, with both hands and vanilla armor slots free. Verify Monk faction level 1, actionbar progress and completion once.
3. Repeat with a roof, westward wall, rain, noon, Nether/End, bow, sword, shield and armor; all must refuse or reset initiation.
4. Confirm Step Height effective value ~1.1, exactly one modifier ID, one-block stepping. Equip/remove conventional armor or weapon and verify effect turns off/on. Ordinary Tyz attributes and an equipped Curio remain unchanged.
5. Logout/relog, die/respawn, change dimension, restart; confirm native faction and root remain and the modifier is not duplicated. Try a second player or switching faction; no cross-player leakage.
6. Attempt direct pre-initiation `/vampirism level ... monk` (exact syntax verify) and ensure server faction transition guard rejects it. Test a legitimate advancement **after** initiation remains allowed.

## Primary architecture

- Primary global Tyz/MXP: [design/universal-mxp-progression-registry](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/tree/design/universal-mxp-progression-registry).
- Monk's additional catalog and API research: [PR #31](https://github.com/heatwaveqc/Too-Much-Magic-Modpack/pull/31).
- No Pufferfish-related code has been added; its removal remains scheduled for the next full build.
