package io.github.heatwaveqc.tyr.progression.runtime.forms;

import io.github.heatwaveqc.tyr.progression.data.FoodDecisionResolver;
import io.github.heatwaveqc.tyr.progression.data.FoodPolicyRegistry;
import io.github.heatwaveqc.tyr.progression.data.NativeDietPolicy;
import io.github.heatwaveqc.tyr.progression.network.FoodPreviewWire;
import io.github.heatwaveqc.tyr.progression.runtime.GildedMechanics;
import java.util.HashMap;
import java.util.UUID;
import net.minecraft.world.item.component.CustomData;
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
        if (!server(player) || stack == null || stack.isEmpty())
            return FoodDecisionResolver.Decision.nativeResult();
        // Gilded's existing gate also covers provider-supplied food properties
        // on items with no default FOOD component. Strict/affinity rules
        // intentionally apply only to component-backed food.
        var decision = stack.get(DataComponents.FOOD) == null
            ? FoodPolicyRegistry.decideCustomUse(activeProfiles(player), stack)
            : FoodPolicyRegistry.decide(player, activeProfiles(player), stack);
        return NativeDietPolicy.intersect(decision,
            GildedMechanics.foodAllowed(player, stack));
    }

    /** One event gate, previously owned separately by the Gilded handler. */
    public static void register() {
        NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST, NativeDietAdapters::foodStart);
        NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST, NativeDietAdapters::foodTick);
        NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST, NativeDietAdapters::foodFinished);
        NeoForge.EVENT_BUS.addListener(NativeDietAdapters::foodStopped);
    }

    public static void foodStart(LivingEntityUseItemEvent.Start event) {
        if (!event.isCanceled() && event.getEntity() instanceof ServerPlayer player
                && decide(player, event.getItem()).outcome() == FoodDecisionResolver.Outcome.DENY)
            event.setCanceled(true);
    }

    private record Before(Object server, ItemStack stack, int hunger, float saturation, int tick) {}
    private static final Map<UUID, Before> BEFORE = new HashMap<>();

    /** Only an earned, otherwise off-diet beverage requires a zero-food lease. */
    public static boolean permissionOnlyDrink(ServerPlayer player, ItemStack stack) {
        if (!server(player) || stack == null || stack.isEmpty()
                || stack.get(DataComponents.FOOD) == null
                || stack.getUseAnimation() != net.minecraft.world.item.UseAnim.DRINK)
            return false;
        var decision = decide(player, stack);
        boolean eligible = DietPermissions.dragonDrink(player,stack)
            || JawrFormState.present(player,"vampire")
               && (DietPermissions.permits(player,stack,"alcohol")
                   ||DietPermissions.permits(player,stack,"recreational_drink"))
            || GildedMechanics.permissionOnlyDrink(player,stack);
        return NativeDietPolicy.permissionOnly(true,true,eligible,
            decision.outcome()==FoodDecisionResolver.Outcome.NUTRITION,
            FoodPolicyRegistry.universallyPermitted(stack),
            decision.outcome()==FoodDecisionResolver.Outcome.DENY);
    }

    public static void foodTick(LivingEntityUseItemEvent.Tick event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            if (event.isCanceled() || decide(player,event.getItem()).outcome()
                    == FoodDecisionResolver.Outcome.DENY) {
                BEFORE.remove(player.getUUID());
                if (!event.isCanceled()) event.setCanceled(true);
                return;
            }
            if (event.getDuration() <= 2 && permissionOnlyDrink(player,event.getItem())) {
                var food=player.getFoodData();
                BEFORE.put(player.getUUID(),new Before(player.getServer(),
                    event.getItem().copy(),food.getFoodLevel(),food.getSaturationLevel(),
                    player.tickCount));
            }
        }
    }

    /**
     * A provider may apply FoodData before posting Finish. Remove only the
     * *positive* hunger/saturation delta attributable to a completed permitted
     * drink. Native potion effects, bottles, cooldowns and blood remain intact.
     */
    public static void foodFinished(LivingEntityUseItemEvent.Finish event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        Before before=BEFORE.remove(player.getUUID());
        if (before==null || before.server()!=player.getServer()
                || player.tickCount-before.tick()<0 || player.tickCount-before.tick()>2
                || !ItemStack.isSameItemSameComponents(before.stack(),event.getItem())
                || !permissionOnlyDrink(player,event.getItem())) return;
        var food=player.getFoodData();
        int gain=Math.max(0,food.getFoodLevel()-before.hunger());
        float satGain=Math.max(0f,food.getSaturationLevel()-before.saturation());
        if(gain>0)food.setFoodLevel(Math.max(0,food.getFoodLevel()-gain));
        if(satGain>0)food.setSaturation(Math.max(0f,food.getSaturationLevel()-satGain));
    }

    public static void foodStopped(LivingEntityUseItemEvent.Stop event) {
        if (event.getEntity() instanceof ServerPlayer player) BEFORE.remove(player.getUUID());
    }
    public static void forget(ServerPlayer player) { BEFORE.remove(player.getUUID()); }

    /**
     * The client gets per-stack lava-caught gates, never a broad item-wide
     * denial waiver. A strict veto from another Path always wins.
     */
    public static Map<String, FoodPreviewWire.Entry> clientPreview(ServerPlayer player,
            Set<String> profiles) {
        if (!server(player)) return Map.of();
        Map<String, FoodPreviewWire.Entry> base =
            FoodPolicyRegistry.clientPreview(player, profiles);
        Map<String, FoodPreviewWire.Entry> projected = new HashMap<>(base);
        boolean gilded=GildedMechanics.hasFoodRestrictions(player);
        boolean conditional=GildedMechanics.hasPotentialStackException(player);
        for (ResourceLocation id : BuiltInRegistries.ITEM.keySet()) {
            ItemStack stack=BuiltInRegistries.ITEM.get(id).getDefaultInstance();
            if (stack.get(DataComponents.FOOD)==null) continue;
            String item=id.toString();
            FoodPreviewWire.Entry original=base.get(item);
            boolean strictDenied=original!=null && original.denied();
            if(gilded && !strictDenied && !GildedMechanics.foodAllowed(player,stack)) {
                if (conditional) {
                    ItemStack marked=stack.copy();
                    CustomData.update(DataComponents.CUSTOM_DATA, marked,
                        data->data.putBoolean("tyr_progression:lava_caught",true));
                    if(GildedMechanics.foodAllowed(player,marked)) {
                        projected.put(item,FoodPreviewWire.Entry.lavaConditional(original));
                        continue;
                    }
                }
                projected.put(item,FoodPreviewWire.Entry.deny());
            } else if(!strictDenied && permissionOnlyDrink(player,stack)) {
                projected.put(item,FoodPreviewWire.Entry.nourishment(0,0));
            }
        }
        return Map.copyOf(projected);
    }

    /** Active effect/rank/permission changes invalidate a client projection. */
    public static String clientFingerprint(ServerPlayer player) {
        return server(player) ? GildedMechanics.foodPolicyFingerprint(player) : "";
    }
}
