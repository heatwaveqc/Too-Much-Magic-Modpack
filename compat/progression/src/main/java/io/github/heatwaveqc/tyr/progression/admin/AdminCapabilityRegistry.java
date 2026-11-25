package io.github.heatwaveqc.tyr.progression.admin;

import java.util.*;

/** Trusted engine extension point. Definitions are registered in code; target keys
 * are discovered from live data. No reflection, raw NBT writes or client actions.
 * The command adapter must call handlers on the authoritative server thread. */
public final class AdminCapabilityRegistry<T> {
    public enum Action { RESET, RECONCILE }

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
        default Set<Action> actions() { return Set.of(); }
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
        var handler=resolve(id,target,key);
        if(!handler.actions().contains(action))
            throw new IllegalArgumentException("Capability does not support " + action);
        return Objects.requireNonNull(handler.plan(target,key,action));
    }
}
