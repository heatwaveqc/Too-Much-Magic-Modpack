package io.github.heatwaveqc.tmm.transformations.core;

import static io.github.heatwaveqc.tmm.transformations.core.ProviderState.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.EnumMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class FormResolverTest {
    private static StateSnapshot observed(ProviderState... present) {
        var map = new EnumMap<ProviderState, Observation>(ProviderState.class);
        for (var state : ProviderState.values()) map.put(state, Observation.ABSENT);
        for (var state : present) map.put(state, Observation.PRESENT);
        return new StateSnapshot(map);
    }
    @Test void missingProvidersAreUnknownNotHuman() {
        assertEquals(EffectiveForm.UNVERIFIED, FormResolver.resolve(new StateSnapshot(Map.of())));
        assertEquals(EffectiveForm.UNVERIFIED, FormResolver.resolve(null));
    }
    @Test void nativeFormsAndLichCombinations() {
        assertEquals(EffectiveForm.HUMAN, FormResolver.resolve(observed()));
        assertEquals(EffectiveForm.VAMPIRE, FormResolver.resolve(observed(JAWR_VAMPIRE)));
        assertEquals(EffectiveForm.WEREWOLF, FormResolver.resolve(observed(JAWR_LYCANTHROPE)));
        assertEquals(EffectiveForm.LICH, FormResolver.resolve(observed(GOETY_LICH)));
        assertEquals(EffectiveForm.LICH, FormResolver.resolve(observed(FORBIDDEN_MAGIC_LICH)));
        assertEquals(EffectiveForm.ARCHLICH, FormResolver.resolve(observed(GOETY_LICH,FORBIDDEN_MAGIC_LICH)));
        assertEquals(EffectiveForm.DRAGON, FormResolver.resolve(observed(DRAGON_SURVIVAL_DRAGON)));
        assertEquals(EffectiveForm.DRACOLICH, FormResolver.resolve(observed(DRAGON_SURVIVAL_DRAGON,GOETY_LICH)));
        assertEquals(EffectiveForm.DRACOLICH, FormResolver.resolve(observed(DRAGON_SURVIVAL_DRAGON,FORBIDDEN_MAGIC_LICH)));
        assertEquals(EffectiveForm.ABOMINATION, FormResolver.resolve(observed(
                JAWR_VAMPIRE,JAWR_LYCANTHROPE,GOETY_LICH,FORBIDDEN_MAGIC_LICH)));
    }
    @Test void conflictsFailClosed() {
        assertEquals(EffectiveForm.CONFLICT, FormResolver.resolve(observed(JAWR_VAMPIRE,JAWR_LYCANTHROPE)));
        assertEquals(EffectiveForm.CONFLICT, FormResolver.resolve(observed(JAWR_VAMPIRE,GOETY_LICH)));
        assertEquals(EffectiveForm.CONFLICT, FormResolver.resolve(observed(DRAGON_SURVIVAL_DRAGON,GOETY_LICH,FORBIDDEN_MAGIC_LICH)));
        assertEquals(EffectiveForm.CONFLICT, FormResolver.resolve(observed(DRAGON_SURVIVAL_DRAGON,JAWR_VAMPIRE)));
    }
}
