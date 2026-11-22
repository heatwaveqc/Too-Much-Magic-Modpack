package io.github.heatwaveqc.tyr.progression.client;

import io.github.heatwaveqc.tyr.progression.ProgressionMod;
import io.github.heatwaveqc.tyr.progression.foundation.TooltipLinePolicy;
import io.github.heatwaveqc.tyr.progression.foundation.TooltipLinePolicy.Kind;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

/**
 * End-of-pipeline, source-specific client tooltip filter. ProjectE runs at
 * NORMAL, Project Expansion at LOW, and this filter at LOWEST. Do not cancel
 * those handlers: they also add pedestal details and Alchemical Collection.
 *
 * Uses exact native translation roots, never localized text/regex. Replication
 * uses GatherComponents and its own right-side MatterTooltipComponent.
 * Divining Rod chat/system messages and machine operation are not touched.
 */
@EventBusSubscriber(modid = ProgressionMod.MOD_ID, value = Dist.CLIENT)
public final class TooltipValueHider {
    private TooltipValueHider() {}

    private static Kind classify(Component component) {
        if (component.getContents() instanceof TranslatableContents translatable) {
            Kind direct = TooltipLinePolicy.classify(translatable.getKey());
            if (direct != Kind.UNRELATED) return direct;
        }
        // Project Expansion may append a learned checkmark to the original
        // ProjectE translated EMC component. Preserve its root classification.
        for (Component sibling : component.getSiblings()) {
            Kind nested = classify(sibling);
            if (nested != Kind.UNRELATED) return nested;
        }
        return Kind.UNRELATED;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (stack.isEmpty()) return;
        if (event.getToolTip().stream().noneMatch(component ->
                classify(component) != Kind.UNRELATED)) return;
        Player owner = Minecraft.getInstance().player;
        // Ingredient viewers may fire with a null event entity. The current
        // local player (already bound to the server lease) remains the owner;
        // never use another entity's knowledge if the event names one.
        boolean correctOwner = owner != null
            && (event.getEntity() == null || owner == event.getEntity());
        boolean nativeKnows = correctOwner
            && ClientTooltipMilestones.mayShowProjecteLearned(true)
            && NativeProjecteKnowledge.hasKnowledge(owner, stack);
        boolean stone = ClientTooltipMilestones.mayShowProjecteLearned(true);
        boolean replica = ClientTooltipMilestones.mayShowReplicationMatter(true);
        event.getToolTip().removeIf(component ->
            TooltipLinePolicy.remove(classify(component), stone, nativeKnows, replica));
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onMatterComponents(RenderTooltipEvent.GatherComponents event) {
        if (event.getItemStack().isEmpty()) return;
        if (ClientTooltipMilestones.mayShowReplicationMatter(true)) return;
        event.getTooltipElements().removeIf(entry ->
            entry.right().map(value ->
                TooltipLinePolicy.removeReplicationComponent(value.getClass().getName(), false))
                .orElse(false)
            || entry.left().map(value -> value instanceof Component component
                && classify(component) == Kind.REPLICATION_HINT).orElse(false));
    }
}
