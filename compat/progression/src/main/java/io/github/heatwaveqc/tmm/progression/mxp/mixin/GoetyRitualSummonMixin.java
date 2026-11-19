package io.github.heatwaveqc.tmm.progression.mxp.mixin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.heatwaveqc.tmm.progression.mxp.hooks.GoetyRitualContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;

@Pseudo
@Mixin(targets="com.Polarice3.Goety.common.ritual.SummonRitual",remap=false)
public abstract class GoetyRitualSummonMixin {
    @WrapOperation(method="finish(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lcom/Polarice3/Goety/common/blocks/entities/DarkAltarBlockEntity;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;)V",at=@At(value="INVOKE",target="Lcom/Polarice3/Goety/common/ritual/SummonRitual;createSummonedEntity(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lcom/Polarice3/Goety/common/blocks/entities/DarkAltarBlockEntity;Lnet/minecraft/world/entity/player/Player;)Lnet/minecraft/world/entity/Entity;"),require=0,allow=2,remap=false)
    private Entity tmm$create(@Coerce Object ritual,EntityType<?> type,Level world,BlockPos pos,@Coerce Object altar,Player actor,Operation<Entity> original){
        Entity entity=original.call(ritual,type,world,pos,altar,actor);
        GoetyRitualContext.created(ritual,entity,type,world,pos,altar,actor);
        return entity;
    }
    @WrapOperation(method="spawnEntity(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/level/Level;)V",at=@At(value="INVOKE",target="Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"),require=0,allow=1,remap=false)
    private boolean tmm$spawn(Level world,Entity entity,Operation<Boolean> original){
        boolean accepted=original.call(world,entity);
        GoetyRitualContext.spawned(this,world,entity,accepted);
        return accepted;
    }
}
