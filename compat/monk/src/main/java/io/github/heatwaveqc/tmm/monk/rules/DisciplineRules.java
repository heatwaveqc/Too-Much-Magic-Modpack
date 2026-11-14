package io.github.heatwaveqc.tmm.monk.rules;

import java.util.EnumSet;
import java.util.Set;

/**
 * Pure target-policy model for later runtime gate implementation.
 * Calling this method does not change the player's faction or equipment.
 */
public final class DisciplineRules {
    public enum Physiology {
        HUMAN, VAMPIRE, WEREWOLF, LICH, ARCHLICH, DRAGON, DRACOLICH, ABOMINATION, UNKNOWN
    }

    private static final Set<Physiology> FORBIDDEN = EnumSet.of(
            Physiology.VAMPIRE, Physiology.WEREWOLF, Physiology.LICH,
            Physiology.ARCHLICH, Physiology.DRAGON,
            Physiology.DRACOLICH, Physiology.ABOMINATION);

    private DisciplineRules() { }

    public static boolean mayInitiate(Physiology physiology, boolean knownNonMonkFaction) {
        return physiology == Physiology.HUMAN && !knownNonMonkFaction;
    }

    public static boolean equipmentPermitsDiscipline(boolean conventionalWeaponEquipped,
                                                      boolean conventionalArmorEquipped) {
        return !conventionalWeaponEquipped && !conventionalArmorEquipped;
    }

    public static boolean permitsStepAssist(boolean monkFaction, boolean rootSkill, boolean validEquipment) {
        return monkFaction && rootSkill && validEquipment;
    }

    public static boolean conflicting(Physiology physiology) {
        return physiology == Physiology.UNKNOWN || FORBIDDEN.contains(physiology);
    }
}
