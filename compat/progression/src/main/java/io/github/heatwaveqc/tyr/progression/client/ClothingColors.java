package io.github.heatwaveqc.tyr.progression.client;

import io.github.heatwaveqc.tyr.progression.ProgressionMod;
import io.github.heatwaveqc.tyr.progression.foundation.TartanPalette;
import io.github.heatwaveqc.tyr.progression.runtime.clothing.ClothingComponents;
import io.github.heatwaveqc.tyr.progression.runtime.clothing.ClothingItems;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

/** Same bounded palette for inventory and worn cutout masks.
 * Client-only subscriber; no per-frame model or texture allocation/upload. */
@EventBusSubscriber(modid = ProgressionMod.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public final class ClothingColors {
    private ClothingColors() {}
    private static final TartanPalette DEFAULT_KILT = TartanPalette.of(6, 8, 15, 12);
    private static int color(int id) {
        return 0xff000000 | (DyeColor.byId(id).getTextureDiffuseColor() & 0xffffff);
    }
    static int tint(ItemStack stack, int layer) {
        if (layer < 0) return -1;
        if (stack.is(ClothingItems.TARTAN_KILT.get())) {
            if (layer >= 4) return -1;
            return color(stack.getOrDefault(ClothingComponents.TARTAN_PALETTE.get(), DEFAULT_KILT).dye(layer));
        }
        if (layer >= 3) return -1;
        return ClothingItems.martialPalette(stack).map(palette -> color(palette.dye(layer))).orElse(-1);
    }

    private static final IClientItemExtensions ARMOR_TINT = new IClientItemExtensions() {
        @Override public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entity, ItemStack stack,
                EquipmentSlot slot, HumanoidModel<?> original) {
            return ClothingGeometry.select(stack, slot, original);
        }
        @Override public int getArmorLayerTintColor(ItemStack stack, LivingEntity entity,
                ArmorMaterial.Layer layer, int layerIdx, int fallbackColor) {
            return tint(stack, layerIdx);
        }
    };

    @SubscribeEvent public static void itemColors(RegisterColorHandlersEvent.Item event) {
        event.register(ClothingColors::tint, ClothingItems.TARTAN_KILT.get());
        for (var set : ClothingItems.MARTIAL.values())
            event.register(ClothingColors::tint, set.upper().get(), set.lower().get());
    }

    @SubscribeEvent public static void armorColors(RegisterClientExtensionsEvent event) {
        event.registerItem(ARMOR_TINT, ClothingItems.TARTAN_KILT.get(), ClothingItems.WHITE_CAP.get(), ClothingItems.LOINCLOTH.get());
        for (var set : ClothingItems.MARTIAL.values())
            event.registerItem(ARMOR_TINT, set.upper().get(), set.lower().get());
    }
}
