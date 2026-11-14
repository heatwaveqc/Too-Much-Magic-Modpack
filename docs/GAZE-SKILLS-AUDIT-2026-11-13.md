# Gaze (Malum addon) × Pufferfish — focused audit

**Date:** 2026-11-13. **Classification: Compatible with work**, rather than Confirmed. This is a **partial inherited-stat bridge**, not a tested integration.

## Baseline

B6 pins **Gaze 1.1.7.1** and **Malum 1.8.2** on Minecraft 1.21.1 NeoForge. They passed combined dedicated-server startup, but no dedicated Gaze gameplay or Pufferfish tests. The Gaze addon is All Rights Reserved; no matching public 1.1.7.1 Java source was established or binary examined.

## Findings

1. The official **Gaze 1.1.7 changelog** says that *some Gaze projectiles now benefit from arcane resonance*. This is sufficient evidence for **partial parent-stat inheritance**. It does not establish that every weapon, projectile, rite, or Geas uses Malum's damage pipeline.
2. Malum's 1.21.1 source registers `malum:arcane_resonance` as a synchronized player attribute (default 1.0, configurable modifiers), enabling the shared Pufferfish Malum branch to target it. There is **no evidence yet** of a distinct Gaze-specific player proficiency attribute.
3. The Gaze changelog mentions **Meditation Ring** improving Staff Charge recovery. Malum's source registers `malum:charge_recovery_rate`, but whether Gaze applies the exact attribute or an independent effect is **not** established.
4. Gaze 1.1.7 reports **limited experimental Iron's Spells integration**. Treat this as an investigation lead, not proof it participates in Ars 'n Spells unified mana or universal spell power.
5. Gaze adds an encyclopedia/progression screen, Geas, rites, weapons, runes and Curios. Do not confuse content progression with a separate character skill tree.

## Proposed integration

- Use the **shared Malum/Pufferfish Arcane Resonance skill** for Gaze effects already consuming that stat; preserve other Gaze mechanics until exact paths are verified.
- No standalone Gaze-specific Pufferfish tree is justified yet. Reconsider only for a confirmed unique adjustable player stat.
- Do not stack universal magic damage or mana bonuses twice because Gaze bridges Malum or Iron's effects.

## Test matrix

- Record damage/effects and base `malum:arcane_resonance` with several Gaze weapons (Spirit Saber, Seidhr, Astral Splinter, other projectiles), then modify the player's attribute and compare. Classify each as inherited or not, without assuming effects scale linearly.
- Test any special damage bypasses, direct-health changes, Umbral effects, new Geas and runes separately. Observe whether status effects or spirit collection rather than raw damage scale.
- Check the Meditation Ring with changes to `malum:charge_recovery_rate` and other equipment; verify stacking and reversibility.
- Verify Gaze's trial Iron's bridge alongside the selected Ars 'n Spells mana-unification configuration; do not infer full common resource support.
- After the real Pufferfish tree exists, retest skill purchase, refund, login, respawn, dimensions and gear swapping. Inspect the **exact pinned Gaze jar/source** before claiming Confirmed integration.

## Evidence

- Gaze 1.1.7 changelog: https://www.curseforge.com/minecraft/mc-mods/gaze-a-malum-addon/files/7250833
- Gaze 1.1.7.1 release for Malum 1.8: https://www.curseforge.com/minecraft/mc-mods/gaze-a-malum-addon/files/7261638
- Gaze described features: https://www.curseforge.com/minecraft/mc-mods/gaze-a-malum-addon
- Malum 1.21.1 source attributes: https://github.com/SammySemicolon/Malum-Mod/blob/1.21.1/src/main/java/com/sammy/malum/registry/common/MalumAttributes.java

**No game test or direct Gaze bytecode examination performed.**
