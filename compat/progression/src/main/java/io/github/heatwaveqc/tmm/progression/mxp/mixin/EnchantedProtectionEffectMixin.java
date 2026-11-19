package io.github.heatwaveqc.tmm.progression.mxp.mixin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import io.github.heatwaveqc.tmm.progression.mxp.hooks.EnchantedCurseWitness;
import io.github.heatwaveqc.tmm.progression.mxp.hooks.MxpReflect;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
@Pseudo
@Mixin(targets={"net.favouriteless.enchanted.common.enchanted.circle_magic.rites.ProtectionRite"},remap=false)
public abstract class EnchantedProtectionEffectMixin{
    private static Object tmm$registered(ServerLevel level,BlockPos pos){
        try{
            Class<?> type=Class.forName("net.favouriteless.enchanted.common.enchanted.stateobservers.ProtectionRiteObserver");
            Object manager=MxpReflect.staticCall("net.favouriteless.stateobserver.api.StateObserverManager","get");
            return MxpReflect.call(manager,"getObserver",level,pos,type);
        }catch(ClassNotFoundException|LinkageError ignored){return null;}
    }
    @WrapMethod(method="getOrCreateObserver(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;)V",require=0,remap=false)
    private void tmm$commission(ServerLevel level,BlockPos pos,Operation<Void> original){
        boolean observing=EnchantedCurseWitness.effectEligible(this,level,"enchanted:protection");
        Object before=observing?tmm$registered(level,pos):null;
        original.call(level,pos);
        if(observing&&before==null){
            Object observer=MxpReflect.field(this,"observer");
            EnchantedCurseWitness.effect(this,level,"enchanted:protection",observer!=null&&observer==tmm$registered(level,pos));
        }
    }
}
