package io.github.heatwaveqc.tyr.progression.data;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.heatwaveqc.tyr.progression.foundation.HarvestCapabilityGraph;
import io.github.heatwaveqc.tyr.progression.runtime.NativeHarvest;
import io.github.heatwaveqc.tyr.progression.runtime.Selectors;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.LevelResource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

/** Explicit operator diagnostic, not a gameplay handler or scheduled scan. */
public final class HarvestRegistryCensus {
    private HarvestRegistryCensus(){}
    private static JsonArray strings(Collection<String> values){
        var result=new JsonArray();values.stream().sorted().forEach(result::add);return result;
    }
    public static JsonObject snapshot(String items,String blocks){
        var fingerprints=NativeHarvest.fingerprints(items,blocks);
        var groups=HarvestCapabilityGraph.plan(fingerprints);
        var byId=new HashMap<String,HarvestCapabilityGraph.Group>();
        groups.forEach(g->byId.put(g.representative(),g));
        var report=new JsonObject();
        report.addProperty("schema",1);
        report.addProperty("scope","loaded_server_registry_default_state_correct_for_drops");
        report.addProperty("item_selector",items);report.addProperty("block_selector",blocks);
        report.addProperty("tool_count",fingerprints.size());report.addProperty("group_count",groups.size());
        report.addProperty("contextual_harvest_verified",false);
        var candidates=new JsonObject();
        new TreeMap<>(fingerprints).forEach((id,states)->candidates.add(id,strings(states)));
        report.add("fingerprints",candidates);
        var omitted=new TreeSet<String>();
        for(var item:BuiltInRegistries.ITEM){
            var stack=new ItemStack(item);
            if(!stack.isEmpty()&&Selectors.item(items,stack)){
                String id=BuiltInRegistries.ITEM.getKey(item).toString();
                if(!fingerprints.containsKey(id))omitted.add(id);
            }
        }
        report.add("selected_items_without_default_tool_component",strings(omitted));
        var rows=new JsonArray();
        for(var g:groups){
            var row=new JsonObject();
            row.addProperty("representative",g.representative());
            row.add("equivalents",strings(g.equivalents()));row.add("predecessors",strings(g.predecessors()));
            row.addProperty("block_count",g.blocks().size());
            row.add("new_blocks",strings(HarvestCapabilityGraph.trainingBlocks(g,byId)));
            rows.add(row);
        }
        report.add("groups",rows);
        var trials=new JsonArray();
        for(var path:new TreeMap<>(PathRegistry.all()).values())for(var m:path.milestones())for(var c:m.criteria())
            if(c.event().equals("material_harvest_trial")){
                var row=new JsonObject();row.addProperty("path",path.id());row.addProperty("milestone",m.id());
                row.addProperty("tool",c.selector());row.addProperty("count",c.count());
                row.add("after",strings(m.after()));row.add("aliases",strings(m.completionAliases()));
                var selected=PathRegistry.generatedTrainingBlocks(path.id(),c.selector());
                if(selected!=null)row.add("configured_training_blocks",strings(selected));
                trials.add(row);
            }
        report.add("loaded_material_trials",trials);
        return report;
    }
    public static int export(CommandSourceStack source,String items,String blocks){
        Path destination=source.getServer().getWorldPath(LevelResource.ROOT).resolve("reports").resolve("tyr-harvest-registry-v1.json");
        try{
            var snapshot=snapshot(items,blocks);
            Files.createDirectories(destination.getParent());
            Path temp=Files.createTempFile(destination.getParent(),".tyr-harvest-",".json.tmp");
            try{
                Files.writeString(temp,new GsonBuilder().setPrettyPrinting().create().toJson(snapshot)+"\n",StandardCharsets.UTF_8);
                try{Files.move(temp,destination,StandardCopyOption.ATOMIC_MOVE,StandardCopyOption.REPLACE_EXISTING);}
                catch(AtomicMoveNotSupportedException unsupported){Files.move(temp,destination,StandardCopyOption.REPLACE_EXISTING);}
            }finally{Files.deleteIfExists(temp);}
            int tools=snapshot.get("tool_count").getAsInt(),groups=snapshot.get("group_count").getAsInt();
            source.sendSuccess(()->Component.literal("Harvest census: "+tools+" native tools, "+groups+" distinct nonempty capability groups; JSON at "+destination),false);
            return tools;
        }catch(IOException|RuntimeException failure){
            source.sendFailure(Component.literal("Harvest census failed: "+failure.getMessage()));
            return 0;
        }
    }
}
