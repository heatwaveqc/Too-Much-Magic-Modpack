# Pufferfish — addon quick-pass audit (2026-11-13)

Scope: Ten addons whose parent mods have already been audited. Psi: Tweaks and Additions is expressly deferred to the Psi audit. Every claim below is a **source/architecture judgment**, NOT a runtime Pufferfish test or assertion that the pinned B5 build is fully working.

## Classification

| Addon | State | Evidence | Remaining work |
|---|---|---|---|
| Ars Additions | Confirmed (parent inheritance) | Mark glyph extends Ars Nouveau AbstractEffect; ReachPerk modifies vanilla BLOCK_INTERACTION_RANGE and ENTITY_INTERACTION_RANGE. | Test glyphs and reach stacking; no special Pufferfish stat required. |
| Ars Controle | Confirmed (parent inheritance) | EffectPreciseDelay extends Ars AbstractEffect, uses SpellStats, SpellContext and Ars delay events. Other features are spell filters and control blocks. | Generic Ars caster bonuses should apply; test new glyph effects. |
| Ars Elemancy | Confirmed (parent inheritance) | ElemancyFocus extends Ars Elemental GreaterElementalFocus; applies amplification through SpellStats.Builder. ElemancyArmor uses Ars AnimatedMagicArmor, PerkAttributes and IManaDiscountEquipment. | Test additive/multiplicative stacking with environmental bonuses; no separate player attribute needed. |
| Ars Elemental | Confirmed (parent inheritance) | Discharge and other glyphs extend Ars AbstractEffect and use SpellStats/SpellResolver. | Core spell bonuses inherit; NOT yet verified whether its new summons count as OwnableEntity or all custom damage types receive generic Pufferfish bonuses. |
| Ars 'n Spells | Compatible with work | Implements actual Ars and Iron's spell power/mana bridges. NeoForge ArsDamageBridge listens to SpellDamageEvent.Pre and avoids explicitly counting Ars SPELL_DAMAGE_BONUS twice. | Review configuration and cross-mod Pufferfish power stacking; default mana mode iss_primary unifies pools, while separate and disabled preserve independent pools with different cross-cast rules. |
| Ars Ocultas | Nothing separate to enhance | IntegrationRegistrar adds Occultism spirits to Ars Containment Jars. It registers no new glyphs in the inspected branch. | Shared crafting/processing balance only; no separate personal progression stat. |
| Ars Unification | Nothing separate to enhance | Adds other mods' Crush/Cut processing recipes to existing Ars spell glyphs, including Occultism Crusher Spirits. | Audit recipe economy; no new player-level resource or skill stat. |
| Extra Botany Revived | Compatible with work | Has its own DamageHandler, custom damage types (including extrabotany:magic_piercing), and equipment modifiers; OwnedEntity is NOT Minecraft OwnableEntity. | Test selected custom magic-damage tags and conventional damage pipeline. Direct health-decrease techniques may bypass Pufferfish damage modifiers. |
| Goety Cataclysm | Compatible with work | Published 1.21.1 Cataclysm spell and servant expansion. The accessible Polarice3 source is for Minecraft 1.20.1 and shows Goety Spell/SpellStat/SpellType and Summoned inheritance. | Source mismatch: cannot claim verified 1.21.1 inheritance until inspecting the pinned Goety Cataclysm 1.21.1-1.8.4 release or its source. Then test spell schools/servant damage. |
| Gaze (Malum addon) | Compatible with work (partial Malum stat) | Author's 1.1.7 notes say *some* Gaze projectiles benefit from Malum's Arcane Resonance; Malum registers that synchronized player attribute in 1.21.1. Exact Gaze bytecode unexamined. | Test Pufferfish attribute changes on affected/unaffected projectiles, Geas, Staff Charge, and trial Iron's compat; see GAZE-SKILLS-AUDIT-2026-11-13.md. |

## High-impact design issue: Ars 'n Spells

