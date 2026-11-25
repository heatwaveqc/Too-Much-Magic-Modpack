package io.github.heatwaveqc.tyr.progression.runtime;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Prevents the same physical removal from being credited by both the normal
 * NeoForge BlockDropsEvent and TreeChop's finish-felling notification.
 * Exactly-once applies to a (player, dimension, position, game tick).
 */
public final class RecentBlockCredits {
    private RecentBlockCredits() {}
    private record Key(UUID player, String dimension, long pos,long tick) {}
    private static final Map<Key, Long> LAST = new HashMap<>();

    public static synchronized boolean first(UUID player, String dimension, long position, long tick) {
        Key key = new Key(player, dimension, position,tick);
        Long previous = LAST.put(key, tick);
        if (LAST.size() > 8192) {
            LAST.entrySet().removeIf(e -> tick - e.getValue() > 10);
        }
        // The caller uses the ORIGINAL break tick even on deferred callbacks.
        return previous == null || previous != tick;
    }

    public static synchronized void forget(UUID player) {
        LAST.keySet().removeIf(k -> k.player().equals(player));
    }
}
