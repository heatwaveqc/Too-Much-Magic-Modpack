package io.github.heatwaveqc.tmm.transformations.mixin;

import io.github.heatwaveqc.tmm.transformations.client.DracolichClientVisuals;
import io.github.heatwaveqc.tmm.transformations.client.DracolichModelLoader;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = "by.dragonsurvivalteam.dragonsurvival.client.models.DragonModel", remap = false)
public abstract class DracolichDragonModelMixin {
    @Inject(method = "getModelResource(Lby/dragonsurvivalteam/dragonsurvival/common/entity/DragonEntity;)Lnet/minecraft/resources/ResourceLocation;",
            at = @At("HEAD"), cancellable = true, remap = false)
    private void tmm$model(@Coerce Object dragon, CallbackInfoReturnable<ResourceLocation> cir) {
        if (DracolichClientVisuals.applies(dragon)) cir.setReturnValue(DracolichModelLoader.MODEL);
    }

    @Inject(method = "getTextureResource(Lby/dragonsurvivalteam/dragonsurvival/common/entity/DragonEntity;)Lnet/minecraft/resources/ResourceLocation;",
            at = @At("HEAD"), cancellable = true, remap = false)
    private void tmm$texture(@Coerce Object dragon, CallbackInfoReturnable<ResourceLocation> cir) {
        if (DracolichClientVisuals.applies(dragon)) cir.setReturnValue(DracolichModelLoader.TEXTURE);
    }
}
