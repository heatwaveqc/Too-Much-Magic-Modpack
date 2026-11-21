package io.github.heatwaveqc.tyr.progression;
import io.github.heatwaveqc.tyr.progression.foundation.FormCombination;
import org.junit.jupiter.api.Test;
import static io.github.heatwaveqc.tyr.progression.foundation.Condition.Result.*;
import static org.junit.jupiter.api.Assertions.*;
class FormCombinationTest{
 @Test void eitherLichSharesSingleEligibility(){assertEquals(PASS,FormCombination.lich(PASS,FAIL));assertEquals(PASS,FormCombination.lich(PASS,PASS));assertEquals(UNAVAILABLE,FormCombination.lich(FAIL,UNAVAILABLE));}
 @Test void rejectTwoLichDragonAndUnknown(){assertEquals(PASS,FormCombination.dracolich(PASS,PASS,FAIL));assertEquals(PASS,FormCombination.dracolich(PASS,FAIL,PASS));assertEquals(FAIL,FormCombination.dracolich(PASS,PASS,PASS));assertEquals(UNAVAILABLE,FormCombination.dracolich(PASS,PASS,UNAVAILABLE));assertEquals(FAIL,FormCombination.archlich(PASS,PASS,PASS));assertEquals(PASS,FormCombination.archlich(FAIL,PASS,PASS));}
}
