package io.github.heatwaveqc.tyr.progression.network;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * Small, source-independent wire contract for the server's already evaluated
 * per-profile diet projection. The client never receives or edits rules, recipes,
 * source evidence, native item components or player entitlements.
 */
public final class FoodPreviewWire {
    public static final int CHUNK_CHARS = 3500; // ASCII IDs/numbers; below 4096-byte codec limit.
    public static final int MAX_CHUNKS = 512;
    private FoodPreviewWire() {}

    public enum Mode { NUTRITION, PERMIT_NATIVE, DENY }

    /**
     * Explicit denial is different from a missing row. Clients must return
     * null FoodProperties for DENY, and preserve native for absent IDs.
     */
    public record Entry(int hunger, float saturationModifier, Mode mode) {
        public Entry {
            Objects.requireNonNull(mode);
            if (hunger < 0 || hunger > 4096 || !Float.isFinite(saturationModifier)
                    || saturationModifier < 0 || saturationModifier > 1024
                    || (mode != Mode.NUTRITION && (hunger != 0 || saturationModifier != 0)))
                throw new IllegalArgumentException("Invalid projected food decision");
        }
        public boolean nativePermission() { return mode == Mode.PERMIT_NATIVE; }
        public boolean denied() { return mode == Mode.DENY; }
        public static Entry permitNative() { return new Entry(0, 0, Mode.PERMIT_NATIVE); }
        public static Entry deny() { return new Entry(0, 0, Mode.DENY); }
        public static Entry nourishment(int hunger, float modifier) {
            return new Entry(hunger, modifier, Mode.NUTRITION);
        }
    }

    private static boolean itemId(String id) {
        return id.matches("[a-z0-9_.-]+:[a-z0-9_./-]+");
    }

    /** Deterministic line records: item TAB 'P' or item TAB hunger TAB modifier. */
    public static List<String> chunks(Map<String, Entry> entries) {
        List<String> pieces = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        for (var row : new TreeMap<>(entries).entrySet()) {
            if (!itemId(row.getKey()))
                throw new IllegalArgumentException("Unsafe dietary preview item ID");
            Entry e = row.getValue();
            String line = row.getKey() + "\t" + (e.nativePermission() ? "P" : e.denied()
                ? "D" : e.hunger() + "\t" + Float.toString(e.saturationModifier())) + "\n";
            if (line.length() > CHUNK_CHARS)
                throw new IllegalArgumentException("Oversized dietary preview record");
            if (current.length() + line.length() > CHUNK_CHARS) {
                pieces.add(current.toString());
                current.setLength(0);
            }
            current.append(line);
        }
        pieces.add(current.toString()); // Empty map clears a previous client snapshot.
        if (pieces.size() > MAX_CHUNKS)
            throw new IllegalArgumentException("Dietary preview exceeds bounded packet budget");
        return List.copyOf(pieces);
    }

    /** Transactional receiver: reject malformed/duplicated rows, never accept half a table. */
    public static Map<String, Entry> parse(String rows) {
        Map<String, Entry> result = new TreeMap<>();
        if (rows.length() > CHUNK_CHARS * MAX_CHUNKS)
            throw new IllegalArgumentException("Oversized dietary preview");
        if (rows.isEmpty()) return Map.of();
        if (rows.charAt(rows.length() - 1) != '\n')
            throw new IllegalArgumentException("Unterminated dietary preview");
        for (String line : rows.split("\n")) {
            String[] fields = line.split("\t", -1);
            if (fields.length < 2 || fields.length > 3 || !itemId(fields[0]))
                throw new IllegalArgumentException("Malformed dietary preview row");
            Entry parsed;
            try {
                if (fields.length == 2 && fields[1].equals("P")) {
                    parsed = Entry.permitNative();
                } else if (fields.length == 2 && fields[1].equals("D")) {
                    parsed = Entry.deny();
                } else if (fields.length == 3) {
                    parsed = Entry.nourishment(Integer.parseInt(fields[1]),
                        Float.parseFloat(fields[2]));
                } else {
                    throw new IllegalArgumentException("Unknown dietary preview record type");
                }
            } catch (NumberFormatException invalid) {
                throw new IllegalArgumentException("Invalid dietary preview numeric value", invalid);
            }
            if (result.putIfAbsent(fields[0], parsed) != null)
                throw new IllegalArgumentException("Duplicate dietary preview item");
        }
        return Map.copyOf(result);
    }
}
