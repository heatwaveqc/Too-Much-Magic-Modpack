package io.github.heatwaveqc.tmm.monk.rules;

/**
 * Pure rules for a future FREE, player-controlled No-Armor Lock.
 *
 * <p>This class does NOT intercept equipment mutations, inventory transfers,
 * graveyard recovery, or client input. A real feature needs pre-write
 * guards and no-loss recovery before it can be enabled safely.</p>
 */
public final class MonkArmorLockRules {
    public static final boolean DEFAULT_ENABLED = true;

    public enum SlotKind {
        HEAD, CHEST, LEGS, FEET,
        CURIOS, MAIN_HAND, OFF_HAND, STORAGE
    }

    private MonkArmorLockRules() { }

    public static boolean isVanillaArmorSlot(SlotKind slot) {
        if (slot == null) throw new IllegalArgumentException("Slot must not be null");
        return switch (slot) {
            case HEAD, CHEST, LEGS, FEET -> true;
            default -> false;
        };
    }

    /** When locked, even unusual wearables must not occupy vanilla armor slots. */
    public static boolean mayEquip(boolean monkFaction, boolean lockEnabled, SlotKind slot) {
        return !(monkFaction && lockEnabled && isVanillaArmorSlot(slot));
    }

    /**
     * Fail closed for an ON request until every worn item can be unequipped
     * and placed atomically without bypassing curses or losing any stack.
     */
    public static boolean mayEnable(boolean monkFaction, boolean wearingAnyArmor,
                                    boolean allItemsMayUnequip, boolean losslessRelocationAvailable) {
        if (!monkFaction) return false;
        if (!wearingAnyArmor) return true;
        return allItemsMayUnequip && losslessRelocationAvailable;
    }

    /**
     * The optional lock never grants permission to use Monk skills in armor.
     * Existing actual-equipment discipline policy remains authoritative.
     */
    public static boolean monkPowerEligible(boolean monkFaction,
                                            boolean lockEnabled,
                                            boolean conventionalWeaponEquipped,
                                            boolean conventionalArmorEquipped) {
        return monkFaction && DisciplineRules.equipmentPermitsDiscipline(
                conventionalWeaponEquipped, conventionalArmorEquipped);
    }
}
