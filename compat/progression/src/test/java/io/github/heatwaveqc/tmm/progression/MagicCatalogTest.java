package io.github.heatwaveqc.tmm.progression;
import io.github.heatwaveqc.tmm.progression.data.MagicCatalog;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class MagicCatalogTest{
 @Test void expandsOneToOneAndPreservesCondition(){
   var defs=MagicCatalog.read(JsonParser.parseString("""
   {"schema":1,"stats":{"test:cost":{"attribute":"test:percent","scale":100}}}
   """).getAsJsonObject());
   var raw=JsonParser.parseString("""
   {"skills":[{"effects":[{"type":"magic_stat","target":"test:cost","per_rank":0.05,"when":{"type":"always"}}]}]}
   """).getAsJsonObject();
   var fx=MagicCatalog.expand(raw,defs).getAsJsonArray("skills").get(0).getAsJsonObject().getAsJsonArray("effects").get(0).getAsJsonObject();
   assertEquals("attribute",fx.get("type").getAsString());assertEquals("test:percent",fx.get("attribute").getAsString());
   assertEquals(5,fx.get("per_rank").getAsDouble());assertTrue(fx.has("when"));assertThrows(IllegalArgumentException.class,()->MagicCatalog.expand(raw,java.util.Map.of()));
 }
}
