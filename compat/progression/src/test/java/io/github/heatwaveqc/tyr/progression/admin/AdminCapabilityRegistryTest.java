package io.github.heatwaveqc.tyr.progression.admin;

import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static io.github.heatwaveqc.tyr.progression.admin.AdminCapabilityRegistry.*;

class AdminCapabilityRegistryTest {
    private static final String ID="test:binding";
    private static final class Target {
        final Map<String,String> values=new HashMap<>();
    }
    private static Handler<Target> handler(){
        return new Handler<>(){
            public String description(){return "example binding";}
            public Collection<String> keys(Target t){return t.values.keySet();}
            public String inspect(Target t,String key){return t.values.get(key);}
            public List<String> validate(Target t,String key){
                return t.values.get(key).isBlank()?List.of("empty binding"):List.of();
            }
            public Set<Action> actions(){return Set.of(Action.RESET);}
            public Change plan(Target t,String key,Action action){
                String before=t.values.get(key);
                return new Change(before,"absent",()->{
                    if(!Objects.equals(t.values.get(key),before))throw new IllegalStateException("changed");
                    t.values.remove(key);
                });
            }
        };
    }

    @Test void previewDoesNotMutateAndConfirmReplansAgainstCurrentTarget(){
        var registry=new AdminCapabilityRegistry<Target>();
        registry.register(ID,handler());
        var first=new Target(); var second=new Target();
        first.values.put("test:drink","tea"); second.values.put("test:drink","water");
        var preview=registry.plan(ID,first,"test:drink",Action.RESET);
        assertEquals("tea",preview.before());
        assertEquals("tea",registry.inspect(ID,first,"test:drink"));
        first.values.put("test:drink","juice");
        assertThrows(IllegalStateException.class,()->preview.commit().run());
        var confirmed=registry.plan(ID,first,"test:drink",Action.RESET);
        assertEquals("juice",confirmed.before());
        confirmed.commit().run();
        assertTrue(registry.keys(ID,first).isEmpty());
        assertEquals("water",registry.inspect(ID,second,"test:drink"));
    }

    @Test void unknownTargetsDuplicateIdsAndUnsupportedOperationsFailClosed(){
        var registry=new AdminCapabilityRegistry<Target>();
        registry.register(ID,handler());
        var target=new Target();
        target.values.put("test:z","");target.values.put("test:a","ok");
        assertEquals(List.of("test:a","test:z"),registry.keys(ID,target));
        assertEquals(List.of("empty binding"),registry.validate(ID,target,"test:z"));
        assertEquals(List.of(),registry.validate(ID,target,"test:a"));
        assertThrows(IllegalArgumentException.class,()->registry.register(ID,handler()));
        assertThrows(IllegalArgumentException.class,()->registry.register("invalid",handler()));
        assertThrows(IllegalArgumentException.class,()->registry.inspect("test:missing",target,"test:z"));
        assertThrows(IllegalArgumentException.class,()->registry.plan(ID,target,"test:missing",Action.RESET));
        assertThrows(IllegalArgumentException.class,()->registry.plan(ID,target,"test:z",Action.RECONCILE));
        assertEquals(2,target.values.size());
    }

    @Test void numericActionsRequireExplicitSupportAndValidAmount(){
        var registry=new AdminCapabilityRegistry<Target>();
        registry.register(ID,handler());
        var target=new Target();target.values.put("test:drink","tea");
        assertThrows(IllegalArgumentException.class,()->registry.plan(ID,target,"test:drink",Action.SET,3));
        assertThrows(IllegalArgumentException.class,()->registry.plan(ID,target,"test:drink",Action.RESET,3));
        assertThrows(IllegalArgumentException.class,()->registry.plan(ID,target,"test:drink",Action.ADD,-1));
        assertTrue(Action.SET.requiresAmount());assertFalse(Action.FILL.requiresAmount());
        assertEquals("tea",registry.inspect(ID,target,"test:drink"));
    }

    @Test void bulkDiagnosisIsBoundedAndReadOnly(){
        var registry=new AdminCapabilityRegistry<Target>();
        registry.register(ID,handler());
        var t=new Target();t.values.put("test:a","");t.values.put("test:b","");t.values.put("test:c","ok");
        var result=registry.diagnose(t,1,2);
        assertEquals(1,result.checked());assertEquals(2,result.omitted());
        assertEquals(List.of(new Finding(ID,"test:a","empty binding")),result.findings());
        assertEquals(3,t.values.size());
    }
    @Test void brokenHandlerDoesNotHideOtherFindings(){
        var registry=new AdminCapabilityRegistry<Target>();
        registry.register("test:a_broken",new Handler<>(){
            public String description(){return "broken";}
            public Collection<String> keys(Target t){throw new IllegalStateException("broken");}
            public String inspect(Target t,String key){return "";}
            public List<String> validate(Target t,String key){return List.of();}
        });
        registry.register(ID,handler());
        var t=new Target();t.values.put("test:drink","");
        var result=registry.diagnose(t,3,6);
        assertEquals(1,result.checked());assertEquals(2,result.findings().size());
        assertEquals("test:a_broken",result.findings().getFirst().capability());
        assertEquals(ID,result.findings().getLast().capability());
    }
}
