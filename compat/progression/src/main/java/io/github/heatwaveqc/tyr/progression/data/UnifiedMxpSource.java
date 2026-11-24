package io.github.heatwaveqc.tyr.progression.data;

import com.google.gson.JsonObject;
import io.github.heatwaveqc.tyr.progression.model.XpChannel;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Configurable shared MXP from trusted server signals; not a second XP ledger. */
public record UnifiedMxpSource(String systemId,String category,String event,
                               String selector,BigDecimal nominal,boolean oncePerTarget) {
    public UnifiedMxpSource {
        if(!systemId.matches("[a-z0-9_.-]+:[a-z0-9_./-]+")||
           !category.matches("[a-z0-9_.-]+:[a-z0-9_./-]+")||
           !Set.of("advancement","animal_petted").contains(event)||
           !("*".equals(selector)||selector.matches("[a-z0-9_.-]+:[a-z0-9_./-]+")||
                   selector.matches("#[a-z0-9_.-]+:[a-z0-9_./-]+"))||
           nominal.signum()<=0||nominal.compareTo(new BigDecimal("1000000"))>0||
           (oncePerTarget && !"advancement".equals(event)))
            throw new IllegalArgumentException("Unsafe shared XP source");
    }
    public static List<UnifiedMxpSource> parse(JsonObject path,Map<String,XpChannel> systems) {
        if(!path.has("unified_mxp_sources"))return List.of();
        var result=new ArrayList<UnifiedMxpSource>();
        var seen=new HashSet<String>();
        for(var element:path.getAsJsonArray("unified_mxp_sources")){
            JsonObject config=element.getAsJsonObject();
            if(!Set.of("system","source","event","target","base_mxp","once_per_target").containsAll(config.keySet()))
                throw new IllegalArgumentException("Unknown shared XP source field");
            var award=new UnifiedMxpSource(config.get("system").getAsString(),
                config.get("source").getAsString(),config.get("event").getAsString(),
                config.get("target").getAsString(),config.get("base_mxp").getAsBigDecimal(),
                config.has("once_per_target") && config.get("once_per_target").getAsBoolean());
            var system=systems.get(award.systemId());
            if(system==null||!system.magic()||!seen.add(award.systemId()+"|"+award.category()))
                throw new IllegalArgumentException("Invalid or duplicate shared source "+award.category());
            result.add(award);
        }
        return List.copyOf(result);
    }
}
