package io.github.heatwaveqc.tyr.progression.runtime;

import io.github.heatwaveqc.tmm.transformations.core.Observation;
import io.github.heatwaveqc.tmm.transformations.core.ProviderState;
import io.github.heatwaveqc.tmm.transformations.core.StateSnapshot;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BountifulDecreeSealsTest {
    private static StateSnapshot observed(ProviderState... present) {
        var states = new EnumMap<ProviderState, Observation>(ProviderState.class);
        for (var state : ProviderState.values()) states.put(state, Observation.ABSENT);
        for (var state : present) states.put(state, Observation.PRESENT);
        return new StateSnapshot(states);
    }

    @Test void finiteExplicitRosterOnly() {
        assertEquals(Set.of("vampire","werewolf","lich_goety","lich_jawr",
            "archlich","abomination","hunter","monk"), BountifulDecreeSealPolicy.supportedIds());
        for (String excluded : Set.of("tmm_dragon_cave","tmm_dragon_forest","tmm_dragon_sea",
                "tmm_dracolich","tmm_gilded","tmm_hunting","vampire","tmm_tmm_vampire","tmm_unknown","")) {
            assertNull(BountifulDecreeSealPolicy.family(excluded));
            assertFalse(BountifulDecreeSealPolicy.allowed(excluded, p -> true, () -> { throw new AssertionError(); }));
        }
        assertNull(BountifulDecreeSealPolicy.family(null));
    }

    @Test void classChecksUseCanonicalPathWithoutNativeProbe() {
        assertTrue(BountifulDecreeSealPolicy.allowed("tmm_hunter", "tyr_progression:hunter"::equals,
            () -> { throw new AssertionError("unrelated native probe"); }));
        assertTrue(BountifulDecreeSealPolicy.allowed("tmm_monk", "tyr_progression:monk"::equals,
            () -> { throw new AssertionError("unrelated native probe"); }));
        assertFalse(BountifulDecreeSealPolicy.allowed("tmm_hunter", "tyr_progression:monk"::equals, () -> null));
        assertFalse(BountifulDecreeSealPolicy.allowed("tmm_monk", "tmm_progression:monk"::equals, () -> null));
    }

    @Test void nativeEligibilityRequiresCompleteCurrentObservation() {
        var vampire = observed(ProviderState.JAWR_VAMPIRE);
        assertTrue(BountifulDecreeSealPolicy.allowed("tmm_vampire", p -> false, () -> vampire));
        assertFalse(BountifulDecreeSealPolicy.allowed("tmm_werewolf", p -> true, () -> vampire));
        assertFalse(BountifulDecreeSealPolicy.allowed("tmm_vampire", p -> true,
            () -> new StateSnapshot(Map.of(ProviderState.JAWR_VAMPIRE, Observation.PRESENT))));
        assertFalse(BountifulDecreeSealPolicy.allowed("tmm_vampire", p -> true, () -> null));
        assertFalse(BountifulDecreeSealPolicy.allowed("tmm_vampire", p -> true, () -> observed()));
    }

    @Test void compositeRequiresEveryProviderAndInheritsIndividualSeals() {
        var lich = observed(ProviderState.GOETY_LICH, ProviderState.FORBIDDEN_MAGIC_LICH);
        assertTrue(BountifulDecreeSealPolicy.allowed("tmm_archlich", p -> false, () -> lich));
        assertTrue(BountifulDecreeSealPolicy.allowed("tmm_lich_goety", p -> false, () -> lich));
        assertTrue(BountifulDecreeSealPolicy.allowed("tmm_lich_jawr", p -> false, () -> lich));
        assertFalse(BountifulDecreeSealPolicy.allowed("tmm_archlich", p -> true,
            () -> observed(ProviderState.GOETY_LICH)));
        assertFalse(BountifulDecreeSealPolicy.allowed("tmm_abomination", p -> true, () -> lich));
        var all = observed(ProviderState.GOETY_LICH, ProviderState.FORBIDDEN_MAGIC_LICH,
            ProviderState.JAWR_VAMPIRE, ProviderState.JAWR_LYCANTHROPE);
        for (String family : Set.of("vampire","werewolf","lich_goety","lich_jawr","archlich","abomination"))
            assertTrue(BountifulDecreeSealPolicy.allowed("tmm_"+family, p -> false, () -> all));
    }
}
