package io.github.heatwaveqc.tmm.transformations.client;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.heatwaveqc.tmm.transformations.TransformationsMod;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;

/** Matches the native eye render pass using our per-player atlas and baked mesh. */
public final class DracolichGlowRenderer {
    private static boolean warned;
    private DracolichGlowRenderer() { }

    public static void render(Object layer, PoseStack poses, Object dragon, Object baked,
                              MultiBufferSource buffers, float partialTick, int packedLight) {
        try {
            Field field = layer.getClass().getDeclaredField("renderer");
            field.setAccessible(true);
            Object renderer = field.get(layer);
            Class<?> dragonClass = Class.forName("by.dragonsurvivalteam.dragonsurvival.common.entity.DragonEntity");
            Class<?> modelClass = Class.forName("software.bernie.geckolib.cache.object.BakedGeoModel");
            RenderType eyes = RenderType.eyes(DracolichModelLoader.GLOW);
            int color = -1;
            for (Method method : renderer.getClass().getMethods()) {
                if (method.getName().equals("getRenderColor") && method.getParameterCount() == 3) {
                    Object nativeColor = method.invoke(renderer, dragon, partialTick, packedLight);
                    color = ((Number) nativeColor.getClass().getMethod("getColor").invoke(nativeColor)).intValue();
                    break;
                }
            }
            Method draw = renderer.getClass().getMethod("actuallyRender", PoseStack.class, dragonClass,
                    modelClass, RenderType.class, MultiBufferSource.class,
                    com.mojang.blaze3d.vertex.VertexConsumer.class, boolean.class,
                    float.class, int.class, int.class, int.class);
            draw.invoke(renderer, poses, dragon, baked, eyes, buffers, buffers.getBuffer(eyes),
                    true, partialTick, packedLight, OverlayTexture.NO_OVERLAY, color);
        } catch (ReflectiveOperationException | LinkageError | RuntimeException failure) {
            if (!warned) {
                warned = true;
                TransformationsMod.LOGGER.warn("Cave Dracolich emissive pass unavailable; base skin remains visible", failure);
            }
        }
    }
}
