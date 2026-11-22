package io.github.heatwaveqc.tyr.progression.state;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * Game-only serialization adapter. The immutable milestone value object is
 * deliberately pure Java so the offline unit-test runtime can load it.
 */
public final class TooltipMilestonesCodec {
    private TooltipMilestonesCodec() {}

    public static final Codec<TooltipMilestones> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
            Codec.BOOL.fieldOf("projecte").forGetter(TooltipMilestones::projecte),
            Codec.BOOL.fieldOf("replication").forGetter(TooltipMilestones::replication)
        ).apply(instance, TooltipMilestones::new));
}
