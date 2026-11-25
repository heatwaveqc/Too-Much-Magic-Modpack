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
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
@Pseudo
@Mixin(targets={"alexthw.eidolon_repraised.common.ritual.AllureRitual","alexthw.eidolon_repraised.common.ritual.RepellingRitual"},remap=false)
public abstract class EidolonContinuousRitualMixin{
    @WrapOperation(method="tick(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Lalexthw/eidolon_repraised/api/ritual/Ritual$RitualResult;",at=@At(value="INVOKE",target="Lnet/minecraft/world/entity/ai/goal/GoalSelector;addGoal(ILnet/minecraft/world/entity/ai/goal/Goal;)V"),require=0,remap=false)
    private void tmm$added(GoalSelector selector,int priority,Goal goal,Operation<Void> original,Level world,BlockPos pos){
        boolean absent=E6bSpawnScope.observing()&&goal!=null&&goal.getClass().getName().equals("alexthw.eidolon_repraised.common.entity.ai.GoToPositionGoal")
            &&selector.getAvailableGoals().stream().noneMatch(w->w.getGoal()==goal);
        original.call(selector,priority,goal);
        if(absent&&selector.getAvailableGoals().stream().anyMatch(w->w.getGoal()==goal))E6bSpawnScope.effect(this,world);
    }
}
