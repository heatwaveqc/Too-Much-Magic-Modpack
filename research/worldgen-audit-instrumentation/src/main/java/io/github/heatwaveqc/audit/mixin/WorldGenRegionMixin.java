package io.github.heatwaveqc.audit.mixin;
import io.github.heatwaveqc.audit.Placement;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
@Mixin(WorldGenRegion.class)
public abstract class WorldGenRegionMixin {
    @Inject(method="setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;II)Z",at=@At("RETURN"))
    private void tmm$write(BlockPos pos, BlockState state, int flags, int recursion, CallbackInfoReturnable<Boolean> ci) {
        if(ci.getReturnValueZ()) Placement.write(pos,state);
    }
}
