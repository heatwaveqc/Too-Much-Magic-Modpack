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
            case "experience_level" -> {
                String id=ResourceLocation.parse(node.get("id").getAsString()).toString();
                int minimum=node.get("minimum").getAsInt();
                if(minimum<0)throw new IllegalArgumentException("Negative experience level");
                yield player -> {
                    if(PathRegistry.economy(id)==null)return Condition.Result.UNAVAILABLE;
                    var wallet=player.getData(io.github.heatwaveqc.tmm.progression.state.ProgressionAttachments.LEDGER.get()).wallets().get(id);
                    return (wallet==null?0:wallet.rank())>=minimum?Condition.Result.PASS:Condition.Result.FAIL;
                };
            }
            case "health_fraction","food_level" -> io.github.heatwaveqc.tmm.progression.foundation.ContextConditions.meter(type,
                Comparison.fromKey(node.get("comparison").getAsString()),node.get("value").getAsDouble());
            case "time_window" -> io.github.heatwaveqc.tmm.progression.foundation.ContextConditions.time(node.get("start").getAsInt(),node.get("end").getAsInt());
            case "weather" -> io.github.heatwaveqc.tmm.progression.foundation.ContextConditions.weather(node.get("value").getAsString());
            case "player_state" -> io.github.heatwaveqc.tmm.progression.foundation.ContextConditions.state(node.get("state").getAsString(),
                !node.has("value")||node.get("value").getAsBoolean());
            case "equipped" -> io.github.heatwaveqc.tmm.progression.foundation.ContextConditions.equipped(node.get("slot").getAsString(),node.get("target").getAsString());
            case "skill_rank" -> io.github.heatwaveqc.tmm.progression.foundation.ContextConditions.skill(
                ResourceLocation.parse(node.get("path").getAsString()).toString(),ResourceLocation.parse(node.get("id").getAsString()).toString(),
                node.has("minimum")?node.get("minimum").getAsInt():1);
            case "milestone" -> io.github.heatwaveqc.tmm.progression.foundation.ContextConditions.milestone(
                ResourceLocation.parse(node.get("path").getAsString()).toString(),ResourceLocation.parse(node.get("id").getAsString()).toString());
            case "dragon_species" -> {
                String species=ResourceLocation.parse(node.get("id").getAsString()).toString();
                yield player -> io.github.heatwaveqc.tmm.progression.runtime.forms.ProviderFlags.dragonSpecies(player,species);
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
