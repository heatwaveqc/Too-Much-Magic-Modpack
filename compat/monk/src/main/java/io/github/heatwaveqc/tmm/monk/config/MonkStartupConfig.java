package io.github.heatwaveqc.tmm.monk.config;

import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * Registration-time Monk settings. STARTUP is required: a faction's
 * highestLevel is fixed at construction, which occurs before COMMON/SERVER
 * configs are loaded. Startup settings are not network-synchronized.
 */
public final class MonkStartupConfig {
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.IntValue MAX_FACTION_LEVEL;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        builder.push("progression");
        MAX_FACTION_LEVEL = builder
                .comment("Maximum Monk faction rank. Requires FULL client/server restart to change.",
                         "Both client and server must use exactly the same value.",
                         "Progression tasks and the UI above rank 20 have NOT been tested.")
                .defineInRange("max_faction_level", 20, 1, Integer.MAX_VALUE);
        builder.pop();
        SPEC = builder.build();
    }

    private MonkStartupConfig() {}

    public static int maximumFactionLevel() {
        return MAX_FACTION_LEVEL.get();
    }
}
