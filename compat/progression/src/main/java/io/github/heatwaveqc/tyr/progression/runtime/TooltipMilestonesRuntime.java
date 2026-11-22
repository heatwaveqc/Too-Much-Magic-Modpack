package io.github.heatwaveqc.tyr.progression.runtime;

import io.github.heatwaveqc.tyr.progression.network.TooltipMilestoneNetwork;
import io.github.heatwaveqc.tyr.progression.state.ProgressionAttachments;
import io.github.heatwaveqc.tyr.progression.state.TooltipMilestones;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

/**
 * Server-only first-acquisition detection through real possessed inventory.
 * Handles normal crafting, gifts, trades and migrated save inventories without
 * trusting recipe unlocks, advancements, client packets, scoreboard tags or Newbie
 * status. Creative acquisition is not a security boundary in this modpack.
 */
public final class TooltipMilestonesRuntime {
    private TooltipMilestonesRuntime() {}

    private static final ResourceLocation STONE =
        ResourceLocation.fromNamespaceAndPath("projecte", "philosophers_stone");
    private static final ResourceLocation REPLICA =
        ResourceLocation.fromNamespaceAndPath("replication", "replica_ingot");
    private static final Map<UUID, TooltipMilestones> LAST = new HashMap<>();

    public static void register() {
        NeoForge.EVENT_BUS.addListener(TooltipMilestonesRuntime::login);
        NeoForge.EVENT_BUS.addListener(TooltipMilestonesRuntime::tick);
        NeoForge.EVENT_BUS.addListener(TooltipMilestonesRuntime::clonePlayer);
        NeoForge.EVENT_BUS.addListener(TooltipMilestonesRuntime::respawn);
        NeoForge.EVENT_BUS.addListener(TooltipMilestonesRuntime::dimension);
        NeoForge.EVENT_BUS.addListener(TooltipMilestonesRuntime::logout);
    }

    private static boolean contains(Container inventory, ResourceLocation itemId) {
        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            ItemStack stack = inventory.getItem(slot);
            if (!stack.isEmpty() && itemId.equals(BuiltInRegistries.ITEM.getKey(stack.getItem())))
                return true;
        }
        return false;
    }

    private static boolean owns(ServerPlayer player, ResourceLocation itemId) {
        // Includes main slots, armor and offhand; existing Ender Chest inventories
        // count as previously acquired, even when a save predates this feature.
        return contains(player.getInventory(), itemId)
            || contains(player.getEnderChestInventory(), itemId);
    }

    /** Called only by server hooks. Unlocked bits can never be unset. */
    public static void reconcile(ServerPlayer player, boolean force) {
        TooltipMilestones prior = player.getData(ProgressionAttachments.TOOLTIP_MILESTONES.get());
        TooltipMilestones next = prior.discover(owns(player, STONE), owns(player, REPLICA));
        if (next != prior) player.setData(ProgressionAttachments.TOOLTIP_MILESTONES.get(), next);
        TooltipMilestones published = LAST.put(player.getUUID(), next);
        if (force || !Objects.equals(next, published) || player.tickCount % 20 == 0)
            TooltipMilestoneNetwork.send(player, next.projecte(), next.replication());
    }

    private static void login(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) reconcile(player, true);
    }

    private static void tick(PlayerTickEvent.Post event) {
        if (event.getEntity() instanceof ServerPlayer player && player.tickCount % 20 == 0)
            reconcile(player, false);
    }

    private static void clonePlayer(PlayerEvent.Clone event) {
        if (!(event.getEntity() instanceof ServerPlayer current)
                || !(event.getOriginal() instanceof ServerPlayer previous)) return;
        current.setData(ProgressionAttachments.TOOLTIP_MILESTONES.get(),
            previous.getData(ProgressionAttachments.TOOLTIP_MILESTONES.get()));
        LAST.remove(current.getUUID());
    }

    private static void respawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) reconcile(player, true);
    }

    private static void dimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) reconcile(player, true);
    }

    private static void logout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) LAST.remove(player.getUUID());
    }
}
