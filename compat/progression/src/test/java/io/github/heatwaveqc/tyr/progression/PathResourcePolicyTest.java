package io.github.heatwaveqc.tyr.progression;
import io.github.heatwaveqc.tyr.progression.runtime.resources.*;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

final class PathResourcePolicyTest {
    static final String A="test:fury",B="test:biomass";
    private ResourcePolicy.Change change(String id,ResourcePolicy.Operation op,long amount){
        return new ResourcePolicy.Change(id,0,100,op,amount);
    }
    @Test void capAndDepletionAreIntegerAndOverflowSafe(){
        var start=ResourceState.empty();
        var full=start.settle(List.of(change(A,ResourcePolicy.Operation.ADD,1_000_000_000_000L)),"a").orElseThrow();
        assertEquals(100,full.balances().get(A));
        var zero=full.settle(List.of(change(A,ResourcePolicy.Operation.ADD,-1_000_000)),"b").orElseThrow();
        assertEquals(0,zero.balances().get(A));
        assertFalse(zero.settle(List.of(change(A,ResourcePolicy.Operation.SPEND,1)),"c").isPresent());
        assertThrows(IllegalArgumentException.class,()->new ResourcePolicy.Change(A,0,0,ResourcePolicy.Operation.ADD,1));
        assertThrows(IllegalArgumentException.class,()->new ResourcePolicy.Change(A,0,1_000_001,ResourcePolicy.Operation.ADD,1));
    }
    @Test void atomicMultiResourceSpendAndBoundedReceiptReplay(){
        var start=ResourceState.empty().settle(List.of(change(A,ResourcePolicy.Operation.ADD,70)),"seed").orElseThrow();
        assertTrue(start.settle(List.of(change(A,ResourcePolicy.Operation.SPEND,50),
            change(B,ResourcePolicy.Operation.SPEND,1)),"cost").isEmpty());
        assertEquals(70,start.balances().get(A));
        var paid=start.settle(List.of(change(A,ResourcePolicy.Operation.SPEND,50),
            change(B,ResourcePolicy.Operation.ADD,2)),"cost").orElseThrow();
        assertEquals(20,paid.balances().get(A));
        assertEquals(2,paid.balances().get(B));
        assertTrue(paid.settle(List.of(change(A,ResourcePolicy.Operation.ADD,10)),"cost").isEmpty());
        assertEquals(2,paid.reset(Set.of(A)).balances().get(B));
        assertFalse(paid.reset(Set.of(A)).balances().containsKey(A));
        assertEquals(2,paid.reset(Set.of(A)).receipts().size());
        assertTrue(ResourceState.empty().balances().isEmpty());
    }
    @Test void integerDisplayDivisorDoesNotChangeStoredUnits(){
        assertEquals("1.25",ResourcePolicy.display(125,100));
        assertEquals("1000000",ResourcePolicy.display(1_000_000,1));
        assertEquals("0",ResourcePolicy.display(0,1000));
        assertThrows(IllegalArgumentException.class,()->ResourcePolicy.display(3,3));
    }
}
