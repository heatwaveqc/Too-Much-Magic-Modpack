package io.github.heatwaveqc.tyr.progression.network;

import io.github.heatwaveqc.tyr.progression.ProgressionMod;
import io.github.heatwaveqc.tyr.progression.client.PathResourceHud;
import io.github.heatwaveqc.tyr.progression.runtime.resources.ResourcePolicy;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import com.google.gson.*;
import java.util.*;

/** One-way, bounded personal HUD snapshot. No resource mutation network message. */
public final class PathResourceNetwork {
    private PathResourceNetwork(){}
    public static final int MAX_METERS=8;
    public record Meter(String title,int value,int capacity,int color,int divisor){
        public Meter{
            if(title==null||title.isBlank()||title.length()>48||title.chars().anyMatch(Character::isISOControl)
                ||capacity<1||capacity>ResourcePolicy.MAX||value<0||value>capacity||color<0||color>0xffffff
                ||!ResourcePolicy.validDivisor(divisor))throw new IllegalArgumentException("Invalid meter");
        }
    }
    public record State(String json) implements CustomPacketPayload{
        public static final Type<State> TYPE=new Type<>(ResourceLocation.fromNamespaceAndPath(ProgressionMod.MOD_ID,"path_resources"));
        public static final StreamCodec<RegistryFriendlyByteBuf,State> CODEC=
            StreamCodec.composite(ByteBufCodecs.stringUtf8(8192),State::json,State::new);
        @Override public Type<? extends CustomPacketPayload> type(){return TYPE;}
    }
    public static void register(RegisterPayloadHandlersEvent event){
        event.registrar("1").playToClient(State.TYPE,State.CODEC,(payload,context)->
            context.enqueueWork(()->PathResourceHud.accept(decode(payload.json()))));
    }
    public static void send(ServerPlayer player,List<Meter> meters){
        if(meters.size()>MAX_METERS)throw new IllegalArgumentException("Too many HUD meters");
        PacketDistributor.sendToPlayer(player,new State(new Gson().toJson(meters)));
    }
    public static List<Meter> decode(String json){
        try{
            var rows=JsonParser.parseString(json).getAsJsonArray();
            if(rows.size()>MAX_METERS)return List.of();
            var result=new ArrayList<Meter>();
            for(var item:rows){
                var m=item.getAsJsonObject();
                result.add(new Meter(m.get("title").getAsString(),m.get("value").getAsBigDecimal().intValueExact(),
                    m.get("capacity").getAsBigDecimal().intValueExact(),m.get("color").getAsBigDecimal().intValueExact(),
                    m.get("divisor").getAsBigDecimal().intValueExact()));
            }
            return List.copyOf(result);
        }catch(RuntimeException bad){return List.of();}
    }
}
