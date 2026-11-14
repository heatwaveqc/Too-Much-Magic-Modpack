# Monk / shared traits — Tyz's Skills tiered advancement

**Selected backend, 2026-11-14.** **Architecture and source-matched
configuration prototypes committed, no runtime test or live pack install.**
Pinned game: NeoForge Minecraft 1.21.1. Approved external mod:
**Tyz's Skills 6.4.2**, CurseForge project 990190 / file 8118598.

## Division of responsibilities

- **Monk native Vampirism-style faction tree** owns one-time powers
  (Step Assist, Blink, Catch Arrows, Glass Touch, Momentum Gliding
  after the Ender Dragon) and **permission/license** unlocks for any
  Monk-only leveled discipline.
- **Tyz's Skills** owns purchased **numeric levels**, spend/refund
  behavior, its own activity XP, **SP currency**, skill GUI and
  common numerical attributes. It supports 99 ranks *per skill*.
  Universal stat purchases remain available to non-Monks; the Monk
  license does not force a second purchase of the same stat.
- **Pufferfish's Skills** remains in the pack for its existing
  tree/layout, category-level XP and one-time shared choices; it
  **does not** also purchase the same numeric attribute modifiers.
- **Improvable Skills 3** may remain for its own vanilla-XP-powered
  abilities, **but Monk leveled skills do not revert to vanilla XP**.

The 24 unified source attribute groups remain research candidates:
some can collapse directly into one multi-mod Tyz CUSTOM skill, while
different mana/attunement mechanics require bridge code. A Tyz JSON
modifier can target existing player attributes from other mods;
non-attribute resources need verified per-mod APIs and registered
Java behavior, not fictitious registry attributes.

## Five-stage repeatable progression

For an open-ended numeric trait, create up to **five separately
registered Tyz skills**, each with **99 purchasable ranks**, with
a possible sixth stage only if the skill still has real headroom.

For stage `t=1..5` and level `n=0..99`:

- **Stage contribution:** `bonus(t,n) = n × baseEffect × 10^(t−1)`
  (cumulative for the stage; previous stage contributions remain).
- **Stage price:** `price(t,n) = baseSP × 10^(t−1) × f(n)`,
  `n=1..99`. The prototype's *unlocked-balance* choice is
  `f(n)=1+floor((n−1)/11)`, a stepwise 1..9 ramp that does not
  create a cheaper first purchase when advancing to the next stage.
  It may be replaced after progression balance tests.
- **Prerequisite:** purchase stage `t+1` only if stage `t`
  is at **exactly 99** on the server. A Tyz
  `config.skillPrerequisites` entry is *also* useful for the UI,
  but it only checks **ownership**, not 99/99 completion.
- **Refund:** refund stage `t` only when all stages above it
  have zero ranks. Refunding an earlier tier out from under
  higher ones would leave an invalid chain.
- **Attribute cap:** never offer a rank with no effective improvement.
  If the attribute has reached a native or effective limit, truncate
  or omit the higher stages entirely. Stage count is a generation
  parameter, **not a blanket promise that all 5 × 99 levels are useful**.

### Strength example — added benefits, not replacement

| Stage | Bonus per rank | Bonus at 99 | Cumulative bonus after completing this stage |
| --- | ---: | ---: | ---: |
| Strength I | +1 | +99 | +99 |
| Strength II | +10 | +990 | +1,089 |
| Strength III | +100 | +9,900 | +10,989 |
| Strength IV | +1,000 | +99,000 | +109,989 |
| Strength V | +10,000 | +990,000 | +1,099,989 |

This is an **arithmetic illustration, not a balanced combat stat**.
Vanilla Minecraft `generic.attack_damage` has a maximum of **2,048**.
For a player starting at 2, stages I and II together add 1,089,
leaving 957 points of effective headroom. Strength III would then
reach that cap within its first ten purchases, making the remaining
Strength III, IV and V purchases ineffective if implemented as direct
attribute modifiers. **Those must not be sold.** To support truly
million-point "Strength" as a different gameplay mechanic would
require an explicit **server-side damage-event adapter** with its
own meaningful cap, attribution, scaling and overflow tests;
the prototype is not such an adapter. For a
capacity stat with a high native limit, all five could be justified.
Use the measured game cap and precision rules per attribute.

Sample default stage-SP cost calculation with `baseSP=1`:
99 ranks in each stage cost 495 × stageFactor SP, totaling
**5,499,945 SP** for five fully purchased stages. This remains
within signed 32-bit *per-purchase* and aggregate cost ranges,
but earned SP, player level/XP, inventory and persistence must
be stress-tested before considering it playable.

## Actual Tyz 6.4.2 integration constraints

1. Custom skills belong under
   `config/tyzs_skills/custom/skills/*.json`, NOT datapack
   `data/*`. The default config folder is overwritten at startup;
   the **custom** directory is persistent. `/skills reload`
   reloads definitions on a test server.
2. Each custom definition uses `"type":"CUSTOM"` and the
   built-in `maximumLevel` of at most 99. Provide exactly
   **99 `prices` values** and **99 cumulative values**
   for each `modifiers` entry in a 99-rank stage. For percentage
   modifiers Tyz's API expects percent units, not ratios.
