package io.github.heatwaveqc.tmm.progression.mxp.mixin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.heatwaveqc.tmm.progression.mxp.hooks.JawrRitualOutput;
import io.github.heatwaveqc.tmm.progression.mxp.hooks.MxpReflect;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import java.util.UUID;
@Pseudo
@Mixin(targets="dev.sterner.witchery.content.ritual_event.MineBlocksBelowRitualEvent",remap=false)
public abstract class JawrMineRitualMixin{
    @WrapOperation(method="tick(Ldev/sterner/witchery/content/block/ritual/ritual_event/RitualContext;)V",at=@At(value="INVOKE",target="Lnet/minecraft/server/level/ServerLevel;setBlockAndUpdate(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z"),require=0,remap=false)
    private boolean tmm$block(ServerLevel level,BlockPos pos,BlockState state,Operation<Boolean> original){
        BlockState before=level.getBlockState(pos);
        boolean accepted=original.call(level,pos,state);
        JawrRitualOutput.mined(this,level,before,accepted&&!before.equals(level.getBlockState(pos)));
        return accepted;
    }
}
