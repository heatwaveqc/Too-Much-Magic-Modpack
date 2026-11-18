package io.github.heatwaveqc.tmm.progression.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.heatwaveqc.tmm.progression.foundation.Condition;
import io.github.heatwaveqc.tmm.progression.foundation.PlayerConditions;
import io.github.heatwaveqc.tmm.progression.foundation.Comparison;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** Declarative eligibility; trusted provider adapters register read-only flags. */
public final class Eligibility {
    private Eligibility() {}
    private static final Map<String,Condition<ServerPlayer>> FLAGS=new ConcurrentHashMap<>();
    public static void registerFlag(String id,Condition<ServerPlayer> predicate) {
        ResourceLocation parsed=ResourceLocation.parse(id);
        if(!parsed.toString().equals(id) || FLAGS.putIfAbsent(id,java.util.Objects.requireNonNull(predicate))!=null)
            throw new IllegalArgumentException("Invalid or duplicate provider flag "+id);
    }
    public static Condition<ServerPlayer> parse(JsonElement node) {
        if(node==null) return player -> Condition.Result.PASS;
        return parse(node.getAsJsonObject(),0,new int[]{0});
    }
    private static Condition<ServerPlayer> parse(JsonObject node,int depth,int[] count) {
        if(depth>12 || ++count[0]>128) throw new IllegalArgumentException("Condition too deep/large");
        String type=node.get("type").getAsString();
        return switch(type) {
            case "always" -> player -> Condition.Result.PASS;
            case "and","or" -> {
                var children=new ArrayList<Condition<ServerPlayer>>();
                for(var child:node.getAsJsonArray("conditions"))
                    children.add(parse(child.getAsJsonObject(),depth+1,count));
                yield type.equals("and")?Condition.and(children):Condition.or(children);
            }
            case "not" -> Condition.not(parse(node.getAsJsonObject("condition"),depth+1,count));
            case "flag" -> {
                String id=ResourceLocation.parse(node.get("id").getAsString()).toString();
                yield player -> {
                    var condition=FLAGS.get(id);
                    if(condition==null) return Condition.Result.UNAVAILABLE;
                    try { return java.util.Objects.requireNonNull(condition.evaluate(player)); }
                    catch(RuntimeException | LinkageError e) { return Condition.Result.UNAVAILABLE; }
                };
            }
            case "path_joined" -> {
                String id=ResourceLocation.parse(node.get("id").getAsString()).toString();
                yield player -> PathRegistry.get(id)==null ? Condition.Result.UNAVAILABLE :
                    (io.github.heatwaveqc.tmm.progression.core.ProgressionApi.track(player,id).joined()
                        ? Condition.Result.PASS:Condition.Result.FAIL);
            }
            case "dimension" -> PlayerConditions.dimension(ResourceLocation.parse(node.get("id").getAsString()));
            case "biome" -> {
                String id=node.get("id").getAsString();
                yield PlayerConditions.biome(ResourceLocation.parse(id.startsWith("#")?id.substring(1):id),id.startsWith("#"));
            }
            case "advancement" -> PlayerConditions.advancement(ResourceLocation.parse(node.get("id").getAsString()));
            case "effect" -> PlayerConditions.effect(ResourceLocation.parse(node.get("id").getAsString()),
                    node.has("min_amplifier")?node.get("min_amplifier").getAsInt():0);
            case "attribute" -> PlayerConditions.attribute(ResourceLocation.parse(node.get("id").getAsString()),
                    Comparison.fromKey(node.get("comparison").getAsString()),node.get("value").getAsDouble());
            default -> throw new IllegalArgumentException("Unknown eligibility type "+type);
        };
    }
}
