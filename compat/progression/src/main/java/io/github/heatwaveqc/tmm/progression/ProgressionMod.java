package io.github.heatwaveqc.tmm.progression;

import io.github.heatwaveqc.tmm.progression.command.ProgressionCommands;
import io.github.heatwaveqc.tmm.progression.runtime.forms.FormSkillRegistration;
import io.github.heatwaveqc.tmm.progression.runtime.forms.FormSkillRuntime;
import net.neoforged.fml.ModList;
import io.github.heatwaveqc.tmm.progression.network.AbilityNetwork;
import io.github.heatwaveqc.tmm.progression.network.ProgressionMenuNetwork;
import io.github.heatwaveqc.tmm.progression.data.PathRegistry;
import io.github.heatwaveqc.tmm.progression.runtime.ProgressionEvents;
import io.github.heatwaveqc.tmm.progression.runtime.TyzMonkBridge;
import io.github.heatwaveqc.tmm.progression.runtime.NewbieMode;
import io.github.heatwaveqc.tmm.progression.runtime.VirtualElytra;
import io.github.heatwaveqc.tmm.progression.runtime.HunterBrewing;
import io.github.heatwaveqc.tmm.progression.runtime.StandPotionEligibility;
import io.github.heatwaveqc.tmm.progression.runtime.HunterItems;
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

    public ProgressionMod(IEventBus modBus, net.neoforged.fml.ModContainer container) {
        new io.github.heatwaveqc.tmm.progression.mxp.MxpMod(modBus,container);
        ProgressionAttachments.ATTACHMENTS.register(modBus);
        VirtualElytra.register(modBus);
        HunterItems.ITEMS.register(modBus);
        modBus.addListener(AbilityNetwork::register);
        modBus.addListener(io.github.heatwaveqc.tmm.progression.network.VisionNetwork::register);
        modBus.addListener(ProgressionMenuNetwork::register);
        NeoForge.EVENT_BUS.addListener(ProgressionCommands::register);
        NeoForge.EVENT_BUS.addListener(HunterBrewing::register);
        NeoForge.EVENT_BUS.addListener(StandPotionEligibility::serverStarted);
        NeoForge.EVENT_BUS.addListener(StandPotionEligibility::serverStopped);
        NeoForge.EVENT_BUS.addListener((AddReloadListenerEvent e) -> e.addListener(new PathRegistry()));
        io.github.heatwaveqc.tmm.progression.runtime.forms.ProviderFlags.register();
        ProgressionEvents.register();
        NewbieMode.register();
        TyzMonkBridge.register();
        // JAWR/Tyz are optional; only install experimental form prefabs/listeners
        // when both upstream mods are present. All 37 catalog skills are inactive
        // by default and effect-time eligibility still checks live JAWR state.
        if (ModList.get().isLoaded("tyzs_skills") && ModList.get().isLoaded("witchery")) {
            modBus.addListener(FormSkillRegistration::onSetup);
            NeoForge.EVENT_BUS.register(FormSkillRegistration.class);
            NeoForge.EVENT_BUS.register(FormSkillRuntime.class);
        }
    }
}
