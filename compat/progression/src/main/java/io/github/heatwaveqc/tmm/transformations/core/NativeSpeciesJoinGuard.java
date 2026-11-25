package io.github.heatwaveqc.tmm.transformations.core;

import io.github.heatwaveqc.tyr.progression.compat.physiology.NativePhysiologyGateway;
import io.github.heatwaveqc.tyr.progression.compat.physiology.NativePhysiologyObservations;
import io.github.heatwaveqc.tyr.progression.compat.physiology.NativePhysiologyPolicy.Decision;
import io.github.heatwaveqc.tyr.progression.compat.physiology.NativePhysiologyPolicy.Form;
import io.github.heatwaveqc.tyr.progression.data.PathRegistry;
import java.util.EnumMap;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

/**
 * One native acquisition boundary backed by Progression's Path/form policy.
 * No reflection into the retired Progression mod ID and no blanket specialist veto.
 * Native providers still own costs, cures, ranks and actual attachment writes.
 */
public final class NativeSpeciesJoinGuard {
    private NativeSpeciesJoinGuard() {}

    public static boolean permit(ServerPlayer player, ProviderState provider, String label) {
        Form form = switch (provider) {
            case JAWR_VAMPIRE -> Form.JAWR_VAMPIRE;
            case JAWR_LYCANTHROPE -> Form.JAWR_WEREWOLF;
            case GOETY_LICH -> Form.GOETY_LICH;
            case FORBIDDEN_MAGIC_LICH -> Form.FORBIDDEN_MAGIC_LICH;
            case DRAGON_SURVIVAL_DRAGON -> null; // species-specific overload required
        };
        if (form == null) return deny(player, label, "Dragon species must be verified");
        return permit(player, provider, form, label);
    }

    /** Check the exact species for both first morph and subsequent species changes. */
    public static boolean permitDragon(ServerPlayer player, ResourceLocation species) {
        Form target = species == null ? null : switch (species.toString()) {
            case "dragonsurvival:cave_dragon" -> Form.CAVE_DRAGON;
            case "dragonsurvival:forest_dragon" -> Form.FOREST_DRAGON;
            case "dragonsurvival:sea_dragon" -> Form.SEA_DRAGON;
            default -> null;
        };
        if (target == null) return deny(player, "Dragon", "Unknown Dragon Survival species");
        return permit(player, ProviderState.DRAGON_SURVIVAL_DRAGON, target, "Dragon");
    }

    private static boolean permit(ServerPlayer player, ProviderState provider,
                                  Form form, String label) {
        if (player == null || player.getServer() == null
                || !player.getServer().isSameThread()) return false;
        // Both gates must operate on the same observed native state. Re-probing
        // via the legacy registry could disagree about optional or unverified
        // providers midway through a single acquisition.
        var observations = NativePhysiologyObservations.observe(player);
        Decision admission = NativePhysiologyGateway.mayAcquire(
                player, form, PathRegistry.nativeCompatibilityRules(), observations);
        if (admission != Decision.ALLOW)
            return deny(player, label, "Progression admission: " + admission);

        StateSnapshot before = ProgressionNativeSnapshot.from(observations);
        // Native rank upgrades do not add a new provider.
        if (before.present(provider)) return true;
        if (!before.isComplete())
            return deny(player, label, "Native provider observations are unverified");

        // Preserve approved JAWR double-affliction coexistence; no lich or dragon bypass.
        boolean vampireAndWolf = (provider == ProviderState.JAWR_VAMPIRE
                    && before.present(ProviderState.JAWR_LYCANTHROPE))
                || (provider == ProviderState.JAWR_LYCANTHROPE
                    && before.present(ProviderState.JAWR_VAMPIRE));
        if (vampireAndWolf
                && !before.present(ProviderState.DRAGON_SURVIVAL_DRAGON)
                && !before.present(ProviderState.GOETY_LICH)
                && !before.present(ProviderState.FORBIDDEN_MAGIC_LICH)) return true;

        EnumMap<ProviderState, Observation> proposed = new EnumMap<>(ProviderState.class);
        proposed.putAll(before.observations());
        proposed.put(provider, Observation.PRESENT);
        TransitionDecision transition = TransitionPolicy.assess(
                before, new StateSnapshot(proposed), TransitionKind.NORMAL);
        return transition.allowed() || deny(player, label, transition.reason());
    }

    private static boolean deny(ServerPlayer player, String label, String reason) {
        if (player != null) player.sendSystemMessage(Component.literal(
                "Cannot acquire " + label + ": " + reason));
        return false;
    }
}
