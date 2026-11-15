package io.github.heatwaveqc.tmm.monk.rules;

import java.util.Optional;

/**
 * Pure inventory policy for the PROPOSED Monk reserved fist hotbar slot.
 *
 * <p>This class is deliberately NOT wired into item pickup or container
 * menus yet. It does not change gameplay without a complete server-side
 * insertion/swap guard, client sync and no-loss item relocation. Do not
 * assume a tick-time cleanup alone implements a hard inventory lock.</p>
 */
public final class MonkFistSlotRules {
    public static final int HOTBAR_SIZE = 9;
    public static final int PLAYER_STORAGE_SIZE = 36;
    public static final int DEFAULT_FIST_INDEX = 8; // Ninth hotbar slot, zero-based.

    private MonkFistSlotRules() { }

    public static boolean isHotbarIndex(int index) {
        return index >= 0 && index < HOTBAR_SIZE;
    }

    public static boolean isReserved(boolean inMonkFaction, int reservedHotbarIndex, int inventoryIndex) {
        if (!isHotbarIndex(reservedHotbarIndex)) {
            throw new IllegalArgumentException("Reserved fist index must be 0..8");
        }
        return inMonkFaction && reservedHotbarIndex == inventoryIndex;
    }

    /** Rejection policy shared by pickup, GUI insertion, and automation adapters. */
    public static boolean mayInsert(boolean inMonkFaction, int reservedHotbarIndex, int inventoryIndex) {
        return !isReserved(inMonkFaction, reservedHotbarIndex, inventoryIndex);
    }

    /**
     * Plans a full transfer of a pre-existing stack to the OTHER 35 slots.
     * Actual capacity for each position (after item/component stack matching
     * and slot restrictions) must be calculated by the authoritative inventory
     * layer. This method does not mutate items and NEVER returns a partial move.
     *
     * @return 36-slot allocation vector if a complete relocation is possible,
     *         or Optional.empty() when the player must clear inventory space.
     */
    public static Optional<int[]> planLosslessRelocation(int incomingCount,
                                                           int reservedHotbarIndex,
                                                           int[] permittedCapacity) {
        if (!isHotbarIndex(reservedHotbarIndex)) {
            throw new IllegalArgumentException("Reserved fist index must be 0..8");
        }
        if (incomingCount <= 0) {
            throw new IllegalArgumentException("Relocation requires a positive stack size");
        }
        if (permittedCapacity == null || permittedCapacity.length != PLAYER_STORAGE_SIZE) {
            throw new IllegalArgumentException("Expected 36 player inventory capacity entries");
        }
        int[] allocation = new int[PLAYER_STORAGE_SIZE];
        long available = 0;
        for (int i = 0; i < permittedCapacity.length; i++) {
            if (permittedCapacity[i] < 0) {
                throw new IllegalArgumentException("Slot capacities cannot be negative");
            }
            if (i != reservedHotbarIndex) available += permittedCapacity[i];
        }
        if (available < incomingCount) return Optional.empty();

        int remaining = incomingCount;
        for (int i = 0; i < permittedCapacity.length && remaining > 0; i++) {
            if (i == reservedHotbarIndex) continue;
            int move = Math.min(remaining, permittedCapacity[i]);
            allocation[i] = move;
            remaining -= move;
        }
        return remaining == 0 ? Optional.of(allocation) : Optional.empty();
    }
}
