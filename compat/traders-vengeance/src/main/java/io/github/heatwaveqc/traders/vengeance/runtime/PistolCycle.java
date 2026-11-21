package io.github.heatwaveqc.traders.vengeance.runtime;

/** Independent NPC-only adaptation of the verified Caliber Arms M1911 game parameters. */
public final class PistolCycle {
    public static final float DAMAGE = 7F;
    public static final float RANGE = 35F;
    public static final int RPM = 420;
    public static final int MAGAZINE = 7;
    public static final int RELOAD_TICKS = 42;
    public static final float INTERVAL = 1200F / RPM;
    public enum Action { NONE, SHOT, MAG_OUT, MAG_IN, SLIDE }

    private int rounds = MAGAZINE + 1;
    private int reload;
    private float cooldown;

    public Action tick(boolean ready) {
        cooldown = cooldown > 0F ? cooldown - 1F : 0F;
        if (reload > 0) {
            int remaining = --reload;
            if (remaining == 0) rounds = MAGAZINE;
            if (remaining == 24) return Action.MAG_IN;
            if (remaining == 6) return Action.SLIDE;
            return Action.NONE;
        }
        if (!ready || cooldown > 0F) {
            if (!ready && cooldown < 0F) cooldown = 0F;
            return Action.NONE;
        }
        if (rounds == 0) {
            reload = RELOAD_TICKS;
            cooldown = 0;
            return Action.MAG_OUT;
        }
        rounds--;
        cooldown += INTERVAL;
        return Action.SHOT;
    }
    int rounds() { return rounds; }
    int reloadRemaining() { return reload; }
}
