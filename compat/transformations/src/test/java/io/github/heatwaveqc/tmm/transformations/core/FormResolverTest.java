package io.github.heatwaveqc.tmm.transformations.core;

import static io.github.heatwaveqc.tmm.transformations.core.ProviderState.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.EnumMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class FormResolverTest {
    private static StateSnapshot fullyObserved(ProviderState... present) {
        EnumMap<ProviderState, Observation> map = new EnumMap<>(ProviderState.class);
        for (ProviderState state : ProviderState.values()) {
            map.put(state, Observation.ABSENT);
        }
        for (ProviderState state : present) {
            map.put(state, Observation.PRESENT);
        }
        return new StateSnapshot(map);
    }

    @Test void missingProbesDoNotLookLikeHuman() {
        assertEquals(EffectiveForm.UNVERIFIED, FormResolver.resolve(new StateSnapshot(Map.of())));
    }

    @Test void baseAndUnifiedForms() {
        assertEquals(EffectiveForm.HUMAN, FormResolver.resolve(fullyObserved()));
        assertEquals(EffectiveForm.HUNTER, FormResolver.resolve(fullyObserved(VAMPIRISM_HUNTER)));
        assertEquals(EffectiveForm.VAMPIRE, FormResolver.resolve(fullyObserved(VAMPIRISM_VAMPIRE, JAWR_VAMPIRE)));
        assertEquals(EffectiveForm.WEREWOLF, FormResolver.resolve(fullyObserved(WEREWOLVES_WEREWOLF, JAWR_LYCANTHROPE)));
        assertEquals(EffectiveForm.LICH, FormResolver.resolve(fullyObserved(GOETY_LICH)));
        assertEquals(EffectiveForm.ARCHLICH, FormResolver.resolve(fullyObserved(GOETY_LICH, FORBIDDEN_MAGIC_LICH)));
        assertEquals(EffectiveForm.DRAGON, FormResolver.resolve(fullyObserved(DRAGON_SURVIVAL_DRAGON)));
        assertEquals(EffectiveForm.DRACOLICH, FormResolver.resolve(fullyObserved(DRAGON_SURVIVAL_DRAGON, GOETY_LICH)));
        assertEquals(EffectiveForm.DRACOLICH, FormResolver.resolve(fullyObserved(DRAGON_SURVIVAL_DRAGON, FORBIDDEN_MAGIC_LICH)));
    }

    @Test void abominationIsVampirePlusWerewolfWithoutLichdom() {
        assertEquals(EffectiveForm.ABOMINATION, FormResolver.resolve(fullyObserved(
                VAMPIRISM_VAMPIRE, WEREWOLVES_WEREWOLF)));
        assertEquals(EffectiveForm.ABOMINATION, FormResolver.resolve(fullyObserved(
                JAWR_VAMPIRE, JAWR_LYCANTHROPE)));
        assertEquals(EffectiveForm.ABOMINATION, FormResolver.resolve(fullyObserved(
                VAMPIRISM_VAMPIRE, JAWR_VAMPIRE, WEREWOLVES_WEREWOLF, JAWR_LYCANTHROPE)));
    }

    @Test void illegalCombinationsFailClassification() {
        assertEquals(EffectiveForm.CONFLICT, FormResolver.resolve(fullyObserved(VAMPIRISM_HUNTER, GOETY_LICH)));
        assertEquals(EffectiveForm.CONFLICT, FormResolver.resolve(fullyObserved(DRAGON_SURVIVAL_DRAGON, GOETY_LICH, FORBIDDEN_MAGIC_LICH)));
        assertEquals(EffectiveForm.CONFLICT, FormResolver.resolve(fullyObserved(VAMPIRISM_VAMPIRE, WEREWOLVES_WEREWOLF, GOETY_LICH)));
        assertEquals(EffectiveForm.CONFLICT, FormResolver.resolve(fullyObserved(
                VAMPIRISM_VAMPIRE, WEREWOLVES_WEREWOLF, GOETY_LICH, FORBIDDEN_MAGIC_LICH)));
        assertEquals(EffectiveForm.CONFLICT, FormResolver.resolve(fullyObserved(JAWR_VAMPIRE, FORBIDDEN_MAGIC_LICH)));
    }
}
