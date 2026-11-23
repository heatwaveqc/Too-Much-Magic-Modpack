package io.github.heatwaveqc.tyr.progression;

import io.github.heatwaveqc.tyr.progression.network.FoodPreviewWire;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/** No Minecraft client/server boot: strict packet table and chunking regressions. */
class FoodPreviewWireTest {
    @Test void preservesNumericalDietAndMagicalPermission() {
        var expected = Map.of(
            "minecraft:beef", FoodPreviewWire.Entry.nourishment(6, 2.0f),
            "minecraft:cooked_beef", FoodPreviewWire.Entry.nourishment(3, 2.0f),
            "example:charred", FoodPreviewWire.Entry.nourishment(2, 1.5f),
            "example:unfavored", FoodPreviewWire.Entry.nourishment(0, 0),
            "example:magical", FoodPreviewWire.Entry.permitNative());
        var chunks = FoodPreviewWire.chunks(expected);
        assertEquals(1, chunks.size());
        assertEquals(expected, FoodPreviewWire.parse(String.join("", chunks)));
        assertEquals(Map.of(), FoodPreviewWire.parse(String.join("",
            FoodPreviewWire.chunks(Map.of()))), "A non-dragon clears stale preview");
    }

    @Test void chunksLargeFoodTablesWithoutSplittingRows() {
        var foods = new HashMap<String, FoodPreviewWire.Entry>();
        for (int n = 0; n < 650; n++)
            foods.put("example:food_" + n, FoodPreviewWire.Entry.nourishment(n % 20, 1.5f));
        var chunks = FoodPreviewWire.chunks(foods);
        assertTrue(chunks.size() > 1);
        assertTrue(chunks.stream().allMatch(c -> c.length() <= FoodPreviewWire.CHUNK_CHARS));
        assertTrue(chunks.stream().allMatch(c -> c.endsWith("\n")));
        assertEquals(foods, FoodPreviewWire.parse(String.join("", chunks)));
    }

    @Test void failsClosedOnMalformedOrDuplicateRows() {
        for (String row : new String[]{
            "minecraft:beef\t6\t2.0", // no terminal newline
            "Minecraft:beef\t6\t2.0\n",
            "minecraft:beef\t-1\t2.0\n",
            "minecraft:beef\t6\tNaN\n",
            "minecraft:beef\t6\tInfinity\n",
            "minecraft:beef\tP\nminecraft:beef\t6\t2.0\n",
            "minecraft:beef\tQ\n",
            "minecraft:beef\t6\t2.0\textra\n"
        }) assertThrows(IllegalArgumentException.class, () -> FoodPreviewWire.parse(row), row);
    }
}
