package io.github.heatwaveqc.tmm.economy.mixin;

import io.github.heatwaveqc.tmm.economy.runtime.PersonalEnchantFilterService;
import io.github.heatwaveqc.tmm.economy.runtime.enchanting.PersonalEnchantFilter;
import java.util.ArrayList;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Exact Eidolon 0.5.0.8 ABI. Second ArrayList.isEmpty in getEnchantmentList
 * occurs after native restrictions and compatibility pruning, immediately
 * before the native empty-return/RNG branch. The first isEmpty is inside
 * the compatibility loop and is deliberately not intercepted.
 */
@Pseudo
@Mixin(targets = "alexthw.eidolon_repraised.gui.SoulEnchanterContainer", remap = false)
public abstract class EidolonPersonalFilterMixin {
    @Shadow @Final private Container tableInventory;
    @Unique private Player tmm$owner;
    @Unique private PersonalEnchantFilter tmm$personalPreview;

    @Inject(method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/inventory/ContainerLevelAccess;)V",
        at = @At("RETURN"), require = 1, remap = false)
    private void tmm$captureOwner(int id, Inventory inventory, ContainerLevelAccess access, CallbackInfo ci) {
        tmm$owner = inventory.player;
    }

    @Redirect(method = "getEnchantmentList(Lnet/minecraft/core/RegistryAccess;Lnet/minecraft/world/item/ItemStack;I)Ljava/util/List;",
        at = @At(value = "INVOKE", target = "Ljava/util/ArrayList;isEmpty()Z", ordinal = 1),
        require = 1, remap = false)
    private boolean tmm$filterBeforeSampling(ArrayList<Holder<Enchantment>> candidates) {
        if (tmm$owner instanceof ServerPlayer owner)
            PersonalEnchantFilterService.removeExcludedRandomCandidates(owner, candidates);
        return candidates.isEmpty();
    }

    @Inject(method = "slotsChanged(Lnet/minecraft/world/Container;)V", at = @At("RETURN"), require = 1, remap = false)
    private void tmm$rememberPreview(Container changed, CallbackInfo ci) {
        if (tmm$owner instanceof ServerPlayer owner)
            tmm$personalPreview = PersonalEnchantFilterService.snapshot(owner);
    }

    @Inject(method = "clickMenuButton(Lnet/minecraft/world/entity/player/Player;I)Z",
        at = @At("HEAD"), cancellable = true, require = 1, remap = false)
    private void tmm$rejectStalePreview(Player clicker, int button, CallbackInfoReturnable<Boolean> cir) {
        if (!(tmm$owner instanceof ServerPlayer owner) || clicker != owner || button < 0 || button > 2) return;
        var now = PersonalEnchantFilterService.snapshot(owner);
        var prior = tmm$personalPreview == null ? PersonalEnchantFilter.locked() : tmm$personalPreview;
        if (!now.unlocked() && !prior.unlocked()) return;
        if (!now.equals(prior)) {
            AbstractContainerMenu menu = (AbstractContainerMenu) (Object) this;
            menu.slotsChanged(tableInventory);
            menu.broadcastChanges();
            cir.setReturnValue(false);
        }
    }
}
