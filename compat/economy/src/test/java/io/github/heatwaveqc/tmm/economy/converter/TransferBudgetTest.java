package io.github.heatwaveqc.tmm.economy.converter;
import java.math.BigInteger;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class TransferBudgetTest {
    @Test void pool() {
        var p = TransferBudget.plan(0, 1_000_000, BigInteger.TEN.pow(40),20,1);
        assertEquals(1_000_000,p.units()); assertEquals(BigInteger.valueOf(50000),p.cost());
        assertEquals(BigInteger.ZERO,p.refundForAccepted(1_000_000));
    }
    @Test void jar() {
        var p = TransferBudget.plan(0,10000,BigInteger.valueOf(7500),4,3);
        assertEquals(10000,p.units()); assertEquals(BigInteger.valueOf(7500),p.cost());
    }
    @Test void insufficientAndResidues() {
        assertEquals(0,TransferBudget.plan(999_981,1_000_000,BigInteger.TEN,20,1).units());
        assertEquals(0,TransferBudget.plan(9997,10000,BigInteger.TEN,4,3).units());
        assertEquals(0,TransferBudget.plan(0,10000,BigInteger.TWO,4,3).units());
        assertEquals(4,TransferBudget.plan(0,10000,BigInteger.valueOf(5),4,3).units());
        assertEquals(0,TransferBudget.plan(0,10000,BigInteger.ZERO,4,3).units());
    }
    @Test void refunds() {
        var p=TransferBudget.plan(0,100,BigInteger.valueOf(75),4,3);
        assertEquals(BigInteger.valueOf(75),p.refundForAccepted(0));
        assertEquals(BigInteger.valueOf(69),p.refundForAccepted(8));
        assertEquals(BigInteger.valueOf(69),p.refundForAccepted(5));
        assertEquals(BigInteger.ZERO,p.refundForAccepted(-1));
        assertEquals(BigInteger.ZERO,p.refundForAccepted(101));
    }
    @Test void invalid() {
        assertEquals(0,TransferBudget.plan(-1,100,BigInteger.TEN,20,1).units());
        assertEquals(0,TransferBudget.plan(101,100,BigInteger.TEN,20,1).units());
        assertThrows(IllegalArgumentException.class,()->TransferBudget.plan(0,100,BigInteger.TEN,0,1));
    }
}
