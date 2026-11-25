package io.github.heatwaveqc.tyr.progression.admin;

import java.util.*;

/** Trusted engine extension point. Definitions are registered in code; target keys
 * are discovered from live data. No reflection, raw NBT writes or client actions.
 * The command adapter must call handlers on the authoritative server thread. */
public final class AdminCapabilityRegistry<T> {
    public enum Action {
        RESET(false), RECONCILE(false), GRANT(false), JOIN(false), LEAVE(false), SET(true), ADD(true), SUBTRACT(true), FILL(false), EMPTY(false);
        private final boolean amount;
        Action(boolean amount){this.amount=amount;}
        public boolean requiresAmount(){return amount;}
    }

    /** Pure preparation; commit runs synchronously only after explicit confirmation.
     * A handler must recheck its preconditions and use its owning state service.
     * Plans are ephemeral: never cache one across command invocations. */
    public record Change(String before, String after, Runnable commit) {
        public Change {
            Objects.requireNonNull(before); Objects.requireNonNull(after);
            Objects.requireNonNull(commit);
        }
    }

    public interface Handler<T> {
        String description();
        Collection<String> keys(T target);
        String inspect(T target,String key);
        /** Read-only, descriptive diagnostics; never fixes state. */
        List<String> validate(T target,String key);
        /** Exclude purely informational handlers from bulk validation. */
        default boolean diagnostic(){return true;}
        default Set<Action> actions() { return Set.of(); }
        default Change plan(T target,String key,Action action,long amount) {
            if(action.requiresAmount())throw new IllegalArgumentException("Numeric action unsupported");
            return plan(target,key,action);
        }
        default Change plan(T target,String key,Action action) {
            throw new IllegalArgumentException("Unsupported capability action");
        }
    }

    private final Map<String,Handler<T>> handlers = new TreeMap<>();
    public synchronized void register(String id,Handler<T> handler) {
        if (id == null || id.length()>128 || !id.matches("[a-z0-9_.-]+:[a-z0-9_./-]+"))
            throw new IllegalArgumentException("Invalid administrative capability ID");
        if (handlers.putIfAbsent(id,Objects.requireNonNull(handler)) != null)
            throw new IllegalArgumentException("Duplicate administrative capability " + id);
    }
    public synchronized List<String> ids() { return List.copyOf(handlers.keySet()); }
    public synchronized Handler<T> require(String id) {
        var handler=handlers.get(id);
        if(handler==null)throw new IllegalArgumentException("Unknown administrative capability " + id);
        return handler;
    }
    public List<String> keys(String id,T target) {
        return require(id).keys(target).stream().distinct().sorted().toList();
    }
    public record Finding(String capability,String key,String detail){}
    public record Diagnosis(int checked,int omitted,List<Finding> findings){}
    /** Discover each key set once; never perform O(n^2) re-discovery for a bulk scan.
     * Bounded per handler and total; exceptions become findings rather than hiding later checks. */
    public Diagnosis diagnose(T target,int perHandler,int total){
        if(perHandler<1||total<1||perHandler>total||total>4096)throw new IllegalArgumentException("Invalid diagnosis budget");
        var findings=new ArrayList<Finding>();int checked=0,omitted=0;
        for(String id:ids()){
            var handler=require(id);
            if(!handler.diagnostic())continue;
            List<String> keys;
            try{keys=keys(id,target);}
            catch(RuntimeException ex){findings.add(new Finding(id,"","Key discovery failed: "+ex.getClass().getSimpleName()));continue;}
            int count=Math.min(keys.size(),Math.min(perHandler,Math.max(0,total-checked)));
            omitted+=keys.size()-count;
            for(int i=0;i<count;i++){
                String key=keys.get(i);checked++;
                try{
                    for(String detail:handler.validate(target,key))
                        if(findings.size()<256)findings.add(new Finding(id,key,detail));
                }catch(RuntimeException ex){
                    if(findings.size()<256)findings.add(new Finding(id,key,"Validation failed: "+ex.getClass().getSimpleName()));
                }
            }
        }
        return new Diagnosis(checked,omitted,List.copyOf(findings));
    }
    private Handler<T> resolve(String id,T target,String key) {
        if(key==null || key.isBlank() || key.length()>1024 || key.chars().anyMatch(Character::isISOControl))
            throw new IllegalArgumentException("Invalid capability target key");
        var handler=require(id);
        if(!handler.keys(target).contains(key))
            throw new IllegalArgumentException("Unknown capability target " + key);
        return handler;
    }
    public String inspect(String id,T target,String key) {
        return resolve(id,target,key).inspect(target,key);
    }
    public List<String> validate(String id,T target,String key) {
        return List.copyOf(resolve(id,target,key).validate(target,key));
    }
    public Change plan(String id,T target,String key,Action action) {
        return plan(id,target,key,action,0);
    }
    public Change plan(String id,T target,String key,Action action,long amount) {
        if(amount<0 || !action.requiresAmount()&&amount!=0)
            throw new IllegalArgumentException("Invalid action amount");
        var handler=resolve(id,target,key);
        if(!handler.actions().contains(action))
            throw new IllegalArgumentException("Capability does not support " + action);
        return Objects.requireNonNull(handler.plan(target,key,action,amount));
    }
}
