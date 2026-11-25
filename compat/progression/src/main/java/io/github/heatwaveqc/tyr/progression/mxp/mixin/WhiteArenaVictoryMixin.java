package io.github.heatwaveqc.tyr.progression.mxp.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.heatwaveqc.tyr.progression.mxp.SpecialBossNative;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets="com.telepathicgrunt.the_bumblezone.blocks.EssenceBlockWhite",remap=false)
public abstract class WhiteArenaVictoryMixin {
    @WrapOperation(method="performUniqueArenaTick(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lcom/telepathicgrunt/the_bumblezone/blocks/blockentities/EssenceBlockEntity;)V",
        at=@At(value="INVOKE",target="Lcom/telepathicgrunt/the_bumblezone/blocks/blockentities/EssenceBlockEntity;EndEvent(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lcom/telepathicgrunt/the_bumblezone/blocks/blockentities/EssenceBlockEntity;Z)V"),require=0,remap=false)
    private void tmm$endWhite(ServerLevel level,BlockPos pos,BlockState state,@Coerce Object controller,boolean won,Operation<Void> original){
        SpecialBossNative.whiteVictory(level,controller,won,()->original.call(level,pos,state,controller,won));
    }
    @Inject(method="awardPlayerWinStat(Lnet/minecraft/server/level/ServerPlayer;)V",at=@At("RETURN"),require=0,remap=false)
    private void tmm$nativeWinner(ServerPlayer player,CallbackInfo ci){SpecialBossNative.whiteWinner(player);}
}
