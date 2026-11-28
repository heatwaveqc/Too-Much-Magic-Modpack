package io.github.heatwaveqc.tyr.progression.foundation;

import java.math.BigDecimal;

/** Full-set targets; each of four standard armor slots contributes one quarter
 * of the bonus above baseline and one quarter of the speed penalty. */
public enum TrainingWeight {
    NORMAL(0,"0","0"), LIGHT(1,"0.5","-0.25"), HEAVY(2,"1","-0.50"), EXTREME(3,"2","-0.75");
    private final int id;
    private final BigDecimal fullBonus;
    private final BigDecimal fullSpeed;
    TrainingWeight(int id,String bonus,String speed){
        this.id=id; this.fullBonus=new BigDecimal(bonus); this.fullSpeed=new BigDecimal(speed);
    }
    public int id(){return id;}
    public BigDecimal pieceBonus(){return fullBonus.divide(BigDecimal.valueOf(4));}
    public BigDecimal pieceSpeed(){return fullSpeed.divide(BigDecimal.valueOf(4));}
    public static TrainingWeight byId(int id){
        if(id<0||id>3)throw new IllegalArgumentException("Invalid training weight");
        return values()[id];
    }
    public boolean canUpgradeTo(TrainingWeight target){return target.id>id;}

    public record Loadout(BigDecimal multiplier,BigDecimal speedChange){
        public BigDecimal nominal(BigDecimal base,boolean verifiedPhysical,boolean fixed){
            return verifiedPhysical&&!fixed?base.multiply(multiplier):base;
        }
        public double speed(){return speedChange.doubleValue();}
    }
    /** Fixed four-slot boundary; absent/ordinary/invalid pieces are NORMAL. */
    public static Loadout loadout(TrainingWeight head,TrainingWeight chest,TrainingWeight legs,TrainingWeight feet){
        BigDecimal multiplier=BigDecimal.ONE,speed=BigDecimal.ZERO;
        for(var tier:new TrainingWeight[]{head,chest,legs,feet}){
            multiplier=multiplier.add(tier.pieceBonus());
            speed=speed.add(tier.pieceSpeed());
        }
        return new Loadout(multiplier,speed);
    }
}
