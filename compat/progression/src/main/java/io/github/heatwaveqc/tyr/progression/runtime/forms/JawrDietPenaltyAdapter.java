package io.github.heatwaveqc.tyr.progression.runtime.forms;

import io.github.heatwaveqc.tyr.progression.data.FoodDecisionResolver;
import io.github.heatwaveqc.tyr.progression.data.FoodPolicyRegistry;
import io.github.heatwaveqc.tyr.progression.data.NativeDietPolicy;
import io.github.heatwaveqc.tyr.progression.runtime.Selectors;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;

/**
 * JAWR 0.5.39 native vampire solid-food penalty interception.
 *
 * Only vetoes the *penalty predicate*, never the native item consumption or
 * status effects. Blood balance remains owned by JAWRBottleInterop and the
 * original native JAWR item handlers. The generic bloody tag is NOT a safe-
 * feeding permit and never credits any vampire blood.
 */
public final class JawrDietPenaltyAdapter {
    private JawrDietPenaltyAdapter() {}

    public static boolean suppress(LivingEntityUseItemEvent.Finish event) {
        if (!(event.getEntity() instanceof ServerPlayer player)
                || player.getServer() == null || !player.getServer().isSameThread())
            return false;
        ItemStack stack = event.getItem();
        if (stack.isEmpty()) return false;
        boolean edible = stack.get(DataComponents.FOOD) != null;
        boolean denied = NativeDietAdapters.decide(player, stack).outcome()
            == FoodDecisionResolver.Outcome.DENY;
        boolean recreational = stack.getUseAnimation() == UseAnim.DRINK
            && Selectors.item("#c:food/recreational", stack);
        boolean unlocked = recreational
            && (DietPermissions.permits(player,stack,"recreational_drink")
                || DietPermissions.permits(player,stack,"alcohol"));
        return NativeDietPolicy.suppressVampireFoodPenalty(
            JawrFormState.present(player,"vampire"), edible, denied,
            FoodPolicyRegistry.universallyPermitted(stack),
            Selectors.item("#tyr_progression:jawr_vampire_safe_foods",stack),
            recreational, unlocked);
    }
}
