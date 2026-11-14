package io.github.heatwaveqc.tmm.transformations;

import io.github.heatwaveqc.tmm.transformations.command.TransformationCommands;
import io.github.heatwaveqc.tmm.transformations.storage.TransformationAttachments;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Bootstraps the compatibility bridge. No external mod states are mutated here.
 */
@Mod(TransformationsMod.MOD_ID)
public final class TransformationsMod {
    public static final String MOD_ID = "tmm_transformations";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public TransformationsMod(IEventBus modBus) {
        TransformationAttachments.ATTACHMENTS.register(modBus);
        NeoForge.EVENT_BUS.addListener(TransformationCommands::register);
        LOGGER.warn("Transformation bridge infrastructure loaded; upstream adapters and transition enforcement are NOT installed.");
    }
}
