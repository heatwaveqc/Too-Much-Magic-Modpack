package io.github.heatwaveqc.tmm.transformations.vampire;

import io.github.heatwaveqc.tmm.transformations.TransformationsMod;
import io.github.heatwaveqc.tmm.transformations.adapters.JAWRVampireProbe;
import io.github.heatwaveqc.tmm.transformations.adapters.VampirismFactionProbe;
import io.github.heatwaveqc.tmm.transformations.adapters.ProbeRegistry;
import io.github.heatwaveqc.tmm.transformations.core.StateSnapshot;
import io.github.heatwaveqc.tmm.transformations.core.Observation;
import io.github.heatwaveqc.tmm.transformations.core.ProviderState;
import io.github.heatwaveqc.tmm.transformations.storage.ReviewRecord;
import io.github.heatwaveqc.tmm.transformations.storage.TransformationAttachments;
import java.lang.reflect.InvocationTargetException;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

/**
 * Server-authoritative prototype. All third-party mutations require explicit
 * config; no automatic unification ships by default.
 */
public final class VampireChassisRuntime {
    private static final ThreadLocal<Boolean> MUTATING = ThreadLocal.withInitial(() -> false);
    private static final VampirismFactionProbe VAMPIRISM =
            new VampirismFactionProbe(ProviderState.VAMPIRISM_VAMPIRE);
    private static final VampirismFactionProbe HUNTER =
            new VampirismFactionProbe(ProviderState.VAMPIRISM_HUNTER);
    private static final JAWRVampireProbe JAWR = new JAWRVampireProbe();

    private VampireChassisRuntime() { }

    public static boolean inTransaction() { return MUTATING.get(); }

    public static boolean isDual(ServerPlayer player) {
        return VampireChassisConfig.chassis()
                && VAMPIRISM.observe(player) == Observation.PRESENT
                && JAWR.observe(player) == Observation.PRESENT;
    }

    private static void hold(ServerPlayer player, String reason, Exception ex) {
        ReviewRecord old = player.getData(TransformationAttachments.REVIEW.get());
        if (old.adminHold()) return; // avoid flooding logs and chat on each native blood tick
        TransformationsMod.LOGGER.error("Vampire bridge halted mutation for {}: {}",
                player.getGameProfile().getName(), reason, ex);
        player.setData(TransformationAttachments.REVIEW.get(),
                new ReviewRecord(ReviewRecord.CURRENT_SCHEMA, true,
                        "Vampire pilot: " + reason));
        player.sendSystemMessage(Component.literal(
                "Vampire bridge error: " + reason + ". Admin review required; do not retry on a valuable save."));
    }

    /**
     * One-way ascension: ordinary Vampirism stays untouched; JAWR acquisition
     * (Lilith's Blood) first enrolls Vampirism if needed.
     * For JAWR-native cures, remove Vampirism before accepting JAWR's removal.
     */
    public static boolean onJAWRSetLevel(ServerPlayer player, int newLevel) {
        if (!VampireChassisConfig.chassis() || inTransaction()) return true;
        try {
            int old = VampireNativeAccess.jawrLevel(player);
            if (old > 0 && newLevel <= 0) {
                if (VAMPIRISM.observe(player) == Observation.PRESENT) {
                    if (!VampireNativeAccess.canLeaveVampirism(player)) {
                        player.sendSystemMessage(Component.literal(
                                "Full vampire cure refused by Vampirism. Both states remain."));
                        return false;
                    }
                    MUTATING.set(true);
                    try {
                        if (!VampireNativeAccess.leaveVampirism(player)
                                || VAMPIRISM.observe(player) != Observation.ABSENT) {
                            throw new IllegalStateException("Vampirism cure did not commit");
                        }
                    } finally {
                        MUTATING.remove();
                    }
                }
                player.setData(TransformationAttachments.BLOOD_CARRY.get(), VampireBloodCarry.empty());
                return true;
            }
            if (old <= 0 && newLevel > 0 && VampireChassisConfig.acquisition()) {
                if (!otherStatesPermitVampire(player)) return false;
                Observation observed = VAMPIRISM.observe(player);
                if (observed == Observation.UNKNOWN) return false;
                if (observed == Observation.PRESENT) return true;
                if (!VampireNativeAccess.methodsAvailable()
                        || !VampireNativeAccess.canJoinVampirism(player)) return false;
                MUTATING.set(true);
                try {
                    if (!VampireNativeAccess.enterVampirism(player, 1)
                            || !VampireNativeAccess.isVampirismVampire(player)) {
                        throw new IllegalStateException("Vampirism ascension enrollment failed");
                    }
                } finally {
                    MUTATING.remove();
                }
            }
            return true;
        } catch (ReflectiveOperationException | LinkageError | RuntimeException e) {
            hold(player, "JAWR ascension or coordinated cure failed", new IllegalStateException(e));
            return false;
        }
    }

