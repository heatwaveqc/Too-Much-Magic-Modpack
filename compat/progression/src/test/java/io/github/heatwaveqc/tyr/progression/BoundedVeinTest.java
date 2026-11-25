package io.github.heatwaveqc.tyr.progression;
import io.github.heatwaveqc.tyr.progression.foundation.BoundedVein;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
class BoundedVeinTest {
 @Test void neverWalksThroughDeniedNodes(){
    List<Integer> broken=new ArrayList<>();
    int count=BoundedVein.walk(0,8,64,i->List.of(i-1,i+1),i->{if(i<0||i==2)return false;broken.add(i);return true;});
    assertEquals(1,count);assertEquals(List.of(1),broken);
 }
 @Test void radialPlanesAreBoundedCenteredAndExcludeOrigin(){
    for(var axis:BoundedVein.Axis.values())for(int radius=1;radius<=3;radius++){
        var plane=BoundedVein.plane(axis,radius);
        assertEquals((2*radius+1)*(2*radius+1)-1,plane.size());
        assertEquals(plane.size(),new HashSet<>(plane).size());
        assertFalse(plane.contains(new BoundedVein.Offset(0,0,0)));
        for(var o:plane){
            assertTrue(Math.abs(o.x())<=radius&&Math.abs(o.y())<=radius&&Math.abs(o.z())<=radius);
            assertEquals(0,switch(axis){case X->o.x();case Y->o.y();case Z->o.z();});
        }
    }
    assertThrows(IllegalArgumentException.class,()->BoundedVein.plane(BoundedVein.Axis.X,4));
 }
 @Test void radialHardnessRejectsUnbreakableAndTooHardBlocks(){
    assertTrue(BoundedVein.radialHardness(1.5,4.5,3));
    assertFalse(BoundedVein.radialHardness(.5,50,3));
    assertFalse(BoundedVein.radialHardness(0,1,3));
    assertFalse(BoundedVein.radialHardness(2,-1,3));
    assertFalse(BoundedVein.radialHardness(2,0,3));
    assertFalse(BoundedVein.radialHardness(2,Double.NaN,3));
 }
 @Test void capsAndDeduplicates(){
    List<Integer> broken=new ArrayList<>();
    assertEquals(3,BoundedVein.walk(0,3,64,i->List.of(i-1,i+1,i),i->{broken.add(i);return true;}));
    assertEquals(3,new HashSet<>(broken).size());assertFalse(broken.contains(0));
 }
}
