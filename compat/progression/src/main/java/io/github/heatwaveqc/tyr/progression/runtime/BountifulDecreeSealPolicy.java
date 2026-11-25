package io.github.heatwaveqc.tyr.progression.runtime;

import io.github.heatwaveqc.tmm.transformations.core.ProviderState;
import io.github.heatwaveqc.tmm.transformations.core.StateSnapshot;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;

/** Reviewed v1 compiler profiles only; native state is supplied by the shared engine. */
public final class BountifulDecreeSealPolicy {
    private BountifulDecreeSealPolicy() {}

    private static final Map<String, Set<ProviderState>> NATIVE_STATES = Map.of(
        "vampire", Set.of(ProviderState.JAWR_VAMPIRE),
        "werewolf", Set.of(ProviderState.JAWR_LYCANTHROPE),
        "lich_goety", Set.of(ProviderState.GOETY_LICH),
        "lich_jawr", Set.of(ProviderState.FORBIDDEN_MAGIC_LICH),
        "archlich", Set.of(ProviderState.GOETY_LICH, ProviderState.FORBIDDEN_MAGIC_LICH),
        "abomination", Set.of(ProviderState.JAWR_VAMPIRE, ProviderState.JAWR_LYCANTHROPE,
                              ProviderState.GOETY_LICH, ProviderState.FORBIDDEN_MAGIC_LICH)
    );
    private static final Map<String, String> CLASS_PATHS = Map.of(
        "hunter", "tyr_progression:hunter",
        "monk", "tyr_progression:monk"
    );

    public static Set<String> supportedIds() {
        HashSet<String> out = new HashSet<>(NATIVE_STATES.keySet());
        out.addAll(CLASS_PATHS.keySet());
        return Set.copyOf(out);
    }

    public static String family(String document) {
        if (document == null || !document.startsWith("tmm_")) return null;
        String family = document.substring(4);
        return NATIVE_STATES.containsKey(family) || CLASS_PATHS.containsKey(family) ? family : null;
    }

    /** Class queries never require unrelated provider probes; native forms fail closed on an incomplete snapshot. */
    public static boolean allowed(String document, Predicate<String> joined, Supplier<StateSnapshot> nativeSnapshot) {
        String family = family(document);
        if (family == null) return false;
        String path = CLASS_PATHS.get(family);
        if (path != null) return joined.test(path);
        StateSnapshot state = nativeSnapshot.get();
        return state != null && state.isComplete()
            && NATIVE_STATES.get(family).stream().allMatch(state::present);
    }
}
