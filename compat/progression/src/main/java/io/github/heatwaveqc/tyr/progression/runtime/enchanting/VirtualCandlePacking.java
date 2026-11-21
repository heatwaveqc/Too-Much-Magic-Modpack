package io.github.heatwaveqc.tyr.progression.runtime.enchanting;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/** Exact owner-approved packing for vanilla candle items in 32 virtual shelf positions. */
public final class VirtualCandlePacking {
    public static final int CANDLES_PER_POSITION = 4;
    public static final int MAX_POSITIONS = 32;

    private VirtualCandlePacking() {}

    public record Cluster(String candleType, int candles) {
        public Cluster {
            if (candleType == null || candleType.isBlank())
                throw new IllegalArgumentException("candle type is required");
            if (candles < 1 || candles > CANDLES_PER_POSITION)
                throw new IllegalArgumentException("a virtual candle position contains one to four candles");
        }
    }

    /**
     * Packs each exact candle block/color independently. Counts are real deposited
     * items; no mixed-color remainder sharing and no fabricated four-candle state.
     */
    public static List<Cluster> pack(Map<String, Integer> counts) {
        if (counts == null) throw new IllegalArgumentException("counts are required");
        List<Cluster> result = new ArrayList<>();
        counts.entrySet().stream()
            .sorted(Comparator.comparing(Map.Entry::getKey))
            .forEach(entry -> append(result, entry.getKey(), entry.getValue()));
        if (result.size() > MAX_POSITIONS)
            throw new IllegalArgumentException("virtual shelf capacity exceeds 32 positions");
        return List.copyOf(result);
    }

    public static int positions(int count) {
        if (count < 0) throw new IllegalArgumentException("candle count cannot be negative");
        return (count + CANDLES_PER_POSITION - 1) / CANDLES_PER_POSITION;
    }

    private static void append(List<Cluster> result, String type, Integer count) {
        if (type == null || type.isBlank()) throw new IllegalArgumentException("candle type is required");
        if (count == null || count < 0) throw new IllegalArgumentException("candle count cannot be negative");
        int remaining = count;
        while (remaining > 0) {
            int cluster = Math.min(CANDLES_PER_POSITION, remaining);
            result.add(new Cluster(type, cluster));
            remaining -= cluster;
        }
    }
}
