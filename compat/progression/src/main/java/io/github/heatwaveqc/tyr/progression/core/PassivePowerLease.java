package io.github.heatwaveqc.tyr.progression.core;

/** Client snapshot freshness is fail-closed after a bounded lifetime. */
public final class PassivePowerLease {
    public static final int MAX_AGE_TICKS = 40;
    private PassivePowerLease() {}
    public static boolean valid(int receivedAt, int currentTick) {
        return receivedAt >= 0 && currentTick >= receivedAt
                && currentTick - receivedAt <= MAX_AGE_TICKS;
    }
}