3. `SkillActionEvent.PurchasePre` fires and is cancellable for
   ordinary and **bulk** purchases. Enforce both Monk-native
   permission and `priorStageLevel == 99` for gated stages;
   ordinary `skillPrerequisites` alone does not enforce it.
4. `SkillActionEvent.RefundPre` also fires for ordinary and
   bulk refunds: cancel when any subsequent tier still has ranks,
   or when a refund would violate defined Monk respec policy.
5. `SkillManager.setSkillLevel` is public and **does not**
   dispatch PurchasePre; admin scripts or other mods could
   bypass tier gates. Treat administrative changes as authorized
   operations, and **reconcile character state** before applying
   Monk-only effects. Use read-only audit/repair tooling if needed.
6. Tyz's default `MAX_LEVEL`, `MAX_SP`, and `MAX_SP_GAIN`
   settings are configurable with `-1` disabling artificial
   caps; its stored SP and prices use signed **`int`**.
   No overflows, negative XP/SP, or underflowed costs permitted.
7. Tyz `CUSTOM` generic effects apply a unique attribute
   modifier per skill ID and player attribute, allowing
   **additive stacking** across distinct tier skill IDs.
   Confirm actual handling for each mod-owned attribute,
   client sync, 0-rank removal and login/respawn.
8. On Monk faction removal/respec: ordinary universal Tyz ranks
   remain, but **Monk-specific effects must stop** even if
   the Tyz level data is retained. This is **not** solved by a
   purchase veto alone; write a modifier reconciliation bridge or
   supported suspension lifecycle.
9. The vanilla-XP pool is never charged for Monk tier purchases.
   **Monastic Mastery XP for faction ranks remains separate from
   Tyz XP/SP** until an explicit economy rule is approved; beware
   duplicated rewards from the same break/kill/food event.
10. There must be exactly **one canonical modifier owner**
    per benefit. Tyz's built-in skills, Improvable Skills,
    Pufferfish shared bonuses and Monk innate adapters must
    not each stack a copy of the *same purchased upgrade*.
    This requires a loaded-mod and generated-default-skill audit.

## Prototypes and test gates

Five source-conforming `CUSTOM` JSON files live at
`compat/monk/tyzs-prototypes/config/tyzs_skills/custom/skills/`:
`tmm_strength_1.json` through `tmm_strength_5.json`.

**All have `"active":false` and are NOT installed.**
Their descriptive +damage values demonstrate the owner's math but
exceed plausible native damage limits. They are a **format and
progression generation test**, not recommended player balance.

- Manifest: `tyzs-skills` desired/approved but
  `enabled_in_pack=false` pending packwiz artifact pin/boot.
- Start a disposable client + server with Tyz 6.4.2,
  Vampirism Monk and current Pufferfish. Confirm actual defaults.
- Enable **only a safe bounded first-tier test** after inspecting
  the live player attribute maximum (choose a stat with headroom).
- Verify 99 values/prices parse; tier II cannot be bought with
  stage I at 1/99 but can at 99/99, including bulk purchasing.
- Test partial/full refunds of stages with higher-tier ranks;
  re-buy at legal state. Test admin direct level-setting and
  invalid stage repair.
- Test a universal Strength-like stat on non-Monks and unarmed
  Monks; no duplicated Pufferfish bonus or Tyz default skill.
- Test actual Tyz SP gain without spending vanilla XP, SP
  configurable gain caps and staged costs at large rank count.
- Test extended save/load, death, dimensions, client reconnect,
  transformation state changes and Monk faction permission
  loss/re-entry.
- Confirm defensive/utility bounded traits receive only
  finite useful levels; harvest tier is **finite and separate**
  from repeatable harvest speed.
- Confirm no Tyz Skill/Apotheosis Unbound grants Creative
  Flight; Draconic Evolution Curio remains the source.

After those checks, generate the per-skill, per-tier config set;
do **not** blindly create five versions of all 172 candidate rows.

## Sources

- [Official Tyz's 6.4+ custom skill guide](https://github.com/Tyzbrand/TyzsSkills/blob/mc1.21.1-(main)/docs/json-tweaks/create_skill.md)
- [Official skill config structure](https://github.com/Tyzbrand/TyzsSkills/blob/mc1.21.1-(main)/docs/json-tweaks/skill-structure-on-tyzs-skills-6.4+.md)
- [Official custom config path / overrides](https://github.com/Tyzbrand/TyzsSkills/blob/mc1.21.1-(main)/docs/json-tweaks/how-works-tyzs-skills-6.4+.md)
- [Purchase/refund hooks and levels](https://github.com/Tyzbrand/TyzsSkills/blob/mc1.21.1-(main)/src/main/java/com/tyzsskills/impl/server/skills/SkillManager.java)
- [Generic attribute modifier stacking](https://github.com/Tyzbrand/TyzsSkills/blob/mc1.21.1-(main)/src/main/java/com/tyzsskills/impl/server/effects/GenericEffects.java)
- [Tyz's Skills 6.4.2 release](https://www.curseforge.com/minecraft/mc-mods/tyzs-skills/files/8118598)
- [Machine-readable rank policy](../compat/monk/profiles/tyzs-tiered-skills-v1.json)
