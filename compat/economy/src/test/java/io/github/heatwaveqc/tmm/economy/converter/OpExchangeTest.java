package io.github.heatwaveqc.tmm.economy.converter;

import java.math.BigInteger;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OpExchangeTest {
    private static BigInteger n(long v) { return BigInteger.valueOf(v); }

    @Test void forward() {
        assertEquals(new OpExchange.Plan(n(16),2560),OpExchange.forward(n(100),3000,n(16)));
        assertEquals(new OpExchange.Plan(n(1),160),OpExchange.forward(n(100),319,n(16)));
        assertEquals(new OpExchange.Plan(n(0),0),OpExchange.forward(n(100),159,n(16)));
        assertEquals(new OpExchange.Plan(n(1),160),OpExchange.forward(n(1),10000,n(16)));
    }
    @Test void actualLedgerFastPath() {
        assertEquals(16, OpExchange.forwardEmc(100, 3000, 16));
        assertEquals(1, OpExchange.forwardEmc(100, 319, 16));
        assertEquals(0, OpExchange.forwardEmc(100, 159, 16));
        assertEquals(0, OpExchange.forwardEmc(0, 10000, 16));
        assertEquals(Long.MAX_VALUE / 160, OpExchange.forwardEmc(Long.MAX_VALUE, Long.MAX_VALUE, Long.MAX_VALUE));
        assertThrows(IllegalArgumentException.class, () -> OpExchange.forwardEmc(-1,100,10));
    }
    @Test void reverse() {
        assertEquals(new OpExchange.Plan(n(16),4000),OpExchange.reverse(5000,n(100),n(16)));
        assertEquals(new OpExchange.Plan(n(1),250),OpExchange.reverse(499,n(100),n(16)));
        assertEquals(new OpExchange.Plan(n(0),0),OpExchange.reverse(249,n(100),n(16)));
        assertEquals(new OpExchange.Plan(n(1),250),OpExchange.reverse(5000,n(1),n(16)));
    }
    @Test void roundTripAndBounds() {
        var f=OpExchange.forward(n(100),Long.MAX_VALUE,n(100));
        assertEquals(n(64),OpExchange.reverse(f.op(),n(1000),n(1000)).emc());
        BigInteger huge=BigInteger.TEN.pow(50);
        for (var p : new OpExchange.Plan[]{OpExchange.forward(huge,Long.MAX_VALUE,huge),OpExchange.reverse(Long.MAX_VALUE,huge,huge)}) {
            assertTrue(p.op()>=0);
            assertTrue(p.emc().signum()>0);
        }
        assertThrows(IllegalArgumentException.class,()->OpExchange.reverse(-1,huge,huge));
    }
}
