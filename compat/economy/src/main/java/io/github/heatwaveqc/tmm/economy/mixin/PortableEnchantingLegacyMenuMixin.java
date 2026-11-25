package io.github.heatwaveqc.tmm.economy.mixin;

import io.github.heatwaveqc.tmm.economy.runtime.enchanting.VirtualShelfMenus;
import io.github.heatwaveqc.tmm.economy.runtime.enchanting.PortableEnchantingCutover;
import io.github.heatwaveqc.tmm.economy.runtime.enchanting.PortableApothicMenu;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Block old numerical enchanting transactions after the coordinated cutover.
 * Preserve the original shelf-settings button as a route to the replacement. */
@Pseudo
@Mixin(targets = "org.zeith.improvableskills.client.gui.abil.ench.ContainerPortableEnchantment", remap = false)
public abstract class PortableEnchantingLegacyMenuMixin implements PortableEnchantingCutover.OldEnchantRoute {
    @Inject(method = "clickMenuButton(Lnet/minecraft/world/entity/player/Player;I)Z",
        at = @At("HEAD"), cancellable = true, require = 1, remap = false)
    private void tmm$redirectOldMenu(Player clicker, int button, CallbackInfoReturnable<Boolean> cir) {
        if (!(clicker instanceof ServerPlayer owner)) return;
        AbstractContainerMenu self = (AbstractContainerMenu) (Object) this;
        cir.setReturnValue(owner.containerMenu == self && button == PortableApothicMenu.SHELF_SETTINGS
            && self.getCarried().isEmpty() && VirtualShelfMenus.openFromNative(owner, true));
    }
}
