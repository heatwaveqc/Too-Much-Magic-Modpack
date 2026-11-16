package io.github.heatwaveqc.tmm.progression.runtime;

import io.github.heatwaveqc.tmm.progression.core.ProgressionApi;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.event.entity.player.ItemFishedEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Optional fishing-rod agnostic lava permission and action XP, no required external JAR.
 * In MC 1.21.1 both Aquaculture 2 and Nether Depths bobbers extend FishingHook and
 * fire ItemFishedEvent. Never count per loot stack: Aquaculture posts a separate
 * ItemFishedEvent for its "double catch", and Reliquified Artifacts adds bonus catches.
 */
public final class GildedFishingRules {
    private GildedFishingRules() {}
    public static final String PROVENANCE = "tmm_progression:lava_caught";
    private static final Map<UUID, Long> CREDITED_BOBBERS = new HashMap<>();
    private static final int TTL_TICKS = 1200;

    public static void register() {
        NeoForge.EVENT_BUS.addListener(GildedFishingRules::bobberTick);
        // Let most other fishing modifiers run before awarding one native class XP.
        NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST, GildedFishingRules::caught);
    }

    /** Only Gilded players may fish in lava, starting immediately on faction join.
     * Cave Dragon permission is separate future work, not implied by this method.
     */
    public static boolean allowed(ServerPlayer player) {
        return GildedRules.joined(player);
    }

    private static boolean inLava(FishingHook hook) {
        return hook.level().getFluidState(hook.blockPosition()).is(FluidTags.LAVA)
            || hook.level().getFluidState(hook.blockPosition().below()).is(FluidTags.LAVA);
    }

    private static void bobberTick(EntityTickEvent.Post event) {
        if (!(event.getEntity() instanceof FishingHook hook)
            || hook.level().isClientSide || hook.isRemoved() || !inLava(hook)) return;
        if (!(hook.getPlayerOwner() instanceof ServerPlayer player) || !allowed(player)) {
            // Aquaculture's secondary double-catch ignores ItemFishedEvent cancellation.
            // Discard before any valid lava bite/reel; event cancellation below is backup.
            hook.discard();
        }
    }

    private static void caught(ItemFishedEvent event) {
        FishingHook hook = event.getHookEntity();
        if (hook.level().isClientSide || !inLava(hook)) return;
        if (!(event.getEntity() instanceof ServerPlayer player)
            || hook.getPlayerOwner() != player || !allowed(player)) {
            event.setCanceled(true);
            hook.discard();
            return;
        }
        if (event.isCanceled() || event.getDrops().stream().allMatch(ItemStack::isEmpty)) return;

        // Food caught from lava is legal even when its vanilla item type is available
        // elsewhere (e.g. cooked cod/chicken). Avoid globally whitelisting those types.
        // The existing food policy inspects the stack's provenance marker at use time.
        for (ItemStack stack : event.getDrops()) {
            if (!stack.isEmpty() && stack.getFoodProperties(player) != null)
                CustomData.update(DataComponents.CUSTOM_DATA,stack,
                    nbt -> nbt.putBoolean(PROVENANCE,true));
        }

        long now = player.serverLevel().getGameTime();
        // Expire old hook UUIDs; duplicate reward events from the same cast never count.
        if (CREDITED_BOBBERS.size() > 1024)
            CREDITED_BOBBERS.entrySet().removeIf(e -> now - e.getValue() > TTL_TICKS);
        if (CREDITED_BOBBERS.putIfAbsent(hook.getUUID(),now) == null)
            ProgressionApi.recordExternal(player,"tmm_progression:gilded_lava_fish_caught");
    }

    public static boolean isLavaCaughtFood(ItemStack stack) {
        var component=stack.get(DataComponents.CUSTOM_DATA);
        return component != null && component.copyTag().getBoolean(PROVENANCE);
    }
}
