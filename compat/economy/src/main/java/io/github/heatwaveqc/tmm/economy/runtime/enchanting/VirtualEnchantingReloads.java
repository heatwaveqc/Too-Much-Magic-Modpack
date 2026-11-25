package io.github.heatwaveqc.tmm.economy.runtime.enchanting;

import java.util.concurrent.atomic.AtomicLong;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;

/** Invalidate old native previews on any server resource reload, even when
 * numeric shelf stats remain identical but infusion recipes/tags change. */
public final class VirtualEnchantingReloads {
    private VirtualEnchantingReloads() {}
    private static final AtomicLong REVISION = new AtomicLong();
    public static long revision() { return REVISION.get(); }
    public static void register() {
        NeoForge.EVENT_BUS.addListener(VirtualEnchantingReloads::addListener);
    }
    private static void addListener(AddReloadListenerEvent event) {
        event.addListener((ResourceManagerReloadListener) resources -> REVISION.incrementAndGet());
    }
}
