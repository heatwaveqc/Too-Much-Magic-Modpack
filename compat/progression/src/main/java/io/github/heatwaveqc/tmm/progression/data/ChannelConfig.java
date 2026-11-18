package io.github.heatwaveqc.tmm.progression.data;

import com.google.gson.JsonObject;
import io.github.heatwaveqc.tmm.progression.model.Definition;
import io.github.heatwaveqc.tmm.progression.model.XpChannel;
import java.util.*;

/** Resolve all tree/channel bindings together. The table is a datapack resource. */
public record ChannelConfig(Map<String,XpChannel> channels,Map<String,XpChannel> paths) {
    public static final String TABLE="tmm_progression:_channels";
    public static ChannelConfig empty() { return new ChannelConfig(Map.of(),Map.of()); }
    public ChannelConfig {
        channels=Map.copyOf(channels); paths=Map.copyOf(paths);
    }
    public static ChannelConfig read(JsonObject table,Map<String,Definition.Path> definitions,
                                     Map<String,JsonObject> rawPaths) {
        if(table==null || !table.has("schema") || table.get("schema").getAsInt()!=1)
            throw new IllegalArgumentException("Missing schema-1 XP channel table");
        var entries=table.getAsJsonObject("channels");
        if(entries==null || entries.size()<1 || entries.size()>3)
            throw new IllegalArgumentException("Configure one to three XP channels");
        var channels=new HashMap<String,XpChannel>(); int magicCount=0;
        for(var row:entries.entrySet()) {
            var value=row.getValue().getAsJsonObject();
            String type=value.get("type").getAsString();
            Definition.Path source=null; double growth=1.0;
            if(type.equals("path_curve")) {
                source=definitions.get(value.get("source_path").getAsString());
                if(source==null) throw new IllegalArgumentException("Unknown channel source path");
            } else if(type.equals("mxp")) {
                if(++magicCount>1) throw new IllegalArgumentException("Only one MXP economy is allowed");
                growth=value.has("growth_factor")?value.get("growth_factor").getAsDouble():1.02;
            } else throw new IllegalArgumentException("Unknown XP channel type "+type);
            channels.put(row.getKey(),new XpChannel(row.getKey(),source,growth));
        }
        var bound=new HashMap<String,XpChannel>();
        for(var path:definitions.values()) {
            var raw=rawPaths.get(path.id());
            var channel=raw.has("xp_channel")?channels.get(raw.get("xp_channel").getAsString()):null;
            if(channel==null) throw new IllegalArgumentException("Missing/unknown xp_channel in "+path.id());
            if(!channel.earnsFrom(path.id()) &&
               (!path.activities().isEmpty() || path.milestones().stream().anyMatch(m -> m.xp()!=0 || m.points()!=0)))
                throw new IllegalArgumentException("Only a curve's designated source tree can award activity/milestone currency: "+path.id());
            if(!channel.magic() && !channel.earnsFrom(path.id()) &&
               (!path.rankXp().equals(channel.source().rankXp()) || path.pointsPerRank()!=channel.source().pointsPerRank()))
                throw new IllegalArgumentException("Shared curve trees must use their source rank thresholds: "+path.id());
            bound.put(path.id(),channel);
        }
        for(var channel:channels.values())
            if(!channel.magic() && bound.get(channel.source().id())!=channel)
                throw new IllegalArgumentException("Curve source must use its own channel: "+channel.id());
        return new ChannelConfig(channels,bound);
    }
}
