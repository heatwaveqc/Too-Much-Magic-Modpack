package io.github.heatwaveqc.tyr.progression.core;

import java.util.Set;

/** Pure policy for Newbie-first and Monk-toggled permission sources. */
public final class VirtualElytraPolicy {
    public static final String NEWBIE = "tyr_progression:newbie";
    public static final String MONK = "tyr_progression:monk_momentum_glide";
    private VirtualElytraPolicy() {}

    public static Set<String> sources(boolean alive, boolean creative, boolean spectator,
                                       boolean newbie, boolean monkUnlocked, boolean monkModeOn) {
        if (!alive || creative || spectator) return Set.of();
        if (newbie) return Set.of(NEWBIE);
        if (monkUnlocked && monkModeOn) return Set.of(MONK);
        return Set.of();
    }
}
