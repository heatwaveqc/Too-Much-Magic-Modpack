package io.github.heatwaveqc.tyr.progression.network;

import com.google.gson.*;
import io.github.heatwaveqc.tyr.progression.ProgressionMod;
import io.github.heatwaveqc.tyr.progression.client.ChallengeHud;
import io.github.heatwaveqc.tyr.progression.data.ChallengeHudSpec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import java.util.*;

/** One-way bounded snapshot. No client-owned challenge state or completion request. */
public final class ChallengeHudNetwork {
    private ChallengeHudNetwork(){}
    public static final int MAX_WIRE=16384;
    public record State(String json) implements CustomPacketPayload{
        public static final Type<State> TYPE=new Type<>(ResourceLocation.fromNamespaceAndPath(ProgressionMod.MOD_ID,"challenge_hud"));
        public static final StreamCodec<RegistryFriendlyByteBuf,State> CODEC=
            StreamCodec.composite(ByteBufCodecs.stringUtf8(MAX_WIRE),State::json,State::new);
        @Override public Type<? extends CustomPacketPayload> type(){return TYPE;}
    }
    public static void register(RegisterPayloadHandlersEvent event){
        event.registrar("1").playToClient(State.TYPE,State.CODEC,(payload,context)->
            context.enqueueWork(()->ChallengeHud.accept(decode(payload.json()))));
    }
    public static void send(ServerPlayer player,List<ChallengeHudSpec.Row> rows){
        if(rows.size()>ChallengeHudSpec.MAX_ROWS)throw new IllegalArgumentException("Too many HUD rows");
        PacketDistributor.sendToPlayer(player,new State(new Gson().toJson(rows)));
    }
    private static ChallengeHudSpec.Text text(JsonObject j){
        if(!j.keySet().equals(Set.of("text","key")))throw new IllegalArgumentException("Invalid HUD text shape");
        var t=j.get("text");var k=j.get("key");
        if(!t.isJsonPrimitive()||!t.getAsJsonPrimitive().isString()||
           !k.isJsonPrimitive()||!k.getAsJsonPrimitive().isString())
            throw new IllegalArgumentException("Invalid HUD text type");
        return new ChallengeHudSpec.Text(t.getAsString(),k.getAsString());
    }
    public static List<ChallengeHudSpec.Row> decode(String json){
        if(json==null||json.length()>MAX_WIRE)return List.of();
        try{
            var rows=JsonParser.parseString(json).getAsJsonArray();
            if(rows.size()>ChallengeHudSpec.MAX_ROWS)return List.of();
            var result=new ArrayList<ChallengeHudSpec.Row>();
            for(var e:rows){
                var j=e.getAsJsonObject();
                if(!j.keySet().equals(Set.of("title","objective","current","total")))
                    throw new IllegalArgumentException("Invalid HUD row shape");
                if(!j.get("current").isJsonPrimitive()||!j.get("current").getAsJsonPrimitive().isNumber()
                   ||!j.get("total").isJsonPrimitive()||!j.get("total").getAsJsonPrimitive().isNumber())
                    throw new IllegalArgumentException("Invalid HUD progress type");
                result.add(new ChallengeHudSpec.Row(text(j.getAsJsonObject("title")),text(j.getAsJsonObject("objective")),
                    j.get("current").getAsBigDecimal().intValueExact(),j.get("total").getAsBigDecimal().intValueExact()));
            }
            return List.copyOf(result);
        }catch(RuntimeException bad){return List.of();}
    }
}
