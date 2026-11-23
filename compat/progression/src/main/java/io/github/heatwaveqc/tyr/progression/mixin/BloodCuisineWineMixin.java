package io.github.heatwaveqc.tyr.progression.mixin;

import io.github.heatwaveqc.tyr.progression.runtime.cuisine.BloodCuisineNative;
import io.github.heatwaveqc.tyr.progression.runtime.cuisine.BloodCuisinePayload;
import io.github.heatwaveqc.tyr.progression.runtime.cuisine.BloodCuisinePolicy;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import java.util.List;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * WineItem.finishUsingItem is the native COMPLETED-drink path. Do not cancel
 * or replace native BnC wine status effects, Tipsy/Intoxication or glass return.
 * Aged wine retains its own WineContents; blood is separate immutable custom
 * data, never a PotionContents effect that could be fortified/extracted.
 */
@Pseudo
@Mixin(targets = "umpaz.brewinandchewin.common.item.WineItem", remap = false)
public abstract class BloodCuisineWineMixin {
    @Inject(method = "finishUsingItem(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;)Lnet/minecraft/world/item/ItemStack;",
            at = @At("HEAD"), cancellable = true, require = 0, remap = false)
    private void tmm$nativeWineBlood(ItemStack wine, Level level, LivingEntity consumer,
                                     CallbackInfoReturnable<ItemStack> result) {
        if (level.isClientSide() || !(consumer instanceof ServerPlayer player)
                || !BloodCuisineNative.RED_WINE.equals(
                        BuiltInRegistries.ITEM.getKey(wine.getItem()).toString()))
            return;
        int blood = BloodCuisinePayload.credit(wine);
        if (!BloodCuisinePolicy.validWinePayload(blood)
                || !BloodCuisineNative.isVampire(player)
                || player.getAbilities().instabuild) return;
        if (!BloodCuisineNative.canCreditNative()) {
            // Fail closed rather than drinking an expensive blood wine for 0 credit.
            result.setReturnValue(wine);
            return;
        }
        BloodCuisineNative.credit(player, blood);
    }

    @Inject(method = "appendHoverText", at = @At("TAIL"), require = 0, remap = false)
    private void tmm$bloodWineTooltip(ItemStack wine, Item.TooltipContext context,
                                      List<Component> lines, TooltipFlag flag, CallbackInfo ci) {
        if (BloodCuisineNative.RED_WINE.equals(
                BuiltInRegistries.ITEM.getKey(wine.getItem()).toString())
                && BloodCuisinePolicy.validWinePayload(BloodCuisinePayload.credit(wine)))
            lines.add(Component.translatable("tooltip.tyr_progression.blood_wine"));
    }
}
