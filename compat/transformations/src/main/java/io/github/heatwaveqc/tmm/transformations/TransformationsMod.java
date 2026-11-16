package io.github.heatwaveqc.tmm.transformations;

import io.github.heatwaveqc.tmm.transformations.adapters.*;
import io.github.heatwaveqc.tmm.transformations.command.TransformationCommands;
import io.github.heatwaveqc.tmm.transformations.dracolich.DracolichDragonFirstEvents;
import io.github.heatwaveqc.tmm.transformations.lich.ArchlichResurrectionConfig;
import io.github.heatwaveqc.tmm.transformations.lich.ArchlichResurrectionEvents;
import io.github.heatwaveqc.tmm.transformations.mob.MobAllianceEvents;
import io.github.heatwaveqc.tmm.transformations.core.NativeSpeciesJoinGuard;
import io.github.heatwaveqc.tmm.transformations.storage.TransformationAttachments;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** JAWR-native transformation diagnostics and guarded cross-provider hooks. */
@Mod(TransformationsMod.MOD_ID)
public final class TransformationsMod {
    public static final String MOD_ID = "tmm_transformations";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public TransformationsMod(IEventBus modBus, ModContainer container) {
        container.registerConfig(ModConfig.Type.SERVER, ArchlichResurrectionConfig.SPEC,
                "tmm_archlich-server.toml");
        TransformationAttachments.ATTACHMENTS.register(modBus);
        ProbeRegistry.register(new JAWRVampireProbe());
        ProbeRegistry.register(new JAWRLycanthropeProbe());
        ProbeRegistry.register(new DragonSurvivalProbe());
        ProbeRegistry.register(new GoetyLichProbe());
        ProbeRegistry.register(new ForbiddenMagicLichProbe());
        NativeSpeciesJoinGuard.registerProgressionJoinVeto();
        NeoForge.EVENT_BUS.addListener(TransformationCommands::register);
        MobAllianceEvents.register();
        ArchlichResurrectionEvents.register();
        DracolichDragonFirstEvents.register();
        LOGGER.info("JAWR-only transformation bridge: five native state probes, mob alliances, Archlich and Dracolich event arbitration registered; no TeamLapen physiology");
    }
}
