package io.github.heatwaveqc.tmm.monk.rules;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/** Future graveyard-restoration guard: free toggle is not a Monk armor bypass. */
final class MonkArmorLockRulesTest {
    @Test void defaultMonkArmorLockBlocksAllFourEquipmentSlots() {
        assertTrue(MonkArmorLockRules.DEFAULT_ENABLED);
        for (var slot : new MonkArmorLockRules.SlotKind[] {
                MonkArmorLockRules.SlotKind.HEAD, MonkArmorLockRules.SlotKind.CHEST,
                MonkArmorLockRules.SlotKind.LEGS, MonkArmorLockRules.SlotKind.FEET }) {
            assertTrue(MonkArmorLockRules.isVanillaArmorSlot(slot));
            assertFalse(MonkArmorLockRules.mayEquip(true, true, slot),
                    "Default Monk armor lock must reject auto-equip");
            assertTrue(MonkArmorLockRules.mayEquip(true, false, slot),
                    "Disabling the lock restores the ordinary equip route");
            assertTrue(MonkArmorLockRules.mayEquip(false, true, slot),
                    "Non-Monks never inherit a Monk-only inventory restriction");
        }
    }

    @Test void curiosAndNormalCargoNeverBecomeArmorByAccident() {
        for (var slot : new MonkArmorLockRules.SlotKind[] {
                MonkArmorLockRules.SlotKind.CURIOS,
                MonkArmorLockRules.SlotKind.MAIN_HAND,
                MonkArmorLockRules.SlotKind.OFF_HAND,
                MonkArmorLockRules.SlotKind.STORAGE }) {
            assertFalse(MonkArmorLockRules.isVanillaArmorSlot(slot));
            assertTrue(MonkArmorLockRules.mayEquip(true, true, slot),
                    "Legal spellbooks, Curios and stored armor must remain accessible");
        }
        assertThrows(IllegalArgumentException.class, () ->
                MonkArmorLockRules.isVanillaArmorSlot(null));
    }

    @Test void enablingLockCannotForceArmorOutOfCursedOrFullInventories() {
        assertTrue(MonkArmorLockRules.mayEnable(true, false, true, false),
                "No equipped armor means relocation capacity does not matter");
        assertTrue(MonkArmorLockRules.mayEnable(true, true, true, true),
                "Allow only when every piece can be removed and transferred");
        assertFalse(MonkArmorLockRules.mayEnable(true, true, true, false),
                "Full storage must refuse rather than delete or drop armor");
        assertFalse(MonkArmorLockRules.mayEnable(true, true, false, true),
                "No curse bypass or unequip restriction bypass");
        assertFalse(MonkArmorLockRules.mayEnable(false, false, true, true),
                "Non-Monks cannot enable the Monk trait");
    }

    @Test void turningLockOffDoesNotMakeWornArmorLegalForMonkPowers() {
        assertTrue(MonkArmorLockRules.monkPowerEligible(true, true, false, false));
        assertTrue(MonkArmorLockRules.monkPowerEligible(true, false, false, false),
                "A Monk without gear retains powers even with Armor Lock OFF");
        assertFalse(MonkArmorLockRules.monkPowerEligible(true, false, false, true),
                "Turning lock OFF is not permission to wear armor without penalty");
        assertFalse(MonkArmorLockRules.monkPowerEligible(true, true, false, true),
                "Unexpected grave gear still suspends skills until reconciled");
        assertFalse(MonkArmorLockRules.monkPowerEligible(true, true, true, false),
                "Conventional held weapons still suppress Monk powers");
        assertFalse(MonkArmorLockRules.monkPowerEligible(false, false, false, false));
    }

    @Test void armorAndFirstFistSlotAreIndependent() {
        assertEquals(0, MonkFistSlotRules.DEFAULT_FIST_INDEX);
        assertFalse(MonkFistSlotRules.mayInsert(true, 0, 0));
        assertFalse(MonkArmorLockRules.mayEquip(true, true,
                MonkArmorLockRules.SlotKind.FEET));
        assertTrue(MonkArmorLockRules.mayEquip(true, false,
                MonkArmorLockRules.SlotKind.FEET));
        assertFalse(MonkFistSlotRules.mayInsert(true, 0, 0),
                "Turning OFF armor lock cannot release the reserved fist position");
    }
}
