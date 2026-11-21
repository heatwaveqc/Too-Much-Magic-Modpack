package io.github.heatwaveqc.tyr.progression.mxp.core;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.ArrayList;

/** Pure final-paying-unit policy for the immutable native quote. No debit or player mutation. */
public final class AnsThriftMath {
    public static final String MARKER="tmm_thrift";
    private AnsThriftMath(){}

    public static double factor(double fraction){
        if(!Double.isFinite(fraction)||fraction<=0)return 1;
        return BigDecimal.ONE.subtract(BigDecimal.valueOf(Math.min(.95,fraction))).doubleValue();
    }

    public record Leg(String unit,double amount){}
    public static boolean validFactor(double f){return Double.isFinite(f)&&f>=.05&&f<=1;}
    public static boolean mana(String unit){return "ARS_MANA".equals(unit)||"IRONS_MANA".equals(unit);}

    /**
     * Empty, malformed, non-mana or already non-integral native legs are not transformed.
     * A zero stays zero. Every formerly positive rounded leg stays >=1 and <= its native cost.
     */
    public static List<Leg> discount(List<Leg> legs,double factor){
        if(legs==null||legs.isEmpty()||!validFactor(factor))return null;
        var out=new ArrayList<Leg>(legs.size());
        for(var leg:legs){
            if(leg==null||!mana(leg.unit())||!Double.isFinite(leg.amount())||
                leg.amount()<0||leg.amount()>Integer.MAX_VALUE||leg.amount()!=Math.rint(leg.amount()))return null;
            double amount=leg.amount();
            if(amount>0&&factor<1){
                // Decimal product avoids ceil(20 * (1 - .95)) becoming 2 through floating error.
                amount=Math.max(1,BigDecimal.valueOf(amount).multiply(BigDecimal.valueOf(factor))
                    .setScale(0,RoundingMode.CEILING).doubleValue());
                amount=Math.min(leg.amount(),amount);
            }
            out.add(new Leg(leg.unit(),amount));
        }
        return List.copyOf(out);
    }
}
