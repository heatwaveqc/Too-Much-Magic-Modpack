package io.github.heatwaveqc.tyr.progression;

import io.github.heatwaveqc.tyr.progression.command.ProgressionCommands;
import net.neoforged.fml.ModList;
import io.github.heatwaveqc.tyr.progression.network.AbilityNetwork;
import io.github.heatwaveqc.tyr.progression.network.ProgressionMenuNetwork;
import io.github.heatwaveqc.tyr.progression.data.PathRegistry;
import io.github.heatwaveqc.tyr.progression.runtime.ProgressionEvents;
import io.github.heatwaveqc.tyr.progression.runtime.NewbieMode;
import io.github.heatwaveqc.tyr.progression.runtime.VirtualElytra;
import io.github.heatwaveqc.tyr.progression.runtime.HunterBrewing;
import io.github.heatwaveqc.tyr.progression.runtime.StandPotionEligibility;
import io.github.heatwaveqc.tyr.progression.runtime.HunterItems;
import io.github.heatwaveqc.tyr.progression.state.ProgressionAttachments;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(ProgressionMod.MOD_ID)
public final class ProgressionMod {
    public static final String MOD_ID = "tyr_progression";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public ProgressionMod(IEventBus modBus, net.neoforged.fml.ModContainer container) {
        // Retired runtime providers must not silently suppress awards or open a
        // second skill-point wallet. Fail with an actionable startup error.
        if (ModList.get().isLoaded("tmm_mxp") || ModList.get().isLoaded("tyzs_skills")) {
            throw new IllegalStateException(
                    "Tyr Progression replaces tmm_mxp and Tyz's Skills; remove both retired JARs.");
        }
        new io.github.heatwaveqc.tyr.progression.mxp.MxpMod(modBus,container);
        ProgressionAttachments.ATTACHMENTS.register(modBus);
        VirtualElytra.register(modBus);
        HunterItems.ITEMS.register(modBus);
        modBus.addListener(AbilityNetwork::register);
        modBus.addListener(io.github.heatwaveqc.tyr.progression.network.DietNetwork::register);
        modBus.addListener(io.github.heatwaveqc.tyr.progression.network.VisionNetwork::register);
        modBus.addListener(ProgressionMenuNetwork::register);
        NeoForge.EVENT_BUS.addListener(ProgressionCommands::register);
        NeoForge.EVENT_BUS.addListener(HunterBrewing::register);
        NeoForge.EVENT_BUS.addListener(StandPotionEligibility::serverStarted);
        NeoForge.EVENT_BUS.addListener(StandPotionEligibility::serverStopped);
        NeoForge.EVENT_BUS.addListener((AddReloadListenerEvent e) -> e.addListener(new PathRegistry()));
        io.github.heatwaveqc.tyr.progression.runtime.forms.ProviderFlags.register();
        ProgressionEvents.register();
        NewbieMode.register();
    }
}
