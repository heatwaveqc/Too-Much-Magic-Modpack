package io.github.heatwaveqc.tyr.progression.mxp;

import io.github.heatwaveqc.tyr.progression.core.MxpMath;
import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * Server-authoritative progression tuning, separate from source pool baselines.
 * Values are read when an award is processed; existing saved lifetime P is kept.
 */
public final class MxpProgressionConfig {
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.DoubleValue GROWTH_FACTOR, BOSS_REPEAT, MINIBOSS_REPEAT, ELITE_BASE;
    public static final ModConfigSpec.IntValue BOSS_CREDIT_TICKS;

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
        builder.push("boss_rewards");
        BOSS_REPEAT = builder.comment("Base MXP for repeat genuine regular victories; divided. First is fixed 1000.").defineInRange("standard_repeat",1000.0,0.01,100000.0);
        MINIBOSS_REPEAT = builder.comment("Base MXP for repeat reviewed miniboss victories; divided. First is fixed 250.").defineInRange("miniboss_repeat",250.0,0.01,100000.0);
        ELITE_BASE = builder.comment("Ordinary divided bonus for an Apotheosis Elite or common/uncommon Invader. Rare/Epic/Mythic multiply by 2/3/4; no first-victory discovery.").defineInRange("apotheosis_base",10.0,0.01,10000.0);
        BOSS_CREDIT_TICKS = builder.comment("Per-player committed damage credit window; vanilla convention is 100 ticks.").defineInRange("credit_window_ticks",100,1,1200);
        builder.pop();
        SPEC = builder.build();
    }

    private MxpProgressionConfig() {}
}
