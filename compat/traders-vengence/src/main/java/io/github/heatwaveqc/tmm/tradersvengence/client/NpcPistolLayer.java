package io.github.heatwaveqc.tmm.tradersvengence.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.VillagerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/** Cosmetic-only standalone model. Render token never leaves the client renderer. */
public final class NpcPistolLayer<T extends AbstractVillager> extends RenderLayer<T, VillagerModel<T>> {
    public static final ModelResourceLocation MODEL = ModelResourceLocation.standalone(
        ResourceLocation.fromNamespaceAndPath("traders_vengence", "item/npc_pistol"));
    private static final ItemStack RENDER_TOKEN = new ItemStack(Items.STICK);

    public NpcPistolLayer(RenderLayerParent<T, VillagerModel<T>> renderer) { super(renderer); }

    @Override public void render(PoseStack poses, MultiBufferSource buffers, int light,
            T defender, float limbSwing, float limbSwingAmount, float partialTick,
            float ageInTicks, float netHeadYaw, float headPitch) {
        if (defender.isInvisible() || defender.isSleeping()) return;
        Minecraft mc = Minecraft.getInstance();
        var model = mc.getModelManager().getModel(MODEL);
        if (model == mc.getModelManager().getMissingModel()) return;
        poses.pushPose();
        try {
            // Provisional folded-arm attachment; tune adult/baby offsets in a live client.
            poses.translate(0.26D, defender.isBaby() ? 0.98D : 0.48D, -0.40D);
            if (defender.isBaby()) poses.scale(0.65F, 0.65F, 0.65F);
            poses.mulPose(Axis.YP.rotationDegrees(netHeadYaw));
            poses.mulPose(Axis.XP.rotationDegrees(headPitch - 12F * defender.getAttackAnim(partialTick)));
            mc.getItemRenderer().render(RENDER_TOKEN, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND,
                false, poses, buffers, light, OverlayTexture.NO_OVERLAY, model);
        } finally {
            poses.popPose();
        }
    }
}