    private static boolean otherStatesPermitVampire(ServerPlayer player) {
        StateSnapshot snapshot = ProbeRegistry.observe(player);
        if (snapshot.get(ProviderState.VAMPIRISM_HUNTER) != Observation.ABSENT) return false;
        for (ProviderState state : ProviderState.values()) {
            if (state == ProviderState.VAMPIRISM_HUNTER || state == ProviderState.VAMPIRISM_VAMPIRE
                    || state == ProviderState.JAWR_VAMPIRE) continue;
            Observation observation = snapshot.get(state);
            if (observation == Observation.PRESENT) return false;
            if (observation == Observation.UNKNOWN && !VampireChassisConfig.allowUnverifiedProviders())
                return false;
        }
        return true;
    }

    /** Ordinary infection is native Vampirism only; never auto-join JAWR. */
    public static boolean approveVampirismEntry(ServerPlayer player) {
        return true;
    }

    /** Called after JAWR changes, without modifying JAWR's native transaction. */
    public static void onJAWRBloodChanged(ServerPlayer player) {
        if (!isDual(player)) return;
        try {
            int nativeMax = VampireNativeAccess.nativeMax(player);
            int jawrMax = VampireNativeAccess.jawrMax(player);
            if (nativeMax <= 0 || jawrMax <= 0) return;
            int projected = VampireBloodMath.project(
                    VampireNativeAccess.jawrCurrent(player), jawrMax, nativeMax);
            if (projected != VampireNativeAccess.nativeCurrent(player)) {
                VampireNativeAccess.setNativeShadowBlood(player, projected);
            }
        } catch (ReflectiveOperationException | LinkageError | RuntimeException e) {
            hold(player, "Vampirism shadow mirror update failed", new IllegalStateException(e));
        }
    }

    /**
     * Divert an original Vampirism feeding operation into JAWR. Return the
     * unused native units in the same convention as BloodStats.addBlood().
     */
    public static int feedFromVampirism(ServerPlayer player, int amount) {
        if (amount <= 0 || !isDual(player)) return amount;
        try {
            int jawrMax = VampireNativeAccess.jawrMax(player);
            int nativeMax = VampireNativeAccess.nativeMax(player);
            int before = VampireNativeAccess.jawrCurrent(player);
            if (jawrMax <= 0 || nativeMax <= 0) throw new IllegalStateException("invalid blood capacity");
            if (before >= jawrMax) return amount;
            int gain = VampireBloodMath.costRoundUp(amount, nativeMax, jawrMax);
            VampireNativeAccess.addJAWRBlood(player, gain);
            int after = VampireNativeAccess.jawrCurrent(player);
            if (after <= before) throw new IllegalStateException("JAWR did not accept feed");
            onJAWRBloodChanged(player);
            // Consume only the units actually credited, rounded upward to
            // avoid gaining JAWR blood without consuming the feeding source.
            int credited = VampireBloodMath.costRoundUp(after - before, jawrMax, nativeMax);
            return Math.max(0, amount - credited);
        } catch (ReflectiveOperationException | LinkageError | RuntimeException e) {
            hold(player, "Vampirism feed to JAWR failed", new IllegalStateException(e));
            return amount;
        }
    }

