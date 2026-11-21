package io.github.heatwaveqc.tyr.progression;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.heatwaveqc.tyr.progression.data.PathJson;
import io.github.heatwaveqc.tyr.progression.model.Definition;
import java.nio.charset.StandardCharsets;
import java.io.InputStreamReader;

/** Legacy parity tests consume a read-only composed projection of independent data files. */
final class TestPaths {
    static JsonObject project(JsonObject raw) {
        if(raw.has("rank_xp")||!raw.has("xp_channel"))return raw;
        String id=raw.get("xp_channel").getAsString();
        String[] parts=id.split(":",2);
        String resource="data/"+parts[0]+"/tyr_progression/experience_systems/"+parts[1]+".json";
        try(var in=TestPaths.class.getClassLoader().getResourceAsStream(resource)){
            if(in==null)throw new IllegalArgumentException("Missing XP definition "+resource);
            var xp=JsonParser.parseReader(new InputStreamReader(in,StandardCharsets.UTF_8)).getAsJsonObject();
            var j=raw.deepCopy();
            j.add("rank_xp",xp.get("rank_xp"));j.add("points_per_rank",xp.get("points_per_rank"));
            j.addProperty("max_level",xp.getAsJsonArray("rank_xp").size());
            j.add("activities",xp.get("sources"));
            if(xp.has("computed_xp"))j.add("computed_xp",xp.get("computed_xp"));
            return j;
        }catch(java.io.IOException e){throw new IllegalStateException(e);}
    }
    static Definition.Path read(String id,JsonObject raw){return PathJson.read(id,project(raw));}
}
