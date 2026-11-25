package io.github.heatwaveqc.tyr.progression.data;

import com.google.gson.JsonParser;
import io.github.heatwaveqc.tyr.progression.core.BaselinePaths;
import io.github.heatwaveqc.tyr.progression.model.Definition;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PathCompatibilityTest {
    private static final String MONK="test:monk", HUNTER="test:hunter", DRYAD="test:dryad";
    private static Definition.Path path(String id,String group){
        return new Definition.Path(id,group,List.of(0L),0,List.of(),List.of(),List.of());
    }
    private static Map<String,Definition.Path> paths(){
        return Map.of(MONK,path(MONK,"martial"),HUNTER,path(HUNTER,"martial"),
                DRYAD,path(DRYAD,"nature"),BaselinePaths.CORE,path(BaselinePaths.CORE,"martial"));
    }
    private static com.google.gson.JsonObject json(String text){
        return JsonParser.parseString(text).getAsJsonObject();
    }

    @Test void matchingExclusiveGroupsNeedReciprocalConsent() {
        var raw=Map.of(MONK,json("{\"compatible_paths\":[\"test:hunter\"]}"),
                HUNTER,json("{}"),DRYAD,json("{}"),BaselinePaths.CORE,json("{}"));
        var oneWay=PathCompatibility.parse(raw,paths());
        assertFalse(oneWay.canCoexist(paths(),MONK,HUNTER));
        assertTrue(oneWay.canCoexist(paths(),MONK,DRYAD));
        assertTrue(oneWay.canCoexist(paths(),MONK,BaselinePaths.CORE));

        var mutual=Map.of(MONK,json("{\"compatible_paths\":[\"test:hunter\"]}"),
                HUNTER,json("{\"compatible_paths\":[\"test:monk\"]}"),
                DRYAD,json("{}"),BaselinePaths.CORE,json("{}"));
        var allowed=PathCompatibility.parse(mutual,paths());
        assertTrue(allowed.canCoexist(paths(),MONK,HUNTER));
    }

    @Test void eitherSideBlacklistWinsEvenWhenGroupsDiffer() {
        var raw=Map.of(MONK,json("{\"incompatible_paths\":[\"test:dryad\"]}"),
                HUNTER,json("{}"),DRYAD,json("{}"),BaselinePaths.CORE,json("{}"));
        var rules=PathCompatibility.parse(raw,paths());
        assertFalse(rules.canCoexist(paths(),MONK,DRYAD));
        assertFalse(rules.canCoexist(paths(),DRYAD,MONK));
    }

    @Test void unknownOrDuplicateConfigurationsAreRejected() {
        assertThrows(IllegalArgumentException.class,()->PathCompatibility.parse(
            Map.of(MONK,json("{\"compatible_paths\":[\"test:not_loaded\"]}")),paths()));
        assertThrows(IllegalArgumentException.class,()->PathCompatibility.parse(
            Map.of(MONK,json("{\"compatible_paths\":[\"test:hunter\",\"test:hunter\"]}")),paths()));
        assertThrows(IllegalArgumentException.class,()->PathCompatibility.parse(
            Map.of(MONK,json("{\"compatible_paths\":[\"test:monk\"]}")),paths()));
    }
}
