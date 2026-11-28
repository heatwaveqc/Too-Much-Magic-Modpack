package io.github.heatwaveqc.tyr.progression.runtime.clothing;

import io.github.heatwaveqc.tyr.progression.ProgressionMod;
import java.util.EnumMap;
import java.util.ArrayList;
import net.minecraft.resources.ResourceLocation;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import io.github.heatwaveqc.tyr.progression.foundation.MartialGarmentStyle;
import io.github.heatwaveqc.tyr.progression.foundation.GarmentPalette;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Native non-protective materials, with fixed per-channel texture layers. */
public final class ClothingItems {
    private ClothingItems() {}
    public static final DeferredRegister<ArmorMaterial> MATERIALS =
            DeferredRegister.create(Registries.ARMOR_MATERIAL, ProgressionMod.MOD_ID);
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(ProgressionMod.MOD_ID);

    private static ArmorMaterial nonProtectiveMaterial(String asset, int channels) {
        var defense = new EnumMap<ArmorItem.Type, Integer>(ArmorItem.Type.class);
        for (var type : ArmorItem.Type.values()) defense.put(type, 0);
        var layers = new ArrayList<ArmorMaterial.Layer>();
        var id = ResourceLocation.fromNamespaceAndPath(ProgressionMod.MOD_ID, asset);
        if (channels == 0) layers.add(new ArmorMaterial.Layer(id, "", false));
        else {
            for (int channel = 0; channel < channels; channel++)
                layers.add(new ArmorMaterial.Layer(id, "_" + channel, true));
            layers.add(new ArmorMaterial.Layer(id, "_" + channels, false));
        }
        return new ArmorMaterial(defense, 0, SoundEvents.ARMOR_EQUIP_LEATHER,
                () -> Ingredient.EMPTY, List.copyOf(layers), 0.0F, 0.0F);
    }

    // Independent materials let the renderer define distinct layers per garment.
    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> KILT_MATERIAL =
            MATERIALS.register("tartan_kilt", () -> nonProtectiveMaterial("tartan_kilt", 4));
    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> CAP_MATERIAL =
            MATERIALS.register("white_cap", () -> nonProtectiveMaterial("white_cap", 0));
    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> LOINCLOTH_MATERIAL =
            MATERIALS.register("loincloth", () -> nonProtectiveMaterial("loincloth", 0));

    public static final DeferredItem<ClothingItem> TARTAN_KILT =
            ITEMS.register("tartan_kilt", () -> new ClothingItem(KILT_MATERIAL, ArmorItem.Type.LEGGINGS));
    public static final DeferredItem<ClothingItem> WHITE_CAP =
            ITEMS.register("white_cap", () -> new ClothingItem(CAP_MATERIAL, ArmorItem.Type.HELMET));
    public static final DeferredItem<ClothingItem> LOINCLOTH =
            ITEMS.register("loincloth", () -> new ClothingItem(LOINCLOTH_MATERIAL, ArmorItem.Type.LEGGINGS));
    public record MartialSet(DeferredItem<ClothingItem> upper, DeferredItem<ClothingItem> lower) {}
    public static final Map<MartialGarmentStyle, MartialSet> MARTIAL = registerMartialSets();

    private static Map<MartialGarmentStyle, MartialSet> registerMartialSets() {
        var sets = new EnumMap<MartialGarmentStyle, MartialSet>(MartialGarmentStyle.class);
        for (var style : MartialGarmentStyle.values()) {
            var material = MATERIALS.register(style.id(), () -> nonProtectiveMaterial(style.id(), 3));
            var upper = ITEMS.register(style.upperId(), () -> new ClothingItem(material, ArmorItem.Type.CHESTPLATE));
            var lower = ITEMS.register(style.lowerId(), () -> new ClothingItem(material, ArmorItem.Type.LEGGINGS));
            sets.put(style, new MartialSet(upper, lower));
        }
        return Map.copyOf(sets);
    }

    public static Optional<MartialGarmentStyle> martialStyle(ItemStack stack) {
        if (stack.isEmpty()) return Optional.empty();
        for (var entry : MARTIAL.entrySet()) {
            var set = entry.getValue();
            if (stack.is(set.upper().get()) || stack.is(set.lower().get())) return Optional.of(entry.getKey());
        }
        return Optional.empty();
    }

    /** Creative/given items without a custom palette use their style's defaults.
     * Renderers should call this rather than duplicating default-color rules. */
    public static Optional<GarmentPalette> martialPalette(ItemStack stack) {
        return martialStyle(stack).map(style ->
                stack.getOrDefault(ClothingComponents.GARMENT_PALETTE.get(), style.defaults()));
    }
}
