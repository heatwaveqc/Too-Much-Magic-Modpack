package io.github.heatwaveqc.tyr.progression.mxp.mixin;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import io.github.heatwaveqc.tyr.progression.runtime.forms.VampireEffects;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
/** Exact native active bat timer duration, never grant flight or reset elapsed time. */
@Pseudo
@Mixin(targets="dev.sterner.witchery.feature.transformation.TransformationHandler",remap=false)
public abstract class JawrBatDurationMixin {
    @ModifyExpressionValue(method="tickBat(Lnet/minecraft/world/entity/player/Player;)V",
        at=@At(value="INVOKE",target="Lnet/minecraft/world/entity/ai/attributes/AttributeInstance;getValue()D"),require=0,allow=1,remap=false)
    private double tmm$duration(double duration,Player player){
        return VampireEffects.batDuration(player,duration);
    }
}
