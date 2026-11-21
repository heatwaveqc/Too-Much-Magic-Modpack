package io.github.heatwaveqc.tmm.progression.mxp.mixin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.heatwaveqc.tmm.progression.mxp.core.RootsRitualProof;
import io.github.heatwaveqc.tmm.progression.mxp.core.RootsRitualProof.Kind;
import io.github.heatwaveqc.tmm.progression.mxp.hooks.RootsRitualContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
@Pseudo
@Mixin(targets={"elucent.rootsclassic.ritual.rituals.RitualFlare"},remap=false)
public abstract class RootsRitualFlareMixin {
    @WrapOperation(method="doEffect(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/Container;Ljava/util/List;)V",at=@At(value="INVOKE",target="Lnet/minecraft/world/entity/LivingEntity;igniteForSeconds(F)V"),require=0,remap=false)
    private void tmm$ignite(LivingEntity target,float seconds,Operation<Void> original){
        boolean watch=!(target instanceof Player)&&target.isAlive()&&!target.fireImmune()&&RootsRitualContext.watching(this,Kind.FLARE,target.level());
        int before=watch?target.getRemainingFireTicks():0;original.call(target,seconds);
        if(watch)RootsRitualContext.changed(this,Kind.FLARE,target.level(),target.getRemainingFireTicks()>before);
    }
}
