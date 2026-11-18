package io.github.heatwaveqc.tmm.progression.model;

import io.github.heatwaveqc.tmm.progression.core.MxpMath;

/** A named XP economy. The native curve is owned by one data-defined source tree. */
public record XpChannel(String id, Definition.Path source, double growth) {
    public XpChannel {
        if (id == null || !id.matches("[a-z0-9_.-]+:[a-z0-9_./-]+"))
            throw new IllegalArgumentException("Invalid channel ID");
        MxpMath.validateGrowth(growth);
    }
    public boolean magic() { return source == null; }
    public boolean earnsFrom(String path) { return source != null && source.id().equals(path); }
}
