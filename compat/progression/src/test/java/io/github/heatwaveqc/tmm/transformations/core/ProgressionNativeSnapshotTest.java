package io.github.heatwaveqc.tmm.transformations.core;

import io.github.heatwaveqc.tyr.progression.compat.physiology.NativePhysiologyPolicy.Form;
import io.github.heatwaveqc.tyr.progression.compat.physiology.NativePhysiologyPolicy.Presence;
import io.github.heatwaveqc.tyr.progression.compat.physiology.NativePhysiologyPolicy.Snapshot;
import java.util.EnumMap;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProgressionNativeSnapshotTest {
    private static Snapshot with(Form form, Presence state) {
        var map = new EnumMap<Form, Presence>(Form.class);
        for (Form f : Form.values()) map.put(f, Presence.ABSENT);
        map.put(form, state);
        return new Snapshot(map);
    }

    @Test void mapsAllAbsentProvidersToVerifiedHuman() {
        var before = ProgressionNativeSnapshot.from(Snapshot.allAbsent());
        assertTrue(before.isComplete());
        assertEquals(EffectiveForm.HUMAN, FormResolver.resolve(before));
        for (ProviderState state : ProviderState.values())
            assertEquals(Observation.ABSENT, before.get(state));
    }

    @Test void mapsEachExactDragonSpeciesToSameNativeProvider() {
        for (Form form : new Form[]{Form.CAVE_DRAGON, Form.FOREST_DRAGON, Form.SEA_DRAGON}) {
            var observation = ProgressionNativeSnapshot.from(with(form, Presence.PRESENT));
            assertTrue(observation.isComplete());
            assertEquals(Observation.PRESENT,
                observation.get(ProviderState.DRAGON_SURVIVAL_DRAGON));
            assertEquals(EffectiveForm.DRAGON, FormResolver.resolve(observation));
        }
    }

    @Test void unknownNativeProvidersStayUnknownAndDenyNewAcquisitions() {
        var incomplete = ProgressionNativeSnapshot.from(with(Form.GOETY_LICH, Presence.UNKNOWN));
        assertEquals(Observation.UNKNOWN, incomplete.get(ProviderState.GOETY_LICH));
        assertFalse(incomplete.isComplete());
        var proposed = new EnumMap<ProviderState, Observation>(ProviderState.class);
        proposed.putAll(incomplete.observations());
        proposed.put(ProviderState.JAWR_VAMPIRE, Observation.PRESENT);
        assertFalse(TransitionPolicy.assess(incomplete, new StateSnapshot(proposed),
            TransitionKind.NORMAL).allowed());
    }

    @Test void partialOrConflictingDragonSpeciesFailClosed() {
        var partial = ProgressionNativeSnapshot.from(with(Form.FOREST_DRAGON, Presence.UNKNOWN));
        assertEquals(Observation.UNKNOWN,
            partial.get(ProviderState.DRAGON_SURVIVAL_DRAGON));
        assertFalse(partial.isComplete());

        var invalid = new EnumMap<Form, Presence>(Form.class);
        for (Form form : Form.values()) invalid.put(form, Presence.ABSENT);
        invalid.put(Form.CAVE_DRAGON, Presence.PRESENT);
        invalid.put(Form.FOREST_DRAGON, Presence.PRESENT);
        var conflict = ProgressionNativeSnapshot.from(new Snapshot(invalid));
        assertEquals(Observation.UNKNOWN,
            conflict.get(ProviderState.DRAGON_SURVIVAL_DRAGON));
        assertEquals(EffectiveForm.UNVERIFIED, FormResolver.resolve(conflict));
    }

    @Test void archlichAndDracolichRemainRecognized() {
        var map = new EnumMap<Form, Presence>(Form.class);
        for (Form form : Form.values()) map.put(form, Presence.ABSENT);
        map.put(Form.GOETY_LICH, Presence.PRESENT);
        map.put(Form.FORBIDDEN_MAGIC_LICH, Presence.PRESENT);
        assertEquals(EffectiveForm.ARCHLICH,
            FormResolver.resolve(ProgressionNativeSnapshot.from(new Snapshot(map))));

        map.put(Form.GOETY_LICH, Presence.ABSENT);
        map.put(Form.CAVE_DRAGON, Presence.PRESENT);
        assertEquals(EffectiveForm.DRACOLICH,
            FormResolver.resolve(ProgressionNativeSnapshot.from(new Snapshot(map))));
    }
}
