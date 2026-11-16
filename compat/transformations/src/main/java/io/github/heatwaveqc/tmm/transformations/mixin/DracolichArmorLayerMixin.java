package io.github.heatwaveqc.tmm.transformations.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.heatwaveqc.tmm.transformations.client.DracolichArmorRenderer;
import io.github.heatwaveqc.tmm.transformations.client.DracolichClientVisuals;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "by.dragonsurvivalteam.dragonsurvival.client.render.entity.dragon.DragonArmorRenderLayer", remap = false)
public abstract class DracolichArmorLayerMixin {
    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lby/dragonsurvivalteam/dragonsurvival/common/entity/DragonEntity;Lsoftware/bernie/geckolib/cache/object/BakedGeoModel;Lnet/minecraft/client/renderer/RenderType;Lnet/minecraft/client/renderer/MultiBufferSource;Lcom/mojang/blaze3d/vertex/VertexConsumer;FII)V",
            at = @At("HEAD"), cancellable = true, remap = false)
    private void tmm$armor(PoseStack poses, @Coerce Object dragon, @Coerce Object baked,
                            RenderType type, MultiBufferSource buffers, VertexConsumer buffer,
                            float partialTick, int light, int overlay, CallbackInfo ci) {
        if (DracolichArmorRenderer.reentered() || !DracolichClientVisuals.applies(dragon)) return;
        DracolichArmorRenderer.render(this, poses, dragon, baked, type, buffers, buffer, partialTick, light, overlay);
        ci.cancel();
    }
}
