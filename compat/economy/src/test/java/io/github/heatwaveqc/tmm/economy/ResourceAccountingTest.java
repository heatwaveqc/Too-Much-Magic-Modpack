package io.github.heatwaveqc.tmm.economy;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class ResourceAccountingTest {
    @Test void exactResourceCosts() {
        var c = ResourceAccounting.Rate.MANA.of(3000);
        assertEquals(1,c.batches()); assertEquals(150,c.emcUnits());
        c = ResourceAccounting.Rate.MANA.of(1);
        assertEquals(20,c.batches()); assertEquals(1,c.emcUnits());
        assertEquals(40,c.scaleCount(2));
        c = ResourceAccounting.Rate.SOURCE.of(1).plus(ResourceAccounting.Rate.BLOOD_MB.of(1));
        assertEquals(20,c.batches()); assertEquals(19,c.emcUnits());
        assertEquals(2,ResourceAccounting.Rate.PRIMAL_CENTIMANA.of(100).emcUnits());
        assertEquals(1,ResourceAccounting.Rate.FE.of(200).emcUnits());
        assertThrows(IllegalArgumentException.class, () -> ResourceAccounting.Rate.MANA.of(-1));
        assertThrows(ArithmeticException.class, () -> ResourceAccounting.Rate.SOUL.of(Long.MAX_VALUE).emcUnits());
        assertThrows(ArithmeticException.class, () -> ResourceAccounting.Rate.MANA.of(1).scaleCount(Integer.MAX_VALUE));
    }
    @Test void decimalSpiritusCosts() {
        var c = ResourceAccounting.Rate.RAW_SPIRITUS.ofDecimal(0.125)
                .plus(ResourceAccounting.Rate.ASPECTED_SPIRITUS.ofDecimal(1.2));
        assertEquals(101,c.emcUnits()); assertEquals(40,c.batches());
        assertEquals(80,c.scaleCount(2)); assertEquals(-40,c.scaleCount(-1));
        assertEquals(1,ResourceAccounting.Rate.RAW_SPIRITUS.ofDecimal(0.25).emcUnits());
        assertEquals(4,ResourceAccounting.Rate.RAW_SPIRITUS.ofDecimal(0.25).batches());
        assertThrows(IllegalArgumentException.class, () -> ResourceAccounting.Rate.RAW_SPIRITUS.ofDecimal(Double.NaN));
        assertThrows(IllegalArgumentException.class, () -> ResourceAccounting.Rate.RAW_SPIRITUS.ofDecimal(Double.POSITIVE_INFINITY));
        assertThrows(IllegalArgumentException.class, () -> ResourceAccounting.Rate.RAW_SPIRITUS.ofDecimal(-0.1));
        assertThrows(ArithmeticException.class, () -> ResourceAccounting.Rate.RAW_SPIRITUS.ofDecimal(0.000000000001).batches());
    }
}
