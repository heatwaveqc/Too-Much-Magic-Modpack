package io.github.heatwaveqc.tyr.progression.state;

import com.mojang.serialization.Codec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Local "first since joining" progress, separate from permanent paid MXP receipts. */
public record PathDiscoveries(Map<String,List<String>> consumed) {
    public static final Codec<PathDiscoveries> CODEC=
        Codec.unboundedMap(Codec.STRING,Codec.STRING.listOf())
            .xmap(PathDiscoveries::new,PathDiscoveries::consumed);
    public PathDiscoveries {
        var entries=new HashMap<String,List<String>>();
        consumed.forEach((key,value)->entries.put(key,List.copyOf(value)));
        consumed=Map.copyOf(entries);
    }
    public static PathDiscoveries empty(){return new PathDiscoveries(Map.of());}
    public boolean contains(String path,String item){
        return consumed.getOrDefault(path,List.of()).contains(item);
    }
    public PathDiscoveries discover(String path,String item){
        if(contains(path,item))return this;
        var rows=new HashMap<>(consumed);
        var history=new ArrayList<>(rows.getOrDefault(path,List.of()));
        if(history.size()>=65536)return this;
        history.add(item);rows.put(path,List.copyOf(history));
        return new PathDiscoveries(rows);
    }
    public PathDiscoveries clear(String path){
        if(!consumed.containsKey(path))return this;
        var rows=new HashMap<>(consumed);rows.remove(path);
        return new PathDiscoveries(rows);
    }
}
