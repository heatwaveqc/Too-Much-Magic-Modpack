package io.github.heatwaveqc.tyr.progression.client;

import java.lang.reflect.Method;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.EntityCapability;

/**
 * Optional ProjectE 1.21.1 native capability adapter. Queries the current
 * client-synced IKnowledgeProvider, including full-knowledge Tome and NBT/item
 * normalization through ProjectE's own hasKnowledge(ItemStack) default method.
 * Never copies learned states to a second cache.
 */
public final class NativeProjecteKnowledge {
    private NativeProjecteKnowledge() {}

    private record Methods(EntityCapability<?, ?> capability, Method hasKnowledge) {}
    private static volatile Methods resolved;
    private static volatile boolean attempted;

    private static Methods methods() {
        if (attempted) return resolved;
        synchronized (NativeProjecteKnowledge.class) {
            if (attempted) return resolved;
            try {
                Class<?> capabilities = Class.forName("moze_intel.projecte.api.capabilities.PECapabilities");
                Object cap = capabilities.getField("KNOWLEDGE_CAPABILITY").get(null);
                Class<?> provider = Class.forName("moze_intel.projecte.api.capabilities.IKnowledgeProvider");
                if (cap instanceof EntityCapability<?, ?> entityCap) {
                    resolved = new Methods(entityCap, provider.getMethod("hasKnowledge", ItemStack.class));
                }
            } catch (ReflectiveOperationException | LinkageError | RuntimeException ignored) {
                // A missing or changed optional ABI is an UNKNOWN, never learned.
            } finally {
                attempted = true;
            }
            return resolved;
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static boolean hasKnowledge(Player player, ItemStack stack) {
        if (player == null || stack == null || stack.isEmpty()) return false;
        Methods handles = methods();
        if (handles == null) return false;
        try {
            Object provider = player.getCapability((EntityCapability) handles.capability());
            return provider != null && Boolean.TRUE.equals(handles.hasKnowledge().invoke(provider, stack));
        } catch (ReflectiveOperationException | LinkageError | RuntimeException ignored) {
            return false;
        }
    }
}
