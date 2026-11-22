package io.github.heatwaveqc.tyr.progression.compat.physiology;

import static io.github.heatwaveqc.tyr.progression.compat.physiology.NativePhysiologyPolicy.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

/** Pure tests: neither optional mods nor a Minecraft runtime are required. */
class NativePhysiologyPolicyTest {
    private static final Rules RULES = NativePhysiologyGateway.withApprovedPairData(
        Map.of(
            "tyr_progression:dryad", Set.of(Form.FOREST_DRAGON),
            "tyr_progression:sexy_demon", Set.of(Form.CAVE_DRAGON),
            "tyr_progression:spawn", Set.of(Form.SEA_DRAGON)
        )
    );

    private static Snapshot snapshot(Form present) {
        EnumMap<Form, Presence> states = new EnumMap<>(Form.class);
        for (Form form : Form.values()) states.put(form, Presence.ABSENT);
        states.put(present, Presence.PRESENT);
        return new Snapshot(states);
    }

    @Test void universalCoreIsNotASecondSpecialist() {
        assertEquals(Decision.ALLOW, mayJoin(false, "tyr_progression:core",
            snapshot(Form.SEA_DRAGON), RULES));
        assertEquals(Decision.ALLOW, mayAcquire(false,
            Set.of("tyr_progression:core"), Snapshot.allAbsent(),
            Form.JAWR_VAMPIRE, RULES));
    }

    @Test void ordinaryGraduatedPlayersMayAcquireAForm() {
        assertEquals(Decision.ALLOW, mayAcquire(false, Set.of("tyr_progression:normie"),
            Snapshot.allAbsent(), Form.CAVE_DRAGON, RULES));
        assertEquals(Decision.ALLOW, mayJoin(false, "tyr_progression:monk",
            Snapshot.allAbsent(), RULES));
    }

    @Test void bothAcquisitionOrdersWorkForAllThreeApprovedPairs() {
        for (var approved : Map.of(
            "tyr_progression:dryad", Form.FOREST_DRAGON,
            "tyr_progression:sexy_demon", Form.CAVE_DRAGON,
            "tyr_progression:spawn", Form.SEA_DRAGON).entrySet()) {
            assertEquals(Decision.ALLOW, mayAcquire(false,
                Set.of("tyr_progression:normie", approved.getKey()),
                Snapshot.allAbsent(), approved.getValue(), RULES));
            assertEquals(Decision.ALLOW, mayJoin(false, approved.getKey(),
                snapshot(approved.getValue()), RULES));
        }
    }

    @Test void disallowUnapprovedCrossSpeciesCombinations() {
        assertEquals(Decision.DENY_INCOMPATIBLE,
            mayAcquire(false, Set.of("tyr_progression:dryad"), Snapshot.allAbsent(),
                Form.CAVE_DRAGON, RULES));
        assertEquals(Decision.DENY_INCOMPATIBLE, mayJoin(false,
            "tyr_progression:spawn", snapshot(Form.FOREST_DRAGON), RULES));
        assertEquals(Decision.DENY_INCOMPATIBLE, mayAcquire(false,
            Set.of("tyr_progression:monk"), Snapshot.allAbsent(),
            Form.SEA_DRAGON, RULES));
    }

    @Test void baselineNormieIsNotASecondSpecialist() {
        assertEquals(Decision.ALLOW, mayJoin(false, "tyr_progression:normie",
            snapshot(Form.JAWR_VAMPIRE), RULES));
        assertEquals(Decision.ALLOW, mayAcquire(false, Set.of("tyr_progression:normie"),
            Snapshot.allAbsent(), Form.JAWR_VAMPIRE, RULES));
    }

    @Test void newcomerAndUnknownNativeStatesFailClosed() {
        assertEquals(Decision.DENY_NEWBIE,
            mayAcquire(true, Set.of("tyr_progression:newbie"),
                Snapshot.allAbsent(), Form.CAVE_DRAGON, RULES));
        assertEquals(Decision.DENY_NEWBIE,
            mayJoin(true, "tyr_progression:monk", Snapshot.allAbsent(), RULES));
        EnumMap<Form, Presence> incomplete = new EnumMap<>(Form.class);
        for (Form form : Form.values()) incomplete.put(form, Presence.ABSENT);
        incomplete.put(Form.GOETY_LICH, Presence.UNKNOWN);
        Snapshot unknown = new Snapshot(incomplete);
        assertEquals(Decision.DENY_UNVERIFIED,
            mayAcquire(false, Set.of(), unknown, Form.SEA_DRAGON, RULES));
        assertEquals(Decision.DENY_UNVERIFIED,
            mayJoin(false, "tyr_progression:dryad", unknown, RULES));
    }

    @Test void nativeLevelUpsAreNotReclassifiedAsInitiations() {
        assertEquals(Decision.ALLOW,
            mayAcquire(true, Set.of("tyr_progression:monk"),
                snapshot(Form.JAWR_VAMPIRE), Form.JAWR_VAMPIRE, RULES));
    }

    @Test void multipleSpecialistsMustEachPermitTheForm() {
        assertEquals(Decision.DENY_INCOMPATIBLE, mayAcquire(false,
            Set.of("tyr_progression:dryad", "tyr_progression:spawn"),
            Snapshot.allAbsent(), Form.FOREST_DRAGON, RULES));
    }

    @Test void rulesAndSnapshotsAreDefensivelyImmutable() {
        var mutable = new java.util.HashMap<String, Set<Form>>();
        mutable.put("tyr_progression:dryad", Set.of(Form.FOREST_DRAGON));
        Rules rules = new Rules(Set.of("tyr_progression:normie"), mutable);
        mutable.clear();
        assertTrue(rules.permits("tyr_progression:dryad", Form.FOREST_DRAGON));
        assertThrows(IllegalArgumentException.class, () ->
            new Snapshot(Map.of(Form.CAVE_DRAGON, Presence.ABSENT)));
        assertThrows(IllegalArgumentException.class, () ->
            new Rules(Set.of("tyr_progression:newbie"),
                Map.of("tyr_progression:newbie", Set.of(Form.CAVE_DRAGON))));
    }
}
