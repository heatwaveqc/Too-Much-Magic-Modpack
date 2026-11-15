package io.github.heatwaveqc.tmm.monk.rules;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/** Future graveyard equipment guard; the worn-binding durability trap is deliberate. */
final class MonkArmorLockRulesTest {
    @Test void defaultMonkArmorLockBlocksAllFourNewEquipmentSlots() {
        assertTrue(MonkArmorLockRules.DEFAULT_ENABLED);
        for (var slot : new MonkArmorLockRules.SlotKind[] {
                MonkArmorLockRules.SlotKind.HEAD, MonkArmorLockRules.SlotKind.CHEST,
                MonkArmorLockRules.SlotKind.LEGS, MonkArmorLockRules.SlotKind.FEET }) {
            assertTrue(MonkArmorLockRules.isVanillaArmorSlot(slot));
            assertFalse(MonkArmorLockRules.mayEquip(true, true, slot));
            assertTrue(MonkArmorLockRules.mayEquip(true, false, slot));
            assertTrue(MonkArmorLockRules.mayEquip(false, true, slot));
        }
    }

    @Test void curiosAndCargoRemainOutsideArmorLock() {
        for (var slot : new MonkArmorLockRules.SlotKind[] {
                MonkArmorLockRules.SlotKind.CURIOS,
                MonkArmorLockRules.SlotKind.MAIN_HAND,
                MonkArmorLockRules.SlotKind.OFF_HAND,
                MonkArmorLockRules.SlotKind.STORAGE }) {
            assertFalse(MonkArmorLockRules.isVanillaArmorSlot(slot));
            assertTrue(MonkArmorLockRules.mayEquip(true, true, slot));
        }
        assertThrows(IllegalArgumentException.class, () ->
                MonkArmorLockRules.isVanillaArmorSlot(null));
    }

    @Test void monkMayToggleOnEvenWithBoundGearOrNoInventorySpace() {
        assertTrue(MonkArmorLockRules.mayToggle(true));
        assertFalse(MonkArmorLockRules.mayToggle(false));
        assertEquals(MonkArmorLockRules.EquippedItemAction.NONE,
                MonkArmorLockRules.classifyWornItem(false, false, false, false));
        assertEquals(MonkArmorLockRules.EquippedItemAction.RELOCATE_LOSSLESSLY,
                MonkArmorLockRules.classifyWornItem(true, true, true, true));
        assertEquals(MonkArmorLockRules.EquippedItemAction.WAIT_FOR_STORAGE,
                MonkArmorLockRules.classifyWornItem(true, true, false, true),
                "Normal armor in a full inventory stays worn, not destroyed");
        assertEquals(MonkArmorLockRules.EquippedItemAction.WEAR_BOUND_ITEM,
                MonkArmorLockRules.classifyWornItem(true, false, false, true),
                "Curse of Binding armor wears whether or not the inventory has space");
        assertEquals(MonkArmorLockRules.EquippedItemAction.WEAR_BOUND_ITEM,
                MonkArmorLockRules.classifyWornItem(true, false, true, true));
        assertEquals(MonkArmorLockRules.EquippedItemAction.KEEP_UNBREAKABLE_BOUND_ITEM,
                MonkArmorLockRules.classifyWornItem(true, false, true, false),
                "Non-damageable binding items cannot be magically broken");
    }

    @Test void onlyBoundDamageableWornArmorTakesPeriodicWear() {
        var wornBinding = MonkArmorLockRules.EquippedItemAction.WEAR_BOUND_ITEM;
        for (var armorSlot : new MonkArmorLockRules.SlotKind[]{
                MonkArmorLockRules.SlotKind.HEAD, MonkArmorLockRules.SlotKind.CHEST,
                MonkArmorLockRules.SlotKind.LEGS, MonkArmorLockRules.SlotKind.FEET}) {
            assertTrue(MonkArmorLockRules.shouldWearBoundItem(true, true, armorSlot, wornBinding));
            assertFalse(MonkArmorLockRules.shouldWearBoundItem(true, false, armorSlot, wornBinding),
                    "Turning OFF immediately stops added durability wear");
            assertFalse(MonkArmorLockRules.shouldWearBoundItem(false, true, armorSlot, wornBinding));
            assertFalse(MonkArmorLockRules.shouldWearBoundItem(true, true, armorSlot,
                    MonkArmorLockRules.EquippedItemAction.WAIT_FOR_STORAGE));
            assertFalse(MonkArmorLockRules.shouldWearBoundItem(true, true, armorSlot,
                    MonkArmorLockRules.EquippedItemAction.KEEP_UNBREAKABLE_BOUND_ITEM));
        }
        assertFalse(MonkArmorLockRules.shouldWearBoundItem(true, true,
                MonkArmorLockRules.SlotKind.CURIOS, wornBinding));
        assertFalse(MonkArmorLockRules.shouldWearBoundItem(true, true,
                MonkArmorLockRules.SlotKind.STORAGE, wornBinding));
    }

    @Test void defaultOneDurabilityWearAttemptEachSecondPerAffectedItem() {
        assertEquals(20, MonkArmorLockRules.DEFAULT_BOUND_WEAR_INTERVAL_TICKS);
        assertEquals(1, MonkArmorLockRules.DEFAULT_BOUND_WEAR_AMOUNT);
        for (int tick = 0; tick < 100; tick++) {
            assertEquals(tick % 20 == 0, MonkArmorLockRules.isWearTick(tick, 20));
        }
        assertFalse(MonkArmorLockRules.isWearTick(-1, 20));
        assertFalse(MonkArmorLockRules.isWearTick(19, 20));
        assertTrue(MonkArmorLockRules.isWearTick(20, 20));
        assertThrows(IllegalArgumentException.class, () ->
                MonkArmorLockRules.isWearTick(20, 0));
        assertThrows(IllegalArgumentException.class, () ->
                MonkArmorLockRules.isWearTick(20, -5));
    }

    @Test void wornArmorSuspendsPowersEvenWhileLockOnOrOff() {
        assertTrue(MonkArmorLockRules.monkPowerEligible(true, true, false, false));
        assertTrue(MonkArmorLockRules.monkPowerEligible(true, false, false, false));
        assertFalse(MonkArmorLockRules.monkPowerEligible(true, false, false, true));
        assertFalse(MonkArmorLockRules.monkPowerEligible(true, true, false, true),
                "Life Mending armor still disables Monk powers until broken");
        assertFalse(MonkArmorLockRules.monkPowerEligible(true, true, true, false));
        assertFalse(MonkArmorLockRules.monkPowerEligible(false, false, false, false));
    }

    @Test void firstFistSlotIsIndependentOfArmorLockAndItsLethalTrap() {
        assertEquals(0, MonkFistSlotRules.DEFAULT_FIST_INDEX);
        assertFalse(MonkFistSlotRules.mayInsert(true, 0, 0));
        assertFalse(MonkArmorLockRules.mayEquip(true, true,
                MonkArmorLockRules.SlotKind.FEET));
        assertTrue(MonkArmorLockRules.mayEquip(true, false,
                MonkArmorLockRules.SlotKind.FEET));
        assertFalse(MonkFistSlotRules.mayInsert(true, 0, 0));
    }
}
