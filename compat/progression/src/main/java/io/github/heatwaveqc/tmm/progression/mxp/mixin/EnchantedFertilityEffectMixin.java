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
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.monster.ZombieVillager;
@Pseudo
@Mixin(targets="net.favouriteless.enchanted.common.enchanted.circle_magic.rites.FertilityRite",remap=false)
public abstract class EnchantedFertilityEffectMixin{
    @WrapOperation(method="lambda$onTick$2(Lnet/minecraft/core/BlockPos$MutableBlockPos;)V",at=@At(value="INVOKE",target="Lnet/minecraft/world/level/block/BonemealableBlock;performBonemeal(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/util/RandomSource;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V"),require=0,remap=false)
    private void tmm$fertility(BonemealableBlock block,ServerLevel level,RandomSource random,BlockPos pos,BlockState state,Operation<Void> original){
        BlockState before=level.getBlockState(pos);
        original.call(block,level,random,pos,state);
        EnchantedCurseWitness.effect(this,level,"enchanted:fertility",!before.equals(level.getBlockState(pos)));
    }
    @WrapOperation(method="applyCureEffects(Ljava/util/UUID;Lnet/minecraft/world/entity/LivingEntity;)V",at=@At(value="INVOKE",target="Lnet/minecraft/world/entity/monster/ZombieVillager;startConverting(Ljava/util/UUID;I)V"),require=0,remap=false)
    private void tmm$cure(ZombieVillager zombie,java.util.UUID owner,int ticks,Operation<Void> original){
        boolean before=zombie.isConverting();
        original.call(zombie,owner,ticks);
        if(zombie.level() instanceof ServerLevel level)EnchantedCurseWitness.effect(this,level,"enchanted:fertility",!before&&zombie.isConverting());
    }
}
