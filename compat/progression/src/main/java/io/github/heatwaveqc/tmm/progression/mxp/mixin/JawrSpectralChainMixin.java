package io.github.heatwaveqc.tmm.progression.mxp.mixin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.heatwaveqc.tmm.progression.mxp.hooks.JawrRitualOutput;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
@Pseudo
@Mixin(targets="dev.sterner.witchery.feature.chain.ChainManager",remap=false)
public abstract class JawrSpectralChainMixin{
    @WrapOperation(method="createChain(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/entity/Entity;FFFILkotlin/jvm/functions/Function1;Ldev/sterner/witchery/feature/chain/ChainType;)Ldev/sterner/witchery/content/entity/ChainEntity;",at=@At(value="INVOKE",target="Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"),require=0,remap=false)
    private boolean tmm$chain(Level level,Entity chain,Operation<Boolean> original,Level world,Vec3 origin,Entity target,float speed,float retract,float pull,int life,@Coerce Object callback,@Coerce Object type){
        boolean accepted=original.call(level,chain);
        if(world==level&&type instanceof Enum<?> e&&"SPIRIT".equals(e.name()))JawrRitualOutput.spectralChain(level,chain,accepted);
        return accepted;
    }
}
