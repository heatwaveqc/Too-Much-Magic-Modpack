package io.github.heatwaveqc.tyr.progression.mixin;

import io.github.heatwaveqc.tyr.progression.runtime.VerifiedConsumptionXp;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Beachparty 2.1.5: a committed decrement of the native 0..3 serving counter is a sip. */
@Pseudo
@Mixin(targets="net.satisfy.beachparty.core.block.CocktailBlock",remap=false)
public abstract class BeachpartyElixirSipMixin {
    @Inject(method="useWithoutItem",at=@At("RETURN"),require=0,remap=false)
    private void tmm$settledSip(BlockState state,Level level,BlockPos pos,Player player,BlockHitResult hit,
                               CallbackInfoReturnable<InteractionResult> ci){
        if(level.isClientSide||!(player instanceof ServerPlayer p))return;
        var after=level.getBlockState(pos);
        if(after.getBlock()!=state.getBlock())return;
        for(var property:state.getProperties()){
            if(property instanceof IntegerProperty stage&&stage.getName().equals("stage")
                &&state.getValue(stage)>0&&after.hasProperty(stage)&&after.getValue(stage)==state.getValue(stage)-1){
                VerifiedConsumptionXp.finished(p,new ItemStack(state.getBlock().asItem()));
                return;
            }
        }
    }
}
