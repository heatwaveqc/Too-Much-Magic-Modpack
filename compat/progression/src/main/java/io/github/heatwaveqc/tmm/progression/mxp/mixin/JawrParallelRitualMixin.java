package io.github.heatwaveqc.tmm.progression.mxp.mixin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.heatwaveqc.tmm.progression.mxp.hooks.JawrRitualOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
@Pseudo
@Mixin(targets="dev.sterner.witchery.content.ritual_event.ParallelRitualEvent",remap=false)
public abstract class JawrParallelRitualMixin{
    @WrapOperation(method="tick(Ldev/sterner/witchery/content/block/ritual/ritual_event/RitualContext;)V",at=@At(value="INVOKE",target="Ldev/sterner/witchery/content/block/ritual/ritual_event/RitualEvent;tick(Ldev/sterner/witchery/content/block/ritual/ritual_event/RitualContext;)V"),require=0,remap=false)
    private void tmm$child(@Coerce Object event,@Coerce Object context,Operation<Void> original){
        JawrRitualOutput.child(this,event,context,()->original.call(event,context));
    }
}
