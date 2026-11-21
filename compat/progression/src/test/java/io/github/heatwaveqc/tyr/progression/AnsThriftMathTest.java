package io.github.heatwaveqc.tyr.progression;

import io.github.heatwaveqc.tyr.progression.mxp.core.AnsThriftMath;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AnsThriftMathTest {
    private static AnsThriftMath.Leg ars(double n){return new AnsThriftMath.Leg("ARS_MANA",n);}
    private static AnsThriftMath.Leg iron(double n){return new AnsThriftMath.Leg("IRONS_MANA",n);}
    private static List<AnsThriftMath.Leg> price(double rate,AnsThriftMath.Leg... legs){
        return AnsThriftMath.discount(List.of(legs),AnsThriftMath.factor(rate));
    }
    @Test void bothSchoolsUseSameRateRegardlessOfWhichPoolPays(){
        assertEquals(List.of(ars(15)),price(.25,ars(20)));
        assertEquals(List.of(iron(15)),price(.25,iron(20)));
        assertEquals(List.of(iron(19)),price(.25,iron(25)));
        assertEquals(List.of(iron(45)),price(.25,iron(60)));
    }
    @Test void splitKeepsUnitsOrderAndNativeZeroLegs(){
        assertEquals(List.of(ars(8),iron(15)),price(.25,ars(10),iron(20)));
        assertEquals(List.of(ars(0),iron(1)),price(.95,ars(0),iron(20)));
        assertEquals(List.of(ars(1),iron(1)),price(.95,ars(1),iron(1)));
        assertEquals(List.of(ars(0)),price(.25,ars(0)));
    }
    @Test void roundAndCapDoNotCreateFreeManaOrPriceIncrease(){
        assertEquals(.05,AnsThriftMath.factor(1));
        assertEquals(1,AnsThriftMath.factor(Double.NaN));
        assertEquals(List.of(iron(1)),price(.95,iron(20)));
        assertEquals(List.of(iron(5)),price(.95,iron(100)));
        assertEquals(List.of(iron(1)),price(.5,iron(1)));
        assertEquals(List.of(iron(20)),price(0,iron(20)));
        assertEquals(List.of(iron(3)),price(.2,iron(3)));
    }
    @Test void rejectWholeMalformedOrMixedResourceQuote(){
        assertNull(price(.25,ars(20),new AnsThriftMath.Leg("LP",10)));
        assertNull(price(.25,iron(Double.NaN)));
        assertNull(price(.25,ars(-1)));
        assertNull(price(.25,ars(.5)));
        assertNull(price(.25,iron((double)Integer.MAX_VALUE+1)));
        assertNull(AnsThriftMath.discount(List.of(iron(20)),0));
    }
}
