package io.github.heatwaveqc.tyr.progression;

import io.github.heatwaveqc.tyr.progression.model.Definition;
import io.github.heatwaveqc.tyr.progression.runtime.taming.TamePolicy;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

final class TamePolicyTest {
    private static Definition.Effect effect(String target,Map<String,String> params){
        return new Definition.Effect("tame","","add_value",target,"","",0,0,false,false,params);
    }
    @Test void persistentOwnershipAndWaitWithoutTransfer(){
        UUID owner=UUID.randomUUID(),mob=UUID.randomUUID(),other=UUID.randomUUID();
        var s=new TamePolicy.State(owner,mob,false);
        assertEquals(s,TamePolicy.decode(s.encode(),mob).orElseThrow());
        var wait=s.toggle();
        assertTrue(wait.waiting());assertEquals(owner,wait.owner());assertEquals(mob,wait.entity());
        assertEquals(wait,TamePolicy.decode(wait.encode(),mob).orElseThrow());
        assertFalse(wait.belongsTo(other));assertTrue(wait.belongsTo(owner));
        assertTrue(TamePolicy.decode(s.encode(),other).isEmpty());
        assertTrue(TamePolicy.decode("corrupt",mob).isEmpty());
    }
    @Test void explicitEligibilityAndCost(){
        assertTrue(TamePolicy.supported("minecraft:pillager"));
        assertTrue(TamePolicy.supported("minecraft:vindicator"));
        assertFalse(TamePolicy.supported("minecraft:evoker"));
        assertFalse(TamePolicy.supported("minecraft:player"));
        assertFalse(TamePolicy.supported("mod:elemental"));
        assertEquals(0,TamePolicy.validate(effect("minecraft:pillager",Map.of())).cost());
        assertEquals(100,TamePolicy.validate(effect("#example:humanoids",Map.of("resource","example:mana","resource_cost","100"))).cost());
        assertThrows(IllegalArgumentException.class,()->TamePolicy.validate(effect("*",Map.of())));
        assertThrows(IllegalArgumentException.class,()->TamePolicy.validate(effect("minecraft:pillager",Map.of("resource_cost","5"))));
        assertThrows(IllegalArgumentException.class,()->TamePolicy.validate(effect("minecraft:pillager",Map.of("max_companions","2"))));
    }
    @Test void defenseRequiresRecentThreatAndNeverProtectedTargets(){
        assertTrue(TamePolicy.threatFresh(100,300));
        assertFalse(TamePolicy.threatFresh(300,300));
        assertFalse(TamePolicy.threatFresh(99,300));
        assertTrue(TamePolicy.combatAllowed(false,true,true,false,true));
        assertFalse(TamePolicy.combatAllowed(true,true,true,false,true));
        assertFalse(TamePolicy.combatAllowed(false,false,true,false,true));
        assertFalse(TamePolicy.combatAllowed(false,true,true,true,true));
        assertFalse(TamePolicy.combatAllowed(false,true,true,false,false));
    }
}
