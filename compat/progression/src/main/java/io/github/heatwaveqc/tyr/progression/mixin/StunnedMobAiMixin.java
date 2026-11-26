package io.github.heatwaveqc.tyr.progression.mixin;
import io.github.heatwaveqc.tyr.progression.runtime.StunningStrike;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public abstract class StunnedMobAiMixin {
    @Inject(method="serverAiStep",at=@At("HEAD"),cancellable=true)
    private void tmm$stun(CallbackInfo ci){
        var mob=(Mob)(Object)this;
        if(StunningStrike.stunned(mob)){StunningStrike.stopIntent(mob);ci.cancel();}
    }
}
