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

    private static String dragonProfile(ServerPlayer player) {
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
                String profile = dragonProfile(player);
                if (profile != null) {
                    if (FoodPolicyRegistry.universallyPermitted(stack)) {
                        // Permission only; no fabricated baseline or extra
                        // effects/debits. The original item keeps its semantics.
                        return original;
                    }
                    var policy = FoodPolicyRegistry.evaluateSource(profile, stack);
                    if (policy.isPresent() && nativeResult != null) {
                        var nourishment = policy.orElseThrow();
                        return new FoodProperties(
                            nourishment.hunger(), nourishment.saturationModifier(),
                            original.canAlwaysEat(), original.eatSeconds(),
                            original.usingConvertsTo(), original.effects());
                    }
                }
                // Existing permitted-drink bridge remains independent of
                // nutrition classification, and only restores real food.
                return DietPermissions.dragonDrink(player, stack)
                    ? original : nativeResult;
            }
            if (entity.level().isClientSide
                    && io.github.heatwaveqc.tyr.progression.client.ClientDiet.allows(entity, stack)) {
                return original; // existing server-authorized beverage preview
            }
            return nativeResult;
        } finally {
            REENTRANT.remove();
        }
    }
}
