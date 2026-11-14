package io.github.heatwaveqc.tmm.monk.rules;

/**
 * Pure geometry/time policy for the first Monk rite. Minecraft day time 12000
 * is sunset and west is -X in Overworld coordinates.
 * There is deliberately no accumulated progress stored in this class.
 */
public final class SunsetTrialRules {
    public static final int REQUIRED_TICKS = 200;
    public static final long START_TICK = 11500L;
    public static final long END_TICK = 12500L;
    private static final double MIN_GAZE_DOT = Math.cos(Math.toRadians(8.0));

    private SunsetTrialRules() { }

    public static boolean duringSunset(long worldDayTime) {
        long clock = Math.floorMod(worldDayTime, 24000L);
        return clock >= START_TICK && clock <= END_TICK;
    }

    public static boolean facingWesternSun(double lookX, double lookY, double lookZ) {
        double lengthSquared = lookX * lookX + lookY * lookY + lookZ * lookZ;
        if (!Double.isFinite(lengthSquared) || lengthSquared < 0.5) return false;
        // Normalize so callers cannot pass a huge -X vector to bypass the cone.
        return -lookX / Math.sqrt(lengthSquared) >= MIN_GAZE_DOT;
    }

    public static int consecutiveTicks(int previous, boolean valid) {
        return valid ? Math.min(REQUIRED_TICKS, previous + 1) : 0;
    }
}
