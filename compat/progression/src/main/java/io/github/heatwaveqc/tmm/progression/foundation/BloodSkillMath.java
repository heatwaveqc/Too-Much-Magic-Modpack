package io.github.heatwaveqc.tmm.progression.foundation;

/** Bounded native integer blood arithmetic; never round positive cost to zero. */
public final class BloodSkillMath {
    private BloodSkillMath(){}
    public static int price(int nominal,double rate){
        if(nominal<=0||!Double.isFinite(rate)||rate<=0)return nominal;
        var product=java.math.BigDecimal.valueOf(nominal).multiply(java.math.BigDecimal.ONE.subtract(
            java.math.BigDecimal.valueOf(Math.min(.25,rate))));
        return Math.max(1,product.setScale(0,java.math.RoundingMode.CEILING).intValueExact());
    }
    public static int feedingBonus(int actual,int remaining,double fraction){
        if(actual<=0||remaining<=0||!Double.isFinite(fraction)||fraction<=0)return 0;
        return (int)Math.min(remaining,Math.floor(actual*Math.min(1,fraction)));
    }
    public static float sunDamage(float amount,double fraction){
        // Native instant-death branch (Float.MAX_VALUE) remains untouched.
        if(!Float.isFinite(amount)||amount<=0||amount>=Float.MAX_VALUE/2||
            !Double.isFinite(fraction)||fraction<=0)return amount;
        return (float)(amount*(1-Math.min(.75,fraction)));
    }
}
