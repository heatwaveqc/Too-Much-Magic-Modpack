package io.github.heatwaveqc.tmm.progression.mxp.mixin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.heatwaveqc.tmm.progression.mxp.core.RootsRitualProof;
import io.github.heatwaveqc.tmm.progression.mxp.core.RootsRitualProof.Kind;
import io.github.heatwaveqc.tmm.progression.mxp.hooks.RootsRitualContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.damagesource.DamageSource;
@Pseudo
@Mixin(targets={"elucent.rootsclassic.ritual.rituals.RitualLifeDrain"},remap=false)
public abstract class RootsRitualDrainMixin {
    @WrapOperation(method="doEffect(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/Container;Ljava/util/List;)V",at=@At(value="INVOKE",target="Lnet/minecraft/world/entity/monster/Monster;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"),require=0,remap=false)
    private boolean tmm$damage(Monster target,DamageSource damage,float amount,Operation<Boolean> original){
        boolean watch=target.isAlive()&&RootsRitualContext.watching(this,Kind.DRAIN,target.level());float before=watch?target.getHealth():0;
        boolean accepted=original.call(target,damage,amount);
        if(watch)RootsRitualContext.changed(this,Kind.DRAIN,target.level(),RootsRitualProof.damageObserved(accepted,before,target.getHealth()));
        return accepted;
    }
}
