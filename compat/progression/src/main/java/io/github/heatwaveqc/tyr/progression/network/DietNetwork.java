package io.github.heatwaveqc.tyr.progression.network;
import io.github.heatwaveqc.tyr.progression.client.ClientDiet;
import io.github.heatwaveqc.tyr.progression.ProgressionMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.PacketDistributor;

/** One-way server projection for beverage preview. No client entitlement requests. */
public final class DietNetwork {
 private DietNetwork(){}
 public record State(String selectors) implements CustomPacketPayload{
  public static final Type<State> TYPE=new Type<>(ResourceLocation.fromNamespaceAndPath(ProgressionMod.MOD_ID,"diet_state"));
  public static final StreamCodec<RegistryFriendlyByteBuf,State> CODEC=StreamCodec.composite(
      ByteBufCodecs.stringUtf8(8192),State::selectors,State::new);
  @Override public Type<? extends CustomPacketPayload> type(){return TYPE;}
 }
 public static void register(RegisterPayloadHandlersEvent event){
    event.registrar("1").playToClient(State.TYPE,State.CODEC,(payload,context)->
        context.enqueueWork(()->ClientDiet.accept(payload.selectors())));
 }
 public static void send(ServerPlayer p,String selectors){PacketDistributor.sendToPlayer(p,new State(selectors));}
}
