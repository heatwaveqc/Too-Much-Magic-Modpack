package io.github.heatwaveqc.tmm.transformations.mixin;

import io.github.heatwaveqc.tmm.transformations.client.DracolichModelLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Client-only, participates in GeckoLib's normal reload/cache/bake lifecycle. */
@Pseudo
@Mixin(targets = "software.bernie.geckolib.loading.FileLoader", remap = false)
public abstract class DracolichModelLoadMixin {
    @Inject(method = "loadModelFile(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/server/packs/resources/ResourceManager;)Lsoftware/bernie/geckolib/loading/json/raw/Model;",
            at = @At("HEAD"), cancellable = true, remap = false)
    private static void tmm$assemble(ResourceLocation location, ResourceManager resources,
                                     CallbackInfoReturnable<Object> cir) {
        if (DracolichModelLoader.MODEL.equals(location)) cir.setReturnValue(DracolichModelLoader.load(resources));
    }
}
