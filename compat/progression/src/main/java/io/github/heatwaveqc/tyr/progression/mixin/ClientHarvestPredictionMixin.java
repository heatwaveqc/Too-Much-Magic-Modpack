package io.github.heatwaveqc.tyr.progression.mixin;

import io.github.heatwaveqc.tyr.progression.runtime.MiningPrediction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Covers consumers such as tooltip providers that query the non-positional native predicate. */
@Mixin(Player.class)
public abstract class ClientHarvestPredictionMixin {
    @Inject(method="hasCorrectToolForDrops(Lnet/minecraft/world/level/block/state/BlockState;)Z",at=@At("RETURN"),cancellable=true)
    private void tmm$predictHarvest(BlockState state,CallbackInfoReturnable<Boolean> cir){
        var player=(Player)(Object)this;
        if(!cir.getReturnValue()&&MiningPrediction.correctTool(player,state))cir.setReturnValue(true);
    }
}
