package io.github.heatwaveqc.tyr.progression.mixin;

import io.github.heatwaveqc.tyr.progression.runtime.TreeChopLootScope;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** TreeChop 0.19.3 source: FellTreeResult.makeBlockBreaker returns the
 * exact per-position consumer, with creditPlayer null by default. */
@Pseudo
@Mixin(targets="ht.treechop.common.chop.FellTreeResult",remap=false)
public abstract class TreeChopFellLootMixin {
    @Inject(method="makeBlockBreaker",at=@At("RETURN"),cancellable=true,remap=false,require=1)
    private static void tmm$carrySightActor(ServerPlayer player,ServerLevel level,
            CallbackInfoReturnable<Consumer<BlockPos>> cir){
        cir.setReturnValue(TreeChopLootScope.wrap(player,level,cir.getReturnValue()));
    }
}
