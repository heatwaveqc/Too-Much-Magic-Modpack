package io.github.heatwaveqc.tmm.economy.mixin;

import dev.shadowsoffire.apotheosis.item.PotionCharmItem;
import io.github.heatwaveqc.tmm.economy.ApotheosisCharmPotionBridge;
import net.minecraft.core.Holder;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Keep registered-potion discovery (including EMIsemble/JEI) consistent with
 * TMM's actual charm recipe. No dependency on either recipe viewer. */
@Pseudo
@Mixin(value = PotionCharmItem.class, remap = false)
public abstract class ApotheosisPotionCharmEligibilityMixin {
    @Inject(method = "isValidPotion(Lnet/minecraft/core/Holder;)Z",
            at = @At("HEAD"), cancellable = true, remap = false, require = 1)
    private static void tmm$registeredPotionEligibility(Holder<Potion> potion,
            CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(potion != null && potion.isBound()
                && ApotheosisCharmPotionBridge.permits(new PotionContents(potion)));
    }
}
