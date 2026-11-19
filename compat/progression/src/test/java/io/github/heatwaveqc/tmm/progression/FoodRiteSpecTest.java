package io.github.heatwaveqc.tmm.progression;
import io.github.heatwaveqc.tmm.progression.data.FoodRiteSpec;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class FoodRiteSpecTest {
 @Test void validatesSeparateOneTimeTrigger(){var spec=FoodRiteSpec.parse(JsonParser.parseString("""
 {"enrollment":"food","food_rite":{"target":"#test:foods","when":{"type":"always"}}}
 """).getAsJsonObject());assertEquals("#test:foods",spec.target());assertEquals("",spec.completionEvent());}
 @Test void rejectMissingOrWildcard(){for(String raw:new String[]{
 "{\"enrollment\":\"food\"}",
 "{\"enrollment\":\"food\",\"food_rite\":{\"target\":\"*\",\"when\":{\"type\":\"always\"}}}"})
 assertThrows(IllegalArgumentException.class,()->FoodRiteSpec.parse(JsonParser.parseString(raw).getAsJsonObject()));}
}
