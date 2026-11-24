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
@Mixin(targets="net.favouriteless.enchanted.common.enchanted.circle_magic.rites.BindFamiliarRite",remap=false)
public abstract class EnchantedFamiliarBindEffectMixin{
    @WrapOperation(method="onTick(Lnet/favouriteless/enchanted/common/enchanted/circle_magic/rites/Rite$RiteParams;)Z",at=@At(value="INVOKE",target="Lnet/minecraft/server/level/ServerLevel;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"),require=0,remap=false)
    private boolean tmm$bound(ServerLevel level,Entity entity,Operation<Boolean> original){
        boolean accepted=original.call(level,entity);
        EnchantedCurseWitness.effect(this,level,"enchanted:familiar_bind",accepted&&EnchantedCurseWitness.familiar(this,level,entity));
        return accepted;
    }
}
