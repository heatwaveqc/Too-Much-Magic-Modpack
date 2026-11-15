package io.github.heatwaveqc.tmm.transformations.lich;

import net.neoforged.neoforge.common.ModConfigSpec;

/** Test-world gate, independent of the optional vampire chassis. */
public final class ArchlichResurrectionConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue ENABLE_PRIORITY = BUILDER
            .comment("EXPERIMENTAL / enabled by default for disposable test worlds: dual Goety +",
                     "Forbidden Magic lich, attempt native JAWR charged phylactery",
                     "resurrection before native Goety Arca fallback.",
                     "Requires pinned Goety 3.2.2 and Forbidden Magic 0.5.37.",
                     "Does not combine hunger, respawn rules or transformation cures.")
            .define("experimentalArchlichPhylacteryFirst", true);

    public static final ModConfigSpec SPEC = BUILDER.build();

    private ArchlichResurrectionConfig() { }

    public static boolean enabled() { return ENABLE_PRIORITY.get(); }
}
