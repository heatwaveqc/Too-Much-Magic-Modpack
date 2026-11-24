package io.github.heatwaveqc.tmm.economy.mixin;

import io.github.heatwaveqc.tmm.economy.runtime.enchanting.VirtualShelfMenus;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Disable legacy book credits/refunds, including stale menu buttons.
 * Native removed() still refunds the transient input slot, but its delayed
 * reopen callback must not reopen the obsolete numeric enchanting screen. */
@Pseudo
@Mixin(targets = "org.zeith.improvableskills.client.gui.abil.ench.ContainerEnchPowBook", remap = false)
public abstract class PortableEnchantingLegacyBooksMixin {
    @Inject(method = "clickMenuButton(Lnet/minecraft/world/entity/player/Player;I)Z",
        at = @At("HEAD"), cancellable = true, require = 1, remap = false)
    private void tmm$disableOldBookPower(Player clicker, int button, CallbackInfoReturnable<Boolean> cir) {
        if (!(clicker instanceof ServerPlayer owner)) return;
        AbstractContainerMenu self = (AbstractContainerMenu) (Object) this;
        cir.setReturnValue(owner.containerMenu == self && button == 1
            && self.getCarried().isEmpty() && VirtualShelfMenus.openFromNative(owner, false));
    }

    @Inject(method = "lambda$removed$2(Lnet/minecraft/world/entity/player/Player;)V",
        at = @At("HEAD"), cancellable = true, require = 1, remap = false)
    private static void tmm$noDelayedLegacyReopen(Player player, CallbackInfo ci) {
        if (player instanceof ServerPlayer) ci.cancel();
    }
}
