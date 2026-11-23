package io.github.heatwaveqc.tyr.progression.compat.jade;

import io.github.heatwaveqc.tyr.progression.client.ClientTooltipMilestones;
import io.github.heatwaveqc.tyr.progression.client.NativeProjecteKnowledge;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import snownee.jade.api.Accessor;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;
import snownee.jade.api.ui.IBoxElement;

/**
 * Client-only optional Jade adapter for ProjectE 1.21.1's JadeDataProvider.
 *
 * ProjectE registers a provider with UID projecte:emc_provider, which adds
 * precisely one translated block valuation line. Jade automatically tags
 * provider elements with the UID; this post-collection callback removes only
 * that element, leaving all other Jade providers, block labels, stored matter,
 * machine status and hover information untouched.
 *
 * Jade discovers @WailaPlugin only when Jade itself is present; the annotation
 * also requires ProjectE before loading the class. The Jade dependency is
 * compile-only, so neither mod is required by Progression at runtime.
 */
@WailaPlugin("projecte")
public final class ProjecteEmcJadePlugin implements IWailaPlugin {

    private static final ResourceLocation PROJECTE_BLOCK_EMC =
        ResourceLocation.fromNamespaceAndPath("projecte", "emc_provider");

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        // Jade collects all regular providers before invoking these callbacks.
        // Run last so another plugin cannot re-add the ProjectE provider line.
        registration.addTooltipCollectedCallback(Integer.MAX_VALUE, ProjecteEmcJadePlugin::filter);
    }

    private static void filter(IBoxElement root, Accessor<?> accessor) {
        if (!(accessor instanceof BlockAccessor block)) return;
        ITooltip tooltip = root.getTooltip();
        // No broad search for "EMC" or rendered text; no other provider removed.
        if (tooltip.get(PROJECTE_BLOCK_EMC).isEmpty()) return;

        LocalPlayer local = Minecraft.getInstance().player;
        boolean correctViewer = local != null && local == accessor.getPlayer();
        // Jade's ProjectE provider prices the *block*, not its picked stack.
        // Check native knowledge of the block's own item identity. An unlearnable
        // block (no item) deliberately cannot reveal a personal item value.
        ItemStack blockItem = new ItemStack(block.getBlock().asItem());
        boolean nativeKnows = correctViewer
            && NativeProjecteKnowledge.hasKnowledge(local, blockItem);
        if (!correctViewer
            || !ClientTooltipMilestones.mayShowProjecteNumber(nativeKnows, true)) {
            tooltip.remove(PROJECTE_BLOCK_EMC);
        }
    }
}
