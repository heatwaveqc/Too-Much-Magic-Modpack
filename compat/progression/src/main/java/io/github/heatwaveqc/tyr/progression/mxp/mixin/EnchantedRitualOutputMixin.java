package io.github.heatwaveqc.tyr.progression.mxp.mixin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.E6bSpawnScope;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
@Pseudo
@Mixin(targets={"net.favouriteless.enchanted.common.enchanted.circle_magic.rites.CreateItemRite","net.favouriteless.enchanted.common.enchanted.circle_magic.rites.EntityBoundCreateItemRite","net.favouriteless.enchanted.common.enchanted.circle_magic.rites.LocationBoundCreateItemRite","net.favouriteless.enchanted.common.enchanted.circle_magic.rites.BindTalismanRite"},remap=false)
public abstract class EnchantedRitualOutputMixin{
    @WrapOperation(method="onStart(Lnet/favouriteless/enchanted/common/enchanted/circle_magic/rites/Rite$RiteParams;)Z",at=@At(value="INVOKE",target="Lnet/minecraft/server/level/ServerLevel;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"),require=0,remap=false)
    private boolean tmm$spawn(ServerLevel level,Entity entity,Operation<Boolean> original){
        boolean accepted=original.call(level,entity);E6bSpawnScope.spawned(this,level,entity,accepted);return accepted;
    }
}
