package io.github.heatwaveqc.tmm.economy.mixin;

import io.github.heatwaveqc.tmm.economy.CaptureInstancePolicy;
import java.util.Optional;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Guard native dynamic fragments and actual entity-targeted charm effects, not global loot. */
@Pseudo
@Mixin(targets = "reliquary.item.MobCharmRegistry", remap = false)
public abstract class ReliquaryCaptureMixin {
    @Inject(method = "handleAddingFragmentDrops(Lnet/neoforged/neoforge/event/entity/living/LivingDropsEvent;)V",
            at = @At("HEAD"), cancellable = true)
    private static void tmm$denyFragments(LivingDropsEvent event, CallbackInfo ci) {
        if (CaptureInstancePolicy.denyCharm(event.getEntity())) ci.cancel();
    }

    @Inject(method = "getCharmDefinitionFor(Lnet/minecraft/world/entity/Entity;)Ljava/util/Optional;",
            at = @At("HEAD"), cancellable = true)
    private static void tmm$denyProtectedCharm(Entity entity, CallbackInfoReturnable<Optional<?>> cir) {
        if (!entity.level().isClientSide() && CaptureInstancePolicy.denyCharm(entity)) cir.setReturnValue(Optional.empty());
    }
}
