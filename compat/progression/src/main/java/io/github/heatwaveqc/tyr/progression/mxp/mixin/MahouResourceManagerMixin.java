package io.github.heatwaveqc.tyr.progression.mxp.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.MahouResourceSkills;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

/** 1.36.31: both native regen capacity reads, awake recovery once, HUD snapshot. */
@Pseudo
@Mixin(targets="stepsword.mahoutsukai.mana.PlayerManaManager",remap=false)
public abstract class MahouResourceManagerMixin {
    private static final String REGEN="regenMana(Lnet/minecraft/world/entity/player/Player;Z)V";
    @WrapOperation(method=REGEN,at=@At(value="INVOKE",target="Lstepsword/mahoutsukai/dataattachments/mahou/IMahou;getMaxMana()I"),require=0,remap=false)
    private static int tyr$effectiveCapacity(@Coerce Object holder,Operation<Integer> original,Player player,boolean sleeping){
        return MahouResourceSkills.capacity(player,original.call(holder));
    }
    @WrapOperation(method=REGEN,at=@At(value="INVOKE",target="Lstepsword/mahoutsukai/mana/PlayerManaManager;getManaRegen(Lnet/minecraft/world/entity/player/Player;)I"),require=0,remap=false)
    private static int tyr$awakeRecovery(Player player,Operation<Integer> original){
        return MahouResourceSkills.recovery(player,original.call(player));
    }
    @WrapOperation(method="updateClientMahou(Lnet/minecraft/server/level/ServerPlayer;Lstepsword/mahoutsukai/dataattachments/mahou/IMahou;)V",
        at=@At(value="INVOKE",target="Lstepsword/mahoutsukai/networking/PacketHandler;sendTo(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/network/protocol/common/custom/CustomPacketPayload;)V"),require=0,remap=false)
    private static void tyr$syncEffectiveCapacity(ServerPlayer player,CustomPacketPayload payload,Operation<Void> original){
        MahouResourceSkills.snapshot(player,payload);
        original.call(player,payload);
    }
}
