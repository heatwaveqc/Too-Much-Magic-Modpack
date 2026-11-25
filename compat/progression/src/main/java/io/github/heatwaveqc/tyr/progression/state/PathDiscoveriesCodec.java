package io.github.heatwaveqc.tyr.progression.state;
import com.mojang.serialization.Codec;
public final class PathDiscoveriesCodec {
    private PathDiscoveriesCodec(){}
    public static final Codec<PathDiscoveries> CODEC=
        Codec.unboundedMap(Codec.STRING,Codec.STRING.listOf())
            .xmap(PathDiscoveries::new,PathDiscoveries::consumed);
}
