package io.github.heatwaveqc.tyr.progression;
import io.github.heatwaveqc.tyr.progression.data.ElixirSpec;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class ElixirSpecTest {
    @Test void bindsOnlyOnce(){
        assertEquals("",ElixirSpec.nextBinding("","test:tea",false));
        assertEquals("test:beer",ElixirSpec.nextBinding("","test:beer",true));
        assertEquals("test:beer",ElixirSpec.nextBinding("test:beer","test:rum",true));
        assertEquals("test:beer",ElixirSpec.nextBinding("test:beer","test:beer",false));
    }
    @Test void excludesOwnerDeniedDrinks(){
        assertTrue(ElixirSpec.excluded("brewinandchewin:kombucha"));
        assertTrue(ElixirSpec.excluded("witchery:blood_wine"));
        assertTrue(ElixirSpec.excluded("other:aged_bloodwine_bottle"));
        assertFalse(ElixirSpec.excluded("cocktailsdelight:red_wine"));
        assertFalse(ElixirSpec.excluded("pamhc2foodextended:pinacoladaitem"));
    }
}
