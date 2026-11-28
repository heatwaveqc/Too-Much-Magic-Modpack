package io.github.heatwaveqc.tyr.progression;

import io.github.heatwaveqc.tyr.progression.foundation.MagicSkillMath;
import io.github.heatwaveqc.tyr.progression.data.MechanicalSpec;
import io.github.heatwaveqc.tyr.progression.model.Definition;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class MagicSkillMathTest {
    @Test void exactDoublingAndAggregateBounds(){
        assertEquals(200d,MagicSkillMath.increase(100,1,1));
        assertEquals(200d,MagicSkillMath.increase(100,1.5,1));
        assertEquals(100d,MagicSkillMath.increase(100,0,1));
        assertEquals(-5d,MagicSkillMath.increase(-5,1,1));
        assertEquals(0d,MagicSkillMath.increase(0,1,1));
        assertEquals(0d,MagicSkillMath.cap(Double.NaN,1));
    }
    @Test void finitePositivePricesAndTimers(){
        assertEquals(7,MagicSkillMath.reduce(10,.3,.95));
        assertEquals(5,MagicSkillMath.reduce(100,1,.95));
        assertEquals(1,MagicSkillMath.reduce(100,1,1));
        assertEquals(1,MagicSkillMath.reduce(1,.5,1));
        assertEquals(0,MagicSkillMath.reduce(0,1,1));
        assertEquals(-1,MagicSkillMath.reduce(-1,1,1));
        assertEquals(500,MagicSkillMath.reduce(1000,.5,1));
        assertEquals(Long.MAX_VALUE,MagicSkillMath.reduce(Long.MAX_VALUE,0,1));
    }
    private Definition.Effect effect(String type,String target,double value){
        return new Definition.Effect(type,"","add_value",target,"","",value,0,false,false,Map.of());
    }
    @Test void modifiersRequireExactScope(){
        for(String type:new String[]{"magic_resource_capacity","magic_resource_recovery","magic_potency","magic_cast_time_reduction","magic_cooldown_reduction"}){
            assertTrue(MechanicalSpec.known(type));
            assertDoesNotThrow(()->MechanicalSpec.validate(effect(type,"example:active_spell",.2)));
            assertThrows(IllegalArgumentException.class,()->MechanicalSpec.validate(effect(type,"*",.2)));
            assertThrows(IllegalArgumentException.class,()->MechanicalSpec.validate(effect(type,"#example:all",.2)));
            assertThrows(IllegalArgumentException.class,()->MechanicalSpec.validate(effect(type,"example:active_spell",1.01)));
        }
    }
}
