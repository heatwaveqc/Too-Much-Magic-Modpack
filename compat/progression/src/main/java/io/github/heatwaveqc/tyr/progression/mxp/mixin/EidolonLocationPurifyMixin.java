package io.github.heatwaveqc.tyr.progression.mxp.mixin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.E6bSpawnScope;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.MxpReflect;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
@Pseudo
@Mixin(targets={"alexthw.eidolon_repraised.common.ritual.LocationRitual","alexthw.eidolon_repraised.common.ritual.PurifyRitual"},remap=false)
public abstract class EidolonLocationPurifyMixin{
    @WrapOperation(method="start(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Lalexthw/eidolon_repraised/api/ritual/Ritual$RitualResult;",at=@At(value="INVOKE",target="Lnet/minecraft/server/level/ServerLevel;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"),require=0,remap=false)
    private boolean tmm$spawn(ServerLevel world,Entity entity,Operation<Boolean> original){
        boolean accepted=original.call(world,entity);E6bSpawnScope.spawned(this,world,entity,accepted);return accepted;
    }
}
