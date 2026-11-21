package io.github.heatwaveqc.tmm.progression.mxp.mixin;

import io.github.heatwaveqc.tmm.progression.mxp.hooks.PsiCastHooks;
import net.minecraft.world.item.ItemStack;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Psi 110's direct PlayerData.tick loopcast path. No craft, idle tick or unrelated deduction XP. */
@Pseudo
@Mixin(targets="vazkii.psi.common.core.handler.PlayerData",remap=false)
public abstract class PsiLoopcastMixin {
    @Inject(method="tick",at=@At("HEAD"),require=0,remap=false)
    private void tmm$beginTick(CallbackInfo ci){PsiCastHooks.clearLoop();}

    @WrapOperation(method="tick",at=@At(value="INVOKE",target="Lvazkii/psi/common/spell/PsiSpellCosts;realCost(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;I)I"),require=0,remap=false)
    private int tmm$loopQuote(ItemStack cad,ItemStack bullet,int nominal,Operation<Integer> original){
        return PsiCastHooks.loopPrice(this,cad,bullet,nominal,original.call(cad,bullet,nominal));
    }

    @WrapOperation(method="tick",at=@At(value="INVOKE",target="Lvazkii/psi/common/core/handler/PlayerData;deductPsi(IIZ)V"),require=0,remap=false)
    private void tmm$loopDebit(@Coerce Object data,int psi,int cooldown,boolean sync,Operation<Void> original){
        var before=PsiCastHooks.beforeLoop(data,psi);
        original.call(data,psi,cooldown,sync);
        PsiCastHooks.afterLoop(data,psi,before);
    }

    @WrapOperation(method="tick",at=@At(value="INVOKE",target="Lvazkii/psi/api/spell/ISpellAcceptor;loopcastSpell(Lvazkii/psi/api/spell/SpellContext;)Z"),require=0,remap=false)
    private boolean tmm$loopEffect(@Coerce Object acceptor,@Coerce Object ctx,Operation<Boolean> original){
        boolean result=original.call(acceptor,ctx);
        PsiCastHooks.loopReturned(this,ctx,result);
        return result;
    }

    @Inject(method="tick",at=@At("RETURN"),require=0,remap=false)
    private void tmm$endTick(CallbackInfo ci){PsiCastHooks.clearLoop();}
}
