package io.github.heatwaveqc.tmm.progression.state;

import com.mojang.serialization.Codec;
import java.util.HashMap;
import java.util.Map;

/** Each configured mutation policy owns a separate persistent state. */
public record MutationLedger(Map<String,HunterMutationState> paths){
    public MutationLedger{paths=Map.copyOf(paths);}
    public static MutationLedger empty(){return new MutationLedger(Map.of());}
    public static Codec<MutationLedger> codec(){return Codec.unboundedMap(Codec.STRING,HunterMutationState.codec()).xmap(MutationLedger::new,MutationLedger::paths);}
    public MutationLedger with(String id,HunterMutationState state){
        var copy=new HashMap<>(paths);copy.put(id,state);return new MutationLedger(copy);
    }
}
