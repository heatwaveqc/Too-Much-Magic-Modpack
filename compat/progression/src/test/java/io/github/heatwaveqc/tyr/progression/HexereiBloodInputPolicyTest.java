package io.github.heatwaveqc.tyr.progression;

import io.github.heatwaveqc.tyr.progression.runtime.blood.HexereiBloodInputPolicy;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pure accounting/allowlist tests. The Hexerei native fill / direct-item
 * injection and registration are assessed by source ABI + authorized game QA.
 */
class HexereiBloodInputPolicyTest {
    @Test
    void canonicalPhysicalBloodIsEligibleOnInput() {
        assertTrue(HexereiBloodInputPolicy.needsInternalConversion("evilcraft:blood"));
    }
    @Test
    void nativeIronsOrdinaryBloodIsEligible() {
        assertTrue(HexereiBloodInputPolicy.needsInternalConversion("irons_spellbooks:blood"));
    }
    @Test
    void doesNotNormalizeOtherResourceSystemsOrWater() {
        assertFalse(HexereiBloodInputPolicy.needsInternalConversion("neovitae:essentia_vitae_source"));
        assertFalse(HexereiBloodInputPolicy.needsInternalConversion("minecraft:water"));
        assertFalse(HexereiBloodInputPolicy.needsInternalConversion("hexerei:blood_fluid"));
        assertFalse(HexereiBloodInputPolicy.needsInternalConversion("forbidden_arcanus:blood"));
        assertFalse(HexereiBloodInputPolicy.needsInternalConversion(null));
    }
    @Test
    void neverMixWithOtherNativeCauldronFluid() {
        assertFalse(HexereiBloodInputPolicy.mayMix("minecraft:water", 1000));
        assertFalse(HexereiBloodInputPolicy.mayMix("evilcraft:blood", 1000));
        assertTrue(HexereiBloodInputPolicy.mayMix("hexerei:blood_fluid", 1000));
        assertTrue(HexereiBloodInputPolicy.mayMix("", 0));
    }
    @Test
    void exactFillsRespectTwoBucketCap() {
        assertEquals(2000, HexereiBloodInputPolicy.availableTransfer(5000, 0, 2000));
        assertEquals(1250, HexereiBloodInputPolicy.availableTransfer(5000, 750, 2000));
        assertEquals(250, HexereiBloodInputPolicy.availableTransfer(5000, 1750, 2000));
        assertEquals(0, HexereiBloodInputPolicy.availableTransfer(250, 2000, 2000));
    }
    @Test
    void partialSourceAndVerySmallAmountsAreExact() {
        for(int amount : new int[]{1, 249, 250, 999, 1000}) {
            assertEquals(amount, HexereiBloodInputPolicy.availableTransfer(amount, 0, 2000));
        }
        assertEquals(100, HexereiBloodInputPolicy.availableTransfer(100, 1250, 2000));
    }
    @Test
    void impossibleOrNegativeNativeHandlerStateFailsClosed() {
        assertEquals(0, HexereiBloodInputPolicy.availableTransfer(-1, 0, 2000));
        assertEquals(0, HexereiBloodInputPolicy.availableTransfer(500, -1, 2000));
        assertEquals(0, HexereiBloodInputPolicy.availableTransfer(500, 2001, 2000));
        assertEquals(0, HexereiBloodInputPolicy.availableTransfer(500, 0, 0));
    }
}
