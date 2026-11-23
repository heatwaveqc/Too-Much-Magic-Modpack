package io.github.heatwaveqc.tyr.progression.runtime.forms;

import io.github.heatwaveqc.tyr.progression.data.FoodPolicyRegistry;
import io.github.heatwaveqc.tyr.progression.foundation.Condition;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;

/**
 * Post-native Dragon Survival 2.0.74 food selection, for player dragons only.
 *
 * A generated source receipt can replace nourishment without replacing the
 * native item's effects, container return, eating duration or use callbacks.
 * Unknown/provenance-free food keeps its native result; magical foods retain
 * their own actual food properties rather than being granted a generic bonus.
 */
public final class DragonDietHook {
    private DragonDietHook() {}

    private static final ThreadLocal<Boolean> REENTRANT = ThreadLocal.withInitial(() -> false);

    static String dragonProfile(ServerPlayer player) {
        if (player.getServer() == null || !player.getServer().isSameThread()) return null;
        String found = null;
        for (String species : new String[]{"cave", "forest", "sea"}) {
            if (ProviderFlags.dragonSpecies(player, "dragonsurvival:" + species + "_dragon")
                    != Condition.Result.PASS) continue;
            if (found != null) return null; // reject a contradictory native snapshot
            found = species + "_dragon";
        }
        return found;
    }

    private static FoodProperties noNourishment(FoodProperties food) {
        return new FoodProperties(0, 0f, food.canAlwaysEat(), food.eatSeconds(),
            food.usingConvertsTo(), food.effects());
    }

    public static FoodProperties restore(ItemStack stack, LivingEntity entity,
                                         FoodProperties nativeResult) {
        if (entity == null || stack.isEmpty() || REENTRANT.get()
                || ModList.get().getModContainerById("dragonsurvival")
                    .filter(c -> c.getModInfo().getVersion().toString().equals("2.0.74"))
                    .isEmpty()) return nativeResult;

        // Only component-backed foods can have safe replacement nutrition here.
        // Custom use handlers, potion-like items and native consumption of
        // special resources remain owned by their upstream mods.
        FoodProperties original = stack.get(DataComponents.FOOD);
        if (original == null) return nativeResult;

        REENTRANT.set(true);
        try {
            if (entity instanceof ServerPlayer player) {
                // This is the same decision as the independent server
                // Start/Tick consumption gate: Dragon affinity, active strict
                // Paths and Gilded stack restrictions cannot contradict.
                var decision = NativeDietAdapters.decide(player, stack);
                switch (decision.outcome()) {
                    case DENY -> { return null; }
                    case PERMIT_NATIVE -> { return original; }
                    case NUTRITION -> {
                        var nourishment = decision.nutrition();
                        return new FoodProperties(
                            nourishment.hunger(), nourishment.saturationModifier(),
                            original.canAlwaysEat(), original.eatSeconds(),
                            original.usingConvertsTo(), original.effects());
                    }
                    case NATIVE -> { } // Preserve native provider result.
                }
                // Acquired Taste permits use and native effects, never
                // off-diet FoodData nourishment by itself.
                return NativeDietAdapters.permissionOnlyDrink(player,stack)
                    ? noNourishment(original) : nativeResult;
            }
            if (entity.level().isClientSide) {
                var preview = io.github.heatwaveqc.tyr.progression.client.ClientDiet.preview(entity, stack);
                if (preview.present()) return preview.food(); // includes explicit DENY=null
                if (io.github.heatwaveqc.tyr.progression.client.ClientDiet.allows(entity, stack))
                    return noNourishment(original); // permission only
            }
            return nativeResult;
        } finally {
            REENTRANT.remove();
        }
    }
}
