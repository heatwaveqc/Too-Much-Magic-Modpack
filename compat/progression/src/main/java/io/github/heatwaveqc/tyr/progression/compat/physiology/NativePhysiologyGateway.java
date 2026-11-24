package io.github.heatwaveqc.tyr.progression.compat.physiology;

import io.github.heatwaveqc.tyr.progression.compat.physiology.NativePhysiologyPolicy.Decision;
import io.github.heatwaveqc.tyr.progression.compat.physiology.NativePhysiologyPolicy.Form;
import io.github.heatwaveqc.tyr.progression.compat.physiology.NativePhysiologyPolicy.Rules;
import io.github.heatwaveqc.tyr.progression.runtime.NewbieMode;
import io.github.heatwaveqc.tyr.progression.state.ProgressionAttachments;
import io.github.heatwaveqc.tyr.progression.state.Track;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.server.level.ServerPlayer;

/**
 * Server-facing bridge from player Path state to pure native-form compatibility.
 *
 * This is deliberately NOT registered as a second admission handler while the
 * legacy Transformations JAR still owns native setter mixins. The migration
 * writer will install one unified callback at both native acquisition and Path
 * enrollment after the configurable compatibility matrix is available.
 */
public final class NativePhysiologyGateway {
    private NativePhysiologyGateway() {}

    /** Source of truth is the Progression ledger, not anyJoined or a name tag. */
    public static Set<String> joinedPaths(ServerPlayer player) {
        return player.getData(ProgressionAttachments.LEDGER.get()).paths()
            .entrySet().stream()
            .filter(entry -> entry.getValue() != null && entry.getValue().joined())
            .map(Map.Entry::getKey)
            .collect(Collectors.toUnmodifiableSet());
    }

    public static boolean hasSpecialization(ServerPlayer player, Rules rules) {
        return joinedPaths(player).stream().anyMatch(path -> !rules.isBaseline(path));
    }

    public static Decision mayJoin(ServerPlayer player, String requested, Rules rules) {
        return NativePhysiologyPolicy.mayJoin(NewbieMode.isNewbie(player), requested,
            NativePhysiologyObservations.observe(player), rules);
    }

    public static Decision mayAcquire(ServerPlayer player, Form form, Rules rules) {
        return NativePhysiologyPolicy.mayAcquire(NewbieMode.isNewbie(player),
            joinedPaths(player), NativePhysiologyObservations.observe(player), form, rules);
    }

    /** Baseline Newbie/Normie and universal Core are never specialist admission vetoes. */
    public static Rules withApprovedPairData(Map<String, Set<Form>> approvedPairs) {
        return new Rules(Set.of("tyr_progression:newbie", "tyr_progression:normie", "tyr_progression:core"),
            approvedPairs);
    }
}
