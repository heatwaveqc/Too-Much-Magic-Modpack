package io.github.heatwaveqc.tmm.progression.foundation;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class NativeAttackScopeTest {
    @Test void exactTargetSourceNestedAndExceptionalCleanup(){
        var scope=new NativeAttackScope();
        var player=new Object();var target=new Object();var source=new Object();var other=new Object();
        assertNull(scope.kind(player,target,source));
        assertTrue(scope.around("wolf",player,target,source,()->{
            assertEquals("wolf",scope.kind(player,target,source));
            assertNull(scope.kind(player,other,source));
            assertNull(scope.kind(player,target,other));
            assertNull(scope.kind(other,target,source));
            assertThrows(IllegalStateException.class,()->scope.around("vampire",player,target,other,()->{
                assertNull(scope.kind(player,target,source));
                assertEquals("vampire",scope.kind(player,target,other));
                throw new IllegalStateException();
            }));
            assertEquals("wolf",scope.kind(player,target,source));
            return true;
        }));
        assertNull(scope.kind(player,target,source));
        assertThrows(IllegalStateException.class,()->scope.around("wolf",player,target,source,()->{throw new IllegalStateException();}));
        assertNull(scope.kind(player,target,source));
    }
}
