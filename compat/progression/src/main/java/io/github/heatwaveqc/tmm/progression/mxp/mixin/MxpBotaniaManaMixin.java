package io.github.heatwaveqc.tmm.progression.mxp.mixin;

import io.github.heatwaveqc.tmm.progression.mxp.hooks.MxpPortableManaHook;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Central Botania Revived mana handler. Includes mana spent to repair worn
 * equipment when paid by a portable tablet or mana ring.
 * DO NOT intercept dispatchMana (that is a transfer, not expenditure).
 */
@Pseudo
@Mixin(targets = "velrondevs.botania.common.impl.mana.ManaItemHandlerImpl", remap = false)
public abstract class MxpBotaniaManaMixin {
    @Inject(method = "requestMana", at = @At("HEAD"), remap = false, require = 0)
    private void tmm$beforeRequest(ItemStack requester, Player player, int amount,
                                    boolean remove, CallbackInfoReturnable<Integer> cir) {
        if (!io.github.heatwaveqc.tmm.progression.mxp.MxpMod.active()) return;
        MxpPortableManaHook.beforeRequest(requester, player, amount, remove);
    }

    @Inject(method = "requestMana", at = @At("RETURN"), remap = false, require = 0)
    private void tmm$afterRequest(ItemStack requester, Player player, int amount,
                                   boolean remove, CallbackInfoReturnable<Integer> cir) {
        int granted = cir.getReturnValue();
        if (!io.github.heatwaveqc.tmm.progression.mxp.MxpMod.active()) return;
        MxpPortableManaHook.afterRequest(granted, remove && granted > 0);
    }

    @Inject(method = "requestManaExact", at = @At("HEAD"), remap = false, require = 0)
    private void tmm$beforeExact(ItemStack requester, Player player, int amount,
                                  boolean remove, CallbackInfoReturnable<Boolean> cir) {
        if (!io.github.heatwaveqc.tmm.progression.mxp.MxpMod.active()) return;
        MxpPortableManaHook.beforeRequest(requester, player, amount, remove);
    }

    @Inject(method = "requestManaExact", at = @At("RETURN"), remap = false, require = 0)
    private void tmm$afterExact(ItemStack requester, Player player, int amount,
                                 boolean remove, CallbackInfoReturnable<Boolean> cir) {
        boolean paid = cir.getReturnValue();
        if (!io.github.heatwaveqc.tmm.progression.mxp.MxpMod.active()) return;
        MxpPortableManaHook.afterRequest(paid ? amount : 0, remove && paid);
    }

    @Inject(method = "requestManaForTool", at = @At("HEAD"), remap = false, require = 0)
    private void tmm$beforeTool(ItemStack tool, Player player, int amount,
                                 boolean remove, CallbackInfoReturnable<Integer> cir) {
        if (!io.github.heatwaveqc.tmm.progression.mxp.MxpMod.active()) return;
        MxpPortableManaHook.beginTool(amount);
    }

    @Inject(method = "requestManaForTool", at = @At("RETURN"), remap = false, require = 0)
    private void tmm$afterTool(ItemStack tool, Player player, int amount,
                                boolean remove, CallbackInfoReturnable<Integer> cir) {
        if (!io.github.heatwaveqc.tmm.progression.mxp.MxpMod.active()) return;
        MxpPortableManaHook.endTool();
    }

    @Inject(method = "requestManaExactForTool", at = @At("HEAD"), remap = false, require = 0)
    private void tmm$beforeExactTool(ItemStack tool, Player player, int amount,
                                      boolean remove, CallbackInfoReturnable<Boolean> cir) {
        if (!io.github.heatwaveqc.tmm.progression.mxp.MxpMod.active()) return;
        MxpPortableManaHook.beginTool(amount);
    }

    @Inject(method = "requestManaExactForTool", at = @At("RETURN"), remap = false, require = 0)
    private void tmm$afterExactTool(ItemStack tool, Player player, int amount,
                                     boolean remove, CallbackInfoReturnable<Boolean> cir) {
        if (!io.github.heatwaveqc.tmm.progression.mxp.MxpMod.active()) return;
        MxpPortableManaHook.endTool();
    }
}
