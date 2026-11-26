package io.github.heatwaveqc.tyr.progression;

import com.google.gson.JsonParser;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.io.InputStreamReader;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NativeSkillKeyCatalogTest {
    @Test void generatedCatalogMatchesShippedActivationNodesAndTranslations() throws Exception {
        var expected=new TreeSet<String>();
        Path root=Path.of("src","main","resources","data");
        try(var files=Files.walk(root)){
            for(var file:files.filter(Files::isRegularFile).toList()){
                String rel=root.relativize(file).toString().replace('\\','/');
                String[] parts=rel.split("/",4);
                if(parts.length!=4||!parts[1].equals("tyr_progression")||!parts[2].equals("paths")||!rel.endsWith(".json"))continue;
                String path=parts[0]+":"+parts[3].substring(0,parts[3].length()-5);
                var doc=JsonParser.parseString(Files.readString(file)).getAsJsonObject();
                if(!doc.has("skills"))continue;
                for(var el:doc.getAsJsonArray("skills")){
                    var node=el.getAsJsonObject();
                    if(node.has("activation"))assertTrue(expected.add(path+"|"+node.get("id").getAsString()));
                }
            }
        }
        var actual=new TreeSet<String>();
        var keys=new HashSet<String>();
        try(var catalog=getClass().getResourceAsStream("/tyr_progression/skill_key_catalog.json");
            var lang=getClass().getResourceAsStream("/assets/tyr_progression_keys/lang/en_us.json")){
            assertNotNull(catalog,"Catalog must be packaged by processResources");
            assertNotNull(lang,"Generated Controls labels must be packaged");
            var rows=JsonParser.parseReader(new InputStreamReader(catalog,StandardCharsets.UTF_8)).getAsJsonArray();
            var labels=JsonParser.parseReader(new InputStreamReader(lang,StandardCharsets.UTF_8)).getAsJsonObject();
            for(var el:rows){
                var row=el.getAsJsonObject();
                String path=row.get("path").getAsString(),skill=row.get("skill").getAsString();
                String identity=path+"|"+skill,key=row.get("key").getAsString(),category=row.get("category").getAsString();
                assertTrue(actual.add(identity));assertTrue(keys.add(key));
                assertEquals("key.tyr_progression.activation."+UUID.nameUUIDFromBytes(identity.getBytes(StandardCharsets.UTF_8)),key);
                assertEquals("key.categories.tyr_progression."+UUID.nameUUIDFromBytes(path.getBytes(StandardCharsets.UTF_8)),category);
                assertFalse(labels.get(key).getAsString().isBlank());
                assertFalse(labels.get(category).getAsString().isBlank());
            }
        }
        assertEquals(expected,actual,"No missing or extra native key mappings");
    }
}
