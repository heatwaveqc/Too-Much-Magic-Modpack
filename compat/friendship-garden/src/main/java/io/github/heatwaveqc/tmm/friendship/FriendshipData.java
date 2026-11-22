package io.github.heatwaveqc.tmm.friendship;

import java.util.UUID;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.entity.player.Player;

/** Namespaced, immutable crafter binding on the crafted ITEM and placed block entity. */
public final class FriendshipData {
    private static final String KEY = "tmm_friendship_garden";
    private FriendshipData() {}

    public static void setOrigin(ItemStack stack, ResourceLocation origin, boolean tall) {
        CompoundTag tag = read(stack);
        CompoundTag data = tag.getCompound(KEY);
        data.putString("origin", origin.toString());
        data.putBoolean("tall", tall);
        tag.put(KEY, data);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    /** The only normal player-binding entrypoint. Does not trust the block placer. */
    public static void craftedBy(ItemStack stack, Player player) {
        CompoundTag tag = read(stack);
        CompoundTag data = tag.getCompound(KEY);
        if (!data.contains("origin", 8) || data.hasUUID("owner")) return;
        data.putUUID("owner", player.getUUID());
        data.putString("name", player.getGameProfile().getName());
        tag.put(KEY, data);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    public static FriendshipFlower bound(ItemStack stack) {
        return decode(read(stack).getCompound(KEY));
    }

    public static FriendshipFlower decode(CompoundTag tag) {
        if (!tag.hasUUID("owner") || !tag.contains("name", 8) || !tag.contains("origin", 8)) return null;
        ResourceLocation id = ResourceLocation.tryParse(tag.getString("origin"));
        if (id == null) return null;
        try {
            return new FriendshipFlower(tag.getUUID("owner"), tag.getString("name"), id.toString(),
                    tag.getBoolean("tall") ? FriendshipFlower.Form.TALL : FriendshipFlower.Form.SHORT,
                    Math.max(0L, tag.getLong("mourning_until")), tag.getBoolean("permanent_wilt"));
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    public static void encode(CompoundTag to, FriendshipFlower flower) {
        to.putUUID("owner", flower.crafterId());
        to.putString("name", flower.crafterDisplayName());
        to.putString("origin", flower.originalFlowerId());
        to.putBoolean("tall", flower.form() == FriendshipFlower.Form.TALL);
        to.putLong("mourning_until", flower.mourningUntilTick());
        to.putBoolean("permanent_wilt", flower.permanentlyWilted());
    }

    public static void applyBound(ItemStack stack, FriendshipFlower flower) {
        CompoundTag tag = read(stack);
        CompoundTag data = new CompoundTag();
        encode(data, flower);
        tag.put(KEY, data);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    private static CompoundTag read(ItemStack stack) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        return data == null ? new CompoundTag() : data.copyTag();
    }
}
