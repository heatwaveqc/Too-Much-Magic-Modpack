package io.github.heatwaveqc.tmm.economy.mixin;

import io.github.heatwaveqc.tmm.economy.NeoFlaskDoseGuard;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Exact pinned Iron's 3.16.3 dynamic fill accepts ANY POTION_CONTENTS item,
 * returns GLASS_BOTTLE and charges 250 mB; Neo Vitae's mirrored effect
 * component is not a single-use bottle. Refuse before the fluid transaction.
 *
 * The independent handleUse guard also prevents insertion through later
 * fallback branches after tryExecuteRecipeInteractions returns empty.
 */
@Pseudo
@Mixin(targets = "io.redspace.ironsspellbooks.block.alchemist_cauldron.AlchemistCauldronTile", remap = false)
public abstract class IronsAlchemistFlaskSafetyMixin {
    @Inject(method = "tryExecuteRecipeInteractions", at = @At("HEAD"), cancellable = true,
            remap = false, require = 1)
    private void tmm$refuseMultiUseFlaskConversion(Level level, ItemStack input,
            CallbackInfoReturnable<ItemStack> cir) {
        if (NeoFlaskDoseGuard.mustRefuseOneBottleConversion(input)) {
            cir.setReturnValue(ItemStack.EMPTY);
        }
    }

    @Inject(method = "handleUse", at = @At("HEAD"), cancellable = true,
            remap = false, require = 1)
    private void tmm$refuseMultiUseFlaskHandIn(BlockState state, Level level,
            BlockPos pos, Player player, InteractionHand hand,
            CallbackInfoReturnable<ItemInteractionResult> cir) {
        if (NeoFlaskDoseGuard.mustRefuseOneBottleConversion(player.getItemInHand(hand))) {
            if (!level.isClientSide()) {
                player.displayClientMessage(Component.translatable(
                        "message.tmm_economy.alchemy.multiuse_flask_refused"), true);
            }
            // Consume the click, NOT a flask charge. No fluid insertion,
            // bottle remainder, recipe execution, or native flask damage.
            cir.setReturnValue(ItemInteractionResult.CONSUME);
        }
    }
}
