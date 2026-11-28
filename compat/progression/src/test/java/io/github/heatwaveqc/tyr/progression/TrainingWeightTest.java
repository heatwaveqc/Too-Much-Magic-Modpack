package io.github.heatwaveqc.tyr.progression;
import io.github.heatwaveqc.tyr.progression.foundation.TrainingWeight;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TrainingWeightTest {
    private static void same(String expected,BigDecimal actual){assertEquals(0,new BigDecimal(expected).compareTo(actual));}
    @Test void fullSetsAndSinglePieceTargets(){
        String[] mult={"1","1.5","2","3"}, speed={"0","-0.25","-0.5","-0.75"};
        String[] pieceMult={"1","1.125","1.25","1.5"}, pieceSpeed={"0","-0.0625","-0.125","-0.1875"};
        var n=TrainingWeight.NORMAL;
        for(var tier:TrainingWeight.values()){
            var full=TrainingWeight.loadout(tier,tier,tier,tier);
            same(mult[tier.id()],full.multiplier());same(speed[tier.id()],full.speedChange());
            var single=TrainingWeight.loadout(n,n,n,tier);
            same(pieceMult[tier.id()],single.multiplier());same(pieceSpeed[tier.id()],single.speedChange());
            assertSame(tier,TrainingWeight.byId(tier.id()));
        }
        assertThrows(IllegalArgumentException.class,()->TrainingWeight.byId(-1));
        assertThrows(IllegalArgumentException.class,()->TrainingWeight.byId(4));
    }
    @Test void all256LoadoutsAddExactlyAndNeverMultiplyExcludedXp(){
        int[] bonusUnits={0,1,2,4}; // eighths of base reward
        int[] speedUnits={0,1,2,3}; // sixteenths of speed
        var base=new BigDecimal("100");
        int count=0;
        for(var a:TrainingWeight.values())for(var b:TrainingWeight.values())
            for(var c:TrainingWeight.values())for(var d:TrainingWeight.values()){
                var load=TrainingWeight.loadout(a,b,c,d);
                int bonus=bonusUnits[a.id()]+bonusUnits[b.id()]+bonusUnits[c.id()]+bonusUnits[d.id()];
                int speed=speedUnits[a.id()]+speedUnits[b.id()]+speedUnits[c.id()]+speedUnits[d.id()];
                assertEquals(0,BigDecimal.ONE.add(BigDecimal.valueOf(bonus).divide(BigDecimal.valueOf(8))).compareTo(load.multiplier()));
                assertEquals(0,BigDecimal.valueOf(-speed).divide(BigDecimal.valueOf(16)).compareTo(load.speedChange()));
                assertEquals(0,base.multiply(load.multiplier()).compareTo(load.nominal(base,true,false)));
                assertEquals(base,load.nominal(base,false,false));assertEquals(base,load.nominal(base,true,true));
                count++;
            }
        assertEquals(256,count);
    }
    @Test void upgradesOnly(){
        for(var a:TrainingWeight.values())for(var b:TrainingWeight.values())
            assertEquals(b.id()>a.id(),a.canUpgradeTo(b));
    }
}
