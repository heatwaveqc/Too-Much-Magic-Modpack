package io.github.heatwaveqc.tmm.economy.runtime.enchanting;

/** Pure transaction guard: one permanent installation, no refund or downgrade. */
public final class PortableAnvilInstallPolicy {
    private PortableAnvilInstallPolicy() {}
    public static boolean canInstall(boolean authorized, boolean notInstalled,
                                     boolean exactItem, int count, boolean clearOtherSlots) {
        return authorized && notInstalled && exactItem && count > 0 && clearOtherSlots;
    }
}
