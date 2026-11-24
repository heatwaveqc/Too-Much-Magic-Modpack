package io.github.heatwaveqc.tyr.progression.runtime;

import com.mojang.logging.LogUtils;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.EntityCapability;
import org.slf4j.Logger;

/**
 * Optional server-side ProjectE PE1.1.0 knowledge adapter.
 * Uses only native knowledge and EMC-query methods, never an EMC mutation,
 * item grant, full-knowledge Tome, or secondary knowledge store.
 */
final class CovalenceKnowledgeGrant {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final List<String> DUST_IDS = List.of(
        "low_covalence_dust", "medium_covalence_dust", "high_covalence_dust");
    private record Methods(EntityCapability<?, ?> capability, Method hasKnowledge,
        Method addKnowledge, Method sync, Object emcProxy, Method hasValue) {}
    private static Methods methods;
    private static boolean attempted;
    private static boolean warned;

    private CovalenceKnowledgeGrant() {}

    private static Methods resolve() throws ReflectiveOperationException {
        if (attempted) return methods;
        Class<?> caps = Class.forName("moze_intel.projecte.api.capabilities.PECapabilities");
        Class<?> provider = Class.forName("moze_intel.projecte.api.capabilities.IKnowledgeProvider");
        Class<?> emc = Class.forName("moze_intel.projecte.api.proxy.IEMCProxy");
        Object capability = caps.getField("KNOWLEDGE_CAPABILITY").get(null);
        if (!(capability instanceof EntityCapability<?, ?> entityCapability)) {
            throw new IllegalStateException("ProjectE knowledge capability has unexpected type");
        }
        methods = new Methods(entityCapability, provider.getMethod("hasKnowledge", ItemStack.class),
            provider.getMethod("addKnowledge", ItemStack.class), provider.getMethod("sync", ServerPlayer.class),
            emc.getField("INSTANCE").get(null), emc.getMethod("hasValue", ItemStack.class));
        attempted = true;
        return methods;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    static boolean deliver(ServerPlayer player) {
        try {
            Methods api = resolve();
            Object provider = player.getCapability((EntityCapability) api.capability());
            if (provider == null) return unavailable("native knowledge provider unavailable");
            // Preflight every ID and effective EMC before changing any knowledge.
            List<ItemStack> dusts = new ArrayList<>(DUST_IDS.size());
            for (String name : DUST_IDS) {
                ResourceLocation id = ResourceLocation.fromNamespaceAndPath("projecte", name);
                if (!BuiltInRegistries.ITEM.containsKey(id)) return unavailable("missing " + id);
                ItemStack dust = new ItemStack(BuiltInRegistries.ITEM.get(id));
                if (dust.isEmpty() || !Boolean.TRUE.equals(api.hasValue().invoke(api.emcProxy(), dust))) {
                    return unavailable("no positive mapped EMC for " + id);
                }
                dusts.add(dust);
            }
            for (ItemStack dust : dusts) {
                if (!Boolean.TRUE.equals(api.hasKnowledge().invoke(provider, dust))) {
                    api.addKnowledge().invoke(provider, dust);
                }
                if (!Boolean.TRUE.equals(api.hasKnowledge().invoke(provider, dust))) {
                    return unavailable("native knowledge grant did not take effect");
                }
            }
            // Full native sync once after the three entries, including after
            // a partial failure/retry. No change to the native EMC balance.
            api.sync().invoke(provider, player);
            return true;
        } catch (ReflectiveOperationException | LinkageError | RuntimeException exception) {
            // Preserve the durable pending receipt and retry through the normal
            // milestone reconciliation. Missing optional ABI cannot crash login.
            return unavailable(exception.toString());
        }
    }

    private static boolean unavailable(String reason) {
        if (!warned) {
            LOGGER.warn("Covalence Dust knowledge delivery pending: {}", reason);
            warned = true;
        }
        return false;
    }
}
