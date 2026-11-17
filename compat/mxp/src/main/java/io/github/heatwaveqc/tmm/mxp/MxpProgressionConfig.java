package io.github.heatwaveqc.tmm.mxp;

import io.github.heatwaveqc.tmm.mxp.core.MxpMath;
import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * Server-authoritative progression tuning, separate from source pool baselines.
 * Values are read when an award is processed; existing saved lifetime P is kept.
 */
public final class MxpProgressionConfig {
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.DoubleValue GROWTH_FACTOR;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        builder.push("progression");
        GROWTH_FACTOR = builder
            .comment(
                "Divisor for ordinary magic XP: effectiveXP = normalizedBaseXP / growth_factor^P.",
                "P is lifetime MXP levels earned, not Tyz skill points spent; respec does not reset it.",
                "1.0 makes XP linear; 1.01 grows slowly; 1.02 is the TMM default; 1.1 or 1.2 are much steeper.",
                "Fixed first-time ritual/boss rewards bypass this divisor.",
                "Changing the factor affects future ordinary awards for all players; it does not rescale stored P or progress.")
            .defineInRange("growth_factor", MxpMath.DEFAULT_GROWTH,
                MxpMath.MIN_GROWTH, MxpMath.MAX_GROWTH);
        builder.pop();
        SPEC = builder.build();
    }

    private MxpProgressionConfig() {}
}
