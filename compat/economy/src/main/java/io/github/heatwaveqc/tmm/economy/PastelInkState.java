package io.github.heatwaveqc.tmm.economy;

import earth.terrarium.pastel.registries.PastelDataComponentTypes;
import net.minecraft.world.item.ItemStack;

/** Optional typed Pastel bridge, called only when the mod is loaded. */
final class PastelInkState {
    private PastelInkState() {}
    static boolean held(ItemStack actual, ItemStack plain) {
        var a = actual.get(PastelDataComponentTypes.INK_STORAGE);
        var b = plain.get(PastelDataComponentTypes.INK_STORAGE);
        java.util.Map<?,Long> av = a == null ? java.util.Map.of() : a.storedEnergy();
        java.util.Map<?,Long> bv = b == null ? java.util.Map.of() : b.storedEnergy();
        if (av.values().stream().anyMatch(v -> v < 0)
                || bv.values().stream().anyMatch(v -> v < 0)) return true;
        boolean any = av.values().stream().anyMatch(v -> v != 0)
                || bv.values().stream().anyMatch(v -> v != 0);
        return any && !av.equals(bv);
    }
}
