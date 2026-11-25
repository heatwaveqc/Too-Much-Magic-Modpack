package io.github.heatwaveqc.tmm.transformations.network;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/** Client's server-authored cosmetic flags. Contains no client game classes. */
public final class DracolichVisualState {
    private static final Set<UUID> ACTIVE = ConcurrentHashMap.newKeySet();
    private DracolichVisualState() { }

    public static void apply(UUID player, boolean active) {
        if (active) ACTIVE.add(player); else ACTIVE.remove(player);
    }

    public static boolean active(UUID player) { return ACTIVE.contains(player); }
    public static void clear() { ACTIVE.clear(); }
}
