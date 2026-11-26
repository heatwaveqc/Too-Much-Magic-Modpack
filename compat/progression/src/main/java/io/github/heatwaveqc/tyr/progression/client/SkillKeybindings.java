package io.github.heatwaveqc.tyr.progression.client;

import com.mojang.blaze3d.platform.InputConstants;
import io.github.heatwaveqc.tyr.progression.ProgressionMod;
import io.github.heatwaveqc.tyr.progression.network.ProgressionMenuNetwork;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

/** Client-local preference, never permission. All activations use the server skill request. */
final class SkillKeybindings {
    private SkillKeybindings(){}
    private static final int MASK=GLFW.GLFW_MOD_SHIFT|GLFW.GLFW_MOD_CONTROL|GLFW.GLFW_MOD_ALT|GLFW.GLFW_MOD_SUPER;
    private record Chord(int key,int modifiers){}
    private static final Map<String,Chord> bindings=new LinkedHashMap<>();
    private static boolean loaded,readFailed;
    private static Path file(){return Minecraft.getInstance().gameDirectory.toPath().resolve("config").resolve("tyr_progression-keybinds.properties");}
    private static boolean id(String value){return value!=null&&value.length()<=128&&value.matches("[a-z0-9_.-]+:[a-z0-9_./-]+");}
    static boolean modifierKey(int key){
        return key>=GLFW.GLFW_KEY_LEFT_SHIFT&&key<=GLFW.GLFW_KEY_RIGHT_SUPER;
    }
    private static boolean valid(Chord c){
        return c.key>=GLFW.GLFW_KEY_SPACE&&c.key<=GLFW.GLFW_KEY_LAST&&!modifierKey(c.key)
            &&c.key!=GLFW.GLFW_KEY_ESCAPE&&c.key!=GLFW.GLFW_KEY_BACKSPACE&&c.key!=GLFW.GLFW_KEY_DELETE
            &&(c.modifiers&~MASK)==0;
    }
    private static void load(){
        if(loaded)return;loaded=true;
        try{
            if(!Files.exists(file()))return;
            Properties props=new Properties();
            try(var in=Files.newInputStream(file())){props.load(in);}
            if(props.size()>4096)throw new IOException("too many bindings");
            Set<Chord> used=new HashSet<>();
            for(String row:new TreeSet<>(props.stringPropertyNames())){
                String[] parts=row.split("\\|",-1),values=props.getProperty(row).split(",",-1);
                if(parts.length!=2||!id(parts[0])||!id(parts[1])||values.length!=2)throw new IOException("invalid binding row");
                Chord chord=new Chord(Integer.parseInt(values[0]),Integer.parseInt(values[1]));
                if(!valid(chord)||!used.add(chord))throw new IOException("invalid or duplicate binding");
                bindings.put(row,chord);
            }
        }catch(IOException|RuntimeException e){
            bindings.clear();readFailed=true;
            ProgressionMod.LOGGER.warn("Cannot read Progression keybindings; preserving original file",e);
        }
    }
    private static boolean nativeConflict(int key){
        var input=InputConstants.Type.KEYSYM.getOrCreate(key);
        for(var mapping:Minecraft.getInstance().options.keyMappings)
            if(mapping!=null&&!mapping.isUnbound()&&mapping.getKey().equals(input))return true;
        return false;
    }
    static String bind(String path,String skill,int key,int modifiers){
        load();if(readFailed)return "Binding file unreadable";
        if(!id(path)||!id(skill))return "Invalid skill";
        String row=path+"|"+skill;
        var next=new LinkedHashMap<>(bindings);
        if(key==-1)next.remove(row);
        else{
            Chord chord=new Chord(key,modifiers&MASK);
            if(!valid(chord))return "Choose a different key";
            if(nativeConflict(key))return "Key already assigned in Controls";
            if(bindings.entrySet().stream().anyMatch(e->!e.getKey().equals(row)&&e.getValue().equals(chord)))
                return "Key already assigned to another skill";
            if(!next.containsKey(row)&&next.size()>=4096)return "Too many bindings";
            next.put(row,chord);
        }
        Path target=file(),temp=null;
        try{
            Files.createDirectories(target.getParent());
            Properties props=new Properties();
            next.forEach((id,c)->props.setProperty(id,c.key+","+c.modifiers));
            temp=Files.createTempFile(target.getParent(),"tyr-keybinds-",".tmp");
            try(var out=Files.newOutputStream(temp)){props.store(out,"Tyr Progression player key preferences");}
            try{Files.move(temp,target,StandardCopyOption.ATOMIC_MOVE,StandardCopyOption.REPLACE_EXISTING);}
            catch(AtomicMoveNotSupportedException e){Files.move(temp,target,StandardCopyOption.REPLACE_EXISTING);}
            bindings.clear();bindings.putAll(next);return null;
        }catch(IOException|RuntimeException e){
            ProgressionMod.LOGGER.warn("Cannot save Progression keybindings",e);
            return "Could not save keybinding";
        }finally{if(temp!=null)try{Files.deleteIfExists(temp);}catch(IOException ignored){}}
    }
    static String label(String path,String skill){
        load();var chord=bindings.get(path+"|"+skill);
        if(chord==null)return "Unbound";
        String prefix=((chord.modifiers&GLFW.GLFW_MOD_CONTROL)!=0?"Ctrl+":"")
            +((chord.modifiers&GLFW.GLFW_MOD_SHIFT)!=0?"Shift+":"")
            +((chord.modifiers&GLFW.GLFW_MOD_ALT)!=0?"Alt+":"")
            +((chord.modifiers&GLFW.GLFW_MOD_SUPER)!=0?"Super+":"");
        return prefix+InputConstants.Type.KEYSYM.getOrCreate(chord.key).getDisplayName().getString();
    }
    static void activate(int key,int modifiers){
        load();if(bindings.isEmpty()||nativeConflict(key))return;
        Chord chord=new Chord(key,modifiers&MASK);
        if(!valid(chord))return;
        for(var entry:bindings.entrySet())if(entry.getValue().equals(chord)){
            String[] parts=entry.getKey().split("\\|",-1);
            PacketDistributor.sendToServer(new ProgressionMenuNetwork.Mutate(parts[0],"activate_key",parts[1],""));
            return;
        }
    }
}
