package io.github.heatwaveqc.tmm.economy.mixin;

import dev.shadowsoffire.apotheosis.Apoth;
import dev.shadowsoffire.apotheosis.recipe.PotionCharmRecipe;
import io.github.heatwaveqc.tmm.economy.ApotheosisCharmPotionBridge;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.crafting.CraftingInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Apotheosis 8.9.0: native recipe geometry, costs, matching and ordinary base
 * potion path remain intact. Intervene only for custom contents, including
 * rejecting mixed effects / extra carrier components before ingredients are
 * consumed. Serialize the whole approved PotionContents, never just its base.
 */
@Pseudo
@Mixin(value = PotionCharmRecipe.class, remap = false)
public abstract class ApotheosisPotionCharmRecipeMixin {
    @Inject(method = "findPotion(Lnet/minecraft/world/item/crafting/CraftingInput;)Lnet/minecraft/world/item/alchemy/PotionContents;",
            at = @At("HEAD"), cancellable = true, remap = false, require = 1)
    private static void tmm$findLosslessContents(CraftingInput input,
            CallbackInfoReturnable<PotionContents> cir) {
        var selection = ApotheosisCharmPotionBridge.inspect(input);
        if (selection.overrideNative()) cir.setReturnValue(selection.contents());
    }

    @Inject(method = "assemble(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/core/HolderLookup$Provider;)Lnet/minecraft/world/item/ItemStack;",
            at = @At("HEAD"), cancellable = true, remap = false, require = 1)
    private void tmm$assembleLosslessCharm(CraftingInput input, HolderLookup.Provider registries,
            CallbackInfoReturnable<ItemStack> cir) {
        var selection = ApotheosisCharmPotionBridge.inspect(input);
        if (!selection.overrideNative()) return;
        if (selection.contents() == PotionContents.EMPTY) {
            cir.setReturnValue(ItemStack.EMPTY);
            return;
        }
        ItemStack charm = new ItemStack(Apoth.Items.POTION_CHARM);
        charm.set(DataComponents.POTION_CONTENTS, selection.contents());
        cir.setReturnValue(charm);
    }
}
