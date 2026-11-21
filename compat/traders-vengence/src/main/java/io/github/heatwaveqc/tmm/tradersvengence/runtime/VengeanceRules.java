package io.github.heatwaveqc.tmm.tradersvengence.runtime;

import java.util.UUID;

/** Pure selection rules; easy to test without launching Minecraft. */
public final class VengeanceRules {
    public static final int SEA_LEVEL_TOLERANCE = 10;
    public static final int SIGN_RADIUS = 32;
    public static final double GOLEM_VENDETTA_RADIUS = 10.0D;
    // Each vanilla search checks up to ten columns; cap total work to 30.
    public static final int TRADER_POSITION_GROUPS = 3;

    private VengeanceRules() {}

    /** Only hits which changed health count as an attack; shields/canceled hits do not. */
    public static boolean isActualHealthDamage(float appliedDamage) {
        return appliedDamage > 0.0F;
    }

    /** A continuing Priest target is not a new crime after the Hero pardon. */
    public static boolean isNewPriestTarget(UUID lastTarget, UUID observedTarget) {
        return observedTarget != null && !observedTarget.equals(lastTarget);
    }

    /** Inclusive Euclidean boundary for proximity culpability at a golem death. */
    public static boolean withinGolemVendettaRadius(double dx, double dy, double dz) {
        return dx * dx + dy * dy + dz * dz
            <= GOLEM_VENDETTA_RADIUS * GOLEM_VENDETTA_RADIUS;
    }

    public static boolean nearSeaLevel(int feetY, int seaLevel) {
        return Math.abs(feetY - seaLevel) <= SEA_LEVEL_TOLERANCE;
    }

    public static boolean markedSign(String line) {
        return line != null && (
            line.trim().equalsIgnoreCase("No Soliciting")
            || line.trim().equalsIgnoreCase("[No Soliciting]")
        );
    }
}
