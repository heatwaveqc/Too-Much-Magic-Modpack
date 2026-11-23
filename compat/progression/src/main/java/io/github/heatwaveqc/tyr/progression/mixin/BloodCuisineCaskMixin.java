package io.github.heatwaveqc.tyr.progression.mixin;

import io.github.heatwaveqc.tyr.progression.runtime.cuisine.BloodCuisineNative;
import io.github.heatwaveqc.tyr.progression.runtime.cuisine.BloodCuisinePayload;
import io.github.heatwaveqc.tyr.progression.runtime.cuisine.BloodCuisinePolicy;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Optional pinned Brewin' and Chewin' 4.5.0 Aging Cask.
 *
 * Native Cask treats tagged bottles as distillates but BnCWineUtils.age does
 * NOT copy their blood value into WineContents. Copying only an explicit
 * 5x custom-data credit into the aged red wine is intentionally separate
 * from native effects, aging time, intoxication and wine capacity.
 *
 * Require at most ONE filled eligible blood vessel per red wine batch;
 * a re-aged blood wine cannot be refilled with another bottle, while
 * ordinary later effect-only aging remains native and preserves the charge.
 */
@Pseudo
@Mixin(targets = "umpaz.brewinandchewin.common.block.entity.AgingCaskBlockEntity", remap = false)
public abstract class BloodCuisineCaskMixin {
    @Shadow private NonNullList<ItemStack> lastInputs;

    @Unique private static boolean tmm$isBloodVessel(ItemStack stack) {
        if (stack.isEmpty()) return false;
        String id = BuiltInRegistries.ITEM.getKey(stack.getItem()).toString();
        return BloodCuisineNative.JAWR.equals(id) || BloodCuisineNative.IRONS.equals(id)
                || BloodCuisineNative.HEXEREI.equals(id);
    }

    @Unique private static boolean tmm$isRedWine(ItemStack stack) {
        return !stack.isEmpty()
                && BloodCuisineNative.RED_WINE.equals(
                        BuiltInRegistries.ITEM.getKey(stack.getItem()).toString());
    }

    @Unique private static int tmm$validate(Container container) {
        boolean one = false;
        int count = 0;
        for (int slot = 1; slot < 9; slot++) {
            ItemStack input = container.getItem(slot);
            if (!tmm$isBloodVessel(input)) continue;
            ++count;
            if (input.getCount() != 1 || BloodCuisineNative.verifiedBottleBlood(input) != 500)
                return -1;
            one = true;
        }
        if (!one) return 0;
        return BloodCuisinePolicy.validCaskBatch(
                tmm$isRedWine(container.getItem(0)), BloodCuisinePayload.credit(container.getItem(0)) > 0,
                count, 0, 1) ? 1 : -1;
    }

    @Inject(method = "canPlaceItem(ILnet/minecraft/world/item/ItemStack;)Z",
            at = @At("HEAD"), cancellable = true, require = 0, remap = false)
    private void tmm$onlyOneBloodVessel(int slot, ItemStack incoming,
                                        CallbackInfoReturnable<Boolean> result) {
        if (slot <= 0 || slot >= 9 || !tmm$isBloodVessel(incoming)) return;
        Container cask = (Container)(Object)this;
        if (!tmm$isRedWine(cask.getItem(0))
                || BloodCuisinePayload.credit(cask.getItem(0)) > 0
                || incoming.getCount() != 1
                || BloodCuisineNative.verifiedBottleBlood(incoming) != 500
                || tmm$validate(cask) != 0) result.setReturnValue(false);
    }

    @Inject(method = "canAge()Z", at = @At("HEAD"), cancellable = true,
            require = 0, remap = false)
    private void tmm$rejectInvalidBloodBatch(CallbackInfoReturnable<Boolean> result) {
        if (tmm$validate((Container)(Object)this) < 0) result.setReturnValue(false);
    }

    @Inject(method = "age(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V",
            at = @At("TAIL"), require = 0, remap = false)
    private void tmm$transferPaidBlood(Level level, BlockPos pos, CallbackInfo ci) {
        if (level.isClientSide() || lastInputs == null || lastInputs.size() < 9) return;
        ItemStack inputWine = lastInputs.get(0);
        if (!tmm$isRedWine(inputWine) || BloodCuisinePayload.credit(inputWine) != 0) return;
        int bottles = 0;
        for (int slot = 1; slot < 9; slot++) {
            ItemStack bottle = lastInputs.get(slot);
            if (!tmm$isBloodVessel(bottle)) continue;
            if (bottle.getCount() != 1 || BloodCuisineNative.verifiedBottleBlood(bottle) != 500) return;
            bottles++;
        }
        if (bottles != 1) return;
        Container cask = (Container)(Object)this;
        ItemStack output = cask.getItem(9);
        if (!tmm$isRedWine(output) || BloodCuisinePayload.credit(output) != 0) return;
        BloodCuisinePayload.set(output,
                BloodCuisinePolicy.bloodFor(BloodCuisinePolicy.Product.WINE, 500));
        ((BlockEntity)(Object)this).setChanged();
    }

    @Inject(method = "getContainerItem(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/ItemStack;",
            at = @At("HEAD"), cancellable = true, require = 0, remap = false)
    private static void tmm$returnOneEmptyGlass(ItemStack original,
                                                 CallbackInfoReturnable<ItemStack> result) {
        if (original.getCount() == 1 && tmm$isBloodVessel(original)
                && BloodCuisineNative.verifiedBottleBlood(original) == 500)
            result.setReturnValue(new ItemStack(Items.GLASS_BOTTLE));
    }
}
