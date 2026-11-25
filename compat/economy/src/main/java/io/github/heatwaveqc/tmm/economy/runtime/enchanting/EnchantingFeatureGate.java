package io.github.heatwaveqc.tmm.economy.runtime.enchanting;

import java.util.function.Predicate;
import net.neoforged.fml.ModList;

/** Optional feature, not an Economy-wide dependency. No native mod classes here. */
public final class EnchantingFeatureGate {
    private EnchantingFeatureGate() {}

    public static boolean enabled(Predicate<String> loaded) {
        // Apotheosis and its split Enchanting module are distinct mod IDs.
        return loaded.test("improvableskills") && loaded.test("apotheosis")
            && loaded.test("apothic_enchanting");
    }

    public static boolean enabled() {
        ModList mods = ModList.get();
        return mods != null && enabled(mods::isLoaded);
    }
}
