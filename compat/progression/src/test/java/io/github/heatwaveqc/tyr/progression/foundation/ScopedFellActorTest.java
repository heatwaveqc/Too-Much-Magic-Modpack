package io.github.heatwaveqc.tyr.progression.foundation;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ScopedFellActorTest {
    @Test void nullNativeCreditOnlyResolvesInsideExactFell(){
        var scope=new ScopedFellActor<Object,Object,Integer>();
        var actor=new Object();var other=new Object();var level=new Object();
        assertNull(scope.actor(null,level,1));
        scope.run(actor,level,1,()->{
            assertSame(actor,scope.actor(null,level,1));
            assertSame(other,scope.actor(other,level,1));
            assertNull(scope.actor(null,level,2));
            assertNull(scope.actor(null,new Object(),1));
            scope.run(other,level,2,()->assertSame(other,scope.actor(null,level,2)));
            assertSame(actor,scope.actor(null,level,1));
        });
        assertNull(scope.actor(null,level,1));
    }
    @Test void exceptionalNativeBreakDoesNotLeakSight(){
        var scope=new ScopedFellActor<Object,Object,Integer>();
        var level=new Object();
        assertThrows(IllegalStateException.class,()->scope.run(new Object(),level,1,()->{
            throw new IllegalStateException("native failure");
        }));
        assertNull(scope.actor(null,level,1));
    }
}
