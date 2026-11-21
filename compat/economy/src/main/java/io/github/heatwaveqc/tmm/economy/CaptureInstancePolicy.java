package io.github.heatwaveqc.tmm.economy;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.neoforged.fml.ModList;

/** Capture and charm restrictions only; never intercept actual entity spawning, death, or loot generally. */
public final class CaptureInstancePolicy {
    private static final String DOLORIS = "neovitae:daemonium_doloris";
    private static final String NATIVE_CLASS = "com.breakinblocks.neovitae.common.entity.mob.DaemoniumDolorisEntity";
    private CaptureInstancePolicy() {}

    /** Unknown Foreman discriminator is fail-closed for this exact shared entity type. */
    public static boolean denyForeman(Entity entity) {
        if (!DOLORIS.equals(BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString())) return false;
        if (!entity.getClass().getName().equals(NATIVE_CLASS)
                || !ModList.get().getModContainerById("neovitae").map(c -> c.getModInfo().getVersion().toString().equals("1.1.53")).orElse(false)) return true;
        try {
            return !Boolean.FALSE.equals(entity.getClass().getMethod("isForeman").invoke(entity));
        } catch (ReflectiveOperationException | RuntimeException | LinkageError ex) {
            return true;
        }
    }

    public static boolean denyCharm(Entity entity) {
        return denyForeman(entity) || (ModList.get().isLoaded("apothic_spawners") && CapturingPolicy.blocked(entity.getType()));
    }
}
