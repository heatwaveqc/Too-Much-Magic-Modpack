package io.github.heatwaveqc.tmm.transformations.core;

import io.github.heatwaveqc.tmm.transformations.TransformationsMod;
import io.github.heatwaveqc.tmm.transformations.adapters.ProbeRegistry;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.EnumMap;
import java.util.function.BiPredicate;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.ModList;

/**
 * Server-side enforcement at native supernatural acquisition setters and
 * at the single ProgressionApi.join() boundary.
 *
 * The standalone modules remain separately buildable; one small reflected
 * integration connects their public APIs when both JARs are present.
 */
public final class NativeSpeciesJoinGuard {
    private static final String NEWBIE_TAG = "tmm_newbie";
    private static final String PROGRESSION_API =
            "io.github.heatwaveqc.tmm.progression.core.ProgressionApi";
    private static volatile Method membershipQuery;

    private NativeSpeciesJoinGuard() {}

    /** Register once during mod initialization, not on every attempted join. */
    public static void registerProgressionJoinVeto() {
        if (!ModList.get().isLoaded("tmm_progression")) return;
        try {
            Class<?> api = Class.forName(PROGRESSION_API);
            Method register = api.getMethod("registerJoinVeto", BiPredicate.class);
            BiPredicate<ServerPlayer, String> veto = (player, pathId) -> {
                if (SpeciesEnrollmentPolicy.canJoinHumanPath(ProbeRegistry.observe(player)))
                    return true;
                player.sendSystemMessage(Component.literal(
                        "Progression paths require a human form. Cure your transformation before joining."));
                return false;
            };
            register.invoke(null, veto);
            membershipQuery = api.getMethod("anyJoined", ServerPlayer.class);
            TransformationsMod.LOGGER.info("Native class/species mutual exclusion registered for all Progression paths");
        } catch (ReflectiveOperationException | LinkageError e) {
            // A silent failure would let players permanently combine incompatible
            // human classes and species. Fail startup rather than bypass the rule.
            throw new IllegalStateException("Cannot register mandatory Progression species join veto", e);
        }
    }

    private static boolean hasJoinedProgression(ServerPlayer player) {
        if (!ModList.get().isLoaded("tmm_progression")) return false;
        try {
            Method method = membershipQuery;
            if (method == null) method = Class.forName(PROGRESSION_API)
                    .getMethod("anyJoined", ServerPlayer.class);
            Object joined = method.invoke(null, player);
            return joined instanceof Boolean status ? status : true; // fail closed
        } catch (ReflectiveOperationException | LinkageError | RuntimeException e) {
            TransformationsMod.LOGGER.error("Cannot inspect Progression class membership; blocking species acquisition", e);
            return true;
        }
    }

    /**
     * True only for authorized new forms; existing ranks and cures remain native.
     * Covers Vampire, Werewolf, Dragon and both lich sources, as well as future
     * native species hooked at this boundary.
     */
    public static boolean permit(ServerPlayer player, ProviderState acquiring, String label) {
        StateSnapshot before = ProbeRegistry.observe(player);
        boolean alreadyHasForm = before.present(acquiring);
        boolean newbie = player.getTags().contains(NEWBIE_TAG);
        boolean joined = !alreadyHasForm && hasJoinedProgression(player);

        if (!SpeciesEnrollmentPolicy.canAcquire(alreadyHasForm, newbie, joined)) {
            String reason = newbie ? "Newbie Mode" : "an active Progression path";
            player.sendSystemMessage(Component.literal(
                    "Cannot become " + label + " while in " + reason
                    + ". Graduate or choose a supernatural path instead."));
            return false;
        }
        if (alreadyHasForm) return true; // upgrades and migrated characters

        if (!before.isComplete()) {
            player.sendSystemMessage(Component.literal(
                    "Cannot verify existing transformations: " + label
                    + " acquisition blocked to prevent conflicting forms."));
            return false;
        }

        // The JAWR-only Abomination design permits dual JAWR afflictions without
        // lichdom. The older classifier is still awaiting that unrelated rewrite.
        boolean acquiringVampire = acquiring == ProviderState.JAWR_VAMPIRE;
        boolean acquiringWerewolf = acquiring == ProviderState.JAWR_LYCANTHROPE;
        if ((acquiringVampire && before.present(ProviderState.JAWR_LYCANTHROPE)
                || acquiringWerewolf && before.present(ProviderState.JAWR_VAMPIRE))
                && !before.present(ProviderState.DRAGON_SURVIVAL_DRAGON)
                && !before.present(ProviderState.GOETY_LICH)
                && !before.present(ProviderState.FORBIDDEN_MAGIC_LICH)) return true;

        EnumMap<ProviderState, Observation> proposed = new EnumMap<>(ProviderState.class);
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
