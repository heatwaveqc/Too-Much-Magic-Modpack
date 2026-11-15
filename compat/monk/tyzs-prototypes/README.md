> **PRIMARY SKILLS ARCHITECTURE — 2026-11-14:** This directory now also contains the initial `tmm_monk` Tyz category and two `active:false` 10-rank Monk-specific CUSTOM skills. They are correct data schemas but are **intentionally NOT PACK INSTALLED**. The primary `design/universal-mxp-progression-registry` tree must install Tyz 6.4.2, disable native activity XP, enforce purchase/faction gates and merge categories before activating them. ONE rank of prior tier is sufficient; the historical 99/99 prerequisite discussion below is superseded. See [two-tree integration contract](../../docs/MONK-EARLY-MASTERY-TWO-TREES.md).

# Tyz's Skills tier prototype — NOT PACK INSTALLED

Owner proposal from 2026-11-14: five additive stages of 99 ranks
for repeatable traits; each stage improves by 10x per rank and costs
10x the previous one. Stage 6 is optional only if genuinely useful.

**This is a schema/serialization prototype, not a playable skill tree.**
All five JSONs intentionally set `"active": false`. They live
under `compat/monk/tyzs-prototypes/` rather than the live
`config/tyzs_skills/custom/skills/` folder. Copying or activating
them without a server-authoritative stage/license guard would allow
unlocking Strength II before Strength I reaches 99, since upstream
`skillPrerequisites` checks *ownership* and not exact level.
The baseline vanilla attack-damage attribute also has an effective
native cap; the enormous illustrative values of the later stages are
not automatically useful or valid in Minecraft.

Skill data format matches Tyz's Skills 6.4.2 docs: `CUSTOM`,
`maximumLevel:99`, 99 integer `prices`, 99 cumulative `values`
per modifier, `config.skillPrerequisites`, `ADD_VALUE`.
Tyz's APIs have a cancellable server-side purchase/refund event for
the bridge, but nothing in this folder is a compiled bridge.

The example tier cost pattern is:
`cost(tier, rank) = 10^(tier-1) * (1+floor((rank-1)/11))`.
Ranks 1–11 of each tier cost factor×1 SP each, ranks 12–22
factor×2, ..., ranks 89–99 factor×9. It is merely an initial
increasing-cost demonstration and is **not locked balance**.

See `../../docs/MONK-TYZS-TIERED-ADVANCEMENT.md` for the gate,
unification, XP and refund requirements.

## Why this example is deliberately disabled

Vanilla `minecraft:generic.attack_damage` is bounded at **2048**.
After full first and second stages, a base-damage-2 player has only
957 damage headroom; the third stage (+100/rank) hits the cap
within 10 purchases. Later stages cannot usefully add damage
with plain attribute modifiers. To implement such extreme Strength
would need a custom damage event instead of raw vanilla attribute
modifiers, with independent balance and overflow tests.
