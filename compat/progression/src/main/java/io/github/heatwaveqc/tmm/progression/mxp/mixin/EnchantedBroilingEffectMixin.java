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
@Mixin(targets={"net.favouriteless.enchanted.common.enchanted.circle_magic.rites.BroilingRite"},remap=false)
public abstract class EnchantedBroilingEffectMixin{
    @WrapOperation(method="onTick(Lnet/favouriteless/enchanted/common/enchanted/circle_magic/rites/Rite$RiteParams;)Z",at=@At(value="INVOKE",target="Lnet/minecraft/server/level/ServerLevel;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z",ordinal=0),require=0,remap=false)
    private boolean tmm$smelt(ServerLevel level,Entity entity,Operation<Boolean> original){
        boolean accepted=original.call(level,entity);
        EnchantedCurseWitness.effect(this,level,"enchanted:broiling",accepted&&entity instanceof net.minecraft.world.entity.item.ItemEntity item&&!item.getItem().isEmpty());
        return accepted;
    }
}
