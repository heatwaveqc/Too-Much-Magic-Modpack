package io.github.heatwaveqc.tyr.progression;

import io.github.heatwaveqc.tyr.progression.foundation.NativeAttackScope;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OwnedMagicDamageScopeTest {
    @Test void exactSourceAndVictimAndProviderOnly(){
        var scope=new NativeAttackScope();
        Object player=new Object(),victim=new Object(),source=new Object();
        scope.around("boundary",player,victim,source,()->{
            assertSame(player,scope.owner("boundary",victim,source));
            assertNull(scope.owner("boundary",victim,new Object()));
            assertNull(scope.owner("boundary",new Object(),source));
            assertNull(scope.owner("well",victim,source));
            return true;
        });
        assertNull(scope.owner("boundary",victim,source));
    }
    @Test void independentProvidersNeverBorrowOwnership(){
        var scope=new NativeAttackScope();
        Object caster=new Object(),priest=new Object(),victim=new Object(),source=new Object();
        scope.around("mahou_drain_life",caster,victim,source,()->{
            assertSame(caster,scope.owner("mahou_drain_life",victim,source));
            scope.around("neo_well_of_suffering",priest,victim,source,()->{
                assertSame(priest,scope.owner("neo_well_of_suffering",victim,source));
                assertNull(scope.owner("mahou_drain_life",victim,source));
                assertNull(scope.owner("torment_nexus",victim,source));
                return true;
            });
            assertSame(caster,scope.owner("mahou_drain_life",victim,source));
            assertNull(scope.owner("neo_well_of_suffering",victim,source));
            return true;
        });
        assertNull(scope.owner("mahou_drain_life",victim,source));
        assertNull(scope.owner("neo_well_of_suffering",victim,source));
    }
    @Test void nestingAndExceptionRestoreOwner(){
        var scope=new NativeAttackScope();
        Object first=new Object(),second=new Object(),victim=new Object(),source=new Object();
        scope.around("boundary",first,victim,source,()->{
            assertThrows(IllegalStateException.class,()->scope.around("boundary",second,victim,source,()->{
                assertSame(second,scope.owner("boundary",victim,source));
                scope.around("boundary",null,victim,source,()->{
                    assertNull(scope.owner("boundary",victim,source));
                    return false;
                });
                throw new IllegalStateException();
            }));
            assertSame(first,scope.owner("boundary",victim,source));
            return true;
        });
        assertNull(scope.owner("boundary",victim,source));
    }
}
