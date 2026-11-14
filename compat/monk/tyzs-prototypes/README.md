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
