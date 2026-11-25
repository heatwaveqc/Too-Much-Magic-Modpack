package io.github.heatwaveqc.tmm.economy.mixin;

import io.github.heatwaveqc.tmm.economy.runtime.enchanting.VirtualShelfMenus;
import io.github.heatwaveqc.tmm.economy.runtime.enchanting.PortableEnchantingCutover;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.zeith.improvableskills.api.PlayerSkillData;
import org.zeith.improvableskills.init.AbilitiesIS;

/** Exact native ability packet callback. Staged with both legacy menu guards;
 * do not enable separately or migrate while old numeric power is usable. */
@Pseudo
@Mixin(targets = "org.zeith.improvableskills.net.PacketOpenPortableEnch", remap = false)
public abstract class PortableEnchantingOpenMixin implements PortableEnchantingCutover.AbilityRoute {
    @Inject(method = "lambda$serverExecute$0(Lnet/minecraft/server/level/ServerPlayer;Lorg/zeith/improvableskills/api/PlayerSkillData;)V",
        at = @At("HEAD"), cancellable = true, require = 1, remap = false)
    private static void tmm$openNativeApothic(ServerPlayer owner, PlayerSkillData data, CallbackInfo ci) {
        ci.cancel();
        if (owner != null && data != null && AbilitiesIS.ENCHANTING.registered()
            && data.hasAbility(AbilitiesIS.ENCHANTING))
            VirtualShelfMenus.openFromNative(owner, false);
    }
}
