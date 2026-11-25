package io.github.heatwaveqc.tmm.economy.mixin;

import io.github.heatwaveqc.tmm.economy.NeoFlaskDoseGuard;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Occultism 1.227.0 DemonicPartner applies all mirrored POTION_CONTENTS
 * effects, then shrinks one ITEM and gives a bottle. Protect Neo Vitae's
 * eight-charge flasks without touching native partner x5 potions, x50 stew,
 * instant-effect bonuses, food, sitting or taming behavior.
 */
@Pseudo
@Mixin(targets = "com.klikli_dev.occultism.common.entity.spirit.demonicpartner.DemonicPartner", remap = false)
public abstract class OccultismPartnerFlaskSafetyMixin {
    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true,
            remap = false, require = 1)
    private void tmm$refuseMultiUseFlask(Player player, InteractionHand hand,
            CallbackInfoReturnable<InteractionResult> cir) {
        if (NeoFlaskDoseGuard.mustRefuseOneBottleConversion(player.getItemInHand(hand))) {
            if (!player.level().isClientSide()) {
                player.displayClientMessage(Component.translatable(
                        "message.tmm_economy.alchemy.multiuse_flask_refused"), true);
            }
            // Consume only the interaction event. The item remains exactly
            // the same, no bottle or effect is produced and no use is debited.
            cir.setReturnValue(InteractionResult.CONSUME);
        }
    }
}
