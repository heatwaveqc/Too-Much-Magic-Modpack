package io.github.heatwaveqc.tyr.progression.runtime;

import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/** Optional provider adapter; no hard dependency, display-name matching, or fallback
 * to accepting a generic ring. Based on Iron's Jewelry 1.21.1 JewelryData API. */
final class JewelryOfferingMaterials {
    private JewelryOfferingMaterials() {}
    static Set<String> read(ItemStack stack) {
        if (!ModList.get().isLoaded("irons_jewelry")) return Set.of();
        try {
            Class<?> type = Class.forName("io.redspace.ironsjewelry.core.data.JewelryData");
            Object data = type.getMethod("get",ItemStack.class).invoke(null,stack);
            if (data == null || !Boolean.TRUE.equals(type.getMethod("isValid").invoke(data))) return Set.of();
            if (!(type.getMethod("parts").invoke(data) instanceof Map<?,?> parts)) return Set.of();
            var ids = new HashSet<String>();
            for (Object value : parts.values()) {
                if (!(value instanceof Holder<?> holder) || holder.unwrapKey().isEmpty()) return Set.of();
                ids.add(holder.unwrapKey().orElseThrow().location().toString());
            }
            return Set.copyOf(ids);
        } catch (ReflectiveOperationException | LinkageError | RuntimeException e) {
            return Set.of(); // Missing or incompatible provider: refuse material-gated offering.
        }
    }
}
