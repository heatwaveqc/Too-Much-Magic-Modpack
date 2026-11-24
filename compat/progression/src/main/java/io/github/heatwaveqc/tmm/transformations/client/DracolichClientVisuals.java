package io.github.heatwaveqc.tmm.transformations.client;

import io.github.heatwaveqc.tmm.transformations.dracolich.DracolichAppearancePolicy;
import io.github.heatwaveqc.tmm.transformations.network.DracolichVisualState;
import java.util.Map;
import java.util.Optional;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.ModList;

/** A per-player decision on every render. Never writes a shared renderer override. */
public final class DracolichClientVisuals {
    private DracolichClientVisuals() { }

    public static boolean applies(Object dragon) {
        if (ModList.get().getModContainerById("dragonsurvival")
                .filter(mod -> mod.getModInfo().getVersion().toString().startsWith("2.0.74")).isEmpty()) return false;
        try {
            // Smithing/skin-editor fake players retain their own preview behavior.
            if (dragon.getClass().getField("overrideUUIDWithLocalPlayerForTextureFetch").getBoolean(dragon)) return false;
            Object owner = dragon.getClass().getMethod("getPlayer").invoke(dragon);
            if (!(owner instanceof Player player) || !DracolichVisualState.active(player.getUUID())) return false;
            if (player.getClass().getName().equals("by.dragonsurvivalteam.dragonsurvival.client.util.FakeClientPlayer")) return false;
            Class<?> provider = Class.forName("by.dragonsurvivalteam.dragonsurvival.common.capability.DragonStateProvider");
            Object state = provider.getMethod("getData", Player.class).invoke(null, player);
            boolean scoped = DracolichAppearancePolicy.supports(
                    state.getClass().getMethod("speciesId").invoke(state).toString(),
                    state.getClass().getMethod("bodyId").invoke(state).toString(),
                    state.getClass().getMethod("getModel").invoke(state).toString());
            if (!scoped) return false;
            Class<?> cache = Class.forName("software.bernie.geckolib.cache.GeckoLibCache");
            Map<?, ?> models = (Map<?, ?>) cache.getMethod("getBakedModels").invoke(null);
            Object model = models.get(DracolichModelLoader.MODEL);
            if (model == null) return false;
            Optional<?> torso = (Optional<?>) model.getClass().getMethod("getBone", String.class).invoke(model, "Torso");
            return torso.isPresent(); // An unsupported rig's marker never becomes an invisible dragon.
        } catch (ReflectiveOperationException | LinkageError | RuntimeException failure) {
            return false;
        }
    }
}
