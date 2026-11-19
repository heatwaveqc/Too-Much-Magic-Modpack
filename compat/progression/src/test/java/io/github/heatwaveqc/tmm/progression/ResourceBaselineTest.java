package io.github.heatwaveqc.tmm.progression;

import io.github.heatwaveqc.tmm.progression.core.MxpMath;
import io.github.heatwaveqc.tmm.progression.mxp.core.ResourceBaselines;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ResourceBaselineTest {
    @Test void hexUsesFixedEmcDerivedMediaNotRoundedItemPrices() {
        double pool = ResourceBaselines.HEX_EMC_MEDIA_REFERENCE;
        assertEquals(1_562_500.0, pool);
        assertEquals(1000.0, MxpMath.normalizedBase(pool, pool), 1e-9);
        assertEquals(6.4, MxpMath.normalizedBase(10_000.0, pool), 1e-9);
        assertEquals(32.0, MxpMath.normalizedBase(50_000.0, pool), 1e-9);
        assertEquals(64.0, MxpMath.normalizedBase(100_000.0, pool), 1e-9);
    }

    @Test void evilCraftUsesFixedPortableBloodExtractorReference() {
        double pool = ResourceBaselines.EVILCRAFT_PORTABLE_BLOOD;
        assertEquals(5000.0, pool);
        assertEquals(1.0, MxpMath.normalizedBase(5.0, pool), 1e-9);
        assertEquals(0.2, MxpMath.normalizedBase(1.0, pool), 1e-9);
        assertEquals(50.0, MxpMath.normalizedBase(250.0, pool), 1e-9);
        assertEquals(1000.0, MxpMath.normalizedBase(pool, pool), 1e-9);
    }
    @org.junit.jupiter.api.Test void approvedGoetyAndPrimalReferences(){
        org.junit.jupiter.api.Assertions.assertEquals(500.0,ResourceBaselines.GOETY_EMC_SOUL_REFERENCE);
        org.junit.jupiter.api.Assertions.assertEquals(50_000.0,ResourceBaselines.PRIMAL_EMC_CENTIMANA_REFERENCE);
    }
}
