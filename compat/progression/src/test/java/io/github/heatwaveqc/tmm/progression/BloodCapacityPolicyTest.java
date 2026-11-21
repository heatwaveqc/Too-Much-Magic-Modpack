package io.github.heatwaveqc.tmm.progression;
import io.github.heatwaveqc.tmm.progression.foundation.BloodCapacityPolicy;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class BloodCapacityPolicyTest{
 @Test void capacityPreservesFillFractionAndDoesNotCompound(){
    var a=BloodCapacityPolicy.reconcile(900,900,450,0,0,.2);
    assertEquals(new BloodCapacityPolicy.Plan(1080,540,true),a);
    assertEquals(a,BloodCapacityPolicy.reconcile(900,1080,540,900,1080,.2));
    assertEquals(new BloodCapacityPolicy.Plan(900,450,false),BloodCapacityPolicy.reconcile(900,1080,540,900,1080,0));
 }
 @Test void nativeResetDoesNotRefillAndForeignMaximumIsNotOwned(){
    assertEquals(new BloodCapacityPolicy.Plan(1080,450,true),BloodCapacityPolicy.reconcile(900,900,450,900,1080,.2));
    assertNull(BloodCapacityPolicy.reconcile(900,1000,500,0,0,.2));
    assertEquals(new BloodCapacityPolicy.Plan(1080,0,true),BloodCapacityPolicy.reconcile(900,900,0,0,0,.2));
 }
}
