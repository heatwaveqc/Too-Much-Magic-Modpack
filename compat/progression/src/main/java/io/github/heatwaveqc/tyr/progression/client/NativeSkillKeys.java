package io.github.heatwaveqc.tyr.progression.client;

import com.google.gson.JsonParser;
import com.mojang.blaze3d.platform.InputConstants;
import io.github.heatwaveqc.tyr.progression.ProgressionMod;
import io.github.heatwaveqc.tyr.progression.network.ProgressionMenuNetwork;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

/** Immutable startup catalog. No dynamic changes to Minecraft's key mapping registry. */
final class NativeSkillKeys {
    private NativeSkillKeys(){}
    private record Entry(String path,String skill,KeyMapping key,boolean legacy){}
    private static final Map<String,Entry> entries=new LinkedHashMap<>();
    static void register(RegisterKeyMappingsEvent event,KeyMapping feign){
        if(!entries.isEmpty())return;
        // Parse and validate all entries before registering anything. Missing catalog is a
        // packaging error; leave existing controls and fallback usable rather than crash startup.
        try(var stream=NativeSkillKeys.class.getResourceAsStream("/tyr_progression/skill_key_catalog.json")){
            if(stream==null)throw new IllegalStateException("Missing generated skill key catalog");
            var rows=JsonParser.parseReader(new InputStreamReader(stream,StandardCharsets.UTF_8)).getAsJsonArray();
            if(rows.size()>4096)throw new IllegalStateException("Too many native skill mappings");
            long feigns=0;var ids=new HashSet<String>();var keys=new HashSet<String>();
            for(var el:rows){
                var row=el.getAsJsonObject();
                String path=row.get("path").getAsString(),skill=row.get("skill").getAsString();
                String key=row.get("key").getAsString(),category=row.get("category").getAsString();
                if(!id(path)||!id(skill)||!ids.add(path+"|"+skill)||!keys.add(key)
                    ||!key.equals("key.tyr_progression.activation."+uuid(path+"|"+skill))
                    ||!category.equals("key.categories.tyr_progression."+uuid(path)))
                    throw new IllegalStateException("Invalid native skill mapping");
                if(row.get("feign").getAsBoolean())feigns++;
            }
            for(var el:rows){
                var row=el.getAsJsonObject();
                String path=row.get("path").getAsString(),skill=row.get("skill").getAsString();
                boolean legacy=feigns==1&&row.get("feign").getAsBoolean();
                KeyMapping key=legacy?feign:new KeyMapping(row.get("key").getAsString(),
                    InputConstants.Type.KEYSYM,GLFW.GLFW_KEY_UNKNOWN,row.get("category").getAsString());
                if(!legacy)event.register(key);
                entries.put(path+"|"+skill,new Entry(path,skill,key,legacy));
            }
            ProgressionMod.LOGGER.info("Registered {} native Progression skill bindings",entries.size());
        }catch(java.io.IOException|RuntimeException failure){
            ProgressionMod.LOGGER.error("Native skill key catalog unavailable; remaining skills use Progression binding fallback",failure);
        }
    }
    private static boolean id(String value){return value.length()<=128&&value.matches("[a-z0-9_.-]+:[a-z0-9_./-]+");}
    private static UUID uuid(String value){return UUID.nameUUIDFromBytes(value.getBytes(StandardCharsets.UTF_8));}
    static boolean has(String path,String skill){return entries.containsKey(path+"|"+skill);}
    static String label(String path,String skill){
        var entry=entries.get(path+"|"+skill);
        return entry==null||entry.key.isUnbound()?"Unbound":entry.key.getTranslatedKeyMessage().getString();
    }
    static void tick(boolean allow){
        for(var e:entries.values()){
            if(e.legacy)continue; // Existing Feign Death hotkey uses its original server-authorized route.
            boolean pressed=false;
            while(e.key.consumeClick())pressed=true;
            if(pressed&&allow&&!e.key.isUnbound())
                PacketDistributor.sendToServer(new ProgressionMenuNetwork.Mutate(e.path,"activate_key",e.skill,""));
        }
    }
}
