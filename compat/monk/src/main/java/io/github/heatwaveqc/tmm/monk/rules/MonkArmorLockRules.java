package io.github.heatwaveqc.tmm.monk.rules;

/**
 * Pure policy for the optional, zero-cost, default-ON Monk Armor Lock.
 *
 * <p>No server event handler or item mutation is installed by this helper.
 * Actual armor equip interception, normal item durability break handling,
 * storage relocation, client toggle and synchronization require runtime code
 * and tests before this feature can be activated in the modpack.</p>
 */
public final class MonkArmorLockRules {
    public static final boolean DEFAULT_ENABLED = true;
    public static final int DEFAULT_BOUND_WEAR_INTERVAL_TICKS = 20;
    public static final int DEFAULT_BOUND_WEAR_AMOUNT = 1;

    public enum SlotKind {
        HEAD, CHEST, LEGS, FEET,
        CURIOS, MAIN_HAND, OFF_HAND, STORAGE
    }

    public enum EquippedItemAction {
        /** No worn armor in this slot. */
        NONE,
        /** Normally removable item can be moved atomically to free storage. */
        RELOCATE_LOSSLESSLY,
        /** Item is removable but no permitted capacity is currently available. */
        WAIT_FOR_STORAGE,
        /** Item cannot be unequipped, but may wear down via ordinary damage handling. */
        WEAR_BOUND_ITEM,
        /** Item cannot be unequipped or damaged; do not invent a destruction bypass. */
        KEEP_UNBREAKABLE_BOUND_ITEM
    }

    private MonkArmorLockRules() { }

    public static boolean isVanillaArmorSlot(SlotKind slot) {
        if (slot == null) throw new IllegalArgumentException("Slot must not be null");
        return switch (slot) {
            case HEAD, CHEST, LEGS, FEET -> true;
            default -> false;
        };
    }

    /** New equipment is blocked when locked; cargo/Curios are unaffected. */
    public static boolean mayEquip(boolean monkFaction, boolean lockEnabled, SlotKind slot) {
        return !(monkFaction && lockEnabled && isVanillaArmorSlot(slot));
    }

    /**
     * ON and OFF are always available to a Monk regardless of currently worn
     * armor, Curse of Binding, Life Mending or available storage space.
     */
    public static boolean mayToggle(boolean monkFaction) {
        return monkFaction;
    }

    /**
     * Pure classification of armor already worn at the instant of activation
     * or seen during subsequent reconciliation. Never forces a cursed item
     * out of its slot and never silently consumes inventory.
     */
    public static EquippedItemAction classifyWornItem(boolean itemPresent,
                                                      boolean canUnequipNormally,
                                                      boolean hasLosslessStorage,
                                                      boolean damageable) {
        if (!itemPresent) return EquippedItemAction.NONE;
        if (canUnequipNormally) {
            return hasLosslessStorage
                    ? EquippedItemAction.RELOCATE_LOSSLESSLY
                    : EquippedItemAction.WAIT_FOR_STORAGE;
        }
        return damageable
                ? EquippedItemAction.WEAR_BOUND_ITEM
                : EquippedItemAction.KEEP_UNBREAKABLE_BOUND_ITEM;
    }

    /**
     * Only bound, damageable vanilla-slot armor takes wear; no OFF, Curios or
     * general penalty for simply carrying a piece in inventory.
     */
    public static boolean shouldWearBoundItem(boolean monkFaction, boolean lockEnabled,
                                              SlotKind slot, EquippedItemAction action) {
        return monkFaction && lockEnabled && isVanillaArmorSlot(slot)
                && action == EquippedItemAction.WEAR_BOUND_ITEM;
    }

    /** Game-tick cadence on the logical server; never damage each client frame. */
    public static boolean isWearTick(long serverGameTime, int tickInterval) {
        if (tickInterval <= 0) throw new IllegalArgumentException("Wear interval must be positive");
        return serverGameTime >= 0 && serverGameTime % tickInterval == 0;
    }

    /** Existing actual-equipment discipline always overrides the optional lock. */
    public static boolean monkPowerEligible(boolean monkFaction,
                                            boolean lockEnabled,
                                            boolean conventionalWeaponEquipped,
                                            boolean conventionalArmorEquipped) {
        return monkFaction && DisciplineRules.equipmentPermitsDiscipline(
                conventionalWeaponEquipped, conventionalArmorEquipped);
    }
}
