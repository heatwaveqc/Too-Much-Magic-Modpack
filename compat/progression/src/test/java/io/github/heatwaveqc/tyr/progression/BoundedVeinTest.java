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
 @Test void capsAndDeduplicates(){
    List<Integer> broken=new ArrayList<>();
    assertEquals(3,BoundedVein.walk(0,3,64,i->List.of(i-1,i+1,i),i->{broken.add(i);return true;}));
    assertEquals(3,new HashSet<>(broken).size());assertFalse(broken.contains(0));
 }
}
