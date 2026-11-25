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

/** Native matches still requires the shaped materials. Replace only potion
 * selection and output, for registered base and custom contents alike. */
@Pseudo
@Mixin(value = PotionCharmRecipe.class, remap = false)
public abstract class ApotheosisPotionCharmRecipeMixin {
    @Inject(method = "findPotion(Lnet/minecraft/world/item/crafting/CraftingInput;)Lnet/minecraft/world/item/alchemy/PotionContents;",
            at = @At("HEAD"), cancellable = true, remap = false, require = 1)
    private static void tmm$findLosslessContents(CraftingInput input,
            CallbackInfoReturnable<PotionContents> cir) {
        cir.setReturnValue(ApotheosisCharmPotionBridge.inspect(input));
    }

    @Inject(method = "assemble(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/core/HolderLookup$Provider;)Lnet/minecraft/world/item/ItemStack;",
            at = @At("HEAD"), cancellable = true, remap = false, require = 1)
    private void tmm$assembleLosslessCharm(CraftingInput input, HolderLookup.Provider registries,
            CallbackInfoReturnable<ItemStack> cir) {
        PotionContents contents = ApotheosisCharmPotionBridge.inspect(input);
        if (contents == PotionContents.EMPTY) {
            cir.setReturnValue(ItemStack.EMPTY);
            return;
        }
        ItemStack charm = new ItemStack(Apoth.Items.POTION_CHARM);
        charm.set(DataComponents.POTION_CONTENTS, contents);
        cir.setReturnValue(charm);
    }
}