    /** All-or-nothing Vampirism power/weapon expenditure against JAWR. */
    public static boolean spendFromVampirism(ServerPlayer player, int amount) {
        if (!isDual(player)) return false;
        if (amount <= 0) return true;
        try {
            int max = VampireNativeAccess.jawrMax(player);
            int nativeMax = VampireNativeAccess.nativeMax(player);
            if (max <= 0 || nativeMax <= 0) throw new IllegalStateException("invalid blood capacity");
            int cost = VampireBloodMath.costRoundUp(amount, nativeMax, max);
            int before = VampireNativeAccess.jawrCurrent(player);
            if (before < cost) return false;
            VampireNativeAccess.removeJAWRBlood(player, cost);
            int after = VampireNativeAccess.jawrCurrent(player);
            if (before - after != cost) {
                hold(player, "Vampirism translated charge mismatched",
                        new IllegalStateException("expected " + cost + ", observed " + (before - after)));
                return false;
            }
            onJAWRBloodChanged(player);
            return true;
        } catch (ReflectiveOperationException | LinkageError | RuntimeException e) {
            hold(player, "Vampirism charge to JAWR failed", new IllegalStateException(e));
            return false;
        }
    }

    /** A native Vampirism cure removes JAWR too, rather than downgrading. */
    public static boolean preflightVampirismCure(ServerPlayer player) {
        if (!isDual(player)) return true;
        try {
            return VampireNativeAccess.methodsAvailable()
                    && VampireNativeAccess.jawrLevel(player) > 0;
        } catch (ReflectiveOperationException | LinkageError e) {
            hold(player, "Vampirism cure could not preflight JAWR", new IllegalStateException(e));
            return false;
        }
    }

    public static void afterVampirismCure(ServerPlayer player) {
        if (!VampireChassisConfig.chassis() || inTransaction()) return;
        if (JAWR.observe(player) != Observation.PRESENT) return;
        try {
            MUTATING.set(true);
            VampireNativeAccess.setJAWRLevel(player, 0);
            if (JAWR.observe(player) != Observation.ABSENT) {
                throw new IllegalStateException("JAWR state retained after Vampirism cure");
            }
            player.setData(TransformationAttachments.BLOOD_CARRY.get(), VampireBloodCarry.empty());
        } catch (ReflectiveOperationException | LinkageError | RuntimeException e) {
            hold(player, "Vampirism cure could not complete JAWR removal", new IllegalStateException(e));
        } finally {
            MUTATING.remove();
        }
    }

    public static void onJAWRLevelAdvanced(ServerPlayer player) {
        if (isDual(player)) onJAWRBloodChanged(player);
    }

    /**
     * Operator-only, disposable-world test cure. Preflight guards known failure
     * paths; third-party side effects cannot yet be fully rolled back.
     */
    public static boolean testCure(ServerPlayer player) {
        if (!VampireChassisConfig.cure() || !isDual(player) || inTransaction()) return false;
        if (player.getData(TransformationAttachments.REVIEW.get()).adminHold()) return false;
        int priorLevel = 0;
        try {
            if (!VampireNativeAccess.canLeaveVampirism(player)) return false;
            priorLevel = VampireNativeAccess.vampirismLevel(player);
            if (priorLevel < 1 || VampireNativeAccess.jawrLevel(player) < 1) return false;
            MUTATING.set(true);
            if (!VampireNativeAccess.leaveVampirism(player)) return false;
            VampireNativeAccess.setJAWRLevel(player, 0);
            if (VampirismObservation(player) != Observation.ABSENT
                    || JAWR.observe(player) != Observation.ABSENT) {
                throw new IllegalStateException("Provider state still present after cure");
            }
            player.setData(TransformationAttachments.BLOOD_CARRY.get(), VampireBloodCarry.empty());
            return true;
        } catch (ReflectiveOperationException | RuntimeException | LinkageError e) {
            try {
                if (priorLevel > 0 && VampirismObservation(player) != Observation.PRESENT) {
                    VampireNativeAccess.enterVampirism(player, priorLevel);
                }
            } catch (ReflectiveOperationException | LinkageError rollback) {
                e.addSuppressed(rollback);
            }
            hold(player, "Coordinated cure failed; progress may need backup restoration",
                    new IllegalStateException(e));
            return false;
        } finally {
            MUTATING.remove();
        }
    }

    private static Observation VampirismObservation(ServerPlayer player) {
        return VAMPIRISM.observe(player);
    }
}
