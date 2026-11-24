package io.github.heatwaveqc.tmm.economy.audit;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.Map;
import java.util.TreeSet;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmcCoverageSnapshotTest {
    @Test
    void exportsSameSortedRegistryAndDistinctMissingZeroAndPositiveState() {
        var all = new TreeSet<>(java.util.List.of(
                "modz:recipe_less", "minecraft:stick", "modz:zero_mapped"));
        var mapped = Map.of("minecraft:stick", 4L, "modz:zero_mapped", 0L);
        var snapshot = EmcCoverageSnapshot.encode(all, mapped);
        JsonObject ids = JsonParser.parseString(snapshot.itemsJson()).getAsJsonObject();
        JsonObject values = JsonParser.parseString(snapshot.emcJson()).getAsJsonObject()
                .getAsJsonObject("entries");
        JsonObject statuses = JsonParser.parseString(snapshot.statusJson()).getAsJsonObject();

        assertEquals(3, snapshot.total());
        assertEquals(1, snapshot.positive());
        assertEquals(1, snapshot.unmapped());
        assertEquals(1, snapshot.mappedZero());
        assertEquals("minecraft:stick", ids.getAsJsonArray("items").get(0).getAsString());
        assertEquals(4L, values.getAsJsonObject("minecraft:stick").get("emc").getAsLong());
        assertEquals(0L, values.getAsJsonObject("modz:zero_mapped").get("emc").getAsLong());
        assertFalse(values.has("modz:recipe_less"));
        assertEquals("modz:recipe_less",
                statuses.getAsJsonArray("unmapped_ids").get(0).getAsString());
        assertEquals("modz:zero_mapped",
                statuses.getAsJsonArray("mapped_zero_ids").get(0).getAsString());
    }

    @Test
    void jsonSerializationIsDeterministicRegardlessOfMapInsertionOrder() {
        var all = new TreeSet<>(java.util.List.of("b:first", "a:last"));
        var left = EmcCoverageSnapshot.encode(all, Map.of("b:first", 99L, "a:last", 2L));
        var right = EmcCoverageSnapshot.encode(all, Map.of("a:last", 2L, "b:first", 99L));
        assertEquals(left.itemsJson(), right.itemsJson());
        assertEquals(left.emcJson(), right.emcJson());
        assertEquals(left.statusJson(), right.statusJson());
    }

    @Test
    void invalidOrOutOfRegistryValuesFailClosed() {
        var all = new TreeSet<>(java.util.List.of("minecraft:stick"));
        assertThrows(IllegalArgumentException.class,
                () -> EmcCoverageSnapshot.encode(all, Map.of("minecraft:stick", -1L)));
        assertThrows(IllegalArgumentException.class,
                () -> EmcCoverageSnapshot.encode(all, Map.of("other:item", 1L)));
        assertThrows(IllegalArgumentException.class,
                () -> EmcCoverageSnapshot.encode(new TreeSet<>(), Map.of()));
    }
}
