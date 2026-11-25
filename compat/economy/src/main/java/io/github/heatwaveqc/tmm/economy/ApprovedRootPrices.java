package io.github.heatwaveqc.tmm.economy;

import com.google.gson.JsonParser;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.Item;
import org.slf4j.Logger;

/** Read plain, positive, explicitly locked item roots from the effective
 * TMM conversion datapack. Missing, malformed or conflicting roots do not
 * authorize co-product subtraction. No new fixed prices are registered. */
final class ApprovedRootPrices {
    private static final Logger LOG = LogUtils.getLogger();
    private ApprovedRootPrices() {}
    static Map<Item,Long> read(ResourceManager manager) {
        Map<Item,Long> roots = new HashMap<>();
        Set<Item> conflicts = new HashSet<>();
        for (var resource : manager.listResources("pe_custom_conversions",
                id -> id.getNamespace().equals("tmm") && id.getPath().endsWith(".json")).entrySet()) {
            try (var reader = resource.getValue().openAsReader()) {
                var json = JsonParser.parseReader(reader).getAsJsonObject();
                if (!json.has("values")) continue;
                var vals = json.getAsJsonObject("values");
                for (String group : new String[]{"before","after"}) {
                    if (!vals.has(group)) continue;
                    for (var raw : vals.getAsJsonArray(group)) {
                        if (!raw.isJsonObject()) continue;
                        var e = raw.getAsJsonObject();
                        if (!e.keySet().equals(Set.of("type","id","emc_value"))
                                || !"projecte:item".equals(e.get("type").getAsString())
                                || !e.get("emc_value").isJsonPrimitive()
                                || !e.get("emc_value").getAsJsonPrimitive().isNumber()) continue;
                        long price = e.get("emc_value").getAsBigDecimal().longValueExact();
                        BuiltInRegistries.ITEM.getOptional(ResourceLocation.parse(e.get("id").getAsString())).ifPresent(item -> {
                            Long old = roots.putIfAbsent(item,price);
                            if (price <= 0 || (old != null && old.longValue() != price)) conflicts.add(item);
                        });
                    }
                }
            } catch (IOException | RuntimeException e) {
                // A malformed active file could hide a conflicting root.
                LOG.warn("TMM fixed-root co-product inference disabled: {}", resource.getKey(), e);
                return Map.of();
            }
        }
        conflicts.forEach(roots::remove);
        return Map.copyOf(roots);
    }
}
