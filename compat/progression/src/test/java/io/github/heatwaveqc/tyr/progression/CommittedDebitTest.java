package io.github.heatwaveqc.tyr.progression;
import io.github.heatwaveqc.tyr.progression.mxp.core.CommittedDebit;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class CommittedDebitTest{
 @Test void actualFundsNotOverdrawOrPreview(){
   assertEquals(100,CommittedDebit.measured(500,400,50,50,100));
   assertEquals(150,CommittedDebit.measured(100,-100,100,50,200));
   assertEquals(0,CommittedDebit.measured(500,500,-1,-1,100));
   assertEquals(0,CommittedDebit.measured(500,500,50,50,100));
   assertEquals(0,CommittedDebit.measured(500,300,50,50,100));
   assertEquals(0,CommittedDebit.measured(500,Double.NaN,50,50,100));
 }
}
