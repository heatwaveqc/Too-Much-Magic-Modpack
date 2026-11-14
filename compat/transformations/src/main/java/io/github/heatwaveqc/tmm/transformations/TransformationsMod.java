package io.github.heatwaveqc.tmm.transformations;

import io.github.heatwaveqc.tmm.transformations.adapters.ProbeRegistry;
import io.github.heatwaveqc.tmm.transformations.adapters.JAWRVampireProbe;
import io.github.heatwaveqc.tmm.transformations.adapters.VampirismFactionProbe;
import io.github.heatwaveqc.tmm.transformations.command.TransformationCommands;
import io.github.heatwaveqc.tmm.transformations.core.ProviderState;
import io.github.heatwaveqc.tmm.transformations.storage.TransformationAttachments;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Bootstraps read-only diagnostics; no upstream state is mutated here. */
@Mod(TransformationsMod.MOD_ID)
public final class TransformationsMod {
    public static final String MOD_ID = "tmm_transformations";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public TransformationsMod(IEventBus modBus) {
        TransformationAttachments.ATTACHMENTS.register(modBus);
        ProbeRegistry.register(new VampirismFactionProbe(ProviderState.VAMPIRISM_VAMPIRE));
        ProbeRegistry.register(new VampirismFactionProbe(ProviderState.VAMPIRISM_HUNTER));
        ProbeRegistry.register(new JAWRVampireProbe());
        NeoForge.EVENT_BUS.addListener(TransformationCommands::register);
        LOGGER.warn("Transformation bridge read-only Vampirism and JAWR observers loaded; no transitions or death handling are enforced.");
    }
}
