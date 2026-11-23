package io.github.heatwaveqc.tyr.progression.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.heatwaveqc.tyr.progression.core.BaselinePaths;
import io.github.heatwaveqc.tyr.progression.model.Definition;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Declarative Path-to-Path compatibility. Unknown declarations fail reload;
 * explicit exclusions win, and an exclusive-group exception needs BOTH
 * Paths' approval. Baselines/Core are handled by their lifecycle, not a
 * specialist mutual-exclusion table. Native mod forms have separate adapters.
 */
public final class PathCompatibility {
    private PathCompatibility() {}

    public record Rules(Map<String,Set<String>> allowed,Map<String,Set<String>> blocked) {
        public Rules {
            allowed=immutableSets(allowed);
            blocked=immutableSets(blocked);
        }
        private static Map<String,Set<String>> immutableSets(Map<String,Set<String>> input){
            var map=new HashMap<String,Set<String>>();
            input.forEach((key,value)->map.put(key,Set.copyOf(value)));
            return Map.copyOf(map);
        }

        public boolean canCoexist(Map<String,Definition.Path> paths,String first,String second) {
            if(first.equals(second))return true;
            if(!BaselinePaths.isSpecialist(first)||!BaselinePaths.isSpecialist(second))
                return true;
            var a=paths.get(first);
            var b=paths.get(second);
            if(a==null||b==null)return false;
            if(blocked.getOrDefault(first,Set.of()).contains(second)
                ||blocked.getOrDefault(second,Set.of()).contains(first))return false;
            if(a.exclusiveGroup().isBlank()||!a.exclusiveGroup().equals(b.exclusiveGroup()))
                return true;
            return allowed.getOrDefault(first,Set.of()).contains(second)
                && allowed.getOrDefault(second,Set.of()).contains(first);
        }
    }

    public static Rules parse(Map<String,JsonObject> rawPaths,Map<String,Definition.Path> paths) {
        var allowed=new HashMap<String,Set<String>>();
        var blocked=new HashMap<String,Set<String>>();
        for(var row:rawPaths.entrySet()){
            allowed.put(row.getKey(),parseSet(row.getKey(),"compatible_paths",row.getValue(),paths));
            blocked.put(row.getKey(),parseSet(row.getKey(),"incompatible_paths",row.getValue(),paths));
        }
        return new Rules(allowed,blocked);
    }

    private static Set<String> parseSet(String owner,String key,JsonObject raw,
                                        Map<String,Definition.Path> paths) {
        var values=new HashSet<String>();
        if(!raw.has(key))return Set.of();
        for(JsonElement entry:raw.getAsJsonArray(key)) {
            String other=entry.getAsString();
            if(!other.matches("[a-z0-9_.-]+:[a-z0-9_./-]+")
                ||other.equals(owner)||!paths.containsKey(other)||!values.add(other))
                throw new IllegalArgumentException("Invalid Path compatibility "+owner+" "+key+" "+other);
        }
        return Set.copyOf(values);
    }
}
