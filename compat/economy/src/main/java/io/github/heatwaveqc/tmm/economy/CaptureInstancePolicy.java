package io.github.heatwaveqc.tmm.economy;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.ModList;

/** Capture and charm restrictions only; never intercept actual entity spawning, death, or loot generally. */
public final class CaptureInstancePolicy {
    private static final String DOLORIS = "neovitae:daemonium_doloris";
    private static final String NATIVE_CLASS = "com.breakinblocks.neovitae.common.entity.mob.DaemoniumDolorisEntity";
    // Both versions have an exact native ABI audit; unknown releases remain fail-closed.
    private static boolean reviewedNeoVersion(String version) {
        return "1.1.53".equals(version) || "1.1.54".equals(version);
    }

    private CaptureInstancePolicy() {}

    /** Unknown Foreman discriminator is fail-closed for this exact shared entity type. */
    public static boolean denyForeman(Entity entity) {
        if (!DOLORIS.equals(BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString())) return false;
        if (!entity.getClass().getName().equals(NATIVE_CLASS)
                || !ModList.get().getModContainerById("neovitae").map(c -> reviewedNeoVersion(c.getModInfo().getVersion().toString())).orElse(false)) return true;
        try {
            return !Boolean.FALSE.equals(entity.getClass().getMethod("isForeman").invoke(entity));
        } catch (ReflectiveOperationException | RuntimeException | LinkageError ex) {
            return true;
        }
    }

    /** The typed fragment contains a registry ID, not the original entity instance. */
    public static boolean denyFragmentEgg(ResourceLocation id) {
        return ModList.get().isLoaded("apothic_spawners") && CapturingPolicy.blocked(id);
    }

    public static boolean denyCharm(Entity entity) {
        return denyForeman(entity) || (ModList.get().isLoaded("apothic_spawners") && CapturingPolicy.blocked(entity.getType()));
    }
}
