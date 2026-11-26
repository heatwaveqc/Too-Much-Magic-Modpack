package io.github.heatwaveqc.tyr.progression.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import io.github.heatwaveqc.tyr.progression.runtime.HeightFallTrial;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/** Use the same native start-position record as the vanilla fall advancement. */
@Mixin(ServerPlayer.class)
public abstract class HeightFallTrialMixin {
    @Shadow private Vec3 startingToFallPosition;
    @WrapMethod(method="checkFallDamage(DZLnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)V")
    private void tmm$heightFall(double dy,boolean onGround,BlockState state,BlockPos pos,Operation<Void> original){
        var player=(ServerPlayer)(Object)this;
        HeightFallTrial.aroundLanding(player,startingToFallPosition,dy,onGround,state,pos,
            ()->original.call(dy,onGround,state,pos));
    }
}
