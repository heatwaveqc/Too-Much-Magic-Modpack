package io.github.heatwaveqc.tyr.progression;

import io.github.heatwaveqc.tyr.progression.foundation.TartanPalette;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TartanPaletteTest {
    @Test void all65536OrderedCombinationsRoundTripWithoutCollision() {
        boolean[] seen = new boolean[65536];
        for (int a=0;a<16;a++) for (int b=0;b<16;b++)
            for (int c=0;c<16;c++) for (int d=0;d<16;d++) {
                var p = TartanPalette.of(a,b,c,d);
                assertFalse(seen[p.packed()]);
                seen[p.packed()] = true;
                var roundTrip = new TartanPalette(p.packed());
                assertEquals(a,roundTrip.dye(0));
                assertEquals(b,roundTrip.dye(1));
                assertEquals(c,roundTrip.dye(2));
                assertEquals(d,roundTrip.dye(3));
                assertEquals(p,TartanPalette.match(3,2,new int[]{a,b,c,-1,d,-1}).orElseThrow());
            }
    }
    @Test void translatedShapeAndRepeatedColorsAccepted() {
        var p = TartanPalette.of(14,11,13,4);
        assertEquals(p,TartanPalette.match(3,3,new int[]{14,11,13,-1,4,-1,-1,-1,-1}).orElseThrow());
        assertEquals(p,TartanPalette.match(3,3,new int[]{-1,-1,-1,14,11,13,-1,4,-1}).orElseThrow());
        assertEquals(TartanPalette.of(0,0,0,0),TartanPalette.match(3,2,new int[]{0,0,0,-1,0,-1}).orElseThrow());
    }
    @Test void rejectsWrongShapeExtrasUnknownItemsAndInvalidValues() {
        assertTrue(TartanPalette.match(3,2,new int[]{1,2,3,4,-1,-1}).isEmpty());
        assertTrue(TartanPalette.match(3,2,new int[]{1,2,3,-1,4,5}).isEmpty());
        assertTrue(TartanPalette.match(3,2,new int[]{1,2,3,-2,4,-1}).isEmpty());
        assertTrue(TartanPalette.match(3,2,new int[]{1,2,3,-1,16,-1}).isEmpty());
        assertTrue(TartanPalette.match(3,2,new int[]{1,2,-1,-1,4,-1}).isEmpty());
        assertTrue(TartanPalette.match(2,2,new int[]{1,2,3,4}).isEmpty());
        assertThrows(IllegalArgumentException.class,()->new TartanPalette(-1));
        assertThrows(IllegalArgumentException.class,()->new TartanPalette(65536));
        assertThrows(IllegalArgumentException.class,()->TartanPalette.of(0,16,0,0));
        assertThrows(IllegalArgumentException.class,()->TartanPalette.of(0,0,-1,0));
        assertThrows(IllegalArgumentException.class,()->TartanPalette.of(0,0,0,0).dye(4));
    }
}
