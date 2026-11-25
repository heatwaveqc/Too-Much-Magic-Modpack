package io.github.heatwaveqc.tyr.progression.mixin;

import io.github.heatwaveqc.tyr.progression.client.OtherworldSightClient;
import net.minecraft.core.BlockPos;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.Set;

/** Runs after native fruit/goggle/staff rendering, without ever touching the shader. */
@Pseudo
@Mixin(targets="com.klikli_dev.occultism.client.render.ThirdEyeEffectRenderer",remap=false)
public abstract class OccultismOtherworldRendererMixin {
    @Shadow public Set<BlockPos> uncoveredBlocks;
    @Inject(method="onPlayerTick",at=@At("TAIL"),require=0,remap=false)
    private void tmm$innateSight(PlayerTickEvent.Post event,CallbackInfo ci){
        OtherworldSightClient.tick(event.getEntity(),uncoveredBlocks);
    }
}
