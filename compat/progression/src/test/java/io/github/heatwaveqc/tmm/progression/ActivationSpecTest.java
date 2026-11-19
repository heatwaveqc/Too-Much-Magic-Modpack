package io.github.heatwaveqc.tmm.progression;
import io.github.heatwaveqc.tmm.progression.data.ActivationSpec;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class ActivationSpecTest{
 @Test void timedStateUsesCooldownAfterEnd(){
    var spec=new ActivationSpec("timed",100,200);
    var state=ActivationSpec.State.empty().request(spec,10);
    assertTrue(state.active(109));assertFalse(state.active(110));
    assertEquals(state,state.request(spec,110));assertEquals(state,state.request(spec,309));
    assertTrue(state.request(spec,310).active(310));
    assertEquals(310,state.stop().ready());
    assertEquals(state,ActivationSpec.State.decode(state.encode()));
 }
 @Test void toggleRetainsCooldownAndOwnershipIsNotStoredHere(){
    var spec=new ActivationSpec("toggle",0,20);
    var state=ActivationSpec.State.empty().request(spec,5);
    assertTrue(state.active(5));var off=state.request(spec,6);
    assertFalse(off.active(6));assertEquals(off,off.request(spec,24));
    assertTrue(off.request(spec,25).active(25));
 }
}
