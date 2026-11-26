package io.github.heatwaveqc.tyr.progression.mixin;

import io.github.heatwaveqc.tyr.progression.runtime.WallClimbing;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Optional;

@Mixin(LivingEntity.class)
public abstract class WallClimbingMixin {
    @Shadow private Optional<BlockPos> lastClimbablePos;
    @Inject(method="onClimbable",at=@At("RETURN"),cancellable=true)
    private void tmm$wallClimb(CallbackInfoReturnable<Boolean> cir){
        var entity=(LivingEntity)(Object)this;
        if(!cir.getReturnValue()&&WallClimbing.canClimb(entity)){
            lastClimbablePos=Optional.of(entity.blockPosition());
            cir.setReturnValue(true);
        }
    }
}
