package io.github.heatwaveqc.tyr.progression.mxp.mixin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.EnchantedCurseWitness;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.MxpReflect;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.util.RandomSource;
@Pseudo
@Mixin(targets={"net.favouriteless.enchanted.common.enchanted.circle_magic.rites.ForestRite"},remap=false)
public abstract class EnchantedForestEffectMixin{
    @WrapOperation(method="lambda$onTick$0(DDLnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos$MutableBlockPos;)V",at=@At(value="INVOKE",target="Lnet/minecraft/world/level/block/grower/TreeGrower;growTree(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/chunk/ChunkGenerator;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/util/RandomSource;)Z"),require=0,remap=false)
    private boolean tmm$tree(TreeGrower grower,ServerLevel level,ChunkGenerator generator,BlockPos pos,BlockState state,RandomSource random,Operation<Boolean> original){
        boolean result=original.call(grower,level,generator,pos,state,random);
        EnchantedCurseWitness.effect(this,level,"enchanted:forest",result);
        return result;
    }
}
