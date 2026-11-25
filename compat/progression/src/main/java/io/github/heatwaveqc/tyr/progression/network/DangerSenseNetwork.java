package io.github.heatwaveqc.tyr.progression.network;

import io.github.heatwaveqc.tyr.progression.ProgressionMod;
import io.github.heatwaveqc.tyr.progression.client.ClientDangerSense;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import java.util.*;
import java.util.stream.Collectors;

public final class DangerSenseNetwork {
    private DangerSenseNetwork(){}
    public static final int MAX_TARGETS=64,MAX_WIRE=64*37;
    public record State(String ids,boolean alert) implements CustomPacketPayload{
        public static final Type<State> TYPE=new Type<>(ResourceLocation.fromNamespaceAndPath(ProgressionMod.MOD_ID,"danger_sense"));
        public static final StreamCodec<RegistryFriendlyByteBuf,State> CODEC=StreamCodec.composite(
            ByteBufCodecs.stringUtf8(MAX_WIRE),State::ids,ByteBufCodecs.BOOL,State::alert,State::new);
        @Override public Type<? extends CustomPacketPayload> type(){return TYPE;}
    }
    public static void register(RegisterPayloadHandlersEvent event){
        event.registrar("1").playToClient(State.TYPE,State.CODEC,(payload,context)->
            context.enqueueWork(()->ClientDangerSense.accept(decode(payload.ids()),payload.alert())));
    }
    public static void send(ServerPlayer p,Set<UUID> ids,boolean alert){
        if(ids.size()>MAX_TARGETS)throw new IllegalArgumentException("Threat snapshot too large");
        PacketDistributor.sendToPlayer(p,new State(ids.stream().map(UUID::toString).sorted().collect(Collectors.joining(",")),alert));
    }
    public static Set<UUID> decode(String ids){
        if(ids==null||ids.length()>MAX_WIRE||ids.isEmpty())return Set.of();
        var parts=ids.split(",",-1);
        if(parts.length>MAX_TARGETS)return Set.of();
        var out=new HashSet<UUID>();
        try{for(String part:parts){var id=UUID.fromString(part);if(!id.toString().equals(part)||!out.add(id))return Set.of();}}
        catch(IllegalArgumentException invalid){return Set.of();}
        return Set.copyOf(out);
    }
}
