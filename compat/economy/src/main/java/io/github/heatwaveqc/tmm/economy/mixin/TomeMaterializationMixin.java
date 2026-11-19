package io.github.heatwaveqc.tmm.economy.mixin;

import io.github.heatwaveqc.tmm.economy.TomeTemplatePolicy;
import io.github.heatwaveqc.tmm.economy.RelicTemplatePolicy;
import moze_intel.projecte.api.ItemInfo;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Defense in depth for old serialized ProjectE templates and cached knowledge.
 * Only ItemInfo materialization is affected; never strip a physical inventory Tome or relic. */
@Mixin(value = ItemInfo.class, remap = false)
public abstract class TomeMaterializationMixin {
    @Inject(method = "createStack", at = @At("HEAD"), cancellable = true)
    private void tmm$materializeEmptyTome(CallbackInfoReturnable<ItemStack> cir) {
        ItemInfo info = (ItemInfo)(Object)this;
        if (info.hasModifiedComponents() && (TomeTemplatePolicy.applies(info) || RelicTemplatePolicy.applies(info)))
            cir.setReturnValue(info.itemOnly().createStack());
    }
}
