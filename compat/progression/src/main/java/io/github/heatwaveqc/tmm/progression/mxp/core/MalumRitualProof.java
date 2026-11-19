package io.github.heatwaveqc.tmm.progression.mxp.core;

import java.util.UUID;
import java.util.Set;

/** First-only native commissioning and successful voluntarily attributable binding. */
public final class MalumRitualProof {
    private MalumRitualProof(){}
    public record Commission(UUID actor,UUID operation){
        public RitualRewards.Receipt receipt(String rite,boolean activated){
            String id=MalumRitualCatalog.riteFamily(rite);
            return actor!=null&&operation!=null&&activated&&id!=null?new RitualRewards.Receipt(id,operation,actor,true,false):null;
        }
    }
    public record Binding(UUID initiator,UUID operation,String effect){
        public boolean matches(String current){return initiator!=null&&operation!=null&&effect!=null&&effect.equals(current)&&MalumRitualCatalog.lookupGeas(effect)!=null;}
        public RitualRewards.Receipt receipt(UUID target,Set<UUID> nativeSacrifices,Set<UUID> optedIn,boolean applied,boolean cleared){
            if(target==null||!matches(effect)||!applied||!cleared||!nativeSacrifices.contains(target)||!(target.equals(initiator)||optedIn.contains(target)))return null;
            return new RitualRewards.Receipt(MalumRitualCatalog.geasFamily(effect),operation,target,true,false);
        }
    }
}
