package io.github.heatwaveqc.tmm.progression.mxp.mixin;

import io.github.heatwaveqc.tmm.progression.mxp.hooks.MxpCastHooks;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Ars 'n Spells reserves mana before native Ars spends; commit is authoritative. */
@Pseudo
@Mixin(targets = "com.otectus.arsnspells.casting.ArsCastPayments", remap = false)
public abstract class MxpAnsArsCommitMixin {
    @Inject(method = "commit", at = @At("RETURN"), remap = false, require = 0)
    private static void tmm$afterAnsCommit(Player player, @Coerce Object ctx,
                                            CallbackInfoReturnable<Boolean> cir) {
        if (!io.github.heatwaveqc.tmm.progression.mxp.MxpMod.active()) return;
        MxpCastHooks.ansArsCommitted(player, ctx, Boolean.TRUE.equals(cir.getReturnValue()));
    }
}
