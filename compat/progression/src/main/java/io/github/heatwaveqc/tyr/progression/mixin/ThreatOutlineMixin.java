package io.github.heatwaveqc.tyr.progression.mixin;
import io.github.heatwaveqc.tyr.progression.client.ClientDangerSense;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
@Mixin(Minecraft.class)
public abstract class ThreatOutlineMixin {
    @Inject(method="shouldEntityAppearGlowing",at=@At("RETURN"),cancellable=true)
    private void tmm$threat(Entity entity,CallbackInfoReturnable<Boolean> cir){
        if(ClientDangerSense.highlighted(entity))cir.setReturnValue(true);
    }
}
