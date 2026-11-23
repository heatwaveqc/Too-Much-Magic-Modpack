package io.github.heatwaveqc.tyr.progression.mxp.mixin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.heatwaveqc.tyr.progression.mxp.core.RootsRitualProof;
import io.github.heatwaveqc.tyr.progression.mxp.core.RootsRitualProof.Kind;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.RootsRitualContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
@Pseudo
@Mixin(targets={"elucent.rootsclassic.ritual.rituals.RitualGrow"},remap=false)
public abstract class RootsRitualGrowMixin {
    @WrapOperation(method="doEffect(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/Container;Ljava/util/List;)V",at=@At(value="INVOKE",target="Lnet/minecraft/world/level/block/BonemealableBlock;performBonemeal(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/util/RandomSource;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V"),require=0,remap=false)
    private void tmm$growth(BonemealableBlock block,ServerLevel world,RandomSource random,BlockPos pos,BlockState state,Operation<Void> original){
        boolean watch=RootsRitualContext.watching(this,Kind.GROW,world);BlockState before=watch?world.getBlockState(pos):null;
        original.call(block,world,random,pos,state);
        if(watch)RootsRitualContext.changed(this,Kind.GROW,world,!before.equals(world.getBlockState(pos)));
    }
}
