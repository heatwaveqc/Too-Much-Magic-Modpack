package io.github.heatwaveqc.tmm.progression.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import io.github.heatwaveqc.tmm.progression.runtime.MiningUtilities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Final;

/** Outer native break receipt; nested break stacks are not another skill activation. */
@Mixin(ServerPlayerGameMode.class)
public abstract class OwnedNativeMiningMixin {
    @Shadow @Final protected ServerPlayer player;
    @WrapMethod(method="destroyBlock")
    private boolean tmm$nativeBreak(BlockPos pos,Operation<Boolean> original){
        return MiningUtilities.nativeBreak(player,pos,()->original.call(pos));
    }
}
