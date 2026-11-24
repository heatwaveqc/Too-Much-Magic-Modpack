package io.github.heatwaveqc.tyr.progression.runtime.resources;

import java.util.*;

/** Per-player saved resource balances and bounded recent committed-event receipts. */
public record ResourceState(Map<String,Integer> balances,List<String> receipts) {
    public ResourceState {
        balances=Map.copyOf(balances); receipts=List.copyOf(receipts);
        if(balances.size()>ResourcePolicy.MAX_STORED || receipts.size()>ResourcePolicy.MAX_RECEIPTS
                || balances.entrySet().stream().anyMatch(e->!ResourcePolicy.validId(e.getKey())
                    || e.getValue()<0 || e.getValue()>ResourcePolicy.MAX)
                || receipts.stream().anyMatch(s->s.isBlank()||s.length()>360))
            throw new IllegalArgumentException("Invalid persisted Path resources");
    }
    public static ResourceState empty(){return new ResourceState(Map.of(),List.of());}
    public Optional<ResourceState> settle(List<ResourcePolicy.Change> changes,String receipt){
        if(changes.isEmpty() || (receipt!=null && (receipt.isBlank()||receipt.length()>360||receipts.contains(receipt))))
            return Optional.empty();
        return ResourcePolicy.apply(balances,changes).map(next->{
            if(receipt==null)return next.equals(balances)?this:new ResourceState(next,receipts);
            var recent=new ArrayList<>(receipts);
            recent.add(receipt);
            if(recent.size()>ResourcePolicy.MAX_RECEIPTS)recent.removeFirst();
            return new ResourceState(next,recent);
        });
    }
    public ResourceState reset(Set<String> ids){
        var next=new HashMap<>(balances);
        ids.forEach(next::remove);
        return next.equals(balances)?this:new ResourceState(next,receipts);
    }
}
