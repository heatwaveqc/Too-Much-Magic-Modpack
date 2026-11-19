package io.github.heatwaveqc.tmm.progression;
import io.github.heatwaveqc.tmm.progression.data.PresentationSpec;
import io.github.heatwaveqc.tmm.progression.foundation.UtilityMath;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class TreePresentationTest{
 @Test void titlesAndLanesHaveBoundedMetadata(){
   var spec=PresentationSpec.parse(JsonParser.parseString("{\"title\":\"Nightstalker\",\"lane\":\"Bloodcraft\"}").getAsJsonObject());
   assertEquals("Nightstalker",spec.title());assertEquals("Bloodcraft",spec.lane());
   var bad=new com.google.gson.JsonObject();bad.addProperty("title","a".repeat(121));
   assertThrows(IllegalArgumentException.class,()->PresentationSpec.parse(bad));
 }
 @Test void configurableVirtualLevelCap(){
   assertEquals(8,UtilityMath.virtualLevel(10));assertEquals(10,UtilityMath.virtualLevel(10,10));assertEquals(32,UtilityMath.virtualLevel(100,50));
 }
}
