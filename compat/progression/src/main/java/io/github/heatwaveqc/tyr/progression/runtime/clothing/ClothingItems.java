package io.github.heatwaveqc.tyr.progression.runtime.clothing;

import io.github.heatwaveqc.tyr.progression.ProgressionMod;
import java.util.EnumMap;
import java.util.List;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Registration checkpoint. Empty material layer lists intentionally leave
 * worn rendering to the next artwork/tint slice; not a shipping visual asset. */
public final class ClothingItems {
    private ClothingItems() {}
    public static final DeferredRegister<ArmorMaterial> MATERIALS =
            DeferredRegister.create(Registries.ARMOR_MATERIAL, ProgressionMod.MOD_ID);
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(ProgressionMod.MOD_ID);

    private static ArmorMaterial nonProtectiveMaterial() {
        var defense = new EnumMap<ArmorItem.Type, Integer>(ArmorItem.Type.class);
        for (var type : ArmorItem.Type.values()) defense.put(type, 0);
        return new ArmorMaterial(defense, 0, SoundEvents.ARMOR_EQUIP_LEATHER,
                () -> Ingredient.EMPTY, List.of(), 0.0F, 0.0F);
    }

    // Independent materials let the renderer define distinct layers per garment.
    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> KILT_MATERIAL =
            MATERIALS.register("tartan_kilt", ClothingItems::nonProtectiveMaterial);
    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> CAP_MATERIAL =
            MATERIALS.register("white_cap", ClothingItems::nonProtectiveMaterial);
    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> LOINCLOTH_MATERIAL =
            MATERIALS.register("loincloth", ClothingItems::nonProtectiveMaterial);

    public static final DeferredItem<ClothingItem> TARTAN_KILT =
            ITEMS.register("tartan_kilt", () -> new ClothingItem(KILT_MATERIAL, ArmorItem.Type.LEGGINGS));
    public static final DeferredItem<ClothingItem> WHITE_CAP =
            ITEMS.register("white_cap", () -> new ClothingItem(CAP_MATERIAL, ArmorItem.Type.HELMET));
    public static final DeferredItem<ClothingItem> LOINCLOTH =
            ITEMS.register("loincloth", () -> new ClothingItem(LOINCLOTH_MATERIAL, ArmorItem.Type.LEGGINGS));
}
