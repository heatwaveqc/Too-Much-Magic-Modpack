package io.github.heatwaveqc.tyr.progression.network;
import io.github.heatwaveqc.tyr.progression.ProgressionMod;
import io.github.heatwaveqc.tyr.progression.client.ClientVision;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

/** One-way visual permission projection. Clients never submit a vision grant. */
public final class VisionNetwork {
    private VisionNetwork(){}
    public record State(boolean night,int lavaRange) implements CustomPacketPayload{
        public static final Type<State> TYPE=new Type<>(ResourceLocation.fromNamespaceAndPath(ProgressionMod.MOD_ID,"vision_state"));
        public static final StreamCodec<RegistryFriendlyByteBuf,State> CODEC=StreamCodec.composite(
            ByteBufCodecs.BOOL,State::night,ByteBufCodecs.VAR_INT,State::lavaRange,State::new);
        @Override public Type<? extends CustomPacketPayload> type(){return TYPE;}
    }
    public static void register(RegisterPayloadHandlersEvent event){
        event.registrar("1").playToClient(State.TYPE,State.CODEC,(payload,context)->
            context.enqueueWork(()->ClientVision.accept(payload.night(),payload.lavaRange())));
    }
    public static void send(ServerPlayer player,boolean night,int lavaRange){
        PacketDistributor.sendToPlayer(player,new State(night,lavaRange));
    }
}
