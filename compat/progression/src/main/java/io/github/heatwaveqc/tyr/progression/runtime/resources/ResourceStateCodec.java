package io.github.heatwaveqc.tyr.progression.runtime.resources;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/** Game-only serialization adapter; immutable state remains pure Java. */
public final class ResourceStateCodec {
    private ResourceStateCodec() {}
    public static final Codec<ResourceState> CODEC=RecordCodecBuilder.create(instance->instance.group(
        Codec.unboundedMap(Codec.STRING,Codec.INT).fieldOf("balances").forGetter(ResourceState::balances),
        Codec.STRING.listOf().fieldOf("receipts").forGetter(ResourceState::receipts)
    ).apply(instance,ResourceState::new));
}
