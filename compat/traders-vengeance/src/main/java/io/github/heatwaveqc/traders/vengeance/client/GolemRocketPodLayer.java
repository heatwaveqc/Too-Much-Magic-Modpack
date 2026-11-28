package io.github.heatwaveqc.traders.vengeance.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.heatwaveqc.traders.vengeance.network.GolemVisualState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.IronGolemModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.IronGolemRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * Experimental left-shoulder rocket pod. A vanilla dispenser's textured
 * block model makes an intentionally simple, cosmetic, forward-facing barrel.
 * No dispenser item is equipped or obtainable from the golem.
 */
public final class GolemRocketPodLayer extends RenderLayer<IronGolem, IronGolemModel<IronGolem>> {
    private static final ItemStack POD = new ItemStack(Items.DISPENSER);

    public GolemRocketPodLayer(IronGolemRenderer renderer) {
        super(renderer);
    }

    @Override public void render(PoseStack poses, MultiBufferSource buffers, int light,
            IronGolem golem, float limbSwing, float limbSwingAmount, float partialTick,
            float ageInTicks, float netHeadYaw, float headPitch) {
        var state = GolemVisualState.get(golem.getUUID());
        if (golem.isPlayerCreated() || golem.isInvisible()
                || state == null || !state.freshDeployment()) return;
        poses.pushPose();
        try {
            // Mirror the accepted right-side minigun mount on the other shoulder.
            poses.translate(0.72D, -0.35D, -0.35D);
            poses.mulPose(Axis.YP.rotationDegrees(netHeadYaw));
            poses.mulPose(Axis.XP.rotationDegrees(headPitch));
            poses.scale(0.65F, 0.48F, 0.75F);
            Minecraft.getInstance().getItemRenderer().render(POD, ItemDisplayContext.FIXED,
                false, poses, buffers, light, OverlayTexture.NO_OVERLAY,
                Minecraft.getInstance().getItemRenderer().getModel(POD,
                    golem.level(), null, golem.getId()));
        } finally {
            poses.popPose();
        }
    }
}
