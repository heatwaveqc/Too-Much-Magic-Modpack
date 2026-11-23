package io.github.heatwaveqc.tyr.progression.runtime.cuisine;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

/**
 * Explicit fixed-dose origin on the item itself. Not a potion/status effect:
 * Brewin' and Chewin' can age/re-age native WineContents independently.
 *
 * The payload is created only by the two strict native recipes or the validated
 * cask conversion and credited at MOST once when that output is consumed.
 */
public final class BloodCuisinePayload {
    private static final String VERSION = "tmm_cuisine_version";
    private static final String CREDIT = "tmm_blood_credit";
    private BloodCuisinePayload() {}

    public static int credit(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return 0;
        CustomData value = stack.get(DataComponents.CUSTOM_DATA);
        if (value == null) return 0;
        CompoundTag tag = value.copyTag();
        if (!tag.contains(VERSION, Tag.TAG_INT) || tag.getInt(VERSION) != 1
                || !tag.contains(CREDIT, Tag.TAG_INT)) return 0;
        int credit = tag.getInt(CREDIT);
        return credit > 0 && credit <= 1000000 ? credit : 0;
    }

    public static void set(ItemStack stack, int credit) {
        if (stack == null || stack.isEmpty() || credit <= 0 || credit > 1000000)
            throw new IllegalArgumentException("Invalid blood charge");
        CustomData original = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        CompoundTag tag = original.copyTag();
        tag.putInt(VERSION, 1);
        tag.putInt(CREDIT, credit);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }
}
