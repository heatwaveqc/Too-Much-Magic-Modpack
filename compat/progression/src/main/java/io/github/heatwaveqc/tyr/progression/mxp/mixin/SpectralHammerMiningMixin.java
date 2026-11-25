package io.github.heatwaveqc.tyr.progression.mxp.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import io.github.heatwaveqc.tyr.progression.runtime.SpellMiningScope;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.List;

@Pseudo
@Mixin(targets="io.redspace.ironsspellbooks.entity.spells.spectral_hammer.SpectralHammer",remap=false)
public abstract class SpectralHammerMiningMixin {
    @Shadow private Player owner;
    @WrapMethod(method="tick",remap=false)
    private void tmm$hammer(Operation<Void> original){
        SpellMiningScope.hammer(owner,((Entity)(Object)this).level(),()->original.call());
    }
    @Inject(method="dropResources",at=@At("HEAD"),require=0,remap=false)
    private static void tmm$removed(BlockState state,Level level,BlockPos pos,CallbackInfoReturnable<List<ItemStack>> cir){
        SpellMiningScope.hammerCommitted(level,pos,state);
    }
}
