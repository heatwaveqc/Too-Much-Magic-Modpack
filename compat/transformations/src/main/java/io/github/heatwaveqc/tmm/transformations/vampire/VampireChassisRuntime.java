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

    public static boolean onJAWRSetLevel(ServerPlayer player, int newLevel) {
        if (!VampireChassisConfig.chassis() || inTransaction()) return true;
        try {
            int old = VampireNativeAccess.jawrLevel(player);
            if (old > 0 && newLevel <= 0 && VAMPIRISM.observe(player) == Observation.PRESENT) {
                player.sendSystemMessage(Component.literal(
                        "Dual-vampire cure blocked; use the controlled test cure after backing up the world."));
                return false;
            }
            if (newLevel > 0 && old <= 0 && VampireChassisConfig.acquisition()) {
                player.setData(TransformationAttachments.BLOOD_CARRY.get(), VampireBloodCarry.empty());
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
                        return false;
                    }
                } finally {
                    MUTATING.remove();
                }
                return true;
            }
            return true;
        } catch (ReflectiveOperationException | LinkageError e) {
            hold(player, "JAWR transformation preparation failed", new IllegalStateException(e));
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

    public static boolean approveVampirismEntry(ServerPlayer player) {
        if (!VampireChassisConfig.acquisition() || inTransaction()) return true;
        try {
            // When starting Vampirism, JAWR's native enrollment must be available.
            return VampireNativeAccess.methodsAvailable()
                    && JAWR.observe(player) != Observation.UNKNOWN
                    && otherStatesPermitVampire(player);
        } catch (RuntimeException e) {
            hold(player, "Vampirism entry preflight failed", e);
            return false;
        }
    }

    public static void afterVampirismEntry(ServerPlayer player) {
        if (!VampireChassisConfig.acquisition() || inTransaction()) return;
        try {
            player.setData(TransformationAttachments.BLOOD_CARRY.get(), VampireBloodCarry.empty());
            if (JAWR.observe(player) == Observation.ABSENT) {
                MUTATING.set(true);
                try {
                    VampireNativeAccess.startJAWR(player);
                } finally {
                    MUTATING.remove();
                }
            }
            if (JAWR.observe(player) != Observation.PRESENT) {
                hold(player, "JAWR enrollment did not commit", new IllegalStateException("missing JAWR vampire"));
            } else {
                onNativeBloodChanged(player);
            }
        } catch (ReflectiveOperationException | LinkageError e) {
            hold(player, "Vampirism entry could not initialize JAWR", new IllegalStateException(e));
        }
    }

    public static void onNativeBloodChanged(ServerPlayer player) {
        if (!isDual(player)) return;
        try {
            int nativeMax = VampireNativeAccess.nativeMax(player);
            int jawrMax = VampireNativeAccess.jawrMax(player);
            if (nativeMax <= 0 || jawrMax <= 0) return; // native leveling may still initialize max.
            int projected = VampireBloodMath.project(
                    VampireNativeAccess.nativeCurrent(player), nativeMax, jawrMax);
            if (projected != VampireNativeAccess.jawrCurrent(player)) {
                VampireNativeAccess.setJAWRShadowBlood(player, projected);
            }
        } catch (ReflectiveOperationException | LinkageError | RuntimeException e) {
            hold(player, "Blood shadow update failed", new IllegalStateException(e));
        }
    }

    /** Return true when JAWR's native mutation must be cancelled. */
    public static boolean interceptJAWRBlood(ServerPlayer player, int amount, boolean gain) {
        if (!isDual(player)) return false;
        if (amount <= 0) return true;
        try {
            int maxJAWR = VampireNativeAccess.jawrMax(player);
            int maxNative = VampireNativeAccess.nativeMax(player);
            if (maxJAWR <= 0 || maxNative <= 0) {
                hold(player, "Uninitialized dual vampire blood capacity",
                        new IllegalStateException("blood max <= 0"));
                return true;
            }
            if (gain) {
                int originalBlood = VampireNativeAccess.nativeCurrent(player);
                if (originalBlood >= maxNative) {
                    player.setData(TransformationAttachments.BLOOD_CARRY.get(), VampireBloodCarry.empty());
                    return true;
                }
                VampireBloodCarry carry = player.getData(TransformationAttachments.BLOOD_CARRY.get());
                VampireBloodCarry.Gain credit = carry.gain(amount, maxJAWR, maxNative);
                int nativeUnits = Math.min(credit.wholeUnits(), maxNative - originalBlood);
                VampireNativeAccess.gainNativeBlood(player, nativeUnits);
                int updated = VampireNativeAccess.nativeCurrent(player);
                if (updated < originalBlood + nativeUnits) {
                    throw new IllegalStateException("Vampirism rejected credited JAWR blood; pending admin review");
                }
                player.setData(TransformationAttachments.BLOOD_CARRY.get(),
                        updated >= maxNative ? VampireBloodCarry.empty() : credit.remainder());
            } else {
                int nativeUnits = VampireBloodMath.costRoundUp(amount, maxJAWR, maxNative);
                VampireNativeAccess.spendNativeBlood(player, nativeUnits);
            }
            // Canonical mutation hooks sync the JAWR shadow; this is a defensive recheck.
            onNativeBloodChanged(player);
        } catch (ReflectiveOperationException | LinkageError | RuntimeException e) {
            hold(player, "JAWR blood transaction translation failed", new IllegalStateException(e));
        }
        return true; // never let a second JAWR food/blood mutation through.
    }

    public static void onJAWRLevelAdvanced(ServerPlayer player) {
        if (isDual(player)) onNativeBloodChanged(player);
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
