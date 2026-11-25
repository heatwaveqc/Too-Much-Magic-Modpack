package io.github.heatwaveqc.tmm.transformations.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import io.github.heatwaveqc.tmm.transformations.magic.MahouCatalystDustBridge;
import java.util.List;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Pinned Mahou 1.36.31 (audit run 44013489360):
 * AboveChecker.checkAbove tests its native checkForItem list OR allowedItem
 * before ifItemFoundAbove. The exchange class inherits an empty default
 * checkForItem list. Add its concrete override, preserving the class test
 * for all native powders; never widen other circles' filters.
 *
 * The first ItemStack.getItem in ifItemFoundAbove is consumed ONLY by
 * instanceof PowderedCatalyst. Change that expression, not the stack or
 * entity. All subsequent reads, mana debits, RNG, output, shrink and discard
 * remain byte-for-byte native. No callback on global ticks or entity mutation.
 */
@Pseudo
@Mixin(targets = "stepsword.mahoutsukai.tile.exchange.CatalystExchangeMahoujinTileEntity", remap = false)
public abstract class MahouCatalystExchangeMixin {
    public List<Item> checkForItem() {
        return MahouCatalystDustBridge.explicitDustItems();
    }

    @ModifyExpressionValue(
        method = "ifItemFoundAbove(Lnet/minecraft/world/entity/item/ItemEntity;)Z",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getItem()Lnet/minecraft/world/item/Item;", ordinal = 0, remap = false),
        remap = false,
        require = 1
    )
    private Item tmm$acceptNeoVitaeAtNativeCatalystCheck(Item original) {
        return MahouCatalystDustBridge.eligibilityItem(original);
    }
}
