package io.github.heatwaveqc.tyr.progression.runtime.forms;

import io.github.heatwaveqc.tyr.progression.data.FoodDecisionResolver;
import io.github.heatwaveqc.tyr.progression.data.FoodPolicyRegistry;
import io.github.heatwaveqc.tyr.progression.network.FoodPreviewWire;
import io.github.heatwaveqc.tyr.progression.runtime.GildedMechanics;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.fml.ModList;

/**
 * Server-owned boundary between shared food policy and existing native
 * item/Path use handlers. Does not own consumable effects or JAWR's blood pool.
 * No client request can add a form or activate a strict policy.
 */
public final class NativeDietAdapters {
    private NativeDietAdapters() {}

    private static boolean server(ServerPlayer p) {
        return p != null && p.getServer() != null && p.getServer().isSameThread();
    }

    /** Native species plus all DECLARED strict rules whose current eligibility passes. */
    public static Set<String> activeProfiles(ServerPlayer player) {
        if (!server(player)) return Set.of();
        Set<String> profiles = new TreeSet<>(FoodPolicyRegistry.activeStrictProfiles(player));
        if (ModList.get().getModContainerById("dragonsurvival")
                .filter(c -> c.getModInfo().getVersion().toString().equals("2.0.74"))
                .isPresent()) {
            String dragon = DragonDietHook.dragonProfile(player);
            if (dragon != null) profiles.add(dragon);
        }
        return Set.copyOf(profiles);
    }

    /**
     * Strict Path/form vetoes and Gilded's real stack-specific effects both
     * apply, so an affinity does not override either. A Gilded restriction's
     * lava-caught exception remains stack-based, never broadened into an item
     * tag or a globally allowed cooked-fish item.
     */
    public static FoodDecisionResolver.Decision decide(ServerPlayer player, ItemStack stack) {
        if (!server(player) || stack == null || stack.isEmpty()
                || stack.get(DataComponents.FOOD) == null)
            return FoodDecisionResolver.Decision.nativeResult();
        var decision = FoodPolicyRegistry.decide(activeProfiles(player), stack);
        if (decision.outcome() == FoodDecisionResolver.Outcome.DENY)
            return decision;
        if (!GildedMechanics.foodAllowed(player, stack))
            return FoodDecisionResolver.Decision.deny();
        return decision;
    }

    /** One event gate, previously owned separately by the Gilded handler. */
    public static void register() {
        NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST, NativeDietAdapters::foodStart);
        NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST, NativeDietAdapters::foodTick);
    }

    public static void foodStart(LivingEntityUseItemEvent.Start event) {
        if (!event.isCanceled() && event.getEntity() instanceof ServerPlayer player
                && decide(player, event.getItem()).outcome() == FoodDecisionResolver.Outcome.DENY)
            event.setCanceled(true);
    }

    public static void foodTick(LivingEntityUseItemEvent.Tick event) {
        if (!event.isCanceled() && event.getEntity() instanceof ServerPlayer player
                && decide(player, event.getItem()).outcome() == FoodDecisionResolver.Outcome.DENY)
            event.setCanceled(true);
    }

    /**
     * Snapshot the full server-relevant food decisions. A stack-specific
     * lava-caught exemption cannot be serialized as an item-wide DENY:
     * omit that Gilded-only denial rather than blocking a valid caught stack
     * on the client; the server event always enforces the actual stack.
     */
    public static Map<String, FoodPreviewWire.Entry> clientPreview(ServerPlayer player,
            Set<String> profiles) {
        if (!server(player)) return Map.of();
        Map<String, FoodPreviewWire.Entry> projected =
            new HashMap<>(FoodPolicyRegistry.clientPreview(profiles));
        if (GildedMechanics.hasFoodRestrictions(player)) {
            boolean conditional = GildedMechanics.hasPotentialStackException(player);
            for (ResourceLocation id : BuiltInRegistries.ITEM.keySet()) {
                ItemStack stack = BuiltInRegistries.ITEM.get(id).getDefaultInstance();
                if (stack.get(DataComponents.FOOD) == null) continue;
                if (!GildedMechanics.foodAllowed(player, stack)
                        && !conditional) projected.put(id.toString(), FoodPreviewWire.Entry.deny());
            }
        }
        return Map.copyOf(projected);
    }

    /** Active effect/rank/permission changes invalidate a client projection. */
    public static String clientFingerprint(ServerPlayer player) {
        return server(player) ? GildedMechanics.foodPolicyFingerprint(player) : "";
    }
}
