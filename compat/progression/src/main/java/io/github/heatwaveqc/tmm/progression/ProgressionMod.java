package io.github.heatwaveqc.tmm.progression;

import io.github.heatwaveqc.tmm.progression.command.ProgressionCommands;
import io.github.heatwaveqc.tmm.progression.network.AbilityNetwork;
import io.github.heatwaveqc.tmm.progression.data.PathRegistry;
import io.github.heatwaveqc.tmm.progression.runtime.ProgressionEvents;
import io.github.heatwaveqc.tmm.progression.runtime.GildedRules;
import io.github.heatwaveqc.tmm.progression.runtime.GildedFishingRules;
import io.github.heatwaveqc.tmm.progression.runtime.TyzGildedBridge;
import io.github.heatwaveqc.tmm.progression.state.ProgressionAttachments;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(ProgressionMod.MOD_ID)
public final class ProgressionMod {
    public static final String MOD_ID = "tmm_progression";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public ProgressionMod(IEventBus modBus) {
        ProgressionAttachments.ATTACHMENTS.register(modBus);
        modBus.addListener(AbilityNetwork::register);
        NeoForge.EVENT_BUS.addListener(ProgressionCommands::register);
        NeoForge.EVENT_BUS.addListener((AddReloadListenerEvent e) -> e.addListener(new PathRegistry()));
        GildedRules.register();
        GildedFishingRules.register();
        TyzGildedBridge.register();
        ProgressionEvents.register();
    }
}
