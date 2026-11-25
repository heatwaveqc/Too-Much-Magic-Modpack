package io.github.heatwaveqc.tmm.economy.mixin;

import io.github.heatwaveqc.tmm.economy.runtime.enchanting.PortableAnvilMenus;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.zeith.improvableskills.api.PlayerSkillData;
import org.zeith.improvableskills.init.AbilitiesIS;

/** Retain the native ability packet and authorization; only route its menu. */
@Pseudo
@Mixin(targets = "org.zeith.improvableskills.net.PacketOpenPortableAnvil", remap = false)
public abstract class PortableAnvilOpenMixin {
    @Inject(method = "lambda$serverExecute$0(Lnet/minecraft/server/level/ServerPlayer;Lorg/zeith/improvableskills/api/PlayerSkillData;)V",
        at = @At("HEAD"), cancellable = true, require = 1, remap = false)
    private static void tmm$openPortableAnvil(ServerPlayer owner, PlayerSkillData data, CallbackInfo ci) {
        if (owner != null && data != null && AbilitiesIS.ANVIL.registered()
            && data.hasAbility(AbilitiesIS.ANVIL) && PortableAnvilMenus.open(owner)) ci.cancel();
    }
}
