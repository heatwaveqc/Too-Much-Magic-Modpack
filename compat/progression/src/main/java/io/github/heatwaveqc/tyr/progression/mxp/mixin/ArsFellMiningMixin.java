package io.github.heatwaveqc.tyr.progression.mxp.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.heatwaveqc.tyr.progression.runtime.SpellMiningScope;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import java.util.Set;
import java.util.function.Consumer;

/** Scope each actual native Fell candidate, without depending on javac lambda names. */
@Pseudo
@Mixin(targets="com.hollingsworth.arsnouveau.common.spell.effect.EffectFell",remap=false)
public abstract class ArsFellMiningMixin {
    @WrapOperation(method="onResolveBlock",at=@At(value="INVOKE",
        target="Ljava/util/Set;forEach(Ljava/util/function/Consumer;)V"),require=0,remap=false)
    private void tmm$each(Set<BlockPos> positions,Consumer<BlockPos> consume,Operation<Void> original,
                         @Local(argsOnly=true) Level level,@Local(argsOnly=true) LivingEntity shooter){
        original.call(positions,(Consumer<BlockPos>)pos->SpellMiningScope.fellPosition(shooter,level,pos,()->consume.accept(pos)));
    }
}
