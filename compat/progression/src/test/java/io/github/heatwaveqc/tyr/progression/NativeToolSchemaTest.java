package io.github.heatwaveqc.tyr.progression;

import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NativeToolSchemaTest {
    private static final String FORM="""
        {"schema":1,"rank_xp":[0],"skills":[{"id":"test:form",
         "activation":{"mode":"toggle","exclusive_group":"test:forms"},
         "effects":[{"type":"tool_form","empty_hand":true,"target":"*",
                     "params":{"form":"shears","entity_target":"minecraft:sheep"}}]}]}
        """;
    private static final String HARVEST="""
        {"schema":1,"rank_xp":[0],"milestones":[{"id":"test:wood",
        "strict_order":true,"criteria":[{"event":"material_harvest_trial","target":"minecraft:wooden_pickaxe","count":5}]}],
        "skills":[{"id":"test:harvest","auto_achievements":["test:wood"],"effects":[
        {"type":"native_harvest","empty_hand":true,"target":"#minecraft:mineable/pickaxe","params":{"tool":"minecraft:wooden_pickaxe"}}]}]}
        """;
    private static void read(String json){
        TestPaths.read("test:path",JsonParser.parseString(json).getAsJsonObject());
    }
    @Test void toolFormMustBeAuthorizedToggle(){
        assertDoesNotThrow(()->read(FORM));
        assertThrows(IllegalArgumentException.class,()->read(FORM.replace("\"exclusive_group\":\"test:forms\"","\"cooldown_ticks\":0")));
        assertThrows(IllegalArgumentException.class,()->read(FORM.replace("\"form\":\"shears\"","\"form\":\"flint_and_steel\"")));
        assertThrows(IllegalArgumentException.class,()->read(FORM.replace("\"empty_hand\":true","\"empty_hand\":false")));
        assertThrows(IllegalArgumentException.class,()->read(FORM.replace("\"form\":\"shears\"","\"form\":\"hoe\"")));
    }
    @Test void materialTrialRequiresStrictOrderAndExactTool(){
        assertDoesNotThrow(()->read(HARVEST));
        assertThrows(IllegalArgumentException.class,()->read(HARVEST.replace("\"strict_order\":true","\"strict_order\":false")));
        assertThrows(IllegalArgumentException.class,()->read(HARVEST.replace("\"target\":\"minecraft:wooden_pickaxe\"","\"target\":\"*\"")));
        assertThrows(IllegalArgumentException.class,()->read(HARVEST.replace("\"tool\":\"minecraft:wooden_pickaxe\"","\"tool\":\"#test:tools\"")));
    }
}
