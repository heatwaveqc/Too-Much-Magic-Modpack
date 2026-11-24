package io.github.heatwaveqc.tmm.transformations.magic;

import java.util.Map;
import java.util.Set;

/** Pure fixed identity policy. Native Mahou still controls payment, timing and output. */
public final class MahouCatalystDustPolicy {
    private static final Map<String,String> POWDERS = Map.of(
        "neovitae:iron_dust", "mahoutsukai:powdered_iron",
        "neovitae:gold_dust", "mahoutsukai:powdered_gold",
        "neovitae:diamond_dust", "mahoutsukai:powdered_diamond",
        "neovitae:emerald_dust", "mahoutsukai:powdered_emerald",
        "neovitae:quartz_dust", "mahoutsukai:powdered_quartz"
    );
    private MahouCatalystDustPolicy() {}
    public static Set<String> acceptedInputs() { return POWDERS.keySet(); }
    public static String nativePowder(String key) { return POWDERS.get(key); }
}