This addon does much more than contribute extra Ars glyphs. By default it merges mana pools in iss_primary mode (Iron's Spells authoritative). Other available modes include ars_primary and hybrid (other forms of unification), separate (distinct pools, cross-casts consume both), and disabled (no cross-mana behavior). Its NeoForge implementation also bridges Iron's spell-power attributes into Ars damage on the precise Ars SpellDamageEvent.Pre path; its own comment indicates the base Ars SPELL_DAMAGE_BONUS is already included in event.damage, so it deliberately avoids counting that particular bonus twice.

**Revised decision (2026-11-13): one cross-school *skill tree*, but NO unified mana pools.** The earlier tentative `iss_primary` preference is withdrawn. For Ars 'n Spells, evaluate `separate` and `disabled` as candidates to keep Ars and Iron's resources distinct; `separate` may consume both for cross-casting. Verify damage/attribute stacking, double charges and source version differences before choosing. **No config changed** and no gameplay validation claimed.

## Version caveat for Goety Cataclysm

The published Minecraft 1.21.1 release uses file name goety_cataclysm-1.21.1-1.8.4.jar (CurseForge file 9044817; published under version label 1.21.1-1.9.0). In contrast, the public Polarice3/Goety_Cataclysm master branch currently declares minecraft_version=1.20.1 in gradle.properties. This is architectural evidence only, not exact current-version Java verification.

## Source references

- Ars Additions: https://github.com/Jarva/Ars-Additions/blob/1.21.0/src/main/java/com/github/jarva/arsadditions/common/glyph/EffectMark.java and https://github.com/Jarva/Ars-Additions/blob/1.21.0/src/main/java/com/github/jarva/arsadditions/common/perk/ReachPerk.java
- Ars Controle: https://github.com/Vonr/Ars-Controle/blob/1.21/src/main/java/dev/qther/ars_controle/spell/effect/EffectPreciseDelay.java
- Ars Elemancy: https://github.com/Lyrellion/Ars-Elemancy/blob/main/src/main/java/lyrellion/ars_elemancy/common/items/foci/ElemancyFocus.java and https://github.com/Lyrellion/Ars-Elemancy/blob/main/src/main/java/lyrellion/ars_elemancy/common/items/armor/ElemancyArmor.java
- Ars Elemental: https://github.com/Alexthw46/Ars-Elemental/blob/1.21/src/main/java/alexthw/ars_elemental/common/glyphs/EffectDischarge.java
- Ars 'n Spells: https://github.com/otectus/ars-n-spells/blob/port/neoforge-1.21.1/src/main/java/com/otectus/arsnspells/combat/ArsDamageBridge.java and https://github.com/otectus/ars-n-spells/blob/port/neoforge-1.21.1/src/main/java/com/otectus/arsnspells/config/ManaUnificationMode.java
- Ars Ocultas: https://github.com/dphaldes/Ars-Ocultas/blob/1.21.x/src/main/java/com/mystchonky/arsocultas/init/IntegrationRegistrar.java
- Ars Unification: https://github.com/Vonr/Ars-Unification/blob/master/README.md
- Extra Botany Revived: https://github.com/Velron-Devs/Extra-Botany-Revived/blob/Neo/1.21.1/src/main/java/velrondevs/extrabotany/common/handler/DamageHandler.java and https://github.com/Velron-Devs/Extra-Botany-Revived/blob/Neo/1.21.1/src/main/java/velrondevs/extrabotany/registry/ExtraBotanyDamageTypes.java and https://github.com/Velron-Devs/Extra-Botany-Revived/blob/Neo/1.21.1/src/main/java/velrondevs/extrabotany/common/entity/OwnedEntity.java
- Gaze: https://modrinth.com/mod/gaze-a-malum-addon
- Goety Cataclysm: https://github.com/Polarice3/Goety_Cataclysm/blob/master/gradle.properties and https://github.com/Polarice3/Goety_Cataclysm/blob/master/src/main/java/com/Polarice3/goety_cataclysm/common/magic/spells/abyss/AbyssalBeamSpell.java and https://www.curseforge.com/minecraft/mc-mods/goety-cataclysm/files/9044817

## Scope and status

All ten addons now have a more specific status than blanket Needs Investigation (four Confirmed, four Compatible with work, two Nothing separate to enhance). Gaze's promotion is based on a documented *partial* inherited attribute, not complete source/runtime verification. Psi: Tweaks and Additions has since been audited alongside Psi 110 and is classified Confirmed for direct player-attribute compatibility; see [Psi deep dive](PSI-SKILLS-DEEP-DIVE-2026-11-13.md). No mod has been called Proven Incompatible.

## Gaze source-limited follow-up (2026-11-13)
The Gaze 1.1.7 author changelog explicitly says some projectiles benefit from Arcane Resonance. Malum's 1.21.1 attribute registry declares `malum:arcane_resonance` and `malum:charge_recovery_rate`; Gaze's Meditation Ring mentions the latter behavior, but its actual hook remains unverified. The Gaze notes additionally describe trial Iron's Spells compatibility. Full Gaze 1.1.7.1 Java implementation is not inspected. [Focused evidence and test matrix](GAZE-SKILLS-AUDIT-2026-11-13.md).

## Psi: Tweaks and Additions follow-up completed
The original ten-addon quick-pass deliberately deferred Psi: Tweaks and Additions. The later parent Psi 110 / addon 0.10.10-hotfix audit found native `psitweaks:spell_damage_factor`, inherited `psi:total_psi` and `psi:regen`, and significant Psi's `PsiEvents` KubeJS support. This meets the matrix definition of **Confirmed source-backed integration path**, but does not demonstrate complete mechanic coverage or gameplay compatibility. [Detailed audit and balance blockers](PSI-SKILLS-DEEP-DIVE-2026-11-13.md).
