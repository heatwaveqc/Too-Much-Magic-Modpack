package io.github.heatwaveqc.tmm.transformations;

import io.github.heatwaveqc.tmm.transformations.adapters.*;
import io.github.heatwaveqc.tmm.transformations.command.TransformationCommands;
import io.github.heatwaveqc.tmm.transformations.dracolich.DracolichDragonFirstEvents;
import io.github.heatwaveqc.tmm.transformations.dracolich.DracolichAppearanceEvents;
import io.github.heatwaveqc.tmm.transformations.network.DracolichVisualNetworking;
import io.github.heatwaveqc.tmm.transformations.lich.ArchlichResurrectionConfig;
import io.github.heatwaveqc.tmm.transformations.lich.ArchlichResurrectionEvents;
import io.github.heatwaveqc.tmm.transformations.mob.MobAllianceEvents;
import io.github.heatwaveqc.tmm.transformations.vampire.JAWRBottleInterop;
import io.github.heatwaveqc.tmm.transformations.storage.TransformationAttachments;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

/** Internal optional adapters owned by the single Progression NeoForge module. */
public final class TransformationsMod {
    public static final String MOD_ID = io.github.heatwaveqc.tyr.progression.ProgressionMod.MOD_ID;
    public static final Logger LOGGER = io.github.heatwaveqc.tyr.progression.ProgressionMod.LOGGER;

    private TransformationsMod() {}

    /** Register one time from ProgressionMod. */
    public static void register(IEventBus modBus, ModContainer container) {
        container.registerConfig(ModConfig.Type.SERVER, ArchlichResurrectionConfig.SPEC,
                "tyr_progression-archlich-server.toml");
        TransformationAttachments.ATTACHMENTS.register(modBus);
        modBus.addListener(DracolichVisualNetworking::register);
        ProbeRegistry.register(new JAWRVampireProbe());
        ProbeRegistry.register(new JAWRLycanthropeProbe());
        ProbeRegistry.register(new DragonSurvivalProbe());
        ProbeRegistry.register(new GoetyLichProbe());
        ProbeRegistry.register(new ForbiddenMagicLichProbe());
        NeoForge.EVENT_BUS.addListener(TransformationCommands::register);
        MobAllianceEvents.register();
        ArchlichResurrectionEvents.register();
        DracolichDragonFirstEvents.register();
        DracolichAppearanceEvents.register();
        // JAWR-only ordinary physical blood bridge; never match special JAWR bottles by item ID alone.
        JAWRBottleInterop.register();
        LOGGER.info("Progression internal native compatibility registered: five probes, JAWR bottles, two death arbitrators, mob alliances and Dracolich visuals");
    }
}
