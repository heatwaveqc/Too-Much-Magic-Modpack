package io.github.heatwaveqc.tmm.transformations.magic;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

/** Only used at the actual native Catalyst Exchange eligibility checks. */
public final class MahouCatalystDustBridge {
    private MahouCatalystDustBridge() {}
    public static List<Item> explicitDustItems() {
        List<Item> items = new ArrayList<>(5);
        for (String id : MahouCatalystDustPolicy.acceptedInputs()) {
            BuiltInRegistries.ITEM.getOptional(ResourceLocation.parse(id)).ifPresent(item -> {
                if (eligibilityItem(item) != item) items.add(item);
            });
        }
        return items;
    }
    public static Item eligibilityItem(Item original) {
        String powder = MahouCatalystDustPolicy.nativePowder(BuiltInRegistries.ITEM.getKey(original).toString());
        if (powder == null) return original;
        return BuiltInRegistries.ITEM.getOptional(ResourceLocation.parse(powder)).orElse(original);
    }
}
