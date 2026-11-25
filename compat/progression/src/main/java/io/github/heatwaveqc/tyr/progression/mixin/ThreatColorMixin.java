package io.github.heatwaveqc.tyr.progression.mixin;
import io.github.heatwaveqc.tyr.progression.client.ClientDangerSense;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
@Mixin(Entity.class)
public abstract class ThreatColorMixin {
    @Inject(method="getTeamColor",at=@At("RETURN"),cancellable=true)
    private void tmm$threatColor(CallbackInfoReturnable<Integer> cir){
        if(ClientDangerSense.highlighted((Entity)(Object)this))cir.setReturnValue(0xff3030);
    }
}
