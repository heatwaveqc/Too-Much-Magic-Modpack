package io.github.heatwaveqc.tmm.economy;

import moze_intel.projecte.api.ItemInfo;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

/** The Tome's native default stack is uncharged and contains no stored fuel.
 * Never specialize its learned identity or its price by components. */
public final class TomeTemplatePolicy {
    private TomeTemplatePolicy() {}

    private static final ResourceLocation TOME = ResourceLocation.parse("reliquary:alkahestry_tome");

    public static boolean applies(ItemInfo info) {
        return info != null && TOME.equals(BuiltInRegistries.ITEM.getKey(info.getItem().value()));
    }

    public static ItemInfo normalize(ItemInfo info) {
        return applies(info) ? info.itemOnly() : info;
    }
}
