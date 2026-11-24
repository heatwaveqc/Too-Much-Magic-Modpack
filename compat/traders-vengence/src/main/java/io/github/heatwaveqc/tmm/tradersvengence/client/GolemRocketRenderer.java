package io.github.heatwaveqc.tmm.tradersvengence.client;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.heatwaveqc.tmm.tradersvengence.runtime.GolemRocket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/** An independently rendered black fire-charge silhouette; vanilla ghast fireballs unaffected. */
public final class GolemRocketRenderer extends EntityRenderer<GolemRocket> {
    public static final ModelResourceLocation MODEL = ModelResourceLocation.standalone(
        ResourceLocation.fromNamespaceAndPath("traders_vengence", "item/golem_rocket"));
    private static final ItemStack TOKEN = new ItemStack(Items.FIRE_CHARGE);
    private static final ResourceLocation TEXTURE =
        ResourceLocation.fromNamespaceAndPath("traders_vengence", "textures/item/golem_rocket.png");

    public GolemRocketRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override public void render(GolemRocket rocket, float yaw, float partialTick,
            PoseStack poses, MultiBufferSource buffers, int light) {
        Minecraft mc = Minecraft.getInstance();
        var model = mc.getModelManager().getModel(MODEL);
        if (model == mc.getModelManager().getMissingModel()) return;
        poses.pushPose();
        poses.scale(1.6F, 1.6F, 1.6F);
        poses.mulPose(entityRenderDispatcher.cameraOrientation());
        mc.getItemRenderer().render(TOKEN, ItemDisplayContext.GROUND, false,
            poses, buffers, light, OverlayTexture.NO_OVERLAY, model);
        poses.popPose();
        super.render(rocket, yaw, partialTick, poses, buffers, light);
    }

    @Override public ResourceLocation getTextureLocation(GolemRocket rocket) {
        return TEXTURE;
    }
}
