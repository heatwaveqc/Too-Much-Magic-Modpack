package io.github.heatwaveqc.tyr.progression.mxp.mixin;

import io.github.heatwaveqc.tyr.progression.mxp.hooks.ForgeRitualCompletion;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.MxpReflect;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets="com.stal111.forbidden_arcanus.common.block.entity.forge.HephaestusForgeBlockEntity",remap=false)
public abstract class ForgeRitualInstallMixin {
    // Verified 2.6.1 lambda used ONLY for ritualManager.tick().ifPresent(stack -> setStack(MAIN_SLOT, stack)).
    @Inject(method="lambda$serverTick$4(Lcom/stal111/forbidden_arcanus/common/block/entity/forge/HephaestusForgeBlockEntity;Lnet/minecraft/world/item/ItemStack;)V",
        at=@At("RETURN"),require=0,remap=false)
    private static void tmm$installed(@Coerce Object forge,ItemStack result,CallbackInfo ci){
        if(!ForgeRitualCompletion.supported())return;
        Object manager=MxpReflect.call(forge,"getRitualManager");
        if(manager instanceof ForgeRitualCompletion.Provider provider){
            var receipt=provider.tmm$takeForgeReceipt();
            if(receipt!=null)receipt.installed(forge,manager,result);
        }
    }
}
