package io.github.heatwaveqc.tmm.tradersvengence.runtime;

/** Pure selection rules; easy to test without launching Minecraft. */
public final class VengeanceRules {
    public static final int SEA_LEVEL_TOLERANCE = 10;
    public static final int SIGN_RADIUS = 32;
    public static final int VILLAGER_RANGE = 16;
    public static final int VILLAGER_COOLDOWN_TICKS = 50;
    public static final float MISSILE_DAMAGE = 6.0F;

    private VengeanceRules() {}

    /** Only hits which changed health count as an attack; shields/canceled hits do not. */
    public static boolean isActualHealthDamage(float appliedDamage) {
        return appliedDamage > 0.0F;
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
