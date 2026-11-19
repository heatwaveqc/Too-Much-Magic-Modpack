package io.github.heatwaveqc.tmm.progression.runtime;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Pure finite eligibility catalogue. No Minecraft classes needed by unit tests.
 * Runtime authority is supplied separately by live Progression/Transformations.
 */
public final class BountifulDecreeSealPolicy {
    private BountifulDecreeSealPolicy() {}

    private static final Map<String, Set<String>> NATIVE_STATES = Map.of(
        "vampire", Set.of("JAWR_VAMPIRE"),
        "werewolf", Set.of("JAWR_LYCANTHROPE"),
        "lich_goety", Set.of("GOETY_LICH"),
        "lich_jawr", Set.of("FORBIDDEN_MAGIC_LICH"),
        "archlich", Set.of("GOETY_LICH", "FORBIDDEN_MAGIC_LICH"),
        "abomination", Set.of("JAWR_VAMPIRE", "JAWR_LYCANTHROPE",
                              "GOETY_LICH", "FORBIDDEN_MAGIC_LICH")
    );
    private static final Map<String, String> CLASS_PATHS = Map.of(
        "hunter", "tmm_progression:hunter",
        "monk", "tmm_progression:monk"
    );

    public static Set<String> supportedIds() {
        HashSet<String> out = new HashSet<>(NATIVE_STATES.keySet());
        out.addAll(CLASS_PATHS.keySet());
        return Set.copyOf(out);
    }

    public static boolean declared(String family) {
        return NATIVE_STATES.containsKey(family) || CLASS_PATHS.containsKey(family);
    }

    public static Set<String> nativeStates(String family) {
        return NATIVE_STATES.get(family);
    }

    public static String classPath(String family) {
        return CLASS_PATHS.get(family);
    }
}
