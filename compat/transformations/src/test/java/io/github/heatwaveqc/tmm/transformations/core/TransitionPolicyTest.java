package io.github.heatwaveqc.tmm.transformations.core;

import static io.github.heatwaveqc.tmm.transformations.core.ProviderState.*;
import static io.github.heatwaveqc.tmm.transformations.core.TransitionKind.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.EnumMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class TransitionPolicyTest {
    private static StateSnapshot states(ProviderState... active) {
        var map = new EnumMap<ProviderState, Observation>(ProviderState.class);
        for (var p : ProviderState.values()) map.put(p, Observation.ABSENT);
        for (var p : active) map.put(p, Observation.PRESENT);
        return new StateSnapshot(map);
    }

    @Test void incompleteObservationsFailClosed() {
        assertFalse(TransitionPolicy.assess(states(), new StateSnapshot(Map.of()), NORMAL).allowed());
        assertFalse(TransitionPolicy.assess(new StateSnapshot(Map.of()), states(), NORMAL).allowed());
    }

    @Test void normalFirstStepsAndIncrementalProgression() {
        var human = states();
        assertTrue(TransitionPolicy.assess(human, states(VAMPIRISM_VAMPIRE), NORMAL).allowed());
        assertTrue(TransitionPolicy.assess(human, states(VAMPIRISM_HUNTER), NORMAL).allowed());
        assertTrue(TransitionPolicy.assess(human, states(GOETY_LICH), NORMAL).allowed());
        assertTrue(TransitionPolicy.assess(human, states(DRAGON_SURVIVAL_DRAGON), NORMAL).allowed());
        assertTrue(TransitionPolicy.assess(states(VAMPIRISM_VAMPIRE),
                states(VAMPIRISM_VAMPIRE, JAWR_VAMPIRE), NORMAL).allowed());
        assertTrue(TransitionPolicy.assess(states(GOETY_LICH),
                states(GOETY_LICH, FORBIDDEN_MAGIC_LICH), NORMAL).allowed());
        assertTrue(TransitionPolicy.assess(states(DRAGON_SURVIVAL_DRAGON),
                states(DRAGON_SURVIVAL_DRAGON, GOETY_LICH), NORMAL).allowed());
    }

    @Test void ordinaryTransitionsCannotJumpOverMilestonesOrRemoveStates() {
        assertFalse(TransitionPolicy.assess(states(), states(GOETY_LICH, FORBIDDEN_MAGIC_LICH), NORMAL).allowed());
        assertFalse(TransitionPolicy.assess(states(GOETY_LICH), states(), NORMAL).allowed());
        assertFalse(TransitionPolicy.assess(states(VAMPIRISM_HUNTER), states(JAWR_VAMPIRE), NORMAL).allowed());
        assertFalse(TransitionPolicy.assess(states(GOETY_LICH, FORBIDDEN_MAGIC_LICH),
                states(VAMPIRISM_VAMPIRE, WEREWOLVES_WEREWOLF, GOETY_LICH, FORBIDDEN_MAGIC_LICH), NORMAL).allowed());
    }

    @Test void hunterReplacementExplicitAndAtomic() {
        var hunter = states(VAMPIRISM_HUNTER);
        assertTrue(TransitionPolicy.assess(hunter, states(VAMPIRISM_VAMPIRE), VOLUNTARY_HUNTER_REPLACEMENT).allowed());
        assertFalse(TransitionPolicy.assess(hunter, states(VAMPIRISM_HUNTER, VAMPIRISM_VAMPIRE), VOLUNTARY_HUNTER_REPLACEMENT).allowed());
        assertFalse(TransitionPolicy.assess(states(), states(VAMPIRISM_VAMPIRE), VOLUNTARY_HUNTER_REPLACEMENT).allowed());
    }

    @Test void abominationMustBeAtomicFromArchlich() {
        var archlich = states(GOETY_LICH, FORBIDDEN_MAGIC_LICH);
        var abomination = states(GOETY_LICH, FORBIDDEN_MAGIC_LICH, JAWR_VAMPIRE, JAWR_LYCANTHROPE);
        assertTrue(TransitionPolicy.assess(archlich, abomination, ABOMINATION_RITUAL).allowed());
        assertFalse(TransitionPolicy.assess(archlich,
                states(GOETY_LICH, FORBIDDEN_MAGIC_LICH, JAWR_VAMPIRE), ABOMINATION_RITUAL).allowed());
        assertFalse(TransitionPolicy.assess(states(), abomination, ABOMINATION_RITUAL).allowed());
    }

    @Test void curesCannotCreateNewStates() {
        assertTrue(TransitionPolicy.assess(states(GOETY_LICH, FORBIDDEN_MAGIC_LICH),
                states(GOETY_LICH), CONTROLLED_CURE).allowed());
        assertFalse(TransitionPolicy.assess(states(GOETY_LICH),
                states(GOETY_LICH, FORBIDDEN_MAGIC_LICH), CONTROLLED_CURE).allowed());
    }

    @Test void exhaustiveCombinationsNeverAllowAnInvalidResult() {
        var providers = ProviderState.values();
        for (int mask = 0; mask < (1 << providers.length); mask++) {
            var all = new EnumMap<ProviderState, Observation>(ProviderState.class);
            for (int i = 0; i < providers.length; i++) {
                all.put(providers[i], (mask & (1 << i)) != 0 ? Observation.PRESENT : Observation.ABSENT);
            }
            var after = new StateSnapshot(all);
            for (var kind : TransitionKind.values()) {
                var decision = TransitionPolicy.assess(states(), after, kind);
                if (decision.allowed()) {
                    assertNotEquals(EffectiveForm.CONFLICT, decision.after(), "mask=" + mask);
                    assertNotEquals(EffectiveForm.UNVERIFIED, decision.after(), "mask=" + mask);
                }
            }
        }
    }
}
