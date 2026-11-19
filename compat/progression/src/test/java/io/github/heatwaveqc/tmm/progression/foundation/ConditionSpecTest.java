package io.github.heatwaveqc.tmm.progression.foundation;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class ConditionSpecTest {
    @Test void dailyWindowWrapsAndNormalizes(){
        var night=new ConditionSpec.TimeWindow(13000,1000);
        assertTrue(night.contains(23500));assertTrue(night.contains(500));assertTrue(night.contains(24500));
        assertFalse(night.contains(6000));assertTrue(night.contains(-500));
        assertThrows(IllegalArgumentException.class,()->new ConditionSpec.TimeWindow(0,24000));
    }
    @Test void meterRejectsInvalidBounds(){
        assertEquals(.3,ConditionSpec.threshold(.3,0,1));
        assertThrows(IllegalArgumentException.class,()->ConditionSpec.threshold(Double.NaN,0,1));
        assertThrows(IllegalArgumentException.class,()->ConditionSpec.threshold(1.1,0,1));
    }
}
