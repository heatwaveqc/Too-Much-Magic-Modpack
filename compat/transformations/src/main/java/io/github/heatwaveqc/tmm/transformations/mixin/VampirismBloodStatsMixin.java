package io.github.heatwaveqc.tmm.transformations.mixin;

import io.github.heatwaveqc.tmm.transformations.vampire.VampireChassisRuntime;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Ordinary vampires keep Vampirism blood untouched. Only dual / ascended
 * vampires divert Vampirism feeding and spending to canonical JAWR blood.
 * Disable Vampirism's FoodData/exhaustion/healing loop in that state so
 * JAWR's native hunger and recovery remain authoritative.
 */
@Pseudo
@Mixin(targets = "de.teamlapen.vampirism.entity.player.vampire.BloodStats", remap = false)
public abstract class VampirismBloodStatsMixin {
    @Shadow @Final private Player player;
    @Shadow private boolean changed;

    @Inject(method = "onUpdate()Z", at = @At("HEAD"), cancellable = true, remap = false)
    private void tmm$skipDuplicatedUpkeep(CallbackInfoReturnable<Boolean> cir) {
        if (player instanceof ServerPlayer sp && VampireChassisRuntime.isDual(sp)) {
            // A derived mirror write sets changed=true. Preserve the normal
            // VampirePlayer sync signal without executing Vampirism's
            // FoodData/exhaustion/healing logic a second time.
            boolean needsSync = changed;
            changed = false;
            cir.setReturnValue(needsSync);
        }
    }

    @Inject(method = "addBlood(IF)I", at = @At("HEAD"), cancellable = true, remap = false)
    private void tmm$redirectFeeding(int amount, float saturation,
            CallbackInfoReturnable<Integer> cir) {
        if (player instanceof ServerPlayer sp && VampireChassisRuntime.isDual(sp)) {
            cir.setReturnValue(VampireChassisRuntime.feedFromVampirism(sp, amount));
        }
    }

    @Inject(method = "removeBlood(IZ)Z", at = @At("HEAD"), cancellable = true, remap = false)
    private void tmm$redirectSpending(int amount, boolean allowPartial,
            CallbackInfoReturnable<Boolean> cir) {
        if (player instanceof ServerPlayer sp && VampireChassisRuntime.isDual(sp)) {
            // Never allow partial charges to power a full ability.
            cir.setReturnValue(VampireChassisRuntime.spendFromVampirism(sp, amount));
        }
    }
}
