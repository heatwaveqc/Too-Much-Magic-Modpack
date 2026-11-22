package io.github.heatwaveqc.tmm.transformations.core;

import static io.github.heatwaveqc.tmm.transformations.core.ProviderState.*;
import static io.github.heatwaveqc.tmm.transformations.core.TransitionKind.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.EnumMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class TransitionPolicyTest {
    private static StateSnapshot states(ProviderState... present) {
        var map = new EnumMap<ProviderState, Observation>(ProviderState.class);
        for (var state : ProviderState.values()) map.put(state, Observation.ABSENT);
        for (var state : present) map.put(state, Observation.PRESENT);
        return new StateSnapshot(map);
    }
    @Test void unknownFailsClosed() {
        assertFalse(TransitionPolicy.assess(states(),new StateSnapshot(Map.of()),NORMAL).allowed());
    }
    @Test void basicAcquisitionAndIncrementalLichdom() {
        assertTrue(TransitionPolicy.assess(states(),states(JAWR_VAMPIRE),NORMAL).allowed());
        assertTrue(TransitionPolicy.assess(states(),states(JAWR_LYCANTHROPE),NORMAL).allowed());
        assertTrue(TransitionPolicy.assess(states(),states(GOETY_LICH),NORMAL).allowed());
        assertTrue(TransitionPolicy.assess(states(),states(DRAGON_SURVIVAL_DRAGON),NORMAL).allowed());
        assertTrue(TransitionPolicy.assess(states(GOETY_LICH),states(GOETY_LICH,FORBIDDEN_MAGIC_LICH),NORMAL).allowed());
        assertFalse(TransitionPolicy.assess(states(),states(GOETY_LICH,FORBIDDEN_MAGIC_LICH),NORMAL).allowed());
    }
    @Test void curesDoNotAddState() {
        assertTrue(TransitionPolicy.assess(states(GOETY_LICH,FORBIDDEN_MAGIC_LICH),states(GOETY_LICH),CONTROLLED_CURE).allowed());
        assertFalse(TransitionPolicy.assess(states(GOETY_LICH),states(GOETY_LICH,FORBIDDEN_MAGIC_LICH),CONTROLLED_CURE).allowed());
    }
    @Test void ritualPreflightRequiresArchlichAndBothJawrStates() {
        var arch = states(GOETY_LICH,FORBIDDEN_MAGIC_LICH);
        var abom = states(GOETY_LICH,FORBIDDEN_MAGIC_LICH,JAWR_VAMPIRE,JAWR_LYCANTHROPE);
        assertTrue(TransitionPolicy.assess(arch,abom,ABOMINATION_RITUAL).allowed());
        assertFalse(TransitionPolicy.assess(arch,states(GOETY_LICH,FORBIDDEN_MAGIC_LICH,JAWR_VAMPIRE),ABOMINATION_RITUAL).allowed());
        assertFalse(TransitionPolicy.assess(states(),abom,ABOMINATION_RITUAL).allowed());
    }
    @Test void noTransitionEverApprovesAConflict() {
        var providers=ProviderState.values();
        for (int mask=0;mask<(1<<providers.length);mask++) {
            var map=new EnumMap<ProviderState,Observation>(ProviderState.class);
            for (int i=0;i<providers.length;i++)
                map.put(providers[i],(mask&(1<<i))!=0?Observation.PRESENT:Observation.ABSENT);
            var after=new StateSnapshot(map);
            for (var kind:TransitionKind.values()) {
                var decision=TransitionPolicy.assess(states(),after,kind);
                if (decision.allowed()) {
                    assertNotEquals(EffectiveForm.CONFLICT,decision.after());
                    assertNotEquals(EffectiveForm.UNVERIFIED,decision.after());
                }
            }
        }
    }
}
