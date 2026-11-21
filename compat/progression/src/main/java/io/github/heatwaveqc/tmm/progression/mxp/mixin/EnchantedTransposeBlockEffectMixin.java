package io.github.heatwaveqc.tmm.progression.mxp.mixin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.heatwaveqc.tmm.progression.mxp.hooks.EnchantedCurseWitness;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
@Pseudo
@Mixin(targets="net.favouriteless.enchanted.common.enchanted.circle_magic.rites.TransposeBlocksRite",remap=false)
public abstract class EnchantedTransposeBlockEffectMixin{
    @WrapOperation(method="onTick(Lnet/favouriteless/enchanted/common/enchanted/circle_magic/rites/Rite$RiteParams;)Z",at=@At(value="INVOKE",target="Lnet/minecraft/server/level/ServerLevel;setBlockAndUpdate(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z"),require=0,remap=false)
    private boolean tmm$block(ServerLevel level,BlockPos pos,BlockState state,Operation<Boolean> original){
        BlockState before=level.getBlockState(pos);
        boolean accepted=original.call(level,pos,state);
        EnchantedCurseWitness.effect(this,level,"enchanted:transpose_blocks",accepted&&!before.equals(level.getBlockState(pos)));
        return accepted;
    }
}
