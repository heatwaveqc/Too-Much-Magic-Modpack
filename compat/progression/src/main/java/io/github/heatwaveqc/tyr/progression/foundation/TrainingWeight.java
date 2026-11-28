package io.github.heatwaveqc.tyr.progression.foundation;

import java.math.BigDecimal;

/** Highest equipped tier wins; weight modifies verified physical training only. */
public enum TrainingWeight {
    NORMAL(0, "1", 0), LIGHT(1, "1.5", -0.15), HEAVY(2, "2", -0.30), EXTREME(3, "3", -0.50);
    private final int id;
    private final BigDecimal multiplier;
    private final double speed;
    TrainingWeight(int id,String multiplier,double speed){
        this.id=id; this.multiplier=new BigDecimal(multiplier); this.speed=speed;
    }
    public int id(){return id;}
    public BigDecimal multiplier(){return multiplier;}
    public double speed(){return speed;}
    public static TrainingWeight byId(int id){
        if(id<0||id>3)throw new IllegalArgumentException("Invalid training weight");
        return values()[id];
    }
    public static TrainingWeight highest(TrainingWeight a,TrainingWeight b){
        return a.id>=b.id?a:b;
    }
    public boolean canUpgradeTo(TrainingWeight target){return target.id>id;}
    public BigDecimal nominal(BigDecimal base,boolean verifiedPhysical,boolean fixed){
        return verifiedPhysical&&!fixed?base.multiply(multiplier):base;
    }
}
