package io.github.heatwaveqc.tmm.friendship;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

/** Render each original registered flower species while its crafter is online.
 *  Four generic offline/death presentations use installed vanilla plant art;
 *  no external flower texture is bundled or duplicated.
 */
@EventBusSubscriber(modid = FriendshipGardenMod.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class FriendshipClient {
    private FriendshipClient() {}

    @SubscribeEvent
    public static void register(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(FriendshipGardenMod.FLOWER_ENTITY.get(), Renderer::new);
    }

    private static final class Renderer implements BlockEntityRenderer<FriendshipBlockEntity> {
        Renderer(BlockEntityRendererProvider.Context ignored) {}

        @Override
        public void render(FriendshipBlockEntity entity, float partialTick, PoseStack pose,
                           MultiBufferSource buffers, int light, int overlay) {
            FriendshipFlower flower = entity.flower();
            if (flower == null) return;
            boolean tall = flower.form() == FriendshipFlower.Form.TALL;
            BlockState state = switch (entity.appearance()) {
                case BUDDING -> tall ? Blocks.TALL_GRASS.defaultBlockState()
                                   : Blocks.FERN.defaultBlockState();
                case WILTED -> Blocks.DEAD_BUSH.defaultBlockState();
                case BLOOMING -> {
                    ResourceLocation id = ResourceLocation.tryParse(flower.originalFlowerId());
                    yield id == null || !BuiltInRegistries.BLOCK.containsKey(id)
                            ? (tall ? Blocks.SUNFLOWER : Blocks.POPPY).defaultBlockState()
                            : BuiltInRegistries.BLOCK.get(id).defaultBlockState();
                }
            };
            if (tall && state.hasProperty(DoublePlantBlock.HALF)) {
                renderPart(state.setValue(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER),
                        pose, buffers, light, overlay);
                pose.pushPose();
                pose.translate(0, 1, 0);
                renderPart(state.setValue(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER),
                        pose, buffers, light, overlay);
                pose.popPose();
            } else {
                renderPart(state, pose, buffers, light, overlay);
                if (tall) {
                    // Distinct tall wilt form without extra copyrighted textures.
                    pose.pushPose();
                    pose.translate(0, 1, 0);
                    renderPart(state, pose, buffers, light, overlay);
                    pose.popPose();
                }
            }
        }

        private void renderPart(BlockState state, PoseStack pose, MultiBufferSource buffer,
                                int light, int overlay) {
            Minecraft.getInstance().getBlockRenderer().renderSingleBlock(state, pose, buffer, light, overlay);
        }
    }
}
