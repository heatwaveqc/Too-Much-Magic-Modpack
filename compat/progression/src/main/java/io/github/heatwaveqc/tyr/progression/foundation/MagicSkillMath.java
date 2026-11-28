package io.github.heatwaveqc.tyr.progression.foundation;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** Shared dimensionless contributions. Native adapters own units, admission and lifecycle. */
public final class MagicSkillMath {
    private MagicSkillMath(){}
    public static double cap(double contribution,double limit){
        if(!Double.isFinite(contribution)||!Double.isFinite(limit)||limit<0)return 0;
        return Math.max(0,Math.min(contribution,limit));
    }
    /** Positive recovery/capacity/effect only; never turn a drain into a bonus. */
    public static double increase(double nativeValue,double contribution,double limit){
        if(!Double.isFinite(nativeValue)||nativeValue<=0)return nativeValue;
        double value=nativeValue*(1+cap(contribution,limit));
        return Double.isFinite(value)?value:nativeValue;
    }
    /** Positive quoted cost or active timer never becomes zero; zero/free remains zero/free.
     * Unit (ticks, milliseconds, resource units) belongs to the verified caller. */
    public static long reduce(long nativeValue,double contribution,double limit){
        if(nativeValue<=0)return nativeValue;
        double rate=cap(contribution,Math.min(1,limit));
        if(rate==0)return nativeValue;
        return Math.max(1,BigDecimal.valueOf(nativeValue)
            .multiply(BigDecimal.ONE.subtract(BigDecimal.valueOf(rate)))
            .setScale(0,RoundingMode.CEILING).longValueExact());
    }
}
