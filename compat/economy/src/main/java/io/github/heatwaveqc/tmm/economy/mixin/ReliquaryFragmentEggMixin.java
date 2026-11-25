package io.github.heatwaveqc.tmm.economy.mixin;

import io.github.heatwaveqc.tmm.economy.CaptureInstancePolicy;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import reliquary.crafting.FragmentRecipeHelper;

/** Retain native recipe gates; never turn an existing denied fragment into a fresh egg. */
@Pseudo
@Mixin(targets = "reliquary.crafting.FragmentToSpawnEggRecipe", remap = false)
public abstract class ReliquaryFragmentEggMixin {
    @Unique
    private static boolean tmm$blockedFragment(CraftingInput input) {
        return FragmentRecipeHelper.getRegistryName(input)
                .map(CaptureInstancePolicy::denyFragmentEgg).orElse(false);
    }

    @Inject(method = "matches(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/world/level/Level;)Z",
            at = @At("RETURN"), cancellable = true)
    private void tmm$guardMatch(CraftingInput input, Level level, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue() && tmm$blockedFragment(input)) cir.setReturnValue(false);
    }

    @Inject(method = "assemble(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/core/HolderLookup$Provider;)Lnet/minecraft/world/item/ItemStack;",
            at = @At("HEAD"), cancellable = true)
    private void tmm$guardOutput(CraftingInput input, HolderLookup.Provider registries,
                                 CallbackInfoReturnable<ItemStack> cir) {
        if (tmm$blockedFragment(input)) cir.setReturnValue(ItemStack.EMPTY);
    }
}
