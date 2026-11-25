package io.github.heatwaveqc.tmm.economy.runtime.enchanting;

import org.zeith.improvableskills.net.PacketOpenPortableEnch;
import org.zeith.improvableskills.client.gui.abil.ench.ContainerPortableEnchantment;
import org.zeith.improvableskills.client.gui.abil.ench.ContainerEnchPowBook;

/**
 * An all-or-nothing cutover. Marker interfaces are added only by the matching
 * Mixins. No refund or new menu can be opened with one old power path still live.
 * Access only behind EnchantingFeatureGate.
 */
public final class PortableEnchantingCutover {
    private PortableEnchantingCutover() {}
    public interface AbilityRoute {}
    public interface OldEnchantRoute {}
    public interface OldBookRoute {}

    public static boolean ready() {
        if (!EnchantingFeatureGate.enabled()) return false;
        try {
            return AbilityRoute.class.isAssignableFrom(PacketOpenPortableEnch.class)
                && OldEnchantRoute.class.isAssignableFrom(ContainerPortableEnchantment.class)
                && OldBookRoute.class.isAssignableFrom(ContainerEnchPowBook.class);
        } catch (LinkageError e) {
            return false;
        }
    }
}
