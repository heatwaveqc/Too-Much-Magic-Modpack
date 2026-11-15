package io.github.heatwaveqc.tmm.transformations;

import io.github.heatwaveqc.tmm.transformations.adapters.ProbeRegistry;
import io.github.heatwaveqc.tmm.transformations.adapters.JAWRVampireProbe;
import io.github.heatwaveqc.tmm.transformations.adapters.VampirismFactionProbe;
import io.github.heatwaveqc.tmm.transformations.command.TransformationCommands;
import io.github.heatwaveqc.tmm.transformations.core.ProviderState;
import io.github.heatwaveqc.tmm.transformations.storage.TransformationAttachments;
import io.github.heatwaveqc.tmm.transformations.vampire.VampireChassisConfig;
import io.github.heatwaveqc.tmm.transformations.vampire.VampireFactionEvents;
import io.github.heatwaveqc.tmm.transformations.vampire.JAWRDefenseEvents;
import io.github.heatwaveqc.tmm.transformations.vampire.JAWRBottleInterop;
import io.github.heatwaveqc.tmm.transformations.vampire.VampireLifecycleEvents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Bootstraps read-only diagnostics; no upstream state is mutated here. */
@Mod(TransformationsMod.MOD_ID)
public final class TransformationsMod {
    public static final String MOD_ID = "tmm_transformations";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public TransformationsMod(IEventBus modBus, ModContainer container) {
        container.registerConfig(ModConfig.Type.SERVER, VampireChassisConfig.SPEC);
        TransformationAttachments.ATTACHMENTS.register(modBus);
        ProbeRegistry.register(new VampirismFactionProbe(ProviderState.VAMPIRISM_VAMPIRE));
        ProbeRegistry.register(new VampirismFactionProbe(ProviderState.VAMPIRISM_HUNTER));
        ProbeRegistry.register(new JAWRVampireProbe());
        NeoForge.EVENT_BUS.addListener(TransformationCommands::register);
        VampireFactionEvents.register();
        JAWRDefenseEvents.register();
        JAWRBottleInterop.register();
        VampireLifecycleEvents.register();
        LOGGER.warn("Experimental vampire chassis loaded; mutation, acquisition and cure are OFF by default. Death/appearance remain untouched.");
    }
}
