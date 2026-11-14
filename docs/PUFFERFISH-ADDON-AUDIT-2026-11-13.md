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
| Gaze (Malum addon) | Needs investigation | Published 1.21.1 project describes evolving new progression and gameplay; project is ARR and the audit found no matching public Java source. | Inspect pinned 1.1.7.1 JAR or authoritative API/docs for Malum attribute inheritance and unique player stats. |

## High-impact design issue: Ars 'n Spells

This addon does much more than contribute extra Ars glyphs. By default it merges mana pools in iss_primary mode (Iron's Spells authoritative). Other available modes include ars_primary and hybrid (other forms of unification), separate (distinct pools, cross-casts consume both), and disabled (no cross-mana behavior). Its NeoForge implementation also bridges Iron's spell-power attributes into Ars damage on the precise Ars SpellDamageEvent.Pre path; its own comment indicates the base Ars SPELL_DAMAGE_BONUS is already included in event.damage, so it deliberately avoids counting that particular bonus twice.

Do not modify the pack config automatically. Decide which degree of independence/synergy is wanted, and test the pinned 3.3.5 build with Pufferfish attributes before skill tree balancing. The repository branch head inspected may contain changes after the pinned released file.

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

Nine of ten addons can leave the blanket Needs Investigation bucket (four Confirmed, three Compatible with work, two Nothing separate to enhance). Gaze stays Needs Investigation. Psi: Tweaks and Additions is unchanged and will be audited with Psi. No mod has been called Proven Incompatible.
