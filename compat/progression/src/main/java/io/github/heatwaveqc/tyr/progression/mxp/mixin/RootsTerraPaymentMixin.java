package io.github.heatwaveqc.tyr.progression.mxp.mixin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.NativePaidSources;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
@Pseudo
@Mixin(targets={"elucent.rootsclassic.item.StaffItem","elucent.rootsclassic.item.CrystalStaffItem"},remap=false)
public abstract class RootsTerraPaymentMixin {
    @WrapMethod(method="releaseUsing",require=0,remap=false)
    private void tmm$release(ItemStack stack,Level level,LivingEntity caster,int timeLeft,Operation<Void> original){
        NativePaidSources.Roots.release(caster,()->original.call(stack,level,caster,timeLeft));
    }
    @WrapOperation(method="releaseUsing",at=@At(value="INVOKE",target="Lelucent/rootsclassic/attachment/ManaAttachment;setMana(F)V"),require=0,remap=false)
    private void tmm$debit(@Coerce Object attachment,float target,Operation<Void> original){
        double before=NativePaidSources.Roots.before(attachment);
        original.call(attachment,target);
        NativePaidSources.Roots.removed(attachment,target,before);
    }
    @WrapOperation(method="releaseUsing",at=@At(value="INVOKE",target="Lelucent/rootsclassic/component/ComponentBase;doEffect(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/Entity;Lelucent/rootsclassic/component/EnumCastType;DDDDDD)V"),require=0,remap=false)
    private void tmm$effect(@Coerce Object comp,Level level,Entity caster,@Coerce Object castType,double x,double y,double z,double potency,double duration,double size,Operation<Void> original){
        original.call(comp,level,caster,castType,x,y,z,potency,duration,size);
        NativePaidSources.Roots.effect(comp,caster);
    }
}
