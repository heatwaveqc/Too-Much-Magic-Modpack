package io.github.heatwaveqc.tmm.tradersvengence.client;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.heatwaveqc.tmm.tradersvengence.network.GolemVisualState;
import net.minecraft.client.model.IronGolemModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.IronGolemRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.IronGolem;

/** Client-only cosmetic layer; no weapon item or inventory slot. */
public final class GolemMinigunLayer extends RenderLayer<IronGolem, IronGolemModel<IronGolem>> {
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(
        ResourceLocation.fromNamespaceAndPath("traders_vengence", "golem_minigun"), "main");
    private final GolemMinigunModel gun;
    public GolemMinigunLayer(IronGolemRenderer renderer, EntityModelSet models) {
        super(renderer);
        gun = new GolemMinigunModel(models.bakeLayer(LAYER));
    }
    @Override public void render(PoseStack poses, MultiBufferSource buffers, int light,
            IronGolem golem, float limbSwing, float limbSwingAmount, float partialTick,
            float ageInTicks, float netHeadYaw, float headPitch) {
        var state = GolemVisualState.get(golem.getUUID());
        if (golem.isPlayerCreated() || golem.isInvisible() || state == null) return;
        poses.pushPose();
        // Provisional shoulder mount: validate scale/orientation in live client.
        poses.translate(-0.55D, 0.45D, -0.35D);
        gun.renderMinigun(poses, buffers, light, LivingEntityRenderer.getOverlayCoords(golem, 0F),
            state.predictedAngle(), netHeadYaw, headPitch, true);
        poses.popPose();
    }
}
