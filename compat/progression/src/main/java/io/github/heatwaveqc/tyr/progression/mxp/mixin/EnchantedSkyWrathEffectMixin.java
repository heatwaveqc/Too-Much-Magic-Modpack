package io.github.heatwaveqc.tyr.progression.mxp.mixin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.EnchantedCurseWitness;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
@Pseudo
@Mixin(targets="net.favouriteless.enchanted.common.enchanted.circle_magic.rites.SkyWrathRite",remap=false)
public abstract class EnchantedSkyWrathEffectMixin{
    @WrapOperation(method="spawnLightning(Lnet/minecraft/server/level/ServerLevel;DDD)V",at=@At(value="INVOKE",target="Lnet/minecraft/server/level/ServerLevel;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"),require=0,remap=false)
    private boolean tmm$lightning(ServerLevel level,Entity entity,Operation<Boolean> original){
        boolean accepted=original.call(level,entity);
        EnchantedCurseWitness.effect(this,level,"minecraft:lightning_bolt",accepted&&entity instanceof net.minecraft.world.entity.LightningBolt);
        return accepted;
    }
}
