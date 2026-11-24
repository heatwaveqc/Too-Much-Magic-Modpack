package io.github.heatwaveqc.tyr.progression.mxp.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.heatwaveqc.tyr.progression.runtime.SpellMiningScope;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import java.util.UUID;

@Pseudo
@Mixin(targets="com.hollingsworth.arsnouveau.common.spell.effect.EffectBreak",remap=false)
public abstract class ArsBreakMiningMixin {
    @WrapOperation(method="onResolveBlock",at=@At(value="INVOKE",
        target="Lcom/hollingsworth/arsnouveau/api/util/BlockUtil;destroyRespectsClaim(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Z"),require=0,remap=false)
    private boolean tmm$claim(Entity actor,Level level,BlockPos pos,Operation<Boolean> original,
                             @Local(argsOnly=true) LivingEntity shooter){
        return SpellMiningScope.claim(shooter,level,pos,()->original.call(actor,level,pos));
    }
    @WrapOperation(method="onResolveBlock",at=@At(value="INVOKE",
        target="Lcom/hollingsworth/arsnouveau/api/util/BlockUtil;breakExtraBlock(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/item/ItemStack;Ljava/util/UUID;Z)Z"),require=0,remap=false)
    private boolean tmm$break(ServerLevel level,BlockPos pos,ItemStack tool,UUID source,boolean bypass,
                             Operation<Boolean> original,@Local(argsOnly=true) LivingEntity shooter){
        // The caster object, not the fake player's UUID, is the authorization.
        return SpellMiningScope.breakBlock(shooter,level,pos,()->original.call(level,pos,tool,source,bypass));
    }
}
