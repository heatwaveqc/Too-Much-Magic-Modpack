package io.github.heatwaveqc.tyr.progression;
import io.github.heatwaveqc.tyr.progression.foundation.TrainingWeight;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TrainingWeightTest {
    @Test void exactPhysicalMultipliersAndFixedOrMagicalExclusion(){
        var amount=new BigDecimal("100");
        var expected=new int[]{100,150,200,300};
        var speed=new double[]{0,-.15,-.30,-.50};
        for(var tier:TrainingWeight.values()){
            assertEquals(0,BigDecimal.valueOf(expected[tier.id()]).compareTo(tier.nominal(amount,true,false)));
            assertEquals(amount,tier.nominal(amount,false,false));
            assertEquals(amount,tier.nominal(amount,true,true));
            assertEquals(speed[tier.id()],tier.speed());
            assertSame(tier,TrainingWeight.byId(tier.id()));
        }
        assertThrows(IllegalArgumentException.class,()->TrainingWeight.byId(-1));
        assertThrows(IllegalArgumentException.class,()->TrainingWeight.byId(4));
    }
    @Test void highestWinsAndOnlyUpwardUpgrades(){
        for(var a:TrainingWeight.values())for(var b:TrainingWeight.values()){
            assertEquals(Math.max(a.id(),b.id()),TrainingWeight.highest(a,b).id());
            assertEquals(b.id()>a.id(),a.canUpgradeTo(b));
        }
        assertSame(TrainingWeight.EXTREME,TrainingWeight.highest(TrainingWeight.EXTREME,TrainingWeight.EXTREME));
    }
}
