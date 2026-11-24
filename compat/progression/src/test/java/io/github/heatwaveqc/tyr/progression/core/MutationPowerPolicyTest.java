package io.github.heatwaveqc.tyr.progression.core;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
class MutationPowerPolicyTest {
    @Test void snapshotsAreBoundedAndValidated(){
        var values=Map.of("minecraft:speed",2,"minecraft:regeneration",1);
        assertEquals(values,MutationPowerPolicy.decode(MutationPowerPolicy.encode(values)));
        assertThrows(IllegalArgumentException.class,()->MutationPowerPolicy.decode("mod:capability=1"));
        assertThrows(IllegalArgumentException.class,()->MutationPowerPolicy.decode("minecraft:speed=10"));
        assertThrows(IllegalArgumentException.class,()->MutationPowerPolicy.decode("minecraft:speed=1\nminecraft:speed=2"));
        assertFalse(MutationPowerPolicy.supported("minecraft:absorption"));
    }
    @Test void temporaryEffectsNeverStackWithSameMutation(){
        assertEquals(3,MutationPowerPolicy.strongest(1,3));
        assertEquals(2,MutationPowerPolicy.strongest(2,0));
        assertEquals(4,MutationPowerPolicy.supplemental(6,2,false),1e-9);
        assertEquals(1.6,(1+0.2)*(1+MutationPowerPolicy.supplemental(.6,.2,true)),1e-9);
        assertEquals(50,MutationPowerPolicy.regenerationInterval(0));
        assertEquals(25,MutationPowerPolicy.regenerationInterval(1));
        assertEquals(1,MutationPowerPolicy.regenerationInterval(9));
    }
}
