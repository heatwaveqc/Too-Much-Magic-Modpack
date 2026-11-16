package io.github.heatwaveqc.tmm.transformations.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.heatwaveqc.tmm.transformations.TransformationsMod;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

/**
 * Native armor masks use the native body UV layout. Draw that installed mesh
 * in our current bone pose for the armor pass; never map those masks over our
 * unrelated skin atlas. Equipment stats and native armor generation are untouched.
 */
public final class DracolichArmorRenderer {
    private static final ThreadLocal<Boolean> REENTERED = ThreadLocal.withInitial(() -> false);
    private static boolean warned;
    private DracolichArmorRenderer() { }

    public static boolean reentered() { return REENTERED.get(); }

    public static void render(Object layer, PoseStack poses, Object dragon, Object skeleton,
                              RenderType type, MultiBufferSource buffers, VertexConsumer buffer,
                              float partialTick, int light, int overlay) {
        try {
            Class<?> cache = Class.forName("software.bernie.geckolib.cache.GeckoLibCache");
            Map<?, ?> models = (Map<?, ?>) cache.getMethod("getBakedModels").invoke(null);
            Object nativeModel = models.get(ResourceLocation.fromNamespaceAndPath("dragonsurvival", "geo/dragon_model.geo.json"));
            if (nativeModel == null) throw new IllegalStateException("Native armor model missing");
            copyModelPose(skeleton, nativeModel);
            Class<?> dragonClass = Class.forName("by.dragonsurvivalteam.dragonsurvival.common.entity.DragonEntity");
            Class<?> modelClass = Class.forName("software.bernie.geckolib.cache.object.BakedGeoModel");
            Method render = layer.getClass().getMethod("render", PoseStack.class, dragonClass, modelClass,
                    RenderType.class, MultiBufferSource.class, VertexConsumer.class, float.class, int.class, int.class);
            REENTERED.set(true);
            try { render.invoke(layer, poses, dragon, nativeModel, type, buffers, buffer, partialTick, light, overlay); }
            finally { REENTERED.remove(); }
        } catch (ReflectiveOperationException | LinkageError | RuntimeException failure) {
            if (!warned) {
                warned = true;
                TransformationsMod.LOGGER.warn("Cave Dracolich native armor pass unavailable; equipment remains functional", failure);
            }
        }
    }

    public static void copyModelPose(Object skeleton, Object nativeModel) throws ReflectiveOperationException {
        Map<String, Object> source = index(skeleton), target = index(nativeModel);
        for (Map.Entry<String, Object> entry : source.entrySet()) {
            Object to = target.get(entry.getKey());
            if (to != null) copyPose(entry.getValue(), to);
        }
    }

    private static Map<String, Object> index(Object model) throws ReflectiveOperationException {
        Map<String, Object> out = new HashMap<>();
        for (Object bone : (List<?>) model.getClass().getMethod("topLevelBones").invoke(model)) indexBone(bone, out);
        return out;
    }

    private static void indexBone(Object bone, Map<String, Object> out) throws ReflectiveOperationException {
        out.put((String) bone.getClass().getMethod("getName").invoke(bone), bone);
        for (Object child : (List<?>) bone.getClass().getMethod("getChildBones").invoke(bone)) indexBone(child, out);
    }

    private static void copyPose(Object from, Object to) throws ReflectiveOperationException {
        Class<?> type = from.getClass();
        for (String channel : new String[]{"Rot", "Pos", "Scale"}) {
            for (String axis : new String[]{"X", "Y", "Z"}) {
                float value = ((Number) type.getMethod("get"+channel+axis).invoke(from)).floatValue();
                type.getMethod("set"+channel+axis, float.class).invoke(to, value);
            }
        }
        type.getMethod("setHidden", boolean.class).invoke(to, type.getMethod("isHidden").invoke(from));
        type.getMethod("setChildrenHidden", boolean.class).invoke(to, type.getMethod("isHidingChildren").invoke(from));
    }
}
