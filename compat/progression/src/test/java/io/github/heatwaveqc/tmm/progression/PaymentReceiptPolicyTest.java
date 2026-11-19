package io.github.heatwaveqc.tmm.progression;
import io.github.heatwaveqc.tmm.progression.mxp.core.PaymentReceiptPolicy;
import io.github.heatwaveqc.tmm.progression.mxp.core.PortableManaPolicy;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PaymentReceiptPolicyTest {
 @Test void repeatedCallbackUsesNativeTransactionIdentity() {
   UUID id=UUID.fromString("00000000-0000-0000-0000-000000000001");
   assertEquals("ans:payment:"+id,PaymentReceiptPolicy.ansTransaction(id));
   assertEquals(PaymentReceiptPolicy.ansTransaction(id),PaymentReceiptPolicy.ansTransaction(id));
   assertNotEquals(PaymentReceiptPolicy.ansTransaction(id),PaymentReceiptPolicy.ansTransaction(UUID.fromString("00000000-0000-0000-0000-000000000002")));
 }
 @Test void rejectsHealthOverflowAndImpossibleDebit() {
   assertTrue(PaymentReceiptPolicy.fullyFunded(80,80));
   assertFalse(PaymentReceiptPolicy.fullyFunded(30,80));
   assertFalse(PaymentReceiptPolicy.fullyFunded(0,80));
   assertFalse(PaymentReceiptPolicy.bounded(81,80));
   assertFalse(PaymentReceiptPolicy.bounded(Double.NaN,80));
 }
 @Test void partialToolRequestProratesAgainstTheRequestedCost() {
   assertEquals(100,PortableManaPolicy.eligibleNominal(100,80,80,80),1e-9);
   assertEquals(50,PortableManaPolicy.eligibleNominal(100,40,40,80),1e-9);
   assertEquals(25,PortableManaPolicy.eligibleNominal(100,20,40,80),1e-9);
   assertEquals(0,PortableManaPolicy.eligibleNominal(100,81,80,80),1e-9);
   assertEquals(0,PortableManaPolicy.eligibleNominal(100,40,90,80),1e-9);
 }
}
