package io.github.heatwaveqc.tmm.transformations.adapters;

import io.github.heatwaveqc.tmm.transformations.core.Observation;
import io.github.heatwaveqc.tmm.transformations.core.ProviderState;
import net.minecraft.server.level.ServerPlayer;

/**
 * Optional, version-specific observer for one upstream condition.
 * Probes MUST be read-only and must not infer states from mod presence alone.
 */
public interface TransformationProbe {
    ProviderState state();
    Observation observe(ServerPlayer player);
}
