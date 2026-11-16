package io.github.heatwaveqc.tmm.transformations.core;

import io.github.heatwaveqc.tmm.transformations.adapters.ProbeRegistry;
import java.util.EnumMap;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

/**
 * Converts the existing advisory TransitionPolicy into an authoritative preflight at
 * each native acquisition setter. Existing forms may continue their native rank
 * progression, and cure/reset calls must never be intercepted by this helper.
 *
 * This gate covers NORMAL acquisitions only. Dedicated resurrection, dracolich and
 * future Abomination-rite transitions have their own policies and entrypoints.
 */
public final class NativeSpeciesJoinGuard {
    private static final String NEWBIE_TAG = "tmm_newbie";

    private NativeSpeciesJoinGuard() {}

    public static boolean permit(ServerPlayer player, ProviderState acquiring, String label) {
        StateSnapshot before = ProbeRegistry.observe(player);
        // Only check first acquisition. An existing transformed character migrating
        // into Newbie Mode should not lose their native upgrade/cure mechanics.
        if (before.present(acquiring)) return true;

        if (player.getTags().contains(NEWBIE_TAG)) {
            player.sendSystemMessage(Component.literal(
                    label + " is unavailable in Newbie Mode. Complete the Introduction graduation quest first."));
            return false;
        }

        if (!before.isComplete()) {
            player.sendSystemMessage(Component.literal(
                    "Cannot verify existing transformations: " + label + " acquisition blocked to prevent conflicting forms."));
            return false;
        }

        EnumMap<ProviderState, Observation> proposed =
                new EnumMap<>(ProviderState.class);
        proposed.putAll(before.observations());
        proposed.put(acquiring, Observation.PRESENT);
        TransitionDecision decision = TransitionPolicy.assess(
                before, new StateSnapshot(proposed), TransitionKind.NORMAL);
        if (!decision.allowed()) {
            player.sendSystemMessage(Component.literal(
                    "Cannot become " + label + ": " + decision.reason()));
            return false;
        }
        return true;
    }
}
